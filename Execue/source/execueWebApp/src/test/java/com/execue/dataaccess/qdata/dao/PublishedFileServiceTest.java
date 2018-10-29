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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.execue.core.ExeCueBaseTest;
import com.execue.core.common.bean.entity.PublishedFileInfo;
import com.execue.core.common.bean.entity.PublishedFileInfoDetails;
import com.execue.core.common.bean.entity.PublishedFileTableDetails;
import com.execue.core.common.bean.entity.PublishedFileTableInfo;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.ColumnType;
import com.execue.core.common.type.ConversionType;
import com.execue.core.common.type.DataType;
import com.execue.core.common.type.PublishedOperationType;
import com.execue.core.exception.qdata.PublishedFileException;
import com.softwarefx.chartfx.server.internal.ev;

public class PublishedFileServiceTest extends ExeCueBaseTest {

   @Before
   public void setUp () {
      baseTestSetup();
   }

   @After
   public void tearDown () {
      baseTestTeardown();
   }

   // @Test
   public void testSaveUpdatePublishedFileInfo () {
      try {
         Date firstAbsorptionDate = new Date();
         Date lastAbsorptionDate = new Date();
         PublishedFileInfo publishedFileInfo = new PublishedFileInfo();// getPublishedFileService().getPublishedFileInfoById(5L);
         // publishedFileInfo.setOriginalFileName("TestPublishedFileInfoUpdate");
         publishedFileInfo.setFileLocation("TestPublishedLocation");
         publishedFileInfo.setUserId(1L);
         publishedFileInfo.setApplicationId(1L);
         publishedFileInfo.setDatasourceId(101L);
         publishedFileInfo.setPublishedOperationType(PublishedOperationType.REFRESH);
         publishedFileInfo.setOperationSuccessful(CheckType.NO);
         publishedFileInfo.setFirstAbsorptionDate(firstAbsorptionDate);
         publishedFileInfo.setLastAbsorptionDate(lastAbsorptionDate);

         getPublishedFileService().persistPublishedFileInfo(publishedFileInfo);
      } catch (PublishedFileException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testSaveUpdatePublishedFileInfoDetail () {
      try {
         List<PublishedFileInfoDetails> publishedFileInfoDetails = new ArrayList<PublishedFileInfoDetails>();
         PublishedFileInfo publishedFileInfo = getPublishedFileService().getPublishedFileInfoById(1L);
         PublishedFileInfoDetails pulishFileInfoDetail = getPublishedFileService()
                  .getyIdPublishedFileInfoDetailById(2L);
         pulishFileInfoDetail.setPropertyName("propertyNameUpdate");
         pulishFileInfoDetail.setPropertyValue("propertyValueUpdate");
         pulishFileInfoDetail.setPublishedFileInfo(publishedFileInfo);
         publishedFileInfoDetails.add(pulishFileInfoDetail);
         getPublishedFileService().updatePublishedFileInfoDetails(publishedFileInfoDetails);
      } catch (PublishedFileException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testSaveUpdatePublishedFileTbaleInfo () {
      try {
         List<PublishedFileTableInfo> publishedFileTableInfos = new ArrayList<PublishedFileTableInfo>();
         PublishedFileInfo publishedFileInfo = getPublishedFileService().getPublishedFileInfoById(1L);
         PublishedFileTableInfo publishedFileTableInfo = new PublishedFileTableInfo();
         publishedFileTableInfo.setWorkSheetName("workSheetNameUpdate");
         publishedFileTableInfo.setEvaluatedTableName("evaluatedTableNameUpdate");
         publishedFileTableInfo.setTempTableName("tempTableNameUpdate");
         publishedFileTableInfo.setPublishedFileInfo(publishedFileInfo);
         publishedFileTableInfos.add(publishedFileTableInfo);
         getPublishedFileService().persistPublishedFileTableInfo(publishedFileTableInfos);
      } catch (PublishedFileException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testSaveUpdatePublishedFileTableDetail () {
      try {
         List<PublishedFileTableDetails> publishedFileTableDetails = new ArrayList<PublishedFileTableDetails>();
         PublishedFileTableInfo publishedFileTableInfo = getPublishedFileService().getPublishedFileTableInfoById(1L);
         PublishedFileTableDetails publishedFileTableDetail = new PublishedFileTableDetails();
         publishedFileTableDetail.setBaseColumnName("baseColumnName4");
         publishedFileTableDetail.setBaseDataType(DataType.DATE);
         publishedFileTableDetail.setBasePrecision(10);
         publishedFileTableDetail.setBaseScale(1);
         publishedFileTableDetail.setColumnIndex(1);
         publishedFileTableDetail.setEvaluatedColumnName("evaluatedColumnName4");
         publishedFileTableDetail.setEvaluatedDataType(DataType.DATETIME);
         publishedFileTableDetail.setEvaluatedPrecision(11);
         publishedFileTableDetail.setEvaluatedScale(2);
         publishedFileTableDetail.setDistribution(CheckType.NO);
         publishedFileTableDetail.setPopulation(CheckType.YES);
         publishedFileTableDetail.setKdxDataType(ColumnType.DIMENSION);
         publishedFileTableDetail.setPublishedFileTableInfo(publishedFileTableInfo);
         publishedFileTableDetail.setUnitType(ConversionType.CURRENCY);

         publishedFileTableDetails.add(publishedFileTableDetail);

         getPublishedFileService().persistPublishedFileTableDetails(publishedFileTableDetails);
      } catch (PublishedFileException e) {
         e.printStackTrace();
      }
   }

   // Test case for get records
   // @Test
   public void getPublishedFileTableDetails () {
      try {
         List<PublishedFileTableDetails> pFTableDetails = getPublishedFileService()
                  .getPublishedFileTableDetailsByTableId(1L);

         for (PublishedFileTableDetails publishedFileTableDetails : pFTableDetails) {
            System.out.println("publishedFileTableDetailsId" + publishedFileTableDetails.getId());
            System.out.println("publishedFileTableDetailsName" + publishedFileTableDetails.getBaseColumnName());
         }
      } catch (PublishedFileException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   // @Test
   public void getPublishedFileTableInfo () {
      try {
         List<PublishedFileTableInfo> pFTableInfos = getPublishedFileService().getPublishedFileTableInfoByFileId(1L);
         for (PublishedFileTableInfo publishedFileTableInfo : pFTableInfos) {
            System.out.println("publishedFileTableDetailsId" + publishedFileTableInfo.getId());
            System.out.println("publishedFileTableDetailsName" + publishedFileTableInfo.getEvaluatedTableName());
         }
      } catch (PublishedFileException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   // @Test
   public void getPublishedFileInfoDetails () {
      try {
         List<PublishedFileInfoDetails> pFTableInfoDetails = getPublishedFileService()
                  .getPublishedFileInfoDetailByFileId(1L);
         for (PublishedFileInfoDetails publishedFileInfoDetail : pFTableInfoDetails) {
            System.out.println("publishedFileTableDetailsId" + publishedFileInfoDetail.getId());
            System.out.println("publishedFileTableDetailsName" + publishedFileInfoDetail.getPropertyName());
         }
      } catch (PublishedFileException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   @Test
   public void testGetPublishedFileTablesInfo () throws PublishedFileException {
      List<String> evaluatedTableNames = new ArrayList<String>();
      evaluatedTableNames.add("exeq_1_100HPCEOscsv_1nA");
      List<PublishedFileTableInfo> publishedFileTablesInfo = getPublishedFileService().getPublishedFileTablesInfo(1L,
               evaluatedTableNames);
      System.out.println(publishedFileTablesInfo.size());
   }

}
