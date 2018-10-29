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

import com.execue.core.common.type.AttributeType;

public class UDXAttribute implements Serializable {

   private Long              id;
   private UnStructuredIndex udx;
   private AttributeType     attributeType;
   private String            attributeValue;

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
    * @return the attributeType
    */
   public AttributeType getAttributeType () {
      return attributeType;
   }

   /**
    * @param attributeType
    *           the attributeType to set
    */
   public void setAttributeType (AttributeType attributeType) {
      this.attributeType = attributeType;
   }

   /**
    * @return the attributeValue
    */
   public String getAttributeValue () {
      return attributeValue;
   }

   /**
    * @param attributeValue
    *           the attributeValue to set
    */
   public void setAttributeValue (String attributeValue) {
      this.attributeValue = attributeValue;
   }

   /**
    * @return the udx
    */
   public UnStructuredIndex getUdx () {
      return udx;
   }

   /**
    * @param udx
    *           the udx to set
    */
   public void setUdx (UnStructuredIndex udx) {
      this.udx = udx;
   }
}