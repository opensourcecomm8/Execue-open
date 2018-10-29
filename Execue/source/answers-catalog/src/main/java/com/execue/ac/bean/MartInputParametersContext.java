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
 * This bean contains the input parameter values needed for mart creation
 * 
 * @author Vishay
 * @version 1.0
 * @since 13/01/2011
 */
public class MartInputParametersContext {

   private Long    totalPopulationRecordCount;
   private Integer populationMaxDataLength;
   private Integer numberOfSlices;

   public Long getTotalPopulationRecordCount () {
      return totalPopulationRecordCount;
   }

   public void setTotalPopulationRecordCount (Long totalPopulationRecordCount) {
      this.totalPopulationRecordCount = totalPopulationRecordCount;
   }

   public Integer getPopulationMaxDataLength () {
      return populationMaxDataLength;
   }

   public void setPopulationMaxDataLength (Integer populationMaxDataLength) {
      this.populationMaxDataLength = populationMaxDataLength;
   }

   public Integer getNumberOfSlices () {
      return numberOfSlices;
   }

   public void setNumberOfSlices (Integer numberOfSlices) {
      this.numberOfSlices = numberOfSlices;
   }

}
