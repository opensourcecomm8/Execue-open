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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.Cloud;
import com.execue.core.common.bean.entity.CloudComponent;
import com.execue.core.common.bean.entity.PathDefinition;
import com.execue.core.common.bean.entity.Type;
import com.execue.core.common.type.BusinessEntityType;
import com.execue.core.common.type.ComponentCategory;
import com.execue.core.common.util.ExecueBeanManagementUtil;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.swi.dataaccess.IKDXCloudDataAccessManager;
import com.execue.swi.dataaccess.IKDXDataAccessManager;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.SWIException;
import com.execue.swi.exception.SWIExceptionCodes;
import com.execue.swi.service.IBaseKDXRetrievalService;
import com.execue.swi.service.IKDXCloudManagementService;
import com.execue.swi.service.IKDXCloudRetrievalService;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.swi.service.IPathDefinitionRetrievalService;

/**
 * @author John
 */
public class KDXCloudManagementServiceImpl implements IKDXCloudManagementService {

   private IKDXCloudDataAccessManager      kdxCloudDataAccessManager;
   private IKDXDataAccessManager           kdxDataAccessManager;
   private IKDXCloudRetrievalService       kdxCloudRetrievalService;
   private IBaseKDXRetrievalService        baseKDXRetrievalService;
   private IKDXRetrievalService            kdxRetrievalService;
   private IPathDefinitionRetrievalService pathDefinitionRetrievalService;

   public void create (Cloud cloud) throws KDXException {
      Cloud existingCloud;
      existingCloud = kdxCloudRetrievalService.getCloudByName(cloud.getName());
      if (existingCloud != null) {
         throw new KDXException(SWIExceptionCodes.ENTITY_ALREADY_EXISTS, "Cloud with name [" + cloud.getName()
                  + "] already exists");
      }
      getKdxCloudDataAccessManager().create(cloud);
      // create base cloud components
      List<BusinessEntityDefinition> baseRealizedTypeBEDs = getBaseKDXRetrievalService().getBEDInBaseModel(
               BusinessEntityType.REALIZED_TYPE);
      if (ExecueCoreUtil.isCollectionNotEmpty(baseRealizedTypeBEDs)) {
         List<CloudComponent> componentsToBeAdded = new ArrayList<CloudComponent>();
         for (BusinessEntityDefinition businessEntityDefinition : baseRealizedTypeBEDs) {
            BusinessEntityDefinition typeBusinessEntityDefinition = kdxRetrievalService
                     .getTypeBusinessEntityDefinition(businessEntityDefinition.getType().getId());
            componentsToBeAdded.add(ExecueBeanManagementUtil.prepareCloudComponent(cloud, businessEntityDefinition,
                     typeBusinessEntityDefinition, ComponentCategory.REALIZATION));
         }
         addComponentsToCloud(componentsToBeAdded);
      }

   }

   public void addComponentsToCloud (List<CloudComponent> componentsToBeAdded) throws KDXException {
      getKdxCloudDataAccessManager().addComponentsToCloud(componentsToBeAdded);
   }

   public void removeComponentsFromCloud (List<CloudComponent> componentsToBeRemoved) throws KDXException {
      getKdxCloudDataAccessManager().removeComponentsFromCloud(componentsToBeRemoved);
   }

   public void removeComponentFromCloud (Long cloudId, Long componentBEId) throws KDXException {
      getKdxCloudDataAccessManager().removeComponentFromCloud(cloudId, componentBEId);
   }

   public Cloud createComplexType (Type type, List<CloudComponent> components, Long knowledgeId) throws KDXException {
      Cloud complexTypeCloud = null;
      // create new type
      // TODO: -JM- modify the signature to send modelId from the caller
      Long modelId = 1L;
      kdxDataAccessManager.createType(modelId, type, false, knowledgeId);
      // create new type cloud
      complexTypeCloud = new Cloud();
      // TODO: -JM- populate all the details into the cloud and create cloud
      create(complexTypeCloud);
      return complexTypeCloud;
   }

   public void manageBaseConceptsInCloud (Cloud cloud, Long modelId) throws SWIException {
      // create base cloud components
      List<BusinessEntityDefinition> baseConceptBEDs = getBaseKDXRetrievalService().getBEDInBaseModel(
               BusinessEntityType.CONCEPT);

      if (ExecueCoreUtil.isCollectionNotEmpty(baseConceptBEDs)) {
         List<CloudComponent> componentsToBeAdded = new ArrayList<CloudComponent>();
         List<CloudComponent> componentsToBeRemoved = new ArrayList<CloudComponent>();
         for (BusinessEntityDefinition businessEntityDefinition : baseConceptBEDs) {
            List<PathDefinition> pathsByDestinationId = getPathDefinitionRetrievalService()
                     .getAllDirectPathsByDestinationId(businessEntityDefinition.getId(), modelId);
            CloudComponent component = getKdxCloudRetrievalService().getCloudComponentByCloudIdAndComponentBedId(
                     cloud.getId(), businessEntityDefinition.getId());
            if (component == null && !CollectionUtils.isEmpty(pathsByDestinationId)) {
               BusinessEntityDefinition typeBusinessEntityDefinition = kdxRetrievalService
                        .getTypeBusinessEntityDefinition(businessEntityDefinition.getType().getId());
               componentsToBeAdded.add(ExecueBeanManagementUtil.prepareCloudComponent(cloud, businessEntityDefinition,
                        typeBusinessEntityDefinition, ComponentCategory.REALIZATION));
            } else if (component != null && CollectionUtils.isEmpty(pathsByDestinationId)) {
               componentsToBeRemoved.add(component);
            }
         }
         addComponentsToCloud(componentsToBeAdded);
         removeComponentsFromCloud(componentsToBeRemoved);
      }
   }

   public void updateCloud (Cloud cloud) throws KDXException {
      getKdxCloudDataAccessManager().update(cloud);
   }

   public IKDXCloudRetrievalService getKdxCloudRetrievalService () {
      return kdxCloudRetrievalService;
   }

   public void setKdxCloudRetrievalService (IKDXCloudRetrievalService kdxCloudRetrievalService) {
      this.kdxCloudRetrievalService = kdxCloudRetrievalService;
   }

   public IKDXCloudDataAccessManager getKdxCloudDataAccessManager () {
      return kdxCloudDataAccessManager;
   }

   public void setKdxCloudDataAccessManager (IKDXCloudDataAccessManager kdxCloudDataAccessManager) {
      this.kdxCloudDataAccessManager = kdxCloudDataAccessManager;
   }

   public IKDXDataAccessManager getKdxDataAccessManager () {
      return kdxDataAccessManager;
   }

   public void setKdxDataAccessManager (IKDXDataAccessManager kdxDataAccessManager) {
      this.kdxDataAccessManager = kdxDataAccessManager;
   }

   public IBaseKDXRetrievalService getBaseKDXRetrievalService () {
      return baseKDXRetrievalService;
   }

   public void setBaseKDXRetrievalService (IBaseKDXRetrievalService baseKDXRetrievalService) {
      this.baseKDXRetrievalService = baseKDXRetrievalService;
   }

   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }

   public IPathDefinitionRetrievalService getPathDefinitionRetrievalService () {
      return pathDefinitionRetrievalService;
   }

   public void setPathDefinitionRetrievalService (IPathDefinitionRetrievalService pathDefinitionRetrievalService) {
      this.pathDefinitionRetrievalService = pathDefinitionRetrievalService;
   }

}
