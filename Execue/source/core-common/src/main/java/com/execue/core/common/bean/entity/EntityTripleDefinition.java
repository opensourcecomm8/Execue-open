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

import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.EntityTripleDefinitionType;
import com.execue.core.common.type.EntityTriplePropertyType;
import com.execue.core.common.type.OriginType;

/**
 * When the triple type is RELATION_RELATION_RELATION, all the Subject, Predicate and Object should be DEDs for Relation
 * only<br/>. When the triple type is CONCEPT_RELATION_CONCEPT, Predicate should only be DED for Relation only.
 * 
 * @author execue
 */
public class EntityTripleDefinition implements java.io.Serializable {

   private Long                       id;

   private BusinessEntityDefinition   sourceBusinessEntityDefinition;
   private BusinessEntityDefinition   relation;
   private BusinessEntityDefinition   destinationBusinessEntityDefinition;

   // Below attributes (cardinality, functional, inverseFunctional, relationSpecified)
   // are meaningful for only triple type of CONCEPT_RELATION_ATTRIBUTE

   private int                        cardinality;
   private boolean                    functional;
   private boolean                    inverseFunctional;
   private boolean                    relationSpecified;

   private EntityTripleDefinitionType tripleType;
   private EntityTriplePropertyType   propertyType;

   private String                     defaultValue;

   private Set<Path>                  paths;

   private Long                       baseETDId;

   private OriginType                 origin;
   private CheckType                  instanceTripleExists = CheckType.NO;

   /**
    * @return the origin
    */
   public OriginType getOrigin () {
      return origin;
   }

   /**
    * @param origin
    *           the origin to set
    */
   public void setOrigin (OriginType origin) {
      this.origin = origin;
   }

   public Long getBaseETDId () {
      return baseETDId;
   }

   public void setBaseETDId (Long baseETDId) {
      this.baseETDId = baseETDId;
   }

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public BusinessEntityDefinition getRelation () {
      return this.relation;
   }

   public void setRelation (BusinessEntityDefinition relation) {
      this.relation = relation;
   }

   public int getCardinality () {
      return cardinality;
   }

   public void setCardinality (int cardinality) {
      this.cardinality = cardinality;
   }

   public boolean isFunctional () {
      return functional;
   }

   public void setFunctional (boolean functional) {
      this.functional = functional;
   }

   public boolean isInverseFunctional () {
      return inverseFunctional;
   }

   public void setInverseFunctional (boolean inverseFunctional) {
      this.inverseFunctional = inverseFunctional;
   }

   public boolean isRelationSpecified () {
      return relationSpecified;
   }

   public void setRelationSpecified (boolean relationSpecified) {
      this.relationSpecified = relationSpecified;
   }

   public Set<Path> getPaths () {
      return paths;
   }

   public void setPaths (Set<Path> paths) {
      this.paths = paths;
   }

   public EntityTripleDefinitionType getTripleType () {
      return tripleType;
   }

   public void setTripleType (EntityTripleDefinitionType tripleType) {
      this.tripleType = tripleType;
   }

   public EntityTriplePropertyType getPropertyType () {
      return propertyType;
   }

   public void setPropertyType (EntityTriplePropertyType propertyType) {
      this.propertyType = propertyType;
   }

   public String getDefaultValue () {
      return defaultValue;
   }

   public void setDefaultValue (String defaultValue) {
      this.defaultValue = defaultValue;
   }

   public BusinessEntityDefinition getSourceBusinessEntityDefinition () {
      return sourceBusinessEntityDefinition;
   }

   public void setSourceBusinessEntityDefinition (BusinessEntityDefinition sourceBusinessEntityDefinition) {
      this.sourceBusinessEntityDefinition = sourceBusinessEntityDefinition;
   }

   public BusinessEntityDefinition getDestinationBusinessEntityDefinition () {
      return destinationBusinessEntityDefinition;
   }

   public void setDestinationBusinessEntityDefinition (BusinessEntityDefinition destinationBusinessEntityDefinition) {
      this.destinationBusinessEntityDefinition = destinationBusinessEntityDefinition;
   }

   /**
    * Method to check if the two triples are equivalent.
    * 
    * @param triple
    * @return
    */
   public boolean equivalent (EntityTripleDefinition triple) {
      if (this.getBaseETDId() != null && triple.getBaseETDId() != null) {
         return this.getBaseETDId().equals(triple.getBaseETDId());
      }
      return this.getId().equals(triple.getId());

   }

   /**
    * @return the instanceTripleExists
    */
   public CheckType getInstanceTripleExists () {
      return instanceTripleExists;
   }

   /**
    * @param instanceTripleExists
    *           the instanceTripleExists to set
    */
   public void setInstanceTripleExists (CheckType instanceTripleExists) {
      this.instanceTripleExists = instanceTripleExists;
   }
}
