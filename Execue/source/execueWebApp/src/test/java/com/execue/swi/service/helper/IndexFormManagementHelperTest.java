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


package com.execue.swi.service.helper;

import static junit.framework.Assert.fail;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.execue.core.ExeCueBaseTest;
import com.execue.core.common.bean.entity.Cloud;
import com.execue.core.common.bean.entity.CloudComponent;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.common.bean.entity.RICloud;
import com.execue.core.exception.swi.KDXException;

public class IndexFormManagementHelperTest extends ExeCueBaseTest {

   private static final Logger log = Logger.getLogger(IndexFormManagementHelperTest.class);

   @BeforeClass
   public static void setUp () {
      baseTestSetup();
   }

   @AfterClass
   public static void tearDown () {
      baseTestTeardown();
   }

   @Test
   public void testPrepareRICloudEntry () {
      try {
         Model model = getKDXRetrievalService().getModelByName("Base Model");
         Cloud cloud = getKDXCloudRetrievalService().getCloudByName("MonthTimeFrame");
         Long modelGroupId = getKDXRetrievalService().getPrimaryGroup(model.getId()).getId();
         List<CloudComponent> cloudComponents = getKDXCloudRetrievalService()
                  .getAllCloudComponentsByCloudIdWithDetails(cloud.getId());
         for (CloudComponent cloudComponent : cloudComponents) {
            printRICloud(getIndexFormManagementHelper().prepareRICloudEntry(cloudComponent, modelGroupId));
         }
      } catch (KDXException kdxException) {
         log.error(kdxException, kdxException);
         fail(kdxException.getMessage());
      }
   }

   private void printRICloud (RICloud riCloud) {
      StringBuilder sb = new StringBuilder();

      sb.append(riCloud.getId()).append("\t");
      sb.append(riCloud.getCloudId()).append("\t");
      sb.append(riCloud.getCloudName()).append("\t");
      sb.append(riCloud.getCloudTypeBusinessEntityId()).append("\t");
      sb.append(riCloud.getCloudTypeName()).append("\t");
      sb.append(riCloud.getCloudCategory()).append("\t");
      sb.append(riCloud.getCloudOutput()).append("\t");
      sb.append(riCloud.getModelGroupId()).append("\t");
      sb.append(riCloud.getComponentBusinessEntityId()).append("\t");
      sb.append(riCloud.getComponentName()).append("\t");
      sb.append(riCloud.getComponentTypeBusinessEntityId()).append("\t");
      sb.append(riCloud.getComponentTypeName()).append("\t");
      sb.append(riCloud.getRealizationBusinessEntityId()).append("\t");
      sb.append(riCloud.getRealizationName()).append("\t");
      sb.append(riCloud.getRequiredBehaviorBusinessEntityId()).append("\t");
      sb.append(riCloud.getRequiredBehaviorName()).append("\t");
      sb.append(riCloud.getComponentCategory()).append("\t");
      sb.append(riCloud.getEntityType()).append("\t");
      sb.append(riCloud.getImportance()).append("\t");
      sb.append(riCloud.getFrequency()).append("\t");
      sb.append(riCloud.getRequired()).append("\t");
      sb.append(riCloud.getDefaultValue()).append("\t");
      sb.append(riCloud.getPrimaryRICloudId()).append("\t");

      if (log.isInfoEnabled()) {
         log.info(sb.toString());
      }
   }
}
