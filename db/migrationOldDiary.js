const sql = require('./sql.js');

sql.query(`SELECT * FROM days WHERE TEXT LIKE '2022년 %'`, function (error, results, fields) {
    if (error) {
        onError(error, res);
        return;
    }

    results.forEach(diary => {
        var lines = diary.text.split("\n");
        lines.shift();
        var result = lines.join("\n").trim();
        console.log(result);

        sql.query(`UPDATE days SET text=? where idx=?`, [result, diary.idx], 
            function(err, results, filed) {
                if (error) {
                    onError(error, res);
                    return;
                }
            });
    });
});