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


package com.execue.ac;

import java.util.ArrayList;
import java.util.List;

import com.execue.ac.bean.MartConfigurationContext;
import com.execue.ac.exception.AnswersCatalogException;
import com.execue.core.common.bean.ac.AnswersCatalogMaintenanceRequestInfo;
import com.execue.core.common.bean.ac.MartCreationContext;
import com.execue.core.common.bean.entity.Asset;

public class AnswersCatalogCommonBaseTest extends AnswersCatalogBaseTest {

   public MartCreationContext getDummyMartCreationContextSecfiling () throws AnswersCatalogException {
      Long modelId = 101L;
      Long applicationId = 101L;
      Long sourceAssetId = 1L;
      //      String population = "Company";
      //      List<String> distributions = new ArrayList<String>(1);
      //      distributions.add("NominalYear");

      List<String> prominentDimensions = new ArrayList<String>(1);
      prominentDimensions.add("Sector");
      // prominentDimensions.add("Industry");
      List<String> prominentMeasures = new ArrayList<String>(1);
      prominentMeasures.add("NetSales");
      prominentMeasures.add("NetIncome");
      // prominentMeasures.add("Cash");
      // prominentMeasures.add("AccountsPayables");
      // prominentMeasures.add("CapitalSurplus");
      // prominentMeasures.add("TotalAssets");
      // prominentMeasures.add("GrossProfit");
      // prominentMeasures.add("OtherIncome");
      // prominentMeasures.add("MarketCapital");
      // prominentMeasures.add("GrossMargin");
      // prominentMeasures.add("InventoryTurnover");
      // prominentMeasures.add("AssetTurnover");

      Asset targetAsset = new Asset();
      String martName = "MartTestNew12";
      targetAsset.setName(martName);
      targetAsset.setDescription(martName);
      targetAsset.setDisplayName(martName);

      // sequence of stats.
      //      List<StatType> stats = new ArrayList<StatType>();
      //      stats.add(StatType.STDDEV);
      //      stats.add(StatType.AVERAGE);

      AnswersCatalogMaintenanceRequestInfo answersCatalogMaintenanceRequestInfo = new AnswersCatalogMaintenanceRequestInfo();
      answersCatalogMaintenanceRequestInfo.setApplicationId(applicationId);
      answersCatalogMaintenanceRequestInfo.setModelId(modelId);
      answersCatalogMaintenanceRequestInfo.setUserId(1L);
      answersCatalogMaintenanceRequestInfo.setParentAssetId(sourceAssetId);
      answersCatalogMaintenanceRequestInfo.setTargetAsset(targetAsset);
      answersCatalogMaintenanceRequestInfo.setProminentDimensions(prominentDimensions);
      answersCatalogMaintenanceRequestInfo.setProminentMeasures(prominentMeasures);

      return getAnswersCatalogContextBuilderService().buildMartCreationContext(answersCatalogMaintenanceRequestInfo);
   }

   public MartCreationContext getDummyMartCreationContext () throws AnswersCatalogException {

      Long modelId = 110L;
      Long applicationId = 110L;
      Long sourceAssetId = 11L;

      List<String> prominentDimensions = new ArrayList<String>(1);
      prominentDimensions.add("AccountStatus");
      // prominentDimensions.add("Product");
      List<String> prominentMeasures = new ArrayList<String>(1);
      prominentMeasures.add("MerchandiseBalance");
      prominentMeasures.add("MerchandiseAmount");
      // prominentMeasures.add("Cash");
      // prominentMeasures.add("AccountsPayables");
      // prominentMeasures.add("CapitalSurplus");
      // prominentMeasures.add("TotalAssets");
      // prominentMeasures.add("GrossProfit");
      // prominentMeasures.add("OtherIncome");
      // prominentMeasures.add("MarketCapital");
      // prominentMeasures.add("GrossMargin");
      // prominentMeasures.add("InventoryTurnover");
      // prominentMeasures.add("AssetTurnover");

      Asset targetAsset = new Asset();
      String martName = "DFS";
      targetAsset.setName(martName);
      targetAsset.setDescription(martName);
      targetAsset.setDisplayName(martName);

      // sequence of stats.
      //      List<StatType> stats = new ArrayList<StatType>();
      //      stats.add(StatType.STDDEV);
      //      stats.add(StatType.AVERAGE);

      AnswersCatalogMaintenanceRequestInfo answersCatalogMaintenanceRequestInfo = new AnswersCatalogMaintenanceRequestInfo();
      answersCatalogMaintenanceRequestInfo.setApplicationId(applicationId);
      answersCatalogMaintenanceRequestInfo.setModelId(modelId);
      answersCatalogMaintenanceRequestInfo.setUserId(1L);
      answersCatalogMaintenanceRequestInfo.setParentAssetId(sourceAssetId);
      answersCatalogMaintenanceRequestInfo.setTargetAsset(targetAsset);
      answersCatalogMaintenanceRequestInfo.setProminentDimensions(prominentDimensions);
      answersCatalogMaintenanceRequestInfo.setProminentMeasures(prominentMeasures);

      return getAnswersCatalogContextBuilderService().buildMartCreationContext(answersCatalogMaintenanceRequestInfo);
   }

   public void printMartConfigurationContext (MartConfigurationContext martConfigurationContext) {
      System.out.println(martConfigurationContext.getFractionalPopulationTableName());
      System.out.println(martConfigurationContext.getFractionalTableNotation());
   }

}
