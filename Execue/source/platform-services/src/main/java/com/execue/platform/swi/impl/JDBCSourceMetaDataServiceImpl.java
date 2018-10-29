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


/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */

package com.execue.platform.swi.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.SQLExeCueCachedResultSet;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.DataSource;
import com.execue.core.common.bean.entity.Membr;
import com.execue.core.common.bean.entity.Tabl;
import com.execue.core.common.bean.entity.wrapper.TableInfo;
import com.execue.core.common.bean.querygen.ConditionEntity;
import com.execue.core.common.bean.querygen.LimitEntity;
import com.execue.core.common.bean.querygen.OrderEntity;
import com.execue.core.common.bean.querygen.Query;
import com.execue.core.common.bean.querygen.QueryColumn;
import com.execue.core.common.bean.querygen.QueryGenerationInput;
import com.execue.core.common.bean.querygen.QueryGenerationOutput;
import com.execue.core.common.bean.querygen.QueryTable;
import com.execue.core.common.bean.querygen.QueryTableColumn;
import com.execue.core.common.bean.querygen.SelectEntity;
import com.execue.core.common.bean.querygen.SelectQueryInfo;
import com.execue.core.common.type.AssetProviderType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.ColumnType;
import com.execue.core.common.type.ConversionType;
import com.execue.core.common.type.DataSourceType;
import com.execue.core.common.type.DataType;
import com.execue.core.common.type.DateQualifier;
import com.execue.core.common.type.LookupType;
import com.execue.core.common.type.OperatorType;
import com.execue.core.common.type.OrderEntityType;
import com.execue.core.common.type.SelectEntityType;
import com.execue.core.common.type.StatType;
import com.execue.core.common.util.ExecueBeanManagementUtil;
import com.execue.core.common.util.ExecueBeanUtil;
import com.execue.core.configuration.ICoreConfigurationService;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.core.util.ExecueDateTimeUtils;
import com.execue.core.util.ExecueStringUtil;
import com.execue.das.jdbc.service.IGenericJDBCService;
import com.execue.dataaccess.AssetSourceConnectionProvider;
import com.execue.dataaccess.clientsource.IClientSourceDAO;
import com.execue.dataaccess.exception.AssetSourceConnectionException;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.dataaccess.exception.DataAccessExceptionCodes;
import com.execue.platform.exception.SourceMetaDataException;
import com.execue.platform.helper.JDBCSourceMetaDataServiceHelper;
import com.execue.platform.swi.ISourceMetaDataService;
import com.execue.querygen.service.IQueryGenerationService;
import com.execue.querygen.service.QueryGenerationServiceFactory;
import com.execue.swi.configuration.ISWIConfigurationService;
import com.execue.swi.exception.SWIException;
import com.execue.swi.service.IConversionService;
import com.execue.swi.service.ISDXRetrievalService;
import com.execue.util.conversion.timeframe.util.TimeFrameUtility;
import com.execue.util.querygen.IQueryGenerationUtilService;
import com.execue.util.querygen.QueryGenerationUtilServiceFactory;

/**
 * @author Raju Gottumukkala
 */
public class JDBCSourceMetaDataServiceImpl extends JDBCSourceMetaDataServiceHelper implements ISourceMetaDataService {

   private AssetSourceConnectionProvider     assetSourceConnectionProvider;

   private QueryGenerationServiceFactory     queryGenerationServiceFactory;

   private QueryGenerationUtilServiceFactory queryGenerationUtilServiceFactory;

   private ISWIConfigurationService          swiConfigurationService;

   private ICoreConfigurationService         coreConfigurationService;

   private ISDXRetrievalService              sdxRetrievalService;

   private IConversionService                conversionService;

   private IGenericJDBCService               genericJDBCService;

   private IClientSourceDAO                  clientSourceDAO;

   private Logger                            logger           = Logger.getLogger(JDBCSourceMetaDataServiceImpl.class);

   private static Map<Integer, String>       calendarMonthMap = new HashMap<Integer, String>();
   static {
      populateCalendarMonthMap(calendarMonthMap);
   }

   public List<Tabl> getTablesFromSource (DataSource dataSource, Long userId) throws SourceMetaDataException {
      logger.debug("Inside getTablesFromSource Method");
      List<Tabl> tables = new ArrayList<Tabl>();
      Connection connection = null;
      try {
         logger.debug("Trying to establish the Connection to data source : " + dataSource.getName());
         connection = getAssetSourceConnectionProvider().getConnection(dataSource.getName());
         DatabaseMetaData dbmd = connection.getMetaData();
         String[] type = { "TABLE" };
         ResultSet rs = null;
         switch (dataSource.getProviderType()) {
            case Oracle:
            case MySql:
               rs = dbmd.getTables(null, dbmd.getUserName(), null, type);
               break;
            case MSSql:
            case POSTGRES:
               rs = dbmd.getTables(null, null, null, type);
               break;
            case Teradata:
               rs = dbmd.getTables(null, dataSource.getOwner(), null, type);
               break;
            case SAS_SHARENET:
               rs = dbmd.getTables(null, dataSource.getOwner(), null, type);
               break;
            case SAS_WORKSPACE:
               rs = dbmd.getTables(null, dataSource.getOwner(), null, type);
               break;
            case DB2:
               rs = dbmd.getTables(null, dataSource.getOwner(), null, type);
               break;

         }
         while (rs.next()) {
            Tabl table = new Tabl();
            // table.setName(rs.getString("TABLE_NAME").trim());
            table.setName(ExecueStringUtil.trimSpaces(rs.getString("TABLE_NAME")));
            table.setOwner(ExecueStringUtil.trimSpaces(rs.getString("TABLE_SCHEM")));
            table.setDescription(ExecueStringUtil.trimSpaces(rs.getString("REMARKS")));
            table.setDisplayName(table.getName());
            tables.add(table);
         }
         return filterSourceTables(dataSource, userId, tables);
      } catch (AssetSourceConnectionException assetSourceConnectionException) {
         logger.error("Connection Refused");
         throw new SourceMetaDataException(DataAccessExceptionCodes.CONNECTION_CREATION_FAILED,
                  assetSourceConnectionException);
      } catch (SQLException sqlException) {
         logger.error("Table Retrieval Failed");
         throw new SourceMetaDataException(DataAccessExceptionCodes.META_DATA_EXTRACTION_FAILED, sqlException);
      } finally {
         try {
            getAssetSourceConnectionProvider().closeConnection(connection);
         } catch (AssetSourceConnectionException e) {
            logger.error("Connection Closed Error");
            throw new SourceMetaDataException(DataAccessExceptionCodes.RESOURCES_CLOSING_FAILED, e);
         }
      }
   }

   private List<Tabl> filterSourceTables (DataSource dataSource, Long userId, List<Tabl> tables) {
      List<Tabl> filteredTables = new ArrayList<Tabl>();
      // if data source is of type uploaded
      filteredTables = filterUploadedDataSourceTables(dataSource, userId, tables);
      // if provider type is oracle, then we need to filter recycle bin tables.
      filteredTables = filterOracleReycleBinTables(dataSource, filteredTables);
      return filteredTables;
   }

   private List<Tabl> filterOracleReycleBinTables (DataSource dataSource, List<Tabl> tables) {
      List<Tabl> filteredTables = new ArrayList<Tabl>();
      if (ExecueCoreUtil.isCollectionNotEmpty(tables) && AssetProviderType.Oracle.equals(dataSource.getProviderType())) {
         for (Tabl tabl : tables) {
            if (!tabl.getName().startsWith("BIN$")) {
               filteredTables.add(tabl);
            }
         }
      } else {
         filteredTables = tables;
      }
      return filteredTables;
   }

   private List<Tabl> filterUploadedDataSourceTables (DataSource dataSource, Long userId, List<Tabl> tables) {
      List<Tabl> filteredTables = new ArrayList<Tabl>();
      if (ExecueCoreUtil.isCollectionNotEmpty(tables) && DataSourceType.UPLOADED.equals(dataSource.getType())) {
         String prefixUploadedTableName = getCoreConfigurationService().getPrefixUploadedTableName() + "_" + userId
                  + "_";
         for (Tabl tabl : tables) {
            if (tabl.getName().startsWith(prefixUploadedTableName)) {
               filteredTables.add(tabl);
            }
         }
      } else {
         filteredTables = tables;
      }
      return filteredTables;
   }

   public TableInfo getTableFromSource (Asset asset, Tabl table) throws SourceMetaDataException {
      logger.debug("Inside getTableFromSource method");
      TableInfo tableMetaInfo = new TableInfo();
      logger.debug("Getting the colums from source for table : " + table.getName());
      tableMetaInfo.setColumns(getColumnsFromSource(asset.getDataSource(), table));
      logger.debug("Getting the members from source for table : " + table.getName());
      tableMetaInfo.setMembers(getMembersFromSource(asset, table, null));
      tableMetaInfo.setTable(table);
      return tableMetaInfo;
   }

   public List<Colum> getColumnsFromSource (DataSource dataSource, Tabl table) throws SourceMetaDataException {
      logger.debug("Inside getColumsFromSource method");
      List<Colum> columns = new ArrayList<Colum>();
      Connection connection = null;
      try {
         logger.debug("Trying to establish the Connection to data source : " + dataSource.getName());
         connection = getAssetSourceConnectionProvider().getConnection(dataSource.getName());
         logger.debug("Getting the Colums for table : " + table.getName());
         DatabaseMetaData dbmd = connection.getMetaData();
         ResultSet rsCols = null;
         switch (dataSource.getProviderType()) {
            case Oracle:
            case MySql:
               rsCols = dbmd.getColumns(null, dbmd.getUserName(), table.getName().toUpperCase(), null);
               break;
            case MSSql:
               rsCols = dbmd.getColumns(null, null, table.getName().toUpperCase(), null);
               break;
            case Teradata:
               rsCols = dbmd.getColumns(null, dataSource.getOwner(), table.getName().toUpperCase(), null);
               break;
            case SAS_SHARENET:
               rsCols = dbmd.getColumns(null, dataSource.getOwner(), table.getName().toUpperCase(), null);
               break;
            case SAS_WORKSPACE:
               rsCols = dbmd.getColumns(null, dataSource.getOwner(), table.getName().toUpperCase(), null);
               break;
            case DB2:
               rsCols = dbmd.getColumns(null, dataSource.getOwner(), table.getName().toUpperCase(), null);
            case POSTGRES:
               rsCols = dbmd.getColumns(null, null, table.getName(), null);

         }
         String tempColumnName = null;
         boolean scaleDefined = false;
         while (rsCols.next()) {
            Colum column = new Colum();
            column.setName(ExecueStringUtil.trimSpaces(rsCols.getString("COLUMN_NAME")));
            tempColumnName = ExecueStringUtil.trimSpaces(rsCols.getString("COLUMN_NAME"));
            column.setDisplayName(tempColumnName.replaceAll("_", " "));
            column.setDescription(ExecueStringUtil.trimSpaces(rsCols.getString("REMARKS")));
            String typeName = ExecueStringUtil.trimSpaces(rsCols.getString("TYPE_NAME"));
            if ((dataSource.getProviderType() == AssetProviderType.SAS_SHARENET || dataSource.getProviderType() == AssetProviderType.SAS_WORKSPACE)
                     && ExecueCoreUtil.isNotEmpty(typeName)) {

               if (typeName.startsWith("$")) {
                  int startIndex = typeName.indexOf("$");
                  int endIndex = typeName.indexOf(".");
                  String precision = typeName.substring(startIndex + 1, endIndex);
                  column.setPrecision(new Integer(precision));
               } else {
                  // NOTE: SAS type name comes as various formats eg: BESTw. and COMMAw.d i.e. BEST12. and COMMA12.2
                  // where w represents precision and d represents scale
                  int startIndex = 0;
                  int endIndex = typeName.length();
                  char[] tempCharArray = typeName.toCharArray();
                  for (int i = 0; i < tempCharArray.length; i++) {
                     if (Character.isLetter(tempCharArray[i])) {
                        startIndex = i + 1; // Assumption is next character will be digit
                     }
                     if (tempCharArray[i] == '.') {
                        endIndex = i;
                        break;
                     }
                  }
                  String precision = typeName.substring(startIndex, endIndex);
                  column.setPrecision(new Integer(precision));
                  if (typeName.length() > endIndex + 1) {
                     String scale = typeName.substring(endIndex + 1, typeName.length());
                     if (ExecueCoreUtil.isNotEmpty(scale)) {
                        int iDecimalDigits = Integer.valueOf(scale).intValue();
                        if (iDecimalDigits > 0) {
                           column.setScale(new Integer(iDecimalDigits).intValue());
                        }
                     }
                  }
               }
            } else {
               scaleDefined = false;
               column.setPrecision(new Integer(rsCols.getInt("COLUMN_SIZE")));
               Object scaleDefinition = rsCols.getObject("DECIMAL_DIGITS");
               if (scaleDefinition != null) {
                  scaleDefined = true;
                  column.setScale(new Integer(scaleDefinition.toString()).intValue());
               }
            }
            column.setRequired(CheckType.NO);
            column.setIsConstraintColum(CheckType.NO);
            column.setDataType(ExecueBeanUtil.getNormalizedDataType(rsCols.getInt("DATA_TYPE")));
            // This will try to correct the data type to Int if the column scale value is zero and the column data type
            // is Number
            if (scaleDefined) {
               correctColumnDataType(column);
            }
            column.setDefaultValue(ExecueStringUtil.trimSpaces(rsCols.getString("COLUMN_DEF")));
            String isNullable = ExecueStringUtil.trimSpaces(rsCols.getString("IS_NULLABLE"));
            if (isNullable.equals("NO")) {
               column.setNullable(false);
            } else {
               column.setNullable(true);
            }

            if (DataType.NUMBER == column.getDataType()) {
               column.setKdxDataType(ColumnType.MEASURE);
            } else {
               column.setKdxDataType(ColumnType.NULL);
            }

            columns.add(column);
         }
         updateColumnWithTableLookupType(table, columns);
      } catch (AssetSourceConnectionException e) {
         logger.error("Connection refused");
         throw new SourceMetaDataException(DataAccessExceptionCodes.CONNECTION_CREATION_FAILED, e);
      } catch (SQLException e) {
         logger.error("Columns Retrieval Failed");
         throw new SourceMetaDataException(DataAccessExceptionCodes.META_DATA_EXTRACTION_FAILED, e);
      } finally {
         try {
            getAssetSourceConnectionProvider().closeConnection(connection);
         } catch (AssetSourceConnectionException e) {
            logger.error("Connection Closed Error");
            throw new SourceMetaDataException(DataAccessExceptionCodes.RESOURCES_CLOSING_FAILED, e);
         }
      }
      return columns;
   }

   private void correctColumnDataType (Colum column) {
      // Check if the data type is number but scale is zero, then change the data type as DataType.INT
      // TODO: NK: Need to handle BIG INT
      if (column.getDataType() == DataType.NUMBER && column.getScale() == 0) {
         column.setDataType(DataType.INT);
      }
   }

   private void updateColumnWithTableLookupType (Tabl table, List<Colum> columns) {
      String lookupValueColumn = table.getLookupValueColumn();
      if (ExecueCoreUtil.isNotEmpty(lookupValueColumn)) {
         for (Colum colum : columns) {
            if (lookupValueColumn.equalsIgnoreCase(colum.getName())) {
               colum.setKdxDataType(ExecueBeanUtil.getColumnType(table.getLookupType()));
               break;
            }
         }
      }
   }

   public List<String> getMinMaxMemberValueFromSource (Asset asset, Tabl tabl, Colum colum)
            throws SourceMetaDataException {
      List<String> minMaxValues = new ArrayList<String>();
      try {
         Query query = createMinMaxMemberValueSQLStatement(asset, tabl, colum);
         QueryGenerationInput queryGenerationInput = new QueryGenerationInput();
         queryGenerationInput.setTargetAsset(asset);
         List<Query> queries = new ArrayList<Query>();
         queries.add(query);
         queryGenerationInput.setInputQueries(queries);
         QueryGenerationOutput queryGenerationOutput = getQueryGenerationService(asset).generateQuery(
                  queryGenerationInput);
         String selectStatement = getQueryGenerationService(asset).extractQueryRepresentation(asset,
                  queryGenerationOutput.getResultQuery()).getQueryString();
         logger.debug("JBBCSourceMetaData - Min Max Member Value Query :: " + selectStatement);

         SQLExeCueCachedResultSet rs = (SQLExeCueCachedResultSet) getClientSourceDAO().executeQuery(
                  asset.getDataSource(), query, selectStatement);
         // move to the record
         rs.next();
         if (DataType.DATE.equals(colum.getDataType()) || DataType.DATETIME.equals(colum.getDataType())) {
            minMaxValues.add(ExecueDateTimeUtils.getFormattedStringFromDate(rs.getDate(0), colum.getDataFormat()));
            minMaxValues.add(ExecueDateTimeUtils.getFormattedStringFromDate(rs.getDate(1), colum.getDataFormat()));
         } else {
            minMaxValues.add(rs.getString(0));
            minMaxValues.add(rs.getString(1));
         }
      } catch (DataAccessException dataAccessException) {
         dataAccessException.printStackTrace();
      } catch (Exception e) {
         e.printStackTrace();
      }
      return minMaxValues;
   }

   public Integer getMembersCount (Asset asset, Tabl table, Colum lookupColumn) throws SourceMetaDataException {
      logger.debug("Inside getMembersCount method");
      Integer membersCount = 0;
      try {
         QueryColumn queryColumn = ExecueBeanManagementUtil.prepareQueryColumn(lookupColumn);
         QueryTable queryTable = ExecueBeanManagementUtil.prepareQueryTable(table);
         SelectQueryInfo distinctCountSelectStatementIncludingNullRecords = getQueryGenerationUtilService(asset)
                  .createDistinctCountSelectStatementIncludingNullRecords(queryTable, queryColumn);
         if (logger.isDebugEnabled()) {
            logger.debug("Query: " + distinctCountSelectStatementIncludingNullRecords.getSelectQuery());
         }
         SQLExeCueCachedResultSet rs = null;
         rs = (SQLExeCueCachedResultSet) getGenericJDBCService().executeQuery(asset.getDataSource().getName(),
                  distinctCountSelectStatementIncludingNullRecords);
         while (rs.next()) {
            // NOTE: This conversion is required, because some db provider like SAS returns column data type as result
            // count type,
            // so needed to generalize the count behavior
            membersCount = Double.valueOf(rs.getObject(0).toString()).intValue();
         }
      } catch (DataAccessException dataAccessException) {
         dataAccessException.printStackTrace();
      } catch (Exception e) {
         e.printStackTrace();
      }
      return membersCount;
   }

   private Query createMinMaxMemberValueSQLStatement (Asset asset, Tabl table, Colum colum) {
      Query query = new Query();
      QueryTableColumn queryTableColumn = ExecueBeanManagementUtil.prepareQueryTableColumn(colum.getName(), colum
               .getDataType(), table.getAlias(), table.getName(), table.getActualName(), table.getVirtual(), table
               .getOwner(), table.getLookupType(), false);
      List<SelectEntity> selectEntities = new ArrayList<SelectEntity>();
      SelectEntity minMemberValueSelectEntity = new SelectEntity();
      minMemberValueSelectEntity.setType(SelectEntityType.TABLE_COLUMN);
      minMemberValueSelectEntity.setTableColumn(queryTableColumn);
      minMemberValueSelectEntity.setFunctionName(StatType.MINIMUM);
      selectEntities.add(minMemberValueSelectEntity);

      SelectEntity maxMemberValueSelectEntity = new SelectEntity();
      maxMemberValueSelectEntity.setType(SelectEntityType.TABLE_COLUMN);
      maxMemberValueSelectEntity.setTableColumn(queryTableColumn);
      maxMemberValueSelectEntity.setFunctionName(StatType.MAXIMUM);
      selectEntities.add(maxMemberValueSelectEntity);
      query.setSelectEntities(selectEntities);
      ConditionEntity notNullConditionEntity = ExecueBeanManagementUtil.prepareNotNullConditionEntity(queryTableColumn,
               OperatorType.IS_NOT_NULL);
      List<ConditionEntity> conditionEntities = new ArrayList<ConditionEntity>();
      conditionEntities.add(notNullConditionEntity);
      query.setWhereEntities(conditionEntities);
      return query;
   }

   public List<Membr> getMembersFromSource (Asset asset, Tabl table, LimitEntity limitEntity)
            throws SourceMetaDataException {
      logger.debug("Inside getMembersFromSource method");
      List<Membr> members = new ArrayList<Membr>();
      try {
         if (!LookupType.None.equals(table.getLookupType()) && table.getLookupType() != null) {
            String lookupValueColumn = table.getLookupValueColumn();
            String lookupDescColumn = table.getLookupDescColumn();

            if (CheckType.NO.equals(table.getVirtualTableDescColumnExistsOnSource())
                     && CheckType.YES.equals(table.getVirtual())
                     && lookupDescColumn.equalsIgnoreCase(lookupValueColumn
                              + getSwiConfigurationService().getVirtualLookupDescriptionColumnSuffix())) {
               lookupDescColumn = lookupValueColumn;
            }
            // Generate Members for Simple Lookup type
            if (LookupType.SIMPLE_LOOKUP.equals(table.getLookupType())) {
               logger.debug("Getting the members for simple lookup type");
               SelectQueryInfo memberRetrievalSQLStatementSL = createMemberRetrievalSQLStatement(asset, table,
                        limitEntity, lookupValueColumn, lookupDescColumn, null, null);
               SQLExeCueCachedResultSet rsForSL = null;
               rsForSL = (SQLExeCueCachedResultSet) getGenericJDBCService().executeQuery(
                        asset.getDataSource().getName(), memberRetrievalSQLStatementSL);
               while (rsForSL.next()) {
                  String lookupDescValue = rsForSL.getString(1);
                  Membr member = new Membr();
                  member.setLookupValue(rsForSL.getString(0));
                  member.setLookupDescription(lookupDescValue);
                  member.setKdxLookupDescription(lookupDescValue);
                  members.add(member);
               }
            }
            // Generate Members for Range Lookup type
            if (LookupType.RANGE_LOOKUP.equals(table.getLookupType())) {
               logger.debug("Getting the members for range lookup type");
               String lowerLimitColumn = table.getLowerLimitColumn();
               String upperLimitColumn = table.getUpperLimitColumn();
               SelectQueryInfo memberRetrievalSQLStatementRL = createMemberRetrievalSQLStatement(asset, table,
                        limitEntity, lookupValueColumn, lookupDescColumn, lowerLimitColumn, upperLimitColumn);
               SQLExeCueCachedResultSet rsForRL = null;
               rsForRL = (SQLExeCueCachedResultSet) getGenericJDBCService().executeQuery(
                        asset.getDataSource().getName(), memberRetrievalSQLStatementRL);
               while (rsForRL.next()) {
                  String lookupDescValue = rsForRL.getString(1);
                  Membr member = new Membr();
                  member.setLookupValue(rsForRL.getString(0));
                  member.setLookupDescription(lookupDescValue);
                  member.setKdxLookupDescription(lookupDescValue);
                  BigDecimal lowerLimit = null;
                  BigDecimal upperLimit = null;
                  if (rsForRL.getObject(2) != null) {
                     try {
                        lowerLimit = rsForRL.getBigDecimal(2);
                     } catch (NumberFormatException nfe) {
                        lowerLimit = new BigDecimal(Integer.MIN_VALUE);
                     }
                  }
                  if (rsForRL.getObject(3) != null) {
                     try {
                        upperLimit = rsForRL.getBigDecimal(3);
                     } catch (NumberFormatException nfe) {
                        upperLimit = new BigDecimal(Integer.MAX_VALUE);
                     }
                  }

                  member.setLowerLimit(lowerLimit);
                  member.setUpperLimit(upperLimit);

                  members.add(member);
               }
            }
            // Generate Members for Simple Hierarchy Lookup type
            if (LookupType.SIMPLEHIERARCHICAL_LOOKUP.equals(table.getLookupType())) {
               // TODO: Add code to process SHL type
            }
         }
         handleDescriptionForVirtualLookupTables(asset, table, members);
         adjustMembers(members);
         copyOriginalDescription(members);
      } catch (DataAccessException e) {
         e.printStackTrace();
         logger.debug("Members retrieval Failed");
      } catch (Exception e) {
         e.printStackTrace();
         logger.debug("Members retrieval Failed");
      }
      return members;
   }

   private void copyOriginalDescription (List<Membr> members) {
      for (Membr membr : members) {
         membr.setOriginalDescription(membr.getLookupDescription());
      }
   }

   private void handleDescriptionForVirtualLookupTables (Asset asset, Tabl table, List<Membr> members)
            throws SWIException, ParseException {
      if (CheckType.YES.equals(table.getVirtual())) {
         Colum originalTableColumn = getSdxRetrievalService().getAssetTableColum(asset.getId(), table.getActualName(),
                  table.getLookupValueColumn());
         if (ConversionType.DATE.equals(originalTableColumn.getConversionType())
                  && originalTableColumn.getDataFormat() != null) {
            DateQualifier dateQualifier = getConversionService().getDateQualifier(originalTableColumn.getDataFormat());
            for (Membr membr : members) {
               if (membr.getLookupDescription() != null) {
                  membr.setLookupDescription(getFormattedValue(dateQualifier, originalTableColumn.getDataFormat(),
                           membr));
               }
            }
         }
      }

   }

   private static String getFormattedValue (DateQualifier dateQualifier, String dateFormat, Membr member)
            throws ParseException {
      StringBuilder formattedMemberDesc = new StringBuilder(member.getLookupDescription());
      if (member.getLookupValue().equalsIgnoreCase(member.getLookupDescription())) {
         String memberDesc = member.getLookupDescription();
         String formattedTimeFrameValueOfString = TimeFrameUtility.getFormattedTimeFrameValueOfString(dateQualifier,
                  dateFormat, memberDesc);
         formattedMemberDesc = new StringBuilder();
         formattedMemberDesc.append(formattedTimeFrameValueOfString);
      }
      return formattedMemberDesc.toString();
   }

   /**
    * This method will correct the null and duplicate member values
    * 
    * @param members
    * @return
    */
   private void adjustMembers (List<Membr> members) {
      for (Membr membr : members) {
         if (ExecueCoreUtil.isEmpty(membr.getLookupDescription())) {
            if (ExecueCoreUtil.isEmpty(membr.getLookupValue())) {
               membr.setLookupDescription(getSwiConfigurationService().getMemberNullValueReprsentation());
            } else {
               membr.setLookupDescription(membr.getLookupValue());
            }
         }
      }

      // no description can be null by now.we need find the duplicates and append the lookupvalues to the desc if any
      for (Membr member : members) {
         List<Membr> duplicateMembers = getDuplicateMembers(members, member);
         for (Membr duplicateMembr : duplicateMembers) {
            if (ExecueCoreUtil.isNotEmpty(duplicateMembr.getLookupValue())) {
               duplicateMembr.setLookupDescription(duplicateMembr.getLookupDescription() + "("
                        + duplicateMembr.getLookupValue() + ")");
            }
         }
      }
   }

   private List<Membr> getDuplicateMembers (List<Membr> members, Membr membrToBeSearched) {
      List<Membr> duplicateMembers = new ArrayList<Membr>();
      for (Membr membr : members) {
         if (membrToBeSearched.getLookupDescription().equalsIgnoreCase(membr.getLookupDescription())) {
            duplicateMembers.add(membr);
         }
      }
      if (duplicateMembers.size() <= 1) {
         return new ArrayList<Membr>();
      } else {
         return duplicateMembers;
      }
   }

   /**
    * This method will return the query which needs to run in order to get the members. It takes into consideration
    * asset provider type and lookup table type
    * 
    * @param asset
    * @param table
    * @param limitEntity
    * @param lookupValueColumn
    * @param lookupDescColumn
    * @param lowerLimitColumn
    * @param upperLimitColumn
    * @return memberRetrievalSQL
    * @throws SourceMetaDataException
    */
   private SelectQueryInfo createMemberRetrievalSQLStatement (Asset asset, Tabl table, LimitEntity limitEntity,
            String lookupValueColumn, String lookupDescColumn, String lowerLimitColumn, String upperLimitColumn)
            throws SourceMetaDataException {
      List<String> columnAliasList = new ArrayList<String>();
      String alias = ExecueCoreUtil.getAlias(columnAliasList);
      Query query = new Query();
      List<SelectEntity> selectEntities = new ArrayList<SelectEntity>();
      SelectEntity lookupValueSelectEntity = new SelectEntity();
      lookupValueSelectEntity.setType(SelectEntityType.TABLE_COLUMN);
      lookupValueSelectEntity.setTableColumn(ExecueBeanManagementUtil.prepareQueryTableColumn(lookupValueColumn,
               DataType.STRING, alias, table.getName(), table.getActualName(), table.getVirtual(), table.getOwner(),
               table.getLookupType(), true));
      String valueColumnAlias = ExecueCoreUtil.getAlias(columnAliasList);
      lookupValueSelectEntity.setAlias(valueColumnAlias);
      selectEntities.add(lookupValueSelectEntity);
      SelectEntity lookupDescSelectEntity = new SelectEntity();
      lookupDescSelectEntity.setType(SelectEntityType.TABLE_COLUMN);
      lookupDescSelectEntity.setTableColumn(ExecueBeanManagementUtil.prepareQueryTableColumn(lookupDescColumn,
               DataType.STRING, alias, table.getName(), table.getActualName(), table.getVirtual(), table.getOwner(),
               table.getLookupType(), false));
      String descColumnAlias = ExecueCoreUtil.getAlias(columnAliasList);
      lookupDescSelectEntity.setAlias(descColumnAlias);
      selectEntities.add(lookupDescSelectEntity);
      if (LookupType.RANGE_LOOKUP.equals(table.getLookupType())) {
         SelectEntity lookupLowerLimitSelectEntity = new SelectEntity();
         lookupLowerLimitSelectEntity.setType(SelectEntityType.TABLE_COLUMN);
         lookupLowerLimitSelectEntity.setTableColumn(ExecueBeanManagementUtil.prepareQueryTableColumn(lowerLimitColumn,
                  DataType.NUMBER, alias, table.getName(), table.getActualName(), table.getVirtual(), table.getOwner(),
                  table.getLookupType(), false));
         String lowerLimitColumnAlias = ExecueCoreUtil.getAlias(columnAliasList);
         lookupLowerLimitSelectEntity.setAlias(lowerLimitColumnAlias);
         selectEntities.add(lookupLowerLimitSelectEntity);
         SelectEntity lookupUpperLimtSelectEntity = new SelectEntity();
         lookupUpperLimtSelectEntity.setType(SelectEntityType.TABLE_COLUMN);
         lookupUpperLimtSelectEntity.setTableColumn(ExecueBeanManagementUtil.prepareQueryTableColumn(upperLimitColumn,
                  DataType.NUMBER, alias, table.getName(), table.getActualName(), table.getVirtual(), table.getOwner(),
                  table.getLookupType(), false));
         String upperLimitColumnAlias = ExecueCoreUtil.getAlias(columnAliasList);
         lookupUpperLimtSelectEntity.setAlias(upperLimitColumnAlias);
         selectEntities.add(lookupUpperLimtSelectEntity);
      }
      if (limitEntity != null) {
         OrderEntity orderEntity = new OrderEntity();
         orderEntity.setOrderType(OrderEntityType.ASCENDING);
         SelectEntity orderSelectEntity = new SelectEntity();
         orderSelectEntity.setType(SelectEntityType.TABLE_COLUMN);
         orderSelectEntity.setTableColumn(ExecueBeanManagementUtil.prepareQueryTableColumn(lookupValueColumn,
                  DataType.STRING, alias, table.getName(), table.getActualName(), table.getVirtual(), table.getOwner(),
                  table.getLookupType(), false));
         orderSelectEntity.setAlias(valueColumnAlias);
         orderEntity.setSelectEntity(orderSelectEntity);
         List<OrderEntity> orderEntities = new ArrayList<OrderEntity>();
         orderEntities.add(orderEntity);
         query.setOrderingEntities(orderEntities);
         query.setLimitingCondition(limitEntity);
      }
      query.setSelectEntities(selectEntities);
      QueryGenerationInput queryGenerationInput = new QueryGenerationInput();
      queryGenerationInput.setTargetAsset(asset);
      List<Query> queries = new ArrayList<Query>();
      queries.add(query);
      queryGenerationInput.setInputQueries(queries);
      QueryGenerationOutput queryGenerationOutput = getQueryGenerationService(asset)
               .generateQuery(queryGenerationInput);
      String selectStatement = getQueryGenerationService(asset).extractQueryRepresentation(asset,
               queryGenerationOutput.getResultQuery()).getQueryString();
      logger.debug("JBBCSourceMetaData - Select Table statement ::" + selectStatement);

      return new SelectQueryInfo(selectStatement);
   }

   public List<String> getPrimaryKeysFromSource (Asset asset, Tabl table) throws SourceMetaDataException {
      logger.debug("Inside getPrimaryKeysFromSource method");
      Connection connection = null;
      List<String> pKeys = new ArrayList<String>();
      try {
         logger.debug("Trying to establish a connection to asset : " + asset.getName());
         connection = getAssetSourceConnectionProvider().getConnection(asset);
         logger.debug("Getting the Primary keys for table : " + table.getName());
         DatabaseMetaData dbmd = connection.getMetaData();

         ResultSet rsPK = dbmd.getPrimaryKeys(null, null, table.getName().toUpperCase());
         while (rsPK.next()) {
            pKeys.add(ExecueStringUtil.trimSpaces(rsPK.getString("COLUMN_NAME")));
         }
      } catch (AssetSourceConnectionException assetSourceConnectionException) {
         logger.error("Connection Refused");
         throw new SourceMetaDataException(DataAccessExceptionCodes.CONNECTION_CREATION_FAILED,
                  assetSourceConnectionException);
      } catch (SQLException e) {
         logger.error("Primary keys retrieval failed");
         throw new SourceMetaDataException(DataAccessExceptionCodes.META_DATA_EXTRACTION_FAILED, e);
      } finally {
         try {
            getAssetSourceConnectionProvider().closeConnection(connection);
         } catch (AssetSourceConnectionException e) {
            logger.error("Connection Closed error");
            throw new SourceMetaDataException(DataAccessExceptionCodes.RESOURCES_CLOSING_FAILED, e);
         }
      }
      return pKeys;
   }

   public Map<String, String> getForeignKeysFromSource (Asset asset, Tabl table) throws SourceMetaDataException {
      logger.debug("Inside getForeignKeysFromSource method");
      Connection connection = null;
      Map<String, String> fKeys = new HashMap<String, String>();
      try {
         logger.debug("Trying to establish a connection to asset : " + asset.getName());
         connection = getAssetSourceConnectionProvider().getConnection(asset);
         DatabaseMetaData dbmd = connection.getMetaData();
         // Get the Foreign Keys
         ResultSet rsFK = dbmd.getImportedKeys(null, null, table.getName().toUpperCase());
         while (rsFK.next()) {
            String col = ExecueStringUtil.trimSpaces(rsFK.getString("FKCOLUMN_NAME"));
            // Store the reference column name qualified with the table name(eg:
            // DEPT.dept_id)
            String ref = ExecueStringUtil.trimSpaces(rsFK.getString("PKTABLE_NAME")) + "."
                     + ExecueStringUtil.trimSpaces(rsFK.getString("PKCOLUMN_NAME"));
            fKeys.put(col, ref);
         }
      } catch (AssetSourceConnectionException assetSourceConnectionException) {
         logger.error("Connection Refused");
         throw new SourceMetaDataException(DataAccessExceptionCodes.CONNECTION_CREATION_FAILED,
                  assetSourceConnectionException);
      } catch (SQLException e) {
         logger.error("ForiegnKeys retreival failed");
         throw new SourceMetaDataException(DataAccessExceptionCodes.META_DATA_EXTRACTION_FAILED, e);
      } finally {
         try {
            getAssetSourceConnectionProvider().closeConnection(connection);
         } catch (AssetSourceConnectionException e) {
            logger.error("Connection Closed error");
            throw new SourceMetaDataException(DataAccessExceptionCodes.RESOURCES_CLOSING_FAILED, e);
         }
      }
      return fKeys;
   }

   public AssetSourceConnectionProvider getAssetSourceConnectionProvider () {
      return assetSourceConnectionProvider;
   }

   public void setAssetSourceConnectionProvider (AssetSourceConnectionProvider assetSourceConnectionProvider) {
      this.assetSourceConnectionProvider = assetSourceConnectionProvider;
   }

   private IQueryGenerationService getQueryGenerationService (Asset asset) {
      return queryGenerationServiceFactory.getQueryGenerationService(asset);
   }

   private IQueryGenerationUtilService getQueryGenerationUtilService (Asset asset) {
      return queryGenerationUtilServiceFactory.getQueryGenerationUtilService(asset.getDataSource().getProviderType());
   }

   public QueryGenerationServiceFactory getQueryGenerationServiceFactory () {
      return queryGenerationServiceFactory;
   }

   public void setQueryGenerationServiceFactory (QueryGenerationServiceFactory queryGenerationServiceFactory) {
      this.queryGenerationServiceFactory = queryGenerationServiceFactory;
   }

   public ISWIConfigurationService getSwiConfigurationService () {
      return swiConfigurationService;
   }

   public void setSwiConfigurationService (ISWIConfigurationService swiConfigurationService) {
      this.swiConfigurationService = swiConfigurationService;
   }

   public IConversionService getConversionService () {
      return conversionService;
   }

   public void setConversionService (IConversionService conversionService) {
      this.conversionService = conversionService;
   }

   public QueryGenerationUtilServiceFactory getQueryGenerationUtilServiceFactory () {
      return queryGenerationUtilServiceFactory;
   }

   public void setQueryGenerationUtilServiceFactory (QueryGenerationUtilServiceFactory queryGenerationUtilServiceFactory) {
      this.queryGenerationUtilServiceFactory = queryGenerationUtilServiceFactory;
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

   public ISDXRetrievalService getSdxRetrievalService () {
      return sdxRetrievalService;
   }

   public void setSdxRetrievalService (ISDXRetrievalService sdxRetrievalService) {
      this.sdxRetrievalService = sdxRetrievalService;
   }

   public IGenericJDBCService getGenericJDBCService () {
      return genericJDBCService;
   }

   public void setGenericJDBCService (IGenericJDBCService genericJDBCService) {
      this.genericJDBCService = genericJDBCService;
   }

   /**
    * @return the clientSourceDAO
    */
   public IClientSourceDAO getClientSourceDAO () {
      return clientSourceDAO;
   }

   /**
    * @param clientSourceDAO
    *           the clientSourceDAO to set
    */
   public void setClientSourceDAO (IClientSourceDAO clientSourceDAO) {
      this.clientSourceDAO = clientSourceDAO;
   }

}
