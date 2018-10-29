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


package com.execue.uswhda.dao.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.execue.core.common.bean.entity.unstructured.Feature;
import com.execue.core.common.bean.entity.unstructured.FeatureDetail;
import com.execue.core.common.bean.entity.unstructured.FeatureRange;
import com.execue.core.common.bean.entity.unstructured.FeatureValue;
import com.execue.core.common.bean.entity.unstructured.RIFeatureContent;
import com.execue.core.common.bean.uss.UnstructuredKeywordSearchInput;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.uswhda.dao.UnstructuredWarehouseCommonBaseTest;

public class UnstructuredWarehouseDataAccessDAOTest extends UnstructuredWarehouseCommonBaseTest {

   private static final Logger logger = Logger.getLogger(UnstructuredWarehouseDataAccessDAOTest.class);

   @BeforeClass
   public static void setUp () {
      baseSetup();
   }

   @AfterClass
   public static void tearDown () {
      baseTeardown();
   }

   // @Test
   public void testGetAllFeatures () {
      Long contextId = 110L;
      try {

         List<Feature> features = getFeatureDAO().getAllFeatures(contextId);
         for (Feature feature : features) {
            logger.info(feature.getFeatureName());
         }
      } catch (DataAccessException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

   }

   // @Test
   public void testGetSemantifiedContentIdsByUserQueryId () {
      Long contextId = 111L;
      Long userQueryId = 1L;
      try {

         List<Long> semantifiedContentIds = getSemantifiedContentDAO().getSemantifiedContentIdsByUserQueryId(contextId,
                  userQueryId);
         logger.info(semantifiedContentIds.size());
         for (Long semantifiedContentId : semantifiedContentIds) {
            logger.info(semantifiedContentId);
         }
      } catch (DataAccessException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

   }

   // @Test
   public void testDeleteExistingSemantifiedContentFeatureInfo () {
      Long contextId = 111L;
      Long semantifiedContentId = 1001L;
      try {
         getSemantifiedContentDAO().deleteExistingSemantifiedContentFeatureInfo(contextId, semantifiedContentId);
      } catch (DataAccessException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

   }

   // @Test
   public void testDeleteExistingSemantifiedContentKeywordInfo () {
      Long contextId = 111L;
      Long semantifiedContentId = 1001L;
      try {
         getSemantifiedContentDAO().deleteExistingSemantifiedContentKeywordInfo(contextId, semantifiedContentId);
      } catch (DataAccessException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

   }

   // @Test
   public void testGetAllFeatureDetailWithAdditionalFeartureInfo () {
      Long contextId = 1508L;

      try {
         List<FeatureDetail> fds = getFeatureDetailDAO().getAllFeatureDetailWithAdditionalFeartureInfo(contextId);
         System.out.println(fds.size());
      } catch (DataAccessException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

   }

   // @Test
   public void testPopulateRIFeatureContents () {
      Long contextId = 110L;

      try {
         List<RIFeatureContent> populateRIFeatureContents = getUnstructuredRIFeatureContentDAO()
                  .populateRIFeatureContents(contextId);
         System.out.println("test case:::::::::::::::::" + populateRIFeatureContents.size());
      } catch (DataAccessException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

   }

   // @Test
   public void testDeleteRIFeatureContents () {
      Long contextId = 111L;

      try {
         getUnstructuredRIFeatureContentDAO().deleteRIFeatureContentByContextId(contextId);

      } catch (DataAccessException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

   }

   // @Test
   public void testFeatureRangesByFeatureId () {
      Long contextId = 110L;
      Long featureId = 6L;

      try {
         List<FeatureRange> featureRanges = getFeatureRangeDAO().getFeatureRangesByFeatureId(contextId, featureId);
         System.out.println(featureRanges);

      } catch (DataAccessException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

   }

   // @Test
   public void getFeatureValueByFeatureIdAndFeatureValue () {
      Long contextId = 110L;
      Long featureId = 24L;
      String featureValue = "Brigham City";

      try {
         FeatureValue featureValueByFeatureIdAndFeatureValue = getFeatureValueDAO()
                  .getFeatureValueByFeatureIdAndFeatureValue(contextId, featureId, featureValue);
         System.out.println(featureValueByFeatureIdAndFeatureValue.getFeatureValue());

      } catch (DataAccessException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

   }

   // @Test
   public void testSemantifiedContentKeyWordMatchResultForUserQuery () {
      UnstructuredKeywordSearchInput unstructuredKeywordSearchInput = new UnstructuredKeywordSearchInput();
      try {
         getSemantifiedContentDAO().populateSemantifiedContentKeyWordMatchResultForUserQuery(
                  unstructuredKeywordSearchInput);
      } catch (DataAccessException e) {
         e.printStackTrace();
      }
   }

   @Test
   public void testFeaturesByContextId () {
      Long contextId = 110L;
      try {

         List<Feature> features = getFeatureDAO().getFeaturesByContextId(contextId);
         for (Feature feature : features) {
            logger.info(feature.getFeatureName());
         }
      } catch (DataAccessException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }
}
