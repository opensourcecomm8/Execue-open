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


package com.execue.swi.dataaccess;

import java.util.List;

import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.ConceptProfile;
import com.execue.core.common.bean.entity.Instance;
import com.execue.core.common.bean.entity.InstanceProfile;
import com.execue.core.common.bean.entity.Stat;
import com.execue.swi.exception.SWIException;

public interface IQueryInterfaceSuggestDataAccessManager {

   /**
    * used for QueryInteface metrics suggestion. Valid return type are CONCEPT, PROFILE, FORMULA Join Concept and
    * profile tables
    */
   // public List<SuggestTerm> suggestBTsForSelect (String searchString, Long userId) throws DataAccessException;
   /**
    * used for QueryInteface metrics suggestion. Valid return type are CONCEPT, PROFILE Join Concept with related Stat
    * Name
    */
   public List<Concept> suggestBTsForSelect (String searchString, String statName, Long userId, Long modelId)
            throws SWIException;

   /**
    * used for QueryInteface population suggestion. Valid return type are CONCEPT (Grain) Join Concept and Asset_Grain
    */
   public List<Concept> suggestBTsForPopulation (String searchString, Long userId, Long modelId) throws SWIException;

   /**
    * used for QueryInteface where condition LHS suggestion. Valid return type are CONCEPT, FORMULA get Concepts , use
    * Concept_type to get data type
    */
   public List<Concept> suggestBTsForWhereLHS (String searchString, Long modelId, Long userId) throws SWIException;

   /**
    * used for QueryInteface where condition LHS suggestion. Valid return type are CONCEPT, FORMULA get Concepts , use
    * Concept_type to get data type for Stat
    */
   public List<Concept> suggestBTsForWhereLHS (String searchString, String stat, Long userId, Long modelId)
            throws SWIException;

   /**
    * used for QueryInteface where condition RHS suggestion. Valid return type are CONCEPT_LOOKUP_INSTANCE, CONCEPT Get
    * Instance from concept display Name
    */
   public List<Instance> suggestBTAndValuesForWhereRHS (String businessTerm, String searchString, Long userId,
            Long modelId) throws SWIException;

   /**
    * Valid return type are STAT used for QueryInteface where method name start Stat. Generally returned list is
    * combined with other list. get Stats
    */
   public List<Stat> suggestStats (String searchString) throws SWIException;

   // Helper methods
   public List<Concept> suggestConcepts (String searchString, Long modelId, Long userId) throws SWIException;

   public List<ConceptProfile> suggestConceptProfiles (String searchString, Long userId, Long modelId)
            throws SWIException;

   public List<InstanceProfile> suggestInstanceProfilesForWhereRHS (String businessTerm, String searchString,
            Long userId, Long modelId) throws SWIException;
}
