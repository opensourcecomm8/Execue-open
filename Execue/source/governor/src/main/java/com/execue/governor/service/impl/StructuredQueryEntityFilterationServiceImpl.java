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
import java.util.List;

import com.execue.core.common.bean.governor.BusinessAssetTerm;
import com.execue.core.common.bean.governor.StructuredCondition;
import com.execue.core.common.bean.governor.StructuredLimitClause;
import com.execue.core.common.bean.governor.StructuredOrderClause;
import com.execue.core.common.bean.governor.StructuredQuery;
import com.execue.core.common.type.AssetEntityType;
import com.execue.core.common.type.OperatorType;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.governor.bean.EntityFilterationInfo;
import com.execue.governor.configuration.IGovernorConfigurationService;
import com.execue.governor.exception.GovernorException;
import com.execue.governor.service.IStructuredQueryEntityFilterationService;
import com.execue.platform.security.ISecurityDefinitionWrapperService;
import com.execue.security.exception.SecurityException;

public class StructuredQueryEntityFilterationServiceImpl implements IStructuredQueryEntityFilterationService {

   private ISecurityDefinitionWrapperService securityDefinitionWrapperService;
   private IGovernorConfigurationService     governorConfigurationService;

   public StructuredQuery filterStructuredQueryBySecurity (StructuredQuery structuredQuery) throws GovernorException {
      try {
         EntityFilterationInfo entityFilterationInfo = populateEntityFilterationInfo(structuredQuery);
         List<Long> filteredColumnAEDIds = getFilteredColumnAEDIds(new ArrayList<Long>(entityFilterationInfo
                  .getColumnAssetEntityDefinitionIds()));
         List<Long> filteredMemberAEDIds = new ArrayList<Long>(entityFilterationInfo
                  .getMemberAssetEntityDefinitionIds());
         // If ApplyMemberLevelSecurityFilters flag has been set in configuration.
         if (getGovernorConfigurationService().isApplyMemberLevelSecurityFilters()) {
            filteredMemberAEDIds = getFilteredMemberAEDIds(filteredMemberAEDIds);
         }
         if (ExecueCoreUtil.isCollectionEmpty(filteredColumnAEDIds)) {
            structuredQuery = null;
         } else {
            organizeStructureQueryByFilteration(structuredQuery, filteredColumnAEDIds, filteredMemberAEDIds);
         }
         return structuredQuery;
      } catch (SecurityException e) {
         throw new GovernorException(e.getCode(), e);
      }
   }

   /**
    * This method collects AssetEntityDefinitionId for all the sections of StructureQuery and populate
    * EntityFilterationInfo bean which consists set of ColumnAEDIds and MemberAEDIds.
    * 
    * @param structuredQuery
    * @return
    */
   private EntityFilterationInfo populateEntityFilterationInfo (StructuredQuery structuredQuery) {
      EntityFilterationInfo entityFilterationInfo = new EntityFilterationInfo();
      populateEntityFilterationInfoByEntityType(entityFilterationInfo, structuredQuery.getMetrics());
      populateEntityFilterationInfoByEntityType(entityFilterationInfo, structuredQuery.getSummarizations());
      populateEntityFilterationInfoByEntityType(entityFilterationInfo, structuredQuery.getPopulations());
      populateEntityFilterationInfoForConditions(entityFilterationInfo, structuredQuery.getConditions());
      populateEntityFilterationInfoForConditions(entityFilterationInfo, structuredQuery.getHavingClauses());
      populateEntityFilterationInfoForOrderClause(entityFilterationInfo, structuredQuery.getOrderClauses());
      populateEntityFilterationInfoForTopBottom(entityFilterationInfo, structuredQuery.getTopBottom());
      populateEntityFilterationInfoForCohort(entityFilterationInfo, structuredQuery.getCohort());
      return entityFilterationInfo;
   }

   private void populateEntityFilterationInfoByEntityType (EntityFilterationInfo entityFilterationInfo,
            List<BusinessAssetTerm> businessAssetTerms) {
      if (ExecueCoreUtil.isCollectionNotEmpty(businessAssetTerms)) {
         for (BusinessAssetTerm businessAssetTerm : businessAssetTerms) {
            populateEntityFilterationInfoByEntityType(entityFilterationInfo, businessAssetTerm);
         }
      }
   }

   /**
    * This method collects AssetEntityDefinitionId on the basis of entity-type of BusinessAssetTerm. If the type is
    * column then it populates ColumnAssetEntityDefinitionIds else if it is member then it populates
    * MemberAssetEntityDefinitionIds.
    * 
    * @param entityFilterationInfo
    * @param businessAssetTerm
    */
   private void populateEntityFilterationInfoByEntityType (EntityFilterationInfo entityFilterationInfo,
            BusinessAssetTerm businessAssetTerm) {
      Long assetEntityDefinitionId = null;
      AssetEntityType assetEntityType = businessAssetTerm.getAssetEntityTerm().getAssetEntityType();
      if (AssetEntityType.COLUMN == assetEntityType) {
         assetEntityDefinitionId = businessAssetTerm.getAssetEntityTerm().getAssetEntityDefinitionId();
         entityFilterationInfo.getColumnAssetEntityDefinitionIds().add(assetEntityDefinitionId);
      } else if (AssetEntityType.MEMBER == assetEntityType) {
         assetEntityDefinitionId = businessAssetTerm.getAssetEntityTerm().getAssetEntityDefinitionId();
         entityFilterationInfo.getMemberAssetEntityDefinitionIds().add(assetEntityDefinitionId);
      }
   }

   private void populateEntityFilterationInfoForConditions (EntityFilterationInfo entityFilterationInfo,
            List<StructuredCondition> structuredConditions) {
      if (ExecueCoreUtil.isCollectionNotEmpty(structuredConditions)) {
         for (StructuredCondition condition : structuredConditions) {
            populateEntityFilterationInfoForConditions(entityFilterationInfo, condition);
         }
      }
   }

   private void populateEntityFilterationInfoForConditions (EntityFilterationInfo entityFilterationInfo,
            StructuredCondition structuredCondition) {
      if (structuredCondition.isSubCondition()) {
         populateEntityFilterationInfoForSubConditions(entityFilterationInfo, structuredCondition.getSubConditions());
      } else {
         // Collect AED id from LHS part of the condition .
         populateEntityFilterationInfoByEntityType(entityFilterationInfo, structuredCondition.getLhsBusinessAssetTerm());
         // Collect AED id's from RHS part of the condition.
         switch (structuredCondition.getOperandType()) {
            case BUSINESS_TERM:
               populateEntityFilterationInfoByEntityType(entityFilterationInfo, structuredCondition
                        .getRhsBusinessAssetTerms());
               break;
            case BUSINESS_QUERY:
               populateEntityFilterationInfoForSubQuery(entityFilterationInfo, structuredCondition);
               break;
         }
      }
   }

   private void populateEntityFilterationInfoForSubConditions (EntityFilterationInfo entityFilterationInfo,
            List<StructuredCondition> structuredConditions) {
      for (StructuredCondition subCondition : structuredConditions) {
         populateEntityFilterationInfoForConditions(entityFilterationInfo, subCondition);
      }
   }

   private void populateEntityFilterationInfoForOrderClause (EntityFilterationInfo entityFilterationInfo,
            List<StructuredOrderClause> orderClauses) {
      if (ExecueCoreUtil.isCollectionNotEmpty(orderClauses)) {
         for (StructuredOrderClause orderClause : orderClauses) {
            populateEntityFilterationInfoByEntityType(entityFilterationInfo, orderClause.getBusinessAssetTerm());
         }
      }
   }

   private void populateEntityFilterationInfoForCohort (EntityFilterationInfo entityFilterationInfo,
            StructuredQuery cohortQuery) {
      if (cohortQuery != null) {
         EntityFilterationInfo cohoEntityFilterationInfo = populateEntityFilterationInfo(cohortQuery);
         if (ExecueCoreUtil.isCollectionNotEmpty(cohoEntityFilterationInfo.getColumnAssetEntityDefinitionIds())) {
            entityFilterationInfo.getColumnAssetEntityDefinitionIds().addAll(
                     cohoEntityFilterationInfo.getColumnAssetEntityDefinitionIds());
         }
      }
   }

   private void populateEntityFilterationInfoForTopBottom (EntityFilterationInfo entityFilterationInfo,
            StructuredLimitClause topBottomClause) {
      if (topBottomClause != null) {
         populateEntityFilterationInfoByEntityType(entityFilterationInfo, topBottomClause.getBusinessAssetTerm());
      }
   }

   private void populateEntityFilterationInfoForSubQuery (EntityFilterationInfo entityFilterationInfo,
            StructuredCondition structuredCondition) {
      StructuredQuery rhsStructuredQuery = structuredCondition.getRhsStructuredQuery();
      EntityFilterationInfo subQueryEntityFilterationInfo = populateEntityFilterationInfo(rhsStructuredQuery);
      if (ExecueCoreUtil.isCollectionNotEmpty(subQueryEntityFilterationInfo.getColumnAssetEntityDefinitionIds())) {
         entityFilterationInfo.getColumnAssetEntityDefinitionIds().addAll(
                  subQueryEntityFilterationInfo.getColumnAssetEntityDefinitionIds());
      }
   }

   private List<Long> getFilteredColumnAEDIds (List<Long> columnAEDIds) throws SecurityException {
      List<Long> filteredColumnAEDIds = null;
      if (ExecueCoreUtil.isCollectionNotEmpty(columnAEDIds)) {
         filteredColumnAEDIds = getSecurityDefinitionWrapperService().filterColumnAEDIDsBySecurity(columnAEDIds);
      }
      return filteredColumnAEDIds;
   }

   private List<Long> getFilteredMemberAEDIds (List<Long> memberAEDIds) throws SecurityException {
      List<Long> filteredMemberAEDIds = null;
      if (ExecueCoreUtil.isCollectionNotEmpty(memberAEDIds)) {
         filteredMemberAEDIds = getSecurityDefinitionWrapperService().filterMemberAEDIDsBySecurity(memberAEDIds);
      }
      return filteredMemberAEDIds;
   }

   /**
    * This method filter StructureQuery by checking each section of it for valid BusinessAssetTerms filtered by
    * security. If a section after validation does not have any valid BusinessAssetTerms then it will have an empty
    * list.
    * 
    * @param structuredQuery
    * @param filteredColumnAEDIds
    * @param filteredMemberAEDIds
    * @throws GovernorException
    */
   private void organizeStructureQueryByFilteration (StructuredQuery structuredQuery, List<Long> columnAEDIds,
            List<Long> memberAEDIds) throws GovernorException {
      structuredQuery.setMetrics(getFilteredBusinessAssetTerms(structuredQuery.getMetrics(), columnAEDIds));
      structuredQuery
               .setSummarizations(getFilteredBusinessAssetTerms(structuredQuery.getSummarizations(), columnAEDIds));
      structuredQuery.setPopulations(getFilteredBusinessAssetTerms(structuredQuery.getPopulations(), columnAEDIds));
      structuredQuery.setConditions(getFilteredStructuredConditions(structuredQuery.getConditions(), columnAEDIds,
               memberAEDIds));
      structuredQuery.setHavingClauses(getFilteredStructuredConditions(structuredQuery.getHavingClauses(),
               columnAEDIds, memberAEDIds));
      organizeStructuredQueryForTopBottom(structuredQuery, columnAEDIds);
      organizeStructuredQueryForCohort(structuredQuery);
   }

   private void organizeStructuredQueryForCohort (StructuredQuery structuredQuery) throws GovernorException {
      if (structuredQuery.getCohort() != null) {
         filterStructuredQueryBySecurity(structuredQuery.getCohort());
      }
   }

   private void organizeStructuredQueryForTopBottom (StructuredQuery structuredQuery, List<Long> columnAEDIds) {
      StructuredLimitClause topBottom = structuredQuery.getTopBottom();
      if (topBottom != null) {
         if (isValidBusinessAssetTerm(topBottom.getBusinessAssetTerm(), columnAEDIds)) {
            structuredQuery.setTopBottom(topBottom);
         } else {
            structuredQuery.setTopBottom(null);
         }
      }
   }

   private List<BusinessAssetTerm> getFilteredBusinessAssetTerms (List<BusinessAssetTerm> businessAssetTerms,
            List<Long> assetEntityDefitionIds) {
      List<BusinessAssetTerm> filteredBusinessAssetTerms = new ArrayList<BusinessAssetTerm>();
      if (ExecueCoreUtil.isCollectionNotEmpty(businessAssetTerms)) {
         for (BusinessAssetTerm businessAssetTerm : businessAssetTerms) {
            if (isValidBusinessAssetTerm(businessAssetTerm, assetEntityDefitionIds)) {
               filteredBusinessAssetTerms.add(businessAssetTerm);
            }
         }
      }
      return filteredBusinessAssetTerms;
   }

   private List<StructuredCondition> getFilteredStructuredConditions (List<StructuredCondition> structureConditions,
            List<Long> columnsAEDIds, List<Long> membersAEDIds) throws GovernorException {
      List<StructuredCondition> filteredStructureConditions = new ArrayList<StructuredCondition>();
      if (ExecueCoreUtil.isCollectionNotEmpty(structureConditions)) {
         for (StructuredCondition structuredCondition : structureConditions) {
            if (structuredCondition.isSubCondition()) {
               getFilteredStructuredConditions(structuredCondition.getSubConditions(), columnsAEDIds, membersAEDIds);
            } else {
               populateFilteredStructuredConditions(filteredStructureConditions, structuredCondition, columnsAEDIds,
                        membersAEDIds);
            }
         }
      }
      return filteredStructureConditions;
   }

   /**
    * This method populates list of StructuredCondition which has valid BusinessAssetTerms in the LHS and RHS part. On
    * the basis of the operand type the validation has been done. Finally according do the size of valid
    * StructuredCondition list the operator has been modified.If the validation fails for any given StructuredCondition
    * then condition will be removed from the StructureQuery.
    * 
    * @param structureConditions
    * @param structuredCondition
    * @param filteredColumnsAEDIds
    * @param filteredMembersAEDIds
    * @throws GovernorException
    */
   private void populateFilteredStructuredConditions (List<StructuredCondition> filteredStructuredConditions,
            StructuredCondition structuredCondition, List<Long> columnsAEDIds, List<Long> membersAEDIds)
            throws GovernorException {
      BusinessAssetTerm lhsBusinessAssetTerm = structuredCondition.getLhsBusinessAssetTerm();
      if (isValidBusinessAssetTerm(lhsBusinessAssetTerm, columnsAEDIds)) {
         switch (structuredCondition.getOperandType()) {
            case BUSINESS_TERM:
               List<BusinessAssetTerm> filteredRHSBusinessAssetTerms = getFilteredRHSBusinessAssetTerms(
                        structuredCondition.getRhsBusinessAssetTerms(), columnsAEDIds, membersAEDIds);
               if (ExecueCoreUtil.isCollectionNotEmpty(filteredRHSBusinessAssetTerms)) {
                  updateStructuredConditionOperator(filteredRHSBusinessAssetTerms, structuredCondition);
                  filteredStructuredConditions.add(structuredCondition);
               }
               break;
            case BUSINESS_QUERY:
               StructuredQuery subStructuredQuery = filterStructuredQueryBySecurity(structuredCondition
                        .getRhsStructuredQuery());
               if (isValidStructureQuery(subStructuredQuery)) {
                  filteredStructuredConditions.add(structuredCondition);
               }
               break;
         }
      }
   }

   /**
    * This method returns list of valid BusinessAssetTerms list from RHS part of the condition. The BusinessAssetTerm
    * can be of a column or a member. If its a column it filters on the basis of FilteredColumnAEDIds else
    * FilteredMemberAEDIds.
    * 
    * @param businessAssetTerms
    * @param filteredColumnsAEDIds
    * @param filteredMembersAEDIds
    * @return
    */
   private List<BusinessAssetTerm> getFilteredRHSBusinessAssetTerms (List<BusinessAssetTerm> businessAssetTerms,
            List<Long> columnsAEDIds, List<Long> membersAEDIds) {
      List<BusinessAssetTerm> filteredRHSBusinessAssetTerms = new ArrayList<BusinessAssetTerm>();
      if (isColumnBusinessAssetTerm(businessAssetTerms)) {
         filteredRHSBusinessAssetTerms = getFilteredBusinessAssetTerms(businessAssetTerms, columnsAEDIds);
      } else {
         filteredRHSBusinessAssetTerms = getFilteredBusinessAssetTerms(businessAssetTerms, membersAEDIds);
      }
      return filteredRHSBusinessAssetTerms;
   }

   private boolean isColumnBusinessAssetTerm (List<BusinessAssetTerm> businessAssetTerms) {
      boolean columnBusinessAssetTerm = true;
      if (ExecueCoreUtil.isCollectionNotEmpty(businessAssetTerms)) {
         BusinessAssetTerm businessAssetTerm = businessAssetTerms.get(0);
         if (AssetEntityType.COLUMN == businessAssetTerm.getAssetEntityTerm().getAssetEntityType()) {
            columnBusinessAssetTerm = true;
         } else if (AssetEntityType.MEMBER == businessAssetTerm.getAssetEntityTerm().getAssetEntityType()) {
            columnBusinessAssetTerm = false;
         }
      }
      return columnBusinessAssetTerm;
   }

   /**
    * This method checks for the valid BusinessAssetTerms size and update the operator in the StructuredCondition
    * according to the size of the valid BusinessAssetTerms. Finally it adds the valid StructuredCondition to the valid
    * StructuredConditions list.
    * 
    * @param structureConditions
    * @param businessAssetTerms
    * @param structuredCondition
    */
   private void updateStructuredConditionOperator (List<BusinessAssetTerm> businessAssetTerms,
            StructuredCondition structuredCondition) {
      if (businessAssetTerms.size() == 1) {
         structuredCondition.setOperator(OperatorType.EQUALS);
      } else {
         structuredCondition.setOperator(OperatorType.IN);
      }
   }

   private boolean isValidBusinessAssetTerm (BusinessAssetTerm businessAssetTerm, List<Long> assetEntityDefinitionIds) {
      boolean validBusinessAssetTerm = false;
      Long assetEntityDefinitionId = businessAssetTerm.getAssetEntityTerm().getAssetEntityDefinitionId();
      if (ExecueCoreUtil.isCollectionNotEmpty(assetEntityDefinitionIds)) {
         if (assetEntityDefinitionIds.contains(assetEntityDefinitionId)) {
            validBusinessAssetTerm = true;
         }
      }
      return validBusinessAssetTerm;
   }

   private boolean isValidStructureQuery (StructuredQuery structuredQuery) {
      return (ExecueCoreUtil.isCollectionNotEmpty(structuredQuery.getMetrics()));
   }

   public ISecurityDefinitionWrapperService getSecurityDefinitionWrapperService () {
      return securityDefinitionWrapperService;
   }

   public void setSecurityDefinitionWrapperService (ISecurityDefinitionWrapperService securityDefinitionWrapperService) {
      this.securityDefinitionWrapperService = securityDefinitionWrapperService;
   }

   public IGovernorConfigurationService getGovernorConfigurationService () {
      return governorConfigurationService;
   }

   public void setGovernorConfigurationService (IGovernorConfigurationService governorConfigurationService) {
      this.governorConfigurationService = governorConfigurationService;
   }
}
