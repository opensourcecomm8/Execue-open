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


package com.execue.publisher.absorbtion;

import java.util.List;

import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.DataSource;
import com.execue.core.common.bean.publisher.DBTableDataNormalizedInfo;
import com.execue.core.common.bean.publisher.DBTableNormalizedInfo;
import com.execue.core.common.type.SuccessFailureType;
import com.execue.publisher.exception.PublisherException;

/**
 * This service is for absorbing the data into datasource location
 * 
 * @author Vishay
 * @since 20/10/09
 * @version 1.0
 */
public interface IPublisherDataAbsorbtionService {

   public boolean absorbPublisherMetaData (DataSource dataSource, DBTableNormalizedInfo dbTableNormalizedInfo)
            throws PublisherException;

   public boolean absorbPublisherData (DataSource dataSource, DBTableDataNormalizedInfo dbTableDataNormalizedInfo)
            throws PublisherException;

   public void transformData (DataSource dataSource, DBTableNormalizedInfo orginalDbTableNormalizedInfo,
            DBTableNormalizedInfo dbTableNormalizedInfo) throws PublisherException;

   public SuccessFailureType absorbAsset (Asset asset, List<DBTableNormalizedInfo> dbTableNormalizedInfoList)
            throws PublisherException;

   public List<Long> mapSDX2KDX (Asset asset, Long modelId, List<Long> freshlyCreatedConceptDEDs,
            List<Long> freshlyCreatedInstanceDEDs) throws PublisherException;

   public void populateTotalRecordsCount (DataSource dataSource, DBTableNormalizedInfo evaluatedDBTableNormalizedInfo)
            throws PublisherException;

}
