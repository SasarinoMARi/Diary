require("dotenv").config();
const sql = require('./sql.js');
const time = require('./time');
const blacklist = require('./AutoBlock');

const ORDER_DEFAULT = 0         // 일기 날짜순 정렬
const ORDER_LAST_MODIFY = 1     // 최종 수정일순 정렬
const ORDER_CREATED_AT = 2      // 등록일자순 정렬

// .env 파일의 KEY 값과 헤더로 전달받은 값 단순 비교.
function simpleAuth(conn, callback) {
    if (process.env.KEY == conn.token) {
        blacklist.checkIsBlocked(conn.ip, {
            success: function () {
                callback();
            },
            banned: function () {
                conn.unauthorize();
            }
        });
    } else {
        conn.unauthorize();
    }
    return false;
}

module.exports = {
    getDays: async function (conn) {
        simpleAuth(conn, () => {
            const limit = 20;
            const offset = conn.body.page ? conn.body.page * limit : 0;

            var orderBy;
            switch (conn.body.order_type) {
                case ORDER_LAST_MODIFY:
                    orderBy = "ORDER BY last_modify asc";
                case ORDER_CREATED_AT:
                    orderBy = "ORDER BY idx desc";
                case ORDER_DEFAULT:
                default:
                    orderBy = "ORDER BY date desc";
                    break;
            }

            sql.query(
                `SELECT * FROM days ${orderBy} LIMIT ${limit} OFFSET ${offset}`, function (error, results, fields) {
                    if (error) {
                        conn.internalError();
                        console.log(error);
                        return;
                    }

                    conn.send(results);
                });
        });
    },
    getDaysWithDate: async function (conn) {
        simpleAuth(conn, () => {
            if (!conn.headers.date) {
                conn.internalError();
                return;
            }

            let query = `SELECT * FROM days WHERE date='${conn.headers.date}'`;
            sql.query(query, function (error, results) {
                if (error) {
                    conn.internalError();
                    console.log(error);
                    return;
                }
                conn.send(results);
            });
        });
    },
    getRandomDay: async function (conn) {
        simpleAuth(conn, () => {

            var mode = conn.header.isFindingCorrectionTarget;
            var isFindingCorrectionTarget = "";
            if (mode) isFindingCorrectionTarget = "last_modify ASC,"

            console.log(`[GET RDAY]`)
            sql.query(
                `SELECT * FROM days ORDER BY ${isFindingCorrectionTarget} RAND() LIMIT 1`, function (error, results, fields) {
                    if (error) {
                        conn.internalError();
                        console.log(error);
                        return;
                    }
                    if (results.length == 0) {
                        conn.send({});    
                        return;
                    }
                    conn.send(results[0]);
                });
        });
    },
    getDayWithKeyword: async function (conn) {
        simpleAuth(conn, () => {

            var keyword = conn.body.keyword;
            if (!keyword) keyword = '';

            console.log(`[SEARCH DAY] keyword : ${keyword}`)
            sql.query(
                `SELECT * FROM days WHERE text like '%${keyword}%' ORDER BY date desc`, function (error, results, fields) {
                    if (error) {
                        conn.internalError();
                        console.log(error);
                        return;
                    }
                    if (results.length == 0) {
                        conn.send([]);
                    }
                    conn.send(results);
                });
        });
    },
    createDay: async function (conn) {
        simpleAuth(conn, () => {
            console.log(`[CREATE DAY] ${conn.body.date}`);

            if (!conn.body.feeling) conn.body.feeling = 'null';

            let query = `
            INSERT INTO days (date, text, feeling, last_modify) 
            VALUES ('${conn.body.date}',${sql.escape(conn.body.text)},${conn.body.feeling},'${time().format("YYYY-MM-DD")}')`;
            sql.query(query, function (error, results, fields) {
                if (error) {
                    conn.internalError();
                    console.log(error);
                    return;
                }

                conn.send({ insertId: results.insertId });
            });
        });
    },
    deleteDay: async function (conn) {
        simpleAuth(conn, () => {

            var id = conn.body.idx;
            if (!id) {
                conn.internalError();
                console.log({ sqlMessage: "id is undefined." });
                return;
            }

            console.log(`[DELETE DAY] IDX=${id}`);

            sql.query(`DELETE FROM days WHERE idx = '${id}'`, function (error, results) {
                if (error) {
                    console.log(error);
                    return;
                }

                conn.send(results);
            });
        });
    },
    modifyDay: async function (conn) {
        simpleAuth(conn, () => {
            console.log(`[MODIFY DAY] ${conn.body.date}`)

            if (!conn.body.feeling) conn.body.feeling = 'null';

            let query = `
            UPDATE days SET \`date\`='${conn.body.date}',  \`text\`=${sql.escape(conn.body.text)},
            feeling=${conn.body.feeling}, last_modify='${time().format("YYYY-MM-DD")}' WHERE idx='${conn.body.idx}'`;
            sql.query(query, function (error, results) {
                if (error) {
                    conn.internalError();
                    console.log(error);
                    return;
                }
                conn.send(results);
            });
        });
    }
}