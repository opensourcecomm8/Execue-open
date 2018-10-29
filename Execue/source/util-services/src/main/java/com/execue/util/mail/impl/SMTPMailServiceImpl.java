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


package com.execue.util.mail.impl;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.util.mail.ISMTPMailService;
import com.execue.util.mail.bean.MailEntity;
import com.execue.util.mail.exception.SMTPMailException;

/**
 * This class uses SMTP to send mails
 * 
 * @author Vishay
 * @version 1.0
 * @since 14/08/09
 */
public class SMTPMailServiceImpl implements ISMTPMailService {

   public void postMail (MailEntity mailEntity) throws SMTPMailException {
      try {
         SimpleEmail email = new SimpleEmail();
         email.setAuthentication(mailEntity.getUserName(), mailEntity.getPassword());
         email.setHostName(mailEntity.getHostName());
         for (String recipient : mailEntity.getRecipients()) {
            email.addTo(recipient);
         }
         email.setFrom(mailEntity.getFrom());
         email.setSubject(mailEntity.getSubject());
         email.setContent(mailEntity.getMessage(), "text/html");
         email.send();
      } catch (EmailException e) {
         throw new SMTPMailException(ExeCueExceptionCodes.MAIL_SENDING_FAILED, e);
      }
   }

}
