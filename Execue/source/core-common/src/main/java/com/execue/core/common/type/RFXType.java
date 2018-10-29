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
public enum RFXType implements IBaseEnumType{
   RFX_QUERY_CACHE (1), RFX_KNOWLEDGE_SEARCH (2), RFX_ENTITY_SEARCH (3), RFX_CONTENT (4);

   private Integer                            value;
   private static final Map<Integer, RFXType> reverseLookupMap = new HashMap<Integer, RFXType>();
   // private static String name = RFXType.class.getSimpleName();

   static {
      for (RFXType rfxEntityType : EnumSet.allOf(RFXType.class)) {
         reverseLookupMap.put(rfxEntityType.value, rfxEntityType);
      }
   }

   RFXType (Integer value) {
      this.value = value;
   }

   public Integer getValue () {
      return this.value;
   }

   public static RFXType getType (Integer value) {
      return reverseLookupMap.get(value);
   }

   // public String getDescription () {
   // return EnumLookupHelper.getEnumLookupDescription(name, String.valueOf(this.value));
   // }
}
