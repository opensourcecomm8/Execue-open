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


package com.execue.handler.uss.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.entity.Application;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.Instance;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.constants.ExecueConstants;
import com.execue.core.exception.ExeCueException;
import com.execue.core.exception.HandlerException;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.core.util.ExecueStringUtil;
import com.execue.handler.bean.UIConcept;
import com.execue.handler.bean.UIInstance;
import com.execue.handler.swi.IKDXServiceHandler;
import com.execue.handler.uss.IUnstructuredDynamicKnowledgeServiceHandler;
import com.execue.swi.configuration.ILocationConfigurationService;
import com.execue.swi.exception.KDXException;
import com.execue.swi.service.IApplicationRetrievalService;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.uss.configuration.IUnstructuredSearchConfigurationService;

public class UnstructuredDynamicKnowledgeServiceHandlerImpl implements IUnstructuredDynamicKnowledgeServiceHandler {

   private static final Logger                     logger = Logger
                                                                   .getLogger(UnstructuredDynamicKnowledgeServiceHandlerImpl.class);

   private IKDXServiceHandler                      kdxServiceHandler;
   private IKDXRetrievalService                    kdxRetrievalService;
   private IUnstructuredSearchConfigurationService unstructuredSearchConfigurationService;
   private ILocationConfigurationService           locationConfigurationService;
   private IApplicationRetrievalService            applicationRetrievalService;

   @Override
   public void createInstance (Long applicationId, Long modelId, Long conceptId, String instanceName)
            throws HandlerException {
      try {
         Application application = getApplicationRetrievalService().getApplicationById(applicationId);
         getKdxServiceHandler().createInstance(applicationId, modelId, application.getSourceType(), conceptId,
                  prepareInstance(instanceName));
      } catch (ExeCueException execueException) {
         throw new HandlerException(execueException.getCode(), execueException);
      }

   }

   @Override
   public List<UIConcept> suggestLookTypeConcepts (Long modelId, String searchString) throws HandlerException {
      List<UIConcept> uiConcepts = null;
      //TODO-JT- need to read limits from configuration
      Long conceptRetrivalLimt = 10L;
      List<Long> locationRealizedTypeIds = getLocationConfigurationService().getChildIdsByParentBedId(
               ExecueConstants.LOCATION_TYPE_BED_ID);
      try {
         List<Concept> conceptsByPopularity = getKdxRetrievalService().getLookupTypeConceptsForModelBySearchString(
                  modelId, searchString, locationRealizedTypeIds, conceptRetrivalLimt);
         uiConcepts = transformUIConcepts(conceptsByPopularity);

      } catch (KDXException e) {
         logger.error(e);
      }
      return uiConcepts;
   }

   @Override
   public List<UIInstance> getInstancesForConcept (Long modelId, Long conceptId) throws HandlerException {
      //TODO-JT- need to read limits from configuration
      Long instanceRetrivalLimt = 5L;
      List<UIInstance> uiInstances = null;
      try {
         List<Instance> instances = getKdxRetrievalService().getInstancesByPopularity(modelId, conceptId,
                  instanceRetrivalLimt);
         uiInstances = transformUIInstances(instances);
      } catch (KDXException e) {
         logger.error(e);
      }
      return uiInstances;
   }

   @Override
   public Boolean isResemantificationCheckBoxVisible () {
      return getUnstructuredSearchConfigurationService().isResemantificationCheckBoxVisible();
   }

   @Override
   public Model getModelsByApplicationId (Long applicationId) throws HandlerException {
      Model model = null;
      try {
         List<Model> models = getKdxRetrievalService().getModelsByApplicationId(applicationId);
         if (ExecueCoreUtil.isCollectionNotEmpty(models)) {
            model = models.get(0);
         }
      } catch (KDXException e) {
         e.printStackTrace();
         throw new HandlerException(e.getCode(), e);
      }
      return model;
   }

   private List<UIInstance> transformUIInstances (List<Instance> instances) {
      List<UIInstance> uiInstances = new ArrayList<UIInstance>();
      if (ExecueCoreUtil.isCollectionNotEmpty(instances)) {
         for (Instance instance : instances) {
            UIInstance uiInstance = new UIInstance();
            uiInstance.setId(instance.getId());
            uiInstance.setDisplayName(instance.getDisplayName());
            uiInstances.add(uiInstance);
         }
      }
      return uiInstances;
   }

   private List<UIConcept> transformUIConcepts (List<Concept> concepts) {
      List<UIConcept> uiConcepts = new ArrayList<UIConcept>();
      if (ExecueCoreUtil.isCollectionNotEmpty(concepts)) {
         for (Concept concept : concepts) {
            UIConcept uiConcept = new UIConcept();
            uiConcept.setDisplayName(concept.getDisplayName());
            uiConcept.setId(concept.getId());
            uiConcepts.add(uiConcept);
         }

      }

      return uiConcepts;
   }

   private Instance prepareInstance (String instanceName) {
      Instance instance = new Instance();
      instance.setName(ExecueStringUtil.getNormalizedName(instanceName));
      instance.setDisplayName(instanceName);
      return instance;
   }

   /**
    * @return the kdxServiceHandler
    */
   public IKDXServiceHandler getKdxServiceHandler () {
      return kdxServiceHandler;
   }

   /**
    * @param kdxServiceHandler the kdxServiceHandler to set
    */
   public void setKdxServiceHandler (IKDXServiceHandler kdxServiceHandler) {
      this.kdxServiceHandler = kdxServiceHandler;
   }

   /**
    * @return the kdxRetrievalService
    */
   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   /**
    * @param kdxRetrievalService the kdxRetrievalService to set
    */
   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }

   /**
    * @return the unstructuredSearchConfigurationService
    */
   public IUnstructuredSearchConfigurationService getUnstructuredSearchConfigurationService () {
      return unstructuredSearchConfigurationService;
   }

   /**
    * @param unstructuredSearchConfigurationService the unstructuredSearchConfigurationService to set
    */
   public void setUnstructuredSearchConfigurationService (
            IUnstructuredSearchConfigurationService unstructuredSearchConfigurationService) {
      this.unstructuredSearchConfigurationService = unstructuredSearchConfigurationService;
   }

   /**
    * @return the locationConfigurationService
    */
   public ILocationConfigurationService getLocationConfigurationService () {
      return locationConfigurationService;
   }

   /**
    * @param locationConfigurationService the locationConfigurationService to set
    */
   public void setLocationConfigurationService (ILocationConfigurationService locationConfigurationService) {
      this.locationConfigurationService = locationConfigurationService;
   }

   /**
    * @return the applicationRetrievalService
    */
   public IApplicationRetrievalService getApplicationRetrievalService () {
      return applicationRetrievalService;
   }

   /**
    * @param applicationRetrievalService the applicationRetrievalService to set
    */
   public void setApplicationRetrievalService (IApplicationRetrievalService applicationRetrievalService) {
      this.applicationRetrievalService = applicationRetrievalService;
   }

}
