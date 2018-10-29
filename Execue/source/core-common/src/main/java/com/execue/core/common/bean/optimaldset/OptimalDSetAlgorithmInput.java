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


package com.execue.core.common.bean.optimaldset;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * This bean represents the OptimalDSet algorithm input object. 
 * 
 * @author Vishay
 * @version 1.0
 * @since 27/06/09
 */
public class OptimalDSetAlgorithmInput {

   private List<OptimalDSetDimension>        dimensions;
   private List<OptimalDSet>                 optimalDSets;
   private boolean                           isOptimalDSetsUpdatedForDimensions = false;
   private Map<String, OptimalDSetDimension> dimensionByNameMap;

   public List<OptimalDSetDimension> getDimensions () {
      return dimensions;
   }

   public void setDimensions (List<OptimalDSetDimension> dimensions) {
      this.dimensions = dimensions;
   }

   public List<OptimalDSet> getOptimalDSets () {
      if (!isOptimalDSetsUpdatedForDimensions) {
         populateDimensionByNameMap();
         updateOptimalDSetsForDimensions();
         isOptimalDSetsUpdatedForDimensions = true;
      }
      return optimalDSets;
   }

   public void setOptimalDSets (List<OptimalDSet> optimalDSets) {
      this.optimalDSets = optimalDSets;
   }

   private Map<String, OptimalDSetDimension> getDimensionByNameMap () {
      return dimensionByNameMap;
   }

   private void populateDimensionByNameMap () {
      dimensionByNameMap = new HashMap<String, OptimalDSetDimension>();
      for (OptimalDSetDimension dimension : getDimensions()) {
         dimensionByNameMap.put(dimension.getName(), dimension);
      }
   }

   private void updateOptimalDSetsForDimensions () {
      for (OptimalDSet dset : optimalDSets) {
         List<String> dimNames = Arrays.asList(StringUtils.split(dset.getDimensionNames(), ","));
         List<OptimalDSetDimension> dims = new ArrayList<OptimalDSetDimension>();
         for (String dimName : dimNames) {
            dims.add(getDimensionByNameMap().get(dimName));
         }
         dset.setDimensions(dims);
      }
   }
}
