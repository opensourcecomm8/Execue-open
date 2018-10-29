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


package com.execue.swi.service;

import java.util.List;

import com.execue.core.common.bean.entity.ConceptProfile;
import com.execue.core.common.bean.entity.InstanceProfile;
import com.execue.core.common.bean.entity.KeyWord;
import com.execue.core.common.bean.entity.ParallelWord;
import com.execue.core.common.bean.entity.RIParallelWord;
import com.execue.swi.exception.PreferencesException;

public interface IPreferencesDeletionService {

   public void deleteInstanceProfile (InstanceProfile instanceProfile) throws PreferencesException;

   public void deleteConceptProfile (ConceptProfile conceptProfile) throws PreferencesException;

   public void deleteRangeHeirarchy (Long rangeId) throws PreferencesException;

   public void deleteRIParallelWords (List<RIParallelWord> riParallelWords) throws PreferencesException;

   public void deleteKeywordHeirarchy (KeyWord keyWord) throws PreferencesException;

   public void deleteParallelWordHeirarchy (KeyWord keyWord, ParallelWord parallelWord) throws PreferencesException;

   public void deleteKeywordsForBusinessEntity (Long bedId) throws PreferencesException;
}
