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


package com.execue.publisher.upload.file.csv;

import java.io.FileNotFoundException;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.execue.core.common.bean.publisher.DBTableColumn;
import com.execue.core.common.bean.publisher.DBTableInfo;
import com.execue.core.common.bean.publisher.PublisherMetaReadContext;
import com.execue.publisher.PublisherCommonBaseTest;
import com.execue.publisher.exception.PublisherException;

/**
 * This test case is for testing uploading of csv file into datasource location
 * 
 * @author Vishay
 * @since 20/10/09
 * @version 1.0
 */
public class CSVFilePublisherDataUploadServiceTest extends PublisherCommonBaseTest {

   @BeforeClass
   public static void setUp () throws FileNotFoundException {
      baseTestSetup();

   }

   @AfterClass
   public static void tearDown () {

   }

   @Test
   public void testCsvMetaExtraction () throws FileNotFoundException {
      try {
         PublisherMetaReadContext publisherMetaReadContext = populatePublisherMetaReadContext(
                  "C:\\Documents and Settings\\jitendra\\Desktop\\100-HP-CEOs_jitendra.csv", new Long(101),
                  new Long(1), true);
         List<DBTableInfo> populateDBTables = getCSVFilePublisherDataUploadService().populateDBTables(
                  publisherMetaReadContext);
         System.out.println(populateDBTables.size());
         DBTableInfo dbTableInfo = populateDBTables.get(0);
         System.out.println("Table Name " + dbTableInfo.getDbTable().getTableName());
         for (DBTableColumn dbTableColumn : dbTableInfo.getDbTableColumns()) {
            System.out.println("Column Name " + dbTableColumn.getColumnName());
            System.out.println("Column DataType " + dbTableColumn.getDataType());
         }
      } catch (PublisherException e) {
         e.printStackTrace();
      }
   }

}
