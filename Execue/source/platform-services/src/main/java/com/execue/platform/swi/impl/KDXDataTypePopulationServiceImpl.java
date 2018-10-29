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


package com.execue.platform.swi.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.ExeCueResultSet;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.Tabl;
import com.execue.core.common.bean.querygen.FromEntity;
import com.execue.core.common.bean.querygen.Query;
import com.execue.core.common.bean.querygen.QueryColumn;
import com.execue.core.common.bean.querygen.QueryGenerationInput;
import com.execue.core.common.bean.querygen.QueryGenerationOutput;
import com.execue.core.common.bean.querygen.QueryTable;
import com.execue.core.common.bean.querygen.QueryTableColumn;
import com.execue.core.common.bean.querygen.SelectEntity;
import com.execue.core.common.bean.querygen.SelectQueryInfo;
import com.execue.core.common.type.ColumnType;
import com.execue.core.common.type.ConversionType;
import com.execue.core.common.type.DataType;
import com.execue.core.common.type.FromEntityType;
import com.execue.core.common.type.LookupType;
import com.execue.core.common.type.SelectEntityType;
import com.execue.core.common.type.StatType;
import com.execue.core.common.util.ExecueBeanManagementUtil;
import com.execue.core.configuration.ICoreConfigurationService;
import com.execue.das.jdbc.service.IGenericJDBCService;
import com.execue.dataaccess.exception.DataAccessExceptionCodes;
import com.execue.platform.swi.IKDXDataTypePopulationService;
import com.execue.querygen.service.IQueryGenerationService;
import com.execue.querygen.service.QueryGenerationServiceFactory;
import com.execue.swi.exception.SDXException;
import com.execue.swi.exception.SWIException;
import com.execue.swi.service.ISDXManagementService;
import com.execue.swi.service.ISDXRetrievalService;

public class KDXDataTypePopulationServiceImpl implements IKDXDataTypePopulationService {

   private ISDXRetrievalService          sdxRetrievalService;
   private ISDXManagementService         sdxManagementService;
   private IGenericJDBCService           genericJDBCService;
   private ICoreConfigurationService     coreConfigurationService;
   private QueryGenerationServiceFactory queryGenerationServiceFactory;
   private static final Logger           logger = Logger.getLogger(KDXDataTypePopulationServiceImpl.class);

   /**
    * This method will find the KDXDatatype for the colums based on constraints information
    */
   public void analyseKDXDataType (Long assetId) throws SWIException {
      Asset asset = getSdxRetrievalService().getAsset(assetId);
      int maxUniqueValues = getCoreConfigurationService().getDimensionTreshold();
      // if it is null, then only change for those colums.
      logger.debug("Getting the tables for an asset");
      List<Tabl> tablesOfAsset = getSdxRetrievalService().getAllTables(asset);
      // for all tables, initialize the KDX type of all colums to null if request is coming second time for update
      for (Tabl tabl : tablesOfAsset) {
         List<Colum> tableColums = getSdxRetrievalService().getColumnsOfTable(tabl);
         for (Colum colum : tableColums) {
            // set each colum kdx data type to null
            if (colum.getKdxDataType() == null) {
               colum.setKdxDataType(ColumnType.NULL);
               getSdxManagementService().updateColumn(assetId, tabl.getId(), colum);
            }
         }
      }
      List<Tabl> lookupTables = new ArrayList<Tabl>();
      for (Tabl tabl : tablesOfAsset) {
         if (!LookupType.None.equals(tabl.getLookupType())) {
            lookupTables.add(tabl);
         }
      }
      List<Tabl> factTables = new ArrayList<Tabl>(tablesOfAsset);
      factTables.removeAll(lookupTables);

      logger.debug("Size of Lookup tables " + lookupTables.size());
      logger.debug("Size of Fact tables " + factTables.size());

      for (Tabl lookupTable : lookupTables) {
         List<Colum> lookupTableColumns = getSdxRetrievalService().getColumnsOfTable(lookupTable);
         for (Colum lookupTableColumn : lookupTableColumns) {
            if (getSdxRetrievalService().isPartOfPrimaryKey(lookupTableColumn.getId())) {
               // set the kdx type for this colum to sl,rl,hl depends upon tabl type
               // get all the foreign keys and set all those colums to dimension
               ColumnType columnType = null;
               switch (lookupTable.getLookupType()) {
                  case SIMPLE_LOOKUP:
                     columnType = ColumnType.SIMPLE_LOOKUP;
                     break;
                  case RANGE_LOOKUP:
                     columnType = ColumnType.RANGE_LOOKUP;
                     break;
                  case SIMPLEHIERARCHICAL_LOOKUP:
                     break;
                  case RANGEHIERARCHICAL_LOOKUP:
                     break;
               }
               lookupTableColumn.setKdxDataType(columnType);
               getSdxManagementService().updateColumn(assetId, lookupTable.getId(), lookupTableColumn);
               List<Colum> foreignKeysForPrimaryKey = getSdxRetrievalService().getForeignKeysForPrimaryKey(
                        lookupTableColumn);
               for (Colum colum : foreignKeysForPrimaryKey) {
                  colum.setKdxDataType(ColumnType.DIMENSION);
                  getSdxManagementService().updateColumn(assetId, lookupTable.getId(), colum);
               }
            }
         }
      }

      for (Tabl factTable : factTables) {
         List<Colum> factTableColums = getSdxRetrievalService().getColumnsOfTable(factTable);
         for (Colum factTableColumn : factTableColums) {
            // TODO:-VG- as primary key implementation is wrong, once we realize that on new structure, we need
            // to fix this method
            // if (isPartOfPrimaryKey(factTableColumn.getId())) {
            // // set the colum kdx type to Id
            // factTableColumn.setKdxDataType(ColumnType.ID);
            // update(factTableColumn);
            // } else {
            // if colum KDX type is null, set it to measure
            if (ColumnType.NULL.equals(factTableColumn.getKdxDataType())
                     && !ConversionType.DATE.equals(factTableColumn.getConversionType())) {
               // check colum type if it is decimal then set its kdx type to measure
               if (DataType.NUMBER.equals(factTableColumn.getDataType())) {
                  factTableColumn.setKdxDataType(ColumnType.MEASURE);
                  getSdxManagementService().updateColumn(assetId, factTable.getId(), factTableColumn);
               } else if (DataType.INT.equals(factTableColumn.getDataType())
                        || DataType.LARGE_INTEGER.equals(factTableColumn.getDataType())) {
                  // put as measure or deduced dimension by running the counts query
                  int count = getColumnCount(asset, factTable, factTableColumn, true);
                  int totalCount = getColumnCount(asset, factTable, factTableColumn, false);
                  if (count == totalCount) {
                     factTableColumn.setKdxDataType(ColumnType.ID);
                     getSdxManagementService().updateColumn(assetId, factTable.getId(), factTableColumn);
                  }

                  // TODO : -RG- This particular condition might be damaging
                  // Need to analyze further
                  else if (count >= maxUniqueValues) {
                     factTableColumn.setKdxDataType(ColumnType.MEASURE);
                     getSdxManagementService().updateColumn(assetId, factTable.getId(), factTableColumn);
                  }

                  else {
                     factTableColumn.setKdxDataType(ColumnType.NULL);
                     getSdxManagementService().updateColumn(assetId, factTable.getId(), factTableColumn);
                  }
               } else {
                  factTableColumn.setKdxDataType(ColumnType.NULL);
                  getSdxManagementService().updateColumn(assetId, factTable.getId(), factTableColumn);
               }
            }
         }
         // }
      }
   }

   private int getColumnCount (Asset asset, Tabl factTable, Colum factTableColumn, boolean distinct)
            throws SDXException {
      try {
         // StringBuilder queryString = new StringBuilder();
         Query query = new Query();
         List<SelectEntity> selectEntities = new ArrayList<SelectEntity>();
         List<FromEntity> fromEntities = new ArrayList<FromEntity>();
         SelectEntity selectEntity = new SelectEntity();
         selectEntity.setFunctionName(StatType.COUNT);
         selectEntity.setType(SelectEntityType.TABLE_COLUMN);
         selectEntity.setTableColumn(prepareQueryTableColum(factTableColumn, distinct));
         selectEntities.add(selectEntity);
         fromEntities.add(prepareFromEntity(factTable));
         query.setSelectEntities(selectEntities);
         query.setFromEntities(fromEntities);
         /*
          * queryString.append("SELECT").append(" ").append(StatType.COUNT).append("(").append("DISTINCT").append(" ")
          * .append(factTableColumn.getName()).append(")").append(" ").append("FROM").append(" ").append(
          * factTable.getName());
          */
         QueryGenerationInput queryGenerationInput = new QueryGenerationInput();
         queryGenerationInput.setTargetAsset(asset);
         List<Query> queries = new ArrayList<Query>();
         queries.add(query);
         queryGenerationInput.setInputQueries(queries);
         QueryGenerationOutput queryGenerationOutput = getQueryGenerationService(asset).generateQuery(
                  queryGenerationInput);
         String selectStatement = getQueryGenerationService(asset).extractQueryRepresentation(asset,
                  queryGenerationOutput.getResultQuery()).getQueryString();
         List<Integer> countColumnIndexes = new ArrayList<Integer>();
         countColumnIndexes.add(0);
         ExeCueResultSet execueResultSet = getGenericJDBCService().executeQuery(asset.getDataSource().getName(),
                  new SelectQueryInfo(selectStatement, countColumnIndexes));

         String count = "0";
         while (execueResultSet.next()) {
            count = execueResultSet.getString(0);
         }
         return Integer.parseInt(count);
      } catch (Exception exception) {
         throw new SDXException(DataAccessExceptionCodes.COLUMN_COUNT_RETRIEVAL_FAILED, exception);
      }
   }

   private QueryTableColumn prepareQueryTableColum (Colum colum, boolean distinct) {
      // prepare QueryTablecolum object from colum object
      QueryTableColumn queryTableColumn = new QueryTableColumn();
      QueryTable queryTable = new QueryTable();
      
      QueryColumn queryColumn = ExecueBeanManagementUtil.prepareQueryColumn(colum);
      queryColumn.setDistinct(distinct);
      
      queryTable.setTableName(colum.getOwnerTable().getName());
      queryTable.setAlias(colum.getOwnerTable().getAlias());
      queryTableColumn.setColumn(queryColumn);
      queryTableColumn.setTable(queryTable);
      return queryTableColumn;
   }

   private FromEntity prepareFromEntity (Tabl factTable) {
      FromEntity fromEntity = new FromEntity();
      fromEntity.setType(FromEntityType.TABLE);
      QueryTable table = new QueryTable();
      table.setTableName(factTable.getName());
      table.setAlias(factTable.getAlias());
      fromEntity.setTable(table);
      return fromEntity;
   }

   public QueryGenerationServiceFactory getQueryGenerationServiceFactory () {
      return queryGenerationServiceFactory;
   }

   private IQueryGenerationService getQueryGenerationService (Asset asset) {
      return getQueryGenerationServiceFactory().getQueryGenerationService(asset);
   }

   public void setQueryGenerationServiceFactory (QueryGenerationServiceFactory queryGenerationServiceFactory) {
      this.queryGenerationServiceFactory = queryGenerationServiceFactory;
   }

   public ISDXRetrievalService getSdxRetrievalService () {
      return sdxRetrievalService;
   }

   public void setSdxRetrievalService (ISDXRetrievalService sdxRetrievalService) {
      this.sdxRetrievalService = sdxRetrievalService;
   }

   public ISDXManagementService getSdxManagementService () {
      return sdxManagementService;
   }

   public void setSdxManagementService (ISDXManagementService sdxManagementService) {
      this.sdxManagementService = sdxManagementService;
   }

   public IGenericJDBCService getGenericJDBCService () {
      return genericJDBCService;
   }

   public void setGenericJDBCService (IGenericJDBCService genericJDBCService) {
      this.genericJDBCService = genericJDBCService;
   }

   /**
    * @return the coreConfigurationService
    */
   public ICoreConfigurationService getCoreConfigurationService () {
      return coreConfigurationService;
   }

   /**
    * @param coreConfigurationService
    *           the coreConfigurationService to set
    */
   public void setCoreConfigurationService (ICoreConfigurationService coreConfigurationService) {
      this.coreConfigurationService = coreConfigurationService;
   }

}
