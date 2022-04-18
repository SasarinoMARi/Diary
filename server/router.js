require("dotenv").config();
const sql = require('./sql.js');
const time = require('./time');

const ORDER_DEFAULT = 0         // 일기 날짜순 정렬
const ORDER_LAST_MODIFY = 1     // 최종 수정일순 정렬
const ORDER_CREATED_AT = 2      // 등록일자순 정렬

// .env 파일의 KEY 값과 헤더로 전달받은 값 단순 비교.
function simpleAuth(req, res) {
    if(process.env.KEY == req.headers.key) return true;
    
    res.statusCode = 403;
    res.message = "인증 정보가 잘못되었습니다.";
    res.json();
    return false;
}

module.exports = {
    getDays : async function(req, res, next) {
        if(!simpleAuth(req, res)) return;

        const limit = 20;
        const offset = req.body.page ? req.body.page * limit : 0;

        var orderBy;
        switch(req.body.order_type) {
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
                onError(error, res);
                return;
            }

            res.json(results);
        });
        
    },
    getDaysWithDate : async function(req, res, next) {
        if(!simpleAuth(req, res)) return;
        if(!req.headers.date) {
            res.statusCode = 500;
            res.send("");
            return;
        }

        let query = `SELECT * FROM days WHERE date='${req.headers.date}'`;
        sql.query(query, function (error, results) {
            if (error) {
                onError(error, res);
                return;
            }
            res.json(results);
        });   
    },
    getRandomDay : async function(req, res, next) {
        if(!simpleAuth(req, res)) return;

        var mode = req.header.isFindingCorrectionTarget;
        var isFindingCorrectionTarget = "";
        if(mode) isFindingCorrectionTarget = "last_modify ASC,"

        console.log(`[GET RDAY]`)
        sql.query(
            `SELECT * FROM days ORDER BY ${isFindingCorrectionTarget} RAND() LIMIT 1`, function (error, results, fields) {
                if (error) {
                    onError(error, res);
                    return;
                }
                if(results.length == 0)
                {
                    onError({sqlMessage: "no match."}, res);
                    return;
                }
                res.json(results[0]);
        });      
    },
    createDay: async function (req, res, next) {
        if(!simpleAuth(req, res)) return;
        console.log(`[CREATE DAY] ${req.body.date}`);
        
        if(!req.body.feeling) req.body.feeling = 'null';

        let query = `
            INSERT INTO days (date, text, feeling, last_modify) 
            VALUES ('${req.body.date}',${sql.escape(req.body.text)},${req.body.feeling},'${time().format("YYYY-MM-DD")}')`;
        sql.query(query, function (error, results, fields) {
            if (error) {
                onError(error, res);
                return;
            }

            res.json({insertId: results.insertId});
        });
    },
    deleteDay: async function (req, res, next) {
        if(!simpleAuth(req, res)) return;
        
        var id = req.body.idx;
        if(!id) {
            onError({sqlMessage: "id is undefined."}, res);
            return;
        }

        console.log(`[DELETE DAY] IDX=${id}`);
        
        sql.query(`DELETE FROM days WHERE idx = '${id}'`, function (error, results) {
            if (error) {
                console.log(error);
                return;
            }

            res.json(results);
        });
    },
    modifyDay : async function(req, res, next) {
        if(!simpleAuth(req, res)) return;
        console.log(`[MODIFY DAY] ${req.body.date}`)

        if(!req.body.feeling) req.body.feeling = 'null';
        
        let query = `
            UPDATE days SET \`date\`='${req.body.date}',  \`text\`=${sql.escape(req.body.text)},
            feeling=${req.body.feeling}, last_modify='${time().format("YYYY-MM-DD")}' WHERE idx='${req.body.idx}'`;
        sql.query(query, function (error, results) {
            if (error) {
                onError(error, res);
                return;
            }
            res.json(results);
        });
    }
}


function onError(error, res) {
    console.log(error);
    res.statusCode = 403;
    res.json({msg: error.sqlMessage});
}