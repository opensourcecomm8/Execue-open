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


package com.execue.web.core.action.swi;

import java.util.ArrayList;
import java.util.List;

import com.execue.handler.bean.grid.IGridBean;
import com.execue.handler.swi.IDashBoardServiceHandler;

public class ApplicationListAction extends PaginationGridAction {

   private IDashBoardServiceHandler dashBoardServiceHandler;
   
   

   @Override
   protected List<IGridBean> processPageGrid () {
      List<IGridBean> gridBeans = new ArrayList<IGridBean>();
      try {
         // We can use menu type and then use set the flag advancedMenu to use getApplicationsByPage(getPageDetail(),
         // advancedMenu). Though the method with boolean flag has a glitch right now at HQL.
         gridBeans = getDashBoardServiceHandler().getApplicationsByPage(getPageDetail());
      } catch (Exception e) {
         // TODO: handle exception
         e.printStackTrace();
      }
      return gridBeans;
   }

   public IDashBoardServiceHandler getDashBoardServiceHandler () {
      return dashBoardServiceHandler;
   }

   public void setDashBoardServiceHandler (IDashBoardServiceHandler dashBoardServiceHandler) {
      this.dashBoardServiceHandler = dashBoardServiceHandler;
   }

}
