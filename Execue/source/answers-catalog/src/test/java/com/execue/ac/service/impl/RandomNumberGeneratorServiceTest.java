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


package com.execue.ac.service.impl;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.execue.ac.AnswersCatalogCommonBaseTest;
import com.execue.ac.exception.AnswersCatalogException;

public class RandomNumberGeneratorServiceTest extends AnswersCatalogCommonBaseTest {

   private static final Logger logger = Logger.getLogger(RandomNumberGeneratorServiceTest.class);

   @BeforeClass
   public static void setUp () {
      answersCatalogBaseSetUp();
   }

   @AfterClass
   public static void tearDown () {
      answersCatalogBaseTearDown();
   }

   public long getQueryId () {
      return System.currentTimeMillis();
   }

   @Test
   public void testGetNextRandomElement () {
      try {
         Integer randomNumber = getRandomNumberGeneratorService().getNextRandomElement(10);
         logger.debug(randomNumber.intValue());
      } catch (AnswersCatalogException answersCatalogException) {
         answersCatalogException.printStackTrace();
      }

   }

}
