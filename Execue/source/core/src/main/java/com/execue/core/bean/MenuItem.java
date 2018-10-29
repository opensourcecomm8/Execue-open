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


package com.execue.core.bean;

import java.util.List;

public class MenuItem {

   private String         name;
   private String         id;
   private String         alias;
   private String         actionName;
   private String         css;
   private String         target;
   private boolean        isDefaultDisplay;
   private boolean        isLeaf;
   private List<Role>     roleList;
   private List<MenuItem> menuItems;
   private List<String>   parentIds;
   private MenuType       menuType = MenuType.SIMPLE;

   public String getName () {
      return name;
   }

   public void setName (String name) {
      this.name = name;
   }

   public String getAlias () {
      return alias;
   }

   public void setAlias (String alias) {
      this.alias = alias;
   }

   public String getActionName () {
      return actionName;
   }

   public void setActionName (String actionName) {
      this.actionName = actionName;
   }

   public String getCss () {
      return css;
   }

   public void setCss (String css) {
      this.css = css;
   }

   public boolean isLeaf () {
      return isLeaf;
   }

   public void setLeaf (boolean isLeaf) {
      this.isLeaf = isLeaf;
   }

   public List<Role> getRoleList () {
      return roleList;
   }

   public void setRoleList (List<Role> roleList) {
      this.roleList = roleList;
   }

   public List<MenuItem> getMenuItems () {
      return menuItems;
   }

   public void setMenuItems (List<MenuItem> menuItems) {
      this.menuItems = menuItems;
   }

   public String getId () {
      return id;
   }

   public void setId (String id) {
      this.id = id;
   }

   public boolean isDefaultDisplay () {
      return isDefaultDisplay;
   }

   public void setDefaultDisplay (boolean isDefaultDisplay) {
      this.isDefaultDisplay = isDefaultDisplay;
   }

   public List<String> getParentIds () {
      return parentIds;
   }

   public void setParentIds (List<String> parentIds) {
      this.parentIds = parentIds;
   }

   public MenuType getMenuType () {
      return menuType;
   }

   public void setMenuType (MenuType menuType) {
      this.menuType = menuType;
   }

   
   public String getTarget () {
      return target;
   }

   
   public void setTarget (String target) {
      this.target = target;
   }

}
