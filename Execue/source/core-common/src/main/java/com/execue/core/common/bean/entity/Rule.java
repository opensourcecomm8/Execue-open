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
package com.execue.core.common.bean.entity;

import java.io.Serializable;
import java.util.Set;

/**
 * @author nitesh
 */
public class Rule implements Serializable {

   private static final long      serialVersionUID = 1L;

   private Long                   id;
   private String                 name;
   private Set<Cloud>             clouds;
   private Set<PossibleAttribute> possibleAttributes;

   public Set<Cloud> getClouds () {
      return clouds;
   }

   public void setClouds (Set<Cloud> clouds) {
      this.clouds = clouds;
   }

   /**
    * @return the id
    */
   public Long getId () {
      return id;
   }

   /**
    * @param id
    *           the id to set
    */
   public void setId (Long id) {
      this.id = id;
   }

   /**
    * @return the name
    */
   public String getName () {
      return name;
   }

   /**
    * @param name
    *           the name to set
    */
   public void setName (String name) {
      this.name = name;
   }

   public Set<PossibleAttribute> getPossibleAttributes () {
      return possibleAttributes;
   }

   public void setPossibleAttributes (Set<PossibleAttribute> possibleAttributes) {
      this.possibleAttributes = possibleAttributes;
   }
}
