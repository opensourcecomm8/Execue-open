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


package com.execue.dataaccess.qdata.dao;

import static org.junit.Assert.fail;

import java.util.Date;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.execue.core.ExeCueBaseTest;
import com.execue.core.common.bean.qdata.QDataUserQuery;
import com.execue.core.exception.dataaccess.DataAccessException;

public class QDataUserQueryDAOTest extends ExeCueBaseTest {

   private static final Logger log = Logger.getLogger(QDataUserQueryDAOTest.class);
   
   @BeforeClass
   public static void setup () {
      baseTestSetup();
   }

   @AfterClass
   public static void teardown () {
      baseTestTeardown();
   }

   // @Test
   public void testUserQueryCreation () {
      QDataUserQuery qDataUserQuery = new QDataUserQuery();
      qDataUserQuery.setId(12L);
      qDataUserQuery.setExecutionDate(new Date());
      try {
         getQDataUserQueryDAO().storeUserQuery(qDataUserQuery);
         if (log.isInfoEnabled()) {
            log.info("UQ ID : "+qDataUserQuery.getId() + " - " + qDataUserQuery.getExecutionDate());
         }
      } catch (DataAccessException e) {
         fail(e.getMessage());
      }
   }
}
