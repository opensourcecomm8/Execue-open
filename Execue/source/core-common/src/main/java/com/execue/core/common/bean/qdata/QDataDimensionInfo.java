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


package com.execue.core.common.bean.qdata;

/**
 * This bean represents the Dimension in the system. It contains dimension name asked in the group by section of query
 * and in how many queries it has been asked
 * 
 * @author Vishay
 * @version 1.0
 * @since 25/06/09
 */
public class QDataDimensionInfo {

   private String name;
   private Long   businessEntityId;
   private long   occurences;
   private long   noOfMembers;

   public String getName () {
      return name;
   }

   public void setName (String name) {
      this.name = name;
   }

   public long getOccurences () {
      return occurences;
   }

   public void setOccurences (long occurences) {
      this.occurences = occurences;
   }

   /**
    * @return the noOfMembers
    */
   public long getNoOfMembers () {
      return noOfMembers;
   }

   /**
    * @param noOfMembers the noOfMembers to set
    */
   public void setNoOfMembers (long noOfMembers) {
      this.noOfMembers = noOfMembers;
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

}
