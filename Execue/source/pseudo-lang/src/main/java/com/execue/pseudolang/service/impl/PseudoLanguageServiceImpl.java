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


package com.execue.pseudolang.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.execue.core.common.bean.BusinessCondition;
import com.execue.core.common.bean.BusinessLimitClause;
import com.execue.core.common.bean.BusinessOrderClause;
import com.execue.core.common.bean.BusinessQuery;
import com.execue.core.common.bean.BusinessTerm;
import com.execue.core.common.bean.QueryForm;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.Conversion;
import com.execue.core.common.bean.governor.BusinessAssetTerm;
import com.execue.core.common.bean.governor.StructuredCondition;
import com.execue.core.common.bean.governor.StructuredLimitClause;
import com.execue.core.common.bean.governor.StructuredOrderClause;
import com.execue.core.common.bean.governor.StructuredQuery;
import com.execue.core.common.bean.nlp.NLPConstants;
import com.execue.core.common.bean.nlp.WordRecognitionState;
import com.execue.core.common.bean.pseudolang.NormalizedPseudoQuery;
import com.execue.core.common.bean.pseudolang.PseudoConditionEntity;
import com.execue.core.common.bean.pseudolang.PseudoEntity;
import com.execue.core.common.bean.pseudolang.PseudoLimitEntity;
import com.execue.core.common.bean.pseudolang.PseudoStat;
import com.execue.core.common.bean.qi.QIBusinessTerm;
import com.execue.core.common.bean.qi.QICondition;
import com.execue.core.common.bean.qi.QIConditionLHS;
import com.execue.core.common.bean.qi.QIConditionRHS;
import com.execue.core.common.bean.qi.QIOrderBy;
import com.execue.core.common.bean.qi.QISelect;
import com.execue.core.common.bean.qi.QISubQuery;
import com.execue.core.common.bean.qi.QIValue;
import com.execue.core.common.bean.querygen.QueryValue;
import com.execue.core.common.type.AssetEntityType;
import com.execue.core.common.type.ColumnType;
import com.execue.core.common.type.ConversionType;
import com.execue.core.common.type.DateQualifier;
import com.execue.core.common.type.OperandType;
import com.execue.core.common.type.OperatorType;
import com.execue.core.common.type.OrderLimitEntityType;
import com.execue.core.constants.ExecueConstants;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.SWIException;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.pseudolang.service.IPseudoLanguageService;
import com.execue.pseudolang.service.helper.PseudoLanguageServiceHelper;
import com.execue.swi.configuration.ISWIConfigurationService;
import com.execue.swi.service.IConversionService;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.util.conversion.timeframe.util.TimeFrameUtility;

/**
 * @author execue
 * @since 4.0
 * @version 1.0
 */
public class PseudoLanguageServiceImpl implements IPseudoLanguageService {

   private static final String      COMMA               = ",";
   private static final String      WHERE               = "where";
   private static final String      SUM_BY              = "summarized by";
   private static final String      SPACE               = " ";
   private static final String      LIMITED_TO_GRP      = "limited to a group";
   private static final String      ORDER_BY            = "ordered by";
   private static final String      HAVING              = "having";
   private static final String      AND_WITH_SPACES     = " and ";
   private static final String      OR_WITH_SPACES      = " or ";
   private static final String      EQUALS              = "equals";
   private static final String      NOT_EQUALS          = "not equal to";
   private static final String      GREATER_THAN        = "greater than";
   private static final String      GREATER_THAN_EQUALS = "greater than or equal to";
   private static final String      LESS_THAN           = "less than";
   private static final String      LESS_THAN_EQUALS    = "less than or equal to";
   private static final String      IS_NULL             = "is null";
   private static final String      IS_NOT_NULL         = "is not null";
   private static final String      BETWEEN             = "between";
   private static final String      IN                  = "in";
   private static final String      OPEN_BRACKET        = "(";
   private static final String      CLOSE_BRACKET       = ")";
   private static final String      OF_WITH_SPACES      = " of ";
   private static final String      BY_WITH_SPACES      = " by ";
   // private static final String COHORT_IDENTIFIER = "(*)";
   // private static final String GRP_OF = "group of";

   // constants for color coding
   private static final String      RECOG_INFO          = "recoginfo";
   private static final String      FULL_RECOG          = "fullrecog";
   private static final String      FULL_UNRECOG        = "fullunrecog";
   private static final String      PART_RECOG          = "partrecog";
   private static final String      PART_UNRECOG        = "partunrecog";

   private IConversionService       conversionService;
   private IKDXRetrievalService     kdxRetrievalService;
   private ISWIConfigurationService swiConfigurationService;

   public IConversionService getConversionService () {
      return conversionService;
   }

   public void setConversionService (IConversionService conversionService) {
      this.conversionService = conversionService;
   }

   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }

   public ISWIConfigurationService getSwiConfigurationService () {
      return swiConfigurationService;
   }

   public void setSwiConfigurationService (ISWIConfigurationService swiConfigurationService) {
      this.swiConfigurationService = swiConfigurationService;
   }

   /**
    * Generates the english statement assigning colors to each word in the statement based on the recognition states
    * 
    * @param queryWords
    * @param wordRecognitionStates
    * @return color coded pseudo language statement
    */
   public String getColorCodedPseudoLanguageStatement (List<String> queryWords,
            Map<Integer, WordRecognitionState> wordRecognitionStates) {
      List<String> validPosTypes = new ArrayList<String>();
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < queryWords.size(); i++) {
         String word = queryWords.get(i);
         boolean appendRecognition = true;
         WordRecognitionState wordRecognitionState = wordRecognitionStates.get(i);
         // if the concept name is not null and not empty, check for the SFL existence
         if (wordRecognitionState != null) {
            if (ExecueCoreUtil.isNotEmpty(wordRecognitionState.getConceptName())) {
               if (i < queryWords.size() - 1 && wordRecognitionState.getInstanceName() == null) {
                  WordRecognitionState nextWRS = wordRecognitionStates.get(i + 1);
                  if (nextWRS != null && wordRecognitionState.getConceptName().equals(nextWRS.getConceptName())) {
                     appendRecognition = false;
                  }
               } else if (i < queryWords.size() - 1 && wordRecognitionState.getInstanceName() != null) {
                  WordRecognitionState nextWRS = wordRecognitionStates.get(i + 1);
                  if (nextWRS != null && wordRecognitionState.getConceptName().equals(nextWRS.getConceptName())
                           && wordRecognitionState.getInstanceName().equals(nextWRS.getInstanceName())) {
                     appendRecognition = false;
                  }
               }
            }
         } else {
            appendRecognition = false;
         }
         if (wordRecognitionState != null && wordRecognitionState.getConceptName() == null) {
            appendRecognition = false;
         }
         // escape comma
         if (NLPConstants.NLP_TAG_POS_COMMA.equals(word)) {
            appendRecognition = false;
         }
         String colorWord = getColorCodeForWord(word, wordRecognitionState, validPosTypes, appendRecognition);
         if (i == 0 || NLPConstants.NLP_TAG_POS_COMMA.equals(word)) {
            sb.append(colorWord);
         } else {
            sb.append(" ").append(colorWord);
         }
      }
      return sb.toString().trim();
   }

   /*
    * This method processes a word using its recognition state to identity the color
    */
   private String getColorCodeForWord (String word, WordRecognitionState wordRecognitionState,
            List<String> validPosTypes, boolean appendRecognition) {
      String colorWord = "<span class=\"" + FULL_UNRECOG + "\">" + word + "</span>";
      if (wordRecognitionState != null) {
         String recognitionType = wordRecognitionState.getRecognitionType();
         String conjunctionType = wordRecognitionState.getConjunctionType();
         if (ExecueCoreUtil.isNotEmpty(recognitionType)) {
            if (ExecueCoreUtil.isEmpty(conjunctionType)) {
               // green - recognized entity
               colorWord = "<span class=\"" + FULL_RECOG + "\">" + word + "</span>";
            } else {
               // black - recognized entity
               colorWord = "<span class=\"" + PART_RECOG + "\">" + word + "</span>";
            }
         } else {
            if (wordRecognitionState.isValidPosType()) {
               // black - unrecognized entity
               colorWord = "<span class=\"" + PART_UNRECOG + "\">" + word + "</span>";
            } else {
               // red - unrecognized entity
               // do nothing as default will be applied
            }
         }
         if (appendRecognition) {
            if (!wordRecognitionState.isBaseConcept()) {
               if (wordRecognitionState.getInstanceName() != null) {
                  colorWord = colorWord + "<span class=\"" + RECOG_INFO + "\">("
                           + wordRecognitionState.getInstanceName() + ", " + wordRecognitionState.getConceptName()
                           + ")</span>";
               } else {
                  colorWord = colorWord + "<span class=\"" + RECOG_INFO + "\">("
                           + wordRecognitionState.getConceptName() + ")</span>";
               }
            }
         }
      }
      return colorWord;
   }

   /**
    * Generates the English language statement from the NormalizedPseudoQuery object
    * 
    * @param pseudoQuery
    * @return String
    */
   public String getPseudoLanguageStatement (NormalizedPseudoQuery pseudoQuery) {
      StringBuffer sbPseudoLanguageStatement = new StringBuffer();
      String metricsStatement = PseudoLanguageServiceHelper.statementForMetrics(pseudoQuery);
      String populationStatement = PseudoLanguageServiceHelper.statementForPopulation(pseudoQuery);
      String conditionsStatement = statementForConditions(pseudoQuery);
      String summarizationStatement = PseudoLanguageServiceHelper.statementForSummarizations(pseudoQuery);
      String orderClausesStatement = PseudoLanguageServiceHelper.statementForOrderClauses(pseudoQuery);
      String havingStatement = statementForHaving(pseudoQuery);
      String cohortStatement = statementForCohort(pseudoQuery);
      if (ExecueCoreUtil.isCollectionNotEmpty(pseudoQuery.getMetrics())) {
         sbPseudoLanguageStatement.append(metricsStatement);
      }
      // if (pseudoQuery.getPopulation() != null) {
      // sbPseudoLanguageStatement.append(SPACE).append(GRP_OF).append(SPACE).append(populationStatement);
      // }
      if (ExecueCoreUtil.isCollectionNotEmpty(pseudoQuery.getConditions())) {
         sbPseudoLanguageStatement.append(SPACE).append(WHERE).append(SPACE).append(conditionsStatement);
      }
      if (ExecueCoreUtil.isCollectionNotEmpty(pseudoQuery.getSummarizations())) {
         sbPseudoLanguageStatement.append(SPACE).append(SUM_BY).append(SPACE).append(summarizationStatement);
      }
      if (ExecueCoreUtil.isCollectionNotEmpty(pseudoQuery.getOrderClauses())) {
         sbPseudoLanguageStatement.append(SPACE).append(ORDER_BY).append(SPACE).append(orderClausesStatement);
      }
      if (ExecueCoreUtil.isCollectionNotEmpty(pseudoQuery.getHavingClauses())) {
         sbPseudoLanguageStatement.append(SPACE).append(HAVING).append(SPACE).append(havingStatement);
      }
      if (pseudoQuery.getCohortClause() != null && ExecueCoreUtil.isNotEmpty(cohortStatement)) {
         // if (ExecueCoreUtil.isEmpty(populationStatement)) {
         // sbPseudoLanguageStatement.append(SPACE).append(LIMITED_TO_GRP);
         // } else {
         sbPseudoLanguageStatement.append(SPACE).append(LIMITED_TO_GRP).append(OF_WITH_SPACES);
         sbPseudoLanguageStatement.append(populationStatement);
         // }
         sbPseudoLanguageStatement.append(cohortStatement);
      }
      return sbPseudoLanguageStatement.toString();
   }

   private String statementForCohort (NormalizedPseudoQuery pseudoQuery) {
      StringBuilder sbCohort = new StringBuilder();
      NormalizedPseudoQuery cohort = pseudoQuery.getCohortClause();
      if (cohort != null) {
         sbCohort.append(getPseudoLanguageStatement(cohort));
      }
      return sbCohort.toString();
   }

   /**
    * Generates the NormalizedPseudoQuery object from the BusinessQuery
    * 
    * @param businessQuery
    * @return NormalizedPseudoQuery
    */
   public NormalizedPseudoQuery getPseudoQuery (BusinessQuery businessQuery) {
      return processBusinessQuery(businessQuery);
   }

   private NormalizedPseudoQuery processBusinessQuery (BusinessQuery businessQuery) {
      // Initialize a new pseudo query object
      NormalizedPseudoQuery normalizedPseudoQuery = new NormalizedPseudoQuery();
      // Initialize the pseudo entities map
      Map<Long, PseudoEntity> pseudoEntities = new HashMap<Long, PseudoEntity>();
      // Set the entities map into the pseudo query object
      normalizedPseudoQuery.setPseudoEntities(pseudoEntities);

      List<PseudoEntity> pMetrics = null;
      List<PseudoEntity> pPopulation = null;
      List<PseudoConditionEntity> pConditions = null;
      List<PseudoEntity> pSummarizations = null;
      List<PseudoEntity> pOrderClauses = null;
      List<PseudoConditionEntity> pHavingClauses = null;
      NormalizedPseudoQuery pCohort = null;
      PseudoLimitEntity pTopBottom = null;

      // Process the metrics of the BQ
      if (ExecueCoreUtil.isCollectionNotEmpty(businessQuery.getMetrics())) {
         pMetrics = processMetrics(businessQuery.getMetrics(), normalizedPseudoQuery);
      }
      // Process the population of the BQ
      if (ExecueCoreUtil.isCollectionNotEmpty(businessQuery.getPopulations())) {
         pPopulation = processPopulation(businessQuery.getPopulations(), normalizedPseudoQuery);
      }
      // Process the conditions of the BQ
      if (ExecueCoreUtil.isCollectionNotEmpty(businessQuery.getConditions())) {
         for (BusinessCondition businessCondition : businessQuery.getConditions()) {
            if (pConditions == null) {
               pConditions = new ArrayList<PseudoConditionEntity>();
            }
            pConditions.add(processPseudoConditionEntity(businessCondition, normalizedPseudoQuery));
         }
      }
      // Process the summarizations of the BQ
      if (ExecueCoreUtil.isCollectionNotEmpty(businessQuery.getSummarizations())) {
         pSummarizations = processEntities(businessQuery.getSummarizations(), null, normalizedPseudoQuery);
      }
      // Process the order clauses of the BQ
      if (ExecueCoreUtil.isCollectionNotEmpty(businessQuery.getOrderClauses())) {
         for (BusinessOrderClause orderClause : businessQuery.getOrderClauses()) {
            if (pOrderClauses == null) {
               pOrderClauses = new ArrayList<PseudoEntity>();
            }
            pOrderClauses.add(PseudoLanguageServiceHelper.processPseudoEntity(orderClause.getBusinessTerm(),
                     normalizedPseudoQuery));
         }
      }
      // Process the having clauses of the BQ
      if (ExecueCoreUtil.isCollectionNotEmpty(businessQuery.getHavingClauses())) {
         for (BusinessCondition businessCondition : businessQuery.getHavingClauses()) {
            if (pHavingClauses == null) {
               pHavingClauses = new ArrayList<PseudoConditionEntity>();
            }
            pHavingClauses.add(processPseudoConditionEntity(businessCondition, normalizedPseudoQuery));
         }
      }
      // Process the cohort of the BQ
      if (businessQuery.getCohort() != null) {
         pCohort = processBusinessQuery(businessQuery.getCohort());
      }
      // Process the Top Bottom of the BQ
      if (businessQuery.getTopBottom() != null) {
         BusinessLimitClause topBottom = businessQuery.getTopBottom();
         pTopBottom = new PseudoLimitEntity();
         pTopBottom.setLimitEntity(PseudoLanguageServiceHelper.processPseudoEntity(topBottom.getBusinessTerm(),
                  normalizedPseudoQuery));
         pTopBottom.setLimitType(topBottom.getLimitType());
         pTopBottom.setLimitValue(topBottom.getLimitValue());
      }
      normalizedPseudoQuery.setMetrics(pMetrics);
      normalizedPseudoQuery.setPopulation(pPopulation);
      normalizedPseudoQuery.setConditions(pConditions);
      normalizedPseudoQuery.setSummarizations(pSummarizations);
      normalizedPseudoQuery.setOrderClauses(pOrderClauses);
      normalizedPseudoQuery.setHavingClauses(pHavingClauses);
      normalizedPseudoQuery.setCohortClause(pCohort);
      normalizedPseudoQuery.setTopBottom(pTopBottom);
      processMetrics(normalizedPseudoQuery);
      return normalizedPseudoQuery;
   }

   private List<PseudoEntity> processPopulation (List<BusinessTerm> populations,
            NormalizedPseudoQuery normalizedPseudoQuery) {
      return processEntities(populations, null, normalizedPseudoQuery);
   }

   // Measures in metrics followed by summarizations where univariant = value
   private void processMetricsForTitle (NormalizedPseudoQuery normalizedPseudoQuery) {
      List<PseudoEntity> newMetrics = new ArrayList<PseudoEntity>();
      List<PseudoEntity> metrics = normalizedPseudoQuery.getMetrics();
      List<PseudoEntity> summarizations = normalizedPseudoQuery.getSummarizations();
      if (ExecueCoreUtil.isCollectionEmpty(metrics)) {
         metrics = new ArrayList<PseudoEntity>();
      }
      if (ExecueCoreUtil.isCollectionEmpty(summarizations)) {
         summarizations = new ArrayList<PseudoEntity>();
      }
      for (PseudoEntity metric : metrics) {
         boolean isDuplicate = false;
         for (PseudoEntity summarization : summarizations) {
            if (summarization.getId() == metric.getId()) { // summarization means dimension - remove them
               isDuplicate = true;
            }
         }
         if (!isDuplicate) {
            newMetrics.add(metric);
         }
         normalizedPseudoQuery.setMetrics(newMetrics);
      }
   }

   /**
    * Analyze the metrics to drop the dimensions in it which are not requested by user
    */
   private void processMetrics (NormalizedPseudoQuery normalizedPseudoQuery) {
      List<PseudoEntity> newMetrics = new ArrayList<PseudoEntity>();
      List<PseudoEntity> metrics = normalizedPseudoQuery.getMetrics();
      List<PseudoEntity> summarizations = normalizedPseudoQuery.getSummarizations();
      if (ExecueCoreUtil.isCollectionEmpty(metrics)) {
         metrics = new ArrayList<PseudoEntity>();
      }
      if (ExecueCoreUtil.isCollectionEmpty(summarizations)) {
         summarizations = new ArrayList<PseudoEntity>();
      }
      for (PseudoEntity metric : metrics) {
         boolean isDuplicate = false;
         for (PseudoEntity summarization : summarizations) {
            if (summarization.getId() == metric.getId() && !metric.isFromUser()
                     && isDimensionTypeNature(summarization.getKdxType())) {
               isDuplicate = true;
            }
         }
         if (!isDuplicate) {
            newMetrics.add(metric);
         }
         normalizedPseudoQuery.setMetrics(newMetrics);
      }
   }

   private boolean isDimensionTypeNature (ColumnType columnType) {
      return ColumnType.DIMENSION.equals(columnType) || ColumnType.SIMPLE_LOOKUP.equals(columnType)
               || ColumnType.RANGE_LOOKUP.equals(columnType) || ColumnType.SIMPLE_HIERARCHY_LOOKUP.equals(columnType);
   }

   private List<PseudoEntity> processMetrics (List<BusinessTerm> entities, NormalizedPseudoQuery normalizedPseudoQuery) {
      List<PseudoEntity> pEntities = null;
      // Process the metrics of the BQ
      for (BusinessTerm businessTerm : entities) {
         // if (businessTerm.isRequestedByUser()) {
         PseudoEntity pseudoEntity = PseudoLanguageServiceHelper.processPseudoEntity(businessTerm,
                  normalizedPseudoQuery);
         if (pEntities == null) {
            pEntities = new ArrayList<PseudoEntity>();
            pEntities.add(pseudoEntity);
         } else {
            // check if the entity has already been added to avoid duplicates
            boolean isDuplicate = false;
            for (PseudoEntity entity : pEntities) {
               if (pseudoEntity.getId() == entity.getId()) {
                  isDuplicate = true;
               }
            }
            if (!isDuplicate) {
               pEntities.add(pseudoEntity);
            }
         }
         // }
      }
      return pEntities;
   }

   private List<PseudoEntity> processEntities (List<BusinessTerm> entities, List<ColumnType> columnTypes,
            NormalizedPseudoQuery normalizedPseudoQuery) {
      List<PseudoEntity> pseudoEntities = generatePseudoEntities(entities, columnTypes, normalizedPseudoQuery);
      return processEntitiesForDuplicates(pseudoEntities, normalizedPseudoQuery);
   }

   private List<PseudoEntity> generatePseudoEntities (List<BusinessTerm> entities, List<ColumnType> columnTypes,
            NormalizedPseudoQuery normalizedPseudoQuery) {
      List<PseudoEntity> pseudoEntities = new ArrayList<PseudoEntity>();
      int entityIndex = 0;
      for (BusinessTerm businessTerm : entities) {
         PseudoEntity pseudoEntity = PseudoLanguageServiceHelper.processPseudoEntity(businessTerm,
                  normalizedPseudoQuery);
         if (pseudoEntity != null) {
            pseudoEntities.add(pseudoEntity);
            if (columnTypes != null) {
               pseudoEntity.setKdxType(columnTypes.get(entityIndex));
            }
         }
         entityIndex++;
      }
      return pseudoEntities;
   }

   private List<PseudoEntity> processEntitiesForDuplicates (List<PseudoEntity> entities,
            NormalizedPseudoQuery normalizedPseudoQuery) {
      List<PseudoEntity> pEntities = new ArrayList<PseudoEntity>();
      // Process the metrics of the BQ
      for (PseudoEntity pseudoEntity : entities) {
         // check if the entity has already been added to avoid duplicates
         boolean isDuplicate = false;
         for (PseudoEntity entity : pEntities) {
            if (pseudoEntity.getId() == entity.getId() && pseudoEntity.isFromCohort() == entity.isFromCohort()) {
               isDuplicate = true;
               break;
            }
         }
         if (!isDuplicate) {
            pEntities.add(pseudoEntity);
         }
      }
      return pEntities;
   }

   private PseudoConditionEntity processPseudoConditionEntity (BusinessCondition businessCondition,
            NormalizedPseudoQuery normalizedPseudoQuery) {
      StringBuilder conditionConversion = new StringBuilder();
      ConversionType conversionType = null;
      BusinessTerm lhsBusinessTerm = businessCondition.getLhsBusinessTerm();
      PseudoStat pseudoStat = PseudoLanguageServiceHelper.getPseudoStat(lhsBusinessTerm);
      PseudoConditionEntity pseudoConditionEntity = new PseudoConditionEntity();
      pseudoConditionEntity.setLhsEntity(PseudoLanguageServiceHelper.processPseudoEntity(lhsBusinessTerm,
               normalizedPseudoQuery));
      pseudoConditionEntity.setPseudoStat(pseudoStat);
      pseudoConditionEntity.setOperator(businessCondition.getOperator());
      pseudoConditionEntity.setOperandType(businessCondition.getOperandType());
      // process conversion id of the condition
      if (businessCondition.getConversionId() != null) {
         Conversion conversion = getConversion(businessCondition.getConversionId());
         if (conversion != null) {
            conditionConversion.append(getConversionStatement(conversion));
            conversionType = conversion.getType();
         }
      }
      if (businessCondition.getRhsBusinessQuery() != null) {
         pseudoConditionEntity.setPseudoQuery(getPseudoQuery(businessCondition.getRhsBusinessQuery()));
      }
      // process the RHS of the condition
      List<String> values = new ArrayList<String>();
      if (OperandType.BUSINESS_TERM.equals(businessCondition.getOperandType())) {
         for (BusinessTerm rhsBusinessTerm : businessCondition.getRhsBusinessTerms()) {
            values.add(PseudoLanguageServiceHelper.getDomainEntityName(rhsBusinessTerm));
         }
      } else if (OperandType.VALUE.equals(businessCondition.getOperandType())) {
         StringBuilder convStmt = null;
         String finalValue = "";
         for (QueryValue value : businessCondition.getRhsValues()) {
            finalValue = value.getValue();
            convStmt = new StringBuilder();
            if (value.getNumberConversionId() != null) {
               Conversion conversion = getConversion(value.getNumberConversionId());
               if (ConversionType.CURRENCY.equals(conversionType)) {
                  convStmt.append(getConversionStatement(conversion)).append(OF_WITH_SPACES)
                           .append(conditionConversion);
               } else if (ConversionType.DISTANCE.equals(conversionType)) {
                  convStmt.append(getConversionStatement(conversion)).append(OF_WITH_SPACES)
                           .append(conditionConversion);
               }
               // handle DATE format
               if (ConversionType.DATE.equals(conversion.getType())) {
                  convStmt.append(getConversionStatement(conversion));
               }
               finalValue += SPACE + convStmt.toString();
            } else {
               if (ExecueCoreUtil.isNotEmpty(conditionConversion.toString())) {
                  finalValue += conditionConversion.toString();
               }
            }
            values.add(finalValue);
         }
      }
      pseudoConditionEntity.setValues(values);
      return pseudoConditionEntity;
   }

   private Conversion getConversion (Long conversionId) {
      Conversion conversion = null;
      try {
         conversion = conversionService.getConversionById(conversionId);
      } catch (SWIException e) {
         e.printStackTrace();
      }
      return conversion;
   }

   private String getConversionStatement (Conversion conversion) {
      StringBuilder statement = new StringBuilder();
      ConversionType cType = conversion.getType();
      switch (cType) {
         case CURRENCY:
            statement.append(conversion.getUnitDisplay());
            break;
         case DATE:
            statement.append(OPEN_BRACKET).append(conversion.getFormat()).append(CLOSE_BRACKET);
            break;
         case NUMBER:
            statement.append(conversion.getUnitDisplay());
            break;
         case DISTANCE:
            statement.append(conversion.getUnitDisplay());
            break;
         case TEMPERATURE:
            statement.append(conversion.getUnitDisplay());
            break;
         case DEFAULT:
            statement.append(conversion.getUnitDisplay());
            break;
      }
      return statement.toString();
   }

   private String statementForConditions (NormalizedPseudoQuery pseudoQuery) {
      StringBuffer sbConditionsStatement = new StringBuffer();
      int index = 0;
      if (ExecueCoreUtil.isCollectionNotEmpty(pseudoQuery.getConditions())) {
         for (PseudoConditionEntity condition : pseudoQuery.getConditions()) {
            if (index++ > 0) {
               sbConditionsStatement.append(AND_WITH_SPACES);
            }
            if (condition.isSubCondition()) {
               StringBuilder stringBuilder = new StringBuilder();
               int subIndex = 0;
               for (PseudoConditionEntity subConditionEntity : condition.getSubConditions()) {
                  if (subIndex++ > 0) {
                     stringBuilder.append(OR_WITH_SPACES);
                  }
                  stringBuilder.append(prepareConditionEntity(subConditionEntity, pseudoQuery));
               }
               sbConditionsStatement.append(stringBuilder.toString());
            } else {
               sbConditionsStatement.append(prepareConditionEntity(condition, pseudoQuery));
            }
         }
      }
      return sbConditionsStatement.toString();
   }

   private String prepareConditionEntity (PseudoConditionEntity condition, NormalizedPseudoQuery pseudoQuery) {
      StringBuffer sbConditionsStatement = new StringBuffer();
      OperandType operandType = condition.getOperandType();
      if (OperandType.VALUE.equals(operandType)) {
         if (condition.isSubCondition()) {
            // Ignoring the stats
            sbConditionsStatement.append(prepareRHSForINOperator(condition));
         } else {
            String lhsStat = "";
            PseudoStat stat = condition.getPseudoStat();
            lhsStat = stat == null ? "" : stat.getStatDescription() + OF_WITH_SPACES;
            sbConditionsStatement.append(lhsStat).append(condition.getLhsEntity().getEntityDescription()).append(SPACE);
            sbConditionsStatement.append(statementForOperatorAndRHS(condition));
         }
      } else if (OperandType.BUSINESS_QUERY.equals(operandType)) {
         sbConditionsStatement.append(condition.getLhsEntity().getEntityDescription()).append(SPACE);
         // currently handling only IN
         OperatorType operatorType = condition.getOperator();
         if (OperatorType.IN.equals(operatorType)) {
            sbConditionsStatement.append(IN);
            sbConditionsStatement.append(SPACE).append(OPEN_BRACKET);
            sbConditionsStatement.append(getPseudoLanguageStatement(pseudoQuery));
            sbConditionsStatement.append(CLOSE_BRACKET);
         }
      } else if (OperandType.BUSINESS_TERM.equals(operandType)) {
         sbConditionsStatement.append(condition.getLhsEntity().getEntityDescription()).append(SPACE);
         sbConditionsStatement.append(statementForOperatorAndRHS(condition));
      }
      return sbConditionsStatement.toString();
   }

   private String prepareRHSForINOperator (PseudoConditionEntity condition) {
      List<String> values = condition.getValues();
      StringBuilder sbRHSAndINOperator = new StringBuilder();
      String lhsString = condition.getLhsEntity().getEntityDescription() + SPACE;
      int noOfValues = values.size();
      for (int index = 0; index < noOfValues; index = (index + 2)) {
         if (index != 0) {
            sbRHSAndINOperator.append(" OR ");
         }
         sbRHSAndINOperator.append(OPEN_BRACKET).append(lhsString).append(SPACE).append(BETWEEN).append(SPACE);
         sbRHSAndINOperator.append(values.get(index)).append(AND_WITH_SPACES).append(values.get(index + 1)).append(
                  CLOSE_BRACKET);
      }
      return sbRHSAndINOperator.toString();
   }

   private String statementForHaving (NormalizedPseudoQuery pseudoQuery) {
      StringBuffer sbHavingStatement = new StringBuffer();
      int index = 0;
      if (ExecueCoreUtil.isCollectionNotEmpty(pseudoQuery.getHavingClauses())) {
         for (PseudoConditionEntity havingClause : pseudoQuery.getHavingClauses()) {
            if (index++ > 0) {
               sbHavingStatement.append(AND_WITH_SPACES);
            }
            if (havingClause.isSubCondition()) {
               StringBuilder stringBuilder = new StringBuilder();
               int subIndex = 0;
               for (PseudoConditionEntity havingSubConditionEntity : havingClause.getSubConditions()) {
                  if (subIndex++ > 0) {
                     stringBuilder.append(OR_WITH_SPACES);
                  }
                  stringBuilder.append(prepareConditionEntity(havingSubConditionEntity, pseudoQuery));
               }
               sbHavingStatement.append(stringBuilder.toString());
            } else {
               sbHavingStatement.append(prepareConditionEntity(havingClause, pseudoQuery));
            }
         }
      }
      return sbHavingStatement.toString();
   }

   private String statementForOperatorAndRHS (PseudoConditionEntity condition) {
      StringBuffer sbOperatorAndRHS = new StringBuffer();
      List<PseudoEntity> entities = condition.getEntities();
      boolean isProfilePresent = false;
      // check for instance profile
      if (ExecueCoreUtil.isCollectionNotEmpty(entities)) {
         for (PseudoEntity entity : entities) {
            if (entity.isPartOfProfile()) {
               isProfilePresent = true;
               break;
            }
         }
      }

      if (isProfilePresent) {
         OperatorType operatorType = condition.getOperator();
         if (OperatorType.EQUALS.equals(operatorType) || OperatorType.IN.equals(operatorType)) {
            // build a map of the profile ids and the list of the components
            Map<String, List<PseudoEntity>> profileMap = new HashMap<String, List<PseudoEntity>>();
            for (PseudoEntity entity : entities) {
               if (entity.isPartOfProfile()) {
                  List<PseudoEntity> profileEntities = null;
                  if (profileMap.containsKey(entity.getProfileName())) {
                     profileEntities = profileMap.get(entity.getProfileName());
                     profileEntities.add(entity);
                  } else {
                     profileEntities = new ArrayList<PseudoEntity>();
                     profileEntities.add(entity);
                     profileMap.put(entity.getProfileName(), profileEntities);
                  }
               }
            }
            // use the profile names to build the pseudo statement
            sbOperatorAndRHS.append(IN);
            sbOperatorAndRHS.append(SPACE).append(OPEN_BRACKET);
            int index = 0;
            for (String profile : profileMap.keySet()) {
               if (index++ > 0) {
                  sbOperatorAndRHS.append(COMMA);
                  sbOperatorAndRHS.append(SPACE);
               }
               sbOperatorAndRHS.append(profile);
            }
            // check if there are any entities outside the profiles and add them to the statement
            index = 0;
            for (PseudoEntity entity : entities) {
               if (!entity.isPartOfProfile()) {
                  sbOperatorAndRHS.append(COMMA);
                  sbOperatorAndRHS.append(SPACE);
                  sbOperatorAndRHS.append(entity.getEntityDescription());
               }
            }
            sbOperatorAndRHS.append(CLOSE_BRACKET);
         } else { // for other operators proceed normally
            sbOperatorAndRHS.append(prepareOperatorAndRHSString(condition));
         }
      } else {
         sbOperatorAndRHS.append(prepareOperatorAndRHSString(condition));
      }
      return sbOperatorAndRHS.toString();
   }

   private String prepareOperatorAndRHSString (PseudoConditionEntity condition) {
      StringBuffer sbOperatorAndRHS = new StringBuffer();
      OperatorType operatorType = condition.getOperator();
      List<String> values = new ArrayList<String>();
      if (ExecueCoreUtil.isCollectionNotEmpty(condition.getValues())) {
         List<String> unformattedValues = condition.getValues();
         // Added by John Mallavalli on 05-JUL-2011 to get the proper display strings of time frame values
         for (String unformattedValue : unformattedValues) {
            String value = unformattedValue;
            try {
               if (condition.isTimeFrame() && OperandType.VALUE.equals(condition.getOperandType())) {
                  value = TimeFrameUtility.getFormattedTimeFrameValueOfString(condition.getDateQualifier(), condition
                           .getDateFormat(), unformattedValue);
               }
            } catch (ParseException e) {
               // do nothing
            }
            values.add(value);
         }
      } else if (ExecueCoreUtil.isCollectionNotEmpty(condition.getEntities())) {
         for (PseudoEntity entity : condition.getEntities()) {
            values.add(entity.getEntityDescription());
         }
      }
      switch (operatorType) {
         case BETWEEN:
            sbOperatorAndRHS.append(BETWEEN);
            sbOperatorAndRHS.append(SPACE).append(values.get(0));
            sbOperatorAndRHS.append(AND_WITH_SPACES);
            sbOperatorAndRHS.append(values.get(1));
            break;
         case EQUALS:
            sbOperatorAndRHS.append(EQUALS);
            sbOperatorAndRHS.append(SPACE).append(values.get(0));
            break;
         case NOT_EQUALS:
            sbOperatorAndRHS.append(NOT_EQUALS);
            sbOperatorAndRHS.append(SPACE).append(values.get(0));
            break;
         case GREATER_THAN:
            sbOperatorAndRHS.append(GREATER_THAN);
            sbOperatorAndRHS.append(SPACE).append(values.get(0));
            break;
         case GREATER_THAN_EQUALS:
            sbOperatorAndRHS.append(GREATER_THAN_EQUALS);
            sbOperatorAndRHS.append(SPACE).append(values.get(0));
            break;
         case LESS_THAN:
            sbOperatorAndRHS.append(LESS_THAN);
            sbOperatorAndRHS.append(SPACE).append(values.get(0));
            break;
         case LESS_THAN_EQUALS:
            sbOperatorAndRHS.append(LESS_THAN_EQUALS);
            sbOperatorAndRHS.append(SPACE).append(values.get(0));
            break;
         case IN:
            sbOperatorAndRHS.append(IN);
            sbOperatorAndRHS.append(SPACE).append(OPEN_BRACKET);
            int index = 0;
            for (String value : values) {
               if (index++ > 0) {
                  sbOperatorAndRHS.append(COMMA).append(SPACE);
               }
               sbOperatorAndRHS.append(value);
            }
            sbOperatorAndRHS.append(CLOSE_BRACKET);
            break;
         case IS_NULL:
            sbOperatorAndRHS.append(IS_NULL);
            break;
         case IS_NOT_NULL:
            sbOperatorAndRHS.append(IS_NOT_NULL);
            break;
         default:
            break;
      }
      return sbOperatorAndRHS.toString();
   }

   /**
    * Generates the NormalizedPseudoQuery out of the user query wrapped in the QueryForm object
    * 
    * @param queryForm
    * @return
    */
   public NormalizedPseudoQuery getPseudoQuery (QueryForm queryForm) {
      NormalizedPseudoQuery normalizedPseudoQuery = new NormalizedPseudoQuery();
      List<PseudoEntity> pMetrics = null;
      List<PseudoConditionEntity> pConditions = null;
      List<PseudoEntity> pSummarizations = null;
      List<PseudoEntity> pOrderClauses = null;
      NormalizedPseudoQuery pCohortPseudoQuery = null;

      pMetrics = new ArrayList<PseudoEntity>();
      if (ExecueCoreUtil.isCollectionNotEmpty(queryForm.getSelects())) {
         for (QISelect qiSelect : queryForm.getSelects()) {
            PseudoEntity pEntity = processQIBusinessTerm(qiSelect.getTerm(), false);
            pEntity.setFromUser(true);
            if (ExecueCoreUtil.isNotEmpty(qiSelect.getStat())) {
               List<PseudoStat> pStats = new ArrayList<PseudoStat>();
               pStats.add(createPseudoStat(qiSelect.getStatDisplayName()));
               pEntity.setStats(pStats);
            }
            pMetrics.add(pEntity);
         }
      }

      if (ExecueCoreUtil.isCollectionNotEmpty(queryForm.getConditions())) {
         for (QICondition qiCondition : queryForm.getConditions()) {
            if (pConditions == null) {
               pConditions = new ArrayList<PseudoConditionEntity>();
            }
            pConditions.add(processQIConditionEntity(qiCondition, false));
         }
      }

      if (ExecueCoreUtil.isCollectionNotEmpty(queryForm.getSummarizations())) {
         pSummarizations = new ArrayList<PseudoEntity>();
         for (QIBusinessTerm qiBusinessTerm : queryForm.getSummarizations()) {
            pSummarizations.add(processQIBusinessTerm(qiBusinessTerm, false));
         }
      }

      if (ExecueCoreUtil.isCollectionNotEmpty(queryForm.getOrderBys())) {
         pOrderClauses = processQIOrderEntities(queryForm.getOrderBys());
      }

      if (queryForm.getCohort() != null) {
         pCohortPseudoQuery = preparePseudoQueryForSubQuery(queryForm.getCohort(), true);
      }

      normalizedPseudoQuery.setMetrics(pMetrics);
      normalizedPseudoQuery.setConditions(pConditions);
      normalizedPseudoQuery.setSummarizations(pSummarizations);
      normalizedPseudoQuery.setOrderClauses(pOrderClauses);
      normalizedPseudoQuery.setCohortClause(pCohortPseudoQuery);
      return normalizedPseudoQuery;
   }

   /*
    * This method converts the list of QIOrderBy objects into list of PseuodEntity objects
    */
   private List<PseudoEntity> processQIOrderEntities (List<QIOrderBy> orderBys) {
      List<PseudoEntity> pOrderBys = new ArrayList<PseudoEntity>();
      for (QIOrderBy orderBy : orderBys) {
         PseudoEntity entity = new PseudoEntity();
         entity.setEntityDescription(orderBy.getTerm().getDisplayName());
         entity.setFromUser(true);
         entity.setOrderType(orderBy.getType());
         pOrderBys.add(entity);
      }
      return pOrderBys;
   }

   private PseudoConditionEntity processQIConditionEntity (QICondition qiCondition, boolean fromCohort) {
      StringBuilder conditionConversion = new StringBuilder();
      ConversionType conversionType = null;
      PseudoConditionEntity pConditionEntity = new PseudoConditionEntity();
      QIConditionLHS qiConditionLHS = qiCondition.getLhsBusinessTerm();
      pConditionEntity.setLhsEntity(prepareLHSEntity(qiConditionLHS, fromCohort));
      pConditionEntity.setOperator(qiCondition.getOperator());
      // process conversion id of the condition
      if (qiCondition.getQiConversion() != null) {
         Conversion conversion = getConversion(qiCondition.getQiConversion().getConversionId());
         if (conversion != null) {
            conditionConversion.append(getConversionStatement(conversion));
            conversionType = conversion.getType();
         }
      }
      // check for the RHS values and set the operand
      QIConditionRHS qiConditionRHS = qiCondition.getRhsValue();
      if (qiConditionRHS.getQuery() != null) {
         pConditionEntity.setOperandType(OperandType.BUSINESS_QUERY);
         pConditionEntity.setPseudoQuery(preparePseudoQueryForSubQuery(qiConditionRHS.getQuery(), false));
      } else if (ExecueCoreUtil.isCollectionNotEmpty(qiConditionRHS.getTerms())) {
         pConditionEntity.setOperandType(OperandType.BUSINESS_TERM);
         List<PseudoEntity> pseudoEntities = new ArrayList<PseudoEntity>();
         for (QIBusinessTerm qiBusinessTerm : qiConditionRHS.getTerms()) {
            PseudoEntity entity = processQIBusinessTerm(qiBusinessTerm, fromCohort);
            pseudoEntities.add(entity);
         }
         pConditionEntity.setEntities(pseudoEntities);
      } else if (ExecueCoreUtil.isCollectionNotEmpty(qiConditionRHS.getValues())) {
         pConditionEntity.setOperandType(OperandType.VALUE);
         List<String> values = new ArrayList<String>();
         StringBuilder convStmt = null;
         String finalValue = "";
         for (QIValue value : qiConditionRHS.getValues()) {
            finalValue = value.getValue();
            convStmt = new StringBuilder();
            if (value.getQiConversion() != null && value.getQiConversion().getConversionId() != null) {
               Conversion conversion = getConversion(value.getQiConversion().getConversionId());
               if (ConversionType.CURRENCY.equals(conversionType)) {
                  convStmt.append(getConversionStatement(conversion)).append(OF_WITH_SPACES)
                           .append(conditionConversion);
               } else if (ConversionType.DISTANCE.equals(conversionType)) {
                  convStmt.append(getConversionStatement(conversion)).append(OF_WITH_SPACES)
                           .append(conditionConversion);
               }
               // handle DATE format
               if (ConversionType.DATE.equals(conversion.getType())) {
                  convStmt.append(getConversionStatement(conversion));
               }
               finalValue += SPACE + convStmt.toString();
            } else {
               if (ExecueCoreUtil.isNotEmpty(conditionConversion.toString())) {
                  finalValue += conditionConversion.toString();
               }
            }
            values.add(finalValue);
         }
         pConditionEntity.setValues(values);
      }
      return pConditionEntity;
   }

   private NormalizedPseudoQuery preparePseudoQueryForSubQuery (QISubQuery qiSubQuery, boolean fromCohort) {
      NormalizedPseudoQuery subPseudoQuery = new NormalizedPseudoQuery();
      List<PseudoConditionEntity> pConditions = null;
      List<PseudoEntity> pSummarizations = null;

      if (ExecueCoreUtil.isCollectionNotEmpty(qiSubQuery.getConditions())) {
         for (QICondition qiCondition : qiSubQuery.getConditions()) {
            if (pConditions == null) {
               pConditions = new ArrayList<PseudoConditionEntity>();
            }
            pConditions.add(processQIConditionEntity(qiCondition, fromCohort));
         }
      }
      if (ExecueCoreUtil.isCollectionNotEmpty(qiSubQuery.getSummarizations())) {
         pSummarizations = new ArrayList<PseudoEntity>();
         for (QIBusinessTerm qiBusinessTerm : qiSubQuery.getSummarizations()) {
            pSummarizations.add(processQIBusinessTerm(qiBusinessTerm, fromCohort));
         }
      }
      subPseudoQuery.setConditions(pConditions);
      subPseudoQuery.setSummarizations(pSummarizations);
      return subPseudoQuery;
   }

   private PseudoEntity prepareLHSEntity (QIConditionLHS qiConditionLHS, boolean fromCohort) {
      PseudoEntity lhsEntity = new PseudoEntity();
      lhsEntity.setEntityDescription(qiConditionLHS.getTerm().getDisplayName());
      lhsEntity.setFromCohort(fromCohort);
      lhsEntity.setFromUser(true);
      PseudoStat pStat = createPseudoStat(qiConditionLHS.getStatDisplayName());
      pStat.setStatFromUser(true);
      List<PseudoStat> pStats = new ArrayList<PseudoStat>();
      pStats.add(pStat);
      lhsEntity.setStats(pStats);
      return lhsEntity;
   }

   private PseudoStat createPseudoStat (String statDescription) {
      PseudoStat pStat = new PseudoStat();
      pStat.setStatDescription(statDescription);
      return pStat;
   }

   private PseudoEntity processQIBusinessTerm (QIBusinessTerm qiBusinessTerm, boolean fromCohort) {
      PseudoEntity pEntity = new PseudoEntity();
      pEntity.setEntityDescription(qiBusinessTerm.getDisplayName());
      pEntity.setFromCohort(fromCohort);
      pEntity.setFromUser(true);
      return pEntity;
   }

   /**
    * Generates the NormalizedPseudoQuery object from the StructuredQuery
    * 
    * @param structuredQuery
    * @return NormalizedPseudoQuery
    */
   public NormalizedPseudoQuery getPseudoQuery (StructuredQuery structuredQuery) {
      return processStructuredQuery(structuredQuery);
   }

   private NormalizedPseudoQuery processStructuredQuery (StructuredQuery structuredQuery) {
      // Initialize a new pseudo query object
      NormalizedPseudoQuery normalizedPseudoQuery = new NormalizedPseudoQuery();
      // Initialize the pseudo entities map
      Map<Long, PseudoEntity> pseudoEntities = new HashMap<Long, PseudoEntity>();
      // Set the entities map into the pseudo query object
      normalizedPseudoQuery.setPseudoEntities(pseudoEntities);

      List<PseudoEntity> pMetrics = null;
      List<PseudoConditionEntity> pConditions = null;
      List<PseudoEntity> pSummarizations = null;
      List<PseudoEntity> pOrderClauses = null;
      List<PseudoConditionEntity> pHavingClauses = null;
      NormalizedPseudoQuery pCohort = null;
      PseudoLimitEntity pTopBottom = null;

      pMetrics = processMetrics(normalizedPseudoQuery, structuredQuery.getMetrics());

      if (ExecueCoreUtil.isCollectionNotEmpty(structuredQuery.getConditions())) {
         List<StructuredCondition> conditions = structuredQuery.getConditions();
         for (StructuredCondition structuredCondition : conditions) {
            if (pConditions == null) {
               pConditions = new ArrayList<PseudoConditionEntity>();
            }
            if (structuredCondition.isSubCondition()) {
               List<PseudoConditionEntity> pseudoConditionEntities = new ArrayList<PseudoConditionEntity>();
               for (StructuredCondition subCondition : structuredCondition.getSubConditions()) {
                  pseudoConditionEntities.add(processPseudoConditionEntity(subCondition, normalizedPseudoQuery,
                           structuredQuery.getModelId()));
               }
               PseudoConditionEntity pseudoConditionEntity = new PseudoConditionEntity();
               pseudoConditionEntity.setSubConditions(pseudoConditionEntities);
               pseudoConditionEntity.setSubCondition(true);
               pConditions.add(pseudoConditionEntity);
            } else {
               pConditions.add(processPseudoConditionEntity(structuredCondition, normalizedPseudoQuery, structuredQuery
                        .getModelId()));
            }
         }
      }
      if (ExecueCoreUtil.isCollectionNotEmpty(structuredQuery.getSummarizations())) {
         List<BusinessTerm> summarizations = new ArrayList<BusinessTerm>();
         List<ColumnType> columnTypes = new ArrayList<ColumnType>();
         for (BusinessAssetTerm batSummarization : structuredQuery.getSummarizations()) {
            if (AssetEntityType.COLUMN.equals(batSummarization.getAssetEntityTerm().getAssetEntityType())) {
               Colum colum = (Colum) batSummarization.getAssetEntityTerm().getAssetEntity();
               columnTypes.add(colum.getKdxDataType());
            }
            summarizations.add(batSummarization.getBusinessTerm());
         }
         pSummarizations = processEntities(summarizations, columnTypes, normalizedPseudoQuery);
      }
      if (ExecueCoreUtil.isCollectionNotEmpty(structuredQuery.getOrderClauses())) {
         List<BusinessTerm> orderClauses = new ArrayList<BusinessTerm>();
         List<ColumnType> columnTypes = new ArrayList<ColumnType>();
         for (StructuredOrderClause batOrderClause : structuredQuery.getOrderClauses()) {
            if (AssetEntityType.COLUMN.equals(batOrderClause.getBusinessAssetTerm().getAssetEntityTerm()
                     .getAssetEntityType())) {
               Colum colum = (Colum) batOrderClause.getBusinessAssetTerm().getAssetEntityTerm().getAssetEntity();
               columnTypes.add(colum.getKdxDataType());
            }
            orderClauses.add(batOrderClause.getBusinessAssetTerm().getBusinessTerm());
         }
         pOrderClauses = processEntities(orderClauses, columnTypes, normalizedPseudoQuery);
      }
      if (ExecueCoreUtil.isCollectionNotEmpty(structuredQuery.getHavingClauses())) {
         for (StructuredCondition structuredCondition : structuredQuery.getHavingClauses()) {
            if (pHavingClauses == null) {
               pHavingClauses = new ArrayList<PseudoConditionEntity>();
            }
            pHavingClauses.add(processPseudoConditionEntity(structuredCondition, normalizedPseudoQuery, structuredQuery
                     .getModelId()));
         }
      }
      if (structuredQuery.getCohort() != null) {
         pCohort = processStructuredQuery(structuredQuery.getCohort());
      }
      if (structuredQuery.getTopBottom() != null) {
         StructuredLimitClause topBottom = structuredQuery.getTopBottom();
         pTopBottom = new PseudoLimitEntity();
         pTopBottom.setLimitEntity(PseudoLanguageServiceHelper.processPseudoEntity(topBottom.getBusinessAssetTerm()
                  .getBusinessTerm(), normalizedPseudoQuery));
         pTopBottom.setLimitType(topBottom.getLimitType());
         pTopBottom.setLimitValue(topBottom.getLimitValue().toString());
      }
      normalizedPseudoQuery.setMetrics(pMetrics);
      normalizedPseudoQuery.setConditions(pConditions);
      normalizedPseudoQuery.setSummarizations(pSummarizations);
      normalizedPseudoQuery.setOrderClauses(pOrderClauses);
      normalizedPseudoQuery.setHavingClauses(pHavingClauses);
      normalizedPseudoQuery.setCohortClause(pCohort);
      normalizedPseudoQuery.setTopBottom(pTopBottom);
      processMetrics(normalizedPseudoQuery);
      if (ExecueCoreUtil.isCollectionEmpty(normalizedPseudoQuery.getMetrics())) {
         pMetrics = processMetrics(normalizedPseudoQuery, structuredQuery.getMetrics());
         normalizedPseudoQuery.setMetrics(pMetrics);
      }
      return normalizedPseudoQuery;
   }

   private List<PseudoEntity> processMetrics (NormalizedPseudoQuery normalizedPseudoQuery,
            List<BusinessAssetTerm> sqMetrics) {
      List<PseudoEntity> pMetrics = new ArrayList<PseudoEntity>();
      if (ExecueCoreUtil.isCollectionNotEmpty(sqMetrics)) {
         List<BusinessTerm> metrics = new ArrayList<BusinessTerm>();
         List<ColumnType> columnTypes = new ArrayList<ColumnType>();
         for (BusinessAssetTerm metric : sqMetrics) {
            if (AssetEntityType.COLUMN.equals(metric.getAssetEntityTerm().getAssetEntityType())) {
               Colum colum = (Colum) metric.getAssetEntityTerm().getAssetEntity();
               columnTypes.add(colum.getKdxDataType());
            }
            metrics.add(metric.getBusinessTerm());
         }
         pMetrics = processEntities(metrics, columnTypes, normalizedPseudoQuery);
      }
      return pMetrics;
   }

   private PseudoConditionEntity processPseudoConditionEntity (StructuredCondition structuredCondition,
            NormalizedPseudoQuery normalizedPseudoQuery, Long modelId) {
      BusinessTerm lhsBusinessTerm = structuredCondition.getLhsBusinessAssetTerm().getBusinessTerm();
      PseudoStat pseudoStat = PseudoLanguageServiceHelper.getPseudoStat(lhsBusinessTerm);
      PseudoConditionEntity pseudoConditionEntity = new PseudoConditionEntity();
      pseudoConditionEntity.setLhsEntity(PseudoLanguageServiceHelper.processPseudoEntity(lhsBusinessTerm,
               normalizedPseudoQuery));
      pseudoConditionEntity.setPseudoStat(pseudoStat);
      pseudoConditionEntity.setOperator(structuredCondition.getOperator());
      pseudoConditionEntity.setOperandType(structuredCondition.getOperandType());

      // Added by John Mallavalli on 05-JUL-2011 to check if the LHS BT is of TimeFrame type
      Long conceptId = ((Concept) lhsBusinessTerm.getBusinessEntityTerm().getBusinessEntity()).getId();
      try {
         boolean isLHSBusinessTermTFType = getKdxRetrievalService().isConceptMatchedBusinessEntityType(conceptId,
                  modelId, ExecueConstants.TIME_FRAME_TYPE);
         pseudoConditionEntity.setTimeFrame(isLHSBusinessTermTFType);
         if (isLHSBusinessTermTFType) {
            // get the date qualifier and the format
            String columnDataFormat = ((Colum) structuredCondition.getLhsBusinessAssetTerm().getAssetEntityTerm()
                     .getAssetEntity()).getDataFormat();
            DateQualifier dateQualifier = getSwiConfigurationService().getDateQualifier(columnDataFormat);
            pseudoConditionEntity.setDateFormat(columnDataFormat);
            pseudoConditionEntity.setDateQualifier(dateQualifier);
         }
      } catch (KDXException e) {
         e.printStackTrace();
      }

      if (structuredCondition.getRhsStructuredQuery() != null) {
         pseudoConditionEntity.setPseudoQuery(getPseudoQuery(structuredCondition.getRhsStructuredQuery()));
      }
      List<String> values = new ArrayList<String>();
      if (structuredCondition.isSubCondition()) {
         for (StructuredCondition subCondition : structuredCondition.getSubConditions()) {
            if (OperandType.BUSINESS_TERM.equals(subCondition.getOperandType())) {
               for (BusinessAssetTerm rhsBusinessAssetTerm : subCondition.getRhsBusinessAssetTerms()) {
                  values.add(PseudoLanguageServiceHelper.getDomainEntityName(rhsBusinessAssetTerm.getBusinessTerm()));
               }
            } else if (OperandType.VALUE.equals(subCondition.getOperandType())) {
               for (QueryValue value : subCondition.getRhsValues()) {
                  values.addAll(getConvertedValues(subCondition, value));
               }
            }
         }
      } else {
         if (OperandType.BUSINESS_TERM.equals(structuredCondition.getOperandType())) {
            for (BusinessAssetTerm rhsBusinessAssetTerm : structuredCondition.getRhsBusinessAssetTerms()) {
               values.add(PseudoLanguageServiceHelper.getDomainEntityName(rhsBusinessAssetTerm.getBusinessTerm()));
            }
         } else if (OperandType.VALUE.equals(structuredCondition.getOperandType())) {
            for (QueryValue value : structuredCondition.getRhsValues()) {
               values.addAll(getConvertedValues(structuredCondition, value));
            }
         }
      }
      pseudoConditionEntity.setSubCondition(structuredCondition.isSubCondition());
      pseudoConditionEntity.setValues(values);
      return pseudoConditionEntity;
   }

   // TODO: JVK need to handle other conversions - currently handling only CURRENCY and DATE
   // update ReportAggregationHelper.getConvertedValue method also (or) move the method to a common place
   private List<String> getConvertedValues (StructuredCondition structuredCondition, QueryValue value) {
      List<String> pseudoConditionValues = new ArrayList<String>();
      pseudoConditionValues.add(getConversionRelatedInfo(value.getValue(), structuredCondition, value));
      return pseudoConditionValues;
   }

   private String getConversionRelatedInfo (String rhsConditionValue, StructuredCondition structuredCondition,
            QueryValue value) {
      StringBuilder sbConversionInfo = new StringBuilder();
      // format the value to limit the decimal digits
      sbConversionInfo.append(PseudoLanguageServiceHelper.getFormattedValueForDecimals(rhsConditionValue));
      boolean hasUnit = false;
      if (ExecueCoreUtil.isNotEmpty(structuredCondition.getTargetConversionUnit())) {
         hasUnit = true;
      }
      if (ExecueCoreUtil.isNotEmpty(value.getTargetConversionFormat())) {
         if (hasUnit) {
            String unit = StringUtils.capitalize(structuredCondition.getTargetConversionUnit().toLowerCase());
            if (value.getTargetConversionFormat().toUpperCase().startsWith("ONE")) {
               sbConversionInfo.append(SPACE).append(unit);
            } else {
               String format = StringUtils.capitalize(value.getTargetConversionFormat().toLowerCase());
               sbConversionInfo.append(SPACE).append(format).append(OF_WITH_SPACES).append(unit);
            }
         } else {
            // convertedValue.append(SPACE).append(OPEN_BRACKET).append(value.getTargetConversionFormat()).append(
            // CLOSE_BRACKET);
            if (ExecueCoreUtil.isNotEmpty(value.getTargetConversionFormat())) {
               // TODO: -JVK- get the ONE from the DB instead of hard coding
               if (!value.getTargetConversionFormat().toUpperCase().startsWith("ONE")) {
                  sbConversionInfo.append(SPACE).append(value.getTargetConversionFormat());
               }
            }
         }
      }
      return sbConversionInfo.toString();
   }

   public String generateTitle (NormalizedPseudoQuery pseudoQuery) {
      // TODO: [comma separated measures] by summarization where single-variant = value
      StringBuffer sbTitle = new StringBuffer();
      StringBuffer sbMetrics = new StringBuffer();
      StringBuffer sbSummarizations = new StringBuffer();

      // Take different route for top-bottom case
      if (pseudoQuery.getTopBottom() != null) {
         PseudoLimitEntity topBottom = pseudoQuery.getTopBottom();
         String metricsStmt = null;
         String type = "First";
         if (ExecueCoreUtil.isCollectionNotEmpty(pseudoQuery.getMetrics())) {
            int index = 0;
            for (PseudoEntity entity : pseudoQuery.getMetrics()) {
               if (!entity.isFromCohort() && entity.isFromUser()) {
                  if (index++ != 0) {
                     sbMetrics.append(COMMA).append(SPACE);
                  }
                  sbMetrics.append(entity.getEntityDescription());
               }
            }
            if (index > 0) {
               metricsStmt = sbMetrics.toString();
            }
         }
         if (OrderLimitEntityType.BOTTOM.equals(topBottom.getLimitType())) {
            type = "Last";
         }
         sbTitle.append(type).append(SPACE);
         sbTitle.append(topBottom.getLimitValue());
         if (metricsStmt != null) {
            sbTitle.append(SPACE).append(metricsStmt);
         }
         sbTitle.append(OF_WITH_SPACES).append(topBottom.getLimitEntity().getEntityDescription());
      } else {
         processMetricsForTitle(pseudoQuery);
         int index = 0;
         List<PseudoEntity> metrics = pseudoQuery.getMetrics();
         if (ExecueCoreUtil.isCollectionNotEmpty(metrics)) {
            boolean isProfilePresent = false;
            // check for the presence of profiles
            for (PseudoEntity metric : metrics) {
               if (metric.isPartOfProfile() && !metric.isFromCohort()) {
                  isProfilePresent = true;
                  break;
               }
            }
            if (isProfilePresent) {
               sbMetrics.append(PseudoLanguageServiceHelper.prepareTitleForProfiles(metrics));
            } else {
               for (PseudoEntity entity : metrics) {
                  if (!entity.isFromCohort()) {
                     if (index++ != 0) {
                        sbMetrics.append(COMMA).append(SPACE);
                     }
                     sbMetrics.append(entity.getEntityDescription());
                     // TODO: -JVK- uncomment the below block if cohort terms are to be displayed in the title
                     // } else {
                     // if (index++ != 0) {
                     // sbMetrics.append(COMMA).append(SPACE);
                     // }
                     // sbMetrics.append(entity.getEntityDescription()).append(SPACE).append(COHORT_IDENTIFIER);
                  }
               }
            }
            sbTitle.append(sbMetrics);
         } else { // user requested metrics are absent, add the metrics present in the condition
            if (ExecueCoreUtil.isCollectionNotEmpty(pseudoQuery.getConditions())) {
               for (PseudoConditionEntity cEntity : pseudoQuery.getConditions()) {
                  if (index++ != 0) {
                     sbMetrics.append(COMMA).append(SPACE);
                  }
                  sbMetrics.append(cEntity.getLhsEntity().getEntityDescription());
               }
               sbTitle.append(sbMetrics);
            }
         }
         if (ExecueCoreUtil.isCollectionNotEmpty(pseudoQuery.getSummarizations())) {
            index = 0;
            for (PseudoEntity entity : pseudoQuery.getSummarizations()) {
               if (index++ != 0) {
                  sbSummarizations.append(COMMA).append(SPACE);
               }
               sbSummarizations.append(entity.getEntityDescription());
            }
            sbTitle.append(BY_WITH_SPACES).append(sbSummarizations);
         }
      }
      return sbTitle.toString();
   }
}