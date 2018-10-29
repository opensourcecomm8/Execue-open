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

import java.util.Set;

import com.execue.core.common.bean.IBusinessEntity;

public class Relation implements IBusinessEntity, java.io.Serializable {

   private Long                          id;
   private String                        name;
   private String                        description;
   private String                        displayName;
   private Set<BusinessEntityDefinition> businessEntityDefinitions;

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public String getName () {
      return name;
   }

   public void setName (String name) {
      this.name = name;
   }

   public String getDescription () {
      return description;
   }

   public void setDescription (String description) {
      this.description = description;
   }

   public String getDisplayName () {
      return displayName;
   }

   public void setDisplayName (String displayName) {
      this.displayName = displayName;
   }

   public Set<BusinessEntityDefinition> getBusinessEntityDefinitions () {
      return businessEntityDefinitions;
   }

   public void setBusinessEntityDefinitions (Set<BusinessEntityDefinition> businessEntityDefinitions) {
      this.businessEntityDefinitions = businessEntityDefinitions;
   }

   /*
    * private Set<EntityTripleDefinition> entityTripleDefinitions; private Set<RelationTripleDefinition>
    * relationTripleDefinitionsForSourceRelationId; private Set<RelationTripleDefinition>
    * relationTripleDefinitionsForDestinationRelationId; private Set<RelationTripleDefinition>
    * relationTripleDefinitionsForRelationId;
    */

}
