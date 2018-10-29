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


package com.execue.swi.service;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.execue.core.ExeCueBaseTest;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.swi.UserQueryPossibility;
import com.execue.core.common.type.BusinessEntityType;
import com.execue.core.exception.swi.SDXException;
import com.execue.core.exception.swi.SWIException;

public class UserQueryPossibilityServiceTest extends ExeCueBaseTest {

   private static final Logger log = Logger.getLogger(UserQueryPossibilityServiceTest.class);

   @BeforeClass
   public static void setUp () {
      baseTestSetup();
   }

   // @Test
   public void testCreateUserQueryPossibilities () {
      List<UserQueryPossibility> userQueryPossibilities = new ArrayList<UserQueryPossibility>();
      
      UserQueryPossibility userQueryPossibility = new UserQueryPossibility();
      userQueryPossibility.setEntityBedId(new Long(1002));
      userQueryPossibility.setEntityType(BusinessEntityType.CONCEPT);
      userQueryPossibility.setModelId(new Long(101));
      userQueryPossibility.setPossibilityId(new Long(1));
      userQueryPossibility.setTypeBasedWeight(new Double(10.0));
      userQueryPossibility.setUserQueryId(new Long(1234));
      
      userQueryPossibilities.add(userQueryPossibility);

      userQueryPossibility = new UserQueryPossibility();
      userQueryPossibility.setEntityBedId(new Long(1006));
      userQueryPossibility.setEntityType(BusinessEntityType.CONCEPT_LOOKUP_INSTANCE);
      userQueryPossibility.setModelId(new Long(101));
      userQueryPossibility.setPossibilityId(new Long(1));
      userQueryPossibility.setTypeBasedWeight(new Double(11.0));
      userQueryPossibility.setUserQueryId(new Long(1234));
      
      userQueryPossibilities.add(userQueryPossibility);

      try {
         getUserQueryPossibilityService().createUserQueryPossibilities(userQueryPossibilities);
      } catch (SWIException e) {
         fail("Creation Failed on UserQueryPossibility with code [" + e.getCode() + "] and message : " + e.getMessage());
         log.error(e, e);
      }
   } //End of test case for createUserQueryPossibilities on UserQueryPossibilityService
   
   @Test
   public void testGetLightAssets () {
      try {
         List<Long> assetIds = new ArrayList<Long>();
         assetIds.add(1L);
         assetIds.add(2L);
         assetIds.add(3L);
         assetIds.add(4L);
         List<Asset> lightAssets = getSDXService().getLightAssets(assetIds);
         System.out.println(lightAssets.size());
      } catch (SDXException e) {
         Assert.fail(e.getCode() + ", " + e.getMessage());
         e.printStackTrace();
      }
   }
}
