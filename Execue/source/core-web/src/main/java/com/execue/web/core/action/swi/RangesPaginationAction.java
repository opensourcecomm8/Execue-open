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
 * @author john
 */
public class RangesPaginationAction extends SWIPaginationAction {

   private List<Concept>    concepts;
   private static Logger    logger          = Logger.getLogger(RangesPaginationAction.class);
   private static final int PAGE_SIZE       = 9;
   private static final int NUMBER_OF_LINKS = 4;

   public List<Concept> getConcepts () {
      return concepts;
   }

   public void setConcepts (List<Concept> concepts) {
      this.concepts = concepts;
   }

   @SuppressWarnings ("unchecked")
   @Override
   public String processPage () throws ExeCueException {
      try {
         getPageDetail().setPageSize(Long.valueOf(PAGE_SIZE));
         getPageDetail().setNumberOfLinks(Long.valueOf(NUMBER_OF_LINKS));
         List<Concept> rangeConcepts = getPreferencesServiceHandler().getConceptsForRanges(
                  getApplicationContext().getModelId());
         // set the record count
         getPageDetail().setRecordCount(Long.valueOf(rangeConcepts.size()));
         concepts = getProcessedResults(rangeConcepts, getPageDetail());
         // push the page object into request
         if (logger.isDebugEnabled()) {
            logger.debug(getPageDetail().toString());
         }
         getHttpRequest().put(PAGINATION, getPageDetail());
         logger.debug("Concepts for Ranges size is " + concepts.size());
      } catch (Exception exception) {
         logger.error(exception, exception);
         throw new ExeCueException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, exception);
      }
      return SUCCESS;
   }

   // TODO: enhance the logic to handle search operation
   private List<Concept> getProcessedResults (List<Concept> rangeConcepts, Page pageDetail) {
      List<Concept> conceptList = rangeConcepts;
      List<PageSearch> searchList = pageDetail.getSearchList();
      if (ExecueCoreUtil.isCollectionNotEmpty(searchList)) {
         // TODO: -JM- Currently there will be only one search object, change later if there are multiple searches
         PageSearch search = searchList.get(0);
         // check for the search info
         if (PageSearchType.STARTS_WITH == search.getType()) {
            conceptList = new ArrayList<Concept>();
            for (Concept concept : rangeConcepts) {
               // TODO: -JM- use the field from the search object
               String cDispName = concept.getDisplayName();
               if (cDispName.toLowerCase().startsWith(search.getString().toLowerCase())) {
                  conceptList.add(concept);
               }
            }
         } else if (PageSearchType.CONTAINS == search.getType()) {
            conceptList = new ArrayList<Concept>();
            for (Concept concept : rangeConcepts) {
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
}
