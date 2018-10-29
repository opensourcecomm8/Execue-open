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
 * This bean represents the table structure of fractional table for each dimension.
 * 
 * @author Vishay
 * @version 1.0
 * @since 14/01/2011
 */
public class MartFractionalDatasetTableStructure {

   private String                                  tableName;
   private QueryColumn                             populationColumn;
   private QueryColumn                             sfactorColumn;
   private SelectEntity                            populationSelectEntity;
   private SelectEntity                            sfactorSelectEntity;
   private MartFractionalDataSetTempTableStructure martFractionalDataSetTempTableStructure;

   public String getTableName () {
      return tableName;
   }

   public void setTableName (String tableName) {
      this.tableName = tableName;
   }

   public QueryColumn getPopulationColumn () {
      return populationColumn;
   }

   public void setPopulationColumn (QueryColumn populationColumn) {
      this.populationColumn = populationColumn;
   }

   public QueryColumn getSfactorColumn () {
      return sfactorColumn;
   }

   public void setSfactorColumn (QueryColumn sfactorColumn) {
      this.sfactorColumn = sfactorColumn;
   }

   public MartFractionalDataSetTempTableStructure getMartFractionalDataSetTempTableStructure () {
      return martFractionalDataSetTempTableStructure;
   }

   public void setMartFractionalDataSetTempTableStructure (
            MartFractionalDataSetTempTableStructure martFractionalDataSetTempTableStructure) {
      this.martFractionalDataSetTempTableStructure = martFractionalDataSetTempTableStructure;
   }

   public SelectEntity getPopulationSelectEntity () {
      return populationSelectEntity;
   }

   public void setPopulationSelectEntity (SelectEntity populationSelectEntity) {
      this.populationSelectEntity = populationSelectEntity;
   }

   public SelectEntity getSfactorSelectEntity () {
      return sfactorSelectEntity;
   }

   public void setSfactorSelectEntity (SelectEntity sfactorSelectEntity) {
      this.sfactorSelectEntity = sfactorSelectEntity;
   }
}
