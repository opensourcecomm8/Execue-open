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
import com.execue.core.common.bean.entity.DataSource;
import com.execue.core.common.type.PageSearchType;
import com.execue.core.exception.ExeCueException;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.core.util.ExecueCoreUtil;

public class DataSourcePaginationAction extends SWIPaginationAction {

   private static final Logger logger          = Logger.getLogger(DataSourcePaginationAction.class);

   private List<DataSource>    dataSources;
   private static final int    PAGE_SIZE       = 9;
   private static final int    NUMBER_OF_LINKS = 4;

   @Override
   public String processPage () throws ExeCueException {
      try {
         getPageDetail().setPageSize(Long.valueOf(PAGE_SIZE));
         getPageDetail().setNumberOfLinks(Long.valueOf(NUMBER_OF_LINKS));
         List<DataSource> listOfDataSource = getSdxServiceHandler().getDisplayableDataSources();
         // set the record count
         getPageDetail().setRecordCount(Long.valueOf(listOfDataSource.size()));
         dataSources = getProcessedResults(listOfDataSource, getPageDetail());
         // push the page object into request
         getHttpRequest().put(PAGINATION, getPageDetail());

      } catch (Exception exception) {
         throw new ExeCueException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, exception);
      }
      return SUCCESS;
   }

   // TODO: enhance the logic to handle search operation
   private List<DataSource> getProcessedResults (List<DataSource> listOfDataSource, Page pageDetail) {
      List<DataSource> dataSourceList = listOfDataSource;
      List<PageSearch> searchList = pageDetail.getSearchList();
      if (ExecueCoreUtil.isCollectionNotEmpty(searchList)) {
         // TODO: -JM- Currently there will be only one search object, change later if there are multiple searches
         PageSearch search = searchList.get(0);
         // check for the search info
         if (PageSearchType.STARTS_WITH == search.getType()) {
            dataSourceList = new ArrayList<DataSource>();
            for (DataSource dataSource : dataSourceList) {
               // TODO: -JM- use the field from the search object
               String cDispName = dataSource.getName();
               if (cDispName.toLowerCase().startsWith(search.getString().toLowerCase())) {
                  dataSourceList.add(dataSource);
               }
            }
         } else if (PageSearchType.CONTAINS == search.getType()) {
            dataSourceList = new ArrayList<DataSource>();
            for (DataSource dataSource : dataSourceList) {
               String cDispName = dataSource.getName();
               if (cDispName.toLowerCase().startsWith(search.getString().toLowerCase())) {
                  dataSourceList.add(dataSource);
               }
            }
         }
      }
      // modify the page object with the new record count which will modify the page count as well
      pageDetail.setRecordCount(Long.valueOf(dataSourceList.size()));
      List<DataSource> pageDataSources = new ArrayList<DataSource>();
      // manipulate the list to return the set of concepts belonging to the page requested
      int start = (pageDetail.getRequestedPage().intValue() - 1) * pageDetail.getPageSize().intValue();
      int end = start + pageDetail.getPageSize().intValue();
      if (end > pageDetail.getRecordCount().intValue()) {
         end = (pageDetail.getRecordCount().intValue());
      }
      for (int i = start; i < end; i++) {
         pageDataSources.add(dataSourceList.get(i));
      }
      return pageDataSources;
   }

   public List<DataSource> getDataSources () {
      return dataSources;
   }

   public void setDataSources (List<DataSource> dataSources) {
      this.dataSources = dataSources;
   }

}
