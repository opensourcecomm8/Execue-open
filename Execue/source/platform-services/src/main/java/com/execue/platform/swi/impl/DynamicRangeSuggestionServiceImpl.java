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
import com.execue.core.common.bean.entity.DataSource;
import com.execue.core.common.bean.entity.Range;
import com.execue.core.common.bean.entity.RangeDetail;
import com.execue.core.common.bean.querygen.OrderEntity;
import com.execue.core.common.bean.querygen.Query;
import com.execue.core.common.bean.querygen.QueryColumn;
import com.execue.core.common.bean.querygen.QueryGenerationInput;
import com.execue.core.common.bean.querygen.QueryGenerationOutput;
import com.execue.core.common.bean.querygen.QueryTableColumn;
import com.execue.core.common.bean.querygen.SelectEntity;
import com.execue.core.common.type.AssetProviderType;
import com.execue.core.common.type.OrderEntityType;
import com.execue.core.common.util.ExecueBeanCloneUtil;
import com.execue.core.configuration.ICoreConfigurationService;
import com.execue.core.constants.ISQLQueryConstants;
import com.execue.dataaccess.clientsource.IClientSourceDAO;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.platform.exception.PlatformServicesExceptionCodes;
import com.execue.platform.exception.RangeSuggestionException;
import com.execue.platform.swi.IRangeSuggestionService;
import com.execue.querybuilder.service.client.ClientSourceQueryBuilderServiceFactory;
import com.execue.querybuilder.service.client.IClientSourceQueryBuilderService;
import com.execue.querygen.service.IQueryGenerationService;
import com.execue.querygen.service.QueryGenerationServiceFactory;

/**
 * This class dynamically calculates the ranges of a column when the user-defined ranges are not available
 * 
 * @author John Mallavalli
 */
public class DynamicRangeSuggestionServiceImpl implements IRangeSuggestionService, ISQLQueryConstants {

   private static final Logger                    logger = Logger.getLogger(DynamicRangeSuggestionServiceImpl.class);

   private ICoreConfigurationService              coreConfigurationService;
   private IClientSourceDAO                       clientSourceDAO;
   private QueryGenerationServiceFactory          queryGenerationServiceFactory;
   private ClientSourceQueryBuilderServiceFactory clientSourceQueryBuilderServiceFactory;

   /**
    * This method generates the ranges for a column dynamically based on an algorithm which deduces the ranges based on
    * the spread of the entire population
    * 
    * @param reportMetaInfo
    *           the ReportMetaInfo object
    * @return the Range object which gets deduced dynamically
    */
   public Range deduceRange (DynamicRangeInput dynamicRangeInput) throws RangeSuggestionException {
      Range range = null;
      SelectEntity rangeSelectEntity = dynamicRangeInput.getRangeSelectEntity();
      Long conceptBedId = dynamicRangeInput.getConceptBedId();
      long sqlCountOfRangeColumn = dynamicRangeInput.getSqlCountOfRangeColumn();
      int bandCount = dynamicRangeInput.getBandCount();
      // get the number of bands from config
      if (bandCount >= sqlCountOfRangeColumn) {
         // returning null as the ranges are too small to be shown on grids.
         // The data doesn't allows the Dimension to be qualified as a range hence returning back to Measure and
         // continue further.
         return null;
      }

      Query innerQuery = getInnerQuery(rangeSelectEntity, dynamicRangeInput.getQuery());

      List<String> rawRanges = prepareRawRanges(bandCount, sqlCountOfRangeColumn, innerQuery, dynamicRangeInput
               .getTargetAsset());
      List<String> negatives = new ArrayList<String>();
      // TODO: -JVK- Revisit later - this is a HACK to properly deduce the ranges for negative values.
      // This works for most of the cases. This is not a foolproof approach. This is NOT the appropriate fix.
      // mark all the ranges which have negative values
      for (int i = 1; i < rawRanges.size(); i++) {
         if (rawRanges.get(i).startsWith("-")) {
            negatives.add(String.valueOf(i));
         }
      }
      if (logger.isDebugEnabled()) {
         logger.debug("Various limits in the ranges before smoothening : ");
         for (int i = 0; i < rawRanges.size(); i++) {
            logger.debug(rawRanges.get(i));
         }
         logger.debug("Ranges before smoothening : ");
         for (int i = 0; i < bandCount; i++) {
            logger.debug(rawRanges.get(i * 2) + " to " + rawRanges.get(i * 2 + 1));
         }
      }
      List<String> ranges = getDynamicSmoothRanges(rawRanges);
      for (int i = 1; i < ranges.size(); i++) {
         if (negatives.contains(String.valueOf(i))) {
            if (!ranges.get(i).startsWith("-")) {
               ranges.set(i, "-" + ranges.get(i));
            }
         }
      }
      if (logger.isDebugEnabled()) {
         logger.debug("Various limits in the ranges after smoothening : ");
         for (int i = 0; i < ranges.size(); i++) {
            logger.debug(ranges.get(i));
         }
         logger.debug("Ranges after smoothening : ");
      }
      Set<RangeDetail> rangeDetails = new HashSet<RangeDetail>();
      for (int i = 0; i < bandCount; i++) {
         String lowerLimit = ranges.get(i * 2);
         String upperLimit = ranges.get(i * 2 + 1);
         RangeDetail rangeDetail = new RangeDetail();
         rangeDetail.setOrder(i + 1);
         // To set the first lower limit as null
         if (i != 0) {
            rangeDetail.setLowerLimit(Double.parseDouble(lowerLimit));
         }
         // To set the last upper limit as null
         if (i != bandCount - 1) {
            rangeDetail.setUpperLimit(Double.parseDouble(upperLimit));
         }
         lowerLimit = i == 0 ? "Low" : lowerLimit;
         upperLimit = i == bandCount - 1 ? "High" : upperLimit;
         String value = lowerLimit + " to " + upperLimit;
         rangeDetail.setValue(value);
         // TODO: -JVK- revisit
         rangeDetail.setDescription(value);
         if (logger.isDebugEnabled()) {
            logger.debug("Value : " + rangeDetail.getValue() + "; Description : " + rangeDetail.getDescription());
         }
         rangeDetails.add(rangeDetail);
      }
      if (rangeDetails.size() > 1) {
         range = new Range();
         range.setId(getCoreConfigurationService().getDynamicRangeId());
         range.setConceptBedId(conceptBedId);
         range.setRangeDetails(rangeDetails);
      }
      return range;
   }

   /**
    * This method rounds off the range limits to the nearest logical end of unit
    * 
    * @param rawRanges
    * @return
    */
   private List<String> getDynamicSmoothRanges (List<String> rawRanges) {
      List<String> ranges = new ArrayList<String>();
      List<String> highRanges = new ArrayList<String>();
      for (int i = 0; i < rawRanges.size();) {
         if (highRanges.isEmpty()) {
            highRanges.add(rawRanges.get(i++));
         }
         highRanges.add(rawRanges.get(i));
         i += 2;
      }
      long previousHigRng = 0;
      double previousHigRngDouble = 0;
      int i = 0;
      while (i < highRanges.size() - 2) {
         String previousUL = highRanges.get(i);
         String currentUL = highRanges.get(i + 1);
         String nextUL = highRanges.get(i + 2);
         double currentDoubleValue = new Double(currentUL).doubleValue();
         double currentRngDiff = Math.abs(currentDoubleValue - new Double(previousUL).doubleValue());
         double nextRngDiff = Math.abs(currentDoubleValue - new Double(nextUL).doubleValue());
         if (logger.isDebugEnabled()) {
            logger.debug(" currentRngDiff=" + currentRngDiff + " nextRngDiff " + nextRngDiff);
         }
         long smoothedRngValue = 0;
         double smoothedRngValueDouble = 0;
         long cRangePower = Math.round(Math.log((new Double(currentRngDiff).doubleValue() * .05)) / 2.303);
         long nRangePower = Math.round(Math.log((new Double(nextRngDiff).doubleValue() * .05)) / 2.303);
         if (logger.isDebugEnabled()) {
            logger.debug(" cRangePower=" + cRangePower + " nRangePower " + nRangePower);
         }
         double smoothCurrentRangeDouble = 0;
         double smoothNextRangeDouble = 0;
         smoothCurrentRangeDouble = Math.ceil(Math.abs(new Double(currentUL).doubleValue()) / Math.pow(10, cRangePower)
                  / 5)
                  * 5 * Math.pow(10, cRangePower);
         smoothNextRangeDouble = Math.ceil(Math.abs(new Double(currentUL).doubleValue()) / Math.pow(10, nRangePower)
                  / 5)
                  * 5 * Math.pow(10, nRangePower);
         if (currentDoubleValue < 0) {
            smoothCurrentRangeDouble *= -1.0;
            smoothNextRangeDouble *= -1.0;
         }
         if (logger.isDebugEnabled()) {
            logger.debug("Double smoothCurrentRangeDouble=" + smoothCurrentRangeDouble + " smoothNextRangeDouble="
                     + smoothNextRangeDouble);
         }
         long smoothCurrentRange = (long) (Math.ceil(Math.abs(new Double(currentUL).doubleValue())
                  / Math.pow(10, cRangePower) / 5) * 5 * Math.pow(10, cRangePower));
         long smoothNextRange = (long) (Math.ceil(Math.abs(new Double(currentUL).doubleValue())
                  / Math.pow(10, nRangePower) / 5) * 5 * Math.pow(10, nRangePower));
         if (logger.isDebugEnabled()) {
            logger.debug(" smoothCurrentRange=" + smoothCurrentRange + " smoothNextRange=" + smoothNextRange);
         }
         smoothedRngValue = smoothNextRange;
         smoothedRngValueDouble = smoothNextRangeDouble;
         if (Math.abs(smoothCurrentRange - new Double(currentUL).doubleValue()) < Math.abs(smoothNextRange
                  - new Double(currentUL).doubleValue())) {
            smoothedRngValue = smoothCurrentRange;
         }
         if (Math.abs(smoothCurrentRangeDouble - new Double(currentUL).doubleValue()) < Math.abs(smoothNextRangeDouble
                  - new Double(currentUL).doubleValue())) {
            smoothedRngValueDouble = smoothCurrentRangeDouble;
         }
         if (cRangePower >= 0) {
            if (i == 0) {
               ranges.add(Long.toString(Long.MIN_VALUE));
            } else {
               ranges.add(Long.toString(previousHigRng));
            }
            ranges.add(Long.toString(smoothedRngValue));
         } else {
            if (i == 0) {
               ranges.add(Double.toString(Double.MAX_VALUE));
            } else {
               ranges.add(Double.toString(previousHigRngDouble));
            }
            ranges.add(Double.toString(smoothedRngValueDouble));
         }
         previousHigRng = smoothedRngValue;
         previousHigRngDouble = smoothedRngValueDouble;
         if (i + 1 == highRanges.size() - 2) {
            if (cRangePower >= 0) {
               ranges.add(Long.toString(previousHigRng));
               ranges.add(Long.toString(Long.MAX_VALUE));
            } else {
               ranges.add(Double.toString(previousHigRngDouble));
               ranges.add(Double.toString(Double.MAX_VALUE));
            }
         }
         i++;
      }
      return ranges;
   }

   private List<String> prepareRawRanges (int bandCount, long sqlCount, Query innerQuery, Asset targetAsset)
            throws RangeSuggestionException {
      List<String> rawRanges = new ArrayList<String>();
      List<Query> inputQueries = new ArrayList<Query>();
      QueryGenerationInput queryGenerationInput = new QueryGenerationInput();
      inputQueries.add(innerQuery);
      queryGenerationInput.setInputQueries(inputQueries);
      queryGenerationInput.setTargetAsset(targetAsset);
      QueryGenerationOutput queryGenerationOutput = getQueryGenerationService(targetAsset).generateQuery(
               queryGenerationInput);
      String innerQueryString = getQueryGenerationService(targetAsset).extractQueryRepresentation(targetAsset,
               queryGenerationOutput.getResultQuery()).getQueryString();
      if (logger.isDebugEnabled()) {
         logger.debug("Inner Query String : " + innerQueryString);
      }
      String rangeColumnAlias = innerQuery.getSelectEntities().get(0).getAlias();
      DataSource dataSource = targetAsset.getDataSource();
      String minMaxWithUnionQueryString = getMinMaxWithUnionQuery(bandCount, sqlCount, rangeColumnAlias,
               innerQueryString, dataSource.getProviderType());
      if (logger.isDebugEnabled()) {
         logger.debug("MinMaxWithUnion Query String : \n" + minMaxWithUnionQueryString);
      }
      try {
         ExeCueResultSet results = getClientSourceDAO()
                  .executeQuery(dataSource, innerQuery, minMaxWithUnionQueryString);
         try {
            while (results.next()) {
               rawRanges.add(results.getString(0));
               rawRanges.add(results.getString(1));
            }
         } catch (Exception exception) {
            exception.printStackTrace();
            logger.error("Exception in DynamicRangesHelper", exception);
            logger.error("Cause : " + exception.getCause());
            throw new RangeSuggestionException(PlatformServicesExceptionCodes.DYNAMIC_RANGES_EXCEPTION_CODE, exception
                     .getCause());
         }
      } catch (DataAccessException dataAccessException) {
         dataAccessException.printStackTrace();
         logger.error("Exception in DynamicRangesHelper", dataAccessException);
         logger.error("Actual Error : " + dataAccessException.getMessage());
         logger.error("Cause : " + dataAccessException.getCause());
         throw new RangeSuggestionException(dataAccessException.getCode(),
                  "Error in prepareRawRanges while executing the min-max-with-union query", dataAccessException
                           .getCause());
      }
      return rawRanges;
   }

   private String getMinMaxWithUnionQuery (int bandCount, long sqlCount, String rangeColumnAlias,
            String innerQueryString, AssetProviderType assetProviderType) {
      StringBuffer minMaxWithUnionQuery = new StringBuffer();
      int[][] rangeBands = getRangeBands(bandCount, sqlCount);
      IClientSourceQueryBuilderService clientSourceQueryBuilderService = getClientSourceQueryBuilderServiceFactory()
               .getQueryBuilderService(assetProviderType);
      if (clientSourceQueryBuilderService != null) {
         for (int i = 0; i < bandCount; i++) {
            if (i > 0) {
               minMaxWithUnionQuery.append(NEW_LINE).append(SQL_UNION_KEYWORD).append(NEW_LINE);
            }
            int[] band = rangeBands[i];
            minMaxWithUnionQuery.append(clientSourceQueryBuilderService.getDynamicRangesQuery(rangeColumnAlias,
                     innerQueryString, band));
         }
         return minMaxWithUnionQuery.toString();
      }
      throw new UnsupportedOperationException("The DB type [" + assetProviderType
               + "] of the selected Asset is not currently supported.");

   }

   // TODO: -JM- delete later
   // public static void main (String[] args) {
   // Asset asset = new Asset();
   // DataSource ds = new DataSource();
   // ds.setProviderType(AssetProviderType.Teradata);
   // asset.setDataSource(ds);
   // int bandCount = 5;
   // long sqlCount = 37900;
   // String rangeColumnAlias = "NUK68";
   // String innerQueryString = "SELECT DISTINCT IST.NET_SALES_OR_REVENUES AS NUK68 FROM income_sheet_yr IST WHERE ";
   // innerQueryString += "IST.nominal_year BETWEEN 2000 AND 2011 ORDER BY IST.NET_SALES_OR_REVENUES ASC";
   // String queryString = new DynamicRangesHelper().getMinMaxWithUnionQuery(bandCount, sqlCount, rangeColumnAlias,
   // innerQueryString, asset);
   // System.out.println(queryString);
   // }

   private int[][] getRangeBands (int bandCount, long sqlCount) {
      int[][] bands = null;
      int endPoint = 0;
      int startPoint = 1;
      if (sqlCount > bandCount) {
         bands = new int[bandCount][2];
         double bandPercentage = (double) 100 / (double) bandCount;
         for (int i = 0; i < bandCount; i++) {
            double newEndPoint = bandPercentage * sqlCount * (i + 1) / 100;
            endPoint = (int) Math.ceil(newEndPoint);
            bands[i][0] = startPoint;
            bands[i][1] = endPoint;
            startPoint = endPoint;
         }
      }
      return bands;
   }

   private Query getInnerQuery (SelectEntity rangeSelectEntity, Query query) {
      // Set the distinct keyword for the range column
      rangeSelectEntity.getTableColumn().getColumn().setDistinct(true);

      QueryTableColumn queryTableColumn = rangeSelectEntity.getTableColumn();
      QueryTableColumn newQueryTableColumn = new QueryTableColumn();
      newQueryTableColumn.setTable(queryTableColumn.getTable());
      QueryColumn clonedQueryColumn = ExecueBeanCloneUtil.cloneQueryColumn(queryTableColumn.getColumn());
      clonedQueryColumn.setDistinct(false);
      newQueryTableColumn.setColumn(clonedQueryColumn);

      // Add the range column into the order by section
      SelectEntity newSelectEntity = new SelectEntity();
      newSelectEntity.setAlias(rangeSelectEntity.getAlias());
      newSelectEntity.setFormula(rangeSelectEntity.getFormula());
      newSelectEntity.setFunctionName(rangeSelectEntity.getFunctionName());
      newSelectEntity.setQueryValue(rangeSelectEntity.getQueryValue());
      newSelectEntity.setSubQuery(rangeSelectEntity.getSubQuery());
      newSelectEntity.setTableColumn(newQueryTableColumn);
      newSelectEntity.setType(rangeSelectEntity.getType());

      OrderEntity newOrderEntity = new OrderEntity();
      newOrderEntity.setOrderType(OrderEntityType.ASCENDING);
      newOrderEntity.setSelectEntity(newSelectEntity);

      List<OrderEntity> orderingEntities = query.getOrderingEntities();
      if (orderingEntities == null) {
         orderingEntities = new ArrayList<OrderEntity>();
         query.setOrderingEntities(orderingEntities);
      }
      orderingEntities.add(newOrderEntity);

      List<SelectEntity> newList = new ArrayList<SelectEntity>();
      newList.add(rangeSelectEntity);

      Query rangeQuery = new Query();
      rangeQuery.setAlias(query.getAlias());
      rangeQuery.setFromEntities(query.getFromEntities());
      rangeQuery.setWhereEntities(query.getWhereEntities());
      rangeQuery.setOrderingEntities(query.getOrderingEntities());
      rangeQuery.setSelectEntities(newList);
      rangeQuery.setJoinEntities(query.getJoinEntities());
      rangeQuery.setLimitingCondition(query.getLimitingCondition());
      return rangeQuery;
   }

   private IQueryGenerationService getQueryGenerationService (Asset asset) {
      return getQueryGenerationServiceFactory().getQueryGenerationService(asset);
   }

   public ICoreConfigurationService getCoreConfigurationService () {
      return coreConfigurationService;
   }

   public void setCoreConfigurationService (ICoreConfigurationService coreConfigurationService) {
      this.coreConfigurationService = coreConfigurationService;
   }

   public IClientSourceDAO getClientSourceDAO () {
      return clientSourceDAO;
   }

   public void setClientSourceDAO (IClientSourceDAO clientSourceDAO) {
      this.clientSourceDAO = clientSourceDAO;
   }

   public QueryGenerationServiceFactory getQueryGenerationServiceFactory () {
      return queryGenerationServiceFactory;
   }

   public void setQueryGenerationServiceFactory (QueryGenerationServiceFactory queryGenerationServiceFactory) {
      this.queryGenerationServiceFactory = queryGenerationServiceFactory;
   }

   public ClientSourceQueryBuilderServiceFactory getClientSourceQueryBuilderServiceFactory () {
      return clientSourceQueryBuilderServiceFactory;
   }

   public void setClientSourceQueryBuilderServiceFactory (
            ClientSourceQueryBuilderServiceFactory clientSourceQueryBuilderServiceFactory) {
      this.clientSourceQueryBuilderServiceFactory = clientSourceQueryBuilderServiceFactory;
   }
}