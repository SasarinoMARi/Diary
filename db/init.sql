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

CREATE TABLE `blacklist` (
	`idx` INT UNSIGNED NOT NULL AUTO_INCREMENT,
	`last_connected` DATETIME NOT NULL,
	`address` VARCHAR(64) NOT NULL,
	`description` TEXT DEFAULT NULL,
	PRIMARY KEY (`idx`),
	UNIQUE KEY (`address`)
)
COLLATE='utf8_general_ci';