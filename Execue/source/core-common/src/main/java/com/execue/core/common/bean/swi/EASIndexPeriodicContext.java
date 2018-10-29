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

/**
 * This bean represents  EASIndexPeriodicContext. It contains information required to invoke
 *  service for EASIndexPeriodicJob
 * 
 * @author Jitendra
 * @version 1.0
 * @since 27/09/11
 */
public class EASIndexPeriodicContext extends JobRequestIdentifier {

   /**
    * 
    */
   private static final long serialVersionUID = 1L;
   private Long              applicationId;

   /**
    * @return the applicationId
    */
   public Long getApplicationId () {
      return applicationId;
   }

   /**
    * @param applicationId the applicationId to set
    */
   public void setApplicationId (Long applicationId) {
      this.applicationId = applicationId;
   }

}
