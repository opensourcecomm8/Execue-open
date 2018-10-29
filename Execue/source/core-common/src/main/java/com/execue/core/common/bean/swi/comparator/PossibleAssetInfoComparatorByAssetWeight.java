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


package com.execue.core.common.bean.swi.comparator;

import java.util.Comparator;

import com.execue.core.common.bean.swi.PossibleAssetInfo;

/**
 * This class is uses as a comparator to sort in descending order the PossibleAssetInfo on basis of their Weights.
 * 
 * @author Nitesh
 * @version 1.0
 * @since 12/23/09
 */
public class PossibleAssetInfoComparatorByAssetWeight implements Comparator<PossibleAssetInfo> {

   /**
    * This method will compare two PossibleAssetInfo on basis of their type based weight. If any of the
    * PossibleAssetInfo dont have weight, we are considering them as equal.
    * 
    * @param possibleAssetInfo1
    * @param possibleAssetInfo2
    * @return 0,-1,+1 value indicating the comparison result
    */

   public int compare (PossibleAssetInfo possibleAssetInfo1, PossibleAssetInfo possibleAssetInfo2) {
      if (possibleAssetInfo1.getTotalTypeBasedWeight() > possibleAssetInfo2.getTotalTypeBasedWeight()) {
         return -1;
      } else if (possibleAssetInfo1.getTotalTypeBasedWeight() < possibleAssetInfo2.getTotalTypeBasedWeight()) {
         return +1;
      } else {
         // -NA- done to remove the ambiguity when two assets are of same weight . when same weight is tehre sorting the
         // assets by ID.
         // return possibleAssetInfo2.getAssetId().compareTo(possibleAssetInfo1.getAssetId());
         return 0;
      }
   }
}
