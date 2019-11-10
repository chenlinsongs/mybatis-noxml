

CREATE TABLE `properties` (
  `id` int(11)  NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_bin DEFAULT '',
  `sec` varchar(255) COLLATE utf8_bin DEFAULT '',
  `value` varchar(255) COLLATE utf8_bin DEFAULT '',
  `age` int(11) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='属性表';


CREATE TABLE `mybatis_test` (
  `id` int(11)  NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_bin DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='自定义sql测试';

insert into `noxml`.`mybatis_test` ( `id`, `name`) values ( '1', '2323');