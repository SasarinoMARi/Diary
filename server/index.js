require("dotenv").config();
const express = require('express');
const app = express();
const sql = require('./sql.js');
const router = require('./router.js');
const Connection = require("./Connection");
const blacklist = require("./AutoBlock");

/*
 * Timezone 설정
 */
const moment = require('moment');
require('moment-timezone');
moment.tz.setDefault("Asia/Seoul");
Date.prototype.toJSON = function(){ return moment(this).format("YYYY-MM-DD"); }

/*
 * Request Body를 json으로 파싱하기 위한 설정
 */
app.use(express.urlencoded({extended: true}));
app.use(express.json())

/*
 * API Endpoints
 */
app.post('/getDays', (req, res, next) => router.getDays(new Connection(req, res)));
app.get('/getDaysWithDate', (req, res, next) => router.getDaysWithDate(new Connection(req, res)));
app.get('/getRandomDay', (req, res, next) => router.getRandomDay(new Connection(req, res)));
app.post('/getDayWithKeyword', (req, res, next) => router.getDayWithKeyword(new Connection(req, res)));
app.post('/modifyDay', (req, res, next) => router.modifyDay(new Connection(req, res)));
app.post('/createDay', (req, res, next) => router.createDay(new Connection(req, res)));
app.post('/deleteDay', (req, res, next) => router.deleteDay(new Connection(req, res)))

app.use((req, res, next) => {
    const conn = new Connection(req, res);
    console.log(`잘못된 경로로 접근 요청됨 : ${req.originalUrl}`, conn.ip);
    blacklist.addIntoBlacklist(conn.getIpAddress());
});

var port = process.env.PORT;
var server = app.listen(port, function () {
    console.log(`Server has started on port ${port}`);
});

function onError(error) {
    console.log(error);
}