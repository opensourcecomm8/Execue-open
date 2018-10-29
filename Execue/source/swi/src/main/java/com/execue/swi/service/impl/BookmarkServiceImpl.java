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


package com.execue.swi.service.impl;

import java.util.List;
import java.util.Set;

import com.execue.core.common.bean.Bookmark;
import com.execue.core.common.bean.Folder;
import com.execue.core.common.bean.security.User;
import com.execue.core.common.type.BookmarkType;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.swi.dataaccess.IBookmarkDataAccessManager;
import com.execue.swi.exception.BookMarkException;
import com.execue.swi.service.IBookmarkService;

/**
 * @author kaliki
 */
public class BookmarkServiceImpl implements IBookmarkService {

   private IBookmarkDataAccessManager bookmarkDataAccessManager;

   public IBookmarkDataAccessManager getBookmarkDataAccessManager () {
      return bookmarkDataAccessManager;
   }

   public void setBookmarkDataAccessManager (IBookmarkDataAccessManager bookmarkDataAccessManager) {
      this.bookmarkDataAccessManager = bookmarkDataAccessManager;
   }

   public void createBookmark (Bookmark bookmark, Long userId) throws BookMarkException {
      User user = new User();
      user.setId(userId);
      bookmark.setUser(user);
      if (bookmarkDataAccessManager.isBookmarkExist(bookmark.getName(), bookmark.getFolder().getId(), userId)) {
         throw new BookMarkException(ExeCueExceptionCodes.ENTITY_ALREADY_EXISTS, "Bookmark with name ["
                  + bookmark.getName() + "] already exists.");
      }
      bookmarkDataAccessManager.createBookmark(bookmark);
   }

   public void createFolder (String folderName, long parentFolderId, Long userId) throws BookMarkException {
      if (bookmarkDataAccessManager.isFolderExist(folderName, userId)) {
         throw new BookMarkException(ExeCueExceptionCodes.ENTITY_ALREADY_EXISTS, "Folder with name [" + folderName
                  + "] already exists.");
      }
      bookmarkDataAccessManager.createFolderForUser(folderName, parentFolderId, userId);
   }

   public void deleteAllBookMarks (Long userId) throws BookMarkException {
      bookmarkDataAccessManager.deleteAllBookMarks(userId);

   }

   public void deleteAllBookMarksOfFolder (long folderId) throws BookMarkException {
      bookmarkDataAccessManager.deleteAllBookMarksOfFolder(folderId);

   }

   public void deleteBookmark (long bookmarkId) throws BookMarkException {
      bookmarkDataAccessManager.deleteBookmark(bookmarkId);

   }

   public void deleteFolder (long folderId) throws BookMarkException {
      bookmarkDataAccessManager.deleteFolder(folderId);

   }

   public List<Bookmark> getBookmarks (Long userId) throws BookMarkException {
      return bookmarkDataAccessManager.getBookmarksForUser(userId);
   }

   public List<Bookmark> getBookmarksForFolder (long folderId) throws BookMarkException {
      return bookmarkDataAccessManager.getBookmarksForFolder(folderId);
   }

   public List<Folder> getFolders (Long userId) throws BookMarkException {
      List<Folder> folders = bookmarkDataAccessManager.getFolders(userId);

      if (ExecueCoreUtil.isCollectionNotEmpty(folders)) {
         for (Folder folder : folders) {
            // lazy load the bookmarks
            Set<Bookmark> bookmarks = folder.getBookmarks();
            if (bookmarks != null) {
               for (Bookmark bookmark : bookmarks) {
                  bookmark.getName();
               }
            }
         }
      }

      return folders;
   }

   public Bookmark retiveBookmark (long bookmarkId) throws BookMarkException {
      return bookmarkDataAccessManager.retiveBookmark(bookmarkId);
   }

   public List<Bookmark> searchBookmarks (String searchString, BookmarkType type, Long userId) throws BookMarkException {
      return bookmarkDataAccessManager.searchBookmarks(searchString, type, userId);
   }

}
