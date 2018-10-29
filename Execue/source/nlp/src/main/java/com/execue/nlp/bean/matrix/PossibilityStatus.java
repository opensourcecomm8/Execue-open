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


package com.execue.nlp.bean.matrix;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import com.execue.core.configuration.EnumLookupHelper;
import com.execue.core.type.IBaseEnumType;

/**
 * User: Abhijit Date: Dec 16, 2008
 */
public enum PossibilityStatus implements IBaseEnumType {

   PRE_PROCESS (0), IN_PROCESS (1), INDIVIDUAL_RECOGNITION (2), GROUP_RECOGNITION (3), DOMAIN_RECOGNITION (4), ASSOCIATION (
            5), DOMAIN_ASSOCIATION (6), INDIVIDUAL_RECOGNITION_COMPLETE (7), GROUP_RECOGNITION_COMPLETE (8), DOMAIN_RECOGNITION_COMPLETE (
            9), ASSOCIATION_COMPLETE (10), DOMAIN_ASSOCIATION_COMPLETE (11), COMPLETED (99);

   private int                                          value;
   private static final Map<Integer, PossibilityStatus> reverseLookupMap = new HashMap<Integer, PossibilityStatus>();
   private static String                                name             = PossibilityStatus.class.getSimpleName();

   static {
      for (PossibilityStatus possibilityStatus : EnumSet.allOf(PossibilityStatus.class)) {
         reverseLookupMap.put(possibilityStatus.value, possibilityStatus);
      }
   }

   PossibilityStatus (int value) {
      this.value = value;
   }

   public Integer getValue () {
      return this.value;
   }

   public static PossibilityStatus getPossibilityStatus (int value) {
      return reverseLookupMap.get(value);
   }

   public String getDescription () {
      return EnumLookupHelper.getEnumLookupDescription(name, String.valueOf(this.value));
   }
}
