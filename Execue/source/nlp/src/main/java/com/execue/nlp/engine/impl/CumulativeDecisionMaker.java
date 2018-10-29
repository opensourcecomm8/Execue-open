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


package com.execue.nlp.engine.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.math3.util.Precision;
import org.apache.log4j.Logger;

import com.execue.core.common.bean.IWeightedEntity;
import com.execue.core.common.bean.WeightInformation;
import com.execue.nlp.bean.entity.ConceptEntity;
import com.execue.nlp.bean.entity.InstanceEntity;
import com.execue.nlp.bean.entity.ProfileEntity;
import com.execue.nlp.bean.entity.RecognitionEntity;
import com.execue.nlp.bean.matrix.IterationSummary;
import com.execue.nlp.bean.matrix.Possibility;
import com.execue.nlp.bean.matrix.RootMatrix;
import com.execue.nlp.configuration.INLPConfigurationService;
import com.execue.nlp.util.NLPUtilities;
import com.execue.util.CollectionUtil;
import com.execue.util.MathUtil;

/**
 * @author Abhijit
 * @since Oct 8, 2008 : 5:09:51 PM
 */
public class CumulativeDecisionMaker {

   private INLPConfigurationService nlpConfigurationService;

   private static final Logger      log                                = Logger
                                                                                .getLogger(CumulativeDecisionMaker.class);
   // TODO -NA- put this variable in Nlpconfiguratin and read it from there
   private static final int         MAX_SIZE_OF_POSSIBILITIES_IN_MODEL = 5;

   // calculate on the basis of FW.
   public static double getAdjustedWeight (Possibility poss) {
      WeightInformation weightInformation = poss.getWeightInformation();
      return weightInformation.getFinalWeight();
   }

   public static List<Possibility> filterByThreshold (RootMatrix rootMatrix) {
      List<Possibility> possibilities = rootMatrix.getPossibilities();
      Map<Possibility, Double> possibilitiesMap = new HashMap<Possibility, Double>(1);
      for (Possibility poss : possibilities) {
         IterationSummary iSummary = poss.getCurrentIteration().getIterationSummary();
         if (!iSummary.getSummaries().isEmpty() && iSummary.getSummaries().size() == 1) {
            double weight = getAdjustedWeight(poss);
            possibilitiesMap.put(poss, weight);
         }
      }
      Map<Possibility, Double> sortedMap = CollectionUtil.sortMapOnValue(NLPUtilities
               .getRelativePercentageMap(possibilitiesMap));
      Map<Possibility, Double> filteredMap = NLPUtilities.filterMapByThreshold(sortedMap, 71);

      return new ArrayList<Possibility>(filteredMap.keySet());
   }

   /**
    * This method filters the possibilities within model and returns the best possibility per model.
    * 
    * @param possibilities
    * @return the List<Possibility>
    */
   public List<Possibility> chooseBestPossibilityPerModel (List<Possibility> possibilities) {
      Map<Long, List<Possibility>> modelIdPossibilitiesmap = getPossibilitiesByModel(possibilities);
      List<Possibility> bestPossibilityList = new ArrayList<Possibility>();
      for (Entry<Long, List<Possibility>> entry : modelIdPossibilitiesmap.entrySet()) {
         List<Possibility> modelPossibilities = entry.getValue();
         List<Possibility> bestPossibilities = new ArrayList<Possibility>(modelPossibilities);

         // Filter the possibilities based on the recognition quality of explicit entities
         bestPossibilities = filterPossibilitiesByRecognitionQuality(bestPossibilities);

         // Apply any App level filtering of possibilities
         bestPossibilities = filterPossibilitiesBasedOnAppRules(bestPossibilities);

         // Filter the possibilities based on association weight of explicit entities.
         bestPossibilities = filterPossibilitiesByAssociationWeight(bestPossibilities);

         // Filter the possibilities by FrameWork Weight
         bestPossibilities = filterPossibilitiesByFrameWorkWeight(bestPossibilities);
         // Apply the top cluster on top quality possibilities by their recognitions popularity average
         bestPossibilities = filterPossibilitiesByRecognitionAveragePopularity(bestPossibilities);

         // Filter the possibilities based on the proximity penalty.
         bestPossibilities = filterPossibilitiesByProximityPenalty(bestPossibilities);

         // Finally, Filter the possibilities based on the alphabetical order(ascending) of recognitions
         bestPossibilities = choosePossibilityByAlphabeticalOrder(bestPossibilities);

         // Add the first possibility to the best possibility list
         if (!CollectionUtils.isEmpty(bestPossibilities)) {
            bestPossibilityList.add(bestPossibilities.get(0));
         }
      }
      return bestPossibilityList;
   }

   /**
    * Apply the top cluster on top quality possibilities by their recognitions popularity average
    * 
    * @param possibilities
    * @return the List<Possibility>
    */
   protected List<Possibility> filterPossibilitiesByRecognitionAveragePopularity (List<Possibility> possibilities) {
      if (CollectionUtils.isEmpty(possibilities) || possibilities.size() <= 1) {
         return possibilities;
      }
      Map<Double, List<Possibility>> possibilitiesByPopularityMap = new LinkedHashMap<Double, List<Possibility>>(1);
      for (Possibility possibility : possibilities) {
         Double avgPopularity = NLPUtilities.getPopularityAverage(possibility.getRecognitionEntities());
         List<Possibility> popularPossibilities = possibilitiesByPopularityMap.get(avgPopularity);
         if (popularPossibilities == null) {
            popularPossibilities = new ArrayList<Possibility>(1);
            possibilitiesByPopularityMap.put(avgPopularity, popularPossibilities);
         }
         popularPossibilities.add(possibility);
      }

      // sort the possibility map based on the top highest avg popularity
      Map<Double, List<Possibility>> sortedPossibilitiesByPopularityMap = CollectionUtil
               .sortMapOnKeyAsDouble(possibilitiesByPopularityMap);

      // Get the top weight list of possibilities and return the first one
      List<Possibility> bestPossibilities = (List<Possibility>) sortedPossibilitiesByPopularityMap.values().toArray()[0];
      return bestPossibilities;
   }

   /**
    * Returns the top weight list of possibilities based on recognition weight of explicit entities.
    * 
    * @param possibilities
    * @return the List<Possibility>
    */
   protected List<Possibility> filterPossibilitiesByRecognitionQuality (List<Possibility> possibilities) {
      if (CollectionUtils.isEmpty(possibilities) || possibilities.size() <= 1) {
         return possibilities;
      }
      Map<Double, List<Possibility>> possibilitiesByQualityMap = new LinkedHashMap<Double, List<Possibility>>(1);
      for (Possibility poss : possibilities) {
         // Get the quality with 2 places of decimal as key as we might get the similar(with close) quality for some
         // possibilities which we dont want to filter here
         double possibilityQuality = Double.parseDouble(String.format("%.2f", poss
                  .getWeightInformationForExplicitEntities().getRecognitionQuality()));
         List<Possibility> posList = possibilitiesByQualityMap.get(possibilityQuality);
         if (posList == null) {
            posList = new ArrayList<Possibility>(1);
            possibilitiesByQualityMap.put(possibilityQuality, posList);
         }
         posList.add(poss);
      }

      Map<Double, List<Possibility>> sortedPossibilitiesByQualityMap = CollectionUtil
               .sortMapOnKeyAsDouble(possibilitiesByQualityMap);

      // Get the top weight list of possibilities and return the first one
      List<Possibility> bestPossibilities = (List<Possibility>) sortedPossibilitiesByQualityMap.values().toArray()[0];

      return bestPossibilities;
   }

   public List<Possibility> filterPossibilitiesBasedOnAppRules (List<Possibility> possibilities) {
      if (CollectionUtils.isEmpty(possibilities) || possibilities.size() <= 1) {
         return possibilities;
      }
      // TODO: NK: Apply the filter here and get the best possibilities, currently setting it to the input possibilities
      List<Possibility> bestPossibilities = possibilities;
      return bestPossibilities;
   }

   /**
    * Returns the list of possibilities with the lowest proximity penalty.
    * 
    * @param possibilities
    * @return the List<Possibility>
    */
   protected List<Possibility> filterPossibilitiesByProximityPenalty (List<Possibility> possibilities) {
      if (CollectionUtils.isEmpty(possibilities) || possibilities.size() <= 1) {
         return possibilities;
      }
      TreeMap<Double, List<Possibility>> possibilitiesByPenaltyMap = new TreeMap<Double, List<Possibility>>();

      for (Possibility possibility : possibilities) {
         Double proximityPeanalty = possibility.getProximityPenalty();
         List<Possibility> possList = possibilitiesByPenaltyMap.get(proximityPeanalty);
         if (possList == null) {
            possList = new ArrayList<Possibility>(1);
            possibilitiesByPenaltyMap.put(proximityPeanalty, possList);
         }
         possList.add(possibility);
      }
      Double minimuPenalty = possibilitiesByPenaltyMap.firstKey();
      List<Possibility> bestPossibilities = possibilitiesByPenaltyMap.get(minimuPenalty);
      return bestPossibilities;
   }

   /**
    * Returns the top weight list of possibilities based on association weight of explicit entities.
    * 
    * @param possibilities
    * @return the List<Possibility>
    */
   protected List<Possibility> filterPossibilitiesByAssociationWeight (List<Possibility> possibilities) {
      if (CollectionUtils.isEmpty(possibilities) || possibilities.size() <= 1) {
         return possibilities;
      }
      Map<Double, List<Possibility>> possibilitiesByQualityMap = new LinkedHashMap<Double, List<Possibility>>(1);
      for (Possibility poss : possibilities) {
         double possibilityQuality = Precision.round(poss.getWeightInfoForAssociation().getFinalWeight(), 3);
         List<Possibility> posList = possibilitiesByQualityMap.get(possibilityQuality);
         if (posList == null) {
            posList = new ArrayList<Possibility>(1);
            // rounding off the quality value by 3 decimal points.
            possibilitiesByQualityMap.put(possibilityQuality, posList);
         }
         posList.add(poss);
      }

      Map<Double, List<Possibility>> sortedPossibilitiesByQualityMap = CollectionUtil
               .sortMapOnKeyAsDouble(possibilitiesByQualityMap);

      // Get the top weight list of possibilities and return the first one
      List<Possibility> bestPossibilities = (List<Possibility>) sortedPossibilitiesByQualityMap.values().toArray()[0];
      return bestPossibilities;
   }

   /**
    * Returns the top weight list of possibilities based on association weight of explicit entities.
    * 
    * @param possibilities
    * @return the List<Possibility>
    */
   protected List<Possibility> filterPossibilitiesByFrameWorkWeight (List<Possibility> possibilities) {
      if (CollectionUtils.isEmpty(possibilities) || possibilities.size() <= 1) {
         return possibilities;
      }
      Map<Double, List<Possibility>> possibilitiesByQualityMap = new LinkedHashMap<Double, List<Possibility>>(1);
      for (Possibility poss : possibilities) {
         double possibilityQuality = poss.getFrameworkWeightInformation().getFinalWeight();
         List<Possibility> posList = possibilitiesByQualityMap.get(possibilityQuality);
         if (posList == null) {
            posList = new ArrayList<Possibility>(1);
            possibilitiesByQualityMap.put(possibilityQuality, posList);
         }
         posList.add(poss);
      }

      Map<Double, List<Possibility>> sortedPossibilitiesByQualityMap = CollectionUtil
               .sortMapOnKeyAsDouble(possibilitiesByQualityMap);

      // Get the top weight list of possibilities and return the first one
      List<Possibility> bestPossibilities = (List<Possibility>) sortedPossibilitiesByQualityMap.values().toArray()[0];
      return bestPossibilities;
   }

   /**
    * This method filters the possibilities within model. First it filters by the quality of recognitions for
    * possibility. Then it filters by popularity of recognitions for possibility. Finally it filters it by alphabetical
    * order of recognitions for possibility.
    * 
    * @param possibilities
    * @return the List<Possibility>
    */
   public List<Possibility> filterByCluster (List<Possibility> possibilities) {
      Map<Long, List<Possibility>> modelIdPossibilitiesmap = getPossibilitiesByModel(possibilities);
      List<Possibility> possibilityList = new ArrayList<Possibility>();
      for (Entry<Long, List<Possibility>> entry : modelIdPossibilitiesmap.entrySet()) {
         List<Possibility> modelPossibilities = entry.getValue();
         // Apply the top cluster based on the recognition quality in the possibility
         Map<Double, List<Possibility>> possibilitiesByQualityMap = new LinkedHashMap<Double, List<Possibility>>(1);
         for (Possibility poss : modelPossibilities) {
            double possibilityQuality = poss.getWeightInformationForExplicitEntities().getRecognitionQuality();
            List<Possibility> posList = possibilitiesByQualityMap.get(possibilityQuality);
            if (posList == null) {
               posList = new ArrayList<Possibility>(1);
               possibilitiesByQualityMap.put(possibilityQuality, posList);
            }
            posList.add(poss);
         }
         List<Double> qualityList = new ArrayList<Double>(possibilitiesByQualityMap.keySet());
         // Get the top quality list based on the top cluster with execue approach
         List<Double> topQualities = MathUtil.getTopCluster(qualityList);
         // List<Double> topQualities = MathUtil.getTopCluster(qualityList);
         List<Possibility> bestQualityPossibilities = new ArrayList<Possibility>(1);
         for (Double topQuality : topQualities) {
            bestQualityPossibilities.addAll(possibilitiesByQualityMap.get(topQuality));
         }

         // if (bestQualityPossibilities.size() > 1) {
         // // Apply the top cluster on top quality possibilities by their recognitions popularity average
         // Map<Double, List<Possibility>> possibilitiesByPopularityMap = new LinkedHashMap<Double, List<Possibility>>(
         // 1);
         // for (Possibility possibility : bestQualityPossibilities) {
         // Double avgPopularity = NLPUtilities.getPopularityAverage(possibility.getRecognitionEntities());
         // List<Possibility> popularPossibilities = possibilitiesByPopularityMap.get(avgPopularity);
         // if (popularPossibilities == null) {
         // popularPossibilities = new ArrayList<Possibility>(1);
         // possibilitiesByPopularityMap.put(avgPopularity, popularPossibilities);
         // }
         // popularPossibilities.add(possibility);
         // }
         //
         // // sort the possibility map based on the top highest avg popularity
         // Map<Double, List<Possibility>> sortedPossibilitiesByPopularityMap = CollectionUtil
         // .sortMapOnKeyAsDouble(possibilitiesByPopularityMap);
         //
         // // Get the top weight list of possibilities and return the first one
         // bestQualityPossibilities = (List<Possibility>) sortedPossibilitiesByPopularityMap.values().toArray()[0];
         // }
         possibilityList.addAll(bestQualityPossibilities);
      }
      return possibilityList;
   }

   protected static List<Possibility> choosePossibilityByAlphabeticalOrder (List<Possibility> possibilities) {

      // Check if only one possibility, return the same
      if (CollectionUtils.isEmpty(possibilities) || possibilities.size() <= 1) {
         return possibilities;
      }

      // Sort the possibility based on the tokens natural order
      SortedMap<String, List<Possibility>> sortedMap = new TreeMap<String, List<Possibility>>();
      for (Possibility possibility : possibilities) {
         String key = getAlphabeticalKey(possibility);
         if (log.isDebugEnabled()) {
            log.debug(possibility.getModel().getName() + " - " + possibility.getId() + " - "
                     + possibility.getPossibilityWeight() + " - " + key);
         }
         List<Possibility> list = sortedMap.get(key);
         if (list == null) {
            list = new ArrayList<Possibility>(1);
         }
         list.add(possibility);
         sortedMap.put(key, list);
      }

      // Return the list of the first alphabetical key
      return (List<Possibility>) sortedMap.values().toArray()[0];
   }

   public static String getAlphabeticalKey (Possibility possibility) {
      StringBuilder sb = new StringBuilder();

      for (IWeightedEntity weightedEntity : possibility.getRecognitionEntities()) {
         RecognitionEntity recognitionEntity = (RecognitionEntity) weightedEntity;
         StringBuilder word = new StringBuilder(recognitionEntity.getWord());
         if (recognitionEntity instanceof InstanceEntity) {
            String conceptName = ((ConceptEntity) recognitionEntity).getConceptDisplayName();
            if (conceptName != null) {
               word.append("-" + conceptName);
            }
            word.append("-");
            word.append(((InstanceEntity) recognitionEntity).getInstanceDisplayString());

         } else if (recognitionEntity instanceof ProfileEntity) {
            word.append(((ProfileEntity) recognitionEntity).getProfileName());
         }
         sb.append(word.toString().replaceAll(" ", "").toLowerCase());
      }

      return sb.toString();

   }

   /**
    * @param topLiberalPossibilities
    * @return
    */
   public static Map<Long, List<Possibility>> getPossibilitiesByModel (List<Possibility> topLiberalPossibilities) {
      Map<Long, List<Possibility>> modelIdPossibilitiesMap = new LinkedHashMap<Long, List<Possibility>>();
      for (Possibility possibility : topLiberalPossibilities) {
         List<Possibility> possibilityList = modelIdPossibilitiesMap.get(possibility.getModel().getId());
         if (CollectionUtils.isEmpty(possibilityList)) {
            possibilityList = new ArrayList<Possibility>();
         }
         possibilityList.add(possibility);
         modelIdPossibilitiesMap.put(possibility.getModel().getId(), possibilityList);
      }
      return modelIdPossibilitiesMap;
   }

   /**
    * @param possibilities
    * @return
    */
   public static List<Possibility> filterPossibilitiesForModelByAverageRecognitionWeight (
            List<Possibility> possibilities) {
      List<Possibility> finalPossibilities = new ArrayList<Possibility>();
      // get the possibility Map by the ModelId.
      Map<Long, List<Possibility>> modelPossibilityMap = getPossibilitiesByModel(possibilities);
      for (Entry<Long, List<Possibility>> entry : modelPossibilityMap.entrySet()) {
         if (entry.getValue().size() > MAX_SIZE_OF_POSSIBILITIES_IN_MODEL) {
            List<Double> allWeightVals = new ArrayList<Double>();
            Map<Possibility, Double> possibilityWeightMap = new HashMap<Possibility, Double>();
            for (Possibility poss : entry.getValue()) {
               double weight = poss.getCurrentIteration().getInput().getAverageOntoRecognitionWeight();
               allWeightVals.add(weight);
               possibilityWeightMap.put(poss, weight);
            }
            List<Double> topCluster = MathUtil.getTopCluster(allWeightVals);
            for (Entry<Possibility, Double> entry2 : possibilityWeightMap.entrySet()) {
               if (topCluster.contains(entry2.getValue().doubleValue())) {
                  finalPossibilities.add(entry2.getKey());
               }
            }

         } else {
            finalPossibilities.addAll(entry.getValue());
         }
      }
      return finalPossibilities;
   }

   /**
    * Method to filter the possibilities across the apps by top cluster. This is done only when the number of
    * possibilities are greater thn a configured number in xml.
    * 
    * @param possibilities
    * @return
    */
   public List<Possibility> filterPossibilitiesAcrossApps (List<Possibility> possibilities) {
      if (possibilities.size() > nlpConfigurationService.getMaxAllowedPossibilitiesForQuery()) {
         // IF there are more than specified number of Possibilities take only top 5
         // When TOP 5 can not be determined based on weight, use alphabetical ordering
         // PossibilityWeightComparator is used for this
         Collections.sort(possibilities, new PossibilityWeightComparator());
         List<Possibility> finalPossibilities = possibilities.subList(0, nlpConfigurationService
                  .getMaxAllowedPossibilitiesForQuery());
         // List<Possibility> finalPossibilities = new ArrayList<Possibility>();
         // Map<Possibility, Double> possibilityWeightMap = new HashMap<Possibility, Double>();
         // List<Double> allWeightVals = new ArrayList<Double>();
         // for (Possibility poss : possibilities) {
         // double weight = poss.getCurrentIteration().getInput().getAverageOntoRecognitionWeight();
         // allWeightVals.add(weight);
         // possibilityWeightMap.put(poss, weight);
         // }
         // List<Double> topCluster = MathUtil.getTopCluster(allWeightVals);
         // for (Entry<Possibility, Double> entry : possibilityWeightMap.entrySet()) {
         // if (topCluster.contains(entry.getValue().doubleValue())) {
         // finalPossibilities.add(entry.getKey());
         // }
         // }
         return finalPossibilities;
      }
      return possibilities;
   }

   /**
    * @return the nlpConfigurationService
    */
   public INLPConfigurationService getNlpConfigurationService () {
      return nlpConfigurationService;
   }

   /**
    * @param nlpConfigurationService
    *           the nlpConfigurationService to set
    */
   public void setNlpConfigurationService (INLPConfigurationService nlpConfigurationService) {
      this.nlpConfigurationService = nlpConfigurationService;
   }
}

class PossibilityWeightComparator implements Comparator {

   public int compare (Object o1, Object o2) {
      if (o1 instanceof Possibility && o2 instanceof Possibility) {
         Comparable c1 = ((Possibility) o1).getCurrentIteration().getInput().getAverageOntoRecognitionWeight();
         Comparable c2 = ((Possibility) o2).getCurrentIteration().getInput().getAverageOntoRecognitionWeight();
         int equal = c2.compareTo(c1);
         if (equal == 0) {
            String key1 = ((Possibility) o1).getCurrentIteration().getInput().getAlphabeticalKey();
            String key2 = ((Possibility) o2).getCurrentIteration().getInput().getAlphabeticalKey();
            return key1.compareTo(key2);
         }
         return equal;
      } else {
         Comparable c1 = (Comparable) o1;
         Comparable c2 = (Comparable) o2;
         return c2.compareTo(c1);
      }
   }
}
