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


package com.execue.core.common.bean;

import com.execue.core.common.bean.governor.AssetQuery;
import com.execue.core.common.bean.querygen.QueryRepresentation;

public class AggregationMessage {

   AssetQuery          assetQuery;
   long                queryId;
   long                businessQueryId;
   QueryRepresentation governorQueryRepresentation;

   public AssetQuery getAssetQuery () {
      return assetQuery;
   }

   public void setAssetQuery (AssetQuery assetQuery) {
      this.assetQuery = assetQuery;
   }

   public long getQueryId () {
      return queryId;
   }

   public void setQueryId (long queryId) {
      this.queryId = queryId;
   }

   public QueryRepresentation getGovernorQueryRepresentation () {
      return governorQueryRepresentation;
   }

   public void setGovernorQueryRepresentation (QueryRepresentation governorQueryRepresentation) {
      this.governorQueryRepresentation = governorQueryRepresentation;
   }

   public long getBusinessQueryId () {
      return businessQueryId;
   }

   public void setBusinessQueryId (long businessQueryId) {
      this.businessQueryId = businessQueryId;
   }

}
