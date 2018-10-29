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
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.execue.core.common.bean.batchMaintenance.InstanceAbsorptionContext;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.AssetEntityDefinition;
import com.execue.core.common.bean.entity.BatchProcess;
import com.execue.core.common.bean.entity.BatchProcessDetail;
import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.Constraint;
import com.execue.core.common.bean.entity.Instance;
import com.execue.core.common.bean.entity.InstanceMappingSuggestion;
import com.execue.core.common.bean.entity.InstanceMappingSuggestionDetail;
import com.execue.core.common.bean.entity.JobOperationalStatus;
import com.execue.core.common.bean.entity.JobRequest;
import com.execue.core.common.bean.entity.Mapping;
import com.execue.core.common.bean.entity.Membr;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.common.bean.entity.ModelGroup;
import com.execue.core.common.bean.entity.Tabl;
import com.execue.core.common.bean.entity.wrapper.TableInfo;
import com.execue.core.common.bean.mapping.AssetMapping;
import com.execue.core.common.bean.mapping.ColumnMapping;
import com.execue.core.common.bean.mapping.MemberMapping;
import com.execue.core.common.bean.mapping.TableMapping;
import com.execue.core.common.type.BatchProcessDetailType;
import com.execue.core.common.type.BatchType;
import com.execue.core.common.type.BusinessEntityType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.ColumnType;
import com.execue.core.common.type.ConstraintType;
import com.execue.core.common.type.JobStatus;
import com.execue.core.common.type.LookupType;
import com.execue.core.common.util.ExecueBeanManagementUtil;
import com.execue.core.configuration.ICoreConfigurationService;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.core.util.ExecueStringUtil;
import com.execue.platform.exception.PlatformException;
import com.execue.platform.swi.IBusinessEntityManagementWrapperService;
import com.execue.platform.swi.ISDX2KDXMappingService;
import com.execue.qdata.exception.QueryDataException;
import com.execue.qdata.service.IJobDataService;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.MappingException;
import com.execue.swi.exception.SDX2KDXMappingException;
import com.execue.swi.exception.SDXException;
import com.execue.swi.exception.SWIExceptionCodes;
import com.execue.swi.service.IBaseKDXRetrievalService;
import com.execue.swi.service.IBatchMaintenanceService;
import com.execue.swi.service.IInstanceMappingSuggestionService;
import com.execue.swi.service.IKDXManagementService;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.swi.service.IMappingManagementService;
import com.execue.swi.service.IMappingRetrievalService;
import com.execue.swi.service.ISDXRetrievalService;

/**
 * @author John Mallavalli
 */
public class SDX2KDXMappingServiceImpl implements ISDX2KDXMappingService {

   private static final Logger                     logger              = Logger
                                                                                .getLogger(SDX2KDXMappingServiceImpl.class);
   private IMappingRetrievalService                mappingRetrievalService;
   private IMappingManagementService               mappingManagementService;
   private ISDXRetrievalService                    sdxRetrievalService;
   private IKDXRetrievalService                    kdxRetrievalService;
   private IKDXManagementService                   kdxManagementService;
   private IInstanceMappingSuggestionService       instanceMappingSuggestionService;
   private IJobDataService                         jobDataService;
   private ICoreConfigurationService               coreConfigurationService;
   private IBatchMaintenanceService                batchMaintenanceService;
   private IBaseKDXRetrievalService                baseKDXRetrievalService;
   private IBusinessEntityManagementWrapperService businessEntityManagementWrapperService;
   private static final Long                       SUGGESTED_ENTITY_ID = -1L;

   // below list should contain the words which can be omitted when the name of the concept is constructed, like 'id'
   public static List<String>                      trivialWords        = new ArrayList<String>();
   // can contain any expansions that are generally used and accepted
   // for now this is empty
   public static Map<String, String>               expansions          = new HashMap<String, String>();

   /**
    * All the unmapped columns and the members, if any, in an asset, would be mapped to the matching concepts or
    * instances
    * 
    * @param asset
    *           the asset for which the mappings have to be determined
    * @param mapMembers
    *           set this flag if the members of a column need to be mapped
    * @param mapMemberAsConcept
    *           set this flag if the members are to be mapped to concepts instead of instances
    * @param create
    *           set this flag if the algorithm needs to create the matching concepts or instances, if the flag is not
    *           set then null is returned when no match is found. If the flag is set, and if there are no matched found
    *           then the normalized name of the column name will be shown as the suggested name of the concept to which
    *           the column can be mapped
    * @return
    */
   public AssetMapping mapSDX2KDXForAsset (Asset asset, boolean mapMembers, boolean mapMemberAsConcept, boolean create,
            Long startCounter, Model model) throws SDX2KDXMappingException {
      AssetMapping assetMapping = new AssetMapping();
      List<TableMapping> tableMappings = new ArrayList<TableMapping>();
      // This map is necessary for generating the indices for the concept names
      Map<String, Integer> conceptsWithIndexMap = new HashMap<String, Integer>();
      // This map holds the mappings that are deduced/suggested and which are not present in the database
      List<Mapping> suggestedMappings = new ArrayList<Mapping>();
      try {
         ModelGroup modelGroup = kdxRetrievalService.getPrimaryGroup(model.getId());
         // Get the list of Tables belonging to the asset
         List<TableInfo> tables = getSdxRetrievalService().getAssetTables(asset);
         for (TableInfo table : tables) {
            TableMapping tableMapping = mapSDX2KDXForTable(asset, table.getTable(), conceptsWithIndexMap,
                     suggestedMappings, mapMembers, mapMemberAsConcept, create, startCounter, model, modelGroup);
            tableMappings.add(tableMapping);
         }
         assetMapping.setTableMappings(tableMappings);
      } catch (SDXException e) {
         throw new SDX2KDXMappingException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e.getCause());
      } catch (KDXException e) {
         throw new SDX2KDXMappingException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e.getCause());
      }
      return assetMapping;
   }

   /**
    * All the unmapped columns and the members, if any, in a particular table, would be mapped to the matching concepts
    * or instances
    * 
    * @param asset
    * @param table
    *           the table whose columns are to be mapped
    * @param mapMembers
    *           set this flag if the members of a column need to be mapped
    * @param mapMemberAsConcept
    *           set this flag if the members are to be mapped to concepts instead of instances
    * @param create
    *           set this flag if the algorithm needs to create the matching concepts or instances, if the flag is not
    *           set then null is returned when no match is found. If the flag is set, and if there are no matched found
    *           then the normalized name of the column name will be shown as the suggested name of the concept to which
    *           the column can be mapped
    * @return
    */
   public TableMapping mapSDX2KDXForTable (Asset asset, Tabl table, boolean mapMembers, boolean mapMemberAsConcept,
            boolean create, Long startCounter, Model model) throws SDX2KDXMappingException {
      TableMapping tableMapping = null;
      try {
         ModelGroup modelGroup = kdxRetrievalService.getPrimaryGroup(model.getId());
         Map<String, Integer> conceptsWithIndexMap = new HashMap<String, Integer>();
         List<Mapping> suggestedMappings = new ArrayList<Mapping>();
         tableMapping = mapSDX2KDXForTable(asset, table, conceptsWithIndexMap, suggestedMappings, mapMembers,
                  mapMemberAsConcept, create, startCounter, model, modelGroup);
      } catch (KDXException e) {
         e.printStackTrace();
      }
      return tableMapping;
   }

   /**
    * The column and its members would be mapped to the matching concepts or instances
    * 
    * @param asset
    * @param table
    * @param column
    *           the column for which the mapping needs to be obtained
    * @param mapMembers
    *           set this flag if the members of the column need to be mapped
    * @param mapMemberAsConcept
    *           set this flag if the members are to be mapped to concepts instead of instances
    * @param create
    *           set this flag if the algorithm needs to create the matching concepts or instances, if the flag is not
    *           set then null is returned when no match is found. If the flag is set, and if there are no matched found
    *           then the normalized name of the column name will be shown as the suggested name of the concept to which
    *           the column can be mapped
    * @return
    */
   public ColumnMapping mapSDX2KDXForColumn (Asset asset, Tabl table, Colum column, boolean mapMembers,
            boolean mapMemberAsConcept, boolean create, Long startCounter, Model model) throws SDX2KDXMappingException {
      ColumnMapping columnMapping = null;
      try {
         ModelGroup modelGroup = kdxRetrievalService.getPrimaryGroup(model.getId());
         Map<String, Integer> conceptsWithIndexMap = new HashMap<String, Integer>();
         List<Mapping> suggestedMappings = new ArrayList<Mapping>();
         // Based on the mapMembers flag, invoke the appropriate method
         if (mapMembers) {
            columnMapping = mapSDX2KDXForColumnAndMembers(asset, table, column, conceptsWithIndexMap,
                     suggestedMappings, mapMemberAsConcept, create, startCounter, model, modelGroup);
         } else {
            columnMapping = mapSDX2KDXForColumn(asset, table, column, conceptsWithIndexMap, suggestedMappings, create,
                     startCounter, model, modelGroup);
         }
      } catch (Exception e) {
         e.printStackTrace();
         throw new SDX2KDXMappingException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e.getCause());
      }
      return columnMapping;
   }

   /**
    * The column and its members, if any, in a particular table, would be mapped to the matching concepts or instances
    * 
    * @param asset
    * @param table
    * @param column
    * @param member
    *           the member for which the mapping needs to be obtained
    * @param memberIndex
    *           the index of the member in the whole list of members of its column
    * @param mapMemberAsConcept
    *           set this flag if the members are to be mapped to concepts instead of instances
    * @param create
    *           set this flag if the algorithm needs to create the matching concepts or instances, if the flag is not
    *           set then null is returned when no match is found. If the flag is set, and if there are no matched found
    *           then the normalized name of the column name will be shown as the suggested name of the concept to which
    *           the column can be mapped
    * @return
    */
   public MemberMapping mapSDX2KDXForMember (Asset asset, Tabl table, Colum column, Membr member, int memberIndex,
            boolean mapMemberAsConcept, boolean create, Long startCounter, Model model) throws SDX2KDXMappingException {
      MemberMapping memberMapping = null;
      try {
         ModelGroup modelGroup = kdxRetrievalService.getPrimaryGroup(model.getId());
         Map<String, Integer> conceptsWithIndexMap = new HashMap<String, Integer>();
         List<Mapping> suggestedMappings = new ArrayList<Mapping>();
         // Get the AED of the member
         AssetEntityDefinition memberAED = getAED(asset, table, column, member);
         // Get the AED of the column of the member
         AssetEntityDefinition columnAED = getAED(asset, table, column, null);
         if (columnAED != null) {
            List<BusinessEntityDefinition> conceptDEDs = getMappedDEDs(columnAED, suggestedMappings);
            if (ExecueCoreUtil.isCollectionNotEmpty(conceptDEDs)) {
               memberMapping = mapSDX2KDXForMember(memberAED, conceptDEDs, memberIndex, conceptsWithIndexMap,
                        suggestedMappings, mapMemberAsConcept, create, startCounter, model, modelGroup);
            } else {
               // There is no concept mapped to the column of the member. First map the column to a concept to perform
               // this action
               throw new SDX2KDXMappingException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE,
                        "No mapping exists for the column '" + column.getName()
                                 + "', cannot deduce mappings for its members!");
            }
         }
      } catch (KDXException e) {
         e.printStackTrace();
      }
      return memberMapping;
   }

   public ColumnMapping mapSDX2KDXForColumnMembers (Asset asset, Tabl table, Colum column, List<Membr> members,
            BusinessEntityDefinition conceptBED, boolean mapMemberAsConcept, boolean create, Long startCounter,
            Model model) throws SDX2KDXMappingException {
      ColumnMapping columnMapping = new ColumnMapping();
      List<Mapping> suggestedMappings = new ArrayList<Mapping>();
      List<MemberMapping> memberMappings = new ArrayList<MemberMapping>();
      try {
         ModelGroup modelGroup = kdxRetrievalService.getPrimaryGroup(model.getId());
         Map<String, Integer> conceptsWithIndexMap = new HashMap<String, Integer>();
         List<BusinessEntityDefinition> conceptDEDs = new ArrayList<BusinessEntityDefinition>();
         conceptDEDs.add(conceptBED);

         int memberIndex = 0;
         for (Membr member : members) {
            // Get the AED of the member
            AssetEntityDefinition memberAED = getAED(asset, table, column, member);
            MemberMapping memberMapping = mapSDX2KDXForMember(memberAED, conceptDEDs, memberIndex++,
                     conceptsWithIndexMap, suggestedMappings, mapMemberAsConcept, create, startCounter++, model,
                     modelGroup);
            memberMappings.add(memberMapping);
         }
         columnMapping.setMemberMappings(memberMappings);
      } catch (KDXException e) {
         e.printStackTrace();
      }
      return columnMapping;
   }

   public ColumnMapping mapSDX2KDXForUserModelColumnMembers (Asset asset, Tabl table, Colum column,
            List<Membr> members, BusinessEntityDefinition conceptBED, boolean mapMemberAsConcept, boolean create,
            Long startCounter, Model model) throws SDX2KDXMappingException {
      ColumnMapping columnMapping = new ColumnMapping();
      List<Mapping> suggestedMappings = new ArrayList<Mapping>();
      List<MemberMapping> memberMappings = new ArrayList<MemberMapping>();
      try {
         ModelGroup modelGroup = kdxRetrievalService.getPrimaryGroup(model.getId());
         Map<String, Integer> conceptsWithIndexMap = new HashMap<String, Integer>();
         List<BusinessEntityDefinition> conceptDEDs = new ArrayList<BusinessEntityDefinition>();
         conceptDEDs.add(conceptBED);

         int memberIndex = 0;
         for (Membr member : members) {
            // Get the AED of the member
            AssetEntityDefinition memberAED = getAED(asset, table, column, member);
            MemberMapping memberMapping = mapSDX2KDXForMember(memberAED, conceptDEDs, memberIndex++,
                     conceptsWithIndexMap, suggestedMappings, mapMemberAsConcept, create, startCounter++, model,
                     modelGroup);
            memberMappings.add(memberMapping);
         }
         columnMapping.setMemberMappings(memberMappings);
      } catch (KDXException e) {
         e.printStackTrace();
      }
      return columnMapping;
   }

   public List<Long> mapMembersForAssetSyncUpProcess (Asset asset, Tabl table, Colum column, List<Membr> members,
            BusinessEntityDefinition conceptDED, Model model) throws SDX2KDXMappingException {
      List<Long> freshlyCreatedInstanceDED = new ArrayList<Long>();
      try {
         ModelGroup modelGroup = kdxRetrievalService.getPrimaryGroup(model.getId());
         List<MemberMapping> memberMappings = new ArrayList<MemberMapping>();
         Map<String, Integer> conceptsWithIndexMap = new HashMap<String, Integer>();
         List<Mapping> suggestedMappings = new ArrayList<Mapping>();
         List<BusinessEntityDefinition> conceptDEDs = new ArrayList<BusinessEntityDefinition>();
         conceptDEDs.add(conceptDED);

         // this will be the index appended to instance name
         int memberIndex = 0;
         Concept concept = conceptDED.getConcept();
         Instance latestInstanceInserted = kdxRetrievalService
                  .getLatestInstanceInserted(model.getId(), concept.getId());
         if (latestInstanceInserted != null) {
            String leftIndex = latestInstanceInserted.getName().replace(concept.getName(), "");
            memberIndex = Integer.parseInt(leftIndex) + 1;
         }
         // this will be used as ded id if we didn't find a matched ded id from swi
         Long startCounter = -99999999L;
         for (Membr member : members) {
            // Get the AED of the member
            AssetEntityDefinition memberAED = getAED(asset, table, column, member);
            MemberMapping memberMapping = mapSDX2KDXForMember(memberAED, conceptDEDs, memberIndex++,
                     conceptsWithIndexMap, suggestedMappings, false, true, startCounter++, model, modelGroup);
            memberMappings.add(memberMapping);
         }
         List<Mapping> mappings = new ArrayList<Mapping>();
         for (MemberMapping memberMapping : memberMappings) {
            Mapping mapping = new Mapping();
            mapping.setAssetEntityDefinition(memberMapping.getAssetEntityDefinition());
            mapping.setBusinessEntityDefinition(memberMapping.getBusinessEntityDefinition());
            BusinessEntityDefinition suggestedInstanceBED = memberMapping.getBusinessEntityDefinition();
            // if (domainEntityDefinition.getId().longValue() == startCounter.longValue()) {
            if (suggestedInstanceBED.getId().longValue() < 0) {
               // means fresh creation
               Instance instance = suggestedInstanceBED.getInstance();
               instance.setId(null);
               getBusinessEntityManagementWrapperService().createInstance(model.getId(), concept.getId(), instance);
               BusinessEntityDefinition swiExistingBED = kdxRetrievalService.getBusinessEntityDefinitionByIds(model
                        .getId(), concept.getId(), instance.getId());
               mapping.setBusinessEntityDefinition(swiExistingBED);
               freshlyCreatedInstanceDED.add(swiExistingBED.getId());
            }
            mappings.add(mapping);
         }
         getMappingManagementService().createMappings(mappings);
      } catch (KDXException e) {
         e.printStackTrace();
      } catch (MappingException e) {
         e.printStackTrace();
      } catch (PlatformException e) {
         e.printStackTrace();
      }
      return freshlyCreatedInstanceDED;
   }

   public List<Long> mapColumnsForAssetSyncUpProcess (Asset asset, Tabl table, List<Colum> columns, Model model)
            throws SDX2KDXMappingException {
      List<Long> freshlyCreatedConceptDED = new ArrayList<Long>();
      try {
         ModelGroup modelGroup = kdxRetrievalService.getPrimaryGroup(model.getId());
         Map<String, Integer> conceptsWithIndexMap = new HashMap<String, Integer>();
         List<Mapping> suggestedMappings = new ArrayList<Mapping>();
         List<ColumnMapping> columnMappings = new ArrayList<ColumnMapping>();
         Long startCounter = -11111L;
         for (Colum column : columns) {
            if (!LookupType.None.equals(table.getLookupType()) && ColumnType.NULL.equals(column.getKdxDataType())) {
               continue;
            }// On lookup table, only lookup columns needs to be mapped
            ColumnMapping columnMapping = mapSDX2KDXForColumn(asset, table, column, conceptsWithIndexMap,
                     suggestedMappings, true, startCounter, model, modelGroup);
            columnMappings.add(columnMapping);
         }
         List<Mapping> mappings = new ArrayList<Mapping>();
         for (ColumnMapping columnMapping : columnMappings) {
            Mapping mapping = new Mapping();
            mapping.setAssetEntityDefinition(columnMapping.getAssetEntityDefinition());
            mapping.setBusinessEntityDefinition(columnMapping.getBusinessEntityDefinition());
            BusinessEntityDefinition businessEntityDefinition = columnMapping.getBusinessEntityDefinition();
            // Check the BED and if the id is positive it means that the BED exists
            if (businessEntityDefinition.getId() > 0) {
               mappings.add(mapping);
            } else { // negative id indicates that the BED is dummy and we need to create the concept to get actual BED
               // INFO : Commented below block since it is redundant to check again for a match after suggestion process
               // BusinessEntityDefinition existingBusinessEntityDefinition = kdxRetrievalService
               // .getBusinessEntityDefinitionByNames(model.getName(), businessEntityDefinition.getConcept()
               // .getName(), null);
               // if (existingBusinessEntityDefinition != null) {
               // mapping.setBusinessEntityDefinition(existingBusinessEntityDefinition);
               // mappings.add(mapping);
               // } else {
               // means fresh creation
               Concept concept = businessEntityDefinition.getConcept();
               // This check, if newly created concept display name is same as system variable, will add random
               // number since we can not return any error from simplified path application creation path
               if (getBaseKDXRetrievalService().isSystemVariableExist(concept.getDisplayName())) {
                  concept.setDisplayName(concept.getDisplayName() + "_" + RandomStringUtils.randomNumeric(2));
               }
               concept.setId(null);
               BusinessEntityDefinition swiExistingBED = getBusinessEntityManagementWrapperService().createConcept(
                        model.getId(), concept);
               freshlyCreatedConceptDED.add(swiExistingBED.getId());
               mapping.setBusinessEntityDefinition(swiExistingBED);
               mappings.add(mapping);
               // }
            }
         }
         getMappingManagementService().createMappings(mappings);
      } catch (Exception e) {
         throw new SDX2KDXMappingException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE,
                  "Failed in mapColumnsForAssetSyncUpProcess method");
      }
      return freshlyCreatedConceptDED;
   }

   private TableMapping mapSDX2KDXForTable (Asset asset, Tabl table, Map<String, Integer> conceptsWithIndexMap,
            List<Mapping> suggestedMappings, boolean mapMembers, boolean mapMemberAsConcept, boolean create,
            Long startCounter, Model model, ModelGroup modelGroup) throws SDX2KDXMappingException {
      TableMapping tableMapping = new TableMapping();
      List<ColumnMapping> columnMappings = new ArrayList<ColumnMapping>();
      logger.debug("Table : " + table.getName());

      // Iterate through the Tables and get the list of Columns for each Table
      List<Colum> columns;
      try {
         // Check if the table is of simple lookup type
         LookupType lookupType = table.getLookupType();
         if (LookupType.SIMPLE_LOOKUP.equals(lookupType)) {
            // Just map the lookup value column and ignore the rest of the columns of the table
            String lookupValueColumnName = table.getLookupValueColumn();
            logger.debug("This table is of SL type, hence obtaining mapping only for " + lookupValueColumnName);
            List<Colum> cols = getSdxRetrievalService().getColumnsOfTable(table);
            columns = new ArrayList<Colum>();
            for (Colum column : cols) {
               if (column.getName().equals(lookupValueColumnName)) {
                  columns.add(column);
                  break;
               }
            }
         } else {
            columns = getSdxRetrievalService().getColumnsOfTable(table);
         }
         // Iterate through the columns list and generate mapping for each column based on the mapMembers flag
         for (Colum column : columns) {
            ColumnMapping columnMapping = null;
            if (mapMembers) {
               columnMapping = mapSDX2KDXForColumnAndMembers(asset, table, column, conceptsWithIndexMap,
                        suggestedMappings, mapMemberAsConcept, create, startCounter++, model, modelGroup);
            } else {
               columnMapping = mapSDX2KDXForColumn(asset, table, column, conceptsWithIndexMap, suggestedMappings,
                        create, startCounter++, model, modelGroup);
            }
            if (columnMapping != null) {
               columnMappings.add(columnMapping);
            }
         }
         tableMapping.setColumnMappings(columnMappings);
      } catch (SDXException e) {
         e.printStackTrace();
         throw new SDX2KDXMappingException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e.getCause());
      }
      for (ColumnMapping columnMapping : tableMapping.getColumnMappings()) {
         if (columnMapping != null) {
            columnMapping.getAssetEntityDefinition().getTabl().getName();
            logger.debug("Mapping for COLUMN : " + columnMapping.getAssetEntityDefinition().getColum().getName());
            logger.debug("Concept -> " + columnMapping.getBusinessEntityDefinition().getConcept().getName());
         }
      }
      return tableMapping;
   }

   /**
    * This method maps the column to the matching concept and does not map the members, if any, of the column.
    * 
    * @param asset
    * @param table
    * @param column
    * @param conceptsWithIndexMap
    * @param suggestedMappings
    * @param create
    * @return the list containing the mappings. The map contains the AED of the column as the key and the list of
    *         deduced DEDs as the value
    */
   private ColumnMapping mapSDX2KDXForColumn (Asset asset, Tabl table, Colum column,
            Map<String, Integer> conceptsWithIndexMap, List<Mapping> suggestedMappings, boolean create,
            Long startCounter, Model model, ModelGroup modelGroup) {
      ColumnMapping columnMapping = null;
      List<BusinessEntityDefinition> suggestedDEDs = new ArrayList<BusinessEntityDefinition>();
      AssetEntityDefinition columnAED = getAED(asset, table, column, null);
      // touch the AED for lazy loading
      columnAED.getTabl().getName();
      columnAED.getColum().getName();
      columnAED.getAsset().getName();

      logger.debug("AED for " + column.getName() + " : " + columnAED.getId());
      // Try to get the mapped DED for the AED
      List<BusinessEntityDefinition> conceptDEDs = getMappedDEDs(columnAED, suggestedMappings);
      // proceed to map this column only if there is no mapped DED
      if (ExecueCoreUtil.isCollectionNotEmpty(conceptDEDs)) {
         // This column has been already mapped, return null
         // suggestedDEDs.addAll(conceptDEDs);
      } else {
         suggestedDEDs = deduce(columnAED, conceptsWithIndexMap, suggestedMappings, create, startCounter, model,
                  modelGroup);
         if (ExecueCoreUtil.isCollectionNotEmpty(suggestedDEDs)) {
            BusinessEntityDefinition suggestedBED = suggestedDEDs.get(0);
            // touch the DED for lazy loading
            suggestedBED.getModelGroup().getName();
            suggestedBED.getConcept().getName();

            columnMapping = new ColumnMapping();
            // colMapping.put(columnAED, suggestedDEDs);
            // columnMapping.add(colMapping);
            columnMapping.setAssetEntityDefinition(columnAED);
            columnMapping.setBusinessEntityDefinition(suggestedBED);
         }
      }
      return columnMapping;
   }

   /**
    * This method will map the members of the column if any along with the column
    * 
    * @param asset
    * @param table
    * @param column
    * @param conceptsWithIndexMap
    * @param suggestedMappings
    * @param mapMemberAsConcept
    * @param create
    * @return the list containing the mappings for the column along with the members. The first element in this list is
    *         that of the column and the rest are of its members. <BR>
    *         In the case of a column, the map contains the AED of the column as the key and the list of deduced DEDs as
    *         the value and in the case of a member, the map contains the AED of the member as the key and the list of
    *         deduced DEDs as the value
    */
   private ColumnMapping mapSDX2KDXForColumnAndMembers (Asset asset, Tabl table, Colum column,
            Map<String, Integer> conceptsWithIndexMap, List<Mapping> suggestedMappings, boolean mapMemberAsConcept,
            boolean create, Long startCounter, Model model, ModelGroup modelGroup) throws SDX2KDXMappingException {
      ColumnMapping columnMapping = null;
      List<MemberMapping> memberMappings = new ArrayList<MemberMapping>();
      columnMapping = mapSDX2KDXForColumn(asset, table, column, conceptsWithIndexMap, suggestedMappings, create,
               startCounter, model, modelGroup);
      if (columnMapping != null) {
         // Get the AED for the column to get the mapped DED for it
         AssetEntityDefinition columnAED = getAED(asset, table, column, null);
         logger.debug("AED for " + column.getName() + " : " + columnAED.getId());
         // Try to get the mapped DED for the AED
         List<BusinessEntityDefinition> conceptDEDs = getMappedDEDs(columnAED, suggestedMappings);
         // Check if the column has any members
         List<Membr> members = getMembersForColumn(column);
         int index = 1;

         for (Membr member : members) {
            // Get the AED of the member
            AssetEntityDefinition memberAED = getAED(asset, table, column, member);
            // lazy loading
            Membr m = memberAED.getMembr();
            m.getLookupDescription();
            MemberMapping memberMapping = mapSDX2KDXForMember(memberAED, conceptDEDs, index++, conceptsWithIndexMap,
                     suggestedMappings, mapMemberAsConcept, create, startCounter++, model, modelGroup);
            memberMappings.add(memberMapping);
         }
         columnMapping.setMemberMappings(memberMappings);
      }
      return columnMapping;
   }

   /**
    * This method gets the suggested mapping for a member
    * 
    * @param memberAED
    * @param conceptDEDs
    * @param memberIndex
    * @param conceptsWithIndexMap
    * @param suggestedMappings
    * @param mapMemberAsConcept
    * @param create
    * @param model
    * @return The map containing the mapping for the member. The map contains the AED of the member as the key and the
    *         list of suggested/deduced DEDs as the value
    */
   private MemberMapping mapSDX2KDXForMember (AssetEntityDefinition memberAED,
            List<BusinessEntityDefinition> conceptDEDs, int memberIndex, Map<String, Integer> conceptsWithIndexMap,
            List<Mapping> suggestedMappings, boolean mapMemberAsConcept, boolean create, Long startCounter,
            Model model, ModelGroup primaryGroup) throws SDX2KDXMappingException {
      MemberMapping mMapping = null;
      List<BusinessEntityDefinition> suggestedDEDs = new ArrayList<BusinessEntityDefinition>();
      BusinessEntityDefinition memberBED = null;
      List<BusinessEntityDefinition> memberDEDs = getMappedDEDs(memberAED, suggestedMappings);
      try {
         if (ExecueCoreUtil.isCollectionEmpty(memberDEDs)) {
            String lookupDesc = memberAED.getMembr().getLookupDescription();
            // For handling special conditions, where the member needs to be mapped to a concept and not to an instance
            if (mapMemberAsConcept) {
               // First get all the concepts in the domain which match the lookup description of the member
               List<Concept> matchingConcepts = getMatchingConceptsByName(model, lookupDesc);
               if (ExecueCoreUtil.isCollectionNotEmpty(matchingConcepts)) {
                  // TODO: Filter the list of concepts obtained to retain those which match the lookup description by
                  // some algorithm(direct match is in use now)
                  // After evaluation send the most suitable concepts as a list(so the best case is to get only one
                  // concept in the list)
                  matchingConcepts = evaluateConcepts(memberAED, matchingConcepts);
                  String conceptName = matchingConcepts.get(0).getName();
                  BusinessEntityDefinition matchingConceptBED;
                  matchingConceptBED = kdxRetrievalService.getBusinessEntityDefinitionByIds(model.getId(),
                           matchingConcepts.get(0).getId(), null);
                  memberBED = matchingConceptBED;
                  if (memberBED == null && create) {
                     conceptName = appendIndexToConceptName(model, conceptName, conceptsWithIndexMap);
                     Concept concept = new Concept();
                     concept.setId(SUGGESTED_ENTITY_ID);
                     concept.setName(conceptName);
                     // name needs to be separated by camel case
                     concept.setDisplayName(ExecueStringUtil.getUncompactedDisplayString(conceptName));
                     memberBED = createBusinessEntityDefinition(primaryGroup, concept, null, startCounter);
                  }
                  // Add the mapping for the AED and DED of the member
                  addMapping(memberAED, memberBED, suggestedMappings);
                  suggestedDEDs.add(memberBED);
                  mMapping = new MemberMapping();
                  mMapping.setAssetEntityDefinition(memberAED);
                  mMapping.setBusinessEntityDefinition(suggestedDEDs.get(0));
               }
            } else { // normal case - the members are mapped to the instances
               BusinessEntityDefinition mappedConceptDED = conceptDEDs.get(0);
               Concept concept = mappedConceptDED.getConcept();
               boolean isSharedDataType = true;
               // Check if we can find a match in the shared models
               memberBED = getMatchingInstanceFromSharedModel(primaryGroup, mappedConceptDED, lookupDesc, startCounter);
               if (memberBED == null) {
                  // returns any matching BED of unmapped instance which matches the member
                  memberBED = getMappingRetrievalService().getMatchedInstanceBedByMemberLookupDescription(
                           model.getId(), concept, lookupDesc);
                  isSharedDataType = false;
               }
               // If there are no matches and if the create flag is true, then create the instances by using the
               // member's lookup description
               if (memberBED == null && create) {
                  // for (BusinessEntityDefinition conceptDED : conceptDEDs) {
                  String instanceName = concept.getName() + memberIndex;
                  // create instance for the member
                  Instance instance = new Instance();
                  instance.setId(SUGGESTED_ENTITY_ID);
                  instance.setName(instanceName);
                  instance.setDescription(lookupDesc);
                  instance.setDisplayName(lookupDesc);
                  memberBED = createBusinessEntityDefinition(primaryGroup, concept, instance, startCounter);
                  logger.debug("no matching instance found, created a new instance : ded id : " + memberBED.getId());
               }
               if (memberBED != null) {
                  // Add the mapping for the AED and DED of the member
                  addMapping(memberAED, memberBED, suggestedMappings);
                  suggestedDEDs.add(memberBED);
                  mMapping = new MemberMapping();
                  mMapping.setAssetEntityDefinition(memberAED);
                  logger.debug("Setting DED " + suggestedDEDs.get(0).getId() + " for AED " + memberAED.getId());
                  mMapping.setBusinessEntityDefinition(suggestedDEDs.get(0));
                  mMapping.setSuggestedMappingFromShared(isSharedDataType);
               }
            }
         } else {
            logger.debug("Member [" + memberAED.getId() + "] already mapped");
            // Member already mapped, return null
         }
      } catch (KDXException kdxException) {
         kdxException.printStackTrace();
         throw new SDX2KDXMappingException(SWIExceptionCodes.ENTITY_RETRIEVAL_FAILED, kdxException.getCause());
      } catch (MappingException mappingException) {
         mappingException.printStackTrace();
         throw new SDX2KDXMappingException(SWIExceptionCodes.ENTITY_RETRIEVAL_FAILED, mappingException.getCause());
      }
      return mMapping;
   }

   private BusinessEntityDefinition getMatchingInstanceFromSharedModel (ModelGroup primaryGroup,
            BusinessEntityDefinition mappedConceptDED, String lookupDesc, Long startCounter) throws KDXException {
      BusinessEntityDefinition memberBED = null;
      // check if the concept's type is from shared data
      BusinessEntityDefinition typeBED = getKdxRetrievalService()
               .getTypeBedByTypeID(mappedConceptDED.getType().getId());
      Long sharedModelGroupdId = getSharedDataModelGroupId(typeBED);
      if (sharedModelGroupdId != null) {
         Instance match = checkForMatchesInSharedModel(lookupDesc, typeBED, sharedModelGroupdId);
         if (match != null) {
            memberBED = createBusinessEntityDefinition(primaryGroup, mappedConceptDED.getConcept(), match, startCounter);
         }
      }
      return memberBED;
   }

   // TODO: -JM- make a service call which will retrieve a match from the shared model
   private Instance checkForMatchesInSharedModel (String lookupDesc, BusinessEntityDefinition typeBED,
            Long sharedModelGroupdId) {
      return null;
   }

   /**
    * This method checks if the type BED is part of any
    */
   private Long getSharedDataModelGroupId (BusinessEntityDefinition typeBED) throws KDXException {
      Long sharedDataModelGroupId = null;
      ModelGroup sharedModelGroup = getKdxRetrievalService().getModelGroupByTypeBedId(typeBED.getId());
      if (sharedModelGroup != null) {
         sharedDataModelGroupId = sharedModelGroup.getId();
      }
      return sharedDataModelGroupId;
   }

   public void createBatchProcess (InstanceAbsorptionContext instanceAbsorptionContext) throws SDX2KDXMappingException {
      JobOperationalStatus jobOperationalStatus = null;
      try {
         JobRequest jobRequest = instanceAbsorptionContext.getJobRequest();

         jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest,
                  "Adding records to BatchProcess Tables", JobStatus.INPROGRESS, null, new Date());
         jobDataService.createJobOperationStatus(jobOperationalStatus);
         Asset asset = getSdxRetrievalService().getAsset(instanceAbsorptionContext.getAssetId());
         BatchProcess batchProcess = new BatchProcess();
         BatchProcessDetail batchProcessDetail = new BatchProcessDetail();
         Set<BatchProcessDetail> batchProcessDetails = new HashSet<BatchProcessDetail>();
         batchProcess.setApplicationId(asset.getApplication().getId());
         batchProcess.setAssetId(asset.getId());
         batchProcess.setModelId(instanceAbsorptionContext.getModelId());
         batchProcess.setBatchType(BatchType.INSTANCE_ABSORPTION);
         batchProcess.setJobRequestId(instanceAbsorptionContext.getJobRequest().getId());
         batchProcessDetail.setParamName(BatchProcessDetailType.COLUMN);
         batchProcessDetail.setParamValue(instanceAbsorptionContext.getColumnAedId().toString());
         batchProcessDetails.add(batchProcessDetail);
         batchProcess.setBatchProcessDetails(batchProcessDetails);
         batchProcessDetail.setBatchProcess(batchProcess);
         getBatchMaintenanceService().createBatchProcess(batchProcess);

         jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                  JobStatus.SUCCESS, null, new Date());
         jobDataService.updateJobOperationStatus(jobOperationalStatus);

      } catch (Exception e) {
         if (jobOperationalStatus != null) {
            ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus, JobStatus.FAILURE,
                     e.getMessage(), new Date());
            try {
               getJobDataService().updateJobOperationStatus(jobOperationalStatus);
            } catch (QueryDataException e1) {
               throw new SDX2KDXMappingException(SWIExceptionCodes.BATCH_PROCESS_CREATION_FAILED, e1);
            }
         }
         e.printStackTrace();
         throw new SDX2KDXMappingException(SWIExceptionCodes.BATCH_PROCESS_CREATION_FAILED, e);
      }
   }

   public void deleteBatchProcess (InstanceAbsorptionContext instanceAbsorptionContext) throws SDX2KDXMappingException {
      // TODO - PSNM- Should handle delete thru cascade
      JobOperationalStatus jobOperationalStatus = null;
      try {
         JobRequest jobRequest = instanceAbsorptionContext.getJobRequest();

         jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest,
                  "Deleting records from BatchProcess Tables on Successfull Completion", JobStatus.INPROGRESS, null,
                  new Date());
         jobDataService.createJobOperationStatus(jobOperationalStatus);
         Asset asset = getSdxRetrievalService().getAsset(instanceAbsorptionContext.getAssetId());
         BatchProcess batchProcess = getBatchMaintenanceService().getBatchProcessByIds(asset.getApplication().getId(),
                  instanceAbsorptionContext.getAssetId(), null, BatchType.INSTANCE_ABSORPTION,
                  BatchProcessDetailType.COLUMN, instanceAbsorptionContext.getColumnAedId().toString());
         Set<BatchProcessDetail> batchProcessDetails = batchProcess.getBatchProcessDetails();
         for (BatchProcessDetail batchProcessDetail : batchProcessDetails) {
            getBatchMaintenanceService().deleteBatchProcessDetail(batchProcessDetail);
         }
         getBatchMaintenanceService().deleteBatchProcess(batchProcess);
         jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                  JobStatus.SUCCESS, null, new Date());
         jobDataService.updateJobOperationStatus(jobOperationalStatus);
      } catch (Exception e) {
         if (jobOperationalStatus != null) {
            ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus, JobStatus.FAILURE,
                     e.getMessage(), new Date());
            try {
               getJobDataService().updateJobOperationStatus(jobOperationalStatus);
            } catch (QueryDataException e1) {
               throw new SDX2KDXMappingException(SWIExceptionCodes.BATCH_PROCESS_CREATION_FAILED, e1);
            }
         }
         e.printStackTrace();
         throw new SDX2KDXMappingException(SWIExceptionCodes.BATCH_PROCESS_CREATION_FAILED, e);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.swi.ISDX2KDXMappingService#saveInstanceMappingSuggestions(com.execue.core.common.bean.batchMaintenance.InstanceAbsorptionContext)
    */
   public void saveInstanceMappingSuggestions (InstanceAbsorptionContext instanceAbsorptionContext)
            throws SDX2KDXMappingException {
      Long columnAedId = instanceAbsorptionContext.getColumnAedId();
      Long modelId = instanceAbsorptionContext.getModelId();
      JobOperationalStatus jobOperationalStatus = null;
      JobRequest jobRequest = instanceAbsorptionContext.getJobRequest();
      try {
         Long suggestionCount = getInstanceMappingSuggestionService().getInstanceMappingSuggestionDetailsCount(
                  columnAedId);
         int batchSize = getCoreConfigurationService().getKDXInstanceMappingBatchSize();
         int noOfBatches = (suggestionCount.intValue() / batchSize);
         int rmndr = suggestionCount.intValue() % batchSize;
         if (rmndr != 0) {
            noOfBatches++;
         }
         // get the suggestion object
         InstanceMappingSuggestion instanceMappingSuggestion = getInstanceMappingSuggestionService()
                  .getInstanceMappingSuggestion(columnAedId);
         List<InstanceMappingSuggestionDetail> suggestionsBatch = null;
         int fromCounter = 1;
         int toCounter = 0;
         for (int batchNum = 1; batchNum <= noOfBatches; batchNum++) {

            suggestionsBatch = getInstanceMappingSuggestionService().getInstanceMappingSuggestionDetailsByBatchAndSize(
                     columnAedId, new Long(1), new Long(batchSize));
            toCounter = batchNum * batchSize;
            if (jobRequest != null) {
               jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest, "Absorbing ["
                        + fromCounter + ".." + toCounter + "] instance_mapping_suggestion into mappings in swi",
                        JobStatus.INPROGRESS, null, new Date());
               jobDataService.createJobOperationStatus(jobOperationalStatus);
            }
            fromCounter = toCounter + 1;
            saveBatchMappings(instanceMappingSuggestion, suggestionsBatch, modelId);

            if (jobRequest != null) {
               jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                        JobStatus.SUCCESS, null, new Date());
               jobDataService.updateJobOperationStatus(jobOperationalStatus);
            }
         }

         // finally delete the InstanceMappingSuggestion of the columnAEDId
         getInstanceMappingSuggestionService().deleteInstanceMappingSuggestionByColumnAEDId(columnAedId);
      } catch (Exception e) {
         if (jobOperationalStatus != null) {
            ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus, JobStatus.FAILURE,
                     e.getMessage(), new Date());
            try {
               getJobDataService().updateJobOperationStatus(jobOperationalStatus);
            } catch (QueryDataException e1) {
               throw new SDX2KDXMappingException(SWIExceptionCodes.BATCH_PROCESS_CREATION_FAILED, e1);
            }
         }
         throw new SDX2KDXMappingException(SWIExceptionCodes.BATCH_PROCESS_CREATION_FAILED, e);
      }
   }

   private BusinessEntityDefinition createInstance (String conceptDisplayName, String instanceDisplayName, Long modelId) {
      BusinessEntityDefinition instanceBED = null;
      // BusinessEntityDefinition mappedInstanceBED = null;
      // // TODO: -JVK- ensure that the code value SWIExceptionCodes.ENTITY_ALREADY_EXISTS is in sync with this value
      // int ENTITY_ALREADY_EXISTS = 900018;
      // Instance newInstance = new Instance();
      // newInstance.setDisplayName(instanceDisplayName);
      // Concept concept = null;
      // try {
      // concept = getKdxRetrievalService().getConceptByDisplayName(modelId, conceptDisplayName);
      // } catch (KDXException e) {
      // e.printStackTrace();
      // }
      // try {
      // getKdxManagementService().createInstance(modelId, concept.getId(), newInstance);
      // } catch (KDXException e) {
      // if (e.getCode() == ENTITY_ALREADY_EXISTS) {
      // try {// get the BED
      // Model model = getKdxRetrievalService().getModelById(modelId);
      // mappedInstanceBED = getKdxRetrievalService().getBEDByInstanceDisplayName(model.getName(),
      // conceptDisplayName, instanceDisplayName);
      // } catch (KDXException e1) {
      // e1.printStackTrace();
      // }
      // }
      // }
      // if (mappedInstanceBED == null) {
      // try {
      // mappedInstanceBED = getKdxRetrievalService().getBusinessEntityDefinitionByIds(modelId, concept.getId(),
      // newInstance.getId());
      // } catch (KDXException e) {
      // e.printStackTrace();
      // }
      // }
      try {
         Model model = getKdxRetrievalService().getModelById(modelId);
         Concept concept = getKdxRetrievalService().getConceptByDisplayName(modelId, conceptDisplayName);
         instanceBED = getKdxRetrievalService().getBEDByInstanceDisplayName(model.getName(), concept.getName(),
                  instanceDisplayName);
         if (instanceBED == null) {
            Instance newInstance = new Instance();
            newInstance.setDisplayName(instanceDisplayName);
            getBusinessEntityManagementWrapperService().createInstance(model.getId(), concept.getId(), newInstance);
            instanceBED = getKdxRetrievalService().getBusinessEntityDefinitionByIds(modelId, concept.getId(),
                     newInstance.getId());
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
      return instanceBED;
   }

   public void saveBatchMappings (InstanceMappingSuggestion instanceMappingSuggestion,
            List<InstanceMappingSuggestionDetail> suggestionsBatch, Long modelId) throws SDX2KDXMappingException {
      List<Long> toBeDeletedSuggestedMappingIds = new ArrayList<Long>();
      List<Mapping> mappings = new ArrayList<Mapping>();
      BusinessEntityDefinition instanceBED = null;
      try {
         for (InstanceMappingSuggestionDetail suggestion : suggestionsBatch) {
            Mapping mapping = new Mapping();
            AssetEntityDefinition memberAED;
            memberAED = getSdxRetrievalService().getAssetEntityDefinitionById(suggestion.getMembrAEDId());
            if (CheckType.YES.equals(suggestion.getIsInstanceExists())) {
               instanceBED = getKdxRetrievalService().getBusinessEntityDefinitionById(suggestion.getInstanceBEDId());
            } else {
               instanceBED = createInstance(instanceMappingSuggestion.getConceptDisplayName(), suggestion
                        .getInstanceDisplayName(), modelId);
            }
            mapping.setAssetEntityDefinition(memberAED);
            mapping.setBusinessEntityDefinition(instanceBED);
            mappings.add(mapping);
            toBeDeletedSuggestedMappingIds.add(suggestion.getId());
         }
         // first create mappings
         getMappingManagementService().createMappings(mappings);
         // next delete suggestions
         getInstanceMappingSuggestionService().deleteInstanceMappingSuggestionDetails(toBeDeletedSuggestedMappingIds);
      } catch (Exception e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
         throw new SDX2KDXMappingException(SWIExceptionCodes.BATCH_PROCESS_CREATION_FAILED, e);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.swi.ISDX2KDXMappingService#generateInstanceMappingSuggestions(com.execue.core.common.bean.batchMaintenance.InstanceAbsorptionContext)
    */
   public String generateInstanceMappingSuggestions (InstanceAbsorptionContext instanceAbsorptionContext)
            throws SDX2KDXMappingException {

      Long modelId = instanceAbsorptionContext.getModelId();
      Long selColAedId = instanceAbsorptionContext.getColumnAedId();
      Long conBedId = instanceAbsorptionContext.getConceptBedId();

      JobOperationalStatus jobOperationalStatus = null;
      String statusMessage = "SUCCESS";

      try {

         InstanceMappingSuggestion instanceMappingSuggestion = null;
         BusinessEntityDefinition bed = null;
         String lookupDesc = null;
         String instanceName = null;
         Instance instance = null;
         AssetEntityDefinition memberAED = null;
         MemberMapping memberMapping = null;
         List<InstanceMappingSuggestionDetail> suggestedMappings = null;
         List<Membr> unmappedMembers = null;
         ModelGroup modelGroup = kdxRetrievalService.getPrimaryGroup(modelId);

         // First check if the concept to which the column is mapped has any instances.
         BusinessEntityDefinition mappedConceptBED = kdxRetrievalService.getBusinessEntityDefinitionById(conBedId);
         Concept concept = mappedConceptBED.getConcept();
         AssetEntityDefinition columnAED = getSdxRetrievalService().getAssetEntityDefinitionById(selColAedId);

         // 1. Find the count of unmapped members for the column
         Long unmappedMemberCount = getMappingRetrievalService().getUnmappedMemberCount(selColAedId);

         if (unmappedMemberCount > 0) {
            // calculate the number of batches
            int index = 1;
            int noOfBatches = 1;

            int batchSize = getCoreConfigurationService().getKDXInstanceMappingBatchSize();

            noOfBatches = (unmappedMemberCount.intValue() / batchSize);
            int rmndr = unmappedMemberCount.intValue() % batchSize;
            if (rmndr != 0) {
               noOfBatches++;
            }
            // save the InstanceMappingSuggestion object
            instanceMappingSuggestion = instanceMappingSuggestionService
                     .getInstanceMappingSuggestion(columnAED.getId());
            // TODO: -JVK- change this logic-the parent row can be there and also some of the suggestions in the
            // temp
            // table
            JobRequest jobRequest = instanceAbsorptionContext.getJobRequest();
            if (instanceMappingSuggestion == null) {
               instanceMappingSuggestion = constructInstanceMappingSuggestion(columnAED, mappedConceptBED, modelId);
               instanceMappingSuggestionService.createInstanceMappingSuggestion(instanceMappingSuggestion);

               // 2. Retrieve the unmapped members batch-wise and generate the suggestions
               int fromCounter = 1;
               int toCounter = 0;
               for (int batchNum = 1; batchNum <= noOfBatches; batchNum++) {

                  suggestedMappings = new ArrayList<InstanceMappingSuggestionDetail>();
                  unmappedMembers = getMappingRetrievalService().getUnmappedMembersByBatchAndSize(
                           columnAED.getColum().getId(), concept.getId(), new Long(batchNum), new Long(batchSize));
                  toCounter = batchNum * batchSize;
                  if (jobRequest != null) {
                     jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest,
                              "Absorbing [" + fromCounter + ".." + toCounter
                                       + "] of suggestions into instance_mapping_suggestion_detail in swi",
                              JobStatus.INPROGRESS, null, new Date());
                     jobDataService.createJobOperationStatus(jobOperationalStatus);
                  }
                  fromCounter = toCounter + 1;
                  for (Membr member : unmappedMembers) {

                     memberAED = getSdxRetrievalService().getAssetEntityDefinitionByIds(columnAED.getAsset(),
                              columnAED.getTabl(), columnAED.getColum(), member);
                     lookupDesc = member.getLookupDescription();
                     // First check if there are any instances belonging to the concept mapped to the column of
                     // the member, matching by the member lookup description
                     bed = getMappingRetrievalService().getMatchedInstanceBedByMemberLookupDescription(modelId,
                              concept, lookupDesc);
                     if (bed == null) {
                        bed = new BusinessEntityDefinition();
                        instanceName = concept.getName() + index;
                        // create instance for the member
                        instance = new Instance();
                        instance.setId(SUGGESTED_ENTITY_ID);
                        instance.setName(instanceName);
                        instance.setDescription(lookupDesc);
                        instance.setDisplayName(lookupDesc);
                        // create the BED
                        bed.setId(SUGGESTED_ENTITY_ID);
                        bed.setModelGroup(modelGroup);
                        bed.setConcept(concept);
                        bed.setInstance(instance);
                        index++;
                     }
                     memberMapping = new MemberMapping();
                     memberMapping.setAssetEntityDefinition(memberAED);
                     memberMapping.setBusinessEntityDefinition(bed);
                     suggestedMappings.add(constructInstanceMappingSuggestionDetail(instanceMappingSuggestion.getId(),
                              memberMapping));
                  }
                  // store the suggestions batch into the database
                  instanceMappingSuggestionService.createInstanceMappingSuggestionDetails(suggestedMappings);

                  if (jobRequest != null) {
                     jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                              JobStatus.SUCCESS, null, new Date());
                     jobDataService.updateJobOperationStatus(jobOperationalStatus);
                  }
               }
            } else {
               logger.error("The suggestions exist, need not run suggestion process..!!");
            }
         } else {
            statusMessage = "All members of the column have been mapped already";
         }
      } catch (Exception e) {
         if (jobOperationalStatus != null) {
            ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus, JobStatus.FAILURE,
                     e.getMessage(), new Date());
            try {
               getJobDataService().updateJobOperationStatus(jobOperationalStatus);
            } catch (QueryDataException e1) {
               throw new SDX2KDXMappingException(SWIExceptionCodes.BATCH_PROCESS_CREATION_FAILED, e1);
            }
         }
         e.printStackTrace();
         throw new SDX2KDXMappingException(SWIExceptionCodes.BATCH_PROCESS_CREATION_FAILED, e);
      }
      return statusMessage;
   }

   public Long getUnmappedMemberCount (Long columnAedId) throws SDX2KDXMappingException {
      try {
         return getMappingRetrievalService().getUnmappedMemberCount(columnAedId);
      } catch (MappingException e) {
         throw new SDX2KDXMappingException(e.getCode(), e.getCause());
      }
   }

   private InstanceMappingSuggestion constructInstanceMappingSuggestion (AssetEntityDefinition columnAED,
            BusinessEntityDefinition conceptBED, Long modelId) {
      InstanceMappingSuggestion instanceMappingSuggestion = new InstanceMappingSuggestion();
      instanceMappingSuggestion.setApplicationId(columnAED.getAsset().getApplication().getId());
      // check if Asset AED is required
      instanceMappingSuggestion.setAssetAEDId(SUGGESTED_ENTITY_ID);
      instanceMappingSuggestion.setColumAEDId(columnAED.getId());
      instanceMappingSuggestion.setColumDisplayName(columnAED.getColum().getName());
      instanceMappingSuggestion.setConceptBEDId(conceptBED.getId());
      instanceMappingSuggestion.setConceptDisplayName(conceptBED.getConcept().getDisplayName());
      instanceMappingSuggestion.setModelGroupId(conceptBED.getModelGroup().getId());
      instanceMappingSuggestion.setModelId(modelId);
      // check if Table AED is required
      instanceMappingSuggestion.setTablAEDId(SUGGESTED_ENTITY_ID);
      instanceMappingSuggestion.setTablDisplayName(columnAED.getTabl().getName());
      return instanceMappingSuggestion;
   }

   private InstanceMappingSuggestionDetail constructInstanceMappingSuggestionDetail (Long instanceMappingSuggestionId,
            MemberMapping memberMapping) {
      Instance instance = memberMapping.getBusinessEntityDefinition().getInstance();
      CheckType isInstanceExists = CheckType.NO;
      // figure out if the instance already exists by inspecting its ID value
      if (instance.getId() > 0) {
         isInstanceExists = CheckType.YES;
      }
      InstanceMappingSuggestionDetail instanceMappingSuggestionDetail = new InstanceMappingSuggestionDetail();
      instanceMappingSuggestionDetail.setInstanceBEDId(memberMapping.getBusinessEntityDefinition().getId());
      instanceMappingSuggestionDetail.setInstanceDescription(instance.getDescription());
      instanceMappingSuggestionDetail.setInstanceDisplayName(instance.getDisplayName());
      instanceMappingSuggestionDetail.setInstanceId(instance.getId());
      instanceMappingSuggestionDetail.setInstanceMappingSuggestionId(instanceMappingSuggestionId);
      instanceMappingSuggestionDetail.setIsInstanceExists(isInstanceExists);
      instanceMappingSuggestionDetail.setMembrAEDId(memberMapping.getAssetEntityDefinition().getId());
      instanceMappingSuggestionDetail.setMembrDisplayName(memberMapping.getAssetEntityDefinition().getMembr()
               .getLookupDescription());
      return instanceMappingSuggestionDetail;
   }

   /**
    * Returns the list of members if any for a column
    * 
    * @param column
    * @return
    */
   private List<Membr> getMembersForColumn (Colum column) {
      List<Membr> members = new ArrayList<Membr>();
      try {
         members = getSdxRetrievalService().getColumnMembers(column);
      } catch (SDXException e) {
         e.printStackTrace();
      }
      return members;
   }

   /**
    * This method tries to deduce the KDX entity for a column by exploring the relations the column has in the SWI and
    * will try to match the mapped KDX entities of the relations
    * 
    * @param aed
    * @return
    */
   private List<BusinessEntityDefinition> deduce (AssetEntityDefinition aed, Map<String, Integer> conceptsWithIndexMap,
            List<Mapping> suggestedMappings, boolean create, Long startCounter, Model model, ModelGroup modelGroup) {
      List<BusinessEntityDefinition> deducedDEDs = new ArrayList<BusinessEntityDefinition>();
      logger.debug("Trying to deduce the kdx entity, by relation for " + aed.getColum().getName());

      // Discard the lookup table description column from being suggested
      if (aed.getColum().getName().equalsIgnoreCase(aed.getTabl().getLookupDescColumn())) {
         return deducedDEDs;
      }

      // Try getting all the relations, if there are no relations then deduce by name
      List<AssetEntityDefinition> relations = getRelatedAEDs(aed);
      if (ExecueCoreUtil.isCollectionNotEmpty(relations)) {
         // There are columns which are having relationship with the current column, explore the possibility of
         // suggesting based on the relationship
         logger.debug("There are relations for " + aed.getColum().getName());
         // Try getting all the DEDs that are mapped to the relations
         for (AssetEntityDefinition relation : relations) {
            logger.debug("Relation AED id : " + relation.getId());
            deducedDEDs = getMappedDEDs(relation, suggestedMappings);
            if (ExecueCoreUtil.isCollectionNotEmpty(deducedDEDs)) {
               break;
            }
         }
         if (ExecueCoreUtil.isCollectionEmpty(deducedDEDs)) {
            // None of the relations have any mappings, need to deduce for the PK, so that all the other relatives can
            // be mapped to the same concept
            logger.debug("None of the relations have been mapped, trying to deduce by column name...");
            // If the current column is not of PK type, find its PK to deduce the mapping
            AssetEntityDefinition pkAED = null;
            try {
               // check if the current column is a PK or not
               if (getSdxRetrievalService().isPartOfPrimaryKey(aed.getColum().getId())) {
                  pkAED = aed;
               } else {
                  // if the current column is not a PK, get the PK
                  pkAED = getPrimaryKeyColumnAED(aed);
               }
            } catch (SDXException e) {
               e.printStackTrace();
            }
            // handling the null condition where the system is unable to find the correct PK column's AED
            if (pkAED == null) {
               pkAED = aed;
            }
            // deduce the mapping for the PK column
            deducedDEDs = deduceByName(pkAED, conceptsWithIndexMap, suggestedMappings, create, startCounter, model,
                     modelGroup);
         }
         // Now, map the same list of DEDs to the column and all of its relations
         if (!relations.contains(aed)) {
            relations.add(aed);
         }
         for (BusinessEntityDefinition deducedDED : deducedDEDs) {
            for (AssetEntityDefinition relation : relations) {
               addMapping(relation, deducedDED, suggestedMappings);
            }
         }
      } else {
         // No relations exist, hence deduce by name
         logger.debug("No relations for " + aed.getColum().getName() + ", so deducing by column name");
         deducedDEDs = deduceByName(aed, conceptsWithIndexMap, suggestedMappings, create, startCounter, model,
                  modelGroup);
         // Add the mapping
         for (BusinessEntityDefinition ded : deducedDEDs) {
            addMapping(aed, ded, suggestedMappings);
         }
      }
      return deducedDEDs;
   }

   /**
    * Finds all the related columns for the given column based on the constraints defined in the SWI. <BR>
    * i.e., for a column of PK type, this method will return all the FKs defined in the SWI spanning across the current
    * asset. <BR>
    * Similarly for a column which is a FK, the PK is returned
    * 
    * @param aed
    *           the AED of the column whose relatives are to be found
    * @return the list of all the relations of a column
    */
   private List<AssetEntityDefinition> getRelatedAEDs (AssetEntityDefinition aed) {
      List<AssetEntityDefinition> relations = new ArrayList<AssetEntityDefinition>();
      AssetEntityDefinition pkAED = null;
      if (CheckType.YES.equals(aed.getColum().getIsConstraintColum())) {
         try {
            if (getSdxRetrievalService().isForeignKeyColum(aed.getColum().getId())) {
               // If the current column is a FK, then first get the PK of it
               pkAED = getPrimaryKeyColumnAED(aed);
               // Add it to the relations
               relations.add(pkAED);
               // Now get all the other FKs which have the same PK
               relations.addAll(getForeignKeysForPrimaryKey(pkAED));
               // Remove the current column from the relations
               relations.remove(aed);
            } else {
               relations.addAll(getForeignKeysForPrimaryKey(aed));
            }
         } catch (SDXException e) {
            e.printStackTrace();
         }
      }
      return relations;
   }

   /**
    * This method returns all the FKs that have their primary key as this column
    * 
    * @param pkAED
    *           the column's AED which is the PK
    * @return the list of AEDs which are the FKs for the given PK
    */
   private List<AssetEntityDefinition> getForeignKeysForPrimaryKey (AssetEntityDefinition pkAED) {
      List<AssetEntityDefinition> fkAEDs = new ArrayList<AssetEntityDefinition>();
      try {
         fkAEDs = getSdxRetrievalService().getForeignKeysForPrimaryKey(pkAED.getAsset(), pkAED.getTabl(),
                  pkAED.getColum());
         logger.debug("Number of Foreign keys for " + pkAED.getColum().getName() + " : " + fkAEDs.size());
         for (AssetEntityDefinition fk : fkAEDs) {
            logger.debug(fk.getColum().getId());
         }
      } catch (SDXException e) {
         e.printStackTrace();
      }
      return fkAEDs;
   }

   /**
    * This method will try to deduce the suitable KDX entity by searching the entire SWI across all assets to find the
    * columns with the same name and match the mapped KDX entities of the matches
    * 
    * @param aed
    * @return
    */
   private List<BusinessEntityDefinition> deduceByName (AssetEntityDefinition aed,
            Map<String, Integer> conceptsWithIndexMap, List<Mapping> suggestedMappings, boolean create,
            Long startCounter, Model model, ModelGroup modelGroup) {
      List<BusinessEntityDefinition> deducedDEDs = new ArrayList<BusinessEntityDefinition>();
      // Get all the columns which are similar to the current column name, across all assets
      logger.debug("Trying to deduce by name for the column : [" + aed.getId() + "]" + aed.getColum().getName());
      List<AssetEntityDefinition> matchingAEDs = getAllMatchingAEDsAcrossAssets(aed);
      // Iterate through the list, check if any of the AED has a mapped DED
      for (AssetEntityDefinition match : matchingAEDs) {
         deducedDEDs = getMappedDEDs(match, suggestedMappings);
         if (ExecueCoreUtil.isCollectionNotEmpty(deducedDEDs)) {
            logger.debug(match.getColum().getName() + " of " + match.getTabl().getName() + " table matched "
                     + aed.getColum().getName() + "; Found matches!!");
            break;
         }
      }

      // TODO: revisit the logic for evaluating the matches
      // Evaluate each of possible DEDs to pick the most suitable DED
      // ded = sdx2kdxMappingHelper.evaluatePossibilities(aed, possibleDEDs);

      // If there are no possibilities, proceed to the suggestion process to suggest the concept
      if (ExecueCoreUtil.isCollectionEmpty(deducedDEDs)) {
         deducedDEDs = suggest(aed, conceptsWithIndexMap, suggestedMappings, create, startCounter, model, modelGroup);
      }
      return deducedDEDs;
   }

   /**
    * This method will try to suggest the best possible closest KDX entity that can be mapped to a column by normalizing
    * the column name to a human readable format and tries to find the matching KDX entities by that name. Various
    * algorithms can be used but currently the "perfect match" algorithm is used.
    * 
    * @param aed
    * @return
    */
   private List<BusinessEntityDefinition> suggest (AssetEntityDefinition aed,
            Map<String, Integer> conceptsWithIndexMap, List<Mapping> suggestedMappings, boolean create,
            Long startCounter, Model model, ModelGroup modelGroup) {
      logger.debug("Trying to suggest...");
      List<BusinessEntityDefinition> suggestedDEDs = new ArrayList<BusinessEntityDefinition>();
      BusinessEntityDefinition ded = null;

      // normalize the name
      String name = normalizeName(aed.getColum().getName(), true);

      logger.debug("Column name after normalizing : " + name);
      // TODO: Rework on the algorithm to match the concepts with the normalized name of the column name
      // Get the matching concepts either by direct match algorithm or proximity match algorithm
      List<Concept> matchingConcepts = getMatchingConceptsByName(model, name);
      logger.debug("matching concepts " + matchingConcepts.size());
      // TODO: Rework on the evaluation algorithm to pick the best matching concept; we can combine the matching and the
      // picking into a single operation
      // Evaluate the matching concepts to pick the most suitable concept; Current impl returns the first concept as a
      // list
      matchingConcepts = evaluateConcepts(aed, matchingConcepts);
      logger.debug("matching concepts after evaluation " + matchingConcepts.size());
      if (create) {
         if (ExecueCoreUtil.isCollectionNotEmpty(matchingConcepts)) {
            for (Concept suitableConcept : matchingConcepts) {
               boolean addIndex = false;
               ColumnType colKDXType = aed.getColum().getKdxDataType();
               // Add incremented index to the existing concept name if the kdx data type is either null or MEASURE
               if ((colKDXType == null) || ColumnType.MEASURE.equals(colKDXType)) {
                  addIndex = true;
               }
               // TODO: -RG- Verify this condition again, as we might not always want
               // to add a number at end for the existing concept
               // Modify the name to include the index based on the KDX data type, else use the same normalized name
               if (addIndex) {
                  name = appendIndexToConceptName(model, suitableConcept.getName(), conceptsWithIndexMap);
                  // Create a new concept
                  Concept concept = new Concept();
                  concept.setId(SUGGESTED_ENTITY_ID);
                  logger.debug("RESULT : Creating a new concept with name " + name);

                  // name needs to be separated by camel case
                  // concept.setDisplayName(ExecueStringUtil.getUncompactedDisplayString(name));
                  concept.setDisplayName(name);

                  concept.setName(name);
                  // Create a new DED for the concept with the new name
                  ded = createBusinessEntityDefinition(modelGroup, concept, null, startCounter);
               } else { // Suggest the same concept if the column type is not MEASURE
                  try {
                     ded = kdxRetrievalService.getBusinessEntityDefinitionByIds(model.getId(), suitableConcept.getId(),
                              null);
                  } catch (KDXException e) {
                     e.printStackTrace();
                  }
               }
               suggestedDEDs.add(ded);
            }
         } else {
            // Create a new concept
            Concept concept = new Concept();
            concept.setId(SUGGESTED_ENTITY_ID);
            logger.debug("RESULT : Creating a new concept with name " + name);

            // name needs to be separated by space and camel case
            // concept.setDisplayName(ExecueStringUtil.getUncompactedDisplayString(name));
            concept.setDisplayName(name);

            concept.setName(name);
            // Create a new DED for the concept with the new name
            ded = createBusinessEntityDefinition(modelGroup, concept, null, startCounter);
            suggestedDEDs.add(ded);
         }
      } else {
         for (Concept c : matchingConcepts) {
            try {
               ded = kdxRetrievalService.getBusinessEntityDefinitionByIds(model.getId(), c.getId(), null);
               suggestedDEDs.add(ded);
            } catch (KDXException e) {
               e.printStackTrace();
            }
         }
      }
      logger.debug("Before returning from suggest : deds list size : " + suggestedDEDs.size());
      return suggestedDEDs;
   }

   /**
    * This method searches the SWI to find the concepts which match the given name. <BR>
    * Currently, the direct match algorithm is being used.
    * 
    * @param conceptName
    * @return
    */
   // TODO: JVK Replace it with the suitable algorithm at a later stage.
   // TODO: String proximity; joins should be considered
   private List<Concept> getMatchingConceptsByName (Model model, String conceptName) {
      List<Concept> concepts = new ArrayList<Concept>();
      try {
         Concept concept = kdxRetrievalService.getConceptByName(model.getId(), conceptName);
         if (concept != null) {
            concepts.add(concept);
         }
      } catch (KDXException e) {
         e.printStackTrace();
      }
      return concepts;
   }

   /**
    * This method picks all the AEDs in the system across all assets which match by the column name of the AED
    * 
    * @param aed
    * @return
    */
   private List<AssetEntityDefinition> getAllMatchingAEDsAcrossAssets (AssetEntityDefinition aed) {
      List<AssetEntityDefinition> aeds = new ArrayList<AssetEntityDefinition>();
      try {
         aeds = getSdxRetrievalService().getAllMatchingAEDsByColumnName(aed.getAsset(), aed.getTabl(), aed.getColum());
      } catch (SDXException e) {
         e.printStackTrace();
      }
      return aeds;
   }

   /**
    * Get the PK table and column of the column which is of FK type and get the corresponding AED for the PK's column
    * 
    * @param aed
    * @return
    */
   private AssetEntityDefinition getPrimaryKeyColumnAED (AssetEntityDefinition aed) {
      AssetEntityDefinition pkAED = null;
      Colum col = aed.getColum();
      Set<Constraint> constraints = col.getConstraints();
      Tabl pkTable = null;
      Colum pkColumn = null;
      for (Constraint constraint : constraints) {
         ConstraintType cType = constraint.getType();
         if (ConstraintType.FOREIGN_KEY.equals(cType)) {
            pkTable = constraint.getReferenceTable();
            pkColumn = constraint.getReferenceColumn();
            break;
         }
      }
      if ((pkTable != null) && (pkColumn != null)) {
         pkAED = getAED(aed.getAsset(), pkTable, pkColumn, null);
      } else {
         logger.debug("Error - No entry in the constrain table....!!");
      }
      return pkAED;
   }

   /**
    * Retrieves the AED for a column
    * 
    * @param asset
    * @param table
    * @param column
    * @return
    */
   private AssetEntityDefinition getAED (Asset asset, Tabl table, Colum column, Membr member) {
      AssetEntityDefinition aed = getSdxRetrievalService().getAssetEntityDefinitionByIds(asset, table, column, member);
      logger.debug("AED id : " + aed.getId());
      Colum colum = aed.getColum();
      if (colum != null) {
         colum.getName();
         logger.debug("Column : " + colum.getName());
         Membr membr = aed.getMembr();
         if (membr != null) {
            logger.debug("Member : " + membr.getLookupDescription());
            membr.getLookupDescription();
         }
      }
      return aed;
   }

   /**
    * Retrieves the mapped DED for a AED. First the SWI mappings are checked and if no match is found, then the
    * suggested mappings are searched
    * 
    * @param aed
    * @return
    */
   private List<BusinessEntityDefinition> getMappedDEDs (AssetEntityDefinition aed, List<Mapping> suggestedMappings) {
      List<BusinessEntityDefinition> deds = new ArrayList<BusinessEntityDefinition>();
      logger.debug("Getting the DED for the aed : " + aed.getId());
      try {
         List<Mapping> matchingMappings = getMappingRetrievalService().getMappingsForAED(aed.getId());
         if (ExecueCoreUtil.isCollectionNotEmpty(matchingMappings)) {
            Mapping mapping = matchingMappings.get(0);
            BusinessEntityDefinition ded = mapping.getBusinessEntityDefinition();
            deds.add(ded);
            logger.debug("found mapping for aed " + aed.getId() + " in the swi : ded.id = " + ded.getId());
         } else {
            for (Mapping mapping : suggestedMappings) {
               if (aed.getId() == mapping.getAssetEntityDefinition().getId()) {
                  BusinessEntityDefinition ded = mapping.getBusinessEntityDefinition();
                  deds.add(ded);
                  logger.debug("found mapping for aed " + aed.getId()
                           + " from suggested mappings : ded's concept name = " + ded.getConcept().getName());
               }
            }
         }
      } catch (MappingException e) {
         e.printStackTrace();
      }
      return deds;
   }

   private String appendIndexToConceptName (Model model, String conceptName, Map<String, Integer> conceptsWithIndexMap) {
      logger.debug("Getting a new index for the concept name : " + conceptName);
      // prepare a map that contains all the existing concepts and the index mapped to each concept as 0
      try {
         List<Concept> concepts = kdxRetrievalService.getConcepts(model.getId());
         if (conceptsWithIndexMap.size() < 1) {
            for (Concept concept : concepts) {
               conceptsWithIndexMap.put(concept.getName(), new Integer(0));
            }
         }
         Integer index = conceptsWithIndexMap.get(conceptName);
         if (index != null) {
            conceptName += ++index;
         }
      } catch (KDXException e) {
         e.printStackTrace();
      }
      return conceptName;
   }

   /**
    * This method creates a new Mapping entry with the supplied AED and DED
    * 
    * @param aed
    * @param ded
    */
   private List<Mapping> addMapping (AssetEntityDefinition aed, BusinessEntityDefinition ded,
            List<Mapping> suggestedMappings) {
      Mapping mapping = new Mapping();
      mapping.setAssetEntityDefinition(aed);
      mapping.setBusinessEntityDefinition(ded);
      suggestedMappings.add(mapping);
      return suggestedMappings;
   }

   /**
    * Create a new BED with the concept and the instance
    * 
    * @param concept
    * @param modelGroup
    * @param instance
    * @param startCounter
    * @return
    */
   private BusinessEntityDefinition createBusinessEntityDefinition (ModelGroup modelGroup, Concept concept,
            Instance instance, Long startCounter) {
      BusinessEntityDefinition businessEntityDefinition = new BusinessEntityDefinition();
      businessEntityDefinition.setId(startCounter);
      businessEntityDefinition.setModelGroup(modelGroup);
      businessEntityDefinition.setConcept(concept);
      businessEntityDefinition.setEntityType(BusinessEntityType.CONCEPT);
      if (instance != null) {
         businessEntityDefinition.setInstance(instance);
         businessEntityDefinition.setEntityType(BusinessEntityType.CONCEPT_LOOKUP_INSTANCE);
      }
      return businessEntityDefinition;
   }

   private List<Concept> evaluateConcepts (AssetEntityDefinition aed, List<Concept> matchingConcepts) {
      // TODO JVK Add implementation; for now return the first concept
      List<Concept> matches = new ArrayList<Concept>(1);
      if (ExecueCoreUtil.isCollectionNotEmpty(matches)) {
         matches.add(matchingConcepts.get(0));
      }
      return matches;
   }

   private String normalizeName (String name, boolean addCapitals) {
      String delimiter = "_";
      String normalizedName = "";
      String expandedName = "";
      StringTokenizer st = new StringTokenizer(name, delimiter);
      while (st.hasMoreTokens()) {
         String token = st.nextToken();

         // remove any thing other than alpha numeric characters from this token
         token = token.replaceAll("[^a-zA-Z0-9]", "");

         String expansion = expansions.get(token.toLowerCase());
         if (expansion != null) {
            expandedName += delimiter + expansion;
         } else {
            expandedName += delimiter + token;
         }
      }
      st = new StringTokenizer(expandedName, delimiter);
      while (st.hasMoreTokens()) {
         String token = st.nextToken();
         if (!trivialWords.contains(token.toUpperCase())) {
            if (addCapitals) {
               token = StringUtils.capitalize(token);
            }
            normalizedName += token;
         } else {
         }
      }
      return normalizedName;
   }

   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }

   public IKDXManagementService getKdxManagementService () {
      return kdxManagementService;
   }

   public void setKdxManagementService (IKDXManagementService kdxManagementService) {
      this.kdxManagementService = kdxManagementService;
   }

   /**
    * @return the instanceMappingSuggestionService
    */
   public IInstanceMappingSuggestionService getInstanceMappingSuggestionService () {
      return instanceMappingSuggestionService;
   }

   /**
    * @param instanceMappingSuggestionService
    *           the instanceMappingSuggestionService to set
    */
   public void setInstanceMappingSuggestionService (IInstanceMappingSuggestionService instanceMappingSuggestionService) {
      this.instanceMappingSuggestionService = instanceMappingSuggestionService;
   }

   /**
    * @return the jobDataService
    */
   public IJobDataService getJobDataService () {
      return jobDataService;
   }

   /**
    * @param jobDataService
    *           the jobDataService to set
    */
   public void setJobDataService (IJobDataService jobDataService) {
      this.jobDataService = jobDataService;
   }

   /**
    * @return the coreConfigurationService
    */
   public ICoreConfigurationService getCoreConfigurationService () {
      return coreConfigurationService;
   }

   /**
    * @param coreConfigurationService
    *           the coreConfigurationService to set
    */
   public void setCoreConfigurationService (ICoreConfigurationService coreConfigurationService) {
      this.coreConfigurationService = coreConfigurationService;
   }

   /**
    * @return the batchMaintenanceService
    */
   public IBatchMaintenanceService getBatchMaintenanceService () {
      return batchMaintenanceService;
   }

   /**
    * @param batchMaintenanceService
    *           the batchMaintenanceService to set
    */
   public void setBatchMaintenanceService (IBatchMaintenanceService batchMaintenanceService) {
      this.batchMaintenanceService = batchMaintenanceService;
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

   public IMappingManagementService getMappingManagementService () {
      return mappingManagementService;
   }

   public void setMappingManagementService (IMappingManagementService mappingManagementService) {
      this.mappingManagementService = mappingManagementService;
   }

   /**
    * @return the baseKDXRetrievalService
    */
   public IBaseKDXRetrievalService getBaseKDXRetrievalService () {
      return baseKDXRetrievalService;
   }

   /**
    * @param baseKDXRetrievalService
    *           the baseKDXRetrievalService to set
    */
   public void setBaseKDXRetrievalService (IBaseKDXRetrievalService baseKDXRetrievalService) {
      this.baseKDXRetrievalService = baseKDXRetrievalService;
   }

   public IBusinessEntityManagementWrapperService getBusinessEntityManagementWrapperService () {
      return businessEntityManagementWrapperService;
   }

   public void setBusinessEntityManagementWrapperService (
            IBusinessEntityManagementWrapperService businessEntityManagementWrapperService) {
      this.businessEntityManagementWrapperService = businessEntityManagementWrapperService;
   }
}