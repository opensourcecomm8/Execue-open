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

import com.execue.core.common.type.PageSearchType;

public class PageSearch {

   /**
    * The column on which the search needs to happen
    */
   private String         field;

   /**
    * The user entered search string
    */
   private String         string;

   /**
    * The type of search - whether contains or startsWith
    */
   private PageSearchType type;

   /**
    * This field has to be populated by the class which has the SQL/HQL with the alias object hierarchy of the search
    * column name
    */
   private String         qualifier;

   public String getQualifier () {
      return qualifier;
   }

   public void setQualifier (String qualifier) {
      this.qualifier = qualifier;
   }

   public String getField () {
      return field;
   }

   public void setField (String field) {
      this.field = field;
   }

   public String getString () {
      return string;
   }

   public void setString (String string) {
      this.string = string;
   }

   public PageSearchType getType () {
      return type;
   }

   public void setType (PageSearchType type) {
      this.type = type;
   }

}
