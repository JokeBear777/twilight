-- ----------------------------
-- Table structure for `interest`
-- ----------------------------
CREATE TABLE `interest` (
  `interest_id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`interest_id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;;


-- ----------------------------
-- Table structure for `info`
-- ----------------------------
CREATE TABLE `member_info` (
  `member_id` bigint NOT NULL AUTO_INCREMENT,
  `age` int DEFAULT NULL,
  `created_at` datetime(6) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `gender` varchar(255) DEFAULT NULL,
  `member_name` varchar(255) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `role` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`member_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;;


-- ----------------------------
-- Table structure for `interests`
-- ----------------------------
CREATE TABLE `member_interests` (
  `member_interests_id` bigint NOT NULL AUTO_INCREMENT,
  `interests_id` bigint NOT NULL,
  `member_id` bigint NOT NULL,
  PRIMARY KEY (`member_interests_id`),
  UNIQUE KEY `UKjevilj6j52msc8rtcom24a5gq` (`member_id`,`interests_id`),
  KEY `FKdpqdjyxx1pi89vr3l625vksjl` (`interests_id`),
  CONSTRAINT `FKdpqdjyxx1pi89vr3l625vksjl` FOREIGN KEY (`interests_id`) REFERENCES `interest` (`interest_id`),
  CONSTRAINT `FKsvkqin3crw99pagu9m2bs6qay` FOREIGN KEY (`member_id`) REFERENCES `member_info` (`member_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;;


-- ----------------------------
-- Table structure for `personality`
-- ----------------------------
CREATE TABLE `member_personality` (
  `member_personality_id` bigint NOT NULL AUTO_INCREMENT,
  `member_id` bigint NOT NULL,
  `personality_id` bigint NOT NULL,
  PRIMARY KEY (`member_personality_id`),
  UNIQUE KEY `UKaeupmbnsvd79r27iru0428omv` (`member_id`,`personality_id`),
  KEY `FKlw788k3pnwtyg83cc9gusv0u7` (`personality_id`),
  CONSTRAINT `FK9xlhkbb54m9cdwmfw91f88sej` FOREIGN KEY (`member_id`) REFERENCES `member_info` (`member_id`),
  CONSTRAINT `FKlw788k3pnwtyg83cc9gusv0u7` FOREIGN KEY (`personality_id`) REFERENCES `personality` (`personality_id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;;


-- ----------------------------
-- Table structure for `question`
-- ----------------------------
CREATE TABLE `member_question` (
  `member_question_id` bigint NOT NULL,
  `question` varchar(255) NOT NULL,
  `question_type` enum('CATEGORY','EMOTION','NATURAL') NOT NULL,
  `tag_id` bigint DEFAULT NULL,
  PRIMARY KEY (`member_question_id`),
  KEY `FKhyl2p9jodjrtbgd2fcaworvku` (`tag_id`),
  CONSTRAINT `FKhyl2p9jodjrtbgd2fcaworvku` FOREIGN KEY (`tag_id`) REFERENCES `tag` (`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;;


-- ----------------------------
-- Table structure for `mapping`
-- ----------------------------
CREATE TABLE `answer_tag_mapping` (
  `answer_tag_mapping_id` bigint NOT NULL AUTO_INCREMENT,
  `member_question_answer_id` bigint DEFAULT NULL,
  `tag_id` bigint DEFAULT NULL,
  PRIMARY KEY (`answer_tag_mapping_id`),
  UNIQUE KEY `UKerfhjxre8g63qtc5b7gcequ6p` (`member_question_answer_id`),
  KEY `FKtnbl6jjwc1lbbtq32lisjqub6` (`tag_id`),
  CONSTRAINT `FKmtbc1f32nnf8ydkstkt2iqin5` FOREIGN KEY (`member_question_answer_id`) REFERENCES `member_question_answer` (`member_question_answer_id`),
  CONSTRAINT `FKtnbl6jjwc1lbbtq32lisjqub6` FOREIGN KEY (`tag_id`) REFERENCES `tag` (`tag_id`)
) ENGINE=InnoDB AUTO_INCREMENT=791 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;;


-- ----------------------------
-- Table structure for `book`
-- ----------------------------
CREATE TABLE `book` (
  `book_id` bigint NOT NULL AUTO_INCREMENT,
  `author` varchar(255) DEFAULT NULL,
  `cover_image_url` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `description` text,
  `name` varchar(255) NOT NULL,
  `page_count` int DEFAULT NULL,
  `published_at` datetime(6) DEFAULT NULL,
  `publisher` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `category` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`book_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1296 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;;


-- ----------------------------
-- Table structure for `record`
-- ----------------------------
CREATE TABLE `book_record` (
  `book_record_id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `book_id` bigint NOT NULL,
  `member_id` bigint NOT NULL,
  PRIMARY KEY (`book_record_id`),
  KEY `FK1p9vtdlxl7400tl8nttah9kju` (`book_id`),
  KEY `FKf0plofh35lu8tyvk4xvc0l7n9` (`member_id`),
  CONSTRAINT `FK1p9vtdlxl7400tl8nttah9kju` FOREIGN KEY (`book_id`) REFERENCES `book` (`book_id`),
  CONSTRAINT `FKf0plofh35lu8tyvk4xvc0l7n9` FOREIGN KEY (`member_id`) REFERENCES `member_info` (`member_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;;


-- ----------------------------
-- Table structure for `tags`
-- ----------------------------
CREATE TABLE `book_tags` (
  `book_tags_id` bigint NOT NULL AUTO_INCREMENT,
  `book_id` bigint DEFAULT NULL,
  `tag_id` bigint DEFAULT NULL,
  PRIMARY KEY (`book_tags_id`),
  KEY `FKgrb34gudrjkbeew59bty9sh45` (`book_id`),
  KEY `FKbgb421y3jrqvhqjo5ktpthb1d` (`tag_id`),
  CONSTRAINT `FKbgb421y3jrqvhqjo5ktpthb1d` FOREIGN KEY (`tag_id`) REFERENCES `tag` (`tag_id`),
  CONSTRAINT `FKgrb34gudrjkbeew59bty9sh45` FOREIGN KEY (`book_id`) REFERENCES `book` (`book_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5180 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;;


-- ----------------------------
-- Table structure for `reading_record`
-- ----------------------------
CREATE TABLE `reading_record` (
  `reading_record_id` bigint NOT NULL AUTO_INCREMENT,
  `contents` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `book_record_id` bigint DEFAULT NULL,
  PRIMARY KEY (`reading_record_id`),
  KEY `FKr26dlitl57mdjen6imioq6fh1` (`book_record_id`),
  CONSTRAINT `FKr26dlitl57mdjen6imioq6fh1` FOREIGN KEY (`book_record_id`) REFERENCES `book_record` (`book_record_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- ----------------------------
-- Table structure for `recommendation`
-- ----------------------------
CREATE TABLE `recommendation` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `ai_answer` longtext NOT NULL,
  `book_id` bigint NOT NULL,
  `member_id` bigint NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- ----------------------------
-- Table structure for `tag`
-- ----------------------------
CREATE TABLE `tag` (
  `tag_id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `tag_type` enum('CATEGORY','EMOTION') NOT NULL,
  PRIMARY KEY (`tag_id`)
) ENGINE=InnoDB AUTO_INCREMENT=505 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- ----------------------------
-- Table structure for `member_question_answer`
-- ----------------------------
CREATE TABLE `member_question_answer` (
  `member_question_answer_id` bigint NOT NULL,
  `answer` varchar(255) DEFAULT NULL,
  `member_question_id` bigint DEFAULT NULL,
  PRIMARY KEY (`member_question_answer_id`),
  KEY `FK9c9s4h0bk6pt20dm5ovnyqpi4` (`member_question_id`),
  CONSTRAINT `FK9c9s4h0bk6pt20dm5ovnyqpi4` FOREIGN KEY (`member_question_id`) REFERENCES `member_question` (`member_question_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- ----------------------------
-- Table structure for `personality`
-- ----------------------------
CREATE TABLE `personality` (
  `personality_id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`personality_id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
