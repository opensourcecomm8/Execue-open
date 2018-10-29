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


package com.execue.handler.swi.mappings;

import java.util.List;

import com.execue.core.common.bean.entity.DefaultMetric;
import com.execue.core.exception.ExeCueException;
import com.execue.handler.IHandler;
import com.execue.handler.bean.mapping.AssetColumnMember;
import com.execue.handler.bean.mapping.AssetTable;
import com.execue.handler.bean.mapping.ConceptAssetMapping;
import com.execue.handler.bean.mapping.ConceptMapping;
import com.execue.handler.bean.mapping.InstanceAssetMapping;
import com.execue.handler.bean.mapping.InstanceMapping;
import com.execue.handler.bean.mapping.InstanceMappingsPageProvider;
import com.execue.handler.bean.mapping.MappableBusinessTerm;
import com.execue.handler.bean.mapping.MappableInstanceTerm;
import com.execue.handler.bean.mapping.MappingsPageProvider;
import com.execue.handler.bean.mapping.SaveMapping;
import com.execue.handler.bean.mapping.TermInfo;

public interface IMappingServiceHandler extends IHandler {

   public ConceptMapping saveConceptMappings (Long modelId, List<SaveMapping> mappings,
            MappingsPageProvider mappingsPageProvider, InstanceMappingsPageProvider instanceMappingsProvider)
            throws ExeCueException;

   public List<ConceptAssetMapping> showExistingConceptMappings (Long assetId) throws ExeCueException;

   public List<AssetTable> getAssetTables (Long assetId) throws ExeCueException;

   public MappableBusinessTerm showConcepts (Long modelId) throws ExeCueException;

   public List<TermInfo> suggestConcepts (Long modelId, String searchString) throws ExeCueException;

   public List<TermInfo> suggestInstances (Long modelId, Long conceptBEDId, String searchString) throws ExeCueException;

   public ConceptMapping suggestConceptMappingsForSelectedColumns (Long modelId, List<Long> selColAedIds,
            List<Long> selConBedIds, MappingsPageProvider mappingsPageProvider) throws ExeCueException;

   public List<InstanceAssetMapping> showExistingInstanceMappings (Long selColAedId, Long conBedId)
            throws ExeCueException;

   public InstanceMapping suggestInstanceMappingsForColumn (Long modelId, Long selColAedId, Long conBedId,
            InstanceMappingsPageProvider provider) throws ExeCueException;

   public AssetColumnMember getColumnMembers (Long selColAedId) throws ExeCueException;

   public MappableInstanceTerm showInstances (Long modelId, Long conBedId) throws ExeCueException;

   public InstanceMapping saveInstanceMappings (Long modelId, Long selColAedId, Long selConBedId,
            List<SaveMapping> saveMappings, InstanceMappingsPageProvider mappingsPageProvider) throws ExeCueException;

   public InstanceMapping suggestMemberMappings (Long modelId, Long selColAedId, Long selConBedId)
            throws ExeCueException;

   public InstanceMapping validateInstanceMappings (Long conceptBEDId, List<SaveMapping> saveMappings)
            throws ExeCueException;

   public InstanceMapping saveMemberMappings (int pageNumber, Long modelId, Long selColAedId, Long selConBedId,
            InstanceMapping instanceMapping, boolean saveAll) throws ExeCueException;

   public InstanceMapping retrieveMemberMappingsByPage (Long columnAEDId, int pageNumber, String mappingFilter)
            throws ExeCueException;

   public Long getUnmappedMemberCount (Long columnAedId) throws ExeCueException;

   public Long suggestMemberMappingsByJob (Long modelId, Long assetId, Long selColAedId, Long selConBedId)
            throws ExeCueException;

   public Long getInstanceMappingSuggestionDetailsCount (Long columnAedId) throws ExeCueException;

   public Long saveMemberMappingsByJob (Long modelId, Long assetId, Long selColAedId, Long selConBedId)
            throws ExeCueException;

   public Long getMaxAllowedSuggestions ();

   public MappableInstanceTerm retrieveInstancesByPage (Long conBedId, Long pageNumber, Long pazeSize)
            throws ExeCueException;

   public AssetColumnMember getColumnMembersByPage (Long selColAedId, Long pageNumber, Long pageSize)
            throws ExeCueException;

   public List<DefaultMetric> getAllPossibleDefaultMetrics (Long assetId, Long tableId) throws ExeCueException;

   public List<DefaultMetric> getValidExistingDefaultMetrics (Long tableId) throws ExeCueException;

   public List<DefaultMetric> getInValidExistingDefaultMetrics (Long tableId) throws ExeCueException;

   public int getMappingPageSize ();

}