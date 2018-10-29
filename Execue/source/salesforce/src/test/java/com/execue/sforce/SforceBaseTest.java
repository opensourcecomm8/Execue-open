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


package com.execue.sforce;

import static junit.framework.Assert.fail;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.execue.core.exception.ConfigurationException;
import com.execue.dataaccess.configuration.DataAccessConfigurationService;
import com.execue.sforce.absorption.ISforceAbsorptionService;
import com.execue.sforce.access.ISforceAccessService;
import com.execue.sforce.bean.SforceLoginContext;
import com.execue.sforce.dataaccess.ISforceDAO;
import com.execue.sforce.login.ISforceLoginService;
import com.execue.sforce.parser.IParseSoapResponseService;
import com.execue.sforce.replication.ISforceReplicationService;
import com.execue.sforce.soap.ISOAPRequestResponseService;

/**
 * This class represents the base test for sales force module
 * 
 * @author Vishay
 * @version 1.0
 * @since 11/08/09
 */
public class SforceBaseTest {

   static ApplicationContext context;

   public static ApplicationContext getContext () {
      return context;
   }

   public static void setContext (ApplicationContext context) {
      SforceBaseTest.context = context;
   }

   public static void baseTestSetup () {
      context = new ClassPathXmlApplicationContext(new String[] { "/bean-config/spring-hibernate.xml",
               "/bean-config/spring-hibernate-qdata.xml", "/bean-config/execue-qdata-dataaccess.xml",
               "/bean-config/execue-qdata.xml", "/bean-config/execue-configuration.xml",
               "/bean-config/execue-dataaccess.xml", "/bean-config/execue-util.xml",
               "/bean-config/execue-query-generation.xml", "/bean-config/execue-swi.xml",
               "/bean-config/execue-job-scheduler.xml", "/bean-config/execue-logging-configuration.xml",
               "/bean-config/execue-sforce.xml", "/bean-config/spring-hibernate-sforce.xml" });
      // context = new ClassPathXmlApplicationContext(new String[] { "/bean-config/execue-applicaton.xml",
      // "/bean-config/execue-sforce.xml" });
      DataAccessConfigurationService dataAccessConfigurationService = (DataAccessConfigurationService) context
               .getBean("dataAccessConfigurationService");
      try {
         dataAccessConfigurationService.doConfigure();
      } catch (ConfigurationException e) {
         fail("Configuration Failed : " + e.getMessage());
      }
   }

   public static void baseTestTearDown () {

   }

   public SforceLoginContext getLoginContext (String partnerSessionId, String partnerSessionURL) {
      SforceLoginContext sforceLoginContext = new SforceLoginContext();
      sforceLoginContext.setPartnerSessionId(partnerSessionId);
      sforceLoginContext.setPartnerSessionURL(partnerSessionURL);
      return sforceLoginContext;
   }

   /**
    * This method prepares the SOAP Request XML for query API Call
    * 
    * @param partnerSessionId
    * @param selectQuery
    * @return soap request xml
    */
   public static String getInsertQuerySOAPRequestXML (String partnerSessionId, String sobjectName) {
      StringBuilder queryInfo = new StringBuilder();
      queryInfo
               .append(
                        "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:urn=\"urn:partner.soap.sforce.com\" xmlns:urn1=\"urn:sobject.partner.soap.sforce.com\"")
               .append(
                        " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">")
               .append("<soapenv:Header>").append("<urn:SessionHeader>").append("<urn:sessionId>").append(
                        partnerSessionId).append("</urn:sessionId>").append("</urn:SessionHeader>").append(
                        "</soapenv:Header>").append("<soapenv:Body>").append("<urn:create>").append("<urn:sObjects>")
               .append("<urn1:type>").append(sobjectName).append("</urn1:type>").append(
                        "<urn1:Id></urn1:Id><urn1:Name>vishay_murthy</urn1:Name>").append("</urn:sObjects>").append(
                        "</urn:create>").append("</soapenv:Body>").append("</soapenv:Envelope>");
      return queryInfo.toString();
   }

   public IParseSoapResponseService getParseSoapResponseService () {
      return (IParseSoapResponseService) context.getBean("parseSOAPResponseService");
   }

   public ISforceLoginService getSforceLoginService () {
      return (ISforceLoginService) context.getBean("sforceLoginService");
   }

   public ISforceAbsorptionService getSObjectAbsorptionService () {
      return (ISforceAbsorptionService) context.getBean("sobjectAbsorptionService");
   }

   public ISOAPRequestResponseService getSOAPRequestResponseService () {
      return (ISOAPRequestResponseService) context.getBean("soapRequestResponseService");
   }

   public ISforceReplicationService getSforceDataReplicationService () {
      return (ISforceReplicationService) context.getBean("sforceReplicationService");
   }

   public ISforceAccessService getSforceAccessService () {
      return (ISforceAccessService) context.getBean("sforceAccessService");
   }

   public ISforceDAO getSforceDAO () {
      return (ISforceDAO) context.getBean("sforceDAO");
   }

}
