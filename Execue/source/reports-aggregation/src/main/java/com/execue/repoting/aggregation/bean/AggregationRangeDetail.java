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

package com.execue.repoting.aggregation.bean;

/**
 * @author Nitesh
 */
public class AggregationRangeDetail {

   private Long    rangeDetailId;
   private Integer order;
   private String  value;
   private String  description;
   private Double  lowerLimit;
   private Double  upperLimit;

   /**
    * @return the rangeDetailId
    */
   public Long getRangeDetailId () {
      return rangeDetailId;
   }

   /**
    * @param rangeDetailId the rangeDetailId to set
    */
   public void setRangeDetailId (Long rangeDetailId) {
      this.rangeDetailId = rangeDetailId;
   }

   /**
    * @return the order
    */
   public Integer getOrder () {
      return order;
   }

   /**
    * @param order the order to set
    */
   public void setOrder (Integer order) {
      this.order = order;
   }

   /**
    * @return the value
    */
   public String getValue () {
      return value;
   }

   /**
    * @param value the value to set
    */
   public void setValue (String value) {
      this.value = value;
   }

   /**
    * @return the description
    */
   public String getDescription () {
      return description;
   }

   /**
    * @param description the description to set
    */
   public void setDescription (String description) {
      this.description = description;
   }

   /**
    * @return the lowerLimit
    */
   public Double getLowerLimit () {
      return lowerLimit;
   }

   /**
    * @param lowerLimit the lowerLimit to set
    */
   public void setLowerLimit (Double lowerLimit) {
      this.lowerLimit = lowerLimit;
   }

   /**
    * @return the upperLimit
    */
   public Double getUpperLimit () {
      return upperLimit;
   }

   /**
    * @param upperLimit the upperLimit to set
    */
   public void setUpperLimit (Double upperLimit) {
      this.upperLimit = upperLimit;
   }

}
