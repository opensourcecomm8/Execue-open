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

import java.util.List;

import com.execue.core.common.bean.entity.Asset;

/**
 * This class represents the mapping information in perspective to source asset.
 * 
 * @author Vishay
 * @version 1.0
 * @since 11/01/2011
 */
public class SourceAssetMappingInfo {

   private Asset                      sourceAsset;
   private ConceptColumnMapping       populatedPopulation;
   private List<ConceptColumnMapping> populatedDistributions;
   private List<ConceptColumnMapping> populatedProminentMeasures;
   private List<ConceptColumnMapping> populatedProminentDimensions;

   public ConceptColumnMapping getPopulatedPopulation () {
      return populatedPopulation;
   }

   public void setPopulatedPopulation (ConceptColumnMapping populatedPopulation) {
      this.populatedPopulation = populatedPopulation;
   }

   public List<ConceptColumnMapping> getPopulatedDistributions () {
      return populatedDistributions;
   }

   public void setPopulatedDistributions (List<ConceptColumnMapping> populatedDistributions) {
      this.populatedDistributions = populatedDistributions;
   }

   public List<ConceptColumnMapping> getPopulatedProminentMeasures () {
      return populatedProminentMeasures;
   }

   public void setPopulatedProminentMeasures (List<ConceptColumnMapping> populatedProminentMeasures) {
      this.populatedProminentMeasures = populatedProminentMeasures;
   }

   public List<ConceptColumnMapping> getPopulatedProminentDimensions () {
      return populatedProminentDimensions;
   }

   public void setPopulatedProminentDimensions (List<ConceptColumnMapping> populatedProminentDimensions) {
      this.populatedProminentDimensions = populatedProminentDimensions;
   }

   public Asset getSourceAsset () {
      return sourceAsset;
   }

   public void setSourceAsset (Asset sourceAsset) {
      this.sourceAsset = sourceAsset;
   }
}
