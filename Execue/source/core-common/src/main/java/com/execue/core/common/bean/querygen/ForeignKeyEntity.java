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


package com.execue.core.common.bean.querygen;

import java.util.List;

import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.Tabl;

/**
 * This bean represents the foreign key structure.
 * 
 * @author Vishay
 * @version 07/03/09
 */
public class ForeignKeyEntity {

   private Tabl        parentTable;
   private Tabl        childTable;
   private List<Colum> parentColums;
   private List<Colum> childColums;

   public Tabl getParentTable () {
      return parentTable;
   }

   public void setParentTable (Tabl parentTable) {
      this.parentTable = parentTable;
   }

   public Tabl getChildTable () {
      return childTable;
   }

   public void setChildTable (Tabl childTable) {
      this.childTable = childTable;
   }

   public List<Colum> getParentColums () {
      return parentColums;
   }

   public void setParentColums (List<Colum> parentColums) {
      this.parentColums = parentColums;
   }

   public List<Colum> getChildColums () {
      return childColums;
   }

   public void setChildColums (List<Colum> childColums) {
      this.childColums = childColums;
   }
}
