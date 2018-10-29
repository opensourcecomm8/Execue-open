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
import com.execue.core.common.type.TermType;

/**
 * This bean represents the ParallelWordMaintenanceContext updation context. It contains information required to invoke
 * the service
 * 
 * @author Vishay
 * @version 1.0
 * @since 01/10/09
 */
public class PopularityHitContext extends JobRequestIdentifier {

   private Long          termId;
   private TermType      termType;
   private Long          hits;
   private PopularityHit popularityHit;
   private Long          userId;

   public Long getUserId () {
      return userId;
   }

   public void setUserId (Long userId) {
      this.userId = userId;
   }

   public Long getTermId () {
      return termId;
   }

   public void setTermId (Long termId) {
      this.termId = termId;
   }

   public TermType getTermType () {
      return termType;
   }

   public void setTermType (TermType termType) {
      this.termType = termType;
   }

   public Long getHits () {
      return hits;
   }

   public void setHits (Long hits) {
      this.hits = hits;
   }

   public PopularityHit getPopularityHit () {
      return popularityHit;
   }

   public void setPopularityHit (PopularityHit popularityHit) {
      this.popularityHit = popularityHit;
   }

}
