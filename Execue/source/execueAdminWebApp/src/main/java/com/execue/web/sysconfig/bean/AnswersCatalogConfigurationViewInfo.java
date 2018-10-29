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


package com.execue.web.sysconfig.bean;

import java.io.Serializable;
import java.util.List;

public class AnswersCatalogConfigurationViewInfo implements Serializable {

   private List<ConfigurationProperty> readOnlyConfigurations;

   private ConfigurationProperty       samplingAlgoErroRate;
   private ConfigurationProperty       samplingAlgoConfidenceLevel;
   private ConfigurationProperty       samplingAlgoMinPopulation;
   private ConfigurationProperty       samplingAlgoMaxPopulation;

   private ConfigurationProperty       martMaxDimensions;
   private ConfigurationProperty       martMaxMeasures;

   private ConfigurationProperty       martSourceSameAsTarget;
   private ConfigurationProperty       martUseBasicAlgo;

   private ConfigurationProperty       cubeSourceSameAsTarget;

   private ConfigurationProperty       optCubeMinUsage;
   private ConfigurationProperty       optCubeMaxSpace;
   private ConfigurationProperty       optApplyConstraints;
   private ConfigurationProperty       optSpaceAtRuntime;
   private ConfigurationProperty       optParentAssetSpace;
   private ConfigurationProperty       optNumberOfParentAssetMeasures;
   private ConfigurationProperty       optLookupValueColumnLengthAtParentAsset;

   public List<ConfigurationProperty> getReadOnlyConfigurations () {
      return readOnlyConfigurations;
   }

   public void setReadOnlyConfigurations (List<ConfigurationProperty> readOnlyConfigurations) {
      this.readOnlyConfigurations = readOnlyConfigurations;
   }

   public ConfigurationProperty getSamplingAlgoErroRate () {
      return samplingAlgoErroRate;
   }

   public void setSamplingAlgoErroRate (ConfigurationProperty samplingAlgoErroRate) {
      this.samplingAlgoErroRate = samplingAlgoErroRate;
   }

   public ConfigurationProperty getSamplingAlgoConfidenceLevel () {
      return samplingAlgoConfidenceLevel;
   }

   public void setSamplingAlgoConfidenceLevel (ConfigurationProperty samplingAlgoConfidenceLevel) {
      this.samplingAlgoConfidenceLevel = samplingAlgoConfidenceLevel;
   }

   public ConfigurationProperty getSamplingAlgoMinPopulation () {
      return samplingAlgoMinPopulation;
   }

   public void setSamplingAlgoMinPopulation (ConfigurationProperty samplingAlgoMinPopulation) {
      this.samplingAlgoMinPopulation = samplingAlgoMinPopulation;
   }

   public ConfigurationProperty getSamplingAlgoMaxPopulation () {
      return samplingAlgoMaxPopulation;
   }

   public void setSamplingAlgoMaxPopulation (ConfigurationProperty samplingAlgoMaxPopulation) {
      this.samplingAlgoMaxPopulation = samplingAlgoMaxPopulation;
   }

   public ConfigurationProperty getMartMaxDimensions () {
      return martMaxDimensions;
   }

   public void setMartMaxDimensions (ConfigurationProperty martMaxDimensions) {
      this.martMaxDimensions = martMaxDimensions;
   }

   public ConfigurationProperty getMartMaxMeasures () {
      return martMaxMeasures;
   }

   public void setMartMaxMeasures (ConfigurationProperty martMaxMeasures) {
      this.martMaxMeasures = martMaxMeasures;
   }

   public ConfigurationProperty getMartSourceSameAsTarget () {
      return martSourceSameAsTarget;
   }

   public void setMartSourceSameAsTarget (ConfigurationProperty martSourceSameAsTarget) {
      this.martSourceSameAsTarget = martSourceSameAsTarget;
   }

   public ConfigurationProperty getMartUseBasicAlgo () {
      return martUseBasicAlgo;
   }

   public void setMartUseBasicAlgo (ConfigurationProperty martUseBasicAlgo) {
      this.martUseBasicAlgo = martUseBasicAlgo;
   }

   public ConfigurationProperty getCubeSourceSameAsTarget () {
      return cubeSourceSameAsTarget;
   }

   public void setCubeSourceSameAsTarget (ConfigurationProperty cubeSourceSameAsTarget) {
      this.cubeSourceSameAsTarget = cubeSourceSameAsTarget;
   }

   public ConfigurationProperty getOptCubeMinUsage () {
      return optCubeMinUsage;
   }

   public void setOptCubeMinUsage (ConfigurationProperty optCubeMinUsage) {
      this.optCubeMinUsage = optCubeMinUsage;
   }

   public ConfigurationProperty getOptCubeMaxSpace () {
      return optCubeMaxSpace;
   }

   public void setOptCubeMaxSpace (ConfigurationProperty optCubeMaxSpace) {
      this.optCubeMaxSpace = optCubeMaxSpace;
   }

   public ConfigurationProperty getOptApplyConstraints () {
      return optApplyConstraints;
   }

   public void setOptApplyConstraints (ConfigurationProperty optApplyConstraints) {
      this.optApplyConstraints = optApplyConstraints;
   }

   public ConfigurationProperty getOptSpaceAtRuntime () {
      return optSpaceAtRuntime;
   }

   public void setOptSpaceAtRuntime (ConfigurationProperty optSpaceAtRuntime) {
      this.optSpaceAtRuntime = optSpaceAtRuntime;
   }

   public ConfigurationProperty getOptParentAssetSpace () {
      return optParentAssetSpace;
   }

   public void setOptParentAssetSpace (ConfigurationProperty optParentAssetSpace) {
      this.optParentAssetSpace = optParentAssetSpace;
   }

   public ConfigurationProperty getOptNumberOfParentAssetMeasures () {
      return optNumberOfParentAssetMeasures;
   }

   public void setOptNumberOfParentAssetMeasures (ConfigurationProperty optNumberOfParentAssetMeasures) {
      this.optNumberOfParentAssetMeasures = optNumberOfParentAssetMeasures;
   }

   public ConfigurationProperty getOptLookupValueColumnLengthAtParentAsset () {
      return optLookupValueColumnLengthAtParentAsset;
   }

   public void setOptLookupValueColumnLengthAtParentAsset (ConfigurationProperty optLookupValueColumnLengthAtParentAsset) {
      this.optLookupValueColumnLengthAtParentAsset = optLookupValueColumnLengthAtParentAsset;
   }

}
