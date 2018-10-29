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


package com.execue.ks.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.execue.core.common.bean.nlp.DomainRecognition;
import com.execue.core.common.bean.nlp.SemanticPossibility;
import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.graph.IGraphComponent;
import com.execue.core.common.bean.nlp.NLPConstants;
import com.execue.ks.exception.KnowledgeSearchException;
import com.execue.ks.service.IEntityLookup;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.SWIException;
import com.execue.swi.service.IKDXModelService;
import com.execue.swi.service.IKDXRetrievalService;

/**
 * @author Laura
 */
public class EntityLookupImpl implements IEntityLookup {

   private IKDXRetrievalService kdxRetrievalService;
   private IKDXModelService     kdxModelService;

   public IKDXModelService getKdxModelService () {
      return kdxModelService;
   }

   public void setKdxModelService (IKDXModelService kdxModelService) {
      this.kdxModelService = kdxModelService;
   }

   /**
    * Performs loose search of the entities in a SemanticPossibility object
    * 
    * @param possibility
    *           the SemanticPossibility of the user query
    * @return List of BusinessEntity IDs of the entities found
    * @throws KnowledgeSearchException
    */
   public Set<Long> search (SemanticPossibility possibility) throws KnowledgeSearchException {
      Set<Long> beids = null;
      List<String> conceptNames = new ArrayList<String>();
      List<String> instanceNames = new ArrayList<String>();
      List<Long> conceptBEIDS = new ArrayList<Long>();
      List<Long> instanceBEIDS = new ArrayList<Long>();

      // look for entities in the SemanticPossibility
      for (IGraphComponent c : possibility.getAllGraphComponents()) {
         DomainRecognition gc = (DomainRecognition) c;
         if (NLPConstants.NLP_TAG_ONTO_INSTANCE.equals(gc.getNlpTag())) {
            instanceNames.add(gc.getBusinessEntityName());
            try {
               conceptBEIDS.add(kdxRetrievalService.getConceptBEDByName(possibility.getModel().getId(),
                        gc.getConceptName()).getId());
               /*
                * for (BusinessEntityDefinition e : kdxRetrievalService.getInstanceBEDsByName(possibility.getModelId(),
                * gc .getBusinessEntityName())) {
                */
               BusinessEntityDefinition bed = kdxRetrievalService.getInstanceBEDByName(possibility.getModel().getId(),
                        gc.getConceptName(), gc.getBusinessEntityName());
               instanceBEIDS.add(bed.getId());
               // }
            } catch (KDXException e) {
               throw new KnowledgeSearchException(e.getCode(), e);
            }
         }
         if (NLPConstants.NLP_TAG_ONTO_CONCEPT.equals(gc.getNlpTag())) {
            conceptNames.add(gc.getBusinessEntityName());
            try {
               conceptBEIDS.add(kdxRetrievalService.getConceptBEDByName(possibility.getModel().getId(),
                        gc.getConceptName()).getId());
            } catch (KDXException e) {
               throw new KnowledgeSearchException(e.getCode(), e);
            }
         }
      }
      try {
         if (instanceBEIDS.isEmpty()) {
            beids = getKdxModelService().getRelatedBEDIdsByConceptBEDIds(conceptBEIDS);
         } else {
            beids = getKdxModelService().getRelatedBEDIdsByInstanceConceptBEDIds(instanceBEIDS, conceptBEIDS);
         }
      } catch (SWIException e) {
         throw new KnowledgeSearchException(e.getCode(), e);
      }

      return beids;
   }

   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }

}
