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


package com.execue.web.core.action.swi;

import java.util.ArrayList;
import java.util.List;

import com.execue.handler.bean.grid.IGridBean;
import com.execue.handler.swi.IUploadServiceHandler;
import com.execue.swi.exception.PublishedFileException;

public class DatasetListAction extends PaginationGridAction {

   private IUploadServiceHandler uploadServiceHandler;

   @Override
   protected List<? extends IGridBean> processPageGrid () {
      try {
         return getUploadServiceHandler().getPublishedFilesByPage(getPageDetail());
      } catch (PublishedFileException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      return new ArrayList<IGridBean>();
   }

   public IUploadServiceHandler getUploadServiceHandler () {
      return uploadServiceHandler;
   }

   public void setUploadServiceHandler (IUploadServiceHandler uploadServiceHandler) {
      this.uploadServiceHandler = uploadServiceHandler;
   }

}
