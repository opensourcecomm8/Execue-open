--
-- Host: localhost    Database: swi-base
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
-- Table structure for table `acl_class`
--

DROP TABLE IF EXISTS `acl_class`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `acl_class` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CLASS` varchar(100) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_AC_CLASS` (`CLASS`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `acl_class`
--

LOCK TABLES `acl_class` WRITE;
/*!40000 ALTER TABLE `acl_class` DISABLE KEYS */;
INSERT INTO `acl_class` VALUES (2,'com.execue.core.common.bean.entity.Tabl');
INSERT INTO `acl_class` VALUES (3,'com.execue.core.common.bean.entity.Colum');
INSERT INTO `acl_class` VALUES (4,'com.execue.core.common.bean.entity.Membr');
INSERT INTO `acl_class` VALUES (1,'com.execue.core.common.bean.entity.Asset');
/*!40000 ALTER TABLE `acl_class` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `acl_entry`
--

DROP TABLE IF EXISTS `acl_entry`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `acl_entry` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ACL_OBJECT_IDENTITY` bigint(20) NOT NULL,
  `ACE_ORDER` int(5) NOT NULL,
  `SID` bigint(20) NOT NULL,
  `MASK` int(5) NOT NULL,
  `GRANTING` int(1) NOT NULL,
  `AUDIT_SUCCESS` int(1) NOT NULL,
  `AUDIT_FAILURE` int(1) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_AE_ID` (`ACL_OBJECT_IDENTITY`,`ACE_ORDER`),
  KEY `FK_AE_SID` (`SID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `acl_entry`
--

LOCK TABLES `acl_entry` WRITE;
/*!40000 ALTER TABLE `acl_entry` DISABLE KEYS */;
/*!40000 ALTER TABLE `acl_entry` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `acl_object_identity`
--

DROP TABLE IF EXISTS `acl_object_identity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `acl_object_identity` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `OBJECT_ID_CLASS` bigint(20) NOT NULL,
  `OBJECT_ID_IDENTITY` bigint(20) NOT NULL,
  `PARENT_OBJECT` bigint(20) DEFAULT NULL,
  `OWNER_SID` bigint(20) DEFAULT NULL,
  `ENTRIES_INHERITING` int(1) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_AOI_ID` (`OBJECT_ID_CLASS`,`OBJECT_ID_IDENTITY`),
  KEY `FK_AOI_PO` (`PARENT_OBJECT`),
  KEY `FK_AOI_OS` (`OWNER_SID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `acl_object_identity`
--

LOCK TABLES `acl_object_identity` WRITE;
/*!40000 ALTER TABLE `acl_object_identity` DISABLE KEYS */;
/*!40000 ALTER TABLE `acl_object_identity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `acl_sid`
--

DROP TABLE IF EXISTS `acl_sid`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `acl_sid` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `PRINCIPAL` bigint(20) NOT NULL,
  `SID` varchar(100) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_AS_ID` (`PRINCIPAL`,`SID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `acl_sid`
--

LOCK TABLES `acl_sid` WRITE;
/*!40000 ALTER TABLE `acl_sid` DISABLE KEYS */;
/*!40000 ALTER TABLE `acl_sid` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `app_data_source`
--

DROP TABLE IF EXISTS `app_data_source`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `app_data_source` (
  `id` bigint(20) NOT NULL,
  `app_id` bigint(20) NOT NULL,
  `data_source_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `app_data_source`
--

LOCK TABLES `app_data_source` WRITE;
/*!40000 ALTER TABLE `app_data_source` DISABLE KEYS */;
/*!40000 ALTER TABLE `app_data_source` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `app_eas_detail_info`
--

DROP TABLE IF EXISTS `app_eas_detail_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `app_eas_detail_info` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `app_id` int(20) NOT NULL,
  `last_refresh_date` date NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_app_ei_info_app_id` (`app_id`),
  KEY `idx_aei_last_rfrsh_date` (`last_refresh_date`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `app_eas_detail_info`
--

LOCK TABLES `app_eas_detail_info` WRITE;
/*!40000 ALTER TABLE `app_eas_detail_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `app_eas_detail_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `application`
--

DROP TABLE IF EXISTS `application`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `application` (
  `ID` bigint(20) NOT NULL,
  `NAME` varchar(255) NOT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `USER_ID` bigint(20) NOT NULL,
  `CREATED_DATE` datetime DEFAULT NULL,
  `MODIFIED_DATE` datetime DEFAULT NULL,
  `application_url` text,
  `POPULARITY` bigint(20) NOT NULL,
  `STATUS` char(1) DEFAULT 'A',
  `PUBLISH_MODE` int(1) DEFAULT NULL,
  `image_id` int(11) DEFAULT '-1',
  `rank` bigint(20) DEFAULT NULL,
  `CONSTANT_RANDOM_FACTOR` decimal(5,2) DEFAULT '0.00',
  `source_type` char(1) DEFAULT 'S',
  `create_type` char(1) DEFAULT 'N',
  `ASSOCIATION_EXIST` char(1) DEFAULT 'N',
  `TAG` text,
  `APPLICATION_TITLE` varchar(255) DEFAULT NULL,
  `APPLICATION_KEY` varchar(255) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_UI_USERS` (`USER_ID`),
  KEY `IDX_A_ST` (`STATUS`),
  KEY `IDX_A_PM` (`PUBLISH_MODE`),
  KEY `IDX_A_NM` (`NAME`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `application`
--

LOCK TABLES `application` WRITE;
/*!40000 ALTER TABLE `application` DISABLE KEYS */;
/*!40000 ALTER TABLE `application` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `application_detail`
--

DROP TABLE IF EXISTS `application_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `application_detail` (
  `ID` bigint(20) NOT NULL,
  `APPLICATION_ID` bigint(20) NOT NULL,
  `image_data` longblob,
  `IMAGE_NAME` varchar(255) NOT NULL,
  `IMAGE_TYPE` varchar(255) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM AUTO_INCREMENT=61 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `application_detail`
--

LOCK TABLES `application_detail` WRITE;
/*!40000 ALTER TABLE `application_detail` DISABLE KEYS */;
/*!40000 ALTER TABLE `application_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `application_example`
--

DROP TABLE IF EXISTS `application_example`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `application_example` (
  `ID` bigint(20) NOT NULL,
  `QUERY_VALUE` text,
  `TYPE` varchar(2) DEFAULT NULL,
  `APPLICATION_ID` bigint(20) DEFAULT NULL,
  `QUERY_NAME` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `IDX_A_TY` (`TYPE`),
  KEY `FK_APP_EXMP_APP` (`APPLICATION_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `application_example`
--

LOCK TABLES `application_example` WRITE;
/*!40000 ALTER TABLE `application_example` DISABLE KEYS */;
/*!40000 ALTER TABLE `application_example` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `application_model_mapping`
--

DROP TABLE IF EXISTS `application_model_mapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `application_model_mapping` (
  `APPLICATION_ID` bigint(20) NOT NULL,
  `MODEL_ID` bigint(20) NOT NULL,
  KEY `FK_AI_APPLICATION` (`APPLICATION_ID`),
  KEY `FK_MI_MODEL` (`MODEL_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `application_model_mapping`
--

LOCK TABLES `application_model_mapping` WRITE;
/*!40000 ALTER TABLE `application_model_mapping` DISABLE KEYS */;
/*!40000 ALTER TABLE `application_model_mapping` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `application_operation`
--

DROP TABLE IF EXISTS `application_operation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `application_operation` (
  `ID` bigint(20) NOT NULL,
  `APPLICATION_ID` bigint(20) NOT NULL,
  `OPERATION_TYPE` varchar(3) DEFAULT NULL,
  `JOB_REQUEST_ID` bigint(20) DEFAULT NULL,
  `OPERATION_STATUS` int(2) NOT NULL DEFAULT '2',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `APP_OPER_APP_ID_PK` (`APPLICATION_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `application_operation`
--

LOCK TABLES `application_operation` WRITE;
/*!40000 ALTER TABLE `application_operation` DISABLE KEYS */;
/*!40000 ALTER TABLE `application_operation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `asset`
--

DROP TABLE IF EXISTS `asset`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `asset` (
  `ID` bigint(20) NOT NULL,
  `BASE_ASSET_ID` bigint(20) DEFAULT NULL,
  `DATA_SOURCE_ID` bigint(20) DEFAULT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `DISPLAY_NAME` varchar(255) DEFAULT NULL,
  `PRIORITY` decimal(10,2) NOT NULL DEFAULT '1.00',
  `AGGREGATED` char(1) DEFAULT 'N',
  `STATUS` char(1) DEFAULT 'A',
  `TYPE` int(2) DEFAULT NULL,
  `SUB_TYPE` int(2) DEFAULT NULL,
  `OWNER` int(2) DEFAULT NULL,
  `APPLICATION_ID` bigint(20) NOT NULL,
  `ORIGIN_TYPE` varchar(15) NOT NULL DEFAULT 'RDBMS',
  `PUBLISH_MODE` int(1) DEFAULT NULL,
  `QUERY_EXECUTION_ALLOWED` char(1) NOT NULL DEFAULT 'Y',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_ASSET` (`NAME`,`APPLICATION_ID`),
  KEY `FK_DS` (`DATA_SOURCE_ID`),
  KEY `FK_APP_ID_ASSET` (`APPLICATION_ID`),
  KEY `IDX_AST_OT` (`ORIGIN_TYPE`),
  KEY `IDX_AST_PM` (`PUBLISH_MODE`),
  KEY `IDX_AST_ST` (`STATUS`),
  KEY `IDX_AST_STY` (`SUB_TYPE`),
  KEY `IDX_AST_TY` (`TYPE`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `asset`
--

LOCK TABLES `asset` WRITE;
/*!40000 ALTER TABLE `asset` DISABLE KEYS */;
/*!40000 ALTER TABLE `asset` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `asset_detail`
--

DROP TABLE IF EXISTS `asset_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `asset_detail` (
  `ID` bigint(20) NOT NULL,
  `ASSET_ID` bigint(20) NOT NULL,
  `SHORT_NOTE` varchar(255) DEFAULT NULL,
  `SHORT_DISCLAIMER` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_AD_AID` (`ASSET_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `asset_detail`
--

LOCK TABLES `asset_detail` WRITE;
/*!40000 ALTER TABLE `asset_detail` DISABLE KEYS */;
/*!40000 ALTER TABLE `asset_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `asset_entity`
--

DROP TABLE IF EXISTS `asset_entity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `asset_entity` (
  `ID` bigint(20) NOT NULL,
  `ASSET_ID` bigint(20) DEFAULT NULL,
  `TABLE_ID` bigint(20) DEFAULT NULL,
  `COLUMN_ID` bigint(20) DEFAULT NULL,
  `MEMBER_ID` bigint(20) DEFAULT NULL,
  `ENTITY_TYPE` varchar(5) NOT NULL,
  `POPULARITY` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_ASSET_DRIVER` (`ASSET_ID`,`TABLE_ID`,`COLUMN_ID`,`MEMBER_ID`),
  KEY `FK_TABL` (`TABLE_ID`),
  KEY `FK_MEMBR` (`MEMBER_ID`),
  KEY `FK_COLUM` (`COLUMN_ID`),
  KEY `IDX_AE_ET` (`ENTITY_TYPE`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `asset_entity`
--

LOCK TABLES `asset_entity` WRITE;
/*!40000 ALTER TABLE `asset_entity` DISABLE KEYS */;
/*!40000 ALTER TABLE `asset_entity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `asset_extended_detail`
--

DROP TABLE IF EXISTS `asset_extended_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `asset_extended_detail` (
  `ID` bigint(20) NOT NULL,
  `ASSET_DETAIL_ID` bigint(20) NOT NULL,
  `EXTENDED_NOTE` mediumtext,
  `EXTENDED_DISCLAIMER` mediumtext,
  `CREATION_INFO` mediumtext,
  PRIMARY KEY (`ID`),
  KEY `FK_AED_ADID` (`ASSET_DETAIL_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `asset_extended_detail`
--

LOCK TABLES `asset_extended_detail` WRITE;
/*!40000 ALTER TABLE `asset_extended_detail` DISABLE KEYS */;
/*!40000 ALTER TABLE `asset_extended_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `asset_operation_detail`
--

DROP TABLE IF EXISTS `asset_operation_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `asset_operation_detail` (
  `ID` bigint(20) NOT NULL,
  `JOB_REQUEST_ID` bigint(20) DEFAULT NULL,
  `ASSET_ID` bigint(20) DEFAULT NULL,
  `PARENT_ASSET_ID` bigint(20) DEFAULT NULL,
  `OPERATION_TYPE` varchar(35) NOT NULL,
  `OPERATION_STATUS` varchar(35) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `asset_operation_detail`
--

LOCK TABLES `asset_operation_detail` WRITE;
/*!40000 ALTER TABLE `asset_operation_detail` DISABLE KEYS */;
/*!40000 ALTER TABLE `asset_operation_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `asset_operation_info`
--

DROP TABLE IF EXISTS `asset_operation_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `asset_operation_info` (
  `ID` bigint(20) NOT NULL,
  `ASSET_ID` bigint(20) NOT NULL,
  `START_DATE` datetime NOT NULL,
  `COMPLETION_DATE` datetime DEFAULT NULL,
  `ASSET_OPERATION_DATA` longtext,
  `CHANGE_FOUND` char(1) DEFAULT 'N',
  `STATUS` char(1) DEFAULT 'N',
  `OPERATION` char(1) NOT NULL,
  `OPERATION_TYPE` varchar(35) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `asset_operation_info`
--

LOCK TABLES `asset_operation_info` WRITE;
/*!40000 ALTER TABLE `asset_operation_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `asset_operation_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `batch_process`
--

DROP TABLE IF EXISTS `batch_process`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `batch_process` (
  `ID` bigint(20) NOT NULL,
  `APPLICATION_ID` bigint(20) DEFAULT NULL,
  `ASSET_ID` bigint(20) DEFAULT NULL,
  `MODEL_ID` bigint(20) DEFAULT NULL,
  `BATCH_TYPE` varchar(35) NOT NULL,
  `JOB_REQUEST_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `batch_process`
--

LOCK TABLES `batch_process` WRITE;
/*!40000 ALTER TABLE `batch_process` DISABLE KEYS */;
/*!40000 ALTER TABLE `batch_process` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `batch_process_detail`
--

DROP TABLE IF EXISTS `batch_process_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `batch_process_detail` (
  `ID` bigint(20) NOT NULL,
  `BATCH_PROCESS_ID` bigint(20) NOT NULL,
  `PARAM_NAME` varchar(255) DEFAULT NULL,
  `PARAM_VALUE` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_BATCH_PROCESS_DETAIL` (`BATCH_PROCESS_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `batch_process_detail`
--

LOCK TABLES `batch_process_detail` WRITE;
/*!40000 ALTER TABLE `batch_process_detail` DISABLE KEYS */;
/*!40000 ALTER TABLE `batch_process_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `behavior`
--

DROP TABLE IF EXISTS `behavior`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `behavior` (
  `ID` bigint(20) NOT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `behavior`
--

LOCK TABLES `behavior` WRITE;
/*!40000 ALTER TABLE `behavior` DISABLE KEYS */;
INSERT INTO `behavior` VALUES (101,'ABSTRACT');
INSERT INTO `behavior` VALUES (102,'ATTRIBUTE');
INSERT INTO `behavior` VALUES (103,'QUANTITATIVE');
INSERT INTO `behavior` VALUES (104,'ENUMERATION');
INSERT INTO `behavior` VALUES (105,'POPULATION');
INSERT INTO `behavior` VALUES (106,'COMPARATIVE');
INSERT INTO `behavior` VALUES (107,'GRAIN');
INSERT INTO `behavior` VALUES (108,'DISTRIBUTION');
INSERT INTO `behavior` VALUES (109,'mutually exclusive');
INSERT INTO `behavior` VALUES (110,'Indicator');
INSERT INTO `behavior` VALUES (111,'MULTIVALUED');
INSERT INTO `behavior` VALUES (112,'MULTIVALUED_GLOBAL_WEIGHT');
INSERT INTO `behavior` VALUES (113,'DEPENDENT_VARIABLE');
/*!40000 ALTER TABLE `behavior` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `behavior_association_rule`
--

DROP TABLE IF EXISTS `behavior_association_rule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `behavior_association_rule` (
  `ID` bigint(20) NOT NULL,
  `BEHAVIOR_BE_ID` bigint(20) NOT NULL,
  `BEHAVIOR_ASSOCIATION_POS` varchar(2) NOT NULL,
  `RULE_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `behavior_association_rule`
--

LOCK TABLES `behavior_association_rule` WRITE;
/*!40000 ALTER TABLE `behavior_association_rule` DISABLE KEYS */;
INSERT INTO `behavior_association_rule` VALUES (1,9010,'D',10008);
/*!40000 ALTER TABLE `behavior_association_rule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bookmark`
--

DROP TABLE IF EXISTS `bookmark`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bookmark` (
  `ID` bigint(20) NOT NULL,
  `FOLDER_ID` bigint(20) DEFAULT NULL,
  `USER_ID` bigint(20) DEFAULT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `TYPE` char(1) NOT NULL DEFAULT 'Q',
  `VALUE` mediumtext,
  `DATE_CREATED` datetime DEFAULT NULL,
  `DATE_MODIFIED` datetime DEFAULT NULL,
  `SUMMARY` varchar(255) DEFAULT NULL,
  `APPLICATION_ID` bigint(20) DEFAULT NULL,
  `MODEL_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_BM_USRID` (`USER_ID`),
  KEY `FK_BM_FLDRID` (`FOLDER_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bookmark`
--

LOCK TABLES `bookmark` WRITE;
/*!40000 ALTER TABLE `bookmark` DISABLE KEYS */;
/*!40000 ALTER TABLE `bookmark` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `business_entity`
--

DROP TABLE IF EXISTS `business_entity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `business_entity` (
  `ID` bigint(20) NOT NULL,
  `CONCEPT_ID` bigint(20) DEFAULT NULL,
  `INSTANCE_ID` bigint(20) DEFAULT NULL,
  `TYPE_ID` bigint(20) DEFAULT NULL,
  `POPULARITY` bigint(20) DEFAULT '0',
  `RELATION_ID` bigint(20) DEFAULT NULL,
  `CONCEPT_PROFILE_ID` bigint(20) DEFAULT NULL,
  `INSTANCE_PROFILE_ID` bigint(20) DEFAULT NULL,
  `ENTITY_TYPE` varchar(5) NOT NULL,
  `MODEL_GROUP_ID` bigint(20) NOT NULL,
  `BEHAVIOR_ID` bigint(20) DEFAULT NULL,
  `KNOWLEDGE_ID` bigint(20) DEFAULT NULL,
  `FROM_SHARED` char(1) NOT NULL DEFAULT 'N',
  PRIMARY KEY (`ID`),
  KEY `FK_BE_RID` (`RELATION_ID`),
  KEY `FK_BE_IPID` (`INSTANCE_PROFILE_ID`),
  KEY `FK_BE_IID` (`INSTANCE_ID`),
  KEY `FK_BE_CPID` (`CONCEPT_PROFILE_ID`),
  KEY `FK_BE_CID` (`CONCEPT_ID`),
  KEY `FK_MGID_BUSINESS_ENTITY` (`MODEL_GROUP_ID`),
  KEY `FK_BE_TID` (`TYPE_ID`),
  KEY `BE_KNOWLEDGE_ID_IDX` (`KNOWLEDGE_ID`),
  KEY `IDX_BE_ET` (`ENTITY_TYPE`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `business_entity`
--

LOCK TABLES `business_entity` WRITE;
/*!40000 ALTER TABLE `business_entity` DISABLE KEYS */;
INSERT INTO `business_entity` VALUES (101,NULL,NULL,101,0,NULL,NULL,NULL,'T',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (102,NULL,NULL,102,0,NULL,NULL,NULL,'T',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (103,104,NULL,103,0,NULL,NULL,NULL,'RT',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (104,116,NULL,104,0,NULL,NULL,NULL,'RT',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (105,105,NULL,105,0,NULL,NULL,NULL,'RT',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (106,103,NULL,106,0,NULL,NULL,NULL,'RT',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (107,NULL,NULL,107,0,NULL,NULL,NULL,'T',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (151,NULL,NULL,151,0,NULL,NULL,NULL,'T',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (152,102,NULL,152,0,NULL,NULL,NULL,'C',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (201,NULL,NULL,201,0,NULL,NULL,NULL,'T',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (202,NULL,NULL,202,0,NULL,NULL,NULL,'T',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (203,NULL,NULL,203,0,NULL,NULL,NULL,'T',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (204,NULL,NULL,204,0,NULL,NULL,NULL,'T',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (205,NULL,NULL,205,0,NULL,NULL,NULL,'T',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (206,NULL,NULL,206,0,NULL,NULL,NULL,'T',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (207,NULL,NULL,207,0,NULL,NULL,NULL,'T',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (208,NULL,NULL,208,0,NULL,NULL,NULL,'T',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (209,NULL,NULL,209,0,NULL,NULL,NULL,'T',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (251,NULL,NULL,251,0,NULL,NULL,NULL,'T',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (301,NULL,NULL,301,0,NULL,NULL,NULL,'T',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (302,NULL,NULL,302,0,NULL,NULL,NULL,'T',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (303,NULL,NULL,303,0,NULL,NULL,NULL,'T',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (304,NULL,NULL,304,0,NULL,NULL,NULL,'T',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (305,NULL,NULL,305,0,NULL,NULL,NULL,'T',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (306,NULL,NULL,306,0,NULL,NULL,NULL,'T',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (307,NULL,NULL,307,0,NULL,NULL,NULL,'T',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (308,NULL,NULL,308,0,NULL,NULL,NULL,'T',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (309,NULL,NULL,309,0,NULL,NULL,NULL,'T',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (5464,124,5464,107,0,NULL,NULL,NULL,'CLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (401,101,NULL,401,0,NULL,NULL,NULL,'RT',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (402,NULL,NULL,402,0,NULL,NULL,NULL,'T',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (403,110,NULL,403,0,NULL,NULL,NULL,'RT',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (404,106,NULL,404,0,NULL,NULL,NULL,'RT',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (405,109,NULL,405,0,NULL,NULL,NULL,'RT',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (406,107,NULL,406,0,NULL,NULL,NULL,'RT',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (407,NULL,NULL,407,0,NULL,NULL,NULL,'T',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (408,108,NULL,408,0,NULL,NULL,NULL,'RT',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (1001,104,1001,103,0,NULL,NULL,NULL,'RTLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (1002,104,1002,103,0,NULL,NULL,NULL,'RTLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (1003,104,1003,103,0,NULL,NULL,NULL,'RTLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (1004,104,1004,103,0,NULL,NULL,NULL,'RTLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (1005,104,1005,103,0,NULL,NULL,NULL,'RTLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (1006,104,1006,103,0,NULL,NULL,NULL,'RTLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (1051,105,1051,105,0,NULL,NULL,NULL,'RTLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (1052,105,1052,105,0,NULL,NULL,NULL,'RTLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (1053,105,1053,105,0,NULL,NULL,NULL,'RTLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (1054,105,1054,105,0,NULL,NULL,NULL,'RTLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (1055,105,1055,105,0,NULL,NULL,NULL,'RTLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (1056,105,1056,105,0,NULL,NULL,NULL,'RTLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (2001,103,2001,106,0,NULL,NULL,NULL,'RTLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (2002,103,2002,106,0,NULL,NULL,NULL,'RTLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (2003,103,2003,106,0,NULL,NULL,NULL,'RTLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (2004,103,2004,106,0,NULL,NULL,NULL,'RTLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (2005,103,2005,106,0,NULL,NULL,NULL,'RTLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (5001,121,5001,107,0,NULL,NULL,NULL,'CLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (5002,121,5002,107,0,NULL,NULL,NULL,'CLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (108,NULL,NULL,108,0,NULL,NULL,NULL,'T',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (210,NULL,NULL,210,0,NULL,NULL,NULL,'T',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (501,NULL,NULL,108,0,101,NULL,NULL,'R',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (502,NULL,NULL,108,0,102,NULL,NULL,'R',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (503,NULL,NULL,108,0,103,NULL,NULL,'R',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (504,NULL,NULL,108,0,104,NULL,NULL,'R',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (1101,106,1101,404,0,NULL,NULL,NULL,'RTLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (1102,106,1102,404,0,NULL,NULL,NULL,'RTLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (1103,106,1103,404,0,NULL,NULL,NULL,'RTLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (1104,106,1104,404,0,NULL,NULL,NULL,'RTLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (1105,106,1105,404,0,NULL,NULL,NULL,'RTLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (1106,106,1106,404,0,NULL,NULL,NULL,'RTLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (1533,NULL,1533,211,0,NULL,NULL,NULL,'TLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (1108,106,1108,404,0,NULL,NULL,NULL,'RTLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (1532,NULL,1532,211,0,NULL,NULL,NULL,'TLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (1110,106,1110,404,0,NULL,NULL,NULL,'RTLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (1111,106,1111,404,0,NULL,NULL,NULL,'RTLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (1112,106,1112,404,0,NULL,NULL,NULL,'RTLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (1531,NULL,1531,211,0,NULL,NULL,NULL,'TLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (1114,106,1114,404,0,NULL,NULL,NULL,'RTLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (1115,106,1115,404,0,NULL,NULL,NULL,'RTLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (1116,107,1116,406,0,NULL,NULL,NULL,'RTLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (1117,108,1117,408,0,NULL,NULL,NULL,'RTLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (1118,109,1118,405,0,NULL,NULL,NULL,'RTLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (1119,109,1119,405,0,NULL,NULL,NULL,'RTLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (1200,110,1200,403,0,NULL,NULL,NULL,'RTLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (1501,NULL,1501,204,0,NULL,NULL,NULL,'TLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (1502,NULL,1502,204,0,NULL,NULL,NULL,'TLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (1503,NULL,1503,204,0,NULL,NULL,NULL,'TLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (1504,NULL,1504,204,0,NULL,NULL,NULL,'TLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (1505,NULL,1505,204,0,NULL,NULL,NULL,'TLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (1506,NULL,1506,204,0,NULL,NULL,NULL,'TLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (1507,NULL,1507,204,0,NULL,NULL,NULL,'TLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (1508,NULL,1508,204,0,NULL,NULL,NULL,'TLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (1509,NULL,1509,204,0,NULL,NULL,NULL,'TLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (1510,NULL,1510,204,0,NULL,NULL,NULL,'TLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (1511,NULL,1511,204,0,NULL,NULL,NULL,'TLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (1512,NULL,1512,204,0,NULL,NULL,NULL,'TLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (6001,101,6001,401,0,NULL,NULL,NULL,'RI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (5201,123,5201,107,0,NULL,NULL,NULL,'CLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (5202,123,5202,107,0,NULL,NULL,NULL,'CLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (5301,122,5301,107,0,NULL,NULL,NULL,'CLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (5302,122,5302,107,0,NULL,NULL,NULL,'CLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (409,113,NULL,409,0,NULL,NULL,NULL,'RT',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (1120,113,1120,409,0,NULL,NULL,NULL,'RTLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (505,NULL,NULL,108,0,105,NULL,NULL,'R',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (109,114,NULL,109,0,NULL,NULL,NULL,'C',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (1007,116,1007,104,0,NULL,NULL,NULL,'RTLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (1008,116,1008,104,0,NULL,NULL,NULL,'RTLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (9001,NULL,NULL,NULL,0,NULL,NULL,NULL,'B',1,101,NULL,'N');
INSERT INTO `business_entity` VALUES (9002,NULL,NULL,NULL,0,NULL,NULL,NULL,'B',1,102,NULL,'N');
INSERT INTO `business_entity` VALUES (9003,NULL,NULL,NULL,0,NULL,NULL,NULL,'B',1,103,NULL,'N');
INSERT INTO `business_entity` VALUES (9004,NULL,NULL,NULL,0,NULL,NULL,NULL,'B',1,104,NULL,'N');
INSERT INTO `business_entity` VALUES (9005,NULL,NULL,NULL,0,NULL,NULL,NULL,'B',1,105,NULL,'N');
INSERT INTO `business_entity` VALUES (9006,NULL,NULL,NULL,0,NULL,NULL,NULL,'B',1,106,NULL,'N');
INSERT INTO `business_entity` VALUES (9007,NULL,NULL,NULL,0,NULL,NULL,NULL,'B',1,107,NULL,'N');
INSERT INTO `business_entity` VALUES (9008,NULL,NULL,NULL,0,NULL,NULL,NULL,'B',1,108,NULL,'N');
INSERT INTO `business_entity` VALUES (506,NULL,NULL,108,0,106,NULL,NULL,'R',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (110,117,NULL,110,0,NULL,NULL,NULL,'RT',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (111,NULL,NULL,111,0,NULL,NULL,NULL,'T',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (507,NULL,NULL,108,0,107,NULL,NULL,'R',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (112,NULL,NULL,112,0,NULL,NULL,NULL,'T',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (410,118,NULL,410,0,NULL,NULL,NULL,'RT',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (1121,118,1121,410,0,NULL,NULL,NULL,'RTLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (1122,118,1122,410,0,NULL,NULL,NULL,'RTLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (1123,130,1123,413,0,NULL,NULL,NULL,'RTLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (1124,130,1124,413,0,NULL,NULL,NULL,'RTLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (1125,130,1125,413,0,NULL,NULL,NULL,'RTLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (1126,130,1126,413,0,NULL,NULL,NULL,'RTLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (1201,110,1201,403,0,NULL,NULL,NULL,'RTLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (508,NULL,NULL,108,0,108,NULL,NULL,'R',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (1513,NULL,1513,206,0,NULL,NULL,NULL,'RI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (411,NULL,NULL,411,0,NULL,NULL,NULL,'T',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (1150,NULL,1150,411,0,NULL,NULL,NULL,'TLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (1151,NULL,1151,411,0,NULL,NULL,NULL,'TLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (5303,122,5303,107,0,NULL,NULL,NULL,'CLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (5304,122,5304,107,0,NULL,NULL,NULL,'CLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (5451,125,5451,107,0,NULL,NULL,NULL,'CLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (5461,124,5461,107,0,NULL,NULL,NULL,'CLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (113,121,NULL,107,1,NULL,NULL,NULL,'C',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (114,122,NULL,107,1,NULL,NULL,NULL,'C',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (115,123,NULL,107,0,NULL,NULL,NULL,'C',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (116,124,NULL,107,0,NULL,NULL,NULL,'C',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (117,125,NULL,107,0,NULL,NULL,NULL,'C',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (153,NULL,NULL,152,0,NULL,NULL,NULL,'T',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (154,112,NULL,152,0,NULL,NULL,NULL,'C',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (155,120,NULL,152,0,NULL,NULL,NULL,'C',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (156,119,NULL,152,0,NULL,NULL,NULL,'C',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (157,126,NULL,152,0,NULL,NULL,NULL,'C',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (211,201,NULL,211,0,NULL,NULL,NULL,'C',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (1515,201,1515,211,NULL,NULL,NULL,NULL,'RI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (412,202,NULL,412,0,NULL,NULL,NULL,'RT',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (1152,202,1152,412,0,NULL,NULL,NULL,'RTLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (1153,202,1153,412,0,NULL,NULL,NULL,'RTLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (212,NULL,NULL,211,0,NULL,NULL,NULL,'T',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (9009,NULL,NULL,NULL,0,NULL,NULL,NULL,'B',1,109,NULL,'N');
INSERT INTO `business_entity` VALUES (509,NULL,NULL,108,0,109,NULL,NULL,'R',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (7001,102,7001,152,NULL,NULL,NULL,NULL,'RI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (158,127,NULL,152,0,NULL,NULL,NULL,'C',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (159,128,NULL,152,0,NULL,NULL,NULL,'C',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (9010,NULL,NULL,NULL,0,NULL,NULL,NULL,'B',1,110,NULL,'N');
INSERT INTO `business_entity` VALUES (160,111,NULL,152,0,NULL,NULL,NULL,'C',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (2006,103,2006,106,0,NULL,NULL,NULL,'RTLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (5305,122,5305,107,0,NULL,NULL,NULL,'CLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (5306,122,5306,107,0,NULL,NULL,NULL,'CLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (5452,125,5452,107,0,NULL,NULL,NULL,'CLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (5453,125,5453,107,0,NULL,NULL,NULL,'CLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (5462,124,5462,107,0,NULL,NULL,NULL,'CLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (5463,124,5463,107,0,NULL,NULL,NULL,'CLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (161,129,NULL,152,0,NULL,NULL,NULL,'C',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (413,130,NULL,413,0,NULL,NULL,NULL,'RT',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (1127,118,1127,410,0,NULL,NULL,NULL,'RTLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (1128,118,1128,410,0,NULL,NULL,NULL,'RTLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (1202,110,1202,403,0,NULL,NULL,NULL,'RTLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (1516,NULL,1516,206,0,NULL,NULL,NULL,'RTLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (1517,NULL,1517,206,0,NULL,NULL,NULL,'RTLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (1518,NULL,1518,206,0,NULL,NULL,NULL,'RTLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (1519,NULL,1519,206,0,NULL,NULL,NULL,'RTLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (1520,NULL,1520,206,0,NULL,NULL,NULL,'RTLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (213,NULL,NULL,213,0,NULL,NULL,NULL,'T',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (1521,NULL,1521,213,0,NULL,NULL,NULL,'TLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (1522,NULL,1522,213,0,NULL,NULL,NULL,'TLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (1523,NULL,1523,213,0,NULL,NULL,NULL,'TLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (1524,NULL,1524,213,0,NULL,NULL,NULL,'TLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (1525,NULL,1525,213,0,NULL,NULL,NULL,'TLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (1526,NULL,1526,213,0,NULL,NULL,NULL,'TLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (1527,NULL,1527,213,0,NULL,NULL,NULL,'TLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (1528,NULL,1528,213,0,NULL,NULL,NULL,'TLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (1534,NULL,1534,211,0,NULL,NULL,NULL,'TLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (510,NULL,NULL,108,0,110,NULL,NULL,'R',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (5465,124,5465,107,0,NULL,NULL,NULL,'CLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (5466,124,5466,107,0,NULL,NULL,NULL,'CLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (414,203,NULL,152,0,NULL,NULL,NULL,'C',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (415,204,NULL,107,0,NULL,NULL,NULL,'C',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (416,204,7002,107,0,NULL,NULL,NULL,'CLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (417,204,7003,107,0,NULL,NULL,NULL,'CLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (418,204,7004,107,0,NULL,NULL,NULL,'CLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (419,204,7005,107,0,NULL,NULL,NULL,'CLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (420,204,7006,107,0,NULL,NULL,NULL,'CLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (421,204,7007,107,0,NULL,NULL,NULL,'CLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (422,205,NULL,152,0,NULL,NULL,NULL,'C',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (423,206,NULL,107,0,NULL,NULL,NULL,'C',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (424,206,7008,107,0,NULL,NULL,NULL,'CLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (425,206,7009,107,0,NULL,NULL,NULL,'CLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (426,206,7010,107,0,NULL,NULL,NULL,'CLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (427,207,NULL,152,0,NULL,NULL,NULL,'C',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (428,208,NULL,107,0,NULL,NULL,NULL,'C',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (429,208,7011,107,0,NULL,NULL,NULL,'CLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (430,208,7012,107,0,NULL,NULL,NULL,'CLI',1,NULL,NULL,'N');
INSERT INTO `business_entity` VALUES (9011,NULL,NULL,NULL,0,NULL,NULL,NULL,'B',1,111,NULL,'N');
INSERT INTO `business_entity` VALUES (9012,NULL,NULL,NULL,0,NULL,NULL,NULL,'B',1,112,NULL,'N');
INSERT INTO `business_entity` VALUES (9013,NULL,NULL,NULL,0,NULL,NULL,NULL,'B',1,113,NULL,'N');
/*!40000 ALTER TABLE `business_entity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `business_entity_maintenance`
--

DROP TABLE IF EXISTS `business_entity_maintenance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `business_entity_maintenance` (
  `ID` bigint(20) NOT NULL,
  `ENTITY_BED_ID` bigint(20) NOT NULL,
  `ENTITY_TYPE` varchar(5) DEFAULT NULL,
  `MODEL_ID` bigint(20) NOT NULL,
  `OPERATION_TYPE` char(1) NOT NULL,
  `PARENT_BED_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `IDX_BEM_OT` (`OPERATION_TYPE`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `business_entity_maintenance`
--

LOCK TABLES `business_entity_maintenance` WRITE;
/*!40000 ALTER TABLE `business_entity_maintenance` DISABLE KEYS */;
/*!40000 ALTER TABLE `business_entity_maintenance` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `business_entity_variation`
--

DROP TABLE IF EXISTS `business_entity_variation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `business_entity_variation` (
  `id` bigint(20) NOT NULL,
  `entity_be_id` bigint(20) DEFAULT NULL,
  `model_group_id` bigint(20) DEFAULT NULL,
  `variation` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `business_entity_variation`
--

LOCK TABLES `business_entity_variation` WRITE;
/*!40000 ALTER TABLE `business_entity_variation` DISABLE KEYS */;
INSERT INTO `business_entity_variation` VALUES (1,5464,1,'cft');
INSERT INTO `business_entity_variation` VALUES (2,5464,1,'cu ft');
INSERT INTO `business_entity_variation` VALUES (3,207,1,'hr');
INSERT INTO `business_entity_variation` VALUES (4,207,1,'hrs');
INSERT INTO `business_entity_variation` VALUES (5,209,1,'secs');
INSERT INTO `business_entity_variation` VALUES (6,208,1,'mins');
INSERT INTO `business_entity_variation` VALUES (7,416,1,'m2');
INSERT INTO `business_entity_variation` VALUES (8,416,1,'sq m');
INSERT INTO `business_entity_variation` VALUES (9,417,1,'in2');
INSERT INTO `business_entity_variation` VALUES (10,417,1,'sq in');
INSERT INTO `business_entity_variation` VALUES (11,418,1,'ft2');
INSERT INTO `business_entity_variation` VALUES (12,418,1,'sq ft');
INSERT INTO `business_entity_variation` VALUES (13,419,1,'yd2');
INSERT INTO `business_entity_variation` VALUES (14,419,1,'sq yd');
INSERT INTO `business_entity_variation` VALUES (15,419,1,'sy');
INSERT INTO `business_entity_variation` VALUES (16,420,1,'ac');
INSERT INTO `business_entity_variation` VALUES (17,421,1,'ha');
INSERT INTO `business_entity_variation` VALUES (18,424,1,'mb');
INSERT INTO `business_entity_variation` VALUES (19,425,1,'gb');
INSERT INTO `business_entity_variation` VALUES (20,426,1,'kb');
INSERT INTO `business_entity_variation` VALUES (21,429,1,'mp');
INSERT INTO `business_entity_variation` VALUES (22,1002,1,'Avg');
INSERT INTO `business_entity_variation` VALUES (23,1003,1,'Cnt');
INSERT INTO `business_entity_variation` VALUES (24,1003,1,'Ct');
INSERT INTO `business_entity_variation` VALUES (25,206,1,'Days');
INSERT INTO `business_entity_variation` VALUES (26,203,1,'Qtr');
INSERT INTO `business_entity_variation` VALUES (27,203,1,'Qtrs');
INSERT INTO `business_entity_variation` VALUES (28,202,1,'Yr');
INSERT INTO `business_entity_variation` VALUES (29,202,1,'Yrs');
INSERT INTO `business_entity_variation` VALUES (30,156,1,'pwr');
INSERT INTO `business_entity_variation` VALUES (31,157,1,'Num');
/*!40000 ALTER TABLE `business_entity_variation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `canned_report`
--

DROP TABLE IF EXISTS `canned_report`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `canned_report` (
  `ID` bigint(20) NOT NULL,
  `NAME` varchar(315) DEFAULT NULL,
  `QUERY_STRING` blob,
  `HEADER_XML` blob,
  `DATASOURCE_ID` bigint(20) DEFAULT NULL,
  `PRESENTATION_XML` blob,
  `TYPE` varchar(315) DEFAULT NULL,
  KEY `IDX_CR_DI` (`DATASOURCE_ID`),
  KEY `IDX_CR_ID` (`ID`),
  KEY `IDX_CR_NM` (`NAME`),
  KEY `IDX_CR_TY` (`TYPE`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `canned_report`
--

LOCK TABLES `canned_report` WRITE;
/*!40000 ALTER TABLE `canned_report` DISABLE KEYS */;
/*!40000 ALTER TABLE `canned_report` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cloud`
--

DROP TABLE IF EXISTS `cloud`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cloud` (
  `ID` bigint(20) NOT NULL,
  `NAME` varchar(255) NOT NULL,
  `CATEGORY` int(2) NOT NULL COMMENT 'Category for the cloud Using 1 for ''type'' cloud',
  `OUTPUT_BE_ID` bigint(20) DEFAULT NULL,
  `OUTPUT_NAME` varchar(255) DEFAULT NULL,
  `OUTPT` char(1) NOT NULL,
  `CLOUD_PARTICIPATION` varchar(255) DEFAULT NULL,
  `REQUIRED_COMP_COUNT` int(2) NOT NULL DEFAULT '0',
  `DFAULT` char(1) DEFAULT 'N',
  PRIMARY KEY (`ID`),
  KEY `NAME_IDX` (`NAME`),
  KEY `FK_cloud` (`OUTPUT_BE_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cloud`
--

LOCK TABLES `cloud` WRITE;
/*!40000 ALTER TABLE `cloud` DISABLE KEYS */;
INSERT INTO `cloud` VALUES (101,'Unit',1,151,'Unit','N',NULL,1,'N');
INSERT INTO `cloud` VALUES (102,'Value',1,153,'Value','N',NULL,1,'N');
INSERT INTO `cloud` VALUES (104,'Quarter',1,203,'Quarter','N',NULL,2,'N');
INSERT INTO `cloud` VALUES (105,'Year',1,202,'Year','N',NULL,1,'N');
INSERT INTO `cloud` VALUES (106,'UnitRange',1,153,'Value','N',NULL,3,'N');
INSERT INTO `cloud` VALUES (107,'TimeRange',1,201,'TimeFrame','N',NULL,3,'N');
INSERT INTO `cloud` VALUES (108,'FromToUnitRange',1,153,'Value','N',NULL,3,'N');
INSERT INTO `cloud` VALUES (109,'FromToTimeRange',1,201,'TimeFrame','N',NULL,3,'N');
INSERT INTO `cloud` VALUES (110,'MonthTimeFrame',1,201,'TimeFrame','N',NULL,1,'N');
INSERT INTO `cloud` VALUES (111,'QuarterTimeFrame',1,201,'TimeFrame','N',NULL,1,'N');
INSERT INTO `cloud` VALUES (112,'YearTimeFrame',1,201,'TimeFrame','N',NULL,1,'N');
INSERT INTO `cloud` VALUES (113,'RelativeYear',1,201,'TimeFrame','N',NULL,1,'N');
INSERT INTO `cloud` VALUES (501,'TopBottom',3,NULL,NULL,'E',NULL,1,'N');
INSERT INTO `cloud` VALUES (502,'Group-By',3,NULL,NULL,'E',NULL,2,'N');
INSERT INTO `cloud` VALUES (114,'ComparativeInformation',1,110,'ComparativeInformation','N',NULL,1,'N');
INSERT INTO `cloud` VALUES (503,'Group-By1',3,NULL,NULL,'E',NULL,2,'N');
INSERT INTO `cloud` VALUES (601,'relativeForQuantitative',4,NULL,NULL,'N',NULL,2,'N');
INSERT INTO `cloud` VALUES (201,'BaseCloud',5,NULL,NULL,'I',NULL,0,'N');
INSERT INTO `cloud` VALUES (115,'Value1',1,153,'Value','N',NULL,2,'N');
INSERT INTO `cloud` VALUES (117,'RelativeMonth',1,204,'Month','N',NULL,2,'N');
INSERT INTO `cloud` VALUES (118,'RelativeQuarter',1,203,'Quarter','N',NULL,2,'N');
INSERT INTO `cloud` VALUES (116,'DayTimeFrame',1,201,'TimeFrame','N',NULL,1,'N');
INSERT INTO `cloud` VALUES (119,'RelativeDay',1,206,'Day','N',NULL,2,'N');
INSERT INTO `cloud` VALUES (606,'BetweenAndValue',4,153,'Value','N',NULL,2,'N');
INSERT INTO `cloud` VALUES (602,'Currency',4,152,'Value','N',NULL,1,'N');
INSERT INTO `cloud` VALUES (603,'Distance',4,154,'Value','N',NULL,2,'N');
INSERT INTO `cloud` VALUES (604,'Volume',4,155,'Value','N',NULL,2,'N');
INSERT INTO `cloud` VALUES (605,'Power',4,156,'Value','N',NULL,2,'N');
INSERT INTO `cloud` VALUES (202,'Time',1,201,'TimeFrame','N',NULL,1,'N');
INSERT INTO `cloud` VALUES (607,'ValueWithPreposition',4,153,'Value','N',NULL,2,'N');
INSERT INTO `cloud` VALUES (203,'Week',1,205,'Week','N',NULL,2,'N');
INSERT INTO `cloud` VALUES (204,'WeekTimeFrame',1,201,'TimeFrame','N',NULL,1,'N');
INSERT INTO `cloud` VALUES (611,'YearTimeFrameConcept',4,201,'TimeFrame','N',NULL,3,'N');
INSERT INTO `cloud` VALUES (121,'RelativeTFTime',1,211,'Time','N',NULL,1,'N');
INSERT INTO `cloud` VALUES (608,'FromToValue',4,153,'Value','N',NULL,1,'N');
INSERT INTO `cloud` VALUES (609,'Weight',4,160,'Value','N',NULL,2,'N');
INSERT INTO `cloud` VALUES (610,'TimeDuration',4,161,'Value','N',NULL,1,'N');
INSERT INTO `cloud` VALUES (122,'RelativeWeek',1,205,'Week','N',NULL,2,'N');
INSERT INTO `cloud` VALUES (123,'RelativeWeekday',1,213,'Weekday','N',NULL,2,'N');
INSERT INTO `cloud` VALUES (124,'WeekdayTimeFrame',1,201,'TimeFrame','N',NULL,1,'N');
INSERT INTO `cloud` VALUES (205,'LocationInformationCloud',5,NULL,NULL,'I',NULL,0,'N');
INSERT INTO `cloud` VALUES (612,'Location',1,301,'Location','N',NULL,0,'N');
INSERT INTO `cloud` VALUES (613,'Area',4,414,'Value','N',NULL,2,'N');
INSERT INTO `cloud` VALUES (614,'Memory',4,422,'Value','N',NULL,2,'N');
INSERT INTO `cloud` VALUES (615,'Resolution',4,427,'Value','N',NULL,2,'N');
INSERT INTO `cloud` VALUES (616,'Number',4,157,'Value','N',NULL,1,'N');
/*!40000 ALTER TABLE `cloud` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cloud_allowed_behavior`
--

DROP TABLE IF EXISTS `cloud_allowed_behavior`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cloud_allowed_behavior` (
  `CLOUD_ID` bigint(20) NOT NULL,
  `BEHAVIOR_BE_ID` bigint(20) NOT NULL,
  KEY `CAB_FK_CLOUD` (`CLOUD_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cloud_allowed_behavior`
--

LOCK TABLES `cloud_allowed_behavior` WRITE;
/*!40000 ALTER TABLE `cloud_allowed_behavior` DISABLE KEYS */;
INSERT INTO `cloud_allowed_behavior` VALUES (501,9002);
/*!40000 ALTER TABLE `cloud_allowed_behavior` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cloud_allowed_component`
--

DROP TABLE IF EXISTS `cloud_allowed_component`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cloud_allowed_component` (
  `CLOUD_ID` bigint(20) NOT NULL,
  `COMP_BE_ID` bigint(20) NOT NULL,
  KEY `FK_cloud_allowed_component` (`CLOUD_ID`),
  KEY `FK_cloud_allowed_component_BE` (`COMP_BE_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cloud_allowed_component`
--

LOCK TABLES `cloud_allowed_component` WRITE;
/*!40000 ALTER TABLE `cloud_allowed_component` DISABLE KEYS */;
INSERT INTO `cloud_allowed_component` VALUES (111,404);
INSERT INTO `cloud_allowed_component` VALUES (104,408);
INSERT INTO `cloud_allowed_component` VALUES (105,408);
INSERT INTO `cloud_allowed_component` VALUES (111,408);
INSERT INTO `cloud_allowed_component` VALUES (111,203);
INSERT INTO `cloud_allowed_component` VALUES (110,408);
INSERT INTO `cloud_allowed_component` VALUES (110,204);
INSERT INTO `cloud_allowed_component` VALUES (105,401);
INSERT INTO `cloud_allowed_component` VALUES (501,406);
INSERT INTO `cloud_allowed_component` VALUES (110,404);
INSERT INTO `cloud_allowed_component` VALUES (116,404);
INSERT INTO `cloud_allowed_component` VALUES (204,404);
INSERT INTO `cloud_allowed_component` VALUES (110,411);
INSERT INTO `cloud_allowed_component` VALUES (111,411);
INSERT INTO `cloud_allowed_component` VALUES (116,411);
INSERT INTO `cloud_allowed_component` VALUES (202,411);
INSERT INTO `cloud_allowed_component` VALUES (204,411);
INSERT INTO `cloud_allowed_component` VALUES (603,408);
INSERT INTO `cloud_allowed_component` VALUES (603,151);
INSERT INTO `cloud_allowed_component` VALUES (602,408);
INSERT INTO `cloud_allowed_component` VALUES (602,151);
INSERT INTO `cloud_allowed_component` VALUES (604,408);
INSERT INTO `cloud_allowed_component` VALUES (604,151);
INSERT INTO `cloud_allowed_component` VALUES (605,408);
INSERT INTO `cloud_allowed_component` VALUES (605,151);
INSERT INTO `cloud_allowed_component` VALUES (609,408);
INSERT INTO `cloud_allowed_component` VALUES (609,151);
INSERT INTO `cloud_allowed_component` VALUES (202,404);
INSERT INTO `cloud_allowed_component` VALUES (124,404);
INSERT INTO `cloud_allowed_component` VALUES (111,405);
INSERT INTO `cloud_allowed_component` VALUES (110,405);
INSERT INTO `cloud_allowed_component` VALUES (613,408);
INSERT INTO `cloud_allowed_component` VALUES (613,151);
INSERT INTO `cloud_allowed_component` VALUES (614,408);
INSERT INTO `cloud_allowed_component` VALUES (614,151);
INSERT INTO `cloud_allowed_component` VALUES (615,408);
INSERT INTO `cloud_allowed_component` VALUES (615,151);
INSERT INTO `cloud_allowed_component` VALUES (612,411);
INSERT INTO `cloud_allowed_component` VALUES (612,404);
INSERT INTO `cloud_allowed_component` VALUES (616,408);
INSERT INTO `cloud_allowed_component` VALUES (616,151);
/*!40000 ALTER TABLE `cloud_allowed_component` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cloud_component`
--

DROP TABLE IF EXISTS `cloud_component`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cloud_component` (
  `ID` bigint(20) NOT NULL,
  `CLOUD_ID` bigint(20) NOT NULL,
  `COMP_BE_ID` bigint(20) NOT NULL,
  `WEIGHT` decimal(10,2) NOT NULL DEFAULT '0.00',
  `FREQUENCY` int(2) NOT NULL DEFAULT '1',
  `COMP_TYPE_BE_ID` bigint(20) DEFAULT NULL,
  `CATEGORY` int(2) DEFAULT NULL,
  `REPRESENTATIVE_ENTITY_TYPE` varchar(5) DEFAULT NULL,
  `REQUIRED_BEHAVIOR` bigint(20) DEFAULT NULL,
  `REQUIRED` char(1) NOT NULL DEFAULT 'N',
  `DEFAULT_VALUE` varchar(255) DEFAULT NULL,
  `OUTPUT_COMPONENT` char(1) DEFAULT 'N',
  `CLOUD_PART` int(2) DEFAULT NULL,
  `cloud_selection` int(1) DEFAULT '1',
  PRIMARY KEY (`ID`),
  KEY `FK_cloud_component` (`CLOUD_ID`),
  KEY `FK_cloud_component_BE` (`COMP_BE_ID`),
  KEY `fk_cloud_component_type_be_id` (`COMP_TYPE_BE_ID`)
) ENGINE=MyISAM AUTO_INCREMENT=139 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cloud_component`
--

LOCK TABLES `cloud_component` WRITE;
/*!40000 ALTER TABLE `cloud_component` DISABLE KEYS */;
INSERT INTO `cloud_component` VALUES (101,101,401,'100.00',1,401,1,'RI',NULL,'Y',NULL,'N',NULL,1);
INSERT INTO `cloud_component` VALUES (102,602,107,'10.00',1,107,1,'RTLI',NULL,'N',NULL,'N',2,1);
INSERT INTO `cloud_component` VALUES (103,101,106,'0.00',1,106,1,'RTLI',NULL,'N',NULL,'N',NULL,1);
INSERT INTO `cloud_component` VALUES (104,102,151,'80.00',1,151,1,'TLI',NULL,'Y',NULL,'N',1,1);
INSERT INTO `cloud_component` VALUES (106,102,105,'10.00',1,105,1,'RTLI',NULL,'N',NULL,'N',3,1);
INSERT INTO `cloud_component` VALUES (143,503,101,'50.00',1,101,1,'C',NULL,'Y',NULL,'N',NULL,1);
INSERT INTO `cloud_component` VALUES (109,104,401,'50.00',1,401,1,'RI',NULL,'Y',NULL,'N',NULL,1);
INSERT INTO `cloud_component` VALUES (110,104,203,'50.00',1,203,1,'T',NULL,'Y',NULL,'N',NULL,1);
INSERT INTO `cloud_component` VALUES (111,105,401,'90.00',1,401,1,'RI',NULL,'Y',NULL,'N',NULL,1);
INSERT INTO `cloud_component` VALUES (112,105,202,'10.00',1,202,1,'T',NULL,'N',NULL,'N',NULL,1);
INSERT INTO `cloud_component` VALUES (113,106,409,'25.00',1,409,1,'RTLI',NULL,'Y',NULL,'N',NULL,1);
INSERT INTO `cloud_component` VALUES (114,106,151,'50.00',2,151,1,'TLI',NULL,'Y',NULL,'N',NULL,1);
INSERT INTO `cloud_component` VALUES (115,106,408,'25.00',1,408,1,'RTLI',NULL,'Y',NULL,'N',NULL,1);
INSERT INTO `cloud_component` VALUES (116,107,409,'25.00',1,409,1,'RTLI',NULL,'Y',NULL,'N',NULL,1);
INSERT INTO `cloud_component` VALUES (117,107,201,'50.00',2,201,1,'RTLI',NULL,'Y',NULL,'N',NULL,1);
INSERT INTO `cloud_component` VALUES (118,107,408,'25.00',1,408,1,'RTLI',NULL,'Y',NULL,'N',NULL,1);
INSERT INTO `cloud_component` VALUES (119,108,404,'25.00',1,404,1,'RTLI',NULL,'Y',NULL,'N',NULL,1);
INSERT INTO `cloud_component` VALUES (120,108,151,'50.00',2,151,1,'TLI',NULL,'Y',NULL,'N',NULL,1);
INSERT INTO `cloud_component` VALUES (121,108,405,'25.00',1,405,1,'RTLI',NULL,'Y',NULL,'N',NULL,1);
INSERT INTO `cloud_component` VALUES (122,109,404,'25.00',1,404,1,'RTLI',NULL,'Y',NULL,'N',NULL,1);
INSERT INTO `cloud_component` VALUES (123,109,201,'50.00',2,201,1,'RTLI',NULL,'Y',NULL,'N',NULL,1);
INSERT INTO `cloud_component` VALUES (124,109,405,'25.00',1,405,1,'RTLI',NULL,'Y',NULL,'N',NULL,1);
INSERT INTO `cloud_component` VALUES (125,110,204,'85.00',1,204,1,'TLI',NULL,'Y',NULL,'N',3,1);
INSERT INTO `cloud_component` VALUES (126,110,202,'10.00',1,202,1,'TLI',NULL,'N',NULL,'N',2,1);
INSERT INTO `cloud_component` VALUES (127,111,203,'85.00',1,203,1,'TLI',NULL,'Y',NULL,'N',3,1);
INSERT INTO `cloud_component` VALUES (128,111,202,'10.00',1,202,1,'TLI',NULL,'N',NULL,'N',2,1);
INSERT INTO `cloud_component` VALUES (129,112,202,'95.00',1,202,1,'TLI',NULL,'Y',NULL,'N',2,1);
INSERT INTO `cloud_component` VALUES (130,113,403,'45.00',1,403,1,'RTLI',NULL,'Y',NULL,'N',NULL,1);
INSERT INTO `cloud_component` VALUES (131,113,401,'10.00',1,401,1,'RI',NULL,'N',NULL,'N',NULL,1);
INSERT INTO `cloud_component` VALUES (132,113,202,'45.00',1,202,1,'T',NULL,'N',NULL,'N',NULL,1);
INSERT INTO `cloud_component` VALUES (155,117,403,'45.00',1,403,1,'RTLI',NULL,'Y',NULL,'N',1,1);
INSERT INTO `cloud_component` VALUES (136,501,9006,'33.00',1,NULL,4,'C',NULL,'N',NULL,'N',3,1);
INSERT INTO `cloud_component` VALUES (137,501,101,'33.00',1,101,1,'C',NULL,'N',NULL,'N',2,1);
INSERT INTO `cloud_component` VALUES (138,501,110,'34.00',1,110,1,'RTLI',NULL,'Y',NULL,'N',1,1);
INSERT INTO `cloud_component` VALUES (139,502,9004,'50.00',1,NULL,4,'C',NULL,'Y',NULL,'N',NULL,1);
INSERT INTO `cloud_component` VALUES (140,502,406,'50.00',1,406,1,'RTLI',NULL,'Y',NULL,'N',NULL,1);
INSERT INTO `cloud_component` VALUES (141,114,401,'50.00',1,401,1,'RI',NULL,'N',NULL,'N',NULL,1);
INSERT INTO `cloud_component` VALUES (142,114,104,'50.00',1,104,1,'RTLI',NULL,'Y',NULL,'N',NULL,1);
INSERT INTO `cloud_component` VALUES (144,503,406,'50.00',1,406,1,'RTLI',NULL,'Y',NULL,'N',NULL,1);
INSERT INTO `cloud_component` VALUES (145,601,9003,'45.00',1,201,3,'C',NULL,'Y',NULL,'Y',NULL,1);
INSERT INTO `cloud_component` VALUES (146,601,401,'10.00',1,401,1,'RI',NULL,'N',NULL,'N',NULL,1);
INSERT INTO `cloud_component` VALUES (147,601,403,'45.00',1,403,1,'RTLI',NULL,'Y',NULL,'N',NULL,1);
INSERT INTO `cloud_component` VALUES (148,115,151,'50.00',1,151,1,'TLI',NULL,'Y',NULL,'N',NULL,1);
INSERT INTO `cloud_component` VALUES (149,115,410,'50.00',1,410,1,'RTLI',NULL,'Y',NULL,'N',NULL,1);
INSERT INTO `cloud_component` VALUES (154,112,413,'5.00',1,413,1,'RTLI',NULL,'N',NULL,'N',1,1);
INSERT INTO `cloud_component` VALUES (156,117,401,'10.00',1,401,1,'RI',NULL,'N',NULL,'N',1,1);
INSERT INTO `cloud_component` VALUES (158,117,204,'45.00',1,204,1,'T',NULL,'Y',NULL,'N',3,1);
INSERT INTO `cloud_component` VALUES (159,118,403,'45.00',1,403,1,'RTLI',NULL,'Y',NULL,'N',1,1);
INSERT INTO `cloud_component` VALUES (160,118,401,'10.00',1,401,1,'RI',NULL,'N',NULL,'N',1,1);
INSERT INTO `cloud_component` VALUES (161,118,203,'45.00',1,203,1,'T',NULL,'Y',NULL,'N',3,1);
INSERT INTO `cloud_component` VALUES (150,116,206,'70.00',1,206,1,'TLI',NULL,'Y',NULL,'N',4,1);
INSERT INTO `cloud_component` VALUES (151,116,204,'15.00',1,204,1,'TLI',NULL,'N',NULL,'N',3,1);
INSERT INTO `cloud_component` VALUES (152,116,202,'10.00',1,202,1,'TLI',NULL,'N',NULL,'N',2,1);
INSERT INTO `cloud_component` VALUES (162,119,403,'45.00',1,403,1,'RTLI',NULL,'Y',NULL,'N',1,1);
INSERT INTO `cloud_component` VALUES (163,119,401,'10.00',1,401,1,'RI',NULL,'N',NULL,'N',2,1);
INSERT INTO `cloud_component` VALUES (164,119,206,'45.00',1,206,1,'T',NULL,'Y',NULL,'N',3,1);
INSERT INTO `cloud_component` VALUES (165,110,413,'5.00',1,413,1,'RTLI',NULL,'N',NULL,'N',1,1);
INSERT INTO `cloud_component` VALUES (166,111,413,'5.00',1,413,1,'RTLI',NULL,'N',NULL,'N',1,1);
INSERT INTO `cloud_component` VALUES (167,116,413,'5.00',1,413,1,'RTLI',NULL,'N',NULL,'N',1,1);
INSERT INTO `cloud_component` VALUES (185,606,408,'10.00',1,408,1,'RTLI',NULL,'N',NULL,'N',NULL,1);
INSERT INTO `cloud_component` VALUES (184,606,153,'80.00',1,153,1,'CLI',NULL,'Y',NULL,'N',NULL,1);
INSERT INTO `cloud_component` VALUES (183,606,409,'10.00',1,409,1,'RTLI',NULL,'Y',NULL,'N',NULL,1);
INSERT INTO `cloud_component` VALUES (172,602,151,'80.00',1,151,1,'TLI',NULL,'Y',NULL,'N',1,1);
INSERT INTO `cloud_component` VALUES (173,602,105,'10.00',1,105,1,'RTLI',NULL,'N',NULL,'N',3,1);
INSERT INTO `cloud_component` VALUES (174,603,107,'10.00',1,107,1,'RTLI',NULL,'Y',NULL,'N',NULL,1);
INSERT INTO `cloud_component` VALUES (175,603,151,'80.00',1,151,1,'TLI',NULL,'Y',NULL,'N',NULL,1);
INSERT INTO `cloud_component` VALUES (176,603,105,'10.00',1,105,1,'RTLI',NULL,'N',NULL,'N',NULL,1);
INSERT INTO `cloud_component` VALUES (177,604,107,'10.00',1,107,1,'RTLI',NULL,'Y',NULL,'N',NULL,1);
INSERT INTO `cloud_component` VALUES (178,604,151,'80.00',1,151,1,'TLI',NULL,'Y',NULL,'N',NULL,1);
INSERT INTO `cloud_component` VALUES (179,604,105,'10.00',1,105,1,'RTLI',NULL,'N',NULL,'N',NULL,1);
INSERT INTO `cloud_component` VALUES (180,605,107,'10.00',1,107,1,'RTLI',NULL,'Y',NULL,'N',NULL,1);
INSERT INTO `cloud_component` VALUES (181,605,151,'80.00',1,151,1,'TLI',NULL,'Y',NULL,'N',NULL,1);
INSERT INTO `cloud_component` VALUES (182,605,105,'10.00',1,105,1,'RTLI',NULL,'N',NULL,'N',NULL,1);
INSERT INTO `cloud_component` VALUES (186,202,211,'40.00',1,211,1,'CLI',NULL,'Y',NULL,'N',1,1);
INSERT INTO `cloud_component` VALUES (187,202,206,'20.00',1,206,1,'TLI',NULL,'N',NULL,'N',2,1);
INSERT INTO `cloud_component` VALUES (188,202,204,'15.00',1,204,1,'TLI',NULL,'N',NULL,'N',3,1);
INSERT INTO `cloud_component` VALUES (189,202,202,'10.00',1,202,1,'TLI',NULL,'N',NULL,'N',4,1);
INSERT INTO `cloud_component` VALUES (190,202,412,'10.00',1,412,1,'RTLI',NULL,'N',NULL,'N',5,1);
INSERT INTO `cloud_component` VALUES (191,202,413,'5.00',1,413,1,'RTLI',NULL,'N',NULL,'N',6,1);
INSERT INTO `cloud_component` VALUES (192,607,153,'50.00',1,153,1,'CLI',NULL,'Y',NULL,'N',NULL,1);
INSERT INTO `cloud_component` VALUES (193,607,410,'50.00',1,410,1,'RTLI',NULL,'Y',NULL,'N',NULL,1);
INSERT INTO `cloud_component` VALUES (194,203,205,'50.00',1,205,1,'T',NULL,'Y',NULL,'N',NULL,1);
INSERT INTO `cloud_component` VALUES (195,203,401,'50.00',1,401,1,'RTLI',NULL,'Y',NULL,'N',NULL,1);
INSERT INTO `cloud_component` VALUES (197,204,205,'30.00',1,205,1,'TLI',NULL,'Y',NULL,'N',NULL,1);
INSERT INTO `cloud_component` VALUES (198,204,204,'20.00',1,204,1,'TLI',NULL,'N',NULL,'N',NULL,1);
INSERT INTO `cloud_component` VALUES (199,204,202,'20.00',1,202,1,'TLI',NULL,'N',NULL,'N',NULL,1);
INSERT INTO `cloud_component` VALUES (105,102,107,'10.00',1,107,1,'RTLI',NULL,'N',NULL,'N',2,1);
INSERT INTO `cloud_component` VALUES (236,111,105,'5.00',1,105,1,'RTLI',NULL,'N',NULL,'N',1,1);
INSERT INTO `cloud_component` VALUES (235,110,105,'5.00',1,105,1,'RTLI',NULL,'N',NULL,'N',1,1);
INSERT INTO `cloud_component` VALUES (234,112,105,'5.00',1,105,1,'RTLI',NULL,'N',NULL,'N',1,1);
INSERT INTO `cloud_component` VALUES (206,121,207,'45.00',1,207,1,'T',NULL,'N',NULL,'N',1,1);
INSERT INTO `cloud_component` VALUES (207,121,208,'45.00',1,208,1,'T',NULL,'N',NULL,'N',1,1);
INSERT INTO `cloud_component` VALUES (208,121,209,'45.00',1,209,1,'T',NULL,'N',NULL,'N',1,1);
INSERT INTO `cloud_component` VALUES (209,121,401,'10.00',1,401,1,'RI',NULL,'N',NULL,'N',2,1);
INSERT INTO `cloud_component` VALUES (210,121,403,'45.00',1,403,1,'RTLI',NULL,'Y',NULL,'N',3,1);
INSERT INTO `cloud_component` VALUES (211,608,405,'10.00',1,405,1,'RTLI',NULL,'Y',NULL,'N',NULL,1);
INSERT INTO `cloud_component` VALUES (212,608,153,'80.00',1,153,1,'CLI',NULL,'Y',NULL,'N',NULL,1);
INSERT INTO `cloud_component` VALUES (213,608,404,'10.00',1,404,1,'RTLI',NULL,'N',NULL,'N',NULL,1);
INSERT INTO `cloud_component` VALUES (214,609,107,'10.00',1,107,1,'RTLI',NULL,'Y',NULL,'N',NULL,1);
INSERT INTO `cloud_component` VALUES (215,609,151,'80.00',1,151,1,'TLI',NULL,'Y',NULL,'N',NULL,1);
INSERT INTO `cloud_component` VALUES (216,609,105,'10.00',1,105,1,'RTLI',NULL,'N',NULL,'N',NULL,1);
INSERT INTO `cloud_component` VALUES (217,610,204,'10.00',1,204,1,'T',NULL,'N',NULL,'N',1,1);
INSERT INTO `cloud_component` VALUES (218,610,202,'10.00',1,202,1,'T',NULL,'N',NULL,'N',1,1);
INSERT INTO `cloud_component` VALUES (219,610,206,'10.00',1,206,1,'T',NULL,'N',NULL,'N',1,1);
INSERT INTO `cloud_component` VALUES (220,610,151,'80.00',1,151,1,'TLI',NULL,'Y',NULL,'N',2,1);
INSERT INTO `cloud_component` VALUES (221,610,105,'10.00',1,105,1,'RTLI',NULL,'N',NULL,'N',3,1);
INSERT INTO `cloud_component` VALUES (222,122,403,'45.00',1,403,1,'RTLI',NULL,'Y',NULL,'N',1,1);
INSERT INTO `cloud_component` VALUES (223,122,401,'10.00',1,401,1,'RI',NULL,'N',NULL,'N',2,1);
INSERT INTO `cloud_component` VALUES (224,122,205,'45.00',1,205,1,'T',NULL,'Y',NULL,'N',3,1);
INSERT INTO `cloud_component` VALUES (225,123,403,'10.00',1,403,1,'RTLI',NULL,'Y',NULL,'N',1,1);
INSERT INTO `cloud_component` VALUES (226,123,401,'10.00',1,401,1,'RI',NULL,'N',NULL,'N',2,1);
INSERT INTO `cloud_component` VALUES (227,123,213,'80.00',1,205,1,'TLI',NULL,'Y',NULL,'N',3,1);
INSERT INTO `cloud_component` VALUES (228,124,413,'1.00',1,413,1,'RTLI',NULL,'N',NULL,'N',1,1);
INSERT INTO `cloud_component` VALUES (229,124,213,'90.00',1,213,1,'TLI',NULL,'Y',NULL,'N',2,1);
INSERT INTO `cloud_component` VALUES (230,124,205,'1.00',1,205,1,'RTLI',NULL,'N',NULL,'N',3,1);
INSERT INTO `cloud_component` VALUES (231,124,204,'4.00',1,204,1,'TLI',NULL,'N',NULL,'N',4,1);
INSERT INTO `cloud_component` VALUES (232,124,202,'4.00',1,202,1,'RTLI',NULL,'N',NULL,'N',5,1);
INSERT INTO `cloud_component` VALUES (233,202,213,'20.00',1,213,1,'TLI',NULL,'N',NULL,'N',2,1);
INSERT INTO `cloud_component` VALUES (237,116,105,'5.00',1,105,1,'RTLI',NULL,'N',NULL,'N',1,1);
INSERT INTO `cloud_component` VALUES (238,202,105,'5.00',1,105,1,'RTLI',NULL,'N',NULL,'N',6,1);
INSERT INTO `cloud_component` VALUES (239,611,9003,'45.00',1,201,3,'C',NULL,'Y',NULL,'Y',1,1);
INSERT INTO `cloud_component` VALUES (240,611,202,'10.00',1,202,1,'TLI',NULL,'Y',NULL,'N',2,1);
INSERT INTO `cloud_component` VALUES (241,611,105,'45.00',1,105,1,'RTLI',NULL,'Y',NULL,'N',3,1);
INSERT INTO `cloud_component` VALUES (242,612,304,'100.00',1,304,1,'TLI',NULL,'N',NULL,'N',NULL,1);
INSERT INTO `cloud_component` VALUES (243,612,303,'100.00',1,303,1,'TLI',NULL,'N',NULL,'N',NULL,1);
INSERT INTO `cloud_component` VALUES (244,612,302,'100.00',1,302,1,'TLI',NULL,'N',NULL,'N',NULL,1);
INSERT INTO `cloud_component` VALUES (245,612,305,'100.00',1,305,1,'TLI',NULL,'N',NULL,'N',NULL,1);
INSERT INTO `cloud_component` VALUES (246,610,205,'10.00',1,205,1,'T',NULL,'N',NULL,'N',1,1);
INSERT INTO `cloud_component` VALUES (247,613,107,'10.00',1,107,1,'RTLI',NULL,'Y',NULL,'N',NULL,1);
INSERT INTO `cloud_component` VALUES (248,613,151,'80.00',1,151,1,'RTLI',NULL,'Y',NULL,'N',NULL,1);
INSERT INTO `cloud_component` VALUES (249,613,105,'10.00',1,105,1,'RTLI',NULL,'N',NULL,'N',NULL,1);
INSERT INTO `cloud_component` VALUES (250,614,107,'10.00',1,107,1,'RTLI',NULL,'Y',NULL,'N',NULL,1);
INSERT INTO `cloud_component` VALUES (251,614,151,'80.00',1,151,1,'RTLI',NULL,'Y',NULL,'N',NULL,1);
INSERT INTO `cloud_component` VALUES (252,614,105,'10.00',1,105,1,'RTLI',NULL,'N',NULL,'N',NULL,1);
INSERT INTO `cloud_component` VALUES (253,615,107,'10.00',1,107,1,'RTLI',NULL,'Y',NULL,'N',NULL,1);
INSERT INTO `cloud_component` VALUES (254,615,151,'80.00',1,151,1,'RTLI',NULL,'Y',NULL,'N',NULL,1);
INSERT INTO `cloud_component` VALUES (255,615,105,'10.00',1,105,1,'RTLI',NULL,'N',NULL,'N',NULL,1);
INSERT INTO `cloud_component` VALUES (256,616,151,'80.00',1,151,1,'RTLI',NULL,'Y',NULL,'N',NULL,1);
INSERT INTO `cloud_component` VALUES (257,616,105,'20.00',1,105,1,'RTLI',NULL,'N',NULL,'N',NULL,1);
/*!40000 ALTER TABLE `cloud_component` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cloud_rule`
--

DROP TABLE IF EXISTS `cloud_rule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cloud_rule` (
  `CLOUD_ID` bigint(20) NOT NULL,
  `RULE_ID` bigint(20) NOT NULL,
  KEY `FK_cloud_ID` (`CLOUD_ID`),
  KEY `FK_CLOUD_RULE_ID` (`RULE_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cloud_rule`
--

LOCK TABLES `cloud_rule` WRITE;
/*!40000 ALTER TABLE `cloud_rule` DISABLE KEYS */;
INSERT INTO `cloud_rule` VALUES (115,108);
INSERT INTO `cloud_rule` VALUES (110,2);
INSERT INTO `cloud_rule` VALUES (110,3);
INSERT INTO `cloud_rule` VALUES (104,11);
INSERT INTO `cloud_rule` VALUES (111,12);
INSERT INTO `cloud_rule` VALUES (111,13);
INSERT INTO `cloud_rule` VALUES (111,14);
INSERT INTO `cloud_rule` VALUES (105,21);
INSERT INTO `cloud_rule` VALUES (105,22);
INSERT INTO `cloud_rule` VALUES (101,101);
INSERT INTO `cloud_rule` VALUES (102,102);
INSERT INTO `cloud_rule` VALUES (112,25);
INSERT INTO `cloud_rule` VALUES (106,104);
INSERT INTO `cloud_rule` VALUES (107,105);
INSERT INTO `cloud_rule` VALUES (108,106);
INSERT INTO `cloud_rule` VALUES (109,107);
INSERT INTO `cloud_rule` VALUES (606,114);
INSERT INTO `cloud_rule` VALUES (609,117);
INSERT INTO `cloud_rule` VALUES (121,126);
INSERT INTO `cloud_rule` VALUES (117,4);
INSERT INTO `cloud_rule` VALUES (118,15);
INSERT INTO `cloud_rule` VALUES (119,31);
INSERT INTO `cloud_rule` VALUES (611,26);
INSERT INTO `cloud_rule` VALUES (602,110);
INSERT INTO `cloud_rule` VALUES (603,111);
INSERT INTO `cloud_rule` VALUES (604,112);
INSERT INTO `cloud_rule` VALUES (605,113);
INSERT INTO `cloud_rule` VALUES (607,115);
INSERT INTO `cloud_rule` VALUES (116,32);
INSERT INTO `cloud_rule` VALUES (116,33);
INSERT INTO `cloud_rule` VALUES (102,116);
INSERT INTO `cloud_rule` VALUES (602,116);
INSERT INTO `cloud_rule` VALUES (610,127);
INSERT INTO `cloud_rule` VALUES (112,24);
INSERT INTO `cloud_rule` VALUES (110,5);
INSERT INTO `cloud_rule` VALUES (116,34);
INSERT INTO `cloud_rule` VALUES (613,128);
INSERT INTO `cloud_rule` VALUES (614,129);
INSERT INTO `cloud_rule` VALUES (615,130);
INSERT INTO `cloud_rule` VALUES (616,118);
/*!40000 ALTER TABLE `cloud_rule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `colum`
--

DROP TABLE IF EXISTS `colum`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `colum` (
  `ID` bigint(20) NOT NULL,
  `NAME` varchar(255) NOT NULL,
  `DISPLAY_NAME` varchar(255) NOT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `data_type` varchar(35) NOT NULL,
  `precison` int(6) NOT NULL,
  `SCALE` int(6) DEFAULT NULL,
  `KDX_DATA_TYPE` varchar(35) DEFAULT NULL,
  `REQUIRED` char(1) DEFAULT NULL,
  `DEFAULT_VALUE` varchar(255) DEFAULT NULL,
  `IS_CONSTRAINT_COLUM` char(1) DEFAULT NULL,
  `DATA_FORMAT` varchar(35) DEFAULT NULL,
  `UNIT` varchar(35) DEFAULT NULL,
  `UNIT_TYPE` varchar(35) DEFAULT NULL,
  `IS_PRIMARY_KEY` char(1) DEFAULT NULL,
  `IS_FOREIGN_KEY` char(1) DEFAULT NULL,
  `GRANULARITY` varchar(25) DEFAULT NULL,
  `original_file_date_format` varchar(35) DEFAULT NULL,
  `INDICATORS` char(1) DEFAULT 'N',
  PRIMARY KEY (`ID`),
  KEY `IDX_CL_DT` (`data_type`),
  KEY `IDX_CL_KDT` (`KDX_DATA_TYPE`),
  KEY `IDX_CL_NM` (`NAME`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `colum`
--

LOCK TABLES `colum` WRITE;
/*!40000 ALTER TABLE `colum` DISABLE KEYS */;
/*!40000 ALTER TABLE `colum` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `colum_constrain`
--

DROP TABLE IF EXISTS `colum_constrain`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `colum_constrain` (
  `COLUM_ID` bigint(20) NOT NULL,
  `CONSTRAIN_ID` bigint(20) NOT NULL,
  KEY `FK_CC_ID` (`COLUM_ID`),
  KEY `FK_CCN_ID` (`CONSTRAIN_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `colum_constrain`
--

LOCK TABLES `colum_constrain` WRITE;
/*!40000 ALTER TABLE `colum_constrain` DISABLE KEYS */;
/*!40000 ALTER TABLE `colum_constrain` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `concept`
--

DROP TABLE IF EXISTS `concept`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `concept` (
  `ID` bigint(20) NOT NULL,
  `NAME` varchar(255) NOT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `DISPLAY_NAME` varchar(255) NOT NULL,
  `DEFAULT_UNIT` varchar(35) DEFAULT NULL,
  `DEFAULT_DATA_FORMAT` varchar(35) DEFAULT NULL,
  `DEFAULT_CONVERSION_TYPE` varchar(35) DEFAULT NULL,
  `SAMPLE_VALUES` varchar(255) DEFAULT NULL,
  `data_sampling_strategy` char(1) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `NAME_IDX` (`NAME`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `concept`
--

LOCK TABLES `concept` WRITE;
/*!40000 ALTER TABLE `concept` DISABLE KEYS */;
INSERT INTO `concept` VALUES (101,'Digit',NULL,'Digit',NULL,NULL,'NULL',NULL,NULL);
INSERT INTO `concept` VALUES (102,'Currency','Currency','Currency',NULL,NULL,'NULL',NULL,NULL);
INSERT INTO `concept` VALUES (103,'UnitScale',NULL,'UnitScale',NULL,NULL,'NULL',NULL,NULL);
INSERT INTO `concept` VALUES (104,'Statistics',NULL,'Statistics',NULL,NULL,'NULL',NULL,NULL);
INSERT INTO `concept` VALUES (105,'Operator',NULL,'Operator',NULL,NULL,'NULL',NULL,NULL);
INSERT INTO `concept` VALUES (106,'Conjunction','Conjunction','Conjunction',NULL,NULL,'NULL',NULL,NULL);
INSERT INTO `concept` VALUES (107,'ByConjunction','By','By',NULL,NULL,'NULL',NULL,NULL);
INSERT INTO `concept` VALUES (108,'CoordinatingConjunction','Coordinating Conjunction','Coordinating Conjunction',NULL,NULL,'NULL',NULL,NULL);
INSERT INTO `concept` VALUES (109,'Preposition','Preposition','Preposition',NULL,NULL,'NULL',NULL,NULL);
INSERT INTO `concept` VALUES (110,'Adjective','Adjective','Adjective',NULL,NULL,'NULL',NULL,NULL);
INSERT INTO `concept` VALUES (111,'Weight','Weight','Weight',NULL,NULL,'NULL',NULL,NULL);
INSERT INTO `concept` VALUES (112,'Distance','Distance','Distance',NULL,NULL,'NULL',NULL,NULL);
INSERT INTO `concept` VALUES (113,'RangePreposition','RangePreposition','RangePreposition',NULL,NULL,'NULL',NULL,NULL);
INSERT INTO `concept` VALUES (114,'ScalingFactor','Scaling Factor','Scaling Factor',NULL,NULL,'NULL',NULL,NULL);
INSERT INTO `concept` VALUES (127,'Temperature','Temperature','Temperature',NULL,NULL,'NULL',NULL,NULL);
INSERT INTO `concept` VALUES (116,'ComparativeStatistics','Comparative Statistics','Comparative Statistics',NULL,NULL,'NULL',NULL,NULL);
INSERT INTO `concept` VALUES (117,'ComparativeInformation','Comparative Information','Comparative Information',NULL,NULL,'NULL',NULL,NULL);
INSERT INTO `concept` VALUES (118,'ValuePreposition','Value Preposition','ValuePreposition',NULL,NULL,'NULL',NULL,NULL);
INSERT INTO `concept` VALUES (119,'Power','Power','Power',NULL,NULL,'NULL',NULL,NULL);
INSERT INTO `concept` VALUES (120,'Volume','Volume','Volume',NULL,NULL,'NULL',NULL,NULL);
INSERT INTO `concept` VALUES (121,'CurrencySymbol','CurrencySymbol','CurrencySymbol',NULL,NULL,'NULL',NULL,NULL);
INSERT INTO `concept` VALUES (122,'DistanceSymbol','DistanceSymbol','DistanceSymbol',NULL,NULL,'NULL',NULL,NULL);
INSERT INTO `concept` VALUES (123,'WeightSymbol','WeightSymbol','WeightSymbol',NULL,NULL,'NULL',NULL,NULL);
INSERT INTO `concept` VALUES (124,'VolumeSymbol','VolumeSymbol','VolumeSymbol',NULL,NULL,'NULL',NULL,NULL);
INSERT INTO `concept` VALUES (125,'PowerSymbol','PowerSymbol','PowerSymbol',NULL,NULL,'NULL',NULL,NULL);
INSERT INTO `concept` VALUES (126,'Number','Number','Number',NULL,NULL,'NULL',NULL,NULL);
INSERT INTO `concept` VALUES (201,'Time','Time','Time',NULL,NULL,'NULL',NULL,NULL);
INSERT INTO `concept` VALUES (202,'TimeQualifier','TimeQualifier','TimeQualifier',NULL,NULL,'NULL',NULL,NULL);
INSERT INTO `concept` VALUES (128,'Percentage','Percentage','Percentage',NULL,NULL,'NULL',NULL,NULL);
INSERT INTO `concept` VALUES (129,'TimeDuration',NULL,'Time Duration',NULL,NULL,'NULL',NULL,NULL);
INSERT INTO `concept` VALUES (130,'TimePreposition',NULL,'Time Preposition',NULL,NULL,'NULL',NULL,NULL);
INSERT INTO `concept` VALUES (203,'Area','Area','Area',NULL,NULL,'NULL',NULL,NULL);
INSERT INTO `concept` VALUES (204,'AreaSymbol','AreaSymbol','AreaSymbol',NULL,NULL,'NULL',NULL,NULL);
INSERT INTO `concept` VALUES (205,'Memory','Memory','Memory',NULL,NULL,'NULL',NULL,NULL);
INSERT INTO `concept` VALUES (206,'MemorySymbol','MemorySymbol','MemorySymbol',NULL,NULL,'NULL',NULL,NULL);
INSERT INTO `concept` VALUES (207,'Resolution','Resolution','Resolution',NULL,NULL,'NULL',NULL,NULL);
INSERT INTO `concept` VALUES (208,'ResolutionSymbol','ResolutionSymbol','ResolutionSymbol',NULL,NULL,'NULL',NULL,NULL);
/*!40000 ALTER TABLE `concept` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `concept_profile`
--

DROP TABLE IF EXISTS `concept_profile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `concept_profile` (
  `ID` bigint(20) NOT NULL,
  `NAME` varchar(255) NOT NULL,
  `DISPLAY_NAME` varchar(255) NOT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `ENABLED` char(1) NOT NULL DEFAULT '1',
  `USER_ID` bigint(20) NOT NULL,
  `MODEL_GROUP_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_CP` (`NAME`,`MODEL_GROUP_ID`),
  KEY `FK_CP_USRID` (`USER_ID`),
  KEY `FK_MGID_CONCEPT_PROFILE` (`MODEL_GROUP_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `concept_profile`
--

LOCK TABLES `concept_profile` WRITE;
/*!40000 ALTER TABLE `concept_profile` DISABLE KEYS */;
/*!40000 ALTER TABLE `concept_profile` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `concept_profile_detail`
--

DROP TABLE IF EXISTS `concept_profile_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `concept_profile_detail` (
  `PROFILE_ID` bigint(20) NOT NULL,
  `CONCEPT_ID` bigint(20) DEFAULT NULL,
  KEY `FK_CPD_CPID` (`PROFILE_ID`),
  KEY `FK_CPD_CID` (`CONCEPT_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `concept_profile_detail`
--

LOCK TABLES `concept_profile_detail` WRITE;
/*!40000 ALTER TABLE `concept_profile_detail` DISABLE KEYS */;
/*!40000 ALTER TABLE `concept_profile_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `concept_stat`
--

DROP TABLE IF EXISTS `concept_stat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `concept_stat` (
  `CONCEPT_ID` bigint(20) NOT NULL,
  `STAT_ID` bigint(20) NOT NULL,
  KEY `FK_CS_SID` (`STAT_ID`),
  KEY `FK_CS_CID` (`CONCEPT_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `concept_stat`
--

LOCK TABLES `concept_stat` WRITE;
/*!40000 ALTER TABLE `concept_stat` DISABLE KEYS */;
/*!40000 ALTER TABLE `concept_stat` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `configuration`
--

DROP TABLE IF EXISTS `configuration`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `configuration` (
  `CONFIG_NAME` varchar(50) NOT NULL,
  `CONFIG_KEY` varchar(255) NOT NULL,
  `CONFIG_VALUE` varchar(100) DEFAULT NULL,
  `NOTES` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`CONFIG_NAME`,`CONFIG_KEY`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `configuration`
--

LOCK TABLES `configuration` WRITE;
/*!40000 ALTER TABLE `configuration` DISABLE KEYS */;
INSERT INTO `configuration` VALUES ('QueryCache','qcache.flags.checkMatch','false',NULL);
INSERT INTO `configuration` VALUES ('QueryInterface','qi.suggest.maxResults','10',NULL);
INSERT INTO `configuration` VALUES ('QueryCache','qcache.flags.matchResultWeight','90',NULL);
INSERT INTO `configuration` VALUES ('QueryCache','qcache.flags.relatedQueryThresholdWeight','70',NULL);
INSERT INTO `configuration` VALUES ('QueryCache','qcache.flags.relatedQueryMaxLimit','10',NULL);
INSERT INTO `configuration` VALUES ('QueryCache','semantic.driver.flags.verticalBasedSorting','false',NULL);
INSERT INTO `configuration` VALUES ('QueryCache','semantic.driver.flags.handle.propcessPossibilities','true',NULL);
INSERT INTO `configuration` VALUES ('QueryCache','querygen.flags.handleTimeFrameClassCastException','true',NULL);
INSERT INTO `configuration` VALUES ('Driver','driver.flags.governor-query-representation-required','false',NULL);
INSERT INTO `configuration` VALUES ('Driver','driver.display-query-string','true',NULL);
INSERT INTO `configuration` VALUES ('Core','core.static-values.system-level-default-stat','AVG',NULL);
INSERT INTO `configuration` VALUES ('AnswersCatalog','ans-catalog.mart.sampling-algo.error-rate-percentage','2.5',NULL);
INSERT INTO `configuration` VALUES ('AnswersCatalog','ans-catalog.mart.sampling-algo.confidence-level-percentage','97',NULL);
INSERT INTO `configuration` VALUES ('AnswersCatalog','ans-catalog.mart.sampling-algo.min-sample-percentage-of-population','10',NULL);
INSERT INTO `configuration` VALUES ('AnswersCatalog','ans-catalog.mart.sampling-algo.max-sample-percentage-of-population-allowed','50',NULL);
INSERT INTO `configuration` VALUES ('AnswersCatalog','ans-catalog.mart.static-values.max-eligible-dimensions','5',NULL);
INSERT INTO `configuration` VALUES ('AnswersCatalog','ans-catalog.mart.static-values.max-eligible-measures','10',NULL);
INSERT INTO `configuration` VALUES ('AnswersCatalog','ans-catalog.mart.flags.target-data-source-same-as-source-data-source','false',NULL);
INSERT INTO `configuration` VALUES ('AnswersCatalog','ans-catalog.mart.flags.use-basic-sampling-algo','false',NULL);
INSERT INTO `configuration` VALUES ('AnswersCatalog','ans-catalog.cube.flags.target-data-source-same-as-source-data-source','false',NULL);
INSERT INTO `configuration` VALUES ('AnswersCatalog','ans-catalog.optimaldset-algo.cube.min-usage-percentage','80',NULL);
INSERT INTO `configuration` VALUES ('AnswersCatalog','ans-catalog.optimaldset-algo.cube.max-space-percentage','10',NULL);
INSERT INTO `configuration` VALUES ('AnswersCatalog','ans-catalog.optimaldset-algo.apply-contraints','true','If this flag is false, space and usage constraints are not be applied');
INSERT INTO `configuration` VALUES ('AnswersCatalog','ans-catalog.optimaldset-algo.deduce-space-at-runtime','true','If this flag to true, other flag values are used to calculate space than from source SWI');
INSERT INTO `configuration` VALUES ('AnswersCatalog','ans-catalog.optimaldset-algo.configured-parent-asset-space','100000','If deduce-space-at-runtime flag is false, this value is used to calculate the space');
INSERT INTO `configuration` VALUES ('AnswersCatalog','ans-catalog.optimaldset-algo.configured-number-of-measures','15','If deduce-space-at-runtime flag is false, this value is used to calculate the space');
INSERT INTO `configuration` VALUES ('AnswersCatalog','ans-catalog.optimaldset-algo.configured-dimension-lookup-value-column-length','8','If deduce-space-at-runtime flag is false, this value is used to calculate the space');
INSERT INTO `configuration` VALUES ('Aggregation','aggregation.static-values.enable-dynamic-ranges','true',NULL);
INSERT INTO `configuration` VALUES ('Aggregation','aggregation.static-values.skip-univariants','false',NULL);
INSERT INTO `configuration` VALUES ('Aggregation','aggregation.static-values.dynamic-ranges-band-count','5',NULL);
INSERT INTO `configuration` VALUES ('Aggregation','aggregation.static-values.data-browser-max-records','500',NULL);
INSERT INTO `configuration` VALUES ('Aggregation','aggregation.static-values.data-browser-min-records','1',NULL);
INSERT INTO `configuration` VALUES ('Aggregation','aggregation.static-values.detail-reports-selection-threshold','600',NULL);
INSERT INTO `configuration` VALUES ('Aggregation','aggregation.static-values.enable-detail-reports','true',NULL);
INSERT INTO `configuration` VALUES ('Aggregation','aggregation.static-values.report-title-length','50',NULL);
INSERT INTO `configuration` VALUES ('Presentation','reports.configuration.axis-label-max-length','20',NULL);
/*!40000 ALTER TABLE `configuration` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `constrain`
--

DROP TABLE IF EXISTS `constrain`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `constrain` (
  `ID` bigint(20) NOT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `TYPE` varchar(35) NOT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `REF_TABLE_ID` bigint(20) DEFAULT NULL,
  `REF_COLUMN_ID` bigint(20) DEFAULT NULL,
  `COLUMN_ORDER` int(3) NOT NULL DEFAULT '0',
  `CONSTRAINT_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_CST_TID` (`REF_TABLE_ID`),
  KEY `FK_CST_CID` (`REF_COLUMN_ID`),
  KEY `IDX_CNST_CID` (`CONSTRAINT_ID`),
  KEY `IDX_CNST_TY` (`TYPE`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `constrain`
--

LOCK TABLES `constrain` WRITE;
/*!40000 ALTER TABLE `constrain` DISABLE KEYS */;
/*!40000 ALTER TABLE `constrain` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `content_cleanup_pattern`
--

DROP TABLE IF EXISTS `content_cleanup_pattern`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `content_cleanup_pattern` (
  `ID` bigint(20) NOT NULL,
  `APP_ID` bigint(20) NOT NULL,
  `LOOKUP_PATTERN` varchar(255) NOT NULL,
  `REPLACE_PATTERN` varchar(255) DEFAULT NULL,
  `ACTIVE` char(1) NOT NULL DEFAULT 'Y',
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `content_cleanup_pattern`
--

LOCK TABLES `content_cleanup_pattern` WRITE;
/*!40000 ALTER TABLE `content_cleanup_pattern` DISABLE KEYS */;
INSERT INTO `content_cleanup_pattern` VALUES (1,-1,'\\\\%',' Percentage ','Y');
INSERT INTO `content_cleanup_pattern` VALUES (2,-1,',','','Y');
INSERT INTO `content_cleanup_pattern` VALUES (3,-1,'\'','','Y');
INSERT INTO `content_cleanup_pattern` VALUES (4,-1,'!','','Y');
INSERT INTO `content_cleanup_pattern` VALUES (5,-1,'\\\"','','Y');
INSERT INTO `content_cleanup_pattern` VALUES (6,-1,'Inc\\\\.','','Y');
INSERT INTO `content_cleanup_pattern` VALUES (7,-1,'Co\\\\.','','Y');
INSERT INTO `content_cleanup_pattern` VALUES (8,-1,'Ltd\\\\.','','Y');
INSERT INTO `content_cleanup_pattern` VALUES (9,-1,'\\\\.com','','Y');
INSERT INTO `content_cleanup_pattern` VALUES (10,-1,'&','','Y');
INSERT INTO `content_cleanup_pattern` VALUES (11,-1,'\\\\?','','Y');
/*!40000 ALTER TABLE `content_cleanup_pattern` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `conversion_formula`
--

DROP TABLE IF EXISTS `conversion_formula`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `conversion_formula` (
  `ID` bigint(20) NOT NULL,
  `TYPE` varchar(35) DEFAULT NULL,
  `FORMULA` varchar(255) DEFAULT NULL,
  `SOURCE` varchar(35) DEFAULT NULL,
  `TARGET` varchar(35) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `IDX_CF_SRC` (`SOURCE`),
  KEY `IDX_CF_TRGT` (`TARGET`),
  KEY `IDX_CF_TY` (`TYPE`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `conversion_formula`
--

LOCK TABLES `conversion_formula` WRITE;
/*!40000 ALTER TABLE `conversion_formula` DISABLE KEYS */;
INSERT INTO `conversion_formula` VALUES (1,'NUMBER','1.0*?','ONE','ONE');
INSERT INTO `conversion_formula` VALUES (2,'NUMBER','?/(1000.0*1000.0)','ONE','MILLION');
INSERT INTO `conversion_formula` VALUES (3,'NUMBER','?/(1000.0*1000.0*1000.0)','ONE','BILLION');
INSERT INTO `conversion_formula` VALUES (4,'NUMBER','?/(1000.0*1000.0*1000.0*1000.0)','ONE','TRILLION');
INSERT INTO `conversion_formula` VALUES (5,'NUMBER','1000.0*1000.0*?','MILLION','ONE');
INSERT INTO `conversion_formula` VALUES (6,'NUMBER','1000.0*1000.0*1000.0*?','BILLION','ONE');
INSERT INTO `conversion_formula` VALUES (7,'NUMBER','1000.0*1000.0*1000.0*1000.0*?','TRILLION','ONE');
INSERT INTO `conversion_formula` VALUES (8,'TEMPERATURE','1.0*?','FAHRENHEIT','FAHRENHEIT');
INSERT INTO `conversion_formula` VALUES (9,'TEMPERATURE','((5.0/9.0)*(?-32.0)','FAHRENHEIT','CELSIUS');
INSERT INTO `conversion_formula` VALUES (10,'TEMPERATURE','((9.0/5.0)*?)+32.0','CELSIUS','FAHRENHEIT');
INSERT INTO `conversion_formula` VALUES (11,'CURRENCY','1*?','DOLLAR','DOLLAR');
INSERT INTO `conversion_formula` VALUES (12,'CURRENCY','0.71*?','DOLLAR','EURO');
INSERT INTO `conversion_formula` VALUES (14,'CURRENCY','1.38*?','EURO','DOLLAR');
INSERT INTO `conversion_formula` VALUES (15,'NUMBER','?/100.0','ONE','HUNDRED');
INSERT INTO `conversion_formula` VALUES (16,'NUMBER','?/1000.0','ONE','THOUSAND');
INSERT INTO `conversion_formula` VALUES (17,'NUMBER','100.0*?','HUNDRED','ONE');
INSERT INTO `conversion_formula` VALUES (18,'NUMBER','1000.0*?','THOUSAND','ONE');
INSERT INTO `conversion_formula` VALUES (19,'WEIGHT','1.0*?','POUND','POUND');
INSERT INTO `conversion_formula` VALUES (20,'WEIGHT','0.4539*?','POUND','KILO');
INSERT INTO `conversion_formula` VALUES (21,'WEIGHT','?/0.4539','KILO','POUND');
INSERT INTO `conversion_formula` VALUES (22,'DISTANCE','1.0*?','METER','METER');
INSERT INTO `conversion_formula` VALUES (23,'DISTANCE','?/1000.0','METER','KILOMETER');
INSERT INTO `conversion_formula` VALUES (24,'DISTANCE','1000.0*?','KILOMETER','METER');
INSERT INTO `conversion_formula` VALUES (25,'DISTANCE','?/1609.344','METER','MILE');
INSERT INTO `conversion_formula` VALUES (26,'DISTANCE','1609.344*?','MILE','METER');
INSERT INTO `conversion_formula` VALUES (27,'DISTANCE','?/0.3048','METER','FEET');
INSERT INTO `conversion_formula` VALUES (28,'DISTANCE','0.3048*?','FEET','METER');
INSERT INTO `conversion_formula` VALUES (29,'DISTANCE','?/0.9144','METER','YARD');
INSERT INTO `conversion_formula` VALUES (30,'DISTANCE','0.9144*?','YARD','METER');
INSERT INTO `conversion_formula` VALUES (31,'DISTANCE','?/0.0254','METER','INCH');
INSERT INTO `conversion_formula` VALUES (32,'DISTANCE','0.0254*?','INCH','METER');
INSERT INTO `conversion_formula` VALUES (33,'POWER','1.0*?','KILOWATT','KILOWATT');
INSERT INTO `conversion_formula` VALUES (34,'POWER','?/1000.0','KILOWATT','WATT');
INSERT INTO `conversion_formula` VALUES (35,'POWER','1000.0*?','WATT','KILOWATT');
INSERT INTO `conversion_formula` VALUES (36,'POWER','?/0.7456','KILOWATT','HORSEPOWER');
INSERT INTO `conversion_formula` VALUES (37,'POWER','0.7456*?','HORSEPOWER','KILOWATT');
INSERT INTO `conversion_formula` VALUES (38,'VOLUME','1.0*?','CUBICCENTIMETER','CUBICCENTIMETER');
INSERT INTO `conversion_formula` VALUES (39,'VOLUME','?/100.0','CUBICCENTIMETER','CUBICMETER');
INSERT INTO `conversion_formula` VALUES (40,'VOLUME','100.0*?','CUBICMETER','CUBICCENTIMETER');
INSERT INTO `conversion_formula` VALUES (41,'VOLUME','?*0.001','CUBICCENTIMETER','LITER');
INSERT INTO `conversion_formula` VALUES (42,'VOLUME','0.001/?','LITER','CUBICCENTIMETER');
INSERT INTO `conversion_formula` VALUES (51,'TIMEDURATION','1.0*?','MONTH','MONTH');
INSERT INTO `conversion_formula` VALUES (52,'TIMEDURATION','?/12','MONTH','YEAR');
INSERT INTO `conversion_formula` VALUES (53,'TIMEDURATION','12*?','YEAR','MONTH');
INSERT INTO `conversion_formula` VALUES (54,'TIMEDURATION','30*?','MONTH','DAY');
INSERT INTO `conversion_formula` VALUES (55,'TIMEDURATION','?/30','DAY','MONTH');
INSERT INTO `conversion_formula` VALUES (56,'TIMEDURATION','7*?','WEEK','DAY');
INSERT INTO `conversion_formula` VALUES (57,'AREA','1550.003*?','SQUARE METER','SQUARE INCH');
INSERT INTO `conversion_formula` VALUES (58,'AREA','10.764*?','SQUARE METER','SQUARE FEET');
INSERT INTO `conversion_formula` VALUES (59,'AREA','1.196*?','SQUARE METER','SQUARE YARD');
INSERT INTO `conversion_formula` VALUES (60,'AREA','(2.471/(1000*10))*?','SQUARE METER','ACRE');
INSERT INTO `conversion_formula` VALUES (61,'AREA','0.0001*?','SQUARE METER','HECTARE');
INSERT INTO `conversion_formula` VALUES (62,'AREA','1.0*?','SQUARE METER','SQUARE METER');
INSERT INTO `conversion_formula` VALUES (63,'AREA','(6.4516/(1000*10))*?','SQUARE INCH','SQUARE METER');
INSERT INTO `conversion_formula` VALUES (64,'AREA','(9.29/100)*?','SQUARE FEET','SQUARE METER');
INSERT INTO `conversion_formula` VALUES (65,'AREA','(8.361/10)*?','SQUARE YARD','SQUARE METER');
INSERT INTO `conversion_formula` VALUES (66,'AREA','4046.856*?','ACRE','SQUARE METER');
INSERT INTO `conversion_formula` VALUES (67,'AREA','(1000*10)*?','HECTARE','SQUARE METER');
INSERT INTO `conversion_formula` VALUES (68,'MEMORY','1024*?','MEGABYTES','KILOBYTES');
INSERT INTO `conversion_formula` VALUES (69,'MEMORY','1024*1024*?','GIGABYTES','KILOBYTES');
INSERT INTO `conversion_formula` VALUES (70,'MEMORY','?/1024','KILOBYTES','MEGABYTES');
INSERT INTO `conversion_formula` VALUES (71,'MEMORY','?/1024*1024','KILOBYTES','GIGABYTES');
INSERT INTO `conversion_formula` VALUES (72,'MEMORY','1.0*?','KILOBYTES','KILOBYTES');
INSERT INTO `conversion_formula` VALUES (73,'RESOLUTION','1000*1000*?','MEGAPIXELS','PIXELS');
INSERT INTO `conversion_formula` VALUES (74,'RESOLUTION','?/1000*1000','PIXELS','MEGAPIXELS');
INSERT INTO `conversion_formula` VALUES (75,'RESOLUTION','1.0*?','PIXELS','PIXELS');
/*!40000 ALTER TABLE `conversion_formula` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `country_lookup`
--

DROP TABLE IF EXISTS `country_lookup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `country_lookup` (
  `ID` bigint(20) NOT NULL,
  `COUNTRY_CODE` varchar(10) NOT NULL,
  `COUNTRY_DESC` varchar(55) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `IDX_CLK_CC` (`COUNTRY_CODE`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `country_lookup`
--

LOCK TABLES `country_lookup` WRITE;
/*!40000 ALTER TABLE `country_lookup` DISABLE KEYS */;
INSERT INTO `country_lookup` VALUES (1000,'AF','Afghanistan');
INSERT INTO `country_lookup` VALUES (1001,'AL','Albania');
INSERT INTO `country_lookup` VALUES (1002,'DZ','Algeria');
INSERT INTO `country_lookup` VALUES (1003,'AS','American Samoa');
INSERT INTO `country_lookup` VALUES (1004,'AD','Andorra');
INSERT INTO `country_lookup` VALUES (1005,'AO','Angola');
INSERT INTO `country_lookup` VALUES (1006,'AI','Anguilla');
INSERT INTO `country_lookup` VALUES (1007,'AQ','Antarctica');
INSERT INTO `country_lookup` VALUES (1008,'AG','Antigua and Barbuda');
INSERT INTO `country_lookup` VALUES (1009,'AR','Argentina');
INSERT INTO `country_lookup` VALUES (1011,'AM','Armenia');
INSERT INTO `country_lookup` VALUES (1012,'AW','Aruba');
INSERT INTO `country_lookup` VALUES (1013,'AU','Australia');
INSERT INTO `country_lookup` VALUES (1014,'AT','Austria');
INSERT INTO `country_lookup` VALUES (1015,'AZ','Azerbaijan');
INSERT INTO `country_lookup` VALUES (1016,'BS','Bahamas');
INSERT INTO `country_lookup` VALUES (1017,'BH','Bahrain');
INSERT INTO `country_lookup` VALUES (1018,'BD','Bangladesh');
INSERT INTO `country_lookup` VALUES (1019,'BB','Barbados');
INSERT INTO `country_lookup` VALUES (1020,'BY','Belarus');
INSERT INTO `country_lookup` VALUES (1021,'BE','Belgium');
INSERT INTO `country_lookup` VALUES (1022,'BZ','Belize');
INSERT INTO `country_lookup` VALUES (1023,'BJ','Benin');
INSERT INTO `country_lookup` VALUES (1024,'BM','Bermuda');
INSERT INTO `country_lookup` VALUES (1025,'BT','Bhutan');
INSERT INTO `country_lookup` VALUES (1026,'BO','Bolivia');
INSERT INTO `country_lookup` VALUES (1027,'BA','Bosnia and Herzegovina');
INSERT INTO `country_lookup` VALUES (1028,'BW','Botswana');
INSERT INTO `country_lookup` VALUES (1029,'BV','Bouvet Island');
INSERT INTO `country_lookup` VALUES (1030,'BR','Brazil');
INSERT INTO `country_lookup` VALUES (1031,'IO','British Indian Ocean Territory');
INSERT INTO `country_lookup` VALUES (1032,'BN','Brunei Darussalam');
INSERT INTO `country_lookup` VALUES (1033,'BG','Bulgaria');
INSERT INTO `country_lookup` VALUES (1034,'BF','Burkina Faso');
INSERT INTO `country_lookup` VALUES (1035,'BI','Burundi');
INSERT INTO `country_lookup` VALUES (1036,'KH','Cambodia');
INSERT INTO `country_lookup` VALUES (1037,'CM','Cameroon');
INSERT INTO `country_lookup` VALUES (1038,'CA','Canada');
INSERT INTO `country_lookup` VALUES (1039,'CV','Cape Verde');
INSERT INTO `country_lookup` VALUES (1040,'KY','Cayman Islands');
INSERT INTO `country_lookup` VALUES (1041,'CF','Central African Republic');
INSERT INTO `country_lookup` VALUES (1042,'TD','Chad');
INSERT INTO `country_lookup` VALUES (1043,'CL','Chile');
INSERT INTO `country_lookup` VALUES (1044,'CN','China');
INSERT INTO `country_lookup` VALUES (1045,'CX','Christmas Island');
INSERT INTO `country_lookup` VALUES (1046,'CC','Cocos (Keeling) Islands');
INSERT INTO `country_lookup` VALUES (1047,'CO','Colombia');
INSERT INTO `country_lookup` VALUES (1048,'KM','Comoros');
INSERT INTO `country_lookup` VALUES (1049,'CG','Congo');
INSERT INTO `country_lookup` VALUES (1050,'CD','Congo');
INSERT INTO `country_lookup` VALUES (1051,'CK','Cook Islands');
INSERT INTO `country_lookup` VALUES (1052,'CR','Costa Rica');
INSERT INTO `country_lookup` VALUES (1053,'CI','Cote D\'Ivoire');
INSERT INTO `country_lookup` VALUES (1054,'HR','Croatia');
INSERT INTO `country_lookup` VALUES (1055,'CU','Cuba');
INSERT INTO `country_lookup` VALUES (1056,'CY','Cyprus');
INSERT INTO `country_lookup` VALUES (1057,'CZ','Czech Republic');
INSERT INTO `country_lookup` VALUES (1058,'DK','Denmark');
INSERT INTO `country_lookup` VALUES (1059,'DJ','Djibouti');
INSERT INTO `country_lookup` VALUES (1060,'DM','Dominica');
INSERT INTO `country_lookup` VALUES (1061,'DO','Dominican Republic');
INSERT INTO `country_lookup` VALUES (1062,'EC','Ecuador');
INSERT INTO `country_lookup` VALUES (1063,'EG','Egypt');
INSERT INTO `country_lookup` VALUES (1064,'SV','El Salvador');
INSERT INTO `country_lookup` VALUES (1065,'GQ','Equatorial Guinea');
INSERT INTO `country_lookup` VALUES (1066,'ER','Eritrea');
INSERT INTO `country_lookup` VALUES (1067,'EE','Estonia');
INSERT INTO `country_lookup` VALUES (1068,'ET','Ethiopia');
INSERT INTO `country_lookup` VALUES (1069,'FK','Falkland Islands (Malvinas)');
INSERT INTO `country_lookup` VALUES (1070,'FO','Faroe Islands');
INSERT INTO `country_lookup` VALUES (1071,'FJ','Fiji');
INSERT INTO `country_lookup` VALUES (1072,'FI','Finland');
INSERT INTO `country_lookup` VALUES (1073,'FR','France');
INSERT INTO `country_lookup` VALUES (1074,'GF','French Guiana');
INSERT INTO `country_lookup` VALUES (1075,'PF','French Polynesia');
INSERT INTO `country_lookup` VALUES (1076,'TF','French Southern Territories');
INSERT INTO `country_lookup` VALUES (1077,'GA','Gabon');
INSERT INTO `country_lookup` VALUES (1078,'GM','Gambia');
INSERT INTO `country_lookup` VALUES (1079,'GE','Georgia');
INSERT INTO `country_lookup` VALUES (1080,'DE','Germany');
INSERT INTO `country_lookup` VALUES (1081,'GH','Ghana');
INSERT INTO `country_lookup` VALUES (1082,'GI','Gibraltar');
INSERT INTO `country_lookup` VALUES (1083,'GR','Greece');
INSERT INTO `country_lookup` VALUES (1084,'GL','Greenland');
INSERT INTO `country_lookup` VALUES (1085,'GD','Grenada');
INSERT INTO `country_lookup` VALUES (1086,'GP','Guadeloupe');
INSERT INTO `country_lookup` VALUES (1087,'GU','Guam');
INSERT INTO `country_lookup` VALUES (1088,'GT','Guatemala');
INSERT INTO `country_lookup` VALUES (1089,'GN','Guinea');
INSERT INTO `country_lookup` VALUES (1090,'GW','Guinea-Bissau');
INSERT INTO `country_lookup` VALUES (1091,'GY','Guyana');
INSERT INTO `country_lookup` VALUES (1092,'HT','Haiti');
INSERT INTO `country_lookup` VALUES (1093,'HM','Heard Island and Mcdonald Islands');
INSERT INTO `country_lookup` VALUES (1094,'VA','Holy See (Vatican City State)');
INSERT INTO `country_lookup` VALUES (1095,'HN','Honduras');
INSERT INTO `country_lookup` VALUES (1096,'HK','Hong Kong');
INSERT INTO `country_lookup` VALUES (1097,'HU','Hungary');
INSERT INTO `country_lookup` VALUES (1098,'IS','Iceland');
INSERT INTO `country_lookup` VALUES (1099,'IN','India');
INSERT INTO `country_lookup` VALUES (1100,'ID','Indonesia');
INSERT INTO `country_lookup` VALUES (1101,'IR','Iran, Islamic Republic of');
INSERT INTO `country_lookup` VALUES (1102,'IQ','Iraq');
INSERT INTO `country_lookup` VALUES (1103,'IE','Ireland');
INSERT INTO `country_lookup` VALUES (1104,'IT','Italy');
INSERT INTO `country_lookup` VALUES (1105,'IL','Israel');
INSERT INTO `country_lookup` VALUES (1106,'JM','Jamaica');
INSERT INTO `country_lookup` VALUES (1107,'JP','Japan');
INSERT INTO `country_lookup` VALUES (1108,'JO','Jordan');
INSERT INTO `country_lookup` VALUES (1109,'KZ','Kazakhstan');
INSERT INTO `country_lookup` VALUES (1110,'KE','Kenya');
INSERT INTO `country_lookup` VALUES (1111,'KI','Kiribati');
INSERT INTO `country_lookup` VALUES (1112,'KP','Korea, Democratic People\'s Republic of');
INSERT INTO `country_lookup` VALUES (1113,'KR','Korea, Republic of');
INSERT INTO `country_lookup` VALUES (1114,'KW','Kuwait');
INSERT INTO `country_lookup` VALUES (1115,'KG','Kyrgyzstan');
INSERT INTO `country_lookup` VALUES (1116,'LA','Lao People\'s Democratic Republic');
INSERT INTO `country_lookup` VALUES (1117,'LV','Latvia');
INSERT INTO `country_lookup` VALUES (1118,'LB','Lebanon');
INSERT INTO `country_lookup` VALUES (1119,'LS','Lesotho');
INSERT INTO `country_lookup` VALUES (1120,'LR','Liberia');
INSERT INTO `country_lookup` VALUES (1121,'LY','Libyan Arab Jamahiriya');
INSERT INTO `country_lookup` VALUES (1122,'LI','Liechtenstein');
INSERT INTO `country_lookup` VALUES (1123,'LT','Lithuania');
INSERT INTO `country_lookup` VALUES (1124,'LU','Luxembourg');
INSERT INTO `country_lookup` VALUES (1125,'MO','Macao');
INSERT INTO `country_lookup` VALUES (1126,'MK','Macedonia, the Former Yugoslav Republic of');
INSERT INTO `country_lookup` VALUES (1127,'MG','Madagascar');
INSERT INTO `country_lookup` VALUES (1128,'MW','Malawi');
INSERT INTO `country_lookup` VALUES (1129,'MY','Malaysia');
INSERT INTO `country_lookup` VALUES (1130,'MV','Maldives');
INSERT INTO `country_lookup` VALUES (1131,'ML','Mali');
INSERT INTO `country_lookup` VALUES (1132,'MT','Malta');
INSERT INTO `country_lookup` VALUES (1133,'MQ','Martinique');
INSERT INTO `country_lookup` VALUES (1134,'MR','Mauritania');
INSERT INTO `country_lookup` VALUES (1135,'MU','Mauritius');
INSERT INTO `country_lookup` VALUES (1136,'YT','Mayotte');
INSERT INTO `country_lookup` VALUES (1137,'MX','Mexico');
INSERT INTO `country_lookup` VALUES (1138,'FM','Micronesia, Federated States of');
INSERT INTO `country_lookup` VALUES (1139,'MD','Moldova, Republic of');
INSERT INTO `country_lookup` VALUES (1140,'MC','Monaco');
INSERT INTO `country_lookup` VALUES (1141,'MN','Mongolia');
INSERT INTO `country_lookup` VALUES (1142,'MS','Montserrat');
INSERT INTO `country_lookup` VALUES (1143,'MA','Morocco');
INSERT INTO `country_lookup` VALUES (1244,'MZ','Mozambique');
INSERT INTO `country_lookup` VALUES (1245,'MM','Myanmar');
INSERT INTO `country_lookup` VALUES (1246,'MH','Marshall Islands');
INSERT INTO `country_lookup` VALUES (1247,'NA','Namibia');
INSERT INTO `country_lookup` VALUES (1248,'NR','Nauru');
INSERT INTO `country_lookup` VALUES (1249,'NP','Nepal');
INSERT INTO `country_lookup` VALUES (1250,'NL','Netherlands');
INSERT INTO `country_lookup` VALUES (1251,'AN','Netherlands Antilles');
INSERT INTO `country_lookup` VALUES (1252,'NC','New Caledonia');
INSERT INTO `country_lookup` VALUES (1253,'NZ','New Zealand');
INSERT INTO `country_lookup` VALUES (1254,'NI','Nicaragua');
INSERT INTO `country_lookup` VALUES (1255,'NE','Niger');
INSERT INTO `country_lookup` VALUES (1256,'NG','Nigeria');
INSERT INTO `country_lookup` VALUES (1257,'NU','Niue');
INSERT INTO `country_lookup` VALUES (1258,'NF','Norfolk Island');
INSERT INTO `country_lookup` VALUES (1259,'MP','Northern Mariana Islands');
INSERT INTO `country_lookup` VALUES (1260,'NO','Norway');
INSERT INTO `country_lookup` VALUES (1261,'OM','Oman');
INSERT INTO `country_lookup` VALUES (1262,'PK','Pakistan');
INSERT INTO `country_lookup` VALUES (1263,'PW','Palau');
INSERT INTO `country_lookup` VALUES (1264,'PS','Palestinian Territory, Occupied');
INSERT INTO `country_lookup` VALUES (1265,'PA','Panama');
INSERT INTO `country_lookup` VALUES (1266,'PG','Papua New Guinea');
INSERT INTO `country_lookup` VALUES (1267,'PY','Paraguay');
INSERT INTO `country_lookup` VALUES (1268,'PE','Peru');
INSERT INTO `country_lookup` VALUES (1269,'PH','Philippines');
INSERT INTO `country_lookup` VALUES (1270,'PN','Pitcairn');
INSERT INTO `country_lookup` VALUES (1271,'PL','Poland');
INSERT INTO `country_lookup` VALUES (1272,'PT','Portugal');
INSERT INTO `country_lookup` VALUES (1273,'PR','Puerto Rico');
INSERT INTO `country_lookup` VALUES (1274,'QA','Qatar');
INSERT INTO `country_lookup` VALUES (1275,'RE','Reunion');
INSERT INTO `country_lookup` VALUES (1276,'RO','Romania');
INSERT INTO `country_lookup` VALUES (1277,'RU','Russian Federation');
INSERT INTO `country_lookup` VALUES (1278,'RW','Rwanda');
INSERT INTO `country_lookup` VALUES (1279,'SH','Saint Helena');
INSERT INTO `country_lookup` VALUES (1280,'KN','Saint Kitts and Nevis');
INSERT INTO `country_lookup` VALUES (1281,'LC','Saint Lucia');
INSERT INTO `country_lookup` VALUES (1282,'PM','Saint Pierre and Miquelon');
INSERT INTO `country_lookup` VALUES (1283,'VC','Saint Vincent and the Grenadines');
INSERT INTO `country_lookup` VALUES (1284,'WS','Samoa');
INSERT INTO `country_lookup` VALUES (1285,'SM','San Marino');
INSERT INTO `country_lookup` VALUES (1286,'ST','Sao Tome and Principe');
INSERT INTO `country_lookup` VALUES (1287,'SA','Saudi Arabia');
INSERT INTO `country_lookup` VALUES (1288,'SN','Senegal');
INSERT INTO `country_lookup` VALUES (1289,'CS','Serbia and Montenegro');
INSERT INTO `country_lookup` VALUES (1290,'SC','Seychelles');
INSERT INTO `country_lookup` VALUES (1291,'SL','Sierra Leone');
INSERT INTO `country_lookup` VALUES (1292,'SG','Singapore');
INSERT INTO `country_lookup` VALUES (1293,'SK','Slovakia');
INSERT INTO `country_lookup` VALUES (1294,'SI','Slovenia');
INSERT INTO `country_lookup` VALUES (1296,'SB','Solomon Islands');
INSERT INTO `country_lookup` VALUES (1297,'SO','Somalia');
INSERT INTO `country_lookup` VALUES (1298,'ZA','South Africa');
INSERT INTO `country_lookup` VALUES (1299,'GS','South Georgia and the South Sandwich Islands');
INSERT INTO `country_lookup` VALUES (1300,'ES','Spain');
INSERT INTO `country_lookup` VALUES (1301,'LK','Sri Lanka');
INSERT INTO `country_lookup` VALUES (1302,'SD','Sudan');
INSERT INTO `country_lookup` VALUES (1303,'SR','Suriname');
INSERT INTO `country_lookup` VALUES (1304,'SJ','Svalbard and Jan Mayen');
INSERT INTO `country_lookup` VALUES (1305,'SZ','Swaziland');
INSERT INTO `country_lookup` VALUES (1306,'SE','Sweden');
INSERT INTO `country_lookup` VALUES (1307,'CH','Switzerland');
INSERT INTO `country_lookup` VALUES (1308,'SY','Syrian Arab Republic');
INSERT INTO `country_lookup` VALUES (1309,'TW','Taiwan, Province of China');
INSERT INTO `country_lookup` VALUES (1310,'TJ','Tajikistan');
INSERT INTO `country_lookup` VALUES (1311,'TZ','Tanzania, United Republic of');
INSERT INTO `country_lookup` VALUES (1312,'TH','Thailand');
INSERT INTO `country_lookup` VALUES (1313,'TL','Timor-Leste');
INSERT INTO `country_lookup` VALUES (1314,'TG','Togo');
INSERT INTO `country_lookup` VALUES (1315,'TK','Tokelau');
INSERT INTO `country_lookup` VALUES (1316,'TO','Tonga');
INSERT INTO `country_lookup` VALUES (1317,'TT','Trinidad and Tobago');
INSERT INTO `country_lookup` VALUES (1318,'TN','Tunisia');
INSERT INTO `country_lookup` VALUES (1319,'TR','Turkey');
INSERT INTO `country_lookup` VALUES (1320,'TM','Turkmenistan');
INSERT INTO `country_lookup` VALUES (1321,'TC','Turks and Caicos Islands');
INSERT INTO `country_lookup` VALUES (1322,'TV','Tuvalu');
INSERT INTO `country_lookup` VALUES (1323,'UG','Uganda');
INSERT INTO `country_lookup` VALUES (1324,'UA','Ukraine');
INSERT INTO `country_lookup` VALUES (1325,'AE','United Arab Emirates');
INSERT INTO `country_lookup` VALUES (1326,'GB','United Kingdom');
INSERT INTO `country_lookup` VALUES (1327,'US','United States');
INSERT INTO `country_lookup` VALUES (1328,'UM','United States Minor Outlying Islands');
INSERT INTO `country_lookup` VALUES (1329,'UY','Uruguay');
INSERT INTO `country_lookup` VALUES (1330,'UZ','Uzbekistan');
INSERT INTO `country_lookup` VALUES (1331,'VU','Vanuatu');
INSERT INTO `country_lookup` VALUES (1332,'VE','Venezuela');
INSERT INTO `country_lookup` VALUES (1333,'VN','Viet Nam');
INSERT INTO `country_lookup` VALUES (1335,'VG','Virgin Islands');
INSERT INTO `country_lookup` VALUES (1336,'VI','Virgin Islands');
INSERT INTO `country_lookup` VALUES (1337,'WF','Wallis and Futuna');
INSERT INTO `country_lookup` VALUES (1338,'EH','Western Sahara');
INSERT INTO `country_lookup` VALUES (1339,'YE','Yemen');
INSERT INTO `country_lookup` VALUES (1340,'ZM','Zambia');
INSERT INTO `country_lookup` VALUES (1341,'ZW','Zimbabwe');
/*!40000 ALTER TABLE `country_lookup` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `data_source`
--

DROP TABLE IF EXISTS `data_source`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `data_source` (
  `ID` bigint(20) NOT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `PROVIDER` int(2) DEFAULT NULL,
  `CONN_TYPE` varchar(35) DEFAULT NULL,
  `JNDI_NAME` varchar(255) DEFAULT NULL,
  `USER_NAME` varchar(55) DEFAULT NULL,
  `PASSWORD` varchar(255) DEFAULT NULL,
  `PASSWORD_ENCRYPTED` char(1) DEFAULT 'Y',
  `LOCATION` varchar(255) DEFAULT NULL,
  `PORT` int(6) DEFAULT NULL,
  `SCHEMA_NAME` varchar(55) DEFAULT NULL,
  `OWNER` varchar(255) DEFAULT NULL,
  `JNDI_CONN_FACTORY` varchar(255) DEFAULT NULL,
  `JNDI_PROVIDER_URL` varchar(255) DEFAULT NULL,
  `TYPE` varchar(15) NOT NULL DEFAULT 'REGULAR',
  `DISPLAY_NAME` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `IDX_DS_NM` (`NAME`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `data_source`
--

LOCK TABLES `data_source` WRITE;
/*!40000 ALTER TABLE `data_source` DISABLE KEYS */;
INSERT INTO `data_source` VALUES (1,'answerscatalog','answerscatalog',2,'PROPERTIES',NULL,'testuser','execue','Y','10.10.50.221',3326,'answerscatalog',NULL,NULL,NULL,'CATALOG','Answers Catalog');
INSERT INTO `data_source` VALUES (2,'uploaded','uploaded',2,'PROPERTIES',NULL,'testuser','execue','Y','10.10.50.221',3326,'uploaded',NULL,NULL,NULL,'UPLOADED','Uploaded');
INSERT INTO `data_source` VALUES (1001,'UnStructuredAppWarehouse','Warehouse Entry for UnStructured App',2,'PROPERTIES',NULL,'root','execue','Y','10.10.56.111',3326,'wh-unstructured-app',NULL,NULL,NULL,'SYSTEM_USWH','UnStructured App Warehouse');
INSERT INTO `data_source` VALUES (1002,'IPLocationDataSource','Entry for IPLocation DataSource',2,'PROPERTIES',NULL,'root','execue','Y','10.10.56.111',3326,'wh-iplocation',NULL,NULL,NULL,'SYSTEM_IP_DB','IPLocation DataSource');
INSERT INTO `data_source` VALUES (1003,'CityCenterZipCodeDS','Entry for City Center Zip Code DataSource',2,'PROPERTIES',NULL,'root','execue','Y','10.10.56.111',3326,'wh-citycenter-zipcode',NULL,NULL,NULL,'SYSTEM_CCZC','City Center Zip Code DataSource');
INSERT INTO `data_source` VALUES (1004,'DefaultContentAggregatorWarehouse','Warehouse Entry for Default Content Aggregator',2,'PROPERTIES',NULL,'appuser','execue','Y','10.10.56.141',3306,'rnews',NULL,NULL,NULL,'SYSTEM_USCA','Default Content Aggregator Warehouse');
INSERT INTO `data_source` VALUES (1005,'SolrDataSource','Entry for Solr Data Source',6,'PROPERTIES',NULL,NULL,NULL,'Y','10.10.56.61',7075,'solr',NULL,NULL,NULL,'SYSTEM_SOLR','Solr Data Source');
/*!40000 ALTER TABLE `data_source` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `date_format`
--

DROP TABLE IF EXISTS `date_format`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `date_format` (
  `ID` bigint(20) NOT NULL,
  `FORMAT` varchar(35) DEFAULT NULL,
  `PROVIDER_TYPE` int(2) DEFAULT NULL,
  `DB_FORMAT` varchar(35) DEFAULT NULL,
  `PLAIN_FORMAT` char(1) DEFAULT 'N',
  `QUALIFIER` varchar(35) DEFAULT NULL,
  `API_SUPPORTED` char(1) DEFAULT 'Y',
  `QUALIFIER_BE_ID` bigint(20) DEFAULT NULL,
  `DATA_TYPE` varchar(20) DEFAULT NULL,
  `EVALUATED` char(1) DEFAULT 'Y',
  PRIMARY KEY (`ID`),
  KEY `IDX_DF_AS` (`API_SUPPORTED`),
  KEY `IDX_DF_PF` (`PLAIN_FORMAT`),
  KEY `IDX_DF_PT` (`PROVIDER_TYPE`),
  KEY `IDX_DF_QF` (`QUALIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `date_format`
--

LOCK TABLES `date_format` WRITE;
/*!40000 ALTER TABLE `date_format` DISABLE KEYS */;
INSERT INTO `date_format` VALUES (1001,'yyyy-MM-dd',2,'%Y-%m-%d','N','Day','Y',206,'DATE','Y');
INSERT INTO `date_format` VALUES (1002,'dd-MM-yyyy',2,'%d-%m-%Y','N','Day','Y',206,'DATE','Y');
INSERT INTO `date_format` VALUES (1003,'MM-dd-yyyy',2,'%m-%d-%Y','N','Day','Y',206,'DATE','Y');
INSERT INTO `date_format` VALUES (1004,'yyyy/MM/dd',2,'%Y/%m/%d','N','Day','Y',206,'DATE','Y');
INSERT INTO `date_format` VALUES (1005,'dd/MM/yyyy',2,'%d/%m/%Y','N','Day','Y',206,'DATE','Y');
INSERT INTO `date_format` VALUES (1006,'MM/dd/yyyy',2,'%m/%d/%Y','N','Day','Y',206,'DATE','Y');
INSERT INTO `date_format` VALUES (1007,'yyyyMM',2,'%Y%m','Y','Month','Y',204,'INT','Y');
INSERT INTO `date_format` VALUES (1008,'yyyy',2,'%Y','Y','Year','Y',202,'INT','Y');
INSERT INTO `date_format` VALUES (1009,'dd\\MM\\yyyy',2,'%d\\%m\\%Y','N','Day','Y',206,'DATE','Y');
INSERT INTO `date_format` VALUES (1010,'MM\\dd\\yyyy',2,'%m\\%d\\%Y','N','Day','Y',206,'DATE','Y');
INSERT INTO `date_format` VALUES (1011,'yyyy\\MM\\dd',2,'%Y\\%m\\%d','N','Day','Y',206,'DATE','Y');
INSERT INTO `date_format` VALUES (1012,'yyyyQ',2,NULL,'Y','Quarter','N',203,'INT','Y');
INSERT INTO `date_format` VALUES (1013,'d-M-yyyy',2,'%c-%e-%Y','N','Day','Y',206,'DATE','Y');
INSERT INTO `date_format` VALUES (1014,'d/M/yyyy',2,'%c/%e/%Y','N','Day','Y',206,'DATE','Y');
INSERT INTO `date_format` VALUES (1016,'M-d-yyyy',2,'%e-%c-%Y','N','Day','Y',206,'DATE','Y');
INSERT INTO `date_format` VALUES (1017,'M/d/yyyy',2,'%e/%c/%Y','N','Day','Y',206,'DATE','Y');
INSERT INTO `date_format` VALUES (1021,'yyyy\\M\\d',2,'%Y\\%e\\%c','N','Day','Y',206,'DATE','Y');
INSERT INTO `date_format` VALUES (1019,'yyyy-M-d',2,'%Y-%e-%c','N','Day','Y',206,'DATE','Y');
INSERT INTO `date_format` VALUES (1020,'yyyy/M/d',2,'%Y/%e/%c','N','Day','Y',206,'DATE','Y');
INSERT INTO `date_format` VALUES (1022,'yyyy-d-M',2,'%Y-%c-%e','N','Day','Y',206,'DATE','Y');
INSERT INTO `date_format` VALUES (1023,'yyyy/d/M',2,'%Y/%c/%e','N','Day','Y',206,'DATE','Y');
INSERT INTO `date_format` VALUES (1018,'M\\d\\yyyy',2,'%e\\%c\\%Y','N','Day','Y',206,'DATE','Y');
INSERT INTO `date_format` VALUES (1025,'yyyy-dd-MM',2,'%Y-%d-%m','N','Day','Y',206,'DATE','Y');
INSERT INTO `date_format` VALUES (1026,'yyyy/dd/MM',2,'%Y/%d/%m','N','Day','Y',206,'DATE','Y');
INSERT INTO `date_format` VALUES (1015,'d\\M\\yyyy',2,'%c\\%e\\%Y','N','Day','Y',206,'DATE','Y');
INSERT INTO `date_format` VALUES (1024,'yyyy\\d\\M',2,'%Y\\%c\\%e','N','Day','Y',206,'DATE','Y');
INSERT INTO `date_format` VALUES (1027,'yyyy\\dd\\MM',2,'%Y\\%d\\%m','N','Day','Y',206,'DATE','Y');
INSERT INTO `date_format` VALUES (1030,'yyyy-MM-dd H:mm',2,'%Y-%m-%d %H:%i','N','Minute','Y',208,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1031,'dd-MM-yyyy H:mm',2,'%d-%m-%Y %H:%i','N','Minute','Y',208,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1032,'MM-dd-yyyy H:mm',2,'%m-%d-%Y %H:%i','N','Minute','Y',208,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1033,'yyyy/MM/dd H:mm',2,'%Y/%m/%d %H:%i','N','Minute','Y',208,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1034,'dd/MM/yyyy H:mm',2,'%d/%m/%Y %H:%i','N','Minute','Y',208,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1035,'MM/dd/yyyy H:mm',2,'%m/%d/%Y %H:%i','N','Minute','Y',208,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1036,'dd\\MM\\yyyy H:mm',2,'%d\\%m\\%Y %H:%i','N','Minute','Y',208,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1037,'MM\\dd\\yyyy H:mm',2,'%m\\%d\\%Y %H:%i','N','Minute','Y',208,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1038,'yyyy\\MM\\dd H:mm',2,'%Y\\%m\\%d %H:%i','N','Minute','Y',208,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1084,'yyyy\\dd\\MM H:mm',2,'%Y\\%d\\%m %H:%i','N','Minute','Y',208,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1085,'yyyy/dd/MM H:mm',2,'%Y/%d/%m %H:%i','N','Minute','Y',208,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1086,'yyyy-dd-MM H:mm',2,'%Y-%d-%m %H:%i','N','Minute','Y',208,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1039,'yyyy-MM-dd H',2,'%Y-%m-%d %H','N','Hour','Y',207,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1040,'dd-MM-yyyy H',2,'%d-%m-%Y %H','N','Hour','Y',207,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1041,'MM-dd-yyyy H',2,'%m-%d-%Y %H','N','Hour','Y',207,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1042,'yyyy/MM/dd H',2,'%Y/%m/%d %H','N','Hour','Y',207,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1043,'dd/MM/yyyy H',2,'%d/%m/%Y %H','N','Hour','Y',207,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1044,'MM/dd/yyyy H',2,'%m/%d/%Y %H','N','Hour','Y',207,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1045,'dd\\MM\\yyyy H',2,'%d\\%m\\%Y %H','N','Hour','Y',207,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1046,'MM\\dd\\yyyy H',2,'%m\\%d\\%Y %H','N','Hour','Y',207,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1047,'yyyy\\MM\\dd H',2,'%Y\\%m\\%d %H','N','Hour','Y',207,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1087,'yyyy\\dd\\MM H',2,'%Y\\%d\\%m %H','N','Hour','Y',207,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1088,'yyyy/dd/MM H',2,'%Y/%d/%m %H','N','Hour','Y',207,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1089,'yyyy-dd-MM H',2,'%Y-%d-%m %H','N','Hour','Y',207,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1048,'yyyy-MM-dd H:mm:ss',2,'%Y-%m-%d %H:%i:%s','N','Second','Y',209,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1049,'dd-MM-yyyy H:mm:ss',2,'%d-%m-%Y %H:%i:%s','N','Second','Y',209,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1050,'MM-dd-yyyy H:mm:ss',2,'%m-%d-%Y %H:%i:%s','N','Second','Y',209,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1051,'yyyy/MM/dd H:mm:ss',2,'%Y/%m/%d %H:%i:%s','N','Second','Y',209,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1052,'dd/MM/yyyy H:mm:ss',2,'%d/%m/%Y %H:%i:%s','N','Second','Y',209,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1053,'MM/dd/yyyy H:mm:ss',2,'%m/%d/%Y %H:%i:%s','N','Second','Y',209,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1054,'dd\\MM\\yyyy H:mm:ss',2,'%d\\%m\\%Y %H:%i:%s','N','Second','Y',209,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1055,'MM\\dd\\yyyy H:mm:ss',2,'%m\\%d\\%Y %H:%i:%s','N','Second','Y',209,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1056,'yyyy\\MM\\dd H:mm:ss',2,'%Y\\%m\\%d %H:%i:%s','N','Second','Y',209,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1090,'yyyy\\dd\\MM H:mm:ss',2,'%Y\\%d\\%m %H:%i:%s','N','Second','Y',209,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1091,'yyyy/dd/MM H:mm:ss',2,'%Y/%d/%m %H:%i:%s','N','Second','Y',209,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1092,'yyyy-dd-MM H:mm:ss',2,'%Y-%d-%m %H:%i:%s','N','Second','Y',209,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1057,'yyyy-MM-dd HH:mm',2,'%Y-%m-%d %H:%i','N','Minute','Y',208,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1058,'dd-MM-yyyy HH:mm',2,'%d-%m-%Y %H:%i','N','Minute','Y',208,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1059,'MM-dd-yyyy HH:mm',2,'%m-%d-%Y %H:%i','N','Minute','Y',208,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1060,'yyyy/MM/dd HH:mm',2,'%Y/%m/%d %H:%i','N','Minute','Y',208,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1061,'dd/MM/yyyy HH:mm',2,'%d/%m/%Y %H:%i','N','Minute','Y',208,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1062,'MM/dd/yyyy HH:mm',2,'%m/%d/%Y %H:%i','N','Minute','Y',208,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1063,'dd\\MM\\yyyy HH:mm',2,'%d\\%m\\%Y %H:%i','N','Minute','Y',208,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1064,'MM\\dd\\yyyy HH:mm',2,'%m\\%d\\%Y %H:%i','N','Minute','Y',208,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1065,'yyyy\\MM\\dd HH:mm',2,'%Y\\%m\\%d %H:%i','N','Minute','Y',208,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1093,'yyyy\\dd\\MM HH:mm',2,'%Y\\%d\\%m %H:%i','N','Minute','Y',208,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1094,'yyyy/dd/MM HH:mm',2,'%Y/%d/%m %H:%i','N','Minute','Y',208,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1095,'yyyy-dd-MM HH:mm',2,'%Y-%d-%m %H:%i','N','Minute','Y',208,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1066,'yyyy-MM-dd HH',2,'%Y-%m-%d %H','N','Hour','Y',207,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1067,'dd-MM-yyyy HH',2,'%d-%m-%Y %H','N','Hour','Y',207,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1068,'MM-dd-yyyy HH',2,'%m-%d-%Y %H','N','Hour','Y',207,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1069,'yyyy/MM/dd HH',2,'%Y/%m/%d %H','N','Hour','Y',207,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1070,'dd/MM/yyyy HH',2,'%d/%m/%Y %H','N','Hour','Y',207,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1071,'MM/dd/yyyy HH',2,'%m/%d/%Y %H','N','Hour','Y',207,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1072,'dd\\MM\\yyyy HH',2,'%d\\%m\\%Y %H','N','Hour','Y',207,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1073,'MM\\dd\\yyyy HH',2,'%m\\%d\\%Y %H','N','Hour','Y',207,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1074,'yyyy\\MM\\dd HH',2,'%Y\\%m\\%d %H','N','Hour','Y',207,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1096,'yyyy\\dd\\MM HH',2,'%Y\\%d\\%m %H','N','Hour','Y',207,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1097,'yyyy/dd/MM HH',2,'%Y/%d/%m %H','N','Hour','Y',207,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1098,'yyyy-dd-MM HH',2,'%Y-%d-%m %H','N','Hour','Y',207,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1075,'yyyy-MM-dd HH:mm:ss',2,'%Y-%m-%d %H:%i:%s','N','Second','Y',209,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1076,'dd-MM-yyyy HH:mm:ss',2,'%d-%m-%Y %H:%i:%s','N','Second','Y',209,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1077,'MM-dd-yyyy HH:mm:ss',2,'%m-%d-%Y %H:%i:%s','N','Second','Y',209,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1078,'yyyy/MM/dd HH:mm:ss',2,'%Y/%m/%d %H:%i:%s','N','Second','Y',209,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1079,'dd/MM/yyyy HH:mm:ss',2,'%d/%m/%Y %H:%i:%s','N','Second','Y',209,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1080,'MM/dd/yyyy HH:mm:ss',2,'%m/%d/%Y %H:%i:%s','N','Second','Y',209,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1081,'dd\\MM\\yyyy HH:mm:ss',2,'%d\\%m\\%Y %H:%i:%s','N','Second','Y',209,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1082,'MM\\dd\\yyyy HH:mm:ss',2,'%m\\%d\\%Y %H:%i:%s','N','Second','Y',209,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1083,'yyyy\\MM\\dd HH:mm:ss',2,'%Y\\%m\\%d %H:%i:%s','N','Second','Y',209,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1099,'yyyy\\dd\\MM HH:mm:ss',2,'%Y\\%d\\%m %H:%i:%s','N','Second','Y',209,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1100,'yyyy/dd/MM HH:mm:ss',2,'%Y/%d/%m %H:%i:%s','N','Second','Y',209,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1101,'yyyy-dd-MM HH:mm:ss',2,'%Y-%d-%m %H:%i:%s','N','Second','Y',209,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1106,'yyyy-M-d H:mm',2,'%Y-%e-%c %H:%i','N','Minute','Y',208,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1107,'d-M-yyyy H:mm',2,'%c-%e-%Y %H:%i','N','Minute','Y',208,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1108,'M-d-yyyy H:mm',2,'%e-%c-%Y %H:%i','N','Minute','Y',208,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1109,'yyyy/M/d H:mm',2,'%Y/%e/%c %H:%i','N','Minute','Y',208,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1110,'d/M/yyyy H:mm',2,'%c/%e/%Y %H:%i','N','Minute','Y',208,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1111,'M/d/yyyy H:mm',2,'%e/%c/%Y %H:%i','N','Minute','Y',208,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1112,'d\\M\\yyyy H:mm',2,'%c\\%e\\%Y %H:%i','N','Minute','Y',208,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1113,'M\\d\\yyyy H:mm',2,'%e\\%c\\%Y %H:%i','N','Minute','Y',208,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1114,'yyyy\\M\\d H:mm',2,'%Y\\%e\\%c %H:%i','N','Minute','Y',208,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1115,'yyyy\\d\\M H:mm',2,'%Y\\%c\\%e %H:%i','N','Minute','Y',208,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1116,'yyyy/d/M H:mm',2,'%Y/%c/%e %H:%i','N','Minute','Y',208,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1117,'yyyy-d-M H:mm',2,'%Y-%c-%e %H:%i','N','Minute','Y',208,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1118,'yyyy-M-d H',2,'%Y-%e-%c %H','N','Hour','Y',207,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1119,'d-M-yyyy H',2,'%c-%e-%Y %H','N','Hour','Y',207,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1120,'M-d-yyyy H',2,'%e-%c-%Y %H','N','Hour','Y',207,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1121,'yyyy/M/d H',2,'%Y/%e/%c %H','N','Hour','Y',207,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1122,'d/M/yyyy H',2,'%c/%e/%Y %H','N','Hour','Y',207,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1123,'M/d/yyyy H',2,'%e/%c/%Y %H','N','Hour','Y',207,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1124,'d\\M\\yyyy H',2,'%c\\%e\\%Y %H','N','Hour','Y',207,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1125,'M\\d\\yyyy H',2,'%e\\%c\\%Y %H','N','Hour','Y',207,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1126,'yyyy\\M\\d H',2,'%Y\\%e\\%c %H','N','Hour','Y',207,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1127,'yyyy\\d\\M H',2,'%Y\\%c\\%e %H','N','Hour','Y',207,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1128,'yyyy/d/M H',2,'%Y/%c/%e %H','N','Hour','Y',207,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1129,'yyyy-d-M H',2,'%Y-%c-%e %H','N','Hour','Y',207,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1130,'yyyy-M-d H:mm:ss',2,'%Y-%e-%c %H:%i:%s','N','Second','Y',209,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1131,'d-M-yyyy H:mm:ss',2,'%c-%e-%Y %H:%i:%s','N','Second','Y',209,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1132,'M-d-yyyy H:mm:ss',2,'%e-%c-%Y %H:%i:%s','N','Second','Y',209,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1133,'yyyy/M/d H:mm:ss',2,'%Y/%e/%c %H:%i:%s','N','Second','Y',209,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1134,'d/M/yyyy H:mm:ss',2,'%c/%e/%Y %H:%i:%s','N','Second','Y',209,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1135,'M/d/yyyy H:mm:ss',2,'%e/%c/%Y %H:%i:%s','N','Second','Y',209,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1136,'d\\M\\yyyy H:mm:ss',2,'%c\\%e\\%Y %H:%i:%s','N','Second','Y',209,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1137,'M\\d\\yyyy H:mm:ss',2,'%e\\%c\\%Y %H:%i:%s','N','Second','Y',209,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1138,'yyyy\\M\\d H:mm:ss',2,'%Y\\%e\\%c %H:%i:%s','N','Second','Y',209,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1139,'yyyy\\d\\M H:mm:ss',2,'%Y\\%c\\%e %H:%i:%s','N','Second','Y',209,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1140,'yyyy/d/M H:mm:ss',2,'%Y/%c/%e %H:%i:%s','N','Second','Y',209,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1141,'yyyy-d-M H:mm:ss',2,'%Y-%c-%e %H:%i:%s','N','Second','Y',209,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1142,'yyyy-M-d HH:mm',2,'%Y-%e-%c %H:%i','N','Minute','Y',208,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1143,'d-M-yyyy HH:mm',2,'%c-%e-%Y %H:%i','N','Minute','Y',208,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1144,'M-d-yyyy HH:mm',2,'%e-%c-%Y %H:%i','N','Minute','Y',208,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1145,'yyyy/M/d HH:mm',2,'%Y/%e/%c %H:%i','N','Minute','Y',208,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1146,'d/M/yyyy HH:mm',2,'%c/%e/%Y %H:%i','N','Minute','Y',208,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1147,'M/d/yyyy HH:mm',2,'%e/%c/%Y %H:%i','N','Minute','Y',208,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1148,'d\\M\\yyyy HH:mm',2,'%c\\%e\\%Y %H:%i','N','Minute','Y',208,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1149,'M\\d\\yyyy HH:mm',2,'%e\\%c\\%Y %H:%i','N','Minute','Y',208,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1150,'yyyy\\M\\d HH:mm',2,'%Y\\%e\\%c %H:%i','N','Minute','Y',208,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1151,'yyyy\\d\\M HH:mm',2,'%Y\\%c\\%e %H:%i','N','Minute','Y',208,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1152,'yyyy/d/M HH:mm',2,'%Y/%c/%e %H:%i','N','Minute','Y',208,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1153,'yyyy-d-M HH:mm',2,'%Y-%c-%e %H:%i','N','Minute','Y',208,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1154,'yyyy-M-d HH',2,'%Y-%e-%c %H','N','Hour','Y',207,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1155,'d-M-yyyy HH',2,'%c-%e-%Y %H','N','Hour','Y',207,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1156,'M-d-yyyy HH',2,'%e-%c-%Y %H','N','Hour','Y',207,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1157,'yyyy/M/d HH',2,'%Y/%e/%c %H','N','Hour','Y',207,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1158,'d/M/yyyy HH',2,'%c/%e/%Y %H','N','Hour','Y',207,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1159,'M/d/yyyy HH',2,'%e/%c/%Y %H','N','Hour','Y',207,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1160,'d\\M\\yyyy HH',2,'%c\\%e\\%Y %H','N','Hour','Y',207,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1161,'M\\d\\yyyy HH',2,'%e\\%c\\%Y %H','N','Hour','Y',207,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1162,'yyyy\\M\\d HH',2,'%Y\\%e\\%c %H','N','Hour','Y',207,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1163,'yyyy\\d\\M HH',2,'%Y\\%c\\%e %H','N','Hour','Y',207,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1164,'yyyy/d/M HH',2,'%Y/%c/%e %H','N','Hour','Y',207,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1165,'yyyy-d-M HH',2,'%Y-%c-%e %H','N','Hour','Y',207,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1166,'yyyy-M-d HH:mm:ss',2,'%Y-%e-%c %H:%i:%s','N','Second','Y',209,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1167,'d-M-yyyy HH:mm:ss',2,'%c-%e-%Y %H:%i:%s','N','Second','Y',209,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1168,'M-d-yyyy HH:mm:ss',2,'%e-%c-%Y %H:%i:%s','N','Second','Y',209,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1169,'yyyy/M/d HH:mm:ss',2,'%Y/%e/%c %H:%i:%s','N','Second','Y',209,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1170,'d/M/yyyy HH:mm:ss',2,'%c/%e/%Y %H:%i:%s','N','Second','Y',209,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1171,'M/d/yyyy HH:mm:ss',2,'%e/%c/%Y %H:%i:%s','N','Second','Y',209,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1172,'d\\M\\yyyy HH:mm:ss',2,'%c\\%e\\%Y %H:%i:%s','N','Second','Y',209,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1173,'M\\d\\yyyy HH:mm:ss',2,'%e\\%c\\%Y %H:%i:%s','N','Second','Y',209,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1174,'yyyy\\M\\d HH:mm:ss',2,'%Y\\%e\\%c %H:%i:%s','N','Second','Y',209,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1175,'yyyy\\d\\M HH:mm:ss',2,'%Y\\%c\\%e %H:%i:%s','N','Second','Y',209,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1176,'yyyy/d/M HH:mm:ss',2,'%Y/%c/%e %H:%i:%s','N','Second','Y',209,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1177,'yyyy-d-M HH:mm:ss',2,'%Y-%c-%e %H:%i:%s','N','Second','Y',209,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1178,'dd-MMM-yy',2,'%d-%b%-y','N','DAY','Y',206,'DATE','N');
INSERT INTO `date_format` VALUES (1179,'dd-MMM-yyyy',2,'%d-%b-%Y','N','Day','Y',206,'DATE','Y');
INSERT INTO `date_format` VALUES (1180,'dd-MMM-yyyy',1,'DD-Mon-YYYY','N','Day','Y',206,'DATE','Y');
INSERT INTO `date_format` VALUES (1181,'dd-MMM-yyyy HH:mm:ss',2,'%d-%b-%Y %H:%i:%s','N','Second','Y',209,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1182,'dd-MMM-yyyy HH:mm:ss',1,'DD-Mon-YYYY HH24:MI:SS','N','Second','Y',209,'DATETIME','Y');
INSERT INTO `date_format` VALUES (1183,'yyyy MMM',8,'MONYY8.','N','Month','Y',204,'VARCHAR','Y');
INSERT INTO `date_format` VALUES (1184,'yyyy-MM-dd',8,'yymmdd10.','N','Day','Y',206,'DATE','Y');
INSERT INTO `date_format` VALUES (1185,'yyyy MMM',2,'%Y %b','N','Month','Y',204,'VARCHAR','Y');
INSERT INTO `date_format` VALUES (1186,'yyyyMMdd',2,'%Y%m%d','Y','Day','Y',206,'INT','Y');
INSERT INTO `date_format` VALUES (1187,'dd/MM/yyyy',11,'DD/MM/YYYY','N','Day','Y',206,'DATE','Y');
INSERT INTO `date_format` VALUES (1188,'dd/MM/yyyy HH:mm:ss',11,'DD/MM/YYYY HH24:MI:SS','N','Second','Y',208,'DATETIME','Y');
/*!40000 ALTER TABLE `date_format` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `default_conversion_detail`
--

DROP TABLE IF EXISTS `default_conversion_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `default_conversion_detail` (
  `ID` bigint(20) NOT NULL,
  `TYPE` varchar(35) NOT NULL,
  `UNIT` varchar(35) NOT NULL,
  `UNIT_DISPLAY` varchar(55) NOT NULL,
  `FORMAT` varchar(35) DEFAULT NULL,
  `ORDR` int(2) NOT NULL DEFAULT '0',
  `BASE` char(1) DEFAULT NULL,
  `value_realization_inst_be_id` bigint(20) DEFAULT NULL,
  `value_realization_be_id` bigint(20) DEFAULT NULL,
  `detail_type_be_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `IDX_DCD_TY` (`TYPE`),
  KEY `IDX_DCD_BEID` (`value_realization_inst_be_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `default_conversion_detail`
--

LOCK TABLES `default_conversion_detail` WRITE;
/*!40000 ALTER TABLE `default_conversion_detail` DISABLE KEYS */;
INSERT INTO `default_conversion_detail` VALUES (1,'DATE','YEAR','Year',NULL,0,'Y',NULL,NULL,202);
INSERT INTO `default_conversion_detail` VALUES (2,'DATE','QUARTER','Quarter',NULL,1,'N',NULL,NULL,203);
INSERT INTO `default_conversion_detail` VALUES (3,'DATE','MONTH','Month',NULL,2,'N',NULL,NULL,204);
INSERT INTO `default_conversion_detail` VALUES (21,'CURRENCY','DOLLAR','Dollar',NULL,0,'Y',5001,152,NULL);
INSERT INTO `default_conversion_detail` VALUES (22,'CURRENCY','EURO','Euro',NULL,1,'N',5002,152,NULL);
INSERT INTO `default_conversion_detail` VALUES (43,'DISTANCE','MILE','Mile',NULL,2,'N',5304,154,NULL);
INSERT INTO `default_conversion_detail` VALUES (61,'NUMBER','ONE','One','ONE',0,'Y',2006,157,NULL);
INSERT INTO `default_conversion_detail` VALUES (62,'NUMBER','MILLION','Million','MILLION',3,'N',2003,157,NULL);
INSERT INTO `default_conversion_detail` VALUES (63,'NUMBER','BILLION','Billion','BILLION',4,'N',2004,157,NULL);
INSERT INTO `default_conversion_detail` VALUES (64,'NUMBER','TRILLION','Trillion','TRILLION',5,'N',2005,157,NULL);
INSERT INTO `default_conversion_detail` VALUES (65,'NUMBER','HUNDRED','Hundred','HUNDRED',1,'N',2001,157,NULL);
INSERT INTO `default_conversion_detail` VALUES (66,'NUMBER','THOUSAND','Thousand','THOUSAND',2,'N',2002,157,NULL);
INSERT INTO `default_conversion_detail` VALUES (81,'TEMPERATURE','FAHRENHEIT','Fahrenheit',NULL,0,'Y',NULL,158,NULL);
INSERT INTO `default_conversion_detail` VALUES (82,'TEMPERATURE','CELSIUS','Celsius',NULL,1,'N',NULL,158,NULL);
INSERT INTO `default_conversion_detail` VALUES (101,'LOCATION','COUNTRY','Country',NULL,0,'Y',NULL,NULL,302);
INSERT INTO `default_conversion_detail` VALUES (102,'LOCATION','STATE','State',NULL,1,'N',NULL,NULL,303);
INSERT INTO `default_conversion_detail` VALUES (103,'LOCATION','CITY','City',NULL,2,'N',NULL,NULL,304);
INSERT INTO `default_conversion_detail` VALUES (90,'PERCENTAGE','PERCENTAGE','Percentage','PERCENTAGE',0,'Y',NULL,159,NULL);
INSERT INTO `default_conversion_detail` VALUES (4,'DATE','WEEK','Week',NULL,3,'N',NULL,NULL,205);
INSERT INTO `default_conversion_detail` VALUES (5,'DATE','DAY','Day',NULL,4,'N',NULL,NULL,206);
INSERT INTO `default_conversion_detail` VALUES (6,'DATE','HOUR','Hour',NULL,5,'N',NULL,NULL,207);
INSERT INTO `default_conversion_detail` VALUES (7,'DATE','MINUTE','Minute',NULL,6,'N',NULL,NULL,208);
INSERT INTO `default_conversion_detail` VALUES (8,'DATE','SECOND','Second',NULL,7,'N',NULL,NULL,209);
INSERT INTO `default_conversion_detail` VALUES (104,'LOCATION','COUNTY','County',NULL,3,'N',NULL,NULL,305);
INSERT INTO `default_conversion_detail` VALUES (105,'LOCATION','STREET','Street',NULL,4,'N',NULL,NULL,306);
INSERT INTO `default_conversion_detail` VALUES (106,'LOCATION','ZIP','Zip',NULL,5,'N',NULL,NULL,307);
INSERT INTO `default_conversion_detail` VALUES (107,'LOCATION','ADDRESS','Address',NULL,6,'N',NULL,NULL,308);
INSERT INTO `default_conversion_detail` VALUES (108,'LOCATION','REGION','Region',NULL,7,'N',NULL,NULL,309);
INSERT INTO `default_conversion_detail` VALUES (201,'VOLUME','CUBICMETER','CubicMeter',NULL,1,'N',5302,155,NULL);
INSERT INTO `default_conversion_detail` VALUES (301,'POWER','WATT','Watt',NULL,1,'N',5452,156,NULL);
INSERT INTO `default_conversion_detail` VALUES (302,'POWER','KILOWATT','Kilowatt',NULL,0,'Y',5453,156,NULL);
INSERT INTO `default_conversion_detail` VALUES (401,'WEIGHT','POUND','Pound',NULL,0,'Y',5202,160,NULL);
INSERT INTO `default_conversion_detail` VALUES (402,'WEIGHT','KILO','Kilo',NULL,1,'N',5201,160,NULL);
INSERT INTO `default_conversion_detail` VALUES (41,'DISTANCE','METER','Meter',NULL,0,'Y',5301,154,NULL);
INSERT INTO `default_conversion_detail` VALUES (42,'DISTANCE','KILOMETER','Kilo Meter',NULL,1,'N',5303,154,NULL);
INSERT INTO `default_conversion_detail` VALUES (44,'DISTANCE','FOOT','Foot',NULL,3,'N',5305,154,NULL);
INSERT INTO `default_conversion_detail` VALUES (45,'DISTANCE','YARD','Yard',NULL,4,'N',5306,154,NULL);
INSERT INTO `default_conversion_detail` VALUES (46,'DISTANCE','Inch','Inch',NULL,4,'N',5302,154,NULL);
INSERT INTO `default_conversion_detail` VALUES (303,'POWER','HORSEPOWER','HorsePower',NULL,2,'N',5451,156,NULL);
INSERT INTO `default_conversion_detail` VALUES (202,'VOLUME','CUBICCENTIMETER','CubicCentiMeter',NULL,0,'Y',5301,155,NULL);
INSERT INTO `default_conversion_detail` VALUES (203,'VOLUME','LITER','Liter',NULL,2,'N',5303,155,NULL);
INSERT INTO `default_conversion_detail` VALUES (501,'TIMEDURATION','MONTH','Month',NULL,0,'Y',204,161,NULL);
INSERT INTO `default_conversion_detail` VALUES (502,'TIMEDURATION','YEAR','Year',NULL,1,'N',202,161,NULL);
INSERT INTO `default_conversion_detail` VALUES (503,'TIMEDURATION','DAY','Day',NULL,2,'N',206,161,NULL);
INSERT INTO `default_conversion_detail` VALUES (504,'TIMEDURATION','WEEK','Week',NULL,3,'N',205,161,NULL);
INSERT INTO `default_conversion_detail` VALUES (505,'AREA','SQUARE METER','Square Meter',NULL,0,'Y',416,414,NULL);
INSERT INTO `default_conversion_detail` VALUES (506,'AREA','SQUARE INCH','Square Inch',NULL,1,'N',417,414,NULL);
INSERT INTO `default_conversion_detail` VALUES (507,'AREA','SQUARE FEET','Square Feet',NULL,2,'N',418,414,NULL);
INSERT INTO `default_conversion_detail` VALUES (508,'AREA','SQUARE YARD','Square Yard',NULL,3,'N',419,414,NULL);
INSERT INTO `default_conversion_detail` VALUES (509,'AREA','ACRE','Acre',NULL,4,'N',420,414,NULL);
INSERT INTO `default_conversion_detail` VALUES (510,'AREA','HECTARE','Hectare',NULL,5,'N',421,414,NULL);
INSERT INTO `default_conversion_detail` VALUES (511,'MEMORY','KILOBYTES','KiloBytes',NULL,0,'Y',426,422,NULL);
INSERT INTO `default_conversion_detail` VALUES (512,'MEMORY','MEGABYTES','MegaBytes',NULL,1,'N',424,422,NULL);
INSERT INTO `default_conversion_detail` VALUES (513,'MEMORY','GIGABYTES','GigaBytes',NULL,2,'N',425,422,NULL);
INSERT INTO `default_conversion_detail` VALUES (514,'RESOLUTION','PIXELS','Pixels',NULL,0,'Y',429,427,NULL);
INSERT INTO `default_conversion_detail` VALUES (515,'RESOLUTION','MEGAPIXELS','Megapixels',NULL,1,'N',430,427,NULL);
/*!40000 ALTER TABLE `default_conversion_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `default_dynamic_value`
--

DROP TABLE IF EXISTS `default_dynamic_value`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `default_dynamic_value` (
  `ID` bigint(20) NOT NULL,
  `ASSET_ID` bigint(20) NOT NULL,
  `BE_ID` bigint(20) NOT NULL,
  `QUALIFIER` varchar(55) NOT NULL,
  `DEFAULT_VALUE` varchar(35) NOT NULL,
  KEY `IDX_DDV_AID` (`ASSET_ID`),
  KEY `IDX_DDV_BEID` (`BE_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `default_dynamic_value`
--

LOCK TABLES `default_dynamic_value` WRITE;
/*!40000 ALTER TABLE `default_dynamic_value` DISABLE KEYS */;
/*!40000 ALTER TABLE `default_dynamic_value` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `default_instance_value`
--

DROP TABLE IF EXISTS `default_instance_value`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `default_instance_value` (
  `ID` bigint(20) NOT NULL,
  `BE_ID` bigint(20) DEFAULT NULL,
  `DEFAULT_VALUE` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_DIV_DID` (`BE_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `default_instance_value`
--

LOCK TABLES `default_instance_value` WRITE;
/*!40000 ALTER TABLE `default_instance_value` DISABLE KEYS */;
/*!40000 ALTER TABLE `default_instance_value` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `default_metric`
--

DROP TABLE IF EXISTS `default_metric`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `default_metric` (
  `ID` bigint(20) NOT NULL,
  `mapping_id` bigint(20) DEFAULT NULL,
  `TABLE_ID` bigint(20) NOT NULL,
  `popularity` bigint(20) DEFAULT NULL,
  `colum_aed_id` bigint(20) NOT NULL,
  `valid` char(1) NOT NULL DEFAULT 'N',
  PRIMARY KEY (`ID`),
  KEY `DM_TABLE_ID_FK` (`TABLE_ID`),
  KEY `DM_MAPPING_ID_FK1` (`mapping_id`),
  KEY `IDX_DM_CAEID` (`colum_aed_id`),
  KEY `IDX_DM_VF` (`valid`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `default_metric`
--

LOCK TABLES `default_metric` WRITE;
/*!40000 ALTER TABLE `default_metric` DISABLE KEYS */;
/*!40000 ALTER TABLE `default_metric` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `eas_index`
--

DROP TABLE IF EXISTS `eas_index`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eas_index` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `token` varchar(255) NOT NULL,
  `token_weight` double(5,2) NOT NULL DEFAULT '1.00',
  `appearances` int(10) NOT NULL DEFAULT '1',
  `model_group_id` int(20) NOT NULL,
  `app_id` int(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_ei_token` (`token`),
  KEY `idx_ei_mgi` (`model_group_id`),
  KEY `idx_ei_app` (`app_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `eas_index`
--

LOCK TABLES `eas_index` WRITE;
/*!40000 ALTER TABLE `eas_index` DISABLE KEYS */;
/*!40000 ALTER TABLE `eas_index` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `eas_pre_index`
--

DROP TABLE IF EXISTS `eas_pre_index`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eas_pre_index` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `token` varchar(255) NOT NULL,
  `sfl_token_id` int(20) DEFAULT NULL,
  `sfl_weight_percentage` double(5,2) DEFAULT NULL,
  `entity_type` varchar(15) NOT NULL DEFAULT 'C',
  `entity_bed_id` int(20) NOT NULL,
  `entity_weight` double(5,2) NOT NULL DEFAULT '1.00',
  `source_type` varchar(15) NOT NULL DEFAULT 'ENTITY',
  `token_weight` double(5,2) NOT NULL DEFAULT '1.00',
  `model_group_id` int(20) NOT NULL,
  `app_id` int(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_epi_token` (`token`),
  KEY `idx_epi_mgi` (`model_group_id`),
  KEY `idx_epi_st` (`source_type`),
  KEY `idx_epi_et` (`entity_type`),
  KEY `idx_epi_ebi` (`entity_bed_id`),
  KEY `idx_epi_sti` (`sfl_token_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `eas_pre_index`
--

LOCK TABLES `eas_pre_index` WRITE;
/*!40000 ALTER TABLE `eas_pre_index` DISABLE KEYS */;
/*!40000 ALTER TABLE `eas_pre_index` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `entity_behavior`
--

DROP TABLE IF EXISTS `entity_behavior`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `entity_behavior` (
  `ID` bigint(20) NOT NULL,
  `ENTITY_BE_ID` bigint(20) DEFAULT NULL,
  `BEHAVIOR_BE_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `IDX_EB_BBEID` (`BEHAVIOR_BE_ID`),
  KEY `IDX_EB_EBEID` (`ENTITY_BE_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `entity_behavior`
--

LOCK TABLES `entity_behavior` WRITE;
/*!40000 ALTER TABLE `entity_behavior` DISABLE KEYS */;
INSERT INTO `entity_behavior` VALUES (101,152,9001);
INSERT INTO `entity_behavior` VALUES (102,154,9001);
INSERT INTO `entity_behavior` VALUES (103,155,9001);
INSERT INTO `entity_behavior` VALUES (104,156,9001);
INSERT INTO `entity_behavior` VALUES (105,152,9002);
INSERT INTO `entity_behavior` VALUES (106,152,9003);
INSERT INTO `entity_behavior` VALUES (119,158,9001);
INSERT INTO `entity_behavior` VALUES (108,154,9002);
INSERT INTO `entity_behavior` VALUES (109,154,9003);
INSERT INTO `entity_behavior` VALUES (118,157,9003);
INSERT INTO `entity_behavior` VALUES (111,155,9002);
INSERT INTO `entity_behavior` VALUES (112,155,9003);
INSERT INTO `entity_behavior` VALUES (117,157,9002);
INSERT INTO `entity_behavior` VALUES (114,156,9002);
INSERT INTO `entity_behavior` VALUES (115,156,9003);
INSERT INTO `entity_behavior` VALUES (116,157,9001);
INSERT INTO `entity_behavior` VALUES (120,158,9002);
INSERT INTO `entity_behavior` VALUES (121,158,9003);
INSERT INTO `entity_behavior` VALUES (122,159,9001);
INSERT INTO `entity_behavior` VALUES (123,159,9002);
INSERT INTO `entity_behavior` VALUES (124,159,9003);
INSERT INTO `entity_behavior` VALUES (125,160,9001);
INSERT INTO `entity_behavior` VALUES (126,160,9002);
INSERT INTO `entity_behavior` VALUES (127,160,9003);
/*!40000 ALTER TABLE `entity_behavior` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `entity_detail_type`
--

DROP TABLE IF EXISTS `entity_detail_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `entity_detail_type` (
  `ID` bigint(20) NOT NULL,
  `ENTITY_BE_ID` bigint(20) NOT NULL,
  `DETAIL_TYPE_BE_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `IDX_EDT_EBI` (`ENTITY_BE_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `entity_detail_type`
--

LOCK TABLES `entity_detail_type` WRITE;
/*!40000 ALTER TABLE `entity_detail_type` DISABLE KEYS */;
/*!40000 ALTER TABLE `entity_detail_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `entity_triple_definition`
--

DROP TABLE IF EXISTS `entity_triple_definition`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `entity_triple_definition` (
  `ID` bigint(20) NOT NULL,
  `SOURCE_BE_ID` bigint(20) NOT NULL,
  `RELATION_BE_ID` bigint(20) NOT NULL,
  `DESTINATION_BE_ID` bigint(20) NOT NULL,
  `CARDINALITY` int(2) DEFAULT '1',
  `FUNCTIONAL` int(1) DEFAULT '0',
  `INVERSE_FUNCTIONAL` int(1) DEFAULT '0',
  `RELATION_SPECIFIED` int(1) DEFAULT '0',
  `TRIPLE_TYPE` varchar(10) NOT NULL,
  `property_type` varchar(15) NOT NULL,
  `DEFAULT_VALUE` varchar(255) DEFAULT NULL,
  `BASE_ETD_ID` bigint(20) DEFAULT NULL,
  `origin` char(2) DEFAULT NULL,
  `INSTANCE_TRIPLE_EXISTS` char(1) DEFAULT 'N',
  PRIMARY KEY (`ID`),
  KEY `FK_ETD_SBID` (`SOURCE_BE_ID`),
  KEY `FK_ETD_RBID` (`RELATION_BE_ID`),
  KEY `FK_ETD_DBID` (`DESTINATION_BE_ID`),
  KEY `Idx_ETD_BASE_ETD_ID` (`BASE_ETD_ID`),
  KEY `Idx_ETD_TRIPLE_TYPE` (`TRIPLE_TYPE`),
  KEY `Idx_ETD_property_type` (`property_type`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `entity_triple_definition`
--

LOCK TABLES `entity_triple_definition` WRITE;
/*!40000 ALTER TABLE `entity_triple_definition` DISABLE KEYS */;
INSERT INTO `entity_triple_definition` VALUES (1,151,507,401,1,0,0,0,'TRT','NORMAL',NULL,NULL,'H','N');
INSERT INTO `entity_triple_definition` VALUES (2,151,507,107,1,0,0,0,'TRT','NORMAL',NULL,NULL,'H','N');
INSERT INTO `entity_triple_definition` VALUES (3,151,507,106,1,0,0,0,'TRT','NORMAL',NULL,NULL,'H','N');
INSERT INTO `entity_triple_definition` VALUES (4,152,507,151,1,0,0,0,'TRT','NORMAL',NULL,NULL,'H','N');
INSERT INTO `entity_triple_definition` VALUES (5,152,507,105,1,0,0,0,'TRT','NORMAL',NULL,NULL,'H','N');
INSERT INTO `entity_triple_definition` VALUES (6,203,507,401,1,0,0,0,'TRT','NORMAL',NULL,NULL,'H','N');
INSERT INTO `entity_triple_definition` VALUES (7,202,507,401,1,0,0,0,'TRT','NORMAL',NULL,NULL,'H','N');
INSERT INTO `entity_triple_definition` VALUES (8,152,507,409,1,0,0,0,'TRT','NORMAL',NULL,NULL,'H','N');
INSERT INTO `entity_triple_definition` VALUES (9,152,507,408,1,0,0,0,'TRT','NORMAL',NULL,NULL,'H','N');
INSERT INTO `entity_triple_definition` VALUES (10,201,507,409,1,0,0,0,'TRT','NORMAL',NULL,NULL,'H','N');
INSERT INTO `entity_triple_definition` VALUES (11,201,507,408,1,0,0,0,'TRT','NORMAL',NULL,NULL,'H','N');
INSERT INTO `entity_triple_definition` VALUES (12,152,507,404,1,0,0,0,'TRT','NORMAL',NULL,NULL,'H','N');
INSERT INTO `entity_triple_definition` VALUES (13,152,507,405,1,0,0,0,'TRT','NORMAL',NULL,NULL,'H','N');
INSERT INTO `entity_triple_definition` VALUES (14,201,507,404,1,0,0,0,'TRT','NORMAL',NULL,NULL,'H','N');
INSERT INTO `entity_triple_definition` VALUES (15,201,507,405,1,0,0,0,'TRT','NORMAL',NULL,NULL,'H','N');
INSERT INTO `entity_triple_definition` VALUES (16,201,507,204,1,0,0,0,'TRT','NORMAL',NULL,NULL,'H','N');
INSERT INTO `entity_triple_definition` VALUES (17,201,507,202,1,0,0,0,'TRT','NORMAL',NULL,NULL,'H','N');
INSERT INTO `entity_triple_definition` VALUES (18,201,507,203,1,0,0,0,'TRT','NORMAL',NULL,NULL,'H','N');
INSERT INTO `entity_triple_definition` VALUES (19,201,507,403,1,0,0,0,'TRT','NORMAL',NULL,NULL,'H','N');
INSERT INTO `entity_triple_definition` VALUES (20,201,507,401,1,0,0,0,'TRT','NORMAL',NULL,NULL,'H','N');
INSERT INTO `entity_triple_definition` VALUES (21,110,507,401,1,0,0,0,'TRT','NORMAL',NULL,NULL,'H','N');
INSERT INTO `entity_triple_definition` VALUES (22,110,507,104,1,0,0,0,'TRT','NORMAL',NULL,NULL,'H','N');
INSERT INTO `entity_triple_definition` VALUES (23,301,506,302,1,0,0,0,'TRT','NORMAL',NULL,NULL,'H','N');
INSERT INTO `entity_triple_definition` VALUES (24,301,506,303,1,0,0,0,'TRT','NORMAL',NULL,NULL,'H','N');
INSERT INTO `entity_triple_definition` VALUES (25,301,506,304,1,0,0,0,'TRT','NORMAL',NULL,NULL,'H','N');
INSERT INTO `entity_triple_definition` VALUES (26,302,510,303,1,0,0,0,'TRT','NORMAL',NULL,NULL,'H','N');
INSERT INTO `entity_triple_definition` VALUES (27,302,510,304,1,0,0,0,'TRT','NORMAL',NULL,NULL,'H','N');
INSERT INTO `entity_triple_definition` VALUES (28,303,510,304,1,0,0,0,'TRT','NORMAL',NULL,NULL,'H','N');
INSERT INTO `entity_triple_definition` VALUES (29,303,510,302,1,0,0,0,'TRT','NORMAL',NULL,NULL,'H','N');
INSERT INTO `entity_triple_definition` VALUES (30,304,510,302,1,0,0,0,'TRT','NORMAL',NULL,NULL,'H','N');
INSERT INTO `entity_triple_definition` VALUES (31,304,510,303,1,0,0,0,'TRT','NORMAL',NULL,NULL,'H','N');
/*!40000 ALTER TABLE `entity_triple_definition` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `enum_lookup`
--

DROP TABLE IF EXISTS `enum_lookup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `enum_lookup` (
  `ID` bigint(20) NOT NULL,
  `TYPE` varchar(255) NOT NULL COMMENT 'Enum class name of the lookup',
  `VALUE` varchar(255) DEFAULT NULL COMMENT 'Value of the constant of the TYPE be represented internally',
  `NAME` varchar(255) NOT NULL COMMENT 'Name of the constant of the TYPE',
  `DESCRIPTION` varchar(255) NOT NULL COMMENT 'Description of the constant of the TYPE',
  `DISPLAY_ORDER` int(4) NOT NULL COMMENT 'Display Order of the NAMEs in a particular type',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_EL_TYPE_NAME` (`TYPE`,`NAME`),
  UNIQUE KEY `UK_EL_TYPE_VALUE` (`TYPE`,`VALUE`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COMMENT='Enum based lookup type defitions';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `enum_lookup`
--

LOCK TABLES `enum_lookup` WRITE;
/*!40000 ALTER TABLE `enum_lookup` DISABLE KEYS */;
INSERT INTO `enum_lookup` VALUES (2951,'DisplayableFeatureAlignmentType','left','LEFT','Left',1);
INSERT INTO `enum_lookup` VALUES (2952,'DisplayableFeatureAlignmentType','center','CENTER','Center',2);
INSERT INTO `enum_lookup` VALUES (2953,'DisplayableFeatureAlignmentType','right','RIGHT','Right',3);
INSERT INTO `enum_lookup` VALUES (7751,'WeightReductionPartType','ALLOWED','ALLOWED','Allowed',1);
INSERT INTO `enum_lookup` VALUES (7752,'WeightReductionPartType','NOT_ALLOWED','NOT_ALLOWED','Not Allowed',2);
INSERT INTO `enum_lookup` VALUES (5301,'PublishAssetMode','1','LOCAL','Locally',1);
INSERT INTO `enum_lookup` VALUES (5302,'PublishAssetMode','2','COMMUNITY','To Everyone',2);
INSERT INTO `enum_lookup` VALUES (5303,'PublishAssetMode','0','NONE','Not Published',3);
INSERT INTO `enum_lookup` VALUES (7451,'SolrOperatorType','E','EQUALS','Equals',1);
INSERT INTO `enum_lookup` VALUES (7452,'SolrOperatorType','I','IN','In',2);
INSERT INTO `enum_lookup` VALUES (7453,'SolrOperatorType','B','BETWEEN','Between',3);
INSERT INTO `enum_lookup` VALUES (5101,'PopularityTermType','1','APPLICATION','Application',1);
INSERT INTO `enum_lookup` VALUES (5102,'PopularityTermType','2','MODEL','Model',2);
INSERT INTO `enum_lookup` VALUES (5103,'PopularityTermType','3','ASSET','Asset',3);
INSERT INTO `enum_lookup` VALUES (5104,'PopularityTermType','4','TABL','Tabl',4);
INSERT INTO `enum_lookup` VALUES (5105,'PopularityTermType','5','COLUM','Colum',5);
INSERT INTO `enum_lookup` VALUES (5106,'PopularityTermType','6','MEMBR','Membr',6);
INSERT INTO `enum_lookup` VALUES (5107,'PopularityTermType','7','CONCEPT','Concept',7);
INSERT INTO `enum_lookup` VALUES (5108,'PopularityTermType','8','INSTANCE','Instance',8);
INSERT INTO `enum_lookup` VALUES (5109,'PopularityTermType','9','SFL_TERM_TOKEN','Sfl Term Token',9);
INSERT INTO `enum_lookup` VALUES (2851,'DataType','CHAR','CHARACTER','Character',1);
INSERT INTO `enum_lookup` VALUES (2852,'DataType','DECIMAL','NUMBER','Number',2);
INSERT INTO `enum_lookup` VALUES (2853,'DataType','TIME','TIME','Time',3);
INSERT INTO `enum_lookup` VALUES (2854,'DataType','DATE','DATE','Date',4);
INSERT INTO `enum_lookup` VALUES (2855,'DataType','DATETIME','DATETIME','Datetime',5);
INSERT INTO `enum_lookup` VALUES (2856,'DataType','INT','INT','Int',6);
INSERT INTO `enum_lookup` VALUES (2857,'DataType','VARCHAR','STRING','String',7);
INSERT INTO `enum_lookup` VALUES (2858,'DataType','BIGINT','LARGE_INTEGER','Large Integer',8);
INSERT INTO `enum_lookup` VALUES (2859,'DataType','BLOB','BLOB','Blob',9);
INSERT INTO `enum_lookup` VALUES (2860,'DataType','TEXT','TEXT','Text',10);
INSERT INTO `enum_lookup` VALUES (6501,'SamplingStrategy','M','MEAN','Mean',1);
INSERT INTO `enum_lookup` VALUES (6502,'SamplingStrategy','P','PROPORTION','Proportion',2);
INSERT INTO `enum_lookup` VALUES (6503,'SamplingStrategy','S','SUB_GROUPING','Sub Grouping',3);
INSERT INTO `enum_lookup` VALUES (6504,'SamplingStrategy','C','CATEGORICAL','Categorical',4);
INSERT INTO `enum_lookup` VALUES (4401,'OperandType','VALUE','VALUE','Value',1);
INSERT INTO `enum_lookup` VALUES (4402,'OperandType','BUSINESSTERM','BUSINESS_TERM','Business Term',2);
INSERT INTO `enum_lookup` VALUES (4403,'OperandType','BUSINESSQUERY','BUSINESS_QUERY','Business Query',3);
INSERT INTO `enum_lookup` VALUES (4404,'OperandType','DYNAMIC','DYNAMIC','Dynamic',4);
INSERT INTO `enum_lookup` VALUES (1451,'AssetType','3','Relational','Relational',1);
INSERT INTO `enum_lookup` VALUES (1452,'AssetType','1','Cube','Cube',2);
INSERT INTO `enum_lookup` VALUES (1453,'AssetType','2','Mart','Mart',3);
INSERT INTO `enum_lookup` VALUES (5151,'PrimaryMappingType','P','PRIMARY','Primary',1);
INSERT INTO `enum_lookup` VALUES (5152,'PrimaryMappingType','N','NON_PRIMARY','Non Primary',2);
INSERT INTO `enum_lookup` VALUES (5153,'PrimaryMappingType','M','MEASURE_NON_PRIMARY','Measure Non Primary',3);
INSERT INTO `enum_lookup` VALUES (3401,'FeatureValueType','S','VALUE_STRING','Value String',1);
INSERT INTO `enum_lookup` VALUES (3402,'FeatureValueType','N','VALUE_NUMBER','Value Number',2);
INSERT INTO `enum_lookup` VALUES (3403,'FeatureValueType','D','VALUE_DUMMY','Value Dummy',3);
INSERT INTO `enum_lookup` VALUES (3404,'FeatureValueType','DT','VALUE_DATE','Value Date',4);
INSERT INTO `enum_lookup` VALUES (4701,'OrderLimitEntityType','TOP','TOP','Top',1);
INSERT INTO `enum_lookup` VALUES (4702,'OrderLimitEntityType','BOTTOM','BOTTOM','Bottom',2);
INSERT INTO `enum_lookup` VALUES (4751,'OriginType','U','USER','User',1);
INSERT INTO `enum_lookup` VALUES (4752,'OriginType','H','Hierarchy','Hierarchy',2);
INSERT INTO `enum_lookup` VALUES (4753,'OriginType','T','TYPE','Type',3);
INSERT INTO `enum_lookup` VALUES (6451,'RFXVariationSubType','1','SUBJECTINSTANCE','Subjectinstance',1);
INSERT INTO `enum_lookup` VALUES (6452,'RFXVariationSubType','2','OBJECTINSTANCE','Objectinstance',2);
INSERT INTO `enum_lookup` VALUES (6453,'RFXVariationSubType','3','RELATION','Relation',3);
INSERT INTO `enum_lookup` VALUES (6454,'RFXVariationSubType','4','SUBJECTINSTANCE_RELATION','Subjectinstance Relation',4);
INSERT INTO `enum_lookup` VALUES (6455,'RFXVariationSubType','5','RELATION_OBJECTINSTANCE','Relation Objectinstance',5);
INSERT INTO `enum_lookup` VALUES (6456,'RFXVariationSubType','6','SUBJECTINSTANCE_OBJECTINSTANCE','Subjectinstance Objectinstance',6);
INSERT INTO `enum_lookup` VALUES (6457,'RFXVariationSubType','7','SUBJECTINSTANCE_RELATION_OBJECTINSTANCE','Subjectinstance Relation Objectinstance',7);
INSERT INTO `enum_lookup` VALUES (6458,'RFXVariationSubType','8','SUBJECTINSTANCE_RELATION_OBJECTCONCEPT','Subjectinstance Relation Objectconcept',8);
INSERT INTO `enum_lookup` VALUES (6459,'RFXVariationSubType','9','SUBJECTCONCEPT_RELATION_OBJECTINSTANCE','Subjectconcept Relation Objectinstance',9);
INSERT INTO `enum_lookup` VALUES (6460,'RFXVariationSubType','10','SUBJECTCONCEPT_RELATION_OBJECTCONCEPT','Subjectconcept Relation Objectconcept',10);
INSERT INTO `enum_lookup` VALUES (6461,'RFXVariationSubType','11','SUBJECTCONCEPT_RELATION','Subjectconcept Relation',11);
INSERT INTO `enum_lookup` VALUES (6462,'RFXVariationSubType','12','SUBJECTCONCEPT_OBJECTINSTANCE','Subjectconcept Objectinstance',12);
INSERT INTO `enum_lookup` VALUES (6463,'RFXVariationSubType','13','SUBJECTINSTANCE_OBJECTCONCEPT','Subjectinstance Objectconcept',13);
INSERT INTO `enum_lookup` VALUES (6464,'RFXVariationSubType','14','RELATION_OBJECTCONCEPT','Relation Objectconcept',14);
INSERT INTO `enum_lookup` VALUES (6465,'RFXVariationSubType','15','SUBJECTCONCEPT_OBJECTCONCEPT','Subjectconcept Objectconcept',15);
INSERT INTO `enum_lookup` VALUES (6466,'RFXVariationSubType','16','SUBJECTCONCEPT','Subjectconcept',16);
INSERT INTO `enum_lookup` VALUES (6467,'RFXVariationSubType','17','OBJECTCONCEPT','Objectconcept',17);
INSERT INTO `enum_lookup` VALUES (6551,'SearchFilterType','1','APP','App',1);
INSERT INTO `enum_lookup` VALUES (6552,'SearchFilterType','2','VERTICAL','Vertical',2);
INSERT INTO `enum_lookup` VALUES (6553,'SearchFilterType','3','GENERAL','General',3);
INSERT INTO `enum_lookup` VALUES (6554,'SearchFilterType','4','APP_SCOPED','App Scoped',4);
INSERT INTO `enum_lookup` VALUES (3901,'LocationType','1','LAT_LONG','Lat Long',1);
INSERT INTO `enum_lookup` VALUES (3902,'LocationType','2','ZIPCODE','Zipcode',2);
INSERT INTO `enum_lookup` VALUES (3903,'LocationType','3','CITY','City',3);
INSERT INTO `enum_lookup` VALUES (3904,'LocationType','4','STATE','State',4);
INSERT INTO `enum_lookup` VALUES (3905,'LocationType','5','COUNTRY','Country',5);
INSERT INTO `enum_lookup` VALUES (3906,'LocationType','6','COUNTY','County',6);
INSERT INTO `enum_lookup` VALUES (2451,'ConstraintSubType','E','ENTIRE','Entire',1);
INSERT INTO `enum_lookup` VALUES (2452,'ConstraintSubType','P','PART','Part',2);
INSERT INTO `enum_lookup` VALUES (251,'NormalizedDataType','com.execue.core.common.bean.nlp.TimeFrameNormalizedData','TIME_FRAME_NORMALIZED_DATA','Time Frame Normalized Data',1);
INSERT INTO `enum_lookup` VALUES (252,'NormalizedDataType','com.execue.core.common.bean.nlp.ValueRealizationNormalizedData','VALUE_NORMALIZED_DATA','Value Normalized Data',2);
INSERT INTO `enum_lookup` VALUES (253,'NormalizedDataType','com.execue.core.common.bean.nlp.UnitNormalizedData','UNIT_NORMALIZED_DATA','Unit Normalized Data',3);
INSERT INTO `enum_lookup` VALUES (254,'NormalizedDataType','com.execue.core.common.bean.nlp.RangeNormalizedData','RANGE_NORMALIZED_DATA','Range Normalized Data',4);
INSERT INTO `enum_lookup` VALUES (255,'NormalizedDataType','com.execue.core.common.bean.nlp.RelativeTimeNormalizedData','RELATIVE_TIME_NORMALIZED_DATA','Relative Time Normalized Data',5);
INSERT INTO `enum_lookup` VALUES (256,'NormalizedDataType','com.execue.core.common.bean.nlp.ListNormalizedData','LIST_NORMALIZED_DATA','List Normalized Data',6);
INSERT INTO `enum_lookup` VALUES (257,'NormalizedDataType','com.execue.core.common.bean.nlp.DefaultNormalizedData','DEFAULT_NORMALIZED_DATA','Default Normalized Data',7);
INSERT INTO `enum_lookup` VALUES (258,'NormalizedDataType','com.execue.core.common.bean.nlp.ComparativeInfoNormalizedData','COMPARATIVE_INFO_NORMALIZED_DATA','Comparative Info Normalized Data',8);
INSERT INTO `enum_lookup` VALUES (259,'NormalizedDataType','com.execue.core.common.bean.nlp.RelativeNormalizedData','RELATIVE_NORMALIZED_DATA','Relative Normalized Data',9);
INSERT INTO `enum_lookup` VALUES (260,'NormalizedDataType','com.execue.core.common.bean.nlp.WeekNormalizedData','WEEK_NORMALIZED_DATA','Week Normalized Data',10);
INSERT INTO `enum_lookup` VALUES (261,'NormalizedDataType','com.execue.core.common.bean.nlp.WeekDayNormalizedDataComponent','WEEK_DAY_NORMALIZED_DATA','Week Day Normalized Data',11);
INSERT INTO `enum_lookup` VALUES (262,'NormalizedDataType','com.execue.core.common.bean.nlp.LocationNormalizedData','LOCATION_NORMALIZED_DATA','Location Normalized Data',12);
INSERT INTO `enum_lookup` VALUES (5701,'QueryClauseType','select','SELECT','Select',1);
INSERT INTO `enum_lookup` VALUES (5702,'QueryClauseType','from','FROM','From',2);
INSERT INTO `enum_lookup` VALUES (5703,'QueryClauseType','groupBy','GROUPBY','Groupby',3);
INSERT INTO `enum_lookup` VALUES (5704,'QueryClauseType','orderBy','ORDERBY','Orderby',4);
INSERT INTO `enum_lookup` VALUES (5705,'QueryClauseType','where','WHERE','Where',5);
INSERT INTO `enum_lookup` VALUES (5706,'QueryClauseType','having','HAVING','Having',6);
INSERT INTO `enum_lookup` VALUES (5707,'QueryClauseType','join','JOIN','Join',7);
INSERT INTO `enum_lookup` VALUES (5708,'QueryClauseType','limit','LIMIT','Limit',8);
INSERT INTO `enum_lookup` VALUES (51,'ResourceScopeType','K','KDX','Kdx',1);
INSERT INTO `enum_lookup` VALUES (52,'ResourceScopeType','S','SDX','Sdx',2);
INSERT INTO `enum_lookup` VALUES (53,'ResourceScopeType','A','ALL','Dataset Collection',3);
INSERT INTO `enum_lookup` VALUES (5951,'QuerySectionType','SELECT','SELECT','Select',1);
INSERT INTO `enum_lookup` VALUES (5952,'QuerySectionType','GROUP','GROUP','Group',2);
INSERT INTO `enum_lookup` VALUES (5953,'QuerySectionType','CONDITION','CONDITION','Condition',3);
INSERT INTO `enum_lookup` VALUES (5954,'QuerySectionType','ORDER','ORDER','Order',4);
INSERT INTO `enum_lookup` VALUES (5955,'QuerySectionType','HAVING','HAVING','Having',5);
INSERT INTO `enum_lookup` VALUES (5956,'QuerySectionType','POPULATION','POPULATION','Population',6);
INSERT INTO `enum_lookup` VALUES (5957,'QuerySectionType','TOP_BOTTOM','TOP_BOTTOM','Top Bottom',7);
INSERT INTO `enum_lookup` VALUES (5958,'QuerySectionType','COHORT_CONDITION','COHORT_CONDITION','Cohort Condition',8);
INSERT INTO `enum_lookup` VALUES (5959,'QuerySectionType','COHORT_GROUP','COHORT_GROUP','Cohort Group',9);
INSERT INTO `enum_lookup` VALUES (1551,'AssociationType','R','RELATION_ASSOCIATION','Relation Association',1);
INSERT INTO `enum_lookup` VALUES (1552,'AssociationType','C','CONVERTABLE_ASSOCIATION','Convertable Association',2);
INSERT INTO `enum_lookup` VALUES (1251,'AssetOperationType','AssetSynchronization','ASSET_SYNCHRONIZATION','Asset Synchronization',1);
INSERT INTO `enum_lookup` VALUES (1252,'AssetOperationType','AssetAnalysis','ASSET_ANALYSIS','Asset Analysis',2);
INSERT INTO `enum_lookup` VALUES (6901,'SyncRequestLevel','TABLE','TABLE','Table',1);
INSERT INTO `enum_lookup` VALUES (6902,'SyncRequestLevel','COLUMN','COLUMN','Column',2);
INSERT INTO `enum_lookup` VALUES (6903,'SyncRequestLevel','MEMBER','MEMBER','Member',3);
INSERT INTO `enum_lookup` VALUES (5901,'QueryFormulaType','static','STATIC','Static',1);
INSERT INTO `enum_lookup` VALUES (5902,'QueryFormulaType','dynamic','DYNAMIC','Dynamic',2);
INSERT INTO `enum_lookup` VALUES (5201,'ProcessingFlagType','N','NOT_PROCESSED','Not Processed',1);
INSERT INTO `enum_lookup` VALUES (5202,'ProcessingFlagType','Y','PROCESSED','Processed',2);
INSERT INTO `enum_lookup` VALUES (5203,'ProcessingFlagType','P','PROCESSING','Processing',3);
INSERT INTO `enum_lookup` VALUES (5204,'ProcessingFlagType','F','FAILED','Failed',4);
INSERT INTO `enum_lookup` VALUES (5205,'ProcessingFlagType','E','NOT_ENOUGH_INFORMATION','Not Enough Information',5);
INSERT INTO `enum_lookup` VALUES (2651,'CSVEmptyField','EMPTY','EMPTY','Empty',1);
INSERT INTO `enum_lookup` VALUES (2652,'CSVEmptyField','NULL','NULL','None',2);
INSERT INTO `enum_lookup` VALUES (2653,'CSVEmptyField','NA','NA','Na',3);
INSERT INTO `enum_lookup` VALUES (1651,'BatchProcessDetailType','TABLE_ID','TABLE','Table',1);
INSERT INTO `enum_lookup` VALUES (1652,'BatchProcessDetailType','COLUMN_ID','COLUMN','Column',2);
INSERT INTO `enum_lookup` VALUES (701,'ACManagementOperationStatusType','INITIATED','INITIATED','Initiated',1);
INSERT INTO `enum_lookup` VALUES (702,'ACManagementOperationStatusType','INACTIVATED','INACTIVATED','Inactivated',2);
INSERT INTO `enum_lookup` VALUES (703,'ACManagementOperationStatusType','PENDING','PENDING','Pending',3);
INSERT INTO `enum_lookup` VALUES (704,'ACManagementOperationStatusType','INPROGRESS','INPROGRESS','Inprogress',4);
INSERT INTO `enum_lookup` VALUES (705,'ACManagementOperationStatusType','SUCCESSFUL','SUCCESSFUL','Successful',5);
INSERT INTO `enum_lookup` VALUES (706,'ACManagementOperationStatusType','FAILED','FAILED','Failed',6);
INSERT INTO `enum_lookup` VALUES (4501,'OperationRequestLevel','SYSTEM','SYSTEM','System',1);
INSERT INTO `enum_lookup` VALUES (4502,'OperationRequestLevel','APPLICATION','APPLICATION','Application',2);
INSERT INTO `enum_lookup` VALUES (4503,'OperationRequestLevel','ASSET','ASSET','Asset',3);
INSERT INTO `enum_lookup` VALUES (7401,'SolrFieldType','N','NUMBER','Number',1);
INSERT INTO `enum_lookup` VALUES (7402,'SolrFieldType','S','STRING','String',2);
INSERT INTO `enum_lookup` VALUES (5551,'PublisherDataType','CHAR','CHARACTER','Character',1);
INSERT INTO `enum_lookup` VALUES (5552,'PublisherDataType','DECIMAL','NUMBER','Number',2);
INSERT INTO `enum_lookup` VALUES (5553,'PublisherDataType','TIME','TIME','Time',3);
INSERT INTO `enum_lookup` VALUES (5554,'PublisherDataType','DATE','DATE','Date',4);
INSERT INTO `enum_lookup` VALUES (5555,'PublisherDataType','DATETIME','DATETIME','Datetime',5);
INSERT INTO `enum_lookup` VALUES (5556,'PublisherDataType','INT','INT','Int',6);
INSERT INTO `enum_lookup` VALUES (5557,'PublisherDataType','VARCHAR','STRING','String',7);
INSERT INTO `enum_lookup` VALUES (5558,'PublisherDataType','LOCATION','LOCATION','Location',8);
INSERT INTO `enum_lookup` VALUES (5559,'PublisherDataType','BIGINT','LARGE_INTEGER','Large Integer',9);
INSERT INTO `enum_lookup` VALUES (3501,'GranularityType','NA','NA','None',1);
INSERT INTO `enum_lookup` VALUES (3502,'GranularityType','GRAIN','GRAIN','Granularity',2);
INSERT INTO `enum_lookup` VALUES (1851,'BooleanType','Y','YES','Yes',1);
INSERT INTO `enum_lookup` VALUES (1852,'BooleanType','N','NO','No',2);
INSERT INTO `enum_lookup` VALUES (7051,'TripleVariationSubType','1','SUBJECTINSTANCE','Subjectinstance',1);
INSERT INTO `enum_lookup` VALUES (7052,'TripleVariationSubType','2','OBJECTINSTANCE','Objectinstance',2);
INSERT INTO `enum_lookup` VALUES (7053,'TripleVariationSubType','3','RELATION','Relation',3);
INSERT INTO `enum_lookup` VALUES (7054,'TripleVariationSubType','4','SUBJECTINSTANCE_RELATION','Subjectinstance Relation',4);
INSERT INTO `enum_lookup` VALUES (7055,'TripleVariationSubType','5','RELATION_OBJECTINSTANCE','Relation Objectinstance',5);
INSERT INTO `enum_lookup` VALUES (7056,'TripleVariationSubType','6','SUBJECTINSTANCE_OBJECTINSTANCE','Subjectinstance Objectinstance',6);
INSERT INTO `enum_lookup` VALUES (7057,'TripleVariationSubType','7','SUBJECTINSTANCE_RELATION_OBJECTINSTANCE','Subjectinstance Relation Objectinstance',7);
INSERT INTO `enum_lookup` VALUES (7058,'TripleVariationSubType','8','SUBJECTINSTANCE_RELATION_OBJECTCONCEPT','Subjectinstance Relation Objectconcept',8);
INSERT INTO `enum_lookup` VALUES (7059,'TripleVariationSubType','9','SUBJECTCONCEPT_RELATION_OBJECTINSTANCE','Subjectconcept Relation Objectinstance',9);
INSERT INTO `enum_lookup` VALUES (7060,'TripleVariationSubType','10','SUBJECTCONCEPT_RELATION_OBJECTCONCEPT','Subjectconcept Relation Objectconcept',10);
INSERT INTO `enum_lookup` VALUES (7061,'TripleVariationSubType','11','SUBJECTCONCEPT_RELATION','Subjectconcept Relation',11);
INSERT INTO `enum_lookup` VALUES (7062,'TripleVariationSubType','12','SUBJECTCONCEPT_OBJECTINSTANCE','Subjectconcept Objectinstance',12);
INSERT INTO `enum_lookup` VALUES (7063,'TripleVariationSubType','13','SUBJECTINSTANCE_OBJECTCONCEPT','Subjectinstance Objectconcept',13);
INSERT INTO `enum_lookup` VALUES (7064,'TripleVariationSubType','14','RELATION_OBJECTCONCEPT','Relation Objectconcept',14);
INSERT INTO `enum_lookup` VALUES (7065,'TripleVariationSubType','15','SUBJECTCONCEPT_OBJECTCONCEPT','Subjectconcept Objectconcept',15);
INSERT INTO `enum_lookup` VALUES (7066,'TripleVariationSubType','16','SUBJECTCONCEPT','Subjectconcept',16);
INSERT INTO `enum_lookup` VALUES (7067,'TripleVariationSubType','17','OBJECTCONCEPT','Objectconcept',17);
INSERT INTO `enum_lookup` VALUES (451,'RecognizedType','Digit','NUMBER_TYPE','Number Type',1);
INSERT INTO `enum_lookup` VALUES (452,'RecognizedType','Day','DAY_TYPE','Day Type',2);
INSERT INTO `enum_lookup` VALUES (453,'RecognizedType','Month','MONTH_TYPE','Month Type',3);
INSERT INTO `enum_lookup` VALUES (454,'RecognizedType','Year','YEAR_TYPE','Year Type',4);
INSERT INTO `enum_lookup` VALUES (455,'RecognizedType','Quarter','QUARTER_TYPE','Quarter Type',5);
INSERT INTO `enum_lookup` VALUES (456,'RecognizedType','Value','VALUE_TYPE','Value Type',6);
INSERT INTO `enum_lookup` VALUES (457,'RecognizedType','Operator','OPERATOR_TYPE','Operator Type',7);
INSERT INTO `enum_lookup` VALUES (458,'RecognizedType','ComparativeStatistics','COMPARATIVE_STATISTICS_TYPE','Comparative Statistics Type',8);
INSERT INTO `enum_lookup` VALUES (459,'RecognizedType','UnitScale','UNIT_SCALE_TYPE','Unit Scale Type',9);
INSERT INTO `enum_lookup` VALUES (460,'RecognizedType','UnitSymbol','UNIT_SYMBOL_TYPE','Unit Symbol Type',10);
INSERT INTO `enum_lookup` VALUES (461,'RecognizedType','Unit','UNIT_TYPE','Unit Type',11);
INSERT INTO `enum_lookup` VALUES (462,'RecognizedType','TimeFrame','TF_TYPE','Tf Type',12);
INSERT INTO `enum_lookup` VALUES (463,'RecognizedType','Adjective','ADJECTIVE_TYPE','Adjective Type',13);
INSERT INTO `enum_lookup` VALUES (464,'RecognizedType','ValuePreposition','VALUE_PREPOSITION','Value Preposition',14);
INSERT INTO `enum_lookup` VALUES (465,'RecognizedType','Name','NAME_TYPE','Name Type',15);
INSERT INTO `enum_lookup` VALUES (466,'RecognizedType','Time','TIME_TYPE','Time Type',16);
INSERT INTO `enum_lookup` VALUES (467,'RecognizedType','Week','WEEK_TYPE','Week Type',17);
INSERT INTO `enum_lookup` VALUES (468,'RecognizedType','WeekDay','WEEK_DAY_TYPE','Week Day Type',18);
INSERT INTO `enum_lookup` VALUES (469,'RecognizedType','TimeQualifier','TIME_QULAIFIER_TYPE','Time Qulaifier Type',19);
INSERT INTO `enum_lookup` VALUES (470,'RecognizedType','Hour','HOUR_TYPE','Hour Type',20);
INSERT INTO `enum_lookup` VALUES (471,'RecognizedType','Minute','MINUTE_TYPE','Minute Type',21);
INSERT INTO `enum_lookup` VALUES (472,'RecognizedType','Second','SECOND_TYPE','Second Type',22);
INSERT INTO `enum_lookup` VALUES (473,'RecognizedType','TimePreposition','TIME_PREPOSITION','Time Preposition',23);
INSERT INTO `enum_lookup` VALUES (474,'RecognizedType','City','CITY_TYPE','City Type',24);
INSERT INTO `enum_lookup` VALUES (475,'RecognizedType','State','STATE_TYPE','State Type',25);
INSERT INTO `enum_lookup` VALUES (476,'RecognizedType','Country','COUNTRY_TYPE','Country Type',26);
INSERT INTO `enum_lookup` VALUES (477,'RecognizedType','County','COUNTY_TYPE','County Type',27);
INSERT INTO `enum_lookup` VALUES (7851,'DateUnitType','DAY','DAY','Day',1);
INSERT INTO `enum_lookup` VALUES (7852,'DateUnitType','WEEK','WEEK','Week',2);
INSERT INTO `enum_lookup` VALUES (7853,'DateUnitType','MONTH','MONTH','Month',3);
INSERT INTO `enum_lookup` VALUES (7854,'DateUnitType','QUARTER','QUARTER','Quarter',4);
INSERT INTO `enum_lookup` VALUES (7855,'DateUnitType','YEAR','YEAR','Year',5);
INSERT INTO `enum_lookup` VALUES (7201,'UniversalSearchType','1','UNSTRUCTURED_SEARCH','Unstructured Search',1);
INSERT INTO `enum_lookup` VALUES (7202,'UniversalSearchType','2','QUERY_CACHE_SEARCH','Query Cache Search',2);
INSERT INTO `enum_lookup` VALUES (7203,'UniversalSearchType','3','RELATED_QUERY_SEARCH','Related Query Search',3);
INSERT INTO `enum_lookup` VALUES (4901,'PaginationType','1','Applications','Applications',1);
INSERT INTO `enum_lookup` VALUES (4902,'PaginationType','2','Assets','Assets',2);
INSERT INTO `enum_lookup` VALUES (4903,'PaginationType','3','SourceTables','Sourcetables',3);
INSERT INTO `enum_lookup` VALUES (4904,'PaginationType','4','Columns','Columns',4);
INSERT INTO `enum_lookup` VALUES (4905,'PaginationType','5','Members','Members',5);
INSERT INTO `enum_lookup` VALUES (4906,'PaginationType','6','EvaluatedColumns','Evaluatedcolumns',6);
INSERT INTO `enum_lookup` VALUES (4907,'PaginationType','7','Concepts','Concepts',7);
INSERT INTO `enum_lookup` VALUES (4908,'PaginationType','8','Instances','Instances',8);
INSERT INTO `enum_lookup` VALUES (4909,'PaginationType','9','BusinessTermsForParallelWords','Businesstermsforparallelwords',9);
INSERT INTO `enum_lookup` VALUES (4910,'PaginationType','10','ConceptsForRanges','Conceptsforranges',10);
INSERT INTO `enum_lookup` VALUES (4911,'PaginationType','11','ConceptsForProfiles','Conceptsforprofiles',11);
INSERT INTO `enum_lookup` VALUES (4912,'PaginationType','12','ConceptsForCubes','Conceptsforcubes',12);
INSERT INTO `enum_lookup` VALUES (3551,'HierarchyRelationType','506#P','ADD_PARENT','Add Parent',1);
INSERT INTO `enum_lookup` VALUES (3552,'HierarchyRelationType','506#C','ADD_CHILD','Add Child',2);
INSERT INTO `enum_lookup` VALUES (3553,'HierarchyRelationType','507#P','ADD_PART','Add Part',3);
INSERT INTO `enum_lookup` VALUES (1601,'AttributeType','freebase_id','FREEBASE_ID','Freebase Id',1);
INSERT INTO `enum_lookup` VALUES (1602,'AttributeType','image_url','IMAGE_URL','Image Url',2);
INSERT INTO `enum_lookup` VALUES (1603,'AttributeType','wiki_url_id','WIKI_URL','Wiki Url',3);
INSERT INTO `enum_lookup` VALUES (1604,'AttributeType','topic_name','TOPIC_NAME','Topic Name',4);
INSERT INTO `enum_lookup` VALUES (3701,'JobStatus','1','PENDING','Pending',1);
INSERT INTO `enum_lookup` VALUES (3702,'JobStatus','2','INPROGRESS','Inprogress',2);
INSERT INTO `enum_lookup` VALUES (3703,'JobStatus','3','SUCCESS','Success',3);
INSERT INTO `enum_lookup` VALUES (3704,'JobStatus','4','FAILURE','Failure',4);
INSERT INTO `enum_lookup` VALUES (7101,'TripleVariationType','1','SUBJECT','Subject',1);
INSERT INTO `enum_lookup` VALUES (7102,'TripleVariationType','2','OBJECT','Object',2);
INSERT INTO `enum_lookup` VALUES (7103,'TripleVariationType','3','RELATION','Relation',3);
INSERT INTO `enum_lookup` VALUES (7104,'TripleVariationType','4','SUBJECT_RELATION','Subject Relation',4);
INSERT INTO `enum_lookup` VALUES (7105,'TripleVariationType','5','RELATION_OBJECT','Relation Object',5);
INSERT INTO `enum_lookup` VALUES (7106,'TripleVariationType','6','SUBJECT_OBJECT','Subject Object',6);
INSERT INTO `enum_lookup` VALUES (7107,'TripleVariationType','7','SUBJECT_RELATION_OBJECT','Subject Relation Object',7);
INSERT INTO `enum_lookup` VALUES (3301,'FeatureDependencyType','FACET','FACET_DEPENDENCY','Facet Dependency',1);
INSERT INTO `enum_lookup` VALUES (3302,'FeatureDependencyType','TIME_CONVERSION','TIME_CONVERSION_DEPENDENCY','Time Conversion Dependency',2);
INSERT INTO `enum_lookup` VALUES (5451,'PublisherAbsorptionStatus','0','STARTED','Started',1);
INSERT INTO `enum_lookup` VALUES (5452,'PublisherAbsorptionStatus','9','FILE_SAVED','File Saved',2);
INSERT INTO `enum_lookup` VALUES (5453,'PublisherAbsorptionStatus','11','TEMP_META_LOAD_FAILED','Temp Meta Load Failed',3);
INSERT INTO `enum_lookup` VALUES (5454,'PublisherAbsorptionStatus','12','TEMP_DATA_LOAD_FAILED','Temp Data Load Failed',4);
INSERT INTO `enum_lookup` VALUES (5455,'PublisherAbsorptionStatus','13','TEMP_LOADED','Temp Loaded',5);
INSERT INTO `enum_lookup` VALUES (5456,'PublisherAbsorptionStatus','14','ANALISYS_FAILED','Analisys Failed',6);
INSERT INTO `enum_lookup` VALUES (5457,'PublisherAbsorptionStatus','19','ANALYZED','Analyzed',7);
INSERT INTO `enum_lookup` VALUES (5458,'PublisherAbsorptionStatus','21','EVAL_META_LOAD_FAILED','Eval Meta Load Failed',8);
INSERT INTO `enum_lookup` VALUES (5459,'PublisherAbsorptionStatus','22','EVAL_DATA_LOAD_FAILED','Eval Data Load Failed',9);
INSERT INTO `enum_lookup` VALUES (5460,'PublisherAbsorptionStatus','29','EVAL_LOADED','Eval Loaded',10);
INSERT INTO `enum_lookup` VALUES (5461,'PublisherAbsorptionStatus','31','ASSET_ANALYSIS_FAILED','Asset Analysis Failed',11);
INSERT INTO `enum_lookup` VALUES (5462,'PublisherAbsorptionStatus','32','ASSET_ABSORB_FAILED','Asset Absorb Failed',12);
INSERT INTO `enum_lookup` VALUES (5463,'PublisherAbsorptionStatus','39','ASSET_ABSORBED','Asset Absorbed',13);
INSERT INTO `enum_lookup` VALUES (5464,'PublisherAbsorptionStatus','41','MODEL_ABSORB_FAILED','Model Absorb Failed',14);
INSERT INTO `enum_lookup` VALUES (5465,'PublisherAbsorptionStatus','49','MODEL_ABSORBED','Model Absorbed',15);
INSERT INTO `enum_lookup` VALUES (5466,'PublisherAbsorptionStatus','75','ASSET_READY','Asset Ready',16);
INSERT INTO `enum_lookup` VALUES (3601,'HierarchyType','1','NON_HIERARCHICAL','Non Hierarchical',1);
INSERT INTO `enum_lookup` VALUES (3602,'HierarchyType','2','PARENTAGE','Parentage',2);
INSERT INTO `enum_lookup` VALUES (3603,'HierarchyType','3','COMPOSITION','Composition',3);
INSERT INTO `enum_lookup` VALUES (1501,'AssociationPositionType','S','SOURCE','Source',1);
INSERT INTO `enum_lookup` VALUES (1502,'AssociationPositionType','D','DESTINATION','Destination',2);
INSERT INTO `enum_lookup` VALUES (501,'UserRequestType','AdvancedOptions','ADVANCED_OPTIONS','Advance Request',1);
INSERT INTO `enum_lookup` VALUES (502,'UserRequestType','DemoRequest','DEMO_REQUEST','Demo Request',2);
INSERT INTO `enum_lookup` VALUES (503,'UserRequestType','GeneralFeedback','GENERAL_FEEDBACK','General Feedback',3);
INSERT INTO `enum_lookup` VALUES (7651,'PossibilityStatus','0','PRE_PROCESS','Pre Process',1);
INSERT INTO `enum_lookup` VALUES (7652,'PossibilityStatus','1','IN_PROCESS','In Process',2);
INSERT INTO `enum_lookup` VALUES (7653,'PossibilityStatus','2','INDIVIDUAL_RECOGNITION','Individual Recognition',3);
INSERT INTO `enum_lookup` VALUES (7654,'PossibilityStatus','3','GROUP_RECOGNITION','Group Recognition',4);
INSERT INTO `enum_lookup` VALUES (7655,'PossibilityStatus','4','DOMAIN_RECOGNITION','Domain Recognition',5);
INSERT INTO `enum_lookup` VALUES (7656,'PossibilityStatus','5','ASSOCIATION','Association',6);
INSERT INTO `enum_lookup` VALUES (7657,'PossibilityStatus','6','DOMAIN_ASSOCIATION','Domain Association',7);
INSERT INTO `enum_lookup` VALUES (7658,'PossibilityStatus','7','INDIVIDUAL_RECOGNITION_COMPLETE','Individual Recognition Complete',8);
INSERT INTO `enum_lookup` VALUES (7659,'PossibilityStatus','8','GROUP_RECOGNITION_COMPLETE','Group Recognition Complete',9);
INSERT INTO `enum_lookup` VALUES (7660,'PossibilityStatus','9','DOMAIN_RECOGNITION_COMPLETE','Domain Recognition Complete',10);
INSERT INTO `enum_lookup` VALUES (7661,'PossibilityStatus','10','ASSOCIATION_COMPLETE','Association Complete',11);
INSERT INTO `enum_lookup` VALUES (7662,'PossibilityStatus','11','DOMAIN_ASSOCIATION_COMPLETE','Domain Association Complete',12);
INSERT INTO `enum_lookup` VALUES (7663,'PossibilityStatus','99','COMPLETED','Completed',13);
INSERT INTO `enum_lookup` VALUES (6101,'ReportType','1','Grid','Grid',1);
INSERT INTO `enum_lookup` VALUES (6102,'ReportType','2','BarChart','Barchart',2);
INSERT INTO `enum_lookup` VALUES (6103,'ReportType','3','LineChart','Linechart',3);
INSERT INTO `enum_lookup` VALUES (6104,'ReportType','4','PivotTable','Pivottable',4);
INSERT INTO `enum_lookup` VALUES (6105,'ReportType','5','GroupTable','Grouptable',5);
INSERT INTO `enum_lookup` VALUES (6106,'ReportType','6','CrossTable','Crosstable',6);
INSERT INTO `enum_lookup` VALUES (6107,'ReportType','7','CrossBarChart','Crossbarchart',7);
INSERT INTO `enum_lookup` VALUES (6108,'ReportType','8','CrossLineChart','Crosslinechart',8);
INSERT INTO `enum_lookup` VALUES (6109,'ReportType','9','HierarchyChart','Hierarchychart',9);
INSERT INTO `enum_lookup` VALUES (6110,'ReportType','10','BarLineChart','Barlinechart',10);
INSERT INTO `enum_lookup` VALUES (6111,'ReportType','11','MultiBarChart','Multibarchart',11);
INSERT INTO `enum_lookup` VALUES (6112,'ReportType','12','ClusterBarChart','Clusterbarchart',12);
INSERT INTO `enum_lookup` VALUES (6113,'ReportType','13','CMultiBarChart','Cmultibarchart',13);
INSERT INTO `enum_lookup` VALUES (6114,'ReportType','14','MultiLineChart','Multilinechart',14);
INSERT INTO `enum_lookup` VALUES (6115,'ReportType','15','ClusterLineChart','Clusterlinechart',15);
INSERT INTO `enum_lookup` VALUES (6116,'ReportType','16','CMultiLineChart','Cmultilinechart',16);
INSERT INTO `enum_lookup` VALUES (6117,'ReportType','17','MultiLineClusterChart','Multilineclusterchart',17);
INSERT INTO `enum_lookup` VALUES (6118,'ReportType','18','CMMultiBarChart','Cmmultibarchart',18);
INSERT INTO `enum_lookup` VALUES (6119,'ReportType','19','CMMultiLineChart','Cmmultilinechart',19);
INSERT INTO `enum_lookup` VALUES (6120,'ReportType','20','DetailGrid','Detailgrid',20);
INSERT INTO `enum_lookup` VALUES (6121,'ReportType','21','DetailGroupTable','Detailgrouptable',21);
INSERT INTO `enum_lookup` VALUES (6122,'ReportType','22','StockChart','Stockchart',22);
INSERT INTO `enum_lookup` VALUES (6123,'ReportType','23','PortraitTable','Portraittable',23);
INSERT INTO `enum_lookup` VALUES (6124,'ReportType','24','ClusterColumnGrid','Clustercolumngrid',24);
INSERT INTO `enum_lookup` VALUES (6125,'ReportType','80','CountryMapChart','Countrymapchart',25);
INSERT INTO `enum_lookup` VALUES (6126,'ReportType','81','CrossStateMapChart','Crossstatemapchart',26);
INSERT INTO `enum_lookup` VALUES (6127,'ReportType','98','DetailCsvFile','Detailcsvfile',27);
INSERT INTO `enum_lookup` VALUES (6128,'ReportType','99','CsvFile','Csvfile',28);
INSERT INTO `enum_lookup` VALUES (6129,'ReportType','50','ClusterPieChart','Clusterpiechart',29);
INSERT INTO `enum_lookup` VALUES (6130,'ReportType','25','PieChart','Piechart',30);
INSERT INTO `enum_lookup` VALUES (101,'ResourceType','A','ACTION','Action Type',1);
INSERT INTO `enum_lookup` VALUES (102,'ResourceType','L','LINK_RESOURCE','Resource Link Type',2);
INSERT INTO `enum_lookup` VALUES (1001,'ArithmeticOperatorType','+','ADDITION','Addition',1);
INSERT INTO `enum_lookup` VALUES (1002,'ArithmeticOperatorType','-','SUBTRACTION','Subtraction',2);
INSERT INTO `enum_lookup` VALUES (1003,'ArithmeticOperatorType','*','MULTIPLICATION','Multiplication',3);
INSERT INTO `enum_lookup` VALUES (1004,'ArithmeticOperatorType','/','DIVISION','Division',4);
INSERT INTO `enum_lookup` VALUES (5501,'PublisherDataMedium','CSV','CSV','Csv',1);
INSERT INTO `enum_lookup` VALUES (5502,'PublisherDataMedium','EXCEL','EXCEL','Excel',2);
INSERT INTO `enum_lookup` VALUES (4851,'PageSearchType','1','STARTS_WITH','Starts With',1);
INSERT INTO `enum_lookup` VALUES (4852,'PageSearchType','2','CONTAINS','Contains',2);
INSERT INTO `enum_lookup` VALUES (4853,'PageSearchType','3','ENDS_WITH','Ends With',3);
INSERT INTO `enum_lookup` VALUES (4854,'PageSearchType','4','EQUALS','Equals',4);
INSERT INTO `enum_lookup` VALUES (4855,'PageSearchType','5','BY_APP_NAME','By App Name',5);
INSERT INTO `enum_lookup` VALUES (4856,'PageSearchType','6','BY_PUBLISHER_NAME','By Publisher Name',6);
INSERT INTO `enum_lookup` VALUES (4857,'PageSearchType','7','VERTICAL','Vertical',7);
INSERT INTO `enum_lookup` VALUES (5751,'QueryConditionOperandType','VALUE','VALUE','Value',1);
INSERT INTO `enum_lookup` VALUES (5752,'QueryConditionOperandType','TABLE_COLUMN','TABLE_COLUMN','Table Column',2);
INSERT INTO `enum_lookup` VALUES (5753,'QueryConditionOperandType','SUB_QUERY','SUB_QUERY','Sub Query',3);
INSERT INTO `enum_lookup` VALUES (5754,'QueryConditionOperandType','SUB_CONDITION','SUB_CONDITION','Sub Condition',4);
INSERT INTO `enum_lookup` VALUES (4451,'OperationEnum','J','JOB','Job',1);
INSERT INTO `enum_lookup` VALUES (4452,'OperationEnum','I','INTERACTIVE','Interactive',2);
INSERT INTO `enum_lookup` VALUES (4151,'NewsCategory','News','NEWS_NEWS','News News',1);
INSERT INTO `enum_lookup` VALUES (4152,'NewsCategory','Finance','FINANCE_NEWS','Finance News',2);
INSERT INTO `enum_lookup` VALUES (4153,'NewsCategory','Sports','SPORTS_NEWS','Sports News',3);
INSERT INTO `enum_lookup` VALUES (4154,'NewsCategory','Entertainment','ENTERNMENT_NEWS','Enternment News',4);
INSERT INTO `enum_lookup` VALUES (4155,'NewsCategory','Business','BUSINESS_NEWS','Business News',5);
INSERT INTO `enum_lookup` VALUES (4156,'NewsCategory','General','GENERAL_NEWS','General News',6);
INSERT INTO `enum_lookup` VALUES (4157,'NewsCategory','Government','GOVERNMENT_NEWS','Government News',7);
INSERT INTO `enum_lookup` VALUES (4158,'NewsCategory','Craigslist','CRAIGSLIST_NEWS','Craigslist News',8);
INSERT INTO `enum_lookup` VALUES (4159,'NewsCategory','Default','DEFAULT','Default',9);
INSERT INTO `enum_lookup` VALUES (6001,'QueryValueType','SV','STRING','String',1);
INSERT INTO `enum_lookup` VALUES (6002,'QueryValueType','NS','NORMALIZAED_STRING','Normalizaed String',2);
INSERT INTO `enum_lookup` VALUES (6003,'QueryValueType','NO','NORMALIZED_OBJECT','Normalized Object',3);
INSERT INTO `enum_lookup` VALUES (2051,'CloudCategory','1','TYPE_CLOUD','Type Cloud',1);
INSERT INTO `enum_lookup` VALUES (2052,'CloudCategory','2','APP_CLOUD','App Cloud',2);
INSERT INTO `enum_lookup` VALUES (2053,'CloudCategory','3','FRAMEWORK_CLOUD','Framework Cloud',3);
INSERT INTO `enum_lookup` VALUES (2054,'CloudCategory','4','CONCEPT_CLOUD','Concept Cloud',4);
INSERT INTO `enum_lookup` VALUES (2055,'CloudCategory','5','INFORMATION_CLOUD','Information Cloud',5);
INSERT INTO `enum_lookup` VALUES (1401,'AssetSubType','0','None','None',1);
INSERT INTO `enum_lookup` VALUES (1402,'AssetSubType','1','SuperSet','Superset',2);
INSERT INTO `enum_lookup` VALUES (1403,'AssetSubType','2','NWay','Nway',3);
INSERT INTO `enum_lookup` VALUES (1404,'AssetSubType','3','SimpleRandomSampled','Simplerandomsampled',4);
INSERT INTO `enum_lookup` VALUES (1405,'AssetSubType','4','SimpleStratifiedSampled','Simplestratifiedsampled',5);
INSERT INTO `enum_lookup` VALUES (1406,'AssetSubType','5','ExeCueSampled','Execuesampled',6);
INSERT INTO `enum_lookup` VALUES (3801,'JoinType','INNER JOIN','INNER','Inner',1);
INSERT INTO `enum_lookup` VALUES (3802,'JoinType','LEFT OUTER JOIN','LEFT_OUTER','Left Outer',2);
INSERT INTO `enum_lookup` VALUES (3803,'JoinType','RIGHT OUTER JOIN','RIGHT_OUTER','Right Outer',3);
INSERT INTO `enum_lookup` VALUES (4301,'NotificationParamName','ApplicationName','APPLICATION_NAME','Application Name',1);
INSERT INTO `enum_lookup` VALUES (4302,'NotificationParamName','AssetName','ASSET_NAME','Asset Name',2);
INSERT INTO `enum_lookup` VALUES (4303,'NotificationParamName','TableName','TABLE_NAME','Table Name',3);
INSERT INTO `enum_lookup` VALUES (4304,'NotificationParamName','ColumnName','COLUMN_NAME','Column Name',4);
INSERT INTO `enum_lookup` VALUES (4305,'NotificationParamName','TimeStamp','TIME_STAMP','Time Stamp',5);
INSERT INTO `enum_lookup` VALUES (4306,'NotificationParamName','FileName','FILE_NAME','File Name',6);
INSERT INTO `enum_lookup` VALUES (4307,'NotificationParamName','Operation','OPERATION','Operation',7);
INSERT INTO `enum_lookup` VALUES (4308,'NotificationParamName','ModelName','MODEL_NAME','Model Name',8);
INSERT INTO `enum_lookup` VALUES (1051,'AssetAnalysisOperationType','LookupTableWithoutMembers','LOOKUP_TABLE_WITHOUT_MEMBERS','Lookup Table Without Members',1);
INSERT INTO `enum_lookup` VALUES (1052,'AssetAnalysisOperationType','TablesWithoutJoins','TABLES_WITHOUT_JOINS','Tables Without Joins',2);
INSERT INTO `enum_lookup` VALUES (1053,'AssetAnalysisOperationType','ColumnsWithMissingColumnType','COLUMNS_WITH_MISSING_COLUMTYPE','Columns With Missing Columtype',3);
INSERT INTO `enum_lookup` VALUES (1054,'AssetAnalysisOperationType','UnmappedColumns','UNMAPPED_COLUMNS','Unmapped Columns',4);
INSERT INTO `enum_lookup` VALUES (1055,'AssetAnalysisOperationType','UnmappedMembers','UNMAPPED_MEMBERS','Unmapped Members',5);
INSERT INTO `enum_lookup` VALUES (1056,'AssetAnalysisOperationType','AssetWithoutGrain','ASSET_WITHOUT_GRAIN','Asset Without Grain',6);
INSERT INTO `enum_lookup` VALUES (1057,'AssetAnalysisOperationType','TableWithoutDefaultMetrics','TABLE_WITHOUT_DEFAULT_METRICS','Table Without Default Metrics',7);
INSERT INTO `enum_lookup` VALUES (3001,'DynamicValueQualifierType','LAST','LAST','Last',1);
INSERT INTO `enum_lookup` VALUES (3002,'DynamicValueQualifierType','FIRST','FIRST','First',2);
INSERT INTO `enum_lookup` VALUES (3003,'DynamicValueQualifierType','NEXT','NEXT','Next',3);
INSERT INTO `enum_lookup` VALUES (3004,'DynamicValueQualifierType','ALL','ALL','All',4);
INSERT INTO `enum_lookup` VALUES (3005,'DynamicValueQualifierType','SINCE','SINCE','Since',5);
INSERT INTO `enum_lookup` VALUES (3006,'DynamicValueQualifierType','BEFORE','BEFORE','Before',6);
INSERT INTO `enum_lookup` VALUES (3007,'DynamicValueQualifierType','AFTER','AFTER','After',7);
INSERT INTO `enum_lookup` VALUES (7601,'InflectionType','singular','SINGULAR','Singular',1);
INSERT INTO `enum_lookup` VALUES (7602,'InflectionType','plural','PLURAL','Plural',2);
INSERT INTO `enum_lookup` VALUES (7603,'InflectionType','verbTense','VERB_TENSE','Verb Tense',3);
INSERT INTO `enum_lookup` VALUES (7901,'RelativeQualifierType','FORWARD','FORWARD','Forward',1);
INSERT INTO `enum_lookup` VALUES (7902,'RelativeQualifierType','BACK','BACK','Back',2);
INSERT INTO `enum_lookup` VALUES (1351,'AssetProviderType','1','Oracle','Oracle',1);
INSERT INTO `enum_lookup` VALUES (1352,'AssetProviderType','2','MySql','MySQL',2);
INSERT INTO `enum_lookup` VALUES (1353,'AssetProviderType','3','MSSql','MSSQL',3);
INSERT INTO `enum_lookup` VALUES (1354,'AssetProviderType','4','DB2','DB2',4);
INSERT INTO `enum_lookup` VALUES (1355,'AssetProviderType','5','DEFAULT','Default',5);
INSERT INTO `enum_lookup` VALUES (1356,'AssetProviderType','6','HTTP','Http',6);
INSERT INTO `enum_lookup` VALUES (1357,'AssetProviderType','7','Teradata','Teradata',7);
INSERT INTO `enum_lookup` VALUES (1358,'AssetProviderType','8','SAS_SHARENET','SAS Sharenet',8);
INSERT INTO `enum_lookup` VALUES (1359,'AssetProviderType','9','SAS_WORKSPACE','SAS Workspace',9);
INSERT INTO `enum_lookup` VALUES (3651,'IndicatorBehaviorType','P','POSITIVE','Positive',1);
INSERT INTO `enum_lookup` VALUES (3652,'IndicatorBehaviorType','N','NEGATIVE','Negative',2);
INSERT INTO `enum_lookup` VALUES (5001,'PathProcessingType','Skip','SKIP','Skip',1);
INSERT INTO `enum_lookup` VALUES (5002,'PathProcessingType','Cut','CUT','Cut',2);
INSERT INTO `enum_lookup` VALUES (5003,'PathProcessingType','Consider','CONSIDER','Consider',3);
INSERT INTO `enum_lookup` VALUES (5004,'PathProcessingType','Implicit','IMPLICIT_PATH','Implicit Path',4);
INSERT INTO `enum_lookup` VALUES (3251,'FacetNatureType','BROWSABLE','BROWSABLE','Browsable',1);
INSERT INTO `enum_lookup` VALUES (3252,'FacetNatureType','RESULT','RESULT_BASED','Result Based',2);
INSERT INTO `enum_lookup` VALUES (3253,'FacetNatureType','COMBO','COMBINATION','Combination',3);
INSERT INTO `enum_lookup` VALUES (6251,'RFXObjectType','D','DOMAIN_ENTITY_DEFINITION','Domain Entity Definition',1);
INSERT INTO `enum_lookup` VALUES (6252,'RFXObjectType','V','VALUE','Value',2);
INSERT INTO `enum_lookup` VALUES (6253,'RFXObjectType','L','LIST','List',3);
INSERT INTO `enum_lookup` VALUES (6254,'RFXObjectType','R','RANGE','Range',4);
INSERT INTO `enum_lookup` VALUES (6801,'SuccessFailureType','0','SUCCESS','Success',1);
INSERT INTO `enum_lookup` VALUES (6802,'SuccessFailureType','-1','FAILURE','Failure',2);
INSERT INTO `enum_lookup` VALUES (4001,'MappingType','CONCEPT_TO_COLUMN','CONCEPT','Concept',1);
INSERT INTO `enum_lookup` VALUES (4002,'MappingType','INSTANCE_TO_MEMBER','INSTANCE','Instance',2);
INSERT INTO `enum_lookup` VALUES (401,'RecognitionEntityType','com.execue.nlp.bean.entity.ConceptEntity','CONCEPT_ENTITY','Concept Entity',1);
INSERT INTO `enum_lookup` VALUES (402,'RecognitionEntityType','com.execue.nlp.bean.entity.InstanceEntity','INSTANCE_ENTITY','Instance Entity',2);
INSERT INTO `enum_lookup` VALUES (403,'RecognitionEntityType','com.execue.nlp.bean.entity.PropertyEntity','PROPERTY_ENTITY','Property Entity',3);
INSERT INTO `enum_lookup` VALUES (404,'RecognitionEntityType','com.execue.nlp.bean.entity.SFLEntity','SFL_ENTITY','Sfl Entity',4);
INSERT INTO `enum_lookup` VALUES (405,'RecognitionEntityType','com.execue.nlp.bean.entity.PWEntity','PW_ENTITY','Pw Entity',5);
INSERT INTO `enum_lookup` VALUES (406,'RecognitionEntityType','com.execue.nlp.bean.entity.CandidateEntity','CANDIDATE_ENTITY','Candidate Entity',6);
INSERT INTO `enum_lookup` VALUES (407,'RecognitionEntityType','com.execue.nlp.bean.entity.ConceptProfileEntity','CONCEPT_PROFILE_ENTITY','Concept Profile Entity',7);
INSERT INTO `enum_lookup` VALUES (408,'RecognitionEntityType','com.execue.nlp.bean.entity.InstanceProfileEntity','INSTANCE_PROFILE_ENTITY','Instance Profile Entity',8);
INSERT INTO `enum_lookup` VALUES (409,'RecognitionEntityType','com.execue.nlp.bean.entity.RecognitionEntity','GENERAL_ENTITY','General Entity',9);
INSERT INTO `enum_lookup` VALUES (410,'RecognitionEntityType','com.execue.nlp.bean.entity.TypeEntity','TYPE_ENTITY','Type Entity',10);
INSERT INTO `enum_lookup` VALUES (411,'RecognitionEntityType','com.execue.nlp.bean.entity.InflectionEntity','INFLECTION_ENTITY','Inflection Entity',11);
INSERT INTO `enum_lookup` VALUES (7151,'UdxCarsInfoSortType','MAKE_NAME##ASC##N','MAKE_ASC','Make',1);
INSERT INTO `enum_lookup` VALUES (7152,'UdxCarsInfoSortType','MODEL_NAME##ASC##N','MODEL_ASC','Model',2);
INSERT INTO `enum_lookup` VALUES (7153,'UdxCarsInfoSortType','MODEL_YEAR##ASC##Y','MODEL_YEAR_ASC','Year : Low to High',3);
INSERT INTO `enum_lookup` VALUES (7154,'UdxCarsInfoSortType','MODEL_YEAR##DESC##Y','MODEL_YEAR_DESC','Year : High to Low',4);
INSERT INTO `enum_lookup` VALUES (7155,'UdxCarsInfoSortType','PRICE##ASC##Y','PRICE_ASC','Price : Low to High',5);
INSERT INTO `enum_lookup` VALUES (7156,'UdxCarsInfoSortType','PRICE##DESC##Y','PRICE_DESC','Price : High to Low',6);
INSERT INTO `enum_lookup` VALUES (7157,'UdxCarsInfoSortType','MILEAGE##ASC##Y','MILEAGE_ASC','Mileage : Low to High',7);
INSERT INTO `enum_lookup` VALUES (7158,'UdxCarsInfoSortType','MILEAGE##DESC##Y','MILEAGE_DESC','Mileage : High to Low',8);
INSERT INTO `enum_lookup` VALUES (7159,'UdxCarsInfoSortType','DISTANCE##ASC##N','DISTANCE_ASC','Distance : Low to High',9);
INSERT INTO `enum_lookup` VALUES (7160,'UdxCarsInfoSortType','DISTANCE##DESC##N','DISTANCE_DESC','Distance : High to Low',10);
INSERT INTO `enum_lookup` VALUES (7161,'UdxCarsInfoSortType','UDX_CONTENT_DATE##DESC##N','UDX_CONTENT_DATE_DESC','Latest Posted Date',11);
INSERT INTO `enum_lookup` VALUES (2001,'CheckType','N','NO','No',1);
INSERT INTO `enum_lookup` VALUES (2002,'CheckType','Y','YES','Yes',2);
INSERT INTO `enum_lookup` VALUES (2501,'ConstraintType','PRIMARY','PRIMARY_KEY','Primary Key',1);
INSERT INTO `enum_lookup` VALUES (2502,'ConstraintType','FOREIGN','FOREIGN_KEY','Foreign Key',2);
INSERT INTO `enum_lookup` VALUES (2503,'ConstraintType','UNIQUE','UNIQUE_KEY','Unique Key',3);
INSERT INTO `enum_lookup` VALUES (6401,'RFXValueType','1','RFX_VALUE_CONTENT','Rfx Value Content',1);
INSERT INTO `enum_lookup` VALUES (6402,'RFXValueType','2','RFX_VALUE_USER_QUERY','Rfx Value User Query',2);
INSERT INTO `enum_lookup` VALUES (4101,'ModelCategoryType','BASE','BASE','Base',1);
INSERT INTO `enum_lookup` VALUES (4102,'ModelCategoryType','SYSTEM','SYSTEM','System',2);
INSERT INTO `enum_lookup` VALUES (4103,'ModelCategoryType','USER','USER','User',3);
INSERT INTO `enum_lookup` VALUES (3951,'LookupType','NONE','None','None',1);
INSERT INTO `enum_lookup` VALUES (3952,'LookupType','SL','SIMPLE_LOOKUP','Simple Lookup',2);
INSERT INTO `enum_lookup` VALUES (3953,'LookupType','RL','RANGE_LOOKUP','Range Lookup',3);
INSERT INTO `enum_lookup` VALUES (3954,'LookupType','SHL','SIMPLEHIERARCHICAL_LOOKUP','Simplehierarchical Lookup',4);
INSERT INTO `enum_lookup` VALUES (3955,'LookupType','RHL','RANGEHIERARCHICAL_LOOKUP','Rangehierarchical Lookup',5);
INSERT INTO `enum_lookup` VALUES (3051,'EntityTripleDefinitionType','CRC','CONCEPT_RELATION_CONCEPT','Concept Relation Concept',1);
INSERT INTO `enum_lookup` VALUES (3052,'EntityTripleDefinitionType','RRR','RELATION_RELATION_RELATION','Relation Relation Relation',2);
INSERT INTO `enum_lookup` VALUES (3053,'EntityTripleDefinitionType','ARA','ATTRIBUTE_RELATION_ATTRIBUTE','Attribute Relation Attribute',3);
INSERT INTO `enum_lookup` VALUES (3054,'EntityTripleDefinitionType','CRA','CONCEPT_RELATION_ATTRIBUTE','Concept Relation Attribute',4);
INSERT INTO `enum_lookup` VALUES (3055,'EntityTripleDefinitionType','TRT','TYPE_RELATION_TYPE','Type Relation Type',5);
INSERT INTO `enum_lookup` VALUES (4551,'OperationType','A','ADD','Add',1);
INSERT INTO `enum_lookup` VALUES (4552,'OperationType','D','DELETE','Delete',2);
INSERT INTO `enum_lookup` VALUES (4553,'OperationType','U','UPDATE','Update',3);
INSERT INTO `enum_lookup` VALUES (6151,'RequestType','Create','CREATE','Create',1);
INSERT INTO `enum_lookup` VALUES (6152,'RequestType','Update','UPDATE','Update',2);
INSERT INTO `enum_lookup` VALUES (6153,'RequestType','Delete','DELETE','Delete',3);
INSERT INTO `enum_lookup` VALUES (6154,'RequestType','View','VIEW','View',4);
INSERT INTO `enum_lookup` VALUES (4601,'OperatorType','=','EQUALS','Equals',1);
INSERT INTO `enum_lookup` VALUES (4602,'OperatorType','!=','NOT_EQUALS','Not Equals',2);
INSERT INTO `enum_lookup` VALUES (4603,'OperatorType','>','GREATER_THAN','Greater Than',3);
INSERT INTO `enum_lookup` VALUES (4604,'OperatorType','>=','GREATER_THAN_EQUALS','Greater Than Equals',4);
INSERT INTO `enum_lookup` VALUES (4605,'OperatorType','<','LESS_THAN','Less Than',5);
INSERT INTO `enum_lookup` VALUES (4606,'OperatorType','<=','LESS_THAN_EQUALS','Less Than Equals',6);
INSERT INTO `enum_lookup` VALUES (4607,'OperatorType','BETWEEN','BETWEEN','Between',7);
INSERT INTO `enum_lookup` VALUES (4608,'OperatorType','IN','IN','In',8);
INSERT INTO `enum_lookup` VALUES (4609,'OperatorType','NOT IN','NOT_IN','Not In',9);
INSERT INTO `enum_lookup` VALUES (4610,'OperatorType','IS NULL','IS_NULL','Is Null',10);
INSERT INTO `enum_lookup` VALUES (4611,'OperatorType','IS NOT NULL','IS_NOT_NULL','Is Not Null',11);
INSERT INTO `enum_lookup` VALUES (6951,'TemplateType','1','SUBJECT','Subject',1);
INSERT INTO `enum_lookup` VALUES (6952,'TemplateType','2','BODY_CONTENT','Body Content',2);
INSERT INTO `enum_lookup` VALUES (2701,'CSVStringEnclosedCharacterType','NONE','NONE','None',1);
INSERT INTO `enum_lookup` VALUES (2702,'CSVStringEnclosedCharacterType','\"','DOUBLE_QUOTE','Double Quote',2);
INSERT INTO `enum_lookup` VALUES (2703,'CSVStringEnclosedCharacterType','\'','SINGLE_QUOTE','Single Quote',3);
INSERT INTO `enum_lookup` VALUES (5351,'PublishedFileType','CSV','CSV','Csv',1);
INSERT INTO `enum_lookup` VALUES (5352,'PublishedFileType','XLS','EXCEL','Excel',2);
INSERT INTO `enum_lookup` VALUES (5353,'PublishedFileType','EXCELDB','EXCELDB','Excel DB',3);
INSERT INTO `enum_lookup` VALUES (5354,'PublishedFileType','RDBMS','RDBMS','Relation Database',4);
INSERT INTO `enum_lookup` VALUES (5355,'PublishedFileType','OTHER','OTHER','Other',5);
INSERT INTO `enum_lookup` VALUES (5356,'PublishedFileType','TSV','TSV','Tsv',6);
INSERT INTO `enum_lookup` VALUES (6301,'RFXRecordType','1','ENTITY','Entity',1);
INSERT INTO `enum_lookup` VALUES (6302,'RFXRecordType','2','PARTIAL','Partial',2);
INSERT INTO `enum_lookup` VALUES (6303,'RFXRecordType','3','FULL','Full',3);
INSERT INTO `enum_lookup` VALUES (4201,'NormalizedLocationType','1','LAT_LONG','Lat Long',1);
INSERT INTO `enum_lookup` VALUES (4202,'NormalizedLocationType','2','ZIPCODE','Zipcode',2);
INSERT INTO `enum_lookup` VALUES (4203,'NormalizedLocationType','3','BED_BASED','Bed Based',3);
INSERT INTO `enum_lookup` VALUES (2751,'CurrentPublisherFlowStatusType','U','UN_SUPPORTED','Un Supported',1);
INSERT INTO `enum_lookup` VALUES (2752,'CurrentPublisherFlowStatusType','A','ANALYZE','Analyze',2);
INSERT INTO `enum_lookup` VALUES (2753,'CurrentPublisherFlowStatusType','B','ANALYZING','Analyzing',3);
INSERT INTO `enum_lookup` VALUES (2754,'CurrentPublisherFlowStatusType','C','CONFIRM_AND_ENABLE','Confirm And Enable',4);
INSERT INTO `enum_lookup` VALUES (2755,'CurrentPublisherFlowStatusType','D','ENABLING','Enabling',5);
INSERT INTO `enum_lookup` VALUES (2756,'CurrentPublisherFlowStatusType','E','ENABLED','Enabled',6);
INSERT INTO `enum_lookup` VALUES (3851,'LocationConversionType','StateByCity','STATE_BY_CITY','State By City',1);
INSERT INTO `enum_lookup` VALUES (3852,'LocationConversionType','CityByState','CITY_BY_STATE','City By State',2);
INSERT INTO `enum_lookup` VALUES (3853,'LocationConversionType','CountryByState','COUNTRY_BY_STATE','Country By State',3);
INSERT INTO `enum_lookup` VALUES (3854,'LocationConversionType','StateByCountry','STATE_BY_COUNTRY','State By Country',4);
INSERT INTO `enum_lookup` VALUES (3855,'LocationConversionType','CountryByCity','COUNTRY_BY_CITY','Country By City',5);
INSERT INTO `enum_lookup` VALUES (3856,'LocationConversionType','CityByCountry','CITY_BY_COUNTRY','City By Country',6);
INSERT INTO `enum_lookup` VALUES (4951,'ParallelWordType','1','DEFAULT','Default',1);
INSERT INTO `enum_lookup` VALUES (4952,'ParallelWordType','3','Synonym','Synonym',2);
INSERT INTO `enum_lookup` VALUES (4953,'ParallelWordType','4','Abbreviation','Abbreviation',3);
INSERT INTO `enum_lookup` VALUES (4954,'ParallelWordType','5','Acronym','Acronym',4);
INSERT INTO `enum_lookup` VALUES (4955,'ParallelWordType','6','Code','Code',5);
INSERT INTO `enum_lookup` VALUES (4956,'ParallelWordType','7','Tag','Tag',6);
INSERT INTO `enum_lookup` VALUES (4957,'ParallelWordType','9','RelatedWord','Relatedword',7);
INSERT INTO `enum_lookup` VALUES (4958,'ParallelWordType','11','Inflection','Inflection',8);
INSERT INTO `enum_lookup` VALUES (4959,'ParallelWordType','10','LinguisticRoot','Linguisticroot',9);
INSERT INTO `enum_lookup` VALUES (7251,'UnstructuredColumnNameType','DISP_S1','DISP_S1','Disp S1',1);
INSERT INTO `enum_lookup` VALUES (7252,'UnstructuredColumnNameType','DISP_N1','DISP_N1','Disp N1',2);
INSERT INTO `enum_lookup` VALUES (7253,'UnstructuredColumnNameType','DISP_S2','DISP_S2','Disp S2',3);
INSERT INTO `enum_lookup` VALUES (7254,'UnstructuredColumnNameType','DISP_N2','DISP_N2','Disp N2',4);
INSERT INTO `enum_lookup` VALUES (7255,'UnstructuredColumnNameType','DISP_S3','DISP_S3','Disp S3',5);
INSERT INTO `enum_lookup` VALUES (7256,'UnstructuredColumnNameType','DISP_N3','DISP_N3','Disp N3',6);
INSERT INTO `enum_lookup` VALUES (7257,'UnstructuredColumnNameType','DISP_S4','DISP_S4','Disp S4',7);
INSERT INTO `enum_lookup` VALUES (7258,'UnstructuredColumnNameType','DISP_N4','DISP_N4','Disp N4',8);
INSERT INTO `enum_lookup` VALUES (7259,'UnstructuredColumnNameType','DISP_S5','DISP_S5','Disp S5',9);
INSERT INTO `enum_lookup` VALUES (7260,'UnstructuredColumnNameType','DISP_N5','DISP_N5','Disp N5',10);
INSERT INTO `enum_lookup` VALUES (7261,'UnstructuredColumnNameType','DISP_S6','DISP_S6','Disp S6',11);
INSERT INTO `enum_lookup` VALUES (7262,'UnstructuredColumnNameType','DISP_N6','DISP_N6','Disp N6',12);
INSERT INTO `enum_lookup` VALUES (7263,'UnstructuredColumnNameType','DISP_S7','DISP_S7','Disp S7',13);
INSERT INTO `enum_lookup` VALUES (7264,'UnstructuredColumnNameType','DISP_N7','DISP_N7','Disp N7',14);
INSERT INTO `enum_lookup` VALUES (7265,'UnstructuredColumnNameType','DISP_S8','DISP_S8','Disp S8',15);
INSERT INTO `enum_lookup` VALUES (7266,'UnstructuredColumnNameType','DISP_N8','DISP_N8','Disp N8',16);
INSERT INTO `enum_lookup` VALUES (7267,'UnstructuredColumnNameType','DISP_S9','DISP_S9','Disp S9',17);
INSERT INTO `enum_lookup` VALUES (7268,'UnstructuredColumnNameType','DISP_N9','DISP_N9','Disp N9',18);
INSERT INTO `enum_lookup` VALUES (7269,'UnstructuredColumnNameType','DISP_S10','DISP_S10','Disp S10',19);
INSERT INTO `enum_lookup` VALUES (7270,'UnstructuredColumnNameType','DISP_N10','DISP_N10','Disp N10',20);
INSERT INTO `enum_lookup` VALUES (1801,'BookmarkType','Q','QUERY_INTERFACE','Query Interface',1);
INSERT INTO `enum_lookup` VALUES (1802,'BookmarkType','S','SEARCH_INTERFACE','Search Interface',2);
INSERT INTO `enum_lookup` VALUES (4051,'MessageStatusType','CREATED','CREATED','Created',1);
INSERT INTO `enum_lookup` VALUES (4052,'MessageStatusType','PROCESSING','PROCESSING','Processing',2);
INSERT INTO `enum_lookup` VALUES (4053,'MessageStatusType','COMPLETED','COMPLETED','Completed',3);
INSERT INTO `enum_lookup` VALUES (4054,'MessageStatusType','ERROR','ERROR','Error',4);
INSERT INTO `enum_lookup` VALUES (601,'UniversalSearchResultItemType','1','PERFECT_MATCH','Perfect Match',1);
INSERT INTO `enum_lookup` VALUES (602,'UniversalSearchResultItemType','2','UNKNOWN_MATCH','Unknown Match',2);
INSERT INTO `enum_lookup` VALUES (603,'UniversalSearchResultItemType','3','PARTIAL_MATCH','Partial Match',3);
INSERT INTO `enum_lookup` VALUES (604,'UniversalSearchResultItemType','4','KEYWORD_MATCH','Keyword Match',4);
INSERT INTO `enum_lookup` VALUES (7951,'TimeFrameConversionOutputOperandType','SINGLE','SINGLE','Single',1);
INSERT INTO `enum_lookup` VALUES (7952,'TimeFrameConversionOutputOperandType','DOUBLE','DOUBLE','Double',2);
INSERT INTO `enum_lookup` VALUES (7953,'TimeFrameConversionOutputOperandType','MULTIPLE','MULTIPLE','Multiple',3);
INSERT INTO `enum_lookup` VALUES (7954,'TimeFrameConversionOutputOperandType','SUBCONDITION','SUBCONDITION','Subcondition',4);
INSERT INTO `enum_lookup` VALUES (801,'AnswersCatalogOperationType','CUBE_CREATION','CUBE_CREATION','Cube Creation',1);
INSERT INTO `enum_lookup` VALUES (802,'AnswersCatalogOperationType','CUBE_UPDATION','CUBE_UPDATION','Cube Updation',2);
INSERT INTO `enum_lookup` VALUES (803,'AnswersCatalogOperationType','CUBE_REFRESH','CUBE_REFRESH','Cube Refresh',3);
INSERT INTO `enum_lookup` VALUES (804,'AnswersCatalogOperationType','MART_CREATION','MART_CREATION','Mart Creation',4);
INSERT INTO `enum_lookup` VALUES (805,'AnswersCatalogOperationType','MART_UPDATION','MART_UPDATION','Mart Updation',5);
INSERT INTO `enum_lookup` VALUES (806,'AnswersCatalogOperationType','MART_REFRESH','MART_REFRESH','Mart Refresh',6);
INSERT INTO `enum_lookup` VALUES (807,'AnswersCatalogOperationType','PARENT_ASSET_SYNC_ABSORPTION','PARENT_ASSET_SYNC_ABSORPTION','Parent Asset Sync Absorption',7);
INSERT INTO `enum_lookup` VALUES (808,'AnswersCatalogOperationType','ASSET_DELETION','ASSET_DELETION','Asset Deletion',8);
INSERT INTO `enum_lookup` VALUES (7501,'HibernateCallbackParameterValueType','OBJECT','OBJECT','Object',1);
INSERT INTO `enum_lookup` VALUES (7502,'HibernateCallbackParameterValueType','COLLECTION','COLLECTION','Collection',2);
INSERT INTO `enum_lookup` VALUES (7503,'HibernateCallbackParameterValueType','ENUMERATION','ENUMERATION','Enumeration',3);
INSERT INTO `enum_lookup` VALUES (7504,'HibernateCallbackParameterValueType','SUGGEST','SUGGEST','Suggest',4);
INSERT INTO `enum_lookup` VALUES (7505,'HibernateCallbackParameterValueType','COLLECTION_ENUM','COLLECTION_ENUM','Collection Enum',5);
INSERT INTO `enum_lookup` VALUES (3201,'ExecueFacetType','N','NUMBER','Number',1);
INSERT INTO `enum_lookup` VALUES (3202,'ExecueFacetType','S','STRING','String',2);
INSERT INTO `enum_lookup` VALUES (7351,'ValidationRuleType','RegEx','REGEX_RULE','Regex Rule',1);
INSERT INTO `enum_lookup` VALUES (7352,'ValidationRuleType','logical','LOGICAL_RULE','Logical Rule',2);
INSERT INTO `enum_lookup` VALUES (6851,'SuggestTermType','CONCEPT','CONCEPT','Concept',1);
INSERT INTO `enum_lookup` VALUES (6852,'SuggestTermType','STAT','STAT','Stat',2);
INSERT INTO `enum_lookup` VALUES (6853,'SuggestTermType','CONCEPT_LOOKUP_INSTANCE','CONCEPT_LOOKUP_INSTANCE','Concept Lookup Instance',3);
INSERT INTO `enum_lookup` VALUES (6854,'SuggestTermType','VALUE','VALUE','Value',4);
INSERT INTO `enum_lookup` VALUES (6855,'SuggestTermType','PROFILE','PROFILE','Profile',5);
INSERT INTO `enum_lookup` VALUES (6856,'SuggestTermType','CONCEPT_PROFILE','CONCEPT_PROFILE','Concept Profile',6);
INSERT INTO `enum_lookup` VALUES (6857,'SuggestTermType','CONCEPT_LOOKUP_INSTANCE_PROFILE','CONCEPT_LOOKUP_INSTANCE_PROFILE','Concept Lookup Instance Profile',7);
INSERT INTO `enum_lookup` VALUES (6858,'SuggestTermType','FORMULA','FORMULA','Formula',8);
INSERT INTO `enum_lookup` VALUES (6859,'SuggestTermType','LOCATION','LOCATION','Location',9);
INSERT INTO `enum_lookup` VALUES (6651,'SelectEntityType','TABLE_COLUMN','TABLE_COLUMN','Table Column',1);
INSERT INTO `enum_lookup` VALUES (6652,'SelectEntityType','FORMULA','FORMULA','Formula',2);
INSERT INTO `enum_lookup` VALUES (6653,'SelectEntityType','SUB_QUERY','SUB_QUERY','Sub Query',3);
INSERT INTO `enum_lookup` VALUES (6654,'SelectEntityType','VALUE','VALUE','Value',4);
INSERT INTO `enum_lookup` VALUES (1,'MenuType','1','SIMPLE','Simple',1);
INSERT INTO `enum_lookup` VALUES (2,'MenuType','2','ADVANCE','Advance',2);
INSERT INTO `enum_lookup` VALUES (3,'MenuType','3','BOTH','Both',3);
INSERT INTO `enum_lookup` VALUES (7001,'TermType','1','ASSET_ENTITY','Asset Entity',1);
INSERT INTO `enum_lookup` VALUES (7002,'TermType','2','BUSINESS_ENTITY','Business Entity',2);
INSERT INTO `enum_lookup` VALUES (7003,'TermType','3','SFL_TERM_TOKEN_ENTITY','Sfl Term Token Entity',3);
INSERT INTO `enum_lookup` VALUES (7004,'TermType','4','RI_PARALLEL_TERM_ENTITY','Ri Parallel Term Entity',4);
INSERT INTO `enum_lookup` VALUES (7005,'TermType','5','APPLICATION','Application',5);
INSERT INTO `enum_lookup` VALUES (7006,'TermType','6','MODEL','Model',6);
INSERT INTO `enum_lookup` VALUES (1301,'AssetOwnerType','1','Client','Client',1);
INSERT INTO `enum_lookup` VALUES (1302,'AssetOwnerType','2','ExeCue','Execue',2);
INSERT INTO `enum_lookup` VALUES (3101,'EntityTriplePropertyType','REQUIRED','REQUIRED','Required',1);
INSERT INTO `enum_lookup` VALUES (3102,'EntityTriplePropertyType','PREFERRED','PREFERRED','Preferred',2);
INSERT INTO `enum_lookup` VALUES (3103,'EntityTriplePropertyType','DEFAULT','DEFAULT','Default',3);
INSERT INTO `enum_lookup` VALUES (3104,'EntityTriplePropertyType','NORMAL','NORMAL','Normal',4);
INSERT INTO `enum_lookup` VALUES (851,'AppCreationType','U','UNIFIED_PROCESS','Unified Process',1);
INSERT INTO `enum_lookup` VALUES (852,'AppCreationType','N','NORMAL_PROCESS','Normal Process',2);
INSERT INTO `enum_lookup` VALUES (3451,'FromEntityType','TABLE','TABLE','Table',1);
INSERT INTO `enum_lookup` VALUES (3452,'FromEntityType','SUB_QUERY','SUB_QUERY','Sub Query',2);
INSERT INTO `enum_lookup` VALUES (551,'UserStatus','A','ACTIVE','Active',1);
INSERT INTO `enum_lookup` VALUES (552,'UserStatus','I','INACTIVE','Inactive',2);
INSERT INTO `enum_lookup` VALUES (553,'UserStatus','D','DELETE','Delete',3);
INSERT INTO `enum_lookup` VALUES (1101,'AssetAnalysisThresholdType','OverThreshold','OVER_THRESHOLD','Over Threshold',1);
INSERT INTO `enum_lookup` VALUES (1102,'AssetAnalysisThresholdType','UnderThreshold','UNDER_THRESHOLD','Under Threshold',2);
INSERT INTO `enum_lookup` VALUES (5601,'PublisherFlowOperationType','T','FILE_TRANSFER','File Transfer',1);
INSERT INTO `enum_lookup` VALUES (5602,'PublisherFlowOperationType','U','ABSORPTION_AND_DATA_ANALYSIS','File Upload And Data Analysis',2);
INSERT INTO `enum_lookup` VALUES (5603,'PublisherFlowOperationType','M','METADATA_CREATION','Metadata Creation',3);
INSERT INTO `enum_lookup` VALUES (5604,'PublisherFlowOperationType','B','BUSINESS_MODEL_PREPARATION','Business Model Preparation',4);
INSERT INTO `enum_lookup` VALUES (6751,'StatType','COUNT','COUNT','Count',1);
INSERT INTO `enum_lookup` VALUES (6752,'StatType','AVG','AVERAGE','Average',2);
INSERT INTO `enum_lookup` VALUES (6753,'StatType','SUM','SUM','Sum',3);
INSERT INTO `enum_lookup` VALUES (6754,'StatType','MIN','MINIMUM','Minimum',4);
INSERT INTO `enum_lookup` VALUES (6755,'StatType','MAX','MAXIMUM','Maximum',5);
INSERT INTO `enum_lookup` VALUES (6756,'StatType','STDDEV','STDDEV','Stddev',6);
INSERT INTO `enum_lookup` VALUES (2101,'CloudComponentSelectionType','1','ENOUGH_FOR_CLOUD_SELECTION','Enough For Cloud Selection',1);
INSERT INTO `enum_lookup` VALUES (2102,'CloudComponentSelectionType','0','NOT_ENOUGH_FOR_CLOUD_SELECTION','Not Enough For Cloud Selection',2);
INSERT INTO `enum_lookup` VALUES (4651,'OrderEntityType','ASC','ASCENDING','Ascending',1);
INSERT INTO `enum_lookup` VALUES (4652,'OrderEntityType','DESC','DESCENDING','Descending',2);
INSERT INTO `enum_lookup` VALUES (7301,'UserInterfaceType','QI','QUERY_INTERFACE','Query Interface',1);
INSERT INTO `enum_lookup` VALUES (7302,'UserInterfaceType','SI','SIMPLE_SEARCH','Simple Search',2);
INSERT INTO `enum_lookup` VALUES (951,'AppSourceType','S','STRUCTURED','Structured',1);
INSERT INTO `enum_lookup` VALUES (952,'AppSourceType','U','UNSTRUCTURED','Unstructured',2);
INSERT INTO `enum_lookup` VALUES (953,'AppSourceType','W','WEBSERVICE','Webservice',3);
INSERT INTO `enum_lookup` VALUES (6601,'SearchType','Default','DEFAULT','Default',1);
INSERT INTO `enum_lookup` VALUES (6602,'SearchType','EntitySearch','ENTITY_SEARCH','Entity Search',2);
INSERT INTO `enum_lookup` VALUES (6603,'SearchType','KnowledgeSearch','KNOWLEDGE_SEARCH','Knowledge Search',3);
INSERT INTO `enum_lookup` VALUES (6604,'SearchType','QueryCache','QUERY_CACHE','Query Cache',4);
INSERT INTO `enum_lookup` VALUES (4351,'NotificationType','1','CUBE_CREATION','Cube Creation',1);
INSERT INTO `enum_lookup` VALUES (4352,'NotificationType','2','CUBE_UPDATION','Cube Updation',2);
INSERT INTO `enum_lookup` VALUES (4353,'NotificationType','3','MART_CREATION','Mart Creation',3);
INSERT INTO `enum_lookup` VALUES (4354,'NotificationType','4','MART_UPDATION','Mart Updation',4);
INSERT INTO `enum_lookup` VALUES (4355,'NotificationType','5','SFORCE_DATA_REPLICATION','Sforce Data Replication',5);
INSERT INTO `enum_lookup` VALUES (4356,'NotificationType','6','SFL_TERM_TOKEN_WEIGHT_UPDATION','Sfl Term Token Weight Updation',6);
INSERT INTO `enum_lookup` VALUES (4357,'NotificationType','7','RI_PARALLELWORD_UPDATION','Ri Parallelword Updation',7);
INSERT INTO `enum_lookup` VALUES (4358,'NotificationType','8','POPULARITY_HIT_UPDATION','Popularity Hit Updation',8);
INSERT INTO `enum_lookup` VALUES (4359,'NotificationType','9','PUBLISHER_DATA_ABSORPTION','Publisher Data Absorption',9);
INSERT INTO `enum_lookup` VALUES (4360,'NotificationType','10','PUBLISHER_DATA_EVALUATION','Publisher Data Evaluation',10);
INSERT INTO `enum_lookup` VALUES (4361,'NotificationType','11','FILE_ONTOLOGY_DATA_ABSORPTION','File Ontology Data Absorption',11);
INSERT INTO `enum_lookup` VALUES (4362,'NotificationType','12','RI_ONTO_TERMS_ABSORPTION','Ri Onto Terms Absorption',12);
INSERT INTO `enum_lookup` VALUES (4363,'NotificationType','13','SNOW_FLAKES_TERMS_ABSORPTION','Snow Flakes Terms Absorption',13);
INSERT INTO `enum_lookup` VALUES (4364,'NotificationType','14','CORRECT_MAPPINGS','Correct Mappings',14);
INSERT INTO `enum_lookup` VALUES (4365,'NotificationType','16','APPLICATION_DELETION','Application Deletion',15);
INSERT INTO `enum_lookup` VALUES (4366,'NotificationType','17','ASSET_DELETION','Asset Deletion',16);
INSERT INTO `enum_lookup` VALUES (4367,'NotificationType','18','POPULARITY_COLLECTION','Popularity Collection',17);
INSERT INTO `enum_lookup` VALUES (4368,'NotificationType','19','POPULARITY_DISPERSION','Popularity Dispersion',18);
INSERT INTO `enum_lookup` VALUES (4369,'NotificationType','21','MEMBER_ABSORPTION','Member Absorption',19);
INSERT INTO `enum_lookup` VALUES (4370,'NotificationType','22','RI_ONTO_TERM_POPULARITY_HIT_UPDATION','Ri Onto Term Popularity Hit Updation',20);
INSERT INTO `enum_lookup` VALUES (4371,'NotificationType','23','BUSINESS_MODEL_PREPARATION','Business Model Preparation',21);
INSERT INTO `enum_lookup` VALUES (4372,'NotificationType','24','PUBLISH_ASSET','Publish Asset',22);
INSERT INTO `enum_lookup` VALUES (4373,'NotificationType','25','INDEX_FORM_MANAGEMENT','Index Form Management',23);
INSERT INTO `enum_lookup` VALUES (4374,'NotificationType','26','INSTANCE_ABSORPTION','Instance Absorption',24);
INSERT INTO `enum_lookup` VALUES (4375,'NotificationType','27','DEFAULT_METRICS_POPULATION','Default Metrics Population',25);
INSERT INTO `enum_lookup` VALUES (4376,'NotificationType','28','SFL_WEIGHT_BY_SECWORD_UPDATION','Sfl Weight By Secword Updation',26);
INSERT INTO `enum_lookup` VALUES (4377,'NotificationType','29','CONCEPT_TYPE_ASSOCIATION','Concept Type Association',27);
INSERT INTO `enum_lookup` VALUES (4378,'NotificationType','30','MEMBER_SYNCHRONIZATION','Member Synchronization',28);
INSERT INTO `enum_lookup` VALUES (4379,'NotificationType','31','RUNTIME_TABLES_CLEANUP','Runtime Tables Cleanup',29);
INSERT INTO `enum_lookup` VALUES (4380,'NotificationType','32','SCHEDULED_POPULARITY_DISPERSION','Scheduled Popularity Dispersion',30);
INSERT INTO `enum_lookup` VALUES (4381,'NotificationType','33','EAS_INDEX_REFRESH','Eas Index Refresh',31);
INSERT INTO `enum_lookup` VALUES (4382,'NotificationType','34','VERTICAL_POPULARITY_UPDATION','Vertical Popularity Updation',32);
INSERT INTO `enum_lookup` VALUES (4383,'NotificationType','35','CRAIGSLIST_RUNTIME_TABLES_CLEANUP','Craigslist Runtime Tables Cleanup',33);
INSERT INTO `enum_lookup` VALUES (4384,'NotificationType','97','DATASET_COLLECTION_ACTIVATION','Dataset Collection Activation',34);
INSERT INTO `enum_lookup` VALUES (4385,'NotificationType','98','SYSTEM_MAINTENANCE','System Maintenance',35);
INSERT INTO `enum_lookup` VALUES (4386,'NotificationType','99','GENERAL_NOTIFICATION','General Notification',36);
INSERT INTO `enum_lookup` VALUES (4251,'NotificationCategory','1','OFFLINE_JOB','Offline Job',1);
INSERT INTO `enum_lookup` VALUES (4252,'NotificationCategory','2','ONLINE_MESSAGE','Online Message',2);
INSERT INTO `enum_lookup` VALUES (4253,'NotificationCategory','3','GENERAL_MESSAGE','General Message',3);
INSERT INTO `enum_lookup` VALUES (1901,'BusinessEntityTermType','C','CONCEPT','Concept',1);
INSERT INTO `enum_lookup` VALUES (1902,'BusinessEntityTermType','I','INSTANCE','Instance',2);
INSERT INTO `enum_lookup` VALUES (1903,'BusinessEntityTermType','R','RELATION','Relation',3);
INSERT INTO `enum_lookup` VALUES (1904,'BusinessEntityTermType','CP','CONCEPT_PROFILE','Concept Profile',4);
INSERT INTO `enum_lookup` VALUES (1905,'BusinessEntityTermType','IP','INSTANCE_PROFILE','Instance Profile',5);
INSERT INTO `enum_lookup` VALUES (151,'SecurityGroupType','1','ALL','All',1);
INSERT INTO `enum_lookup` VALUES (152,'SecurityGroupType','2','USER_GROUP','User Group',2);
INSERT INTO `enum_lookup` VALUES (153,'SecurityGroupType','3','PUBLIHSER_GROUP','Publisher Group',3);
INSERT INTO `enum_lookup` VALUES (154,'SecurityGroupType','4','ADV_PUBLISHER_GROUP','Advanced Publisher Group',4);
INSERT INTO `enum_lookup` VALUES (2201,'ColumnType','NULL','NULL','None',1);
INSERT INTO `enum_lookup` VALUES (2202,'ColumnType','ID','ID','Id',2);
INSERT INTO `enum_lookup` VALUES (2203,'ColumnType','MEASURE','MEASURE','Measure',3);
INSERT INTO `enum_lookup` VALUES (2204,'ColumnType','DIMENSION','DIMENSION','Lookup',4);
INSERT INTO `enum_lookup` VALUES (2205,'ColumnType','SL','SIMPLE_LOOKUP','Simple Lookup',5);
INSERT INTO `enum_lookup` VALUES (2206,'ColumnType','RL','RANGE_LOOKUP','Range Lookup',6);
INSERT INTO `enum_lookup` VALUES (2207,'ColumnType','DD','DEDUCED_DIMENSION','Deduced Dimension',7);
INSERT INTO `enum_lookup` VALUES (2208,'ColumnType','ND','NON_DEDUCABLE','Non Deducable',8);
INSERT INTO `enum_lookup` VALUES (2209,'ColumnType','SHL','SIMPLE_HIERARCHY_LOOKUP','Simple Hierarchy Lookup',9);
INSERT INTO `enum_lookup` VALUES (2551,'ConversionType','DATE','DATE','Date',1);
INSERT INTO `enum_lookup` VALUES (2552,'ConversionType','NUMBER','NUMBER','Number',2);
INSERT INTO `enum_lookup` VALUES (2553,'ConversionType','CURRENCY','CURRENCY','Currency',3);
INSERT INTO `enum_lookup` VALUES (2554,'ConversionType','DISTANCE','DISTANCE','Distance',4);
INSERT INTO `enum_lookup` VALUES (2555,'ConversionType','TEMPERATURE','TEMPERATURE','Temperature',5);
INSERT INTO `enum_lookup` VALUES (2556,'ConversionType','DEFAULT','DEFAULT','String',6);
INSERT INTO `enum_lookup` VALUES (2557,'ConversionType','NULL','NULL','None',7);
INSERT INTO `enum_lookup` VALUES (2558,'ConversionType','PERCENTAGE','PERCENTAGE','Percentage',8);
INSERT INTO `enum_lookup` VALUES (2559,'ConversionType','LOCATION','LOCATION','Location',9);
INSERT INTO `enum_lookup` VALUES (2560,'ConversionType','VOLUME','VOLUME','Volume',10);
INSERT INTO `enum_lookup` VALUES (2561,'ConversionType','POWER','POWER','Power',11);
INSERT INTO `enum_lookup` VALUES (2562,'ConversionType','WEIGHT','WEIGHT','Weight',12);
INSERT INTO `enum_lookup` VALUES (2563,'ConversionType','TIMEDURATION','TIMEDURATION','Time Duration',13);
INSERT INTO `enum_lookup` VALUES (2564,'ConversionType','MEMORY','MEMORY','Memory',14);
INSERT INTO `enum_lookup` VALUES (2565,'ConversionType','RESOLUTION','RESOLUTION','Resolution',15);
INSERT INTO `enum_lookup` VALUES (2566,'ConversionType','AREA','AREA','Area',16);
INSERT INTO `enum_lookup` VALUES (7551,'LocationLookupVariationType','ListById','LIST_BY_ID','List By Id',1);
INSERT INTO `enum_lookup` VALUES (7552,'LocationLookupVariationType','ListByList','LIST_BY_LIST','List By List',2);
INSERT INTO `enum_lookup` VALUES (7553,'LocationLookupVariationType','MapByList','MAP_BY_LIST','Map By List',3);
INSERT INTO `enum_lookup` VALUES (7701,'AssociationRuleType','weightAssignment','WEIGHT_ASSIGNMENT','Weight Assignment',1);
INSERT INTO `enum_lookup` VALUES (7702,'AssociationRuleType','validation','VALIDATION','Validation',2);
INSERT INTO `enum_lookup` VALUES (8001,'EncryptionAlgorithm','DES','DES','Des',1);
INSERT INTO `enum_lookup` VALUES (8002,'EncryptionAlgorithm','DESede','TRIPLE_DES','Triple Des',2);
INSERT INTO `enum_lookup` VALUES (5401,'PublishedOperationType','A','ADDITION','Addition',1);
INSERT INTO `enum_lookup` VALUES (5402,'PublishedOperationType','R','REFRESH','Refresh',2);
INSERT INTO `enum_lookup` VALUES (5403,'PublishedOperationType','E','APPEND','Append',3);
INSERT INTO `enum_lookup` VALUES (651,'ACManagementOperationSourceType','OPTIMAL_DSET','OPTIMAL_DSET','Optimal Dset',1);
INSERT INTO `enum_lookup` VALUES (652,'ACManagementOperationSourceType','ASSET_SYNC','ASSET_SYNC','Asset Sync',2);
INSERT INTO `enum_lookup` VALUES (653,'ACManagementOperationSourceType','USER_REQUEST','USER_REQUEST','User Request',3);
INSERT INTO `enum_lookup` VALUES (5251,'ProfileType','CONCEPT','CONCEPT','Concept',1);
INSERT INTO `enum_lookup` VALUES (5252,'ProfileType','CONCEPT_LOOKUP_INSTANCE','CONCEPT_LOOKUP_INSTANCE','Concept Lookup Instance',2);
INSERT INTO `enum_lookup` VALUES (2251,'ComponentCategory','1','TYPE','Type',1);
INSERT INTO `enum_lookup` VALUES (2252,'ComponentCategory','2','REALIZATION','Realization',2);
INSERT INTO `enum_lookup` VALUES (2253,'ComponentCategory','3','BEHAVIOR_WITH_TYPE','Behavior With Type',3);
INSERT INTO `enum_lookup` VALUES (2254,'ComponentCategory','4','ONLY_BEHAVIOR','Only Behavior',4);
INSERT INTO `enum_lookup` VALUES (1701,'BatchType','I','INSTANCE_ABSORPTION','Instance Absorption',1);
INSERT INTO `enum_lookup` VALUES (1702,'BatchType','M','MEMBER_ABSORPTION','Member Absorption',2);
INSERT INTO `enum_lookup` VALUES (6051,'RecognitionType','1','Exact','Exact',1);
INSERT INTO `enum_lookup` VALUES (6052,'RecognitionType','2','DisplayName','Displayname',2);
INSERT INTO `enum_lookup` VALUES (6053,'RecognitionType','3','Synonym','Synonym',3);
INSERT INTO `enum_lookup` VALUES (6054,'RecognitionType','4','Abbreviation','Abbreviation',4);
INSERT INTO `enum_lookup` VALUES (6055,'RecognitionType','5','Acronym','Acronym',5);
INSERT INTO `enum_lookup` VALUES (6056,'RecognitionType','6','Code','Code',6);
INSERT INTO `enum_lookup` VALUES (6057,'RecognitionType','7','Tag','Tag',7);
INSERT INTO `enum_lookup` VALUES (6058,'RecognitionType','8','Description','Description',8);
INSERT INTO `enum_lookup` VALUES (6059,'RecognitionType','9','RelatedWord','Relatedword',9);
INSERT INTO `enum_lookup` VALUES (6060,'RecognitionType','10','LinguisticRoot','Linguisticroot',10);
INSERT INTO `enum_lookup` VALUES (6061,'RecognitionType','11','EntityVariant','Entityvariant',11);
INSERT INTO `enum_lookup` VALUES (5051,'PathSelectionType','1','NORMAL_PATH','Normal Path',1);
INSERT INTO `enum_lookup` VALUES (5052,'PathSelectionType','2','DEFAULT_VALUE_PATH','Default Value Path',2);
INSERT INTO `enum_lookup` VALUES (1951,'BusinessEntityType','C','CONCEPT','Concept',1);
INSERT INTO `enum_lookup` VALUES (1952,'BusinessEntityType','CLI','CONCEPT_LOOKUP_INSTANCE','Concept Lookup Instance',2);
INSERT INTO `enum_lookup` VALUES (1953,'BusinessEntityType','R','RELATION','Relation',3);
INSERT INTO `enum_lookup` VALUES (1954,'BusinessEntityType','CR','CONCEPT_RELATION','Concept Relation',4);
INSERT INTO `enum_lookup` VALUES (1955,'BusinessEntityType','RI','REGEX_INSTANCE','Regex Instance',5);
INSERT INTO `enum_lookup` VALUES (1956,'BusinessEntityType','PRF','PROFILE','Profile',6);
INSERT INTO `enum_lookup` VALUES (1957,'BusinessEntityType','CP','CONCEPT_PROFILE','Concept Profile',7);
INSERT INTO `enum_lookup` VALUES (1958,'BusinessEntityType','IP','INSTANCE_PROFILE','Instance Profile',8);
INSERT INTO `enum_lookup` VALUES (1959,'BusinessEntityType','F','FORMULA','Formula',9);
INSERT INTO `enum_lookup` VALUES (1960,'BusinessEntityType','T','TYPE','Type',10);
INSERT INTO `enum_lookup` VALUES (1961,'BusinessEntityType','TLI','TYPE_LOOKUP_INSTANCE','Type Lookup Instance',11);
INSERT INTO `enum_lookup` VALUES (1962,'BusinessEntityType','RT','REALIZED_TYPE','Realized Type',12);
INSERT INTO `enum_lookup` VALUES (1963,'BusinessEntityType','RTLI','REALIZED_TYPE_LOOKUP_INSTANCE','Realized Type Lookup Instance',13);
INSERT INTO `enum_lookup` VALUES (1964,'BusinessEntityType','B','BEHAVIOR','Behavior',14);
INSERT INTO `enum_lookup` VALUES (5651,'PublisherProcessType','SPP','SIMPLIFIED_PUBLISHER_PROCESS','Simplified Publisher Process',1);
INSERT INTO `enum_lookup` VALUES (5652,'PublisherProcessType','DPP','DETAILED_PUBLISHER_PROCESS','Detailed Publisher Process',2);
INSERT INTO `enum_lookup` VALUES (2401,'ConnectionType','PROPERTIES','PROPERTIES','Properties',1);
INSERT INTO `enum_lookup` VALUES (2402,'ConnectionType','JNDI','JNDI','Jndi',2);
INSERT INTO `enum_lookup` VALUES (2601,'CSVDelimeter',',','COMMA','Comma',1);
INSERT INTO `enum_lookup` VALUES (2602,'CSVDelimeter','	','TAB','Tab',2);
INSERT INTO `enum_lookup` VALUES (7801,'AssociationDirectionType','1','LEFT_ASSOCIATION','Left Association',1);
INSERT INTO `enum_lookup` VALUES (7802,'AssociationDirectionType','2','RIGHT_ASSOCIATION','Right Association',2);
INSERT INTO `enum_lookup` VALUES (2301,'ConceptBaseType','MeasurableEntity','MEASURABLE_ENTITY','Measurable Entity',1);
INSERT INTO `enum_lookup` VALUES (2302,'ConceptBaseType','TimeFrame','TIME_FRAME','Time Frame',2);
INSERT INTO `enum_lookup` VALUES (2303,'ConceptBaseType','OntoEntity','ONTO_ENTITY','Onto Entity',3);
INSERT INTO `enum_lookup` VALUES (351,'GraphComponentType','1','Vertex','Vertex',1);
INSERT INTO `enum_lookup` VALUES (352,'GraphComponentType','2','Edge','Edge',2);
INSERT INTO `enum_lookup` VALUES (901,'AppOperationType','NO','NONE','None',1);
INSERT INTO `enum_lookup` VALUES (902,'AppOperationType','DA','ANALYZING','Analyzing Data',2);
INSERT INTO `enum_lookup` VALUES (903,'AppOperationType','DAC','ANALYZED','Data Analyzed',3);
INSERT INTO `enum_lookup` VALUES (904,'AppOperationType','AF','FULFILLING','Fulfilling Application',4);
INSERT INTO `enum_lookup` VALUES (905,'AppOperationType','AFC','FULFILLED','App Fulfilled',5);
INSERT INTO `enum_lookup` VALUES (906,'AppOperationType','AP','PUBLISHING','Publishing',6);
INSERT INTO `enum_lookup` VALUES (907,'AppOperationType','APC','PUBLISHED','Published',7);
INSERT INTO `enum_lookup` VALUES (908,'AppOperationType','AD','DELETING','Deleting',8);
INSERT INTO `enum_lookup` VALUES (909,'AppOperationType','ADC','DELETED','Deleted',9);
INSERT INTO `enum_lookup` VALUES (301,'TimeFrameType','DayTimeFrame','DAY_TIME_FRAME','Day Time Frame',1);
INSERT INTO `enum_lookup` VALUES (302,'TimeFrameType','WeekTimeFrame','WEEK_TIME_FRAME','Week Time Frame',2);
INSERT INTO `enum_lookup` VALUES (303,'TimeFrameType','MonthTimeFrame','MONTH_TIME_FRAME','Month Time Frame',3);
INSERT INTO `enum_lookup` VALUES (304,'TimeFrameType','QuarterTimeFrame','QUARTER_TIME_FRAME','Quarter Time Frame',4);
INSERT INTO `enum_lookup` VALUES (305,'TimeFrameType','YearTimeFrame','YEAR_TIME_FRAME','Year Time Frame',5);
INSERT INTO `enum_lookup` VALUES (306,'TimeFrameType','YearTimeFrameConcept','YEAR_TIME_FRAME_CONCEPT','Year Time Frame Concept',6);
INSERT INTO `enum_lookup` VALUES (307,'TimeFrameType','Time','TIME','Time',7);
INSERT INTO `enum_lookup` VALUES (3351,'FeatureDetailType','DISPLAYABLE_FEATURE','DISPLAYABLE_FEATURE','Displayable Feature',1);
INSERT INTO `enum_lookup` VALUES (3352,'FeatureDetailType','FACET_FEATURE','FACET_FEATURE','Facet Feature',2);
INSERT INTO `enum_lookup` VALUES (6201,'RFXEntityType','CT','CONCEPT_TRIPLE','Concept Triple',1);
INSERT INTO `enum_lookup` VALUES (6202,'RFXEntityType','IT','INSTANCE_TRIPLE','Instance Triple',2);
INSERT INTO `enum_lookup` VALUES (6203,'RFXEntityType','CRI','CONCEPT_INSTANCE_TRIPLE','Concept Instance Triple',3);
INSERT INTO `enum_lookup` VALUES (6204,'RFXEntityType','IRC','INSTANCE_CONCEPT_TRIPLE','Instance Concept Triple',4);
INSERT INTO `enum_lookup` VALUES (6205,'RFXEntityType','SC','SOURCE_CONCEPT','Source Concept',5);
INSERT INTO `enum_lookup` VALUES (6206,'RFXEntityType','SI','SOURCE_INSTANCE','Source Instance',6);
INSERT INTO `enum_lookup` VALUES (6207,'RFXEntityType','BED','BUSINESS_ENTITY_DEFINITION','Business Entity Definition',7);
INSERT INTO `enum_lookup` VALUES (1751,'BehaviorType','9001','ABSTRACT','Abstract',1);
INSERT INTO `enum_lookup` VALUES (1752,'BehaviorType','9002','ATTRIBUTE','Attribute',2);
INSERT INTO `enum_lookup` VALUES (1753,'BehaviorType','9003','QUANTITATIVE','Quantitative',3);
INSERT INTO `enum_lookup` VALUES (1754,'BehaviorType','9004','ENUMERATION','Enumeration',4);
INSERT INTO `enum_lookup` VALUES (1755,'BehaviorType','9005','POPULATION','Population',5);
INSERT INTO `enum_lookup` VALUES (1756,'BehaviorType','9006','COMPARATIVE','Comparative',6);
INSERT INTO `enum_lookup` VALUES (1757,'BehaviorType','9007','GRAIN','Grain',7);
INSERT INTO `enum_lookup` VALUES (1758,'BehaviorType','9008','DISTRIBUTION','Distribution',8);
INSERT INTO `enum_lookup` VALUES (1759,'BehaviorType','9009','MUTUALYEXCLUSIVE','Mutual Exclusive',9);
INSERT INTO `enum_lookup` VALUES (1760,'BehaviorType','9010','INDICATOR','Indicator',10);
INSERT INTO `enum_lookup` VALUES (1761,'BehaviorType','9011','MULTIVALUED','Multivalued',11);
INSERT INTO `enum_lookup` VALUES (1762,'BehaviorType','9012','MULTI_VALUED_GLOBAL_PENALTY','Multi Valued Global Penalty',12);
INSERT INTO `enum_lookup` VALUES (4801,'Owner','Y','YES','Yes',1);
INSERT INTO `enum_lookup` VALUES (4802,'Owner','N','NO','No',2);
INSERT INTO `enum_lookup` VALUES (6351,'RFXType','1','RFX_QUERY_CACHE','Rfx Query Cache',1);
INSERT INTO `enum_lookup` VALUES (6352,'RFXType','2','RFX_KNOWLEDGE_SEARCH','Rfx Knowledge Search',2);
INSERT INTO `enum_lookup` VALUES (6353,'RFXType','3','RFX_ENTITY_SEARCH','Rfx Entity Search',3);
INSERT INTO `enum_lookup` VALUES (6354,'RFXType','4','RFX_CONTENT','Rfx Content',4);
INSERT INTO `enum_lookup` VALUES (5801,'QueryElementType','Simple String','SIMPLE_STRING','Simple String',1);
INSERT INTO `enum_lookup` VALUES (5802,'QueryElementType','Case Statement','CASE_STATEMENT','Case Statement',2);
INSERT INTO `enum_lookup` VALUES (5803,'QueryElementType','Sub Query','SUB_QUERY','Sub Query',3);
INSERT INTO `enum_lookup` VALUES (2901,'DateQualifier','Year','YEAR','Year',1);
INSERT INTO `enum_lookup` VALUES (2902,'DateQualifier','Month','MONTH','Month',2);
INSERT INTO `enum_lookup` VALUES (2903,'DateQualifier','Quarter','QUARTER','Quarter',3);
INSERT INTO `enum_lookup` VALUES (2904,'DateQualifier','Day','DAY','Day',4);
INSERT INTO `enum_lookup` VALUES (2905,'DateQualifier','Hour','HOUR','Hour',5);
INSERT INTO `enum_lookup` VALUES (2906,'DateQualifier','Minute','MINUTE','Minute',6);
INSERT INTO `enum_lookup` VALUES (2907,'DateQualifier','Second','SECOND','Second',7);
INSERT INTO `enum_lookup` VALUES (2908,'DateQualifier','Week','WEEK','Week',8);
INSERT INTO `enum_lookup` VALUES (2351,'ConnectionEndPointType','SUBJECT','SUBJECT','Subject',1);
INSERT INTO `enum_lookup` VALUES (2352,'ConnectionEndPointType','OBJECT','OBJECT','Object',2);
INSERT INTO `enum_lookup` VALUES (1201,'AssetGrainType','DC','DISTRIBUTION_CONCEPT','Distribution Concept',1);
INSERT INTO `enum_lookup` VALUES (1202,'AssetGrainType','PC','POPULATION_CONCEPT','Population Concept',2);
INSERT INTO `enum_lookup` VALUES (1203,'AssetGrainType','DDC','DEFAULT_DISTRIBUTION_CONCEPT','Default Distribution Concept',3);
INSERT INTO `enum_lookup` VALUES (1204,'AssetGrainType','DPC','DEFAULT_POPULATION_CONCEPT','Default Population Concept',4);
INSERT INTO `enum_lookup` VALUES (1205,'AssetGrainType','GC','GRAIN_CONCEPT','Grain Concept',5);
INSERT INTO `enum_lookup` VALUES (6701,'SharedTypeModelMappingType','301','LOCATION','Location',1);
INSERT INTO `enum_lookup` VALUES (3751,'JobType','1','CUBE_CREATION','Cube Creation',1);
INSERT INTO `enum_lookup` VALUES (3752,'JobType','2','CUBE_UPDATION','Cube Updation',2);
INSERT INTO `enum_lookup` VALUES (3753,'JobType','3','MART_CREATION','Mart Creation',3);
INSERT INTO `enum_lookup` VALUES (3754,'JobType','4','MART_UPDATION','Mart Updation',4);
INSERT INTO `enum_lookup` VALUES (3755,'JobType','5','SFORCE_DATA_REPLICATION','Sforce Data Replication',5);
INSERT INTO `enum_lookup` VALUES (3756,'JobType','6','SFL_TERM_TOKEN_WEIGHT_UPDATION','Sfl Term Token Weight Updation',6);
INSERT INTO `enum_lookup` VALUES (3757,'JobType','7','RI_PARALLELWORD_UPDATION','Ri Parallelword Updation',7);
INSERT INTO `enum_lookup` VALUES (3758,'JobType','8','POPULARITY_HIT_UPDATION','Popularity Hit Updation',8);
INSERT INTO `enum_lookup` VALUES (3759,'JobType','9','PUBLISHER_DATA_ABSORPTION','Publisher Data Absorption',9);
INSERT INTO `enum_lookup` VALUES (3760,'JobType','10','PUBLISHER_DATA_EVALUATION','Publisher Data Evaluation',10);
INSERT INTO `enum_lookup` VALUES (3761,'JobType','11','FILE_ONTOLOGY_DATA_ABSORPTION','File Ontology Data Absorption',11);
INSERT INTO `enum_lookup` VALUES (3762,'JobType','12','RI_ONTO_TERMS_ABSORPTION','Ri Onto Terms Absorption',12);
INSERT INTO `enum_lookup` VALUES (3763,'JobType','13','SNOW_FLAKES_TERMS_ABSORPTION','Snow Flakes Terms Absorption',13);
INSERT INTO `enum_lookup` VALUES (3764,'JobType','14','CORRECT_MAPPINGS','Correct Mappings',14);
INSERT INTO `enum_lookup` VALUES (3765,'JobType','16','APPLICATION_DELETION','Application Deletion',15);
INSERT INTO `enum_lookup` VALUES (3766,'JobType','17','ASSET_DELETION','Asset Deletion',16);
INSERT INTO `enum_lookup` VALUES (3767,'JobType','18','POPULARITY_COLLECTION','Popularity Collection',17);
INSERT INTO `enum_lookup` VALUES (3768,'JobType','19','POPULARITY_DISPERSION','Popularity Dispersion',18);
INSERT INTO `enum_lookup` VALUES (3769,'JobType','21','MEMBER_ABSORPTION','Member Absorption',19);
INSERT INTO `enum_lookup` VALUES (3770,'JobType','22','RI_ONTO_TERM_POPULARITY_HIT_UPDATION','Ri Onto Term Popularity Hit Updation',20);
INSERT INTO `enum_lookup` VALUES (3771,'JobType','23','BUSINESS_MODEL_PREPARATION','Business Model Preparation',21);
INSERT INTO `enum_lookup` VALUES (3772,'JobType','24','PUBLISH_ASSET','Publish Asset',22);
INSERT INTO `enum_lookup` VALUES (3773,'JobType','25','INDEX_FORM_MANAGEMENT','Index Form Management',23);
INSERT INTO `enum_lookup` VALUES (3774,'JobType','26','INSTANCE_ABSORPTION','Instance Absorption',24);
INSERT INTO `enum_lookup` VALUES (3775,'JobType','27','DEFAULT_METRICS_POPULATION','Default Metrics Population',25);
INSERT INTO `enum_lookup` VALUES (3776,'JobType','28','SFL_WEIGHT_UPDATION_BY_SECWORD','Sfl Weight Updation By Secword',26);
INSERT INTO `enum_lookup` VALUES (3777,'JobType','29','CONCEPT_TYPE_ASSOCIATION','Concept Type Association',27);
INSERT INTO `enum_lookup` VALUES (3778,'JobType','30','SDX_SYNCHRONIZATION','Sdx Synchronization',28);
INSERT INTO `enum_lookup` VALUES (3779,'JobType','31','RUNTIME_TABLES_CLEANUP','Runtime Tables Cleanup',29);
INSERT INTO `enum_lookup` VALUES (3780,'JobType','32','SCHEDULED_POPULARITY_HIT_MAINTENANCE','Scheduled Popularity Hit Maintenance',30);
INSERT INTO `enum_lookup` VALUES (3781,'JobType','33','EAS_INDEX_REFRESH','Eas Index Refresh',31);
INSERT INTO `enum_lookup` VALUES (3782,'JobType','34','VERTICAL_POPULARITY_UPDATION','Vertical Popularity Updation',32);
INSERT INTO `enum_lookup` VALUES (3783,'JobType','35','CRAIGSLIST_RUNTIME_TABLES_CLEANUP','Craigslist Runtime Tables Cleanup',33);
INSERT INTO `enum_lookup` VALUES (3784,'JobType','36','FEATURE_COUNT','Feature Count',34);
INSERT INTO `enum_lookup` VALUES (3785,'JobType','37','CUBE_REFRESH','Cube Refresh',35);
INSERT INTO `enum_lookup` VALUES (3786,'JobType','38','MART_REFRESH','Mart Refresh',36);
INSERT INTO `enum_lookup` VALUES (3787,'JobType','39','PARENT_ASSET_SYNCHRONIZATION','Parent Asset Synchronization',37);
INSERT INTO `enum_lookup` VALUES (3788,'JobType','40','ANSWER_CATALOG_MANAGEMENT_QUEUE','Answer Catalog Management Queue',38);
INSERT INTO `enum_lookup` VALUES (3789,'JobType','41','OPTIMAL_DSET','Optimal Dset',39);
INSERT INTO `enum_lookup` VALUES (3790,'JobType','42','SCHEDULED_OPTIMAL_DSET','Scheduled Optimal Dset',40);
INSERT INTO `enum_lookup` VALUES (201,'StatusEnum','A','ACTIVE','Active',1);
INSERT INTO `enum_lookup` VALUES (202,'StatusEnum','I','INACTIVE','Inactive',2);
INSERT INTO `enum_lookup` VALUES (203,'StatusEnum','D','DELETE','Delete',3);
INSERT INTO `enum_lookup` VALUES (751,'AggregateQueryType','1','BUSINESS_SUMMARY','Business Summary',1);
INSERT INTO `enum_lookup` VALUES (752,'AggregateQueryType','2','TOTAL_SUMMARY','Total Summary',2);
INSERT INTO `enum_lookup` VALUES (753,'AggregateQueryType','3','DETAILED_SUMMARY','Detailed Summary',3);
INSERT INTO `enum_lookup` VALUES (754,'AggregateQueryType','4','HIERARCHY_SUMMARY','Hierarchy Summary',4);
INSERT INTO `enum_lookup` VALUES (1151,'AssetEntityType','A','ASSET','Asset',1);
INSERT INTO `enum_lookup` VALUES (1152,'AssetEntityType','T','TABLE','Table',2);
INSERT INTO `enum_lookup` VALUES (1153,'AssetEntityType','C','COLUMN','Column',3);
INSERT INTO `enum_lookup` VALUES (1154,'AssetEntityType','M','MEMBER','Member',4);
INSERT INTO `enum_lookup` VALUES (3151,'EntityType','C','CONCEPT','Concept',1);
INSERT INTO `enum_lookup` VALUES (3152,'EntityType','CLI','CONCEPT_LOOKUP_INSTANCE','Concept Lookup Instance',2);
INSERT INTO `enum_lookup` VALUES (3153,'EntityType','TLI','TYPE_LOOKUP_INSTANCE','Type Lookup Instance',3);
INSERT INTO `enum_lookup` VALUES (3154,'EntityType','R','RELATION','Relation',4);
INSERT INTO `enum_lookup` VALUES (3155,'EntityType','CP','CONCEPT_PROFILE','Concept Profile',5);
INSERT INTO `enum_lookup` VALUES (3156,'EntityType','IP','INSTANCE_PROFILE','Instance Profile',6);
INSERT INTO `enum_lookup` VALUES (3157,'EntityType','RANGE','RANGE','Range',7);
INSERT INTO `enum_lookup` VALUES (3158,'EntityType','KW','KEYWORD','Keyword',8);
INSERT INTO `enum_lookup` VALUES (3159,'EntityType','PW','PARALLEL_WORD','Parallel Word',9);
INSERT INTO `enum_lookup` VALUES (5851,'QueryFormulaOperandType','TABLE_COLUMN','TABLE_COLUMN','Table Column',1);
INSERT INTO `enum_lookup` VALUES (5852,'QueryFormulaOperandType','VALUE','VALUE','Value',2);
INSERT INTO `enum_lookup` VALUES (2151,'CloudOutput','N','NEW_VALUE','New Value',1);
INSERT INTO `enum_lookup` VALUES (2152,'CloudOutput','E','ENHANCED','Enhanced',2);
INSERT INTO `enum_lookup` VALUES (2153,'CloudOutput','I','INFORMATION','Information',3);
INSERT INTO `enum_lookup` VALUES (2801,'DataSourceType','CATALOG','CATALOG','Answers Catalog',1);
INSERT INTO `enum_lookup` VALUES (2802,'DataSourceType','UPLOADED','UPLOADED','Uploaded Dataset Container',2);
INSERT INTO `enum_lookup` VALUES (2803,'DataSourceType','REGULAR','REGULAR','Relational Database',3);
INSERT INTO `enum_lookup` VALUES (2804,'DataSourceType','SYSTEM_USWH','SYSTEM_UNSTRUCTURED_WAREHOUSE','System Unstructured Warehouse',4);
INSERT INTO `enum_lookup` VALUES (2805,'DataSourceType','SYSTEM_USCA','SYSTEM_UNSTRUCTURED_CONTENT_AGGREGATOR','System Unstructured Content Aggregator',5);
INSERT INTO `enum_lookup` VALUES (2806,'DataSourceType','SYSTEM_IP_DB','SYSTEM_IP_DB','SYSTEM Level IP Location Database',6);
INSERT INTO `enum_lookup` VALUES (2807,'DataSourceType','SYSTEM_CCZC','SYSTEM_CITY_CENTER_ZIP_CODE','SYSTEM Level City Center Zip Code Database',7);
INSERT INTO `enum_lookup` VALUES (2808,'DataSourceType','SYSTEM_SOLR','SYSTEM_SOLR','System Solr',8);
INSERT INTO `enum_lookup` VALUES (50005,'AssetProviderType','10','DERBY','DERBY',10);
INSERT INTO `enum_lookup` VALUES (50006,'AssetProviderType','11','POSTGRES','POSTGRES',11);
INSERT INTO `enum_lookup` VALUES (1763,'BehaviorType','9013','DEPENDENT_VARIABLE','Dependent Variable',13);
/*!40000 ALTER TABLE `enum_lookup` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `folder`
--

DROP TABLE IF EXISTS `folder`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `folder` (
  `ID` bigint(20) NOT NULL,
  `USER_ID` bigint(20) NOT NULL,
  `FOLDER_NAME` varchar(255) DEFAULT NULL,
  `DATE_CREATED` datetime DEFAULT NULL,
  `DATE_MODIFIED` datetime DEFAULT NULL,
  `PARENT_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_FL_USRID` (`USER_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `folder`
--

LOCK TABLES `folder` WRITE;
/*!40000 ALTER TABLE `folder` DISABLE KEYS */;
/*!40000 ALTER TABLE `folder` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `h_asset_operation_info`
--

DROP TABLE IF EXISTS `h_asset_operation_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `h_asset_operation_info` (
  `ID` bigint(20) NOT NULL,
  `ASSET_ID` bigint(20) NOT NULL,
  `START_DATE` datetime NOT NULL,
  `COMPLETION_DATE` datetime DEFAULT NULL,
  `ASSET_OPERATION_DATA` longtext,
  `CHANGE_FOUND` char(1) DEFAULT 'N',
  `STATUS` char(1) DEFAULT 'N',
  `OPERATION` char(1) NOT NULL,
  `OPERATION_TYPE` varchar(35) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `h_asset_operation_info`
--

LOCK TABLES `h_asset_operation_info` WRITE;
/*!40000 ALTER TABLE `h_asset_operation_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `h_asset_operation_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hierarchy`
--

DROP TABLE IF EXISTS `hierarchy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hierarchy` (
  `id` bigint(20) NOT NULL,
  `name` varchar(55) NOT NULL,
  `model_group_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_h_mg_id` (`model_group_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hierarchy`
--

LOCK TABLES `hierarchy` WRITE;
/*!40000 ALTER TABLE `hierarchy` DISABLE KEYS */;
/*!40000 ALTER TABLE `hierarchy` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hierarchy_detail`
--

DROP TABLE IF EXISTS `hierarchy_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hierarchy_detail` (
  `id` bigint(20) NOT NULL,
  `hierarchy_id` bigint(20) NOT NULL,
  `concept_bed_id` bigint(20) NOT NULL,
  `levl` int(2) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `fk_hd_hid` (`hierarchy_id`),
  KEY `idx_hd_cbedid` (`concept_bed_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hierarchy_detail`
--

LOCK TABLES `hierarchy_detail` WRITE;
/*!40000 ALTER TABLE `hierarchy_detail` DISABLE KEYS */;
/*!40000 ALTER TABLE `hierarchy_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `instance`
--

DROP TABLE IF EXISTS `instance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `instance` (
  `ID` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `DISPLAY_NAME` varchar(255) DEFAULT NULL,
  `EXPRESSION` varchar(255) DEFAULT NULL,
  `ABBR` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `instance`
--

LOCK TABLES `instance` WRITE;
/*!40000 ALTER TABLE `instance` DISABLE KEYS */;
INSERT INTO `instance` VALUES (1001,'Statistics1','Summation','Summation on data elements',NULL,'SUM');
INSERT INTO `instance` VALUES (1002,'Statistics2','Average','Average of data elements',NULL,'AVG');
INSERT INTO `instance` VALUES (1003,'Statistics3','Count','Count of data elements',NULL,'CNT');
INSERT INTO `instance` VALUES (1004,'Statistics4','Minimum','Minimum of data elements',NULL,'MIN');
INSERT INTO `instance` VALUES (1005,'Statistics5','Maximum','Maximum of data elements',NULL,'MAX');
INSERT INTO `instance` VALUES (1006,'Statistics6','Standard Deviation','Standard Deviation of data elements',NULL,'SD');
INSERT INTO `instance` VALUES (1051,'Operator1','Equals',NULL,'=','EQ');
INSERT INTO `instance` VALUES (1052,'Operator2','Less Than',NULL,'<','LT');
INSERT INTO `instance` VALUES (1053,'Operator3','Greater Than',NULL,'>','GT');
INSERT INTO `instance` VALUES (1054,'Operator4','Not Equals',NULL,'!=','NE');
INSERT INTO `instance` VALUES (1055,'Operator5','Less Than Equal To',NULL,'<=','LTE');
INSERT INTO `instance` VALUES (1056,'Operator6','Greater Than Equal To',NULL,'>=','GTE');
INSERT INTO `instance` VALUES (2001,'UnitScale1','Hundred','Hundred',NULL,'H');
INSERT INTO `instance` VALUES (2002,'UnitScale2','Thousand','Thousand',NULL,'K');
INSERT INTO `instance` VALUES (2003,'UnitScale3','Million','Million',NULL,'M');
INSERT INTO `instance` VALUES (2004,'UnitScale4','Billion','Billion',NULL,'B');
INSERT INTO `instance` VALUES (2005,'UnitScale5','Trillion','Trillion',NULL,'T');
INSERT INTO `instance` VALUES (5001,'CurrencySymbol1','Dollar','Dollar',NULL,'$');
INSERT INTO `instance` VALUES (5002,'CurrencySymbol2','Euro','Euro',NULL,'E');
INSERT INTO `instance` VALUES (1101,'Conjunction1','OF','OF',NULL,NULL);
INSERT INTO `instance` VALUES (1102,'Conjunction2','ON','ON',NULL,NULL);
INSERT INTO `instance` VALUES (1103,'Conjunction3','AT','AT',NULL,NULL);
INSERT INTO `instance` VALUES (1104,'Conjunction4','WITH','WITH',NULL,NULL);
INSERT INTO `instance` VALUES (1105,'Conjunction5','FROM','FROM',NULL,NULL);
INSERT INTO `instance` VALUES (1106,'Conjunction6','FOR','FOR',NULL,NULL);
INSERT INTO `instance` VALUES (1533,'Evening',NULL,'Evening',NULL,NULL);
INSERT INTO `instance` VALUES (1108,'Conjunction8','THOUGH','THOUGH',NULL,NULL);
INSERT INTO `instance` VALUES (1532,'Afternoon',NULL,'Afternoon',NULL,NULL);
INSERT INTO `instance` VALUES (1110,'Conjunction10','UNTIL','UNTIL',NULL,NULL);
INSERT INTO `instance` VALUES (1111,'Conjunction11','OR','OR',NULL,NULL);
INSERT INTO `instance` VALUES (1112,'Conjunction12','UNLESS','UNLESS',NULL,NULL);
INSERT INTO `instance` VALUES (1531,'Morning',NULL,'Morning',NULL,NULL);
INSERT INTO `instance` VALUES (1114,'Conjunction14','IN','IN',NULL,NULL);
INSERT INTO `instance` VALUES (1115,'Conjunction15','BASED','BASED',NULL,NULL);
INSERT INTO `instance` VALUES (1116,'By1','BY','BY',NULL,NULL);
INSERT INTO `instance` VALUES (1117,'CoordinatingConjunction1','AND','AND',NULL,NULL);
INSERT INTO `instance` VALUES (1118,'Preposition1','TO','TO',NULL,NULL);
INSERT INTO `instance` VALUES (1119,'Preposition2','THROUGH','THROUGH',NULL,NULL);
INSERT INTO `instance` VALUES (1200,'Adjective1','Last','Last',NULL,NULL);
INSERT INTO `instance` VALUES (1501,'January','January','January',NULL,'Jan');
INSERT INTO `instance` VALUES (1502,'February','February','February',NULL,'Feb');
INSERT INTO `instance` VALUES (1503,'March','March','March',NULL,'Mar');
INSERT INTO `instance` VALUES (1504,'April','April','April',NULL,'Apr');
INSERT INTO `instance` VALUES (1505,'May','May','May',NULL,'May');
INSERT INTO `instance` VALUES (1506,'June','June','June',NULL,'Jun');
INSERT INTO `instance` VALUES (1507,'July','July','July',NULL,'Jul');
INSERT INTO `instance` VALUES (1508,'August','August','August',NULL,'Aug');
INSERT INTO `instance` VALUES (1509,'September','September','September',NULL,'Sep');
INSERT INTO `instance` VALUES (1510,'October','October','October',NULL,'Oct');
INSERT INTO `instance` VALUES (1511,'November','November','November',NULL,'Nov');
INSERT INTO `instance` VALUES (1512,'December','December','December',NULL,'Dec');
INSERT INTO `instance` VALUES (6001,'Number1','Regular Expression for Number Value','Number Value','((\\d*)?(\\.)?(\\d+)(%)?)',NULL);
INSERT INTO `instance` VALUES (5201,'WeightSymbol1','Kilo','Kilo',NULL,'kg');
INSERT INTO `instance` VALUES (5202,'WeightSymbol2','Pound','Pound',NULL,'lbs');
INSERT INTO `instance` VALUES (5301,'DistanceSymbol1','Meter','Meter',NULL,'mtr');
INSERT INTO `instance` VALUES (5302,'DistanceSymbol2','Inches','Inches',NULL,'inch');
INSERT INTO `instance` VALUES (1120,'RangePreposition1','Between','Between',NULL,NULL);
INSERT INTO `instance` VALUES (1007,'ComparativeStatistics1','Top','Top most element of the Collection',NULL,NULL);
INSERT INTO `instance` VALUES (1008,'ComparativeStatistics2','Bottom','Smallest element of the collection',NULL,NULL);
INSERT INTO `instance` VALUES (1201,'Adjective2','First','First',NULL,NULL);
INSERT INTO `instance` VALUES (1121,'ValuePreposition1','over','over','>',NULL);
INSERT INTO `instance` VALUES (1122,'ValuePreposition2','under','under','<',NULL);
INSERT INTO `instance` VALUES (1123,'TimePreposition1','before','before','<',NULL);
INSERT INTO `instance` VALUES (1124,'TimePreposition2','after','after','>',NULL);
INSERT INTO `instance` VALUES (1125,'TimePreposition3','since','since','>=',NULL);
INSERT INTO `instance` VALUES (1126,'TimePreposition4','till','till','<=',NULL);
INSERT INTO `instance` VALUES (1513,'day','Regular Expression for Day Value','Day value','(^(([0]?1)|21|31)(st)?$)|(^(([0]?2)|22)(nd)?$)|(^(([0]?3)|23)(rd)?$)|(^(((0)?[4-9])|([1][0-9])|(20)|((2[4-9])|(30)))(th)?$)',NULL);
INSERT INTO `instance` VALUES (1150,'Punctuation1',',',',',NULL,NULL);
INSERT INTO `instance` VALUES (1151,'Punctuation2',';',';',NULL,NULL);
INSERT INTO `instance` VALUES (5303,'DistanceSymbol3','Kilometer','Kilometer',NULL,'km');
INSERT INTO `instance` VALUES (5304,'DistanceSymbol4','Miles','Miles',NULL,NULL);
INSERT INTO `instance` VALUES (5451,'PowerSymbol1','Horse Power','hp',NULL,'hp');
INSERT INTO `instance` VALUES (5461,'VolumeSymbol1','Cubic Centimeter','cc',NULL,'cc');
INSERT INTO `instance` VALUES (1515,'Time','Regular Expression for Time Value','Time value','^(([0-1]?[0-9])|([2][0-3])):([0-5]?[0-9])(:([0-5]?[0-9]))?$',NULL);
INSERT INTO `instance` VALUES (1152,'AM','AM','AM',NULL,NULL);
INSERT INTO `instance` VALUES (1153,'PM','PM','PM',NULL,NULL);
INSERT INTO `instance` VALUES (7001,'Currency1','Regular Expression for Currency Value','Currency value','(=|<|>|>=|<=)?\\s*([$])((\\d*)?(\\.)?(\\d+))([h,H,K,k,M,m,B,b,T,t]?)|(=|<|>|>=|<=)?\\s*((\\d*)?(\\.)?(\\d+))([h,H,K,k,M,m,B,b,T,t]?)([$])',NULL);
INSERT INTO `instance` VALUES (2006,'UnitScale6','One','One',NULL,NULL);
INSERT INTO `instance` VALUES (5305,'DistanceSymbol5','Foot','Foot',NULL,'ft');
INSERT INTO `instance` VALUES (5306,'DistanceSymbol6','Yard','Yard',NULL,'yd');
INSERT INTO `instance` VALUES (5452,'PowerSymbol2','Watt','Watt',NULL,NULL);
INSERT INTO `instance` VALUES (5453,'PowerSymbol3','Kilo Watt','KiloWatt',NULL,'kw');
INSERT INTO `instance` VALUES (5462,'VolumeSymbol2','Cubic Meter','CubicMeter',NULL,'cc');
INSERT INTO `instance` VALUES (5463,'VolumeSymbol3','Liter','Liter',NULL,NULL);
INSERT INTO `instance` VALUES (1127,'ValuePreposition3','below','below','<',NULL);
INSERT INTO `instance` VALUES (1128,'ValuePreposition4','above','above','>',NULL);
INSERT INTO `instance` VALUES (1202,'Adjective3','Next','Next',NULL,NULL);
INSERT INTO `instance` VALUES (1516,'Today','Today','Today',NULL,NULL);
INSERT INTO `instance` VALUES (1517,'Tomorrow','Tomorrow','Tomorrow',NULL,NULL);
INSERT INTO `instance` VALUES (1518,'Yesterday','Yesterday','Yesterday',NULL,NULL);
INSERT INTO `instance` VALUES (1519,'DayAfterTomorrow','Day After Tomorrow','Day After Tomorrow',NULL,NULL);
INSERT INTO `instance` VALUES (1520,'DayBeforeYesterday','Day Before Yesterday','Day Before Yesterday',NULL,NULL);
INSERT INTO `instance` VALUES (1521,'Sunday',NULL,'Sunday',NULL,'Sun');
INSERT INTO `instance` VALUES (1522,'Monday',NULL,'Monday',NULL,'Mon');
INSERT INTO `instance` VALUES (1523,'Tuesday',NULL,'Tuesday',NULL,'Tue');
INSERT INTO `instance` VALUES (1524,'Wednesday',NULL,'Wednesday',NULL,'Wed');
INSERT INTO `instance` VALUES (1525,'Thursday',NULL,'Thursday',NULL,'Thu');
INSERT INTO `instance` VALUES (1526,'Friday',NULL,'Friday',NULL,'Fri');
INSERT INTO `instance` VALUES (1527,'Saturday',NULL,'Saturday',NULL,'Sat');
INSERT INTO `instance` VALUES (1528,'Weekend',NULL,'Weekend',NULL,NULL);
INSERT INTO `instance` VALUES (1534,'Night',NULL,'Night',NULL,NULL);
INSERT INTO `instance` VALUES (5464,'VolumeSymbol4','CubicFeet','Cubic Feet',NULL,NULL);
INSERT INTO `instance` VALUES (5465,'VolumeSymbol5','Pints','Pints',NULL,NULL);
INSERT INTO `instance` VALUES (5466,'VolumeSymbol6','Gallons','Gallons',NULL,NULL);
INSERT INTO `instance` VALUES (7002,'AreaSymbol1','square meter','square meter',NULL,NULL);
INSERT INTO `instance` VALUES (7003,'AreaSymbol2','square inch','square inch',NULL,NULL);
INSERT INTO `instance` VALUES (7004,'AreaSymbol3','square feet','square feet',NULL,NULL);
INSERT INTO `instance` VALUES (7005,'AreaSymbol4','square yard','square yard',NULL,NULL);
INSERT INTO `instance` VALUES (7006,'AreaSymbol5','acre','acre',NULL,NULL);
INSERT INTO `instance` VALUES (7007,'AreaSymbol6','hectare','hectare',NULL,NULL);
INSERT INTO `instance` VALUES (7008,'MemorySymbol1','Mega Bytes','Mega Bytes',NULL,NULL);
INSERT INTO `instance` VALUES (7009,'MemorySymbol2','Giga Bytes','Giga Bytes',NULL,NULL);
INSERT INTO `instance` VALUES (7010,'MemorySymbol3','Kilo Bytes','Kilo Bytes',NULL,NULL);
INSERT INTO `instance` VALUES (7011,'ResolutionSymbol1','Mega Pixels','Mega Pixels',NULL,NULL);
INSERT INTO `instance` VALUES (7012,'ResolutionSymbol2','Pixels','Pixels',NULL,NULL);
/*!40000 ALTER TABLE `instance` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `instance_map_suggest_det`
--

DROP TABLE IF EXISTS `instance_map_suggest_det`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `instance_map_suggest_det` (
  `ID` bigint(20) NOT NULL,
  `INSTANCE_MAPPING_SUGGESTION_ID` bigint(20) DEFAULT NULL,
  `MEMBR_AE_ID` bigint(20) NOT NULL,
  `MEMBR_DISPLAY_NAME` varchar(255) DEFAULT NULL,
  `INSTANCE_ID` bigint(20) DEFAULT NULL,
  `INSTANCE_BE_ID` bigint(20) DEFAULT NULL,
  `INSTANCE_DISPLAY_NAME` varchar(255) DEFAULT NULL,
  `INSTANCE_DESCRIPTION` varchar(255) DEFAULT NULL,
  `INSTANCE_EXISTS` char(1) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_INSTANCE_MAP_SUGGES_DET` (`INSTANCE_MAPPING_SUGGESTION_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `instance_map_suggest_det`
--

LOCK TABLES `instance_map_suggest_det` WRITE;
/*!40000 ALTER TABLE `instance_map_suggest_det` DISABLE KEYS */;
/*!40000 ALTER TABLE `instance_map_suggest_det` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `instance_mapping_suggestion`
--

DROP TABLE IF EXISTS `instance_mapping_suggestion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `instance_mapping_suggestion` (
  `ID` bigint(20) NOT NULL,
  `APPLICATION_ID` bigint(20) NOT NULL,
  `MODEL_ID` bigint(20) NOT NULL,
  `MODEL_GROUP_ID` bigint(20) NOT NULL,
  `ASSET_AE_ID` bigint(20) NOT NULL,
  `TABL_AE_ID` bigint(20) NOT NULL,
  `TABL_DISPLAY_NAME` varchar(255) DEFAULT NULL,
  `COLUM_AE_ID` bigint(20) NOT NULL,
  `COLUM_DISPLAY_NAME` varchar(255) DEFAULT NULL,
  `CONCEPT_BE_ID` bigint(20) NOT NULL,
  `CONCEPT_DISPLAY_NAME` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `instance_mapping_suggestion`
--

LOCK TABLES `instance_mapping_suggestion` WRITE;
/*!40000 ALTER TABLE `instance_mapping_suggestion` DISABLE KEYS */;
/*!40000 ALTER TABLE `instance_mapping_suggestion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `instance_path_definition`
--

DROP TABLE IF EXISTS `instance_path_definition`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `instance_path_definition` (
  `ID` bigint(20) NOT NULL,
  `SOURCE_BE_ID` bigint(20) NOT NULL,
  `DESTINATION_BE_ID` bigint(20) NOT NULL,
  `PATH_DEFINITION_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_IPD_SBID` (`SOURCE_BE_ID`),
  KEY `FK_IPD_PDID` (`PATH_DEFINITION_ID`),
  KEY `FK_IPD_DBID` (`DESTINATION_BE_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `instance_path_definition`
--

LOCK TABLES `instance_path_definition` WRITE;
/*!40000 ALTER TABLE `instance_path_definition` DISABLE KEYS */;
/*!40000 ALTER TABLE `instance_path_definition` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `instance_profile`
--

DROP TABLE IF EXISTS `instance_profile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `instance_profile` (
  `ID` bigint(20) NOT NULL,
  `CONCEPT_ID` bigint(20) DEFAULT NULL,
  `NAME` varchar(255) NOT NULL,
  `DISPLAY_NAME` varchar(255) NOT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `ENABLED` char(1) NOT NULL DEFAULT '1',
  `USER_ID` bigint(20) NOT NULL,
  `MODEL_GROUP_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_IP` (`NAME`,`MODEL_GROUP_ID`),
  KEY `FK_IP_USRID` (`USER_ID`),
  KEY `FK_IP_CID` (`CONCEPT_ID`),
  KEY `FK_MGID_INSTANCE_PROFILE` (`MODEL_GROUP_ID`),
  KEY `IDX_IP_CID` (`CONCEPT_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `instance_profile`
--

LOCK TABLES `instance_profile` WRITE;
/*!40000 ALTER TABLE `instance_profile` DISABLE KEYS */;
/*!40000 ALTER TABLE `instance_profile` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `instance_profile_detail`
--

DROP TABLE IF EXISTS `instance_profile_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `instance_profile_detail` (
  `PROFILE_ID` bigint(20) NOT NULL,
  `INSTANCE_ID` bigint(20) DEFAULT NULL,
  KEY `FK_IPD_IID` (`INSTANCE_ID`),
  KEY `FK_IPD_CPID` (`PROFILE_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `instance_profile_detail`
--

LOCK TABLES `instance_profile_detail` WRITE;
/*!40000 ALTER TABLE `instance_profile_detail` DISABLE KEYS */;
/*!40000 ALTER TABLE `instance_profile_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `instance_triple_definition`
--

DROP TABLE IF EXISTS `instance_triple_definition`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `instance_triple_definition` (
  `ID` bigint(20) NOT NULL,
  `SRC_INSTANCE_BE_ID` bigint(20) DEFAULT NULL,
  `DEST_INSTANCE_BE_ID` bigint(20) DEFAULT NULL,
  `SRC_CONCEPT_BE_ID` bigint(20) DEFAULT NULL,
  `DEST_CONCEPT_BE_ID` bigint(20) DEFAULT NULL,
  `RELATION_BE_ID` bigint(20) DEFAULT NULL,
  `VALUE` varchar(255) DEFAULT NULL,
  `CLOUD_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `idx_itd_src_ins` (`SRC_INSTANCE_BE_ID`),
  KEY `idx_itd_des_ins` (`DEST_INSTANCE_BE_ID`),
  KEY `idx_itd_src_cnpt` (`SRC_CONCEPT_BE_ID`),
  KEY `idx_itd_dest_cnpt` (`DEST_CONCEPT_BE_ID`),
  KEY `idx_itd_rel` (`RELATION_BE_ID`),
  KEY `idx_itd_cld_id` (`CLOUD_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `instance_triple_definition`
--

LOCK TABLES `instance_triple_definition` WRITE;
/*!40000 ALTER TABLE `instance_triple_definition` DISABLE KEYS */;
/*!40000 ALTER TABLE `instance_triple_definition` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `joins`
--

DROP TABLE IF EXISTS `joins`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `joins` (
  `ID` bigint(20) NOT NULL,
  `ASSET_ID` bigint(20) NOT NULL,
  `SOURCE_TABLE_NAME` varchar(255) DEFAULT NULL,
  `DESTINATION_TABLE_NAME` varchar(255) DEFAULT NULL,
  `JOIN_ORDER` int(3) NOT NULL DEFAULT '0',
  `JOIN_LENGTH` int(3) NOT NULL DEFAULT '0',
  PRIMARY KEY (`ID`),
  KEY `FK_ASSET_ID1` (`ASSET_ID`),
  KEY `IDX_JN_DTNM` (`DESTINATION_TABLE_NAME`),
  KEY `IDX_JN_STNM` (`SOURCE_TABLE_NAME`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `joins`
--

LOCK TABLES `joins` WRITE;
/*!40000 ALTER TABLE `joins` DISABLE KEYS */;
/*!40000 ALTER TABLE `joins` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `joins_definition`
--

DROP TABLE IF EXISTS `joins_definition`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `joins_definition` (
  `ID` bigint(20) NOT NULL,
  `ASSET_ID` bigint(20) NOT NULL,
  `SOURCE_TABLE_NAME` varchar(255) DEFAULT NULL,
  `DESTINATION_TABLE_NAME` varchar(255) DEFAULT NULL,
  `SOURCE_COLUMN_NAME` varchar(255) DEFAULT NULL,
  `DESTINATION_COLUMN_NAME` varchar(255) DEFAULT NULL,
  `TYPE` varchar(35) NOT NULL DEFAULT 'INNER',
  PRIMARY KEY (`ID`),
  KEY `FK_ASSET_ID` (`ASSET_ID`),
  KEY `IDX_JD_DCNM` (`DESTINATION_COLUMN_NAME`),
  KEY `IDX_JD_DTNM` (`DESTINATION_TABLE_NAME`),
  KEY `IDX_JD_SCNM` (`SOURCE_COLUMN_NAME`),
  KEY `IDX_JD_STNM` (`SOURCE_TABLE_NAME`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `joins_definition`
--

LOCK TABLES `joins_definition` WRITE;
/*!40000 ALTER TABLE `joins_definition` DISABLE KEYS */;
/*!40000 ALTER TABLE `joins_definition` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `joins_joins_definition`
--

DROP TABLE IF EXISTS `joins_joins_definition`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `joins_joins_definition` (
  `JOINS_ID` bigint(20) NOT NULL,
  `JOINS_DEFINITION_ID` bigint(20) NOT NULL,
  KEY `FK_JOINS_ID` (`JOINS_ID`),
  KEY `FK_JD_ID1` (`JOINS_DEFINITION_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `joins_joins_definition`
--

LOCK TABLES `joins_joins_definition` WRITE;
/*!40000 ALTER TABLE `joins_joins_definition` DISABLE KEYS */;
/*!40000 ALTER TABLE `joins_joins_definition` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `key_word`
--

DROP TABLE IF EXISTS `key_word`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `key_word` (
  `ID` bigint(20) NOT NULL,
  `BE_ID` bigint(20) DEFAULT NULL,
  `WORD` varchar(255) DEFAULT NULL,
  `MODEL_GROUP_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_KWD` (`WORD`,`MODEL_GROUP_ID`),
  KEY `FK_KW_BEID` (`BE_ID`),
  KEY `FK_MGID_KEY_WORD` (`MODEL_GROUP_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `key_word`
--

LOCK TABLES `key_word` WRITE;
/*!40000 ALTER TABLE `key_word` DISABLE KEYS */;
INSERT INTO `key_word` VALUES (101,2001,'Hundred',1);
INSERT INTO `key_word` VALUES (102,2002,'Thousand',1);
INSERT INTO `key_word` VALUES (103,2003,'Million',1);
INSERT INTO `key_word` VALUES (104,2004,'Billion',1);
INSERT INTO `key_word` VALUES (105,2005,'Trillion',1);
INSERT INTO `key_word` VALUES (106,5001,'Dollar',1);
INSERT INTO `key_word` VALUES (107,NULL,'Day',NULL);
INSERT INTO `key_word` VALUES (108,1051,'=',1);
INSERT INTO `key_word` VALUES (109,1052,'<',1);
INSERT INTO `key_word` VALUES (110,1053,'>',1);
INSERT INTO `key_word` VALUES (111,1054,'!=',1);
INSERT INTO `key_word` VALUES (112,1055,'<=',1);
INSERT INTO `key_word` VALUES (113,1056,'>=',1);
INSERT INTO `key_word` VALUES (114,1200,'Last',1);
/*!40000 ALTER TABLE `key_word` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mapping`
--

DROP TABLE IF EXISTS `mapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mapping` (
  `ID` bigint(20) NOT NULL,
  `ASSET_ENTITY_ID` bigint(20) NOT NULL,
  `BUSINESS_ENTITY_ID` bigint(20) NOT NULL,
  `GRAIN` varchar(6) DEFAULT NULL,
  `PRIMRY` char(1) NOT NULL DEFAULT 'N',
  PRIMARY KEY (`ID`),
  KEY `FK_MP_BEID` (`BUSINESS_ENTITY_ID`),
  KEY `FK_MP_AEID` (`ASSET_ENTITY_ID`),
  KEY `IDX_MAP_PF` (`PRIMRY`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mapping`
--

LOCK TABLES `mapping` WRITE;
/*!40000 ALTER TABLE `mapping` DISABLE KEYS */;
/*!40000 ALTER TABLE `mapping` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `membr`
--

DROP TABLE IF EXISTS `membr`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `membr` (
  `ID` bigint(20) NOT NULL,
  `LOOKUP_VALUE` varchar(255) DEFAULT NULL,
  `LOOKUP_DESCRIPTION` varchar(255) DEFAULT NULL,
  `LONG_DESCRIPTION` varchar(255) DEFAULT NULL,
  `LOWER_LIMIT` decimal(20,6) DEFAULT NULL,
  `UPPER_LIMIT` decimal(20,6) DEFAULT NULL,
  `KDX_LOOKUP_DESCRIPTION` varchar(255) DEFAULT NULL,
  `INDICATOR_BEHAVIOR` varchar(20) DEFAULT NULL,
  `ORIGINAL_DESCRIPTION` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `membr`
--

LOCK TABLES `membr` WRITE;
/*!40000 ALTER TABLE `membr` DISABLE KEYS */;
/*!40000 ALTER TABLE `membr` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `model`
--

DROP TABLE IF EXISTS `model`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `model` (
  `ID` bigint(20) NOT NULL,
  `NAME` varchar(255) NOT NULL,
  `CREATED_DATE` datetime DEFAULT NULL,
  `MODIFIED_DATE` datetime DEFAULT NULL,
  `POPULARITY` bigint(20) NOT NULL,
  `STATUS` char(1) DEFAULT 'A',
  `EVALUATE` char(1) NOT NULL DEFAULT 'Y',
  `INDEX_EVALUATION` varchar(1) DEFAULT 'Y',
  `category` varchar(10) DEFAULT 'USER',
  PRIMARY KEY (`ID`),
  KEY `IDX_MDL_NM` (`NAME`),
  KEY `IDX_MDL_ST` (`STATUS`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `model`
--

LOCK TABLES `model` WRITE;
/*!40000 ALTER TABLE `model` DISABLE KEYS */;
INSERT INTO `model` VALUES (1,'Base Model','2009-12-10 00:00:00',NULL,0,'A','N','Y','BASE');
INSERT INTO `model` VALUES (2,'Location','2012-09-08 12:56:58',NULL,0,'A','N','N','SYSTEM');
/*!40000 ALTER TABLE `model` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `model_cloud`
--

DROP TABLE IF EXISTS `model_cloud`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `model_cloud` (
  `MODEL_ID` bigint(20) NOT NULL,
  `CLOUD_ID` bigint(20) NOT NULL,
  KEY `FK_model_cloud_MODEL` (`MODEL_ID`),
  KEY `FK_model_cloud_CLOUD` (`CLOUD_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `model_cloud`
--

LOCK TABLES `model_cloud` WRITE;
/*!40000 ALTER TABLE `model_cloud` DISABLE KEYS */;
INSERT INTO `model_cloud` VALUES (1,101);
INSERT INTO `model_cloud` VALUES (1,102);
INSERT INTO `model_cloud` VALUES (1,104);
INSERT INTO `model_cloud` VALUES (1,105);
/*!40000 ALTER TABLE `model_cloud` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `model_group`
--

DROP TABLE IF EXISTS `model_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `model_group` (
  `ID` bigint(20) NOT NULL,
  `NAME` varchar(255) NOT NULL,
  `category` varchar(10) DEFAULT 'USER',
  `context_id` bigint(20) DEFAULT NULL,
  `shared` char(1) DEFAULT 'N',
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `model_group`
--

LOCK TABLES `model_group` WRITE;
/*!40000 ALTER TABLE `model_group` DISABLE KEYS */;
INSERT INTO `model_group` VALUES (1,'Base Group','BASE',1,'N');
INSERT INTO `model_group` VALUES (2,'Location','SYSTEM',301,'Y');
/*!40000 ALTER TABLE `model_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `model_group_mapping`
--

DROP TABLE IF EXISTS `model_group_mapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `model_group_mapping` (
  `ID` bigint(20) NOT NULL,
  `MODEL_ID` bigint(20) NOT NULL,
  `MODEL_GROUP_ID` bigint(20) NOT NULL,
  `OWNER` char(1) DEFAULT NULL,
  `PRIMRY` char(1) DEFAULT 'N',
  PRIMARY KEY (`ID`),
  KEY `FK_MID_MODEL` (`MODEL_ID`),
  KEY `FK_MGI_MODEL_GROUP` (`MODEL_GROUP_ID`),
  KEY `IDX_MGM_OWNR` (`OWNER`),
  KEY `IDX_MGM_PF` (`PRIMRY`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `model_group_mapping`
--

LOCK TABLES `model_group_mapping` WRITE;
/*!40000 ALTER TABLE `model_group_mapping` DISABLE KEYS */;
INSERT INTO `model_group_mapping` VALUES (1,1,1,'Y','Y');
INSERT INTO `model_group_mapping` VALUES (2,2,2,'Y','Y');
/*!40000 ALTER TABLE `model_group_mapping` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `parallel_word`
--

DROP TABLE IF EXISTS `parallel_word`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `parallel_word` (
  `ID` bigint(20) NOT NULL,
  `KEY_WORD_ID` bigint(20) NOT NULL,
  `PARALLEL_WORD` varchar(255) NOT NULL,
  `PREFIX_SPACE` int(1) NOT NULL DEFAULT '1',
  `SUFFIX_SPACE` int(1) NOT NULL DEFAULT '1',
  `USER_ID` bigint(20) NOT NULL,
  `PWD_TYPE` int(2) NOT NULL DEFAULT '1',
  `relevance` decimal(10,2) NOT NULL DEFAULT '0.00',
  `POPULARITY` bigint(20) NOT NULL,
  `pos_type` varchar(2) DEFAULT NULL,
  `IS_DIFFERENT_WORD` char(1) DEFAULT 'Y',
  PRIMARY KEY (`ID`),
  KEY `FK_PW_KWID` (`KEY_WORD_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `parallel_word`
--

LOCK TABLES `parallel_word` WRITE;
/*!40000 ALTER TABLE `parallel_word` DISABLE KEYS */;
INSERT INTO `parallel_word` VALUES (101,101,'h',1,1,1,1,'0.00',0,NULL,'Y');
INSERT INTO `parallel_word` VALUES (102,102,'k',1,1,0,1,'0.00',0,NULL,'Y');
INSERT INTO `parallel_word` VALUES (103,103,'m',1,1,0,1,'0.00',0,NULL,'Y');
INSERT INTO `parallel_word` VALUES (104,104,'b',1,1,0,1,'0.00',0,NULL,'Y');
INSERT INTO `parallel_word` VALUES (105,105,'t',1,1,1,1,'0.00',0,NULL,'Y');
INSERT INTO `parallel_word` VALUES (106,106,'$',1,1,1,1,'0.00',0,NULL,'Y');
INSERT INTO `parallel_word` VALUES (108,108,'EQ',1,1,0,1,'0.85',0,NULL,'Y');
INSERT INTO `parallel_word` VALUES (109,109,'LT',1,1,0,1,'0.85',0,NULL,'Y');
INSERT INTO `parallel_word` VALUES (110,110,'GT',1,1,0,1,'0.85',0,NULL,'Y');
INSERT INTO `parallel_word` VALUES (111,111,'NE',1,1,0,1,'0.85',0,NULL,'Y');
INSERT INTO `parallel_word` VALUES (112,112,'LTE',1,1,0,1,'0.85',0,NULL,'Y');
INSERT INTO `parallel_word` VALUES (113,113,'GTE',1,1,0,1,'0.85',0,NULL,'Y');
INSERT INTO `parallel_word` VALUES (114,114,'Old',1,1,1,3,'0.95',0,NULL,'Y');
/*!40000 ALTER TABLE `parallel_word` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `parallel_word_cluster`
--

DROP TABLE IF EXISTS `parallel_word_cluster`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `parallel_word_cluster` (
  `PARALLEL_WORD_ID` bigint(20) NOT NULL,
  `BE_ID` bigint(20) NOT NULL,
  KEY `FK_PWC_PWID` (`PARALLEL_WORD_ID`),
  KEY `FK_PWC_BEID` (`BE_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `parallel_word_cluster`
--

LOCK TABLES `parallel_word_cluster` WRITE;
/*!40000 ALTER TABLE `parallel_word_cluster` DISABLE KEYS */;
/*!40000 ALTER TABLE `parallel_word_cluster` ENABLE KEYS */;
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
) ENGINE=MyISAM AUTO_INCREMENT=208 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patch_info`
--

LOCK TABLES `patch_info` WRITE;
/*!40000 ALTER TABLE `patch_info` DISABLE KEYS */;
INSERT INTO `patch_info` VALUES (1,'swi-clean-4.7.2.R1.03','DML','For introducing Time Duration conversion type','2011-02-09','2012-09-08 12:56:52','Raju Gottumukkala','4.7.2.R1','03','CleanApp','03','SWI');
INSERT INTO `patch_info` VALUES (2,'swi-clean-4.7.2.R1.06','DML','For updating the weights in Unit cloud Comps','2011-02-09','2012-09-08 12:56:52','Nihar Agrawal','4.7.2.R1','06','CleanApp','06','SWI');
INSERT INTO `patch_info` VALUES (3,'swi-clean-4.7.2.R1.07','DML','For Adding new Cloud for Value Realization as TimeDuration','2011-02-09','2012-09-08 12:56:52','Nihar Agrawal','4.7.2.R1','07','4','07','SWI');
INSERT INTO `patch_info` VALUES (4,'swi-clean-4.7.2.R1.08','DDL','Added Column in Colum Table','2011-02-10','2012-09-08 12:56:53','Jitendra Tiwari','4.7.2.R1','08','CleanApp','08','SWI');
INSERT INTO `patch_info` VALUES (5,'swi-clean-4.7.2.R1.11','DML','Introduce a new Type Called Time Preposition and migrated few instances of valuePrep to this.','2011-02-16','2012-09-08 12:56:53','Nihar Agrawl','4.7.2.R1','11','CleanApp','11','SWI');
INSERT INTO `patch_info` VALUES (6,'swi-clean-4.7.2.R1.12','DML','A new RegEx Rule for relative TFs with operator','2011-02-17','2012-09-08 12:56:53','Nihar Agrawl','4.7.2.R1','12','CleanApp','12','SWI');
INSERT INTO `patch_info` VALUES (7,'swi-clean-4.7.2.R1.16','DML','Added record for enum UdxCarsInfoSortType','2011-02-21','2012-09-08 12:56:53','Jitendra Tiwari','4.7.2.R1','16','CleanApp','16','SWI');
INSERT INTO `patch_info` VALUES (8,'swi-clean-4.7.3.R1.06','DML','Script to insert new rule for month cloud','2011-03-08','2012-09-08 12:56:53','Nihar','4.7.3.R1','06','CleanApp','06','SWI');
INSERT INTO `patch_info` VALUES (9,'swi-clean-4.7.3.R1.07','DDL','Script to create EAS tables','2011-03-08','2012-09-08 12:56:55','Vishay','4.7.3.R1','07','CleanApp','07','SWI');
INSERT INTO `patch_info` VALUES (10,'swi-clean-4.7.4.R1.00','DDL','For Adding Category Based Patch Number to Patch Info table','2011-03-09','2012-09-08 12:56:55','Raju Gottumukkala','4.7.4.R1','00','CleanApp','00','SWI');
INSERT INTO `patch_info` VALUES (11,'4.7.4.R1-swi-clean-C01-U01','DDL','Added record for enum UdxCarsInfoSortType','2011-03-09','2012-09-08 12:56:55','Jitendra Tiwari','4.7.4.R1','U01','CleanApp','C01','SWI');
INSERT INTO `patch_info` VALUES (12,'4.7.4.R1-swi-clean-C02-U02','DML','Updated discription for enum ','2011-03-11','2012-09-08 12:56:55','Jitendra Tiwari','4.7.4.R1','U02','CleanApp','C02','SWI');
INSERT INTO `patch_info` VALUES (13,'4.7.4.R1-swi-clean-C03-U03','DDL','Added original_description column to the membr table ','2011-03-14','2012-09-08 12:56:55','Vishay Guptha','4.7.4.R1','U03','CleanApp','C03','SWI');
INSERT INTO `patch_info` VALUES (14,'4.7.4.R1-swi-clean-C04-U06','DML','Update model_groupId to 1 for Ri_parallel_words if its null ','2011-03-16','2012-09-08 12:56:55','Nihar Agrawal','4.7.4.R1','U06','CleanApp','C04','SWI');
INSERT INTO `patch_info` VALUES (15,'4.7.5.R1-swi-clean-C01-U01','DML','Patch to add new instances for valuePreposition ','2011-03-21','2012-09-08 12:56:55','Abhijit Patil','4.7.5.R1','U01','CleanApp','C01','SWI');
INSERT INTO `patch_info` VALUES (16,'4.7.5.R1-swi-clean-C02-U02','DML','Adding new formats for Day','2011-03-24','2012-09-08 12:56:55','Raju Gottumukkala','4.7.5.R1','U02','CleanApp','C02','SWI');
INSERT INTO `patch_info` VALUES (17,'4.7.4.R1-swi-clean-C03-U04','DML','Added enum for  BusinessEntityTermType to show in synonms screen ','2011-03-25','2012-09-08 12:56:55','Jitendra Tiwari','4.7.4.R1','U04','CleanApp','C03','SWI');
INSERT INTO `patch_info` VALUES (18,'4.7.4.R1-swi-clean-C04-U05','DML','Setting the visibility to the types needed','2011-03-28','2012-09-08 12:56:55','Raju Gottumukkala','4.7.4.R1','U05','CleanApp','C04','SWI');
INSERT INTO `patch_info` VALUES (19,'4.7.5.R1-swi-clean-C05-U06','DML','Update the Name to digit for the base Number Type','2011-03-29','2012-09-08 12:56:55','Nihar Agrawal','4.7.5.R1','U06','CleanApp','C05','SWI');
INSERT INTO `patch_info` VALUES (20,'4.7.5.R1-swi-clean-C06-U07','DML','added virt_table_desc_column_exists_on_src ','2011-03-31','2012-09-08 12:56:55','Vishay','4.7.5.R1','U07','CleanApp','C06','SWI');
INSERT INTO `patch_info` VALUES (21,'4.7.5.R1-swi-clean-C07-U09','DML','Adding new time formats for Second','2011-03-24','2012-09-08 12:56:55','Raju Gottumukkala','4.7.5.R1','U09','CleanApp','C07','SWI');
INSERT INTO `patch_info` VALUES (22,'4.7.5.R1-swi-clean-C08-U10','DML','patch to update the last key word related ids to base id','2011-04-15','2012-09-08 12:56:55','Nitesh','4.7.5.R1','U10','CleanApp','C08','SWI');
INSERT INTO `patch_info` VALUES (23,'4.7.6.R1-swi-clean-C01-U01','DML','Added enum for  SourceType','2011-04-20','2012-09-08 12:56:55','Jitendra Tiwari','4.7.6.R1','U01','CleanApp','C01','SWI');
INSERT INTO `patch_info` VALUES (24,'4.7.6.R1-swi-clean-C02-U02','DML','Added ontoTerms for hour minute and second types','2011-04-20','2012-09-08 12:56:55','Nihar','4.7.6.R1','U02','CleanApp','C02','SWI');
INSERT INTO `patch_info` VALUES (25,'4.7.6.R1-swi-clean-C03-U03','DML','Added next as adjective to work with relative clouds','2011-04-20','2012-09-08 12:56:55','Nihar','4.7.6.R1','U03','CleanApp','C03','SWI');
INSERT INTO `patch_info` VALUES (26,'4.7.6.R1-swi-clean-C04-U04','DML','Added Conjunction as allowed comp for Time cloud','2011-04-20','2012-09-08 12:56:55','Nihar','4.7.6.R1','U04','CleanApp','C04','SWI');
INSERT INTO `patch_info` VALUES (27,'4.7.6.R1-swi-clean-C05-U05','DML','Added today tomorrow, yesterday as TF instances','2011-04-20','2012-09-08 12:56:55','Nihar','4.7.6.R1','U05','CleanApp','C05','SWI');
INSERT INTO `patch_info` VALUES (28,'4.7.6.R1-swi-clean-C06-U06','DML','Added Relative Week Cloud','2011-04-21','2012-09-08 12:56:55','Nihar','4.7.6.R1','U06','CleanApp','C06','SWI');
INSERT INTO `patch_info` VALUES (29,'4.7.6.R1-swi-clean-C07-U07','DML','Added Type and instances for weekdays','2011-04-21','2012-09-08 12:56:55','Nihar','4.7.6.R1','U07','CleanApp','C07','SWI');
INSERT INTO `patch_info` VALUES (30,'4.7.6.R1-swi-clean-C08-U08','DML','Added relative cloud for weekday','2011-04-27','2012-09-08 12:56:55','Nihar','4.7.6.R1','U08','CleanApp','C08','SWI');
INSERT INTO `patch_info` VALUES (31,'4.7.6.R1-swi-clean-C09-U09','DML','Added Timeframe cloud for Weekday instances','2011-04-27','2012-09-08 12:56:56','Nihar','4.7.6.R1','U09','CleanApp','C09','SWI');
INSERT INTO `patch_info` VALUES (32,'4.7.6.R1-swi-clean-C10-U11','DML','Script to add weekend as weekday intance','2011-05-02','2012-09-08 12:56:56','Nihar Agrawal','4.7.6.R1','U11','CleanApp','C10','SWI');
INSERT INTO `patch_info` VALUES (33,'4.7.6.R1-swi-clean-C11-U12','DML','Script to update today/tomorrow etc as day instyance instead of timeframe instances','2011-05-03','2012-09-08 12:56:56','Nihar Agrawal','4.7.6.R1','U12','CleanApp','C11','SWI');
INSERT INTO `patch_info` VALUES (34,'4.7.6.R1-swi-clean-C12-U19','DML','Script to update comp Type name to valuePreposition in clouds with comp type id as value prep','2011-05-09','2012-09-08 12:56:56','Nihar Agrawal','4.7.6.R1','U19','CleanApp','C12','SWI');
INSERT INTO `patch_info` VALUES (35,'4.7.6.R1-swi-clean-C13-U20','DML','Added entry in requorce table','2011-05-09','2012-09-08 12:56:56','Jitendra Tiwari','4.7.6.R1','U20','CleanApp','C13','SWI');
INSERT INTO `patch_info` VALUES (36,'4.7.6.R1-swi-clean-C14-U21','DML','To delte conjucntion instance which are already instances of Time Preposition too','2011-05-10','2012-09-08 12:56:56','Nihar Agrawal','4.7.6.R1','U21','CleanApp','C14','SWI');
INSERT INTO `patch_info` VALUES (37,'4.7.6.R1-swi-clean-C15-U22','DML','To mark preposition as secondary words in SFL term tokens','2011-05-10','2012-09-08 12:56:56','Nihar Agrawal','4.7.6.R1','U22','CleanApp','C15','SWI');
INSERT INTO `patch_info` VALUES (38,'4.7.7.R1-swi-clean-C01-U04','DML','Script to add new Time instances like morning , evening','2011-05-20','2012-09-08 12:56:56','Nihar Agrawal','4.7.7.R1','U04','CleanApp','C01','SWI');
INSERT INTO `patch_info` VALUES (39,'4.7.7.R1-swi-clean-C02-U05','DML','Added enum for  UdxCarsInfoSortType','2011-05-23','2012-09-08 12:56:56','Jitendra Tiwari','4.7.7.R1','U05','CleanApp','C02','SWI');
INSERT INTO `patch_info` VALUES (40,'4.8.0.R1-swi-clean-C01-U01','DML','Scripts to Add the weekday as cloud comp for time TF cloud','2011-06-07','2012-09-08 12:56:57','Nihar','4.8.0.R1','U01','CleanApp','C01','SWI');
INSERT INTO `patch_info` VALUES (41,'4.8.0.R1-swi-clean-C02-U02','DML','Scripts to update the weight of the weekday as cloud comp for time TF cloud','2011-06-08','2012-09-08 12:56:57','Nihar','4.8.0.R1','U02','CleanApp','C02','SWI');
INSERT INTO `patch_info` VALUES (42,'4.8.0.R1-swi-clean-C03-U04','DML','Scripts to Add column','2011-06-10','2012-09-08 12:56:57','Jitehn','4.8.0.R1','U04','CleanApp','C03','SWI');
INSERT INTO `patch_info` VALUES (43,'4.8.0.R1-swi-clean-C04-U06','DML','Scripts to drop column','2011-06-10','2012-09-08 12:56:57','Jitehn','4.8.0.R1','U06','CleanApp','C04','SWI');
INSERT INTO `patch_info` VALUES (44,'4.8.0.R1-swi-clean-C05-U07','DDL','Scripts to add column entity_be_id in ri_onto_term','2011-06-11','2012-09-08 12:56:57','Nihar','4.8.0.R1','U07','CleanApp','C05','SWI');
INSERT INTO `patch_info` VALUES (45,'4.8.0.R1-swi-clean-C06-U08','DDL','Scripts to insert missing eiontoterms entries for base relations','2011-06-11','2012-09-08 12:56:58','Nihar','4.8.0.R1','U08','CleanApp','C06','SWI');
INSERT INTO `patch_info` VALUES (46,'4.8.0.R1-swi-clean-C07-U09','DML','To Update  Entity_be_id column in ri_ontoTerm','2011-06-11','2012-09-08 12:56:58','Nihar','4.8.0.R1','U09','CleanApp','C07','SWI');
INSERT INTO `patch_info` VALUES (47,'4.8.0.R1-swi-clean-C08-U14','DML','Drop statement for unsued table default_column_value and tabl_indx','2011-06-13','2012-09-08 12:56:58','Jitendra Tiwari','4.8.0.R1','U14','CleanApp','C08','SWI');
INSERT INTO `patch_info` VALUES (48,'4.8.0.R1-swi-clean-C08-U15','DML','Insert missing ObntoTerms for RTs in base','2011-06-13','2012-09-08 12:56:58','Nihar Agrawal','4.8.0.R1','U15','CleanApp','C08','SWI');
INSERT INTO `patch_info` VALUES (49,'4.8.0.R1-swi-clean-C10-U16','DML','Changes to Model, Model Grou pand Model Group Mapping tables','2011-06-20','2012-09-08 12:56:58','Vishay Gupta','4.8.0.R1','U16','CleanApp','C10','SWI');
INSERT INTO `patch_info` VALUES (50,'4.8.0.R1-swi-clean-C11-U18','DML','Create basic MGM structure for Location Model','2011-06-20','2012-09-08 12:56:58','Vishay Gupta','4.8.0.R1','U18','CleanApp','C11','SWI');
INSERT INTO `patch_info` VALUES (51,'4.8.0.R1-swi-clean-C12-U20','DML','Create entries for ModelCategoryType Enum in enum lookup','2011-06-20','2012-09-08 12:56:58','Vishay Gupta','4.8.0.R1','U20','CleanApp','C12','SWI');
INSERT INTO `patch_info` VALUES (52,'4.8.0.R1-swi-clean-C13-U21','DML','Entries for LocationLookupVariationType and LocationConversionType in enum lookup','2011-06-22','2012-09-08 12:56:58','Raju Gottumukkala','4.8.0.R1','U21','CleanApp','C13','SWI');
INSERT INTO `patch_info` VALUES (53,'4.8.0.R1-swi-clean-C14-U24','DML','Patch to set Rquired flag to false for weekday comp in Time Cloud','2011-06-23','2012-09-08 12:56:58','Nihar Agrawal','4.8.0.R1','U24','CleanApp','C14','SWI');
INSERT INTO `patch_info` VALUES (54,'4.8.0.R1-swi-clean-C15-U25','DML','Script to udpate the unit symbol related concept realization and its instances','2011-06-29','2012-09-08 12:56:58','Nitesh','4.8.0.R1','U25','CleanApp','C15','SWI');
INSERT INTO `patch_info` VALUES (55,'4.8.0.R1-swi-clean-C16-U27','DML','Script to udpate the report group image url','2011-07-06','2012-09-08 12:56:58','Jitendra','4.8.0.R1','U27','CleanApp','C16','SWI');
INSERT INTO `patch_info` VALUES (56,'4.8.1.R1-swi-clean-C01-U01','DDL','Script to Add Column realizable in type to mark the types which can be realized in apps','2011-07-13','2012-09-08 12:56:58','Nihar','4.8.1.R1','U01','CleanApp','C01','SWI');
INSERT INTO `patch_info` VALUES (57,'4.8.1.R1-swi-clean-C02-U02','DML','Script to update entity type column in business_entity table with TLI for Weekday Type instead of T','2011-07-14','2012-09-08 12:56:58','Prasanna','4.8.1.R1','U02','CleanApp','C02','SWI');
INSERT INTO `patch_info` VALUES (58,'4.8.1.R1-swi-clean-C03-U03','DML','Script to add new column FROM_SHARED to business_entity table','2011-07-14','2012-09-08 12:56:59','Murthy S.N','4.8.1.R1','U03','CleanApp','C03','SWI');
INSERT INTO `patch_info` VALUES (59,'4.8.1.R1-swi-clean-C04-U04','DDL','Script to alter cloumn names in cloud table','2011-07-19','2012-09-08 12:56:59','Prasanna','4.8.1.R1','U04','CleanApp','C04','SWI');
INSERT INTO `patch_info` VALUES (60,'4.8.1.R1-swi-clean-C05-U05','DDL','Script to alter cloumn name in cloud_component table','2011-07-19','2012-09-08 12:56:59','Prasanna','4.8.1.R1','U05','CleanApp','C05','SWI');
INSERT INTO `patch_info` VALUES (61,'4.8.1.R1-swi-clean-C06-U06','DDL','Script to alter cloumn name in ri_cloud table','2011-07-19','2012-09-08 12:56:59','Prasanna','4.8.1.R1','U06','CleanApp','C06','SWI');
INSERT INTO `patch_info` VALUES (62,'4.8.1.R1-swi-clean-C07-U07','DML','Script to update display_name of unit in type table','2011-07-19','2012-09-08 12:56:59','Prasanna','4.8.1.R1','U07','CleanApp','C07','SWI');
INSERT INTO `patch_info` VALUES (63,'4.8.1.R1-swi-clean-C08-U08','DDL','Script to delete power, volume types from type,business_entity,ri_onto_term tables','2011-07-20','2012-09-08 12:56:59','Prasanna','4.8.1.R1','U08','CleanApp','C08','SWI');
INSERT INTO `patch_info` VALUES (64,'4.8.1.R1-swi-clean-C09-U09','DDL','Script to Drop columns from business_entity','2011-07-21','2012-09-08 12:56:59','MurthySN','4.8.1.R1','U09','CleanApp','C09','SWI');
INSERT INTO `patch_info` VALUES (65,'4.8.1.R1-swi-clean-C10-U10','DML','Script to create path between location and city,state,country','2011-07-22','2012-09-08 12:56:59','Prasanna','4.8.1.R1','U10','CleanApp','C10','SWI');
INSERT INTO `patch_info` VALUES (66,'4.8.1.R1-swi-clean-C11-U11','DML','Script to alter cloumn names in ri_cloud table','2011-07-25','2012-09-08 12:57:00','Prasanna','4.8.1.R1','U11','CleanApp','C11','SWI');
INSERT INTO `patch_info` VALUES (67,'4.8.1.R1-swi-clean-C12-U14','DML','Script to delete distance concept from type and business_entity tables','2011-07-29','2012-09-08 12:57:00','Prasanna','4.8.1.R1','U14','CleanApp','C12','SWI');
INSERT INTO `patch_info` VALUES (68,'4.8.1.R1-swi-clean-C13-U15','DML','Script to update the model year detail type in craigslist app','2011-08-02','2012-09-08 12:57:00','Nitesh','4.8.1.R1','U15','CleanApp','C13','SWI');
INSERT INTO `patch_info` VALUES (69,'4.8.1.R1-swi-clean-C14-U16','DML','Script to set application and model id for queries from advanced search','2011-08-03','2012-09-08 12:57:00','Raju Gottumukkala','4.8.1.R1','U16','CleanApp','C14','SWI');
INSERT INTO `patch_info` VALUES (70,'4.8.1.R1-swi-portal-P03-U17','DML','Script to update on business entity for entity type from LI to CLI','2011-08-04','2012-09-08 12:57:00','Raju Gottumukkala','4.8.1.R1','U17','PortalApp','P03','SWI');
INSERT INTO `patch_info` VALUES (71,'4.8.2.R1-swi-clean-C00-U00','DML','Blank Script just to denote the release','2011-08-04','2012-09-08 12:57:00','Raju Gottumukkala','4.8.2.R1','U00','CleanApp','C00','SWI');
INSERT INTO `patch_info` VALUES (72,'4.8.3.R1-swi-clean-C00-U00','DML','Blank Script just to denote the release','2011-08-19','2012-09-08 12:57:00','Raju Gottumukkala','4.8.3.R1','U00','CleanApp','C00','SWI');
INSERT INTO `patch_info` VALUES (73,'4.8.3.R1-swi-clean-C01-U01','DML','Script to update the EntityType enum to BusinessEntityType as per the code changes','2011-08-22','2012-09-08 12:57:00','Nitesh','4.8.3.R1','U01','CleanApp','C01','SWI');
INSERT INTO `patch_info` VALUES (74,'swi-clean-4.8.3.R1.02','DDL','Script to create business_entity_variation table and add an entry into sequences for the new table','2011-08-23','2012-09-08 12:57:00','John Mallavalli','4.8.3.R1','02','CleanApp',NULL,'SWI');
INSERT INTO `patch_info` VALUES (75,'4.8.3.R1-swi-clean-C03-U03','DML','Added entry in requorce table','2011-08-29','2012-09-08 12:57:00','Jitendra Tiwari','4.8.3.R1','U03','CleanApp','C03','SWI');
INSERT INTO `patch_info` VALUES (76,'4.8.3.R1-swi-clean-C04-U04','DDL','New table creation APP_DATA_SOURCE','2011-08-29','2012-09-08 12:57:00','Vishay Gupta','4.8.3.R1','U04','CleanApp','C04','SWI');
INSERT INTO `patch_info` VALUES (77,'4.8.3.R1-swi-clean-C05-U05','DML','Data Source entry for default Generci Un Structured Warehouse Schema','2011-08-30','2012-09-08 12:57:00','Raju Gottumukkala','4.8.3.R1','U05','CleanApp','C05','SWI');
INSERT INTO `patch_info` VALUES (78,'4.8.3.R1-swi-clean-C06-U06','DML','Data Source entry for IP Location Schema','2011-08-30','2012-09-08 12:57:00','Vishay Gupta','4.8.3.R1','U06','CleanApp','C06','SWI');
INSERT INTO `patch_info` VALUES (79,'4.8.3.R1-swi-clean-C07-U07','DML','Data Source entry for City Center Zip Code Schema','2011-08-30','2012-09-08 12:57:00','Vishay Gupta','4.8.3.R1','U07','CleanApp','C07','SWI');
INSERT INTO `patch_info` VALUES (80,'4.8.3.R1-swi-clean-C08-U08','DML','New Data Source Type entries in enum lookup','2011-08-30','2012-09-08 12:57:00','Vishay Gupta','4.8.3.R1','U08','CleanApp','C08','SWI');
INSERT INTO `patch_info` VALUES (81,'4.8.3.R1-swi-clean-C09-U09','DDL','Table for storing the shared and user model mappings','2011-08-30','2012-09-08 12:57:00','John Mallavalli','4.8.3.R1','U09','CleanApp','C09','SWI');
INSERT INTO `patch_info` VALUES (82,'4.8.3.R1-swi-clean-C10-U10','DML','Update Location Model Group context id to Location type bed id','2011-08-30','2012-09-08 12:57:00','Raju Gottumukkala','4.8.3.R1','U10','CleanApp','C10','SWI');
INSERT INTO `patch_info` VALUES (83,'4.8.3.R1-swi-clean-C11-U11','DML','Data Source entry for deafult content aggregator','2011-08-31','2012-09-08 12:57:00','Raju Gottumukkala','4.8.3.R1','U11','CleanApp','C11','SWI');
INSERT INTO `patch_info` VALUES (84,'4.8.3.R1-swi-clean-C12-U13','DML','Added cube showSnapshot url entry in requorce table','2011-09-02','2012-09-08 12:57:00','Jitendra Tiwari','4.8.3.R1','U13','CleanApp','C12','SWI');
INSERT INTO `patch_info` VALUES (85,'4.8.3.R1-swi-clean-C13-U14','DML','Script to delete the value concept ri onto term entry','2011-09-02','2012-09-08 12:57:00','Nitesh','4.8.3.R1','U14','CleanApp','C13','SWI');
INSERT INTO `patch_info` VALUES (86,'4.8.3.R1-swi-clean-C14-U16','DML','Script to update the realizable flag to false for unitscale and unitsymbol type','2011-09-05','2012-09-08 12:57:00','Nitesh','4.8.3.R1','U16','CleanApp','C14','SWI');
INSERT INTO `patch_info` VALUES (87,'4.8.3.R1-swi-clean-C15-U17','DML','Script to delete relativeTFOperator cloud trace from base swi','2011-09-06','2012-09-08 12:57:00','prasanna','4.8.3.R1','U17','CleanApp','C15','SWI');
INSERT INTO `patch_info` VALUES (88,'4.8.3.R1-swi-clean-C16-U18','DML','script to add operator as cloud component in yearTimeFrame cloud','2011-09-07','2012-09-08 12:57:00','prasanna','4.8.3.R1','U18','CleanApp','C16','SWI');
INSERT INTO `patch_info` VALUES (89,'4.8.3.R1-swi-clean-C17-U19','DML','Script to add operator as cloud component in MonthTimeFrame cloud','2011-09-07','2012-09-08 12:57:00','prasanna','4.8.3.R1','U19','CleanApp','C17','SWI');
INSERT INTO `patch_info` VALUES (90,'4.8.3.R1-swi-clean-C18-U20','DML','Script to add operator as cloud component in QuarterTimeFrame cloud','2011-09-07','2012-09-08 12:57:00','prasanna','4.8.3.R1','U20','CleanApp','C18','SWI');
INSERT INTO `patch_info` VALUES (91,'4.8.3.R1-swi-clean-C19-U21','DML','Script to add operator as cloud component in DayTimeFrame cloud','2011-09-07','2012-09-08 12:57:00','prasanna','4.8.3.R1','U21','CleanApp','C19','SWI');
INSERT INTO `patch_info` VALUES (92,'4.8.3.R1-swi-clean-C20-U22','DML','Script to correct the time preposition beds and instance','2011-09-08','2012-09-08 12:57:00','Nitesh','4.8.3.R1','U22','CleanApp','C20','SWI');
INSERT INTO `patch_info` VALUES (93,'4.8.3.R1-swi-clean-C21-U23','DML','Script to add operator as cloud component in Time cloud','2011-09-08','2012-09-08 12:57:00','Prasanna','4.8.3.R1','U23','CleanApp','C21','SWI');
INSERT INTO `patch_info` VALUES (94,'4.8.3.R1-swi-clean-C22-U24','DML','Script to delete duplicate entries in ri onto term','2011-09-12','2012-09-08 12:57:00','Prasanna','4.8.3.R1','U24','CleanApp','C22','SWI');
INSERT INTO `patch_info` VALUES (95,'4.8.3.R1-swi-clean-C23-U25','DML','Script to update instance name of kilo, pound in ri onto term table','2011-09-12','2012-09-08 12:57:00','Prasanna','4.8.3.R1','U25','CleanApp','C23','SWI');
INSERT INTO `patch_info` VALUES (96,'4.8.3.R1-swi-clean-C24-U26','DML','Script to update relation bedid in business entity table','2011-09-12','2012-09-08 12:57:00','Prasanna','4.8.3.R1','U26','CleanApp','C24','SWI');
INSERT INTO `patch_info` VALUES (97,'4.8.3.R1-swi-clean-C25-U28','DML','Script to create year time frame concept cloud','2011-09-15','2012-09-08 12:57:00','Prasanna','4.8.3.R1','U28','CleanApp','C25','SWI');
INSERT INTO `patch_info` VALUES (98,'4.8.3.R1-swi-clean-C26-U29','DML','Script to insert the regex validation rule for year time frame','2011-09-15','2012-09-08 12:57:00','Nitesh','4.8.3.R1','U29','CleanApp','C26','SWI');
INSERT INTO `patch_info` VALUES (99,'4.8.3.R1-swi-clean-C27-U30','DML','Script to remove the value-2 rule and cloud rule entry','2011-09-16','2012-09-08 12:57:00','Nitesh','4.8.3.R1','U30','CleanApp','C27','SWI');
INSERT INTO `patch_info` VALUES (100,'4.8.3.R1-swi-clean-C28-U31','DML','Script to insert the location cloud related entries','2011-09-16','2012-09-08 12:57:00','Prasanna','4.8.3.R1','U31','CleanApp','C28','SWI');
INSERT INTO `patch_info` VALUES (101,'4.8.3.R1-swi-clean-C29-U32','DML','Script to insert the regex validation rules for year time frame type and concept cloud','2011-09-16','2012-09-08 12:57:00','Nitesh','4.8.3.R1','U32','CleanApp','C29','SWI');
INSERT INTO `patch_info` VALUES (102,'4.8.3.R1-swi-clean-C30-U37','DML','Script to updatebase column of default_conversion_detail table for type volume','2011-09-22','2012-09-08 12:57:00','prasanna','4.8.3.R1','U37','CleanApp','C30','SWI');
INSERT INTO `patch_info` VALUES (103,'4.8.3.R1-swi-clean-C31-U38','DML','Script to set the COMP_TYPE_BE_ID column to nullable','2011-09-22','2012-09-08 12:57:00','Nitesh','4.8.3.R1','U38','CleanApp','C31','SWI');
INSERT INTO `patch_info` VALUES (104,'4.8.3.R1-swi-clean-C32-U39','DML','Script to update the frame work cloud behavior component category to only behavior','2011-09-22','2012-09-08 12:57:00','Nitesh','4.8.3.R1','U39','CleanApp','C32','SWI');
INSERT INTO `patch_info` VALUES (105,'4.8.3.R1-swi-clean-C33-U64','DML','Script to add instances for volume symbol','2011-09-27','2012-09-08 12:57:00','Prasanna','4.8.3.R1','U64','CleanApp','C33','SWI');
INSERT INTO `patch_info` VALUES (106,'4.8.3.R1-swi-clean-C34-U65','DML','Script to update the missing comp type name in the ri cloud table','2011-10-01','2012-09-08 12:57:00','Nitesh','4.8.3.R1','U65','CleanApp','C34','SWI');
INSERT INTO `patch_info` VALUES (107,'4.8.3.R1-swi-clean-C35-U66','DML','Script to insert the week time duration conversion entries','2011-10-03','2012-09-08 12:57:00','Nitesh','4.8.3.R1','U66','CleanApp','C35','SWI');
INSERT INTO `patch_info` VALUES (108,'4.8.3.R1-swi-clean-C36-U67','DML','Script to add week as cloud component in Time Duration cloud','2011-10-03','2012-09-08 12:57:00','Nitesh','4.8.3.R1','U67','CleanApp','C36','SWI');
INSERT INTO `patch_info` VALUES (109,'4.8.3.R1-swi-clean-C37-U71','DML','Script to add entity variation for minute, second, hour in ri_onto_term table','2011-10-12','2012-09-08 12:57:00','Prasanna','4.8.3.R1','U71','CleanApp','C37','SWI');
INSERT INTO `patch_info` VALUES (110,'4.8.3.R1-swi-clean-C38-U78','DML','Script to add entity variation for minute, second, hour in entity variation table','2011-10-13','2012-09-08 12:57:00','Prasanna','4.8.3.R1','U78','CleanApp','C38','SWI');
INSERT INTO `patch_info` VALUES (111,'4.8.3.R1-swi-clean-C39-U79','DML','Script to add missing digit ri onto term entry','2011-10-19','2012-09-08 12:57:00','Nitesh','4.8.3.R1','U79','CleanApp','C39','SWI');
INSERT INTO `patch_info` VALUES (112,'4.8.3.R1-swi-clean-C40-U86','DML','Script to insert the preposition as allowed component for quarter timeframe type cloud','2011-10-20','2012-09-08 12:57:00','Nitesh','4.8.3.R1','U86','CleanApp','C40','SWI');
INSERT INTO `patch_info` VALUES (113,'4.8.3.R1-swi-clean-C41-U87','DML','Script to insert the preposition as allowed component for month timeframe type cloud','2011-10-20','2012-09-08 12:57:00','Nitesh','4.8.3.R1','U87','CleanApp','C41','SWI');
INSERT INTO `patch_info` VALUES (114,'4.8.4.R1-swi-clean-C00-U00','DML','Blank patch to denote code release','2011-10-25','2012-09-08 12:57:00','Raju Gottumukkala','4.8.4.R1','U00','CleanApp','C00','SWI');
INSERT INTO `patch_info` VALUES (115,'4.8.4.R1-swi-clean-C01-U01','DML','added enum lookup entry for FeatureColumnMappingType','2011-10-25','2012-09-08 12:57:00','Jitendra Tiwari','4.8.4.R1','U01','CleanApp','C01','SWI');
INSERT INTO `patch_info` VALUES (116,'4.8.4.R1-swi-clean-C02-U02','DML','script to add new value realization(area)','2011-11-03','2012-09-08 12:57:00','Prasanna','4.8.4.R1','U02','CleanApp','C02','SWI');
INSERT INTO `patch_info` VALUES (117,'4.8.4.R1-swi-clean-C03-U03','DML','script to add new cloud for new value realization(area)','2011-11-03','2012-09-08 12:57:00','Prasanna','4.8.4.R1','U03','CleanApp','C03','SWI');
INSERT INTO `patch_info` VALUES (118,'4.8.4.R1-swi-clean-C04-U04','DML','script to add conversion formula for area','2011-11-03','2012-09-08 12:57:00','Prasanna','4.8.4.R1','U04','CleanApp','C04','SWI');
INSERT INTO `patch_info` VALUES (119,'4.8.4.R1-swi-clean-C05-U11','DDL','script to add primary key and indexes on base instance be id and model group id','2011-11-14','2012-09-08 12:57:01','Raju Gottumukkala','4.8.4.R1','U11','CleanApp','C05','SWI');
INSERT INTO `patch_info` VALUES (120,'4.8.4.R1-swi-clean-C06-U12','DML','script to add new value realization ie memory','2011-11-15','2012-09-08 12:57:01','Prasanna','4.8.4.R1','U12','CleanApp','C06','SWI');
INSERT INTO `patch_info` VALUES (121,'4.8.4.R1-swi-clean-C07-U13','DML','script to add new cloud for new value realization(memory)','2011-11-15','2012-09-08 12:57:01','Prasanna','4.8.4.R1','U13','CleanApp','C07','SWI');
INSERT INTO `patch_info` VALUES (122,'4.8.4.R1-swi-clean-C08-U14','DML','script to add conversion formula for memory','2011-11-15','2012-09-08 12:57:01','Prasanna','4.8.4.R1','U14','CleanApp','C08','SWI');
INSERT INTO `patch_info` VALUES (123,'4.8.4.R1-swi-clean-C09-U15','DML','script to add new value realization ie resolution','2011-11-09','2012-09-08 12:57:01','Prasanna','4.8.4.R1','U15','CleanApp','C09','SWI');
INSERT INTO `patch_info` VALUES (124,'4.8.4.R1-swi-clean-C10-U16','DML','script to add new cloud for new value realization(area)','2011-11-10','2012-09-08 12:57:01','Prasanna','4.8.4.R1','U16','CleanApp','C10','SWI');
INSERT INTO `patch_info` VALUES (125,'4.8.4.R1-swi-clean-C11-U17','DML','script to add conversion formula for resolution','2011-11-09','2012-09-08 12:57:01','Prasanna','4.8.4.R1','U17','CleanApp','C11','SWI');
INSERT INTO `patch_info` VALUES (126,'4.8.4.R1-swi-clean-C12-U18','DDL','script to create content cleanup pattern table','2011-11-15','2012-09-08 12:57:01','Deenu','4.8.4.R1','U18','CleanApp','C12','SWI');
INSERT INTO `patch_info` VALUES (127,'4.8.4.R1-swi-clean-C13-U19','DML','script to add new behavior ie. multivalued','2011-11-16','2012-09-08 12:57:01','Prasanna','4.8.4.R1','U19','CleanApp','C13','SWI');
INSERT INTO `patch_info` VALUES (128,'4.8.4.R1-swi-clean-C14-U20','DML','script to add the allowed components for the location cloud','2011-11-18','2012-09-08 12:57:01','Nitesh','4.8.4.R1','U20','CleanApp','C14','SWI');
INSERT INTO `patch_info` VALUES (129,'4.8.4.R1-swi-clean-C15-U21','DML','script to populate content cleanup pattern table','2011-11-22','2012-09-08 12:57:01','Deenu','4.8.4.R1','U21','CleanApp','C15','SWI');
INSERT INTO `patch_info` VALUES (130,'4.8.4.R1-swi-clean-C16-U22','DML','Script to add a column with name key on application table','2011-11-23','2012-09-08 12:57:01','Raju Gottumukkala','4.8.4.R1','U22','CleanApp','C16','SWI');
INSERT INTO `patch_info` VALUES (131,'4.8.4.R1-swi-clean-C17-U23','DML','script to add new behavior MULTIVALUED_GLOBAL_WEIGHT','2011-11-23','2012-09-08 12:57:01','Raju Gottumukkala','4.8.4.R1','U23','CleanApp','C17','SWI');
INSERT INTO `patch_info` VALUES (132,'4.8.4.R1-swi-clean-C18-U24','DML','script to update the digit ri onto term word as number for number value realization','2011-11-25','2012-09-08 12:57:01','Nitesh','4.8.4.R1','U24','CleanApp','C18','SWI');
INSERT INTO `patch_info` VALUES (133,'4.8.4.R1-swi-clean-C19-U25','DML','script to add default data source entry for solr instance','2011-11-29','2012-09-08 12:57:01','Raju Gottumukkala','4.8.4.R1','U25','CleanApp','C19','SWI');
INSERT INTO `patch_info` VALUES (134,'4.8.4.R1-swi-clean-C20-U26','DML','script to add new cloud for new number value realization','2011-12-01','2012-09-08 12:57:01','Nitesh','4.8.4.R1','U26','CleanApp','C20','SWI');
INSERT INTO `patch_info` VALUES (135,'4.8.4.R1-swi-clean-C21-U27','DDL','Script to add new table UNSTRUCTURED_APP_DETAIL','2011-12-08','2012-09-08 12:57:01','Raju Gottumukkala','4.8.4.R1','U27','CleanApp','C21','SWI');
INSERT INTO `patch_info` VALUES (136,'4.8.4.R1-swi-clean-C22-U28','DDL','Script to add entries  for enum  FacetNatureType','2011-12-08','2012-09-08 12:57:01','Jitendra Tiwari','4.8.4.R1','U28','CleanApp','C22','SWI');
INSERT INTO `patch_info` VALUES (137,'4.8.4.R1-swi-clean-C23-U29','DML','Script to add entries  for enum  AssetProviderType','2011-12-13','2012-09-08 12:57:01','Raju Gottumukkala','4.8.4.R1','U29','CleanApp','C23','SWI');
INSERT INTO `patch_info` VALUES (138,'4.8.4.R1-swi-clean-C24-U30','DML','script to update wrongly spelt from to cloud name','2011-12-20','2012-09-08 12:57:01','Prasanna','4.8.4.R1','U30','CleanApp','C24','SWI');
INSERT INTO `patch_info` VALUES (139,'4.8.4.R1-swi-clean-C25-U31','DML','script to add entries for Multivalued and Multivalued Global Weight in enum lookup table','2011-12-16','2012-09-08 12:57:01','Prasanna','4.8.4.R1','U31','CleanApp','C25','SWI');
INSERT INTO `patch_info` VALUES (140,'4.8.4.R1-swi-clean-C26-U32','DML','Script to update the currency regex instance expression to support hundred as unit scale in currency regex pattern','2011-01-04','2012-09-08 12:57:01','Nitesh','4.8.4.R1','U32','CleanApp','C26','SWI');
INSERT INTO `patch_info` VALUES (141,'4.8.4.R1-swi-clean-C27-U33','DDL','Added new column OWENER in datasource table','2011-02-01','2012-09-08 12:57:01','Jitendra','4.8.4.R1','U33','CleanApp','C27','SWI');
INSERT INTO `patch_info` VALUES (142,'4.8.4.R1-swi-clean-C28-U34','DML','Script to add new asset provider type','2011-12-20','2012-09-08 12:57:01','Nitesh Khandelwal','4.8.4.R1','U34','CleanApp','C28','SWI');
INSERT INTO `patch_info` VALUES (143,'4.8.4.R1-swi-clean-C29-U35','DDL','Added new column SALT in User table','2011-02-03','2012-09-08 12:57:01','Jitendra','4.8.4.R1','U35','CleanApp','C29','SWI');
INSERT INTO `patch_info` VALUES (144,'4.8.4.R1-swi-clean-C30-U36','DDL','Alter script to rename table name','2012-02-14','2012-09-08 12:57:01','Aditya','4.8.4.R1','U36','CleanApp','C30','SWI');
INSERT INTO `patch_info` VALUES (145,'4.8.4.R1-swi-clean-C31-U37','DDL','Alter script for indexes re-creation','2012-02-14','2012-09-08 12:57:03','Aditya','4.8.4.R1','U37','CleanApp','C31','SWI');
INSERT INTO `patch_info` VALUES (146,'4.8.4.R1-swi-clean-C32-U38','DML','Added new entry  showRelations action  in Resource table','2012-02-21','2012-09-08 12:57:03','Jitendra','4.8.4.R1','U38','CleanApp','C32','SWI');
INSERT INTO `patch_info` VALUES (147,'4.8.4.R1-swi-clean-C33-U39','DDL','To add a new column DATA_SAMPLING_STRATEGY on concept table','2012-03-07','2012-09-08 12:57:03','Vishay Gupta','4.8.4.R1','U39','CleanApp','C33','SWI');
INSERT INTO `patch_info` VALUES (148,'4.8.5.R1-swi-clean-C00-U00','DML','Blank patch to denote code release',NULL,'2012-09-08 12:57:03','Kaliki','4.8.5.R1','U00','CleanApp','C00','SWI');
INSERT INTO `patch_info` VALUES (149,'4.8.5.R1-swi-clean-C01-U01','DML','script to add the new report type and group in report_type, report_group_type table','2012-03-21','2012-09-08 12:57:03','Deenu','4.8.5.R1','U01','CleanApp','C01','SWI');
INSERT INTO `patch_info` VALUES (150,'4.8.5.R1-swi-clean-C02-U02','DML','script to alter the report_type table by adding description column','2012-03-21','2012-09-08 12:57:03','Deenu','4.8.5.R1','U02','CleanApp','C02','SWI');
INSERT INTO `patch_info` VALUES (151,'4.8.5.R1-swi-clean-C03-U03','DML','Script to add entry for showMaintenanceHome action','2012-04-11','2012-09-08 12:57:03','Jitendra Tiwari','4.8.5.R1','U03','CleanApp','C03','SWI');
INSERT INTO `patch_info` VALUES (152,'4.8.5.R1-swi-clean-C04-U04','DDL','Script to increase the size of the enum lookup table column for type, value and name','2012-04-13','2012-09-08 12:57:04','Raju Gottumukkala','4.8.5.R1','U04','CleanApp','C04','SWI');
INSERT INTO `patch_info` VALUES (153,'4.8.5.R1-swi-clean-C05-U05','DML','Script to refresh enum_lookup entries','2012-04-13','2012-09-08 12:57:04','Raju Gottumukkala','4.8.5.R1','U05','CleanApp','C05','SWI');
INSERT INTO `patch_info` VALUES (154,'4.8.6.R1-swi-clean-C00-U00','DML','Blank patch to denote code release',NULL,'2012-09-08 12:57:04','Shiva S','4.8.6.R1','U00','CleanApp','C00','SWI');
INSERT INTO `patch_info` VALUES (155,'4.8.6.R1-swi-clean-C01-U01','DDL','script to add new table asset operation detail',NULL,'2012-09-08 12:57:04','prasanna','4.8.6.R1','U01','CleanApp','C01','SWI');
INSERT INTO `patch_info` VALUES (156,'4.8.6.R1-swi-clean-C02-U02','DML','script to update the currency regex',NULL,'2012-09-08 12:57:04','prasanna','4.8.6.R1','U02','CleanApp','C02','SWI');
INSERT INTO `patch_info` VALUES (157,'4.8.6.R1-swi-clean-C03-U03','DDL','Script to add new column PARENT_ASSET_ID and made ASSET_ID nullable in  ASSET_OPERATION_DETAIL tab','2012-04-27','2012-09-08 12:57:04','Jitendra Tiwari','4.8.6.R1','U03','CleanApp','C03','SWI');
INSERT INTO `patch_info` VALUES (158,'4.8.6.R1-swi-clean-C04-U04','DML','script to update the currency regex','2012-05-03','2012-09-08 12:57:04','prasanna','4.8.6.R1','U04','CleanApp','C04','SWI');
INSERT INTO `patch_info` VALUES (159,'4.8.6.R1-swi-clean-C05-U05','DML','Script to add entry for manageMarts action in resources','2012-05-03','2012-09-08 12:57:04','Jitendra Tiwari','4.8.6.R1','U05','CleanApp','C05','SWI');
INSERT INTO `patch_info` VALUES (160,'4.8.6.R1-swi-clean-C06-U06','DML','Script to update value of Stat Type entry of STDDEV','2012-05-04','2012-09-08 12:57:04','Raju Gottumukkala','4.8.6.R1','U06','CleanApp','C06','SWI');
INSERT INTO `patch_info` VALUES (161,'4.8.6.R1-swi-clean-C07-U07','DML','Script to add entity variations for base concepts','2012-05-09','2012-09-08 12:57:04','Prasanna','4.8.6.R1','U07','CleanApp','C07','SWI');
INSERT INTO `patch_info` VALUES (162,'4.8.6.R1-swi-clean-C08-U08','DDL','Script to add creation_info column on asset_extended_detail table','2012-05-11','2012-09-08 12:57:04','Raju Gottumukkala','4.8.6.R1','U08','CleanApp','C08','SWI');
INSERT INTO `patch_info` VALUES (163,'4.8.6.R1-swi-clean-C09-U09','DDL','Script to add MEASURE_GROUP_BY and MEASURE_CONDITION_WITHOUT_STAT columns on user_query_possibility table','2012-06-08','2012-09-08 12:57:05','Raju Gottumukkala','4.8.6.R1','U09','CleanApp','C09','SWI');
INSERT INTO `patch_info` VALUES (164,'4.8.6.R1-swi-clean-C10-U10','DDL','Script to add QUERY_EXECUTION_ALLOWED column on ASSET table','2012-06-14','2012-09-08 12:57:05','Raju Gottumukkala','4.8.6.R1','U10','CleanApp','C10','SWI');
INSERT INTO `patch_info` VALUES (165,'4.8.7.R1-swi-clean-C00-U00','DML','Blank patch to denote code release','2012-06-19','2012-09-08 12:57:05','Raju Gottumukkala','4.8.7.R1','U00','CleanApp','C00','SWI');
INSERT INTO `patch_info` VALUES (166,'4.8.7.R1-swi-clean-C01-U01','DML','Added new entry  for security screens','2012-06-20','2012-09-08 12:57:05','Jitendra','4.8.7.R1','U01','CleanApp','C01','SWI');
INSERT INTO `patch_info` VALUES (167,'4.8.7.R1-swi-clean-C02-U02','DDL','script to create table hierarchy and hierarchy_detail','2012-06-22','2012-09-08 12:57:06','Deenu','4.8.7.R1','U02','CleanApp','C02','SWI');
INSERT INTO `patch_info` VALUES (168,'4.8.7.R1-swi-clean-C03-U03','DML','Added new acl class entries for Tabl, Colum and Membr objects','2012-06-25','2012-09-08 12:57:06','Raju Gottumukkala','4.8.7.R1','U03','CleanApp','C03','SWI');
INSERT INTO `patch_info` VALUES (169,'4.8.7.R1-swi-clean-C04-U04','DML','Added new entry  for hierarchy screens','2012-06-29','2012-09-08 12:57:06','Jitendra','4.8.7.R1','U04','CleanApp','C04','SWI');
INSERT INTO `patch_info` VALUES (170,'4.8.7.R1-swi-clean-C05-U05','DML','Added resource entry  for member security screens','2012-07-03','2012-09-08 12:57:06','Jitendra','4.8.7.R1','U05','CleanApp','C05','SWI');
INSERT INTO `patch_info` VALUES (171,'4.8.7.R1-swi-clean-C06-U06','DML','Update on description on few entries for Asset Provider Type','2012-07-12','2012-09-08 12:57:06','Raju Gottumukkala','4.8.7.R1','U06','CleanApp','C06','SWI');
INSERT INTO `patch_info` VALUES (172,'4.8.7.R1-swi-clean-C07-U07','DML','New hierarchical report group and type are added','2012-07-20','2012-09-08 12:57:06','Raju Gottumukkala','4.8.7.R1','U07','CleanApp','C07','SWI');
INSERT INTO `patch_info` VALUES (173,'4.8.7.R1-swi-clean-C08-U08','DML','Update report group action class for hierarchies group','2012-07-27','2012-09-08 12:57:06','Raju Gottumukkala','4.8.7.R1','U08','CleanApp','C08','SWI');
INSERT INTO `patch_info` VALUES (174,'4.8.7.R1-swi-clean-C09-U09','DDL','Column and Table name adjustments towards cross vendor compatibility','2012-08-01','2012-09-08 12:57:07','Raju Gottumukkala','4.8.7.R1','U09','CleanApp','C09','SWI');
INSERT INTO `patch_info` VALUES (175,'4.8.7.R1-swi-clean-C10-U10','DML','Update report group action class for DetailExcel group','2012-08-01','2012-09-08 12:57:07','Raju Gottumukkala','4.8.7.R1','U10','CleanApp','C10','SWI');
INSERT INTO `patch_info` VALUES (176,'4.8.7.R1-swi-clean-C11-U11','DDL','Change VALUE column on ENUM_LOOKUP table to be nullable','2012-08-01','2012-09-08 12:57:07','Raju Gottumukkala','4.8.7.R1','U11','CleanApp','C11','SWI');
INSERT INTO `patch_info` VALUES (177,'4.8.7.R1-swi-clean-C12-U12','DDL','Alter Application Example table query name column to be Varchar than text column','2012-08-09','2012-09-08 12:57:07','Raju Gottumukkala','4.8.7.R1','U12','CleanApp','C12','SWI');
INSERT INTO `patch_info` VALUES (178,'4.8.7.R1-swi-clean-C13-U13','DDL','Rename column of Configuration table by prefixing with CONFIG word','2012-08-10','2012-09-08 12:57:08','Raju Gottumukkala','4.8.7.R1','U13','CleanApp','C13','SWI');
INSERT INTO `patch_info` VALUES (179,'4.8.7.R1-swi-clean-C14-U14','DML','Update Lookup Type None value from blank to NONE in tabl','2012-08-10','2012-09-08 12:57:08','Raju Gottumukkala','4.8.7.R1','U14','CleanApp','C14','SWI');
INSERT INTO `patch_info` VALUES (180,'4.8.7.R1-swi-clean-C15-U15','DML','Update Coversion Type null value from blank to NULL in respective tables','2012-08-10','2012-09-08 12:57:08','Raju Gottumukkala','4.8.7.R1','U15','CleanApp','C15','SWI');
INSERT INTO `patch_info` VALUES (181,'4.8.7.R1-swi-clean-C16-U16','DML','Correct the enum lookup entries corresponding to the BLANK value records','2012-08-21','2012-09-08 12:57:08','Raju Gottumukkala','4.8.7.R1','U16','CleanApp','C16','SWI');
INSERT INTO `patch_info` VALUES (182,'4.8.7.R1-swi-clean-C17-U17','DML','Added sas specific date formats','2012-08-22','2012-09-08 12:57:08','Raju Gottumukkala','4.8.7.R1','U17','CleanApp','C17','SWI');
INSERT INTO `patch_info` VALUES (183,'4.8.8.R1-swi-clean-C00-U00','DML','Blank patch to denote code release','2012-08-23','2012-09-10 15:57:27','Kaliki Aritakula','4.8.8.R1','U00','CleanApp','C00','SWI');
INSERT INTO `patch_info` VALUES (184,'4.8.8.R1-swi-clean-C01-U01','DML','New Date Format to support INT based Day','2012-08-31','2012-09-10 15:57:27','Raju Gottumukkala','4.8.8.R1','U01','CleanApp','C01','SWI');
INSERT INTO `patch_info` VALUES (185,'4.9.0.R1-swi-clean-C00-U00','DML','Blank patch to denote code release','2012-09-10','2012-10-22 11:25:03','Raju Gottumukkala','4.9.0.R1','U00','CleanApp','C00','SWI');
INSERT INTO `patch_info` VALUES (186,'4.9.0.R1-swi-clean-C01-U01','DDL','Column name changes on tabl, colum and cloud for cross vendor support','2012-09-11','2012-10-22 11:25:03','Raju Gottumukkala','4.9.0.R1','U01','CleanApp','C01','SWI');
INSERT INTO `patch_info` VALUES (187,'4.9.0.R1-swi-clean-C02-U02','DML','Sequence entry for user query possibility table','2012-09-12','2012-10-22 11:25:03','Raju Gottumukkala','4.9.0.R1','U02','CleanApp','C02','SWI');
INSERT INTO `patch_info` VALUES (188,'4.9.0.R1-swi-clean-C03-U03','DML','Correct data source display names for Catalog and Uploaded','2012-09-13','2012-10-22 11:25:03','Raju Gottumukkala','4.9.0.R1','U03','CleanApp','C03','SWI');
INSERT INTO `patch_info` VALUES (189,'4.9.0.R1-swi-clean-C04-U04','DML','Take off entries not needed from sequences table, drop indx table','2012-09-13','2012-10-22 11:25:03','Raju Gottumukkala','4.9.0.R1','U04','CleanApp','C04','SWI');
INSERT INTO `patch_info` VALUES (190,'4.9.0.R1-swi-clean-C05-U05','DML','Moved from 4.8.7.1b branch patch: Added configuration entries for the needed keys to support Config Screens','2012-09-13','2012-10-22 11:25:03','Kaliki Aritakula','4.9.0.R1','U05','CleanApp','C05','SWI');
INSERT INTO `patch_info` VALUES (191,'4.9.0.R1-swi-clean-C06-U06','DML','Sequence entry for COUNTRY LOOKUP','2012-09-14','2012-10-22 11:25:03','Raju Gottumukkala','4.9.0.R1','U06','CleanApp','C06','SWI');
INSERT INTO `patch_info` VALUES (192,'4.9.0.R1-swi-clean-C07-U07','DML','Enum_Lookup entry for Asset Providers Postgres, Derby','2012-09-26','2012-10-22 11:25:03','Deenu','4.9.0.R1','U07','CleanApp','C07','SWI');
INSERT INTO `patch_info` VALUES (193,'4.9.0.R1-swi-clean-C08-U08','DDL','Correcting column data types according to the enum value types','2012-09-28','2012-10-22 11:25:03','Raju Gottumukkala','4.9.0.R1','U08','CleanApp','C08','SWI');
INSERT INTO `patch_info` VALUES (194,'4.9.0.R1-swi-clean-C08-U08','DML','Lookup value columns to be in lower case in reverse index columns','2012-09-27','2012-10-22 11:25:03','Raju Gottumukkala','4.9.0.R1','U08','CleanApp','C08','SWI');
INSERT INTO `patch_info` VALUES (195,'4.9.0.R1-swi-clean-C09-U09','DDL','Correcting column data types according to the enum value types','2012-09-28','2012-10-22 11:25:03','Raju Gottumukkala','4.9.0.R1','U09','CleanApp','C09','SWI');
INSERT INTO `patch_info` VALUES (196,'4.9.0.R1-swi-clean-C10-U10','DDL','Default Value on Profile tables needs to be 1','2012-10-01','2012-10-22 11:25:03','Raju Gottumukkala','4.9.0.R1','U10','CleanApp','C10','SWI');
INSERT INTO `patch_info` VALUES (197,'4.9.0.R1-swi-clean-C11-U11','DML','Enum lookup descriptions changed on publish asset mode','2012-10-03','2012-10-22 11:25:03','Raju Gottumukkala','4.9.0.R1','U11','CleanApp','C11','SWI');
INSERT INTO `patch_info` VALUES (198,'4.9.0.R1-swi-clean-C12-U12','DML','ID columns of the tables should not be not null and with out any default','2012-10-03','2012-10-22 11:25:03','Raju Gottumukkala','4.9.0.R1','U12','CleanApp','C12','SWI');
INSERT INTO `patch_info` VALUES (199,'4.9.0.R1-swi-clean-C13-U13','DML','Report Type entries for simple 2d scatter chart','2012-10-03','2012-10-22 11:25:03','Raju Gottumukkala','4.9.0.R1','U13','CleanApp','C13','SWI');
INSERT INTO `patch_info` VALUES (200,'4.9.0.R1-swi-clean-C14-U14','DML','ACL Class entry for Asset','2012-10-03','2012-10-22 11:25:03','Raju Gottumukkala','4.9.0.R1','U14','CleanApp','C14','SWI');
INSERT INTO `patch_info` VALUES (201,'4.9.0.R1-swi-clean-C15-U15','DDL','Rename table RULE to RULES','2012-10-11','2012-10-22 11:25:03','Raju Gottumukkala','4.9.0.R1','U15','CleanApp','C15','SWI');
INSERT INTO `patch_info` VALUES (202,'4.9.0.R1-swi-clean-C16-U16','DML','New behavior (Dependent Variable) added','2012-10-18','2012-10-22 11:25:03','Raju Gottumukkala','4.9.0.R1','U16','CleanApp','C16','SWI');
INSERT INTO `patch_info` VALUES (203,'4.9.0.R1-swi-clean-C17-U17','DML','added dateformat entries for postgresql support in upload proces','2012-10-19','2012-10-22 11:25:03','Raju Gottumukkala','4.9.0.R1','U17','CleanApp','C17','SWI');
INSERT INTO `patch_info` VALUES (204,'4.9.1.R1-swi-clean-C00-U00','DML','Blank patch to denote code release','2012-10-22','2012-11-03 15:21:40','Raju Gottumukkala','4.9.1.R1','U00','CleanApp','C00','SWI');
INSERT INTO `patch_info` VALUES (205,'4.9.1.R1-swi-clean-C01-U01','DML','Script to add PASSWORD_ENCRYPTED column to data source table','2012-10-29','2012-11-03 15:21:40','Nitesh Khandelwal','4.9.1.R1','U01','CleanApp','C01','SWI');
INSERT INTO `patch_info` VALUES (206,'4.9.2.R1-swi-clean-C00-U00','DML','Blank patch to denote code release','2012-11-05','2012-11-11 20:54:22','Raju Gottumukkala','4.9.2.R1','U00','CleanApp','C00','SWI');
INSERT INTO `patch_info` VALUES (207,'4.9.2.R1-swi-clean-C01-U01','DDL','changed datatype from char to varchar for cross vendor support','2012-11-05','2012-11-11 20:54:22','Raju Gottumukkala','4.9.2.R1','U01','CleanApp','C01','SWI');
/*!40000 ALTER TABLE `patch_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `path_definition`
--

DROP TABLE IF EXISTS `path_definition`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `path_definition` (
  `ID` bigint(20) NOT NULL,
  `SOURCE_BE_ID` bigint(20) NOT NULL,
  `DESTINATION_BE_ID` bigint(20) NOT NULL,
  `PATH_LENGTH` int(5) NOT NULL DEFAULT '1',
  `PRIORITY` int(2) DEFAULT NULL,
  `PATH_TYPE` varchar(10) DEFAULT NULL,
  `HIERARCHY_TYPE` int(1) DEFAULT '1',
  `CENTRAL_CONCEPT` char(1) DEFAULT 'N',
  `cloud_id` bigint(20) NOT NULL,
  `association_type` varchar(2) DEFAULT 'R',
  `PATH_SELECTION_TYPE` int(2) DEFAULT '1',
  PRIMARY KEY (`ID`),
  KEY `FK_PT_SBID` (`SOURCE_BE_ID`),
  KEY `FK_PT_DBID` (`DESTINATION_BE_ID`),
  KEY `Idx_PD_PATH_LENGTH` (`PATH_LENGTH`),
  KEY `Idx_PD_PRIORITY` (`PRIORITY`),
  KEY `Idx_PD_PATH_TYPE` (`PATH_TYPE`),
  KEY `Idx_PD_PARENT_CHILD` (`HIERARCHY_TYPE`),
  KEY `Idx_PD_CENTRAL_CONCEPT` (`CENTRAL_CONCEPT`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `path_definition`
--

LOCK TABLES `path_definition` WRITE;
/*!40000 ALTER TABLE `path_definition` DISABLE KEYS */;
INSERT INTO `path_definition` VALUES (1,151,401,1,1,'TRT',3,'N',201,'R',1);
INSERT INTO `path_definition` VALUES (2,151,107,1,1,'TRT',3,'N',201,'R',1);
INSERT INTO `path_definition` VALUES (3,151,106,1,1,'TRT',3,'N',201,'R',1);
INSERT INTO `path_definition` VALUES (4,152,151,1,1,'TRT',3,'N',201,'R',1);
INSERT INTO `path_definition` VALUES (5,152,105,1,1,'TRT',3,'N',201,'R',1);
INSERT INTO `path_definition` VALUES (6,203,401,1,1,'TRT',3,'N',201,'R',1);
INSERT INTO `path_definition` VALUES (7,202,401,1,1,'TRT',3,'N',201,'R',1);
INSERT INTO `path_definition` VALUES (8,152,409,1,1,'TRT',3,'N',201,'R',1);
INSERT INTO `path_definition` VALUES (9,152,408,1,1,'TRT',3,'N',201,'R',1);
INSERT INTO `path_definition` VALUES (10,201,409,1,1,'TRT',3,'N',201,'R',1);
INSERT INTO `path_definition` VALUES (11,201,408,1,1,'TRT',3,'N',201,'R',1);
INSERT INTO `path_definition` VALUES (12,152,404,1,1,'TRT',3,'N',201,'R',1);
INSERT INTO `path_definition` VALUES (13,152,405,1,1,'TRT',3,'N',201,'R',1);
INSERT INTO `path_definition` VALUES (14,201,404,1,1,'TRT',3,'N',201,'R',1);
INSERT INTO `path_definition` VALUES (15,201,405,1,1,'TRT',3,'N',201,'R',1);
INSERT INTO `path_definition` VALUES (16,201,204,1,1,'TRT',3,'N',201,'R',1);
INSERT INTO `path_definition` VALUES (17,201,202,1,1,'TRT',3,'N',201,'R',1);
INSERT INTO `path_definition` VALUES (18,201,203,1,1,'TRT',3,'N',201,'R',1);
INSERT INTO `path_definition` VALUES (19,201,403,1,1,'TRT',3,'N',201,'R',1);
INSERT INTO `path_definition` VALUES (20,201,401,1,1,'TRT',3,'N',201,'R',1);
INSERT INTO `path_definition` VALUES (21,110,401,1,1,'TRT',3,'N',201,'R',1);
INSERT INTO `path_definition` VALUES (22,110,104,1,1,'TRT',3,'N',201,'R',1);
INSERT INTO `path_definition` VALUES (23,301,302,1,1,'TRT',3,'N',205,'R',1);
INSERT INTO `path_definition` VALUES (24,301,303,1,1,'TRT',3,'N',205,'R',1);
INSERT INTO `path_definition` VALUES (25,301,304,1,1,'TRT',3,'N',205,'R',1);
INSERT INTO `path_definition` VALUES (26,302,303,1,1,'TRT',3,'N',205,'R',1);
INSERT INTO `path_definition` VALUES (27,302,304,1,1,'TRT',3,'N',205,'R',1);
INSERT INTO `path_definition` VALUES (28,303,304,1,1,'TRT',3,'N',205,'R',1);
INSERT INTO `path_definition` VALUES (29,303,302,1,1,'TRT',3,'N',205,'R',1);
INSERT INTO `path_definition` VALUES (30,304,302,1,1,'TRT',3,'N',205,'R',1);
INSERT INTO `path_definition` VALUES (31,304,303,1,1,'TRT',3,'N',205,'R',1);
/*!40000 ALTER TABLE `path_definition` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `path_definition_etd`
--

DROP TABLE IF EXISTS `path_definition_etd`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `path_definition_etd` (
  `ID` bigint(20) NOT NULL,
  `PATH_DEFINITION_ID` bigint(20) NOT NULL,
  `ENTITY_TRIPLE_DEF_ID` bigint(20) NOT NULL,
  `ENTITY_TRIPLE_ORDER` int(5) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_PETD_PID` (`PATH_DEFINITION_ID`),
  KEY `FK_PETD_ETDID` (`ENTITY_TRIPLE_DEF_ID`),
  KEY `Idx_PDE_ORDER` (`ENTITY_TRIPLE_ORDER`)
) ENGINE=MyISAM AUTO_INCREMENT=23 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `path_definition_etd`
--

LOCK TABLES `path_definition_etd` WRITE;
/*!40000 ALTER TABLE `path_definition_etd` DISABLE KEYS */;
INSERT INTO `path_definition_etd` VALUES (1,1,1,1);
INSERT INTO `path_definition_etd` VALUES (2,2,2,1);
INSERT INTO `path_definition_etd` VALUES (3,3,3,1);
INSERT INTO `path_definition_etd` VALUES (4,4,4,1);
INSERT INTO `path_definition_etd` VALUES (5,5,5,1);
INSERT INTO `path_definition_etd` VALUES (6,6,6,1);
INSERT INTO `path_definition_etd` VALUES (7,7,7,1);
INSERT INTO `path_definition_etd` VALUES (8,8,8,1);
INSERT INTO `path_definition_etd` VALUES (9,9,9,1);
INSERT INTO `path_definition_etd` VALUES (10,10,10,1);
INSERT INTO `path_definition_etd` VALUES (11,11,11,1);
INSERT INTO `path_definition_etd` VALUES (12,12,12,1);
INSERT INTO `path_definition_etd` VALUES (13,13,13,1);
INSERT INTO `path_definition_etd` VALUES (14,14,14,1);
INSERT INTO `path_definition_etd` VALUES (15,15,15,1);
INSERT INTO `path_definition_etd` VALUES (16,16,16,1);
INSERT INTO `path_definition_etd` VALUES (17,17,17,1);
INSERT INTO `path_definition_etd` VALUES (18,18,18,1);
INSERT INTO `path_definition_etd` VALUES (19,19,19,1);
INSERT INTO `path_definition_etd` VALUES (20,20,20,1);
INSERT INTO `path_definition_etd` VALUES (21,21,21,1);
INSERT INTO `path_definition_etd` VALUES (22,22,22,1);
INSERT INTO `path_definition_etd` VALUES (23,23,23,1);
INSERT INTO `path_definition_etd` VALUES (24,24,24,1);
INSERT INTO `path_definition_etd` VALUES (25,25,25,1);
INSERT INTO `path_definition_etd` VALUES (26,26,26,1);
INSERT INTO `path_definition_etd` VALUES (27,27,27,1);
INSERT INTO `path_definition_etd` VALUES (28,28,28,1);
INSERT INTO `path_definition_etd` VALUES (29,29,29,1);
INSERT INTO `path_definition_etd` VALUES (30,30,30,1);
INSERT INTO `path_definition_etd` VALUES (31,31,31,1);
/*!40000 ALTER TABLE `path_definition_etd` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `path_definition_rule`
--

DROP TABLE IF EXISTS `path_definition_rule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `path_definition_rule` (
  `PATH_DEF_ID` bigint(20) NOT NULL,
  `RULE_ID` bigint(20) NOT NULL,
  KEY `FK_path_definition_rule_id` (`RULE_ID`),
  KEY `FK_path_definition_rule` (`PATH_DEF_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `path_definition_rule`
--

LOCK TABLES `path_definition_rule` WRITE;
/*!40000 ALTER TABLE `path_definition_rule` DISABLE KEYS */;
/*!40000 ALTER TABLE `path_definition_rule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `popularity_hit`
--

DROP TABLE IF EXISTS `popularity_hit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `popularity_hit` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `TERM_ID` bigint(20) NOT NULL,
  `TERM_TYPE` int(1) NOT NULL,
  `HITS` bigint(20) NOT NULL,
  `DATE_CREATED` datetime DEFAULT NULL,
  `PROCESSING_STATE` char(1) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `popularity_hit`
--

LOCK TABLES `popularity_hit` WRITE;
/*!40000 ALTER TABLE `popularity_hit` DISABLE KEYS */;
INSERT INTO `popularity_hit` VALUES (1,0,0,0,NULL,NULL);
INSERT INTO `popularity_hit` VALUES (2,0,0,0,NULL,NULL);
/*!40000 ALTER TABLE `popularity_hit` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `possible_attribute_rule`
--

DROP TABLE IF EXISTS `possible_attribute_rule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `possible_attribute_rule` (
  `ID` bigint(20) NOT NULL,
  `POSS_ATTR_ID` bigint(20) NOT NULL,
  `RULE_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_poss_attr_id` (`POSS_ATTR_ID`),
  KEY `FK_poss_attr_rule_id` (`RULE_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `possible_attribute_rule`
--

LOCK TABLES `possible_attribute_rule` WRITE;
/*!40000 ALTER TABLE `possible_attribute_rule` DISABLE KEYS */;
INSERT INTO `possible_attribute_rule` VALUES (101,102,10003);
INSERT INTO `possible_attribute_rule` VALUES (102,102,10004);
INSERT INTO `possible_attribute_rule` VALUES (103,103,10001);
INSERT INTO `possible_attribute_rule` VALUES (104,103,10002);
/*!40000 ALTER TABLE `possible_attribute_rule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `possible_attributes`
--

DROP TABLE IF EXISTS `possible_attributes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `possible_attributes` (
  `ID` bigint(20) NOT NULL,
  `TYPE_BE_ID` bigint(20) NOT NULL,
  `COMPONENT_TYPE_BE_ID` bigint(20) NOT NULL,
  `RELATION_BE_ID` bigint(20) DEFAULT NULL,
  `OPTIONAL` char(1) NOT NULL DEFAULT 'Y',
  `INHERENT` char(1) NOT NULL DEFAULT 'N',
  `MULTIPLE_REALIZATIONS` char(1) DEFAULT 'N',
  `DEFAULT_REALIZATION_BE_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_behavior_component_type` (`TYPE_BE_ID`),
  KEY `FK_behavior_comp_comp_type` (`COMPONENT_TYPE_BE_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `possible_attributes`
--

LOCK TABLES `possible_attributes` WRITE;
/*!40000 ALTER TABLE `possible_attributes` DISABLE KEYS */;
INSERT INTO `possible_attributes` VALUES (101,101,103,501,'Y','N','N',NULL);
INSERT INTO `possible_attributes` VALUES (102,101,153,502,'N','Y','N',157);
INSERT INTO `possible_attributes` VALUES (103,101,201,503,'Y','N','Y',NULL);
INSERT INTO `possible_attributes` VALUES (104,101,301,504,'Y','N','Y',NULL);
INSERT INTO `possible_attributes` VALUES (105,111,201,503,'Y','N','Y',NULL);
INSERT INTO `possible_attributes` VALUES (106,111,301,504,'Y','N','Y',NULL);
/*!40000 ALTER TABLE `possible_attributes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `possible_behavior`
--

DROP TABLE IF EXISTS `possible_behavior`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `possible_behavior` (
  `ID` bigint(20) NOT NULL,
  `TYPE_BE_ID` bigint(20) DEFAULT NULL,
  `BEHAVIOR_BE_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM AUTO_INCREMENT=149 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `possible_behavior`
--

LOCK TABLES `possible_behavior` WRITE;
/*!40000 ALTER TABLE `possible_behavior` DISABLE KEYS */;
INSERT INTO `possible_behavior` VALUES (101,103,9002);
INSERT INTO `possible_behavior` VALUES (102,104,9002);
INSERT INTO `possible_behavior` VALUES (103,105,9002);
INSERT INTO `possible_behavior` VALUES (104,153,9002);
INSERT INTO `possible_behavior` VALUES (105,201,9002);
INSERT INTO `possible_behavior` VALUES (106,202,9002);
INSERT INTO `possible_behavior` VALUES (107,203,9002);
INSERT INTO `possible_behavior` VALUES (108,204,9002);
INSERT INTO `possible_behavior` VALUES (109,205,9002);
INSERT INTO `possible_behavior` VALUES (110,206,9002);
INSERT INTO `possible_behavior` VALUES (111,301,9002);
INSERT INTO `possible_behavior` VALUES (112,302,9002);
INSERT INTO `possible_behavior` VALUES (113,303,9002);
INSERT INTO `possible_behavior` VALUES (114,304,9002);
INSERT INTO `possible_behavior` VALUES (115,305,9002);
INSERT INTO `possible_behavior` VALUES (116,307,9002);
INSERT INTO `possible_behavior` VALUES (117,309,9002);
INSERT INTO `possible_behavior` VALUES (118,401,9002);
INSERT INTO `possible_behavior` VALUES (119,210,9002);
INSERT INTO `possible_behavior` VALUES (120,105,9003);
INSERT INTO `possible_behavior` VALUES (121,153,9003);
INSERT INTO `possible_behavior` VALUES (122,201,9003);
INSERT INTO `possible_behavior` VALUES (123,202,9003);
INSERT INTO `possible_behavior` VALUES (124,203,9003);
INSERT INTO `possible_behavior` VALUES (125,204,9003);
INSERT INTO `possible_behavior` VALUES (126,205,9003);
INSERT INTO `possible_behavior` VALUES (127,206,9003);
INSERT INTO `possible_behavior` VALUES (128,401,9003);
INSERT INTO `possible_behavior` VALUES (129,210,9003);
INSERT INTO `possible_behavior` VALUES (130,103,9004);
INSERT INTO `possible_behavior` VALUES (131,104,9004);
INSERT INTO `possible_behavior` VALUES (132,105,9004);
INSERT INTO `possible_behavior` VALUES (133,153,9004);
INSERT INTO `possible_behavior` VALUES (134,201,9004);
INSERT INTO `possible_behavior` VALUES (135,202,9004);
INSERT INTO `possible_behavior` VALUES (136,203,9004);
INSERT INTO `possible_behavior` VALUES (137,204,9004);
INSERT INTO `possible_behavior` VALUES (138,205,9004);
INSERT INTO `possible_behavior` VALUES (139,206,9004);
INSERT INTO `possible_behavior` VALUES (140,301,9004);
INSERT INTO `possible_behavior` VALUES (141,302,9004);
INSERT INTO `possible_behavior` VALUES (142,303,9004);
INSERT INTO `possible_behavior` VALUES (143,304,9004);
INSERT INTO `possible_behavior` VALUES (144,305,9004);
INSERT INTO `possible_behavior` VALUES (145,307,9004);
INSERT INTO `possible_behavior` VALUES (146,309,9004);
INSERT INTO `possible_behavior` VALUES (147,401,9004);
INSERT INTO `possible_behavior` VALUES (148,210,9004);
/*!40000 ALTER TABLE `possible_behavior` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `possible_detail_type`
--

DROP TABLE IF EXISTS `possible_detail_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `possible_detail_type` (
  `ID` bigint(20) NOT NULL,
  `TYPE_BE_ID` bigint(20) NOT NULL,
  `DETAIL_TYPE_BE_ID` bigint(20) NOT NULL,
  `DFAULT` char(1) DEFAULT 'N',
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `possible_detail_type`
--

LOCK TABLES `possible_detail_type` WRITE;
/*!40000 ALTER TABLE `possible_detail_type` DISABLE KEYS */;
INSERT INTO `possible_detail_type` VALUES (1,201,202,'Y');
INSERT INTO `possible_detail_type` VALUES (2,201,203,'N');
INSERT INTO `possible_detail_type` VALUES (3,201,204,'N');
INSERT INTO `possible_detail_type` VALUES (4,201,205,'N');
INSERT INTO `possible_detail_type` VALUES (5,201,206,'N');
INSERT INTO `possible_detail_type` VALUES (6,201,207,'N');
INSERT INTO `possible_detail_type` VALUES (7,201,208,'N');
INSERT INTO `possible_detail_type` VALUES (8,201,209,'N');
INSERT INTO `possible_detail_type` VALUES (9,301,302,'Y');
INSERT INTO `possible_detail_type` VALUES (10,301,303,'N');
INSERT INTO `possible_detail_type` VALUES (11,301,304,'N');
INSERT INTO `possible_detail_type` VALUES (12,301,305,'N');
INSERT INTO `possible_detail_type` VALUES (13,301,306,'N');
INSERT INTO `possible_detail_type` VALUES (14,301,307,'N');
INSERT INTO `possible_detail_type` VALUES (15,301,308,'N');
INSERT INTO `possible_detail_type` VALUES (16,301,309,'N');
/*!40000 ALTER TABLE `possible_detail_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `published_file_info`
--

DROP TABLE IF EXISTS `published_file_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `published_file_info` (
  `ID` bigint(20) NOT NULL,
  `FILE_NAME` varchar(255) NOT NULL,
  `FILE_DESCRIPTION` varchar(255) DEFAULT NULL,
  `ORIGINAL_FILE_NAME` varchar(255) DEFAULT NULL,
  `FILE_LOCATION` varchar(255) DEFAULT NULL,
  `USER_ID` bigint(20) NOT NULL,
  `APPLICATION_ID` bigint(20) DEFAULT NULL,
  `MODEL_ID` bigint(20) DEFAULT NULL,
  `DATASOURCE_ID` bigint(20) DEFAULT NULL,
  `LAST_ABSORPTION_DATE` datetime DEFAULT NULL,
  `FIRST_ABSORPTION_DATE` datetime DEFAULT NULL,
  `OPERATION_SUCCESSFUL` char(1) NOT NULL DEFAULT 'N',
  `OPERATION_TYPE` char(1) NOT NULL DEFAULT 'A',
  `CURRENT_OPERATION` char(1) DEFAULT NULL,
  `CURRENT_OPERATION_STATUS` int(2) DEFAULT NULL,
  `SOURCE_TYPE` varchar(15) NOT NULL DEFAULT 'CSV',
  `FILE_LINK` char(1) NOT NULL DEFAULT 'N',
  `FILE_ABSORBED` char(1) NOT NULL DEFAULT 'N',
  `DATASET_COLLECTION_CREATION` char(1) DEFAULT 'N',
  `ABSORBTION_JOB_REQUEST_ID` bigint(20) DEFAULT NULL,
  `EVALUATION_JOB_REQUEST_ID` bigint(20) DEFAULT NULL,
  `PUBLISHER_PROCESS_TYPE` varchar(5) NOT NULL DEFAULT 'DPP',
  PRIMARY KEY (`ID`),
  KEY `IDX_PFI_AID` (`APPLICATION_ID`),
  KEY `IDX_PFI_MID` (`MODEL_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `published_file_info`
--

LOCK TABLES `published_file_info` WRITE;
/*!40000 ALTER TABLE `published_file_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `published_file_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `published_file_info_details`
--

DROP TABLE IF EXISTS `published_file_info_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `published_file_info_details` (
  `ID` bigint(20) NOT NULL,
  `FILE_ID` bigint(20) NOT NULL,
  `PROPERTY_NAME` varchar(255) NOT NULL,
  `PROPERTY_VALUE` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_PUB_FILE_ID` (`FILE_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `published_file_info_details`
--

LOCK TABLES `published_file_info_details` WRITE;
/*!40000 ALTER TABLE `published_file_info_details` DISABLE KEYS */;
/*!40000 ALTER TABLE `published_file_info_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `published_file_table_details`
--

DROP TABLE IF EXISTS `published_file_table_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `published_file_table_details` (
  `ID` bigint(20) NOT NULL,
  `FILE_TABLE_INFO_ID` bigint(20) NOT NULL,
  `BASE_COLUMN_NAME` varchar(255) DEFAULT NULL,
  `EVALUATED_COLUMN_NAME` varchar(255) DEFAULT NULL,
  `EVALUATED_COLUMN_DISPLAY_NAME` varchar(255) DEFAULT NULL,
  `BASE_DATA_TYPE` varchar(255) DEFAULT NULL,
  `EVALUATED_DATA_TYPE` varchar(255) DEFAULT NULL,
  `BASE_PRECISION` int(10) DEFAULT NULL,
  `BASE_SCALE` int(10) NOT NULL,
  `EVALUATED_PRECISION` int(10) NOT NULL,
  `EVALUATED_SCALE` int(10) NOT NULL,
  `KDXTYPE` varchar(55) DEFAULT NULL,
  `COLUM_INDEX` int(10) DEFAULT NULL,
  `POPULATION` char(1) NOT NULL DEFAULT 'N',
  `DISTRIBUTION` char(1) NOT NULL DEFAULT 'N',
  `TIME_BASED` char(1) NOT NULL DEFAULT 'N',
  `LOCATION_BASED` char(1) NOT NULL DEFAULT 'N',
  `FORMAT` varchar(35) DEFAULT NULL,
  `UNIT` varchar(35) DEFAULT NULL,
  `UNIT_TYPE` varchar(35) DEFAULT NULL,
  `GRANULARITY` varchar(25) DEFAULT NULL,
  `default_metric` char(1) DEFAULT 'N',
  `ORIGINAL_EVALUATED_DATA_TYPE` varchar(255) DEFAULT NULL,
  `ORIGINAL_EVALUATED_PRECISION` int(10) DEFAULT NULL,
  `ORIGINAL_EVALUATED_SCALE` int(10) DEFAULT NULL,
  `ORIGINAL_UNIT_TYPE` varchar(35) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_PUB_TAB_FILE_INFO_ID` (`FILE_TABLE_INFO_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `published_file_table_details`
--

LOCK TABLES `published_file_table_details` WRITE;
/*!40000 ALTER TABLE `published_file_table_details` DISABLE KEYS */;
/*!40000 ALTER TABLE `published_file_table_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `published_file_table_info`
--

DROP TABLE IF EXISTS `published_file_table_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `published_file_table_info` (
  `ID` bigint(20) NOT NULL,
  `FILE_ID` bigint(20) NOT NULL,
  `WORK_SHEET_NAME` varchar(255) DEFAULT NULL,
  `TEMP_TABLE_NAME` varchar(255) DEFAULT NULL,
  `EVALUATED_TABLE_NAME` varchar(255) DEFAULT NULL,
  `DISPLAY_TABLE_NAME` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_PUB_TAB_FILE_ID` (`FILE_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `published_file_table_info`
--

LOCK TABLES `published_file_table_info` WRITE;
/*!40000 ALTER TABLE `published_file_table_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `published_file_table_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `range_detail`
--

DROP TABLE IF EXISTS `range_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `range_detail` (
  `ID` bigint(20) NOT NULL,
  `RANGE_ID` bigint(20) NOT NULL,
  `RANGE_ORDER` int(3) NOT NULL,
  `DESCRIPTION` varchar(255) NOT NULL,
  `LOWER_LIMIT` decimal(20,4) DEFAULT NULL,
  `UPPER_LIMIT` decimal(20,4) DEFAULT NULL,
  `VALUE` varchar(5) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_RD_RID` (`RANGE_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `range_detail`
--

LOCK TABLES `range_detail` WRITE;
/*!40000 ALTER TABLE `range_detail` DISABLE KEYS */;
/*!40000 ALTER TABLE `range_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ranges`
--

DROP TABLE IF EXISTS `ranges`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ranges` (
  `ID` bigint(20) NOT NULL,
  `NAME` varchar(255) NOT NULL,
  `DESCRIPTION` varchar(255) NOT NULL,
  `ENABLED` int(1) NOT NULL DEFAULT '0',
  `USER_ID` bigint(20) NOT NULL,
  `CONCEPT_BED_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_R_UID` (`USER_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ranges`
--

LOCK TABLES `ranges` WRITE;
/*!40000 ALTER TABLE `ranges` DISABLE KEYS */;
/*!40000 ALTER TABLE `ranges` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `relation`
--

DROP TABLE IF EXISTS `relation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `relation` (
  `ID` bigint(20) NOT NULL,
  `NAME` varchar(255) NOT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `DISPLAY_NAME` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `NAME_IDX` (`NAME`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `relation`
--

LOCK TABLES `relation` WRITE;
/*!40000 ALTER TABLE `relation` DISABLE KEYS */;
INSERT INTO `relation` VALUES (101,'hasStatistics','Associates Statistics','Statistics');
INSERT INTO `relation` VALUES (102,'hasValue','Connects Value','Value');
INSERT INTO `relation` VALUES (103,'hasTimeframe','Connects Timeframe','Time Frame');
INSERT INTO `relation` VALUES (104,'hasLocation','Connects Location','Location');
INSERT INTO `relation` VALUES (105,'hasPeriodicInformation','Has Periodic Information','Has Periodic Information');
INSERT INTO `relation` VALUES (106,'parentResource','Parent Resource','Parent Resource');
INSERT INTO `relation` VALUES (107,'isComposedOf',NULL,'is Composed Of');
INSERT INTO `relation` VALUES (108,'isConvertableTo',NULL,'isConvertableTo');
INSERT INTO `relation` VALUES (109,'isRealizedAs',NULL,'is Realized As');
INSERT INTO `relation` VALUES (110,'isTransformableTo','isTransformableTo','isTransformableTo');
/*!40000 ALTER TABLE `relation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `report_group`
--

DROP TABLE IF EXISTS `report_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `report_group` (
  `ID` bigint(20) NOT NULL,
  `NAME` varchar(40) NOT NULL,
  `LINK_URL` varchar(100) NOT NULL,
  `IMAGE_URL` varchar(100) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `report_group`
--

LOCK TABLES `report_group` WRITE;
/*!40000 ALTER TABLE `report_group` DISABLE KEYS */;
INSERT INTO `report_group` VALUES (1,'Grid','reportView.action','images/main/CROSSTABLE.gif');
INSERT INTO `report_group` VALUES (2,'Chart','reportView.action','images/main/bartable.gif');
INSERT INTO `report_group` VALUES (3,'Pivot','showPivot.action','images/main/PIVOTTABLE.gif');
INSERT INTO `report_group` VALUES (4,'Excel','showCSV.action','images/main/CSVFILE.jpg');
INSERT INTO `report_group` VALUES (5,'Detail','showDetailReport.action','images/main/DGRID.gif');
INSERT INTO `report_group` VALUES (6,'DetailExcel','showCSV.action','images/main/DCSVFILE.gif');
INSERT INTO `report_group` VALUES (7,'DetailGroup','reportView.action','images/main/DGROUPTABLE.png');
INSERT INTO `report_group` VALUES (8,'Hierarchies','showHierarchyReport.action','images/main/HIERARCHIES.png');
/*!40000 ALTER TABLE `report_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `report_group_type`
--

DROP TABLE IF EXISTS `report_group_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `report_group_type` (
  `REPORT_GROUP_ID` bigint(20) NOT NULL,
  `REPORT_TYPE_ID` bigint(20) NOT NULL,
  KEY `FK_RGT_RTID` (`REPORT_TYPE_ID`),
  KEY `FK_RGT_RGID` (`REPORT_GROUP_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `report_group_type`
--

LOCK TABLES `report_group_type` WRITE;
/*!40000 ALTER TABLE `report_group_type` DISABLE KEYS */;
INSERT INTO `report_group_type` VALUES (1,1);
INSERT INTO `report_group_type` VALUES (1,5);
INSERT INTO `report_group_type` VALUES (2,2);
INSERT INTO `report_group_type` VALUES (2,3);
INSERT INTO `report_group_type` VALUES (2,7);
INSERT INTO `report_group_type` VALUES (2,8);
INSERT INTO `report_group_type` VALUES (2,9);
INSERT INTO `report_group_type` VALUES (2,10);
INSERT INTO `report_group_type` VALUES (2,11);
INSERT INTO `report_group_type` VALUES (2,12);
INSERT INTO `report_group_type` VALUES (2,13);
INSERT INTO `report_group_type` VALUES (2,14);
INSERT INTO `report_group_type` VALUES (2,15);
INSERT INTO `report_group_type` VALUES (2,16);
INSERT INTO `report_group_type` VALUES (2,17);
INSERT INTO `report_group_type` VALUES (2,18);
INSERT INTO `report_group_type` VALUES (2,19);
INSERT INTO `report_group_type` VALUES (3,4);
INSERT INTO `report_group_type` VALUES (4,99);
INSERT INTO `report_group_type` VALUES (5,20);
INSERT INTO `report_group_type` VALUES (6,98);
INSERT INTO `report_group_type` VALUES (7,21);
INSERT INTO `report_group_type` VALUES (2,25);
INSERT INTO `report_group_type` VALUES (2,50);
INSERT INTO `report_group_type` VALUES (8,60);
INSERT INTO `report_group_type` VALUES (2,26);
/*!40000 ALTER TABLE `report_group_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `report_type`
--

DROP TABLE IF EXISTS `report_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `report_type` (
  `ID` bigint(20) NOT NULL,
  `NAME` varchar(40) NOT NULL,
  `DESCRIPTION` text,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `report_type`
--

LOCK TABLES `report_type` WRITE;
/*!40000 ALTER TABLE `report_type` DISABLE KEYS */;
INSERT INTO `report_type` VALUES (1,'Grid',NULL);
INSERT INTO `report_type` VALUES (2,'BarChart',NULL);
INSERT INTO `report_type` VALUES (3,'LineChart',NULL);
INSERT INTO `report_type` VALUES (4,'PivotTable',NULL);
INSERT INTO `report_type` VALUES (5,'GroupTable',NULL);
INSERT INTO `report_type` VALUES (6,'CrossTable',NULL);
INSERT INTO `report_type` VALUES (7,'CrossBarChart',NULL);
INSERT INTO `report_type` VALUES (8,'CrossLineChart',NULL);
INSERT INTO `report_type` VALUES (9,'HierarchyChart',NULL);
INSERT INTO `report_type` VALUES (10,'BarLineChart',NULL);
INSERT INTO `report_type` VALUES (11,'MultiBarChart',NULL);
INSERT INTO `report_type` VALUES (12,'ClusterBarChart',NULL);
INSERT INTO `report_type` VALUES (13,'CMultiBarChart',NULL);
INSERT INTO `report_type` VALUES (14,'MultiLineChart',NULL);
INSERT INTO `report_type` VALUES (15,'ClusterLineChart',NULL);
INSERT INTO `report_type` VALUES (16,'CMultiLineChart',NULL);
INSERT INTO `report_type` VALUES (17,'MultiLineClusterChart',NULL);
INSERT INTO `report_type` VALUES (18,'CMMultiBarChart',NULL);
INSERT INTO `report_type` VALUES (19,'CMMultiLineChart',NULL);
INSERT INTO `report_type` VALUES (20,'DetailGrid',NULL);
INSERT INTO `report_type` VALUES (98,'DetailCsvFile',NULL);
INSERT INTO `report_type` VALUES (99,'CsvFile',NULL);
INSERT INTO `report_type` VALUES (21,'DetailGroupTable',NULL);
INSERT INTO `report_type` VALUES (25,'PieChart',NULL);
INSERT INTO `report_type` VALUES (50,'ClusterPieChart',NULL);
INSERT INTO `report_type` VALUES (60,'HierarchicalGrid','Tree Grid representation for hierarchical data');
INSERT INTO `report_type` VALUES (26,'ScatterChart',NULL);
/*!40000 ALTER TABLE `report_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `resources`
--

DROP TABLE IF EXISTS `resources`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `resources` (
  `ID` bigint(20) NOT NULL,
  `NAME` varchar(50) NOT NULL COMMENT 'Resource name like action name, link name etc',
  `SCOPE` char(1) DEFAULT NULL COMMENT 'Scope of the action',
  `TYPE` char(1) DEFAULT NULL COMMENT 'Resource type like link, action',
  `DISPLAY_NAME` varchar(50) DEFAULT NULL COMMENT 'Display name',
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `resources`
--

LOCK TABLES `resources` WRITE;
/*!40000 ALTER TABLE `resources` DISABLE KEYS */;
INSERT INTO `resources` VALUES (1,'showApplications','A','A','Search Apps');
INSERT INTO `resources` VALUES (2,'showAssets','S','A','Extract Metadata');
INSERT INTO `resources` VALUES (3,'showAsset','S','A','Extract Metadata');
INSERT INTO `resources` VALUES (4,'showJoins','S','A','Review Joins');
INSERT INTO `resources` VALUES (5,'showMappings','S','A','Review Mappings');
INSERT INTO `resources` VALUES (6,'showConstraints','S','A','Review Constraints');
INSERT INTO `resources` VALUES (23,'showPublishedFileTables','S','A','Review Metadata');
INSERT INTO `resources` VALUES (8,'showUploadCSV','S','A','Upload');
INSERT INTO `resources` VALUES (9,'showDataSources','S','A','Locate');
INSERT INTO `resources` VALUES (10,'showDataSource','S','A','Locate');
INSERT INTO `resources` VALUES (11,'showSearchAppsDashboard','A','A','Search App Dashboard');
INSERT INTO `resources` VALUES (12,'showKnowledgebaseDashboard','K','A','Knowledge Base Dashboard');
INSERT INTO `resources` VALUES (22,'showAttributeRelations','K','A','Attribute Relation');
INSERT INTO `resources` VALUES (14,'showBusinessTerms','K','A','Entities & Relations');
INSERT INTO `resources` VALUES (21,'showMetadata','S','A','Review Metadata');
INSERT INTO `resources` VALUES (16,'showParallelWords','K','A','Synonyms');
INSERT INTO `resources` VALUES (17,'showAssetsDashboard','A','A','Datasets Dashboard');
INSERT INTO `resources` VALUES (18,'showUploadDataset','A','A','Locate or Upload');
INSERT INTO `resources` VALUES (19,'showPublishDatasets','S','A','Publish');
INSERT INTO `resources` VALUES (20,'showConceptType','K','A','Initial Concept to Type Association');
INSERT INTO `resources` VALUES (24,'showPublishDatasets','S','A','Publish App');
INSERT INTO `resources` VALUES (25,'showProfiles','K','A','Entity Groups');
INSERT INTO `resources` VALUES (26,'showConceptDefaultValue','K','A','Concept Default value');
INSERT INTO `resources` VALUES (27,'showMartRequest','S','A','Build Custom Datamarts');
INSERT INTO `resources` VALUES (28,'showCubeRequest','S','A','Build Custom Cubes');
INSERT INTO `resources` VALUES (29,'showRanges','K','A','Show User Defined Ranges');
INSERT INTO `resources` VALUES (30,'manageCubes','S','A','Manage Cubes');
INSERT INTO `resources` VALUES (31,'showBusinessEntityVariations','K','A','Entity Variations');
INSERT INTO `resources` VALUES (32,'showSnapshot','K','A','Cube Snapshot');
INSERT INTO `resources` VALUES (33,'showRelations','K','A','Relations');
INSERT INTO `resources` VALUES (34,'showMaintenanceHome','K','A','Show Maintenance Home');
INSERT INTO `resources` VALUES (35,'manageMarts','S','A','Manage Marts');
INSERT INTO `resources` VALUES (36,'showAssetSecurity','S','A','Asset Security');
INSERT INTO `resources` VALUES (37,'showTableSecurity','S','A','Table Security');
INSERT INTO `resources` VALUES (38,'showColumnSecurity','S','A','Column Security');
INSERT INTO `resources` VALUES (39,'showHierarchy','K','A','Hierarchy');
INSERT INTO `resources` VALUES (40,'showMemberSecurity','S','A','Member Security');
/*!40000 ALTER TABLE `resources` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ri_cloud`
--

DROP TABLE IF EXISTS `ri_cloud`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ri_cloud` (
  `ID` bigint(20) NOT NULL DEFAULT '0',
  `CLOUD_ID` bigint(20) NOT NULL COMMENT 'Id Of the Cloud',
  `CLOUD_NAME` varchar(255) DEFAULT NULL,
  `CLOUD_OUTPUT` char(1) DEFAULT NULL,
  `CLOUD_OUTPUT_BE_ID` bigint(20) DEFAULT NULL,
  `CLOUD_OUTPUT_NAME` varchar(255) DEFAULT NULL,
  `COMP_BE_ID` bigint(20) DEFAULT NULL,
  `COMP_NAME` varchar(255) DEFAULT NULL,
  `COMP_TYPE_BE_ID` bigint(20) DEFAULT NULL,
  `COMP_TYPE_NAME` varchar(255) DEFAULT NULL,
  `CLOUD_CATEGORY` int(2) DEFAULT NULL,
  `IMPORTANCE` decimal(10,2) NOT NULL DEFAULT '0.00',
  `FREQUENCY` int(2) NOT NULL DEFAULT '1',
  `MODEL_GROUP_ID` bigint(20) NOT NULL,
  `REALIZATION_BE_ID` bigint(20) DEFAULT NULL,
  `REALIZATION_NAME` varchar(255) DEFAULT NULL,
  `COMP_CATEGORY` int(2) DEFAULT NULL,
  `REPRESENTATIVE_ENTITY_TYPE` varchar(25) DEFAULT NULL,
  `REQUIRED_BEHAVIOR_BE_ID` bigint(20) DEFAULT NULL,
  `REQUIRED_BEHAVIOR_NAME` varchar(255) DEFAULT NULL,
  `REQUIRED` char(1) NOT NULL DEFAULT 'N' COMMENT 'To indicate if the current component is required for the cloud selection.',
  `DEFAULT_VALUE` varchar(255) DEFAULT NULL,
  `PRIMARY_RI_CLOUD_ID` bigint(20) DEFAULT NULL COMMENT 'Id of the primaryRiCloud from where the current entry is generated.',
  `OUTPUT_COMPONENT` char(1) DEFAULT 'N' COMMENT 'To indicate if the current component id is to be used as ouput be id for the cloud result',
  `CLOUD_PART` int(2) DEFAULT NULL COMMENT 'To indicate the Part Id of the component. two or more component may share the same partId',
  `cloud_selection` int(1) DEFAULT '1' COMMENT 'To indicate if the given component is enough to select a cloud, used for the app clouds.',
  PRIMARY KEY (`ID`),
  KEY `CLOUD_ID_IDX` (`CLOUD_ID`),
  KEY `COMP_BE_ID_IDX` (`COMP_BE_ID`),
  KEY `fk_ri_cloud_comp_type_be_id` (`COMP_TYPE_BE_ID`),
  KEY `fk_ricloud_realiz_typebeid` (`REALIZATION_BE_ID`)
) ENGINE=MyISAM AUTO_INCREMENT=236 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ri_cloud`
--

LOCK TABLES `ri_cloud` WRITE;
/*!40000 ALTER TABLE `ri_cloud` DISABLE KEYS */;
INSERT INTO `ri_cloud` VALUES (101,101,'Unit','N',151,'Unit',401,'Digit',401,'Digit',1,'100.00',1,1,NULL,NULL,1,'RI',NULL,NULL,'Y',NULL,NULL,'N',NULL,1);
INSERT INTO `ri_cloud` VALUES (102,602,'Currency','N',152,'Value',107,'UnitSymbol',107,'UnitSymbol',4,'10.00',1,1,NULL,NULL,1,'RTLI',NULL,NULL,'N',NULL,NULL,'N',2,1);
INSERT INTO `ri_cloud` VALUES (103,101,'Unit','N',151,'Unit',106,'UnitScale',106,'UnitScale',1,'0.00',1,1,NULL,NULL,1,'RTLI',NULL,NULL,'N',NULL,NULL,'N',NULL,1);
INSERT INTO `ri_cloud` VALUES (104,102,'Value','N',153,'Value',151,'Unit',151,'Unit',1,'80.00',1,1,NULL,NULL,1,'TLI',NULL,NULL,'Y',NULL,NULL,'N',1,1);
INSERT INTO `ri_cloud` VALUES (105,102,'Value','N',153,'Value',105,'Operator',105,'Operator',1,'10.00',1,1,NULL,NULL,1,'RTLI',NULL,NULL,'N',NULL,NULL,'N',3,1);
INSERT INTO `ri_cloud` VALUES (107,104,'Quarter','N',203,'Quarter',401,'Digit',401,'Digit',1,'50.00',1,1,NULL,NULL,1,'RI',NULL,NULL,'Y',NULL,NULL,'N',NULL,1);
INSERT INTO `ri_cloud` VALUES (108,104,'Quarter','N',203,'Quarter',203,'Quarter',203,'Quarter',1,'50.00',1,1,NULL,NULL,1,'T',NULL,NULL,'Y',NULL,NULL,'N',NULL,1);
INSERT INTO `ri_cloud` VALUES (109,105,'Year','N',202,'Year',401,'Digit',401,'Digit',1,'90.00',1,1,NULL,NULL,1,'RI',NULL,NULL,'Y',NULL,NULL,'N',NULL,1);
INSERT INTO `ri_cloud` VALUES (110,105,'Year','N',202,'Year',202,'Year',202,'Year',1,'10.00',1,1,NULL,NULL,1,'T',NULL,NULL,'N',NULL,NULL,'N',NULL,1);
INSERT INTO `ri_cloud` VALUES (111,106,'UnitRange','N',153,'Value',409,'RangePreposition',409,'RangePreposition',1,'25.00',1,1,NULL,NULL,1,'RTLI',NULL,NULL,'Y',NULL,NULL,'N',NULL,1);
INSERT INTO `ri_cloud` VALUES (112,106,'UnitRange','N',153,'Value',151,'Unit',151,'Unit',1,'50.00',2,1,NULL,NULL,1,'TLI',NULL,NULL,'Y',NULL,NULL,'N',NULL,1);
INSERT INTO `ri_cloud` VALUES (113,106,'UnitRange','N',153,'Value',408,'CoordinatingConjunction',408,'CoordinatingConjunction',1,'25.00',1,1,NULL,NULL,1,'RTLI',NULL,NULL,'Y',NULL,NULL,'N',NULL,1);
INSERT INTO `ri_cloud` VALUES (114,107,'TimeRange','N',201,'TimeFrame',409,'RangePreposition',409,'RangePreposition',1,'25.00',1,1,NULL,NULL,1,'RTLI',NULL,NULL,'Y',NULL,NULL,'N',NULL,1);
INSERT INTO `ri_cloud` VALUES (115,107,'TimeRange','N',201,'TimeFrame',201,'TimeFrame',201,'TimeFrame',1,'50.00',2,1,NULL,NULL,1,'RTLI',NULL,NULL,'Y',NULL,NULL,'N',NULL,1);
INSERT INTO `ri_cloud` VALUES (116,107,'TimeRange','N',201,'TimeFrame',408,'CoordinatingConjunction',408,'CoordinatingConjunction',1,'25.00',1,1,NULL,NULL,1,'RTLI',NULL,NULL,'Y',NULL,NULL,'N',NULL,1);
INSERT INTO `ri_cloud` VALUES (117,108,'FromToUnitRange','N',153,'Value',404,'Conjunction',404,'Conjunction',1,'25.00',1,1,NULL,NULL,1,'RTLI',NULL,NULL,'Y',NULL,NULL,'N',NULL,1);
INSERT INTO `ri_cloud` VALUES (118,108,'FromToUnitRange','N',153,'Value',151,'Unit',151,'Unit',1,'50.00',2,1,NULL,NULL,1,'TLI',NULL,NULL,'Y',NULL,NULL,'N',NULL,1);
INSERT INTO `ri_cloud` VALUES (119,108,'FromToUnitRange','N',153,'Value',405,'Preposition',405,'Preposition',1,'25.00',1,1,NULL,NULL,1,'RTLI',NULL,NULL,'Y',NULL,NULL,'N',NULL,1);
INSERT INTO `ri_cloud` VALUES (120,109,'FromToTimeRange','N',201,'TimeFrame',404,'Conjunction',404,'Conjunction',1,'25.00',1,1,NULL,NULL,1,'RTLI',NULL,NULL,'Y',NULL,NULL,'N',NULL,1);
INSERT INTO `ri_cloud` VALUES (121,109,'FromToTimeRange','N',201,'TimeFrame',201,'TimeFrame',201,'TimeFrame',1,'50.00',2,1,NULL,NULL,1,'RTLI',NULL,NULL,'Y',NULL,NULL,'N',NULL,1);
INSERT INTO `ri_cloud` VALUES (122,109,'FromToTimeRange','N',201,'TimeFrame',405,'Preposition',405,'Preposition',1,'25.00',1,1,NULL,NULL,1,'RTLI',NULL,NULL,'Y',NULL,NULL,'N',NULL,1);
INSERT INTO `ri_cloud` VALUES (123,110,'MonthTimeFrame','N',201,'TimeFrame',204,'Month',204,'Month',1,'85.00',1,1,NULL,NULL,1,'TLI',NULL,NULL,'Y',NULL,NULL,'N',3,1);
INSERT INTO `ri_cloud` VALUES (124,110,'MonthTimeFrame','N',201,'TimeFrame',202,'Year',202,'Year',1,'10.00',1,1,NULL,NULL,1,'TLI',NULL,NULL,'N',NULL,NULL,'N',2,1);
INSERT INTO `ri_cloud` VALUES (125,111,'QuarterTimeFrame','N',201,'TimeFrame',203,'Quarter',203,'Quarter',1,'85.00',1,1,NULL,NULL,1,'TLI',NULL,NULL,'Y',NULL,NULL,'N',3,1);
INSERT INTO `ri_cloud` VALUES (126,111,'QuarterTimeFrame','N',201,'TimeFrame',202,'Year',202,'Year',1,'10.00',1,1,NULL,NULL,1,'TLI',NULL,NULL,'N',NULL,NULL,'N',2,1);
INSERT INTO `ri_cloud` VALUES (127,112,'YearTimeFrame','N',201,'TimeFrame',202,'Year',202,'Year',1,'95.00',1,1,NULL,NULL,1,'TLI',NULL,NULL,'Y',NULL,NULL,'N',2,1);
INSERT INTO `ri_cloud` VALUES (128,113,'RelativeYear','N',201,'TimeFrame',403,'Adjective',403,'Adjective',1,'45.00',1,1,NULL,NULL,1,'RTLI',NULL,NULL,'Y',NULL,NULL,'N',NULL,1);
INSERT INTO `ri_cloud` VALUES (129,113,'RelativeYear','N',201,'TimeFrame',401,'Digit',401,'Digit',1,'10.00',1,1,NULL,NULL,1,'RI',NULL,NULL,'N',NULL,NULL,'N',NULL,1);
INSERT INTO `ri_cloud` VALUES (130,113,'RelativeYear','N',201,'TimeFrame',202,'Year',202,'Year',1,'45.00',1,1,NULL,NULL,1,'T',NULL,NULL,'N',NULL,NULL,'N',NULL,1);
INSERT INTO `ri_cloud` VALUES (156,117,'RelativeMonth','N',204,'Month',401,'Digit',401,'Digit',1,'10.00',1,1,NULL,NULL,1,'RI',NULL,NULL,'N',NULL,NULL,'N',2,1);
INSERT INTO `ri_cloud` VALUES (155,117,'RelativeMonth','N',204,'Month',403,'Adjective',403,'Adjective',1,'45.00',1,1,NULL,NULL,1,'RTLI',NULL,NULL,'Y',NULL,NULL,'N',1,1);
INSERT INTO `ri_cloud` VALUES (133,501,'TopBottom','E',NULL,NULL,9006,'COMPARATIVE',NULL,NULL,3,'33.00',1,1,NULL,NULL,4,'C',NULL,NULL,'N',NULL,NULL,'N',3,1);
INSERT INTO `ri_cloud` VALUES (134,501,'TopBottom','E',NULL,NULL,101,'MeasurableEntity',101,'MeasurableEntity',3,'33.00',1,1,NULL,NULL,1,'C',NULL,NULL,'N',NULL,NULL,'N',2,1);
INSERT INTO `ri_cloud` VALUES (135,501,'TopBottom','E',NULL,NULL,110,'ComparativeInformation',110,'ComparativeInformation',3,'34.00',1,1,NULL,NULL,1,'RTLI',NULL,NULL,'Y',NULL,NULL,'N',1,1);
INSERT INTO `ri_cloud` VALUES (136,502,'Group-By','E',NULL,NULL,9004,'ENUMERATION',NULL,NULL,3,'50.00',1,1,NULL,NULL,4,'C',NULL,NULL,'Y',NULL,NULL,'N',NULL,1);
INSERT INTO `ri_cloud` VALUES (137,502,'Group-By','E',NULL,NULL,406,'ByConjunction',406,'ByConjunction',3,'50.00',1,1,NULL,NULL,1,'RTLI',NULL,NULL,'Y',NULL,NULL,'N',NULL,1);
INSERT INTO `ri_cloud` VALUES (138,114,'ComparativeInformation','N',110,'ComparativeInformation',401,'Digit',401,'Digit',1,'50.00',1,1,NULL,NULL,1,'RI',NULL,NULL,'N',NULL,NULL,'N',NULL,1);
INSERT INTO `ri_cloud` VALUES (139,114,'ComparativeInformation','N',110,'ComparativeInformation',104,'ComparativeStatistics',104,'ComparativeStatistics',1,'50.00',1,1,NULL,NULL,1,'RTLI',NULL,NULL,'Y',NULL,NULL,'N',NULL,1);
INSERT INTO `ri_cloud` VALUES (140,503,'Group-By1','E',NULL,NULL,101,'MeasurableEntity',101,'MeasurableEntity',3,'50.00',1,1,NULL,NULL,1,'C',NULL,NULL,'Y',NULL,NULL,'N',NULL,1);
INSERT INTO `ri_cloud` VALUES (141,503,'Group-By1','E',NULL,NULL,406,'ByConjunction',406,'ByConjunction',3,'50.00',1,1,NULL,NULL,1,'RTLI',NULL,NULL,'Y',NULL,NULL,'N',NULL,1);
INSERT INTO `ri_cloud` VALUES (142,601,'RelativeForQuantitative','N',NULL,NULL,9003,'Quantitative',201,'TimeFrame',4,'45.00',1,1,NULL,NULL,3,'C',NULL,NULL,'Y',NULL,NULL,'Y',NULL,1);
INSERT INTO `ri_cloud` VALUES (143,601,'RelativeForQuantitative','N',NULL,NULL,401,'Digit',401,'Digit',4,'10.00',1,1,NULL,NULL,1,'RI',NULL,NULL,'N',NULL,NULL,'N',NULL,1);
INSERT INTO `ri_cloud` VALUES (144,601,'RelativeForQuantitative','N',NULL,NULL,403,'Adjective',403,'Adjective',4,'45.00',1,1,NULL,NULL,1,'RTLI',NULL,NULL,'Y',NULL,NULL,'N',NULL,1);
INSERT INTO `ri_cloud` VALUES (145,115,'Value1','N',153,'Value',151,'Unit',151,'Unit',1,'50.00',1,1,NULL,NULL,1,'TLI',NULL,NULL,'Y',NULL,NULL,'N',NULL,1);
INSERT INTO `ri_cloud` VALUES (146,115,'Value1','N',153,'Value',410,'valuePreposition',410,'ValuePreposition',1,'50.00',1,1,NULL,NULL,1,'RTLI',NULL,NULL,'Y',NULL,NULL,'N',NULL,1);
INSERT INTO `ri_cloud` VALUES (150,112,'YearTimeFrame','N',201,'TimeFrame',413,'TimePreposition',413,'TimePreposition',1,'5.00',1,1,NULL,NULL,1,'RTLI',NULL,NULL,'N',NULL,NULL,'N',1,1);
INSERT INTO `ri_cloud` VALUES (157,117,'RelativeMonth','N',204,'Month',204,'Month',204,'Month',1,'45.00',1,1,NULL,NULL,1,'T',NULL,NULL,'Y',NULL,NULL,'N',3,1);
INSERT INTO `ri_cloud` VALUES (159,118,'RelativeQuarter','N',203,'Quarter',403,'Adjective',403,'Adjective',1,'45.00',1,1,NULL,NULL,1,'RTLI',NULL,NULL,'Y',NULL,NULL,'N',1,1);
INSERT INTO `ri_cloud` VALUES (160,118,'RelativeQuarter','N',203,'Quarter',401,'Digit',401,'Digit',1,'10.00',1,1,NULL,NULL,1,'RI',NULL,NULL,'N',NULL,NULL,'N',2,1);
INSERT INTO `ri_cloud` VALUES (161,118,'RelativeQuarter','N',203,'Quarter',203,'Quarter',203,'Quarter',1,'45.00',1,1,NULL,NULL,1,'T',NULL,NULL,'Y',NULL,NULL,'N',3,1);
INSERT INTO `ri_cloud` VALUES (147,116,'DayTimeFrame','N',201,'TimeFrame',206,'day',206,'Day',1,'70.00',1,1,NULL,NULL,1,'TLI',NULL,NULL,'Y',NULL,NULL,'N',4,1);
INSERT INTO `ri_cloud` VALUES (148,116,'DayTimeFrame','N',201,'TimeFrame',204,'Month',204,'Month',1,'15.00',1,1,NULL,NULL,1,'TLI',NULL,NULL,'N',NULL,NULL,'N',3,1);
INSERT INTO `ri_cloud` VALUES (149,116,'DayTimeFrame','N',201,'TimeFrame',202,'Year',202,'Year',1,'10.00',1,1,NULL,NULL,1,'TLI',NULL,NULL,'N',NULL,NULL,'N',2,1);
INSERT INTO `ri_cloud` VALUES (162,119,'RelativeDay','N',206,'Day',403,'Adjective',403,'Adjective',1,'45.00',1,1,NULL,NULL,1,'RTLI',NULL,NULL,'Y',NULL,NULL,'N',1,1);
INSERT INTO `ri_cloud` VALUES (163,119,'RelativeDay','N',206,'Day',401,'Digit',401,'Digit',1,'10.00',1,1,NULL,NULL,1,'RI',NULL,NULL,'N',NULL,NULL,'N',2,1);
INSERT INTO `ri_cloud` VALUES (164,119,'RelativeDay','N',206,'Day',206,'Day',206,'Day',1,'45.00',1,1,NULL,NULL,1,'T',NULL,NULL,'Y',NULL,NULL,'N',3,1);
INSERT INTO `ri_cloud` VALUES (165,110,'MonthTimeFrame','N',201,'TimeFrame',413,'TimePreposition',413,'TimePreposition',1,'5.00',1,1,NULL,NULL,1,'RTLI',NULL,NULL,'N',NULL,NULL,'N',1,1);
INSERT INTO `ri_cloud` VALUES (166,111,'QuarterTimeFrame','N',201,'TimeFrame',413,'TimePreposition',413,'TimePreposition',1,'5.00',1,1,NULL,NULL,1,'RTLI',NULL,NULL,'N',NULL,NULL,'N',1,1);
INSERT INTO `ri_cloud` VALUES (167,116,'DayTimeFrame','N',201,'TimeFrame',413,'TimePreposition',413,'TimePreposition',1,'5.00',1,1,NULL,NULL,1,'RTLI',NULL,NULL,'N',NULL,NULL,'N',1,1);
INSERT INTO `ri_cloud` VALUES (184,606,'BetweenAndValue','N',153,'Value',408,'CoordinatingConjunction',408,'CoordinatingConjunction',4,'10.00',1,1,NULL,NULL,1,'TLI',NULL,NULL,'N',NULL,NULL,'N',NULL,1);
INSERT INTO `ri_cloud` VALUES (185,606,'BetweenAndValue','N',153,'Value',153,'Value',153,'Value',4,'80.00',2,1,NULL,NULL,1,'TLI',NULL,NULL,'Y',NULL,NULL,'Y',NULL,1);
INSERT INTO `ri_cloud` VALUES (183,606,'BetweenAndValue','N',153,'Value',409,'RangePreposition',409,'RangePreposition',4,'10.00',1,1,NULL,NULL,1,'RTLI',NULL,NULL,'Y',NULL,NULL,'N',NULL,1);
INSERT INTO `ri_cloud` VALUES (172,602,'Currency','N',152,'Value',151,'UNIT',151,'UNIT',4,'80.00',1,1,NULL,NULL,1,'TLI',NULL,NULL,'Y',NULL,NULL,'N',1,1);
INSERT INTO `ri_cloud` VALUES (173,602,'Currency','N',152,'Value',105,'Operator',105,'Operator',4,'10.00',1,1,NULL,NULL,1,'RTLI',NULL,NULL,'N',NULL,NULL,'N',3,1);
INSERT INTO `ri_cloud` VALUES (174,603,'Distance','N',154,'Value',107,'UNITSymbol',107,'UNITSymbol',4,'10.00',1,1,NULL,NULL,1,'RTLI',NULL,NULL,'Y',NULL,NULL,'N',NULL,1);
INSERT INTO `ri_cloud` VALUES (176,603,'Distance','N',154,'Value',151,'UNIT',151,'UNIT',4,'80.00',1,1,NULL,NULL,1,'TLI',NULL,NULL,'Y',NULL,NULL,'N',NULL,1);
INSERT INTO `ri_cloud` VALUES (175,603,'Distance','N',154,'Value',105,'Operator',105,'Operator',4,'10.00',1,1,NULL,NULL,1,'RTLI',NULL,NULL,'N',NULL,NULL,'N',NULL,1);
INSERT INTO `ri_cloud` VALUES (177,604,'Volume','N',155,'Value',107,'UNITSymbol',107,'UNITSymbol',4,'10.00',1,1,NULL,NULL,1,'RTLI',NULL,NULL,'Y',NULL,NULL,'N',NULL,1);
INSERT INTO `ri_cloud` VALUES (179,604,'Volume','N',155,'Value',151,'UNIT',151,'UNIT',4,'80.00',1,1,NULL,NULL,1,'TLI',NULL,NULL,'Y',NULL,NULL,'N',NULL,1);
INSERT INTO `ri_cloud` VALUES (178,604,'Volume','N',155,'Value',105,'Operator',105,'Operator',4,'10.00',1,1,NULL,NULL,1,'RTLI',NULL,NULL,'N',NULL,NULL,'N',NULL,1);
INSERT INTO `ri_cloud` VALUES (180,605,'Power','N',156,'Value',107,'UNITSymbol',107,'UNITSymbol',4,'10.00',1,1,NULL,NULL,1,'RTLI',NULL,NULL,'Y',NULL,NULL,'N',NULL,1);
INSERT INTO `ri_cloud` VALUES (182,605,'Power','N',156,'Value',151,'UNIT',151,'UNIT',4,'80.00',1,1,NULL,NULL,1,'TLI',NULL,NULL,'Y',NULL,NULL,'N',NULL,1);
INSERT INTO `ri_cloud` VALUES (181,605,'Power','N',156,'Value',105,'Operator',105,'Operator',4,'10.00',1,1,NULL,NULL,1,'RTLI',NULL,NULL,'N',NULL,NULL,'N',NULL,1);
INSERT INTO `ri_cloud` VALUES (186,202,'Time','N',201,'TimeFrame',211,'Time',211,'Time',1,'40.00',1,1,NULL,NULL,1,'TLI',NULL,NULL,'Y',NULL,NULL,'N',1,1);
INSERT INTO `ri_cloud` VALUES (187,202,'Time','N',201,'TimeFrame',206,'Day',206,'Day',1,'20.00',1,1,NULL,NULL,1,'TLI',NULL,NULL,'N',NULL,NULL,'N',2,1);
INSERT INTO `ri_cloud` VALUES (188,202,'Time','N',201,'TimeFrame',204,'Month',204,'Month',1,'15.00',1,1,NULL,NULL,1,'TLI',NULL,NULL,'N',NULL,NULL,'N',3,1);
INSERT INTO `ri_cloud` VALUES (189,202,'Time','N',201,'TimeFrame',202,'Year',202,'year',1,'10.00',1,1,NULL,NULL,1,'TLI',NULL,NULL,'N',NULL,NULL,'N',4,1);
INSERT INTO `ri_cloud` VALUES (190,202,'Time','N',201,'TimeFrame',412,'TimeQualifier',412,'TimeQualifier',1,'10.00',1,1,NULL,NULL,1,'TLI',NULL,NULL,'N',NULL,NULL,'N',5,1);
INSERT INTO `ri_cloud` VALUES (191,202,'Time','N',201,'TimeFrame',413,'TimePreposition',413,'TimePreposition',1,'5.00',1,1,NULL,NULL,1,'TLI',NULL,NULL,'N',NULL,NULL,'N',6,1);
INSERT INTO `ri_cloud` VALUES (192,607,'ValueWithPreposition','N',153,'Value',153,'Value',153,'Value',4,'50.00',1,1,NULL,NULL,1,'CLI',NULL,NULL,'Y',NULL,NULL,'Y',NULL,1);
INSERT INTO `ri_cloud` VALUES (193,607,'ValueWithPreposition','N',153,'Value',410,'ValuePreposition',410,'ValuePreposition',4,'50.00',1,1,NULL,NULL,1,'RTLI',NULL,NULL,'Y',NULL,NULL,'N',NULL,1);
INSERT INTO `ri_cloud` VALUES (194,203,'Week','N',205,'Week',205,'Week',205,'Week',1,'50.00',1,1,NULL,NULL,1,'T',NULL,NULL,'Y',NULL,NULL,'N',NULL,1);
INSERT INTO `ri_cloud` VALUES (195,203,'Week','N',205,'Week',401,'Digit',401,'Digit',1,'50.00',1,1,NULL,NULL,1,'RTLI',NULL,NULL,'Y',NULL,NULL,'N',NULL,1);
INSERT INTO `ri_cloud` VALUES (197,204,'WeekTimeFrame','N',201,'TimeFrame',205,'Week',205,'Week',1,'70.00',1,1,NULL,NULL,1,'TLI',NULL,NULL,'Y',NULL,NULL,'N',NULL,1);
INSERT INTO `ri_cloud` VALUES (198,204,'WeekTimeFrame','N',201,'TimeFrame',204,'Month',204,'Month',1,'15.00',1,1,NULL,NULL,1,'TLI',NULL,NULL,'N',NULL,NULL,'N',NULL,1);
INSERT INTO `ri_cloud` VALUES (199,204,'WeekTimeFrame','N',201,'TimeFrame',202,'Year',202,'year',1,'15.00',1,1,NULL,NULL,1,'TLI',NULL,NULL,'N',NULL,NULL,'N',NULL,1);
INSERT INTO `ri_cloud` VALUES (106,102,'Value','N',153,'Value',107,'UNITSymbol',107,'UNITSymbol',1,'10.00',1,1,NULL,NULL,1,'RTLI',NULL,NULL,'N',NULL,NULL,'N',2,1);
INSERT INTO `ri_cloud` VALUES (239,116,'DayTimeFrame','N',201,'TimeFrame',105,'Operator',105,'Operator',1,'5.00',1,1,NULL,NULL,1,'RTLI',NULL,NULL,'N',NULL,NULL,'N',1,1);
INSERT INTO `ri_cloud` VALUES (238,111,'QuarterTimeFrame','N',201,'TimeFrame',105,'Operator',105,'Operator',1,'5.00',1,1,NULL,NULL,1,'RTLI',NULL,NULL,'N',NULL,NULL,'N',1,1);
INSERT INTO `ri_cloud` VALUES (237,110,'MonthTimeFrame','N',201,'TimeFrame',105,'Operator',105,'Operator',1,'5.00',1,1,NULL,NULL,1,'RTLI',NULL,NULL,'N',NULL,NULL,'N',1,1);
INSERT INTO `ri_cloud` VALUES (236,112,'YearTimeFrame','N',201,'TimeFrame',105,'Operator',105,'Operator',1,'5.00',1,1,NULL,NULL,1,'RTLI',NULL,NULL,'N',NULL,NULL,'N',1,1);
INSERT INTO `ri_cloud` VALUES (208,121,'RelativeTFTime','N',211,'Time',207,'Hour',207,'Hour',1,'45.00',1,1,NULL,NULL,1,'T',NULL,NULL,'N',NULL,NULL,'N',1,1);
INSERT INTO `ri_cloud` VALUES (209,121,'RelativeTFTime','N',211,'Time',208,'Minute',208,'Minute',1,'45.00',1,1,NULL,NULL,1,'T',NULL,NULL,'N',NULL,NULL,'N',1,1);
INSERT INTO `ri_cloud` VALUES (210,121,'RelativeTFTime','N',211,'Time',209,'Second',209,'Second',1,'45.00',1,1,NULL,NULL,1,'T',NULL,NULL,'N',NULL,NULL,'N',1,1);
INSERT INTO `ri_cloud` VALUES (211,121,'RelativeTFTime','N',211,'Time',401,'Digit',401,'Digit',1,'10.00',1,1,NULL,NULL,1,'RI',NULL,NULL,'N',NULL,NULL,'N',2,1);
INSERT INTO `ri_cloud` VALUES (212,121,'RelativeTFTime','N',211,'Time',403,'Adjective',403,'Adjective',1,'45.00',1,1,NULL,NULL,1,'RTLI',NULL,NULL,'Y',NULL,NULL,'N',3,1);
INSERT INTO `ri_cloud` VALUES (213,608,'FromToValue','N',153,'Value',405,'Preposition',405,'Preposition',4,'10.00',1,1,NULL,NULL,1,'RTLI',NULL,NULL,'Y',NULL,NULL,'N',NULL,1);
INSERT INTO `ri_cloud` VALUES (215,608,'FromToValue','N',153,'Value',153,'Value',153,'Value',4,'80.00',2,1,NULL,NULL,1,'TLI',NULL,NULL,'Y',NULL,NULL,'Y',NULL,1);
INSERT INTO `ri_cloud` VALUES (214,608,'FromToValue','N',153,'Value',404,'Conjunction',404,'Conjunction',4,'10.00',1,1,NULL,NULL,1,'TLI',NULL,NULL,'N',NULL,NULL,'N',NULL,1);
INSERT INTO `ri_cloud` VALUES (216,609,'Weight','N',160,'Value',107,'UnitSymbol',107,'UnitSymbol',4,'10.00',1,1,NULL,NULL,1,'RTLI',NULL,NULL,'Y',NULL,NULL,'N',NULL,1);
INSERT INTO `ri_cloud` VALUES (217,609,'Weight','N',160,'Value',151,'Unit',151,'Unit',4,'80.00',1,1,NULL,NULL,1,'TLI',NULL,NULL,'Y',NULL,NULL,'N',NULL,1);
INSERT INTO `ri_cloud` VALUES (218,609,'Weight','N',160,'Value',105,'Operator',105,'Operator',4,'10.00',1,1,NULL,NULL,1,'RTLI',NULL,NULL,'N',NULL,NULL,'N',NULL,1);
INSERT INTO `ri_cloud` VALUES (219,610,'TimeDuration','N',161,'Value',204,'Month',204,'Month',4,'10.00',1,1,NULL,NULL,1,'T',NULL,NULL,'N',NULL,NULL,'N',1,1);
INSERT INTO `ri_cloud` VALUES (220,610,'TimeDuration','N',161,'Value',202,'Year',202,'Year',4,'10.00',1,1,NULL,NULL,1,'T',NULL,NULL,'N',NULL,NULL,'N',1,1);
INSERT INTO `ri_cloud` VALUES (221,610,'TimeDuration','N',161,'Value',206,'Day',206,'Day',4,'10.00',1,1,NULL,NULL,1,'T',NULL,NULL,'N',NULL,NULL,'N',1,1);
INSERT INTO `ri_cloud` VALUES (222,610,'TimeDuration','N',161,'Value',151,'Unit',151,'Unit',4,'80.00',1,1,NULL,NULL,1,'TLI',NULL,NULL,'Y',NULL,NULL,'N',2,1);
INSERT INTO `ri_cloud` VALUES (223,610,'TimeDuration','N',161,'Value',105,'Operator',105,'Operator',4,'10.00',1,1,NULL,NULL,1,'RTLI',NULL,NULL,'N',NULL,NULL,'N',3,1);
INSERT INTO `ri_cloud` VALUES (224,122,'RelativeWeek','N',205,'Week',403,'Adjective',403,'Adjective',1,'45.00',1,1,NULL,NULL,1,'RTLI',NULL,NULL,'Y',NULL,NULL,'N',1,1);
INSERT INTO `ri_cloud` VALUES (225,122,'RelativeWeek','N',205,'Week',401,'Digit',401,'Digit',1,'10.00',1,1,NULL,NULL,1,'RI',NULL,NULL,'N',NULL,NULL,'N',2,1);
INSERT INTO `ri_cloud` VALUES (226,122,'RelativeWeek','N',205,'Week',205,'Week',205,'Week',1,'45.00',1,1,NULL,NULL,1,'T',NULL,NULL,'Y',NULL,NULL,'N',3,1);
INSERT INTO `ri_cloud` VALUES (227,123,'RelativeWeekday','N',213,'Weekday',403,'Adjective',403,'Adjective',1,'10.00',1,1,NULL,NULL,1,'RTLI',NULL,NULL,'Y',NULL,NULL,'N',1,1);
INSERT INTO `ri_cloud` VALUES (228,123,'RelativeWeekday','N',213,'Weekday',401,'Digit',401,'Digit',1,'10.00',1,1,NULL,NULL,1,'RI',NULL,NULL,'N',NULL,NULL,'N',2,1);
INSERT INTO `ri_cloud` VALUES (229,123,'RelativeWeekday','N',213,'Weekday',213,'Weekday',213,'Weekday',1,'80.00',1,1,NULL,NULL,1,'TLI',NULL,NULL,'Y',NULL,NULL,'N',3,1);
INSERT INTO `ri_cloud` VALUES (230,124,'WeekdayTimeFrame','N',201,'TimeFrame',413,'TimePreposition',413,'TimePreposition',1,'1.00',1,1,NULL,NULL,1,'RTLI',NULL,NULL,'N',NULL,NULL,'N',1,1);
INSERT INTO `ri_cloud` VALUES (231,124,'WeekdayTimeFrame','N',201,'TimeFrame',213,'WeekDay',213,'WeekDay',1,'90.00',1,1,NULL,NULL,1,'TLI',NULL,NULL,'Y',NULL,NULL,'N',2,1);
INSERT INTO `ri_cloud` VALUES (232,124,'WeekdayTimeFrame','N',201,'TimeFrame',205,'Week',205,'Week',1,'1.00',1,1,NULL,NULL,1,'RTLI',NULL,NULL,'N',NULL,NULL,'N',3,1);
INSERT INTO `ri_cloud` VALUES (233,124,'WeekdayTimeFrame','N',201,'TimeFrame',204,'Month',204,'Month',1,'4.00',1,1,NULL,NULL,1,'TLI',NULL,NULL,'N',NULL,NULL,'N',4,1);
INSERT INTO `ri_cloud` VALUES (234,124,'WeekdayTimeFrame','N',201,'TimeFrame',202,'Year',202,'Year',1,'4.00',1,1,NULL,NULL,1,'RTLI',NULL,NULL,'N',NULL,NULL,'N',5,1);
INSERT INTO `ri_cloud` VALUES (235,202,'Time','N',201,'TimeFrame',213,'WeekDay',213,'WeekDay',1,'20.00',1,1,NULL,NULL,1,'TLI',NULL,NULL,'N',NULL,NULL,'N',2,1);
INSERT INTO `ri_cloud` VALUES (240,202,'Time','N',201,'TimeFrame',105,'Operator',105,'Operator',1,'5.00',1,1,NULL,NULL,1,'RTLI',NULL,NULL,'N',NULL,NULL,'N',6,1);
INSERT INTO `ri_cloud` VALUES (241,611,'YearTimeFrameConcept','N',201,'TimeFrame',9003,'Quantitative',201,'TimeFrame',4,'45.00',1,1,NULL,NULL,3,'C',NULL,NULL,'Y',NULL,NULL,'Y',1,1);
INSERT INTO `ri_cloud` VALUES (242,611,'YearTimeFrameConcept','N',201,'TimeFrame',202,'Year',202,'Year',4,'10.00',1,1,NULL,NULL,1,'TLI',NULL,NULL,'Y',NULL,NULL,'N',2,1);
INSERT INTO `ri_cloud` VALUES (243,611,'YearTimeFrameConcept','N',201,'TimeFrame',105,'Operator',105,'Operator',4,'45.00',1,1,NULL,NULL,1,'RTLI',NULL,NULL,'Y',NULL,NULL,'N',3,1);
INSERT INTO `ri_cloud` VALUES (244,612,'Location','N',301,'Location',304,'City',304,'City',1,'100.00',1,1,NULL,NULL,1,'TLI',NULL,NULL,'N',NULL,NULL,'N',NULL,1);
INSERT INTO `ri_cloud` VALUES (245,612,'Location','N',301,'Location',303,'State',303,'State',1,'100.00',1,1,NULL,NULL,1,'TLI',NULL,NULL,'N',NULL,NULL,'N',NULL,1);
INSERT INTO `ri_cloud` VALUES (246,612,'Location','N',301,'Location',302,'Country',302,'Country',1,'100.00',1,1,NULL,NULL,1,'TLI',NULL,NULL,'N',NULL,NULL,'N',NULL,1);
INSERT INTO `ri_cloud` VALUES (247,612,'Location','N',301,'Location',305,'County',305,'County',1,'100.00',1,1,NULL,NULL,1,'TLI',NULL,NULL,'N',NULL,NULL,'N',NULL,1);
INSERT INTO `ri_cloud` VALUES (248,610,'TimeDuration','N',161,'Value',205,'Week',205,'Week',4,'10.00',1,1,NULL,NULL,1,'T',NULL,NULL,'N',NULL,NULL,'N',1,1);
INSERT INTO `ri_cloud` VALUES (249,613,'Area','N',414,'Value',107,'UnitSymbol',107,'UnitSymbol',4,'10.00',1,1,NULL,NULL,1,'RTLI',NULL,NULL,'Y',NULL,NULL,'N',NULL,1);
INSERT INTO `ri_cloud` VALUES (250,613,'Area','N',414,'Value',151,'Unit',151,'Unit',4,'80.00',1,1,NULL,NULL,1,'RTLI',NULL,NULL,'Y',NULL,NULL,'N',NULL,1);
INSERT INTO `ri_cloud` VALUES (251,613,'Area','N',414,'Value',105,'Operator',105,'Operator',4,'10.00',1,1,NULL,NULL,1,'RTLI',NULL,NULL,'N',NULL,NULL,'N',NULL,1);
INSERT INTO `ri_cloud` VALUES (252,614,'Memory','N',422,'Value',107,'UnitSymbol',107,'UnitSymbol',4,'10.00',1,1,NULL,NULL,1,'RTLI',NULL,NULL,'Y',NULL,NULL,'N',NULL,1);
INSERT INTO `ri_cloud` VALUES (253,614,'Memory','N',422,'Value',151,'Unit',151,'Unit',4,'80.00',1,1,NULL,NULL,1,'RTLI',NULL,NULL,'Y',NULL,NULL,'N',NULL,1);
INSERT INTO `ri_cloud` VALUES (254,614,'Memory','N',422,'Value',105,'Operator',105,'Operator',4,'10.00',1,1,NULL,NULL,1,'RTLI',NULL,NULL,'N',NULL,NULL,'N',NULL,1);
INSERT INTO `ri_cloud` VALUES (255,615,'Resolution','N',427,'Value',107,'UnitSymbol',107,'UnitSymbol',4,'10.00',1,1,NULL,NULL,1,'RTLI',NULL,NULL,'Y',NULL,NULL,'N',NULL,1);
INSERT INTO `ri_cloud` VALUES (256,615,'Resolution','N',427,'Value',151,'Unit',151,'Unit',4,'80.00',1,1,NULL,NULL,1,'RTLI',NULL,NULL,'Y',NULL,NULL,'N',NULL,1);
INSERT INTO `ri_cloud` VALUES (257,615,'Resolution','N',427,'Value',105,'Operator',105,'Operator',4,'10.00',1,1,NULL,NULL,1,'RTLI',NULL,NULL,'N',NULL,NULL,'N',NULL,1);
INSERT INTO `ri_cloud` VALUES (258,616,'Number','N',157,'Value',151,'Unit',151,'Unit',4,'80.00',1,1,NULL,NULL,1,'RTLI',NULL,NULL,'Y',NULL,NULL,'N',NULL,1);
INSERT INTO `ri_cloud` VALUES (259,616,'Number','N',157,'Value',105,'Operator',105,'Operator',4,'20.00',1,1,NULL,NULL,1,'RTLI',NULL,NULL,'N',NULL,NULL,'N',NULL,1);
/*!40000 ALTER TABLE `ri_cloud` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ri_onto_term`
--

DROP TABLE IF EXISTS `ri_onto_term`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ri_onto_term` (
  `ID` bigint(20) NOT NULL,
  `WORD` varchar(255) DEFAULT NULL,
  `CONCEPT_NAME` varchar(255) DEFAULT NULL,
  `INSTANCE_NAME` varchar(255) DEFAULT NULL,
  `RELATION_NAME` varchar(255) DEFAULT NULL,
  `PROFILE_NAME` varchar(255) DEFAULT NULL,
  `WORD_TYPE` int(2) DEFAULT '1',
  `ENTITY_TYPE` varchar(5) DEFAULT 'C',
  `CONCEPT_BE_ID` bigint(20) DEFAULT NULL,
  `TYPE_BE_ID` bigint(20) DEFAULT NULL,
  `INSTANCE_BE_ID` bigint(20) DEFAULT NULL,
  `RELATION_BE_ID` bigint(20) DEFAULT NULL,
  `PROFILE_BE_ID` bigint(20) DEFAULT NULL,
  `MODEL_GROUP_ID` bigint(20) NOT NULL,
  `POPULARITY` bigint(20) NOT NULL,
  `TYPE_NAME` varchar(255) DEFAULT NULL,
  `KNOWLEDGE_ID` bigint(20) DEFAULT NULL,
  `DETAIL_TYPE_BE_ID` bigint(20) DEFAULT NULL,
  `DETAIL_TYPE_NAME` varchar(255) DEFAULT NULL,
  `DEFAULT_UNIT` varchar(35) DEFAULT NULL,
  `DEFAULT_DATA_FORMAT` varchar(35) DEFAULT NULL,
  `DEFAULT_CONVERSION_TYPE` varchar(35) DEFAULT NULL,
  `ENTITY_BE_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `WORD_IDX` (`WORD`),
  KEY `entity_type_idx` (`ENTITY_TYPE`),
  KEY `RI_ONTO_TERM_KNOWLEDGE_ID_IDX` (`KNOWLEDGE_ID`),
  KEY `IDX_RIO_MGID` (`MODEL_GROUP_ID`),
  KEY `IDX_RIO_IBEID` (`INSTANCE_BE_ID`),
  KEY `IDX_RIO_RBEID` (`RELATION_BE_ID`),
  KEY `IDX_RIO_PBEID` (`PROFILE_BE_ID`),
  KEY `IDX_RIO_TBEID` (`TYPE_BE_ID`),
  KEY `IDX_RIO_DTBI` (`DETAIL_TYPE_BE_ID`),
  KEY `RI_ENTITY_BE_ID_IDX` (`ENTITY_BE_ID`),
  KEY `IDX_RIONTO_CONCEPT_BEDID` (`CONCEPT_BE_ID`)
) ENGINE=MyISAM AUTO_INCREMENT=7011 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ri_onto_term`
--

LOCK TABLES `ri_onto_term` WRITE;
/*!40000 ALTER TABLE `ri_onto_term` DISABLE KEYS */;
INSERT INTO `ri_onto_term` VALUES (101,'year','Year',NULL,NULL,NULL,1,'T',NULL,202,NULL,NULL,NULL,1,0,'Year',NULL,NULL,NULL,NULL,NULL,'NULL',202);
INSERT INTO `ri_onto_term` VALUES (102,'quarter','Quarter',NULL,NULL,NULL,1,'T',NULL,203,NULL,NULL,NULL,1,0,'Quarter',NULL,NULL,NULL,NULL,NULL,'NULL',203);
INSERT INTO `ri_onto_term` VALUES (103,'month','Month',NULL,NULL,NULL,1,'T',NULL,204,NULL,NULL,NULL,1,0,'Month',NULL,NULL,NULL,NULL,NULL,'NULL',204);
INSERT INTO `ri_onto_term` VALUES (104,'week','Week',NULL,NULL,NULL,1,'T',NULL,205,NULL,NULL,NULL,1,0,'Week',NULL,NULL,NULL,NULL,NULL,'NULL',205);
INSERT INTO `ri_onto_term` VALUES (105,'day','Day',NULL,NULL,NULL,1,'T',NULL,206,NULL,NULL,NULL,1,0,'Day',NULL,NULL,NULL,NULL,NULL,'NULL',206);
INSERT INTO `ri_onto_term` VALUES (1001,'summation','Statistics','Statistics1',NULL,NULL,2,'RTLI',NULL,103,1001,NULL,NULL,1,0,'Statistics',NULL,NULL,NULL,NULL,NULL,'NULL',1001);
INSERT INTO `ri_onto_term` VALUES (1002,'average','Statistics','Statistics2',NULL,NULL,2,'RTLI',NULL,103,1002,NULL,NULL,1,0,'Statistics',NULL,NULL,NULL,NULL,NULL,'NULL',1002);
INSERT INTO `ri_onto_term` VALUES (1003,'count','Statistics','Statistics3',NULL,NULL,2,'RTLI',NULL,103,1003,NULL,NULL,1,0,'Statistics',NULL,NULL,NULL,NULL,NULL,'NULL',1003);
INSERT INTO `ri_onto_term` VALUES (1004,'minimum','Statistics','Statistics4',NULL,NULL,2,'RTLI',NULL,103,1004,NULL,NULL,1,0,'Statistics',NULL,NULL,NULL,NULL,NULL,'NULL',1004);
INSERT INTO `ri_onto_term` VALUES (1005,'maximum','Statistics','Statistics5',NULL,NULL,2,'RTLI',NULL,103,1005,NULL,NULL,1,0,'Statistics',NULL,NULL,NULL,NULL,NULL,'NULL',1005);
INSERT INTO `ri_onto_term` VALUES (1006,'standard deviation','Statistics','Statistics6',NULL,NULL,2,'RTLI',NULL,103,1006,NULL,NULL,1,0,'Statistics',NULL,NULL,NULL,NULL,NULL,'NULL',1006);
INSERT INTO `ri_onto_term` VALUES (1051,'equals','Operator','Operator1',NULL,NULL,1,'RTLI',NULL,105,1051,NULL,NULL,1,0,'Operator',NULL,NULL,NULL,NULL,NULL,'NULL',1051);
INSERT INTO `ri_onto_term` VALUES (1052,'less than','Operator','Operator2',NULL,NULL,1,'RTLI',NULL,105,1052,NULL,NULL,1,0,'Operator',NULL,NULL,NULL,NULL,NULL,'NULL',1052);
INSERT INTO `ri_onto_term` VALUES (1053,'greater than','Operator','Operator3',NULL,NULL,1,'RTLI',NULL,105,1053,NULL,NULL,1,0,'Operator',NULL,NULL,NULL,NULL,NULL,'NULL',1053);
INSERT INTO `ri_onto_term` VALUES (1054,'not equals','Operator','Operator4',NULL,NULL,1,'RTLI',NULL,105,1054,NULL,NULL,1,0,'Operator',NULL,NULL,NULL,NULL,NULL,'NULL',1054);
INSERT INTO `ri_onto_term` VALUES (1055,'less than equal to','Operator','Operator5',NULL,NULL,1,'RTLI',NULL,105,1055,NULL,NULL,1,0,'Operator',NULL,NULL,NULL,NULL,NULL,'NULL',1055);
INSERT INTO `ri_onto_term` VALUES (1056,'greater than equal to','Operator','Operator6',NULL,NULL,1,'RTLI',NULL,105,1056,NULL,NULL,1,0,'Operator',NULL,NULL,NULL,NULL,NULL,'NULL',1056);
INSERT INTO `ri_onto_term` VALUES (2001,'hundred','UnitScale','UnitScale1',NULL,NULL,1,'RTLI',NULL,106,2001,NULL,NULL,1,0,'UnitScale',NULL,NULL,NULL,NULL,NULL,'NULL',2001);
INSERT INTO `ri_onto_term` VALUES (2002,'thousand','UnitScale','UnitScale2',NULL,NULL,1,'RTLI',NULL,106,2002,NULL,NULL,1,0,'UnitScale',NULL,NULL,NULL,NULL,NULL,'NULL',2002);
INSERT INTO `ri_onto_term` VALUES (2003,'million','UnitScale','UnitScale3',NULL,NULL,1,'RTLI',NULL,106,2003,NULL,NULL,1,0,'UnitScale',NULL,NULL,NULL,NULL,NULL,'NULL',2003);
INSERT INTO `ri_onto_term` VALUES (2004,'billion','UnitScale','UnitScale4',NULL,NULL,1,'RTLI',NULL,106,2004,NULL,NULL,1,0,'UnitScale',NULL,NULL,NULL,NULL,NULL,'NULL',2004);
INSERT INTO `ri_onto_term` VALUES (2005,'trillion','UnitScale','UnitScale5',NULL,NULL,1,'RTLI',NULL,106,2005,NULL,NULL,1,0,'UnitScale',NULL,NULL,NULL,NULL,NULL,'NULL',2005);
INSERT INTO `ri_onto_term` VALUES (5001,'dollar','CurrencySymbol','CurrencySymbol1',NULL,NULL,1,'CLI',113,107,5001,NULL,NULL,1,0,'UnitSymbol',NULL,NULL,NULL,NULL,NULL,'NULL',5001);
INSERT INTO `ri_onto_term` VALUES (5002,'euro','CurrencySymbol','CurrencySymbol2',NULL,NULL,1,'CLI',113,107,5002,NULL,NULL,1,0,'UnitSymbol',NULL,NULL,NULL,NULL,NULL,'NULL',5002);
INSERT INTO `ri_onto_term` VALUES (1101,'of','Conjunction','Conjunction1',NULL,NULL,1,'RTLI',NULL,404,1101,NULL,NULL,1,0,'Conjunction',NULL,NULL,NULL,NULL,NULL,'NULL',1101);
INSERT INTO `ri_onto_term` VALUES (1102,'on','Conjunction','Conjunction2',NULL,NULL,1,'RTLI',NULL,404,1102,NULL,NULL,1,0,'Conjunction',NULL,NULL,NULL,NULL,NULL,'NULL',1102);
INSERT INTO `ri_onto_term` VALUES (1103,'at','Conjunction','Conjunction3',NULL,NULL,1,'RTLI',NULL,404,1103,NULL,NULL,1,0,'Conjunction',NULL,NULL,NULL,NULL,NULL,'NULL',1103);
INSERT INTO `ri_onto_term` VALUES (1104,'with','Conjunction','Conjunction4',NULL,NULL,1,'RTLI',NULL,404,1104,NULL,NULL,1,0,'Conjunction',NULL,NULL,NULL,NULL,NULL,'NULL',1104);
INSERT INTO `ri_onto_term` VALUES (1105,'from','Conjunction','Conjunction5',NULL,NULL,1,'RTLI',NULL,404,1105,NULL,NULL,1,0,'Conjunction',NULL,NULL,NULL,NULL,NULL,'NULL',1105);
INSERT INTO `ri_onto_term` VALUES (1106,'for','Conjunction','Conjunction6',NULL,NULL,1,'RTLI',NULL,404,1106,NULL,NULL,1,0,'Conjunction',NULL,NULL,NULL,NULL,NULL,'NULL',1106);
INSERT INTO `ri_onto_term` VALUES (1551,'evening',NULL,'Evening',NULL,NULL,1,'TLI',NULL,211,1533,NULL,NULL,1,0,'Time',NULL,NULL,NULL,NULL,NULL,'NULL',1533);
INSERT INTO `ri_onto_term` VALUES (1108,'though','Conjunction','Conjunction8',NULL,NULL,1,'RTLI',NULL,404,1108,NULL,NULL,1,0,'Conjunction',NULL,NULL,NULL,NULL,NULL,'NULL',1108);
INSERT INTO `ri_onto_term` VALUES (1550,'afternoon',NULL,'Afternoon',NULL,NULL,1,'TLI',NULL,211,1532,NULL,NULL,1,0,'Time',NULL,NULL,NULL,NULL,NULL,'NULL',1532);
INSERT INTO `ri_onto_term` VALUES (1110,'until','Conjunction','Conjunction10',NULL,NULL,1,'RTLI',NULL,404,1110,NULL,NULL,1,0,'Conjunction',NULL,NULL,NULL,NULL,NULL,'NULL',1110);
INSERT INTO `ri_onto_term` VALUES (1111,'or','Conjunction','Conjunction11',NULL,NULL,1,'RTLI',NULL,404,1111,NULL,NULL,1,0,'Conjunction',NULL,NULL,NULL,NULL,NULL,'NULL',1111);
INSERT INTO `ri_onto_term` VALUES (1112,'unless','Conjunction','Conjunction12',NULL,NULL,1,'RTLI',NULL,404,1112,NULL,NULL,1,0,'Conjunction',NULL,NULL,NULL,NULL,NULL,'NULL',1112);
INSERT INTO `ri_onto_term` VALUES (1549,'morning',NULL,'Morning',NULL,NULL,1,'TLI',NULL,211,1531,NULL,NULL,1,0,'Time',NULL,NULL,NULL,NULL,NULL,'NULL',1531);
INSERT INTO `ri_onto_term` VALUES (1114,'in','Conjunction','Conjunction14',NULL,NULL,1,'RTLI',NULL,404,1114,NULL,NULL,1,0,'Conjunction',NULL,NULL,NULL,NULL,NULL,'NULL',1114);
INSERT INTO `ri_onto_term` VALUES (1115,'based','Conjunction','Conjunction15',NULL,NULL,1,'RTLI',NULL,404,1115,NULL,NULL,1,0,'Conjunction',NULL,NULL,NULL,NULL,NULL,'NULL',1115);
INSERT INTO `ri_onto_term` VALUES (1116,'by','ByConjunction','By1',NULL,NULL,1,'RTLI',NULL,406,1116,NULL,NULL,1,0,'ByConjunction',NULL,NULL,NULL,NULL,NULL,'NULL',1116);
INSERT INTO `ri_onto_term` VALUES (1117,'and','CoordinatingConjunction','CoordinatingConjunction1',NULL,NULL,1,'RTLI',NULL,408,1117,NULL,NULL,1,0,'CoordinatingConjunction',NULL,NULL,NULL,NULL,NULL,'NULL',1117);
INSERT INTO `ri_onto_term` VALUES (1118,'to','Preposition','Preposition1',NULL,NULL,1,'RTLI',NULL,405,1118,NULL,NULL,1,0,'Preposition',NULL,NULL,NULL,NULL,NULL,'NULL',1118);
INSERT INTO `ri_onto_term` VALUES (1119,'through','Preposition','Preposition2',NULL,NULL,1,'RTLI',NULL,405,1119,NULL,NULL,1,0,'Preposition',NULL,NULL,NULL,NULL,NULL,'NULL',1119);
INSERT INTO `ri_onto_term` VALUES (1200,'last','Adjective','Adjective1',NULL,NULL,1,'RTLI',NULL,403,1200,NULL,NULL,1,0,'Adjective',NULL,NULL,NULL,NULL,NULL,'NULL',1200);
INSERT INTO `ri_onto_term` VALUES (1501,'january',NULL,'January',NULL,NULL,1,'TLI',NULL,204,1501,NULL,NULL,1,0,'Month',NULL,NULL,NULL,NULL,NULL,'NULL',1501);
INSERT INTO `ri_onto_term` VALUES (1502,'february',NULL,'February',NULL,NULL,1,'TLI',NULL,204,1502,NULL,NULL,1,0,'Month',NULL,NULL,NULL,NULL,NULL,'NULL',1502);
INSERT INTO `ri_onto_term` VALUES (1503,'march',NULL,'March',NULL,NULL,1,'TLI',NULL,204,1503,NULL,NULL,1,0,'Month',NULL,NULL,NULL,NULL,NULL,'NULL',1503);
INSERT INTO `ri_onto_term` VALUES (1504,'april',NULL,'April',NULL,NULL,1,'TLI',NULL,204,1504,NULL,NULL,1,0,'Month',NULL,NULL,NULL,NULL,NULL,'NULL',1504);
INSERT INTO `ri_onto_term` VALUES (1505,'may',NULL,'May',NULL,NULL,1,'TLI',NULL,204,1505,NULL,NULL,1,0,'Month',NULL,NULL,NULL,NULL,NULL,'NULL',1505);
INSERT INTO `ri_onto_term` VALUES (1506,'june',NULL,'June',NULL,NULL,1,'TLI',NULL,204,1506,NULL,NULL,1,0,'Month',NULL,NULL,NULL,NULL,NULL,'NULL',1506);
INSERT INTO `ri_onto_term` VALUES (1507,'july',NULL,'July',NULL,NULL,1,'TLI',NULL,204,1507,NULL,NULL,1,0,'Month',NULL,NULL,NULL,NULL,NULL,'NULL',1507);
INSERT INTO `ri_onto_term` VALUES (1508,'august',NULL,'August',NULL,NULL,1,'TLI',NULL,204,1508,NULL,NULL,1,0,'Month',NULL,NULL,NULL,NULL,NULL,'NULL',1508);
INSERT INTO `ri_onto_term` VALUES (1509,'september',NULL,'September',NULL,NULL,1,'TLI',NULL,204,1509,NULL,NULL,1,0,'Month',NULL,NULL,NULL,NULL,NULL,'NULL',1509);
INSERT INTO `ri_onto_term` VALUES (1510,'october',NULL,'October',NULL,NULL,1,'TLI',NULL,204,1510,NULL,NULL,1,0,'Month',NULL,NULL,NULL,NULL,NULL,'NULL',1510);
INSERT INTO `ri_onto_term` VALUES (1511,'november',NULL,'November',NULL,NULL,1,'TLI',NULL,204,1511,NULL,NULL,1,0,'Month',NULL,NULL,NULL,NULL,NULL,'NULL',1511);
INSERT INTO `ri_onto_term` VALUES (1512,'december',NULL,'December',NULL,NULL,1,'TLI',NULL,204,1512,NULL,NULL,1,0,'Month',NULL,NULL,NULL,NULL,NULL,'NULL',1512);
INSERT INTO `ri_onto_term` VALUES (1513,'jan',NULL,'January',NULL,NULL,2,'TLI',NULL,204,1501,NULL,NULL,1,0,'Month',NULL,NULL,NULL,NULL,NULL,'NULL',1501);
INSERT INTO `ri_onto_term` VALUES (1514,'feb',NULL,'February',NULL,NULL,2,'TLI',NULL,204,1502,NULL,NULL,1,0,'Month',NULL,NULL,NULL,NULL,NULL,'NULL',1502);
INSERT INTO `ri_onto_term` VALUES (1515,'mar',NULL,'March',NULL,NULL,2,'TLI',NULL,204,1503,NULL,NULL,1,0,'Month',NULL,NULL,NULL,NULL,NULL,'NULL',1503);
INSERT INTO `ri_onto_term` VALUES (1516,'apr',NULL,'April',NULL,NULL,2,'TLI',NULL,204,1504,NULL,NULL,1,0,'Month',NULL,NULL,NULL,NULL,NULL,'NULL',1504);
INSERT INTO `ri_onto_term` VALUES (1518,'jun',NULL,'June',NULL,NULL,2,'TLI',NULL,204,1506,NULL,NULL,1,0,'Month',NULL,NULL,NULL,NULL,NULL,'NULL',1506);
INSERT INTO `ri_onto_term` VALUES (1519,'jul',NULL,'July',NULL,NULL,2,'TLI',NULL,204,1507,NULL,NULL,1,0,'Month',NULL,NULL,NULL,NULL,NULL,'NULL',1507);
INSERT INTO `ri_onto_term` VALUES (1520,'aug',NULL,'August',NULL,NULL,2,'TLI',NULL,204,1508,NULL,NULL,1,0,'Month',NULL,NULL,NULL,NULL,NULL,'NULL',1508);
INSERT INTO `ri_onto_term` VALUES (1521,'sep',NULL,'September',NULL,NULL,2,'TLI',NULL,204,1509,NULL,NULL,1,0,'Month',NULL,NULL,NULL,NULL,NULL,'NULL',1509);
INSERT INTO `ri_onto_term` VALUES (1522,'oct',NULL,'October',NULL,NULL,2,'TLI',NULL,204,1510,NULL,NULL,1,0,'Month',NULL,NULL,NULL,NULL,NULL,'NULL',1510);
INSERT INTO `ri_onto_term` VALUES (1523,'nov',NULL,'November',NULL,NULL,2,'TLI',NULL,204,1511,NULL,NULL,1,0,'Month',NULL,NULL,NULL,NULL,NULL,'NULL',1511);
INSERT INTO `ri_onto_term` VALUES (1524,'dec',NULL,'December',NULL,NULL,2,'TLI',NULL,204,1512,NULL,NULL,1,0,'Month',NULL,NULL,NULL,NULL,NULL,'NULL',1512);
INSERT INTO `ri_onto_term` VALUES (1057,'=','Operator','Operator1',NULL,NULL,1,'RTLI',NULL,105,1051,NULL,NULL,1,0,'Operator',NULL,NULL,NULL,NULL,NULL,'NULL',1051);
INSERT INTO `ri_onto_term` VALUES (1058,'<','Operator','Operator2',NULL,NULL,1,'RTLI',NULL,105,1052,NULL,NULL,1,0,'Operator',NULL,NULL,NULL,NULL,NULL,'NULL',1052);
INSERT INTO `ri_onto_term` VALUES (1059,'>','Operator','Operator3',NULL,NULL,1,'RTLI',NULL,105,1053,NULL,NULL,1,0,'Operator',NULL,NULL,NULL,NULL,NULL,'NULL',1053);
INSERT INTO `ri_onto_term` VALUES (1060,'!=','Operator','Operator4',NULL,NULL,1,'RTLI',NULL,105,1054,NULL,NULL,1,0,'Operator',NULL,NULL,NULL,NULL,NULL,'NULL',1054);
INSERT INTO `ri_onto_term` VALUES (1061,'<=','Operator','Operator5',NULL,NULL,1,'RTLI',NULL,105,1055,NULL,NULL,1,0,'Operator',NULL,NULL,NULL,NULL,NULL,'NULL',1055);
INSERT INTO `ri_onto_term` VALUES (1062,'>=','Operator','Operator6',NULL,NULL,1,'RTLI',NULL,105,1056,NULL,NULL,1,0,'Operator',NULL,NULL,NULL,NULL,NULL,'NULL',1056);
INSERT INTO `ri_onto_term` VALUES (109,'power','Power',NULL,NULL,NULL,1,'C',156,153,NULL,NULL,NULL,1,0,'Value',NULL,NULL,NULL,NULL,NULL,'NULL',156);
INSERT INTO `ri_onto_term` VALUES (108,'volume','Volume',NULL,NULL,NULL,1,'C',155,153,NULL,NULL,NULL,1,0,'Value',NULL,NULL,NULL,NULL,NULL,'NULL',155);
INSERT INTO `ri_onto_term` VALUES (107,'distance','Distance',NULL,NULL,NULL,1,'C',154,153,NULL,NULL,NULL,1,0,'Value',NULL,NULL,NULL,NULL,NULL,'NULL',154);
INSERT INTO `ri_onto_term` VALUES (5201,'kg','WeightSymbol','WeightSymbol1',NULL,NULL,1,'CLI',115,107,5201,NULL,NULL,1,0,'UnitSymbol',NULL,NULL,NULL,NULL,NULL,'NULL',5201);
INSERT INTO `ri_onto_term` VALUES (5202,'lbs','WeightSymbol','WeightSymbol2',NULL,NULL,1,'CLI',115,107,5202,NULL,NULL,1,0,'UnitSymbol',NULL,NULL,NULL,NULL,NULL,'NULL',5202);
INSERT INTO `ri_onto_term` VALUES (5301,'mtr','DistanceSymbol','DistanceSymbol1',NULL,NULL,1,'CLI',114,107,5301,NULL,NULL,1,0,'UnitSymbol',NULL,NULL,NULL,NULL,NULL,'NULL',5301);
INSERT INTO `ri_onto_term` VALUES (5302,'inch','DistanceSymbol','DistanceSymbol2',NULL,NULL,1,'CLI',114,107,5302,NULL,NULL,1,0,'UnitSymbol',NULL,NULL,NULL,NULL,NULL,'NULL',5302);
INSERT INTO `ri_onto_term` VALUES (1120,'between','RangePreposition','RangePreposition1',NULL,NULL,1,'RTLI',NULL,409,1120,NULL,NULL,1,0,'RangePreposition',NULL,NULL,NULL,NULL,NULL,'NULL',1120);
INSERT INTO `ri_onto_term` VALUES (1007,'top','ComparativeStatistics','ComparativeStatistics1',NULL,NULL,2,'RTLI',NULL,104,1007,NULL,NULL,1,0,'ComparativeStatistics',NULL,NULL,NULL,NULL,NULL,'NULL',1007);
INSERT INTO `ri_onto_term` VALUES (1008,'bottom','ComparativeStatistics','ComparativeStatistics2',NULL,NULL,2,'RTLI',NULL,104,1008,NULL,NULL,1,0,'ComparativeStatistics',NULL,NULL,NULL,NULL,NULL,'NULL',1008);
INSERT INTO `ri_onto_term` VALUES (1069,'((\\d*)?(\\.)?(\\d+)(%)?)','Digit','Number1',NULL,NULL,2,'RI',NULL,401,6001,NULL,NULL,1,0,'Digit',NULL,NULL,NULL,NULL,NULL,'NULL',6001);
INSERT INTO `ri_onto_term` VALUES (1070,'sum','Statistics','Statistics1',NULL,NULL,2,'RTLI',NULL,103,1001,NULL,NULL,1,0,'Statistics',NULL,NULL,NULL,NULL,NULL,'NULL',1001);
INSERT INTO `ri_onto_term` VALUES (7069,'avg','Statistics','Statistics2',NULL,NULL,11,'RTLI',NULL,103,1002,NULL,NULL,1,0,'Statistics',NULL,NULL,NULL,NULL,NULL,'NULL',1002);
INSERT INTO `ri_onto_term` VALUES (7070,'cnt','Statistics','Statistics3',NULL,NULL,11,'RTLI',NULL,103,1003,NULL,NULL,1,0,'Statistics',NULL,NULL,NULL,NULL,NULL,'NULL',1003);
INSERT INTO `ri_onto_term` VALUES (1073,'min','Statistics','Statistics4',NULL,NULL,2,'RTLI',NULL,103,1004,NULL,NULL,1,0,'Statistics',NULL,NULL,NULL,NULL,NULL,'NULL',1004);
INSERT INTO `ri_onto_term` VALUES (1074,'max','Statistics','Statistics5',NULL,NULL,2,'RTLI',NULL,103,1005,NULL,NULL,1,0,'Statistics',NULL,NULL,NULL,NULL,NULL,'NULL',1005);
INSERT INTO `ri_onto_term` VALUES (1075,'sd','Statistics','Statistics6',NULL,NULL,2,'RTLI',NULL,103,1006,NULL,NULL,1,0,'Statistics',NULL,NULL,NULL,NULL,NULL,'NULL',1006);
INSERT INTO `ri_onto_term` VALUES (106,'currency','Currency',NULL,NULL,NULL,1,'C',152,153,NULL,NULL,NULL,1,0,'Value',NULL,NULL,NULL,NULL,NULL,'NULL',152);
INSERT INTO `ri_onto_term` VALUES (1201,'first','Adjective','Adjective2',NULL,NULL,1,'RTLI',NULL,403,1201,NULL,NULL,1,0,'Adjective',NULL,NULL,NULL,NULL,NULL,'NULL',1201);
INSERT INTO `ri_onto_term` VALUES (1121,'over','ValuePreposition','ValuePreposition1',NULL,NULL,1,'RTLI',NULL,410,1121,NULL,NULL,1,0,'ValuePreposition',NULL,NULL,NULL,NULL,NULL,'NULL',1121);
INSERT INTO `ri_onto_term` VALUES (1122,'under','ValuePreposition','ValuePreposition2',NULL,NULL,1,'RTLI',NULL,410,1122,NULL,NULL,1,0,'ValuePreposition',NULL,NULL,NULL,NULL,NULL,'NULL',1122);
INSERT INTO `ri_onto_term` VALUES (1123,'before','TimePreposition','TimePreposition1',NULL,NULL,1,'RTLI',NULL,413,1123,NULL,NULL,1,0,'TimePreposition',NULL,NULL,NULL,NULL,NULL,'NULL',1123);
INSERT INTO `ri_onto_term` VALUES (1124,'after','TimePreposition','TimePreposition2',NULL,NULL,1,'RTLI',NULL,413,1124,NULL,NULL,1,0,'TimePreposition',NULL,NULL,NULL,NULL,NULL,'NULL',1124);
INSERT INTO `ri_onto_term` VALUES (1125,'since','TimePreposition','TimePreposition3',NULL,NULL,1,'RTLI',NULL,413,1125,NULL,NULL,1,0,'TimePreposition',NULL,NULL,NULL,NULL,NULL,'NULL',1125);
INSERT INTO `ri_onto_term` VALUES (1126,'till','TimePreposition','TimePreposition4',NULL,NULL,1,'RTLI',NULL,413,1126,NULL,NULL,1,0,'TimePreposition',NULL,NULL,NULL,NULL,NULL,'NULL',1126);
INSERT INTO `ri_onto_term` VALUES (1526,'(^(([0]?1)|21|31)(st)?$)|(^(([0]?2)|22)(nd)?$)|(^(([0]?3)|23)(rd)?$)|(^(((0)?[4-9])|([1][0-9])|(20)|((2[4-9])|(30)))(th)?$)','Day','day',NULL,NULL,2,'RI',NULL,206,1513,NULL,NULL,1,0,'Day',NULL,NULL,NULL,NULL,NULL,'NULL',1513);
INSERT INTO `ri_onto_term` VALUES (1150,',',NULL,'Punctuation1',NULL,NULL,1,'TLI',NULL,411,1150,NULL,NULL,1,1,'Punctuation',NULL,NULL,NULL,NULL,NULL,'NULL',1150);
INSERT INTO `ri_onto_term` VALUES (1151,';',NULL,'Punctuation2',NULL,NULL,1,'TLI',NULL,411,1151,NULL,NULL,1,1,'Punctuation',NULL,NULL,NULL,NULL,NULL,'NULL',1151);
INSERT INTO `ri_onto_term` VALUES (5303,'kilometer','DistanceSymbol','DistanceSymbol3',NULL,NULL,1,'CLI',114,107,5303,NULL,NULL,1,0,'UnitSymbol',NULL,NULL,NULL,NULL,NULL,'NULL',5303);
INSERT INTO `ri_onto_term` VALUES (5304,'miles','DistanceSymbol','DistanceSymbol4',NULL,NULL,1,'CLI',114,107,5304,NULL,NULL,1,0,'UnitSymbol',NULL,NULL,NULL,NULL,NULL,'NULL',5304);
INSERT INTO `ri_onto_term` VALUES (5451,'hp','PowerSymbol','PowerSymbol1',NULL,NULL,1,'CLI',117,107,5451,NULL,NULL,1,0,'UnitSymbol',NULL,NULL,NULL,NULL,NULL,'NULL',5451);
INSERT INTO `ri_onto_term` VALUES (5461,'cc','VolumeSymbol','VolumeSymbol1',NULL,NULL,1,'CLI',116,107,5461,NULL,NULL,1,0,'UnitSymbol',NULL,NULL,NULL,NULL,NULL,'NULL',5461);
INSERT INTO `ri_onto_term` VALUES (1528,'^(([0-1]?[0-9])|([2][0-3])):([0-5]?[0-9])(:([0-5]?[0-9]))?$','Time','Time',NULL,NULL,2,'RI',211,211,1515,NULL,NULL,1,0,'Time',NULL,NULL,NULL,NULL,NULL,'NULL',1515);
INSERT INTO `ri_onto_term` VALUES (1152,'am','TimeQualifier','AM',NULL,NULL,1,'RTLI',NULL,412,1152,NULL,NULL,1,0,'TimeQualifier',NULL,NULL,NULL,NULL,NULL,'NULL',1152);
INSERT INTO `ri_onto_term` VALUES (1153,'pm','TimeQualifier','PM',NULL,NULL,1,'RTLI',NULL,412,1153,NULL,NULL,1,0,'TimeQualifier',NULL,NULL,NULL,NULL,NULL,'NULL',1153);
INSERT INTO `ri_onto_term` VALUES (7001,'(=|<|>|>=|<=)?\\s*([$])((\\d*)?(\\.)?(\\d+))([h,h,k,k,m,m,b,b,t,t]?)|(=|<|>|>=|<=)?\\s*((\\d*)?(\\.)?(\\d+))([h,h,k,k,m,m,b,b,t,t]?)([$])','Currency','Currency1',NULL,NULL,2,'RI',152,153,7001,NULL,NULL,1,0,'Value',NULL,NULL,NULL,NULL,NULL,'NULL',7001);
INSERT INTO `ri_onto_term` VALUES (1076,'more than','Operator','Operator3',NULL,NULL,1,'RTLI',NULL,105,1053,NULL,NULL,1,0,'Operator',NULL,NULL,NULL,NULL,NULL,'NULL',1053);
INSERT INTO `ri_onto_term` VALUES (111,'weight','Weight',NULL,NULL,NULL,1,'C',160,153,NULL,NULL,NULL,1,0,'Value',NULL,NULL,NULL,NULL,NULL,'NULL',160);
INSERT INTO `ri_onto_term` VALUES (1009,'best','ComparativeStatistics','ComparativeStatistics1',NULL,NULL,2,'RTLI',NULL,104,1007,NULL,NULL,1,0,'ComparativeStatistics',NULL,NULL,NULL,NULL,NULL,'NULL',1007);
INSERT INTO `ri_onto_term` VALUES (1010,'highest','ComparativeStatistics','ComparativeStatistics1',NULL,NULL,2,'RTLI',NULL,104,1007,NULL,NULL,1,0,'ComparativeStatistics',NULL,NULL,NULL,NULL,NULL,'NULL',1007);
INSERT INTO `ri_onto_term` VALUES (1011,'worst','ComparativeStatistics','ComparativeStatistics2',NULL,NULL,2,'RTLI',NULL,104,1008,NULL,NULL,1,0,'ComparativeStatistics',NULL,NULL,NULL,NULL,NULL,'NULL',1008);
INSERT INTO `ri_onto_term` VALUES (1012,'lowest','ComparativeStatistics','ComparativeStatistics2',NULL,NULL,2,'RTLI',NULL,104,1008,NULL,NULL,1,0,'ComparativeStatistics',NULL,NULL,NULL,NULL,NULL,'NULL',1008);
INSERT INTO `ri_onto_term` VALUES (5305,'foot','DistanceSymbol','DistanceSymbol5',NULL,NULL,1,'CLI',114,107,5305,NULL,NULL,1,0,'UnitSymbol',NULL,NULL,NULL,NULL,NULL,'NULL',5305);
INSERT INTO `ri_onto_term` VALUES (5306,'yard','DistanceSymbol','DistanceSymbol6',NULL,NULL,1,'CLI',114,107,5306,NULL,NULL,1,0,'UnitSymbol',NULL,NULL,NULL,NULL,NULL,'NULL',5306);
INSERT INTO `ri_onto_term` VALUES (5452,'watt','PowerSymbol','PowerSymbol2',NULL,NULL,1,'CLI',117,107,5452,NULL,NULL,1,0,'UnitSymbol',NULL,NULL,NULL,NULL,NULL,'NULL',5452);
INSERT INTO `ri_onto_term` VALUES (5453,'kilowatt','PowerSymbol','PowerSymbol3',NULL,NULL,1,'CLI',117,107,5452,NULL,NULL,1,0,'UnitSymbol',NULL,NULL,NULL,NULL,NULL,'NULL',5452);
INSERT INTO `ri_onto_term` VALUES (5462,'cubicmeter','VolumeSymbol','VolumeSymbol2',NULL,NULL,1,'CLI',116,107,5462,NULL,NULL,1,0,'UnitSymbol',NULL,NULL,NULL,NULL,NULL,'NULL',5462);
INSERT INTO `ri_onto_term` VALUES (5463,'liter','VolumeSymbol','VolumeSymbol3',NULL,NULL,1,'CLI',116,107,5462,NULL,NULL,1,0,'UnitSymbol',NULL,NULL,NULL,NULL,NULL,'NULL',5462);
INSERT INTO `ri_onto_term` VALUES (5203,'kilo','WeightSymbol','WeightSymbol1',NULL,NULL,1,'CLI',115,107,5201,NULL,NULL,1,0,'UnitSymbol',NULL,NULL,NULL,NULL,NULL,'NULL',5201);
INSERT INTO `ri_onto_term` VALUES (5204,'pound','WeightSymbol','WeightSymbol2',NULL,NULL,1,'CLI',115,107,5202,NULL,NULL,1,0,'UnitSymbol',NULL,NULL,NULL,NULL,NULL,'NULL',5202);
INSERT INTO `ri_onto_term` VALUES (2006,'h','UnitScale','UnitScale1',NULL,NULL,1,'RTLI',NULL,106,2001,NULL,NULL,1,0,'UnitScale',NULL,NULL,NULL,NULL,NULL,'NULL',2001);
INSERT INTO `ri_onto_term` VALUES (2007,'k','UnitScale','UnitScale2',NULL,NULL,1,'RTLI',NULL,106,2002,NULL,NULL,1,0,'UnitScale',NULL,NULL,NULL,NULL,NULL,'NULL',2002);
INSERT INTO `ri_onto_term` VALUES (2008,'m','UnitScale','UnitScale3',NULL,NULL,1,'RTLI',NULL,106,2003,NULL,NULL,1,0,'UnitScale',NULL,NULL,NULL,NULL,NULL,'NULL',2003);
INSERT INTO `ri_onto_term` VALUES (2009,'b','UnitScale','UnitScale4',NULL,NULL,1,'RTLI',NULL,106,2004,NULL,NULL,1,0,'UnitScale',NULL,NULL,NULL,NULL,NULL,'NULL',2004);
INSERT INTO `ri_onto_term` VALUES (2010,'t','UnitScale','UnitScale5',NULL,NULL,1,'RTLI',NULL,106,2005,NULL,NULL,1,0,'UnitScale',NULL,NULL,NULL,NULL,NULL,'NULL',2005);
INSERT INTO `ri_onto_term` VALUES (5003,'$','CurrencySymbol','CurrencySymbol1',NULL,NULL,1,'CLI',113,107,5001,NULL,NULL,1,0,'UnitSymbol',NULL,NULL,NULL,NULL,NULL,'NULL',5001);
INSERT INTO `ri_onto_term` VALUES (112,'timeduration','TimeDuration',NULL,NULL,NULL,1,'C',161,153,NULL,NULL,NULL,1,0,'Value',NULL,NULL,NULL,NULL,NULL,'NULL',161);
INSERT INTO `ri_onto_term` VALUES (1127,'below','ValuePreposition','ValuePreposition3',NULL,NULL,1,'RTLI',NULL,410,1127,NULL,NULL,1,0,'ValuePreposition',NULL,NULL,NULL,NULL,NULL,'NULL',1127);
INSERT INTO `ri_onto_term` VALUES (1128,'above','ValuePreposition','ValuePreposition4',NULL,NULL,1,'RTLI',NULL,410,1128,NULL,NULL,1,0,'ValuePreposition',NULL,NULL,NULL,NULL,NULL,'NULL',1128);
INSERT INTO `ri_onto_term` VALUES (113,'hour',NULL,NULL,NULL,NULL,1,'T',NULL,207,NULL,NULL,NULL,1,0,'Hour',NULL,NULL,NULL,NULL,NULL,'NULL',207);
INSERT INTO `ri_onto_term` VALUES (114,'minute',NULL,NULL,NULL,NULL,1,'T',NULL,208,NULL,NULL,NULL,1,0,'Minute',NULL,NULL,NULL,NULL,NULL,'NULL',208);
INSERT INTO `ri_onto_term` VALUES (115,'second',NULL,NULL,NULL,NULL,1,'T',NULL,209,NULL,NULL,NULL,1,0,'Second',NULL,NULL,NULL,NULL,NULL,'NULL',209);
INSERT INTO `ri_onto_term` VALUES (1202,'next','Adjective','Adjective3',NULL,NULL,1,'RTLI',NULL,403,1202,NULL,NULL,1,0,'Adjective',NULL,NULL,NULL,NULL,NULL,'NULL',1202);
INSERT INTO `ri_onto_term` VALUES (1529,'today',NULL,'Today',NULL,NULL,1,'RTLI',NULL,206,1516,NULL,NULL,1,0,'day',NULL,NULL,NULL,NULL,NULL,'NULL',1516);
INSERT INTO `ri_onto_term` VALUES (1530,'tomorrow',NULL,'Tomorrow',NULL,NULL,1,'RTLI',NULL,206,1517,NULL,NULL,1,0,'day',NULL,NULL,NULL,NULL,NULL,'NULL',1517);
INSERT INTO `ri_onto_term` VALUES (1531,'yesterday',NULL,'Yesterday',NULL,NULL,1,'RTLI',NULL,206,1518,NULL,NULL,1,0,'day',NULL,NULL,NULL,NULL,NULL,'NULL',1518);
INSERT INTO `ri_onto_term` VALUES (1532,'day after tomorrow',NULL,'DayAfterTomorrow',NULL,NULL,1,'RTLI',NULL,206,1519,NULL,NULL,1,0,'day',NULL,NULL,NULL,NULL,NULL,'NULL',1519);
INSERT INTO `ri_onto_term` VALUES (1533,'day before yesterday',NULL,'DayBeforeYesterday',NULL,NULL,1,'RTLI',NULL,206,1520,NULL,NULL,1,0,'day',NULL,NULL,NULL,NULL,NULL,'NULL',1520);
INSERT INTO `ri_onto_term` VALUES (1534,'sunday',NULL,'Sunday',NULL,NULL,1,'TLI',NULL,213,1521,NULL,NULL,1,0,'Weekday',NULL,NULL,NULL,NULL,NULL,'NULL',1521);
INSERT INTO `ri_onto_term` VALUES (1535,'sun',NULL,'Sunday',NULL,NULL,2,'TLI',NULL,213,1521,NULL,NULL,1,0,'Weekday',NULL,NULL,NULL,NULL,NULL,'NULL',1521);
INSERT INTO `ri_onto_term` VALUES (1536,'monday',NULL,'Monday',NULL,NULL,1,'TLI',NULL,213,1522,NULL,NULL,1,0,'Weekday',NULL,NULL,NULL,NULL,NULL,'NULL',1522);
INSERT INTO `ri_onto_term` VALUES (1537,'mon',NULL,'Monday',NULL,NULL,2,'TLI',NULL,213,1522,NULL,NULL,1,0,'Weekday',NULL,NULL,NULL,NULL,NULL,'NULL',1522);
INSERT INTO `ri_onto_term` VALUES (1538,'tuesday',NULL,'Tuesday',NULL,NULL,1,'TLI',NULL,213,1523,NULL,NULL,1,0,'Weekday',NULL,NULL,NULL,NULL,NULL,'NULL',1523);
INSERT INTO `ri_onto_term` VALUES (1539,'tue',NULL,'Tuesday',NULL,NULL,2,'TLI',NULL,213,1523,NULL,NULL,1,0,'Weekday',NULL,NULL,NULL,NULL,NULL,'NULL',1523);
INSERT INTO `ri_onto_term` VALUES (1540,'wednesday',NULL,'Wednesday',NULL,NULL,1,'TLI',NULL,213,1524,NULL,NULL,1,0,'Weekday',NULL,NULL,NULL,NULL,NULL,'NULL',1524);
INSERT INTO `ri_onto_term` VALUES (1541,'wed',NULL,'Wednesday',NULL,NULL,2,'TLI',NULL,213,1524,NULL,NULL,1,0,'Weekday',NULL,NULL,NULL,NULL,NULL,'NULL',1524);
INSERT INTO `ri_onto_term` VALUES (1542,'thursday',NULL,'Thursday',NULL,NULL,1,'TLI',NULL,213,1525,NULL,NULL,1,0,'Weekday',NULL,NULL,NULL,NULL,NULL,'NULL',1525);
INSERT INTO `ri_onto_term` VALUES (1543,'thu',NULL,'Thursday',NULL,NULL,2,'TLI',NULL,213,1525,NULL,NULL,1,0,'Weekday',NULL,NULL,NULL,NULL,NULL,'NULL',1525);
INSERT INTO `ri_onto_term` VALUES (1544,'friday',NULL,'Friday',NULL,NULL,1,'TLI',NULL,213,1526,NULL,NULL,1,0,'Weekday',NULL,NULL,NULL,NULL,NULL,'NULL',1526);
INSERT INTO `ri_onto_term` VALUES (1545,'fri',NULL,'Friday',NULL,NULL,2,'TLI',NULL,213,1526,NULL,NULL,1,0,'Weekday',NULL,NULL,NULL,NULL,NULL,'NULL',1526);
INSERT INTO `ri_onto_term` VALUES (1546,'saturday',NULL,'Saturday',NULL,NULL,1,'TLI',NULL,213,1527,NULL,NULL,1,0,'Weekday',NULL,NULL,NULL,NULL,NULL,'NULL',1527);
INSERT INTO `ri_onto_term` VALUES (1547,'sat',NULL,'Saturday',NULL,NULL,2,'TLI',NULL,213,1527,NULL,NULL,1,0,'Weekday',NULL,NULL,NULL,NULL,NULL,'NULL',1527);
INSERT INTO `ri_onto_term` VALUES (1548,'weekend',NULL,'Weekend',NULL,NULL,1,'TLI',NULL,213,1528,NULL,NULL,1,0,'Weekday',NULL,NULL,NULL,NULL,NULL,'NULL',1528);
INSERT INTO `ri_onto_term` VALUES (1552,'night',NULL,'Night',NULL,NULL,1,'TLI',NULL,211,1534,NULL,NULL,1,0,'Time',NULL,NULL,NULL,NULL,NULL,'NULL',1534);
INSERT INTO `ri_onto_term` VALUES (7002,'hasstatistics',NULL,NULL,'hasStatistics',NULL,1,'R',NULL,108,NULL,501,NULL,1,0,'OntoEntity',NULL,NULL,NULL,NULL,NULL,'NULL',501);
INSERT INTO `ri_onto_term` VALUES (7003,'hasvalue',NULL,NULL,'hasValue',NULL,1,'R',NULL,108,NULL,502,NULL,1,0,'OntoEntity',NULL,NULL,NULL,NULL,NULL,'NULL',502);
INSERT INTO `ri_onto_term` VALUES (7004,'hastimeframe',NULL,NULL,'hasTimeframe',NULL,1,'R',NULL,108,NULL,503,NULL,1,0,'OntoEntity',NULL,NULL,NULL,NULL,NULL,'NULL',503);
INSERT INTO `ri_onto_term` VALUES (7005,'haslocation',NULL,NULL,'hasLocation',NULL,1,'R',NULL,108,NULL,504,NULL,1,0,'OntoEntity',NULL,NULL,NULL,NULL,NULL,'NULL',504);
INSERT INTO `ri_onto_term` VALUES (7006,'hasperiodicinformation',NULL,NULL,'hasPeriodicInformation',NULL,1,'R',NULL,108,NULL,505,NULL,1,0,'OntoEntity',NULL,NULL,NULL,NULL,NULL,'NULL',505);
INSERT INTO `ri_onto_term` VALUES (7007,'parentresource',NULL,NULL,'parentResource',NULL,1,'R',NULL,108,NULL,506,NULL,1,0,'OntoEntity',NULL,NULL,NULL,NULL,NULL,'NULL',506);
INSERT INTO `ri_onto_term` VALUES (7008,'iscomposedof',NULL,NULL,'isComposedOf',NULL,1,'R',NULL,108,NULL,507,NULL,1,0,'OntoEntity',NULL,NULL,NULL,NULL,NULL,'NULL',507);
INSERT INTO `ri_onto_term` VALUES (7009,'isconvertableto',NULL,NULL,'isConvertableTo',NULL,1,'R',NULL,108,NULL,508,NULL,1,0,'OntoEntity',NULL,NULL,NULL,NULL,NULL,'NULL',508);
INSERT INTO `ri_onto_term` VALUES (7010,'isrealizedas',NULL,NULL,'isRealizedAs',NULL,1,'R',NULL,108,NULL,509,NULL,1,0,'OntoEntity',NULL,NULL,NULL,NULL,NULL,'NULL',509);
INSERT INTO `ri_onto_term` VALUES (120,'number','Number',NULL,NULL,NULL,1,'C',157,153,NULL,NULL,NULL,1,0,'Value',157,NULL,NULL,NULL,NULL,'NULL',157);
INSERT INTO `ri_onto_term` VALUES (7027,'istransformableto',NULL,NULL,'isTransformableTo',NULL,1,'R',NULL,108,NULL,510,NULL,1,0,'OntoEntity',NULL,NULL,NULL,NULL,NULL,'NULL',510);
INSERT INTO `ri_onto_term` VALUES (7011,'statistics',NULL,NULL,NULL,NULL,1,'RT',NULL,103,NULL,NULL,NULL,1,0,'Statistics',NULL,NULL,NULL,NULL,NULL,'NULL',103);
INSERT INTO `ri_onto_term` VALUES (7012,'comparativestatistics',NULL,NULL,NULL,NULL,1,'RT',NULL,104,NULL,NULL,NULL,1,0,'ComparativeStatistics',NULL,NULL,NULL,NULL,NULL,'NULL',104);
INSERT INTO `ri_onto_term` VALUES (7013,'operator',NULL,NULL,NULL,NULL,1,'RT',NULL,105,NULL,NULL,NULL,1,0,'Operator',NULL,NULL,NULL,NULL,NULL,'NULL',105);
INSERT INTO `ri_onto_term` VALUES (7014,'unitscale',NULL,NULL,NULL,NULL,1,'RT',NULL,106,NULL,NULL,NULL,1,0,'UnitScale',NULL,NULL,NULL,NULL,NULL,'NULL',106);
INSERT INTO `ri_onto_term` VALUES (7015,'digit',NULL,NULL,NULL,NULL,1,'RT',NULL,401,NULL,NULL,NULL,1,0,'Digit',NULL,NULL,NULL,NULL,NULL,'NULL',401);
INSERT INTO `ri_onto_term` VALUES (7016,'adjective',NULL,NULL,NULL,NULL,1,'RT',NULL,403,NULL,NULL,NULL,1,0,'Adjective',NULL,NULL,NULL,NULL,NULL,'NULL',403);
INSERT INTO `ri_onto_term` VALUES (7017,'conjunction',NULL,NULL,NULL,NULL,1,'RT',NULL,404,NULL,NULL,NULL,1,0,'Conjunction',NULL,NULL,NULL,NULL,NULL,'NULL',404);
INSERT INTO `ri_onto_term` VALUES (7018,'preposition',NULL,NULL,NULL,NULL,1,'RT',NULL,405,NULL,NULL,NULL,1,0,'Preposition',NULL,NULL,NULL,NULL,NULL,'NULL',405);
INSERT INTO `ri_onto_term` VALUES (7019,'byconjunction',NULL,NULL,NULL,NULL,1,'RT',NULL,406,NULL,NULL,NULL,1,0,'ByConjunction',NULL,NULL,NULL,NULL,NULL,'NULL',406);
INSERT INTO `ri_onto_term` VALUES (7020,'coordinatingconjunction',NULL,NULL,NULL,NULL,1,'RT',NULL,408,NULL,NULL,NULL,1,0,'CoordinatingConjunction',NULL,NULL,NULL,NULL,NULL,'NULL',408);
INSERT INTO `ri_onto_term` VALUES (7021,'comparativeinformation',NULL,NULL,NULL,NULL,1,'RT',NULL,110,NULL,NULL,NULL,1,0,'ComparativeInformation',NULL,NULL,NULL,NULL,NULL,'NULL',110);
INSERT INTO `ri_onto_term` VALUES (7022,'valuepreposition',NULL,NULL,NULL,NULL,1,'RT',NULL,410,NULL,NULL,NULL,1,0,'ValuePreposition',NULL,NULL,NULL,NULL,NULL,'NULL',410);
INSERT INTO `ri_onto_term` VALUES (5464,'cubicfeet','VolumeSymbol','VolumeSymbol4',NULL,NULL,1,'CLI',116,107,5464,NULL,NULL,1,0,'UnitSymbol',NULL,NULL,NULL,NULL,NULL,'NULL',5464);
INSERT INTO `ri_onto_term` VALUES (7025,'timequalifier',NULL,NULL,NULL,NULL,1,'RT',NULL,412,NULL,NULL,NULL,1,0,'TimeQualifier',NULL,NULL,NULL,NULL,NULL,'NULL',412);
INSERT INTO `ri_onto_term` VALUES (7026,'timepreposition',NULL,NULL,NULL,NULL,1,'RT',NULL,413,NULL,NULL,NULL,1,0,'TimePreposition',NULL,NULL,NULL,NULL,NULL,'NULL',413);
INSERT INTO `ri_onto_term` VALUES (5465,'cubic feet','VolumeSymbol','VolumeSymbol4',NULL,NULL,1,'CLI',116,107,5464,NULL,NULL,1,0,'UnitSymbol',NULL,NULL,NULL,NULL,NULL,'NULL',5464);
INSERT INTO `ri_onto_term` VALUES (5466,'cft','VolumeSymbol','VolumeSymbol4',NULL,NULL,1,'CLI',116,107,5464,NULL,NULL,1,0,'UnitSymbol',NULL,NULL,NULL,NULL,NULL,'NULL',5464);
INSERT INTO `ri_onto_term` VALUES (5467,'cuft','VolumeSymbol','VolumeSymbol4',NULL,NULL,1,'CLI',116,107,5464,NULL,NULL,1,0,'UnitSymbol',NULL,NULL,NULL,NULL,NULL,'NULL',5464);
INSERT INTO `ri_onto_term` VALUES (5468,'cu ft','VolumeSymbol','VolumeSymbol4',NULL,NULL,1,'CLI',116,107,5464,NULL,NULL,1,0,'UnitSymbol',NULL,NULL,NULL,NULL,NULL,'NULL',5464);
INSERT INTO `ri_onto_term` VALUES (5469,'pints','VolumeSymbol','VolumeSymbol5',NULL,NULL,1,'CLI',116,107,5465,NULL,NULL,1,0,'UnitSymbol',NULL,NULL,NULL,NULL,NULL,'NULL',5465);
INSERT INTO `ri_onto_term` VALUES (5470,'gallons','VolumeSymbol','VolumeSymbol6',NULL,NULL,1,'CLI',116,107,5466,NULL,NULL,1,0,'UnitSymbol',NULL,NULL,NULL,NULL,NULL,'NULL',5466);
INSERT INTO `ri_onto_term` VALUES (116,'mins',NULL,NULL,NULL,NULL,11,'T',NULL,208,NULL,NULL,NULL,1,0,'Minute',NULL,NULL,NULL,NULL,NULL,'NULL',208);
INSERT INTO `ri_onto_term` VALUES (117,'secs',NULL,NULL,NULL,NULL,11,'T',NULL,209,NULL,NULL,NULL,1,0,'Second',NULL,NULL,NULL,NULL,NULL,'NULL',209);
INSERT INTO `ri_onto_term` VALUES (118,'hr',NULL,NULL,NULL,NULL,11,'T',NULL,207,NULL,NULL,NULL,1,0,'Hour',NULL,NULL,NULL,NULL,NULL,'NULL',207);
INSERT INTO `ri_onto_term` VALUES (119,'hrs',NULL,NULL,NULL,NULL,11,'T',NULL,207,NULL,NULL,NULL,1,0,'Hour',NULL,NULL,NULL,NULL,NULL,'NULL',207);
INSERT INTO `ri_onto_term` VALUES (7028,'square meter','AreaSymbol','AreaSymbol1',NULL,NULL,1,'CLI',415,107,416,NULL,NULL,1,0,'UnitSymbol',NULL,NULL,NULL,NULL,NULL,'NULL',416);
INSERT INTO `ri_onto_term` VALUES (7029,'squaremeter','AreaSymbol','AreaSymbol1',NULL,NULL,1,'CLI',415,107,416,NULL,NULL,1,0,'UnitSymbol',NULL,NULL,NULL,NULL,NULL,'NULL',416);
INSERT INTO `ri_onto_term` VALUES (7030,'sq m','AreaSymbol','AreaSymbol1',NULL,NULL,11,'CLI',415,107,416,NULL,NULL,1,0,'UnitSymbol',NULL,NULL,NULL,NULL,NULL,'NULL',416);
INSERT INTO `ri_onto_term` VALUES (7031,'sqm','AreaSymbol','AreaSymbol1',NULL,NULL,11,'CLI',415,107,416,NULL,NULL,1,0,'UnitSymbol',NULL,NULL,NULL,NULL,NULL,'NULL',416);
INSERT INTO `ri_onto_term` VALUES (7032,'area','Area',NULL,NULL,NULL,1,'C',414,153,NULL,NULL,NULL,1,0,'Value',NULL,NULL,NULL,NULL,NULL,'NULL',414);
INSERT INTO `ri_onto_term` VALUES (7033,'sq in','AreaSymbol','AreaSymbol2',NULL,NULL,11,'CLI',415,107,417,NULL,NULL,1,0,'UnitSymbol',NULL,NULL,NULL,NULL,NULL,'NULL',417);
INSERT INTO `ri_onto_term` VALUES (7034,'sqin','AreaSymbol','AreaSymbol2',NULL,NULL,11,'CLI',415,107,417,NULL,NULL,1,0,'UnitSymbol',NULL,NULL,NULL,NULL,NULL,'NULL',417);
INSERT INTO `ri_onto_term` VALUES (7035,'square inch','AreaSymbol','AreaSymbol2',NULL,NULL,1,'CLI',415,107,417,NULL,NULL,1,0,'UnitSymbol',NULL,NULL,NULL,NULL,NULL,'NULL',417);
INSERT INTO `ri_onto_term` VALUES (7036,'squareinch','AreaSymbol','AreaSymbol2',NULL,NULL,1,'CLI',415,107,417,NULL,NULL,1,0,'UnitSymbol',NULL,NULL,NULL,NULL,NULL,'NULL',417);
INSERT INTO `ri_onto_term` VALUES (7037,'square feet','AreaSymbol','AreaSymbol3',NULL,NULL,1,'CLI',415,107,418,NULL,NULL,1,0,'UnitSymbol',NULL,NULL,NULL,NULL,NULL,'NULL',418);
INSERT INTO `ri_onto_term` VALUES (7038,'squarefeet','AreaSymbol','AreaSymbol3',NULL,NULL,1,'CLI',415,107,418,NULL,NULL,1,0,'UnitSymbol',NULL,NULL,NULL,NULL,NULL,'NULL',418);
INSERT INTO `ri_onto_term` VALUES (7039,'sq ft','AreaSymbol','AreaSymbol3',NULL,NULL,11,'CLI',415,107,418,NULL,NULL,1,0,'UnitSymbol',NULL,NULL,NULL,NULL,NULL,'NULL',418);
INSERT INTO `ri_onto_term` VALUES (7040,'sqft','AreaSymbol','AreaSymbol3',NULL,NULL,11,'CLI',415,107,418,NULL,NULL,1,0,'UnitSymbol',NULL,NULL,NULL,NULL,NULL,'NULL',418);
INSERT INTO `ri_onto_term` VALUES (7041,'sq yd','AreaSymbol','AreaSymbol4',NULL,NULL,11,'CLI',415,107,419,NULL,NULL,1,0,'UnitSymbol',NULL,NULL,NULL,NULL,NULL,'NULL',419);
INSERT INTO `ri_onto_term` VALUES (7042,'sqyd','AreaSymbol','AreaSymbol4',NULL,NULL,11,'CLI',415,107,419,NULL,NULL,1,0,'UnitSymbol',NULL,NULL,NULL,NULL,NULL,'NULL',419);
INSERT INTO `ri_onto_term` VALUES (7043,'square yard','AreaSymbol','AreaSymbol4',NULL,NULL,1,'CLI',415,107,419,NULL,NULL,1,0,'UnitSymbol',NULL,NULL,NULL,NULL,NULL,'NULL',419);
INSERT INTO `ri_onto_term` VALUES (7044,'squareyard','AreaSymbol','AreaSymbol4',NULL,NULL,1,'CLI',415,107,419,NULL,NULL,1,0,'UnitSymbol',NULL,NULL,NULL,NULL,NULL,'NULL',419);
INSERT INTO `ri_onto_term` VALUES (7045,'acre','AreaSymbol','AreaSymbol5',NULL,NULL,1,'CLI',415,107,420,NULL,NULL,1,0,'UnitSymbol',NULL,NULL,NULL,NULL,NULL,'NULL',420);
INSERT INTO `ri_onto_term` VALUES (7046,'ac','AreaSymbol','AreaSymbol5',NULL,NULL,11,'CLI',415,107,420,NULL,NULL,1,0,'UnitSymbol',NULL,NULL,NULL,NULL,NULL,'NULL',420);
INSERT INTO `ri_onto_term` VALUES (7047,'hectare','AreaSymbol','AreaSymbol6',NULL,NULL,1,'CLI',415,107,421,NULL,NULL,1,0,'UnitSymbol',NULL,NULL,NULL,NULL,NULL,'NULL',421);
INSERT INTO `ri_onto_term` VALUES (7048,'ha','AreaSymbol','AreaSymbol6',NULL,NULL,11,'CLI',415,107,421,NULL,NULL,1,0,'UnitSymbol',NULL,NULL,NULL,NULL,NULL,'NULL',421);
INSERT INTO `ri_onto_term` VALUES (7049,'m2','AreaSymbol','AreaSymbol1',NULL,NULL,11,'CLI',415,107,416,NULL,NULL,1,0,'UnitSymbol',NULL,NULL,NULL,NULL,NULL,'NULL',416);
INSERT INTO `ri_onto_term` VALUES (7050,'in2','AreaSymbol','AreaSymbol2',NULL,NULL,11,'CLI',415,107,417,NULL,NULL,1,0,'UnitSymbol',NULL,NULL,NULL,NULL,NULL,'NULL',417);
INSERT INTO `ri_onto_term` VALUES (7051,'ft2','AreaSymbol','AreaSymbol3',NULL,NULL,11,'CLI',415,107,418,NULL,NULL,1,0,'UnitSymbol',NULL,NULL,NULL,NULL,NULL,'NULL',418);
INSERT INTO `ri_onto_term` VALUES (7052,'sy','AreaSymbol','AreaSymbol4',NULL,NULL,11,'CLI',415,107,419,NULL,NULL,1,0,'UnitSymbol',NULL,NULL,NULL,NULL,NULL,'NULL',419);
INSERT INTO `ri_onto_term` VALUES (7053,'yd2','AreaSymbol','AreaSymbol4',NULL,NULL,11,'CLI',415,107,419,NULL,NULL,1,0,'UnitSymbol',NULL,NULL,NULL,NULL,NULL,'NULL',419);
INSERT INTO `ri_onto_term` VALUES (7054,'megabytes','MemorySymbol','MemorySymbol1',NULL,NULL,1,'CLI',423,107,424,NULL,NULL,1,0,'UnitSymbol',NULL,NULL,NULL,NULL,NULL,'NULL',424);
INSERT INTO `ri_onto_term` VALUES (7055,'mb','MemorySymbol','MemorySymbol1',NULL,NULL,1,'CLI',423,107,424,NULL,NULL,1,0,'UnitSymbol',NULL,NULL,NULL,NULL,NULL,'NULL',424);
INSERT INTO `ri_onto_term` VALUES (7056,'mega bytes','MemorySymbol','MemorySymbol1',NULL,NULL,1,'CLI',423,107,424,NULL,NULL,1,0,'UnitSymbol',NULL,NULL,NULL,NULL,NULL,'NULL',424);
INSERT INTO `ri_onto_term` VALUES (7057,'gigabytes','MemorySymbol','MemorySymbol2',NULL,NULL,1,'CLI',423,107,425,NULL,NULL,1,0,'UnitSymbol',NULL,NULL,NULL,NULL,NULL,'NULL',425);
INSERT INTO `ri_onto_term` VALUES (7058,'gb','MemorySymbol','MemorySymbol2',NULL,NULL,1,'CLI',423,107,425,NULL,NULL,1,0,'UnitSymbol',NULL,NULL,NULL,NULL,NULL,'NULL',425);
INSERT INTO `ri_onto_term` VALUES (7059,'giga bytes','MemorySymbol','MemorySymbol2',NULL,NULL,1,'CLI',423,107,425,NULL,NULL,1,0,'UnitSymbol',NULL,NULL,NULL,NULL,NULL,'NULL',425);
INSERT INTO `ri_onto_term` VALUES (7060,'kilobytes','MemorySymbol','MemorySymbol3',NULL,NULL,1,'CLI',423,107,426,NULL,NULL,1,0,'UnitSymbol',NULL,NULL,NULL,NULL,NULL,'NULL',426);
INSERT INTO `ri_onto_term` VALUES (7061,'kb','MemorySymbol','MemorySymbol3',NULL,NULL,1,'CLI',423,107,426,NULL,NULL,1,0,'UnitSymbol',NULL,NULL,NULL,NULL,NULL,'NULL',426);
INSERT INTO `ri_onto_term` VALUES (7062,'kilo bytes','MemorySymbol','MemorySymbol3',NULL,NULL,1,'CLI',423,107,426,NULL,NULL,1,0,'UnitSymbol',NULL,NULL,NULL,NULL,NULL,'NULL',426);
INSERT INTO `ri_onto_term` VALUES (7063,'memory','Memory',NULL,NULL,NULL,1,'C',422,153,NULL,NULL,NULL,1,0,'Value',NULL,NULL,NULL,NULL,NULL,'NULL',422);
INSERT INTO `ri_onto_term` VALUES (7064,'megapixels','ResolutionSymbol','ResolutionSymbol1',NULL,NULL,1,'CLI',428,107,429,NULL,NULL,1,0,'UnitSymbol',NULL,NULL,NULL,NULL,NULL,'NULL',429);
INSERT INTO `ri_onto_term` VALUES (7065,'mp','ResolutionSymbol','ResolutionSymbol1',NULL,NULL,1,'CLI',428,107,429,NULL,NULL,1,0,'UnitSymbol',NULL,NULL,NULL,NULL,NULL,'NULL',429);
INSERT INTO `ri_onto_term` VALUES (7066,'mega pixels','ResolutionSymbol','ResolutionSymbol1',NULL,NULL,1,'CLI',428,107,429,NULL,NULL,1,0,'UnitSymbol',NULL,NULL,NULL,NULL,NULL,'NULL',429);
INSERT INTO `ri_onto_term` VALUES (7067,'pixels','ResolutionSymbol','ResolutionSymbol2',NULL,NULL,1,'CLI',428,107,430,NULL,NULL,1,0,'UnitSymbol',NULL,NULL,NULL,NULL,NULL,'NULL',430);
INSERT INTO `ri_onto_term` VALUES (7068,'resolution','Resolution',NULL,NULL,NULL,1,'C',427,153,NULL,NULL,NULL,1,0,'Value',NULL,NULL,NULL,NULL,NULL,'NULL',427);
INSERT INTO `ri_onto_term` VALUES (7071,'ct','Statistics','Statistics3',NULL,NULL,11,'RTLI',NULL,103,1003,NULL,NULL,1,0,'Statistics',NULL,NULL,NULL,NULL,NULL,'NULL',1003);
INSERT INTO `ri_onto_term` VALUES (7072,'days','Day',NULL,NULL,NULL,11,'T',NULL,206,NULL,NULL,NULL,1,0,'Day',NULL,NULL,NULL,NULL,NULL,'NULL',206);
INSERT INTO `ri_onto_term` VALUES (7073,'qtr','Quarter',NULL,NULL,NULL,11,'T',NULL,203,NULL,NULL,NULL,1,0,'Quarter',NULL,NULL,NULL,NULL,NULL,'NULL',203);
INSERT INTO `ri_onto_term` VALUES (7074,'qtrs','Quarter',NULL,NULL,NULL,11,'T',NULL,203,NULL,NULL,NULL,1,0,'Quarter',NULL,NULL,NULL,NULL,NULL,'NULL',203);
INSERT INTO `ri_onto_term` VALUES (7075,'yr','Year',NULL,NULL,NULL,11,'T',NULL,202,NULL,NULL,NULL,1,0,'Year',NULL,NULL,NULL,NULL,NULL,'NULL',202);
INSERT INTO `ri_onto_term` VALUES (7076,'yrs','Year',NULL,NULL,NULL,11,'T',NULL,202,NULL,NULL,NULL,1,0,'Year',NULL,NULL,NULL,NULL,NULL,'NULL',202);
INSERT INTO `ri_onto_term` VALUES (7077,'pwr','Power',NULL,NULL,NULL,11,'C',156,153,NULL,NULL,NULL,1,0,'Value',NULL,NULL,NULL,NULL,NULL,'NULL',156);
INSERT INTO `ri_onto_term` VALUES (7078,'num','Number',NULL,NULL,NULL,11,'C',157,153,NULL,NULL,NULL,1,0,'Value',NULL,NULL,NULL,NULL,NULL,'NULL',157);
/*!40000 ALTER TABLE `ri_onto_term` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ri_parallel_word`
--

DROP TABLE IF EXISTS `ri_parallel_word`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ri_parallel_word` (
  `ID` bigint(20) NOT NULL,
  `WORD` varchar(255) DEFAULT NULL COMMENT 'COLUMN ON WHICH LOOKUP SHOULD BE DONE',
  `EQUIVALENT_WORD` varchar(255) DEFAULT NULL,
  `CLUSTER_TERMS` varchar(255) DEFAULT NULL COMMENT 'UNDER WHICH CONTEXT THIS DEFINITIONIS VALID, COMMA SEPARATED STRING',
  `PREFIX_SPACE` int(1) DEFAULT '0' COMMENT '1 INDIACTES VALUE IS PREFIXED WITH SPACE',
  `SUFFIX_SPACE` int(1) DEFAULT '0' COMMENT '1 INDICATES VALUE SUFFIXED WITH SPACE',
  `KEY_WORD` int(1) DEFAULT '0' COMMENT '1 INDICATES IT IS A BUSINESS TERM',
  `MULTI_WORD` int(1) DEFAULT '0' COMMENT '1 INDICATES IT IS PART OF MULTI WORD STRING',
  `KEY_WORD_ID` bigint(20) NOT NULL,
  `HITS` bigint(20) NOT NULL,
  `quality` decimal(10,2) NOT NULL DEFAULT '0.85',
  `USER_ID` bigint(20) NOT NULL,
  `BE_ID` bigint(20) DEFAULT NULL,
  `PWD_TYPE` int(2) NOT NULL DEFAULT '1',
  `MODEL_GROUP_ID` bigint(20) DEFAULT NULL,
  `pos_type` varchar(2) DEFAULT NULL,
  `pref_select` int(1) DEFAULT '0',
  `IS_DIFFERENT_WORD` char(1) DEFAULT 'Y',
  PRIMARY KEY (`ID`),
  KEY `Idx_RIP_WORD` (`WORD`),
  KEY `Idx_RIP_EQUIVALENT_WORD` (`EQUIVALENT_WORD`),
  KEY `Idx_RIP_USER_ID` (`USER_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ri_parallel_word`
--

LOCK TABLES `ri_parallel_word` WRITE;
/*!40000 ALTER TABLE `ri_parallel_word` DISABLE KEYS */;
INSERT INTO `ri_parallel_word` VALUES (101,'h','Hundred',NULL,1,1,1,0,101,1,'0.85',0,2001,1,1,NULL,1,'Y');
INSERT INTO `ri_parallel_word` VALUES (102,'hundred','h',NULL,1,1,0,0,101,1,'0.85',0,2001,1,1,NULL,0,'Y');
INSERT INTO `ri_parallel_word` VALUES (103,'k','Thousand',NULL,1,1,1,0,102,1,'0.85',0,2002,1,1,NULL,1,'Y');
INSERT INTO `ri_parallel_word` VALUES (104,'thousand','k',NULL,1,1,0,0,102,1,'0.85',0,2002,1,1,NULL,0,'Y');
INSERT INTO `ri_parallel_word` VALUES (105,'m','Million',NULL,1,1,1,0,103,1,'0.85',0,2003,1,1,NULL,1,'Y');
INSERT INTO `ri_parallel_word` VALUES (106,'million','m',NULL,1,1,0,0,103,1,'0.85',0,2003,1,1,NULL,0,'Y');
INSERT INTO `ri_parallel_word` VALUES (107,'b','Billion',NULL,1,1,1,0,104,1,'0.85',0,2004,1,1,NULL,1,'Y');
INSERT INTO `ri_parallel_word` VALUES (108,'billion','b',NULL,1,1,0,0,104,1,'0.85',0,2004,1,1,NULL,0,'Y');
INSERT INTO `ri_parallel_word` VALUES (109,'t','Trillion',NULL,1,1,1,0,105,1,'0.85',0,2005,1,1,NULL,1,'Y');
INSERT INTO `ri_parallel_word` VALUES (110,'trillion','t',NULL,1,1,0,0,105,1,'0.85',0,2005,1,1,NULL,0,'Y');
INSERT INTO `ri_parallel_word` VALUES (111,'dollar','$',NULL,1,1,1,0,106,1,'0.85',1,5001,1,1,NULL,0,'Y');
INSERT INTO `ri_parallel_word` VALUES (112,'$','Dollar',NULL,1,1,1,0,106,1,'0.85',1,5001,1,1,NULL,1,'Y');
INSERT INTO `ri_parallel_word` VALUES (115,'eq','=',NULL,1,1,1,0,108,1,'0.85',0,1051,1,1,NULL,1,'Y');
INSERT INTO `ri_parallel_word` VALUES (116,'=','EQ',NULL,1,1,1,0,108,1,'0.85',0,1051,1,1,NULL,0,'Y');
INSERT INTO `ri_parallel_word` VALUES (117,'lt','<',NULL,1,1,1,0,109,1,'0.85',0,1052,1,1,NULL,1,'Y');
INSERT INTO `ri_parallel_word` VALUES (118,'<','LT',NULL,1,1,1,0,109,1,'0.85',0,1052,1,1,NULL,0,'Y');
INSERT INTO `ri_parallel_word` VALUES (119,'gt','>',NULL,1,1,1,0,110,1,'0.85',0,1053,1,1,NULL,1,'Y');
INSERT INTO `ri_parallel_word` VALUES (120,'>','GT',NULL,1,1,1,0,110,1,'0.85',0,1053,1,1,NULL,0,'Y');
INSERT INTO `ri_parallel_word` VALUES (121,'ne','!=',NULL,1,1,1,0,111,1,'0.85',0,1054,1,1,NULL,1,'Y');
INSERT INTO `ri_parallel_word` VALUES (122,'!=','NE',NULL,1,1,1,0,111,1,'0.85',0,1054,1,1,NULL,0,'Y');
INSERT INTO `ri_parallel_word` VALUES (123,'lte','<=',NULL,1,1,1,0,112,1,'0.85',0,1055,1,1,NULL,1,'Y');
INSERT INTO `ri_parallel_word` VALUES (124,'<=','LTE',NULL,1,1,1,0,112,1,'0.85',0,1055,1,1,NULL,0,'Y');
INSERT INTO `ri_parallel_word` VALUES (125,'gte','>=',NULL,1,1,1,0,113,1,'0.85',0,1056,1,1,NULL,1,'Y');
INSERT INTO `ri_parallel_word` VALUES (126,'>=','GTE',NULL,1,1,1,0,113,1,'0.85',0,1056,1,1,NULL,0,'Y');
INSERT INTO `ri_parallel_word` VALUES (127,'old','Last',NULL,1,1,1,0,114,1,'0.95',1,1200,3,1,NULL,1,'Y');
INSERT INTO `ri_parallel_word` VALUES (128,'last','Old',NULL,1,1,1,0,114,1,'0.95',1,1200,3,1,NULL,0,'Y');
/*!40000 ALTER TABLE `ri_parallel_word` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ri_shared_user_model_mapping`
--

DROP TABLE IF EXISTS `ri_shared_user_model_mapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ri_shared_user_model_mapping` (
  `ID` bigint(20) NOT NULL,
  `base_instance_be_id` bigint(20) DEFAULT NULL,
  `model_group_id` bigint(20) DEFAULT NULL,
  `instance_be_id` bigint(20) DEFAULT NULL,
  `concept_be_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `IDX_RISUMM_BIBEID` (`base_instance_be_id`),
  KEY `IDX_RISUMM_MGID` (`model_group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ri_shared_user_model_mapping`
--

LOCK TABLES `ri_shared_user_model_mapping` WRITE;
/*!40000 ALTER TABLE `ri_shared_user_model_mapping` DISABLE KEYS */;
/*!40000 ALTER TABLE `ri_shared_user_model_mapping` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rules`
--

DROP TABLE IF EXISTS `rules`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rules` (
  `ID` bigint(20) NOT NULL,
  `RULE_NAME` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rules`
--

LOCK TABLES `rules` WRITE;
/*!40000 ALTER TABLE `rules` DISABLE KEYS */;
INSERT INTO `rules` VALUES (1,'MonthType');
INSERT INTO `rules` VALUES (2,'Month-1');
INSERT INTO `rules` VALUES (3,'Month-2');
INSERT INTO `rules` VALUES (4,'Relative-Month');
INSERT INTO `rules` VALUES (11,'QuarterType');
INSERT INTO `rules` VALUES (12,'Quarter-1');
INSERT INTO `rules` VALUES (13,'Quarter-2');
INSERT INTO `rules` VALUES (14,'Quarter-3');
INSERT INTO `rules` VALUES (15,'Relative-Quarter');
INSERT INTO `rules` VALUES (21,'Year-1');
INSERT INTO `rules` VALUES (22,'Year-2');
INSERT INTO `rules` VALUES (23,'Relative-Year');
INSERT INTO `rules` VALUES (101,'Unit-1');
INSERT INTO `rules` VALUES (102,'Value-1');
INSERT INTO `rules` VALUES (25,'Year-TF-2');
INSERT INTO `rules` VALUES (104,'BETWEEN-AND-VALUE');
INSERT INTO `rules` VALUES (105,'BETWEEN-AND-TF');
INSERT INTO `rules` VALUES (106,'FROM-TO-VALUE');
INSERT INTO `rules` VALUES (107,'FROM-TO-TF');
INSERT INTO `rules` VALUES (10001,'TimeFrame Association Left');
INSERT INTO `rules` VALUES (10002,'TimeFrame Association Right');
INSERT INTO `rules` VALUES (10003,'Value Association Left');
INSERT INTO `rules` VALUES (10004,'Value Association Right');
INSERT INTO `rules` VALUES (10005,'Stat Association Right');
INSERT INTO `rules` VALUES (10007,'Stat Association Left');
INSERT INTO `rules` VALUES (108,'value-3');
INSERT INTO `rules` VALUES (31,'Relative-Day');
INSERT INTO `rules` VALUES (109,'Unit-Distance');
INSERT INTO `rules` VALUES (110,'Currency-1');
INSERT INTO `rules` VALUES (111,'Distance-1');
INSERT INTO `rules` VALUES (112,'Volume-1');
INSERT INTO `rules` VALUES (113,'Power-1');
INSERT INTO `rules` VALUES (114,'between-and-value');
INSERT INTO `rules` VALUES (115,'ValueWithPreposition');
INSERT INTO `rules` VALUES (32,'Absolute-day');
INSERT INTO `rules` VALUES (33,'relative-DayTF');
INSERT INTO `rules` VALUES (116,'value-logical');
INSERT INTO `rules` VALUES (26,'Year-TF-Concept-1');
INSERT INTO `rules` VALUES (126,'RelativeTimeTF');
INSERT INTO `rules` VALUES (10008,'IndicatorValidation');
INSERT INTO `rules` VALUES (117,'WeightRule');
INSERT INTO `rules` VALUES (127,'TimeDuration');
INSERT INTO `rules` VALUES (24,'Year-TF-1');
INSERT INTO `rules` VALUES (5,'Month-3');
INSERT INTO `rules` VALUES (34,'absolute-day-instance');
INSERT INTO `rules` VALUES (128,'Area-1');
INSERT INTO `rules` VALUES (129,'Memory-1');
INSERT INTO `rules` VALUES (130,'Resolution-1');
INSERT INTO `rules` VALUES (118,'Number-1');
/*!40000 ALTER TABLE `rules` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `secondary_word`
--

DROP TABLE IF EXISTS `secondary_word`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `secondary_word` (
  `ID` bigint(20) NOT NULL,
  `WORD` varchar(255) NOT NULL,
  `DEFAULT_WEIGHT` decimal(10,2) DEFAULT '0.00',
  `FREQUENCY` bigint(20) DEFAULT NULL,
  `MODEL_GROUP_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_SCNDRY_WRD` (`WORD`,`MODEL_GROUP_ID`),
  KEY `FK_MGID_SECONDARY_WORD` (`MODEL_GROUP_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `secondary_word`
--

LOCK TABLES `secondary_word` WRITE;
/*!40000 ALTER TABLE `secondary_word` DISABLE KEYS */;
/*!40000 ALTER TABLE `secondary_word` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `security_group_roles`
--

DROP TABLE IF EXISTS `security_group_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `security_group_roles` (
  `GROUP_ID` bigint(20) NOT NULL,
  `ROLE_ID` bigint(20) NOT NULL,
  KEY `FK_GR_RID` (`ROLE_ID`),
  KEY `FK_GR_GID` (`GROUP_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `security_group_roles`
--

LOCK TABLES `security_group_roles` WRITE;
/*!40000 ALTER TABLE `security_group_roles` DISABLE KEYS */;
INSERT INTO `security_group_roles` VALUES (1,1);
INSERT INTO `security_group_roles` VALUES (1,2);
INSERT INTO `security_group_roles` VALUES (1,3);
INSERT INTO `security_group_roles` VALUES (2,2);
INSERT INTO `security_group_roles` VALUES (3,2);
INSERT INTO `security_group_roles` VALUES (3,4);
INSERT INTO `security_group_roles` VALUES (4,5);
/*!40000 ALTER TABLE `security_group_roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `security_groups`
--

DROP TABLE IF EXISTS `security_groups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `security_groups` (
  `ID` bigint(20) NOT NULL,
  `NAME` varchar(50) NOT NULL,
  `DESCRIPTION` varchar(100) NOT NULL,
  `STATUS` char(1) NOT NULL DEFAULT 'D',
  `DATE_CREATED` datetime DEFAULT NULL,
  `DATE_MODIFIED` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `security_groups`
--

LOCK TABLES `security_groups` WRITE;
/*!40000 ALTER TABLE `security_groups` DISABLE KEYS */;
INSERT INTO `security_groups` VALUES (1,'All','Every permission','A','2009-01-01 00:00:00',NULL);
INSERT INTO `security_groups` VALUES (2,'User Group','User Group','A','2009-01-01 00:00:00',NULL);
INSERT INTO `security_groups` VALUES (3,'Publisher Group','Publisher Group','A','2009-12-10 00:00:00',NULL);
INSERT INTO `security_groups` VALUES (4,'Adv publisher Group','Advanced Publisher Group','A','2012-09-08 00:00:00',NULL);
/*!40000 ALTER TABLE `security_groups` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `security_roles`
--

DROP TABLE IF EXISTS `security_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `security_roles` (
  `ID` bigint(20) NOT NULL,
  `NAME` varchar(50) NOT NULL,
  `DESCRIPTION` varchar(100) NOT NULL,
  `STATUS` char(1) NOT NULL DEFAULT 'D',
  `DATE_CREATED` datetime DEFAULT NULL,
  `DATE_MODIFIED` datetime DEFAULT NULL,
  `SYSTEM_ROLE` char(1) DEFAULT 'N',
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `security_roles`
--

LOCK TABLES `security_roles` WRITE;
/*!40000 ALTER TABLE `security_roles` DISABLE KEYS */;
INSERT INTO `security_roles` VALUES (1,'ROLE_ADMIN','Admin Role','A','2009-01-01 00:00:00',NULL,'Y');
INSERT INTO `security_roles` VALUES (2,'ROLE_USER','End User Role','A','2009-01-01 00:00:00',NULL,'Y');
INSERT INTO `security_roles` VALUES (3,'ROLE_GUEST','Guest Role','A','2009-01-01 00:00:00',NULL,'Y');
INSERT INTO `security_roles` VALUES (4,'ROLE_PUBLISHER','Publisher Role','A','2009-12-10 00:00:00',NULL,'Y');
INSERT INTO `security_roles` VALUES (5,'ROLE_ADV_PUBLISHER','Advanced Publisher','A','2012-09-08 00:00:00',NULL,'Y');
/*!40000 ALTER TABLE `security_roles` ENABLE KEYS */;
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
  PRIMARY KEY (`SEQUENCE_NAME`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sequences`
--

LOCK TABLES `sequences` WRITE;
/*!40000 ALTER TABLE `sequences` DISABLE KEYS */;
INSERT INTO `sequences` VALUES ('ACL_CLASS',1001);
INSERT INTO `sequences` VALUES ('ACL_ENTRY',1001);
INSERT INTO `sequences` VALUES ('ACL_OBJECT_IDENTITY',1001);
INSERT INTO `sequences` VALUES ('ACL_SID',1001);
INSERT INTO `sequences` VALUES ('APPLICATION',110);
INSERT INTO `sequences` VALUES ('ASSET',1001);
INSERT INTO `sequences` VALUES ('ASSET_DETAIL',1000);
INSERT INTO `sequences` VALUES ('ASSET_ENTITY',1001);
INSERT INTO `sequences` VALUES ('ASSET_EXTENDED_DETAIL',1000);
INSERT INTO `sequences` VALUES ('BOOKMARK',1001);
INSERT INTO `sequences` VALUES ('BUSINESS_ENTITY',10001);
INSERT INTO `sequences` VALUES ('COLUM',1001);
INSERT INTO `sequences` VALUES ('CONCEPT',1001);
INSERT INTO `sequences` VALUES ('CONSTRAIN',1001);
INSERT INTO `sequences` VALUES ('CONVERSION_FORMULA',1001);
INSERT INTO `sequences` VALUES ('DATA_SOURCE',1006);
INSERT INTO `sequences` VALUES ('DATE_FORMAT',1189);
INSERT INTO `sequences` VALUES ('DEFAULT_CONVERSION_DETAIL',1001);
INSERT INTO `sequences` VALUES ('DEFAULT_INSTANCE_VALUE',1001);
INSERT INTO `sequences` VALUES ('DEFAULT_DYNAMIC_VALUE',101);
INSERT INTO `sequences` VALUES ('ENTITY_TRIPLE_DEFINITION',10001);
INSERT INTO `sequences` VALUES ('FOLDER',1001);
INSERT INTO `sequences` VALUES ('INDEX',1001);
INSERT INTO `sequences` VALUES ('INSTANCE',10001);
INSERT INTO `sequences` VALUES ('INSTANCE_MAPPING_SUGGESTION',10001);
INSERT INTO `sequences` VALUES ('INSTANCE_MAPPING_SUGGESTION_DETAIL',10001);
INSERT INTO `sequences` VALUES ('INSTANCE_PATH_DEFINITION',10001);
INSERT INTO `sequences` VALUES ('JOINS',1001);
INSERT INTO `sequences` VALUES ('JOINS_DEFINITION',1001);
INSERT INTO `sequences` VALUES ('KEY_WORD',10003);
INSERT INTO `sequences` VALUES ('MAPPING',10001);
INSERT INTO `sequences` VALUES ('MEMBR',1001);
INSERT INTO `sequences` VALUES ('MODEL',110);
INSERT INTO `sequences` VALUES ('MODEL_GROUP',110);
INSERT INTO `sequences` VALUES ('MODEL_GROUP_MAPPING',101);
INSERT INTO `sequences` VALUES ('PARALLEL_WORD',10003);
INSERT INTO `sequences` VALUES ('PATH_DEFINITION',10001);
INSERT INTO `sequences` VALUES ('PATH_DEFINITION_ETD',10001);
INSERT INTO `sequences` VALUES ('POPULARITY_HIT',1001);
INSERT INTO `sequences` VALUES ('PROFILE',2001);
INSERT INTO `sequences` VALUES ('RANGES',1001);
INSERT INTO `sequences` VALUES ('RANGE_DETAIL',1001);
INSERT INTO `sequences` VALUES ('RELATION',1001);
INSERT INTO `sequences` VALUES ('RI_ONTO_TERM',10001);
INSERT INTO `sequences` VALUES ('RI_PARALLEL_WORD',10004);
INSERT INTO `sequences` VALUES ('SECONDARY_WORD',10001);
INSERT INTO `sequences` VALUES ('SECURITY_GROUPS',101);
INSERT INTO `sequences` VALUES ('SECURITY_ROLES',101);
INSERT INTO `sequences` VALUES ('SFL_TERM',10001);
INSERT INTO `sequences` VALUES ('SFL_TERM_TOKEN',10001);
INSERT INTO `sequences` VALUES ('STAT',1001);
INSERT INTO `sequences` VALUES ('SYSTEM_INFO',1001);
INSERT INTO `sequences` VALUES ('TABL',1001);
INSERT INTO `sequences` VALUES ('USERS',1001);
INSERT INTO `sequences` VALUES ('APPLICATION_EXAMPLE',1001);
INSERT INTO `sequences` VALUES ('DEFAULT_METRIC',100);
INSERT INTO `sequences` VALUES ('APPLICATION_DETAIL',1001);
INSERT INTO `sequences` VALUES ('BATCH_PROCESS',1001);
INSERT INTO `sequences` VALUES ('BATCH_PROCESS_DETAIL',1001);
INSERT INTO `sequences` VALUES ('ENUM_LOOKUP',50007);
INSERT INTO `sequences` VALUES ('PUBLISHED_FILE_INFO',1001);
INSERT INTO `sequences` VALUES ('PUBLISHED_FILE_INFO_DETAILS',1001);
INSERT INTO `sequences` VALUES ('PUBLISHED_FILE_TABLE_DETAILS',1001);
INSERT INTO `sequences` VALUES ('PUBLISHED_FILE_TABLE_INFO',1001);
INSERT INTO `sequences` VALUES ('RESOURCE',1001);
INSERT INTO `sequences` VALUES ('BEHAVIOR',1001);
INSERT INTO `sequences` VALUES ('CLOUD',1001);
INSERT INTO `sequences` VALUES ('CLOUD_COMPONENT',1001);
INSERT INTO `sequences` VALUES ('ENTITY_BEHAVIOR',1001);
INSERT INTO `sequences` VALUES ('POSSIBLE_ATTRIBUTE_RULE',1001);
INSERT INTO `sequences` VALUES ('POSSIBLE_ATTRIBUTES',1001);
INSERT INTO `sequences` VALUES ('POSSIBLE_BEHAVIOR',1001);
INSERT INTO `sequences` VALUES ('RI_CLOUD',1001);
INSERT INTO `sequences` VALUES ('RULES',10009);
INSERT INTO `sequences` VALUES ('TYPE',1001);
INSERT INTO `sequences` VALUES ('ASSET_OPERATION_INFO',1001);
INSERT INTO `sequences` VALUES ('H_ASSET_OPERATION_INFO',1001);
INSERT INTO `sequences` VALUES ('BUSINESS_ENTITY_MAINTENANCE',1000);
INSERT INTO `sequences` VALUES ('VERTICAL_APP_WEIGHT',3);
INSERT INTO `sequences` VALUES ('CANNED_REPORT',1000);
INSERT INTO `sequences` VALUES ('INSTANCE_TRIPLE_DEFINITION',1001);
INSERT INTO `sequences` VALUES ('COUNTRY_LOOKUP',1342);
INSERT INTO `sequences` VALUES ('VERTICAL',1005);
INSERT INTO `sequences` VALUES ('VERTICAL_APP_EXAMPLE',1001);
INSERT INTO `sequences` VALUES ('USER_REQUEST',1001);
INSERT INTO `sequences` VALUES ('APPLICATION_OPERATION',100);
INSERT INTO `sequences` VALUES ('POSSIBLE_DETAIL_TYPE',101);
INSERT INTO `sequences` VALUES ('ENTITY_DETAIL_TYPE',101);
INSERT INTO `sequences` VALUES ('APP_EAS_DETAIL_INFO',1001);
INSERT INTO `sequences` VALUES ('system_variable',73);
INSERT INTO `sequences` VALUES ('EAS_INDEX',1001);
INSERT INTO `sequences` VALUES ('BUSINESS_ENTITY_VARIATION',32);
INSERT INTO `sequences` VALUES ('APP_DATA_SOURCE',1001);
INSERT INTO `sequences` VALUES ('CONTENT_CLEANUP_PATTERN',1);
INSERT INTO `sequences` VALUES ('UNSTRUCTURED_APP_DETAIL',100);
INSERT INTO `sequences` VALUES ('ASSET_OPERATION_DETAIL',1);
INSERT INTO `sequences` VALUES ('HIERARCHY',1);
INSERT INTO `sequences` VALUES ('HIERARCHY_DETAIL',1);
INSERT INTO `sequences` VALUES ('USER_QUERY_POSSIBILITY',1);
/*!40000 ALTER TABLE `sequences` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sfl_term`
--

DROP TABLE IF EXISTS `sfl_term`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sfl_term` (
  `ID` bigint(20) NOT NULL,
  `BUSINESS_TERM` varchar(255) NOT NULL,
  `context_id` bigint(20) NOT NULL,
  `required_token_count` int(2) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `bt_index` (`BUSINESS_TERM`),
  KEY `IDX_ST_CID` (`context_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sfl_term`
--

LOCK TABLES `sfl_term` WRITE;
/*!40000 ALTER TABLE `sfl_term` DISABLE KEYS */;
INSERT INTO `sfl_term` VALUES (1,'Greater Than',1,NULL);
INSERT INTO `sfl_term` VALUES (2,'Equal To',1,NULL);
INSERT INTO `sfl_term` VALUES (3,'Not Equal To',1,NULL);
INSERT INTO `sfl_term` VALUES (4,'Less Than',1,NULL);
INSERT INTO `sfl_term` VALUES (5,'Standard Deviation',1,NULL);
INSERT INTO `sfl_term` VALUES (6,'Greater Than Equal To',1,NULL);
INSERT INTO `sfl_term` VALUES (7,'Less Than Equal To',1,NULL);
INSERT INTO `sfl_term` VALUES (8,'More Than',1,NULL);
INSERT INTO `sfl_term` VALUES (9,'day after tomorrow',1,0);
INSERT INTO `sfl_term` VALUES (10,'day before yesterday',1,0);
INSERT INTO `sfl_term` VALUES (11,'Cubic Feet',1,2);
INSERT INTO `sfl_term` VALUES (12,'cu ft',1,2);
INSERT INTO `sfl_term` VALUES (13,'square meter',1,2);
INSERT INTO `sfl_term` VALUES (14,'sq m',1,2);
INSERT INTO `sfl_term` VALUES (15,'sq in',1,2);
INSERT INTO `sfl_term` VALUES (16,'square inch',1,2);
INSERT INTO `sfl_term` VALUES (17,'square feet',1,2);
INSERT INTO `sfl_term` VALUES (18,'sq ft',1,2);
INSERT INTO `sfl_term` VALUES (19,'square yard',1,2);
INSERT INTO `sfl_term` VALUES (20,'sq yd',1,2);
INSERT INTO `sfl_term` VALUES (21,'Kilo Bytes',1,2);
INSERT INTO `sfl_term` VALUES (22,'Giga Bytes',1,2);
INSERT INTO `sfl_term` VALUES (23,'Mega Bytes',1,2);
INSERT INTO `sfl_term` VALUES (24,'Mega Pixels',1,2);
/*!40000 ALTER TABLE `sfl_term` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sfl_term_token`
--

DROP TABLE IF EXISTS `sfl_term_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sfl_term_token` (
  `ID` bigint(20) NOT NULL,
  `SFL_TERM_ID` bigint(20) NOT NULL,
  `BUSINESS_TERM_TOKEN` varchar(255) DEFAULT NULL,
  `HITS` bigint(20) NOT NULL,
  `WEIGHT` decimal(10,2) DEFAULT '35.00' COMMENT 'THE VALUE CAN NEVER BE IN THREE DIGITS',
  `GR0UP` int(3) DEFAULT '1',
  `TOKEN_ORDER` int(3) DEFAULT NULL,
  `primary_word` int(1) DEFAULT '1',
  `context_id` bigint(20) NOT NULL,
  `business_term` varchar(255) DEFAULT NULL,
  `required_token_count` int(2) DEFAULT '0',
  `required` int(1) DEFAULT '0',
  PRIMARY KEY (`ID`),
  KEY `FK_STT_STID` (`SFL_TERM_ID`),
  KEY `BUSINESS_TERM_TOKEN_IDX` (`BUSINESS_TERM_TOKEN`),
  KEY `IDX_STT_CID` (`context_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sfl_term_token`
--

LOCK TABLES `sfl_term_token` WRITE;
/*!40000 ALTER TABLE `sfl_term_token` DISABLE KEYS */;
INSERT INTO `sfl_term_token` VALUES (1,1,'greater',0,'50.00',1,0,1,1,'Greater Than',0,0);
INSERT INTO `sfl_term_token` VALUES (2,1,'than',0,'50.00',1,1,0,1,'Greater Than',0,0);
INSERT INTO `sfl_term_token` VALUES (3,2,'equal',0,'50.00',1,0,1,1,'Equal To',0,0);
INSERT INTO `sfl_term_token` VALUES (4,2,'to',0,'50.00',1,1,0,1,'Equal To',0,0);
INSERT INTO `sfl_term_token` VALUES (5,3,'not',0,'40.00',1,0,0,1,'Not Equal To',0,0);
INSERT INTO `sfl_term_token` VALUES (6,3,'equal',0,'30.00',1,1,1,1,'Not Equal To',0,0);
INSERT INTO `sfl_term_token` VALUES (7,3,'to',0,'30.00',1,2,0,1,'Not Equal To',0,0);
INSERT INTO `sfl_term_token` VALUES (8,4,'less',0,'50.00',1,0,1,1,'Less Than',0,0);
INSERT INTO `sfl_term_token` VALUES (9,4,'than',0,'50.00',1,1,0,1,'Less Than',0,0);
INSERT INTO `sfl_term_token` VALUES (10,5,'standard',0,'50.00',1,0,1,1,'Standard Deviation',0,0);
INSERT INTO `sfl_term_token` VALUES (11,5,'deviation',0,'50.00',1,1,1,1,'Standard Deviation',0,0);
INSERT INTO `sfl_term_token` VALUES (12,6,'greater',0,'40.00',1,0,1,1,'Greater Than Equal To',0,0);
INSERT INTO `sfl_term_token` VALUES (13,6,'than',0,'10.00',1,1,0,1,'Greater Than Equal To',0,0);
INSERT INTO `sfl_term_token` VALUES (14,6,'equal',0,'40.00',1,2,0,1,'Greater Than Equal To',0,0);
INSERT INTO `sfl_term_token` VALUES (15,6,'to',0,'10.00',1,3,0,1,'Greater Than Equal To',0,0);
INSERT INTO `sfl_term_token` VALUES (16,7,'less',0,'40.00',1,0,1,1,'Less Than Equal To',0,0);
INSERT INTO `sfl_term_token` VALUES (17,7,'than',0,'10.00',1,1,0,1,'Less Than Equal To',0,0);
INSERT INTO `sfl_term_token` VALUES (18,7,'equal',0,'40.00',1,2,0,1,'Less Than Equal To',0,0);
INSERT INTO `sfl_term_token` VALUES (19,7,'to',0,'10.00',1,3,0,1,'Less Than Equal To',0,0);
INSERT INTO `sfl_term_token` VALUES (20,8,'more',0,'50.00',1,0,1,1,'More Than',0,0);
INSERT INTO `sfl_term_token` VALUES (21,8,'than',0,'50.00',1,1,0,1,'More Than',0,0);
INSERT INTO `sfl_term_token` VALUES (22,9,'day',0,'40.00',1,0,1,1,'day after tomorrow',0,0);
INSERT INTO `sfl_term_token` VALUES (23,9,'after',0,'40.00',1,1,0,1,'day after tomorrow',0,0);
INSERT INTO `sfl_term_token` VALUES (24,9,'tomorrow',0,'20.00',1,2,1,1,'day after tomorrow',0,0);
INSERT INTO `sfl_term_token` VALUES (25,10,'day',0,'40.00',1,0,1,1,'day before yesterday',0,0);
INSERT INTO `sfl_term_token` VALUES (26,10,'before',0,'40.00',1,1,0,1,'day before yesterday',0,0);
INSERT INTO `sfl_term_token` VALUES (27,10,'yesterday',0,'20.00',1,2,1,1,'day before yesterday',0,0);
INSERT INTO `sfl_term_token` VALUES (28,11,'cubic',0,'80.00',1,0,1,1,'Cubic Feet',2,1);
INSERT INTO `sfl_term_token` VALUES (29,11,'feet',0,'20.00',1,1,1,1,'Cubic Feet',2,1);
INSERT INTO `sfl_term_token` VALUES (30,12,'cu',0,'80.00',1,0,1,1,'cu ft',2,1);
INSERT INTO `sfl_term_token` VALUES (31,12,'ft',0,'20.00',1,1,1,1,'cu ft',2,1);
INSERT INTO `sfl_term_token` VALUES (32,13,'square',0,'50.00',1,0,0,1,'square meter',2,1);
INSERT INTO `sfl_term_token` VALUES (33,13,'meter',0,'50.00',1,1,1,1,'square meter',2,1);
INSERT INTO `sfl_term_token` VALUES (34,14,'sq',0,'50.00',1,0,0,1,'sq m',2,1);
INSERT INTO `sfl_term_token` VALUES (35,14,'m',0,'50.00',1,1,1,1,'sq m',2,1);
INSERT INTO `sfl_term_token` VALUES (36,15,'sq',0,'50.00',1,0,0,1,'sq in',2,1);
INSERT INTO `sfl_term_token` VALUES (37,15,'in',0,'50.00',1,1,1,1,'sq in',2,1);
INSERT INTO `sfl_term_token` VALUES (38,16,'square',0,'50.00',1,0,0,1,'square inch',2,1);
INSERT INTO `sfl_term_token` VALUES (39,16,'inch',0,'50.00',1,1,1,1,'square inch',2,1);
INSERT INTO `sfl_term_token` VALUES (40,17,'square',0,'50.00',1,0,0,1,'square feet',2,1);
INSERT INTO `sfl_term_token` VALUES (41,17,'feet',0,'50.00',1,1,1,1,'square feet',2,1);
INSERT INTO `sfl_term_token` VALUES (42,18,'sq',0,'50.00',1,0,0,1,'sq ft',2,1);
INSERT INTO `sfl_term_token` VALUES (43,18,'ft',0,'50.00',1,1,1,1,'sq ft',2,1);
INSERT INTO `sfl_term_token` VALUES (44,19,'square',0,'50.00',1,0,0,1,'square yard',2,1);
INSERT INTO `sfl_term_token` VALUES (45,19,'yard',0,'50.00',1,1,1,1,'square yard',2,1);
INSERT INTO `sfl_term_token` VALUES (46,20,'sq',0,'50.00',1,0,0,1,'sq yd',2,1);
INSERT INTO `sfl_term_token` VALUES (47,20,'yd',0,'50.00',1,1,1,1,'sq yd',2,1);
INSERT INTO `sfl_term_token` VALUES (48,21,'kilo',0,'50.00',1,0,1,1,'Kilo Bytes',2,1);
INSERT INTO `sfl_term_token` VALUES (49,21,'bytes',0,'50.00',1,1,0,1,'Kilo Bytes',2,1);
INSERT INTO `sfl_term_token` VALUES (50,22,'giga',0,'50.00',1,0,1,1,'Giga Bytes',2,1);
INSERT INTO `sfl_term_token` VALUES (51,22,'bytes',0,'50.00',1,1,0,1,'Giga Bytes',2,1);
INSERT INTO `sfl_term_token` VALUES (52,23,'mega',0,'50.00',1,0,1,1,'Mega Bytes',2,1);
INSERT INTO `sfl_term_token` VALUES (53,23,'bytes',0,'50.00',1,1,0,1,'Mega Bytes',2,1);
INSERT INTO `sfl_term_token` VALUES (54,24,'mega',0,'50.00',1,0,1,1,'Mega Pixels',2,1);
INSERT INTO `sfl_term_token` VALUES (55,24,'pixels',0,'50.00',1,1,0,1,'Mega Pixels',2,1);
/*!40000 ALTER TABLE `sfl_term_token` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `stat`
--

DROP TABLE IF EXISTS `stat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `stat` (
  `ID` bigint(20) NOT NULL,
  `NAME` varchar(35) NOT NULL,
  `DISPLAY_NAME` varchar(55) NOT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `BUSINESS_ENTITY_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `IDX_ST_BEID` (`BUSINESS_ENTITY_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `stat`
--

LOCK TABLES `stat` WRITE;
/*!40000 ALTER TABLE `stat` DISABLE KEYS */;
INSERT INTO `stat` VALUES (1,'COUNT','Count','Count of data elements',1003);
INSERT INTO `stat` VALUES (2,'SUM','Sum','Summation on data elements',1001);
INSERT INTO `stat` VALUES (3,'AVG','Average','Average of data elements',1002);
INSERT INTO `stat` VALUES (4,'STDDEV_POP','Standard Deviation','Standard Deviation of data elements',1006);
INSERT INTO `stat` VALUES (5,'MIN','Minimum','Minimum of data elements',1004);
INSERT INTO `stat` VALUES (6,'MAX','Maximum','Maximum of data elements',1005);
/*!40000 ALTER TABLE `stat` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `stat_function`
--

DROP TABLE IF EXISTS `stat_function`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `stat_function` (
  `STAT_ID` bigint(20) NOT NULL,
  `PROVIDER` varchar(35) DEFAULT NULL,
  `FUNCTION_NAME` varchar(35) DEFAULT NULL,
  `FUNCTION_DESCRIPTION` varchar(255) DEFAULT NULL,
  KEY `FK_SF_SID` (`STAT_ID`),
  KEY `IDX_SF_PRV` (`PROVIDER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `stat_function`
--

LOCK TABLES `stat_function` WRITE;
/*!40000 ALTER TABLE `stat_function` DISABLE KEYS */;
/*!40000 ALTER TABLE `stat_function` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `system_info`
--

DROP TABLE IF EXISTS `system_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `system_info` (
  `ID` bigint(20) NOT NULL,
  `PARAM` varchar(255) NOT NULL,
  `VALUE` varchar(255) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `system_info`
--

LOCK TABLES `system_info` WRITE;
/*!40000 ALTER TABLE `system_info` DISABLE KEYS */;
INSERT INTO `system_info` VALUES (1,'APPLICATION_NAME','Company Financials');
INSERT INTO `system_info` VALUES (2,'APPLICATION_FIRST_DEPLOYED_ON','June 19 2009');
INSERT INTO `system_info` VALUES (101,'PLATFORM_VERSION','4.0.0');
INSERT INTO `system_info` VALUES (102,'PLATFORM_PATCH_LEVEL','0.0.0');
INSERT INTO `system_info` VALUES (103,'PLATFORM_PATCH_LEVEL_APPLIED_ON','July 04 2009');
INSERT INTO `system_info` VALUES (104,'APPLICATION_VERSION','4.0');
INSERT INTO `system_info` VALUES (105,'APPLICATION_PATCH_LEVEL','0.0.0');
INSERT INTO `system_info` VALUES (106,'APPLICATION_PATCH_LEVEL_APPLIED_ON','July 04 2009');
INSERT INTO `system_info` VALUES (107,'SWI_SCHEMA_PATCH_LEVEL','4');
INSERT INTO `system_info` VALUES (108,'SWI_SCHEMA_PATCH_LEVEL_APPLIED_ON','July 04 2009');
INSERT INTO `system_info` VALUES (109,'QDATA_SCHEMA_PATCH_LEVEL','1');
INSERT INTO `system_info` VALUES (110,'QDATA_SCHEMA_PATCH_LEVEL_APPLIED_ON','July 04 2009');
/*!40000 ALTER TABLE `system_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `system_variable`
--

DROP TABLE IF EXISTS `system_variable`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `system_variable` (
  `ID` bigint(20) NOT NULL,
  `word` varchar(50) DEFAULT NULL,
  `entity_type` varchar(5) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM AUTO_INCREMENT=73 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `system_variable`
--

LOCK TABLES `system_variable` WRITE;
/*!40000 ALTER TABLE `system_variable` DISABLE KEYS */;
INSERT INTO `system_variable` VALUES (1,'MeasurableEntity','T');
INSERT INTO `system_variable` VALUES (2,'Identity','T');
INSERT INTO `system_variable` VALUES (3,'UnitSymbol','T');
INSERT INTO `system_variable` VALUES (4,'Unit','T');
INSERT INTO `system_variable` VALUES (5,'TimeFrame','T');
INSERT INTO `system_variable` VALUES (6,'Year','T');
INSERT INTO `system_variable` VALUES (7,'Quarter','T');
INSERT INTO `system_variable` VALUES (8,'Month','T');
INSERT INTO `system_variable` VALUES (9,'Week','T');
INSERT INTO `system_variable` VALUES (10,'Day','T');
INSERT INTO `system_variable` VALUES (11,'Hour','T');
INSERT INTO `system_variable` VALUES (12,'Minute','T');
INSERT INTO `system_variable` VALUES (13,'Second','T');
INSERT INTO `system_variable` VALUES (14,'Mass','T');
INSERT INTO `system_variable` VALUES (15,'Location','T');
INSERT INTO `system_variable` VALUES (16,'Country','T');
INSERT INTO `system_variable` VALUES (17,'State','T');
INSERT INTO `system_variable` VALUES (18,'City','T');
INSERT INTO `system_variable` VALUES (19,'County','T');
INSERT INTO `system_variable` VALUES (20,'Street','T');
INSERT INTO `system_variable` VALUES (21,'Zip','T');
INSERT INTO `system_variable` VALUES (22,'Address','T');
INSERT INTO `system_variable` VALUES (23,'Region','T');
INSERT INTO `system_variable` VALUES (24,'Distance','T');
INSERT INTO `system_variable` VALUES (25,'Text','T');
INSERT INTO `system_variable` VALUES (26,'SubordinatingConjunction','T');
INSERT INTO `system_variable` VALUES (27,'OntoEntity','T');
INSERT INTO `system_variable` VALUES (28,'AbstractTime','T');
INSERT INTO `system_variable` VALUES (29,'RangePreposition','T');
INSERT INTO `system_variable` VALUES (30,'MeasuresProfile','T');
INSERT INTO `system_variable` VALUES (31,'Name','T');
INSERT INTO `system_variable` VALUES (32,'Punctuation','T');
INSERT INTO `system_variable` VALUES (33,'Value','T');
INSERT INTO `system_variable` VALUES (34,'Time','T');
INSERT INTO `system_variable` VALUES (35,'Statistics','RT');
INSERT INTO `system_variable` VALUES (36,'ComparativeStatistics','RT');
INSERT INTO `system_variable` VALUES (37,'Operator','RT');
INSERT INTO `system_variable` VALUES (38,'UnitScale','RT');
INSERT INTO `system_variable` VALUES (39,'Number','RT');
INSERT INTO `system_variable` VALUES (40,'Adjective','RT');
INSERT INTO `system_variable` VALUES (41,'Conjunction','RT');
INSERT INTO `system_variable` VALUES (42,'Preposition','RT');
INSERT INTO `system_variable` VALUES (43,'ByConjunction','RT');
INSERT INTO `system_variable` VALUES (44,'CoordinatingConjunction','RT');
INSERT INTO `system_variable` VALUES (45,'ScalingFactor','RT');
INSERT INTO `system_variable` VALUES (46,'ComparativeInformation','RT');
INSERT INTO `system_variable` VALUES (47,'ValuePreposition','RT');
INSERT INTO `system_variable` VALUES (48,'Power','RT');
INSERT INTO `system_variable` VALUES (49,'Volume','RT');
INSERT INTO `system_variable` VALUES (50,'UnitSymbol','RT');
INSERT INTO `system_variable` VALUES (51,'UnitSymbol','RT');
INSERT INTO `system_variable` VALUES (52,'UnitSymbol','RT');
INSERT INTO `system_variable` VALUES (53,'TimeQualifier','RT');
INSERT INTO `system_variable` VALUES (54,'Currency','C');
INSERT INTO `system_variable` VALUES (55,'CurrencySymbol','C');
INSERT INTO `system_variable` VALUES (56,'DistanceSymbol','C');
INSERT INTO `system_variable` VALUES (57,'Distance','C');
INSERT INTO `system_variable` VALUES (58,'Volume','C');
INSERT INTO `system_variable` VALUES (59,'Power','C');
INSERT INTO `system_variable` VALUES (60,'Number','C');
INSERT INTO `system_variable` VALUES (61,'Time','C');
INSERT INTO `system_variable` VALUES (62,'Temperature','C');
INSERT INTO `system_variable` VALUES (63,'Percentage','C');
INSERT INTO `system_variable` VALUES (64,'hasStatistics','R');
INSERT INTO `system_variable` VALUES (65,'hasValue','R');
INSERT INTO `system_variable` VALUES (66,'hasTimeframe','R');
INSERT INTO `system_variable` VALUES (67,'hasLocation','R');
INSERT INTO `system_variable` VALUES (68,'hasPeriodicInformation','R');
INSERT INTO `system_variable` VALUES (69,'parentResource','R');
INSERT INTO `system_variable` VALUES (70,'isComposedOf','R');
INSERT INTO `system_variable` VALUES (71,'isConvertableTo','R');
INSERT INTO `system_variable` VALUES (72,'isRealizedAs','R');
/*!40000 ALTER TABLE `system_variable` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tabl`
--

DROP TABLE IF EXISTS `tabl`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tabl` (
  `ID` bigint(20) NOT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `ACTUAL_NAME` varchar(255) DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `ALIAC` varchar(5) DEFAULT NULL,
  `LOOKUP_TYPE` varchar(10) DEFAULT NULL,
  `LOOKUP_VALUE_COLUMN` varchar(255) DEFAULT NULL,
  `LOOKUP_DESC_COLUMN` varchar(255) DEFAULT NULL,
  `LOWER_LIMIT_COLUMN` varchar(255) DEFAULT NULL,
  `UPPER_LIMIT_COLUMN` varchar(255) DEFAULT NULL,
  `PARENT_TABLE` varchar(255) DEFAULT NULL,
  `PARENT_COLUMN` varchar(255) DEFAULT NULL,
  `AGGREGATED` char(1) DEFAULT 'N',
  `VIRTUAL` char(1) DEFAULT 'N',
  `OWNER` varchar(70) DEFAULT NULL,
  `PARENT_TABLE_ID` bigint(20) DEFAULT NULL,
  `DISPLAY_NAME` varchar(255) NOT NULL,
  `eligible_default_metric` char(1) NOT NULL DEFAULT 'Y',
  `INDICATORS` char(1) DEFAULT 'N',
  `virt_tab_des_c_ets_on_src` char(1) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `IDX_TBL_LT` (`LOOKUP_TYPE`),
  KEY `IDX_TBL_NM` (`NAME`),
  KEY `IDX_TBL_VF` (`VIRTUAL`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tabl`
--

LOCK TABLES `tabl` WRITE;
/*!40000 ALTER TABLE `tabl` DISABLE KEYS */;
/*!40000 ALTER TABLE `tabl` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `type`
--

DROP TABLE IF EXISTS `type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `type` (
  `ID` bigint(20) NOT NULL,
  `NAME` varchar(255) NOT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `DISPLAY_NAME` varchar(255) NOT NULL,
  `OWNER` int(2) DEFAULT NULL,
  `abstract` char(1) DEFAULT 'N',
  `VISIBILITY` char(1) DEFAULT 'N',
  `REALIZABLE` char(1) NOT NULL DEFAULT 'Y',
  PRIMARY KEY (`ID`),
  KEY `NAME_IDX` (`NAME`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `type`
--

LOCK TABLES `type` WRITE;
/*!40000 ALTER TABLE `type` DISABLE KEYS */;
INSERT INTO `type` VALUES (101,'MeasurableEntity',NULL,'Measurable Entity',0,'N','Y','Y');
INSERT INTO `type` VALUES (102,'Identity',NULL,'Identity',0,'N','Y','Y');
INSERT INTO `type` VALUES (103,'Statistics',NULL,'Statistics',0,'N','N','Y');
INSERT INTO `type` VALUES (104,'ComparativeStatistics',NULL,'Comparative Statistics',0,'N','N','Y');
INSERT INTO `type` VALUES (105,'Operator',NULL,'Operator',0,'N','N','Y');
INSERT INTO `type` VALUES (106,'UnitScale',NULL,'Unit Scale',0,'N','N','N');
INSERT INTO `type` VALUES (107,'UnitSymbol',NULL,'Unit Symbol',0,'N','N','N');
INSERT INTO `type` VALUES (151,'Unit',NULL,'Unit',0,'N','N','N');
INSERT INTO `type` VALUES (152,'Value',NULL,'Value',0,'N','N','Y');
INSERT INTO `type` VALUES (201,'TimeFrame',NULL,'TimeFrame',0,'Y','Y','Y');
INSERT INTO `type` VALUES (202,'Year',NULL,'Year',0,'N','N','N');
INSERT INTO `type` VALUES (203,'Quarter',NULL,'Quarter',0,'N','N','N');
INSERT INTO `type` VALUES (204,'Month',NULL,'Month',0,'N','N','N');
INSERT INTO `type` VALUES (205,'Week',NULL,'Week',0,'N','N','N');
INSERT INTO `type` VALUES (206,'Day',NULL,'Day',0,'N','N','N');
INSERT INTO `type` VALUES (207,'Hour',NULL,'Hour',0,'N','N','N');
INSERT INTO `type` VALUES (208,'Minute',NULL,'Minute',0,'N','N','N');
INSERT INTO `type` VALUES (209,'Second',NULL,'Second',0,'N','N','N');
INSERT INTO `type` VALUES (251,'Mass',NULL,'Mass',0,'N','N','Y');
INSERT INTO `type` VALUES (301,'Location',NULL,'Location',0,'Y','Y','Y');
INSERT INTO `type` VALUES (302,'Country',NULL,'Country',0,'N','N','Y');
INSERT INTO `type` VALUES (303,'State',NULL,'State',0,'N','Y','Y');
INSERT INTO `type` VALUES (304,'City',NULL,'City',0,'N','Y','Y');
INSERT INTO `type` VALUES (305,'County',NULL,'County',0,'N','Y','Y');
INSERT INTO `type` VALUES (306,'Street',NULL,'Street',0,'N','N','Y');
INSERT INTO `type` VALUES (307,'Zip',NULL,'Zip',0,'N','Y','Y');
INSERT INTO `type` VALUES (308,'Address',NULL,'Address',0,'N','Y','Y');
INSERT INTO `type` VALUES (309,'Region',NULL,'Region',0,'N','N','Y');
INSERT INTO `type` VALUES (401,'Digit',NULL,'Digit',0,'N','N','N');
INSERT INTO `type` VALUES (402,'Text',NULL,'Text',0,'N','N','Y');
INSERT INTO `type` VALUES (403,'Adjective',NULL,'Adjective',0,'N','N','Y');
INSERT INTO `type` VALUES (404,'Conjunction',NULL,'Conjunction',0,'N','N','Y');
INSERT INTO `type` VALUES (405,'Preposition',NULL,'Preposition',0,'N','N','Y');
INSERT INTO `type` VALUES (406,'ByConjunction',NULL,'ByConjunction',0,'N','N','Y');
INSERT INTO `type` VALUES (407,'SubordinatingConjunction',NULL,'SubordinatingConjunction',0,'N','N','Y');
INSERT INTO `type` VALUES (408,'CoordinatingConjunction',NULL,'CoordinatingConjunction',0,'N','N','Y');
INSERT INTO `type` VALUES (108,'OntoEntity',NULL,'Onto Entity',0,'N','Y','Y');
INSERT INTO `type` VALUES (210,'AbstractTime',NULL,'Abstract Time',0,'N','N','Y');
INSERT INTO `type` VALUES (409,'RangePreposition',NULL,'RangePreposition',0,'N','N','N');
INSERT INTO `type` VALUES (109,'ScalingFactor',NULL,'Scaling Factor',0,'N','N','Y');
INSERT INTO `type` VALUES (110,'ComparativeInformation',NULL,'Comparative Information',0,'N','N','Y');
INSERT INTO `type` VALUES (111,'MeasuresProfile',NULL,'Measures Profile',0,'N','Y','Y');
INSERT INTO `type` VALUES (112,'Name',NULL,'Name',0,'N','Y','Y');
INSERT INTO `type` VALUES (410,'ValuePreposition',NULL,'ValuePreposition',0,'N','N','N');
INSERT INTO `type` VALUES (411,'Punctuation',NULL,'Punctuation',0,'N','N','Y');
INSERT INTO `type` VALUES (211,'Time','Time','Time',NULL,'N','N','N');
INSERT INTO `type` VALUES (412,'TimeQualifier','Time Qualifier','Time Qualifier',0,'N','N','N');
INSERT INTO `type` VALUES (413,'TimePreposition',NULL,'Time Preposition',0,'N','N','N');
INSERT INTO `type` VALUES (213,'WeekDay','WeekDay','WeekDay',0,'N','N','N');
/*!40000 ALTER TABLE `type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `type_rule`
--

DROP TABLE IF EXISTS `type_rule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `type_rule` (
  `TYPE_BE_ID` bigint(20) NOT NULL,
  `RULE_ID` bigint(20) NOT NULL,
  KEY `FK_TYPE_BE_ID` (`TYPE_BE_ID`),
  KEY `FK_RULE_ID` (`RULE_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `type_rule`
--

LOCK TABLES `type_rule` WRITE;
/*!40000 ALTER TABLE `type_rule` DISABLE KEYS */;
/*!40000 ALTER TABLE `type_rule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `unstructured_app_detail`
--

DROP TABLE IF EXISTS `unstructured_app_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `unstructured_app_detail` (
  `ID` bigint(20) NOT NULL,
  `APP_ID` bigint(20) NOT NULL,
  `FACET_NATURE` varchar(35) NOT NULL DEFAULT 'COMBO',
  `LOCATION_FROM_CONTENT` char(1) NOT NULL DEFAULT 'N',
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `unstructured_app_detail`
--

LOCK TABLES `unstructured_app_detail` WRITE;
/*!40000 ALTER TABLE `unstructured_app_detail` DISABLE KEYS */;
/*!40000 ALTER TABLE `unstructured_app_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_data_source`
--

DROP TABLE IF EXISTS `user_data_source`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_data_source` (
  `USER_ID` bigint(20) NOT NULL,
  `DATA_SOURCE_ID` bigint(20) NOT NULL,
  KEY `USER_DATA_SOURCE_USER_ID` (`USER_ID`),
  KEY `IDX_USR_DS_DATASRCID` (`DATA_SOURCE_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_data_source`
--

LOCK TABLES `user_data_source` WRITE;
/*!40000 ALTER TABLE `user_data_source` DISABLE KEYS */;
INSERT INTO `user_data_source` VALUES (1,1);
INSERT INTO `user_data_source` VALUES (1,2);
INSERT INTO `user_data_source` VALUES (3,1);
INSERT INTO `user_data_source` VALUES (3,2);
/*!40000 ALTER TABLE `user_data_source` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_query_possibility`
--

DROP TABLE IF EXISTS `user_query_possibility`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_query_possibility` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `MODEL_ID` bigint(20) NOT NULL,
  `POSSIBILITY_ID` bigint(20) NOT NULL,
  `ENTITY_BED_ID` bigint(20) NOT NULL,
  `ENTITY_TYPE` varchar(5) DEFAULT NULL,
  `REC_WEIGHT` decimal(10,2) DEFAULT NULL,
  `MAX_POSSIBLE_WEIGHT` decimal(10,2) DEFAULT NULL,
  `TYPE_BASED_WEIGHT` decimal(10,2) DEFAULT NULL,
  `MEASURE_GROUP_BY` char(1) DEFAULT 'N',
  `MEASURE_CONDITION_WITHOUT_STAT` char(1) DEFAULT 'N',
  `USER_QUERY_ID` bigint(20) NOT NULL,
  `EXECUTION_DATE` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_query_possibility`
--

LOCK TABLES `user_query_possibility` WRITE;
/*!40000 ALTER TABLE `user_query_possibility` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_query_possibility` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_request`
--

DROP TABLE IF EXISTS `user_request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_request` (
  `ID` bigint(20) NOT NULL,
  `USER_ID` bigint(20) DEFAULT NULL,
  `FIRST_NAME` varchar(255) DEFAULT NULL,
  `LAST_NAME` varchar(255) DEFAULT NULL,
  `JOB_TITLE` varchar(55) DEFAULT NULL,
  `ORGANIZATION` varchar(55) DEFAULT NULL,
  `SUBJECT` varchar(255) DEFAULT NULL,
  `NOTES` varchar(255) DEFAULT NULL,
  `EMAIL_ID` varchar(255) DEFAULT NULL,
  `CONTACT_PHONE_NUM` varchar(25) DEFAULT NULL,
  `REGION` varchar(55) DEFAULT NULL,
  `USER_REQUEST_TYPE` varchar(25) DEFAULT NULL,
  `comments` varchar(255) DEFAULT NULL,
  `ACCEPT_REJECT_REQUEST` char(1) DEFAULT NULL,
  `UPDATE_NOTIFICATION` char(1) DEFAULT 'N',
  PRIMARY KEY (`ID`),
  KEY `UR_FK_USER_ID` (`USER_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_request`
--

LOCK TABLES `user_request` WRITE;
/*!40000 ALTER TABLE `user_request` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_request` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_security_groups`
--

DROP TABLE IF EXISTS `user_security_groups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_security_groups` (
  `USER_ID` bigint(20) NOT NULL,
  `GROUP_ID` bigint(20) NOT NULL,
  KEY `FK_UG_UID` (`USER_ID`),
  KEY `FK_UG_GID` (`GROUP_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_security_groups`
--

LOCK TABLES `user_security_groups` WRITE;
/*!40000 ALTER TABLE `user_security_groups` DISABLE KEYS */;
INSERT INTO `user_security_groups` VALUES (1,1);
INSERT INTO `user_security_groups` VALUES (1,2);
INSERT INTO `user_security_groups` VALUES (2,2);
INSERT INTO `user_security_groups` VALUES (3,3);
/*!40000 ALTER TABLE `user_security_groups` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `ID` bigint(20) NOT NULL,
  `USERNAME` varchar(100) NOT NULL,
  `PASSWORD` varchar(255) NOT NULL,
  `SALT` varchar(50) DEFAULT NULL,
  `FIRST_NAME` varchar(55) NOT NULL,
  `LAST_NAME` varchar(55) NOT NULL,
  `FULL_NAME` varchar(255) DEFAULT NULL,
  `EMAIL_ID` varchar(255) DEFAULT NULL,
  `PASSWORD_TRIES` int(2) DEFAULT NULL,
  `STATUS` char(1) NOT NULL DEFAULT 'D',
  `CHANGE_PASSWORD` int(1) DEFAULT NULL,
  `LAST_LOGIN_DATE` datetime DEFAULT NULL,
  `DATE_CREATED` datetime DEFAULT NULL,
  `DATE_MODIFIED` datetime DEFAULT NULL,
  `ENCRYPTED_KEY` varchar(255) DEFAULT NULL,
  `GROUP_TYPE` int(2) DEFAULT NULL,
  `IS_PUBLISHER` char(1) DEFAULT 'N',
  `ADDRESS1` varchar(255) DEFAULT NULL,
  `ADDRESS2` varchar(255) DEFAULT NULL,
  `CITY` varchar(20) DEFAULT NULL,
  `STATE` varchar(20) DEFAULT NULL,
  `ZIP` varchar(7) DEFAULT NULL,
  `COUNTRY` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_UN` (`USERNAME`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'system','54b53072540eeeb8f8e9343e71f28176',NULL,'System','System','System System','info@execue.com',0,'A',0,NULL,NULL,NULL,NULL,NULL,'Y',NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `users` VALUES (2,'user1','7c6a180b36896a0a8c02787eeafb0e4c',NULL,'user','one','user one','info@execue.com',0,'A',0,NULL,NULL,NULL,NULL,NULL,'N',NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `users` VALUES (3,'execue','6191ef9abcbe5197591fa49066fdf447',NULL,'ExeCue','Inc','ExeCue Inc','info@execue.com',0,'A',0,NULL,NULL,NULL,NULL,NULL,'Y',NULL,NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vertical`
--

DROP TABLE IF EXISTS `vertical`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `vertical` (
  `ID` bigint(20) NOT NULL,
  `VERTICAL_NAME` varchar(55) DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `URL` varchar(255) DEFAULT NULL,
  `IMPORTANCE` int(4) DEFAULT '1',
  `POPULARITY` int(10) DEFAULT '0',
  `HOMEPAGE_VISIBILITY` char(1) DEFAULT 'N',
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vertical`
--

LOCK TABLES `vertical` WRITE;
/*!40000 ALTER TABLE `vertical` DISABLE KEYS */;
INSERT INTO `vertical` VALUES (1001,'Finance',NULL,NULL,1,0,'N');
INSERT INTO `vertical` VALUES (1002,'Government',NULL,NULL,1,0,'N');
INSERT INTO `vertical` VALUES (1003,'Others',NULL,NULL,1,0,'N');
INSERT INTO `vertical` VALUES (1004,'Advertisement',NULL,NULL,1,0,'N');
/*!40000 ALTER TABLE `vertical` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vertical_app_example`
--

DROP TABLE IF EXISTS `vertical_app_example`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `vertical_app_example` (
  `ID` bigint(20) NOT NULL,
  `VERTICAL_ID` bigint(20) NOT NULL,
  `QUERY_EXAMPLE` varchar(255) DEFAULT NULL,
  `APPLICATION_ID` bigint(20) DEFAULT NULL,
  `DAY` int(4) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `IDX_VAE_AID` (`APPLICATION_ID`),
  KEY `IDX_VAE_VID` (`VERTICAL_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vertical_app_example`
--

LOCK TABLES `vertical_app_example` WRITE;
/*!40000 ALTER TABLE `vertical_app_example` DISABLE KEYS */;
/*!40000 ALTER TABLE `vertical_app_example` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vertical_app_weight`
--

DROP TABLE IF EXISTS `vertical_app_weight`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `vertical_app_weight` (
  `ID` bigint(20) NOT NULL,
  `APPLICATION_ID` bigint(20) NOT NULL,
  `WEIGHT` double(5,2) DEFAULT NULL,
  `VERTICAL_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `IDX_VAW_AID` (`APPLICATION_ID`),
  KEY `IDX_VAW_VID` (`VERTICAL_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vertical_app_weight`
--

LOCK TABLES `vertical_app_weight` WRITE;
/*!40000 ALTER TABLE `vertical_app_weight` DISABLE KEYS */;
INSERT INTO `vertical_app_weight` VALUES (1,101,1.00,NULL);
INSERT INTO `vertical_app_weight` VALUES (2,748,1.00,NULL);
/*!40000 ALTER TABLE `vertical_app_weight` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vertical_entity_redirect`
--

DROP TABLE IF EXISTS `vertical_entity_redirect`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `vertical_entity_redirect` (
  `ID` bigint(20) NOT NULL,
  `APPLICAION_ID` bigint(20) DEFAULT NULL,
  `BED_ID` bigint(20) DEFAULT NULL,
  `ENTITY_TYPE` varchar(30) DEFAULT NULL,
  `URL` varchar(255) DEFAULT NULL,
  `VERTICAL_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `VERTICAL_ID` (`VERTICAL_ID`),
  KEY `IDX_VED_AID` (`APPLICAION_ID`),
  KEY `IDX_VED_BEID` (`BED_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vertical_entity_redirect`
--

LOCK TABLES `vertical_entity_redirect` WRITE;
/*!40000 ALTER TABLE `vertical_entity_redirect` DISABLE KEYS */;
/*!40000 ALTER TABLE `vertical_entity_redirect` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2012-11-11 20:54:34
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
