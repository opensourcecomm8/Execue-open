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


package com.execue.core.common.bean.qdata;

import java.io.Serializable;

import com.execue.core.common.type.RFXVariationSubType;

public class RankingWeight implements Serializable {

   private static final long   serialVersionUID = 1L;
   private Long                id;
   private RFXVariationSubType userQueryVariationSubType;
   private RFXVariationSubType contentVariationSubType;
   private Double              matchWeight;

   /**
    * @return the id
    */
   public Long getId () {
      return id;
   }

   /**
    * @param id
    *           the id to set
    */
   public void setId (Long id) {
      this.id = id;
   }

   /**
    * @return the userQueryVariationSubType
    */
   public RFXVariationSubType getUserQueryVariationSubType () {
      return userQueryVariationSubType;
   }

   /**
    * @param userQueryVariationSubType
    *           the userQueryVariationSubType to set
    */
   public void setUserQueryVariationSubType (RFXVariationSubType userQueryVariationSubType) {
      this.userQueryVariationSubType = userQueryVariationSubType;
   }

   /**
    * @return the contentVariationSubType
    */
   public RFXVariationSubType getContentVariationSubType () {
      return contentVariationSubType;
   }

   /**
    * @param contentVariationSubType
    *           the contentVariationSubType to set
    */
   public void setContentVariationSubType (RFXVariationSubType contentVariationSubType) {
      this.contentVariationSubType = contentVariationSubType;
   }

   /**
    * @return the matchWeight
    */
   public Double getMatchWeight () {
      return matchWeight;
   }

   /**
    * @param matchWeight
    *           the matchWeight to set
    */
   public void setMatchWeight (Double matchWeight) {
      this.matchWeight = matchWeight;
   }
}