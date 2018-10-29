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


package com.execue.scheduler;

import com.execue.scheduler.jobdata.ExecueJobData;
import com.execue.scheduler.jobdata.ExecueJobSchedule;
import com.execue.scheduler.props.ExecueTaskProps;

/**
 * 
 * Provided helper methods to delete jobs, add schedules on the 
 * scheduler UI for audit jobs
 * 
 * @author jaydev
 *
 */
public class SchedulerHelper{
	/**
     * 
     * This methods adds the schedule for the selected job
     * The schedule can be monthly, weekly, daily or hourly
     * 
     * Start and End dates may be mentioned, forming the boundaries for the 
     * schedule
     */
/*    public void addSchedule(ExecueJobData jobData, ExecueJobSchedule jobSchedule) {
    	
		try {
    		jobSchedule = new ExecueJobSchedule();	    		
    		 
    		//prepare the jobdata here
    	
   			fillScheduleInfo(jobData, jobSchedule);
			ExecueJobSchedulerServiceImpl.scheduleJob(jobData, jobSchedule, null);
			
		} catch (Exception e) {
			
		}
    }*/
    
    /**
     * This method fills in the basic schedule info for any 
     * type of job; After calling this, its up to the specific
     * requirement to fill in the appropriate JobData
     * 

     * @param jobData - jobdata needed for execution
     * @param jobSchedule - jobschedule details that come from the UI
     */
    private void fillScheduleInfo(ExecueJobData jobData, ExecueJobSchedule jobSchedule) throws Exception {
    	
		/*int monthDay = -1;
		int hourSelected = -1;
		int minuteSelected = -1;
		int freqSelected = -1;
		String dateTimeSelected = DateUtil.formatDateTime(new Date());
		
		int scheduleType = getIntParameter(req, "schedule_types");
		
		if(scheduleType == ExecueTaskProps.MONTHLY_SCHEDULE) {
			monthDay = getIntParameter(req, "day_types");
			hourSelected = getIntParameter(req, "hourly_freq_types");
			minuteSelected = getIntParameter(req, "minutely_freq_types");
			
			jobSchedule.setPeriodicity(ExecueTaskProps.MONTHLY_SCHEDULE);
    		jobSchedule.setDayOfMonth(monthDay);
    		jobSchedule.setHourOfDay(hourSelected);
    		jobSchedule.setMinuteOfHour(minuteSelected);
			
		} else if(scheduleType == ExecueTaskProps.WEEKLY_SCHEDULE) {
			monthDay = getIntParameter(req, "week_day_types");
			hourSelected = getIntParameter(req, "hourly_freq_types");
			minuteSelected = getIntParameter(req, "minutely_freq_types");
			
			//prepare the job schedule
    		
    		jobSchedule.setPeriodicity(ExecueTaskProps.WEEKLY_SCHEDULE);
    		jobSchedule.setDayOfWeek(monthDay);
    		jobSchedule.setHourOfDay(hourSelected);
    		jobSchedule.setMinuteOfHour(minuteSelected);
			
		} else if(scheduleType == ExecueTaskProps.DAILY_SCHEDULE) {
			hourSelected = getIntParameter(req, "hourly_freq_types");
			minuteSelected = getIntParameter(req, "minutely_freq_types");
			
			jobSchedule.setPeriodicity(ExecueTaskProps.DAILY_SCHEDULE);
    		jobSchedule.setHourOfDay(hourSelected);
    		jobSchedule.setMinuteOfHour(minuteSelected);
			
		} else if(scheduleType == ExecueTaskProps.HOURLY_SCHEDULE) {
			freqSelected = getIntParameter(req, "freq_types");
			
			jobSchedule.setPeriodicity(ExecueTaskProps.HOURLY_SCHEDULE);
    		jobSchedule.setEveryNthHour(freqSelected);
			
		} else if(scheduleType == ExecueTaskProps.MINUTELY_SCHEDULE) {
			 dateTimeSelected = getParameter(req, "schedule_date_time");
			 //Date selectedDate = DateUtil.parseDate(dateTimeSelected, true);
			 jobSchedule.setPeriodicity(ExecueTaskProps.MINUTELY_SCHEDULE);
    	 	 jobSchedule.setStartTime(selectedDate);
    		 jobSchedule.setNoOfRepeats(0);
    		 Calendar cal = new GregorianCalendar();
    		 cal.setTime(selectedDate);
    		 jobSchedule.setHourOfDay(cal.get((Calendar.HOUR)));
    		 jobSchedule.setMinuteOfHour(cal.get((Calendar.MINUTE)));
		}
		
		// get start and end time if given by user otherwise no
		long startSchedule 		= getLongParameter(req, "start_schedule");
	    long endSchedule 			= getLongParameter(req, "end_schedule");
	    
	    Date startDate = null;
	    Date endDate = null;
	    
	    if(startSchedule != -1) {
	    	String startTimeSelected = getParameter(req, "start_date_time");
	    	startDate = DateUtil.parseDate(startTimeSelected, true);
	    }
	    
	    if(endSchedule != -1) {
	    	String endTimeSelected = getParameter(req, "end_date_time");
	    	endDate = DateUtil.parseDate(endTimeSelected, true);
	    }
	    
	    if(endDate != null) {
			if(((startDate != null)) && (endDate.getTime() < startDate.getTime())) {
				throw new Exception("End Time cannot be set before Start Time");
			}
			
			if((endDate.getTime() < new Date().getTime())) {
				throw new Exception("End Time cannot be in the past");
			}
		}
	    
	    jobData.setDateAdded(new Date());
	    
	    if(startDate != null){
			if(startDate.getTime() < (new Date().getTime())) {
				throw new Exception("Start Time cannot be in the past");
			}
			
			jobSchedule.setStartTime(startDate);
			jobData.setExecutionTime(jobSchedule.getStartTime());
		}
	
	    if(endDate != null){
			jobSchedule.setEndTime(endDate);
		}*/
    }
   
    private static int getIndexOfMonth(String month)
    {
    	int index = -1;
    	
    	String[] months = ExecueTaskProps.EX_MONTHS;
    	for(int i=0; i<months.length; i++)
    	{
    		if(month.equals(months[i]))
    			return i+1;
    	}
    	
    	return index;
    }
    
    private static int getIndexOfWeek(String week)
    {
    	int index = -1;
    	
    	String[] weeks = ExecueTaskProps.EXDAYS_OF_WEEK;
    	for(int i=0; i<weeks.length; i++)
    	{
    		if(week.equals(weeks[i]))
    			return i+1;
    	}
    	
    	return index;
    }

}