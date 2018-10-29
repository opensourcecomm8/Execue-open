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


package com.execue.platform.swi.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.Constraint;
import com.execue.core.common.bean.entity.Join;
import com.execue.core.common.bean.entity.Membr;
import com.execue.core.common.bean.entity.Tabl;
import com.execue.core.common.type.LookupType;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.platform.security.ISecurityDefinitionWrapperService;
import com.execue.platform.swi.ISDXDeletionWrapperService;
import com.execue.security.exception.SecurityException;
import com.execue.swi.configuration.ISWIConfigurationService;
import com.execue.swi.exception.JoinException;
import com.execue.swi.exception.MappingException;
import com.execue.swi.exception.SDXException;
import com.execue.swi.service.IJoinService;
import com.execue.swi.service.IMappingDeletionService;
import com.execue.swi.service.ISDXDeletionService;
import com.execue.swi.service.ISDXRetrievalService;

public class SDXDeletionWrapperServiceImpl implements ISDXDeletionWrapperService {

   ISDXRetrievalService              sdxRetrievalService;
   IMappingDeletionService           mappingDeletionService;
   ISDXDeletionService               sdxDeletionService;
   ISecurityDefinitionWrapperService securityDefinitionWrapperService;
   IJoinService                      joinService;
   ISWIConfigurationService          swiConfigurationService;

   @Override
   public void deleteAssetTables (Asset asset, List<Tabl> tables) throws SDXException {
      try {
         /*
          * If there are some virtual tables generated out of these tables, which are not already in, then get those
          * virtual tables and add to the list of tables
          */
         List<Long> consideredTableIds = new ArrayList<Long>();
         List<String> consideredTableNames = new ArrayList<String>();
         for (Tabl tabl : tables) {
            consideredTableIds.add(tabl.getId());
            consideredTableNames.add(tabl.getName());
         }
         List<Tabl> notAlreadyConsideredVirtualTables = getSdxRetrievalService()
                  .getNotAlreadyConsideredVirtualTablesFromTables(asset.getId(), consideredTableIds,
                           consideredTableNames);
         if (ExecueCoreUtil.isCollectionNotEmpty(notAlreadyConsideredVirtualTables)) {
            tables.addAll(notAlreadyConsideredVirtualTables);
         }// Done with considering virtual tables

         deleteTables(asset, tables);
      } catch (SecurityException securityException) {
         throw new SDXException(securityException.getCode(), securityException);
      }
   }
   
   public void deleteAssetTableColumns (Asset asset, Tabl table, List<Colum> columns) throws SDXException {
      try {
         int batchSize = getSwiConfigurationService().getMemberDeletionBatchSize();
         for (Colum column : columns) {
            Set<Constraint> directConstraints = column.getConstraints();
            List<Constraint> referencedConstraints = getSdxRetrievalService().getReferencedConstraints(column.getId());
            List<Constraint> columConstraints = new ArrayList<Constraint>();
            if (ExecueCoreUtil.isCollectionNotEmpty(directConstraints)) {
               columConstraints.addAll(directConstraints);
            }
            if (ExecueCoreUtil.isCollectionNotEmpty(referencedConstraints)) {
               columConstraints.addAll(referencedConstraints);
            }
            if (columConstraints.size() > 0) {
               getSdxDeletionService().deleteConstraints(columConstraints);
            }
            deleteSecurityDefinitionsOnMembers(asset, table);
            getSdxDeletionService().deleteMembersInBatches(column, batchSize);
         }
         List<Join> existingJoins = getJoinService().getAllExistingJoins(asset.getId(), table.getName());
         if (existingJoins.size() > 0) {
            // get the joins corresponding to these columns which needs to be deleted
            List<Join> matchedJoins = getSdxRetrievalService().getMatchedJoins(existingJoins, columns);
            if (ExecueCoreUtil.isCollectionNotEmpty(matchedJoins)) {
               getJoinService().deleteJoins(asset.getId(), matchedJoins);
            }
         }
         getSecurityDefinitionWrapperService().deleteSecurityDefinitions(columns);
         getSdxDeletionService().deleteColumnsWithOutConsideringChildEntities(columns);
      } catch (JoinException joinException) {
         joinException.printStackTrace();
      } catch (SecurityException securityException) {
         throw new SDXException(securityException.getCode(), securityException);
      }
   }

   private void deleteTables (Asset asset, List<Tabl> tables) throws SDXException, SecurityException {
      try {
         int batchSize = getSwiConfigurationService().getMemberDeletionBatchSize();
         for (Tabl table : tables) {
            getMappingDeletionService().deleteDefaultMetrics(table.getId());
            List<Colum> columns = getSdxRetrievalService().getColumnsOfTable(table);
            if (columns.size() > 0) {
               for (Colum column : columns) {

                  Set<Constraint> directConstraints = column.getConstraints();
                  List<Constraint> referencedConstraints = getSdxRetrievalService().getReferencedConstraints(
                           column.getId());
                  List<Constraint> columConstraints = new ArrayList<Constraint>();
                  if (ExecueCoreUtil.isCollectionNotEmpty(directConstraints)) {
                     columConstraints.addAll(directConstraints);
                  }
                  if (ExecueCoreUtil.isCollectionNotEmpty(referencedConstraints)) {
                     columConstraints.addAll(referencedConstraints);
                  }
                  if (columConstraints.size() > 0) {
                     getSdxDeletionService().deleteConstraints(columConstraints);
                  }
                  deleteSecurityDefinitionsOnMembers(asset, table);
                  getSdxDeletionService().deleteMembersInBatches(column, batchSize);
               }
               getSecurityDefinitionWrapperService().deleteSecurityDefinitions(columns);
               getSdxDeletionService().deleteColumnsWithOutConsideringChildEntities(columns);
            }
            List<Join> existingJoins = getJoinService().getAllExistingJoins(asset.getId(), table.getName());
            if (existingJoins.size() > 0) {
               getJoinService().deleteJoins(asset.getId(), existingJoins);
            }
         }
         if (tables.size() > 0) {
            getSecurityDefinitionWrapperService().deleteSecurityDefinitions(tables);
            getSdxDeletionService().deleteTablesWithOutConsideringChildEntities(tables);
         }
      } catch (JoinException e) {
         e.printStackTrace();
      } catch (MappingException e) {
         e.printStackTrace();
      }
   }

   private void deleteSecurityDefinitionsOnMembers (Asset asset, Tabl table) throws SDXException, SecurityException {
      // delete security definitions on Members if the table is of any Lookup type
      if (table.getLookupType() != null && !table.getLookupType().equals(LookupType.None)) {
         Colum lookupValueColumn = getSdxRetrievalService().getAssetTableColum(asset.getId(), table.getName(),
                  table.getLookupValueColumn());
         Long totalMembersCount = getSdxRetrievalService().getTotalMembersCountOfColumn(lookupValueColumn);
         Long nextPageNumber = 1L;
         Long pageSize = new Long(getSwiConfigurationService().getMemberDeletionBatchSize());
         while (nextPageNumber != -99 && totalMembersCount > 0) {
            List<Membr> members = getSdxRetrievalService().getColumnMembersByPage(lookupValueColumn, nextPageNumber,
                     pageSize);
            getSecurityDefinitionWrapperService().deleteSecurityDefinitions(members);
            nextPageNumber = ExecueCoreUtil.getNextPageNumber(nextPageNumber, pageSize, totalMembersCount);
         }
      }
   }

   public ISDXRetrievalService getSdxRetrievalService () {
      return sdxRetrievalService;
   }

   public void setSdxRetrievalService (ISDXRetrievalService sdxRetrievalService) {
      this.sdxRetrievalService = sdxRetrievalService;
   }

   public IMappingDeletionService getMappingDeletionService () {
      return mappingDeletionService;
   }

   public void setMappingDeletionService (IMappingDeletionService mappingDeletionService) {
      this.mappingDeletionService = mappingDeletionService;
   }

   public ISDXDeletionService getSdxDeletionService () {
      return sdxDeletionService;
   }

   public void setSdxDeletionService (ISDXDeletionService sdxDeletionService) {
      this.sdxDeletionService = sdxDeletionService;
   }

   public ISecurityDefinitionWrapperService getSecurityDefinitionWrapperService () {
      return securityDefinitionWrapperService;
   }

   public void setSecurityDefinitionWrapperService (ISecurityDefinitionWrapperService securityDefinitionWrapperService) {
      this.securityDefinitionWrapperService = securityDefinitionWrapperService;
   }

   public IJoinService getJoinService () {
      return joinService;
   }

   public void setJoinService (IJoinService joinService) {
      this.joinService = joinService;
   }

   
   public ISWIConfigurationService getSwiConfigurationService () {
      return swiConfigurationService;
   }

   
   public void setSwiConfigurationService (ISWIConfigurationService swiConfigurationService) {
      this.swiConfigurationService = swiConfigurationService;
   }

}
