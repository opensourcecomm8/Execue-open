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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

import com.execue.core.common.bean.entity.Hierarchy;
import com.execue.core.common.bean.entity.HierarchyDetail;
import com.execue.swi.SWICommonBaseTest;
import com.execue.swi.exception.KDXException;

/**
 * @author plexus
 */
public class KDXManagementServiceTest extends SWICommonBaseTest {

   Logger logger = Logger.getLogger(KDXManagementServiceTest.class);

   @BeforeClass
   public static void setUp () {
      baseSetup();
   }

   // @Test
   public void getHierarchyByIdTest () {
      try {
         Hierarchy hierarchyById = getKDXRetrievalService().getHierarchyById(3l);
         printHierarchy(hierarchyById);
         for (HierarchyDetail hierarchyDetail : hierarchyById.getHierarchyDetails()) {
            System.out.println(hierarchyDetail.getId() + " " + hierarchyDetail.getConceptBedId() + " "
                     + hierarchyDetail.getLevel());
         }
      } catch (KDXException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   // @Test
   public void getHierarchiesByModelIdTest () {
      try {
         List<Hierarchy> hierarchies = getKDXRetrievalService().getHierarchiesByModelId(110l);
         for (Hierarchy hierarchy : hierarchies) {
            printHierarchy(hierarchy);
            System.out.println("Hieraechy Details");
            for (HierarchyDetail hierarchyDetail : hierarchy.getHierarchyDetails()) {
               System.out.println(hierarchyDetail.getId() + " " + hierarchyDetail.getConceptBedId() + " "
                        + hierarchyDetail.getLevel());
            }
         }
      } catch (KDXException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   @Test
   public void getUpdateHierarchyValidationTest () {
      try {
         List<Hierarchy> hierarchies = getKDXRetrievalService().getHierarchiesByModelId(110l);
         for (Hierarchy hierarchy : hierarchies) {
            printHierarchy(hierarchy);
            printHierarchyDetails(hierarchy.getHierarchyDetails());
            hierarchy.setName("xyz");
            getKDXManagementService().updateHierarchy(hierarchy.getModelGroupId(), hierarchy);
         }
      } catch (KDXException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   // @Test
   public void getExistingHierarchiesForConceptTest () {
      try {
         List<Hierarchy> hierarchies = getKDXRetrievalService().getExistingHierarchiesForConcept(123L);
         for (Hierarchy hierarchy : hierarchies) {
            printHierarchy(hierarchy);
            printHierarchyDetails(hierarchy.getHierarchyDetails());
         }
      } catch (KDXException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   private void printHierarchyDetails (Set<HierarchyDetail> hierarchyDetails) {
      System.out.println("Hieraechy Details");
      for (HierarchyDetail hierarchyDetail : hierarchyDetails) {
         System.out.println(hierarchyDetail.getId() + " " + hierarchyDetail.getConceptBedId() + " "
                  + hierarchyDetail.getLevel());
      }
   }

   private void printHierarchy (Hierarchy hierarchy) {
      System.out.println("Hieraechy");
      System.out.println(hierarchy.getId() + " " + hierarchy.getName() + " " + hierarchy.getModelGroupId());
   }

   // @Test
   public void updateHierarchyByIdTest () {
      try {

         Hierarchy existingHierarchy = getKDXRetrievalService().getHierarchyById(5L);
         System.out.println("Before Update********");
         System.out.println(existingHierarchy.getName());
         printHierarchyDetails(existingHierarchy.getHierarchyDetails());

         // update the hierarchy and details
         // existingHierarchy.setName("abc");
         Set<HierarchyDetail> existingHierarchyDetails = existingHierarchy.getHierarchyDetails();
         List<HierarchyDetail> newHierarchyDetails = new ArrayList<HierarchyDetail>();
         int i = 0;
         for (HierarchyDetail hierarchyDetail : existingHierarchyDetails) {
            if (i == 0) {
               i++;
               continue;
            }
            // hierarchyDetail.setConceptBedId(hierarchyDetail.getConceptBedId() + 1);
            newHierarchyDetails.add(hierarchyDetail);
         }

         existingHierarchy.setHierarchyDetails(new HashSet<HierarchyDetail>(newHierarchyDetails));
         getKDXManagementService().updateHierarchy(existingHierarchy.getModelGroupId(), existingHierarchy);

         System.out.println("After Update********");
         System.out.println(existingHierarchy.getName());
         printHierarchyDetails(existingHierarchy.getHierarchyDetails());
      } catch (KDXException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   // @Test
   public void deleteHierarchyTest () {
      try {
         Hierarchy hierarchyById = getKDXRetrievalService().getHierarchyById(6L);
         System.out.println(hierarchyById.getModelGroupId());
         getKDXManagementService().deleteHierarchy(hierarchyById);
      } catch (KDXException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   // @Test
   public void createHierarchyTest () {
      // HierarchyDetail
      HierarchyDetail hierarchyDetail = new HierarchyDetail();
      hierarchyDetail.setConceptBedId(125l);
      hierarchyDetail.setLevel(0);

      HierarchyDetail hierarchyDetail2 = new HierarchyDetail();
      hierarchyDetail2.setConceptBedId(124l);
      hierarchyDetail2.setLevel(1);

      Set<HierarchyDetail> hDetail = new HashSet<HierarchyDetail>();
      hDetail.add(hierarchyDetail);
      hDetail.add(hierarchyDetail2);

      // hierarchy
      Hierarchy hierarchy = new Hierarchy();
      hierarchy.setModelGroupId(110l);
      hierarchy.setName("xyz Hierarchy");
      hierarchy.setHierarchyDetails(hDetail);

      try {
         getKDXManagementService().createHierarchy(hierarchy.getModelGroupId(), hierarchy);
      } catch (KDXException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

}
