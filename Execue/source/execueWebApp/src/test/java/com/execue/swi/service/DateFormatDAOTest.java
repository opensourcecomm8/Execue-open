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

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.execue.core.ExeCueBaseTest;
import com.execue.core.common.bean.entity.DateFormat;
import com.execue.core.common.type.AssetProviderType;
import com.execue.core.common.type.CheckType;
import com.execue.core.exception.dataaccess.DataAccessException;
import com.execue.dataaccess.swi.dao.IDateFormatDAO;

public class DateFormatDAOTest extends ExeCueBaseTest {

   @Before
   public void setUp () {
      baseTestSetup();
   }

   @After
   public void tearDown () {
      baseTestTeardown();
   }

   // @Test
   public void testCreateMessage () {
      IDateFormatDAO dateFormatDAO = getDateFormatDAO();
      try {
         // DateFormat dateFormat = dateFormatDAO.getDateFormat(1001L);
         // System.out.println("Id::" + dateFormat.getId());
         // DateFormat dateFormat = dateFormatDAO.getDateFormat("yyyy/MM/dd", AssetProviderType.MySql);
         // System.out.println("Id::" + dateFormat.getId());
         List<DateFormat> dateFormat = dateFormatDAO.getSupportedDateFormats(AssetProviderType.MySql, CheckType.YES);
         for (DateFormat dateFormat2 : dateFormat) {
            System.out.println("dateFormat Id::" + dateFormat2.getId());
         }
      } catch (DataAccessException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   @Test
   public void testGetAllDateFormats () {
      IDateFormatDAO dateFormatDAO = getDateFormatDAO();
      try {
         List<DateFormat> dateFormats = dateFormatDAO.getAllSupportedDateFormats();
         for (DateFormat dateFormat2 : dateFormats) {
            System.out.println("dateFormat Id::" + dateFormat2.getId());
         }

      } catch (DataAccessException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

   }

}
