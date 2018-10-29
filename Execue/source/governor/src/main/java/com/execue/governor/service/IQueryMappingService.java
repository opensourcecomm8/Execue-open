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
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.governor.BusinessEntityTerm;
import com.execue.core.common.bean.governor.EntityMapping;
import com.execue.core.common.bean.governor.EntityMappingInfo;
import com.execue.governor.exception.QueryMappingException;

/**
 * This interface contain methods for extracting mappings from SWI and correct those mapping across assets and within
 * assets and then find out the list of unique assets which can answer the business query.
 * 
 * @author Vishay
 * @version 1.0
 * @since 10/02/09
 */
public interface IQueryMappingService {

   /**
    * This method will extract the EntityMapping for each businessTerm from SWI if it exists. It uses mapping service to
    * get List<Mapping> mappings between businessEntityTerm and List<AssetEntityDefintions>(list because one
    * businessEntityTerm can have many mappings to SDX world). It will populate the EntityMapping object for each
    * businessEntityTerm and return the list of them.
    * 
    * @param businessTerms
    * @return entityMappings
    * @throws QueryMappingException
    */
   public List<EntityMapping> extractEntityMappings (Set<BusinessEntityTerm> businessEntityTerms, Long modelId)
            throws QueryMappingException;

   public EntityMapping extractBaseEntityMapping (BusinessEntityTerm businessEntityTerm) throws QueryMappingException;

   /**
    * The unwanted asset entities will be dropped here. The following can be the criteria - If the asset entities are a
    * mix of primary and foreign keys, then choose the asset entity that is a primary key. Choosing the appropriate
    * asset entity for a business entity is also influenced by the query section type that the business entity belongs
    * to. For example, Sales may be present in the SELECT and GROUP BY sections of the business query. When an asset is
    * chosen, it may or may not contain the Sales as both a dimension and measure at the same time. If it contains both
    * (like a cube), then the info has to be maintained as two different entity mappings for the same business term. If
    * it is ID(we can find this from KDXDataType) then pick the ID ( if it is not primary or foriegn key)). If all out
    * filters fail to filter we will pick them all.
    * 
    * @param entityMappings
    * @param entityMappingInfo
    * @return assets - unique assets which can answer this business query
    * @throws QueryMappingException
    */
   public Set<Asset> correctMappings (List<EntityMapping> entityMappings, List<EntityMappingInfo> entityMappingInfo,
            BusinessQuery businessQuery) throws QueryMappingException;

   public List<EntityMapping> extractEntityMappings (Set<BusinessEntityTerm> businessEntityTerms, Long modelId,
            List<Long> assetIds) throws QueryMappingException;

   public EntityMapping extractBaseEntityMapping (BusinessEntityTerm businessEntityTerm, List<Long> assetIds)
            throws QueryMappingException;
}
