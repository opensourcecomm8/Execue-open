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


package com.execue.querygen.service;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.execue.core.ExeCueBaseTest;

public class QueryGenerationServiceTest extends ExeCueBaseTest {

   private static final Logger log = Logger.getLogger(QueryGenerationServiceTest.class);

   @BeforeClass
   public static void setup () {
      ApplicationContext context = new ClassPathXmlApplicationContext(new String[] {
               "/bean-config/execue-configuration.xml", "/bean-config/spring-hibernate.xml",
               "/bean-config/execue-dataaccess.xml", "/bean-config/execue-query-generation.xml",
               "/bean-config/execue-swi.xml" });
      setContext(context);
   }

   @AfterClass
   public static void teardown () {
      baseTestTeardown();
   }

   /**
    * This method should not becoming into this test class. But it depends on some modules which are not a compile time dependency for query generation module.<br/> More over this
    * is a test case on a private method. <br/> once unit tested, remove this test case or comment it out
    */
   // @Test
   /*
    * public void testApplyJoins () { List<QueryTable> fromTables = new ArrayList<QueryTable>(); fromTables.add(QueryGenerationSeedProvider.getQueryTable("CARD_TYPE", "CDT"));
    * fromTables.add(QueryGenerationSeedProvider.getQueryTable("ACCOUNT2", "ACT")); Asset targetAsset = QueryGenerationSeedProvider.getTargetAsset(new Long(11)); Query query =
    * QueryGenerationSeedProvider.getTestQuery(fromTables); try { getMySqlQueryGenerationService().applyJoins(targetAsset, query); } catch (SQLGenerationException
    * sqlGenerationException) { Assert.fail("applyJoins failed : " + sqlGenerationException.getClass() + " - " + sqlGenerationException.getMessage()); } if (log.isDebugEnabled()) {
    * log.debug("Number of Join Entities Applied : " + query.getJoinEntities().size()); } }
    */
}
