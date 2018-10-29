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


package com.execue.publisher.upload;

import java.util.List;

import com.execue.core.common.bean.publisher.DBTableInfo;
import com.execue.core.common.bean.publisher.PublisherBatchDataReadContext;
import com.execue.core.common.bean.publisher.PublisherMetaReadContext;
import com.execue.publisher.exception.PublisherException;

/**
 * This service is for populating the java objects from csv file
 * 
 * @author Vishay
 * @since 20/10/09
 * @version 1.0
 */
public interface IPublisherDataUploadService {

   public List<DBTableInfo> populateDBTables (PublisherMetaReadContext publisherUploadContext)
            throws PublisherException;

   public List<List<Object>> populateDBTableDataBatch (PublisherBatchDataReadContext batchDataReadContext)
            throws PublisherException;

}