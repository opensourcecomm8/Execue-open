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


package com.execue.ontology.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.EntityTripleDefinition;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.common.bean.entity.PathDefinition;
import com.execue.core.common.bean.entity.RIOntoTerm;
import com.execue.core.common.type.PathSelectionType;
import com.execue.ontology.bean.OntoClass;
import com.execue.ontology.bean.OntoInstance;
import com.execue.ontology.bean.OntoProperty;
import com.execue.ontology.bean.OntoResource;
import com.execue.ontology.bean.Triple;
import com.execue.ontology.exceptions.OntologyException;

public interface IOntologyService {

   public Map<String, OntoClass> getConceptMap ();

   public Map<String, OntoInstance> getInstanceMap ();

   public Map<String, OntoProperty> getPropertyMap ();

   public List<String> getRelations (String subject, String object);

   public List<List<Triple>> getTriples (String subject, String object);

   public List<List<Triple>> getCCTriples (String subject, String object);

   public List<Triple> getCATriples (String subject, String object);

   public boolean isInstance (String instanceName);

   public OntoClass getConcept (String conceptName);

   public OntoProperty getProperty (String propName);

   public OntoResource getResource (String resourceName);

   public List<String> getChildConcepts (String conceptName, boolean all);

   public List<String> getAllParents (String resourceName);

   public List<String> getAllParentConceptsWithProperties (String conceptName);

   public List<String> getParentConcepts (String conceptName, boolean all);

   public Collection<String> getRelatedConcepts (String conceptName);

   public List<String> getChildProperties (String propertyName, boolean all);

   public List<String> getParentProperties (String propertyName, boolean all);

   public boolean isDisjoint (String concept1, String concept2);

   public boolean hasParent (String resourceName, String parentResourceName);

   public boolean hasImmediateParent (String resourceName, String parentName);

   public boolean isSameNameSpace (OntoResource or1, OntoResource or2);

   public boolean areInverseProperties (String propName1, String propName2);

   public OntoResource reachAndGetCentralConcept (String destConceptName, List<String> tempProps);

   public List<OntoProperty> getPropertiesWithDomain (String className);

   public List<OntoProperty> getRequiredPropertiesWithDomain (String className);

   public boolean isObjectProperty (String propName);

   public boolean isDatatypeProperty (String propName);

   public boolean isFunctionalProperty (String propName);

   public boolean isInverseFunctionalProperty (String propName);

   public boolean hasProperty (String className, String propName);

   public List<String> getDefaultValuePropertyValues (String propName, String valuePropName);

   public List<OntoProperty> getPreferredProperties (String conceptName);

   // New Interface

   public boolean isConcept (Long conceptID) throws OntologyException;

   public boolean isProperty (Long propID) throws OntologyException;

   public boolean isInstance (Long instanceID) throws OntologyException;

   public OntoClass getConcept (Long resourceID) throws OntologyException;

   public OntoProperty getProperty (Long resourceID) throws OntologyException;

   public boolean hasParent (Long resourceID, Long parentResourceID) throws OntologyException;

   public boolean hasParent (Long resourceID, String parentName) throws OntologyException;

   public List<OntoProperty> getPropertiesWithRange (Long classID) throws OntologyException;

   public List<OntoProperty> getPropertiesWithDomain (Long classID) throws OntologyException;

   public List<OntoProperty> getRequiredPropertiesWithDomain (Long classID) throws OntologyException;

   public List<BusinessEntityDefinition> getCentralConceptsDEDs (Long modelId) throws OntologyException;

   public List<OntoClass> getAllParentConcepts (Long conceptID) throws OntologyException;

   public List<OntoClass> getImmediateParentConcepts (Long conceptID) throws OntologyException;

   public List<OntoResource> reachAndGetCentralConcept (Long destinationDEDID) throws OntologyException;

   public List<OntoProperty> getPreferredProperties (Long conceptID) throws OntologyException;

   public List<List<EntityTripleDefinition>> getPaths (Long sourceConceptID, Long destinationConceptID)
            throws OntologyException;

   public List<List<EntityTripleDefinition>> getCCPaths (Long sourceConceptID, Long destinationConceptID)
            throws OntologyException;

   public List<EntityTripleDefinition> getCAPaths (Long sourceConceptID, Long destinationConceptID)
            throws OntologyException;

   public List<PathDefinition> absorbParentPropertiesInAllChildConcepts (List<PathDefinition> parentPaths, Long modelId)
            throws OntologyException;

   public List<PathDefinition> absorbParentPropertiesInAllChildConcepts (List<PathDefinition> parentPaths,
            boolean excludeARA, Long modelId) throws OntologyException;

   public List<PathDefinition> assignChildrenAsRangeInPlaceOfParent (List<PathDefinition> parentPaths, Long modelId)
            throws OntologyException;

   public List<RIOntoTerm> getDomainLookupOntoInstanceTermsForModel (String keyWord, Model model)
            throws OntologyException;

   public void markCentralConceptPaths (Long modelId) throws OntologyException;

   /**
    * Method to get All the Paths between the BEIds passed. DB Query will contain the Same list as source and
    * destinations to get The path Definitions. IN map the Key will be sourceBEId + "-" + destBEID. and value will be
    * list of pathDefinitions between source and destinations.
    * 
    * @param sourceBedIds
    * @param destBedIds
    * @return
    * @throws OntologyException
    */
   public Map<String, List<PathDefinition>> getAllNonParentPathsForSourceAndDestination (Set<Long> sourceBedIds,
            Set<Long> destBedIds) throws OntologyException;

   /**
    * Method to get the Entity Triple Definition betWeen a source and destination BE id from populated Map of the path
    * definitions.
    * 
    * @param sourceBEID
    * @param destBEID
    * @param sourceDestPathDefMap
    * @return
    */
   public List<EntityTripleDefinition> getCAPathsFromPathDefMap (Long sourceBEID, Long destBEID,
            Map<String, List<PathDefinition>> sourceDestPathDefMap);

   /**
    * Method to get the Entity Triple Definition betWeen a source and destination BE id from populated Map of the path
    * definitions.
    * 
    * @param sourceBEId
    * @param destBEId
    * @param sourceDestPathDefMap
    * @return
    */
   public List<List<EntityTripleDefinition>> getCCPathsFromPathDefMap (Long sourceBEId, Long destBEId,
            Map<String, List<PathDefinition>> sourceDestPathDefMap);

   /**
    * @param destinationDEDID
    * @return
    * @throws OntologyException
    */
   public List<BusinessEntityDefinition> reachAndGetCentralConceptBEDs (Long destinationDEDID) throws OntologyException;

   public Map<Long, PathDefinition> getNonParentPathsByDestIdAndPathSelectionType (Set<Long> beIds,
            PathSelectionType default_value_path, Long modelId) throws OntologyException;
}
