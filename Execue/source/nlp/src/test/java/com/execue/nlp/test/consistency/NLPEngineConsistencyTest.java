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


package com.execue.nlp.test.consistency;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.execue.core.common.bean.nlp.NLPInformation;
import com.execue.core.common.bean.nlp.SemanticPossibility;
import com.execue.nlp.engine.barcode.matrix.MatrixUtility;
import com.execue.nlp.test.NLPBaseTest;

public class NLPEngineConsistencyTest extends NLPBaseTest {

   private Logger              logger   = Logger.getLogger(NLPEngineConsistencyTest.class);

   private static final String NEW_LINE = System.getProperty("line.separator");

   @BeforeClass
   public static void setUp () {
      baseSetup();
   }

   @Test
   public void testBaseEngine () {
      NLPInformation information = null;
      String query = null;
      try {
         query = "net sales, net income, total assets";
         Map<Long, String> modelIdRFMap = new HashMap<Long, String>(1);
         for (int i = 0; i < 50; i++) {
            information = getNLPEngine().processQuery(query);
            logger.info("Reduced forms " + information.getReducedForms().size());
            if (logger.isInfoEnabled()) {
               MatrixUtility.displayReducedForms(information.getReducedForms());
            }
            for (SemanticPossibility reducedForm : information.getReducedForms().keySet()) {
               if (i == 0) {
                  modelIdRFMap.put(reducedForm.getModel().getId(), reducedForm.getCompareString());
               } else {
                  String prevRF = modelIdRFMap.get(reducedForm.getModel().getId());
                  String currentRF = reducedForm.getCompareString();
                  if (!prevRF.equals(currentRF)) {
                     Assert.fail("Test Faild as pref RF was " + prevRF + " and current RF is " + currentRF);
                  }
               }
            }
         }

      } catch (Exception e) {
         e.printStackTrace();
      }

      Assert.assertNotNull(information);
   }

   @AfterClass
   public static void tearDown () {
      baseTeardown();
   }
}
