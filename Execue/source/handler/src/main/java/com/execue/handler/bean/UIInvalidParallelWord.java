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

public class UIInvalidParallelWord {

   private String  name;
   private boolean isSystemVariable;
   private boolean isExactBusinessTerm;
   private boolean isDuplicate;

   /**
    * @return the name
    */
   public String getName () {
      return name;
   }

   /**
    * @param name the name to set
    */
   public void setName (String name) {
      this.name = name;
   }

   /**
    * @return the isSystemVariable
    */
   public boolean isSystemVariable () {
      return isSystemVariable;
   }

   /**
    * @param isSystemVariable the isSystemVariable to set
    */
   public void setSystemVariable (boolean isSystemVariable) {
      this.isSystemVariable = isSystemVariable;
   }

   /**
    * @return the isExactBusinessTerm
    */
   public boolean isExactBusinessTerm () {
      return isExactBusinessTerm;
   }

   /**
    * @param isExactBusinessTerm the isExactBusinessTerm to set
    */
   public void setExactBusinessTerm (boolean isExactBusinessTerm) {
      this.isExactBusinessTerm = isExactBusinessTerm;
   }

   /**
    * @return the isDuplicate
    */
   public boolean isDuplicate () {
      return isDuplicate;
   }

   /**
    * @param isDuplicate the isDuplicate to set
    */
   public void setDuplicate (boolean isDuplicate) {
      this.isDuplicate = isDuplicate;
   }

}
