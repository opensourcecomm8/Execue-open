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


package com.execue.web.core.action.qi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.interceptor.SessionAware;

import com.execue.core.common.bean.AssetResult;
import com.execue.core.common.bean.Pagination;
import com.execue.core.common.bean.PossibilityResult;
import com.execue.core.common.bean.QueryResult;
import com.execue.core.common.util.ExeCueXMLUtils;
import com.execue.core.configuration.ICoreConfigurationService;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.core.exception.HandlerRequestTransformException;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.handler.IRequestResponseHandler;
import com.execue.handler.bean.Response;
import com.execue.handler.bean.UIUserInput;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author kaliki
 * @since 4.0
 */
public class QueryInterfaceSearchAction extends ActionSupport implements SessionAware, RequestAware {

   // XML request data
   private Long                      userQueryId;
   private Long                      businessQueryId;
   private Long                      assetId;
   private String                    aggregateQueryId;
   private String                    request;
   private IRequestResponseHandler   queryInterfaceHandler;
   private QueryResult               result;
   private Logger                    logger                = Logger.getLogger(QueryInterfaceSearchAction.class);
   private static final String       ERR                   = "error.";
   private String                    qiJSONString;
   private Map                       session;
   private ICoreConfigurationService coreConfigurationService;
   private String                    type;
   private String                    requestedPage;
   private String                    resultsPerPage;
   private Map                       httpRequest;
   private int                       startIndex;
   private Long                      applicationId;
   private static final String       SINGLE_ASSET_REDIRECT = "singleAssetRedirection";

   public String getRequestedPage () {
      return requestedPage;
   }

   public void setRequestedPage (String requestedPage) {
      this.requestedPage = requestedPage;
   }

   public String getRequest () {
      return request;
   }

   public void setRequest (String request) {
      this.request = request;
   }

   public QueryResult getResult () {
      return result;
   }

   public IRequestResponseHandler getQueryInterfaceHandler () {
      return queryInterfaceHandler;
   }

   public void setQueryInterfaceHandler (IRequestResponseHandler queryInterfaceHandler) {
      this.queryInterfaceHandler = queryInterfaceHandler;
   }

   public String search () {
      String redirect = "success";
      try {
         if (logger.isDebugEnabled()) {
            logger.debug("Request " + request);
            logger.debug("type " + type);
         }
         httpRequest.put("RESULTS_PAGINATION_FLAG", "simpleReport");
         Response response = null;
         if (session.containsKey("USERPAGESIZE")) {
            if (resultsPerPage != null && !resultsPerPage.equals(session.containsKey("USERPAGESIZE"))) {
               session.put("USERPAGESIZE", resultsPerPage);
            } else {
               resultsPerPage = (String) session.get("USERPAGESIZE");
            }
         }
         // if (userQueryId != null && businessQueryId != null && assetId != null) {
         if (aggregateQueryId != null) {
            // response = queryInterfaceHandler.fetchResponse(userQueryId, businessQueryId, assetId);
            long aggQueryId = 0;
            if (aggregateQueryId.contains(",")) {
               String[] stringList = aggregateQueryId.split(",");
               for (String item : stringList)
                  aggQueryId = Long.parseLong(item);
            } else {
               aggQueryId = Long.parseLong(aggregateQueryId);
            }
            response = queryInterfaceHandler.fetchResponse(aggQueryId);
         } else {
            UIUserInput userInput = new UIUserInput();
            userInput.setRequest(request);
            userInput.setPageSize(resultsPerPage);
            userInput.setRequestedPage(getRequestedPage());
            response = queryInterfaceHandler.processRequest(userInput);
         }
         result = (QueryResult) response.getObject();
         setUserQueryIntoSession();
         setApplicationIdForAdvanceSearch();
         preparePaginationInfo(result);
         if (ExecueCoreUtil.isCollectionNotEmpty(result.getMessages())) {
            // get the messages using the keys
            List<String> messages = new ArrayList<String>();
            for (String code : result.getMessages()) {
               messages.add(getText(code));
            }
            result.setMessages(messages);
         }
         if (ExecueCoreUtil.isCollectionNotEmpty(result.getPossibilites())) {
            // directly getting the first element in the possibilities list
            List<AssetResult> assetResults = result.getPossibilites().get(0).getAssets();
            // Resolve the error codes into error messages
            for (AssetResult assetResult : assetResults) {
               if (assetResult.getError() != null) {
                  assetResult.setError(getText(ERR + assetResult.getError()));
               }
            }
            // redirect to third page(result page) in case of single asset result
            if (getCoreConfigurationService().isSingleAssetRedirection() && result.getPossibilites().size() == 1
                     && assetResults.size() == 1) {
               redirect = SINGLE_ASSET_REDIRECT;
            }
         }
         if (result.getError() != null) {
            // Get the error message from the properties file using the error code
            result.setError(getText(ERR + result.getError()));
            return ERROR;
         }
         if (result.getQuerySuggestions() != null) {
            logger.debug("Obtained suggestions : returning the input : INPUT : " + INPUT);
            return INPUT;
         }

      } catch (Exception exception) {
         try {
            setUserQueryIntoSession();
            setApplicationId(ExeCueXMLUtils.parseXML(request).getApplicationId());
         } catch (HandlerRequestTransformException handRequestTransformException) {
            handRequestTransformException.printStackTrace();
            logger.error("Error in the QISearchAction During xml transformation: "
                     + handRequestTransformException.getCause());
         }
         exception.printStackTrace();
         logger.error("Error in the QISearchAction : " + exception.getCause());
         addActionError(getText(ERR + Integer.toString(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE)));
         return ERROR;
      }
      return redirect;
   }

   private void setUserQueryIntoSession () {
      if (getSession() != null) {
         session.put(getCoreConfigurationService().getXMLRequest(), applicationId + "###" + request);
      }

   }

   private void setApplicationIdForAdvanceSearch () {
      if (result != null && ExecueCoreUtil.isCollectionNotEmpty(result.getPossibilites()) && "QI".equals(type)) {
         PossibilityResult possibilityResult = result.getPossibilites().get(0);
         setApplicationId(possibilityResult.getAppId());
      }
   }

   private void preparePaginationInfo (QueryResult result) {
      String baseURL = "querySearch.action?request=" + request;
      String pageCount = Integer.toString(result.getPageCount());
      String requestedPage = Integer.toString(result.getRequestedPage());
      Pagination pagination = new Pagination();

      logger.info("setting values for the first time");
      if (requestedPage == null || requestedPage == "") {
         requestedPage = "1";
         logger.info("requestedPage " + requestedPage);
      }
      if (resultsPerPage == null || resultsPerPage == "") {
         resultsPerPage = Integer.toString(getCoreConfigurationService().getResultsPageSize());
         logger.info("resultsperpage " + resultsPerPage);
      }

      startIndex = ((Integer.parseInt(requestedPage) - 1) * Integer.parseInt(resultsPerPage)) + 1;
      if (httpRequest == null) {
         httpRequest = new HashMap();
      }

      // pagination.setResultsPerPage(resultsPerPage);
      pagination.setBaseURL(baseURL);
      pagination.setPageCount(pageCount);
      pagination.setRequestedPage(requestedPage);
      session.put("USERPAGESIZE", resultsPerPage);
      httpRequest.put("PAGINATION", pagination);
   }

   public String getQiJSONString () {
      return qiJSONString;
   }

   public void setQiJSONString (String qiJSONString) {
      this.qiJSONString = qiJSONString;
   }

   public Map getSession () {
      return session;
   }

   public void setSession (Map session) {
      this.session = session;
   }

   /**
    * @return the coreConfigurationService
    */
   public ICoreConfigurationService getCoreConfigurationService () {
      return coreConfigurationService;
   }

   /**
    * @param coreConfigurationService the coreConfigurationService to set
    */
   public void setCoreConfigurationService (ICoreConfigurationService coreConfigurationService) {
      this.coreConfigurationService = coreConfigurationService;
   }

   public String getType () {
      return type;
   }

   public void setType (String type) {
      this.type = type;
   }

   public Long getUserQueryId () {
      return userQueryId;
   }

   public void setUserQueryId (Long userQueryId) {
      this.userQueryId = userQueryId;
   }

   public Long getBusinessQueryId () {
      return businessQueryId;
   }

   public void setBusinessQueryId (Long businessQueryId) {
      this.businessQueryId = businessQueryId;
   }

   public Long getAssetId () {
      return assetId;
   }

   public void setAssetId (Long assetId) {
      this.assetId = assetId;
   }

   public void setRequest (Map httpRequest) {
      this.httpRequest = httpRequest;
   }

   public String getResultsPerPage () {
      return resultsPerPage;
   }

   public void setResultsPerPage (String resultsPerPage) {
      this.resultsPerPage = resultsPerPage;
   }

   public int getStartIndex () {
      return startIndex;
   }

   public void setStartIndex (int startIndex) {
      this.startIndex = startIndex;
   }

   public String getAggregateQueryId () {
      return aggregateQueryId;
   }

   public void setAggregateQueryId (String aggregateQueryId) {
      this.aggregateQueryId = aggregateQueryId;
   }

   /**
    * @return the applicationId
    */
   public Long getApplicationId () {
      return applicationId;
   }

   /**
    * @param applicationId the applicationId to set
    */
   public void setApplicationId (Long applicationId) {
      this.applicationId = applicationId;
   }

}
