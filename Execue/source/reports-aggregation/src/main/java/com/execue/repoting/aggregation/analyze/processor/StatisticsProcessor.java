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

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.BusinessStat;
import com.execue.core.common.bean.BusinessTerm;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.Instance;
import com.execue.core.common.bean.entity.Stat;
import com.execue.core.common.type.BusinessEntityType;
import com.execue.core.common.type.ColumnType;
import com.execue.core.common.type.StatType;
import com.execue.core.common.util.ExecueBeanUtil;
import com.execue.core.configuration.ICoreConfigurationService;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.repoting.aggregation.bean.ReportColumnInfo;
import com.execue.repoting.aggregation.bean.ReportMetaInfo;
import com.execue.repoting.aggregation.configuration.IAggregationConfigurationService;
import com.execue.repoting.aggregation.exception.ReportMetadataException;
import com.execue.repoting.aggregation.helper.ReportAggregationHelper;
import com.execue.repoting.aggregation.service.IReportMetaProcessor;
import com.execue.swi.exception.KDXException;
import com.execue.swi.service.IKDXRetrievalService;

/**
 * Each of the column is processed and ensured that all the MEASURES are aggregated by applying the statistics that are
 * being defined in the SWI <BR>
 * or declared by the user and if none of them are available then the system default statistic is applied.
 * 
 * @author kaliki
 * @since 4.0
 */

public class StatisticsProcessor implements IReportMetaProcessor {

   private static final Logger              logger = Logger.getLogger(StatisticsProcessor.class);
   private IAggregationConfigurationService aggregationConfigurationService;
   private ICoreConfigurationService        coreConfigurationService;
   private IKDXRetrievalService             kdxRetrievalService;

   /**
    * This method handles the application of aggregated functions on columns.<BR>
    * MEASURE - apply the user set statistic. If it is missing, then apply the statistics defined in SWI, <BR>
    * and if SWI does not contain the statistics then apply the system wide default statistic <BR>
    * DIMENSION - do not apply any statistics.<BR>
    * ID - apply COUNT as the statistic overriding any other statistic present.
    */
   @SuppressWarnings ("unused")
   public boolean analyzeMetadata (ReportMetaInfo reportMetaInfo) throws ReportMetadataException {
      boolean flag = true;

      // Check if the asset is a CUBE
      Asset asset = reportMetaInfo.getAssetQuery().getLogicalQuery().getAsset();
      boolean isExecueOwnedCube = ExecueBeanUtil.isExecueOwnedCube(asset);

      for (ReportColumnInfo reportColumnInfo : reportMetaInfo.getReportColumns()) {
         ColumnType columnType = reportColumnInfo.getColumnType();
         BusinessTerm bizTerm = reportColumnInfo.getBizAssetTerm().getBusinessTerm();
         // TODO: Revisit this rule - For ID column, override all the statistics and apply only COUNT as COUNT may not
         // be the relevant aggregate function in case the user asks
         // for something different
         // NOTE: Do not set the count business stat if it is execue owned cube 
         // as we get that count information from the frequency column of the cube  
         if (ColumnType.ID.equals(columnType) && !isExecueOwnedCube) {
            Set<BusinessStat> businessStats = new HashSet<BusinessStat>();
            BusinessStat businessStat = new BusinessStat();
            businessStat.setStat(getStat(StatType.getStatType(getAggregationConfigurationService().getCountStat())));
            businessStat.setRequestedByUser(false);
            businessStats.add(businessStat);
            reportColumnInfo.setBusinessStats(businessStats);
            reportColumnInfo.setStatsDeduced(true);
         } else if (ColumnType.MEASURE.equals(columnType)) {
            // -Vishy
            // Check the user defined Statistics on the business term first,
            // if they don't exist, apply the default statistics
            if (bizTerm.getBusinessStat() != null && bizTerm.getBusinessStat().isRequestedByUser()) {
               Set<BusinessStat> businessStats = new HashSet<BusinessStat>();
               BusinessStat businessStat = bizTerm.getBusinessStat();
               businessStats.add(businessStat);
               reportColumnInfo.setBusinessStats(businessStats);
               if (logger.isDebugEnabled()) {
                  logger.debug("Setting the user specified stats for "
                           + ReportAggregationHelper.getEntityName(reportColumnInfo.getBizAssetTerm()));
               }
            } else {
               Set<BusinessStat> businessStats = new HashSet<BusinessStat>();
               Concept concept = null;
               if (BusinessEntityType.CONCEPT.equals(bizTerm.getBusinessEntityTerm().getBusinessEntityType())) {
                  concept = (Concept) bizTerm.getBusinessEntityTerm().getBusinessEntity();
               } else if (BusinessEntityType.CONCEPT_LOOKUP_INSTANCE.equals(bizTerm.getBusinessEntityTerm()
                        .getBusinessEntityType())) {
                  concept = ((Instance) bizTerm.getBusinessEntityTerm().getBusinessEntity()).getParentConcept();
               }
               if (concept != null) {
                  Set<Stat> stats = concept.getStats();
                  if (ExecueCoreUtil.isCollectionNotEmpty(stats)) {
                     for (Stat stat : stats) {
                        BusinessStat businessStat = new BusinessStat();
                        businessStat.setStat(stat);
                        businessStat.setRequestedByUser(false);
                        businessStats.add(businessStat);
                     }
                     if (logger.isDebugEnabled()) {
                        logger.debug("Added the user specified default stats for " + concept.getName());
                     }
                  } else {
                     // apply the system default statistic
                     BusinessStat defBusinessStat = new BusinessStat();
                     defBusinessStat.setStat(getStat(StatType.getStatType(getCoreConfigurationService()
                              .getSystemLevelDefaultStat())));
                     defBusinessStat.setRequestedByUser(false);
                     businessStats.add(defBusinessStat);
                     if (logger.isDebugEnabled()) {
                        logger.debug("Added the system default stat for " + concept.getName());
                     }
                  }
                  reportColumnInfo.setBusinessStats(businessStats);
                  reportColumnInfo.setStatsDeduced(true);
               }
            }
         } else {
            // do nothing for DIMENSION
         }
      }
      return flag;
   }

   /**
    * Retrieves the Statistic as a complete object by the StatType
    * 
    * @param statType
    */
   private Stat getStat (StatType statType) {
      Stat stat = null;
      try {
         stat = kdxRetrievalService.getStatByName(statType.getValue());
      } catch (KDXException e) {
         e.printStackTrace();
      }
      return stat;
   }

   /**
    * @return the coreConfigurationService
    */
   public ICoreConfigurationService getCoreConfigurationService () {
      return coreConfigurationService;
   }

   /**
    * @param coreConfigurationService the coreConfigurationService to set
    */
   public void setCoreConfigurationService (ICoreConfigurationService coreConfigurationService) {
      this.coreConfigurationService = coreConfigurationService;
   }

   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
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

}