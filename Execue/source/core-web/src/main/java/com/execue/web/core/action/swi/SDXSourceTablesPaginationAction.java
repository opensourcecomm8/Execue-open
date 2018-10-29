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

import org.apache.log4j.Logger;

import com.execue.core.common.bean.Page;
import com.execue.core.common.bean.PageSearch;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.type.PageSearchType;
import com.execue.core.exception.ExeCueException;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.handler.bean.UITable;

public class SDXSourceTablesPaginationAction extends SWIPaginationAction {

   private static final Logger logger          = Logger.getLogger(SDXSourceTablesPaginationAction.class);

   private Asset               asset;
   private List<UITable>       sourceTables;
   private static final int    PAGE_SIZE       = 9;
   private static final int    NUMBER_OF_LINKS = 4;

   @SuppressWarnings ("unchecked")
   @Override
   public String processPage () throws ExeCueException {
      try {
         getPageDetail().setPageSize(Long.valueOf(PAGE_SIZE));
         getPageDetail().setNumberOfLinks(Long.valueOf(NUMBER_OF_LINKS));
         List<UITable> listOfTables = new ArrayList<UITable>();
         asset = getSdxServiceHandler().getAsset(asset.getId());
         listOfTables = getSdxServiceHandler().getAllSourceTables(asset);
         // set the record count
         getPageDetail().setRecordCount(Long.valueOf(listOfTables.size()));
         sourceTables = getProcessedResults(listOfTables, getPageDetail());
         // push the page object into request
         if (logger.isDebugEnabled()) {
            logger.debug("\n" + getPageDetail().toString());
         }
         getHttpRequest().put(PAGINATION, getPageDetail());
         logger.debug("table size is " + sourceTables.size());
      } catch (Exception exception) {
         logger.error(exception, exception);
         throw new ExeCueException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, exception);
      }
      return SUCCESS;
   }

   // TODO: enhance the logic to handle search operation
   private List<UITable> getProcessedResults (List<UITable> listOfTables, Page pageDetail) {
      List<UITable> tableList = listOfTables;
      List<PageSearch> searchList = pageDetail.getSearchList();
      if (ExecueCoreUtil.isCollectionNotEmpty(searchList)) {
         // TODO: -JM- Currently there will be only one search object, change later if there are multiple searches
         PageSearch search = searchList.get(0);
         // check for the search info
         if (PageSearchType.STARTS_WITH == search.getType()) {
            tableList = new ArrayList<UITable>();
            for (UITable table : listOfTables) {
               String cDispName = table.getDisplayName();
               if (ExecueCoreUtil.isNotEmpty(cDispName)
                        && cDispName.toLowerCase().startsWith(search.getString().toLowerCase())) {
                  tableList.add(table);
               }
            }
         } else if (PageSearchType.CONTAINS == search.getType()) {
            tableList = new ArrayList<UITable>();
            for (UITable table : listOfTables) {
               String cDispName = table.getDisplayName();
               if (ExecueCoreUtil.isNotEmpty(cDispName)
                        && cDispName.toLowerCase().contains(search.getString().toLowerCase())) {
                  tableList.add(table);
               }
            }
         }
      }
      // modify the page object with the new record count which will modify the page count as well
      pageDetail.setRecordCount(Long.valueOf(tableList.size()));
      List<UITable> pageTables = new ArrayList<UITable>();
      // manipulate the list to return the set of concepts belonging to the page requested
      int start = (pageDetail.getRequestedPage().intValue() - 1) * pageDetail.getPageSize().intValue();
      int end = start + pageDetail.getPageSize().intValue();
      if (end > pageDetail.getRecordCount().intValue()) {
         end = (pageDetail.getRecordCount().intValue());
      }
      for (int i = start; i < end; i++) {
         pageTables.add(tableList.get(i));
      }
      return pageTables;
   }

   public Asset getAsset () {
      return asset;
   }

   public void setAsset (Asset asset) {
      this.asset = asset;
   }

   public List<UITable> getSourceTables () {
      return sourceTables;
   }

   public void setSourceTables (List<UITable> sourceTables) {
      this.sourceTables = sourceTables;
   }

}
