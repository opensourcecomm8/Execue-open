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

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.QueryResult;
import com.execue.core.common.bean.UserInput;
import com.execue.core.common.bean.nlp.CandidateEntity;
import com.execue.core.common.bean.nlp.RecognitionEntityType;
import com.execue.core.exception.ExeCueException;
import com.execue.core.exception.HandlerException;
import com.execue.core.exception.HandlerResponseTransformException;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.driver.semantic.ISemanticDriver;
import com.execue.handler.BaseHandler;
import com.execue.handler.bean.Response;
import com.execue.handler.bean.UICandidateEntity;
import com.execue.handler.bean.UIUserInput;

/**
 * @author Nihar
 */
public class SemanticSearchHandler extends BaseHandler {

   private static final Logger log = Logger.getLogger(SemanticSearchHandler.class);

   private ISemanticDriver     driver;

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.handler.BaseHandler#process(java.lang.Object)
    */
   @Override
   public Object process (Object inputBean) throws HandlerException {
      try {
         return driver.process(inputBean);
      } catch (ExeCueException e) {
         throw new HandlerException(e.getCode(), e.getMessage(), e);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.handler.BaseHandler#processRequest(java.lang.Object)
    */
   // @Override
   @SuppressWarnings ("unchecked")
   @Override
   public Response processRequest (Object request) throws HandlerException {
      Response response = new Response();
      try {
         UIUserInput userRequest = (UIUserInput) request;
         UserInput userInput = generateUserInput(userRequest);
         Object outputBean = process(userInput);
         Object responseObject = getHandlerResponse().transformResponse(outputBean);
         response.setObject(responseObject);
      } catch (HandlerResponseTransformException rte) {
         throw new HandlerException(rte.getCode(), rte.getCause());
      }
      return response;
   }

   private UserInput generateUserInput (UIUserInput userRequest) {
      UserInput userInput = new UserInput();
      userInput.setAssetId(userRequest.getAssetId());
      userInput.setBusinessQueryId(userRequest.getBusinessQueryId());
      userInput.setRequest(userRequest.getRequest());
      userInput.setPageSize(userRequest.getPageSize());
      userInput.setRequestedPage(userRequest.getRequestedPage());
      userInput.setUserQueryId(userRequest.getUserQueryId());
      userInput.setDisableQueryCache(userRequest.isDisableQueryCache());
      userInput.setSearchFilter(userRequest.getSearchFilter());
      userInput.setUserRemoteLocationInfo(userRequest.getUserRemoteLocationInfo());
      if (ExecueCoreUtil.isCollectionNotEmpty(userRequest.getEntities())) {
         userInput.setEntities(getCandidateEntities(userRequest.getEntities()));
      }
      if (getUserContext().getUser() != null) {
         userInput.setUserId(getUserContext().getUser().getId());
      }
      return userInput;
   }

   public Response fetchResponse (Long userQueryId, Long businessQueryId, Long assetId) throws HandlerException {
      Response response = new Response();
      try {
         QueryResult queryResult = driver.getCachedQueryDataResult(userQueryId, businessQueryId, assetId);
         response.setObject(queryResult);
      } catch (ExeCueException exeCueException) {
         throw new HandlerException(exeCueException.getCode(), "Error from Driver", exeCueException);
      }
      return response;
   }

   public Response fetchResponse (Long aggregateQueryId) throws HandlerException {
      Response response = new Response();
      try {
         QueryResult queryResult = driver.getCachedQueryDataResult(aggregateQueryId);
         response.setObject(queryResult);
      } catch (ExeCueException exeCueException) {
         throw new HandlerException(exeCueException.getCode(), "Error from Driver", exeCueException);
      }
      return response;
   }

   private List<CandidateEntity> getCandidateEntities (List<UICandidateEntity> entities) {
      List<CandidateEntity> candidateEnties = new ArrayList<CandidateEntity>();
      for (UICandidateEntity uiCandidateEntity : entities) {
         CandidateEntity candidateEntity = new CandidateEntity();
         candidateEntity.setId(uiCandidateEntity.getId());

         candidateEntity.setName(uiCandidateEntity.getName());
         if (log.isDebugEnabled()) {
            log.debug("id::" + uiCandidateEntity.getId());
            log.debug("name::" + uiCandidateEntity.getName());
         }
         // TODO:- JT- currently put entity type as SFL_ENTITY, will remove in next visit
         candidateEntity.setType(RecognitionEntityType.SFL_ENTITY);
         candidateEntity.setWords(uiCandidateEntity.getWords());
         candidateEnties.add(candidateEntity);
      }
      return candidateEnties;
   }

   /**
    * @return the driver
    */
   public ISemanticDriver getDriver () {
      return driver;
   }

   /**
    * @param driver
    *           the driver to set
    */
   public void setDriver (ISemanticDriver driver) {
      this.driver = driver;
   }
}
