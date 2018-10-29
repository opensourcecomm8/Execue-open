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


package com.execue.das;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.execue.core.common.bean.entity.DataSource;
import com.execue.core.common.bean.querygen.SQLIndex;
import com.execue.dataaccess.exception.DataAccessException;

public class SystemDataAccessServicesTest extends DataAccessServicesCommonBaseTest {

   private static final Logger logger = Logger.getLogger(SystemDataAccessServicesTest.class);

   @BeforeClass
   public static void setUp () {
      baseSetup();
   }

   @AfterClass
   public static void tearDown () {
      baseTeardown();
   }

   @Test
   public void TestCreateMultipleIndexesViaSingleDDLOnTable () {
      DataSource dataSource = null;
      List<SQLIndex> indexes = null;
      try {
         indexes = buildIndexesForMultipleIndexesViaSingleDDL();
         dataSource = getDataSourceDAO().getDataSource("answerscatalog");
         getSystemDataAccessService().createMultipleIndexesOnTable(dataSource, indexes, 30);
         for (SQLIndex sqlIndex : indexes) {
            System.out.println(sqlIndex.getName());
         }
         
         indexes = buildIndexesForMultipleIndexesViaSingleDDLOracle();
         dataSource = getDataSourceDAO().getDataSource("testDS");
         getSystemDataAccessService().createMultipleIndexesOnTable(dataSource, indexes, 30);
         for (SQLIndex sqlIndex : indexes) {
            System.out.println(sqlIndex.getName());
         }
      } catch (DataAccessException e) {
         Assert.fail(e.getMessage());
         logger.error(e, e);
      }
   }
}
