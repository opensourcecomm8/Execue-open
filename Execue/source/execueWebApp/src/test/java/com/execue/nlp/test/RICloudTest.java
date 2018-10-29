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


package com.execue.nlp.test;

import java.io.IOException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.execue.core.ExeCueBaseTest;
import com.execue.core.common.bean.entity.Cloud;
import com.execue.core.common.bean.entity.CloudComponent;
import com.execue.core.common.bean.entity.RICloud;
import com.execue.core.exception.ConfigurationException;
import com.execue.core.exception.swi.KDXException;

public class RICloudTest extends ExeCueBaseTest {

   @Before
   public void setUp () throws ConfigurationException, IOException {
      baseTestSetup();
   }

   @After
   public void tearDown () {
      baseTestTeardown();
   }

   // @Test
   public void TestPopulateRICloudeAsRealization () {

      String QUERY_SELECT_CLOUDE = "insert into  RICloud (cloud, componentBusinessEntityId, componentTypeBusinessEntity, cloudCategory, weight, frequency,"
               + "modelGroupId, componentCategory, entityType, requiredBehavior, required, defaultValue) select c,cc.componentBed.id ,cc.componentTypeBed "
               + "c.category, cc.weight, cc.frequency, be.modelGroup.id,cc.componentCategory, cc.entityType, "
               + "cc.requiredBehavior, cc.required, cc.defaultValue"
               + " from Cloud as c, CloudComponent as cc, BusinessEntityDefinition as be "
               + "where  c.id = cc.cloud.id and cc.componentBed.id = be.id";

      SessionFactory sessionFactory = getHibernateTemplate().getSessionFactory();
      Session session = sessionFactory.openSession();
      Transaction tx = session.beginTransaction();
      session.createQuery(QUERY_SELECT_CLOUDE).executeUpdate();
      System.out.println("operation done");
      tx.commit();
      session.close();

      // ALTER TABLE ri_cloud CHANGE id ID INT(10) NOT NULL AUTO_INCREMENT;
      // ALTER TABLE ri_cloud AUTO_INCREMENT = 10001;
      // ALTER TABLE ri_cloud modify MODEL_ID int(10) null;

   }

   // @Test
   public void TestPopulateRICloudeAsType () {

      String QUERY_SELECT_CLOUDE = "insert into  RICloud (cloud, componentBusinessEntityId, realizedTypeBusinessEntity, cloudCategory, weight, frequency,"
               + " modelGroupId, componentCategory, entityType, requiredBehavior, required, defaultValue) select c,cc.componentTypeBed.id ,cc.componentBed, "
               + "c.category, cc.weight, cc.frequency, be.modelGroup.id, cc.componentCategory, cc.entityType, "
               + "cc.requiredBehavior, cc.required, cc.defaultValue"
               + " from Cloud as c, CloudComponent as cc, BusinessEntityDefinition as be "
               + "where  c.id = cc.cloud.id and cc.componentBed.id = be.id and cc.componentTypeBed.id not in (101,108) and be.modelGroup.id != 1";

      SessionFactory sessionFactory = getHibernateTemplate().getSessionFactory();
      Session session = sessionFactory.openSession();
      Transaction tx = session.beginTransaction();
      session.createQuery(QUERY_SELECT_CLOUDE).executeUpdate();
      System.out.println("operation done");
      tx.commit();
      session.close();

      // ALTER TABLE ri_cloud CHANGE id ID INT(10) NOT NULL AUTO_INCREMENT;
      // ALTER TABLE ri_cloud AUTO_INCREMENT = 10001;
      // ALTER TABLE ri_cloud modify MODEL_ID int(10) null;

   }

   @Test
   public void TestGenerateRIClouds () {
      try {
         Long modelId = new Long(101);
         Cloud cloud = getKDXCloudRetrievalService().getDefaultAppCloud(modelId);
         getRICloudsAbsorptionService().regenerateRICloudFromCloud(cloud, modelId);
      } catch (KDXException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   public void testPrepareRICloudEntries () {
      Long cloudId = null;
      Long componentBedId = null;
      CloudComponent cloudComponent = null;
      try {
         cloudComponent = getKDXCloudRetrievalService().getCloudComponentByCloudIdAndComponentBedId(cloudId,
                  componentBedId);
         RICloud riCloud = getIndexFormManagementHelper().prepareRICloudEntry(cloudComponent, 1L);
      } catch (KDXException e) {
         e.printStackTrace();
      }
   }
}
