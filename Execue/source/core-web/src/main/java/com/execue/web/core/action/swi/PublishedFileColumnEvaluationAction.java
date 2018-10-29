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


package com.execue.web.core.action.swi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.ApplicationContext;
import com.execue.core.common.bean.entity.Application;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.common.bean.entity.PublishedFileInfo;
import com.execue.core.common.bean.entity.PublishedFileTableInfo;
import com.execue.core.common.type.AppCreationType;
import com.execue.core.common.type.ColumnType;
import com.execue.core.common.type.ConversionType;
import com.execue.core.common.type.GranularityType;
import com.execue.core.common.util.ExecueBeanManagementUtil;
import com.execue.core.common.util.ExecueBeanUtil;
import com.execue.core.exception.ExeCueException;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.handler.UIPublishedFileColumnInfo;
import com.execue.handler.UIPublishedFileEvaluatedColumnDetail;
import com.execue.handler.UIPublishedFileGridColumnInfo;
import com.execue.handler.UITransposedPublishedFileTableDetails;
import com.execue.handler.bean.UITable;
import com.execue.handler.bean.grid.IGridBean;
import com.execue.handler.swi.IApplicationServiceHandler;
import com.execue.handler.swi.IPublishedFileColumnEvaluationServiceHandler;
import com.execue.swi.exception.PublishedFileException;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

public class PublishedFileColumnEvaluationAction extends PaginationGridAction {

   private static final Logger                          log                = Logger
                                                                                    .getLogger(PublishedFileColumnEvaluationAction.class);

   private static final long                            serialVersionUID   = 1L;
   private static final String                          PUBLISHED_FILE     = "PF";
   private static final String                          DATASET_COLLECTION = "DC";

   private Long                                         publishedFileId;
   private Long                                         fileTableId;
   private Long                                         fileTableColumnId;
   private String                                       dateFormat;
   private String                                       columnsUpdateStatus;
   private List<String>                                 updateValidationErrorMessages;
   private List<PublishedFileTableInfo>                 publishedFileTables;
   private UIPublishedFileColumnInfo                    uiPublishedFileColumnInfo;
   private UITransposedPublishedFileTableDetails        tranposeEvaluatedColumns;
   private static int                                   COLUMN_SIZE        = 5;
   private String                                       evaluatedColumnList;
   private String                                       conversionTypeStr;
   private Long                                         jobRequestId;
   private Long                                         applicationId;
   private Long                                         assetId;
   private Long                                         tableId;
   private String                                       infoSource         = PUBLISHED_FILE;                                              // PF|DC
   private String                                       NORMAL_METADETA    = "normal_metadata";
   private String                                       sourceName         = ASSET;
   private Asset                                        asset;
   private List<Asset>                                  assets;
   private PublishedFileInfo                            publishedFileInfo;

   private IPublishedFileColumnEvaluationServiceHandler publishedFileColumnEvaluationServiceHandler;
   private IApplicationServiceHandler                   applicationServiceHandler;

   @Override
   protected List<? extends IGridBean> processPageGrid () {
      List<UITransposedPublishedFileTableDetails> tranposeEvaluatedColumns = new ArrayList<UITransposedPublishedFileTableDetails>();
      try {
         List<UIPublishedFileEvaluatedColumnDetail> columns = null;
         // TODO : -VG- this should come from jsp later.
         getPageDetail().setPageSize(new Long(COLUMN_SIZE));
         if (DATASET_COLLECTION.equals(getInfoSource())) {
            columns = getPublishedFileColumnEvaluationServiceHandler().getAssetTableColumnsByPage(assetId, tableId,
                     getPageDetail());
         } else {
            columns = publishedFileColumnEvaluationServiceHandler.getEvaluatedColumnsByPage(fileTableId,
                     getPageDetail());
         }
         if (ExecueCoreUtil.isCollectionNotEmpty(columns)) {
            tranposeEvaluatedColumns = tranposeEvaluatedColumns(columns);
         }
      } catch (PublishedFileException e) {
         log.error(e);
      } catch (ExeCueException e) {
         log.error(e);
      }
      return tranposeEvaluatedColumns;
   }

   public String showPublishedFileTables () {
      try {
         if (DATASET_COLLECTION.equals(getInfoSource())) {
            if (getTableId() == null) {
               Asset asset = getSdxServiceHandler().getAsset(assetId);
               List<UITable> factTables = getSdxServiceHandler().getAllAssetFactTables(asset);
               tableId = factTables.get(0).getId();
            }
         } else {
            PublishedFileTableInfo publishedFileTableInfo = null;
            publishedFileTables = publishedFileColumnEvaluationServiceHandler.getPublishedFileTables(publishedFileId);
            publishedFileInfo = publishedFileColumnEvaluationServiceHandler
                     .getPublishedFileInfoByFileId(publishedFileId);
            if (ExecueCoreUtil.isCollectionNotEmpty(publishedFileTables) && publishedFileTables.size() == 1) {
               publishedFileTableInfo = publishedFileTables.get(0);
               fileTableId = publishedFileTableInfo.getId();
            }
         }
      } catch (PublishedFileException e) {
         log.error(e);
      } catch (ExeCueException e) {
         log.error(e);
      }
      return SUCCESS;
   }

   public String showMetadata () {
      try {
         if (getApplicationId() == null) {
            setApplicationId(getApplicationContext().getAppId());
         }
         adjustApplicationContext();
         // get application creation type
         Application appication = getApplicationServiceHandler().getApplicationById(getApplicationContext().getAppId());
         if (AppCreationType.NORMAL_PROCESS.equals(appication.getCreationType())) {
            assets = getSdxServiceHandler().getAllAssets(getApplicationContext().getAppId());
            Long sessionAssetId = getApplicationContext().getAssetId();
            if (sessionAssetId != null) {
               for (Asset localAsset : assets) {
                  if (localAsset.getId().longValue() == sessionAssetId.longValue()) {
                     asset = localAsset;
                     break;
                  }
               }
            }
            return NORMAL_METADETA;
         }
         if (getApplicationContext().getAssetId() != null) {
            setInfoSource(DATASET_COLLECTION);
            setAssetId(getApplicationContext().getAssetId());
         } else {
            Asset asset = getPublishedFileColumnEvaluationServiceHandler().getApplicationAsset(getApplicationId());
            if (asset != null) {
               setInfoSource(DATASET_COLLECTION);
               setAssetId(asset.getId());
            } else {
               PublishedFileInfo fileInfo = getPublishedFileColumnEvaluationServiceHandler()
                        .getApplicationPublishedFileInfo(getApplicationId());
               setInfoSource(PUBLISHED_FILE);
               setPublishedFileId(fileInfo.getId());
               setJobRequestId(fileInfo.getAbsorbtionJobRequestId());
            }
         }
      } catch (ExeCueException exception) {
         log.error(exception);
      }
      return SUCCESS;
   }

   public String showEvaluatedColumns () {
      try {
         List<UIPublishedFileEvaluatedColumnDetail> evaluatedColumns = null;
         if (DATASET_COLLECTION.equals(getInfoSource())) {
            // get from asset
            evaluatedColumns = getPublishedFileColumnEvaluationServiceHandler().getAssetTableColumns(assetId, tableId);
         } else {
            evaluatedColumns = publishedFileColumnEvaluationServiceHandler.getEvaluatedColumns(fileTableId);
         }
         if (ExecueCoreUtil.isCollectionNotEmpty(evaluatedColumns)) {
            // tranposeEvaluatedColumns = tranposeEvaluatedColumns(evaluatedColumns);
         }
      } catch (PublishedFileException e) {
         log.error(e);
      } catch (ExeCueException e) {
         log.error(e);
      }
      return SUCCESS;
   }

   private void adjustApplicationContext () {
      try {
         ApplicationContext ctx = getApplicationContext();
         if (ctx != null && getApplicationId().equals(ctx.getAppId())) {
            return;
         } else {
            Application application = getApplicationServiceHandler().getApplicationById(getApplicationId());
            Model model = getApplicationServiceHandler().getModelsByApplicationId(application.getId()).get(0);
            ApplicationContext applicationContext = ExecueBeanManagementUtil.prepareApplicationContext(model.getId(),
                     application.getId(), application.getName(), null, null, application.getSourceType());
            setApplicationContext(applicationContext);
         }
      } catch (Exception exception) {
         log.error(exception);
      }
   }

   private List<UITransposedPublishedFileTableDetails> tranposeEvaluatedColumns (
            List<UIPublishedFileEvaluatedColumnDetail> evaluatedColumns) {
      List<UITransposedPublishedFileTableDetails> tranformedDataDetails = new ArrayList<UITransposedPublishedFileTableDetails>();
      for (int rowNum = 1; rowNum <= 6; rowNum++) {
         tranformedDataDetails.add(transposePublishedFileTableDetails(evaluatedColumns, rowNum));
      }
      return tranformedDataDetails;
   }

   private UITransposedPublishedFileTableDetails transposePublishedFileTableDetails (
            List<UIPublishedFileEvaluatedColumnDetail> evaluatedColumns, int rowNum) {
      UITransposedPublishedFileTableDetails uiTransposedPublishedFileTableDetail = new UITransposedPublishedFileTableDetails();
      List<UIPublishedFileGridColumnInfo> uiPublishedFileGridColumnsInfo = new ArrayList<UIPublishedFileGridColumnInfo>();
      int columnSize = evaluatedColumns.size();
      for (int columnIndex = 0; columnIndex < columnSize; columnIndex++) {
         uiPublishedFileGridColumnsInfo.add(getAttribute(evaluatedColumns.get(columnIndex), rowNum));
      }
      if (columnSize % COLUMN_SIZE != 0) {
         for (int dummyColumnIndex = columnSize; dummyColumnIndex < 5; dummyColumnIndex++) {
            uiPublishedFileGridColumnsInfo.add(getDummyAttribute(rowNum));
         }
      }
      uiTransposedPublishedFileTableDetail.setCols(uiPublishedFileGridColumnsInfo);
      uiTransposedPublishedFileTableDetail.setRowHeader(getRowHeader(rowNum));
      uiTransposedPublishedFileTableDetail.setId(new Long(rowNum));
      return uiTransposedPublishedFileTableDetail;
   }

   private String getRowHeader (int rowNum) {
      String rowHeader = null;
      switch (rowNum) {
         case 1:
            rowHeader = "Field Name";
            break;
         case 2:
            rowHeader = "Data Type";
            break;
         case 3:
            rowHeader = "Data Format";
            break;
         case 4:
            rowHeader = "Data Unit";
            break;
         case 5:
            rowHeader = "Semantifi Type";
            break;
         case 6:
            rowHeader = "Granularity";
            break;
      }
      return rowHeader;
   }

   private UIPublishedFileGridColumnInfo getAttribute (
            UIPublishedFileEvaluatedColumnDetail publishedFileEvaluatedColumnDetail, int rowNum) {
      UIPublishedFileGridColumnInfo uiPublishedFileGridColumnInfo = new UIPublishedFileGridColumnInfo();
      switch (rowNum) {
         case 1:
            uiPublishedFileGridColumnInfo.setColumnInfo(publishedFileEvaluatedColumnDetail.getColumnName());
            break;
         case 2:
            uiPublishedFileGridColumnInfo.setConversionTypeColumnInfo(publishedFileEvaluatedColumnDetail
                     .getColumnDetail());
            break;
         case 3:
            uiPublishedFileGridColumnInfo.setColumnInfo(publishedFileEvaluatedColumnDetail.getFormat());
            break;
         case 4:
            uiPublishedFileGridColumnInfo.setColumnInfo(publishedFileEvaluatedColumnDetail.getUnit());
            break;
         case 5:
            uiPublishedFileGridColumnInfo.setColumnInfo(publishedFileEvaluatedColumnDetail.getKdxDataType().toString());
            break;
         case 6:
            uiPublishedFileGridColumnInfo.setColumnInfo(publishedFileEvaluatedColumnDetail.getGranularity().toString());
            break;
      }
      return uiPublishedFileGridColumnInfo;
   }

   private UIPublishedFileGridColumnInfo getDummyAttribute (int rowNum) {
      UIPublishedFileGridColumnInfo uiPublishedFileGridColumnInfo = new UIPublishedFileGridColumnInfo();
      switch (rowNum) {
         case 1:
            uiPublishedFileGridColumnInfo.setColumnInfo(null);
            break;
         case 2:
            uiPublishedFileGridColumnInfo.setConversionTypeColumnInfo(null);
            break;
         case 3:
            uiPublishedFileGridColumnInfo.setColumnInfo("");
            break;
         case 4:
            uiPublishedFileGridColumnInfo.setColumnInfo("");
            break;
         case 5:
            uiPublishedFileGridColumnInfo.setColumnInfo(null);
            break;
         case 6:
            uiPublishedFileGridColumnInfo.setColumnInfo(null);
            break;
      }
      return uiPublishedFileGridColumnInfo;
   }

   @SuppressWarnings ("unchecked")
   public String updateEvaluatedColumns () {
      try {
         ArrayList<HashMap> jsonObjectArray = (ArrayList<HashMap>) JSONUtil.deserialize(getEvaluatedColumnList());
         List<UIPublishedFileEvaluatedColumnDetail> tranposedEvaluatedList = new ArrayList<UIPublishedFileEvaluatedColumnDetail>();
         for (HashMap map : jsonObjectArray) {
            UIPublishedFileEvaluatedColumnDetail transposedEvaluatedColumnDetail = new UIPublishedFileEvaluatedColumnDetail();
            transposeFromTokens(transposedEvaluatedColumnDetail, map);
            tranposedEvaluatedList.add(transposedEvaluatedColumnDetail);
         }
         if (DATASET_COLLECTION.equals(getInfoSource())) {
            updateValidationErrorMessages = getPublishedFileColumnEvaluationServiceHandler().updateAssetTableColumns(
                     getApplicationContext().getModelId(), assetId, tableId, tranposedEvaluatedList);
         } else {
            updateValidationErrorMessages = publishedFileColumnEvaluationServiceHandler.updateEvaluatedColumns(
                     fileTableId, tranposedEvaluatedList);
         }
      } catch (PublishedFileException e) {
         log.error(e);
         return ERROR;
      } catch (JSONException e) {
         log.error(e);
         return ERROR;
      } catch (ExeCueException e) {
         log.error(e);
         return ERROR;
      }
      return SUCCESS;
   }

   @SuppressWarnings ("unchecked")
   public String getUIPublishedFileColumnInfo () {
      try {
         Map map = (Map) JSONUtil.deserialize(getConversionTypeStr());
         UIPublishedFileColumnInfo tranformedUiColumnInfo = new UIPublishedFileColumnInfo();
         transposeFromTokens(tranformedUiColumnInfo, map);
         setUiPublishedFileColumnInfo(tranformedUiColumnInfo);
         uiPublishedFileColumnInfo = publishedFileColumnEvaluationServiceHandler
                  .getModifiedUIPublishedFileColumnInfo(uiPublishedFileColumnInfo);
      } catch (PublishedFileException e) {
         log.error(e);
         return ERROR;
      } catch (JSONException e) {
         log.error(e);
         return ERROR;
      }
      return SUCCESS;
   }

   private void transposeFromTokens (UIPublishedFileColumnInfo transformedUiColumnInfo, Map map) {
      transformedUiColumnInfo.setPublishedFileTableDetailId(Long.parseLong((String) map
               .get("publishedFileTableDetailId")));
      transformedUiColumnInfo.setConversionType(ConversionType.getType((String) map.get("conversionType")));
      transformedUiColumnInfo.setConceptExist(ExecueBeanUtil.getCorrespondingBooleanValue((String) map
               .get("conceptExist")));
      String conceptId = (String) map.get("conceptId");
      if (ExecueCoreUtil.isNotEmpty(conceptId) && !"null".equalsIgnoreCase(conceptId)) {
         transformedUiColumnInfo.setConceptId(Long.parseLong(conceptId));
      }
   }

   private void transposeFromTokens (UIPublishedFileEvaluatedColumnDetail tranformedUiEvaluatedColumnDetails,
            HashMap map) {
      tranformedUiEvaluatedColumnDetails.setColumnName((String) map.get("columnName"));
      tranformedUiEvaluatedColumnDetails.setKdxDataType(ColumnType.getColumnType((String) map.get("kdxDataType")));
      tranformedUiEvaluatedColumnDetails.setGranularity(GranularityType.getType((String) map.get("granularity")));
      tranformedUiEvaluatedColumnDetails.setFormat((String) map.get("format"));
      tranformedUiEvaluatedColumnDetails.setUnit((String) map.get("unit"));
      UIPublishedFileColumnInfo transformedColumnInfo = new UIPublishedFileColumnInfo();
      transposeFromTokens(transformedColumnInfo, (Map) map.get("columnDetail"));
      tranformedUiEvaluatedColumnDetails.setColumnDetail(transformedColumnInfo);
   }

   public IPublishedFileColumnEvaluationServiceHandler getPublishedFileColumnEvaluationServiceHandler () {
      return publishedFileColumnEvaluationServiceHandler;
   }

   public void setPublishedFileColumnEvaluationServiceHandler (
            IPublishedFileColumnEvaluationServiceHandler publishedFileColumnEvaluationServiceHandler) {
      this.publishedFileColumnEvaluationServiceHandler = publishedFileColumnEvaluationServiceHandler;
   }

   public Long getFileTableId () {
      return fileTableId;
   }

   public void setFileTableId (Long fileTableId) {
      this.fileTableId = fileTableId;
   }

   public String getColumnsUpdateStatus () {
      return columnsUpdateStatus;
   }

   public void setColumnsUpdateStatus (String columnsUpdateStatus) {
      this.columnsUpdateStatus = columnsUpdateStatus;
   }

   public List<PublishedFileTableInfo> getPublishedFileTables () {
      return publishedFileTables;
   }

   public void setPublishedFileTables (List<PublishedFileTableInfo> publishedFileTables) {
      this.publishedFileTables = publishedFileTables;
   }

   public UIPublishedFileColumnInfo getUiPublishedFileColumnInfo () {
      return uiPublishedFileColumnInfo;
   }

   public void setUiPublishedFileColumnInfo (UIPublishedFileColumnInfo uiPublishedFileColumnInfo) {
      this.uiPublishedFileColumnInfo = uiPublishedFileColumnInfo;
   }

   public UITransposedPublishedFileTableDetails getTranposeEvaluatedColumns () {
      return tranposeEvaluatedColumns;
   }

   public void setTranposeEvaluatedColumns (UITransposedPublishedFileTableDetails tranposeEvaluatedColumns) {
      this.tranposeEvaluatedColumns = tranposeEvaluatedColumns;
   }

   public String getEvaluatedColumnList () {
      return evaluatedColumnList;
   }

   public void setEvaluatedColumnList (String evaluatedColumnList) {
      this.evaluatedColumnList = evaluatedColumnList;
   }

   public String getConversionTypeStr () {
      return conversionTypeStr;
   }

   public void setConversionTypeStr (String conversionTypeStr) {
      this.conversionTypeStr = conversionTypeStr;
   }

   public Long getFileTableColumnId () {
      return fileTableColumnId;
   }

   public void setFileTableColumnId (Long fileTableColumnId) {
      this.fileTableColumnId = fileTableColumnId;
   }

   public String getDateFormat () {
      return dateFormat;
   }

   public void setDateFormat (String dateFormat) {
      this.dateFormat = dateFormat;
   }

   @Override
   public List<ConversionType> getConversionTypes () {
      List<ConversionType> conversionTypes = super.getConversionTypes();
      conversionTypes.remove(ConversionType.NULL);
      conversionTypes.add(0, ConversionType.DEFAULT);
      return conversionTypes;
   }

   public Long getPublishedFileId () {
      return publishedFileId;
   }

   public void setPublishedFileId (Long publishedFileId) {
      this.publishedFileId = publishedFileId;
   }

   public Long getJobRequestId () {
      return jobRequestId;
   }

   public void setJobRequestId (Long jobRequestId) {
      this.jobRequestId = jobRequestId;
   }

   public Long getAssetId () {
      return assetId;
   }

   public void setAssetId (Long assetId) {
      this.assetId = assetId;
   }

   public Long getTableId () {
      return tableId;
   }

   public void setTableId (Long tableId) {
      this.tableId = tableId;
   }

   public String getInfoSource () {
      return infoSource;
   }

   public void setInfoSource (String infoSource) {
      this.infoSource = infoSource;
   }

   public List<String> getUpdateValidationErrorMessages () {
      return updateValidationErrorMessages;
   }

   public void setUpdateValidationErrorMessages (List<String> updateValidationErrorMessages) {
      this.updateValidationErrorMessages = updateValidationErrorMessages;
   }

   public Long getApplicationId () {
      return applicationId;
   }

   public void setApplicationId (Long applicationId) {
      this.applicationId = applicationId;
   }

   public IApplicationServiceHandler getApplicationServiceHandler () {
      return applicationServiceHandler;
   }

   public void setApplicationServiceHandler (IApplicationServiceHandler applicationServiceHandler) {
      this.applicationServiceHandler = applicationServiceHandler;
   }

   public Asset getAsset () {
      return asset;
   }

   public void setAsset (Asset asset) {
      this.asset = asset;
   }

   public List<Asset> getAssets () {
      return assets;
   }

   public void setAssets (List<Asset> assets) {
      this.assets = assets;
   }

   public String getSourceName () {
      return sourceName;
   }

   public void setSourceName (String sourceName) {
      this.sourceName = sourceName;
   }

   public PublishedFileInfo getPublishedFileInfo () {
      return publishedFileInfo;
   }

   public void setPublishedFileInfo (PublishedFileInfo publishedFileInfo) {
      this.publishedFileInfo = publishedFileInfo;
   }

}
