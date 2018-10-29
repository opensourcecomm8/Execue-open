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
 * @author Vishay
 */
public enum NotificationParamName implements IBaseEnumType{
   APPLICATION_NAME ("ApplicationName"), ASSET_NAME ("AssetName"), TABLE_NAME ("TableName"), COLUMN_NAME ("ColumnName"), TIME_STAMP (
            "TimeStamp"), FILE_NAME ("FileName"), OPERATION ("Operation"), MODEL_NAME ("ModelName");

   private String                                          value;
   private static final Map<String, NotificationParamName> reverseLookupMap      = new HashMap<String, NotificationParamName>();
   private static final Map<String, NotificationParamName> typeLookupByStringMap = new HashMap<String, NotificationParamName>();
   private static String                                   name                  = NotificationParamName.class
                                                                                          .getSimpleName();

   static {
      for (NotificationParamName notificationParamName : EnumSet.allOf(NotificationParamName.class)) {
         reverseLookupMap.put(notificationParamName.value, notificationParamName);
         typeLookupByStringMap.put(notificationParamName.toString(), notificationParamName);
      }
   }

   NotificationParamName (String value) {
      this.value = value;
   }

   public String getValue () {
      return this.value;
   }

   public static NotificationParamName getType (String value) {
      return reverseLookupMap.get(value);
   }

   public String getDescription () {
      return EnumLookupHelper.getEnumLookupDescription(name, String.valueOf(this.value));
   }

   public static NotificationParamName getTypeByLiteral (String literal) {
      return typeLookupByStringMap.get(literal);
   }
}
