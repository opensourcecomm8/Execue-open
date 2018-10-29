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

import com.execue.core.common.type.PopularityTermType;

/**
 * @author John Mallavalli
 */
public class PopularityInfo implements Serializable {

   private String             fullyQualifiedName;
   private PopularityTermType popularityTermType;
   private Long               hits;
   private Double             weight;

   public String getFullyQualifiedName () {
      return fullyQualifiedName;
   }

   public void setFullyQualifiedName (String fullyQualifiedName) {
      this.fullyQualifiedName = fullyQualifiedName;
   }

   public PopularityTermType getPopularityTermType () {
      return popularityTermType;
   }

   public void setPopularityTermType (PopularityTermType popularityTermType) {
      this.popularityTermType = popularityTermType;
   }

   public Long getHits () {
      return hits;
   }

   public void setHits (Long hits) {
      this.hits = hits;
   }

   public Double getWeight () {
      return weight;
   }

   public void setWeight (Double weight) {
      this.weight = weight;
   }

}