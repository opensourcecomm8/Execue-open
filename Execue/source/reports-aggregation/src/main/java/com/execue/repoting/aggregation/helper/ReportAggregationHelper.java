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


package com.execue.repoting.aggregation.helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.log4j.Logger;

import com.execue.core.common.bean.AggregateQuery;
import com.execue.core.common.bean.BusinessStat;
import com.execue.core.common.bean.BusinessTerm;
import com.execue.core.common.bean.ExeCueResultSet;
import com.execue.core.common.bean.HierarchyTerm;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.AssetEntityDefinition;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.Instance;
import com.execue.core.common.bean.entity.Membr;
import com.execue.core.common.bean.entity.Range;
import com.execue.core.common.bean.entity.RangeDetail;
import com.execue.core.common.bean.entity.Stat;
import com.execue.core.common.bean.governor.AssetEntityTerm;
import com.execue.core.common.bean.governor.AssetQuery;
import com.execue.core.common.bean.governor.BusinessAssetTerm;
import com.execue.core.common.bean.governor.BusinessEntityTerm;
import com.execue.core.common.bean.governor.StructuredCondition;
import com.execue.core.common.bean.governor.StructuredLimitClause;
import com.execue.core.common.bean.governor.StructuredOrderClause;
import com.execue.core.common.bean.governor.StructuredQuery;
import com.execue.core.common.bean.querygen.Query;
import com.execue.core.common.bean.querygen.QueryColumn;
import com.execue.core.common.bean.querygen.QueryGenerationInput;
import com.execue.core.common.bean.querygen.QueryGenerationOutput;
import com.execue.core.common.bean.querygen.QueryTableColumn;
import com.execue.core.common.bean.querygen.QueryValue;
import com.execue.core.common.bean.querygen.SelectEntity;
import com.execue.core.common.bean.security.User;
import com.execue.core.common.type.AggregateQueryType;
import com.execue.core.common.type.AssetEntityType;
import com.execue.core.common.type.BusinessEntityType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.ColumnType;
import com.execue.core.common.type.DataType;
import com.execue.core.common.type.OperandType;
import com.execue.core.common.type.OperatorType;
import com.execue.core.common.type.OrderEntityType;
import com.execue.core.common.type.OrderLimitEntityType;
import com.execue.core.common.type.ReportType;
import com.execue.core.common.type.SelectEntityType;
import com.execue.core.common.type.StatType;
import com.execue.core.common.util.ExecueBeanCloneUtil;
import com.execue.core.configuration.ICoreConfigurationService;
import com.execue.core.constants.ExecueConstants;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.dataaccess.clientsource.IClientSourceDAO;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.querygen.service.IQueryGenerationService;
import com.execue.querygen.service.IStructuredQueryTransformerService;
import com.execue.querygen.service.QueryGenerationServiceFactory;
import com.execue.repoting.aggregation.analyze.processor.RangeProcessor;
import com.execue.repoting.aggregation.bean.AggregationBusinessAssetTerm;
import com.execue.repoting.aggregation.bean.AggregationColumnInfo;
import com.execue.repoting.aggregation.bean.AggregationCondition;
import com.execue.repoting.aggregation.bean.AggregationHierarchyColumnInfo;
import com.execue.repoting.aggregation.bean.AggregationLimitClause;
import com.execue.repoting.aggregation.bean.AggregationMetaHierarchyInfo;
import com.execue.repoting.aggregation.bean.AggregationMetaInfo;
import com.execue.repoting.aggregation.bean.AggregationOrderClause;
import com.execue.repoting.aggregation.bean.AggregationRangeDetail;
import com.execue.repoting.aggregation.bean.AggregationRangeInfo;
import com.execue.repoting.aggregation.bean.AggregationStructuredQuery;
import com.execue.repoting.aggregation.bean.ReportColumnInfo;
import com.execue.repoting.aggregation.bean.ReportHierarchyColumnInfo;
import com.execue.repoting.aggregation.bean.ReportMetaHierarchyInfo;
import com.execue.repoting.aggregation.bean.ReportMetaInfo;
import com.execue.repoting.aggregation.bean.ReportSelectionConstants;
import com.execue.repoting.aggregation.comparator.ColumnCountComparator;
import com.execue.repoting.aggregation.configuration.IAggregationConfigurationService;
import com.execue.repoting.aggregation.exception.AggregationExceptionCodes;
import com.execue.repoting.aggregation.exception.ReportException;
import com.execue.repoting.aggregation.exception.ReportMetadataException;
import com.execue.repoting.aggregation.exception.ReportQueryGenerationException;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.MappingException;
import com.execue.swi.exception.SDXException;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.swi.service.IMappingRetrievalService;
import com.execue.swi.service.ISDXRetrievalService;
import com.thoughtworks.xstream.XStream;

/**
 * @author John Mallavalli
 */

public class ReportAggregationHelper {

   private static final Logger                logger           = Logger.getLogger(ReportAggregationHelper.class);
   private static final String                COHORT_INDICATOR = "(*)";
   private static final String                COMMA            = ",";
   private static final String                SPACE            = " ";
   private static final String                AND              = "and";
   private static final String                OR               = "or";
   private static final String                EQUALS           = "equals";
   private static final String                NOT_EQUALS       = "not equal to";
   private static final String                GREATER_THAN     = "greater than";
   private static final String                LESS_THAN        = "less than";
   private static final String                IS_NULL          = "is null";
   private static final String                IS_NOT_NULL      = "is not null";
   private static final String                BETWEEN          = "between";
   private static final String                IN               = "in";
   private static final String                OPEN_BRACKET     = "(";
   private static final String                CLOSE_BRACKET    = ")";
   private static final String                OF_WITH_SPACES   = " of ";
   private static final String                WHERE            = "where";

   private ICoreConfigurationService          coreConfigurationService;
   private IAggregationConfigurationService   aggregationConfigurationService;
   private ISDXRetrievalService               sdxRetrievalService;
   private IMappingRetrievalService           mappingRetrievalService;
   private IKDXRetrievalService               kdxRetrievalService;
   private QueryGenerationServiceFactory      queryGenerationServiceFactory;
   private IClientSourceDAO                   clientSourceDAO;
   private RangeProcessor                     rangeProcessor;
   private IStructuredQueryTransformerService structuredQueryTransformerService;

   /**
    * This method modifies the structured query by rearranging the select, group by and order by sections, <BR>
    * so that the dimensions appear first, the measures with the stats and then the id columns
    */
   public void rearrangeStructuredQuery (ReportMetaInfo reportMetaInfo) {
      List<ReportColumnInfo> ids = new ArrayList<ReportColumnInfo>();
      List<ReportColumnInfo> measures = new ArrayList<ReportColumnInfo>();
      List<ReportColumnInfo> dimensions = new ArrayList<ReportColumnInfo>();
      for (ReportColumnInfo reportColumnInfo : reportMetaInfo.getReportColumns()) {
         ColumnType columnType = reportColumnInfo.getColumnType();
         switch (columnType) {
            case ID:
               ids.add(reportColumnInfo);
               break;
            case MEASURE:
               measures.add(reportColumnInfo);
               break;
            case DIMENSION:
            case SIMPLE_LOOKUP:
            case RANGE_LOOKUP:
               dimensions.add(reportColumnInfo);
               break;
         }
      }
      StructuredQuery structuredQuery = reportMetaInfo.getAssetQuery().getLogicalQuery();
      rearrangeSelectGroupByOrderByDimensions(structuredQuery, dimensions, ids);
   }

   public void rearrangeStructuredQueryForDetailReport (ReportMetaInfo reportMetaInfo) {
      List<ReportColumnInfo> ids = new ArrayList<ReportColumnInfo>();
      List<ReportColumnInfo> measures = new ArrayList<ReportColumnInfo>();
      List<ReportColumnInfo> dimensions = new ArrayList<ReportColumnInfo>();
      for (ReportColumnInfo reportColumnInfo : reportMetaInfo.getReportColumns()) {
         ColumnType columnType = reportColumnInfo.getColumnType();
         switch (columnType) {
            case ID:
               ids.add(reportColumnInfo);
               break;
            case MEASURE:
               measures.add(reportColumnInfo);
               break;
            case DIMENSION:
            case SIMPLE_LOOKUP:
            case RANGE_LOOKUP:
               dimensions.add(reportColumnInfo);
               break;
         }
      }
      StructuredQuery structuredQuery = reportMetaInfo.getAssetQuery().getLogicalQuery();
      rearrangeSelectForDetailReport(structuredQuery, dimensions, ids);
   }

   /**
    * This method prepares the meta info object for the asset query
    * 
    * @param assetQuery
    */
   public ReportMetaInfo prepareReportMetaInfo (AssetQuery assetQuery) {
      StructuredQuery structuredQuery = assetQuery.getLogicalQuery();
      List<ReportColumnInfo> reportColumns = prepareReportColumns(structuredQuery.getMetrics());

      // Process the hierarchies
      List<ReportMetaHierarchyInfo> reportMetaHierarchyInfos = new ArrayList<ReportMetaHierarchyInfo>();
      List<HierarchyTerm> hierarchies = structuredQuery.getHierarchies();
      if (ExecueCoreUtil.isCollectionNotEmpty(hierarchies)) {
         for (HierarchyTerm hierarchyTerm : hierarchies) {

            // Prepare the reportMetaHierarchyInfo and add to the list
            List<BusinessAssetTerm> hierarchyBusinessAssetTerms = hierarchyTerm.getHierarchyBusinessAssetDefinition();
            List<ReportHierarchyColumnInfo> hierarchyColumns = new ArrayList<ReportHierarchyColumnInfo>();
            int level = 0;
            for (BusinessAssetTerm hierarchyBusinessAssetTerm : hierarchyBusinessAssetTerms) {
               ReportHierarchyColumnInfo reportHierarchyColumnInfo = new ReportHierarchyColumnInfo();
               reportHierarchyColumnInfo.setBizAssetTerm(hierarchyBusinessAssetTerm);
               reportHierarchyColumnInfo.setLevel(level++);
               hierarchyColumns.add(reportHierarchyColumnInfo);
            }

            ReportMetaHierarchyInfo reportMetaHierarchyInfo = new ReportMetaHierarchyInfo();
            reportMetaHierarchyInfo.setHierarchyName(hierarchyTerm.getHierarchyName());
            reportMetaHierarchyInfo.setHierarchyColumns(hierarchyColumns);

            reportMetaHierarchyInfos.add(reportMetaHierarchyInfo);
         }
      }
      ReportMetaInfo reportMetaInfo = new ReportMetaInfo(assetQuery);
      reportMetaInfo.setReportColumns(reportColumns);
      reportMetaInfo.setReportMetaHierarchyInfo(reportMetaHierarchyInfos);
      return reportMetaInfo;
   }

   /**
    * @param structuredQuery
    * @return
    */
   public List<ReportColumnInfo> prepareReportColumns (List<BusinessAssetTerm> businessAssetTerms) {
      List<ReportColumnInfo> reportColumns = new ArrayList<ReportColumnInfo>();
      for (BusinessAssetTerm businessAssetTerm : businessAssetTerms) {
         reportColumns.add(prepareReportColumn(businessAssetTerm));
      }
      return reportColumns;
   }

   /**
    * @param businessAssetTerm
    * @return
    */
   private ReportColumnInfo prepareReportColumn (BusinessAssetTerm businessAssetTerm) {
      return new ReportColumnInfo(businessAssetTerm);
   }

   /**
    * This method will process the uni-variants of the requested query and remove them from the select, group by & order
    * by clauses of the structured query
    * 
    * @param aggregateQuery
    * @param reportMetaInfo
    */
   public void processUnivariants (ReportMetaInfo reportMetaInfo) {
      List<ReportColumnInfo> uniVariants = new ArrayList<ReportColumnInfo>();
      for (ReportColumnInfo reportColumnInfo : reportMetaInfo.getReportColumns()) {
         // Check if the term is having count=1 and a dimension
         if (reportColumnInfo.getCountSize() == 1) {
            ColumnType columnType = reportColumnInfo.getColumnType();
            if (isDimensionType(columnType)) {
               uniVariants.add(reportColumnInfo);
               // remove the corresponding term from group by and order by sections
               // removeFromGroupByOrderBy(reportMetaInfo, reportColumnInfo);
               // remove the term from the select section
               removeFromSelect(reportMetaInfo, reportColumnInfo);
               // add into the where section of the SQ
               addUnivariantWhereCondition(reportMetaInfo, reportColumnInfo);
            }
         }
      }
      // TODO: do we need to remove the univariant report column info objects from the report meta info
      // remove the uni-variants, if any, from the meta info object
      reportMetaInfo.getReportColumns().removeAll(uniVariants);
   }

   public static boolean checkIfAllDimensionAreUnivariants (ReportMetaInfo reportMetaInfo) {
      boolean allDimensionAreUnivaraints = true;
      for (ReportColumnInfo reportColumnInfo : reportMetaInfo.getReportColumns()) {
         ColumnType columnType = reportColumnInfo.getColumnType();
         // Check if the term is having count=1 and a dimension
         if (isDimensionType(columnType) && reportColumnInfo.getCountSize() != 1) {
            allDimensionAreUnivaraints = false;
         }
      }
      return allDimensionAreUnivaraints;
   }

   /**
    * @param columnType
    * @return
    */
   private static boolean isDimensionType (ColumnType columnType) {
      return ColumnType.DIMENSION.equals(columnType) || ColumnType.RANGE_LOOKUP.equals(columnType)
               || ColumnType.SIMPLE_LOOKUP.equals(columnType);
   }

   /**
    * This method will remove the term represented by the reportColumnInfo object from the select clause of the
    * structured query
    * 
    * @param aggregateQuery
    * @param reportMetaInfo
    * @param reportColumnInfo
    */
   public void removeFromSelect (ReportMetaInfo reportMetaInfo, ReportColumnInfo reportColumnInfo) {
      List<BusinessAssetTerm> metrics = reportMetaInfo.getAssetQuery().getLogicalQuery().getMetrics();
      BusinessAssetTerm match = null;
      if (metrics != null && metrics.size() > 0) {
         for (BusinessAssetTerm metric : metrics) {
            if (metric.getBusinessTerm().getBusinessEntityTerm().equals(
                     reportColumnInfo.getBizAssetTerm().getBusinessTerm().getBusinessEntityTerm())) {
               match = metric;
               break;
            }
         }
      }
      if (match != null) {
         metrics.remove(match);
      }
   }

   /**
    * Adds a new "Condition" into the StructuredQuery representing the univariant ReportColumnInfo object
    * 
    * @param reportMetaInfo
    * @param reportColumnInfo
    */
   private void addUnivariantWhereCondition (ReportMetaInfo reportMetaInfo, ReportColumnInfo reportColumnInfo) {
      List<StructuredCondition> conditions = reportMetaInfo.getAssetQuery().getLogicalQuery().getConditions();
      if (ExecueCoreUtil.isCollectionNotEmpty(conditions)) {
         for (StructuredCondition condition : conditions) {
            if (!condition.isSubCondition()
                     && condition.getLhsBusinessAssetTerm().getBusinessTerm().getBusinessEntityTerm().equals(
                              reportColumnInfo.getBizAssetTerm().getBusinessTerm().getBusinessEntityTerm())) {
               return;
            }
         }
      } else {
         conditions = new ArrayList<StructuredCondition>();
      }
      // prepare a new condition
      StructuredCondition univariantCondition = new StructuredCondition();
      univariantCondition.setLhsBusinessAssetTerm(reportColumnInfo.getBizAssetTerm());
      univariantCondition.setOperator(OperatorType.EQUALS);
      univariantCondition.setOperandType(OperandType.VALUE);
      List<QueryValue> rhsValues = new ArrayList<QueryValue>();
      QueryValue queryValue = new QueryValue();
      queryValue.setValue(reportColumnInfo.getUnivariantValue());
      queryValue.setDataType(DataType.STRING);
      rhsValues.add(queryValue);
      univariantCondition.setRhsValues(rhsValues);
      conditions.add(univariantCondition);
   }

   /**
    * This method does the following in the order: <BR>
    * 1. Sort the dimensions based on the member counts.<BR>
    * 2. Put all the dimensions not belonging to the cohort query into GroupBy and OrderBy.<BR>
    * 3. Rearrange the select section terms so that they are arranged in the order of DIMESIONS, MEASURES & IDS.<BR>
    * 
    * @param structuredQuery
    * @param dimensions
    * @param ids
    */
   private void rearrangeSelectGroupByOrderByDimensions (StructuredQuery structuredQuery,
            List<ReportColumnInfo> dimensions, List<ReportColumnInfo> ids) {
      // 27-Jan-2011 : Re-organized the code so that the TOP-BOTTOM case is separated entirely from the normal route
      // Re-arranged the select order for the top-bottom case so that the order is IDs, DIMs & MEAs
      List<StructuredOrderClause> finalOrderClauses = new ArrayList<StructuredOrderClause>();
      if (structuredQuery.getTopBottom() == null) {
         List<BusinessAssetTerm> summarizations = structuredQuery.getSummarizations();
         if (dimensions.size() > 0) {
            dimensions = sortDimensionsByMemberCount(dimensions);
            summarizations = new ArrayList<BusinessAssetTerm>();
            for (ReportColumnInfo reportColumnInfo : dimensions) {
               BusinessAssetTerm businessAssetTerm = reportColumnInfo.getBizAssetTerm();
               businessAssetTerm.getBusinessTerm().setBusinessStat(null);
               summarizations.add(businessAssetTerm);
            }
         }
         structuredQuery.setSummarizations(summarizations);
         if (dimensions.size() > 0) {
            for (ReportColumnInfo reportColumnInfo : dimensions) {
               StructuredOrderClause orderClause = getStructuredOrderClause(reportColumnInfo.getBizAssetTerm());
               orderClause.getBusinessAssetTerm().getBusinessTerm().setBusinessStat(null);
               finalOrderClauses.add(orderClause);
            }
         }
         if (finalOrderClauses.size() > 0) {
            structuredQuery.setOrderClauses(finalOrderClauses);
         }
         List<BusinessAssetTerm> metrics = structuredQuery.getMetrics();
         // First, move the dimensions to the start of the select section
         List<BusinessAssetTerm> orderedMetrics = new ArrayList<BusinessAssetTerm>();
         if (dimensions.size() > 0) {
            for (ReportColumnInfo col : dimensions) {
               for (BusinessAssetTerm term : metrics) {
                  if (col.getBizAssetTerm() == term) {
                     orderedMetrics.add(term);
                  }
               }
            }
         }
         if (orderedMetrics.size() > 0) {
            for (BusinessAssetTerm term : orderedMetrics) {
               metrics.remove(term);
            }
         }
         orderedMetrics.addAll(metrics);
         // Move the ids to the end of the select section
         List<BusinessAssetTerm> finalOrderedMetrics = new ArrayList<BusinessAssetTerm>();
         if (ids.size() > 0) {
            for (ReportColumnInfo col : ids) {
               for (BusinessAssetTerm term : orderedMetrics) {
                  if (col.getBizAssetTerm() == term) {
                     finalOrderedMetrics.add(term);
                  }
               }
            }
         }
         if (finalOrderedMetrics.size() > 0) {
            for (BusinessAssetTerm term : finalOrderedMetrics) {
               orderedMetrics.remove(term);
            }
         }
         finalOrderedMetrics.addAll(0, orderedMetrics);
         structuredQuery.setMetrics(finalOrderedMetrics);
      } else {
         // Top Bottom case
         structuredQuery.setSummarizations(null);
         if (dimensions.size() > 0) {
            for (ReportColumnInfo reportColumnInfo : dimensions) {
               StructuredOrderClause orderClause = getStructuredOrderClause(reportColumnInfo.getBizAssetTerm());
               orderClause.getBusinessAssetTerm().getBusinessTerm().setBusinessStat(null);
               finalOrderClauses.add(orderClause);
            }
         }
         // handle the Top Bottom entity so that it is added as the first entry in the order clause
         if (structuredQuery.getTopBottom() != null) {
            StructuredOrderClause topBottomClause = new StructuredOrderClause();
            StructuredLimitClause topBottom = structuredQuery.getTopBottom();
            topBottomClause.setBusinessAssetTerm(topBottom.getBusinessAssetTerm());
            if (OrderLimitEntityType.TOP.equals(topBottom.getLimitType())) {
               topBottomClause.setOrderEntityType(OrderEntityType.DESCENDING);
            } else if (OrderLimitEntityType.BOTTOM.equals(topBottom.getLimitType())) {
               topBottomClause.setOrderEntityType(OrderEntityType.ASCENDING);
            }
            finalOrderClauses.add(0, topBottomClause);
         }
         if (finalOrderClauses.size() > 0) {
            structuredQuery.setOrderClauses(finalOrderClauses);
         }
         List<BusinessAssetTerm> metrics = structuredQuery.getMetrics();
         // First, move the dimensions to the start of the select section
         List<BusinessAssetTerm> orderedMetrics = new ArrayList<BusinessAssetTerm>();
         if (dimensions.size() > 0) {
            for (ReportColumnInfo col : dimensions) {
               for (BusinessAssetTerm term : metrics) {
                  if (col.getBizAssetTerm() == term) {
                     orderedMetrics.add(term);
                  }
               }
            }
         }
         if (orderedMetrics.size() > 0) {
            for (BusinessAssetTerm term : orderedMetrics) {
               metrics.remove(term);
            }
         }
         orderedMetrics.addAll(metrics);
         // Move the ids to the start of the select section
         List<BusinessAssetTerm> finalOrderedMetrics = new ArrayList<BusinessAssetTerm>();
         if (ids.size() > 0) {
            for (ReportColumnInfo col : ids) {
               for (BusinessAssetTerm term : orderedMetrics) {
                  if (col.getBizAssetTerm() == term) {
                     finalOrderedMetrics.add(0, term);
                  }
               }
            }
         }
         if (finalOrderedMetrics.size() > 0) {
            for (BusinessAssetTerm term : finalOrderedMetrics) {
               orderedMetrics.remove(term);
            }
         }
         finalOrderedMetrics.addAll(orderedMetrics);
         structuredQuery.setMetrics(finalOrderedMetrics);
      }
   }

   // id columns first, followed by dimensions and then measures at the end
   private void rearrangeSelectForDetailReport (StructuredQuery structuredQuery, List<ReportColumnInfo> dimensions,
            List<ReportColumnInfo> ids) {
      // if (structuredQuery.getTopBottom() == null) {
      if (dimensions.size() > 0) {
         // Add logic to separate out the time-frame dimensions
         // 1. Check for user requested dimensions (URDs) and add them to a list
         // 2. Gather the remaining dimensions into another list
         // 3. Sort both these lists separately and then join them
         // 4. If there are no URDs, then check if there are any time-frame dimensions.
         // 5. If any time-frame dimension is found, then put it as the first metric
         List<ReportColumnInfo> userRequestedDimensions = new ArrayList<ReportColumnInfo>();
         List<ReportColumnInfo> deducedDimensions = new ArrayList<ReportColumnInfo>();
         boolean timeFrameDimPresent = false;
         for (ReportColumnInfo dim : dimensions) {
            if (dim.isUserRequestedSummarization()) {
               userRequestedDimensions.add(dim);
            } else {
               deducedDimensions.add(dim);
               if (!timeFrameDimPresent) {
                  timeFrameDimPresent = isTimeFrameColumn(dim);
               }
            }
         }
         // sort the URDs list
         if (userRequestedDimensions.size() > 0) {
            userRequestedDimensions = sortDimensionsByMemberCount(userRequestedDimensions);
         }
         // sort the deduced dimensions list
         if (deducedDimensions.size() > 0) {
            deducedDimensions = sortDimensionsByMemberCount(deducedDimensions);
         }
         // join both the lists
         List<ReportColumnInfo> finalDimensions = new ArrayList<ReportColumnInfo>();
         if (userRequestedDimensions.size() > 0) {
            finalDimensions.addAll(userRequestedDimensions);
            if (deducedDimensions.size() > 0) {
               finalDimensions.addAll(deducedDimensions);
            }
         } else { // there are no user requested dimensions
            // check if there are time-frames
            if (timeFrameDimPresent) {
               // move the time-frames to the beginning of the list
               List<ReportColumnInfo> tfDims = new ArrayList<ReportColumnInfo>();
               List<ReportColumnInfo> nonTFDims = new ArrayList<ReportColumnInfo>();
               for (ReportColumnInfo tfDim : deducedDimensions) {
                  if (isTimeFrameColumn(tfDim)) {
                     tfDims.add(tfDim);
                  } else {
                     nonTFDims.add(tfDim);
                  }
               }
               finalDimensions.addAll(tfDims);
               finalDimensions.addAll(nonTFDims);
            } else {
               if (deducedDimensions.size() > 0) {
                  finalDimensions.addAll(deducedDimensions);
               }
            }
         }
         dimensions = finalDimensions;
      }
      // } // end of topbottom if loop
      structuredQuery.setSummarizations(null);
      List<StructuredOrderClause> finalOrderClauses = new ArrayList<StructuredOrderClause>();
      if (dimensions.size() > 0) {
         for (ReportColumnInfo reportColumnInfo : dimensions) {
            StructuredOrderClause orderClause = getStructuredOrderClause(reportColumnInfo.getBizAssetTerm());
            // check if the dimension is of time-frame type and set to Descending order
            if (isTimeFrameColumn(reportColumnInfo)) {
               orderClause.setOrderEntityType(OrderEntityType.DESCENDING);
            }
            orderClause.getBusinessAssetTerm().getBusinessTerm().setBusinessStat(null);
            finalOrderClauses.add(orderClause);
         }
      }
      // handle the Top Bottom entity so that it is added as the first entry in the order clause
      if (structuredQuery.getTopBottom() != null) {
         StructuredOrderClause topBottomClause = new StructuredOrderClause();
         StructuredLimitClause topBottom = structuredQuery.getTopBottom();
         topBottomClause.setBusinessAssetTerm(topBottom.getBusinessAssetTerm());
         if (OrderLimitEntityType.TOP.equals(topBottom.getLimitType())) {
            topBottomClause.setOrderEntityType(OrderEntityType.DESCENDING);
         } else if (OrderLimitEntityType.BOTTOM.equals(topBottom.getLimitType())) {
            topBottomClause.setOrderEntityType(OrderEntityType.ASCENDING);
         }
         finalOrderClauses.add(0, topBottomClause);
      }
      if (finalOrderClauses.size() > 0) {
         structuredQuery.setOrderClauses(finalOrderClauses);
      }

      List<BusinessAssetTerm> metrics = structuredQuery.getMetrics();
      // First, move the dimensions to the start of the select section
      List<BusinessAssetTerm> orderedMetrics = new ArrayList<BusinessAssetTerm>();
      if (dimensions.size() > 0) {
         for (ReportColumnInfo col : dimensions) {
            for (BusinessAssetTerm term : metrics) {
               if (col.getBizAssetTerm() == term) {
                  orderedMetrics.add(term);
               }
            }
         }
      }
      if (orderedMetrics.size() > 0) {
         for (BusinessAssetTerm term : orderedMetrics) {
            metrics.remove(term);
         }
      }
      orderedMetrics.addAll(metrics);
      // Move the ids to the start of the select section
      List<BusinessAssetTerm> finalOrderedMetrics = new ArrayList<BusinessAssetTerm>();
      if (ids.size() > 0) {
         for (ReportColumnInfo col : ids) {
            for (BusinessAssetTerm term : orderedMetrics) {
               if (col.getBizAssetTerm() == term) {
                  finalOrderedMetrics.add(0, term);
               }
            }
         }
      }
      if (finalOrderedMetrics.size() > 0) {
         for (BusinessAssetTerm term : finalOrderedMetrics) {
            orderedMetrics.remove(term);
         }
      }
      finalOrderedMetrics.addAll(orderedMetrics);
      structuredQuery.setMetrics(finalOrderedMetrics);
   }

   /**
    * Checks if the column is of time frame type or not
    */
   private boolean isTimeFrameColumn (ReportColumnInfo reportColumnInfo) {
      boolean isTimeFrame = false;
      try {
         isTimeFrame = getKdxRetrievalService().isMatchedBusinessEntityType(
                  reportColumnInfo.getBizAssetTerm().getBusinessTerm().getBusinessEntityTerm()
                           .getBusinessEntityDefinitionId(), ExecueConstants.TIME_FRAME_TYPE);
      } catch (KDXException e) {
         // do nothing - BAD
      }
      return isTimeFrame;
   }

   /**
    * This helper method prepares the StructuredOrderClause from a BusinessAssetTerm
    * 
    * @param businessAssetTerm
    */
   private StructuredOrderClause getStructuredOrderClause (BusinessAssetTerm businessAssetTerm) {
      StructuredOrderClause structuredOrderClause = new StructuredOrderClause();
      structuredOrderClause.setBusinessAssetTerm(businessAssetTerm);
      structuredOrderClause.setOrderEntityType(OrderEntityType.ASCENDING);
      return structuredOrderClause;
   }

   /**
    * This method will sort the list of the dimensions by their member count in the descending order
    */
   private List<ReportColumnInfo> sortDimensionsByMemberCount (List<ReportColumnInfo> dimensions) {
      Collections.sort(dimensions, new ColumnCountComparator());
      return dimensions;
   }

   /**
    * This method iterates through the ResultSet and generates the list of the distinct counts of the metrics
    * 
    * @param reportMetaInfo
    */
   private List<Integer> getUniqueValueCounts (ReportMetaInfo reportMetaInfo) throws ReportException {
      List<Integer> counts = null;
      Query modifiedQuery = null;
      try {
         // processQuery(reportMetaInfo.getAssetQuery().getPhysicalQuery());
         modifiedQuery = getModifiedQuery(reportMetaInfo.getAssetQuery().getPhysicalQuery());
         ExeCueResultSet results = getCountsResultSet(modifiedQuery, reportMetaInfo.getAssetQuery().getLogicalQuery()
                  .getAsset());
         counts = new ArrayList<Integer>();
         if (results != null) {
            while (results.next()) {
               logger.debug("total count : count(*) : " + results.getString(0));
               counts.add(0, new Integer(results.getString(0)));
               for (int i = 0; i < reportMetaInfo.getReportColumns().size(); i++) {
                  logger.debug("count : " + results.getString(i + 1));
                  counts.add(new Integer(results.getString(i + 1)));
               }
            }
         }
      } catch (ReportException reportException) {
         throw reportException;
      } catch (Exception exception) {
         exception.printStackTrace();
         logger.error("Exception in ReportAggregationHelper", exception);
         logger.error("Cause : " + exception.getCause());
         throw new ReportException(ExeCueExceptionCodes.RESULT_SET_ERROR,
                  "Error while processing the ExecueResultSet object", exception.getCause());
      } finally {
         deprocessQuery(modifiedQuery);
      }
      return counts;
   }

   /**
    * This method will retrieve the counts of the metrics from the data source and populate the respective
    * ReportColumnInfo object
    * 
    * @param reportMetaInfo
    */
   public void runUniqueCount (ReportMetaInfo reportMetaInfo) throws ReportException {
      int index = 0;
      List<Integer> counts = getUniqueValueCounts(reportMetaInfo);
      if (ExecueCoreUtil.isCollectionNotEmpty(counts)) {
         // Remove the COUNT(*) value from the counts list
         reportMetaInfo.setTotalCount(counts.remove(0).longValue());
         for (ReportColumnInfo reportColumnInfo : reportMetaInfo.getReportColumns()) {
            reportColumnInfo.setCountSize(counts.get(index++).longValue());
         }
      } else {// count failed, set to default
         reportMetaInfo.setTotalCount(-1);
         for (ReportColumnInfo reportColumnInfo : reportMetaInfo.getReportColumns()) {
            reportColumnInfo.setCountSize(-1);
         }
         logger.error("Counts query failed, could not retrieve counts !!");
      }
      reportMetaInfo.setUniqueCounts(true);
   }

   /**
    * This method will generate a new Query object with all the sections, except GroupBy, OrderBy & Having sections,
    * taken from the supplied Query object
    * 
    * @param originalQuery
    */
   private Query suppressGroupByOrderBy (Query originalQuery) {
      Query query = new Query();
      query.setAlias(originalQuery.getAlias());
      query.setFromEntities(originalQuery.getFromEntities());
      query.setJoinEntities(originalQuery.getJoinEntities());
      query.setSelectEntities(originalQuery.getSelectEntities());
      query.setWhereEntities(originalQuery.getWhereEntities());
      query.setRollupQuery(originalQuery.isRollupQuery());
      return query;
   }

   /**
    * This method generates the ResultSet containing the counts of the metrics
    * 
    * @param query
    * @param targetAsset
    */
   private ExeCueResultSet getCountsResultSet (Query query, Asset targetAsset) throws ReportException {
      ExeCueResultSet results = null;
      QueryGenerationInput queryGenerationInput = new QueryGenerationInput();
      queryGenerationInput.setTargetAsset(targetAsset);
      QueryGenerationOutput queryGenerationOutput = new QueryGenerationOutput();
      queryGenerationOutput.setResultQuery(query);
      queryGenerationOutput.setQueryGenerationInput(queryGenerationInput);
      String queryString = getQueryGenerationService(targetAsset).extractQueryRepresentation(targetAsset,
               queryGenerationOutput.getResultQuery()).getQueryString();
      logger.info("\nCounts Query String : \n" + queryString);
      try {
         results = getClientSourceDAO().executeQuery(targetAsset.getDataSource(), query, queryString);
         logger.info("\nTime taken by Counts Query : " + results.getQueryExecutionTime() + "\n");
      } catch (DataAccessException dataAccessException) {
         dataAccessException.printStackTrace();
         logger.error("DataAccessException in ReportAggregationHelper", dataAccessException);
         logger.error("Actual Error : [" + dataAccessException.getCode() + "] " + dataAccessException.getMessage());
         logger.error("Cause : " + dataAccessException.getCause());
         throw new ReportException(AggregationExceptionCodes.COUNTS_RETRIEVAL_EXCEPTION_CODE,
                  "Error while retrieving the counts", dataAccessException.getCause());
      }
      return results;
   }

   /**
    * This method will add the intermediate statistic that needs to be applied on the select entity of the query <BR>
    * to retrieve more information about the column corresponding to the select entity.
    * 
    * @param query
    *           the query containing the select entities which need to be modified
    * @param functionName
    *           statistic to be applied
    */
   private void processQuery (Query query) {
      for (SelectEntity selectEntity : query.getSelectEntities()) {
         selectEntity.setFunctionName(StatType.COUNT);
         if (selectEntity.getTableColumn() != null) {
            selectEntity.getTableColumn().getColumn().setDistinct(true);
         }
      }
      // Add the count(*) term to the select entities list
      QueryColumn queryColumn = new QueryColumn();
      queryColumn.setColumnName("*");

      QueryTableColumn tableColumn = new QueryTableColumn();
      tableColumn.setColumn(queryColumn);

      SelectEntity sEntity = new SelectEntity();
      sEntity.setType(SelectEntityType.TABLE_COLUMN);
      sEntity.setFunctionName(StatType.COUNT);
      sEntity.setTableColumn(tableColumn);

      List<SelectEntity> selectEntities = query.getSelectEntities();
      selectEntities.add(0, sEntity);
   }

   /**
    * This method will remove the <i>distinct</i> keyword, if any, and the COUNT statistic applied on the select entity
    * of the query <BR>
    * that was introduced for retrieving more information about the column corresponding to the select entity.
    * 
    * @param query
    *           the modified query
    */
   private void deprocessQuery (Query query) {
      List<SelectEntity> selectEntities = query.getSelectEntities();
      // Remove the COUNT(*) term
      selectEntities.remove(0);
      for (SelectEntity selectEntity : selectEntities) {
         selectEntity.setFunctionName(null);
         if (selectEntity.getTableColumn() != null) {
            selectEntity.getTableColumn().getColumn().setDistinct(false);
         }
      }
   }

   /**
    * Returns a modified query object after suppressing the <i>Group By</i> and <i>Order By</i> sections <BR>
    * and applying the COUNT statistic along with the distinct keyword on the column corresponding to the select entity
    * 
    * @param originalQuery
    *           the original query obtained from the AssetQuery object
    * @return the modified Query object
    */
   private Query getModifiedQuery (Query originalQuery) {
      Query query = suppressGroupByOrderBy(originalQuery);
      logger.debug("Calling Process Query from getModified Query ");
      processQuery(query);
      return query;
   }

   /**
    * Returns the name of the Entity
    * 
    * @param businessAssetTerm
    * @return name of the entity
    */
   public static String getEntityName (BusinessAssetTerm businessAssetTerm) {
      String entityName = null;
      BusinessEntityType entityType = businessAssetTerm.getBusinessTerm().getBusinessEntityTerm()
               .getBusinessEntityType();
      if (BusinessEntityType.CONCEPT.equals(entityType)) {
         Concept entity = (Concept) businessAssetTerm.getBusinessTerm().getBusinessEntityTerm().getBusinessEntity();
         entityName = entity.getName();
      } else if (BusinessEntityType.CONCEPT_LOOKUP_INSTANCE.equals(entityType)) {
         Instance entity = (Instance) businessAssetTerm.getBusinessTerm().getBusinessEntityTerm().getBusinessEntity();
         entityName = entity.getName();
      }
      return entityName;
   }

   /**
    * Returns the display name of the Entity
    * 
    * @param businessAssetTerm
    * @return name of the entity
    */
   public static String getEntityDisplayName (BusinessAssetTerm businessAssetTerm) {
      String entityName = null;
      BusinessEntityType entityType = businessAssetTerm.getBusinessTerm().getBusinessEntityTerm()
               .getBusinessEntityType();
      if (BusinessEntityType.CONCEPT.equals(entityType)) {
         Concept entity = (Concept) businessAssetTerm.getBusinessTerm().getBusinessEntityTerm().getBusinessEntity();
         entityName = entity.getDisplayName();
      } else if (BusinessEntityType.CONCEPT_LOOKUP_INSTANCE.equals(entityType)) {
         Instance entity = (Instance) businessAssetTerm.getBusinessTerm().getBusinessEntityTerm().getBusinessEntity();
         entityName = entity.getDisplayName();
      }
      return entityName;
   }

   /**
    * This is a helper method to check if a selected column of the metrics section exists in the summarization section
    * or not
    */
   public boolean isPresentInSummarizations (ReportMetaInfo reportMetaInfo, ReportColumnInfo reportColumnInfo) {
      boolean flag = false;
      List<BusinessAssetTerm> summarizations = reportMetaInfo.getAssetQuery().getLogicalQuery().getSummarizations();
      if (summarizations != null && summarizations.size() > 0) {
         for (BusinessAssetTerm summarization : summarizations) {
            if (ReportAggregationHelper.getEntityName(summarization).equals(
                     ReportAggregationHelper.getEntityName(reportColumnInfo.getBizAssetTerm()))) {
               flag = true;
               if (summarization.getBusinessTerm().isRequestedByUser()) {
                  reportColumnInfo.setUserRequestedSummarization(flag);
               }
               break;
            }
         }
      }
      return flag;
   }

   /**
    * This method determines if a column is eligible to be treated as a DIMENSION or not, in the context of the query.
    * 
    * @param long
    *           columnUniqueCount the unique count of the column in the query's result set
    * @param long
    *           totalCount the total count of the rows returned by the query
    * @return boolean
    */
   public boolean isEligibleAsDimension (long columnUniqueCount, long totalCount) {
      boolean isDim = false;
      double percentage = (double) columnUniqueCount / (double) totalCount * 100;
      // If the column unique count is less than the system's maximum value, proceed to check against the dimension
      // threshold value.
      // If it is less than or equal to the dimension threshold value then mark as DIMENSION.
      // If it is greater than the dimension threshold value then check for the percent rule.
      // If the column unique count fails then mark as ID else mark as DIMENSION
      if (columnUniqueCount < getCoreConfigurationService().getSystemMaxDimensionValue()) {
         if (logger.isDebugEnabled()) {
            logger.debug("Column count is less than the system max value....");
         }
         if (columnUniqueCount <= getCoreConfigurationService().getDimensionTreshold()) {
            if (logger.isDebugEnabled()) {
               logger.debug("Column count is less than the dimension threshold....");
            }
            isDim = true;
         } else {
            if (logger.isDebugEnabled()) {
               logger.debug("Column count is GT than dimension threshold "
                        + getCoreConfigurationService().getDimensionTreshold());
            }
            if (percentage < getAggregationConfigurationService().getUniqueCountPercent()) {
               if (logger.isDebugEnabled()) {
                  logger.debug("Column count is GT than dimension threshold, but LT allowable percent specified");
               }
               isDim = true;
            } else {
               if (logger.isDebugEnabled()) {
                  logger.debug("Column count % is GT than unique count percent "
                           + getAggregationConfigurationService().getUniqueCountPercent() + "%");
               }
            }
         }
      } else {
         if (logger.isDebugEnabled()) {
            logger.debug("ATTN : Column count is greater the system max value....cannot be a DIMENSION");
         }
      }
      return isDim;
   }

   /**
    * This method checks if the underlying BusinessTerm of the ReportColumnInfo object is a Population term or not
    * 
    * @param reportMetaInfo
    * @param reportColumnInfo
    */
   public boolean isPopulationTerm (ReportMetaInfo reportMetaInfo, ReportColumnInfo reportColumnInfo) {
      boolean isPopulationTerm = false;
      List<BusinessAssetTerm> populations = reportMetaInfo.getAssetQuery().getLogicalQuery().getPopulations();
      if (ExecueCoreUtil.isCollectionNotEmpty(populations) && populations.contains(reportColumnInfo.getBizAssetTerm())) {
         for (BusinessAssetTerm population : populations) {
            if (population.getBusinessTerm().getBusinessEntityTerm().equals(
                     reportColumnInfo.getBizAssetTerm().getBusinessTerm().getBusinessEntityTerm())) {
               isPopulationTerm = true;
               break;
            }
         }
      }
      return isPopulationTerm;
   }

   /**
    * This method will check if the total data points does not exceed the threshold. If it exceeds, then in an iterative
    * manner, the dimension with high number of members will be set as ID, effectively reducing the number of data
    * points
    * 
    * @param reportMetaInfo
    */
   public void handleLargeGroupBy (ReportMetaInfo reportMetaInfo) {
      long memberCount = 1;
      List<ReportColumnInfo> dimensions = new ArrayList<ReportColumnInfo>();
      for (ReportColumnInfo reportColumnInfo : reportMetaInfo.getReportColumns()) {
         // collect all the dimensions - exclude those marked for range derivation or of type RANGE_LOOKUP
         boolean isRangeEntity = reportColumnInfo.isEligibleForRanges()
                  || ColumnType.RANGE_LOOKUP.equals(reportColumnInfo.getColumn().getKdxDataType());
         if (!isRangeEntity) {
            if (ColumnType.DIMENSION.equals(reportColumnInfo.getColumnType())) {
               memberCount *= reportColumnInfo.getCountSize();
               dimensions.add(reportColumnInfo);
            }
         }
      }
      sortDimensionsByMemberCount(dimensions);
      if (ReportSelectionConstants.HIGH_RECORDS_LIMIT <= memberCount) {
         // mark the dimension with max members as ID
         ReportColumnInfo maxMemberDimension = dimensions.get(0);
         maxMemberDimension.modifyColumnType(ColumnType.ID);
         // this column is already in the metrics, COUNT will be added by the Statistics Processor
         // if already present, this column has to be removed from summarizations
         removeFromSummarizations(maxMemberDimension, reportMetaInfo.getAssetQuery().getLogicalQuery()
                  .getSummarizations());
         logger.debug(maxMemberDimension.getColumn().getName()
                  + " : Removed a column from the Summarizations - adjusted the data points count");
         // make a recursive call
         handleLargeGroupBy(reportMetaInfo);
      } else {
         logger.debug("Final number of data points : " + memberCount);
         return;
      }
   }

   /**
    * This method will remove the selected column from the summarizations section if present.
    * 
    * @param maxMemberDimension
    * @param summarizations
    */
   private void removeFromSummarizations (ReportColumnInfo maxMemberDimension, List<BusinessAssetTerm> summarizations) {
      if (ExecueCoreUtil.isCollectionNotEmpty(summarizations)) {
         BusinessAssetTerm match = null;
         for (BusinessAssetTerm summarization : summarizations) {
            if (maxMemberDimension.getBizAssetTerm().getBusinessTerm().getBusinessEntityTerm().equals(
                     summarization.getBusinessTerm().getBusinessEntityTerm())) {
               match = summarization;
               break;
            }
         }
         if (match != null) {
            summarizations.remove(match);
         }
      }
   }

   /**
    * This method generates the title to be displayed in the reports. <BR>
    * Format : [Measures], [DIMENSIONS], [IDS] followed by the list of conditions
    * 
    * @param reportMetaInfo
    */
   public String prepareReportTitle (ReportMetaInfo reportMetaInfo) {
      StringBuilder title = new StringBuilder();
      StructuredLimitClause topBottom = reportMetaInfo.getAssetQuery().getLogicalQuery().getTopBottom();
      if (topBottom != null && !reportMetaInfo.isGenerateDetailReport()) {
         String type = "First";
         if (OrderLimitEntityType.BOTTOM.equals(topBottom.getLimitType())) {
            type = "Last";
         }
         title.append(type).append(SPACE);
         title.append(topBottom.getLimitValue());
         title.append(OF_WITH_SPACES).append(getTermLabel(topBottom.getBusinessAssetTerm()));
      } else {
         // metrics
         List<String> measures = new ArrayList<String>();
         List<String> dims = new ArrayList<String>();
         List<String> ids = new ArrayList<String>();
         // conditions
         List<StructuredCondition> structuredConditions = reportMetaInfo.getAssetQuery().getLogicalQuery()
                  .getConditions();
         for (ReportColumnInfo reportColumnInfo : reportMetaInfo.getReportColumns()) {
            // if (reportColumnInfo.getBizAssetTerm().getBusinessTerm().isRequestedByUser()) {
            String termName = getTermLabel(reportColumnInfo.getBizAssetTerm());
            ColumnType columnType = reportColumnInfo.getColumnType();
            switch (columnType) {
               case DIMENSION:
               case SIMPLE_LOOKUP:
               case RANGE_LOOKUP:
                  dims.add(termName);
                  break;
               case ID:
                  ids.add(termName);
                  break;
               case MEASURE:
                  measures.add(termName);
                  break;
            }
            // }
         }
         // prepare string for terms
         int index = 0;
         boolean areUserRequestedMetricsPresent = measures.size() > 0 || dims.size() > 0 || ids.size() > 0;
         if (areUserRequestedMetricsPresent) {
            for (String measure : measures) {
               if (index++ != 0) {
                  title.append(COMMA).append(SPACE);
               }
               title.append(measure);
            }
            for (String dim : dims) {
               if (index++ != 0) {
                  title.append(COMMA).append(SPACE);
               }
               title.append(dim);
            }
            for (String id : ids) {
               if (index++ != 0) {
                  title.append(COMMA).append(SPACE);
               }
               title.append(id);
            }
         } else {
            // there are no user requested terms in the metrics
            // try to add the terms appearing in the conditions into the metrics section
            // for (StructuredCondition condition : structuredConditions) {
            for (ReportColumnInfo reportColumnInfo : reportMetaInfo.getReportColumns()) {
               // if (condition.getLhsBusinessAssetTerm().getBusinessTerm().getBusinessEntityTerm().equals(
               // reportColumnInfo.getBizAssetTerm().getBusinessTerm().getBusinessEntityTerm())
               // && condition.getLhsBusinessAssetTerm().getBusinessTerm().isRequestedByUser()) {
               // if (condition.getLhsBusinessAssetTerm().getBusinessTerm().getBusinessEntityTerm().equals(
               // reportColumnInfo.getBizAssetTerm().getBusinessTerm().getBusinessEntityTerm())) {

               String termName = getTermLabel(reportColumnInfo.getBizAssetTerm());
               if (index++ != 0) {
                  title.append(COMMA).append(SPACE);
               }
               title.append(termName);
            }
            // }
            // }
         }
         // conditions
         List<String> conditions = new ArrayList<String>();
         if (ExecueCoreUtil.isCollectionNotEmpty(structuredConditions)) {
            for (StructuredCondition structuredCondition : structuredConditions) {
               if (structuredCondition.isSubCondition()) {
                  StringBuilder stringBuilder = new StringBuilder();
                  int currIndex = 0;
                  for (StructuredCondition subCondition : structuredCondition.getSubConditions()) {
                     stringBuilder.append(getConditionString(subCondition));
                     if (currIndex != structuredCondition.getSubConditions().size() - 1) {
                        stringBuilder.append(SPACE);
                        stringBuilder.append(OR);
                        stringBuilder.append(SPACE);
                     }
                     currIndex++;
                  }
                  conditions.add(stringBuilder.toString());
               } else {
                  conditions.add(getConditionString(structuredCondition));
               }
            }
         }
         // prepare string for conditions
         index = 0;
         if (ExecueCoreUtil.isCollectionNotEmpty(conditions)) {
            title.append(SPACE).append(WHERE).append(SPACE);
            for (String condition : conditions) {
               if (index++ != 0) {
                  title.append(SPACE).append(AND).append(SPACE);
               }
               title.append(condition);
            }
         }
      }
      logger.debug("REPORT TITLE : " + title.toString());
      return title.toString();
   }

   private String getConditionString (StructuredCondition structuredCondition) {
      StringBuilder conditionString = new StringBuilder();
      String lhsTerm = getTermLabel(structuredCondition.getLhsBusinessAssetTerm());
      String lhsStat = "";
      OperatorType operatorType = structuredCondition.getOperator();
      if (structuredCondition.getLhsBusinessAssetTerm().getBusinessTerm().getBusinessStat() != null) {
         lhsStat = structuredCondition.getLhsBusinessAssetTerm().getBusinessTerm().getBusinessStat().getStat()
                  .getDisplayName();
      }
      if (!StringUtils.isEmpty(lhsStat)) {
         conditionString.append(lhsStat).append(OF_WITH_SPACES);
      }
      conditionString.append(lhsTerm).append(SPACE);

      // process RHS
      List<String> values = new ArrayList<String>();
      if (structuredCondition.getRhsStructuredQuery() != null) {
         // TODO: JVK handle sub query
         logger.debug("Yet to handle sub query in RHS");
      }
      if (structuredCondition.getRhsBusinessAssetTerms() != null) {
         for (BusinessAssetTerm rhsBusinessAssetTerm : structuredCondition.getRhsBusinessAssetTerms()) {
            values.add(getEntityDisplayName(rhsBusinessAssetTerm));
         }
      } else if (structuredCondition.getRhsValues() != null) {
         for (QueryValue value : structuredCondition.getRhsValues()) {
            values.add(getConvertedValue(structuredCondition, value));
         }
      }
      conditionString.append(statementForOperatorAndRHS(operatorType, values));
      return conditionString.toString();
   }

   // TODO: JVK need to handle other conversions - currently handling only CURRENCY and DATE
   private String getConvertedValue (StructuredCondition structuredCondition, QueryValue value) {
      StringBuilder convertedValue = new StringBuilder();
      convertedValue.append(value.getValue());
      boolean hasUnit = false;
      if (ExecueCoreUtil.isNotEmpty(structuredCondition.getTargetConversionUnit())) {
         hasUnit = true;
      }
      if (ExecueCoreUtil.isNotEmpty(value.getTargetConversionFormat())) {
         if (hasUnit) {
            String unit = StringUtils.capitalize(structuredCondition.getTargetConversionUnit().toLowerCase());
            String format = StringUtils.capitalize(value.getTargetConversionFormat().toLowerCase());
            convertedValue.append(SPACE).append(format).append(OF_WITH_SPACES).append(unit);
         } else {
            // -JM- 13-SEP-2010 : Escape if the conversion format results in 'ONE' - append other format strings
            String targetConversionFormat = value.getTargetConversionFormat();
            if (!"ONE".equalsIgnoreCase(targetConversionFormat)) {
               convertedValue.append(SPACE).append(OPEN_BRACKET).append(value.getTargetConversionFormat()).append(
                        CLOSE_BRACKET);
            }
         }
      }
      return convertedValue.toString();
   }

   public ReportMetaInfo cloneReportMetaInfo (ReportMetaInfo reportMetaInfo, boolean isDetailedReport) {
      // clone the AssetQuery
      AssetQuery clonedAssetQuery = cloneAssetQuery(reportMetaInfo.getAssetQuery(), isDetailedReport);
      clonedAssetQuery.setUserQueryId(reportMetaInfo.getAssetQuery().getUserQueryId());

      // clone the Report Column Info objects
      List<BusinessAssetTerm> clonedMetrics = clonedAssetQuery.getLogicalQuery().getMetrics();
      Map<BusinessAssetTerm, ReportColumnInfo> metricsMap = getReportColumnInfoByBATMap(reportMetaInfo, clonedMetrics);
      List<ReportColumnInfo> clonedReportColumns = new ArrayList<ReportColumnInfo>(reportMetaInfo.getReportColumns()
               .size());
      // create new RCI objects using the cloned BATs of the metrics section of the cloned SQ
      for (BusinessAssetTerm clonedMetric : clonedMetrics) {
         ReportColumnInfo clonedReportColumnInfo = prepareReportColumn(clonedMetric);
         ReportColumnInfo originalReportColumnInfo = metricsMap.get(clonedMetric);
         populateClonedReportColumnInfo(clonedReportColumnInfo, originalReportColumnInfo);
         clonedReportColumns.add(clonedReportColumnInfo);
      }

      ReportMetaInfo clonedReportMetaInfo = new ReportMetaInfo(clonedAssetQuery);
      clonedReportMetaInfo.setReportColumns(clonedReportColumns);
      clonedReportMetaInfo.setReportMetaHierarchyInfo(reportMetaInfo.getReportMetaHierarchyInfo());
      clonedReportMetaInfo.setSummaryPathType(reportMetaInfo.getSummaryPathType());
      clonedReportMetaInfo.setUniqueCounts(reportMetaInfo.hasUniqueCounts());
      clonedReportMetaInfo.setMinMaxValues(reportMetaInfo.hasMinMaxValues());
      clonedReportMetaInfo.setTotalCount(reportMetaInfo.getTotalCount());
      clonedReportMetaInfo.setUnivariants(reportMetaInfo.isUnivariants());
      clonedReportMetaInfo.setGenerateBusinessSummary(reportMetaInfo.isGenerateBusinessSummary());
      clonedReportMetaInfo.setGenerateDetailReport(reportMetaInfo.isGenerateDetailReport());
      clonedReportMetaInfo.setGenerateHierarchyReport(reportMetaInfo.isGenerateHierarchyReport());
      clonedReportMetaInfo.setGenerateOnlyDataBrowser(reportMetaInfo.isGenerateOnlyDataBrowser());
      clonedReportMetaInfo.setGenerateMultipleDetailReports(reportMetaInfo.isGenerateMultipleDetailReports());
      clonedReportMetaInfo.setFinalQuery(reportMetaInfo.getFinalQuery());
      clonedReportMetaInfo.setProfilePresent(reportMetaInfo.isProfilePresent());

      return clonedReportMetaInfo;
   }

   /**
    * @param reportMetaInfo
    * @param clonedMetrics
    * @return
    */
   public static Map<BusinessAssetTerm, ReportColumnInfo> getReportColumnInfoByBATMap (ReportMetaInfo reportMetaInfo,
            List<BusinessAssetTerm> clonedMetrics) {
      // prepare a map for connecting the metric with its corresponding Report Column Info object
      Map<BusinessAssetTerm, ReportColumnInfo> metricsMap = new HashMap<BusinessAssetTerm, ReportColumnInfo>();
      for (BusinessAssetTerm clonedMetric : clonedMetrics) {
         Long bedId = clonedMetric.getBusinessTerm().getBusinessEntityTerm().getBusinessEntityDefinitionId();
         for (ReportColumnInfo colInfo : reportMetaInfo.getReportColumns()) {
            Long colInfoBedId = colInfo.getBizAssetTerm().getBusinessTerm().getBusinessEntityTerm()
                     .getBusinessEntityDefinitionId();
            if (bedId == colInfoBedId) {
               metricsMap.put(clonedMetric, colInfo);
               break;
            }
         }
      }
      return metricsMap;
   }

   private void populateClonedReportColumnInfo (ReportColumnInfo clonedReportColumnInfo,
            ReportColumnInfo toBeClonedReportColumnInfo) {
      clonedReportColumnInfo.setBusinessStats(toBeClonedReportColumnInfo.getBusinessStats());
      clonedReportColumnInfo.setColumnTypeDeduced(toBeClonedReportColumnInfo.isColumnTypeDeduced());
      if (toBeClonedReportColumnInfo.isColumnTypeDeduced()) {
         clonedReportColumnInfo.modifyColumnType(toBeClonedReportColumnInfo.getColumnType());
      }
      clonedReportColumnInfo.setDetailReportColumnTypeDeduced(toBeClonedReportColumnInfo
               .isDetailReportColumnTypeDeduced());
      if (toBeClonedReportColumnInfo.isDetailReportColumnTypeDeduced()) {
         clonedReportColumnInfo.modifyDetailReportColumnType(toBeClonedReportColumnInfo.getDetailReportColumnType());
      }
      clonedReportColumnInfo.setCountRequired(toBeClonedReportColumnInfo.isCountRequired());
      clonedReportColumnInfo.setCountSize(toBeClonedReportColumnInfo.getCountSize());
      clonedReportColumnInfo.setEligibleForRanges(toBeClonedReportColumnInfo.isEligibleForRanges());
      clonedReportColumnInfo.setMarkedForRangeDerivation(toBeClonedReportColumnInfo.isMarkedForRangeDerivation());
      clonedReportColumnInfo.setMaximumValue(toBeClonedReportColumnInfo.getMaximumValue());
      clonedReportColumnInfo.setMinimumValue(toBeClonedReportColumnInfo.getMinimumValue());
      clonedReportColumnInfo.setStatsDeduced(toBeClonedReportColumnInfo.areStatsDeduced());
      clonedReportColumnInfo.setUnivariantValue(toBeClonedReportColumnInfo.getUnivariantValue());
      clonedReportColumnInfo.setUserRequestedSummarization(toBeClonedReportColumnInfo.isUserRequestedSummarization());
   }

   private AssetQuery cloneAssetQuery (AssetQuery assetQuery, boolean isDetailedReport) {
      AssetQuery clonedAssetQuery = new AssetQuery();
      clonedAssetQuery.setPhysicalQuery(null);
      clonedAssetQuery.setLogicalQuery(cloneStructuredQuery(assetQuery.getLogicalQuery(), isDetailedReport));
      clonedAssetQuery.setUserQueryId(assetQuery.getUserQueryId());
      return clonedAssetQuery;
   }

   public AggregateQuery cloneAggregateQuery (AggregateQuery toBeClonedAggregateQuery, boolean isDetailedReport) {
      AggregateQuery clonedAggregateQuery = new AggregateQuery();
      clonedAggregateQuery.setAssetQuery(cloneAssetQuery(toBeClonedAggregateQuery.getAssetQuery(), isDetailedReport));
      clonedAggregateQuery.setDataExtracted(toBeClonedAggregateQuery.isDataExtracted());
      clonedAggregateQuery.setDataPresent(toBeClonedAggregateQuery.isDataPresent());
      clonedAggregateQuery.setQueryExecutionTime(toBeClonedAggregateQuery.getQueryExecutionTime());
      clonedAggregateQuery.setQueryRepresentation(toBeClonedAggregateQuery.getQueryRepresentation());
      clonedAggregateQuery.setReportMetaInfoStructure(toBeClonedAggregateQuery.getReportMetaInfoStructure());
      clonedAggregateQuery.setReportTypes(toBeClonedAggregateQuery.getReportTypes());
      clonedAggregateQuery.setType(toBeClonedAggregateQuery.getType());
      clonedAggregateQuery.setXmlData(toBeClonedAggregateQuery.getXmlData());

      return clonedAggregateQuery;
   }

   private StructuredQuery cloneStructuredQueryForDetailReport (StructuredQuery logicalQuery) {
      StructuredQuery clonedStructuredQuery = new StructuredQuery();
      clonedStructuredQuery.setAssetWeight(logicalQuery.getAssetWeight());
      clonedStructuredQuery.setRelevance(logicalQuery.getRelevance());
      clonedStructuredQuery.setAsset(logicalQuery.getAsset());
      clonedStructuredQuery.setStructuredQueryWeight(logicalQuery.getStructuredQueryWeight());
      clonedStructuredQuery.setRollupQuery(logicalQuery.isRollupQuery());
      if (logicalQuery.getScalingFactor() != null) {
         clonedStructuredQuery.setScalingFactor(cloneBATForDetailReport(logicalQuery.getScalingFactor()));
      }
      if (logicalQuery.getCohort() != null) {
         clonedStructuredQuery.setCohort(cloneStructuredQueryForDetailReport(logicalQuery.getCohort()));
      }
      if (ExecueCoreUtil.isCollectionNotEmpty(logicalQuery.getMetrics())) {
         List<BusinessAssetTerm> clonedMetrics = new ArrayList<BusinessAssetTerm>();
         for (BusinessAssetTerm metric : logicalQuery.getMetrics()) {
            clonedMetrics.add(cloneBATForDetailReport(metric));
         }
         clonedStructuredQuery.setMetrics(clonedMetrics);
      }
      if (ExecueCoreUtil.isCollectionNotEmpty(logicalQuery.getConditions())) {
         List<StructuredCondition> clonedConditions = new ArrayList<StructuredCondition>();
         for (StructuredCondition condition : logicalQuery.getConditions()) {
            if (condition.isSubCondition()) {
               List<StructuredCondition> clonedSubConditions = new ArrayList<StructuredCondition>();
               for (StructuredCondition subCondition : condition.getSubConditions()) {
                  clonedSubConditions.add(cloneStructuredConditionForDetailReport(subCondition));
               }
               StructuredCondition clonedStructuredCondition = new StructuredCondition();
               clonedStructuredCondition.setSubCondition(true);
               clonedStructuredCondition.setSubConditions(clonedSubConditions);
               clonedConditions.add(clonedStructuredCondition);
            } else {
               clonedConditions.add(cloneStructuredConditionForDetailReport(condition));
            }
         }
         clonedStructuredQuery.setConditions(clonedConditions);
      }
      if (ExecueCoreUtil.isCollectionNotEmpty(logicalQuery.getPopulations())) {
         List<BusinessAssetTerm> clonedPopulations = new ArrayList<BusinessAssetTerm>();
         for (BusinessAssetTerm population : logicalQuery.getPopulations()) {
            clonedPopulations.add(cloneBATForDetailReport(population));
         }
         clonedStructuredQuery.setPopulations(clonedPopulations);
      }
      // if (ExecueCoreUtil.isCollectionNotEmpty(logicalQuery.getSummarizations())) {
      // List<BusinessAssetTerm> clonedSummarizations = new ArrayList<BusinessAssetTerm>();
      // for (BusinessAssetTerm summarization : logicalQuery.getSummarizations()) {
      // clonedSummarizations.add(cloneBATForDetailReport(summarization));
      // }
      // clonedStructuredQuery.setSummarizations(clonedSummarizations);
      // }
      // if (ExecueCoreUtil.isCollectionNotEmpty(logicalQuery.getOrderClauses())) {
      // List<StructuredOrderClause> clonedOrderBys = new ArrayList<StructuredOrderClause>();
      // for (StructuredOrderClause orderBy : logicalQuery.getOrderClauses()) {
      // StructuredOrderClause clonedOrderBy = new StructuredOrderClause();
      // clonedOrderBy.setBusinessAssetTerm(cloneBATForDetailReport(orderBy.getBusinessAssetTerm()));
      // clonedOrderBy.setOrderEntityType(orderBy.getOrderEntityType());
      // clonedOrderBys.add(clonedOrderBy);
      // }
      // clonedStructuredQuery.setOrderClauses(clonedOrderBys);
      // }
      if (logicalQuery.getTopBottom() != null) {
         clonedStructuredQuery.setTopBottom(cloneLimitClauseForDetailReport(logicalQuery.getTopBottom()));
      }
      return clonedStructuredQuery;
   }

   private StructuredQuery cloneStructuredQuery (StructuredQuery logicalQuery, boolean isDetailedReport) {
      StructuredQuery clonedStructuredQuery = new StructuredQuery();
      clonedStructuredQuery.setAsset(logicalQuery.getAsset());

      if (logicalQuery.getScalingFactor() != null) {
         clonedStructuredQuery.setScalingFactor(cloneBusinessAssetTerm(logicalQuery.getScalingFactor(),
                  isDetailedReport));
      }

      clonedStructuredQuery.setStructuredQueryWeight(logicalQuery.getStructuredQueryWeight());
      clonedStructuredQuery.setRelativePriority(logicalQuery.getRelativePriority());
      clonedStructuredQuery.setModelId(logicalQuery.getModelId());
      clonedStructuredQuery.setAssetAEDId(logicalQuery.getAssetAEDId());
      clonedStructuredQuery.setAssetWeight(logicalQuery.getAssetWeight());
      clonedStructuredQuery.setStandarizedApplicationWeight(logicalQuery.getStandarizedApplicationWeight());
      clonedStructuredQuery.setStandarizedPossiblityWeight(logicalQuery.getStandarizedPossiblityWeight());
      clonedStructuredQuery.setStandarizedAssetWeight(logicalQuery.getStandarizedAssetWeight());
      clonedStructuredQuery.setRelevance(logicalQuery.getRelevance());
      clonedStructuredQuery.setRollupQuery(logicalQuery.isRollupQuery());

      clonedStructuredQuery.setMetrics(new ArrayList<BusinessAssetTerm>());
      clonedStructuredQuery.setConditions(new ArrayList<StructuredCondition>());
      clonedStructuredQuery.setSummarizations(new ArrayList<BusinessAssetTerm>());
      clonedStructuredQuery.setOrderClauses(new ArrayList<StructuredOrderClause>());
      clonedStructuredQuery.setHavingClauses(new ArrayList<StructuredCondition>());
      clonedStructuredQuery.setPopulations(new ArrayList<BusinessAssetTerm>());
      clonedStructuredQuery.setHierarchies(new ArrayList<HierarchyTerm>());

      if (ExecueCoreUtil.isCollectionNotEmpty(logicalQuery.getMetrics())) {
         List<BusinessAssetTerm> clonedMetrics = new ArrayList<BusinessAssetTerm>();
         for (BusinessAssetTerm metric : logicalQuery.getMetrics()) {
            clonedMetrics.add(cloneBusinessAssetTerm(metric, isDetailedReport));
         }
         clonedStructuredQuery.setMetrics(clonedMetrics);
      }
      if (ExecueCoreUtil.isCollectionNotEmpty(logicalQuery.getConditions())) {
         List<StructuredCondition> clonedConditions = new ArrayList<StructuredCondition>();
         for (StructuredCondition condition : logicalQuery.getConditions()) {
            if (condition.isSubCondition()) {
               List<StructuredCondition> clonedSubConditions = new ArrayList<StructuredCondition>();
               for (StructuredCondition subCondition : condition.getSubConditions()) {
                  clonedSubConditions.add(cloneStructuredCondition(subCondition, isDetailedReport));
               }
               StructuredCondition clonedStructuredCondition = new StructuredCondition();
               clonedStructuredCondition.setSubCondition(true);
               clonedStructuredCondition.setSubConditions(clonedSubConditions);
               clonedConditions.add(clonedStructuredCondition);
            } else {
               clonedConditions.add(cloneStructuredCondition(condition, isDetailedReport));
            }
         }
         clonedStructuredQuery.setConditions(clonedConditions);
      }
      if (ExecueCoreUtil.isCollectionNotEmpty(logicalQuery.getPopulations())) {
         List<BusinessAssetTerm> clonedPopulations = new ArrayList<BusinessAssetTerm>();
         for (BusinessAssetTerm population : logicalQuery.getPopulations()) {
            clonedPopulations.add(cloneBusinessAssetTerm(population, isDetailedReport));
         }
         clonedStructuredQuery.setPopulations(clonedPopulations);
      }

      // Clone summarizations and order by only if it is not detailed report
      // For detailed report we do need to nullify the summarizations and order by's
      if (!isDetailedReport) {
         if (ExecueCoreUtil.isCollectionNotEmpty(logicalQuery.getSummarizations())) {
            List<BusinessAssetTerm> clonedSummarizations = new ArrayList<BusinessAssetTerm>();
            for (BusinessAssetTerm summarization : logicalQuery.getSummarizations()) {
               clonedSummarizations.add(cloneBusinessAssetTerm(summarization, isDetailedReport));
            }
            clonedStructuredQuery.setSummarizations(clonedSummarizations);
         }

         if (ExecueCoreUtil.isCollectionNotEmpty(logicalQuery.getHavingClauses())) {
            List<StructuredCondition> clonedHavingClauses = new ArrayList<StructuredCondition>();
            for (StructuredCondition havingClause : logicalQuery.getHavingClauses()) {
               clonedHavingClauses.add(cloneStructuredCondition(havingClause, isDetailedReport));
            }
            clonedStructuredQuery.setHavingClauses(clonedHavingClauses);
         }

         if (ExecueCoreUtil.isCollectionNotEmpty(logicalQuery.getOrderClauses())) {
            List<StructuredOrderClause> clonedOrderClauses = new ArrayList<StructuredOrderClause>();
            for (StructuredOrderClause orderClause : logicalQuery.getOrderClauses()) {
               clonedOrderClauses.add(cloneOrderClause(orderClause, isDetailedReport));
            }
            clonedStructuredQuery.setOrderClauses(clonedOrderClauses);
         }
      }

      // Clone the top bottom
      if (logicalQuery.getTopBottom() != null) {
         clonedStructuredQuery.setTopBottom(cloneLimitClause(logicalQuery.getTopBottom(), isDetailedReport));
      }

      // Clone the cohort
      if (logicalQuery.getCohort() != null) {
         clonedStructuredQuery.setCohort(cloneStructuredQuery(logicalQuery.getCohort(), isDetailedReport));
      }

      return clonedStructuredQuery;
   }

   private StructuredLimitClause cloneLimitClauseForDetailReport (StructuredLimitClause topBottom) {
      StructuredLimitClause clonedTopBottom = new StructuredLimitClause();
      clonedTopBottom.setBusinessAssetTerm(cloneBATForDetailReport(topBottom.getBusinessAssetTerm()));
      clonedTopBottom.setLimitType(topBottom.getLimitType());
      clonedTopBottom.setLimitValue(topBottom.getLimitValue());
      return clonedTopBottom;
   }

   private StructuredLimitClause cloneLimitClause (StructuredLimitClause topBottom, boolean isDetailedReport) {
      StructuredLimitClause clonedTopBottom = new StructuredLimitClause();
      clonedTopBottom.setBusinessAssetTerm(cloneBusinessAssetTerm(topBottom.getBusinessAssetTerm(), isDetailedReport));
      clonedTopBottom.setLimitType(topBottom.getLimitType());
      clonedTopBottom.setLimitValue(topBottom.getLimitValue());
      return clonedTopBottom;
   }

   private StructuredCondition cloneStructuredConditionForDetailReport (StructuredCondition condition) {
      StructuredCondition clonedCondition = new StructuredCondition();
      clonedCondition.setLhsBusinessAssetTerm(cloneBATForDetailReport(condition.getLhsBusinessAssetTerm()));
      clonedCondition.setOperandType(condition.getOperandType());
      clonedCondition.setOperator(condition.getOperator());
      clonedCondition.setConversionId(condition.getConversionId());
      clonedCondition.setOriginalLhsBusinessTerm(condition.getOriginalLhsBusinessTerm());
      clonedCondition.setSubCondition(condition.isSubCondition());
      clonedCondition.setTargetConversionUnit(condition.getTargetConversionUnit());
      if (ExecueCoreUtil.isCollectionNotEmpty(condition.getRhsBusinessAssetTerms())) {
         List<BusinessAssetTerm> rhsBusinessAssetTerms = new ArrayList<BusinessAssetTerm>();
         for (BusinessAssetTerm bat : condition.getRhsBusinessAssetTerms()) {
            BusinessAssetTerm rhsBusinessAssetTerm = cloneBATForDetailReport(bat);
            rhsBusinessAssetTerms.add(rhsBusinessAssetTerm);
         }
         clonedCondition.setRhsBusinessAssetTerms(rhsBusinessAssetTerms);
      } else if (condition.getRhsStructuredQuery() != null) {
         clonedCondition.setRhsStructuredQuery(cloneStructuredQueryForDetailReport(condition.getRhsStructuredQuery()));
      } else if (ExecueCoreUtil.isCollectionNotEmpty(condition.getRhsValues())) {
         clonedCondition.setRhsValues(condition.getRhsValues());
      }
      return clonedCondition;
   }

   private StructuredCondition cloneStructuredCondition (StructuredCondition condition, boolean isDetailedReport) {
      StructuredCondition clonedCondition = new StructuredCondition();
      clonedCondition.setLhsBusinessAssetTerm(cloneBusinessAssetTerm(condition.getLhsBusinessAssetTerm(),
               isDetailedReport));
      clonedCondition.setOperandType(condition.getOperandType());
      clonedCondition.setOperator(condition.getOperator());
      clonedCondition.setConversionId(condition.getConversionId());
      clonedCondition.setOriginalLhsBusinessTerm(condition.getOriginalLhsBusinessTerm());
      clonedCondition.setSubCondition(condition.isSubCondition());
      clonedCondition.setTargetConversionUnit(condition.getTargetConversionUnit());

      if (ExecueCoreUtil.isCollectionNotEmpty(condition.getRhsBusinessAssetTerms())) {
         List<BusinessAssetTerm> rhsBusinessAssetTerms = new ArrayList<BusinessAssetTerm>();
         for (BusinessAssetTerm bat : condition.getRhsBusinessAssetTerms()) {
            BusinessAssetTerm rhsBusinessAssetTerm = cloneBusinessAssetTerm(bat, isDetailedReport);
            rhsBusinessAssetTerms.add(rhsBusinessAssetTerm);
         }
         clonedCondition.setRhsBusinessAssetTerms(rhsBusinessAssetTerms);
      } else if (condition.getRhsStructuredQuery() != null) {
         clonedCondition
                  .setRhsStructuredQuery(cloneStructuredQuery(condition.getRhsStructuredQuery(), isDetailedReport));
      } else if (ExecueCoreUtil.isCollectionNotEmpty(condition.getRhsValues())) {
         clonedCondition.setRhsValues(condition.getRhsValues());
      }
      return clonedCondition;
   }

   private BusinessAssetTerm cloneBATForDetailReport (BusinessAssetTerm bat) {
      BusinessTerm clonedBT = ExecueBeanCloneUtil.cloneBusinessTerm(bat.getBusinessTerm());
      AssetEntityTerm clonedAET = ExecueBeanCloneUtil.cloneAssetEntityTerm(bat.getAssetEntityTerm());

      // remove the stats, ranges from the cloned BT
      clonedBT.setBusinessStat(null);
      clonedBT.setRange(null);

      BusinessAssetTerm clonedBAT = new BusinessAssetTerm();
      clonedBAT.setBusinessTerm(clonedBT);
      clonedBAT.setAssetEntityTerm(clonedAET);

      return clonedBAT;
   }

   private BusinessAssetTerm cloneBusinessAssetTerm (BusinessAssetTerm bat, boolean isDetailedReport) {
      BusinessTerm clonedBT = ExecueBeanCloneUtil.cloneBusinessTerm(bat.getBusinessTerm());
      AssetEntityTerm clonedAET = cloneAssetEntityTerm(bat.getAssetEntityTerm());

      // remove the stats, ranges from the cloned BT if it is for detailed reoport
      if (isDetailedReport) {
         clonedBT.setBusinessStat(null);
         clonedBT.setRange(null);
      }

      BusinessAssetTerm clonedBAT = new BusinessAssetTerm();
      clonedBAT.setBusinessTerm(clonedBT);
      clonedBAT.setAssetEntityTerm(clonedAET);
      return clonedBAT;
   }

   private AssetEntityTerm cloneAssetEntityTerm (AssetEntityTerm term) {
      AssetEntityTerm clonedAET = new AssetEntityTerm();
      clonedAET.setAssetEntityType(term.getAssetEntityType());
      clonedAET.setAssetEntityDefinitionId(new Long(term.getAssetEntityDefinitionId().longValue()));
      if (AssetEntityType.COLUMN.equals(term.getAssetEntityType())) {
         Colum col = (Colum) term.getAssetEntity();
         Colum clonedCol = ExecueBeanCloneUtil.cloneColum(col, true);
         clonedAET.setAssetEntity(clonedCol);
      } else if (AssetEntityType.MEMBER.equals(term.getAssetEntityType())) {
         Membr member = (Membr) term.getAssetEntity();
         Membr clonedMember = ExecueBeanCloneUtil.cloneMember(member);
         clonedAET.setAssetEntity(clonedMember);
      }
      return clonedAET;
   }

   private StructuredOrderClause cloneOrderClause (StructuredOrderClause orderClause, boolean isDetailedReport) {
      StructuredOrderClause clonedOrderClause = new StructuredOrderClause();
      clonedOrderClause.setBusinessAssetTerm(cloneBusinessAssetTerm(orderClause.getBusinessAssetTerm(),
               isDetailedReport));
      clonedOrderClause.setOrderEntityType(orderClause.getOrderEntityType());
      return clonedOrderClause;
   }

   private String statementForOperatorAndRHS (OperatorType operatorType, List<String> values) {
      StringBuffer sbOperatorAndRHS = new StringBuffer();
      switch (operatorType) {
         case BETWEEN:
            sbOperatorAndRHS.append(BETWEEN);
            sbOperatorAndRHS.append(SPACE).append(values.get(0));
            sbOperatorAndRHS.append(SPACE).append(AND);
            sbOperatorAndRHS.append(SPACE).append(values.get(1));
            break;
         case EQUALS:
            sbOperatorAndRHS.append(EQUALS);
            sbOperatorAndRHS.append(SPACE).append(values.get(0));
            break;
         case NOT_EQUALS:
            sbOperatorAndRHS.append(NOT_EQUALS);
            sbOperatorAndRHS.append(SPACE).append(values.get(0));
            break;
         case GREATER_THAN:
            sbOperatorAndRHS.append(GREATER_THAN);
            sbOperatorAndRHS.append(SPACE).append(values.get(0));
            break;
         case LESS_THAN:
            sbOperatorAndRHS.append(LESS_THAN);
            sbOperatorAndRHS.append(SPACE).append(values.get(0));
            break;
         case IN:
            sbOperatorAndRHS.append(IN);
            sbOperatorAndRHS.append(SPACE).append(OPEN_BRACKET);
            int index = 0;
            for (String value : values) {
               if (index++ > 0) {
                  sbOperatorAndRHS.append(COMMA).append(SPACE);
               }
               sbOperatorAndRHS.append(value);
            }
            sbOperatorAndRHS.append(CLOSE_BRACKET);
            break;
         case IS_NULL:
            sbOperatorAndRHS.append(IS_NULL);
            break;
         case IS_NOT_NULL:
            sbOperatorAndRHS.append(IS_NOT_NULL);
            break;
         default:
            break;
      }
      return sbOperatorAndRHS.toString();
   }

   private String getTermLabel (BusinessAssetTerm term) {
      StringBuilder label = new StringBuilder();
      Concept concept = (Concept) term.getBusinessTerm().getBusinessEntityTerm().getBusinessEntity();
      label.append(concept.getDisplayName());
      if (term.getBusinessTerm().isFromCohort()) {
         label.append(SPACE).append(COHORT_INDICATOR);
      }
      return label.toString();
   }

   public AggregateQuery getBusinessAggregateQuery (ReportMetaInfo reportMetaInfo)
            throws ReportQueryGenerationException {
      // Step 1. Get the list of measures and apply statistic(s) on each measure.
      // You may have more than one statistic on a single measure
      // if your logical query has sales as one measure then this column will become AVG(sales)
      // Step 2. Get the list of dimensions, use this list to generate
      // group by and order by clauses
      // Step 3. Create a new aggregated query and set all the selects, group by and order by
      // You may initialize this aggregate query with the structured query you obtained from AssetBusinessQuery
      // You are writing this for a typical DW where the record level data

      // FROM and WHERE clauses are not derived in the Aggregation process and hence the Query object need not be
      // modified for these sections
      try {

         // Modify the structured query for multiple stats and missing stats on measures and id column. Also rearrange
         // selects, group by and order by
         modifyStructuredQuery(reportMetaInfo);

         AssetQuery assetQuery = reportMetaInfo.getAssetQuery();

         // Initialize the AggregateQuery object and set the modified final structured query
         AggregateQuery aggregateQuery = new AggregateQuery();
         aggregateQuery.setType(AggregateQueryType.BUSINESS_SUMMARY);
         aggregateQuery.setAssetQuery(assetQuery);
         return aggregateQuery;
      } catch (Exception exception) {
         throw new ReportQueryGenerationException(4000, "Error while generating the BusinessReport", exception
                  .getCause());
      }
   }

   /**
    * Modify the structured query for multiple stats and missing stats on measures and id column. Also rearrange
    * selects, group by and order by
    * 
    * @param reportMetaInfo
    */
   private void modifyStructuredQuery (ReportMetaInfo reportMetaInfo) {
      AssetQuery assetQuery = reportMetaInfo.getAssetQuery();
      StructuredQuery structuredQuery = assetQuery.getLogicalQuery();
      List<BusinessAssetTerm> metrics = structuredQuery.getMetrics();
      List<ReportColumnInfo> ids = new ArrayList<ReportColumnInfo>();
      List<ReportColumnInfo> measures = new ArrayList<ReportColumnInfo>();
      List<ReportColumnInfo> dimensions = new ArrayList<ReportColumnInfo>();

      // Step 1. Segregate the columns based on their type
      for (ReportColumnInfo reportColumnInfo : reportMetaInfo.getReportColumns()) {
         ColumnType columnType = reportColumnInfo.getColumnType();
         switch (columnType) {
            case ID:
               ids.add(reportColumnInfo);
               break;
            case MEASURE:
               measures.add(reportColumnInfo);
               break;
            case DIMENSION:
               dimensions.add(reportColumnInfo);
               break;
            default:
               logger.debug("It should not reach here!! [" + reportColumnInfo.getColumn().getName() + "-" + columnType
                        + "]");
               break;
         }
      }
      // Step 2. Process the MEASURE columns to apply the statistics only if the statistics are deduced, else the
      // user defined statistics are already in the StructuredQuery
      List<BusinessAssetTerm> removals = new ArrayList<BusinessAssetTerm>();
      for (ReportColumnInfo reportColumnInfo : measures) {
         logger.debug("Current column : " + reportColumnInfo.getColumn().getName());
         if (reportColumnInfo.areStatsDeduced()) {
            for (BusinessStat businessStat : reportColumnInfo.getBusinessStats()) {

               BusinessEntityTerm existingBusinessEntityTerm = reportColumnInfo.getBizAssetTerm().getBusinessTerm()
                        .getBusinessEntityTerm();

               // Prepare the new business entity term
               BusinessEntityTerm newBusinessEntityTerm = new BusinessEntityTerm();
               newBusinessEntityTerm.setBusinessEntity(existingBusinessEntityTerm.getBusinessEntity());
               newBusinessEntityTerm.setBusinessEntityType(existingBusinessEntityTerm.getBusinessEntityType());
               newBusinessEntityTerm.setBusinessEntityDefinitionId(existingBusinessEntityTerm
                        .getBusinessEntityDefinitionId());
               newBusinessEntityTerm.setDependantMeasure(existingBusinessEntityTerm.isDependantMeasure());

               // Prepare the new business term
               BusinessTerm newBusinessTerm = new BusinessTerm();
               // TODO : -VG- user has asked for the business term, we are applying stats on top of that term.
               newBusinessTerm.setRequestedByUser(true);
               newBusinessTerm.setFromCohort(reportColumnInfo.getBizAssetTerm().getBusinessTerm().isFromCohort());
               newBusinessTerm.setBusinessEntityTerm(newBusinessEntityTerm);
               newBusinessTerm.setBusinessStat(businessStat);

               // create a new business asset term and add it to the metrics section of StructuredQuery
               BusinessAssetTerm newBusinessAssetTerm = new BusinessAssetTerm();
               newBusinessAssetTerm.setBusinessTerm(newBusinessTerm);
               newBusinessAssetTerm.setAssetEntityTerm(reportColumnInfo.getBizAssetTerm().getAssetEntityTerm());
               logger.debug("Added a new business asset term into the metrics of the SQ");
               metrics.add(newBusinessAssetTerm);

               // Reset the report column info business asset term
               reportColumnInfo.getBizAssetTerm().setAssetEntityTerm(newBusinessAssetTerm.getAssetEntityTerm());
               reportColumnInfo.getBizAssetTerm().setBusinessTerm(newBusinessAssetTerm.getBusinessTerm());
               reportColumnInfo.getBizAssetTerm().setAssetEntityDefinitions(
                        newBusinessAssetTerm.getAssetEntityDefinitions());
            }
            removals.add(reportColumnInfo.getBizAssetTerm());
         }
      }
      for (BusinessAssetTerm removeBAT : removals) {
         metrics.remove(removeBAT);
      }

      // For ID type columns, set COUNT as the statistic in the BusinessTerm - modifying the StructuredQuery
      for (ReportColumnInfo reportColumnInfo : ids) {
         if (logger.isDebugEnabled()) {
            logger.debug("ID column : " + reportColumnInfo.getColumn().getName());
         }
         List<BusinessStat> businessStats = new ArrayList<BusinessStat>();
         if (reportColumnInfo.getBusinessStats() != null && reportColumnInfo.getBusinessStats().size() > 0) {
            businessStats.addAll(reportColumnInfo.getBusinessStats());
            // Get the first element as there will be only one element in the list
            reportColumnInfo.getBizAssetTerm().getBusinessTerm().setBusinessStat(businessStats.get(0));
         }
      }

      // Step 3. Rearrange the select, groupby and orderby sections
      rearrangeStructuredQuery(reportMetaInfo);
   }

   public AggregateQuery getDetailedAggregateQuery (ReportMetaInfo reportMetaInfo) {
      StructuredQuery modifiedStructuredQuery = getDetailedStructuredQuery(reportMetaInfo.getAssetQuery()
               .getLogicalQuery());

      // add the limiting condition - only if the data browser is enabled
      if (reportMetaInfo.isGenerateOnlyDataBrowser()) {
         StructuredLimitClause limitClause = new StructuredLimitClause();
         limitClause.setBusinessAssetTerm(reportMetaInfo.getAssetQuery().getLogicalQuery().getMetrics().get(0));
         limitClause.setLimitValue(getAggregationConfigurationService().getDataBrowserMaxRecords());
         limitClause.setStartValue(getAggregationConfigurationService().getDataBrowserMinRecords());
         limitClause.setLimitType(OrderLimitEntityType.TOP);
         modifiedStructuredQuery.setTopBottom(limitClause);
      }

      // modify the SQ inside the ReportMetaInfo as well
      AssetQuery assetQuery = reportMetaInfo.getAssetQuery();
      assetQuery.setLogicalQuery(modifiedStructuredQuery);

      // Re arrange the structured query
      rearrangeStructuredQueryForDetailReport(reportMetaInfo);

      AggregateQuery detailedAggregateQuery = new AggregateQuery();
      detailedAggregateQuery.setType(AggregateQueryType.DETAILED_SUMMARY);
      detailedAggregateQuery.setAssetQuery(assetQuery);
      return detailedAggregateQuery;
   }

   private StructuredQuery getDetailedStructuredQuery (StructuredQuery logicalQuery) {
      StructuredQuery modifiedSQ = new StructuredQuery();
      modifiedSQ.setModelId(logicalQuery.getModelId());
      modifiedSQ.setRollupQuery(logicalQuery.isRollupQuery());
      List<BusinessAssetTerm> detailedMetrics = new ArrayList<BusinessAssetTerm>();
      List<BusinessAssetTerm> detailedPopulations = new ArrayList<BusinessAssetTerm>();
      List<StructuredCondition> detailedConditions = new ArrayList<StructuredCondition>();

      // get the selects from the logical query
      List<BusinessAssetTerm> metrics = logicalQuery.getMetrics();
      if (ExecueCoreUtil.isCollectionNotEmpty(logicalQuery.getMetrics())) {
         for (BusinessAssetTerm metric : metrics) {
            BusinessAssetTerm detailedMetric = getDetailedBAT(metric);
            // -JM- 25-FEB-2011 : check if the metric is already present in the list
            if (!isMetricAlreadyPresent(detailedMetrics, detailedMetric)) {
               detailedMetrics.add(detailedMetric);
            }
         }
      }

      // get the population
      List<BusinessAssetTerm> populations = logicalQuery.getPopulations();
      if (ExecueCoreUtil.isCollectionNotEmpty(populations)) {
         for (BusinessAssetTerm population : populations) {
            BusinessAssetTerm detailedPopulation = getDetailedBAT(population);
            detailedPopulations.add(detailedPopulation);
         }
      }

      // get the conditions
      List<StructuredCondition> conditions = logicalQuery.getConditions();
      if (ExecueCoreUtil.isCollectionNotEmpty(conditions)) {
         for (StructuredCondition condition : conditions) {
            StructuredCondition detailedCondition = null;
            if (condition.isSubCondition()) {
               List<StructuredCondition> subConditions = new ArrayList<StructuredCondition>();
               for (StructuredCondition structuredCondition : condition.getSubConditions()) {
                  StructuredCondition subCondition = getDetailedCondition(structuredCondition);
                  subConditions.add(subCondition);
               }
               detailedCondition = new StructuredCondition();
               detailedCondition.setSubCondition(true);
               detailedCondition.setSubConditions(subConditions);
            } else {
               detailedCondition = getDetailedCondition(condition);
            }
            detailedConditions.add(detailedCondition);
         }
      }

      // get the cohort
      StructuredQuery cohortSQ = logicalQuery.getCohort();
      StructuredQuery detailedCohortSQ = null;
      if (cohortSQ != null) {
         detailedCohortSQ = getDetailedStructuredQuery(cohortSQ);
      }

      // get the scaling factor
      BusinessAssetTerm scalingFactor = logicalQuery.getScalingFactor();
      BusinessAssetTerm detailedScalingFactor = null;
      if (scalingFactor != null) {
         detailedScalingFactor = getDetailedBAT(scalingFactor);
      }
      modifiedSQ.setAsset(logicalQuery.getAsset());
      modifiedSQ.setMetrics(detailedMetrics);
      modifiedSQ.setPopulations(detailedPopulations);
      modifiedSQ.setConditions(detailedConditions);
      modifiedSQ.setCohort(detailedCohortSQ);
      modifiedSQ.setScalingFactor(detailedScalingFactor);
      modifiedSQ.setStructuredQueryWeight(logicalQuery.getStructuredQueryWeight());
      modifiedSQ.setAssetWeight(logicalQuery.getAssetWeight());
      modifiedSQ.setRelevance(logicalQuery.getRelevance());

      return modifiedSQ;
   }

   /**
    * This method checks if a particular column is already present in the metrics list that is getting built for the
    * detail path. The metrics of the business summary can differ from the detail path. <BR>
    * Eg: In the Business Summary, the metrics can be "Avg Sales, Net Sales, Company, Nominal Year" but in the detail
    * path, the same metrics section becomes "Sales, Company, Nominal Year"
    */
   private boolean isMetricAlreadyPresent (List<BusinessAssetTerm> detailedMetrics, BusinessAssetTerm detailedMetric) {
      boolean alreadyPresent = false;
      if (ExecueCoreUtil.isCollectionNotEmpty(detailedMetrics)) {
         Long toBeAddedBATBEDId = detailedMetric.getBusinessTerm().getBusinessEntityTerm()
                  .getBusinessEntityDefinitionId();
         for (BusinessAssetTerm dMetric : detailedMetrics) {
            Long bedId = dMetric.getBusinessTerm().getBusinessEntityTerm().getBusinessEntityDefinitionId();
            if (toBeAddedBATBEDId.equals(bedId) && !detailedMetric.getBusinessTerm().isFromCohort()
                     && !dMetric.getBusinessTerm().isFromCohort()) {
               alreadyPresent = true;
               break;
            }
         }
      }
      return alreadyPresent;
   }

   private StructuredCondition getDetailedCondition (StructuredCondition condition) {
      StructuredCondition detailedCondition = new StructuredCondition();
      detailedCondition.setConversionId(condition.getConversionId());
      detailedCondition.setTargetConversionUnit(condition.getTargetConversionUnit());
      detailedCondition.setLhsBusinessAssetTerm(getDetailedBAT(condition.getLhsBusinessAssetTerm()));
      detailedCondition.setOperandType(condition.getOperandType());
      detailedCondition.setOperator(condition.getOperator());
      if (condition.getRhsStructuredQuery() != null) {
         detailedCondition.setRhsStructuredQuery(getDetailedStructuredQuery(condition.getRhsStructuredQuery()));
      } else if (ExecueCoreUtil.isCollectionNotEmpty(condition.getRhsBusinessAssetTerms())) {
         List<BusinessAssetTerm> detailedRHSBATs = new ArrayList<BusinessAssetTerm>();
         for (BusinessAssetTerm rhsBAT : condition.getRhsBusinessAssetTerms()) {
            BusinessAssetTerm detailedRHSBAT = getDetailedBAT(rhsBAT);
            detailedRHSBATs.add(detailedRHSBAT);
         }
         detailedCondition.setRhsBusinessAssetTerms(detailedRHSBATs);
      } else if (ExecueCoreUtil.isCollectionNotEmpty(condition.getRhsValues())) {
         detailedCondition.setRhsValues(condition.getRhsValues());
      }
      return detailedCondition;
   }

   // modified to use the same BAT
   private BusinessAssetTerm getDetailedBAT (BusinessAssetTerm metric) {
      // BusinessAssetTerm detailedMetric = new BusinessAssetTerm();
      // detailedMetric.setAssetEntityTerm(metric.getAssetEntityTerm());
      // BusinessTerm clonedBT = ExecueBeanCloneUtil.cloneBusinessTerm(metric.getBusinessTerm());
      // remove the stats, ranges from the cloned BT
      // clonedBT.setBusinessStat(null);
      // clonedBT.setRange(null);
      // detailedMetric.setBusinessTerm(clonedBT);
      // return detailedMetric;
      BusinessTerm bizTerm = metric.getBusinessTerm();
      bizTerm.setBusinessStat(null);
      bizTerm.setRange(null);
      return metric;
   }

   public XStream getXStreamForMetaInfoTransformation () throws ReportQueryGenerationException {
      XStream xstream = new XStream();
      try {
         xstream.alias("AggregationMetaInfo", AggregationMetaInfo.class);
         xstream.alias("AggregationBusinessAssetTerm", AggregationBusinessAssetTerm.class);
         xstream.alias("AggregationCondition", AggregationCondition.class);
         xstream.alias("AggregationColumnInfo", AggregationColumnInfo.class);
         xstream.alias("AggregationRangeInfo", AggregationRangeInfo.class);
         xstream.alias("AggregationRangeDetail", AggregationRangeDetail.class);
         xstream.alias("AggregationMetaHierarchyInfo", AggregationMetaHierarchyInfo.class);
         xstream.alias("AggregationHierarchyColumnInfo", AggregationHierarchyColumnInfo.class);

         xstream.aliasField("AggregationRangeInfo", AggregationBusinessAssetTerm.class, "aggregationRangeInfo");
         xstream.aliasField("AggregationRangeDetail", AggregationRangeInfo.class, "aggregationRangeDetails");
         xstream.aliasField("AggregationBusinessAssetTerm", AggregationColumnInfo.class, "bizAssetTerm");
         xstream.aliasField("AggregationStructuredQuery", AggregationMetaInfo.class, "structuredQuery");
      } catch (Exception exception) {
         throw new ReportQueryGenerationException(4000, "Error while generating XStream for MetaInfo", exception
                  .getCause());
      }
      return xstream;
   }

   public AggregationMetaInfo transformMetaInfo (ReportMetaInfo reportMetaInfo) throws ReportQueryGenerationException {
      try {
         // SQ related info
         StructuredQuery structuredQuery = reportMetaInfo.getAssetQuery().getLogicalQuery();
         AggregationStructuredQuery aggregationStructuredQuery = transformStructuredQuery(structuredQuery,
                  reportMetaInfo.getSummaryPathType());

         // Prepare the report column infos
         List<AggregationColumnInfo> columnInfoList = new ArrayList<AggregationColumnInfo>();
         for (ReportColumnInfo reportColumnInfo : reportMetaInfo.getReportColumns()) {
            AggregationColumnInfo colInfo = transformReportColumnInfo(reportColumnInfo);
            columnInfoList.add(colInfo);
         }

         AggregationMetaInfo aggregationMetaInfo = new AggregationMetaInfo();
         aggregationMetaInfo.setStructuredQuery(aggregationStructuredQuery);
         aggregationMetaInfo.setReportColumns(columnInfoList);
         aggregationMetaInfo.setGenerateDetailedReport(reportMetaInfo.isGenerateDetailReport());
         aggregationMetaInfo.setSummaryPathType(reportMetaInfo.getSummaryPathType());
         aggregationMetaInfo.setMinMaxValues(reportMetaInfo.hasMinMaxValues());
         aggregationMetaInfo.setTotalCount(reportMetaInfo.getTotalCount());
         aggregationMetaInfo.setUniqueCounts(reportMetaInfo.hasUniqueCounts());
         aggregationMetaInfo.setUnivariants(reportMetaInfo.isUnivariants());
         aggregationMetaInfo.setDataBrowser(reportMetaInfo.isGenerateOnlyDataBrowser());
         aggregationMetaInfo.setUserQueryId(reportMetaInfo.getAssetQuery().getUserQueryId());

         if (reportMetaInfo.getSummaryPathType() == AggregateQueryType.HIERARCHY_SUMMARY) {
            // NOTE:: We should get only one hierarchy report meta info if summary path hierarchy
            ReportMetaHierarchyInfo reportMetaHierarchyInfo = reportMetaInfo.getReportMetaHierarchyInfo().get(0);
            List<AggregationHierarchyColumnInfo> hierarchyColumns = new ArrayList<AggregationHierarchyColumnInfo>();
            for (ReportHierarchyColumnInfo reportColumnInfo : reportMetaHierarchyInfo.getHierarchyColumns()) {
               AggregationHierarchyColumnInfo colInfo = transformReportHierarchyColumnInfo(reportColumnInfo);
               hierarchyColumns.add(colInfo);
            }
            AggregationMetaHierarchyInfo aggregationMetaHierarchyInfo = new AggregationMetaHierarchyInfo();
            aggregationMetaHierarchyInfo.setHierarchyName(reportMetaHierarchyInfo.getHierarchyName());
            aggregationMetaHierarchyInfo.setHierarchyDetails(hierarchyColumns);
            aggregationMetaHierarchyInfo.setHierarchyEntityCount(hierarchyColumns.size());
            aggregationMetaInfo.setAggregationMetaHierarchyInfo(aggregationMetaHierarchyInfo);
         }
         return aggregationMetaInfo;
      } catch (Exception exception) {
         exception.printStackTrace();
         throw new ReportQueryGenerationException(4000, "Error while transforming ReportMetaInfo object", exception
                  .getCause());
      }
   }

   private AggregationStructuredQuery transformStructuredQuery (StructuredQuery structuredQuery,
            AggregateQueryType aggregateQueryType) {

      AggregationStructuredQuery aggregationStructuredQuery = new AggregationStructuredQuery();
      aggregationStructuredQuery.setAssetId(structuredQuery.getAsset().getId());
      aggregationStructuredQuery.setModelId(structuredQuery.getModelId());
      aggregationStructuredQuery.setRollupQuery(structuredQuery.isRollupQuery());

      // process the metrics
      List<AggregationBusinessAssetTerm> metrics = new ArrayList<AggregationBusinessAssetTerm>();
      for (BusinessAssetTerm bat : structuredQuery.getMetrics()) {
         AggregationBusinessAssetTerm metric = transformBusinessAssetTerm(bat);
         metrics.add(metric);
      }
      aggregationStructuredQuery.setMetrics(metrics);

      // process the population
      List<AggregationBusinessAssetTerm> populations = new ArrayList<AggregationBusinessAssetTerm>();
      for (BusinessAssetTerm bat : structuredQuery.getPopulations()) {
         AggregationBusinessAssetTerm population = transformBusinessAssetTerm(bat);
         populations.add(population);
      }
      aggregationStructuredQuery.setPopulations(populations);

      // process the limit clause
      StructuredLimitClause limitClause = structuredQuery.getTopBottom();
      if (limitClause != null) {
         AggregationLimitClause topBottom = new AggregationLimitClause();
         AggregationBusinessAssetTerm limitEntity = transformBusinessAssetTerm(limitClause.getBusinessAssetTerm());
         topBottom.setBusinessAssetTerm(limitEntity);
         topBottom.setLimitType(limitClause.getLimitType());
         topBottom.setLimitValue(limitClause.getLimitValue());
         aggregationStructuredQuery.setTopBottom(topBottom);
      }

      // process the conditions
      List<AggregationCondition> conditions = new ArrayList<AggregationCondition>();
      for (StructuredCondition structuredCondition : structuredQuery.getConditions()) {
         if (structuredCondition.isSubCondition()) {
            AggregationCondition aggregationCondition = new AggregationCondition();
            aggregationCondition.setSubCondition(true);
            List<AggregationCondition> subConditions = new ArrayList<AggregationCondition>();
            for (StructuredCondition subCondition : structuredCondition.getSubConditions()) {
               subConditions.add(transformStructuredCondition(subCondition, aggregateQueryType));
            }
            aggregationCondition.setSubConditions(subConditions);
            conditions.add(aggregationCondition);
         } else {
            conditions.add(transformStructuredCondition(structuredCondition, aggregateQueryType));
         }
      }
      aggregationStructuredQuery.setConditions(conditions);

      // process the cohort
      if (structuredQuery.getCohort() != null) {
         aggregationStructuredQuery
                  .setCohort(transformStructuredQuery(structuredQuery.getCohort(), aggregateQueryType));
      }

      // process the summarizations
      if (ExecueCoreUtil.isCollectionNotEmpty(structuredQuery.getSummarizations())) {
         List<AggregationBusinessAssetTerm> summarizations = new ArrayList<AggregationBusinessAssetTerm>();
         for (BusinessAssetTerm bat : structuredQuery.getSummarizations()) {
            AggregationBusinessAssetTerm summarization = transformBusinessAssetTerm(bat);
            summarizations.add(summarization);
         }
         aggregationStructuredQuery.setSummarizations(summarizations);
      }

      // process the order by's
      if (ExecueCoreUtil.isCollectionNotEmpty(structuredQuery.getOrderClauses())) {
         List<AggregationOrderClause> orderClauses = new ArrayList<AggregationOrderClause>();
         for (StructuredOrderClause soc : structuredQuery.getOrderClauses()) {
            AggregationOrderClause orderClause = new AggregationOrderClause();
            orderClause.setBusinessAssetTerm(transformBusinessAssetTerm(soc.getBusinessAssetTerm()));
            orderClause.setOrderEntityType(soc.getOrderEntityType());
            orderClauses.add(orderClause);
         }
         aggregationStructuredQuery.setOrderClauses(orderClauses);
      }

      // process the scaling factor
      BusinessAssetTerm scalingFactorBAT = structuredQuery.getScalingFactor();
      if (scalingFactorBAT != null) {
         AggregationBusinessAssetTerm scalingFactor = transformBusinessAssetTerm(scalingFactorBAT);
         aggregationStructuredQuery.setScalingFactor(scalingFactor);
      }

      // set the structuredQueryWeight
      aggregationStructuredQuery.setStructuredQueryWeight(structuredQuery.getStructuredQueryWeight());
      aggregationStructuredQuery.setRelevance(structuredQuery.getRelevance());
      return aggregationStructuredQuery;
   }

   private AggregationCondition transformStructuredCondition (StructuredCondition structuredCondition,
            AggregateQueryType aggregateQueryType) {
      AggregationCondition condition = new AggregationCondition();
      AggregationBusinessAssetTerm lhsBusinessAssetTerm = transformBusinessAssetTerm(structuredCondition
               .getLhsBusinessAssetTerm());
      condition.setLhsBusinessAssetTerm(lhsBusinessAssetTerm);
      condition.setOperandType(structuredCondition.getOperandType());
      condition.setOperator(structuredCondition.getOperator());
      condition.setConversionId(structuredCondition.getConversionId());
      condition.setTargetConversionUnit(structuredCondition.getTargetConversionUnit());
      if (ExecueCoreUtil.isCollectionNotEmpty(structuredCondition.getRhsBusinessAssetTerms())) {
         List<AggregationBusinessAssetTerm> rhsBusinessAssetTerms = new ArrayList<AggregationBusinessAssetTerm>();
         for (BusinessAssetTerm bat : structuredCondition.getRhsBusinessAssetTerms()) {
            AggregationBusinessAssetTerm rhsBusinessAssetTerm = transformBusinessAssetTerm(bat);
            rhsBusinessAssetTerms.add(rhsBusinessAssetTerm);
         }
         condition.setRhsBusinessAssetTerms(rhsBusinessAssetTerms);
      } else if (structuredCondition.getRhsStructuredQuery() != null) {
         condition.setRhsStructuredQuery(transformStructuredQuery(structuredCondition.getRhsStructuredQuery(),
                  aggregateQueryType));
      } else if (ExecueCoreUtil.isCollectionNotEmpty(structuredCondition.getRhsValues())) {
         condition.setRhsValues(structuredCondition.getRhsValues());
      }
      return condition;
   }

   private AggregationBusinessAssetTerm transformBusinessAssetTerm (BusinessAssetTerm businessAssetTerm) {

      AssetEntityTerm assetEntityTerm = businessAssetTerm.getAssetEntityTerm();
      BusinessTerm businessTerm = businessAssetTerm.getBusinessTerm();

      BusinessStat businessStat = businessTerm.getBusinessStat();
      BusinessEntityTerm businessEntityTerm = businessTerm.getBusinessEntityTerm();

      AggregationBusinessAssetTerm aggBizAssetTerm = new AggregationBusinessAssetTerm();
      aggBizAssetTerm.setIdentifier(businessAssetTerm.hashCode());

      aggBizAssetTerm.setAssetEntityType(assetEntityTerm.getAssetEntityType());
      aggBizAssetTerm.setAssetEntityDefinitionId(assetEntityTerm.getAssetEntityDefinitionId());

      aggBizAssetTerm.setRequestedByUser(businessTerm.isRequestedByUser());
      aggBizAssetTerm.setFromCohort(businessTerm.isFromCohort());
      aggBizAssetTerm.setFromPopulation(businessTerm.isFromPopulation());
      aggBizAssetTerm.setFromDistribution(businessTerm.isFromDistribution());
      aggBizAssetTerm.setAggregationRangeInfo(transformRange(businessTerm.getRange()));

      aggBizAssetTerm.setBusinessEntityDefinitionId(businessEntityTerm.getBusinessEntityDefinitionId());
      aggBizAssetTerm.setBusinessEntityType(businessEntityTerm.getBusinessEntityType());
      aggBizAssetTerm.setDependantMeasure(businessEntityTerm.isDependantMeasure());

      if (businessStat != null) {
         aggBizAssetTerm.setBusinessStatType(businessStat.getStat().getStatType());
         aggBizAssetTerm.setBusinessStatRequestedByUser(businessStat.isRequestedByUser());
      }
      return aggBizAssetTerm;
   }

   private AggregationRangeInfo transformRange (Range range) {

      if (range == null) {
         return null;
      }

      Set<AggregationRangeDetail> aggregationRangeDetails = transformRangeDetails(range.getRangeDetails());

      // prepare the aggregationRangeInfo
      AggregationRangeInfo aggregationRangeInfo = new AggregationRangeInfo();
      aggregationRangeInfo.setAggregationRangeDetails(aggregationRangeDetails);
      aggregationRangeInfo.setConceptBedId(range.getConceptBedId());
      aggregationRangeInfo.setConceptDisplayName(range.getConceptDisplayName());
      aggregationRangeInfo.setDescription(range.getDescription());
      aggregationRangeInfo.setEnabled(range.isEnabled());
      aggregationRangeInfo.setName(range.getName());
      aggregationRangeInfo.setRangeId(range.getId());
      if (range.getUser() != null) {
         aggregationRangeInfo.setUserId(range.getUser().getId());
      }

      return aggregationRangeInfo;
   }

   private Set<AggregationRangeDetail> transformRangeDetails (Set<RangeDetail> rangeDetails) {

      Set<AggregationRangeDetail> aggregationRangeDetails = new HashSet<AggregationRangeDetail>();
      for (RangeDetail rangeDetail : rangeDetails) {
         aggregationRangeDetails.add(transformRangeDetail(rangeDetail));
      }

      return aggregationRangeDetails;
   }

   private AggregationRangeDetail transformRangeDetail (RangeDetail rangeDetail) {

      AggregationRangeDetail aggregationRangeDetail = new AggregationRangeDetail();
      aggregationRangeDetail.setDescription(rangeDetail.getDescription());
      aggregationRangeDetail.setLowerLimit(rangeDetail.getLowerLimit());
      aggregationRangeDetail.setOrder(rangeDetail.getOrder());
      aggregationRangeDetail.setRangeDetailId(rangeDetail.getId());
      aggregationRangeDetail.setUpperLimit(rangeDetail.getUpperLimit());
      aggregationRangeDetail.setValue(rangeDetail.getValue());

      return aggregationRangeDetail;
   }

   private AggregationHierarchyColumnInfo transformReportHierarchyColumnInfo (
            ReportHierarchyColumnInfo reportHierarchyColumnInfo) {
      AggregationHierarchyColumnInfo aggregationHierarchyColumnInfo = new AggregationHierarchyColumnInfo();
      aggregationHierarchyColumnInfo.setBizAssetTerm(transformBusinessAssetTerm(reportHierarchyColumnInfo
               .getBizAssetTerm()));
      aggregationHierarchyColumnInfo.setLevel(reportHierarchyColumnInfo.getLevel());
      return aggregationHierarchyColumnInfo;
   }

   private AggregationColumnInfo transformReportColumnInfo (ReportColumnInfo reportColumnInfo) {

      AggregationBusinessAssetTerm bizAssetTerm = transformBusinessAssetTerm(reportColumnInfo.getBizAssetTerm());
      List<StatType> businessStatTypes = new ArrayList<StatType>();
      Set<BusinessStat> businessStats = reportColumnInfo.getBusinessStats();
      if (ExecueCoreUtil.isCollectionNotEmpty(businessStats)) {
         for (BusinessStat businessStat : reportColumnInfo.getBusinessStats()) {
            businessStatTypes.add(businessStat.getStat().getStatType());
         }
      }

      AggregationColumnInfo aggregationColumnInfo = new AggregationColumnInfo();
      aggregationColumnInfo.setBizAssetTerm(bizAssetTerm);
      aggregationColumnInfo.setBusinessStatTypes(businessStatTypes);
      aggregationColumnInfo.setColumnTypeDeduced(reportColumnInfo.isColumnTypeDeduced());
      aggregationColumnInfo.setColumnType(reportColumnInfo.getColumnType());
      aggregationColumnInfo.setCountRequired(reportColumnInfo.isCountRequired());
      aggregationColumnInfo.setStatsDeduced(reportColumnInfo.areStatsDeduced());
      aggregationColumnInfo.setCountSize(reportColumnInfo.getCountSize());
      aggregationColumnInfo.setEligibleForRanges(reportColumnInfo.isEligibleForRanges());
      aggregationColumnInfo.setMarkedForRangeDerivation(reportColumnInfo.isMarkedForRangeDerivation());
      aggregationColumnInfo.setMaximumValue(reportColumnInfo.getMaximumValue());
      aggregationColumnInfo.setMinimumValue(reportColumnInfo.getMinimumValue());
      aggregationColumnInfo.setUnivariantValue(reportColumnInfo.getUnivariantValue());
      aggregationColumnInfo.setUserRequestedSummarization(reportColumnInfo.isUserRequestedSummarization());
      return aggregationColumnInfo;
   }

   public ReportMetaInfo constructReportMetaInfo (AggregationMetaInfo aggregationMetaInfo) throws ReportException {
      try {
         // Prepare the AssetQuery
         AssetQuery assetQuery = new AssetQuery();
         StructuredQuery logicalQuery = constructStructuredQuery(aggregationMetaInfo.getStructuredQuery(),
                  aggregationMetaInfo.getSummaryPathType());
         assetQuery.setLogicalQuery(logicalQuery);
         assetQuery.setUserQueryId(aggregationMetaInfo.getUserQueryId());

         // Prepare the report columns
         List<ReportColumnInfo> reportColumns = new ArrayList<ReportColumnInfo>();
         for (AggregationColumnInfo colInfo : aggregationMetaInfo.getReportColumns()) {
            reportColumns.add(constructReportColumnInfo(colInfo));
         }

         ReportMetaInfo reportMetaInfo = new ReportMetaInfo(assetQuery);
         reportMetaInfo.setReportColumns(reportColumns);
         reportMetaInfo.setSummaryPathType(aggregationMetaInfo.getSummaryPathType());
         reportMetaInfo.setGenerateDetailReport(aggregationMetaInfo.isGenerateDetailedReport());
         reportMetaInfo.setMinMaxValues(aggregationMetaInfo.isMinMaxValues());
         reportMetaInfo.setTotalCount(aggregationMetaInfo.getTotalCount());
         reportMetaInfo.setUniqueCounts(aggregationMetaInfo.isUniqueCounts());
         reportMetaInfo.setUnivariants(aggregationMetaInfo.isUnivariants());
         reportMetaInfo.setGenerateOnlyDataBrowser(aggregationMetaInfo.isDataBrowser());

         if (aggregationMetaInfo.getSummaryPathType() == AggregateQueryType.HIERARCHY_SUMMARY) {

            List<ReportHierarchyColumnInfo> reportHierarchyColumnInfos = new ArrayList<ReportHierarchyColumnInfo>();
            for (AggregationHierarchyColumnInfo aggregationHierarchyColumnInfo : aggregationMetaInfo
                     .getAggregationMetaHierarchyInfo().getHierarchyDetails()) {
               reportHierarchyColumnInfos.add(constructReportHierarchyColumnInfo(aggregationHierarchyColumnInfo));
            }
            ReportMetaHierarchyInfo reportMetaHierarchyInfo = new ReportMetaHierarchyInfo();
            reportMetaHierarchyInfo.setHierarchyName(aggregationMetaInfo.getAggregationMetaHierarchyInfo()
                     .getHierarchyName());
            reportMetaHierarchyInfo.setHierarchyColumns(reportHierarchyColumnInfos);
            List<ReportMetaHierarchyInfo> reportMetaHierarchyInfos = new ArrayList<ReportMetaHierarchyInfo>();
            reportMetaHierarchyInfos.add(reportMetaHierarchyInfo);
            reportMetaInfo.setReportMetaHierarchyInfo(reportMetaHierarchyInfos);
         }
         return reportMetaInfo;
      } catch (SDXException sdxException) {
         sdxException.printStackTrace();
         sdxException.printStackTrace();
         logger.error("SDXException in ReportAggregationHelper : ", sdxException);
         logger.error("Actual Error : [" + sdxException.getCode() + "] " + sdxException.getMessage());
         logger.error("Cause : " + sdxException.getCause());
         throw new ReportException(sdxException.getCode(), sdxException.getMessage(), sdxException.getCause());
      } catch (KDXException kdException) {
         kdException.printStackTrace();
         logger.error("KDXException in ReportAggregationHelper : ", kdException);
         logger.error("Actual Error : [" + kdException.getCode() + "] " + kdException.getMessage());
         logger.error("Cause : " + kdException.getCause());
         throw new ReportException(kdException.getCode(), kdException.getMessage(), kdException.getCause());
      }
   }

   private ReportHierarchyColumnInfo constructReportHierarchyColumnInfo (
            AggregationHierarchyColumnInfo aggregationHierarchyColumnInfo) throws SDXException, KDXException {
      ReportHierarchyColumnInfo reportHierarchyColumnInfo = new ReportHierarchyColumnInfo();
      reportHierarchyColumnInfo.setBizAssetTerm(constructBusinessAssetTerm(aggregationHierarchyColumnInfo
               .getBizAssetTerm()));
      reportHierarchyColumnInfo.setLevel(aggregationHierarchyColumnInfo.getLevel());
      return reportHierarchyColumnInfo;
   }

   private ReportColumnInfo constructReportColumnInfo (AggregationColumnInfo colInfo) throws SDXException, KDXException {
      BusinessAssetTerm bizAssetTerm = constructBusinessAssetTerm(colInfo.getBizAssetTerm());

      List<StatType> businessStatTypes = colInfo.getBusinessStatTypes();
      Set<BusinessStat> businessStats = new HashSet<BusinessStat>();
      boolean areStatsDeduced = colInfo.isStatsDeduced();
      if (ExecueCoreUtil.isCollectionNotEmpty(businessStatTypes)) {
         for (StatType businessStatType : businessStatTypes) {
            // TODO:NK:: Check with GA/VG if requestedByUser can be set as !areStatsDeduced as in below
            businessStats.add(prepareBusinessStat(businessStatType, !areStatsDeduced));
         }
      }

      ReportColumnInfo reportColumnInfo = prepareReportColumn(bizAssetTerm);
      reportColumnInfo.setBusinessStats(businessStats);
      reportColumnInfo.setColumnTypeDeduced(colInfo.isColumnTypeDeduced());
      reportColumnInfo.setCountRequired(colInfo.isCountRequired());
      reportColumnInfo.setStatsDeduced(areStatsDeduced);
      reportColumnInfo.setCountSize(colInfo.getCountSize());
      reportColumnInfo.setEligibleForRanges(colInfo.isEligibleForRanges());
      reportColumnInfo.setMarkedForRangeDerivation(colInfo.isMarkedForRangeDerivation());
      reportColumnInfo.setMaximumValue(colInfo.getMaximumValue());
      reportColumnInfo.setMinimumValue(colInfo.getMinimumValue());
      reportColumnInfo.setUnivariantValue(colInfo.getUnivariantValue());
      reportColumnInfo.setUserRequestedSummarization(colInfo.isUserRequestedSummarization());
      return reportColumnInfo;
   }

   private BusinessAssetTerm constructBusinessAssetTerm (AggregationBusinessAssetTerm aggBAT) throws SDXException,
            KDXException {

      // Prepare the business entity term
      BusinessEntityTerm businessEntityTerm = new BusinessEntityTerm();
      businessEntityTerm.setBusinessEntityType(aggBAT.getBusinessEntityType());
      businessEntityTerm.setBusinessEntityDefinitionId(aggBAT.getBusinessEntityDefinitionId());
      businessEntityTerm.setDependantMeasure(aggBAT.isDependantMeasure());
      if (BusinessEntityType.CONCEPT.equals(businessEntityTerm.getBusinessEntityType())) {
         Concept concept = kdxRetrievalService.getBusinessEntityDefinitionById(aggBAT.getBusinessEntityDefinitionId())
                  .getConcept();
         businessEntityTerm.setBusinessEntity(concept);
      } else if (BusinessEntityType.CONCEPT_LOOKUP_INSTANCE.equals(businessEntityTerm.getBusinessEntityType())) {
         Instance instance = kdxRetrievalService
                  .getBusinessEntityDefinitionById(aggBAT.getBusinessEntityDefinitionId()).getInstance();
         businessEntityTerm.setBusinessEntity(instance);
      }

      // Prepare the business term
      BusinessTerm businessTerm = new BusinessTerm();
      businessTerm.setBusinessEntityTerm(businessEntityTerm);
      businessTerm.setRequestedByUser(aggBAT.isRequestedByUser());
      businessTerm.setFromCohort(aggBAT.isFromCohort());
      businessTerm.setFromDistribution(aggBAT.isFromDistribution());
      businessTerm.setFromPopulation(aggBAT.isFromPopulation());
      businessTerm.setRange(prepareRange(aggBAT.getAggregationRangeInfo()));
      businessTerm.setBusinessStat(prepareBusinessStat(aggBAT.getBusinessStatType(), aggBAT
               .isBusinessStatRequestedByUser()));

      // Prepare the asset entity term
      AssetEntityTerm assetEntityTerm = new AssetEntityTerm();
      assetEntityTerm.setAssetEntityType(aggBAT.getAssetEntityType());
      assetEntityTerm.setAssetEntityDefinitionId(aggBAT.getAssetEntityDefinitionId());
      if (AssetEntityType.COLUMN.equals(assetEntityTerm.getAssetEntityType())) {
         AssetEntityDefinition columnAED = getSdxRetrievalService().getAssetEntityDefinitionById(
                  aggBAT.getAssetEntityDefinitionId());
         Colum column = columnAED.getColum();
         column.setOwnerTable(columnAED.getTabl());
         assetEntityTerm.setAssetEntity(column);
      } else if (AssetEntityType.MEMBER.equals(assetEntityTerm.getAssetEntityType())) {
         Membr member = getSdxRetrievalService().getAssetEntityDefinitionById(aggBAT.getAssetEntityDefinitionId())
                  .getMembr();
         assetEntityTerm.setAssetEntity(member);
      }

      // Prepare the business asset term
      BusinessAssetTerm bat = new BusinessAssetTerm();
      bat.setBusinessTerm(businessTerm);
      bat.setAssetEntityTerm(assetEntityTerm);

      return bat;
   }

   private Range prepareRange (AggregationRangeInfo aggregationRangeInfo) {

      // Prepare the range details
      if (aggregationRangeInfo == null) {
         return null;
      }
      Set<AggregationRangeDetail> aggregationRangeDetails = aggregationRangeInfo.getAggregationRangeDetails();
      Set<RangeDetail> rangeDetails = prepareRangeDetails(aggregationRangeDetails);

      // Prepare the user
      User user = new User();
      user.setId(aggregationRangeInfo.getUserId());

      // Prepare the range
      Range range = new Range();
      range.setConceptBedId(aggregationRangeInfo.getConceptBedId());
      range.setConceptDisplayName(aggregationRangeInfo.getConceptDisplayName());
      range.setDescription(aggregationRangeInfo.getDescription());
      range.setEnabled(aggregationRangeInfo.isEnabled());
      range.setId(aggregationRangeInfo.getRangeId());
      range.setName(aggregationRangeInfo.getName());
      range.setRangeDetails(rangeDetails);
      range.setUser(user);

      return range;
   }

   private Set<RangeDetail> prepareRangeDetails (Set<AggregationRangeDetail> aggregationRangeDetails) {

      Set<RangeDetail> rangeDetails = new HashSet<RangeDetail>();

      for (AggregationRangeDetail aggregationRangeDetail : aggregationRangeDetails) {
         rangeDetails.add(prepareRangeDetail(aggregationRangeDetail));
      }

      return rangeDetails;
   }

   private RangeDetail prepareRangeDetail (AggregationRangeDetail aggregationRangeDetail) {

      RangeDetail rangeDetail = new RangeDetail();
      rangeDetail.setDescription(aggregationRangeDetail.getDescription());
      rangeDetail.setId(aggregationRangeDetail.getRangeDetailId());
      rangeDetail.setLowerLimit(aggregationRangeDetail.getLowerLimit());
      rangeDetail.setOrder(aggregationRangeDetail.getOrder());
      rangeDetail.setUpperLimit(aggregationRangeDetail.getUpperLimit());
      rangeDetail.setValue(aggregationRangeDetail.getValue());

      return rangeDetail;
   }

   /**
    * Method to Build the business stat
    * 
    * @param statType
    * @throws KDXException
    */
   public BusinessStat prepareBusinessStat (StatType statType, boolean requestedByUser) throws KDXException {
      if (statType == null) {
         return null;
      }

      // Prepare the business stat
      Stat stat = getKdxRetrievalService().getStatByName(statType.getValue());
      BusinessStat businessStat = new BusinessStat();
      businessStat.setRequestedByUser(requestedByUser);
      businessStat.setStat(stat);
      return businessStat;
   }

   private StructuredQuery constructStructuredQuery (AggregationStructuredQuery aggregationStructuredQuery,
            AggregateQueryType aggregateQueryType) throws SDXException, KDXException {
      StructuredQuery structuredQuery = new StructuredQuery();
      structuredQuery.setModelId(aggregationStructuredQuery.getModelId());
      structuredQuery.setRollupQuery(aggregationStructuredQuery.isRollupQuery());

      // process the asset information
      Asset asset = getSdxRetrievalService().getAsset(aggregationStructuredQuery.getAssetId());
      structuredQuery.setAsset(asset);

      // map having the hashcode and the metrics
      Map<Integer, BusinessAssetTerm> hashCodeMetricsMap = new HashMap<Integer, BusinessAssetTerm>();

      // process the metrics
      List<BusinessAssetTerm> metrics = new ArrayList<BusinessAssetTerm>();
      for (AggregationBusinessAssetTerm bat : aggregationStructuredQuery.getMetrics()) {
         BusinessAssetTerm metric = constructBusinessAssetTerm(bat);
         metrics.add(metric);
         hashCodeMetricsMap.put(bat.getIdentifier(), metric);
      }
      structuredQuery.setMetrics(metrics);

      // process the population
      List<BusinessAssetTerm> populations = new ArrayList<BusinessAssetTerm>();
      for (AggregationBusinessAssetTerm bat : aggregationStructuredQuery.getPopulations()) {
         BusinessAssetTerm population = constructBusinessAssetTerm(bat);
         populations.add(population);
      }
      structuredQuery.setPopulations(populations);

      // process the limit clause
      AggregationLimitClause limitClause = aggregationStructuredQuery.getTopBottom();
      if (limitClause != null) {
         StructuredLimitClause topBottom = new StructuredLimitClause();
         BusinessAssetTerm limitEntity = constructBusinessAssetTerm(limitClause.getBusinessAssetTerm());
         topBottom.setBusinessAssetTerm(limitEntity);
         topBottom.setLimitType(limitClause.getLimitType());
         topBottom.setLimitValue(limitClause.getLimitValue());
         topBottom.setStartValue(limitClause.getStartValue());
         structuredQuery.setTopBottom(topBottom);
      }

      // process the conditions
      List<StructuredCondition> structuredConditions = new ArrayList<StructuredCondition>();
      for (AggregationCondition condition : aggregationStructuredQuery.getConditions()) {
         if (condition.isSubCondition()) {
            StructuredCondition structuredCondition = new StructuredCondition();
            List<StructuredCondition> subConditions = new ArrayList<StructuredCondition>();
            for (AggregationCondition subCondition : condition.getSubConditions()) {
               subConditions.add(constructStructuredCondition(subCondition, aggregateQueryType));
            }
            structuredCondition.setSubCondition(true);
            structuredCondition.setSubConditions(subConditions);
            structuredConditions.add(structuredCondition);
         } else {
            structuredConditions.add(constructStructuredCondition(condition, aggregateQueryType));
         }
      }
      structuredQuery.setConditions(structuredConditions);

      // process the cohort
      if (aggregationStructuredQuery.getCohort() != null) {
         structuredQuery
                  .setCohort(constructStructuredQuery(aggregationStructuredQuery.getCohort(), aggregateQueryType));
      }

      // process the scaling factor
      AggregationBusinessAssetTerm scalingFactorBAT = aggregationStructuredQuery.getScalingFactor();
      if (scalingFactorBAT != null) {
         BusinessAssetTerm scalingFactor = constructBusinessAssetTerm(scalingFactorBAT);
         structuredQuery.setScalingFactor(scalingFactor);
      }

      // process the summarizations only in case of hierarchy summary
      if (ExecueCoreUtil.isCollectionNotEmpty(aggregationStructuredQuery.getSummarizations())) {
         List<BusinessAssetTerm> summarizations = new ArrayList<BusinessAssetTerm>();
         for (AggregationBusinessAssetTerm bat : aggregationStructuredQuery.getSummarizations()) {
            BusinessAssetTerm summarization = constructBusinessAssetTerm(bat);
            summarizations.add(summarization);
         }
         structuredQuery.setSummarizations(summarizations);
      }

      // process the order bys
      if (ExecueCoreUtil.isCollectionNotEmpty(aggregationStructuredQuery.getOrderClauses())) {
         List<StructuredOrderClause> orderClauses = new ArrayList<StructuredOrderClause>();
         for (AggregationOrderClause soc : aggregationStructuredQuery.getOrderClauses()) {
            StructuredOrderClause orderClause = new StructuredOrderClause();
            // check in the map if a metric exists for the hashcode of the order clause
            if (soc.getBusinessAssetTerm().getIdentifier() != null
                     && hashCodeMetricsMap.get(soc.getBusinessAssetTerm().getIdentifier()) != null) {
               orderClause.setBusinessAssetTerm(hashCodeMetricsMap.get(soc.getBusinessAssetTerm().getIdentifier()));
            } else {
               orderClause.setBusinessAssetTerm(constructBusinessAssetTerm(soc.getBusinessAssetTerm()));
            }
            orderClause.setOrderEntityType(soc.getOrderEntityType());
            orderClauses.add(orderClause);
         }
         structuredQuery.setOrderClauses(orderClauses);
      }

      // set the structuredQueryWeight
      structuredQuery.setStructuredQueryWeight(aggregationStructuredQuery.getStructuredQueryWeight());
      structuredQuery.setRelevance(aggregationStructuredQuery.getRelevance());

      return structuredQuery;
   }

   private StructuredCondition constructStructuredCondition (AggregationCondition condition,
            AggregateQueryType aggregateQueryType) throws SDXException, KDXException {
      StructuredCondition structuredCondition = new StructuredCondition();
      BusinessAssetTerm lhsBusinessAssetTerm = constructBusinessAssetTerm(condition.getLhsBusinessAssetTerm());
      structuredCondition.setLhsBusinessAssetTerm(lhsBusinessAssetTerm);
      structuredCondition.setOperandType(condition.getOperandType());
      structuredCondition.setOperator(condition.getOperator());
      if (ExecueCoreUtil.isCollectionNotEmpty(condition.getRhsBusinessAssetTerms())) {
         List<BusinessAssetTerm> rhsBusinessAssetTerms = new ArrayList<BusinessAssetTerm>();
         for (AggregationBusinessAssetTerm bat : condition.getRhsBusinessAssetTerms()) {
            BusinessAssetTerm rhsBusinessAssetTerm = constructBusinessAssetTerm(bat);
            rhsBusinessAssetTerms.add(rhsBusinessAssetTerm);
         }
         structuredCondition.setRhsBusinessAssetTerms(rhsBusinessAssetTerms);
      } else if (structuredCondition.getRhsStructuredQuery() != null) {
         structuredCondition.setRhsStructuredQuery(constructStructuredQuery(condition.getRhsStructuredQuery(),
                  aggregateQueryType));
      } else if (ExecueCoreUtil.isCollectionNotEmpty(condition.getRhsValues())) {
         structuredCondition.setRhsValues(condition.getRhsValues());
      }
      return structuredCondition;
   }

   public AggregateQuery getHierarchyAggregateQuery (ReportMetaInfo reportMetaInfo)
            throws ReportQueryGenerationException {

      // Modify the structured query for multiple stats and missing stats on measures and id column.
      // Also rearrange selects, group by and order by
      modifyStructuredQuery(reportMetaInfo);

      // Modify the structured query as per the report hierarchy meta info
      modifyStructuredQueryBasedOnHierarchyInfo(reportMetaInfo);

      // reset the report columns
      reArrangeReportColumns(reportMetaInfo);

      // convert the ReportMetaInfo object into AggregationMetaInfo object
      AggregationMetaInfo aggregationMetaInfoStructure = transformMetaInfo(reportMetaInfo);

      // generate the AggregateMetaInfo xml
      String aggregateMetaInfoXML = getXStreamForMetaInfoTransformation().toXML(aggregationMetaInfoStructure);

      // NOTE:: We are setting report type as only hierarchy grid for now.
      // Later, if required, we should change or add report types based on any rules.
      List<ReportType> reportTypes = new ArrayList<ReportType>();
      reportTypes.add(ReportType.HierarchicalGrid);

      // Prepare the aggregate query for the hierarchy
      AggregateQuery hierarchyAggregateQuery = new AggregateQuery();
      hierarchyAggregateQuery.setType(AggregateQueryType.HIERARCHY_SUMMARY);
      hierarchyAggregateQuery.setAssetQuery(reportMetaInfo.getAssetQuery());
      hierarchyAggregateQuery.setReportMetaInfoStructure(aggregateMetaInfoXML);
      // NOTE:: For hierarchy summary, we are extracting the data at the time of user clicking the report link
      // hence setting the data extracted as NO
      hierarchyAggregateQuery.setDataExtracted(CheckType.NO);
      hierarchyAggregateQuery.setReportTypes(reportTypes);

      return hierarchyAggregateQuery;

   }

   public AggregateQuery getScatterAggregateQuery (ReportMetaInfo reportMetaInfo,
            List<BusinessAssetTerm> combinationMetrics) throws ReportQueryGenerationException {

      // Modify the structured query for multiple stats and missing stats on measures and id column.
      // Also rearrange selects, group by and order by
      modifyStructuredQuery(reportMetaInfo);

      // Modify the structured query as per the scatter report combination metrics info
      modifyStructuredQueryBasedOnScatterInfo(reportMetaInfo, combinationMetrics);

      // reset the report columns
      reArrangeReportColumns(reportMetaInfo);

      // List<ReportColumnInfo> reportColumns = prepareReportColumns(reportMetaInfo.getAssetQuery().getLogicalQuery()
      // .getMetrics());
      // reportMetaInfo.setReportColumns(reportColumns);

      // NOTE: Assumption here is first one is always independent metric which is required to be plot as dimension using dynamic ranges
      // So modify the column type as dimension and set eligibility for ranges
      ReportColumnInfo reportColumnInfo = reportMetaInfo.getReportColumns().get(0);
      reportColumnInfo.modifyColumnType(ColumnType.DIMENSION);
      reportColumnInfo.setEligibleForRanges(true);

      // TODO:: NK: Check if we need to go via range process route here or can just simply call dynamic range suggestion service here??
      try {
         QueryGenerationOutput queryGenerationOutput = getStructuredQueryTransformerService()
                  .populateQueryGenerationOutput(reportMetaInfo.getAssetQuery().getLogicalQuery(),
                           new HashMap<String, BusinessAssetTerm>());
         reportMetaInfo.getAssetQuery().setPhysicalQuery(queryGenerationOutput.getResultQuery());
         getRangeProcessor().analyzeMetadata(reportMetaInfo);
      } catch (ReportMetadataException e) {
         throw new ReportQueryGenerationException(e.getCode(), e);
      }

      // convert the ReportMetaInfo object into AggregationMetaInfo object
      AggregationMetaInfo aggregationMetaInfoStructure = transformMetaInfo(reportMetaInfo);

      // generate the AggregateMetaInfo xml
      String aggregateMetaInfoXML = getXStreamForMetaInfoTransformation().toXML(aggregationMetaInfoStructure);

      // NOTE:: We are setting report type as only scatter chart for now.
      // Later, if required, we should change or add report types based on any rules.
      List<ReportType> reportTypes = new ArrayList<ReportType>();
      reportTypes.add(ReportType.ScatterChart);

      // Prepare the aggregate query for the scatter
      AggregateQuery scatterAggregateQuery = new AggregateQuery();
      scatterAggregateQuery.setType(AggregateQueryType.SCATTER);
      scatterAggregateQuery.setAssetQuery(reportMetaInfo.getAssetQuery());
      scatterAggregateQuery.setReportMetaInfoStructure(aggregateMetaInfoXML);
      scatterAggregateQuery.setReportTypes(reportTypes);
      scatterAggregateQuery.setDataPresent(CheckType.NO);
      // NOTE:: For scatter in business summary case, we are extracting the data at the time
      // of user clicking the report link hence setting the data extracted as NO
      scatterAggregateQuery.setDataExtracted(CheckType.NO);

      return scatterAggregateQuery;
   }

   private void modifyStructuredQueryBasedOnScatterInfo (ReportMetaInfo reportMetaInfo,
            List<BusinessAssetTerm> combinationMetrics) {

      // Get the structured query
      StructuredQuery structuredQuery = reportMetaInfo.getAssetQuery().getLogicalQuery();
      BusinessAssetTerm independentMetric = combinationMetrics.get(1);

      // Assumption here is independent metric is ALWAYS second in the combination metric list
      BusinessAssetTerm clonedIndependentMetric = cloneBusinessAssetTerm(independentMetric, false);
      clonedIndependentMetric.getBusinessTerm().setBusinessStat(null);

      // Prepare the metrics for the scatter
      List<BusinessAssetTerm> metrics = new ArrayList<BusinessAssetTerm>();
      metrics.add(clonedIndependentMetric);
      metrics.addAll(combinationMetrics);

      // Prepare the summarization for the scatter
      List<BusinessAssetTerm> summarizations = new ArrayList<BusinessAssetTerm>();
      summarizations.add(clonedIndependentMetric);

      // reset the order by as the hierarchy dimensions
      List<StructuredOrderClause> scatterOrderBys = new ArrayList<StructuredOrderClause>();
      scatterOrderBys.add(getStructuredOrderClause(clonedIndependentMetric));

      // Reset the existing metrics(selects), summarizations, order by's
      structuredQuery.setMetrics(metrics);
      structuredQuery.setSummarizations(summarizations);
      structuredQuery.setOrderClauses(scatterOrderBys);
   }

   /**
    * This method will update the structured(asset) query based on the reportMetaHierarchyInfo It adds the hierarchy
    * dimension to the select, group by and order by section and removes the existing matches in them
    * 
    * @param reportMetaInfo
    * @throws MappingException
    */
   private void modifyStructuredQueryBasedOnHierarchyInfo (ReportMetaInfo reportMetaInfo) {

      // Prepare the hierarchy dimensions business asset terms
      ReportMetaHierarchyInfo reportMetaHierarchyInfo = reportMetaInfo.getReportMetaHierarchyInfo().get(0);
      List<ReportHierarchyColumnInfo> reportHierarchyColumnInfos = reportMetaHierarchyInfo.getHierarchyColumns();
      List<BusinessAssetTerm> hierarchyDimensions = new ArrayList<BusinessAssetTerm>();
      for (ReportHierarchyColumnInfo reportHierarchyColumnInfo : reportHierarchyColumnInfos) {
         hierarchyDimensions.add(reportHierarchyColumnInfo.getBizAssetTerm());
      }

      // Get the structured query
      StructuredQuery logicalQuery = reportMetaInfo.getAssetQuery().getLogicalQuery();

      // Process for (group by's)
      List<BusinessAssetTerm> summarizationsToRemove = new ArrayList<BusinessAssetTerm>(logicalQuery
               .getSummarizations());
      // reset the summarizations(group by) as the hierarchy dimensions
      logicalQuery.setSummarizations(hierarchyDimensions);

      // Process for metrics(selects)
      List<BusinessAssetTerm> metricsToRemove = new ArrayList<BusinessAssetTerm>();
      List<BusinessAssetTerm> metrics = logicalQuery.getMetrics();
      for (BusinessAssetTerm metric : metrics) {
         Colum column = (Colum) metric.getAssetEntityTerm().getAssetEntity();
         for (BusinessAssetTerm hDim : hierarchyDimensions) {
            Colum hCol = (Colum) hDim.getAssetEntityTerm().getAssetEntity();
            if (column.getName().equals(hCol.getName())) {
               updateHierarchyDimension(hDim, metric);
               metricsToRemove.add(metric);
               break;
            }
         }
      }

      // remove the extra metric
      for (BusinessAssetTerm removeBAT : metricsToRemove) {
         metrics.remove(removeBAT);
      }

      // Also remove the references from previous summarizations if they are of dimension type nature
      metricsToRemove = new ArrayList<BusinessAssetTerm>();
      for (BusinessAssetTerm metric : metrics) {
         Colum metricColumn = (Colum) metric.getAssetEntityTerm().getAssetEntity();

         // Skip if not dimension type nature
         if (!isDimensionType(metricColumn.getKdxDataType())) {
            continue;
         }

         for (BusinessAssetTerm summarization : summarizationsToRemove) {
            Colum summarizationColumn = (Colum) summarization.getAssetEntityTerm().getAssetEntity();
            if (metricColumn.getName().equals(summarizationColumn.getName())) {
               metricsToRemove.add(metric);
               break;
            }
         }
      }
      // remove the extra metric
      for (BusinessAssetTerm removeBAT : metricsToRemove) {
         metrics.remove(removeBAT);
      }

      // add the hierarchy dimensions at the beginning of the metrics list
      metrics.addAll(0, hierarchyDimensions);

      // Process for order by
      // reset the order by as the hierarchy dimensions
      List<StructuredOrderClause> hierarchyOrderBys = new ArrayList<StructuredOrderClause>();
      for (BusinessAssetTerm hDim : hierarchyDimensions) {
         hierarchyOrderBys.add(getStructuredOrderClause(hDim));
      }
      logicalQuery.setOrderClauses(hierarchyOrderBys);
   }

   private void updateHierarchyDimension (BusinessAssetTerm hierarchyDimension, BusinessAssetTerm metric) {
      hierarchyDimension.setBusinessTerm(metric.getBusinessTerm());
      hierarchyDimension.setAssetEntityTerm(metric.getAssetEntityTerm());
      hierarchyDimension.setAssetEntityDefinitions(metric.getAssetEntityDefinitions());
   }

   /**
    * This method resets the type of each column to the original type as described in the SWI, i.e, the deductions based
    * on the heuristics are removed. But if the column type is NULL, then the deduction is honoured.
    * 
    * @param reportMetaInfo
    */
   public void resetColumnTypes (ReportMetaInfo reportMetaInfo) {
      for (ReportColumnInfo colInfo : reportMetaInfo.getReportColumns()) {
         if (colInfo.isColumnTypeDeduced()) {
            ColumnType swiColType = ((Colum) colInfo.getBizAssetTerm().getAssetEntityTerm().getAssetEntity())
                     .getKdxDataType();
            if (!(swiColType == ColumnType.NULL || swiColType == null)) {
               colInfo.modifyColumnType(swiColType);
            }
         }
      }
   }

   public int getMaximumReportTitleLength () {
      Integer validLength = getAggregationConfigurationService().getReportTitleMaxAllowedLength();
      return validLength.intValue();
   }

   public String alterReportTitle (String reportTitle) {
      String appendWord = getAggregationConfigurationService().getReportTitleTextToAppend();
      if (ExecueCoreUtil.isNotEmpty(appendWord)) {
         reportTitle = reportTitle.trim() + " " + appendWord;
      }
      return reportTitle;
   }

   public boolean isDetailReportPathEnabled () {
      return getAggregationConfigurationService().enableDetailReports();
   }

   public Long getDetailReportsSelectionThreshold () {
      return getAggregationConfigurationService().getDetailReportSelectionThreshold();
   }

   public static String populatePlotAs (String columnType, ReportColumnInfo reportColumnInfo,
            ReportMetaInfo reportMetaInfo) {
      String plotType = "";
      // TODO: -JM- is it ok to skip the SL, RL and other types ?
      if (ColumnType.MEASURE.getValue().equals(columnType) || ColumnType.DIMENSION.getValue().equals(columnType)) {
         // same as ctype
         plotType = columnType;
      } else if (ColumnType.ID.getValue().equals(columnType)) {
         // deduce based on the stat and the aggregate query path
         if (reportMetaInfo.isGenerateBusinessSummary()) {
            // TODO: -JM- plotAs is MEASURE only when the ID column value has to be treated as a number, eg: when COUNT
            // is applied on the ID column; for all other cases treat it as STRING
            logger.info("BUSINESS SUMMARY / DATA BROWSER");
            BusinessStat businessStat = reportColumnInfo.getBizAssetTerm().getBusinessTerm().getBusinessStat();
            if (businessStat != null) {
               plotType = ColumnType.MEASURE.getValue();
            }
         } else if (reportMetaInfo.isGenerateDetailReport() && !reportMetaInfo.isGenerateOnlyDataBrowser()) {
            logger.info("DETAIL PATH");
            plotType = ColumnType.DIMENSION.getValue();
         }
      }
      return plotType;
   }

   // TODO: -JM- refactor the code to remove duplication - detailreportcolumntype and columntype
   // and avoid two different methods for the same purpose
   public void deduceDetailReportColumnTypes (ReportMetaInfo reportMetaInfo) {
      for (ReportColumnInfo reportColumnInfo : reportMetaInfo.getReportColumns()) {
         ColumnType columnType = reportColumnInfo.getDetailReportColumnType();
         if (columnType == ColumnType.NULL || columnType == null) {
            // rules for deducing the column type
            // Modify the column type based on the data type of the column when there is no data type defined
            // in SWI
            List<String> dimDataTypes = getAggregationConfigurationService().getDimensionDataTypes();
            List<String> measureDataTypes = getAggregationConfigurationService().getMeasureDataTypes();

            if (measureDataTypes.contains(reportColumnInfo.getColumn().getDataType().getValue())) {
               reportColumnInfo.modifyDetailReportColumnType(ColumnType.MEASURE);
            } else if (dimDataTypes.contains(reportColumnInfo.getColumn().getDataType().getValue())) {
               reportColumnInfo.modifyDetailReportColumnType(ColumnType.DIMENSION);
            } else {
               String defColumnTypeString = getAggregationConfigurationService().getDefaultColumnType();
               reportColumnInfo.modifyDetailReportColumnType(ColumnType.getColumnType(defColumnTypeString));
               if (logger.isDebugEnabled()) {
                  logger.debug("Unable to deduce the column type - marked as ID");
               }
            }
         }
      }
   }

   public void deduceColumnType (ReportColumnInfo reportColumnInfo) {
      ColumnType columnType = reportColumnInfo.getColumnType();
      if (columnType == ColumnType.NULL || columnType == null) {
         // rules for deducing the column type
         // Modify the column type based on the data type of the column when there is no data type defined
         // in SWI
         List<String> dimDataTypes = getAggregationConfigurationService().getDimensionDataTypes();
         List<String> measureDataTypes = getAggregationConfigurationService().getMeasureDataTypes();

         if (measureDataTypes.contains(reportColumnInfo.getColumn().getDataType().getValue())) {
            reportColumnInfo.modifyColumnType(ColumnType.MEASURE);
         } else if (dimDataTypes.contains(reportColumnInfo.getColumn().getDataType().getValue())) {
            reportColumnInfo.modifyColumnType(ColumnType.DIMENSION);
         } else {
            String defColumnTypeString = getAggregationConfigurationService().getDefaultColumnType();
            reportColumnInfo.modifyColumnType(ColumnType.getColumnType(defColumnTypeString));
            if (logger.isDebugEnabled()) {
               logger.debug("Unable to deduce the column type - marked as ID");
            }
         }
      }
   }

   public void reArrangeReportColumns (ReportMetaInfo clonedReportMetaInfo) {
      List<BusinessAssetTerm> currentMetrics = clonedReportMetaInfo.getAssetQuery().getLogicalQuery().getMetrics();
      Map<BusinessAssetTerm, ReportColumnInfo> reportColumnInfoByBATMap = getReportColumnInfoByBATMap(
               clonedReportMetaInfo, currentMetrics);

      List<ReportColumnInfo> newReportColumns = new ArrayList<ReportColumnInfo>();
      for (BusinessAssetTerm businessAssetTerm : currentMetrics) {

         ReportColumnInfo reportColumnInfo = reportColumnInfoByBATMap.get(businessAssetTerm);
         if (reportColumnInfo == null) {
            newReportColumns.add(prepareReportColumn(businessAssetTerm));
         } else {
            ReportColumnInfo clonedReportColumnInfo = prepareReportColumn(businessAssetTerm);
            populateClonedReportColumnInfo(clonedReportColumnInfo, reportColumnInfo);
            newReportColumns.add(clonedReportColumnInfo);
         }
      }

      // Reset the report columns
      clonedReportMetaInfo.setReportColumns(newReportColumns);
   }

   /**
    * Method to return the combination of list of list of measures each list having two entries. 
    * First one should be considered as dependent and second one should be considered as independent  
    * 
    * @param measures
    * @return List<List<BusinessAssetTerm>> combinations for scatter
    */
   public List<List<BusinessAssetTerm>> getCombinationMetricsForScatter (List<BusinessAssetTerm> metrics) {
      List<List<BusinessAssetTerm>> combinationMetricsList = new ArrayList<List<BusinessAssetTerm>>();

      List<BusinessAssetTerm> measures = getMeasures(metrics);

      // Plotting scatter is not feasible, hence break
      if (ExecueCoreUtil.isCollectionEmpty(measures) || measures.size() < 2) {
         return combinationMetricsList;
      }

      List<BusinessAssetTerm> dependentMetrics = getDependentMetrics(measures);
      List<BusinessAssetTerm> independentMetrics = getIndependentMetrics(measures);

      combinationMetricsList = getCombinationMetrics(dependentMetrics, independentMetrics);
      return combinationMetricsList;
   }

   public static List<BusinessAssetTerm> getMeasures (List<BusinessAssetTerm> metrics) {
      List<BusinessAssetTerm> measures = new ArrayList<BusinessAssetTerm>();
      for (BusinessAssetTerm businessAssetTerm : metrics) {
         if (businessAssetTerm.getBusinessTerm().getBusinessEntityTerm().isMeasurableEntity()) {
            measures.add(businessAssetTerm);
         }
      }
      return measures;
   }

   public static List<BusinessAssetTerm> getDimensions (List<ReportColumnInfo> reportColumnInfos) {

      List<BusinessAssetTerm> dimensions = new ArrayList<BusinessAssetTerm>();
      for (ReportColumnInfo reportColumnInfo : reportColumnInfos) {
         ColumnType columnType = reportColumnInfo.getColumnType();
         if (ColumnType.DIMENSION == columnType) {
            dimensions.add(reportColumnInfo.getBizAssetTerm());
         }
      }
      return dimensions;
   }

   public static int getMeasureCount (List<BusinessAssetTerm> metrics) {
      return getMeasures(metrics).size();
   }

   private List<List<BusinessAssetTerm>> getCombinationMetrics (List<BusinessAssetTerm> dependantMetrics,
            List<BusinessAssetTerm> independantMetrics) {

      List<List<BusinessAssetTerm>> combinationMetricsList = new ArrayList<List<BusinessAssetTerm>>();

      // No Dependent Variable present in the query
      if (ExecueCoreUtil.isCollectionEmpty(dependantMetrics) && ExecueCoreUtil.isCollectionNotEmpty(independantMetrics)) {
         populateDefaultCombinationMetrics(independantMetrics, combinationMetricsList);
      } else if (ExecueCoreUtil.isCollectionNotEmpty(dependantMetrics)
               && ExecueCoreUtil.isCollectionEmpty(independantMetrics)) {
         // No Independent Variable present in the query
         populateDefaultCombinationMetrics(dependantMetrics, combinationMetricsList);
      } else {
         // Dependent and Independent Variable(s) present in the query
         for (BusinessAssetTerm dependantMetric : dependantMetrics) {
            for (BusinessAssetTerm independantMetric : independantMetrics) {
               List<BusinessAssetTerm> combinationMetrics = new ArrayList<BusinessAssetTerm>();
               combinationMetrics.add(dependantMetric);
               combinationMetrics.add(independantMetric);
               combinationMetricsList.add(combinationMetrics);
            }
         }
      }

      return combinationMetricsList;
   }

   /**
    * @param metrics
    * @param combinationMetricsList
    */
   private void populateDefaultCombinationMetrics (List<BusinessAssetTerm> metrics,
            List<List<BusinessAssetTerm>> combinationMetricsList) {
      BusinessAssetTerm dependant = metrics.remove(0);

      for (BusinessAssetTerm metric : metrics) {
         List<BusinessAssetTerm> combinationMetrics = new ArrayList<BusinessAssetTerm>();
         combinationMetrics.add(dependant);
         combinationMetrics.add(metric);

         combinationMetricsList.add(combinationMetrics);
      }
   }

   private List<BusinessAssetTerm> getIndependentMetrics (List<BusinessAssetTerm> measures) {
      List<BusinessAssetTerm> independantMetrics = new ArrayList<BusinessAssetTerm>();
      if (ExecueCoreUtil.isCollectionEmpty(measures)) {
         return independantMetrics;
      }

      for (BusinessAssetTerm businessAssetTerm : measures) {
         if (!businessAssetTerm.getBusinessTerm().getBusinessEntityTerm().isDependantMeasure()) {
            independantMetrics.add(businessAssetTerm);
         }
      }
      return independantMetrics;
   }

   private List<BusinessAssetTerm> getDependentMetrics (List<BusinessAssetTerm> measures) {
      List<BusinessAssetTerm> dependantMetrics = new ArrayList<BusinessAssetTerm>();
      if (ExecueCoreUtil.isCollectionEmpty(measures)) {
         return dependantMetrics;
      }

      for (BusinessAssetTerm businessAssetTerm : measures) {
         if (businessAssetTerm.getBusinessTerm().getBusinessEntityTerm().isDependantMeasure()) {
            dependantMetrics.add(businessAssetTerm);
         }
      }
      return dependantMetrics;
   }

   public static String getReportColumnDescription (BusinessAssetTerm businessAssetTerm) {
      StringBuilder label = new StringBuilder();
      if (businessAssetTerm.getBusinessTerm().getBusinessStat() != null) {
         String statName = businessAssetTerm.getBusinessTerm().getBusinessStat().getStat().getStatType().getValue();
         label.append(WordUtils.capitalizeFully(statName)).append(" ");
      }
      Concept concept = (Concept) businessAssetTerm.getBusinessTerm().getBusinessEntityTerm().getBusinessEntity();
      label.append(concept.getDisplayName());
      // Fix for cohort - add "From Cohort" label to the column label
      if (businessAssetTerm.getBusinessTerm().isFromCohort()) {
         label.append(SPACE).append(COHORT_INDICATOR);
      }
      return label.toString();
   }

   public boolean skipUnivariants () {
      return getAggregationConfigurationService().skipUnivariants();
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

   public IAggregationConfigurationService getAggregationConfigurationService () {
      return aggregationConfigurationService;
   }

   public void setAggregationConfigurationService (IAggregationConfigurationService aggregationConfigurationService) {
      this.aggregationConfigurationService = aggregationConfigurationService;
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

   public IMappingRetrievalService getMappingRetrievalService () {
      return mappingRetrievalService;
   }

   public void setMappingRetrievalService (IMappingRetrievalService mappingRetrievalService) {
      this.mappingRetrievalService = mappingRetrievalService;
   }

   public IClientSourceDAO getClientSourceDAO () {
      return clientSourceDAO;
   }

   public void setClientSourceDAO (IClientSourceDAO clientSourceDAO) {
      this.clientSourceDAO = clientSourceDAO;
   }

   public RangeProcessor getRangeProcessor () {
      return rangeProcessor;
   }

   public void setRangeProcessor (RangeProcessor rangeProcessor) {
      this.rangeProcessor = rangeProcessor;
   }

   /**
    * @return the structuredQueryTransformerService
    */
   public IStructuredQueryTransformerService getStructuredQueryTransformerService () {
      return structuredQueryTransformerService;
   }

   /**
    * @param structuredQueryTransformerService the structuredQueryTransformerService to set
    */
   public void setStructuredQueryTransformerService (
            IStructuredQueryTransformerService structuredQueryTransformerService) {
      this.structuredQueryTransformerService = structuredQueryTransformerService;
   }
}