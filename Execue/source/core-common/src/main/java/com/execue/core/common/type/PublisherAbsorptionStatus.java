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
 * @since 4.0
 */
public enum PublisherAbsorptionStatus  implements IBaseEnumType{

   STARTED (0), // File upload started
   FILE_SAVED (9), // File saved at server end

   TEMP_META_LOAD_FAILED (11), // Temporary table creation failed
   TEMP_DATA_LOAD_FAILED (12), // Temporary table data load failed
   TEMP_LOADED (13), // Temporary table loaded with data
   ANALISYS_FAILED (14), // Analysis on temporary table failed
   ANALYZED (19), // Data from temporary table analyzed

   EVAL_META_LOAD_FAILED (21), // Evaluated table creation failed
   EVAL_DATA_LOAD_FAILED (22), // Evaluated table data load failed
   EVAL_LOADED (29), // Evaluated table loaded with data

   ASSET_ANALYSIS_FAILED (31), // Failed while analyzing the virtual lookups
   ASSET_ABSORB_FAILED (32), // Asset absorption failed
   ASSET_ABSORBED (39), // Asset absorbed successfully

   MODEL_ABSORB_FAILED (41), // Model extraction failed
   MODEL_ABSORBED (49), // Model extracted

   ASSET_READY (75); // Asset is ready for searching

   private int                                                  value;
   private static final Map<Integer, PublisherAbsorptionStatus> reverseLookupMap = new HashMap<Integer, PublisherAbsorptionStatus>();
   private static String                                        name             = PublisherAbsorptionStatus.class
                                                                                          .getSimpleName();

   static {
      for (PublisherAbsorptionStatus absorptionStatus : EnumSet.allOf(PublisherAbsorptionStatus.class)) {
         reverseLookupMap.put(absorptionStatus.value, absorptionStatus);
      }
   }

   PublisherAbsorptionStatus (int value) {
      this.value = value;
   }

   public Integer getValue () {
      return this.value;
   }

   public static PublisherAbsorptionStatus getType (int value) {
      return reverseLookupMap.get(value);
   }

   public String getDescription () {
      return EnumLookupHelper.getEnumLookupDescription(name, String.valueOf(this.value));
   }
}
