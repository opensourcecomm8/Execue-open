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
 * @author John Mallavalli
 */
public enum TermType implements IBaseEnumType{
   ASSET_ENTITY (1), BUSINESS_ENTITY (2), SFL_TERM_TOKEN_ENTITY (3), RI_PARALLEL_TERM_ENTITY (4), APPLICATION (5), MODEL (
            6);

   private Integer                             value;
   private static final Map<Integer, TermType> reverseLookupMap = new HashMap<Integer, TermType>();
   private static String                       name             = TermType.class.getSimpleName();

   static {
      for (TermType termType : EnumSet.allOf(TermType.class)) {
         reverseLookupMap.put(termType.value, termType);
      }
   }

   TermType (Integer value) {
      this.value = value;
   }

   public Integer getValue () {
      return this.value;
   }

   public static TermType getType (Integer value) {
      return reverseLookupMap.get(value);
   }

   public String getDescription () {
      return EnumLookupHelper.getEnumLookupDescription(name, String.valueOf(this.value));
   }
}
