create database diary;

CREATE TABLE `days` (
	`idx` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '고유번호',
	`date` DATE NOT NULL COMMENT '일기 날짜',
	`text` TEXT NOT NULL COMMENT '일기 내용',
	`last_modify` DATE NULL DEFAULT NULL COMMENT '마지막 수정일',
	`feeling` TINYINT NULL DEFAULT NULL COMMENT '기분',
	PRIMARY KEY (`idx`)
)
COLLATE='utf8_general_ci';