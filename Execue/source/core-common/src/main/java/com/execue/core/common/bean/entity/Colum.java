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

import java.util.Set;

import com.execue.core.common.bean.IAssetEntity;
import com.execue.core.common.bean.ISecurityBean;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.ColumnType;
import com.execue.core.common.type.ConstraintSubType;
import com.execue.core.common.type.ConversionType;
import com.execue.core.common.type.DataType;
import com.execue.core.common.type.GranularityType;

/**
 * This class represents the Column object.
 * 
 * @author Vishay
 * @version 1.0
 * @since 12/01/09
 */
public class Colum implements java.io.Serializable, IAssetEntity, ISecurityBean {

   private static final long          serialVersionUID = 1L;
   private Long                       id;
   private String                     name;
   private String                     displayName;
   private Tabl                       ownerTable;
   private String                     description;
   private DataType                   dataType;
   private ColumnType                 kdxDataType;
   private CheckType                  required;
   private String                     defaultValue;
   private CheckType                  isConstraintColum;
   private ConstraintSubType          primaryKey;
   private ConstraintSubType          foreignKey;
   private int                        scale;                                // after decimal digits
   private boolean                    nullable;
   private int                        precision;                            // total length of field
   private Set<AssetEntityDefinition> assetEntityDefinitions;
   private Set<Constraint>            constraints;
   private String                     dataFormat;
   private String                     fileDateFormat;
   private ConversionType             conversionType;
   private String                     unit;
   private GranularityType            granularity      = GranularityType.NA;
   private CheckType                  indicator        = CheckType.NO;
   private transient CheckType        defaultMetric    = CheckType.NO;

   public GranularityType getGranularity () {
      return granularity;
   }

   public void setGranularity (GranularityType granularity) {
      this.granularity = granularity;
   }

   public String getDataFormat () {
      return dataFormat;
   }

   public void setDataFormat (String dataFormat) {
      this.dataFormat = dataFormat;
   }

   public int getScale () {
      return scale;
   }

   public void setScale (int size) {
      this.scale = size;
   }

   public boolean isNullable () {
      return nullable;
   }

   public void setNullable (boolean nullable) {
      this.nullable = nullable;
   }

   public int getPrecision () {
      return precision;
   }

   public void setPrecision (int precision) {
      this.precision = precision;
   }

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

   public CheckType getRequired () {
      return required;
   }

   public void setRequired (CheckType required) {
      this.required = required;
   }

   public String getDefaultValue () {
      return defaultValue;
   }

   public void setDefaultValue (String defaultValue) {
      this.defaultValue = defaultValue;
   }

   public Set<AssetEntityDefinition> getAssetEntityDefinitions () {
      return assetEntityDefinitions;
   }

   public void setAssetEntityDefinitions (Set<AssetEntityDefinition> assetEntityDefinitions) {
      this.assetEntityDefinitions = assetEntityDefinitions;
   }

   public Tabl getOwnerTable () {
      return ownerTable;
   }

   public void setOwnerTable (Tabl ownerTable) {
      this.ownerTable = ownerTable;
   }

   public DataType getDataType () {
      return dataType;
   }

   public void setDataType (DataType dataType) {
      this.dataType = dataType;
   }

   public Set<Constraint> getConstraints () {
      return constraints;
   }

   public void setConstraints (Set<Constraint> constraints) {
      this.constraints = constraints;
   }

   public CheckType getIsConstraintColum () {
      return isConstraintColum;
   }

   public void setIsConstraintColum (CheckType isConstraintColum) {
      this.isConstraintColum = isConstraintColum;
   }

   public ColumnType getKdxDataType () {
      return kdxDataType;
   }

   public void setKdxDataType (ColumnType kdxDataType) {
      this.kdxDataType = kdxDataType;
   }

   public ConversionType getConversionType () {
      return conversionType;
   }

   public void setConversionType (ConversionType conversionType) {
      this.conversionType = conversionType;
   }

   public String getUnit () {
      return unit;
   }

   public void setUnit (String unit) {
      this.unit = unit;
   }

   public ConstraintSubType getPrimaryKey () {
      return primaryKey;
   }

   public void setPrimaryKey (ConstraintSubType primaryKey) {
      this.primaryKey = primaryKey;
   }

   public ConstraintSubType getForeignKey () {
      return foreignKey;
   }

   public void setForeignKey (ConstraintSubType foreignKey) {
      this.foreignKey = foreignKey;
   }

   public CheckType getDefaultMetric () {
      return defaultMetric;
   }

   public void setDefaultMetric (CheckType defaultMetric) {
      this.defaultMetric = defaultMetric;
   }

   public String getDisplayName () {
      return displayName;
   }

   public void setDisplayName (String displayName) {
      this.displayName = displayName;
   }

   public String getFileDateFormat () {
      return fileDateFormat;
   }

   public void setFileDateFormat (String fileDateFormat) {
      this.fileDateFormat = fileDateFormat;
   }

   public CheckType getIndicator () {
      return indicator;
   }

   public void setIndicator (CheckType indicator) {
      this.indicator = indicator;
   }
}