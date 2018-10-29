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


package com.execue.sforce.parser;

import java.util.List;
import java.util.Set;

import com.execue.core.common.bean.querygen.QueryColumn;
import com.execue.sforce.bean.SObjectColumn;
import com.execue.sforce.bean.SObjectTable;
import com.execue.sforce.bean.SforceLoginContext;
import com.execue.sforce.bean.SforceUserInfo;
import com.execue.sforce.bean.SforceUserProfileInfo;
import com.execue.sforce.exception.SforceException;

/**
 * This interface contains methods to parse the soap responses
 * 
 * @author Vishay
 * @version 1.0
 * @since 11/08/09
 */
public interface IParseSoapResponseService {

   public List<List<String>> parseSOAPDataResponseXML (List<QueryColumn> queryColumns, String soapDataResponseXML)
            throws SforceException;

   public List<SObjectColumn> parseSOAPMetaResponseXML (String soapDataResponseXML) throws SforceException;

   public Set<SObjectTable> parseSOAPDescribeTabsResponseXML (String soapDataResponseXML) throws SforceException;

   public SforceLoginContext populateSforceLoginContext (String soapLoginResponseXML) throws SforceException;

   public SforceUserInfo populateSforceUserInfo (String soapUserInfoResponseXML) throws SforceException;

   public SforceUserProfileInfo populateSforceUserProfileInfo (String soapUserProfileInfoResponseXML)
            throws SforceException;

   public String checkSOAPResponseForValidity (String soapResponseXML) throws SforceException;

   public String parseSOAPDataResponseXMLForQueryLocator (String soapDataResponseXML) throws SforceException;

   public List<String> parseSOAPDeleteResponseXML (String soapDataResponseXML) throws SforceException;

   public List<String> parseSOAPUpdateResponseXML (String soapDataResponseXML) throws SforceException;

   public String parseSOAPUpdateResponseXMLForModificatonDate (String soapDataResponseXML) throws SforceException;
}
