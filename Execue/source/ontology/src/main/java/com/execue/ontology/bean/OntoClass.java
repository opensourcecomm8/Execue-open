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

public class OntoClass extends OntoResource {

   public OntoClass () {
   }

   public OntoClass (String name) {
      super(name);
   }

   public OntoClass (String nameSpace, String name) {
      super(nameSpace, name);
   }

   // Utility Methods

   public boolean isUnionClass () {
      return this instanceof UnionOntoClass;
   }

   public boolean isIntersectionClass () {
      return this instanceof IntersectionOntoClass;
   }

   public UnionOntoClass asUnionClass () {
      return (UnionOntoClass) this;
   }

   public IntersectionOntoClass asIntersectionClass () {
      return (IntersectionOntoClass) this;
   }

   // Overridden Methods

   @Override
   public boolean equals(Object obj) {
      return (obj instanceof OntoClass) && ((OntoClass) obj).getDomainEntityId().equals(this.getDomainEntityId());
   }
}
