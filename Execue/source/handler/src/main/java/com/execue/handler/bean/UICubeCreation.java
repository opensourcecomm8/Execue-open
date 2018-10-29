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


package com.execue.handler.bean;

import java.util.ArrayList;
import java.util.List;

import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.Range;

public class UICubeCreation {

   private Asset         baseAsset;
   private Asset         targetAsset;
   private List<Concept> selectedConcepts = new ArrayList<Concept>();
   private List<Range>   selectedRanges   = new ArrayList<Range>();
   private Long          modelId;

   private List<String>  simpleLookupDimensionsForUI;
   private List<String>  rangeLookupDimensionsForUI;                  ;

   public Long getModelId () {
      return modelId;
   }

   public void setModelId (Long modelId) {
      this.modelId = modelId;
   }

   public Asset getBaseAsset () {
      return baseAsset;
   }

   public void setBaseAsset (Asset baseAsset) {
      this.baseAsset = baseAsset;
   }

   public List<Concept> getSelectedConcepts () {
      return selectedConcepts;
   }

   public void setSelectedConcepts (List<Concept> selectedConcepts) {
      this.selectedConcepts = selectedConcepts;
   }

   public List<Range> getSelectedRanges () {
      return selectedRanges;
   }

   public void setSelectedRanges (List<Range> selectedRanges) {
      this.selectedRanges = selectedRanges;
   }

   public void addSelectedRange (Range selectedRange) {
      this.selectedRanges.add(selectedRange);
   }

   public void addSelectedConcept (Concept selectedConcept) {
      this.selectedConcepts.add(selectedConcept);
   }

   public void deleteSelectedRange (int rangeIndex) {
      this.selectedRanges.remove(rangeIndex);
   }

   public void deleteSelectedConcept (int conceptIndex) {
      this.selectedConcepts.remove(conceptIndex);
   }

   public Asset getTargetAsset () {
      return targetAsset;
   }

   public void setTargetAsset (Asset targetAsset) {
      this.targetAsset = targetAsset;
   }

   /**
    * @return the simpleLookupDimensionsForUI
    */
   public List<String> getSimpleLookupDimensionsForUI () {
      return simpleLookupDimensionsForUI;
   }

   /**
    * @param simpleLookupDimensionsForUI
    *           the simpleLookupDimensionsForUI to set
    */
   public void setSimpleLookupDimensionsForUI (List<String> simpleLookupDimensionsForUI) {
      this.simpleLookupDimensionsForUI = simpleLookupDimensionsForUI;
   }

   /**
    * @return the rangeLookupDimensionsForUI
    */
   public List<String> getRangeLookupDimensionsForUI () {
      return rangeLookupDimensionsForUI;
   }

   /**
    * @param rangeLookupDimensionsForUI
    *           the rangeLookupDimensionsForUI to set
    */
   public void setRangeLookupDimensionsForUI (List<String> rangeLookupDimensionsForUI) {
      this.rangeLookupDimensionsForUI = rangeLookupDimensionsForUI;
   }

}
