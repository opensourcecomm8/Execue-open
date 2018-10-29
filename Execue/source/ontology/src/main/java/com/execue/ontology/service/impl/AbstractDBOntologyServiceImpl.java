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
import java.util.List;
import java.util.Map;

import com.execue.ontology.bean.OntoClass;
import com.execue.ontology.bean.OntoInstance;
import com.execue.ontology.bean.OntoProperty;
import com.execue.ontology.bean.OntoResource;
import com.execue.ontology.bean.Triple;
import com.execue.ontology.configuration.IOntologyConfiguration;
import com.execue.ontology.configuration.OntologyConstants;
import com.execue.ontology.service.IOntologyService;
import com.execue.swi.service.IKDXRetrievalService;

public abstract class AbstractDBOntologyServiceImpl implements IOntologyService {

   protected IOntologyConfiguration ontologyConfigurationService;
   protected IKDXRetrievalService   kdxRetrievalService;

   public Map<String, OntoClass> getConceptMap () {
      return null;
   }

   public Map<String, OntoInstance> getInstanceMap () {
      return null;
   }

   public Map<String, OntoProperty> getPropertyMap () {
      return null;
   }

   public List<String> getRelations (String subject, String object) {
      return null;
   }

   public List<List<Triple>> getTriples (String subject, String object) {
      return new ArrayList<List<Triple>>();
   }

   public List<List<Triple>> getCCTriples (String subject, String object) {
      List<List<Triple>> ccTriples = new ArrayList<List<Triple>>(1);
      List<List<Triple>> allTriples = getTriples(subject, object);
      if (!allTriples.isEmpty()) {
         for (List<Triple> triples : allTriples) {
            List<Triple> thisCCTriples = new ArrayList<Triple>(1);
            for (Triple triple : triples) {
               if (!triple.getRange().equalsIgnoreCase(OntologyConstants.INDICATOR_CONCEPT)
                        && !hasParent(triple.getRange(), OntologyConstants.INDICATOR_CONCEPT)) {
                  thisCCTriples.add(triple);
               }
            }
            if (!thisCCTriples.isEmpty() && thisCCTriples.size() == triples.size()) {
               ccTriples.add(thisCCTriples);
            }
         }
      }
      return ccTriples;
   }

   public List<Triple> getCATriples (String subject, String object) {
      List<Triple> caTriples = new ArrayList<Triple>(1);
      List<List<Triple>> allTriples = getTriples(subject, object);
      if (!allTriples.isEmpty()) {
         for (List<Triple> triples : allTriples) {
            for (Triple triple : triples) {
               if (hasParent(triple.getRange(), OntologyConstants.ATTRIBUTE)) {
                  caTriples.add(triple);
               }
            }
         }
      }
      return caTriples;
   }

   public boolean areInverseProperties (String propName1, String propName2) {
      return false;
   }

   public boolean isInstance (String instanceName) {
      // TODO Auto-generated method stub
      return false;
   }

   public OntoClass getConcept (String conceptName) {
      // TODO -AP- Remove when DBOntology Implementation is complete
      return null;
   }

   public OntoProperty getProperty (String propName) {
      // TODO -AP- Remove when DBOntology Implementation is complete
      return null;
   }

   public boolean hasParentConcept (String conceptName, String parentName) {
      List<String> parents = getParentConcepts(conceptName, true);
      if (parents.contains(parentName))
         return true;
      else {
         for (String parent : parents) {
            if (parent.equalsIgnoreCase(parentName))
               return true;
         }
      }
      return false;
   }

   public boolean hasImmediateParentConcept (String conceptName, String parentName) {
      List<String> parents = getParentConcepts(conceptName, false);
      if (parents.contains(parentName))
         return true;
      else {
         for (String parent : parents) {
            if (parent.equalsIgnoreCase(parentName))
               return true;
         }
      }
      return false;
   }

   public boolean hasParentProperty (String propName, String parentName) {
      List<String> parents = getParentProperties(propName, true);
      if (parents.contains(parentName))
         return true;
      else {
         for (String parent : parents) {
            if (parent.equalsIgnoreCase(parentName))
               return true;
         }
      }
      return false;
   }

   public boolean hasImmediateParentProperty (String propName, String parentName) {
      List<String> parents = getParentProperties(propName, false);
      if (parents.contains(parentName))
         return true;
      else {
         for (String parent : parents) {
            if (parent.equalsIgnoreCase(parentName))
               return true;
         }
      }
      return false;
   }

   public OntoResource getResource (String resourceName) {
      OntoResource or = null;
      or = getConcept(resourceName);
      if (or == null)
         or = getProperty(resourceName);
      return or;
   }

   public List<String> getChildConcepts (String conceptName, boolean all) {
      return null;
   }

   // public List<String> getAllChildConcepts (String conceptName) {
   // return getChildConcepts(conceptName, true);
   // }

   // public List<String> getImmediatChildConcepts (String conceptName) {
   // return getChildConcepts(conceptName, false);
   // }

   public List<String> getAllParents (String resourceName) {
      OntoResource or = getResource(resourceName);
      if (or instanceof OntoClass) {
         return getParentConcepts(resourceName, true);
      } else if (or instanceof OntoProperty) {
         return getParentProperties(resourceName, true);
      }
      return new ArrayList<String>(1);
   }

   public List<String> getAllParentConceptsWithProperties (String conceptName) {
      return null;
   }

   public List<String> getParentConcepts (String conceptName, boolean all) {
      return null;
   }

   // public List<String> getAllParentConcepts (String conceptName) {
   // return getParentConcepts(conceptName, true);
   // }
   //
   // public List<String> getImmediateParentConcepts (String conceptName) {
   // return getParentConcepts(conceptName, false);
   // }

   public Collection<String> getRelatedConcepts (String conceptName) {
      return null;
   }

   public List<String> getChildProperties (String propertyName, boolean all) {
      return null;
   }

   // public List<String> getAllChildProperties (String propertyName) {
   // return getChildProperties(propertyName, true);
   // }

   // public List<String> getImmediatChildProperties (String propertyName) {
   // return getChildProperties(propertyName, false);
   // }

   public List<String> getParentProperties (String propertyName, boolean all) {
      return null;
   }

   // public List<String> getAllParentProperties (String propertyName) {
   // return getParentProperties(propertyName, true);
   // }

   // public List<String> getImmediateParentProperties (String propertyName) {
   // return getParentProperties(propertyName, false);
   // }

   // public Map getAllInstances (String conceptName) {
   // return null;
   // }

   public boolean isDisjoint (String concept1, String concept2) {
      return false;
   }

   public boolean hasParent (String resourceName, String parentName) {
      OntoResource or = getResource(resourceName);
      OntoResource parentOR = getResource(parentName);
      if (or != null && parentOR != null) {
         if (or.getClass() == parentOR.getClass()) {
            if (or instanceof OntoClass) {
               return hasParentConcept(resourceName, parentName);
            } else if (or instanceof OntoProperty) {
               return hasParentProperty(resourceName, parentName);
            }
         }
      }
      return false;
   }

   public boolean hasImmediateParent (String resourceName, String parentName) {
      OntoResource or = getResource(resourceName);
      OntoResource parentOR = getResource(parentName);
      if (or != null && parentOR != null) {
         if (or.getClass() == parentOR.getClass()) {
            if (or instanceof OntoClass) {
               return hasImmediateParentConcept(resourceName, parentName);
            } else if (or instanceof OntoProperty) {
               return hasImmediateParentProperty(resourceName, parentName);
            }
         }
      }
      return false;
   }

   // public boolean hasChild (String conceptName, String childConceptName) {
   // return false;
   // }

   public boolean isSameNameSpace (OntoResource or1, OntoResource or2) {
      if (or1.getNameSpace() == null || or2.getNameSpace() == null) {
         return false;
      }
      String nameSpace1 = or1.getNameSpace().substring(0, or1.getNameSpace().lastIndexOf("/"));
      String nameSpace2 = or2.getNameSpace().substring(0, or2.getNameSpace().lastIndexOf("/"));

      return nameSpace1.equalsIgnoreCase(nameSpace2);
   }

   public boolean isObjectProperty (String propName) {
      return false;
   }

   public boolean isDatatypeProperty (String propName) {
      return false;
   }

   public boolean isFunctionalProperty (String propName) {
      return false;
   }

   public boolean isInverseFunctionalProperty (String propName) {
      return false;
   }

   public boolean hasProperty (String className, String propName) {
      List<OntoProperty> propNames = getPropertiesWithDomain(className);
      for (OntoProperty prop : propNames) {
         if (propName.equalsIgnoreCase(prop.getName()))
            return true;
      }
      return false;
   }

   public List<String> getDefaultValuePropertyValues (String propName, String valuePropName) {
      return null;
   }

   public List<OntoProperty> getPreferredProperties (String conceptName) {
      List<OntoProperty> preferredProp = new ArrayList<OntoProperty>(1);
      List<OntoProperty> props = getPropertiesWithDomain(conceptName);
      for (OntoProperty prop : props) {
         if (hasParent(prop.getName(), OntologyConstants.PREFERRED_PROPERTY))
            preferredProp.add(prop);
      }
      return preferredProp;
   }

   public List<OntoProperty> getPropertiesWithDomain (String className) {
      // TODO -AP- will go away once DBOntologyService is done
      return null;
   }

   public List<OntoProperty> getRequiredPropertiesWithDomain (String className) {
      // TODO -AP- will go away once DBOntologyService is done
      return null;
   }

   public OntoResource reachAndGetCentralConcept (String destConceptName, List<String> tempProps) {
      // TODO -AP- will go away once DBOntologyService is done
      return null;
   }

   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }

   public IOntologyConfiguration getOntologyConfigurationService () {
      return ontologyConfigurationService;
   }

   public void setOntologyConfigurationService (IOntologyConfiguration ontologyConfigurationService) {
      this.ontologyConfigurationService = ontologyConfigurationService;
   }
}
