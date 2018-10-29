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


package com.execue.ontology.bean;

import java.util.List;
import java.util.Map;

public class Ontology {

   protected String                                       name;
   protected String                                       namespace;
   protected String                                       uri;

   protected Map<String, Long>                            conceptNameBEIDMap;
   protected Map<String, Long>                            relationNameBEIDMap;
   protected Map<String, Long>                            instanceNameBEIDMap;

   // TODO -AP- get rid of all these Maps once complete Onto 2 DB is done
   
   protected Map<Long, OntoClass>                         concepts;
   protected Map<Long, OntoProperty>                      properties;
   protected Map<String, OntoClass>                       conceptsByName;
   protected Map<String, OntoProperty>                    propertiesByName;
   protected Map<String, OntoInstance>                    instances;
   protected Map<String, Map<String, List<List<Triple>>>> triples;

   public String getName () {
      return name;
   }

   public void setName (String name) {
      this.name = name;
   }

   public String getUri () {
      return uri;
   }

   public void setUri (String uri) {
      this.uri = uri;
   }

   public String getNamespace () {
      return namespace;
   }

   public void setNamespace (String namespace) {
      this.namespace = namespace;
   }

   public Map<String, Long> getConceptNameBEIDMap () {
      return conceptNameBEIDMap;
   }

   public void setConceptNameBEIDMap (Map<String, Long> conceptNameBEIDMap) {
      this.conceptNameBEIDMap = conceptNameBEIDMap;
   }

   public Map<String, Long> getRelationNameBEIDMap () {
      return relationNameBEIDMap;
   }

   public void setRelationNameBEIDMap (Map<String, Long> relationNameBEIDMap) {
      this.relationNameBEIDMap = relationNameBEIDMap;
   }

   public Map<String, Long> getInstanceNameBEIDMap () {
      return instanceNameBEIDMap;
   }

   public void setInstanceNameBEIDMap (Map<String, Long> instanceNameBEIDMap) {
      this.instanceNameBEIDMap = instanceNameBEIDMap;
   }

   public Map<Long, OntoClass> getConcepts () {
      return concepts;
   }

   public void setConcepts (Map<Long, OntoClass> concepts) {
      this.concepts = concepts;
   }

   public Map<Long, OntoProperty> getProperties () {
      return properties;
   }

   public void setProperties (Map<Long, OntoProperty> properties) {
      this.properties = properties;
   }

   public Map<String, OntoInstance> getInstances () {
      return instances;
   }

   public Map<String, OntoClass> getConceptsByName () {
      return conceptsByName;
   }

   public void setConceptsByName (Map<String, OntoClass> conceptsByName) {
      this.conceptsByName = conceptsByName;
   }

   public Map<String, OntoProperty> getPropertiesByName () {
      return propertiesByName;
   }

   public void setPropertiesByName (Map<String, OntoProperty> propertiesByName) {
      this.propertiesByName = propertiesByName;
   }

   public void setInstances (Map<String, OntoInstance> instances) {
      this.instances = instances;
   }

   public Map<String, Map<String, List<List<Triple>>>> getTriples () {
      return triples;
   }

   public void setTriples (Map<String, Map<String, List<List<Triple>>>> triples) {
      this.triples = triples;
   }
   
   // Utility Methods
   
   public Long getConceptBEID (String conceptName) {
      if(this.conceptNameBEIDMap != null) return this.conceptNameBEIDMap.get(conceptName.toLowerCase());
      return null;
   }

   public Long getRelationBEID (String relationName) {
      if(this.relationNameBEIDMap != null) return this.relationNameBEIDMap.get(relationName.toLowerCase());
      return null;
   }
}
