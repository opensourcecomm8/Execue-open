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


package com.execue.web.core.action.swi.security;

import com.execue.core.common.bean.security.SecurityGroups;
import com.execue.handler.swi.IGroupsHandler;

public abstract class BaseGroupAction extends SecurityAction {

   protected IGroupsHandler groupsHandler;
   protected SecurityGroups  group;

   public IGroupsHandler getGroupsHandler () {
      return groupsHandler;
   }

   public void setGroupsHandler (IGroupsHandler groupsHandler) {
      this.groupsHandler = groupsHandler;
   }

   public SecurityGroups getGroup () {
      return group;
   }

   public void setGroup (SecurityGroups group) {
      this.group = group;
   }

}
