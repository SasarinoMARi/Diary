const LOG_SUBJECT = require('path').basename(__filename);
const sql = require('./sql');
const time = require('./time')

class AutoBlock {
    constructor() { }

    checkIsBlocked(ip, callback) {
        ip = ip.replace('\'', '\\\'');

        sql.query(`SELECT * FROM blacklist where address='${ip}'`, function (err, results, fields) {
            if (err) {
                console.log(`${LOG_SUBJECT}\terror fetching blacklist: ${err.sqlMessage}`);
                return;
            }

            if (results.length > 0) {
                callback.banned();
            } else {
                callback.success();
            }
        });
    }

    addIntoBlacklist(ip) {
        const addr = ip.replace('\'', '\\\'');
        const date = time().format("YYYY-MM-DD HH:mm:ss");
        var query = `INSERT INTO \`blacklist\` (last_connected, \`address\`) \
            VALUES ('${date}','${addr}')
            ON DUPLICATE KEY UPDATE last_connected='${date}', \`address\`='${addr}'`;

        sql.query(query, function(err, results, fields) {
            if (err) {
                // DUPLICATE 에러
                if (err.errno == 1062) 
                    return;

                console.log(err);
                return;
            }

            console.log(`${LOG_SUBJECT}\t사용자가 블랙리스트에 추가됨, addr`);
        });
    }
}

module.exports = new AutoBlock();