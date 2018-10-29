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


package com.execue.ontology.configuration.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.configuration.IConfiguration;
import com.execue.core.util.ExecueStringUtil;
import com.execue.ontology.bean.OntoClass;
import com.execue.ontology.bean.OntoProperty;
import com.execue.ontology.bean.Ontology;
import com.execue.ontology.bean.Triple;
import com.execue.ontology.configuration.IOntologyConfiguration;
import com.execue.ontology.service.OntologyFactory;
import com.execue.ontology.service.ontoModel.IOntologyModelServices;
import com.execue.swi.exception.KDXException;
import com.execue.swi.service.IKDXRetrievalService;

public class OntologyConfigurationImpl implements IOntologyConfiguration {

   // ********* Constants for configuration properties*************************************************************

   private static final String            ONTOLOGY_FILE_URL_KEY                            = "ontology.files.ontologyFileURL";
   private static final String            ONTOLOGY_NAME_KEY                                = "ontology.properties.ontologyName";
   private static final String            ONTOLOGY_URI_KEY                                 = "ontology.properties.ontologyURI";
   private static final String            ONTOLOGY_NAMESPACE_KEY                           = "ontology.properties.ontologyNameSpace";
   private static final String            ONTOLOGY_SERVICE_IMPLEMENTATION_CLASS_NAME       = "ontology.serviceImplementations.ontologyServiceClassName";
   private static final String            ONTOLOGY_MODEL_SERVICE_IMPLEMENTATION_CLASS_NAME = "ontology.serviceImplementations.ontologyModelServiceClassName";
   private static final String            ONTOLOGY_SKIP_CONCEPTS_KEY                       = "ontology.skipConcepts.conceptName";
   private static final String            ONTOLOGY_SKIP_PROPERTIES_KEY                     = "ontology.skipProperties.propertyName";

   private static Logger                  logger                                           = Logger
                                                                                                    .getLogger(OntologyConfigurationImpl.class);
   private Ontology                       ontology;
   private List<BusinessEntityDefinition> regexInstances;
   private IConfiguration                 ontologyConfiguration;
   private IKDXRetrievalService           kdxRetrievalService;

   public Ontology getOntology () {
      return ontology;
   }

   @Override
   public List<BusinessEntityDefinition> getRegexInstances () {
      return regexInstances;
   }

   public void populateRegexInstances () {
      try {
         regexInstances = getKdxRetrievalService().getRegularExpressionBasedInstances();
      } catch (KDXException e) {
         e.printStackTrace();
      }
   }

   /**
    * This method parses the Ontology and converts it into a Graph structure
    * 
    * @param ontologyFileURL
    *           The URL of the Ontology
    * @return ExeCue representation of the ontology
    */
   protected Ontology loadOntology (String ontologyFileURL, Long modelId) {

      Ontology ontology = new Ontology();

      ontology.setName(ONTOLOGY_NAME_KEY);
      ontology.setUri(ONTOLOGY_URI_KEY);
      ontology.setNamespace(ONTOLOGY_NAMESPACE_KEY);

      IOntologyModelServices ontModelServices = OntologyFactory.getInstance().getOntologyModelServices();

      ontModelServices.loadOntology(ontologyFileURL);

      // Set Ontology Components
      Map<String, OntoClass> concepts = ontModelServices.getConcepts();
      Map<String, OntoProperty> props = ontModelServices.getProperties();

      // Set the Domain Entity IDs
      try {
         Map<String, Long> conceptNameBEIDMap = new HashMap<String, Long>();
         Map<String, Long> relationNameBEIDMap = new HashMap<String, Long>();

         Map<Long, OntoClass> conceptsMap = new HashMap<Long, OntoClass>();
         Map<Long, OntoProperty> propsMap = new HashMap<Long, OntoProperty>();

         List<Concept> conceptsList = kdxRetrievalService.getConcepts(modelId);

         BusinessEntityDefinition bed = null;
         // TODO: -AP- needs to be taken from configuration
         List<String> skipConceptList = ExecueStringUtil
                  .getAsList(
                           "ExeCueConcept,SuperConcept,Attribute,Abstract,EnumerationConcept,QuantitativeConcept,Profile,DomainConcept,ExecueConcept,NLPConcept,ExcludedConcept,PeriodicInformationProfile,DomainAdjectives",
                           ",");
         List<String> skipPropertyList = ExecueStringUtil.getAsList(
                  "defaultValue,defaultProperty,idProperty,preferredProperty,requiredProperty", ",");

         Long count = 1L;
         for (OntoClass element : concepts.values()) {
            if (skipConceptList.contains(element.getName())) {
               conceptsMap.put(count, element);
               conceptNameBEIDMap.put(element.getName().toLowerCase(), count);
               count++;
            } else {
               bed = kdxRetrievalService.getConceptBEDByName(modelId, element.getName());
               if (bed != null) {
                  element.setDomainEntityId(bed.getId());
                  conceptsMap.put(bed.getId(), element);
                  conceptNameBEIDMap.put(element.getName().toLowerCase(), bed.getId());
               } else {
                  logger.error("DED not found for concept with name [" + element.getName() + "]");
               }
            }
         }
         ontology.setConcepts(conceptsMap);
         ontology.setConceptsByName(concepts);
         for (OntoProperty element : props.values()) {
            if (skipPropertyList.contains(element.getName()))
               continue;
            bed = kdxRetrievalService.getRelationBEDByName(modelId, element.getName());
            if (bed != null) {
               element.setDomainEntityId(bed.getId());
               propsMap.put(bed.getId(), element);
               relationNameBEIDMap.put(element.getName().toLowerCase(), bed.getId());
            } else {
               logger.error("DED not found for relation with name [" + element.getName() + "]");
            }
         }
         ontology.setProperties(propsMap);
         ontology.setPropertiesByName(props);

         ontology.setConceptNameBEIDMap(conceptNameBEIDMap);
         ontology.setRelationNameBEIDMap(relationNameBEIDMap);
      } catch (KDXException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      Map<String, Map<String, List<Triple>>> triples = ontModelServices.getDirectTriples();

      boolean procced = true;

      Map<String, Map<String, List<List<Triple>>>> triplesPathMap = new HashMap<String, Map<String, List<List<Triple>>>>(
               1);

      for (Map.Entry<String, Map<String, List<Triple>>> entry1 : triples.entrySet()) {
         // Get the Starting Concept
         String sourceConceptName = entry1.getKey();
         // Get the current level connections for this starting concept
         Map<String, List<Triple>> tripMap = entry1.getValue();
         Map<String, List<List<Triple>>> pathMap = new HashMap<String, List<List<Triple>>>();
         // Go over each connected concept
         for (Map.Entry<String, List<Triple>> entry2 : tripMap.entrySet()) {
            // Get the concept name
            String midConceptName = entry2.getKey();
            if (midConceptName.equals("string"))
               continue;
            List<Triple> startingTriples = entry2.getValue();
            // Create a list of lists in which each internal list gives a path between the associated concepts
            List<List<Triple>> paths = new ArrayList<List<Triple>>();
            for (Triple trp : startingTriples) {
               List<Triple> tempList = new ArrayList<Triple>(1);
               tempList.add(trp);
               paths.add(tempList);
            }
            pathMap.put(midConceptName, paths);
         }
         if (!pathMap.isEmpty())
            triplesPathMap.put(sourceConceptName, pathMap);
      }

      while (procced) {
         Map<String, Map<String, List<List<Triple>>>> indirectTriples = getIndirectTriples(triplesPathMap);
         if (indirectTriples.isEmpty())
            procced = false;
         else {
            for (Map.Entry<String, Map<String, List<List<Triple>>>> entry : indirectTriples.entrySet()) {
               String sourceConceptName = entry.getKey();
               Map<String, List<List<Triple>>> tripMap = entry.getValue();
               triplesPathMap.get(sourceConceptName).putAll(tripMap);
            }
         }
      }
      ontology.setTriples(triplesPathMap);

      return ontology;
   }

   public Map<String, Map<String, List<List<Triple>>>> getIndirectTriples (
            Map<String, Map<String, List<List<Triple>>>> directTriples) {
      Map<String, Map<String, List<List<Triple>>>> triplesPathMap = new HashMap<String, Map<String, List<List<Triple>>>>(
               1);

      for (Map.Entry<String, Map<String, List<List<Triple>>>> entry1 : directTriples.entrySet()) {
         // Get the Starting Concept
         String sourceConceptName = entry1.getKey();
         // Get the current level connections for this starting concept
         Map<String, List<List<Triple>>> tripMap = entry1.getValue();
         // Go over each connected concept
         for (Map.Entry<String, List<List<Triple>>> entry2 : tripMap.entrySet()) {
            // Get the concept name
            String midConceptName = entry2.getKey();
            if (midConceptName.equals("string"))
               continue;
            // Get the currently existing triples between starting concept and this intermediate concept
            List<List<Triple>> startingTriples = entry2.getValue();
            // Check if this concept has any entry as starting concept
            if (directTriples.containsKey(midConceptName)) {
               // If yes then get the connections for this intermediate concept
               Map<String, List<List<Triple>>> tempIndirectTrips = directTriples.get(midConceptName);
               // Go over connections of intermediate concept
               for (Map.Entry<String, List<List<Triple>>> entry3 : tempIndirectTrips.entrySet()) {
                  // Create new map of connections
                  Map<String, List<List<Triple>>> newTripMap = new HashMap<String, List<List<Triple>>>();
                  // Get the destination concept
                  String destConceptName = entry3.getKey();
                  if (destConceptName.equals("string"))
                     continue;
                  // If entry for destination concept is already there then skip
                  if (tripMap.containsKey(destConceptName))
                     continue;
                  // Get the triples from intermediate concept to destination concept
                  List<List<Triple>> tempTriples = entry3.getValue();
                  // Create new list of triples from existing ones
                  List<List<Triple>> newTriples = new ArrayList<List<Triple>>();
                  for (List<Triple> curTrpls : startingTriples) {
                     List<Triple> tempTrpls = new ArrayList<Triple>(curTrpls);
                     newTriples.add(tempTrpls);
                  }
                  // Add to the list of new triples
                  for (List<Triple> curTrpls : newTriples) {
                     for (List<Triple> tempTrpls : tempTriples) {
                        curTrpls.addAll(tempTrpls);
                     }
                  }
                  newTripMap.put(destConceptName, newTriples);
                  if (triplesPathMap.containsKey(sourceConceptName)) {
                     triplesPathMap.get(sourceConceptName).putAll(newTripMap);
                  } else {
                     triplesPathMap.put(sourceConceptName, newTripMap);
                  }
               }
            }
         }
      }

      return triplesPathMap;
   }

   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }

   public IConfiguration getOntologyConfiguration () {
      return ontologyConfiguration;
   }

   public void setOntologyConfiguration (IConfiguration ontologyConfiguration) {
      this.ontologyConfiguration = ontologyConfiguration;
   }

   @Override
   public String getOntologyFileURL () {
      return getOntologyConfiguration().getProperty(ONTOLOGY_FILE_URL_KEY);
   }

   @Override
   public String getOntologyModelServiceClassName () {
      return getOntologyConfiguration().getProperty(ONTOLOGY_MODEL_SERVICE_IMPLEMENTATION_CLASS_NAME);
   }

   @Override
   public String getOntologyName () {
      return getOntologyConfiguration().getProperty(ONTOLOGY_NAME_KEY);
   }

   @Override
   public String getOntologyNameSpace () {
      return getOntologyConfiguration().getProperty(ONTOLOGY_NAMESPACE_KEY);
   }

   @Override
   public String getOntologyServiceClassName () {
      return getOntologyConfiguration().getProperty(ONTOLOGY_SERVICE_IMPLEMENTATION_CLASS_NAME);
   }

   @Override
   public List<String> getOntologySkipConcepts () {
      return getOntologyConfiguration().getList(ONTOLOGY_SKIP_CONCEPTS_KEY);
   }

   @Override
   public List<String> getOntologySkipProperties () {
      return getOntologyConfiguration().getList(ONTOLOGY_SKIP_PROPERTIES_KEY);

   }

   @Override
   public String getOntologyURI () {
      return getOntologyConfiguration().getProperty(ONTOLOGY_URI_KEY);
   }
}
