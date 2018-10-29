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


package com.execue.das;

import java.util.ArrayList;
import java.util.List;

import com.execue.core.common.bean.querygen.SQLIndex;
import com.execue.das.solr.bean.SolrConditionEntity;
import com.execue.das.solr.bean.SolrDistanceConditionEntity;
import com.execue.das.solr.bean.SolrFacetFieldEntity;
import com.execue.das.solr.bean.SolrFacetQueryEntity;
import com.execue.das.solr.bean.SolrFacetQueryInput;
import com.execue.das.solr.bean.SolrLocationInfo;
import com.execue.das.solr.bean.SolrRangeEntity;
import com.execue.das.solr.type.SolrFieldType;
import com.execue.das.solr.type.SolrOperatorType;

public class DataAccessServicesCommonBaseTest extends DataAccessServicesBaseTest {

   protected List<SolrFacetQueryEntity> buildSolrFacetQueryEntities () {
      List<SolrFacetQueryEntity> queryEntities = new ArrayList<SolrFacetQueryEntity>();
      SolrFacetQueryEntity queryEntity = new SolrFacetQueryEntity();
      queryEntity.setFacetField("price_d");
      queryEntity.addRangeEntity(buildRangeEntity(null, "200", true, "low"));
      queryEntity.addRangeEntity(buildRangeEntity("200", "400", true, "mid"));
      queryEntity.addRangeEntity(buildRangeEntity("400", null, true, "high"));

      SolrFacetQueryEntity queryEntity1 = new SolrFacetQueryEntity();
      queryEntity1.setFacetField("ctx_l");
      queryEntity1.addRangeEntity(buildRangeEntity(null, "200", false, "low"));
      queryEntity1.addRangeEntity(buildRangeEntity("200", "400", false, "mid"));
      queryEntity1.addRangeEntity(buildRangeEntity("400", null, false, "high"));

      queryEntities.add(queryEntity);
      queryEntities.add(queryEntity1);
      return queryEntities;
   }

   protected List<SolrFacetFieldEntity> buildSolrFacetFieldEntities () {
      List<SolrFacetFieldEntity> fieldEntities = new ArrayList<SolrFacetFieldEntity>();
      fieldEntities.add(new SolrFacetFieldEntity("name_s"));
      return fieldEntities;
   }

   protected SolrDistanceConditionEntity buildDistanceCondition () {
      SolrDistanceConditionEntity distanceConditionEntity = new SolrDistanceConditionEntity();
      distanceConditionEntity.setDistance(100d);
      distanceConditionEntity.setSolrLongitudefieldName("long_d");
      distanceConditionEntity.setSolrLatitudefieldName("lat_d");
      distanceConditionEntity.addSolrLocationInfo(new SolrLocationInfo(20d, 10d));
      distanceConditionEntity.addSolrLocationInfo(new SolrLocationInfo(40d, 20d));
      return distanceConditionEntity;
   }

   protected List<SolrConditionEntity> buildConditionEntities () {
      List<SolrConditionEntity> conditionEntities = new ArrayList<SolrConditionEntity>();
      conditionEntities.add(buildNameConditionEntity());
      // conditionEntities.add(buildUpperRangeConditionEntity());
      SolrConditionEntity solrConditionEntity = new SolrConditionEntity();
      solrConditionEntity.addSubCondition(buildUpperRangeConditionEntity());
      solrConditionEntity.addSubCondition(buildLowerRangeConditionEntity());
      solrConditionEntity.setCompositeCondition(true);
      conditionEntities.add(solrConditionEntity);
      return conditionEntities;
   }

   protected SolrConditionEntity buildNameConditionEntity () {
      SolrConditionEntity solrConditionEntity = new SolrConditionEntity();
      solrConditionEntity.setFacetField("name_s");
      solrConditionEntity.setFieldType(SolrFieldType.STRING);
      solrConditionEntity.setOperatorType(SolrOperatorType.IN);
      solrConditionEntity.addRhsValue("vishay");
      // solrConditionEntity.addRhsValue("vishay1");
      return solrConditionEntity;
   }

   protected SolrConditionEntity buildUpperRangeConditionEntity () {
      SolrConditionEntity solrConditionEntity = new SolrConditionEntity();
      solrConditionEntity.setFacetField("price_d");
      solrConditionEntity.setFieldType(SolrFieldType.NUMBER);
      solrConditionEntity.setOperatorType(SolrOperatorType.BETWEEN);
      solrConditionEntity.setRange(buildRangeEntity(null, "400", false, null));
      return solrConditionEntity;
   }

   protected SolrRangeEntity buildRangeEntity (String lowerBound, String upperBound, boolean inclusive, String name) {
      SolrRangeEntity rangeEntity = new SolrRangeEntity();
      rangeEntity.setLowerBound(lowerBound);
      rangeEntity.setUpperBound(upperBound);
      rangeEntity.setInclusive(inclusive);
      rangeEntity.setRangeName(name);
      return rangeEntity;
   }

   protected SolrConditionEntity buildLowerRangeConditionEntity () {
      SolrConditionEntity solrConditionEntity = new SolrConditionEntity();
      solrConditionEntity.setFacetField("price_d");
      solrConditionEntity.setFieldType(SolrFieldType.NUMBER);
      solrConditionEntity.setOperatorType(SolrOperatorType.BETWEEN);
      solrConditionEntity.setRange(buildRangeEntity("500", null, true, null));
      return solrConditionEntity;
   }

   protected SolrFacetQueryInput buildFacetQueryInput () {
      SolrFacetQueryInput queryInput = new SolrFacetQueryInput();
      // queryInput.setDistanceCondition(buildDistanceCondition());
      queryInput.setQueryConditions(buildConditionEntities());
      // queryInput.setFilterQueryConditions(buildConditionEntities());
      queryInput.setFacetQueries(buildSolrFacetQueryEntities());
      queryInput.setFacetFields(buildSolrFacetFieldEntities());
      return queryInput;
   }

   protected List<SQLIndex> buildIndexesForMultipleIndexesViaSingleDDL () {
      List<SQLIndex> indexes = new ArrayList<SQLIndex>();
      List<String> tempColumnNames = null;
      
      SQLIndex index = new SQLIndex();
      index.setTableName("sl_currency_code_fx");
      tempColumnNames = new ArrayList<String>();
      tempColumnNames.add("CURRENCY_CODE");
      index.setColumnNames(tempColumnNames);
      indexes.add(index);
      
      index = new SQLIndex();
      index.setTableName("sl_currency_code_fx");
      tempColumnNames = new ArrayList<String>();
      tempColumnNames.add("CURRENCY_CODE_DESC");
      index.setColumnNames(tempColumnNames);
      indexes.add(index);
      
      index = new SQLIndex();
      index.setTableName("sl_currency_code_fx");
      tempColumnNames = new ArrayList<String>();
      tempColumnNames.add("CURRENCY_CODE");
      tempColumnNames.add("CURRENCY_CODE_DESC");
      index.setColumnNames(tempColumnNames);
      indexes.add(index);
      
      index = new SQLIndex();
      index.setTableName("sl_currency_code_fx");
      tempColumnNames = new ArrayList<String>();
      tempColumnNames.add("CURRENCY_CODE_FROM_SOURCE");
      index.setColumnNames(tempColumnNames);
      indexes.add(index);
      
      return indexes;
   }
   
   protected List<SQLIndex> buildIndexesForMultipleIndexesViaSingleDDLOracle () {
      List<SQLIndex> indexes = new ArrayList<SQLIndex>();
      List<String> tempColumnNames = null;
      
      SQLIndex index = new SQLIndex();
      index.setTableName("TEST_TABLE");
      tempColumnNames = new ArrayList<String>();
      tempColumnNames.add("NME");
      index.setColumnNames(tempColumnNames);
      indexes.add(index);
      
      index = new SQLIndex();
      index.setTableName("TEST_TABLE");
      tempColumnNames = new ArrayList<String>();
      tempColumnNames.add("ID");
      index.setColumnNames(tempColumnNames);
      indexes.add(index);
      
      index = new SQLIndex();
      index.setTableName("TEST_TABLE");
      tempColumnNames = new ArrayList<String>();
      tempColumnNames.add("ID");
      tempColumnNames.add("NME");
      index.setColumnNames(tempColumnNames);
      indexes.add(index);
      
      return indexes;
   }
}
