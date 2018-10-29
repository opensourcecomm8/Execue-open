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
package com.execue.core.common.bean.entity.wrapper;

import java.util.List;

import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.Membr;
import com.execue.core.common.bean.entity.Tabl;

/**
 * @author Jayadev
 */
public class TableInfo {

   private Tabl table;
   private List<Colum> columns;
   private List<Membr> members;

   public List<Colum> getColumns () {
      return columns;
   }

   public void setColumns (List<Colum> columns) {
      this.columns = columns;
   }

   public List<Membr> getMembers () {
      return members;
   }

   public void setMembers (List<Membr> members) {
      this.members = members;
   }

   
   public Tabl getTable () {
      return table;
   }

   
   public void setTable (Tabl table) {
      this.table = table;
   }
}
