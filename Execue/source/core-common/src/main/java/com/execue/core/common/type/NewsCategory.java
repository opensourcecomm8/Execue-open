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
package com.execue.core.common.type;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import com.execue.core.configuration.EnumLookupHelper;
import com.execue.core.type.IBaseEnumType;

/**
 * @author Nitesh
 */
public enum NewsCategory implements IBaseEnumType{

   NEWS_NEWS ("News"), FINANCE_NEWS ("Finance"), SPORTS_NEWS ("Sports"), ENTERNMENT_NEWS ("Entertainment"), BUSINESS_NEWS (
            "Business"), GENERAL_NEWS ("General"), GOVERNMENT_NEWS ("Government"), CRAIGSLIST_NEWS ("Craigslist"), DEFAULT (
            "Default");

   private String                                 value;
   private static final Map<String, NewsCategory> reverseLookupMap = new HashMap<String, NewsCategory>();
   private static String                          name             = NewsCategory.class.getSimpleName();

   static {
      for (NewsCategory newsCategory : EnumSet.allOf(NewsCategory.class)) {
         reverseLookupMap.put(newsCategory.value, newsCategory);
      }
   }

   NewsCategory (String value) {
      this.value = value;
   }

   public String getValue () {
      return this.value;
   }

   public static NewsCategory getType (String value) {
      return reverseLookupMap.get(value);
   }

   public String getDescription () {
      return EnumLookupHelper.getEnumLookupDescription(name, String.valueOf(this.value));
   }
}
