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


package com.execue.web.core.bean;

import java.util.List;

import com.execue.core.bean.MenuItem;
import com.execue.core.bean.MenuType;
import com.execue.core.bean.Resource;

/**
 * Holds Menu Objects for Current Selection
 * 
 * @author kaliki
 */
public class MenuSelection {

   private MenuItem     selectedMenuItem;
   private List<String> expandedMenuId;
   private Resource     resource;
   private MenuType       menuType;

   public MenuItem getSelectedMenuItem () {
      return selectedMenuItem;
   }

   public void setSelectedMenuItem (MenuItem selectedMenuItem) {
      this.selectedMenuItem = selectedMenuItem;
   }

   public List<String> getExpandedMenuId () {
      return expandedMenuId;
   }

   public void setExpandedMenuId (List<String> expandedMenuId) {
      this.expandedMenuId = expandedMenuId;
   }

   public Resource getResource () {
      return resource;
   }

   public void setResource (Resource resource) {
      this.resource = resource;
   }

   public MenuType getMenuType () {
      return menuType;
   }

   public void setMenuType (MenuType menuType) {
      this.menuType = menuType;
   }

}
