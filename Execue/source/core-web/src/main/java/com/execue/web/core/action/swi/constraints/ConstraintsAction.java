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


package com.execue.web.core.action.swi.constraints;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.type.ConstraintType;
import com.execue.core.exception.ExeCueException;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.handler.bean.UIColumn;
import com.execue.handler.bean.UIConstraint;
import com.execue.handler.bean.UITable;
import com.execue.handler.bean.UITableConstraintsInfo;
import com.execue.web.core.action.swi.ConstraintStatus;
import com.execue.web.core.action.swi.SWIAction;

public class ConstraintsAction extends SWIAction {

   private static final long      serialVersionUID = 1L;

   private static final Logger    log              = Logger.getLogger(ConstraintsAction.class);

   private Asset                  asset;
   private List<UITable>          tables;
   private List<UITable>          orphanTables;
   private List<UITable>          primaryKeyTables;
   private List<UITable>          foriegnKeyTables;
   private UITableConstraintsInfo tableConstraintsInfo;
   private List<Long>             primaryKeyColumnIds;
   private List<Long>             primaryKeyIds;
   private List<Long>             foreignKeyIds;
   private ConstraintStatus       constraintStatus;
   private Long                   tableId;
   private List<UIColumn>         refTableColums;
   private List<Long>             fKeyColumns;
   private List<Long>             fKeyrefTable;
   private List<String>           fkeyName;
   private List<Long>             refColumns;
   private Long                   fkeyConstraintId;
   private List<Long>             fkeyConstraintColumnIds;
   private String                 sourceName       = "constraints";
   private List<Asset>            assets;

   // Action Methods

   public String input () {
      try {
         // TODO:- JT- Tried to get assetId from app context, will revisit and test it.
         assets = getSdxServiceHandler().getAllAssets(getApplicationContext().getAppId());
         if (getApplicationContext().getAssetId() != null) {
            asset = getConstraintServiceHandler().getAsset(getApplicationContext().getAssetId());
         }
      } catch (ExeCueException exeCueException) {
         log.error(exeCueException, exeCueException);
         return ERROR;
      }
      return SUCCESS;
   }

   public String showTables () {
      try {
         tables = getConstraintServiceHandler().getUITablesForAsset(asset.getId());
         orphanTables = getConstraintServiceHandler().getUIOrphanTablesForAsset(asset.getId());
         primaryKeyTables = getConstraintServiceHandler().getUIPrimaryKeyTablesForAsset(asset.getId());

      } catch (ExeCueException exeCueException) {
         log.error(exeCueException, exeCueException);
         return ERROR;
      }
      return SUCCESS;
   }

   public String showForeignKeyTables () {
      try {
         foriegnKeyTables = getForeignKeyTables(getConstraintServiceHandler().getUIPrimaryKeyTablesForAsset(
                  asset.getId()), getConstraintServiceHandler().getTableById(tableId).getName());

      } catch (ExeCueException exeCueException) {
         log.error(exeCueException, exeCueException);
         return ERROR;
      }
      return SUCCESS;
   }

   public String showConstraints () {
      try {
         String tableName = (getConstraintServiceHandler().getTableById(tableConstraintsInfo.getTable().getId()))
                  .getName();
         tables = getConstraintServiceHandler().getUITablesForAsset(asset.getId());
         foriegnKeyTables = getForeignKeyTables(getConstraintServiceHandler().getUIPrimaryKeyTablesForAsset(
                  asset.getId()), tableName);
         tableConstraintsInfo = getConstraintServiceHandler().getTableInfoById(asset.getId(),
                  tableConstraintsInfo.getTable().getId());
      } catch (ExeCueException exeCueException) {
         log.error(exeCueException, exeCueException);
         return ERROR;
      }
      return SUCCESS;
   }

   public String showReferenceTableColumns () {

      try {
         refTableColums = getConstraintServiceHandler().getUICoumnsForTable(tableId);
      } catch (ExeCueException exeCueException) {
         log.error(exeCueException, exeCueException);
         return ERROR;
      }
      return SUCCESS;
   }

   public String updateConstraints () {
      populateDefaultStatus();
      // TODO: -RG- Implements the action
      return SUCCESS;
   }

   public String deleteConstraint () {
      try {
         UIConstraint constraint = new UIConstraint();
         constraint.setType(ConstraintType.FOREIGN_KEY);
         constraint.setConstraintId(fkeyConstraintId);
         List<Colum> constraintColums = new ArrayList<Colum>();
         for (Long columId : fkeyConstraintColumnIds) {
            constraintColums.add(getConstraintServiceHandler().getColumnById(columId));
         }
         constraint.setConstraintColums(constraintColums);
         getConstraintServiceHandler().deleteForeignKeyConstraint(constraint);
      } catch (ExeCueException exeCueException) {
         log.error(exeCueException, exeCueException);
         return ERROR;
      }
      return SUCCESS;
   }

   public String saveConstraint () {
      UITableConstraintsInfo uiTableConstraintsInfo = new UITableConstraintsInfo();
      try {
         Long tableId = tableConstraintsInfo.getTable().getId();
         uiTableConstraintsInfo.setTable(getConstraintServiceHandler().getTableById(tableId));
         uiTableConstraintsInfo.setColumns(getConstraintServiceHandler().getColumnsOfTable(tableId));
         UIConstraint primarykeyConstraint = new UIConstraint();
         List<UIConstraint> foreignKeyConstraints = new ArrayList<UIConstraint>();
         primarykeyConstraint.setName(tableConstraintsInfo.getPkConstraint().getName());
         if (primaryKeyIds != null)
            for (int count = 0; count < primaryKeyIds.size(); count++) {
               primarykeyConstraint.setConstraintId(primaryKeyIds.get(count));
            }
         if (primaryKeyColumnIds == null)
            primaryKeyColumnIds = new ArrayList<Long>();
         List<Colum> constraintColumns = new ArrayList<Colum>();
         for (int count = 0; count < primaryKeyColumnIds.size(); count++) {
            constraintColumns.add(getConstraintServiceHandler().getColumnById(primaryKeyColumnIds.get(count)));
         }
         primarykeyConstraint.setConstraintColums(constraintColumns);
         primarykeyConstraint.setType(ConstraintType.PRIMARY_KEY);
         tableConstraintsInfo.setPkConstraint(primarykeyConstraint);
         if (ExecueCoreUtil.isCollectionNotEmpty(fKeyColumns)) {
            for (int fkeyCount = 0; fkeyCount < fKeyColumns.size(); fkeyCount++) {
               UIConstraint fkeyConstraint = new UIConstraint();
               if (ExecueCoreUtil.isCollectionNotEmpty(foreignKeyIds) && foreignKeyIds.size() > fkeyCount) {
                  List<Long> fkeyIds = new ArrayList<Long>();
                  fkeyIds.add(foreignKeyIds.get(fkeyCount));
                  // fkeyConstraint.setId(fkeyIds);
               }
               fkeyConstraint.setType(ConstraintType.FOREIGN_KEY);
               fkeyConstraint.setName(fkeyName.get(fkeyCount));
               fkeyConstraint
                        .setReferenceTable(getConstraintServiceHandler().getTableById(fKeyrefTable.get(fkeyCount)));
               List<Colum> fkeyConstraintColumns = new ArrayList<Colum>();
               fkeyConstraintColumns.add(getConstraintServiceHandler().getColumnById(fKeyColumns.get(fkeyCount)));
               fkeyConstraint.setConstraintColums(fkeyConstraintColumns);
               List<Colum> fkeyRefColumns = new ArrayList<Colum>();
               fkeyRefColumns.add(getConstraintServiceHandler().getColumnById(refColumns.get(fkeyCount)));
               fkeyConstraint.setReferenceColumns(fkeyRefColumns);
               foreignKeyConstraints.add(fkeyConstraint);
            }
            tableConstraintsInfo.setFkConstraints(foreignKeyConstraints);
         }
         getConstraintServiceHandler().saveConstraint(tableConstraintsInfo);
         // addActionMessage("success");
      } catch (ExeCueException exeCueException) {
         log.error(exeCueException, exeCueException);
         // addActionMessage(getText("failure"));
         return ERROR;
      }

      return SUCCESS;
   }

   // End of Action Methods

   // Private methods

   private void populateDefaultStatus () {
      constraintStatus = new ConstraintStatus();
      constraintStatus.setStatus(SUCCESS);
      constraintStatus.setMessage(getText("execue.constraints.update.success"));
   }

   private List<UITable> getForeignKeyTables (List<UITable> primarykeyTables, String tableName) {
      List<UITable> foriegnKeyTables = new ArrayList<UITable>();
      for (UITable uiTable : primarykeyTables) {
         if (!uiTable.getName().equals(tableName))
            foriegnKeyTables.add(uiTable);
      }
      return foriegnKeyTables;
   }

   // End of Private methods

   // Mutators
   public Asset getAsset () {
      return asset;
   }

   public void setAsset (Asset asset) {
      this.asset = asset;
   }

   public ConstraintStatus getConstraintStatus () {
      return constraintStatus;
   }

   public void setConstraintStatus (ConstraintStatus constraintStatus) {
      this.constraintStatus = constraintStatus;
   }

   public List<UITable> getTables () {
      return tables;
   }

   public void setTables (List<UITable> tables) {
      this.tables = tables;
   }

   public UITableConstraintsInfo getTableConstraintsInfo () {
      return tableConstraintsInfo;
   }

   public void setTableConstraintsInfo (UITableConstraintsInfo tableConstraintsInfo) {
      this.tableConstraintsInfo = tableConstraintsInfo;
   }

   public List<Long> getPrimaryKeyColumnIds () {
      return primaryKeyColumnIds;
   }

   public void setPrimaryKeyColumnIds (List<Long> primaryKeyColumnIds) {
      this.primaryKeyColumnIds = primaryKeyColumnIds;
   }

   public Long getTableId () {
      return tableId;
   }

   public void setTableId (Long tableId) {
      this.tableId = tableId;
   }

   public List<UIColumn> getRefTableColums () {
      return refTableColums;
   }

   public void setRefTableColums (List<UIColumn> refTableColums) {
      this.refTableColums = refTableColums;
   }

   public List<UITable> getOrphanTables () {
      return orphanTables;
   }

   public void setOrphanTables (List<UITable> orphanTables) {
      this.orphanTables = orphanTables;
   }

   public List<Long> getFKeyColumns () {
      return fKeyColumns;
   }

   public void setFKeyColumns (List<Long> keyColumns) {
      fKeyColumns = keyColumns;
   }

   public List<Long> getFKeyrefTable () {
      return fKeyrefTable;
   }

   public void setFKeyrefTable (List<Long> keyrefTable) {
      fKeyrefTable = keyrefTable;
   }

   public List<Long> getRefColumns () {
      return refColumns;
   }

   public void setRefColumns (List<Long> refColumns) {
      this.refColumns = refColumns;
   }

   public List<String> getFkeyName () {
      return fkeyName;
   }

   public void setFkeyName (List<String> fkeyName) {
      this.fkeyName = fkeyName;
   }

   public List<UITable> getPrimaryKeyTables () {
      return primaryKeyTables;
   }

   public void setPrimaryKeyTables (List<UITable> primaryKeyTables) {
      this.primaryKeyTables = primaryKeyTables;
   }

   public List<Long> getPrimaryKeyIds () {
      return primaryKeyIds;
   }

   public void setPrimaryKeyIds (List<Long> primaryKeyIds) {
      this.primaryKeyIds = primaryKeyIds;
   }

   public List<Long> getForeignKeyIds () {
      return foreignKeyIds;
   }

   public void setForeignKeyIds (List<Long> foreignKeyIds) {
      this.foreignKeyIds = foreignKeyIds;
   }

   public List<Long> getFkeyConstraintColumnIds () {
      return fkeyConstraintColumnIds;
   }

   public void setFkeyConstraintColumnIds (List<Long> fkeyConstraintColumnIds) {
      this.fkeyConstraintColumnIds = fkeyConstraintColumnIds;
   }

   public List<UITable> getForiegnKeyTables () {
      return foriegnKeyTables;
   }

   public void setForiegnKeyTables (List<UITable> foriegnKeyTables) {
      this.foriegnKeyTables = foriegnKeyTables;
   }

   public Long getFkeyConstraintId () {
      return fkeyConstraintId;
   }

   public void setFkeyConstraintId (Long fkeyConstraintId) {
      this.fkeyConstraintId = fkeyConstraintId;
   }

   /**
    * @return the sourceName
    */
   public String getSourceName () {
      return sourceName;
   }

   /**
    * @param sourceName
    *           the sourceName to set
    */
   public void setSourceName (String sourceName) {
      this.sourceName = sourceName;
   }

   /**
    * @return the assets
    */
   public List<Asset> getAssets () {
      return assets;
   }

   /**
    * @param assets
    *           the assets to set
    */
   public void setAssets (List<Asset> assets) {
      this.assets = assets;
   }

}
