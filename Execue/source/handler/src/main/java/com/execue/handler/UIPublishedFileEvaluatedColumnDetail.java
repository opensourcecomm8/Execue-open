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


package com.execue.handler;

import com.execue.core.common.type.ColumnType;
import com.execue.core.common.type.GranularityType;
import com.execue.handler.bean.grid.IGridBean;

public class UIPublishedFileEvaluatedColumnDetail implements IGridBean {

   private String                    columnName;
   private ColumnType                kdxDataType;
   private String                    format;
   private String                    unit;
   private UIPublishedFileColumnInfo columnDetail;
   private GranularityType           granularity = GranularityType.NA;

   public String getColumnName () {
      return columnName;
   }

   public void setColumnName (String columnName) {
      this.columnName = columnName;
   }

   public ColumnType getKdxDataType () {
      return kdxDataType;
   }

   public void setKdxDataType (ColumnType kdxDataType) {
      this.kdxDataType = kdxDataType;
   }

   public GranularityType getGranularity () {
      return granularity;
   }

   public void setGranularity (GranularityType granularity) {
      this.granularity = granularity;
   }

   public UIPublishedFileColumnInfo getColumnDetail () {
      return columnDetail;
   }

   public void setColumnDetail (UIPublishedFileColumnInfo columnDetail) {
      this.columnDetail = columnDetail;
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

}
