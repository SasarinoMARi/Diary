// txt 파일을 db에 입력하는 스크립트

const sql = require('./sql.js');
const fs = require("fs");
const detector = require('detect-character-encoding');
const iconv = require('iconv-lite');
const time = require('./time');

const inputFolderPath = "./txt"; // txt 파일 넣어둘 폴더 경로

const files = fs.readdirSync(inputFolderPath);
var currentDate = time().format("YYYY-MM-DD");

function escape(str) {
    return str
        .replaceAll("\r\n", "\n")   // 개행 처리
        .replaceAll("\'", "\\\'")   // 작은 따옴표 처리
}

files.forEach(file => {
    const filePath = `${inputFolderPath}/${file}`;
    const buffer = fs.readFileSync(filePath);

    // EUC-KR과 UTF-8 중 하나로 랜덤하게 저장되어 있어서 인코딩 확인 후 올바르게 읽어들이는 작업
    const charset = detector(buffer);
    const content = iconv.decode(buffer, charset.encoding).toString();
    

    // 정규식으로 파일명에서 날짜 추출
    const pattern = /(\d{4})+년 (\d{1,2})+월 (\d{1,2})일.txt+/;
    const date = file.replace(pattern, '$1-$2-$3');

    sql.query(
        `INSERT INTO days (date, text, last_modify) VALUES ('${date}', '${escape(content)}', '${currentDate}')`,
        function(error, results, fields) {
            if(error) {
                console.log(error);
            }
            else {
                fs.unlinkSync(filePath); // 처리 완료된 파일은 삭제
            }
        }
    )
});

sql.end();