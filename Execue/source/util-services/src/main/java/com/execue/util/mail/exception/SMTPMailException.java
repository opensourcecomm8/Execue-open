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


package com.execue.util.mail.exception;

import com.execue.core.exception.ExeCueException;

/**
 * This exception class defines the exception for mail service
 * 
 * @author Vishay
 * @version 1.0
 * @since 14/08/09
 */
public class SMTPMailException extends ExeCueException {

   public SMTPMailException (int exceptionCode, String message) {
      super(exceptionCode, message);
   }

   public SMTPMailException (int exceptionCode, String message, Throwable cause) {
      super(exceptionCode, message, cause);
   }

   public SMTPMailException (int exceptionCode, Throwable cause) {
      super(exceptionCode, cause);
   }

}
