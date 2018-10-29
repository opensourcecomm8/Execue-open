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

import java.util.List;

import com.execue.core.common.bean.PageSearch;
import com.execue.core.common.type.PageSearchType;
import com.execue.core.exception.ExeCueException;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.handler.bean.grid.IGridBean;
import com.execue.handler.bean.grid.UIGrid;

/**
 * Used for JSON representation for grid object.
 * 
 * @author kaliki
 */
public abstract class PaginationGridAction extends SWIPaginationAction {

   private UIGrid grid;

   @Override
   public String processPage () throws ExeCueException {
      // transform searchType
      transformSearchType();
      List<IGridBean> list = (List<IGridBean>) processPageGrid();
      grid = new UIGrid();
      grid.setPage(getPageDetail().getRequestedPage().intValue());
      grid.setRecords(getPageDetail().getRecordCount().intValue());
      grid.setTotal(getPageDetail().getPageCount().intValue());
      grid.setRows(list);
      return SUCCESS;
   }

   private void transformSearchType () {
      if (ExecueCoreUtil.isCollectionEmpty(getPageDetail().getSearchList())) {
         return;
      }
      PageSearch pageSearch = getPageDetail().getSearchList().get(0);
      String searchType = getSearchType();
      // TODO: -KA- how to handle multiple list ?
      if ("bw".equalsIgnoreCase(searchType)) {
         pageSearch.setType(PageSearchType.STARTS_WITH);
      } else if ("ew".equalsIgnoreCase(searchType)) {
         pageSearch.setType(PageSearchType.ENDS_WITH);
      } else if ("cn".equalsIgnoreCase(searchType)) {
         pageSearch.setType(PageSearchType.CONTAINS);
      } else if ("eq".equalsIgnoreCase(searchType)) {
         pageSearch.setType(PageSearchType.EQUALS);
      }
   }

   protected abstract List<? extends IGridBean> processPageGrid ();

   public UIGrid getGrid () {
      return grid;
   }

   public void setGrid (UIGrid grid) {
      this.grid = grid;
   }

}
