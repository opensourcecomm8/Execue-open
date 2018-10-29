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

import java.util.List;

import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.Tabl;
import com.execue.core.common.type.ConstraintType;

/**
 * This bean represents the fully populated constraint object logically. The Constraint bean represents one row in our
 * schema but this object represents the entire constraint info
 * 
 * @author Vishay
 * @version 1.0
 * @since 15/07/09
 */
public class UIConstraint {

   private Long           constraintId;
   private String         name;
   private ConstraintType type;
   private Tabl           referenceTable;
   private List<Colum>    referenceColumns;
   private List<Colum>    constraintColums;

   

   public String getName () {
      return name;
   }

   public void setName (String name) {
      this.name = name;
   }

   public ConstraintType getType () {
      return type;
   }

   public void setType (ConstraintType type) {
      this.type = type;
   }

   public Tabl getReferenceTable () {
      return referenceTable;
   }

   public void setReferenceTable (Tabl referenceTable) {
      this.referenceTable = referenceTable;
   }

   public List<Colum> getReferenceColumns () {
      return referenceColumns;
   }

   public void setReferenceColumns (List<Colum> referenceColumns) {
      this.referenceColumns = referenceColumns;
   }

   public List<Colum> getConstraintColums () {
      return constraintColums;
   }

   public void setConstraintColums (List<Colum> constraintColums) {
      this.constraintColums = constraintColums;
   }

   
   public Long getConstraintId () {
      return constraintId;
   }

   
   public void setConstraintId (Long constraintId) {
      this.constraintId = constraintId;
   }
}

