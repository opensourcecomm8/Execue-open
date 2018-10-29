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


package com.execue.governor.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.BusinessCondition;
import com.execue.core.common.bean.BusinessOrderClause;
import com.execue.core.common.bean.BusinessQuery;
import com.execue.core.common.bean.BusinessTerm;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.AssetEntityDefinition;
import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.Mapping;
import com.execue.core.common.bean.entity.Tabl;
import com.execue.core.common.bean.governor.AssetEntityTerm;
import com.execue.core.common.bean.governor.BusinessAssetTerm;
import com.execue.core.common.bean.governor.BusinessEntityTerm;
import com.execue.core.common.bean.governor.EntityMapping;
import com.execue.core.common.bean.governor.EntityMappingInfo;
import com.execue.core.common.bean.governor.LightAssetEntityDefinitionInfo;
import com.execue.core.common.bean.governor.StructuredCondition;
import com.execue.core.common.bean.governor.StructuredOrderClause;
import com.execue.core.common.bean.governor.StructuredQuery;
import com.execue.core.common.type.AssetEntityType;
import com.execue.core.common.type.BusinessEntityType;
import com.execue.core.common.type.OperandType;
import com.execue.core.common.util.ExecueBeanCloneUtil;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.governor.exception.GovernorException;
import com.execue.governor.exception.GovernorExceptionCodes;

/**
 * This class contains helper methods for governor process
 * 
 * @author Vishay
 * @version 1.0
 * @since 23/08/2010
 */
public class GovernorServiceHelper {

   private static final Logger logger = Logger.getLogger(GovernorServiceHelper.class);

   /**
    * This method will be used to identify the unique BusinessEntityTerms inside the business query. There will be
    * different businessEntityTerms across multiple sections of the business query and some might be same. We will try
    * to find unique terms so that we can reduce the number of calls to database in order to find their SDX mappings. If
    * business query contains subquery, we will use recursion to find the businessEntityTerms for subquery first and
    * then add those businessEntity terms to the main query businessEntity terms.
    * 
    * @param businessQuery
    * @return Set<BusinessEntityTerm> businessEntityTerms
    */
   public static Set<BusinessEntityTerm> populateBusinessEntityTerms (BusinessQuery businessQuery)
            throws GovernorException {
      logger.debug("Inside PopulateBusinessEntityTerms Method");
      logger.debug("Got Business Query " + businessQuery);
      Set<BusinessEntityTerm> businessEntityTerms = new HashSet<BusinessEntityTerm>();
      businessEntityTerms.addAll(getBusinessEntityTerms(businessQuery.getMetrics()));
      businessEntityTerms.addAll(getConditionalBusinessEntityTerms(businessQuery.getConditions()));
      businessEntityTerms.addAll(getBusinessEntityTerms(businessQuery.getSummarizations()));
      businessEntityTerms.addAll(getOrderingBusinessEntityTerms(businessQuery.getOrderClauses()));
      businessEntityTerms.addAll(getConditionalBusinessEntityTerms(businessQuery.getHavingClauses()));
      businessEntityTerms.addAll(getBusinessEntityTerms(businessQuery.getPopulations()));
      if (businessQuery.getCohort() != null) {
         logger.debug("Populating the terms from Cohort Query");
         businessEntityTerms.addAll(populateBusinessEntityTerms(businessQuery.getCohort()));
      }
      if (businessQuery.getTopBottom() != null) {
         logger.debug("Populating the term from limit entity");
         businessEntityTerms.add(businessQuery.getTopBottom().getBusinessTerm().getBusinessEntityTerm());
      }
      logger.debug("Got Unique BusinessEntityTerms " + businessEntityTerms.size());
      return businessEntityTerms;
   }

   /**
    * This method will be used to identify the unique BusinessEntityTerms inside the business query. There will be
    * different businessEntityTerms across multiple sections of the business query and some might be same. We will try
    * to find unique terms so that we can reduce the number of calls to database in order to find their SDX mappings. If
    * business query contains subquery, we will use recursion to find the businessEntityTerms for subquery first and
    * then add those businessEntity terms to the main query businessEntity terms.
    * 
    * @param businessQuery
    * @return Set<BusinessEntityTerm> businessEntityIds
    */
   public static Set<Long> populateBusinessEntityIds (BusinessQuery businessQuery) throws GovernorException {
      logger.debug("Inside PopulateBusinessEntityTerms Method");
      logger.debug("Got Business Query " + businessQuery);
      Set<Long> businessEntityIds = new HashSet<Long>();
      businessEntityIds.addAll(getBusinessEntityIds(businessQuery.getMetrics()));
      businessEntityIds.addAll(getConditionalBusinessEntityIds(businessQuery.getConditions()));
      businessEntityIds.addAll(getBusinessEntityIds(businessQuery.getSummarizations()));
      businessEntityIds.addAll(getOrderingBusinessEntityIds(businessQuery.getOrderClauses()));
      businessEntityIds.addAll(getConditionalBusinessEntityIds(businessQuery.getHavingClauses()));
      businessEntityIds.addAll(getBusinessEntityIds(businessQuery.getPopulations()));
      if (businessQuery.getCohort() != null) {
         logger.debug("Populating the terms from Cohort Query");
         businessEntityIds.addAll(populateBusinessEntityIds(businessQuery.getCohort()));
      }
      if (businessQuery.getTopBottom() != null) {
         logger.debug("Populating the term from limit entity");
         businessEntityIds.add(businessQuery.getTopBottom().getBusinessTerm().getBusinessEntityTerm()
                  .getBusinessEntityDefinitionId());
      }
      logger.debug("Got Unique BusinessEntityTerms " + businessEntityIds.size());
      return businessEntityIds;
   }

   /**
    * This method will be used to identify the unique BusinessEntityTerms inside the structured query. There will be
    * different businessEntityTerms across multiple sections of the business query and some might be same. If business
    * query contains subquery, we will use recursion to find the businessEntityTerms for subquery first and then add
    * those businessEntity terms to the main query businessEntity terms.This method will only get from summarizations,
    * populations because we are looking for defaults added to structured query which doesn't exist in business query
    * 
    * @param structuredQuery
    * @return Set<BusinessEntityTerm> businessEntityIds
    */
   public static Set<Long> populateBusinessEntityIds (StructuredQuery structuredQuery) throws GovernorException {
      logger.debug("Inside PopulateBusinessEntityTerms Method");
      logger.debug("Got Structured Query " + structuredQuery);
      Set<Long> businessEntityIds = new HashSet<Long>();
      businessEntityIds.addAll(getBusinessEntityIdsSet(structuredQuery.getMetrics()));
      businessEntityIds.addAll(getBusinessEntityIdsSet(structuredQuery.getSummarizations()));
      businessEntityIds.addAll(getBusinessEntityIdsSet(structuredQuery.getPopulations()));
      businessEntityIds.addAll(getConditionalBusinessEntityIdsSet(structuredQuery.getConditions()));
      businessEntityIds.addAll(getConditionalBusinessEntityIdsSet(structuredQuery.getHavingClauses()));
      if (ExecueCoreUtil.isCollectionNotEmpty(structuredQuery.getOrderClauses())) {
         for (StructuredOrderClause structuredOrderClause : structuredQuery.getOrderClauses()) {
            businessEntityIds.add(structuredOrderClause.getBusinessAssetTerm().getBusinessTerm()
                     .getBusinessEntityTerm().getBusinessEntityDefinitionId());
         }
      }
      if (structuredQuery.getCohort() != null) {
         businessEntityIds.addAll(populateBusinessEntityIds(structuredQuery.getCohort()));
      }
      if (structuredQuery.getTopBottom() != null) {
         businessEntityIds.add(structuredQuery.getTopBottom().getBusinessAssetTerm().getBusinessTerm()
                  .getBusinessEntityTerm().getBusinessEntityDefinitionId());
      }
      logger.debug("Got Unique BusinessEntityTerms " + businessEntityIds.size());
      return businessEntityIds;
   }

   /**
    * This method will be used to identify the unique BusinessEntityTerms inside the structured query. There will be
    * different assetEntityTerms across multiple sections of the business query and some might be same. If business
    * query contains subquery, we will use recursion to find the businessEntityTerms for subquery first and then add
    * those businessEntity terms to the main query businessEntity terms.This method will only get from summarizations,
    * populations and where conditions because we are looking for defaults added to structured query which doesn't exist
    * in business query
    * 
    * @param structuredQuery
    * @return Set<AssetEntityTerm> assetEntityIds
    */
   public static Set<Long> populateAssetEntityIds (StructuredQuery structuredQuery) throws GovernorException {
      logger.debug("Inside populateAssetEntityTerms Method");
      logger.debug("Got Structured Query " + structuredQuery);
      Set<Long> assetEntityIds = new HashSet<Long>();
      assetEntityIds.addAll(getAssetEntityIdsSet(structuredQuery.getMetrics()));
      assetEntityIds.addAll(getAssetEntityIdsSet(structuredQuery.getSummarizations()));
      assetEntityIds.addAll(getAssetEntityIdsSet(structuredQuery.getPopulations()));
      assetEntityIds.addAll(getConditionalAssetEntityIds(structuredQuery.getConditions()));
      assetEntityIds.addAll(getConditionalAssetEntityIds(structuredQuery.getHavingClauses()));
      if (ExecueCoreUtil.isCollectionNotEmpty(structuredQuery.getOrderClauses())) {
         for (StructuredOrderClause structuredOrderClause : structuredQuery.getOrderClauses()) {
            assetEntityIds.add(structuredOrderClause.getBusinessAssetTerm().getAssetEntityTerm()
                     .getAssetEntityDefinitionId());
         }
      }
      if (structuredQuery.getCohort() != null) {
         assetEntityIds.addAll(populateAssetEntityIds(structuredQuery.getCohort()));
      }
      if (structuredQuery.getTopBottom() != null) {
         assetEntityIds.add(structuredQuery.getTopBottom().getBusinessAssetTerm().getAssetEntityTerm()
                  .getAssetEntityDefinitionId());
      }
      logger.debug("Got Unique AssetEntityTerms " + assetEntityIds.size());
      return assetEntityIds;
   }

   /**
    * This method is helper method to populateBusinessEntityTerms() in order to get unique businessEntityTerms for
    * businessOrderClause section of businessQuery
    * 
    * @param businessOrderClauses
    * @return businessEntityTerms
    */
   private static Set<BusinessEntityTerm> getOrderingBusinessEntityTerms (List<BusinessOrderClause> businessOrderClauses) {
      logger.debug("Inside getOrderingBusinessEntityTerms method");
      logger.debug("Got businessOrderClauses " + businessOrderClauses);
      Set<BusinessEntityTerm> businessEntityTerms = new HashSet<BusinessEntityTerm>();
      if (ExecueCoreUtil.isCollectionNotEmpty(businessOrderClauses)) {
         logger.debug("Populating Unique BusinessEntityTerms from BusinessTerms");
         for (BusinessOrderClause businessOrderClause : businessOrderClauses) {
            if (businessOrderClause.getBusinessTerm() != null) {
               // Set will check on basis of hashcode and equals defined in
               // BusinessEntityTerm
               businessEntityTerms.add(businessOrderClause.getBusinessTerm().getBusinessEntityTerm());
            }
         }
      }
      return businessEntityTerms;
   }

   /**
    * This method is helper method to populateBusinessEntity ids in order to get unique businessEntityTerms for
    * businessOrderClause section of businessQuery
    * 
    * @param businessOrderClauses
    * @return businessEntityIds
    */
   private static Set<Long> getOrderingBusinessEntityIds (List<BusinessOrderClause> businessOrderClauses) {
      logger.debug("Inside getOrderingBusinessEntityTerms method");
      logger.debug("Got businessOrderClauses " + businessOrderClauses);
      Set<Long> businessEntityIds = new HashSet<Long>();
      if (ExecueCoreUtil.isCollectionNotEmpty(businessOrderClauses)) {
         logger.debug("Populating Unique BusinessEntityTerms from BusinessTerms");
         for (BusinessOrderClause businessOrderClause : businessOrderClauses) {
            if (businessOrderClause.getBusinessTerm() != null) {
               // Set will check on basis of hashcode and equals defined in
               // BusinessEntityTerm
               businessEntityIds.add(businessOrderClause.getBusinessTerm().getBusinessEntityTerm()
                        .getBusinessEntityDefinitionId());
            }
         }
      }
      return businessEntityIds;
   }

   /**
    * This method is helper method to populateBusinessEntityTerms() in order to get unique businessEntityTerms for
    * metrics and summarizations sections of businessQuery
    * 
    * @param businessTerms
    * @return businessEntityTerms
    */
   private static Set<BusinessEntityTerm> getBusinessEntityTerms (List<BusinessTerm> businessTerms) {
      logger.debug("Inside getBusinessEntityTerms method");
      logger.debug("Got Business Terms : " + businessTerms);
      Set<BusinessEntityTerm> businessEntityTerms = new HashSet<BusinessEntityTerm>();
      if (ExecueCoreUtil.isCollectionNotEmpty(businessTerms)) {
         logger.debug("Populating Unique BusinessEntityTerms from BusinessTerms");
         for (BusinessTerm businessTerm : businessTerms) {
            // Set will check on basis of hashCode and equals defined in
            // BusinessEntityTerm
            businessEntityTerms.add(businessTerm.getBusinessEntityTerm());
         }
      }
      return businessEntityTerms;
   }

   /**
    * This method is helper method to populateBusinessEntity Ids in order to get unique businessEntityTerms for metrics
    * and summarizations sections of businessQuery
    * 
    * @param businessTerms
    * @return businessEntityIds
    */
   private static Set<Long> getBusinessEntityIds (List<BusinessTerm> businessTerms) {
      logger.debug("Inside getBusinessEntityTerms method");
      logger.debug("Got Business Terms : " + businessTerms);
      Set<Long> businessEntityIds = new HashSet<Long>();
      if (ExecueCoreUtil.isCollectionNotEmpty(businessTerms)) {
         logger.debug("Populating Unique BusinessEntityTerms from BusinessTerms");
         for (BusinessTerm businessTerm : businessTerms) {
            // Set will check on basis of hashCode and equals defined in
            // BusinessEntityTerm
            businessEntityIds.add(businessTerm.getBusinessEntityTerm().getBusinessEntityDefinitionId());
            // set profile id if exists
            if (businessTerm.getProfileDomainEntityDefinitionId() != null) {
               businessEntityIds.add(businessTerm.getProfileDomainEntityDefinitionId());
            }
         }
      }
      return businessEntityIds;
   }

   /**
    * This method is helper method to get asset entity terms from businessAssetTerms
    * 
    * @param businessAssetTerms
    * @return assetEntityIds
    */
   private static Set<Long> getAssetEntityIdsSet (List<BusinessAssetTerm> businessAssetTerms) throws GovernorException {
      Set<Long> assetEntityIds = new HashSet<Long>();
      if (ExecueCoreUtil.isCollectionNotEmpty(businessAssetTerms)) {
         for (BusinessAssetTerm businessAssetTerm : businessAssetTerms) {
            assetEntityIds.add(businessAssetTerm.getAssetEntityTerm().getAssetEntityDefinitionId());
         }
      }
      return assetEntityIds;
   }

   /**
    * This method is helper method to get business entity terms from businessAssetTerms
    * 
    * @param businessAssetTerms
    * @return businessEntityIds
    */
   private static Set<Long> getBusinessEntityIdsSet (List<BusinessAssetTerm> businessAssetTerms)
            throws GovernorException {
      Set<Long> businessEntityIds = new HashSet<Long>();
      if (ExecueCoreUtil.isCollectionNotEmpty(businessAssetTerms)) {
         for (BusinessAssetTerm businessAssetTerm : businessAssetTerms) {
            businessEntityIds.add(businessAssetTerm.getBusinessTerm().getBusinessEntityTerm()
                     .getBusinessEntityDefinitionId());
         }
      }
      return businessEntityIds;
   }

   private static Set<Long> getConditionalBusinessEntityIdsSet (List<StructuredCondition> structuredConditions)
            throws GovernorException {
      Set<Long> businessEntityIds = new HashSet<Long>();
      if (ExecueCoreUtil.isCollectionNotEmpty(structuredConditions)) {
         logger.debug("Populating Unique BusinessEntityTerms from BusinessTerms");
         if (ExecueCoreUtil.isCollectionNotEmpty(structuredConditions)) {
            for (StructuredCondition structuredCondition : structuredConditions) {
               businessEntityIds.add(structuredCondition.getLhsBusinessAssetTerm().getBusinessTerm()
                        .getBusinessEntityTerm().getBusinessEntityDefinitionId());
               if (OperandType.BUSINESS_TERM.equals(structuredCondition.getOperandType())) {
                  businessEntityIds.addAll(getBusinessEntityIdsSet(structuredCondition.getRhsBusinessAssetTerms()));
               } else if (OperandType.BUSINESS_QUERY.equals(structuredCondition.getOperandType())) {
                  businessEntityIds.addAll(populateBusinessEntityIds(structuredCondition.getRhsStructuredQuery()));
               }
            }
         }

      }
      return businessEntityIds;
   }

   /**
    * This method is helper method to populateBusinessEntityTerms() in order to get unique businessEntityTerms for
    * Conditions and having sections of businessQuery
    * 
    * @param businessConditions
    * @return businessEntityTerms
    */
   private static Set<BusinessEntityTerm> getConditionalBusinessEntityTerms (List<BusinessCondition> businessConditions)
            throws GovernorException {
      logger.debug("Inside getConditionalBusinessEntityTerms method");
      logger.debug("Got businessConditions " + businessConditions);
      Set<BusinessEntityTerm> businessEntityTerms = new HashSet<BusinessEntityTerm>();
      if (ExecueCoreUtil.isCollectionNotEmpty(businessConditions)) {
         logger.debug("Populating Unique BusinessEntityTerms from BusinessTerms");
         for (BusinessCondition businessCondition : businessConditions) {
            // Set will check on basis of hashCode and equals defined in
            // BusinessEntityTerm
            businessEntityTerms.add(businessCondition.getLhsBusinessTerm().getBusinessEntityTerm());
            if (ExecueCoreUtil.isCollectionNotEmpty(businessCondition.getLhsBusinessTermVariations())) {
               for (BusinessTerm businessTerm : businessCondition.getLhsBusinessTermVariations()) {
                  businessEntityTerms.add(businessTerm.getBusinessEntityTerm());
               }
            }
            if (OperandType.BUSINESS_TERM.equals(businessCondition.getOperandType())) {
               logger.debug("Found BusinessTerms");
               for (BusinessTerm businessTerm : businessCondition.getRhsBusinessTerms()) {
                  businessEntityTerms.add(businessTerm.getBusinessEntityTerm());
               }
            } else if (OperandType.BUSINESS_QUERY.equals(businessCondition.getOperandType())) {
               logger.debug("Found business sub query");
               businessEntityTerms.addAll(populateBusinessEntityTerms(businessCondition.getRhsBusinessQuery()));
            }
         }
      }

      return businessEntityTerms;
   }

   /**
    * This method is helper method to populateBusinessEntity ids in order to get unique businessEntityTerms for
    * Conditions and having sections of businessQuery
    * 
    * @param businessConditions
    * @return businessEntityIds
    */
   private static Set<Long> getConditionalBusinessEntityIds (List<BusinessCondition> businessConditions)
            throws GovernorException {
      logger.debug("Inside getConditionalBusinessEntityTerms method");
      logger.debug("Got businessConditions " + businessConditions);
      Set<Long> businessEntityIds = new HashSet<Long>();
      if (ExecueCoreUtil.isCollectionNotEmpty(businessConditions)) {
         logger.debug("Populating Unique BusinessEntityTerms from BusinessTerms");
         for (BusinessCondition businessCondition : businessConditions) {
            // Set will check on basis of hashCode and equals defined in
            // BusinessEntityTerm
            businessEntityIds.add(businessCondition.getLhsBusinessTerm().getBusinessEntityTerm()
                     .getBusinessEntityDefinitionId());
            if (ExecueCoreUtil.isCollectionNotEmpty(businessCondition.getLhsBusinessTermVariations())) {
               for (BusinessTerm businessTerm : businessCondition.getLhsBusinessTermVariations()) {
                  businessEntityIds.add(businessTerm.getBusinessEntityTerm().getBusinessEntityDefinitionId());
               }
            }
            if (OperandType.BUSINESS_TERM.equals(businessCondition.getOperandType())) {
               logger.debug("Found BusinessTerms");
               for (BusinessTerm businessTerm : businessCondition.getRhsBusinessTerms()) {
                  businessEntityIds.add(businessTerm.getBusinessEntityTerm().getBusinessEntityDefinitionId());
                  // set profile Id if exists
                  if (businessTerm.getProfileDomainEntityDefinitionId() != null) {
                     businessEntityIds.add(businessTerm.getProfileDomainEntityDefinitionId());
                  }
               }
            } else if (OperandType.BUSINESS_QUERY.equals(businessCondition.getOperandType())) {
               logger.debug("Found business sub query");
               businessEntityIds.addAll(populateBusinessEntityIds(businessCondition.getRhsBusinessQuery()));
            }
         }
      }

      return businessEntityIds;
   }

   /**
    * This method is helper method to populateAssetEntityTerms() in order to get unique businessEntityTerms for
    * Conditions and having sections of businessQuery
    * 
    * @param structuredConditions
    * @return assetEntityIds
    */
   private static Set<Long> getConditionalAssetEntityIds (List<StructuredCondition> structuredConditions)
            throws GovernorException {
      Set<Long> assetEntityIds = new HashSet<Long>();
      if (ExecueCoreUtil.isCollectionNotEmpty(structuredConditions)) {
         for (StructuredCondition structuredCondition : structuredConditions) {
            assetEntityIds.add(structuredCondition.getLhsBusinessAssetTerm().getAssetEntityTerm()
                     .getAssetEntityDefinitionId());
            if (OperandType.BUSINESS_TERM.equals(structuredCondition.getOperandType())) {
               assetEntityIds.addAll(getAssetEntityIdsSet(structuredCondition.getRhsBusinessAssetTerms()));
            } else if (OperandType.BUSINESS_QUERY.equals(structuredCondition.getOperandType())) {
               assetEntityIds.addAll(populateAssetEntityIds(structuredCondition.getRhsStructuredQuery()));
            }
         }
      }

      return assetEntityIds;
   }

   /**
    * This method returns the list of EntityMappingInfo
    * 
    * @param entityMappings
    * @return entityMappingInfos - list of EntityMappingInfo derived from the input entityMappings list
    */
   public static List<EntityMappingInfo> prepareEntityMappingInfo (List<EntityMapping> entityMappings) {
      if (logger.isDebugEnabled()) {
         logger.debug("Inside prepareEntityMappingInfo method");
         logger.debug("Got entityMappings : " + entityMappings);
      }
      List<EntityMappingInfo> entityMappingInfo = new ArrayList<EntityMappingInfo>();
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
      }
      return entityMappingInfo;
   }

   /**
    * This method is helper method to populateStructuredQueries() in order to find the BusinessAssetTerm corresponding
    * to businessTerm using the entityMappings passed and asset passed. If there is no mapping for businessTerm in
    * entityMappings then it cannot create an businessAssetTerm. Inside entityMapping , it might be the case there is no
    * assetentityDefinition for the asset in picture then also it won't create and businessAssetTerm.
    * 
    * @param businessTerm
    * @param entityMappings
    * @param asset
    * @return businessAssetTerm
    */
   public static BusinessAssetTerm populateBusinessAssetTerm (BusinessTerm businessTerm,
            List<EntityMappingInfo> entityMappings, Asset asset) {
      logger.debug("Inside populateBusinessAssetTerm method");
      logger.debug("Got BusinessTerm for which we need to populate BusinessAssetTerm : " + businessTerm);
      logger.debug("Got entityMapping using which we will populate BusinessAssetTerm : " + entityMappings);
      logger.debug("Got Asset for which we need to populate BusinessAssetTerm : " + asset.getName());
      BusinessAssetTerm businessAssetTerm = null;
      if (ExecueCoreUtil.isCollectionNotEmpty(entityMappings)) {
         EntityMappingInfo entityMappingInfo = getEntityMappingInfo(businessTerm.getBusinessEntityTerm(),
                  entityMappings);
         // if there is no mapping exist for this businessTerm
         if (entityMappingInfo != null) {
            AssetEntityTerm assetEntityTerm = entityMappingInfo.getAssetBasedAssetEntityTerm().get(asset.getId());
            if (assetEntityTerm != null) {
               businessAssetTerm = new BusinessAssetTerm();
               businessAssetTerm.setBusinessTerm(ExecueBeanCloneUtil.cloneBusinessTerm(businessTerm));
               businessAssetTerm.setAssetEntityTerm(assetEntityTerm);
               businessAssetTerm.setAssetEntityDefinitions(entityMappingInfo.getAssetBasedLightAssetEntityDefinitions()
                        .get(asset.getId()));
            }
         }
      }
      return businessAssetTerm;
   }

   /**
    * This method is helper method to populateStructuredQueries() in order to find the BusinessAssetTerms corresponding
    * to businessTerms using the entityMappings passed and asset passed. If there is no mapping for businessTerm in
    * entityMappings then it cannot create an businessAssetTerm. Inside entityMapping , it might be the case there is no
    * assetentityDefinition for the asset in picture then also it won't create and businessAssetTerm.
    * 
    * @param businessTerms
    * @param entityMappingInfo
    * @param asset
    * @return businessAssetTerms
    */
   public static List<BusinessAssetTerm> populateBusinessAssetTerms (List<BusinessTerm> businessTerms,
            List<EntityMappingInfo> entityMappingInfo, Asset asset) {
      logger.debug("Inside populateBusinessAssetTerms method");
      logger.debug("Got BusinessTerms for which we need to populate BusinessAssetTerms : " + businessTerms);
      logger.debug("Got entityMapping using which we will populate BusinessAssetTerms : " + entityMappingInfo);
      logger.debug("Got Asset for which we need to populate BusinessAssetTerms : " + asset.getName());
      List<BusinessAssetTerm> businessAssetTerms = new ArrayList<BusinessAssetTerm>();
      if (ExecueCoreUtil.isCollectionNotEmpty(businessTerms)) {
         for (BusinessTerm businessTerm : businessTerms) {
            EntityMappingInfo matchedEntityMappingInfo = getEntityMappingInfo(businessTerm.getBusinessEntityTerm(),
                     entityMappingInfo);
            // if there is no mapping exist for this businessTerm
            if (matchedEntityMappingInfo != null) {
               AssetEntityTerm assetEntityTerm = matchedEntityMappingInfo.getAssetBasedAssetEntityTerm().get(
                        asset.getId());
               if (assetEntityTerm != null) {
                  BusinessAssetTerm businessAssetTerm = new BusinessAssetTerm();
                  businessAssetTerm.setBusinessTerm(ExecueBeanCloneUtil.cloneBusinessTerm(businessTerm));
                  businessAssetTerm.setAssetEntityTerm(assetEntityTerm);
                  businessAssetTerm.setAssetEntityDefinitions(matchedEntityMappingInfo
                           .getAssetBasedLightAssetEntityDefinitions().get(asset.getId()));
                  businessAssetTerms.add(businessAssetTerm);
               }
            }
         }
      }
      return businessAssetTerms;
   }

   /**
    * This method is helper method to populateStructuredQueries() in order to find assetEntityTerms using
    * assetEntityDefintions passed and for the asset passed.
    * 
    * @param assetEntityDefinition
    */
   public static AssetEntityTerm populateAssetEntityTerm (AssetEntityDefinition assetEntityDefinition)
            throws GovernorException {
      logger.debug("Inside populateAssetEntityTerm method");
      AssetEntityTerm assetEntityTerm = new AssetEntityTerm();
      try {
         assetEntityTerm.setAssetEntityDefinitionId(assetEntityDefinition.getId().longValue());
         if (assetEntityDefinition.getMembr() != null) {
            assetEntityTerm.setAssetEntityType(AssetEntityType.MEMBER);
            assetEntityTerm.setAssetEntity(assetEntityDefinition.getMembr());
         } else if (assetEntityDefinition.getColum() != null) {
            assetEntityTerm.setAssetEntityType(AssetEntityType.COLUMN);
            assetEntityTerm.setAssetEntity(assetEntityDefinition.getColum());
         } else if (assetEntityDefinition.getTabl() != null) {
            assetEntityTerm.setAssetEntityType(AssetEntityType.TABLE);
            assetEntityTerm.setAssetEntity(assetEntityDefinition.getTabl());
         }
      } catch (Exception exception) {
         throw new GovernorException(GovernorExceptionCodes.GOV_DEFAULT_EXCEPTION_CODE,
                  "Error while obtaining the AssetEntity from the AssetEntityDefinition", exception.getCause());
      }
      return assetEntityTerm;
   }

   /**
    * This method is helper method for populateStructuredQueries() in order to find the mapping for the
    * businessEntityTerm among the list of entityMapping's passed to it.
    * 
    * @param businessEntityTerm
    * @param entityMappings
    * @return EntityMapping
    */
   public static EntityMappingInfo getEntityMappingInfo (BusinessEntityTerm businessEntityTerm,
            List<EntityMappingInfo> entityMappingInfo) {
      logger.debug("Inside getEntityMapping method");
      logger.debug("Got BusinessEntityTerm for which we need to find the mapping : " + businessEntityTerm);
      logger.debug("Got List of entityMapping in which we need to find the mapping : " + entityMappingInfo);
      EntityMappingInfo foundEntityMapping = null;
      if (ExecueCoreUtil.isCollectionNotEmpty(entityMappingInfo)) {
         for (EntityMappingInfo entityMapping : entityMappingInfo) {
            if (businessEntityTerm.equals(entityMapping.getBusinessEntityTerm())) {
               foundEntityMapping = entityMapping;
               break;
            }
         }
      }
      return foundEntityMapping;
   }

   /**
    * This method will check if the businessAssetTerm passed has the same business term in any of the businessAssetTerms
    * of metrics and different asset terms.It creates a new BusinessAssetTerm by cloning the business term of the
    * businessAssetTerm passed
    * 
    * @param businessAssetTerm
    * @param metrics
    * @return metricBusinessAssetTerm
    */
   public static BusinessAssetTerm findBusinessAssetTermsForMetrics (BusinessAssetTerm businessAssetTerm,
            List<BusinessAssetTerm> metrics) {
      BusinessAssetTerm metricBusinessAssetTerm = null;
      if (!isBusinessAssetTermExists(metrics, businessAssetTerm)) {
         List<BusinessAssetTerm> matchedBusinessAssetTerms = matchedBusinessTerms(metrics, businessAssetTerm);
         for (BusinessAssetTerm bAssetTerm : matchedBusinessAssetTerms) {
            if (!matchAssetTerms(businessAssetTerm, bAssetTerm)) {
               BusinessTerm clonedBusinessTerm = ExecueBeanCloneUtil.cloneBusinessTerm(businessAssetTerm
                        .getBusinessTerm());
               clonedBusinessTerm.setRequestedByUser(false);
               metricBusinessAssetTerm = new BusinessAssetTerm();
               metricBusinessAssetTerm.setBusinessTerm(clonedBusinessTerm);
               metricBusinessAssetTerm.setAssetEntityTerm(businessAssetTerm.getAssetEntityTerm());
            }
         }
      }
      return metricBusinessAssetTerm;
   }

   /**
    * This method adds or updates the businessAssetTerm to structured query if that term doesn't exists already in
    * metrics
    * 
    * @param structuredQuery
    * @param businessAssetTerm
    * @param fromPopulation
    * @param fromDistribution
    */
   public static void addUpdateBusinessAssetTermToMetrics (List<BusinessAssetTerm> metrics,
            BusinessAssetTerm businessAssetTerm, boolean fromPopulation, boolean fromDistribution) {
      BusinessAssetTerm matchedBusinessAssetTerm = getMatchedBusinessAssetTerm(businessAssetTerm, metrics);
      if (matchedBusinessAssetTerm == null) {
         BusinessTerm clonedBusinessTerm = ExecueBeanCloneUtil.cloneBusinessTerm(businessAssetTerm.getBusinessTerm());
         clonedBusinessTerm.setFromPopulation(fromPopulation);
         clonedBusinessTerm.setFromDistribution(fromDistribution);
         BusinessAssetTerm clonedBusinessAssetTerm = new BusinessAssetTerm();
         clonedBusinessAssetTerm.setBusinessTerm(clonedBusinessTerm);
         clonedBusinessAssetTerm.setAssetEntityTerm(businessAssetTerm.getAssetEntityTerm());
         clonedBusinessAssetTerm.setAssetEntityDefinitions(businessAssetTerm.getAssetEntityDefinitions());
         metrics.add(clonedBusinessAssetTerm);
      } else {
         matchedBusinessAssetTerm.getBusinessTerm().setFromPopulation(fromPopulation);
         matchedBusinessAssetTerm.getBusinessTerm().setFromDistribution(fromDistribution);
      }
   }

   /**
    * This method gets the matched the businessAssetTerm to list of metrics
    * 
    * @param businessAssetTerm
    * @param businessAssetTerms
    * @return matchedBusinessAssetTerm
    */
   public static BusinessAssetTerm getMatchedBusinessAssetTerm (BusinessAssetTerm businessAssetTerm,
            List<BusinessAssetTerm> businessAssetTerms) {
      BusinessAssetTerm matchedBusinessAssetTerm = null;
      for (BusinessAssetTerm businessAssTerm : businessAssetTerms) {
         if (businessAssetTerm.getBusinessTerm().getBusinessEntityTerm().equals(
                  businessAssTerm.getBusinessTerm().getBusinessEntityTerm())) {
            logger.debug("BusinessEntity Term match found");
            matchedBusinessAssetTerm = businessAssTerm;
            break;
         }
      }
      return matchedBusinessAssetTerm;
   }

   public static BusinessAssetTerm getMatchedBusinessAssetTerm (BusinessTerm businessTerm,
            List<BusinessAssetTerm> businessAssetTerms) {
      BusinessAssetTerm matchedBusinessAssetTerm = null;
      for (BusinessAssetTerm businessAssTerm : businessAssetTerms) {
         if (businessTerm.getBusinessEntityTerm().equals(businessAssTerm.getBusinessTerm().getBusinessEntityTerm())) {
            logger.debug("BusinessEntity Term match found");
            matchedBusinessAssetTerm = businessAssTerm;
            break;
         }
      }
      return matchedBusinessAssetTerm;
   }

   /**
    * This method takes mapping object as input and returns BusinessAssetTerm object as output
    * 
    * @param mapping
    * @return businessAssetTerm
    */
   public static BusinessAssetTerm prepareBusinessAssetTerm (Mapping mapping) {
      BusinessEntityDefinition businessEntityDefinition = mapping.getBusinessEntityDefinition();

      BusinessEntityTerm businessEntityTerm = new BusinessEntityTerm();
      businessEntityTerm.setBusinessEntity(businessEntityDefinition.getConcept());
      businessEntityTerm.setBusinessEntityType(BusinessEntityType.CONCEPT);
      businessEntityTerm.setBusinessEntityDefinitionId(businessEntityDefinition.getId());

      BusinessTerm businessTerm = new BusinessTerm();
      businessTerm.setBusinessEntityTerm(businessEntityTerm);
      businessTerm.setRequestedByUser(false);

      AssetEntityDefinition assetEntityDefinition = mapping.getAssetEntityDefinition();

      Tabl tabl = assetEntityDefinition.getTabl();
      Colum colum = assetEntityDefinition.getColum();
      colum.setOwnerTable(tabl);

      AssetEntityTerm assetEntityTerm = new AssetEntityTerm();
      assetEntityTerm.setAssetEntity(colum);
      assetEntityTerm.setAssetEntityType(AssetEntityType.COLUMN);
      assetEntityTerm.setAssetEntityDefinitionId(assetEntityDefinition.getId());

      List<AssetEntityTerm> assetEntityTerms = new ArrayList<AssetEntityTerm>();
      assetEntityTerms.add(assetEntityTerm);

      BusinessAssetTerm businessAssetTerm = new BusinessAssetTerm();
      businessAssetTerm.setBusinessTerm(businessTerm);
      businessAssetTerm.setAssetEntityTerm(assetEntityTerm);
      return businessAssetTerm;
   }

   /**
    * This method will check whether this businessAssetTerm exists in list of metrics.
    * 
    * @param metrics
    * @param businessAssetTerm
    * @return boolean value
    */
   private static boolean isBusinessAssetTermExists (List<BusinessAssetTerm> metrics,
            BusinessAssetTerm businessAssetTerm) {
      boolean isExists = false;
      List<BusinessAssetTerm> matchedBusinesTerms = matchedBusinessTerms(metrics, businessAssetTerm);
      for (BusinessAssetTerm bAssetTerm : matchedBusinesTerms) {
         if (matchAssetTerms(businessAssetTerm, bAssetTerm)) {
            isExists = true;
            break;
         }
      }
      return isExists;
   }

   /**
    * This method will check whether the asset terms at zeroth location of businessAsset terms are same or not.
    * 
    * @param businessAssetTerm1
    * @param businessAssetTerm2
    * @return boolean value
    */
   private static boolean matchAssetTerms (BusinessAssetTerm businessAssetTerm1, BusinessAssetTerm businessAssetTerm2) {
      AssetEntityTerm assetEntityTerm1 = businessAssetTerm1.getAssetEntityTerm();
      AssetEntityTerm assetEntityTerm2 = businessAssetTerm2.getAssetEntityTerm();
      Colum colum1 = (Colum) assetEntityTerm1.getAssetEntity();
      Colum colum2 = (Colum) assetEntityTerm2.getAssetEntity();
      return colum1.getName().equalsIgnoreCase(colum2.getName());
   }

   /**
    * This method will check whether the businessTerm exists in the list of businessAssetTerms passed, if yes return the
    * matched businessAssetTerms
    * 
    * @param selectBusinessAssetTerms
    * @param businessAssetTerm
    * @return matchedBusinessTerms
    */
   private static List<BusinessAssetTerm> matchedBusinessTerms (List<BusinessAssetTerm> selectBusinessAssetTerms,
            BusinessAssetTerm businessAssetTerm) {
      List<BusinessAssetTerm> matchedBusinessTerms = new ArrayList<BusinessAssetTerm>();
      BusinessTerm businessTerm = businessAssetTerm.getBusinessTerm();
      for (BusinessAssetTerm bAssetTerm : selectBusinessAssetTerms) {
         BusinessTerm bTerm = bAssetTerm.getBusinessTerm();
         if (bTerm.getBusinessStat() != null && businessTerm.getBusinessStat() != null) {
            if (bTerm.getBusinessStat().getStat().getStatType().equals(
                     businessTerm.getBusinessStat().getStat().getStatType())
                     && bTerm.getBusinessEntityTerm().equals(businessTerm.getBusinessEntityTerm())) {
               matchedBusinessTerms.add(bAssetTerm);
            }
         } else {
            if (bTerm.getBusinessEntityTerm().equals(businessTerm.getBusinessEntityTerm())) {
               matchedBusinessTerms.add(bAssetTerm);
            }
         }
      }
      return matchedBusinessTerms;
   }
}
