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


package com.execue.governor.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.BusinessQuery;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.governor.BusinessEntityTerm;
import com.execue.core.common.bean.governor.EntityMapping;
import com.execue.core.common.bean.governor.EntityMappingInfo;
import com.execue.core.common.bean.governor.LightAssetEntityDefinitionInfo;
import com.execue.governor.exception.QueryMappingException;
import com.execue.governor.service.IQueryMappingService;
import com.execue.swi.exception.MappingException;
import com.execue.swi.exception.SDXException;
import com.execue.swi.exception.SWIExceptionCodes;
import com.execue.swi.service.IMappingRetrievalService;
import com.execue.swi.service.ISDXRetrievalService;

/**
 * This class contain methods for extracting mappings from SWI and correct those mapping across assets and within assets
 * and then find out the list of unique assets which can answer the business query.
 * 
 * @author Vishay
 * @version 1.0
 * @since 10/02/09
 */
public class QueryMappingServiceImpl implements IQueryMappingService {

   private static final Logger      logger = Logger.getLogger(QueryMappingServiceImpl.class);
   private IMappingRetrievalService mappingRetrievalService;
   private ISDXRetrievalService     sdxRetrievalService;

   /**
    * The unwanted asset entities will be dropped here. The following can be the criteria - If the asset entities are a
    * mix of primary and foreign keys, then choose the asset entity that is a primary key. Choosing the appropriate
    * asset entity for a business entity is also influenced by the query section type that the business entity belongs
    * to. For example, Sales may be present in the SELECT and GROUP BY sections of the business query. When an asset is
    * chosen, it may or may not contain the Sales as both a dimension and measure at the same time. If it contains both
    * (like a cube), then the info has to be maintained as two different entity mappings for the same business term. If
    * it is ID(we can find this from KDXDataType) then pick the ID ( if it is not primary or foreign key)). If all out
    * filters fail to filter we will pick them all.
    * 
    * @param entityMappings
    * @return assets - unique assets which can answer this business query
    * @throws QueryMappingException
    */
   public Set<Asset> correctMappings (List<EntityMapping> entityMappings, List<EntityMappingInfo> entityMappingInfo,
            BusinessQuery businessQuery) throws QueryMappingException {
      logger.debug("Inside correctMappings method");
      logger.debug("Got entityMappings : " + entityMappings);
      logger.debug("Got BusinessQuery : " + businessQuery);
      Set<Asset> activeAssets = new HashSet<Asset>();
      try {
         // Set contains list of unique assets that can answer the business query
         Set<Long> assets = new HashSet<Long>();
         for (EntityMapping entityMapping : entityMappings) {
            // maintain a map which contains the assets and their corresponding AED's for current entityMapping
            // internally we can say this as filtering across assets
            Map<Long, List<LightAssetEntityDefinitionInfo>> acrossAssetDefinitionsMap = new HashMap<Long, List<LightAssetEntityDefinitionInfo>>();
            for (LightAssetEntityDefinitionInfo assetEntityDefinition : entityMapping.getLightAssetEntityDefinitions()) {
               List<LightAssetEntityDefinitionInfo> assetEntityDefinitions = null;
               // if the current asset is already exiting in the map as a key, then we will append new AED to existing
               // list
               if (acrossAssetDefinitionsMap.containsKey(assetEntityDefinition.getAssetId())) {
                  assetEntityDefinitions = acrossAssetDefinitionsMap.get(assetEntityDefinition.getAssetId());
               }
               // if the asset as a key doesn't exist then we will create a new list
               else {
                  assetEntityDefinitions = new ArrayList<LightAssetEntityDefinitionInfo>();
               }
               assetEntityDefinitions.add(assetEntityDefinition);
               acrossAssetDefinitionsMap.put(assetEntityDefinition.getAssetId(), assetEntityDefinitions);
            }
            EntityMappingInfo newEntityMappingInfo = new EntityMappingInfo();
            newEntityMappingInfo.setBusinessEntityTerm(entityMapping.getBusinessEntityTerm());
            newEntityMappingInfo.setAssetBasedLightAssetEntityDefinitions(acrossAssetDefinitionsMap);
            entityMappingInfo.add(newEntityMappingInfo);
            assets.addAll(acrossAssetDefinitionsMap.keySet());
         }
         for (Long assetId : assets) {
            Asset asset = getSdxRetrievalService().getAsset(assetId);
            activeAssets.add(asset);
         }
      } catch (SDXException sdxException) {
         throw new QueryMappingException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, sdxException);
      }
      return activeAssets;
   }

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
            throws QueryMappingException {
      logger.debug("Inside extractEntityMapping Method");
      logger.debug("Got businessEntityTerms : " + businessEntityTerms.size());
      List<EntityMapping> entityMappingList = new ArrayList<EntityMapping>();
      try {
         for (BusinessEntityTerm businessEntityTerm : businessEntityTerms) {
            logger.debug("Calling the getAssetEntities() method to get mapping for businessEntityTerm : "
                     + businessEntityTerm);

            List<LightAssetEntityDefinitionInfo> assetEntityDefinitions = null;
            if (businessEntityTerm.isBaseGroupEntity()) {
               assetEntityDefinitions = getMappingRetrievalService().getAssetEntitiesForAllAssetsInBaseGroup(
                        businessEntityTerm.getBusinessEntityDefinitionId(), modelId);
            } else {
               assetEntityDefinitions = getMappingRetrievalService().getAssetEntitiesForAllAssets(
                        businessEntityTerm.getBusinessEntityDefinitionId(), modelId);
            }
            if (assetEntityDefinitions != null && assetEntityDefinitions.size() > 0) {
               logger.debug("Found Mapping for businessEntityTerm" + businessEntityTerm.toString());
               EntityMapping entityMapping = new EntityMapping();
               entityMapping.setBusinessEntityTerm(businessEntityTerm);
               entityMapping.setLightAssetEntityDefinitions(assetEntityDefinitions);
               entityMappingList.add(entityMapping);
            }
         }
      } catch (MappingException e) {
         throw new QueryMappingException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
      return entityMappingList;
   }

   public EntityMapping extractBaseEntityMapping (BusinessEntityTerm businessEntityTerm) throws QueryMappingException {
      // TODO Auto-generated method stub
      return null;
   }

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
   public List<EntityMapping> extractEntityMappings (Set<BusinessEntityTerm> businessEntityTerms, Long modelId,
            List<Long> assetIds) throws QueryMappingException {
      logger.debug("Inside extractEntityMapping Method");
      logger.debug("Got businessEntityTerms : " + businessEntityTerms.size());
      List<EntityMapping> entityMappingList = new ArrayList<EntityMapping>();
      try {
         for (BusinessEntityTerm businessEntityTerm : businessEntityTerms) {
            logger.debug("Calling the getAssetEntities() method to get mapping for businessEntityTerm : "
                     + businessEntityTerm);

            // NK: update the call to get the assetEntityDefinitions list for the given answerable assets and mapping to
            // 'P' and 'M'
            List<LightAssetEntityDefinitionInfo> assetEntityDefinitions = null;
            if (businessEntityTerm.isBaseGroupEntity()) {
               assetEntityDefinitions = getMappingRetrievalService().getPrimaryAssetEntitiesForAssetsInBaseGroup(
                        businessEntityTerm.getBusinessEntityDefinitionId(), modelId, assetIds);
            } else {
               assetEntityDefinitions = getMappingRetrievalService().getPrimaryAssetEntitiesForAssets(
                        businessEntityTerm.getBusinessEntityDefinitionId(), modelId, assetIds);
            }
            if (assetEntityDefinitions != null && assetEntityDefinitions.size() > 0) {
               logger.debug("Found Mapping for businessEntityTerm" + businessEntityTerm.toString());
               EntityMapping entityMapping = new EntityMapping();
               entityMapping.setBusinessEntityTerm(businessEntityTerm);
               entityMapping.setLightAssetEntityDefinitions(assetEntityDefinitions);
               entityMappingList.add(entityMapping);
            }
         }
      } catch (MappingException e) {
         throw new QueryMappingException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
      return entityMappingList;
   }

   public EntityMapping extractBaseEntityMapping (BusinessEntityTerm businessEntityTerm, List<Long> assetIds)
            throws QueryMappingException {
      try {
         List<LightAssetEntityDefinitionInfo> assetEntityDefinitions = getMappingRetrievalService()
                  .getPrimaryAssetEntitiesForAssets(businessEntityTerm.getBusinessEntityDefinitionId(), null, assetIds);

         if (assetEntityDefinitions != null && assetEntityDefinitions.size() > 0) {
            logger.debug("Found Mapping for businessEntityTerm" + businessEntityTerm.toString());
            EntityMapping entityMapping = new EntityMapping();
            entityMapping.setBusinessEntityTerm(businessEntityTerm);
            entityMapping.setLightAssetEntityDefinitions(assetEntityDefinitions);

         }
      } catch (MappingException e) {
         throw new QueryMappingException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
      return null;
   }

   public ISDXRetrievalService getSdxRetrievalService () {
      return sdxRetrievalService;
   }

   public void setSdxRetrievalService (ISDXRetrievalService sdxRetrievalService) {
      this.sdxRetrievalService = sdxRetrievalService;
   }

   public IMappingRetrievalService getMappingRetrievalService () {
      return mappingRetrievalService;
   }

   public void setMappingRetrievalService (IMappingRetrievalService mappingRetrievalService) {
      this.mappingRetrievalService = mappingRetrievalService;
   }

}
