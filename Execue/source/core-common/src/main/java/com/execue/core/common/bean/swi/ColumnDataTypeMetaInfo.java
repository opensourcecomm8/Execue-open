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

import com.execue.core.common.type.ConversionType;
import com.execue.core.common.type.DataType;

public class ColumnDataTypeMetaInfo {

   private DataType       dataType;

   private int            precision;

   private int            scale;

   private String         dateFormat;

   private ConversionType unitType;

   public DataType getDataType () {
      return dataType;
   }

   public void setDataType (DataType dataType) {
      this.dataType = dataType;
   }

   public int getPrecision () {
      return precision;
   }

   public void setPrecision (int precision) {
      this.precision = precision;
   }

   public int getScale () {
      return scale;
   }

   public void setScale (int scale) {
      this.scale = scale;
   }

   public String getDateFormat () {
      return dateFormat;
   }

   public void setDateFormat (String dateFormat) {
      this.dateFormat = dateFormat;
   }

   public ConversionType getUnitType () {
      return unitType;
   }

   public void setUnitType (ConversionType unitType) {
      this.unitType = unitType;
   }

}
