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


package com.execue.sforce.helper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.w3c.dom.Node;

import com.execue.core.common.bean.querygen.QueryColumn;
import com.execue.core.common.type.DataType;
import com.execue.core.common.util.ExecueBeanUtils;
import com.execue.sforce.bean.SObjectTable;
import com.execue.sforce.bean.type.SoapColumnDataType;
import com.execue.sforce.exception.SforceException;
import com.execue.sforce.exception.SforceExceptionCodes;

/**
 * This class contains the utility methods for sforce module
 * 
 * @author Vishay
 * @version 1.0
 * @since 11/08/09
 */

public class SforceUtilityHelper {

   public static String prepareSOAPLoginRequestXML (String userName, String password, String securityToken) {
      StringBuilder queryInfo = new StringBuilder();
      queryInfo
               .append(
                        "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:urn=\"urn:partner.soap.sforce.com\"")
               .append(
                        " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">")
               .append("<soapenv:Header>").append("</soapenv:Header>").append("<soapenv:Body>").append("<urn:login>")
               .append("<urn:username>").append(userName).append("</urn:username>").append("<urn:password>").append(
                        password + securityToken).append("</urn:password>").append("</urn:login>").append(
                        "</soapenv:Body>").append("</soapenv:Envelope>");
      return queryInfo.toString();
   }

   /**
    * This method prepares the SOAP Request XML for query API Call
    * 
    * @param partnerSessionId
    * @param selectQuery
    * @return soap request xml
    */
   public static String prepareSOAPDataRequestXML (String partnerSessionId, String selectQuery) {
      return prepareSOAPDataRequestXML(partnerSessionId, selectQuery, -1);
   }

   /**
    * This method prepares the SOAP Request XML for query API Call
    * 
    * @param partnerSessionId
    * @param selectQuery
    * @return soap request xml
    */
   public static String prepareSOAPDataRequestXML (String partnerSessionId, String selectQuery, int batchSize) {
      StringBuilder queryInfo = new StringBuilder();
      queryInfo
               .append(
                        "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:urn=\"urn:partner.soap.sforce.com\"")
               .append(
                        " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">")
               .append("<soapenv:Header>");
      if (batchSize > 0) {
         queryInfo.append("<urn:QueryOptions>").append("<urn:batchSize>").append(batchSize).append("</urn:batchSize>")
                  .append("</urn:QueryOptions>");
      }
      queryInfo.append("<urn:SessionHeader>").append("<urn:sessionId>").append(partnerSessionId).append(
               "</urn:sessionId>").append("</urn:SessionHeader>").append("</soapenv:Header>").append("<soapenv:Body>")
               .append("<urn:query>").append("<urn:queryString>").append(selectQuery).append("</urn:queryString>")
               .append("</urn:query>").append("</soapenv:Body>").append("</soapenv:Envelope>");
      return queryInfo.toString();
   }

   public static String prepareSOAPMetaRequestXML (String partnerSessionId, SObjectTable sObjectTable) {
      StringBuilder sObjectDescribeQuery = new StringBuilder();
      sObjectDescribeQuery
               .append(
                        "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:urn=\"urn:partner.soap.sforce.com\"")
               .append(
                        " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">")
               .append("<soapenv:Header>").append("<urn:SessionHeader>").append("<urn:sessionId>").append(
                        partnerSessionId).append("</urn:sessionId>").append("</urn:SessionHeader>").append(
                        "</soapenv:Header>").append("<soapenv:Body>").append("<urn:describeSObject>").append(
                        "<urn:sObjectType>").append(sObjectTable.getTableName()).append("</urn:sObjectType>").append(
                        "</urn:describeSObject>").append("</soapenv:Body>").append("</soapenv:Envelope>");
      return sObjectDescribeQuery.toString();
   }

   public static String prepareSOAPQueryLocatorXML (String partnerSessionId, String queryLocator, int batchSize) {
      StringBuilder sObjectDescribeQuery = new StringBuilder();
      sObjectDescribeQuery
               .append(
                        "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:urn=\"urn:partner.soap.sforce.com\"")
               .append(
                        " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">")
               .append("<soapenv:Header>").append("<urn:QueryOptions>").append("<urn:batchSize>").append(batchSize)
               .append("</urn:batchSize>").append("</urn:QueryOptions>").append("<urn:SessionHeader>").append(
                        "<urn:sessionId>").append(partnerSessionId).append("</urn:sessionId>").append(
                        "</urn:SessionHeader>").append("</soapenv:Header>").append("<soapenv:Body>").append(
                        "<urn:queryMore>").append("<urn:queryLocator>").append(queryLocator).append(
                        "</urn:queryLocator>").append("</urn:queryMore>").append("</soapenv:Body>").append(
                        "</soapenv:Envelope>");
      return sObjectDescribeQuery.toString();
   }

   public static String prepareSOAPDescribeTabsRequestXML (String partnerSessionId) {
      StringBuilder describeTabsQuery = new StringBuilder();
      describeTabsQuery
               .append(
                        "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:urn=\"urn:partner.soap.sforce.com\"")
               .append(
                        " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">")
               .append("<soapenv:Header>").append("<urn:SessionHeader>").append("<urn:sessionId>").append(
                        partnerSessionId).append("</urn:sessionId>").append("</urn:SessionHeader>").append(
                        "</soapenv:Header>").append("<soapenv:Body>").append("<urn:describeTabs/>").append(
                        "</soapenv:Body>").append("</soapenv:Envelope>");
      return describeTabsQuery.toString();
   }

   /**
    * This method prepares the SOAP Request XML for getUserInfo API Call
    * 
    * @param partnerSessionId
    * @param selectQuery
    * @return soap request xml
    */
   public static String prepareSOAPUserInfoRequestXML (String partnerSessionId) {
      StringBuilder userInfo = new StringBuilder();
      userInfo
               .append(
                        "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:urn=\"urn:partner.soap.sforce.com\"")
               .append(
                        " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">")
               .append("<soapenv:Header>").append("<urn:SessionHeader>").append("<urn:sessionId>").append(
                        partnerSessionId).append("</urn:sessionId>").append("</urn:SessionHeader>").append(
                        "</soapenv:Header>").append("<soapenv:Body>").append("<urn:getUserInfo/>").append(
                        "</soapenv:Body>").append("</soapenv:Envelope>");

      return userInfo.toString();
   }

   public static String prepareSOAPDeleteRecordsRequestXML (String tableName, String startDate, String endDate,
            String partnerSessionId) {
      StringBuilder deleteRecordsQuery = new StringBuilder();
      deleteRecordsQuery
               .append(
                        "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:urn=\"urn:partner.soap.sforce.com\"")
               .append(
                        " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">")
               .append("<soapenv:Header>").append("<urn:SessionHeader>").append("<urn:sessionId>").append(
                        partnerSessionId).append("</urn:sessionId>").append("</urn:SessionHeader>").append(
                        "</soapenv:Header>").append("<soapenv:Body>").append("<urn:getDeleted>").append(
                        "<urn:sObjectType>").append(tableName).append("</urn:sObjectType>").append(" <urn:startDate>")
               .append(startDate).append("</urn:startDate>").append(" <urn:endDate>").append(endDate).append(
                        "</urn:endDate>").append(" </urn:getDeleted>").append("</soapenv:Body>").append(
                        "</soapenv:Envelope>");

      return deleteRecordsQuery.toString();
   }
   
   public static String prepareSOAPUpdateRecordsRequestXML (String tableName, String startDate, String endDate,
            String partnerSessionId) {
      StringBuilder deleteRecordsQuery = new StringBuilder();
      deleteRecordsQuery
               .append(
                        "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:urn=\"urn:partner.soap.sforce.com\"")
               .append(
                        " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">")
               .append("<soapenv:Header>").append("<urn:SessionHeader>").append("<urn:sessionId>").append(
                        partnerSessionId).append("</urn:sessionId>").append("</urn:SessionHeader>").append(
                        "</soapenv:Header>").append("<soapenv:Body>").append("<urn:getUpdated>").append(
                        "<urn:sObjectType>").append(tableName).append("</urn:sObjectType>").append(" <urn:startDate>")
               .append(startDate).append("</urn:startDate>").append(" <urn:endDate>").append(endDate).append(
                        "</urn:endDate>").append(" </urn:getUpdated>").append("</soapenv:Body>").append(
                        "</soapenv:Envelope>");

      return deleteRecordsQuery.toString();
   }

   /**
    * This method prepares the user profile SOQL query for getting the user profile information
    * 
    * @param userId
    * @return userProfileQuery
    */
   public static String prepareUserProfileQuery (String userId) {
      StringBuilder userProfileQuery = new StringBuilder();
      userProfileQuery
               .append(
                        "select id, name, UserLicenseId, UserType, PermissionsViewAllData, PermissionsManageSelfService, PermissionsApiEnabled from Profile where id='")
               .append(userId).append("'");
      return userProfileQuery.toString();
   }

   /**
    * This method returns the SOAP Message corresponding to the given soapXML
    * 
    * @param soapXML
    * @return soapMessage
    * @throws SforceException
    */
   public static SOAPMessage prepareSOAPMessage (String soapXML) throws SforceException {
      ByteArrayInputStream byteArrayInputStream = null;
      try {
         MimeHeaders mimeheaders = new MimeHeaders();
         byteArrayInputStream = new ByteArrayInputStream(soapXML.getBytes("UTF-8"));
         MessageFactory messageFactory = MessageFactory.newInstance();
         SOAPMessage soapMessage = messageFactory.createMessage(mimeheaders, byteArrayInputStream);
         SOAPBody soapBody = soapMessage.getSOAPBody();
         Node firstChild = soapBody.getFirstChild();
         String soapAction = firstChild.getLocalName();
         soapMessage.getMimeHeaders().addHeader("Content-Type", "text/xml; charset=utf-8");
         soapMessage.getMimeHeaders().addHeader("Accept",
                  "application/soap+xml, application/dime, multipart/related, text/*");
         soapMessage.getMimeHeaders().addHeader("Cache-Control", "no-cache");
         soapMessage.getMimeHeaders().addHeader("Pragma", "no-cache");
         soapMessage.getMimeHeaders().addHeader("SOAPAction", soapAction);
         return soapMessage;
      } catch (SOAPException e) {
         throw new SforceException(SforceExceptionCodes.SFORCE_SOAP_MESSAGE_FAILED_EXCEPTION_CODE, e);
      } catch (UnsupportedEncodingException e) {
         throw new SforceException(SforceExceptionCodes.SFORCE_SOAP_MESSAGE_FAILED_EXCEPTION_CODE, e);
      } catch (IOException e) {
         throw new SforceException(SforceExceptionCodes.SFORCE_SOAP_MESSAGE_FAILED_EXCEPTION_CODE, e);
      } finally {
         if (byteArrayInputStream != null) {
            try {
               byteArrayInputStream.close();
            } catch (IOException e) {
               throw new SforceException(SforceExceptionCodes.SFORCE_SOAP_MESSAGE_FAILED_EXCEPTION_CODE, e);
            }
         }
      }
   }
   
 
   public static List<Integer> getParameterTypes (List<QueryColumn> queryColumns) {
      List<Integer> paramTypes = new ArrayList<Integer>();
      for (QueryColumn queryColumn : queryColumns) {
         paramTypes.add(ExecueBeanUtils.getSQLDataType(queryColumn.getDataType()));
      }
      return paramTypes;
   }

   public static DataType getNormalizedDataType (SoapColumnDataType soapColumnType) {
      DataType dataType = null;
      switch (soapColumnType) {
         case DATE:
            dataType = DataType.DATE;
            break;
         case DATETIME:
            dataType = DataType.DATETIME;
            break;
         case TIME:
            dataType = DataType.TIME;
            break;
         case ID:
            dataType = DataType.STRING;
            break;
         case DOUBLE:
            dataType = DataType.NUMBER;
            break;
         case INT:
            dataType = DataType.INT;
            break;
         case STRING:
            dataType = DataType.STRING;
            break;
         case BOOLEAN:
            dataType = DataType.INT;
            break;
         case BINARY:
            dataType = DataType.STRING;
      }
      return dataType;
   }
}
