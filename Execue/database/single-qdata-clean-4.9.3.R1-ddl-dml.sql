--
-- Host: localhost    Database: qdata-base
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
-- Table structure for table `ac_context`
--

DROP TABLE IF EXISTS `ac_context`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ac_context` (
  `ID` bigint(20) NOT NULL,
  `ASSET_ID` bigint(20) NOT NULL,
  `CONTEXT_DATA` text,
  `LATEST_OPERATION` varchar(35) NOT NULL,
  `USER_ID` bigint(20) NOT NULL,
  `PARENT_ASSET_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ac_context`
--

LOCK TABLES `ac_context` WRITE;
/*!40000 ALTER TABLE `ac_context` DISABLE KEYS */;
/*!40000 ALTER TABLE `ac_context` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ac_mgmt_queue`
--

DROP TABLE IF EXISTS `ac_mgmt_queue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ac_mgmt_queue` (
  `id` bigint(20) NOT NULL,
  `asset_id` bigint(20) DEFAULT NULL,
  `parent_asset_id` bigint(20) DEFAULT NULL,
  `dependent_mgmt_queue_id` varchar(255) DEFAULT NULL,
  `operation_type` varchar(35) NOT NULL,
  `operation_context` text,
  `requested_date` datetime NOT NULL,
  `operation_src_type` varchar(35) NOT NULL,
  `operation_status` varchar(35) NOT NULL,
  `remarks` text,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ac_mgmt_queue`
--

LOCK TABLES `ac_mgmt_queue` WRITE;
/*!40000 ALTER TABLE `ac_mgmt_queue` DISABLE KEYS */;
/*!40000 ALTER TABLE `ac_mgmt_queue` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `additional_info`
--

DROP TABLE IF EXISTS `additional_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `additional_info` (
  `id` bigint(20) NOT NULL,
  `instance_name` varchar(255) DEFAULT NULL,
  `external_id` char(50) NOT NULL,
  `wiki_url_id` text,
  `be_id` bigint(20) DEFAULT NULL,
  `netflix_url_id` text,
  `imdb_url_id` text,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=1853 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `additional_info`
--

LOCK TABLES `additional_info` WRITE;
/*!40000 ALTER TABLE `additional_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `additional_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `aggregated_query`
--

DROP TABLE IF EXISTS `aggregated_query`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `aggregated_query` (
  `ID` bigint(20) NOT NULL,
  `ASSET_ID` bigint(20) DEFAULT NULL,
  `USER_QUERY_ID` bigint(20) DEFAULT NULL,
  `BUSINESS_QUERY_ID` bigint(20) DEFAULT NULL,
  `TYPE` int(2) DEFAULT NULL,
  `EXECUTION_DATE` datetime DEFAULT NULL,
  `RELEVANCE` decimal(6,3) DEFAULT NULL,
  `TITLE` mediumtext,
  `ENGLISH_QUERY_STRING` mediumtext,
  `GOVERNOR_QUERY_STRING` mediumtext,
  `AGGREGATED_QUERY_STRING` mediumtext,
  `TOTAL_SUMMARY_TABLE_NAME` varchar(255) DEFAULT NULL,
  `GOVERNOR_QRY_STRING_STRUCT` mediumtext,
  `AGG_QRY_STRING_STRUCT` mediumtext,
  `DATA_PRESENT` char(1) DEFAULT NULL,
  `DATA_EXTRACTED` char(1) DEFAULT NULL,
  `QUERY_EXECUTION_TIME` bigint(20) DEFAULT NULL,
  `REPORT_META_INFO_STRUCTURE` mediumtext,
  `ASSET_WEIGHT` decimal(6,3) NOT NULL DEFAULT '0.000',
  PRIMARY KEY (`ID`),
  KEY `FK_AQ_UQID` (`USER_QUERY_ID`),
  KEY `FK_AQ_BQID` (`BUSINESS_QUERY_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aggregated_query`
--

LOCK TABLES `aggregated_query` WRITE;
/*!40000 ALTER TABLE `aggregated_query` DISABLE KEYS */;
/*!40000 ALTER TABLE `aggregated_query` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `aggregated_query_column`
--

DROP TABLE IF EXISTS `aggregated_query_column`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `aggregated_query_column` (
  `ID` bigint(20) NOT NULL,
  `AGGREGATED_QUERY_ID` bigint(20) DEFAULT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `STAT` varchar(35) DEFAULT NULL,
  `QUERY_SECTION` varchar(35) DEFAULT NULL,
  `OPERATR` varchar(35) DEFAULT NULL,
  `VALU` varchar(255) DEFAULT NULL,
  `BUSINESS_ENTITY_ID` bigint(20) NOT NULL,
  `IS_REQUESTED_BY_USER` char(1) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_AQC_AQID` (`AGGREGATED_QUERY_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aggregated_query_column`
--

LOCK TABLES `aggregated_query_column` WRITE;
/*!40000 ALTER TABLE `aggregated_query_column` DISABLE KEYS */;
/*!40000 ALTER TABLE `aggregated_query_column` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `aggregated_report_type`
--

DROP TABLE IF EXISTS `aggregated_report_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `aggregated_report_type` (
  `ID` bigint(20) NOT NULL,
  `AGGREGATED_QUERY_ID` bigint(20) DEFAULT NULL,
  `REPORT_TYPE` int(3) NOT NULL DEFAULT '1',
  PRIMARY KEY (`ID`),
  KEY `FK_AQR_AQID` (`AGGREGATED_QUERY_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aggregated_report_type`
--

LOCK TABLES `aggregated_report_type` WRITE;
/*!40000 ALTER TABLE `aggregated_report_type` DISABLE KEYS */;
/*!40000 ALTER TABLE `aggregated_report_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `app_news_popularity`
--

DROP TABLE IF EXISTS `app_news_popularity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `app_news_popularity` (
  `ID` bigint(20) NOT NULL,
  `APP_ID` bigint(20) NOT NULL,
  `ENTITY_BE_ID` bigint(20) NOT NULL,
  `HITS` int(4) NOT NULL,
  `UDX_ID` bigint(20) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `app_news_popularity`
--

LOCK TABLES `app_news_popularity` WRITE;
/*!40000 ALTER TABLE `app_news_popularity` DISABLE KEYS */;
/*!40000 ALTER TABLE `app_news_popularity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `application_category_mapping`
--

DROP TABLE IF EXISTS `application_category_mapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `application_category_mapping` (
  `ID` bigint(20) NOT NULL,
  `CONTEXT_ID` bigint(20) NOT NULL,
  `CATEGORY_NAME` varchar(255) NOT NULL,
  `SEARCH_FILTER_TYPE` int(2) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `application_category_mapping`
--

LOCK TABLES `application_category_mapping` WRITE;
/*!40000 ALTER TABLE `application_category_mapping` DISABLE KEYS */;
INSERT INTO `application_category_mapping` VALUES (1001,1001,'Finance',2);
INSERT INTO `application_category_mapping` VALUES (1002,1508,'Craigslist',1);
/*!40000 ALTER TABLE `application_category_mapping` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `business_query`
--

DROP TABLE IF EXISTS `business_query`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `business_query` (
  `ID` bigint(20) NOT NULL,
  `USER_QUERY_ID` bigint(20) DEFAULT NULL,
  `EXECUTION_DATE` datetime DEFAULT NULL,
  `QUERY_STRING` mediumtext,
  `APP_NAME` varchar(255) DEFAULT NULL,
  `REQ_RECOG_AS` text,
  `APP_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_BQ_UQID` (`USER_QUERY_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `business_query`
--

LOCK TABLES `business_query` WRITE;
/*!40000 ALTER TABLE `business_query` DISABLE KEYS */;
/*!40000 ALTER TABLE `business_query` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `business_query_column`
--

DROP TABLE IF EXISTS `business_query_column`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `business_query_column` (
  `ID` bigint(20) NOT NULL,
  `BUSINESS_QUERY_ID` bigint(20) DEFAULT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `STAT` varchar(35) DEFAULT NULL,
  `QUERY_SECTION` varchar(35) DEFAULT NULL,
  `OPERATR` varchar(35) DEFAULT NULL,
  `VALU` varchar(255) DEFAULT NULL,
  `BUSINESS_ENTITY_ID` bigint(20) NOT NULL,
  `IS_REQUESTED_BY_USER` char(1) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_BQC_UQID` (`BUSINESS_QUERY_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `business_query_column`
--

LOCK TABLES `business_query_column` WRITE;
/*!40000 ALTER TABLE `business_query_column` DISABLE KEYS */;
/*!40000 ALTER TABLE `business_query_column` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cached_report_results`
--

DROP TABLE IF EXISTS `cached_report_results`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cached_report_results` (
  `ID` bigint(20) NOT NULL,
  `AGGREGATED_QUERY_ID` bigint(20) DEFAULT NULL,
  `REPORT_TYPE_ID` int(3) DEFAULT NULL,
  `META_INFO` mediumtext,
  `REPORT_DATA` mediumblob,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cached_report_results`
--

LOCK TABLES `cached_report_results` WRITE;
/*!40000 ALTER TABLE `cached_report_results` DISABLE KEYS */;
/*!40000 ALTER TABLE `cached_report_results` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `h_operational_status`
--

DROP TABLE IF EXISTS `h_operational_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `h_operational_status` (
  `ID` bigint(20) NOT NULL,
  `JOB_REQUEST_ID` bigint(20) NOT NULL,
  `TYPE` int(2) NOT NULL,
  `OPERATION_STAGE` varchar(255) NOT NULL,
  `STATUS` int(2) NOT NULL,
  `STATUS_DETAIL` text,
  `START_DATE` datetime DEFAULT NULL,
  `END_DATE` datetime DEFAULT NULL,
  `USER_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_HOS_JRQID` (`JOB_REQUEST_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `h_operational_status`
--

LOCK TABLES `h_operational_status` WRITE;
/*!40000 ALTER TABLE `h_operational_status` DISABLE KEYS */;
/*!40000 ALTER TABLE `h_operational_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `job_request`
--

DROP TABLE IF EXISTS `job_request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `job_request` (
  `ID` bigint(20) NOT NULL,
  `TYPE` int(2) NOT NULL,
  `REQUEST_DATA` text,
  `STATUS` int(2) NOT NULL,
  `requested_date` datetime DEFAULT NULL,
  `completion_date` datetime DEFAULT NULL,
  `USER_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `job_request`
--

LOCK TABLES `job_request` WRITE;
/*!40000 ALTER TABLE `job_request` DISABLE KEYS */;
/*!40000 ALTER TABLE `job_request` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `message`
--

DROP TABLE IF EXISTS `message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `message` (
  `ID` bigint(20) NOT NULL,
  `TRANSACTION_ID` bigint(20) DEFAULT NULL,
  `TYPE` varchar(50) DEFAULT NULL,
  `CURRENT_STATUS` varchar(50) DEFAULT NULL,
  `DATE_CREATED` datetime DEFAULT NULL,
  `DATE_MODIFIED` datetime DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `message`
--

LOCK TABLES `message` WRITE;
/*!40000 ALTER TABLE `message` DISABLE KEYS */;
/*!40000 ALTER TABLE `message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `message_history`
--

DROP TABLE IF EXISTS `message_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `message_history` (
  `ID` bigint(20) NOT NULL,
  `MESSAGE_ID` bigint(20) NOT NULL,
  `MESSAGE_TYPE` varchar(50) DEFAULT NULL,
  `STATUS` varchar(50) DEFAULT NULL,
  `DATE_CREATED` datetime DEFAULT NULL,
  `DATE_MODIFIED` datetime DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `message_history`
--

LOCK TABLES `message_history` WRITE;
/*!40000 ALTER TABLE `message_history` DISABLE KEYS */;
/*!40000 ALTER TABLE `message_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `news_item`
--

DROP TABLE IF EXISTS `news_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `news_item` (
  `url` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `description` text,
  `source` varchar(255) DEFAULT NULL,
  `category` varchar(255) NOT NULL,
  `ID` bigint(20) NOT NULL,
  `PROCESSED` char(2) DEFAULT 'N',
  `ADDED_DATE` datetime DEFAULT NULL,
  `FAILURE_CAUSE` text,
  `BATCH_ID` int(10) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM AUTO_INCREMENT=3719 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `news_item`
--

LOCK TABLES `news_item` WRITE;
/*!40000 ALTER TABLE `news_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `news_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notification`
--

DROP TABLE IF EXISTS `notification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `notification` (
  `ID` bigint(20) NOT NULL,
  `CREATED_DATE` datetime NOT NULL,
  `CATEGORY` int(2) DEFAULT '3',
  `NOTIFICATION_TYPE` int(2) DEFAULT '99',
  `SUBJECT` text NOT NULL,
  `USER_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notification`
--

LOCK TABLES `notification` WRITE;
/*!40000 ALTER TABLE `notification` DISABLE KEYS */;
/*!40000 ALTER TABLE `notification` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notification_detail`
--

DROP TABLE IF EXISTS `notification_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `notification_detail` (
  `ID` bigint(20) NOT NULL,
  `NOTIFICATION_ID` bigint(20) NOT NULL,
  `PARAM_NAME` varchar(55) NOT NULL,
  `PARAM_VALUE` varchar(255) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `IDX_NOTIF_DET_ID` (`NOTIFICATION_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notification_detail`
--

LOCK TABLES `notification_detail` WRITE;
/*!40000 ALTER TABLE `notification_detail` DISABLE KEYS */;
/*!40000 ALTER TABLE `notification_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notification_template`
--

DROP TABLE IF EXISTS `notification_template`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `notification_template` (
  `ID` bigint(20) NOT NULL,
  `NOTIFICATION_TYPE` int(2) NOT NULL,
  `TEMPLATE_TYPE` int(2) NOT NULL,
  `TEMPLATE` longtext NOT NULL,
  `PARAM_NAMES` text,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notification_template`
--

LOCK TABLES `notification_template` WRITE;
/*!40000 ALTER TABLE `notification_template` DISABLE KEYS */;
INSERT INTO `notification_template` VALUES (1000,1,1,'Cube Creation Request has been &Operation for Dataset Collection &AssetName','AssetName~Operation');
INSERT INTO `notification_template` VALUES (1001,1,2,'Cube Creation Request has been &Operation for<br>Dataset Collection &AssetName at &TimeStamp','AssetName~TimeStamp~Operation');
INSERT INTO `notification_template` VALUES (1002,2,1,'','');
INSERT INTO `notification_template` VALUES (1003,2,2,'','');
INSERT INTO `notification_template` VALUES (1004,3,1,'','');
INSERT INTO `notification_template` VALUES (1005,3,2,'','');
INSERT INTO `notification_template` VALUES (1006,4,1,'','');
INSERT INTO `notification_template` VALUES (1007,4,2,'','');
INSERT INTO `notification_template` VALUES (1008,5,1,'','');
INSERT INTO `notification_template` VALUES (1009,5,2,'','');
INSERT INTO `notification_template` VALUES (1010,6,1,'Sfl term token weight updation job has been &Operation on Model Name : &ModelName','Operation~ModelName');
INSERT INTO `notification_template` VALUES (1011,6,2,'Sfl term token weight updation job has been &Operation on Model Name : &ModelName at &TimeStamp','Operation~ModelName~TimeStamp');
INSERT INTO `notification_template` VALUES (1012,7,1,'','');
INSERT INTO `notification_template` VALUES (1013,7,2,'','');
INSERT INTO `notification_template` VALUES (1014,8,1,'Popularity Hit Maintenance has been &Operation','Operation');
INSERT INTO `notification_template` VALUES (1015,8,2,'Popularity Hit Maintenance has been &Operation at &TimeStamp','Operation~TimeStamp');
INSERT INTO `notification_template` VALUES (1016,9,1,'File Evaluation Request has been &Operation on File Name &FileName','FileName~Operation');
INSERT INTO `notification_template` VALUES (1017,9,2,'File Evaluation Request has been &Operation on<br>File Name &FileName','FileName~Operation');
INSERT INTO `notification_template` VALUES (1018,10,1,'File Evaluation Request has been &Operation on File Name &FileName','FileName~Operation');
INSERT INTO `notification_template` VALUES (1019,10,2,'File Evaluation Request has been &Operation on<br>File Name &FileName for application &ApplicationName','FileName~ApplicationName~Operation');
INSERT INTO `notification_template` VALUES (1020,11,1,'File ontology data absorption job has been &Operation on Model Name : &ModelName','Operation~ModelName');
INSERT INTO `notification_template` VALUES (1021,11,2,'File ontology data absorption job has been &Operation on Model Name : &ModelName at &TimeStamp','Operation~ModelName~TimeStamp');
INSERT INTO `notification_template` VALUES (1022,12,1,'RI Onto Term absorption job has been &Operation on Model Name : &ModelName','Operation~ModelName');
INSERT INTO `notification_template` VALUES (1023,12,2,'RI Onto Term absorption job has been &Operation on Model Name : &ModelName at &TimeStamp','Operation~ModelName~TimeStamp');
INSERT INTO `notification_template` VALUES (1024,13,1,'Snow flakes Terms Absorption job has been &Operation on Model Name : &ModelName','Operation~ModelName');
INSERT INTO `notification_template` VALUES (1025,13,2,'Snow flakes Terms Absorption job has been &Operation on Model Name : &ModelName at &TimeStamp','Operation~ModelName~TimeStamp');
INSERT INTO `notification_template` VALUES (1026,14,1,'Correct Mappings has been &Operation on Application Name : &ApplicationName','Operation~ApplicationName');
INSERT INTO `notification_template` VALUES (1027,14,2,'Correct Mappings has been &Operation on Application Name : &ApplicationName at &TimeStamp','Operation~ApplicationName~TimeStamp');
INSERT INTO `notification_template` VALUES (1030,16,1,'Application Deletion has been &Operation on Application Name : &ApplicationName','Operation~ApplicationName');
INSERT INTO `notification_template` VALUES (1031,16,2,'Application Deletion has been &Operation on Application Name : &ApplicationName at &TimeStamp','Operation~ApplicationName~TimeStamp');
INSERT INTO `notification_template` VALUES (1032,17,1,'Asset Deletion has been &Operation on Asset Name : &AssetName','Operation~AssetName');
INSERT INTO `notification_template` VALUES (1033,17,2,'Asset Deletion has been &Operation on Asset Name : &AssetName at &TimeStamp','Operation~AssetName~TimeStamp');
INSERT INTO `notification_template` VALUES (1034,18,1,'Popularity Collection has been &Operation on Application Name : &ApplicationName','Operation~ApplicationName');
INSERT INTO `notification_template` VALUES (1035,18,2,'Popularity Collection has been &Operation on Application Name : &ApplicationName at &TimeStamp','Operation~ApplicationName~TimeStamp');
INSERT INTO `notification_template` VALUES (1036,19,1,'Popularity Dispersion has been &Operation on Application Name : &ApplicationName','Operation~ApplicationName');
INSERT INTO `notification_template` VALUES (1037,19,2,'Popularity Dispersion has been &Operation on Application Name : &ApplicationName at &TimeStamp','Operation~ApplicationName~TimeStamp');
INSERT INTO `notification_template` VALUES (1040,21,1,'Member Absorption has been &Operation on Asset Name &AssetName and Table Name &TableName','Operation~AssetName~TableName');
INSERT INTO `notification_template` VALUES (1041,21,2,'Member Absorption has been &Operation on Asset Name &AssetName and Table Name &TableName at &TimeStamp','Operation~ApplicationName~TableName~TimeStamp');
INSERT INTO `notification_template` VALUES (1042,22,1,'RIOntoTermPopularityHitMaintainence has been &Operation','Operation');
INSERT INTO `notification_template` VALUES (1043,22,2,'RIOntoTermPopularityHitMaintainence as been &Operation at &TimeStamp','Operation~TimeStamp');
INSERT INTO `notification_template` VALUES (1044,23,1,'','');
INSERT INTO `notification_template` VALUES (1045,23,2,'','');
INSERT INTO `notification_template` VALUES (1046,24,1,'Publish Asset has been &Operation on Application Name &ApplicationName and Asset Name &AssetName','Operation~ApplicationName~AssetName');
INSERT INTO `notification_template` VALUES (1047,24,2,'Publish Asset has been &Operation on Application Name &ApplicationName and Asset Name &AssetName at &TimeStamp','Operation~ApplicationName~AssetName~TimeStamp');
INSERT INTO `notification_template` VALUES (1048,25,1,'Index Farm Management has been &Operation on Application Name : &ApplicationName','Operation~ApplicationName');
INSERT INTO `notification_template` VALUES (1049,25,2,'Index Farm Management has been &Operation on Application Name : &ApplicationName at &TimeStamp','Operation~ApplicationName~TimeStamp');
INSERT INTO `notification_template` VALUES (1050,26,1,'Instance Absorption has been &Operation on Asset Name &AssetName','Operation~AssetName');
INSERT INTO `notification_template` VALUES (1051,26,2,'Instance Absorption has been &Operation on Asset Name &AssetName at &TimeStamp','Operation~ApplicationName~TimeStamp');
INSERT INTO `notification_template` VALUES (1052,27,1,'DefaultMetrics Population has been &Operation at &TimeStamp','Operation~TimeStamp');
INSERT INTO `notification_template` VALUES (1053,27,2,'DefaultMetrics Population has been &Operation at &TimeStamp','Operation~TimeStamp');
INSERT INTO `notification_template` VALUES (1054,97,1,'Default Metrics has been &Operation on Application Name : &ApplicationName','Operation~ApplicationName');
INSERT INTO `notification_template` VALUES (1055,97,2,'Default Metrics has been &Operation on Application Name : &ApplicationName at &TimeStamp','Operation~ApplicationName~TimeStamp');
INSERT INTO `notification_template` VALUES (1056,99,1,'','');
INSERT INTO `notification_template` VALUES (1057,99,2,'','');
INSERT INTO `notification_template` VALUES (1058,98,1,'','');
INSERT INTO `notification_template` VALUES (1059,98,2,'','');
INSERT INTO `notification_template` VALUES (1060,30,1,'','');
INSERT INTO `notification_template` VALUES (1061,30,2,'','');
INSERT INTO `notification_template` VALUES (1062,31,1,'RuntimeTablesCleanUp has been &Operation at &TimeStamp','Operation~TimeStamp');
INSERT INTO `notification_template` VALUES (1063,31,2,'RuntimeTablesCleanUp has been &Operation at &TimeStamp','Operation~TimeStamp');
INSERT INTO `notification_template` VALUES (1066,33,1,'Scheduled EAS Index Refresh has been &Operation','Operation');
INSERT INTO `notification_template` VALUES (1067,33,2,'Scheduled EAS Index Refresh has been &Operation at &TimeStamp','Operation~TimeStamp');
INSERT INTO `notification_template` VALUES (1070,35,1,'Craigslist Runtime tables cleanup Request has been &Operation','Operation');
INSERT INTO `notification_template` VALUES (1071,35,2,'Craigslist Runtime tables cleanup Request has been &Operation at &TimeStamp','Operation~TimeStamp');
/*!40000 ALTER TABLE `notification_template` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `operational_status`
--

DROP TABLE IF EXISTS `operational_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `operational_status` (
  `ID` bigint(20) NOT NULL,
  `JOB_REQUEST_ID` bigint(20) NOT NULL,
  `TYPE` int(2) NOT NULL,
  `OPERATION_STAGE` varchar(255) NOT NULL,
  `STATUS` int(2) NOT NULL,
  `STATUS_DETAIL` text,
  `START_DATE` datetime DEFAULT NULL,
  `END_DATE` datetime DEFAULT NULL,
  `USER_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_OS_JRQID` (`JOB_REQUEST_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `operational_status`
--

LOCK TABLES `operational_status` WRITE;
/*!40000 ALTER TABLE `operational_status` DISABLE KEYS */;
/*!40000 ALTER TABLE `operational_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `optimal_dset_swi_info`
--

DROP TABLE IF EXISTS `optimal_dset_swi_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `optimal_dset_swi_info` (
  `ID` bigint(20) NOT NULL,
  `ASSET_ID` bigint(20) NOT NULL,
  `BUSINESS_ENTITY_ID` bigint(20) NOT NULL,
  `KDX_DATA_TYPE` varchar(35) NOT NULL,
  `CONCEPT_NAME` varchar(255) NOT NULL,
  `RANGE_ID` bigint(20) DEFAULT NULL,
  `MEMBER_COUNT` int(6) DEFAULT '0',
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `optimal_dset_swi_info`
--

LOCK TABLES `optimal_dset_swi_info` WRITE;
/*!40000 ALTER TABLE `optimal_dset_swi_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `optimal_dset_swi_info` ENABLE KEYS */;
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
) ENGINE=MyISAM AUTO_INCREMENT=32 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patch_info`
--

LOCK TABLES `patch_info` WRITE;
/*!40000 ALTER TABLE `patch_info` DISABLE KEYS */;
INSERT INTO `patch_info` VALUES (1,'qdata-clean-4.7.4.R1.00','DDL','For Adding Category Based Patch Number to Patch Info table','2011-03-09','2012-09-03 02:00:16','Raju Gottumukkala','4.7.4.R1','00','CleanApp','00','QDATA');
INSERT INTO `patch_info` VALUES (2,'4.7.4.R1-qdata-clean-C01-U01','DDL','Created new table in qdata schema','2011-03-16','2012-09-03 02:00:16','Shobhit Sharma','4.7.4.R1','U01','CleanApp','U01','QDATA');
INSERT INTO `patch_info` VALUES (3,'qdata-clean-4.7.5.R1.01','DDL','Script to add the ARTICLE_REF_ID column to UDX table','2011-03-22','2012-09-03 02:00:16','Murthy','4.7.5.R1','01','CleanApp','01','QDATA');
INSERT INTO `patch_info` VALUES (4,'4.7.5.R1-qdata-clean-C02-U03','DDL','Alter table for cached report results','2011-03-24','2012-09-03 02:00:17','Shobhit Sharma','4.7.5.R1','U03','CleanApp','U03','QDATA');
INSERT INTO `patch_info` VALUES (5,'qdata-clean-4.7.6.R1.01','DDL','Script to create the application_category_mapping table','2011-04-21','2012-09-03 02:00:17','Murthy','4.7.6.R1','01','CleanApp','01','QDATA');
INSERT INTO `patch_info` VALUES (6,'qdata-portal-4.7.6.R1.02','DDL','Script to insert the entries for finance and craigslist in the application_category_mapping table',NULL,'2012-09-03 02:00:17','Nitesh','4.7.6.R1','02','PortalApp','02','QDATA');
INSERT INTO `patch_info` VALUES (7,'qdata-portal-4.7.6.R1.03','DDL','Script to add colums to user_query table','2011-05-10','2012-09-03 02:00:17','John','4.7.6.R1','03','PortalApp','03','QDATA');
INSERT INTO `patch_info` VALUES (8,'qdata-portal-4.7.6.R1.04','DDL','Script to delete qrtz_job_details ','2011-05-10','2012-09-03 02:00:17','Murthy','4.7.6.R1','04','PortalApp','04','QDATA');
INSERT INTO `patch_info` VALUES (9,'4.8.2.R1-qdata-clean-C00-U00','DML','Blank Script just to denote the release','2011-08-04','2012-09-03 02:00:17','Raju Gottumukkala','4.8.2.R1','U00','CleanApp','C00','QDATA');
INSERT INTO `patch_info` VALUES (10,'4.8.3.R1-qdata-clean-C00-U00','DML','Blank Script just to denote the release','2011-08-19','2012-09-03 02:00:17','Raju Gottumukkala','4.8.3.R1','U00','CleanApp','C00','QDATA');
INSERT INTO `patch_info` VALUES (11,'4.8.4.R1-qdata-clean-C00-U00','DML','Blank patch to denote code release','2011-10-25','2012-09-03 02:00:17','Raju Gottumukkala','4.8.4.R1','U00','CleanApp','C00','QDATA');
INSERT INTO `patch_info` VALUES (12,'4.8.4.R1-qdata-clean-C01-U01','DDL','Alter script to rename column names','2012-02-14','2012-09-03 02:00:17','Aditya','4.8.4.R1','U01','CleanApp','C01','QDATA');
INSERT INTO `patch_info` VALUES (13,'4.8.4.R1-qdata-clean-C02-U02','DDL','Alter script for Indexes re-creation','2012-02-14','2012-09-03 02:00:23','Aditya','4.8.4.R1','U02','CleanApp','C02','QDATA');
INSERT INTO `patch_info` VALUES (14,'4.8.4.R1-qdata-clean-C03-U03','DDL','Added new table optimal_dset_swi_info','2012-03-07','2012-09-03 02:00:23','Aditya Gole','4.8.4.R1','U03','CleanApp','C03','QDATA');
INSERT INTO `patch_info` VALUES (15,'4.8.5.R1-qdata-clean-C00-U00','DML','Blank patch to denote code release','2012-03-19','2012-09-03 02:00:23','Raju Gottumukkala','4.8.5.R1','U00','CleanApp','C00','QDATA');
INSERT INTO `patch_info` VALUES (16,'4.8.5.R1-qdata-clean-C01-U01','DDL','Drop Status, Start Date, End Date columns and add Parent Asset Id column','2012-03-22','2012-09-03 02:00:23','Raju Gottumukkala','4.8.5.R1','U01','CleanApp','C01','QDATA');
INSERT INTO `patch_info` VALUES (17,'4.8.5.R1-qdata-clean-C02-U02','DDL','Add a new column query_execution_time to aggregated_query table','2012-03-26','2012-09-03 02:00:23','John Mallavalli','4.8.5.R1','U02','CleanApp','C02','QDATA');
INSERT INTO `patch_info` VALUES (18,'4.8.5.R1-qdata-clean-C03-U03','DDL','Add a new table AC_MGMT_QUEUE (ANSWERS CATALOG MANAGEMENT QUEUE)','2012-03-27','2012-09-03 02:00:23','Raju Gottumukkala','4.8.5.R1','U03','CleanApp','C03','QDATA');
INSERT INTO `patch_info` VALUES (19,'4.8.5.R1-qdata-clean-C04-U04','DDL','Made assetId nullable in AC_MGMT_QUEUE (ANSWERS CATALOG MANAGEMENT QUEUE) table','2012-04-06','2012-09-03 02:00:23','Aditya Gole','4.8.5.R1','U04','CleanApp','C04','QDATA');
INSERT INTO `patch_info` VALUES (20,'4.8.6.R1-qdata-clean-C00-U00','DML','Blank patch to denote code release',NULL,'2012-09-03 02:00:23','Shiva S','4.8.6.R1','U00','CleanApp','C00','QDATA');
INSERT INTO `patch_info` VALUES (21,'4.8.6.R1-qdata-clean-C01-U01','DDL','modified column type of dependent_mgmt_queue_id to string from bigint','2012-04-17','2012-09-03 02:00:24','Prasanna','4.8.6.R1','U01','CleanApp','C01','QDATA');
INSERT INTO `patch_info` VALUES (22,'4.8.7.R1-qdata-clean-C00-U00','DML','Blank patch to denote code release','2012-06-19','2012-09-03 02:00:24','Raju Gottumukkala','4.8.7.R1','U00','CleanApp','C00','QDATA');
INSERT INTO `patch_info` VALUES (23,'4.8.8.R1-qdata-clean-C00-U00','DML','Blank patch to denote code release','2012-08-23','2012-09-10 16:08:32','Kaliki Aritakula','4.8.8.R1','U00','CleanApp','C00','QDATA');
INSERT INTO `patch_info` VALUES (24,'4.8.8.R1-qdata-clean-C01-U01','DDL','to add anonymous_user colum in user_query','2012-08-24','2012-09-10 16:08:32','Deenu','4.8.8.R1','U01','CleanApp','C01','QDATA');
INSERT INTO `patch_info` VALUES (25,'4.9.0.R1-qdata-clean-C00-U00','DML','Blank patch to denote code release','2012-09-10','2012-10-22 11:17:47','Shiva Sirigineedi','4.9.0.R1','U00','CleanApp','C00','QDATA');
INSERT INTO `patch_info` VALUES (26,'4.9.0.R1-qdata-clean-C01-U01','DDL','Column Name changes in report_comment table for cross vendor support','2012-09-12','2012-10-22 11:17:47','Raju Gottumukkala','4.9.0.R1','U01','CleanApp','C01','QDATA');
INSERT INTO `patch_info` VALUES (27,'4.9.0.R1-qdata-clean-C02-U02','DML','Sequence entry corrections','2012-09-14','2012-10-22 11:17:47','Raju Gottumukkala','4.9.0.R1','U02','CleanApp','C02','QDATA');
INSERT INTO `patch_info` VALUES (28,'4.9.0.R1-qdata-clean-C03-U03','DDL','Correcting column data types according to the enum value types','2012-09-28','2012-10-22 11:17:47','Raju Gottumukkala','4.9.0.R1','U03','CleanApp','C03','QDATA');
INSERT INTO `patch_info` VALUES (30,'4.9.1.R1-qdata-clean-C00-U00','DML','Blank patch to denote code release','2012-10-22','2012-11-03 15:16:41','Raju Gottumukkala','4.9.1.R1','U00','CleanApp','C00','QDATA');
INSERT INTO `patch_info` VALUES (31,'4.9.2.R1-qdata-clean-C00-U00','DML','Blank patch to denote code release','2012-11-05','2012-11-11 20:54:30','Raju Gottumukkala','4.9.2.R1','U00','CleanApp','C00','QDATA');
/*!40000 ALTER TABLE `patch_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qrtz_blob_triggers`
--

DROP TABLE IF EXISTS `qrtz_blob_triggers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qrtz_blob_triggers` (
  `TRIGGER_NAME` varchar(80) NOT NULL,
  `TRIGGER_GROUP` varchar(80) NOT NULL,
  `BLOB_DATA` blob,
  PRIMARY KEY (`TRIGGER_NAME`,`TRIGGER_GROUP`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qrtz_blob_triggers`
--

LOCK TABLES `qrtz_blob_triggers` WRITE;
/*!40000 ALTER TABLE `qrtz_blob_triggers` DISABLE KEYS */;
/*!40000 ALTER TABLE `qrtz_blob_triggers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qrtz_calendars`
--

DROP TABLE IF EXISTS `qrtz_calendars`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qrtz_calendars` (
  `CALENDAR_NAME` varchar(80) NOT NULL,
  `CALENDAR` blob NOT NULL,
  PRIMARY KEY (`CALENDAR_NAME`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qrtz_calendars`
--

LOCK TABLES `qrtz_calendars` WRITE;
/*!40000 ALTER TABLE `qrtz_calendars` DISABLE KEYS */;
/*!40000 ALTER TABLE `qrtz_calendars` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qrtz_cron_triggers`
--

DROP TABLE IF EXISTS `qrtz_cron_triggers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qrtz_cron_triggers` (
  `TRIGGER_NAME` varchar(80) NOT NULL,
  `TRIGGER_GROUP` varchar(80) NOT NULL,
  `CRON_EXPRESSION` varchar(80) NOT NULL,
  `TIME_ZONE_ID` varchar(80) DEFAULT NULL,
  PRIMARY KEY (`TRIGGER_NAME`,`TRIGGER_GROUP`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qrtz_cron_triggers`
--

LOCK TABLES `qrtz_cron_triggers` WRITE;
/*!40000 ALTER TABLE `qrtz_cron_triggers` DISABLE KEYS */;
/*!40000 ALTER TABLE `qrtz_cron_triggers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qrtz_fired_triggers`
--

DROP TABLE IF EXISTS `qrtz_fired_triggers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qrtz_fired_triggers` (
  `ENTRY_ID` varchar(95) NOT NULL,
  `TRIGGER_NAME` varchar(80) NOT NULL,
  `TRIGGER_GROUP` varchar(80) NOT NULL,
  `IS_VOLATILE` varchar(1) NOT NULL,
  `INSTANCE_NAME` varchar(80) NOT NULL,
  `FIRED_TIME` bigint(13) NOT NULL,
  `PRIORITY` int(13) NOT NULL,
  `STATE` varchar(16) NOT NULL,
  `JOB_NAME` varchar(80) DEFAULT NULL,
  `JOB_GROUP` varchar(80) DEFAULT NULL,
  `IS_STATEFUL` varchar(1) DEFAULT NULL,
  `REQUESTS_RECOVERY` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`ENTRY_ID`),
  KEY `idx_qrtz_ft_trig_name` (`TRIGGER_NAME`),
  KEY `idx_qrtz_ft_trig_group` (`TRIGGER_GROUP`),
  KEY `idx_qrtz_ft_trig_nm_gp` (`TRIGGER_NAME`,`TRIGGER_GROUP`),
  KEY `idx_qrtz_ft_trig_volatile` (`IS_VOLATILE`),
  KEY `idx_qrtz_ft_trig_inst_name` (`INSTANCE_NAME`),
  KEY `idx_qrtz_ft_job_name` (`JOB_NAME`),
  KEY `idx_qrtz_ft_job_group` (`JOB_GROUP`),
  KEY `idx_qrtz_ft_job_stateful` (`IS_STATEFUL`),
  KEY `idx_qrtz_ft_job_req_recovery` (`REQUESTS_RECOVERY`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qrtz_fired_triggers`
--

LOCK TABLES `qrtz_fired_triggers` WRITE;
/*!40000 ALTER TABLE `qrtz_fired_triggers` DISABLE KEYS */;
/*!40000 ALTER TABLE `qrtz_fired_triggers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qrtz_job_details`
--

DROP TABLE IF EXISTS `qrtz_job_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qrtz_job_details` (
  `JOB_NAME` varchar(80) NOT NULL,
  `JOB_GROUP` varchar(80) NOT NULL,
  `DESCRIPTION` varchar(120) DEFAULT NULL,
  `JOB_CLASS_NAME` varchar(128) NOT NULL,
  `IS_DURABLE` varchar(1) NOT NULL,
  `IS_VOLATILE` varchar(1) NOT NULL,
  `IS_STATEFUL` varchar(1) NOT NULL,
  `REQUESTS_RECOVERY` varchar(1) NOT NULL,
  `JOB_DATA` blob,
  PRIMARY KEY (`JOB_NAME`,`JOB_GROUP`),
  KEY `idx_rqrtz_job_list_recovery` (`REQUESTS_RECOVERY`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qrtz_job_details`
--

LOCK TABLES `qrtz_job_details` WRITE;
/*!40000 ALTER TABLE `qrtz_job_details` DISABLE KEYS */;
/*!40000 ALTER TABLE `qrtz_job_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qrtz_job_listeners`
--

DROP TABLE IF EXISTS `qrtz_job_listeners`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qrtz_job_listeners` (
  `JOB_NAME` varchar(80) NOT NULL,
  `JOB_GROUP` varchar(80) NOT NULL,
  `JOB_LISTENER` varchar(80) NOT NULL,
  PRIMARY KEY (`JOB_NAME`,`JOB_GROUP`,`JOB_LISTENER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qrtz_job_listeners`
--

LOCK TABLES `qrtz_job_listeners` WRITE;
/*!40000 ALTER TABLE `qrtz_job_listeners` DISABLE KEYS */;
/*!40000 ALTER TABLE `qrtz_job_listeners` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qrtz_locks`
--

DROP TABLE IF EXISTS `qrtz_locks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qrtz_locks` (
  `LOCK_NAME` varchar(40) NOT NULL,
  PRIMARY KEY (`LOCK_NAME`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qrtz_locks`
--

LOCK TABLES `qrtz_locks` WRITE;
/*!40000 ALTER TABLE `qrtz_locks` DISABLE KEYS */;
INSERT INTO `qrtz_locks` VALUES ('CALENDAR_ACCESS');
INSERT INTO `qrtz_locks` VALUES ('JOB_ACCESS');
INSERT INTO `qrtz_locks` VALUES ('MISFIRE_ACCESS');
INSERT INTO `qrtz_locks` VALUES ('STATE_ACCESS');
INSERT INTO `qrtz_locks` VALUES ('TRIGGER_ACCESS');
/*!40000 ALTER TABLE `qrtz_locks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qrtz_paused_trigger_grps`
--

DROP TABLE IF EXISTS `qrtz_paused_trigger_grps`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qrtz_paused_trigger_grps` (
  `TRIGGER_GROUP` varchar(80) NOT NULL,
  PRIMARY KEY (`TRIGGER_GROUP`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qrtz_paused_trigger_grps`
--

LOCK TABLES `qrtz_paused_trigger_grps` WRITE;
/*!40000 ALTER TABLE `qrtz_paused_trigger_grps` DISABLE KEYS */;
/*!40000 ALTER TABLE `qrtz_paused_trigger_grps` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qrtz_scheduler_state`
--

DROP TABLE IF EXISTS `qrtz_scheduler_state`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qrtz_scheduler_state` (
  `INSTANCE_NAME` varchar(80) NOT NULL,
  `LAST_CHECKIN_TIME` bigint(13) NOT NULL,
  `CHECKIN_INTERVAL` int(13) NOT NULL,
  PRIMARY KEY (`INSTANCE_NAME`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qrtz_scheduler_state`
--

LOCK TABLES `qrtz_scheduler_state` WRITE;
/*!40000 ALTER TABLE `qrtz_scheduler_state` DISABLE KEYS */;
/*!40000 ALTER TABLE `qrtz_scheduler_state` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qrtz_simple_triggers`
--

DROP TABLE IF EXISTS `qrtz_simple_triggers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qrtz_simple_triggers` (
  `TRIGGER_NAME` varchar(80) NOT NULL,
  `TRIGGER_GROUP` varchar(80) NOT NULL,
  `REPEAT_COUNT` int(7) NOT NULL,
  `REPEAT_INTERVAL` int(12) NOT NULL,
  `TIMES_TRIGGERED` int(7) NOT NULL,
  PRIMARY KEY (`TRIGGER_NAME`,`TRIGGER_GROUP`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qrtz_simple_triggers`
--

LOCK TABLES `qrtz_simple_triggers` WRITE;
/*!40000 ALTER TABLE `qrtz_simple_triggers` DISABLE KEYS */;
/*!40000 ALTER TABLE `qrtz_simple_triggers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qrtz_trigger_listeners`
--

DROP TABLE IF EXISTS `qrtz_trigger_listeners`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qrtz_trigger_listeners` (
  `TRIGGER_NAME` varchar(80) NOT NULL,
  `TRIGGER_GROUP` varchar(80) NOT NULL,
  `TRIGGER_LISTENER` varchar(80) NOT NULL,
  PRIMARY KEY (`TRIGGER_NAME`,`TRIGGER_GROUP`,`TRIGGER_LISTENER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qrtz_trigger_listeners`
--

LOCK TABLES `qrtz_trigger_listeners` WRITE;
/*!40000 ALTER TABLE `qrtz_trigger_listeners` DISABLE KEYS */;
/*!40000 ALTER TABLE `qrtz_trigger_listeners` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qrtz_triggers`
--

DROP TABLE IF EXISTS `qrtz_triggers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qrtz_triggers` (
  `TRIGGER_NAME` varchar(80) NOT NULL,
  `TRIGGER_GROUP` varchar(80) NOT NULL,
  `JOB_NAME` varchar(80) NOT NULL,
  `JOB_GROUP` varchar(80) NOT NULL,
  `IS_VOLATILE` varchar(1) NOT NULL,
  `DESCRIPTION` varchar(120) DEFAULT NULL,
  `NEXT_FIRE_TIME` bigint(13) DEFAULT NULL,
  `PREV_FIRE_TIME` bigint(13) DEFAULT NULL,
  `PRIORITY` int(13) DEFAULT NULL,
  `TRIGGER_STATE` varchar(16) NOT NULL,
  `TRIGGER_TYPE` varchar(8) NOT NULL,
  `START_TIME` bigint(13) NOT NULL,
  `END_TIME` bigint(13) DEFAULT NULL,
  `CALENDAR_NAME` varchar(80) DEFAULT NULL,
  `MISFIRE_INSTR` int(2) DEFAULT NULL,
  `JOB_DATA` blob,
  PRIMARY KEY (`TRIGGER_NAME`,`TRIGGER_GROUP`),
  KEY `JOB_NAME` (`JOB_NAME`,`JOB_GROUP`),
  KEY `idx_qrtz_t_next_fire_time` (`NEXT_FIRE_TIME`),
  KEY `idx_qrtz_t_state` (`TRIGGER_STATE`),
  KEY `idx_qrtz_t_nft_st` (`NEXT_FIRE_TIME`,`TRIGGER_STATE`),
  KEY `idx_qrtz_t_volatile` (`IS_VOLATILE`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qrtz_triggers`
--

LOCK TABLES `qrtz_triggers` WRITE;
/*!40000 ALTER TABLE `qrtz_triggers` DISABLE KEYS */;
/*!40000 ALTER TABLE `qrtz_triggers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ranking_weights`
--

DROP TABLE IF EXISTS `ranking_weights`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ranking_weights` (
  `ID` bigint(20) NOT NULL,
  `USER_QUERY_VARIATION_SUB_TYPE` int(2) DEFAULT NULL,
  `PAGE_RF_VARIATION_SUB_TYPE` int(2) NOT NULL DEFAULT '0',
  `MATCH_WEIGHT` int(3) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `UQ_VST_IDX` (`USER_QUERY_VARIATION_SUB_TYPE`),
  KEY `PAGE_RF_VST_IDX` (`PAGE_RF_VARIATION_SUB_TYPE`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ranking_weights`
--

LOCK TABLES `ranking_weights` WRITE;
/*!40000 ALTER TABLE `ranking_weights` DISABLE KEYS */;
INSERT INTO `ranking_weights` VALUES (1,1,1,100);
INSERT INTO `ranking_weights` VALUES (3,1,4,70);
INSERT INTO `ranking_weights` VALUES (4,1,6,70);
INSERT INTO `ranking_weights` VALUES (5,1,7,50);
INSERT INTO `ranking_weights` VALUES (6,1,8,60);
INSERT INTO `ranking_weights` VALUES (7,1,14,70);
INSERT INTO `ranking_weights` VALUES (9,2,2,100);
INSERT INTO `ranking_weights` VALUES (10,2,5,70);
INSERT INTO `ranking_weights` VALUES (11,2,6,70);
INSERT INTO `ranking_weights` VALUES (12,2,7,50);
INSERT INTO `ranking_weights` VALUES (13,2,9,60);
INSERT INTO `ranking_weights` VALUES (14,2,13,70);
INSERT INTO `ranking_weights` VALUES (15,3,3,100);
INSERT INTO `ranking_weights` VALUES (16,3,4,70);
INSERT INTO `ranking_weights` VALUES (17,3,5,70);
INSERT INTO `ranking_weights` VALUES (18,3,7,50);
INSERT INTO `ranking_weights` VALUES (19,3,8,50);
INSERT INTO `ranking_weights` VALUES (20,3,9,50);
INSERT INTO `ranking_weights` VALUES (21,3,10,50);
INSERT INTO `ranking_weights` VALUES (22,3,11,70);
INSERT INTO `ranking_weights` VALUES (23,3,12,70);
INSERT INTO `ranking_weights` VALUES (24,4,4,100);
INSERT INTO `ranking_weights` VALUES (25,4,7,80);
INSERT INTO `ranking_weights` VALUES (26,4,8,80);
INSERT INTO `ranking_weights` VALUES (27,5,4,100);
INSERT INTO `ranking_weights` VALUES (28,5,7,80);
INSERT INTO `ranking_weights` VALUES (29,5,9,80);
INSERT INTO `ranking_weights` VALUES (30,6,6,100);
INSERT INTO `ranking_weights` VALUES (31,6,7,80);
INSERT INTO `ranking_weights` VALUES (32,7,7,100);
INSERT INTO `ranking_weights` VALUES (33,8,7,90);
INSERT INTO `ranking_weights` VALUES (34,8,8,100);
INSERT INTO `ranking_weights` VALUES (35,9,7,90);
INSERT INTO `ranking_weights` VALUES (36,9,9,100);
INSERT INTO `ranking_weights` VALUES (37,10,7,50);
INSERT INTO `ranking_weights` VALUES (38,10,8,70);
INSERT INTO `ranking_weights` VALUES (39,10,9,70);
INSERT INTO `ranking_weights` VALUES (40,10,10,100);
INSERT INTO `ranking_weights` VALUES (41,11,4,90);
INSERT INTO `ranking_weights` VALUES (42,11,7,65);
INSERT INTO `ranking_weights` VALUES (43,11,8,65);
INSERT INTO `ranking_weights` VALUES (44,11,9,85);
INSERT INTO `ranking_weights` VALUES (45,11,10,90);
INSERT INTO `ranking_weights` VALUES (46,11,11,100);
INSERT INTO `ranking_weights` VALUES (47,12,5,90);
INSERT INTO `ranking_weights` VALUES (48,12,7,65);
INSERT INTO `ranking_weights` VALUES (49,12,8,85);
INSERT INTO `ranking_weights` VALUES (50,12,9,65);
INSERT INTO `ranking_weights` VALUES (51,12,10,90);
INSERT INTO `ranking_weights` VALUES (52,12,12,100);
INSERT INTO `ranking_weights` VALUES (53,13,6,90);
INSERT INTO `ranking_weights` VALUES (54,13,7,70);
INSERT INTO `ranking_weights` VALUES (55,13,9,90);
INSERT INTO `ranking_weights` VALUES (56,13,13,100);
INSERT INTO `ranking_weights` VALUES (57,14,6,90);
INSERT INTO `ranking_weights` VALUES (58,14,7,70);
INSERT INTO `ranking_weights` VALUES (59,14,8,90);
INSERT INTO `ranking_weights` VALUES (60,14,14,100);
INSERT INTO `ranking_weights` VALUES (61,15,6,80);
INSERT INTO `ranking_weights` VALUES (62,15,7,60);
INSERT INTO `ranking_weights` VALUES (63,15,8,65);
INSERT INTO `ranking_weights` VALUES (64,15,9,65);
INSERT INTO `ranking_weights` VALUES (65,15,10,90);
INSERT INTO `ranking_weights` VALUES (66,15,13,90);
INSERT INTO `ranking_weights` VALUES (67,15,14,90);
INSERT INTO `ranking_weights` VALUES (68,15,15,100);
INSERT INTO `ranking_weights` VALUES (69,16,1,60);
INSERT INTO `ranking_weights` VALUES (70,16,4,60);
INSERT INTO `ranking_weights` VALUES (71,16,6,60);
INSERT INTO `ranking_weights` VALUES (72,16,7,40);
INSERT INTO `ranking_weights` VALUES (73,16,8,50);
INSERT INTO `ranking_weights` VALUES (74,16,9,60);
INSERT INTO `ranking_weights` VALUES (75,16,10,60);
INSERT INTO `ranking_weights` VALUES (76,16,11,70);
INSERT INTO `ranking_weights` VALUES (77,16,13,70);
INSERT INTO `ranking_weights` VALUES (78,16,14,60);
INSERT INTO `ranking_weights` VALUES (79,16,15,70);
INSERT INTO `ranking_weights` VALUES (80,16,16,100);
INSERT INTO `ranking_weights` VALUES (81,17,2,60);
INSERT INTO `ranking_weights` VALUES (82,17,5,60);
INSERT INTO `ranking_weights` VALUES (83,17,6,60);
INSERT INTO `ranking_weights` VALUES (84,17,7,40);
INSERT INTO `ranking_weights` VALUES (85,17,8,60);
INSERT INTO `ranking_weights` VALUES (86,17,9,50);
INSERT INTO `ranking_weights` VALUES (87,17,10,60);
INSERT INTO `ranking_weights` VALUES (88,17,12,70);
INSERT INTO `ranking_weights` VALUES (89,17,13,60);
INSERT INTO `ranking_weights` VALUES (90,17,14,70);
INSERT INTO `ranking_weights` VALUES (91,17,15,700);
INSERT INTO `ranking_weights` VALUES (92,17,17,100);
/*!40000 ALTER TABLE `ranking_weights` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reduced_query`
--

DROP TABLE IF EXISTS `reduced_query`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `reduced_query` (
  `ID` bigint(20) NOT NULL,
  `USER_QUERY_ID` bigint(20) DEFAULT NULL,
  `APPLICATION_ID` bigint(20) DEFAULT NULL,
  `REDUCED_QUERY_STRING` mediumtext,
  `ENTITY_COUNT` int(10) DEFAULT NULL,
  `MAX_MATCH_WEIGHT` decimal(10,2) DEFAULT NULL,
  `REDUCED_QUERY_WEIGHT` decimal(10,2) DEFAULT NULL,
  `BASE_USER_QUERY_WEIGHT` decimal(10,2) DEFAULT NULL,
  `REDUCED_QUERY_MATCH_PERCENT` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_RQ_UQID` (`USER_QUERY_ID`),
  KEY `REDUCED_QUERY_APP_ID_IDX` (`APPLICATION_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reduced_query`
--

LOCK TABLES `reduced_query` WRITE;
/*!40000 ALTER TABLE `reduced_query` DISABLE KEYS */;
/*!40000 ALTER TABLE `reduced_query` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `report_comment`
--

DROP TABLE IF EXISTS `report_comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `report_comment` (
  `ID` bigint(20) NOT NULL,
  `ASSET_ID` bigint(20) DEFAULT NULL,
  `QUERY_ID` bigint(20) DEFAULT NULL,
  `USER_ID` bigint(20) DEFAULT NULL,
  `USER_NAME` varchar(35) DEFAULT NULL,
  `QUERY_HASH` varchar(255) DEFAULT NULL,
  `COMMENTS` mediumtext,
  `CREATED_DATE` datetime DEFAULT NULL,
  `PUBLIC_TYPE` char(1) DEFAULT 'Y',
  `ABUSE_REPORT` char(1) DEFAULT 'N',
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `report_comment`
--

LOCK TABLES `report_comment` WRITE;
/*!40000 ALTER TABLE `report_comment` DISABLE KEYS */;
/*!40000 ALTER TABLE `report_comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `report_data`
--

DROP TABLE IF EXISTS `report_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `report_data` (
  `ID` bigint(20) NOT NULL,
  `AGGREGATED_QUERY_ID` bigint(20) DEFAULT NULL,
  `payload` longtext,
  PRIMARY KEY (`ID`),
  KEY `FK_RD_AQID` (`AGGREGATED_QUERY_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `report_data`
--

LOCK TABLES `report_data` WRITE;
/*!40000 ALTER TABLE `report_data` DISABLE KEYS */;
/*!40000 ALTER TABLE `report_data` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rfx`
--

DROP TABLE IF EXISTS `rfx`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rfx` (
  `ID` bigint(20) NOT NULL,
  `REDUCED_FORM_ID` bigint(20) NOT NULL,
  `RFX_ENTITY_TYPE` varchar(35) NOT NULL,
  `RFX_VARIATION_SUB_TYPE` int(2) DEFAULT NULL,
  `RFX_ORDER` int(9) DEFAULT NULL,
  `SRC_INSTANCE_BE_ID` bigint(20) DEFAULT NULL,
  `SRC_CONCEPT_BE_ID` bigint(20) DEFAULT NULL,
  `RELATION_BE_ID` bigint(20) DEFAULT NULL,
  `DEST_INSTANCE_BE_ID` bigint(20) DEFAULT NULL,
  `DEST_CONCEPT_BE_ID` bigint(20) DEFAULT NULL,
  `VALUE` varchar(255) DEFAULT NULL,
  `APPLICATION_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `RF_ID_IDX` (`REDUCED_FORM_ID`),
  KEY `RFX_APP_ID_IDX` (`APPLICATION_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rfx`
--

LOCK TABLES `rfx` WRITE;
/*!40000 ALTER TABLE `rfx` DISABLE KEYS */;
/*!40000 ALTER TABLE `rfx` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rfx_value`
--

DROP TABLE IF EXISTS `rfx_value`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rfx_value` (
  `ID` bigint(20) NOT NULL,
  `REDUCED_FORM_ID` bigint(20) NOT NULL,
  `SRC_CONCEPT_BE_ID` bigint(20) NOT NULL,
  `RELATION_BE_ID` bigint(20) NOT NULL,
  `DEST_CONCEPT_BE_ID` bigint(20) NOT NULL,
  `OPERATOR` varchar(15) NOT NULL,
  `VALUE` decimal(20,2) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `RFX_VALUE_REDUCED_FORM_ID_IDX` (`REDUCED_FORM_ID`),
  KEY `RFX_VALUE_RELATION_BE_ID_IDX` (`RELATION_BE_ID`),
  KEY `RFX_VALUE_OPERATOR_IDX` (`OPERATOR`),
  KEY `RFX_VALUE_VALUE_IDX` (`VALUE`),
  KEY `IDX_RFX_VAL_SRC_CONCEPTBEID` (`SRC_CONCEPT_BE_ID`),
  KEY `IDX_RFX_VAL_DEST_CONCEPTBEID` (`DEST_CONCEPT_BE_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rfx_value`
--

LOCK TABLES `rfx_value` WRITE;
/*!40000 ALTER TABLE `rfx_value` DISABLE KEYS */;
/*!40000 ALTER TABLE `rfx_value` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ri_instance_triple_definition`
--

DROP TABLE IF EXISTS `ri_instance_triple_definition`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ri_instance_triple_definition` (
  `ID` bigint(20) NOT NULL,
  `BE_ID_1` bigint(20) DEFAULT NULL,
  `BE_ID_2` bigint(20) DEFAULT NULL,
  `BE_ID_3` bigint(20) DEFAULT NULL,
  `BE_ID_1_NAME` varchar(255) DEFAULT NULL,
  `BE_ID_2_NAME` varchar(255) DEFAULT NULL,
  `BE_ID_3_NAME` varchar(255) DEFAULT NULL,
  `BE_TYPE` int(2) DEFAULT '0',
  `VARIATION_TYPE` int(1) DEFAULT '0',
  `VARIATION_SUB_TYPE` int(2) DEFAULT '0',
  `MATCH_WEIGHT` decimal(5,2) DEFAULT '0.00',
  `INSTANCE_TRIPLE_ID` bigint(20) DEFAULT NULL,
  `SOURCE_BE_ID` bigint(20) DEFAULT NULL,
  `RELATION_BE_ID` bigint(20) DEFAULT NULL,
  `DESTINATION_BE_ID` bigint(20) DEFAULT NULL,
  `SEARCH_TYPE` int(1) DEFAULT NULL,
  `CONTENT_SOURCE` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `RI_ITD_BE_ID_1_IDX` (`BE_ID_1`),
  KEY `RI_ITD_BE_ID_2_IDX` (`BE_ID_2`),
  KEY `RI_ITD_BE_ID_3_IDX` (`BE_ID_3`),
  KEY `RI_ITD_VARIATION_TYPE_IDX` (`VARIATION_TYPE`),
  KEY `RI_ITD_VARIATION_SUB_TYPE_IDX` (`VARIATION_SUB_TYPE`),
  KEY `RI_ITD_BE_TYPE_IDX` (`BE_TYPE`),
  KEY `RI_ITD_INSTANCE_TRIPLE_ID_IDX` (`INSTANCE_TRIPLE_ID`)
) ENGINE=MyISAM AUTO_INCREMENT=4336 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ri_instance_triple_definition`
--

LOCK TABLES `ri_instance_triple_definition` WRITE;
/*!40000 ALTER TABLE `ri_instance_triple_definition` DISABLE KEYS */;
/*!40000 ALTER TABLE `ri_instance_triple_definition` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ri_query_cache`
--

DROP TABLE IF EXISTS `ri_query_cache`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ri_query_cache` (
  `ID` bigint(20) NOT NULL,
  `USER_QUERY_ID` bigint(20) DEFAULT NULL,
  `BE_ID_1` bigint(20) DEFAULT NULL,
  `BE_ID_2` bigint(20) DEFAULT NULL,
  `BE_ID_3` bigint(20) DEFAULT NULL,
  `VARIATION_TYPE` int(1) DEFAULT NULL,
  `VARIATION_SUBTYPE` int(2) DEFAULT NULL,
  `ORIGINAL_SUBTYPE` int(2) DEFAULT NULL,
  `DERIVED` int(1) DEFAULT NULL,
  `VARIATION_WEIGHT` decimal(5,2) DEFAULT NULL,
  `MAX_WEIGHT` decimal(5,2) DEFAULT NULL,
  `REC_WEIGHT` decimal(5,2) DEFAULT NULL,
  `ENTITY_COUNT` int(10) DEFAULT NULL,
  `SEARCH_TYPE` int(1) DEFAULT NULL,
  `APPLICATION_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `RI_QC_BE_ID_1_IDX` (`BE_ID_1`),
  KEY `RI_QC_BE_ID_2_IDX` (`BE_ID_2`),
  KEY `RI_QC_BE_ID_3_IDX` (`BE_ID_3`),
  KEY `RI_QC_VARIATION_TYPE_IDX` (`VARIATION_TYPE`),
  KEY `RI_QC_VARIATION_SUB_TYPE_IDX` (`VARIATION_SUBTYPE`),
  KEY `RI_QC_APPLICATION_ID_IDX` (`APPLICATION_ID`),
  KEY `idx_ri_query_cache_derived` (`DERIVED`),
  KEY `idx_ri_qc_reduced_form_id` (`USER_QUERY_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ri_query_cache`
--

LOCK TABLES `ri_query_cache` WRITE;
/*!40000 ALTER TABLE `ri_query_cache` DISABLE KEYS */;
/*!40000 ALTER TABLE `ri_query_cache` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ri_udx`
--

DROP TABLE IF EXISTS `ri_udx`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ri_udx` (
  `ID` bigint(20) NOT NULL,
  `UDX_ID` bigint(20) NOT NULL,
  `BE_ID_1` bigint(20) DEFAULT NULL,
  `BE_ID_2` bigint(20) DEFAULT NULL,
  `BE_ID_3` bigint(20) DEFAULT NULL,
  `VARIATION_TYPE` int(1) DEFAULT NULL,
  `VARIATION_SUBTYPE` int(2) DEFAULT NULL,
  `ORIGINAL_SUBTYPE` int(2) DEFAULT NULL,
  `DERIVED` int(1) DEFAULT NULL,
  `VARIATION_WEIGHT` decimal(5,2) DEFAULT NULL,
  `MAX_WEIGHT` decimal(5,2) DEFAULT NULL,
  `REC_WEIGHT` decimal(5,2) DEFAULT NULL,
  `ENTITY_COUNT` int(10) DEFAULT NULL,
  `SEARCH_TYPE` int(1) DEFAULT NULL,
  `APPLICATION_ID` bigint(20) DEFAULT NULL,
  `RF_ID` bigint(20) DEFAULT NULL,
  `CONTENT_DATE` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `idx_ri_udx_be_id1` (`BE_ID_1`),
  KEY `idx_ri_udx_be_id2` (`BE_ID_2`),
  KEY `idx_ri_udx_be_id3` (`BE_ID_3`),
  KEY `idx_ri_udx_variation_type` (`VARIATION_TYPE`),
  KEY `idx_ri_udx_variation_sub_type` (`VARIATION_SUBTYPE`),
  KEY `RI_UDX_APP_ID_IDX` (`APPLICATION_ID`),
  KEY `idx_ri_udx_derived` (`DERIVED`),
  KEY `idx_ri_udx_udx_id` (`UDX_ID`),
  KEY `idx_ri_udx_RF_ID` (`RF_ID`),
  KEY `idx_ri_udx_CONTENT_DATE` (`CONTENT_DATE`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ri_udx`
--

LOCK TABLES `ri_udx` WRITE;
/*!40000 ALTER TABLE `ri_udx` DISABLE KEYS */;
/*!40000 ALTER TABLE `ri_udx` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ri_user_query`
--

DROP TABLE IF EXISTS `ri_user_query`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ri_user_query` (
  `ID` bigint(20) NOT NULL,
  `QUERY_ID` bigint(20) DEFAULT NULL,
  `BE_ID_1` bigint(20) DEFAULT NULL,
  `BE_ID_2` bigint(20) DEFAULT NULL,
  `BE_ID_3` bigint(20) DEFAULT NULL,
  `REC_WEIGHT` decimal(5,2) DEFAULT NULL,
  `VARIATION_WEIGHT` decimal(5,2) DEFAULT NULL,
  `VARIATION_TYPE` int(1) DEFAULT NULL,
  `VARIATION_SUBTYPE` int(2) DEFAULT NULL,
  `ORIGINAL_SUBTYPE` int(2) DEFAULT NULL,
  `DERIVED` int(1) DEFAULT NULL,
  `ENTITY_COUNT` int(10) DEFAULT NULL,
  `APPLICATION_ID` bigint(20) DEFAULT NULL,
  `EXECUTION_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `TRIPLE_IDENTIFIER` int(2) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `Query_id_idx` (`QUERY_ID`),
  KEY `idx_ri_user_query_be_id1` (`BE_ID_1`),
  KEY `idx_ri_user_query_be_id2` (`BE_ID_2`),
  KEY `idx_ri_user_query_be_id3` (`BE_ID_3`),
  KEY `idx_ri_uq_variation_type` (`VARIATION_TYPE`),
  KEY `idx_ri_uq_variat_sub_type` (`VARIATION_SUBTYPE`),
  KEY `idx_ri_uq_app_id` (`APPLICATION_ID`),
  KEY `idx_ri_uq_exec_date` (`EXECUTION_DATE`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ri_user_query`
--

LOCK TABLES `ri_user_query` WRITE;
/*!40000 ALTER TABLE `ri_user_query` DISABLE KEYS */;
/*!40000 ALTER TABLE `ri_user_query` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `seed`
--

DROP TABLE IF EXISTS `seed`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `seed` (
  `ID` int(10) NOT NULL DEFAULT '0',
  `node_id` int(10) DEFAULT NULL,
  `type` varchar(20) DEFAULT 'TRANSACTION',
  `next_value` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `idx_seed_nodeID_type` (`node_id`,`type`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `seed`
--

LOCK TABLES `seed` WRITE;
/*!40000 ALTER TABLE `seed` DISABLE KEYS */;
INSERT INTO `seed` VALUES (1,1,'TRANSACTION',1);
INSERT INTO `seed` VALUES (2,2,'TRANSACTION',1);
INSERT INTO `seed` VALUES (3,1,'RF',1);
INSERT INTO `seed` VALUES (4,2,'RF',1);
/*!40000 ALTER TABLE `seed` ENABLE KEYS */;
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
INSERT INTO `sequences` VALUES ('USER_QUERY',1);
INSERT INTO `sequences` VALUES ('REDUCED_QUERY',1);
INSERT INTO `sequences` VALUES ('BUSINESS_QUERY',1);
INSERT INTO `sequences` VALUES ('AGGREGATED_QUERY',1);
INSERT INTO `sequences` VALUES ('USER_QUERY_COLUMN',1);
INSERT INTO `sequences` VALUES ('BUSINESS_QUERY_COLUMN',1);
INSERT INTO `sequences` VALUES ('AGGREGATED_QUERY_COLUMN',1);
INSERT INTO `sequences` VALUES ('REPORT_DATA',1);
INSERT INTO `sequences` VALUES ('AGGREGATED_REPORT_TYPE',1);
INSERT INTO `sequences` VALUES ('JOB_REQUEST',1);
INSERT INTO `sequences` VALUES ('OPERATIONAL_STATUS',1);
INSERT INTO `sequences` VALUES ('H_OPERATIONAL_STATUS',1);
INSERT INTO `sequences` VALUES ('AC_CONTEXT',1);
INSERT INTO `sequences` VALUES ('RFX',11576);
INSERT INTO `sequences` VALUES ('USER_QUERY_RFX',101);
INSERT INTO `sequences` VALUES ('UDX',1759);
INSERT INTO `sequences` VALUES ('REPORT_COMMENT',1000);
INSERT INTO `sequences` VALUES ('NOTIFICATION',1000);
INSERT INTO `sequences` VALUES ('NOTIFICATION_DETAIL',1000);
INSERT INTO `sequences` VALUES ('NOTIFICATION_TEMPLATE',1072);
INSERT INTO `sequences` VALUES ('MESSAGE',101);
INSERT INTO `sequences` VALUES ('MESSAGE_HISTORY',101);
INSERT INTO `sequences` VALUES ('NEWS_ITEM',3719);
INSERT INTO `sequences` VALUES ('RANKING_WEIGHTS',93);
INSERT INTO `sequences` VALUES ('RI_QUERY_CACHE',1);
INSERT INTO `sequences` VALUES ('APP_NEWS_POPULARITY',1001);
INSERT INTO `sequences` VALUES ('RFX_VALUE',1001);
INSERT INTO `sequences` VALUES ('UDX_ATTRIBUTE',1);
INSERT INTO `sequences` VALUES ('UDX_KEY_WORD',1001);
INSERT INTO `sequences` VALUES ('CACHED_REPORT_RESULTS',1);
INSERT INTO `sequences` VALUES ('APPLICATION_CATEGORY_MAPPING',1003);
INSERT INTO `sequences` VALUES ('AC_MGMT_QUEUE',1);
INSERT INTO `sequences` VALUES ('OPTIMAL_DSET_SWI_INFO',1);
INSERT INTO `sequences` VALUES ('RI_INSTANCE_TRIPLE_DEFINITION',1);
INSERT INTO `sequences` VALUES ('RI_UDX',1);
INSERT INTO `sequences` VALUES ('UDX_KEY_WORD_MATCH',1);
INSERT INTO `sequences` VALUES ('UNIVERSAL_SEARCH_RESULT',1);
INSERT INTO `sequences` VALUES ('USER_QUERY_RFX_VALUE',1);
/*!40000 ALTER TABLE `sequences` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `udx`
--

DROP TABLE IF EXISTS `udx`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `udx` (
  `ID` bigint(20) NOT NULL,
  `ARTICLE_REF_ID` bigint(20) DEFAULT NULL,
  `RF_ID` bigint(20) NOT NULL,
  `URL` text,
  `IMAGE_URL` text,
  `SHORT_DESC` text,
  `LONG_DESC` text,
  `ENTITY_COUNT` int(10) DEFAULT NULL,
  `MAX_MATCH_WEIGHT` decimal(10,2) DEFAULT NULL,
  `SOURCE` varchar(255) DEFAULT NULL,
  `CONTENT_SOURCE_TYPE` varchar(255) DEFAULT NULL,
  `CONTENT_DATE` datetime DEFAULT NULL,
  `CREATED_DATE` datetime DEFAULT NULL,
  `IMAGE_URL_PROCESSED` varchar(2) DEFAULT 'N',
  `BATCH_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `RF_ID_IDX` (`RF_ID`),
  KEY `UDX_CONTENT_DATE_IDX` (`CONTENT_DATE`),
  KEY `UDX_IMAGE_URL_PROCESSED_IDX` (`IMAGE_URL_PROCESSED`),
  KEY `UDX_ARTICLE_REF_ID_IDX` (`ARTICLE_REF_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `udx`
--

LOCK TABLES `udx` WRITE;
/*!40000 ALTER TABLE `udx` DISABLE KEYS */;
/*!40000 ALTER TABLE `udx` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `udx_attribute`
--

DROP TABLE IF EXISTS `udx_attribute`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `udx_attribute` (
  `ID` bigint(20) NOT NULL,
  `UDX_ID` bigint(20) NOT NULL,
  `ATTRIBUTE_TYPE` varchar(255) NOT NULL,
  `ATTRIBUTE_VALUE` varchar(500) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `UDX_ATTRIBUTE_UDX_ID_IDX` (`UDX_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `udx_attribute`
--

LOCK TABLES `udx_attribute` WRITE;
/*!40000 ALTER TABLE `udx_attribute` DISABLE KEYS */;
/*!40000 ALTER TABLE `udx_attribute` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `udx_key_word`
--

DROP TABLE IF EXISTS `udx_key_word`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `udx_key_word` (
  `id` int(20) NOT NULL,
  `udx_id` int(20) NOT NULL,
  `key_word_text` text,
  `content_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `udx_key_word_udx_id_idx` (`udx_id`),
  KEY `idx_udx_kw_content_date` (`content_date`),
  FULLTEXT KEY `key_word_text` (`key_word_text`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `udx_key_word`
--

LOCK TABLES `udx_key_word` WRITE;
/*!40000 ALTER TABLE `udx_key_word` DISABLE KEYS */;
/*!40000 ALTER TABLE `udx_key_word` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `udx_key_word_match`
--

DROP TABLE IF EXISTS `udx_key_word_match`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `udx_key_word_match` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `query_id` bigint(20) NOT NULL,
  `udx_id` bigint(20) NOT NULL,
  `match_score` decimal(5,2) NOT NULL,
  `execution_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`),
  KEY `udx_key_word_match_udx_id_idx` (`udx_id`),
  KEY `idx_udx_kw_match_query` (`query_id`),
  KEY `idx_udx_kw_exec_date` (`execution_date`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `udx_key_word_match`
--

LOCK TABLES `udx_key_word_match` WRITE;
/*!40000 ALTER TABLE `udx_key_word_match` DISABLE KEYS */;
/*!40000 ALTER TABLE `udx_key_word_match` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `universal_search_result`
--

DROP TABLE IF EXISTS `universal_search_result`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `universal_search_result` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `UDX_ID` bigint(20) NOT NULL,
  `RF_ID` bigint(20) DEFAULT NULL,
  `QUERY_ID` bigint(20) NOT NULL,
  `Match_Weight` decimal(10,2) DEFAULT NULL,
  `ENTITY_COUNT` int(10) DEFAULT NULL,
  `SEARCH_TYPE` int(1) DEFAULT NULL,
  `APPLICATION_ID` bigint(20) DEFAULT NULL,
  `MATCHED_TRIPLES` varchar(255) DEFAULT NULL,
  `MATCHED_TRIPLES_SUM` int(10) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `IDX_USRESULT_UDX_ID` (`UDX_ID`),
  KEY `IDX_USR_QUERY_ID` (`QUERY_ID`),
  KEY `IDX_USR_RF_ID` (`RF_ID`),
  KEY `IDX_USR_APP_ID` (`APPLICATION_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `universal_search_result`
--

LOCK TABLES `universal_search_result` WRITE;
/*!40000 ALTER TABLE `universal_search_result` DISABLE KEYS */;
/*!40000 ALTER TABLE `universal_search_result` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_query`
--

DROP TABLE IF EXISTS `user_query`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_query` (
  `ID` bigint(20) NOT NULL,
  `EXECUTION_DATE` datetime DEFAULT NULL,
  `TYPE` varchar(10) DEFAULT NULL,
  `QUERY_STRING` mediumtext,
  `ENTITY_COUNT` int(10) DEFAULT NULL,
  `MAX_MATCH_WEIGHT` decimal(10,2) DEFAULT NULL,
  `REQUEST_SUCCESSFUL` char(1) DEFAULT 'N',
  `SCOPE` int(1) DEFAULT NULL,
  `USER_ID` bigint(20) DEFAULT NULL,
  `ANONYMOUS_USER` char(1) NOT NULL DEFAULT 'N',
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_query`
--

LOCK TABLES `user_query` WRITE;
/*!40000 ALTER TABLE `user_query` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_query` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_query_column`
--

DROP TABLE IF EXISTS `user_query_column`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_query_column` (
  `ID` bigint(20) NOT NULL,
  `USER_QUERY_ID` bigint(20) DEFAULT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `STAT` varchar(35) DEFAULT NULL,
  `QUERY_SECTION` varchar(35) DEFAULT NULL,
  `OPERATR` varchar(35) DEFAULT NULL,
  `VALU` varchar(255) DEFAULT NULL,
  `BUSINESS_ENTITY_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_UQC_UQID` (`USER_QUERY_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_query_column`
--

LOCK TABLES `user_query_column` WRITE;
/*!40000 ALTER TABLE `user_query_column` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_query_column` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_query_rfx`
--

DROP TABLE IF EXISTS `user_query_rfx`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_query_rfx` (
  `ID` bigint(20) NOT NULL,
  `REDUCED_FORM_ID` bigint(20) NOT NULL,
  `RFX_ENTITY_TYPE` varchar(35) NOT NULL,
  `RFX_VARIATION_SUB_TYPE` int(2) DEFAULT NULL,
  `RFX_ORDER` int(9) DEFAULT NULL,
  `SRC_INSTANCE_BE_ID` bigint(20) DEFAULT NULL,
  `SRC_CONCEPT_BE_ID` bigint(20) DEFAULT NULL,
  `RELATION_BE_ID` bigint(20) DEFAULT NULL,
  `DEST_INSTANCE_BE_ID` bigint(20) DEFAULT NULL,
  `DEST_CONCEPT_BE_ID` bigint(20) DEFAULT NULL,
  `VALUE` varchar(255) DEFAULT NULL,
  `RFX_TYPE` int(2) DEFAULT '1',
  `APPLICATION_ID` bigint(20) DEFAULT NULL,
  `EXECUTION_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_query_rfx`
--

LOCK TABLES `user_query_rfx` WRITE;
/*!40000 ALTER TABLE `user_query_rfx` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_query_rfx` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_query_rfx_value`
--

DROP TABLE IF EXISTS `user_query_rfx_value`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_query_rfx_value` (
  `ID` bigint(20) NOT NULL,
  `QUERY_ID` bigint(20) NOT NULL,
  `SRC_CONCEPT_BE_ID` bigint(20) NOT NULL,
  `RELATION_BE_ID` bigint(20) NOT NULL,
  `DEST_CONCEPT_BE_ID` bigint(20) NOT NULL,
  `OPERATOR` varchar(15) NOT NULL,
  `VALUE` decimal(20,2) DEFAULT NULL,
  `START_VALUE` decimal(30,2) DEFAULT NULL,
  `END_VALUE` decimal(30,2) DEFAULT NULL,
  `TRIPLE_IDENTIFIER` int(2) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `USER_QUERY_RFX_VALUE_VALUE_IDX` (`VALUE`),
  KEY `IDX_UQ_RFX_VALUE_QID` (`QUERY_ID`),
  KEY `IDX_UQ_RFXV_SRC_CONBEID` (`SRC_CONCEPT_BE_ID`),
  KEY `IDX_UQ_RFXV_RELBEID` (`RELATION_BE_ID`),
  KEY `IDX_UQ_RFXV_DEST_CONBEID` (`DEST_CONCEPT_BE_ID`),
  KEY `IDX_UQ_RFXV_OPERATOR` (`OPERATOR`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_query_rfx_value`
--

LOCK TABLES `user_query_rfx_value` WRITE;
/*!40000 ALTER TABLE `user_query_rfx_value` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_query_rfx_value` ENABLE KEYS */;
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
