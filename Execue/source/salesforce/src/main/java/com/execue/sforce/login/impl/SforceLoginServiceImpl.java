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


package com.execue.sforce.login.impl;

import com.execue.sforce.bean.SforceLoginContext;
import com.execue.sforce.exception.SforceException;
import com.execue.sforce.helper.SforceUtilityHelper;
import com.execue.sforce.login.ISforceLoginService;
import com.execue.sforce.parser.IParseSoapResponseService;
import com.execue.sforce.soap.ISOAPRequestResponseService;

/**
 * This class is for login service to sforce
 * 
 * @author Vishay
 * @version 1.0
 * @since 18/08/09
 */
public class SforceLoginServiceImpl implements ISforceLoginService {

   private ISOAPRequestResponseService soapRequestResponseService;
   private IParseSoapResponseService   parseSOAPResponseService;

   public SforceLoginContext loginToSforce () throws SforceException {
      SforceLoginContext sforceLoginContext = null;
      // TODO : -VG- these credentials will come from db context
      String userName = "vishay123@execue.com";
      String password = "execue123";
      String securityToken = "NI2Vi5pG9Td3dfYGQKraaSIF";
      String sforceLoginURL = "https://www.salesforce.com/services/Soap/u/16.0";
      String loginSOAPRequestXML = SforceUtilityHelper.prepareSOAPLoginRequestXML(userName, password, securityToken);
      String soapLoginResponseXML = getSoapRequestResponseService().executeSOAPRequest(sforceLoginURL,
               loginSOAPRequestXML);
      String validityStatus = getParseSOAPResponseService().checkSOAPResponseForValidity(soapLoginResponseXML);
      if (validityStatus == null) {
         sforceLoginContext = getParseSOAPResponseService().populateSforceLoginContext(soapLoginResponseXML);
      } else {
         // log the message
      }
      return sforceLoginContext;
   }

   public ISOAPRequestResponseService getSoapRequestResponseService () {
      return soapRequestResponseService;
   }

   public void setSoapRequestResponseService (ISOAPRequestResponseService soapRequestResponseService) {
      this.soapRequestResponseService = soapRequestResponseService;
   }

   public IParseSoapResponseService getParseSOAPResponseService () {
      return parseSOAPResponseService;
   }

   public void setParseSOAPResponseService (IParseSoapResponseService parseSOAPResponseService) {
      this.parseSOAPResponseService = parseSOAPResponseService;
   }

}
