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


package com.execue.das.solr.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.execue.core.util.ExecueCoreUtil;
import com.execue.das.configuration.IDataAccessServicesConfigurationService;
import com.execue.das.solr.bean.SolrConditionEntity;
import com.execue.das.solr.bean.SolrDistanceConditionEntity;
import com.execue.das.solr.bean.SolrDistanceConfigurationInfo;
import com.execue.das.solr.bean.SolrFacetFieldEntity;
import com.execue.das.solr.bean.SolrFacetQuery;
import com.execue.das.solr.bean.SolrFacetQueryConstantParameters;
import com.execue.das.solr.bean.SolrFacetQueryEntity;
import com.execue.das.solr.bean.SolrFacetQueryInfo;
import com.execue.das.solr.bean.SolrFacetQueryInput;
import com.execue.das.solr.bean.SolrLocationInfo;
import com.execue.das.solr.bean.SolrRangeEntity;
import com.execue.das.solr.exception.SolrException;
import com.execue.das.solr.service.ISolrConstants;
import com.execue.das.solr.service.ISolrQueryGenerationService;
import com.execue.das.solr.type.SolrFieldType;

/**
 * @author Vishay
 */
public class SolrQueryGenerationServiceImpl implements ISolrQueryGenerationService, ISolrConstants {

   private IDataAccessServicesConfigurationService dataAccessServicesConfigurationService;

   @Override
   public SolrFacetQuery generateSolrFacetQuery (SolrFacetQueryInput queryInput) throws SolrException {
      List<String> facetFields = buildFacetFields(queryInput.getFacetFields());
      Map<String, List<SolrFacetQueryInfo>> facetQueries = buildFacetQueries(queryInput.getFacetQueries());
      String distanceCondition = buildDistanceCondition(queryInput.getDistanceCondition());
      String query = buildMergedConditionQuery(queryInput.getQueryConditions());
      List<String> filterConditionQueries = buildConditionQueries(queryInput.getFilterQueryConditions());
      SolrFacetQueryConstantParameters queryConstantParameters = getDataAccessServicesConfigurationService()
               .getSolrFacetQueryConstantParameters();

      SolrFacetQuery facetQuery = new SolrFacetQuery();
      facetQuery.setFacetFields(facetFields);
      facetQuery.setFacetQueries(facetQueries);
      if (ExecueCoreUtil.isEmpty(query)) {
         query = getDataAccessServicesConfigurationService().getSolrDefaultQuery();
      }
      facetQuery.setQuery(query);
      List<String> filterQueries = new ArrayList<String>();
      if (ExecueCoreUtil.isNotEmpty(distanceCondition)) {
         filterQueries.add(distanceCondition);
      }
      if (ExecueCoreUtil.isCollectionNotEmpty(filterConditionQueries)) {
         filterQueries.addAll(filterConditionQueries);
      }
      facetQuery.setFilterQueries(filterQueries);
      facetQuery.setSolrFacetQueryConstantParameters(queryConstantParameters);
      return facetQuery;
   }

   @Override
   public String generateSolrConditionQuery (List<SolrConditionEntity> conditionEntities) throws SolrException {
      return buildMergedConditionQuery(conditionEntities);
   }

   private List<String> buildFacetFields (List<SolrFacetFieldEntity> facetFieldEntities) {
      List<String> facetFields = new ArrayList<String>();
      if (ExecueCoreUtil.isCollectionNotEmpty(facetFieldEntities)) {
         for (SolrFacetFieldEntity facetFieldEntity : facetFieldEntities) {
            facetFields.add(facetFieldEntity.getFacetField());
         }
      }
      return facetFields;
   }

   private Map<String, List<SolrFacetQueryInfo>> buildFacetQueries (List<SolrFacetQueryEntity> facetQueryEntities) {
      Map<String, List<SolrFacetQueryInfo>> facetQueries = new HashMap<String, List<SolrFacetQueryInfo>>();
      if (ExecueCoreUtil.isCollectionNotEmpty(facetQueryEntities)) {
         for (SolrFacetQueryEntity facetQueryEntity : facetQueryEntities) {
            facetQueries.put(facetQueryEntity.getFacetField(), buildFacetQueryInfoList(facetQueryEntity));
         }
      }
      return facetQueries;
   }

   private List<SolrFacetQueryInfo> buildFacetQueryInfoList (SolrFacetQueryEntity facetQueryEntity) {
      List<SolrFacetQueryInfo> facetQueryInfoList = new ArrayList<SolrFacetQueryInfo>();
      String facetField = facetQueryEntity.getFacetField();
      List<SolrRangeEntity> rangeEntities = facetQueryEntity.getFacetRanges();
      for (SolrRangeEntity rangeEntity : rangeEntities) {
         String rangeCondition = buildRangeCondition(rangeEntity);
         String facetQuery = facetField + COLON + rangeCondition;
         facetQueryInfoList.add(new SolrFacetQueryInfo(facetQuery, rangeEntity.getRangeName()));
      }
      return facetQueryInfoList;
   }

   private String buildRangeCondition (SolrRangeEntity rangeEntity) {
      StringBuilder rangeCondition = new StringBuilder();
      String lowerBound = buildRangeValue(rangeEntity.getLowerBound());
      String upperBound = buildRangeValue(rangeEntity.getUpperBound());
      rangeCondition.append(lowerBound);
      rangeCondition.append(SPACE);
      rangeCondition.append(RANGE_DEFINITION_OPERATOR);
      rangeCondition.append(SPACE);
      rangeCondition.append(upperBound);
      return buildRange(rangeCondition.toString(), rangeEntity.isInclusive());
   }

   private String buildRange (String rangeCondition, boolean inclusive) {
      if (inclusive) {
         rangeCondition = INCLUSIVE_RANGE_START + rangeCondition + INCLUSIVE_RANGE_END;
      } else {
         rangeCondition = EXCLUSIVE_RANGE_START + rangeCondition + EXCLUSIVE_RANGE_END;
      }
      return rangeCondition;
   }

   private String buildRangeValue (String rangeValue) {
      if (ExecueCoreUtil.isEmpty(rangeValue)) {
         rangeValue = INFINITY_RANGE_SYMBOL;
      }
      return rangeValue;
   }

   private String buildDistanceCondition (SolrDistanceConditionEntity distanceConditionEntity) {
      String distanceCondition = null;
      if (distanceConditionEntity != null) {
         List<SolrLocationInfo> locationInfoList = distanceConditionEntity.getLocationInfoList();
         SolrDistanceConfigurationInfo distanceConfigurationInfo = getDataAccessServicesConfigurationService()
                  .getSolrDistanceConfigurationInfo();
         // single location
         if (locationInfoList.size() == 1) {
            SolrLocationInfo solrLocationInfo = locationInfoList.get(0);
            distanceCondition = distanceConfigurationInfo.getSingleLocationDistanceFormula();
            distanceCondition = distanceCondition.replace(distanceConfigurationInfo.getLongitudeReplaceToken(),
                     solrLocationInfo.getLongitude().toString());
            distanceCondition = distanceCondition.replace(distanceConfigurationInfo.getLatitudeReplaceToken(),
                     solrLocationInfo.getLatitude().toString());
         }
         // multiple locations
         else if (locationInfoList.size() > 1) {
            distanceCondition = distanceConfigurationInfo.getMultipleLocationDistanceFormula();
            String multipleLocationLongitudeLatitudeValue = buildMultipleLocationLongitudeLatitudeValue(locationInfoList);
            distanceCondition = distanceCondition.replace(distanceConfigurationInfo
                     .getMutipleLontitudeLatitudeReplaceToken(), multipleLocationLongitudeLatitudeValue);
         }

         distanceCondition = distanceCondition.replace(distanceConfigurationInfo.getDistanceReplaceToken(),
                  distanceConditionEntity.getDistance().toString());
         distanceCondition = distanceCondition.replace(distanceConfigurationInfo.getLongitudeFieldReplaceToken(),
                  distanceConditionEntity.getSolrLongitudefieldName());
         distanceCondition = distanceCondition.replace(distanceConfigurationInfo.getLatitudeFieldReplaceToken(),
                  distanceConditionEntity.getSolrLatitudefieldName());
      }
      return distanceCondition;
   }

   private String buildMultipleLocationLongitudeLatitudeValue (List<SolrLocationInfo> locationInfoList) {
      List<String> longLatCombinations = new ArrayList<String>();
      for (SolrLocationInfo locationInfo : locationInfoList) {
         longLatCombinations.add(locationInfo.getLongitude() + COMMA + locationInfo.getLatitude());
      }
      return ExecueCoreUtil.joinCollection(longLatCombinations, SEMICOLON);
   }

   private String buildCondition (SolrConditionEntity conditionEntity) {
      StringBuilder conditionQuery = new StringBuilder();
      if (conditionEntity.isCompositeCondition()) {
         List<String> subConditions = new ArrayList<String>();
         for (SolrConditionEntity subConditionEntity : conditionEntity.getSubConditions()) {
            String subCondition = buildCondition(subConditionEntity);
            subConditions.add(subCondition);
         }
         String condition = ExecueCoreUtil.joinCollection(subConditions, SPACE + OR_OPERATOR + SPACE);
         conditionQuery.append(BRACE_START);
         conditionQuery.append(condition);
         conditionQuery.append(BRACE_END);
      } else {
         conditionQuery.append(conditionEntity.getFacetField());
         conditionQuery.append(COLON);
         switch (conditionEntity.getOperatorType()) {
            case EQUALS:
               String singleOperandValue = buildValue(conditionEntity.getRhsValue(), conditionEntity.getFieldType());
               conditionQuery.append(singleOperandValue);
               break;
            case IN:
               List<String> parsedRhsValues = new ArrayList<String>();
               for (String rhsValue : conditionEntity.getRhsValues()) {
                  parsedRhsValues.add(buildValue(rhsValue, conditionEntity.getFieldType()));
               }
               conditionQuery.append(BRACE_START);
               conditionQuery.append(ExecueCoreUtil.joinCollection(parsedRhsValues, SPACE));
               conditionQuery.append(BRACE_END);
               break;
            case BETWEEN:
               String rangeCondition = buildRangeCondition(conditionEntity.getRange());
               conditionQuery.append(rangeCondition);
               break;
         }
      }
      return conditionQuery.toString();
   }

   private String buildValue (String value, SolrFieldType fieldType) {
      if (SolrFieldType.STRING.equals(fieldType)) {
         value = DOUBLE_QUOTE + value + DOUBLE_QUOTE;
      }
      return value;
   }

   private List<String> buildConditionQueries (List<SolrConditionEntity> conditionEntities) {
      List<String> conditions = new ArrayList<String>();
      if (ExecueCoreUtil.isCollectionNotEmpty(conditionEntities)) {
         for (SolrConditionEntity conditionEntity : conditionEntities) {
            String condition = buildCondition(conditionEntity);
            conditions.add(condition);
         }
      }
      return conditions;
   }

   private String buildMergedConditionQuery (List<SolrConditionEntity> conditionEntities) {
      StringBuilder conditionQuery = new StringBuilder();
      if (ExecueCoreUtil.isCollectionNotEmpty(conditionEntities)) {
         List<String> conditionQueries = buildConditionQueries(conditionEntities);
         if (ExecueCoreUtil.isCollectionNotEmpty(conditionQueries)) {
            String condition = ExecueCoreUtil.joinCollection(conditionQueries, SPACE + AND_OPERATOR + SPACE);
            conditionQuery.append(BRACE_START);
            conditionQuery.append(condition);
            conditionQuery.append(BRACE_END);
         }
      }
      return conditionQuery.toString();
   }

   public IDataAccessServicesConfigurationService getDataAccessServicesConfigurationService () {
      return dataAccessServicesConfigurationService;
   }

   public void setDataAccessServicesConfigurationService (
            IDataAccessServicesConfigurationService dataAccessServicesConfigurationService) {
      this.dataAccessServicesConfigurationService = dataAccessServicesConfigurationService;
   }

}
