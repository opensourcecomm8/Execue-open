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


package com.execue.qdata.news.dataaccess.dao.impl;

import java.util.List;

import com.execue.core.common.bean.qdata.news.AppNewsUsageWeight;
import com.execue.core.common.bean.qdata.news.AppTrend;
import com.execue.core.common.bean.qdata.news.AppUsagePopularity;
import com.execue.core.common.bean.qdata.news.NewsUsagePopularity;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.dataaccess.qdata.dao.IAppPopularityDataAccessDAO;
import com.execue.qdata.news.dataaccess.IAppPopularityDataAccessManager;
import com.execue.qdata.news.exception.AppTrendException;

public class AppPopularityDataAcessManagerImpl implements IAppPopularityDataAccessManager {

   private IAppPopularityDataAccessDAO     appPopularityDataAccessdao;
   

   public void createAppTrend (List<AppTrend> appTrends) throws AppTrendException {
      try {
         getAppPopularityDataAccessdao().createAppTrend(appTrends);
      } catch (DataAccessException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
         throw new AppTrendException(e.getCode(), e);
      }

   }

   public List<AppUsagePopularity> getAppUsagePopularity () throws AppTrendException {
      try {
         return getAppPopularityDataAccessdao().getAppUsagepopularity();
      } catch (DataAccessException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
         throw new AppTrendException(e.getCode(), e);
      }
      // return null;
   }

   public List<NewsUsagePopularity> getAppNewsUsagePopularity (Integer maxResults) throws AppTrendException {
      try {
         return getAppPopularityDataAccessdao().getAppNewspopularityByLimit(maxResults);
      } catch (DataAccessException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
         throw new AppTrendException(e.getCode(), e);
      }
      // return null;
   }

   public List<NewsUsagePopularity> getAppNewsUsagePopularity () throws AppTrendException {
      try {
         return getAppPopularityDataAccessdao().getAppNewspopularity();
      } catch (DataAccessException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
         throw new AppTrendException(e.getCode(), e);
      }
      // return null;
   }

   public IAppPopularityDataAccessDAO getAppPopularityDataAccessdao () {
      return appPopularityDataAccessdao;
   }

   public void setAppPopularityDataAccessdao (IAppPopularityDataAccessDAO appPopularityDataAccessdao) {
      this.appPopularityDataAccessdao = appPopularityDataAccessdao;
   }

   public AppNewsUsageWeight getAppNewsUsageWeights () throws AppTrendException {
      try {
         return getAppPopularityDataAccessdao().getAppNewsUsageWeights();
      } catch (DataAccessException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
         throw new AppTrendException(e.getCode(), e);
      }
      // return null;
   }

  

}
