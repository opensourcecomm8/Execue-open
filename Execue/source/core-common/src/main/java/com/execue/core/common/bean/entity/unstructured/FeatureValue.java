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


package com.execue.core.common.bean.entity.unstructured;

import java.io.Serializable;

import com.execue.core.common.type.CheckType;

public class FeatureValue implements Serializable {

   private static final long   serialVersionUID = 1L;
   private Long                id;
   private Long                featureId;
   private String              featureValue;
   private Long                featureValueBeId;
   private transient Long      featureValueCount;
   private transient CheckType selected         = CheckType.NO;

   public Long getFeatureId () {
      return featureId;
   }

   public void setFeatureId (Long featureId) {
      this.featureId = featureId;
   }

   public String getFeatureValue () {
      return featureValue;
   }

   public void setFeatureValue (String featureValue) {
      this.featureValue = featureValue;
   }

   public Long getFeatureValueCount () {
      return featureValueCount;
   }

   public void setFeatureValueCount (Long featureValueCount) {
      this.featureValueCount = featureValueCount;
   }

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   /**
    * @return the featureValueBeId
    */
   public Long getFeatureValueBeId () {
      return featureValueBeId;
   }

   /**
    * @param featureValueBeId
    *           the featureValueBeId to set
    */
   public void setFeatureValueBeId (Long featureValueBeId) {
      this.featureValueBeId = featureValueBeId;
   }

   /**
    * @return the selected
    */
   public CheckType getSelected () {
      return selected;
   }

   /**
    * @param selected
    *           the selected to set
    */
   public void setSelected (CheckType selected) {
      this.selected = selected;
   }

}
