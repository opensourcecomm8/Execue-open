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
package com.execue.nlp.bean.entity;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

/**
 * This Class will contain the information about previous rec Entity for which the hits needs to be updated. As of Now
 * This keeps Information about SflToken which are participating for the recognition of this entity.
 * 
 * @author nihar
 */
public class HitsUpdateInfo {

   Set<Long> sflTokensIds;

   /**
    * @return the sflTokens
    */
   public Set<Long> getSflTokensIds () {
      if (CollectionUtils.isEmpty(sflTokensIds)) {
         sflTokensIds = new HashSet<Long>(1);
      }
      return sflTokensIds;
   }

   /**
    * @param sflTokens
    *           the sflTokens to set
    */
   public void setSflTokensIds (Set<Long> sflTokens) {
      this.sflTokensIds = sflTokens;
   }
}
