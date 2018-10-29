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
package com.execue.core.common.bean.uss;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import com.execue.core.type.IBaseEnumType;

/**
 * @author Nitesh
 *
 */
public enum UniversalSearchResultItemType implements IBaseEnumType  {

   PERFECT_MATCH (1), UNKNOWN_MATCH (2), PARTIAL_MATCH (3), KEYWORD_MATCH (4);

   private Integer                                                 value;
   private static final Map<Integer, UniversalSearchResultItemType> reverseLookupMap = new HashMap<Integer, UniversalSearchResultItemType>();

   static {
      for (UniversalSearchResultItemType variationSubType : EnumSet.allOf(UniversalSearchResultItemType.class)) {
         reverseLookupMap.put(variationSubType.value, variationSubType);
      }
   }

   UniversalSearchResultItemType (Integer value) {
      this.value = value;
   }

   public Integer getValue () {
      return this.value;
   }

   public static UniversalSearchResultItemType getType (Integer value) {
      return reverseLookupMap.get(value);
   }
}
