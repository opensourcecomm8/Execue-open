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


package com.execue.core;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.AssetEntityDefinition;
import com.execue.core.common.bean.entity.Join;
import com.execue.core.common.bean.entity.Mapping;
import com.execue.core.common.type.AssetEntityType;
import com.execue.dataaccess.swi.dao.IAssetDAO;

/**
 * This class is Junit test class for testing DAO operations on Asset object.
 * 
 * @author Kaliki
 * @version 1.0
 */
public class AssetDuplicateDAOTest extends ExeCueBaseTest {

   private static final Logger logger = Logger.getLogger(AssetDuplicateDAOTest.class);

   @BeforeClass
   public static void setup () {
      logger.debug("Inside the Setup Method");
      baseTestSetup();
   }

   @Test
   public void testProcessor () {

      DataSourceTransactionManager transactionManager = (DataSourceTransactionManager) getContext().getBean(
               "swiTxManager");

      TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);

      transactionTemplate.execute(new TransactionCallbackWithoutResult() {

         public void doInTransactionWithoutResult (TransactionStatus status) {
            Asset asset = null;
            try {
               asset = (Asset) ((IAssetDAO) getContext().getBean("assetDAO")).getById(new Long(11), Asset.class);
               Hibernate.initialize(asset.getAssetEntityDefinitions());
               // asset entites
               for (AssetEntityDefinition entityDefinition : asset.getAssetEntityDefinitions()) {
                  // get tables
                  if (AssetEntityType.TABLE == entityDefinition.getEntityType()) {
                     Hibernate.initialize(entityDefinition.getTabl());
                     entityDefinition.getTabl().setId(null);
                  }
                  // get columns
                  if (AssetEntityType.COLUMN == entityDefinition.getEntityType()) {
                     Hibernate.initialize(entityDefinition.getColum());
                     entityDefinition.getColum().setId(0L);
                     Hibernate.initialize(entityDefinition.getColum().getConstraints());
                     for (com.execue.core.common.bean.entity.Constraint constraint : entityDefinition.getColum()
                              .getConstraints()) {
                        constraint.setId(0L);
                     }
                  }
                  // get member
                  if (AssetEntityType.MEMBER == entityDefinition.getEntityType()) {
                     Hibernate.initialize(entityDefinition.getMembr());
                     entityDefinition.getMembr().setId(0L);
                  }
                  // get Mapping
                  Hibernate.initialize(entityDefinition.getMappings());
                  for (Mapping mapping : entityDefinition.getMappings()) {
                     mapping.setId(0L);
                  }
               }
               // get Joins
               Hibernate.initialize(asset.getJoins());
               for (Join join : asset.getJoins()) {
                  join.setId(0L);
               }
               Hibernate.initialize(asset.getJoinDefinitions());
               asset.setId(null);

               logger.debug("Testing 1");
               ((IAssetDAO) getContext().getBean("assetDAO")).create(asset);
               logger.debug("Testing saved");

            } catch (final Exception dae) {
               dae.printStackTrace();
            }
         }
      });

   }

   @AfterClass
   public static void teardown () {
      logger.debug("Inside the TearDown Method");
      baseTestTeardown();
   }

}
