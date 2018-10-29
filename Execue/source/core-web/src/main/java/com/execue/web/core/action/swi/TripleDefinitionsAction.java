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

import java.util.Arrays;
import java.util.List;

import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.type.HierarchyRelationType;
import com.execue.core.exception.ExeCueException;
import com.execue.core.exception.HandlerException;
import com.execue.handler.bean.UIConcept;
import com.execue.handler.bean.UIPathDefinition;
import com.execue.handler.bean.UIRelation;
import com.execue.handler.swi.ITripleDefinitionsServiceHandler;

/**
 * @author JTiwari
 */

public class TripleDefinitionsAction extends SWIAction {

   private Concept                          concept;
   private List<UIConcept>                  eligibleHierarchyConcepts;
   private List<UIConcept>                  eligibleRelationConcepts;
   private List<UIPathDefinition>           selectedHierarchyPathDefinitions;
   private List<UIPathDefinition>           savedHierarchyPathDefinitions;
   private List<UIPathDefinition>           selectedRelationPathDefinitions;
   private List<UIPathDefinition>           savedRelationPathDefinitions;
   private HierarchyRelationType            selectedHierarchyType;
   private String                           search;
   private List<UIRelation>                 relations;
   private ITripleDefinitionsServiceHandler tripleDefinitionsServiceHandler;

   // Action methods

   public String showRelationDetails () {
      try {
         Long modelId = getApplicationContext().getModelId();
         setConcept(getKdxServiceHandler().getPopulatedConceptWithStats(concept.getId()));
         prepareRelationList(modelId, concept);
      } catch (ExeCueException e) {
         e.printStackTrace();
      }
      return SUCCESS;
   }

   public String showHierarchy () {
      try {
         // TODO:- we are making two call to get concept, one with relation and other with hierarchy, need to avoid one
         Long modelId = getApplicationContext().getModelId();
         setConcept(getKdxServiceHandler().getPopulatedConceptWithStats(concept.getId()));
      } catch (ExeCueException e) {
         e.printStackTrace();
      }
      return SUCCESS;
   }

   public String showHierarchyDetails () {
      try {
         Long modelId = getApplicationContext().getModelId();
         prepareHierarchyList(modelId, concept);
      } catch (HandlerException e) {
         e.printStackTrace();
      }
      return SUCCESS;
   }

   public String saveHierarchyPathDefinitions () {
      Long modelId = getApplicationContext().getModelId();
      try {
         getTripleDefinitionsServiceHandler().saveHierarchyPathDefinitions(modelId, selectedHierarchyType,
                  selectedHierarchyPathDefinitions, savedHierarchyPathDefinitions);
      } catch (HandlerException e) {
         e.printStackTrace();
      } finally {
         try {
            prepareHierarchyList(modelId, concept);
         } catch (HandlerException e) {
            e.printStackTrace();
         }
      }
      return SUCCESS;
   }

   public String saveRelationPathDefinitions () {
      Long modelId = getApplicationContext().getModelId();
      try {
         getTripleDefinitionsServiceHandler().saveRelationPathDefinitions(modelId, selectedRelationPathDefinitions,
                  savedRelationPathDefinitions);
         addActionMessage("Relation added successfully");
      } catch (HandlerException e) {
         e.printStackTrace();
      } finally {
         try {
            prepareRelationList(modelId, concept);
         } catch (HandlerException e) {
            e.printStackTrace();
         }
      }
      return SUCCESS;

   }

   public String suggestRelations () {
      try {
         setRelations(getTripleDefinitionsServiceHandler().suggestRelationsByName(getApplicationContext().getModelId(),
                  search));
      } catch (HandlerException e) {
         e.printStackTrace();
      }
      return SUCCESS;
   }

   private void prepareHierarchyList (Long modelId, Concept concept) throws HandlerException {
      setEligibleHierarchyConcepts(getTripleDefinitionsServiceHandler().getUIConceptsForHeirarchy(modelId,
               concept.getBedId(), selectedHierarchyType));
      setSelectedHierarchyPathDefinitions(getTripleDefinitionsServiceHandler().getUIPathDefinitionsForHeirarchy(
               modelId, concept.getBedId(), selectedHierarchyType));

   }

   private void prepareRelationList (Long modelId, Concept concept) throws HandlerException {
      setEligibleRelationConcepts(getTripleDefinitionsServiceHandler().getUIConceptsForRelation(modelId,
               concept.getBedId()));
      setSelectedRelationPathDefinitions(getTripleDefinitionsServiceHandler().getUIPathDefinitionsForRelation(modelId,
               concept.getBedId()));
   }

   public List<HierarchyRelationType> getRelationHiearchyTypes () {
      return Arrays.asList(HierarchyRelationType.values());
   }

   public List<UIConcept> getEligibleHierarchyConcepts () {
      return eligibleHierarchyConcepts;
   }

   public void setEligibleHierarchyConcepts (List<UIConcept> eligibleHierarchyConcepts) {
      this.eligibleHierarchyConcepts = eligibleHierarchyConcepts;
   }

   public List<UIConcept> getEligibleRelationConcepts () {
      return eligibleRelationConcepts;
   }

   public void setEligibleRelationConcepts (List<UIConcept> eligibleRelationConcepts) {
      this.eligibleRelationConcepts = eligibleRelationConcepts;
   }

   public List<UIPathDefinition> getSelectedHierarchyPathDefinitions () {
      return selectedHierarchyPathDefinitions;
   }

   public void setSelectedHierarchyPathDefinitions (List<UIPathDefinition> selectedHierarchyPathDefinitions) {
      this.selectedHierarchyPathDefinitions = selectedHierarchyPathDefinitions;
   }

   public List<UIPathDefinition> getSavedHierarchyPathDefinitions () {
      return savedHierarchyPathDefinitions;
   }

   public void setSavedHierarchyPathDefinitions (List<UIPathDefinition> savedHierarchyPathDefinitions) {
      this.savedHierarchyPathDefinitions = savedHierarchyPathDefinitions;
   }

   public List<UIPathDefinition> getSelectedRelationPathDefinitions () {
      return selectedRelationPathDefinitions;
   }

   public void setSelectedRelationPathDefinitions (List<UIPathDefinition> selectedRelationPathDefinitions) {
      this.selectedRelationPathDefinitions = selectedRelationPathDefinitions;
   }

   public List<UIPathDefinition> getSavedRelationPathDefinitions () {
      return savedRelationPathDefinitions;
   }

   public void setSavedRelationPathDefinitions (List<UIPathDefinition> savedRelationPathDefinitions) {
      this.savedRelationPathDefinitions = savedRelationPathDefinitions;
   }

   public ITripleDefinitionsServiceHandler getTripleDefinitionsServiceHandler () {
      return tripleDefinitionsServiceHandler;
   }

   public void setTripleDefinitionsServiceHandler (ITripleDefinitionsServiceHandler tripleDefinitionsServiceHandler) {
      this.tripleDefinitionsServiceHandler = tripleDefinitionsServiceHandler;
   }

   public Concept getConcept () {
      return concept;
   }

   public void setConcept (Concept concept) {
      this.concept = concept;
   }

   public HierarchyRelationType getSelectedHierarchyType () {
      return selectedHierarchyType;
   }

   public void setSelectedHierarchyType (HierarchyRelationType selectedHierarchyType) {
      this.selectedHierarchyType = selectedHierarchyType;
   }

   public List<UIRelation> getRelations () {
      return relations;
   }

   public void setRelations (List<UIRelation> relations) {
      this.relations = relations;
   }

   public String getSearch () {
      return search;
   }

   public void setSearch (String search) {
      this.search = search;
   }

}
