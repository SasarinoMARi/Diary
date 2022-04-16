require("dotenv").config();
const express = require('express');
const app = express();
const sql = require('./sql.js');
const router = require('./router.js');

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
app.post('/getDays', router.getDays);
app.post('/getDay', router.getDay);
app.post('/modifyDay', router.modifyDay);
app.post('/createDay', router.createDay);
app.post('/deleteDay', router.deleteDay)

var port = process.env.PORT;
var server = app.listen(port, function () {
    console.log(`Server has started on port ${port}`);
});

function onError(error) {
    console.log(error);
}