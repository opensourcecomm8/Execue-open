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
package com.execue.sforce.access;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.execue.sforce.SforceBaseTest;
import com.execue.sforce.bean.SforceUserInfo;
import com.execue.sforce.exception.SforceException;


/**
 * @author Nitesh
 *
 */
public class SforceAccessTest extends SforceBaseTest{

   //provide the valid session id and session url
   private String partnerSessionId = "00D900000000XhF!ARkAQJ3Q_N6YYeCwN8tlGOz1hu_XD5v9iBstkRXytFrpkAKh61sWUm94K1E3tWctGVcTxEDG5B5c89Zeqt8WuJDusAbz3.Dv";
   private String partnerSessionURL = "https://ap1-api.salesforce.com/services/Soap/u/16.0/00D900000000XhF";
   
   /**
    * @throws java.lang.Exception
    */
   @Before
   public void setUp () throws Exception {
      baseTestSetup();
   }

   /**
    * @throws java.lang.Exception
    */
   @After
   public void tearDown () throws Exception {
      baseTestTearDown();
   }

   @Test
   public void testGetSforceUserInfo () {
      try {
         getSforceAccessService().getSforceUserInfo(partnerSessionId, partnerSessionURL);
      } catch (SforceException e) {
         e.printStackTrace();
      }
   }

   @Test
   public void testGetSforceUserProfileInfo () {
      try {
         SforceUserInfo userInfo = getSforceAccessService().getSforceUserInfo(partnerSessionId, partnerSessionURL);
         getSforceAccessService().getSforceUserProfileInfo(partnerSessionId, partnerSessionURL, userInfo.getProfileId());
      } catch (SforceException e) {
         e.printStackTrace();
      }
   }
   
}
