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

import java.io.Externalizable;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.execue.core.ExeCueBaseTest;
import com.execue.core.exception.swi.AssetSynchronizationException;

/**
 * This class will test the asset synchronization methods
 * 
 * @author Vishay
 * @version 1.0
 * @since 21/08/09
 */
public class AssetSynchronizationServiceTest extends ExeCueBaseTest {

   @Before
   public void setup () {
      baseTestSetup();
   }

   @After
   public void tearDown () {
      baseTestTeardown();
   }

   //@Test
   public void testAssetSyncAbsorption () {
      Long modelId = 100L;
      try {
         getAssetSyncAbsorptionService().absorbAssetSyncInfo(11L, modelId);
      } catch (AssetSynchronizationException e) {
         e.printStackTrace();
      }
   }

   @Test
   public void testAssetSynchronizationService () {
      Long modelId = 100L;
      try {
         getAssetSynchronizationService().assetSynchronization(11L, modelId);
      } catch (AssetSynchronizationException e) {
         e.printStackTrace();
      }
   }

   //@Test
   public void testAssetPopulateService () {
      try {
         getAssetSyncPopulateService().populateAssetSyncInfo(11L);
      } catch (AssetSynchronizationException e) {
         e.printStackTrace();
      }
   }
}
