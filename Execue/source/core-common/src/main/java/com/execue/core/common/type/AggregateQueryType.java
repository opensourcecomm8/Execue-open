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
 * @author kaliki
 * @since 4.0
 */
public enum AggregateQueryType implements IBaseEnumType {
   BUSINESS_SUMMARY (1), TOTAL_SUMMARY (2), DETAILED_SUMMARY (3), HIERARCHY_SUMMARY (4), NO_SUMMARY (5), SCATTER (6);

   private Integer                                       value;
   private static final Map<Integer, AggregateQueryType> reverseLookupMap = new HashMap<Integer, AggregateQueryType>();
   private static String                                 name             = AggregateQueryType.class.getSimpleName();

   static {
      for (AggregateQueryType assetEntityType : EnumSet.allOf(AggregateQueryType.class)) {
         reverseLookupMap.put(assetEntityType.value, assetEntityType);
      }
   }

   AggregateQueryType (Integer value) {
      this.value = value;
   }

   public Integer getValue () {
      return this.value;
   }

   public static AggregateQueryType getAggregateQueryType (Integer value) {
      return reverseLookupMap.get(value);
   }

   public String getDescription () {
      return EnumLookupHelper.getEnumLookupDescription(name, String.valueOf(this.value));
   }
}
