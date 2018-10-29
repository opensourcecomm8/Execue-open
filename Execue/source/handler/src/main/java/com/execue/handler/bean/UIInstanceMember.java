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


package com.execue.handler.bean;


public class UIInstanceMember {

   private Long instanceId;
   private Long memberId;
   private String instanceDisplayName;
   private String memberValue;
   private String memberDescription;
   
   public Long getInstanceId () {
      return instanceId;
   }
   
   public void setInstanceId (Long instanceId) {
      this.instanceId = instanceId;
   }
   
   public Long getMemberId () {
      return memberId;
   }
   
   public void setMemberId (Long memberId) {
      this.memberId = memberId;
   }
   
   public String getInstanceDisplayName () {
      return instanceDisplayName;
   }
   
   public void setInstanceDisplayName (String instanceDisplayName) {
      this.instanceDisplayName = instanceDisplayName;
   }
   
   public String getMemberValue () {
      return memberValue;
   }
   
   public void setMemberValue (String memberValue) {
      this.memberValue = memberValue;
   }
   
   public String getMemberDescription () {
      return memberDescription;
   }
   
   public void setMemberDescription (String memberDescription) {
      this.memberDescription = memberDescription;
   }
   
}
