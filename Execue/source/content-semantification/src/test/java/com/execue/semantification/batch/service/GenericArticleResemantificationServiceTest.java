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


package com.execue.semantification.batch.service;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.execue.core.exception.ConfigurationException;
import com.execue.semantification.ContentSemantificationCommonBaseTest;

public class GenericArticleResemantificationServiceTest extends ContentSemantificationCommonBaseTest {

   private static final Logger log = Logger.getLogger(GenericArticleResemantificationServiceTest.class);

   @BeforeClass
   public static void setup () throws ConfigurationException {
      baseSetup();
   }

   @AfterClass
   public static void tearDown () {
      baseTeardown();
   }

   @Test
   public void testGenericArticleSemantificationService () {
      Long contextId = 111L;
      Long userQueryId = 1001L;
      getGenericArticleResemantificationService().semantifiArticles(contextId, userQueryId);
   }

}
