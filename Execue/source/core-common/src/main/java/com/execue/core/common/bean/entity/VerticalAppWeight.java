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


package com.execue.core.common.bean.entity;

import java.io.Serializable;

/**
 * This class represents the Concept object.
 * 
 * @author Vishay
 * @version 1.0
 * @since 12/01/09
 */
public class VerticalAppWeight implements Serializable {

   private static final long serialVersionUID = 1L;
   private Long              id;
   private Long              verticalId;
   private Long              applicationId;
   private Double            weight           = 1.00;

   public Double getWeight () {
      return weight;
   }

   /**
    * @param weight
    *           the weight to set
    */
   public void setWeight (Double weight) {
      this.weight = weight;
   }

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public Long getVerticalId () {
      return verticalId;
   }

   public void setVerticalId (Long verticalId) {
      this.verticalId = verticalId;
   }

   public Long getApplicationId () {
      return applicationId;
   }

   public void setApplicationId (Long applicationId) {
      this.applicationId = applicationId;
   }

}
