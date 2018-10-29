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


public class UIJoinDefintionInfo {

   private String lhsColumn;
   private String rhsColumn;
   private String type;
   private String checkedState;
   private boolean isSuggestedJoin;

   public String getLhsColumn () {
      return lhsColumn;
   }

   public void setLhsColumn (String lhsColumn) {
      this.lhsColumn = lhsColumn;
   }

   public String getRhsColumn () {
      return rhsColumn;
   }

   public void setRhsColumn (String rhsColumn) {
      this.rhsColumn = rhsColumn;
   }

   public String getType () {
      return type;
   }

   public void setType (String type) {
      this.type = type;
   }

   public String getCheckedState () {
      return checkedState;
   }

   public void setCheckedState (String checkedState) {
      this.checkedState = checkedState;
   }

   
   public boolean getSuggestedJoin () {
      return isSuggestedJoin;
   }

   
   public void setSuggestedJoin (boolean isSuggestedJoin) {
      this.isSuggestedJoin = isSuggestedJoin;
   }

   
   

}
