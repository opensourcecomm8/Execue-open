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


package com.execue.ac.service.impl;

import com.execue.ac.bean.AnswersCatalogConfigurationContext;
import com.execue.ac.configuration.IAnswersCatalogConfigurationService;
import com.execue.core.common.type.DataType;
import com.execue.core.configuration.ICoreConfigurationService;

/**
 * This service contains methods to populate the context common to answers catalog
 * 
 * @author Vishay
 * @version 1.0
 * @since 13/01/2011
 */
public abstract class AnswersCatalogContextPopulationServiceImpl {

   private IAnswersCatalogConfigurationService answersCatalogConfigurationService;
   private ICoreConfigurationService           coreConfigurationService;

   /**
    * This method populates the answers catalog configuration context which is common to both of cube and mart. It reads
    * configuration file and populates the bean object.
    * 
    * @return answersCatalogConfigurationContext
    */
   protected AnswersCatalogConfigurationContext populateAnswersCatalogConfigurationContext () {
      int maxDbObjectLength = getCoreConfigurationService().getMaxDBObjectLength();
      String statisticsConceptName = getAnswersCatalogConfigurationService().getStatsConceptName();
      DataType measureColumnDataType = DataType.getType(getAnswersCatalogConfigurationService()
               .getMeasureColumnDataType());
      DataType frequencyColumnDataType = DataType.getType(getAnswersCatalogConfigurationService()
               .getFrequencyColumnDataType());
      int measurePrecisionValue = getAnswersCatalogConfigurationService().getMeasureColumnPrecision();
      int measureScaleValue = getAnswersCatalogConfigurationService().getMeasureColumnScale();
      AnswersCatalogConfigurationContext answersCatalogConfigurationContext = new AnswersCatalogConfigurationContext();
      answersCatalogConfigurationContext.setMaxDBObjectLength(maxDbObjectLength);
      answersCatalogConfigurationContext.setStatisticsConceptName(statisticsConceptName);
      answersCatalogConfigurationContext.setMeasureColumnDataType(measureColumnDataType);
      answersCatalogConfigurationContext.setFrequencyColumnDataType(frequencyColumnDataType);
      answersCatalogConfigurationContext.setMeasurePrecisionValue(measurePrecisionValue);
      answersCatalogConfigurationContext.setMeasureScaleValue(measureScaleValue);
      return answersCatalogConfigurationContext;
   }

   /**
    * @return the answersCatalogConfigurationService
    */
   public IAnswersCatalogConfigurationService getAnswersCatalogConfigurationService () {
      return answersCatalogConfigurationService;
   }

   /**
    * @param answersCatalogConfigurationService the answersCatalogConfigurationService to set
    */
   public void setAnswersCatalogConfigurationService (
            IAnswersCatalogConfigurationService answersCatalogConfigurationService) {
      this.answersCatalogConfigurationService = answersCatalogConfigurationService;
   }

   public ICoreConfigurationService getCoreConfigurationService () {
      return coreConfigurationService;
   }

   public void setCoreConfiguration (ICoreConfigurationService coreConfigurationService) {
      this.coreConfigurationService = coreConfigurationService;
   }

   /**
    * @param coreConfigurationService the coreConfigurationService to set
    */
   public void setCoreConfigurationService (ICoreConfigurationService coreConfigurationService) {
      this.coreConfigurationService = coreConfigurationService;
   }
}
