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
package com.execue.core.common.bean.qdata;

/**
 * @author Nitesh
 *
 */
public class QueryHistoryBusinessEntityInfo implements Comparable<QueryHistoryBusinessEntityInfo>{

   private String name;
   private Long   businessEntityId;
   private double usagePercentage;

   /**
    * @return the name
    */
   public String getName () {
      return name;
   }

   /**
    * @param name the name to set
    */
   public void setName (String name) {
      this.name = name;
   }

   /**
    * @return the businessEntityId
    */
   public Long getBusinessEntityId () {
      return businessEntityId;
   }

   /**
    * @param businessEntityId the businessEntityId to set
    */
   public void setBusinessEntityId (Long businessEntityId) {
      this.businessEntityId = businessEntityId;
   }

   /**
    * @return the usagePercentage
    */
   public double getUsagePercentage () {
      return usagePercentage;
   }

   /**
    * @param usagePercentage the usagePercentage to set
    */
   public void setUsagePercentage (double usagePercentage) {
      this.usagePercentage = usagePercentage;
   }

   @Override
   public String toString () {
      StringBuilder sb = new StringBuilder();
      sb.append("Name : ");
      sb.append(this.getName());
      sb.append("\tBED ID : ");
      sb.append(this.getBusinessEntityId());
      sb.append("\tUsage % : ");
      sb.append(this.getUsagePercentage());
      return sb.toString();
   }
   
   @Override
   public int compareTo (QueryHistoryBusinessEntityInfo nextQueryHistoryBusinessEntityInfo) {
      int comparision = 0;
         if (nextQueryHistoryBusinessEntityInfo != null) {
            if (this.getUsagePercentage() < nextQueryHistoryBusinessEntityInfo.getUsagePercentage()) {
               comparision = 1;
            } else {
               comparision = -1;
            }
         }
      return comparision;
   }
}
