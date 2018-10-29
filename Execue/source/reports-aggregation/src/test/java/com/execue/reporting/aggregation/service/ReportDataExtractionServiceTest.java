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


package com.execue.reporting.aggregation.service;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.execue.core.common.bean.SQLExeCueCachedResultSet;
import com.execue.core.common.bean.entity.DataSource;
import com.execue.core.common.bean.querygen.SelectQueryInfo;
import com.execue.core.common.type.AssetProviderType;
import com.execue.dataaccess.IGenericJDBCDAO;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.dataaccess.impl.GenericJDBCDAOImpl;
import com.execue.reporting.aggregation.AggregationBaseTest;

public class ReportDataExtractionServiceTest extends AggregationBaseTest {

   private static final Logger logger = Logger.getLogger(ReportDataExtractionServiceTest.class);

   @BeforeClass
   public static void setUp () throws Exception {
      logger.debug("Inside the Setup Method");
      baseSetup();
   }

   @AfterClass
   public static void teardown () {
      logger.debug("Inside the TearDown Method");
      baseTeardown();
   }

   @Test
   public void testDataExtraction () {

      IGenericJDBCDAO genericJDBCDAO = new GenericJDBCDAOImpl();
      // IReportDataExtractionService reportDataExtractionService = new ReportDataExtractionServiceImpl();

      // prepare sample query string to be run on the warehouse
      // String queryString1 = "select vntg_dt, vntg_ym, vntg_ym_desc from vntg_ym";
      String queryString = "select CARD_TYPE_CD, card_type from card_type";

      // prepare the asset data source connection details
      DataSource oracleDataSource = new DataSource();
      oracleDataSource.setProviderType(AssetProviderType.Oracle);
      oracleDataSource.setSchemaName("ORCL1");
      oracleDataSource.setLocation("10.10.52.111");
      oracleDataSource.setPort(new Integer(1521));
      oracleDataSource.setUserName("abcclient".toUpperCase());
      oracleDataSource.setPassword("execue");

      // execute the query
      SQLExeCueCachedResultSet sqlCachedResultSet = null;
      genericJDBCDAO = new GenericJDBCDAOImpl();

      try {
         sqlCachedResultSet = (SQLExeCueCachedResultSet) genericJDBCDAO.executeQuery(oracleDataSource.getName(),
                  new SelectQueryInfo(queryString));
      } catch (DataAccessException e) {
         e.printStackTrace();
      }
      // ReportQueryData reportQueryData = reportDataExtractionService.extractReportQueryData(sqlCachedResultSet);
      // System.out.println(reportQueryData.getXmlData());
   }
}