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


package com.execue.sforce.replication;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.execue.sforce.SforceBaseTest;
import com.execue.sforce.exception.SforceException;

/**
 * This class represents the test case for sforce replication service
 * 
 * @author Vishay
 * @version 1.0
 * @since 11/08/09
 */
public class SforceReplicationServiceTest extends SforceBaseTest {

   @Before
   public void setUp () {
      baseTestSetup();
   }

   @After
   public void tearDown () {
      baseTestTearDown();
   }

    //@Test
   public void testReplicateSforceData () {
      String partnerSessionId = "00D900000008ioS!ARoAQJDbX.JijnDSAOTxVh_M.4qlW0zdyj1GWTCpk37ToMffi40vbYDuzNZoe8W8EeFF0Z4i3jseNRR.nTyp7fe_DYDfsxog";
      String partnerSessionURL = "https://ap1-api.salesforce.com/services/Soap/u/16.0/00D900000008ioS";
     try {
         getSforceDataReplicationService().startReplication(getLoginContext(partnerSessionId, partnerSessionURL));
      } catch (SforceException e) {
         e.printStackTrace();
      }
   }

   @Test
   public void testSyncRecords () {
      String partnerSessionId = "00D900000008ioS!ARoAQJDbX.JijnDSAOTxVh_M.4qlW0zdyj1GWTCpk37ToMffi40vbYDuzNZoe8W8EeFF0Z4i3jseNRR.nTyp7fe_DYDfsxog";
      String partnerSessionURL = "https://ap1-api.salesforce.com/services/Soap/u/16.0/00D900000008ioS";
      try {
         getSforceDataReplicationService().startDataSyncUpProcess(getLoginContext(partnerSessionId, partnerSessionURL));
      } catch (SforceException e) {
         e.printStackTrace();
      }
   }
}
