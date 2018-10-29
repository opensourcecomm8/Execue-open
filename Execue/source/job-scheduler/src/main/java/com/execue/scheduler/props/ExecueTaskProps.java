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


package com.execue.scheduler.props;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jayadev Trigger Properties specific to ExeCue product
 */
public class ExecueTaskProps {

   // Possible combinations of jobs schedules
   public static final String              ADD_NONE                     = "Add new job without any schedules";
   public static final String              ADD_ADD                      = "Add new job with a new schedule";
   // TODO - JP - how can we add multiple schedules at once, for a new job
   public static final String              RETAIN_ADD                   = "Retain existing job, add a new schedule";
   // TODO - JP - how can we add multiple schedules at once, to an existing job

   public static final String              RETAIN_DELETEALL             = "Retain existing job, delete all associated schedules";
   public static final String              RETAIN_DELETE                = "Retain existing job, delete mentioned schedules";
   public static final String              DELETE_NONE                  = "Delete existing job, that wipes out all the associated schedules";
   public static final String              BAD_COMBINATION              = "Bad Combination of job and schedule";

   public static final String              SCHEDULE_NAME_SEPARATOR      = ",";
   public static final String              OPERATION_SCHEDULE_SEPARATOR = "=";

   // things that we set (as well as get) on a task
   public static final String              JOB_NAME                     = "job_name";
   public static final String              JOB_GROUP                    = "job_group";
   public static final String              JOB_TYPE                     = "job_type";                                                        // scheduled
   // or
   // on-demand
   public static final String              jOB_DESCRIPTION              = "job_description";
   public static final String              JOB_CLASS                    = "job_class";

   public static final String              SCHEDULED_JOB                = "scheduled_job";
   public static final String              ONDEMAND_JOB                 = "ondemand_job";
   public static final String              UNSCHEDULED_JOB              = "unscheduled_job";

   public static final String              TRIGGER_NAME_EXTN            = "_trigger";

   public static Map<String, List<Object>> jobInfoMap                   = new HashMap<String, List<Object>>();

   // keys for objects stored in jobdetail and triggerdetail jobDataMaps
   public static final String              EXJOB_DATA                   = "EXJOB_data";
   public static final String              EXJOB_SCHEDULE               = "EXJOB_schedule";

   public static final int                 MONTHLY_SCHEDULE             = 1;
   public static final int                 WEEKLY_SCHEDULE              = 2;
   public static final int                 DAILY_SCHEDULE               = 3;
   public static final int                 HOURLY_SCHEDULE              = 4;
   public static final int                 MINUTELY_SCHEDULE            = 5;
   public static final int                 START_TIME                   = 6;
   public static final int                 END_TIME                     = 7;
   public static final int                 REPEATS                      = 8;

   // Status Types //

   /** Task has not been run and is not currently running. */
   public static final int                 TASK_PENDING                 = 1;

   /** Task is currently being run by a task thread (or was running when the thread died). */
   public static final int                 TASK_RUNNING                 = 3;

   /** Task completed without any known issues. */
   public static final int                 TASK_COMPLETED               = 4;

   /** Task completed after an exception was thrown. */
   public static final int                 TASK_FAILED                  = 5;

   public static final int                 SUNDAY                       = 1;
   public static final int                 MONDAY                       = 2;
   public static final int                 TUESDAY                      = 3;
   public static final int                 WEDNESDAY                    = 4;
   public static final int                 THURSDAY                     = 5;
   public static final int                 FRIDAY                       = 6;
   public static final int                 SATURDAY                     = 7;

   public static final String[]            EX_MONTHS                    = { "January", "February", "March", "April",
            "May", "June", "July", "August", "September", "October", "November", "December" };

   public static final int[]               EX_DAYS_OF_WEEK_INT          = { SUNDAY, MONDAY, TUESDAY, WEDNESDAY,
            THURSDAY, FRIDAY, SATURDAY                                 };

   public static final String[]            EXDAYS_OF_WEEK               = { "Sunday", "Monday", "Tuesday", "Wednesday",
            "Thursday", "Friday", "Saturday"                           };

   public static final String[]            EX_DAYS_OF_MONTH             = { "1", "2", "3", "4", "5", "6", "7", "8",
            "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26",
            "27", "28", "29", "30", "31"                               };

   public static final String[]            EX_HOURLY_FREQUENCY          = { "0", "1", "2", "3", "4", "5", "6", "7",
            "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" };

   public static final String[]            EX_MINUTELY_FREQUENCY        = { "0", "1", "2", "3", "4", "5", "6", "7",
            "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25",
            "26", "27", "28", "29", "30", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53",
            "54", "55", "56", "57", "58", "59"                         };

   public static final String[]            EX_FREQUENCY                 = { "1", "2", "3", "4", "5", "6", "7", "8",
            "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" };

   private static List<Object> newList (Object[] objArray) {
      List<Object> returnList = new ArrayList<Object>();
      for (int i = 0; i < objArray.length; i++) {
         returnList.add(objArray[i]);
      }
      return returnList;
   }
}
