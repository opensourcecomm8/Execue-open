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
import com.execue.core.common.bean.querygen.SelectEntity;

/**
 * This bean represents the table structure of temp fractional table for each dimension.
 * 
 * @author Vishay
 * @version 1.0
 * @since 14/01/2011
 */
public class MartFractionalDataSetTempTableStructure {

   private String             tableName;
   private QueryColumn        idColumn;
   private QueryColumn        populationColumn;
   private QueryColumn        dimensionColumn;
   private List<QueryColumn>  distributionColumns;
   private List<QueryColumn>  statMeasureColumns;
   private QueryColumn        sliceCountColumn;
   private SelectEntity       populationSelectEntity;
   private SelectEntity       dimensionSelectEntity;
   private List<SelectEntity> distributionSelectEntities;
   private List<SelectEntity> statMeasureSelectEntities;
   private SelectEntity       sliceCountSelectEntity;
   private boolean            usedExistingCube;

   public String getTableName () {
      return tableName;
   }

   public void setTableName (String tableName) {
      this.tableName = tableName;
   }

   public QueryColumn getIdColumn () {
      return idColumn;
   }

   public void setIdColumn (QueryColumn idColumn) {
      this.idColumn = idColumn;
   }

   public QueryColumn getPopulationColumn () {
      return populationColumn;
   }

   public void setPopulationColumn (QueryColumn populationColumn) {
      this.populationColumn = populationColumn;
   }

   public QueryColumn getDimensionColumn () {
      return dimensionColumn;
   }

   public void setDimensionColumn (QueryColumn dimensionColumn) {
      this.dimensionColumn = dimensionColumn;
   }

   public List<QueryColumn> getDistributionColumns () {
      return distributionColumns;
   }

   public void setDistributionColumns (List<QueryColumn> distributionColumns) {
      this.distributionColumns = distributionColumns;
   }

   public List<QueryColumn> getStatMeasureColumns () {
      return statMeasureColumns;
   }

   public void setStatMeasureColumns (List<QueryColumn> statMeasureColumns) {
      this.statMeasureColumns = statMeasureColumns;
   }

   public QueryColumn getSliceCountColumn () {
      return sliceCountColumn;
   }

   public void setSliceCountColumn (QueryColumn sliceCountColumn) {
      this.sliceCountColumn = sliceCountColumn;
   }

   public SelectEntity getPopulationSelectEntity () {
      return populationSelectEntity;
   }

   public void setPopulationSelectEntity (SelectEntity populationSelectEntity) {
      this.populationSelectEntity = populationSelectEntity;
   }

   public SelectEntity getDimensionSelectEntity () {
      return dimensionSelectEntity;
   }

   public void setDimensionSelectEntity (SelectEntity dimensionSelectEntity) {
      this.dimensionSelectEntity = dimensionSelectEntity;
   }

   public List<SelectEntity> getDistributionSelectEntities () {
      return distributionSelectEntities;
   }

   public void setDistributionSelectEntities (List<SelectEntity> distributionSelectEntities) {
      this.distributionSelectEntities = distributionSelectEntities;
   }

   public List<SelectEntity> getStatMeasureSelectEntities () {
      return statMeasureSelectEntities;
   }

   public void setStatMeasureSelectEntities (List<SelectEntity> statMeasureSelectEntities) {
      this.statMeasureSelectEntities = statMeasureSelectEntities;
   }

   public SelectEntity getSliceCountSelectEntity () {
      return sliceCountSelectEntity;
   }

   public void setSliceCountSelectEntity (SelectEntity sliceCountSelectEntity) {
      this.sliceCountSelectEntity = sliceCountSelectEntity;
   }

   public boolean isUsedExistingCube () {
      return usedExistingCube;
   }

   public void setUsedExistingCube (boolean usedExistingCube) {
      this.usedExistingCube = usedExistingCube;
   }

}
