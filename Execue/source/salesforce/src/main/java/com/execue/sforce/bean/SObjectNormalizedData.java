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


package com.execue.sforce.bean;

import java.util.List;

import com.execue.core.common.bean.querygen.QueryColumn;

/**
 * This bean represents the sobject data along with their respective normalized beans from execue
 * 
 * @author Vishay
 * @version 1.0
 * @since 11/08/09
 */
public class SObjectNormalizedData {

   private SObjectTable       sObjectTable;
   private List<List<String>> sObjectDataPoints;
   private List<QueryColumn>  normalizedQueryColumns;
   private List<List<Object>> normalizedDataPoints;
   

   public List<QueryColumn> getNormalizedQueryColumns () {
      return normalizedQueryColumns;
   }

   public void setNormalizedQueryColumns (List<QueryColumn> normalizedQueryColumns) {
      this.normalizedQueryColumns = normalizedQueryColumns;
   }

   public List<List<Object>> getNormalizedDataPoints () {
      return normalizedDataPoints;
   }

   public void setNormalizedDataPoints (List<List<Object>> normalizedDataPoints) {
      this.normalizedDataPoints = normalizedDataPoints;
   }

   public List<List<String>> getSObjectDataPoints () {
      return sObjectDataPoints;
   }

   public void setSObjectDataPoints (List<List<String>> objectDataPoints) {
      sObjectDataPoints = objectDataPoints;
   }

   public SObjectTable getSObjectTable () {
      return sObjectTable;
   }

   public void setSObjectTable (SObjectTable objectTable) {
      sObjectTable = objectTable;
   }

}
