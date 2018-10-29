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


package com.execue.platform.unstructured;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.execue.core.common.bean.ExecueFacet;
import com.execue.core.common.bean.ExecueFacetDetail;
import com.execue.core.common.bean.FacetQueryInput;
import com.execue.core.common.bean.LocationDetail;
import com.execue.core.common.bean.entity.unstructured.FeatureRange;
import com.execue.core.common.bean.entity.unstructured.RIFeatureContent;
import com.execue.core.common.bean.entity.unstructured.SemantifiedContentFeatureInformation;
import com.execue.core.common.bean.entity.unstructured.UserQueryFeatureInformation;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.FacetNatureType;
import com.execue.core.common.type.FeatureValueType;
import com.execue.core.common.type.ProcessingFlagType;
import com.execue.core.common.util.ExecueBeanUtil;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.core.util.ExecueDateTimeUtils;
import com.execue.das.configuration.IDataAccessServicesConfigurationService;
import com.execue.das.solr.bean.SolrConditionEntity;
import com.execue.das.solr.bean.SolrDistanceConditionEntity;
import com.execue.das.solr.bean.SolrDocument;
import com.execue.das.solr.bean.SolrFacetFieldEntity;
import com.execue.das.solr.bean.SolrFacetQueryEntity;
import com.execue.das.solr.bean.SolrFacetQueryInput;
import com.execue.das.solr.bean.SolrFacetResponse;
import com.execue.das.solr.bean.SolrFacetResponseDetail;
import com.execue.das.solr.bean.SolrLocationInfo;
import com.execue.das.solr.bean.SolrRangeEntity;
import com.execue.das.solr.bean.SolrStaticFieldsConfigurationInfo;
import com.execue.das.solr.service.SolrUtility;
import com.execue.das.solr.type.SolrFieldType;
import com.execue.platform.exception.PlatformException;
import com.execue.swi.exception.KDXException;
import com.execue.swi.service.IApplicationRetrievalService;
import com.execue.uswh.configuration.IUnstructuredWHConfigurationService;
import com.execue.uswh.exception.UnstructuredWarehouseException;
import com.execue.uswh.helper.UnstructuredWarehouseHelper;
import com.execue.uswh.service.IUnstructuredWHFeatureContentLookupService;
import com.execue.uswh.service.IUnstructuredWarehouseRetrievalService;

/**
 * @author Vishay
 */
public class UnstructuredFacetServiceHelper {

   private IDataAccessServicesConfigurationService    dataAccessServicesConfigurationService;
   private IUnstructuredWHFeatureContentLookupService unstructuredWHFeatureContentLookupService;
   private IUnstructuredWHConfigurationService        unstructuredWHConfigurationService;
   private IApplicationRetrievalService               applicationRetrievalService;
   private IUnstructuredWarehouseRetrievalService     unstructuredWarehouseRetrievalService;
   private UnstructuredWarehouseHelper                unstructuredWarehouseHelper;

   public IDataAccessServicesConfigurationService getDataAccessServicesConfigurationService () {
      return dataAccessServicesConfigurationService;
   }

   public void setDataAccessServicesConfigurationService (
            IDataAccessServicesConfigurationService dataAccessServicesConfigurationService) {
      this.dataAccessServicesConfigurationService = dataAccessServicesConfigurationService;
   }

   public String buildDocumentId (Long contextId, Long semantifiedContentId) {
      String documentIdFieldValue = contextId
               + getDataAccessServicesConfigurationService().getSolrDocumentIdFieldSeperator() + semantifiedContentId;
      return documentIdFieldValue;
   }

   /**
    * This method builds the execue solr document from the scfi list from semantification
    * 
    * @param featureInfoList
    * @return
    * @throws PlatformException
    */
   public SolrDocument buildSolrDocument (List<SemantifiedContentFeatureInformation> featureInfoList)
            throws PlatformException {
      SolrDocument solrDocument = null;
      try {
         // get the static fields defined in the config
         SolrStaticFieldsConfigurationInfo staticFieldsConfigurationInfo = getDataAccessServicesConfigurationService()
                  .getSolrStaticFieldsConfigurationInfo();
         String multiValuedFeatureSeperator = getDataAccessServicesConfigurationService()
                  .getSolrMultiValuedFeatureSeperator();
         // remove the invalid features. it means "-1" it comes just to add document in our system(for keyword search)
         // irrespective of no valid reduced form.
         featureInfoList = removeInvalidFeatures(featureInfoList);
         // for each feature, get the values as list. Only in multivalued feature, we should have more than one value in
         // this list
         Map<Long, List<Object>> uniqueFeatureValuesList = populateUniqueFeatureValuesList(featureInfoList);
         solrDocument = new SolrDocument();
         Map<String, Object> documentFieldValues = new LinkedHashMap<String, Object>();
         // take any one article row
         SemantifiedContentFeatureInformation SemantifiedContentFeatureInformation = featureInfoList.get(0);
         Long semantifiedContentId = SemantifiedContentFeatureInformation.getSemantifiedContentId();
         Long contextId = SemantifiedContentFeatureInformation.getContextId();
         String documentIdFieldValue = buildDocumentId(contextId, semantifiedContentId);
         documentFieldValues.put(staticFieldsConfigurationInfo.getDocumentIdField().getFacetField(),
                  documentIdFieldValue);
         documentFieldValues.put(staticFieldsConfigurationInfo.getContextIdField().getFacetField(), contextId);
         documentFieldValues.put(staticFieldsConfigurationInfo.getLocationIdField().getFacetField(),
                  SemantifiedContentFeatureInformation.getLocationId());
         documentFieldValues.put(staticFieldsConfigurationInfo.getImagePresentField().getFacetField(),
                  SemantifiedContentFeatureInformation.getImagePresent().getValue());
         documentFieldValues.put(staticFieldsConfigurationInfo.getLatitudeField().getFacetField(),
                  SemantifiedContentFeatureInformation.getLatitude());
         documentFieldValues.put(staticFieldsConfigurationInfo.getLongitudeField().getFacetField(),
                  SemantifiedContentFeatureInformation.getLongitude());
         documentFieldValues.put(staticFieldsConfigurationInfo.getProcessingStateField().getFacetField(),
                  SemantifiedContentFeatureInformation.getProcessingState().getValue());
         documentFieldValues.put(staticFieldsConfigurationInfo.getContentDateField().getFacetField(),
                  SemantifiedContentFeatureInformation.getSemantifiedContentDate());
         // for each feature
         for (Long featureId : uniqueFeatureValuesList.keySet()) {
            RIFeatureContent featureContent = getUnstructuredWHFeatureContentLookupService()
                     .getRIFeatureContentByFeatureId(contextId, featureId);
            List<Object> featureValues = uniqueFeatureValuesList.get(featureId);
            Object documentFeatureValue = featureValues.get(0);
            // if feature is multivalued
            if (CheckType.YES.equals(featureContent.getMultiValued())) {
               // if more than one value for multivalued feature, then combine the value per solr notation
               if (featureValues.size() > 1) {
                  List<String> multivaluedFeatureValues = new ArrayList<String>();
                  for (Object featureValue : featureValues) {
                     multivaluedFeatureValues.add((String) featureValue);
                  }
                  documentFeatureValue = ExecueCoreUtil.joinCollection(multivaluedFeatureValues,
                           multiValuedFeatureSeperator);
               }
            }
            documentFieldValues.put(featureContent.getFieldName(), documentFeatureValue);
         }
         solrDocument.setDocumentFieldValues(documentFieldValues);
         solrDocument.setDocumentId(SemantifiedContentFeatureInformation.getSemantifiedContentId());
      } catch (UnstructuredWarehouseException e) {
         throw new PlatformException(e.getCode(), e);
      }
      return solrDocument;
   }

   private List<SemantifiedContentFeatureInformation> removeInvalidFeatures (
            List<SemantifiedContentFeatureInformation> featureInfoList) {
      Long dummyFeatureId = getUnstructuredWHConfigurationService().getDummyFeatureId();
      List<SemantifiedContentFeatureInformation> validFeatures = new ArrayList<SemantifiedContentFeatureInformation>();
      for (SemantifiedContentFeatureInformation featureInformation : featureInfoList) {
         if (!featureInformation.getFeatureId().equals(dummyFeatureId)) {
            validFeatures.add(featureInformation);
         }
      }
      return validFeatures;
   }

   private Map<Long, List<Object>> populateUniqueFeatureValuesList (
            List<SemantifiedContentFeatureInformation> featureInfoList) {
      Map<Long, List<Object>> uniqueFeatureValuesList = new HashMap<Long, List<Object>>();
      for (SemantifiedContentFeatureInformation featureInformation : featureInfoList) {
         Long featureId = featureInformation.getFeatureId();
         FeatureValueType valueType = featureInformation.getFeatureValueType();
         Object featureValue = null;
         if (FeatureValueType.VALUE_NUMBER.equals(valueType)) {
            featureValue = featureInformation.getFeatureNumberValue();
         } else if (FeatureValueType.VALUE_STRING.equals(valueType)) {
            featureValue = featureInformation.getFeatureValue();
         }
         if (ExecueCoreUtil.isCollectionEmpty(uniqueFeatureValuesList.get(featureId))) {
            List<Object> featureValues = new ArrayList<Object>();
            uniqueFeatureValuesList.put(featureId, featureValues);
         }
         uniqueFeatureValuesList.get(featureId).add(featureValue);
      }
      return uniqueFeatureValuesList;
   }

   /**
    * This method builds the list of solr query input from the facet input.
    * 
    * @param contextId
    * @param facetQueryInput
    * @return
    * @throws PlatformException
    */
   public List<SolrFacetQueryInput> buildSolrFacetQueryInput (Long contextId, FacetQueryInput facetQueryInput)
            throws PlatformException {
      List<SolrFacetQueryInput> queryInputList = new ArrayList<SolrFacetQueryInput>();
      try {
         // get all the facet features
         List<RIFeatureContent> facetFeatures = getUnstructuredWHFeatureContentLookupService()
                  .getFacetRIFeatureContentByContextId(contextId);
         // based on the nature, build the queries. In case of combo only we can have more than one query
         FacetNatureType facetNatureType = getApplicationRetrievalService().getFacetNatureByApplicationId(contextId);
         if (FacetNatureType.BROWSABLE.equals(facetNatureType)) {
            SolrFacetQueryInput browsableBasedSolrFacetQueryInput = buildBrowsableBasedSolrFacetQueryInput(contextId,
                     facetQueryInput, facetFeatures);
            queryInputList.add(browsableBasedSolrFacetQueryInput);
         } else if (FacetNatureType.RESULT_BASED.equals(facetNatureType)) {
            SolrFacetQueryInput resultBasedSolrFacetQueryInput = buildResultBasedSolrFacetQueryInput(contextId,
                     facetQueryInput, facetFeatures);
            queryInputList.add(resultBasedSolrFacetQueryInput);
         } else if (FacetNatureType.COMBINATION.equals(facetNatureType)) {
            queryInputList.addAll(buildComboBasedSolrFacetQueryInput(contextId, facetQueryInput, facetFeatures));
         }
      } catch (UnstructuredWarehouseException e) {
         throw new PlatformException(e.getCode(), e);
      } catch (KDXException e) {
         throw new PlatformException(e.getCode(), e);
      }
      return queryInputList;
   }

   private SolrFacetQueryInput buildSolrFacetQueryInputExceptUserQueryConditions (Long contextId,
            FacetQueryInput facetQueryInput, List<RIFeatureContent> facetFeatures)
            throws UnstructuredWarehouseException {
      // this method builds select plus default conditions other than user asked conditions.
      SolrFacetQueryInput queryInput = new SolrFacetQueryInput();
      queryInput.setFacetFields(buildFieldEntities(facetFeatures));
      queryInput.setFacetQueries(buildQueryEntities(contextId, facetFeatures));
      queryInput.addQueryCondition(buildProcessingStateCondition());
      queryInput.addQueryCondition(buildContextIdConditionEntity(contextId));
      queryInput.addQueryCondition(buildImagePresentCondition(facetQueryInput.getImagePresent()));
      queryInput.setDistanceCondition(buildDistanceCondition(facetQueryInput.getDistance(), facetQueryInput
               .getLocationDetails()));
      return queryInput;
   }

   private SolrFacetQueryInput buildResultBasedSolrFacetQueryInput (Long contextId, FacetQueryInput facetQueryInput,
            List<RIFeatureContent> facetFeatures) throws UnstructuredWarehouseException {
      // build the query except user conditions
      SolrFacetQueryInput queryInput = buildSolrFacetQueryInputExceptUserQueryConditions(contextId, facetQueryInput,
               facetFeatures);
      // preparing the feature conditions and filter them based on default values
      List<FeatureConditionInfo> featureConditionInfoList = populateAndFilterFeatureConditionInfo(contextId,
               facetQueryInput);
      if (ExecueCoreUtil.isCollectionNotEmpty(featureConditionInfoList)) {
         // build solr condition by feature Id map
         Map<Long, SolrConditionEntity> conditionEntitiesByFeatureIdMap = buildConditionEntities(featureConditionInfoList);
         // add all the conditions to the result based query
         for (Long featureId : conditionEntitiesByFeatureIdMap.keySet()) {
            queryInput.addQueryCondition(conditionEntitiesByFeatureIdMap.get(featureId));
         }
      }
      return queryInput;
   }

   private SolrFacetQueryInput buildBrowsableBasedSolrFacetQueryInput (Long contextId, FacetQueryInput facetQueryInput,
            List<RIFeatureContent> facetFeatures) throws UnstructuredWarehouseException {
      // add the select section with default conditions to it
      SolrFacetQueryInput queryInput = new SolrFacetQueryInput();
      queryInput.setFacetFields(buildFieldEntities(facetFeatures));
      queryInput.setFacetQueries(buildQueryEntities(contextId, facetFeatures));
      queryInput.addQueryCondition(buildProcessingStateCondition());
      queryInput.addQueryCondition(buildContextIdConditionEntity(contextId));
      return queryInput;
   }

   private List<SolrFacetQueryInput> buildComboBasedSolrFacetQueryInput (Long contextId,
            FacetQueryInput facetQueryInput, List<RIFeatureContent> facetFeatures)
            throws UnstructuredWarehouseException {
      List<SolrFacetQueryInput> queryInputList = new ArrayList<SolrFacetQueryInput>();
      // build the feature conditions and filter them
      List<FeatureConditionInfo> featureConditionInfoList = populateAndFilterFeatureConditionInfo(contextId,
               facetQueryInput);
      // if no conditions asked, then build default query
      if (ExecueCoreUtil.isCollectionEmpty(featureConditionInfoList)) {
         queryInputList
                  .add(buildSolrFacetQueryInputExceptUserQueryConditions(contextId, facetQueryInput, facetFeatures));
      } else {
         SolrConditionEntity processingStateCondition = buildProcessingStateCondition();
         SolrConditionEntity contextIdConditionEntity = buildContextIdConditionEntity(contextId);
         SolrConditionEntity imagePresentCondition = buildImagePresentCondition(facetQueryInput.getImagePresent());
         SolrDistanceConditionEntity distanceCondition = buildDistanceCondition(facetQueryInput.getDistance(),
                  facetQueryInput.getLocationDetails());
         // build condition entity map with featureId
         Map<Long, SolrConditionEntity> conditionEntitiesByFeatureIdMap = buildConditionEntities(featureConditionInfoList);
         // build map of facet dependency with facets
         Map<String, List<RIFeatureContent>> facetsByFacetDependencyMap = buildFacetsByFacetDependencyMap(facetFeatures);
         String facetDependencyDelimeter = getUnstructuredWHConfigurationService().getFacetDependencyDelimeter();
         for (String facetDependency : facetsByFacetDependencyMap.keySet()) {
            List<Long> facetDependencyList = new ArrayList<Long>();
            if (ExecueCoreUtil.isNotEmpty(facetDependency)) {
               if (facetDependency.contains(facetDependencyDelimeter)) {
                  String[] facetDependencyTokenList = facetDependency.split(facetDependencyDelimeter);
                  for (String facetDependencyToken : facetDependencyTokenList) {
                     facetDependencyList.add(new Long(facetDependencyToken));
                  }
               } else {
                  facetDependencyList.add(new Long(facetDependency));
               }
            }
            // list of facets for this dependency
            List<RIFeatureContent> facets = facetsByFacetDependencyMap.get(facetDependency);
            // look for the conditions by dependent facets
            List<SolrConditionEntity> conditionEntities = new ArrayList<SolrConditionEntity>();
            for (Long facetCondition : facetDependencyList) {
               SolrConditionEntity userCondition = conditionEntitiesByFeatureIdMap.get(facetCondition);
               if (userCondition != null) {
                  conditionEntities.add(userCondition);
               }
            }
            SolrFacetQueryInput queryInput = new SolrFacetQueryInput();
            queryInput.setFacetFields(buildFieldEntities(facets));
            queryInput.setFacetQueries(buildQueryEntities(contextId, facets));
            queryInput.setQueryConditions(conditionEntities);
            queryInput.addQueryCondition(imagePresentCondition);
            queryInput.addQueryCondition(processingStateCondition);
            queryInput.addQueryCondition(contextIdConditionEntity);
            queryInput.setDistanceCondition(distanceCondition);
            queryInputList.add(queryInput);
         }
      }
      return queryInputList;
   }

   private Map<String, List<RIFeatureContent>> buildFacetsByFacetDependencyMap (List<RIFeatureContent> facetFeatures) {
      Map<String, List<RIFeatureContent>> facetsByFacetDependency = new HashMap<String, List<RIFeatureContent>>();
      for (RIFeatureContent facetFeature : facetFeatures) {
         String facetDependency = facetFeature.getFacetDependency();
         if (facetsByFacetDependency.get(facetDependency) == null) {
            List<RIFeatureContent> facets = new ArrayList<RIFeatureContent>();
            facetsByFacetDependency.put(facetDependency, facets);
         }
         facetsByFacetDependency.get(facetDependency).add(facetFeature);
      }
      return facetsByFacetDependency;
   }

   private Map<Long, SolrConditionEntity> buildConditionEntities (List<FeatureConditionInfo> conditionInfoList) {
      Map<Long, SolrConditionEntity> conditionEntities = new HashMap<Long, SolrConditionEntity>();
      if (ExecueCoreUtil.isCollectionNotEmpty(conditionInfoList)) {
         for (FeatureConditionInfo conditionInfo : conditionInfoList) {
            SolrConditionEntity conditionEntity = null;
            FeatureValueType featureValueType = conditionInfo.getFeatureContent().getFeatureValueType();
            if (FeatureValueType.VALUE_NUMBER.equals(featureValueType)) {
               conditionEntity = buildNumberBasedFeatureCondition(conditionInfo);
            } else if (FeatureValueType.VALUE_STRING.equals(featureValueType)) {
               conditionEntity = buildStringBasedFeatureCondition(conditionInfo);
            }
            conditionEntities.put(conditionInfo.getFeatureId(), conditionEntity);
         }
      }
      return conditionEntities;
   }

   private SolrConditionEntity buildNumberBasedFeatureCondition (FeatureConditionInfo conditionInfo) {
      SolrConditionEntity conditionEntity = null;
      RIFeatureContent featureContent = conditionInfo.getFeatureContent();
      List<FeatureRange> rangeValues = conditionInfo.getFeatureRangeValues();
      List<SolrConditionEntity> conditionEntities = new ArrayList<SolrConditionEntity>();
      for (FeatureRange featureRange : rangeValues) {
         SolrRangeEntity rangeEntity = buildRangeEntity(featureRange);
         conditionEntities.add(SolrUtility.buildSolrConditionEntity(featureContent.getFieldName(), rangeEntity));
      }
      if (conditionEntities.size() == 1) {
         conditionEntity = conditionEntities.get(0);
      } else {
         conditionEntity = SolrUtility.buildCompositeSolrConditionEntity(conditionEntities);
      }
      return conditionEntity;
   }

   private SolrConditionEntity buildStringBasedFeatureCondition (FeatureConditionInfo conditionInfo) {
      SolrConditionEntity conditionEntity = null;
      RIFeatureContent featureContent = conditionInfo.getFeatureContent();
      List<String> featureValues = conditionInfo.getFeatureValues();
      SolrFieldType solrFieldType = getCorrespondingSolrFieldType(featureContent.getFeatureValueType());
      List<SolrConditionEntity> conditionEntities = new ArrayList<SolrConditionEntity>();
      for (String featureValue : featureValues) {
         conditionEntities.add(SolrUtility.buildSolrConditionEntity(featureContent.getFieldName(), featureValue,
                  solrFieldType));
      }
      if (conditionEntities.size() == 1) {
         conditionEntity = conditionEntities.get(0);
      } else {
         conditionEntity = SolrUtility.buildCompositeSolrConditionEntity(conditionEntities);
      }
      return conditionEntity;
   }

   private List<SolrFacetFieldEntity> buildFieldEntities (List<RIFeatureContent> facetFeatures) {
      List<SolrFacetFieldEntity> fieldEntities = new ArrayList<SolrFacetFieldEntity>();
      if (ExecueCoreUtil.isCollectionNotEmpty(facetFeatures)) {
         for (RIFeatureContent featureContent : facetFeatures) {
            if (FeatureValueType.VALUE_STRING.equals(featureContent.getFeatureValueType())) {
               SolrFieldType solrFieldType = getCorrespondingSolrFieldType(featureContent.getFeatureValueType());
               fieldEntities.add(new SolrFacetFieldEntity(featureContent.getFieldName(), solrFieldType));
            }
         }
      }
      return fieldEntities;
   }

   private List<SolrFacetQueryEntity> buildQueryEntities (Long contextId, List<RIFeatureContent> facetFeatures)
            throws UnstructuredWarehouseException {
      List<SolrFacetQueryEntity> queryEntities = new ArrayList<SolrFacetQueryEntity>();
      if (ExecueCoreUtil.isCollectionNotEmpty(facetFeatures)) {
         for (RIFeatureContent featureContent : facetFeatures) {
            if (FeatureValueType.VALUE_NUMBER.equals(featureContent.getFeatureValueType())) {
               SolrFieldType solrFieldType = getCorrespondingSolrFieldType(featureContent.getFeatureValueType());
               List<FeatureRange> featureRanges = getUnstructuredWarehouseRetrievalService()
                        .getFeatureRangesByFeatureId(contextId, featureContent.getFeatureId());
               List<SolrRangeEntity> rangeEntities = buildRangeEntities(featureRanges);
               SolrFacetQueryEntity queryEntity = new SolrFacetQueryEntity();
               queryEntity.setFacetField(featureContent.getFieldName());
               queryEntity.setFieldType(solrFieldType);
               queryEntity.setFacetRanges(rangeEntities);
               queryEntities.add(queryEntity);
            }
         }
      }
      return queryEntities;
   }

   private SolrRangeEntity buildRangeEntity (FeatureRange range) {
      String startValue = null;
      String endValue = null;
      if (range.getStartValue() != null) {
         startValue = range.getStartValue().toString();
      }
      if (range.getEndValue() != null) {
         endValue = range.getEndValue().toString();
      }
      return SolrUtility.buildSolrRangeEntity(startValue, endValue, true, range.getRangeName());
   }

   private List<SolrRangeEntity> buildRangeEntities (List<FeatureRange> ranges) {
      List<SolrRangeEntity> rangeEntities = new ArrayList<SolrRangeEntity>();
      for (FeatureRange range : ranges) {
         rangeEntities.add(buildRangeEntity(range));
      }
      return rangeEntities;
   }

   private SolrFieldType getCorrespondingSolrFieldType (FeatureValueType featureValueType) {
      SolrFieldType fieldType = null;
      if (FeatureValueType.VALUE_NUMBER.equals(featureValueType)) {
         fieldType = SolrFieldType.NUMBER;
      } else if (FeatureValueType.VALUE_STRING.equals(featureValueType)) {
         fieldType = SolrFieldType.STRING;
      }
      return fieldType;
   }

   // this method builds the feature condition info per feature, we fetch feature content and based on type we build
   // values or feature range list.
   private List<FeatureConditionInfo> populateAndFilterFeatureConditionInfo (Long contextId,
            FacetQueryInput facetQueryInput) throws UnstructuredWarehouseException {
      List<UserQueryFeatureInformation> userQueryFeatureInfoList = facetQueryInput.getQueryConditions();
      List<FeatureConditionInfo> uniqueFeatureConditionList = new ArrayList<FeatureConditionInfo>();
      if (ExecueCoreUtil.isCollectionNotEmpty(userQueryFeatureInfoList)) {
         userQueryFeatureInfoList = filterDummyFeature(userQueryFeatureInfoList);
         Map<Long, List<UserQueryFeatureInformation>> uniqueUserQueryFeatureValuesList = new HashMap<Long, List<UserQueryFeatureInformation>>();
         for (UserQueryFeatureInformation userQueryFeatureInformation : userQueryFeatureInfoList) {
            Long featureId = userQueryFeatureInformation.getFeatureId();
            if (ExecueCoreUtil.isCollectionEmpty(uniqueUserQueryFeatureValuesList.get(featureId))) {
               List<UserQueryFeatureInformation> featureInfoList = new ArrayList<UserQueryFeatureInformation>();
               uniqueUserQueryFeatureValuesList.put(featureId, featureInfoList);
            }
            uniqueUserQueryFeatureValuesList.get(featureId).add(userQueryFeatureInformation);
         }
         // from the map, build the object now
         for (Long featureId : uniqueUserQueryFeatureValuesList.keySet()) {
            FeatureConditionInfo featureConditionInfo = new FeatureConditionInfo();
            RIFeatureContent featureContent = getUnstructuredWHFeatureContentLookupService()
                     .getRIFeatureContentByFeatureId(contextId, featureId);
            featureConditionInfo.setFeatureId(featureId);
            featureConditionInfo.setFeatureContent(featureContent);
            List<UserQueryFeatureInformation> featureBasedInformationList = uniqueUserQueryFeatureValuesList
                     .get(featureId);
            // filter the default values not asked by user
            featureBasedInformationList = filterDefaultFeatureValues(featureBasedInformationList, featureContent);
            if (FeatureValueType.VALUE_STRING.equals(featureContent.getFeatureValueType())) {
               List<String> featureValues = new ArrayList<String>();
               for (UserQueryFeatureInformation userQueryFeatureInformation : featureBasedInformationList) {
                  featureValues.add(userQueryFeatureInformation.getStartValue());
               }
               featureConditionInfo.setFeatureValues(featureValues);
            } else if (FeatureValueType.VALUE_NUMBER.equals(featureContent.getFeatureValueType())) {
               List<FeatureRange> featureRanges = new ArrayList<FeatureRange>();
               for (UserQueryFeatureInformation userQueryFeatureInformation : featureBasedInformationList) {
                  featureRanges.add(buildFeatureRange(userQueryFeatureInformation));
               }
               featureConditionInfo.setFeatureRangeValues(featureRanges);
            }
            uniqueFeatureConditionList.add(featureConditionInfo);
         }
      }
      return uniqueFeatureConditionList;
   }

   private List<UserQueryFeatureInformation> filterDefaultFeatureValues (
            List<UserQueryFeatureInformation> userQueryFeatureInfoList, RIFeatureContent featureContent) {
      return getUnstructuredWarehouseHelper().filterDefaultFeatureValues(userQueryFeatureInfoList, featureContent);
   }

   private List<UserQueryFeatureInformation> filterDummyFeature (
            List<UserQueryFeatureInformation> userQueryFeatureInfoList) {
      Long dummyFeatureId = getUnstructuredWHConfigurationService().getDummyFeatureId();
      List<UserQueryFeatureInformation> validFeatureInfoList = new ArrayList<UserQueryFeatureInformation>();
      for (UserQueryFeatureInformation featureInformation : userQueryFeatureInfoList) {
         if (!featureInformation.getFeatureId().equals(dummyFeatureId)) {
            validFeatureInfoList.add(featureInformation);
         }
      }
      return validFeatureInfoList;
   }

   private FeatureRange buildFeatureRange (UserQueryFeatureInformation userQueryFeatureInformation) {
      FeatureRange range = new FeatureRange();
      String startValue = userQueryFeatureInformation.getStartValue();
      if (ExecueCoreUtil.isNotEmpty(startValue)) {
         range.setStartValue(new Double(startValue));
      }
      String endValue = userQueryFeatureInformation.getEndValue();
      if (ExecueCoreUtil.isNotEmpty(endValue)) {
         range.setEndValue(new Double(endValue));
      }
      return range;
   }

   private SolrDistanceConditionEntity buildDistanceCondition (Double distance, List<LocationDetail> locationDetailList) {
      SolrDistanceConditionEntity distanceCondition = null;
      if (distance != null && ExecueCoreUtil.isCollectionNotEmpty(locationDetailList)) {
         SolrFacetFieldEntity longitudeField = getDataAccessServicesConfigurationService()
                  .getSolrStaticFieldLongitude();
         SolrFacetFieldEntity latitudeField = getDataAccessServicesConfigurationService().getSolrStaticFieldLatitude();
         List<SolrLocationInfo> locationInfoList = new ArrayList<SolrLocationInfo>();
         for (LocationDetail locationDetail : locationDetailList) {
            locationInfoList.add(new SolrLocationInfo(locationDetail.getLongitude(), locationDetail.getLatitude()));
         }
         distanceCondition = new SolrDistanceConditionEntity(distance, longitudeField.getFacetField(), latitudeField
                  .getFacetField(), locationInfoList);
      }
      return distanceCondition;
   }

   private SolrConditionEntity buildImagePresentCondition (CheckType imagePresent) {
      SolrConditionEntity imagePresentCondition = null;
      if (CheckType.YES.equals(imagePresent)) {
         SolrFacetFieldEntity imagePresentField = getDataAccessServicesConfigurationService()
                  .getSolrStaticFieldImagePresent();
         imagePresentCondition = SolrUtility.buildSolrConditionEntity(imagePresentField.getFacetField(), imagePresent
                  .getValue().toString(), imagePresentField.getFieldType());
      }
      return imagePresentCondition;
   }

   private SolrConditionEntity buildProcessingStateCondition () {
      SolrFacetFieldEntity processingStateField = getDataAccessServicesConfigurationService()
               .getSolrStaticFieldProcessingState();
      List<String> validProcessingStates = new ArrayList<String>();
      validProcessingStates.add(ProcessingFlagType.PROCESSED.getValue().toString());
      validProcessingStates.add(ProcessingFlagType.NOT_PROCESSED.getValue().toString());
      return SolrUtility.buildSolrConditionEntity(processingStateField.getFacetField(), validProcessingStates,
               processingStateField.getFieldType());
   }

   public SolrConditionEntity buildContextIdConditionEntity (Long contextId) {
      SolrFacetFieldEntity contextIdField = getDataAccessServicesConfigurationService().getSolrStaticFieldContextId();
      return SolrUtility.buildSolrConditionEntity(contextIdField.getFacetField(), contextId.toString(), contextIdField
               .getFieldType());
   }

   public SolrConditionEntity buildContentDateUpperBoundRangeConditionEntity (Date contentDate) {
      SolrFacetFieldEntity contentDateField = getDataAccessServicesConfigurationService()
               .getSolrStaticFieldContentDate();
      String contentDateAsString = ExecueDateTimeUtils.getISODateTimeFormatFromDate(contentDate);
      SolrRangeEntity rangeEntity = SolrUtility.buildSolrRangeEntity(null, contentDateAsString, false);
      return SolrUtility.buildSolrConditionEntity(contentDateField.getFacetField(), rangeEntity);
   }

   /**
    * This method parses the solr facet response to execue facet
    * 
    * @param contextId
    * @param facetResponseList
    * @return
    * @throws PlatformException
    */
   public List<ExecueFacet> parseFacetQueryResponse (Long contextId, List<SolrFacetResponse> facetResponseList)
            throws PlatformException {
      List<ExecueFacet> facets = new ArrayList<ExecueFacet>();
      try {
         if (ExecueCoreUtil.isCollectionNotEmpty(facetResponseList)) {
            for (SolrFacetResponse facetResponse : facetResponseList) {
               String facetField = facetResponse.getFacetField();
               List<SolrFacetResponseDetail> responseDetails = facetResponse.getResponseDetails();
               if (ExecueCoreUtil.isCollectionNotEmpty(responseDetails)) {
                  // from the solr facet field, we will get the feature content and populate execue facet
                  RIFeatureContent featureContent = getUnstructuredWHFeatureContentLookupService()
                           .getRIFeatureContentByFieldName(contextId, facetField);
                  ExecueFacet facet = new ExecueFacet();
                  facet.setId(featureContent.getFeatureId().toString());
                  facet.setName(featureContent.getFeatureDisplayName());
                  facet.setType(ExecueBeanUtil.getCorrespondingExecueFacetType(featureContent.getFeatureValueType()));
                  facet.setDisplayOrder(featureContent.getFacetDisplayOrder());
                  List<ExecueFacetDetail> facetDetails = new ArrayList<ExecueFacetDetail>();
                  for (SolrFacetResponseDetail responseDetail : responseDetails) {
                     ExecueFacetDetail facetDetail = new ExecueFacetDetail();
                     facetDetail.setName(responseDetail.getFacetFieldValue());
                     facetDetail.setCount(responseDetail.getCount());
                     facetDetails.add(facetDetail);
                  }
                  facet.setFacetDetails(facetDetails);
                  facets.add(facet);
               }
            }
         }
      } catch (UnstructuredWarehouseException e) {
         throw new PlatformException(e.getCode(), e);
      }
      return facets;
   }

   public IUnstructuredWHFeatureContentLookupService getUnstructuredWHFeatureContentLookupService () {
      return unstructuredWHFeatureContentLookupService;
   }

   public void setUnstructuredWHFeatureContentLookupService (
            IUnstructuredWHFeatureContentLookupService unstructuredWHFeatureContentLookupService) {
      this.unstructuredWHFeatureContentLookupService = unstructuredWHFeatureContentLookupService;
   }

   public IApplicationRetrievalService getApplicationRetrievalService () {
      return applicationRetrievalService;
   }

   public void setApplicationRetrievalService (IApplicationRetrievalService applicationRetrievalService) {
      this.applicationRetrievalService = applicationRetrievalService;
   }

   class FeatureConditionInfo {

      private Long               featureId;
      private RIFeatureContent   featureContent;
      private List<String>       featureValues;
      private List<FeatureRange> featureRangeValues;

      public Long getFeatureId () {
         return featureId;
      }

      public void setFeatureId (Long featureId) {
         this.featureId = featureId;
      }

      public RIFeatureContent getFeatureContent () {
         return featureContent;
      }

      public void setFeatureContent (RIFeatureContent featureContent) {
         this.featureContent = featureContent;
      }

      public List<String> getFeatureValues () {
         return featureValues;
      }

      public void setFeatureValues (List<String> featureValues) {
         this.featureValues = featureValues;
      }

      public List<FeatureRange> getFeatureRangeValues () {
         return featureRangeValues;
      }

      public void setFeatureRangeValues (List<FeatureRange> featureRangeValues) {
         this.featureRangeValues = featureRangeValues;
      }
   }

   public IUnstructuredWHConfigurationService getUnstructuredWHConfigurationService () {
      return unstructuredWHConfigurationService;
   }

   public void setUnstructuredWHConfigurationService (
            IUnstructuredWHConfigurationService unstructuredWHConfigurationService) {
      this.unstructuredWHConfigurationService = unstructuredWHConfigurationService;
   }

   /**
    * @return the unstructuredWarehouseRetrievalService
    */
   public IUnstructuredWarehouseRetrievalService getUnstructuredWarehouseRetrievalService () {
      return unstructuredWarehouseRetrievalService;
   }

   /**
    * @param unstructuredWarehouseRetrievalService
    *           the unstructuredWarehouseRetrievalService to set
    */
   public void setUnstructuredWarehouseRetrievalService (
            IUnstructuredWarehouseRetrievalService unstructuredWarehouseRetrievalService) {
      this.unstructuredWarehouseRetrievalService = unstructuredWarehouseRetrievalService;
   }

   public UnstructuredWarehouseHelper getUnstructuredWarehouseHelper () {
      return unstructuredWarehouseHelper;
   }

   public void setUnstructuredWarehouseHelper (UnstructuredWarehouseHelper unstructuredWarehouseHelper) {
      this.unstructuredWarehouseHelper = unstructuredWarehouseHelper;
   }

}
