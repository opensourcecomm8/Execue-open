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


/**
 *
 */
package com.execue.handler.sementic;

import java.util.List;

import com.execue.core.common.bean.SearchFilter;
import com.execue.core.common.bean.SemanticSuggestTerm;
import com.execue.core.common.bean.security.User;
import com.execue.core.configuration.ICoreConfigurationService;
import com.execue.core.exception.HandlerException;
import com.execue.handler.qi.ISemanticSuggestHandler;
import com.execue.platform.ISemanticSuggestService;
import com.execue.platform.exception.PlatformException;
import com.execue.security.UserContextService;

/**
 * @author John Mallavalli
 */
public class SemanticSuggestHandler extends UserContextService implements ISemanticSuggestHandler {

   private ISemanticSuggestService   semanticSuggestService;
   private ICoreConfigurationService coreConfigurationService;

   public ISemanticSuggestService getSemanticSuggestService () {
      return semanticSuggestService;
   }

   public void setSemanticSuggestService (ISemanticSuggestService semanticSuggestService) {
      this.semanticSuggestService = semanticSuggestService;
   }

   public List<SemanticSuggestTerm> getSuggestTerms (String searchString, SearchFilter searchFilter)
            throws HandlerException {
      try {
         Long userId = null;
         if (getUserContext().getUser() != null && getUserContext().getUser().getId() > 0) {
            userId = getUserContext().getUser().getId();
         }
         return semanticSuggestService.getSuggestTerms(searchString, searchFilter, userId);
      } catch (PlatformException e) {
         throw new HandlerException(e.getCode(), e);
      }
   }

   /**
    * @return the coreConfigurationService
    */
   public ICoreConfigurationService getCoreConfigurationService () {
      return coreConfigurationService;
   }

   /**
    * @param coreConfigurationService
    *           the coreConfigurationService to set
    */
   public void setCoreConfigurationService (ICoreConfigurationService coreConfigurationService) {
      this.coreConfigurationService = coreConfigurationService;
   }

}
