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


package com.execue.platform.swi;

import java.util.List;

import com.execue.core.common.bean.batchMaintenance.InstanceAbsorptionContext;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.InstanceMappingSuggestion;
import com.execue.core.common.bean.entity.InstanceMappingSuggestionDetail;
import com.execue.core.common.bean.entity.Membr;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.common.bean.entity.Tabl;
import com.execue.core.common.bean.mapping.AssetMapping;
import com.execue.core.common.bean.mapping.ColumnMapping;
import com.execue.core.common.bean.mapping.MemberMapping;
import com.execue.core.common.bean.mapping.TableMapping;
import com.execue.swi.exception.SDX2KDXMappingException;

public interface ISDX2KDXMappingService {

   public AssetMapping mapSDX2KDXForAsset (Asset asset, boolean mapMembers, boolean mapMemberAsConcept, boolean create,
            Long startCounter, Model model) throws SDX2KDXMappingException;

   public TableMapping mapSDX2KDXForTable (Asset asset, Tabl table, boolean mapMembers, boolean mapMemberAsConcept,
            boolean create, Long startCounter, Model model) throws SDX2KDXMappingException;

   public ColumnMapping mapSDX2KDXForColumn (Asset asset, Tabl table, Colum column, boolean mapMembers,
            boolean mapMemberAsConcept, boolean create, Long startCounter, Model model) throws SDX2KDXMappingException;

   public MemberMapping mapSDX2KDXForMember (Asset asset, Tabl table, Colum column, Membr member, int memberIndex,
            boolean mapMemberAsConcept, boolean create, Long startCounter, Model model) throws SDX2KDXMappingException;

   public ColumnMapping mapSDX2KDXForColumnMembers (Asset asset, Tabl table, Colum column, List<Membr> members,
            BusinessEntityDefinition conceptDED, boolean mapMemberAsConcept, boolean create, Long startCounter,
            Model model) throws SDX2KDXMappingException;

   public Long getUnmappedMemberCount (Long columnAedId) throws SDX2KDXMappingException;

   public void createBatchProcess (InstanceAbsorptionContext instanceAbsorptionContext) throws SDX2KDXMappingException;

   public void deleteBatchProcess (InstanceAbsorptionContext instanceAbsorptionContext) throws SDX2KDXMappingException;

   public void saveInstanceMappingSuggestions (InstanceAbsorptionContext instanceAbsorptionContext)
            throws SDX2KDXMappingException;

   public String generateInstanceMappingSuggestions (InstanceAbsorptionContext instanceAbsorptionContext)
            throws SDX2KDXMappingException;

   public List<Long> mapMembersForAssetSyncUpProcess (Asset asset, Tabl table, Colum column, List<Membr> members,
            BusinessEntityDefinition conceptDED, Model model) throws SDX2KDXMappingException;

   public List<Long> mapColumnsForAssetSyncUpProcess (Asset asset, Tabl table, List<Colum> columns, Model model)
            throws SDX2KDXMappingException;

   public void saveBatchMappings (InstanceMappingSuggestion instanceMappingSuggestion,
            List<InstanceMappingSuggestionDetail> suggestionsBatch, Long modelId) throws SDX2KDXMappingException;

}