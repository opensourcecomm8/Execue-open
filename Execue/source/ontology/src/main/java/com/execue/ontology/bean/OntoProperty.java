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

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.execue.ontology.configuration.OntologyConstants;
import com.execue.ontology.service.OntologyFactory;

public class OntoProperty extends OntoResource {

   public OntoProperty (String name) {
      super(name);
   }

   public OntoProperty (String nameSpace, String name) {
      super(nameSpace, name);
   }

   public OntoResource getDomain () {
      Map<List<String>, String> domainNames = OntologyFactory.getInstance().getOntologyModelServices()
               .getDomainForProperty(this);
      for (Map.Entry<List<String>, String> entry : domainNames.entrySet()) {
         List<String> domains = entry.getKey();
         String type = entry.getValue();
         if (type.equals(OntologyConstants.UNION_CLASS)) {
            UnionOntoClass uc = new UnionOntoClass();
            for (String domainName : domains) {
               uc.addComponentClass(domainName);
            }
            return uc;
         } else if (type.equals(OntologyConstants.INTERSECTION_CLASS)) {
            IntersectionOntoClass ic = new IntersectionOntoClass();
            for (String domainName : domains) {
               ic.addComponentClass(domainName);
            }
            return ic;
         } else {
            return OntologyFactory.getInstance().getOntologyService().getConcept(domains.get(0));
         }
      }
      return null;
   }

   public String getDomainName () {
      Map<List<String>, String> domainNames = OntologyFactory.getInstance().getOntologyModelServices()
               .getDomainForProperty(this);
      Iterator<Map.Entry<List<String>, String>> it = domainNames.entrySet().iterator();
      if (it.hasNext()) {
         Map.Entry<List<String>, String> entry = it.next();
         List<String> domains = entry.getKey();
         return domains.get(0);
      }
      return null;
   }

   public OntoResource getRange () {
      String rangeName = OntologyFactory.getInstance().getOntologyModelServices().getRangeForProperty(this);
      return OntologyFactory.getInstance().getOntologyService().getConcept(rangeName);
   }

   public String getRangeName () {
      return OntologyFactory.getInstance().getOntologyModelServices().getRangeForProperty(this);
   }

   public boolean isObjectProperty () {
      return OntologyFactory.getInstance().getOntologyService().isObjectProperty(this.getName());
   }

   public boolean isDatatypeProperty () {
      return OntologyFactory.getInstance().getOntologyService().isDatatypeProperty(this.getName());
   }
}
