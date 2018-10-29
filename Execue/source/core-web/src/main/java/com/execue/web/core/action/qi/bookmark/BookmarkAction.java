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


package com.execue.web.core.action.qi.bookmark;

import java.util.List;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.Bookmark;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.core.exception.HandlerException;
import com.execue.handler.bean.UIFolder;

public class BookmarkAction extends BaseBookmarkAction {

   private static final long   serialVersionUID = 1L;
   private static final Logger log              = Logger.getLogger(BookmarkAction.class);
   private List<UIFolder>      folderList;
   private String              folderName;
   private long                folderId;
   private Bookmark            bookmark;
   private long                bookmarkId;
   private Long                applicationId;
   private Long                modelId;

   public List<UIFolder> getFolderList () {
      return folderList;
   }

   public void setFolderList (List<UIFolder> folderList) {
      this.folderList = folderList;
   }

   public String getFolderName () {
      return folderName;
   }

   public void setFolderName (String folderName) {
      this.folderName = folderName;
   }

   public long getFolderId () {
      return folderId;
   }

   public void setFolderId (long folderId) {
      this.folderId = folderId;
   }

   public Bookmark getBookmark () {
      return bookmark;
   }

   public void setBookmark (Bookmark bookmark) {
      this.bookmark = bookmark;
   }

   public long getBookmarkId () {
      return bookmarkId;
   }

   public void setBookmarkId (long bookmarkId) {
      this.bookmarkId = bookmarkId;
   }

   public String getUserFolders () {
      try {
         folderList = getBookmarkHandler().getFolders();
      } catch (HandlerException e) {
         e.printStackTrace();
      }
      return SUCCESS;
   }

   public String createFolder () {
      try {
         getBookmarkHandler().createFolder(folderName, 0L);
         addActionMessage(getText("execue.global.insert.success", new String[] { getText("execue.bookmark.folder") }));

      } catch (HandlerException e) {
         if (e.getCode() == ExeCueExceptionCodes.ENTITY_ALREADY_EXISTS) {
            addActionMessage(getText("execue.global.exist.message", new String[] { folderName }));
         } else {
            addActionError(getText("execue.global.error", new String[] { e.getMessage() }));
         }
      }
      return SUCCESS;
   }

   public String createBookmark () {
      try {
         if (bookmark != null) {
            getBookmarkHandler().createBookmark(bookmark);
            addActionMessage(getText("execue.global.insert.success", new String[] { getText("execue.bookmark.lebel") }));
         }
      } catch (HandlerException e) {
         if (e.getCode() == ExeCueExceptionCodes.ENTITY_ALREADY_EXISTS) {
            addActionMessage(getText("execue.global.exist.message", new String[] { bookmark.getName() }));
         } else {
            addActionMessage(getText("execue.global.error", new String[] { e.getMessage() }));
         }
      }
      return SUCCESS;
   }

   public String deleteFolder () {
      try {
         getBookmarkHandler().deleteFolder(folderId);
         addActionMessage(getText("execue.global.delete.success", new String[] { getText("execue.bookmark.folder") }));
      } catch (HandlerException e) {
         addActionError(getText("execue.global.error", new String[] { e.getMessage() }));
         return ERROR;
      }
      return SUCCESS;
   }

   public String deleteBookmark () {
      try {
         getBookmarkHandler().deleteBookmark(bookmarkId);
         addActionMessage(getText("execue.global.delete.success", new String[] { getText("execue.bookmark.lebel") }));
      } catch (HandlerException e) {
         addActionError(getText("execue.global.error", new String[] { e.getMessage() }));
         return ERROR;
      }
      return SUCCESS;
   }

   public String deleteFolderFromSavedQueries () {
      try {
         getBookmarkHandler().deleteFolder(folderId);
         addActionMessage(getText("execue.global.delete.success", new String[] { getText("execue.bookmark.folder") }));
      } catch (HandlerException e) {
         addActionError(getText("execue.global.error", new String[] { e.getMessage() }));
         return ERROR;
      }
      return SUCCESS;
   }

   public String deleteBookmarkFromSavedQueries () {
      try {
         getBookmarkHandler().deleteBookmark(bookmarkId);
         addActionMessage(getText("execue.global.delete.success", new String[] { getText("execue.bookmark.lebel") }));
      } catch (HandlerException e) {
         addActionError(getText("execue.global.error", new String[] { e.getMessage() }));
         return ERROR;
      }
      return SUCCESS;
   }

   /**
    * @return the applicationId
    */
   public Long getApplicationId () {
      return applicationId;
   }

   /**
    * @param applicationId
    *           the applicationId to set
    */
   public void setApplicationId (Long applicationId) {
      this.applicationId = applicationId;
   }

   /**
    * @return the modelId
    */
   public Long getModelId () {
      return modelId;
   }

   /**
    * @param modelId
    *           the modelId to set
    */
   public void setModelId (Long modelId) {
      this.modelId = modelId;
   }

}
