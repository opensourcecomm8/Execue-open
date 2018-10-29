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


package com.execue.repoting.aggregation.analyze.processor;

import org.apache.log4j.Logger;

import com.execue.core.common.type.ColumnType;
import com.execue.core.common.type.DataType;
import com.execue.repoting.aggregation.bean.ReportColumnInfo;
import com.execue.repoting.aggregation.bean.ReportMetaInfo;
import com.execue.repoting.aggregation.bean.ReportSelectionConstants;
import com.execue.repoting.aggregation.exception.ReportMetadataException;
import com.execue.repoting.aggregation.helper.ReportAggregationHelper;
import com.execue.repoting.aggregation.service.IReportMetaProcessor;

/**
 * The ReductionProcessor analyzes each of the column entity present in the metrics section(SELECT section) <BR>
 * of the StructuredQuery and deduces the type of each entity(ID or MEASURE or DIMENSION) based on various rules.
 * 
 * @author kaliki
 * @since 4.0
 */

public class ReductionProcessor implements IReportMetaProcessor {

   private static final Logger     logger = Logger.getLogger(RangeProcessor.class);
   private ReportAggregationHelper reportAggregationHelper;

   public ReportAggregationHelper getReportAggregationHelper () {
      return reportAggregationHelper;
   }

   public void setReportAggregationHelper (ReportAggregationHelper reportAggregationHelper) {
      this.reportAggregationHelper = reportAggregationHelper;
   }

   /**
    * This method analyzes the various ReportColumnInfo objects for deducing the column type
    * 
    * @param ReportMetaInfo
    *           contains all the columns forming the metrics section of the StructuredQuery
    */
   @SuppressWarnings ("unused")
   public boolean analyzeMetadata (ReportMetaInfo reportMetaInfo) throws ReportMetadataException {
      boolean flag = true;
      boolean isPopulationSet = false;
      long columnUniqueCount = 0;
      long totalCount = 0;
      // Iterate through all the column objects to check the column type that has been declared in the SWI
      for (ReportColumnInfo reportColumnInfo : reportMetaInfo.getReportColumns()) {
         ColumnType columnType = reportColumnInfo.getColumnType();
         columnUniqueCount = reportColumnInfo.getCountSize();
         totalCount = reportMetaInfo.getTotalCount();
         if (logger.isDebugEnabled()) {
            logger.debug("Column Type : " + columnType);
         }
         // Check if the current column is population term or not. If yes set to ID
         if (!isPopulationSet && reportAggregationHelper.isPopulationTerm(reportMetaInfo, reportColumnInfo)) {
            isPopulationSet = true;
            if (!ColumnType.ID.equals(reportColumnInfo.getColumnType())) {
               reportColumnInfo.modifyColumnType(ColumnType.ID);
               if (logger.isDebugEnabled()) {
                  logger.debug("Found Population term : deduced the column type as ID");
               }
            }
         } else {
            // If the declared SWI column type is DIMENSION, no need for any further deductions, just skip
            if (ColumnType.DIMENSION.equals(columnType) || ColumnType.RANGE_LOOKUP.equals(columnType)
                     || ColumnType.SIMPLE_LOOKUP.equals(columnType)) {
               if (logger.isDebugEnabled()) {
                  logger.debug(ReportAggregationHelper.getEntityName(reportColumnInfo.getBizAssetTerm())
                           + " - type is DIMENSION; skipping deduction of column type");
               }
               reportColumnInfo.modifyColumnType(ColumnType.DIMENSION);
            } else if (ColumnType.ID.equals(columnType) || ColumnType.MEASURE.equals(columnType)) {
               // 1. If the column type is MEASURE, then check if the column is present in the summarizations section
               // and if present deduce the column type as DIMENSION
               // 2. Also if the column has been deduced as a DIMENSION from MEASURE, then it becomes eligible for Range
               // application
               if (ColumnType.MEASURE.equals(columnType)
                        && reportMetaInfo.getAssetQuery().getLogicalQuery().getSummarizations() != null
                        && reportAggregationHelper.isPresentInSummarizations(reportMetaInfo, reportColumnInfo)) {
                  reportColumnInfo.modifyColumnType(ColumnType.DIMENSION);
                  reportColumnInfo.setEligibleForRanges(true);
                  if (logger.isDebugEnabled()) {
                     logger.debug(ReportAggregationHelper.getEntityName(reportColumnInfo.getBizAssetTerm())
                              + " is present in summarization section - Marking as DIMENSION");
                  }
               }
            } else {
               // 31-JUL-2009 : Aggregation Rule Change
               // Check if the column's data type is DATE or if the column's mapped concept has been declared as a
               // distribution concept
               if (DataType.TIME.equals(reportColumnInfo.getColumn().getDataType())
                        || DataType.DATE.equals(reportColumnInfo.getColumn().getDataType())
                        || reportColumnInfo.getBizAssetTerm().getBusinessTerm().isFromDistribution()) {
                  if (logger.isDebugEnabled()) {
                     logger
                              .debug(ReportAggregationHelper.getEntityName(reportColumnInfo.getBizAssetTerm())
                                       + " is not ID/DIM/MEA - deduced as DIMENSION - column's data type is DATE or mapped concept has been declared as a distribution concept");
                  }
                  reportColumnInfo.modifyColumnType(ColumnType.DIMENSION);
               } else {
                  // if the KDX type is not ID or MEASURE or DIMENSION, deduce the column type by obtaining the
                  // distinct count of the column. After obtaining the count, check if the count is greater than the
                  // declared maximum unique values a column can have to be deduced as a DIMENSION.
                  // If the count is greater, then deduce it as a MEASURE else as a DIMENSION
                  if (reportAggregationHelper.isEligibleAsDimension(columnUniqueCount, totalCount)) {
                     if (logger.isDebugEnabled()) {
                        logger.debug(ReportAggregationHelper.getEntityName(reportColumnInfo.getBizAssetTerm())
                                 + " is not ID/DIM/MEA - deduced as DIMENSION by running the eligibility logic");
                     }
                     reportColumnInfo.modifyColumnType(ColumnType.DIMENSION);
                  } else {
                     // 19-JUN-2009 : Aggregation Rule Change
                     // Modify the column type based on the data type of the column when there is no data type defined
                     // in SWI
                     reportAggregationHelper.deduceColumnType(reportColumnInfo);

                     if (logger.isDebugEnabled()) {
                        logger.debug(ReportAggregationHelper.getEntityName(reportColumnInfo.getBizAssetTerm())
                                 + " is not ID/DIM/MEA - deduced as " + reportColumnInfo.getColumnType().getValue()
                                 + "by running the counts query");
                     }
                     // Check to see if this column is present in the GROUP BY section and if present, mark as a
                     // DIMENSION
                     if (reportAggregationHelper.isPresentInSummarizations(reportMetaInfo, reportColumnInfo)) {
                        reportColumnInfo.modifyColumnType(ColumnType.DIMENSION);
                        if (logger.isDebugEnabled()) {
                           logger.debug(ReportAggregationHelper.getEntityName(reportColumnInfo.getBizAssetTerm())
                                    + " is present in summarization section - Marking as DIMENSION");
                        }
                     }
                  }
               }
            }
            if (ColumnType.DIMENSION.equals(columnType) || ColumnType.RANGE_LOOKUP.equals(columnType)
                     || ColumnType.SIMPLE_LOOKUP.equals(columnType)) {
               // 19-JUN-2009 : Aggregation Rule Change
               // If the count is greater than the max value then mark it as id and remove the term from group by and
               // order by sections

               // 31-JUL-2009 : Aggregation Rule Change
               // Check if the column's data type is DATE or if the column's mapped concept has been declared as a
               // distribution concept
               // TODO: -JM- 26Jan2011 Is this if loop necessary here ? Does it make more sense to have this check in
               // the else{} above
               if (DataType.TIME.equals(reportColumnInfo.getColumn().getDataType())
                        || DataType.DATE.equals(reportColumnInfo.getColumn().getDataType())
                        || reportColumnInfo.getBizAssetTerm().getBusinessTerm().isFromDistribution()) {
                  if (logger.isDebugEnabled()) {
                     logger
                              .debug(ReportAggregationHelper.getEntityName(reportColumnInfo.getBizAssetTerm())
                                       + " is not ID/DIM/MEA - deduced as DIMENSION - column's data type is DATE or mapped concept has been declared as a distribution concept");
                  }
                  reportColumnInfo.modifyColumnType(ColumnType.DIMENSION);
               }
               // 26Jan2011 -JM- Added to check and set the flag if the current metric is part of the "group by"
               // requested by user
               reportAggregationHelper.isPresentInSummarizations(reportMetaInfo, reportColumnInfo);
            }
         }
      }
      // 10% rule to check if Detail Report needs to be specially handled or not
      // reportAggregationHelper.processDetailReport(reportMetaInfo);
      // 15-SEP-2009 : Aggregation Rule Change
      // check against threshold and process the set of dimensions to remove the dimension having maximum members
      // 19-FEB-2010 : Handle large group by only when the total record count is greater than the HIGH_RECORDS_LIMIT
      if (reportMetaInfo.getTotalCount() > ReportSelectionConstants.HIGH_RECORDS_LIMIT) {
         reportAggregationHelper.handleLargeGroupBy(reportMetaInfo);
      }
      return flag;
   }
}