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

import com.execue.core.common.bean.INormalizedData;
import com.execue.core.common.type.DataType;
import com.execue.core.common.type.QueryValueType;

public class QueryValue {

   private String          value;
   private QueryValueType  valueType;
   private DataType        dataType;
   private Long            numberConversionId;
   private String          targetConversionFormat;
   private INormalizedData normalizedData;

   public String getValue () {
      return value;
   }

   public void setValue (String value) {
      this.value = value;
   }

   public DataType getDataType () {
      return dataType;
   }

   public void setDataType (DataType dataType) {
      this.dataType = dataType;
   }

   public Long getNumberConversionId () {
      return numberConversionId;
   }

   public void setNumberConversionId (Long numberConversionId) {
      this.numberConversionId = numberConversionId;
   }

   public String getTargetConversionFormat () {
      return targetConversionFormat;
   }

   public void setTargetConversionFormat (String targetConversionFormat) {
      this.targetConversionFormat = targetConversionFormat;
   }

   public QueryValueType getValueType () {
      return valueType;
   }

   public void setValueType (QueryValueType valueType) {
      this.valueType = valueType;
   }

   public INormalizedData getNormalizedData () {
      return normalizedData;
   }

   public void setNormalizedData (INormalizedData normalizedData) {
      this.normalizedData = normalizedData;
   }

}
