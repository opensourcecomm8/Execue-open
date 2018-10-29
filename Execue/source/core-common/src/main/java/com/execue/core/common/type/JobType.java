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
 * This enum represents the job type
 * 
 * @author Vishay
 * @version 1.0
 * @since 26/06/09
 */
public enum JobType implements IBaseEnumType {
   CUBE_CREATION (1), CUBE_UPDATION (2), MART_CREATION (3), MART_UPDATION (4), SFORCE_DATA_REPLICATION (5), SFL_TERM_TOKEN_WEIGHT_UPDATION (
            6), RI_PARALLELWORD_UPDATION (7), POPULARITY_HIT_UPDATION (8), PUBLISHER_DATA_ABSORPTION (9), PUBLISHER_DATA_EVALUATION (
            10), FILE_ONTOLOGY_DATA_ABSORPTION (11), RI_ONTO_TERMS_ABSORPTION (12), SNOW_FLAKES_TERMS_ABSORPTION (13), CORRECT_MAPPINGS (
            14), APPLICATION_DELETION (16), ASSET_DELETION (17), POPULARITY_COLLECTION (18), POPULARITY_DISPERSION (19), MEMBER_ABSORPTION (
            21), RI_ONTO_TERM_POPULARITY_HIT_UPDATION (22), BUSINESS_MODEL_PREPARATION (23), PUBLISH_ASSET (24), INDEX_FORM_MANAGEMENT (
            25), INSTANCE_ABSORPTION (26), DEFAULT_METRICS_POPULATION (27), SFL_WEIGHT_UPDATION_BY_SECWORD (28), CONCEPT_TYPE_ASSOCIATION (
            29), SDX_SYNCHRONIZATION (30), RUNTIME_TABLES_CLEANUP (31), SCHEDULED_POPULARITY_HIT_MAINTENANCE (32), EAS_INDEX_REFRESH (
            33), VERTICAL_POPULARITY_UPDATION (34), CRAIGSLIST_RUNTIME_TABLES_CLEANUP (35), FEATURE_COUNT (36), CUBE_REFRESH (
            37), MART_REFRESH (38), PARENT_ASSET_SYNCHRONIZATION (39), ANSWER_CATALOG_MANAGEMENT_QUEUE (40), OPTIMAL_DSET (
            41), SCHEDULED_OPTIMAL_DSET (42);

   private Integer                            value;
   private static final Map<Integer, JobType> reverseLookupMap = new HashMap<Integer, JobType>();
   private static String                      name             = JobType.class.getSimpleName();

   static {
      for (JobType jobType : EnumSet.allOf(JobType.class)) {
         reverseLookupMap.put(jobType.value, jobType);
      }
   }

   JobType (Integer value) {
      this.value = value;
   }

   public Integer getValue () {
      return this.value;
   }

   public static JobType getType (Integer value) {
      return reverseLookupMap.get(value);
   }

   public String getDescription () {
      return EnumLookupHelper.getEnumLookupDescription(name, String.valueOf(this.value));
   }
}
