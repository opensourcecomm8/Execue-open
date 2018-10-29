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

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.execue.core.common.bean.optimaldset.OptimalDSet;
import com.execue.core.common.bean.optimaldset.OptimalDSetAlgorithmInput;
import com.execue.core.common.bean.optimaldset.comparator.OptimalDSetComparator;

public class HistoricalDataAlgorithm extends AbstractAlgorithm {

   private static final Logger logger        = Logger.getLogger(HistoricalDataAlgorithm.class);
   double                      inputCoverage = 100;
   int                         maxIterations;

   public Collection<OptimalDSet> computeOptimalDSets (OptimalDSetAlgorithmInput optimalDSetAlgorithmInput) {
      Collection<OptimalDSet> inputSets = optimalDSetAlgorithmInput.getOptimalDSets();
      if (logger.isInfoEnabled()) {
         logger.info("Size of Input DataSets " + inputSets.size());
      }
      OptimalDSetComparator optimalDSetComparator = new OptimalDSetComparator(true, false);
      inputSets = filterDSetsByInputCoverage(inputSets, optimalDSetComparator);
      if (logger.isDebugEnabled()) {
         logger.debug("Size after accepting " + inputCoverage + " is " + inputSets.size());
      }
      Collection<OptimalDSet> finalSets = this.generateOptimalSets(inputSets);
      List<OptimalDSet> resultSets = this.sort(finalSets, optimalDSetComparator);

      // NK: We should not remove DSets further as it will impact the total query coverage criteria, hence commented
      // if (maxSetsToReturn > 0 && resultSets.size() > maxSetsToReturn) {
      // if (logger.isDebugEnabled()) {
      // logger.debug("Returning only " + maxSetsToReturn);
      // }
      // return resultSets.subList(0, maxSetsToReturn);
      // }

      return resultSets;

   }

   /**
    * This method filters the input DSets by the configured coverage limit
    * 
    * @param inputSets
    * @param optimalDSetComparator
    * @return the Set<OptimalDSet>
    */
   private Collection<OptimalDSet> filterDSetsByInputCoverage (Collection<OptimalDSet> inputSets,
            OptimalDSetComparator optimalDSetComparator) {
      if (CollectionUtils.isEmpty(inputSets) || inputCoverage >= 100) {
         return inputSets;
      }

      // Sort the input DSets by the usage combination
      Set<OptimalDSet> filteredDSet = new HashSet<OptimalDSet>(1);
      SortedSet<OptimalDSet> sortedDSet = new TreeSet<OptimalDSet>(optimalDSetComparator);
      sortedDSet.addAll(inputSets);
      double totalUsagePercentage = 0;
      if (inputCoverage < 100) {
         Iterator<OptimalDSet> iter = sortedDSet.iterator();
         while (iter.hasNext()) {
            if (totalUsagePercentage >= inputCoverage) {
               break;
            }
            OptimalDSet optimalDSet = iter.next();
            totalUsagePercentage += optimalDSet.getUsagePercentage();
            filteredDSet.add(optimalDSet);
         }
      }
      return filteredDSet;
   }

   public Collection<OptimalDSet> generateOptimalSets (Collection<OptimalDSet> sets) {
      if (CollectionUtils.isEmpty(sets) || sets.size() == 1) {
         return sets;
      }
      Set<OptimalDSet> finalSets = new HashSet<OptimalDSet>(1);
      finalSets.addAll(sets);
      int i = 0;
      do {
         if (logger.isDebugEnabled()) {
            logger.debug("Iteration #" + i);
         }
         Set<OptimalDSet> tempSets = new HashSet<OptimalDSet>(1);
         for (OptimalDSet optimalDSet : finalSets) {
            for (OptimalDSet initSet : sets) {
               OptimalDSet mergedSet = merge(optimalDSet, initSet);

               if (mergedSet != null) {
                  tempSets.add(mergedSet);
               }
            }
         }
         // Break if there are no merged set or the output merged set is part of final set
         if (tempSets.size() == 0 || finalSets.containsAll(tempSets)) {
            if (logger.isDebugEnabled()) {
               logger.debug("Cannot find more sets to combine after iteration#" + i);
            }
            break;
         }
         if (logger.isDebugEnabled()) {
            logger.debug(" Adding " + tempSets.size() + " after iteration#" + i);
         }
         finalSets.addAll(tempSets);
         i++;
      } while (i < maxIterations);
      if (logger.isDebugEnabled()) {
         logger.debug("Removing SubSets");
      }
      finalSets = removeSubSets(finalSets);
      return finalSets;

   }

   public double getInputCoverage () {
      return inputCoverage;
   }

   public void setInputCoverage (double inputCoverage) {
      this.inputCoverage = inputCoverage;
   }

   public int getMaxIterations () {
      return maxIterations;
   }

   public void setMaxIterations (int maxIterations) {
      this.maxIterations = maxIterations;
   }

}