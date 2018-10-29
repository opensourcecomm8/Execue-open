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

import com.execue.core.common.bean.security.User;

/**
 * @author Raju Gottumukkala
 */
public class Range implements Serializable {

   private Long             id;
   private String           name;
   private String           description;
   private Long             conceptBedId;
   private boolean          enabled;
   private Set<RangeDetail> rangeDetails;
   private User             user;
   private transient String conceptDisplayName;

   /**
    * @return the conceptBedId
    */
   public Long getConceptBedId () {
      return conceptBedId;
   }

   /**
    * @param conceptBedId the conceptBedId to set
    */
   public void setConceptBedId (Long conceptBedId) {
      this.conceptBedId = conceptBedId;
   }

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

   public boolean isEnabled () {
      return enabled;
   }

   public void setEnabled (boolean enabled) {
      this.enabled = enabled;
   }

   public Set<RangeDetail> getRangeDetails () {
      return rangeDetails;
   }

   public void setRangeDetails (Set<RangeDetail> rangeDetails) {
      this.rangeDetails = rangeDetails;
   }

   public User getUser () {
      return user;
   }

   public void setUser (User user) {
      this.user = user;
   }

   public String toString () {

      StringBuffer rangeRep = new StringBuffer();
      // Removing the usage of concept object to avoid lazy initialization issue
      // rangeRep.append("Range[" + name + "] on concept[" + concept.getName() + "] -- ");
      rangeRep.append("Range[" + name + "]  -- ");
      for (RangeDetail detail : rangeDetails) {
         rangeRep.append("RangeDetail :: [value:" + detail.getValue() + "--" + "lowerLimit:" + detail.getLowerLimit()
                  + "--" + "upperLimit:" + detail.getUpperLimit() + "]--");
      }
      return rangeRep.toString();
   }

   /**
    * @return the conceptDisplayName
    */
   public String getConceptDisplayName () {
      return conceptDisplayName;
   }

   /**
    * @param conceptDisplayName the conceptDisplayName to set
    */
   public void setConceptDisplayName (String conceptDisplayName) {
      this.conceptDisplayName = conceptDisplayName;
   }
}