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


package com.execue.handler.swi;

import java.util.List;

import com.execue.core.common.bean.Bookmark;
import com.execue.core.common.type.BookmarkType;
import com.execue.core.exception.HandlerException;
import com.execue.handler.bean.UIBookmark;
import com.execue.handler.bean.UIBookmarkSearch;
import com.execue.handler.bean.UIFolder;

public interface IBookmarkHandler {

   public void createFolder (String folderName, Long parentFolderId) throws HandlerException;

   public void createBookmark (Bookmark bookmark) throws HandlerException;

   public void deleteFolder (Long folderId) throws HandlerException;

   public void deleteBookmark (Long bookmarkId) throws HandlerException;

   public List<UIFolder> getFolders () throws HandlerException;

   public List<UIBookmark> getBookmarksForFolder (Long folderId) throws HandlerException;

   public List<UIBookmarkSearch> searchBookmarks (String searchString, BookmarkType type) throws HandlerException;
}
