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


package com.execue.platform.test.unstructured.content.transporter;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.execue.core.exception.ConfigurationException;
import com.execue.platform.exception.UnstructuredContentTransporterException;
import com.execue.platform.test.PlatformCommonBaseTest;

public class UnstructuredTargetBasedContentTransporterTest extends PlatformCommonBaseTest {

   private static final Logger log = Logger.getLogger(UnstructuredTargetBasedContentTransporterTest.class);

   @BeforeClass
   public static void setup () throws ConfigurationException {
      baseSetup();
   }

   @AfterClass
   public static void tearDown () {
      baseTeardown();
   }

   @Test
   public void testTransportContent () {
      try {
         List<Long> targetWareHouseDataSourceIds = new ArrayList<Long>();
         targetWareHouseDataSourceIds.add(1001L);
         getUnstructuredTargetBasedContentTransporter().transportContent(targetWareHouseDataSourceIds);
      } catch (UnstructuredContentTransporterException usctException) {
         log.error(usctException.getMessage(), usctException);
         fail(usctException.getMessage());
      }
   }
}
