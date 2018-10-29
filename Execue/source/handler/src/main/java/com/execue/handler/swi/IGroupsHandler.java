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


package com.execue.handler.swi;

import java.util.List;

import com.execue.core.common.bean.security.SecurityGroups;
import com.execue.core.common.bean.security.SecurityRoles;
import com.execue.core.exception.HandlerException;

public interface IGroupsHandler {

   public Long createGroup (SecurityGroups group) throws HandlerException;

   public void updateGroup (SecurityGroups group) throws HandlerException;

   public SecurityGroups getGroupById (Long id) throws HandlerException;

   public List<SecurityGroups> getGroups (String status) throws HandlerException;

   public SecurityGroups getGroupAndRoles (Long id) throws HandlerException;

   public void assignGroupRoles (SecurityGroups group, List<SecurityRoles> roles) throws HandlerException;

   public void deleteGroup (SecurityGroups group) throws HandlerException;

   public List<SecurityGroups> getAllGroups () throws HandlerException;

   public List<SecurityGroups> getUserGroups (long userId) throws HandlerException;
}
