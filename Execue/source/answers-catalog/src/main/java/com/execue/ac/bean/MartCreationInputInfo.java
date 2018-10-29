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

import com.execue.core.common.bean.ac.MartCreationContext;

/**
 * This bean contains the information needed for creating mart.
 * 
 * @author Vishay
 * @version 1.0
 * @since 11/01/2011
 */
public class MartCreationInputInfo {

   private MartCreationContext                martCreationContext;
   private MartCreationPopulatedContext       martCreationPopulatedContext;
   private AnswersCatalogConfigurationContext answersCatalogConfigurationContext;
   private MartConfigurationContext           martConfigurationContext;
   private MartInputParametersContext         martCreationInputParametersContext;
   private BasicSamplingAlgorithmStaticInput  basicSamplingAlgorithmStaticInput;
   private SamplingAlgorithmStaticInput       samplingAlgorithmStaticInput;
   private SlicingAlgorithmStaticInput        slicingAlgorithmStaticInput;
   private BatchCountAlgorithmStaticInput     batchCountAlgorithmStaticInput;
   private boolean                            useBasicSamplingAlgorithm;

   public BatchCountAlgorithmStaticInput getBatchCountAlgorithmStaticInput () {
      return batchCountAlgorithmStaticInput;
   }

   public void setBatchCountAlgorithmStaticInput (BatchCountAlgorithmStaticInput batchCountAlgorithmStaticInput) {
      this.batchCountAlgorithmStaticInput = batchCountAlgorithmStaticInput;
   }

   public MartCreationContext getMartCreationContext () {
      return martCreationContext;
   }

   public void setMartCreationContext (MartCreationContext martCreationContext) {
      this.martCreationContext = martCreationContext;
   }

   public MartCreationPopulatedContext getMartCreationPopulatedContext () {
      return martCreationPopulatedContext;
   }

   public void setMartCreationPopulatedContext (MartCreationPopulatedContext martCreationPopulatedContext) {
      this.martCreationPopulatedContext = martCreationPopulatedContext;
   }

   public MartConfigurationContext getMartConfigurationContext () {
      return martConfigurationContext;
   }

   public void setMartConfigurationContext (MartConfigurationContext martConfigurationContext) {
      this.martConfigurationContext = martConfigurationContext;
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

   public BasicSamplingAlgorithmStaticInput getBasicSamplingAlgorithmStaticInput () {
      return basicSamplingAlgorithmStaticInput;
   }

   public void setBasicSamplingAlgorithmStaticInput (BasicSamplingAlgorithmStaticInput basicSamplingAlgorithmStaticInput) {
      this.basicSamplingAlgorithmStaticInput = basicSamplingAlgorithmStaticInput;
   }

   public SamplingAlgorithmStaticInput getSamplingAlgorithmStaticInput () {
      return samplingAlgorithmStaticInput;
   }

   public void setSamplingAlgorithmStaticInput (SamplingAlgorithmStaticInput samplingAlgorithmStaticInput) {
      this.samplingAlgorithmStaticInput = samplingAlgorithmStaticInput;
   }

   public boolean isUseBasicSamplingAlgorithm () {
      return useBasicSamplingAlgorithm;
   }

   public void setUseBasicSamplingAlgorithm (boolean useBasicSamplingAlgorithm) {
      this.useBasicSamplingAlgorithm = useBasicSamplingAlgorithm;
   }
}
