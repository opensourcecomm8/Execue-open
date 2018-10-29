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


package com.execue.repoting.aggregation.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.AggregateQuery;
import com.execue.core.common.bean.BusinessStat;
import com.execue.core.common.bean.ExeCueResultSetMetaData;
import com.execue.core.common.bean.ReportQueryData;
import com.execue.core.common.bean.SQLExeCueCachedResultSet;
import com.execue.core.common.bean.aggregation.QueryData;
import com.execue.core.common.bean.aggregation.QueryDataColumnData;
import com.execue.core.common.bean.aggregation.QueryDataColumnMember;
import com.execue.core.common.bean.aggregation.QueryDataHeader;
import com.execue.core.common.bean.aggregation.QueryDataHeaderColumnMeta;
import com.execue.core.common.bean.aggregation.QueryDataHeaderHierarchyColumnMeta;
import com.execue.core.common.bean.aggregation.QueryDataHeaderHierarchyMeta;
import com.execue.core.common.bean.aggregation.QueryDataRowData;
import com.execue.core.common.bean.aggregation.QueryDataRows;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.Membr;
import com.execue.core.common.bean.entity.RangeDetail;
import com.execue.core.common.bean.entity.comparator.RangeDetailOrderComparator;
import com.execue.core.common.bean.governor.BusinessAssetTerm;
import com.execue.core.common.bean.governor.StructuredQuery;
import com.execue.core.common.bean.querygen.Query;
import com.execue.core.common.bean.querygen.QueryGenerationOutput;
import com.execue.core.common.bean.querygen.QueryRepresentation;
import com.execue.core.common.bean.querygen.SelectEntity;
import com.execue.core.common.type.AggregateQueryType;
import com.execue.core.common.type.BehaviorType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.ColumnType;
import com.execue.core.common.type.ConversionType;
import com.execue.core.common.type.StatType;
import com.execue.core.common.util.ExecueBeanUtil;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.dataaccess.clientsource.IClientSourceDAO;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.querygen.service.IQueryGenerationService;
import com.execue.querygen.service.IStructuredQueryTransformerService;
import com.execue.querygen.service.QueryGenerationServiceFactory;
import com.execue.repoting.aggregation.bean.ReportColumnInfo;
import com.execue.repoting.aggregation.bean.ReportHierarchyColumnInfo;
import com.execue.repoting.aggregation.bean.ReportMetaHierarchyInfo;
import com.execue.repoting.aggregation.bean.ReportMetaInfo;
import com.execue.repoting.aggregation.comparator.LookupValueComparator;
import com.execue.repoting.aggregation.configuration.IAggregationConfigurationService;
import com.execue.repoting.aggregation.exception.AggregationExceptionCodes;
import com.execue.repoting.aggregation.exception.ReportException;
import com.execue.repoting.aggregation.helper.ReportAggregationHelper;
import com.execue.repoting.aggregation.service.IReportDataExtractionService;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.SDXException;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.swi.service.ISDXRetrievalService;

/**
 * @author John Mallavalli
 */

public class ReportDataExtractionServiceImpl implements IReportDataExtractionService {

   private static Logger                      logger           = Logger
                                                                        .getLogger(ReportDataExtractionServiceImpl.class);
   private IClientSourceDAO                   clientSourceDAO;
   private IKDXRetrievalService               kdxRetrievalService;
   private IStructuredQueryTransformerService structuredQueryTransformerService;
   private QueryGenerationServiceFactory      queryGenerationServiceFactory;
   private ISDXRetrievalService               sdxRetrievalService;
   private IAggregationConfigurationService   aggregationConfigurationService;

   private static String                      NULL_STRING      = "null";
   private static String                      NULL             = "N/A";
   private static String                      COHORT_INDICATOR = "(*)";
   private static String                      SPACE            = " ";

   // private static String DTYPE_AS_STRING = "string";

   @SuppressWarnings ("unused")
   public ReportQueryData getQueryData (AggregateQuery aggregatedQuery, ReportMetaInfo reportMetaInfo)
            throws ReportException {
      Asset asset = aggregatedQuery.getAssetQuery().getLogicalQuery().getAsset();
      Map<String, BusinessAssetTerm> aliasBusinessAssetTerm = new HashMap<String, BusinessAssetTerm>();

      StructuredQuery structuredQuery = reportMetaInfo.getAssetQuery().getLogicalQuery();
      QueryGenerationOutput queryGenerationOutput = getStructuredQueryTransformerService()
               .populateQueryGenerationOutput(structuredQuery, aliasBusinessAssetTerm);
      Query query = queryGenerationOutput.getResultQuery();

      // add the distinct to the count stats
      addDistinctForCountStatistic(query);

      QueryRepresentation queryRepresentation = getQueryGenerationService(asset).extractQueryRepresentation(asset,
               query);

      String queryString = queryRepresentation.getQueryString();

      if (logger.isDebugEnabled()) {
         logger.debug("\nFinal Query String : \n" + queryString);
      }

      ReportQueryData reportQueryData = extractReportQueryData(asset, query, queryString, reportMetaInfo,
               aliasBusinessAssetTerm);

      // Update the existing aggregate query
      aggregatedQuery.setQueryRepresentation(queryRepresentation);
      aggregatedQuery.getAssetQuery().setPhysicalQuery(query);
      aggregatedQuery.setDataExtracted(CheckType.YES);
      return reportQueryData;
   }

   public ReportQueryData generateReportQueryData (ReportMetaInfo reportMetaInfo,
            Map<String, BusinessAssetTerm> aliasBusinessAssetTerm) throws ReportException {

      StructuredQuery structuredQuery = reportMetaInfo.getAssetQuery().getLogicalQuery();

      QueryGenerationOutput queryGenerationOutput = getStructuredQueryTransformerService()
               .populateQueryGenerationOutput(structuredQuery, aliasBusinessAssetTerm);

      Query query = queryGenerationOutput.getResultQuery();
      Asset asset = reportMetaInfo.getAssetQuery().getLogicalQuery().getAsset();
      QueryRepresentation queryRepresentation = getQueryGenerationService(asset).extractQueryRepresentation(asset,
               query);
      String queryString = queryRepresentation.getQueryString();

      // TODO: -JVK- this is a HACK; revisit later
      if (reportMetaInfo.isGenerateDetailReport()
               && reportMetaInfo.getAssetQuery().getLogicalQuery().getCohort() != null
               && ExecueCoreUtil.isNotEmpty(reportMetaInfo.getFinalQuery())) {
         queryString = correctQueryStringForCohort(reportMetaInfo.getFinalQuery(), queryString);
      }

      System.out.println("\nFinal Query String for Presentation Time Report : \n" + queryString + "\n");

      ReportQueryData reportQueryData = extractReportQueryData(asset, query, queryString, reportMetaInfo,
               aliasBusinessAssetTerm);
      return reportQueryData;
   }

   private static String correctQueryStringForCohort (String finalQuery, String queryString) {

      // TODO:: NK:: Verify with GA/KA if this is the correct fix
      int lastIndexOfFrom = finalQuery.lastIndexOf("FROM ");
      int firstIndexOfWhere = finalQuery.indexOf("WHERE ");
      String fromSection = finalQuery.substring(lastIndexOfFrom, firstIndexOfWhere);
      int endIndex = queryString.lastIndexOf("FROM ");

      // String fromSection = StringUtils.substringBetween(finalQuery, "FROM ", " WHERE");
      // int endIndex = queryString.indexOf("FROM ") + 5;
      String part1 = queryString.substring(0, endIndex);
      endIndex = queryString.indexOf(" WHERE");
      String part3 = queryString.substring(endIndex);

      String correctedQueryString = part1 + fromSection + part3;
      return correctedQueryString;
   }

   /**
    * This method will set the DISTINCT keyword for the count statistic of the select entity term
    * 
    * @param resultQuery
    *           the query which needs to be modified
    */
   private void addDistinctForCountStatistic (Query resultQuery) {
      if (ExecueCoreUtil.isCollectionEmpty(resultQuery.getSelectEntities())) {
         return;
      }
      for (SelectEntity selectEntity : resultQuery.getSelectEntities()) {
         if (StatType.COUNT.equals(selectEntity.getFunctionName())) {
            if (selectEntity.getTableColumn() != null) {
               selectEntity.getTableColumn().getColumn().setDistinct(true);
            }
         }
      }
   }

   private ReportQueryData extractReportQueryData (Asset asset, Query query, String queryString,
            ReportMetaInfo reportMetaInfo, Map<String, BusinessAssetTerm> aliasBusinessAssetTerm)
            throws ReportException {
      try {
         SQLExeCueCachedResultSet sqlCachedResultSet = (SQLExeCueCachedResultSet) getClientSourceDAO().executeQuery(
                  asset.getDataSource(), query, queryString);
         QueryData queryData = generateQueryDataFromResultSet(sqlCachedResultSet, reportMetaInfo,
                  aliasBusinessAssetTerm);
         // TODO: JVK implement cleaner approach
         if (queryData.getQueryDataRows().getQueryDataRowsList().size() == 1
                  && reportMetaInfo.getSummaryPathType() != AggregateQueryType.HIERARCHY_SUMMARY) {
            logger.debug("checking for all null values in the row......");
            boolean nullRow = true;
            QueryDataRowData row = queryData.getQueryDataRows().getQueryDataRowsList().get(0);
            // TODO: -JVK- extra guard condition - revisit to eliminate
            if (row.getQueryDataColumns() != null) {
               for (QueryDataColumnData colData : row.getQueryDataColumns()) {
                  if (!NULL.equals(colData.getColumnValue())) {
                     nullRow = false;
                     break;
                  }
               }
            }
            if (nullRow) {
               boolean b = queryData.getQueryDataRows().getQueryDataRowsList().remove(row);
               logger.debug("Deleting the row with all null values......" + b);
            }
         }
         ReportQueryData reportQueryData = new ReportQueryData();
         reportQueryData.setNumberOfRows(queryData.getQueryDataRows().getQueryDataRowsList().size());
         reportQueryData.setQueryData(queryData);
         reportQueryData.setQueryExecutionTime(sqlCachedResultSet.getQueryExecutionTime());
         return reportQueryData;
      } catch (DataAccessException e) {
         e.printStackTrace();
         throw new ReportException(e.getCode(), e.getMessage(), e.getCause());
      }
   }

   /**
    * This method generates the normalized data structure QueryData which is a wrapper for the data obtained from the
    * data source
    * 
    * @param sqlCachedResultSet
    *           the result set obtained from the data source
    * @param aliasBusinessAssetTerm
    * @return the query data object containing the meta info of the columns and the data
    */
   private QueryData generateQueryDataFromResultSet (SQLExeCueCachedResultSet sqlCachedResultSet,
            ReportMetaInfo reportMetaInfo, Map<String, BusinessAssetTerm> aliasBusinessAssetTerm)
            throws ReportException {
      try {

         // Check for the skip variants
         boolean skipUnivariants = getAggregationConfigurationService().skipUnivariants();
         // NOTE:: For hierarchy reports we have to skip uni-variants handling though in the configuration it is not set
         // to skip,
         // hence override the configuration settings
         if (reportMetaInfo.getSummaryPathType() == AggregateQueryType.HIERARCHY_SUMMARY) {
            skipUnivariants = true;
         }

         ExeCueResultSetMetaData metaData = sqlCachedResultSet.getMetaData();
         List<Integer> univariantIndices = new ArrayList<Integer>();
         if (!skipUnivariants) {
            // Process the columns and collect the indices of the columns which are single variants
            univariantIndices = getUnivariantColumnIndices(reportMetaInfo, metaData, aliasBusinessAssetTerm);
         }

         // Process the columns and prepare the header data for each column
         List<Integer> colWithMemberIndices = new ArrayList<Integer>();
         Map<Integer, List<RangeDetail>> rangesMap = new HashMap<Integer, List<RangeDetail>>();
         Map<Integer, ReportHierarchyColumnInfo> hierachcyColumnInfoByColumnIndex = new HashMap<Integer, ReportHierarchyColumnInfo>();
         List<QueryDataHeaderColumnMeta> queryDataHeaderColumns = prepareQueryDataHeaderColumnMeta(reportMetaInfo,
                  metaData, aliasBusinessAssetTerm, univariantIndices, colWithMemberIndices, rangesMap,
                  hierachcyColumnInfoByColumnIndex);

         // Process the hierarchy columns and prepare the header data for each hierarchy column
         QueryDataHeaderHierarchyMeta queryDataHeaderHierarchyMeta = null;
         if (reportMetaInfo.getSummaryPathType() == AggregateQueryType.HIERARCHY_SUMMARY) {
            queryDataHeaderHierarchyMeta = prepareQueryDataHeaderHierarchyMeta(reportMetaInfo
                     .getReportMetaHierarchyInfo().get(0), metaData, hierachcyColumnInfoByColumnIndex,
                     queryDataHeaderColumns);
         }

         // Process the rows and prepare the row data for each row
         List<QueryDataRowData> rows = prepareQueryDataRows(reportMetaInfo, sqlCachedResultSet, aliasBusinessAssetTerm,
                  univariantIndices, colWithMemberIndices, rangesMap, queryDataHeaderColumns);

         // Prepare the query data header
         QueryDataHeader queryDataHeader = new QueryDataHeader();
         queryDataHeader.setColumnCount(queryDataHeaderColumns.size());
         queryDataHeader.setQueryDataHeaderColumns(queryDataHeaderColumns);
         queryDataHeader.setQueryDataHeaderHierarchyMeta(queryDataHeaderHierarchyMeta);

         // Prepare the query data rows
         QueryDataRows queryDataRows = new QueryDataRows();
         queryDataRows.setQueryDataRowsList(rows);

         // Prepare the query data
         QueryData queryData = new QueryData();
         queryData.setQueryDataHeader(queryDataHeader);
         queryData.setQueryDataRows(queryDataRows);
         return queryData;
      } catch (KDXException kdException) {
         kdException.printStackTrace();
         logger.error("KDXException in ReportDataExtractionServiceImpl", kdException);
         logger.error("Actual Error : [" + kdException.getCode() + "] " + kdException.getMessage());
         logger.error("Cause : " + kdException.getCause());
         throw new ReportException(kdException.getCode(), kdException.getMessage(), kdException.getCause());
      } catch (SDXException sdxException) {
         sdxException.printStackTrace();
         logger.error("SDXException in ReportDataExtractionServiceImpl", sdxException);
         logger.error("Actual Error : [" + sdxException.getCode() + "] " + sdxException.getMessage());
         logger.error("Cause : " + sdxException.getCause());
         throw new ReportException(sdxException.getCode(), sdxException.getMessage(), sdxException.getCause());
      } catch (Exception exception) {
         exception.printStackTrace();
         logger.error("Exception in ReportDataExtractionServiceImpl", exception);
         logger.error("Cause : " + exception.getCause());
         throw new ReportException(AggregationExceptionCodes.AGG_REPORT_QUERY_DATA_EXCEPTION_CODE, exception
                  .getMessage(), exception.getCause());
      }
   }

   private QueryDataHeaderHierarchyMeta prepareQueryDataHeaderHierarchyMeta (
            ReportMetaHierarchyInfo reportMetaHierarchyInfo, ExeCueResultSetMetaData metaData,
            Map<Integer, ReportHierarchyColumnInfo> hierachcyColumnInfoByColumnIndex,
            List<QueryDataHeaderColumnMeta> queryDataHeaderColumns) throws Exception {

      int columnCount = metaData.getColumnCount();

      // process the hierarchy map and populate the header
      List<QueryDataHeaderHierarchyColumnMeta> hierarhcyDetails = new ArrayList<QueryDataHeaderHierarchyColumnMeta>();
      if (hierachcyColumnInfoByColumnIndex.size() > 0) {
         for (int i = 0; i < columnCount; i++) {
            if (hierachcyColumnInfoByColumnIndex.containsKey(i)) {
               ReportHierarchyColumnInfo reportHierarchyColumnInfo = hierachcyColumnInfoByColumnIndex.get(i);
               QueryDataHeaderColumnMeta colMeta = queryDataHeaderColumns.get(i);
               String referenceColumnId = colMeta.getId();
               int level = reportHierarchyColumnInfo.getLevel();

               QueryDataHeaderHierarchyColumnMeta queryDataHeaderHierarchyColumnMeta = new QueryDataHeaderHierarchyColumnMeta();
               queryDataHeaderHierarchyColumnMeta.setReferenceColumnId(referenceColumnId);
               queryDataHeaderHierarchyColumnMeta.setLevel(level);

               hierarhcyDetails.add(queryDataHeaderHierarchyColumnMeta);
            }
         }
      }
      QueryDataHeaderHierarchyMeta queryDataHeaderHierarchyMeta = new QueryDataHeaderHierarchyMeta();
      queryDataHeaderHierarchyMeta.setHierarchyName(reportMetaHierarchyInfo.getHierarchyName());
      queryDataHeaderHierarchyMeta.setHierarchyEntityCount(hierarhcyDetails.size());
      queryDataHeaderHierarchyMeta.setHierarhcyDetails(hierarhcyDetails);

      return queryDataHeaderHierarchyMeta;
   }

   /**
    * @param reportMetaInfo
    * @param sqlCachedResultSet
    * @param aliasBusinessAssetTerm
    * @param univariantIndices
    * @param colWithMemberIndices
    * @param rangesMap
    * @param queryDataHeaderColumns
    * @return List<QueryDataRowData>
    * @throws Exception
    * @throws SDXException
    */
   private List<QueryDataRowData> prepareQueryDataRows (ReportMetaInfo reportMetaInfo,
            SQLExeCueCachedResultSet sqlCachedResultSet, Map<String, BusinessAssetTerm> aliasBusinessAssetTerm,
            List<Integer> univariantIndices, List<Integer> colWithMemberIndices,
            Map<Integer, List<RangeDetail>> rangesMap, List<QueryDataHeaderColumnMeta> queryDataHeaderColumns)
            throws Exception, SDXException {
      Map<Integer, Set<Object>> membersMap = new HashMap<Integer, Set<Object>>();
      List<QueryDataRowData> rows = new ArrayList<QueryDataRowData>();
      int counter = 0;
      ExeCueResultSetMetaData metaData = sqlCachedResultSet.getMetaData();
      int columnCount = metaData.getColumnCount();
      while (sqlCachedResultSet.next()) {
         QueryDataRowData row = new QueryDataRowData();
         List<QueryDataColumnData> values = new ArrayList<QueryDataColumnData>();
         int index = 0;
         for (int i = 0; i < columnCount; i++) {
            if (univariantIndices.contains(i)) {
               if (counter == 0) {
                  BusinessAssetTerm univariantBAT = aliasBusinessAssetTerm.get(metaData.getColumnLabel(i));
                  String univariantValue = sqlCachedResultSet.getString(metaData.getColumnLabel(i));
                  ReportColumnInfo reportColumnInfo = getCorrespondingReportColumnInfo(reportMetaInfo, univariantBAT);
                  List<String> lookupValues = new ArrayList<String>();
                  lookupValues.add(univariantValue);
                  // use the lookup description instead of the lookup value
                  logger.debug("INITIAL univariant value : " + univariantValue);
                  Colum column = (Colum) univariantBAT.getAssetEntityTerm().getAssetEntity();
                  if (column != null) {
                     List<Membr> members = getSdxRetrievalService().getMembersByLookupValues(
                              reportMetaInfo.getAssetQuery().getLogicalQuery().getAsset(), column.getOwnerTable(),
                              column, lookupValues);
                     if (ExecueCoreUtil.isCollectionNotEmpty(members)) {
                        univariantValue = members.get(0).getLookupDescription();
                     }
                  }
                  logger.debug("FINAL univariant value : " + univariantValue);
                  reportColumnInfo.setUnivariantValue(univariantValue);
                  reportMetaInfo.setUnivariants(true);
               } else {
                  continue;
               }
            } else {
               QueryDataColumnData queryDataColumnData = new QueryDataColumnData();
               queryDataColumnData.setColumnName(queryDataHeaderColumns.get(index).getId());
               Object colValue = sqlCachedResultSet.getObject(metaData.getColumnLabel(i));
               // Check the value for null & empty string
               String strColValue = String.valueOf(colValue).trim();
               if (NULL_STRING.equalsIgnoreCase(strColValue) || strColValue.length() == 0) {
                  // replace it with the constant "null"
                  colValue = strColValue = NULL;
               }
               if (colWithMemberIndices.contains(i)) {
                  Set<Object> members = membersMap.get(i);
                  if (members == null) {
                     members = new HashSet<Object>();
                  }
                  members.add(colValue);
                  membersMap.put(i, members);
               }

               queryDataColumnData.setColumnValue(strColValue);
               values.add(queryDataColumnData);
               row.setQueryDataColumns(values);
               index++;
            }
         }
         rows.add(row);
         counter++;
      }
      // process the members map and populate the header
      if (membersMap.size() > 0) {
         int queryDataIndex = 0;
         for (int i = 0; i < columnCount; i++) {
            if (univariantIndices.contains(i)) {
               continue;
            }
            if (membersMap.containsKey(i)) {
               Set<Object> lookupVals = membersMap.get(i);
               QueryDataHeaderColumnMeta colMeta = queryDataHeaderColumns.get(queryDataIndex);
               // query the SWI to get the member information and populate the QueryDataColumnMember object
               BusinessAssetTerm match = aliasBusinessAssetTerm.get(metaData.getColumnLabel(i));
               Colum column = (Colum) match.getAssetEntityTerm().getAssetEntity();
               List<String> lookupValues = new ArrayList<String>();
               if (lookupVals.size() > 0) {
                  // sort the lookup values
                  List<Object> genericObjList = new ArrayList<Object>();
                  for (Object item : lookupVals) {
                     lookupValues.add(String.valueOf(item));
                     genericObjList.add(item);
                  }
                  lookupValues = sortLookupValues(genericObjList);
                  logger.debug("Lookup Values : " + lookupValues);
                  // set the member count
                  colMeta.setMemberCount(lookupVals.size());
                  if (column != null) {
                     List<QueryDataColumnMember> queryDataColumnMembers = new ArrayList<QueryDataColumnMember>();
                     if (ColumnType.ID.getValue().equals(colMeta.getCtype())
                              && ColumnType.DIMENSION.getValue().equals(colMeta.getPlotAs())) {
                        for (String lookupValue : lookupValues) {
                           QueryDataColumnMember queryDataColumnMember = new QueryDataColumnMember();
                           queryDataColumnMember.setValue(lookupValue);
                           queryDataColumnMember.setDescription(lookupValue);
                           queryDataColumnMember.setDisplayString(lookupValue);
                           queryDataColumnMembers.add(queryDataColumnMember);
                        }
                        colMeta.setMembers(queryDataColumnMembers);
                     } else {
                        List<Membr> members = getSdxRetrievalService().getMembersByLookupValues(
                                 reportMetaInfo.getAssetQuery().getLogicalQuery().getAsset(), column.getOwnerTable(),
                                 column, lookupValues);
                        logger.debug("From SWI : fetched " + members.size() + " members");
                        Map<String, Membr> swiMembersMap = null;
                        if (ExecueCoreUtil.isCollectionNotEmpty(members)) {
                           // prepare a map with the lookup value as the key and the Membr object as the value
                           swiMembersMap = new HashMap<String, Membr>(members.size());
                           for (Membr member : members) {
                              String lookupValue = member.getLookupValue();
                              swiMembersMap.put(lookupValue, member);
                           }
                        }
                        for (String lookupValue : lookupValues) {
                           // logger.debug("Finding match for lookup value : " + lookupValue + "....");
                           Membr member = null;
                           QueryDataColumnMember queryDataColumnMember = new QueryDataColumnMember();
                           // Check the value for null & empty string
                           if (ExecueCoreUtil.isEmpty(lookupValue)) {
                              // replace it with the constant "null"
                              lookupValue = NULL;
                              logger.debug("Look up value is NULL : " + lookupValue);
                           } else {
                              member = getMatchingMember(lookupValue, swiMembersMap);
                           }
                           queryDataColumnMember.setValue(lookupValue);
                           if (member == null) {
                              logger.debug("No match found...setting the lookupDescription as lookupValue itself..."
                                       + lookupValue);
                              queryDataColumnMember.setDescription(lookupValue);
                              queryDataColumnMember.setDisplayString(lookupValue);
                           } else {
                              logger.debug("Setting the lookup desc : " + member.getLookupDescription());
                              queryDataColumnMember.setDescription(member.getLookupDescription());
                              // TODO: -JVK- set the mapped instance's display name
                              queryDataColumnMember.setDisplayString(member.getLookupDescription());
                           }
                           queryDataColumnMembers.add(queryDataColumnMember);
                        }
                        colMeta.setMembers(queryDataColumnMembers);
                     }
                  } else {
                     List<QueryDataColumnMember> queryDataColumnMembers = new ArrayList<QueryDataColumnMember>();
                     for (String lookupValue : lookupValues) {
                        QueryDataColumnMember queryDataColumnMember = new QueryDataColumnMember();
                        queryDataColumnMember.setValue(lookupValue);
                        queryDataColumnMember.setDescription(lookupValue);
                        queryDataColumnMember.setDisplayString(lookupValue);
                        queryDataColumnMembers.add(queryDataColumnMember);
                     }
                     colMeta.setMembers(queryDataColumnMembers);
                  }
               }
            }
            queryDataIndex++;
         }
      }
      // process the ranges map and populate the header
      if (rangesMap.size() > 0) {
         int queryDataIndex = 0;
         for (int i = 0; i < columnCount; i++) {
            if (univariantIndices.contains(i)) {
               continue;
            }
            if (rangesMap.containsKey(i)) {
               List<RangeDetail> rList = rangesMap.get(i);
               QueryDataHeaderColumnMeta colMeta = queryDataHeaderColumns.get(queryDataIndex);
               colMeta.setMemberCount(rList.size());
               List<QueryDataColumnMember> queryDataColumnMembers = new ArrayList<QueryDataColumnMember>();
               // sort the lookup values based on the order of the range details and create the members
               RangeDetail[] rdArray = null;
               if (rList.size() > 0) {
                  rdArray = new RangeDetail[rList.size()];
                  int j = 0;
                  for (RangeDetail rd : rList) {
                     rdArray[j++] = rd;
                  }
               }
               Arrays.sort(rdArray, new RangeDetailOrderComparator());
               rList = Arrays.asList(rdArray);
               for (RangeDetail rd : rList) {
                  QueryDataColumnMember queryDataColumnMember = new QueryDataColumnMember();
                  queryDataColumnMember.setValue(String.valueOf(rd.getOrder()));
                  queryDataColumnMember.setDescription(rd.getDescription());
                  queryDataColumnMember.setDisplayString(rd.getDescription());
                  queryDataColumnMembers.add(queryDataColumnMember);
               }
               colMeta.setMembers(queryDataColumnMembers);
            }
            queryDataIndex++;
         }
      }
      return rows;
   }

   /**
    * @param reportMetaInfo
    * @param metaData
    * @param aliasBusinessAssetTerm
    * @throws Exception
    */
   private List<Integer> getUnivariantColumnIndices (ReportMetaInfo reportMetaInfo, ExeCueResultSetMetaData metaData,
            Map<String, BusinessAssetTerm> aliasBusinessAssetTerm) throws Exception {
      int dimensionCount = 0;
      int idCount = 0;
      int columnCount = metaData.getColumnCount();
      List<Integer> univariantIndices = new ArrayList<Integer>();
      for (int index = 0; index < columnCount; index++) {
         BusinessAssetTerm match = aliasBusinessAssetTerm.get(metaData.getColumnLabel(index));
         ReportColumnInfo reportColumnInfo = getCorrespondingReportColumnInfo(reportMetaInfo, match);
         boolean isDimensionType = false;
         // TODO: -JVK- handle single variant measures
         if (ColumnType.DIMENSION.equals(reportColumnInfo.getColumnType())
                  || ColumnType.SIMPLE_LOOKUP.equals(reportColumnInfo.getColumnType())
                  || ColumnType.RANGE_LOOKUP.equals(reportColumnInfo.getColumnType())) {
            isDimensionType = true;
            dimensionCount++;
         } else if (ColumnType.ID.equals(reportColumnInfo.getColumnType())) {
            idCount++;
         }
         // Identify single variants
         if (isDimensionType && reportColumnInfo.getCountSize() == 1) {
            univariantIndices.add(index);
         }
      }
      // check if the single variants can be applied or not
      // 28Jan2011 : JM : Added the condition to check for the availability of ID type columns before we
      // check whether univariants can be applied or not
      if (dimensionCount == univariantIndices.size() && idCount == 0) {
         univariantIndices.clear();
      }
      return univariantIndices;
   }

   private List<QueryDataHeaderColumnMeta> prepareQueryDataHeaderColumnMeta (ReportMetaInfo reportMetaInfo,
            ExeCueResultSetMetaData metaData, Map<String, BusinessAssetTerm> aliasBusinessAssetTerm,
            List<Integer> univariantIndices, List<Integer> colWithMemberIndices,
            Map<Integer, List<RangeDetail>> rangesMap,
            Map<Integer, ReportHierarchyColumnInfo> hierachcyColumnInfoByColumnIndex) throws Exception {
      List<QueryDataHeaderColumnMeta> queryDataHeaderColumns = new ArrayList<QueryDataHeaderColumnMeta>();
      int columnCount = metaData.getColumnCount();
      String colName = "c";
      int columnIndex = 0;
      boolean isHierarchySummary = reportMetaInfo.getSummaryPathType() == AggregateQueryType.HIERARCHY_SUMMARY;
      for (int index = 0; index < columnCount; index++) {
         String id = colName + columnIndex;
         String ctype = ColumnType.MEASURE.getValue();
         String plotAs = ctype;
         String dtype = metaData.getColumnTypeName(index);
         String conLabel = "";
         String dataFormat = "";
         String unitType = "";
         String unit = "";
         String precision = String.valueOf(metaData.getColumnPrecision(index));
         String scale = String.valueOf(metaData.getColumnScale(index));
         // String parent = "NONE";
         QueryDataHeaderColumnMeta queryDataHeaderColumnMeta = new QueryDataHeaderColumnMeta();
         if (!univariantIndices.contains(index)) {
            BusinessAssetTerm match = aliasBusinessAssetTerm.get(metaData.getColumnLabel(index));
            ReportColumnInfo reportColumnInfo = getCorrespondingReportColumnInfo(reportMetaInfo, match);
            if (isHierarchySummary) {
               ReportHierarchyColumnInfo reportHierarchyColumnInfo = getCorrespondingReportHierarchyColumnInfo(
                        reportMetaInfo, match);
               if (reportHierarchyColumnInfo != null) {
                  hierachcyColumnInfoByColumnIndex.put(index, reportHierarchyColumnInfo);
               }
            }
            // Obtain the column and the data format, unit and unit type values
            Colum column = reportColumnInfo.getColumn();
            // the below three attributes (dataFormat, unitType and unit on a column) could be null, so made as
            // empty
            if (ExecueCoreUtil.isNotEmpty(column.getDataFormat())) {
               dataFormat = column.getDataFormat();
            }
            if (ExecueCoreUtil.isNotEmpty(column.getUnit())) {
               unit = column.getUnit();
            }
            if (column.getConversionType() != null) {
               unitType = column.getConversionType().getValue();
            }
            // Get hold of the concept
            Concept concept = reportColumnInfo.getConcept();
            // Retrieve the display type of the concept
            if (concept != null) {
               concept = kdxRetrievalService.getPopulatedConceptWithStats(concept.getId());
               if (concept.getDefaultConversionType() != null
                        && !ConversionType.NULL.equals(concept.getDefaultConversionType())) {
                  dtype = concept.getDefaultConversionType().getValue();
               }
               /*
                * If the concept under consideration is a Population in nature and the asset is owned by ExeCue and of
                * type Cube then we should add COUNT as stat on the Business Term. This is necessary because the stat
                * doesn't come on this column from SQ. At Cube this column itself is count of Population.
                */
               if (concept.getStats().isEmpty()) {
                  logger.debug("the stats on concept are null for " + concept.getDisplayName());
                  Long conceptBedId = reportColumnInfo.getBizAssetTerm().getBusinessTerm().getBusinessEntityTerm()
                           .getBusinessEntityDefinitionId();
                  boolean isPopulationConcept = getKdxRetrievalService().isConceptMatchedBehavior(conceptBedId,
                           BehaviorType.POPULATION);
                  if (isPopulationConcept
                           && ExecueBeanUtil.isExecueOwnedCube(reportMetaInfo.getAssetQuery().getLogicalQuery()
                                    .getAsset())) {
                     // if the column is of ID type, then asset is of cube type then there must be a Count Stat
                     BusinessStat businessStat = new BusinessStat();
                     businessStat.setRequestedByUser(false);
                     businessStat.setStat(kdxRetrievalService.getStatByName(StatType.COUNT.getValue()));
                     reportColumnInfo.getBizAssetTerm().getBusinessTerm().setBusinessStat(businessStat);
                  }
               }
            }
            conLabel = ReportAggregationHelper.getReportColumnDescription(match);
            if (match.getBusinessTerm().isFromCohort()) {
               queryDataHeaderColumnMeta.setFromCohort(true);
            }

            boolean isDeducedAsDim = false;
            ColumnType deducedType = reportColumnInfo.getColumnType();
            ctype = deducedType.getValue();
            logger.debug("Deduced CTYPE : " + deducedType);
            logger.debug("KDX Data Type : " + column.getKdxDataType());
            if (ColumnType.DIMENSION.equals(deducedType) || ColumnType.SIMPLE_LOOKUP.equals(deducedType)
                     || ColumnType.RANGE_LOOKUP.equals(deducedType)) {
               isDeducedAsDim = true;
            }
            if (isDeducedAsDim && match.getBusinessTerm().getRange() == null) {
               colWithMemberIndices.add(index);
               ctype = ColumnType.DIMENSION.getValue();
               dtype = "string";
            } else if (match.getBusinessTerm().getRange() != null) {// has ranges
               List<RangeDetail> rList = new ArrayList<RangeDetail>();
               for (RangeDetail rd : match.getBusinessTerm().getRange().getRangeDetails()) {
                  rList.add(rd);
               }
               rangesMap.put(index, rList);
               ctype = ColumnType.DIMENSION.getValue();
               dtype = "string";
            }

            // populate the plotAs field
            plotAs = ReportAggregationHelper.populatePlotAs(ctype, reportColumnInfo, reportMetaInfo);

            // -JM- Added on 01-MAR-2011 : based on plotAs value, mark the column for member addition
            if (ColumnType.DIMENSION.getValue().equals(plotAs)) {
               colWithMemberIndices.add(index);
            }

            logger.debug("[ctype:dtype:label] : " + "[" + ctype + ":" + dtype + ":" + conLabel + "]");
            logger.debug("Data Format :-" + dataFormat + "-" + "; " + "Unit Type :-" + unitType + "-" + "; "
                     + "Unit :-" + unit + "-");
            // populate the column's meta information into the header
            queryDataHeaderColumnMeta.setId(id);
            queryDataHeaderColumnMeta.setDescription(conLabel);
            queryDataHeaderColumnMeta.setCtype(ctype);
            queryDataHeaderColumnMeta.setPlotAs(plotAs);
            queryDataHeaderColumnMeta.setDtype(dtype);
            queryDataHeaderColumnMeta.setDataFormat(dataFormat);
            queryDataHeaderColumnMeta.setUnitType(unitType);
            queryDataHeaderColumnMeta.setUnit(unit);
            // queryDataHeaderColumnMeta.setParent(parent);
            queryDataHeaderColumnMeta.setPrecision(precision);
            queryDataHeaderColumnMeta.setScale(scale);
            queryDataHeaderColumns.add(queryDataHeaderColumnMeta);
            columnIndex++;
         }
      }
      return queryDataHeaderColumns;
   }

   private ReportHierarchyColumnInfo getCorrespondingReportHierarchyColumnInfo (ReportMetaInfo reportMetaInfo,
            BusinessAssetTerm match) {
      ReportHierarchyColumnInfo reportColumnInfo = null;
      List<ReportHierarchyColumnInfo> hierarchyColumns = reportMetaInfo.getReportMetaHierarchyInfo().get(0)
               .getHierarchyColumns();
      for (ReportHierarchyColumnInfo colInfo : hierarchyColumns) {
         if (colInfo.getBizAssetTerm().getBusinessTerm().getBusinessEntityTerm().equals(
                  match.getBusinessTerm().getBusinessEntityTerm())) {
            reportColumnInfo = colInfo;
            break;
         }
      }
      return reportColumnInfo;
   }

   private static <T> List<String> sortLookupValues (List<T> lookupValues) {
      Collections.sort(lookupValues, new LookupValueComparator<T>());
      List<String> lookupStringValues = new ArrayList<String>();
      for (Object item : lookupValues) {
         lookupStringValues.add(String.valueOf(item).trim());
      }
      return lookupStringValues;
   }

   // private static List<String> sortLookupValues (List<String> lookupValues, String dType) {
   // /*
   // * if (dType.equalsIgnoreCase(DTYPE_AS_STRING)) { TODO: -SS- Need to check in case the lookupValues are actual
   // * numbers. Collections.sort(lookupValues, new LookupValueComparator()); return lookupValues; } else {
   // */
   // List<Integer> numberValues = new ArrayList<Integer>();
   // List<String> stringValues = new ArrayList<String>();
   // List<String> sortedValues = new ArrayList<String>();
   // // Check if the lookup values are numbers
   // for (String val : lookupValues) {
   // if (NumberUtils.isNumber(val)) {
   // // -JVK- : Treat any kind of number other than Integer as a String and proceed
   // try {
   // numberValues.add(Integer.parseInt(val));
   // } catch (NumberFormatException nfe) {
   // stringValues.add(val);
   // }
   // } else {
   // stringValues.add(val);
   // }
   // }
   // Collections.sort(numberValues);
   // Collections.sort(stringValues, new LookupValueComparator());
   // if (ExecueCoreUtil.isCollectionNotEmpty(numberValues)) {
   // for (Integer dVal : numberValues) {
   // sortedValues.add(dVal.toString());
   // }
   // }
   // sortedValues.addAll(stringValues);
   // return sortedValues;
   // // }
   // }

   private ReportColumnInfo getCorrespondingReportColumnInfo (ReportMetaInfo reportMetaInfo, BusinessAssetTerm match) {
      ReportColumnInfo reportColumnInfo = null;
      for (ReportColumnInfo colInfo : reportMetaInfo.getReportColumns()) {
         if (colInfo.getBizAssetTerm().getBusinessTerm().getBusinessEntityTerm().equals(
                  match.getBusinessTerm().getBusinessEntityTerm())
                  && colInfo.getBizAssetTerm().getAssetEntityTerm().equals(match.getAssetEntityTerm())) {
            reportColumnInfo = colInfo;
            break;
         }
      }
      return reportColumnInfo;
   }

   private Membr getMatchingMember (String lookupValue, Map<String, Membr> membersMap) {
      Membr match = null;
      if (ExecueCoreUtil.isNotEmpty(lookupValue)) {
         if (membersMap != null && membersMap.size() > 0) {
            match = membersMap.get(lookupValue.trim());
         }
      }
      return match;
   }

   private IQueryGenerationService getQueryGenerationService (Asset asset) {
      return getQueryGenerationServiceFactory().getQueryGenerationService(asset);
   }

   public IStructuredQueryTransformerService getStructuredQueryTransformerService () {
      return structuredQueryTransformerService;
   }

   public void setStructuredQueryTransformerService (
            IStructuredQueryTransformerService structuredQueryTransformerService) {
      this.structuredQueryTransformerService = structuredQueryTransformerService;
   }

   public QueryGenerationServiceFactory getQueryGenerationServiceFactory () {
      return queryGenerationServiceFactory;
   }

   public void setQueryGenerationServiceFactory (QueryGenerationServiceFactory queryGenerationServiceFactory) {
      this.queryGenerationServiceFactory = queryGenerationServiceFactory;
   }

   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }

   public ISDXRetrievalService getSdxRetrievalService () {
      return sdxRetrievalService;
   }

   public void setSdxRetrievalService (ISDXRetrievalService sdxRetrievalService) {
      this.sdxRetrievalService = sdxRetrievalService;
   }

   /**
    * @return the aggregationConfigurationService
    */
   public IAggregationConfigurationService getAggregationConfigurationService () {
      return aggregationConfigurationService;
   }

   /**
    * @param aggregationConfigurationService
    *           the aggregationConfigurationService to set
    */
   public void setAggregationConfigurationService (IAggregationConfigurationService aggregationConfigurationService) {
      this.aggregationConfigurationService = aggregationConfigurationService;
   }

   public IClientSourceDAO getClientSourceDAO () {
      return clientSourceDAO;
   }

   public void setClientSourceDAO (IClientSourceDAO clientSourceDAO) {
      this.clientSourceDAO = clientSourceDAO;
   }
}