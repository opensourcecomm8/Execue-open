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


package com.execue.handler.swi;

import java.util.List;
import java.util.Set;

import com.execue.core.common.bean.Page;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.ConceptProfile;
import com.execue.core.common.bean.entity.Instance;
import com.execue.core.common.bean.entity.InstanceProfile;
import com.execue.core.common.bean.entity.KeyWord;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.common.bean.entity.Profile;
import com.execue.core.common.bean.entity.RIOntoTerm;
import com.execue.core.common.bean.entity.RIParallelWord;
import com.execue.core.common.bean.entity.Range;
import com.execue.core.common.bean.entity.RangeDetail;
import com.execue.core.common.bean.governor.BusinessEntityInfo;
import com.execue.core.common.type.BusinessEntityTermType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.ProfileType;
import com.execue.core.exception.ExeCueException;
import com.execue.core.exception.HandlerException;
import com.execue.handler.bean.UIParallelWord;
import com.execue.handler.bean.UIParallelWordValidationResult;
import com.execue.handler.bean.UIRangeDetail;

public interface IPreferencesServiceHandler {

   public void createProfile (Long modelId, Profile profile, CheckType hybridProfile) throws ExeCueException;

   public void updateProfile (Long modelId, Profile profile) throws ExeCueException;

   public void deleteProfiles (Long modelId, List<Profile> profiles) throws ExeCueException;

   public Profile getProfile (Long profileId, ProfileType profileType) throws ExeCueException;

   public List<Concept> getBaseProfiles (Long modelId) throws ExeCueException;

   public List<ConceptProfile> getConceptProfiles (Long modelId) throws ExeCueException;

   public List<InstanceProfile> getInstanceProfiles (Long modelId) throws ExeCueException;

   public List<Concept> getConceptsForConceptProfiles (Long modelId, CheckType hybridProfile) throws ExeCueException;

   public CheckType isConceptProfileHybridProfileType (Long modelId, ConceptProfile conceptProfile)
            throws ExeCueException;

   public List<Concept> getConceptsForInstanceProfiles (Long modelId) throws ExeCueException;

   public List<Instance> getInstancesForConcept (Long modelId, Long conceptId) throws ExeCueException;

   public Concept getConceptById (Long conceptId) throws ExeCueException;

   public Model getModelById (Long modelId) throws ExeCueException;

   public void createRange (Long modelId, Range range) throws ExeCueException;

   public void updateRange (Long modelId, Range range) throws ExeCueException;

   public Range getRangeById (Long rangeId) throws ExeCueException;

   public List<Range> getRanges (Long modelId) throws ExeCueException;

   public List<Concept> getConceptsForRanges (Long modelId) throws ExeCueException;

   public Range getExistingRangeForConcept (Long modelId, Long conceptBedId) throws ExeCueException;

   public void deleteRanges (List<Long> rangeIds) throws ExeCueException;

   public List<UIRangeDetail> transformRangeDetails (Set<RangeDetail> domainRangeDetails);

   public Set<RangeDetail> transformRangeDetails (List<UIRangeDetail> uiRangeDetails);

   public List<BusinessEntityInfo> getAllBusinessTermEntities (Long modelId,
            BusinessEntityTermType businessEntityTermType, Page page) throws HandlerException;

   public List<KeyWord> getAllExistingKeywords (Long modelId) throws HandlerException;

   public void deleteKeyWords (List<KeyWord> keyWords) throws HandlerException;

   public KeyWord getKeyWord (String keyWordName, Long modelId) throws HandlerException;

   public List<UIParallelWord> getParallelWordsById (Long keyWordId) throws HandlerException;

   // TODO: -JVK- check if the caller can provide the modelId or not
   public UIParallelWordValidationResult createUpdateKeywordParallelWords (Long modelId, KeyWord keyWord,
            List<UIParallelWord> parallelWords) throws HandlerException;

   public List<RIParallelWord> getRIParallelWordById (long keyWordId) throws HandlerException;

   public List<RIOntoTerm> getRIOntoTermsByKeyWord (String keyWord) throws HandlerException;

   public Range getSuggestedRangesForConceptByApplication (Long applicationId, Long modelId, Long ConceptBedId)
            throws ExeCueException;
}
