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
 * This bean represents the primary key structure.
 * 
 * @author Vishay
 * @version 07/03/09 
 */
public class PrimaryKeyEntity {

   private Tabl        tabl;
   private List<Colum> colums;

   public List<Colum> getColums () {
      return colums;
   }

   public void setColums (List<Colum> colums) {
      this.colums = colums;
   }

   public Tabl getTabl () {
      return tabl;
   }

   public void setTabl (Tabl tabl) {
      this.tabl = tabl;
   }
}
