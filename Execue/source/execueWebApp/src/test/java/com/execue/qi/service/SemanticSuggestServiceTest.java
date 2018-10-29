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


package com.execue.qi.service;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.execue.core.ExeCueBaseTest;
import com.execue.core.common.bean.SemanticSuggestTerm;
import com.execue.core.exception.qi.QIException;
import com.execue.core.util.ExeCueUtils;

public class SemanticSuggestServiceTest extends ExeCueBaseTest {

   @BeforeClass
   public static void setup () {
      baseTestSetup();
   }

   @Test
   public void getSuggestTermsTest () {
      String searchString = "MidWest";
      try {
         List<SemanticSuggestTerm> terms = getSemanticSuggestService().getSuggestTerms(searchString, null);
         if (!ExeCueUtils.isCollectionEmpty(terms)) {
            System.out.println("Found : " + terms.size());
            // for (SuggestTerm term : terms) {
            // System.out.println(term.getName() + " : " + term.getDisplayName() + " : " + term.getType().getValue());
            // }
         } else {
            System.out.println("No suggestions found!!");
         }
      } catch (QIException e) {
         e.printStackTrace();
      }
   }

   @AfterClass
   public static void teardown () {
      baseTestTeardown();
   }
}