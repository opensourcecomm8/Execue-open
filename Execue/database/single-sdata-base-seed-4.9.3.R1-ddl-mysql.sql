--
-- Host: localhost    Database: sdata-base
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
-- Table structure for table `city_state_country`
--

DROP TABLE IF EXISTS `city_state_country`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `city_state_country` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `city_be_id` bigint(20) NOT NULL,
  `City_Name` varchar(255) DEFAULT NULL,
  `City_Desc` varchar(255) DEFAULT NULL,
  `state_be_id` bigint(20) DEFAULT NULL,
  `State_Name` varchar(255) DEFAULT NULL,
  `State_Code` varchar(10) DEFAULT NULL,
  `country_be_id` bigint(20) DEFAULT NULL,
  `Country_Name` varchar(255) DEFAULT NULL,
  `Country_Code` varchar(10) DEFAULT NULL,
  KEY `idx_ctc_id` (`id`),
  KEY `idx_ctc_ctbeid` (`city_be_id`),
  KEY `idx_ctc_stbeid` (`state_be_id`),
  KEY `idx_ctc_cntrybeid` (`country_be_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `city_state_country`
--

LOCK TABLES `city_state_country` WRITE;
/*!40000 ALTER TABLE `city_state_country` DISABLE KEYS */;
/*!40000 ALTER TABLE `city_state_country` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `country_city`
--

DROP TABLE IF EXISTS `country_city`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `country_city` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `country_id` bigint(20) DEFAULT NULL,
  `city_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_cc_country_id` (`country_id`),
  KEY `idx_cc_city_id` (`city_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `country_city`
--

LOCK TABLES `country_city` WRITE;
/*!40000 ALTER TABLE `country_city` DISABLE KEYS */;
/*!40000 ALTER TABLE `country_city` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `country_state`
--

DROP TABLE IF EXISTS `country_state`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `country_state` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `country_id` bigint(20) DEFAULT NULL,
  `state_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_cs_country_id` (`country_id`),
  KEY `idx_cs_state_id` (`state_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `country_state`
--

LOCK TABLES `country_state` WRITE;
/*!40000 ALTER TABLE `country_state` DISABLE KEYS */;
/*!40000 ALTER TABLE `country_state` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `location_point_info`
--

DROP TABLE IF EXISTS `location_point_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `location_point_info` (
  `ID` bigint(20) NOT NULL,
  `ZIP_CODE` varchar(11) DEFAULT NULL,
  `LOCATION_BE_ID` bigint(20) DEFAULT NULL,
  `LOCATION_TYPE` int(1) NOT NULL,
  `LONGITUDE` float(12,8) NOT NULL,
  `LATITUDE` float(12,8) NOT NULL,
  `LOCATION_PT` point DEFAULT NULL,
  `LOCATION_TXT` varchar(255) DEFAULT NULL,
  `LOCATION_DISPLAY_NAME` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `IDX_LPI_ZIP_CODE` (`ZIP_CODE`),
  KEY `IDX_LPI_LOCATION_BE_ID` (`LOCATION_BE_ID`),
  KEY `IDX_LPI_LOCATION_TYPE` (`LOCATION_TYPE`),
  KEY `IDX_LPI_LOCATION_TXT` (`LOCATION_TXT`),
  KEY `IDX_LPI_LOC_DISP_NAME` (`LOCATION_DISPLAY_NAME`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `location_point_info`
--

LOCK TABLES `location_point_info` WRITE;
/*!40000 ALTER TABLE `location_point_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `location_point_info` ENABLE KEYS */;
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
) ENGINE=MyISAM AUTO_INCREMENT=14 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patch_info`
--

LOCK TABLES `patch_info` WRITE;
/*!40000 ALTER TABLE `patch_info` DISABLE KEYS */;
INSERT INTO `patch_info` VALUES (1,'4.8.3.R1-sdata-clean-C01-U01','DML','script to create location_point_info table','2011-09-26','2012-09-03 02:03:06','Prasanna','4.8.3.R1','U01','CleanApp','C01','SDATA');
INSERT INTO `patch_info` VALUES (2,'4.8.4.R1-sdata-clean-C00-U00','DML','Blank patch to denote code release','2011-10-25','2012-09-03 02:03:06','Raju Gottumukkala','4.8.4.R1','U00','CleanApp','C00','SDATA');
INSERT INTO `patch_info` VALUES (3,'4.8.4.R1-sdata-clean-C01-U01','DDL','Creation of indexes on LPI table for type and txt columns','2011-11-09','2012-09-03 02:03:06','Raju Gottumukkala','4.8.4.R1','U01','CleanApp','C01','SDATA');
INSERT INTO `patch_info` VALUES (4,'4.8.4.R1-sdata-clean-C02-U02','DDL','Addition of new column location display name on LPI table','2011-11-23','2012-09-03 02:03:07','Raju Gottumukkala','4.8.4.R1','U02','CleanApp','C02','SDATA');
INSERT INTO `patch_info` VALUES (5,'4.8.4.R1-sdata-clean-C03-U03','DDL','Updation of location type on LPI table to default for bed id based types','2011-12-21','2012-09-03 02:03:07','Raju Gottumukkala','4.8.4.R1','U03','CleanApp','C03','SDATA');
INSERT INTO `patch_info` VALUES (6,'4.8.5.R1-sdata-clean-C00-U00','DML','Blank patch to denote code release','2012-03-19','2012-09-03 02:03:07','Kaliki','4.8.5.R1','U00','CleanApp','C00','SDATA');
INSERT INTO `patch_info` VALUES (7,'4.8.6.R1-sdata-clean-C00-U00','DML','Blank patch to denote code release','2012-06-19','2012-09-03 02:03:07','Raju Gottumukkala','4.8.6.R1','U00','CleanApp','C00','SDATA');
INSERT INTO `patch_info` VALUES (8,'4.8.7.R1-sdata-clean-C00-U00','DML','Blank patch to denote code release','2012-06-19','2012-09-03 02:03:07','Raju Gottumukkala','4.8.7.R1','U00','CleanApp','C00','SDATA');
INSERT INTO `patch_info` VALUES (9,'4.8.8.R1-sdata-clean-C00-U00','DML','Blank patch to denote code release','2012-08-23','2012-09-10 16:20:03','Kaliki Aritakula','4.8.8.R1','U00','CleanApp','C00','SDATA');
INSERT INTO `patch_info` VALUES (10,'4.9.0.R1-sdata-clean-C00-U00','DML','Blank patch to denote code release','2012-09-10','2012-10-22 11:22:57','Shiva Sirigineedi','4.9.0.R1','U00','CleanApp','C00','SDATA');
INSERT INTO `patch_info` VALUES (11,'4.9.0.R1-sdata-clean-C01-U01','DML','Sequence entries are added','2012-09-14','2012-10-22 11:22:57','Raju Gottumukkala','4.9.0.R1','U01','CleanApp','C01','SDATA');
INSERT INTO `patch_info` VALUES (12,'4.9.1.R1-sdata-clean-C00-U00','DML','Blank patch to denote code release','2012-10-22','2012-11-03 15:18:27','Raju Gottumukkala','4.9.1.R1','U00','CleanApp','C00','SDATA');
INSERT INTO `patch_info` VALUES (13,'4.9.2.R1-sdata-clean-C00-U00','DML','Blank patch to denote code release','2012-11-05','2012-11-11 20:54:31','Raju Gottumukkala','4.9.2.R1','U00','CleanApp','C00','SDATA');
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
  KEY `IDX_S_SN` (`SEQUENCE_NAME`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sequences`
--

LOCK TABLES `sequences` WRITE;
/*!40000 ALTER TABLE `sequences` DISABLE KEYS */;
INSERT INTO `sequences` VALUES ('CITY_STATE_COUNTRY',1);
INSERT INTO `sequences` VALUES ('COUNTRY_CITY',1);
INSERT INTO `sequences` VALUES ('COUNTRY_STATE',1);
INSERT INTO `sequences` VALUES ('LOCATION_POINT_INFO',1);
INSERT INTO `sequences` VALUES ('STATE_CITY',1);
/*!40000 ALTER TABLE `sequences` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `state_city`
--

DROP TABLE IF EXISTS `state_city`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `state_city` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `state_id` bigint(20) DEFAULT NULL,
  `city_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_sc_state_id` (`state_id`),
  KEY `idx_sc_city_id` (`city_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `state_city`
--

LOCK TABLES `state_city` WRITE;
/*!40000 ALTER TABLE `state_city` DISABLE KEYS */;
/*!40000 ALTER TABLE `state_city` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2012-11-11 20:54:35
/* ---------------------------Meta Information------------------------------- */
Select @purposeDesc := 'Blank patch to denote code release';

Select @authorName := 'Build';

Select @createdDate := 'Nov 11 2012'; -- MMM DD YYYY

Select @category := 'CleanApp'; -- 'PortalApp'|'CleanApp'

Select @releaseVersion := '4.9.3.R1'; -- Example: 4.7.1.R2, 3.6.2.R1 etc

Select @uniquePatchNumber := '00'; -- Example: 01, 15, 14.a etc

Select @categoryPatchNumber := '00'; -- Example: 01, 15, 14.a etc, but for unique with in each category (CleanApp | PortalApp)

Select @targetSchema := 'SWI'; -- 'SWI'|'QDATA'|'SDATA'|'USWH'

Select @sqlType := 'DML'; -- 'DDL'|'DML'
/* ---------------------------Script Begin------------------------------- */

-- Blank patch to denote code release

/* ---------------------------Script End---------------------------------------- */
SELECT @categoryPatchNumber := CASE
	WHEN @category = 'PortalApp'
	THEN CONCAT('P',@categoryPatchNumber)
	ELSE CONCAT('C',@categoryPatchNumber)
	END;

SELECT @uniquePatchNumber := CONCAT('U',@uniquePatchNumber);

SELECT @patchDetail := CASE
	WHEN @category = 'PortalApp'
	THEN CONCAT(@releaseVersion,'-',LOWER(@targetSchema),'-','portal-',@categoryPatchNumber,'-',@uniquePatchNumber)
	ELSE CONCAT(@releaseVersion,'-',LOWER(@targetSchema),'-','clean-',@categoryPatchNumber,'-',@uniquePatchNumber)
	END;

INSERT INTO `PATCH_INFO` (`PATCH_DETAIL`,`RELEASE_VERSION`,`PATCH_NUMBER`,`PATCH_CATEGORY`,`CATEGORY_PATCH_NUMBER`,`SQL_TYPE`,`CREATED_ON`,
	`APPLIED_AT`,`AUTHOR_NAME`,`TARGET_SCHEMA`,`PURPOSE_DESC`)
VALUES (@patchDetail,@releaseVersion,@uniquePatchNumber,@category,@categoryPatchNumber,@sqlType,STR_TO_DATE(@createdDate, '%b %d %Y'),
	CURRENT_TIMESTAMP,@authorName,@targetSchema,@purposeDesc);

-- https://share.execue.com/share/index.php/Patch_Maintenance_Procedure
