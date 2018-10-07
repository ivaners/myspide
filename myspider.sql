/*
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for proxyip
-- ----------------------------
DROP TABLE IF EXISTS `proxyip`;
CREATE TABLE `proxyip` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ip` varchar(20) DEFAULT NULL,
  `port` int(11) DEFAULT NULL,
  `type` varchar(20) DEFAULT NULL,
  `addr` varchar(100) DEFAULT NULL,
  `used` tinyint(4) DEFAULT '0',
  `other` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=711 DEFAULT CHARSET=utf8;