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


package com.execue.nlp.bean.entity;

/**
 * @author Abhijit
 * @since Apr 3, 2009 : 1:19:56 PM
 */
public class ProfileEntity extends ConceptEntity {

   private static final long serialVersionUID = 1L;
   private String            profileName;
   private Long              profileID;

   @Override
   public Object clone () throws CloneNotSupportedException {
      ProfileEntity profileEntity = (ProfileEntity) super.clone();
      profileEntity.setProfileName(this.getProfileName());
      profileEntity.setProfileID(this.getProfileID());
      return profileEntity;
   }

   public String getProfileName () {
      return profileName;
   }

   public void setProfileName (String profileName) {
      this.profileName = profileName;
   }

   public Long getProfileID () {
      return profileID;
   }

   public void setProfileID (Long profileID) {
      this.profileID = profileID;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.nlp.bean.entity.ConceptEntity#getDisplayName()
    */
   @Override
   public String getDisplayName () {
      return profileName;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.nlp.bean.entity.OntoEntity#getId()
    */
   @Override
   public Long getId () {
      return profileID;
   }
}
