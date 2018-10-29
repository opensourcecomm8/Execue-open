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

/**
 * This bean contains the entire constraints information for the given table
 * 
 * @author Vishay
 * @version 1.0
 * @since 15/07/09
 */
public class UITableConstraintsInfo {

   private Tabl               table;
   private List<Colum>        columns;
   private UIConstraint       pkConstraint;
   private List<UIConstraint> fkConstraints;
   private List<UIConstraint> pkReferences;

   public Tabl getTable () {
      return table;
   }

   public void setTable (Tabl table) {
      this.table = table;
   }

   public UIConstraint getPkConstraint () {
      return pkConstraint;
   }

   public void setPkConstraint (UIConstraint pkConstraint) {
      this.pkConstraint = pkConstraint;
   }

   public List<UIConstraint> getFkConstraints () {
      return fkConstraints;
   }

   public void setFkConstraints (List<UIConstraint> fkConstraints) {
      this.fkConstraints = fkConstraints;
   }

   public List<UIConstraint> getPkReferences () {
      return pkReferences;
   }

   public void setPkReferences (List<UIConstraint> pkReferences) {
      this.pkReferences = pkReferences;
   }

   public List<Colum> getColumns () {
      return columns;
   }

   public void setColumns (List<Colum> columns) {
      this.columns = columns;
   }

}
