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

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.governor.BusinessEntityInfo;
import com.execue.core.common.type.BusinessEntityTermType;
import com.execue.core.exception.ExeCueException;
import com.execue.core.exception.ExeCueExceptionCodes;

/**
 * @author John Mallavalli
 */
public class ParallelWordsPaginationAction extends SWIPaginationAction {

   private static Logger            logger                 = Logger.getLogger(ConceptsPaginationAction.class);
   private static final int         PAGE_SIZE              = 9;
   private static final int         NUMBER_OF_LINKS        = 4;
   private List<BusinessEntityInfo> businessTerms;
   private BusinessEntityTermType   businessEntityTermType = BusinessEntityTermType.CONCEPT;

   // This is Memory Pagination type - get all the concepts and persist in the session
   @SuppressWarnings ("unchecked")
   @Override
   public String processPage () throws ExeCueException {
      try {
         getPageDetail().setPageSize(Long.valueOf(PAGE_SIZE));
         getPageDetail().setNumberOfLinks(Long.valueOf(NUMBER_OF_LINKS));
         businessTerms = getPreferencesServiceHandler().getAllBusinessTermEntities(
                  getApplicationContext().getModelId(), businessEntityTermType, getPageDetail());
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

   public List<BusinessEntityTermType> getBusinessEntityTermTypes () {
      return Arrays.asList(BusinessEntityTermType.values());

   }

   public List<BusinessEntityInfo> getBusinessTerms () {
      return businessTerms;
   }

   public void setBusinessTerms (List<BusinessEntityInfo> businessTerms) {
      this.businessTerms = businessTerms;
   }

   public BusinessEntityTermType getBusinessEntityTermType () {
      return businessEntityTermType;
   }

   public void setBusinessEntityTermType (BusinessEntityTermType businessEntityTermType) {
      this.businessEntityTermType = businessEntityTermType;
   }
}
