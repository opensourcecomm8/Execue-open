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
package com.execue.swi.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.execue.core.ExeCueBaseTest;
import com.execue.core.common.bean.entity.Application;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.Instance;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.common.bean.nlp.NLPConstants;
import com.execue.core.common.bean.nlp.NLPInformation;
import com.execue.core.common.bean.nlp.SemanticPossibility;
import com.execue.core.common.bean.nlp.WordRecognitionState;
import com.execue.core.exception.ConfigurationException;
import com.execue.core.exception.nlp.NLPException;
import com.execue.core.exception.swi.KDXException;
import com.execue.util.StringUtilities;

/**
 * This class will test the basic entity recognition in NLP
 * 
 * @author Nitesh
 * @version 1.0
 * @since 13/10/09
 */
public class BasicEntityRecognitionTest extends ExeCueBaseTest {

   private static final String OUTPUT_FILE_CONCEPT_NAME              = "/apps/logs/unrecognized_concept_name.csv";

   private static final String OUTPUT_FILE_CONCEPT_DISPLAY_NAME      = "/apps/logs/unrecognized_concept_display_name.csv";

   private static final String OUTPUT_FILE_CONCEPT_RECOGNITION_TYPE  = "/apps/logs/concept_recognition_type.txt";

   private static final String OUTPUT_FILE_INSTANCE_NAME             = "/apps/logs/unrecognized_instance_name.csv";

   private static final String OUTPUT_FILE_INSTANCE_DISPLAY_NAME     = "/apps/logs/unrecognized_instance_display_name.csv";

   private static final String OUTPUT_FILE_INSTANCE_RECOGNITION_TYPE = "/apps/logs/instance_recognition_type.txt";

   private static final String QUERY_GET_INSTANCES                   = "from Instance";

   @Before
   public void setup () {
      baseTestSetup();
      try {
         getSWIConfiguration().doConfigure();
         getNLPConfiguration().doConfigure();
      } catch (ConfigurationException e) {
         Assert.fail("NLP Configuration Failed : " + e.getMessage());
      }
   }

   @After
   public void tearDown () {
      baseTestTeardown();
   }

   @Test
   public void testPerformEntityRecognitionTask () {

      // 1. Recognize all concepts
      performConceptsRecognition();

      // 2. Recognize all instances
      performInstancesRecognition();

   }

   @SuppressWarnings ("unchecked")
   private void performInstancesRecognition () {

      // Create the output file buffer
      BufferedWriter outputFileBufferInstance = null;
      BufferedWriter outputFileBufferInstanceDisplay = null;
      BufferedWriter outputFileBufferInstanceRecognitionType = null;

      try {
         outputFileBufferInstance = new BufferedWriter(new FileWriter(new File(OUTPUT_FILE_INSTANCE_NAME)));
         outputFileBufferInstanceDisplay = new BufferedWriter(new FileWriter(
                  new File(OUTPUT_FILE_INSTANCE_DISPLAY_NAME)));
         outputFileBufferInstanceRecognitionType = new BufferedWriter(new FileWriter(new File(
                  OUTPUT_FILE_INSTANCE_RECOGNITION_TYPE)));
      } catch (IOException e) {
         e.printStackTrace();
         Assert.fail("Failed to open the output file: " + e.getMessage());
      }

      List<Instance> instances = getHibernateTemplate().find(QUERY_GET_INSTANCES);
      String query = "";
      Instance testInstance = null;
      try {
         for (Instance instance : instances) {
            testInstance = instance;
            query = instance.getName();
            logUnrecognizedEntity(outputFileBufferInstance, outputFileBufferInstanceRecognitionType, query);
            if (!StringUtilities.matches(query, instance.getDisplayName())) {
               query = instance.getDisplayName();
               logUnrecognizedEntity(outputFileBufferInstanceDisplay, outputFileBufferInstanceRecognitionType, query);
            }
         }
      } catch (NLPException e) {
         System.out.println("Failed for instance " + testInstance.getName());
         //Assert.fail("Failed to process the query: " + query);
      } finally {
         // close the output file buffers
         try {
            outputFileBufferInstance.close();
            outputFileBufferInstanceDisplay.close();
            outputFileBufferInstanceRecognitionType.close();
         } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("Failed to close the output file: " + e.getMessage());
         }
      }
   }

   private void performConceptsRecognition () {

      BufferedWriter outputFileBufferConcepts = null;
      BufferedWriter outputFileBufferConceptsDisplay = null;
      BufferedWriter outputFileBufferConceptsRecongnitionType = null;

      try {
         outputFileBufferConcepts = new BufferedWriter(new FileWriter(new File(OUTPUT_FILE_CONCEPT_NAME)));
         outputFileBufferConceptsDisplay = new BufferedWriter(
                  new FileWriter(new File(OUTPUT_FILE_CONCEPT_DISPLAY_NAME)));
         outputFileBufferConceptsRecongnitionType = new BufferedWriter(new FileWriter(new File(
                  OUTPUT_FILE_CONCEPT_RECOGNITION_TYPE)));
      } catch (IOException e) {
         e.printStackTrace();
         Assert.fail("Failed to open the output file: " + e.getMessage());
      }
      List<Concept> concepts = null;
      String query = "";
      Concept testConcept = null;
      try {
         List<Application> applications = getApplicationService().getAllApplications();
         for (Application application : applications) {
            List<Model> modelsByApplicationId = getKDXRetrievalService().getModelsByApplicationId(application.getId());
            for (Model model : modelsByApplicationId) {
               concepts = getKDXRetrievalService().getConcepts(model.getId());
            }         
            for (Concept concept : concepts) {
               testConcept = concept;
               query = concept.getName();
               logUnrecognizedEntity(outputFileBufferConcepts, outputFileBufferConceptsRecongnitionType, query);
               if (!StringUtilities.matches(query, concept.getDisplayName())) {
                  query = concept.getDisplayName();
                  logUnrecognizedEntity(outputFileBufferConceptsDisplay, outputFileBufferConceptsRecongnitionType,
                           query);
               }
            }
         }
      } catch (KDXException e1) {
         System.out.println("Failed for concept " + testConcept.getName());
      } catch (NLPException e) {
         System.out.println("Failed for concept " + testConcept.getName());
//         Assert.fail("Failed to process the query: " + query);
      } finally {
         // close the output file buffers
         try {
            outputFileBufferConcepts.close();
            outputFileBufferConceptsDisplay.close();
            outputFileBufferConceptsRecongnitionType.close();
         } catch (IOException e) {
            System.out.println("Failed for concept " + testConcept.getName());
            //Assert.fail("Failed to close the output file: " + e.getMessage());
         }
      }
   }

   /**
    * @param outputFileBuffer
    * @param recognitionTypeFile
    * @param query
    * @throws NLPException
    * @throws IOException
    */
   private void logUnrecognizedEntity (BufferedWriter outputFileBuffer, BufferedWriter recognitionTypeFile, String query)
            throws NLPException {
      NLPInformation information;
      information = getNLPEngine().processQuery(query);
      try {
         if (!isRecognizedEntity(information.getReducedForms(), recognitionTypeFile, query)) {
            outputFileBuffer.write(query);
            outputFileBuffer.newLine();
         }
      } catch (IOException e) {
         e.printStackTrace();
         Assert.fail("Failed to recognized the query: " + query);
      }
   }

   public boolean isRecognizedEntity (Map<SemanticPossibility, Integer> reducedForms,
            BufferedWriter recognitionTypeFile, String query) throws IOException {
      boolean recognized = false;
      recognitionTypeFile.newLine();
      // System.out.print("\nQuery: " + query);
      recognitionTypeFile.write(query);
      for (Map.Entry<SemanticPossibility, Integer> entry : reducedForms.entrySet()) {
         Map<Integer, WordRecognitionState> recStates = entry.getKey().getWordRecognitionStates();
         for (Map.Entry<Integer, WordRecognitionState> recStateEntry : recStates.entrySet()) {
            String recType = recStateEntry.getValue().getRecognitionType();
            // System.out.print("\t\tRecognitionType: " + recType);
            recognitionTypeFile.write("\t" + recType);
            if (NLPConstants.NLP_TAG_ONTO_CONCEPT.equals(recType) || NLPConstants.NLP_TAG_ONTO_INSTANCE.equals(recType)
                     || NLPConstants.NLP_TAG_ONTO_INSTANCE_STAT.equals(recType)) {
               return true;
            }
         }
      }
      return recognized;
   }

}
