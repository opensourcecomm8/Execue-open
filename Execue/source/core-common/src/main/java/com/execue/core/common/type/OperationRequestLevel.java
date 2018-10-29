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


/**
 * 
 */
package com.execue.core.common.type;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import com.execue.core.configuration.EnumLookupHelper;
import com.execue.core.type.IBaseEnumType;

/**
 * @author Nitesh
 *
 */
public enum OperationRequestLevel implements IBaseEnumType {

   SYSTEM ("SYSTEM"), APPLICATION ("APPLICATION"), ASSET ("ASSET");

   private String                                          value;
   private static final Map<String, OperationRequestLevel> reverseLookupMap = new HashMap<String, OperationRequestLevel>();
   private static String                                   name             = OperationRequestLevel.class
                                                                                     .getSimpleName();

   static {
      for (OperationRequestLevel checkType : EnumSet.allOf(OperationRequestLevel.class)) {
         reverseLookupMap.put(checkType.value, checkType);
      }
   }

   OperationRequestLevel (String value) {
      this.value = value;
   }

   public String getValue () {
      return this.value;
   }

   public static OperationRequestLevel getType (String value) {
      return reverseLookupMap.get(value);
   }

   public String getDescription () {
      return EnumLookupHelper.getEnumLookupDescription(name, String.valueOf(this.value));
   }
}
