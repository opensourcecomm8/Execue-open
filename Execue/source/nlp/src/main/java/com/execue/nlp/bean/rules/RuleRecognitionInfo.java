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


package com.execue.nlp.bean.rules;

import java.util.List;

import com.execue.core.common.bean.algorithm.BaseBean;

public class RuleRecognitionInfo extends BaseBean {

   /**
    *
    */
   private static final long               serialVersionUID = 3554417172835063229L;

   private Long                            recognitionConceptID;
   private String                          recognitionConcept;
   private String                          recognitionGroup;
   private String                          recognitionNLPTag;
   private String                          recognitionGroupSuffix;
   private RuleRecognitionConceptReference recognitionConceptReference;
   private RuleRecognitionValue            recognitionValue;
   private List<String>                    resultIds;
   private List<String>                    recognitionCandidates;
   private String                          recognitionValueReference;

   public String getRecognitionGroup () {
      return recognitionGroup;
   }

   public void setRecognitionGroup (String recognitionGroup) {
      this.recognitionGroup = recognitionGroup;
   }

   public Long getRecognitionConceptID() {
      return recognitionConceptID;
   }

   public void setRecognitionConceptID(Long recognitionConceptID) {
      this.recognitionConceptID = recognitionConceptID;
   }

   public String getRecognitionConcept () {
      return recognitionConcept;
   }

   public void setRecognitionConcept (String recognitionConcept) {
      this.recognitionConcept = recognitionConcept;
   }

   public String getRecognitionNLPTag () {
      return recognitionNLPTag;
   }

   public void setRecognitionNLPTag (String recognitionNLPTag) {
      this.recognitionNLPTag = recognitionNLPTag;
   }

   public String getRecognitionGroupSuffix () {
      return recognitionGroupSuffix;
   }

   public void setRecognitionGroupSuffix (String recognitionGroupSuffix) {
      this.recognitionGroupSuffix = recognitionGroupSuffix;
   }

   public RuleRecognitionConceptReference getRecognitionConceptReference () {
      return recognitionConceptReference;
   }

   public void setRecognitionConceptReference (RuleRecognitionConceptReference recognitionConceptReference) {
      this.recognitionConceptReference = recognitionConceptReference;
   }

   public RuleRecognitionValue getRecognitionValue () {
      return recognitionValue;
   }

   public void setRecognitionValue (RuleRecognitionValue recognitionValue) {
      this.recognitionValue = recognitionValue;
   }

   public static long getSerialVersionUID () {
      return serialVersionUID;
   }

   public List<String> getResultIds () {
      return resultIds;
   }

   public void setResultIds (List<String> resultIds) {
      this.resultIds = resultIds;
   }

   public List<String> getRecognitionCandidates () {
      return recognitionCandidates;
   }

   public void setRecognitionCandidates (List<String> recognitionCandidates) {
      this.recognitionCandidates = recognitionCandidates;
   }

   public String getRecognitionValueReference () {
      return recognitionValueReference;
   }

   public void setRecognitionValueReference (String recognitionValueReference) {
      this.recognitionValueReference = recognitionValueReference;
   }
}
