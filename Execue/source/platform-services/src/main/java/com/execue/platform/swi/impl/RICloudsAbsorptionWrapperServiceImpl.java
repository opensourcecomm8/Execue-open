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

import java.util.List;

import com.execue.core.common.bean.entity.Cloud;
import com.execue.core.common.bean.entity.CloudComponent;
import com.execue.core.common.bean.entity.RICloud;
import com.execue.core.common.type.BusinessEntityType;
import com.execue.core.common.type.CloudCategory;
import com.execue.core.common.type.ComponentCategory;
import com.execue.core.common.util.ExecueBeanCloneUtil;
import com.execue.platform.helper.IndexFormManagementHelper;
import com.execue.platform.swi.IRICloudsAbsorptionWrapperService;
import com.execue.swi.configuration.ISWIConfigurationService;
import com.execue.swi.exception.KDXException;
import com.execue.swi.service.IKDXCloudRetrievalService;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.swi.service.IRICloudsAbsorptionService;

public class RICloudsAbsorptionWrapperServiceImpl implements IRICloudsAbsorptionWrapperService {

   private IndexFormManagementHelper  indexFormManagementHelper;
   private IKDXCloudRetrievalService  kdxCloudRetrievalService;
   private IKDXRetrievalService       kdxRetrievalService;
   private ISWIConfigurationService   swiConfigurationService;
   private IRICloudsAbsorptionService riCloudsAbsorptionService;

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.swi.IRICloudsAbsorptionService#regenerateRICloudFromCloud(com.execue.core.common.bean
    *      .entity.Cloud)
    */
   public void regenerateRICloudFromCloud (Cloud cloud, Long modelId) throws KDXException {
      Long modelGroupId = getKdxRetrievalService().getPrimaryGroup(modelId).getId();
      getRiCloudsAbsorptionService().truncateRICloudByCloudId(cloud.getId());
      List<CloudComponent> cloudComponents = getKdxCloudRetrievalService().getAllCloudComponentsByCloudIdWithDetails(
               cloud.getId());
      for (CloudComponent cloudComponent : cloudComponents) {
         regenerateRICloudEntries(cloud.getId(), cloudComponent, modelGroupId);
      }
   }

   public void truncateRICloudByCloudId (Long cloudId) throws KDXException {
      getRiCloudsAbsorptionService().truncateRICloudByCloudId(cloudId);
   }

   /**
    * Get the prepared RI Cloud entries from the helper class. clear off the old entries and create the newly prepared
    * entries
    * 
    * @param cloudComponent
    * @param clearExistingEntries
    * @throws KDXException
    */
   private void regenerateRICloudEntries (Long cloudId, CloudComponent cloudComponent, Long modelGroupId)
            throws KDXException {
      RICloud primaryRICloud = getIndexFormManagementHelper().prepareRICloudEntry(cloudComponent, modelGroupId);

      RICloud secondaryRICloud = null;
      if (isSecondaryRICloudEntryRequired(primaryRICloud)) {
         secondaryRICloud = ExecueBeanCloneUtil.cloneRICloud(primaryRICloud);
         secondaryRICloud.setComponentBusinessEntityId(primaryRICloud.getComponentTypeBusinessEntityId());
         secondaryRICloud.setComponentName(primaryRICloud.getComponentTypeName());
         secondaryRICloud.setComponentCategory(ComponentCategory.TYPE);
      }

      getRiCloudsAbsorptionService().clearRICloudEntriesByComponentBEId(cloudId,
               cloudComponent.getComponentBed().getId());

      getRiCloudsAbsorptionService().createRICloud(primaryRICloud);

      if (secondaryRICloud != null) {
         secondaryRICloud.setPrimaryRICloudId(primaryRICloud.getId());
         getRiCloudsAbsorptionService().createRICloud(secondaryRICloud);
      }
   }

   private boolean isSecondaryRICloudEntryRequired (RICloud primaryRICloud) {
      boolean isSecondaryEntryRequired = false;
      if (CloudCategory.APP_CLOUD.equals(primaryRICloud.getCloudCategory())
               && BusinessEntityType.CONCEPT.equals(primaryRICloud.getRepresentativeEntityType())
               && !getSwiConfigurationService().getTypeBEDIdsRestrictedForSecondaryRICloudEntry().contains(
                        primaryRICloud.getComponentTypeBusinessEntityId())) {
         isSecondaryEntryRequired = true;
      }
      return isSecondaryEntryRequired;
   }

   /**
    * @return the indexFormManagementHelper
    */
   public IndexFormManagementHelper getIndexFormManagementHelper () {
      return indexFormManagementHelper;
   }

   /**
    * @param indexFormManagementHelper
    *           the indexFormManagementHelper to set
    */
   public void setIndexFormManagementHelper (IndexFormManagementHelper indexFormManagementHelper) {
      this.indexFormManagementHelper = indexFormManagementHelper;
   }

   /**
    * @return the kdxCloudRetrievalService
    */
   public IKDXCloudRetrievalService getKdxCloudRetrievalService () {
      return kdxCloudRetrievalService;
   }

   /**
    * @param kdxCloudRetrievalService
    *           the kdxCloudRetrievalService to set
    */
   public void setKdxCloudRetrievalService (IKDXCloudRetrievalService kdxCloudRetrievalService) {
      this.kdxCloudRetrievalService = kdxCloudRetrievalService;
   }

   /**
    * @return the kdxRetrievalService
    */
   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   /**
    * @param kdxRetrievalService
    *           the kdxRetrievalService to set
    */
   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }

   /**
    * @return the swiConfigurationService
    */
   public ISWIConfigurationService getSwiConfigurationService () {
      return swiConfigurationService;
   }

   /**
    * @param swiConfigurationService
    *           the swiConfigurationService to set
    */
   public void setSwiConfigurationService (ISWIConfigurationService swiConfigurationService) {
      this.swiConfigurationService = swiConfigurationService;
   }

   public IRICloudsAbsorptionService getRiCloudsAbsorptionService () {
      return riCloudsAbsorptionService;
   }

   public void setRiCloudsAbsorptionService (IRICloudsAbsorptionService riCloudsAbsorptionService) {
      this.riCloudsAbsorptionService = riCloudsAbsorptionService;
   }

}
