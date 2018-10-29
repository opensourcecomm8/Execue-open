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


package com.execue.web.core.action.qi;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestQueryIntefaceSearchAction extends QIActionBaseTest {

   @Test
   public void testMetrics () {
      String retString = null;
      try {
         String data = getResourceData("/qi/metrics_query.xml");
         QueryInterfaceSearchAction action = getSearchAction();
         action.setRequest(data);
         retString = action.search();
      } catch (Exception e) {
         e.printStackTrace();
      }
      assertEquals("SUCCESS", retString);
   }

}
