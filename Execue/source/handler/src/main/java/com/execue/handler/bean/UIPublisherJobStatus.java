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


package com.execue.handler.bean;

/**
 * @author JTiwari
 * @since 03/11/2009
 */
public class UIPublisherJobStatus extends UIJobRequestStatus {

   private String appName;
   private String dataSetName;
   private String fileName;
   private String remark;

   /**
    * @return the appName
    */
   public String getAppName () {
      return appName;
   }

   /**
    * @param appName
    *           the appName to set
    */
   public void setAppName (String appName) {
      this.appName = appName;
   }

   /**
    * @return the dataSetName
    */
   public String getDataSetName () {
      return dataSetName;
   }

   /**
    * @param dataSetName
    *           the dataSetName to set
    */
   public void setDataSetName (String dataSetName) {
      this.dataSetName = dataSetName;
   }

   /**
    * @return the fileName
    */
   public String getFileName () {
      return fileName;
   }

   /**
    * @param fileName
    *           the fileName to set
    */
   public void setFileName (String fileName) {
      this.fileName = fileName;
   }

   /**
    * @return the remark
    */
   public String getRemark () {
      return remark;
   }

   /**
    * @param remark
    *           the remark to set
    */
   public void setRemark (String remark) {
      this.remark = remark;
   }

}
