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


package com.execue.core.common.bean.entity;

import java.io.Serializable;

import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.Owner;

public class Type implements Serializable {

   private Long      id;

   private String    name;

   private String    description;

   private String    displayName;

   private Owner     owner;

   private CheckType abstrat;
   private CheckType visibility;
   private CheckType realizable = CheckType.YES;

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public String getName () {
      return name;
   }

   public void setName (String name) {
      this.name = name;
   }

   public String getDescription () {
      return description;
   }

   public void setDescription (String description) {
      this.description = description;
   }

   public String getDisplayName () {
      return displayName;
   }

   public void setDisplayName (String displayName) {
      this.displayName = displayName;
   }

   public Owner getOwner () {
      return owner;
   }

   public void setOwner (Owner owner) {
      this.owner = owner;
   }

   public CheckType getAbstrat () {
      return abstrat;
   }

   public void setAbstrat (CheckType abstrat) {
      this.abstrat = abstrat;
   }

   public CheckType getVisibility () {
      return visibility;
   }

   public void setVisibility (CheckType visibility) {
      this.visibility = visibility;
   }

   /**
    * @return the realizable
    */
   public CheckType getRealizable () {
      return realizable;
   }

   /**
    * @param realizable
    *           the realizable to set
    */
   public void setRealizable (CheckType realizable) {
      this.realizable = realizable;
   }

}
