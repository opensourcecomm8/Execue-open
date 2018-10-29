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

/**
 * This class represents the PublishedFileInfoDetails object.
 * 
 * @author MurthySN
 * @version 1.0
 * @since 27/10/09
 */

public class PublishedFileInfoDetails implements Serializable {

   private static final long serialVersionUID = 1L;
   private Long              id;
   private String            propertyName;
   private String            propertyValue;
   private PublishedFileInfo publishedFileInfo;

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public PublishedFileInfo getPublishedFileInfo () {
      return publishedFileInfo;
   }

   public void setPublishedFileInfo (PublishedFileInfo publishedFileInfo) {
      this.publishedFileInfo = publishedFileInfo;
   }

   public String getPropertyName () {
      return propertyName;
   }

   public void setPropertyName (String propertyName) {
      this.propertyName = propertyName;
   }

   public String getPropertyValue () {
      return propertyValue;
   }

   public void setPropertyValue (String propertyValue) {
      this.propertyValue = propertyValue;
   }
}
