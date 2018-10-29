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
package com.execue.sforce.access.impl;

import com.execue.sforce.access.ISforceAccessService;
import com.execue.sforce.bean.SforceUserInfo;
import com.execue.sforce.bean.SforceUserProfileInfo;
import com.execue.sforce.exception.SforceException;
import com.execue.sforce.helper.SforceUtilityHelper;
import com.execue.sforce.parser.IParseSoapResponseService;
import com.execue.sforce.soap.ISOAPRequestResponseService;


/**
 * This class is for BiFactor App Access from salesforce 
 * 
 * @author Nitesh
 *
 */
public class SforceAccessServiceImpl implements ISforceAccessService {

   private ISOAPRequestResponseService soapRequestResponseService;
   private IParseSoapResponseService   parseSOAPResponseService;
   
   /**
    * This method returns the salesforce user information 
    * 
    * @param partnerSessionId
    * @param partnerSessionUrl
    * @return SforceUserInfo object
    * @throws SforceException
    */
   public SforceUserInfo getSforceUserInfo(String partnerSessionId, String partnerSessionURL) throws SforceException {
      String userInfo = SforceUtilityHelper.prepareSOAPUserInfoRequestXML(partnerSessionId);
      String soapResponse = soapRequestResponseService.executeSOAPRequest(partnerSessionURL, userInfo);
      SforceUserInfo sforceUserInfo = parseSOAPResponseService.populateSforceUserInfo(soapResponse);
      return sforceUserInfo;
   }
   
   public boolean isValidSession(SforceUserInfo sforceUserInfo) throws SforceException {
      return (sforceUserInfo != null); 
   }   
   
   
   public void setSoapRequestResponseService (ISOAPRequestResponseService soapRequestResponseService) {
      this.soapRequestResponseService = soapRequestResponseService;
   }

   
   public void setParseSOAPResponseService (IParseSoapResponseService parseSOAPResponseService) {
      this.parseSOAPResponseService = parseSOAPResponseService;
   }

   public SforceUserProfileInfo getSforceUserProfileInfo (String partnerSessionId, String partnerSessionURL, String profileId) throws SforceException {
      String userProfileInfoQuery = SforceUtilityHelper.prepareUserProfileQuery(profileId);
      String userProfileSOAPXML = SforceUtilityHelper.prepareSOAPDataRequestXML(partnerSessionId, userProfileInfoQuery);
      String soapResponse = soapRequestResponseService.executeSOAPRequest(partnerSessionURL, userProfileSOAPXML);
      SforceUserProfileInfo userProfileInfo = parseSOAPResponseService.populateSforceUserProfileInfo(soapResponse);
      return userProfileInfo;
   }
}
