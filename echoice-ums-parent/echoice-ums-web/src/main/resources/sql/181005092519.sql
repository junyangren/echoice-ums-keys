/*
MySQL Backup
Source Server Version: 5.7.16
Source Database: casums_db
Date: 2018/10/5 09:25:19
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
--  Table structure for `cfg_result_code`
-- ----------------------------
DROP TABLE IF EXISTS `cfg_result_code`;
CREATE TABLE `cfg_result_code` (
  `code` varchar(6) CHARACTER SET gbk NOT NULL DEFAULT '' COMMENT '错误代码',
  `note` varchar(100) CHARACTER SET gbk DEFAULT NULL COMMENT '错误原因',
  `errmessages` varchar(128) CHARACTER SET gbk DEFAULT NULL COMMENT '错误原因',
  `type` varchar(6) CHARACTER SET gbk DEFAULT NULL COMMENT '错误类型：1:',
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `ec_accss_mode`
-- ----------------------------
DROP TABLE IF EXISTS `ec_accss_mode`;
CREATE TABLE `ec_accss_mode` (
  `accss_id` bigint(20) NOT NULL,
  `name` varchar(128) DEFAULT NULL,
  `alias` varchar(128) DEFAULT NULL,
  `status` varchar(4) DEFAULT NULL,
  `note` varchar(1024) DEFAULT NULL,
  `taxis` bigint(20) DEFAULT '99999',
  `op_time` date DEFAULT NULL,
  PRIMARY KEY (`accss_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `ec_cakey_order`
-- ----------------------------
DROP TABLE IF EXISTS `ec_cakey_order`;
CREATE TABLE `ec_cakey_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
  `order_id` varchar(32) NOT NULL DEFAULT '-1' COMMENT '工单号',
  `op_count` bigint(20) NOT NULL DEFAULT '0' COMMENT '操作数量',
  `op_type` varchar(2) NOT NULL DEFAULT '-1' COMMENT '操作类型:01,入库;02,领取;03,标记丢失;04,离职归还',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_user` varchar(64) NOT NULL DEFAULT '' COMMENT '创建用户',
  `op_time` datetime NOT NULL COMMENT '修改时间',
  `op_user` varchar(64) NOT NULL DEFAULT '-1' COMMENT '修改用户',
  `sign_pdf` varchar(256) DEFAULT NULL COMMENT '签名PDF路径',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
--  Table structure for `ec_cakey_order_detail`
-- ----------------------------
DROP TABLE IF EXISTS `ec_cakey_order_detail`;
CREATE TABLE `ec_cakey_order_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
  `order_id` varchar(32) NOT NULL DEFAULT '-1' COMMENT '工单号',
  `name` varchar(32) NOT NULL DEFAULT '-1' COMMENT '姓名',
  `idcard` varchar(32) NOT NULL DEFAULT '-1' COMMENT '身份证号',
  `hardware_sn` varchar(32) NOT NULL DEFAULT '-1' COMMENT 'key硬件介质SN',
  `op_type` varchar(2) NOT NULL DEFAULT '-1' COMMENT '操作类型:01,入库;02,发放;03,标记丢失;04,离职归还',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_user` varchar(64) NOT NULL DEFAULT '' COMMENT '创建用户',
  `op_time` datetime NOT NULL COMMENT '修改时间',
  `op_user` varchar(64) NOT NULL DEFAULT '-1' COMMENT '修改用户',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
--  Table structure for `ec_common_seq`
-- ----------------------------
DROP TABLE IF EXISTS `ec_common_seq`;
CREATE TABLE `ec_common_seq` (
  `pk_key` varchar(32) NOT NULL DEFAULT '',
  `pk_value` int(11) DEFAULT NULL,
  PRIMARY KEY (`pk_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `ec_group`
-- ----------------------------
DROP TABLE IF EXISTS `ec_group`;
CREATE TABLE `ec_group` (
  `group_id` bigint(20) NOT NULL,
  `name` varchar(128) DEFAULT NULL,
  `alias` varchar(128) DEFAULT NULL,
  `parent_id` bigint(20) DEFAULT NULL,
  `note` varchar(1024) DEFAULT NULL,
  `taxis` bigint(20) DEFAULT '99999',
  `type` varchar(8) DEFAULT NULL,
  `op_time` date DEFAULT NULL,
  `full_name` varchar(1024) DEFAULT NULL,
  `group_path` varchar(1024) DEFAULT NULL,
  `note2` varchar(512) DEFAULT NULL,
  `note3` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `ec_group_assignment`
-- ----------------------------
DROP TABLE IF EXISTS `ec_group_assignment`;
CREATE TABLE `ec_group_assignment` (
  `ga_id` bigint(20) NOT NULL,
  `group_id` bigint(20) DEFAULT NULL,
  `role_id` bigint(20) DEFAULT NULL,
  `op_time` date DEFAULT NULL,
  PRIMARY KEY (`ga_id`),
  KEY `FK_Relationship_12` (`role_id`),
  KEY `FK_Relationship_13` (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `ec_objects`
-- ----------------------------
DROP TABLE IF EXISTS `ec_objects`;
CREATE TABLE `ec_objects` (
  `obj_id` bigint(20) NOT NULL,
  `name` varchar(128) DEFAULT NULL,
  `alias` varchar(128) DEFAULT NULL,
  `type` varchar(30) DEFAULT NULL,
  `icon` varchar(512) DEFAULT NULL,
  `status` varchar(4) DEFAULT NULL,
  `parent_id` bigint(20) DEFAULT NULL,
  `note` varchar(1024) DEFAULT NULL,
  `taxis` bigint(20) DEFAULT '99999',
  `op_time` date DEFAULT NULL,
  `note2` varchar(512) DEFAULT NULL,
  `note3` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`obj_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `ec_operator`
-- ----------------------------
DROP TABLE IF EXISTS `ec_operator`;
CREATE TABLE `ec_operator` (
  `oper_id` bigint(20) NOT NULL,
  `obj_id` bigint(20) DEFAULT NULL,
  `accss_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`oper_id`),
  KEY `FK_Relationship_1` (`obj_id`),
  KEY `FK_Relationship_2` (`accss_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `ec_permission`
-- ----------------------------
DROP TABLE IF EXISTS `ec_permission`;
CREATE TABLE `ec_permission` (
  `pa_id` bigint(20) NOT NULL,
  `role_id` bigint(20) DEFAULT NULL,
  `oper_id` bigint(20) DEFAULT NULL,
  `op_time` date DEFAULT NULL,
  PRIMARY KEY (`pa_id`),
  KEY `FK_Relationship_11` (`oper_id`),
  KEY `FK_Relationship_3` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `ec_resource`
-- ----------------------------
DROP TABLE IF EXISTS `ec_resource`;
CREATE TABLE `ec_resource` (
  `resource_id` bigint(20) NOT NULL,
  `name` varchar(128) DEFAULT NULL,
  `alias` varchar(128) DEFAULT NULL,
  `type` varchar(30) DEFAULT NULL,
  `status` varchar(4) DEFAULT NULL,
  `parent_id` bigint(20) DEFAULT NULL,
  `taxis` bigint(20) DEFAULT '99999',
  `op_time` date DEFAULT NULL,
  PRIMARY KEY (`resource_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `ec_role`
-- ----------------------------
DROP TABLE IF EXISTS `ec_role`;
CREATE TABLE `ec_role` (
  `role_id` bigint(20) NOT NULL,
  `name` varchar(128) DEFAULT NULL,
  `alias` varchar(128) DEFAULT NULL,
  `status` varchar(4) DEFAULT NULL,
  `parent_id` bigint(20) DEFAULT NULL,
  `note` varchar(512) DEFAULT NULL,
  `taxis` bigint(20) DEFAULT '99999',
  `op_time` date DEFAULT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `ec_user`
-- ----------------------------
DROP TABLE IF EXISTS `ec_user`;
CREATE TABLE `ec_user` (
  `user_id` bigint(20) NOT NULL,
  `name` varchar(64) DEFAULT NULL,
  `alias` varchar(64) DEFAULT NULL,
  `job_number` varchar(64) DEFAULT NULL,
  `password` varchar(128) DEFAULT NULL,
  `email` varchar(128) DEFAULT NULL,
  `status` varchar(4) DEFAULT NULL,
  `note` varchar(512) DEFAULT NULL,
  `taxis` bigint(20) DEFAULT '99999',
  `op_time` date DEFAULT NULL,
  `mobile` varchar(64) DEFAULT NULL COMMENT '手机',
  `tel` varchar(32) DEFAULT NULL COMMENT '电话',
  `address` varchar(256) DEFAULT NULL COMMENT '地址',
  `qq` varchar(32) DEFAULT NULL COMMENT 'QQ',
  `wechat` varchar(32) DEFAULT NULL COMMENT '微信',
  `duty` varchar(512) DEFAULT NULL COMMENT '职责',
  `leader_id` int(11) DEFAULT NULL,
  `idcard` varchar(32) DEFAULT NULL COMMENT '身份证号',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `ec_users_assignmen`
-- ----------------------------
DROP TABLE IF EXISTS `ec_users_assignmen`;
CREATE TABLE `ec_users_assignmen` (
  `ua_id` bigint(20) NOT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `role_id` bigint(20) DEFAULT NULL,
  `op_time` date DEFAULT NULL,
  PRIMARY KEY (`ua_id`),
  KEY `FK_Relationship_5` (`user_id`),
  KEY `FK_Relationship_6` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `ec_user_cakey`
-- ----------------------------
DROP TABLE IF EXISTS `ec_user_cakey`;
CREATE TABLE `ec_user_cakey` (
  `id` bigint(20) NOT NULL COMMENT '自增主键ID',
  `idcard` varchar(32) NOT NULL DEFAULT '-1' COMMENT '身份证号',
  `hardware_sn` varchar(32) NOT NULL DEFAULT '-1' COMMENT 'key硬件介质SN',
  `status` varchar(2) NOT NULL DEFAULT '-1' COMMENT '状态:01,未领取;02,已领取;03,已丢失;04,离职归还',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_user` varchar(64) NOT NULL DEFAULT '' COMMENT '创建用户',
  `op_time` datetime NOT NULL COMMENT '修改时间',
  `op_user` varchar(64) NOT NULL DEFAULT '-1' COMMENT '修改用户',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
--  Table structure for `ec_user_extend`
-- ----------------------------
DROP TABLE IF EXISTS `ec_user_extend`;
CREATE TABLE `ec_user_extend` (
  `user_id` bigint(20) NOT NULL,
  `mobile` varchar(64) DEFAULT NULL,
  `tel` varchar(64) DEFAULT NULL,
  `address` varchar(256) DEFAULT NULL,
  `qq` varchar(64) DEFAULT NULL,
  `msn` varchar(64) DEFAULT NULL,
  `duty` varchar(256) DEFAULT NULL,
  `leader_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `ec_user_group`
-- ----------------------------
DROP TABLE IF EXISTS `ec_user_group`;
CREATE TABLE `ec_user_group` (
  `ug_id` bigint(20) NOT NULL,
  `group_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ug_id`),
  KEY `FK_Relationship_10` (`group_id`),
  KEY `FK_Relationship_8` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  View definition for `ec_l2_cakey_view`
-- ----------------------------
DROP VIEW IF EXISTS `ec_l2_cakey_view`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `ec_l2_cakey_view` AS select `tb`.`reportTime` AS `reportTime`,`tb`.`status` AS `status`,count(1) AS `total` from (select `ta`.`op_type` AS `status`,date_format(`ta`.`create_time`,'%Y-%m-%d') AS `reportTime` from `casums_db`.`ec_cakey_order_detail` `ta`) `tb` group by `tb`.`reportTime`,`tb`.`status`;

-- ----------------------------
--  Records 
-- ----------------------------
INSERT INTO `cfg_result_code` VALUES ('404','404','',''), ('500','服务异常','','');
INSERT INTO `ec_accss_mode` VALUES ('2','可视','view','y','ok','200','2018-09-29'), ('115','编辑','edit','y','edit','300','2018-09-29'), ('119','特权操作','super','y','','300','2018-10-04');
INSERT INTO `ec_cakey_order` VALUES ('11','2018100123592200004','1','02','2018-10-01 23:59:22','admin','2018-10-01 23:59:22','admin',NULL), ('12','2018100200002800005','1','02','2018-10-02 00:00:28','admin','2018-10-02 00:00:28','admin',NULL), ('13','2018100200015500006','1','02','2018-10-02 00:01:55','admin','2018-10-02 23:43:21','admin','c:\\\\2018\\10\\02\\20181002114321-0001.pdf'), ('14','2018100200022700007','1','03','2018-10-02 00:02:27','admin','2018-10-02 00:02:27','admin',NULL), ('15','2018100200033000008','1','02','2018-10-02 00:03:30','admin','2018-10-02 00:03:30','admin',NULL), ('16','2018100200033600009','1','04','2018-10-02 00:03:37','admin','2018-10-02 00:03:37','admin',NULL), ('17','2018100200044600010','1','02','2018-10-02 00:04:46','admin','2018-10-02 00:04:46','admin',NULL), ('18','2018100200045000011','1','04','2018-10-02 00:04:50','admin','2018-10-02 00:04:50','admin',NULL), ('19','2018100314210800001','1','02','2018-10-03 14:21:08','admin','2018-10-03 14:21:08','admin',NULL), ('20','2018100410562600001','4','02','2018-10-04 10:56:26','admin','2018-10-04 10:56:26','admin',NULL), ('22','2018100412071400002','4','01','2018-10-04 12:07:15','admin','2018-10-04 12:07:15','admin',NULL);
INSERT INTO `ec_cakey_order_detail` VALUES ('17','2018100123592200004','天三','350583197210211005','a00005','02','2018-10-01 23:59:22','admin','2018-10-01 23:59:22','admin'), ('18','2018100200002800005','天二','350583197210211004','a00004','02','2018-10-02 00:00:28','admin','2018-10-02 00:00:28','admin'), ('19','2018100200015500006','天一','350583197210211003','a00003','02','2018-10-02 00:01:55','admin','2018-10-02 00:01:55','admin'), ('20','2018100200022700007','天一','350583197210211003','a00003','03','2018-10-02 00:02:27','admin','2018-10-02 00:02:27','admin'), ('21','2018100200033000008','小李','350583197210211002','a00002','02','2018-10-02 00:03:30','admin','2018-10-02 00:03:30','admin'), ('22','2018100200033600009','小李','350583197210211002','a00002','04','2018-10-02 00:03:37','admin','2018-10-02 00:03:37','admin'), ('23','2018100200044600010','小张','350583197210211001','a00001','02','2018-10-02 00:04:46','admin','2018-10-02 00:04:46','admin'), ('24','2018100200045000011','小张','350583197210211001','a00001','04','2018-10-02 00:04:50','admin','2018-10-02 00:04:50','admin'), ('25','2018100314210800001','天三','350583197210211005','c00005','02','2018-10-03 14:21:08','admin','2018-10-03 14:21:08','admin'), ('26','2018100410562600001','小李','350583197210211002','b00002','02','2018-10-04 10:56:26','admin','2018-10-04 10:56:26','admin'), ('27','2018100410562600001','天一','350583197210211003','b00003','02','2018-10-04 10:56:26','admin','2018-10-04 10:56:26','admin'), ('28','2018100410562600001','天二','350583197210211004','b00004','02','2018-10-04 10:56:26','admin','2018-10-04 10:56:26','admin'), ('29','2018100410562600001','天三','350583197210211005','b00005','02','2018-10-04 10:56:26','admin','2018-10-04 10:56:26','admin'), ('34','2018100412071400002','小张','350583197210211001','c00001','01','2018-10-04 12:07:15','admin','2018-10-04 12:07:15','admin'), ('35','2018100412071400002','小李','350583197210211002','c00002','01','2018-10-04 12:07:15','admin','2018-10-04 12:07:15','admin'), ('36','2018100412071400002','天一','350583197210211003','c00003','01','2018-10-04 12:07:15','admin','2018-10-04 12:07:15','admin'), ('37','2018100412071400002','天二','350583197210211004','c00004','01','2018-10-04 12:07:15','admin','2018-10-04 12:07:15','admin');
INSERT INTO `ec_common_seq` VALUES ('ec_cakey_order','23'), ('ec_cakey_order_detail','38'), ('ec_user_cakey','52'), ('ic_app_info','60'), ('ums_ec_accss_mode','120'), ('ums_ec_group','126'), ('ums_ec_group_assignment','115'), ('ums_ec_objects','251'), ('ums_ec_operator','193'), ('ums_ec_permission','177'), ('ums_ec_resource','110'), ('ums_ec_role','118'), ('ums_ec_user','151'), ('ums_ec_users_assignmen','122'), ('ums_ec_user_group','147');
INSERT INTO `ec_group` VALUES ('1','UMS系统用户组','1','-1','','1','01','2015-04-30','UMS系统用户组','1','',''), ('110','平台用户组','110','-1','','2','01','2018-09-29','平台用户组','110','',''), ('112','开发组','110-112','110','','2','01','2015-04-30','VC用户组/开发组','110-112','',''), ('113','维护组','110-113','110','','1','01','2015-04-30','VC用户组/维护组','110-113','',''), ('114','商务组','110-114','110','','3','01','2015-08-06','智信公司用户组/商务组','110-114','',''), ('119','test','110-113-119','113','','9999','01','2018-09-28','VC用户组/维护组/test','110-113-119','',''), ('120','福建医科大学附属第一医院','120','-1','','9999','01','2018-10-01','福建医科大学附属第一医院','120','',''), ('121','脑外科','120-121','120','','9999','01','2018-10-01','福建医科大学附属第一医院/脑外科','120-121','',''), ('122','神经科','120-122','120','','9999','','2018-10-01','福建医科大学附属第一医院/神经科','120-122','','');
INSERT INTO `ec_objects` VALUES ('1','UMS管理系统','ec-ums','ecums',NULL,'y','-1','ec-ums权限','1','2015-04-30','',''), ('2','资源操作管理','obj_accordion','ecums',NULL,'y','1','','100','2018-09-28','',''), ('3','用户组织管理','user_accordion','ecums',NULL,'y','1','','200','2018-09-28','',''), ('4','权限分配管理','per_accordion','ecums',NULL,'y','1','','297','2018-09-28','',''), ('5','资源管理','resource_menu','ecums',NULL,'y','2','{\"url\":\"/objects/index\"}','100','2018-09-28','',''), ('6','操作管理','opt_menu','ecums',NULL,'y','2','{\"url\":\"/accssMode/index\"}','200','2018-09-28','',''), ('7','用户组管理','group_menu','ecums',NULL,'y','3','{\"url\":\"/group/index\"}','100','2018-09-28','',''), ('8','用户管理','user_menu','ecums',NULL,'y','3','{\"url\":\"/user/index\"}','200','2018-09-28','',''), ('9','角色管理','role_menu','ecums',NULL,'y','4','{\"url\":\"/role/index\"}','100','2018-09-29','',''), ('10','权限检索','role_search','ecums',NULL,'y','4','','200','2018-09-28','',''), ('110','后台菜单管理','console-menu','ecums',NULL,'y','-1','','2','2015-08-06','',''), ('113','业务管理','c-business-menu','menu',NULL,'y','110','','1','2018-10-01','Hui-iconfont-order',''), ('114','业务参数管理','bs-param-cfg','ecums',NULL,'y','-1','','3','2015-08-06','',''), ('146','统计报表','c-report-menu','menu',NULL,'y','110','','20','2017-10-09','Hui-iconfont-tongji-zhu',''), ('167','基础配置','c-param-menu','menu',NULL,'y','110','','30','2017-10-12','Hui-iconfont-manage',''), ('168','错误码配置','c-param-resultcode-menu','ecums',NULL,'y','167','{url:\'/console/resultCode\'}','11','2017-09-30','',''), ('171','应用管理','c-appInfo-manage-menu','menu',NULL,'y','113','{url:\'/console/appInfo\'}','100','2017-10-10','',''), ('174','应用系统','c-app-sys','ecums',NULL,'y','110','','40','2017-10-09','Hui-iconfont-tongji',''), ('175','统一权限','c-app-ecums','ecums',NULL,'y','174','{url:\'http://192.168.21.60:6001/ecums/\'}','99999','2017-09-30','',''), ('221','认证演示','c-identity-demo','ecums',NULL,'y','113','{\"url\":\"/console/identityDemo\"}','600','2017-10-09','',''), ('222','认证查询','c-interface-call-log','ecums',NULL,'y','113','{\"url\":\"/console/report/interfaceCall\"}','200','2017-10-10','',''), ('223','认证统计','c-report-interface_call_log','ecums',NULL,'y','146','{url:\'/console/report/l2InfclogReport\'}','10','2017-10-17','',''), ('226','刷新缓存管理','ec-mgr-cacheReload','ecums',NULL,'y','167','{url:\'/console/cacheReload\'}','100','2017-10-10','',''), ('227','JOB任务管理','c-mgr-jobTask','ecums',NULL,'y','167','{\"url\":\"/console/jobTask\"}','200','2017-10-11','',''), ('228','接口信息','c-interface-info','ecums',NULL,'y','113','{\"url\":\"/console/interfaceInfo\"}','150','2017-10-12','',''), ('236','test1','test1','ecums',NULL,'y','114','','9999','2018-09-23','',''), ('246','Key资产业务','app_business','ecums',NULL,'y','1','','400','2018-10-03','',''), ('247','Key资产管理','app_cakey_mg','ecums',NULL,'y','246','{\"url\":\"/userCakey/index\"}','100','2018-10-03','',''), ('248','Key资产工单','app_cakey_order','ecums',NULL,'y','246','{\"url\":\"/cakeyOrder/index\"}','200','2018-10-03','',''), ('249','Key资产统计','app_cakey_rport','ecums',NULL,'y','246','{\"url\":\"/cakeyOrder/report\"}','300','2018-10-03','','');
INSERT INTO `ec_operator` VALUES ('2','1','2'), ('3','2','2'), ('4','5','2'), ('5','6','2'), ('6','3','2'), ('7','7','2'), ('8','8','2'), ('9','4','2'), ('10','9','2'), ('11','10','2'), ('110','110','2'), ('113','113','2'), ('117','146','2'), ('132','168','2'), ('133','167','2'), ('136','171','2'), ('138','174','2'), ('139','175','2'), ('167','221','2'), ('168','222','2'), ('170','223','2'), ('171','226','2'), ('172','227','2'), ('173','228','2'), ('174','171','115'), ('175','168','115'), ('183','246','2'), ('184','247','2'), ('185','248','2'), ('186','249','2'), ('187','5','115'), ('188','6','115'), ('189','7','115'), ('190','8','115'), ('191','9','115'), ('192','1','119');
INSERT INTO `ec_permission` VALUES ('1','1','1','2009-07-27'), ('2','1','3','2011-04-19'), ('3','1','4','2011-04-19'), ('4','1','5','2011-04-19'), ('5','1','6','2011-04-19'), ('6','1','7','2011-04-19'), ('7','1','8','2011-04-19'), ('8','1','10','2011-04-19'), ('9','1','11','2011-04-19'), ('10','1','9','2011-04-19'), ('110','111','110','2014-07-10'), ('113','111','113','2014-07-10'), ('117','111','117','2015-02-05'), ('124','111','133','2015-05-04'), ('125','111','132','2015-05-04'), ('128','111','136','2015-12-10'), ('130','111','138','2015-12-17'), ('131','111','139','2015-12-17'), ('158','111','167','2017-10-09'), ('159','111','168','2017-10-09'), ('160','111','170','2017-10-10'), ('161','111','171','2017-10-10'), ('162','111','172','2017-10-11'), ('163','111','173','2017-10-12'), ('164','111','174','2017-10-18'), ('169','116','110','2018-09-30'), ('170','111','183','2018-10-01'), ('171','111','184','2018-10-01'), ('172','111','185','2018-10-01'), ('173','111','186','2018-10-03'), ('174','1','192','2018-10-04'), ('176','111','192','2018-10-05');
INSERT INTO `ec_role` VALUES ('1','ums系统管理员角色','ums-admin','y','110','','99999','2015-05-04'), ('110','ums系统角色','ums-role','y','-1','','10','2017-10-09'), ('111','系统管理员角色','app-admin-role','y','114','','1','2018-10-03'), ('114','Key资产管理平台角色','idcard-role','y','-1','','100','2018-10-01'), ('116','test','test','y','114','','9999','2018-09-29'), ('117','test3','test3','y','114','','9999','2018-10-05');
INSERT INTO `ec_user` VALUES ('1','超级管理员','root','','C6285F69B6765A5CDD3B09556D84C5BE',NULL,'y',NULL,'99999','2018-09-28',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL), ('110','系统管理员','admin','','9116392DC24B7B84483BA00B0D72B80C','','y',NULL,'99999','2018-09-29','18950295821','','','',NULL,'',NULL,NULL), ('120','test','test','test','49BF6CD1A87F97AC1D6C4BC45ABA857D','test1@qq.com','y',NULL,'9999','2018-09-29','','','','',NULL,'',NULL,NULL), ('146','小张','350583197210211001',NULL,'8AC00157D7B4A4471116682C49D75D9A',NULL,'y',NULL,'9999','2018-10-01','18950295811',NULL,NULL,NULL,NULL,NULL,NULL,'350583197210211001'), ('147','小李','350583197210211002',NULL,'C7FEF7F63ACF35645F20FF76FD43A0F3',NULL,'y',NULL,'9999','2018-10-01','18950295812',NULL,NULL,NULL,NULL,NULL,NULL,'350583197210211002'), ('148','天一','350583197210211003',NULL,'E3E3590E3472B27ACE184B1C0B892B67',NULL,'y',NULL,'9999','2018-10-01',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'350583197210211003'), ('149','天二','350583197210211004',NULL,'DECE8BB5FA54C6A3928806591AB89E7F',NULL,'y',NULL,'9999','2018-10-01',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'350583197210211004'), ('150','天三','350583197210211005',NULL,'7F656B57F03DD1E8C32CA9D295801CF6',NULL,'y',NULL,'9999','2018-10-01',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'350583197210211005');
INSERT INTO `ec_users_assignmen` VALUES ('1','1','1',NULL), ('110','110','1','2014-07-10'), ('111','110','111','2014-07-10');
INSERT INTO `ec_user_cakey` VALUES ('33','350583197210211001','a00001','04','2018-10-01 23:59:13','admin','2018-10-02 00:04:50','admin'), ('34','350583197210211002','a00002','04','2018-10-01 23:59:13','admin','2018-10-02 00:03:37','admin'), ('35','350583197210211003','a00003','03','2018-10-01 23:59:13','admin','2018-10-02 00:02:27','admin'), ('36','350583197210211004','a00004','02','2018-10-01 23:59:13','admin','2018-10-02 00:00:28','admin'), ('37','350583197210211005','a00005','02','2018-10-01 23:59:13','admin','2018-10-01 23:59:22','admin'), ('38','350583197210211001','b00001','01','2018-10-03 14:18:42','admin','2018-10-03 14:18:42','admin'), ('39','350583197210211002','b00002','02','2018-10-03 14:18:42','admin','2018-10-04 10:56:26','admin'), ('40','350583197210211003','b00003','02','2018-10-03 14:18:42','admin','2018-10-04 10:56:26','admin'), ('41','350583197210211004','b00004','02','2018-10-03 14:18:42','admin','2018-10-04 10:56:26','admin'), ('42','350583197210211005','b00005','02','2018-10-03 14:18:42','admin','2018-10-04 10:56:26','admin'), ('43','350583197210211005','c00005','02','2018-10-03 14:19:18','admin','2018-10-03 14:21:08','admin'), ('48','350583197210211001','c00001','01','2018-10-04 12:07:15','admin','2018-10-04 12:07:15','admin'), ('49','350583197210211002','c00002','01','2018-10-04 12:07:15','admin','2018-10-04 12:07:15','admin'), ('50','350583197210211003','c00003','01','2018-10-04 12:07:15','admin','2018-10-04 12:07:15','admin'), ('51','350583197210211004','c00004','01','2018-10-04 12:07:15','admin','2018-10-04 12:07:15','admin');
INSERT INTO `ec_user_extend` VALUES ('1','','','','',NULL,'11',NULL), ('110','','','','',NULL,'',NULL), ('111','','',NULL,'','','',NULL), ('112','','',NULL,'','','',NULL), ('117','','',NULL,'','','',NULL), ('119','','',NULL,'','','',NULL), ('120','','','','',NULL,'',NULL), ('121','','','','',NULL,'',NULL), ('122','','','','',NULL,'',NULL), ('123','-7','','','',NULL,'',NULL), ('124','','','','',NULL,'',NULL);
INSERT INTO `ec_user_group` VALUES ('1','1','1'), ('110','110','110'), ('116','110','120'), ('142','121','146'), ('143','122','147'), ('144','122','148'), ('145','122','149'), ('146','122','150');
