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

/**
 * This bean represents the table structure of fractional population table. It contains the sampled population.
 * 
 * @author Vishay
 * @version 1.0
 * @since 14/01/2011
 */
public class MartFractionalPopulationTableStructure {

   private String      mergedTempTableName;
   private String      mergedTableName;
   private QueryColumn populationColumn;
   private QueryColumn sfactorColumn;
   private Long        populationCount;

   public String getMergedTempTableName () {
      return mergedTempTableName;
   }

   public void setMergedTempTableName (String mergedTempTableName) {
      this.mergedTempTableName = mergedTempTableName;
   }

   public String getMergedTableName () {
      return mergedTableName;
   }

   public void setMergedTableName (String mergedTableName) {
      this.mergedTableName = mergedTableName;
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

   public Long getPopulationCount () {
      return populationCount;
   }

   public void setPopulationCount (Long populationCount) {
      this.populationCount = populationCount;
   }
}
