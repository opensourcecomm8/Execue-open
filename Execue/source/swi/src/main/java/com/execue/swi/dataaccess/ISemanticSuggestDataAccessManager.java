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

import com.execue.core.common.bean.SearchFilter;
import com.execue.core.common.bean.entity.Application;
import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.ModelGroup;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.swi.exception.SWIException;

/**
 * @author John Mallavalli
 */
public interface ISemanticSuggestDataAccessManager {

   public List<Long> getAuthorizedModelGroupIdsForAutoSuggestions (Long userId, SearchFilter searchFilter)
            throws SWIException;

   public String getApplicationNameByModelGroup (ModelGroup modelGroup) throws SWIException;

   public List<BusinessEntityDefinition> getConceptsWithAppNameBySearchString (String searchString,
            List<Long> modelGroupIds) throws SWIException;

   public List<BusinessEntityDefinition> getInstancesWithAppNameBySearchString (String searchString,
            List<Long> modelGroupIds) throws SWIException;

   public List<BusinessEntityDefinition> getConceptProfilesWithAppNameBySearchString (String searchString,
            List<Long> modelGroupIds) throws SWIException;

   public List<BusinessEntityDefinition> getInstanceProfilesWithAppNameBySearchString (String searchString,
            List<Long> modelGroupIds) throws SWIException;

   public List<Long> getAuthorizedModelGroupIdsForApplications (List<Application> applications)
            throws SWIException;
}
