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


package com.execue.swi.service;

import java.util.List;

import com.execue.core.common.bean.Bookmark;
import com.execue.core.common.bean.Folder;
import com.execue.core.common.type.BookmarkType;
import com.execue.swi.exception.BookMarkException;

/**
 * @author kaliki
 * @since 4.0
 */
public interface IBookmarkService {

   /**
    * create Folder. parentFolderId is used if the folder to be created is a sub-folder of another folder. Currently
    * this will be 0 all the time.
    * 
    * @param folderName
    * @param parentFolderId
    * @return
    */
   public void createFolder (String folderName, long parentFolderId, Long userId) throws BookMarkException;

   public void createBookmark (Bookmark bookmark, Long userId) throws BookMarkException;

   /**
    * Used for searching bookmarks that match with searchstring. Type is used to narrow search based on BookmarkType .
    * Bookmark will contain only name and summary name. summary - English like statement.
    * 
    * @param searchString
    * @param type
    * @return
    */
   public List<Bookmark> searchBookmarks (String searchString, BookmarkType type, Long userId) throws BookMarkException;

   public Bookmark retiveBookmark (long bookmarkId) throws BookMarkException; // complete info for the searched

   // bookmark

   // get
   public List<Bookmark> getBookmarks (Long userId) throws BookMarkException; // for the user

   public List<Bookmark> getBookmarksForFolder (long folderId) throws BookMarkException; // for the user by

   // folder

   public List<Folder> getFolders (Long userId) throws BookMarkException; // for the user

   // deletion
   public void deleteBookmark (long bookmarkId) throws BookMarkException;

   public void deleteFolder (long folderId) throws BookMarkException;

   public void deleteAllBookMarks (Long userId) throws BookMarkException; // for the user making the call

   public void deleteAllBookMarksOfFolder (long folderId) throws BookMarkException; // for the user making the call

}
