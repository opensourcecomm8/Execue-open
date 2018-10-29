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


/**
 * 
 */
package com.execue.scheduler.helper;

import java.util.List;

import com.execue.ac.exception.AnswersCatalogException;
import com.execue.core.common.bean.AnswersCatalogManagementQueueIdentifier;
import com.execue.core.common.bean.ac.AnswersCatalogManagementQueue;
import com.execue.core.common.bean.ac.AnswersCatalogUpdationContext;
import com.execue.core.common.bean.ac.CubeCreationContext;
import com.execue.core.common.bean.ac.MartCreationContext;
import com.execue.core.common.bean.swi.AssetDeletionContext;
import com.execue.core.common.bean.swi.AssetSyncAbsorptionContext;
import com.execue.core.common.type.AnswersCatalogOperationType;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.qdata.configuration.IQueryDataConfigurationService;
import com.execue.scheduler.service.IAssetDeletionJobService;
import com.execue.scheduler.service.ICubeCreationJobService;
import com.execue.scheduler.service.ICubeRefreshJobService;
import com.execue.scheduler.service.ICubeUpdationJobService;
import com.execue.scheduler.service.IDataMartCreationJobService;
import com.execue.scheduler.service.IDataMartRefreshJobService;
import com.execue.scheduler.service.IDataMartUpdationJobService;
import com.execue.scheduler.service.IParentAssetSyncAbsorptionJobService;
import com.execue.swi.exception.SDXException;
import com.thoughtworks.xstream.XStream;

/**
 * @author jitendra
 *
 */
public class AnswersCatalogMgmtQueueJobServiceHelper {

   private IQueryDataConfigurationService       queryDataConfigurationService;

   private ICubeCreationJobService              cubeCreationJobService;
   private ICubeUpdationJobService              cubeUpdationJobService;
   private ICubeRefreshJobService               cubeRefreshJobService;
   private IDataMartCreationJobService          dataMartCreationJobService;
   private IDataMartUpdationJobService          dataMartUpdationJobService;
   private IDataMartRefreshJobService           dataMartRefreshJobService;
   private IParentAssetSyncAbsorptionJobService parentAssetSyncAbsorptionJobService;
   private IAssetDeletionJobService             assetDeletionJobService;

   public void processAnswersCatalogManagementQueueJob (
            List<AnswersCatalogManagementQueue> answersCatalogManagementQueues) throws AnswersCatalogException {

      if (ExecueCoreUtil.isCollectionEmpty(answersCatalogManagementQueues)) {
         return;
      }
      for (AnswersCatalogManagementQueue answersCatalogManagementQueue : answersCatalogManagementQueues) {
         AnswersCatalogOperationType operationType = answersCatalogManagementQueue.getOperationType();
         String operationContext = answersCatalogManagementQueue.getOperationContext();
         AnswersCatalogManagementQueueIdentifier answersCatalogManagementQueueIdentifier = (AnswersCatalogManagementQueueIdentifier) new XStream()
                  .fromXML(operationContext);
         answersCatalogManagementQueueIdentifier.setAnswersCatalogManagementQueueId(answersCatalogManagementQueue
                  .getId());
         answersCatalogManagementQueueIdentifier.setOperationType(answersCatalogManagementQueue.getOperationType());
         String dependentManagementId = answersCatalogManagementQueue.getDependentManagementId();
         if (ExecueCoreUtil.isNotEmpty(dependentManagementId)) {
            answersCatalogManagementQueueIdentifier.setDependantACMQIds(ExecueCoreUtil.getLongListFromString(
                     dependentManagementId, getQueryDataConfigurationService().getSqlConcatDelimeter()));
         }

         if (AnswersCatalogOperationType.CUBE_CREATION == operationType) {

            getCubeCreationJobService().scheduleCubeCreationJob(
                     (CubeCreationContext) answersCatalogManagementQueueIdentifier);

         } else if (AnswersCatalogOperationType.CUBE_UPDATION == operationType) {

            getCubeUpdationJobService().scheduleCubeUpdationJob(
                     (AnswersCatalogUpdationContext) answersCatalogManagementQueueIdentifier);

         } else if (AnswersCatalogOperationType.CUBE_REFRESH == operationType) {

            getCubeRefreshJobService().scheduleCubeRefreshJob(
                     (AnswersCatalogUpdationContext) answersCatalogManagementQueueIdentifier);

         } else if (AnswersCatalogOperationType.MART_CREATION == operationType) {

            getDataMartCreationJobService().scheduleMartCreationJob(
                     (MartCreationContext) answersCatalogManagementQueueIdentifier);

         } else if (AnswersCatalogOperationType.MART_UPDATION == operationType) {

            getDataMartUpdationJobService().scheduleMartUpdationJob(
                     (AnswersCatalogUpdationContext) answersCatalogManagementQueueIdentifier);

         } else if (AnswersCatalogOperationType.MART_REFRESH == operationType) {

            getDataMartRefreshJobService().scheduleMartRefreshJob(
                     (AnswersCatalogUpdationContext) answersCatalogManagementQueueIdentifier);

         } else if (AnswersCatalogOperationType.PARENT_ASSET_SYNC_ABSORPTION == operationType) {
            getParentAssetSyncAbsorptionJobService().scheduleParentAssetSyncAbsortpionJob(
                     (AssetSyncAbsorptionContext) answersCatalogManagementQueueIdentifier);

         } else if (AnswersCatalogOperationType.ASSET_DELETION == operationType
                  || AnswersCatalogOperationType.CUBE_DELETION == operationType
                  || AnswersCatalogOperationType.MART_DELETION == operationType) {
            try {
               getAssetDeletionJobService().scheduleAssetDeletionJob(
                        (AssetDeletionContext) answersCatalogManagementQueueIdentifier);
            } catch (SDXException e) {
               throw new AnswersCatalogException(e.getCode(), e);
            }

         }
      }

   }

   /**
   * @return the cubeCreationJobService
   */
   public ICubeCreationJobService getCubeCreationJobService () {
      return cubeCreationJobService;
   }

   /**
   * @param cubeCreationJobService the cubeCreationJobService to set
   */
   public void setCubeCreationJobService (ICubeCreationJobService cubeCreationJobService) {
      this.cubeCreationJobService = cubeCreationJobService;
   }

   /**
   * @return the cubeUpdationJobService
   */
   public ICubeUpdationJobService getCubeUpdationJobService () {
      return cubeUpdationJobService;
   }

   /**
   * @param cubeUpdationJobService the cubeUpdationJobService to set
   */
   public void setCubeUpdationJobService (ICubeUpdationJobService cubeUpdationJobService) {
      this.cubeUpdationJobService = cubeUpdationJobService;
   }

   /**
   * @return the cubeRefreshJobService
   */
   public ICubeRefreshJobService getCubeRefreshJobService () {
      return cubeRefreshJobService;
   }

   /**
   * @param cubeRefreshJobService the cubeRefreshJobService to set
   */
   public void setCubeRefreshJobService (ICubeRefreshJobService cubeRefreshJobService) {
      this.cubeRefreshJobService = cubeRefreshJobService;
   }

   /**
   * @return the dataMartCreationJobService
   */
   public IDataMartCreationJobService getDataMartCreationJobService () {
      return dataMartCreationJobService;
   }

   /**
   * @param dataMartCreationJobService the dataMartCreationJobService to set
   */
   public void setDataMartCreationJobService (IDataMartCreationJobService dataMartCreationJobService) {
      this.dataMartCreationJobService = dataMartCreationJobService;
   }

   /**
   * @return the dataMartUpdationJobService
   */
   public IDataMartUpdationJobService getDataMartUpdationJobService () {
      return dataMartUpdationJobService;
   }

   /**
   * @param dataMartUpdationJobService the dataMartUpdationJobService to set
   */
   public void setDataMartUpdationJobService (IDataMartUpdationJobService dataMartUpdationJobService) {
      this.dataMartUpdationJobService = dataMartUpdationJobService;
   }

   /**
   * @return the dataMartRefreshJobService
   */
   public IDataMartRefreshJobService getDataMartRefreshJobService () {
      return dataMartRefreshJobService;
   }

   /**
   * @param dataMartRefreshJobService the dataMartRefreshJobService to set
   */
   public void setDataMartRefreshJobService (IDataMartRefreshJobService dataMartRefreshJobService) {
      this.dataMartRefreshJobService = dataMartRefreshJobService;
   }

   /**
   * @return the parentAssetSyncAbsorptionJobService
   */
   public IParentAssetSyncAbsorptionJobService getParentAssetSyncAbsorptionJobService () {
      return parentAssetSyncAbsorptionJobService;
   }

   /**
   * @param parentAssetSyncAbsorptionJobService the parentAssetSyncAbsorptionJobService to set
   */
   public void setParentAssetSyncAbsorptionJobService (
            IParentAssetSyncAbsorptionJobService parentAssetSyncAbsorptionJobService) {
      this.parentAssetSyncAbsorptionJobService = parentAssetSyncAbsorptionJobService;
   }

   /**
    * @return the assetDeletionJobService
    */
   public IAssetDeletionJobService getAssetDeletionJobService () {
      return assetDeletionJobService;
   }

   /**
    * @param assetDeletionJobService the assetDeletionJobService to set
    */
   public void setAssetDeletionJobService (IAssetDeletionJobService assetDeletionJobService) {
      this.assetDeletionJobService = assetDeletionJobService;
   }

   /**
    * @return the queryDataConfigurationService
    */
   public IQueryDataConfigurationService getQueryDataConfigurationService () {
      return queryDataConfigurationService;
   }

   /**
    * @param queryDataConfigurationService the queryDataConfigurationService to set
    */
   public void setQueryDataConfigurationService (IQueryDataConfigurationService queryDataConfigurationService) {
      this.queryDataConfigurationService = queryDataConfigurationService;
   }
}