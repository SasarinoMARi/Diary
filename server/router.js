const sql = require('./sql.js');
require("dotenv").config();
const time = require('./time');

// .env 파일의 KEY 값과 헤더로 전달받은 값 단순 비교.
function simpleAuth(req, res) {
    if(process.env.KEY == req.headers.key) return true;
    
    // 인증 실패시
    res.statusCode = 403;
    res.message = "인증 정보가 잘못되었습니다.";
    res.json();
    return false;
}

module.exports = {
    // 일기 목록 조회
    getDays : async function(req, res, next) {
        if(!simpleAuth(req, res)) return;

        sql.query(
            `SELECT date FROM days`, function (error, results, fields) {

            if (error) {
                onError(error);
                return;
            }
            
            var result = [];
            results.forEach(it => {
                result.push(it.date);
            });
            res.json(result);
        });
        
    },
    // 그 날의 일기 조회
    getDay : async function(req, res, next) {
        if(!simpleAuth(req, res)) return;

        var date = req.body.date;
        console.log(`[GET DAY] ${date}`)
        sql.query(
            `SELECT * FROM days WHERE date = '${date}'`, 
            function (error, results, fields) {
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
            }
        );        
    },
    // 일기 생성
    createDay: async function (req, res, next) {
        if(!simpleAuth(req, res)) return;
        
        var req_json = req.body;

        function buildQuery(requestJson) {
            // 파라미터 파싱
            var date = requestJson.date;
            var text = requestJson.text;
            var felling = requestJson.felling;
        
            // 전달받은 column에 대한 쿼리 생성
            var columns = "";
            var values = "";
            function append(column, value) {
                if(value != undefined) {
                    columns += `${column},`;
                    values += `"${value}",`;
                }
            }
            append("date", date);
            append("text", text);
            append("felling", felling);
            append("last_modify", time().format("YYYY-MM-DD"));
            
            // 꼬리 자르기
            if(columns.endsWith(",")) columns = columns.substring(0, columns.length-1);
            if(values.endsWith(",")) values = values.substring(0, values.length-1);
        
            // console.log(`Query Builded: (${columns}) = ${values}`);
        
            return [columns, values];
        }

        var qp = buildQuery(req_json);
        console.log(`[NEW DAY] ${req_json.date}`)

        sql.query(`INSERT INTO days (${qp[0]}) VALUES (${qp[1]})`, function (error, results, fields) {
            if (error) {
                onError(error, res);
                return;
            }

            res.json({insertId: results.insertId});
        });
    },
    deleteDay: async function (req, res, next) {
        if(!simpleAuth(req, res)) return;
        
        var req_json = req.body;
        var id = req_json.idx;
        if(id == undefined) {
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
        
        var req_json = req.body;
        var id = req_json.idx;
        if(id == undefined) {
            onError({sqlMessage: "id is undefined."}, res);
            return;
        }
        
        function buildQuery(requestJson) {
            // 파라미터 파싱
            var date = requestJson.date;
            var text = requestJson.text;
            var felling = requestJson.felling;
        
            // 전달받은 column에 대한 쿼리 생성
            var values = "";
            function append(column, value) {
                if(value != undefined) {
                    values += `${column}="${value}",`;
                }
            }
            append("date", date);
            append("text", text);
            append("felling", felling);
            append("last_modify", time().format("YYYY-MM-DD"));
            
            // 꼬리 자르기
            if(values.endsWith(",")) values = values.substring(0, values.length-1);
        
            // console.log(`Update Query Builded: ${values}`);
        
            return values;
        }
        var qp = buildQuery(req_json);

        console.log(`[MODIFY DAY] ${id}`);
        sql.query(`UPDATE days SET ${qp} WHERE idx='${id}'`, function (error, results) {
            if (error) {
                onError(error);
                return;
            }
            res.json(results);
        });
    }
}


function onError(error, res) {
    console.log(error);
    res.status(500).json({msg: error.sqlMessage});
}