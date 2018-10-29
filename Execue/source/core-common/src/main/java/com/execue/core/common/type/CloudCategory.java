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
 * This enum represents the cloud category type
 * 
 * @author Nihar
 * @version 1.0
 * @since 28/01/10
 */
public enum CloudCategory implements IBaseEnumType {
   TYPE_CLOUD (1), APP_CLOUD (2), FRAMEWORK_CLOUD (3), CONCEPT_CLOUD (4), INFORMATION_CLOUD (5);

   private Integer                                  value;
   private static final Map<Integer, CloudCategory> reverseLookupMap = new HashMap<Integer, CloudCategory>();

   static {
      for (CloudCategory jobType : EnumSet.allOf(CloudCategory.class)) {
         reverseLookupMap.put(jobType.value, jobType);
      }
   }

   CloudCategory (Integer value) {
      this.value = value;
   }

   public Integer getValue () {
      return this.value;
   }

   public static CloudCategory getType (Integer value) {
      return reverseLookupMap.get(value);
   }
}
