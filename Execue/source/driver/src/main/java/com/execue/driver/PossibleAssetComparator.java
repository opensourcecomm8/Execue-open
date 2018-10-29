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
package com.execue.driver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.execue.core.common.bean.nlp.SemanticPossibility;
import com.execue.core.common.bean.entity.Application;
import com.execue.core.common.bean.swi.PossibleAssetInfo;

/**
 * Comparator to compare the Possibility based on teh possible Asset info List stored.
 * 
 * @author Nihar
 */
public class PossibleAssetComparator implements Comparator<Long> {

   Map<Long, List<PossibleAssetInfo>> possibleAssetForPossibilityMap;
   Map<Long, SemanticPossibility>     possibilityMap;

   /**
    * @return the possibleAssetForPossibilityMap
    */
   public Map<Long, List<PossibleAssetInfo>> getPossibleAssetForPossibilityMap () {
      return possibleAssetForPossibilityMap;
   }

   /**
    * @param possibleAssetForPossibilityMap
    *           the possibleAssetForPossibilityMap to set
    */
   public void setPossibleAssetForPossibilityMap (Map<Long, List<PossibleAssetInfo>> possibleAssetForPossibilityMap) {
      this.possibleAssetForPossibilityMap = possibleAssetForPossibilityMap;
   }

   /**
    * @return the possibilityMap
    */
   public Map<Long, SemanticPossibility> getPossibilityMap () {
      return possibilityMap;
   }

   /**
    * @param possibilityMap
    *           the possibilityMap to set
    */
   public void setPossibilityMap (Map<Long, SemanticPossibility> possibilityMap) {
      this.possibilityMap = possibilityMap;
   }

   public int compare (Long possibilityId1, Long possibilityId2) {

      // step1 Compare the Top Weight assetInfor against the Possibility ID.
      List<PossibleAssetInfo> assetInfos1 = new ArrayList<PossibleAssetInfo>(1);
      assetInfos1.addAll(possibleAssetForPossibilityMap.get(possibilityId1));
      List<PossibleAssetInfo> assetInfos2 = new ArrayList<PossibleAssetInfo>(1);
      assetInfos2.addAll(possibleAssetForPossibilityMap.get(possibilityId2));
      Collections.sort(assetInfos1, new Comparator<PossibleAssetInfo>() {

         public int compare (PossibleAssetInfo o1, PossibleAssetInfo o2) {
            return (int) (o2.getRelavance() - o1.getRelavance());
         }
      });
      Collections.sort(assetInfos2, new Comparator<PossibleAssetInfo>() {

         public int compare (PossibleAssetInfo o1, PossibleAssetInfo o2) {
            return (int) (o2.getRelavance() - o1.getRelavance());
         }
      });
      double weight1 = assetInfos1.get(0).getRelavance();
      double weight2 = assetInfos2.get(0).getRelavance();
      if (weight2 > weight1) {
         return 1;
      } else if (weight2 < weight1) {
         return -1;
      } else {
         // Step 2 compare the App Popularity for the Possibility Application
         SemanticPossibility semanticPossibility1 = possibilityMap.get(possibilityId1);
         Application application1 = semanticPossibility1.getApplication();
         SemanticPossibility semanticPossibility2 = possibilityMap.get(possibilityId2);
         Application application2 = semanticPossibility2.getApplication();
         if (application2.getPopularity() > application1.getPopularity()) {
            return 1;
         } else if (application2.getPopularity() < application1.getPopularity()) {
            return -1;
         }

         /*
          * // Step 3 Compare the App Name. // return application2.getName().compareTo(application1.getName()); //
          * Commented above line and added below if else, so that this comparator // does just sorting than filtering
          */

         if (!application1.getId().equals(application2.getId())) {
            // Step 3 Compare the App Name.
            return application2.getName().compareTo(application1.getName());
         } else {
            // Step 4 order by possibilityId
            return possibilityId1.compareTo(possibilityId2);
         }
      }
   }
}
