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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.PublishedFileTableDetails;
import com.execue.core.common.bean.entity.PublishedFileTableInfo;
import com.execue.core.common.bean.entity.Tabl;
import com.execue.core.common.type.ConversionType;
import com.execue.core.common.type.DataType;
import com.execue.core.common.util.ExecueBeanManagementUtil;
import com.execue.core.common.util.ExecueBeanUtil;
import com.execue.core.configuration.ICoreConfigurationService;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.platform.swi.IAssetSourcePublisherMergeService;
import com.execue.swi.exception.PublishedFileException;
import com.execue.swi.exception.SWIException;
import com.execue.swi.service.IPublishedFileRetrievalService;
import com.execue.swi.service.ISDXManagementService;

public class AssetSourcePublisherMergeServiceImpl implements IAssetSourcePublisherMergeService {

   private ISDXManagementService          sdxManagementService;
   private IPublishedFileRetrievalService publishedFileRetrievalService;
   private ICoreConfigurationService      coreConfigurationService;

   public void mergeAssetPublisherColumns (Asset asset, Tabl table, List<Colum> columns, Long userId)
            throws SWIException {
      try {
         PublishedFileTableInfo publishedFileTableInfo = getPublishedFileRetrievalService().getPublishedFileTableInfo(
                  userId, table.getName());
         if (publishedFileTableInfo != null) {
            List<PublishedFileTableDetails> publishedFileTableDetailsList = getPublishedFileRetrievalService()
                     .getPublishedFileTableDetailsByTableId(publishedFileTableInfo.getId());
            for (Colum colum : columns) {
               PublishedFileTableDetails matchedPublishedFileTableDetails = getCorrespondingPublishedFileTableDetails(
                        publishedFileTableDetailsList, colum);
               colum.setKdxDataType(matchedPublishedFileTableDetails.getKdxDataType());
               if (!ConversionType.DEFAULT.equals(matchedPublishedFileTableDetails.getUnitType())) {
                  colum.setConversionType(matchedPublishedFileTableDetails.getUnitType());
               }
               colum.setDataFormat(matchedPublishedFileTableDetails.getFormat());
               handleDateFormats(asset, colum, matchedPublishedFileTableDetails.getFormat());
               colum.setUnit(matchedPublishedFileTableDetails.getUnit());
               colum.setGranularity(matchedPublishedFileTableDetails.getGranularity());
               colum.setDefaultMetric(matchedPublishedFileTableDetails.getDefaultMetric());
               getSdxManagementService().updateColumn(asset.getId(), table.getId(), colum);
            }
         }
      } catch (PublishedFileException publishedFileException) {
         throw new SWIException(publishedFileException.getCode(), publishedFileException);
      }
   }

   public void mergeSourcePublisherColumns (Asset asset, Tabl table, List<Colum> columns, Long userId)
            throws SWIException {
      try {
         PublishedFileTableInfo publishedFileTableInfo = getPublishedFileRetrievalService().getPublishedFileTableInfo(
                  userId, table.getName());
         if (publishedFileTableInfo != null) {
            List<PublishedFileTableDetails> publishedFileTableDetailsList = getPublishedFileRetrievalService()
                     .getPublishedFileTableDetailsByTableId(publishedFileTableInfo.getId());
            for (Colum colum : columns) {
               PublishedFileTableDetails matchedPublishedFileTableDetails = getCorrespondingPublishedFileTableDetails(
                        publishedFileTableDetailsList, colum);
               if (matchedPublishedFileTableDetails != null) {
                  String format = matchedPublishedFileTableDetails.getFormat();
                  if (ExecueCoreUtil.isNotEmpty(format)) {
                     colum.setConversionType(ConversionType.DATE);
                     colum.setFileDateFormat(format);
                     if (ExecueBeanManagementUtil.isDataTypeDateFamily(colum)) {
                        if (DataType.DATE.equals(colum.getDataType())) {
                           colum.setDataFormat(ExecueBeanUtil.getDefaultDateFormat(asset.getDataSource()
                                    .getProviderType(), (ICoreConfigurationService) getCoreConfigurationService()));
                        } else {
                           colum.setDataFormat(ExecueBeanUtil.getDefaultDateTimeFormat(asset.getDataSource()
                                    .getProviderType(), (ICoreConfigurationService) getCoreConfigurationService()));
                        }
                     }
                  } else {
                     colum.setDataFormat(format);
                  }
               }
            }
         }
      } catch (PublishedFileException publishedFileException) {
         throw new SWIException(publishedFileException.getCode(), publishedFileException);
      }
   }

   public void mergeSourcePublisherTables (List<Tabl> tables, Long userId) throws SWIException {
      try {
         List<String> evaluatedTableNames = new ArrayList<String>();
         for (Tabl tabl : tables) {
            evaluatedTableNames.add(tabl.getName());
         }
         if (ExecueCoreUtil.isCollectionNotEmpty(evaluatedTableNames)) {

            List<PublishedFileTableInfo> publishedFileTablesInfo = getPublishedFileRetrievalService()
                     .getPublishedFileTablesInfo(userId, evaluatedTableNames);
            Map<String, String> nameDisplayNameMap = new HashMap<String, String>();
            for (PublishedFileTableInfo publishedFileTableInfo : publishedFileTablesInfo) {
               nameDisplayNameMap.put(publishedFileTableInfo.getEvaluatedTableName().toLowerCase(),
                        publishedFileTableInfo.getDisplayTableName());
            }
            for (Tabl tabl : tables) {
               tabl.setDisplayName(nameDisplayNameMap.get(tabl.getName().toLowerCase()));
            }
         }
      } catch (PublishedFileException publishedFileException) {
         throw new SWIException(publishedFileException.getCode(), publishedFileException);
      }
   }

   private PublishedFileTableDetails getCorrespondingPublishedFileTableDetails (
            List<PublishedFileTableDetails> publishedFileTableDetailsList, Colum colum) {
      PublishedFileTableDetails matchedPublishedFileTableDetails = null;
      for (PublishedFileTableDetails publishedFileTableDetails : publishedFileTableDetailsList) {
         if (publishedFileTableDetails.getEvaluatedColumnName().equalsIgnoreCase(colum.getName())) {
            matchedPublishedFileTableDetails = publishedFileTableDetails;
            break;
         }
      }
      return matchedPublishedFileTableDetails;
   }

   private void handleDateFormats (Asset asset, Colum colum, String format) {
      if (ExecueCoreUtil.isNotEmpty(format)) {
         colum.setFileDateFormat(format);
         if (ExecueBeanManagementUtil.isDataTypeDateFamily(colum)) {
            if (DataType.DATE.equals(colum.getDataType())) {
               colum.setDataFormat(ExecueBeanUtil.getDefaultDateFormat(asset.getDataSource().getProviderType(),
                        (ICoreConfigurationService) getCoreConfigurationService()));
            } else {
               colum.setDataFormat(ExecueBeanUtil.getDefaultDateTimeFormat(asset.getDataSource().getProviderType(),
                        (ICoreConfigurationService) getCoreConfigurationService()));
            }
         } else {
            colum.setDataFormat(format);
            colum.setFileDateFormat(format);
         }
      }
   }

   /**
    * @return the coreConfigurationService
    */
   public ICoreConfigurationService getCoreConfigurationService () {
      return coreConfigurationService;
   }

   /**
    * @param coreConfigurationService the coreConfigurationService to set
    */
   public void setCoreConfigurationService (ICoreConfigurationService coreConfigurationService) {
      this.coreConfigurationService = coreConfigurationService;
   }

   public ISDXManagementService getSdxManagementService () {
      return sdxManagementService;
   }

   public void setSdxManagementService (ISDXManagementService sdxManagementService) {
      this.sdxManagementService = sdxManagementService;
   }

   public IPublishedFileRetrievalService getPublishedFileRetrievalService () {
      return publishedFileRetrievalService;
   }

   public void setPublishedFileRetrievalService (IPublishedFileRetrievalService publishedFileRetrievalService) {
      this.publishedFileRetrievalService = publishedFileRetrievalService;
   }

}
