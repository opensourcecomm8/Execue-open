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

import java.util.Date;
import java.util.List;

import com.execue.core.common.bean.swi.ApplicationScopeIndexDetail;
import com.execue.core.common.bean.swi.ApplicationScopeInfo;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.dataaccess.swi.dao.IEASIndexDAO;
import com.execue.swi.dataaccess.IEASIndexDataAccessManager;
import com.execue.swi.exception.SWIException;

public class EASIndexDataAccessManagerImpl implements IEASIndexDataAccessManager {

   private IEASIndexDAO easIndexDAO;

   public void deleteEASIndexByApplicationId (Long applicationId) throws SWIException {
      try {
         getEasIndexDAO().deleteEASIndexByApplicationId(applicationId);
      } catch (DataAccessException e) {
         throw new SWIException(e.getCode(), e);
      }
   }

   public void deleteEASPreIndexByApplicationId (Long applicationId) throws SWIException {
      try {
         getEasIndexDAO().deleteEASPreIndexByApplicationId(applicationId);
      } catch (DataAccessException e) {
         throw new SWIException(e.getCode(), e);
      }
   }

   public ApplicationScopeIndexDetail getApplicationScopeIndexDetailByAppId (Long appId) throws SWIException {
      try {
         return getEasIndexDAO().getApplicationScopeIndexDetailByAppId(appId);
      } catch (DataAccessException e) {
         throw new SWIException(e.getCode(), e);
      }
   }

   public List<ApplicationScopeIndexDetail> getApplicationScopeIndexDetails (Date lastRefreshDate) throws SWIException {
      try {
         return getEasIndexDAO().getApplicationScopeIndexDetails(lastRefreshDate);
      } catch (DataAccessException e) {
         throw new SWIException(e.getCode(), e);
      }
   }

   public List<ApplicationScopeInfo> getScopedApps (List<String> values) throws SWIException {
      try {
         return getEasIndexDAO().getScopedApps(values);
      } catch (DataAccessException e) {
         throw new SWIException(e.getCode(), e);
      }
   }

   public List<ApplicationScopeInfo> getScopedAppsByVertical (List<String> paramValues, Long verticalId)
            throws SWIException {
      try {
         return getEasIndexDAO().getScopedAppsByVertical(paramValues, verticalId);
      } catch (DataAccessException e) {
         throw new SWIException(e.getCode(), e);
      }
   }

   public void populateEASIndexFromEASPreIndex (Long applicationId, Long modelGroupId) throws SWIException {
      try {
         getEasIndexDAO().populateEASIndexFromEASPreIndex(applicationId, modelGroupId);
      } catch (DataAccessException e) {
         throw new SWIException(e.getCode(), e);
      }
   }

   public void populateEASPreIndexForRiOntoTerm (Long applicationId, Long modelGroupId) throws SWIException {
      try {
         getEasIndexDAO().populateEASPreIndexForRiOntoTerm(applicationId, modelGroupId);
      } catch (DataAccessException e) {
         throw new SWIException(e.getCode(), e);
      }
   }

   public void populateEASPreIndexForSFLTerm (Long applicationId, Long modelGroupId) throws SWIException {
      try {
         getEasIndexDAO().populateEASPreIndexForSFLTerm(applicationId, modelGroupId);
      } catch (DataAccessException e) {
         throw new SWIException(e.getCode(), e);
      }
   }

   public void updateEASPreIndexTokenWeightForEntity (Long applicationId, Long modelGroupId) throws SWIException {
      try {
         getEasIndexDAO().updateEASPreIndexTokenWeightForEntity(applicationId, modelGroupId);
      } catch (DataAccessException e) {
         throw new SWIException(e.getCode(), e);
      }
   }

   public void updateEASPreIndexTokenWeightForSFLTerm (Long applicationId, Long modelGroupId) throws SWIException {
      try {
         getEasIndexDAO().updateEASPreIndexTokenWeightForSFLTerm(applicationId, modelGroupId);
      } catch (DataAccessException e) {
         throw new SWIException(e.getCode(), e);
      }
   }

   public void setEasIndexDAO (IEASIndexDAO easIndexDAO) {
      this.easIndexDAO = easIndexDAO;
   }

   public void createApplicationScopeIndexDetail (ApplicationScopeIndexDetail applicationScopeIndexDetail)
            throws SWIException {
      try {
         getEasIndexDAO().create(applicationScopeIndexDetail);
      } catch (DataAccessException e) {
         throw new SWIException(e.getCode(), e);
      }
   }

   public void deleteApplicationScopeIndexDetail (ApplicationScopeIndexDetail applicationScopeIndexDetail)
            throws SWIException {
      try {
         getEasIndexDAO().delete(applicationScopeIndexDetail);
      } catch (DataAccessException e) {
         throw new SWIException(e.getCode(), e);
      }
   }

   public ApplicationScopeIndexDetail getApplicationScopeIndexDetailById (Long applicationScopeIndexDetailId)
            throws SWIException {
      try {
         return getEasIndexDAO().getById(applicationScopeIndexDetailId, ApplicationScopeIndexDetail.class);
      } catch (DataAccessException e) {
         throw new SWIException(e.getCode(), e);
      }
   }

   public void updateApplicationScopeIndexDetail (ApplicationScopeIndexDetail applicationScopeIndexDetail)
            throws SWIException {
      try {
         getEasIndexDAO().update(applicationScopeIndexDetail);
      } catch (DataAccessException e) {
         throw new SWIException(e.getCode(), e);
      }
   }

   /**
    * @return the easIndexDAO
    */
   public IEASIndexDAO getEasIndexDAO () {
      return easIndexDAO;
   }

}
