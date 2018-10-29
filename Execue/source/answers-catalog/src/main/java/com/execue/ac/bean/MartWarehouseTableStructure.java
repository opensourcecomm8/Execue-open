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
import java.util.Map;

import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.Tabl;
import com.execue.core.common.bean.querygen.QueryColumn;

/**
 * This bean represents the table structure for mart corresponding to source table.
 * 
 * @author Vishay
 * @version 1.0
 * @since 24/01/2011
 */
public class MartWarehouseTableStructure {

   private Tabl                     sourceTable;
   private List<Colum>              sourceColumns;
   private String                   martTableName;
   private List<QueryColumn>        martTableColumns;
   private Map<String, QueryColumn> sourceMartColumnMap;
   private boolean                  populationTable = false;
   private QueryColumn              populationSfactorColumn;
   private boolean                  indexCreated    = false;

   public QueryColumn getPopulationSfactorColumn () {
      return populationSfactorColumn;
   }

   public void setPopulationSfactorColumn (QueryColumn populationSfactorColumn) {
      this.populationSfactorColumn = populationSfactorColumn;
   }

   public Tabl getSourceTable () {
      return sourceTable;
   }

   public void setSourceTable (Tabl sourceTable) {
      this.sourceTable = sourceTable;
   }

   public List<Colum> getSourceColumns () {
      return sourceColumns;
   }

   public void setSourceColumns (List<Colum> sourceColumns) {
      this.sourceColumns = sourceColumns;
   }

   public String getMartTableName () {
      return martTableName;
   }

   public void setMartTableName (String martTableName) {
      this.martTableName = martTableName;
   }

   public List<QueryColumn> getMartTableColumns () {
      return martTableColumns;
   }

   public void setMartTableColumns (List<QueryColumn> martTableColumns) {
      this.martTableColumns = martTableColumns;
   }

   public Map<String, QueryColumn> getSourceMartColumnMap () {
      return sourceMartColumnMap;
   }

   public void setSourceMartColumnMap (Map<String, QueryColumn> sourceMartColumnMap) {
      this.sourceMartColumnMap = sourceMartColumnMap;
   }

   public boolean isPopulationTable () {
      return populationTable;
   }

   public void setPopulationTable (boolean populationTable) {
      this.populationTable = populationTable;
   }

   public boolean isIndexCreated () {
      return indexCreated;
   }

   public void setIndexCreated (boolean indexCreated) {
      this.indexCreated = indexCreated;
   }

}
