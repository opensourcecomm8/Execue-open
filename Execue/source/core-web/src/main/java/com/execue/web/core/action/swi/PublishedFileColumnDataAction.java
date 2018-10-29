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
import java.util.List;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.Page;
import com.execue.core.exception.ExeCueException;
import com.execue.handler.UIPublishedFileTableColumnData;
import com.execue.handler.bean.grid.IGridBean;
import com.execue.handler.swi.IPublishedFileColumnEvaluationServiceHandler;
import com.execue.swi.exception.PublishedFileException;

public class PublishedFileColumnDataAction extends PaginationGridAction {

   private static final Logger                          log                = Logger
                                                                                    .getLogger(PublishedFileColumnDataAction.class);

   private static final long                            serialVersionUID   = 1L;
   private static final String                          PUBLISHED_FILE     = "PF";
   private static final String                          DATASET_COLLECTION = "DC";

   private Long                                         fileId;
   private Long                                         fileTableId;
   private Long                                         currentPage;
   private static Long                                  COLUMN_SIZE        = 5L;

   private Long                                         assetId;
   private Long                                         tableId;
   private String                                       infoSource         = PUBLISHED_FILE;

   private IPublishedFileColumnEvaluationServiceHandler publishedFileColumnEvaluationServiceHandler;

   @Override
   protected List<? extends IGridBean> processPageGrid () {
      List<UIPublishedFileTableColumnData> publishedFileColumnsData = new ArrayList<UIPublishedFileTableColumnData>();
      try {
         List<List<String>> columnarData = new ArrayList<List<String>>();
         Page metaPageDetail = new Page();
         metaPageDetail.setRequestedPage(currentPage);
         metaPageDetail.setPageSize(COLUMN_SIZE);
         if (DATASET_COLLECTION.equals(getInfoSource())) {
            columnarData = getPublishedFileColumnEvaluationServiceHandler().getAssetTableDataFromSourceByPage(assetId,
                     tableId, metaPageDetail, getPageDetail());
         } else {
            columnarData = publishedFileColumnEvaluationServiceHandler.getUploadedFileDataFromSource(fileId,
                     fileTableId, metaPageDetail, getPageDetail());
         }
         for (List<String> columnarRow : columnarData) {
            UIPublishedFileTableColumnData fileTableRowColumnsData = new UIPublishedFileTableColumnData();
            fileTableRowColumnsData.setCells(columnarRow);
            publishedFileColumnsData.add(fileTableRowColumnsData);
         }
      } catch (PublishedFileException e) {
         e.printStackTrace();
      } catch (ExeCueException e) {
         e.printStackTrace();
      }
      return publishedFileColumnsData;
   }

   public IPublishedFileColumnEvaluationServiceHandler getPublishedFileColumnEvaluationServiceHandler () {
      return publishedFileColumnEvaluationServiceHandler;
   }

   public void setPublishedFileColumnEvaluationServiceHandler (
            IPublishedFileColumnEvaluationServiceHandler publishedFileColumnEvaluationServiceHandler) {
      this.publishedFileColumnEvaluationServiceHandler = publishedFileColumnEvaluationServiceHandler;
   }

   public Long getFileId () {
      return fileId;
   }

   public void setFileId (Long fileId) {
      this.fileId = fileId;
   }

   public Long getFileTableId () {
      return fileTableId;
   }

   public void setFileTableId (Long fileTableId) {
      this.fileTableId = fileTableId;
   }

   public Long getCurrentPage () {
      return currentPage;
   }

   public void setCurrentPage (Long currentPage) {
      this.currentPage = currentPage;
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

}
