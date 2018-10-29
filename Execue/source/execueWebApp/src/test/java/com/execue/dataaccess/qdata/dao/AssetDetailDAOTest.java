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


package com.execue.dataaccess.qdata.dao;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.execue.core.ExeCueBaseTest;
import com.execue.core.exception.dataaccess.DataAccessException;

/**
 * This class is Junit test class for testing DAO operations on AssetDetail object.
 * 
 * @author MurthySN
 * @version 1.0
 * @since 09/01/10
 */
public class AssetDetailDAOTest extends ExeCueBaseTest {

   private static final Logger logger = Logger.getLogger(AssetDetailDAOTest.class);

   @BeforeClass
   public static void setup () {
      logger.debug("Inside the Setup Method");
      baseTestSetup();
   }

   @Test
   public void testAssetExtendedDisclaimerByAssetDetailId () {
      Long assetDetailId = 1L;
      try {
         String extendedDisclaimerString = getAssetDetailDAO().getAssetExtendedDetailInfo(assetDetailId).getExtendedDisclaimer();
         logger.debug("Extended Disclaimer String" + extendedDisclaimerString);

      } catch (DataAccessException e) {
         e.printStackTrace();
      }
   }

   @Test
   public void testAssetExtendedNoteByAssetDetailId () {
      Long assetDetailId = 1L;
      try {
         String extendedNoteString = getAssetDetailDAO().getAssetExtendedDetailInfo(assetDetailId).getExtendedNote();
         logger.debug("Extended Note String" + extendedNoteString);

      } catch (DataAccessException e) {
         e.printStackTrace();
      }
   }

   @AfterClass
   public static void teardown () {
      logger.debug("Inside the TearDown Method");
      baseTestTeardown();
   }

}
