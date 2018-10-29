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

import org.junit.After;
import org.junit.Before;

import com.execue.core.ExeCueBaseTest;
import com.execue.core.common.bean.entity.PublishedFileInfo;
import com.execue.core.common.type.CheckType;
import com.execue.core.exception.qdata.PublishedFileException;

public class PublishedFileDAOTest extends ExeCueBaseTest {

   @Before
   public void setUp () {
      baseTestSetup();
   }

   @After
   public void tearDown () {
      baseTestTeardown();
   }

   public void testSavePublishedFileInfo () {
      try {

         PublishedFileInfo publishedFileInfo = new PublishedFileInfo();
         publishedFileInfo.setId(1L);
         publishedFileInfo.setOriginalFileName("MyFile");
         publishedFileInfo.setFileLocation("fileLocation");
         publishedFileInfo.setUserId(1L);
         publishedFileInfo.setApplicationId(1L);
         publishedFileInfo.setDatasourceId(101L);
         publishedFileInfo.setOperationSuccessful(CheckType.NO);
         
         getPublishedFileDAO().persistPublishedFileInfo(publishedFileInfo);
      } catch (PublishedFileException e) {
         e.printStackTrace();
      }
   }

   /*
    * public void testSavePublishedFileInfoDetail () { try { PublishedFileInfoDetails publishedFileInfoDetail = new
    * PublishedFileInfoDetails(); publishedFileInfoDetail.setPropertyName("propertyName");
    * publishedFileInfoDetail.setPropertyValue("propertyValue"); PublishedFileInfo publishedFileInfo =
    * getPublishedFileDAO().getPublishedFileInfoById(1L);
    * publishedFileInfoDetail.setPublishedFileInfo(publishedFileInfo);
    * getPublishedFileDAO().persistPublishedFileInfoDetails(publishedFileInfoDetail); } catch (PublishedFileException e) {
    * e.printStackTrace(); } } public void testSavePublishedFileTableInfo () { try { PublishedFileTableInfo
    * publishedFileInfoDetail = new PublishedFileTableInfo(); PublishedFileInfo publishedFileInfo =
    * getPublishedFileDAO().getPublishedFileInfoById(1L);
    * publishedFileInfoDetail.setPublishedFileInfo(publishedFileInfo);
    * getPublishedFileDAO().persistPublishedFileTableInfo(publishedFileInfoDetail); } catch (PublishedFileException e) {
    * e.printStackTrace(); } } @Test public void testSavePublishedFileTableDetails () { System.out.println("Test"); try {
    * PublishedFileTableDetails publisheFileTableDetail = new PublishedFileTableDetails(); PublishedFileTableInfo
    * publishedFileTableInfo = getPublishedFileDAO().getPublishedFileTableInfoById(1L);
    * publisheFileTableDetail.setBaseColumnName("baseColumnName"); publisheFileTableDetail.setBasePrecision(1);
    * publisheFileTableDetail.setEvaluatedPrecision(2); publisheFileTableDetail.setBaseScale(3);
    * publisheFileTableDetail.setEvaluatedScale(4); publisheFileTableDetail.setKdxDataType(ColumnType.MEASURE);
    * publisheFileTableDetail.setColumnIndex(1); publisheFileTableDetail.setIsDistribution(CheckType.NO);
    * publisheFileTableDetail.setIsPopulation(CheckType.NO); publisheFileTableDetail.setIsLocationBased(CheckType.NO);
    * publisheFileTableDetail.setIsTimeBased(CheckType.NO);
    * publisheFileTableDetail.setPublishedFileTableInfo(publishedFileTableInfo);
    * getPublishedFileDAO().persistPublishedFileTableDetails(publisheFileTableDetail); } catch (PublishedFileException
    * e) { e.printStackTrace(); } }
    */
}
