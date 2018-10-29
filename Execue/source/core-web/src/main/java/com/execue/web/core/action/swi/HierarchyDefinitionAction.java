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


package com.execue.web.core.action.swi;

import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.entity.Hierarchy;
import com.execue.core.exception.ExeCueException;
import com.execue.core.exception.HandlerException;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.handler.bean.UIConcept;
import com.execue.handler.bean.UIStatus;
import com.execue.handler.swi.IHierarchyDefinitionServiceHandler;
import com.execue.swi.exception.SWIExceptionCodes;

/**
 * 
 * @author Jitendra
 *
 */

public class HierarchyDefinitionAction extends SWIPaginationAction {

   private static final Logger                logger = Logger.getLogger(HierarchyDefinitionAction.class);
   private IHierarchyDefinitionServiceHandler hierarchyDefinitionServiceHandler;
   private List<Hierarchy>                    hierarchies;
   private List<UIConcept>                    dimensions;
   private List<UIConcept>                    existingHierarchyDefinitions;
   private Hierarchy                          hierarchy;
   private List<Long>                         selectedHierarchyDefinitions;
   private UIStatus                           uiStatus;
   private Long                               maxHierarchyDefinitionSize;

   //Action Methods
   /**
    * dimensions:  Hierarchy list to show in left pane
    * @return
    */
   public String input () {
      try {
         Long modelId = getApplicationContext().getModelId();
         setMaxHierarchyDefinitionSize(getHierarchyDefinitionServiceHandler().getMaxHierarchySize());
         hierarchies = getHierarchyDefinitionServiceHandler().getHierarchiesForModel(modelId);
      } catch (HandlerException e) {
         logger.error(e);
         e.printStackTrace();
      }
      return SUCCESS;
   }

   /**
    * dimensions : Dimensions for model to show in left block of middle pane
    * existingHierarchyDefinitions : existing hierarchy definition
    * @return
    */
   public String getHierarchyDefinitions () {
      try {
         showHierarchyDefinitions();
      } catch (HandlerException e) {
         logger.error(e);
      }
      return SUCCESS;
   }

   public String showNewHierarchyDefinition () {
      Long modelId = getApplicationContext().getModelId();
      try {
         dimensions = getHierarchyDefinitionServiceHandler().getAllDimensionsForModel(modelId);
         Collections.sort(dimensions);
      } catch (HandlerException handlerException) {
         logger.error(handlerException);
      }
      return SUCCESS;
   }

   public String saveHierarchyDefinitions () {
      if (logger.isDebugEnabled()) {
         logger.debug(hierarchy.getId());
         logger.debug(hierarchy.getName());
         for (Long conceptBed : selectedHierarchyDefinitions) {
            logger.debug(conceptBed);
         }
      }
      try {
         hierarchy = getHierarchyDefinitionServiceHandler().saveUpdateHierarchyDefinitions(
                  getApplicationContext().getModelId(), hierarchy, selectedHierarchyDefinitions);
         showHierarchyDefinitions();
         addActionMessage(getText("execue.hierarchy.save.success"));
      } catch (HandlerException handlerException) {
         uiStatus = new UIStatus();
         uiStatus.setStatus(false);
         if (SWIExceptionCodes.ENTITY_ALREADY_EXISTS == 900018) {
            uiStatus.addErrorMessage(getText("execue.hierarchy.exists", new String[] { hierarchy.getName() }));
         } else {
            uiStatus.addErrorMessage(getText("execue.errors.general"));
         }
         logger.error(handlerException);
         return ERROR;
      }
      return SUCCESS;
   }

   public String deleteHierarchyDefinitions () {
      uiStatus = new UIStatus();
      try {
         getHierarchyDefinitionServiceHandler().deleteHierarchyDefinitions(hierarchy.getId());
         uiStatus.setMessage(getText("execue.hierarchy.delete.success"));
         uiStatus.setStatus(true);
      } catch (HandlerException handlerException) {
         uiStatus.setStatus(false);
         uiStatus.addErrorMessage(handlerException.getMessage());
         logger.error(handlerException);
         return ERROR;
      }
      return SUCCESS;
   }

   private void showHierarchyDefinitions () throws HandlerException {
      List<UIConcept> hierarchyDefinitions = getHierarchyDefinitionServiceHandler().getExistingHierarchyDefinitions(
               hierarchy.getId());
      dimensions = getHierarchyDefinitionServiceHandler()
               .getAllDimensionsForModel(getApplicationContext().getModelId());
      if (ExecueCoreUtil.isCollectionNotEmpty(hierarchyDefinitions)) {
         dimensions.removeAll(hierarchyDefinitions);
         existingHierarchyDefinitions = adjustHierarchyLevelWithWhiteSpace(hierarchyDefinitions);
      }
      Collections.sort(dimensions);
   }

   private List<UIConcept> adjustHierarchyLevelWithWhiteSpace (List<UIConcept> hierarchyDefinitions) {
      int index = 0;
      for (UIConcept concept : hierarchyDefinitions) {
         String hierarchyLevel = generateHierarchyLevelForUI(index);
         concept.setDisplayName(hierarchyLevel + concept.getDisplayName());
         index++;
      }
      return hierarchyDefinitions;
   }

   private String generateHierarchyLevelForUI (int index) {
      StringBuilder level = new StringBuilder();
      for (int i = 1; i <= index; i++) {
         level.append("&nbsp;&nbsp;&nbsp;");
      }
      return level.toString();
   }

   /**
    * @return the hierarchyDefinitionServiceHandler
    */
   public IHierarchyDefinitionServiceHandler getHierarchyDefinitionServiceHandler () {
      return hierarchyDefinitionServiceHandler;
   }

   /**
    * @param hierarchyDefinitionServiceHandler the hierarchyDefinitionServiceHandler to set
    */
   public void setHierarchyDefinitionServiceHandler (
            IHierarchyDefinitionServiceHandler hierarchyDefinitionServiceHandler) {
      this.hierarchyDefinitionServiceHandler = hierarchyDefinitionServiceHandler;
   }

   /**
    * @return the dimensions
    */
   public List<UIConcept> getDimensions () {
      return dimensions;
   }

   /**
    * @param dimensions the dimensions to set
    */
   public void setDimensions (List<UIConcept> dimensions) {
      this.dimensions = dimensions;
   }

   @Override
   public String processPage () throws ExeCueException {
      // We dont need pagination here as of now will see if need
      return null;
   }

   /**
    * @return the hierarchies
    */
   public List<Hierarchy> getHierarchies () {
      return hierarchies;
   }

   /**
    * @param hierarchies the hierarchies to set
    */
   public void setHierarchies (List<Hierarchy> hierarchies) {
      this.hierarchies = hierarchies;
   }

   /**
    * @return the existingHierarchyDefinitions
    */
   public List<UIConcept> getExistingHierarchyDefinitions () {
      return existingHierarchyDefinitions;
   }

   /**
    * @param existingHierarchyDefinitions the existingHierarchyDefinitions to set
    */
   public void setExistingHierarchyDefinitions (List<UIConcept> existingHierarchyDefinitions) {
      this.existingHierarchyDefinitions = existingHierarchyDefinitions;
   }

   /**
    * @return the hierarchy
    */
   public Hierarchy getHierarchy () {
      return hierarchy;
   }

   /**
    * @param hierarchy the hierarchy to set
    */
   public void setHierarchy (Hierarchy hierarchy) {
      this.hierarchy = hierarchy;
   }

   /**
    * @return the selectedHierarchyDefinitions
    */
   public List<Long> getSelectedHierarchyDefinitions () {
      return selectedHierarchyDefinitions;
   }

   /**
    * @param selectedHierarchyDefinitions the selectedHierarchyDefinitions to set
    */
   public void setSelectedHierarchyDefinitions (List<Long> selectedHierarchyDefinitions) {
      this.selectedHierarchyDefinitions = selectedHierarchyDefinitions;
   }

   /**
    * @return the uiStatus
    */
   public UIStatus getUiStatus () {
      return uiStatus;
   }

   /**
    * @param uiStatus the uiStatus to set
    */
   public void setUiStatus (UIStatus uiStatus) {
      this.uiStatus = uiStatus;
   }

   /**
    * @return the maxHierarchyDefinitionSize
    */
   public Long getMaxHierarchyDefinitionSize () {
      return maxHierarchyDefinitionSize;
   }

   /**
    * @param maxHierarchyDefinitionSize the maxHierarchyDefinitionSize to set
    */
   public void setMaxHierarchyDefinitionSize (Long maxHierarchyDefinitionSize) {
      this.maxHierarchyDefinitionSize = maxHierarchyDefinitionSize;
   }

}
