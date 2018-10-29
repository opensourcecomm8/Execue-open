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


package com.execue.swi.service.impl;

import java.util.List;

import com.execue.core.common.bean.entity.Cloud;
import com.execue.core.common.bean.entity.CloudComponent;
import com.execue.swi.exception.KDXException;
import com.execue.swi.dataaccess.IKDXCloudDataAccessManager;
import com.execue.swi.service.IKDXCloudDeletionService;
import com.execue.swi.service.IKDXCloudRetrievalService;

public class KDXCloudDeletionServiceImpl implements IKDXCloudDeletionService {

   private IKDXCloudDataAccessManager kdxCloudDataAccessManager;
   private IKDXCloudRetrievalService  kdxCloudRetrievalService;

   public void deleteCloudComponentsByCloudId (Long cloudId) throws KDXException {
      getKdxCloudDataAccessManager().deleteCloudComponentsByCloudId(cloudId);
   }

   public void deleteClouds (List<Cloud> clouds) throws KDXException {
      getKdxCloudDataAccessManager().deleteAll(clouds);
   }

   public void removeComponentsFromCloud (List<CloudComponent> componentsToBeRemoved) throws KDXException {
      getKdxCloudDataAccessManager().removeComponentsFromCloud(componentsToBeRemoved);
   }

   public void removeComponentFromCloud (Long cloudId, Long componentBEId) throws KDXException {
      getKdxCloudDataAccessManager().removeComponentFromCloud(cloudId, componentBEId);
   }

   public void removeComponentFromCloudByModelId (Long modelId, Long componentBEId) throws KDXException {
      Cloud cloud = getKdxCloudRetrievalService().getDefaultAppCloud(modelId);
      getKdxCloudDataAccessManager().removeComponentFromCloud(cloud.getId(), componentBEId);
   }

   public IKDXCloudDataAccessManager getKdxCloudDataAccessManager () {
      return kdxCloudDataAccessManager;
   }

   public void setKdxCloudDataAccessManager (IKDXCloudDataAccessManager kdxCloudDataAccessManager) {
      this.kdxCloudDataAccessManager = kdxCloudDataAccessManager;
   }

   public IKDXCloudRetrievalService getKdxCloudRetrievalService () {
      return kdxCloudRetrievalService;
   }

   public void setKdxCloudRetrievalService (IKDXCloudRetrievalService kdxCloudRetrievalService) {
      this.kdxCloudRetrievalService = kdxCloudRetrievalService;
   }

}
