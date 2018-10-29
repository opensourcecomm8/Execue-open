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


/**
 * 
 */
package com.execue.swi.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.Cloud;
import com.execue.core.common.bean.entity.CloudComponent;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.common.bean.entity.ModelGroup;
import com.execue.core.common.bean.entity.RICloud;
import com.execue.core.common.bean.entity.Rule;
import com.execue.core.common.type.BusinessEntityType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.CloudCategory;
import com.execue.core.common.type.CloudOutput;
import com.execue.swi.dataaccess.IKDXCloudDataAccessManager;
import com.execue.swi.dataaccess.IKDXDataAccessManager;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.SWIException;
import com.execue.swi.exception.SWIExceptionCodes;
import com.execue.swi.service.IKDXCloudRetrievalService;

/**
 * @author Nitesh
 */
public class KDXCloudRetrievalServiceImpl implements IKDXCloudRetrievalService {

   private IKDXCloudDataAccessManager kdxCloudDataAccessManager;
   private IKDXDataAccessManager      kdxDataAccessManager;

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.swi.IKDXCloudRetrievalService#getRICloudsByCompBedIdsAndCategory(java.util.Set,
    *      com.execue.core.common.type.CloudOutput)
    */
   public Map<CloudCategory, List<RICloud>> getRICloudsByCompBedIdsAndCloudOutput (Set<Long> compBedIds,
            CloudOutput cloudOutput, Long cloudId) throws KDXException {
      try {
         Map<CloudCategory, List<RICloud>> riCloudsCategory = new HashMap<CloudCategory, List<RICloud>>();
         List<RICloud> riClouds = getKdxCloudDataAccessManager().getRICloudsByCompBedIdsAndCloudOutput(compBedIds,
                  cloudOutput, cloudId);
         if (CollectionUtils.isEmpty(riClouds)) {
            return new HashMap<CloudCategory, List<RICloud>>();
         }
         for (RICloud riCloud : riClouds) {
            // Load Details of Clouds
            // loadCloudDetails(riCloud);
            // Separate RI Cloud entries by Category
            if (riCloudsCategory.containsKey(riCloud.getCloudCategory())) {
               List<RICloud> riList = riCloudsCategory.get(riCloud.getCloudCategory());
               riList.add(riCloud);
            } else {
               List<RICloud> riList = new ArrayList<RICloud>();
               riList.add(riCloud);
               riCloudsCategory.put(riCloud.getCloudCategory(), riList);
            }
         }
         return riCloudsCategory;
      } catch (SWIException swiException) {
         throw new KDXException(SWIExceptionCodes.ENTITY_RETRIEVAL_FAILED, swiException);
      }
   }

   private void loadCloudDetails (Cloud cloud) {
      cloud.getName();
      Set<BusinessEntityDefinition> allowedBehaviors = cloud.getCloudAllowedBehavior();
      if (!CollectionUtils.isEmpty(allowedBehaviors)) {
         for (BusinessEntityDefinition behavior : allowedBehaviors) {
            behavior.getBehavior();
            behavior.getBehavior().getBehavior();
         }
      }
      Set<Rule> validationRules = cloud.getCloudValidationRules();
      if (!CollectionUtils.isEmpty(validationRules)) {
         for (Rule rule : validationRules) {
            rule.getName();
            rule.getId();
         }
      }
      Set<BusinessEntityDefinition> cloudAllowedComponents = cloud.getCloudAllowedComponents();
      for (BusinessEntityDefinition allowedComponent : cloudAllowedComponents) {
         allowedComponent.getId();
         allowedComponent.getEntityType();
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.swi.IKDXCloudRetrievalService#getDefaultAppCloud(java.lang.Long)
    */
   public Cloud getDefaultAppCloud (Long modelId) throws KDXException {
      Cloud defaultCloud = null;
      Model model = getKdxCloudDataAccessManager().getById(modelId, Model.class);

      for (Cloud cloud : model.getClouds()) {
         if (cloud.getIsDefault().equals(CheckType.YES)) {
            defaultCloud = cloud;
            break;
         }
      }
      defaultCloud.getName();
      return defaultCloud;
   }

   public List<Cloud> getAllCloudsByModelId (Long modelId) throws KDXException {
      return getKdxCloudDataAccessManager().getAllCloudsByModelId(modelId);
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.swi.IKDXCloudRetrievalService#getCloudByName(java.lang.String)
    */
   public Cloud getCloudByName (String cloudName) throws KDXException {
      Cloud cloud = getKdxCloudDataAccessManager().getCloudByName(cloudName);
      if (cloud != null) {
         loadCloudDetails(cloud);
      }
      return cloud;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.swi.IKDXCloudRetrievalService#getCloudComponentByCloudIdAndComponentBedId(java.lang
    *      .Long, java.lang.Long)
    */
   public CloudComponent getCloudComponentByCloudIdAndComponentBedId (Long cloudId, Long componentBedId)
            throws KDXException {
      return getKdxCloudDataAccessManager().getCloudComponentByCloudIdAndComponentBedId(cloudId, componentBedId);
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.swi.IKDXCloudRetrievalService#getAllComponentsWithDetails(java.lang.Long)
    */
   public List<CloudComponent> getAllCloudComponentsByCloudIdWithDetails (Long cloudId) throws KDXException {
      List<CloudComponent> cloudComponents = getKdxCloudDataAccessManager().getAllCloudComponentsByCloudId(cloudId);
      for (CloudComponent cloudComponent : cloudComponents) {

         cloudComponent.getCloud().getName();
         BusinessEntityDefinition componentBed = cloudComponent.getComponentBed();
         componentBed.getModelGroup();
         BusinessEntityType entityType = componentBed.getEntityType();
         if (BusinessEntityType.BEHAVIOR.equals(entityType)) {
            componentBed.getBehavior().getBehavior();
         } else if (BusinessEntityType.TYPE.equals(entityType)) {
            componentBed.getType().getName();
         } else if (BusinessEntityType.REALIZED_TYPE.equals(entityType)) {
            componentBed.getConcept().getName();
         } else if (BusinessEntityType.CONCEPT.equals(entityType)) {
            componentBed.getConcept().getName();
         } else if (BusinessEntityType.CONCEPT_PROFILE.equals(entityType)) {
            componentBed.getConceptProfile().getName();
         } else if (BusinessEntityType.RELATION.equals(entityType)) {
            componentBed.getRelation().getName();
         }

         cloudComponent.getComponentTypeBed().getType().getName();

      }
      return cloudComponents;
   }

   /**
    * @return the kdxCloudDataAccessManager
    */
   public IKDXCloudDataAccessManager getKdxCloudDataAccessManager () {
      return kdxCloudDataAccessManager;
   }

   /**
    * @param kdxCloudDataAccessManager
    *           the kdxCloudDataAccessManager to set
    */
   public void setKdxCloudDataAccessManager (IKDXCloudDataAccessManager kdxCloudDataAccessManager) {
      this.kdxCloudDataAccessManager = kdxCloudDataAccessManager;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.swi.IKDXCloudRetrievalService#getDefaultAppCloudByAppId(java.lang.Long)
    */
   public Cloud getDefaultAppCloudByAppId (Long appId) throws KDXException {
      Cloud defaultCloud = null;
      List<Model> modelsByApplicationId = getKdxDataAccessManager().getModelsByApplicationId(appId);
      for (Model model : modelsByApplicationId) {
         for (Cloud cloud : model.getClouds()) {
            if (cloud.getIsDefault().equals(CheckType.YES)) {
               defaultCloud = cloud;
               defaultCloud.getName();
               return defaultCloud;
            }
         }
      }
      return defaultCloud;
   }

   public Map<CloudCategory, List<RICloud>> getRICloudsByCompBedIdsAndCloudOutput (Set<Long> componentBedIds,
            CloudOutput cloudOutput) throws KDXException {
      return getRICloudsByCompBedIdsAndCloudOutput(componentBedIds, cloudOutput, null);
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.swi.IKDXCloudRetrievalService#getCloudByName(java.lang.String)
    */
   public Cloud getCloudById (Long cloudId) throws KDXException {
      Cloud cloud = getKdxCloudDataAccessManager().getCloudById(cloudId);
      if (cloud != null) {
         loadCloudDetails(cloud);
         Set<Model> models = cloud.getModels();
         if (!CollectionUtils.isEmpty(models)) {
            for (Model model : models) {
               Set<ModelGroup> modelGroups = model.getModelGroups();
               for (ModelGroup modelGroup : modelGroups) {
                  modelGroup.getId();
                  modelGroup.getName();
               }
            }
         }
      }
      return cloud;
   }

   public List<Cloud> getCloudsByCategory (CloudCategory category) throws KDXException {
      List<Cloud> cloudsForCategory = getKdxCloudDataAccessManager().getCloudsByCategory(category);
      if (CollectionUtils.isEmpty(cloudsForCategory)) {
         return new ArrayList<Cloud>(1);
      }
      for (Cloud cloud : cloudsForCategory) {
         if (cloud != null) {
            loadCloudDetails(cloud);
            Set<Model> models = cloud.getModels();
            if (!CollectionUtils.isEmpty(models)) {
               for (Model model : models) {
                  Set<ModelGroup> modelGroups = model.getModelGroups();
                  for (ModelGroup modelGroup : modelGroups) {
                     modelGroup.getId();
                     modelGroup.getName();
                  }
               }
            }
         }
      }
      return cloudsForCategory;
   }

   public IKDXDataAccessManager getKdxDataAccessManager () {
      return kdxDataAccessManager;
   }

   public void setKdxDataAccessManager (IKDXDataAccessManager kdxDataAccessManager) {
      this.kdxDataAccessManager = kdxDataAccessManager;
   }
}