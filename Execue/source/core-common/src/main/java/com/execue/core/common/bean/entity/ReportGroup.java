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
 * @author kaliki
 * @since 4.0
 */
public class ReportGroup implements Serializable {

   private int             id;
   private String          name;
   private String          linkUrl;
   private String          imageUrl;
   private Set<ReportType> reportTypes;

   public ReportGroup () {
   }

   public ReportGroup (int id, String name, String linkUrl, String imageUrl) {
      this.id = id;
      this.name = name;
      this.linkUrl = linkUrl;
      this.imageUrl = imageUrl;
   }

   public int getId () {
      return this.id;
   }

   public void setId (int id) {
      this.id = id;
   }

   public String getName () {
      return this.name;
   }

   public void setName (String name) {
      this.name = name;
   }

   public String getLinkUrl () {
      return this.linkUrl;
   }

   public void setLinkUrl (String linkUrl) {
      this.linkUrl = linkUrl;
   }

   public String getImageUrl () {
      return this.imageUrl;
   }

   public void setImageUrl (String imageUrl) {
      this.imageUrl = imageUrl;
   }

   public Set<ReportType> getReportTypes () {
      return this.reportTypes;
   }

   public void setReportTypes (Set<ReportType> reportTypes) {
      this.reportTypes = reportTypes;
   }

}
