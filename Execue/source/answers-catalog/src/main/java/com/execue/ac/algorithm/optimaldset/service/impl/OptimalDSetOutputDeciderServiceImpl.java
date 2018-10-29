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


package com.execue.ac.algorithm.optimaldset.service.impl;

/**
 * @author Vishay
 * @version 1.0
 * @since 18/02/2012
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.execue.ac.algorithm.optimaldset.bean.OptimalDSetCubeOutputInfo;
import com.execue.ac.algorithm.optimaldset.bean.OptimalDSetLevelOutputInfo;
import com.execue.ac.algorithm.optimaldset.comparator.OptimalDSetLevelOutputInfoSpaceComparator;
import com.execue.ac.algorithm.optimaldset.comparator.OptimalDSetLevelOutputInfoUsageComparator;
import com.execue.ac.algorithm.optimaldset.service.IOptimalDSetOutputDeciderService;
import com.execue.ac.exception.AnswersCatalogException;

public class OptimalDSetOutputDeciderServiceImpl implements IOptimalDSetOutputDeciderService {

   /**
    * This method chooses the best output among all the levels. Rules are take the best usage. among the best usage take
    * the least space taken.
    */
   @Override
   public OptimalDSetCubeOutputInfo chooseBestOptimalDSet (List<OptimalDSetLevelOutputInfo> levelOutputInfoList)
            throws AnswersCatalogException {
      Collections.sort(levelOutputInfoList, new OptimalDSetLevelOutputInfoUsageComparator());
      Double bestUsage = levelOutputInfoList.get(levelOutputInfoList.size() - 1).getTotalUsage();
      List<OptimalDSetLevelOutputInfo> bestUsageLevels = findMatchingLevels(levelOutputInfoList, bestUsage);

      Collections.sort(bestUsageLevels, new OptimalDSetLevelOutputInfoSpaceComparator());
      OptimalDSetLevelOutputInfo bestLevelOutput = bestUsageLevels.get(0);

      OptimalDSetCubeOutputInfo cubeOutputInfo = new OptimalDSetCubeOutputInfo();
      cubeOutputInfo.setLevelPatterns(bestLevelOutput.getConstrainedLevelPatterns());
      cubeOutputInfo.setTotalSpace(bestLevelOutput.getTotalSpace());
      cubeOutputInfo.setTotalUsage(bestLevelOutput.getTotalUsage());
      cubeOutputInfo.setTotalSpaceInPercentageOfParentAsset(bestLevelOutput.getTotalSpaceInPercentageOfParentAsset());
      return cubeOutputInfo;
   }

   private List<OptimalDSetLevelOutputInfo> findMatchingLevels (List<OptimalDSetLevelOutputInfo> levelOutputInfoList,
            Double usage) {
      List<OptimalDSetLevelOutputInfo> matchedLevels = new ArrayList<OptimalDSetLevelOutputInfo>();
      for (OptimalDSetLevelOutputInfo levelOutputInfo : levelOutputInfoList) {
         if (levelOutputInfo.getTotalUsage().equals(usage)) {
            matchedLevels.add(levelOutputInfo);
         }
      }
      return matchedLevels;
   }

}
