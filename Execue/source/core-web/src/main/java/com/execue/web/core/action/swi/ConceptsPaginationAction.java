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
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.type.PageSearchType;
import com.execue.core.exception.ExeCueException;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.core.util.ExecueCoreUtil;

/**
 * @author John Mallavalli
 */
public class ConceptsPaginationAction extends SWIPaginationAction {

   private static Logger    logger          = Logger.getLogger(ConceptsPaginationAction.class);
   private List<Concept>    concepts;
   private static final int PAGE_SIZE       = 11;
   private static final int NUMBER_OF_LINKS = 4;
   // TODO:-JT- This variable(appModelId) will be used when to display concepts with pagination on for an app on app
   // page as we are not putting modeliD into app context.
   private Long             appModelId;

   // This is Memory Pagination type - get all the concepts and persist in the session
   @SuppressWarnings ("unchecked")
   public String processPage () throws ExeCueException {
      try {
         Long modelId = null;
         getPageDetail().setPageSize(Long.valueOf(PAGE_SIZE));
         getPageDetail().setNumberOfLinks(Long.valueOf(NUMBER_OF_LINKS));
         if (appModelId == null) {
            modelId = getApplicationContext().getModelId();
         } else {
            modelId = appModelId;
         }
         List<Concept> allConcepts = getKdxServiceHandler().getConcepts(modelId);
         // set the record count
         getPageDetail().setRecordCount(Long.valueOf(allConcepts.size()));
         concepts = getProcessedResults(allConcepts, getPageDetail());
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
   private List<Concept> getProcessedResults (List<Concept> allConcepts, Page pageDetail) {
      List<Concept> conceptList = allConcepts;
      List<PageSearch> searchList = pageDetail.getSearchList();
      if (ExecueCoreUtil.isCollectionNotEmpty(searchList)) {
         // TODO: -JM- Currently there will be only one search object, change later if there are multiple searches
         PageSearch search = searchList.get(0);
         // check for the search info
         if (PageSearchType.STARTS_WITH == search.getType()) {
            conceptList = new ArrayList<Concept>();
            for (Concept concept : allConcepts) {
               // TODO: -JM- use the field from the search object
               String cDispName = concept.getDisplayName();
               if (cDispName.toLowerCase().startsWith(search.getString().toLowerCase())) {
                  conceptList.add(concept);
               }
            }
         } else if (PageSearchType.CONTAINS == search.getType()) {
            conceptList = new ArrayList<Concept>();
            for (Concept concept : allConcepts) {
               String cDispName = concept.getDisplayName();
               if (cDispName.toLowerCase().contains(search.getString().toLowerCase())) {
                  conceptList.add(concept);
               }
            }
         }
      }
      // modify the page object with the new record count which will modify the page count as well
      pageDetail.setRecordCount(Long.valueOf(conceptList.size()));
      List<Concept> pageConcepts = new ArrayList<Concept>();
      // manipulate the list to return the set of concepts belonging to the page requested
      int start = (pageDetail.getRequestedPage().intValue() - 1) * pageDetail.getPageSize().intValue();
      int end = start + pageDetail.getPageSize().intValue();
      if (end > pageDetail.getRecordCount().intValue()) {
         end = (pageDetail.getRecordCount().intValue());
      }
      for (int i = start; i < end; i++) {
         pageConcepts.add(conceptList.get(i));
      }
      return pageConcepts;
   }

   public List<Concept> getConcepts () {
      return concepts;
   }

   public void setConcepts (List<Concept> concepts) {
      this.concepts = concepts;
   }

   public Long getAppModelId () {
      return appModelId;
   }

   public void setAppModelId (Long appModelId) {
      this.appModelId = appModelId;
   }

}
