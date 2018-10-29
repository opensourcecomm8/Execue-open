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


package com.execue.core.common.type;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import com.execue.core.configuration.EnumLookupHelper;
import com.execue.core.type.IBaseEnumType;

public enum ExecueBasePermissionType implements IBaseEnumType {

   GRANT ("R"), WRITE ("W"), CREATE ("C"), REVOKE ("D"), ADMINISTRATION ("A");
   /*
    * NOTE: -RG- GRANT means READ w.r.t Spring Definition and REVOKE means DELETE
    *    Definition here is changed in order build Grant and Revoke based functionality 
    *    using Mask rather than Granting of Spring Security.
    *    This is needed as Spring Security API does not provide any method to change 
    *    the Granting flag to turn on and off once it is created.
    */

   private String                                             value;
   private static final Map<String, ExecueBasePermissionType> reverseLookupMap = new HashMap<String, ExecueBasePermissionType>();
   private static String                                      name             = ExecueBasePermissionType.class
                                                                                        .getSimpleName();

   static {
      for (ExecueBasePermissionType basePermissionType : EnumSet.allOf(ExecueBasePermissionType.class)) {
         reverseLookupMap.put(basePermissionType.value, basePermissionType);
      }
   }

   ExecueBasePermissionType (String value) {
      this.value = value;
   }

   public String getValue () {
      return this.value;
   }

   public static ExecueBasePermissionType getType (String value) {
      return reverseLookupMap.get(value);
   }

   public String getDescription () {
      return EnumLookupHelper.getEnumLookupDescription(name, value);
   }

}
