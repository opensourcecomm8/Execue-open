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


package com.execue.platform.swi.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.DynamicRangeInput;
import com.execue.core.common.bean.ExeCueResultSet;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.AssetEntityDefinition;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.Mapping;
import com.execue.core.common.bean.entity.Range;
import com.execue.core.common.bean.entity.RangeDetail;
import com.execue.core.common.bean.governor.BusinessEntityTerm;
import com.execue.core.common.bean.querygen.Query;
import com.execue.core.common.bean.querygen.QueryGenerationInput;
import com.execue.core.common.bean.querygen.QueryGenerationOutput;
import com.execue.core.common.bean.querygen.SelectEntity;
import com.execue.core.common.bean.querygen.SelectQueryInfo;
import com.execue.core.common.type.AssetType;
import com.execue.core.common.type.BusinessEntityType;
import com.execue.core.common.type.SelectEntityType;
import com.execue.core.common.type.StatType;
import com.execue.core.common.util.ExecueBeanManagementUtil;
import com.execue.core.configuration.ICoreConfigurationService;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.das.jdbc.service.IGenericJDBCService;
import com.execue.platform.configuration.IPlatformServicesConfigurationService;
import com.execue.platform.exception.RangeSuggestionException;
import com.execue.platform.swi.IRangeSuggestionService;
import com.execue.querygen.service.IQueryGenerationService;
import com.execue.querygen.service.QueryGenerationServiceFactory;
import com.execue.swi.exception.SWIException;
import com.execue.swi.exception.SWIExceptionCodes;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.swi.service.IMappingRetrievalService;

public class RangeSuggestionServiceImpl implements IRangeSuggestionService {

   private static final Logger                   logger = Logger.getLogger(RangeSuggestionServiceImpl.class);

   private IPlatformServicesConfigurationService platformServicesConfigurationService;
   private ICoreConfigurationService             coreConfigurationService;

   private IMappingRetrievalService              mappingRetrievalService;
   private IKDXRetrievalService                  kdxRetrievalService;
   private IGenericJDBCService                   genericJDBCService;
   private QueryGenerationServiceFactory         queryGenerationServiceFactory;

   public Range deduceRange (DynamicRangeInput dynamicRangeInput) throws RangeSuggestionException {
      Long modelId = dynamicRangeInput.getModelId();
      Long assetId = dynamicRangeInput.getAssetId();
      Long conceptBedId = dynamicRangeInput.getConceptBedId();
      int numBands = getCoreConfigurationService().getTotalRangeBands();
      double minRangeValue = 0.0;
      double maxRangeValue = 0.0;
      try {
         Concept concept = getKdxRetrievalService().getConceptByBEDId(conceptBedId);
         BusinessEntityTerm businessEntityTerm = new BusinessEntityTerm();
         businessEntityTerm.setBusinessEntityType(BusinessEntityType.CONCEPT);
         businessEntityTerm.setBusinessEntity(concept);
         List<Mapping> assetEntities = getMappingRetrievalService().getAssetEntities(businessEntityTerm, modelId,
                  assetId);
         for (Mapping mapping : assetEntities) {
            AssetEntityDefinition assetEntityDefinition = mapping.getAssetEntityDefinition();
            if (assetEntityDefinition.getAsset().getType().equals(AssetType.Relational)) {
               List<SelectEntity> selectEntities = new ArrayList<SelectEntity>();
               List<Query> queries = new ArrayList<Query>();
               Query query = new Query();
               SelectEntity minSelectEntity = new SelectEntity();
               minSelectEntity.setFunctionName(StatType.MINIMUM);
               minSelectEntity.setType(SelectEntityType.TABLE_COLUMN);
               minSelectEntity.setTableColumn(ExecueBeanManagementUtil.prepareQueryTableColumn(assetEntityDefinition
                        .getColum(), assetEntityDefinition.getTabl(), assetEntityDefinition.getTabl().getAlias()));
               selectEntities.add(minSelectEntity);
               SelectEntity maxSelectEntity = new SelectEntity();
               maxSelectEntity.setFunctionName(StatType.MAXIMUM);
               maxSelectEntity.setType(SelectEntityType.TABLE_COLUMN);
               maxSelectEntity.setTableColumn(ExecueBeanManagementUtil.prepareQueryTableColumn(assetEntityDefinition
                        .getColum(), assetEntityDefinition.getTabl(), assetEntityDefinition.getTabl().getAlias()));
               selectEntities.add(maxSelectEntity);
               query.setSelectEntities(selectEntities);
               QueryGenerationInput queryGenerationInput = new QueryGenerationInput();
               queryGenerationInput.setTargetAsset(assetEntityDefinition.getAsset());
               queries.add(query);
               queryGenerationInput.setInputQueries(queries);
               QueryGenerationOutput queryGenerationOutput = getQueryGenerationService(assetEntityDefinition.getAsset())
                        .generateQuery(queryGenerationInput);
               String minmaxQuery = getQueryGenerationService(assetEntityDefinition.getAsset())
                        .extractQueryRepresentation(assetEntityDefinition.getAsset(),
                                 queryGenerationOutput.getResultQuery()).getQueryString();
               ExeCueResultSet executeMinMaxQuery = getGenericJDBCService().executeQuery(
                        assetEntityDefinition.getAsset().getDataSource().getName(),
                        new SelectQueryInfo(minmaxQuery.toString()));
               // TODO : -VG- check for null cases
               while (executeMinMaxQuery.next()) {
                  minRangeValue = Double.parseDouble(executeMinMaxQuery.getString(0));
                  maxRangeValue = Double.parseDouble(executeMinMaxQuery.getString(1));
               }
               break;
            }
         }

         // TODO: -RG- move this logic to Dynamic Range Processor's getSimpleRangeDefintion() method
         Range range = new Range();
         range.setConceptBedId(conceptBedId);
         range.setName("Range " + concept.getDisplayName());
         range.setDescription("Range " + concept.getDisplayName());
         // By defaults marked range as enabled
         range.setEnabled(true);
         Set<RangeDetail> rangeDetails = new HashSet<RangeDetail>();
         double rangeDiff = maxRangeValue - minRangeValue;
         double rangeDetailDiff = rangeDiff / numBands;
         for (int i = 0; i < numBands; i++) {
            RangeDetail rangeDetail = new RangeDetail();
            if (i != 0) {
               rangeDetail.setLowerLimit(ExecueCoreUtil.getSmoothenedNumber(minRangeValue + i * rangeDetailDiff));
            }
            if (i != numBands - 1) {
               rangeDetail.setUpperLimit(ExecueCoreUtil.getSmoothenedNumber(minRangeValue + (i + 1) * rangeDetailDiff));
            }
            if (rangeDetail.getLowerLimit() == null) {
               rangeDetail.setDescription(getPlatformServicesConfigurationService().getRangeLowerLimit()
                        + getPlatformServicesConfigurationService().getRangeDescriptionDelimiter()
                        + ExecueCoreUtil.getSmoothenedNumberDescription(ExecueCoreUtil.getSmoothenedNumber(rangeDetail
                                 .getUpperLimit())));
            } else if (rangeDetail.getUpperLimit() == null) {
               rangeDetail.setDescription(ExecueCoreUtil.getSmoothenedNumberDescription(ExecueCoreUtil
                        .getSmoothenedNumber(rangeDetail.getLowerLimit()))
                        + getPlatformServicesConfigurationService().getRangeDescriptionDelimiter()
                        + getPlatformServicesConfigurationService().getRangeUpperLimit());
            } else {
               rangeDetail.setDescription(ExecueCoreUtil.getSmoothenedNumberDescription(ExecueCoreUtil
                        .getSmoothenedNumber(rangeDetail.getLowerLimit()))
                        + getPlatformServicesConfigurationService().getRangeDescriptionDelimiter()
                        + ExecueCoreUtil.getSmoothenedNumberDescription(ExecueCoreUtil.getSmoothenedNumber(rangeDetail
                                 .getUpperLimit())));
            }
            rangeDetail.setOrder(i);
            rangeDetail.setValue(String.valueOf(i));
            rangeDetails.add(rangeDetail);
         }
         range.setRangeDetails(rangeDetails);
         return range;
      } catch (SWIException swiException) {
         swiException.printStackTrace();
         throw new RangeSuggestionException(swiException.getCode(), swiException);
      } catch (Exception exception) {
         exception.printStackTrace();
         throw new RangeSuggestionException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, exception);
      }
   }

   private IQueryGenerationService getQueryGenerationService (Asset asset) {
      return getQueryGenerationServiceFactory().getQueryGenerationService(asset);
   }

   public IMappingRetrievalService getMappingRetrievalService () {
      return mappingRetrievalService;
   }

   public void setMappingRetrievalService (IMappingRetrievalService mappingRetrievalService) {
      this.mappingRetrievalService = mappingRetrievalService;
   }

   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }

   public IGenericJDBCService getGenericJDBCService () {
      return genericJDBCService;
   }

   public void setGenericJDBCService (IGenericJDBCService genericJDBCService) {
      this.genericJDBCService = genericJDBCService;
   }

   public QueryGenerationServiceFactory getQueryGenerationServiceFactory () {
      return queryGenerationServiceFactory;
   }

   public void setQueryGenerationServiceFactory (QueryGenerationServiceFactory queryGenerationServiceFactory) {
      this.queryGenerationServiceFactory = queryGenerationServiceFactory;
   }

   /**
    * @return the platformServicesConfigurationService
    */
   public IPlatformServicesConfigurationService getPlatformServicesConfigurationService () {
      return platformServicesConfigurationService;
   }

   /**
    * @param platformServicesConfigurationService
    *           the platformServicesConfigurationService to set
    */
   public void setPlatformServicesConfigurationService (
            IPlatformServicesConfigurationService platformServicesConfigurationService) {
      this.platformServicesConfigurationService = platformServicesConfigurationService;
   }

   public ICoreConfigurationService getCoreConfigurationService () {
      return coreConfigurationService;
   }

   public void setCoreConfigurationService (ICoreConfigurationService coreConfigurationService) {
      this.coreConfigurationService = coreConfigurationService;
   }
}
