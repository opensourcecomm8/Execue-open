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
package com.execue.core.common.bean.qdata;

import java.util.List;

/**
 * @author Nitesh
 *
 */
public class QueryHistoryDimensionPatternInfo {

   private Double                   usagePercentage;
   private List<OptimalDSetSWIInfo> optimalDSetSwiInfos;

   /**
    * @return the usagePercentage
    */
   public Double getUsagePercentage () {
      return usagePercentage;
   }

   /**
    * @param usagePercentage the usagePercentage to set
    */
   public void setUsagePercentage (Double usagePercentage) {
      this.usagePercentage = usagePercentage;
   }

   /**
    * @return the optimalDSetSwiInfos
    */
   public List<OptimalDSetSWIInfo> getOptimalDSetSwiInfos () {
      return optimalDSetSwiInfos;
   }

   /**
    * @param optimalDSetSwiInfos the optimalDSetSwiInfos to set
    */
   public void setOptimalDSetSwiInfos (List<OptimalDSetSWIInfo> optimalDSetSwiInfos) {
      this.optimalDSetSwiInfos = optimalDSetSwiInfos;
   }

}
