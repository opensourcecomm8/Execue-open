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
package com.execue.core.common.bean.nlp;

/**
 * @author Abhijit
 * @since June 11, 2009
 */
public class WordRecognitionState {

   private String  recognitionType;
   private String  conjunctionType;
   private String  posType;
   private String  conceptName;
   private String  instanceName;
   private boolean isAssociated;
   private boolean isBaseConcept;
   private boolean isValidPosType;

   public boolean isBaseConcept () {
      return isBaseConcept;
   }

   public void setBaseConcept (boolean isBaseConcept) {
      this.isBaseConcept = isBaseConcept;
   }

   /**
    * @return the recognitionType
    */
   public String getRecognitionType () {
      return recognitionType;
   }

   /**
    * @param recognitionType
    *           the recognitionType to set
    */
   public void setRecognitionType (String recognitionType) {
      this.recognitionType = recognitionType;
   }

   /**
    * @return the conjunctionType
    */
   public String getConjunctionType () {
      return conjunctionType;
   }

   /**
    * @param conjunctionType
    *           the conjunctionType to set
    */
   public void setConjunctionType (String conjunctionType) {
      this.conjunctionType = conjunctionType;
   }

   /**
    * @return the posType
    */
   public String getPosType () {
      return posType;
   }

   /**
    * @param posType
    *           the posType to set
    */
   public void setPosType (String posType) {
      this.posType = posType;
   }

   /**
    * @return the isAssociated
    */
   public boolean isAssociated () {
      return isAssociated;
   }

   /**
    * @param isAssociated
    *           the isAssociated to set
    */
   public void setAssociated (boolean isAssociated) {
      this.isAssociated = isAssociated;
   }

   public String getConceptName () {
      return conceptName;
   }

   public void setConceptName (String conceptName) {
      this.conceptName = conceptName;
   }

   public String getInstanceName () {
      return instanceName;
   }

   public void setInstanceName (String instanceName) {
      this.instanceName = instanceName;
   }

   public boolean isValidPosType () {
      return isValidPosType;
   }

   public void setValidPosType (boolean isValidPosType) {
      this.isValidPosType = isValidPosType;
   }
}
