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


package com.execue.core.common.bean.swi;

import java.io.Serializable;

public class ApplicationScopeInfo implements Serializable {

   private Long   appId;
   private Double tokenWeightsSum;
   private Long   tokenCount;
   private Long   appearancesCount;

   public Double getTokenWeightsSum () {
      return tokenWeightsSum;
   }

   public void setTokenWeightsSum (Double tokenWeightsSum) {
      this.tokenWeightsSum = tokenWeightsSum;
   }

   /**
    * @return the tokenCount
    */
   public Long getTokenCount () {
      return tokenCount;
   }

   /**
    * @param tokenCount
    *           the tokenCount to set
    */
   public void setTokenCount (Long tokenCount) {
      this.tokenCount = tokenCount;
   }

   /**
    * @return the appearancesCount
    */
   public Long getAppearancesCount () {
      return appearancesCount;
   }

   /**
    * @param appearancesCount
    *           the appearancesCount to set
    */
   public void setAppearancesCount (Long appearancesCount) {
      this.appearancesCount = appearancesCount;
   }

   public Long getAppId () {
      return appId;
   }

   public void setAppId (Long appId) {
      this.appId = appId;
   }

}
