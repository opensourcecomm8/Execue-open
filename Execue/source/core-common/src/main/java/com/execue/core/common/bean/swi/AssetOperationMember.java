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
import java.math.BigDecimal;

public class AssetOperationMember implements Serializable {

   private Long       id;
   private String     lookupValue;
   private String     lookupDescription;
   private String     longDescription;
   private BigDecimal lowerLimit;
   private BigDecimal upperLimit;
   private String     kdxLookupDescription;

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public String getLookupValue () {
      return lookupValue;
   }

   public void setLookupValue (String lookupValue) {
      this.lookupValue = lookupValue;
   }

   public String getLookupDescription () {
      return lookupDescription;
   }

   public void setLookupDescription (String lookupDescription) {
      this.lookupDescription = lookupDescription;
   }

   public String getLongDescription () {
      return longDescription;
   }

   public void setLongDescription (String longDescription) {
      this.longDescription = longDescription;
   }

   public BigDecimal getLowerLimit () {
      return lowerLimit;
   }

   public void setLowerLimit (BigDecimal lowerLimit) {
      this.lowerLimit = lowerLimit;
   }

   public BigDecimal getUpperLimit () {
      return upperLimit;
   }

   public void setUpperLimit (BigDecimal upperLimit) {
      this.upperLimit = upperLimit;
   }

   public String getKdxLookupDescription () {
      return kdxLookupDescription;
   }

   public void setKdxLookupDescription (String kdxLookupDescription) {
      this.kdxLookupDescription = kdxLookupDescription;
   }

}
