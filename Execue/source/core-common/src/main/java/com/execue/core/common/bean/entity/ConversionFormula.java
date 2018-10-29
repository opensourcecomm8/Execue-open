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

import com.execue.core.common.type.ConversionType;

/**
 * This bean represents the conversion formula between source and destination units or formats.
 * 
 * @author Vishay
 * @version 1.0
 * @since 30/06/09
 */
public class ConversionFormula {

   private Long           id;
   private ConversionType type;
   private String         source;
   private String         target;
   private String         formula;

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public ConversionType getType () {
      return type;
   }

   public void setType (ConversionType type) {
      this.type = type;
   }

   public String getFormula () {
      return formula;
   }

   public void setFormula (String formula) {
      this.formula = formula;
   }

   
   public String getSource () {
      return source;
   }

   
   public void setSource (String source) {
      this.source = source;
   }

   
   public String getTarget () {
      return target;
   }

   
   public void setTarget (String target) {
      this.target = target;
   }
}
