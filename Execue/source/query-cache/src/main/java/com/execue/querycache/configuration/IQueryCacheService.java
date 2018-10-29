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


package com.execue.querycache.configuration;

import java.util.List;
import java.util.Map;

import com.execue.core.IService;
import com.execue.core.common.bean.QueryForm;
import com.execue.core.common.bean.RelatedUserQuery;
import com.execue.core.common.bean.qdata.QDataReducedQuery;
import com.execue.core.common.bean.qdata.QDataUserQuery;
import com.execue.core.common.bean.qdata.QueryCacheResultInfo;
import com.execue.querycache.exception.QueryCacheException;

public interface IQueryCacheService extends IService {

   public QDataUserQuery getMatchQueryIdOnUserQuery (String userQuery, QueryForm form) throws QueryCacheException;

   public List<QueryCacheResultInfo> getQueryCacheResults (Long existingUserQueryId, Long applicationId)
            throws QueryCacheException;

   public List<RelatedUserQuery> performUniversalSearchForRelatedQueries (long queryId,
            Map<Long, QDataReducedQuery> reducedQueryByAppId, String queryName) throws QueryCacheException;

}
