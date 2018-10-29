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


package com.execue.core.common.bean.publisher;

import com.csvreader.CsvReader;

/**
 * This bean represents the publisher context
 * 
 * @author Vishay
 * @since 20/10/09
 * @version 1.0
 */
public class PublisherMetaReadContext {

   private Long      applicationId;
   private Long      userId;
   private boolean   isColumnsAvailable;
   private String    displayFileName;
   private String    fileName;
   private CsvReader csvReader;
   private String    originalFileName;
   private String    normalizedFileName;

   public Long getApplicationId () {
      return applicationId;
   }

   public void setApplicationId (Long applicationId) {
      this.applicationId = applicationId;
   }

   public Long getUserId () {
      return userId;
   }

   public void setUserId (Long userId) {
      this.userId = userId;
   }

   public boolean isColumnsAvailable () {
      return isColumnsAvailable;
   }

   public void setColumnsAvailable (boolean isColumnsAvailable) {
      this.isColumnsAvailable = isColumnsAvailable;
   }

   public String getFileName () {
      return fileName;
   }

   public void setFileName (String fileName) {
      this.fileName = fileName;
   }

   public CsvReader getCsvReader () {
      return csvReader;
   }

   public void setCsvReader (CsvReader csvReader) {
      this.csvReader = csvReader;
   }

   public String getOriginalFileName () {
      return originalFileName;
   }

   public void setOriginalFileName (String originalFileName) {
      this.originalFileName = originalFileName;
   }

   public String getNormalizedFileName () {
      return normalizedFileName;
   }

   public void setNormalizedFileName (String normalizedFileName) {
      this.normalizedFileName = normalizedFileName;
   }

   /**
    * @return the displayFileName
    */
   public String getDisplayFileName () {
      return displayFileName;
   }

   /**
    * @param displayFileName
    *           the displayFileName to set
    */
   public void setDisplayFileName (String displayFileName) {
      this.displayFileName = displayFileName;
   }

}
