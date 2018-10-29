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


package com.execue.platform.swi.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.AssetEntityDefinition;
import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.Instance;
import com.execue.core.common.bean.entity.Mapping;
import com.execue.core.common.bean.entity.Membr;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.common.bean.entity.ModelGroup;
import com.execue.core.common.bean.entity.Tabl;
import com.execue.core.common.type.BusinessEntityType;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.MappingException;
import com.execue.swi.exception.SDXException;
import com.execue.swi.exception.SWIException;
import com.execue.swi.service.IKDXManagementService;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.swi.service.IMappingManagementService;
import com.execue.swi.service.ISDXRetrievalService;

/**
 * @author Vishay
 */
public class BulkInstanceMappingCreation {

   private ISDXRetrievalService      sdxRetrievalService;
   private IKDXRetrievalService      kdxRetrievalService;
   private IKDXManagementService     kdxManagementService;
   private IMappingManagementService mappingManagementService;
   private final Logger              logger = Logger.getLogger(BulkInstanceMappingCreation.class);

   public void createInstanceMappings (Long modelId, Long conceptId, Long assetId, Long tableId, Long columId,
            Long startingMemberId, int batchSize) {
      try {

         Model model = kdxRetrievalService.getModelById(modelId);
         Concept concept = kdxRetrievalService.getConceptById(conceptId);
         Asset asset = getSdxRetrievalService().getAssetById(assetId);
         Tabl table = getSdxRetrievalService().getTableById(tableId);
         Colum colum = getSdxRetrievalService().getColumnById(columId);

         List<Membr> members = getColumMembers(colum);
         logger.debug("Number of Members found for colum " + colum.getId() + " is : " + members.size());
         List<Membr> membersInIteration = new ArrayList<Membr>();
         List<Instance> instances = createInstancesFromMembers(members, startingMemberId, batchSize, membersInIteration);
         populateMappings(model, concept, asset, table, colum, membersInIteration, instances);
      } catch (SWIException e) {
         e.printStackTrace();
      }
   }

   private void populateMappings (Model model, Concept concept, Asset asset, Tabl tabl, Colum colum,
            List<Membr> members, List<Instance> instances) throws KDXException, MappingException {
      // get the existing instances and delete them.
      // List<Instance> existingInstancesForConcept = kdxRetrievalService.getInstances(model.getId(), concept.getId());
      Map<String, Instance> existingInstancesMap = kdxRetrievalService.getInstanceDisplayNameMap(model.getId(), concept
               .getId());
      // getKdxService().deleteInstances(domain, concept, existingInstances);
      // create new instances in swi.
      // getKdxService().createInstances(domain, concept, instances);
      // prepare mapping between member and instance.
      List<Mapping> mappings = new ArrayList<Mapping>();
      long startTime = System.currentTimeMillis();
      if (logger.isDebugEnabled()) {
         logger.debug("Preparing mappings... ");
      }
      List<Instance> existingInstances = getKdxRetrievalService().getInstances(model.getId(), concept.getId());
      int counter = 1;
      if (!CollectionUtils.isEmpty(existingInstances)) {
         counter = existingInstances.size() + 1;
      }
      ModelGroup modelGroup = getKdxRetrievalService().getPrimaryGroup(model.getId());
      for (int i = 0; i < members.size(); i++) {
         AssetEntityDefinition aed = getSdxRetrievalService().getAssetEntityDefinitionByIds(asset, tabl, colum,
                  members.get(i));
         Instance currInstance = instances.get(i);
         /*
          * boolean isExistingFound = false; for (Instance instance : existingInstancesForConcept) { if
          * (currInstance.getDisplayName().equals(instance.getDisplayName())) { currInstance = instance; isExistingFound =
          * true; break; } }
          */
         BusinessEntityDefinition bed = null;
         if (existingInstancesMap.containsKey(currInstance.getDisplayName())) {
            currInstance = existingInstancesMap.get(currInstance.getDisplayName());
            bed = kdxRetrievalService.getBusinessEntityDefinitionByIds(model.getId(), concept.getId(), currInstance
                     .getId());
            if (logger.isDebugEnabled()) {
               logger.debug("Found existing instance with display name: " + currInstance.getDisplayName());
            }
         } else {
            bed = createInstance(modelGroup, concept, currInstance, counter++);
         }
         /*
          * if (!isExistingFound) { kdxManagementService.createInstance(model.getId(), concept.getId(), currInstance); }
          * BusinessEntityDefinition bed = kdxRetrievalService.getBusinessEntityDefinitionByIds(model.getId(), concept
          * .getId(), currInstance.getId());
          */
         Mapping mapping = new Mapping();
         mapping.setAssetEntityDefinition(aed);
         mapping.setBusinessEntityDefinition(bed);
         // List<Mapping> mappings = new ArrayList<Mapping>();
         mappings.add(mapping);
         // getMappingService().createMappings(mappings);
      }
      if (logger.isDebugEnabled()) {
         long endTime = System.currentTimeMillis();
         logger.debug("Time taken to prepare mappings: " + (endTime - startTime));
      }
      startTime = System.currentTimeMillis();
      if (logger.isDebugEnabled()) {
         logger.debug("Saving mappings... " + mappings.size());
      }
      getMappingManagementService().saveOrUpdateMappings(mappings);
      if (logger.isDebugEnabled()) {
         long endTime = System.currentTimeMillis();
         logger.debug("Time taken to save " + mappings.size() + " mappings: " + (endTime - startTime));
      }
   }

   private void adjustInstanceName (Concept concept, Instance instance, int counter) throws KDXException {
      instance.setName(concept.getName() + (counter + 1));
   }

   public BusinessEntityDefinition createInstance (ModelGroup modelGroup, Concept concept, Instance instance,
            int counter) throws KDXException {
      adjustInstanceName(concept, instance, counter);
      BusinessEntityDefinition businessEntityDefinition = new BusinessEntityDefinition();
      businessEntityDefinition.setModelGroup(modelGroup);
      businessEntityDefinition.setConcept(concept);
      businessEntityDefinition.setInstance(instance);
      businessEntityDefinition.setEntityType(BusinessEntityType.CONCEPT_LOOKUP_INSTANCE);
      return businessEntityDefinition;
   }

   private List<Instance> createInstancesFromMembers (List<Membr> members, Long startingMemberId, int batchSize,
            List<Membr> membersInIteration) {
      Set<String> usedMemberDescriptions = new HashSet<String>();

      if (startingMemberId == null) {
         startingMemberId = members.get(0).getId();
      }

      if (batchSize == 0) {
         batchSize = new Long((members.get(members.size() - 1).getId() - startingMemberId) + 1).intValue();
      }

      Long endingMemberId = startingMemberId + batchSize;

      List<Instance> instances = new ArrayList<Instance>();

      for (Membr membr : members) {
         if (membr.getId() >= endingMemberId) {
            break;
         }
         String instanceDescription = membr.getLookupDescription();
         if (instanceDescription == null) {
            instanceDescription = membr.getLookupValue();
         } else if (usedMemberDescriptions.contains(membr.getLookupDescription())) {
            instanceDescription = instanceDescription + " " + (membr.getLookupValue());
         } else {
            usedMemberDescriptions.add(instanceDescription);
         }
         if (membr.getId() >= startingMemberId) {
            Instance instance = new Instance();
            instance.setDescription(instanceDescription);
            instance.setDisplayName(instanceDescription);
            instances.add(instance);
            membersInIteration.add(membr);
         }
      }
      return instances;
   }

   private List<Membr> getColumMembers (Colum colum) throws SDXException {
      List<Membr> columnMembers = getSdxRetrievalService().getColumnMembers(colum);
      Collections.sort(columnMembers, new SortMemberComparator());
      return columnMembers;
   }

   public class SortMemberComparator implements Comparator<Membr> {

      public int compare (Membr first, Membr second) {
         return first.getId().compareTo(second.getId());
      }
   }

   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }

   public IKDXManagementService getKdxManagementService () {
      return kdxManagementService;
   }

   public void setKdxManagementService (IKDXManagementService kdxManagementService) {
      this.kdxManagementService = kdxManagementService;
   }

   public ISDXRetrievalService getSdxRetrievalService () {
      return sdxRetrievalService;
   }

   public void setSdxRetrievalService (ISDXRetrievalService sdxRetrievalService) {
      this.sdxRetrievalService = sdxRetrievalService;
   }

   public IMappingManagementService getMappingManagementService () {
      return mappingManagementService;
   }

   public void setMappingManagementService (IMappingManagementService mappingManagementService) {
      this.mappingManagementService = mappingManagementService;
   }

}
