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


package com.execue.sforce.parser.impl;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.execue.core.common.bean.querygen.QueryColumn;
import com.execue.sforce.bean.SObjectColumn;
import com.execue.sforce.bean.SObjectTable;
import com.execue.sforce.bean.SforceLoginContext;
import com.execue.sforce.bean.SforceUserInfo;
import com.execue.sforce.bean.SforceUserProfileInfo;
import com.execue.sforce.exception.SforceException;
import com.execue.sforce.exception.SforceExceptionCodes;
import com.execue.sforce.parser.IParseSoapResponseService;

/**
 * This interface contains methods to parse the soap responses
 * 
 * @author Vishay
 * @version 1.0
 * @since 11/08/09
 */
public class ParseSoapResponseServiceImpl implements IParseSoapResponseService {

   public List<List<String>> parseSOAPDataResponseXML (List<QueryColumn> queryColumns, String soapDataResponseXML)
            throws SforceException {
      try {
         DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
         InputSource inputSource = new InputSource();
         inputSource.setCharacterStream(new StringReader(soapDataResponseXML));
         Document document = documentBuilder.parse(inputSource);
         NodeList nodeList = document.getElementsByTagName("records");
         List<List<String>> soapResponseData = new ArrayList<List<String>>();
         for (int recordCount = 0; recordCount < nodeList.getLength(); recordCount++) {
            Element element = (Element) nodeList.item(recordCount);
            List<String> dataRow = new ArrayList<String>();
            for (int columnCount = 0; columnCount < queryColumns.size(); columnCount++) {
               NodeList elementNodeList = element.getElementsByTagName("sf:"
                        + queryColumns.get(columnCount).getColumnName());
               Element columnElement = (Element) elementNodeList.item(0);
               Node columnNode = columnElement.getFirstChild();
               String nodeDataPoint = null;
               if (columnNode != null) {
                  nodeDataPoint = columnNode.getNodeValue();
               }
               dataRow.add(nodeDataPoint);
            }
            soapResponseData.add(dataRow);
         }
         return soapResponseData;
      } catch (ParserConfigurationException e) {
         throw new SforceException(SforceExceptionCodes.SFORCE_PARSE_SOAP_RESPONSE_FAILED_EXCEPTION_CODE, e);
      } catch (SAXException e) {
         throw new SforceException(SforceExceptionCodes.SFORCE_PARSE_SOAP_RESPONSE_FAILED_EXCEPTION_CODE, e);
      } catch (IOException e) {
         throw new SforceException(SforceExceptionCodes.SFORCE_PARSE_SOAP_RESPONSE_FAILED_EXCEPTION_CODE, e);
      }
   }

   public String parseSOAPDataResponseXMLForQueryLocator (String soapDataResponseXML) throws SforceException {
      try {
         String queryLocatorValue = null;
         DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
         InputSource inputSource = new InputSource();
         inputSource.setCharacterStream(new StringReader(soapDataResponseXML));
         Document document = documentBuilder.parse(inputSource);
         NodeList nodeList = document.getElementsByTagName("result");
         Element element = (Element) nodeList.item(0);
         NodeList queryLocator = element.getElementsByTagName("queryLocator");
         Element queryLocatorElement = (Element) queryLocator.item(0);
         Node queryLocatorNode = queryLocatorElement.getFirstChild();
         if (queryLocatorNode != null) {
            queryLocatorValue = queryLocatorNode.getNodeValue();
         }
         return queryLocatorValue;
      } catch (ParserConfigurationException e) {
         throw new SforceException(SforceExceptionCodes.SFORCE_PARSE_SOAP_RESPONSE_FAILED_EXCEPTION_CODE, e);
      } catch (SAXException e) {
         throw new SforceException(SforceExceptionCodes.SFORCE_PARSE_SOAP_RESPONSE_FAILED_EXCEPTION_CODE, e);
      } catch (IOException e) {
         throw new SforceException(SforceExceptionCodes.SFORCE_PARSE_SOAP_RESPONSE_FAILED_EXCEPTION_CODE, e);
      }
   }

   public Set<SObjectTable> parseSOAPDescribeTabsResponseXML (String soapDataResponseXML) throws SforceException {
      try {
         DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
         InputSource inputSource = new InputSource();
         inputSource.setCharacterStream(new StringReader(soapDataResponseXML));
         Document document = documentBuilder.parse(inputSource);
         NodeList nodeList = document.getElementsByTagName("tabs");
         Set<SObjectTable> sObjectTables = new HashSet<SObjectTable>();
         for (int recordCount = 0; recordCount < nodeList.getLength(); recordCount++) {
            Element parentElement = (Element) nodeList.item(recordCount);
            NodeList elementNodeList = parentElement.getElementsByTagName("sobjectName");
            Element element = (Element) elementNodeList.item(0);
            Node node = element.getFirstChild();
            if (node != null) {
               SObjectTable sObjectTable = new SObjectTable();
               sObjectTable.setTableName(node.getNodeValue());
               sObjectTables.add(sObjectTable);
            }
         }
         return sObjectTables;
      } catch (ParserConfigurationException e) {
         throw new SforceException(SforceExceptionCodes.SFORCE_PARSE_SOAP_RESPONSE_FAILED_EXCEPTION_CODE, e);
      } catch (SAXException e) {
         throw new SforceException(SforceExceptionCodes.SFORCE_PARSE_SOAP_RESPONSE_FAILED_EXCEPTION_CODE, e);
      } catch (IOException e) {
         throw new SforceException(SforceExceptionCodes.SFORCE_PARSE_SOAP_RESPONSE_FAILED_EXCEPTION_CODE, e);
      }
   }

   public List<SObjectColumn> parseSOAPMetaResponseXML (String soapDataResponseXML) throws SforceException {
      try {
         DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
         InputSource inputSource = new InputSource();
         inputSource.setCharacterStream(new StringReader(soapDataResponseXML));
         Document document = documentBuilder.parse(inputSource);
         NodeList nodeList = document.getElementsByTagName("fields");
         List<SObjectColumn> sObjectColumns = new ArrayList<SObjectColumn>();
         for (int recordCount = 0; recordCount < nodeList.getLength(); recordCount++) {
            SObjectColumn sObjectColumn = new SObjectColumn();
            Element element = (Element) nodeList.item(recordCount);
            sObjectColumn.setColumnName(readTagValue(element, "name"));
            sObjectColumn.setSoapType(readTagValue(element, "soapType"));
            sObjectColumn.setDigits(readTagValue(element, "digits"));
            sObjectColumn.setLength(readTagValue(element, "length"));
            sObjectColumn.setPrecision(readTagValue(element, "precision"));
            sObjectColumn.setScale(readTagValue(element, "scale"));
            sObjectColumn.setNullable(readTagValue(element, "nillable"));
            sObjectColumn.setUnique(readTagValue(element, "unique"));
            sObjectColumns.add(sObjectColumn);
         }
         return sObjectColumns;
      } catch (ParserConfigurationException e) {
         throw new SforceException(SforceExceptionCodes.SFORCE_PARSE_SOAP_RESPONSE_FAILED_EXCEPTION_CODE, e);
      } catch (SAXException e) {
         throw new SforceException(SforceExceptionCodes.SFORCE_PARSE_SOAP_RESPONSE_FAILED_EXCEPTION_CODE, e);
      } catch (IOException e) {
         throw new SforceException(SforceExceptionCodes.SFORCE_PARSE_SOAP_RESPONSE_FAILED_EXCEPTION_CODE, e);
      }
   }

   @SuppressWarnings ("unchecked")
   public List<String> parseSOAPDeleteResponseXML (String soapDataResponseXML) throws SforceException {
      try {
         DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
         InputSource inputSource = new InputSource();
         inputSource.setCharacterStream(new StringReader(soapDataResponseXML));
         Document document = documentBuilder.parse(inputSource);
         List<String> ids = new ArrayList<String>();
         NodeList nodeList = document.getElementsByTagName("deletedRecords");
         if (nodeList.getLength() != 0) {
            for (int recordCount = 0; recordCount < nodeList.getLength(); recordCount++) {
               Element element = (Element) nodeList.item(recordCount);
               String value = readTagValue(element, "id");
               ids.add(value);
            }
         }
         return ids;
      } catch (ParserConfigurationException e) {
         throw new SforceException(SforceExceptionCodes.SFORCE_PARSE_SOAP_RESPONSE_FAILED_EXCEPTION_CODE, e);
      } catch (SAXException e) {
         throw new SforceException(SforceExceptionCodes.SFORCE_PARSE_SOAP_RESPONSE_FAILED_EXCEPTION_CODE, e);
      } catch (IOException e) {
         throw new SforceException(SforceExceptionCodes.SFORCE_PARSE_SOAP_RESPONSE_FAILED_EXCEPTION_CODE, e);
      }
   }

   private String readTagValue (Element parentElement, String tagName) {
      String tagValue = null;
      NodeList elementNodeList = parentElement.getElementsByTagName(tagName);
      Element element = (Element) elementNodeList.item(0);
      Node node = element.getFirstChild();
      if (node != null) {
         tagValue = node.getNodeValue();
      }
      return tagValue;
   }

   public SforceLoginContext populateSforceLoginContext (String soapLoginResponseXML) throws SforceException {
      SforceLoginContext sforceLoginContext = new SforceLoginContext();
      try {
         DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
         InputSource inputSource = new InputSource();
         inputSource.setCharacterStream(new StringReader(soapLoginResponseXML));
         Document document = documentBuilder.parse(inputSource);
         NodeList nodeList = document.getElementsByTagName("result");
         Element element = (Element) nodeList.item(0);
         NodeList serverURLNodeList = element.getElementsByTagName("serverUrl");
         NodeList sessionIdNodeList = element.getElementsByTagName("sessionId");
         Element serverURLElement = (Element) serverURLNodeList.item(0);
         Element sessionIdElement = (Element) sessionIdNodeList.item(0);
         sforceLoginContext.setPartnerSessionURL(serverURLElement.getFirstChild().getNodeValue());
         sforceLoginContext.setPartnerSessionId(sessionIdElement.getFirstChild().getNodeValue());
         return sforceLoginContext;
      } catch (ParserConfigurationException e) {
         throw new SforceException(SforceExceptionCodes.SFORCE_PARSE_SOAP_RESPONSE_FAILED_EXCEPTION_CODE, e);
      } catch (SAXException e) {
         throw new SforceException(SforceExceptionCodes.SFORCE_PARSE_SOAP_RESPONSE_FAILED_EXCEPTION_CODE, e);
      } catch (IOException e) {
         throw new SforceException(SforceExceptionCodes.SFORCE_PARSE_SOAP_RESPONSE_FAILED_EXCEPTION_CODE, e);
      }
   }

   /**
    * This method populates the user information present in the soapUserInfoResponseXML
    * 
    * @param soapUserInfoResponseXML
    * @return SforceUserInfo object
    * @throws SforceException
    */
   public SforceUserInfo populateSforceUserInfo (String soapUserInfoResponseXML) throws SforceException {
      SforceUserInfo sforceUserInfo = new SforceUserInfo();
      try {

         // Parse the user info response xml
         DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
         InputSource inputSource = new InputSource();
         inputSource.setCharacterStream(new StringReader(soapUserInfoResponseXML));
         Document document = documentBuilder.parse(inputSource);

         // Get the result node list from the response dom
         NodeList nodeList = document.getElementsByTagName("result");
         Element element = (Element) nodeList.item(0);

         // Get the user info node list form the result element
         NodeList orgIdNodeList = element.getElementsByTagName("organizationId");
         NodeList orgNameNodeList = element.getElementsByTagName("organizationName");
         NodeList profileIdNodeList = element.getElementsByTagName("profileId");
         NodeList userEmailNodeList = element.getElementsByTagName("userEmail");
         NodeList userNameNodeList = element.getElementsByTagName("userName");
         NodeList userIdNodeList = element.getElementsByTagName("userId");

         // Get the user info element from each node list
         Element orgIdElement = (Element) orgIdNodeList.item(0);
         Element orgNameElement = (Element) orgNameNodeList.item(0);
         Element profileIdElement = (Element) profileIdNodeList.item(0);
         Element userEmailElement = (Element) userEmailNodeList.item(0);
         Element userNameElement = (Element) userNameNodeList.item(0);
         Element userIdElement = (Element) userIdNodeList.item(0);

         // Set the user information
         sforceUserInfo.setOrganizationId(orgIdElement.getFirstChild().getNodeValue());
         sforceUserInfo.setOrganizationName(orgNameElement.getFirstChild().getNodeValue());
         sforceUserInfo.setProfileId(profileIdElement.getFirstChild().getNodeValue());
         sforceUserInfo.setUserEmail(userEmailElement.getFirstChild().getNodeValue());
         sforceUserInfo.setUserName(userNameElement.getFirstChild().getNodeValue());
         sforceUserInfo.setUserId(userIdElement.getFirstChild().getNodeValue());

         return sforceUserInfo;
      } catch (ParserConfigurationException e) {
         throw new SforceException(SforceExceptionCodes.SFORCE_PARSE_SOAP_RESPONSE_FAILED_EXCEPTION_CODE, e);
      } catch (SAXException e) {
         throw new SforceException(SforceExceptionCodes.SFORCE_PARSE_SOAP_RESPONSE_FAILED_EXCEPTION_CODE, e);
      } catch (IOException e) {
         throw new SforceException(SforceExceptionCodes.SFORCE_PARSE_SOAP_RESPONSE_FAILED_EXCEPTION_CODE, e);
      }
   }

   public String checkSOAPResponseForValidity (String soapResponseXML) throws SforceException {
      String faultCode = null;
      try {
         DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
         InputSource inputSource = new InputSource();
         inputSource.setCharacterStream(new StringReader(soapResponseXML));
         Document document = documentBuilder.parse(inputSource);
         NodeList nodeList = document.getElementsByTagName("faultcode");
         Node node = nodeList.item(0);
         if (node != null) {
            faultCode = node.getFirstChild().getNodeValue();
         }
         return faultCode;
      } catch (ParserConfigurationException e) {
         throw new SforceException(SforceExceptionCodes.SFORCE_PARSE_SOAP_RESPONSE_FAILED_EXCEPTION_CODE, e);
      } catch (SAXException e) {
         throw new SforceException(SforceExceptionCodes.SFORCE_PARSE_SOAP_RESPONSE_FAILED_EXCEPTION_CODE, e);
      } catch (IOException e) {
         throw new SforceException(SforceExceptionCodes.SFORCE_PARSE_SOAP_RESPONSE_FAILED_EXCEPTION_CODE, e);
      }

   }

   public SforceUserProfileInfo populateSforceUserProfileInfo (String soapUserProfileInfoResponseXML)
            throws SforceException {
      SforceUserProfileInfo sforceUserProfileInfo = new SforceUserProfileInfo();
      try {

         // Parse the user profile info response xml
         DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
         InputSource inputSource = new InputSource();
         inputSource.setCharacterStream(new StringReader(soapUserProfileInfoResponseXML));
         Document document = documentBuilder.parse(inputSource);

         // Get the result node list from the response dom
         NodeList recordsNodeList = document.getElementsByTagName("records");
         Element element = (Element) recordsNodeList.item(0);

         NodeList profileIdNodeList = element.getElementsByTagName("sf:Id");
         NodeList profileNameNodeList = element.getElementsByTagName("sf:Name");
         NodeList userLicenseIdNodeList = element.getElementsByTagName("sf:UserLicenseId");
         NodeList userTypeNodeList = element.getElementsByTagName("sf:UserType");
         NodeList permissionsViewAllDataNodeList = element.getElementsByTagName("sf:PermissionsViewAllData");
         NodeList permissionsManageSelfServiceNodeList = element
                  .getElementsByTagName("sf:PermissionsManageSelfService");
         NodeList permissionsApiEnabledNodeList = element.getElementsByTagName("sf:PermissionsApiEnabled");

         Element idElement = (Element) profileIdNodeList.item(0);
         Element nameElement = (Element) profileNameNodeList.item(0);
         Element userLicenseIdElement = (Element) userLicenseIdNodeList.item(0);
         Element userTypeElement = (Element) userTypeNodeList.item(0);
         Element permissionsViewAllDataElement = (Element) permissionsViewAllDataNodeList.item(0);
         Element permissionsManageSelfServiceElement = (Element) permissionsManageSelfServiceNodeList.item(0);
         Element permissionsApiEnabledElement = (Element) permissionsApiEnabledNodeList.item(0);

         // Set the user profile information
         sforceUserProfileInfo.setId(idElement.getFirstChild().getNodeValue());
         sforceUserProfileInfo.setName(nameElement.getFirstChild().getNodeValue());
         sforceUserProfileInfo.setUserLicenseId(userLicenseIdElement.getFirstChild().getNodeValue());
         sforceUserProfileInfo.setUserType(userTypeElement.getFirstChild().getNodeValue());
         sforceUserProfileInfo.setPermissionsViewAllData(permissionsViewAllDataElement.getFirstChild().getNodeValue());
         sforceUserProfileInfo.setPermissionsManageSelfService(permissionsManageSelfServiceElement.getFirstChild()
                  .getNodeValue());
         sforceUserProfileInfo.setPermissionsApiEnabled(permissionsApiEnabledElement.getFirstChild().getNodeValue());

         return sforceUserProfileInfo;
      } catch (ParserConfigurationException e) {
         throw new SforceException(SforceExceptionCodes.SFORCE_PARSE_SOAP_RESPONSE_FAILED_EXCEPTION_CODE, e);
      } catch (SAXException e) {
         throw new SforceException(SforceExceptionCodes.SFORCE_PARSE_SOAP_RESPONSE_FAILED_EXCEPTION_CODE, e);
      } catch (IOException e) {
         throw new SforceException(SforceExceptionCodes.SFORCE_PARSE_SOAP_RESPONSE_FAILED_EXCEPTION_CODE, e);
      }
   }

   public List<String> parseSOAPUpdateResponseXML (String soapDataResponseXML) throws SforceException {
      try {
         DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
         InputSource inputSource = new InputSource();
         inputSource.setCharacterStream(new StringReader(soapDataResponseXML));
         Document document = documentBuilder.parse(inputSource);
         List<String> ids = new ArrayList<String>();
         NodeList nodeList = document.getElementsByTagName("result");
         if (nodeList.getLength() != 0) {
            for (int recordCount = 0; recordCount < nodeList.getLength(); recordCount++) {
               Element element = (Element) nodeList.item(recordCount);
               NodeList elementNodeList = element.getElementsByTagName("ids");
               System.out.println(elementNodeList.getLength());
               for (int count = 0; count < elementNodeList.getLength(); count++) {
                  Element idElement = (Element) elementNodeList.item(count);
                  Node node = idElement.getFirstChild();
                  ids.add(node.getNodeValue());
               }
            }
         }
         return ids;
      } catch (ParserConfigurationException e) {
         throw new SforceException(SforceExceptionCodes.SFORCE_PARSE_SOAP_RESPONSE_FAILED_EXCEPTION_CODE, e);
      } catch (SAXException e) {
         throw new SforceException(SforceExceptionCodes.SFORCE_PARSE_SOAP_RESPONSE_FAILED_EXCEPTION_CODE, e);
      } catch (IOException e) {
         throw new SforceException(SforceExceptionCodes.SFORCE_PARSE_SOAP_RESPONSE_FAILED_EXCEPTION_CODE, e);
      }
   }

   public String parseSOAPUpdateResponseXMLForModificatonDate (String soapDataResponseXML) throws SforceException {
      String lastModifiedDate = new String();
      try {
         DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
         InputSource inputSource = new InputSource();
         inputSource.setCharacterStream(new StringReader(soapDataResponseXML));
         Document document = documentBuilder.parse(inputSource);
         List<String> ids = new ArrayList<String>();
         NodeList nodeList = document.getElementsByTagName("result");
         if (nodeList.getLength() != 0) {
            for (int recordCount = 0; recordCount < nodeList.getLength(); recordCount++) {
               Element element = (Element) nodeList.item(recordCount);
               NodeList elementNodeList = element.getElementsByTagName("latestDateCovered");
               System.out.println(elementNodeList.getLength());
               Element idElement = (Element) elementNodeList.item(0);
               Node node = idElement.getFirstChild();
               lastModifiedDate = node.getNodeValue();
            }
         }
         return lastModifiedDate;
      } catch (ParserConfigurationException e) {
         throw new SforceException(SforceExceptionCodes.SFORCE_PARSE_SOAP_RESPONSE_FAILED_EXCEPTION_CODE, e);
      } catch (SAXException e) {
         throw new SforceException(SforceExceptionCodes.SFORCE_PARSE_SOAP_RESPONSE_FAILED_EXCEPTION_CODE, e);
      } catch (IOException e) {
         throw new SforceException(SforceExceptionCodes.SFORCE_PARSE_SOAP_RESPONSE_FAILED_EXCEPTION_CODE, e);
      }
   }

}
