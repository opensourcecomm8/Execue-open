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

public class OntoResource {

   protected String name;
   protected String nameSpace;
   protected String displayName;
   protected String id;
   protected Long   domainEntityId;

   public OntoResource () {
   }

   public OntoResource (String name) {
      this.name = name;
   }

   public OntoResource (String nameSpace, String name) {
      this.name = name;
      this.nameSpace = nameSpace;
   }

   public String getName () {
      return name;
   }

   public void setName (String name) {
      this.name = name;
   }

   public Long getDomainEntityId () {
      return domainEntityId;
   }

   public void setDomainEntityId (Long domainEntityId) {
      this.domainEntityId = domainEntityId;
   }

   public String getNameSpace () {
      return nameSpace;
   }

   public void setNameSpace (String nameSpace) {
      this.nameSpace = nameSpace;
   }

   public String getDisplayName () {
      return displayName;
   }

   public void setDisplayName (String displayName) {
      this.displayName = displayName;
   }

   public String getId () {
      return id;
   }

   public void setId (String id) {
      this.id = id;
   }

   // Utility Methods

   public OntoClass asClass () {
      if (this instanceof OntoClass)
         return (OntoClass) this;
      else
         return null;
   }

   public OntoProperty asProperty () {
      if (this instanceof OntoProperty)
         return (OntoProperty) this;
      else
         return null;
   }

   public boolean isClass () {
      return this instanceof OntoClass;
   }

   public boolean isProperty () {
      return this instanceof OntoProperty;
   }
}
