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


package com.execue.uswhda.dataaccess.dao;

import java.util.Date;
import java.util.List;

import com.execue.core.common.bean.querygen.QueryTable;
import com.execue.core.common.type.ProcessingFlagType;
import com.execue.dataaccess.exception.DataAccessException;

public interface ISourceContentDAO extends IUnstructuredWHContextCrudDAO {

   public void updateSourceContentProcessedStateByBatchId (Long contextId, Long batchId,
            ProcessingFlagType existingProcessedState, ProcessingFlagType targetProcessedState)
            throws DataAccessException;

   public void updateSourceContentByProcessingState (Long contextId, Long batchId, Long startSourceContentId,
            Long endSourceContentId, ProcessingFlagType processingFlagType) throws DataAccessException;

   public List<Long> getLatestSourceContentIds (Long contextId, int batchSize) throws DataAccessException;

   public Long getMaxSourceContentItemIdByContextAndSource (Long contextId, Long sourceNodeId)
            throws DataAccessException;

   public void deleteSourceContentByContentDate (Long contextId, Date contentDate) throws DataAccessException;

   public void removeDuplicateFromSourceContentTempTable (Long contextId, QueryTable queryTable)
            throws DataAccessException;

   public void populateSourceContentTable (Long contextId, QueryTable queryTable) throws DataAccessException;
}
