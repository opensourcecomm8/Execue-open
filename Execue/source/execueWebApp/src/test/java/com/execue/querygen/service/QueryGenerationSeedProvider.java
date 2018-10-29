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


package com.execue.querygen.service;

import java.util.ArrayList;
import java.util.List;

import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.querygen.FromEntity;
import com.execue.core.common.bean.querygen.Query;
import com.execue.core.common.bean.querygen.QueryTable;
import com.execue.core.common.type.FromEntityType;

public class QueryGenerationSeedProvider {

   public static Query getTestQuery(List<QueryTable> fromTables) {
      Query query = new Query();
      query.setFromEntities(getFromEntities(fromTables));
      return query;
   }
   
   public static Asset getTargetAsset(Long assetId) {
      Asset asset = new Asset();
      asset.setId(assetId);
      return asset;
   }
   
   public static List<FromEntity> getFromEntities (List<QueryTable> tables) {
      List<FromEntity> fromEntities = new ArrayList<FromEntity>();
      for (QueryTable table : tables) {
         fromEntities.add(getFromEntityForTable(table));
      }
      return fromEntities;
   }
   
   public static FromEntity getFromEntityForTable (QueryTable table) {
      FromEntity fromEntity = new FromEntity();
      fromEntity.setTable(getQueryTable(table.getTableName(), table.getAlias()));
      fromEntity.setType(FromEntityType.TABLE);
      return fromEntity;
   }
   
   public static QueryTable getQueryTable (String tableName, String tableAlias) {
      QueryTable queryTable = new QueryTable();
      queryTable.setTableName(tableName);
      queryTable.setAlias(tableAlias);
      return queryTable;
   }
}
