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


package com.execue.core.common.bean.security;

import com.execue.core.common.type.ExecueBasePermissionType;

/**
 * 
 * @author Jitendra
 * This object represent AccessControlEntry.
 * NOTE: for added bare minimum info will enhance this object later on.
 */

public class ExecueAccessControlEntry {

   private Long                     id;
   private String                   grantedAuthority;
   private ExecueBasePermissionType basePermissionType;

   /**
    * @return the id
    */
   public Long getId () {
      return id;
   }

   /**
    * @param id the id to set
    */
   public void setId (Long id) {
      this.id = id;
   }

   /**
    * @return the grantedAuthority
    */
   public String getGrantedAuthority () {
      return grantedAuthority;
   }

   /**
    * @param grantedAuthority the grantedAuthority to set
    */
   public void setGrantedAuthority (String grantedAuthority) {
      this.grantedAuthority = grantedAuthority;
   }

   /**
    * @return the basePermissionType
    */
   public ExecueBasePermissionType getBasePermissionType () {
      return basePermissionType;
   }

   /**
    * @param basePermissionType the basePermissionType to set
    */
   public void setBasePermissionType (ExecueBasePermissionType basePermissionType) {
      this.basePermissionType = basePermissionType;
   }

}
