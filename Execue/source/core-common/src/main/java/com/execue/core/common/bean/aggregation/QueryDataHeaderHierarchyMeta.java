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


/**
 * 
 */
package com.execue.core.common.bean.aggregation;

import java.util.List;

/**
 * @author Nitesh
 *
 */
public class QueryDataHeaderHierarchyMeta {

   private String                                   hierarchyName;
   private List<QueryDataHeaderHierarchyColumnMeta> hierarhcyDetails;
   private int                                      hierarchyEntityCount;

   /**
    * @return the hierarchyName
    */
   public String getHierarchyName () {
      return hierarchyName;
   }

   /**
    * @param hierarchyName the hierarchyName to set
    */
   public void setHierarchyName (String hierarchyName) {
      this.hierarchyName = hierarchyName;
   }

   /**
    * @return the hierarhcyDetails
    */
   public List<QueryDataHeaderHierarchyColumnMeta> getHierarhcyDetails () {
      return hierarhcyDetails;
   }

   /**
    * @param hierarhcyDetails the hierarhcyDetails to set
    */
   public void setHierarhcyDetails (List<QueryDataHeaderHierarchyColumnMeta> hierarhcyDetails) {
      this.hierarhcyDetails = hierarhcyDetails;
   }

   /**
    * @return the hierarchyEntityCount
    */
   public int getHierarchyEntityCount () {
      return hierarchyEntityCount;
   }

   /**
    * @param hierarchyEntityCount the hierarchyEntityCount to set
    */
   public void setHierarchyEntityCount (int hierarchyEntityCount) {
      this.hierarchyEntityCount = hierarchyEntityCount;
   }
}