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


package com.execue.platform.swi.operation.synchronization.impl;

import static com.execue.platform.swi.operation.AssetOperationHelper.convertXMLtoAssetSyncObject;
import static com.execue.platform.swi.operation.AssetOperationHelper.tranformAssetOperationMembers;
import static com.execue.platform.swi.operation.AssetOperationHelper.transformAssetOperationColumns;
import static com.execue.platform.swi.operation.AssetOperationHelper.transformAssetOperationTable;
import static com.execue.platform.swi.operation.AssetOperationHelper.transformAssetOperationTables;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.AssetEntityDefinition;
import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.Mapping;
import com.execue.core.common.bean.entity.Membr;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.common.bean.entity.Tabl;
import com.execue.core.common.bean.swi.AssetOperationColumn;
import com.execue.core.common.bean.swi.AssetOperationMember;
import com.execue.core.common.bean.swi.AssetOperationTable;
import com.execue.core.common.bean.swi.AssetSyncAbsorptionContext;
import com.execue.core.common.bean.swi.AssetSyncInfo;
import com.execue.core.common.bean.swi.BusinessModelPreparationContext;
import com.execue.core.common.bean.swi.ColumnSyncInfo;
import com.execue.core.common.bean.swi.ColumnUpdationInfo;
import com.execue.core.common.bean.swi.MemberSyncInfo;
import com.execue.core.common.bean.swi.MemberUpdationInfo;
import com.execue.core.common.bean.swi.TableSyncInfo;
import com.execue.core.common.bean.swi.TableUpdationInfo;
import com.execue.core.common.type.AssetOperationType;
import com.execue.core.common.type.ColumnType;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.platform.IBusinessModelPreparationService;
import com.execue.platform.exception.AssetSynchronizationException;
import com.execue.platform.swi.ISDX2KDXMappingService;
import com.execue.platform.swi.ISDXDeletionWrapperService;
import com.execue.platform.swi.operation.synchronization.IParentAssetSyncAbsorptionService;
import com.execue.swi.exception.AssetOperationExceptionCodes;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.MappingException;
import com.execue.swi.exception.SDX2KDXMappingException;
import com.execue.swi.exception.SDXException;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.swi.service.IMappingRetrievalService;
import com.execue.swi.service.ISDXDeletionService;
import com.execue.swi.service.ISDXManagementService;
import com.execue.swi.service.ISDXRetrievalService;

/**
 * This service absorbs the AssetSync information into swi for particular asset
 * 
 * @author Vishay
 * @version 1.0
 * @since 20/08/09
 */
public class ParentAssetSyncAbsorptionServiceImpl implements IParentAssetSyncAbsorptionService {

   private static final Logger              log = Logger.getLogger(ParentAssetSyncAbsorptionServiceImpl.class);

   private ISDXRetrievalService             sdxRetrievalService;
   private ISDXManagementService            sdxManagementService;
   private ISDXDeletionService              sdxDeletionService;
   private IMappingRetrievalService         mappingRetrievalService;
   private ISDX2KDXMappingService           sdx2kdxMappingService;
   private IKDXRetrievalService             kdxRetrievalService;
   private IBusinessModelPreparationService businessModelPreparationService;
   private ISDXDeletionWrapperService       sdxDeletionWrapperService;

   // rules for absorption
   // 1. the list of tables which are deleted needs to be deleted with standard call to delete a table
   // 2. updation of table is pretty straightforward, we need to fire update on table
   // 3. columns deleted, if column is lookup column, delete the table else delete the column
   // 4. columns updated, fire update on columns
   // 5. columns added, addition is easy but how to map ????
   // 6. members deletion pretty straight forward
   // 7. members updation pretty straight forward
   // 8. members addition need to find how to map to instance ??
   // TODO : -VG- the fields which are editable at swi console like description we need to evaluate further before
   // changing it
   public boolean absorbAssetSyncInfo (AssetSyncAbsorptionContext assetSyncAbsorptionContext)
            throws AssetSynchronizationException {
      boolean operationStatus = true;
      try {
         operationStatus = absorbSyncInfo(assetSyncAbsorptionContext);

         if (operationStatus) {
            BusinessModelPreparationContext businessModelPreparationContext = new BusinessModelPreparationContext();
            businessModelPreparationContext.setApplicationId(assetSyncAbsorptionContext.getApplicationId());
            businessModelPreparationContext.setModelId(assetSyncAbsorptionContext.getModelId());
            businessModelPreparationContext.setAssetId(assetSyncAbsorptionContext.getAssetId());
            businessModelPreparationContext.setJobRequest(assetSyncAbsorptionContext.getJobRequest());
            businessModelPreparationContext.setUserId(assetSyncAbsorptionContext.getUserId());

            operationStatus = getBusinessModelPreparationService()
                     .prepareBusinessModel(businessModelPreparationContext);
         }
      } catch (KDXException kdxException) {
         throw new AssetSynchronizationException(kdxException.getCode(), kdxException);
      }

      return operationStatus;
   }

   private boolean absorbSyncInfo (AssetSyncAbsorptionContext assetSyncAbsorptionContext)
            throws AssetSynchronizationException {
      try {
         Model model = getKdxRetrievalService().getModelById(assetSyncAbsorptionContext.getModelId());
         String syncData = getSdxRetrievalService().getAssetOperationDataByAssetId(
                  assetSyncAbsorptionContext.getAssetId(), AssetOperationType.ASSET_SYNCHRONIZATION);
         if (ExecueCoreUtil.isEmpty(syncData)) {
            return false;
         }
         AssetSyncInfo assetSyncInfo = convertXMLtoAssetSyncObject(syncData);
         Asset asset = getSdxRetrievalService().getAssetById(assetSyncInfo.getAssetId());
         preProcessing(assetSyncInfo);
         boolean isMemberSyncAbsorptionSuccess = absorbMemberSyncInfo(asset, model, assetSyncInfo.getMemberSyncInfo());
         boolean isColumnSyncAbsorptionSuccess = absorbColumnSyncInfo(asset, model, assetSyncInfo.getColumnSyncInfo());
         boolean isTableSyncAbsorptionSuccess = absorbTableSyncInfo(asset, assetSyncInfo.getTableSyncInfo());
         // TODO : -VG- update the record in db with status updated
         return isTableSyncAbsorptionSuccess && isMemberSyncAbsorptionSuccess && isColumnSyncAbsorptionSuccess;
      } catch (SDXException sdxException) {
         throw new AssetSynchronizationException(AssetOperationExceptionCodes.FETCHING_ASSET_OPERATION_DATA_FAILED,
                  sdxException);
      } catch (KDXException e) {
         throw new AssetSynchronizationException(AssetOperationExceptionCodes.FETCHING_ASSET_OPERATION_DATA_FAILED, e);
      }
   }

   private void preProcessing (AssetSyncInfo assetSyncInfo) {
      List<AssetOperationTable> lookupTablesToBeDeleted = new ArrayList<AssetOperationTable>();
      List<ColumnSyncInfo> columnSyncInfoToBeRemoved = new ArrayList<ColumnSyncInfo>();
      List<ColumnSyncInfo> columnSyncInfo = assetSyncInfo.getColumnSyncInfo();
      if (ExecueCoreUtil.isCollectionNotEmpty(columnSyncInfo)) {
         for (ColumnSyncInfo columnSyncInfoReference : columnSyncInfo) {
            boolean isLookupValueColumToBeDeleted = false;
            List<AssetOperationColumn> deletedColumns = columnSyncInfoReference.getDeletedColumns();
            if (ExecueCoreUtil.isCollectionNotEmpty(deletedColumns)) {
               for (AssetOperationColumn colum : deletedColumns) {
                  if (!ColumnType.NULL.equals(colum.getKdxDataType())) {
                     isLookupValueColumToBeDeleted = true;
                     break;
                  }
               }
               if (isLookupValueColumToBeDeleted) {
                  lookupTablesToBeDeleted.add(columnSyncInfoReference.getTabl());
                  columnSyncInfoToBeRemoved.add(columnSyncInfoReference);
               }
            }
         }
      }
      if (ExecueCoreUtil.isCollectionNotEmpty(columnSyncInfoToBeRemoved)) {
         assetSyncInfo.getColumnSyncInfo().removeAll(columnSyncInfoToBeRemoved);
      }
      if (ExecueCoreUtil.isCollectionNotEmpty(lookupTablesToBeDeleted)) {
         TableSyncInfo tableSyncInfo = assetSyncInfo.getTableSyncInfo();
         if (tableSyncInfo == null) {
            tableSyncInfo = new TableSyncInfo();
         }
         List<AssetOperationTable> deletedTables = tableSyncInfo.getDeletedTables();
         if (ExecueCoreUtil.isCollectionEmpty(deletedTables)) {
            deletedTables = new ArrayList<AssetOperationTable>();
         }
         deletedTables.addAll(lookupTablesToBeDeleted);
         tableSyncInfo.setDeletedTables(deletedTables);
         assetSyncInfo.setTableSyncInfo(tableSyncInfo);
      }
   }

   private boolean absorbTableSyncInfo (Asset asset, TableSyncInfo tableSyncInfo) throws AssetSynchronizationException {
      boolean isAbsorptionSuccess = true;
      try {
         if (tableSyncInfo != null) {
            List<AssetOperationTable> deletedTables = tableSyncInfo.getDeletedTables();
            List<TableUpdationInfo> updatedTables = tableSyncInfo.getUpdatedTables();
            if (ExecueCoreUtil.isCollectionNotEmpty(deletedTables)) {
               for (AssetOperationTable tabl : deletedTables) {
                  log.debug("Deleted Table : " + tabl.getName());
               }
               getSdxDeletionWrapperService().deleteAssetTables(asset, transformAssetOperationTables(deletedTables));
            }
            if (ExecueCoreUtil.isCollectionNotEmpty(updatedTables)) {
               List<Tabl> swiUpdatedTables = new ArrayList<Tabl>();
               for (TableUpdationInfo tableUpdationInfo : updatedTables) {
                  AssetOperationTable swiTable = tableUpdationInfo.getSwiTable();
                  AssetOperationTable sourceTable = tableUpdationInfo.getSourceTable();
                  // need to load the actual table from swi because of some other changes happened in between
                  Tabl tableFromSwi = getSdxRetrievalService().getAssetTable(asset.getId(), swiTable.getName());
                  if (tableUpdationInfo.isDescriptionChanged()) {
                     tableFromSwi.setDescription(sourceTable.getDescription());
                     swiUpdatedTables.add(tableFromSwi);
                  }
               }
               getSdxManagementService().updateTables(swiUpdatedTables);
            }
         }
      } catch (SDXException sdxException) {
         isAbsorptionSuccess = false;
         throw new AssetSynchronizationException(AssetOperationExceptionCodes.TABLE_SYNC_DATA_ABSORPTION_FAILED,
                  sdxException);
      }
      return isAbsorptionSuccess;
   }

   private boolean absorbColumnSyncInfo (Asset asset, Model model, List<ColumnSyncInfo> columnSyncInfo)
            throws AssetSynchronizationException {
      boolean isAbsorptionSuccess = true;
      try {
         if (ExecueCoreUtil.isCollectionNotEmpty(columnSyncInfo)) {
            for (ColumnSyncInfo columnSyncInfoReference : columnSyncInfo) {
               AssetOperationTable tabl = columnSyncInfoReference.getTabl();
               List<AssetOperationColumn> deletedColumns = columnSyncInfoReference.getDeletedColumns();
               List<AssetOperationColumn> addedColumns = columnSyncInfoReference.getAddedColumns();
               List<ColumnUpdationInfo> updatedColumns = columnSyncInfoReference.getUpdatedColumns();
               Tabl transformedTable = transformAssetOperationTable(tabl);
               if (ExecueCoreUtil.isCollectionNotEmpty(deletedColumns)) {
                  getSdxDeletionWrapperService().deleteAssetTableColumns(asset, transformedTable,
                           transformAssetOperationColumns(deletedColumns));
               }
               if (ExecueCoreUtil.isCollectionNotEmpty(addedColumns)) {
                  List<Colum> columnsToBeAdded = transformAssetOperationColumns(addedColumns);
                  getSdxManagementService().createColumns(asset, transformedTable, columnsToBeAdded);
                  getSdx2kdxMappingService().mapColumnsForAssetSyncUpProcess(asset, transformedTable, columnsToBeAdded,
                           model);
               }
               if (ExecueCoreUtil.isCollectionNotEmpty(updatedColumns)) {
                  List<Colum> swiColumnsUpdated = new ArrayList<Colum>();
                  for (ColumnUpdationInfo columnUpdationInfo : updatedColumns) {
                     AssetOperationColumn swiColumn = columnUpdationInfo.getSwiColumn();
                     AssetOperationColumn sourceColumn = columnUpdationInfo.getSourceColumn();
                     Colum columnToBeUpdated = getSdxRetrievalService().getAssetTableColum(asset.getId(),
                              transformedTable.getName(), swiColumn.getName());
                     if (columnUpdationInfo.isConstraintInfoUpdated()) {
                        // TODO : -VG- evaluate the constraints
                     }
                     if (columnUpdationInfo.isDataTypeUpdated()) {
                        columnToBeUpdated.setDataType(sourceColumn.getDataType());
                        // TODO : -VG- need to evaluate further
                     }
                     if (columnUpdationInfo.isDefaultValueUpdated()) {
                        columnToBeUpdated.setDefaultValue(sourceColumn.getDefaultValue());
                     }
                     if (columnUpdationInfo.isDescriptionUpdated()) {
                        columnToBeUpdated.setDescription(sourceColumn.getDescription());
                     }
                     if (columnUpdationInfo.isPrecisionUpdated()) {
                        columnToBeUpdated.setPrecision(sourceColumn.getPrecision());
                     }
                     if (columnUpdationInfo.isRequiredUpdated()) {
                        columnToBeUpdated.setRequired(sourceColumn.getRequired());
                     }
                     if (columnUpdationInfo.isScaleUpdated()) {
                        columnToBeUpdated.setScale(sourceColumn.getScale());
                     }
                     swiColumnsUpdated.add(columnToBeUpdated);
                  }
                  getSdxManagementService().updateColumns(asset.getId(), transformedTable.getId(), swiColumnsUpdated);
               }
            }
         }
      } catch (SDXException sdxException) {
         isAbsorptionSuccess = false;
         throw new AssetSynchronizationException(AssetOperationExceptionCodes.TABLE_SYNC_DATA_ABSORPTION_FAILED,
                  sdxException);
      } catch (SDX2KDXMappingException e) {
         isAbsorptionSuccess = false;
         throw new AssetSynchronizationException(AssetOperationExceptionCodes.TABLE_SYNC_DATA_ABSORPTION_FAILED, e);
      }
      return isAbsorptionSuccess;
   }

   private boolean absorbMemberSyncInfo (Asset asset, Model model, List<MemberSyncInfo> memberSyncInfo)
            throws AssetSynchronizationException {
      boolean isAbsorptionSuccess = true;
      try {
         if (ExecueCoreUtil.isCollectionNotEmpty(memberSyncInfo)) {
            for (MemberSyncInfo memberSyncInfoReference : memberSyncInfo) {
               AssetOperationTable tabl = memberSyncInfoReference.getTabl();
               AssetOperationColumn colum = memberSyncInfoReference.getColum();
               List<AssetOperationMember> deletedMembers = memberSyncInfoReference.getDeletedMembers();
               List<AssetOperationMember> addedMembers = memberSyncInfoReference.getAddedMembers();
               List<MemberUpdationInfo> updatedMembers = memberSyncInfoReference.getUpdatedMembers();
               // down the line in method create members, we need fully populated table
               Tabl swiTable = getSdxRetrievalService().getTableById(tabl.getId());
               Colum swiColumn = getSdxRetrievalService().getColumnById(colum.getId());
               if (ExecueCoreUtil.isCollectionNotEmpty(deletedMembers)) {
                  getSdxDeletionService().deleteMembers(tranformAssetOperationMembers(deletedMembers));
               }
               if (ExecueCoreUtil.isCollectionNotEmpty(addedMembers)) {
                  List<Colum> columns = new ArrayList<Colum>();
                  columns.add(swiColumn);
                  List<Membr> membersToBeAdded = tranformAssetOperationMembers(addedMembers);
                  getSdxManagementService().createMembers(asset, swiTable, columns, membersToBeAdded);
                  AssetEntityDefinition assetEntityDefinition = getSdxRetrievalService().getAssetEntityDefinitionByIds(
                           asset, swiTable, swiColumn, null);
                  List<Mapping> mappingsForAED = getMappingRetrievalService().getMappingsForAED(
                           assetEntityDefinition.getId());
                  if (ExecueCoreUtil.isCollectionNotEmpty(mappingsForAED)) {
                     BusinessEntityDefinition conceptBED = mappingsForAED.get(0).getBusinessEntityDefinition();
                     getSdx2kdxMappingService().mapMembersForAssetSyncUpProcess(asset, swiTable, swiColumn,
                              membersToBeAdded, conceptBED, model);
                  }
               }
               if (ExecueCoreUtil.isCollectionNotEmpty(updatedMembers)) {
                  List<Membr> swiMembersUpdated = new ArrayList<Membr>();
                  for (MemberUpdationInfo memberUpdationInfo : updatedMembers) {
                     AssetOperationMember swiMember = memberUpdationInfo.getSwiMember();
                     AssetOperationMember sourceMember = memberUpdationInfo.getSourceMember();
                     Membr member = getSdxRetrievalService().getMemberById(swiMember.getId());
                     if (memberUpdationInfo.isDescriptionChanged()) {
                        member.setLookupDescription(sourceMember.getLookupDescription());
                        member.setKdxLookupDescription(sourceMember.getKdxLookupDescription());
                        member.setOriginalDescription(sourceMember.getLookupDescription());
                     }
                     swiMembersUpdated.add(member);
                  }
                  getSdxManagementService().updateMembers(swiMembersUpdated);
               }
            }
         }
      } catch (SDXException sdxException) {
         isAbsorptionSuccess = false;
         throw new AssetSynchronizationException(AssetOperationExceptionCodes.TABLE_SYNC_DATA_ABSORPTION_FAILED,
                  sdxException);
      } catch (SDX2KDXMappingException e) {
         isAbsorptionSuccess = false;
         throw new AssetSynchronizationException(AssetOperationExceptionCodes.TABLE_SYNC_DATA_ABSORPTION_FAILED, e);
      } catch (MappingException e) {
         isAbsorptionSuccess = false;
         throw new AssetSynchronizationException(AssetOperationExceptionCodes.TABLE_SYNC_DATA_ABSORPTION_FAILED, e);
      }
      return isAbsorptionSuccess;
   }

   public ISDX2KDXMappingService getSdx2kdxMappingService () {
      return sdx2kdxMappingService;
   }

   public void setSdx2kdxMappingService (ISDX2KDXMappingService sdx2kdxMappingService) {
      this.sdx2kdxMappingService = sdx2kdxMappingService;
   }

   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }

   public ISDXRetrievalService getSdxRetrievalService () {
      return sdxRetrievalService;
   }

   public void setSdxRetrievalService (ISDXRetrievalService sdxRetrievalService) {
      this.sdxRetrievalService = sdxRetrievalService;
   }

   public ISDXManagementService getSdxManagementService () {
      return sdxManagementService;
   }

   public void setSdxManagementService (ISDXManagementService sdxManagementService) {
      this.sdxManagementService = sdxManagementService;
   }

   public IMappingRetrievalService getMappingRetrievalService () {
      return mappingRetrievalService;
   }

   public void setMappingRetrievalService (IMappingRetrievalService mappingRetrievalService) {
      this.mappingRetrievalService = mappingRetrievalService;
   }

   public ISDXDeletionService getSdxDeletionService () {
      return sdxDeletionService;
   }

   public void setSdxDeletionService (ISDXDeletionService sdxDeletionService) {
      this.sdxDeletionService = sdxDeletionService;
   }

   public IBusinessModelPreparationService getBusinessModelPreparationService () {
      return businessModelPreparationService;
   }

   public void setBusinessModelPreparationService (IBusinessModelPreparationService businessModelPreparationService) {
      this.businessModelPreparationService = businessModelPreparationService;
   }

   
   public ISDXDeletionWrapperService getSdxDeletionWrapperService () {
      return sdxDeletionWrapperService;
   }

   
   public void setSdxDeletionWrapperService (ISDXDeletionWrapperService sdxDeletionWrapperService) {
      this.sdxDeletionWrapperService = sdxDeletionWrapperService;
   }

}
