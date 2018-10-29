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


package com.execue.core.common.bean.governor;

/**
 * @author jtiwari
 */
public class BusinessEntityInfo {

   private Long   businessEntityTermId;
   private String businessEntityTermDisplayName;
   private String parentBusinessEntityTermDisplayName;

   /**
    * @return the businessEntityTermId
    */
   public Long getBusinessEntityTermId () {
      return businessEntityTermId;
   }

   /**
    * @param businessEntityTermId
    *           the businessEntityTermId to set
    */
   public void setBusinessEntityTermId (Long businessEntityTermId) {
      this.businessEntityTermId = businessEntityTermId;
   }

   /**
    * @return the businessEntityTermDisplayName
    */
   public String getBusinessEntityTermDisplayName () {
      return businessEntityTermDisplayName;
   }

   /**
    * @param businessEntityTermDisplayName
    *           the businessEntityTermDisplayName to set
    */
   public void setBusinessEntityTermDisplayName (String businessEntityTermDisplayName) {
      this.businessEntityTermDisplayName = businessEntityTermDisplayName;
   }

   /**
    * @return the parentBusinessEntityTermDisplayName
    */
   public String getParentBusinessEntityTermDisplayName () {
      return parentBusinessEntityTermDisplayName;
   }

   /**
    * @param parentBusinessEntityTermDisplayName the parentBusinessEntityTermDisplayName to set
    */
   public void setParentBusinessEntityTermDisplayName (String parentBusinessEntityTermDisplayName) {
      this.parentBusinessEntityTermDisplayName = parentBusinessEntityTermDisplayName;
   }

}
