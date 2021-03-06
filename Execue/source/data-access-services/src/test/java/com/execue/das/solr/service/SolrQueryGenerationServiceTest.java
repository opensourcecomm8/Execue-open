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


package com.execue.das.solr.service;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.execue.das.DataAccessServicesCommonBaseTest;
import com.execue.das.solr.bean.SolrFacetQuery;
import com.execue.das.solr.exception.SolrException;

/**
 * @author Vishay
 */
public class SolrQueryGenerationServiceTest extends DataAccessServicesCommonBaseTest {

   @BeforeClass
   public static void setUp () {
      baseSetup();
   }

   @AfterClass
   public static void tearDown () {
      baseTeardown();
   }

   // @Test
   public void testGenerateSolrQuery () throws SolrException {
      SolrFacetQuery query = getSolrQueryGenerationService().generateSolrFacetQuery(buildFacetQueryInput());
      System.out.println(query);
   }

   @Test
   public void testGenerateSolrConditionQuery () throws SolrException {
      String conditionQuery = getSolrQueryGenerationService().generateSolrConditionQuery(buildConditionEntities());
      System.out.println(conditionQuery);
   }

}
