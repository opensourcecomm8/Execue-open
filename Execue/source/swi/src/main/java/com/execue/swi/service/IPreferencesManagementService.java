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

import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.KeyWord;
import com.execue.core.common.bean.entity.ParallelWord;
import com.execue.core.common.bean.entity.Profile;
import com.execue.core.common.bean.entity.RIParallelWord;
import com.execue.core.common.bean.entity.Range;
import com.execue.core.common.bean.entity.Type;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.ProfileType;
import com.execue.swi.exception.PreferencesException;

public interface IPreferencesManagementService {

   // *********** Methods related to Profiles **************************************************************
   public BusinessEntityDefinition createProfile (Profile profile, Type type, Long modelId, Long userId,
            CheckType hybridProfile, Long knowledgeId) throws PreferencesException;

   public BusinessEntityDefinition updateProfile (Profile profile, Long modelId) throws PreferencesException;

   public void cleanProfile (Profile profile) throws PreferencesException;

   // *********** Methods related to Ranges **************************************************************

   public void createRange (Long modelId, Long userId, Range range) throws PreferencesException;

   public void updateRange (Long modelId, Long userId, Range range) throws PreferencesException;

   // *********** Methods related to Riontoterm's,KeyWord's,Parallelword's ******************************************

   public void createRIParallelWords (List<RIParallelWord> riParallelWords) throws PreferencesException;

   public void createParallelWords (List<ParallelWord> parallelWords) throws PreferencesException;

   public void createKeyWord (KeyWord keyWord) throws PreferencesException;

   public ParallelWord createParallelWord (ParallelWord parallelWord) throws PreferencesException;

   public void updateParallelWords (List<ParallelWord> parallelWords) throws PreferencesException;

   public ParallelWord updateParallelWord (ParallelWord parallelWord) throws PreferencesException;

   public void deleteProfile (Profile profile, Long modelId, BusinessEntityDefinition businessEntityDefinition,
            ProfileType profileType) throws PreferencesException;

}
