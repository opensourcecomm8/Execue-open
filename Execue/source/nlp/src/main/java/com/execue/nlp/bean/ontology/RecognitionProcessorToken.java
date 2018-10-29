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


package com.execue.nlp.bean.ontology;

import java.util.List;

import com.execue.nlp.bean.NLPToken;
import com.execue.nlp.bean.entity.ConceptEntity;
import com.execue.nlp.engine.barcode.token.TokenUtility;

public class RecognitionProcessorToken {

   protected NLPToken nlpToken;

   public RecognitionProcessorToken (NLPToken token) {
      this.nlpToken = token;
   }

   public NLPToken getNlpToken () {
      return nlpToken;
   }

   public String getWord () {
      return TokenUtility.getCurrentWordValue(this.nlpToken);
   }

   public String getConcept () {
      return this.nlpToken.getDefaultRecognitionEntity().getWord();
   }

   public Long getConceptID () {
      return ((ConceptEntity) this.nlpToken.getDefaultRecognitionEntity()).getConceptBedId();
   }

   public List<String> getPossibleWords () {
      return TokenUtility.getAllPossibleWordValues(this.nlpToken);
   }

   public int getPosition () {
      return this.nlpToken.getDefaultRecognitionEntity().getPosition();
   }

   public List<Integer> getOriginalPositions () {
      return this.nlpToken.getOriginalPositions();
   }

   public boolean containsRecognition () {
      return this.nlpToken.getRecognitionEntities() == null || this.nlpToken.getRecognitionEntities().isEmpty();
   }

   public double getQuality () {
      return nlpToken.getDefaultRecognitionEntity().getWeightInformation().getRecognitionQuality();
   }
}
