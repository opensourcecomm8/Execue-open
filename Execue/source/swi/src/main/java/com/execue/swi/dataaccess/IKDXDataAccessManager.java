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


package com.execue.swi.dataaccess;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.execue.core.common.bean.ApplicationInfo;
import com.execue.core.common.bean.ContentCleanupPattern;
import com.execue.core.common.bean.Page;
import com.execue.core.common.bean.entity.Application;
import com.execue.core.common.bean.entity.ApplicationDetail;
import com.execue.core.common.bean.entity.ApplicationExample;
import com.execue.core.common.bean.entity.ApplicationOperation;
import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.BusinessEntityMaintenanceInfo;
import com.execue.core.common.bean.entity.BusinessEntityVariation;
import com.execue.core.common.bean.entity.Cloud;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.DefaultDynamicValue;
import com.execue.core.common.bean.entity.EntityBehavior;
import com.execue.core.common.bean.entity.EntityDetailType;
import com.execue.core.common.bean.entity.EntityTripleDefinition;
import com.execue.core.common.bean.entity.Hierarchy;
import com.execue.core.common.bean.entity.Instance;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.common.bean.entity.ModelGroup;
import com.execue.core.common.bean.entity.ModelGroupMapping;
import com.execue.core.common.bean.entity.Path;
import com.execue.core.common.bean.entity.PathDefinition;
import com.execue.core.common.bean.entity.PossibleAttribute;
import com.execue.core.common.bean.entity.PossibleDetailType;
import com.execue.core.common.bean.entity.RIOntoTerm;
import com.execue.core.common.bean.entity.RIParallelWord;
import com.execue.core.common.bean.entity.RISharedUserModelMapping;
import com.execue.core.common.bean.entity.Relation;
import com.execue.core.common.bean.entity.SFLTerm;
import com.execue.core.common.bean.entity.SFLTermToken;
import com.execue.core.common.bean.entity.SecondaryWord;
import com.execue.core.common.bean.entity.Stat;
import com.execue.core.common.bean.entity.Type;
import com.execue.core.common.bean.entity.UnstructuredApplicationDetail;
import com.execue.core.common.bean.entity.Vertical;
import com.execue.core.common.bean.entity.VerticalAppExample;
import com.execue.core.common.bean.entity.VerticalAppWeight;
import com.execue.core.common.bean.entity.VerticalEntityBasedRedirection;
import com.execue.core.common.bean.entity.wrapper.AppDashBoardInfo;
import com.execue.core.common.bean.governor.BusinessEntityInfo;
import com.execue.core.common.bean.governor.BusinessEntityTerm;
import com.execue.core.common.bean.nlp.SFLInfo;
import com.execue.core.common.bean.swi.PopularityBusinessEntityDefinitionInfo;
import com.execue.core.common.type.AppOperationType;
import com.execue.core.common.type.AppSourceType;
import com.execue.core.common.type.BehaviorType;
import com.execue.core.common.type.BusinessEntityType;
import com.execue.core.common.type.EntityTripleDefinitionType;
import com.execue.core.common.type.EntityType;
import com.execue.core.common.type.JobStatus;
import com.execue.core.common.type.OperationType;
import com.execue.core.common.type.PublishAssetMode;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.swi.exception.KDXException;

public interface IKDXDataAccessManager extends ISWIDataAccessManager {

   public List<Concept> getConcepts (Long modelId) throws KDXException;

   public List<Instance> getInstances (Long modelId, Long conceptId) throws KDXException;

   public List<Instance> getTypeInstances (Long modelId, Long typeId) throws KDXException;

   public List<Instance> getInstancesByPage (Long modelId, Long conceptId, Page page) throws KDXException;

   public long getInstancesCount (Long modelId, Long conceptId) throws KDXException;

   public List<Stat> getAllStats () throws KDXException;

   public void createModel (Model model) throws KDXException;

   public void createHierarchy (Hierarchy hierarchy) throws KDXException;

   public void updateHierarchy (Hierarchy hierarchy) throws KDXException;

   public void createModelGroupMapping (ModelGroupMapping modelGroupMapping) throws KDXException;

   public void createModelGroup (ModelGroup modelGroup) throws KDXException;

   public BusinessEntityDefinition createConcept (Long modelId, Type type, Concept concept, Long knowledgeId)
            throws KDXException;

   public BusinessEntityDefinition createInstance (Long modelId, BusinessEntityDefinition conceptBED,
            Instance instance, Long knowledgeId) throws KDXException;

   public BusinessEntityDefinition createTypeInstance (Long modelId, BusinessEntityDefinition typeBED,
            Instance instance, Long knowledgeId) throws KDXException;

   public BusinessEntityDefinition updateConcept (Long modelId, Cloud cloud, Type type, Concept concept,
            BusinessEntityDefinition conceptBED) throws KDXException;

   public BusinessEntityDefinition updateInstance (Long modelId, Long conceptId, Instance instance) throws KDXException;

   public BusinessEntityDefinition getBEDByBehaviorName (String behaviorName) throws KDXException;

   public void updateRelation (Long modelId, Relation relation) throws KDXException;

   public Stat getStatByName (String name) throws KDXException;

   public Model getModelByUserModelGroupId (Long userModelGroupId) throws KDXException;

   public BusinessEntityDefinition getBusinessEntityDefinitionByIds (Long modelId, Long conceptId, Long instanceId)
            throws KDXException;

   public BusinessEntityDefinition getBusinessEntityDefinitionByTypeIds (Long modelId, Long typeId, Long instanceId)
            throws KDXException;

   public BusinessEntityDefinition getBusinessEntityDefinitionByNames (String businessName, String conceptName,
            String instanceName) throws KDXException;

   public List<Concept> getConceptsBySearchString (Long modelId, String searchString) throws KDXException;

   public List<Instance> getInstancesOfConceptBySearchString (Long modelId, Long conceptId, String searchString)
            throws KDXException;

   public List<Instance> getInstancesForAssetMetaInfo (Long modelId, Long conceptId) throws KDXException;

   public List<Concept> getConceptsForAsset (Long assetId) throws KDXException;

   public List<Concept> getConceptsForAssetMetaInfo (Long assetId) throws KDXException;

   public List<Concept> getMeasureConceptsForAsset (Long assetId) throws KDXException;

   public Integer getMeasureConceptsCountForAsset (Long assetId) throws KDXException;

   public BusinessEntityDefinition getBEDByInstanceDisplayName (String modelName, String conceptName,
            String instanceDisplayName) throws KDXException;

   public BusinessEntityDefinition getBEDByMappingConceptName (Long modelId, Long mappingId, String conceptDisplayName)
            throws KDXException;

   public BusinessEntityDefinition getRelationBEDByName (Long modelId, String relationName) throws KDXException;

   public BusinessEntityDefinition getConceptBEDByName (Long modelId, String conceptName) throws KDXException;

   public BusinessEntityDefinition getConceptBEDByDisplayName (Long modelId, String conceptDisplayName)
            throws KDXException;

   public BusinessEntityDefinition getInstanceBEDByName (Long modelId, String conceptName, String instanceName)
            throws KDXException;

   public BusinessEntityDefinition getInstanceBEDByDisplayName (Long modelId, String conceptName,
            String instanceDisplayName) throws KDXException;

   public List<RIOntoTerm> getTermsByLookupWord (String word) throws KDXException;

   public List<RIOntoTerm> getTermsByLookupWords (List<String> words, boolean skipLocationTypeRecognition,
            List<Long> locationChildTypeBedIds) throws KDXException;

   public List<RIOntoTerm> getTermsByLookupWordAndEntityType (String word, BusinessEntityType entityType)
            throws KDXException;

   public List<RIOntoTerm> getTermsByLookupWordAndEntityTypeInGroup (String word, BusinessEntityType entityType,
            Long modelGroupId) throws KDXException;

   public List<RIOntoTerm> getTermsByLookupWordsAndEntityType (List<String> words, BusinessEntityType entityType)
            throws KDXException;

   public List<SecondaryWord> getAllSecondaryWords () throws KDXException;

   public List<RIParallelWord> getParallelWordsByLookupWord (String word) throws KDXException;

   /**
    * Get the list Of RiParallelWords with the given words and where model Group id is in the list passed .
    * 
    * @param words
    * @param modelGroupIds
    * @return
    * @throws KDXException
    */
   public List<RIParallelWord> getParallelWordsByLookupWords (List<String> words, Set<Long> modelGroupIds)
            throws KDXException;

   public List<BusinessEntityDefinition> getRegularExpressionBasedInstances () throws KDXException;

   public List<SFLTermToken> getSFLTermTokensByLookupWord (String word) throws KDXException;

   public List<SFLTermToken> getSFLTermTokensByLookupWords (List<String> words) throws KDXException;

   public List<SFLInfo> getSFLInfoForWords (List<String> words, Set<Long> modelGroupIds, boolean groupConcatDBSupported)
            throws KDXException;

   public Stat getStatByBusinessEntityId (Long businessEntityDefinitionId) throws KDXException;

   public BusinessEntityType getBusinessEntityType (Long businessEntityDefinitionId) throws KDXException;

   public List<BusinessEntityDefinition> getAllAbstractConcepts () throws KDXException;

   public List<BusinessEntityDefinition> getAllAtributeConcepts () throws KDXException;

   public List<BusinessEntityDefinition> getAllEnumerationConcepts () throws KDXException;

   public List<BusinessEntityDefinition> getAllQuantitativeConcepts () throws KDXException;

   public List<BusinessEntityDefinition> getAllComparativeConcepts () throws KDXException;

   public List<BusinessEntityDefinition> getAllDistributionConcepts () throws KDXException;

   public boolean isGrainConcept (Long bedID) throws KDXException;

   public boolean isAbstractConcept (Long bedID) throws KDXException;

   public boolean isAttributeConcept (Long bedID) throws KDXException;

   public boolean isComparativeConcept (Long bedID) throws KDXException;

   public boolean isEnumerationConcept (Long bedID) throws KDXException;

   public boolean isQuantitativeConcept (Long bedID) throws KDXException;

   public boolean isDistributionConcept (Long bedID) throws KDXException;

   public List<PathDefinition> getConceptToAttributePathDefinitionDetails (Long sourceBEDId, Long destBEDId)
            throws KDXException;

   public List<PathDefinition> getPathDefinitionDetailsLessThanLength (Long sourceBEDId, Long destBEDId, int length)
            throws KDXException;

   public List<PathDefinition> getPathDefinitions (Long sourceBEDId, Long destBEDId) throws KDXException;

   public List<PathDefinition> getParentPathDefinitions (Long sourceBEDId, Long destBEDId) throws KDXException;

   public List<PathDefinition> getChildPathDefinitionsForSource (Long sourceBEDId) throws KDXException;

   public List<PathDefinition> getChildPathDefinitionsForSourceForModel (Long modelId, Long sourceBEDId)
            throws KDXException;

   public List<PathDefinition> getAllParentPathDefinitionsForDestination (Long destinationBEDId) throws KDXException;

   public List<PathDefinition> getImmediateParentPathDefinitionsForDestination (Long destinationBEDId)
            throws KDXException;

   public List<PathDefinition> getConceptToConceptPathDefinitionDetails (Long sourceBEDId, Long destBEDId)
            throws KDXException;

   public List<PathDefinition> getRelationToRelationPathDefinitionDetails (Long sourceBEDId, Long destBEDId)
            throws KDXException;

   public EntityTripleDefinition getEntityTriple (Long sourceBEDId, Long relationBEDId, Long destBEDId)
            throws KDXException;

   public List<EntityTripleDefinition> getEntityTriplesForSourceAndRelation (Long sourceBEDId, Long relationBEDId)
            throws KDXException;

   public List<EntityTripleDefinition> getEntityTriplesForDestinationAndRelation (Long relationBEDId, Long destBEDId)
            throws KDXException;

   public List<EntityTripleDefinition> getEntityTriplesForSource (Long sourceBEDId) throws KDXException;

   public List<EntityTripleDefinition> getEntityTriplesForDestination (Long destBEDId) throws KDXException;

   public List<EntityTripleDefinition> getEntityTriplesForRelation (Long relBEDId) throws KDXException;

   public Set<Long> getRelatedBEDIdsByInstanceConceptBEDIds (List<Long> instanceBEDIds, List<Long> conceptBEDIds)
            throws KDXException;

   public Set<Long> getRelatedBEDIdsByConceptBEDIds (List<Long> conceptBEDIds) throws DataAccessException;

   public Integer updateBusinessEntitiesPopularity (List<BusinessEntityTerm> businessEntityTerms) throws KDXException;

   public void updateBusinessEntityDefinitions (List<BusinessEntityDefinition> businessEntityDefinitions)
            throws KDXException;

   public List<PathDefinition> getPathsBetweenMultipleSourcesToSingleDestination (List<Long> sourceBEDIDs,
            Long destinationBEDId) throws KDXException;

   public BusinessEntityDefinition createRelation (Long modelId, Relation relation, Type type, Long knowledgeId)
            throws KDXException;

   public List<Instance> getInstancesByDisplayNameAcrossAllConcepts (Long modelId, String instanceDisplayName)
            throws KDXException;

   public Path getPathByETD (EntityTripleDefinition etd) throws KDXException;

   public void createRIOntoTerm (RIOntoTerm riOnTOTerm) throws KDXException;

   public Instance getLatestInstanceInserted (Long modelId, Long conceptId) throws KDXException;

   public void insertOrderForSFLTermTokens () throws KDXException;

   public void updateSFLTermToken (SFLTermToken termToken) throws KDXException;

   public List<SFLTerm> getAllSFLTerms () throws KDXException;

   public void updateSFLTermTokens (List<SFLTermToken> termTokens) throws KDXException;

   public List<SFLTermToken> getSFLTermTokensByTermId (Long sflTermId) throws KDXException;

   public SFLTerm getSFLTermByWord (String word) throws KDXException;

   public RIParallelWord getParallelWordById (Long id) throws KDXException;

   public void updateRIParallelWord (RIParallelWord parallelWord) throws KDXException;

   public List<PathDefinition> getParentPathsForDestList (Long sourceBED, List<Long> destinationBEDs)
            throws KDXException;

   public List<BusinessEntityInfo> getAllConceptsBusinessEntities (Long modelId) throws KDXException;

   public List<BusinessEntityInfo> getAllInstanceBusinessEntities (Long modelId) throws KDXException;

   public List<BusinessEntityInfo> getAllConceptProfileBusinessEntities (Long modelId) throws KDXException;

   public List<BusinessEntityInfo> getAllInstanceProfileBusinessEntities (Long modelId) throws KDXException;

   public List<BusinessEntityInfo> getAllRelationBusinessEntities (Long modelId) throws KDXException;

   public void createSFLTerm (SFLTerm sflTerm) throws KDXException;

   public void deleteSFLTerm (SFLTerm sflTerm) throws KDXException;

   public List<SFLTerm> getSFLTerms (String sflTerm) throws KDXException;

   public List<ApplicationInfo> getAllApplications () throws KDXException;

   public Application createApplication (Application application) throws KDXException;

   public ApplicationDetail createApplicationImage (ApplicationDetail applicationImage) throws KDXException;

   public void updateApplicationImage (ApplicationDetail applicationImage) throws KDXException;

   public void deleteApplicationImage (ApplicationDetail applicationDetail) throws KDXException;

   public ApplicationDetail getImageByApplicationId (Long applicationId) throws KDXException;

   public ApplicationDetail getImageByApplicationImageId (Long applicationId, Long imageId) throws KDXException;

   public void updateApplication (Application application) throws KDXException;

   public void updateApplicationOperationDetails (Long applicationId, AppOperationType operationType,
            Long jobRequestId, JobStatus operationStatus) throws KDXException;

   public List<Application> getAllExistingApplications () throws KDXException;

   public ModelGroup getPrimaryGroup (Long modelId) throws KDXException;

   public Application getApplicationById (Long applicationId) throws KDXException;

   public List<Model> getModelsByApplicationId (Long appliationId) throws KDXException;

   public List<SFLTerm> getSFLTermsForKeyWord (String keyWord) throws KDXException;

   public Concept getConceptByName (Long modelId, String conceptName) throws KDXException;

   public BusinessEntityDefinition getBEDByRealizationName (Long modelId, String realizationName) throws KDXException;

   public Concept getConceptByDisplayName (Long modelId, String conceptDisplayName) throws KDXException;

   public List<Concept> getConcepts (List<ModelGroup> userModelGroups) throws KDXException;

   public Relation getRelationByName (Long modelId, String relationName, boolean includeBaseModelGroup)
            throws KDXException;

   public boolean isApplicationExist (String applicationName) throws KDXException;

   public boolean isModelExist (String modelName) throws KDXException;

   public boolean isCloudExist (String cloudName) throws KDXException;

   public List<RIOntoTerm> getTermsByLookupWordsAndEntityTypeInGroup (List<String> words,
            BusinessEntityType entityType, Long modelGroupId) throws KDXException;

   public List<Application> getApplications (Long userId) throws KDXException;

   /**
    * Method to get the central Concept paths for a destination.
    * 
    * @param destinationBEDId
    * @return
    * @throws KDXException
    */
   public List<PathDefinition> getCentralConceptPathsForDestination (Long destinationBEDId) throws KDXException;

   public List<Model> getModelByGroupIds (Set<Long> groupIds) throws KDXException;

   public List<ModelGroup> getUserModelGroupsByModelId (Long modelId) throws KDXException;

   // public void deleteConceptAndRespectiveInstancesForModel (Long modelId)
   // throws KDXException;

   public List<PathDefinition> getAllNonParentPathsForSourceAndDestination (List<Long> tokenBEIDs, List<Long> destBeIds)
            throws KDXException;

   public List<Relation> getRelations (List<ModelGroup> userModelGroups) throws KDXException;

   public List<Relation> getRelationsByModel (Long modelId) throws KDXException;

   public List<Relation> suggestRelationsByName (Long modelId, String searchString, int maxResults) throws KDXException;

   public void deleteConcepts (List<Concept> concepts) throws KDXException;

   public void deleteRelations (List<Relation> relations) throws KDXException;

   public void deleteSecondaryWords (List<SecondaryWord> secondaryWords) throws KDXException;

   // Methods for deletion of application hierarchy
   public List<ModelGroupMapping> getPopulatedModelGroupMapping (Long modelId) throws KDXException;

   public void cleanModelGroupMappings (List<ModelGroupMapping> modelGroupMappings) throws KDXException;

   public void cleanModelGroups (List<ModelGroup> userModelGroups) throws KDXException;

   public void cleanApplication (Application application) throws KDXException;

   public void cleanModels (List<Model> models) throws KDXException;

   public void cleanRIOntoTerms (List<ModelGroup> userModelGroups) throws KDXException;

   public void cleanDefaultDynamicValues (List<ModelGroup> userModelGroups) throws KDXException;

   public void cleanSFLTerms (List<Long> sflTermIds) throws KDXException;

   public List<BusinessEntityDefinition> getInstanceBEDsByConceptId (long modelId, long conceptId) throws KDXException;

   public List<Long> getInstanceBEDIdsByConceptId (Long modelId, Long conceptId) throws KDXException;

   public void updateBusinessEntityDefinition (BusinessEntityDefinition businessEntityDefinition) throws KDXException;

   public Application getApplicationByName (String applicationName) throws KDXException;

   public Model getModelByName (String modelName) throws KDXException;

   public void updateModel (Model model) throws KDXException;

   public List<PopularityBusinessEntityDefinitionInfo> getPopularityInfoForModelGroup (Long modelGroupId)
            throws KDXException;

   public List<Long> getSFLTermIdsTobeDeleted () throws KDXException;

   public List<Long> getAllSFLTermIds () throws KDXException;

   public List<Long> getSFLTermIdsForNonZeroHits () throws KDXException;

   public List<SFLTerm> getPopulatedSFLTerms (List<Long> sflTermIds) throws KDXException;

   public List<BusinessEntityDefinition> getConceptBEDsOfModel (Long modelId) throws KDXException;

   public List<SFLTerm> getOrphanSFLTerms (Long modelGroupId) throws KDXException;

   public List<RIOntoTerm> getRIOntoTermsByBEDId (Long bedId, BusinessEntityType type) throws KDXException;

   public void deleteRIOntoTerm (RIOntoTerm riOntoTerm) throws KDXException;

   public List<RIOntoTerm> getTermsByLookupWordAndEntityTypeInGroupForInstance (String word,
            BusinessEntityType entityType, Long modelGroupId, Long conceptBEDID) throws KDXException;

   public List<Application> getApplicationsByModelId (Long modelId) throws KDXException;

   public List<Application> getApplicationsByPage (Page pageDetail) throws KDXException;

   public List<AppDashBoardInfo> getAppDashBoardInfosByPage (Page pageDetail) throws KDXException;

   public List<AppDashBoardInfo> getAppDashBoardInfosByPage (Page pageDetail, boolean advancedMenu) throws KDXException;

   public List<Application> getApplicationsByImageId (Long imageId) throws KDXException;

   public Long getAllExistingApplicationsCount () throws KDXException;

   public Long getInstanceBEDsCountByConceptId (Long conceptId) throws KDXException;

   public List<BusinessEntityDefinition> getInstanceBEDsByPage (Long conceptId, Long pageNumber, Long pageSize)
            throws KDXException;

   public ApplicationExample createApplicationExample (ApplicationExample applicationExample) throws KDXException;

   public void updateApplicationExample (ApplicationExample applicationExample) throws KDXException;

   public void deleteApplicationExample (ApplicationExample applicationExample) throws KDXException;

   public void deleteApplicationExamples (List<ApplicationExample> applicationExamples) throws KDXException;

   public void deleteApplicationOperations (List<ApplicationOperation> applicationOperations) throws KDXException;

   public List<ApplicationExample> getAllAppExampleForApplication (Long appId) throws KDXException;

   /**
    * Method to check the behavior for the give typeBedId
    * 
    * @param resourceID
    * @param behaviorType
    * @return the boolean true if the type bed id has the behavior, else false
    * @throws KDXException
    */
   public boolean hasBehavior (Long resourceID, BehaviorType behaviorType) throws KDXException;

   /**
    * Method to get the OntoTerms for a given Type with matching word.
    * 
    * @param word
    *           word of the ontoTerm
    * @param typeBedId
    *           typeBeId of the Type the term should belong
    * @return List of Onto Terms
    * @throws DataAccessException
    */
   public List<RIOntoTerm> getTermsByLookupWordAndTypeBedId (String word, Long typeBedId) throws KDXException;

   /**
    * Method to get all the possible behavior for the given type bed id
    * 
    * @param typeBeId
    * @return list of BehaviorTypes
    * @throws KDXException
    */
   public List<BehaviorType> getAllPossibleBehavior (Long typeBeId) throws KDXException;

   public List<PossibleAttribute> getAllPossibleAttributes (Long typeBedId) throws KDXException;

   /**
    * Method to get the list of behavior for the given entity bed id
    * 
    * @param entityBedId
    * @return list of BehaviorTypes
    * @throws KDXException
    */
   public List<BehaviorType> getAllBehaviorForEntity (Long entityBedId) throws KDXException;

   /**
    * Method to check the behavior for the given entityBedId
    * 
    * @param entityBedId
    * @param behaviorType
    * @return the boolean true if the entity bed id has the behavior, else false
    * @throws KDXException
    */
   public boolean checkEntityHasBehavior (Long entityBedId, BehaviorType behaviorType) throws KDXException;

   public List<RIOntoTerm> getTermsByLookupWordAndConceptBedId (String word, Long conceptBedId) throws KDXException;

   /**
    * @param entityBehaviors
    * @throws KDXException
    */
   public void createEntityBehaviors (List<EntityBehavior> entityBehaviors) throws KDXException;

   /**
    * @param typeBedId
    * @param modelId
    * @return
    * @throws KDXException
    */
   public List<BusinessEntityDefinition> getRealizationsForTypeInModelIncludingBaseGroup (Long typeBedId, Long modelId)
            throws KDXException;

   /**
    * @param possibleAttributeIds
    * @return
    * @throws KDXException
    */
   public List<PossibleAttribute> getPossibleAttributesByIds (List<Long> possibleAttributeIds) throws KDXException;

   public Type getTypeByName (String typeName) throws KDXException;

   public BusinessEntityDefinition createType (Long modelId, Type type, boolean isRealizedType, Long knowledgeId)
            throws KDXException;

   public BusinessEntityDefinition getTypeBusinessEntityDefinition (Long typeId) throws KDXException;

   public BusinessEntityDefinition getTypeBedByName (String typeName, boolean isRealizedType) throws KDXException;

   public BusinessEntityDefinition getTypeBedByTypeID (Long typeID, boolean isRealizedType) throws KDXException;

   public List<Type> getAllTypes () throws KDXException;

   public List<RIOntoTerm> getRegularExpressionBasedOntoTerms () throws KDXException;

   public void deleteEntityBehavior (Long conceptBedId) throws KDXException;

   public void deleteEntityBehaviors (Long conceptBedId, List<BehaviorType> behaviorTypes) throws KDXException;

   public PossibleAttribute getPossibleAttribute (Long typeBedId, Long componentTypeBedId, Long relationBedId)
            throws KDXException;

   public List<SFLTerm> getSFLTermsForInstancesOfConceptByBatchNumber (Long conceptBedId, Long batchNumber,
            Long batchSize) throws KDXException;

   /**
    * Retrieves the BEDs of the types which are marked as Attributes in the base model.
    */
   public List<BusinessEntityDefinition> getAllAttributeTypes () throws KDXException;

   /**
    * Retrieves the BEDs of the entities which are marked as Attributes in a specific model
    */
   public List<BusinessEntityDefinition> getAllAttributeEntities (Long modelId) throws KDXException;

   public List<SecondaryWord> getAllSecondaryWordsForModel (Long modelId) throws KDXException;

   public void createSecondaryWords (List<SecondaryWord> secondaryWords) throws KDXException;

   public List<String> getEligibleSecondaryWordsForModel (Long modelId, Long threshold) throws KDXException;

   public void createBusinessEntityMaintenanceInfo (BusinessEntityMaintenanceInfo businessEntityMaintenanceInfo)
            throws KDXException;

   public void deleteBusinessEntityMaintenanceDetails (Long modelId) throws KDXException;

   public void deleteBusinessEntityMaintenanceDetails (Long modelId, EntityType entityType) throws KDXException;

   public List<Long> getBusinessEntityMaintenanceDetails (Long modelId, OperationType operationType,
            EntityType entityType) throws KDXException;

   public List<Long> getBusinessEntityMaintenanceParentDetails (Long modelId, EntityType entityType)
            throws KDXException;

   public List<Long> getDistinctUpdatedEntityMaintenanceDetails (Long modelId, EntityType entityType)
            throws KDXException;

   public List<Long> getBusinessEntityMaintenanceDetailsByParentId (Long modelId, OperationType operationType,
            EntityType entityType, Long parentId) throws KDXException;

   public Map<Long, Map<Long, String>> getAllParentToChildTriplesForModel (Long modelId) throws KDXException;

   public Map<Long, Map<Long, String>> getAllChildToParentTriplesForModel (Long modelId) throws KDXException;

   public List<EntityTripleDefinition> getEntityTriplesForSourceAndTripleType (Long sourceBEDId,
            EntityTripleDefinitionType tripleType) throws KDXException;

   public List<EntityTripleDefinition> getEntityTriplesForDestinationAndTripleType (Long destBEDId,
            EntityTripleDefinitionType tripleType) throws KDXException;

   public List<Application> getApplicationsForVertical (String verticalName) throws KDXException;

   public List<String> getApplicationNamesForVertical (String verticalName) throws KDXException;

   public List<BusinessEntityDefinition> getDistinctUpdatedMisMatchedEnumerationConceptBEDsFromBEMDetails (Long modelId)
            throws KDXException;

   public void createDefaultDynamicValue (DefaultDynamicValue defaultDynamicValue) throws KDXException;

   public void deleteDefaultDynamicValue (DefaultDynamicValue defaultDynamicValue) throws KDXException;

   public List<DefaultDynamicValue> getDefaultDynamicValues (Long assetId) throws KDXException;

   public PossibleAttribute getPossibleAttribute (Long typeBedId, Long componentTypeBedId) throws KDXException;

   public List<RIOntoTerm> getConceptTermsByTypeBedIdAndModelGroupId (Long typeBedId, Long modelGroupId)
            throws KDXException;

   public boolean isMatchedBusinessEntityType (Long bedId, String typeName) throws KDXException;

   public boolean isConceptMatchedBusinessEntityType (Long conceptId, Long modelId, String typeName)
            throws KDXException;

   public void createDefaultDynamicValues (List<DefaultDynamicValue> defaultDynamicValues) throws KDXException;

   public List<BusinessEntityDefinition> getConceptsOfParticularType (Long modelId, String typeName)
            throws KDXException;

   public List<Long> getSFLTermIdsByContextId (Long contextId) throws KDXException;

   public List<Application> getVerticalApplicationsByRank (Long varticalId, Long limit) throws KDXException;

   public void createVerticalAppWeight (VerticalAppWeight verticalAppWeight) throws KDXException;

   public Vertical getVerticalByName (String name) throws KDXException;

   public VerticalAppWeight getVerticalAppWeightByApplicationId (Long id) throws KDXException;

   public void deleteVerticalAppWeight (VerticalAppWeight verticalAppWeight) throws KDXException;

   public Map<String, List<VerticalAppExample>> getVerticalAppExamples () throws KDXException;

   public Map<String, List<VerticalAppExample>> getVerticalAppExamplesByDay (int day) throws KDXException;

   public List<Application> getApplicationsByRank (Long limit) throws KDXException;

   public BusinessEntityDefinition getRelationBEDByNameIncludingBaseGroup (Long modelId, String relationName)
            throws KDXException;

   public List<Instance> getInstancesByPopularity (Long modelId, Long conceptId, Long limit) throws KDXException;

   public List<Application> getApplicationsByVerticalId (Long verticalId) throws KDXException;

   public List<Concept> getConceptsByPopularity (Long modelId, Long limt) throws KDXException;

   public List<VerticalEntityBasedRedirection> getVerticalRedirectionEntitiesByApplicationId (Long id)
            throws KDXException;

   public List<VerticalAppExample> getVerticalAppExamplesByApplicationId (Long applicationId) throws KDXException;

   public List<ApplicationOperation> getApplicationOperationsByApplicationId (Long applicationId) throws KDXException;

   public void deleteVerticalRedirectionEntities (
            List<VerticalEntityBasedRedirection> verticalEntityBasedRedirectionList) throws KDXException;

   public void deleteVerticalAppExamples (List<VerticalAppExample> verticalAppExamples) throws KDXException;

   public void deleteBusinessEntityMaintenanceDetails (Long modelId, List<Long> entityBedId, EntityType entityType)
            throws KDXException;

   public void deleteInstances (List<Instance> instances) throws KDXException;

   public Instance getInstanceByInstanceName (Long conceptBedId, String instanceName) throws KDXException;

   public List<RIOntoTerm> getConceptOntoTermsByConceptBedIds (Set<Long> conceptBedIds) throws KDXException;

   public List<Vertical> getAppVerticals (Long applicationId) throws KDXException;

   public void cleanApplicationOperationData () throws KDXException;

   public List<Model> getAllModels () throws KDXException;

   public List<PossibleDetailType> getPossibleDetailTypes (Long typeBedId) throws KDXException;

   public EntityDetailType getDetailTypeForConcept (Long conceptBedId) throws KDXException;

   public void createEntityDetailType (EntityDetailType entityDetailType) throws KDXException;

   public void deleteEntityDetailTypeByConcept (Long conceptBedId) throws KDXException;

   public BusinessEntityDefinition getDefaultDetailType (Long typeBedId) throws KDXException;

   public List<BusinessEntityDefinition> getPossibleDetailTypesForType (Long typeBedId) throws KDXException;

   public List<Long> getModelGroupIdsByApplicationId (Long applicationId) throws KDXException;

   public List<DefaultDynamicValue> getDefaultDynamicValues (Long assetId, List<Long> bedIds) throws KDXException;

   public List<DefaultDynamicValue> getDefaultDynamicValues (Long assetId, Long bedId) throws KDXException;

   public List<Concept> getMappedConceptsForParticularType (Long modelId, Long assetId, String typeName)
            throws KDXException;

   public List<Concept> getMappedConceptsForParticularBehaviour (Long modelId, Long assetId, BehaviorType behaviourType)
            throws KDXException;

   public List<Application> getAllApplicationsOrderedByName () throws KDXException;

   public List<Type> getAllVisibleTypes () throws KDXException;

   public List<BusinessEntityDefinition> getAllNonAttributeEntities (Long modelId) throws KDXException;

   public Map<String, String> getConceptInstanceDisplayNamesByAbbrevatedNames (Long modelId, String conceptName,
            List<String> abbrevatedNames) throws KDXException;

   public Instance getTopPopularityInstance (Long modelId, String originAirportConceptName) throws KDXException;

   public List<Application> getAllActiveApplications () throws KDXException;

   public List<Application> getAllActiveStructuredApplications () throws KDXException;

   public Map<Long, RIOntoTerm> getOntoTermsByEntityBeId (Set<Long> entityBedIds) throws KDXException;

   public List<String> getBusinessEntityVariationNames (Long entityBedId) throws KDXException;

   public List<BusinessEntityVariation> getBusinessEntityVariations (Long entityBedId) throws KDXException;

   public void createBusinessEntityVariation (BusinessEntityVariation businessEntityVariation) throws KDXException;

   public void deleteBusinessEntityVariation (BusinessEntityVariation businessEntityVariation) throws KDXException;

   public void updateBusinessEntityVariation (BusinessEntityVariation businessEntityVariation) throws KDXException;

   public List<RISharedUserModelMapping> getRISharedUserModelMappings (List<Long> baseInstanceBedIds,
            Set<Long> modelGroupIds) throws KDXException;

   public void createRISharedUserModelMapping (RISharedUserModelMapping riSharedUserModelMapping) throws KDXException;

   public void deleteRISharedUserModelMappings (List<Long> userModelGroupIds) throws KDXException;

   public void deleteBusinessEntityVariations (List<Long> modelGroupIds) throws KDXException;

   public List<Long> getConceptBedIdsHavingInstances (Long modelId) throws KDXException;

   public List<Long> getConceptBedsHavingBehaviorType (List<Long> conceptBedIds, BehaviorType behaviorType)
            throws KDXException;

   public void deleteBusinessEntityDefinitionById (Long bedId) throws KDXException;

   public void deleteIndexFormsByBedId (Long instanceProfileBedId) throws KDXException;

   public List<Long> getNonSharedBusinessEntityMaintenanceDetailsByParentId (Long modelId, OperationType operationType,
            EntityType entityType, Long parentId) throws KDXException;

   public List<Instance> getInstancesByBEDIds (List<Long> instanceBEDIds) throws KDXException;

   public List<BusinessEntityInfo> getInstanceBusinessEntitiesByPageForModel (Long modelId, Page page)
            throws KDXException;

   public List<BusinessEntityInfo> getConceptBusinessEntitiesByPageForModel (Long modelId, Page page)
            throws KDXException;

   public List<BusinessEntityInfo> getConceptProfileBusinessEntitiesByPageForModel (Long modelId, Page page)
            throws KDXException;

   public List<BusinessEntityInfo> getInstanceProfileBusinessEntitiesByPageForModel (Long modelId, Page page)
            throws KDXException;

   public List<BusinessEntityInfo> getRelationBusinessEntitiesByPageForModel (Long modelId, Page page)
            throws KDXException;

   public void deleteRelation (Relation relation) throws KDXException;

   public void deleteConcept (Concept concept) throws KDXException;

   public void deleteInstance (Instance instance) throws KDXException;

   public List<Concept> getEligibleConceptsOfAssetForCubeByPage (Long assetId, Page page) throws KDXException;

   public void createBusinessEntityVariations (List<BusinessEntityVariation> businessEntityVariations)
            throws KDXException;

   public void deleteBusinessEntityVariationsByBedId (Long businessEntityId) throws KDXException;

   public List<RISharedUserModelMapping> getRISharedUserModelMappings (List<Long> baseInstanceBedIds)
            throws KDXException;

   public List<RIParallelWord> getParallelWordsByLookupWords (List<String> words) throws KDXException;

   public ModelGroup getModelGroupByTypeBedId (Long typeBedId) throws KDXException;

   public List<BusinessEntityDefinition> getBedByModelGroupsAndConceptId (List<Long> modelGroupIds, Long conceptId)
            throws KDXException;

   public void deleteVerticalEntityRedirectionEntitiesByEntityBedId (Long entityBedId, BusinessEntityType entityType)
            throws KDXException;

   public void deleteDefaultDynamicValueByBedId (Long bedId) throws KDXException;

   public List<Application> getApplicationsByType (AppSourceType appSourceType) throws KDXException;

   public void updateInstanceRIOntoTermsWithConceptInfo (Long modelGroupId, Long conceptBEDId, String conceptName,
            Long typeBEDId, String typeName) throws KDXException;

   public void deleteInstanceRIOntoTermsByConceptBEDId (Long modelGroupId, Long conceptBEDId) throws KDXException;

   public void deleteRIOntoTermsByProfileBEDId (Long modelId, Long profileBEDId) throws KDXException;

   public void deleteBusinessEntityMaintenanceDetailsByEntityBedId (Long entityBedId) throws KDXException;

   public void deleteBusinessEntityMaintenanceInstancesByConceptId (Long modelId, Long conceptId) throws KDXException;

   public List<Long> getInstanceIdsByConceptId (Long modelId, Long conceptId) throws KDXException;

   public List<RIOntoTerm> getTermsByLookupWords (List<String> words, List<Long> modelGroupIds,
            boolean skipLocationTypeRecognition, List<Long> locationChildTypeBedIds) throws KDXException;

   public List<Long> findMatchingTypeInstanceIncludingVariations (Long modelGroupId, Long typeId, String instanceValue,
            BusinessEntityType entityType) throws KDXException;

   public List<Concept> getLookupTypeConceptsForModelBySearchString (Long modelId, String searchString,
            List<Long> locationRealizedTypeIds, Long retrievalLimit) throws KDXException;

   public Long getEntityCountWithTypeBedIds (Long modelId, List<Long> locationChildrenBedIds) throws KDXException;

   public boolean isConceptMatchedBehavior (Long conceptBedId, BehaviorType behaviorType) throws KDXException;

   public List<ContentCleanupPattern> getContentCleanupPatterns (Long applicationId) throws KDXException;

   public void createContentCleanupPattern (ContentCleanupPattern contentCleanupPattern) throws KDXException;

   public UnstructuredApplicationDetail getUnstructuredApplicationDetailByApplicationId (Long applicationId)
            throws KDXException;

   public void createUnstructuredApplicationDetail (UnstructuredApplicationDetail unstructuredApplicationDetail)
            throws KDXException;

   public void updateUnstructuredApplicationDetail (UnstructuredApplicationDetail unstructuredApplicationDetail)
            throws KDXException;

   public RISharedUserModelMapping getRISharedUserModelMappingByAppInstanceBedId (Long appInstanceBedId,
            Set<Long> modelGroupIds) throws KDXException;

   public Application getApplicationByKey (String applicationKey) throws KDXException;

   public BusinessEntityDefinition getRelationBEDById (Long modelId, Long relationId) throws KDXException;

   public void deleteInstanceVariationsForConcept (Long modelId, Long conceptId) throws KDXException;

   public void deleteInstanceBedsForConcept (Long modelId, Long conceptId) throws KDXException;

   public void deleteInstanceByIds (List<Long> instanceIds) throws KDXException;

   public boolean hasInstances (Long modelId, Long conceptId) throws KDXException;

   public List<Concept> getConceptByBEDIds (List<Long> conceptBEDIds) throws KDXException;

   public void deleteHierarchy (Hierarchy hierarchy) throws KDXException;

   public List<Hierarchy> getHierarchiesByModelId (Long modelId) throws KDXException;

   public List<Hierarchy> getExistingHierarchiesForConcept (Long conceptBedId) throws KDXException;

   public Hierarchy getHierarchyByNameForModel (Long modelId, String hierarchyName) throws KDXException;

   public List<Hierarchy> getHierarchiesByConceptBEDIds (List<Long> conceptBEDIDs) throws KDXException;

   public List<Application> getAllActiveStructuredApplicationsByPublishMode (PublishAssetMode publishMode)
            throws KDXException;

   public List<Application> getAllActiveStructuredEligibleApplicationsByUserId (Long userId,
            boolean skipOtherCommunityApps) throws KDXException;

   public List<Application> getAllEligibleApplicationsByUserId (Long userId, boolean skipOtherCommunityApps)
            throws KDXException;

   public List<Application> getApplicationsForByPublishModeOrderByRank (PublishAssetMode publishMode)
            throws KDXException;

}