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


package com.execue.ontology.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.execue.core.common.bean.entity.BusinessEntityByWord;
import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.EntityTripleDefinition;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.common.bean.entity.PathDefinition;
import com.execue.core.common.bean.entity.RIOntoTerm;
import com.execue.core.common.type.BusinessEntityType;
import com.execue.core.common.type.EntityTripleDefinitionType;
import com.execue.core.common.type.PathSelectionType;
import com.execue.ontology.exceptions.OntologyException;
import com.execue.ontology.exceptions.OntologySystemException;
import com.execue.swi.exception.KDXException;
import com.execue.swi.service.IKDXModelService;

/**
 * @author Abhijit
 */
public class DBFileOntologyServiceImpl extends AbstractFileOntologyServiceImpl {

   private IKDXModelService kdxModelService;

   /**
    * @return the kdxModelService
    */
   public IKDXModelService getKdxModelService () {
      return kdxModelService;
   }

   /**
    * @param kdxModelService
    *           the kdxModelService to set
    */
   public void setKdxModelService (IKDXModelService kdxModelService) {
      this.kdxModelService = kdxModelService;
   }

   public Map<String, BusinessEntityByWord> getDomainLookupEntities (List<String> keyWords) {
      try {
         return getKdxRetrievalService().getBusinessEntityWordMapByLookupWords(keyWords);
      } catch (KDXException kdxException) {
         throw new OntologySystemException(kdxException.getCode(), kdxException.getMessage(), kdxException);
      }
   }

   public Map<String, List<RIOntoTerm>> getDomainLookupOntoTerms (List<String> keyWords) throws OntologyException {
      try {
         return getKdxRetrievalService().getOntoTermsMapByLookupWords(keyWords, false);
      } catch (KDXException e) {
         throw new OntologyException(e.getCode(), e.getMessage(), e);
      }
   }

   public List<RIOntoTerm> getDomainLookupOntoTerm (String keyWords) throws OntologyException {
      List<String> wordList = new ArrayList<String>();
      wordList.add(keyWords);
      // Retruned Map has Key in Lower case
      Map<String, List<RIOntoTerm>> returnList = getDomainLookupOntoTerms(wordList);
      return returnList.get(keyWords.toLowerCase().trim());
   }

   public List<List<EntityTripleDefinition>> getCCPaths (Long sourceConceptDEDID, Long destinationConceptDEDID)
            throws OntologyException {
      List<List<EntityTripleDefinition>> ccPaths = new ArrayList<List<EntityTripleDefinition>>();
      List<List<EntityTripleDefinition>> pathList = getPaths(sourceConceptDEDID, destinationConceptDEDID);
      for (List<EntityTripleDefinition> ePath : pathList) {
         List<EntityTripleDefinition> etdPath = new ArrayList<EntityTripleDefinition>();
         for (EntityTripleDefinition etd : ePath) {
            if (EntityTripleDefinitionType.CONCEPT_RELATION_CONCEPT.equals(etd.getTripleType())) {
               etdPath.add(etd);
            }
         }
         if (etdPath.size() > 0) {
            ccPaths.add(etdPath);
         }
      }
      return ccPaths;
   }

   public List<EntityTripleDefinition> getCAPaths (Long sourceConceptDEDID, Long destinationConceptDEDID)
            throws OntologyException {
      List<EntityTripleDefinition> caPaths = new ArrayList<EntityTripleDefinition>();
      List<List<EntityTripleDefinition>> pathList = getPaths(sourceConceptDEDID, destinationConceptDEDID);
      for (List<EntityTripleDefinition> ePath : pathList) {
         for (EntityTripleDefinition etd : ePath) {
            if (EntityTripleDefinitionType.CONCEPT_RELATION_CONCEPT.equals(etd.getTripleType())) {
               caPaths.add(etd);
            }
         }
      }
      return caPaths;
   }

   public List<PathDefinition> absorbParentPropertiesInAllChildConcepts (List<PathDefinition> parentPaths)
            throws OntologyException {
      // TODO Auto-generated method stub
      return null;
   }

   public List<PathDefinition> absorbParentPropertiesInAllChildConcepts (List<PathDefinition> parentPaths,
            boolean excludeARA, Long modelId) throws OntologyException {
      // TODO Auto-generated method stub
      return null;
   }

   public List<PathDefinition> assignChildrenAsRangeInPlaceOfParent (List<PathDefinition> parentPaths, Long modelId)
            throws OntologyException {
      // TODO Auto-generated method stub
      return null;
   }

   public List<BusinessEntityDefinition> getCentralConceptsDEDs (Long modelId) throws OntologyException {
      // TODO Auto-generated method stub
      return null;
   }

   public List<PathDefinition> absorbParentPropertiesInAllChildConcepts (List<PathDefinition> parentPaths, Long modelId)
            throws OntologyException {
      // TODO Auto-generated method stub
      return null;
   }

   public List<RIOntoTerm> getDomainLookupOntoInstanceTermsForModel (String keyWord, Model model)
            throws OntologyException {
      List<String> words = new ArrayList<String>();
      words.add(keyWord);
      try {
         return getKdxRetrievalService().getTermsByLookupWordsAndEntityTypeInModel(words,
                  BusinessEntityType.CONCEPT_LOOKUP_INSTANCE, model);
      } catch (KDXException e) {
         throw new OntologyException(e.code, e.getMessage(), e);
      }
   }

   public void markCentralConceptPaths (Long modelId) throws OntologyException {
      // TODO Auto-generated method stub

   }

   public Map<String, List<PathDefinition>> getAllNonParentPathsForSourceAndDestination (Set<Long> sourceBEIds,
            Set<Long> destBeIds) {
      // TODO Auto-generated method stub
      return null;
   }

   public List<EntityTripleDefinition> getCAPathsFromPathDefMap (Long latestOntConceptID, Long latestOntConceptID2,
            Map<String, List<PathDefinition>> sourceDestPathDefMap) {
      // TODO Auto-generated method stub
      return null;
   }

   public List<List<EntityTripleDefinition>> getCCPathsFromPathDefMap (Long sourceBEId, Long destBEId,
            Map<String, List<PathDefinition>> sourceDestPathDefMap) {
      // TODO Auto-generated method stub
      return null;
   }

   public BusinessEntityDefinition getBusinessEntityDefinitionById (Long bedId) throws OntologyException {
      // TODO Auto-generated method stub
      return null;
   }

   public List<BusinessEntityDefinition> reachAndGetCentralConceptBEDs (Long destinationDEDID) throws OntologyException {
      // TODO Auto-generated method stub
      return null;
   }

   public Map<Long, PathDefinition> getNonParentPathsByDestIdAndPathSelectionType (Set<Long> beIds,
            PathSelectionType default_value_path, Long modelId) {
      // TODO Auto-generated method stub
      return null;
   }

}
