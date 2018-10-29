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


package com.execue.qi.processor.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.execue.core.common.bean.BusinessCondition;
import com.execue.core.common.bean.BusinessLimitClause;
import com.execue.core.common.bean.BusinessOrderClause;
import com.execue.core.common.bean.BusinessQuery;
import com.execue.core.common.bean.BusinessStat;
import com.execue.core.common.bean.BusinessTerm;
import com.execue.core.common.bean.QueryForm;
import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.ConceptProfile;
import com.execue.core.common.bean.entity.Instance;
import com.execue.core.common.bean.entity.InstanceProfile;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.common.bean.entity.NormalizedDataType;
import com.execue.core.common.bean.governor.BusinessEntityTerm;
import com.execue.core.common.bean.nlp.NormalizedCloudDataFactory;
import com.execue.core.common.bean.nlp.NormalizedDataEntity;
import com.execue.core.common.bean.nlp.TimeFrameNormalizedData;
import com.execue.core.common.bean.qi.QIBusinessTerm;
import com.execue.core.common.bean.qi.QICondition;
import com.execue.core.common.bean.qi.QIConditionLHS;
import com.execue.core.common.bean.qi.QIConditionRHS;
import com.execue.core.common.bean.qi.QIOrderBy;
import com.execue.core.common.bean.qi.QISelect;
import com.execue.core.common.bean.qi.QISubQuery;
import com.execue.core.common.bean.qi.QITopBottom;
import com.execue.core.common.bean.qi.QIValue;
import com.execue.core.common.bean.querygen.QueryValue;
import com.execue.core.common.type.BehaviorType;
import com.execue.core.common.type.BusinessEntityType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.ConversionType;
import com.execue.core.common.type.DataType;
import com.execue.core.common.type.DateQualifier;
import com.execue.core.common.type.OperandType;
import com.execue.core.common.type.OperatorType;
import com.execue.core.common.type.ProfileType;
import com.execue.core.common.type.QueryValueType;
import com.execue.core.common.type.UserInterfaceType;
import com.execue.core.constants.ExecueConstants;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.platform.IBusinessQueryOrganizationService;
import com.execue.qi.exception.QIException;
import com.execue.qi.exception.QIExceptionCodes;
import com.execue.qi.processor.IBusinessQueryGenerator;
import com.execue.swi.configuration.ISWIConfigurationService;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.PreferencesException;
import com.execue.swi.service.IBaseKDXRetrievalService;
import com.execue.swi.service.IKDXModelService;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.swi.service.IPreferencesRetrievalService;

public class BusinessQueryGenerator implements IBusinessQueryGenerator {

   private static Logger                     logger = Logger.getLogger(BusinessQueryGenerator.class);
   private IPreferencesRetrievalService      preferencesRetrievalService;
   private IKDXRetrievalService              kdxRetrievalService;
   private IKDXModelService                  kdxModelService;
   private IBaseKDXRetrievalService          baseKDXRetrievalService;
   private IBusinessQueryOrganizationService businessQueryOrganizationService;
   private ISWIConfigurationService          swiConfigurationService;

   public BusinessQuery generate (QueryForm form) throws QIException {
      Map<String, BusinessEntityTerm> entityTermMap = new HashMap<String, BusinessEntityTerm>();
      BusinessQuery query = new BusinessQuery();
      query.setUserInterfaceType(UserInterfaceType.QUERY_INTERFACE);
      try {
         Long modelId = form.getModelId();
         query.setModelId(modelId);
         Model model = kdxRetrievalService.getModelById(query.getModelId());
         // process selects
         query.setMetrics(generateSelects(form.getSelects(), entityTermMap, model));
         // process summarization
         query.setSummarizations(generateSummarizations(form.getSummarizations(), entityTermMap, model));
         // process conditions for where clause
         List<BusinessCondition> conditions = generateConditions(form.getConditions(), entityTermMap, model);
         query.setConditions(conditions);
         // process orderbys
         List<BusinessOrderClause> orderBys = generateOrderBys(form.getOrderBys(), entityTermMap, model);
         BusinessLimitClause topBottom = generateTopBottom(form.getTopBottom(), entityTermMap, model);
         query.setOrderClauses(orderBys);
         // set the top bottom clause
         query.setTopBottom(topBottom);

         // process population
         List<BusinessTerm> populatedPopulation = populatePopulation(form.getPopulations(), entityTermMap, model);
         if (ExecueCoreUtil.isCollectionEmpty(populatedPopulation)) {
            query.setPopulationDefaulted(true);
         } else {
            query.setPopulationDefaulted(false);
            query.setPopulations(populatedPopulation);
         }

         // process cohort
         query.setCohort(generateCohort(form.getCohort(), entityTermMap, model));
         // start of organizing query

         getBusinessQueryOrganizationService().organizeBusinessQuery(query);

      } catch (KDXException kdxException) {
         throw new QIException(QIExceptionCodes.QI_KDX_RETRIEVAL_EXCEPTION_CODE,
                  "Error while retrieving the Business Terms", kdxException.getCause());
      }
      return query;
   }

   private BusinessLimitClause generateTopBottom (QITopBottom topBottom, Map<String, BusinessEntityTerm> entityTermMap,
            Model model) throws QIException {
      BusinessLimitClause businessLimitClause = null;
      if (topBottom != null) {
         businessLimitClause = new BusinessLimitClause();
         BusinessTerm businessTerm = getBusinessTerm(topBottom.getTerm(), null, entityTermMap, model);
         if (businessTerm != null) {
            businessTerm.setRequestedByUser(true);
            businessLimitClause.setBusinessTerm(businessTerm);
            businessLimitClause.setLimitType(topBottom.getType());
            businessLimitClause.setLimitValue(topBottom.getValue());
         }
      }
      return businessLimitClause;
   }

   /*
    * In the current implementation we are assuming that the order by in the queries in its pure form will not occur.
    * Only the TOP/BOTTOM queries will be used.
    */
   private List<BusinessOrderClause> generateOrderBys (List<QIOrderBy> orderBys,
            Map<String, BusinessEntityTerm> entityTermMap, Model model) throws QIException {
      List<BusinessOrderClause> businessOrderClauses = new ArrayList<BusinessOrderClause>();
      if (ExecueCoreUtil.isCollectionNotEmpty(orderBys)) {
         for (QIOrderBy orderBy : orderBys) {
            BusinessTerm businessTerm = getBusinessTerm(orderBy.getTerm(), null, entityTermMap, model);
            if (businessTerm != null) {
               businessTerm.setRequestedByUser(true);
               BusinessOrderClause businessOrderClause = new BusinessOrderClause();
               businessOrderClause.setBusinessTerm(businessTerm);
               businessOrderClause.setOrderEntityType(orderBy.getType());
               businessOrderClauses.add(businessOrderClause);
            }
         }
      }
      return businessOrderClauses;
   }

   private BusinessQuery generateCohort (QISubQuery cohort, Map<String, BusinessEntityTerm> entityTermMap, Model model)
            throws QIException {
      BusinessQuery cohortQuery = null;
      if (cohort != null) {
         cohortQuery = generateSubQuery(cohort, entityTermMap, model);
      }
      return cohortQuery;
   }

   private List<BusinessTerm> populatePopulation (List<QIBusinessTerm> populations,
            Map<String, BusinessEntityTerm> entityTermMap, Model model) throws QIException {
      List<BusinessTerm> populationList = new ArrayList<BusinessTerm>();
      if (ExecueCoreUtil.isCollectionNotEmpty(populations)) {
         for (QIBusinessTerm population : populations) {
            BusinessTerm businessTerm = getBusinessTerm(population, null, entityTermMap, model);
            if (businessTerm != null) {
               businessTerm.setRequestedByUser(true);
               populationList.add(businessTerm);
            }
         }
      }
      return populationList;
   }

   private List<BusinessCondition> generateConditions (List<QICondition> conditions,
            Map<String, BusinessEntityTerm> entityTermMap, Model model) throws QIException {
      List<BusinessCondition> businessConditions = new ArrayList<BusinessCondition>();
      if (ExecueCoreUtil.isCollectionNotEmpty(conditions)) {
         // merge the conditions
         List<QICondition> mergedConditions = mergeConditions(conditions);
         for (QICondition condition : mergedConditions) {
            QIConditionLHS lhsCondtion = condition.getLhsBusinessTerm();
            QIConditionRHS rhsCondition = condition.getRhsValue();
            BusinessCondition businessCondition = new BusinessCondition();
            BusinessTerm busTermLHS = getBusinessTermAndStat(lhsCondtion.getTerm(), lhsCondtion.getStat(),
                     entityTermMap, model);
            busTermLHS.setRequestedByUser(true);
            boolean isMeasure = busTermLHS.getBusinessEntityTerm().isMeasurableEntity();
            if (busTermLHS.getBusinessStat() == null && isMeasure) {
               busTermLHS.getBusinessEntityTerm().setMeasureConditionWithoutStat(CheckType.YES);
            }

            businessCondition.setLhsBusinessTerm(busTermLHS);
            // Setting conversion ID into business condition
            if (condition.getQiConversion() != null) {
               businessCondition.setConversionId(condition.getQiConversion().getConversionId());
            }
            if (condition.getOperator() != null) {
               businessCondition.setOperator(condition.getOperator());
            } else {
               businessCondition.setOperator(OperatorType.EQUALS);
            }

            if (rhsCondition.getValues() != null) {
               List<QIValue> values = rhsCondition.getValues();
               businessCondition.setOperandType(OperandType.VALUE);
               List<QueryValue> rhsValues = new ArrayList<QueryValue>();
               boolean isTimeFrame = false;
               // if LHS is TF type
               if (ConversionType.DATE.toString().equalsIgnoreCase(lhsCondtion.getDatatype())) {
                  // populate Normalized data
                  isTimeFrame = true;
                  // normalizedData.setDateQualifier();
                  businessCondition.setNormalizedDataType(NormalizedDataType.TIME_FRAME_NORMALIZED_DATA);
               }
               for (QIValue value : values) {
                  QueryValue rhsValue = new QueryValue();
                  if (isTimeFrame) {
                     TimeFrameNormalizedData normalizedData = (TimeFrameNormalizedData) NormalizedCloudDataFactory
                              .getNormalizedCloudData(NormalizedDataType.TIME_FRAME_NORMALIZED_DATA);
                     rhsValue.setNormalizedData(getNormalizeData(normalizedData, value));
                     rhsValue.setValueType(QueryValueType.NORMALIZED_OBJECT);
                  } else {
                     try {
                        Integer.parseInt(value.getValue());
                        rhsValue.setDataType(DataType.NUMBER);
                     } catch (NumberFormatException e) {
                        rhsValue.setDataType(DataType.STRING);
                     }
                     // setting conversion Id into BusinessCondition RHS value
                     if (value.getQiConversion() != null) {
                        rhsValue.setNumberConversionId(value.getQiConversion().getConversionId());
                     }
                  }
                  rhsValue.setValue(value.getValue());
                  rhsValues.add(rhsValue);
               }
               businessCondition.setRhsValues(rhsValues);
            } else if (rhsCondition.getTerms() != null) {
               List<QIBusinessTerm> terms = rhsCondition.getTerms();
               businessCondition.setOperandType(OperandType.BUSINESS_TERM);
               List<BusinessTerm> rhsBusinessTerms = new ArrayList<BusinessTerm>();
               for (QIBusinessTerm term : terms) {
                  Concept concept = (Concept) busTermLHS.getBusinessEntityTerm().getBusinessEntity();
                  BusinessTerm businessTerm = getBusinessTerm(term, concept, entityTermMap, model);
                  if (businessTerm != null) {
                     businessTerm.setRequestedByUser(true);
                     rhsBusinessTerms.add(businessTerm);
                  }
               }
               businessCondition.setRhsBusinessTerms(rhsBusinessTerms);
            } else if (rhsCondition.getQuery() != null) {
               businessCondition.setOperandType(OperandType.BUSINESS_QUERY);
               BusinessQuery busQuery = generateSubQuery(rhsCondition.getQuery(), entityTermMap, model);
               List<BusinessTerm> selects = new ArrayList<BusinessTerm>();
               selects.add(busTermLHS);
               busQuery.setMetrics(selects);
               businessCondition.setRhsBusinessQuery(busQuery);
            }

            businessConditions.add(businessCondition);
         }
      }
      return businessConditions;
   }

   private TimeFrameNormalizedData getNormalizeData (TimeFrameNormalizedData normalizedData, QIValue qiValue)
            throws QIException {
      // This method expect time frame info in given format(quarter-month-day-hours-mins-secs-year)
      // quarter or month
      String value = qiValue.getValue();
      String[] values = value.split("-");
      if (values.length > 0) {
         normalizedData.setDateQualifier(DateQualifier.getType(qiValue.getDateQualifier()));
         normalizedData.setYear(getNormalizedDataEntity(values[values.length - 1]));
         if (DateQualifier.QUARTER.equals(normalizedData.getDateQualifier())) {
            String[] quater = values[0].split(" ");
            normalizedData.setQuarter(getNormalizedDataEntity(quater[1]));
         } else if (DateQualifier.MONTH.equals(normalizedData.getDateQualifier())) {
            normalizedData.setMonth(getNormalizedDataEntity(values[0]));
         } else if (DateQualifier.DAY.equals(normalizedData.getDateQualifier())) {
            normalizedData.setMonth(getNormalizedDataEntity(values[0]));
            normalizedData.setDay(getNormalizedDataEntity(values[1]));
         } else if (DateQualifier.HOUR.equals(normalizedData.getDateQualifier())) {
            normalizedData.setMonth(getNormalizedDataEntity(values[0]));
            normalizedData.setDay(getNormalizedDataEntity(values[1]));
            normalizedData.setHour(getNormalizedDataEntity(values[2]));
         } else if (DateQualifier.MINUTE.equals(normalizedData.getDateQualifier())) {
            normalizedData.setMonth(getNormalizedDataEntity(values[0]));
            normalizedData.setDay(getNormalizedDataEntity(values[1]));
            normalizedData.setHour(getNormalizedDataEntity(values[2]));
            normalizedData.setMinute(getNormalizedDataEntity(values[3]));
         } else if (DateQualifier.SECOND.equals(normalizedData.getDateQualifier())) {
            normalizedData.setMonth(getNormalizedDataEntity(values[0]));
            normalizedData.setDay(getNormalizedDataEntity(values[1]));
            normalizedData.setHour(getNormalizedDataEntity(values[2]));
            normalizedData.setMinute(getNormalizedDataEntity(values[3]));
            normalizedData.setSecond(getNormalizedDataEntity(values[4]));
         }
      }
      return normalizedData;
   }

   private NormalizedDataEntity getNormalizedDataEntity (String value) {
      NormalizedDataEntity normalizedDataEntity = new NormalizedDataEntity();
      normalizedDataEntity.setValue(value);
      return normalizedDataEntity;
   }

   /**
    * This method will merge the conditions based on lhs term is same or not
    * 
    * @param conditions
    * @return mergedConditions
    */
   private List<QICondition> mergeConditions (List<QICondition> conditions) {
      List<QICondition> mergedConditions = new ArrayList<QICondition>();
      // preparing the list of operator types
      List<OperatorType> equalityOperators = new ArrayList<OperatorType>();
      equalityOperators.add(OperatorType.EQUALS);
      equalityOperators.add(OperatorType.IN);
      List<OperatorType> nonEqualityOperators = new ArrayList<OperatorType>();
      nonEqualityOperators.add(OperatorType.NOT_EQUALS);
      nonEqualityOperators.add(OperatorType.NOT_IN);

      for (QICondition condition : conditions) {
         List<OperatorType> compatibleOperatorType = new ArrayList<OperatorType>();
         QICondition matchedTerm = getMatchedTerm(condition, mergedConditions, equalityOperators, nonEqualityOperators,
                  compatibleOperatorType);
         if (matchedTerm == null) {
            mergedConditions.add(condition);
         } else {
            List<QIValue> rhsValues = condition.getRhsValue().getValues();
            List<QIBusinessTerm> rhsTerms = condition.getRhsValue().getTerms();
            if (ExecueCoreUtil.isCollectionNotEmpty(rhsValues)) {
               List<QIValue> mergedValues = matchedTerm.getRhsValue().getValues();
               for (QIValue value : rhsValues) {
                  if (!isValueExist(value, mergedValues)) {
                     mergedValues.add(value);
                  }
               }
               matchedTerm.setOperator(getExactOperator(mergedValues.size(), compatibleOperatorType, equalityOperators,
                        nonEqualityOperators));
               matchedTerm.getRhsValue().setValues(mergedValues);
            } else if (ExecueCoreUtil.isCollectionNotEmpty(rhsTerms)) {
               List<QIBusinessTerm> mergedTerms = matchedTerm.getRhsValue().getTerms();
               for (QIBusinessTerm businessTerm : rhsTerms) {
                  if (!isTermExists(businessTerm, mergedTerms)) {
                     mergedTerms.add(businessTerm);
                  }
               }
               matchedTerm.setOperator(getExactOperator(mergedTerms.size(), compatibleOperatorType, equalityOperators,
                        nonEqualityOperators));
               matchedTerm.getRhsValue().setTerms(mergedTerms);
            }
         }
      }
      return mergedConditions;
   }

   /**
    * This method will return the exact operator to be used using compatible operator type
    * 
    * @param size
    * @param compatibleOperatorType
    * @param equalityOperators
    * @param nonEqualityOperators
    * @return operatorType
    */
   private OperatorType getExactOperator (int size, List<OperatorType> compatibleOperatorType,
            List<OperatorType> equalityOperators, List<OperatorType> nonEqualityOperators) {
      OperatorType operatorType = null;
      if (compatibleOperatorType.equals(equalityOperators)) {
         if (size == 1) {
            operatorType = OperatorType.EQUALS;
         } else {
            operatorType = OperatorType.IN;
         }
      } else if (compatibleOperatorType.equals(nonEqualityOperators)) {
         if (size == 1) {
            operatorType = OperatorType.NOT_EQUALS;
         } else {
            operatorType = OperatorType.NOT_IN;
         }
      }
      return operatorType;
   }

   /**
    * This method will check whether the value exists in list or not
    * 
    * @param value
    * @param values
    * @return isExists
    */
   private boolean isValueExist (QIValue value, List<QIValue> values) {
      boolean isExists = false;
      for (QIValue qValue : values) {
         if (qValue.getValue().equalsIgnoreCase(value.getValue())) {
            isExists = true;
            break;
         }
      }
      return isExists;
   }

   /**
    * This method checks whether the term exists in the list or not
    * 
    * @param businessTerm
    * @param businessTerms
    * @return isExists
    */
   private boolean isTermExists (QIBusinessTerm businessTerm, List<QIBusinessTerm> businessTerms) {
      boolean isExists = false;
      for (QIBusinessTerm qBusinessTerm : businessTerms) {
         if (qBusinessTerm.getName().equalsIgnoreCase(businessTerm.getName())) {
            isExists = true;
            break;
         }
      }
      return isExists;
   }

   /**
    * This method will get the matched term from the list if any
    * 
    * @param currCondition
    * @param mergedConditions
    * @param nonEqualityOperators
    * @param equalityOperators
    * @param compatibleOperatorType
    * @return matchedCondition
    */
   private QICondition getMatchedTerm (QICondition currCondition, List<QICondition> mergedConditions,
            List<OperatorType> equalityOperators, List<OperatorType> nonEqualityOperators,
            List<OperatorType> compatibleOperatorType) {
      QICondition matchedCondition = null;
      for (QICondition condition : mergedConditions) {
         if (condition.getLhsBusinessTerm().getTerm().getName().equalsIgnoreCase(
                  currCondition.getLhsBusinessTerm().getTerm().getName())) {
            boolean isTermSame = false;
            if (condition.getLhsBusinessTerm().getStat() != null
                     && currCondition.getLhsBusinessTerm().getStat() != null) {
               if (condition.getLhsBusinessTerm().getStat().equalsIgnoreCase(
                        currCondition.getLhsBusinessTerm().getStat())) {
                  isTermSame = true;
               } else {
                  isTermSame = false;
               }
            } else if (condition.getLhsBusinessTerm().getStat() == null
                     && currCondition.getLhsBusinessTerm().getStat() == null) {
               isTermSame = true;
            } else {
               isTermSame = false;
            }
            if (isTermSame) {
               List<OperatorType> localCompatibleOperatorType = getCompatibleOperatorType(currCondition.getOperator(),
                        condition.getOperator(), equalityOperators, nonEqualityOperators);
               if (ExecueCoreUtil.isCollectionNotEmpty(localCompatibleOperatorType)) {
                  compatibleOperatorType.addAll(localCompatibleOperatorType);
                  matchedCondition = condition;
                  break;
               }
            }
         }
      }
      return matchedCondition;
   }

   /**
    * This method will return whether both operators are compatiable or not
    * 
    * @param operatorType1
    * @param operatorType2
    * @param equalityOperators
    * @param nonEqualityOperators
    * @return operatorType
    */
   private List<OperatorType> getCompatibleOperatorType (OperatorType operatorType1, OperatorType operatorType2,
            List<OperatorType> equalityOperators, List<OperatorType> nonEqualityOperators) {
      List<OperatorType> operatorType = null;
      if (equalityOperators.contains(operatorType1) && equalityOperators.contains(operatorType2)) {
         operatorType = equalityOperators;
      } else if (nonEqualityOperators.contains(operatorType1) && nonEqualityOperators.contains(operatorType2)) {
         operatorType = nonEqualityOperators;
      }
      return operatorType;
   }

   private BusinessQuery generateSubQuery (QISubQuery subQuery, Map<String, BusinessEntityTerm> entityTermMap,
            Model model) throws QIException {
      BusinessQuery businessQuery = new BusinessQuery();
      businessQuery.setModelId(model.getId());
      businessQuery.setSummarizations(generateSummarizations(subQuery.getSummarizations(), entityTermMap, model));
      List<BusinessCondition> conditions = generateConditions(subQuery.getConditions(), entityTermMap, model);
      businessQuery.setConditions(conditions);
      return businessQuery;
   }

   private List<BusinessTerm> generateSummarizations (List<QIBusinessTerm> summarizations,
            Map<String, BusinessEntityTerm> entityTermMap, Model model) throws QIException {
      List<BusinessTerm> groupBys = new ArrayList<BusinessTerm>();
      if (ExecueCoreUtil.isCollectionNotEmpty(summarizations)) {
         for (QIBusinessTerm summarization : summarizations) {
            BusinessTerm businessTerm = getBusinessTerm(summarization, null, entityTermMap, model);
            if (businessTerm != null) {
               BusinessEntityTerm businessEntityTerm = businessTerm.getBusinessEntityTerm();
               if (businessEntityTerm.isMeasurableEntity()) {
                  businessEntityTerm.setMeasureGroupBy(CheckType.YES);
               }
               businessTerm.setRequestedByUser(true);
               groupBys.add(businessTerm);
            }
         }
      }
      return groupBys;
   }

   private List<BusinessTerm> generateSelects (List<QISelect> selects, Map<String, BusinessEntityTerm> entityTermMap,
            Model model) throws QIException {
      // filter the duplicate selects from qiform
      List<QISelect> uniqueSelects = getUniqueSelects(selects);
      List<BusinessTerm> metrics = new ArrayList<BusinessTerm>();
      if (ExecueCoreUtil.isCollectionNotEmpty(uniqueSelects)) {
         for (QISelect select : uniqueSelects) {
            BusinessTerm businessTerm = getBusinessTermAndStat(select.getTerm(), select.getStat(), entityTermMap, model);
            if (businessTerm != null) {
               businessTerm.setRequestedByUser(true);
               metrics.add(businessTerm);
            }
         }
      }
      return metrics;
   }

   /**
    * This method will return the unique selects based on some criteria of stat matching and term match
    * 
    * @param selects
    * @return selectTerms
    */
   private List<QISelect> getUniqueSelects (List<QISelect> selects) {
      List<QISelect> selectTerms = new ArrayList<QISelect>();
      for (QISelect selectTerm : selects) {
         QISelect matchedTerm = getMatchedTerm(selectTerm, selectTerms);
         if (matchedTerm == null) {
            selectTerms.add(selectTerm);
         } else {
            if (selectTerm.getStat() != null) {
               int currIndex = selectTerms.indexOf(matchedTerm);
               selectTerms.remove(matchedTerm);
               selectTerms.add(currIndex, selectTerm);
            }
         }
      }
      return selectTerms;
   }

   /**
    * This method returns the matched term among the list
    * 
    * @param selectTerm
    * @param selectTerms
    * @return matchedTerm
    */
   private QISelect getMatchedTerm (QISelect selectTerm, List<QISelect> selectTerms) {
      QISelect matchedTerm = null;
      for (QISelect select : selectTerms) {
         if (select.getTerm().getName().equalsIgnoreCase(selectTerm.getTerm().getName())) {
            if (select.getStat() != null && selectTerm.getStat() != null) {
               if (select.getStat().equalsIgnoreCase(selectTerm.getStat())) {
                  matchedTerm = select;
                  break;
               }
            } else {
               matchedTerm = select;
               break;
            }
         }
      }
      return matchedTerm;
   }

   private BusinessTerm getBusinessTermAndStat (QIBusinessTerm term, String stat,
            Map<String, BusinessEntityTerm> entityTermMap, Model model) throws QIException {
      BusinessTerm businessTerm = getBusinessTerm(term, null, entityTermMap, model);
      if (businessTerm != null) {
         logger.debug("create  stat for  business Entity for : " + term.getName() + " stat : " + stat);
         if (StringUtils.isNotBlank(stat)) {
            try {
               BusinessStat businessStat = new BusinessStat();
               businessStat.setRequestedByUser(true);
               businessStat.setStat(kdxRetrievalService.getStatByName(stat));
               businessTerm.setBusinessStat(businessStat);
            } catch (KDXException kdxException) {
               throw new QIException(QIExceptionCodes.QI_DEFAULT_EXCEPTION_CODE,
                        "Error while retrieving the stat by name", kdxException.getCause());
            }
         }
      }
      return businessTerm;
   }

   private BusinessTerm getBusinessTerm (QIBusinessTerm term, Concept parentConceptForInstance,
            Map<String, BusinessEntityTerm> entityTermMap, Model model) throws QIException {
      BusinessEntityTerm businessEntityTerm = getBusinessEntityTerm(term, parentConceptForInstance, entityTermMap,
               model);
      BusinessTerm businessTerm = new BusinessTerm();
      businessTerm.setBusinessEntityTerm(businessEntityTerm);
      return businessTerm;
   }

   private BusinessEntityTerm getBusinessEntityTerm (QIBusinessTerm term, Concept parentConceptForInstance,
            Map<String, BusinessEntityTerm> entityTermMap, Model model) throws QIException {
      BusinessEntityTerm businessEntityTerm = null;
      try {
         if (BusinessEntityType.CONCEPT.equals(term.getType())) {
            BusinessEntityTerm finalBusinessEntityTerm = getBusinessEntityTermFromCache(entityTermMap, term.getName(),
                     BusinessEntityType.CONCEPT);
            if (finalBusinessEntityTerm == null) {
               finalBusinessEntityTerm = new BusinessEntityTerm();
               Concept concept = kdxRetrievalService.getPopulatedConceptWithStats(model.getId(), term.getName());
               BusinessEntityDefinition businessEntityDefinition = kdxRetrievalService
                        .getBusinessEntityDefinitionByIds(model.getId(), concept.getId(), null);

               finalBusinessEntityTerm.setBusinessEntity(concept);
               finalBusinessEntityTerm.setBusinessEntityDefinitionId(businessEntityDefinition.getId().longValue());
               finalBusinessEntityTerm.setBusinessEntityType(BusinessEntityType.CONCEPT);
               if (ExecueConstants.MEASURABLE_ENTITY_TYPE
                        .equalsIgnoreCase(businessEntityDefinition.getType().getName())) {
                  finalBusinessEntityTerm.setMeasurableEntity(true);
               }
               finalBusinessEntityTerm.setDependantMeasure(getKdxModelService().checkEntityHasBehavior(
                        businessEntityDefinition.getId(), BehaviorType.DEPENDENT_VARIABLE));
               storeBusinessEntityTermToCache(entityTermMap, finalBusinessEntityTerm, term.getName());
            }
            businessEntityTerm = finalBusinessEntityTerm;
         } else if (BusinessEntityType.CONCEPT_LOOKUP_INSTANCE.equals(term.getType())) {
            BusinessEntityTerm finalBusinessEntityTerm = getBusinessEntityTermFromCache(entityTermMap, term.getName(),
                     BusinessEntityType.CONCEPT_LOOKUP_INSTANCE);
            if (finalBusinessEntityTerm == null) {
               finalBusinessEntityTerm = new BusinessEntityTerm();
               BusinessEntityDefinition businessEntityDefinition = kdxRetrievalService
                        .getBusinessEntityDefinitionByNames(model.getName(), parentConceptForInstance.getName(), term
                                 .getName());
               Instance instance = businessEntityDefinition.getInstance();
               instance.setParentConcept(parentConceptForInstance);
               finalBusinessEntityTerm.setBusinessEntity(instance);
               finalBusinessEntityTerm.setBusinessEntityDefinitionId(businessEntityDefinition.getId().longValue());
               finalBusinessEntityTerm.setBusinessEntityType(BusinessEntityType.CONCEPT_LOOKUP_INSTANCE);
               storeBusinessEntityTermToCache(entityTermMap, finalBusinessEntityTerm, term.getName());
            }
            businessEntityTerm = finalBusinessEntityTerm;
         } else if (BusinessEntityType.CONCEPT_PROFILE.equals(term.getType())) {
            ConceptProfile conceptProfile = (ConceptProfile) getPreferencesRetrievalService().getProfile(model.getId(),
                     term.getName(), ProfileType.CONCEPT);
            BusinessEntityTerm conceptProfileEntityTerm = new BusinessEntityTerm();
            conceptProfileEntityTerm.setBusinessEntityType(BusinessEntityType.CONCEPT_PROFILE);
            conceptProfileEntityTerm.setBusinessEntity(conceptProfile);
            BusinessEntityDefinition businessEntityDefinition = getPreferencesRetrievalService()
                     .getBusinessEntityDefinitionForConceptProfile(conceptProfile);
            conceptProfileEntityTerm.setBusinessEntityDefinitionId(businessEntityDefinition.getId());
            businessEntityTerm = conceptProfileEntityTerm;
         } else if (BusinessEntityType.INSTANCE_PROFILE.equals(term.getType())) {
            InstanceProfile instanceProfile = (InstanceProfile) getPreferencesRetrievalService().getProfile(
                     model.getId(), term.getName(), ProfileType.CONCEPT_LOOKUP_INSTANCE);
            BusinessEntityTerm instanceProfileEntityTerm = new BusinessEntityTerm();
            instanceProfileEntityTerm.setBusinessEntityType(BusinessEntityType.INSTANCE_PROFILE);
            instanceProfileEntityTerm.setBusinessEntity(instanceProfile);
            BusinessEntityDefinition businessEntityDefinition = getPreferencesRetrievalService()
                     .getBusinessEntityDefinitionForInstanceProfile(instanceProfile);
            instanceProfileEntityTerm.setBusinessEntityDefinitionId(businessEntityDefinition.getId());
            businessEntityTerm = instanceProfileEntityTerm;
         }
      } catch (KDXException kdxException) {
         throw new QIException(QIExceptionCodes.QI_DEFAULT_EXCEPTION_CODE, "Error while retrieving the Business Terms",
                  kdxException.getCause());
      } catch (PreferencesException preferencesException) {
         throw new QIException(QIExceptionCodes.QI_DEFAULT_EXCEPTION_CODE,
                  "Error while retrieving the Business Terms for Profile", preferencesException.getCause());
      }
      return businessEntityTerm;
   }

   private BusinessEntityTerm getBusinessEntityTermFromCache (Map<String, BusinessEntityTerm> entityTermMap,
            String termName, BusinessEntityType businessEntityType) {
      BusinessEntityTerm cachedBusinessEntityTerm = null;
      for (String term : entityTermMap.keySet()) {
         BusinessEntityTerm tempEntityTerm = entityTermMap.get(term);
         if (termName.equalsIgnoreCase(term) && businessEntityType.equals(tempEntityTerm.getBusinessEntityType())) {
            cachedBusinessEntityTerm = tempEntityTerm;
            break;
         }
      }
      return cachedBusinessEntityTerm;
   }

   private void storeBusinessEntityTermToCache (Map<String, BusinessEntityTerm> entityTermMap,
            BusinessEntityTerm businessEntityTerm, String termName) {
      entityTermMap.put(termName, businessEntityTerm);
   }

   public IBusinessQueryOrganizationService getBusinessQueryOrganizationService () {
      return businessQueryOrganizationService;
   }

   public void setBusinessQueryOrganizationService (IBusinessQueryOrganizationService businessQueryOrganizationService) {
      this.businessQueryOrganizationService = businessQueryOrganizationService;
   }

   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
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

   public ISWIConfigurationService getSwiConfigurationService () {
      return swiConfigurationService;
   }

   public void setSwiConfigurationService (ISWIConfigurationService swiConfigurationService) {
      this.swiConfigurationService = swiConfigurationService;
   }

   public IPreferencesRetrievalService getPreferencesRetrievalService () {
      return preferencesRetrievalService;
   }

   public void setPreferencesRetrievalService (IPreferencesRetrievalService preferencesRetrievalService) {
      this.preferencesRetrievalService = preferencesRetrievalService;
   }

   /**
    * @return the kdxModelService
    */
   public IKDXModelService getKdxModelService () {
      return kdxModelService;
   }

   /**
    * @param kdxModelService the kdxModelService to set
    */
   public void setKdxModelService (IKDXModelService kdxModelService) {
      this.kdxModelService = kdxModelService;
   }

}
