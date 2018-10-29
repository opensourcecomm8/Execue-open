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


package com.execue.sforce.bean.type;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * This represents the Soap data types available
 * 
 * @author Vishay
 * @version 1.0
 * @since 17/08/09
 */
public enum SoapColumnDataType {
   STRING ("xsd:string"), ID ("tns:ID"), DOUBLE ("xsd:double"), INT ("xsd:int"), DATETIME ("xsd:dateTime"), DATE (
            "xsd:date"), TIME ("xsd:time"), BOOLEAN ("xsd:boolean"), BINARY ("xsd:base64Binary");

   private String                                       value;
   private static final Map<String, SoapColumnDataType> reverseLookupMap = new HashMap<String, SoapColumnDataType>();

   static {
      for (SoapColumnDataType columnType : EnumSet.allOf(SoapColumnDataType.class)) {
         reverseLookupMap.put(columnType.value, columnType);
      }
   }

   SoapColumnDataType (String value) {
      this.value = value;
   }

   public String getValue () {
      return this.value;
   }

   public static SoapColumnDataType getSoapColumnType (String value) {
      return reverseLookupMap.get(value);
   }
}
