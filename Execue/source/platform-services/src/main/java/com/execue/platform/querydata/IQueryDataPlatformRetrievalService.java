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


package com.execue.platform.querydata;

import java.util.List;

import com.execue.core.common.bean.qdata.QDataAggregatedQuery;
import com.execue.core.common.bean.qdata.QDataUserQuery;
import com.execue.qdata.exception.QueryDataException;

public interface IQueryDataPlatformRetrievalService {

   public QDataAggregatedQuery getLoadedAggregatedQueryById (Long aggregateQueryId) throws QueryDataException;

   public List<QDataUserQuery> getMatchUseryQueries (String userQuery) throws QueryDataException;

   public Integer getNextSeedValueByTypeAndNodeId (Long nodeId, String type) throws QueryDataException;
}
