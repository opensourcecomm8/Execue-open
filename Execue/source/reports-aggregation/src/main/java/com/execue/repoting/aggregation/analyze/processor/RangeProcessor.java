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

import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.DynamicRangeInput;
import com.execue.core.common.bean.entity.Range;
import com.execue.core.common.bean.entity.RangeDetail;
import com.execue.core.common.bean.querygen.SelectEntity;
import com.execue.core.common.type.AggregateQueryType;
import com.execue.core.common.type.ColumnType;
import com.execue.platform.exception.RangeSuggestionException;
import com.execue.platform.helper.RangeSuggestionServiceHelper;
import com.execue.platform.swi.IRangeSuggestionService;
import com.execue.repoting.aggregation.bean.ReportColumnInfo;
import com.execue.repoting.aggregation.bean.ReportMetaInfo;
import com.execue.repoting.aggregation.configuration.IAggregationConfigurationService;
import com.execue.repoting.aggregation.exception.ReportMetadataException;
import com.execue.repoting.aggregation.service.IReportMetaProcessor;
import com.execue.swi.exception.PreferencesException;
import com.execue.swi.service.IPreferencesRetrievalService;

/**
 * The RangeProcessor works out the Ranges that can be applied for a column. In case if there are no user-defined/system
 * specified range details available, <BR>
 * then the range details are deduced dynamically by this processor based on the values of the column obtained from the
 * asset.
 * 
 * @author kaliki
 * @since 4.0
 */

public class RangeProcessor implements IReportMetaProcessor {

   private static final Logger              logger = Logger.getLogger(RangeProcessor.class);
   private IAggregationConfigurationService aggregationConfigurationService;
   private IPreferencesRetrievalService     preferencesRetrievalService;
   private IRangeSuggestionService          dynamicRangeSuggestionService;

   /**
    * If the column becomes a DIMENSION from MEASURE after deduction then apply the user-defined ranges via the SWI
    * service.<BR>
    * If the user-defined ranges are unavailable, then dynamically deduce the ranges by using the minimum and maximum
    * values of that column
    */
   public boolean analyzeMetadata (ReportMetaInfo reportMetaInfo) throws ReportMetadataException {
      boolean flag = true;
      Range range = null;
      try {
         // Iterate through the list of columns and check to see if any column is eligible for application of ranges
         for (ReportColumnInfo reportColumnInfo : reportMetaInfo.getReportColumns()) {
            if (reportColumnInfo.isEligibleForRanges()) {
               if (logger.isDebugEnabled()) {
                  logger.debug("Processing ranges...");
               }
               // Obtain the KDX entity(concept) of the column
               // Concept concept = (Concept)
               // reportColumnInfo.getBizAssetTerm().getBusinessTerm().getBusinessEntityTerm()
               // .getBusinessEntity();
               Long conceptBedId = reportColumnInfo.getBizAssetTerm().getBusinessTerm().getBusinessEntityTerm()
                        .getBusinessEntityDefinitionId();
               try {
                  if (logger.isDebugEnabled()) {
                     logger.debug("Obtaining Range information for concept id : " + conceptBedId);
                  }
                  // Check to see if the concept has the user-defined ranges in the SWI
                  List<Range> ranges = getPreferencesRetrievalService()
                           .getUserDefinedRangeForConceptForDynamicRangeEvaluation(conceptBedId);
                  // TODO : -VG- check which range to pickup
                  if (ranges.size() > 0) {
                     range = ranges.get(0);
                  }
                  if (range != null) {
                     Set<RangeDetail> rangeDetails = range.getRangeDetails();
                     for (RangeDetail rangeDetail : rangeDetails) {
                        if (logger.isDebugEnabled()) {
                           logger.debug(rangeDetail.getOrder() + " [" + rangeDetail.getLowerLimit() + " - "
                                    + rangeDetail.getUpperLimit() + "]");
                        }
                     }
                     logger.debug("Setting the range inside the business term");
                     // Set the range object obtained via the service into the business term of the column
                     reportColumnInfo.getBizAssetTerm().getBusinessTerm().setRange(range);
                  } else { // There are no user-defined ranges, hence have to deduce dynamically
                     if (logger.isDebugEnabled()) {
                        logger.debug("No range details in SWI, hence marking the column for deriving the ranges...");
                     }
                     // mark the column for deriving the ranges dynamically
                     reportColumnInfo.setMarkedForRangeDerivation(true);
                  }
               } catch (PreferencesException e) {
                  e.printStackTrace();
                  throw new ReportMetadataException(10, "");
               }
            }
         }

         // Check for the dynamic ranges if the dynamic ranges are enabled
         if (getAggregationConfigurationService().enableDynamicRanges()
                  || reportMetaInfo.getSummaryPathType() == AggregateQueryType.SCATTER) {
            int bandCount = getAggregationConfigurationService().getDynamicRangesBandCount();
            for (int index = 0; index < reportMetaInfo.getReportColumns().size(); index++) {
               ReportColumnInfo reportColumnInfo = reportMetaInfo.getReportColumns().get(index);
               if (reportColumnInfo.isMarkedForRangeDerivation()) {
                  SelectEntity rangeSelectEntity = reportMetaInfo.getAssetQuery().getPhysicalQuery()
                           .getSelectEntities().get(index);
                  Long conceptBedId = reportColumnInfo.getBizAssetTerm().getBusinessTerm().getBusinessEntityTerm()
                           .getBusinessEntityDefinitionId();
                  Long sqlCountOfRangeColumn = reportColumnInfo.getCountSize();
                  if (logger.isDebugEnabled()) {
                     logger.debug("Distinct count of the range field : " + sqlCountOfRangeColumn);
                  }

                  DynamicRangeInput dynamicRangeInput = RangeSuggestionServiceHelper.populateDynamicRangeInput(
                           reportMetaInfo.getAssetQuery().getPhysicalQuery(), reportMetaInfo.getAssetQuery()
                                    .getLogicalQuery().getAsset(), rangeSelectEntity, conceptBedId, bandCount,
                           sqlCountOfRangeColumn);
                  range = getDynamicRangeSuggestionService().deduceRange(dynamicRangeInput);
                  if (range != null) {
                     reportColumnInfo.getBizAssetTerm().getBusinessTerm().setRange(range);
                  } else {
                     reportColumnInfo.setMarkedForRangeDerivation(false);
                     reportColumnInfo.modifyColumnType(ColumnType.MEASURE);
                  }
               }
            }
         }

         // Replace the count of the range column with the number of bands
         for (ReportColumnInfo reportColumnInfo : reportMetaInfo.getReportColumns()) {
            if (reportColumnInfo.getBizAssetTerm().getBusinessTerm().getRange() != null) {
               reportColumnInfo.setCountSize(reportColumnInfo.getBizAssetTerm().getBusinessTerm().getRange()
                        .getRangeDetails().size());
            }
         }
      } catch (RangeSuggestionException rangeSuggestionException) {
         logger.error("RangeSuggestionException in RangeProcessor", rangeSuggestionException);
         logger.error("Actual Error : [" + rangeSuggestionException.getCode() + "] "
                  + rangeSuggestionException.getMessage());
         logger.error("Cause : " + rangeSuggestionException.getCause());
         throw new ReportMetadataException(rangeSuggestionException.getCode(), rangeSuggestionException.getMessage(),
                  rangeSuggestionException.getCause());
      }
      return flag;
   }

   public IPreferencesRetrievalService getPreferencesRetrievalService () {
      return preferencesRetrievalService;
   }

   public void setPreferencesRetrievalService (IPreferencesRetrievalService preferencesRetrievalService) {
      this.preferencesRetrievalService = preferencesRetrievalService;
   }

   /**
    * @return the aggregationConfigurationService
    */
   public IAggregationConfigurationService getAggregationConfigurationService () {
      return aggregationConfigurationService;
   }

   /**
    * @param aggregationConfigurationService the aggregationConfigurationService to set
    */
   public void setAggregationConfigurationService (IAggregationConfigurationService aggregationConfigurationService) {
      this.aggregationConfigurationService = aggregationConfigurationService;
   }

   /**
    * @return the dynamicRangeSuggestionService
    */
   public IRangeSuggestionService getDynamicRangeSuggestionService () {
      return dynamicRangeSuggestionService;
   }

   /**
    * @param dynamicRangeSuggestionService the dynamicRangeSuggestionService to set
    */
   public void setDynamicRangeSuggestionService (IRangeSuggestionService dynamicRangeSuggestionService) {
      this.dynamicRangeSuggestionService = dynamicRangeSuggestionService;
   }
}