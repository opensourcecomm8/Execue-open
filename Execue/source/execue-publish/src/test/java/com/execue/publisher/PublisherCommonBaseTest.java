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


package com.execue.publisher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import com.csvreader.CsvReader;
import com.execue.core.common.bean.publisher.PublisherMetaReadContext;

public abstract class PublisherCommonBaseTest extends PublisherBaseTest {

   public PublisherMetaReadContext populatePublisherMetaReadContext (String filePath, Long applicationId, Long userId,
            boolean isColumnsAvailable) throws FileNotFoundException {
      PublisherMetaReadContext publisherUploadContext = new PublisherMetaReadContext();
      publisherUploadContext.setApplicationId(applicationId);
      publisherUploadContext.setUserId(userId);
      publisherUploadContext.setColumnsAvailable(isColumnsAvailable);
      File file = new File(filePath);
      BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
      CsvReader csvReader = new CsvReader(bufferedReader, ',');
      publisherUploadContext.setFileName(file.getName());
      publisherUploadContext.setCsvReader(csvReader);
      publisherUploadContext.setOriginalFileName(file.getName());
      return publisherUploadContext;
   }

}
