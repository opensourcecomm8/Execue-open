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


package com.execue.platform.unstructured.content.transporter.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.entity.Application;
import com.execue.core.common.bean.entity.DataSource;
import com.execue.core.common.bean.querygen.QueryTable;
import com.execue.core.common.type.AssetProviderType;
import com.execue.core.common.type.SuccessFailureType;
import com.execue.core.common.util.ExecueBeanManagementUtil;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.das.dataaccess.ISystemDataAccessService;
import com.execue.das.datatransfer.etl.bean.DataTransferQuery;
import com.execue.das.datatransfer.etl.bean.DataTransferQueryStatus;
import com.execue.das.datatransfer.etl.bean.DataTransferStatus;
import com.execue.das.datatransfer.etl.exception.RemoteDataTransferException;
import com.execue.das.datatransfer.etl.service.IRemoteDataTransferService;
import com.execue.das.exception.SQLGenerationException;
import com.execue.das.jdbc.service.IGenericJDBCService;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.platform.exception.UnstructuredContentTransporterException;
import com.execue.platform.exception.UnstructuredContentTransporterExceptionCodes;
import com.execue.platform.helper.UnstructuredContentTransporterHelper;
import com.execue.platform.unstructured.content.transporter.IUnstructuredContentTransporter;
import com.execue.platform.unstructured.content.transporter.bean.UnstructuredContentTransporterContext;
import com.execue.swi.exception.SDXException;
import com.execue.swi.service.ISDXRetrievalService;
import com.execue.uswh.exception.UnstructuredWarehouseException;
import com.execue.uswh.service.IUnstructuredWarehouseManagementService;
import com.execue.uswh.service.IUnstructuredWarehouseRetrievalService;

public class UnstructuredContentTransporterImpl implements IUnstructuredContentTransporter {

   private ISystemDataAccessService                systemDataAccessService;
   private IGenericJDBCService                     genericJDBCService;
   private IRemoteDataTransferService              remoteDataTransferService;
   private IUnstructuredWarehouseRetrievalService  unstructuredWarehouseRetrievalService;
   private IUnstructuredWarehouseManagementService unstructuredWarehouseManagementService;
   private UnstructuredContentTransporterHelper    unstructuredContentTransporterHelper;
   private ISDXRetrievalService                    sdxRetrievalService;

   private static final Logger                     log = Logger.getLogger(UnstructuredContentTransporterImpl.class);

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.transportation.service.IUnstructuredContentTransporter#transportContent(com.execue.core.common.bean.entity.Application,
    *      com.execue.transportation.bean.UnstructuredContentTransporterContext)
    */
   public void transportContent (Application application,
            UnstructuredContentTransporterContext unstructuredContentTransporterContext)
            throws UnstructuredContentTransporterException {
      try {
         DataSource dataSource = getSdxRetrievalService().getUnstructuredWHDataSourceByAppId(application.getId());
         AssetProviderType providerType = dataSource.getProviderType();
         QueryTable sourceContentTempQueryTable = ExecueBeanManagementUtil.prepareQueryTable(
                  unstructuredContentTransporterContext.getContentTempTableName(), null, dataSource.getOwner());
         String ddlTempTableCreateStatement = getUnstructuredContentTransporterHelper()
                  .constructSourceContentTempTableCreationStatement(providerType, sourceContentTempQueryTable);

         String ddlTempTableTruncateStatement = getUnstructuredContentTransporterHelper()
                  .constructTruncateTableStatement(providerType, sourceContentTempQueryTable);

         Long contextId = application.getId();

         // Prepare the temp table for transport
         if (!getSystemDataAccessService().isTableExists(
                  unstructuredContentTransporterContext.getTargetWareHouseDataSource(),
                  unstructuredContentTransporterContext.getContentTempTableName())) {
            getUnstructuredWarehouseManagementService().createSourceContentTempTable(contextId,
                     ddlTempTableCreateStatement);

            // TODO : -VG- need to handle if auto increment clause not supported like oracle case.
            QueryTable contentTempQueryTable = ExecueBeanManagementUtil.prepareQueryTable(
                     unstructuredContentTransporterContext.getContentTempTableName(), null,
                     unstructuredContentTransporterContext.getTargetWareHouseDataSource().getOwner());
            String ddlTempTableIDColumnAlterStatemet = getUnstructuredContentTransporterHelper()
                     .constructSourceContentTempTableIDAutoIncrementAlterStatement(providerType, contentTempQueryTable);

            getUnstructuredWarehouseManagementService().alterSourceContentTempTableForAutoIncrement(contextId,
                     ddlTempTableIDColumnAlterStatemet);
         } else {
            getUnstructuredWarehouseManagementService().truncateSourceContentTempTable(contextId,
                     ddlTempTableTruncateStatement);
         }

         // Prepare SQLs for ETL Process
         Long maxSourceContentItemIdAtTarget = getUnstructuredWarehouseRetrievalService()
                  .getMaxSourceContentItemIdByContextAndSource(application.getId(),
                           unstructuredContentTransporterContext.getSourceContentDataSource().getId());

         DataTransferQuery dataTransferQuery = getUnstructuredContentTransporterHelper().getSelectInsertStmtForETL(
                  dataSource, application.getId(), maxSourceContentItemIdAtTarget,
                  unstructuredContentTransporterContext.getContentTempTableName(),
                  unstructuredContentTransporterContext.getSourceContentDataSource().getId(), providerType);

         // invoke the ETL Process
         DataTransferStatus remoteDataTransferStatus = getRemoteDataTransferService().transferRemoteData(
                  unstructuredContentTransporterContext.getSourceContentDataSource(),
                  unstructuredContentTransporterContext.getTargetWareHouseDataSource(),
                  dataTransferQuery.getTargetInsertStatement(), dataTransferQuery.getSourceSelectQuery(),
                  ddlTempTableCreateStatement, ddlTempTableTruncateStatement,
                  unstructuredContentTransporterContext.getContentTempTableName());

         List<DataTransferQueryStatus> failedStatuses = new ArrayList<DataTransferQueryStatus>();
         List<DataTransferQueryStatus> successStatuses = new ArrayList<DataTransferQueryStatus>();
         for (DataTransferQueryStatus remoteQueryStatus : remoteDataTransferStatus.getDataTransferQueryStatus()) {
            if (SuccessFailureType.FAILURE.equals(remoteQueryStatus.getQueryExecutionStatus())) {
               failedStatuses.add(remoteQueryStatus);
            } else {
               successStatuses.add(remoteQueryStatus);
            }
         }

         String operationThreadIdentity = getOperationThreadIdentity(unstructuredContentTransporterContext
                  .getTargetWareHouseDataSource().getId(), application.getId(), unstructuredContentTransporterContext
                  .getSourceContentDataSource().getId(), unstructuredContentTransporterContext
                  .getContentTempTableName());

         logSuccessMessages(operationThreadIdentity, successStatuses);

         if (ExecueCoreUtil.isCollectionNotEmpty(failedStatuses)) {
            logFailureMessages(operationThreadIdentity, failedStatuses);
            throw new UnstructuredContentTransporterException(
                     UnstructuredContentTransporterExceptionCodes.REMOTE_DATA_TRANSFER_FAILED_CODE,
                     "Messages are logged");
         }

         // dedup the content items on target temp table
         deduplicateContentTempTableDataOnTarget(contextId, unstructuredContentTransporterContext);

      } catch (DataAccessException dataAccessException) {
         throw new UnstructuredContentTransporterException(dataAccessException.getCode(), dataAccessException);
      } catch (RemoteDataTransferException remoteDataTransferException) {
         throw new UnstructuredContentTransporterException(remoteDataTransferException.getCode(),
                  remoteDataTransferException);
      } catch (UnstructuredWarehouseException unstructuredWarehouseException) {
         throw new UnstructuredContentTransporterException(unstructuredWarehouseException.getCode(),
                  unstructuredWarehouseException);
      } catch (SDXException sdxe) {
         throw new UnstructuredContentTransporterException(sdxe.getCode(), sdxe);
      }
   }

   private String getOperationThreadIdentity (Long targetWarehouseDataSourceId, Long applicationId,
            Long sourceContentWarehouseDataSourceId, String contentTempTableName) {
      StringBuilder sb = new StringBuilder();
      sb.append("> ");
      sb.append(targetWarehouseDataSourceId);
      sb.append(" - ");
      sb.append(applicationId);
      sb.append(" - ");
      sb.append(sourceContentWarehouseDataSourceId);
      sb.append(" : ");
      return sb.toString();
   }

   private void logSuccessMessages (String operationThreadIdentity, List<DataTransferQueryStatus> successStatuses) {
      for (DataTransferQueryStatus remoteQueryStatus : successStatuses) {
         if (log.isInfoEnabled()) {
            log.info(operationThreadIdentity + "Record Count : " + remoteQueryStatus.getRecordCount());
            log.info(operationThreadIdentity + "Start Time   : " + new Date(remoteQueryStatus.getStartTime()));
            log.info(operationThreadIdentity + "End Time     : " + new Date(remoteQueryStatus.getEndTime()));
         }
      }
   }

   private void logFailureMessages (String operationThreadIdentity, List<DataTransferQueryStatus> failedStatuses) {
      for (DataTransferQueryStatus remoteQueryStatus : failedStatuses) {
         log.error(operationThreadIdentity + "Error Code    : " + remoteQueryStatus.getErrorCode());
         log.error(operationThreadIdentity + "Error Message : " + remoteQueryStatus.getErrorMsg());
         log.error(operationThreadIdentity + "Rollback Status : "
                  + remoteQueryStatus.getQueryRollbackStatus().getDescription());
      }
   }

   /**
    * De-duplicate the content items by url
    * 
    * @param contextId
    * @param unstructuredContentTransporterContext
    * @throws UnstructuredWarehouseException
    * @throws SQLGenerationException
    */
   private void deduplicateContentTempTableDataOnTarget (Long contextId,
            UnstructuredContentTransporterContext unstructuredContentTransporterContext)
            throws UnstructuredWarehouseException {

      QueryTable queryTable = ExecueBeanManagementUtil.prepareQueryTable(unstructuredContentTransporterContext
               .getContentTempTableName(), null, unstructuredContentTransporterContext.getTargetWareHouseDataSource()
               .getOwner());
      getUnstructuredWarehouseManagementService().removeDuplicateFromSourceContentTempTable(contextId, queryTable);
   }

   public IRemoteDataTransferService getRemoteDataTransferService () {
      return remoteDataTransferService;
   }

   public void setRemoteDataTransferService (IRemoteDataTransferService remoteDataTransferService) {
      this.remoteDataTransferService = remoteDataTransferService;
   }

   public ISystemDataAccessService getSystemDataAccessService () {
      return systemDataAccessService;
   }

   public void setSystemDataAccessService (ISystemDataAccessService systemDataAccessService) {
      this.systemDataAccessService = systemDataAccessService;
   }

   public IGenericJDBCService getGenericJDBCService () {
      return genericJDBCService;
   }

   public void setGenericJDBCService (IGenericJDBCService genericJDBCService) {
      this.genericJDBCService = genericJDBCService;
   }

   /**
    * @return the unstructuredWarehouseRetrievalService
    */
   public IUnstructuredWarehouseRetrievalService getUnstructuredWarehouseRetrievalService () {
      return unstructuredWarehouseRetrievalService;
   }

   /**
    * @param unstructuredWarehouseRetrievalService
    *           the unstructuredWarehouseRetrievalService to set
    */
   public void setUnstructuredWarehouseRetrievalService (
            IUnstructuredWarehouseRetrievalService unstructuredWarehouseRetrievalService) {
      this.unstructuredWarehouseRetrievalService = unstructuredWarehouseRetrievalService;
   }

   /**
    * @return the unstructuredWarehouseManagementService
    */
   public IUnstructuredWarehouseManagementService getUnstructuredWarehouseManagementService () {
      return unstructuredWarehouseManagementService;
   }

   /**
    * @param unstructuredWarehouseManagementService
    *           the unstructuredWarehouseManagementService to set
    */
   public void setUnstructuredWarehouseManagementService (
            IUnstructuredWarehouseManagementService unstructuredWarehouseManagementService) {
      this.unstructuredWarehouseManagementService = unstructuredWarehouseManagementService;
   }

   public UnstructuredContentTransporterHelper getUnstructuredContentTransporterHelper () {
      return unstructuredContentTransporterHelper;
   }

   public void setUnstructuredContentTransporterHelper (
            UnstructuredContentTransporterHelper unstructuredContentTransporterHelper) {
      this.unstructuredContentTransporterHelper = unstructuredContentTransporterHelper;
   }

   public ISDXRetrievalService getSdxRetrievalService () {
      return sdxRetrievalService;
   }

   public void setSdxRetrievalService (ISDXRetrievalService sdxRetrievalService) {
      this.sdxRetrievalService = sdxRetrievalService;
   }
}