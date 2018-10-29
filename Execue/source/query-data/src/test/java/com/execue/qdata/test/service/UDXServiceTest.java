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


package com.execue.qdata.test.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.execue.core.common.bean.uss.UniversalUnstructuredSearchResult;
import com.execue.core.common.bean.uss.UnstructuredSearchResultItem;
import com.execue.qdata.exception.UDXException;
import com.execue.qdata.test.QueryDataServiceBaseTest;

public class UDXServiceTest extends QueryDataServiceBaseTest {

   private static final Logger log = Logger.getLogger(UDXServiceTest.class);

   @BeforeClass
   public static void setUp () {
      baseSetup();
   }

   @AfterClass
   public static void tearDown () {
      baseTeardown();
   }

   /*
    * @Test public void testGetRFIdsByDEDId () { List<Long> rfxIds = null; Long dedId = 3129L; try { rfxIds =
    * getUDXService().getRFIdsByDEDId(dedId); if (CollectionUtils.isEmpty(rfxIds)) { fail("RFX IDs not found for ded id : " +
    * dedId); } if (log.isDebugEnabled()) { log.debug("RFX IDs for ded id [" + dedId+"] is "+rfxIds); } } catch
    * (SWIException swiException) { log.error(swiException, swiException); } } @Test public void testGetUDXIdsByRFId () {
    * List<Long> udxIds = null; Long rfId = 1L; try { udxIds = getUDXService().getUDXIdsByRFId(rfId); if
    * (CollectionUtils.isEmpty(udxIds)) { fail("UDX IDs not found for RF id : " + rfId); } if (log.isDebugEnabled()) {
    * log.debug("UDX IDs for RF id [" + rfId+"] is "+udxIds); } } catch (SWIException swiException) {
    * log.error(swiException, swiException); } }
    */
   @Test
   public void testGetRIUnstructuredIndexByUserQueryId () {
      try {
         UniversalUnstructuredSearchResult ussHolder = getUDXService().getRIUnstructuredResultsByUserQuery(4L, 1, 10);
         List<UnstructuredSearchResultItem> riUdxbeans = ussHolder.getUnstructuredSearchResultItem();
         int count = ussHolder.getTotalCount();
         System.out.println("count is " + count);
         for (UnstructuredSearchResultItem riUdxBean : riUdxbeans) {
            System.out.println("RiUdx URL " + riUdxBean.getUrl() + "  RiUdx Rank is " + riUdxBean.getWeight()
                     + " RiUdx shortDescription " + riUdxBean.getShortDescription());

         }
      } catch (UDXException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }
}
