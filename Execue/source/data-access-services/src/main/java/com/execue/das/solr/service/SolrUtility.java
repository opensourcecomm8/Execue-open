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


package com.execue.das.solr.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.common.SolrInputDocument;

import com.execue.core.util.ExecueCoreUtil;
import com.execue.das.solr.bean.SolrConditionEntity;
import com.execue.das.solr.bean.SolrDocument;
import com.execue.das.solr.bean.SolrFacetQuery;
import com.execue.das.solr.bean.SolrFacetQueryConstantParameters;
import com.execue.das.solr.bean.SolrFacetQueryInfo;
import com.execue.das.solr.bean.SolrFacetResponse;
import com.execue.das.solr.bean.SolrFacetResponseDetail;
import com.execue.das.solr.bean.SolrRangeEntity;
import com.execue.das.solr.type.SolrFieldType;
import com.execue.das.solr.type.SolrOperatorType;

/**
 * @author Vishay
 */
public class SolrUtility {

   public static List<SolrInputDocument> buildSolrInputDocuments (List<SolrDocument> solrDocuments) {
      List<SolrInputDocument> solrInputDocumentsList = new ArrayList<SolrInputDocument>();
      for (SolrDocument solrDocument : solrDocuments) {
         solrInputDocumentsList.add(buildSolrInputDocument(solrDocument));
      }
      return solrInputDocumentsList;
   }

   public static SolrInputDocument buildSolrInputDocument (SolrDocument solrDocument) {
      SolrInputDocument solrInputDocument = new SolrInputDocument();
      Map<String, Object> documentFieldValues = solrDocument.getDocumentFieldValues();
      for (String documentField : documentFieldValues.keySet()) {
         solrInputDocument.addField(documentField, documentFieldValues.get(documentField));
      }
      return solrInputDocument;
   }

   @SuppressWarnings ("deprecation")
   public static SolrQuery buildSolrQuery (SolrFacetQuery solrFacetQuery) {
      SolrQuery solrQuery = new SolrQuery();
      solrQuery.setQuery(solrFacetQuery.getQuery());
      if (ExecueCoreUtil.isCollectionNotEmpty(solrFacetQuery.getFilterQueries())) {
         for (String filterQuery : solrFacetQuery.getFilterQueries()) {
            solrQuery.addFilterQuery(filterQuery);
         }
      }
      if (ExecueCoreUtil.isCollectionNotEmpty(solrFacetQuery.getFacetFields())) {
         for (String facetField : solrFacetQuery.getFacetFields()) {
            solrQuery.addFacetField(facetField);
         }
      }
      Map<String, List<SolrFacetQueryInfo>> facetQueries = solrFacetQuery.getFacetQueries();
      if (ExecueCoreUtil.isMapNotEmpty(facetQueries)) {
         for (String facetField : facetQueries.keySet()) {
            List<SolrFacetQueryInfo> facetQueryInfoList = facetQueries.get(facetField);
            for (SolrFacetQueryInfo facetQueryInfo : facetQueryInfoList) {
               solrQuery.addFacetQuery(facetQueryInfo.getFacetQuery());
            }
         }
      }
      SolrFacetQueryConstantParameters solrFacetQueryConstantParameters = solrFacetQuery
               .getSolrFacetQueryConstantParameters();
      solrQuery.setFacet(solrFacetQueryConstantParameters.isFacet());
      solrQuery.setFacetLimit(solrFacetQueryConstantParameters.getFacetLimit());
      solrQuery.setFacetMinCount(solrFacetQueryConstantParameters.getFetchFacetsWithMinCount());
      solrQuery.setFacetSort(solrFacetQueryConstantParameters.isFacetSortByCount());
      solrQuery.setRows(solrFacetQueryConstantParameters.getTotalRows());
      solrQuery.setParam(solrFacetQueryConstantParameters.getFacetMethodParamName(), solrFacetQueryConstantParameters
               .getFacetMethodParamValue());
      return solrQuery;
   }

   @SuppressWarnings ("unchecked")
   public static List<SolrFacetResponse> parserQueryResponse (QueryResponse queryResponse, SolrFacetQuery solrFacetQuery) {
      List<SolrFacetResponse> solrFacetResponseList = new ArrayList<SolrFacetResponse>();
      List<SolrFacetResponse> facetFieldResponseList = parseQueryResponseForFacetFields(queryResponse);
      if (ExecueCoreUtil.isCollectionNotEmpty(facetFieldResponseList)) {
         solrFacetResponseList.addAll(facetFieldResponseList);
      }
      List<SolrFacetResponse> facetQueriesResponseList = parseQueryResponseForFacetQueries(queryResponse,
               solrFacetQuery);
      if (ExecueCoreUtil.isCollectionNotEmpty(facetQueriesResponseList)) {
         solrFacetResponseList.addAll(facetQueriesResponseList);
      }
      return solrFacetResponseList;
   }

   private static List<SolrFacetResponse> parseQueryResponseForFacetQueries (QueryResponse queryResponse,
            SolrFacetQuery solrFacetQuery) {
      List<SolrFacetResponse> solrFacetResponseList = new ArrayList<SolrFacetResponse>();
      Map<String, List<SolrFacetQueryInfo>> inputFacetQueries = solrFacetQuery.getFacetQueries();
      Map<String, Integer> facetQueryResponse = queryResponse.getFacetQuery();
      if (ExecueCoreUtil.isMapNotEmpty(facetQueryResponse) && ExecueCoreUtil.isMapNotEmpty(inputFacetQueries)) {
         for (String facetField : inputFacetQueries.keySet()) {
            List<SolrFacetResponseDetail> facetResponseDetailList = new ArrayList<SolrFacetResponseDetail>();
            for (SolrFacetQueryInfo facetQueryInfo : inputFacetQueries.get(facetField)) {
               Integer count = facetQueryResponse.get(facetQueryInfo.getFacetQuery());
               if (count > 0) {
                  facetResponseDetailList
                           .add(new SolrFacetResponseDetail(facetQueryInfo.getLabel(), count.longValue()));
               }
            }
            solrFacetResponseList.add(new SolrFacetResponse(facetField, facetResponseDetailList));
         }
      }
      return solrFacetResponseList;
   }

   private static List<SolrFacetResponse> parseQueryResponseForFacetFields (QueryResponse queryResponse) {
      List<SolrFacetResponse> solrFacetResponseList = new ArrayList<SolrFacetResponse>();
      List<FacetField> facetFields = queryResponse.getFacetFields();
      if (ExecueCoreUtil.isCollectionNotEmpty(facetFields)) {
         for (FacetField facetField : facetFields) {
            List<Count> facetFieldValues = facetField.getValues();
            if (ExecueCoreUtil.isCollectionNotEmpty(facetFieldValues)) {
               List<SolrFacetResponseDetail> solrFacetResponseDetailList = new ArrayList<SolrFacetResponseDetail>();
               for (Count count : facetFieldValues) {
                  solrFacetResponseDetailList.add(new SolrFacetResponseDetail(count.getName(), count.getCount()));
               }
               solrFacetResponseList.add(new SolrFacetResponse(facetField.getName(), solrFacetResponseDetailList));
            }
         }
      }
      return solrFacetResponseList;
   }

   public static SolrConditionEntity buildCompositeSolrConditionEntity (List<SolrConditionEntity> conditionEntities) {
      SolrConditionEntity conditionEntity = new SolrConditionEntity();
      conditionEntity.setCompositeCondition(true);
      conditionEntity.setSubConditions(conditionEntities);
      return conditionEntity;
   }

   public static SolrConditionEntity buildSolrConditionEntity (String facetField, String rhsValue,
            SolrFieldType fieldType) {
      SolrConditionEntity conditionEntity = new SolrConditionEntity();
      conditionEntity.setFacetField(facetField);
      conditionEntity.setFieldType(fieldType);
      conditionEntity.setOperatorType(SolrOperatorType.EQUALS);
      conditionEntity.setRhsValue(rhsValue);
      return conditionEntity;
   }

   public static SolrConditionEntity buildSolrConditionEntity (String facetField, List<String> rhsValues,
            SolrFieldType fieldType) {
      SolrConditionEntity conditionEntity = new SolrConditionEntity();
      conditionEntity.setFacetField(facetField);
      conditionEntity.setFieldType(fieldType);
      conditionEntity.setOperatorType(SolrOperatorType.IN);
      conditionEntity.setRhsValues(rhsValues);
      return conditionEntity;
   }

   public static SolrConditionEntity buildSolrConditionEntity (String facetField, SolrRangeEntity rangeEntity) {
      SolrConditionEntity conditionEntity = new SolrConditionEntity();
      conditionEntity.setFacetField(facetField);
      conditionEntity.setOperatorType(SolrOperatorType.BETWEEN);
      conditionEntity.setRange(rangeEntity);
      return conditionEntity;
   }

   public static SolrRangeEntity buildSolrRangeEntity (String lowerBound, String upperBound) {
      return buildSolrRangeEntity(lowerBound, upperBound, true, null);
   }

   public static SolrRangeEntity buildSolrRangeEntity (String lowerBound, String upperBound, boolean inclusive) {
      return buildSolrRangeEntity(lowerBound, upperBound, inclusive, null);
   }

   public static SolrRangeEntity buildSolrRangeEntity (String lowerBound, String upperBound, boolean inclusive,
            String name) {
      SolrRangeEntity rangeEntity = new SolrRangeEntity();
      rangeEntity.setLowerBound(lowerBound);
      rangeEntity.setUpperBound(upperBound);
      rangeEntity.setInclusive(inclusive);
      rangeEntity.setRangeName(name);
      return rangeEntity;
   }

}
