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


package com.execue.swi.service;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.execue.core.common.bean.entity.ReportGroup;
import com.execue.swi.SWIBaseTest;


public class LookupServiceTest extends SWIBaseTest {

   @BeforeClass
   public static void setUp () {
      baseSetup();
   }
   
   @Test
   public void testLooupService () {
      testReportGroup();
   }
   
   public void testReportGroup () {
      
      List<ReportGroup> rList = getLookupService().getReportGroups();
      assertNotNull(rList);
      assertTrue("Report Group list is > 0", rList.size()> 0);
      assertTrue("Report Group type list is > 0", rList.get(0).getReportTypes().size()> 0);
   }
}
