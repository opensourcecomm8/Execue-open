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


package com.execue.ac.bean;

import java.util.List;

import com.execue.core.common.bean.querygen.QueryColumn;
import com.execue.core.common.type.DataType;
import com.execue.core.common.type.StatType;

/**
 * This bean contains the information needed for creating cube from configuration.
 * 
 * @author Vishay
 * @version 1.0
 * @since 17/02/2011
 */
public class CubeConfigurationContext {

   private DataType       dimensionDataType;
   private Integer        minDimensionPrecision;
   private String         frequencyColumnPrefix;
   private String         simpleLookupColumnPrefix;
   private String         rangeLookupColumnPrefix;
   private QueryColumn    queryIdColumn;
   private QueryColumn    statColumn;
   private QueryColumn    rangeLookupLowerLimitColumn;
   private QueryColumn    rangeLookupUpperLimitColumn;
   private String         descriptionColumnSuffix;
   private DataType       descriptionColumnDataType;
   private Integer        descriptionColumnPrecision;
   private String         cubeFactTablePrefix;
   private String         cubePreFactTableSuffix;
   private String         cubeAllValueRepresentation;
   private List<StatType> applicableStats;
   private Integer        dataTransferQueriesExecutionThreadPoolSize;
   private Integer        threadWaitTime;
   private boolean        cleanTemporaryResources;

   public String getCubeAllValueRepresentation () {
      return cubeAllValueRepresentation;
   }

   public void setCubeAllValueRepresentation (String cubeAllValueRepresentation) {
      this.cubeAllValueRepresentation = cubeAllValueRepresentation;
   }

   public List<StatType> getApplicableStats () {
      return applicableStats;
   }

   public void setApplicableStats (List<StatType> applicableStats) {
      this.applicableStats = applicableStats;
   }

   public DataType getDimensionDataType () {
      return dimensionDataType;
   }

   public void setDimensionDataType (DataType dimensionDataType) {
      this.dimensionDataType = dimensionDataType;
   }

   public Integer getMinDimensionPrecision () {
      return minDimensionPrecision;
   }

   public void setMinDimensionPrecision (Integer minDimensionPrecision) {
      this.minDimensionPrecision = minDimensionPrecision;
   }

   public String getFrequencyColumnPrefix () {
      return frequencyColumnPrefix;
   }

   public void setFrequencyColumnPrefix (String frequencyColumnPrefix) {
      this.frequencyColumnPrefix = frequencyColumnPrefix;
   }

   public String getSimpleLookupColumnPrefix () {
      return simpleLookupColumnPrefix;
   }

   public void setSimpleLookupColumnPrefix (String simpleLookupColumnPrefix) {
      this.simpleLookupColumnPrefix = simpleLookupColumnPrefix;
   }

   public String getRangeLookupColumnPrefix () {
      return rangeLookupColumnPrefix;
   }

   public void setRangeLookupColumnPrefix (String rangeLookupColumnPrefix) {
      this.rangeLookupColumnPrefix = rangeLookupColumnPrefix;
   }

   public QueryColumn getQueryIdColumn () {
      return queryIdColumn;
   }

   public void setQueryIdColumn (QueryColumn queryIdColumn) {
      this.queryIdColumn = queryIdColumn;
   }

   public String getDescriptionColumnSuffix () {
      return descriptionColumnSuffix;
   }

   public void setDescriptionColumnSuffix (String descriptionColumnSuffix) {
      this.descriptionColumnSuffix = descriptionColumnSuffix;
   }

   public String getCubeFactTablePrefix () {
      return cubeFactTablePrefix;
   }

   public void setCubeFactTablePrefix (String cubeFactTablePrefix) {
      this.cubeFactTablePrefix = cubeFactTablePrefix;
   }

   public String getCubePreFactTableSuffix () {
      return cubePreFactTableSuffix;
   }

   public void setCubePreFactTableSuffix (String cubePreFactTableSuffix) {
      this.cubePreFactTableSuffix = cubePreFactTableSuffix;
   }

   public QueryColumn getStatColumn () {
      return statColumn;
   }

   public void setStatColumn (QueryColumn statColumn) {
      this.statColumn = statColumn;
   }

   public QueryColumn getRangeLookupLowerLimitColumn () {
      return rangeLookupLowerLimitColumn;
   }

   public void setRangeLookupLowerLimitColumn (QueryColumn rangeLookupLowerLimitColumn) {
      this.rangeLookupLowerLimitColumn = rangeLookupLowerLimitColumn;
   }

   public QueryColumn getRangeLookupUpperLimitColumn () {
      return rangeLookupUpperLimitColumn;
   }

   public void setRangeLookupUpperLimitColumn (QueryColumn rangeLookupUpperLimitColumn) {
      this.rangeLookupUpperLimitColumn = rangeLookupUpperLimitColumn;
   }

   public DataType getDescriptionColumnDataType () {
      return descriptionColumnDataType;
   }

   public void setDescriptionColumnDataType (DataType descriptionColumnDataType) {
      this.descriptionColumnDataType = descriptionColumnDataType;
   }

   public Integer getDescriptionColumnPrecision () {
      return descriptionColumnPrecision;
   }

   public void setDescriptionColumnPrecision (Integer descriptionColumnPrecision) {
      this.descriptionColumnPrecision = descriptionColumnPrecision;
   }

   public Integer getDataTransferQueriesExecutionThreadPoolSize () {
      return dataTransferQueriesExecutionThreadPoolSize;
   }

   public void setDataTransferQueriesExecutionThreadPoolSize (Integer dataTransferQueriesExecutionThreadPoolSize) {
      this.dataTransferQueriesExecutionThreadPoolSize = dataTransferQueriesExecutionThreadPoolSize;
   }

   public boolean isCleanTemporaryResources () {
      return cleanTemporaryResources;
   }

   public void setCleanTemporaryResources (boolean cleanTemporaryResources) {
      this.cleanTemporaryResources = cleanTemporaryResources;
   }

   public Integer getThreadWaitTime () {
      return this.threadWaitTime;
   }

   public void setThreadWaitTime (Integer threadWaitTime) {
      this.threadWaitTime = threadWaitTime;
   }
}
