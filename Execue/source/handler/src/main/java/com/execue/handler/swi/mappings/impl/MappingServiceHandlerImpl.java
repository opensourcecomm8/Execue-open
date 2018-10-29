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


package com.execue.handler.swi.mappings.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;

import com.execue.core.common.bean.batchMaintenance.InstanceAbsorptionContext;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.AssetEntityDefinition;
import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.DefaultMetric;
import com.execue.core.common.bean.entity.Instance;
import com.execue.core.common.bean.entity.InstanceMappingSuggestion;
import com.execue.core.common.bean.entity.InstanceMappingSuggestionDetail;
import com.execue.core.common.bean.entity.Mapping;
import com.execue.core.common.bean.entity.Membr;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.common.bean.entity.Tabl;
import com.execue.core.common.bean.mapping.ColumnMapping;
import com.execue.core.common.bean.mapping.MemberMapping;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.ColumnType;
import com.execue.core.common.type.LookupType;
import com.execue.core.common.util.MappingEntityType;
import com.execue.core.configuration.ICoreConfigurationService;
import com.execue.core.exception.ExeCueException;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.handler.bean.mapping.AssetColumn;
import com.execue.handler.bean.mapping.AssetColumnMember;
import com.execue.handler.bean.mapping.AssetMember;
import com.execue.handler.bean.mapping.AssetTable;
import com.execue.handler.bean.mapping.ConceptAssetMapping;
import com.execue.handler.bean.mapping.ConceptMapping;
import com.execue.handler.bean.mapping.InstanceAssetMapping;
import com.execue.handler.bean.mapping.InstanceMapping;
import com.execue.handler.bean.mapping.InstanceMappingsPageProvider;
import com.execue.handler.bean.mapping.MappableBusinessTerm;
import com.execue.handler.bean.mapping.MappableInstanceTerm;
import com.execue.handler.bean.mapping.MappingHeader;
import com.execue.handler.bean.mapping.MappingsPageProvider;
import com.execue.handler.bean.mapping.SaveMapping;
import com.execue.handler.bean.mapping.TermInfo;
import com.execue.handler.swi.mappings.IMappingServiceHandler;
import com.execue.platform.swi.IBusinessEntityManagementWrapperService;
import com.execue.platform.swi.ISDX2KDXMappingService;
import com.execue.scheduler.service.IInstanceAbsorptionJobService;
import com.execue.security.UserContextService;
import com.execue.swi.configuration.ISWIConfigurationService;
import com.execue.swi.exception.BatchMaintenanceException;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.MappingException;
import com.execue.swi.exception.SDXException;
import com.execue.swi.service.IBaseKDXRetrievalService;
import com.execue.swi.service.IInstanceMappingSuggestionService;
import com.execue.swi.service.IKDXManagementService;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.swi.service.IMappingDeletionService;
import com.execue.swi.service.IMappingManagementService;
import com.execue.swi.service.IMappingRetrievalService;
import com.execue.swi.service.ISDXRetrievalService;

public class MappingServiceHandlerImpl extends UserContextService implements IMappingServiceHandler {

   private static final Logger                     logger    = Logger.getLogger(MappingServiceHandlerImpl.class);
   private static final int                        PAGE_SIZE = 30;

   private IBaseKDXRetrievalService                baseKDXRetrievalService;
   private IKDXRetrievalService                    kdxRetrievalService;
   private IKDXManagementService                   kdxManagementService;
   private ISDXRetrievalService                    sdxRetrievalService;
   private IInstanceMappingSuggestionService       instanceMappingSuggestionService;
   private IMappingDeletionService                 mappingDeletionService;
   private IMappingRetrievalService                mappingRetrievalService;
   private ISDX2KDXMappingService                  sdx2kdxMappingService;
   private IBusinessEntityManagementWrapperService businessEntityManagementWrapperService;
   private IMappingManagementService               mappingManagementService;
   private ICoreConfigurationService               coreConfigurationService;
   private IInstanceAbsorptionJobService           instanceAbsorptionJobService;
   private ISWIConfigurationService                swiConfigurationService;

   public ConceptMapping saveConceptMappings (Long modelId, List<SaveMapping> saveMappings,
            MappingsPageProvider provider, InstanceMappingsPageProvider instanceMappingsPageProvider)
            throws ExeCueException {

      ConceptMapping conceptMapping = new ConceptMapping();
      List<TermInfo> newTerms = new ArrayList<TermInfo>();
      List<ConceptAssetMapping> finalList = new ArrayList<ConceptAssetMapping>();
      List<ConceptAssetMapping> toBeSavedCAMs = new ArrayList<ConceptAssetMapping>();
      List<ConceptAssetMapping> toBeDeletedCAMs = new ArrayList<ConceptAssetMapping>();
      List<ConceptAssetMapping> invalidMappings = new ArrayList<ConceptAssetMapping>();

      try {
         for (SaveMapping saveMapping : saveMappings) {
            ConceptAssetMapping conceptAssetMapping = provider.getConceptMappingByAEDId(saveMapping.getAedId());
            if (conceptAssetMapping == null) {
               // if there is no match in the provider, then create a new
               // CAM for the saveMapping
               conceptAssetMapping = convertToConceptAssetMapping(saveMapping);
               // add it to the provider
               provider.addConceptMapping(conceptAssetMapping);
            }
            // separate the mappings which are to be deleted from those
            // which are to be saved
            boolean deleteMapping = Boolean.parseBoolean(saveMapping.getDelMap());
            if (deleteMapping) {
               toBeDeletedCAMs.add(conceptAssetMapping);
            } else {
               // 1. merge the details in the save mapping with this match
               mergeMappingDetails(saveMapping, conceptAssetMapping);
               // 2. clear the errors/warnings if any
               clearMessage(conceptAssetMapping);
               // 3. validate the save mappings for errors/warnings
               boolean mappingValid = isValidMapping(conceptAssetMapping, provider);
               // move the invalid mapping to the top of the list
               if (mappingValid) {
                  toBeSavedCAMs.add(conceptAssetMapping);
               } else {
                  invalidMappings.add(conceptAssetMapping);
               }
            }
         }

         if (toBeDeletedCAMs.size() > 0) {
            deleteMappings(toBeDeletedCAMs, provider, instanceMappingsPageProvider);
         }

         for (ConceptAssetMapping caMapping : toBeSavedCAMs) {
            AssetEntityDefinition aed = null;
            BusinessEntityDefinition bed = null;
            try {
               aed = getSdxRetrievalService().getPopulatedAssetEntityDefinitionById(caMapping.getAedId());
            } catch (SDXException e) {
               // eat the exception if the aed doesnt exist.
            }
            try {
               bed = getKdxRetrievalService().getBusinessEntityDefinitionById(caMapping.getBedId());
            } catch (KDXException e) {
               // eat the exception if the bed doesnt exist.
            }

            Mapping swiMapping = null;
            if (bed != null) {
               List<Mapping> swiMappings = getMappingRetrievalService().getMappingsForAED(aed.getId());
               if ((swiMappings != null) && (swiMappings.size() > 0)) {
                  swiMapping = getMappingRetrievalService().getMappingsForAED(aed.getId()).get(0);
               }
            }
            if (swiMapping == null) { // no mapping found
               // applicable to only NEW and SUGGESTED type mappings
               if (bed == null) { // SUGGESTED type
                  // TODO: -JVK- do the below check in a single service
                  // call instead of using different service calls
                  // check if any concept exists by the same name
                  Concept concept = getKdxRetrievalService().getConceptByDisplayName(modelId,
                           caMapping.getConDispName());
                  if (concept != null) {
                     bed = getKdxRetrievalService().getBusinessEntityDefinitionByIds(modelId, concept.getId(), null);
                  }
                  if (bed == null) { // there is no concept, need to
                     // create the concept
                     // create the concept and get its BED
                     Concept newConcept = new Concept();
                     newConcept.setDisplayName(caMapping.getConDispName());

                     // Added the ELIGIBLE_GRAIN field value - defaulting
                     // to NO
                     // newConcept.setEligibleGrain(CheckType.NO);
                     // Not Required -RG- -NOTE- as we can get it from
                     // DED level flags
                     getBusinessEntityManagementWrapperService().createConcept(modelId, newConcept);
                     bed = getKdxRetrievalService().getBusinessEntityDefinitionByIds(modelId, newConcept.getId(), null);
                     populateTermInfo(newTerms, newConcept, bed);
                  }
               }
               // create the mapping
               List<Mapping> mappings = createMappings(swiMapping, aed, bed);
               // update the CAM
               updateConceptAssetMappings(mappings, caMapping, aed, bed);
               // update the display name field of other CAMs which have
               // the same DED id
               for (ConceptAssetMapping match : provider.getConceptMappingsByBEDid(caMapping.getBedId())) {
                  match.setConDispName(caMapping.getConDispName());
               }
               // check if the 'Map Instances' link can be enabled for the
               // mapping
               if (isMappableToInstances(aed)) {
                  caMapping.setMapInstance(true);
               }
            } else {
               BusinessEntityDefinition oldBED = swiMapping.getBusinessEntityDefinition();
               // update the concept's display name
               Concept concept = bed.getConcept();
               concept.setDisplayName(caMapping.getConDispName());
               getKdxManagementService().updateConcept(modelId, concept);
               swiMapping.setBusinessEntityDefinition(bed);
               // update the existing mapping with the ded if the ded is
               // different from the original
               if (oldBED.getId().longValue() != bed.getId().longValue()) {
                  // delete the mappings belonging to the members of the
                  // column if any
                  // Get the members for the column
                  List<Membr> members = getSdxRetrievalService().getColumnMembers(aed.getColum());
                  List<Mapping> swiMemberMappings = new ArrayList<Mapping>();
                  for (Membr member : members) {
                     // Delete the mappings of each member
                     AssetEntityDefinition memberAED = getSdxRetrievalService().getAssetEntityDefinitionByIds(
                              aed.getAsset(), aed.getTabl(), aed.getColum(), member);
                     swiMemberMappings.addAll(getMappingRetrievalService().getMappingsForAED(memberAED.getId()));
                     // delete the member mappings from the instance
                     // mappings provider object
                     deleteFromInstanceMappingsPageProvider(memberAED, instanceMappingsPageProvider);
                  }
                  // delete the member mappings from SWI
                  getMappingDeletionService().deleteUIMappings(swiMemberMappings);
                  // update the existing mapping with the new concept's
                  // DED
                  getMappingManagementService().updateMapping(swiMapping);
                  // check if the 'Map Instances' link can be enabled for
                  // the new mapping
                  if (isMappableToInstances(aed)) {
                     caMapping.setMapInstance(true);
                  }
               }
               // update the display name and ded fields of other CAMs
               // which have ded id as in the SWI mapping before
               // update
               List<ConceptAssetMapping> matchBEDMappings = provider.getConceptMappingsByBEDid(oldBED.getId());
               if (matchBEDMappings != null) {
                  for (ConceptAssetMapping match : matchBEDMappings) {
                     match.setConDispName(caMapping.getConDispName());
                     match.setConId(caMapping.getConId());
                     match.setBedId(caMapping.getBedId());
                  }
               }
            }
         }

         conceptMapping.setMappings(provider.getConceptMappings());
         conceptMapping.setNewConcepts(newTerms);
         // return the invalid and tobesaved lists.
         finalList.addAll(invalidMappings);
         finalList.addAll(toBeSavedCAMs);
         conceptMapping.setMappings(finalList);
      } catch (Exception e) {
         e.printStackTrace();
         throw new ExeCueException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
      return conceptMapping;
   }

   private List<Mapping> createMappings (Mapping swiMapping, AssetEntityDefinition aed, BusinessEntityDefinition bed)
            throws MappingException {
      List<Mapping> mappings = new ArrayList<Mapping>();
      swiMapping = new Mapping();
      swiMapping.setAssetEntityDefinition(aed);
      swiMapping.setBusinessEntityDefinition(bed);
      mappings.add(swiMapping);
      getMappingManagementService().persistUIMappings(mappings);
      return mappings;
   }

   private void updateConceptAssetMappings (List<Mapping> mappings, ConceptAssetMapping caMapping,
            AssetEntityDefinition aed, BusinessEntityDefinition bed) {
      caMapping.setId(mappings.get(0).getId());
      caMapping.setMappingType(MappingEntityType.EXISTING);
      caMapping.setConType(MappingEntityType.EXISTING);
      caMapping.setBedId(bed.getId());
      caMapping.setConId(bed.getConcept().getId());
   }

   private void populateTermInfo (List<TermInfo> newTerms, Concept newConcept, BusinessEntityDefinition bed) {
      TermInfo termInfo = new TermInfo();
      termInfo.setId(newConcept.getId());
      termInfo.setBedId(bed.getId());
      termInfo.setMapped(true);
      termInfo.setName(newConcept.getName());
      termInfo.setDispName(newConcept.getDisplayName());
      newTerms.add(termInfo);
   }

   private void deleteMappings (List<ConceptAssetMapping> toBeDeletedCAMs, MappingsPageProvider provider,
            InstanceMappingsPageProvider instanceMappingsPageProvider) throws SDXException, MappingException {
      List<Mapping> delMappings = new ArrayList<Mapping>();
      for (ConceptAssetMapping delMapping : toBeDeletedCAMs) {
         if (MappingEntityType.EXISTING.equals(delMapping.getMappingType())) {
            AssetEntityDefinition aed = getSdxRetrievalService().getPopulatedAssetEntityDefinitionById(
                     delMapping.getAedId());
            Mapping entry = new Mapping();
            entry.setId(delMapping.getId());
            delMappings.add(entry);

            // delete the mappings belonging to the members of the column if
            // any
            // Get the members for the column
            List<Membr> members = getSdxRetrievalService().getColumnMembers(aed.getColum());
            List<Mapping> swiMemberMappings = new ArrayList<Mapping>();
            for (Membr member : members) {
               // Delete the mappings of each member
               AssetEntityDefinition memberAED = getSdxRetrievalService().getAssetEntityDefinitionByIds(aed.getAsset(),
                        aed.getTabl(), aed.getColum(), member);
               swiMemberMappings.addAll(getMappingRetrievalService().getMappingsForAED(memberAED.getId()));
               // delete the member mappings from the instance mappings
               // provider object
               deleteFromInstanceMappingsPageProvider(memberAED, instanceMappingsPageProvider);
            }
            // delete the member mappings from SWI
            getMappingDeletionService().deleteUIMappings(swiMemberMappings);
         }
         // delete from the provider object
         provider.deleteConceptMapping(delMapping);
      }
      // delete the concept mapping from SWI
      getMappingDeletionService().deleteUIMappings(delMappings);

   }

   private void deleteFromInstanceMappingsPageProvider (AssetEntityDefinition memberAED,
            InstanceMappingsPageProvider instanceMappingsPageProvider) {
      if (instanceMappingsPageProvider != null) {
         InstanceAssetMapping instanceAssetMapping = instanceMappingsPageProvider.getInstanceMappingByAEDId(memberAED
                  .getId());
         if (instanceAssetMapping != null) {
            logger.debug("deleting the IAM from the provider object for the AED "
                     + memberAED.getMembr().getLookupDescription());
            instanceMappingsPageProvider.deleteInstanceMapping(instanceAssetMapping);
         }
      }
   }

   private boolean isValidMapping (ConceptAssetMapping conceptAssetMapping, MappingsPageProvider provider)
            throws KDXException {
      // rule 1 : duplicate rule
      // rule 2 : single mapping for MEASURES
      // If type is NEW, check for rule 1 and 2 and for others check for rule
      // 2
      boolean flag = true;
      if (MappingEntityType.NEW.equals(conceptAssetMapping.getMappingType())) {
         if (mappingExists(conceptAssetMapping, provider) || isConceptMappedToMeasure(conceptAssetMapping, provider)) {
            flag = false;
         }
      } else {
         if (isConceptMappedToMeasure(conceptAssetMapping, provider)) {
            flag = false;
         }
      }
      if (getBaseKDXRetrievalService().isSystemVariableExist(conceptAssetMapping.getConDispName())) {
         conceptAssetMapping.setMsgType("E");
         conceptAssetMapping
                  .setMsg("Concept " + conceptAssetMapping.getColDispName() + " already exists in the System");
         flag = false;
      }
      return flag;
   }

   private boolean mappingExists (ConceptAssetMapping conceptAssetMapping, MappingsPageProvider provider) {
      boolean exists = false;
      ConceptAssetMapping match = provider.getConceptMappingByAEDId(conceptAssetMapping.getAedId());
      if ((match != null) && MappingEntityType.EXISTING.equals(match.getMappingType())) {
         conceptAssetMapping.setMsgType("E");
         conceptAssetMapping.setMsg("Mapping already exists.");
         logger.debug("Mapping already exists for AED " + conceptAssetMapping.getAedId());
         exists = true;
      }
      return exists;
   }

   private boolean isConceptMappedToMeasure (ConceptAssetMapping conceptAssetMapping, MappingsPageProvider provider) {
      boolean mappedToMeasure = false;
      try {
         // type 1 - concept already mapped to a column and the current
         // column is of type measure
         // type 2 - concept already mapped to a measure
         List<ConceptAssetMapping> matchingMappings = provider
                  .getConceptMappingsByBEDid(conceptAssetMapping.getBedId());
         if ((matchingMappings != null) && (matchingMappings.size() > 0)) {
            // remove the current CAM from the matches
            matchingMappings.remove(conceptAssetMapping);
            if (matchingMappings.size() > 0) {
               Colum currentColumn = getSdxRetrievalService().getPopulatedAssetEntityDefinitionById(
                        conceptAssetMapping.getAedId()).getColum();
               ColumnType currentColumnType = currentColumn.getKdxDataType();
               if (ColumnType.MEASURE.equals(currentColumnType)) {
                  conceptAssetMapping.setMsgType("E");
                  conceptAssetMapping
                           .setMsg("A concept of type MEASURE cannot be mapped to multiple columns. The concept has already been mapped to a column and is now mapped to a column which has the kdx type as MEASURE.");
                  mappedToMeasure = true; // type 1
               } else {
                  for (ConceptAssetMapping cam : matchingMappings) {
                     Colum prevColumn = getSdxRetrievalService().getPopulatedAssetEntityDefinitionById(cam.getAedId())
                              .getColum();
                     if (ColumnType.MEASURE.equals(prevColumn.getKdxDataType())) {
                        conceptAssetMapping.setMsgType("E");
                        conceptAssetMapping
                                 .setMsg("A concept of type MEASURE cannot be mapped to multiple columns. The concept has already been mapped to a column which has the kdx type as MEASURE.");
                        mappedToMeasure = true; // type 2
                        break;
                     }
                  }
               }
            }
         }
      } catch (SDXException e) {
         e.printStackTrace();
      }
      return mappedToMeasure;
   }

   private void clearMessage (ConceptAssetMapping conceptAssetMapping) {
      conceptAssetMapping.setMsg(null);
      conceptAssetMapping.setMsgType(null);
   }

   private void mergeMappingDetails (SaveMapping saveMapping, ConceptAssetMapping conceptAssetMapping) {
      conceptAssetMapping.setBedId(saveMapping.getBedId());
      conceptAssetMapping.setConDispName(saveMapping.getDispName());
      conceptAssetMapping.setConType(saveMapping.getBedType());
   }

   private ConceptAssetMapping convertToConceptAssetMapping (SaveMapping saveMapping) {
      ConceptAssetMapping caMapping = new ConceptAssetMapping();
      try {
         AssetEntityDefinition aed = getSdxRetrievalService().getPopulatedAssetEntityDefinitionById(
                  saveMapping.getAedId());
         Concept concept = getKdxRetrievalService().getBusinessEntityDefinitionById(saveMapping.getBedId())
                  .getConcept();
         caMapping.setMappingType(saveMapping.getMapType());
         caMapping.setAedId(saveMapping.getAedId());
         caMapping.setBedId(saveMapping.getBedId());
         caMapping.setColDispName(aed.getColum().getName());
         caMapping.setConDispName(saveMapping.getDispName());
         caMapping.setConId(concept.getId());
         caMapping.setConType(saveMapping.getBedType());
         caMapping.setTblDispName(aed.getTabl().getName());
         caMapping.setTableDisplayName(aed.getTabl().getDisplayName());
         // caMapping.setMapInstance(isMappableToInstances(aed));
         caMapping.setMapInstance(false);
         // caMapping.setRelevance(relevance);
      } catch (SDXException se) {
         se.printStackTrace();
      } catch (KDXException ke) {
         ke.printStackTrace();
      }
      return caMapping;
   }

   private InstanceAssetMapping convertToInstanceAssetMapping (SaveMapping saveMapping) {
      InstanceAssetMapping iaMapping = new InstanceAssetMapping();
      try {
         AssetEntityDefinition aed = getSdxRetrievalService().getPopulatedAssetEntityDefinitionById(
                  saveMapping.getAedId());
         Instance instance = getKdxRetrievalService().getBusinessEntityDefinitionById(saveMapping.getBedId())
                  .getInstance();
         iaMapping.setMappingType(saveMapping.getMapType());
         iaMapping.setAedId(saveMapping.getAedId());
         iaMapping.setBedId(saveMapping.getBedId());
         iaMapping.setColDispName(aed.getColum().getName());
         iaMapping.setMemDispName(aed.getMembr().getLookupDescription());
         iaMapping.setConDispName(saveMapping.getDispName());
         iaMapping.setInsId(instance.getId());
         iaMapping.setInstanceType(MappingEntityType.EXISTING);
         iaMapping.setTblDispName(aed.getTabl().getName());
      } catch (SDXException se) {
         se.printStackTrace();
      } catch (KDXException ke) {
         ke.printStackTrace();
      }
      return iaMapping;
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
   // private List<AssetEntityDefinition> getRelatedAEDs (AssetEntityDefinition
   // aed) {
   // List<AssetEntityDefinition> relations = new
   // ArrayList<AssetEntityDefinition>();
   // AssetEntityDefinition pkAED = null;
   // if (CheckType.YES.equals(aed.getColum().getIsConstraintColum())) {
   // try {
   // if (getSdxService().isForeignKeyColum(aed.getColum().getId())) {
   // // If the current column is a FK, then first get the PK of it
   // pkAED = getPrimaryKeyColumnAED(aed);
   // // Add it to the relations
   // relations.add(pkAED);
   // // Now get all the other FKs which have the same PK
   // relations.addAll(getForeignKeysForPrimaryKey(pkAED));
   // // Remove the current column from the relations
   // relations.remove(aed);
   // } else {
   // relations.addAll(getForeignKeysForPrimaryKey(aed));
   // }
   // } catch (SDXException e) {
   // e.printStackTrace();
   // }
   // }
   // return relations;
   // }
   /**
    * This method returns all the FKs that have their primary key as this column
    * 
    * @param pkAED
    *           the column's AED which is the PK
    * @return the list of AEDs which are the FKs for the given PK
    */
   // private List<AssetEntityDefinition> getForeignKeysForPrimaryKey
   // (AssetEntityDefinition pkAED) {
   // List<AssetEntityDefinition> fkAEDs = new
   // ArrayList<AssetEntityDefinition>();
   // try {
   // fkAEDs = getSdxService().getForeignKeysForPrimaryKey(pkAED.getAsset(),
   // pkAED.getTabl(), pkAED.getColum());
   // logger.debug("Number of Foreign keys for " + pkAED.getColum().getName() +
   // " : " + fkAEDs.size());
   // for (AssetEntityDefinition fk : fkAEDs) {
   // logger.debug(fk.getColum().getId());
   // }
   // } catch (SDXException e) {
   // e.printStackTrace();
   // }
   // return fkAEDs;
   // }
   /**
    * Get the PK table and column of the column which is of FK type and get the corresponding AED for the PK's column
    * 
    * @param aed
    * @return
    */
   // private AssetEntityDefinition getPrimaryKeyColumnAED
   // (AssetEntityDefinition aed) {
   // AssetEntityDefinition pkAED = null;
   // Colum col = aed.getColum();
   // Set<Constraint> constraints = col.getConstraints();
   // Tabl pkTable = null;
   // Colum pkColumn = null;
   // for (Constraint constraint : constraints) {
   // ConstraintType cType = constraint.getType();
   // if (ConstraintType.FOREIGN_KEY.equals(cType)) {
   // pkTable = constraint.getReferenceTable();
   // pkColumn = constraint.getReferenceColumn();
   // break;
   // }
   // }
   // if (pkTable != null && pkColumn != null) {
   // pkAED = getSdxService().getAssetEntityDefinitionByIds(aed.getAsset(),
   // pkTable, pkColumn, null);
   // } else {
   // logger.debug("Error - No entry in the constrain table....!!");
   // }
   // return pkAED;
   // }
   // private void storeInstanceMapping (Mapping mapping) {
   // List<Mapping> persistMappings = new ArrayList<Mapping>();
   // persistMappings.clear();
   // persistMappings.add(mapping);
   // try {
   // getMappingService().persistUIMappings(persistMappings);
   // } catch (MappingException e) {
   // e.printStackTrace();
   // }
   // }
   /*
    * This will be called when the screen is being loaded; Need to capture all the pre-approved mappings
    */
   public List<ConceptAssetMapping> showExistingConceptMappings (Long assetId) throws ExeCueException {
      List<ConceptAssetMapping> caMappings = new ArrayList<ConceptAssetMapping>();
      // Get the asset object
      Asset asset = getSdxRetrievalService().getAssetById(assetId);
      // Get all the mappings of column AEDs belonging to the current asset
      List<Mapping> mappings = getMappingRetrievalService().getExistingMappingsForColumns(asset.getId());
      // data structure transformation into ConceptAssetMapping
      caMappings = getConceptAssetMappings(mappings);
      return caMappings;
   }

   private List<ConceptAssetMapping> getConceptAssetMappings (List<Mapping> mappings) {
      List<ConceptAssetMapping> caMappings = new ArrayList<ConceptAssetMapping>();
      for (Mapping mapping : mappings) {
         ConceptAssetMapping conceptAssetMapping = new ConceptAssetMapping();
         conceptAssetMapping.setId(mapping.getId());
         conceptAssetMapping.setAedId(mapping.getAssetEntityDefinition().getId());
         conceptAssetMapping.setBedId(mapping.getBusinessEntityDefinition().getId());
         conceptAssetMapping.setMappingType(MappingEntityType.EXISTING);
         conceptAssetMapping.setConType(MappingEntityType.EXISTING);
         conceptAssetMapping.setColDispName(mapping.getAssetEntityDefinition().getColum().getName());
         conceptAssetMapping.setTblDispName(mapping.getAssetEntityDefinition().getTabl().getName());
         conceptAssetMapping.setTableDisplayName(mapping.getAssetEntityDefinition().getTabl().getDisplayName());
         conceptAssetMapping.setConDispName(mapping.getBusinessEntityDefinition().getConcept().getDisplayName());
         conceptAssetMapping.setConId(mapping.getBusinessEntityDefinition().getConcept().getId());
         conceptAssetMapping.setMapInstance(isMappableToInstances(mapping.getAssetEntityDefinition()));
         caMappings.add(conceptAssetMapping);
      }
      return caMappings;
   }

   public List<AssetTable> getAssetTables (Long assetId) throws ExeCueException {
      List<AssetTable> assetTables = new ArrayList<AssetTable>();
      try {
         Asset asset = getSdxRetrievalService().getAssetById(assetId);
         // Get the tables for the asset
         List<Tabl> tables = getSdxRetrievalService().getAllTables(asset);
         // Get the columns for each table
         for (Tabl table : tables) {
            AssetTable assetTable = new AssetTable();
            // Get the associated AED for the table
            AssetEntityDefinition tableAED = getSdxRetrievalService().getAssetEntityDefinitionByIds(asset, table, null,
                     null);
            assetTable.setAedId(tableAED.getId());
            assetTable.setId(table.getId());
            assetTable.setDispName(table.getName());
            assetTable.setTableDisplayName(table.getDisplayName());
            List<Colum> columns = getSdxRetrievalService().getColumnsOfTable(table);
            List<AssetColumn> assetColumns = new ArrayList<AssetColumn>();
            boolean isSimpleLookupTable = LookupType.SIMPLE_LOOKUP.equals(table.getLookupType());
            for (Colum column : columns) {
               if (isSimpleLookupTable) {
                  if (column.getName().equalsIgnoreCase(table.getLookupDescColumn())) {
                     continue;
                  }
               }
               AssetColumn assetColumn = new AssetColumn();
               // Get the associated AED for the column
               AssetEntityDefinition columnAED = getSdxRetrievalService().getAssetEntityDefinitionByIds(asset, table,
                        column, null);
               assetColumn.setAedId(columnAED.getId());
               assetColumn.setId(column.getId());
               assetColumn.setDispName(column.getName());
               assetColumns.add(assetColumn);
            }
            assetTable.setCols(assetColumns);
            assetTables.add(assetTable);
         }
      } catch (Exception e) {
         e.printStackTrace();
         throw new ExeCueException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e.getMessage());
      }
      return assetTables;
   }

   public MappableBusinessTerm showConcepts (Long modelId) throws ExeCueException {
      MappableBusinessTerm mappableBusinssTerm = null;
      try {
         List<BusinessEntityDefinition> conceptBEDList = getKdxRetrievalService().getConceptBEDsOfModel(modelId);
         if (ExecueCoreUtil.isCollectionNotEmpty(conceptBEDList)) {
            mappableBusinssTerm = new MappableBusinessTerm();
            List<TermInfo> conceptInfoList = new ArrayList<TermInfo>();
            for (BusinessEntityDefinition bed : conceptBEDList) {
               TermInfo cInfo = new TermInfo();
               cInfo.setBedId(bed.getId());
               cInfo.setDispName(bed.getConcept().getDisplayName());
               cInfo.setId(bed.getConcept().getId());
               cInfo.setName(bed.getConcept().getName());
               conceptInfoList.add(cInfo);
            }
            mappableBusinssTerm.setTerms(conceptInfoList);
         }
      } catch (KDXException e) {
         e.printStackTrace();
         throw new ExeCueException(e.getCode(), e.getMessage());
      }
      return mappableBusinssTerm;
   }

   /**
    * The user can just change the display name of an existing concept, or map the column to a new concept The user will
    * get a list of concepts in the model based on user's input; AJAX call
    */
   public List<TermInfo> suggestConcepts (Long modelId, String searchString) throws ExeCueException {
      List<TermInfo> cols = null;
      try {
         List<Concept> suggestedConcepts = getKdxRetrievalService().getConceptsBySearchString(modelId, searchString);
         if ((suggestedConcepts != null) && (suggestedConcepts.size() > 0)) {
            cols = new ArrayList<TermInfo>();
            for (Concept concept : suggestedConcepts) {
               BusinessEntityDefinition conceptBED = getKdxRetrievalService().getBusinessEntityDefinitionByIds(modelId,
                        concept.getId(), null);
               TermInfo cInfo = new TermInfo();
               cInfo.setBedId(conceptBED.getId());
               cInfo.setDispName(concept.getDisplayName());
               cInfo.setId(concept.getId());
               cInfo.setName(concept.getName());
               cols.add(cInfo);
            }
         }
      } catch (KDXException e) {
         e.printStackTrace();
         throw new ExeCueException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e.getMessage());
      }
      return cols;
   }

   /**
    * Usecase 1) The user may just select a list of columns from the asset tables Usecase 2) The user may select a few
    * concepts along with the list of columns A column that's already been mapped will not participate in the suggestion
    * process Suggested concepts can be existing or new concepts that doesn't exist yet; The following is the strict
    * order of precedence 1) If a PK/FK relation exists b/w an existing mapping columns, the same concept will be
    * suggested or the related AEDs 2) If the suggested name (for a new concept) matches with existing concept, then we
    * suggest the column be mapped to the same concept 3) The normalized name will be the suggested name for the new
    * concept, the last resort
    */
   public ConceptMapping suggestConceptMappingsForSelectedColumns (Long modelId, List<Long> selColAedIds,
            List<Long> selConBedIds, MappingsPageProvider provider) throws ExeCueException {
      Model model = getKdxRetrievalService().getModelById(modelId);
      ConceptMapping conceptMapping = new ConceptMapping();
      List<ConceptAssetMapping> mappings = new ArrayList<ConceptAssetMapping>();
      List<ConceptAssetMapping> finalMappings = new ArrayList<ConceptAssetMapping>();
      List<String> selectedConceptDispNames = new ArrayList<String>();
      try {
         for (Long colAedId : selColAedIds) {
            AssetEntityDefinition colAED = getSdxRetrievalService().getPopulatedAssetEntityDefinitionById(colAedId);
            ColumnMapping colMapping = getSdx2kdxMappingService().mapSDX2KDXForColumn(colAED.getAsset(),
                     colAED.getTabl(), colAED.getColum(), false, false, true, provider.getCounter(), model);
            if (colMapping != null) {
               ConceptAssetMapping caMapping = new ConceptAssetMapping();
               caMapping.setAedId(colAedId);
               caMapping.setBedId(colMapping.getBusinessEntityDefinition().getId());
               caMapping.setColDispName(colMapping.getAssetEntityDefinition().getColum().getName());
               Long conceptId = colMapping.getBusinessEntityDefinition().getConcept().getId();
               caMapping.setConId(conceptId);
               if (getBaseKDXRetrievalService().isSystemVariableExist(
                        colMapping.getBusinessEntityDefinition().getConcept().getDisplayName())) {
                  caMapping.setConDispName(colMapping.getBusinessEntityDefinition().getConcept().getDisplayName() + "_"
                           + RandomStringUtils.randomNumeric(2));
               } else {
                  caMapping.setConDispName(colMapping.getBusinessEntityDefinition().getConcept().getDisplayName());
               }
               if (conceptId == -1L) {
                  caMapping.setConType(MappingEntityType.SUGGESTED);
               } else {
                  caMapping.setConType(MappingEntityType.EXISTING);
               }
               caMapping.setMappingType(MappingEntityType.SUGGESTED);
               caMapping.setTblDispName(colMapping.getAssetEntityDefinition().getTabl().getName());
               caMapping.setTableDisplayName(colMapping.getAssetEntityDefinition().getTabl().getDisplayName());
               // caMapping.setHasInstance(false);
               // caMapping.setRelevance(relevance);
               mappings.add(caMapping);
            }
            // increment the counter value of the provider object
            provider.setCounter(provider.getCounter() + 1);
         }
         if (ExecueCoreUtil.isCollectionNotEmpty(selConBedIds)) {
            for (Long conBedId : selConBedIds) {
               BusinessEntityDefinition conBED = getKdxRetrievalService().getBusinessEntityDefinitionById(conBedId);
               String conceptDispName = conBED.getConcept().getDisplayName();
               selectedConceptDispNames.add(conceptDispName);
            }
            // Filter the results based on the concepts selected by the user
            // Assumption: Retain all the mappings which feature any one of
            // the selected concepts and discard the rest
            for (ConceptAssetMapping caMapping : mappings) {
               if (selectedConceptDispNames.contains(caMapping.getConDispName())) {
                  finalMappings.add(caMapping);
               }
            }
         } else {
            finalMappings = mappings;
         }
      } catch (SDXException e) {
         e.printStackTrace();
         throw new ExeCueException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e.getMessage());
      }
      if (logger.isDebugEnabled()) {
         logger.debug("before returning : counter " + provider.getCounter());
      }
      provider.setConceptMappings(finalMappings);
      conceptMapping.setMappings(provider.getConceptMappings());
      return conceptMapping;
   }

   /**
    * This method checks whether the 'Map Instances' link can be enabled for the mapping which the current column is
    * part of
    */
   private boolean isMappableToInstances (AssetEntityDefinition aed) {
      boolean isMappable = false;
      boolean isDimension = false;
      boolean hasMembers = false;
      ColumnType columnType = aed.getColum().getKdxDataType();
      if (ColumnType.DIMENSION.equals(columnType) || ColumnType.SIMPLE_LOOKUP.equals(columnType)
               || ColumnType.RANGE_LOOKUP.equals(columnType)) {
         isDimension = true;
      }
      try {
         List<Membr> members = getSdxRetrievalService().getColumnMembers(aed.getColum());
         if ((members != null) && (members.size() > 0)) {
            hasMembers = true;
         }
      } catch (SDXException e) {
         e.printStackTrace();
      }
      if (isDimension && hasMembers && !CheckType.YES.equals(aed.getColum().getIndicator())) {
         isMappable = true;
      }
      return isMappable;
   }

   /**
    * This method retrieves all the existing mappings between members and instances in SWI
    */
   public List<InstanceAssetMapping> showExistingInstanceMappings (Long selColAedId, Long conBedId)
            throws ExeCueException {
      List<InstanceAssetMapping> iaMappings = new ArrayList<InstanceAssetMapping>();
      List<Mapping> memberMappings = new ArrayList<Mapping>();
      AssetEntityDefinition colAED = getSdxRetrievalService().getPopulatedAssetEntityDefinitionById(selColAedId);
      Colum selectedColumn = colAED.getColum();
      // TODO: CHANGE TO QUERY
      List<Membr> members = getSdxRetrievalService().getColumnMembers(selectedColumn);
      // Get all the mappings of instance AEDs belonging to the selected
      // column
      for (Membr member : members) {
         AssetEntityDefinition memberAED = getSdxRetrievalService().getAssetEntityDefinitionByIds(colAED.getAsset(),
                  colAED.getTabl(), selectedColumn, member);
         memberMappings.addAll(getMappingRetrievalService().getMappingsForAED(memberAED.getId()));
      }
      iaMappings = getInstanceAssetMappings(memberMappings);
      return iaMappings;
   }

   private List<InstanceAssetMapping> getInstanceAssetMappings (List<Mapping> mappings) {
      List<InstanceAssetMapping> iaMappings = new ArrayList<InstanceAssetMapping>();
      for (Mapping mapping : mappings) {
         InstanceAssetMapping instanceAssetMapping = new InstanceAssetMapping();
         instanceAssetMapping.setId(mapping.getId());
         instanceAssetMapping.setInsId(mapping.getBusinessEntityDefinition().getInstance().getId());
         instanceAssetMapping.setAedId(mapping.getAssetEntityDefinition().getId());
         instanceAssetMapping.setBedId(mapping.getBusinessEntityDefinition().getId());
         instanceAssetMapping.setMappingType(MappingEntityType.EXISTING);
         instanceAssetMapping.setInstanceType(MappingEntityType.EXISTING);
         instanceAssetMapping.setMemDispName(mapping.getAssetEntityDefinition().getMembr().getLookupDescription());
         instanceAssetMapping.setColDispName(mapping.getAssetEntityDefinition().getColum().getName());
         instanceAssetMapping.setTblDispName(mapping.getAssetEntityDefinition().getTabl().getName());
         instanceAssetMapping.setConDispName(mapping.getBusinessEntityDefinition().getConcept().getDisplayName());
         instanceAssetMapping.setInstanceDispName(mapping.getBusinessEntityDefinition().getInstance().getDisplayName());
         iaMappings.add(instanceAssetMapping);
      }
      return iaMappings;
   }

   /**
    * This method generates the suggestions for all the members of a column
    */
   public InstanceMapping suggestInstanceMappingsForColumn (Long modelId, Long selColAedId, Long conBedId,
            InstanceMappingsPageProvider provider) throws ExeCueException {
      Model model = getKdxRetrievalService().getModelById(modelId);
      InstanceMapping instanceMapping = new InstanceMapping();
      List<InstanceAssetMapping> iaMappings = new ArrayList<InstanceAssetMapping>();
      AssetEntityDefinition colAED = getSdxRetrievalService().getPopulatedAssetEntityDefinitionById(selColAedId);
      String colDispName = colAED.getColum().getName();
      String tblDispName = colAED.getTabl().getName();
      BusinessEntityDefinition conceptBED = getKdxRetrievalService().getBusinessEntityDefinitionById(conBedId);
      List<Membr> members = getSdxRetrievalService().getColumnMembers(colAED.getColum());
      ColumnMapping columnMapping = getSdx2kdxMappingService().mapSDX2KDXForColumnMembers(colAED.getAsset(),
               colAED.getTabl(), colAED.getColum(), members, conceptBED, false, true, provider.getCounter(), model);
      if (columnMapping != null) {
         for (MemberMapping memberMapping : columnMapping.getMemberMappings()) {
            if (memberMapping != null) {
               iaMappings.add(prepareInstanceAssetMapping(memberMapping));
            }
         }
      }
      // set the mappings into the session
      provider.setInstanceMappings(iaMappings);
      if (iaMappings.size() == 0) {
         instanceMapping.setMsgType("W");
         instanceMapping.setMsg("Unable to suggest since all the members have been mapped");
      }
      instanceMapping.setMappings(provider.getInstanceMappings(colDispName, tblDispName));
      return instanceMapping;
   }

   private InstanceAssetMapping prepareInstanceAssetMapping (MemberMapping mapping) {
      InstanceAssetMapping iaMapping = new InstanceAssetMapping();
      if (logger.isDebugEnabled()) {
         logger.debug(mapping.getAssetEntityDefinition().getId() + " mapping.getAssetEntityDefinition().getId()");
         logger.debug(mapping.getBusinessEntityDefinition().getId() + " mapping.getBusinessEntityDefinition().getId()");
      }
      iaMapping.setAedId(mapping.getAssetEntityDefinition().getId());
      iaMapping.setBedId(mapping.getBusinessEntityDefinition().getId());
      iaMapping.setColDispName(mapping.getAssetEntityDefinition().getColum().getName());
      iaMapping.setConDispName(mapping.getBusinessEntityDefinition().getConcept().getDisplayName());
      iaMapping.setInstanceDispName(mapping.getBusinessEntityDefinition().getInstance().getDisplayName());
      Long instanceId = mapping.getBusinessEntityDefinition().getInstance().getId();
      if (instanceId == -1L) {
         iaMapping.setInstanceType(MappingEntityType.SUGGESTED);
      } else {
         iaMapping.setInstanceType(MappingEntityType.EXISTING);
      }
      iaMapping.setMappingType(MappingEntityType.SUGGESTED);
      iaMapping.setMemDispName(mapping.getAssetEntityDefinition().getMembr().getLookupDescription());
      iaMapping.setTblDispName(mapping.getAssetEntityDefinition().getTabl().getName());
      return iaMapping;
   }

   /**
    * This method retrieves the members for a column to be displayed in the pane1 of the instance-member mappings screen
    */
   public AssetColumnMember getColumnMembers (Long selColAedId) throws ExeCueException {
      AssetEntityDefinition colAED = getSdxRetrievalService().getPopulatedAssetEntityDefinitionById(selColAedId);
      List<Membr> members = getSdxRetrievalService().getColumnMembers(colAED.getColum());
      List<AssetMember> aMembers = new ArrayList<AssetMember>();
      AssetColumnMember colMember = new AssetColumnMember();
      colMember.setAedId(colAED.getId());
      colMember.setColDispName(colAED.getColum().getName());
      colMember.setId(colAED.getColum().getId());
      colMember.setTblDispName(colAED.getTabl().getName());
      for (Membr member : members) {
         AssetMember aMember = new AssetMember();
         AssetEntityDefinition memberAED = getSdxRetrievalService().getAssetEntityDefinitionByIds(colAED.getAsset(),
                  colAED.getTabl(), colAED.getColum(), member);
         aMember.setAedId(memberAED.getId());
         aMember.setDispName(memberAED.getMembr().getLookupDescription());
         aMember.setId(memberAED.getMembr().getId());
         aMembers.add(aMember);
      }
      colMember.setMembers(aMembers);
      return colMember;
   }

   // get column members by page
   public AssetColumnMember getColumnMembersByPage (Long selColAedId, Long pageNumber, Long pageSize)
            throws ExeCueException {
      int pageCount = 0;
      AssetEntityDefinition colAED = getSdxRetrievalService().getPopulatedAssetEntityDefinitionById(selColAedId);
      Long memberCount = getSdxRetrievalService().getTotalMembersCountOfColumn(colAED.getColum());
      pageCount = calculatePageCount(memberCount.intValue(), pageSize.intValue());
      if (pageNumber > pageCount) {
         // Error : show the first page
         pageNumber = 1L;
      }
      List<Membr> members = getSdxRetrievalService().getColumnMembersByPage(colAED.getColum(), pageNumber, pageSize);
      List<AssetMember> aMembers = new ArrayList<AssetMember>();
      AssetColumnMember colMember = new AssetColumnMember();
      colMember.setAedId(colAED.getId());
      colMember.setColDispName(colAED.getColum().getName());
      colMember.setId(colAED.getColum().getId());
      colMember.setTblDispName(colAED.getTabl().getName());
      for (Membr member : members) {
         AssetMember aMember = new AssetMember();
         AssetEntityDefinition memberAED = getSdxRetrievalService().getAssetEntityDefinitionByIds(colAED.getAsset(),
                  colAED.getTabl(), colAED.getColum(), member);
         aMember.setAedId(memberAED.getId());
         aMember.setDispName(memberAED.getMembr().getLookupDescription());
         aMember.setId(memberAED.getMembr().getId());
         aMembers.add(aMember);
      }
      colMember.setMembers(aMembers);

      MappingHeader header = new MappingHeader();
      header.setCurrentPage(pageNumber.intValue());
      header.setTotalPages(pageCount);
      colMember.setHeader(header);
      return colMember;
   }

   public MappableInstanceTerm showInstances (Long modelId, Long conBedId) throws ExeCueException {
      MappableInstanceTerm instanceTerm = new MappableInstanceTerm();
      BusinessEntityDefinition conceptBED = getKdxRetrievalService().getBusinessEntityDefinitionById(conBedId);
      Concept concept = conceptBED.getConcept();
      List<BusinessEntityDefinition> instanceBEDList = getKdxRetrievalService().getInstanceBEDsByConceptId(modelId,
               concept.getId());
      List<TermInfo> terms = new ArrayList<TermInfo>();
      instanceTerm.setConDispName(concept.getDisplayName());
      instanceTerm.setBedId(conceptBED.getId());
      if (ExecueCoreUtil.isCollectionNotEmpty(instanceBEDList)) {
         for (BusinessEntityDefinition bed : instanceBEDList) {
            TermInfo term = new TermInfo();
            term.setBedId(bed.getId());
            term.setDispName(bed.getInstance().getDisplayName());
            term.setId(bed.getInstance().getId());
            term.setName(bed.getInstance().getName());
            terms.add(term);
         }
         instanceTerm.setTerms(terms);
      } else {
         instanceTerm.setErrorMsg("There are no instances for the selected concept '" + concept.getName() + "'");
      }
      return instanceTerm;
   }

   public MappableInstanceTerm retrieveInstancesByPage (Long conBedId, Long pageNumber, Long pageSize)
            throws ExeCueException {
      int pageCount = 0;

      MappableInstanceTerm instanceTerm = new MappableInstanceTerm();
      BusinessEntityDefinition conceptBED = getKdxRetrievalService().getBusinessEntityDefinitionById(conBedId);
      Concept concept = conceptBED.getConcept();
      Long instanceCount = getKdxRetrievalService().getInstanceBEDsCountByConceptId(concept.getId());
      pageCount = calculatePageCount(instanceCount.intValue(), pageSize.intValue());
      if (pageNumber > pageCount) {
         // Error : show the first page
         pageNumber = 1L;
      }
      List<BusinessEntityDefinition> instanceBEDList = getKdxRetrievalService().getInstanceBEDsByPage(concept.getId(),
               pageNumber, pageSize);
      List<TermInfo> terms = new ArrayList<TermInfo>();
      instanceTerm.setConDispName(concept.getDisplayName());
      instanceTerm.setBedId(conceptBED.getId());
      if (ExecueCoreUtil.isCollectionNotEmpty(instanceBEDList)) {
         for (BusinessEntityDefinition bed : instanceBEDList) {
            TermInfo term = new TermInfo();
            term.setBedId(bed.getId());
            term.setDispName(bed.getInstance().getDisplayName());
            term.setId(bed.getInstance().getId());
            term.setName(bed.getInstance().getName());
            terms.add(term);
         }
         instanceTerm.setTerms(terms);
      } else {
         instanceTerm.setErrorMsg("There are no instances for the selected concept '" + concept.getName() + "'");
      }
      // add the pagination details
      MappingHeader header = new MappingHeader();
      header.setCurrentPage(pageNumber.intValue());
      header.setTotalPages(pageCount);
      instanceTerm.setHeader(header);
      return instanceTerm;
   }

   /**
    * This method is for displaying the auto suggestions when the user tries to map a member to instance
    */
   public List<TermInfo> suggestInstances (Long modelId, Long conceptBEDId, String searchString) throws ExeCueException {
      List<TermInfo> instances = null;
      try {
         BusinessEntityDefinition bed = getKdxRetrievalService().getBusinessEntityDefinitionById(conceptBEDId);
         List<Instance> suggestedInstances = getKdxRetrievalService().getInstancesOfConceptBySearchString(modelId,
                  bed.getConcept().getId(), searchString);
         if ((suggestedInstances != null) && (suggestedInstances.size() > 0)) {
            instances = new ArrayList<TermInfo>();
            for (Instance instance : suggestedInstances) {
               BusinessEntityDefinition instanceBED = getKdxRetrievalService().getBusinessEntityDefinitionByIds(
                        modelId, bed.getConcept().getId(), instance.getId());
               TermInfo iInfo = new TermInfo();
               iInfo.setBedId(instanceBED.getId());
               iInfo.setDispName(instance.getDisplayName());
               iInfo.setId(instance.getId());
               iInfo.setName(instance.getName());
               // iInfo.setMapped(false);
               instances.add(iInfo);
            }
         }
      } catch (KDXException e) {
         e.printStackTrace();
         throw new ExeCueException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e.getMessage());
      }
      return instances;
   }

   public InstanceMapping saveInstanceMappings (Long modelId, Long selColAedId, Long selConBedId,
            List<SaveMapping> saveMappings, InstanceMappingsPageProvider provider) throws ExeCueException {
      String colDispName = null;
      String tblDispName = null;
      InstanceMapping instanceMapping = new InstanceMapping();
      List<TermInfo> newTerms = new ArrayList<TermInfo>();
      List<InstanceAssetMapping> finalList = new ArrayList<InstanceAssetMapping>();
      List<InstanceAssetMapping> toBeSavedIAMs = new ArrayList<InstanceAssetMapping>();
      List<InstanceAssetMapping> toBeDeletedIAMs = new ArrayList<InstanceAssetMapping>();
      boolean validFlag = true;
      try {
         for (SaveMapping saveMapping : saveMappings) {
            InstanceAssetMapping instanceAssetMapping = provider.getInstanceMappingByAEDId(saveMapping.getAedId());
            if (instanceAssetMapping == null) {
               // if there is no match in the provider, then create a new
               // IAM for the saveMapping
               instanceAssetMapping = convertToInstanceAssetMapping(saveMapping);
               // add it to the provider
               provider.addInstanceMapping(instanceAssetMapping);
            }

            if (colDispName == null) {
               colDispName = instanceAssetMapping.getColDispName();
            }
            if (tblDispName == null) {
               tblDispName = instanceAssetMapping.getTblDispName();
            }

            boolean deleteMapping = Boolean.parseBoolean(saveMapping.getDelMap());

            // mapping unchecked on the UI
            if (deleteMapping) {
               toBeDeletedIAMs.add(instanceAssetMapping);
            } else {
               // 1. update the provider with the latest changes available
               // in saveMapping
               mergeMappingDetails(saveMapping, instanceAssetMapping);
               // 2. clear the errors/warnings if any; Will be added in
               // validation phase
               clearMessage(instanceAssetMapping);
               // 3. validate the save mappings for errors/warnings
               // CRITICAL step that performs the validation
               boolean tempValidFlag = isValidMapping(instanceAssetMapping, provider);
               if (validFlag) {
                  validFlag = tempValidFlag;
               }
               // move the invalid mapping to the top of the list
               if (tempValidFlag) {
                  toBeSavedIAMs.add(instanceAssetMapping);
               } else {
                  toBeSavedIAMs.add(0, instanceAssetMapping);
               }
            } // else ends
         } // for ends
         // check for errors
         if (validFlag) { // complete save process
            // first process the delete list;
            if (toBeDeletedIAMs.size() > 0) {
               // to be deleted from SWI
               List<Mapping> delMappings = new ArrayList<Mapping>();
               for (InstanceAssetMapping delMapping : toBeDeletedIAMs) {
                  if (MappingEntityType.EXISTING.equals(delMapping.getMappingType())) {
                     Mapping entry = new Mapping();
                     entry.setId(delMapping.getId());
                     delMappings.add(entry);
                  }
                  // delete mapping from the provider
                  provider.deleteInstanceMapping(delMapping);
               }
               // delete from SWI
               getMappingDeletionService().deleteUIMappings(delMappings);
            }
            // attempt to save the checked mappings
            // updated the provider only with the unchecked mappings until
            // this point
            for (InstanceAssetMapping iaMapping : toBeSavedIAMs) {
               AssetEntityDefinition aed = getSdxRetrievalService().getPopulatedAssetEntityDefinitionById(
                        iaMapping.getAedId());
               BusinessEntityDefinition bed = null;
               try {
                  bed = getKdxRetrievalService().getBusinessEntityDefinitionById(iaMapping.getBedId());
               } catch (KDXException ke) {
               }
               Mapping swiMapping = null;
               if (bed != null) {
                  List<Mapping> swiMappings = getMappingRetrievalService().getMappingsForAED(aed.getId());
                  if ((swiMappings != null) && (swiMappings.size() > 0)) {
                     // swiMapping = swiMappings.get(0);
                     swiMapping = getMappingRetrievalService().getMappingsForAED(aed.getId()).get(0);
                  }
               }
               if (swiMapping == null) { // no mapping found
                  // applicable to only NEW and SUGGESTED type mappings
                  if (bed == null) { // SUGGESTED type with newly created
                     // instance
                     Model model = getKdxRetrievalService().getModelById(modelId);
                     // check if any instance exists by the same name
                     bed = getKdxRetrievalService().getBEDByInstanceDisplayName(model.getName(),
                              iaMapping.getConDispName(), iaMapping.getInstanceDispName());
                     logger.debug("Trying to get instance from SWI : " + model.getName() + "; "
                              + iaMapping.getConDispName() + "; " + iaMapping.getInstanceDispName());
                     logger.debug("The BED from SWI before creating new instance : " + bed);
                     if (bed == null) {
                        // create the instance and get its DED
                        Instance newInstance = new Instance();
                        newInstance.setDisplayName(iaMapping.getInstanceDispName());
                        Concept concept = getKdxRetrievalService().getConceptByDisplayName(modelId,
                                 iaMapping.getConDispName());
                        getBusinessEntityManagementWrapperService().createInstance(modelId, concept.getId(),
                                 newInstance);
                        bed = getKdxRetrievalService().getBusinessEntityDefinitionByIds(modelId, concept.getId(),
                                 newInstance.getId());
                        TermInfo termInfo = new TermInfo();
                        termInfo.setId(newInstance.getId());
                        termInfo.setBedId(bed.getId());
                        termInfo.setMapped(true);
                        termInfo.setName(newInstance.getName());
                        termInfo.setDispName(newInstance.getDisplayName());
                        newTerms.add(termInfo);
                     }
                  }
                  // create the mapping as the mapping is not existing
                  List<Mapping> mappings = new ArrayList<Mapping>();
                  swiMapping = new Mapping();
                  swiMapping.setAssetEntityDefinition(aed);
                  swiMapping.setBusinessEntityDefinition(bed);
                  mappings.add(swiMapping);
                  getMappingManagementService().persistUIMappings(mappings);
                  // update the IAM
                  iaMapping.setId(mappings.get(0).getId());
                  iaMapping.setMappingType(MappingEntityType.EXISTING);
                  iaMapping.setInstanceType(MappingEntityType.EXISTING);
                  iaMapping.setBedId(bed.getId());
                  iaMapping.setInsId(bed.getInstance().getId());
                  // update the display name field of other IAMs which
                  // have the same BED id in our cache
                  for (InstanceAssetMapping match : provider.getInstanceMappingsByBEDid(iaMapping.getBedId())) {
                     match.setInstanceDispName(iaMapping.getInstanceDispName());
                  }
               } else {
                  // get the existing DED in SWI that might have got
                  // changed by the user
                  BusinessEntityDefinition oldBED = swiMapping.getBusinessEntityDefinition();
                  // update the instance's display name
                  Instance instance = bed.getInstance();
                  instance.setDisplayName(iaMapping.getInstanceDispName());
                  // update the display
                  getKdxManagementService().updateInstance(modelId, bed.getConcept().getId(), instance);
                  swiMapping.setBusinessEntityDefinition(bed);
                  // update the existing mapping with the ded if the ded
                  // is different from the original
                  if (oldBED.getId() != bed.getId()) {
                     getMappingManagementService().updateMapping(swiMapping);
                  }
                  // update the provider
                  List<InstanceAssetMapping> matchBEDMappings = provider.getInstanceMappingsByBEDid(oldBED.getId());
                  if (matchBEDMappings != null) {
                     for (InstanceAssetMapping match : matchBEDMappings) {
                        match.setInstanceDispName(iaMapping.getInstanceDispName());
                        match.setInsId(iaMapping.getInsId());
                        match.setBedId(iaMapping.getBedId());
                     }
                  }
               }
            }
            instanceMapping.setMappings(provider.getInstanceMappings(colDispName, tblDispName));
            instanceMapping.setNewInstances(newTerms);
         } else {
            finalList.addAll(toBeSavedIAMs);
            finalList.addAll(toBeDeletedIAMs);
            instanceMapping.setMappings(finalList);
         }
      } catch (Exception e) {
         e.printStackTrace();
         throw new ExeCueException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
      return instanceMapping;
   }

   private boolean isValidMapping (InstanceAssetMapping toBeSavedIAM, InstanceMappingsPageProvider provider) {
      // Rule : No two member-instance mappings can have the same instance
      boolean flag = true;
      String colDispName = toBeSavedIAM.getColDispName();
      String tblDispName = toBeSavedIAM.getTblDispName();
      List<InstanceAssetMapping> iams = provider.getInstanceMappings(colDispName, tblDispName);
      for (InstanceAssetMapping iam : iams) {
         if ((toBeSavedIAM.getAedId() != iam.getAedId())
                  && toBeSavedIAM.getInstanceDispName().equals(iam.getInstanceDispName())) {
            logger.debug("Instance names cannot be duplicate " + toBeSavedIAM);
            toBeSavedIAM.setMsgType("E");
            toBeSavedIAM.setMsg("An instance cannot be used in more than one mapping.");
            flag = false;
         }
      }
      return flag;
   }

   private void clearMessage (InstanceAssetMapping instanceAssetMapping) {
      instanceAssetMapping.setMsg(null);
      instanceAssetMapping.setMsgType(null);
   }

   private void mergeMappingDetails (SaveMapping saveMapping, InstanceAssetMapping instanceAssetMapping) {
      instanceAssetMapping.setBedId(saveMapping.getBedId());
      instanceAssetMapping.setInstanceDispName(saveMapping.getDispName());
      instanceAssetMapping.setInstanceType(saveMapping.getBedType());
   }

   public Long getUnmappedMemberCount (Long columnAedId) throws ExeCueException {
      return getSdx2kdxMappingService().getUnmappedMemberCount(columnAedId);
   }

   public Long suggestMemberMappingsByJob (Long modelId, Long assetId, Long selColAedId, Long selConBedId)
            throws ExeCueException {
      Long jobRequestId = null;
      try {
         InstanceAbsorptionContext instanceAbsorptionContext = new InstanceAbsorptionContext();
         instanceAbsorptionContext.setModelId(modelId);
         instanceAbsorptionContext.setAssetId(assetId);
         instanceAbsorptionContext.setColumnAedId(selColAedId);
         instanceAbsorptionContext.setConceptBedId(selConBedId);
         instanceAbsorptionContext.setUserId(getUserContext().getUser().getId());
         instanceAbsorptionContext.setGenerateSuggestionMappings(true);
         jobRequestId = getInstanceAbsorptionJobService().scheduleInstanceAbsorptionJob(instanceAbsorptionContext);
      } catch (BatchMaintenanceException batchMaintenanceException) {
         throw new ExeCueException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, batchMaintenanceException);
      }
      return jobRequestId;
   }

   public InstanceMapping suggestMemberMappings (Long modelId, Long selColAedId, Long conBedId) throws ExeCueException {
      InstanceMapping instanceMapping = new InstanceMapping();
      // TODO: -JVK- ensure that the temp tables are in sync - delete the
      // parent if there are no children
      InstanceAbsorptionContext instanceAbsorptionContext = new InstanceAbsorptionContext();
      instanceAbsorptionContext.setModelId(modelId);
      instanceAbsorptionContext.setColumnAedId(selColAedId);
      instanceAbsorptionContext.setConceptBedId(conBedId);
      instanceAbsorptionContext.setUserId(1L);
      instanceAbsorptionContext.setGenerateSuggestionMappings(true);
      String statusMessage = getSdx2kdxMappingService().generateInstanceMappingSuggestions(instanceAbsorptionContext);
      if (statusMessage.equals("SUCCESS")) {
         // retrieve the suggestions by the column AED id
         // TODO: -JVK- use separate Hibernate Callback similar to DB
         // pagination callback to retrieve page wise results
         List<InstanceMappingSuggestionDetail> suggestionDetailsByColumAEDId = getInstanceMappingSuggestionService()
                  .getInstanceMappingSuggestionDetailsByColumAEDId(selColAedId);
         // for now get the total list and then pull out the results page
         // wise
         // TODO: add to constants
         int fromIndex = 0;
         List<InstanceMappingSuggestionDetail> suggestionsByPage = null;
         if (suggestionDetailsByColumAEDId.size() > PAGE_SIZE) {
            suggestionsByPage = suggestionDetailsByColumAEDId.subList(fromIndex, PAGE_SIZE);
         } else {
            suggestionsByPage = suggestionDetailsByColumAEDId;
         }
         if (ExecueCoreUtil.isCollectionNotEmpty(suggestionsByPage)) {
            // get the suggestion object
            // InstanceMappingSuggestion instanceMappingSuggestion =
            // getInstanceMappingSuggestionService()
            // .getInstanceMappingSuggestion(selColAedId);
            // for (InstanceMappingSuggestionDetail
            // instanceMappingSuggestionDetail : suggestionsByPage) {
            // iaMapping =
            // prepareInstanceAssetMapping(instanceMappingSuggestion,
            // instanceMappingSuggestionDetail);
            // iaMappings.add(iaMapping);
            // instanceMapping.setMappings(iaMappings);
            // }
            instanceMapping = retrieveMemberMappingsByPage(selColAedId, 1, null);
         } else {
            instanceMapping.setMsgType("W");
            instanceMapping.setMsg("Unable to suggest since all the members have been mapped");
         }
      } else {
         // handle error
         instanceMapping.setMsgType("E");
         instanceMapping.setMsg(statusMessage);
      }
      return instanceMapping;
   }

   private InstanceAssetMapping prepareInstanceAssetMapping (InstanceMappingSuggestion instanceMappingSuggestion,
            InstanceMappingSuggestionDetail suggestionDetail) {
      InstanceAssetMapping iaMapping = null;
      try {
         AssetEntityDefinition memberAED = getSdxRetrievalService().getAssetEntityDefinitionById(
                  suggestionDetail.getMembrAEDId());
         iaMapping = new InstanceAssetMapping();
         iaMapping.setId(suggestionDetail.getId());
         iaMapping.setAedId(memberAED.getId());
         iaMapping.setBedId(suggestionDetail.getInstanceBEDId());
         iaMapping.setColDispName(memberAED.getColum().getName());
         iaMapping.setConDispName(instanceMappingSuggestion.getConceptDisplayName());
         iaMapping.setInstanceDispName(suggestionDetail.getInstanceDisplayName());
         Long instanceId = suggestionDetail.getInstanceId();
         if (instanceId == -1L) {
            iaMapping.setInstanceType(MappingEntityType.SUGGESTED);
         } else {
            iaMapping.setInstanceType(MappingEntityType.EXISTING);
         }
         iaMapping.setMappingType(MappingEntityType.SUGGESTED);
         iaMapping.setMemDispName(memberAED.getMembr().getLookupDescription());
         iaMapping.setTblDispName(memberAED.getTabl().getName());
      } catch (SDXException sdxException) {
         sdxException.printStackTrace();
      }
      return iaMapping;
   }

   public Long getInstanceMappingSuggestionDetailsCount (Long columnAedId) throws ExeCueException {
      return getInstanceMappingSuggestionService().getInstanceMappingSuggestionDetailsCount(columnAedId);
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.handler.swi.mappings.IMappingServiceHandler#saveMemberMappingsByJob(java.lang.Long,
    *      java.lang.Long, java.lang.Long, java.lang.Long)
    */
   public Long saveMemberMappingsByJob (Long modelId, Long assetId, Long selColAedId, Long selConBedId)
            throws ExeCueException {
      Long jobRequestId = null;
      try {
         InstanceAbsorptionContext instanceAbsorptionContext = new InstanceAbsorptionContext();
         instanceAbsorptionContext.setModelId(modelId);
         instanceAbsorptionContext.setAssetId(assetId);
         instanceAbsorptionContext.setColumnAedId(selColAedId);
         instanceAbsorptionContext.setConceptBedId(selConBedId);
         instanceAbsorptionContext.setUserId(getUserContext().getUser().getId());
         instanceAbsorptionContext.setGenerateSuggestionMappings(false); // Save
         // suggest
         // mappings
         // instead
         // of
         // generating
         jobRequestId = getInstanceAbsorptionJobService().scheduleInstanceAbsorptionJob(instanceAbsorptionContext);
      } catch (BatchMaintenanceException batchMaintenanceException) {
         throw new ExeCueException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, batchMaintenanceException);
      }
      return jobRequestId;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.handler.swi.mappings.IMappingServiceHandler#getMaxAllowedSuggestions()
    */
   public Long getMaxAllowedSuggestions () {
      return getCoreConfigurationService().getMaxAllowedInstanceMappingSuggestions();
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.handler.swi.mappings.IMappingServiceHandler#saveMemberMappings(int, java.lang.Long,
    *      java.lang.Long, java.lang.Long, com.execue.handler.bean.mapping.InstanceMapping, boolean)
    */
   public InstanceMapping saveMemberMappings (int pageNumber, Long modelId, Long selColAedId, Long selConBedId,
            InstanceMapping instanceMapping, boolean saveAll) throws ExeCueException {
      // TODO: -JVK- error handling - when save mappings fails, return with
      // the proper message and the same mappings
      // which UI gave as input
      // collect the InstanceAssetMappings which need to be returned to UI in
      // case of error
      List<InstanceAssetMapping> iaMappings = instanceMapping.getMappings();
      // first save the page mappings
      savePageMappings(modelId, selColAedId, selConBedId, iaMappings);
      // check the save all flag
      if (saveAll) {
         Long suggestionCount = getInstanceMappingSuggestionService().getInstanceMappingSuggestionDetailsCount(
                  selColAedId);
         int batchSize = getCoreConfigurationService().getKDXInstanceMappingBatchSize();

         int noOfBatches = calculatePageCount(suggestionCount.intValue(), batchSize);
         // get the suggestion object
         InstanceMappingSuggestion instanceMappingSuggestion = getInstanceMappingSuggestionService()
                  .getInstanceMappingSuggestion(selColAedId);
         List<InstanceMappingSuggestionDetail> suggestionsBatch = null;
         for (int batchNum = 1; batchNum <= noOfBatches; batchNum++) {
            suggestionsBatch = getInstanceMappingSuggestionService().getInstanceMappingSuggestionDetailsByBatchAndSize(
                     selColAedId, 1L, new Long(batchSize));
            getSdx2kdxMappingService().saveBatchMappings(instanceMappingSuggestion, suggestionsBatch, modelId);
         }
         // get the first page of the existing mappings and return
         instanceMapping = retrieveMemberMappingsByPage(selColAedId, 1, MappingEntityType.EXISTING);
      } else {
         instanceMapping = retrieveMemberMappingsByPage(selColAedId, pageNumber, null);
      }
      // finally delete the InstanceMappingSuggestion of the columnAEDId
      cleanUpInstanceMappingSuggestions(selColAedId);

      return instanceMapping;
   }

   /**
    * @param selColAedId
    * @throws MappingException
    */
   private void cleanUpInstanceMappingSuggestions (Long selColAedId) throws MappingException {
      // check if there are any suggestions for the columnAEDId
      Long finalSuggestionCount = getInstanceMappingSuggestionService().getInstanceMappingSuggestionDetailsCount(
               selColAedId);
      if (finalSuggestionCount == 0) {
         try {
            getInstanceMappingSuggestionService().deleteInstanceMappingSuggestionByColumnAEDId(selColAedId);
         } catch (Exception exception) {
            // exception since there is no row to delete - do nothing
         }
      }
   }

   private List<InstanceAssetMapping> constructInstanceAssetMappings (List<Mapping> mappings) {
      List<InstanceAssetMapping> iaMappings = new ArrayList<InstanceAssetMapping>();
      for (Mapping mapping : mappings) {
         InstanceAssetMapping iaMapping = new InstanceAssetMapping();
         AssetEntityDefinition aed = mapping.getAssetEntityDefinition();
         BusinessEntityDefinition bed = mapping.getBusinessEntityDefinition();
         iaMapping.setId(mapping.getId());
         iaMapping.setMappingType(MappingEntityType.EXISTING);
         iaMapping.setAedId(aed.getId());
         iaMapping.setBedId(bed.getId());
         iaMapping.setColDispName(aed.getColum().getName());
         iaMapping.setMemDispName(aed.getMembr().getLookupDescription());
         iaMapping.setConDispName(bed.getConcept().getDisplayName());
         iaMapping.setInsId(bed.getInstance().getId());
         iaMapping.setInstanceDispName(bed.getInstance().getDisplayName());
         iaMapping.setInstanceType(MappingEntityType.EXISTING);
         iaMapping.setTblDispName(aed.getTabl().getName());
         iaMappings.add(iaMapping);
      }
      return iaMappings;
   }

   private BusinessEntityDefinition prepareBEDForInstanceAssetMapping (InstanceAssetMapping instanceAssetMapping,
            Long modelId) throws KDXException {
      BusinessEntityDefinition mappedInstanceBED = null;
      // TODO: review: NEW Case will not occur
      // if
      // (MappingEntityType.NEW.equals(instanceAssetMapping.getInstanceType()))
      // {
      // mappedInstanceBED =
      // createInstance(instanceAssetMapping.getConDispName(),
      // instanceAssetMapping
      // .getInstanceDispName(), modelId);
      // } else if
      // (MappingEntityType.EXISTING.equals(instanceAssetMapping.getInstanceType()))
      // {
      if (MappingEntityType.EXISTING.equals(instanceAssetMapping.getInstanceType())) {
         // get the instance from SWI and check the display name with the
         // current display name
         mappedInstanceBED = getKdxRetrievalService().getBusinessEntityDefinitionById(instanceAssetMapping.getBedId());
         Instance swiInstance = mappedInstanceBED.getInstance();
         if (!mappedInstanceBED.getInstance().getDisplayName().equals(instanceAssetMapping.getInstanceDispName())) {
            // update the instance
            swiInstance.setDisplayName(instanceAssetMapping.getInstanceDispName());
            getKdxManagementService().updateInstance(modelId, mappedInstanceBED.getConcept().getId(), swiInstance);
         }
         // if they are different, update the instance's display name
      } else if (MappingEntityType.SUGGESTED.equals(instanceAssetMapping.getInstanceType())) {
         mappedInstanceBED = createInstance(instanceAssetMapping.getConDispName(), instanceAssetMapping
                  .getInstanceDispName(), modelId);
      }
      return mappedInstanceBED;
   }

   private BusinessEntityDefinition createInstance (String conceptDisplayName, String instanceDisplayName, Long modelId) {
      BusinessEntityDefinition instanceBED = null;
      // BusinessEntityDefinition mappedInstanceBED = null;
      // // TODO: -JVK- ensure that the code value
      // SWIExceptionCodes.ENTITY_ALREADY_EXISTS is in sync with this value
      // int ENTITY_ALREADY_EXISTS = 900018;
      // Instance newInstance = new Instance();
      // newInstance.setDisplayName(instanceDisplayName);
      // Concept concept = null;
      // try {
      // concept = getKdxRetrievalService().getConceptByDisplayName(modelId,
      // conceptDisplayName);
      // } catch (KDXException e) {
      // e.printStackTrace();
      // }
      // try {
      // getKdxManagementService().createInstance(modelId, concept.getId(),
      // newInstance);
      // } catch (KDXException e) {
      // if (e.getCode() == ENTITY_ALREADY_EXISTS) {
      // try {// get the BED
      // Model model = getKdxRetrievalService().getModelById(modelId);
      // mappedInstanceBED =
      // getKdxRetrievalService().getBEDByInstanceDisplayName(model.getName(),
      // conceptDisplayName, instanceDisplayName);
      // } catch (KDXException e1) {
      // e1.printStackTrace();
      // }
      // }
      // }
      // if (mappedInstanceBED == null) {
      // try {
      // mappedInstanceBED =
      // getKdxRetrievalService().getBusinessEntityDefinitionByIds(modelId,
      // concept.getId(),
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

   private void savePageMappings (Long modelId, Long selColAedId, Long selConBedId,
            List<InstanceAssetMapping> iaMappings) throws SDXException, KDXException, MappingException {
      // Collect the temp table ids in a list
      List<Long> suggestionsToBeDeleted = new ArrayList<Long>();
      // Collect the mapping objects to persist
      List<Mapping> mappingsToBeSaved = new ArrayList<Mapping>();
      // Collect all the existing mappings to be deleted
      List<Mapping> mappingsToBeDeleted = new ArrayList<Mapping>();
      AssetEntityDefinition aed = null;
      for (InstanceAssetMapping iaMapping : iaMappings) {
         aed = getSdxRetrievalService().getPopulatedAssetEntityDefinitionById(iaMapping.getAedId());
         // check the type of mapping
         if (MappingEntityType.SUGGESTED.equals(iaMapping.getMappingType())) {
            if (!iaMapping.isDeleteMapping()) {
               // It is a suggested mapping so first take the instance
               // display name as is and create it
               BusinessEntityDefinition mappedInstanceBED = prepareBEDForInstanceAssetMapping(iaMapping, modelId);
               // persist in mapping table and delete from temp
               Mapping swiMapping = new Mapping();
               swiMapping.setAssetEntityDefinition(aed);
               swiMapping.setBusinessEntityDefinition(mappedInstanceBED);
               mappingsToBeSaved.add(swiMapping);
               suggestionsToBeDeleted.add(iaMapping.getId());
            } else {
               suggestionsToBeDeleted.add(iaMapping.getId());
            }
         } else if (MappingEntityType.EXISTING.equals(iaMapping.getMappingType())) {
            if (!iaMapping.isDeleteMapping()) {
               BusinessEntityDefinition mappedInstanceBED = prepareBEDForInstanceAssetMapping(iaMapping, modelId);
               // check if the BED id is same the bed id found in the swi
               // mapping
               Mapping swiMapping = getMappingRetrievalService().getMapping(iaMapping.getId());
               if (!(swiMapping.getBusinessEntityDefinition().getId().longValue() == mappedInstanceBED.getId()
                        .longValue())) {
                  swiMapping.setBusinessEntityDefinition(mappedInstanceBED);
                  getMappingManagementService().updateMapping(swiMapping);
               }
            } else {
               // delete from mapping table
               Mapping deleteMapping = new Mapping();
               deleteMapping.setId(iaMapping.getId());
               mappingsToBeDeleted.add(deleteMapping);
            }
         } else if (MappingEntityType.NEW.equals(iaMapping.getMappingType())) {
            if (!iaMapping.isDeleteMapping()) {
               BusinessEntityDefinition mappedInstanceBED = prepareBEDForInstanceAssetMapping(iaMapping, modelId);
               Mapping swiMapping = new Mapping();
               swiMapping.setAssetEntityDefinition(aed);
               swiMapping.setBusinessEntityDefinition(mappedInstanceBED);
               mappingsToBeSaved.add(swiMapping);
            }
         }
      }
      // now perform the save operation in a transaction
      performSave(mappingsToBeSaved, mappingsToBeDeleted, suggestionsToBeDeleted);
   }

   // TODO: Use spring to handle the transaction
   private void performSave (List<Mapping> mappingsToBeSaved, List<Mapping> mappingsToBeDeleted,
            List<Long> suggestionsToBeDeleted) throws MappingException {
      // delete the mappings
      if (ExecueCoreUtil.isCollectionNotEmpty(mappingsToBeDeleted)) {
         getMappingDeletionService().deleteUIMappings(mappingsToBeDeleted);
      }
      // save the mappings
      if (ExecueCoreUtil.isCollectionNotEmpty(mappingsToBeSaved)) {
         getMappingManagementService().createMappings(mappingsToBeSaved);
      }
      // delete the suggestions
      if (ExecueCoreUtil.isCollectionNotEmpty(suggestionsToBeDeleted)) {
         getInstanceMappingSuggestionService().deleteInstanceMappingSuggestionDetails(suggestionsToBeDeleted);
      }
   }

   private List<InstanceAssetMapping> convertToInstanceAssetMappings (Long conceptBEDId, List<SaveMapping> saveMappings)
            throws SDXException, KDXException {
      List<InstanceAssetMapping> iaMappings = new ArrayList<InstanceAssetMapping>();
      InstanceAssetMapping iaMapping = null;
      AssetEntityDefinition memberAED = null;
      BusinessEntityDefinition conceptBED = getKdxRetrievalService().getBusinessEntityDefinitionById(conceptBEDId);
      for (SaveMapping saveMapping : saveMappings) {
         iaMapping = new InstanceAssetMapping();
         memberAED = getSdxRetrievalService().getPopulatedAssetEntityDefinitionById(saveMapping.getAedId());
         iaMapping.setId(saveMapping.getId());
         iaMapping.setMappingType(saveMapping.getMapType());
         iaMapping.setAedId(saveMapping.getAedId());
         iaMapping.setBedId(saveMapping.getBedId());
         iaMapping.setColDispName(memberAED.getColum().getName());
         iaMapping.setMemDispName(memberAED.getMembr().getLookupDescription());
         iaMapping.setConDispName(conceptBED.getConcept().getDisplayName());
         iaMapping.setInstanceDispName(saveMapping.getDispName());
         iaMapping.setInstanceType(saveMapping.getBedType());
         iaMapping.setTblDispName(memberAED.getTabl().getName());
         iaMapping.setDeleteMapping(Boolean.parseBoolean(saveMapping.getDelMap()));
         iaMappings.add(iaMapping);
      }
      return iaMappings;
   }

   public InstanceMapping retrieveMemberMappingsByPage (Long columnAEDId, int pageNumber, String mappingFilter)
            throws ExeCueException {
      InstanceMapping instanceMapping = new InstanceMapping();
      List<InstanceAssetMapping> pageMappings = new ArrayList<InstanceAssetMapping>();
      int pageCount = 0;
      try {
         if (mappingFilter == null) {
            int suggestionsCount = getInstanceMappingSuggestionService().getInstanceMappingSuggestionDetailsCount(
                     columnAEDId).intValue();
            // TODO -NK- PAGE_SIZE Should come from the UI or configuration
            int suggestionsPageCount = calculatePageCount(suggestionsCount, PAGE_SIZE);
            int mappingsCount = getMappingRetrievalService().getMappedMemberCount(columnAEDId).intValue();
            int totalMappingsCount = suggestionsCount + mappingsCount;
            pageCount = calculatePageCount(totalMappingsCount, PAGE_SIZE);
            int remainder = suggestionsCount % PAGE_SIZE;
            if (remainder == 0) { // this means that the suggestions
               // correctly fit into the page
               if (pageNumber <= suggestionsPageCount) { // all are
                  // suggestions
                  InstanceMappingSuggestion instanceMappingSuggestion = getInstanceMappingSuggestionService()
                           .getInstanceMappingSuggestion(columnAEDId);
                  List<InstanceMappingSuggestionDetail> suggestions = getInstanceMappingSuggestionService()
                           .getInstanceMappingSuggestionDetailsByBatchAndSize(columnAEDId, new Long(pageNumber),
                                    new Long(PAGE_SIZE));
                  for (InstanceMappingSuggestionDetail suggestion : suggestions) {
                     InstanceAssetMapping instanceAssetMapping = prepareInstanceAssetMapping(instanceMappingSuggestion,
                              suggestion);
                     pageMappings.add(instanceAssetMapping);
                  }
               } else {
                  // all are existing mappings
                  int dbPageNumber = pageNumber - suggestionsPageCount;
                  List<Mapping> mappings = getMappingRetrievalService().getMemberMappingsByPage(columnAEDId,
                           dbPageNumber, PAGE_SIZE);
                  pageMappings = constructInstanceAssetMappings(mappings);
               }
            } else {
               // check if the page number is falling on the last page of
               // the suggestions - page has combination of
               // suggestions and existing mappings
               if (pageNumber == suggestionsPageCount) {
                  // first get the suggestions
                  InstanceMappingSuggestion instanceMappingSuggestion = getInstanceMappingSuggestionService()
                           .getInstanceMappingSuggestion(columnAEDId);
                  List<InstanceMappingSuggestionDetail> suggestions = getInstanceMappingSuggestionService()
                           .getInstanceMappingSuggestionDetailsByBatchAndSize(columnAEDId, new Long(pageNumber),
                                    new Long(PAGE_SIZE));
                  for (InstanceMappingSuggestionDetail suggestion : suggestions) {
                     InstanceAssetMapping instanceAssetMapping = prepareInstanceAssetMapping(instanceMappingSuggestion,
                              suggestion);
                     pageMappings.add(instanceAssetMapping);
                  }
                  int offset = PAGE_SIZE - remainder;
                  List<Mapping> remainderMappings = getMappingRetrievalService().getMemberMappingsOfPageByStartIndex(
                           columnAEDId, 0, offset);
                  List<InstanceAssetMapping> existingMappings = constructInstanceAssetMappings(remainderMappings);
                  pageMappings.addAll(existingMappings);
               } else if (pageNumber < suggestionsPageCount) {
                  // all are suggestions
                  InstanceMappingSuggestion instanceMappingSuggestion = getInstanceMappingSuggestionService()
                           .getInstanceMappingSuggestion(columnAEDId);
                  List<InstanceMappingSuggestionDetail> suggestions = getInstanceMappingSuggestionService()
                           .getInstanceMappingSuggestionDetailsByBatchAndSize(columnAEDId, new Long(pageNumber),
                                    new Long(PAGE_SIZE));
                  for (InstanceMappingSuggestionDetail suggestion : suggestions) {
                     InstanceAssetMapping instanceAssetMapping = prepareInstanceAssetMapping(instanceMappingSuggestion,
                              suggestion);
                     pageMappings.add(instanceAssetMapping);
                  }
               } else {
                  // all are existing mappings - but modify the start
                  // index
                  int dbPageNumber = pageNumber - suggestionsPageCount;
                  int offset = PAGE_SIZE - remainder;
                  int startIndex = ((dbPageNumber - 1) * PAGE_SIZE) + offset;
                  List<Mapping> mappings = getMappingRetrievalService().getMemberMappingsOfPageByStartIndex(
                           columnAEDId, startIndex, PAGE_SIZE);
                  pageMappings = constructInstanceAssetMappings(mappings);
               }
            }
         } else {
            if (MappingEntityType.SUGGESTED.equals(mappingFilter)) {
               // get only the suggested mappings
               int suggestionsCount = getInstanceMappingSuggestionService().getInstanceMappingSuggestionDetailsCount(
                        columnAEDId).intValue();
               pageCount = calculatePageCount(suggestionsCount, PAGE_SIZE);
               if (pageNumber > pageCount) {
                  // Error : show the first page
                  pageNumber = 1;
               }
               InstanceMappingSuggestion instanceMappingSuggestion = getInstanceMappingSuggestionService()
                        .getInstanceMappingSuggestion(columnAEDId);
               List<InstanceMappingSuggestionDetail> suggestions = getInstanceMappingSuggestionService()
                        .getInstanceMappingSuggestionDetailsByBatchAndSize(columnAEDId, new Long(pageNumber),
                                 new Long(PAGE_SIZE));
               for (InstanceMappingSuggestionDetail suggestion : suggestions) {
                  InstanceAssetMapping instanceAssetMapping = prepareInstanceAssetMapping(instanceMappingSuggestion,
                           suggestion);
                  pageMappings.add(instanceAssetMapping);
               }
            } else if (MappingEntityType.EXISTING.equals(mappingFilter)) {
               // get only the existing mappings
               int mappingsCount = getMappingRetrievalService().getMappedMemberCount(columnAEDId).intValue();
               pageCount = calculatePageCount(mappingsCount, PAGE_SIZE);
               if (pageNumber > pageCount) {
                  // Error : show the first page
                  pageNumber = 1;
               }
               List<Mapping> mappings = getMappingRetrievalService().getMemberMappingsByPage(columnAEDId, pageNumber,
                        PAGE_SIZE);
               pageMappings = constructInstanceAssetMappings(mappings);
            }
         }
         // add the pagination details
         MappingHeader header = new MappingHeader();
         header.setCurrentPage(pageNumber);
         header.setTotalPages(pageCount);
         header.setFilterType(mappingFilter);
         instanceMapping.setHeader(header);
         instanceMapping.setMappings(pageMappings);
      } catch (MappingException mappingException) {
         throw new ExeCueException(mappingException.getCode(), mappingException);
      }
      return instanceMapping;
   }

   private int calculatePageCount (int totalRows, int pageSize) {
      int pageCount = (totalRows / pageSize);
      if (totalRows % pageSize != 0) {
         pageCount++;
      }
      return pageCount;
   }

   public InstanceMapping validateInstanceMappings (Long conceptBEDId, List<SaveMapping> saveMappings)
            throws ExeCueException {
      InstanceMapping instanceMapping = new InstanceMapping();
      String imMsgType = "S";
      String message = "SUCCESS";
      String messageType = "S";
      AssetEntityDefinition membrAED = null;
      BusinessEntityDefinition instanceBED = null;
      List<InstanceAssetMapping> instanceAssetMappings = convertToInstanceAssetMappings(conceptBEDId, saveMappings);
      // first check within the page for duplicates
      for (InstanceAssetMapping outerIAM : instanceAssetMappings) {
         if ("S".equals(imMsgType)) {
            if (!outerIAM.isDeleteMapping()) {
               for (InstanceAssetMapping innerIAM : instanceAssetMappings) {
                  if ((outerIAM.getId() != innerIAM.getId()) && !innerIAM.isDeleteMapping()) {
                     if (outerIAM.getInstanceDispName().equalsIgnoreCase(innerIAM.getInstanceDispName())) {
                        message = "This instance is also being used to map another member '"
                                 + innerIAM.getMemDispName() + "'";
                        messageType = "E";
                        outerIAM.setMsg(message);
                        outerIAM.setMsgType(messageType);
                        imMsgType = "E";
                        break;
                     }
                  }
               }
            }
         } else {
            break;
         }
      }
      // proceed if there are no duplicates
      if ("S".equals(imMsgType)) {
         for (InstanceAssetMapping instanceAssetMapping : instanceAssetMappings) {
            // if the mapping already exists
            if (MappingEntityType.EXISTING.equals(instanceAssetMapping.getMappingType())) {
               membrAED = getSdxRetrievalService().getAssetEntityDefinitionById(instanceAssetMapping.getAedId());
               instanceBED = getKdxRetrievalService().getBusinessEntityDefinitionById(instanceAssetMapping.getBedId());
               message = getAlreadyMappedMemberLookupDescription(membrAED, instanceBED, instanceAssetMapping
                        .getInstanceDispName());
               if (!"SUCCESS".equals(message)) {
                  messageType = "E";
                  imMsgType = "E";
               }
            } else if (MappingEntityType.SUGGESTED.equals(instanceAssetMapping.getMappingType())) {
               // mapping is a suggestion
               if (MappingEntityType.SUGGESTED.equals(instanceAssetMapping.getInstanceType())) {
                  // the instance does not exist, its a suggestion
                  InstanceMappingSuggestionDetail instanceMappingSuggestionDetail = getInstanceMappingSuggestionService()
                           .getInstanceMappingSuggestionDetailByInstanceDisplayName(
                                    instanceAssetMapping.getInstanceDispName());
                  if (instanceMappingSuggestionDetail != null) {
                     // TODO: -JVK- use a running counter instead of hard
                     // coding
                     String updatedDisplayName = instanceAssetMapping.getInstanceDispName() + "_1";
                     instanceMappingSuggestionDetail.setInstanceDisplayName(updatedDisplayName);
                     getInstanceMappingSuggestionService().updateInstanceMappingSuggestionDetail(
                              instanceMappingSuggestionDetail);
                  }
               } else if (MappingEntityType.EXISTING.equals(instanceAssetMapping.getInstanceType())) {
                  membrAED = getSdxRetrievalService().getAssetEntityDefinitionById(instanceAssetMapping.getAedId());
                  instanceBED = getKdxRetrievalService().getBusinessEntityDefinitionById(
                           instanceAssetMapping.getBedId());
                  message = getAlreadyMappedMemberLookupDescription(membrAED, instanceBED, instanceAssetMapping
                           .getInstanceDispName());
                  if (!"SUCCESS".equals(message)) {
                     messageType = "E";
                     imMsgType = "E";
                  }
               }
            }
            instanceAssetMapping.setMsg(message);
            instanceAssetMapping.setMsgType(messageType);
         }
      }
      instanceMapping.setMappings(instanceAssetMappings);
      instanceMapping.setMsgType(imMsgType);
      return instanceMapping;
   }

   private String getAlreadyMappedMemberLookupDescription (AssetEntityDefinition memberAED,
            BusinessEntityDefinition instanceBED, String instanceDisplayName) throws MappingException {
      String message = "SUCCESS";
      List<Mapping> mappingsByInstanceDisplayName = getMappingRetrievalService().getMappingsByInstanceDisplayName(
               memberAED.getColum().getId(), memberAED.getMembr().getId(), instanceBED.getConcept().getId(),
               instanceDisplayName);
      if (ExecueCoreUtil.isCollectionNotEmpty(mappingsByInstanceDisplayName)) {
         Mapping mapping = mappingsByInstanceDisplayName.get(0);
         String mappedMemberDescription = mapping.getAssetEntityDefinition().getMembr().getLookupDescription();
         if (ExecueCoreUtil.isCollectionNotEmpty(mappingsByInstanceDisplayName)) {
            message = "The instance you are trying to map is already mapped to the '" + mappedMemberDescription
                     + "' member.";
         }
      }
      return message;
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

   public List<DefaultMetric> getAllPossibleDefaultMetrics (Long assetId, Long tableId) throws ExeCueException {
      return getMappingRetrievalService().getAllPossibleDefaultMetrics(assetId, tableId);
   }

   public List<DefaultMetric> getInValidExistingDefaultMetrics (Long tableId) throws ExeCueException {
      return getMappingRetrievalService().getInValidExistingDefaultMetrics(tableId);
   }

   public List<DefaultMetric> getValidExistingDefaultMetrics (Long tableId) throws ExeCueException {
      return getMappingRetrievalService().getValidExistingDefaultMetrics(tableId);
   }

   public IMappingDeletionService getMappingDeletionService () {
      return mappingDeletionService;
   }

   public void setMappingDeletionService (IMappingDeletionService mappingDeletionService) {
      this.mappingDeletionService = mappingDeletionService;
   }

   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }

   public ISDXRetrievalService getSdxRetrievalService () {
      return sdxRetrievalService;
   }

   public void setSdxRetrievalService (ISDXRetrievalService sdxRetrievalService) {
      this.sdxRetrievalService = sdxRetrievalService;
   }

   public IKDXManagementService getKdxManagementService () {
      return kdxManagementService;
   }

   public void setKdxManagementService (IKDXManagementService kdxManagementService) {
      this.kdxManagementService = kdxManagementService;
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

   public ISDX2KDXMappingService getSdx2kdxMappingService () {
      return sdx2kdxMappingService;
   }

   public void setSdx2kdxMappingService (ISDX2KDXMappingService sdx2kdxMappingService) {
      this.sdx2kdxMappingService = sdx2kdxMappingService;
   }

   /**
    * @return the coreConfigurationService
    */
   public ICoreConfigurationService getCoreConfigurationService () {
      return coreConfigurationService;
   }

   /**
    * @param coreConfigurationService the coreConfigurationService to set
    */
   public void setCoreConfigurationService (ICoreConfigurationService coreConfigurationService) {
      this.coreConfigurationService = coreConfigurationService;
   }

   public IInstanceAbsorptionJobService getInstanceAbsorptionJobService () {
      return instanceAbsorptionJobService;
   }

   public void setInstanceAbsorptionJobService (IInstanceAbsorptionJobService instanceAbsorptionJobService) {
      this.instanceAbsorptionJobService = instanceAbsorptionJobService;
   }

   public IBusinessEntityManagementWrapperService getBusinessEntityManagementWrapperService () {
      return businessEntityManagementWrapperService;
   }

   public void setBusinessEntityManagementWrapperService (
            IBusinessEntityManagementWrapperService businessEntityManagementWrapperService) {
      this.businessEntityManagementWrapperService = businessEntityManagementWrapperService;
   }

   /**
    * @return the swiConfigurationService
    */
   public ISWIConfigurationService getSwiConfigurationService () {
      return swiConfigurationService;
   }

   /**
    * @param swiConfigurationService the swiConfigurationService to set
    */
   public void setSwiConfigurationService (ISWIConfigurationService swiConfigurationService) {
      this.swiConfigurationService = swiConfigurationService;
   }

   @Override
   public int getMappingPageSize () {
      return getSwiConfigurationService().getMappingPageSize();
   }
}