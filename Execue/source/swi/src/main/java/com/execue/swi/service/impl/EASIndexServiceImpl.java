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

import java.util.Date;
import java.util.List;

import com.execue.core.common.bean.swi.ApplicationScopeIndexDetail;
import com.execue.core.common.bean.swi.ApplicationScopeInfo;
import com.execue.swi.dataaccess.IEASIndexDataAccessManager;
import com.execue.swi.exception.SWIException;
import com.execue.swi.service.IEASIndexService;

public class EASIndexServiceImpl implements IEASIndexService {

   private IEASIndexDataAccessManager easIndexDataAccessManager;

   public List<ApplicationScopeInfo> getScopedApps (List<String> values) throws SWIException {
      return getEasIndexDataAccessManager().getScopedApps(values);
   }

   public List<ApplicationScopeInfo> getScopedAppsByVertical (List<String> paramValues, Long verticalId)
            throws SWIException {
      return getEasIndexDataAccessManager().getScopedAppsByVertical(paramValues, verticalId);
   }

   public ApplicationScopeIndexDetail getApplicationScopeIndexDetailByAppId (Long appId) throws SWIException {
      return getEasIndexDataAccessManager().getApplicationScopeIndexDetailByAppId(appId);
   }

   public List<ApplicationScopeIndexDetail> getApplicationScopeIndexDetails (Date lastRefreshDate) throws SWIException {
      return getEasIndexDataAccessManager().getApplicationScopeIndexDetails(lastRefreshDate);
   }

   public void updateApplicationScopeIndexDetail (ApplicationScopeIndexDetail applicationScopeIndexDetail)
            throws SWIException {
      getEasIndexDataAccessManager().updateApplicationScopeIndexDetail(applicationScopeIndexDetail);
   }

   public void createApplicationScopeIndexDetail (ApplicationScopeIndexDetail applicationScopeIndexDetail)
            throws SWIException {
      getEasIndexDataAccessManager().createApplicationScopeIndexDetail(applicationScopeIndexDetail);
   }

   public ApplicationScopeIndexDetail getApplicationScopeIndexDetailById (Long applicationScopeIndexDetailId)
            throws SWIException {
      return getEasIndexDataAccessManager().getApplicationScopeIndexDetailById(applicationScopeIndexDetailId);
   }

   public void deleteApplicationScopeIndexDetail (ApplicationScopeIndexDetail applicationScopeIndexDetail)
            throws SWIException {
      getEasIndexDataAccessManager().deleteApplicationScopeIndexDetail(applicationScopeIndexDetail);
   }

   public void deleteEASIndexByApplicationId (Long applicationId) throws SWIException {
      getEasIndexDataAccessManager().deleteEASIndexByApplicationId(applicationId);
   }

   public void deleteEASPreIndexByApplicationId (Long applicationId) throws SWIException {
      getEasIndexDataAccessManager().deleteEASPreIndexByApplicationId(applicationId);
   }

   public void populateEASPreIndexForRiOntoTerm (Long applicationId, Long modelGroupId) throws SWIException {
      getEasIndexDataAccessManager().populateEASPreIndexForRiOntoTerm(applicationId, modelGroupId);
   }

   public void populateEASPreIndexForSFLTerm (Long applicationId, Long modelGroupId) throws SWIException {
      getEasIndexDataAccessManager().populateEASPreIndexForSFLTerm(applicationId, modelGroupId);
   }

   public void populateEASIndexFromEASPreIndex (Long applicationId, Long modelGroupId) throws SWIException {
      getEasIndexDataAccessManager().populateEASIndexFromEASPreIndex(applicationId, modelGroupId);
   }

   /**
    * @return the easIndexDataAccessManager
    */
   public IEASIndexDataAccessManager getEasIndexDataAccessManager () {
      return easIndexDataAccessManager;
   }

   /**
    * @param easIndexDataAccessManager the easIndexDataAccessManager to set
    */
   public void setEasIndexDataAccessManager (IEASIndexDataAccessManager easIndexDataAccessManager) {
      this.easIndexDataAccessManager = easIndexDataAccessManager;
   }

}
