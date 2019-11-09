DROP DATABASE IF EXISTS noxml;

CREATE DATABASE IF NOT EXISTS noxml default charset utf8mb4 COLLATE utf8mb4_unicode_ci;

use noxml;

CREATE TABLE `mybatis_test` (
  `id` int(11) NOT NULL DEFAULT '0',
  `name` varchar(255) COLLATE utf8_bin DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='属性表';

insert into `noxml`.`mybatis_test` ( `id`, `name`) values ( '1', '232');

CREATE TABLE `properties` (
  `id` int(11) NOT NULL DEFAULT '0',
  `name` varchar(255) COLLATE utf8_bin DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='属性表';

insert into `noxml`.`properties` ( `id`, `name`) values ( '1', 'sdjfjd');