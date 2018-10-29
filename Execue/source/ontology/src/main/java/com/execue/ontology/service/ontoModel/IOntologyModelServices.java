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


package com.execue.ontology.service.ontoModel;

import java.util.List;
import java.util.Map;

import com.execue.ontology.bean.OntoClass;
import com.execue.ontology.bean.OntoProperty;
import com.execue.ontology.bean.Triple;
import com.hp.hpl.jena.ontology.OntClass;

/**
 * User: abhijit Date: Aug 22, 2008 Time: 5:43:38 PM
 */

public interface IOntologyModelServices {

   public void loadOntology (String source);

   public Map<String, OntoClass> getConcepts ();

   public Map<String, OntoProperty> getProperties ();

   public OntClass getConcept (String conceptName);

   public Map<String, Map<String, List<Triple>>> getDirectTriples ();

   public Map<String, Map<String, List<Triple>>> getTriples ();

   public List<String> getChildConcepts (OntoClass oc, boolean all);

   public List<String> getChildProperties (OntoProperty op, boolean all);

   public List<String> getParentConcepts (OntoClass oc, boolean all);

   public List<String> getParentConceptsWithProperties (OntoClass oc, boolean all);

   public List<String> getParentProperties (OntoProperty op, boolean all);

   public List<String> getPropertiesWithRange (String className);

   public List<String> getPropertiesWithDomain (String className);

   public List<String> getRequiredPropertiesWithDomain (String className);

   public Map<List<String>, String> getDomainForProperty (OntoProperty op);

   public String getRangeForProperty (OntoProperty op);

   public boolean hasSuperProperty (OntoProperty op, String superPropName);

   public boolean hasSubProperty (OntoProperty op, String superPropName);

   public boolean isObjectProperty (OntoProperty op);

   public boolean isDatatypeProperty (OntoProperty op);

   public boolean isFunctionalProperty (OntoProperty op);

   public boolean isInverseFunctionalProperty (OntoProperty op);

   public boolean areInverseProperties (OntoProperty op1, OntoProperty op2);

   public List<String> getDefaultValuePropertyValues (OntoProperty op, OntoProperty valueProp);
}
