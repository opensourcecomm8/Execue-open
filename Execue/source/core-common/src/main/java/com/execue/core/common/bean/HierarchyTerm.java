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

import java.util.List;

import com.execue.core.common.bean.governor.BusinessAssetTerm;

public class HierarchyTerm {

   private Long                    hierarchyId;
   private String                  hierarchyName;
   private List<BusinessTerm>      hierarchyBusinessDefinition;
   private List<BusinessAssetTerm> hierarchyBusinessAssetDefinition;
   private List<Long>              participatingQueryEntityIDs;

   public Long getHierarchyId () {
      return hierarchyId;
   }

   public void setHierarchyId (Long hierarchyId) {
      this.hierarchyId = hierarchyId;
   }

   public String getHierarchyName () {
      return hierarchyName;
   }

   public void setHierarchyName (String hierarchyName) {
      this.hierarchyName = hierarchyName;
   }

   public List<BusinessTerm> getHierarchyBusinessDefinition () {
      return hierarchyBusinessDefinition;
   }

   public void setHierarchyBusinessDefinition (List<BusinessTerm> hierarchyBusinessDefinition) {
      this.hierarchyBusinessDefinition = hierarchyBusinessDefinition;
   }

   public List<BusinessAssetTerm> getHierarchyBusinessAssetDefinition () {
      return hierarchyBusinessAssetDefinition;
   }

   public void setHierarchyBusinessAssetDefinition (List<BusinessAssetTerm> hierarchyBusinessAssetDefinition) {
      this.hierarchyBusinessAssetDefinition = hierarchyBusinessAssetDefinition;
   }

   public List<Long> getParticipatingQueryEntityIDs () {
      return participatingQueryEntityIDs;
   }

   public void setParticipatingQueryEntityIDs (List<Long> participatingQueryEntityIDs) {
      this.participatingQueryEntityIDs = participatingQueryEntityIDs;
   }

}
