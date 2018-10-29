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


package com.execue.platform.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.ExeCueResultSet;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.DataSource;
import com.execue.core.common.bean.entity.Mapping;
import com.execue.core.common.bean.entity.Tabl;
import com.execue.core.common.bean.querygen.ConditionEntity;
import com.execue.core.common.bean.querygen.FromEntity;
import com.execue.core.common.bean.querygen.Query;
import com.execue.core.common.bean.querygen.QueryColumn;
import com.execue.core.common.bean.querygen.QueryRepresentation;
import com.execue.core.common.bean.querygen.QueryTable;
import com.execue.core.common.bean.querygen.QueryTableColumn;
import com.execue.core.common.bean.querygen.SelectEntity;
import com.execue.core.common.type.AssetProviderType;
import com.execue.core.common.type.DataType;
import com.execue.core.common.type.OperatorType;
import com.execue.core.common.type.SamplingStrategy;
import com.execue.core.common.type.StatType;
import com.execue.core.common.util.ExecueBeanCloneUtil;
import com.execue.core.common.util.ExecueBeanManagementUtil;
import com.execue.dataaccess.clientsource.IClientSourceDAO;
import com.execue.platform.ISamplingStrategyIdentificationService;
import com.execue.platform.configuration.IPlatformServicesConfigurationService;
import com.execue.platform.exception.PlatformException;
import com.execue.platform.exception.PlatformServicesExceptionCodes;
import com.execue.querygen.service.IQueryGenerationService;
import com.execue.querygen.service.QueryGenerationServiceFactory;
import com.execue.swi.exception.MappingException;
import com.execue.swi.exception.SDXException;
import com.execue.swi.service.IMappingRetrievalService;
import com.execue.swi.service.ISDXRetrievalService;

/**
 * @author Vishay
 * @version 1.0
 * @since 11/01/2011
 */
public class SamplingStrategyIdentificationServiceImpl implements ISamplingStrategyIdentificationService {

   private static final Logger                   log = Logger
                                                              .getLogger(SamplingStrategyIdentificationServiceImpl.class);

   private ISDXRetrievalService                  sdxRetrievalService;
   private IMappingRetrievalService              mappingRetrievalService;
   private IClientSourceDAO                      clientSourceDAO;
   private IPlatformServicesConfigurationService platformServicesConfigurationService;

   @Override
   public SamplingStrategy identifyCorrectSampingStrategy (Long modelId, Long conceptId, Long assetId)
            throws PlatformException {
      // default strategy is mean
      SamplingStrategy samplingStrategy = SamplingStrategy.MEAN;
      try {
         Asset asset = getSdxRetrievalService().getAssetById(assetId);
         DataSource dataSource = getSdxRetrievalService().getDataSourceByAssetId(assetId);
         asset.setDataSource(dataSource);
         Mapping primaryMapping = getMappingRetrievalService().getPrimaryMapping(conceptId, modelId, assetId);
         // check is it is proportion
         if (isProportionSamplingStrategy(primaryMapping, asset, dataSource)) {
            samplingStrategy = SamplingStrategy.PROPORTION;
            // check is it is sub-grouping
         } else if (isSubGroupingSamplingStrategy(primaryMapping, dataSource, asset)) {
            samplingStrategy = SamplingStrategy.SUB_GROUPING;
         }
      } catch (MappingException e) {
         throw new PlatformException(e.getCode(), e);
      } catch (SDXException e) {
         throw new PlatformException(e.getCode(), e);
      } catch (Exception e) {
         throw new PlatformException(PlatformServicesExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
      return samplingStrategy;
   }

   // proportion means only two values and that too is zero and 1.
   private boolean isProportionSamplingStrategy (Mapping primaryMapping, Asset asset, DataSource dataSource)
            throws Exception {
      Tabl table = primaryMapping.getAssetEntityDefinition().getTabl();
      Colum column = primaryMapping.getAssetEntityDefinition().getColum();
      QueryTable queryTable = ExecueBeanManagementUtil.prepareQueryTable(table);
      QueryColumn queryColumn = ExecueBeanManagementUtil.prepareQueryColumn(column);
      queryColumn.setDistinct(true);
      QueryTableColumn queryTableColumn = ExecueBeanManagementUtil.prepareQueryTableColumn(queryColumn, queryTable);
      Query proportionSamplingStrategyQuery = prepareProportionSamplingStrategyQuery(queryTableColumn);
      QueryRepresentation queryRepresentation = getQueryGenerationService(dataSource.getProviderType())
               .extractQueryRepresentation(asset, proportionSamplingStrategyQuery);
      ExeCueResultSet exeCueResultSet = getClientSourceDAO().executeQuery(dataSource, proportionSamplingStrategyQuery,
               queryRepresentation.getQueryString());
      return exeCueResultSet.next();
   }

   private Query prepareProportionSamplingStrategyQuery (QueryTableColumn queryTableColumn) {
      // query to check proportion based sampling. it means distinct count of records 2 and that too values should be 0
      // and 1.
      // SELECT DISTINCT currency_code
      // FROM shipping_order, (SELECT COUNT(DISTINCT currency_code) AS distinctRecordCount FROM shipping_order)
      // inlineQueryAlias
      // WHERE inlineQueryAlias.distinctRecordCount = 2 and currency_code in (0,1);
      // currency_code = column
      // shipping_order = table
      String inlineQueryColumnName = "DISTINCTRECORDCOUNT";
      Query proportionSamplingStrategyInlineQuery = prepareProportionSamplingStrategyInlineQuery(queryTableColumn,
               inlineQueryColumnName);
      List<SelectEntity> selectEntities = ExecueBeanManagementUtil.prepareSelectEntities(queryTableColumn,
               queryTableColumn.getColumn().getColumnName(), null);
      List<FromEntity> fromEntities = new ArrayList<FromEntity>();
      fromEntities.add(ExecueBeanManagementUtil.createFromEntityObject(proportionSamplingStrategyInlineQuery));
      fromEntities.add(ExecueBeanManagementUtil.createFromEntityObject(queryTableColumn.getTable()));
      Query query = new Query();
      query.setSelectEntities(selectEntities);
      query.setFromEntities(fromEntities);
      query.setWhereEntities(prepareConditionEntities(queryTableColumn, proportionSamplingStrategyInlineQuery,
               inlineQueryColumnName));
      return query;
   }

   private List<ConditionEntity> prepareConditionEntities (QueryTableColumn queryTableColumn,
            Query proportionSamplingStrategyInlineQuery, String inlineQueryColumnAlias) {
      List<ConditionEntity> conditionEntities = new ArrayList<ConditionEntity>();
      QueryColumn clonedQueryColumn = ExecueBeanCloneUtil.cloneQueryColumn(queryTableColumn.getColumn());
      clonedQueryColumn.setDistinct(false);
      conditionEntities.add(ExecueBeanManagementUtil.prepareConditionEntity(queryTableColumn.getTable(),
               clonedQueryColumn, getPlatformServicesConfigurationService()
                        .getSamplingStrategyAllowedProportionValues(), OperatorType.IN));
      QueryTable queryTable = new QueryTable();
      queryTable.setAlias(proportionSamplingStrategyInlineQuery.getAlias());
      QueryColumn queryColumn = new QueryColumn();
      queryColumn.setColumnName(inlineQueryColumnAlias);
      queryColumn.setDataType(DataType.INT);
      List<String> rhsValues = new ArrayList<String>();
      rhsValues.add("2");
      conditionEntities.add(ExecueBeanManagementUtil.prepareConditionEntity(queryTable, queryColumn, rhsValues,
               OperatorType.EQUALS));
      return conditionEntities;
   }

   private Query prepareProportionSamplingStrategyInlineQuery (QueryTableColumn queryTableColumn,
            String inlineQueryColumnAlias) {
      List<SelectEntity> selectEntities = ExecueBeanManagementUtil.prepareSelectEntities(queryTableColumn,
               inlineQueryColumnAlias, StatType.COUNT);
      List<FromEntity> fromEntities = ExecueBeanManagementUtil.createFromEntities(queryTableColumn.getTable());
      Query query = new Query();
      query.setSelectEntities(selectEntities);
      query.setFromEntities(fromEntities);
      query.setAlias("INLINEQUERYALIAS");
      return query;
   }

   // sub grouping means data is not normally distributed, hence we need to go for sub grouping.
   private boolean isSubGroupingSamplingStrategy (Mapping primaryMapping, DataSource dataSource, Asset asset)
            throws Exception {
      boolean isSubGroupingSamplingStrategy = false;
      Tabl table = primaryMapping.getAssetEntityDefinition().getTabl();
      Colum column = primaryMapping.getAssetEntityDefinition().getColum();
      QueryTable queryTable = ExecueBeanManagementUtil.prepareQueryTable(table);
      QueryColumn queryColumn = ExecueBeanManagementUtil.prepareQueryColumn(column);
      QueryTableColumn queryTableColumn = ExecueBeanManagementUtil.prepareQueryTableColumn(queryColumn, queryTable);
      Query subGroupingBasedSamplingStrategyQuery = prepareSubGroupingBasedSamplingStrategyQuery(queryTableColumn);
      QueryRepresentation queryRepresentation = getQueryGenerationService(dataSource.getProviderType())
               .extractQueryRepresentation(asset, subGroupingBasedSamplingStrategyQuery);
      ExeCueResultSet exeCueResultSet = getClientSourceDAO().executeQuery(dataSource,
               subGroupingBasedSamplingStrategyQuery, queryRepresentation.getQueryString());
      if (exeCueResultSet.next()) {
         StatInfo statInfo = populateStatInfo(exeCueResultSet);
         isSubGroupingSamplingStrategy = !isDataDistributionLikeBellCurve(statInfo, primaryMapping, dataSource, asset);
      }
      return isSubGroupingSamplingStrategy;
   }

   /**
    * This method is too simple test and too stringent, use isDataDistributionLikeBellCurve instead
    * 
    * @param statInfo
    * @return
    */
   private boolean isDataDistributionLikeBellCurveSimple (StatInfo statInfo) {
      // formula is calculate theoritcal mean and theoritcal stddev and see if they are close to actual mean and stdev
      // formula for theortical mean and stdev
      // mean is (min + max) /2
      // stdev = (((max-min)*(max-min))/12) power to 1/2
      Double minValue = statInfo.getMinValue();
      Double maxValue = statInfo.getMaxValue();
      Double theorticalMean = (minValue + maxValue) / 2;
      Double theorticalStddev = Math.pow((Math.pow((maxValue - minValue), 2) / 12), 0.5);
      boolean theorticalMeanValueAllowedPerVariationsAllowed = isTheorticalValueAllowedPerVariationsAllowed(statInfo
               .getMeanValue(), theorticalMean);
      boolean theorticalStddevValueAllowedPerVariationsAllowed = isTheorticalValueAllowedPerVariationsAllowed(statInfo
               .getStddevValue(), theorticalStddev);
      return theorticalMeanValueAllowedPerVariationsAllowed && theorticalStddevValueAllowedPerVariationsAllowed;
   }

   /**
    * Data is Normally distributed if all of the below are true
    *   1) The calculated mean M is close to theoretical mean of (max+min)/2
    *   2) The calculated SD is close to theoretical standard deviation of (max-min)/6.8
    *      (about 3.4*Sigma on each side of Mean to form the bell curve)
    *   3) About 95% of variable count is between (M-2*SD and M+2*SD)
    *   Closeness means about 10% which is 20% covering both sides
    * 
    * @param statInfo
    * @return
    * @throws Exception 
    */
   private boolean isDataDistributionLikeBellCurve (StatInfo statInfo, Mapping primaryMapping, DataSource dataSource,
            Asset sourceAsset) throws Exception {
      Double minValue = statInfo.getMinValue();
      Double maxValue = statInfo.getMaxValue();
      Double theorticalMean = (minValue + maxValue) / 2;
      Double theorticalStddev = (maxValue - minValue) / 6.8d;

      boolean theorticalMeanValueAllowedPerVariationsAllowed = isTheorticalValueAllowedPerVariationsAllowed(statInfo
               .getMeanValue(), theorticalMean);

      boolean theorticalStddevValueAllowedPerVariationsAllowed = isTheorticalValueAllowedPerVariationsAllowed(statInfo
               .getStddevValue(), theorticalStddev);

      Double theoriticalMinValue = statInfo.getMeanValue() - 2 * statInfo.getStddevValue();
      Double theoriticalMaxValue = statInfo.getMeanValue() + 2 * statInfo.getStddevValue();
      boolean recCountCovers95PercentForTheoriticalBoundaries = isRecordCount95PercentOfTotalRecordCount(
               primaryMapping, dataSource, sourceAsset, theoriticalMinValue, theoriticalMaxValue, statInfo
                        .getCountValue());

      if (log.isDebugEnabled()) {
         log.debug("Is Mean Close to Theoritical Mean        : " + theorticalMeanValueAllowedPerVariationsAllowed);
         log.debug("Is Stddev Close to Theoritical Stddev    : " + theorticalStddevValueAllowedPerVariationsAllowed);
         log.debug("Is 95% Covered by Theoritical Boundaries : " + recCountCovers95PercentForTheoriticalBoundaries);
      }

      return theorticalMeanValueAllowedPerVariationsAllowed && theorticalStddevValueAllowedPerVariationsAllowed
               && recCountCovers95PercentForTheoriticalBoundaries;
   }

   private boolean isTheorticalValueAllowedPerVariationsAllowed (Double realValue, Double theorticalValue) {
      boolean isTheorticalValueAllowedPerVariationsAllowed = false;
      Double theorticalValueVariationAllowed = theorticalValue
               * getPlatformServicesConfigurationService()
                        .getSamplingStrategyTheorticalMeanAndStddevVariationAllowedPercentage() / 100;
      Double theorticalValueFirstBoundary = theorticalValue - theorticalValueVariationAllowed;
      Double theorticalValueSecondBoundary = theorticalValue + theorticalValueVariationAllowed;
      if (realValue >= theorticalValueFirstBoundary && realValue <= theorticalValueSecondBoundary
               || realValue <= theorticalValueFirstBoundary && realValue >= theorticalValueSecondBoundary) {
         isTheorticalValueAllowedPerVariationsAllowed = true;
      }
      return isTheorticalValueAllowedPerVariationsAllowed;
   }

   private boolean isRecordCount95PercentOfTotalRecordCount (Mapping primaryMapping, DataSource dataSource,
            Asset sourceAsset, Double minValue, Double maxValue, Long totalRecordCount) throws Exception {
      boolean recordCount95PercentOfTotalRecordCount = false;
      Long theoriticalRecCount = null;
      Tabl table = primaryMapping.getAssetEntityDefinition().getTabl();
      Colum column = primaryMapping.getAssetEntityDefinition().getColum();
      QueryTable queryTable = ExecueBeanManagementUtil.prepareQueryTable(table);
      QueryColumn queryColumn = ExecueBeanManagementUtil.prepareQueryColumn(column);
      QueryTableColumn queryTableColumn = ExecueBeanManagementUtil.prepareQueryTableColumn(queryColumn, queryTable);
      Query theoriticalBoundaryBasedCountQuery = prepareTheoriticalBoundaryBasedCountQuery(queryTableColumn, minValue,
               maxValue);
      QueryRepresentation queryRepresentation = getQueryGenerationService(dataSource.getProviderType())
               .extractQueryRepresentation(sourceAsset, theoriticalBoundaryBasedCountQuery);
      ExeCueResultSet exeCueResultSet = getClientSourceDAO().executeQuery(dataSource,
               theoriticalBoundaryBasedCountQuery, queryRepresentation.getQueryString());
      if (exeCueResultSet.next()) {
         theoriticalRecCount = exeCueResultSet.getLong(0);
         if (log.isDebugEnabled()) {
            log.debug("Theoritical Record Count : " + theoriticalRecCount);
            log.debug("Total Record Count : " + totalRecordCount);
         }
         if (theoriticalRecCount * 1.0 / totalRecordCount * 1.0 * 100 > getPlatformServicesConfigurationService()
                  .getNeededPopulationCoveragePercentageWhileCheckingForBellCurve()) {
            recordCount95PercentOfTotalRecordCount = true;
         }
      }
      return recordCount95PercentOfTotalRecordCount;
   }

   private Query prepareTheoriticalBoundaryBasedCountQuery (QueryTableColumn queryTableColumn, Double minValue,
            Double maxValue) {
      List<SelectEntity> selectEntities = new ArrayList<SelectEntity>();
      selectEntities.add(ExecueBeanManagementUtil.prepareSelectEntity(queryTableColumn, StatType.COUNT.getValue()
               + queryTableColumn.getColumn().getColumnName(), StatType.COUNT));
      List<FromEntity> fromEntities = ExecueBeanManagementUtil.createFromEntities(queryTableColumn.getTable());
      List<ConditionEntity> conditionEntities = new ArrayList<ConditionEntity>();
      List<String> rhsValues = new ArrayList<String>();
      rhsValues.add(minValue.toString());
      rhsValues.add(maxValue.toString());
      ConditionEntity conditionEntity = ExecueBeanManagementUtil.prepareConditionEntity(queryTableColumn.getTable(),
               queryTableColumn.getColumn(), rhsValues, OperatorType.BETWEEN);
      conditionEntities.add(conditionEntity);
      Query query = new Query();
      query.setSelectEntities(selectEntities);
      query.setFromEntities(fromEntities);
      query.setWhereEntities(conditionEntities);
      return query;
   }

   private StatInfo populateStatInfo (ExeCueResultSet exeCueResultSet) throws Exception {
      int index = 0;
      Double minValue = exeCueResultSet.getDouble(index++);
      if (minValue == null) {
         minValue = 0D;
      }
      Double maxValue = exeCueResultSet.getDouble(index++);
      if (maxValue == null) {
         maxValue = 0D;
      }
      Double meanValue = exeCueResultSet.getDouble(index++);
      if (meanValue == null) {
         meanValue = 0D;
      }
      Double stddevValue = exeCueResultSet.getDouble(index++);
      if (stddevValue == null) {
         stddevValue = 0D;
      }
      Long countValue = exeCueResultSet.getLong(index++);
      if (countValue == null) {
         countValue = 0L;
      }
      return new StatInfo(stddevValue, meanValue, minValue, maxValue, countValue);
   }

   private Query prepareSubGroupingBasedSamplingStrategyQuery (QueryTableColumn queryTableColumn) {
      // SELECT MIN(currency_code),MAX(currency_code),AVG(currency_code),STDDEV(currency_code) FROM shipping_order;
      List<SelectEntity> selectEntities = new ArrayList<SelectEntity>();
      selectEntities.add(ExecueBeanManagementUtil.prepareSelectEntity(queryTableColumn, StatType.MINIMUM.getValue()
               + queryTableColumn.getColumn().getColumnName(), StatType.MINIMUM));
      selectEntities.add(ExecueBeanManagementUtil.prepareSelectEntity(queryTableColumn, StatType.MAXIMUM.getValue()
               + queryTableColumn.getColumn().getColumnName(), StatType.MAXIMUM));
      selectEntities.add(ExecueBeanManagementUtil.prepareSelectEntity(queryTableColumn, StatType.AVERAGE.getValue()
               + queryTableColumn.getColumn().getColumnName(), StatType.AVERAGE));
      selectEntities.add(ExecueBeanManagementUtil.prepareSelectEntity(queryTableColumn, StatType.STDDEV.getValue()
               + queryTableColumn.getColumn().getColumnName(), StatType.STDDEV));
      selectEntities.add(ExecueBeanManagementUtil.prepareSelectEntity(queryTableColumn, StatType.COUNT.getValue()
               + queryTableColumn.getColumn().getColumnName(), StatType.COUNT));
      List<FromEntity> fromEntities = ExecueBeanManagementUtil.createFromEntities(queryTableColumn.getTable());
      Query query = new Query();
      query.setSelectEntities(selectEntities);
      query.setFromEntities(fromEntities);
      return query;
   }

   private IQueryGenerationService getQueryGenerationService (AssetProviderType providerType) {
      return QueryGenerationServiceFactory.getInstance().getQueryGenerationService(providerType);
   }

   public IMappingRetrievalService getMappingRetrievalService () {
      return mappingRetrievalService;
   }

   public void setMappingRetrievalService (IMappingRetrievalService mappingRetrievalService) {
      this.mappingRetrievalService = mappingRetrievalService;
   }

   public ISDXRetrievalService getSdxRetrievalService () {
      return sdxRetrievalService;
   }

   public void setSdxRetrievalService (ISDXRetrievalService sdxRetrievalService) {
      this.sdxRetrievalService = sdxRetrievalService;
   }

   public IClientSourceDAO getClientSourceDAO () {
      return clientSourceDAO;
   }

   public void setClientSourceDAO (IClientSourceDAO clientSourceDAO) {
      this.clientSourceDAO = clientSourceDAO;
   }

   public IPlatformServicesConfigurationService getPlatformServicesConfigurationService () {
      return platformServicesConfigurationService;
   }

   public void setPlatformServicesConfigurationService (
            IPlatformServicesConfigurationService platformServicesConfigurationService) {
      this.platformServicesConfigurationService = platformServicesConfigurationService;
   }

   public class StatInfo {

      private Double stddevValue;
      private Double meanValue;
      private Double minValue;
      private Double maxValue;
      private Long   countValue;

      public StatInfo (Double stddevValue, Double meanValue, Double minValue, Double maxValue, Long countValue) {
         super();
         this.stddevValue = stddevValue;
         this.meanValue = meanValue;
         this.minValue = minValue;
         this.maxValue = maxValue;
         this.countValue = countValue;
      }

      public Double getStddevValue () {
         return stddevValue;
      }

      public void setStddevValue (Double stddevValue) {
         this.stddevValue = stddevValue;
      }

      public Double getMeanValue () {
         return meanValue;
      }

      public void setMeanValue (Double meanValue) {
         this.meanValue = meanValue;
      }

      public Double getMinValue () {
         return minValue;
      }

      public void setMinValue (Double minValue) {
         this.minValue = minValue;
      }

      public Double getMaxValue () {
         return maxValue;
      }

      public void setMaxValue (Double maxValue) {
         this.maxValue = maxValue;
      }

      public Long getCountValue () {
         return countValue;
      }

      public void setCountValue (Long countValue) {
         this.countValue = countValue;
      }

   }

}
