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

/**
 * @author Vishay
 * @since 4.0
 */
public enum CurrentPublisherFlowStatusType implements IBaseEnumType {
   UN_SUPPORTED ('U'), ANALYZE ('A'), ANALYZING ('B'), CONFIRM_AND_ENABLE ('C'), ENABLING ('D'), ENABLED ('E');

   private char                                                        value;
   private static final Map<Character, CurrentPublisherFlowStatusType> reverseLookupMap = new HashMap<Character, CurrentPublisherFlowStatusType>();
   private static String                                               name             = CurrentPublisherFlowStatusType.class
                                                                                                 .getSimpleName();

   static {
      for (CurrentPublisherFlowStatusType currentPublisherFlowStatusType : EnumSet
               .allOf(CurrentPublisherFlowStatusType.class)) {
         reverseLookupMap.put(currentPublisherFlowStatusType.value, currentPublisherFlowStatusType);
      }
   }

   CurrentPublisherFlowStatusType (char value) {
      this.value = value;
   }

   public Character getValue () {
      return this.value;
   }

   public static CurrentPublisherFlowStatusType getType (char value) {
      return reverseLookupMap.get(value);
   }

   public String getDescription () {
      return EnumLookupHelper.getEnumLookupDescription(name, String.valueOf(this.value));
   }
}
