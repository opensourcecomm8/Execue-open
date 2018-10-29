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

import java.util.Date;
import java.util.List;

import com.execue.core.common.bean.swi.ApplicationScopeIndexDetail;
import com.execue.core.common.bean.swi.ApplicationScopeInfo;
import com.execue.swi.exception.SWIException;

public interface IEASIndexService {

   public List<ApplicationScopeInfo> getScopedApps (List<String> values) throws SWIException;

   public List<ApplicationScopeInfo> getScopedAppsByVertical (List<String> paramValues, Long verticalId)
            throws SWIException;

   public List<ApplicationScopeIndexDetail> getApplicationScopeIndexDetails (Date lastRefreshDate) throws SWIException;

   public ApplicationScopeIndexDetail getApplicationScopeIndexDetailByAppId (Long appId) throws SWIException;

   public void updateApplicationScopeIndexDetail (ApplicationScopeIndexDetail applicationScopeIndexDetail)
            throws SWIException;

   public void createApplicationScopeIndexDetail (ApplicationScopeIndexDetail applicationScopeIndexDetail)
            throws SWIException;

   public void deleteApplicationScopeIndexDetail (ApplicationScopeIndexDetail applicationScopeIndexDetail)
            throws SWIException;

   public ApplicationScopeIndexDetail getApplicationScopeIndexDetailById (Long applicationScopeIndexDetailId)
            throws SWIException;

   public void deleteEASIndexByApplicationId (Long applicationId) throws SWIException;

   public void populateEASPreIndexForSFLTerm (Long applicationId, Long modelGroupId) throws SWIException;

   public void populateEASPreIndexForRiOntoTerm (Long applicationId, Long modelGroupId) throws SWIException;

   public void deleteEASPreIndexByApplicationId (Long applicationId) throws SWIException;

   public void populateEASIndexFromEASPreIndex (Long applicationId, Long modelGroupId) throws SWIException;
}
