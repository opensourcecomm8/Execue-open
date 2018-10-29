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


package com.execue.governor.service;

import java.util.List;
import java.util.Set;

import com.execue.core.common.bean.BusinessQuery;
import com.execue.core.common.bean.BusinessTerm;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.governor.BusinessEntityTerm;
import com.execue.core.common.bean.governor.EntityMappingInfo;
import com.execue.core.common.bean.governor.StructuredQuery;
import com.execue.governor.exception.GovernorException;

/**
 * This service populates the structured queries for business query
 * 
 * @author Vishay Gupta
 * @version 1.0
 * @since 24/08/10
 */
public interface IStructuredQueryPopulationService {

   public List<StructuredQuery> populateStructuredQueries (Set<Asset> answerAssets, BusinessQuery businessQuery,
            List<EntityMappingInfo> entityMappingInfo) throws GovernorException;

   public BusinessTerm createScalingFactorBusinessTerm () throws GovernorException;

   public void loadPopulatedBusinessEntityTerms (Set<BusinessEntityTerm> businessEntityTerms) throws GovernorException;

   public void correctAndLoadEntityMappingInfo (List<EntityMappingInfo> entityMappingInfo, Set<Asset> activeAnswerAssets)
            throws GovernorException;

   public void correctAndLoadEntityMappingInfo2 (List<EntityMappingInfo> entityMappingInfo, Set<Asset> answerAssets)
            throws GovernorException;

}
