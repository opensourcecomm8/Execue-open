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

import javax.servlet.jsp.JspException;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.util.AbsorptionWizardLinkDetail;
import com.execue.swi.configuration.impl.AbsorptionWizardHelper;

/**
 * 
 * @author Ganesh Tarala
 *
 */
public class AbsorptionWizardPrevNext extends AbsorptionWizard {

   private static final Logger      log            = Logger.getLogger(AbsorptionWizardPrevNext.class);

   public boolean init () {
      return super.init();
   }

   public int doStartTag () throws JspException {
      if (!init()) {
         return SKIP_BODY;
      }
      try {

         pageContext.getOut().write(
                  getPreviousLinkData(getCurrentPage(), getSourceType()) + "  |" + getCurrentPage() + "|  "
                           + getNextLinkData(getCurrentPage(), getSourceType()));
         log.info("    " + getParamLinks().size());
      } catch (IOException e) {
         e.printStackTrace();
      }
      return SKIP_BODY;
   }

   public String getPreviousLinkData (String currData, String sType) {
      log.info(" inside getPreviousLinkData::: " + currData + " -------------- " + sType);
      int tempIndex = getPathsList().indexOf(currData);
      log.info(" Index of current Data::: " + tempIndex);
      String retString = "";

      StringBuilder bcList = new StringBuilder();
     
      AbsorptionWizardLinkDetail absorptionWizardLinkDetail;
      if (tempIndex > 0) {
         String str = getPathsList().get(tempIndex - 1);
         absorptionWizardLinkDetail = AbsorptionWizardHelper.getWizardPathLinkDetailMap().get(str);
         bcList = bcList.append("<a href=\"" + absorptionWizardLinkDetail.getBaseLink() + getBaseURL() + "\">" + absorptionWizardLinkDetail.getLinkDescription()
                  + "</a>");
         log.info("<< PREVIOUS  " + absorptionWizardLinkDetail.getBaseLink() + "|");
         retString = "<b> Previous </b>" + bcList.toString();
      }
      log.info(" URL::: " + bcList.toString());
      bcList = null;
      return retString;
   }

   public String getNextLinkData (String nextData, String sType) {
      log.info(" inside getNextLinkData::: " + nextData + " #################### " + sType);
      int tempIndex = getPathsList().indexOf(nextData);
      log.info(" Index of current Data::: " + tempIndex);
      String retString = "";

      StringBuilder bcList = new StringBuilder();
      AbsorptionWizardLinkDetail absorptionWizardLinkDetail;
      log.info(" Path Lists Size " + (getPathsList().size() - 1));
      if (tempIndex < getPathsList().size() - 1) {
         String str = getPathsList().get(tempIndex + 1);
         log.info(" Index " + tempIndex + " Previous STring  " + str);
         absorptionWizardLinkDetail = AbsorptionWizardHelper.getWizardPathLinkDetailMap().get(str);
         bcList = bcList.append("<b> Next : </b>");
         bcList = bcList.append("<a href=\"" + absorptionWizardLinkDetail.getBaseLink() + getBaseURL() + "\">" + absorptionWizardLinkDetail.getLinkDescription()
                  + "</a>");
         retString = bcList.toString();
      }
      log.info(" URL::: " + bcList.toString());
      bcList = null;
      return retString;
   }
}
