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
import java.util.Set;

import com.execue.core.common.bean.security.User;

public class Folder implements Serializable {

   private Long          id;
   private String        name;
   private User          user;
   private Long          parentId;
   private Date          createdDate;
   private Date          modifiedDate;

   // TODO - RG- SOlve ths problem by having a table SUB_FOLDERS(FODLER_ID, SUB_FOLDER_ID) with sub folder id is being
   // pointed from folder tables itself
   private Set<Folder>   subFolders;

   private Set<Bookmark> bookmarks;

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

   public User getUser () {
      return user;
   }

   public void setUser (User user) {
      this.user = user;
   }

   public Set<Folder> getSubFolders () {
      return subFolders;
   }

   public void setSubFolders (Set<Folder> subFolders) {
      this.subFolders = subFolders;
   }

   public Set<Bookmark> getBookmarks () {
      return bookmarks;
   }

   public void setBookmarks (Set<Bookmark> bookmarks) {
      this.bookmarks = bookmarks;
   }

   public Long getParentId () {
      return parentId;
   }

   public void setParentId (Long parentId) {
      this.parentId = parentId;
   }

   public Date getCreatedDate () {
      return createdDate;
   }

   public void setCreatedDate (Date createdDate) {
      this.createdDate = createdDate;
   }

   public Date getModifiedDate () {
      return modifiedDate;
   }

   public void setModifiedDate (Date modifiedDate) {
      this.modifiedDate = modifiedDate;
   }

}
