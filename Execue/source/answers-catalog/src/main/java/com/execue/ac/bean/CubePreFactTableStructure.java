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
 * @author Vishay
 * @version 1.0
 * @since 22/01/2011
 */
public class CubePreFactTableStructure {

   private String             tableName;
   private List<QueryColumn>  frequencyMeasureColumns;
   private List<QueryColumn>  simpleLookupColumns;
   private List<QueryColumn>  rangeLookupColumns;
   private List<QueryColumn>  statMeasureColumns;
   private QueryColumn        queryIdColumn;
   private List<SelectEntity> frequencyMeasureSelectEntities;
   private List<SelectEntity> simpleLookupSelectEntities;
   private List<SelectEntity> rangeLookupSelectEntities;
   private List<SelectEntity> statMeasureSelectEntities;
   private SelectEntity       queryIdSelectEntity;
   private Integer            queryIdCurrentValue = 1;

   /**
    * @return the tableName
    */
   public String getTableName () {
      return tableName;
   }

   /**
    * @param tableName
    *           the tableName to set
    */
   public void setTableName (String tableName) {
      this.tableName = tableName;
   }

   /**
    * @return the frequencyMeasureColumns
    */
   public List<QueryColumn> getFrequencyMeasureColumns () {
      return frequencyMeasureColumns;
   }

   /**
    * @param frequencyMeasureColumns
    *           the frequencyMeasureColumns to set
    */
   public void setFrequencyMeasureColumns (List<QueryColumn> frequencyMeasureColumns) {
      this.frequencyMeasureColumns = frequencyMeasureColumns;
   }

   /**
    * @return the simpleLookupColumns
    */
   public List<QueryColumn> getSimpleLookupColumns () {
      return simpleLookupColumns;
   }

   /**
    * @param simpleLookupColumns
    *           the simpleLookupColumns to set
    */
   public void setSimpleLookupColumns (List<QueryColumn> simpleLookupColumns) {
      this.simpleLookupColumns = simpleLookupColumns;
   }

   /**
    * @return the rangeLookupColumns
    */
   public List<QueryColumn> getRangeLookupColumns () {
      return rangeLookupColumns;
   }

   /**
    * @param rangeLookupColumns
    *           the rangeLookupColumns to set
    */
   public void setRangeLookupColumns (List<QueryColumn> rangeLookupColumns) {
      this.rangeLookupColumns = rangeLookupColumns;
   }

   /**
    * @return the statMeasureColumns
    */
   public List<QueryColumn> getStatMeasureColumns () {
      return statMeasureColumns;
   }

   /**
    * @param statMeasureColumns
    *           the statMeasureColumns to set
    */
   public void setStatMeasureColumns (List<QueryColumn> statMeasureColumns) {
      this.statMeasureColumns = statMeasureColumns;
   }

   /**
    * @return the queryIdColumn
    */
   public QueryColumn getQueryIdColumn () {
      return queryIdColumn;
   }

   /**
    * @param queryIdColumn
    *           the queryIdColumn to set
    */
   public void setQueryIdColumn (QueryColumn queryIdColumn) {
      this.queryIdColumn = queryIdColumn;
   }

   /**
    * @return the frequencyMeasureSelectEntities
    */
   public List<SelectEntity> getFrequencyMeasureSelectEntities () {
      return frequencyMeasureSelectEntities;
   }

   /**
    * @param frequencyMeasureSelectEntities
    *           the frequencyMeasureSelectEntities to set
    */
   public void setFrequencyMeasureSelectEntities (List<SelectEntity> frequencyMeasureSelectEntities) {
      this.frequencyMeasureSelectEntities = frequencyMeasureSelectEntities;
   }

   /**
    * @return the simpleLookupSelectEntities
    */
   public List<SelectEntity> getSimpleLookupSelectEntities () {
      return simpleLookupSelectEntities;
   }

   /**
    * @param simpleLookupSelectEntities
    *           the simpleLookupSelectEntities to set
    */
   public void setSimpleLookupSelectEntities (List<SelectEntity> simpleLookupSelectEntities) {
      this.simpleLookupSelectEntities = simpleLookupSelectEntities;
   }

   /**
    * @return the rangeLookupSelectEntities
    */
   public List<SelectEntity> getRangeLookupSelectEntities () {
      return rangeLookupSelectEntities;
   }

   /**
    * @param rangeLookupSelectEntities
    *           the rangeLookupSelectEntities to set
    */
   public void setRangeLookupSelectEntities (List<SelectEntity> rangeLookupSelectEntities) {
      this.rangeLookupSelectEntities = rangeLookupSelectEntities;
   }

   /**
    * @return the statMeasureSelectEntities
    */
   public List<SelectEntity> getStatMeasureSelectEntities () {
      return statMeasureSelectEntities;
   }

   /**
    * @param statMeasureSelectEntities
    *           the statMeasureSelectEntities to set
    */
   public void setStatMeasureSelectEntities (List<SelectEntity> statMeasureSelectEntities) {
      this.statMeasureSelectEntities = statMeasureSelectEntities;
   }

   /**
    * @return the queryIdSelectEntity
    */
   public SelectEntity getQueryIdSelectEntity () {
      return queryIdSelectEntity;
   }

   /**
    * @param queryIdSelectEntity
    *           the queryIdSelectEntity to set
    */
   public void setQueryIdSelectEntity (SelectEntity queryIdSelectEntity) {
      this.queryIdSelectEntity = queryIdSelectEntity;
   }

   public Integer getQueryIdCurrentValue () {
      return queryIdCurrentValue;
   }

   public void setQueryIdCurrentValue (Integer queryIdCurrentValue) {
      this.queryIdCurrentValue = queryIdCurrentValue;
   }

}
