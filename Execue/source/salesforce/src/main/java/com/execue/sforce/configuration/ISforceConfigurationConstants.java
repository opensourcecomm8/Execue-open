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


package com.execue.sforce.configuration;

/**
 * This interface contains the constants from the sforce configuration file
 * 
 * @author Vishay
 * @version 1.0
 * @since 11/08/09
 */
public interface ISforceConfigurationConstants {

   String DATA_BATCH_SIZE_KEY                 = "sforce.static-values.data-batch-size";
   String DEFAULT_TARGET_DATA_SOURCE_NAME_KEY = "sforce.static-values.target-datasource-name";
   String DEFAULT_SFORCE_JOB_NAME_KEY         = "sforce.static-values.sforce-job-name";
   String DEFAULT_SFORCE_JOB_GROUP_KEY        = "sforce.static-values.sforce-job-group";
}
