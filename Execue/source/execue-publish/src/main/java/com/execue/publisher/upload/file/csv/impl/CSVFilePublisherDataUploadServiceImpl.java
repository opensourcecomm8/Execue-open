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


package com.execue.publisher.upload.file.csv.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.RandomStringUtils;

import com.csvreader.CsvReader;
import com.execue.core.common.bean.publisher.DBTable;
import com.execue.core.common.bean.publisher.DBTableInfo;
import com.execue.core.common.bean.publisher.PublisherBatchDataReadContext;
import com.execue.core.common.bean.publisher.PublisherMetaReadContext;
import com.execue.core.configuration.ICoreConfigurationService;
import com.execue.core.util.ExecueStringUtil;
import com.execue.publisher.configuration.IPublisherConfigurationService;
import com.execue.publisher.exception.PublisherException;
import com.execue.publisher.exception.PublisherExceptionCodes;
import com.execue.publisher.upload.IPublisherDataUploadService;
import com.execue.publisher.util.PublisherUtilityHelper;
import com.execue.publisher.validator.IPublisherColumnValidator;

/**
 * This class is for uploading the data to datasource from csv file.
 * 
 * @author Vishay
 * @since 20/10/09
 * @version 1.0
 */
public class CSVFilePublisherDataUploadServiceImpl implements IPublisherDataUploadService {

   private IPublisherConfigurationService publisherConfigurationService;
   private ICoreConfigurationService      coreConfigurationService;
   private IPublisherColumnValidator      publisherColumnValidator;

   public List<DBTableInfo> populateDBTables (PublisherMetaReadContext publisherUploadContext)
            throws PublisherException {
      List<DBTableInfo> dbTablesInfo = new ArrayList<DBTableInfo>();
      try {
         CsvReader csvReader = publisherUploadContext.getCsvReader();
         csvReader.readHeaders();
         String[] headers = csvReader.getHeaders();
         DBTableInfo dbTableInfo = new DBTableInfo();
         DBTable dbTable = new DBTable();

         // Prepare the temporary table name
         String specialCharacterRegexTableName = publisherConfigurationService
                  .getEscapeSpecialCharactersForTableNameRegEx();
         String normalizedName = PublisherUtilityHelper.normalizeName(publisherUploadContext.getOriginalFileName(),
                  specialCharacterRegexTableName);
         publisherUploadContext.setNormalizedFileName(normalizedName);

         dbTable.setTableName(getCoreConfigurationService().getPrefixUploadedTempTableName()
                  + "_"
                  + publisherUploadContext.getUserId()
                  + "_"
                  + normalizedName
                  + "_"
                  + RandomStringUtils.randomAlphanumeric(getPublisherConfigurationService()
                           .getTableNameRandomStringLength()));

         dbTable.setTableName(ExecueStringUtil.getNormalizedName(dbTable.getTableName(),
                  getCoreConfigurationService().getMaxDBObjectLength()).toLowerCase());
         dbTable.setDisplayName(publisherUploadContext.getDisplayFileName());
         dbTableInfo.setDbTable(dbTable);
         dbTableInfo.setDbTableColumns(publisherColumnValidator.normalizeColumnNames(headers, publisherUploadContext
                  .isColumnsAvailable()));
         dbTablesInfo.add(dbTableInfo);
      } catch (IOException e) {
         throw new PublisherException(PublisherExceptionCodes.PUBLISHER_DATA_UPLOAD_FAILED_EXCEPTION_CODE, e);
      }
      return dbTablesInfo;
   }

   public List<List<Object>> populateDBTableDataBatch (PublisherBatchDataReadContext batchDataReadContext)
            throws PublisherException {
      return PublisherUtilityHelper.readDataBatch(batchDataReadContext);
   }

   public IPublisherConfigurationService getPublisherConfigurationService () {
      return publisherConfigurationService;
   }

   public void setPublisherConfigurationService (IPublisherConfigurationService publisherConfigurationService) {
      this.publisherConfigurationService = publisherConfigurationService;
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

   public IPublisherColumnValidator getPublisherColumnValidator () {
      return publisherColumnValidator;
   }

   public void setPublisherColumnValidator (IPublisherColumnValidator publisherColumnValidator) {
      this.publisherColumnValidator = publisherColumnValidator;
   }

}