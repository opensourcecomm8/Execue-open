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


package com.execue.qdata.news.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.execue.core.common.bean.qdata.news.AppNewsUsageWeight;
import com.execue.core.common.bean.qdata.news.AppTrend;
import com.execue.core.common.bean.qdata.news.AppUsagePopularity;
import com.execue.core.common.bean.qdata.news.NewsUsagePopularity;
import com.execue.qdata.news.Helper.AppTrendHelper;
import com.execue.qdata.news.dataaccess.IAppPopularityDataAccessManager;
import com.execue.qdata.news.exception.AppTrendException;
import com.execue.qdata.news.service.IAppPopularityService;

public class AppPopularityServiceImpl implements IAppPopularityService {

   private IAppPopularityDataAccessManager appPopularityDataAccessManager;

   public List<AppTrend> processAppUsageTrend (Integer newsLimit) throws AppTrendException {
      List<AppTrend> appTrends = new ArrayList<AppTrend>();

      List<AppUsagePopularity> appUsagePopularity = new ArrayList<AppUsagePopularity>();
      List<NewsUsagePopularity> newsUsagePopularity = new ArrayList<NewsUsagePopularity>();
      AppNewsUsageWeight weights = null;
      Integer appNewsUsageLimit = 0;

      try {
         appUsagePopularity = getAppPopularityDataAccessManager().getAppUsagePopularity();
         if (newsLimit != null) {
            newsUsagePopularity = getAppPopularityDataAccessManager().getAppNewsUsagePopularity(newsLimit);
         }
         newsUsagePopularity = getAppPopularityDataAccessManager().getAppNewsUsagePopularity();

         weights = getAppPopularityDataAccessManager().getAppNewsUsageWeights();
         weights.setTotalApps(appUsagePopularity.size());
         processAppUsageSort(appUsagePopularity);
         processAppUsageRanking(appUsagePopularity, weights);
         processAppUsageEntitySort(appUsagePopularity);
         processAppUsageEntityRanking(appUsagePopularity, weights);
         processAppUsage(appUsagePopularity, weights);

         // News
         processNewsUsageSort(newsUsagePopularity);
         processNewsUsageRanking(newsUsagePopularity, weights);
         processNewsUsageEntitySort(newsUsagePopularity);
         processNewsUsageEntityRanking(newsUsagePopularity, weights);
         processNewsUsage(newsUsagePopularity, weights);
         // /
         processAppTrend(appTrends, appUsagePopularity, newsUsagePopularity, weights);
         appPopularityDataAccessManager.createAppTrend(appTrends);
         return appTrends;

      } catch (AppTrendException e) {
         // TODO Auto-generated catch block
         throw e;
      }

   }

   public IAppPopularityDataAccessManager getAppPopularityDataAccessManager () {
      return appPopularityDataAccessManager;
   }

   public void setAppPopularityDataAccessManager (IAppPopularityDataAccessManager appPopularityDataAccessManager) {
      this.appPopularityDataAccessManager = appPopularityDataAccessManager;
   }

   private void processAppUsageRanking (List<AppUsagePopularity> appUsagePopularity, AppNewsUsageWeight weights) {
      int size = appUsagePopularity.size();
      for (AppUsagePopularity appUsagePopularityLoop : appUsagePopularity) {
         appUsagePopularityLoop.setUsetHitsRank(Long.valueOf("" + size--));
         appUsagePopularityLoop.setUsageScore(((weights.getTotalApps() + 1 - appUsagePopularityLoop.getUsetHitsRank())
                  / weights.getTotalApps() + 0.0));

      }

   }

   private void processAppUsageEntityRanking (List<AppUsagePopularity> appUsagePopularity, AppNewsUsageWeight weights) {
      int size = appUsagePopularity.size();
      for (AppUsagePopularity appUsagePopularityLoop : appUsagePopularity) {
         appUsagePopularityLoop.setUseEntityRank(new Long(size--));
         appUsagePopularityLoop.setEntityUsageScore(((weights.getTotalApps() + 1 - appUsagePopularityLoop
                  .getUseEntityRank())
                  / weights.getTotalApps() + 0.0));

      }
   }

   private void processNewsUsageRanking (List<NewsUsagePopularity> newsUsagePopularities, AppNewsUsageWeight weights) {
      int size = newsUsagePopularities.size();
      for (NewsUsagePopularity newsUsagePopularity : newsUsagePopularities) {
         newsUsagePopularity.setUsetHitsRank(new Long(size--));
         newsUsagePopularity.setUsageScore(((weights.getTotalApps() + 1 - newsUsagePopularity.getUseEntityRank())
                  / weights.getTotalApps() + 0.0));

      }
   }

   private void processNewsUsageEntityRanking (List<NewsUsagePopularity> newsUsagePopularities,
            AppNewsUsageWeight weights) {
      int size = newsUsagePopularities.size();
      for (NewsUsagePopularity newsUsagePopularity : newsUsagePopularities) {
         newsUsagePopularity.setUseEntityRank(new Long(size--));
         newsUsagePopularity.setEntityUsageScore(((weights.getTotalApps() + 1 - newsUsagePopularity.getUseEntityRank())
                  / weights.getTotalApps() + 0.0));

      }
   }

   private void processAppUsage (List<AppUsagePopularity> appUsagePopularity, AppNewsUsageWeight weights) {
      for (AppUsagePopularity appUsagePopularityLoop : appUsagePopularity) {
         appUsagePopularityLoop.setTotalUsageScore(weights.getUsageWeight1() * appUsagePopularityLoop.getUsageScore()
                  + weights.getUsageWeight2() * appUsagePopularityLoop.getEntityUsageScore());

      }
   }

   private void processNewsUsage (List<NewsUsagePopularity> newsUsagePopularities, AppNewsUsageWeight weights) {
      for (NewsUsagePopularity newsUsagePopularity : newsUsagePopularities) {
         newsUsagePopularity.setTotalUsageScore(weights.getNewsWeight1() * newsUsagePopularity.getUsageScore()
                  + weights.getNewsWeight2() * newsUsagePopularity.getEntityUsageScore());

      }
   }

   private void processAppTrend (List<AppTrend> appTrends, List<AppUsagePopularity> appUsagePopularity,
            List<NewsUsagePopularity> newsUsagePopularities, AppNewsUsageWeight weights) {

      for (AppUsagePopularity appUsagePopularityLoop : appUsagePopularity) {
         Double dynamiFactor = getRandomValue() * appUsagePopularityLoop.getAppConstanRandFactor()
                  * weights.getGlobalRandFactor();
         AppTrend appTrend = new AppTrend();
         Double finalScore = appUsagePopularityLoop.getTotalUsageScore() * weights.getUsage();
         appTrend.setAppId(appUsagePopularityLoop.getAppId());
         appTrend.setAppUsagePopularity(appUsagePopularityLoop);
         for (NewsUsagePopularity newsUsagePopularity : newsUsagePopularities) {
            if (newsUsagePopularity.getAppId().longValue() == appUsagePopularityLoop.getAppId().longValue()) {
               finalScore = finalScore + newsUsagePopularity.getTotalUsageScore() * weights.getNews();
               appTrend.setNewsTrend(newsUsagePopularity);

               break;
            }
         }
         appTrend.setAppRank(getFormatValue((finalScore + dynamiFactor)));
         appTrends.add(appTrend);
      }
   }

   private void processAppUsageSort (List<AppUsagePopularity> appUsagePopularity) {
      if (appUsagePopularity != null) {
         Collections.sort(appUsagePopularity, AppTrendHelper.AppUsageComparator);
      }

   }

   private void processAppUsageEntitySort (List<AppUsagePopularity> appUsagePopularity) {
      if (appUsagePopularity != null) {
         Collections.sort(appUsagePopularity, AppTrendHelper.AppUsageEntityComparator);
      }

   }

   private void processNewsUsageSort (List<NewsUsagePopularity> newsUsagePopularities) {
      if (newsUsagePopularities != null) {
         Collections.sort(newsUsagePopularities, AppTrendHelper.NewsUsageComparator);
      }
   }

   private void processNewsUsageEntitySort (List<NewsUsagePopularity> newsUsagePopularities) {
      if (newsUsagePopularities != null) {
         Collections.sort(newsUsagePopularities, AppTrendHelper.NewsUsageEntityComparator);
      }
   }

   private Double getRandomValue () {
      java.text.DecimalFormat df = new java.text.DecimalFormat("##.##");
      Random r = new Random();
      return new Double(df.format(r.nextDouble()));

   }

   private Double getFormatValue (Double doubleValue) {
      java.text.DecimalFormat df = new java.text.DecimalFormat("###.##");
      return new Double(df.format(doubleValue));
   }
}
