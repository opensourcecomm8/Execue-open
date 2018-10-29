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
 * 
 * @author jitendra
 *
 */
public enum UnstructuredColumnNameType implements IBaseEnumType{

   DISP_S1 ("DISP_S1"), DISP_N1 ("DISP_N1"), DISP_S2 ("DISP_S2"), DISP_N2 ("DISP_N2"), DISP_S3 ("DISP_S3"), DISP_N3 (
            "DISP_N3"), DISP_S4 ("DISP_S4"), DISP_N4 ("DISP_N4"), DISP_S5 ("DISP_S5"), DISP_N5 ("DISP_N5"), DISP_S6 (
            "DISP_S6"), DISP_N6 ("DISP_N6"), DISP_S7 ("DISP_S7"), DISP_N7 ("DISP_N7"), DISP_S8 ("DISP_S8"), DISP_N8 (
            "DISP_N8"), DISP_S9 ("DISP_S9"), DISP_N9 ("DISP_N9"), DISP_S10 ("DISP_S10"), DISP_N10 ("DISP_N10");

   private String                                               value;
   private static final Map<String, UnstructuredColumnNameType> reverseLookupMap = new HashMap<String, UnstructuredColumnNameType>();
   private static String                                        name             = UnstructuredColumnNameType.class
                                                                                          .getSimpleName();

   static {
      for (UnstructuredColumnNameType unstructuredColumnNameType : EnumSet.allOf(UnstructuredColumnNameType.class)) {
         reverseLookupMap.put(unstructuredColumnNameType.value, unstructuredColumnNameType);
      }
   }

   UnstructuredColumnNameType (String value) {
      this.value = value;
   }

   public String getValue () {
      return this.value;
   }

   public static UnstructuredColumnNameType getType (String value) {
      return reverseLookupMap.get(value);
   }

   public String getDescription () {
      return EnumLookupHelper.getEnumLookupDescription(name, String.valueOf(this.value));
   }
}
