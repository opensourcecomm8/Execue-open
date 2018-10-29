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

import com.execue.core.common.type.RecognitionType;

/**
 * @author john
 */
public class EntityNameVariation {

   private String          name;
   private RecognitionType type;
   private boolean         skipSecondaryWordCheck = true;

   public String getName () {
      return name;
   }

   public EntityNameVariation () {
   }

   public EntityNameVariation (String name, RecognitionType type) {
      this.name = name;
      this.type = type;
   }

   public void setName (String name) {
      this.name = name;
   }

   public RecognitionType getType () {
      return type;
   }

   public void setType (RecognitionType type) {
      this.type = type;
   }

   public boolean isSkipSecondaryWordCheck () {
      return skipSecondaryWordCheck;
   }

   public void setSkipSecondaryWordCheck (boolean skipSecondaryWordCheck) {
      this.skipSecondaryWordCheck = skipSecondaryWordCheck;
   }
}
