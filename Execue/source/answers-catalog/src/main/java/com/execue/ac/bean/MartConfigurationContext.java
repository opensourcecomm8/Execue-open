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
import com.execue.core.common.type.StatType;

/**
 * This bean contains the information needed for creating mart from configuration.
 * 
 * @author Vishay
 * @version 1.0
 * @since 11/01/2011
 */
public class MartConfigurationContext {

   private boolean        randomNumberGeneratorDBApproach;
   private String         scalingFactorConceptName;
   private List<StatType> applicableStats;

   private String         populationTableName;
   private QueryColumn    populationTableSliceNumberColumn;
   private QueryColumn    populationTableIdColumn;

   private String         fractionalTempTableNotation;
   private QueryColumn    fractionalTempSliceCountColumn;
   private QueryColumn    fractionalTempIdColumn;

   private String         fractionalTableNotation;
   private QueryColumn    fractionalTableSfactorColumn;

   private String         fractionalPopulationTempTableName;
   private String         fractionalPopulationTableName;

   private Integer        warehouseExtractionThreadPoolSize;
   private boolean        splitBatchDataTransferToAvoidSubConditions;
   private Integer        batchDataTransferThreadPoolSize;
   private Integer        fractionalTablePopulationThreadPoolSize;
   private boolean        cleanTemporaryResources;
   private Integer        maxTasksAllowedPerThreadPool;
   private Integer        threadWaitTime;

   public boolean isRandomNumberGeneratorDBApproach () {
      return randomNumberGeneratorDBApproach;
   }

   public void setRandomNumberGeneratorDBApproach (boolean randomNumberGeneratorDBApproach) {
      this.randomNumberGeneratorDBApproach = randomNumberGeneratorDBApproach;
   }

   public String getPopulationTableName () {
      return populationTableName;
   }

   public void setPopulationTableName (String populationTableName) {
      this.populationTableName = populationTableName;
   }

   public QueryColumn getPopulationTableSliceNumberColumn () {
      return populationTableSliceNumberColumn;
   }

   public void setPopulationTableSliceNumberColumn (QueryColumn populationTableSliceNumberColumn) {
      this.populationTableSliceNumberColumn = populationTableSliceNumberColumn;
   }

   public QueryColumn getPopulationTableIdColumn () {
      return populationTableIdColumn;
   }

   public void setPopulationTableIdColumn (QueryColumn populationTableIdColumn) {
      this.populationTableIdColumn = populationTableIdColumn;
   }

   public String getFractionalTempTableNotation () {
      return fractionalTempTableNotation;
   }

   public void setFractionalTempTableNotation (String fractionalTempTableNotation) {
      this.fractionalTempTableNotation = fractionalTempTableNotation;
   }

   public QueryColumn getFractionalTempSliceCountColumn () {
      return fractionalTempSliceCountColumn;
   }

   public void setFractionalTempSliceCountColumn (QueryColumn fractionalTempSliceCountColumn) {
      this.fractionalTempSliceCountColumn = fractionalTempSliceCountColumn;
   }

   public QueryColumn getFractionalTempIdColumn () {
      return fractionalTempIdColumn;
   }

   public void setFractionalTempIdColumn (QueryColumn fractionalTempIdColumn) {
      this.fractionalTempIdColumn = fractionalTempIdColumn;
   }

   public String getFractionalTableNotation () {
      return fractionalTableNotation;
   }

   public void setFractionalTableNotation (String fractionalTableNotation) {
      this.fractionalTableNotation = fractionalTableNotation;
   }

   public QueryColumn getFractionalTableSfactorColumn () {
      return fractionalTableSfactorColumn;
   }

   public void setFractionalTableSfactorColumn (QueryColumn fractionalTableSfactorColumn) {
      this.fractionalTableSfactorColumn = fractionalTableSfactorColumn;
   }

   public String getFractionalPopulationTableName () {
      return fractionalPopulationTableName;
   }

   public void setFractionalPopulationTableName (String fractionalPopulationTableName) {
      this.fractionalPopulationTableName = fractionalPopulationTableName;
   }

   public String getFractionalPopulationTempTableName () {
      return fractionalPopulationTempTableName;
   }

   public void setFractionalPopulationTempTableName (String fractionalPopulationTempTableName) {
      this.fractionalPopulationTempTableName = fractionalPopulationTempTableName;
   }

   public String getScalingFactorConceptName () {
      return scalingFactorConceptName;
   }

   public void setScalingFactorConceptName (String scalingFactorConceptName) {
      this.scalingFactorConceptName = scalingFactorConceptName;
   }

   public List<StatType> getApplicableStats () {
      return applicableStats;
   }

   public void setApplicableStats (List<StatType> applicableStats) {
      this.applicableStats = applicableStats;
   }

   public Integer getWarehouseExtractionThreadPoolSize () {
      return warehouseExtractionThreadPoolSize;
   }

   public void setWarehouseExtractionThreadPoolSize (Integer warehouseExtractionThreadPoolSize) {
      this.warehouseExtractionThreadPoolSize = warehouseExtractionThreadPoolSize;
   }

   public boolean isSplitBatchDataTransferToAvoidSubConditions () {
      return splitBatchDataTransferToAvoidSubConditions;
   }

   public void setSplitBatchDataTransferToAvoidSubConditions (boolean splitBatchDataTransferToAvoidSubConditions) {
      this.splitBatchDataTransferToAvoidSubConditions = splitBatchDataTransferToAvoidSubConditions;
   }

   public Integer getBatchDataTransferThreadPoolSize () {
      return batchDataTransferThreadPoolSize;
   }

   public void setBatchDataTransferThreadPoolSize (Integer batchDataTransferThreadPoolSize) {
      this.batchDataTransferThreadPoolSize = batchDataTransferThreadPoolSize;
   }

   public Integer getFractionalTablePopulationThreadPoolSize () {
      return fractionalTablePopulationThreadPoolSize;
   }

   public void setFractionalTablePopulationThreadPoolSize (Integer fractionalTablePopulationThreadPoolSize) {
      this.fractionalTablePopulationThreadPoolSize = fractionalTablePopulationThreadPoolSize;
   }

   public boolean isCleanTemporaryResources () {
      return cleanTemporaryResources;
   }

   public void setCleanTemporaryResources (boolean cleanTemporaryResources) {
      this.cleanTemporaryResources = cleanTemporaryResources;
   }

   public Integer getMaxTasksAllowedPerThreadPool () {
      return maxTasksAllowedPerThreadPool;
   }

   public void setMaxTasksAllowedPerThreadPool (Integer maxTasksAllowedPerThreadPool) {
      this.maxTasksAllowedPerThreadPool = maxTasksAllowedPerThreadPool;
   }

   public Integer getThreadWaitTime () {
      return this.threadWaitTime;
   }

   public void setThreadWaitTime (Integer threadWaitTime) {
      this.threadWaitTime = threadWaitTime;
   }
}