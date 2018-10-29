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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.BusinessStat;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.governor.BusinessAssetTerm;
import com.execue.core.common.bean.governor.StructuredCondition;
import com.execue.core.common.bean.governor.StructuredQuery;
import com.execue.core.common.type.OperandType;
import com.execue.core.common.util.ExecueBeanUtil;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.repoting.aggregation.bean.ReportColumnInfo;
import com.execue.repoting.aggregation.bean.ReportHierarchyColumnInfo;
import com.execue.repoting.aggregation.bean.ReportMetaHierarchyInfo;
import com.execue.repoting.aggregation.bean.ReportMetaInfo;
import com.execue.repoting.aggregation.exception.ReportMetadataException;
import com.execue.repoting.aggregation.service.IReportMetaProcessor;

/**
 * @author Nitesh
 */

public class HierarchyProcessor implements IReportMetaProcessor {

   private static final Logger logger = Logger.getLogger(HierarchyProcessor.class);

   /**
    * This method checks and sets the flags to check the paths that the current query can traverse
    */
   public boolean analyzeMetadata (ReportMetaInfo reportMetaInfo) throws ReportMetadataException {

      // Analyze for hierarchy report and set the flag accordingly
      if (ExecueCoreUtil.isCollectionNotEmpty(reportMetaInfo.getReportMetaHierarchyInfo())) {

         StructuredQuery structuredQuery = reportMetaInfo.getAssetQuery().getLogicalQuery();
         Asset asset = structuredQuery.getAsset();
         boolean isExecueOwnedCube = ExecueBeanUtil.isExecueOwnedCube(asset);

         // Rules to set the generate hierarchy to true
         if (reportMetaInfo.getAssetQuery().getLogicalQuery().getTopBottom() != null) {
            // Rule 1: Top Bottom queries can never produce drill downs
            reportMetaInfo.setGenerateHierarchyReport(false);
         } else if (reportMetaInfo.getAssetQuery().getLogicalQuery().getCohort() != null) {
            // Rule 2: Cohort Queries can never drill downs
            reportMetaInfo.setGenerateHierarchyReport(false);
         } else if (isExecueOwnedCube
                  && (multiValueConditionFound(reportMetaInfo.getReportMetaHierarchyInfo(), structuredQuery
                           .getConditions()) || uniqueMultipleStatsFound(structuredQuery.getMetrics()))) {
            // Cases where cube query can not produce Drill Downs
            // Rule 3A: When a condition is based on more than one value
            // Rule 3B: When there is more than one type of stat involved
            reportMetaInfo.setGenerateHierarchyReport(false);
         } else {
            reportMetaInfo.setGenerateHierarchyReport(true);
         }

         // Remove the report meta hierarchy info if hierarchy report is not possible
         if (!reportMetaInfo.isGenerateHierarchyReport()) {
            reportMetaInfo.setReportMetaHierarchyInfo(null);
         }

         // Filter the report meta hierarchy info 
         // if we have the condition at the lowest level of the hierarchy in case of cubes
         // if we have only one result count on the lowest level of the hierarchy
         if (reportMetaInfo.isGenerateHierarchyReport()) {

            List<ReportMetaHierarchyInfo> filteredReportMetaHierarchyInfos = filterReportMetaHierarchyInfos(
                     reportMetaInfo, structuredQuery.getConditions(), isExecueOwnedCube);
            reportMetaInfo.setReportMetaHierarchyInfo(filteredReportMetaHierarchyInfos);
            if (ExecueCoreUtil.isCollectionEmpty(filteredReportMetaHierarchyInfos)) {
               reportMetaInfo.setGenerateHierarchyReport(false);
            }
         }
      }
      return true;
   }

   private List<ReportMetaHierarchyInfo> filterReportMetaHierarchyInfos (ReportMetaInfo reportMetaInfo,
            List<StructuredCondition> conditions, boolean isExecueOwnedCube) {

      List<ReportMetaHierarchyInfo> reportMetaHierarchyInfos = reportMetaInfo.getReportMetaHierarchyInfo();

      Map<BusinessAssetTerm, StructuredCondition> structuredConditionByBAT = getStructuredConditionByBAT(conditions);

      Map<BusinessAssetTerm, ReportColumnInfo> reportColumnInfoByBAT = getReportColumnInfoByBATMap(reportMetaInfo
               .getReportColumns());

      List<ReportMetaHierarchyInfo> filteredReportMetaHierarchyInfos = new ArrayList<ReportMetaHierarchyInfo>();
      for (ReportMetaHierarchyInfo reportMetaHierarchyInfo : reportMetaHierarchyInfos) {

         List<ReportHierarchyColumnInfo> hierarchyColumns = reportMetaHierarchyInfo.getHierarchyColumns();
         ReportHierarchyColumnInfo lowestLevelHierarchy = getLowestLowelHierarchy(hierarchyColumns);

         int hierarchyConditionLowestLevel = getHierarchyConditionLowestLevel(structuredConditionByBAT,
                  hierarchyColumns);
         int lowestLevel = lowestLevelHierarchy.getLevel();

         // Filter the report meta hierarchy info 
         if (hierarchyConditionLowestLevel == lowestLevel) {
            // if we have the condition at the lowest level of the hierarchy and asset is execue owned cube
            if (isExecueOwnedCube) {
               continue;
            } else {
               BusinessAssetTerm bizAssetTerm = lowestLevelHierarchy.getBizAssetTerm();
               ReportColumnInfo reportColumnInfo = reportColumnInfoByBAT.get(bizAssetTerm);
               // if we have only one result count on the lowest level of the hierarchy
               if (reportColumnInfo.getCountSize() == 1) {
                  continue;
               }
            }
         }

         // Get the hierarchies from the lowest level to the end level in case of cubes. 
         if (isExecueOwnedCube) {
            List<ReportHierarchyColumnInfo> validHierarchyColumns = getValidReportHierarchyColumnInfos(
                     hierarchyColumns, hierarchyConditionLowestLevel);
            reportMetaHierarchyInfo.setHierarchyColumns(validHierarchyColumns);
         }

         filteredReportMetaHierarchyInfos.add(reportMetaHierarchyInfo);
      }

      return filteredReportMetaHierarchyInfos;
   }

   private Map<BusinessAssetTerm, ReportColumnInfo> getReportColumnInfoByBATMap (List<ReportColumnInfo> reportColumns) {
      Map<BusinessAssetTerm, ReportColumnInfo> reportColumnInfoByBAT = new HashMap<BusinessAssetTerm, ReportColumnInfo>();
      for (ReportColumnInfo reportColumnInfo : reportColumns) {
         reportColumnInfoByBAT.put(reportColumnInfo.getBizAssetTerm(), reportColumnInfo);
      }
      return reportColumnInfoByBAT;
   }

   private List<ReportHierarchyColumnInfo> getValidReportHierarchyColumnInfos (
            List<ReportHierarchyColumnInfo> hierarchyColumns, int hierarchyConditionLowestLevel) {
      // Get the hierarchies from the lowest level to the end level. 
      List<ReportHierarchyColumnInfo> validHierarchyColumns = new ArrayList<ReportHierarchyColumnInfo>();
      for (ReportHierarchyColumnInfo reportHierarchyColumnInfo : hierarchyColumns) {
         if (hierarchyConditionLowestLevel <= reportHierarchyColumnInfo.getLevel()) {
            validHierarchyColumns.add(reportHierarchyColumnInfo);
         }
      }

      // Reset the valid hierarchy columns
      return validHierarchyColumns;
   }

   /**
    * @param structuredConditionByBAT
    * @param hierarchyColumns
    * @return
    */
   private int getHierarchyConditionLowestLevel (Map<BusinessAssetTerm, StructuredCondition> structuredConditionByBAT,
            List<ReportHierarchyColumnInfo> hierarchyColumns) {
      // Get the lowest level of the hierarchy where condition is present
      int lowestLevel = 0;
      for (ReportHierarchyColumnInfo reportHierarchyColumnInfo : hierarchyColumns) {
         StructuredCondition structuredCondition = structuredConditionByBAT.get(reportHierarchyColumnInfo
                  .getBizAssetTerm());
         if (structuredCondition != null) {
            lowestLevel = reportHierarchyColumnInfo.getLevel();
         }
      }
      return lowestLevel;
   }

   private ReportHierarchyColumnInfo getLowestLowelHierarchy (List<ReportHierarchyColumnInfo> hierarchyColumns) {
      int lowestLevel = 0;
      ReportHierarchyColumnInfo lowestLevelHierarchy = null;
      for (ReportHierarchyColumnInfo reportHierarchyColumnInfo : hierarchyColumns) {
         int currentLevel = reportHierarchyColumnInfo.getLevel();
         if (lowestLevel < currentLevel) {
            lowestLevel = reportHierarchyColumnInfo.getLevel();
            lowestLevelHierarchy = reportHierarchyColumnInfo;
         }
      }
      return lowestLevelHierarchy;
   }

   private Map<BusinessAssetTerm, StructuredCondition> getStructuredConditionByBAT (List<StructuredCondition> conditions) {
      Map<BusinessAssetTerm, StructuredCondition> matchedSelectEntities = new HashMap<BusinessAssetTerm, StructuredCondition>();
      for (StructuredCondition structuredCondition : conditions) {
         matchedSelectEntities.put(structuredCondition.getLhsBusinessAssetTerm(), structuredCondition);
      }
      return matchedSelectEntities;
   }

   /**
    * Rule 3A: When a condition is based on more than one value
    * @param list 
    * @param structuredQuery
    * @return boolean true/false
    */
   private boolean multiValueConditionFound (List<ReportMetaHierarchyInfo> reportMetaHierarchyInfo,
            List<StructuredCondition> structuredConditions) {
      boolean multiValueConditionFound = false;
      Map<BusinessAssetTerm, List<ReportHierarchyColumnInfo>> reportHierarchyColumnInfosByBAT = getReportHierarchyColumnInfosByBAT(reportMetaHierarchyInfo);

      for (StructuredCondition structuredCondition : structuredConditions) {

         List<ReportHierarchyColumnInfo> reportHierarchyColumnInfos = reportHierarchyColumnInfosByBAT
                  .get(structuredCondition.getLhsBusinessAssetTerm());

         // Skip checking for conditions based on hierarchies
         if (ExecueCoreUtil.isCollectionNotEmpty(reportHierarchyColumnInfos)) {
            continue;
         }
         if (OperandType.VALUE == structuredCondition.getOperandType() && structuredCondition.getRhsValues().size() > 1) {
            multiValueConditionFound = true;
            break;
         }
      }
      return multiValueConditionFound;
   }

   private Map<BusinessAssetTerm, List<ReportHierarchyColumnInfo>> getReportHierarchyColumnInfosByBAT (
            List<ReportMetaHierarchyInfo> reportMetaHierarchyInfos) {
      Map<BusinessAssetTerm, List<ReportHierarchyColumnInfo>> reportHierarchyColumnInfosByBAT = new HashMap<BusinessAssetTerm, List<ReportHierarchyColumnInfo>>();

      for (ReportMetaHierarchyInfo reportMetaHierarchyInfo : reportMetaHierarchyInfos) {
         List<ReportHierarchyColumnInfo> hierarchyColumns = reportMetaHierarchyInfo.getHierarchyColumns();
         for (ReportHierarchyColumnInfo reportHierarchyColumnInfo : hierarchyColumns) {
            BusinessAssetTerm bizAssetTerm = reportHierarchyColumnInfo.getBizAssetTerm();
            List<ReportHierarchyColumnInfo> reportHierarchyColumnInfos = reportHierarchyColumnInfosByBAT
                     .get(bizAssetTerm);
            if (reportHierarchyColumnInfos == null) {
               reportHierarchyColumnInfos = new ArrayList<ReportHierarchyColumnInfo>();
               reportHierarchyColumnInfosByBAT.put(bizAssetTerm, reportHierarchyColumnInfos);
            }
            reportHierarchyColumnInfos.add(reportHierarchyColumnInfo);
         }
      }

      return reportHierarchyColumnInfosByBAT;
   }

   /**
    * Rule 3B: When there is more than one type of stat involved
    * @param metrics
    * @return boolean true/false
    */
   private boolean uniqueMultipleStatsFound (List<BusinessAssetTerm> metrics) {
      Set<String> stats = new HashSet<String>();
      for (BusinessAssetTerm businessAssetTerm : metrics) {
         BusinessStat businessStat = businessAssetTerm.getBusinessTerm().getBusinessStat();
         if (businessStat != null) {
            stats.add(businessStat.getStat().getStatType().getValue());
         }
      }
      if (stats.size() > 1) {
         return true;
      }
      return false;
   }

}