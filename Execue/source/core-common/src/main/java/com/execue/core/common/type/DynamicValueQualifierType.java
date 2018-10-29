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


/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */

package com.execue.core.common.type;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import com.execue.core.configuration.EnumLookupHelper;
import com.execue.core.type.IBaseEnumType;

/**
 * @author ExeCue
 */
public enum DynamicValueQualifierType implements IBaseEnumType {
   LAST ("LAST"), FIRST ("FIRST"), NEXT ("NEXT"), ALL ("ALL"), SINCE ("SINCE"), BEFORE ("BEFORE"), AFTER ("AFTER");

   private String                                              value;
   private static final Map<String, DynamicValueQualifierType> reverseLookupMap = new HashMap<String, DynamicValueQualifierType>();
   private static String                                       name             = DynamicValueQualifierType.class
                                                                                         .getSimpleName();

   static {
      for (DynamicValueQualifierType dynamicValueQualifierType : EnumSet.allOf(DynamicValueQualifierType.class)) {
         reverseLookupMap.put(dynamicValueQualifierType.value, dynamicValueQualifierType);
      }
   }

   DynamicValueQualifierType (String value) {
      this.value = value;
   }

   public String getValue () {
      return this.value;
   }

   public static DynamicValueQualifierType getType (String value) {
      return reverseLookupMap.get(value);
   }

   public String getDescription () {
      return EnumLookupHelper.getEnumLookupDescription(name, String.valueOf(this.value));
   }
}
