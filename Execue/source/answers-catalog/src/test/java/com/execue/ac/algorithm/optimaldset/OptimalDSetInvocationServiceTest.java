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


package com.execue.ac.algorithm.optimaldset;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.execue.ac.AnswersCatalogBaseTest;
import com.execue.ac.algorithm.optimaldset.bean.OptimalDSetCubeOutputInfo;
import com.execue.ac.algorithm.optimaldset.bean.OptimalDSetMartOutputInfo;
import com.execue.ac.exception.AnswersCatalogException;
import com.execue.core.common.bean.optimaldset.OptimalDSetContext;
import com.execue.core.common.type.OperationRequestLevel;

/**
 * This test case is for testing optimalDset algorithm along with consumption of its output
 * 
 * @author Vishay
 * @version 1.0
 * @since 27/06/09
 */
public class OptimalDSetInvocationServiceTest extends AnswersCatalogBaseTest {

   @Before
   public void setUp () {
      answersCatalogBaseSetUp();
   }

   @After
   public void tearDown () {
      answersCatalogBaseTearDown();
   }

   //   @Test
   public void testGenerateCubeOptimalDset () {
      try {
         // NOTE: Specify the correct asset id and model id
         OptimalDSetCubeOutputInfo cubeOptimalDset = getOptimalDSetInvocationService().generateCubeOptimalDset(11L,
                  110L);
         //OptimalDSetCubeOutputInfo cubeOptimalDset = getOptimalDSetInvocationService().generateCubeOptimalDset(1001L);
      } catch (AnswersCatalogException e) {
         e.printStackTrace();
      }
   }

   @Test
   public void testExecuteOptimalDset () {
      try {
         // NOTE: Specify the correct asset id and model id
         Long assetId = 11L;
         Long modelId = 110L;
         OptimalDSetContext optimalDSetContext = new OptimalDSetContext();
         optimalDSetContext.setId(assetId);
         optimalDSetContext.setModelId(modelId);
         optimalDSetContext.setOperationRequestLevel(OperationRequestLevel.ASSET);
         getOptimalDSetWrapperService().executeOptimalDSet(optimalDSetContext);
      } catch (AnswersCatalogException e) {
         e.printStackTrace();
      }
   }

   //   @Test
   public void testGenerateMartOptimalDset () {
      try {
         // NOTE: Specify the correct asset id and model id
         OptimalDSetMartOutputInfo martOptimalDset = getOptimalDSetInvocationService().generateMartOptimalDset(11L,
                  110l);
      } catch (AnswersCatalogException e) {
         e.printStackTrace();
      }
   }
}
