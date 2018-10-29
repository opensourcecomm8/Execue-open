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


package com.execue.security.dataaccess;

import com.execue.core.common.bean.StatusEnum;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.security.SecurityGroups;
import com.execue.core.common.bean.security.SecurityRoles;
import com.execue.core.common.bean.security.User;
import com.execue.core.common.bean.security.UserStatus;
import com.execue.swi.exception.SDXException;

public class SecurityCommonBaseTest extends SecurityBaseTest {

   public User getSampleUser (int uniqueIndex, UserStatus status) {
      User user = new User();
      user.setUsername("user" + uniqueIndex);
      user.setFirstName("first" + uniqueIndex);
      user.setLastName("last" + uniqueIndex);
      user.setPassword("password" + uniqueIndex);
      user.setStatus(status);
      return user;
   }

   public SecurityGroups getSampleSecurityGroup (int uniqueIndex, StatusEnum status) {
      SecurityGroups group = new SecurityGroups();
      group.setName("group" + uniqueIndex);
      group.setDescription("group" + uniqueIndex);
      group.setStatus(status);
      return group;

   }

   public SecurityRoles getSampleSecurityRole (int uniqueIndex, StatusEnum status) {
      SecurityRoles role = new SecurityRoles();
      role.setName("Admin" + uniqueIndex);
      role.setDescription("administrator" + uniqueIndex);
      role.setStatus(status);
      return role;
   }

   public Asset populateAsset (Long id) {
      Asset asset = new Asset();
      asset.setId(id);
      return asset;
   }
   public Asset populateAsset () {
      Long assetId = 2024L;
      return populateAsset(assetId);
   }

}
