ALTER table `ec_objects` add  `app_url` VARCHAR(256) DEFAULT NULL COMMENT '应用URL地址';
ALTER table `ec_objects` add  `cs_paths` VARCHAR(256) DEFAULT NULL COMMENT 'CS应用路径,多个地址用分号隔开';
ALTER table `ec_objects` add  `brower_types` VARCHAR(64) DEFAULT NULL COMMENT '使用浏览器类型:ie,chrome';


ALTER table ec_user add `pwd_expire_time` DATE DEFAULT '9999-12-31' COMMENT '密码过期时间';
ALTER table ec_user add `type` VARCHAR(64) COLLATE utf8_general_ci DEFAULT NULL;
ALTER table ec_user add `face_img` VARCHAR(20) COLLATE utf8_general_ci DEFAULT NULL COMMENT '人像路径';
ALTER table ec_user add `fingerprint` INTEGER(11) DEFAULT NULL COMMENT '指纹路径';