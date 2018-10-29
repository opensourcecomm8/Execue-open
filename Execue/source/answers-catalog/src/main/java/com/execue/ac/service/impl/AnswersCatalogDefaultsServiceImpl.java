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

import java.util.ArrayList;
import java.util.List;

import com.execue.ac.configuration.IAnswersCatalogConfigurationService;
import com.execue.ac.exception.AnswersCatalogException;
import com.execue.ac.service.IAnswersCatalogDefaultsService;
import com.execue.core.common.bean.entity.DataSource;
import com.execue.core.common.bean.entity.Stat;
import com.execue.core.configuration.ICoreConfigurationService;
import com.execue.swi.exception.SWIException;
import com.execue.swi.service.IDataSourceSelectionService;
import com.execue.swi.service.IKDXRetrievalService;

public class AnswersCatalogDefaultsServiceImpl implements IAnswersCatalogDefaultsService {

   private IAnswersCatalogConfigurationService answersCatalogConfigurationService;
   private ICoreConfigurationService           coreConfigurationService;
   private IKDXRetrievalService                kdxRetrievalService;
   private IDataSourceSelectionService         dataSourceSelectionService;

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

   public List<Stat> getDefaultStatsForCubeCreation () throws AnswersCatalogException {
      List<Stat> defaultStats = new ArrayList<Stat>();
      List<String> statNames = getDefaultStatNamesForCubeCreation();
      try {
         for (String statName : statNames) {
            defaultStats.add(kdxRetrievalService.getStatByName(statName));
         }
      } catch (SWIException swiException) {
         throw new AnswersCatalogException(swiException.getCode(), swiException);
      }
      return defaultStats;
   }

   public List<String> getDefaultStatNamesForCubeCreation () throws AnswersCatalogException {
      return getAnswersCatalogConfigurationService().getDefaultStatNames();
   }

   public DataSource getDefaultTargetDataSource () throws AnswersCatalogException {
      DataSource targetDataSource = null;
      try {
         targetDataSource = getDataSourceSelectionService().getDataSourceForCatalogDatasets();
      } catch (SWIException swiException) {
         throw new AnswersCatalogException(swiException.getCode(), swiException);
      }
      return targetDataSource;
   }

   public String getStatisticsConceptName () throws AnswersCatalogException {
      return getAnswersCatalogConfigurationService().getStatsConceptName();
   }

   public String getCubeAllValue () throws AnswersCatalogException {
      return getCoreConfigurationService().getCubeAllValue();
   }

   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }

   public IDataSourceSelectionService getDataSourceSelectionService () {
      return dataSourceSelectionService;
   }

   public void setDataSourceSelectionService (IDataSourceSelectionService dataSourceSelectionService) {
      this.dataSourceSelectionService = dataSourceSelectionService;
   }

   public boolean isMartTargetDataSourceSameAsSourceDataSource () throws AnswersCatalogException {
      return getAnswersCatalogConfigurationService().isMartTargetDataSourceSameAsSourceDataSource();
   }

   public boolean isCubeTargetDataSourceSameAsSourceDataSource () throws AnswersCatalogException {
      return getAnswersCatalogConfigurationService().isCubeTargetDataSourceSameAsSourceDataSource();
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

}
