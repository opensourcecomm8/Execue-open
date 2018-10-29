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


package com.execue.publisher.evaluate.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.entity.DataSource;
import com.execue.core.common.bean.entity.DateFormat;
import com.execue.core.common.bean.entity.PublishedFileInfo;
import com.execue.core.common.bean.publisher.DBTableColumn;
import com.execue.core.common.bean.publisher.DBTableInfo;
import com.execue.core.common.bean.publisher.DBTableNormalizedInfo;
import com.execue.core.common.bean.publisher.QueryColumnDetail;
import com.execue.core.common.bean.querygen.QueryColumn;
import com.execue.core.common.bean.querygen.QueryTable;
import com.execue.core.common.bean.querygen.SelectQueryInfo;
import com.execue.core.common.bean.swi.ColumnDataTypeMetaInfo;
import com.execue.core.common.type.AssetProviderType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.ColumnType;
import com.execue.core.common.type.DataType;
import com.execue.core.common.type.StatType;
import com.execue.core.common.util.ExecueBeanCloneUtil;
import com.execue.core.common.util.ExecueBeanManagementUtil;
import com.execue.core.constants.ISQLQueryConstants;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.platform.helper.PublishedFileJDBCHelper;
import com.execue.platform.swi.IJDBCSourceColumnMetaService;
import com.execue.publisher.absorbtion.IPublisherDataAbsorbtionService;
import com.execue.publisher.configuration.IPublisherConfigurationService;
import com.execue.publisher.evaluate.IPublisherDataEvaluationService;
import com.execue.publisher.exception.PublisherException;
import com.execue.publisher.util.PublisherUtilityHelper;
import com.execue.swi.exception.PublishedFileException;
import com.execue.swi.exception.SWIException;
import com.execue.swi.service.IConversionService;
import com.execue.swi.service.IPublishedFileRetrievalService;
import com.execue.swi.service.ISDXRetrievalService;
import com.execue.util.querygen.IQueryGenerationUtilService;
import com.execue.util.querygen.QueryGenerationUtilServiceFactory;

/**
 * This service is for evaluation of the information from csv file.
 * 
 * @author Vishay
 * @since 20/10/09
 * @version 1.0
 */
public class PublisherDataEvaluationServiceImpl implements IPublisherDataEvaluationService, ISQLQueryConstants {

   private static final Logger               logger = Logger.getLogger(PublisherDataEvaluationServiceImpl.class);
   private IPublisherConfigurationService    publisherConfigurationService;
   private QueryGenerationUtilServiceFactory queryGenerationUtilServiceFactory;
   private IPublisherDataAbsorbtionService   publisherDataAbsorbtionService;
   private IConversionService                conversionService;
   private ISDXRetrievalService              sdxRetrievalService;
   private IPublishedFileRetrievalService    publishedFileRetrievalService;
   private PublishedFileJDBCHelper           publishedFileJDBCHelper;
   private IJDBCSourceColumnMetaService      JDBCSourceColumnMetaService;

   public List<QueryColumn> evaluateVirtualLooks (DBTableNormalizedInfo dbTableNormalizedInfo, DataSource dataSource)
            throws PublisherException {
      String tableName = dbTableNormalizedInfo.getDbTable().getTableName();
      QueryTable queryTable = ExecueBeanManagementUtil.prepareQueryTable(tableName);
      Long minLookupRecordsRequired = publisherConfigurationService.getMinimumMembersEligibilityLookup();
      Long maxLookupRecordsAllowed = calculateMaxLookupRecordsAllowed(dbTableNormalizedInfo.getTotalRecordsCount());
      Long dateTypeMaxLookupRecordsAllowed = calculateDateTypeMaxLookupRecordsAllowed(dbTableNormalizedInfo
               .getTotalRecordsCount());
      List<QueryColumn> virtualLookupEligiblityColumns = new ArrayList<QueryColumn>();
      virtualLookupEligiblityColumns = getUserRequestedLookups(dbTableNormalizedInfo);

      try {
         // if system evaluation to amend the user selection for Virtual Lookups is set to true,
         // then allow system evaluation on top of user selection
         // else restrict the Virtual Lookups to User Selection only
         // If user did not selected any VL then allow the system evaluation to happen
         if (!publisherConfigurationService.isForceSysteEvalToAmendUserSelectionForLookups()) {
            if (virtualLookupEligiblityColumns.size() > 0) {
               return virtualLookupEligiblityColumns;
            }
         }

         int index = 0;
         for (QueryColumn queryColumn : dbTableNormalizedInfo.getNormalizedColumns()) {
            boolean isEligibleForLookup = true;

            // If the KDX Data Type is already set by user, ignore those columns for evaluation of lookups
            QueryColumnDetail queryColumnDetail = dbTableNormalizedInfo.getNormalizedColumnDetails().get(index);
            if (!ColumnType.NULL.equals(queryColumnDetail.getKdxDataType())) {
               index++;
               continue;
            }

            // columns which cannot be lookups
            if (isEligibleForLookup && DataType.NUMBER.equals(queryColumn.getDataType())) {
               isEligibleForLookup = false;
            }
            if (isEligibleForLookup && DataType.STRING.equals(queryColumn.getDataType())) {
               if (queryColumn.getPrecision() > publisherConfigurationService
                        .getMaxStringColumnLengthLookupEligibility()) {
                  isEligibleForLookup = false;
               }
            }
            if (isEligibleForLookup) {
               QueryColumn clonedQueryColumn = ExecueBeanCloneUtil.cloneQueryColumn(queryColumn);
               clonedQueryColumn.setDistinct(true);
               Long distinctColumnRecords = getPublishedFileJDBCHelper().getStatBasedColumnValue(dataSource,
                        queryTable, clonedQueryColumn, StatType.COUNT);

               if (DataType.DATE.equals(queryColumn.getDataType())) {
                  if (distinctColumnRecords >= minLookupRecordsRequired
                           && distinctColumnRecords <= dateTypeMaxLookupRecordsAllowed) {
                     if (logger.isDebugEnabled()) {
                        logger.debug("Found lookup for column " + queryColumn.getColumnName());
                     }
                     virtualLookupEligiblityColumns.add(clonedQueryColumn);
                  }
               } else {
                  if (distinctColumnRecords >= minLookupRecordsRequired
                           && distinctColumnRecords <= maxLookupRecordsAllowed) {
                     if (logger.isDebugEnabled()) {
                        logger.debug("Found lookup for column " + queryColumn.getColumnName());
                     }
                     virtualLookupEligiblityColumns.add(clonedQueryColumn);
                  }
               }
            }
            index++;
         }
      } catch (DataAccessException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      return virtualLookupEligiblityColumns;
   }

   private List<QueryColumn> getUserRequestedLookups (DBTableNormalizedInfo evaluatedDBTableNormalizedInfo) {
      List<QueryColumn> userRequestedLookups = new ArrayList<QueryColumn>();
      int index = 0;

      for (QueryColumnDetail queryColumnDetail : evaluatedDBTableNormalizedInfo.getNormalizedColumnDetails()) {
         if (ColumnType.DIMENSION.equals(queryColumnDetail.getKdxDataType())
                  || ColumnType.SIMPLE_LOOKUP.equals(queryColumnDetail.getKdxDataType())
                  || ColumnType.RANGE_LOOKUP.equals(queryColumnDetail.getKdxDataType())
                  || ColumnType.SIMPLE_HIERARCHY_LOOKUP.equals(queryColumnDetail.getKdxDataType())) {
            QueryColumn lookupQUeryColumn = ExecueBeanCloneUtil.cloneQueryColumn(evaluatedDBTableNormalizedInfo
                     .getNormalizedColumns().get(index));
            userRequestedLookups.add(lookupQUeryColumn);
         }
         index++;
      }
      return userRequestedLookups;
   }

   private Long calculateDateTypeMaxLookupRecordsAllowed (Long totalRecordsCount) {

      List<Long> lookupEligiblityLongCriteriaRecords = getPublisherConfigurationService()
               .getDateLookupEligibilityCriteriaRecords();

      List<Double> lookupEligiblityLongCriteriaPercentages = getPublisherConfigurationService()
               .getDateLookupEligibilityCriteriaPercentage();

      int counter = 0;
      Double percentage = null;
      for (Long recordCount : lookupEligiblityLongCriteriaRecords) {
         if (totalRecordsCount < recordCount) {
            percentage = lookupEligiblityLongCriteriaPercentages.get(counter);
            break;
         }
         counter++;
      }
      if (percentage == null) {
         percentage = lookupEligiblityLongCriteriaPercentages.get(lookupEligiblityLongCriteriaPercentages.size() - 1);
      }
      return Double.valueOf(percentage / 100.00 * totalRecordsCount).longValue();
   }

   private Long calculateMaxLookupRecordsAllowed (Long totalRecordsCount) {

      List<Long> lookupEligiblityLongCriteriaRecords = getPublisherConfigurationService()
               .getLookupEligibilityCriteriaRecords();

      List<Double> lookupEligiblityLongCriteriaPercentages = getPublisherConfigurationService()
               .getLookupEligibilityCriteriaPercentage();

      int counter = 0;
      Double percentage = null;
      for (Long recordCount : lookupEligiblityLongCriteriaRecords) {
         if (totalRecordsCount < recordCount) {
            percentage = lookupEligiblityLongCriteriaPercentages.get(counter);
            break;
         }
         counter++;
      }
      if (percentage == null) {
         percentage = lookupEligiblityLongCriteriaPercentages.get(lookupEligiblityLongCriteriaPercentages.size() - 1);
      }
      return Double.valueOf(percentage / 100.00 * totalRecordsCount).longValue();
   }

   public DBTableNormalizedInfo evaluateDBTableInfo (DBTableInfo dbTableInfo, DataSource dataSource)
            throws PublisherException {
      DBTableNormalizedInfo dbTableNormalizedInfo = new DBTableNormalizedInfo();
      dbTableNormalizedInfo.setDbTable(PublisherUtilityHelper.cloneDBTable(dbTableInfo.getDbTable()));
      dbTableNormalizedInfo.setDbTableColumns(PublisherUtilityHelper.cloneDBTableColumns(dbTableInfo
               .getDbTableColumns()));
      List<QueryColumn> normalizedColumns = new ArrayList<QueryColumn>();
      for (DBTableColumn column : dbTableInfo.getDbTableColumns()) {
         QueryColumn normalizedColumn = PublisherUtilityHelper.getQueryColumn(column);

         ColumnDataTypeMetaInfo columnDataTypeMetaInfo;
         try {
            columnDataTypeMetaInfo = getJDBCSourceColumnMetaService().getColumnDataTypeMetaInfo(dataSource,
                     dbTableInfo.getDbTable().getTableName(), normalizedColumn);
            normalizedColumn.setUnitType(columnDataTypeMetaInfo.getUnitType());
            normalizedColumn.setPrecision(columnDataTypeMetaInfo.getPrecision());
            normalizedColumn.setScale(columnDataTypeMetaInfo.getScale());
            normalizedColumn.setDataFormat(columnDataTypeMetaInfo.getDateFormat());
            normalizedColumn.setDataType(columnDataTypeMetaInfo.getDataType());
         } catch (SWIException e) {
            throw new PublisherException(e.getCode(), e);
         }
         normalizedColumns.add(normalizedColumn);
      }
      dbTableNormalizedInfo.setNormalizedColumns(normalizedColumns);
      return dbTableNormalizedInfo;
   }

   public boolean isColumnDateTypeConfirmation (Long publishedFileTableInfoId, String tableName,
            QueryColumn queryColumn, String format) throws PublisherException {
      boolean isColumnDateType = false;
      try {
         PublishedFileInfo publishedFileInfo = getPublishedFileRetrievalService().getPublishedFileInfoByTableInfoId(
                  publishedFileTableInfoId);
         DataSource dataSource = getSdxRetrievalService().getDataSourceById(publishedFileInfo.getDatasourceId());
         IQueryGenerationUtilService queryGenerationUtilService = getQueryGenerationUtilService(dataSource
                  .getProviderType());
         QueryTable queryTable = ExecueBeanManagementUtil.prepareQueryTable(tableName, null, dataSource.getOwner());
         SelectQueryInfo selectQueryInfo = queryGenerationUtilService.createCountNotNullRegexStatement(queryTable,
                  queryColumn, null);
         logger.debug("SQL Query for counting all the records " + selectQueryInfo.getSelectQuery());
         Long countEntireRecords = getPublishedFileJDBCHelper()
                  .executeCountQuery(dataSource.getName(), selectQueryInfo);

         if (CheckType.NO.equals(conversionService.isDateFormatSupported(format))) {
            SelectQueryInfo countNonNullRecordLengthStatement = queryGenerationUtilService
                     .createCountNotNullRecordsLengthStatement(queryTable, queryColumn, format.length());
            Long countNotNullRecordLength = getPublishedFileJDBCHelper().executeCountQuery(dataSource.getName(),
                     countNonNullRecordLengthStatement);
            if (countEntireRecords.longValue() == countNotNullRecordLength.longValue()) {
               isColumnDateType = true;
            }
         } else {
            DateFormat dateFormat = conversionService.getSupportedDateFormat(format, dataSource.getProviderType());
            String dateRegex = publisherConfigurationService.getDateRegEx();

            int minLength = publisherConfigurationService.getMinDateFieldLength();

            if (CheckType.YES.equals(dateFormat.getIsPlainFormat())) {
               dateRegex = publisherConfigurationService.getPlainDateRegEx();

               minLength = publisherConfigurationService.getMinPlainDateFieldLength();
            }

            SelectQueryInfo dateRegexSizeNotNullStatement = queryGenerationUtilService
                     .createDateRegexSizeNotNullStatement(queryTable, queryColumn, dateRegex, minLength);
            logger.debug("SQL Query for counting all the date records "
                     + dateRegexSizeNotNullStatement.getSelectQuery());

            Long countDateRecords = getPublishedFileJDBCHelper().executeCountQuery(dataSource.getName(),
                     dateRegexSizeNotNullStatement);

            isColumnDateType = getJDBCSourceColumnMetaService().isColumnDateType(dataSource, tableName, queryColumn,
                     dateFormat, countEntireRecords, countDateRecords);
         }
      } catch (SWIException swie) {
         throw new PublisherException(swie.getCode(), swie);
      } catch (PublishedFileException pfe) {
         throw new PublisherException(pfe.getCode(), pfe);
      } catch (DataAccessException e) {
         throw new PublisherException(e.getCode(), e);
      }
      return isColumnDateType;
   }

   private IQueryGenerationUtilService getQueryGenerationUtilService (AssetProviderType assetProviderType) {
      return getQueryGenerationUtilServiceFactory().getQueryGenerationUtilService(assetProviderType);
   }

   public QueryGenerationUtilServiceFactory getQueryGenerationUtilServiceFactory () {
      return queryGenerationUtilServiceFactory;
   }

   public void setQueryGenerationUtilServiceFactory (QueryGenerationUtilServiceFactory queryGenerationUtilServiceFactory) {
      this.queryGenerationUtilServiceFactory = queryGenerationUtilServiceFactory;
   }

   public IPublisherDataAbsorbtionService getPublisherDataAbsorbtionService () {
      return publisherDataAbsorbtionService;
   }

   public void setPublisherDataAbsorbtionService (IPublisherDataAbsorbtionService publisherDataAbsorbtionService) {
      this.publisherDataAbsorbtionService = publisherDataAbsorbtionService;
   }

   public IPublisherConfigurationService getPublisherConfigurationService () {
      return publisherConfigurationService;
   }

   public void setPublisherConfigurationService (IPublisherConfigurationService publisherConfiguration) {
      this.publisherConfigurationService = publisherConfiguration;
   }

   public IConversionService getConversionService () {
      return conversionService;
   }

   public void setConversionService (IConversionService conversionService) {
      this.conversionService = conversionService;
   }

   /**
    * @return the publishedFileJDBCHelper
    */
   public PublishedFileJDBCHelper getPublishedFileJDBCHelper () {
      return publishedFileJDBCHelper;
   }

   /**
    * @param publishedFileJDBCHelper
    *           the publishedFileJDBCHelper to set
    */
   public void setPublishedFileJDBCHelper (PublishedFileJDBCHelper publishedFileJDBCHelper) {
      this.publishedFileJDBCHelper = publishedFileJDBCHelper;
   }

   public IJDBCSourceColumnMetaService getJDBCSourceColumnMetaService () {
      return JDBCSourceColumnMetaService;
   }

   public void setJDBCSourceColumnMetaService (IJDBCSourceColumnMetaService sourceColumnMetaService) {
      JDBCSourceColumnMetaService = sourceColumnMetaService;
   }

   public ISDXRetrievalService getSdxRetrievalService () {
      return sdxRetrievalService;
   }

   public void setSdxRetrievalService (ISDXRetrievalService sdxRetrievalService) {
      this.sdxRetrievalService = sdxRetrievalService;
   }

   public IPublishedFileRetrievalService getPublishedFileRetrievalService () {
      return publishedFileRetrievalService;
   }

   public void setPublishedFileRetrievalService (IPublishedFileRetrievalService publishedFileRetrievalService) {
      this.publishedFileRetrievalService = publishedFileRetrievalService;
   }

}
