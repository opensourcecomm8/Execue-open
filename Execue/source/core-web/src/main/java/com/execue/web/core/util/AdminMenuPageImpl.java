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
import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;

import org.apache.log4j.Logger;

import com.execue.core.bean.Menu;
import com.execue.core.bean.MenuItem;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.web.core.bean.MenuSelection;
import com.execue.web.core.helper.MenuHelper;

public class AdminMenuPageImpl implements Tag, Serializable {

   private static final long   serialVersionUID = 3932948932205132618L;
   private static final Logger log              = Logger.getLogger(AdminMenuPageImpl.class);
   private PageContext         pageContext;
   private Tag                 tag;
   private Menu                adminMenu;

   public void init () {
      try {
         if (null == getAdminMenu())
            adminMenu = (Menu) pageContext.getSession().getServletContext().getAttribute("adminMenu");
      } catch (Exception e) {
         log.error("\n\nError generating html menu: ", e);
      }
   }

   public int doEndTag () throws JspException {
      return EVAL_PAGE;
   }

   public int doStartTag () throws JspException {
      init();
      MenuSelection menuSelection = (MenuSelection) pageContext.getSession().getAttribute(
               ExecueWebConstants.CURRENT_MENU_SESSION_ATTRIBUTE);

      generateMenu(getAdminMenu().getMenuItems(), pageContext, menuSelection);
      return SKIP_BODY;
   }

   private void generateMenu (List<MenuItem> currentMenuItemList, PageContext pageContext, MenuSelection menuSelection) {
      try {

         List<String> openMenuItems = new ArrayList<String>();
         MenuItem currentMenuItem = null;
         if (menuSelection != null) {
            if (menuSelection.getExpandedMenuId() != null) {
               openMenuItems = menuSelection.getExpandedMenuId();
            }
            currentMenuItem = menuSelection.getSelectedMenuItem();
         }

         for (MenuItem item : currentMenuItemList) {
            String itemId = item.getId();
            if (MenuHelper.hasAccess(item, menuSelection.getMenuType())) {
               if (!item.isLeaf()) {
                  String openMenuString = "<span class=\"" + item.getCss() + "\" id=\"G_" + itemId + "\">"
                           + " <img src=\"../images/open.gif\" id=\"I_" + itemId + "\" onclick=\"showBranch('" + itemId
                           + "')\"/><a id=\"G_" + itemId + "\" onclick=\"showGroup('" + itemId + "')\" href=\""
                           + item.getActionName() + "\" class=\"group\">" + item.getName() + "</a><span id=\"" + itemId
                           + "\" class=\"branch\" style=\"display: block;\">";
                  String closedMenuString = "<span class=\"" + item.getCss() + "\" id=\"" + "G_" + itemId + "\">"
                           + " <img src=\"../images/closed.gif\" id=\"I_" + itemId + "\" onclick=\"showBranch('"
                           + itemId + "')\"/><a id=\"G_" + itemId + "\" onclick=\"showGroup('" + itemId
                           + "')\" href=\"" + item.getActionName() + "\" class=\"group\">" + item.getName()
                           + "</a><span id=\"" + itemId + "\" class=\"branch\" style=\"display: none;\">";
                  if (openMenuItems.size() > 0) {
                     if (openMenuItems.contains(itemId)) {
                        pageContext.getOut().write(openMenuString);
                     } else {
                        pageContext.getOut().write(closedMenuString);
                     }
                  } else if (item.isDefaultDisplay()) {
                     pageContext.getOut().write(openMenuString);
                  } else {
                     pageContext.getOut().write(closedMenuString);
                  }

                  if (item.getMenuItems().size() > 0)
                     generateMenu(item.getMenuItems(), pageContext, menuSelection);
                  pageContext.getOut().write("</span>");
               } else {
                  String clickedLinkString = "";
                  String defaultLinkString = "";
                  if (ExecueCoreUtil.isNotEmpty(item.getTarget())) {
                     clickedLinkString = "<a class=\"" + item.getCss() + "\" style=\"font-weight: bolder;\" href=\""
                              + item.getActionName() + "\" id=\"" + itemId + "\" target=\"" + item.getTarget() + "\" >"
                              + item.getName() + "</a><br>";
                     defaultLinkString = "<a class=\"" + item.getCss() + "\" href=\"" + item.getActionName()
                              + "\" id=\"" + itemId + "\" target=\"" + item.getTarget() + "\" >" + item.getName()
                              + "</a><br>";
                  } else {
                     clickedLinkString = "<a class=\"" + item.getCss() + "\" style=\"font-weight: bolder;\" href=\""
                              + item.getActionName() + "\" id=\"" + itemId + "\">" + item.getName() + "</a><br>";
                     defaultLinkString = "<a class=\"" + item.getCss() + "\" href=\"" + item.getActionName()
                              + "\" id=\"" + itemId + "\">" + item.getName() + "</a><br>";
                  }
                  if (currentMenuItem != null && currentMenuItem.getId().equals(item.getId())) {
                     pageContext.getOut().write(clickedLinkString);
                  } else {
                     pageContext.getOut().write(defaultLinkString);
                  }

               }
            }
         }
         pageContext.getOut().write("</span>");
      } catch (IOException ioe) {
         log.error("\n\nError in while performing IO operation: ", ioe);
      }
   }

   public Tag getParent () {
      return getTag();
   }

   public void release () {
      pageContext = null;
      tag = null;
   }

   public void setPageContext (PageContext arg0) {
      pageContext = arg0;
   }

   public void setParent (Tag arg0) {
      setTag(arg0);
   }

   public Tag getTag () {
      return tag;
   }

   public void setTag (Tag tag) {
      this.tag = tag;
   }

   public PageContext getPageContext () {
      return pageContext;
   }

   public Menu getAdminMenu () {
      return adminMenu;
   }

   public void setAdminMenu (Menu adminMenu) {
      this.adminMenu = adminMenu;
   }

}
