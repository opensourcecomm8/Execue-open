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
 * This enum represents the if the cloud Comp is enough to select a cloud
 * 
 * @author Nihar
 * @version 1.0
 * @since 28/01/10
 */
public enum CloudComponentSelectionType implements IBaseEnumType {
   ENOUGH_FOR_CLOUD_SELECTION (1), NOT_ENOUGH_FOR_CLOUD_SELECTION (0);

   private Integer                                                value;
   private static final Map<Integer, CloudComponentSelectionType> reverseLookupMap = new HashMap<Integer, CloudComponentSelectionType>();

   static {
      for (CloudComponentSelectionType jobType : EnumSet.allOf(CloudComponentSelectionType.class)) {
         reverseLookupMap.put(jobType.value, jobType);
      }
   }

   CloudComponentSelectionType (Integer value) {
      this.value = value;
   }

   public Integer getValue () {
      return this.value;
   }

   public static CloudComponentSelectionType getType (Integer value) {
      return reverseLookupMap.get(value);
   }
}
