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


/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */

package com.execue.core.common.bean.entity;

import java.io.Serializable;

/**
 * @author venu
 */
public class RangeDetail implements Serializable {

   private Long    id;
   private Range   range;
   private Integer order;
   private String  value;
   private String  description;
   private Double  lowerLimit;
   private Double  upperLimit;

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public Range getRange () {
      return range;
   }

   public void setRange (Range range) {
      this.range = range;
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

   public Double getLowerLimit () {
      return lowerLimit;
   }

   public void setLowerLimit (Double lowerLimit) {
      this.lowerLimit = lowerLimit;
   }

   public Double getUpperLimit () {
      return upperLimit;
   }

   public void setUpperLimit (Double upperLimit) {
      this.upperLimit = upperLimit;
   }

   public Integer getOrder () {
      return order;
   }

   public void setOrder (Integer order) {
      this.order = order;
   }

}
