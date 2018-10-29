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

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.execue.core.bean.Menu;
import com.execue.core.bean.MenuItem;
import com.execue.core.bean.Resource;
import com.execue.core.bean.ResourceScopeType;
import com.execue.core.common.bean.ApplicationContext;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.web.core.bean.MenuSelection;
import com.execue.web.core.helper.MenuHelper;

public class AdminBreadcrumbImpl extends TagSupport implements Serializable {

   private static final long   serialVersionUID                  = 1L;
   private static final Logger logger                            = Logger.getLogger(AdminMenuPageImpl.class);
   private static final String APPLICATION                       = "APPLICATION";
   private static final String SPAN_LINK_CLASS_START             = "<span class=\"bcLinks\" >";
   private static final String SPAN_NOLINK_CLASS_START           = "<span class=\"bcNoLinks\" >";
   private static final String SPAN_SEPARATOR_CLASS_START        = "<span class=\"bcArrows\" >";
   private static final String SPAN_APPNAME_ID_START             = "<span id=\"appNameDiv\">";
   private static final String SPAN_CURRENT_PAGE_ID_START        = "<span id=\"currentPageDiv\">";
   private static final String SPAN_APP_ID_NODISPLAY_CLASS_START = "<span id=\"appDiv\" style=\"display: none;\">";
   private static final String SPAN_APP_ID_DISPLAY_CLASS_START   = "<span id=\"appDiv\" style=\"display: inline;\">";
   private static final String SPAN_ASSET_ID_DISPLAY_CLASS_START = "<span id=\"assetNameDiv\">";
   private static final String SPAN_END_TAG                      = "</span>";
   private static final String DEFAULT_SEPARATOR                 = "&gt;&gt;";
   private static final String DEFAULT_DISPLAY_SEPARATOR_START   = "(";
   private static final String DEFAULT_DISPLAY_SEPARATOR_END     = ")";
   private static final String DEFAULT_ASSET_DISPLAY_NAME        = "Asset";
   private static final String DEFAULT_APP_DISPLAY_NAME          = "App";
   private static final String SPACE_SEPARATOR                   = " ";

   // attributes
   private String              homeName                          = null;
   private String              homeLink                          = null;
   private String              separator                         = null;
   private String              assetDisplayName                  = null;
   private String              appDisplayName                    = null;

   public int doEndTag () throws JspException {
      return EVAL_PAGE;
   }

   public int doStartTag () throws JspException {
      StringBuilder bcBuilder = new StringBuilder();
      bcBuilder.append(generateHomeBreadcrumb());
      try {
         MenuSelection menuSelection = (MenuSelection) pageContext.getSession().getAttribute(
                  ExecueWebConstants.CURRENT_MENU_SESSION_ATTRIBUTE);
         Menu menu = (Menu) pageContext.getServletContext().getAttribute(ExecueWebConstants.ADMIN_MENU);
         MenuItem selectedMenuItem = menuSelection.getSelectedMenuItem();
         Resource resource = menuSelection.getResource();

         if (selectedMenuItem == null) {
            // pageContext.getOut().write("<a href=\"../swi/showConsole.action\" >Publisher Console</a></span>");
         } else if (selectedMenuItem.getParentIds().size() == 0) {
            // bcBuilder.append(generateHref(selectedMenuItem.getActionName(), selectedMenuItem.getName()));
            // bcBuilder.append(generateSeperatorSpan());
         } else {
            for (String parentId : selectedMenuItem.getParentIds()) {
               MenuItem menuItem = MenuHelper.getMenuItemById(menu, parentId);
               bcBuilder.append(generateHref(menuItem.getActionName(), menuItem.getName(), menuItem.getTarget()));
               bcBuilder.append(generateSeperatorSpan());
            }
         }

         if (resource != null && null != getAppContext()
                  && (resource.getScope() == ResourceScopeType.SDX || resource.getScope() == ResourceScopeType.KDX)) {
            bcBuilder.append(SPAN_APP_ID_DISPLAY_CLASS_START).append(SPAN_NOLINK_CLASS_START);
            bcBuilder.append(getAppDisplayName());
            bcBuilder.append(SPAN_APPNAME_ID_START);
            bcBuilder.append(DEFAULT_DISPLAY_SEPARATOR_START).append(SPACE_SEPARATOR);
            bcBuilder.append(getAppContext().getApplicationName());
            bcBuilder.append(SPACE_SEPARATOR).append(DEFAULT_DISPLAY_SEPARATOR_END);
            bcBuilder.append(SPAN_END_TAG);
            bcBuilder.append(generateSeperatorSpan());
            bcBuilder.append(SPAN_END_TAG).append(SPAN_END_TAG);
            if (resource.getScope() == ResourceScopeType.SDX && !StringUtils.isEmpty(getAppContext().getAssetName())) {
               bcBuilder.append(SPAN_NOLINK_CLASS_START);
               bcBuilder.append(getAssetDisplayName());
               bcBuilder.append(SPAN_ASSET_ID_DISPLAY_CLASS_START);
               bcBuilder.append(DEFAULT_DISPLAY_SEPARATOR_START).append(SPACE_SEPARATOR).append(
                        getAppContext().getAssetName()).append(DEFAULT_DISPLAY_SEPARATOR_END);
               bcBuilder.append(SPAN_END_TAG);
               bcBuilder.append(generateSeperatorSpan());
               bcBuilder.append(SPAN_END_TAG);

            }
         } else {
            bcBuilder.append(SPAN_APP_ID_NODISPLAY_CLASS_START).append(SPAN_NOLINK_CLASS_START);
            bcBuilder.append(getAppDisplayName()).append(SPACE_SEPARATOR);
            bcBuilder.append(SPAN_APPNAME_ID_START).append(SPAN_END_TAG);
            bcBuilder.append(SPAN_END_TAG);
            bcBuilder.append(generateSeperatorSpan());
            bcBuilder.append(SPAN_END_TAG);

         }

         if (menuSelection != null) {
            String menuDisplayName = "";
            if (null != resource) {
               menuDisplayName = resource.getDisplayName();
            } else if (selectedMenuItem != null) {
               menuDisplayName = selectedMenuItem.getName();
            }
            bcBuilder.append(SPAN_CURRENT_PAGE_ID_START);
            bcBuilder.append(menuDisplayName);
            bcBuilder.append(SPAN_END_TAG);
         }

         if (logger.isDebugEnabled()) {
            logger.debug(bcBuilder.toString());
         }

         pageContext.getOut().write(bcBuilder.toString());

      } catch (IOException e) {
         e.printStackTrace();
      }
      return SKIP_BODY;
   }

   private ApplicationContext getAppContext () {
      return (ApplicationContext) pageContext.getSession().getAttribute(APPLICATION);
   }

   private String generateHref (String link, String displayName, String target) {
      StringBuilder sBuilder = new StringBuilder();
      sBuilder.append("<a href=\"");
      sBuilder.append(link);
      sBuilder.append("\"");
      if (ExecueCoreUtil.isNotEmpty(target)) {
         sBuilder.append(" target=\"" + target + "\"");
      }
      sBuilder.append(" >");
      sBuilder.append(displayName);
      sBuilder.append("</a>");
      return sBuilder.toString();
   }

   private String generateSeperatorSpan () {
      StringBuilder sBuilder = new StringBuilder();
      sBuilder.append(SPAN_SEPARATOR_CLASS_START);
      sBuilder.append(getSeparator());
      sBuilder.append(SPAN_END_TAG);
      return sBuilder.toString();
   }

   private String generateHomeBreadcrumb () {
      StringBuilder sBuilder = new StringBuilder();
      sBuilder.append(SPAN_LINK_CLASS_START);
      sBuilder.append(generateHref(getHomeLink(), getHomeName(), null));
      sBuilder.append(SPAN_END_TAG);
      sBuilder.append(generateSeperatorSpan());
      return sBuilder.toString();
   }

   public String getHomeName () {
      return homeName;
   }

   public void setHomeName (String homeName) {
      this.homeName = homeName;
   }

   public String getHomeLink () {
      return homeLink;
   }

   public void setHomeLink (String homeLink) {
      this.homeLink = homeLink;
   }

   public String getSeparator () {
      if (separator != null) {
         return separator;
      } else {
         return DEFAULT_SEPARATOR;
      }
   }

   public void setSeparator (String separator) {
      this.separator = separator;
   }

   public String getAssetDisplayName () {
      if (assetDisplayName != null) {
         return assetDisplayName;
      } else {
         return DEFAULT_ASSET_DISPLAY_NAME;
      }
   }

   public void setAssetDisplayName (String assetDisplayName) {
      this.assetDisplayName = assetDisplayName;
   }

   public String getAppDisplayName () {
      if (appDisplayName != null) {
         return appDisplayName;
      } else {
         return DEFAULT_APP_DISPLAY_NAME;
      }
   }

   public void setAppDisplayName (String appDisplayName) {
      this.appDisplayName = appDisplayName;
   }

}
