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


package com.execue.swi.dataaccess.impl;

import java.io.Serializable;
import java.util.List;

import com.execue.core.common.bean.Bookmark;
import com.execue.core.common.bean.Folder;
import com.execue.core.common.bean.security.User;
import com.execue.core.common.type.BookmarkType;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.dataaccess.swi.dao.IBookmarksDAO;
import com.execue.swi.dataaccess.IBookmarkDataAccessManager;
import com.execue.swi.exception.BookMarkException;

public class BookmarkDataAccessManagerImpl implements IBookmarkDataAccessManager {

   private IBookmarksDAO bookMarksDAO;

   public IBookmarksDAO getBookMarksDAO () {
      return bookMarksDAO;
   }

   public void setBookMarksDAO (IBookmarksDAO bookMarksDAO) {
      this.bookMarksDAO = bookMarksDAO;
   }

   public <DomainObject extends Serializable> DomainObject getById (Long id, Class<DomainObject> clazz)
            throws BookMarkException {
      try {
         return bookMarksDAO.getById(id, clazz);
      } catch (DataAccessException dataAccessException) {
         throw new BookMarkException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public void createBookmark (Bookmark bookmark) throws BookMarkException {
      try {
         bookMarksDAO.create(bookmark);
      } catch (DataAccessException e) {
         throw new BookMarkException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }

   }

   public void createFolderForUser (String folderName, long parentFolderId, long userid) throws BookMarkException {
      try {
         Folder folder = new Folder();
         folder.setName(folderName);
         folder.setParentId(parentFolderId);
         User user = new User();
         user.setId(userid);
         folder.setUser(user);
         bookMarksDAO.create(folder);
      } catch (DataAccessException e) {
         throw new BookMarkException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public void deleteBookmark (long bookmarkId) throws BookMarkException {
      try {
         bookMarksDAO.deleteBookmark(bookmarkId);
      } catch (DataAccessException e) {
         throw new BookMarkException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }

   }

   public void deleteAllBookMarks (long userid) throws BookMarkException {
      try {
         bookMarksDAO.deleteAllBookMarks(userid);
      } catch (DataAccessException e) {
         throw new BookMarkException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }

   }

   public void deleteAllBookMarksOfFolder (long folderId) throws BookMarkException {
      try {
         bookMarksDAO.deleteAllBookMarksOfFolder(folderId);
      } catch (DataAccessException e) {
         throw new BookMarkException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }

   }

   public void deleteFolder (long folderId) throws BookMarkException {
      try {
         bookMarksDAO.deleteFolder(folderId);
      } catch (DataAccessException e) {
         throw new BookMarkException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }

   }

   public List<Bookmark> getBookmarksForFolder (long folderId) throws BookMarkException {
      try {
         return bookMarksDAO.getBookmarksForFolder(folderId);
      } catch (DataAccessException e) {
         throw new BookMarkException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public List<Bookmark> getBookmarksForUser (long userid) throws BookMarkException {
      try {
         return bookMarksDAO.getBookmarksForUser(userid);
      } catch (DataAccessException e) {
         throw new BookMarkException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public List<Folder> getFolders (long userid) throws BookMarkException {
      try {
         return bookMarksDAO.getFolders(userid);
      } catch (DataAccessException e) {
         throw new BookMarkException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public Bookmark retiveBookmark (long bookmarkId) throws BookMarkException {
      try {
         return bookMarksDAO.retiveBookmark(bookmarkId);
      } catch (DataAccessException e) {
         throw new BookMarkException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public List<Bookmark> searchBookmarks (String searchString, BookmarkType type, long userid) throws BookMarkException {
      try {
         return bookMarksDAO.searchBookmarksForUser(searchString, type, userid);
      } catch (DataAccessException e) {
         throw new BookMarkException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public boolean isBookmarkExist (String bookmarkName, long folderId, long userId) throws BookMarkException {

      try {
         return bookMarksDAO.isBookmarkExist(bookmarkName, folderId, userId);
      } catch (DataAccessException e) {

         throw new BookMarkException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public boolean isFolderExist (String folderName, long userId) throws BookMarkException {

      try {
         return bookMarksDAO.isFolderExist(folderName, userId);
      } catch (DataAccessException e) {
         throw new BookMarkException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

}
