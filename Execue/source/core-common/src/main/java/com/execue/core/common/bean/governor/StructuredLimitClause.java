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


package com.execue.core.common.bean.governor;

import com.execue.core.common.type.OrderLimitEntityType;

public class StructuredLimitClause {

   private BusinessAssetTerm    businessAssetTerm;
   private OrderLimitEntityType limitType;
   private Integer              limitValue;
   private Integer              startValue;

   public BusinessAssetTerm getBusinessAssetTerm () {
      return businessAssetTerm;
   }

   public void setBusinessAssetTerm (BusinessAssetTerm businessAssetTerm) {
      this.businessAssetTerm = businessAssetTerm;
   }

   public OrderLimitEntityType getLimitType () {
      return limitType;
   }

   public void setLimitType (OrderLimitEntityType limitType) {
      this.limitType = limitType;
   }

   public Integer getLimitValue () {
      return limitValue;
   }

   public void setLimitValue (Integer limitValue) {
      this.limitValue = limitValue;
   }

   public Integer getStartValue () {
      return startValue;
   }

   public void setStartValue (Integer startValue) {
      this.startValue = startValue;
   }
}