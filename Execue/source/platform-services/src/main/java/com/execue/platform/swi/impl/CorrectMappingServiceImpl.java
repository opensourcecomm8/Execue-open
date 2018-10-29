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
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.JobOperationalStatus;
import com.execue.core.common.bean.entity.Mapping;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.common.bean.governor.LightAssetEntityDefinitionInfo;
import com.execue.core.common.bean.swi.CorrectMappingsMaintenanceContext;
import com.execue.core.common.type.ColumnType;
import com.execue.core.common.type.ConstraintSubType;
import com.execue.core.common.type.JobStatus;
import com.execue.core.common.type.LookupType;
import com.execue.core.common.type.PrimaryMappingType;
import com.execue.core.common.util.ExecueBeanManagementUtil;
import com.execue.core.common.util.ExecueBeanUtil;
import com.execue.core.exception.ExeCueException;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.platform.swi.ICorrectMappingService;
import com.execue.qdata.exception.QueryDataException;
import com.execue.qdata.service.IJobDataService;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.MappingException;
import com.execue.swi.exception.SDXException;
import com.execue.swi.exception.SWIExceptionCodes;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.swi.service.IMappingManagementService;
import com.execue.swi.service.IMappingRetrievalService;
import com.execue.swi.service.ISDXRetrievalService;

public class CorrectMappingServiceImpl implements ICorrectMappingService {

   private static final Logger       logger = Logger.getLogger(CorrectMappingServiceImpl.class);

   private ISDXRetrievalService      sdxRetrievalService;
   private IKDXRetrievalService      kdxRetrievalService;
   private IMappingRetrievalService  mappingRetrievalService;
   private IMappingManagementService mappingManagementService;
   private IJobDataService           jobDataService;

   public void correctMappings (CorrectMappingsMaintenanceContext correctMappingsMaintenanceContext)
            throws MappingException {
      JobOperationalStatus jobOperationalStatus = null;
      try {
         if (correctMappingsMaintenanceContext.getJobRequest() != null) {
            jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(
                     correctMappingsMaintenanceContext.getJobRequest(), "Processing correct mapping for asset",
                     JobStatus.INPROGRESS, null, new Date());
            jobDataService.createJobOperationStatus(jobOperationalStatus);
         }
         List<Long> assetIds = correctMappingsMaintenanceContext.getAssetIds();
         if (ExecueCoreUtil.isCollectionEmpty(assetIds)) {
            assetIds = getSdxRetrievalService().getAllAssetIds(correctMappingsMaintenanceContext.getApplicationId());
         }
         correctMappingsForAssets(correctMappingsMaintenanceContext.getApplicationId(), assetIds);
         if (correctMappingsMaintenanceContext.getJobRequest() != null) {
            jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                     JobStatus.SUCCESS, null, new Date());
            getJobDataService().updateJobOperationStatus(jobOperationalStatus);
         }
      } catch (ExeCueException exception) {
         if (jobOperationalStatus != null) {
            ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus, JobStatus.FAILURE, exception
                     .getMessage(), new Date());
            try {
               getJobDataService().updateJobOperationStatus(jobOperationalStatus);
            } catch (QueryDataException e1) {
               throw new MappingException(SWIExceptionCodes.CORRECT_MAPPINGS_JOB_SCHEDULING_FAILURE_CODE, exception);
            }
         }
         exception.printStackTrace();
         throw new MappingException(SWIExceptionCodes.CORRECT_MAPPINGS_JOB_SCHEDULING_FAILURE_CODE, exception);
      }
   }

   public void correctMappingsForAssets (Long applicationId, List<Long> assetIds) throws MappingException {
      try {
         Model model = getKdxRetrievalService().getModelsByApplicationId(applicationId).get(0);
         for (Long assetId : assetIds) {
            correctMappingsForAsset(assetId, model);
         }
      } catch (KDXException kdxException) {
         throw new MappingException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, kdxException);
      } catch (SDXException sdxException) {
         throw new MappingException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, sdxException);
      }

   }

   public void correctMappingsForAsset (Long applicationId, Long assetId) throws MappingException {
      List<Long> assetIds = new ArrayList<Long>();
      assetIds.add(assetId);
      correctMappingsForAssets(applicationId, assetIds);
   }

   private void correctMappingsForAsset (Long assetId, Model model) throws MappingException, SDXException {
      if (logger.isDebugEnabled()) {
         logger.debug("Processing correct mappings for asset [" + assetId + "]");
      }
      getMappingManagementService().updatePrimaryForUniqueMappingsByAsset(assetId);
      Set<Mapping> correctedMappings = correctNonUniqueMappingsForAsset(assetId, model.getId());
      if (logger.isDebugEnabled()) {
         logger.debug("Number of non-unique mappings being updated on Asset [" + assetId + "] are : "
                  + correctedMappings.size());
      }
      getMappingManagementService().updateMappings(new ArrayList<Mapping>(correctedMappings));

      // TODO :-VG- code to handle the case of concept mapped to both measure and dimension.
      Asset asset = getSdxRetrievalService().getAsset(assetId);
      if (ExecueBeanUtil.isExecueOwnedCube(asset)) {
         List<Mapping> conceptMappingsWithDiffInNature = getMappingRetrievalService()
                  .getMappingsOfConceptsMappedWithDiffInNature(assetId);
         List<Mapping> measureMappings = getConceptMappedAsMeasureWithDiffInNature(conceptMappingsWithDiffInNature);
         for (Mapping measureMapping : measureMappings) {
            measureMapping.setPrimary(PrimaryMappingType.MEASURE_NON_PRIMARY);
            getMappingManagementService().updateMapping(measureMapping);
         }
      }

      if (logger.isDebugEnabled()) {
         logger.debug("Processed correct mappings for asset [" + assetId + "]");
      }
   }

   private List<Mapping> getConceptMappedAsMeasureWithDiffInNature (List<Mapping> conceptMappingsWithDiffInNature) {
      List<Mapping> measureMappings = new ArrayList<Mapping>();
      for (Mapping mapping : conceptMappingsWithDiffInNature) {
         if (ColumnType.MEASURE.equals(mapping.getAssetEntityDefinition().getColum().getKdxDataType())) {
            measureMappings.add(mapping);
         }
      }
      return measureMappings;
   }

   private LightAssetEntityDefinitionInfo pickCorrectAssetEntityDefinitionInfo (
            List<LightAssetEntityDefinitionInfo> assetEntityDefinitions) throws MappingException {
      // rules for picking the right aed are
      // 3. pick up the dimension type(id, sl, rl, shl,rhl,dimension)
      // 2. if column belongs to lookup table
      // 1. if column is entire pk
      // 4. if column is part of pk
      // if the list of AED's if greater than 0, then we pick up the first one in the list.
      // this will take care of situation if one and only one entry exists.
      // if more than one entry exits, then we will iterate and find the one which is primary key
      // or whose datatype is dimension.
      LightAssetEntityDefinitionInfo correctAssetEntityDefinition = assetEntityDefinitions.get(0);
      boolean correctEntityFound = false;
      try {
         if (assetEntityDefinitions.size() == 1) {
            correctEntityFound = true;
         } else {
            if (!correctEntityFound) {
               for (LightAssetEntityDefinitionInfo assetEntityDefinition : assetEntityDefinitions) {
                  if (isTableTypeLookupCategory(assetEntityDefinition.getTableLookupType())) {
                     correctEntityFound = true;
                     correctAssetEntityDefinition = assetEntityDefinition;
                     break;
                  }
               }
            }
            if (!correctEntityFound) {
               for (LightAssetEntityDefinitionInfo assetEntityDefinition : assetEntityDefinitions) {
                  if (isKDXDataTypeDimensionCategory(assetEntityDefinition.getColumnType())) {
                     correctEntityFound = true;
                     correctAssetEntityDefinition = assetEntityDefinition;
                     break;
                  }
               }
            }
            if (!correctEntityFound) {
               for (LightAssetEntityDefinitionInfo assetEntityDefinition : assetEntityDefinitions) {
                  if (assetEntityDefinition.getPrimaryKey() != null
                           && ConstraintSubType.ENTIRE.equals(assetEntityDefinition.getPrimaryKey())) {
                     correctEntityFound = true;
                     correctAssetEntityDefinition = assetEntityDefinition;
                     break;
                  }
               }
            }
            if (!correctEntityFound) {
               for (LightAssetEntityDefinitionInfo assetEntityDefinition : assetEntityDefinitions) {
                  if (assetEntityDefinition.getPrimaryKey() != null
                           && ConstraintSubType.PART.equals(assetEntityDefinition.getPrimaryKey())) {
                     correctEntityFound = true;
                     correctAssetEntityDefinition = assetEntityDefinition;
                     break;
                  }
               }
            }
         }
      } catch (Exception exception) {
         throw new MappingException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE,
                  "Error in picking CorrectAssetEntityDefinitionInfo", exception.getCause());
      }
      return correctAssetEntityDefinition;
   }

   private boolean isTableTypeLookupCategory (LookupType lookupType) {
      boolean isLookupTypeCategory = false;
      if (LookupType.SIMPLE_LOOKUP.equals(lookupType) || LookupType.RANGE_LOOKUP.equals(lookupType)
               || LookupType.SIMPLEHIERARCHICAL_LOOKUP.equals(lookupType)
               || LookupType.RANGEHIERARCHICAL_LOOKUP.equals(lookupType)) {
         isLookupTypeCategory = true;
      }
      return isLookupTypeCategory;
   }

   private boolean isKDXDataTypeDimensionCategory (ColumnType columnType) {
      boolean isDimensionCategory = false;
      if (ColumnType.ID.equals(columnType) || ColumnType.DIMENSION.equals(columnType)
               || ColumnType.SIMPLE_LOOKUP.equals(columnType) || ColumnType.RANGE_LOOKUP.equals(columnType)
               || ColumnType.SIMPLE_HIERARCHY_LOOKUP.equals(columnType)) {
         isDimensionCategory = true;
      }
      return isDimensionCategory;
   }

   private Set<Mapping> correctNonUniqueMappingsForAsset (Long assetId, Long modelId) throws MappingException {
      List<Long> nonUniquelyMappedBEDIDsForAsset = getMappingRetrievalService().getNonUniquelyMappedBEDIDsForAsset(
               assetId);
      Set<Mapping> primaryMappings = new HashSet<Mapping>();
      for (Long bedID : nonUniquelyMappedBEDIDsForAsset) {
         List<LightAssetEntityDefinitionInfo> lightAssetEntityList = getMappingRetrievalService()
                  .getAssetEntitiesForAsset(bedID, modelId, assetId);
         if (ExecueCoreUtil.isCollectionNotEmpty(lightAssetEntityList)) {
            LightAssetEntityDefinitionInfo lightAssetEntityDefinitionInfo = pickCorrectAssetEntityDefinitionInfo(lightAssetEntityList);
            Mapping primaryMapping = getMappingRetrievalService().getMapping(
                     lightAssetEntityDefinitionInfo.getAssetEntityDefinitionId(), bedID);
            primaryMapping.setPrimary(PrimaryMappingType.PRIMARY);
            primaryMappings.add(primaryMapping);
            // For all other mappings for the same BED on the asset should be set to NO on primary flag
            getMappingManagementService().updateNonPrimaryForNonUniqueMappingsByBED(assetId, bedID);
         }
      }
      return primaryMappings;
   }

   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }

   /**
    * @return the jobDataService
    */
   public IJobDataService getJobDataService () {
      return jobDataService;
   }

   /**
    * @param jobDataService
    *           the jobDataService to set
    */
   public void setJobDataService (IJobDataService jobDataService) {
      this.jobDataService = jobDataService;
   }

   public ISDXRetrievalService getSdxRetrievalService () {
      return sdxRetrievalService;
   }

   public void setSdxRetrievalService (ISDXRetrievalService sdxRetrievalService) {
      this.sdxRetrievalService = sdxRetrievalService;
   }

   public IMappingRetrievalService getMappingRetrievalService () {
      return mappingRetrievalService;
   }

   public void setMappingRetrievalService (IMappingRetrievalService mappingRetrievalService) {
      this.mappingRetrievalService = mappingRetrievalService;
   }

   public IMappingManagementService getMappingManagementService () {
      return mappingManagementService;
   }

   public void setMappingManagementService (IMappingManagementService mappingManagementService) {
      this.mappingManagementService = mappingManagementService;
   }

}
