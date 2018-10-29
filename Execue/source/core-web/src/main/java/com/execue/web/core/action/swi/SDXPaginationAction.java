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

public class SDXPaginationAction extends SWIPaginationAction {

   private static final Logger logger          = Logger.getLogger(SDXPaginationAction.class);

   private List<Asset>         assets;
   private static final int    PAGE_SIZE       = 9;
   private static final int    NUMBER_OF_LINKS = 4;

   @SuppressWarnings ("unchecked")
   @Override
   public String processPage () throws ExeCueException {
      try {
         getPageDetail().setPageSize(Long.valueOf(PAGE_SIZE));
         getPageDetail().setNumberOfLinks(Long.valueOf(NUMBER_OF_LINKS));
         List<Asset> listOfDataset = getSdxServiceHandler().getAllAssets(getApplicationContext().getAppId());
         // set the record count
         getPageDetail().setRecordCount(Long.valueOf(listOfDataset.size()));
         assets = getProcessedResults(listOfDataset, getPageDetail());
         // push the page object into request
         getHttpRequest().put(PAGINATION, getPageDetail());
      } catch (Exception exception) {
         logger.error(exception, exception);
         throw new ExeCueException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, exception);
      }
      return SUCCESS;
   }

   // TODO: enhance the logic to handle search operation
   private List<Asset> getProcessedResults (List<Asset> listOfDataset, Page pageDetail) {
      List<Asset> datasetList = listOfDataset;
      List<PageSearch> searchList = pageDetail.getSearchList();
      if (ExecueCoreUtil.isCollectionNotEmpty(searchList)) {
         // TODO: -JM- Currently there will be only one search object, change later if there are multiple searches
         PageSearch search = searchList.get(0);
         // check for the search info
         if (PageSearchType.STARTS_WITH == search.getType()) {
            datasetList = new ArrayList<Asset>();
            for (Asset dataset : listOfDataset) {
               String cDispName = dataset.getDisplayName();
               if (cDispName.toLowerCase().startsWith(search.getString().toLowerCase())) {
                  datasetList.add(dataset);
               }
            }
         } else if (PageSearchType.CONTAINS == search.getType()) {
            datasetList = new ArrayList<Asset>();
            for (Asset dataset : listOfDataset) {
               String cDispName = dataset.getDisplayName();
               if (cDispName.toLowerCase().contains(search.getString().toLowerCase())) {
                  datasetList.add(dataset);
               }
            }
         }
      }
      // modify the page object with the new record count which will modify the page count as well
      pageDetail.setRecordCount(Long.valueOf(datasetList.size()));
      List<Asset> pageDatasets = new ArrayList<Asset>();
      // manipulate the list to return the set of concepts belonging to the page requested
      int start = (pageDetail.getRequestedPage().intValue() - 1) * pageDetail.getPageSize().intValue();
      int end = start + pageDetail.getPageSize().intValue();
      if (end > pageDetail.getRecordCount().intValue()) {
         end = (pageDetail.getRecordCount().intValue());
      }
      for (int i = start; i < end; i++) {
         pageDatasets.add(datasetList.get(i));
      }
      return pageDatasets;
   }

   public List<Asset> getAssets () {
      return assets;
   }

   public void setAssets (List<Asset> assets) {
      this.assets = assets;
   }

}
