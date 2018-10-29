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


package com.execue.sforce.dataaccess;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.execue.core.exception.dataaccess.DataAccessException;
import com.execue.sforce.SforceBaseTest;
import com.execue.sforce.bean.entity.SObjectMetaEntity;
import com.execue.sforce.exception.SforceException;

public class SforceDAOTest extends SforceBaseTest {

   @Before
   public void setUp () throws Exception {
      baseTestSetup();
   }

   /**
    * @throws java.lang.Exception
    */
   @After
   public void tearDown () throws Exception {
      baseTestTearDown();
   }

  // @Test
   public void testGetLastModifiedDataDate () {
      try {
         System.out.println(getSforceDAO().getLastModifiedDataDate(1L));
      } catch (SforceException e) {
         e.printStackTrace();
      }
   }
   @Test
   public void testSObjectMetaEntiyCreation () {
      SObjectMetaEntity sObjectMetaEntity = new SObjectMetaEntity();
      sObjectMetaEntity.setId(2L);
      sObjectMetaEntity.setSobjectName("account");
      sObjectMetaEntity.setCreationDate(new Date());
      sObjectMetaEntity.setLastModifiedData(new Date());
      sObjectMetaEntity.setLastModifiedMeta(new Date());

      try {

         getSforceDAO().create(sObjectMetaEntity);
      } catch (DataAccessException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

   }

}
