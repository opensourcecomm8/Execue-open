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


package com.execue.web.core.util;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.execue.core.common.bean.util.AbsorptionWizardLinkDetail;
import com.execue.swi.configuration.impl.AbsorptionWizardHelper;

/**
 * 
 * @author Ganesh Tarala
 *
 */
public class AbsorptionWizard extends TagSupport implements Serializable {

   private static final Logger log            = Logger.getLogger(AbsorptionWizard.class);

   private String              currentPage;
   private String              wizardBased;
   private String              sourceType;

   private List<String>        pathsList;
   private List<String>        paramLinks;

   private String              breadCrumbList;
   private String              baseURL;
   private Map<String, String> paramsForLinks = new HashMap<String, String>();
   private WizardPathDetail    wizardPathDetail;

   public boolean init () {
      if (!"Y".equals(wizardBased)) {
         return false;
      }
      pathsList = AbsorptionWizardHelper.getSourceTypeWizardPathsMap().get(sourceType);
      paramLinks = AbsorptionWizardHelper.getUrlParams();
      String tempParamValue;
      paramsForLinks = new HashMap<String, String>();
      for (String paramName : paramLinks) {
         tempParamValue = ((HttpServletRequest) pageContext.getRequest()).getParameter(paramName);
         if (!StringUtils.isBlank(tempParamValue)) {
            paramsForLinks.put(paramName, tempParamValue);
         }
      }
      setBaseURL(generateURL(paramsForLinks));
      return true;
   }

   public int doStartTag () throws JspException {
      if (!init()) {
         return SKIP_BODY;
      }
      try {
         breadCrumbList = generateBreadCrumbList();
         pageContext.getOut().write(breadCrumbList);
      } catch (IOException e) {
         // TODO: What to do with exceptions ??
         e.printStackTrace();
      }
      return SKIP_BODY;
   }

   public int doEndTag () throws JspException {
      return EVAL_PAGE;
   }

   protected String generateURL (Map<String, String> linksMap) {
      StringBuilder tempUrl = new StringBuilder();
      int count = 0;
      for (Map.Entry<String, String> entry : paramsForLinks.entrySet()) {
         count++;
         if (count == 1)
            tempUrl = tempUrl.append("?" + entry.getKey() + "=" + entry.getValue() + "&");
         else if (count < paramsForLinks.size())
            tempUrl = tempUrl.append(entry.getKey() + "=" + entry.getValue() + "&");
         else if (count == paramsForLinks.size())
            tempUrl = tempUrl.append(entry.getKey() + "=" + entry.getValue());
      }
      return tempUrl.toString();
   }

   private String generateBreadCrumbList () {
      StringBuilder bcList = new StringBuilder();
      AbsorptionWizardLinkDetail absorptionWizardLinkDetail;
      for (String path : getPathsList()) {
         absorptionWizardLinkDetail = AbsorptionWizardHelper.getWizardPathLinkDetailMap().get(path);
         bcList = bcList.append("<td style='padding:10px;font-family:Arial;vertical-align:middle; '><a href='"
                  + absorptionWizardLinkDetail.getBaseLink() + "" + baseURL + "'>"
                  + absorptionWizardLinkDetail.getBreadcrumbDescription() + "</a></td>");
      }
      log.info(" URL::: " + bcList.toString());
      return bcList.toString();
   }

   public List<String> getPathsList () {
      return pathsList;
   }

   public void setPathsList (List<String> pathsList) {
      this.pathsList = pathsList;
   }

   public String getBaseURL () {
      return baseURL;
   }

   public void setBaseURL (String baseURL) {
      this.baseURL = baseURL;
   }

   public List<String> getParamLinks () {
      return paramLinks;
   }

   public void setParamLinks (List<String> paramLinks) {
      this.paramLinks = paramLinks;
   }

   public String getBreadCrumbList () {
      return breadCrumbList;
   }

   public void setBreadCrumbList (String breadCrumbList) {
      this.breadCrumbList = breadCrumbList;
   }

   public String getCurrentPage () {
      return currentPage;
   }

   public void setCurrentPage (String currentPage) {
      this.currentPage = currentPage;
   }

   public String getWizardBased () {
      return wizardBased;
   }

   public Map<String, String> getParamsForLinks () {
      return paramsForLinks;
   }

   public void setParamsForLinks (Map<String, String> paramsForLinks) {
      this.paramsForLinks = paramsForLinks;
   }

   public void setWizardBased (String wizardBased) {
      this.wizardBased = wizardBased;
   }

   public String getSourceType () {
      return sourceType;
   }

   public void setSourceType (String sourceType) {
      this.sourceType = sourceType;
   }

   public WizardPathDetail getWizardPathDetail () {
      return wizardPathDetail;
   }

   public void setWizardPathDetail (WizardPathDetail wizardPathDetail) {
      this.wizardPathDetail = wizardPathDetail;
   }

}

class WizardPathDetail {

   private String           pathType;
   private List<String>     paths;
   private List<WizardPage> wizardPages;

   public String getPathType () {
      return pathType;
   }

   public void setPathType (String pathType) {
      this.pathType = pathType;
   }

   public List<String> getPaths () {
      return paths;
   }

   public void setPaths (List<String> paths) {
      this.paths = paths;
   }

   public List<WizardPage> getWizardPages () {
      return wizardPages;
   }

   public void setWizardPages (List<WizardPage> wizardPages) {
      this.wizardPages = wizardPages;
   }

}

class WizardPage {

   private String linkDescription;
   private String breadcrumbDescription;
   private String baseLink;

   public String getLinkDescription () {
      return linkDescription;
   }

   public void setLinkDescription (String linkDescription) {
      this.linkDescription = linkDescription;
   }

   public String getBreadcrumbDescription () {
      return breadcrumbDescription;
   }

   public void setBreadcrumbDescription (String breadcrumbDescription) {
      this.breadcrumbDescription = breadcrumbDescription;
   }

   public String getBaseLink () {
      return baseLink;
   }

   public void setBaseLink (String baseLink) {
      this.baseLink = baseLink;
   }

}
