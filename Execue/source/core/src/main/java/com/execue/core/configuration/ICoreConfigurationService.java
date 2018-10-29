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


package com.execue.core.configuration;

import java.util.Hashtable;
import java.util.List;

public interface ICoreConfigurationService {

   /**
    * This key will retrieve the name of the statistic which is set as a system default
    */
   public static final String SYSTEM_LEVEL_DEFAULT_STAT_KEY = "core.static-values.system-level-default-stat";

   public Hashtable<String, String> getInitialContextEnvironment ();

   public String getJobsConfiguraion ();

   public boolean isPrintStackTraceRequired ();

   public boolean isEncrpytionConfigured ();

   public boolean isEncryptionMetaDataConfigured ();

   public boolean isMaskingMetaSchemaEnabled ();

   public boolean isSystemLogEnabled ();

   public boolean isAppScopeEnabled ();

   public String getSystemLevelDefaultStat ();

   public Integer getDimensionTreshold ();

   public Integer getSystemMaxDimensionValue ();

   public String getOracleDefaultDateFormat ();

   public String getMySQLDefaultDateFormat ();

   public String getMsSQLDefaultDateFormat ();

   public String getTeradataDefaultDateFormat ();

   public String getOracleDefaultDateTimeFormat ();

   public String getMySQLDefaultDateTimeFormat ();

   public String getMsSQLDefaultDateTimeFormat ();

   public String getTeradataDefaultDateTimeFormat ();

   public String getXMLRequest ();

   public Integer getApplicationRetrievalLimit ();

   public Integer getApplicationExampleTruncatedLength ();

   public Integer getAppSpecificExampleTruncatedLength ();

   public Integer getAppSpecificExampleRetrievalLimit ();

   public Integer getApplicationExampleRetrievalLimit ();

   public Integer getApplicationDescriptionTruncatedLength ();

   public Integer getConceptRetrievalLimit ();

   public Integer getInstanceRetrievalLimit ();

   public List<String> getStockChartValidApps ();

   public List<String> getStockChartValidConceptBeds ();

   public Boolean isUniversalSearchFromWarehouseEnabled ();

   public Boolean getUniversalSearchFromWarehouse ();

   public Integer getMaxDBObjectLength ();

   public String getCubeAllValue ();

   public String getStatisticsColumnName ();

   public String getMailServerFrom ();

   public String getMailServerTo ();

   public String getMailServerHostName ();

   public String getMailServerSubject ();

   public String getMailServerSubjectAdvancedOptions ();

   public String getMailServerUserName ();

   public String getMailServerPassword ();

   public String getMailServerUrl ();

   public String getMailServerUserResetPasswordUrl ();

   public String getMailServerPublisherBody ();

   public String getMailServerFeedbackBody ();

   public String getMailServerPublisherSubject ();

   public String getMailServerFeedbackSubject ();

   public String getMailServerUserActivationBody ();

   public String getMailServerUserAdvancedOptionsBody ();

   public String getMailServerUserResetPasswordBody ();

   public String getMailServerUserResetPasswordSubject ();

   public String getMailServerUserAdvancedOptionsAcceptRejectBody ();

   public String getMailServerUserDemoRequestsAcceptRejectBody ();

   public String getMailServerFeedbackAcceptRejectBody ();

   public String getMailServerDemoRequestSubject ();

   public Integer getMaxUserQueryLength ();

   public Integer getResultsPageSize ();

   public Integer getDetailResultsPageSize ();

   public String getPrefixUploadedTableName ();

   public String getPrefixUploadedTempTableName ();

   public Long getMaxAllowedInstanceMappingSuggestions ();

   public Integer getAssetBasedConceptDefaultRecognitionWeight ();

   public Integer getAssetBasedInstanceDefaultRecognitionWeight ();

   public Integer getAssetBasedRelationDefaultRecognitionWeight ();

   public Integer getModelBasedConceptDefaultRecognitionWeight ();

   public Integer getModelBasedInstanceDefaultRecognitionWeight ();

   public Integer getModelBasedRelationDefaultRecognitionWeight ();

   public Integer getSDXMemberAbsorptionBatchSize ();

   public Integer getKDXInstanceMappingBatchSize ();

   public String getDefaultDataSourceNameForUploadedDatasets ();

   public String getDefaultDataSourceNameForCatalogDatasets ();

   public Long getRIUserQueryExecutionTime ();

   public Boolean isFilterUniversalSearchResultByValueMatch ();

   public Integer getUniversalSearchMatchQueryLimit ();

   public Integer getUniversalSearchTopClusterLimit ();

   public Boolean isThreadPoolForAggregationProcessingEnabled ();

   public Integer getMessageCleanupHours ();

   public Integer getUnstructuredAppGroupingResultCount ();

   public Integer getMaxDefaultMetrics ();

   public Boolean getUnstructuredAppUserVisibility ();

   public String getWebMainContext ();

   public String getWebAdminContext ();

   public String getApplicationSearchPageURL ();

   public String getUserRemoteLocationUrl ();

   public String getUserRemoteLocationConnectTimeout ();

   public String getUserRemoteLocationReadTimeout ();

   public Long getAdminUserId ();

   public String getSkipDerivedUserQueryVariation ();

   public List<String> getSkipDerivedUserQueryVariationAppNames ();

   public String getMailServerSemantifiEnterpriseSubject ();

   public String getMailServerDemoRequestBody ();

   public String getMailServerSemantifiEnterpriseRequestBody ();

   public String getSASDefaultDateFormat ();

   public String getSASDefaultDateTimeFormat ();

   public String getExecueDataSourcePasswordEncyptionDescryptionKey ();

   public Integer getTotalRangeBands ();

   public Long getDynamicRangeId ();

   public Long getMaxHierarchySize ();

   public boolean isForcePooledDataSourcesViaJNDI ();

   public boolean isGroupConcatDBSupported ();

   public boolean isNLPTaggerInitiationRequired ();

   public String getApplicationName ();

   public String getRunningApplicationTypeValue ();

   public String getPostgresqlDefaultDateTimeFormat ();

   public String getPostgresqlDefaultDateFormat ();

   public boolean isSingleAssetRedirection ();
}
