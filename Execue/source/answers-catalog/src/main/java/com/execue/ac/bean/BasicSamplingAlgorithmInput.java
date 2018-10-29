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
 * This bean contains the input needed for sampling algorithm per island.
 * 
 * @author Vishay
 * @version 1.0
 * @since 11/01/2011
 */
public class BasicSamplingAlgorithmInput {

   private Long                  population;
   private List<MeasureStatInfo> measureStatInfo;

   public Long getPopulation () {
      return population;
   }

   public void setPopulation (Long population) {
      this.population = population;
   }

   public List<MeasureStatInfo> getMeasureStatInfo () {
      return measureStatInfo;
   }

   public void setMeasureStatInfo (List<MeasureStatInfo> measureStatInfo) {
      this.measureStatInfo = measureStatInfo;
   }

}
