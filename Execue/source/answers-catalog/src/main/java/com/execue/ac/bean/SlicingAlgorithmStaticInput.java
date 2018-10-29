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

/**
 * This bean contains the input needed for slicing algorithm which is same for whole of the mart creation process.
 * 
 * @author Vishay
 * @version 1.0
 * @since 24/02/2011
 */
public class SlicingAlgorithmStaticInput {

   private List<String> slicingEligiblityCriteriaRecords;
   private List<String> slicingEligiblityCriteriaPercentages;
   private Integer      minSlices;
   private Integer      maxSlices;
   private Integer      avgSlices;

   public List<String> getSlicingEligiblityCriteriaRecords () {
      return slicingEligiblityCriteriaRecords;
   }

   public void setSlicingEligiblityCriteriaRecords (List<String> slicingEligiblityCriteriaRecords) {
      this.slicingEligiblityCriteriaRecords = slicingEligiblityCriteriaRecords;
   }

   public List<String> getSlicingEligiblityCriteriaPercentages () {
      return slicingEligiblityCriteriaPercentages;
   }

   public void setSlicingEligiblityCriteriaPercentages (List<String> slicingEligiblityCriteriaPercentages) {
      this.slicingEligiblityCriteriaPercentages = slicingEligiblityCriteriaPercentages;
   }

   public Integer getMinSlices () {
      return minSlices;
   }

   public void setMinSlices (Integer minSlices) {
      this.minSlices = minSlices;
   }

   public Integer getMaxSlices () {
      return maxSlices;
   }

   public void setMaxSlices (Integer maxSlices) {
      this.maxSlices = maxSlices;
   }

   public Integer getAvgSlices () {
      return avgSlices;
   }

   public void setAvgSlices (Integer avgSlices) {
      this.avgSlices = avgSlices;
   }

}
