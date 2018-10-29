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

import static junit.framework.Assert.fail;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import junit.framework.Assert;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.execue.core.ExeCueBaseTest;
import com.execue.core.common.bean.IWeightedEntity;
import com.execue.core.common.bean.WeightInformation;
import com.execue.core.common.bean.entity.Cloud;
import com.execue.core.common.bean.entity.CloudComponent;
import com.execue.core.common.bean.entity.RICloud;
import com.execue.core.common.bean.nlp.NLPConstants;
import com.execue.core.common.type.CloudCategory;
import com.execue.core.common.type.CloudOutput;
import com.execue.core.exception.swi.KDXException;
import com.execue.nlp.bean.entity.RecognitionEntity;
import com.execue.nlp.bean.entity.RecognizedCloudEntity;
import com.execue.nlp.bean.entity.TypeEntity;

/**
 * @author Nitesh
 */
public class KDXCloudRetrievalServiceTest extends ExeCueBaseTest {

   private final Logger logger = Logger.getLogger(KDXCloudRetrievalServiceTest.class);

   @Before
   public void setUp () throws Exception {
      baseTestSetup();
   }

   @After
   public void tearDown () throws Exception {
      baseTestTeardown();
   }

   // @Test
   public void testGetRICloudsByCompBedIdsAndNewValueCloudOutput () {
      Set<Long> compBedIds = new HashSet<Long>();
      compBedIds.add(202L); // year
      compBedIds.add(401L); // number
      try {
         Map<CloudCategory, List<RICloud>> cloudsByCompBedIdsAndCloudOutput = getKDXCloudRetrievalService()
                  .getRICloudsByCompBedIdsAndCloudOutput(compBedIds, CloudOutput.NEW_VALUE);
         printCloudInfo(cloudsByCompBedIdsAndCloudOutput);
      } catch (KDXException e) {
         e.printStackTrace();
         Assert.fail("Failed to get the RI Cloud for type be ids: " + e.getMessage());
      }
   }

   // @Test
   public void testGetDefaultAppCloud () {
      Long modelId = 112L;
      try {
         Cloud defaultAppCloud = getKDXCloudRetrievalService().getDefaultAppCloud(modelId);
         System.out.println(defaultAppCloud);
      } catch (KDXException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testRecognizedCloudsEntities () {
      Set<Long> typeBeIds = new HashSet<Long>();
      typeBeIds.add(202L); // year
      typeBeIds.add(401L); // number

      // eg. query = "sales for year 2006";
      List<IWeightedEntity> recEntities = new ArrayList<IWeightedEntity>(1);
      recEntities.add(getYearRecognitionEntiy());
      recEntities.add(getNumberRecognitionEntiy());

      try {
         Map<CloudCategory, List<RICloud>> cloudsByCompBedIdsAndCategory = getKDXCloudRetrievalService()
                  .getRICloudsByCompBedIdsAndCloudOutput(typeBeIds, CloudOutput.NEW_VALUE);
         printCloudInfo(cloudsByCompBedIdsAndCategory);
         for (Entry<CloudCategory, List<RICloud>> entry : cloudsByCompBedIdsAndCategory.entrySet()) {
            List<RecognizedCloudEntity> recCloudEntities = getNLPServiceHelper().getRecognizedCloudEntities(
                     entry.getValue(), recEntities);
            printRecognizedEntities(recCloudEntities);
         }
      } catch (KDXException e) {
         e.printStackTrace();
         Assert.fail("Failed to get the RI Cloud for type be ids: " + e.getMessage());
      }
   }

   // @Test
   public void testGetRICloudsByCompBedIdsAndEnhanceCloudOuput () {
      Set<Long> compBedIds = new HashSet<Long>();
      compBedIds.add(104L); // adjective type for top bottom enhance cloud
      compBedIds.add(19740L); // nominal year for i2app app cloud
      try {
         Map<CloudCategory, List<RICloud>> cloudsByCompBedIdsAndCloudOuput = getKDXCloudRetrievalService()
                  .getRICloudsByCompBedIdsAndCloudOutput(compBedIds, CloudOutput.ENHANCED);
         printCloudInfo(cloudsByCompBedIdsAndCloudOuput);
      } catch (KDXException e) {
         e.printStackTrace();
         Assert.fail("Failed to get the RI Cloud for type be ids: " + e.getMessage());
      }
   }

   @Test
   public void testGetAllCloudComponentsByCloudIdWithDetails () {
      Cloud cloud;
      try {
         cloud = getKDXCloudRetrievalService().getCloudByName("SECFilings");
         List<CloudComponent> cloudComponents = getKDXCloudRetrievalService()
                  .getAllCloudComponentsByCloudIdWithDetails(cloud.getId());
         if (logger.isInfoEnabled()) {
            logger.info("Cloud Component Count : " + cloudComponents.size());
            printCloudComponents(cloudComponents);
         }
      } catch (KDXException kdxException) {
         logger.error(kdxException, kdxException);
         fail(kdxException.getMessage());
      }

   }

   private void printCloudComponents (List<CloudComponent> cloudComponents) {
      if (CollectionUtils.isEmpty(cloudComponents)) {
         return;
      }
      for (CloudComponent cloudComponent : cloudComponents) {
         logger.info("Model Group: " + cloudComponent.getComponentBed().getModelGroup().getId() + " Component: "
                  + cloudComponent.getComponentBed().getId());
      }

   }

   private void printRecognizedEntities (List<RecognizedCloudEntity> recCloudEntities) {

      StringBuilder cloudInfo = new StringBuilder();
      cloudInfo.append("Recognized Cloud Entity Info: ");
      for (RecognizedCloudEntity recognizedCloudEntity : recCloudEntities) {
         cloudInfo.append("\n").append(recognizedCloudEntity);
      }
      if (logger.isInfoEnabled()) {
         logger.info(cloudInfo);
      }
   }

   private RecognitionEntity getYearRecognitionEntiy () {
      WeightInformation weightInformation = new WeightInformation();
      weightInformation.setRecognitionQuality(NLPConstants.MAX_QUALITY);
      weightInformation.setRecognitionWeight(NLPConstants.MAX_WEIGHT);
      TypeEntity recognitionEntity = new TypeEntity();
      recognitionEntity.setWeightInformation(weightInformation);
      recognitionEntity.setWord("Year");
      recognitionEntity.setPosition(2);
      recognitionEntity.setTypeBedId(102L);
      return recognitionEntity;
   }

   private RecognitionEntity getNumberRecognitionEntiy () {
      WeightInformation weightInformation = new WeightInformation();
      weightInformation.setRecognitionQuality(NLPConstants.MAX_QUALITY);
      weightInformation.setRecognitionWeight(NLPConstants.MAX_WEIGHT);
      TypeEntity recognitionEntity = new TypeEntity();
      recognitionEntity.setWeightInformation(weightInformation);
      recognitionEntity.setWord("2006");
      recognitionEntity.setPosition(3);
      recognitionEntity.setTypeBedId(401L);
      return recognitionEntity;
   }

   private void printCloudInfo (Map<CloudCategory, List<RICloud>> cloudsByCompBedIdsAndCategory) {
      StringBuilder cloudInfo = new StringBuilder();
      for (Entry<CloudCategory, List<RICloud>> entry : cloudsByCompBedIdsAndCategory.entrySet()) {
         cloudInfo.append("\n\nCloud Category: " + entry.getKey());
         for (RICloud cloud : entry.getValue()) {
            cloudInfo.append("\n").append(cloud);
         }
      }

      if (logger.isInfoEnabled()) {
         logger.info(cloudInfo);
      }
   }
}
