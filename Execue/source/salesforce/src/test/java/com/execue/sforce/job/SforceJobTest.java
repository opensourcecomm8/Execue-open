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


package com.execue.sforce.job;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.execue.core.common.bean.entity.JobRequest;
import com.execue.sforce.SforceBaseTest;
import com.execue.sforce.bean.SforceLoginContext;
import com.execue.sforce.exception.SforceException;

/**
 * This class represents the test case of sforce job 
 * 
 * @author Vishay
 * @version 1.0
 * @since 11/08/09
 */
public class SforceJobTest extends SforceBaseTest {

   @Before
   public void setUp () {
      baseTestSetup();
   }

   @After
   public void tearDown () {
      baseTestTearDown();
   }

   @Test
   public void testSforceJob () {
      JobRequest jobRequest = new JobRequest();
      jobRequest.setId(15L);
      jobRequest.setUserId(1L);
      SforceLoginContext loginToSforce;
      try {
         loginToSforce = getSforceLoginService().loginToSforce();
         loginToSforce.setJobRequest(jobRequest);
         getSforceDataReplicationService().startReplication(loginToSforce);
      } catch (SforceException e) {
         e.printStackTrace();
      }
   }

}
