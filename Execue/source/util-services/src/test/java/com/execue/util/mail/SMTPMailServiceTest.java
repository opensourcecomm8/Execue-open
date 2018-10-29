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


package com.execue.util.mail;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.execue.UtilServicesBaseTest;
import com.execue.util.mail.bean.MailEntity;
import com.execue.util.mail.exception.SMTPMailException;

/**
 * This test class tests smtp mail service
 * 
 * @author Vishay
 * @version 1.0
 * @since 14/08/09
 */
public class SMTPMailServiceTest extends UtilServicesBaseTest {

   @Before
   public void setUp () {
      baseTestSetup();
   }

   @After
   public void tearDown () {
      baseTestTearDown();
   }

   @Test
   public void testPostMail () {
      MailEntity mailEntity = new MailEntity();
      mailEntity.setFrom("vishay@execue.com");
      mailEntity.setHostName("mail.execue.com");
      mailEntity.setMessage("Hello Welcome to mail service of execue");
      mailEntity.setPassword("army12");
      List<String> recipients = new ArrayList<String>();
      recipients.add("shobhits@execue.com");
      mailEntity.setRecipients(recipients);
      mailEntity.setSubject("Execue Online Mail system");
      mailEntity.setUserName("vishay@execue.com");
      try {
         getSMTPMailService().postMail(mailEntity);
      } catch (SMTPMailException e) {
         e.printStackTrace();
      }
   }
}
