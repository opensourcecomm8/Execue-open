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


package com.execue.web.core.action.uss;

import java.util.List;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.entity.Model;
import com.execue.core.exception.HandlerException;
import com.execue.handler.bean.UIConcept;
import com.execue.handler.bean.UIInstance;
import com.execue.handler.bean.UIStatus;
import com.execue.handler.uss.IUnstructuredDynamicKnowledgeServiceHandler;
import com.execue.swi.exception.SWIExceptionCodes;
import com.opensymphony.xwork2.ActionSupport;

/**
 * This Action class contains methods to take user input(knowledge) and process them which will be further used during
 * unstructured search.
 * 
 * @author jitendra
 */
public class UnstructuredDynamicKnowledgeAbsorptionAction extends ActionSupport {

   private static final long                           serialVersionUID = 1L;
   private static final Logger                         logger           = Logger
                                                                                 .getLogger(UnstructuredDynamicKnowledgeAbsorptionAction.class);

   private Long                                        modelId;
   private Long                                        conceptId;
   private String                                      instanceName;
   private Long                                        applicationId;
   private String                                      applicationName;
   private IUnstructuredDynamicKnowledgeServiceHandler unstructuredDynamicKnowledgeServiceHandler;
   private List<UIConcept>                             concepts;
   private UIStatus                                    status;
   private String                                      search;
   private List<UIInstance>                            instances;

   // Action methods

   public String input () {
      try {
         if (modelId == null || modelId.intValue()==-1) {
            Model model = getUnstructuredDynamicKnowledgeServiceHandler().getModelsByApplicationId(applicationId);
            if (model != null) {
               setModelId(model.getId());
            }
         }
      } catch (HandlerException e) {
         e.printStackTrace();
      }
      return SUCCESS;
   }

   public String createInstance () {
      if (logger.isDebugEnabled()) {
         logger.debug("Create instance");
      }
      try {
         status = new UIStatus();
         getUnstructuredDynamicKnowledgeServiceHandler()
                  .createInstance(applicationId, modelId, conceptId, instanceName);
         status.addErrorMessage(getText("execue.instance.create.success"));
      } catch (HandlerException handlerException) {
         logger.error(handlerException);
         if (SWIExceptionCodes.ENTITY_ALREADY_EXISTS == handlerException.getCode()) {
            status.addErrorMessage(getText("execue.global.exist.message", new String[] { instanceName }));
         } else if (SWIExceptionCodes.RESERVE_WORD_MATCH == handlerException.getCode()) {
            status.addErrorMessage(getText("execue.kdx.reservedword.message", new String[] { instanceName }));
         } else {
            status.addErrorMessage(getText("execue.global.error", handlerException.getMessage()));
         }
         status.setStatus(false);
      }
      return SUCCESS;
   }

   public String suggestConcepts () {
      if (logger.isDebugEnabled()) {
         logger.debug("Suggest existing concepts");
      }
      try {
         setConcepts(getUnstructuredDynamicKnowledgeServiceHandler().suggestLookTypeConcepts(modelId, search));
      } catch (HandlerException handlerException) {
         logger.error(handlerException);
      }
      return SUCCESS;
   }

   public String showInstances () {
      if (logger.isDebugEnabled()) {
         logger.debug("Suggest existing concepts");
      }
      try {
         setInstances(getUnstructuredDynamicKnowledgeServiceHandler().getInstancesForConcept(modelId, conceptId));
      } catch (HandlerException handlerException) {
         logger.error(handlerException);
      }
      return SUCCESS;
   }

   public boolean isResemanticationCheckBoxVisible () {
      return getUnstructuredDynamicKnowledgeServiceHandler().isResemantificationCheckBoxVisible();
   }

   /**
    * @return the modelId
    */
   public Long getModelId () {
      return modelId;
   }

   /**
    * @param modelId
    *           the modelId to set
    */
   public void setModelId (Long modelId) {
      this.modelId = modelId;
   }

   /**
    * @return the conceptId
    */
   public Long getConceptId () {
      return conceptId;
   }

   /**
    * @param conceptId
    *           the conceptId to set
    */
   public void setConceptId (Long conceptId) {
      this.conceptId = conceptId;
   }

   /**
    * @return the instanceName
    */
   public String getInstanceName () {
      return instanceName;
   }

   /**
    * @param instanceName
    *           the instanceName to set
    */
   public void setInstanceName (String instanceName) {
      this.instanceName = instanceName;
   }

   /**
    * @return the applicationName
    */
   public String getApplicationName () {
      return applicationName;
   }

   /**
    * @param applicationName
    *           the applicationName to set
    */
   public void setApplicationName (String applicationName) {
      this.applicationName = applicationName;
   }

   /**
    * @return the concepts
    */
   public List<UIConcept> getConcepts () {
      return concepts;
   }

   /**
    * @param concepts
    *           the concepts to set
    */
   public void setConcepts (List<UIConcept> concepts) {
      this.concepts = concepts;
   }

   /**
    * @return the applicationId
    */
   public Long getApplicationId () {
      return applicationId;
   }

   /**
    * @param applicationId
    *           the applicationId to set
    */
   public void setApplicationId (Long applicationId) {
      this.applicationId = applicationId;
   }

   /**
    * @return the unstructuredDynamicKnowledgeServiceHandler
    */
   public IUnstructuredDynamicKnowledgeServiceHandler getUnstructuredDynamicKnowledgeServiceHandler () {
      return unstructuredDynamicKnowledgeServiceHandler;
   }

   /**
    * @param unstructuredDynamicKnowledgeServiceHandler
    *           the unstructuredDynamicKnowledgeServiceHandler to set
    */
   public void setUnstructuredDynamicKnowledgeServiceHandler (
            IUnstructuredDynamicKnowledgeServiceHandler unstructuredDynamicKnowledgeServiceHandler) {
      this.unstructuredDynamicKnowledgeServiceHandler = unstructuredDynamicKnowledgeServiceHandler;
   }

   /**
    * @return the status
    */
   public UIStatus getStatus () {
      return status;
   }

   /**
    * @param status
    *           the status to set
    */
   public void setStatus (UIStatus status) {
      this.status = status;
   }

   /**
    * @return the instances
    */
   public List<UIInstance> getInstances () {
      return instances;
   }

   /**
    * @param instances
    *           the instances to set
    */
   public void setInstances (List<UIInstance> instances) {
      this.instances = instances;
   }

   public String getSearch () {
      return search;
   }

   public void setSearch (String search) {
      this.search = search;
   }

}
