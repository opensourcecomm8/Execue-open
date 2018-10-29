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
package com.execue.util.sfl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.execue.core.ExeCueBaseTest;
import com.execue.core.exception.ontology.OntologyException;

/**
 * @author Nitesh
 */
public class SnowFlakesGeneratorTest extends ExeCueBaseTest {

   @Before
   public void setup () {
      baseTestSetup();
   }

   @After
   public void tearDown () {
      baseTestTeardown();
   }

   @Test
   public void testGenerateSnowFlakes () throws OntologyException {
      try {
         String termsFileName = "SFLTerm.txt", tokensFileName = "SFLTermToken.txt";
         File output = new File(termsFileName);
         BufferedWriter bufferTerms = new BufferedWriter(new FileWriter(output));
         File output2 = new File(tokensFileName);
         BufferedWriter bufferTokens = new BufferedWriter(new FileWriter(output2));

         // TODO: Commenting out for government spending app snow flakes generation as discussed with kaliki
         // add the special chars
         // specialChars.add(".");
         // specialChars.add(",");
         // specialChars.add(";");
         // specialChars.add("-");
         // specialChars.add("_");
         // specialChars.add("(");
         // specialChars.add(")");
         // specialChars.add("*");
         // specialChars.add("/");
         // specialChars.add("#");
         // specialChars.add("+");
         // specialChars.add("&");
         // specialChars.add("'");

         long modelId = 101L;
         int startIndex = 1000;
         String ids = SnowflakesGenerator.generateSnowFlakesForConcepts(bufferTerms, bufferTokens, modelId, startIndex);
         System.out.println(ids);
         ids = SnowflakesGenerator.generateSnowFlakesForRelations(bufferTerms, bufferTokens, modelId, startIndex,
                  Integer.parseInt(ids.split(",")[0]), Integer.parseInt(ids.split(",")[1]));
         System.out.println(ids);
         SnowflakesGenerator.generateSnowFlakesForInstanceDescriptionsAndDisplayNames(bufferTerms, bufferTokens,
                  modelId, startIndex, Integer.parseInt(ids.split(",")[0]), Integer.parseInt(ids.split(",")[1]));

         bufferTerms.close();
         bufferTokens.close();
      } catch (SQLException e) {
         e.printStackTrace();
         Assert.fail("Failed to genarate the SFL Terms into the database: " + e.getMessage());
      } catch (ClassNotFoundException e) {
         e.printStackTrace();
         Assert.fail("Failed to genarate the SFL Terms into the database: " + e.getMessage());
      } catch (IOException e) {
         e.printStackTrace();
         Assert.fail("Failed to genarate the SFL Terms into the database: " + e.getMessage());
      }
   }
}