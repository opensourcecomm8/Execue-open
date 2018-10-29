--
-- Host: localhost    Database: uswh-base
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
-- Table structure for table `feature`
--

DROP TABLE IF EXISTS `feature`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `feature` (
  `ID` int(10) NOT NULL DEFAULT '0',
  `FEATURE_NAME` varchar(255) NOT NULL,
  `FEATURE_DISPLAY_NAME` varchar(255) DEFAULT NULL,
  `FEATURE_SYMBOL` varchar(20) DEFAULT NULL,
  `FEATURE_BE_ID` bigint(20) DEFAULT NULL,
  `FEATURE_TYPE` varchar(1) DEFAULT NULL,
  `CONTEXT_ID` bigint(20) NOT NULL,
  `LOCATION_BASED` char(1) NOT NULL DEFAULT 'N',
  `MULTI_VALUED` char(1) NOT NULL DEFAULT 'N',
  `MULTI_VALUED_GLOBAL_PENALTY` char(1) NOT NULL DEFAULT 'N',
  PRIMARY KEY (`ID`),
  KEY `IDX_F_CTX_ID` (`CONTEXT_ID`),
  KEY `IDX_F_FT` (`FEATURE_TYPE`),
  KEY `idx_f_fn` (`FEATURE_NAME`),
  KEY `idx_f_fbe_id` (`FEATURE_BE_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `feature`
--

LOCK TABLES `feature` WRITE;
/*!40000 ALTER TABLE `feature` DISABLE KEYS */;
/*!40000 ALTER TABLE `feature` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `feature_dependency`
--

DROP TABLE IF EXISTS `feature_dependency`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `feature_dependency` (
  `ID` bigint(20) NOT NULL,
  `CONTEXT_ID` bigint(20) NOT NULL,
  `FEATURE_ID` bigint(20) NOT NULL,
  `DEPENDENCY_FEATURE_ID` bigint(20) DEFAULT NULL,
  `DEPENDENCY_TYPE` varchar(35) NOT NULL DEFAULT 'FACET',
  PRIMARY KEY (`ID`),
  KEY `IDX_FD_CTX_ID` (`CONTEXT_ID`),
  KEY `IDX_FD_F_ID` (`FEATURE_ID`),
  KEY `IDX_FD_DT` (`DEPENDENCY_TYPE`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `feature_dependency`
--

LOCK TABLES `feature_dependency` WRITE;
/*!40000 ALTER TABLE `feature_dependency` DISABLE KEYS */;
/*!40000 ALTER TABLE `feature_dependency` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `feature_detail`
--

DROP TABLE IF EXISTS `feature_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `feature_detail` (
  `ID` bigint(20) NOT NULL,
  `CONTEXT_ID` bigint(20) NOT NULL,
  `FEATURE_ID` bigint(20) NOT NULL,
  `column_name` varchar(35) DEFAULT NULL,
  `DISPLAY_ORDER` int(2) NOT NULL DEFAULT '1',
  `SORTABLE` char(1) NOT NULL DEFAULT 'Y',
  `DEFAULT_SORT_ORDER` varchar(4) DEFAULT 'ASC',
  `DETAIL_TYPE` varchar(35) NOT NULL,
  `ALIGNMENT` varchar(15) DEFAULT 'right',
  `DATA_HEADER` varchar(2) NOT NULL DEFAULT 'N',
  `FACET_DEPENDENCY` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `IDX_FDTL_CTX_ID` (`CONTEXT_ID`),
  KEY `IDX_FDTL_F_ID` (`FEATURE_ID`),
  KEY `IDX_FDTL_DT` (`DETAIL_TYPE`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `feature_detail`
--

LOCK TABLES `feature_detail` WRITE;
/*!40000 ALTER TABLE `feature_detail` DISABLE KEYS */;
/*!40000 ALTER TABLE `feature_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `feature_range`
--

DROP TABLE IF EXISTS `feature_range`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `feature_range` (
  `ID` int(10) NOT NULL,
  `FEATURE_ID` varchar(255) NOT NULL,
  `RANGE_NAME` varchar(55) NOT NULL DEFAULT 'Un Known',
  `START_VALUE` decimal(20,6) DEFAULT NULL,
  `END_VALUE` decimal(20,6) DEFAULT NULL,
  `RANGE_ORDER` int(2) DEFAULT NULL,
  `IS_RANGE_TYPE` varchar(1) DEFAULT 'Y',
  PRIMARY KEY (`ID`),
  KEY `IDX_FR_RN` (`RANGE_NAME`),
  KEY `idx_fr_f_id` (`FEATURE_ID`)
) ENGINE=MyISAM AUTO_INCREMENT=224 DEFAULT CHARSET=latin1 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `feature_range`
--

LOCK TABLES `feature_range` WRITE;
/*!40000 ALTER TABLE `feature_range` DISABLE KEYS */;
/*!40000 ALTER TABLE `feature_range` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `feature_range_info`
--

DROP TABLE IF EXISTS `feature_range_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `feature_range_info` (
  `ID` bigint(20) NOT NULL,
  `FEATURE_ID` bigint(20) NOT NULL,
  `DEFAULT_VALUE` decimal(16,6) NOT NULL,
  `MINIMUM_VALUE` decimal(16,6) NOT NULL,
  `MAXIMUM_VALUE` decimal(16,6) NOT NULL,
  `MINIMUM_IMPACT_VALUE` decimal(16,6) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `IDX_FRI_FI` (`FEATURE_ID`),
  KEY `IDX_FRI_DV` (`DEFAULT_VALUE`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `feature_range_info`
--

LOCK TABLES `feature_range_info` WRITE;
/*!40000 ALTER TABLE `feature_range_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `feature_range_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `feature_value`
--

DROP TABLE IF EXISTS `feature_value`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `feature_value` (
  `ID` int(10) NOT NULL DEFAULT '0',
  `FEATURE_ID` varchar(255) NOT NULL,
  `FEATURE_VALUE` varchar(255) DEFAULT NULL,
  `FEATURE_VALUE_BE_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `idx_fv_f_id` (`FEATURE_ID`),
  KEY `idx_fv_fv` (`FEATURE_VALUE`),
  KEY `idx_fv_fvbe_id` (`FEATURE_VALUE_BE_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `feature_value`
--

LOCK TABLES `feature_value` WRITE;
/*!40000 ALTER TABLE `feature_value` DISABLE KEYS */;
/*!40000 ALTER TABLE `feature_value` ENABLE KEYS */;
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
) ENGINE=MyISAM AUTO_INCREMENT=45 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patch_info`
--

LOCK TABLES `patch_info` WRITE;
/*!40000 ALTER TABLE `patch_info` DISABLE KEYS */;
INSERT INTO `patch_info` VALUES (1,'4.8.3.R1-uswh-clean-C01-U01','DML','SOURCE_CONTENT table creation','2011-08-26','2012-09-03 02:09:26','Raju Gottumukkala','4.8.3.R1','U01','CleanApp','C01','USWH');
INSERT INTO `patch_info` VALUES (2,'4.8.3.R1-uswh-clean-C02-U02','DDL','SEMANTIFIED_CONTENT table creation and correction on Source Content table','2011-09-21','2012-09-03 02:09:26','Raju Gottumukkala','4.8.3.R1','U02','CleanApp','C02','USWH');
INSERT INTO `patch_info` VALUES (3,'4.8.3.R1-uswh-clean-C03-U03','DDL','Tables cleanup on names and unwanted tables cleanup','2011-09-21','2012-09-03 02:09:27','Raju Gottumukkala','4.8.3.R1','U03','CleanApp','C03','USWH');
INSERT INTO `patch_info` VALUES (4,'4.8.3.R1-uswh-clean-C04-U04','DDL','Category column renamed to context id and to BIGINT from varchar','2011-09-22','2012-09-03 02:09:27','Raju Gottumukkala','4.8.3.R1','U04','CleanApp','C04','USWH');
INSERT INTO `patch_info` VALUES (5,'4.8.3.R1-uswh-clean-C05-U05','DDL','Unwanted COlumns taken off from Semantified Content Feature Info table','2011-09-23','2012-09-03 02:09:27','Raju Gottumukkala','4.8.3.R1','U05','CleanApp','C05','USWH');
INSERT INTO `patch_info` VALUES (6,'4.8.3.R1-uswh-clean-C06-U06','DDL','Script to create the user_query_location_info table','2011-09-27','2012-09-03 02:09:27','Nitesh','4.8.3.R1','U06','CleanApp','C06','USWH');
INSERT INTO `patch_info` VALUES (7,'4.8.3.R1-uswh-clean-C07-U07','DDL','ZIP CODE Column taken off from Semantified Content Feature Info table','2011-09-27','2012-09-03 02:09:28','Raju Gottumukkala','4.8.3.R1','U07','CleanApp','C07','USWH');
INSERT INTO `patch_info` VALUES (8,'4.8.3.R1-uswh-clean-C08-U08','DDL','Script to add location related columns to semantified_content_feature_info table','2011-09-27','2012-09-03 02:09:28','Nitesh','4.8.3.R1','U08','CleanApp','C08','USWH');
INSERT INTO `patch_info` VALUES (9,'4.8.3.R1-uswh-clean-C09-U09','DDL','script to add columns to semantified_content table','2011-09-29','2012-09-03 02:09:28','MurthySN','4.8.3.R1','U09','CleanApp','C09','USWH');
INSERT INTO `patch_info` VALUES (10,'4.8.3.R1-uswh-clean-C10-U10','DDL','script to modify scale of decimal columns ','2011-10-17','2012-09-03 02:09:28','Aditya Gole','4.8.3.R1','U10','CleanApp','C10','USWH');
INSERT INTO `patch_info` VALUES (11,'4.8.4.R1-uswh-clean-C00-U00','DML','Blank patch to denote code release','2011-10-25','2012-09-03 02:09:28','Raju Gottumukkala','4.8.4.R1','U00','CleanApp','C00','USWH');
INSERT INTO `patch_info` VALUES (12,'4.8.4.R1-uswh-clean-C01-U01','DDL','Added new column in feature table','2011-10-25','2012-09-03 02:09:29','Jitendra Tiwari','4.8.4.R1','U01','CleanApp','C01','USWH');
INSERT INTO `patch_info` VALUES (13,'4.8.4.R1-uswh-clean-C02-U02','DDL','Added new table in feature_column_mapping','2011-10-25','2012-09-03 02:09:29','Jitendra Tiwari','4.8.4.R1','U02','CleanApp','C02','USWH');
INSERT INTO `patch_info` VALUES (14,'4.8.4.R1-uswh-clean-C03-U03','DDL','Added new column in semantified_content_feature_info table','2011-10-25','2012-09-03 02:09:29','Jitendra Tiwari','4.8.4.R1','U03','CleanApp','C03','USWH');
INSERT INTO `patch_info` VALUES (15,'4.8.4.R1-uswh-clean-C04-U04','DDL','Alter table feature_column_mapping added new column ALIGNMENT','2011-11-04','2012-09-03 02:09:29','Jitendra Tiwari','4.8.4.R1','U04','CleanApp','C04','USWH');
INSERT INTO `patch_info` VALUES (16,'4.8.4.R1-uswh-clean-C05-U05','DDL','Script to add the location related columns in the user_query_location_info table','2011-11-08','2012-09-03 02:09:30','Nitesh','4.8.4.R1','U05','CleanApp','C05','USWH');
INSERT INTO `patch_info` VALUES (17,'4.8.4.R1-uswh-clean-C06-U06','DDL','Script to add location_display_name and PROCESSING_STATE column on SCFI table and data_header flag column on FCM table','2011-11-10','2012-09-03 02:09:30','Raju Gottumukkala','4.8.4.R1','U06','CleanApp','C06','USWH');
INSERT INTO `patch_info` VALUES (18,'4.8.4.R1-uswh-clean-C07-U07','DDL','Script to alter the FEATURE_WEIGHT_FACTOR to decimal type in the user_query_feature_info table','2011-11-23','2012-09-03 02:09:30','Nitesh','4.8.4.R1','U07','CleanApp','C07','USWH');
INSERT INTO `patch_info` VALUES (19,'4.8.4.R1-uswh-clean-C08-U08','DDL','Script to add a new flag MULTI_VALUED_GLOBAL_PENALTY on feature table','2011-11-23','2012-09-03 02:09:30','Raju Gottumukkala','4.8.4.R1','U08','CleanApp','C08','USWH');
INSERT INTO `patch_info` VALUES (20,'4.8.4.R1-uswh-clean-C09-U09','DDL','Script to add a new table FACET_DEPENDENCY and two columns to FEATURE_COLUMN_MAPPING table','2011-11-24','2012-09-03 02:09:31','MurthySN','4.8.4.R1','U09','CleanApp','C09','USWH');
INSERT INTO `patch_info` VALUES (21,'4.8.4.R1-uswh-clean-C10-U10','DDL','Script to add location fields to Semantified_content table','2011-11-30','2012-09-03 02:09:31','MurthySN','4.8.4.R1','U10','CleanApp','C10','USWH');
INSERT INTO `patch_info` VALUES (22,'4.8.4.R1-uswh-clean-C11-U11','DDL','Script to add Range Name on feature range table','2011-12-01','2012-09-03 02:09:31','Raju Gottumukkala','4.8.4.R1','U11','CleanApp','C11','USWH');
INSERT INTO `patch_info` VALUES (23,'4.8.4.R1-uswh-clean-C12-U12','DDL','Script to create a new table ri_feature_content','2011-12-01','2012-09-03 02:09:32','Raju Gottumukkala','4.8.4.R1','U12','CleanApp','C12','USWH');
INSERT INTO `patch_info` VALUES (24,'4.8.4.R1-uswh-clean-C13-U13','DDL','Script to rename FDD table to FEATURE_DEPENDENCY','2011-12-08','2012-09-03 02:09:32','Raju Gottumukkala','4.8.4.R1','U13','CleanApp','C13','USWH');
INSERT INTO `patch_info` VALUES (25,'4.8.4.R1-uswh-clean-C14-U14','DDL','Script to Remove Facet Column Name and add Field Name','2011-12-08','2012-09-03 02:09:33','Raju Gottumukkala','4.8.4.R1','U14','CleanApp','C14','USWH');
INSERT INTO `patch_info` VALUES (26,'4.8.4.R1-uswh-clean-C15-U15','DDL','Script to rename FCM to Feature detail','2011-12-08','2012-09-03 02:09:33','Raju Gottumukkala','4.8.4.R1','U15','CleanApp','C15','USWH');
INSERT INTO `patch_info` VALUES (27,'4.8.4.R1-uswh-clean-C16-U16','DML','Script to populate field_name on ri_feature_content table','2011-12-08','2012-09-03 02:09:33','Raju Gottumukkala','4.8.4.R1','U16','CleanApp','C16','USWH');
INSERT INTO `patch_info` VALUES (28,'4.8.4.R1-uswh-clean-C17-U17','DML','Script to take off prominent facet column and rename mapping_type to detail_type on feature_detail','2011-12-08','2012-09-03 02:09:33','Raju Gottumukkala','4.8.4.R1','U17','CleanApp','C17','USWH');
INSERT INTO `patch_info` VALUES (29,'4.8.4.R1-uswh-clean-C18-U18','DDL','Script to set the column_name to accept null and for facet_feature detail type records the value also needs to be null','2011-12-09','2012-09-03 02:09:33','Raju Gottumukkala','4.8.4.R1','U18','CleanApp','C18','USWH');
INSERT INTO `patch_info` VALUES (30,'4.8.4.R1-uswh-clean-C19-U19','DDL','Script to add FEATURE_RANGE_INFO and populate with default values','2011-12-20','2012-09-03 02:09:34','Raju Gottumukkala','4.8.4.R1','U19','CleanApp','C19','USWH');
INSERT INTO `patch_info` VALUES (31,'4.8.4.R1-uswh-clean-C20-U20','DDL','Script to add RANGE as prefix for FRI columns on RIFC table','2011-12-21','2012-09-03 02:09:34','Raju Gottumukkala','4.8.4.R1','U20','CleanApp','C20','USWH');
INSERT INTO `patch_info` VALUES (32,'4.8.4.R1-uswh-clean-C21-U21','DDL','Script to create indexes needed on all tables','2011-12-28','2012-09-03 02:09:39','Raju Gottumukkala','4.8.4.R1','U21','CleanApp','C21','USWH');
INSERT INTO `patch_info` VALUES (33,'4.8.4.R1-uswh-clean-C22-U22','DDL','Script to correct existing indexes on all tables','2011-12-28','2012-09-03 02:09:43','Raju Gottumukkala','4.8.4.R1','U22','CleanApp','C22','USWH');
INSERT INTO `patch_info` VALUES (34,'4.8.4.R1-uswh-clean-C23-U23','DDL','Script to create db function MIN_ST_LN_DIST to calculate minimum distance in case of multiple location in query','2011-12-28','2012-09-03 02:09:43','Prasanna K','4.8.4.R1','U23','CleanApp','C23','USWH');
INSERT INTO `patch_info` VALUES (35,'4.8.4.R1-uswh-clean-C24-U24','DML','Script to update feature name and display name on RIFC from feature table','2011-01-05','2012-09-03 02:09:43','Raju Gottumukkala','4.8.4.R1','U24','CleanApp','C24','USWH');
INSERT INTO `patch_info` VALUES (36,'4.8.4.R1-uswh-clean-C25-U25','DDL','Alter script to rename table names','2012-02-14','2012-09-03 02:09:43','Aditya','4.8.4.R1','U25','CleanApp','C25','USWH');
INSERT INTO `patch_info` VALUES (37,'4.8.5.R1-uswh-clean-C00-U00','DML','Blank patch to denote code release','2011-03-19','2012-09-03 02:09:43','Kaliki','4.8.5.R1','U00','CleanApp','C00','USWH');
INSERT INTO `patch_info` VALUES (38,'4.8.6.R1-uswh-clean-C00-U00','DML','Blank patch to denote code release','2011-04-19','2012-09-03 02:09:43','Raju Gottumukkala','4.8.6.R1','U00','CleanApp','C00','USWH');
INSERT INTO `patch_info` VALUES (39,'4.8.7.R1-uswh-clean-C00-U00','DML','Blank patch to denote code release','2011-06-18','2012-09-03 02:09:43','Raju Gottumukkala','4.8.7.R1','U00','CleanApp','C00','USWH');
INSERT INTO `patch_info` VALUES (40,'4.9.0.R1-uswh-clean-C00-U00','DML','Blank patch to denote code release','2011-09-10','2012-10-22 11:27:06','Shiva Sirigineedi','4.9.0.R1','U00','CleanApp','C00','USWH');
INSERT INTO `patch_info` VALUES (41,'4.9.0.R1-uswh-clean-C01-U00','DML','Corrected sequence names for consistancy with table names','2011-09-13','2012-10-22 11:27:06','Raju Gottumukkala','4.9.0.R1','U00','CleanApp','C01','USWH');
INSERT INTO `patch_info` VALUES (42,'4.9.1.R1-uswh-clean-C00-U00','DML','Blank patch to denote code release','2011-10-22','2012-11-03 15:24:12','Raju Gottumukkala','4.9.1.R1','U00','CleanApp','C00','USWH');
INSERT INTO `patch_info` VALUES (43,'4.9.2.R1-uswh-clean-C00-U00','DML','Blank patch to denote code release','2011-11-05','2012-11-11 20:54:33','Raju Gottumukkala','4.9.2.R1','U00','CleanApp','C00','USWH');
INSERT INTO `patch_info` VALUES (44,'4.9.3.R1-swi-clean-C00-U00','DML','Blank patch to denote code release','2012-11-11','2012-11-13 23:28:39','Build','4.9.3.R1','U00','CleanApp','C00','SWI');
/*!40000 ALTER TABLE `patch_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ri_feature_content`
--

DROP TABLE IF EXISTS `ri_feature_content`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ri_feature_content` (
  `ID` bigint(20) NOT NULL,
  `CONTEXT_ID` bigint(20) NOT NULL,
  `FEATURE_ID` bigint(20) NOT NULL,
  `FEATURE_NAME` varchar(255) NOT NULL,
  `FEATURE_DISPLAY_NAME` varchar(255) DEFAULT NULL,
  `FEATURE_SYMBOL` varchar(20) DEFAULT NULL,
  `FEATURE_BE_ID` bigint(20) DEFAULT NULL,
  `FEATURE_TYPE` varchar(1) DEFAULT NULL,
  `LOCATION_BASED` char(1) NOT NULL DEFAULT 'N',
  `MULTI_VALUED` char(1) NOT NULL DEFAULT 'N',
  `MULTI_VALUED_GLOBAL_PENALTY` char(1) NOT NULL DEFAULT 'N',
  `FIELD_NAME` varchar(55) DEFAULT NULL,
  `DISPLAYABLE` char(1) NOT NULL DEFAULT 'N',
  `DISP_COLUMN_NAME` varchar(35) DEFAULT NULL,
  `DISP_ORDER` int(2) NOT NULL DEFAULT '1',
  `SORTABLE` char(1) NOT NULL DEFAULT 'Y',
  `DEFAULT_SORT_ORDER` varchar(4) DEFAULT 'ASC',
  `ALIGNMENT` varchar(15) DEFAULT 'right',
  `DATA_HEADER` varchar(2) NOT NULL DEFAULT 'N',
  `FACET` char(1) NOT NULL DEFAULT 'N',
  `FACET_ORDER` int(2) NOT NULL DEFAULT '1',
  `FACET_DEPENDENCY` varchar(255) DEFAULT NULL,
  `RANGE_DEFAULT_VALUE` decimal(16,6) DEFAULT NULL,
  `RANGE_MINIMUM_VALUE` decimal(16,6) DEFAULT NULL,
  `RANGE_MAXIMUM_VALUE` decimal(16,6) DEFAULT NULL,
  `RANGE_MINIMUM_IMPACT_VALUE` decimal(16,6) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `IDX_RFC_CTX_ID` (`CONTEXT_ID`),
  KEY `IDX_RFC_F_ID` (`FEATURE_ID`),
  KEY `IDX_RFC_F_NAME` (`FEATURE_NAME`),
  KEY `IDX_RFC_F_DISP_NAME` (`FEATURE_DISPLAY_NAME`),
  KEY `IDX_RFC_DISP` (`DISPLAYABLE`),
  KEY `IDX_RFC_FACET` (`FACET`),
  KEY `IDX_RFC_FTYPE` (`FEATURE_TYPE`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ri_feature_content`
--

LOCK TABLES `ri_feature_content` WRITE;
/*!40000 ALTER TABLE `ri_feature_content` DISABLE KEYS */;
/*!40000 ALTER TABLE `ri_feature_content` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sem_content_feature_info`
--

DROP TABLE IF EXISTS `sem_content_feature_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sem_content_feature_info` (
  `ID` int(20) NOT NULL AUTO_INCREMENT,
  `semantified_content_id` bigint(20) DEFAULT NULL,
  `CONTEXT_ID` bigint(20) DEFAULT NULL,
  `FEATURE_ID` int(10) DEFAULT NULL,
  `VALUE_STRING` varchar(255) DEFAULT NULL,
  `VALUE_NUMBER` decimal(20,6) DEFAULT NULL,
  `VALUE_TYPE` char(2) DEFAULT 'S',
  `FEATURE_WEIGHT` decimal(10,2) DEFAULT '0.00',
  `semantified_content_date` datetime DEFAULT NULL,
  `IMAGE_PRESENT` char(1) DEFAULT 'N',
  `PROCESSING_STATE` varchar(2) DEFAULT 'N',
  `LOCATION_ID` bigint(20) DEFAULT NULL,
  `LOCATION_DISPLAY_NAME` varchar(75) DEFAULT NULL,
  `LATITUDE` decimal(10,6) DEFAULT NULL,
  `LONGITUDE` decimal(10,6) DEFAULT NULL,
  `DISP_N1` decimal(20,2) DEFAULT NULL,
  `DISP_S1` varchar(255) DEFAULT NULL,
  `DISP_N2` decimal(20,2) DEFAULT NULL,
  `DISP_S2` varchar(255) DEFAULT NULL,
  `DISP_N3` decimal(20,2) DEFAULT NULL,
  `DISP_S3` varchar(255) DEFAULT NULL,
  `DISP_N4` decimal(20,2) DEFAULT NULL,
  `DISP_S4` varchar(255) DEFAULT NULL,
  `DISP_N5` decimal(20,2) DEFAULT NULL,
  `DISP_S5` varchar(255) DEFAULT NULL,
  `DISP_N6` decimal(20,2) DEFAULT NULL,
  `DISP_S6` varchar(255) DEFAULT NULL,
  `DISP_N7` decimal(20,2) DEFAULT NULL,
  `DISP_S7` varchar(255) DEFAULT NULL,
  `DISP_N8` decimal(20,2) DEFAULT NULL,
  `DISP_S8` varchar(255) DEFAULT NULL,
  `DISP_N9` decimal(20,2) DEFAULT NULL,
  `DISP_S9` varchar(255) DEFAULT NULL,
  `DISP_N10` decimal(20,2) DEFAULT NULL,
  `DISP_S10` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `IDX_SCFI_CTX_ID` (`CONTEXT_ID`),
  KEY `IDX_SCFI_F_ID` (`FEATURE_ID`),
  KEY `IDX_SCFI_VS` (`VALUE_STRING`),
  KEY `IDX_SCFI_VN` (`VALUE_NUMBER`),
  KEY `IDX_SCFI_VT` (`VALUE_TYPE`),
  KEY `IDX_SCFI_IP` (`IMAGE_PRESENT`),
  KEY `IDX_SCFI_PS` (`PROCESSING_STATE`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sem_content_feature_info`
--

LOCK TABLES `sem_content_feature_info` WRITE;
/*!40000 ALTER TABLE `sem_content_feature_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `sem_content_feature_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sem_content_kw_match`
--

DROP TABLE IF EXISTS `sem_content_kw_match`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sem_content_kw_match` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `query_id` bigint(20) NOT NULL,
  `semantified_content_id` bigint(20) DEFAULT NULL,
  `match_score` decimal(5,2) NOT NULL,
  `execution_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `context_id` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `IDX_SCKWM_CTX_ID` (`context_id`),
  KEY `idx_sckwm_q_id` (`query_id`),
  KEY `idx_sckwm_sc_id` (`semantified_content_id`),
  KEY `idx_sckwm_ed` (`execution_date`)
) ENGINE=MyISAM AUTO_INCREMENT=366659 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sem_content_kw_match`
--

LOCK TABLES `sem_content_kw_match` WRITE;
/*!40000 ALTER TABLE `sem_content_kw_match` DISABLE KEYS */;
/*!40000 ALTER TABLE `sem_content_kw_match` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `semantified_content`
--

DROP TABLE IF EXISTS `semantified_content`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `semantified_content` (
  `id` bigint(20) NOT NULL,
  `source` varchar(255) DEFAULT NULL,
  `context_id` bigint(20) NOT NULL,
  `source_content_id` bigint(20) DEFAULT NULL,
  `url` text,
  `image_url` text,
  `short_desc` text,
  `long_desc` text,
  `content_date` datetime DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `image_url_processed` varchar(2) DEFAULT 'n',
  `batch_id` int(11) DEFAULT NULL,
  `processing_state` varchar(2) DEFAULT 'N',
  `user_query_id` bigint(20) DEFAULT NULL,
  `location_id` bigint(20) DEFAULT NULL,
  `location_display_name` varchar(75) DEFAULT NULL,
  `latitude` decimal(10,6) DEFAULT NULL,
  `longitude` decimal(10,6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `IDX_SEMC_BI` (`batch_id`),
  KEY `IDX_SEMC_PS` (`processing_state`),
  KEY `IDX_SEMC_UQ_ID` (`user_query_id`),
  KEY `idx_semc_cd` (`content_date`),
  KEY `idx_semc_iurl_p` (`image_url_processed`),
  KEY `idx_semc_sc_id` (`source_content_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `semantified_content`
--

LOCK TABLES `semantified_content` WRITE;
/*!40000 ALTER TABLE `semantified_content` DISABLE KEYS */;
/*!40000 ALTER TABLE `semantified_content` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `semantified_content_key_word`
--

DROP TABLE IF EXISTS `semantified_content_key_word`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `semantified_content_key_word` (
  `id` int(20) NOT NULL,
  `semantified_content_id` bigint(20) DEFAULT NULL,
  `key_word_text` text,
  `content_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `context_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `IDX_SCKW_CTX_ID` (`context_id`),
  KEY `idx_sckw_sc_id` (`semantified_content_id`),
  KEY `idx_sckw_cd` (`content_date`),
  FULLTEXT KEY `idx_sckw_kwt` (`key_word_text`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `semantified_content_key_word`
--

LOCK TABLES `semantified_content_key_word` WRITE;
/*!40000 ALTER TABLE `semantified_content_key_word` DISABLE KEYS */;
/*!40000 ALTER TABLE `semantified_content_key_word` ENABLE KEYS */;
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
INSERT INTO `sequences` VALUES ('SEMANTIFIED_CONTENT_KEY_WORD',1);
INSERT INTO `sequences` VALUES ('SEM_CONTENT_FEATURE_INFO',1);
INSERT INTO `sequences` VALUES ('USER_QUERY_FEATURE_INFO',1);
INSERT INTO `sequences` VALUES ('FEATURE',1);
INSERT INTO `sequences` VALUES ('SEMANTIFIED_CONTENT',1);
INSERT INTO `sequences` VALUES ('FEATURE_VALUE',1);
INSERT INTO `sequences` VALUES ('FEATURE_RANGE',1);
INSERT INTO `sequences` VALUES ('SEM_CONTENT_KW_MATCH',1);
INSERT INTO `sequences` VALUES ('USER_QUERY_LOCATION_INFO',1);
INSERT INTO `sequences` VALUES ('FEATURE_DETAIL',1);
INSERT INTO `sequences` VALUES ('FEATURE_DEPENDENCY',1);
INSERT INTO `sequences` VALUES ('RI_FEATURE_CONTENT',1);
INSERT INTO `sequences` VALUES ('FEATURE_RANGE_INFO',1);
/*!40000 ALTER TABLE `sequences` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `source_content`
--

DROP TABLE IF EXISTS `source_content`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `source_content` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `url` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `description` text,
  `source` varchar(255) DEFAULT NULL,
  `context_id` bigint(20) NOT NULL,
  `PROCESSED` char(2) DEFAULT 'N',
  `ADDED_DATE` datetime DEFAULT NULL,
  `FAILURE_CAUSE` text,
  `BATCH_ID` int(10) DEFAULT NULL,
  `source_item_id` bigint(20) NOT NULL,
  `source_server_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `IDX_SC_CTX_ID` (`context_id`),
  KEY `IDX_SC_P` (`PROCESSED`),
  KEY `IDX_SC_B_ID` (`BATCH_ID`),
  KEY `IDX_SC_SI_ID` (`source_item_id`),
  KEY `IDX_SC_SS_ID` (`source_server_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `source_content`
--

LOCK TABLES `source_content` WRITE;
/*!40000 ALTER TABLE `source_content` DISABLE KEYS */;
/*!40000 ALTER TABLE `source_content` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_query_feature_info`
--

DROP TABLE IF EXISTS `user_query_feature_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_query_feature_info` (
  `ID` bigint(20) NOT NULL,
  `QUERY_ID` bigint(20) NOT NULL,
  `CONTEXT_ID` bigint(20) DEFAULT NULL,
  `FEATURE_ID` bigint(20) DEFAULT NULL,
  `START_VALUE` varchar(255) DEFAULT NULL,
  `END_VALUE` varchar(255) DEFAULT NULL,
  `FEATURE_WEIGHT_FACTOR` double(5,2) NOT NULL DEFAULT '1.00',
  `EXECUTION_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `REQUIRED` int(1) DEFAULT '0',
  PRIMARY KEY (`ID`),
  KEY `IDX_UQFI_Q_ID` (`QUERY_ID`),
  KEY `IDX_UQFI_CTX_ID` (`CONTEXT_ID`),
  KEY `IDX_UQFI_F_ID` (`FEATURE_ID`),
  KEY `IDX_UQFI_SV` (`START_VALUE`),
  KEY `IDX_UQFI_EV` (`END_VALUE`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_query_feature_info`
--

LOCK TABLES `user_query_feature_info` WRITE;
/*!40000 ALTER TABLE `user_query_feature_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_query_feature_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_query_location_info`
--

DROP TABLE IF EXISTS `user_query_location_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_query_location_info` (
  `ID` bigint(20) NOT NULL,
  `QUERY_ID` bigint(20) NOT NULL,
  `CONTEXT_ID` bigint(20) NOT NULL,
  `LOCATION_ID` bigint(20) NOT NULL,
  `LATITUDE` decimal(10,6) NOT NULL,
  `LONGITUDE` decimal(10,6) NOT NULL,
  `EXECUTION_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`),
  KEY `IDX_UQLI_QID` (`QUERY_ID`),
  KEY `IDX_UQLI_CTX_ID` (`CONTEXT_ID`),
  KEY `IDX_UQLI_L_ID` (`LOCATION_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_query_location_info`
--

LOCK TABLES `user_query_location_info` WRITE;
/*!40000 ALTER TABLE `user_query_location_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_query_location_info` ENABLE KEYS */;
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
