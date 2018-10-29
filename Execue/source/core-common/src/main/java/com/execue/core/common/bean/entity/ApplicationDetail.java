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

/**
 * This class represents the Application object.
 */

import java.io.Serializable;
import java.sql.Blob;

public class ApplicationDetail implements Serializable {

   private static final long serialVersionUID = 1L;
   private Long              id;
   private Long              applicationId;
   private Blob              imageData;
   private String            imageName;
   private String            imageType;

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public Long getApplicationId () {
      return applicationId;
   }

   public void setApplicationId (Long applicationId) {
      this.applicationId = applicationId;
   }

   public Blob getImageData () {
      return imageData;
   }

   public void setImageData (Blob imageData) {
      this.imageData = imageData;
   }

   public String getImageName () {
      return imageName;
   }

   public void setImageName (String imageName) {
      this.imageName = imageName;
   }

   public String getImageType () {
      return imageType;
   }

   public void setImageType (String imageType) {
      this.imageType = imageType;
   }

}
