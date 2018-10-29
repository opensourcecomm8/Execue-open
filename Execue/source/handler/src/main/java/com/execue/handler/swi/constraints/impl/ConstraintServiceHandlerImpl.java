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


package com.execue.handler.swi.constraints.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.Constraint;
import com.execue.core.common.bean.entity.ConstraintInfo;
import com.execue.core.common.bean.entity.Membr;
import com.execue.core.common.bean.entity.Tabl;
import com.execue.core.common.bean.entity.wrapper.TableInfo;
import com.execue.core.common.type.ConstraintType;
import com.execue.core.exception.ExeCueException;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.core.exception.UidException;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.handler.bean.UIColumn;
import com.execue.handler.bean.UIConstraint;
import com.execue.handler.bean.UIMember;
import com.execue.handler.bean.UITable;
import com.execue.handler.bean.UITableConstraintsInfo;
import com.execue.handler.swi.constraints.IConstraintServiceHandler;
import com.execue.platform.IUidService;
import com.execue.swi.exception.SDXException;
import com.execue.swi.service.IJoinService;
import com.execue.swi.service.ISDXDeletionService;
import com.execue.swi.service.ISDXManagementService;
import com.execue.swi.service.ISDXRetrievalService;

public class ConstraintServiceHandlerImpl implements IConstraintServiceHandler {

   private ISDXRetrievalService  sdxRetrievalService;
   private ISDXManagementService sdxManagementService;
   private ISDXDeletionService   sdxDeletionService;
   private IJoinService          joinService;
   private IUidService           transactionIdGenerationService;

   // Service methods
   public Asset getAsset (Long assetId) throws ExeCueException {
      return getSdxRetrievalService().getAsset(assetId);
   }

   public UITableConstraintsInfo getTableInfoById (Long assetId, Long tableId) throws ExeCueException {
      UITableConstraintsInfo uiTableConstraintInfo = new UITableConstraintsInfo();
      Asset asset = getAsset(assetId);
      Tabl table = getSdxRetrievalService().getTableById(tableId);
      TableInfo tableInfo = getSdxRetrievalService().getAssetTable(asset, table);
      uiTableConstraintInfo.setTable(tableInfo.getTable());
      uiTableConstraintInfo.setColumns(tableInfo.getColumns());
      ConstraintInfo constraintInfo = getSdxRetrievalService().getPrimaryKeyConstraint(tableId);
      List<ConstraintInfo> refConstraints = getSdxRetrievalService().getPrimaryKeyReferenceConstraints(table);
      List<ConstraintInfo> fkeyConstraints = getSdxRetrievalService().getForiegnKeyConstraint(tableId);
      if (constraintInfo != null || ExecueCoreUtil.isCollectionNotEmpty(refConstraints)
               || ExecueCoreUtil.isCollectionNotEmpty(fkeyConstraints)) {
         if (constraintInfo != null) {
            uiTableConstraintInfo.setPkConstraint(transFormToUIConstraint(constraintInfo));
         }
         if (ExecueCoreUtil.isCollectionNotEmpty(fkeyConstraints)) {
            List<UIConstraint> fkeyUIConstraints = new ArrayList<UIConstraint>();
            for (ConstraintInfo fkeyConstraint : fkeyConstraints) {
               fkeyUIConstraints.add(transFormToUIConstraint(fkeyConstraint));
            }
            uiTableConstraintInfo.setFkConstraints(fkeyUIConstraints);
         }
         if (ExecueCoreUtil.isCollectionNotEmpty(refConstraints)) {
            List<UIConstraint> refUIConstraints = new ArrayList<UIConstraint>();
            for (ConstraintInfo refConstraint : refConstraints) {
               refUIConstraints.add(transFormToUIConstraint(refConstraint));
            }
            uiTableConstraintInfo.setPkReferences(refUIConstraints);
         }
      }
      return uiTableConstraintInfo;
   }

   public Colum getColumnById (Long columnId) throws ExeCueException {
      return getSdxRetrievalService().getColumnById(columnId);
   }

   public Tabl getTableById (Long tableId) throws ExeCueException {
      return getSdxRetrievalService().getTableById(tableId);
   }

   public List<Colum> getColumnsOfTable (Long tableId) throws ExeCueException {
      return getSdxRetrievalService().getColumnsOfTable(getTableById(tableId));
   }

   public List<UITable> getUITablesForAsset (Long assetId) throws ExeCueException {
      List<TableInfo> assetTableInfos = getSdxRetrievalService().getAssetTables(getAsset(assetId));
      return transformTableInfoToUITables(assetTableInfos);
   }

   public List<UITable> getUIOrphanTablesForAsset (Long assetId) throws ExeCueException {
      List<Tabl> tables = getSdxRetrievalService().getOrphanTables(assetId);
      return transformToUITables(tables);
   }

   public List<UITable> getUIPrimaryKeyTablesForAsset (Long assetId) throws ExeCueException {
      List<Tabl> tables = getSdxRetrievalService().getPrimaryKeyTables(assetId);
      return transformToUITables(tables);
   }

   public void updateConstraintInfo (TableInfo tableInfo) throws ExeCueException {
      // TODO -RG- Implement the method

   }

   public List<UIColumn> getUICoumnsForTable (Long tableId) throws ExeCueException {
      Tabl table = getSdxRetrievalService().getTableById(tableId);
      List<Colum> columns = getSdxRetrievalService().getColumnsOfTable(table);
      return transformToUIColumns(columns);
   }

   // Private methods
   private List<UITable> transformTableInfoToUITables (List<TableInfo> assetTableInfos) {
      List<UITable> uiTables = new ArrayList<UITable>();
      UITable tempUITable = null;
      for (TableInfo tableInfo : assetTableInfos) {
         tempUITable = new UITable();
         tempUITable.setId(tableInfo.getTable().getId());
         tempUITable.setName(tableInfo.getTable().getName());
         tempUITable.setVirtual(tableInfo.getTable().getVirtual().getValue());
         tempUITable.setDescription(tableInfo.getTable().getDescription());
         tempUITable.setDisplayName(tableInfo.getTable().getDisplayName());
         tempUITable.setEligibleSystemDefaultMetric(tableInfo.getTable().getEligibleDefaultMetric());
         // TODO: -RG- Populate if constraint defined and is PK defined
         /*
          * tempUITable.setConstraintDefined(); tempUITable.setPkDefined();
          */
         uiTables.add(tempUITable);
      }
      return uiTables;
   }

   private List<UIColumn> transformToUIColumns (List<Colum> columns) throws ExeCueException {
      List<UIColumn> uiColumns = new ArrayList<UIColumn>();
      UIColumn uiColumn = null;
      for (Colum column : columns) {
         uiColumn = new UIColumn();
         uiColumn.setColumnType(column.getKdxDataType());
         uiColumn.setDataType(column.getDataType());
         uiColumn.setDescription(column.getDescription());
         uiColumn.setId(column.getId());
         List<Membr> members = getSdxRetrievalService().getColumnMembers(column);
         uiColumn.setMembers(transformToUIMembers(members));
         uiColumn.setName(column.getName());
         uiColumns.add(uiColumn);
      }
      return uiColumns;
   }

   private List<UITable> transformToUITables (List<Tabl> tables) {
      List<UITable> uiTables = new ArrayList<UITable>();
      UITable uiTable = null;
      for (Tabl tabl : tables) {
         uiTable = new UITable();
         uiTable.setId(tabl.getId());
         uiTable.setName(tabl.getName());
         uiTable.setLookupColumnName(tabl.getLookupValueColumn());
         uiTable.setOwner(tabl.getOwner());
         uiTable.setDescription(tabl.getDescription());
         uiTable.setDisplayName(tabl.getDisplayName());
         uiTable.setEligibleSystemDefaultMetric(tabl.getEligibleDefaultMetric());
         uiTables.add(uiTable);
      }
      return uiTables;
   }

   private List<UIMember> transformToUIMembers (List<Membr> members) {
      List<UIMember> uiMembers = new ArrayList<UIMember>();
      UIMember uiMember = null;
      for (Membr member : members) {
         uiMember = new UIMember();
         uiMember.setDescription(member.getLookupDescription());
         uiMember.setId(member.getId());
         uiMember.setName(member.getLookupValue());
         uiMembers.add(uiMember);
      }
      return uiMembers;
   }

   private UIConstraint transFormToUIConstraint (ConstraintInfo constraintInfo) {
      UIConstraint uiConstraint = new UIConstraint();
      uiConstraint.setConstraintColums(constraintInfo.getConstraintColums());
      uiConstraint.setConstraintId(constraintInfo.getConstraintId());
      uiConstraint.setName(constraintInfo.getName());
      uiConstraint.setType(constraintInfo.getType());
      if (ConstraintType.FOREIGN_KEY.equals(uiConstraint.getType())) {
         uiConstraint.setReferenceColumns(constraintInfo.getReferenceColumns());
         uiConstraint.setReferenceTable(constraintInfo.getReferenceTable());
      }
      return uiConstraint;
   }

   private List<Constraint> transformToConstraint (UIConstraint uiConstraint) {
      List<Constraint> constraints = new ArrayList<Constraint>();
      for (int count = 0; count < uiConstraint.getConstraintColums().size(); count++) {
         Constraint constraint = new Constraint();
         Set<Colum> constraintColums = new HashSet<Colum>();
         constraint.setConstraintId(uiConstraint.getConstraintId());
         constraint.setType(uiConstraint.getType());
         if (ConstraintType.FOREIGN_KEY.equals(uiConstraint.getType())) {
            constraint.setReferenceTable(uiConstraint.getReferenceTable());
            constraint.setReferenceColumn(uiConstraint.getReferenceColumns().get(count));
         }
         constraintColums.add(uiConstraint.getConstraintColums().get(count));
         constraint.setConstraintColums(constraintColums);
         constraint.setName(uiConstraint.getName());
         constraint.setDescription(uiConstraint.getName());
         constraints.add(constraint);
      }
      return constraints;
   }

   private List<Constraint> transformToConstraintForDelete (UIConstraint uiConstraint) {
      List<Constraint> constraints = new ArrayList<Constraint>();
      for (int count = 0; count < uiConstraint.getConstraintColums().size(); count++) {
         Constraint constraint = new Constraint();
         Set<Colum> constraintColums = new HashSet<Colum>();
         constraint.setConstraintId(uiConstraint.getConstraintId());
         constraint.setType(uiConstraint.getType());
         constraintColums.add(uiConstraint.getConstraintColums().get(count));
         constraint.setConstraintColums(constraintColums);
         constraints.add(constraint);
      }
      return constraints;
   }

   public void saveConstraint (UITableConstraintsInfo tableConstraintsInfo) throws ExeCueException {
      // print the information existing ones and newly added ones.make sure ids is not a issue.
      UIConstraint pkConstraint = tableConstraintsInfo.getPkConstraint();
      if (pkConstraint != null) {
         if (pkConstraint.getConstraintId() == null) {
            createConstraint(pkConstraint);
         } else {
            // update or delete
            if (ExecueCoreUtil.isCollectionEmpty(pkConstraint.getConstraintColums())) {
               List<Constraint> constraints = getSdxRetrievalService().getConstraintsByConstraintID(
                        pkConstraint.getConstraintId());
               getSdxDeletionService().deleteConstraints(constraints);
            } else {
               List<Constraint> constraints = getSdxRetrievalService().getConstraintsByConstraintID(
                        pkConstraint.getConstraintId());
               boolean isChanged = isPrimaryKeyConstraintUpdated(constraints, pkConstraint);
               if (isChanged) {
                  getSdxDeletionService().deleteConstraints(constraints);
                  createConstraint(pkConstraint);
               }
            }
         }
      }
      List<UIConstraint> fkConstraints = tableConstraintsInfo.getFkConstraints();
      if (ExecueCoreUtil.isCollectionNotEmpty(fkConstraints)) {
         for (UIConstraint fkConstraint : fkConstraints) {
            if (fkConstraint.getConstraintId() == null) {
               if (ExecueCoreUtil.isCollectionNotEmpty(fkConstraint.getConstraintColums())) {
                  for (Colum colum : fkConstraint.getConstraintColums()) {
                     try {
                        fkConstraint.setConstraintId(getTransactionIdGenerationService().getNextId());
                     } catch (UidException e) {
                        throw new ExeCueException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
                     }
                  }
                  getSdxManagementService().createConstraints(transformToConstraint(fkConstraint));
               }
            } else {
               // getSdxService().updateConstraints(transformToConstraint(fkConstraint));
            }
         }
      }
   }

   private void createConstraint (UIConstraint pkConstraint) throws ExeCueException {
      if (ExecueCoreUtil.isCollectionNotEmpty(pkConstraint.getConstraintColums())) {
         for (Colum colum : pkConstraint.getConstraintColums()) {
            try {
               pkConstraint.setConstraintId(getTransactionIdGenerationService().getNextId());
            } catch (UidException e) {
               throw new ExeCueException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
            }
         }
         try {
            getSdxManagementService().createConstraints(transformToConstraint(pkConstraint));
         } catch (SDXException e) {
            throw new ExeCueException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
         }
      }
   }

   private boolean isPrimaryKeyConstraintUpdated (List<Constraint> constraints, UIConstraint pkConstraint) {
      boolean isChanged = false;
      if (!(constraints.size() == pkConstraint.getConstraintColums().size())) {
         isChanged = true;
      } else {
         List<String> constraintNames = new ArrayList<String>();
         for (Constraint constraint : constraints) {
            constraintNames.add(constraint.getName());
         }
         for (Colum colum : pkConstraint.getConstraintColums()) {
            if (!constraintNames.contains(colum.getName())) {
               isChanged = true;
               break;
            }
         }
      }
      return isChanged;
   }

   public void deleteForeignKeyConstraint (UIConstraint fkConstraint) throws ExeCueException {
      List<Constraint> fkeyConstraints = getSdxRetrievalService().getConstraintsByConstraintID(
               fkConstraint.getConstraintId());
      getSdxDeletionService().deleteConstraints(fkeyConstraints);
   }

   public IJoinService getJoinService () {
      return joinService;
   }

   public void setJoinService (IJoinService joinService) {
      this.joinService = joinService;
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

   public ISDXDeletionService getSdxDeletionService () {
      return sdxDeletionService;
   }

   public void setSdxDeletionService (ISDXDeletionService sdxDeletionService) {
      this.sdxDeletionService = sdxDeletionService;
   }

   public IUidService getTransactionIdGenerationService () {
      return transactionIdGenerationService;
   }

   public void setTransactionIdGenerationService (IUidService transactionIdGenerationService) {
      this.transactionIdGenerationService = transactionIdGenerationService;
   }

}
