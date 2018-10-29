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


package com.execue.ontology.service.ontoModel.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.execue.ontology.bean.OntoClass;
import com.execue.ontology.bean.OntoInstance;
import com.execue.ontology.bean.OntoLookupInstance;
import com.execue.ontology.bean.OntoProperty;
import com.execue.ontology.bean.Triple;
import com.execue.ontology.configuration.IOntologyConfiguration;
import com.execue.ontology.configuration.OntologyConstants;
import com.execue.ontology.service.ontoModel.IOntologyModelServices;
import com.hp.hpl.jena.ontology.IntersectionClass;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntDocumentManager;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.ontology.ProfileRegistry;
import com.hp.hpl.jena.ontology.UnionClass;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.util.iterator.Filter;
import com.hp.hpl.jena.util.iterator.FilterIterator;

/**
 * <p/> This class uses the JENA API to parse the Ontologies. The ontologies can be in RDF,DAML+OIL or OWL format.<p/>
 * <p/> User: Abhijit Date: Jul 22, 2008 Time: 2:51:40 PM
 */

public class JenaServices implements IOntologyModelServices {

   private static final Logger    log = Logger.getLogger(JenaServices.class);

   private OntModel               model;
   private String                 ontNamespace;
   private String                 ontName;
   private IOntologyConfiguration ontologyConfigurationService;

   /**
    * Loads Ontology into Memory
    * 
    * @param source
    *           URI of the ontology
    */
   public void loadOntology (String source) {
      // Create the Ontology model to read Owl Ontologies
      model = ModelFactory.createOntologyModel(ProfileRegistry.OWL_LANG);

      if (source.endsWith(".daml")) {
         // If its a DAML Ontology then create the Ontology Model to read DAML Ontologies
         model = ModelFactory.createOntologyModel(ProfileRegistry.DAML_LANG);
      } else if (source.endsWith(".rdf")) {
         // If its an RDF Ontology then create the Ontology Model to read RDF Ontologies
         model = ModelFactory.createOntologyModel(ProfileRegistry.RDFS_LANG);
      }

      OntDocumentManager dm = model.getDocumentManager();

      dm.setProcessImports(true);

      // Read the Ontology to create an in-memory DOM structure
      model.read(source);

      ontNamespace = getOntologyConfigurationService().getOntologyNameSpace();
      ontName = getOntologyConfigurationService().getOntologyName();
   }

   public List<String> getChildConcepts (OntoClass oClass, boolean all) {
      List<String> childClasses = new ArrayList<String>(1);
      OntClass oc = model.getOntClass(oClass.getNameSpace() + oClass.getName());
      ExtendedIterator iter = oc.listSubClasses(!all);
      while (iter.hasNext()) {
         OntResource child = (OntResource) iter.next();
         childClasses.add(child.getLocalName());
      }
      return childClasses;
   }

   public List<String> getChildProperties (OntoProperty prop, boolean all) {
      List<String> childProperties = new ArrayList<String>(1);
      OntProperty op = model.getOntProperty(prop.getNameSpace() + prop.getName());
      ExtendedIterator iter = op.listSubProperties(!all);
      while (iter.hasNext()) {
         OntResource child = (OntResource) iter.next();
         childProperties.add(child.getLocalName());
      }
      return childProperties;
   }

   public List<String> getParentConcepts (OntoClass oClass, boolean all) {
      List<String> parentClasses = new ArrayList<String>(1);
      OntClass oc = model.getOntClass(oClass.getNameSpace() + oClass.getName());
      ExtendedIterator iter = listConcepts(oc.listSuperClasses(!all));
      while (iter.hasNext()) {
         OntResource parent = (OntResource) iter.next();
         parentClasses.add(parent.getLocalName());
      }
      return parentClasses;
   }

   public List<String> getParentConceptsWithProperties (OntoClass oClass, boolean all) {
      List<String> parentClasses = new ArrayList<String>(1);
      OntClass oc = model.getOntClass(oClass.getNameSpace() + oClass.getName());
      ExtendedIterator iter = listConcepts(oc.listSuperClasses(!all));
      while (iter.hasNext()) {
         OntResource parent = (OntResource) iter.next();
         if (parent.isClass()) {
            ExtendedIterator it = listProperties(parent.asClass().listDeclaredProperties(true));
            if (it.hasNext()) {
               parentClasses.add(parent.getLocalName());
            } else {
               List<String> props = getPropertiesWithRange(parent.getLocalName());
               if (!props.isEmpty()) {
                  parentClasses.add(parent.getLocalName());
               }
            }
         }
      }
      return parentClasses;
   }

   public List<String> getParentProperties (OntoProperty prop, boolean all) {
      List<String> parentProperties = new ArrayList<String>(1);
      OntProperty op = model.getOntProperty(prop.getNameSpace() + prop.getName());
      ExtendedIterator iter = op.listSuperProperties(!all);
      while (iter.hasNext()) {
         OntResource parent = (OntResource) iter.next();
         parentProperties.add(parent.getLocalName());
      }
      return parentProperties;
   }

   public Map<String, OntoClass> getConcepts () {
      Map<String, OntoClass> conceptMap = new HashMap<String, OntoClass>(1);
      // Get an Iterator over the filtered classes
      ExtendedIterator et = listConcepts(model.listNamedClasses());

      // Iterate over all the classes, create OntoClass for every class
      while (et.hasNext()) {
         OntClass c = (OntClass) et.next();
         addConceptToOntology(conceptMap, c.getNameSpace(), c.getLocalName());
      }
      return conceptMap;
   }

   public Map<String, OntoProperty> getProperties () {
      Map<String, OntoProperty> propertiesMap = new HashMap<String, OntoProperty>(1);

      // Get Each Indidual property
      ExtendedIterator propIterator = model.listOntProperties();
      while (propIterator.hasNext()) {
         // Get each individual property
         OntProperty prop = (OntProperty) propIterator.next();
         String propNameSpace = prop.getNameSpace().substring(0, prop.getNameSpace().lastIndexOf("/"));
         if (propNameSpace.equalsIgnoreCase(ontNamespace)) {
            if (prop.getSuperProperty() == null
                     || !prop.getSuperProperty().getLocalName().equalsIgnoreCase("abstractProperty")) {
               if (prop.getDomain() != null && prop.getRange() != null) {
                  if (prop.getLocalName().trim().length() == 0) {
                     log.debug("No Name Property = " + prop.getNameSpace() + " - " + prop.getLocalName());
                  } else {
                     addPropertyToOntology(propertiesMap, prop.getNameSpace(), prop.getLocalName());
                  }
               }
            }
         }
      }
      return propertiesMap;
   }

   public Map<String, Map<String, List<Triple>>> getTriples () {
      return getDirectTriples();
   }

   public Map<String, Map<String, List<Triple>>> getDirectTriples () {
      Map<String, Map<String, List<Triple>>> triplesMap = new HashMap<String, Map<String, List<Triple>>>(1);

      // Get Each Indidual property
      ExtendedIterator propIterator = model.listOntProperties();
      while (propIterator.hasNext()) {
         // Get each individual property
         OntProperty prop = (OntProperty) propIterator.next();
         String propNameSpace = prop.getNameSpace().substring(0, prop.getNameSpace().lastIndexOf("/"));
         if (propNameSpace.equalsIgnoreCase(ontNamespace)) {
            if (prop.getSuperProperty() == null
                     || !prop.getSuperProperty().getLocalName().equalsIgnoreCase("abstractProperty")) {
               if (prop.getDomain() != null && prop.getRange() != null) {
                  OntResource domain = prop.getDomain();
                  OntResource range = prop.getRange();
                  List<OntClass> rangeClasses = new ArrayList<OntClass>(1);
                  if (range.isClass()) {
                     if (range.asClass().isUnionClass() || range.asClass().isIntersectionClass()) {
                        rangeClasses = getComponentClasses(range);
                     } else if (!range.isAnon()) {
                        if (range.getLocalName().equalsIgnoreCase("date")) {
                           rangeClasses.add(model.getOntClass(ontNamespace + "/" + "ExeCue.owl#Time"));
                        } else if (range.getLocalName().equalsIgnoreCase("integer")
                                 || range.getLocalName().equalsIgnoreCase("float")) {
                           rangeClasses.add(model.getOntClass(ontNamespace + "/" + "ExeCue.owl#Number"));
                        } else {
                           rangeClasses.add(range.asClass());
                        }
                     }
                  }
                  if (domain.isClass()) {
                     if (domain.asClass().isUnionClass() || domain.asClass().isIntersectionClass()) {
                        List<OntClass> componentClasses = getComponentClasses(domain);
                        for (OntClass partClass : componentClasses) {
                           for (OntClass rangeClass : rangeClasses) {
                              addTripleToOntology(triplesMap, partClass.getLocalName(), rangeClass.getLocalName(), prop
                                       .getLocalName());
                           }
                        }
                     } else {
                        for (OntClass rangeClass : rangeClasses) {
                           addTripleToOntology(triplesMap, domain.getLocalName(), rangeClass.getLocalName(), prop
                                    .getLocalName());
                        }
                     }
                  }
               }
            }
         }
      }
      return triplesMap;
   }

   private List<OntClass> getComponentClasses (OntResource range) {
      List<OntClass> componentClasses = new ArrayList<OntClass>(1);
      if (range.asClass().isUnionClass()) {
         UnionClass uc = range.asClass().asUnionClass();
         ExtendedIterator it = uc.listOperands();
         while (it.hasNext()) {
            OntClass partClass = (OntClass) it.next();
            componentClasses.add(partClass);
         }
      }
      if (range.asClass().isIntersectionClass()) {
         IntersectionClass uc = range.asClass().asIntersectionClass();
         ExtendedIterator it = uc.listOperands();
         while (it.hasNext()) {
            OntClass partClass = (OntClass) it.next();
            componentClasses.add(partClass);
         }
      }
      return componentClasses;
   }

   /**
    * This method filters the concepts from the ontology based on the specified custom filter
    * 
    * @param ontClassIt
    *           iterator passed
    * @return Filtered Iterator
    */

   public static ExtendedIterator listConcepts (Iterator ontClassIt) {
      return new FilterIterator(new ConceptFilter(), ontClassIt);
   }

   public static ExtendedIterator listProperties (Iterator propIter) {
      return new FilterIterator(new PropertyFilter(), propIter);
   }

   /**
    * This class extends the Filter class from JENA to provide a custom filter. This filter filters out the anonymous
    * classes and restriction classes
    */
   private static class ConceptFilter implements Filter {

      public boolean accept (Object o) {
         if (o instanceof OntClass) {
            OntClass c = (OntClass) o;
            return !c.isAnon() && !c.isRestriction() && !c.toString().endsWith("#Resource")
                     && !c.toString().endsWith("#Thing");
         }
         return false;
      }
   }

   private static class PropertyFilter implements Filter {

      public boolean accept (Object o) {
         if (o instanceof OntProperty) {
            String name = ((OntProperty) o).getLocalName();
            OntResource range = ((OntProperty) o).getRange();
            return !name.equalsIgnoreCase("defaultProperty")
                     && !name.equalsIgnoreCase("requiredProperty")
                     && !name.equalsIgnoreCase("idProperty")
                     && !name.equalsIgnoreCase("preferredProperty")
                     && !name.equalsIgnoreCase("defaultValue")
                     && name.trim().length() > 0
                     && (range != null && range.getLocalName() != null && !range.getLocalName().equalsIgnoreCase(
                              "Thing"));
         } else if (o instanceof String) {
            String name = o.toString();
            return !name.equalsIgnoreCase("defaultProperty") && !name.equalsIgnoreCase("requiredProperty")
                     && !name.equalsIgnoreCase("idProperty") && !name.equalsIgnoreCase("preferredProperty")
                     && !name.equalsIgnoreCase("defaultValue");
         }
         return false;
      }
   }

   protected void addConceptToOntology (Map<String, OntoClass> conceptMap, String nameSpace, String name) {
      OntoClass eoc = new OntoClass(nameSpace, name);
      boolean addConcept = true;
      if (conceptMap.containsKey(name.toLowerCase())) {
         OntoClass tempOntoClass = conceptMap.get(name.toLowerCase());
         if (tempOntoClass.getNameSpace().indexOf(this.ontName) > -1) {
            addConcept = false;
         }
      }
      if (addConcept)
         conceptMap.put(name.toLowerCase(), eoc);
   }

   protected void addLookupInstanceToOntology (Map<String, OntoInstance> instanceMap, String nameSpace, String name) {
      OntoLookupInstance li = new OntoLookupInstance(nameSpace, name);
      instanceMap.put(name.toLowerCase(), li);
   }

   protected void addPropertyToOntology (Map<String, OntoProperty> propertiesMap, String nameSpace, String name) {
      OntoProperty eop = new OntoProperty(nameSpace, name);
      propertiesMap.put(name.toLowerCase(), eop);
   }

   protected void addTripleToOntology (Map<String, Map<String, List<Triple>>> triplesMap, String domain, String range,
            String relation) {
      boolean found = false;

      if (domain.equalsIgnoreCase("resource") || range.equalsIgnoreCase("resource")) {
         log.debug("");
      }
      Triple triple = new Triple();
      triple.setDomain(domain);
      triple.setRange(range);
      triple.setProperty(relation);

      List<Triple> tempList = new ArrayList<Triple>(1);
      tempList.add(triple);

      if (triplesMap.containsKey(domain)) {
         Map<String, List<Triple>> triplesByRange = triplesMap.get(domain);

         if (triplesByRange.containsKey(range)) {
            List<Triple> triples = triplesByRange.get(range);

            for (Triple currentTriple : triples) {
               if (currentTriple.getProperty().equalsIgnoreCase(relation)) {
                  found = true;
                  break;
               }
            }
            if (!found) {
               triples.addAll(tempList);
            }
         } else {
            triplesByRange.put(range, tempList);
         }
      } else {
         Map<String, List<Triple>> tempMap = new HashMap<String, List<Triple>>(1);
         tempMap.put(range, tempList);

         triplesMap.put(domain, tempMap);
      }
   }

   /**
    * Returns true if namespaces of both the resources is same. Here namespace does not include ontology name.
    * 
    * @param or1
    *           First Ontological resource
    * @param or2
    *           Second ONtological resource
    * @return true or false
    */
   public boolean isSameNameSpace (OntResource or1, OntResource or2) {
      if (or1.getNameSpace() == null || or2.getNameSpace() == null) {
         return false;
      }
      String nameSpace1 = or1.getNameSpace().substring(0, or1.getNameSpace().lastIndexOf("/"));
      String nameSpace2 = or2.getNameSpace().substring(0, or2.getNameSpace().lastIndexOf("/"));

      return nameSpace1.equalsIgnoreCase(nameSpace2);
   }

   public List<String> getPropertiesWithRange (String className) {
      List<String> propVector = new ArrayList<String>();

      ExtendedIterator propIterator = listProperties(model.listOntProperties());

      while (propIterator.hasNext()) {
         // Get each individual property
         OntProperty prop = (OntProperty) propIterator.next();

         ExtendedIterator rangeIter = prop.listRange();
         while (rangeIter.hasNext()) {
            // Get each individual range
            OntResource range = (OntResource) rangeIter.next();

            if (range != null && range.getLocalName() != null) {
               if (range.getLocalName().equalsIgnoreCase(className)) {
                  propVector.add(prop.getLocalName());
               } else if (range.isClass()) {
                  OntClass rangeClass = range.asClass();
                  if (checkForSubClasses(rangeClass, className)) {
                     propVector.add(prop.getLocalName());
                  }
               }
            }
         }
      }
      return propVector;
   }

   public List<String> getPropertiesWithDomain (String className) {
      List<String> propVector = new ArrayList<String>();

      ExtendedIterator propIterator = listProperties(model.listOntProperties());

      while (propIterator.hasNext()) {
         // Get each individual property
         OntProperty prop = (OntProperty) propIterator.next();
         ExtendedIterator domainIter = prop.listDomain();
         while (domainIter.hasNext()) {
            // Get each individual range
            OntResource domain = (OntResource) domainIter.next();

            if (domain != null) {
               if (domain.getLocalName() != null) {
                  if (domain.getLocalName().equalsIgnoreCase(className)) {
                     propVector.add(prop.getLocalName());
                  } else if (domain.isClass()) {
                     OntClass domainClass = domain.asClass();
                     if (checkForSubClasses(domainClass, className)) {
                        propVector.add(prop.getLocalName());
                     }
                  }
               } else {
                  List<OntClass> domainClasses = getComponentClasses(domain);
                  for (OntClass partClass : domainClasses) {
                     if (partClass.getLocalName().equalsIgnoreCase(className)) {
                        propVector.add(prop.getLocalName());
                     } else if (checkForSubClasses(partClass, className)) {
                        propVector.add(prop.getLocalName());
                     }
                  }
               }
            }
         }
      }
      return propVector;
   }

   public List<String> getRequiredPropertiesWithDomain (String className) {
      List<String> propVector = new ArrayList<String>();

      ExtendedIterator propIterator = model.listOntProperties();

      while (propIterator.hasNext()) {
         // Get each individual property
         OntProperty prop = (OntProperty) propIterator.next();

         ExtendedIterator domainIter = prop.listDomain();
         while (domainIter.hasNext()) {
            // Get each individual range
            OntResource domain = (OntResource) domainIter.next();

            if (domain != null && domain.getLocalName() != null) {
               if (domain.getLocalName().equalsIgnoreCase(className)) {
                  if (prop.isFunctionalProperty() && prop.isInverseFunctionalProperty()) {
                     propVector.add(prop.getLocalName());
                  }
               } else if (domain.isClass()) {
                  OntClass domainClass = domain.asClass();
                  if (checkForSubClasses(domainClass, className)) {
                     if (prop.isFunctionalProperty() && prop.isInverseFunctionalProperty()) {
                        propVector.add(prop.getLocalName());
                     }
                  }
               }
            }
         }
      }
      return propVector;
   }

   private boolean checkForSubClasses (OntClass tempClass, String className) {
      ExtendedIterator subIter = tempClass.listSubClasses(false);
      while (subIter.hasNext()) {
         OntClass subClass = (OntClass) subIter.next();
         if (!subClass.isAnon() && subClass.getLocalName().equalsIgnoreCase(className)) {
            return true;
         }
      }
      return false;
   }

   public Map<List<String>, String> getDomainForProperty (OntoProperty op) {
      Map<List<String>, String> domainsMap = new HashMap<List<String>, String>(1);
      OntProperty jenaOP = model.getOntProperty(op.getNameSpace() + op.getName());
      OntResource or = jenaOP.getDomain();
      if (or.isClass()) {
         List<String> domains = new ArrayList<String>(1);
         if (or.asClass().isUnionClass()) {
            UnionClass uc = or.asClass().asUnionClass();
            ExtendedIterator it = uc.listOperands();
            while (it.hasNext()) {
               OntClass partClass = (OntClass) it.next();
               domains.add(partClass.getLocalName());
            }
            domainsMap.put(domains, OntologyConstants.UNION_CLASS);
         } else if (or.asClass().isIntersectionClass()) {
            IntersectionClass uc = or.asClass().asIntersectionClass();
            ExtendedIterator it = uc.listOperands();
            while (it.hasNext()) {
               OntClass partClass = (OntClass) it.next();
               domains.add(partClass.getLocalName());
            }
            domainsMap.put(domains, OntologyConstants.INTERSECTION_CLASS);
         } else {
            domains.add(or.asClass().getLocalName());
            domainsMap.put(domains, OntologyConstants.SIMPLE_CLASS);
         }
      }
      return domainsMap;
   }

   public String getRangeForProperty (OntoProperty op) {
      OntProperty jenaOP = model.getOntProperty(op.getNameSpace() + op.getName());
      return jenaOP.getRange().getLocalName();
   }

   public OntClass getConcept (String conceptName) {
      return model.getOntClass(this.ontNamespace + "/" + this.ontName + conceptName);
   }

   public boolean hasSuperProperty (OntoProperty op, String superPropName) {
      OntProperty jenaOP = model.getOntProperty(op.getNameSpace() + op.getName());
      ExtendedIterator superIter = jenaOP.listSuperProperties();
      return checkIfPropertyExistsInList(superPropName, superIter);
   }

   public boolean hasSubProperty (OntoProperty op, String superPropName) {
      OntProperty jenaOP = model.getOntProperty(op.getNameSpace() + op.getName());
      ExtendedIterator superIter = jenaOP.listSuperProperties();
      return checkIfPropertyExistsInList(superPropName, superIter);
   }

   public boolean isObjectProperty (OntoProperty op) {
      OntProperty jenaOP = model.getOntProperty(op.getNameSpace() + op.getName());
      return jenaOP.isObjectProperty();
   }

   public boolean isDatatypeProperty (OntoProperty op) {
      OntProperty jenaOP = model.getOntProperty(op.getNameSpace() + op.getName());
      return jenaOP.isDatatypeProperty();
   }

   public boolean isFunctionalProperty (OntoProperty op) {
      OntProperty jenaOP = model.getOntProperty(op.getNameSpace() + op.getName());
      return jenaOP.isFunctionalProperty();
   }

   public boolean isInverseFunctionalProperty (OntoProperty op) {
      OntProperty jenaOP = model.getOntProperty(op.getNameSpace() + op.getName());
      return jenaOP.isInverseFunctionalProperty();
   }

   public boolean areInverseProperties (OntoProperty op1, OntoProperty op2) {
      OntProperty jenaOP1 = model.getOntProperty(op1.getNameSpace() + op1.getName());
      OntProperty jenaOP2 = model.getOntProperty(op2.getNameSpace() + op2.getName());
      return jenaOP1.isInverseOf(jenaOP2);
   }

   private boolean checkIfPropertyExistsInList (String propName, ExtendedIterator propIter) {
      while (propIter.hasNext()) {
         OntProperty curProp = (OntProperty) propIter.next();
         if (curProp != null && curProp.getNameSpace() != null) {
            if (curProp.getLocalName().equalsIgnoreCase(propName)) {
               return true;
            }
         }
      }
      return false;
   }

   public List<String> getDefaultValuePropertyValues (OntoProperty op, OntoProperty valueProp) {
      List<String> propValues = new ArrayList<String>();
      OntProperty prop = model.getOntProperty(op.getNameSpace() + op.getName());
      NodeIterator it = prop.listPropertyValues(model.getOntProperty(op.getNameSpace() + valueProp.getName()));
      while (it.hasNext()) {
         Object node = it.next();
         if (node instanceof Literal) {
            propValues.add(((Literal) node).getString());
         }
      }
      return propValues;
   }

   public IOntologyConfiguration getOntologyConfigurationService () {
      return ontologyConfigurationService;
   }

   public void setOntologyConfigurationService (IOntologyConfiguration ontologyConfigurationService) {
      this.ontologyConfigurationService = ontologyConfigurationService;
   }
}
