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
 * @author Laura
 * 
 * 
 */
public enum TripleVariationSubType implements IBaseEnumType {
   SUBJECTINSTANCE ("1"), OBJECTINSTANCE ("2"), RELATION ("3"), SUBJECTINSTANCE_RELATION ("4"), RELATION_OBJECTINSTANCE (
            "5"), SUBJECTINSTANCE_OBJECTINSTANCE ("6"), SUBJECTINSTANCE_RELATION_OBJECTINSTANCE ("7"), SUBJECTINSTANCE_RELATION_OBJECTCONCEPT (
            "8"), SUBJECTCONCEPT_RELATION_OBJECTINSTANCE ("9"), SUBJECTCONCEPT_RELATION_OBJECTCONCEPT ("10"), SUBJECTCONCEPT_RELATION (
            "11"), SUBJECTCONCEPT_OBJECTINSTANCE ("12"), SUBJECTINSTANCE_OBJECTCONCEPT ("13"), RELATION_OBJECTCONCEPT (
            "14"), SUBJECTCONCEPT_OBJECTCONCEPT ("15"), SUBJECTCONCEPT ("16"), OBJECTCONCEPT ("17");

   private String                                           value;
   private static final Map<String, TripleVariationSubType> reverseLookupMap = new HashMap<String, TripleVariationSubType>();
   private static String                                    name             = TripleVariationSubType.class
                                                                                      .getSimpleName();

   static {
      for (TripleVariationSubType variationSubType : EnumSet.allOf(TripleVariationSubType.class)) {
         reverseLookupMap.put(variationSubType.value, variationSubType);
      }
   }

   TripleVariationSubType (String value) {
      this.value = value;
   }

   public String getValue () {
      return this.value;
   }

   public static TripleVariationSubType getType (String value) {
      return reverseLookupMap.get(value);
   }

   public String getDescription () {
      return EnumLookupHelper.getEnumLookupDescription(name, String.valueOf(this.value));
   }
}
