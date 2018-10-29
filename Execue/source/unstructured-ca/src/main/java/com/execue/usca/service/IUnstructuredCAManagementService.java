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


package com.execue.usca.service;

import java.util.Date;
import java.util.List;

import com.execue.core.common.bean.entity.unstructured.ca.Category;
import com.execue.core.common.bean.entity.unstructured.ca.Feed;
import com.execue.usca.exception.UnstructuredCAException;

public interface IUnstructuredCAManagementService {

   /**
    * Get the content aggregater sources mapped to the context and 
    *   on all of them 
    *     delete the links which are prior to the published date provided
    * 
    * @param contextId
    * @param publishedDate
    * @throws UnstructuredCAException
    */
   public void deleteLinksPriorToPublishedDate (Long contextId, Date publishedDate) throws UnstructuredCAException;

   /**
    * Get the content aggregater sources mapped to the context and 
    *   on all of them create the Category provided
    * 
    * @param contextId
    * @param category
    * @throws UnstructuredCAException
    */
   public void createCategory (Long contextId, Category category) throws UnstructuredCAException;

   /**
    * For the context provided, add the feed 
    *   only on the content aggregater source provided
    * 
    * @param contextId
    * @param caDataSourceName
    * @param feed
    * @throws UnstructuredCAException
    */
   public void createFeed (Long contextId, String caDataSourceName, Feed feed) throws UnstructuredCAException;

   /**
    * For the context provided, add the feed(s) 
    *   only on the content aggregater source provided
    * 
    * @param contextId
    * @param caDataSourceName
    * @param feed
    * @throws UnstructuredCAException
    */
   public void createFeeds (Long contextId, String caDataSourceName, List<Feed> feeds) throws UnstructuredCAException;
}
