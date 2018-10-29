--
-- Host: localhost    Database: audit-base
-- ------------------------------------------------------
-- Server version	5.1.38-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `anonymous_user`
--

DROP TABLE IF EXISTS `anonymous_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `anonymous_user` (
  `ID` bigint(20) NOT NULL,
  `COUNTRY_CODE` varchar(5) DEFAULT NULL,
  `COUNTRY_NAME` varchar(255) DEFAULT NULL,
  `STATE_CODE` varchar(15) DEFAULT NULL,
  `STATE_NAME` varchar(255) DEFAULT NULL,
  `CITY_NAME` varchar(255) DEFAULT NULL,
  `ZIP_CODE` varchar(25) DEFAULT NULL,
  `IP_LOCATION` varchar(255) DEFAULT NULL,
  `LATITUDE` varchar(255) DEFAULT NULL,
  `LONGITUDE` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `anonymous_user`
--

LOCK TABLES `anonymous_user` WRITE;
/*!40000 ALTER TABLE `anonymous_user` DISABLE KEYS */;
/*!40000 ALTER TABLE `anonymous_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `patch_info`
--

DROP TABLE IF EXISTS `patch_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `patch_info` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `PATCH_DETAIL` varchar(100) DEFAULT NULL,
  `SQL_TYPE` varchar(35) DEFAULT NULL,
  `PURPOSE_DESC` text,
  `CREATED_ON` date DEFAULT NULL,
  `APPLIED_AT` datetime DEFAULT NULL,
  `AUTHOR_NAME` varchar(100) DEFAULT NULL,
  `RELEASE_VERSION` varchar(100) DEFAULT NULL,
  `PATCH_NUMBER` varchar(100) DEFAULT NULL,
  `PATCH_CATEGORY` varchar(100) DEFAULT NULL,
  `CATEGORY_PATCH_NUMBER` varchar(100) DEFAULT NULL,
  `TARGET_SCHEMA` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patch_info`
--

LOCK TABLES `patch_info` WRITE;
/*!40000 ALTER TABLE `patch_info` DISABLE KEYS */;
INSERT INTO `patch_info` VALUES (1,'4.8.8.R1-audit-clean-C00-U00','DML','Blank patch to denote code release','2012-08-23','2012-09-08 13:06:10','Kaliki Aritakula','4.8.8.R1','U00','CleanApp','C00','AUDIT');
INSERT INTO `patch_info` VALUES (2,'4.8.9.R1-audit-clean-C00-U00','DML','Blank patch to denote code release','2012-09-18','2012-10-22 11:12:59','Raju Gottumukkala','4.8.9.R1','U00','CleanApp','C00','AUDIT');
INSERT INTO `patch_info` VALUES (3,'4.9.0.R1-audit-clean-C00-U00','DML','Blank patch to denote code release','2012-08-23','2012-10-22 11:13:02','Shiva Sirigineedi','4.9.0.R1','U00','CleanApp','C00','AUDIT');
INSERT INTO `patch_info` VALUES (6,'4.9.1.R1-audit-clean-C00-U00','DML','Blank patch to denote code release','2012-10-22','2012-11-03 15:12:54','Raju Gottumukkala','4.9.1.R1','U00','CleanApp','C00','AUDIT');
INSERT INTO `patch_info` VALUES (7,'4.9.2.R1-audit-clean-C00-U00','DML','Blank patch to denote code release','2012-11-05','2012-11-11 20:54:33','Raju Gottumukkala','4.9.2.R1','U00','CleanApp','C00','AUDIT');
INSERT INTO `patch_info` VALUES (8,'4.9.3.R1-swi-clean-C00-U00','DML','Blank patch to denote code release','2012-11-11','2012-11-13 23:28:40','Build','4.9.3.R1','U00','CleanApp','C00','SWI');
/*!40000 ALTER TABLE `patch_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sequences`
--

DROP TABLE IF EXISTS `sequences`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sequences` (
  `SEQUENCE_NAME` varchar(255) NOT NULL,
  `NEXT_VAL` bigint(20) NOT NULL DEFAULT '1',
  PRIMARY KEY (`SEQUENCE_NAME`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sequences`
--

LOCK TABLES `sequences` WRITE;
/*!40000 ALTER TABLE `sequences` DISABLE KEYS */;
INSERT INTO `sequences` VALUES ('USER_ACCESS_AUDIT',1);
INSERT INTO `sequences` VALUES ('ANONYMOUS_USER',900000001);
/*!40000 ALTER TABLE `sequences` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_access_audit`
--

DROP TABLE IF EXISTS `user_access_audit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_access_audit` (
  `ID` bigint(20) NOT NULL,
  `USER_ID` bigint(20) NOT NULL,
  `ACCESSED_TIME` datetime NOT NULL,
  `IP_LOCATION` varchar(255) DEFAULT NULL,
  `AUDIT_LOG_TYPE` varchar(25) NOT NULL,
  `ACCESSED_SYSTEM` varchar(15) DEFAULT NULL,
  `ANONYMOUS_USER` varchar(3) NOT NULL DEFAULT 'NO',
  `COMMENTS` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `IDX_UAA_USER_ID` (`USER_ID`),
  KEY `IDX_UAA_LOGTYPE` (`AUDIT_LOG_TYPE`),
  KEY `IDX_UAA_SYS` (`ACCESSED_SYSTEM`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_access_audit`
--

LOCK TABLES `user_access_audit` WRITE;
/*!40000 ALTER TABLE `user_access_audit` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_access_audit` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2012-11-13 23:28:53
