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


package com.execue.nlp.engine.barcode.matrix;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.execue.core.common.bean.nlp.RecognitionEntityType;
import com.execue.core.common.bean.nlp.NLPConstants;
import com.execue.core.common.bean.nlp.SemanticPossibility;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.nlp.bean.NLPToken;
import com.execue.nlp.bean.entity.EntityFactory;
import com.execue.nlp.bean.entity.RecognitionEntity;
import com.execue.nlp.bean.matrix.Iteration;
import com.execue.nlp.bean.matrix.Matrix;
import com.execue.nlp.bean.matrix.Possibility;
import com.execue.nlp.bean.matrix.RootMatrix;
import com.execue.nlp.bean.matrix.Summary;
import com.execue.nlp.util.NLPUtilities;
import com.thoughtworks.xstream.XStream;

/**
 * Utility method for Matrix creation These method should not be call directly. These should be called from each
 * processing logic class
 * 
 * @author kaliki
 */

public class MatrixUtility {

   private static Logger logger = Logger.getLogger(MatrixUtility.class);

   public static RootMatrix createBaseRootMatrix (List<NLPToken> nlpTokens) throws CloneNotSupportedException {
      RootMatrix rootMatrix = new RootMatrix();
      Summary summary = createSummaryFromUserQuery(nlpTokens);

      Possibility possibility = createPossibility(summary, rootMatrix.getNextPossibilityId());

      rootMatrix.addPossibility(possibility);
      return rootMatrix;
   }

   public static Possibility createPossibility (Summary input, int possibilityId) {
      Possibility possibility = new Possibility(possibilityId);
      possibility.setStatus(Possibility.CREATED);
      Iteration iteration = createIteration(input, possibility.getNextIterationId());
      possibility.addIteration(iteration);

      return possibility;
   }

   public static Iteration createIteration (Summary summary, int iterationId) {
      Iteration iteration = null;
      try {
         Summary newSummary = (Summary) summary.clone();
         iteration = new Iteration(iterationId, newSummary);
         iteration.setMatrix(createMatrix(newSummary));
      } catch (CloneNotSupportedException e) {
         // TODO: handle exception
      }
      return iteration;
   }

   public static Matrix createMatrix (Summary summary) {
      List<NLPToken> tokens = summary.getNLPTokens();
      Matrix matrix = new Matrix();
      for (NLPToken token : tokens) {
         try {
            NLPToken newToken = (NLPToken) token.clone();
            if (newToken.getRecognitionEntities() != null) {
               newToken.clearRecognitionEntities();
               newToken.clearAssociationEntities();
            }
            matrix.addNLPToken(newToken);
         } catch (CloneNotSupportedException e) {
            e.printStackTrace();
         }
      }
      return matrix;
   }

   public static boolean isSummaryEquals (Summary summary1, Summary summary2) {
      boolean retValue = false;
      if (summary1.getNLPTokens().size() != summary2.getNLPTokens().size()) {
         return false;
      }
      List<NLPToken> tokens1 = summary1.getNLPTokens();
      List<NLPToken> tokens2 = summary2.getNLPTokens();

      for (int i = 0; i < tokens1.size(); i++) {
         NLPToken token1 = tokens1.get(i);
         NLPToken token2 = tokens2.get(i);
         List<RecognitionEntity> entities1 = token1.getRecognitionEntities();
         List<RecognitionEntity> entities2 = token2.getRecognitionEntities();
         retValue = entities1 != null && entities1.containsAll(entities2);
         if (!retValue) {
            return retValue;
         }
      }

      return retValue;
   }

   public static Summary createSummaryFromUserQuery (List<NLPToken> nlpTokens) throws CloneNotSupportedException {
      List<NLPToken> nlpTList = new ArrayList<NLPToken>();

      for (NLPToken token : nlpTokens) {
         // TODO See if necessary
         NLPToken nlpToken = (NLPToken) token.clone();
         nlpTList.add(nlpToken);
      }

      Summary summary = new Summary();
      summary.setNLPTokens(nlpTList);

      return summary;
   }

   public static void printRootMatrixAsXML (RootMatrix rootMatrix) {
      if (logger.isInfoEnabled()) {
         XStream xStream = new XStream();
         String xml = xStream.toXML(rootMatrix);
         logger.info(xml);
      }
   }

   /**
    * Separates User String into an Array of TaggedWords
    * 
    * @param taggedQuerySentence
    * @return
    */
   public static List<NLPToken> unmarshalNameValuePairs (String taggedQuerySentence) {
      List<NLPToken> tokens = new ArrayList<NLPToken>();
      if (StringUtils.isBlank(taggedQuerySentence)) {
         // TODO: throw exception
      }
      StringTokenizer queryTokens = new StringTokenizer(taggedQuerySentence, " ", false);
      NLPToken token = null;
      String currentNameValuePair = null;
      String splitter = "/";
      String name = null;
      String value = null;
      int tokenPosition = 0;
      while (queryTokens.hasMoreTokens()) {
         currentNameValuePair = queryTokens.nextToken();
         splitter = "/";
         if (currentNameValuePair.indexOf("@") != -1) {
            splitter = "@";
         }
         String nameValueArray[] = currentNameValuePair.split(splitter);
         // nameValuePairTokens = new StringTokenizer(currentNameValuePair, splitter, false);
         if (nameValueArray.length == 1) {
            // TODO: throw an exception
            value = nameValueArray[0];
            name = "/";
         }
         if (nameValueArray.length == 2) {
            name = nameValueArray[0];
            value = nameValueArray[1];
         } else if (nameValueArray.length > 2) {
            name = nameValueArray[0];
            for (int i = 1; i < nameValueArray.length - 1; i++) {
               name = name + splitter + nameValueArray[i];
            }
            value = nameValueArray[nameValueArray.length - 1];
         } else {
            continue;
         }

         // Check if value is cardinal number, then validate. If validation does not succeeds then make the value as
         // NOUN
         if (value.equals(NLPConstants.NLP_TAG_POS_CARDINAL_NUMBER)) {
            if (!ExecueCoreUtil.isCardinalNumber(name)) {
               value = NLPConstants.NLP_TAG_POS_NOUN;
            }
         }

         RecognitionEntity recoEntity = EntityFactory.getRecognitionEntity(RecognitionEntityType.GENERAL_ENTITY, name, value,
                  null, tokenPosition);
         recoEntity.setWeightInformation(NLPUtilities.getDefaultWeightInformation());
         recoEntity.addReferedTokenPosition(tokenPosition);
         token = new NLPToken(name, tokenPosition);
         token.addRecognitionEntity(recoEntity);
         tokens.add(token);
         tokenPosition++;
      }
      return tokens;
   }

   public static void displayReducedForms (Map<SemanticPossibility, Integer> reducedForms) {
      for (Map.Entry<SemanticPossibility, Integer> entry : reducedForms.entrySet()) {
         logger.info("\nPossibility " + entry.getValue() + ".\n" + entry.getKey().getDisplayString() + "\n"
                  + "Possibility Recognition Weight "
                  + entry.getKey().getWeightInformationForExplicitEntities().getFinalWeight()
                  + " Possibility Association Weight "
                  + entry.getKey().getWeightInformationForAssociations().getFinalWeight()
                  + " Possibility Framework Weight "
                  + entry.getKey().getWeightInformationForFrmeworks().getFinalWeight());
      }
   }
}