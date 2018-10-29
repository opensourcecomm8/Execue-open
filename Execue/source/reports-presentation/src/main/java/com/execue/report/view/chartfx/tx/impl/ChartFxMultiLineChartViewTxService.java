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


/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.execue.report.view.chartfx.tx.impl;

import java.util.Map;

import com.execue.core.common.bean.reports.ReportingConstants;
import com.execue.exception.ReportViewTxServiceException;
import com.execue.rules.bean.RuleParameters;
import com.execue.rules.exception.RuleException;
import com.softwarefx.chartfx.server.ChartServer;

/**
 * @author Owner
 */
public class ChartFxMultiLineChartViewTxService extends ChartFxSingleCanvasViewTxService {

   @Override
   public void applyViewRules (Map<String, Object> domainObject) throws ReportViewTxServiceException {
      super.applyViewRules(domainObject);
      setSeriesAxesAllocation((ChartServer) domainObject.get(ReportingConstants.CHART_SERVER_KEY));
      RuleParameters ruleParams = new RuleParameters();
      ruleParams.addInput(domainObject.get(ReportingConstants.CHART_SERVER_KEY));
      try {
         getRuleService().executeRule(getPresentationConfigurationService().getFirstSeriesOnLeftAxis(), ruleParams);
         adjustChartWidthAndHeightOnLegendTextSize((ChartServer) domainObject.get(ReportingConstants.CHART_SERVER_KEY));
      } catch (RuleException e) {
         throw new ReportViewTxServiceException(e.getCode(), e);
      }
   }
}