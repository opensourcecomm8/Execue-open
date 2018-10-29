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


package com.execue.web.core.action.reporting.presentation;

import java.util.List;

import com.execue.handler.presentation.IPresentationHandler;
import com.opensymphony.xwork2.ActionSupport;

public class StockChartAction extends ActionSupport {

   private IPresentationHandler presentationHandler;

   private String               tkrName;
   private List<Long>           agQueryIdList;
   private Long                 aggQueryAppId;

   @Override
   public String execute () throws Exception {
      Long aggQueryId = agQueryIdList.get(0);
      setTkrName(getPresentationHandler().getTkrNameByAggregatedQuery(aggQueryId, aggQueryAppId));
      return SUCCESS;

   }

   public String getTkrName () {
      return tkrName;
   }

   public void setTkrName (String tkrName) {
      this.tkrName = tkrName;
   }

   public IPresentationHandler getPresentationHandler () {
      return presentationHandler;
   }

   public void setPresentationHandler (IPresentationHandler presentationHandler) {
      this.presentationHandler = presentationHandler;
   }

   public List<Long> getAgQueryIdList () {
      return agQueryIdList;
   }

   public void setAgQueryIdList (List<Long> agQueryIdList) {
      this.agQueryIdList = agQueryIdList;
   }

   public Long getAggQueryAppId () {
      return aggQueryAppId;
   }

   public void setAggQueryAppId (Long aggQueryAppId) {
      this.aggQueryAppId = aggQueryAppId;
   }

}
