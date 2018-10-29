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

import com.execue.core.common.bean.entity.unstructured.SemantifiedContent;
import com.execue.core.common.bean.entity.unstructured.SemantifiedContentFeatureInformation;
import com.execue.core.common.bean.uss.UnstructuredKeywordSearchInput;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.ProcessingFlagType;
import com.execue.dataaccess.exception.DataAccessException;

public interface ISemantifiedContentDAO extends IUnstructuredWHContextCrudDAO {

   public Integer populateSemantifiedContentKeyWordMatchResultForUserQuery (
            UnstructuredKeywordSearchInput unstructuredKeywordSearchInput) throws DataAccessException;

   public void markSemantifiedContentForResemantification (Long contextId, Long userQueryId) throws DataAccessException;

   public void updateSemantifiedContentByProcessedStateUserQueryId (Long contextId, Long userQueryId,
            ProcessingFlagType processingFlagType) throws DataAccessException;

   public Long getSemantifiedConentKeywordMatchMaxExecutionDate (Long contextId) throws DataAccessException;

   public void deleteSemantifiedContentKeyWordMatchByExecutionDate (Long contextId, Date executionDate)
            throws DataAccessException;

   public void deleteSemantifiedContentKeyword (Long contextId, Date contentDate) throws DataAccessException;

   public void deleteSemantifiedContentFeatureInfo (Long contextId, Date contentDate) throws DataAccessException;

   public void deleteSemantifiedContentByContentDate (Long contextId, Date contentDate) throws DataAccessException;

   public List<SemantifiedContentFeatureInformation> getSemantifiedContentFeatureInfoBySemantifiedContentId (
            Long contextId, Long semantifiedContentId) throws DataAccessException;

   public List<SemantifiedContent> getSemantfiedContentByIds (Long contextId, List<Long> semantifiedContentIds)
            throws DataAccessException;

   public void deleteExistingSemantifiedContentFeatureInfo (Long contextId, Long semantifiedContentId)
            throws DataAccessException;

   public void deleteExistingSemantifiedContentKeywordInfo (Long contextId, Long semantifiedContentId)
            throws DataAccessException;

   public List<Long> getSemantifiedContentIdsByUserQueryId (Long contextId, Long userQueryId)
            throws DataAccessException;

   public void updateSemantifiedContentImageURLProcessedStateByBatchId (Long contextId,
            ProcessingFlagType updatingProcessedState, Long batchId, ProcessingFlagType existingProcessedState)
            throws DataAccessException;

   public List<Long> getSemantifiedContentIdsByImageUrlProcessedState (Long contextId, ProcessingFlagType processed,
            int batchSize) throws DataAccessException;

   public List<Long> getSemantifiedContentIdsByBadImageUrlLikeDomain (Long contextId, String badImageURLDomainName)
            throws DataAccessException;

   public void setImagePresentInSemantifiedContentFeatureInfo (Long contextId, Long semantifiedContentId,
            CheckType imagePresent) throws DataAccessException;

   public void setImageURLToNullInSemantifiedContent (Long contextId, Long semantifiedContentId)
            throws DataAccessException;
   
   public void updateSemantifiedContentImageURLProcessedState (Long contextId, ProcessingFlagType processedState,
            Long minSemantifiedContentId, Long maxSemantifiedContentId, Long batchId) throws DataAccessException;
}
