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


package com.execue.content.preprocessor.service.impl;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.execue.content.preprocessor.exception.ContentPreProcessorException;
import com.execue.content.preprocessor.service.ContentPreprocessorCommonBaseTest;

public class ContentCleanupPatternProcessorTest extends ContentPreprocessorCommonBaseTest {

   @BeforeClass
   public static void setUp () throws Exception {

      baseSetup();
   }

   @Test
   public void testContentCleanupPatternProcessor () {
      String testCase = "G\\oogle.com";
      Long appId = -1L;
      String result = null;
      try {
         result = getContentCleanupPatternProcessor().processContent(testCase, appId);
         System.out.println("result: " + result);
      } catch (ContentPreProcessorException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   @AfterClass
   public static void teardown () {
      baseTearDown();
   }

}
