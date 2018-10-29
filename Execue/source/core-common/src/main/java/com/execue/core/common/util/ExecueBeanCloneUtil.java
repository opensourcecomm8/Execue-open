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


package com.execue.core.common.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.execue.core.common.bean.BusinessStat;
import com.execue.core.common.bean.BusinessTerm;
import com.execue.core.common.bean.entity.AnswersCatalogContext;
import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.DefaultDynamicValue;
import com.execue.core.common.bean.entity.DefaultMetric;
import com.execue.core.common.bean.entity.JobHistoryOperationalStatus;
import com.execue.core.common.bean.entity.JobOperationalStatus;
import com.execue.core.common.bean.entity.Mapping;
import com.execue.core.common.bean.entity.Membr;
import com.execue.core.common.bean.entity.NormalizedDataType;
import com.execue.core.common.bean.entity.PublishedFileTableDetails;
import com.execue.core.common.bean.entity.RICloud;
import com.execue.core.common.bean.entity.RIOntoTerm;
import com.execue.core.common.bean.entity.Range;
import com.execue.core.common.bean.entity.RangeDetail;
import com.execue.core.common.bean.entity.Stat;
import com.execue.core.common.bean.entity.Tabl;
import com.execue.core.common.bean.governor.AssetEntityTerm;
import com.execue.core.common.bean.governor.BusinessAssetTerm;
import com.execue.core.common.bean.governor.StructuredQuery;
import com.execue.core.common.bean.nlp.RelativeTimeNormalizedData;
import com.execue.core.common.bean.nlp.TimeFrameNormalizedData;
import com.execue.core.common.bean.optimaldset.OptimalDSetContext;
import com.execue.core.common.bean.querygen.ConditionEntity;
import com.execue.core.common.bean.querygen.FromEntity;
import com.execue.core.common.bean.querygen.JoinEntity;
import com.execue.core.common.bean.querygen.LimitEntity;
import com.execue.core.common.bean.querygen.OrderEntity;
import com.execue.core.common.bean.querygen.Query;
import com.execue.core.common.bean.querygen.QueryColumn;
import com.execue.core.common.bean.querygen.QueryFormula;
import com.execue.core.common.bean.querygen.QueryTable;
import com.execue.core.common.bean.querygen.QueryTableColumn;
import com.execue.core.common.bean.querygen.QueryValue;
import com.execue.core.common.bean.querygen.SelectEntity;
import com.execue.core.common.bean.security.User;
import com.execue.core.common.type.AssetEntityType;
import com.execue.core.common.type.QueryConditionOperandType;
import com.execue.core.common.type.SelectEntityType;
import com.execue.core.util.ExecueCoreUtil;

public class ExecueBeanCloneUtil {

   public static BusinessAssetTerm cloneBusinessAssetTerm (BusinessAssetTerm bat) {
      BusinessTerm clonedBT = cloneBusinessTerm(bat.getBusinessTerm());
      AssetEntityTerm clonedAET = cloneAssetEntityTerm(bat.getAssetEntityTerm());
      BusinessAssetTerm clonedBAT = new BusinessAssetTerm();
      clonedBAT.setBusinessTerm(clonedBT);
      clonedBAT.setAssetEntityTerm(clonedAET);
      return clonedBAT;
   }

   public static AssetEntityTerm cloneAssetEntityTerm (AssetEntityTerm term) {
      AssetEntityTerm clonedAET = new AssetEntityTerm();
      clonedAET.setAssetEntityType(term.getAssetEntityType());
      clonedAET.setAssetEntityDefinitionId(new Long(term.getAssetEntityDefinitionId().longValue()));
      if (AssetEntityType.COLUMN.equals(term.getAssetEntityType())) {
         Colum col = (Colum) term.getAssetEntity();
         Colum clonedCol = cloneColum(col, true);
         clonedAET.setAssetEntity(clonedCol);
      } else if (AssetEntityType.MEMBER.equals(term.getAssetEntityType())) {
         Membr member = (Membr) term.getAssetEntity();
         Membr clonedMember = cloneMember(member);
         clonedAET.setAssetEntity(clonedMember);
      }
      return clonedAET;
   }

   /**
    * This method will do physical cloning of businessTerm. It doesn't clone concept inside the stat object because stat
    * is not going to change concept. It doesn't clone range details also inside range as range details belongs to
    * single range only.
    * 
    * @param toBeClonedBusinessTerm
    * @return clonedBusinessTerm
    */
   public static BusinessTerm cloneBusinessTerm (BusinessTerm toBeClonedBusinessTerm) {
      BusinessTerm clonedBusinessTerm = new BusinessTerm();
      if (toBeClonedBusinessTerm.getBusinessTermWeight() != null) {
         clonedBusinessTerm.setBusinessTermWeight(toBeClonedBusinessTerm.getBusinessTermWeight().doubleValue());
      }
      clonedBusinessTerm.setRequestedByUser(toBeClonedBusinessTerm.isRequestedByUser());
      clonedBusinessTerm.setFromCohort(toBeClonedBusinessTerm.isFromCohort());
      clonedBusinessTerm.setFromPopulation(toBeClonedBusinessTerm.isFromPopulation());
      clonedBusinessTerm.setFromDistribution(toBeClonedBusinessTerm.isFromDistribution());
      clonedBusinessTerm.setBusinessEntityTerm(toBeClonedBusinessTerm.getBusinessEntityTerm());
      clonedBusinessTerm.setProfileBusinessEntityDefinitionId(toBeClonedBusinessTerm
               .getProfileDomainEntityDefinitionId());
      clonedBusinessTerm.setProfileName(toBeClonedBusinessTerm.getProfileName());
      clonedBusinessTerm.setAlternateEntity(toBeClonedBusinessTerm.isAlternateEntity());
      Range toBeClonedRange = toBeClonedBusinessTerm.getRange();
      BusinessStat toBeClonedBusinessStat = toBeClonedBusinessTerm.getBusinessStat();
      if (toBeClonedRange != null) {
         Range clonedRange = new Range();
         clonedRange.setConceptBedId(toBeClonedRange.getConceptBedId());
         clonedRange.setDescription(toBeClonedRange.getDescription());
         clonedRange.setEnabled(toBeClonedRange.isEnabled());
         clonedRange.setId(toBeClonedRange.getId());
         clonedRange.setName(toBeClonedRange.getName());
         clonedRange.setRangeDetails(toBeClonedRange.getRangeDetails());
         clonedRange.setUser(toBeClonedRange.getUser());
         clonedBusinessTerm.setRange(clonedRange);
      }
      if (toBeClonedBusinessStat != null) {
         BusinessStat clonedBusinessStat = new BusinessStat();
         clonedBusinessStat.setRequestedByUser(toBeClonedBusinessStat.isRequestedByUser());
         Stat toBeClonedStat = toBeClonedBusinessStat.getStat();
         if (toBeClonedStat != null) {
            Stat clonedStat = new Stat();
            clonedStat.setConcepts(toBeClonedStat.getConcepts());
            clonedStat.setDescription(toBeClonedStat.getDescription());
            clonedStat.setDisplayName(toBeClonedStat.getDisplayName());
            clonedStat.setId(toBeClonedStat.getId());
            clonedStat.setStatType(toBeClonedStat.getStatType());
            clonedBusinessStat.setStat(clonedStat);
         }
         clonedBusinessTerm.setBusinessStat(clonedBusinessStat);
      }
      clonedBusinessTerm.setQuerySectionTypes(toBeClonedBusinessTerm.getQuerySectionTypes());
      return clonedBusinessTerm;
   }

   public static QueryTableColumn cloneQueryTableColumn (QueryTableColumn queryTableColumn) {
      if (queryTableColumn == null) {
         return null;
      }
      QueryTableColumn clonedQueryTableColumn = new QueryTableColumn();
      clonedQueryTableColumn.setColumn(cloneQueryColumn(queryTableColumn.getColumn()));
      clonedQueryTableColumn.setTable(cloneQueryTable(queryTableColumn.getTable()));
      return clonedQueryTableColumn;
   }

   public static QueryTable cloneQueryTable (QueryTable toBeClonedQueryTable) {
      QueryTable clonedQueryTable = new QueryTable();
      clonedQueryTable.setTableName(toBeClonedQueryTable.getTableName());
      clonedQueryTable.setAlias(toBeClonedQueryTable.getAlias());
      clonedQueryTable.setTableType(toBeClonedQueryTable.getTableType());
      clonedQueryTable.setOwner(toBeClonedQueryTable.getOwner());
      clonedQueryTable.setActualName(toBeClonedQueryTable.getActualName());
      clonedQueryTable.setVirtual(toBeClonedQueryTable.getVirtual());
      return clonedQueryTable;
   }

   public static QueryColumn cloneQueryColumn (QueryColumn toBeClonedQueryColumn) {
      QueryColumn clonedQueryColumn = new QueryColumn();
      clonedQueryColumn.setColumnName(toBeClonedQueryColumn.getColumnName());
      clonedQueryColumn.setDataType(toBeClonedQueryColumn.getDataType());
      clonedQueryColumn.setDefaultValue(toBeClonedQueryColumn.getDefaultValue());
      clonedQueryColumn.setDistinct(toBeClonedQueryColumn.isDistinct());
      clonedQueryColumn.setNullable(toBeClonedQueryColumn.isNullable());
      clonedQueryColumn.setPrecision(toBeClonedQueryColumn.getPrecision());
      clonedQueryColumn.setScale(toBeClonedQueryColumn.getScale());
      clonedQueryColumn.setDataFormat(toBeClonedQueryColumn.getDataFormat());
      clonedQueryColumn.setFileDateFormat(toBeClonedQueryColumn.getFileDateFormat());
      clonedQueryColumn.setAutoIncrement(toBeClonedQueryColumn.isAutoIncrement());
      return clonedQueryColumn;
   }

   public static Tabl cloneTable (Tabl toBeClonedTable) {
      Tabl clonedTable = new Tabl();
      clonedTable.setId(toBeClonedTable.getId());
      clonedTable.setName(toBeClonedTable.getName());
      clonedTable.setDisplayName(toBeClonedTable.getDisplayName());
      clonedTable.setActualName(toBeClonedTable.getActualName());
      clonedTable.setAggregated(toBeClonedTable.getAggregated());
      clonedTable.setAlias(toBeClonedTable.getAlias());
      clonedTable.setAssetEntityDefinitions(toBeClonedTable.getAssetEntityDefinitions());
      clonedTable.setDescription(toBeClonedTable.getDescription());
      clonedTable.setLookupDescColumn(toBeClonedTable.getLookupDescColumn());
      clonedTable.setLookupType(toBeClonedTable.getLookupType());
      clonedTable.setLookupValueColumn(toBeClonedTable.getLookupValueColumn());
      clonedTable.setLowerLimitColumn(toBeClonedTable.getLowerLimitColumn());
      clonedTable.setOwner(toBeClonedTable.getOwner());
      clonedTable.setOwnerAsset(toBeClonedTable.getOwnerAsset());
      clonedTable.setParentColumn(toBeClonedTable.getParentColumn());
      clonedTable.setParentTable(toBeClonedTable.getParentTable());
      clonedTable.setParentTableId(toBeClonedTable.getParentTableId());
      clonedTable.setUpperLimitColumn(toBeClonedTable.getUpperLimitColumn());
      clonedTable.setVirtual(toBeClonedTable.getVirtual());
      clonedTable.setEligibleDefaultMetric(toBeClonedTable.getEligibleDefaultMetric());
      clonedTable.setIndicator(toBeClonedTable.getIndicator());
      clonedTable.setVirtualTableDescColumnExistsOnSource(toBeClonedTable.getVirtualTableDescColumnExistsOnSource());
      return clonedTable;
   }

   /**
    * This method will clone the colum object
    * 
    * @param toBeClonedColum
    * @return clonedColum
    */
   public static Colum cloneColum (Colum toBeClonedColum) {
      return cloneColum(toBeClonedColum, false);
   }

   public static Colum cloneColum (Colum toBeClonedColum, boolean cloneOwnerTable) {
      // clone the colum object
      Colum clonedColum = new Colum();
      clonedColum.setId(toBeClonedColum.getId());
      clonedColum.setName(toBeClonedColum.getName());
      clonedColum.setDisplayName(toBeClonedColum.getDisplayName());
      clonedColum.setDataType(toBeClonedColum.getDataType());
      clonedColum.setPrecision(toBeClonedColum.getPrecision());
      clonedColum.setScale(toBeClonedColum.getScale());
      clonedColum.setDefaultValue(toBeClonedColum.getDefaultValue());
      clonedColum.setDescription(toBeClonedColum.getDescription());
      clonedColum.setIsConstraintColum(toBeClonedColum.getIsConstraintColum());
      clonedColum.setNullable(toBeClonedColum.isNullable());
      clonedColum.setKdxDataType(toBeClonedColum.getKdxDataType());
      clonedColum.setRequired(toBeClonedColum.getRequired());
      clonedColum.setAssetEntityDefinitions(toBeClonedColum.getAssetEntityDefinitions());
      clonedColum.setConversionType(toBeClonedColum.getConversionType());
      clonedColum.setDataFormat(toBeClonedColum.getDataFormat());
      clonedColum.setUnit(toBeClonedColum.getUnit());
      clonedColum.setGranularity(toBeClonedColum.getGranularity());
      clonedColum.setFileDateFormat(toBeClonedColum.getFileDateFormat());
      clonedColum.setIndicator(toBeClonedColum.getIndicator());
      if (cloneOwnerTable) {
         clonedColum.setOwnerTable(cloneTable(toBeClonedColum.getOwnerTable()));
      } else {
         clonedColum.setOwnerTable(toBeClonedColum.getOwnerTable());
      }
      return clonedColum;
   }

   public static PublishedFileTableDetails clonePublishedFileTableDetails (
            PublishedFileTableDetails toBeClonedPublishedFileTableDetails) {
      PublishedFileTableDetails clonedPublishedFileTableDetails = new PublishedFileTableDetails();

      clonedPublishedFileTableDetails.setBaseColumnName(toBeClonedPublishedFileTableDetails.getBaseColumnName());
      clonedPublishedFileTableDetails.setBaseDataType(toBeClonedPublishedFileTableDetails.getBaseDataType());
      clonedPublishedFileTableDetails.setBasePrecision(toBeClonedPublishedFileTableDetails.getBasePrecision());
      clonedPublishedFileTableDetails.setBaseScale(toBeClonedPublishedFileTableDetails.getBaseScale());
      clonedPublishedFileTableDetails.setColumnIndex(toBeClonedPublishedFileTableDetails.getColumnIndex());
      clonedPublishedFileTableDetails.setDefaultMetric(toBeClonedPublishedFileTableDetails.getDefaultMetric());
      clonedPublishedFileTableDetails.setDistribution(toBeClonedPublishedFileTableDetails.getDistribution());
      clonedPublishedFileTableDetails.setEvaluatedColumnDisplayName(toBeClonedPublishedFileTableDetails
               .getEvaluatedColumnDisplayName());
      clonedPublishedFileTableDetails.setEvaluatedColumnName(toBeClonedPublishedFileTableDetails
               .getEvaluatedColumnName());
      clonedPublishedFileTableDetails.setEvaluatedDataType(toBeClonedPublishedFileTableDetails.getEvaluatedDataType());
      clonedPublishedFileTableDetails
               .setEvaluatedPrecision(toBeClonedPublishedFileTableDetails.getEvaluatedPrecision());
      clonedPublishedFileTableDetails.setEvaluatedScale(toBeClonedPublishedFileTableDetails.getEvaluatedScale());
      clonedPublishedFileTableDetails.setOriginalEvaluatedDataType(toBeClonedPublishedFileTableDetails
               .getOriginalEvaluatedDataType());
      clonedPublishedFileTableDetails.setOriginalEvaluatedPrecision(toBeClonedPublishedFileTableDetails
               .getOriginalEvaluatedPrecision());
      clonedPublishedFileTableDetails.setOriginalEvaluatedScale(toBeClonedPublishedFileTableDetails
               .getOriginalEvaluatedScale());
      clonedPublishedFileTableDetails.setFormat(toBeClonedPublishedFileTableDetails.getFormat());
      clonedPublishedFileTableDetails.setGranularity(toBeClonedPublishedFileTableDetails.getGranularity());
      clonedPublishedFileTableDetails.setKdxDataType(toBeClonedPublishedFileTableDetails.getKdxDataType());
      clonedPublishedFileTableDetails.setPopulation(toBeClonedPublishedFileTableDetails.getPopulation());
      clonedPublishedFileTableDetails.setUnit(toBeClonedPublishedFileTableDetails.getUnit());
      clonedPublishedFileTableDetails.setUnitType(toBeClonedPublishedFileTableDetails.getUnitType());
      clonedPublishedFileTableDetails.setOriginalUnitType(toBeClonedPublishedFileTableDetails.getOriginalUnitType());
      return clonedPublishedFileTableDetails;
   }

   public static Membr cloneMember (Membr toBeClonedMembr) {
      Membr clonedMember = new Membr();
      clonedMember.setId(toBeClonedMembr.getId());
      clonedMember.setKdxLookupDescription(toBeClonedMembr.getKdxLookupDescription());
      clonedMember.setLongDescription(toBeClonedMembr.getLongDescription());
      clonedMember.setLookupColumn(toBeClonedMembr.getLookupColumn());
      clonedMember.setLookupDescription(toBeClonedMembr.getLookupDescription());
      clonedMember.setOriginalDescription(toBeClonedMembr.getOriginalDescription());
      clonedMember.setLookupValue(toBeClonedMembr.getLookupValue());
      clonedMember.setLowerLimit(toBeClonedMembr.getLowerLimit());
      clonedMember.setUpperLimit(toBeClonedMembr.getUpperLimit());
      clonedMember.setIndicatorBehavior(toBeClonedMembr.getIndicatorBehavior());
      clonedMember.setAssetEntityDefinitions(toBeClonedMembr.getAssetEntityDefinitions());
      return clonedMember;
   }

   public static Node cloneNode (Node nodeToBeCloned, String acrossMemberValue) {
      Node node = nodeToBeCloned.cloneNode(true);
      NodeList nodeList = node.getChildNodes();
      for (int childNode = 0; childNode < nodeList.getLength(); childNode++) {
         if (childNode == 1) {
            nodeList.item(childNode).setTextContent(acrossMemberValue);
         } else if (childNode > 1) {
            nodeList.item(childNode).setTextContent("N/A");
         }
      }
      return node;
   }

   public static User cloneUser (User user) {
      User newUser = new User();
      newUser.setFirstName(user.getFirstName());
      newUser.setLastName(user.getLastName());
      newUser.setUsername(user.getUsername());
      newUser.setIsPublisher(user.getIsPublisher());
      newUser.setId(user.getId());
      newUser.setPassword(user.getPassword());
      newUser.setSalt(user.getSalt());
      return newUser;
   }

   public static RIOntoTerm cloneRIOntoTerm (RIOntoTerm toBeClonedRIOntoTerm) {
      RIOntoTerm clonedRIOntoTerm = new RIOntoTerm();
      clonedRIOntoTerm.setConceptBEDID(toBeClonedRIOntoTerm.getConceptBEDID());
      clonedRIOntoTerm.setConceptName(toBeClonedRIOntoTerm.getConceptName());
      clonedRIOntoTerm.setEntityType(toBeClonedRIOntoTerm.getEntityType());
      clonedRIOntoTerm.setId(toBeClonedRIOntoTerm.getId());
      clonedRIOntoTerm.setInstanceBEDID(toBeClonedRIOntoTerm.getInstanceBEDID());
      clonedRIOntoTerm.setInstanceName(toBeClonedRIOntoTerm.getInstanceName());
      clonedRIOntoTerm.setProfileBEDID(toBeClonedRIOntoTerm.getProfileBEDID());
      clonedRIOntoTerm.setProfileName(toBeClonedRIOntoTerm.getProfileName());
      clonedRIOntoTerm.setRelationBEDID(toBeClonedRIOntoTerm.getRelationBEDID());
      clonedRIOntoTerm.setRelationName(toBeClonedRIOntoTerm.getRelationName());
      clonedRIOntoTerm.setWord(toBeClonedRIOntoTerm.getWord());
      clonedRIOntoTerm.setWordType(toBeClonedRIOntoTerm.getWordType());
      clonedRIOntoTerm.setModelGroupId(toBeClonedRIOntoTerm.getModelGroupId());
      clonedRIOntoTerm.setPopularity(toBeClonedRIOntoTerm.getPopularity());
      clonedRIOntoTerm.setKnowledgeId(toBeClonedRIOntoTerm.getKnowledgeId());
      clonedRIOntoTerm.setDetailTypeBedId(toBeClonedRIOntoTerm.getDetailTypeBedId());
      clonedRIOntoTerm.setDetailTypeName(toBeClonedRIOntoTerm.getDetailTypeName());
      clonedRIOntoTerm.setEntityBEDID(toBeClonedRIOntoTerm.getEntityBEDID());
      return clonedRIOntoTerm;
   }

   public static List<JobHistoryOperationalStatus> cloneJobOperationStatus (
            List<JobOperationalStatus> jobOperationalStatus) {
      List<JobHistoryOperationalStatus> jobHistoryOperationalStatus = new ArrayList<JobHistoryOperationalStatus>();
      for (JobOperationalStatus newJobOperationalStatus : jobOperationalStatus) {
         JobHistoryOperationalStatus newJobHistoryOperationalStatus = new JobHistoryOperationalStatus();
         newJobHistoryOperationalStatus.setJobRequest(newJobOperationalStatus.getJobRequest());
         newJobHistoryOperationalStatus.setJobType(newJobOperationalStatus.getJobType());
         newJobHistoryOperationalStatus.setOperationalStage(newJobOperationalStatus.getOperationalStage());
         newJobHistoryOperationalStatus.setJobStatus(newJobOperationalStatus.getJobStatus());
         newJobHistoryOperationalStatus.setStatusDetail(newJobOperationalStatus.getStatusDetail());
         newJobHistoryOperationalStatus.setStartDate(newJobOperationalStatus.getStartDate());
         newJobHistoryOperationalStatus.setEndDate(newJobOperationalStatus.getEndDate());
         newJobHistoryOperationalStatus.setUserId(newJobOperationalStatus.getUserId());
         jobHistoryOperationalStatus.add(newJobHistoryOperationalStatus);
      }
      return jobHistoryOperationalStatus;
   }

   public static List<TimeFrameNormalizedData> cloneTimeFrameNormalizedDataList (
            List<TimeFrameNormalizedData> toBeClonedTimeFrameNormalizedDataList) throws CloneNotSupportedException {
      List<TimeFrameNormalizedData> clonedTimeFrameNormalizedDataList = new ArrayList<TimeFrameNormalizedData>();
      for (TimeFrameNormalizedData timeFrameNormalizedData : toBeClonedTimeFrameNormalizedDataList) {
         if (NormalizedDataType.TIME_FRAME_NORMALIZED_DATA.equals(timeFrameNormalizedData.getNormalizedDataType())) {
            clonedTimeFrameNormalizedDataList.add((TimeFrameNormalizedData) timeFrameNormalizedData.clone());
         } else if (NormalizedDataType.RELATIVE_TIME_NORMALIZED_DATA.equals(timeFrameNormalizedData
                  .getNormalizedDataType())) {
            RelativeTimeNormalizedData relativeTimeNormalizedData = (RelativeTimeNormalizedData) timeFrameNormalizedData;
            clonedTimeFrameNormalizedDataList.add((TimeFrameNormalizedData) relativeTimeNormalizedData.clone());
         }
      }
      return clonedTimeFrameNormalizedDataList;
   }

   /**
    * This method will clone the query values
    * 
    * @param toBeClonedQueryValues
    * @return cloneQueryValues
    * @throws CloneNotSupportedException
    */
   public static List<QueryValue> cloneQueryValues (List<QueryValue> toBeClonedQueryValues,
            NormalizedDataType normalizedDataType) throws CloneNotSupportedException {
      List<QueryValue> clonedQueryValues = new ArrayList<QueryValue>();
      for (QueryValue toBeClonedQueryValue : toBeClonedQueryValues) {
         clonedQueryValues.add(cloneQueryValue(toBeClonedQueryValue, normalizedDataType));
      }
      return clonedQueryValues;
   }

   public static QueryValue cloneQueryValue (QueryValue toBeClonedQueryValue, NormalizedDataType normalizedDataType)
            throws CloneNotSupportedException {
      QueryValue clonedQueryValue = new QueryValue();
      clonedQueryValue.setDataType(toBeClonedQueryValue.getDataType());
      if (toBeClonedQueryValue.getNumberConversionId() != null) {
         clonedQueryValue.setNumberConversionId(toBeClonedQueryValue.getNumberConversionId().longValue());
      }
      clonedQueryValue.setValue(toBeClonedQueryValue.getValue());
      clonedQueryValue.setValueType(toBeClonedQueryValue.getValueType());
      clonedQueryValue.setTargetConversionFormat(toBeClonedQueryValue.getTargetConversionFormat());
      if (NormalizedDataType.TIME_FRAME_NORMALIZED_DATA.equals(normalizedDataType)) {
         TimeFrameNormalizedData timeFrameNormalizedData = (TimeFrameNormalizedData) toBeClonedQueryValue
                  .getNormalizedData();
         clonedQueryValue.setNormalizedData((TimeFrameNormalizedData) timeFrameNormalizedData.clone());
      } else if (NormalizedDataType.RELATIVE_TIME_NORMALIZED_DATA.equals(normalizedDataType)) {
         RelativeTimeNormalizedData relativeTimeNormalizedData = (RelativeTimeNormalizedData) toBeClonedQueryValue
                  .getNormalizedData();
         clonedQueryValue.setNormalizedData((RelativeTimeNormalizedData) relativeTimeNormalizedData.clone());
      }
      return clonedQueryValue;
   }

   public static RICloud cloneRICloud (RICloud riCloud) {
      RICloud clonedRICloud = new RICloud();
      clonedRICloud.setCloudCategory(riCloud.getCloudCategory());
      clonedRICloud.setCloudId(riCloud.getCloudId());
      clonedRICloud.setCloudName(riCloud.getCloudName());
      clonedRICloud.setCloudOutput(riCloud.getCloudOutput());
      clonedRICloud.setCloudOutputBusinessEntityId(riCloud.getCloudOutputBusinessEntityId());
      clonedRICloud.setCloudOutputName(riCloud.getCloudOutputName());
      clonedRICloud.setComponentBusinessEntityId(riCloud.getComponentBusinessEntityId());
      clonedRICloud.setComponentCategory(riCloud.getComponentCategory());
      clonedRICloud.setComponentName(riCloud.getComponentName());
      clonedRICloud.setComponentTypeBusinessEntityId(riCloud.getComponentTypeBusinessEntityId());
      clonedRICloud.setComponentTypeName(riCloud.getComponentTypeName());
      clonedRICloud.setDefaultValue(riCloud.getDefaultValue());
      clonedRICloud.setRepresentativeEntityType(riCloud.getRepresentativeEntityType());
      clonedRICloud.setFrequency(riCloud.getFrequency());
      clonedRICloud.setImportance(riCloud.getImportance());
      clonedRICloud.setModelGroupId(riCloud.getModelGroupId());
      clonedRICloud.setPrimaryRICloudId(riCloud.getPrimaryRICloudId());
      clonedRICloud.setRealizationBusinessEntityId(riCloud.getRealizationBusinessEntityId());
      clonedRICloud.setRealizationName(riCloud.getRealizationName());
      clonedRICloud.setRequired(riCloud.getRequired());
      clonedRICloud.setRequiredBehaviorBusinessEntityId(riCloud.getRequiredBehaviorBusinessEntityId());
      clonedRICloud.setRequiredBehaviorName(riCloud.getRequiredBehaviorName());
      clonedRICloud.setOutputComponent(riCloud.getOutputComponent());
      clonedRICloud.setCloudSelection(riCloud.getCloudSelection());
      return clonedRICloud;
   }

   public static DefaultDynamicValue cloneDefaultDynamicValue (DefaultDynamicValue ddv) {
      DefaultDynamicValue defaultDynamicValue = new DefaultDynamicValue();
      defaultDynamicValue.setAssetId(ddv.getAssetId());
      defaultDynamicValue.setDefaultValue(ddv.getDefaultValue());
      defaultDynamicValue.setLhsDEDId(ddv.getLhsDEDId());
      defaultDynamicValue.setQualifier(ddv.getQualifier());
      return defaultDynamicValue;
   }

   public static DefaultMetric cloneDefaultMetric (DefaultMetric defaultMetric) {
      DefaultMetric clonedDefaultMetric = new DefaultMetric();
      clonedDefaultMetric.setAedId(defaultMetric.getAedId());
      clonedDefaultMetric.setColumnName(defaultMetric.getColumnName());
      clonedDefaultMetric.setConceptName(defaultMetric.getConceptName());
      clonedDefaultMetric.setMappingId(defaultMetric.getMappingId());
      clonedDefaultMetric.setPopularity(defaultMetric.getPopularity());
      clonedDefaultMetric.setTableId(defaultMetric.getTableId());
      clonedDefaultMetric.setValid(defaultMetric.getValid());
      return clonedDefaultMetric;
   }

   public static Mapping cloneMapping (Mapping mapping) {
      Mapping clonedMapping = new Mapping();
      clonedMapping.setAssetEntityDefinition(mapping.getAssetEntityDefinition());
      clonedMapping.setBusinessEntityDefinition(mapping.getBusinessEntityDefinition());
      clonedMapping.setAssetGrainType(mapping.getAssetGrainType());
      clonedMapping.setPrimary(mapping.getPrimary());
      return clonedMapping;
   }

   public static List<SelectEntity> cloneSelectEntities (List<SelectEntity> toBeClonedSelectEntities)
            throws CloneNotSupportedException {
      if (ExecueCoreUtil.isCollectionEmpty(toBeClonedSelectEntities)) {
         return null;
      }
      List<SelectEntity> clonedSelectEntities = new ArrayList<SelectEntity>();
      for (SelectEntity selectEntity : toBeClonedSelectEntities) {
         clonedSelectEntities.add(cloneSelectEntity(selectEntity));
      }
      return clonedSelectEntities;
   }

   public static SelectEntity cloneSelectEntity (SelectEntity toBeClonedSelectEntity) throws CloneNotSupportedException {
      SelectEntity clonedSelectEntity = new SelectEntity();
      clonedSelectEntity.setAlias(toBeClonedSelectEntity.getAlias());
      clonedSelectEntity.setFormula(cloneQueryFormula(toBeClonedSelectEntity.getFormula()));
      clonedSelectEntity.setFunctionName(toBeClonedSelectEntity.getFunctionName());
      clonedSelectEntity.setRange(cloneRange(toBeClonedSelectEntity.getRange()));
      clonedSelectEntity.setType(toBeClonedSelectEntity.getType());
      if (toBeClonedSelectEntity.getRoundFunctionTargetColumn() != null) {
         clonedSelectEntity.setRoundFunctionTargetColumn(cloneQueryColumn(toBeClonedSelectEntity
                  .getRoundFunctionTargetColumn()));
      }
      if (SelectEntityType.VALUE.equals(toBeClonedSelectEntity.getType())) {
         clonedSelectEntity.setQueryValue(cloneQueryValue(toBeClonedSelectEntity.getQueryValue(), null));
      } else if (SelectEntityType.SUB_QUERY.equals(toBeClonedSelectEntity.getType())) {
         clonedSelectEntity.setSubQuery(toBeClonedSelectEntity.getSubQuery());
      } else if (SelectEntityType.TABLE_COLUMN.equals(toBeClonedSelectEntity.getType())) {
         clonedSelectEntity.setTableColumn(cloneQueryTableColumn(toBeClonedSelectEntity.getTableColumn()));
      }
      clonedSelectEntity.setDateAsStringRequired(toBeClonedSelectEntity.isDateAsStringRequired());
      return clonedSelectEntity;
   }

   public static List<FromEntity> cloneFromEntities (List<FromEntity> toBeClonedFromEntities)
            throws CloneNotSupportedException {
      if (ExecueCoreUtil.isCollectionEmpty(toBeClonedFromEntities)) {
         return null;
      }
      List<FromEntity> clonedFromEntities = new ArrayList<FromEntity>();
      for (FromEntity toBeClonedFromEntity : toBeClonedFromEntities) {
         clonedFromEntities.add(cloneFromEntity(toBeClonedFromEntity));
      }
      return clonedFromEntities;
   }

   public static FromEntity cloneFromEntity (FromEntity toBeClonedFromEntity) throws CloneNotSupportedException {
      if (toBeClonedFromEntity == null) {
         return null;
      }
      FromEntity clonedFromEntity = new FromEntity();

      clonedFromEntity.setType(toBeClonedFromEntity.getType());
      clonedFromEntity.setTable(cloneQueryTable(toBeClonedFromEntity.getTable()));
      clonedFromEntity.setSubQuery(cloneQuery(toBeClonedFromEntity.getSubQuery()));

      return clonedFromEntity;
   }

   public static List<JoinEntity> cloneJoinEntities (List<JoinEntity> toBeClonedJoinEntities)
            throws CloneNotSupportedException {
      if (ExecueCoreUtil.isCollectionEmpty(toBeClonedJoinEntities)) {
         return null;
      }
      List<JoinEntity> clonedJoinEntities = new ArrayList<JoinEntity>();
      for (JoinEntity toBeClonedJoinEntity : toBeClonedJoinEntities) {
         clonedJoinEntities.add(cloneJoinEntity(toBeClonedJoinEntity));
      }
      return clonedJoinEntities;
   }

   public static JoinEntity cloneJoinEntity (JoinEntity toBeClonedJoinEntity) throws CloneNotSupportedException {
      if (toBeClonedJoinEntity == null) {
         return null;
      }
      JoinEntity clonedJoinEntity = new JoinEntity();

      clonedJoinEntity.setLhsOperand(cloneQueryTableColumn(toBeClonedJoinEntity.getLhsOperand()));
      clonedJoinEntity.setRhsOperand(cloneQueryTableColumn(toBeClonedJoinEntity.getRhsOperand()));
      clonedJoinEntity.setType(toBeClonedJoinEntity.getType());

      return clonedJoinEntity;
   }

   public static List<ConditionEntity> cloneConditionalEntities (List<ConditionEntity> toBeClonedConditionEntities)
            throws CloneNotSupportedException {
      if (ExecueCoreUtil.isCollectionEmpty(toBeClonedConditionEntities)) {
         return null;
      }
      List<ConditionEntity> clonedConditionEntities = new ArrayList<ConditionEntity>();
      for (ConditionEntity toBeClonedConditionEntity : toBeClonedConditionEntities) {
         clonedConditionEntities.add(cloneConditionalEntity(toBeClonedConditionEntity));
      }
      return clonedConditionEntities;
   }

   public static ConditionEntity cloneConditionalEntity (ConditionEntity toBeClonedConditionEntity)
            throws CloneNotSupportedException {
      ConditionEntity clonedConditionEntity = new ConditionEntity();
      if (toBeClonedConditionEntity.isSubCondition()) {
         clonedConditionEntity.setSubCondition(true);
         List<ConditionEntity> clonedSubConditionEntities = new ArrayList<ConditionEntity>();
         for (ConditionEntity conditionEntity : toBeClonedConditionEntity.getSubConditionEntities()) {
            clonedSubConditionEntities.add(cloneConditionEntityExceptSubConditions(conditionEntity));
         }
         clonedConditionEntity.setSubConditionEntities(clonedSubConditionEntities);
      } else {
         clonedConditionEntity = cloneConditionEntityExceptSubConditions(toBeClonedConditionEntity);
      }
      return clonedConditionEntity;
   }

   public static ConditionEntity cloneConditionEntityExceptSubConditions (ConditionEntity toBeClonedConditionEntity)
            throws CloneNotSupportedException {
      ConditionEntity clonedConditionEntity = new ConditionEntity();
      clonedConditionEntity.setLhsFunctionName(toBeClonedConditionEntity.getLhsFunctionName());
      clonedConditionEntity.setOperandType(toBeClonedConditionEntity.getOperandType());
      clonedConditionEntity.setOperator(toBeClonedConditionEntity.getOperator());
      clonedConditionEntity.setLhsTableColumn(cloneQueryTableColumn(toBeClonedConditionEntity.getLhsTableColumn()));
      if (QueryConditionOperandType.VALUE.equals(toBeClonedConditionEntity.getOperandType())) {
         clonedConditionEntity.setRhsValues(cloneQueryValues(toBeClonedConditionEntity.getRhsValues(), null));
      } else if (QueryConditionOperandType.TABLE_COLUMN.equals(toBeClonedConditionEntity.getOperandType())) {
         List<QueryTableColumn> clonedQueryTableColums = new ArrayList<QueryTableColumn>();
         for (QueryTableColumn queryTableColumn : toBeClonedConditionEntity.getRhsTableColumns()) {
            clonedQueryTableColums.add(cloneQueryTableColumn(queryTableColumn));
         }
         clonedConditionEntity.setRhsTableColumns(clonedQueryTableColums);
      } else if (QueryConditionOperandType.SUB_QUERY.equals(toBeClonedConditionEntity.getOperandType())) {
         clonedConditionEntity.setRhsSubQuery(cloneQuery(toBeClonedConditionEntity.getRhsSubQuery()));
      }
      return clonedConditionEntity;
   }

   public static List<OrderEntity> cloneOrderEntities (List<OrderEntity> toBeClonedOrderEntities)
            throws CloneNotSupportedException {
      if (ExecueCoreUtil.isCollectionEmpty(toBeClonedOrderEntities)) {
         return null;
      }
      List<OrderEntity> clonedOrderEntities = new ArrayList<OrderEntity>();
      for (OrderEntity toBeClonedOrderEntity : toBeClonedOrderEntities) {
         clonedOrderEntities.add(cloneOrderEntity(toBeClonedOrderEntity));
      }
      return clonedOrderEntities;
   }

   public static OrderEntity cloneOrderEntity (OrderEntity toBeClonedOrderEntity) throws CloneNotSupportedException {
      if (toBeClonedOrderEntity == null) {
         return null;
      }
      OrderEntity clonedOrderEntity = new OrderEntity();

      clonedOrderEntity.setOrderType(toBeClonedOrderEntity.getOrderType());
      clonedOrderEntity.setSelectEntity(cloneSelectEntity(toBeClonedOrderEntity.getSelectEntity()));

      return clonedOrderEntity;
   }

   public static LimitEntity cloneLimitEntity (LimitEntity toBeClonedLimitEntity) throws CloneNotSupportedException {
      if (toBeClonedLimitEntity == null) {
         return null;
      }
      LimitEntity clonedLimitEntity = new LimitEntity();
      clonedLimitEntity.setStartingNumber(toBeClonedLimitEntity.getStartingNumber());
      clonedLimitEntity.setEndingNumber(toBeClonedLimitEntity.getEndingNumber());
      return clonedLimitEntity;
   }

   public static BusinessEntityDefinition cloneBusinessEntityDefinition (BusinessEntityDefinition toBeClonedBed) {
      // TODO : -VG- need to clone bed
      return toBeClonedBed;
   }

   public static Concept cloneConcept (Concept toBeClonedConcept) {
      // TODO : -VG- need to clone concept
      return toBeClonedConcept;
   }

   public static StructuredQuery cloneStructuredQuery (StructuredQuery toBeClonedStructuredQuery) {
      // TODO : -VG- need to clone structured query
      return toBeClonedStructuredQuery;
   }

   public static Query cloneQuery (Query toBeClonedQuery) throws CloneNotSupportedException {
      // TODO : -VG- need to clone query
      if (toBeClonedQuery == null) {
         return null;
      }
      Query clonedQuery = new Query();

      clonedQuery.setAlias(toBeClonedQuery.getAlias());
      clonedQuery.setSelectEntities(cloneSelectEntities(toBeClonedQuery.getSelectEntities()));
      clonedQuery.setFromEntities(cloneFromEntities(toBeClonedQuery.getFromEntities()));
      clonedQuery.setJoinEntities(cloneJoinEntities(toBeClonedQuery.getJoinEntities()));
      clonedQuery.setWhereEntities(cloneConditionalEntities(toBeClonedQuery.getWhereEntities()));
      clonedQuery.setGroupingEntities(cloneSelectEntities(toBeClonedQuery.getGroupingEntities()));
      clonedQuery.setOrderingEntities(cloneOrderEntities(toBeClonedQuery.getOrderingEntities()));
      clonedQuery.setHavingEntities(cloneConditionalEntities(toBeClonedQuery.getHavingEntities()));
      clonedQuery.setLimitingCondition(cloneLimitEntity(toBeClonedQuery.getLimitingCondition()));
      clonedQuery.setScalingFactorEntity(cloneQueryTableColumn(toBeClonedQuery.getScalingFactorEntity()));
      clonedQuery.setPopulationEntities(cloneSelectEntities(toBeClonedQuery.getPopulationEntities()));
      clonedQuery.setRollupQuery(toBeClonedQuery.isRollupQuery());

      return clonedQuery;
   }

   public static Range cloneRange (Range toBeClonedRange) {
      Range clonedRange = null;
      if (toBeClonedRange != null) {
         clonedRange = new Range();
         clonedRange.setId(toBeClonedRange.getId());
         clonedRange.setName(toBeClonedRange.getName());
         clonedRange.setDescription(toBeClonedRange.getDescription());
         clonedRange.setEnabled(toBeClonedRange.isEnabled());
         clonedRange.setConceptBedId(toBeClonedRange.getConceptBedId());
         clonedRange.setUser(cloneUser(toBeClonedRange.getUser()));
         if (ExecueCoreUtil.isCollectionNotEmpty(toBeClonedRange.getRangeDetails())) {
            Set<RangeDetail> clonedRangeDetail = new HashSet<RangeDetail>();
            for (RangeDetail rangeDetail : toBeClonedRange.getRangeDetails()) {
               clonedRangeDetail.add(cloneRangeDetail(rangeDetail));
            }
            clonedRange.setRangeDetails(clonedRangeDetail);
         }
      }
      return clonedRange;
   }

   private static RangeDetail cloneRangeDetail (RangeDetail toBeClonedRangeDetail) {
      RangeDetail rangeDetail = null;
      if (toBeClonedRangeDetail != null) {
         rangeDetail = new RangeDetail();
         rangeDetail.setId(toBeClonedRangeDetail.getId());
         rangeDetail.setDescription(toBeClonedRangeDetail.getDescription());
         if (toBeClonedRangeDetail.getLowerLimit() != null) {
            rangeDetail.setLowerLimit(toBeClonedRangeDetail.getLowerLimit().doubleValue());
         }
         if (toBeClonedRangeDetail.getUpperLimit() != null) {
            rangeDetail.setUpperLimit(toBeClonedRangeDetail.getUpperLimit().doubleValue());
         }
         rangeDetail.setOrder(toBeClonedRangeDetail.getOrder());
         rangeDetail.setValue(toBeClonedRangeDetail.getValue());        
      }
      return rangeDetail;
   }

   public static QueryFormula cloneQueryFormula (QueryFormula toBeClonedQueryFormula) {
      QueryFormula clonedQueryFormula = null;
      if (toBeClonedQueryFormula != null) {
         clonedQueryFormula = new QueryFormula();
         clonedQueryFormula.setArithmeticOperatorType(toBeClonedQueryFormula.getArithmeticOperatorType());
         clonedQueryFormula.setFirstOperandQueryTableColumn(cloneQueryTableColumn(toBeClonedQueryFormula
                  .getFirstOperandQueryTableColumn()));
         clonedQueryFormula.setFirstOperandType(toBeClonedQueryFormula.getFirstOperandType());
         clonedQueryFormula.setFirstOperandValue(toBeClonedQueryFormula.getFirstOperandValue());
         clonedQueryFormula.setQueryFormulaType(toBeClonedQueryFormula.getQueryFormulaType());
         clonedQueryFormula.setSecondOperandQueryTableColumn(cloneQueryTableColumn(toBeClonedQueryFormula
                  .getSecondOperandQueryTableColumn()));
         clonedQueryFormula.setSecondOperandType(toBeClonedQueryFormula.getSecondOperandType());
         clonedQueryFormula.setSecondOperandValue(toBeClonedQueryFormula.getSecondOperandValue());
         clonedQueryFormula.setStaticFormula(toBeClonedQueryFormula.getFirstOperandValue());
      }
      return clonedQueryFormula;
   }

   public static AnswersCatalogContext cloneAnswersCatalogContext (AnswersCatalogContext toBeClonedAnswersCatalogContext) {
      AnswersCatalogContext clonedAnswersCatalogContext = null;
      if (toBeClonedAnswersCatalogContext != null) {
         clonedAnswersCatalogContext = new AnswersCatalogContext();
         clonedAnswersCatalogContext.setAssetId(toBeClonedAnswersCatalogContext.getAssetId());
         clonedAnswersCatalogContext.setContextData(toBeClonedAnswersCatalogContext.getContextData());
         clonedAnswersCatalogContext.setLatestOperation(toBeClonedAnswersCatalogContext.getLatestOperation());
         clonedAnswersCatalogContext.setParentAssetId(toBeClonedAnswersCatalogContext.getParentAssetId());
         clonedAnswersCatalogContext.setUserId(toBeClonedAnswersCatalogContext.getUserId());
      }
      return clonedAnswersCatalogContext;
   }

   public static OptimalDSetContext cloneOptimalDSetContext (OptimalDSetContext toBeClonedOptimalDSetContext) {
      OptimalDSetContext optimalDSetContext = new OptimalDSetContext();
      if (toBeClonedOptimalDSetContext != null) {
         optimalDSetContext.setId(toBeClonedOptimalDSetContext.getId());
         optimalDSetContext.setJobRequest(toBeClonedOptimalDSetContext.getJobRequest());
         optimalDSetContext.setModelId(toBeClonedOptimalDSetContext.getModelId());
         optimalDSetContext.setOperationRequestLevel(toBeClonedOptimalDSetContext.getOperationRequestLevel());
      }
      return optimalDSetContext;
   }
}
