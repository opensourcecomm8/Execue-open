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


package com.execue.swi.service.impl;

import java.util.List;

import com.execue.core.common.bean.entity.InstanceMappingSuggestion;
import com.execue.core.common.bean.entity.InstanceMappingSuggestionDetail;
import com.execue.swi.exception.MappingException;
import com.execue.swi.dataaccess.IInstanceMappingSuggestionDataAccessManager;
import com.execue.swi.service.IInstanceMappingSuggestionService;

public class InstanceMappingSuggestionServiceImpl implements IInstanceMappingSuggestionService {

   private IInstanceMappingSuggestionDataAccessManager instanceMappingSuggestionDataAccessManager;

   /**
    * @return the instanceMappingSuggestionDataAccessManager
    */
   public IInstanceMappingSuggestionDataAccessManager getInstanceMappingSuggestionDataAccessManager () {
      return instanceMappingSuggestionDataAccessManager;
   }

   /**
    * @param instanceMappingSuggestionDataAccessManager
    *           the instanceMappingSuggestionDataAccessManager to set
    */
   public void setInstanceMappingSuggestionDataAccessManager (
            IInstanceMappingSuggestionDataAccessManager instanceMappingSuggestionDataAccessManager) {
      this.instanceMappingSuggestionDataAccessManager = instanceMappingSuggestionDataAccessManager;
   }

   public void createInstanceMappingSuggestion (InstanceMappingSuggestion instanceMappingSuggestion)
            throws MappingException {
      instanceMappingSuggestionDataAccessManager.createInstanceMappingSuggestion(instanceMappingSuggestion);
   }

   public void createInstanceMappingSuggestionDetails (
            List<InstanceMappingSuggestionDetail> instanceMappingSuggestionDetails) throws MappingException {
      instanceMappingSuggestionDataAccessManager
               .createInstanceMappingSuggestionDetails(instanceMappingSuggestionDetails);
   }

   public List<InstanceMappingSuggestionDetail> getInstanceMappingSuggestionDetails (Long instanceMappingSuggestionId)
            throws MappingException {
      return instanceMappingSuggestionDataAccessManager
               .getInstanceMappingSuggestionDetails(instanceMappingSuggestionId);
   }

   public List<InstanceMappingSuggestionDetail> getInstanceMappingSuggestionDetailsByColumAEDId (Long columAEDId)
            throws MappingException {
      return instanceMappingSuggestionDataAccessManager.getInstanceMappingSuggestionDetailsByColumAEDId(columAEDId);
   }

   public void deleteInstanceMappingSuggestionDetails (List<Long> instanceMappingSuggestionDetailsIds)
            throws MappingException {
      instanceMappingSuggestionDataAccessManager
               .deleteInstanceMappingSuggestionDetails(instanceMappingSuggestionDetailsIds);
   }

   public void deleteInstanceMappingSuggestionByColumnAEDId (Long columAEDId) throws MappingException {
      instanceMappingSuggestionDataAccessManager.deleteInstanceMappingSuggestionByColumnAEDId(columAEDId);
   }

   public Long getInstanceMappingSuggestionDetailsCount (Long selColAedId) throws MappingException {
      return instanceMappingSuggestionDataAccessManager.getInstanceMappingSuggestionDetailsCount(selColAedId);
   }

   public List<InstanceMappingSuggestionDetail> getInstanceMappingSuggestionDetailsByBatchAndSize (Long selColAedId,
            Long batchNum, Long batchSize) throws MappingException {
      return instanceMappingSuggestionDataAccessManager.getInstanceMappingSuggestionDetailsByBatchAndSize(selColAedId,
               batchNum, batchSize);
   }

   public InstanceMappingSuggestion getInstanceMappingSuggestion (Long columnAEDId) throws MappingException {
      return instanceMappingSuggestionDataAccessManager.getInstanceMappingSuggestion(columnAEDId);
   }

   public InstanceMappingSuggestionDetail getInstanceMappingSuggestionDetailByInstanceDisplayName (
            String instanceDisplayName) throws MappingException {
      return instanceMappingSuggestionDataAccessManager
               .getInstanceMappingSuggestionDetailByInstanceDisplayName(instanceDisplayName);
   }

   public void updateInstanceMappingSuggestionDetail (InstanceMappingSuggestionDetail instanceMappingSuggestionDetail)
            throws MappingException {
      instanceMappingSuggestionDataAccessManager.updateInstanceMappingSuggestionDetail(instanceMappingSuggestionDetail);
   }
}