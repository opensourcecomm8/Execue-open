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
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.execue.core.common.bean.entity;

import java.util.Set;

import com.execue.core.common.type.ProfileType;

/**
 *
 * @author venu
 */
public class InstanceProfile extends Profile {

   private Set<Instance> instances;
   
   private Concept concept;

   @Override
   public ProfileType getType() {
      return ProfileType.CONCEPT_LOOKUP_INSTANCE;
   }

   
   public Set<Instance> getInstances () {
      return instances;
   }

   
   public void setInstances (Set<Instance> instances) {
      this.instances = instances;
   }

   
   public Concept getConcept () {
      return concept;
   }

   
   public void setConcept (Concept concept) {
      this.concept = concept;
   }

}
