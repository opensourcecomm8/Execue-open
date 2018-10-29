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

public class UIRangeDetail {

   private Long   id;
   private String order;
   private String value;
   private String description;
   private String lowerLimit;
   private String upperLimit;

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public String getValue () {
      return value;
   }

   public void setValue (String value) {
      this.value = value;
   }

   public String getDescription () {
      return description;
   }

   public void setDescription (String description) {
      this.description = description;
   }

   public String getLowerLimit () {
      return lowerLimit;
   }

   public void setLowerLimit (String lowerLimit) {
      this.lowerLimit = lowerLimit;
   }

   public String getUpperLimit () {
      return upperLimit;
   }

   public void setUpperLimit (String upperLimit) {
      this.upperLimit = upperLimit;
   }

   public String getOrder () {
      return order;
   }

   public void setOrder (String order) {
      this.order = order;
   }

}
