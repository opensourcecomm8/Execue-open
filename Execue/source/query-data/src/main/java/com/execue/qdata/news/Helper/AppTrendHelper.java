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


package com.execue.qdata.news.Helper;

import java.util.Comparator;

import com.execue.core.common.bean.qdata.news.AppUsagePopularity;
import com.execue.core.common.bean.qdata.news.NewsUsagePopularity;

public class AppTrendHelper {

   public static Comparator<AppUsagePopularity> AppUsageComparator        = new Comparator<AppUsagePopularity>() {

                                                                                     public int compare (
                                                                                              AppUsagePopularity obj1,
                                                                                              AppUsagePopularity obj2) {
                                                                                        return obj1
                                                                                                 .getAppPopularity()
                                                                                                 .compareTo(
                                                                                                          obj2
                                                                                                                   .getAppPopularity());
                                                                                     }

                                                                                  };
   public static Comparator<AppUsagePopularity> AppUsageEntityComparator  = new Comparator<AppUsagePopularity>() {

                                                                                     public int compare (
                                                                                              AppUsagePopularity obj1,
                                                                                              AppUsagePopularity obj2) {
                                                                                        return obj1
                                                                                                 .getBusinessEntityPopularity()
                                                                                                 .compareTo(
                                                                                                          obj2
                                                                                                                   .getBusinessEntityPopularity());
                                                                                     }

                                                                                  };
   public static Comparator<NewsUsagePopularity>        NewsUsageComparator       = new Comparator<NewsUsagePopularity>() {

                                                                                     public int compare (
                                                                                              NewsUsagePopularity obj1,
                                                                                              NewsUsagePopularity obj2) {
                                                                                        return obj1
                                                                                                 .getNewsHitsPopularity()
                                                                                                 .compareTo(
                                                                                                          obj2
                                                                                                                   .getNewsHitsPopularity());
                                                                                     }

                                                                                  };
   public static Comparator<NewsUsagePopularity>        NewsUsageEntityComparator = new Comparator<NewsUsagePopularity>() {

                                                                                     public int compare (
                                                                                              NewsUsagePopularity obj1,
                                                                                              NewsUsagePopularity obj2) {
                                                                                        return obj1
                                                                                                 .getNewsEntityPopularity()
                                                                                                 .compareTo(
                                                                                                          obj2
                                                                                                                   .getNewsEntityPopularity());
                                                                                     }

                                                                                  };

}
