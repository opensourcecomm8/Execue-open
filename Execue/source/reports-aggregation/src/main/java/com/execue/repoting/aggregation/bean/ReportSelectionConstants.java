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


package com.execue.repoting.aggregation.bean;

/**
 * Contains all the constants used for Report Selection logic
 * 
 * @author John Mallavalli
 */
public class ReportSelectionConstants {

   // List of configuration parameters for report view selection
   public static int    LOW_RECORDS_LIMIT  = 650;
   public static int    HIGH_RECORDS_LIMIT = 1500;

   public static int    LOW_EFF_GROUPS     = 0;
   public static int    MED_EFF_GROUPS     = 1;
   public static int    HIGH_EFF_GROUPS    = 2;

   public static int    HIGH_GROUPS_SIZE   = 2;                   // Get the correct value

   public static int    LOW_DATA_POINTS    = 60;
   public static int    MED_DATA_POINTS    = 120;
   public static int    HIGH_DATA_POINTS   = 650;

   public static int    LOW_MEASURES_SIZE  = 10;                  // Get the correct value
   public static int    MED_MEASURES_SIZE  = 20;                  // Get the correct value

   // List of possible report views
   public static String CSV                = "CSV";
   public static String TABLE              = "TABLE";
   public static String PIVOT_TABLE        = "PIVOT_TABLE";
   public static String GROUP_TABLE        = "GROUP_TABLE";
   public static String CROSS_TABLE        = "CROSS_TABLE";
   public static String HIERARCHY_TABLE    = "HIERARCHY_TABLE";

   public static String BAR_CHART          = "BAR_CHART";
   public static String LINE_CHART         = "LINE_CHART";

   public static String C_BAR              = "C_BAR";
   public static String MULTI_BAR          = "MULTI_BAR";
   public static String CLUSTER_BAR        = "CLUSTER_BAR";
   public static String CMULTI_BAR         = "CMULTI_BAR";
   public static String CM_MULTI_BAR       = "CM_MULTI_BAR";

   public static String C_LINE             = "C_LINE";
   public static String BAR_LINE           = "BAR_LINE";
   public static String MULTI_LINE         = "MULTI_LINE";
   public static String CMULTI_LINE        = "CMULTI_LINE";
   public static String CM_MULTI_LINE      = "CM_MULTI_LINE";
   public static String CLUSTER_LINE       = "CLUSTER_LINE";
   public static String MULTI_LINE_CLUSTER = "MULTI_LINE_CLUSTER";
}