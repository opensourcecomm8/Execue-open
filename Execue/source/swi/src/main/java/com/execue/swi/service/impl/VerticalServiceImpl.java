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

import java.util.List;

import com.execue.core.common.bean.entity.Application;
import com.execue.core.common.bean.entity.Vertical;
import com.execue.swi.exception.SWIException;
import com.execue.swi.dataaccess.IVerticalDataAccessManager;
import com.execue.swi.service.IVerticalService;

public class VerticalServiceImpl implements IVerticalService {

   private IVerticalDataAccessManager verticalDataAccessManager;

   public Vertical getVerticalByName (String name) throws SWIException {
      return getVerticalDataAccessManager().getVerticalByName(name);
   }

   public void createVertical (Vertical vertical) throws SWIException {
      getVerticalDataAccessManager().createUpdateVertical(vertical);
   }

   public void deleteVertical (Long verticalId) throws SWIException {
      getVerticalDataAccessManager().deleteVertical(verticalId);
   }

   public List<Vertical> getAllVerticals () throws SWIException {
      return getVerticalDataAccessManager().getAllVerticals();
   }

   public List<Vertical> getAllVisibleVerticals () throws SWIException {
      return getVerticalDataAccessManager().getAllVisibleVertical();
   }

   public Vertical getVertical (Long verticalId) throws SWIException {
      return getVerticalDataAccessManager().getVertical(verticalId);
   }

   public List<Application> getApplications (Long verticalId) throws SWIException {
      return getVerticalDataAccessManager().getApplications(verticalId);
   }

   public List<Long> getVerticalExcludedApps (List<Long> applicationIds, Long verticalId) throws SWIException {
      return getVerticalDataAccessManager().getVerticalExcludedApps(applicationIds, verticalId);
   }

   public List<Long> getVerticalIds (Long applicationId) throws SWIException {
      return getVerticalDataAccessManager().getVerticalIds(applicationId);
   }

   public void updateVertical (Vertical vertical) throws SWIException {
      getVerticalDataAccessManager().createUpdateVertical(vertical);
   }

   public void createVerticalAppAssociation (List<Long> applicationIds, Long verticalId) throws SWIException {
      getVerticalDataAccessManager().createVerticalAppAssociation(applicationIds, verticalId);
   }

   public void deleteVerticalAppAssociation (List<Long> applicationIds, Long verticalId) throws SWIException {
      getVerticalDataAccessManager().deleteVerticalAppAssociation(applicationIds, verticalId);
   }

   public void addToDefaultVerticalAppAssociation (List<Long> applicationIds, Long defaultVerticalId)
            throws SWIException {
      getVerticalDataAccessManager().addToDefaultVerticalAppAssociation(applicationIds, defaultVerticalId);
   }

   public void populateVerticalPopularity () throws SWIException {
      // get the list of id for all the vertical in the system
      List<Long> verticalIds = getAllVerticalIds();
      // iterate upon each id and update the table with the popularity
      // which is the sum of all the application's popularity under that vertical
      for (Long verticalId : verticalIds) {
         getVerticalDataAccessManager().populateVerticalPopularity(verticalId);
      }
   }

   public List<Long> getAllVerticalIds () throws SWIException {
      return getVerticalDataAccessManager().getAllVerticalIds();
   }

   public IVerticalDataAccessManager getVerticalDataAccessManager () {
      return verticalDataAccessManager;
   }

   public void setVerticalDataAccessManager (IVerticalDataAccessManager verticalDataAccessManager) {
      this.verticalDataAccessManager = verticalDataAccessManager;
   }

}
