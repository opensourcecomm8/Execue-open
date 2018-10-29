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


package com.execue.sdata.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.execue.core.common.bean.sdata.location.LocationPointInfo;
import com.execue.core.common.bean.sdata.location.LocationSuggestTerm;
import com.execue.core.common.bean.sdata.location.StateCity;
import com.execue.core.common.type.NormalizedLocationType;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.sdata.SharedDataCommonBaseTest;
import com.execue.sdata.exception.LocationException;

public class LocationRetrievalServiceTest extends SharedDataCommonBaseTest {

   private static final Logger logger = Logger.getLogger(LocationRetrievalServiceTest.class);

   @BeforeClass
   public static void setUp () {
      baseSetup();
   }

   @AfterClass
   public static void tearDown () {
      baseTeardown();
   }

   // @Test
   public void testGetLocationPointsByZipCodes () {

      List<String> zipCodes = new ArrayList<String>();
      zipCodes.add("NYK");
      try {
         List<LocationPointInfo> locationPointInfoList = getLocationRetrievalService().getLocationPointsByZipCodes(
                  zipCodes, NormalizedLocationType.ZIPCODE);
         for (LocationPointInfo locationPointInfo : locationPointInfoList) {
            logger.info(locationPointInfo.getLocationBedId());
         }
      } catch (LocationException e) {
         e.printStackTrace();
      }

   }

   // @Test
   public void testGetLocationPointsByBedIds () {

      List<Long> locationBedIds = new ArrayList<Long>();
      locationBedIds.add(667183L);
      try {
         List<LocationPointInfo> locationPointInfoList = getLocationRetrievalService().getLocationPointsByBedIds(
                  locationBedIds, NormalizedLocationType.ZIPCODE);

         for (LocationPointInfo locationPointInfo : locationPointInfoList) {
            logger.info(locationPointInfo.getZipCode());
         }
      } catch (LocationException e) {
         e.printStackTrace();
      }
   }

   @Test
   public void testGetLocationPointInfo () {
      // Long locationEntityBedId = 667321L;
      String zipCode = "99923";
      try {
         // LocationPointInfo locationPointInfoByBedId = getLocationRetrievalService()
         // .getLocationPointInfoByLocationBedId(locationEntityBedId);
         LocationPointInfo locationPointInfoByZipCode = getLocationRetrievalService().getLocationPointInfoByZipCode(
                  zipCode);
         System.out.println(locationPointInfoByZipCode.getId());
         // System.out.println(locationPointInfoByBedId.getId());

      } catch (LocationException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testGetValidStateCityCombinations () {
      List<StateCity> validStateCityCombinations = null;
      List<Long> stateIds = new ArrayList<Long>();
      List<Long> cityIds = new ArrayList<Long>();
      try {
         stateIds.add(667181L);
         stateIds.add(667196L);
         stateIds.add(667199L);

         cityIds.add(667183L);
         cityIds.add(667187L);
         cityIds.add(667197L);
         cityIds.add(667193L);
         cityIds.add(667194L);
         cityIds.add(667205L);

         validStateCityCombinations = getLocationRetrievalService().getValidStateCityCombinations(stateIds, cityIds);
         if (ExecueCoreUtil.isCollectionNotEmpty(validStateCityCombinations)) {
            System.out.println("Valid StateCity combinations are [State Id, City Id] :");
            for (StateCity stateCity : validStateCityCombinations) {
               System.out.println("[" + stateCity.getStateId() + ", " + stateCity.getCityId() + "]");
            }
         } else {
            System.out.println("No valid combinations");
         }
      } catch (LocationException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testGetLocationPointInfoBySearchString () {

      try {
         List<LocationSuggestTerm> suggestLocationTermsBySearchString = getLocationRetrievalService()
                  .suggestLocationBySearchString("a", 10);

         System.out.println(suggestLocationTermsBySearchString);
      } catch (LocationException e) {
         e.printStackTrace();
      }
   }

}
