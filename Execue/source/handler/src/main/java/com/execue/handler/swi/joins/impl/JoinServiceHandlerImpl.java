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


package com.execue.handler.swi.joins.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.Join;
import com.execue.core.common.bean.entity.JoinDefinition;
import com.execue.core.common.bean.entity.Tabl;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.handler.bean.UIColumnForJoins;
import com.execue.handler.bean.UIJoinDefintionInfo;
import com.execue.handler.bean.UIJoinInfo;
import com.execue.handler.bean.UITableForJoins;
import com.execue.handler.swi.joins.IJoinServiceHandler;
import com.execue.handler.util.SWIBeanTransformer;
import com.execue.swi.exception.JoinException;
import com.execue.swi.exception.SDXException;
import com.execue.swi.exception.SWIException;
import com.execue.swi.service.IJoinService;
import com.execue.swi.service.ISDXRetrievalService;

public class JoinServiceHandlerImpl implements IJoinServiceHandler {

   private IJoinService         joinService;
   private ISDXRetrievalService sdxRetrievalService;

   // get joins for the Asset
   public List<UIJoinInfo> getAssetJoins (long assetId) throws JoinException {
      List<Join> joins = getJoinService().getDirectAssetJoins(assetId);
      try {
         Map<String, String> tableNameDisplayNameMap = new HashMap<String, String>();
         if (ExecueCoreUtil.isCollectionNotEmpty(joins)) {
            tableNameDisplayNameMap = populateTableNameDisplayNameMap(assetId, joins);
         }
         return SWIBeanTransformer.getUIJoinInfo(tableNameDisplayNameMap, joins);
      } catch (SDXException e) {
         throw new JoinException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }

   }

   private Map<String, String> populateTableNameDisplayNameMap (Long assetId, List<Join> joins) throws SDXException {
      Map<String, String> tableNameDisplayNameMap = new HashMap<String, String>();
      Set<String> uniqueTableNames = new HashSet<String>();
      for (Join join : joins) {
         uniqueTableNames.add(join.getSourceTableName());
         uniqueTableNames.add(join.getDestTableName());
      }
      List<String> uniqueTableNamesList = new ArrayList<String>(uniqueTableNames);
      List<Tabl> tables = getSdxRetrievalService().getAssetTables(assetId, uniqueTableNamesList);
      for (Tabl tabl : tables) {
         tableNameDisplayNameMap.put(tabl.getName(), tabl.getDisplayName());
      }
      return tableNameDisplayNameMap;
   }

   // get existing joins between two tables
   public List<UIJoinDefintionInfo> getExistingJoinDefinitions (Long assetId, String sourceTableName,
            String destinationTableName) throws JoinException {

      List<UIJoinDefintionInfo> uiJoinDefinitions = SWIBeanTransformer.getUIJoinDefinitions(getJoinService()
               .getExistingDirectJoinDefinitions(assetId, sourceTableName, destinationTableName));
      for (UIJoinDefintionInfo joinDefintionInfo : uiJoinDefinitions) {
         joinDefintionInfo.setCheckedState("checked");
         joinDefintionInfo.setSuggestedJoin(false);
      }
      return uiJoinDefinitions;
   }

   // get suggested between two tables
   public List<UIJoinDefintionInfo> getSuggestedJoinDefinitions (Long assetId, String sourceTableName,
            String destinationTableName) throws JoinException {
      List<UIJoinDefintionInfo> uiJoinDefinitions = SWIBeanTransformer.getUIJoinDefinitions(getJoinService()
               .getSuggestedJoinDefinitions(assetId, sourceTableName, destinationTableName));
      for (UIJoinDefintionInfo joinDefintionInfo : uiJoinDefinitions) {
         joinDefintionInfo.setCheckedState("");
         joinDefintionInfo.setSuggestedJoin(true);
      }
      return uiJoinDefinitions;
   }

   // get tables for the Asset
   public List<UITableForJoins> getAssetTables (long assetId) throws JoinException {
      try {
         Asset asset = getSdxRetrievalService().getAssetById(assetId);
         return SWIBeanTransformer.getUITablesForJoins(getSdxRetrievalService().getAllTables(asset));
      } catch (SWIException e) {
         // TODO : -JT- has to handle exception
         throw new JoinException(10, e);
      }
   }

   // get columns for the table
   public List<UIColumnForJoins> getColForJoinTables (long assetId, String tableName) throws JoinException {
      Tabl table = null;
      List<UIColumnForJoins> uiColums = new ArrayList<UIColumnForJoins>();
      try {
         table = getSdxRetrievalService().getAssetTable(assetId, tableName);
         if (table != null) {
            uiColums = SWIBeanTransformer.getUIColumnsForColums(getSdxRetrievalService().getColumnsOfTable(table));
         }
         return uiColums;
      } catch (SWIException e) {
         throw new JoinException(10, e);
         // TODO : -JT- has to handle exception
      }
   }

   // Persist joins
   public void persistJoins (Long assetId, String sourceTableName, String destTableName,
            List<UIJoinDefintionInfo> uiJoinDefintions) throws JoinException {
      List<Join> persistJoins = new ArrayList<Join>();
      Asset asset = new Asset();
      asset.setId(assetId);
      // prepare list for checked records
      List<UIJoinDefintionInfo> chekedUIjoinDefinitions = new ArrayList<UIJoinDefintionInfo>();
      for (UIJoinDefintionInfo joinDefintionInfo : uiJoinDefintions) {
         if (joinDefintionInfo.getCheckedState() != null && joinDefintionInfo.getCheckedState().equalsIgnoreCase("yes")) {
            chekedUIjoinDefinitions.add(joinDefintionInfo);
         }
      }
      uiJoinDefintions.retainAll(chekedUIjoinDefinitions);

      List<JoinDefinition> joinDefinitions = SWIBeanTransformer.getJoinDefinitions(uiJoinDefintions);
      for (JoinDefinition joinDefinition : joinDefinitions) {
         joinDefinition.setAsset(asset);
         joinDefinition.setSourceTableName(sourceTableName);
         joinDefinition.setDestTableName(destTableName);
      }
      Join joinInfo = new Join();
      joinInfo.setAsset(asset);
      joinInfo.setSourceTableName(sourceTableName);
      joinInfo.setDestTableName(destTableName);
      joinInfo.setJoinDefinitions(new HashSet<JoinDefinition>(joinDefinitions));
      persistJoins.add(joinInfo);
      getJoinService().createJoins(assetId, persistJoins);
   }

   // Delete joins
   public void deleteJoins (Long assetId, List<UIJoinInfo> deleteJoins) throws JoinException {
      Asset asset = new Asset();
      asset.setId(assetId);
      // prepare list for checked records
      List<UIJoinInfo> checkedDelJoins = new ArrayList<UIJoinInfo>();
      for (UIJoinInfo joinInfo : deleteJoins) {
         if (joinInfo.getCheckedStatus() != null && joinInfo.getCheckedStatus().equalsIgnoreCase("yes")) {
            checkedDelJoins.add(joinInfo);
         }
      }
      deleteJoins.retainAll(checkedDelJoins);
      List<Join> joinsToDelete = SWIBeanTransformer.getJoins(deleteJoins);
      for (Join join : joinsToDelete) {
         join.setAsset(asset);
      }
      getJoinService().deleteJoins(assetId, joinsToDelete);
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

}
