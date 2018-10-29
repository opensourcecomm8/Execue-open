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

import java.util.ArrayList;
import java.util.List;

import com.execue.core.common.bean.entity.Application;
import com.execue.core.common.bean.entity.Vertical;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.swi.exception.SWIException;
import com.execue.dataaccess.swi.dao.IVerticalDAO;
import com.execue.swi.dataaccess.IVerticalDataAccessManager;

public class VerticalDataAccessManagerImpl implements IVerticalDataAccessManager {

   private IVerticalDAO verticalDAO;

   public Vertical getVerticalByName (String name) throws SWIException {
      try {
         return getVerticalDAO().getVerticalByName(name);
      } catch (DataAccessException dataAccessException) {
         throw new SWIException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public void createUpdateVertical (Vertical vertical) throws SWIException {
      try {
         getVerticalDAO().createUpdateVertical(vertical);
      } catch (DataAccessException dataAccessException) {
         throw new SWIException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public void deleteVertical (Long verticalId) throws SWIException {
      try {
         List<Long> existingVerticalApps = getApplicationIds(getVerticalDAO().getApplications(verticalId));
         // delete the existing apps association first. This method will associate the children without parents to the
         // default vertical (parent).
         deleteVerticalAppAssociation(existingVerticalApps, verticalId);
         // after clean up delete the vertical.
         getVerticalDAO().deleteVertical(verticalId);
      } catch (DataAccessException dataAccessException) {
         throw new SWIException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<Vertical> getAllVerticals () throws SWIException {
      try {
         return getVerticalDAO().getAllVerticals();
      } catch (DataAccessException dataAccessException) {
         throw new SWIException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<Vertical> getAllVisibleVertical () throws SWIException {
      try {
         return getVerticalDAO().getAllVisibleVerticals();
      } catch (DataAccessException dataAccessException) {
         throw new SWIException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public Vertical getVertical (Long verticalId) throws SWIException {
      try {
         return getVerticalDAO().getVertical(verticalId);
      } catch (DataAccessException dataAccessException) {
         throw new SWIException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<Application> getApplications (Long verticalId) throws SWIException {
      try {
         return getVerticalDAO().getApplications(verticalId);
      } catch (DataAccessException dataAccessException) {
         throw new SWIException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<Long> getVerticalExcludedApps (List<Long> applicationIds, Long verticalId) throws SWIException {
      try {
         return getVerticalDAO().getVerticalExcludedApps(applicationIds, verticalId);
      } catch (DataAccessException dataAccessException) {
         throw new SWIException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<Long> getVerticalIds (Long applicationId) throws SWIException {
      try {
         return getVerticalDAO().getVerticalIds(applicationId);
      } catch (DataAccessException dataAccessException) {
         throw new SWIException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public void createVerticalAppAssociation (List<Long> applicationIds, Long verticalId) throws SWIException {
      try {
         getVerticalDAO().createVerticalAppAssociation(applicationIds, verticalId);
      } catch (DataAccessException dataAccessException) {
         throw new SWIException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public void deleteVerticalAppAssociation (List<Long> applicationIds, Long verticalId) throws SWIException {
      try {
         getVerticalDAO().deleteVerticalAppAssociation(applicationIds, verticalId);
      } catch (DataAccessException dataAccessException) {
         throw new SWIException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public void addToDefaultVerticalAppAssociation (List<Long> applicationIds, Long defaultVerticalId)
            throws SWIException {
      try {
         getVerticalDAO().addToDefaultVerticalAppAssociation(applicationIds, defaultVerticalId);
      } catch (DataAccessException dataAccessException) {
         throw new SWIException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public void populateVerticalPopularity (Long verticalId) throws SWIException {
      try {
         getVerticalDAO().populateVerticalPopularity(verticalId);
      } catch (DataAccessException dataAccessException) {
         throw new SWIException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<Long> getAllVerticalIds () throws SWIException {
      try {
         return getVerticalDAO().getAllVerticalIds();
      } catch (DataAccessException dataAccessException) {
         throw new SWIException(dataAccessException.getCode(), dataAccessException);
      }
   }

   private List<Long> getApplicationIds (List<Application> applications) {
      List<Long> applicationIds = new ArrayList<Long>();
      for (Application app : applications) {
         applicationIds.add(app.getId());
      }
      return applicationIds;
   }

   public IVerticalDAO getVerticalDAO () {
      return verticalDAO;
   }

   public void setVerticalDAO (IVerticalDAO verticalDAO) {
      this.verticalDAO = verticalDAO;
   }

}
