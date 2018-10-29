/**
 * Licensed to the Execue Software Foundation (ESF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ESF licenses this file
 * to you under the Execue License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. 
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.execue.core.configuration.impl;

import java.util.Hashtable;
import java.util.List;

import org.apache.log4j.Logger;

import com.execue.core.configuration.IConfiguration;
import com.execue.core.configuration.ICoreConfigurationService;
import com.execue.core.system.ExecueSystem;
import com.execue.core.type.SystemType;

public class CoreConfigurationServiceImpl implements ICoreConfigurationService {

   private static final Logger       log                                                      = Logger
                                                                                                       .getLogger(CoreConfigurationServiceImpl.class);

   private IConfiguration            coreConfiguration;
   private IConfiguration            coreDBConfiguration;

   private Hashtable<String, String> initialContextEnvironment;
   private String                    applicationName;

   /**
    * This key will retrieve the path of the jobs configuration properties file
    */
   private static final String       JOBS_CONFIG_PROPERTIES_FILE_KEY                          = "core.files.jobs-config";
   private static final String       FLAGS_PRINTSTACK_TRACE_KEY                               = "core.flags.printTrace";
   private static final String       ENCRYPTION_CONFIGURATION_KEY                             = "core.encryption.configuration";
   private static final String       ENCRYPTION_METADATA_KEY                                  = "core.encryption.metaData";
   private static final String       MASKING_METASCHEMA_KEY                                   = "core.masking.metaSchema";
   private static final String       SYSTEM_LOGGING_LOG_KEY                                   = "core.logging.log";
   private static final String       APP_SCOPING_ENABLED                                      = "core.static-values.app-scoping-enabled";
   // private static final String APP_SCOPING_REFRESH_INDEX_ENABLED =
   // "core.static-values.app-scoping-refresh-index-enabled";

   /**
    * This key will retrieve the count value which is used to determine the type of a column
    */
   private static final String       DIMENSION_THRESHOLD                                      = "core.static-values.dimension-threshold";

   /**
    * This key will retrieve the maximum count value used to determine the type of a column
    */
   private static final String       SYSTEM_MAX_DIMENSION_VALUE                               = "core.static-values.system-max-dimension-value";

   private static final String       ORACLE_DEFAULT_DATE_FORMAT                               = "core.static-values.oracle-default-date-format";
   private static final String       MYSQL_DEFAULT_DATE_FORMAT                                = "core.static-values.mysql-default-date-format";
   private static final String       MSSQL_DEFAULT_DATE_FORMAT                                = "core.static-values.mssql-default-date-format";
   private static final String       TERADATA_DEFAULT_DATE_FORMAT                             = "core.static-values.teradata-default-date-format";
   private static final String       SAS_DEFAULT_DATE_FORMAT                                  = "core.static-values.sas-default-date-format";
   private static final String       ORACLE_DEFAULT_DATETIME_FORMAT                           = "core.static-values.oracle-default-datetime-format";
   private static final String       MYSQL_DEFAULT_DATETIME_FORMAT                            = "core.static-values.mysql-default-datetime-format";
   private static final String       MSSQL_DEFAULT_DATETIME_FORMAT                            = "core.static-values.mssql-default-datetime-format";
   private static final String       TERADATA_DEFAULT_DATETIME_FORMAT                         = "core.static-values.teradata-default-datetime-format";
   private static final String       SAS_DEFAULT_DATETIME_FORMAT                              = "core.static-values.sas-default-datetime-format";
   private static final String       POSTGRESQL_DEFAULT_DATE_FORMAT                           = "core.static-values.postgresql-default-date-format";
   private static final String       POSTGRESQL_DEFAULT_DATETIME_FORMAT                       = "core.static-values.postgresql-default-datetime-format";

   private static final String       XML_REQUEST                                              = "core.static-values.xml-request";
   private static final String       APPLICATION_RETRIVAL_LIMIT                               = "core.static-values.application-retrieval-limit";
   private static final String       APPLICATION_EXAMPLE_TRUNCATED_LENGTH                     = "core.static-values.application-example-truncated-char-length";
   private static final String       APP_SPECIFIC_EXAMPLE_TRUNCATED_LENGTH                    = "core.static-values.app-specific-example-truncated-char-length";
   private static final String       APP_SPECIFIC_EXAMPLE_RETRIVAL_LIMIT                      = "core.static-values.app-specific-example-retrieval-limit";
   private static final String       APPLICATION_EXAMPLE_RETRIVAL_LIMIT                       = "core.static-values.application-example-retrieval-limit";
   private static final String       APPLICATION_DESC_TRUNCATED_LENGTH                        = "core.static-values.application-description-truncated-char-length";

   private static final String       CONCEPT_RETRIVAL_LIMIT                                   = "core.static-values.concept-retrieval-limit";
   private static final String       INSTANCE_RETRIVAL_LIMIT                                  = "core.static-values.instance-retrieval-limit";
   private static final String       STOCK_CHART_VALID_APPS_FOR_STOCK_CHART                   = "core.static-values.stock-chart-valid-apps-combinations.app-concept.appId";
   private static final String       STOCK_CHART_VALID_CONCEPT_BE_IDS_FOR_STOCK_CHART         = "core.static-values.stock-chart-valid-apps-combinations.app-concept.conceptBedId";

   private static final String       UNIVERSAL_SEARCH_FROM_WAREHOUSE                          = "core.static-values.universal-search-from-warehouse";
   private static final String       SKIP_DERIVED_USER_QUERY_VARIATION                        = "core.skip-derived-user-query-variation.app-name";
   /**
    * Key to retrieve the maximum length of the private static final String that could be used as an Alias
    */
   private static final String       MAX_DB_OBJECT_LENGTH                                     = "core.static-values.max-db-object-length";
   private static final String       CUBE_ALL_VALUE_KEY                                       = "core.static-values.cube.cube-all-value";
   private static final String       STATISTICS_COLUMN_NAME_KEY                               = "core.static-values.cube.statistics-column-name";

   /**
    * keys to retrieve email information
    */
   private static final String       MAIL_SERVER_FROM_KEY                                     = "core.static-values.mail-server-from";
   private static final String       MAIL_SERVER_TO_KEY                                       = "core.static-values.mail-server-to";
   private static final String       MAIL_SERVER_HOST_NAME_KEY                                = "core.static-values.mail-server-host-name";
   private static final String       MAIL_SERVER_SUBJECT_KEY                                  = "core.static-values.mail-server-subject";
   private static final String       MAIL_SERVER_SUBJECT_ADVANCED_OPTIONS_KEY                 = "core.static-values.mail-server-subject-advanced-options-request";
   private static final String       MAIL_SERVER_USER_NAME_KEY                                = "core.static-values.mail-server-user-name";
   private static final String       MAIL_SERVER_PASSWORD_KEY                                 = "core.static-values.mail-server-password";
   private static final String       MAIL_SERVER_URL_KEY                                      = "core.static-values.mail-server-url";
   private static final String       MAIL_SERVER_USER_RESET_PASSWORD_URL_KEY                  = "core.static-values.mail-server-reset-password-url";
   private static final String       MAIL_SERVER_PUBLISHER_BODY_KEY                           = "core.static-values.mail-server-publisher-message-body";
   private static final String       MAIL_SERVER_FEEDBACK_BODY_KEY                            = "core.static-values.mail-server-feedback-message-body";
   private static final String       MAIL_SERVER_PUBLISHER_SUBJECT_KEY                        = "core.static-values.mail-server-publisher-subject";
   private static final String       MAIL_SERVER_FEEDBACK_SUBJECT_KEY                         = "core.static-values.mail-server-feedback-subject";
   private static final String       MAIL_SERVER_USER_ACTIVATION_BODY_KEY                     = "core.static-values.mail-server-user-activation-message-body";
   private static final String       MAIL_SERVER_USER_ADVANCED_OPTIONS_BODY_KEY               = "core.static-values.mail-server-user-advanced-options-message-body";
   private static final String       MAIL_SERVER_USER_RESET_PASSWORD_BODY_KEY                 = "core.static-values.mail-server-user-reset-password-message-body";
   private static final String       MAIL_SERVER_USER_RESET_PASSWORD_SUBJECT_KEY              = "core.static-values.mail-server-user-reset-password-subject";

   private static final String       MAIL_SERVER_USER_ADVANCED_OPTIONS_ACCEPT_REJECT_BODY_KEY = "core.static-values.mail-server-user-advanced-request-accept-reject-message-body";
   private static final String       MAIL_SERVER_USER_DEMO_REQUESTS_ACCEPT_REJECT_BODY_KEY    = "core.static-values.mail-server-user-demo-request-accept-reject-message-body";
   private static final String       MAIL_SERVER_USER_FEEDBACK_ACCEPT_REJECT_BODY_KEY         = "core.static-values.mail-server-user-feedback-accept-reject-message-body";
   private static final String       MAIL_SERVER_DEMO_REQUEST_SUBJECT_KEY                     = "core.static-values.mail-server-demorequest-subject";
   private static final String       MAX_USER_QUERY_LENGTH_KEY                                = "core.static-values.max-user-query-length";
   private static final String       RESULTS_PAGE_SIZE                                        = "core.static-values.page-size";
   private static final String       DETAIL_RESULTS_PAGE_SIZE                                 = "core.static-values.detail-page-size";
   private static final String       PREFIX_UPLOADED_TABLE_NAME                               = "core.static-values.prefix-uploaded-table-name";
   private static final String       PREFIX_UPLOADED_TEMP_TABLE_NAME                          = "core.static-values.prefix-uploaded-temp-table-name";
   private static final String       MAIL_SERVER_SEMANTIFI_ENTERPRISE_SUBJECT_KEY             = "core.static-values.mail-server-semantifi-enterprise-subject";
   private static final String       MAIL_SERVER_DEMO_REQUEST_BODY_KEY                        = "core.static-values.mail-server-demorequest-message-body";
   private static final String       MAIL_SERVER_SEMANTIFI_ENTERPRISE_REQUEST_BODY_KEY        = "core.static-values.mail-server-semantifi-enterprise-message-body";

   /**
    * Key to get max allowed size for suggest instances
    */
   private static final String       MAX_ALLOWED_INSTANCE_MAPPING_SUGGESTIONS                 = "core.static-values.max-allowed-instance-mapping-suggestions";

   /**
    * keys to retrieve ASSET Based Default recognition weights (at governor/driver)
    */
   private static final String       ASSET_BASED_CONCEPT_DEFAULT_RECOGNITION_WEIGHT           = "core.default-asset-based-recognition-weights.concept-weight";
   private static final String       ASSET_BASED_INSTANCE_DEFAULT_RECOGNITION_WEIGHT          = "core.default-asset-based-recognition-weights.instance-weight";
   private static final String       ASSET_BASED_RELATION_DEFAULT_RECOGNITION_WEIGHT          = "core.default-asset-based-recognition-weights.relation-weight";

   /**
    * keys to retrieve Model Based Default recognition weights
    */
   private static final String       MODEL_BASED_CONCEPT_DEFAULT_RECOGNITION_WEIGHT           = "core.default-model-based-recognition-weights.concept-weight";
   private static final String       MODEL_BASED_INSTANCE_DEFAULT_RECOGNITION_WEIGHT          = "core.default-model-based-recognition-weights.instance-weight";
   private static final String       MODEL_BASED_RELATION_DEFAULT_RECOGNITION_WEIGHT          = "core.default-model-based-recognition-weights.relation-weight";

   /**
    * Keys to get batch size for batch maintenaces services
    */
   private static final String       SDX_MEMBER_ABSORPTION_BATCH_SIZE_KEY                     = "core.batch-maintenance.batch-sizes.member-absorption";
   private static final String       KDX_INSTANCE_MAPPING_BATCH_SIZE_KEY                      = "core.batch-maintenance.batch-sizes.instance-mapping";

   /**
    * Default Data Source names
    */
   private static final String       DEFAULT_DATASOURCE_NAME_FOR_UPLOADED_DATASETS_KEY        = "core.static-values.default-datasource-uploaded";
   private static final String       DEFAULT_DATASOURCE_NAME_FOR_CATALOG_DATASETS_KEY         = "core.static-values.default-datasource-catalog";

   private static final String       RI_USER_QUERY_EXECUTION_TIME                             = "core.static-values.maintain-ri-user-query-execution-time-ms";

   /**
    * Flag to check the filtering of universal search results by value match
    */
   private static final String       FILTER_UNIVERSAL_SEARCH_RESULT_BY_VALUE_MATCH            = "core.static-values.filter-universal-search-result-by-value-match";
   private static final String       UNIVERSAL_SEARCH_MATCH_QUERY_LIMIT                       = "core.static-values.universal-search-match-query-limit";
   private static final String       UNIVERSAL_SEARCH_TOP_CLUSTER_LIMIT                       = "core.static-values.universal-search-top-cluster-limit";
   private static final String       THREAD_POOL_USE_AGGREGATION_PROCESSING                   = "core.static-values.thread-pool-use-aggregation-processing";
   private static final String       MESSAGE_CLEANUP_HOURS                                    = "core.static-values.message-cleanup-hours";
   private static final String       UNSTRUCTURED_APP_GROUPING_RESULT_COUNT                   = "core.static-values.unstructured-app-grouping-result-count";
   private static final String       MAX_DEFAULT_METRICS                                      = "core.static-values.max-default-metrics";
   private static final String       UNSTRUCTURED_APP_USER_VISIBILITY                         = "core.static-values.unstructured-app-user-visibility";
   private static final String       WEB_MAIN_CONTEXT                                         = "core.static-values.web-main-context";
   private static final String       WEB_ADMIN_CONTEXT                                        = "core.static-values.web-admin-context";
   private static final String       APPLICATION_SEARCH_URL_PREFIX                            = "core.static-values.application-search-url-prefix";
   private static final String       USER_REMOTE_LOCATION_URL                                 = "core.static-values.user-remote-location-url";
   private static final String       USER_REMOTE_LOCATION_CONNECT_TIMEOUT                     = "core.static-values.user-remote-location-connect-timeout";
   private static final String       USER_REMOTE_LOCATION_READ_TIMEOUT                        = "core.static-values.user-remote-location-read-timeout";
   private static final String       ADMIN_USER_ID                                            = "core.static-values.admin-user-id";
   private static final String       EXECUE_DATASOURCE_PASSWORD_ENCRYPTION_DECRYPTION_KEY     = "core.static-values.execue-datasource-password-encyption-decryption-key";
   private static final String       TOTAL_RANGE_BANDS_KEY                                    = "core.static-values.total-range-bands";
   private static final String       MAX_HIERARCHY_SIZE                                       = "core.static-values.max-hierarchy-size";

   private static final String       FORCE_POOLED_DATA_SOURCES_VIA_JNDI                       = "core.static-values.force-pooled-data-sources-via-jndi";

   private static final String       NLP_TAGGER_INITIATION_REQUIRED                           = "core.static-values.nlp-tagger-dictionary-initiation-required";
   /**
    * This key is used to get the dynamic range id
    */
   private static final String       DYNAMIC_RANGE_ID_KEY                                     = "core.static-values.dynamic-range-id";

   private static final String       GROUP_CONCAT_DB_SUPPORTED                                = "core.static-values.group-concat-db-supported";
   private static final String       SINGLE_ASSET_REDIRECT                                    = "core.static-values.single-asset-redirect";

   public Hashtable<String, String> getInitialContextEnvironment () {
      return initialContextEnvironment;
   }

   void setInitialContextEnvironment (Hashtable<String, String> initialContextEnvironment) {
      this.initialContextEnvironment = initialContextEnvironment;
   }

   void setApplicationName (String appName) {
      this.applicationName = appName;
   }

   @Override
   public Long getAdminUserId () {
      return getCoreConfiguration().getLong(ADMIN_USER_ID);
   }

   @Override
   public Integer getAppSpecificExampleRetrievalLimit () {
      return getCoreConfiguration().getInt(APP_SPECIFIC_EXAMPLE_RETRIVAL_LIMIT);
   }

   @Override
   public Integer getAppSpecificExampleTruncatedLength () {
      return getCoreConfiguration().getInt(APP_SPECIFIC_EXAMPLE_TRUNCATED_LENGTH);
   }

   @Override
   public Integer getApplicationDescriptionTruncatedLength () {
      return getCoreConfiguration().getInt(APPLICATION_DESC_TRUNCATED_LENGTH);
   }

   @Override
   public Integer getApplicationExampleRetrievalLimit () {
      return getCoreConfiguration().getInt(APPLICATION_EXAMPLE_RETRIVAL_LIMIT);
   }

   @Override
   public Integer getApplicationExampleTruncatedLength () {
      return getCoreConfiguration().getInt(APPLICATION_EXAMPLE_TRUNCATED_LENGTH);
   }

   @Override
   public Integer getApplicationRetrievalLimit () {
      return getCoreConfiguration().getInt(APPLICATION_RETRIVAL_LIMIT);

   }

   @Override
   public Integer getAssetBasedConceptDefaultRecognitionWeight () {
      return getCoreConfiguration().getInt(ASSET_BASED_CONCEPT_DEFAULT_RECOGNITION_WEIGHT);
   }

   @Override
   public Integer getAssetBasedInstanceDefaultRecognitionWeight () {
      return getCoreConfiguration().getInt(ASSET_BASED_INSTANCE_DEFAULT_RECOGNITION_WEIGHT);
   }

   @Override
   public Integer getAssetBasedRelationDefaultRecognitionWeight () {
      return getCoreConfiguration().getInt(ASSET_BASED_RELATION_DEFAULT_RECOGNITION_WEIGHT);
   }

   @Override
   public Integer getConceptRetrievalLimit () {
      return getCoreConfiguration().getInt(CONCEPT_RETRIVAL_LIMIT);
   }

   @Override
   public String getCubeAllValue () {
      return getCoreConfiguration().getProperty(CUBE_ALL_VALUE_KEY);
   }

   @Override
   public String getDefaultDataSourceNameForCatalogDatasets () {
      return getCoreConfiguration().getProperty(DEFAULT_DATASOURCE_NAME_FOR_CATALOG_DATASETS_KEY);
   }

   @Override
   public String getDefaultDataSourceNameForUploadedDatasets () {
      return getCoreConfiguration().getProperty(DEFAULT_DATASOURCE_NAME_FOR_UPLOADED_DATASETS_KEY);
   }

   @Override
   public Integer getDetailResultsPageSize () {
      return getCoreConfiguration().getInt(DETAIL_RESULTS_PAGE_SIZE);
   }

   @Override
   public Integer getDimensionTreshold () {
      return getCoreConfiguration().getInt(DIMENSION_THRESHOLD);
   }

   @Override
   public Integer getInstanceRetrievalLimit () {
      return getCoreConfiguration().getInt(INSTANCE_RETRIVAL_LIMIT);
   }

   @Override
   public String getJobsConfiguraion () {
      return getCoreConfiguration().getProperty(JOBS_CONFIG_PROPERTIES_FILE_KEY);
   }

   @Override
   public Integer getKDXInstanceMappingBatchSize () {
      return getCoreConfiguration().getInt(KDX_INSTANCE_MAPPING_BATCH_SIZE_KEY);
   }

   @Override
   public String getMailServerDemoRequestSubject () {
      return getCoreConfiguration().getProperty(MAIL_SERVER_DEMO_REQUEST_SUBJECT_KEY);
   }

   @Override
   public String getMailServerFeedbackAcceptRejectBody () {
      return getCoreConfiguration().getProperty(MAIL_SERVER_USER_FEEDBACK_ACCEPT_REJECT_BODY_KEY);
   }

   @Override
   public String getMailServerFeedbackBody () {
      return getCoreConfiguration().getProperty(MAIL_SERVER_FEEDBACK_BODY_KEY);
   }

   @Override
   public String getMailServerFeedbackSubject () {
      return getCoreConfiguration().getProperty(MAIL_SERVER_FEEDBACK_SUBJECT_KEY);
   }

   @Override
   public String getMailServerFrom () {
      return getCoreConfiguration().getProperty(MAIL_SERVER_FROM_KEY);
   }

   @Override
   public String getMailServerHostName () {
      return getCoreConfiguration().getProperty(MAIL_SERVER_HOST_NAME_KEY);
   }

   @Override
   public String getMailServerPassword () {
      return getCoreConfiguration().getProperty(MAIL_SERVER_PASSWORD_KEY);
   }

   @Override
   public String getMailServerPublisherBody () {
      return getCoreConfiguration().getProperty(MAIL_SERVER_PUBLISHER_BODY_KEY);
   }

   @Override
   public String getMailServerPublisherSubject () {
      return getCoreConfiguration().getProperty(MAIL_SERVER_PUBLISHER_SUBJECT_KEY);
   }

   @Override
   public String getMailServerSubject () {
      return getCoreConfiguration().getProperty(MAIL_SERVER_SUBJECT_KEY);
   }

   @Override
   public String getMailServerSubjectAdvancedOptions () {
      return getCoreConfiguration().getProperty(MAIL_SERVER_SUBJECT_ADVANCED_OPTIONS_KEY);
   }

   @Override
   public String getMailServerTo () {
      return getCoreConfiguration().getProperty(MAIL_SERVER_TO_KEY);
   }

   @Override
   public String getMailServerUrl () {
      return getCoreConfiguration().getProperty(MAIL_SERVER_URL_KEY);
   }

   @Override
   public String getMailServerUserActivationBody () {
      return getCoreConfiguration().getProperty(MAIL_SERVER_USER_ACTIVATION_BODY_KEY);
   }

   @Override
   public String getMailServerUserAdvancedOptionsAcceptRejectBody () {
      return getCoreConfiguration().getProperty(MAIL_SERVER_USER_ADVANCED_OPTIONS_ACCEPT_REJECT_BODY_KEY);
   }

   @Override
   public String getMailServerUserAdvancedOptionsBody () {
      return getCoreConfiguration().getProperty(MAIL_SERVER_USER_ADVANCED_OPTIONS_BODY_KEY);
   }

   @Override
   public String getMailServerUserDemoRequestsAcceptRejectBody () {
      return getCoreConfiguration().getProperty(MAIL_SERVER_USER_DEMO_REQUESTS_ACCEPT_REJECT_BODY_KEY);
   }

   @Override
   public String getMailServerUserName () {
      return getCoreConfiguration().getProperty(MAIL_SERVER_USER_NAME_KEY);
   }

   @Override
   public String getMailServerUserResetPasswordBody () {
      return getCoreConfiguration().getProperty(MAIL_SERVER_USER_RESET_PASSWORD_BODY_KEY);
   }

   @Override
   public String getMailServerUserResetPasswordSubject () {
      return getCoreConfiguration().getProperty(MAIL_SERVER_USER_RESET_PASSWORD_SUBJECT_KEY);
   }

   @Override
   public String getMailServerUserResetPasswordUrl () {
      return getCoreConfiguration().getProperty(MAIL_SERVER_USER_RESET_PASSWORD_URL_KEY);
   }

   @Override
   public Long getMaxAllowedInstanceMappingSuggestions () {
      return getCoreConfiguration().getLong(MAX_ALLOWED_INSTANCE_MAPPING_SUGGESTIONS);
   }

   @Override
   public Integer getMaxDBObjectLength () {
      return getCoreConfiguration().getInt(MAX_DB_OBJECT_LENGTH);
   }

   @Override
   public Integer getMaxDefaultMetrics () {
      return getCoreConfiguration().getInt(MAX_DEFAULT_METRICS);
   }

   @Override
   public Integer getMaxUserQueryLength () {
      return getCoreConfiguration().getInt(MAX_USER_QUERY_LENGTH_KEY);
   }

   @Override
   public Integer getMessageCleanupHours () {
      return getCoreConfiguration().getInt(MESSAGE_CLEANUP_HOURS);
   }

   @Override
   public Integer getModelBasedConceptDefaultRecognitionWeight () {
      return getCoreConfiguration().getInt(MODEL_BASED_CONCEPT_DEFAULT_RECOGNITION_WEIGHT);
   }

   @Override
   public Integer getModelBasedInstanceDefaultRecognitionWeight () {
      return getCoreConfiguration().getInt(MODEL_BASED_INSTANCE_DEFAULT_RECOGNITION_WEIGHT);
   }

   @Override
   public Integer getModelBasedRelationDefaultRecognitionWeight () {
      return getCoreConfiguration().getInt(MODEL_BASED_RELATION_DEFAULT_RECOGNITION_WEIGHT);
   }

   @Override
   public String getMsSQLDefaultDateFormat () {
      return getCoreConfiguration().getProperty(MSSQL_DEFAULT_DATE_FORMAT);
   }

   @Override
   public String getMsSQLDefaultDateTimeFormat () {
      return getCoreConfiguration().getProperty(MSSQL_DEFAULT_DATETIME_FORMAT);
   }

   @Override
   public String getMySQLDefaultDateFormat () {
      return getCoreConfiguration().getProperty(MYSQL_DEFAULT_DATE_FORMAT);
   }

   @Override
   public String getMySQLDefaultDateTimeFormat () {
      return getCoreConfiguration().getProperty(MYSQL_DEFAULT_DATETIME_FORMAT);
   }

   @Override
   public String getOracleDefaultDateFormat () {
      return getCoreConfiguration().getProperty(ORACLE_DEFAULT_DATE_FORMAT);
   }

   @Override
   public String getOracleDefaultDateTimeFormat () {
      return getCoreConfiguration().getProperty(ORACLE_DEFAULT_DATETIME_FORMAT);
   }

   @Override
   public String getTeradataDefaultDateFormat () {
      return getCoreConfiguration().getProperty(TERADATA_DEFAULT_DATE_FORMAT);
   }

   @Override
   public String getTeradataDefaultDateTimeFormat () {
      return getCoreConfiguration().getProperty(TERADATA_DEFAULT_DATETIME_FORMAT);
   }

   @Override
   public String getSASDefaultDateFormat () {
      return getCoreConfiguration().getProperty(SAS_DEFAULT_DATE_FORMAT);
   }

   @Override
   public String getSASDefaultDateTimeFormat () {
      return getCoreConfiguration().getProperty(SAS_DEFAULT_DATETIME_FORMAT);
   }

   @Override
   public String getPrefixUploadedTableName () {
      return getCoreConfiguration().getProperty(PREFIX_UPLOADED_TABLE_NAME);
   }

   @Override
   public String getPrefixUploadedTempTableName () {
      return getCoreConfiguration().getProperty(PREFIX_UPLOADED_TEMP_TABLE_NAME);
   }

   @Override
   public Long getRIUserQueryExecutionTime () {
      return getCoreConfiguration().getLong(RI_USER_QUERY_EXECUTION_TIME);
   }

   @Override
   public Integer getResultsPageSize () {
      return getCoreConfiguration().getInt(RESULTS_PAGE_SIZE);
   }

   @Override
   public Integer getSDXMemberAbsorptionBatchSize () {
      return getCoreConfiguration().getInt(SDX_MEMBER_ABSORPTION_BATCH_SIZE_KEY);
   }

   @Override
   public String getStatisticsColumnName () {
      return getCoreConfiguration().getProperty(STATISTICS_COLUMN_NAME_KEY);
   }

   @Override
   public List<String> getStockChartValidApps () {
      return getCoreConfiguration().getList(STOCK_CHART_VALID_APPS_FOR_STOCK_CHART);
   }

   @Override
   public List<String> getStockChartValidConceptBeds () {
      return getCoreConfiguration().getList(STOCK_CHART_VALID_CONCEPT_BE_IDS_FOR_STOCK_CHART);
   }

   @Override
   public String getSystemLevelDefaultStat () {
      return getCoreDBConfiguration().getProperty(SYSTEM_LEVEL_DEFAULT_STAT_KEY);
   }

   @Override
   public Integer getSystemMaxDimensionValue () {
      return getCoreConfiguration().getInt(SYSTEM_MAX_DIMENSION_VALUE);
   }

   @Override
   public Boolean getUniversalSearchFromWarehouse () {
      return getCoreConfiguration().getBoolean(UNIVERSAL_SEARCH_FROM_WAREHOUSE);
   }

   @Override
   public Integer getUniversalSearchMatchQueryLimit () {
      return getCoreConfiguration().getInt(UNIVERSAL_SEARCH_MATCH_QUERY_LIMIT);
   }

   @Override
   public Integer getUniversalSearchTopClusterLimit () {
      return getCoreConfiguration().getInt(UNIVERSAL_SEARCH_TOP_CLUSTER_LIMIT);
   }

   @Override
   public Integer getUnstructuredAppGroupingResultCount () {
      return getCoreConfiguration().getInt(UNSTRUCTURED_APP_GROUPING_RESULT_COUNT);
   }

   @Override
   public String getUserRemoteLocationConnectTimeout () {
      return getCoreConfiguration().getProperty(USER_REMOTE_LOCATION_CONNECT_TIMEOUT);
   }

   @Override
   public String getUserRemoteLocationReadTimeout () {
      return getCoreConfiguration().getProperty(USER_REMOTE_LOCATION_READ_TIMEOUT);
   }

   @Override
   public String getUserRemoteLocationUrl () {
      return getCoreConfiguration().getProperty(USER_REMOTE_LOCATION_URL);
   }

   @Override
   public String getWebAdminContext () {
      return getCoreConfiguration().getProperty(WEB_ADMIN_CONTEXT);
   }

   @Override
   public String getApplicationSearchPageURL () {
      return getCoreConfiguration().getProperty(APPLICATION_SEARCH_URL_PREFIX);
   }

   @Override
   public String getWebMainContext () {
      return getCoreConfiguration().getProperty(WEB_MAIN_CONTEXT);
   }

   @Override
   public String getXMLRequest () {
      return getCoreConfiguration().getProperty(XML_REQUEST);
   }

   @Override
   public boolean isAppScopeEnabled () {
      return getCoreConfiguration().getBoolean(APP_SCOPING_ENABLED);
   }

   @Override
   public boolean isEncrpytionConfigured () {
      return getCoreConfiguration().getBoolean(ENCRYPTION_CONFIGURATION_KEY);
   }

   @Override
   public boolean isEncryptionMetaDataConfigured () {
      return getCoreConfiguration().getBoolean(ENCRYPTION_METADATA_KEY);
   }

   @Override
   public Boolean isFilterUniversalSearchResultByValueMatch () {
      // TODO Auto-generated method stub
      return getCoreConfiguration().getBoolean(FILTER_UNIVERSAL_SEARCH_RESULT_BY_VALUE_MATCH);
   }

   @Override
   public boolean isMaskingMetaSchemaEnabled () {
      return getCoreConfiguration().getBoolean(MASKING_METASCHEMA_KEY);
   }

   @Override
   public boolean isPrintStackTraceRequired () {
      return getCoreConfiguration().getBoolean(FLAGS_PRINTSTACK_TRACE_KEY);
   }

   @Override
   public boolean isSystemLogEnabled () {
      return getCoreConfiguration().getBoolean(SYSTEM_LOGGING_LOG_KEY);
   }

   @Override
   public Boolean isThreadPoolForAggregationProcessingEnabled () {
      return getCoreConfiguration().getBoolean(THREAD_POOL_USE_AGGREGATION_PROCESSING);
   }

   @Override
   public Boolean isUniversalSearchFromWarehouseEnabled () {
      return getCoreConfiguration().getBoolean(UNIVERSAL_SEARCH_FROM_WAREHOUSE);
   }

   @Override
   public Boolean getUnstructuredAppUserVisibility () {
      return getCoreConfiguration().getBoolean(UNSTRUCTURED_APP_USER_VISIBILITY);
   }

   @Override
   public Long getMaxHierarchySize () {
      return getCoreConfiguration().getLong(MAX_HIERARCHY_SIZE);
   }

   /**
    * @return the coreConfiguration
    */
   public IConfiguration getCoreConfiguration () {
      return coreConfiguration;
   }

   /**
    * @param coreConfiguration
    *           the coreConfiguration to set
    */
   public void setCoreConfiguration (IConfiguration coreConfiguration) {
      this.coreConfiguration = coreConfiguration;
   }

   @Override
   public String getSkipDerivedUserQueryVariation () {
      return getCoreConfiguration().getProperty(SKIP_DERIVED_USER_QUERY_VARIATION);
   }

   @Override
   public String getMailServerDemoRequestBody () {
      return getCoreConfiguration().getProperty(MAIL_SERVER_DEMO_REQUEST_BODY_KEY);
   }

   @Override
   public String getMailServerSemantifiEnterpriseRequestBody () {
      return getCoreConfiguration().getProperty(MAIL_SERVER_SEMANTIFI_ENTERPRISE_REQUEST_BODY_KEY);
   }

   @Override
   public String getMailServerSemantifiEnterpriseSubject () {
      return getCoreConfiguration().getProperty(MAIL_SERVER_SEMANTIFI_ENTERPRISE_SUBJECT_KEY);
   }

   @Override
   public List<String> getSkipDerivedUserQueryVariationAppNames () {
      return getCoreConfiguration().getList(SKIP_DERIVED_USER_QUERY_VARIATION);
   }

   @Override
   public String getExecueDataSourcePasswordEncyptionDescryptionKey () {
      return getCoreConfiguration().getProperty(EXECUE_DATASOURCE_PASSWORD_ENCRYPTION_DECRYPTION_KEY);
   }

   @Override
   public Integer getTotalRangeBands () {
      return getCoreConfiguration().getInt(TOTAL_RANGE_BANDS_KEY);
   }

   @Override
   public Long getDynamicRangeId () {
      return getCoreConfiguration().getLong(DYNAMIC_RANGE_ID_KEY);
   }

   @Override
   public boolean isForcePooledDataSourcesViaJNDI () {
      return getCoreConfiguration().getBoolean(FORCE_POOLED_DATA_SOURCES_VIA_JNDI);
   }

   @Override
   public boolean isGroupConcatDBSupported () {
      return getCoreConfiguration().getBoolean(GROUP_CONCAT_DB_SUPPORTED);
   }

   @Override
   public boolean isNLPTaggerInitiationRequired () {
      return getCoreConfiguration().getBoolean(NLP_TAGGER_INITIATION_REQUIRED + "." + getRunningApplicationTypeValue());
   }

   public IConfiguration getCoreDBConfiguration () {
      return coreDBConfiguration;
   }

   public void setCoreDBConfiguration (IConfiguration coreDBConfiguration) {
      this.coreDBConfiguration = coreDBConfiguration;
   }

   public String getApplicationName () {
      return applicationName;
   }

   public String getRunningApplicationTypeValue () {
      String systemType = "BATCH";
      if (ExecueSystem.getSystemType() != null && SystemType.BATCH != ExecueSystem.getSystemType()) {
         String configuredConsoleApplicationName = getWebAdminContext().replaceAll("/", "");
         String configuredSearchApplicationName = getWebMainContext().replaceAll("/", "");
         if (getApplicationName().equalsIgnoreCase(configuredConsoleApplicationName)) {
            systemType = "CONSOLE_APP";
         } else if (getApplicationName().equalsIgnoreCase(configuredSearchApplicationName)) {
            systemType = "SEARCH_APP";
         } else {
            log.warn("Context Paths in Core Configuration are WRONG., System Type as [" + ExecueSystem.getSystemType()
                     + "], Application Name as [" + getApplicationName() + "], Console Context as ["
                     + configuredConsoleApplicationName + "] and Seaqrch Context as ["
                     + configuredSearchApplicationName + "]");
         }
      }
      return systemType;
   }

   @Override
   public String getPostgresqlDefaultDateTimeFormat () {
      return getCoreConfiguration().getProperty(POSTGRESQL_DEFAULT_DATETIME_FORMAT);
   }

   @Override
   public String getPostgresqlDefaultDateFormat () {
      return getCoreConfiguration().getProperty(POSTGRESQL_DEFAULT_DATE_FORMAT);
   }

   @Override
   public boolean isSingleAssetRedirection () {
      return getCoreConfiguration().getBoolean(SINGLE_ASSET_REDIRECT);
   }
}
