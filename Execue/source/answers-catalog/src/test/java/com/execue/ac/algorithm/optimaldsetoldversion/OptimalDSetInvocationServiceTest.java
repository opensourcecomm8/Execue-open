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


package com.execue.ac.algorithm.optimaldsetoldversion;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.execue.ac.AnswersCatalogBaseTest;
import com.execue.ac.exception.AnswersCatalogException;

/**
 * This test case is for testing optimalDset algorithm along with consumption of its output
 * 
 * @author Vishay
 * @version 1.0
 * @since 27/06/09
 */
public class OptimalDSetInvocationServiceTest extends AnswersCatalogBaseTest {

   @BeforeClass
   public static void setUp () {
      answersCatalogBaseSetUp();
   }

   @AfterClass
   public static void tearDown () {
      answersCatalogBaseTearDown();
   }

   @Test
   public void testOptimalDSetAlgorithm () {
      try {
         getOptimalDsetInvocationService().generateCubeOptimalDset(1001L, 110L);
      } catch (AnswersCatalogException e) {
         e.printStackTrace();
      }
   }
}
