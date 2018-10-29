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

import java.io.Serializable;

import com.execue.core.common.type.AssetProviderType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.DataType;
import com.execue.core.common.type.DateQualifier;

public class DateFormat implements Serializable {

   private static final long serialVersionUID = 1L;
   private Long              id;
   private String            format;
   private AssetProviderType assetProviderType;
   private String            dbFormat;
   private CheckType         isPlainFormat;
   private DateQualifier     qualifier;
   private CheckType         supported        = CheckType.YES;
   private Long              qualifierBEDId;
   private DataType          dataType;
   private CheckType         evaluated        = CheckType.NO;

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public String getFormat () {
      return format;
   }

   public void setFormat (String format) {
      this.format = format;
   }

   public AssetProviderType getAssetProviderType () {
      return assetProviderType;
   }

   public void setAssetProviderType (AssetProviderType assetProviderType) {
      this.assetProviderType = assetProviderType;
   }

   public String getDbFormat () {
      return dbFormat;
   }

   public void setDbFormat (String dbFormat) {
      this.dbFormat = dbFormat;
   }

   public CheckType getIsPlainFormat () {
      return isPlainFormat;
   }

   public void setIsPlainFormat (CheckType isPlainFormat) {
      this.isPlainFormat = isPlainFormat;
   }

   public DateQualifier getQualifier () {
      return qualifier;
   }

   public void setQualifier (DateQualifier qualifier) {
      this.qualifier = qualifier;
   }

   public CheckType getSupported () {
      return supported;
   }

   public void setSupported (CheckType supported) {
      this.supported = supported;
   }

   public Long getQualifierBEDId () {
      return qualifierBEDId;
   }

   public void setQualifierBEDId (Long qualifierBEDId) {
      this.qualifierBEDId = qualifierBEDId;
   }

   public DataType getDataType () {
      return dataType;
   }

   public void setDataType (DataType dataType) {
      this.dataType = dataType;
   }

   public CheckType getEvaluated () {
      return evaluated;
   }

   public void setEvaluated (CheckType evaluated) {
      this.evaluated = evaluated;
   }

}
