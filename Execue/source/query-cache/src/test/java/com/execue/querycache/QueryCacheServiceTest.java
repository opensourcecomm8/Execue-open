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


package com.execue.querycache;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.execue.core.common.bean.qdata.QueryCacheResultInfo;
import com.execue.querycache.exception.QueryCacheException;

public class QueryCacheServiceTest extends QueryCacheServiceCommonTest {

   @BeforeClass
   public static void setUp () {
      baseSetup();
   }

   @AfterClass
   public static void tearDown () {
      baseTeardown();
   }

   @Test
   public void testGetQueryCacheResults () {

      Long existingUserQueryId = 20000170057L;
      Long applicationId = 1530L;

      try {
         List<QueryCacheResultInfo> queryCacheResultInfo = getQueryCacheService().getQueryCacheResults(
                  existingUserQueryId, applicationId);
         System.out.println(queryCacheResultInfo.size());
      } catch (QueryCacheException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

   }
}