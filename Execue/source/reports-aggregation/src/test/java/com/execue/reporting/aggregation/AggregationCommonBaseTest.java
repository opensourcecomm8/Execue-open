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


package com.execue.reporting.aggregation;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.governor.AssetQuery;
import com.execue.core.common.bean.governor.BusinessAssetTerm;
import com.execue.core.common.bean.governor.StructuredCondition;
import com.execue.core.common.bean.governor.StructuredOrderClause;
import com.execue.core.common.bean.governor.StructuredQuery;
import com.execue.core.common.bean.querygen.ConditionEntity;
import com.execue.core.common.bean.querygen.FromEntity;
import com.execue.core.common.bean.querygen.JoinEntity;
import com.execue.core.common.bean.querygen.LimitEntity;
import com.execue.core.common.bean.querygen.OrderEntity;
import com.execue.core.common.bean.querygen.Query;
import com.execue.core.common.bean.querygen.SelectEntity;
import com.execue.reporting.aggregation.test.ReportAggregationTestData;
import com.execue.repoting.aggregation.bean.ReportColumnInfo;
import com.execue.repoting.aggregation.bean.ReportMetaInfo;
import com.execue.repoting.aggregation.bean.ReportSelection;
import com.execue.repoting.aggregation.configuration.IAggregationConfigurationService;
import com.thoughtworks.xstream.XStream;

public class AggregationCommonBaseTest extends AggregationBaseTest {

   public AssetQuery getAssetQuery () {
      AssetQuery assetQuery = new AssetQuery();
      assetQuery.setLogicalQuery(ReportAggregationTestData.generateLogicalQuery());
      assetQuery.setPhysicalQuery(ReportAggregationTestData.generatePhysicalQuery());
      return assetQuery;
   }

   public ReportMetaInfo getReportMetaInfo (AssetQuery assetQuery) {
      ReportMetaInfo reportMetaInfo = new ReportMetaInfo(assetQuery);
      List<ReportColumnInfo> reportColumns = new ArrayList<ReportColumnInfo>();
      List<BusinessAssetTerm> businessAssetTerms = assetQuery.getLogicalQuery().getMetrics();
      for (BusinessAssetTerm businessAssetTerm : businessAssetTerms) {
         reportColumns.add(new ReportColumnInfo(businessAssetTerm));
      }
      reportMetaInfo.setReportColumns(reportColumns);
      return reportMetaInfo;
   }

   public void populateReportSelection (ReportSelection reportSelection) {
      IAggregationConfigurationService aggregationConfigurationService = (IAggregationConfigurationService) aggregationContext
               .getBean("aggregationConfigurationService");

      reportSelection.setDataPoints(aggregationConfigurationService.getDataPoints());
      reportSelection.setNumberOfEffectiveGroups(aggregationConfigurationService.getNumberOfEffectiveGroups());
      reportSelection.setNumberOfEffectiveRecords(aggregationConfigurationService.getNumberOfEffectiveRecords());
      reportSelection.setNumberOfGroups(aggregationConfigurationService.getNumberOfGroups());
      reportSelection.setNumberOfIdColumns(aggregationConfigurationService.getNumberOfIdColumns());
      reportSelection.setNumberOfMeasures(aggregationConfigurationService.getNumberOfMeasures());
      reportSelection.setNumberOfRecords(aggregationConfigurationService.getNumberOfRecords());
   }

   public static void generateAssetQueryAsXML () {
      AssetQuery assetQuery = new AssetQuery();
      StructuredQuery structuredQuery = mockStructuredQuery();
      Query query = mockQuery();
      assetQuery.setLogicalQuery(structuredQuery);
      assetQuery.setPhysicalQuery(query);
      XStream xs = new XStream();
      // xs.alias("assetQuery", com.execue.core.common.bean.governor.AssetQuery.class);
      String xmlString = xs.toXML(assetQuery);
      System.out.println(xmlString);
   }

   public static StructuredQuery mockStructuredQuery () {
      StructuredQuery structuredQuery = new StructuredQuery();

      Asset asset = new Asset();
      asset.setName("ASSET_SALES");

      List<BusinessAssetTerm> metrics = new ArrayList<BusinessAssetTerm>();

      List<StructuredCondition> conditions = new ArrayList<StructuredCondition>();
      List<BusinessAssetTerm> summarizations = new ArrayList<BusinessAssetTerm>();
      List<StructuredOrderClause> orderClauses = new ArrayList<StructuredOrderClause>();
      List<StructuredCondition> havingClauses = new ArrayList<StructuredCondition>();
      Double structuredQueryWeight = new Double(20);

      structuredQuery.setAsset(asset);
      structuredQuery.setMetrics(metrics);
      structuredQuery.setConditions(conditions);
      structuredQuery.setSummarizations(summarizations);
      structuredQuery.setOrderClauses(orderClauses);
      structuredQuery.setHavingClauses(havingClauses);
      structuredQuery.setStructuredQueryWeight(structuredQueryWeight);

      return structuredQuery;
   }

   public static Query mockQuery () {
      Query query = new Query();
      List<SelectEntity> selectEntities = new ArrayList<SelectEntity>();
      List<FromEntity> fromEntities = new ArrayList<FromEntity>();
      List<JoinEntity> joinEntities = new ArrayList<JoinEntity>();
      List<ConditionEntity> whereEntities = new ArrayList<ConditionEntity>();
      List<SelectEntity> groupingEntities = new ArrayList<SelectEntity>();
      List<OrderEntity> orderingEntities = new ArrayList<OrderEntity>();
      List<ConditionEntity> havingEntities = new ArrayList<ConditionEntity>();
      LimitEntity limitingCondition = new LimitEntity();
      String alias = "q";

      query.setAlias(alias);
      query.setFromEntities(fromEntities);
      query.setGroupingEntities(groupingEntities);
      query.setHavingEntities(havingEntities);
      query.setJoinEntities(joinEntities);
      query.setLimitingCondition(limitingCondition);
      query.setOrderingEntities(orderingEntities);
      query.setSelectEntities(selectEntities);
      query.setWhereEntities(whereEntities);

      return query;
   }

   public AssetQuery getAssetQueryFromXML (String name) {
      AssetQuery assetQuery = null;
      try {
         InputStream inputStream = this.getClass().getResourceAsStream(name);
         XStream xstream = new XStream();
         assetQuery = (AssetQuery) xstream.fromXML(inputStream);
      } catch (Exception e) {
         e.printStackTrace();
      }
      return assetQuery;
   }

   public static void main (String[] args) {
      generateAssetQueryAsXML();
   }
}
