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
package com.execue.repoting.aggregation.bean;

import java.util.List;

/**
 * @author nitesh
 *
 */
public class AggregationMetaHierarchyInfo {

   private String                               hierarchyName;
   private int                                  hierarchyEntityCount;
   private List<AggregationHierarchyColumnInfo> hierarchyDetails;

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

   /**
    * @return the hierarchyDetails
    */
   public List<AggregationHierarchyColumnInfo> getHierarchyDetails () {
      return hierarchyDetails;
   }

   /**
    * @param hierarchyDetails the hierarchyDetails to set
    */
   public void setHierarchyDetails (List<AggregationHierarchyColumnInfo> hierarchyDetails) {
      this.hierarchyDetails = hierarchyDetails;
   }
}