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


package com.execue.ac.algorithm.optimaldset.bean;

import java.util.List;

/**
 * @author Vishay
 * @version 1.0
 * @since 18/02/2012
 */
public class OptimalDSetMartOutputInfo {

   private List<OptimalDSetDimensionInfo> outputDimensions;
   private List<OptimalDSetMeasureInfo>   outputMeasures;

   public List<OptimalDSetDimensionInfo> getOutputDimensions () {
      return outputDimensions;
   }

   public void setOutputDimensions (List<OptimalDSetDimensionInfo> outputDimensions) {
      this.outputDimensions = outputDimensions;
   }

   public List<OptimalDSetMeasureInfo> getOutputMeasures () {
      return outputMeasures;
   }

   public void setOutputMeasures (List<OptimalDSetMeasureInfo> outputMeasures) {
      this.outputMeasures = outputMeasures;
   }

   public OptimalDSetMartOutputInfo (List<OptimalDSetDimensionInfo> outputDimensions,
            List<OptimalDSetMeasureInfo> outputMeasures) {
      super();
      this.outputDimensions = outputDimensions;
      this.outputMeasures = outputMeasures;
   }
}
