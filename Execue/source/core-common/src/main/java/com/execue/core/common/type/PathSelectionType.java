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
 * @author Nihar Agrawal
 */
public enum PathSelectionType implements IBaseEnumType{
   NORMAL_PATH (1), DEFAULT_VALUE_PATH (2);

   private Integer                                      value;
   private static final Map<Integer, PathSelectionType> reverseLookupMap = new HashMap<Integer, PathSelectionType>();

   static {
      for (PathSelectionType pathSelectionType : EnumSet.allOf(PathSelectionType.class)) {
         reverseLookupMap.put(pathSelectionType.value, pathSelectionType);
      }
   }

   PathSelectionType (Integer value) {
      this.value = value;
   }

   public Integer getValue () {
      return this.value;
   }

   public static PathSelectionType getType (Integer value) {
      return reverseLookupMap.get(value);
   }
}
