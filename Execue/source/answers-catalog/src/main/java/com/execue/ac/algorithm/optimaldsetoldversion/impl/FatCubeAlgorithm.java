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


package com.execue.ac.algorithm.optimaldsetoldversion.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.optimaldset.OptimalDSet;
import com.execue.core.common.bean.optimaldset.OptimalDSetAlgorithmInput;
import com.execue.core.common.bean.optimaldset.OptimalDSetDimension;
import com.execue.core.common.bean.optimaldset.comparator.OptimalDSetComparator;

public class FatCubeAlgorithm extends HistoricalDataAlgorithm {

   private static final Logger logger = Logger.getLogger(FatCubeAlgorithm.class);

   public Collection<OptimalDSet> computeOptimalDSets (OptimalDSetAlgorithmInput optimalDSetAlgorithmInput) {
      Collection<OptimalDSetDimension> dimensions = optimalDSetAlgorithmInput.getDimensions();
      Collection<OptimalDSet> initSets = new ArrayList<OptimalDSet>();
      for (OptimalDSetDimension dimension : dimensions) {
         OptimalDSet optimalDSet = new OptimalDSet();
         List<OptimalDSetDimension> dims = new ArrayList<OptimalDSetDimension>();
         dims.add(dimension);
         optimalDSet.setDimensions(dims);
         optimalDSet.setName(dimension.getName());
         optimalDSet.setUsagePercentage(0);
         initSets.add(optimalDSet);
      }
      this.setMinCommonDimension(0);
      Collection<OptimalDSet> finalSets = this.generateOptimalSets(initSets);
      List<OptimalDSet> resultSets = this.sort(finalSets, new OptimalDSetComparator(false, true));

      if (maxSetsToReturn > 0 && resultSets.size() > maxSetsToReturn) {
         if (logger.isDebugEnabled()) {
            logger.debug("Returning only " + maxSetsToReturn);
         }
         return resultSets.subList(0, maxSetsToReturn);
      }
      return resultSets;

   }

}
