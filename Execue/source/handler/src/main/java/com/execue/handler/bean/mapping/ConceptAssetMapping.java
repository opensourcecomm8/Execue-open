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


package com.execue.handler.bean.mapping;

/**
 * JSON Object representation of Concept to Table.Column Mapping
 * 
 * @author kaliki
 * @since 4.0
 */
public class ConceptAssetMapping extends AbstractAssetMapping {

   private String  tblDispName;
   private String  tableDisplayName;
   private String  colDispName;
   private String  conDispName;
   private String  relevance;
   private boolean mapInstance;
   private long    conId;
   /**
    * Concept Type can take the values SUGGESTED, EXISTING and NEW
    */
   private String  conType;

   public String getTblDispName () {
      return tblDispName;
   }

   public void setTblDispName (String tableDisplayName) {
      this.tblDispName = tableDisplayName;
   }

   public String getColDispName () {
      return colDispName;
   }

   public void setColDispName (String columnDisplayName) {
      this.colDispName = columnDisplayName;
   }

   public String getConDispName () {
      return conDispName;
   }

   public void setConDispName (String conceptDisplayName) {
      this.conDispName = conceptDisplayName;
   }

   public String getRelevance () {
      return relevance;
   }

   public void setRelevance (String relevance) {
      this.relevance = relevance;
   }

   public boolean isMapInstance () {
      return mapInstance;
   }

   public void setMapInstance (boolean hasInstance) {
      this.mapInstance = hasInstance;
   }

   public String getConType () {
      return conType;
   }

   public void setConType (String conceptType) {
      this.conType = conceptType;
   }

   public long getConId () {
      return conId;
   }

   public void setConId (long conId) {
      this.conId = conId;
   }

   /**
    * @return the tableDisplayName
    */
   public String getTableDisplayName () {
      return tableDisplayName;
   }

   /**
    * @param tableDisplayName
    *           the tableDisplayName to set
    */
   public void setTableDisplayName (String tableDisplayName) {
      this.tableDisplayName = tableDisplayName;
   }

}