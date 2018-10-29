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


package com.execue.reporting.presentation.bean;

/**
 * Result object from transform service
 * 
 * @author kaliki
 * @since 4.0
 */
public class PresentationTransformData {

   private String xmlData;
   private int    pageCount;
   private int    requestedPage;

   public int getRequestedPage () {
      return requestedPage;
   }

   public void setRequestedPage (int requestedPage) {
      this.requestedPage = requestedPage;
   }

   public int getPageCount () {
      return pageCount;
   }

   public void setPageCount (int pageCount) {
      this.pageCount = pageCount;
   }

   public String getXmlData () {
      return xmlData;
   }

   public void setXmlData (String xmlData) {
      this.xmlData = xmlData;
   }
}
