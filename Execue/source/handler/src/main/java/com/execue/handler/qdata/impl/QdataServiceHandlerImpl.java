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


package com.execue.handler.qdata.impl;

import java.util.List;

import com.execue.core.common.bean.qdata.NewsItem;
import com.execue.core.common.type.NewsCategory;
import com.execue.core.exception.HandlerException;
import com.execue.handler.qdata.IQdataServiceHandler;
import com.execue.qdata.exception.QueryDataException;
import com.execue.qdata.service.IQueryDataService;

public class QdataServiceHandlerImpl implements IQdataServiceHandler {

   public List<NewsItem> getNewsItemsByCategory (NewsCategory category) throws HandlerException {
      // TODO-need to read size from the configuration
      try {
         List<NewsItem> newsItemsByCategory = getQueryDataService().getNewsItemsByCategory(category, 14L);
         removeHtmlContent(newsItemsByCategory);
         return newsItemsByCategory;
      } catch (QueryDataException e) {
         throw new HandlerException(e.getCode(), e);
      }
   }

   private void removeHtmlContent (List<NewsItem> newsItems) {
      for (NewsItem newsItem : newsItems) {
         String desciption = newsItem.getSummary();
         // modify the description of the news.
         if (desciption != null) {
            int htmlTagIndex = desciption.indexOf('<');
            if (htmlTagIndex != -1) {
               desciption = desciption.substring(0, htmlTagIndex);
               newsItem.setSummary(desciption);
            }
         }
      }
   }

   private IQueryDataService queryDataService;

   public IQueryDataService getQueryDataService () {
      return queryDataService;
   }

   public void setQueryDataService (IQueryDataService queryDataService) {
      this.queryDataService = queryDataService;
   }

}
