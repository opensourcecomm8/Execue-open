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
import java.util.List;

import com.execue.core.common.bean.AggregateQuery;
import com.execue.core.common.bean.ReportQueryData;
import com.execue.core.common.type.ReportType;
import com.execue.repoting.aggregation.bean.ReportMetaInfo;
import com.execue.repoting.aggregation.bean.ReportSelection;
import com.execue.repoting.aggregation.configuration.IAggregationConfigurationService;
import com.execue.repoting.aggregation.exception.ReportException;
import com.execue.repoting.aggregation.helper.ReportSelectionHelper;
import com.execue.repoting.aggregation.service.IReportSelectionService;
import com.execue.rules.bean.RuleParameters;
import com.execue.rules.exception.RuleException;
import com.execue.rules.service.IRuleService;

/**
 * @author kaliki
 * @since 4.0
 */

public class ReportSelectionServiceImpl implements IReportSelectionService {

   private IAggregationConfigurationService aggregationConfigurationService;
   private IRuleService                     ruleService;
   private ReportSelectionHelper            reportSelectionHelper;

   public IRuleService getRuleService () {
      return ruleService;
   }

   public void setRuleService (IRuleService ruleService) {
      this.ruleService = ruleService;
   }

   public ReportSelectionHelper getReportSelectionHelper () {
      return reportSelectionHelper;
   }

   public void setReportSelectionHelper (ReportSelectionHelper reportSelectionHelper) {
      this.reportSelectionHelper = reportSelectionHelper;
   }

   public List<ReportType> getReportTypes (ReportMetaInfo reportMetaInfo, AggregateQuery aggregateQuery,
            ReportQueryData queryData) throws ReportException {
      // populate the report selection object first
      ReportSelection reportSelection = reportSelectionHelper.populateReportSelection(reportMetaInfo, aggregateQuery,
               queryData);
      List<ReportType> reportTypes = new ArrayList<ReportType>();
      reportSelection.setReportTypes(reportTypes);

      // no need of elaborate report selection for data browser & top-bottom cases
      if (reportSelection.isDataBrowser()) {
         reportTypes.add(ReportType.DetailGrid);
         reportTypes.add(ReportType.DetailCsvFile);
         reportSelection.setReportTypes(reportTypes);
      } else if (reportSelection.isTopBottom()) {
         reportTypes.add(ReportType.Grid);
         reportTypes.add(ReportType.CsvFile);
         reportSelection.setReportTypes(reportTypes);
      } else {
         if (ruleService != null) { // rule based selection
            try {
               String reportSelectionRuleName = getAggregationConfigurationService().getReportSelectionRuleName();
               RuleParameters ruleParameters = new RuleParameters();
               ruleParameters.addInput(reportSelection);
               ruleService.executeRule(reportSelectionRuleName, ruleParameters);
            } catch (RuleException re) {
               re.printStackTrace();
            }
         } else { // java method based selection
            reportSelectionHelper.selectReportTypes(reportSelection, aggregateQuery);
         }
      }
      return reportSelection.getReportTypes();
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