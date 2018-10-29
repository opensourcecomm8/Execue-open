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

import java.util.ArrayList;
import java.util.List;

import com.execue.core.common.bean.querygen.QueryColumn;

/**
 * This bean represents the fact table structure for cube
 * 
 * @author Vishay
 * @version 1.0
 * @since 22/01/2011
 */
public class CubeFactTableStructure {

   private String                        tableName;
   private List<CubeSourceColumnMapping> frequencyMeasureColumns;
   private List<CubeSourceColumnMapping> simpleLookupColumns;
   private List<CubeSourceColumnMapping> rangeLookupColumns;
   private QueryColumn                   statColumn;
   private List<CubeSourceColumnMapping> measureColumns;

   public String getTableName () {
      return tableName;
   }

   public void setTableName (String tableName) {
      this.tableName = tableName;
   }

   public List<CubeSourceColumnMapping> getFrequencyMeasureColumns () {
      return frequencyMeasureColumns;
   }

   public void setFrequencyMeasureColumns (List<CubeSourceColumnMapping> frequencyMeasureColumns) {
      this.frequencyMeasureColumns = frequencyMeasureColumns;
   }

   public List<CubeSourceColumnMapping> getSimpleLookupColumns () {
      return simpleLookupColumns;
   }

   public void setSimpleLookupColumns (List<CubeSourceColumnMapping> simpleLookupColumns) {
      this.simpleLookupColumns = simpleLookupColumns;
   }

   public List<CubeSourceColumnMapping> getRangeLookupColumns () {
      return rangeLookupColumns;
   }

   public void setRangeLookupColumns (List<CubeSourceColumnMapping> rangeLookupColumns) {
      this.rangeLookupColumns = rangeLookupColumns;
   }

   public QueryColumn getStatColumn () {
      return statColumn;
   }

   public void setStatColumn (QueryColumn statColumn) {
      this.statColumn = statColumn;
   }

   public List<CubeSourceColumnMapping> getMeasureColumns () {
      return measureColumns;
   }

   public void setMeasureColumns (List<CubeSourceColumnMapping> measureColumns) {
      this.measureColumns = measureColumns;
   }

   public List<CubeSourceColumnMapping> getCombinedColumnsExceptStat () {
      List<CubeSourceColumnMapping> combinedColumnsExceptStat = new ArrayList<CubeSourceColumnMapping>();
      combinedColumnsExceptStat.addAll(getFrequencyMeasureColumns());
      combinedColumnsExceptStat.addAll(getSimpleLookupColumns());
      combinedColumnsExceptStat.addAll(getRangeLookupColumns());
      combinedColumnsExceptStat.addAll(getMeasureColumns());
      return combinedColumnsExceptStat;
   }

   public List<CubeSourceColumnMapping> getCombinedColumnsExceptFrequencyMeasuresAndStat () {
      List<CubeSourceColumnMapping> combinedColumnsExceptFrequencyMeasuresAndStat = new ArrayList<CubeSourceColumnMapping>();
      combinedColumnsExceptFrequencyMeasuresAndStat.addAll(getSimpleLookupColumns());
      combinedColumnsExceptFrequencyMeasuresAndStat.addAll(getRangeLookupColumns());
      combinedColumnsExceptFrequencyMeasuresAndStat.addAll(getMeasureColumns());
      return combinedColumnsExceptFrequencyMeasuresAndStat;
   }

   public List<QueryColumn> getCombinedLookupColumns () {
      List<QueryColumn> lookupColumns = new ArrayList<QueryColumn>();
      for (CubeSourceColumnMapping cubeSourceColumnMapping : getSimpleLookupColumns()) {
         lookupColumns.add(cubeSourceColumnMapping.getQueryColumn());
      }
      for (CubeSourceColumnMapping cubeSourceColumnMapping : getRangeLookupColumns()) {
         lookupColumns.add(cubeSourceColumnMapping.getQueryColumn());
      }
      lookupColumns.add(getStatColumn());
      return lookupColumns;
   }

   public List<CubeSourceColumnMapping> getCombinedLookupColumnsExceptStat () {
      List<CubeSourceColumnMapping> combinedLookupColumnsExceptStat = new ArrayList<CubeSourceColumnMapping>();
      combinedLookupColumnsExceptStat.addAll(getSimpleLookupColumns());
      combinedLookupColumnsExceptStat.addAll(getRangeLookupColumns());
      return combinedLookupColumnsExceptStat;
   }

   public List<CubeSourceColumnMapping> getCombinedColumnsExceptLookupAndStat () {
      List<CubeSourceColumnMapping> combinedColumnsExceptLookupAndStat = new ArrayList<CubeSourceColumnMapping>();
      combinedColumnsExceptLookupAndStat.addAll(getFrequencyMeasureColumns());
      combinedColumnsExceptLookupAndStat.addAll(getMeasureColumns());
      return combinedColumnsExceptLookupAndStat;
   }

}
