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


package com.execue.util.print;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.swi.PossibleAssetInfo;

public class SemanticDriverPrintHelper {

   private SemanticDriverPrintHelper () {

   }

   private static final Logger log = Logger.getLogger(SemanticDriverPrintHelper.class);

   public static void printPossibleAssetInfo (List<PossibleAssetInfo> possibleAssetInfoList) {
      for (PossibleAssetInfo possibleAssetInfo : possibleAssetInfoList) {
         log.info("Possible Asset Info: "+ possibleAssetInfo.getPossibilityId() +" - "+ possibleAssetInfo.getAssetId() + " - "
                  + possibleAssetInfo.getTotalTypeBasedWeight() + " - "
                  + possibleAssetInfo.getStandarizedTotalTypeBasedWeight() + " - " + possibleAssetInfo.getRelavance()
                  + " - " + possibleAssetInfo.isFromQueryCache());
      }
   }

   public static void printPossibleAssetInfo (Map<Long, List<PossibleAssetInfo>> possibleAssetForPossibilityMap) {
      for (Long posId : possibleAssetForPossibilityMap.keySet()) {
         for (PossibleAssetInfo possibleAssetInfo : possibleAssetForPossibilityMap.get(posId)) {
            log.info("Possible Asset Info: " + posId + " - " + possibleAssetInfo.getAssetId() + " - "
                     + possibleAssetInfo.getTotalTypeBasedWeight() + " - "
                     + possibleAssetInfo.getStandarizedTotalTypeBasedWeight() + " - "
                     + possibleAssetInfo.getRelavance() + " - " + possibleAssetInfo.isFromQueryCache());
         }
      }
   }

}
