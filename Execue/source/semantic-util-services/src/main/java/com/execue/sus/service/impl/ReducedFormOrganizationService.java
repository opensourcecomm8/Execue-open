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
package com.execue.sus.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.collections.CollectionUtils;

import com.execue.core.common.bean.nlp.DomainRecognition;
import com.execue.core.common.bean.nlp.SemanticPossibility;
import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.Instance;
import com.execue.core.common.bean.entity.InstanceProfile;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.common.bean.graph.Graph;
import com.execue.core.common.bean.graph.GraphPath;
import com.execue.core.common.bean.graph.IGraphComponent;
import com.execue.core.common.bean.graph.IGraphPath;
import com.execue.core.common.bean.nlp.ListNormalizedData;
import com.execue.core.common.bean.nlp.NLPConstants;
import com.execue.core.common.bean.nlp.NormalizedDataEntity;
import com.execue.core.common.type.ProfileType;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.sus.service.IReducedFormOrganizationService;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.PreferencesException;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.swi.service.IPreferencesRetrievalService;

/**
 * @author Nihar
 */
public class ReducedFormOrganizationService implements IReducedFormOrganizationService {

   private IKDXRetrievalService         kdxRetrievalService;
   private IPreferencesRetrievalService preferencesRetrievalService;

   public void disintegrateInstanceProfiles (SemanticPossibility semanticPossibility) throws PreferencesException,
            KDXException {
      Graph reducedFormGraph = semanticPossibility.getReducedForm();
      List<IGraphPath> graphPaths = new ArrayList<IGraphPath>(1);
      if (!CollectionUtils.isEmpty(reducedFormGraph.getPaths())) {
         graphPaths.addAll(reducedFormGraph.getPaths());
      }
      Model model = semanticPossibility.getModel();
      List<IGraphComponent> coveredRecognitions = new ArrayList<IGraphComponent>(1);
      for (IGraphPath path : graphPaths) {
         GraphPath graphPath = (GraphPath) path;
         int pathLength = graphPath.getPathLength();
         int start = 0;
         for (int i = 3; i <= pathLength;) {
            List<IGraphComponent> components = graphPath.getPartialPath(start, i);
            DomainRecognition source = (DomainRecognition) components.get(0);
            DomainRecognition destination = (DomainRecognition) components.get(2);
            coveredRecognitions.add(source);
            coveredRecognitions.add(destination);
            if (source.getNlpTag().equals(NLPConstants.NLP_TAG_ONTO_INSTANCE_PROFILE)) {
               processInstanceProfile(model, source);
            }
            if (destination.getNlpTag().equals(NLPConstants.NLP_TAG_ONTO_INSTANCE_PROFILE)) {
               processInstanceProfile(model, destination);
            }
            start = start + 2;
            i = i + 2;
         }
         Collection<IGraphComponent> rootVertices = semanticPossibility.getRootVertices();
         for (IGraphComponent rv : rootVertices) {
            DomainRecognition entity = (DomainRecognition) rv;
            if (!coveredRecognitions.contains(entity)) {
               if (entity.getNlpTag().equals(NLPConstants.NLP_TAG_ONTO_INSTANCE_PROFILE)) {
                  processInstanceProfile(model, entity);
               }
            }
         }

      }
   }

   /**
    * @param model
    * @param source
    * @throws PreferencesException
    * @throws KDXException
    */
   private void processInstanceProfile (Model model, DomainRecognition source) throws PreferencesException,
            KDXException {
      InstanceProfile instanceProfile = (InstanceProfile) getPreferencesRetrievalService().getProfile(model.getId(),
               source.getProfileName(), ProfileType.CONCEPT_LOOKUP_INSTANCE);
      Set<Instance> instances = instanceProfile.getInstances();
      List<NormalizedDataEntity> entities = new ArrayList<NormalizedDataEntity>(1);
      if (ExecueCoreUtil.isCollectionNotEmpty(instances)) {
         for (Instance instance : instances) {
            BusinessEntityDefinition businessEntityDefinition = kdxRetrievalService.getBusinessEntityDefinitionByIds(
                     model.getId(), instanceProfile.getConcept().getId(), instance.getId());
            NormalizedDataEntity normalizedDataEntity = new NormalizedDataEntity();
            normalizedDataEntity.setDisplayValue(instance.getDisplayName());
            normalizedDataEntity.setTypeBedId(source.getTypeBEDId());
            normalizedDataEntity.setValue(instance.getName());
            normalizedDataEntity.setValueBedId(businessEntityDefinition.getId());
            normalizedDataEntity.setValueKnowledgeId(businessEntityDefinition.getKnowledgeId());
            entities.add(normalizedDataEntity);
         }
      }
      // source.setInstanceBEDId(null);
      ListNormalizedData listNormalizedData = new ListNormalizedData();
      listNormalizedData.setNormalizedDataEntities(entities);
      TreeSet<Integer> posiList = new TreeSet<Integer>();
      posiList.add(source.getPosition());
      listNormalizedData.setReferredTokenPositions(posiList);
      listNormalizedData.setOriginalReferredTokenPositions(posiList);
      listNormalizedData.setTypeBedId(source.getTypeBEDId());
      source.setNormalizedData(listNormalizedData);
      source.setNlpTag(NLPConstants.NLP_TAG_ONTO_INSTANCE);
   }

   /**
    * @return the kdxRetrievalService
    */
   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   /**
    * @param kdxRetrievalService
    *           the kdxRetrievalService to set
    */
   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }

   public IPreferencesRetrievalService getPreferencesRetrievalService () {
      return preferencesRetrievalService;
   }

   public void setPreferencesRetrievalService (IPreferencesRetrievalService preferencesRetrievalService) {
      this.preferencesRetrievalService = preferencesRetrievalService;
   }
}
