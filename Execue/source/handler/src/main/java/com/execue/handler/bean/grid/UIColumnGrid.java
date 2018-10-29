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

public class UIColumnGrid {

   private long                     numOfColumns;
   private long                     gridId;
   private String                   gridTitle;
   private List<UIColumnGridHeader> columnGridHeader;

   /**
    * @return the numOfColumns
    */
   public long getNumOfColumns () {
      return numOfColumns;
   }

   /**
    * @param numOfColumns
    *           in the grid the numOfColumns to set
    */
   public void setNumOfColumns (long numOfColumns) {
      this.numOfColumns = numOfColumns;
   }

   /**
    * @return the gridId which can be id to fetch the grid data
    */
   public long getGridId () {
      return gridId;
   }

   /**
    * @param gridId
    *           is the id to fetch the grid data the gridId to set
    */
   public void setGridId (long gridId) {
      this.gridId = gridId;
   }

   /**
    * @return the gridTitle is the caption for the grid to show
    */
   public String getGridTitle () {
      return gridTitle;
   }

   /**
    * @param gridTitle
    *           the gridTitle to set
    */
   public void setGridTitle (String gridTitle) {
      this.gridTitle = gridTitle;
   }

   /**
    * @return the columnGridHeader which are the columns to be displayed in the grid
    */
   public List<UIColumnGridHeader> getColumnGridHeader () {
      return columnGridHeader;
   }

   /**
    * @param columnGridHeader
    *           the columnGridHeader to set
    */
   public void setColumnGridHeader (List<UIColumnGridHeader> columnGridHeader) {
      this.columnGridHeader = columnGridHeader;
   }

}
