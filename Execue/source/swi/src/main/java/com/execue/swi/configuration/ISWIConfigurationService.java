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


package com.execue.swi.configuration;

import java.util.List;
import java.util.Set;

import com.execue.core.common.bean.entity.DateFormat;
import com.execue.core.common.bean.entity.SystemVariable;
import com.execue.core.common.bean.entity.Vertical;
import com.execue.core.common.bean.kb.POSContext;
import com.execue.core.common.type.AssetProviderType;
import com.execue.core.common.type.DateQualifier;

public interface ISWIConfigurationService {

   public POSContext getPosContext ();

   // TODO : -RG- this configuration would have been a flag on type table rather than a static list
   public List<Long> getTypeBEDIdsRestrictedForSecondaryRICloudEntry ();

   /**
    * Provides the Date Format object based on the format and provider type. Both format and provider type should not be
    * null
    * 
    * @param format
    * @param assetProviderType
    * @return
    */
   public DateFormat getSupportedDateFormat (String format, AssetProviderType assetProviderType);

   /**
    * Provides the Date Qualifier based on the format. Format should not be null
    * 
    * @param format
    * @return
    */
   public DateQualifier getDateQualifier (String format);

   public List<Vertical> getVisibleVerticals ();

   public Set<SystemVariable> getSystemVariables ();

   public Set<String> getSystemVariableWords ();

   public List<String> getBaseRelations ();

   public Set<Long> getNonRealizableTypeBedIds ();

   /** configuration properties methods* */

   public Integer getMaxHopsForIndirectJoins ();

   public Long getUserQueryPossiblilityTimeInMillisecond ();

   public String getPopularityRestorationJobUrl ();

   public String getPopularityRestorationJobDriverClassName ();

   public String getPopularityRestorationJobUserName ();

   public String getPopularityRestorationJobPassword ();

   public String getPopularityRestorationJobDelimeter ();

   public String getPopularityRestorationJobProviderType ();

   public Integer getPopularityBatchSize ();

   public String getVirtualLookupDescriptionColumnSuffix ();

   public Integer getLookupTablesWithoutMembersLimit ();

   public Integer getColumnWithoutKdxTypeLimit ();

   public Integer getTablesWithoutJoinsLimit ();

   public Integer getTableWithUnmappedColumnsLimit ();

   public Integer getTableWithUnmappedMemebrsLimit ();

   public String getDefaultThresholdForSecondaryWords ();

   public String getDefaultWeightForConjunctionSecondaryWords ();

   public String getDefaultWeightForNumberSecondaryWords ();

   public String getDefaultWeightForSingleCharTokens ();

   public Integer getMappingPageSize ();

   public List<String> getPublishCommunityAclRoles ();

   public String getPlainDateRegex (AssetProviderType providerType);

   public Integer getPlainDateFieldMinLength ();

   public String getDateRegex (AssetProviderType providerType);

   public Integer getDateFieldMinLength ();

   public Integer getIntegerDataTypeBufferSize ();

   public Integer getIntegerDataTypeMaximumLength ();

   public Integer getStringDataTypeBufferSize ();

   public Integer getDecimalDataTypePrecisionBufferSize ();

   public Integer getDecimalDataTypeScaleBufferSize ();

   public Integer getScaleMaximumLength ();

   public Integer getSampleDateRecordsSize ();

   public String getNumberRegex (AssetProviderType providerType);

   public String getIntegerRegex (AssetProviderType providerType);

   public String getMemberNullValueReprsentation ();

   public Integer getMaxDefaultMetricsPerTable ();

   public Double getDefaultWeightForBaseRIontoTermWords ();

   public List<String> getTypeNames ();

   public int getMemberDeletionBatchSize ();
}
