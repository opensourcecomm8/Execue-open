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


package com.execue.handler.swi.impl;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.Bookmark;
import com.execue.core.common.bean.Folder;
import com.execue.core.common.bean.entity.Application;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.common.type.BookmarkType;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.core.exception.HandlerException;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.handler.bean.UIBookmark;
import com.execue.handler.bean.UIBookmarkSearch;
import com.execue.handler.bean.UIFolder;
import com.execue.handler.swi.IBookmarkHandler;
import com.execue.pseudolang.service.IPseudoLanguageService;
import com.execue.security.UserContextService;
import com.execue.swi.exception.BookMarkException;
import com.execue.swi.service.IBookmarkService;

public class BookmarkHandlerImpl extends UserContextService implements IBookmarkHandler {

   private static final Logger    logger = Logger.getLogger(BookmarkHandlerImpl.class);

   private IPseudoLanguageService pseudoLanguageService;
   private IBookmarkService       bookmarkService;

   public void createBookmark (Bookmark bookmark) throws HandlerException {
      try {
         getBookmarkService().createBookmark(bookmark, getUserId());
      } catch (BookMarkException bookMarkException) {
         if (bookMarkException.getCode() == ExeCueExceptionCodes.ENTITY_ALREADY_EXISTS) {
            throw new HandlerException(ExeCueExceptionCodes.ENTITY_ALREADY_EXISTS, bookMarkException);
         } else {
            throw new HandlerException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, bookMarkException);
         }
      }
   }

   public void createFolder (String folderName, Long parentFolderId) throws HandlerException {
      try {
         getBookmarkService().createFolder(folderName, parentFolderId, getUserId());
      } catch (BookMarkException bookMarkException) {
         if (bookMarkException.getCode() == ExeCueExceptionCodes.ENTITY_ALREADY_EXISTS) {
            throw new HandlerException(ExeCueExceptionCodes.ENTITY_ALREADY_EXISTS, bookMarkException);
         } else {
            throw new HandlerException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, bookMarkException);
         }
      }
   }

   public void deleteBookmark (Long bookmarkId) throws HandlerException {
      try {
         getBookmarkService().deleteBookmark(bookmarkId);
      } catch (BookMarkException bookMarkException) {
         throw new HandlerException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, bookMarkException);
      }

   }

   public void deleteFolder (Long folderId) throws HandlerException {
      try {
         getBookmarkService().deleteFolder(folderId);
      } catch (BookMarkException bookMarkException) {
         throw new HandlerException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, bookMarkException);
      }
   }

   public List<UIFolder> getFolders () throws HandlerException {
      List<UIFolder> uiFolders = new ArrayList<UIFolder>();
      try {
         List<Folder> folders = getBookmarkService().getFolders(getUserId());
         if (ExecueCoreUtil.isCollectionNotEmpty(folders)) {
            // we don't need the user information in the actions
            for (Folder folder : folders) {
               UIFolder uiFolder = new UIFolder();
               uiFolder.setName(folder.getName());
               uiFolder.setId(folder.getId());
               Set<Bookmark> bookmarks = folder.getBookmarks();
               Set<UIBookmark> uiBookmarks = new LinkedHashSet<UIBookmark>();
               if (ExecueCoreUtil.isCollectionNotEmpty(bookmarks)) {
                  for (Bookmark bookmark : bookmarks) {
                     UIBookmark uibookmark = new UIBookmark();
                     uibookmark.setName(bookmark.getName());
                     uibookmark.setId(bookmark.getId());
                     uibookmark.setValue(bookmark.getValue());
                     uibookmark.setType(bookmark.getType());
                     Application application = bookmark.getApplication();
                     if (application != null) {
                        uibookmark.setApplicationId(application.getId());
                     }
                     Model model = bookmark.getModel();
                     if (model != null) {
                        uibookmark.setModelId(model.getId());
                     }
                     uiBookmarks.add(uibookmark);
                  }
               }
               uiFolder.setUiBookmarks(uiBookmarks);
               uiFolders.add(uiFolder);
            }
         }
      } catch (BookMarkException bookMarkException) {
         throw new HandlerException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, bookMarkException);
      }
      return uiFolders;

   }

   public List<UIBookmark> getBookmarksForFolder (Long folderId) throws HandlerException {
      List<UIBookmark> uiBookmarks = new ArrayList<UIBookmark>();
      try {
         List<Bookmark> bookmarks = getBookmarkService().getBookmarksForFolder(folderId);
         if (bookmarks != null) {
            for (Bookmark bookmark : bookmarks) {
               UIBookmark uiBookmark = new UIBookmark();
               uiBookmark.setName(bookmark.getName());
               uiBookmark.setId(bookmark.getId());
               uiBookmark.setValue(bookmark.getValue());
               uiBookmark.setType(bookmark.getType());
               uiBookmark.setApplicationId(bookmark.getApplication().getId());
               uiBookmark.setModelId(bookmark.getModel().getId());
               uiBookmarks.add(uiBookmark);
            }
         }
      } catch (BookMarkException bookMarkException) {
         throw new HandlerException(ExeCueExceptionCodes.ENTITY_RETRIEVAL_FAILED, bookMarkException);
      }
      return uiBookmarks;
   }

   public List<UIBookmarkSearch> searchBookmarks (String searchString, BookmarkType type) throws HandlerException {
      List<UIBookmarkSearch> uiBookmarkSearchs = new ArrayList<UIBookmarkSearch>();

      try {
         List<Bookmark> bookmarks = getBookmarkService().searchBookmarks(searchString, type, getUserId());
         if (bookmarks != null) {
            for (Bookmark bookmark : bookmarks) {
               UIBookmarkSearch uiBookmarkSearch = new UIBookmarkSearch();
               uiBookmarkSearch.setId(bookmark.getId());
               uiBookmarkSearch.setName(bookmark.getName());
               uiBookmarkSearch.setSummary(bookmark.getSummary());
               uiBookmarkSearch.setValue(bookmark.getValue());
               if (bookmark.getApplication() != null) {
                  uiBookmarkSearch.setApplicationId(bookmark.getApplication().getId());
               }
               uiBookmarkSearchs.add(uiBookmarkSearch);
            }
         }
      } catch (BookMarkException bookMarkException) {
         throw new HandlerException(ExeCueExceptionCodes.ENTITY_RETRIEVAL_FAILED, bookMarkException);
      }

      return uiBookmarkSearchs;
   }

   /**
    * @return the pseudoLanguageService
    */
   public IPseudoLanguageService getPseudoLanguageService () {
      return pseudoLanguageService;
   }

   /**
    * @param pseudoLanguageService
    *           the pseudoLanguageService to set
    */
   public void setPseudoLanguageService (IPseudoLanguageService pseudoLanguageService) {
      this.pseudoLanguageService = pseudoLanguageService;
   }

   public IBookmarkService getBookmarkService () {
      return bookmarkService;
   }

   public void setBookmarkService (IBookmarkService bookmarkService) {
      this.bookmarkService = bookmarkService;
   }

   private Long getUserId () {
      return getUserContext().getUser().getId();
   }
}
