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


package com.execue.qdata.news.dataaccess;

import java.util.List;

import com.execue.core.common.bean.qdata.news.AppNewsUsageWeight;
import com.execue.core.common.bean.qdata.news.AppTrend;
import com.execue.core.common.bean.qdata.news.AppUsagePopularity;
import com.execue.core.common.bean.qdata.news.NewsUsagePopularity;
import com.execue.qdata.news.exception.AppTrendException;

public interface IAppPopularityDataAccessManager {

   public void createAppTrend (List<AppTrend> appTrends) throws AppTrendException;

   public List<NewsUsagePopularity> getAppNewsUsagePopularity () throws AppTrendException;

   public List<NewsUsagePopularity> getAppNewsUsagePopularity (Integer Limit) throws AppTrendException;

   public List<AppUsagePopularity> getAppUsagePopularity () throws AppTrendException;

   public AppNewsUsageWeight getAppNewsUsageWeights () throws AppTrendException;

}
