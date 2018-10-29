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


package com.execue.core.common.bean.entity;

import java.io.Serializable;
import java.util.Set;

/**
 * This class represents the PublisherTableInfo object.
 * 
 * @author MurthySN
 * @version 1.0
 * @since 27/10/09
 */

public class PublishedFileTableInfo implements Serializable {

   private static final long              serialVersionUID = 1L;
   private Long                           id;
   private String                         workSheetName;
   private String                         tempTableName;
   private String                         evaluatedTableName;
   private String                         displayTableName;
   private PublishedFileInfo              publishedFileInfo;
   private Set<PublishedFileTableDetails> publishedFileTableDetails;

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public String getWorkSheetName () {
      return workSheetName;
   }

   public void setWorkSheetName (String workSheetName) {
      this.workSheetName = workSheetName;
   }

   public String getTempTableName () {
      return tempTableName;
   }

   public void setTempTableName (String tempTableName) {
      this.tempTableName = tempTableName;
   }

   public String getEvaluatedTableName () {
      return evaluatedTableName;
   }

   public void setEvaluatedTableName (String evaluatedTableName) {
      this.evaluatedTableName = evaluatedTableName;
   }

   public PublishedFileInfo getPublishedFileInfo () {
      return publishedFileInfo;
   }

   public void setPublishedFileInfo (PublishedFileInfo publishedFileInfo) {
      this.publishedFileInfo = publishedFileInfo;
   }

   public Set<PublishedFileTableDetails> getPublishedFileTableDetails () {
      return publishedFileTableDetails;
   }

   public void setPublishedFileTableDetails (Set<PublishedFileTableDetails> publishedFileTableDetails) {
      this.publishedFileTableDetails = publishedFileTableDetails;
   }

   /**
    * @return the displayTableName
    */
   public String getDisplayTableName () {
      return displayTableName;
   }

   /**
    * @param displayTableName
    *           the displayTableName to set
    */
   public void setDisplayTableName (String displayTableName) {
      this.displayTableName = displayTableName;
   }

}
