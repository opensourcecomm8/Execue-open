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


/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */

package com.execue.core.common.type;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import com.execue.core.configuration.EnumLookupHelper;
import com.execue.core.type.IBaseEnumType;

/**
 * @author MurthySN
 */
public enum DataSourceType implements IBaseEnumType {
   CATALOG ("CATALOG"), UPLOADED ("UPLOADED"), REGULAR ("REGULAR"), SYSTEM_UNSTRUCTURED_WAREHOUSE ("SYSTEM_USWH"), SYSTEM_UNSTRUCTURED_CONTENT_AGGREGATOR (
            "SYSTEM_USCA"), SYSTEM_IP_DB ("SYSTEM_IP_DB"), SYSTEM_CITY_CENTER_ZIP_CODE ("SYSTEM_CCZC"), SYSTEM_SOLR (
            "SYSTEM_SOLR");

   private String                                   value;
   private static final Map<String, DataSourceType> reverseLookupMap = new HashMap<String, DataSourceType>();
   private static String                            name             = DataSourceType.class.getSimpleName();

   static {
      for (DataSourceType dataSourceType : EnumSet.allOf(DataSourceType.class)) {
         reverseLookupMap.put(dataSourceType.value, dataSourceType);
      }
   }

   DataSourceType (String value) {
      this.value = value;
   }

   public String getValue () {
      return this.value;
   }

   public static DataSourceType getType (String value) {
      return reverseLookupMap.get(value);
   }

   public String getDescription () {
      return EnumLookupHelper.getEnumLookupDescription(name, String.valueOf(this.value));
   }

}
