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


package com.execue.core.common.util;

import java.util.ArrayList;
import java.util.List;

import com.execue.core.common.bean.entity.Mapping;
import com.execue.core.common.type.AssetGrainType;
import com.execue.core.util.ExecueCoreUtil;

/**
 * This class is utility class for getting different parts of grain
 * 
 * @author Vishay
 * @version 1.0
 * @since 17/06/09
 */
public class AssetGrainUtils {

   /**
    * This method returns the default population concept among the grain concepts
    * 
    * @param assetGrain
    * @return defaultPopulation
    */
   public static Mapping getDefaultPopulationConcept (List<Mapping> assetGrain) {
      Mapping defaultPopulationMapping = null;
      if (ExecueCoreUtil.isCollectionNotEmpty(assetGrain)) {
         for (Mapping mapping : assetGrain) {
            if (AssetGrainType.DEFAULT_POPULATION_CONCEPT.equals(mapping.getAssetGrainType())) {
               defaultPopulationMapping = mapping;
               break;
            }
         }
      }
      return defaultPopulationMapping;
   }

   /**
    * This method returns the default distribution concept among the grain concepts
    * 
    * @param assetGrain
    * @return defaultDistribution
    */
   public static Mapping getDefaultDistributionConcept (List<Mapping> assetGrain) {
      Mapping defaultDistributionMapping = null;
      if (ExecueCoreUtil.isCollectionNotEmpty(assetGrain)) {
         for (Mapping mapping : assetGrain) {
            if (AssetGrainType.DEFAULT_DISTRIBUTION_CONCEPT.equals(mapping.getAssetGrainType())) {
               defaultDistributionMapping = mapping;
               break;
            }
         }
      }
      return defaultDistributionMapping;
   }

   /**
    * This method returns only the distribution concepts among the grain concepts
    * 
    * @param assetGrain
    * @return distributionConcepts
    */
   public static List<Mapping> getDistributionConcepts (List<Mapping> assetGrain) {
      List<Mapping> distributionConcepts = new ArrayList<Mapping>();
      for (Mapping mapping : assetGrain) {
         if (AssetGrainType.DISTRIBUTION_CONCEPT.equals(mapping.getAssetGrainType())) {
            distributionConcepts.add(mapping);
         }
      }
      return distributionConcepts;
   }

   /**
    * This method returns only the population concepts among the grain concepts
    * 
    * @param assetGrain
    * @return populationConcepts
    */
   public static List<Mapping> getPopulationConcepts (List<Mapping> assetGrain) {
      List<Mapping> populationConcepts = new ArrayList<Mapping>();
      for (Mapping mapping : assetGrain) {
         if (AssetGrainType.POPULATION_CONCEPT.equals(mapping.getAssetGrainType())) {
            populationConcepts.add(mapping);
         }
      }
      return populationConcepts;
   }

   /**
    * This method returns only the distribution concepts among the grain concepts
    * 
    * @param assetGrain
    * @return distributionConcepts
    */
   public static List<Mapping> getAllDistributionConcepts (List<Mapping> assetGrain) {
      List<Mapping> distributionConcepts = new ArrayList<Mapping>();
      for (Mapping mapping : assetGrain) {
         if (AssetGrainType.DISTRIBUTION_CONCEPT.equals(mapping.getAssetGrainType())
                  || AssetGrainType.DEFAULT_DISTRIBUTION_CONCEPT.equals(mapping.getAssetGrainType())) {
            distributionConcepts.add(mapping);
         }
      }
      return distributionConcepts;
   }

}
