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

import com.execue.core.type.IBaseEnumType;

/**
 * @author Nitesh
 */
public enum UniversalSearchType implements IBaseEnumType{
   UNSTRUCTURED_SEARCH (1), QUERY_CACHE_SEARCH (2), RELATED_QUERY_SEARCH (3);

   private Integer                                        value;
   private static final Map<Integer, UniversalSearchType> reverseLookupMap = new HashMap<Integer, UniversalSearchType>();

   static {
      for (UniversalSearchType variationSubType : EnumSet.allOf(UniversalSearchType.class)) {
         reverseLookupMap.put(variationSubType.value, variationSubType);
      }
   }

   UniversalSearchType (Integer value) {
      this.value = value;
   }

   public Integer getValue () {
      return this.value;
   }

   public static UniversalSearchType getType (Integer value) {
      return reverseLookupMap.get(value);
   }
}