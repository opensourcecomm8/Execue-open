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

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.execue.core.ExeCueBaseTest;
import com.execue.core.common.bean.entity.DataSource;
import com.execue.core.common.type.DataSourceType;
import com.execue.core.exception.swi.SDXException;

public class DataSourceSelectionServiceTest extends ExeCueBaseTest {

   private static final Logger log = Logger.getLogger(DataSourceSelectionServiceTest.class);

   @BeforeClass
   public static void setup () {
      baseTestSetup();
   }

   @AfterClass
   public static void tearDown () {
      baseTestTeardown();
   }

   @Test
   public void testGetLeasetLoadedDataSource () throws Exception {
      DataSource dataSource = getDataSourceSelectionService().getDataSourceForUploadedDatasets(1L);
      if (log.isDebugEnabled()) {
         log.debug("Least loaded Data Source for Uploaded datasets : " + dataSource.getName());
      }
      dataSource = getDataSourceSelectionService().getDataSourceForCatalogDatasets();
      if (log.isDebugEnabled()) {
         log.debug("Least loaded Data Source for Catalog datasets : " + dataSource.getName());
      }
   }
   
   @Test
   public void testGetLeasetLoadedDataSourceFromDAM () {
      try {
         DataSource dataSource = getSDXDataAccessManager().getLeasetLoadedDataSource(DataSourceType.UPLOADED);
         System.out.println(dataSource.getDisplayName());
      } catch (SDXException e) {
         e.printStackTrace();
      }
   }
}
