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


package com.execue.core.common.bean.querygen;

import com.execue.core.common.type.ConversionType;
import com.execue.core.common.type.DataType;
import com.execue.core.common.type.GranularityType;

public class QueryColumn {

   private String          columnName;
   private boolean         isDistinct = false;
   private DataType        dataType;
   private int             precision  = 0;
   private int             scale      = 0;
   private boolean         isNullable = true;
   private String          defaultValue;
   private String          dataFormat;
   private String          fileDateFormat;
   private ConversionType  unitType;
   private GranularityType granularity;
   private boolean         autoIncrement;

   public GranularityType getGranularity () {
      return granularity;
   }

   public void setGranularity (GranularityType granularity) {
      this.granularity = granularity;
   }

   public boolean isDistinct () {
      return isDistinct;
   }

   public void setDistinct (boolean isDistinct) {
      this.isDistinct = isDistinct;
   }

   public String getColumnName () {
      return columnName;
   }

   public void setColumnName (String columnName) {
      this.columnName = columnName;
   }

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

   public boolean isNullable () {
      return isNullable;
   }

   public void setNullable (boolean isNullable) {
      this.isNullable = isNullable;
   }

   public String getDefaultValue () {
      return defaultValue;
   }

   public void setDefaultValue (String defaultValue) {
      this.defaultValue = defaultValue;
   }

   public String getDataFormat () {
      return dataFormat;
   }

   public void setDataFormat (String dataFormat) {
      this.dataFormat = dataFormat;
   }

   public ConversionType getUnitType () {
      return unitType;
   }

   public void setUnitType (ConversionType unitType) {
      this.unitType = unitType;
   }

   public String getFileDateFormat () {
      return fileDateFormat;
   }

   public void setFileDateFormat (String fileDateFormat) {
      this.fileDateFormat = fileDateFormat;
   }

   public boolean isAutoIncrement () {
      return autoIncrement;
   }

   public void setAutoIncrement (boolean autoIncrement) {
      this.autoIncrement = autoIncrement;
   }

}
