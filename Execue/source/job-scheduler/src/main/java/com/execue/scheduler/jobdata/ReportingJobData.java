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


package com.execue.scheduler.jobdata;

/**
 * Class representing job data entity for scheduler purpose Used for saving data to be used for running excel generation
 * and email of file
 * 
 * @author jayadev
 */
public class ReportingJobData extends ExecueJobData {

   /**
    * Generated Serial Version ID
    */
   private static final long serialVersionUID = -6645411771597688775L;

   private String            fromEmailId;
   private String            toEmailId;
   private String            emailSubject;
   private String            emailBody;

   private long              reportId;
   private long              companyId;                               // not required currently but from future point
                                                                        // of view

   /**
    * @return the serialVersionUID
    */
   public static long getSerialVersionUID () {
      return serialVersionUID;
   }

   /**
    * @return the companyId
    */
   public long getCompanyId () {
      return companyId;
   }

   /**
    * @param companyId
    *           the companyId to set
    */
   public void setCompanyId (long companyId) {
      this.companyId = companyId;
   }

   /**
    * @return the emailBody
    */
   public String getEmailBody () {
      return emailBody;
   }

   /**
    * @param emailBody
    *           the emailBody to set
    */
   public void setEmailBody (String emailBody) {
      this.emailBody = emailBody;
   }

   /**
    * @return the emailSubject
    */
   public String getEmailSubject () {
      return emailSubject;
   }

   /**
    * @param emailSubject
    *           the emailSubject to set
    */
   public void setEmailSubject (String emailSubject) {
      this.emailSubject = emailSubject;
   }

   /**
    * @return the fromEmailId
    */
   public String getFromEmailId () {
      return fromEmailId;
   }

   /**
    * @param fromEmailId
    *           the fromEmailId to set
    */
   public void setFromEmailId (String fromEmailId) {
      this.fromEmailId = fromEmailId;
   }

   /**
    * @return the reportId
    */
   public long getReportId () {
      return reportId;
   }

   /**
    * @param reportId
    *           the reportId to set
    */
   public void setReportId (long reportId) {
      this.reportId = reportId;
   }

   /**
    * @return the toEmailId
    */
   public String getToEmailId () {
      return toEmailId;
   }

   /**
    * @param toEmailId
    *           the toEmailId to set
    */
   public void setToEmailId (String toEmailId) {
      this.toEmailId = toEmailId;
   }
}
