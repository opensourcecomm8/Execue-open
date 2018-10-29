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


package com.execue.swi.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.execue.core.common.bean.ContentCleanupPattern;
import com.execue.core.common.bean.Page;
import com.execue.core.common.bean.entity.Behavior;
import com.execue.core.common.bean.entity.BusinessEntityByWord;
import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.BusinessEntityVariation;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.DefaultInstanceValue;
import com.execue.core.common.bean.entity.EntityDetailType;
import com.execue.core.common.bean.entity.Hierarchy;
import com.execue.core.common.bean.entity.Instance;
import com.execue.core.common.bean.entity.Mapping;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.common.bean.entity.ModelGroup;
import com.execue.core.common.bean.entity.ModelGroupMapping;
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
import com.execue.core.common.bean.entity.Vertical;
import com.execue.core.common.bean.entity.VerticalAppExample;
import com.execue.core.common.bean.governor.BusinessEntityInfo;
import com.execue.core.common.bean.nlp.SFLInfo;
import com.execue.core.common.bean.swi.PopularityBusinessEntityDefinitionInfo;
import com.execue.core.common.type.BehaviorType;
import com.execue.core.common.type.BusinessEntityType;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.MappingException;

// need to add methods for retrieval of information from base model
public interface IKDXRetrievalService extends ISWIService {

   public List<Stat> getAllStats () throws KDXException;

   public Behavior getBehaviorById (Long behaviorId) throws KDXException;

   public List<Concept> getConcepts (Long modelId) throws KDXException;

   public List<Concept> getConceptsByPopularity (Long modelId, Long limit) throws KDXException;

   public List<Instance> getInstances (Long modelId, Long conceptId) throws KDXException;

   public List<Instance> getTypeInstances (Long modelId, Long typeId) throws KDXException;

   public List<Instance> getInstancesByPage (Long modelId, Long conceptId, Page page) throws KDXException;

   public List<Instance> getInstancesForAssetMetaInfo (Long modelId, Long conceptId) throws KDXException;

   public BusinessEntityDefinition getMappedConceptBEDForColumn (Long assetId, Long tableId, Long columnId)
            throws MappingException;

   /**
    * Provides the count of instances for the conceptId and modelId
    * 
    * @param modelId
    * @param conceptId
    * @return
    * @throws KDXException
    */
   public long getInstancesCount (Long modelId, Long conceptId) throws KDXException;

   public Model getModelById (Long modelId) throws KDXException;

   public Concept getConceptById (Long conceptId) throws KDXException;

   public Instance getInstanceById (Long instanceId) throws KDXException;

   // public ModelGroup getModelGroupById (Long modelGroupId) throws
   // KDXException;

   /**
    * Returns the associated Model for a model-group-id. <BR>
    * This method should not be used for retrieving the base Model. Use the BaseKDXRetrievalService for getting the base
    * Model.
    * 
    * @throws KDXException
    * @param modelGroupId
    * @return Model
    */
   public Model getModelByUserModelGroupId (Long userModelGroupId) throws KDXException;

   public List<ModelGroup> getUserModelGroupsByModelId (Long modelId) throws KDXException;

   public ModelGroup getPrimaryGroup (Long modelId) throws KDXException;

   public Concept getPopulatedConceptWithStats (Long modelId, String conceptName) throws KDXException;

   public Concept getPopulatedConceptWithStats (Long conceptId) throws KDXException;

   public Stat getStatByName (String name) throws KDXException;

   public Stat getStatByBusinessEntityId (Long businessEntityDefinitionId) throws KDXException;

   public BusinessEntityDefinition getBusinessEntityDefinitionByIds (Long modelId, Long conceptId, Long instanceId)
            throws KDXException;

   public BusinessEntityDefinition getBusinessEntityDefinitionByTypeIds (Long modelId, Long typeId, Long instanceId)
            throws KDXException;

   public BusinessEntityDefinition getBusinessEntityDefinitionByNames (String modelName, String conceptName,
            String instanceName) throws KDXException;

   /**
    * Get just the Business Entity Definition object alone by not loading any of it's components
    * 
    * @param modelName
    * @param conceptName
    * @param instanceName
    * @return
    * @throws KDXException
    */
   public BusinessEntityDefinition getBusinessEntityDefinitionWrapperByNames (String modelName, String conceptName,
            String instanceName) throws KDXException;

   public BusinessEntityDefinition getBEDByMappingConceptName (Long modelId, Long mappingId, String conceptDisplayName)
            throws KDXException;

   public List<Concept> getConceptsBySearchString (Long modelId, String searchString) throws KDXException;

   public BusinessEntityDefinition getBusinessEntityDefinitionById (Long bedId) throws KDXException;

   public List<Instance> getInstancesOfConceptBySearchString (Long modelId, Long conceptId, String searchString)
            throws KDXException;

   public List<Concept> getConceptsForAsset (Long assetId) throws KDXException;

   public List<Concept> getConceptsForAssetMetaInfo (Long assetId) throws KDXException;

   public List<Concept> getMeasureConceptsForAsset (Long assetId) throws KDXException;

   public Integer getMeasureConceptsCountForAsset (Long assetId) throws KDXException;

   public BusinessEntityDefinition getBEDByInstanceDisplayName (String modelName, String conceptName,
            String instanceDisplayName) throws KDXException;

   public Mapping getMappingById (Long mappingId) throws KDXException;

   public Concept getConceptByName (Long modelId, String conceptName) throws KDXException;

   public Concept getConceptByDisplayName (Long modelId, String conceptDisplayName) throws KDXException;

   public List<Concept> getConcepts (List<ModelGroup> userModelGroups) throws KDXException;

   public List<Relation> getRelationsByModel (Long modelId) throws KDXException;

   public List<Relation> suggestRelationsByName (Long modelId, String searchString, int maxResults) throws KDXException;

   public List<Relation> getRelations (List<ModelGroup> userModelGroups) throws KDXException;

   public Relation getRelationByName (Long modelId, String relationName, boolean includeBaseModelGroup)
            throws KDXException;

   public BusinessEntityDefinition getRelationBEDByName (Long modelId, String relationName) throws KDXException;

   public BusinessEntityDefinition getConceptBEDByName (Long modelId, String conceptName) throws KDXException;

   public BusinessEntityDefinition getBEDByRealizationName (Long modelId, String realizationName) throws KDXException;

   public BusinessEntityDefinition getInstanceBEDByName (Long modelId, String conceptName, String instanceName)
            throws KDXException;

   public BusinessEntityDefinition getInstanceBEDByDisplayName (Long modelId, String conceptName,
            String instanceDisplayName) throws KDXException;

   public Map<Long, String> getDisplayNamesByBEDIds (List<Long> bedIds) throws KDXException;

   public List<RIOntoTerm> getTermsByLookupWord (String word) throws KDXException;

   public List<RIOntoTerm> getTermsByLookupWordAndEntityType (String word, BusinessEntityType entityType)
            throws KDXException;

   public List<RIOntoTerm> getTermsByLookupWordsAndEntityType (List<String> words, BusinessEntityType entityType)
            throws KDXException;

   public Map<String, BusinessEntityByWord> getBusinessEntityWordMapByLookupWords (List<String> words)
            throws KDXException;

   public Map<String, List<RIOntoTerm>> getOntoTermsMapByLookupWords (List<String> words,
            boolean skipLocationTypeRecognition) throws KDXException;

   public BusinessEntityByWord getBusinessEntityWordMapByLookupWord (String word) throws KDXException;

   public List<SecondaryWord> getAllSecondaryWords () throws KDXException;

   public Map<String, SecondaryWord> getAllSecondaryWordsMapForModel (Long modelId) throws KDXException;

   public SFLTerm getSFLTermByWord (String word) throws KDXException;

   public SFLTerm getSFLTermById (Long sflTermId) throws KDXException;

   public List<SFLTermToken> getSFLTermTokensByLookupWord (String word) throws KDXException;

   public List<SFLTermToken> getSFLTermTokensByLookupWords (List<String> words) throws KDXException;

   public List<SFLTermToken> getSFLTermTokensByTermId (Long sflTermId) throws KDXException;

   public List<SFLInfo> getSFLInfoForWords (List<String> words, Set<Long> modelGroupIds) throws KDXException;

   public List<RIParallelWord> getParallelWordsByLookupWord (String word) throws KDXException;

   /**
    * Get the list Of RiParallelWords with the given woprds and where model Group id is in the list passed .
    * 
    * @param words
    * @param modelGroupIds
    * @return
    * @throws KDXException
    */
   public List<RIParallelWord> getParallelWordsByLookupWords (List<String> words, Set<Long> modelGroupIds)
            throws KDXException;

   public List<BusinessEntityDefinition> getRegularExpressionBasedInstances () throws KDXException;

   public DefaultInstanceValue getDefaultInstanceValue (Long businessEntityDefinitonId) throws KDXException;

   public Relation getRelationByID (Long resourceID) throws KDXException;

   public List<Instance> getInstancesByDisplayNameAcrossAllConcepts (Long modelId, String instanceDisplayName)
            throws KDXException;

   public List<BusinessEntityInfo> getAllConceptBusinessEntities (Long modelId) throws KDXException;

   public List<BusinessEntityInfo> getAllInstanceBusinessEntities (Long modelId) throws KDXException;

   public List<BusinessEntityInfo> getAllInstanceProfileBusinessEntities (Long modelId) throws KDXException;

   public List<BusinessEntityInfo> getAllConceptProfileBusinessEntities (Long modelId) throws KDXException;

   public List<BusinessEntityInfo> getAllRelationBusinessEntities (Long modelId) throws KDXException;

   public Long isExactBusinessTerm (Long modelId, String inputString) throws KDXException;

   public boolean isPartOfBusinessTerm (Long modelId, String inputString) throws KDXException;

   public boolean doesSFLTermExist (String sflTerm) throws KDXException;

   public List<Model> getAllModels () throws KDXException;

   public List<Model> getModelsByApplicationId (Long applicationId) throws KDXException;

   public Instance getLatestInstanceInserted (Long modelId, Long conceptId) throws KDXException;

   public String getInstanceNameByBedId (Long bedId) throws KDXException;

   public List<SFLTerm> getAllSFLTerms () throws KDXException;

   public void updateSFLTermTokens (List<SFLTermToken> termTokens) throws KDXException;

   public List<SFLTerm> getSFLTermsForKeyWord (String keyWord) throws KDXException;

   public boolean isModelExist (String modelName) throws KDXException;

   public boolean isCloudExist (String cloudName) throws KDXException;

   public List<RIOntoTerm> getTermsByLookupWordsAndEntityTypeInModel (List<String> words,
            BusinessEntityType entityType, Model model) throws KDXException;

   /**
    * Get the lIst of models associated By groupIds.
    * 
    * @param groupIds
    * @return
    * @throws KDXException
    */
   public List<Model> getModelsByGroupIds (Set<Long> groupIds) throws KDXException;

   /**
    * Method to get the list of sfl terms lazily loaded along with the sfl term token
    * 
    * @param words
    * @return
    * @throws KDXException
    */
   public List<SFLTermToken> getFullSFLTermTokensByLookupWords (List<String> words) throws KDXException;

   public Map<String, Instance> getInstanceDisplayNameMap (Long modelId, Long conceptId) throws KDXException;

   /**
    * Retrieves the list of business entity definitions for the instances of a concept
    * 
    * @param modelId
    * @param conceptId
    * @return list of business entity definitions of instances
    */
   public List<BusinessEntityDefinition> getInstanceBEDsByConceptId (long modelId, long conceptId) throws KDXException;

   public List<Long> getInstanceBEDIdsByConceptId (Long modelId, Long conceptId) throws KDXException;

   // Method for deletion of application hierarchy
   public List<ModelGroupMapping> getPopulatedModelGroupMapping (Long modelId) throws KDXException;

   public Model getModelByName (String modelName) throws KDXException;

   public List<PopularityBusinessEntityDefinitionInfo> getPopularityInfoForModelGroup (Long modelGroupId)
            throws KDXException;

   public List<Long> getSFLTermIdsTobeDeleted () throws KDXException;

   public List<Long> getAllSFLTermIds () throws KDXException;

   public List<SFLTerm> getPopulatedSFLTerms (List<Long> sflTermIds) throws KDXException;

   public List<Long> getSFLTermIdsForNonZeroHits () throws KDXException;

   /**
    * Retrieves the business entity definitions of all the non-base concepts belonging to a model ordered by the concept
    * display name.
    * 
    * @param modelId
    *           ID of the model
    * @return list of the concept beds
    * @throws KDXException
    *            Exception
    */
   public List<BusinessEntityDefinition> getConceptBEDsOfModel (Long modelId) throws KDXException;

   /**
    * Retrieves the SFLTerm id list whose corresponding RIOntoTerm entries are not found
    * 
    * @param contextId
    *           ID of the Context
    * @return list of SFLTerm ids
    * @throws KDXException
    *            Exception
    */
   public List<SFLTerm> getOrphanSFLTerms (Long contextId) throws KDXException;

   /**
    * Retrieves the RIOntoTerm list for a business entity
    * 
    * @param bedId
    *           Business Entity Definition Id
    * @param type
    *           the type of the business entity
    * @return List of RIOntoTerms
    * @throws KDXException
    *            Exception
    */
   public List<RIOntoTerm> getRIOntoTermsByBEDId (Long bedId, BusinessEntityType type) throws KDXException;

   public Long getInstanceBEDsCountByConceptId (Long conceptId) throws KDXException;

   public List<BusinessEntityDefinition> getInstanceBEDsByPage (Long conceptId, Long pageNumber, Long pageSize)
            throws KDXException;

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
    * @param typeBedId
    * @return list of BehaviorTypes
    * @throws KDXException
    */
   public List<BehaviorType> getAllPossibleBehavior (Long typeBedId) throws KDXException;

   /**
    * Method to get the list of behavior for the given entity bed id
    * 
    * @param entityBedId
    * @return list of BehaviorTypes
    * @throws KDXException
    */
   public List<BehaviorType> getAllBehaviorForEntity (Long entityBedId) throws KDXException;

   public List<RIOntoTerm> getTermsByLookupWordAndConceptBedId (String word, Long conceptBedId) throws KDXException;

   public List<PossibleAttribute> getPossibleAttributesByIds (List<Long> possibleAttributeIds) throws KDXException;

   public Type getTypeByName (String typeName) throws KDXException;

   /**
    * Return the type BusinessEntityDefinition based on the given typeName and default business entity type as TYPE
    * 
    * @param typeName
    *           Name of type
    * @return the BusinessEntityDefinition
    * @throws KDXException
    *            Exception
    */
   public BusinessEntityDefinition getTypeBedByName (String typeName) throws KDXException;

   /**
    * Return the type BusinessEntityDefinition based on the given typeName and isRealizedType flag
    * 
    * @param typeName
    *           Name of type
    * @param isRealizedType
    *           If type is realized type
    * @return Returns Business Entity Definition for this type
    * @throws KDXException
    *            Exception
    */
   public BusinessEntityDefinition getTypeBedByName (String typeName, boolean isRealizedType) throws KDXException;

   /**
    * Retutn Type BED ID based on its id
    * 
    * @param typeID
    *           ID of type
    * @param isRealizedType
    *           If type is realized type
    * @return Returns Business Entity Definition for this type
    * @throws KDXException
    *            Exception
    */
   public BusinessEntityDefinition getTypeBedByTypeID (Long typeID, boolean isRealizedType) throws KDXException;

   public BusinessEntityDefinition getTypeBedByTypeID (Long typeID) throws KDXException;

   public BusinessEntityDefinition getTypeBusinessEntityDefinition (Long typeId) throws KDXException;

   public List<PossibleAttribute> getAllPossibleAttributes (Long typeBedId) throws KDXException;

   public List<BusinessEntityDefinition> getRealizationsForTypeInModelIncludingBaseGroup (Long typeId, Long modelGroupId)
            throws KDXException;

   public List<BusinessEntityDefinition> getPopulatedRealizationsForTypeInModelIncludingBaseGroup (Long typeId,
            Long modelId) throws KDXException;

   public List<Type> getAllTypes () throws KDXException;

   public List<RIOntoTerm> getRegularExpressionBasedOntoTerms () throws KDXException;

   public PossibleAttribute getPossibleAttribute (Long typeBedId, Long componentTypeBedId, Long relationBedId)
            throws KDXException;

   public PossibleAttribute getPossibleAttribute (Long typeBedId, Long componentTypeBedId) throws KDXException;

   /**
    * @param conceptBedId
    * @param batchNumber
    * @param batchSize
    * @return the List<SFLTerm>
    * @throws KDXException
    */
   public List<SFLTerm> getSFLTermsForInstancesOfConceptByBatchNumber (Long conceptBedId, Long batchNumber,
            Long batchSize) throws KDXException;

   public List<BusinessEntityDefinition> getAllAttributes (Long modelId, Long typeId) throws KDXException;

   public Map<String, Double> getAllSecondaryWordsWeightMapForModel (Long modelId) throws KDXException;

   public List<SecondaryWord> getAllSecondaryWordsForModel (Long modelId) throws KDXException;

   public Set<String> getEligibleSecondaryWordsForModel (Long modelId, Long threshold) throws KDXException;

   public BusinessEntityDefinition getPopulatedBEDForIndexForms (Long BEDId) throws KDXException;

   public BusinessEntityDefinition getPopulatedTypeBusinessEntityDefinition (Long typeId) throws KDXException;

   public BusinessEntityDefinition getBEDByBehaviorName (String behaviorName) throws KDXException;

   public List<RIOntoTerm> getConceptTermsByTypeBedIdAndModelGroupId (Long typeBedId, Long modelGroupId)
            throws KDXException;

   public boolean isMatchedBusinessEntityType (Long bedId, String typeName) throws KDXException;

   public boolean isConceptMatchedBusinessEntityType (Long conceptId, Long modelId, String typeName)
            throws KDXException;

   public List<BusinessEntityDefinition> getConceptsOfParticularType (Long modelId, String typeName)
            throws KDXException;

   public List<Long> getSFLTermIdsByContextId (Long contextId) throws KDXException;

   public Vertical getVerticalByName (String name) throws KDXException;

   public Vertical getVerticalById (Long id) throws KDXException;

   public Map<String, List<VerticalAppExample>> getVerticalAppExamples () throws KDXException;

   public Map<String, List<VerticalAppExample>> getVerticalAppExamplesByDay (int day) throws KDXException;

   public List<Instance> getInstancesByPopularity (Long modelId, Long conceptId, Long limit) throws KDXException;

   public BusinessEntityDefinition getRelationBEDByNameIncludingBaseGroup (Long modelId, String relationName)
            throws KDXException;

   public Instance getInstanceByInstanceName (Long conceptBedId, String instanceName) throws KDXException;

   public Map<Long, RIOntoTerm> getConceptOntoTermsByConceptBedIds (Set<Long> conceptBedIds) throws KDXException;

   public List<PossibleDetailType> getPossibleDetailTypes (Long typeBedId) throws KDXException;

   public EntityDetailType getDetailTypeForConcept (Long conceptBedId) throws KDXException;

   public BusinessEntityDefinition getDefaultPopulatedDetailType (Long typeBedId) throws KDXException;

   public List<Long> getModelGroupIdsByApplicationId (Long applicationId) throws KDXException;

   public List<Concept> getMappedConceptsForParticularType (Long modelId, Long assetId, String typeName)
            throws KDXException;

   public List<Concept> getMappedConceptsForParticularBehaviour (Long modelId, Long assetId, BehaviorType behaviourType)
            throws KDXException;

   public List<Type> getAllVisibleTypes () throws KDXException;

   public List<BusinessEntityDefinition> getAllNonAttributeEntities (Long modelId) throws KDXException;

   public Map<String, String> getConceptInstanceDisplayNamesByAbbrevatedNames (Long modelId, String conceptName,
            List<String> abbrevatedNames) throws KDXException;

   public Instance getTopPopularityInstance (Long modelId, String originAirportConceptName) throws KDXException;

   /**
    * Get the Map of RiOntoTerms by passed set of entity BeIds
    * 
    * @param entityBedIds
    * @return
    * @throws KDXException
    */
   public Map<Long, RIOntoTerm> getOntoTermsByEntityBeId (Set<Long> entityBedIds) throws KDXException;

   public List<BusinessEntityVariation> getBusinessEntityVariations (Long entityBedId) throws KDXException;

   public List<String> getBusinessEntityVariationNames (Long entityBedId) throws KDXException;

   public List<RISharedUserModelMapping> getRISharedUserModelMappings (List<Long> baseInstanceBedIds,
            Set<Long> modelGroupIds) throws KDXException;

   public List<Long> getConceptBedIdsHavingInstances (Long modelId) throws KDXException;

   public List<Long> getConceptBedsHavingBehaviorType (List<Long> conceptBedIds, BehaviorType behaviorType)
            throws KDXException;

   public List<Instance> getInstancesByBEDIds (List<Long> instanceBEDIds) throws KDXException;

   /**
    * This Method Return only Concept by Concept bedId
    * 
    * @param conceptBEDId
    * @return Concept
    * @throws KDXException
    */

   public Concept getConceptByBEDId (Long conceptBEDId) throws KDXException;

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

   public List<Concept> getEligibleConceptsOfAssetForCubeByPage (Long assetId, Page page) throws KDXException;

   public List<RISharedUserModelMapping> getRISharedUserModelMappings (List<Long> baseInstanceBedIds)
            throws KDXException;

   public List<RIParallelWord> getParallelWordsByLookupWords (List<String> words) throws KDXException;

   public ModelGroup getModelGroupByTypeBedId (Long typeBedId) throws KDXException;

   public List<BusinessEntityDefinition> getBedByModelGroupsAndConceptId (List<Long> modelGroupIds, Long conceptId)
            throws KDXException;

   public List<Long> getInstanceIdsByConceptId (Long modelId, Long conceptId) throws KDXException;

   public Map<String, List<RIOntoTerm>> getOntoTermsMapByLookupWords (List<String> words, List<Long> modelGroupIds,
            boolean skipLocationTypeRecognition) throws KDXException;

   public List<Long> findMatchingTypeInstanceIncludingVariations (Long modelGroupId, Long typeId, String instanceValue,
            BusinessEntityType entityType) throws KDXException;

   public List<Concept> getLookupTypeConceptsForModelBySearchString (Long modelId, String searchString,
            List<Long> locationRealizedTypeIds, Long retrievalLimit) throws KDXException;

   public Long getEntityCountWithTypeBedIds (Long modelId, List<Long> locationChildrenBedIds) throws KDXException;

   public boolean isConceptMatchedBehavior (Long conceptBedId, BehaviorType behaviorType) throws KDXException;

   public List<ContentCleanupPattern> getContentCleanupPatterns (Long applicationId) throws KDXException;

   // Content Cleanup Pattern related methods
   public void createContentCleanupPattern (ContentCleanupPattern contentCleanupPattern) throws KDXException;

   public List<BusinessEntityDefinition> getPossibleDetailTypesForType (Long typeBedId) throws KDXException;

   public RISharedUserModelMapping getRISharedUserModelMappingByAppInstanceBedId (Long appInstanceBedId,
            Set<Long> modelGroupIds) throws KDXException;

   public BusinessEntityDefinition getUnpopulatedBusinessEntityDefinitionByIds (Long modelId, Long conceptId,
            Long instanceId) throws KDXException;

   public BusinessEntityDefinition getRelationBEDById (Long modelId, Long relationId) throws KDXException;

   public boolean hasInstances (Long modelId, Long conceptId) throws KDXException;

   public List<Concept> getConceptByBEDIds (List<Long> conceptBEDIds) throws KDXException;

   public Hierarchy getHierarchyById (Long hierarchyId) throws KDXException;

   public List<Hierarchy> getHierarchiesByModelId (Long modelId) throws KDXException;

   public List<Hierarchy> getExistingHierarchiesForConcept (Long conceptBedId) throws KDXException;

   public Hierarchy getHierarchyByNameForModel (Long modelId, String hierarchyName) throws KDXException;

   public List<Hierarchy> getHierarchiesByConceptBEDIds (List<Long> conceptBEDIDs) throws KDXException;

}