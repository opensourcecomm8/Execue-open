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
package com.execue.core.common.bean;

/**
 * Class to contain the weight information across the application.
 * 
 * @author Nihar Agrawal
 */
public class WeightInformation implements Cloneable {

   private double recognitionQuality;
   private double recognitionWeight;
   private double peanalty;
   /**
    * signifies the importance of the recognition as per the user query. default would be 1 but for the implicit
    * recognition this can be changed.
    */
   private double importance = 1;

   /**
    * @return the recognitionQuality
    */
   public double getRecognitionQuality () {
      return recognitionQuality * importance;
   }

   /**
    * @param recognitionQuality
    *           the recognitionQuality to set
    */
   public void setRecognitionQuality (double recognitionQuality) {
      this.recognitionQuality = recognitionQuality;
   }

   /**
    * @return the recognitionWeight
    */
   public double getRecognitionWeight () {
      return recognitionWeight;
   }

   /**
    * @param recognitionWeight
    *           the recognitionWeight to set
    */
   public void setRecognitionWeight (double recognitionWeight) {
      this.recognitionWeight = recognitionWeight;
   }

   /**
    * TODO - NA- write proper comments towards cost effectiveness.
    * 
    * @return the finalWeight
    */
   public double getFinalWeight () {
      double finalWeight = getRecognitionQuality() * recognitionWeight;
      finalWeight = finalWeight - peanalty;
      return finalWeight;
   }

   /**
    * @param importance
    *           the importance to set
    */
   public void setImportance (double importance) {
      this.importance = importance;
   }

   /**
    * @return the peanalty
    */
   public double getPeanalty () {
      return peanalty;
   }

   /**
    * @param peanalty
    *           the peanalty to set
    */
   public void setPeanalty (double peanalty) {
      this.peanalty = peanalty;
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#clone()
    */
   @Override
   public WeightInformation clone () throws CloneNotSupportedException {
      WeightInformation weightInformation = (WeightInformation) super.clone();
      weightInformation.setRecognitionQuality(recognitionQuality);
      weightInformation.setRecognitionWeight(recognitionWeight);
      weightInformation.setImportance(importance);
      weightInformation.setPeanalty(peanalty);
      return weightInformation;
   }

}
