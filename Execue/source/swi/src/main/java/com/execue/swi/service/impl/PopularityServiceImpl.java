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


package com.execue.swi.service.impl;

import java.util.List;

import com.execue.core.common.bean.entity.Model;
import com.execue.core.common.bean.swi.PopularityHit;
import com.execue.core.common.type.TermType;
import com.execue.swi.exception.SWIException;
import com.execue.swi.dataaccess.IPopularityDataAccessManager;
import com.execue.swi.service.IPopularityService;

/**
 * @author John Mallavalli
 */
public class PopularityServiceImpl implements IPopularityService {

   private IPopularityDataAccessManager popularityDataAccessManager;

   public void updatePopularity (Long termId, TermType type, Long hits) throws SWIException {
      getPopularityDataAccessManager().updatePopularity(termId, type, hits);
   }

   public void updatePopularity (PopularityHit popularityHit) throws SWIException {
      getPopularityDataAccessManager().updatePopularity(popularityHit);
   }

   public void saveAll (List<PopularityHit> popularityHits) throws SWIException {
      getPopularityDataAccessManager().saveAll(popularityHits);
   }

   public void deleteProcessedPopularityHit () throws SWIException {
      getPopularityDataAccessManager().deleteProcessedPopularityHit();
   }

   public void updateRIOntoTermsPopularity (Long modelId, int batchSize) throws SWIException {
      getPopularityDataAccessManager().updateRIOntoTermsPopularity(modelId, batchSize);

   }

   public List<Model> updateTermsBasedOnPopularity (int batchSize) throws SWIException {
      return getPopularityDataAccessManager().updateTermsBasedOnPopularity(batchSize);

   }

   public IPopularityDataAccessManager getPopularityDataAccessManager () {
      return popularityDataAccessManager;
   }

   public void setPopularityDataAccessManager (IPopularityDataAccessManager popularityDataAccessManager) {
      this.popularityDataAccessManager = popularityDataAccessManager;
   }

}
