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


package com.execue;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.execue.util.cryptography.ICryptographyService;
import com.execue.util.mail.ISMTPMailService;

/**
 * This class represents the base test for utility services module
 * 
 * @author Vishay
 * @version 1.0
 * @since 14/08/09
 */
public class UtilServicesBaseTest {

   static ApplicationContext context;

   public static void baseTearDown () {

   }

   public static void baseSetup () {
   }

   public static ApplicationContext getContext () {
      return context;
   }

   public static void setContext (ApplicationContext context) {
      UtilServicesBaseTest.context = context;
   }

   public static void baseTestSetup () {
      context = new ClassPathXmlApplicationContext(new String[] { "/platform/bean-config/execue-util-services.xml" });
   }

   public static void baseTestTearDown () {

   }

   public ICryptographyService getCryptograhyService () {
      return (ICryptographyService) context.getBean("cryptographyService");
   }

   public ISMTPMailService getSMTPMailService () {
      return (ISMTPMailService) context.getBean("smtpMailService");
   }

}
