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
 * @author Nitesh
 */
public enum AnswersCatalogOperationType implements IBaseEnumType {
   CUBE_CREATION ("CUBE_CREATION"), CUBE_UPDATION ("CUBE_UPDATION"), CUBE_REFRESH ("CUBE_REFRESH"), CUBE_DELETION (
            "CUBE_DELETION"), MART_CREATION ("MART_CREATION"), MART_UPDATION ("MART_UPDATION"), MART_REFRESH (
            "MART_REFRESH"), MART_DELETION ("MART_DELETION"), PARENT_ASSET_SYNC_ABSORPTION (
            "PARENT_ASSET_SYNC_ABSORPTION"), ASSET_DELETION ("ASSET_DELETION");

   private String                                                value;
   private static final Map<String, AnswersCatalogOperationType> reverseLookupMap = new HashMap<String, AnswersCatalogOperationType>();
   private static String                                         name             = AnswersCatalogOperationType.class
                                                                                           .getSimpleName();

   static {
      for (AnswersCatalogOperationType answersCatalogOperationType : EnumSet.allOf(AnswersCatalogOperationType.class)) {
         reverseLookupMap.put(answersCatalogOperationType.value, answersCatalogOperationType);
      }
   }

   AnswersCatalogOperationType (String value) {
      this.value = value;
   }

   public String getValue () {
      return this.value;
   }

   public static AnswersCatalogOperationType getType (String value) {
      return reverseLookupMap.get(value);
   }

   public String getDescription () {
      return EnumLookupHelper.getEnumLookupDescription(name, String.valueOf(this.value));
   }
}
