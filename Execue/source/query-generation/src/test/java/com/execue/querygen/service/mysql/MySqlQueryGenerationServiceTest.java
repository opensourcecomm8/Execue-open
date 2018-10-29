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


package com.execue.querygen.service.mysql;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.execue.core.common.bean.querygen.JoinEntity;
import com.execue.core.common.bean.querygen.QueryGenerationInput;
import com.execue.core.common.bean.querygen.QueryGenerationOutput;
import com.execue.querygen.service.QueryGenerationCommonBaseTest;

public class MySqlQueryGenerationServiceTest extends QueryGenerationCommonBaseTest {

   private static QueryGenerationOutput queryGenerationOutput;

   @BeforeClass
   public static void setUp () {
      queryGenerationBaseSetUp();
   }

   @AfterClass
   public static void tearDown () {
      queryGenerationBaseTearDown();
   }

   @Test
   public void testGenerateQuery () {
      QueryGenerationInput queryGenerationInput = prepareQueryGenerationInput();
      QueryGenerationOutput queryGenerationOutput = getQueryGenerationService().generateQuery(queryGenerationInput);
      //      QueryGenerationOutput queryGenerationOutput = getQueryGenerationServiceFactory().getQueryGenerationService(
      //               queryGenerationInput.getTargetAsset()).generateQuery(queryGenerationInput);
      assertNotNull("QueryGenerationOutput Object is Null", queryGenerationOutput);
      setQueryGenerationOutput(queryGenerationOutput);
   }

   @Test
   public void testExtractQueryString () {
      // logger.debug(getQueryGenerationService().extractQueryString(getQueryGenerationOutput()));
   }

   // @Test
   public void testJoinExtraction () {
      List<JoinEntity> joinEntities = new ArrayList<JoinEntity>();
      populateJoinEntities(joinEntities);
      // logger.debug(createJoinCondition(joinEntities));
   }

   public QueryGenerationOutput getQueryGenerationOutput () {
      return queryGenerationOutput;
   }

   public void setQueryGenerationOutput (QueryGenerationOutput queryGenerationOutput) {
      MySqlQueryGenerationServiceTest.queryGenerationOutput = queryGenerationOutput;
   }

}
