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


/**
 *
 */
package com.execue.swi.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.EntityTripleDefinition;
import com.execue.core.common.bean.entity.Path;
import com.execue.core.common.bean.entity.PathDefinition;
import com.execue.swi.SWIBaseTest;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.SWIException;

/**
 * @author Nihar
 */

public class KDXModelServiceTest extends SWIBaseTest {

   // TODO hardcoded Ids as of NOW will need to have a framwork for inserting dummy values for Testing
   private static final long DESTINATION_BED_ID = 1106L;
   private static final long relationBedId      = 1575L;
   private static final long sourceBedID        = 1199L;
   Logger                    log                = Logger.getLogger(KDXModelServiceTest.class);

   @BeforeClass
   public static void setUp () {
      baseSetup();
   }

   // @Test
   public void testGetEntityTriple () {
      try {
         log.debug("inside testGetEntityTriple");
         getKDXModelService().getEntityTriple(sourceBedID, relationBedId, DESTINATION_BED_ID);

      } catch (SWIException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testGetEntityTripleForDestination () {
      try {
         log.debug("inside testGetEntityTripleForDestination");
         List<EntityTripleDefinition> triples = getKDXModelService().getEntityTriplesForDestination(DESTINATION_BED_ID);
         for (EntityTripleDefinition triple : triples) {
            System.out.println(triple.getDefaultValue());
         }

      } catch (SWIException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testGetEntityTriplesForSource () {
      try {
         log.debug("inside testGetEntityTriplesForSource");
         List<EntityTripleDefinition> triples = getKDXModelService().getEntityTriplesForSource(sourceBedID);
         for (EntityTripleDefinition triple : triples) {
            System.out.println(triple.getDefaultValue());
         }

      } catch (SWIException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testGetEntityTriplesForSourceAndRelation () {
      try {
         log.debug("inside testGetEntityTriplesForSourceAndRelation");
         List<EntityTripleDefinition> triples = getKDXModelService().getEntityTriplesForSourceAndRelation(sourceBedID,
                  relationBedId);
         for (EntityTripleDefinition triple : triples) {
            System.out.println(triple.getDefaultValue());
         }

      } catch (SWIException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testGetEntityTriplesForDestinationAndRelation () {
      try {
         log.debug("inside testGetEntityTriplesForDestinationAndRelation");
         List<EntityTripleDefinition> triples = getKDXModelService().getEntityTriplesForDestinationAndRelation(
                  relationBedId, DESTINATION_BED_ID);
         for (EntityTripleDefinition triple : triples) {
            System.out.println(triple.getDefaultValue());
         }

      } catch (SWIException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testGetEntityTriplesForRelation () {
      try {
         log.debug("inside testGetEntityTriplesForRelation");
         List<EntityTripleDefinition> triples = getKDXModelService().getEntityTriplesForRelation(relationBedId);
         for (EntityTripleDefinition triple : triples) {
            System.out.println(triple.getDefaultValue());
         }

      } catch (SWIException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testGetPathDefinitionDetails () {
      try {
         log.debug("inside testGetPathDefinitionDetails");
         List<PathDefinition> pdList = getKDXModelService().getPathDefinitionDetails(sourceBedID, DESTINATION_BED_ID);
         for (PathDefinition pd : pdList) {
            System.out.println(pd.getPathLength());
            for (Path p : pd.getPaths()) {
               System.out.println("\t" + p.getEntityTripleDefinition().getCardinality());
               System.out.println("\t" + p.getEntityTripleDefinition().getSourceBusinessEntityDefinition().getId());
               System.out.println("\t" + p.getEntityTripleDefinition().getRelation().getId());
               System.out
                        .println("\t" + p.getEntityTripleDefinition().getDestinationBusinessEntityDefinition().getId());
            }
         }
      } catch (SWIException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testGetAllparentConcepts () {
      try {
         getKDXModelService().getAllParentConcepts(1111L);
      } catch (KDXException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testHasParent () {
      try {
         List<PathDefinition> paths = getKDXModelService().getParentPathDefinitions(1106L, 1111L);
         System.out.println(paths.size());
      } catch (KDXException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   // @Test
   public void testGetImmidiateParentConcepts () {
      try {
         getKDXModelService().getImmediateParentConcepts(1111L);
      } catch (KDXException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testGetAllDistributionConcepts () {
      try {
         log.debug("inside getAllDistributionConcepts");
         List<BusinessEntityDefinition> allDistributionConcepts = getKDXModelService().getAllDistributionConcepts();
      } catch (SWIException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testGetAllAbstractConcepts () {
      try {
         log.debug("inside testGetAllAbstractConcepts");
         List<BusinessEntityDefinition> allAbstractConcepts = getKDXModelService().getAllAbstractConcepts();
         log.debug(allAbstractConcepts.size());

      } catch (SWIException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testGetAllAttributeConcepts () {
      try {
         log.debug("inside testGetAllAttributeConcepts");
         List<BusinessEntityDefinition> allAttributeConcepts = getKDXModelService().getAllAttributeConcepts();
         log.debug(allAttributeConcepts.size());

      } catch (SWIException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testGetAllComparativeConcepts () {
      try {
         log.debug("inside testGetAllComparativeConcepts");
         List<BusinessEntityDefinition> allComparativeConcepts = getKDXModelService().getAllComparativeConcepts();
         log.debug(allComparativeConcepts.size());

      } catch (SWIException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testGetAllEnumerationConcepts () {
      try {
         log.debug("inside testGetAllEnumerationConcepts");
         List<BusinessEntityDefinition> allEnumerationConcepts = getKDXModelService().getAllEnumerationConcepts();
         log.debug(allEnumerationConcepts.size());

      } catch (SWIException e) {
         e.printStackTrace();
      }
   }

   @Test
   public void testGetAllQuantitativeConcepts () {
      try {
         log.debug("inside testGetAllQuantitativeConcepts");
         List<BusinessEntityDefinition> allQuantitativeConcepts = getKDXModelService().getAllQuantitativeConcepts();
         log.debug(allQuantitativeConcepts.size());

      } catch (SWIException e) {
         e.printStackTrace();
      }
   }
}
