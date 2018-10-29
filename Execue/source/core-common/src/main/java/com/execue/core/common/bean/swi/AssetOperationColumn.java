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

import java.io.Serializable;

import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.ColumnType;
import com.execue.core.common.type.DataType;

public class AssetOperationColumn implements Serializable {

   private Long       id;
   private String     name;
   private String     description;
   private DataType   dataType;
   private ColumnType kdxDataType;
   private CheckType  required;
   private String     defaultValue;
   private CheckType  isConstraintColum;
   private int        scale;
   private boolean    nullable;
   private int        precision;

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public String getName () {
      return name;
   }

   public void setName (String name) {
      this.name = name;
   }

   public String getDescription () {
      return description;
   }

   public void setDescription (String description) {
      this.description = description;
   }

   /**
    * @return the dataType
    */
   public DataType getDataType () {
      return dataType;
   }

   /**
    * @param dataType
    *           the dataType to set
    */
   public void setDataType (DataType dataType) {
      this.dataType = dataType;
   }

   /**
    * @return the kdxDataType
    */
   public ColumnType getKdxDataType () {
      return kdxDataType;
   }

   /**
    * @param kdxDataType
    *           the kdxDataType to set
    */
   public void setKdxDataType (ColumnType kdxDataType) {
      this.kdxDataType = kdxDataType;
   }

   /**
    * @return the required
    */
   public CheckType getRequired () {
      return required;
   }

   /**
    * @param required
    *           the required to set
    */
   public void setRequired (CheckType required) {
      this.required = required;
   }

   /**
    * @return the defaultValue
    */
   public String getDefaultValue () {
      return defaultValue;
   }

   /**
    * @param defaultValue
    *           the defaultValue to set
    */
   public void setDefaultValue (String defaultValue) {
      this.defaultValue = defaultValue;
   }

   /**
    * @return the isConstraintColum
    */
   public CheckType getIsConstraintColum () {
      return isConstraintColum;
   }

   /**
    * @param isConstraintColum
    *           the isConstraintColum to set
    */
   public void setIsConstraintColum (CheckType isConstraintColum) {
      this.isConstraintColum = isConstraintColum;
   }

   /**
    * @return the scale
    */
   public int getScale () {
      return scale;
   }

   /**
    * @param scale
    *           the scale to set
    */
   public void setScale (int scale) {
      this.scale = scale;
   }

   /**
    * @return the nullable
    */
   public boolean isNullable () {
      return nullable;
   }

   /**
    * @param nullable
    *           the nullable to set
    */
   public void setNullable (boolean nullable) {
      this.nullable = nullable;
   }

   /**
    * @return the precision
    */
   public int getPrecision () {
      return precision;
   }

   /**
    * @param precision
    *           the precision to set
    */
   public void setPrecision (int precision) {
      this.precision = precision;
   }

}
