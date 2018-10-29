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


package com.execue.usca.service.impl;

import java.util.Date;
import java.util.List;

import com.execue.core.common.bean.entity.unstructured.ca.Category;
import com.execue.core.common.bean.entity.unstructured.ca.Feed;
import com.execue.usca.dataaccess.IUnstructuredCADataAccessManager;
import com.execue.usca.exception.UnstructuredCAException;
import com.execue.usca.service.IUnstructuredCAManagementService;

public class UnstructuredCAManagementServiceImpl implements IUnstructuredCAManagementService {

   private IUnstructuredCADataAccessManager unstructuredCADataAccessManager;

   public void deleteLinksPriorToPublishedDate (Long contextId, Date publishedDate) throws UnstructuredCAException {
      getUnstructuredCADataAccessManager().deleteLinksPriorToPublishedDate(contextId, publishedDate);
   }

   public void createCategory (Long contextId, Category category) throws UnstructuredCAException {
      getUnstructuredCADataAccessManager().createCategory(contextId, category);
   }

   public void createFeed (Long contextId, String caDataSourceName, Feed feed) throws UnstructuredCAException {
      getUnstructuredCADataAccessManager().createFeed(contextId, caDataSourceName, feed);
   }

   public void createFeeds (Long contextId, String caDataSourceName, List<Feed> feeds) throws UnstructuredCAException {
      getUnstructuredCADataAccessManager().createFeeds(contextId, caDataSourceName, feeds);
   }

   public IUnstructuredCADataAccessManager getUnstructuredCADataAccessManager () {
      return unstructuredCADataAccessManager;
   }

   public void setUnstructuredCADataAccessManager (IUnstructuredCADataAccessManager unstructuredCADataAccessManager) {
      this.unstructuredCADataAccessManager = unstructuredCADataAccessManager;
   }

}
