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
 * @author Raju Gottumukkala
 */
public enum RecognitionType implements IBaseEnumType {
   Exact (1), DisplayName (2), Synonym (3), Abbreviation (4), Acronym (5), Code (6), // Indiana - IN
   Tag (7), Description (8), RelatedWord (9), // Director - DirectedBy
   LinguisticRoot (10), // To take care of matching by root of the word at Language
   EntityVariant (11);

   private int                                        value;
   private static final Map<Integer, RecognitionType> reverseLookupMap = new HashMap<Integer, RecognitionType>();
   private static String                              name             = RecognitionType.class.getSimpleName();

   static {
      for (RecognitionType querySectionType : EnumSet.allOf(RecognitionType.class)) {
         reverseLookupMap.put(new Integer(querySectionType.value), querySectionType);
      }
   }

   RecognitionType (int value) {
      this.value = value;
   }

   public Integer getValue () {
      return this.value;
   }

   public static RecognitionType getType (Integer value) {
      return reverseLookupMap.get(new Integer(value));
   }

   public String getDescription () {
      return EnumLookupHelper.getEnumLookupDescription(name, String.valueOf(this.value));
   }
}
