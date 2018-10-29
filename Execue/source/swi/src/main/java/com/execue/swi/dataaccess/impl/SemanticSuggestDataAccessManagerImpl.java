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

import com.execue.core.common.bean.SearchFilter;
import com.execue.core.common.bean.entity.Application;
import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.ModelGroup;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.dataaccess.swi.dao.ISemanticSuggestDAO;
import com.execue.swi.dataaccess.ISemanticSuggestDataAccessManager;
import com.execue.swi.exception.SWIException;
import com.execue.swi.exception.SWIExceptionCodes;

/**
 * @author ExeCue
 */
public class SemanticSuggestDataAccessManagerImpl implements ISemanticSuggestDataAccessManager {

   private ISemanticSuggestDAO semanticSuggestDAO;

   @SuppressWarnings ("unchecked")
   public List<BusinessEntityDefinition> getConceptsWithAppNameBySearchString (String searchString,
            List<Long> modelGroupIds) throws SWIException {
      try {
         return getSemanticSuggestDAO().getConceptsWithAppNameBySearchString(searchString, modelGroupIds);
      } catch (DataAccessException dae) {
         throw new SWIException(ExeCueExceptionCodes.ENTITY_RETRIEVAL_FAILED, dae);
      }
   }

   @SuppressWarnings ("unchecked")
   public List<BusinessEntityDefinition> getInstancesWithAppNameBySearchString (String searchString,
            List<Long> modelGroupIds) throws SWIException {
      try {
         return getSemanticSuggestDAO().getInstancesWithAppNameBySearchString(searchString, modelGroupIds);
      } catch (DataAccessException dae) {
         throw new SWIException(ExeCueExceptionCodes.ENTITY_RETRIEVAL_FAILED, dae);
      }
   }

   @SuppressWarnings ("unchecked")
   public List<BusinessEntityDefinition> getConceptProfilesWithAppNameBySearchString (String searchString,
            List<Long> modelGroupIds) throws SWIException {
      try {
         return getSemanticSuggestDAO().getConceptProfilesWithAppNameBySearchString(searchString, modelGroupIds);
      } catch (DataAccessException dae) {
         throw new SWIException(ExeCueExceptionCodes.ENTITY_RETRIEVAL_FAILED, dae);
      }
   }

   @SuppressWarnings ("unchecked")
   public List<BusinessEntityDefinition> getInstanceProfilesWithAppNameBySearchString (String searchString,
            List<Long> modelGroupIds) throws SWIException {
      try {
         return getSemanticSuggestDAO().getInstanceProfilesWithAppNameBySearchString(searchString, modelGroupIds);
      } catch (DataAccessException dae) {
         throw new SWIException(ExeCueExceptionCodes.ENTITY_RETRIEVAL_FAILED, dae);
      }
   }

   @SuppressWarnings ("unchecked")
   public String getApplicationNameByModelGroup (ModelGroup modelGroup) throws SWIException {
      try {
         return getSemanticSuggestDAO().getApplicationNameByModelGroup(modelGroup);
      } catch (DataAccessException dae) {
         throw new SWIException(SWIExceptionCodes.QI_AUTHORIZED_APPLICATIONS_RETRIEVAL_EXCEPTION_CODE, dae);
      }
   }

   // this method returns all the applications whose owner is same as the userId or those which are public
   @SuppressWarnings ("unchecked")
   public List<Long> getAuthorizedModelGroupIdsForAutoSuggestions (Long userId, SearchFilter searchFilter)
            throws SWIException {
      List<Long> authorizedModelGroupIds = null;
      try {
         authorizedModelGroupIds = getSemanticSuggestDAO().getAuthorizedModelGroupIdsForAutoSuggestions(userId,
                  searchFilter);
      } catch (DataAccessException dae) {
         throw new SWIException(SWIExceptionCodes.QI_AUTHORIZED_APPLICATIONS_RETRIEVAL_EXCEPTION_CODE, dae);
      }
      return authorizedModelGroupIds;
   }

   @Override
   public List<Long> getAuthorizedModelGroupIdsForApplications (List<Application> applications) throws SWIException {
      try {
         return getSemanticSuggestDAO().getAuthorizedModelGroupIdsForApplications(applications);
      } catch (DataAccessException e) {
         throw new SWIException(SWIExceptionCodes.QI_AUTHORIZED_APPLICATIONS_RETRIEVAL_EXCEPTION_CODE, e);
      }

   }

   public ISemanticSuggestDAO getSemanticSuggestDAO () {
      return semanticSuggestDAO;
   }

   public void setSemanticSuggestDAO (ISemanticSuggestDAO semanticSuggestDAO) {
      this.semanticSuggestDAO = semanticSuggestDAO;
   }

}
