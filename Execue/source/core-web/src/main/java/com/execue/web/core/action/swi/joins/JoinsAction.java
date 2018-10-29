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


package com.execue.web.core.action.swi.joins;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.entity.Asset;
import com.execue.core.exception.ExeCueException;
import com.execue.handler.bean.UIColumnForJoins;
import com.execue.handler.bean.UIJoinDefintionInfo;
import com.execue.handler.bean.UIJoinInfo;
import com.execue.handler.bean.UITableForJoins;
import com.execue.swi.exception.JoinException;
import com.execue.web.core.action.swi.SWIAction;

public class JoinsAction extends SWIAction {

   private static final Logger       log        = Logger.getLogger(JoinsAction.class);
   private Long                      assetId;
   private Long                      tableId;
   private String                    tableName;
   private String                    sourceTableName;
   private String                    destTableName;

   private List<UIJoinDefintionInfo> uiJoinDefInfo;
   private List<UITableForJoins>     uiTableForJoins;
   private List<UIJoinInfo>          uiAssetJoins;
   private List<UIColumnForJoins>    uiColumnForJoins;
   private List<Asset>               assets;
   private String                    sourceName = JOIN;
   private Asset                     asset;

   public String getTableName () {
      return tableName;
   }

   public void setTableName (String tableName) {
      this.tableName = tableName;
   }

   public String getSourceTableName () {
      return sourceTableName;
   }

   public void setSourceTableName (String sourceTableName) {
      this.sourceTableName = sourceTableName;
   }

   public String getDestTableName () {
      return destTableName;
   }

   public void setDestTableName (String destTableName) {
      this.destTableName = destTableName;
   }

   public long getAssetId () {
      return assetId;
   }

   public void setAssetId (long assetId) {
      this.assetId = assetId;
   }

   public long getTableId () {
      return tableId;
   }

   public void setTableId (long tableId) {
      this.tableId = tableId;
   }

   public List<UIJoinDefintionInfo> getUiJoinDefInfo () {
      return uiJoinDefInfo;
   }

   public void setUiJoinDefInfo (List<UIJoinDefintionInfo> uiJoinDefInfo) {
      this.uiJoinDefInfo = uiJoinDefInfo;
   }

   public List<UITableForJoins> getUiTableForJoins () {
      return uiTableForJoins;
   }

   public void setUiTableForJoins (List<UITableForJoins> uiTableForJoins) {
      this.uiTableForJoins = uiTableForJoins;
   }

   public List<UIJoinInfo> getUiAssetJoins () {
      return uiAssetJoins;
   }

   public void setUiAssetJoins (List<UIJoinInfo> uiAssetJoins) {
      this.uiAssetJoins = uiAssetJoins;
   }

   public List<UIColumnForJoins> getUiColumnForJoins () {
      return uiColumnForJoins;
   }

   public void setUiColumnForJoins (List<UIColumnForJoins> uiColumnForJoins) {
      this.uiColumnForJoins = uiColumnForJoins;
   }

   // Action Methods
   public String createJoins () {
      if (log.isDebugEnabled()) {
         log.debug("asset id : " + assetId);
         log.debug("lhs table : " + sourceTableName);
         log.debug("rha table : " + destTableName);
      }
      if (uiJoinDefInfo != null) {
         for (UIJoinDefintionInfo uiJoinDefInfoElement : uiJoinDefInfo) {
            if (log.isDebugEnabled()) {
               log.debug("LHS : " + uiJoinDefInfoElement.getLhsColumn());
               log.debug("RHS : " + uiJoinDefInfoElement.getRhsColumn());
               log.debug("Checked : " + uiJoinDefInfoElement.getCheckedState());
               log.debug("Type : " + uiJoinDefInfoElement.getType());
            }
         }
      }
      try {
         getJoinServiceHandler().persistJoins(assetId, sourceTableName, destTableName, uiJoinDefInfo);
         setMessage(getText("execue.global.insert.success", new String[] { getText("execue.joins.heading") }));
      } catch (JoinException e) {
         setErrorMessage(getText("execue.errors.general"));
         return ERROR;
      }

      return SUCCESS;
   }

   public String getAssetJoins () {
      try {
         uiAssetJoins = getJoinServiceHandler().getAssetJoins(assetId);
      } catch (JoinException e) {
         e.printStackTrace();
      }
      return SUCCESS;
   }

   public String getAssetTables () {
      try {
         // TODO:- JT- Tried to get assetId from app context, will revisit and test it.
         assetId = getApplicationContext().getAssetId();
         assets = getSdxServiceHandler().getAllAssets(getApplicationContext().getAppId());
         if (assetId != null) {
            uiTableForJoins = getJoinServiceHandler().getAssetTables(assetId);
            asset = getSdxServiceHandler().getAsset(assetId);
         }
      } catch (JoinException e) {
         e.printStackTrace();
      } catch (ExeCueException exeCueException) {
         log.error(exeCueException, exeCueException);
      }
      return SUCCESS;
   }

   public String getJoinTableColumn () {
      try {
         uiColumnForJoins = getJoinServiceHandler().getColForJoinTables(assetId, tableName);
      } catch (JoinException e) {
         e.printStackTrace();
      }
      return SUCCESS;
   }

   public String getExistingJoinDefinitions () {
      uiJoinDefInfo = new ArrayList<UIJoinDefintionInfo>();
      try {
         List<UIJoinDefintionInfo> existingJoinDefinitions = getJoinServiceHandler().getExistingJoinDefinitions(
                  assetId, sourceTableName, destTableName);
         List<UIJoinDefintionInfo> suggestedJoinDefinitions = getJoinServiceHandler().getSuggestedJoinDefinitions(
                  assetId, sourceTableName, destTableName);
         if (existingJoinDefinitions.size() > 0) {
            uiJoinDefInfo.addAll(existingJoinDefinitions);
         }
         if (suggestedJoinDefinitions.size() > 0) {
            for (UIJoinDefintionInfo suggestedJoinDefinition : suggestedJoinDefinitions) {
               if (!isJoinDefintionAlreadyExists(suggestedJoinDefinition, uiJoinDefInfo)) {
                  uiJoinDefInfo.add(suggestedJoinDefinition);
               }
            }
         }
      } catch (JoinException e) {
         e.printStackTrace();
      }
      return SUCCESS;
   }

   private boolean isJoinDefintionAlreadyExists (UIJoinDefintionInfo suggestedJoinDefinition,
            List<UIJoinDefintionInfo> uiJoinDefInfo) {
      boolean alreadyExists = false;
      for (UIJoinDefintionInfo joinDefintion : uiJoinDefInfo) {
         if ((joinDefintion.getLhsColumn().equalsIgnoreCase(suggestedJoinDefinition.getLhsColumn()) || joinDefintion
                  .getLhsColumn().equalsIgnoreCase(suggestedJoinDefinition.getRhsColumn()))
                  && (joinDefintion.getRhsColumn().equalsIgnoreCase(suggestedJoinDefinition.getRhsColumn()) || joinDefintion
                           .getRhsColumn().equalsIgnoreCase(suggestedJoinDefinition.getLhsColumn()))) {
            alreadyExists = true;
            break;
         }
      }
      return alreadyExists;
   }

   public String deleteJoins () {
      if (uiAssetJoins != null) {
         for (UIJoinInfo uiAssetJoinsElement : uiAssetJoins) {
            log.debug("LHS Table : " + uiAssetJoinsElement.getLhsTableName());
            log.debug("RHS Table : " + uiAssetJoinsElement.getRhsTableName());
            log.debug("Checked status : " + uiAssetJoinsElement.getCheckedStatus());
         }
      }
      try {
         getJoinServiceHandler().deleteJoins(assetId, uiAssetJoins);
         setMessage(getText("execue.global.delete.success", new String[] { getText("execue.joins.heading") }));
      } catch (JoinException e) {
         setErrorMessage(getText("execue.errors.general"));
         return ERROR;
      }
      return SUCCESS;
   }

   public List<Asset> getAssets () {
      return assets;
   }

   public String getSourceName () {
      return sourceName;
   }

   public void setSourceName (String sourceName) {
      this.sourceName = sourceName;
   }

   public Asset getAsset () {
      return asset;
   }

   public void setAsset (Asset asset) {
      this.asset = asset;
   }

}
