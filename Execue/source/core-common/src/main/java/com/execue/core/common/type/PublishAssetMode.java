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
 * @author john
 */
public enum PublishAssetMode implements IBaseEnumType{

   LOCAL (1), COMMUNITY (2), NONE(0);

   private Integer                                     value;
   private static final Map<Integer, PublishAssetMode> reverseLookupMap = new HashMap<Integer, PublishAssetMode>();
   private static String                               name             = PublishAssetMode.class.getSimpleName();

   static {
      for (PublishAssetMode publishAssetMode : EnumSet.allOf(PublishAssetMode.class)) {
         reverseLookupMap.put(publishAssetMode.value, publishAssetMode);
      }
   }

   PublishAssetMode (Integer value) {
      this.value = value;
   }

   public Integer getValue () {
      return this.value;
   }

   public static PublishAssetMode getType (Integer value) {
      return reverseLookupMap.get(value);
   }

   public String getDescription () {
      return EnumLookupHelper.getEnumLookupDescription(name, String.valueOf(this.value));
   }
}
