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

import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.ColumnType;
import com.execue.core.common.type.ConversionType;
import com.execue.core.common.type.DataType;
import com.execue.core.common.type.GranularityType;

/**
 * This class represents the PublisherTableDetails object.
 * 
 * @author MurthySN
 * @version 1.0
 * @since 27/10/09
 */

public class PublishedFileTableDetails implements Serializable {

   private static final long      serialVersionUID = 1L;
   private Long                   id;
   private String                 baseColumnName;
   private String                 evaluatedColumnName;
   private String                 evaluatedColumnDisplayName;
   private DataType               baseDataType;
   private DataType               evaluatedDataType;
   private DataType               originalEvaluatedDataType;
   private int                    basePrecision;
   private int                    evaluatedPrecision;
   private int                    originalEvaluatedPrecision;
   private int                    baseScale;
   private int                    originalEvaluatedScale;
   private int                    evaluatedScale;
   private int                    columnIndex;
   private ColumnType             kdxDataType;
   private CheckType              population       = CheckType.NO;
   private CheckType              distribution     = CheckType.NO;
   private GranularityType        granularity      = GranularityType.NA;
   private String                 format;
   private String                 unit;
   private ConversionType         unitType;
   private ConversionType         originalUnitType;
   private CheckType              defaultMetric    = CheckType.NO;
   private PublishedFileTableInfo publishedFileTableInfo;

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public String getBaseColumnName () {
      return baseColumnName;
   }

   public void setBaseColumnName (String baseColumnName) {
      this.baseColumnName = baseColumnName;
   }

   public String getEvaluatedColumnName () {
      return evaluatedColumnName;
   }

   public void setEvaluatedColumnName (String evaluatedColumnName) {
      this.evaluatedColumnName = evaluatedColumnName;
   }

   public String getEvaluatedColumnDisplayName () {
      return evaluatedColumnDisplayName;
   }

   public void setEvaluatedColumnDisplayName (String evaluatedColumnDisplayName) {
      this.evaluatedColumnDisplayName = evaluatedColumnDisplayName;
   }

   public DataType getBaseDataType () {
      return baseDataType;
   }

   public void setBaseDataType (DataType baseDataType) {
      this.baseDataType = baseDataType;
   }

   public DataType getEvaluatedDataType () {
      return evaluatedDataType;
   }

   public void setEvaluatedDataType (DataType evaluatedDataType) {
      this.evaluatedDataType = evaluatedDataType;
   }

   public int getBasePrecision () {
      return basePrecision;
   }

   public void setBasePrecision (int basePrecision) {
      this.basePrecision = basePrecision;
   }

   public int getEvaluatedPrecision () {
      return evaluatedPrecision;
   }

   public void setEvaluatedPrecision (int evaluatedPrecision) {
      this.evaluatedPrecision = evaluatedPrecision;
   }

   public int getBaseScale () {
      return baseScale;
   }

   public void setBaseScale (int baseScale) {
      this.baseScale = baseScale;
   }

   public int getEvaluatedScale () {
      return evaluatedScale;
   }

   public void setEvaluatedScale (int evaluatedScale) {
      this.evaluatedScale = evaluatedScale;
   }

   public int getColumnIndex () {
      return columnIndex;
   }

   public void setColumnIndex (int columnIndex) {
      this.columnIndex = columnIndex;
   }

   /*
    * public CheckType getIsTimeBased () { return isTimeBased; } public void setIsTimeBased (CheckType isTimeBased) {
    * this.isTimeBased = isTimeBased; } public CheckType getIsLocationBased () { return isLocationBased; } public void
    * setIsLocationBased (CheckType isLocationBased) { this.isLocationBased = isLocationBased; }
    */

   public PublishedFileTableInfo getPublishedFileTableInfo () {
      return publishedFileTableInfo;
   }

   public void setPublishedFileTableInfo (PublishedFileTableInfo publishedFileTableInfo) {
      this.publishedFileTableInfo = publishedFileTableInfo;
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

   public GranularityType getGranularity () {
      return granularity;
   }

   public void setGranularity (GranularityType granularity) {
      this.granularity = granularity;
   }

   public CheckType getPopulation () {
      return population;
   }

   public void setPopulation (CheckType population) {
      this.population = population;
   }

   public CheckType getDistribution () {
      return distribution;
   }

   public void setDistribution (CheckType distribution) {
      this.distribution = distribution;
   }

   public CheckType getDefaultMetric () {
      return defaultMetric;
   }

   public void setDefaultMetric (CheckType defaultMetric) {
      this.defaultMetric = defaultMetric;
   }

   public DataType getOriginalEvaluatedDataType () {
      return originalEvaluatedDataType;
   }

   public void setOriginalEvaluatedDataType (DataType originalEvaluatedDataType) {
      this.originalEvaluatedDataType = originalEvaluatedDataType;
   }

   public int getOriginalEvaluatedPrecision () {
      return originalEvaluatedPrecision;
   }

   public void setOriginalEvaluatedPrecision (int originalEvaluatedPrecision) {
      this.originalEvaluatedPrecision = originalEvaluatedPrecision;
   }

   public int getOriginalEvaluatedScale () {
      return originalEvaluatedScale;
   }

   public void setOriginalEvaluatedScale (int originalEvaluatedScale) {
      this.originalEvaluatedScale = originalEvaluatedScale;
   }

   public ConversionType getOriginalUnitType () {
      return originalUnitType;
   }

   public void setOriginalUnitType (ConversionType originalUnitType) {
      this.originalUnitType = originalUnitType;
   }

}
