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


package com.execue.core.common.bean.entity;

import java.util.ArrayList;
import java.util.List;

public class BusinessEntityByWord {

   private String                       word;

   private List<BusinessEntityDefinition> concepts = new ArrayList<BusinessEntityDefinition>();
   private List<BusinessEntityDefinition> instances = new ArrayList<BusinessEntityDefinition>();
   private List<BusinessEntityDefinition> relations = new ArrayList<BusinessEntityDefinition>();
   private List<BusinessEntityDefinition> profiles = new ArrayList<BusinessEntityDefinition>();

   public String getWord () {
      return word;
   }

   public void setWord (String word) {
      this.word = word;
   }

   public List<BusinessEntityDefinition> getConcepts () {
      return concepts;
   }

   public void setConcepts (List<BusinessEntityDefinition> concepts) {
      this.concepts = concepts;
   }

   public List<BusinessEntityDefinition> getInstances () {
      return instances;
   }

   public void setInstances (List<BusinessEntityDefinition> instances) {
      this.instances = instances;
   }

   public List<BusinessEntityDefinition> getRelations () {
      return relations;
   }

   public void setRelations (List<BusinessEntityDefinition> relations) {
      this.relations = relations;
   }

   public List<BusinessEntityDefinition> getProfiles () {
      return profiles;
   }

   public void setProfiles (List<BusinessEntityDefinition> profiles) {
      this.profiles = profiles;
   }

}
