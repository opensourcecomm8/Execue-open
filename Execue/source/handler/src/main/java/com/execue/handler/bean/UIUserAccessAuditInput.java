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

/**
 * This UI object represents user input from UI  to search audit log
 * @author Jitendra
 *
 */

public class UIUserAccessAuditInput {

   private Long         id;             //dummy id
   private List<Long>   userIds;
   private List<String> auditLogTypeIds;
   private String       operator;
   private List<String> operands;

   /**
    * @return the userIds
    */
   public List<Long> getUserIds () {
      return userIds;
   }

   /**
    * @param userIds the userIds to set
    */
   public void setUserIds (List<Long> userIds) {
      this.userIds = userIds;
   }

   /**
    * @return the auditLogTypeIds
    */
   public List<String> getAuditLogTypeIds () {
      return auditLogTypeIds;
   }

   /**
    * @param auditLogTypeIds the auditLogTypeIds to set
    */
   public void setAuditLogTypeIds (List<String> auditLogTypeIds) {
      this.auditLogTypeIds = auditLogTypeIds;
   }

   /**
    * @return the operator
    */
   public String getOperator () {
      return operator;
   }

   /**
    * @param operator the operator to set
    */
   public void setOperator (String operator) {
      this.operator = operator;
   }

   /**
    * @return the operands
    */
   public List<String> getOperands () {
      return operands;
   }

   /**
    * @param operands the operands to set
    */
   public void setOperands (List<String> operands) {
      this.operands = operands;
   }

   /**
    * @return the id
    */
   public Long getId () {
      return id;
   }

   /**
    * @param id the id to set
    */
   public void setId (Long id) {
      this.id = id;
   }

}
