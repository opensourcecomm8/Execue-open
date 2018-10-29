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


package com.execue.core.common.bean.graph;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import com.execue.core.configuration.EnumLookupHelper;
import com.execue.core.type.IBaseEnumType;

/**
 * @author Abhijit
 * @since Sep 15, 2008 - 12:14:22 PM
 */
public enum GraphComponentType implements IBaseEnumType  {
   Vertex (1), Edge (2);

   private Integer                                       value;
   private static final Map<Integer, GraphComponentType> reverseLookupMap = new HashMap<Integer, GraphComponentType>();
   private static String                                 name             = GraphComponentType.class.getSimpleName();

   static {
      for (GraphComponentType wordType : EnumSet.allOf(GraphComponentType.class)) {
         reverseLookupMap.put(wordType.value, wordType);
      }
   }

   GraphComponentType (Integer value) {
      this.value = value;
   }

   public Integer getValue () {
      return this.value;
   }

   public static GraphComponentType getWordType (Integer value) {
      return reverseLookupMap.get(value);
   }

   public String getDescription () {
      return EnumLookupHelper.getEnumLookupDescription(name, String.valueOf(this.value));
   }
}
