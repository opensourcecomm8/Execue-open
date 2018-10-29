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


package com.execue.swi.service.impl;

import java.util.List;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.entity.ConceptProfile;
import com.execue.core.common.bean.entity.InstanceProfile;
import com.execue.core.common.bean.entity.KeyWord;
import com.execue.core.common.bean.entity.ParallelWord;
import com.execue.core.common.bean.entity.RIParallelWord;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.swi.dataaccess.IPreferencesDataAccessManager;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.PreferencesException;
import com.execue.swi.service.IBusinessEntityMaintenanceService;
import com.execue.swi.service.IPreferencesDeletionService;
import com.execue.swi.service.IPreferencesRetrievalService;

public class PreferencesDeletionServiceImpl extends KDXCommonServiceImpl implements IPreferencesDeletionService {

   private static final Logger               log = Logger.getLogger(PreferencesDeletionServiceImpl.class);

   private IPreferencesDataAccessManager     preferencesDataAccessManager;

   private IBusinessEntityMaintenanceService businessEntityMaintenanceService;

   private IPreferencesRetrievalService      preferencesRetrievalService;

   public void deleteConceptProfile (ConceptProfile conceptProfile) throws PreferencesException {
      getPreferencesDataAccessManager().deleteConceptProfile(conceptProfile);
   }

   public void deleteInstanceProfile (InstanceProfile instanceProfile) throws PreferencesException {
      getPreferencesDataAccessManager().deleteInstanceProfile(instanceProfile);
   }

   public void deleteRIParallelWords (List<RIParallelWord> riParallelWords) throws PreferencesException {
      getPreferencesDataAccessManager().deleteRIParallelWords(riParallelWords);
   }

   public void deleteKeywordsForBusinessEntity (Long bedId) throws PreferencesException {
      List<KeyWord> keywords = getPreferencesRetrievalService().getKeyWordsByBedId(bedId);
      if (ExecueCoreUtil.isCollectionNotEmpty(keywords)) {
         for (KeyWord keyWord : keywords) {
            deleteKeywordHeirarchy(keyWord);
         }
      }
   }

   public void deleteParallelWordHeirarchy (KeyWord keyWord, ParallelWord parallelWord) throws PreferencesException {
      try {
         List<RIParallelWord> riParallelWords = getPreferencesRetrievalService().getRIParallelWordsForKeyWord(
                  keyWord.getId());
         if (ExecueCoreUtil.isCollectionNotEmpty(riParallelWords)) {
            deleteRIParallelWords(riParallelWords);
         }
         getBusinessEntityMaintenanceService()
                  .deleteBusinessEntityMaintenanceDetailsByEntityBedId(parallelWord.getId());
         getPreferencesDataAccessManager().deleteParallelWord(parallelWord);
      } catch (KDXException e) {
         throw new PreferencesException(e.getCode(), e);
      }
   }

   public void deleteRangeHeirarchy (Long rangeId) throws PreferencesException {
      getPreferencesDataAccessManager().deleteRange(rangeId);
   }

   public void deleteKeywordHeirarchy (KeyWord keyWord) throws PreferencesException {
      List<ParallelWord> parallelWords = getPreferencesRetrievalService().getParallelWordsForKeyWord(keyWord.getId());
      if (ExecueCoreUtil.isCollectionNotEmpty(parallelWords)) {
         for (ParallelWord parallelWord : parallelWords) {
            deleteParallelWordHeirarchy(keyWord, parallelWord);
         }
      }
      getPreferencesDataAccessManager().deleteKeyWord(keyWord);
   }

   public IBusinessEntityMaintenanceService getBusinessEntityMaintenanceService () {
      return businessEntityMaintenanceService;
   }

   public void setBusinessEntityMaintenanceService (IBusinessEntityMaintenanceService businessEntityMaintenanceService) {
      this.businessEntityMaintenanceService = businessEntityMaintenanceService;
   }

   public IPreferencesRetrievalService getPreferencesRetrievalService () {
      return preferencesRetrievalService;
   }

   public void setPreferencesRetrievalService (IPreferencesRetrievalService preferencesRetrievalService) {
      this.preferencesRetrievalService = preferencesRetrievalService;
   }

   public IPreferencesDataAccessManager getPreferencesDataAccessManager () {
      return preferencesDataAccessManager;
   }

   public void setPreferencesDataAccessManager (IPreferencesDataAccessManager preferencesDataAccessManager) {
      this.preferencesDataAccessManager = preferencesDataAccessManager;
   }

}
