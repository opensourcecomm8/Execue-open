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


package com.execue.core.util;

import org.apache.log4j.Logger;

public class ExecueCoreUtilTest {

   private static final Logger log = Logger.getLogger(ExecueCoreUtilTest.class);

   public void testTrimStringToLengthFromRightEnd () {
      String trimmedString = ExecueStringUtil.trimStringToLengthFromEnd("123456789", 5);
      if (log.isDebugEnabled()) {
         log.debug(trimmedString);
      }
      trimmedString = ExecueStringUtil.trimStringToLengthFromEnd("12345", 5);
      if (log.isDebugEnabled()) {
         log.debug(trimmedString);
      }
      trimmedString = ExecueStringUtil.trimStringToLengthFromEnd("1234", 5);
      if (log.isDebugEnabled()) {
         log.debug(trimmedString);
      }
      trimmedString = ExecueStringUtil.trimStringToLengthFromEnd("sl_12345_sa", 5);
      if (log.isDebugEnabled()) {
         log.debug(trimmedString);
      }
      trimmedString = ExecueStringUtil.trimStringToLengthFromEnd("sl_12345_s", 5);
      if (log.isDebugEnabled()) {
         log.debug(trimmedString);
      }
      trimmedString = ExecueStringUtil.trimStringToLengthFromEnd("ssl_12345_sa", 5);
      if (log.isDebugEnabled()) {
         log.debug(trimmedString);
      }
   }

   public static void main (String[] args) {
      ExecueCoreUtilTest utils = new ExecueCoreUtilTest();
      utils.testTrimStringToLengthFromRightEnd();
   }
}
