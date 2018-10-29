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


package com.execue.swi.dataaccess;

import java.io.Serializable;
import java.util.List;

import com.execue.core.common.bean.Bookmark;
import com.execue.core.common.bean.Folder;
import com.execue.core.common.type.BookmarkType;
import com.execue.swi.exception.BookMarkException;

public interface IBookmarkDataAccessManager {

   public <DomainObject extends Serializable> DomainObject getById (Long id, Class<DomainObject> clazz)
            throws BookMarkException;

   // Create
   public void createFolderForUser (String folderName, long parentFolderId, long userid) throws BookMarkException;

   public void createBookmark (Bookmark bookmark) throws BookMarkException;

   // Search
   public List<Bookmark> searchBookmarks (String searchString, BookmarkType type, long userid) throws BookMarkException;

   public Bookmark retiveBookmark (long bookmarkId) throws BookMarkException;

   public List<Bookmark> getBookmarksForUser (long userid) throws BookMarkException; // for the user

   public List<Bookmark> getBookmarksForFolder (long folderId) throws BookMarkException; // for the user

   // by folder

   public List<Folder> getFolders (long userid) throws BookMarkException; // for the user

   // Deletion
   public void deleteBookmark (long bookmarkId) throws BookMarkException;

   public void deleteFolder (long folderId) throws BookMarkException;

   public void deleteAllBookMarks (long userid) throws BookMarkException; // for the user making the call

   public void deleteAllBookMarksOfFolder (long folderId) throws BookMarkException; // for the user

   public boolean isBookmarkExist (String bookmarkName, long folderId, long userId) throws BookMarkException;

   public boolean isFolderExist (String folderName, long userId) throws BookMarkException;

}
