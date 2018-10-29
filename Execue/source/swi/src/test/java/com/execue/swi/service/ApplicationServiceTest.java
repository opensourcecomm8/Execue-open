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

import java.util.List;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;

import com.execue.core.common.bean.entity.Application;
import com.execue.core.common.bean.entity.UnstructuredApplicationDetail;
import com.execue.core.common.type.BusinessEntityType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.FacetNatureType;
import com.execue.swi.SWIBaseTest;
import com.execue.swi.exception.KDXException;

public class ApplicationServiceTest extends SWIBaseTest {

   @BeforeClass
   public static void setUp () {
      baseSetup();
   }

   // @Test
   public void testtest () throws KDXException {
      List<Long> list = getKDXRetrievalService().findMatchingTypeInstanceIncludingVariations(1L, 103L, "average",
               BusinessEntityType.REALIZED_TYPE_LOOKUP_INSTANCE);
      System.out.println(list.size());
   }

   // @Test
   public void testGetApplications () {
      try {
         List<Application> applications = getApplicationRetrievalService().getApplications(1L);
         Assert.assertEquals("application size must not be zero", applications.size());
      } catch (KDXException e) {
         e.printStackTrace();
      }
   }

   @Test
   public void testGetApplicationByKey () {
      try {
         String applicationKey = "Abc_Cards";
         Application application = getApplicationRetrievalService().getApplicationByKey(applicationKey);
         Assert.assertEquals(applicationKey, application.getApplicationKey());
      } catch (KDXException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testUnstructuredAppDetail () {
      Long applicationId = 110L;
      try {
         UnstructuredApplicationDetail unstructuredApplicationDetail = getApplicationRetrievalService()
                  .getUnstructuredApplicationDetailByApplicationId(applicationId);
         System.out.println(unstructuredApplicationDetail.getApplicationId());

         FacetNatureType facetNature = getApplicationRetrievalService().getFacetNatureByApplicationId(applicationId);
         System.out.println("facetNature:-" + facetNature);

         CheckType locationFromContent = getApplicationRetrievalService().getLocationFromContentByApplicationId(
                  applicationId);
         System.out.println("locationFromContent:-" + locationFromContent);
      } catch (KDXException e) {
         e.printStackTrace();
      }
   }

   //   @Test
   public void testAppWithExample () {
      Long applicationId = 101L;
      try {
         Application applicationWithExample = getApplicationRetrievalService().getApplicationWithExample(applicationId);

         System.out.println(applicationWithExample);

      } catch (KDXException e) {
         e.printStackTrace();
      }
   }
}
