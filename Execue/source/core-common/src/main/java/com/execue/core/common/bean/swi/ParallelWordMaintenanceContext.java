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


package com.execue.core.common.bean.swi;

import com.execue.core.common.bean.JobRequestIdentifier;
import com.execue.core.common.type.OperationType;

/**
 * This bean represents the ParallelWordMaintenanceContext updation context. It contains information required to invoke
 * the service
 * 
 * @author Vishay
 * @version 1.0
 * @since 01/10/09
 */
public class ParallelWordMaintenanceContext extends JobRequestIdentifier {

   private Long          userId;
   private Long          parallelWordId;
   private Long          keyWordId;
   private OperationType operationType;
   private String        originalParallelWordName;
   private Long          contextId;

   /**
    * @return the contextId
    */
   public Long getContextId () {
      return contextId;
   }

   /**
    * @param contextId the contextId to set
    */
   public void setContextId (Long contextId) {
      this.contextId = contextId;
   }

   public Long getParallelWordId () {
      return parallelWordId;
   }

   public void setParallelWordId (Long parallelWordId) {
      this.parallelWordId = parallelWordId;
   }

   public OperationType getOperationType () {
      return operationType;
   }

   public void setOperationType (OperationType operationType) {
      this.operationType = operationType;
   }

   public String getOriginalParallelWordName () {
      return originalParallelWordName;
   }

   public void setOriginalParallelWordName (String originalParallelWordName) {
      this.originalParallelWordName = originalParallelWordName;
   }

   public Long getKeyWordId () {
      return keyWordId;
   }

   public void setKeyWordId (Long keyWordId) {
      this.keyWordId = keyWordId;
   }

   public Long getUserId () {
      return userId;
   }

   public void setUserId (Long userId) {
      this.userId = userId;
   }

}
