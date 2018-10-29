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
import com.execue.core.common.bean.entity.Relation;
import com.execue.core.common.type.PageSearchType;
import com.execue.core.exception.ExeCueException;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.core.util.ExecueCoreUtil;

/**
 * @author John Mallavalli
 */
public class RelationsPaginationAction extends SWIPaginationAction {

   private static Logger    logger          = Logger.getLogger(RelationsPaginationAction.class);
   private List<Relation>   relations;
   private static final int PAGE_SIZE       = 11;
   private static final int NUMBER_OF_LINKS = 4;

   // This is Memory Pagination type - get all the relations and persist in the session
   @SuppressWarnings ("unchecked")
   public String processPage () throws ExeCueException {
      try {
         getPageDetail().setPageSize(Long.valueOf(PAGE_SIZE));
         getPageDetail().setNumberOfLinks(Long.valueOf(NUMBER_OF_LINKS));
         List<Relation> allRelations = getKdxServiceHandler().getRelations(getApplicationContext().getModelId());
         // set the record count
         getPageDetail().setRecordCount(Long.valueOf(allRelations.size()));
         relations = getProcessedResults(allRelations, getPageDetail());
         // push the page object into request
         if (logger.isDebugEnabled()) {
            logger.debug(getPageDetail().toString());
         }
         getHttpRequest().put(PAGINATION, getPageDetail());
      } catch (Exception exception) {
         logger.error(exception, exception);
         throw new ExeCueException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, exception);
      }
      return SUCCESS;
   }

   // TODO: enhance the logic to handle search operation
   private List<Relation> getProcessedResults (List<Relation> allRelations, Page pageDetail) {
      List<Relation> relationsList = allRelations;
      List<PageSearch> searchList = pageDetail.getSearchList();
      if (ExecueCoreUtil.isCollectionNotEmpty(searchList)) {
         // TODO: -JM- Currently there will be only one search object, change later if there are multiple searches
         PageSearch search = searchList.get(0);
         // check for the search info
         if (PageSearchType.STARTS_WITH == search.getType()) {
            relationsList = new ArrayList<Relation>();
            for (Relation relation : allRelations) {
               // TODO: -JM- use the field from the search object
               String relationDisplayName = relation.getDisplayName();
               if (relationDisplayName.toLowerCase().startsWith(search.getString().toLowerCase())) {
                  relationsList.add(relation);
               }
            }
         } else if (PageSearchType.CONTAINS == search.getType()) {
            relationsList = new ArrayList<Relation>();
            for (Relation relation : allRelations) {
               String relationDisplayName = relation.getDisplayName();
               if (relationDisplayName.toLowerCase().contains(search.getString().toLowerCase())) {
                  relationsList.add(relation);
               }
            }
         }
      }
      // modify the page object with the new record count which will modify the page count as well
      pageDetail.setRecordCount(Long.valueOf(relationsList.size()));
      List<Relation> pageRelations = new ArrayList<Relation>();
      // manipulate the list to return the set of relations belonging to the page requested
      int start = (pageDetail.getRequestedPage().intValue() - 1) * pageDetail.getPageSize().intValue();
      int end = start + pageDetail.getPageSize().intValue();
      if (end > pageDetail.getRecordCount().intValue()) {
         end = (pageDetail.getRecordCount().intValue());
      }
      for (int i = start; i < end; i++) {
         pageRelations.add(relationsList.get(i));
      }
      return pageRelations;
   }

   public List<Relation> getRelations () {
      return relations;
   }

   public void setRelations (List<Relation> relations) {
      this.relations = relations;
   }
}
