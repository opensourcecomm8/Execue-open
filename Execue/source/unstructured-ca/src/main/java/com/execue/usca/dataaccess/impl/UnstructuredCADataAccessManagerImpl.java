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


package com.execue.usca.dataaccess.impl;

import java.util.Date;
import java.util.List;

import com.execue.core.common.bean.entity.unstructured.ca.Category;
import com.execue.core.common.bean.entity.unstructured.ca.Feed;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.usca.dataaccess.IUnstructuredCADataAccessManager;
import com.execue.usca.exception.UnstructuredCAException;
import com.execue.uscada.dataaccess.dao.IUnstructuredCALinkDAO;

public class UnstructuredCADataAccessManagerImpl implements IUnstructuredCADataAccessManager {

   private IUnstructuredCALinkDAO unstructuredCALinkDAO;

   public void deleteLinksPriorToPublishedDate (Long contextId, Date publishedDate) throws UnstructuredCAException {
      try {
         getUnstructuredCALinkDAO().deleteLinksPriorToPublishedDate(contextId, publishedDate);
      } catch (DataAccessException dae) {
         throw new UnstructuredCAException(dae.getCode(), dae);
      }
   }

   public void createCategory (Long contextId, Category category) throws UnstructuredCAException {

   }

   public void createFeed (Long contextId, String caDataSourceName, Feed feed) throws UnstructuredCAException {

   }

   public void createFeeds (Long contextId, String caDataSourceName, List<Feed> feeds) throws UnstructuredCAException {

   }

   public IUnstructuredCALinkDAO getUnstructuredCALinkDAO () {
      return unstructuredCALinkDAO;
   }

   public void setUnstructuredCALinkDAO (IUnstructuredCALinkDAO unstructuredCALinkDAO) {
      this.unstructuredCALinkDAO = unstructuredCALinkDAO;
   }

}
