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


package com.execue.handler;

import java.util.List;

import com.execue.handler.bean.grid.IGridBean;

public class UITransposedPublishedFileTableDetails implements IGridBean {

   private Long                                id;
   private String                              rowHeader;
   private List<UIPublishedFileGridColumnInfo> cols;

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   
   public String getRowHeader () {
      return rowHeader;
   }

   
   public void setRowHeader (String rowHeader) {
      this.rowHeader = rowHeader;
   }

   
   public List<UIPublishedFileGridColumnInfo> getCols () {
      return cols;
   }

   
   public void setCols (List<UIPublishedFileGridColumnInfo> cols) {
      this.cols = cols;
   }

}


