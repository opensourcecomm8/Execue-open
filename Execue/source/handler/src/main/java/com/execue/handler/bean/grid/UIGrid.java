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


package com.execue.handler.bean.grid;

import java.util.List;

public class UIGrid {

   /**
    * This is a generic bean and should be used for any table info to be populated into this given format for jQGrid to
    * work.
    * 
    * @author kaliki
    */

   private Integer         records;
   private Integer         page;
   private Integer         total;
   private List<IGridBean> rows;

   public Integer getRecords () {
      return records;
   }

   public void setRecords (Integer records) {
      this.records = records;
   }

   public Integer getPage () {
      return page;
   }

   public void setPage (Integer page) {
      this.page = page;
   }

   public Integer getTotal () {
      return total;
   }

   public void setTotal (Integer total) {
      this.total = total;
   }

   public List<IGridBean> getRows () {
      return rows;
   }

   public void setRows (List<IGridBean> rows) {
      this.rows = rows;
   }

}
