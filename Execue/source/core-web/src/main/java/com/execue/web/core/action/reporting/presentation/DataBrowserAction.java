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

import java.util.ArrayList;
import java.util.List;

import com.execue.core.exception.HandlerException;
import com.execue.handler.bean.grid.IGridBean;
import com.execue.handler.bean.grid.UIColumnGrid;
import com.execue.handler.bean.grid.UIDataBrowserGrid;
import com.execue.handler.presentation.IPresentationHandler;
import com.execue.qdata.service.IQueryDataService;
import com.execue.web.core.action.swi.PaginationGridAction;

/**
 * @author John Mallavalli
 */
public class DataBrowserAction extends PaginationGridAction {

   private IQueryDataService    queryDataService;
   private String               agQueryIdList;
   private IPresentationHandler presentationHandler;
   private UIColumnGrid         dataBrowserGrid;
   private long                 agQueryId;
   private String               source;

   // field for holding JQGrid header data
   @Override
   protected List<? extends IGridBean> processPageGrid () {
      // return list of UIDataBrowserGrid (extending from IGridBean)
      return retrieveGridData();
   }

   // method for returning the header data
   public String retrieveHeaderData () {
      // get the header data and set it into gridHeaders variable - using report data xml
      List<Long> aggregatedQueryIds = new ArrayList<Long>();
      if (agQueryIdList.contains(",")) {
         String[] stringList = agQueryIdList.split(",");
         for (String item : stringList)
            aggregatedQueryIds.add(Long.parseLong(item));
      } else {
         aggregatedQueryIds.add(Long.parseLong(agQueryIdList));
      }
      try {
         for (long aggQueryId : aggregatedQueryIds) {
            UIColumnGrid columnGrid = new UIColumnGrid();
            // TODO -SS- create a new service layer which just returns the header without any data
            columnGrid = presentationHandler.processDetailReportHeaderRequest(aggQueryId);
            if (columnGrid != null) {
               setDataBrowserGrid(columnGrid);
               if (getDataBrowserGrid() != null) {
                  getDataBrowserGrid().setGridId(aggQueryId);
                  // TODO -SS- set the assetId, bqId and usrQId as well in the handler layer to be used by other modules
                  // at reporting page
                  setAgQueryId(aggQueryId);
               }
            }
         }
      } catch (HandlerException e) {
         e.printStackTrace();
      }
      return SUCCESS;
   }

   // method for returning the grid data
   public List<UIDataBrowserGrid> retrieveGridData () {
      // internal service calls to get the actual page data
      List<UIDataBrowserGrid> gridCellData = new ArrayList<UIDataBrowserGrid>();
      try {
         gridCellData = presentationHandler.processReportRequest(agQueryId, getPageDetail());
      } catch (Exception e) {
         e.printStackTrace();
      }
      return gridCellData;
   }

   public IQueryDataService getQueryDataService () {
      return queryDataService;
   }

   public void setQueryDataService (IQueryDataService queryDataService) {
      this.queryDataService = queryDataService;
   }

   public String getAgQueryIdList () {
      return agQueryIdList;
   }

   public void setAgQueryIdList (String agQueryIdList) {
      this.agQueryIdList = agQueryIdList;
   }

   public IPresentationHandler getPresentationHandler () {
      return presentationHandler;
   }

   public void setPresentationHandler (IPresentationHandler presentationHandler) {
      this.presentationHandler = presentationHandler;
   }

   public long getAgQueryId () {
      return agQueryId;
   }

   public void setAgQueryId (long agQueryId) {
      this.agQueryId = agQueryId;
   }

   /**
    * @return the dataBrowserGrid
    */
   public UIColumnGrid getDataBrowserGrid () {
      return dataBrowserGrid;
   }

   /**
    * @param dataBrowserGrid
    *           the dataBrowserGrid to set
    */
   public void setDataBrowserGrid (UIColumnGrid dataBrowserGrid) {
      this.dataBrowserGrid = dataBrowserGrid;
   }

   public String getSource () {
      return source;
   }

   public void setSource (String source) {
      this.source = source;
   }

}
