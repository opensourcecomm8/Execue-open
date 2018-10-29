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

/**
 * @author deenu
 */
public class HierarchyDetail implements Serializable, Comparable<HierarchyDetail> {

   private Long      id;
   private Hierarchy hierarchy;
   private Long      conceptBedId;
   private Integer   level;

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public Hierarchy getHierarchy () {
      return hierarchy;
   }

   public void setHierarchy (Hierarchy hierarchy) {
      this.hierarchy = hierarchy;
   }

   public Long getConceptBedId () {
      return conceptBedId;
   }

   public void setConceptBedId (Long conceptBedId) {
      this.conceptBedId = conceptBedId;
   }

   public Integer getLevel () {
      return level;
   }

   public void setLevel (Integer level) {
      this.level = level;
   }

   @Override
   public int compareTo (HierarchyDetail inHierarchyDetail) {
      return this.level.compareTo(inHierarchyDetail.level);
   }

   @Override
   public int hashCode () {
      return this.conceptBedId.hashCode();
   }

   @Override
   public boolean equals (Object obj) {
      HierarchyDetail other = (HierarchyDetail) obj;
      return this.conceptBedId.equals(other.getConceptBedId());
   }

}
