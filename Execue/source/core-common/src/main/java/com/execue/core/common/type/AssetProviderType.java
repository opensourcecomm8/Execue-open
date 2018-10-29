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
 * @author venu
 */
public enum AssetProviderType implements IBaseEnumType {
   Oracle (1), MySql (2), MSSql (3), DB2 (4), DEFAULT (5), HTTP (6), Teradata (7), SAS_SHARENET (8), SAS_WORKSPACE (9), DERBY (
            10), POSTGRES (11);

   private Integer                                      value;
   private static String                                name             = AssetProviderType.class.getSimpleName();
   private static final Map<Integer, AssetProviderType> reverseLookupMap = new HashMap<Integer, AssetProviderType>();

   static {
      for (AssetProviderType assetProviderType : EnumSet.allOf(AssetProviderType.class)) {
         reverseLookupMap.put(assetProviderType.value, assetProviderType);
      }
   }

   AssetProviderType (Integer value) {
      this.value = value;
   }

   public Integer getValue () {
      return this.value;
   }

   public static AssetProviderType getWordType (Integer value) {
      return reverseLookupMap.get(value);
   }

   public String getDescription () {
      return EnumLookupHelper.getEnumLookupDescription(name, String.valueOf(this.value));
   }
}
