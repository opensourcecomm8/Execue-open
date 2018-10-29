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

/**
 * Starting and Ending both numbers are inclusive. I.E in order to get records top 10, we need to put starting as 1 and
 * ending as 10. 11 and 20 to get records from 11 to 20th.
 * 
 * @author Raju Gottumukkala
 */
public class LimitEntity {

   private Long startingNumber = 1L;
   private Long endingNumber;

   public Long getStartingNumber () {
      return startingNumber;
   }

   public void setStartingNumber (Long startingNumber) {
      this.startingNumber = startingNumber;
   }

   public Long getEndingNumber () {
      return endingNumber;
   }

   public void setEndingNumber (Long endingNumber) {
      this.endingNumber = endingNumber;
   }

}
