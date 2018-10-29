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


package com.execue.web.core.swi;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.struts2.ServletActionContext;

import com.execue.core.bean.Menu;
import com.execue.core.bean.MenuItem;
import com.execue.core.bean.Resource;
import com.execue.core.bean.ResourceScopeType;
import com.execue.core.configuration.ResourceHelper;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.web.core.bean.MenuSelection;
import com.execue.web.core.helper.MenuHelper;
import com.execue.web.core.util.ExecueWebConstants;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

/**
 * Interceptor for all generic admin/console action handling
 * 
 * @author kaliki
 */
public class AdminInterceptor extends AbstractInterceptor {

   @Override
   public String intercept (ActionInvocation invocation) throws Exception {

      // handle menu actions parameter

      ActionContext context = invocation.getInvocationContext();
      MenuSelection selection = MenuHelper.getMenuSelection(ServletActionContext.getRequest().getSession());

      Resource resource = ResourceHelper.getResourceByActionName(context.getName());
      selection.setResource(resource);

      String menuParams = ServletActionContext.getRequest().getParameter(ExecueWebConstants.CURRENT_MENU_ITEM);

      Menu menu = (Menu) ServletActionContext.getServletContext().getAttribute(ExecueWebConstants.ADMIN_MENU);

      if (ExecueCoreUtil.isNotEmpty(menuParams)) {
         MenuHelper.parseAndPopulateMenuCurrentSelectionString(selection, menu, menuParams);
      } else {
         MenuItem item = MenuHelper.getMenuItemByAction(menu, context.getName());
         if (item != null) {
            // if the item does exists in the menu list get all parents and open them along with existing ones
            Set<String> openGroupsSet = new HashSet<String>();
            List<String> openGroups = new ArrayList<String>();
            openGroupsSet.addAll(selection.getExpandedMenuId());
            openGroupsSet.addAll(item.getParentIds());
            // TODO: - KA- CHECK FOR BETTER LOGIC ?
            if (!item.isLeaf()) {
               openGroupsSet.add(item.getId());
            }
            openGroups.addAll(openGroupsSet);
            selection.setExpandedMenuId(openGroups);
            // highlighting the item.
            selection.setSelectedMenuItem(item);

         }
         // else if the item doesen't exists in the menu list keep the last highlighted as is.
         // if it exists in resources table, put it into session for the breadcrumb to show it.

      }

      // App Selection logic for restricting resources
      if (resource != null
               && (ResourceScopeType.KDX == resource.getScope() || ResourceScopeType.SDX == resource.getScope())) {
         if (ServletActionContext.getRequest().getSession().getAttribute("APPLICATION") != null) {
            return invocation.invoke();
         } else {
            return "appSelection-failure";
         }
      } else {
         return invocation.invoke();
      }
   }
}
