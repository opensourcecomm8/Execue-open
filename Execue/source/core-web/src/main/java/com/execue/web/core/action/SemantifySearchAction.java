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


package com.execue.web.core.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.interceptor.SessionAware;

import com.execue.core.common.bean.AssetResult;
import com.execue.core.common.bean.Pagination;
import com.execue.core.common.bean.PossibilityResult;
import com.execue.core.common.bean.QueryResult;
import com.execue.core.common.bean.SearchFilter;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.nlp.CandidateEntity;
import com.execue.core.common.bean.nlp.TokenCandidate;
import com.execue.core.common.bean.security.UserRemoteLocationInfo;
import com.execue.core.common.type.AppSourceType;
import com.execue.core.common.type.SearchFilterType;
import com.execue.core.configuration.ExecueConfiguration;
import com.execue.core.configuration.ICoreConfigurationService;
import com.execue.core.exception.HandlerException;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.handler.IRequestResponseHandler;
import com.execue.handler.bean.Response;
import com.execue.handler.bean.UICandidateEntity;
import com.execue.handler.bean.UIUserInput;
import com.execue.handler.swi.ISDXServiceHandler;
import com.execue.handler.util.RemoteLocationRetrievalUtil;
import com.execue.web.core.exception.WebExceptionCodes;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author Nihar
 */
public class SemantifySearchAction extends ActionSupport implements RequestAware, SessionAware {

   // XML request data
   private Long                      userQueryId;
   private Long                      businessQueryId;
   private Long                      assetId;
   private String                    agQueryIdList;
   private String                    request;
   private IRequestResponseHandler   semanticSearchHandler;
   private ISDXServiceHandler        sdxServiceHandler;
   private QueryResult               result;
   private Logger                    logger                         = Logger.getLogger(SemantifySearchAction.class);
   private static final String       ERR                            = "error.";
   private String                    qiJSONString;
   private ExecueConfiguration       requestXmlConfiguration;
   private String                    type;
   private List<UICandidateEntity>   entities;
   private UIUserInput               userInput;
   private String                    requestedPage;
   private Map                       httpRequest;
   private Map                       httpSession;
   private String                    resultsPerPage;
   // for numbering each result
   private int                       startIndex;
   // for disabling the query cache
   private boolean                   disableCache                   = false;
   private ICoreConfigurationService coreConfigurationService;
   private SearchFilter              searchFilter;
   private String                    redirectURL;
   private Long                      verticalId                     = -1L;
   // need to send for app specific search
   private Long                      applicationId                  = -1L;
   private String                    appNameForURL;                                                                 //
   private static final String       VERTICAL_ENTITY_BASED_REDIRECT = "verticalEntityBasedRedirect";
   private static final String       SINGLE_APP_REDIRECT            = "singleAppRedirection";
   private static final String       SINGLE_ASSET_REDIRECT          = "singleAssetRedirection";

   public String getResultsPerPage () {
      return resultsPerPage;
   }

   public void setResultsPerPage (String resultsPerPage) {
      this.resultsPerPage = resultsPerPage;
   }

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

   public String search () {
      String redirect = "success";
      try {
         if (logger.isDebugEnabled()) {
            logger.debug("Request " + request);
         }
         httpRequest.put("RESULTS_PAGINATION_FLAG", "simpleReport");
         UIUserInput userInput = new UIUserInput();
         searchFilter = new SearchFilter();
         searchFilter.setSearchFilterType(SearchFilterType.GENERAL);
         if (verticalId != null && verticalId != -1) {
            searchFilter.setId(verticalId);
            searchFilter.setSearchFilterType(SearchFilterType.VERTICAL);
         } else if (applicationId != null && applicationId != -1) {
            searchFilter.setId(applicationId);
            searchFilter.setSearchFilterType(SearchFilterType.APP);
         }
         userInput.setSearchFilter(searchFilter);

         Response response = null;
         logger.info("resultsPerPage " + resultsPerPage);
         logger.info("request " + request);

         if (httpSession.containsKey("USERPAGESIZE")) {
            if (resultsPerPage != null && !resultsPerPage.equals(httpSession.containsKey("USERPAGESIZE"))) {
               httpSession.put("USERPAGESIZE", resultsPerPage);
            } else {
               resultsPerPage = (String) httpSession.get("USERPAGESIZE");
            }
         }

         // Populate the user remote location info
         UserRemoteLocationInfo userRemoteLocationInfo = RemoteLocationRetrievalUtil.populateUserRemoteLocationInfo(
                  ServletActionContext.getRequest(), getHttpSession(), getCoreConfigurationService()
                           .getUserRemoteLocationUrl(), getCoreConfigurationService()
                           .getUserRemoteLocationConnectTimeout(), getCoreConfigurationService()
                           .getUserRemoteLocationReadTimeout());
         userInput.setUserRemoteLocationInfo(userRemoteLocationInfo);

         // if (userQueryId != null && businessQueryId != null && assetId != null) {
         if (agQueryIdList != null) {
            userInput.setUserQueryId(getUserQueryId());
            userInput.setBusinessQueryId(getBusinessQueryId());
            userInput.setAssetId(getAssetId());
            userInput.setPageSize(resultsPerPage);
            // TODO: -JVK- change the method to accept UserInput instead of the three params
            // response = semanticSearchHandler.fetchResponse(userInput);
            // response = semanticSearchHandler.fetchResponse(userQueryId, businessQueryId, assetId);
            long aggQueryId = 0;
            if (agQueryIdList.contains(",")) {
               String[] stringList = agQueryIdList.split(",");
               for (String item : stringList) {
                  aggQueryId = Long.parseLong(item);
               }
            } else {
               aggQueryId = Long.parseLong(agQueryIdList);
            }
            response = semanticSearchHandler.fetchResponse(aggQueryId);
         } else { // This is the normal route
            userInput.setRequest(request);
            userInput.setPageSize(resultsPerPage);
            userInput.setRequestedPage(requestedPage);
            // set the disable query cache flag
            userInput.setDisableQueryCache(getDisableCache());
            response = semanticSearchHandler.processRequest(userInput);
         }
         result = (QueryResult) response.getObject();

         preparePaginationInfo(result);

         if (result != null) {
            if (ExecueCoreUtil.isCollectionNotEmpty(result.getTokenCandedates())) {
               for (TokenCandidate tokenCandidate : result.getTokenCandedates()) {
                  for (CandidateEntity candidateEntity : tokenCandidate.getEntities()) {
                     logger.info("candidate entity name:" + candidateEntity.getName());
                     logger.info("candidate entity type:" + candidateEntity.getType());
                     logger.info("candidate entity id:" + candidateEntity.getId());
                  }
               }
            }
         }

         if (ExecueCoreUtil.isCollectionNotEmpty(result.getMessages())) {
            // get the messages using the keys
            List<String> messages = new ArrayList<String>();
            for (String code : result.getMessages()) {
               messages.add(getText(code));
            }
            result.setMessages(messages);
         }
         // populate error message.
         for (PossibilityResult possibilityResult : result.getPossibilites()) {
            if (possibilityResult.getApplicationType() == AppSourceType.UNSTRUCTURED) {
               continue;
            }
            List<AssetResult> assetResults = possibilityResult.getAssets();
            // Resolve the error codes into error messages
            for (AssetResult assetResult : assetResults) {
               if (assetResult.getError() != null) {
                  assetResult.setError(getText(ERR + assetResult.getError()));
               }
               Asset asset = sdxServiceHandler.getAssetPopulatedByApplicationOwner(assetResult.getAssetId());
               assetResult.setAppOwnerId(asset.getApplication().getOwner().getId());
               assetResult.setAppOwnerName(asset.getApplication().getOwner().getFirstName());
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
         if (result.isRedirectURLPresent()) {
            redirect = SINGLE_APP_REDIRECT;
            redirectURL = result.getRedirectURL();
         } else if (getCoreConfigurationService().isSingleAssetRedirection()
                  && ExecueCoreUtil.isCollectionNotEmpty(result.getPossibilites())
                  && result.getPossibilites().size() == 1 && result.getPossibilites().get(0).getAssets().size() == 1) {
            redirect = SINGLE_ASSET_REDIRECT;
         }
         // if (result != null && ExecueCoreUtil.isCollectionNotEmpty(result.getVerticalBusinessEntityIds())) {
         // redirect = VERTICAL_ENTITY_BASED_REDIRECT;
         // redirectURL = result.getVerticalEntityBasedRedirection().getRedirectUrl();
         // } else if (SearchFilterType.VERTICAL.equals(searchFilter.getSearchFilterType())) {
         // redirect = searchFilter.getName();
         // }
      } catch (HandlerException handlerException) {
         handlerException.printStackTrace();
         logger.error("HandlerException in the SemantifySearchAction : " + handlerException.getCause());
         addActionError(getText(ERR + Integer.toString(handlerException.getCode())));
         return ERROR;
      } catch (Exception e) {
         e.printStackTrace();
         logger.error("Exception in the SemantifySearchAction : " + e.getCause());
         addActionError(getText(ERR + Integer.toString(WebExceptionCodes.WEB_DEFAULT_EXCEPTION_CODE)));
         return ERROR;
      } catch (Error e) {
         e.printStackTrace();
         logger.error("Error in the SemantifySearchAction : " + e.getCause());
         addActionError(getText(ERR + Integer.toString(WebExceptionCodes.WEB_SYSTEM_ERROR_CODE)));
         return ERROR;
      }
      logger.info("EXITING SEMANTIFI SEARCH ACTION...........");
      return redirect;
   }

   private void preparePaginationInfo (QueryResult result) {
      String baseURL = "semanticSearch.action?request=" + request + "&type=" + type;
      String pageCount = Integer.toString(result.getPageCount());

      logger.info("Page Count from Action " + pageCount + " Total no of results " + result.getPageSize());
      String requestedPage = Integer.toString(result.getRequestedPage());
      Pagination pagination = new Pagination();

      logger.info("setting values for the first time");
      if (requestedPage == null || requestedPage == "") {
         requestedPage = "1";
         logger.info("requestedPage " + requestedPage);
      }
      logger.info("resultsperpage session " + resultsPerPage);
      if (resultsPerPage == null || resultsPerPage == "") {
         resultsPerPage = Integer.toString(getCoreConfigurationService().getResultsPageSize());
         logger.info("resultsperpage " + resultsPerPage);
      }

      startIndex = (Integer.parseInt(requestedPage) - 1) * Integer.parseInt(resultsPerPage) + 1;
      int links = Integer.parseInt(pageCount) / Integer.parseInt(resultsPerPage);
      int rmndr = Integer.parseInt(pageCount) % Integer.parseInt(resultsPerPage);
      if (rmndr != 0) {
         links++;
      }
      if (Integer.parseInt(requestedPage) > links) {
         requestedPage = Integer.toString(links);
         startIndex = (Integer.parseInt(requestedPage) - 1) * Integer.parseInt(resultsPerPage) + 1;
      }
      pagination.setBaseURL(baseURL);
      pagination.setPageCount(pageCount);
      pagination.setRequestedPage(requestedPage);
      // pagination.setResultsPerPage(resultsPerPage);
      httpRequest.put("PAGINATION", pagination);
      // pagination.setResultsPerPage(resultsPerPage);
      httpSession.put("USERPAGESIZE", resultsPerPage);
      // httpSession.put("PAGINATION", pagination);
   }

   public String processEntities () {
      if (userInput == null) {
         userInput = new UIUserInput();
      }
      userInput.setRequest(getRequest());
      if (httpSession.containsKey("USERPAGESIZE")) {
         if (resultsPerPage != null && !resultsPerPage.equals(httpSession.containsKey("USERPAGESIZE"))) {
            httpSession.put("USERPAGESIZE", resultsPerPage);
         } else {
            resultsPerPage = (String) httpSession.get("USERPAGESIZE");
         }
      }
      userInput.setPageSize(resultsPerPage);
      if (entities != null) {
         if (logger.isDebugEnabled()) {
            for (UICandidateEntity candidateEntity : entities) {
               if (ExecueCoreUtil.isCollectionNotEmpty(candidateEntity.getWords())) {
                  for (String word : candidateEntity.getWords()) {
                     logger.debug("word::" + word);
                  }
               }
            }
         }
         userInput.setEntities(getEntities());
         try {
            Response response = semanticSearchHandler.processRequest(userInput);
            result = (QueryResult) response.getObject();
            preparePaginationInfo(result);
         } catch (HandlerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
      }
      return SUCCESS;
   }

   public String getQiJSONString () {
      return qiJSONString;
   }

   public void setQiJSONString (String qiJSONString) {
      this.qiJSONString = qiJSONString;
   }

   public ExecueConfiguration getRequestXmlConfiguration () {
      return requestXmlConfiguration;
   }

   public void setRequestXmlConfiguration (ExecueConfiguration requestXmlConfiguration) {
      this.requestXmlConfiguration = requestXmlConfiguration;
   }

   /**
    * @return the sementicSearchHandler
    */
   public IRequestResponseHandler getSemanticSearchHandler () {
      return semanticSearchHandler;
   }

   /**
    * @param sementicSearchHandler
    *           the sementicSearchHandler to set
    */
   public void setSemanticSearchHandler (IRequestResponseHandler sementicSearchHandler) {
      this.semanticSearchHandler = sementicSearchHandler;
   }

   public String getType () {
      return type;
   }

   public void setType (String type) {
      this.type = type;
   }

   public List<UICandidateEntity> getEntities () {
      return entities;
   }

   public void setEntities (List<UICandidateEntity> entities) {
      this.entities = entities;
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

   public UIUserInput getUserInput () {
      return userInput;
   }

   public void setUserInput (UIUserInput userInput) {
      this.userInput = userInput;
   }

   public int getStartIndex () {
      return startIndex;
   }

   public void setStartIndex (int startIndex) {
      this.startIndex = startIndex;
   }

   public void setSession (Map session) {
      this.httpSession = session;
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

   public void setRequest (Map requestObj) {
      this.httpRequest = requestObj;

   }

   public Map getHttpRequest () {
      return httpRequest;
   }

   public void setHttpResponse (Map httpRequest) {
      this.httpRequest = httpRequest;
   }

   public String getAgQueryIdList () {
      return agQueryIdList;
   }

   public void setAgQueryIdList (String agQueryIdList) {
      this.agQueryIdList = agQueryIdList;
   }

   public ISDXServiceHandler getSdxServiceHandler () {
      return sdxServiceHandler;
   }

   public void setSdxServiceHandler (ISDXServiceHandler sdxServiceHandler) {
      this.sdxServiceHandler = sdxServiceHandler;
   }

   public boolean getDisableCache () {
      return disableCache;
   }

   public void setDisableCache (boolean disableCache) {
      this.disableCache = disableCache;
   }

   public SearchFilter getSearchFilter () {
      return searchFilter;
   }

   public void setSearchFilter (SearchFilter searchFilter) {
      this.searchFilter = searchFilter;
   }

   public String getRedirectURL () {
      return redirectURL;
   }

   public void setRedirectURL (String redirectURL) {
      this.redirectURL = redirectURL;
   }

   public Long getVerticalId () {
      return verticalId;
   }

   public void setVerticalId (Long verticalId) {
      this.verticalId = verticalId;
   }

   public Long getApplicationId () {
      return applicationId;
   }

   public void setApplicationId (Long applicationId) {
      this.applicationId = applicationId;
   }

   public String getAppNameForURL () {
      return appNameForURL;
   }

   public void setAppNameForURL (String appNameForURL) {
      this.appNameForURL = appNameForURL;
   }

   /**
    * @return the httpSession
    */
   public Map getHttpSession () {
      return httpSession;
   }
}
