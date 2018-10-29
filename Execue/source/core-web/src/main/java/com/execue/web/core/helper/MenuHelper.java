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


package com.execue.web.core.helper;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.execue.core.bean.Menu;
import com.execue.core.bean.MenuItem;
import com.execue.core.bean.MenuType;
import com.execue.core.bean.Role;
import com.execue.security.util.ExecueSecurityUitl;
import com.execue.web.core.bean.MenuSelection;
import com.execue.web.core.util.ExecueWebConstants;

public class MenuHelper {

   private static String MENU_ID     = "id";
   private static String MENU_ACTION = "actionName";
   private static String ACTION      = ".action";

   public static MenuItem getMenuItemById (Menu menu, String id) {
      MenuItem item = null;
      item = getMenuItem(menu.getMenuItems(), MENU_ID, id);
      return item;
   }

   public static MenuItem getMenuItemByAction (Menu menu, String id) {
      MenuItem item = null;
      item = getMenuItem(menu.getMenuItems(), MENU_ACTION, id);
      return item;
   }

   public static void changeMenuType (MenuSelection menuSelection, String type) {
      if (type.equalsIgnoreCase(MenuType.ADVANCE.getValue())) {
         menuSelection.setMenuType(MenuType.ADVANCE);
      } else {
         menuSelection.setMenuType(MenuType.SIMPLE);
      }

   }

   public static void parseAndPopulateMenuCurrentSelectionString (MenuSelection menuSelection, Menu menu,
            String menuParams) {

      String[] tokens = menuParams.split("#");
      if (tokens.length >= 2) {
         MenuItem selected = getMenuItem(menu.getMenuItems(), MENU_ID, tokens[0]);
         menuSelection.setSelectedMenuItem(selected);
         String[] openGroupIds = StringUtils.split(tokens[1], ',');
         ArrayList<String> openGrpIdList = new ArrayList<String>();
         for (int i = 0; i < openGroupIds.length; i++) {
            openGrpIdList.add(openGroupIds[i].trim());
         }
         menuSelection.setExpandedMenuId(openGrpIdList);
      }
   }

   private static MenuItem getMenuItem (List<MenuItem> items, String type, String id) {
      MenuItem selectItem = null;
      for (MenuItem menuItem : items) {
         boolean match = false;
         if (MENU_ID.equals(type) && menuItem.getId().equals(id)) {
            match = true;
         } else if (MENU_ACTION.equals(type) && menuItem.getActionName().contains(id + ACTION)) {
            match = true;
         }
         if (match) {
            selectItem = menuItem;
            break;
         } else if (menuItem.getMenuItems() != null) {
            selectItem = getMenuItem(menuItem.getMenuItems(), type, id);
            if (selectItem != null) {
               return selectItem;
            }
         }
      }
      return selectItem;
   }

   public static boolean hasAccess (MenuItem item, MenuType selectionType) {
      List<GrantedAuthority> grantedAuthorities = ExecueSecurityUitl.getAuthoritiesFromContext();
      List<Role> roles = item.getRoleList();
      boolean hasAccess = false;
      if (roles == null) {
         return true;
      }
      for (Role role : roles) {
         String roleName = role.getRoleName();
         for (GrantedAuthority grantedAuthority : grantedAuthorities) {
            if (grantedAuthority.getAuthority().equals(roleName)) {
               hasAccess = true;
               break;
            }
         }
         if (hasAccess) {
            break;
         }
      }

      // check for menu type access
      if (hasAccess && !(item.getMenuType() == MenuType.BOTH)) {
         hasAccess = selectionType == item.getMenuType();
      }
      return hasAccess;
   }

   public static MenuSelection getMenuSelection (HttpSession session) {
      MenuSelection selection = (MenuSelection) session.getAttribute(ExecueWebConstants.CURRENT_MENU_SESSION_ATTRIBUTE);
      if (selection == null) {
         selection = new MenuSelection();
         selection.setExpandedMenuId(new ArrayList<String>());
         session.setAttribute(ExecueWebConstants.CURRENT_MENU_SESSION_ATTRIBUTE, selection);
      }

      if (selection.getMenuType() == null) {
         MenuHelper.populateDefaultMenuType(selection);
      }
      return selection;
   }

   public static void populateDefaultMenuType (MenuSelection selection) {
      selection.setMenuType(MenuType.SIMPLE);
      if (isAdvanceUser()) {
         selection.setMenuType(MenuType.ADVANCE);
      }

   }

   public static boolean isAdvanceUser () {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      if (authentication != null) {
         List<GrantedAuthority> grantedAuthorities = ExecueSecurityUitl.getAuthoritiesFromContext();
         for (GrantedAuthority grantedAuthority : grantedAuthorities) {
            if (grantedAuthority.getAuthority().equals(ExecueWebConstants.ADVANCED_PUBLISHER_ROLE)
                     || grantedAuthority.getAuthority().equals(ExecueWebConstants.ADMIN_ROLE)) {
               return true;
            }
         }
      }
      return false;
   }
}
