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


package com.execue.core.common.bean;


/**
 * @author John Mallavalli
 */
public class Pagination {

   private String requestedPage;
   private String pageSize;
   private String pageCount;
   private String baseURL;

   // private List<Colum> resultList;
   // private String resultsPerPage;
   // private List<Membr> resultMemList;

   public String getRequestedPage () {
      return requestedPage;
   }

   public void setRequestedPage (String requestedPage) {
      this.requestedPage = requestedPage;
   }

   public String getPageSize () {
      return pageSize;
   }

   public void setPageSize (String pageSize) {
      this.pageSize = pageSize;
   }

   public String getPageCount () {
      return pageCount;
   }

   public void setPageCount (String pageCount) {
      this.pageCount = pageCount;
   }

   public String getBaseURL () {
      return baseURL;
   }

   public void setBaseURL (String baseURL) {
      this.baseURL = baseURL;
   }

   /*
    * public List<Colum> getResultList () { return resultList; } public void setResultList (List<Colum> resultList) {
    * this.resultList = resultList; } public String getResultsPerPage () { return resultsPerPage; } public void
    * setResultsPerPage (String resultsPerPage) { this.resultsPerPage = resultsPerPage; } public List<Membr>
    * getResultMemList () { return resultMemList; } public void setResultMemList (List<Membr> resultMemList) {
    * this.resultMemList = resultMemList; }
    */
}
