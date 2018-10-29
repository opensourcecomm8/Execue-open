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


package com.execue.web.core.action.swi;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.execue.core.ExeCueBaseTest;

public class UploadActionTest extends ExeCueBaseTest {

   private static final Logger log = Logger.getLogger(UploadActionTest.class);

   @BeforeClass
   public static void setUp () {
      baseTestSetup();
   }

   @AfterClass
   public static void tearDown () {
      baseTestTeardown();
   }

   @Test
   public void testInvokeUploadedFileAbsorption () {
      UploadAction uploadAction = getUploadAction();

      uploadAction.setPublishedFileInfoId(new Long(1013));

      uploadAction.invokeUploadedFileAbsorption();
      if (log.isInfoEnabled()) {
         log.info("Status : " + uploadAction.getJobRequestResult().getStatus());
         log.info("Error Message : " + uploadAction.getJobRequestResult().getErrMsg());
         log.info("Job Id : " + uploadAction.getJobRequestResult().getJobId());
      }
   }

}
