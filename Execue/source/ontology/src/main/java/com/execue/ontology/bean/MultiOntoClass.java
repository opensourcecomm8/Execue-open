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

import java.util.ArrayList;
import java.util.List;

import com.execue.ontology.service.OntologyFactory;

/**
 * @author Abhijit
 * @since Mar 18, 2009 : 7:31:49 PM
 */
public class MultiOntoClass extends OntoClass {

   protected List<OntoClass> componentClasses;

   // Constructors

   public MultiOntoClass () {
   }

   public MultiOntoClass (String name) {
      super(name);
   }

   public MultiOntoClass (String nameSpace, String name) {
      super(nameSpace, name);
   }

   // Getter and Setters

   public List<OntoClass> getComponentClasses () {
      return componentClasses;
   }

   public void setComponentClasses (List<OntoClass> componentClasses) {
      this.componentClasses = componentClasses;
   }

   // Overridden Methods

   // Utility Methods

   public void addComponentClass (String ontClassName) {
      if (this.componentClasses == null)
         this.componentClasses = new ArrayList<OntoClass>();
      this.componentClasses.add(OntologyFactory.getInstance().getOntologyService().getConcept(ontClassName));
   }

   public void addComponentClass (OntoClass ontClass) {
      if (this.componentClasses == null)
         this.componentClasses = new ArrayList<OntoClass>();
      this.componentClasses.add(ontClass);
   }
}
