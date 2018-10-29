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


package com.execue.swi.configuration.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.execue.core.common.bean.entity.DateFormat;
import com.execue.core.common.bean.entity.SystemVariable;
import com.execue.core.common.bean.entity.Vertical;
import com.execue.core.common.bean.kb.POSContext;
import com.execue.core.common.bean.util.AbsorptionWizardLinkDetail;
import com.execue.core.common.type.AssetProviderType;
import com.execue.core.common.type.DateQualifier;
import com.execue.core.configuration.IConfiguration;
import com.execue.swi.configuration.ISWIConfigurationService;

public class SWIConfigurationServiceImpl implements ISWIConfigurationService {

   /** Configuration constant start * */

   private static final String        MAX_HOPS_INDIRECT_JOINS_KEY                             = "swi.static-values.max-hops-indirect-joins";
   private static final String        USER_QUERY_POSSIBILITY_TIME                             = "swi.static-values.maintain-user-query-possibiliity-time-ms";
   // private String LOOUP_NULL_VALUE_REPLACEMENT_KEY = "swi.static-values.lookup-null-value-replacements";

   private static final String        POPULARITY_RESTORATION_JOB_INFO_URL                     = "swi.static-values.popularity-restoration-job-info-url";
   private static final String        POPULARITY_RESTORATION_JOB_INFO_DRIVER_CLASS_NAME       = "swi.static-values.popularity-restoration-job-info-driver-class-name";
   private static final String        POPULARITY_RESTORATION_JOB_INFO_USER_NAME               = "swi.static-values.popularity-restoration-job-info-user-name";
   private static final String        POPULARITY_RESTORATION_JOB_INFO_PASSWORD                = "swi.static-values.popularity-restoration-job-info-password";
   private static final String        POPULARITY_RESTORATION_JOB_INFO_DELIMETER               = "swi.static-values.popularity-restoration-job-info-delimeter";
   private static final String        POPULARITY_RESTORATION_JOB_INFO_PROVIDER_TYPE           = "swi.static-values.popularity-restoration-job-info-provider-type";

   private static final String        POPULARITY_BATCH_SIZE                                   = "swi.static-values.popularity-batch-size";
   private static final String        VIRTUAL_LOOKUP_DESC_COLUMN_SUFFIX_KEY                   = "swi.static-values.virtual-lookup-desc-column-suffix";
   private static final String        ENUM_LOOKUP_NAME_VALUE_SEPRATOR_KEY                     = "swi.static-values.enum-lookup-name-value-separator";

   /**
    * Page size key for displaying Mappings
    */
   private static final String        MAPPINGS_PAGE_SIZE_KEY                                  = "swi.mappings.page-size";
   private static final String        MEMBER_NULL_VALUE_REPRESENTATION                        = "swi.static-values.member-null-value-representation";
   private static final String        MAX_DEFAULT_METRICS_PER_TABLE                           = "swi.static-values.max-default-metrics-per-table";

   // Community Roles

   private static final String        PUBLISH_COMMUNITY_ACL_ROLES                             = "swi.publish.community-roles.role";

   // Wizard related configuration parameter keys
   // private String SWI_WIZARD_PATHS = "swi.wizard.paths";
   // private String SWI_WIZARD_PAGE_DETAILS = "swi.wizard.page-details";
   private static final String        SWI_WIZARD_PAGE_DETAILS_KEY_PREFIX                      = "swi.wizard.page-details";
   private static final String        SWI_WIZARD_SOURCE_PATHS_KEY_PREFIX                      = "swi.wizard.source-paths";
   private static final String        SWI_WIZARD_REQUEST_PARAMETERS_KEY                       = "swi.wizard.url-params.param";
   private static final String        SWI_WIZARD_SOURC_TYPES_KEY                              = "swi.wizard.source-types.source-type";
   private static final String        SWI_WIZARD_WIZARD_PATHS_KEY                             = "swi.wizard.wizard-paths.path";
   // private String SWI_WIZARD_SOURCE_TYPE_URL_PARAM_NAME_KEY = "swi.wizard.source-type-url-param";
   // private String SWI_WIZARD_WIZARD_BASED_URL_PARAM_NAME_KEY = "swi.wizard.wizard-based-url-param";
   private static final String        SWI_WIZARD_PAGE_DETAILS_LINK_DESCRIPTION                = "linkDescription";
   private static final String        SWI_WIZARD_PAGE_DETAILS_BREADCRUMB_DESCRIPTION          = "breadcrumbDescription";
   private static final String        SWI_WIZARD_PAGE_DETAILS_BASE_LINK                       = "baseLink";
   private static final String        SWI_WIZARD_PAGE_DETAILS_PARAM                           = "param";
   private static final String        SWI_WIZARD_PAGE_DETAILS_SCRIPT_CALL                     = "scriptCall";

   // Asset Analysis Treshold parameter keys

   private static final String        LOOKUP_TABLES_WITHOUT_MEMBERS_LIMIT                     = "swi.asset-analysis-treshold.lookup-tables-without-members";
   private static final String        COLUMNS_WITHOUT_KDX_TYPE_LIMIT                          = "swi.asset-analysis-treshold.columns-without-kdxtype";
   private static final String        TABLES_WITHOUT_JOINS_LIMIT                              = "swi.asset-analysis-treshold.tables-without-joins";
   private static final String        TABLE_WITH_UNMAPPED_COLUMNS_LIMIT                       = "swi.asset-analysis-treshold.unmapped-columns";
   private static final String        TABLE_WITH_UNMAPPED_MEMBERS_LIMIT                       = "swi.asset-analysis-treshold.unmapped-members";
   private static final String        TYPE_NAMES_RESTRICTED_FOR_SECONDARY_RI_CLOUD__ENTRY_KEY = "swi.static-values.type-names-restricted-for-secondary-ricloud-entry";
   private static final String        DEFAULT_THRESHOLD_FOR_SECONDARY_WORDS                   = "swi.static-values.default-threshold-for-secondary-words";
   private static final String        DEFAULT_WEIGHT_FOR_CONJUNCTION_SECONDARY_WORDS          = "swi.static-values.default-weight-for-conjunction-secondary-words";
   private static final String        DEFAULT_WEIGHT_FOR_NUMBER_SECONDARY_WORDS               = "swi.static-values.default-weight-for-number-secondary-words";
   private static final String        DEFAULT_WEIGHT_FOR_SINGLE_CHAR_TOKEN                    = "swi.static-values.default-weight-for-single-char-token";
   private static final String        DEFAULT_WEIGHT_FOR_BASE_RIONTO_TERM_WORDS               = "swi.static-values.default-weight-for-base-riontoterm-words";

   // Constants for JDBCSourceColumnMetaService
   private static final String        NUMBER_REGEX_KEY                                        = "swi.static-values.upload-regex.number-regex";
   private static final String        INTEGER_REGEX_KEY                                       = "swi.static-values.upload-regex.integer-regex";

   private static final String        PLAIN_DATE_REGEX_KEY                                    = "swi.static-values.upload-regex.plain-date-regex";
   private static final String        MIN_PLAIN_DATE_FIELD_LENGTH_KEY                         = "swi.static-values.min-plain-date-field-length";
   private static final String        DATE_REGEX_KEY                                          = "swi.static-values.upload-regex.date-regex";
   private static final String        MIN_DATE_FIELD_LENGTH_KEY                               = "swi.static-values.min-date-field-length";

   private static final String        INTEGER_DATA_TYPE_SIZE_BUFFER_KEY                       = "swi.static-values.integer-data-type-size-buffer";
   private static final String        INTEGER_DATA_TYPE_MAXIMUM_LENGTH_KEY                    = "swi.static-values.integer-data-type-maximum-length";
   private static final String        STRING_DATA_TYPE_SIZE_BUFFER_KEY                        = "swi.static-values.string-data-type-size-buffer";
   private static final String        DECIMAL_DATA_TYPE_PRECISION_BUFFER_KEY                  = "swi.static-values.decimal-data-type-precision-buffer";
   private static final String        DECIMAL_DATA_TYPE_SCALE_BUFFER_KEY                      = "swi.static-values.decimal-data-type-scale-buffer";
   private static final String        MAX_SCALE_LENGTH_KEY                                    = "swi.static-values.max-scale-length";
   private static final String        SAMPLE_DATE_RECORDS_SIZE_KEY                            = "swi.static-values.sample-date-records-size";
   private static final String        TYPE_NAMES_KEY                                          = "swi.sfl-secondary-words.type-names.name";
   private static final String        MEMBER_DELETION_BATCH_SIZE_KEY                          = "swi.static-values.member-deletion-batch-size";

   /** Configuration constant end * */

   private IConfiguration             swiConfiguration;
   private POSContext                 posContext;
   private List<Long>                 typeBEDIdsRestrictedForSecondaryRICloudEntry            = new ArrayList<Long>();
   private Map<String, DateFormat>    supportedDateFormatByFormatAndProviderTypeMap           = new HashMap<String, DateFormat>();
   private Map<String, DateQualifier> dateQualifierByFormatMap                                = new HashMap<String, DateQualifier>();
   private List<Vertical>             visibleVerticals                                        = new ArrayList<Vertical>();
   private Set<SystemVariable>        systemVariables;
   private Set<String>                systemVariableWords;
   private List<String>               baseRelations                                           = new ArrayList<String>();
   private Set<Long>                  nonRealizableTypeBedIds                                 = new HashSet<Long>(1);

   public DateFormat getSupportedDateFormat (String format, AssetProviderType assetProviderType) {
      return supportedDateFormatByFormatAndProviderTypeMap.get(format + assetProviderType.getValue());
   }

   public DateQualifier getDateQualifier (String format) {
      return dateQualifierByFormatMap.get(format);
   }

   public List<Long> getTypeBEDIdsRestrictedForSecondaryRICloudEntry () {
      return typeBEDIdsRestrictedForSecondaryRICloudEntry;
   }

   /**
    * @return the posContext
    */
   public POSContext getPosContext () {
      return posContext;
   }

   public List<Vertical> getVisibleVerticals () {
      return visibleVerticals;
   }

   public Set<SystemVariable> getSystemVariables () {
      return systemVariables;
   }

   public Set<String> getSystemVariableWords () {
      return systemVariableWords;
   }

   public List<String> getBaseRelations () {
      return baseRelations;
   }

   public Set<Long> getNonRealizableTypeBedIds () {
      return nonRealizableTypeBedIds;
   }

   public String getEnumLookupNameValueSeprator () {
      return getSwiConfiguration().getProperty(ENUM_LOOKUP_NAME_VALUE_SEPRATOR_KEY);
   }

   public List<String> getTypesRestrictedForSecondaryRICloud () {
      return getSwiConfiguration().getList(TYPE_NAMES_RESTRICTED_FOR_SECONDARY_RI_CLOUD__ENTRY_KEY);
   }

   public List<String> getSwiWizardSourceTypes () {
      return getSwiConfiguration().getList(SWI_WIZARD_SOURC_TYPES_KEY);
   }

   public List<String> getSwiWizardPaths () {
      return getSwiConfiguration().getList(SWI_WIZARD_WIZARD_PATHS_KEY);
   }

   public List<String> getSwiWizardUrlRequestParameters () {
      return getSwiConfiguration().getList(SWI_WIZARD_REQUEST_PARAMETERS_KEY);
   }

   public Map<String, List<String>> populateSourceTypeWizardPathsMap () {
      Map<String, List<String>> sourceTypeWizardPathsMap = new HashMap<String, List<String>>();
      List<String> sourceTypes = getSwiWizardSourceTypes();
      for (String sourceType : sourceTypes) {
         List<String> sourceTypeBasedWizardPaths = getSwiConfiguration().getList(
                  SWI_WIZARD_SOURCE_PATHS_KEY_PREFIX + "." + sourceType + ".path");
         sourceTypeWizardPathsMap.put(sourceType, sourceTypeBasedWizardPaths);
      }
      return sourceTypeWizardPathsMap;
   }

   public Map<String, AbsorptionWizardLinkDetail> populateWizardPathLinkDetailMap () {
      Map<String, AbsorptionWizardLinkDetail> wizardPathLinkDetailMap = new HashMap<String, AbsorptionWizardLinkDetail>();
      List<String> wizardPaths = getSwiWizardPaths();
      for (String wizardPath : wizardPaths) {
         AbsorptionWizardLinkDetail absorptionWizardLinkDetail = new AbsorptionWizardLinkDetail();
         absorptionWizardLinkDetail.setBaseLink(getSwiConfiguration().getProperty(
                  SWI_WIZARD_PAGE_DETAILS_KEY_PREFIX + "." + wizardPath + "." + SWI_WIZARD_PAGE_DETAILS_BASE_LINK));
         absorptionWizardLinkDetail.setBreadcrumbDescription(getSwiConfiguration().getProperty(
                  SWI_WIZARD_PAGE_DETAILS_KEY_PREFIX + "." + wizardPath + "."
                           + SWI_WIZARD_PAGE_DETAILS_BREADCRUMB_DESCRIPTION));
         absorptionWizardLinkDetail.setLinkDescription(getSwiConfiguration().getProperty(
                  SWI_WIZARD_PAGE_DETAILS_KEY_PREFIX + "." + wizardPath + "."
                           + SWI_WIZARD_PAGE_DETAILS_LINK_DESCRIPTION));
         absorptionWizardLinkDetail.setParams(getSwiConfiguration().getList(
                  SWI_WIZARD_PAGE_DETAILS_KEY_PREFIX + "." + wizardPath + "." + SWI_WIZARD_PAGE_DETAILS_PARAM));
         absorptionWizardLinkDetail.setScriptCall(getSwiConfiguration().getBoolean(
                  SWI_WIZARD_PAGE_DETAILS_KEY_PREFIX + "." + wizardPath + "." + SWI_WIZARD_PAGE_DETAILS_SCRIPT_CALL));

         wizardPathLinkDetailMap.put(wizardPath, absorptionWizardLinkDetail);
      }

      return wizardPathLinkDetailMap;
   }

   @Override
   public String getVirtualLookupDescriptionColumnSuffix () {
      return getSwiConfiguration().getProperty(VIRTUAL_LOOKUP_DESC_COLUMN_SUFFIX_KEY);
   }

   @Override
   public Integer getMaxHopsForIndirectJoins () {
      return getSwiConfiguration().getInt(MAX_HOPS_INDIRECT_JOINS_KEY);
   }

   @Override
   public Integer getPopularityBatchSize () {
      return getSwiConfiguration().getInt(POPULARITY_BATCH_SIZE);
   }

   @Override
   public String getPopularityRestorationJobDelimeter () {
      return getSwiConfiguration().getProperty(POPULARITY_RESTORATION_JOB_INFO_DELIMETER);
   }

   @Override
   public String getPopularityRestorationJobDriverClassName () {
      return getSwiConfiguration().getProperty(POPULARITY_RESTORATION_JOB_INFO_DRIVER_CLASS_NAME);
   }

   @Override
   public String getPopularityRestorationJobPassword () {
      return getSwiConfiguration().getProperty(POPULARITY_RESTORATION_JOB_INFO_PASSWORD);
   }

   @Override
   public String getPopularityRestorationJobProviderType () {
      return getSwiConfiguration().getProperty(POPULARITY_RESTORATION_JOB_INFO_PROVIDER_TYPE);
   }

   @Override
   public String getPopularityRestorationJobUrl () {
      return getSwiConfiguration().getProperty(POPULARITY_RESTORATION_JOB_INFO_URL);
   }

   @Override
   public String getPopularityRestorationJobUserName () {
      return getSwiConfiguration().getProperty(POPULARITY_RESTORATION_JOB_INFO_USER_NAME);
   }

   @Override
   public Long getUserQueryPossiblilityTimeInMillisecond () {
      return getSwiConfiguration().getLong(USER_QUERY_POSSIBILITY_TIME);
   }

   @Override
   public Integer getColumnWithoutKdxTypeLimit () {
      return getSwiConfiguration().getInt(COLUMNS_WITHOUT_KDX_TYPE_LIMIT);
   }

   @Override
   public String getDefaultThresholdForSecondaryWords () {
      return getSwiConfiguration().getProperty(DEFAULT_THRESHOLD_FOR_SECONDARY_WORDS);
   }

   @Override
   public String getDefaultWeightForConjunctionSecondaryWords () {
      return getSwiConfiguration().getProperty(DEFAULT_WEIGHT_FOR_CONJUNCTION_SECONDARY_WORDS);
   }

   @Override
   public String getDefaultWeightForNumberSecondaryWords () {
      return getSwiConfiguration().getProperty(DEFAULT_WEIGHT_FOR_NUMBER_SECONDARY_WORDS);
   }

   @Override
   public String getDefaultWeightForSingleCharTokens () {
      return getSwiConfiguration().getProperty(DEFAULT_WEIGHT_FOR_SINGLE_CHAR_TOKEN);
   }

   @Override
   public Integer getLookupTablesWithoutMembersLimit () {
      return getSwiConfiguration().getInt(LOOKUP_TABLES_WITHOUT_MEMBERS_LIMIT);
   }

   @Override
   public Integer getTableWithUnmappedColumnsLimit () {
      return getSwiConfiguration().getInt(TABLE_WITH_UNMAPPED_COLUMNS_LIMIT);
   }

   @Override
   public Integer getTableWithUnmappedMemebrsLimit () {
      return getSwiConfiguration().getInt(TABLE_WITH_UNMAPPED_MEMBERS_LIMIT);
   }

   @Override
   public Integer getTablesWithoutJoinsLimit () {
      return getSwiConfiguration().getInt(TABLES_WITHOUT_JOINS_LIMIT);
   }

   @Override
   public Integer getMappingPageSize () {
      return getSwiConfiguration().getInt(MAPPINGS_PAGE_SIZE_KEY);
   }

   @Override
   public List<String> getPublishCommunityAclRoles () {
      return getSwiConfiguration().getList(PUBLISH_COMMUNITY_ACL_ROLES);
   }

   @Override
   public Integer getDateFieldMinLength () {
      return getSwiConfiguration().getInt(MIN_DATE_FIELD_LENGTH_KEY);
   }

   @Override
   public String getDateRegex (AssetProviderType providerType) {
      String subKey = "default";
      if (AssetProviderType.POSTGRES == providerType) {
         subKey = String.valueOf(AssetProviderType.POSTGRES);
      }
      return getSwiConfiguration().getProperty(DATE_REGEX_KEY + "." + subKey);
   }

   @Override
   public Integer getPlainDateFieldMinLength () {
      return getSwiConfiguration().getInt(MIN_PLAIN_DATE_FIELD_LENGTH_KEY);
   }

   @Override
   public String getPlainDateRegex (AssetProviderType providerType) {
      String subKey = "default";
      if (AssetProviderType.POSTGRES == providerType) {
         subKey = String.valueOf(AssetProviderType.POSTGRES);
      }
      return getSwiConfiguration().getProperty(PLAIN_DATE_REGEX_KEY + "." + subKey);
   }

   @Override
   public Integer getDecimalDataTypePrecisionBufferSize () {

      return getSwiConfiguration().getInt(DECIMAL_DATA_TYPE_PRECISION_BUFFER_KEY);
   }

   @Override
   public Integer getDecimalDataTypeScaleBufferSize () {
      return getSwiConfiguration().getInt(DECIMAL_DATA_TYPE_SCALE_BUFFER_KEY);
   }

   @Override
   public Integer getIntegerDataTypeBufferSize () {
      return getSwiConfiguration().getInt(INTEGER_DATA_TYPE_SIZE_BUFFER_KEY);
   }

   @Override
   public Integer getIntegerDataTypeMaximumLength () {
      return getSwiConfiguration().getInt(INTEGER_DATA_TYPE_MAXIMUM_LENGTH_KEY);
   }

   @Override
   public Integer getSampleDateRecordsSize () {
      return getSwiConfiguration().getInt(SAMPLE_DATE_RECORDS_SIZE_KEY);
   }

   @Override
   public Integer getScaleMaximumLength () {
      return getSwiConfiguration().getInt(MAX_SCALE_LENGTH_KEY);
   }

   @Override
   public Integer getStringDataTypeBufferSize () {
      return getSwiConfiguration().getInt(STRING_DATA_TYPE_SIZE_BUFFER_KEY);
   }

   @Override
   public String getIntegerRegex (AssetProviderType providerType) {
      String subKey = "default";
      if (AssetProviderType.POSTGRES == providerType) {
         subKey = String.valueOf(AssetProviderType.POSTGRES);
      }
      return getSwiConfiguration().getProperty(INTEGER_REGEX_KEY + "." + subKey);
   }

   @Override
   public String getNumberRegex (AssetProviderType providerType) {
      String subKey = "default";
      if (AssetProviderType.POSTGRES == providerType) {
         subKey = String.valueOf(AssetProviderType.POSTGRES);
      }
      return getSwiConfiguration().getProperty(NUMBER_REGEX_KEY + "." + subKey);
   }

   @Override
   public String getMemberNullValueReprsentation () {
      return getSwiConfiguration().getProperty(MEMBER_NULL_VALUE_REPRESENTATION);
   }

   @Override
   public Integer getMaxDefaultMetricsPerTable () {
      return getSwiConfiguration().getInt(MAX_DEFAULT_METRICS_PER_TABLE);
   }

   @Override
   public Double getDefaultWeightForBaseRIontoTermWords () {
      return getSwiConfiguration().getDouble(DEFAULT_WEIGHT_FOR_BASE_RIONTO_TERM_WORDS);
   }

   /**
    * @return the supportedDateFormatByFormatAndProviderTypeMap
    */
   public Map<String, DateFormat> getSupportedDateFormatByFormatAndProviderTypeMap () {
      return supportedDateFormatByFormatAndProviderTypeMap;
   }

   /**
    * @param supportedDateFormatByFormatAndProviderTypeMap
    *           the supportedDateFormatByFormatAndProviderTypeMap to set
    */
   public void setSupportedDateFormatByFormatAndProviderTypeMap (
            Map<String, DateFormat> supportedDateFormatByFormatAndProviderTypeMap) {
      this.supportedDateFormatByFormatAndProviderTypeMap = supportedDateFormatByFormatAndProviderTypeMap;
   }

   /**
    * @return the dateQualifierByFormatMap
    */
   public Map<String, DateQualifier> getDateQualifierByFormatMap () {
      return dateQualifierByFormatMap;
   }

   /**
    * @param dateQualifierByFormatMap
    *           the dateQualifierByFormatMap to set
    */
   public void setDateQualifierByFormatMap (Map<String, DateQualifier> dateQualifierByFormatMap) {
      this.dateQualifierByFormatMap = dateQualifierByFormatMap;
   }

   /**
    * @param posContext
    *           the posContext to set
    */
   public void setPosContext (POSContext posContext) {
      this.posContext = posContext;
   }

   /**
    * @param typeBEDIdsRestrictedForSecondaryRICloudEntry
    *           the typeBEDIdsRestrictedForSecondaryRICloudEntry to set
    */
   public void setTypeBEDIdsRestrictedForSecondaryRICloudEntry (List<Long> typeBEDIdsRestrictedForSecondaryRICloudEntry) {
      this.typeBEDIdsRestrictedForSecondaryRICloudEntry = typeBEDIdsRestrictedForSecondaryRICloudEntry;
   }

   /**
    * @param visibleVerticals
    *           the visibleVerticals to set
    */
   public void setVisibleVerticals (List<Vertical> visibleVerticals) {
      this.visibleVerticals = visibleVerticals;
   }

   /**
    * @param systemVariables
    *           the systemVariables to set
    */
   public void setSystemVariables (Set<SystemVariable> systemVariables) {
      this.systemVariables = systemVariables;
   }

   /**
    * @param systemVariableWords
    *           the systemVariableWords to set
    */
   public void setSystemVariableWords (Set<String> systemVariableWords) {
      this.systemVariableWords = systemVariableWords;
   }

   /**
    * @param baseRelations
    *           the baseRelations to set
    */
   public void setBaseRelations (List<String> baseRelations) {
      this.baseRelations = baseRelations;
   }

   /**
    * @param nonRealizableTypeBedIds
    *           the nonRealizableTypeBedIds to set
    */
   public void setNonRealizableTypeBedIds (Set<Long> nonRealizableTypeBedIds) {
      this.nonRealizableTypeBedIds = nonRealizableTypeBedIds;
   }

   /**
    * @return the swiConfiguration
    */
   public IConfiguration getSwiConfiguration () {
      return swiConfiguration;
   }

   /**
    * @param swiConfiguration
    *           the swiConfiguration to set
    */
   public void setSwiConfiguration (IConfiguration swiConfiguration) {
      this.swiConfiguration = swiConfiguration;
   }

   @Override
   public List<String> getTypeNames () {
      return getSwiConfiguration().getList(TYPE_NAMES_KEY);
   }

   @Override
   public int getMemberDeletionBatchSize () {
      return getSwiConfiguration().getInt(MEMBER_DELETION_BATCH_SIZE_KEY);
   }
}