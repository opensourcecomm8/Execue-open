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


package com.execue.handler.bean;

import java.io.Serializable;

import com.execue.core.common.bean.qi.QIConversion;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.ColumnType;
import com.execue.core.common.type.ConversionType;
import com.execue.core.common.type.GranularityType;
import com.execue.core.common.type.PublisherDataType;

/**
 * @author JTiwari
 * @since 05/11/2009
 */
public class UIPublishedFileTableDetail implements Serializable {

   private Long              id;
   private String            baseColumnName;
   private String            evaluatedColumnName;
   private PublisherDataType baseDataType;
   private PublisherDataType evaluatedDataType;
   private int               basePrecision;
   private int               evaluatedPrecision;
   private int               baseScale;
   private int               evaluatedScale;
   private ColumnType        kdxDataType;
   private CheckType         isPopulation;
   private CheckType         isDistribution;
   private CheckType         isTimeBased;
   private CheckType         isLocationBased;
   private GranularityType   granularity   = GranularityType.NA;
   private String            format;
   private String            unit;
   private ConversionType    unitType;
   private int               columnIndex;
   private QIConversion      qiConversion;
   private CheckType         defaultMetric = CheckType.NO;

   /**
    * @return the id
    */
   public Long getId () {
      return id;
   }

   /**
    * @param id
    *           the id to set
    */
   public void setId (Long id) {
      this.id = id;
   }

   /**
    * @return the evaluatedColumnName
    */
   public String getEvaluatedColumnName () {
      return evaluatedColumnName;
   }

   /**
    * @param evaluatedColumnName
    *           the evaluatedColumnName to set
    */
   public void setEvaluatedColumnName (String evaluatedColumnName) {
      this.evaluatedColumnName = evaluatedColumnName;
   }

   /**
    * @return the evaluatedDataType
    */
   public PublisherDataType getEvaluatedDataType () {
      return evaluatedDataType;
   }

   /**
    * @param evaluatedDataType
    *           the evaluatedDataType to set
    */
   public void setEvaluatedDataType (PublisherDataType evaluatedDataType) {
      this.evaluatedDataType = evaluatedDataType;
   }

   /**
    * @return the evaluatedPrecision
    */
   public int getEvaluatedPrecision () {
      return evaluatedPrecision;
   }

   /**
    * @param evaluatedPrecision
    *           the evaluatedPrecision to set
    */
   public void setEvaluatedPrecision (int evaluatedPrecision) {
      this.evaluatedPrecision = evaluatedPrecision;
   }

   /**
    * @return the evaluatedScale
    */
   public int getEvaluatedScale () {
      return evaluatedScale;
   }

   /**
    * @param evaluatedScale
    *           the evaluatedScale to set
    */
   public void setEvaluatedScale (int evaluatedScale) {
      this.evaluatedScale = evaluatedScale;
   }

   /**
    * @return the isPopulation
    */
   public CheckType getIsPopulation () {
      return isPopulation;
   }

   /**
    * @param isPopulation
    *           the isPopulation to set
    */
   public void setIsPopulation (CheckType isPopulation) {
      this.isPopulation = isPopulation;
   }

   /**
    * @return the isDistribution
    */
   public CheckType getIsDistribution () {
      return isDistribution;
   }

   /**
    * @param isDistribution
    *           the isDistribution to set
    */
   public void setIsDistribution (CheckType isDistribution) {
      this.isDistribution = isDistribution;
   }

   /**
    * @return the isTimeBased
    */
   public CheckType getIsTimeBased () {
      return isTimeBased;
   }

   /**
    * @param isTimeBased
    *           the isTimeBased to set
    */
   public void setIsTimeBased (CheckType isTimeBased) {
      this.isTimeBased = isTimeBased;
   }

   /**
    * @return the isLocationBased
    */
   public CheckType getIsLocationBased () {
      return isLocationBased;
   }

   /**
    * @param isLocationBased
    *           the isLocationBased to set
    */
   public void setIsLocationBased (CheckType isLocationBased) {
      this.isLocationBased = isLocationBased;
   }

   /**
    * @return the format
    */
   public String getFormat () {
      return format;
   }

   /**
    * @param format
    *           the format to set
    */
   public void setFormat (String format) {
      this.format = format;
   }

   /**
    * @return the unit
    */
   public String getUnit () {
      return unit;
   }

   /**
    * @param unit
    *           the unit to set
    */
   public void setUnit (String unit) {
      this.unit = unit;
   }

   /**
    * @return the unitType
    */
   public ConversionType getUnitType () {
      return unitType;
   }

   /**
    * @param unitType
    *           the unitType to set
    */
   public void setUnitType (ConversionType unitType) {
      this.unitType = unitType;
   }

   /**
    * @return the baseColumnName
    */
   public String getBaseColumnName () {
      return baseColumnName;
   }

   /**
    * @param baseColumnName
    *           the baseColumnName to set
    */
   public void setBaseColumnName (String baseColumnName) {
      this.baseColumnName = baseColumnName;
   }

   /**
    * @return the baseDataType
    */
   public PublisherDataType getBaseDataType () {
      return baseDataType;
   }

   /**
    * @param baseDataType
    *           the baseDataType to set
    */
   public void setBaseDataType (PublisherDataType baseDataType) {
      this.baseDataType = baseDataType;
   }

   /**
    * @return the basePrecision
    */
   public int getBasePrecision () {
      return basePrecision;
   }

   /**
    * @param basePrecision
    *           the basePrecision to set
    */
   public void setBasePrecision (int basePrecision) {
      this.basePrecision = basePrecision;
   }

   /**
    * @return the baseScale
    */
   public int getBaseScale () {
      return baseScale;
   }

   /**
    * @param baseScale
    *           the baseScale to set
    */
   public void setBaseScale (int baseScale) {
      this.baseScale = baseScale;
   }

   /**
    * @return the columnIndex
    */
   public int getColumnIndex () {
      return columnIndex;
   }

   /**
    * @param columnIndex
    *           the columnIndex to set
    */
   public void setColumnIndex (int columnIndex) {
      this.columnIndex = columnIndex;
   }

   /**
    * @return the qiConversion
    */
   public QIConversion getQiConversion () {
      return qiConversion;
   }

   /**
    * @param qiConversion
    *           the qiConversion to set
    */
   public void setQiConversion (QIConversion qiConversion) {
      this.qiConversion = qiConversion;
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

   public CheckType getDefaultMetric () {
      return defaultMetric;
   }

   public void setDefaultMetric (CheckType defaultMetric) {
      this.defaultMetric = defaultMetric;
   }

}
