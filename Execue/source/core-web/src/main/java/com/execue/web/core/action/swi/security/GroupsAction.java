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

import java.util.List;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.StatusEnum;
import com.execue.core.common.bean.security.SecurityGroups;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.core.exception.HandlerException;
public class GroupsAction extends BaseGroupAction {

   private List<SecurityGroups> groups;

   private static Logger       logger = Logger.getLogger(GroupsAction.class);

   public String list () {

      try {
         groups = getGroupsHandler().getAllGroups();
      } catch (Exception e) {
         e.printStackTrace();
         // TODO: populate the error message
      }
      return SUCCESS;
   }

   public String input () {

      if (group != null && group.getId() != null) {
         try {
            group = groupsHandler.getGroupById(group.getId());
         } catch (Exception e) {
            return ERROR;
         }

         if (logger.isDebugEnabled()) {
            logger.debug("group name " + group.getName());
         }
      }
      return INPUT;
   }

   public String save () {
      try {
         if (group.getId() == null) {
            groupsHandler.createGroup(group);
            addActionMessage(getText("execue.global.insert.success", new String[] { getText("execue.group.label") + " "
                     + group.getName() }));
         } else {
            groupsHandler.updateGroup(group);
            addActionMessage(getText("execue.global.update.success", new String[] { getText("execue.group.label") + " "
                     + group.getName() }));
         }
      } catch (HandlerException e) {
         if (e.getCode() == ExeCueExceptionCodes.ENTITY_ALREADY_EXISTS)
            addActionError(getText("execue.global.exist.message", new String[] { group.getName() }));
         else
            addActionError(getText("execue.global.error", new String[] { e.getMessage() }));
         return ERROR;
      }
      return SUCCESS;
   }
   public String updateGroupStatus () {    
      try {
         if (group != null && group.getId() != null) {
            group = groupsHandler.getGroupById(group.getId());         
            if (StatusEnum.ACTIVE.equals(group.getStatus())) {             
               group.setStatus(StatusEnum.INACTIVE);
               groupsHandler.updateGroup(group);
            } else if (StatusEnum.INACTIVE.equals(group.getStatus())) {             
               group.setStatus(StatusEnum.ACTIVE);
               groupsHandler.updateGroup(group);
            }
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
      return INPUT;
   }
   public List<SecurityGroups> getGroups () {
      return groups;
   }

   public void setGroups (List<SecurityGroups> groups) {
      this.groups = groups;
   }
   
}
