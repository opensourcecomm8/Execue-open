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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.execue.core.common.bean.optimaldset.OptimalDSet;
import com.execue.core.common.bean.optimaldset.OptimalDSetDimension;

public abstract class AbstractAlgorithm implements OptimalDSetAlgorithm {

   int                         minCommonDimension;
   double                      maxCostPerSet;
   double                      maxWeigtedCostPerSet;
   int                         maxSetsToReturn;

   private static final Logger logger = Logger.getLogger(AbstractAlgorithm.class);

   protected Set<OptimalDSet> removeSubSets (Set<OptimalDSet> sets) {
      if (CollectionUtils.isEmpty(sets) || sets.size() == 1) {
         return sets;
      }
      Set<OptimalDSet> resultSet = new HashSet<OptimalDSet>();
      logger.debug("Initial Size " + resultSet.size());
      for (OptimalDSet optimalDSet : sets) {
         List<OptimalDSet> removeSet = new ArrayList<OptimalDSet>();
         boolean isSubSet = false;
         for (OptimalDSet rOptimalDSet : resultSet) {
            if (optimalDSet.isSubSet(rOptimalDSet)) {
               if (logger.isDebugEnabled()) {
                  logger.debug("Removing " + rOptimalDSet.getName());
               }
               removeSet.add(rOptimalDSet);
            }
            if (rOptimalDSet.isSubSet(optimalDSet)) {
               isSubSet = true;
               break;
            }
         }
         if (logger.isDebugEnabled()) {
            logger.debug("Adding " + optimalDSet.getName());
         }
         if (!removeSet.isEmpty()) {
            resultSet.removeAll(removeSet);
         }
         if (!isSubSet) {
            resultSet.add(optimalDSet);
         }
      }
      if (logger.isDebugEnabled()) {
         logger.debug("final Size " + resultSet.size());
      }
      return resultSet;

   }

   protected List<OptimalDSet> sort (Collection<OptimalDSet> optimalDSets, Comparator<OptimalDSet> comparator) {
      if (CollectionUtils.isEmpty(optimalDSets)) {
         return new ArrayList<OptimalDSet>(1);
      }
      List<OptimalDSet> sortedDSet = new ArrayList<OptimalDSet>(optimalDSets);
      if (optimalDSets.size() == 1) {
         return sortedDSet;
      }
      Collections.sort(sortedDSet, comparator);
      return sortedDSet;
   }

   protected OptimalDSet merge (OptimalDSet set1, OptimalDSet set2) {
      if (logger.isDebugEnabled()) {
         logger.debug(" Trying to Merge " + set1.getName() + " with " + set2.getName());
      }
      if (canBeMerged(set1, set2) == true) {
         if (checkUnionCost(set1, set2)) {
            if (logger.isDebugEnabled()) {
               logger.debug("Needs to be merged " + set1.getName() + "," + set2.getName());
            }
            return join(set1, set2);
         }

      }
      if (logger.isDebugEnabled()) {
         logger.debug("Cannot be Merged " + set1.getName() + "," + set2.getName());
      }
      return null;

   }

   protected OptimalDSet join (OptimalDSet set1, OptimalDSet set2) {
      HashSet<OptimalDSetDimension> dimSet = new HashSet<OptimalDSetDimension>();
      dimSet.addAll(set1.getDimensions());
      dimSet.addAll(set2.getDimensions());
      OptimalDSet optimalDSet = new OptimalDSet("(" + set1.getName() + " U " + set2.getName() + ")", dimSet, set1
               .getUsagePercentage()
               + set2.getUsagePercentage());
      return optimalDSet;
   }

   protected boolean canBeMerged (OptimalDSet set1, OptimalDSet set2) {

      HashSet<OptimalDSetDimension> s1 = new HashSet<OptimalDSetDimension>();
      s1.addAll(set1.getDimensions());
      HashSet<OptimalDSetDimension> s2 = new HashSet<OptimalDSetDimension>();
      s2.addAll(set2.getDimensions());
      if (s1.containsAll(s2)) {
         if (logger.isDebugEnabled()) {
            logger.debug(set1.getName() + " is SuperSet of " + set2.getName() + " returning false");
         }
         return false;
      }
      s1.retainAll(s2);
      if (s1.size() < minCommonDimension) {
         if (logger.isDebugEnabled()) {
            logger.debug(set1.getName() + "and " + set2.getName() + " do not have atleast common " + minCommonDimension
                     + " dimension(s) returning false");
         }
         return false;
      }
      logger.debug("Intersection requiremnets successful for " + set1.getName() + "," + set2.getName());
      return true;

   }

   protected boolean checkUnionCost (OptimalDSet set1, OptimalDSet set2) {
      // double cost =set1.getCost()+set2.getCost();
      set1.getDimensions();
      HashSet<OptimalDSetDimension> set = new HashSet<OptimalDSetDimension>();
      set.addAll(set1.getDimensions());
      set.addAll(set2.getDimensions());
      double cost = 0.0;
      for (OptimalDSetDimension dimension : set) {
         if (cost == 0.0) {
            cost = dimension.getNoOfMembers();
         } else {
            cost *= dimension.getNoOfMembers();
         }
      }

      if (cost > maxCostPerSet) {
         if (logger.isDebugEnabled()) {
            logger.debug("Union Cost " + cost + " is greater than " + maxCostPerSet);
         }
         return false;
      }
      if (logger.isDebugEnabled()) {
         logger.debug("Satisfied Union Cost Requirements");
      }
      return true;
   }

   public int getMinCommonDimension () {
      return minCommonDimension;
   }

   public void setMinCommonDimension (int minCommonDimension) {
      this.minCommonDimension = minCommonDimension;
   }

   public double getMaxCostPerSet () {
      return maxCostPerSet;
   }

   public void setMaxCostPerSet (double maxCostPerSet) {
      this.maxCostPerSet = maxCostPerSet;
   }

   public double getMaxWeigtedCostPerSet () {
      return maxWeigtedCostPerSet;
   }

   public void setMaxWeigtedCostPerSet (double maxWeigtedCostPerSet) {
      this.maxWeigtedCostPerSet = maxWeigtedCostPerSet;
   }

   public int getMaxSetsToReturn () {
      return maxSetsToReturn;
   }

   public void setMaxSetsToReturn (int maxSetsToReturn) {
      this.maxSetsToReturn = maxSetsToReturn;
   }

   protected boolean checkWeigtedCost (OptimalDSet set1, OptimalDSet set2) {

      double cost = (set1.getCost() + set2.getCost()) / (set1.getUsagePercentage() + set2.getUsagePercentage());
      if (cost > maxWeigtedCostPerSet) {
         return false;
      }
      return true;
   }

   protected Collection<OptimalDSetDimension> getAllDimensions (Map<String, OptimalDSetDimension> dimensionByNameMap) {
      List<OptimalDSetDimension> dims = new ArrayList<OptimalDSetDimension>();
      dims.addAll(dimensionByNameMap.values());
      return dims;
   }

}
