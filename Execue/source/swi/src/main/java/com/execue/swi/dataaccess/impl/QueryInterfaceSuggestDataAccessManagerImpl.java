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


package com.execue.swi.dataaccess.impl;

import java.util.List;

import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.ConceptProfile;
import com.execue.core.common.bean.entity.Instance;
import com.execue.core.common.bean.entity.InstanceProfile;
import com.execue.core.common.bean.entity.Stat;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.dataaccess.swi.dao.IBusinessEntityDefinitionDAO;
import com.execue.dataaccess.swi.dao.IConceptDAO;
import com.execue.dataaccess.swi.dao.IQueryInterfaceSuggestDAO;
import com.execue.swi.dataaccess.IQueryInterfaceSuggestDataAccessManager;
import com.execue.swi.exception.SWIException;

/**
 * @author John Mallavalli
 */
public class QueryInterfaceSuggestDataAccessManagerImpl implements IQueryInterfaceSuggestDataAccessManager {

   private IQueryInterfaceSuggestDAO    queryInterfaceSuggestDAO;
   private IBusinessEntityDefinitionDAO businessEntityDefinitionDAO;
   private IConceptDAO                  conceptDAO;

   @SuppressWarnings ("unchecked")
   public List<Concept> suggestConcepts (final String searchString, Long modelId, Long userId) throws SWIException {
      try {
         return getQueryInterfaceSuggestDAO().suggestConcepts(searchString, modelId, userId);
      } catch (DataAccessException dae) {
         throw new SWIException(dae.getCode(), dae);
      }
   }

   @SuppressWarnings ("unchecked")
   // TODO: -RG- userId has to be considered in query string
   public List<ConceptProfile> suggestConceptProfiles (String searchString, Long userId, Long modelId)
            throws SWIException {
      try {
         return getQueryInterfaceSuggestDAO().suggestConceptProfiles(searchString, userId, modelId);
      } catch (DataAccessException dae) {
         throw new SWIException(dae.getCode(), dae);
      }
   }

   @SuppressWarnings ("unchecked")
   // TODO: -VG- userId has to be considered in query string
   public List<InstanceProfile> suggestInstanceProfilesForWhereRHS (String businessTerm, String searchString,
            Long userId, Long modelId) throws SWIException {
      try {
         return getQueryInterfaceSuggestDAO().suggestInstanceProfilesForWhereRHS(businessTerm, searchString, userId,
                  modelId);
      } catch (DataAccessException dae) {
         throw new SWIException(dae.getCode(), dae);
      }
   }

   @SuppressWarnings ("unchecked")
   public List<Stat> suggestStats (final String searchString) throws SWIException {
      try {
         return getQueryInterfaceSuggestDAO().suggestStats(searchString);
      } catch (DataAccessException dae) {
         throw new SWIException(dae.getCode(), dae);
      }
   }

   @SuppressWarnings ("unchecked")
   public List<Concept> suggestBTsForSelect (final String searchString, final String statName, Long userId, Long modelId)
            throws SWIException {
      try {
         return getQueryInterfaceSuggestDAO().suggestBTsForSelect(searchString, statName, userId, modelId);
      } catch (DataAccessException dae) {
         throw new SWIException(dae.getCode(), dae);
      }
   }

   @SuppressWarnings ("unchecked")
   public List<Concept> suggestBTsForPopulation (String searchString, Long userId, Long modelId) throws SWIException {
      try {
         return getQueryInterfaceSuggestDAO().suggestBTsForPopulation(searchString, userId, modelId);
      } catch (DataAccessException dae) {
         throw new SWIException(dae.getCode(), dae);
      }
   }

   // used for QueryInteface where condition LHS suggestion. Valid return type are CONCEPT, FORMULA get Concepts , use
   // Concept_type to get data type
   @SuppressWarnings ("unchecked")
   public List<Concept> suggestBTsForWhereLHS (String searchString, Long modelId, Long userId) throws SWIException {
      try {
         return getQueryInterfaceSuggestDAO().suggestBTsForWhereLHS(searchString, modelId, userId);
      } catch (DataAccessException dae) {
         throw new SWIException(dae.getCode(), dae);
      }
   }

   @SuppressWarnings ("unchecked")
   public List<Concept> suggestBTsForWhereLHS (String searchString, String statName, Long userId, Long modelId)
            throws SWIException {
      try {
         return getQueryInterfaceSuggestDAO().suggestBTsForWhereLHS(searchString, statName, userId, modelId);
      } catch (DataAccessException dae) {
         throw new SWIException(dae.getCode(), dae);
      }
   }

   @SuppressWarnings ("unchecked")
   public List<Instance> suggestBTAndValuesForWhereRHS (String businessTerm, String searchString, Long userId,
            Long modelId) throws SWIException {
      try {
         return getQueryInterfaceSuggestDAO()
                  .suggestBTAndValuesForWhereRHS(businessTerm, searchString, userId, modelId);
      } catch (DataAccessException dae) {
         throw new SWIException(dae.getCode(), dae);
      }
   }

   public IQueryInterfaceSuggestDAO getQueryInterfaceSuggestDAO () {
      return queryInterfaceSuggestDAO;
   }

   public void setQueryInterfaceSuggestDAO (IQueryInterfaceSuggestDAO queryInterfaceSuggestDAO) {
      this.queryInterfaceSuggestDAO = queryInterfaceSuggestDAO;
   }

   public IBusinessEntityDefinitionDAO getBusinessEntityDefinitionDAO () {
      return businessEntityDefinitionDAO;
   }

   public void setBusinessEntityDefinitionDAO (IBusinessEntityDefinitionDAO businessEntityDefinitionDAO) {
      this.businessEntityDefinitionDAO = businessEntityDefinitionDAO;
   }

   /**
    * @return the conceptDAO
    */
   public IConceptDAO getConceptDAO () {
      return conceptDAO;
   }

   /**
    * @param conceptDAO the conceptDAO to set
    */
   public void setConceptDAO (IConceptDAO conceptDAO) {
      this.conceptDAO = conceptDAO;
   }

}