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


/**
 * 
 */
package com.execue.nlp.bean.entity;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import com.execue.core.configuration.EnumLookupHelper;
import com.execue.core.type.IBaseEnumType;

/**
 * @author Nihar
 */
public enum InflectionType implements IBaseEnumType{ 
   SINGULAR ("singular"), PLURAL ("plural"), VERB_TENSE ("verbTense");

   private String                                   value;
   private static final Map<String, InflectionType> reverseLookupMap = new HashMap<String, InflectionType>();
   private static String                            name             = InflectionType.class.getSimpleName();

   static {
      for (InflectionType inflectionType : EnumSet.allOf(InflectionType.class)) {
         reverseLookupMap.put(inflectionType.value, inflectionType);
      }
   }

   InflectionType (String className) {
      this.value = className;
   }

   public String getValue () {
      return this.value;
   }

   public static InflectionType getInflectionType (String value) {
      return reverseLookupMap.get(value);
   }

   public String getDescription () {
      return EnumLookupHelper.getEnumLookupDescription(name, String.valueOf(this.value));
   }
}
