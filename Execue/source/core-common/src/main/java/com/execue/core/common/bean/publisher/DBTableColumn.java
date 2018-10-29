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


package com.execue.core.common.bean.publisher;

import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.ConversionType;
import com.execue.core.common.type.DataType;
import com.execue.core.common.type.GranularityType;

/**
 * This bean represents the DBTableColumn information
 * 
 * @author Vishay
 * @since 20/10/09
 * @version 1.0
 */
public class DBTableColumn {

   private String          columnName;
   private DataType        dataType;
   private int             scale;
   private int             precision;
   private String          format;
   private String          unit;
   private ConversionType  unitType      = ConversionType.NULL;
   private GranularityType granularity   = GranularityType.NA;
   private CheckType       defaultMetric = CheckType.NO;

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

   public int getScale () {
      return scale;
   }

   public void setScale (int scale) {
      this.scale = scale;
   }

   public int getPrecision () {
      return precision;
   }

   public void setPrecision (int precision) {
      this.precision = precision;
   }

   public String getFormat () {
      return format;
   }

   public void setFormat (String format) {
      this.format = format;
   }

   public String getUnit () {
      return unit;
   }

   public void setUnit (String unit) {
      this.unit = unit;
   }

   public ConversionType getUnitType () {
      return unitType;
   }

   public void setUnitType (ConversionType unitType) {
      this.unitType = unitType;
   }

   public GranularityType getGranularity () {
      return granularity;
   }

   public void setGranularity (GranularityType granularity) {
      this.granularity = granularity;
   }

   
   public CheckType getDefaultMetric () {
      return defaultMetric;
   }

   
   public void setDefaultMetric (CheckType defaultMetric) {
      this.defaultMetric = defaultMetric;
   }

}
