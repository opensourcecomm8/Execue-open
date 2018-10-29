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
import java.util.Set;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.execue.core.ExeCueBaseTest;
import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.SecondaryWord;
import com.execue.core.common.bean.entity.Type;
import com.execue.core.exception.swi.KDXException;

public class KDXRetrievalServiceTest extends ExeCueBaseTest {

   Logger logger = Logger.getLogger(KDXRetrievalServiceTest.class);

   @Before
   public void setUp () throws Exception {
      baseTestSetup();
   }

   @After
   public void tearDown () throws Exception {
      baseTestTeardown();
   }

   // @Test
   public void testGetAllSecondaryWordsForModel () {
      Long modelId = 101L;
      try {
         List<SecondaryWord> secondaryWordsForModel = getKDXRetrievalService().getAllSecondaryWordsForModel(modelId);
      } catch (KDXException e) {
         e.printStackTrace();
         Assert.fail(e.getMessage());
      }
   }

   @Test
   public void testGetEligibleSecondaryWordsForModel () {
      Long modelId = 101L;
      Long threshold = 150L;
      try {
         Set<String> secondaryWordsForModel = getKDXRetrievalService().getEligibleSecondaryWordsForModel(modelId,
                  threshold);
         if (logger.isDebugEnabled()) {
            logger.debug("\nEligible Secondary Words: " + secondaryWordsForModel);
         }
      } catch (KDXException e) {
         e.printStackTrace();
         Assert.fail(e.getMessage());
      }
   }

   // @Test
   public void testGetTypeBED () {
      Type type = new Type();
      type.setId(108L);
      try {
         BusinessEntityDefinition typeBED = getKDXRetrievalService().getTypeBusinessEntityDefinition(type.getId());
         System.out.println(typeBED.getId());
      } catch (KDXException e) {
         e.printStackTrace();
         Assert.fail(e.getMessage());
      }
   }
}
