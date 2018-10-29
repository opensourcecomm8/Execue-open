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


package com.execue.nlp.bean.entity;

import java.io.Serializable;

public class InflectionEntity extends TagEntity implements Cloneable, Serializable {

   private InflectionType inflectionType;

   @Override
   public Object clone () throws CloneNotSupportedException {
      InflectionEntity recognitionEntity = (InflectionEntity) super.clone();
      recognitionEntity.setInflectionType(inflectionType);
      return recognitionEntity;
   }

   @Override
   public String toString () {
      StringBuffer sb = new StringBuffer();
      sb.append(inflectionType.getValue());
      sb.append(" ");
      sb.append(super.toString());
      return sb.toString();
   }

   /**
    * @return the inflectionType
    */
   public InflectionType getInflectionType () {
      return inflectionType;
   }

   /**
    * @param inflectionType
    *           the inflectionType to set
    */
   public void setInflectionType (InflectionType inflectionType) {
      this.inflectionType = inflectionType;
   }

}
