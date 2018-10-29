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


package com.execue.core.common.bean.querygen;

import java.util.List;

public class SelectQueryInfo {

   private String        selectQuery;
   // zero based index list
   private List<Integer> countColumnIndexes;

   public SelectQueryInfo (String selectQuery, List<Integer> countColumnIndexes) {
      super();
      this.selectQuery = selectQuery;
      this.countColumnIndexes = countColumnIndexes;
   }

   public SelectQueryInfo (String selectQuery) {
      super();
      this.selectQuery = selectQuery;
   }

   public String getSelectQuery () {
      return selectQuery;
   }

   public void setSelectQuery (String selectQuery) {
      this.selectQuery = selectQuery;
   }

   public List<Integer> getCountColumnIndexes () {
      return countColumnIndexes;
   }

   public void setCountColumnIndexes (List<Integer> countColumnIndexes) {
      this.countColumnIndexes = countColumnIndexes;
   }
}