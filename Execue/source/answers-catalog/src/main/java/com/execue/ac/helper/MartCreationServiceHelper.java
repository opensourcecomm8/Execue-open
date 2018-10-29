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


package com.execue.ac.helper;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.execue.ac.bean.ConceptColumnMapping;
import com.execue.ac.bean.MartCreationInputInfo;
import com.execue.ac.bean.MartCreationOutputInfo;
import com.execue.ac.bean.MartCreationPopulatedContext;
import com.execue.ac.exception.AnswersCatalogException;
import com.execue.ac.exception.AnswersCatalogExceptionCodes;
import com.execue.ac.util.AnswersCatalogUtil;
import com.execue.core.common.bean.ExeCueResultSet;
import com.execue.core.common.bean.StatusEnum;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.AssetCreationInfo;
import com.execue.core.common.bean.entity.DataSource;
import com.execue.core.common.bean.entity.Membr;
import com.execue.core.common.bean.entity.Tabl;
import com.execue.core.common.bean.entity.wrapper.TableInfo;
import com.execue.core.common.bean.querygen.ConditionEntity;
import com.execue.core.common.bean.querygen.LimitEntity;
import com.execue.core.common.bean.querygen.Query;
import com.execue.core.common.bean.querygen.QueryColumn;
import com.execue.core.common.bean.querygen.QueryTable;
import com.execue.core.common.bean.querygen.SelectQueryInfo;
import com.execue.core.common.type.AssetOwnerType;
import com.execue.core.common.type.AssetSubType;
import com.execue.core.common.type.AssetType;
import com.execue.core.common.type.CheckType;
import com.execue.platform.IVirtualTableManagementService;
import com.execue.swi.exception.MappingException;
import com.execue.swi.exception.SDXException;
import com.execue.swi.exception.SWIException;

/**
 * This class contains helper methods specific to mart process
 * 
 * @author Vishay
 * @version 1.0
 * @since 26/01/2011
 */
public class MartCreationServiceHelper extends AnswersCatalogServiceHelper {

   private static final Logger            logger = Logger.getLogger(MartCreationServiceHelper.class);

   private IVirtualTableManagementService virtualTableManagementService;

   /**
    * This method gets the cube assets came out of sourceAssetId mapped to this concept bed id.
    * 
    * @param conceptBedId
    * @param sourceAssetId
    * @return assets
    * @throws AnswersCatalogException
    */
   public List<Asset> getCubeAssetsHavingMappedConceptBedId (Long conceptBedId, Long sourceAssetId)
            throws AnswersCatalogException {
      try {
         return getMappingRetrievalService().getAssetsByConceptBedIdAndAssetType(conceptBedId, AssetType.Cube,
                  sourceAssetId);
      } catch (MappingException e) {
         throw new AnswersCatalogException(AnswersCatalogExceptionCodes.DEFAULT_ANSWERS_CATALOG_EXCEPTION_CODE, e);
      }
   }

   /**
    * This method creates virtual table for asset passed
    * 
    * @param asset
    * @param actualTable
    * @param virtualTable
    * @return virtualTableInfo
    * @throws AnswersCatalogException
    */
   public TableInfo createVirtualTable (Asset asset, Tabl actualTable, Tabl virtualTable)
            throws AnswersCatalogException {
      try {
         TableInfo virtualTableInfo = getVirtualTableManagementService().prepareVirtualTableInfo(asset, actualTable,
                  virtualTable);
         getVirtualTableManagementService().createVirtualTableInfo(asset, virtualTableInfo);
         return virtualTableInfo;
      } catch (SWIException e) {
         throw new AnswersCatalogException(AnswersCatalogExceptionCodes.DEFAULT_ANSWERS_CATALOG_EXCEPTION_CODE, e);
      }
   }

   /**
    * This method copies the information from source member to target member
    * 
    * @param sourceMember
    * @param targetMember
    * @throws AnswersCatalogException
    */
   public void copyMemberProperties (Membr sourceMember, Membr targetMember, CheckType virtualTable)
            throws AnswersCatalogException {
      try {
         // in case of virtual table, target members can be less than source members based on sampling.
         if (CheckType.YES.equals(virtualTable)) {
            if (targetMember != null) {
               copyMemberProperties(sourceMember, targetMember);
            }
         } else {
            copyMemberProperties(sourceMember, targetMember);
         }
      } catch (SDXException e) {
         throw new AnswersCatalogException(AnswersCatalogExceptionCodes.DEFAULT_ANSWERS_CATALOG_EXCEPTION_CODE, e);
      }
   }

   /**
    * This method updates the asset properties for mart from source asset
    * 
    * @param sourceAsset
    * @param targetAsset
    * @throws AnswersCatalogException
    */
   public void updateAssetProperties (Asset sourceAsset, Asset targetAsset, AssetCreationInfo assetCreationInfo)
            throws AnswersCatalogException {
      try {
         targetAsset.setBaseAssetId(sourceAsset.getId());
         targetAsset.setStatus(StatusEnum.INACTIVE);
         targetAsset.setType(AssetType.Mart);
         targetAsset.setSubType(AssetSubType.ExeCueSampled);
         targetAsset.setOwnerType(AssetOwnerType.ExeCue);
         getSdxManagementService().updateAsset(targetAsset);
         copyAssetDetailProperties(sourceAsset, targetAsset, assetCreationInfo);
      } catch (SDXException e) {
         throw new AnswersCatalogException(AnswersCatalogExceptionCodes.DEFAULT_ANSWERS_CATALOG_EXCEPTION_CODE, e);
      }
   }

   /**
    * This method populates the id column with rownum value for oracle.
    * 
    * @param targetDataSource
    * @param tableName
    * @param idColumn
    * @throws AnswersCatalogException
    */
   public void populateIdColumnData (DataSource targetDataSource, String tableName, QueryColumn idColumn)
            throws AnswersCatalogException {
      // update statement for id population with rownum.
      QueryTable queryTable = AnswersCatalogUtil.createQueryTable(tableName, targetDataSource.getOwner());
      List<ConditionEntity> setConditions = new ArrayList<ConditionEntity>();
      setConditions.add(AnswersCatalogUtil.prepareConditionEntity(queryTable, idColumn, ORACLE_ROWNUM_VALUE));
      getAnswersCatalogDataAccessService().executeUpdateStatement(targetDataSource, queryTable, setConditions);
   }

   /**
    * This method prepares the condition entity for population based on population data passed
    * 
    * @param martCreationInputInfo
    * @param limitEntity
    * @param baseQueryForRecordsPopulationQuery
    * @param targetAsset
    * @param populationValues
    * @param maxAllowedExpressionsInCondition
    * @param useSubConditionToProcessPopulationData
    * @return conditionEntity
    * @throws Exception
    */
   public ConditionEntity prepareConditionalEntityForPopulation (MartCreationInputInfo martCreationInputInfo,
            Asset targetAsset, Query baseQueryForRecordsPopulationQuery, LimitEntity limitEntity,
            Integer maxAllowedExpressionsInCondition) throws Exception {
      ConditionEntity conditionEntity = new ConditionEntity();
      // get the population data by limit
      List<String> populationValues = getPopulationDataByLimitEntity(targetAsset, baseQueryForRecordsPopulationQuery,
               limitEntity);
      MartCreationPopulatedContext martCreationPopulatedContext = martCreationInputInfo
               .getMartCreationPopulatedContext();
      ConceptColumnMapping populatedPopulation = martCreationPopulatedContext.getSourceAssetMappingInfo()
               .getPopulatedPopulation();
      // it build sub conditions based on data count.
      List<ConditionEntity> subConditionEntities = new ArrayList<ConditionEntity>();
      for (int startIndex = 0; startIndex < populationValues.size(); startIndex = startIndex
               + maxAllowedExpressionsInCondition) {
         int endIndex = startIndex + maxAllowedExpressionsInCondition;
         if (endIndex > populationValues.size()) {
            endIndex = populationValues.size();
         }
         List<String> batchList = populationValues.subList(startIndex, endIndex);
         subConditionEntities.add(AnswersCatalogUtil.prepareConditionEntity(populatedPopulation.getQueryTable(),
                  populatedPopulation.getQueryColumn(), batchList));
      }
      if (subConditionEntities.size() == 1) {
         conditionEntity = subConditionEntities.get(0);
      } else {
         conditionEntity.setSubCondition(true);
         conditionEntity.setSubConditionEntities(subConditionEntities);
      }
      return conditionEntity;
   }

   /**
    * This method returns the population data by adding limit entity to it. It reads records from population table based
    * on slices to be picked
    * 
    * @param targetAsset
    * @param baseQueryForRecordsPopulationQuery
    * @param limitEntity
    * @return populationData
    * @throws Exception
    */
   public List<String> getPopulationDataByLimitEntity (Asset targetAsset, Query baseQueryForRecordsPopulationQuery,
            LimitEntity limitEntity) throws Exception {
      baseQueryForRecordsPopulationQuery.setLimitingCondition(limitEntity);
      String recordsPopulationQueryString = getAnswersCatalogSQLQueryGenerationService().prepareSelectQuery(
               targetAsset, baseQueryForRecordsPopulationQuery);
      if (logger.isDebugEnabled()) {
         logger.debug("Population Records Select Query : " + recordsPopulationQueryString);
      }
      // TODO:: RG SAS :: Need to replace the below call with client source dao for sas limit handling 
      ExeCueResultSet recordPopulationResultSet = getAnswersCatalogDataAccessService().executeSQLQuery(
               targetAsset.getDataSource().getName(), new SelectQueryInfo(recordsPopulationQueryString));
      List<String> populationData = new ArrayList<String>();
      while (recordPopulationResultSet.next()) {
         populationData.add(recordPopulationResultSet.getString(0));
      }
      return populationData;
   }

   public AssetCreationInfo buildAssetCreationInfo (MartCreationOutputInfo martCreationOutputInfo) {
      AssetCreationInfo assetCreationInfo = new AssetCreationInfo();

      if (martCreationOutputInfo.getMartCreationInputInfo().getSamplingAlgorithmStaticInput() != null) {
         assetCreationInfo.setConfidenceLevel(martCreationOutputInfo.getMartCreationInputInfo()
                  .getSamplingAlgorithmStaticInput().getConfidenceLevelPercentage());
         assetCreationInfo.setErrorRate(martCreationOutputInfo.getMartCreationInputInfo()
                  .getSamplingAlgorithmStaticInput().getErrorRatePercentage());
      } else {
         assetCreationInfo.setConfidenceLevel(100.00 * martCreationOutputInfo.getMartCreationInputInfo()
                  .getBasicSamplingAlgorithmStaticInput().getConfidenceLevel());
         assetCreationInfo.setErrorRate(100.00 * martCreationOutputInfo.getMartCreationInputInfo()
                  .getBasicSamplingAlgorithmStaticInput().getErrorRate());
      }

      Double sample = 100.00
               * martCreationOutputInfo.getFractionalPopulationTable().getPopulationCount()
               * 1.00
               / martCreationOutputInfo.getMartCreationInputInfo().getMartCreationInputParametersContext()
                        .getTotalPopulationRecordCount() * 1.00;
      sample = AnswersCatalogUtil.getRoundedValue(sample, 2);
      assetCreationInfo.setSample(sample);

      List<Long> populationBEDIDs = new ArrayList<Long>();
      populationBEDIDs.add(martCreationOutputInfo.getMartCreationInputInfo().getMartCreationPopulatedContext()
               .getSourceAssetMappingInfo().getPopulatedPopulation().getBusinessEntityDefinition().getId());
      assetCreationInfo.setPopulationBEDIDs(populationBEDIDs);
      assetCreationInfo.setDimensionBEDIDs(martCreationOutputInfo.getMartCreationInputInfo().getMartCreationContext()
               .getProminentDimensionBEDIDs());
      assetCreationInfo.setMeasureBEDIDs(martCreationOutputInfo.getMartCreationInputInfo().getMartCreationContext()
               .getProminentMeasureBEDIDs());

      List<ConceptColumnMapping> distributions = martCreationOutputInfo.getMartCreationInputInfo()
               .getMartCreationPopulatedContext().getSourceAssetMappingInfo().getPopulatedDistributions();

      assetCreationInfo.setDistributionBEDIDs(getBEDIDsFromConceptColumnMappings(distributions));

      return assetCreationInfo;
   }

   public IVirtualTableManagementService getVirtualTableManagementService () {
      return virtualTableManagementService;
   }

   public void setVirtualTableManagementService (IVirtualTableManagementService virtualTableManagementService) {
      this.virtualTableManagementService = virtualTableManagementService;
   }

}
