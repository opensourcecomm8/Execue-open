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


package com.execue.nlp.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

import com.execue.core.common.bean.nlp.NLPInformation;
import com.execue.core.common.bean.nlp.SemanticPossibility;
import com.execue.nlp.engine.barcode.matrix.MatrixUtility;

/**
 * @author kaliki
 */
public class NLPEngineFromFileTest extends NLPCommonBaseTest {

   private Logger logger = Logger.getLogger(NLPEngineFromFileTest.class);

   @BeforeClass
   public static void setUp () {
      baseSetup();
   }

   @Test
   public void testNLPEngine () throws Exception {
      BufferedReader br = new BufferedReader(new FileReader("/apps/i2App/input.txt"));
      PrintWriter pw = new PrintWriter(new FileWriter("/apps/i2App/output/output.html"));
      String userInput = null;
      pw.println("<html><title>Test Results </title><body>");
      pw.println("<table border=\"1\"><tr><td>Query Name</td><td>Results</td></tr>");
      try {
         while ((userInput = br.readLine()) != null) {
            pw.println("<tr><td>");
            pw.write(userInput);
            pw.println("</td><td>");
            try {
               long start = System.currentTimeMillis();
               Map<SemanticPossibility, Integer> reducedForms = executeNLP(userInput);
               long end = System.currentTimeMillis();

               pw.print("<pre>");
               for (Map.Entry<SemanticPossibility, Integer> entry : reducedForms.entrySet()) {
                  pw.println("Possibility " + entry.getValue() + "<br>" + entry.getKey().getDisplayString() + "<br>");
               }
               pw.print("Time taken : " + (long) (end - start) + " msec</pre>");
            } catch (Exception e) {
               e.printStackTrace();
               pw.write("ERROR");
            }
            pw.write("</td></tr>");
         }

      } catch (Exception e) {
         e.printStackTrace();
      }
      pw.println("</table></body></html>");
      pw.flush();
      pw.close();
   }

   private Map<SemanticPossibility, Integer> executeNLP (String string) throws Exception {
      NLPInformation nlpInfo = getNLPEngine().processQuery(string);
      MatrixUtility.displayReducedForms(nlpInfo.getReducedForms());
      return nlpInfo.getReducedForms();
   }
}
