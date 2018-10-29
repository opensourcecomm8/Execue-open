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


package com.execue.ac.bean;

/**
 * This bean contains the information needed for updating mart.
 * 
 * @author Vishay
 * @version 1.0
 * @since 11/01/2011
 */
public class MartUpdationInputInfo {

   private MartUpdationContext                martUpdationContext;
   private MartUpdationPopulatedContext       martUpdationPopulatedContext;
   private AnswersCatalogConfigurationContext answersCatalogConfigurationContext;
   private MartConfigurationContext           martConfigurationContext;
   private MartInputParametersContext         martCreationInputParametersContext;
   private BasicSamplingAlgorithmStaticInput       samplingAlgorithmStaticInput;
   private SlicingAlgorithmStaticInput        slicingAlgorithmStaticInput;
   private BatchCountAlgorithmStaticInput     batchCountAlgorithmStaticInput;

   public BatchCountAlgorithmStaticInput getBatchCountAlgorithmStaticInput () {
      return batchCountAlgorithmStaticInput;
   }

   public void setBatchCountAlgorithmStaticInput (BatchCountAlgorithmStaticInput batchCountAlgorithmStaticInput) {
      this.batchCountAlgorithmStaticInput = batchCountAlgorithmStaticInput;
   }

   public MartConfigurationContext getMartConfigurationContext () {
      return martConfigurationContext;
   }

   public void setMartConfigurationContext (MartConfigurationContext martConfigurationContext) {
      this.martConfigurationContext = martConfigurationContext;
   }

   public BasicSamplingAlgorithmStaticInput getSamplingAlgorithmStaticInput () {
      return samplingAlgorithmStaticInput;
   }

   public void setSamplingAlgorithmStaticInput (BasicSamplingAlgorithmStaticInput samplingAlgorithmStaticInput) {
      this.samplingAlgorithmStaticInput = samplingAlgorithmStaticInput;
   }

   public MartInputParametersContext getMartCreationInputParametersContext () {
      return martCreationInputParametersContext;
   }

   public void setMartCreationInputParametersContext (MartInputParametersContext martCreationInputParametersContext) {
      this.martCreationInputParametersContext = martCreationInputParametersContext;
   }

   public AnswersCatalogConfigurationContext getAnswersCatalogConfigurationContext () {
      return answersCatalogConfigurationContext;
   }

   public void setAnswersCatalogConfigurationContext (
            AnswersCatalogConfigurationContext answersCatalogConfigurationContext) {
      this.answersCatalogConfigurationContext = answersCatalogConfigurationContext;
   }

   public SlicingAlgorithmStaticInput getSlicingAlgorithmStaticInput () {
      return slicingAlgorithmStaticInput;
   }

   public void setSlicingAlgorithmStaticInput (SlicingAlgorithmStaticInput slicingAlgorithmStaticInput) {
      this.slicingAlgorithmStaticInput = slicingAlgorithmStaticInput;
   }

   public MartUpdationContext getMartUpdationContext () {
      return martUpdationContext;
   }

   public void setMartUpdationContext (MartUpdationContext martUpdationContext) {
      this.martUpdationContext = martUpdationContext;
   }

   public MartUpdationPopulatedContext getMartUpdationPopulatedContext () {
      return martUpdationPopulatedContext;
   }

   public void setMartUpdationPopulatedContext (MartUpdationPopulatedContext martUpdationPopulatedContext) {
      this.martUpdationPopulatedContext = martUpdationPopulatedContext;
   }
}
