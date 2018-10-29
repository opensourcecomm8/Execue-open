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

import com.execue.core.common.bean.querygen.QueryColumn;
import com.execue.core.common.bean.querygen.SelectEntity;

/**
 * This bean represents the table structure of population table.
 * 
 * @author Vishay
 * @version 1.0
 * @since 14/01/2011
 */
public class MartPopulationTableStructure {

   private String       tableName;
   private QueryColumn  idColumn;
   private QueryColumn  populationColumn;
   private QueryColumn  sliceNumberColumn;
   private SelectEntity populationSelectEntity;
   private SelectEntity sliceNumberSelectEntity;
   private boolean      dbApproachForSliceNumber;

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

   public QueryColumn getSliceNumberColumn () {
      return sliceNumberColumn;
   }

   public void setSliceNumberColumn (QueryColumn sliceNumberColumn) {
      this.sliceNumberColumn = sliceNumberColumn;
   }

   public boolean isDbApproachForSliceNumber () {
      return dbApproachForSliceNumber;
   }

   public void setDbApproachForSliceNumber (boolean dbApproachForSliceNumber) {
      this.dbApproachForSliceNumber = dbApproachForSliceNumber;
   }

   public SelectEntity getPopulationSelectEntity () {
      return populationSelectEntity;
   }

   public void setPopulationSelectEntity (SelectEntity populationSelectEntity) {
      this.populationSelectEntity = populationSelectEntity;
   }

   public SelectEntity getSliceNumberSelectEntity () {
      return sliceNumberSelectEntity;
   }

   public void setSliceNumberSelectEntity (SelectEntity sliceNumberSelectEntity) {
      this.sliceNumberSelectEntity = sliceNumberSelectEntity;
   }

}
