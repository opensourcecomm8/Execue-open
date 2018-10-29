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


package com.execue.qdata.dataaccess;

import com.execue.dataaccess.qdata.dao.INewsItemDAO;
import com.execue.dataaccess.qdata.dao.IOptimalDSetSWIDAO;
import com.execue.dataaccess.qdata.dao.IQDataAggregatedQueryDAO;
import com.execue.dataaccess.qdata.dao.IQDataBusinessQueryDAO;
import com.execue.dataaccess.qdata.dao.IQDataReducedQueryDAO;
import com.execue.dataaccess.qdata.dao.IQDataUserQueryDAO;
import com.execue.dataaccess.qdata.dao.IUserNotificationDAO;

public class QDataDAOComponents {

   private IQDataBusinessQueryDAO   qDataBusinessQueryDAO;
   private IQDataAggregatedQueryDAO qDataAggregatedQueryDAO;
   private IQDataUserQueryDAO       qDataUserQueryDAO;
   private IQDataReducedQueryDAO    qDataReducedQueryDAO;
   private IUserNotificationDAO     userNotificationDAO;
   private INewsItemDAO             newsItemDAO;
   private IOptimalDSetSWIDAO       optimalDSetSWIDAO;

   public INewsItemDAO getNewsItemDAO () {
      return newsItemDAO;
   }

   public void setNewsItemDAO (INewsItemDAO newsItemDAO) {
      this.newsItemDAO = newsItemDAO;
   }

   public IQDataBusinessQueryDAO getQDataBusinessQueryDAO () {
      return qDataBusinessQueryDAO;
   }

   public void setQDataBusinessQueryDAO (IQDataBusinessQueryDAO dataBusinessQueryDAO) {
      qDataBusinessQueryDAO = dataBusinessQueryDAO;
   }

   public IQDataAggregatedQueryDAO getQDataAggregatedQueryDAO () {
      return qDataAggregatedQueryDAO;
   }

   public void setQDataAggregatedQueryDAO (IQDataAggregatedQueryDAO dataAggregatedQueryDAO) {
      qDataAggregatedQueryDAO = dataAggregatedQueryDAO;
   }

   public IQDataUserQueryDAO getQDataUserQueryDAO () {
      return qDataUserQueryDAO;
   }

   public void setQDataUserQueryDAO (IQDataUserQueryDAO dataUserQueryDAO) {
      qDataUserQueryDAO = dataUserQueryDAO;
   }

   public IQDataReducedQueryDAO getQDataReducedQueryDAO () {
      return qDataReducedQueryDAO;
   }

   public void setQDataReducedQueryDAO (IQDataReducedQueryDAO dataReducedQueryDAO) {
      qDataReducedQueryDAO = dataReducedQueryDAO;
   }

   public IUserNotificationDAO getUserNotificationDAO () {
      return userNotificationDAO;
   }

   public void setUserNotificationDAO (IUserNotificationDAO userNotificationDAO) {
      this.userNotificationDAO = userNotificationDAO;
   }

   
   public IOptimalDSetSWIDAO getOptimalDSetSWIDAO () {
      return optimalDSetSWIDAO;
   }

   
   public void setOptimalDSetSWIDAO (IOptimalDSetSWIDAO optimalDSetSWIDAO) {
      this.optimalDSetSWIDAO = optimalDSetSWIDAO;
   }
}
