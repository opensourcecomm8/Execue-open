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

import com.execue.core.common.type.JoinType;

public class JoinEntity {

   private QueryTableColumn lhsOperand;
   private JoinType         type;
   private QueryTableColumn rhsOperand;

   public QueryTableColumn getLhsOperand () {
      return lhsOperand;
   }

   public void setLhsOperand (QueryTableColumn lhsOperand) {
      this.lhsOperand = lhsOperand;
   }

   public QueryTableColumn getRhsOperand () {
      return rhsOperand;
   }

   public void setRhsOperand (QueryTableColumn rhsOperand) {
      this.rhsOperand = rhsOperand;
   }

   public JoinType getType () {
      return type;
   }

   public void setType (JoinType type) {
      this.type = type;
   }

}
