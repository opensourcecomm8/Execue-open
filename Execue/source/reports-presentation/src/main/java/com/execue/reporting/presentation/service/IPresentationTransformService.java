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


package com.execue.reporting.presentation.service;

import com.execue.core.IService;
import com.execue.core.common.type.AggregateQueryType;
import com.execue.reporting.presentation.bean.PresentationTransformData;

/**
 * Transform Service to convert Query Data summary report data to specific need
 * 
 * @author kaliki
 * @since 4.0
 */

public interface IPresentationTransformService extends IService {

   /**
    * get only Data part of report xml
    * 
    * @param queryId
    * @param assetId
    * @return
    */
   public PresentationTransformData getData (long queryId, long assetId, long businessQueryId);

   /**
    * get only Header part of report xml
    * 
    * @param queryId
    * @param assetId
    * @return
    */
   public PresentationTransformData getHeader (long queryId, long assetId, long businessQueryId);

   /**
    * get both header and data of report xml
    * 
    * @param queryId
    * @param assetId
    * @return
    */
   // public PresentationTransformData getReport (long queryId, long assetId, long businessQueryId);
   public PresentationTransformData getReport (long queryId, long assetId, long businessQueryId, AggregateQueryType type);

   public PresentationTransformData getReport (int pageNum, int pageSize, long queryId, long assetId,
            long businessQueryId, AggregateQueryType type);

   public PresentationTransformData getReport (int pageNum, int pageSize, long aggregatedQueryId);
}
