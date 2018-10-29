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


package com.execue.swi.dataaccess.impl;

import java.util.List;

import com.execue.core.common.bean.entity.InstanceMappingSuggestion;
import com.execue.core.common.bean.entity.InstanceMappingSuggestionDetail;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.swi.exception.MappingException;
import com.execue.dataaccess.swi.dao.IInstanceMappingSuggestionDAO;
import com.execue.swi.dataaccess.IInstanceMappingSuggestionDataAccessManager;
import com.execue.swi.exception.SWIExceptionCodes;

public class InstanceMappingSuggestionDataAccessManagerImpl implements IInstanceMappingSuggestionDataAccessManager {

   private IInstanceMappingSuggestionDAO instanceMappingSuggestionDAO;

   /**
    * @return the instanceMappingSuggestionDAO
    */
   public IInstanceMappingSuggestionDAO getInstanceMappingSuggestionDAO () {
      return instanceMappingSuggestionDAO;
   }

   /**
    * @param instanceMappingSuggestionDAO
    *           the instanceMappingSuggestionDAO to set
    */
   public void setInstanceMappingSuggestionDAO (IInstanceMappingSuggestionDAO instanceMappingSuggestionDAO) {
      this.instanceMappingSuggestionDAO = instanceMappingSuggestionDAO;
   }

   public void createInstanceMappingSuggestion (InstanceMappingSuggestion instanceMappingSuggestion)
            throws MappingException {
      try {
         instanceMappingSuggestionDAO.create(instanceMappingSuggestion);
      } catch (DataAccessException e) {
         throw new MappingException(SWIExceptionCodes.INSTANCE_MAPPING_SUGGESTION_CREATION_FAILED, e);
      }

   }

   public void createInstanceMappingSuggestionDetails (
            List<InstanceMappingSuggestionDetail> instanceMappingSuggestionDetails) throws MappingException {
      try {
         instanceMappingSuggestionDAO.createAll(instanceMappingSuggestionDetails);
      } catch (DataAccessException e) {
         throw new MappingException(SWIExceptionCodes.INSTANCE_MAPPING_SUGGESTION_DETAIL_CREATION_FAILED, e);

      }
   }

   public List<InstanceMappingSuggestionDetail> getInstanceMappingSuggestionDetails (Long instanceMappingSuggestionId)
            throws MappingException {
      try {
         return instanceMappingSuggestionDAO.getInstanceMappingSuggestionDetails(instanceMappingSuggestionId);
      } catch (DataAccessException e) {
         throw new MappingException(SWIExceptionCodes.INSTANCE_MAPPING_SUGGESTION_DETAIL_RETRIEVAL_FAILED, e);
      }
   }

   public List<InstanceMappingSuggestionDetail> getInstanceMappingSuggestionDetailsByColumAEDId (Long columAEDId)
            throws MappingException {
      try {
         return instanceMappingSuggestionDAO.getInstanceMappingSuggestionDetailsByColumAEDId(columAEDId);
      } catch (DataAccessException e) {
         throw new MappingException(SWIExceptionCodes.INSTANCE_MAPPING_SUGGESTION_DETAIL_RETRIEVAL_FAILED, e);
      }
   }

   public void deleteInstanceMappingSuggestionDetails (List<Long> instanceMappingSuggestionDetailsIds)
            throws MappingException {
      try {
         instanceMappingSuggestionDAO.deleteInstanceMappingSuggestionDetails(instanceMappingSuggestionDetailsIds);
      } catch (DataAccessException e) {
         throw new MappingException(SWIExceptionCodes.INSTANCE_MAPPING_SUGGESTION_DETAIL_RETRIEVAL_FAILED, e);
      }

   }

   public void deleteInstanceMappingSuggestionByColumnAEDId (Long columAEDId) throws MappingException {
      try {
         instanceMappingSuggestionDAO.deleteInstanceMappingSuggestionByColumnAEDId(columAEDId);
      } catch (DataAccessException e) {
         throw new MappingException(SWIExceptionCodes.INSTANCE_MAPPING_SUGGESTION_DETAIL_RETRIEVAL_FAILED, e);
      }
   }

   public Long getInstanceMappingSuggestionDetailsCount (Long selColAedId) throws MappingException {
      try {
         return instanceMappingSuggestionDAO.getInstanceMappingSuggestionDetailsCount(selColAedId);
      } catch (DataAccessException e) {
         throw new MappingException(SWIExceptionCodes.INSTANCE_MAPPING_SUGGESTION_DETAIL_COUNT_RETRIEVAL_FAILED, e);
      }
   }

   public List<InstanceMappingSuggestionDetail> getInstanceMappingSuggestionDetailsByBatchAndSize (Long selColAedId,
            Long batchNum, Long batchSize) throws MappingException {
      try {
         return instanceMappingSuggestionDAO.getInstanceMappingSuggestionDetailsByBatchAndSize(selColAedId, batchNum,
                  batchSize);
      } catch (DataAccessException e) {
         throw new MappingException(SWIExceptionCodes.INSTANCE_MAPPING_SUGGESTION_DETAIL_RETRIEVAL_FAILED, e);
      }
   }

   public InstanceMappingSuggestion getInstanceMappingSuggestion (Long columnAEDId) throws MappingException {
      try {
         return instanceMappingSuggestionDAO.getInstanceMappingSuggestion(columnAEDId);
      } catch (DataAccessException e) {
         throw new MappingException(SWIExceptionCodes.INSTANCE_MAPPING_SUGGESTION_RETRIEVAL_FAILED, e);
      }
   }

   public InstanceMappingSuggestionDetail getInstanceMappingSuggestionDetailByInstanceDisplayName (
            String instanceDisplayName) throws MappingException {
      try {
         return instanceMappingSuggestionDAO
                  .getInstanceMappingSuggestionDetailByInstanceDisplayName(instanceDisplayName);
      } catch (DataAccessException e) {
         throw new MappingException(SWIExceptionCodes.INSTANCE_MAPPING_SUGGESTION_DETAIL_RETRIEVAL_FAILED, e);
      }
   }

   public void updateInstanceMappingSuggestionDetail (InstanceMappingSuggestionDetail instanceMappingSuggestionDetail)
            throws MappingException {
      try {
         instanceMappingSuggestionDAO.update(instanceMappingSuggestionDetail);
      } catch (DataAccessException e) {
         throw new MappingException(SWIExceptionCodes.INSTANCE_MAPPING_SUGGESTION_DETAIL_RETRIEVAL_FAILED, e);
      }
   }

}