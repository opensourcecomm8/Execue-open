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

public class CannedReport implements Serializable {

   private Long   id;
   private String name;
   private String queryString;
   private String headerXML;
   private Long   dataSourceId;
   private String presentationXML;
   private String type;

   public String getType () {
      return type;
   }

   public void setType (String type) {
      this.type = type;
   }

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public String getName () {
      return name;
   }

   public void setName (String name) {
      this.name = name;
   }

   public String getQueryString () {
      return queryString;
   }

   public void setQueryString (String queryString) {
      this.queryString = queryString;
   }

   public String getHeaderXML () {
      return headerXML;
   }

   public void setHeaderXML (String headerXML) {
      this.headerXML = headerXML;
   }

   public Long getDataSourceId () {
      return dataSourceId;
   }

   public void setDataSourceId (Long dataSourceId) {
      this.dataSourceId = dataSourceId;
   }

   public String getPresentationXML () {
      return presentationXML;
   }

   public void setPresentationXML (String presentationXML) {
      this.presentationXML = presentationXML;
   }

}
