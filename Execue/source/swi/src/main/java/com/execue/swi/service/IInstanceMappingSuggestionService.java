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


package com.execue.swi.service;

import java.util.List;

import com.execue.core.common.bean.entity.InstanceMappingSuggestion;
import com.execue.core.common.bean.entity.InstanceMappingSuggestionDetail;
import com.execue.swi.exception.MappingException;

public interface IInstanceMappingSuggestionService {

   public void createInstanceMappingSuggestion (InstanceMappingSuggestion instanceMappingSuggestion)
            throws MappingException;

   public void createInstanceMappingSuggestionDetails (
            List<InstanceMappingSuggestionDetail> instanceMappingSuggestionDetails) throws MappingException;

   public InstanceMappingSuggestion getInstanceMappingSuggestion (Long columnAEDId) throws MappingException;

   public List<InstanceMappingSuggestionDetail> getInstanceMappingSuggestionDetails (Long instanceMappingSuggestionId)
            throws MappingException;

   public List<InstanceMappingSuggestionDetail> getInstanceMappingSuggestionDetailsByColumAEDId (Long columAEDId)
            throws MappingException;

   public void deleteInstanceMappingSuggestionDetails (List<Long> instanceMappingSuggestionDetailsIds)
            throws MappingException;

   public void deleteInstanceMappingSuggestionByColumnAEDId (Long columAEDId) throws MappingException;

   public Long getInstanceMappingSuggestionDetailsCount (Long selColAedId) throws MappingException;

   public InstanceMappingSuggestionDetail getInstanceMappingSuggestionDetailByInstanceDisplayName (
            String instanceDisplayName) throws MappingException;

   public void updateInstanceMappingSuggestionDetail (InstanceMappingSuggestionDetail instanceMappingSuggestionDetail)
            throws MappingException;

   public List<InstanceMappingSuggestionDetail> getInstanceMappingSuggestionDetailsByBatchAndSize (Long selColAedId,
            Long batchNum, Long batchSize) throws MappingException;

}
