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


package com.execue.uss.service.impl;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.execue.uss.exception.USSException;
import com.execue.uss.test.UnstructuredCommonBaseTest;

/**
 * @author Abhijit
 * @since Jul 7, 2009 : 6:03:45 PM
 */
public class UnstructuredSearchTest extends UnstructuredCommonBaseTest {

   private Logger              logger   = Logger.getLogger(UnstructuredSearchTest.class);

   private static final String NEW_LINE = System.getProperty("line.separator");

   @BeforeClass
   public static void setUp () {
      baseSetup();
   }

   @AfterClass
   public static void tearDown () {
      baseTeardown();
   }

   @Test
   public void testSearch () {
      Set<Long> dedIds = new HashSet<Long>();
      try {
         getEntitySearchEngine().search(dedIds);
      } catch (USSException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

}
