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

import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

import com.execue.core.ExeCueBaseTest;
import com.execue.core.common.api.IUserContext;
import com.execue.core.common.bean.entity.Application;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.DataSource;
import com.execue.core.common.bean.security.User;
import com.execue.core.common.bean.swi.ApplicationDeletionContext;
import com.execue.core.common.bean.swi.AssetAnalysisReportInfo;
import com.execue.core.common.bean.swi.AssetAnalysisTableInfo;
import com.execue.core.common.bean.swi.AssetOperationColumn;
import com.execue.core.common.bean.swi.AssetOperationMember;
import com.execue.core.common.type.CheckType;
import com.execue.core.exception.swi.AssetAnalysisException;
import com.execue.core.exception.swi.KDXException;
import com.execue.core.exception.swi.SDXException;
import com.execue.core.util.ExeCueUtils;
import com.execue.qi.test.TestUserContext;
import com.execue.swi.service.impl.SDXServiceImpl;

public class ApplicationServiceTest extends ExeCueBaseTest {

   private static final Logger log = Logger.getLogger(ApplicationServiceTest.class);

   @BeforeClass
   public static void setUp () {
      baseTestSetup();
   }

   // @Test
   public void testGetAllApplications () {
      try {
         List<Application> existingApps = getApplicationService().getAllApplications();
         System.out.println("Size " + existingApps.size());
         Application application = new Application();
         application.setName("My App");
         application.setCreatedDate(new Date());
         User user = new User();
         user.setId(1L);
         application.setOwner(user);
         getApplicationService().createApplicationHierarchy(application);
      } catch (KDXException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testGetApplications () {
      try {
         List<Application> applications = getApplicationService().getApplications(1L);
         Assert.assertEquals("application size must not be zero", applications.size());
      } catch (KDXException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testDeleteApplication () {
      Long applicationId = 114L;
      Long userId = 1L;
      try {
         ApplicationDeletionContext ctx = new ApplicationDeletionContext();
         ctx.setApplicationId(applicationId);
         ctx.setUserId(userId);
         getApplicationService().deleteApplicationHierarchy(ctx);

      } catch (KDXException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   // log.error(sdxException, sdxException);@Test
   public void testGetDisplayableDataSources () {
      SDXServiceImpl sdxService = null;
      IUserContext preUserContext = null;
      try {
         sdxService = (SDXServiceImpl) getSDXService();
         preUserContext = sdxService.getUserContext();
         IUserContext userContext = new TestUserContext();
         sdxService.setUserContext(userContext);
         List<DataSource> displayableDataSources = getSDXService().getDisplayableDataSources(true);
         if (log.isDebugEnabled()) {
            log.info("Displayable Data Sources : ");
            int index = 0;
            for (DataSource dataSource : displayableDataSources) {
               log.info((++index) + ". " + dataSource.getName());
            }
         }
      } catch (SDXException sdxException) {
         log.error(sdxException, sdxException);
      } finally {
         sdxService.setUserContext(preUserContext);
      }
   }

    @Test
   public void testGetAllTableIdsForAsset () {
      Long assetId = 7052L;
      try {
         List<Long> tableIds = getSDXService().getAllTableIdsForAsset(assetId,CheckType.YES);
         log.debug("Tables size ::" + tableIds.size());
         for (Long tableId : tableIds) {
            log.debug("TableId ::" + tableId);
         }

      } catch (SDXException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testLookupTableIdsForAsset () {
      Long assetId = 1001L;
      try {
         List<Long> tableIds = getSDXService().getLookupTableIdsForAsset(assetId);
         log.debug("Tables size ::" + tableIds.size());
         for (Long tableId : tableIds) {
            log.debug("TableId ::" + tableId);
         }

      } catch (SDXException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testLookupTableIdsWithMembersForAsset () {
      Long assetId = 1001L;
      try {
         List<Long> tableIds = getSDXService().getLookupTableIdsWithMembers(assetId);
         log.debug("Tables size ::" + tableIds.size());
         for (Long tableId : tableIds) {
            log.debug("TableId ::" + tableId);
         }

      } catch (SDXException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testLgetAllColumnIdsForTable () {
      Long assetId = 1001L;
      Long tableId = 1023L;

      try {
         List<Long> tableIds = getSDXService().getAllColumnIdsForTable(assetId, tableId);
         log.debug("Tables size ::" + tableIds.size());
         for (Long id : tableIds) {
            log.debug("TableId ::" + id);
         }

      } catch (SDXException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testGetAllMemberIds () {
      Long assetId = 1001L;
      Long tableId = 1023L;

      try {
         List<Long> tableIds = getSDXService().getAllMemberIds(assetId, tableId);
         log.debug("Tables size ::" + tableIds.size());
         for (Long id : tableIds) {
            log.debug("TableId ::" + id);
         }

      } catch (SDXException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testAnalyseLookupTablesWithoutMembers () throws SDXException {
      Asset asset = getSDXService().getAssetById(1001L);
      try {
         AssetAnalysisReportInfo analyseLookupTablesWithoutMembers = getAssetAnalysisService()
                  .analyseLookupTablesWithoutMembers(asset);
         log.debug("Tables size ::" + analyseLookupTablesWithoutMembers.getAssetAnalysisTablesInfo().size());

      } catch (AssetAnalysisException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

   }

   // @Test
   public void testanalyseColumnWithoutColumnType () throws SDXException {
      Asset asset = getSDXService().getAssetById(1001L);
      try {
         AssetAnalysisReportInfo analyseColumnWithoutColumnType = getAssetAnalysisService()
                  .analyseColumnWithoutColumnType(asset);
         System.out.println("report size ::" + analyseColumnWithoutColumnType.getAssetAnalysisTablesInfo().size());
         for (AssetAnalysisTableInfo tableInfo : analyseColumnWithoutColumnType.getAssetAnalysisTablesInfo()) {
            System.out.println("table Name ::" + tableInfo.getOperationTable().getName());
            List<AssetOperationColumn> columns = tableInfo.getOperationColumns();
            if (!ExeCueUtils.isCollectionEmpty(columns)) {
               for (AssetOperationColumn colum : columns) {
                  log.debug("column Id ::" + colum.getId());
               }
            }
         }

      } catch (AssetAnalysisException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

   }

   // @Test
   public void testGetColumnsWithoutKDXType () {
      Long assetId = 1001L;
      Long tableId = 1006L;
      try {
         List<Long> columnIds = getSDXService().getColumnsWithoutKDXDataType(assetId, tableId);
         System.out.println("columns ids size ::" + columnIds.size());
         for (Long columnId : columnIds) {
            log.debug("column Id :" + columnId);
         }
      } catch (SDXException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

   }

   // @Test
   public void testAnalyseUnmappedColumns () throws SDXException {
      Asset asset = getSDXService().getAssetById(1001L);
      try {
         AssetAnalysisReportInfo analyseColumnWithoutColumnType = getAssetAnalysisService().analyseUnmappedColumns(
                  asset);
         log.debug("report size ::" + analyseColumnWithoutColumnType.getAssetAnalysisTablesInfo().size());
         for (AssetAnalysisTableInfo tableInfo : analyseColumnWithoutColumnType.getAssetAnalysisTablesInfo()) {
            System.out.println("table Name ::" + tableInfo.getOperationTable().getName());
            List<AssetOperationColumn> columns = tableInfo.getOperationColumns();
            if (!ExeCueUtils.isCollectionEmpty(columns)) {
               for (AssetOperationColumn colum : columns) {
                  log.debug("column Id ::" + colum.getId());
               }
            }
         }

      } catch (AssetAnalysisException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

   }

   // @Test
   public void testAnalyseUnmappedMembers () throws SDXException {
      Asset asset = getSDXService().getAssetById(1L);
      try {
         AssetAnalysisReportInfo analyseColumnWithoutColumnType = getAssetAnalysisService().analyseUnmappedMembers(
                  asset);
         log.debug("report size ::" + analyseColumnWithoutColumnType.getAssetAnalysisTablesInfo().size());
         for (AssetAnalysisTableInfo tableInfo : analyseColumnWithoutColumnType.getAssetAnalysisTablesInfo()) {
            System.out.println("table Name ::" + tableInfo.getOperationTable().getName());
            List<AssetOperationMember> members = tableInfo.getOperationMembers();
            if (!ExeCueUtils.isCollectionEmpty(members)) {
               for (AssetOperationMember member : members) {
                  log.debug("Member Id ::" + member.getId());
               }
            }
         }

      } catch (AssetAnalysisException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

   }

   @Test
   public void testAnalyseAssetGrain () throws SDXException {
      Asset asset = getSDXService().getAssetById(1L);
      try {
         AssetAnalysisReportInfo assetAnalysisReportInfo = getAssetAnalysisService().analyseAssetForGrain(asset);
         log.debug(assetAnalysisReportInfo.getThresholdMessage());
      } catch (AssetAnalysisException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

   }

   @Test
   public void testcreateApplication () {
      Application application = null;
      try {
         application = new Application();
         application.setName("NewApplication2");
         application.setDescription("New Application 2");
         User user = new User();
         user.setId(1L);
         application.setOwner(user);
         application.setCreatedDate(new Date());
         application.setPopularity(100L);
         getApplicationService().createApplicationHierarchy(application);
      } catch (KDXException e) {
         e.printStackTrace();
      }
   }
}