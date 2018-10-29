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
 * @author kaliki
 * @since 4.0
 */
public enum PageSearchType implements IBaseEnumType{
   STARTS_WITH (1), CONTAINS (2), ENDS_WITH (3), EQUALS (4), BY_APP_NAME (5), BY_PUBLISHER_NAME (6), VERTICAL (7);

   private Integer                                   value;
   private static final Map<Integer, PageSearchType> reverseLookupMap = new HashMap<Integer, PageSearchType>();
   private static String                             name             = PageSearchType.class.getSimpleName();

   static {
      for (PageSearchType assetEntityType : EnumSet.allOf(PageSearchType.class)) {
         reverseLookupMap.put(assetEntityType.value, assetEntityType);
      }
   }

   PageSearchType (Integer value) {
      this.value = value;
   }

   public Integer getValue () {
      return this.value;
   }

   public static PageSearchType getAggregateQueryType (Integer value) {
      return reverseLookupMap.get(value);
   }

   public String getDescription () {
      return EnumLookupHelper.getEnumLookupDescription(name, String.valueOf(this.value));
   }
}
