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

import com.execue.core.common.bean.util.AbsorptionWizardLinkDetail;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.swi.configuration.impl.AbsorptionWizardHelper;

public class AbsorptionWizardNextTagImpl extends TagSupport implements Serializable {

   private String              currentPage;
   private String              sourceType;

   private String              baseURL;
   private Map<String, String> paramsForLinks;

   public boolean init () {
      if (StringUtils.isBlank(getSourceType()) || StringUtils.isBlank(getCurrentPage())) {
         return false;
      }
      String tempParamValue;
      paramsForLinks = new HashMap<String, String>();
      for (String paramName : AbsorptionWizardHelper.getUrlParams()) {
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
         int flowIndex = AbsorptionWizardHelper.getSourceTypeWizardPathsMap().get(sourceType).indexOf(currentPage);
         if (flowIndex < 0) {
            return SKIP_BODY;
         }
         pageContext.getOut().write(getNextLinkData(getCurrentPage(), getSourceType(), flowIndex));
      } catch (IOException e) {
         e.printStackTrace();
      }
      return SKIP_BODY;
   }

   private String getNextLinkData (String currentPage, String sourceType, int flowIndex) {
      String tagOutputString = "";
      StringBuilder bcList = new StringBuilder();
      AbsorptionWizardLinkDetail absorptionWizardLinkDetail;
      if (flowIndex < AbsorptionWizardHelper.getSourceTypeWizardPathsMap().get(sourceType).size() - 1) {

         String str = AbsorptionWizardHelper.getSourceTypeWizardPathsMap().get(sourceType).get(flowIndex + 1);

         absorptionWizardLinkDetail = AbsorptionWizardHelper.getWizardPathLinkDetailMap().get(str);
         if (ExecueCoreUtil.isCollectionNotEmpty(absorptionWizardLinkDetail.getParams())
                  && !absorptionWizardLinkDetail.isScriptCall()) {
            appendToBaseURL(absorptionWizardLinkDetail.getParams());
         }
         bcList = bcList.append("<b> Next : </b>");
         bcList = bcList.append("<a href=\"" + absorptionWizardLinkDetail.getBaseLink());
         if (!absorptionWizardLinkDetail.isScriptCall()) {
            bcList = bcList.append(getBaseURL());
         }
         bcList = bcList.append("\">");
         bcList = bcList.append(absorptionWizardLinkDetail.getLinkDescription() + "</a>");

         tagOutputString = bcList.toString();
      }
      return tagOutputString;
   }

   private String generateURL (Map<String, String> linksMap) {
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

   private void appendToBaseURL (List<String> requestParams) {
      String tempParamValue;
      String tempURL = getBaseURL();
      for (String paramName : requestParams) {
         tempParamValue = ((HttpServletRequest) pageContext.getRequest()).getParameter(paramName);
         if (!StringUtils.isBlank(tempParamValue)) {
            tempURL = tempURL.concat("&").concat(paramName).concat("=").concat(tempParamValue);
         }
      }
      setBaseURL(tempURL);
   }

   public String getCurrentPage () {
      return currentPage;
   }

   public void setCurrentPage (String currentPage) {
      this.currentPage = currentPage;
   }

   public String getSourceType () {
      return sourceType;
   }

   public void setSourceType (String sourceType) {
      this.sourceType = sourceType;
   }

   public String getBaseURL () {
      return baseURL;
   }

   public void setBaseURL (String baseURL) {
      this.baseURL = baseURL;
   }

   public Map<String, String> getParamsForLinks () {
      return paramsForLinks;
   }

   public void setParamsForLinks (Map<String, String> paramsForLinks) {
      this.paramsForLinks = paramsForLinks;
   }

}
