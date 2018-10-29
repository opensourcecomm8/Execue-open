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

public enum SharedTypeModelMappingType implements IBaseEnumType{
   LOCATION (301L);

   private Long                                               value;
   private static final Map<Long, SharedTypeModelMappingType> reverseLookupMap = new HashMap<Long, SharedTypeModelMappingType>();
   private static String                                      name             = SharedTypeModelMappingType.class
                                                                                        .getSimpleName();

   static {
      for (SharedTypeModelMappingType dataType : EnumSet.allOf(SharedTypeModelMappingType.class)) {
         reverseLookupMap.put(dataType.value, dataType);
      }
   }

   SharedTypeModelMappingType (Long value) {
      this.value = value;
   }

   public Long getValue () {
      return this.value;
   }

   public static SharedTypeModelMappingType getType (String value) {
      return reverseLookupMap.get(value);
   }

   public String getDescription () {
      return EnumLookupHelper.getEnumLookupDescription(name, String.valueOf(this.value));
   }

}
