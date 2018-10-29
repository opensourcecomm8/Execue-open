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
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.EntityTripleDefinition;
import com.execue.core.common.bean.entity.RIOntoTerm;
import com.execue.core.common.type.BusinessEntityType;
import com.execue.ontology.bean.OntoClass;
import com.execue.ontology.bean.OntoProperty;
import com.execue.ontology.bean.OntoResource;
import com.execue.ontology.bean.Triple;
import com.execue.ontology.bean.UnionOntoClass;
import com.execue.ontology.configuration.OntologyConstants;
import com.execue.ontology.exceptions.OntologyException;
import com.execue.ontology.service.IOntologyService;
import com.execue.ontology.service.OntologyFactory;
import com.execue.ontology.service.ontoModel.IOntologyModelServices;
import com.execue.swi.exception.KDXException;

public abstract class AbstractFileOntologyServiceImpl extends AbstractDBOntologyServiceImpl {

   Logger logger = Logger.getLogger(AbstractDBOntologyServiceImpl.class);

   @Override
   public List<String> getParentConcepts (String conceptName, boolean all) {
      return OntologyFactory.getInstance().getOntologyModelServices().getParentConcepts(getConcept(conceptName), all);
   }

   @Override
   public List<String> getAllParentConceptsWithProperties (String conceptName) {
      return OntologyFactory.getInstance().getOntologyModelServices().getParentConceptsWithProperties(
               getConcept(conceptName), true);
   }

   @Override
   public List<String> getChildConcepts (String conceptName, boolean all) {
      if (getConcept(conceptName) == null) {
         return new ArrayList<String>(1);
      }
      return OntologyFactory.getInstance().getOntologyModelServices().getChildConcepts(getConcept(conceptName), all);
   }

   @Override
   public Collection<String> getRelatedConcepts (String conceptName) {
      Set<String> relatedConcepts = new HashSet<String>(1);
      getRelatedConceptsRecursively(conceptName, relatedConcepts);

      List<OntoProperty> properties = getPropertiesWithRange(conceptName);
      for (OntoProperty prop : properties) {
         OntoResource domain = prop.getDomain();
         String domainName = domain.getName();
         if (domainName != null && isConcept(domain.getDomainEntityId()) && !domainName.equals(conceptName)
                  && !relatedConcepts.contains(domainName)) {
            getRelatedConceptsRecursively(domainName, relatedConcepts);
         }
      }
      return relatedConcepts;
   }

   public void getRelatedConceptsRecursively (String conceptName, Set<String> relatedConcepts) {
      relatedConcepts.add(conceptName);
      // Get All parent concepts
      relatedConcepts.addAll(getParentConcepts(conceptName, true));
      // Get All concepts that are range of properties of these concepts
      List<OntoProperty> properties = getPropertiesWithDomain(conceptName);
      for (OntoProperty prop : properties) {
         OntoResource range = prop.getRange();
         String rangeName = range.getName();
         if (rangeName != null && isConcept(range.getDomainEntityId()) && !rangeName.equals(conceptName)
                  && !relatedConcepts.contains(rangeName)) {
            getRelatedConceptsRecursively(rangeName, relatedConcepts);
         }
      }
      List<String> subClasses = getChildConcepts(conceptName, true);
      if (subClasses != null) {
         relatedConcepts.addAll(subClasses);
         for (String subClassName : subClasses) {
            properties = getPropertiesWithDomain(subClassName);
            for (OntoProperty prop : properties) {
               OntoResource range = prop.getRange();
               String rangeName = range.getName();
               if (rangeName != null && isConcept(range.getDomainEntityId()) && !rangeName.equals(subClassName)
                        && !relatedConcepts.contains(rangeName)) {
                  getRelatedConceptsRecursively(rangeName, relatedConcepts);
               }
            }
         }
      }
   }

   @Override
   public List<String> getParentProperties (String propName, boolean all) {
      return OntologyFactory.getInstance().getOntologyModelServices().getParentProperties(getProperty(propName), all);
   }

   @Override
   public boolean isObjectProperty (String propName) {
      return OntologyFactory.getInstance().getOntologyModelServices().isObjectProperty(getProperty(propName));
   }

   @Override
   public boolean isDatatypeProperty (String propName) {
      return OntologyFactory.getInstance().getOntologyModelServices().isDatatypeProperty(getProperty(propName));
   }

   @Override
   public boolean isFunctionalProperty (String propName) {
      return OntologyFactory.getInstance().getOntologyModelServices().isFunctionalProperty(getProperty(propName));
   }

   @Override
   public boolean isInverseFunctionalProperty (String propName) {
      return OntologyFactory.getInstance().getOntologyModelServices()
               .isInverseFunctionalProperty(getProperty(propName));
   }

   @Override
   public List<String> getDefaultValuePropertyValues (String propName, String valuePropName) {
      return OntologyFactory.getInstance().getOntologyModelServices().getDefaultValuePropertyValues(
               getProperty(propName), getProperty(valuePropName));
   }

   @Override
   public List<List<Triple>> getTriples (String subject, String object) {
      IOntologyService ontoService = OntologyFactory.getInstance().getOntologyService();
      Map<String, Map<String, List<List<Triple>>>> triplesMap = getOntologyConfigurationService().getOntology()
               .getTriples();

      List<List<Triple>> returnTriplesList = new ArrayList<List<Triple>>(1);
      // Find Direct relationship first
      List<List<Triple>> triplesList = getTriplesFromList(triplesMap, subject, object);
      // If no direct relationship exists then check for direct relationship between
      // Parents of subject and object
      if (triplesList.isEmpty()) {
         List<String> parentsSubject = ontoService.getAllParentConceptsWithProperties(subject);
         for (String parentSubject : parentsSubject) {
            triplesList.addAll(getTriplesFromList(triplesMap, parentSubject, object));
         }
         // If still no relationship exists then check for direct relationship between
         // subject and Parents of object
         if (triplesList.isEmpty()) {
            List<String> parentsObjects = ontoService.getAllParentConceptsWithProperties(object);
            for (String parentObject : parentsObjects) {
               triplesList.addAll(getTriplesFromList(triplesMap, subject, parentObject));
            }
            // If still no relationship exists then check for direct relationship between
            // parent subject and Parents of object
            if (triplesList.isEmpty()) {
               for (String parentSubject : parentsSubject) {
                  for (String parentObject : parentsObjects) {
                     triplesList.addAll(getTriplesFromList(triplesMap, parentSubject, parentObject));
                  }
               }
            }
         }
      }
      if (!triplesList.isEmpty()) {
         for (List<Triple> tempTripleList : triplesList) {
            try {
               List<Triple> returnTriples = new ArrayList<Triple>(1);
               for (Triple tempTriple : tempTripleList) {
                  Triple newTriple = (Triple) tempTriple.clone();
                  returnTriples.add(newTriple);
               }
               returnTriplesList.add(returnTriples);
            } catch (CloneNotSupportedException e) {
               e.printStackTrace();
            }
         }
      }
      return returnTriplesList;
   }

   private List<List<Triple>> getTriplesFromList (Map<String, Map<String, List<List<Triple>>>> triples, String subject,
            String object) {
      List<List<Triple>> triplesList = new ArrayList<List<Triple>>(1);
      if (triples.containsKey(subject)) {
         Map<String, List<List<Triple>>> triplesForSubject = triples.get(subject);
         if (triplesForSubject.containsKey(object)) {
            triplesList.addAll(triplesForSubject.get(object));
         }
      }
      return triplesList;
   }

   public List<List<EntityTripleDefinition>> getPaths (Long sourceConceptID, Long destinationConceptID)
            throws OntologyException {
      // TODO:
      return null;
   }

   @Override
   public boolean areInverseProperties (String propName1, String propName2) {
      return OntologyFactory.getInstance().getOntologyModelServices().areInverseProperties(getProperty(propName1),
               getProperty(propName2));
   }

   public List<OntoProperty> getPropertiesWithRange (String className) {
      List<OntoProperty> props = new ArrayList<OntoProperty>();
      List<String> propNames = OntologyFactory.getInstance().getOntologyModelServices().getPropertiesWithRange(
               className);
      for (String propName : propNames) {
         props.add(getProperty(propName));
      }
      return props;
   }

   @Override
   public List<OntoProperty> getPropertiesWithDomain (String className) {
      List<OntoProperty> props = new ArrayList<OntoProperty>();
      List<String> propNames = OntologyFactory.getInstance().getOntologyModelServices().getPropertiesWithDomain(
               className);
      for (String propName : propNames) {
         props.add(getProperty(propName));
      }
      return props;
   }

   @Override
   public List<OntoProperty> getRequiredPropertiesWithDomain (String className) {
      List<OntoProperty> props = new ArrayList<OntoProperty>();
      List<String> propNames = OntologyFactory.getInstance().getOntologyModelServices()
               .getRequiredPropertiesWithDomain(className);
      for (String propName : propNames) {
         props.add(getProperty(propName));
      }
      return props;
   }

   @Override
   public OntoResource reachAndGetCentralConcept (String destConceptName, List<String> tempProps) {
      IOntologyModelServices ontologyModelService = OntologyFactory.getInstance().getOntologyModelServices();
      IOntologyService ontologyService = OntologyFactory.getInstance().getOntologyService();

      OntoClass destConcept = ontologyService.getResource(destConceptName).asClass();
      List<String> propList = ontologyModelService.getPropertiesWithRange(destConceptName);
      for (String opName : propList) {
         OntoProperty op = getProperty(opName);
         boolean processProp = true;
         for (String curPropName : tempProps) {
            OntoProperty curProp = getProperty(curPropName);
            if (areInverseProperties(op.getName(), curProp.getName())) {
               processProp = false;
               break;
            }
         }
         if (!processProp) {
            continue;
         }
         OntoClass domainClass = op.getDomain().asClass();
         if (domainClass.isUnionClass()) {
            UnionOntoClass uc = domainClass.asUnionClass();
            List<OntoClass> components = uc.getComponentClasses();
            for (OntoClass partClass : components) {
               if (partClass.getName().equalsIgnoreCase(destConceptName)) {
                  return null;
               }
               if (isSameNameSpace(partClass, destConcept)) {
                  if (ontologyService.hasParent(partClass.getName(), OntologyConstants.SUPER_CONCEPT)) {
                     return ontologyService.getResource(partClass.getName());
                  } else {
                     tempProps.add(op.getName());
                     return reachAndGetCentralConcept(partClass.getName(), tempProps);
                  }
               }
            }
         } else {
            if (domainClass.getName().equalsIgnoreCase(destConceptName)) {
               return null;
            }
            if (isSameNameSpace(domainClass, destConcept)) {
               if (ontologyService.hasParent(domainClass.getName(), OntologyConstants.SUPER_CONCEPT)) {
                  return ontologyService.getResource(domainClass.getName());
               } else {
                  tempProps.add(op.getName());
                  return reachAndGetCentralConcept(domainClass.getName(), tempProps);
               }
            }
         }
      }
      return null;
   }

   public OntoClass getConcept (Long resourceID) {
      return getOntologyConfigurationService().getOntology().getConcepts().get(resourceID);
   }

   public OntoProperty getProperty (Long resourceID) {
      return getOntologyConfigurationService().getOntology().getProperties().get(resourceID);
   }

   public boolean isConcept (Long conceptID) {
      return getOntologyConfigurationService().getOntology().getConcepts().containsKey(conceptID);
   }

   public boolean isProperty (Long propID) {
      return getOntologyConfigurationService().getOntology().getConcepts().containsKey(propID);
   }

   public List<OntoProperty> getRequiredPropertiesWithDomain (Long classID) {
      return getRequiredPropertiesWithDomain(getConcept(classID).getName());
   }

   @Override
   public OntoClass getConcept (String conceptName) {
      return getOntologyConfigurationService().getOntology().getConceptsByName().get(conceptName.toLowerCase());
   }

   @Override
   public OntoProperty getProperty (String propName) {
      return getOntologyConfigurationService().getOntology().getPropertiesByName().get(propName.toLowerCase());
   }

   public List<OntoProperty> getPropertiesWithRange (Long classID) {
      if (classID == null) {
         logger.error("getting classId NUll for statistics return emp[ty List");
         return new ArrayList<OntoProperty>();
      }
      return getPropertiesWithRange(getConcept(classID).getName());
   }

   public List<OntoProperty> getPropertiesWithDomain (Long classID) {
      return getPropertiesWithDomain(getConcept(classID).getName());
   }

   public List<OntoClass> getCentralConcepts () {
      List<OntoClass> centralConcepts = new ArrayList<OntoClass>();
      Map<Long, OntoClass> concepts = getOntologyConfigurationService().getOntology().getConcepts();
      for (OntoClass entry : concepts.values()) {
         if (hasImmediateParent(entry.getName(), OntologyConstants.SUPER_CONCEPT)) {
            centralConcepts.add(entry);
         }
      }
      return centralConcepts;
   }

   public boolean isAttribute (Long conceptID) throws OntologyException {
      if (conceptID == null) {
         // TODO ConcepId is passed as Null in case of statistics from CentralConcept Recognition Processor. Returning
         // False as of Now
         logger
                  .error("ConcepId is passed as Null in case of statistics from CentralConcept Recognition Processor. Returning False as of Now");
         return false;
      }
      OntoClass oc = getOntologyConfigurationService().getOntology().getConcepts().get(conceptID);
      return hasParent(oc.getName(), OntologyConstants.ATTRIBUTE);
   }

   public boolean isAbstract (Long conceptID) throws OntologyException {
      OntoClass oc = getOntologyConfigurationService().getOntology().getConcepts().get(conceptID);
      return hasParent(oc.getName(), OntologyConstants.ABSTRACT_CONCEPT);
   }

   public List<RIOntoTerm> getDomainRegexTerms () throws OntologyException {
      return null;
   }

   public List<BusinessEntityDefinition> getDomainRegexInstances () throws OntologyException {
      return getOntologyConfigurationService().getRegexInstances();
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.ontology.service.IOntologyService#reachAndGetCentralConcept(java.lang.Long)
    */
   public List<OntoProperty> getPreferredProperties (Long conceptID) throws OntologyException {
      List<OntoProperty> preferredProp = new ArrayList<OntoProperty>(1);
      List<OntoProperty> props = getPropertiesWithDomain(conceptID);
      for (OntoProperty prop : props) {
         if (hasParent(prop.getName(), OntologyConstants.PREFERRED_PROPERTY)) {
            preferredProp.add(prop);
         }
      }
      return preferredProp;
   }

   public List<OntoClass> getAllParentConcepts (Long conceptID) throws OntologyException {
      List<OntoClass> parentConcepts = new ArrayList<OntoClass>();
      OntoClass concept = getOntologyConfigurationService().getOntology().getConcepts().get(conceptID);
      List<String> parentConceptNames = getParentConcepts(concept.getName(), true);
      for (String parentConceptName : parentConceptNames) {
         OntoClass parentConceptClass = new OntoClass();
         parentConceptClass.setName(parentConceptName);
         parentConcepts.add(parentConceptClass);
      }
      return parentConcepts;
   }

   public List<OntoClass> getImmediateParentConcepts (Long conceptID) throws OntologyException {
      List<OntoClass> immParentConcepts = new ArrayList<OntoClass>();
      OntoClass concept = getOntologyConfigurationService().getOntology().getConcepts().get(conceptID);
      List<String> immParentConceptNames = getParentConcepts(concept.getName(), false);
      for (String immParentConceptName : immParentConceptNames) {
         OntoClass parentConceptClass = new OntoClass();
         parentConceptClass.setName(immParentConceptName);
         immParentConcepts.add(parentConceptClass);
      }
      return immParentConcepts;
   }

   // TODO: -JVK- The instances are not getting loaded into ontology during the startup. Check in DB whether an instance
   // exists with the id to determine whether its an instance or not
   public boolean isInstance (Long instanceID) {
      boolean flag = false;
      try {
         BusinessEntityDefinition instanceDED = kdxRetrievalService.getBusinessEntityDefinitionById(instanceID);
         if (instanceDED != null && BusinessEntityType.CONCEPT_LOOKUP_INSTANCE.equals(instanceDED.getEntityType())) {
            flag = true;
         }
      } catch (KDXException e) {
         // do nothing
      }
      return flag;
   }

   public boolean hasParent (Long resourceID, Long parentResourceID) {
      return hasParent(resourceID, getOntologyConfigurationService().getOntology().getConcepts().get(parentResourceID)
               .getName());
   }

   public boolean hasParent (Long resourceID, String parentName) {
      if (resourceID == null) {
         logger.error("Called with Null resource Id Returning false from hasParent for parent Name " + parentName);
         return false;
      }
      OntoResource ontoResource = getOntologyConfigurationService().getOntology().getConcepts().get(resourceID);
      List<String> parents = getParentConcepts(ontoResource.getName(), true);
      if (parents.contains(parentName)) {
         return true;
      }
      for (String parent : parents) {
         if (parent.equalsIgnoreCase(parentName)) {
            return true;
         }
      }
      return false;
   }

   public List<OntoResource> reachAndGetCentralConcept (Long destinationDEDID) throws OntologyException {
      // Get the list of central concepts
      List<OntoResource> centralConcepts = new ArrayList<OntoResource>();
      List<OntoProperty> propList = getPropertiesWithRange(destinationDEDID);
      for (OntoProperty prop : propList) {
         OntoClass source = prop.getDomain().asClass();
         if (hasParent(source.getName(), OntologyConstants.SUPER_CONCEPT)) {
            centralConcepts.add(source);
         }
      }

      // int pathLength = -1;
      // OntoClass centralConcept = null;
      // for (OntoClass source : centralConcepts) {
      // Map<String, List<List<Triple>>> map = getOntologyConfigurationService().getOntology().getTriples().get(source);
      // List<List<Triple>> triplesList = map.get(destConcept);
      // // For each central concept, get the shortest path till destination
      // if (pathLength == -1 || pathLength > triplesList.size()) {
      // pathLength = triplesList.size();
      // centralConcept = source;
      // }
      // }
      // pathLength);

      // TODO: -JVK- from the central concepts list, get the concept with the shortest path
      // For now just returning the first one in the list
      if (centralConcepts.size() > 0) {
         return centralConcepts;
      }
      return null;
   }

}
