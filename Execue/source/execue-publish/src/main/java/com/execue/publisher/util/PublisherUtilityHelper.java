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


package com.execue.publisher.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.csvreader.CsvReader;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.DateFormat;
import com.execue.core.common.bean.entity.JobRequest;
import com.execue.core.common.bean.entity.PublishedFileInfo;
import com.execue.core.common.bean.entity.PublishedFileTableDetails;
import com.execue.core.common.bean.publisher.DBTable;
import com.execue.core.common.bean.publisher.DBTableColumn;
import com.execue.core.common.bean.publisher.DBTableDataNormalizedInfo;
import com.execue.core.common.bean.publisher.DBTableInfo;
import com.execue.core.common.bean.publisher.DBTableNormalizedInfo;
import com.execue.core.common.bean.publisher.PublisherBatchDataReadContext;
import com.execue.core.common.bean.publisher.QueryColumnDetail;
import com.execue.core.common.bean.querygen.QueryColumn;
import com.execue.core.common.bean.swi.BusinessModelPreparationContext;
import com.execue.core.common.bean.swi.PublishAssetContext;
import com.execue.core.common.type.PublishAssetMode;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.publisher.exception.PublisherException;
import com.execue.publisher.exception.PublisherExceptionCodes;
import com.execue.util.StringUtilities;

/**
 * This helper class is for publisher module.
 * 
 * @author Vishay
 * @since 20/10/09
 * @version 1.0
 */
public class PublisherUtilityHelper {

   private static final Logger logger = Logger.getLogger(PublisherUtilityHelper.class);

   public static DBTableNormalizedInfo normalizeDBTableInfo (DBTableInfo dbTableInfo) throws PublisherException {
      DBTableNormalizedInfo dbTableNormalizedInfo = new DBTableNormalizedInfo();
      dbTableNormalizedInfo.setDbTable(cloneDBTable(dbTableInfo.getDbTable()));
      List<QueryColumn> normalizedQueryColumns = new ArrayList<QueryColumn>();
      for (DBTableColumn dbTableColumn : dbTableInfo.getDbTableColumns()) {
         QueryColumn normalizedQueryColumn = new QueryColumn();
         normalizedQueryColumn.setColumnName(dbTableColumn.getColumnName());
         normalizedQueryColumn.setDataType(dbTableColumn.getDataType());
         normalizedQueryColumn.setScale(dbTableColumn.getScale());
         normalizedQueryColumn.setPrecision(dbTableColumn.getPrecision());
         normalizedQueryColumn.setDataFormat(dbTableColumn.getFormat());
         normalizedQueryColumn.setGranularity(dbTableColumn.getGranularity());
         normalizedQueryColumns.add(normalizedQueryColumn);
      }
      dbTableNormalizedInfo.setNormalizedColumns(normalizedQueryColumns);
      dbTableNormalizedInfo.setDbTableColumns(cloneDBTableColumns(dbTableInfo.getDbTableColumns()));
      return dbTableNormalizedInfo;
   }

   public static QueryColumnDetail getQueryColumnDetailForPublishedFileTableColumn (
            PublishedFileTableDetails publishedFileTableDetails) {
      QueryColumnDetail queryColumnDetail = new QueryColumnDetail();
      queryColumnDetail.setFormat(publishedFileTableDetails.getFormat());
      queryColumnDetail.setIsDistribution(publishedFileTableDetails.getDistribution());
      queryColumnDetail.setIsPopulation(publishedFileTableDetails.getPopulation());
      queryColumnDetail.setKdxDataType(publishedFileTableDetails.getKdxDataType());
      queryColumnDetail.setUnit(publishedFileTableDetails.getUnit());
      queryColumnDetail.setUnitType(publishedFileTableDetails.getUnitType());
      queryColumnDetail.setGranularity(publishedFileTableDetails.getGranularity());
      queryColumnDetail.setDefaultMetric(publishedFileTableDetails.getDefaultMetric());
      return queryColumnDetail;
   }

   public static DBTableColumn getDBTableColumnForPublishedFileTableColumn (
            PublishedFileTableDetails publishedFileTableDetails) {
      DBTableColumn dbTableColumn = new DBTableColumn();
      dbTableColumn.setColumnName(publishedFileTableDetails.getBaseColumnName());
      dbTableColumn.setDataType(publishedFileTableDetails.getBaseDataType());
      dbTableColumn.setPrecision(publishedFileTableDetails.getBasePrecision());
      dbTableColumn.setScale(publishedFileTableDetails.getBaseScale());
      dbTableColumn.setFormat(publishedFileTableDetails.getFormat());
      dbTableColumn.setUnit(publishedFileTableDetails.getUnit());
      dbTableColumn.setUnitType(publishedFileTableDetails.getUnitType());
      dbTableColumn.setGranularity(publishedFileTableDetails.getGranularity());
      dbTableColumn.setDefaultMetric(publishedFileTableDetails.getDefaultMetric());
      return dbTableColumn;
   }

   public static DBTableDataNormalizedInfo populateDBTableDataNormalizedInfo (
            DBTableNormalizedInfo dbTableNormalizedInfo, List<List<Object>> dbTableDataBatch) {
      DBTableDataNormalizedInfo dbTableDataNormalizedInfo = new DBTableDataNormalizedInfo();
      dbTableDataNormalizedInfo.setDbTable(cloneDBTable(dbTableNormalizedInfo.getDbTable()));
      dbTableDataNormalizedInfo.setDbTableColumns(cloneDBTableColumns(dbTableNormalizedInfo.getDbTableColumns()));
      dbTableDataNormalizedInfo.setNormalizedColumns(dbTableNormalizedInfo.getNormalizedColumns());
      dbTableDataNormalizedInfo.setNormalizedDataPoints(dbTableDataBatch);
      return dbTableDataNormalizedInfo;
   }

   public static QueryColumn getQueryColumn (DBTableColumn dbTableColumn) {
      QueryColumn queryColumn = new QueryColumn();
      queryColumn.setColumnName(dbTableColumn.getColumnName());
      queryColumn.setDataType(dbTableColumn.getDataType());
      queryColumn.setPrecision(dbTableColumn.getPrecision());
      queryColumn.setGranularity(dbTableColumn.getGranularity());
      queryColumn.setScale(dbTableColumn.getScale());
      queryColumn.setUnitType(dbTableColumn.getUnitType());
      queryColumn.setDataFormat(dbTableColumn.getFormat());
      return queryColumn;
   }

   public static List<List<Object>> readDataBatch (PublisherBatchDataReadContext batchDataReadContext)
            throws PublisherException {
      List<List<Object>> tableData = new ArrayList<List<Object>>();
      int counter = 1;
      try {
         CsvReader csvReader = batchDataReadContext.getCsvReader();
         // skipping the records
         while (counter < batchDataReadContext.getStartingLine()) {
            csvReader.skipLine();
            counter++;
         }
         counter = 1;
         while (counter <= batchDataReadContext.getBatchSize()) {
            if (!csvReader.readRecord()) {
               if (logger.isDebugEnabled()) {
                  logger.debug("End of file");
               }
               break;
            }
            List<Object> data = parseRowData(batchDataReadContext);
            if (ExecueCoreUtil.isCollectionEmpty(data)) {
               if (logger.isDebugEnabled()) {
                  logger.debug("Found Error Record");
               }
            } else {
               tableData.add(data);
               counter++;
            }
         }
      } catch (IOException e) {
         throw new PublisherException(PublisherExceptionCodes.PUBLISHER_DATA_UPLOAD_FAILED_EXCEPTION_CODE, e);
      }
      return tableData;
   }

   private static List<Object> parseRowData (PublisherBatchDataReadContext batchDataReadContext) throws IOException {
      boolean foundAtLeastOneCellData = false;
      List<Object> data = new ArrayList<Object>();
      for (int index = 0; index < batchDataReadContext.getNumColumns(); index++) {
         String token = batchDataReadContext.getCsvReader().get(index);
         for (CharSequence toBeEscaped : batchDataReadContext.getCharactersToEscape()) {
            if (token.contains(toBeEscaped)) {
               token.replace(toBeEscaped, batchDataReadContext.getEscapeCharacter() + toBeEscaped);
            }
         }

         token = StringUtilities.normalizeForAccentedcharacters(token);

         for (Entry<String, String> entry : batchDataReadContext.getReplaceValueMapToAvoidScriptInjection().entrySet()) {
            token = token.replaceAll(entry.getKey(), entry.getValue());
         }

         // truncate the data point to max length allowed
         if (token.length() > batchDataReadContext.getMaxDataLength()) {
            token = token.substring(0, batchDataReadContext.getMaxDataLength());
         }
         if (ExecueCoreUtil.isEmpty(token) || token.equalsIgnoreCase(batchDataReadContext.getCsvEmptyField())) {
            data.add(null);
         } else {
            data.add(token.trim());
            foundAtLeastOneCellData = true;
         }
      }
      int totalValidPoints = data.size();
      if (totalValidPoints < batchDataReadContext.getNumColumns()) {
         for (int index = 1; index <= batchDataReadContext.getNumColumns() - totalValidPoints; index++) {
            data.add(null);
         }
      }
      if (!foundAtLeastOneCellData) {
         data = null;
      }
      return data;
   }

   public static String normalizeName (String name, String regex) {
      String normalizedName = name.replaceAll(regex, "");
      normalizedName = normalizedName.trim();
      normalizedName = normalizedName.replaceAll("\\s", "_");
      normalizedName = normalizedName.replaceAll("__", "_");
      // TODO :-VG- in mysql it works, but in oracle it wont work.
      // // if db object name starts with digit, it has to appended with exeq
      // if (normalizedName.length() > 0) {
      // if (Character.isDigit(normalizedName.charAt(0))) {
      // normalizedName = "exeq_" + normalizedName;
      // }
      // }
      return normalizedName;
   }

   public static DBTable cloneDBTable (DBTable toBeClonedTable) {
      DBTable clonedDBTable = new DBTable();
      clonedDBTable.setTableName(toBeClonedTable.getTableName());
      clonedDBTable.setDisplayName(toBeClonedTable.getDisplayName());
      return clonedDBTable;
   }

   public static List<DBTableColumn> cloneDBTableColumns (List<DBTableColumn> toBeClonedColumns) {
      List<DBTableColumn> clonedDBTableColumns = new ArrayList<DBTableColumn>();
      for (DBTableColumn toBeClonedColumn : toBeClonedColumns) {
         DBTableColumn clonedDBTableColumn = new DBTableColumn();
         clonedDBTableColumn.setColumnName(toBeClonedColumn.getColumnName());
         clonedDBTableColumn.setDataType(toBeClonedColumn.getDataType());
         clonedDBTableColumn.setPrecision(toBeClonedColumn.getPrecision());
         clonedDBTableColumn.setScale(toBeClonedColumn.getScale());
         clonedDBTableColumn.setFormat(toBeClonedColumn.getFormat());
         clonedDBTableColumns.add(clonedDBTableColumn);
      }
      return clonedDBTableColumns;
   }

   public static void cloneDateFormat (DateFormat clonedDateFormat, DateFormat toBeClonedDateFormat) {
      clonedDateFormat.setId(toBeClonedDateFormat.getId());
      clonedDateFormat.setFormat(toBeClonedDateFormat.getFormat());
      clonedDateFormat.setDbFormat(toBeClonedDateFormat.getDbFormat());
      clonedDateFormat.setAssetProviderType(toBeClonedDateFormat.getAssetProviderType());
      clonedDateFormat.setIsPlainFormat(toBeClonedDateFormat.getIsPlainFormat());
      clonedDateFormat.setQualifier(toBeClonedDateFormat.getQualifier());
      clonedDateFormat.setSupported(toBeClonedDateFormat.getSupported());
      clonedDateFormat.setQualifierBEDId(toBeClonedDateFormat.getQualifierBEDId());
   }

   public static BusinessModelPreparationContext populateBusinessModelPreparationContext (
            PublishedFileInfo publishedFileInfo, Asset asset, JobRequest jobRequest) {
      BusinessModelPreparationContext businessModelPreparationContext = new BusinessModelPreparationContext();
      businessModelPreparationContext.setApplicationId(publishedFileInfo.getApplicationId());
      businessModelPreparationContext.setModelId(publishedFileInfo.getModelId());
      businessModelPreparationContext.setJobRequest(jobRequest);
      businessModelPreparationContext.setUserId(publishedFileInfo.getUserId());
      businessModelPreparationContext.setAssetId(asset.getId());
      return businessModelPreparationContext;

   }

   public static PublishAssetContext populatePublishAssetContext (PublishedFileInfo publishedFileInfo, Asset asset,
            JobRequest jobRequest, PublishAssetMode publishAssetMode) {
      PublishAssetContext publishAssetContext = new PublishAssetContext();
      publishAssetContext.setApplicationId(publishedFileInfo.getApplicationId());
      publishAssetContext.setAssetId(asset.getId());
      publishAssetContext.setJobRequest(jobRequest);
      publishAssetContext.setModelId(publishedFileInfo.getModelId());
      publishAssetContext.setPublishMode(publishAssetMode);
      publishAssetContext.setUserId(publishedFileInfo.getUserId());
      return publishAssetContext;
   }
}
