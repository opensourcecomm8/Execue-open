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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;

import com.execue.core.common.bean.QueryForm;
import com.execue.core.common.bean.UserInput;
import com.execue.core.common.bean.entity.VerticalAppExample;
import com.execue.core.common.bean.qdata.NewsItem;
import com.execue.core.common.type.NewsCategory;
import com.execue.core.common.type.PublishAssetMode;
import com.execue.core.configuration.IConfiguration;
import com.execue.core.configuration.ICoreConfigurationService;
import com.execue.core.exception.HandlerException;
import com.execue.core.exception.HandlerRequestTransformException;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.handler.bean.UIApplicationInfo;
import com.execue.handler.bean.UIApplicationModelInfo;
import com.execue.handler.bean.UIUserInput;
import com.execue.handler.qdata.IQdataServiceHandler;
import com.execue.handler.qi.QueryInterfaceHandlerReqeust;
import com.execue.handler.swi.IApplicationServiceHandler;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ActionSupport;

public class HomeAction extends ActionSupport implements SessionAware {

   private static final long                     serialVersionUID               = 1L;
   private static final Logger                   logger                         = Logger.getLogger(HomeAction.class);

   private String                                type                           = "SI";
   private static String                         QueryInterface                 = "QI";
   private static String                         QI_SEARCH_PAGE                 = "qiSearch";
   private static String                         SEARCH_PAGE                    = "search";
   private static final Logger                   log                            = Logger.getLogger(HomeAction.class);
   private String                                qiJSONString;
   private String                                requestString;
   private QueryInterfaceHandlerReqeust          requestQueryFormTransform;
   private ICoreConfigurationService             coreConfigurationService;
   private Map                                   session;
   private IConfiguration                        configuration;
   private List<String>                          examples;
   private static final String                   SI_EXAMPLES                    = "SI_EXAMPLES";
   private static final String                   QI_EXAMPLES                    = "QI_EXAMPLES";
   private IApplicationServiceHandler            applicationServiceHandler;
   private IQdataServiceHandler                  qdataServiceHandler;
   private List<UIApplicationModelInfo>          applications;
   private List<UIApplicationInfo>               applicationList;
   private List<NewsItem>                        newsItemsList;
   private Long                                  verticalId;
   private Long                                  craigsListAppId;
   private Map<String, List<VerticalAppExample>> verticalAppExampleMap;
   private String                                isUnstructuredAppVisibleToUser = "true";
   private Long                                  applicationId;

   /**
    * This method will be used for platform landing page
    */
   @Override
   public String input () {
      String resultType = SEARCH_PAGE;
      // try {
      if (QueryInterface.equalsIgnoreCase(type)) {
         // applications = applicationServiceHandler.getAllApplicationsOrderedByName();
         if (getSession() != null) {
            String tempAppQuery = (String) session.get(getCoreConfigurationService().getXMLRequest());
            if (!StringUtils.isEmpty(tempAppQuery)) {
               Long tempAppId = Long.valueOf(tempAppQuery.split("###")[0]);
               if (applicationId == null) {
                  setApplicationId(tempAppId);
               }
               if (applicationId.longValue() == tempAppId.longValue()) {
                  requestString = tempAppQuery.split("###")[1];
               }
            }
         }
         if (logger.isDebugEnabled()) {
            logger.debug("requestString:" + requestString);
         }
         if (requestString != null) {
            getJSONData(requestString);
         }
         resultType = QI_SEARCH_PAGE;

      } else {
         // applicationList = applicationServiceHandler.getApplicationsByRank();
      }
      // } catch (HandlerException e) {
      // addActionError(getText("execue.errors.unable.process"));
      // }
      return resultType;

   }

   public String showCommunityApplicationsIncludingUserSpecificApps () {
      String resultType = SEARCH_PAGE;
      try {

         if (QueryInterface.equalsIgnoreCase(type)) {
            applications = applicationServiceHandler
                     .getAllActiveStructuredCommunityApplicationsIncludingUserApps();            
            resultType = QI_SEARCH_PAGE;
         } else {
            applicationList = applicationServiceHandler.getAllCommunityApplicationsIncludingUserApps();
         }
      } catch (HandlerException e) {
         addActionError(getText("execue.errors.unable.process"));
      }
      return resultType;

   }

   /**
    * This method will be used for Portal landing page
    */
   public String verticalInput () {
      String resultType = SEARCH_PAGE;
      String tempAppQuery = null;
      String tempApplicationId = null;
      try {
         if (QueryInterface.equals(type)) {
            applications = applicationServiceHandler.getAllApplicationsOrderedByName();
            if (getSession() != null) {
               tempAppQuery = (String) session.get(getCoreConfigurationService().getXMLRequest());
               if (!StringUtils.isEmpty(tempAppQuery)) {
                  tempApplicationId = tempAppQuery.split("###")[0];
                  if (applicationId.longValue() == Long.valueOf(tempApplicationId).longValue()) {
                     requestString = tempAppQuery.split("###")[1];
                  }
               }
               if (logger.isDebugEnabled()) {
                  logger.debug("requestString:" + requestString);
               }
            }
            if (requestString != null) {
               getJSONData(requestString);
            }
            resultType = QI_SEARCH_PAGE;
         } else {
            applicationList = applicationServiceHandler.getApplicationsByRank();
            setIsUnstructuredAppVisibleToUser(getApplicationServiceHandler().isUnstructuredAppVisibleToUser());
            if (!ExecueCoreUtil.isCollectionEmpty(applicationList)) {
               // TODO-JT- this is a temp code to get the graiglistappId will remove it later
               craigsListAppId = getCragListAppId(applicationList);
               Map<String, List<VerticalAppExample>> vAppExamples = applicationServiceHandler.getVerticalAppExamples();
               // TODO-JT-This is a hack, to show the craigslist example.Override Advertisement name to craigslist auto
               // for display purpose
               ovrrideAdvertisementVerticalAsCraigsListAuto(vAppExamples);
            }
         }

      } catch (HandlerException e) {
         addActionError(getText("execue.errors.unable.process"));
      }
      return resultType;
   }

   public String financeHome () {
      try {
         setIsUnstructuredAppVisibleToUser(getApplicationServiceHandler().isUnstructuredAppVisibleToUser());
         applicationList = applicationServiceHandler.getVerticalApplicationsByRank(verticalId);
         craigsListAppId = getCragListAppId(applicationList);
         newsItemsList = qdataServiceHandler.getNewsItemsByCategory(NewsCategory.FINANCE_NEWS);
      } catch (HandlerException e) {
         addActionError(getText("execue.errors.unable.process"));
      }
      return SEARCH_PAGE;
   }

   public String verticalHome () {
      try {
         applicationList = applicationServiceHandler.getVerticalApplicationsByRank(verticalId);
         setIsUnstructuredAppVisibleToUser(getApplicationServiceHandler().isUnstructuredAppVisibleToUser());
         craigsListAppId = getCragListAppId(applicationList);
         if (verticalId == 1002L) {
            newsItemsList = qdataServiceHandler.getNewsItemsByCategory(NewsCategory.GOVERNMENT_NEWS);
         } else if (verticalId == 1001L) {
            newsItemsList = qdataServiceHandler.getNewsItemsByCategory(NewsCategory.FINANCE_NEWS);
         } else {
            newsItemsList = qdataServiceHandler.getNewsItemsByCategory(NewsCategory.NEWS_NEWS);
         }
      } catch (HandlerException e) {
         addActionError(getText("execue.errors.unable.process"));
      }
      return SEARCH_PAGE;
   }

   public String getExample () {
      if (!QueryInterface.equalsIgnoreCase(type)) {
         examples = configuration.getList(SI_EXAMPLES);
      } else if (QueryInterface.equalsIgnoreCase(type)) {
         examples = configuration.getList(QI_EXAMPLES);
         /*
          * List<String> localListOfExamples = new ArrayList<String>(); examples = new ArrayList<String>();
          * localListOfExamples = configuration.getList(QI_EXAMPLES); for (String exampleString : localListOfExamples) {
          * String stringSet[] = new String[2]; stringSet = exampleString.split("~"); getJSONData(stringSet[1]);
          * examples.add(stringSet[0] + "~" + qiJSONString); }
          */
      }
      return SUCCESS;
   }

   private void getJSONData (String xmlReqString) {
      try {
         UIUserInput userRequest = new UIUserInput();
         userRequest.setRequest(xmlReqString);
         UserInput userInput = (UserInput) requestQueryFormTransform.transformRequest(userRequest);
         QueryForm queryForm = userInput.getQueryForm();
         qiJSONString = JSONUtil.serialize(queryForm);
      } catch (HandlerRequestTransformException e) {
         e.printStackTrace();
      } catch (JSONException e) {
         e.printStackTrace();
      }
   }

   private void ovrrideAdvertisementVerticalAsCraigsListAuto (Map<String, List<VerticalAppExample>> vAppExamples) {
      verticalAppExampleMap = new LinkedHashMap<String, List<VerticalAppExample>>();
      List<VerticalAppExample> list = vAppExamples.get("Advertisement");
      if (ExecueCoreUtil.isCollectionNotEmpty(list)) {
         vAppExamples.remove("Advertisement");
         verticalAppExampleMap.put(getCoreConfigurationService().getSkipDerivedUserQueryVariation(), list);
      }
      Set<String> keySet = vAppExamples.keySet();
      for (String string : keySet) {
         verticalAppExampleMap.put(string, vAppExamples.get(string));
      }

   }

   private Long getCragListAppId (List<UIApplicationInfo> applicationList2) {
      Long appId = null;
      for (UIApplicationInfo applicationInfo : applicationList2) {
         if (getCoreConfigurationService().getSkipDerivedUserQueryVariation().equalsIgnoreCase(
                  applicationInfo.getApplicationName())) {
            appId = applicationInfo.getApplicationId();
            break;
         }

      }
      if (appId == null) {
         // TODO : made it hardcoded will remove it later after proper solution
         appId = 1508L;
      }
      return appId;
   }

   public String getQiJSONString () {
      return qiJSONString;
   }

   public void setQiJSONString (String qiJSONString) {
      this.qiJSONString = qiJSONString;
   }

   public QueryInterfaceHandlerReqeust getRequestQueryFormTransform () {
      return requestQueryFormTransform;
   }

   public void setRequestQueryFormTransform (QueryInterfaceHandlerReqeust requestQueryFormTransform) {
      this.requestQueryFormTransform = requestQueryFormTransform;
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
    * @param coreConfigurationService
    *           the coreConfigurationService to set
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

   public String getRequestString () {
      return requestString;
   }

   public void setRequestString (String requestString) {
      this.requestString = requestString;
   }

   public IConfiguration getConfiguration () {
      return configuration;
   }

   public void setConfiguration (IConfiguration configuration) {
      this.configuration = configuration;
   }

   public List<String> getExamples () {
      return examples;
   }

   /**
    * @return the applicationServiceHandler
    */
   public IApplicationServiceHandler getApplicationServiceHandler () {
      return applicationServiceHandler;
   }

   /**
    * @param applicationServiceHandler
    *           the applicationServiceHandler to set
    */
   public void setApplicationServiceHandler (IApplicationServiceHandler applicationServiceHandler) {
      this.applicationServiceHandler = applicationServiceHandler;
   }

   /**
    * @return the applications
    */
   public List<UIApplicationModelInfo> getApplications () {
      return applications;
   }

   /**
    * @param applications
    *           the applications to set
    */
   public void setApplications (List<UIApplicationModelInfo> applications) {
      this.applications = applications;
   }

   public List<UIApplicationInfo> getApplicationList () {
      return applicationList;
   }

   public void setApplicationList (List<UIApplicationInfo> applicationList) {
      this.applicationList = applicationList;
   }

   public Long getVerticalId () {
      return verticalId;
   }

   public void setVerticalId (Long verticalId) {
      this.verticalId = verticalId;
   }

   public IQdataServiceHandler getQdataServiceHandler () {
      return qdataServiceHandler;
   }

   public void setQdataServiceHandler (IQdataServiceHandler qdataServiceHandler) {
      this.qdataServiceHandler = qdataServiceHandler;
   }

   public List<NewsItem> getNewsItemsList () {
      return newsItemsList;
   }

   public void setNewsItemsList (List<NewsItem> newsItemsList) {
      this.newsItemsList = newsItemsList;
   }

   public Map<String, List<VerticalAppExample>> getVerticalAppExampleMap () {
      return verticalAppExampleMap;
   }

   public void setVerticalAppExampleMap (Map<String, List<VerticalAppExample>> verticalAppExampleMap) {
      this.verticalAppExampleMap = verticalAppExampleMap;
   }

   public Long getCraigsListAppId () {
      return craigsListAppId;
   }

   public void setCraigsListAppId (Long craigsListAppId) {
      this.craigsListAppId = craigsListAppId;
   }

   public String getIsUnstructuredAppVisibleToUser () {
      return isUnstructuredAppVisibleToUser;
   }

   public void setIsUnstructuredAppVisibleToUser (String isUnstructuredAppVisibleToUser) {
      this.isUnstructuredAppVisibleToUser = isUnstructuredAppVisibleToUser;
   }

   /**
    * @return the applicationId
    */
   public Long getApplicationId () {
      return applicationId;
   }

   /**
    * @param applicationId
    *           the applicationId to set
    */
   public void setApplicationId (Long applicationId) {
      this.applicationId = applicationId;
   }

}
