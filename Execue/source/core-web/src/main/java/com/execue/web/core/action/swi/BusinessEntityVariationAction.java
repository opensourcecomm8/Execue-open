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
import com.execue.handler.swi.IKDXServiceHandler;
import com.execue.handler.swi.IPreferencesServiceHandler;

public class BusinessEntityVariationAction extends SWIPaginationAction {

   private static final long          serialVersionUID       = 1L;
   Logger                             log                    = Logger.getLogger(BusinessEntityVariationAction.class);
   private static final int           PAGE_SIZE              = 9;
   private static final int           NUMBER_OF_LINKS        = 4;
   private List<BusinessEntityInfo>   businessTerms;
   private BusinessEntityTermType     businessEntityTermType = BusinessEntityTermType.CONCEPT;
   private String                     entityName;
   private IPreferencesServiceHandler preferencesServiceHandler;
   private IKDXServiceHandler         kdxServiceHandler;
   private List<String>               variations;
   private Long                       entityBedId;

   // Action Methods

   @Override
   public String processPage () throws ExeCueException {
      try {
         getPageDetail().setPageSize(Long.valueOf(PAGE_SIZE));
         getPageDetail().setNumberOfLinks(Long.valueOf(NUMBER_OF_LINKS));
         businessTerms = getPreferencesServiceHandler().getAllBusinessTermEntities(
                  getApplicationContext().getModelId(), businessEntityTermType, getPageDetail());
         // push the page object into request
         if (log.isDebugEnabled()) {
            log.debug(getPageDetail().toString());
         }
         getHttpRequest().put(PAGINATION, getPageDetail());
      } catch (Exception exception) {
         addActionError("Error: " + exception);
         log.error(exception, exception);
         throw new ExeCueException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, exception);
      }

      return SUCCESS;
   }

   public String getBusinessEntityVariation () {
      try {
         setVariations(getKdxServiceHandler().getBusinessEntityVariations(entityBedId));
      } catch (ExeCueException exception) {
         addActionError("Error: " + exception);
         log.error(exception, exception);
      }
      return SUCCESS;
   }

   public String saveVariations () {
      try {
         getKdxServiceHandler().saveBusinessEntityVariations(getApplicationContext().getModelId(), entityBedId,
                  variations);
         addActionMessage(getText("execue.entityVariations.saved.successful"));

      } catch (ExeCueException exception) {
         addActionError(getText("execue.entityVariations.saved.failed"));
         log.error(exception, exception);
      }
      return SUCCESS;
   }

   public List<BusinessEntityTermType> getBusinessEntityTermTypes () {
      return Arrays.asList(BusinessEntityTermType.values());

   }

   /**
    * @return the businessTerms
    */
   public List<BusinessEntityInfo> getBusinessTerms () {
      return businessTerms;
   }

   /**
    * @param businessTerms
    *           the businessTerms to set
    */
   public void setBusinessTerms (List<BusinessEntityInfo> businessTerms) {
      this.businessTerms = businessTerms;
   }

   /**
    * @return the businessEntityTermType
    */
   public BusinessEntityTermType getBusinessEntityTermType () {
      return businessEntityTermType;
   }

   /**
    * @param businessEntityTermType
    *           the businessEntityTermType to set
    */
   public void setBusinessEntityTermType (BusinessEntityTermType businessEntityTermType) {
      this.businessEntityTermType = businessEntityTermType;
   }

   /**
    * @return the preferencesServiceHandler
    */
   public IPreferencesServiceHandler getPreferencesServiceHandler () {
      return preferencesServiceHandler;
   }

   /**
    * @param preferencesServiceHandler
    *           the preferencesServiceHandler to set
    */
   public void setPreferencesServiceHandler (IPreferencesServiceHandler preferencesServiceHandler) {
      this.preferencesServiceHandler = preferencesServiceHandler;
   }

   /**
    * @return the kdxServiceHandler
    */
   public IKDXServiceHandler getKdxServiceHandler () {
      return kdxServiceHandler;
   }

   /**
    * @param kdxServiceHandler
    *           the kdxServiceHandler to set
    */
   public void setKdxServiceHandler (IKDXServiceHandler kdxServiceHandler) {
      this.kdxServiceHandler = kdxServiceHandler;
   }

   public String getEntityName () {
      return entityName;
   }

   public void setEntityName (String entityName) {
      this.entityName = entityName;
   }

   public Long getEntityBedId () {
      return entityBedId;
   }

   public void setEntityBedId (Long entityBedId) {
      this.entityBedId = entityBedId;
   }

   /**
    * @return the variations
    */
   public List<String> getVariations () {
      return variations;
   }

   /**
    * @param variations the variations to set
    */
   public void setVariations (List<String> variations) {
      this.variations = variations;
   }

}
