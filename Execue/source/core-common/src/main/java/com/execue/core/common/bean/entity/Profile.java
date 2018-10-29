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


/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */

package com.execue.core.common.bean.entity;

import java.io.Serializable;
import java.util.Set;

import com.execue.core.common.bean.IBusinessEntity;
import com.execue.core.common.bean.security.User;
import com.execue.core.common.type.ProfileType;

/**
 * @author venu
 */
public abstract class Profile implements IBusinessEntity, Serializable {

   private Long                          id;
   private ModelGroup                    modelGroup;
   private String                        name;
   private String                        displayName;
   private String                        description;
   private boolean                       enabled;
   private User                          user;

   private Set<BusinessEntityDefinition> businessEntityDefinitions;

   /* private DomainEntityDefinition profileConcept; */

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

   public String getDisplayName () {
      return displayName;
   }

   public void setDisplayName (String displayName) {
      this.displayName = displayName;
   }

   public String getDescription () {
      return description;
   }

   public void setDescription (String description) {
      this.description = description;
   }

   public abstract ProfileType getType ();

   public boolean isEnabled () {
      return enabled;
   }

   public void setEnabled (boolean enabled) {
      this.enabled = enabled;
   }

   public User getUser () {
      return user;
   }

   public void setUser (User user) {
      this.user = user;
   }

   public Set<BusinessEntityDefinition> getBusinessEntityDefinitions () {
      return businessEntityDefinitions;
   }

   public void setBusinessEntityDefinitions (Set<BusinessEntityDefinition> businessEntityDefinitions) {
      this.businessEntityDefinitions = businessEntityDefinitions;
   }

   public ModelGroup getModelGroup () {
      return modelGroup;
   }

   public void setModelGroup (ModelGroup modelGroup) {
      this.modelGroup = modelGroup;
   }

   /*
    * public DomainEntityDefinition getProfileConcept () { return profileConcept; } public void setProfileConcept
    * (DomainEntityDefinition profileConcept) { this.profileConcept = profileConcept; }
    */

}
