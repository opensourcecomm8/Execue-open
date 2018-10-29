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

import com.execue.core.common.bean.security.User;
import com.execue.handler.swi.IUsersHandler;

public abstract class BaseUserAction extends SecurityAction {

   protected IUsersHandler  usersHandler;
   protected User           user;
   
   public IUsersHandler getUsersHandler () {
      return usersHandler;
   }
   
   public void setUsersHandler (IUsersHandler usersHandler) {
      this.usersHandler = usersHandler;
   }
   
   public User getUser () {
      return user;
   }
   
   public void setUser (User user) {
      this.user = user;
   }
}
