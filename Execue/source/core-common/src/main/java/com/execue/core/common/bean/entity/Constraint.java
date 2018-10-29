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
import java.util.Set;

import com.execue.core.common.type.ConstraintType;

public class Constraint implements Serializable {

   private static final long serialVersionUID = 1L;
   private Long              id;
   private Long              constraintId;
   private String            name;
   private ConstraintType    type;
   private String            description;
   private Tabl              referenceTable;
   private Colum             referenceColumn;
   private int               columOrder;
   private Set<Colum>        constraintColums;

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

   public Tabl getReferenceTable () {
      return referenceTable;
   }

   public void setReferenceTable (Tabl referenceTable) {
      this.referenceTable = referenceTable;
   }

   public Colum getReferenceColumn () {
      return referenceColumn;
   }

   public void setReferenceColumn (Colum referenceColumn) {
      this.referenceColumn = referenceColumn;
   }

   public Set<Colum> getConstraintColums () {
      return constraintColums;
   }

   public void setConstraintColums (Set<Colum> constraintColums) {
      this.constraintColums = constraintColums;
   }

   public int getColumOrder () {
      return columOrder;
   }

   public void setColumOrder (int columOrder) {
      this.columOrder = columOrder;
   }

   public ConstraintType getType () {
      return type;
   }

   public void setType (ConstraintType type) {
      this.type = type;
   }

   
   public Long getConstraintId () {
      return constraintId;
   }

   
   public void setConstraintId (Long constraintId) {
      this.constraintId = constraintId;
   }
}
