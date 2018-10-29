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


package com.execue.handler.swi.impl;

import java.util.ArrayList;
import java.util.List;

import com.execue.core.common.bean.entity.Application;
import com.execue.core.common.bean.entity.Vertical;
import com.execue.core.constants.ExecueConstants;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.core.exception.HandlerException;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.handler.bean.UIApplicationInfo;
import com.execue.handler.swi.IVerticalServiceHandler;
import com.execue.swi.configuration.ISWIConfigurationService;
import com.execue.swi.exception.SWIException;
import com.execue.swi.service.IVerticalService;

public class VerticalServiceHandlerImpl implements IVerticalServiceHandler {

   private IVerticalService         verticalService;
   private ISWIConfigurationService swiConfigurationService;

   public void createVertical (Vertical vertical) throws HandlerException {
      try {
         // The null check defines that is a create vertical request, otherwise its update
         if (vertical.getId() == null) {
            Vertical existingVertical = getVerticalService().getVerticalByName(vertical.getName());

            if (existingVertical != null) {
               throw new HandlerException(ExeCueExceptionCodes.ENTITY_ALREADY_EXISTS, "A Vertical with the name \""
                        + vertical.getName() + "\" already exists. Please choose a different name.");
            }
         }
         getVerticalService().createVertical(vertical);
      } catch (SWIException e) {
         throw new HandlerException(ExeCueExceptionCodes.ENTITY_ALREADY_EXISTS, e.getMessage());
      }
   }

   public void deleteVertical (Long verticalId) throws HandlerException {
      try {
         // get the list of apps asociated to this vertical before deletion.
         List<Long> applicationIds = new ArrayList<Long>();
         applicationIds = getAppIds(getApplications(verticalId));
         // delete the associated apps first
         deleteVerticalAppAssociation(applicationIds, verticalId);
         // now delete the vertical after the association has been taken care off.
         getVerticalService().deleteVertical(verticalId);
      } catch (SWIException e) {
         throw new HandlerException(ExeCueExceptionCodes.DEFAULT_SYSTEM_EXCEPTION_CODE, e);
      }
   }

   public List<Vertical> getAllVerticals () throws HandlerException {
      List<Vertical> verticalApplications = new ArrayList<Vertical>();
      try {
         verticalApplications = getVerticalService().getAllVerticals();
      } catch (SWIException e) {
         throw new HandlerException(ExeCueExceptionCodes.ENTITY_RETRIEVAL_FAILED, e);
      }
      return verticalApplications;
   }

   public List<Vertical> getAllVisibleVerticals () throws HandlerException {
      return getSwiConfigurationService().getVisibleVerticals();
   }

   public Vertical getVertical (Long verticalId) throws HandlerException {
      Vertical verticalDetails = new Vertical();
      try {
         verticalDetails = getVerticalService().getVertical(verticalId);
      } catch (SWIException e) {
         throw new HandlerException(ExeCueExceptionCodes.ENTITY_RETRIEVAL_FAILED, e);
      }
      return verticalDetails;
   }

   public List<UIApplicationInfo> getApplications (Long verticalId) throws HandlerException {
      List<UIApplicationInfo> verticalApplications = new ArrayList<UIApplicationInfo>();
      try {
         transposeFromApplicationToUI(getVerticalService().getApplications(verticalId), verticalApplications);
      } catch (SWIException e) {
         throw new HandlerException(ExeCueExceptionCodes.ENTITY_RETRIEVAL_FAILED, e);
      }
      return verticalApplications;
   }

   public List<Long> getVerticalIds (Long applicationId) throws HandlerException {
      List<Long> verticalIds = new ArrayList<Long>();
      try {
         verticalIds = getVerticalService().getVerticalIds(applicationId);
      } catch (SWIException e) {
         throw new HandlerException(ExeCueExceptionCodes.ENTITY_RETRIEVAL_FAILED, e);
      }
      return verticalIds;
   }

   public List<Long> getVerticalExcludedApps (List<Long> applicationIds, Long verticalId) throws HandlerException {
      List<Long> verticalIds = new ArrayList<Long>();
      try {
         verticalIds = getVerticalService().getVerticalExcludedApps(applicationIds, verticalId);
      } catch (SWIException e) {
         throw new HandlerException(ExeCueExceptionCodes.ENTITY_RETRIEVAL_FAILED, e);
      }
      return verticalIds;
   }

   public void updateVertical (Vertical vertical) throws HandlerException {
      try {
         getVerticalService().updateVertical(vertical);
      } catch (SWIException e) {
         throw new HandlerException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public void createVerticalAppAssociation (List<Long> applicationIds, Long verticalId) throws HandlerException {
      try {
         if (ExecueCoreUtil.isCollectionNotEmpty(applicationIds)) {
            // delete every thing from table by verticalId
            getVerticalService().deleteVerticalAppAssociation(applicationIds, verticalId);
            getVerticalService().createVerticalAppAssociation(applicationIds, verticalId);
         }
      } catch (SWIException e) {
         throw new HandlerException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public void deleteVerticalAppAssociation (List<Long> applicationIds, Long verticalId) throws HandlerException {
      try {
         if (ExecueCoreUtil.isCollectionNotEmpty(applicationIds)) {
            // chk for the app having more than 1 association, if not then add it to default vertical 1st before
            // deletion.
            List<Long> appIdsForEvaluation = new ArrayList<Long>(applicationIds);
            List<Long> verticalExcludedApps = getVerticalExcludedApps(appIdsForEvaluation, verticalId);
            if (ExecueCoreUtil.isCollectionNotEmpty(verticalExcludedApps)) {
               addToDefaultVerticalAppAssociation(verticalExcludedApps);
            }
            getVerticalService().deleteVerticalAppAssociation(applicationIds, verticalId);
         }
      } catch (SWIException e) {
         throw new HandlerException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public void moveAppsAcrossVertical (List<Long> applicationIds, Long verticalId) throws HandlerException {
      try {
         if (ExecueCoreUtil.isCollectionNotEmpty(applicationIds)) {
            getVerticalService().deleteVerticalAppAssociation(applicationIds, verticalId);
         }
      } catch (SWIException e) {
         throw new HandlerException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public void addToDefaultVerticalAppAssociation (List<Long> applicationIds) throws HandlerException {
      try {
         Vertical defaultVertical = getVerticalService().getVerticalByName(ExecueConstants.DEFAULT_VERTICAL_NAME);
         getVerticalService().addToDefaultVerticalAppAssociation(applicationIds, defaultVertical.getId());
      } catch (SWIException e) {
         throw new HandlerException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   private void transposeFromApplicationToUI (List<Application> fromList, List<UIApplicationInfo> toList) {
      for (Application app : fromList) {
         UIApplicationInfo uiApp = new UIApplicationInfo();
         uiApp.setApplicationId(app.getId());
         uiApp.setApplicationName(app.getName());
         toList.add(uiApp);
      }
   }

   private List<Long> getAppIds (List<UIApplicationInfo> appInfoList) {
      List<Long> applicationIds = new ArrayList<Long>();
      for (UIApplicationInfo appInfo : appInfoList) {
         applicationIds.add(appInfo.getApplicationId());
      }
      return applicationIds;
   }

   public IVerticalService getVerticalService () {
      return verticalService;
   }

   public void setVerticalService (IVerticalService verticalService) {
      this.verticalService = verticalService;
   }

   public ISWIConfigurationService getSwiConfigurationService () {
      return swiConfigurationService;
   }

   public void setSwiConfigurationService (ISWIConfigurationService swiConfigurationService) {
      this.swiConfigurationService = swiConfigurationService;
   }

}
