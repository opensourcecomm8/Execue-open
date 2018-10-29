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


package com.execue.core.common.bean.qi.suggest;

/**
 * @author kaliki
 * @since 4.0
 */
public class SuggestConditionTerm extends SuggestTerm {

   private String  datatype;
   private boolean hasInstances;
   private String  unitType;
   private String  dataFromat;

   public String getDatatype () {
      return datatype;
   }

   public void setDatatype (String datatype) {
      this.datatype = datatype;
   }

   public boolean isHasInstances () {
      return hasInstances;
   }

   public void setHasInstances (boolean hasInstances) {
      this.hasInstances = hasInstances;
   }

   public String getUnitType () {
      return unitType;
   }

   public void setUnitType (String unitType) {
      this.unitType = unitType;
   }

   public String getDataFromat () {
      return dataFromat;
   }

   public void setDataFromat (String dataFromat) {
      this.dataFromat = dataFromat;
   }

}
