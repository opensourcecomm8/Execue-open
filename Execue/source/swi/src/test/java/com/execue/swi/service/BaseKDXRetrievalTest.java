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


package com.execue.swi.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.Concept;
import com.execue.swi.SWIBaseTest;
import com.execue.swi.exception.KDXException;

public class BaseKDXRetrievalTest extends SWIBaseTest {

   @BeforeClass
   public static void setUp () {
      baseSetup();
   }

   // @Test
   public void testGetBEDByNames () {
      String conceptName = "ScalingFactor";
      try {
         BusinessEntityDefinition bed = getBaseKDXRetrievalService().getBusinessEntityDefinitionByNames(conceptName,
                  null);
         System.out.println("BED : " + bed.getId());
      } catch (KDXException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testGetConceptBEDByName () {
      try {
         BusinessEntityDefinition bed = getBaseKDXRetrievalService().getConceptBEDByName("Country");
         System.out.println("bed id --" + bed.getId());
      } catch (KDXException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   // @Test
   public void testGetConceptByName () {
      try {
         Concept concept = getBaseKDXRetrievalService().getConceptByName("Country");
         System.out.println("Concept id :-" + concept.getId());
      } catch (KDXException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   // @Test
   public void testGetSystemVariable () {
      Set sysVariable = new HashSet();
      try {
         sysVariable = getBaseKDXRetrievalService().getSystemVariables();
         System.out.println("system Variables :-" + sysVariable);
      } catch (KDXException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   @Test
   public void testGetSecondrayWordsByBaseModel () {
      List<String> baseRIontoWords = new ArrayList<String>(1);
      try {
         baseRIontoWords = getBaseKDXRetrievalService().getSecondrayWordsByBaseModel();
         System.out.println("baseRIontoWords :" + baseRIontoWords.toString());
      } catch (KDXException e) {
         // TODO: handle exception
         e.printStackTrace();
      }
   }
}
