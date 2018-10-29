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


/**
 * 
 */
package com.execue.nlp.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.execue.core.exception.swi.KDXException;

/**
 * @author Nihar
 */
public class SFLTermHitsUpdatHelperTest extends NLPBaseTest {

   /**
    * @throws java.lang.Exception
    */
   @BeforeClass
   public static void setUp () {
      baseSetup();
   }

   @Test
   public void testUpdateSFLTokenWeights () {
      String[] conjunctions = { "OF", "ON", "AT", "WITH", "FROM", "FOR", "AFTER", "THOUGH", "BEFORE", "UNTIL", "OR",
               "UNLESS", "SINCE", "IN", "BASED", "BY", "AND", "TO", "THROUGH", "A", "AN", "THE" };
      Map<String, Double> wordWeightMap = new HashMap<String, Double>();
      for (String word : conjunctions) {
         wordWeightMap.put(word, 5.0);
      }
      try {
         getSFLTermsHitsUpdateHelper().updateSFLTokenWeights(wordWeightMap);
      } catch (KDXException e) {
         e.printStackTrace();
         Assert.fail(e.getMessage());
      }
   }

}
