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

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.Page;
import com.execue.core.common.bean.PageSearch;
import com.execue.core.common.type.PageSearchType;
import com.execue.core.constants.ExecueConstants;
import com.execue.core.exception.ExeCueException;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.core.exception.HandlerException;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.handler.bean.UIConcept;
import com.execue.handler.bean.UIPathDefinition;
import com.execue.handler.bean.UIRelation;
import com.execue.handler.swi.IAttributeAssociationServiceHandler;

public class AttributeAssociationAction extends SWIPaginationAction {

   private static final long                   serialVersionUID = 1L;
   private static final Logger                 log              = Logger.getLogger(AttributeAssociationAction.class);
   private List<UIConcept>                     timeFrameTypeconcepts;
   private List<UIPathDefinition>              existingPathDefinitions;
   private List<UIPathDefinition>              selectedPathDefinitions;
   private UIRelation                          relation;
   private Long                                conceptBedId;
   private String                              conceptName;
   private static final int                    PAGE_SIZE        = 11;
   private static final int                    NUMBER_OF_LINKS  = 4;

   private IAttributeAssociationServiceHandler attributeAssociationServiceHandler;

   public String showExistingPathDefinitions () {
      try {
         setTimeFrameTypeconcepts(getAttributeAssociationServiceHandler().getConceptsByType(
                  getApplicationContext().getModelId(), ExecueConstants.TIME_FRAME_TYPE));
         setRelation(getAttributeAssociationServiceHandler().getRelationByName(getApplicationContext().getModelId(),
                  ExecueConstants.TIME_FRAME_CONVERTABLE_RELATION));
         // remove the concept from the list in question
         UIConcept toBeRemovedConcept = new UIConcept();
         for (UIConcept concept : getTimeFrameTypeconcepts())
            if (concept.getBedId().equals(conceptBedId)) {
               toBeRemovedConcept = concept;
               conceptName = concept.getDisplayName();
            }

         getTimeFrameTypeconcepts().remove(toBeRemovedConcept);
         setExistingPathDefinitions(getAttributeAssociationServiceHandler().getExistingPathDefinitions(conceptBedId,
                  relation.getBedId()));
      } catch (HandlerException e) {
         e.printStackTrace();
         return ERROR;
      }
      return SUCCESS;
   }

   public String updatePathDefinitions () {
      try {
         getAttributeAssociationServiceHandler().associateAttributePathDefinitions(
                  getApplicationContext().getModelId(), getExistingPathDefinitions(), getSelectedPathDefinitions());
         addActionMessage(getText("execue.global.update.success", new String[] { conceptName }));
         showExistingPathDefinitions();
      } catch (HandlerException e) {
         addActionError(getText("execue.global.error", e.getMessage()));
         return ERROR;
      }
      return SUCCESS;
   }

   @SuppressWarnings ("unchecked")
   public String processPage () throws ExeCueException {
      try {
         getPageDetail().setPageSize(Long.valueOf(PAGE_SIZE));
         getPageDetail().setNumberOfLinks(Long.valueOf(NUMBER_OF_LINKS));

         List<UIConcept> allConcepts = getAttributeAssociationServiceHandler().getConceptsByType(
                  getApplicationContext().getModelId(), ExecueConstants.TIME_FRAME_TYPE);
         setRelation(getAttributeAssociationServiceHandler().getRelationByName(getApplicationContext().getModelId(),
                  ExecueConstants.TIME_FRAME_CONVERTABLE_RELATION));
         // set the record count
         getPageDetail().setRecordCount(Long.valueOf(allConcepts.size()));
         setTimeFrameTypeconcepts(getProcessedResults(allConcepts, getPageDetail()));

         getHttpRequest().put(PAGINATION, getPageDetail());
      } catch (Exception exception) {

         throw new ExeCueException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, exception);
      }
      return SUCCESS;
   }

   private List<UIConcept> getProcessedResults (List<UIConcept> allConcepts, Page pageDetail) {
      List<UIConcept> conceptList = allConcepts;
      List<PageSearch> searchList = pageDetail.getSearchList();
      if (ExecueCoreUtil.isCollectionNotEmpty(searchList)) {
         PageSearch search = searchList.get(0);
         if (PageSearchType.STARTS_WITH == search.getType()) {
            conceptList = new ArrayList<UIConcept>();
            for (UIConcept concept : allConcepts) {
               String cDispName = concept.getDisplayName();
               if (cDispName.toLowerCase().startsWith(search.getString().toLowerCase())) {
                  conceptList.add(concept);
               }
            }
         } else if (PageSearchType.CONTAINS == search.getType()) {
            conceptList = new ArrayList<UIConcept>();
            for (UIConcept concept : allConcepts) {
               String cDispName = concept.getDisplayName();
               if (cDispName.toLowerCase().contains(search.getString().toLowerCase())) {
                  conceptList.add(concept);
               }
            }
         }
      }
      // modify the page object with the new record count which will modify the page count as well
      pageDetail.setRecordCount(Long.valueOf(conceptList.size()));
      List<UIConcept> pageConcepts = new ArrayList<UIConcept>();
      // manipulate the list to return the set of concepts belonging to the page requested
      int start = (pageDetail.getRequestedPage().intValue() - 1) * pageDetail.getPageSize().intValue();
      int end = start + pageDetail.getPageSize().intValue();
      if (end > pageDetail.getRecordCount().intValue()) {
         end = (pageDetail.getRecordCount().intValue());
      }
      for (int i = start; i < end; i++) {
         pageConcepts.add(conceptList.get(i));
      }
      return pageConcepts;
   }

   public List<UIConcept> getTimeFrameTypeconcepts () {
      return timeFrameTypeconcepts;
   }

   public void setTimeFrameTypeconcepts (List<UIConcept> timeFrameTypeconcepts) {
      this.timeFrameTypeconcepts = timeFrameTypeconcepts;
   }

   public List<UIPathDefinition> getExistingPathDefinitions () {
      return existingPathDefinitions;
   }

   public void setExistingPathDefinitions (List<UIPathDefinition> existingPathDefinitions) {
      this.existingPathDefinitions = existingPathDefinitions;
   }

   public List<UIPathDefinition> getSelectedPathDefinitions () {
      return selectedPathDefinitions;
   }

   public void setSelectedPathDefinitions (List<UIPathDefinition> selectedPathDefinitions) {
      this.selectedPathDefinitions = selectedPathDefinitions;
   }

   public UIRelation getRelation () {
      return relation;
   }

   public void setRelation (UIRelation relation) {
      this.relation = relation;
   }

   public IAttributeAssociationServiceHandler getAttributeAssociationServiceHandler () {
      return attributeAssociationServiceHandler;
   }

   public void setAttributeAssociationServiceHandler (
            IAttributeAssociationServiceHandler attributeAssociationServiceHandler) {
      this.attributeAssociationServiceHandler = attributeAssociationServiceHandler;
   }

   public Long getConceptBedId () {
      return conceptBedId;
   }

   public void setConceptBedId (Long conceptBedId) {
      this.conceptBedId = conceptBedId;
   }

   public String getConceptName () {
      return conceptName;
   }

   public void setConceptName (String conceptName) {
      this.conceptName = conceptName;
   }

}
