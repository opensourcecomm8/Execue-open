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


package com.execue.web.core.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.Instance;
import com.execue.core.common.bean.entity.Membr;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.common.bean.entity.wrapper.TableInfo;
import com.execue.core.exception.ExeCueException;
import com.execue.handler.bean.UITable;
import com.execue.swi.exception.KDXException;
import com.execue.web.core.action.swi.SWIAction;

public class AssetMetaInfoAction extends SWIAction {

   private Long                         assetId;
   private String                       assetName;
   private static final Logger          log               = Logger.getLogger(AssetMetaInfoAction.class);

   private Map<Concept, List<Instance>> conceptInstaceMap = new HashMap<Concept, List<Instance>>();
   private Map<UITable, TableInfo>      tableInfoMap      = new HashMap<UITable, TableInfo>();

   public String showAssetMetaInfo () {
      try {
         assetName = getSdxServiceHandler().getAsset(assetId).getName();
         setAssetName(assetName);
         log.debug("assetName ::" + getAssetName());
         Long applicationId = getSdxServiceHandler().getAsset(assetId).getApplication().getId();
         List<Model> models = getKdxServiceHandler().getModelsByApplicationId(applicationId);

         List<Concept> concepts = getKdxServiceHandler().getConceptsForAssetMetaInfo(assetId);
         for (Concept concept : concepts) {

            List<Instance> instances = getKdxServiceHandler().getInstancesForAssetMetaInfo(models.get(0).getId(),
                     concept.getId());
            conceptInstaceMap.put(concept, instances);
         }
         Asset asset = getSdxServiceHandler().getAsset(assetId);
         List<UITable> tables = getSdxServiceHandler().getAllAssetTables(asset);
         for (UITable table : tables) {
            TableInfo tableInfo = getSdxServiceHandler().getAssetTable(asset, table.getName());
            if (tableInfo.getMembers() != null) {
               if (tableInfo.getMembers().size() > 20) {
                  tableInfo.setMembers(tableInfo.getMembers().subList(0, 20));
               }
            } else {
               tableInfo.setMembers(new ArrayList<Membr>());
            }
            tableInfoMap.put(table, tableInfo);
         }
      } catch (KDXException kdxException) {
         log.error(kdxException, kdxException);
         kdxException.printStackTrace();
         return ERROR;
      } catch (ExeCueException exeCueException) {
         log.error(exeCueException, exeCueException);
         exeCueException.printStackTrace();
         return ERROR;
      }
      return SUCCESS;
   }

   public Long getAssetId () {
      return assetId;
   }

   public void setAssetId (Long assetId) {
      this.assetId = assetId;
   }

   public String getAssetName () {
      return assetName;
   }

   public void setAssetName (String assetName) {
      this.assetName = assetName;
   }

   public Map<Concept, List<Instance>> getConceptInstaceMap () {
      return conceptInstaceMap;
   }

   public void setConceptInstaceMap (Map<Concept, List<Instance>> conceptInstaceMap) {
      this.conceptInstaceMap = conceptInstaceMap;
   }

   public Map<UITable, TableInfo> getTableInfoMap () {
      return tableInfoMap;
   }

   public void setTableInfoMap (Map<UITable, TableInfo> tableInfoMap) {
      this.tableInfoMap = tableInfoMap;
   }

}
