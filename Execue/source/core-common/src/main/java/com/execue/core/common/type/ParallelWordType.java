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
 * The value should be in sync with RecognitionType as they both should always be in sync with what ever is common in
 * both of them
 * 
 * @author Raju Gottumukkala
 */
public enum ParallelWordType implements IBaseEnumType{
   DEFAULT (1), Synonym (3), Abbreviation (4), Acronym (5), Code (6), // Indiana - IN
   Tag (7), RelatedWord (9), Inflection (11), // Director - DirectedBy
   LinguisticRoot (10); // To take care of matching by root of the word at Language

   private int                                         value;
   private static final Map<Integer, ParallelWordType> reverseLookupMap = new HashMap<Integer, ParallelWordType>();
   private static String                               name             = ParallelWordType.class.getSimpleName();

   static {
      for (ParallelWordType parallelWordType : EnumSet.allOf(ParallelWordType.class)) {
         reverseLookupMap.put(new Integer(parallelWordType.value), parallelWordType);
      }
   }

   ParallelWordType (Integer value) {
      this.value = value;
   }

   public Integer getValue () {
      return this.value;
   }

   public static ParallelWordType getType (Integer value) {
      return reverseLookupMap.get(value);
   }

   public String getDescription () {
      return EnumLookupHelper.getEnumLookupDescription(name, String.valueOf(this.value));
   }
}
