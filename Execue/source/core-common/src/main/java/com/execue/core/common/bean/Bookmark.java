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


package com.execue.core.common.bean;

import java.io.Serializable;
import java.util.Date;

import com.execue.core.common.bean.entity.Application;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.common.bean.security.User;
import com.execue.core.common.type.BookmarkType;

/**
 * @author kaliki
 */
public class Bookmark implements Serializable {

   private Long         id;
   private String       name;
   private String       value;
   private String       summary;
   private BookmarkType type;
   private Folder       folder;
   private User         user;
   private Date         dateCreated;
   private Date         dateModified;
   private Model        model;
   private Application  application;

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

   public String getValue () {
      return value;
   }

   public void setValue (String value) {
      this.value = value;
   }

   public BookmarkType getType () {
      return type;
   }

   public void setType (BookmarkType type) {
      this.type = type;
   }

   public Folder getFolder () {
      return folder;
   }

   public void setFolder (Folder folder) {
      this.folder = folder;
   }

   public User getUser () {
      return user;
   }

   public void setUser (User user) {
      this.user = user;
   }

   public String getSummary () {
      return summary;
   }

   public void setSummary (String summary) {
      this.summary = summary;
   }

   public Date getDateCreated () {
      return dateCreated;
   }

   public void setDateCreated (Date dateCreated) {
      this.dateCreated = dateCreated;
   }

   public Date getDateModified () {
      return dateModified;
   }

   public void setDateModified (Date dateModified) {
      this.dateModified = dateModified;
   }

   /**
    * @return the model
    */
   public Model getModel () {
      return model;
   }

   /**
    * @param model
    *           the model to set
    */
   public void setModel (Model model) {
      this.model = model;
   }

   /**
    * @return the application
    */
   public Application getApplication () {
      return application;
   }

   /**
    * @param application
    *           the application to set
    */
   public void setApplication (Application application) {
      this.application = application;
   }

}
