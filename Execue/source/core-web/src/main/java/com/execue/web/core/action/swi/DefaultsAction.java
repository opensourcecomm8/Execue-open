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


package com.execue.web.core.action.swi;

import java.util.ArrayList;
import java.util.List;

import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.DefaultMetric;
import com.execue.core.exception.ExeCueException;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.handler.bean.UITable;

public class DefaultsAction extends SWIAction {

   private List<Asset>         assets;
   private List<UITable>       tables;
   private List<DefaultMetric> eligibleDefaultMetrics       = new ArrayList<DefaultMetric>();
   private List<DefaultMetric> validExistingDefaultMetrics  = new ArrayList<DefaultMetric>();
   private String              invalidDefaultMetricsMessage = null;
   private UITable             currentTable;
   private String              sourceName                   = "defaultMetrics";
   private Asset               asset;

   public String showAssets () {
      try {
         assets = getSdxServiceHandler().getAllAssets(getApplicationContext().getAppId());
      } catch (ExeCueException e) {
         return ERROR;
      }
      return SUCCESS;
   }

   public String showTables () {
      try {
         tables = getSdxServiceHandler().getAllAssetFactTables(asset);
      } catch (ExeCueException e) {
         return ERROR;
      }
      return SUCCESS;
   }

   public String showDefaultMetrics () {
      try {
         eligibleDefaultMetrics = getMappingServiceHandler().getAllPossibleDefaultMetrics(asset.getId(),
                  currentTable.getId());

         List<DefaultMetric> invalidExistingDefaultMetrics = getMappingServiceHandler()
                  .getInValidExistingDefaultMetrics(currentTable.getId());
         List<String> columnNames = new ArrayList<String>();
         if (ExecueCoreUtil.isCollectionNotEmpty(invalidExistingDefaultMetrics)) {
            for (DefaultMetric defaultMetric : invalidExistingDefaultMetrics) {
               columnNames.add(defaultMetric.getColumnName());
            }
            String columnNamesList = ExecueCoreUtil.joinCollection(columnNames);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("There are invalid default metrics on this table for columns : [");
            stringBuilder.append(columnNamesList);
            stringBuilder.append(" ]. You need to map them to make them valid");
            invalidDefaultMetricsMessage = stringBuilder.toString();
         }

         validExistingDefaultMetrics = getMappingServiceHandler().getValidExistingDefaultMetrics(currentTable.getId());

         if (ExecueCoreUtil.isCollectionNotEmpty(validExistingDefaultMetrics)) {
            for (DefaultMetric existingMetric : validExistingDefaultMetrics) {
               DefaultMetric matchedElement = getMatchingElement(existingMetric);
               if (matchedElement != null) {
                  eligibleDefaultMetrics.remove(matchedElement);
               }
            }
         }

      } catch (ExeCueException e) {
         return ERROR;
      }
      return SUCCESS;
   }

   private DefaultMetric getMatchingElement (DefaultMetric existingMetric) {
      DefaultMetric matchedElement = null;
      for (DefaultMetric matric : eligibleDefaultMetrics) {
         if (matric.getMappingId().equals(existingMetric.getMappingId())) {
            matchedElement = matric;
            break;
         }
      }
      return matchedElement;
   }

   public String saveUpdateDefaultMetrics () {
      try {
         getSdxServiceHandler().updateTableForSystemDefaultMetric(currentTable.getId(),
                  currentTable.getEligibleSystemDefaultMetric());
         getSdxServiceHandler().saveUpdateDefaultMetrics(currentTable.getId(), validExistingDefaultMetrics);
         showDefaultMetrics();
         addActionMessage(getText("execue.defaultMetrics.update.success"));
      } catch (ExeCueException e) {
         showDefaultMetrics();
         addActionError(getText("execue.defaultMetrics.update.failed"));
      }
      return SUCCESS;
   }

   public List<Asset> getAssets () {
      return assets;
   }

   public void setAssets (List<Asset> assets) {
      this.assets = assets;
   }

   public List<UITable> getTables () {
      return tables;
   }

   public void setTables (List<UITable> tables) {
      this.tables = tables;
   }

   public List<DefaultMetric> getEligibleDefaultMetrics () {
      return eligibleDefaultMetrics;
   }

   public void setEligibleDefaultMetrics (List<DefaultMetric> eligibleDefaultMetrics) {
      this.eligibleDefaultMetrics = eligibleDefaultMetrics;
   }

   public List<DefaultMetric> getExistingDefaultMetrics () {
      return validExistingDefaultMetrics;
   }

   public void setExistingDefaultMetrics (List<DefaultMetric> existingDefaultMetrics) {
      this.validExistingDefaultMetrics = existingDefaultMetrics;
   }

   public UITable getCurrentTable () {
      return currentTable;
   }

   public void setCurrentTable (UITable currentTable) {
      this.currentTable = currentTable;
   }

   public Asset getAsset () {
      return asset;
   }

   public void setAsset (Asset asset) {
      this.asset = asset;
   }

   public String getSourceName () {
      return sourceName;
   }

   public void setSourceName (String sourceName) {
      this.sourceName = sourceName;
   }

   public String getInvalidDefaultMetricsMessage () {
      return invalidDefaultMetricsMessage;
   }

   public void setInvalidDefaultMetricsMessage (String invalidDefaultMetricsMessage) {
      this.invalidDefaultMetricsMessage = invalidDefaultMetricsMessage;
   }

}
