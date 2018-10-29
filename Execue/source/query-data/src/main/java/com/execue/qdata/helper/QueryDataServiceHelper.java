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


package com.execue.qdata.helper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.execue.core.common.bean.AggregateQuery;
import com.execue.core.common.bean.BusinessCondition;
import com.execue.core.common.bean.BusinessOrderClause;
import com.execue.core.common.bean.BusinessQuery;
import com.execue.core.common.bean.BusinessTerm;
import com.execue.core.common.bean.QueryForm;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.Instance;
import com.execue.core.common.bean.governor.BusinessAssetTerm;
import com.execue.core.common.bean.governor.StructuredCondition;
import com.execue.core.common.bean.governor.StructuredOrderClause;
import com.execue.core.common.bean.governor.StructuredQuery;
import com.execue.core.common.bean.qdata.QDataAggregatedQueryColumn;
import com.execue.core.common.bean.qdata.QDataAggregatedReportType;
import com.execue.core.common.bean.qdata.QDataBusinessQueryColumn;
import com.execue.core.common.bean.qdata.QDataReducedQuery;
import com.execue.core.common.bean.qdata.QDataUserQueryColumn;
import com.execue.core.common.bean.qi.QIBusinessTerm;
import com.execue.core.common.bean.qi.QICondition;
import com.execue.core.common.bean.qi.QIConditionLHS;
import com.execue.core.common.bean.qi.QIOrderBy;
import com.execue.core.common.bean.qi.QISelect;
import com.execue.core.common.bean.qi.QIValue;
import com.execue.core.common.bean.querygen.QueryValue;
import com.execue.core.common.type.BusinessEntityType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.OperandType;
import com.execue.core.common.type.QuerySectionType;
import com.execue.core.common.type.ReportType;
import com.execue.core.util.ExecueCoreUtil;

/**
 * This is helper class to populate the QData query column objects
 * 
 * @author Vishay
 * @version 1.0
 * @since 25/06/09
 */
public class QueryDataServiceHelper {

   private static final char delimiter = '~';

   public static Set<QDataAggregatedReportType> genereateQDataReportTypes (List<ReportType> reportList) {
      if (reportList == null) {
         return null;
      }
      Set<QDataAggregatedReportType> qDataReportList = new HashSet<QDataAggregatedReportType>();
      for (ReportType reportType : reportList) {
         QDataAggregatedReportType qDataReportType = new QDataAggregatedReportType();
         qDataReportType.setType(reportType);
         qDataReportList.add(qDataReportType);
      }
      return qDataReportList;
   }

   /**
    * This method will populate the QDataAggregatedQuery columns.
    * 
    * @param aggregatedQuery
    * @return qDataAggregatedColumns
    */
   public static Set<QDataAggregatedQueryColumn> generateAggregatedQueryColumns (AggregateQuery aggregatedQuery) {
      Set<QDataAggregatedQueryColumn> qDataAggregatedColumns = new HashSet<QDataAggregatedQueryColumn>();
      StructuredQuery structuredQuery = aggregatedQuery.getAssetQuery().getLogicalQuery();
      if (ExecueCoreUtil.isCollectionNotEmpty(structuredQuery.getMetrics())) {
         for (BusinessAssetTerm businessAssetTerm : structuredQuery.getMetrics()) {
            qDataAggregatedColumns.add(populateQDataAggregatedQueryColumn(businessAssetTerm.getBusinessTerm(),
                     QuerySectionType.SELECT));
         }
      }
      if (ExecueCoreUtil.isCollectionNotEmpty(structuredQuery.getSummarizations())) {
         for (BusinessAssetTerm businessAssetTerm : structuredQuery.getSummarizations()) {
            QDataAggregatedQueryColumn groupQDataAggregatedQueryColumn = populateQDataAggregatedQueryColumn(businessAssetTerm.getBusinessTerm(),
                     QuerySectionType.GROUP);
            if (businessAssetTerm.getBusinessTerm().getRange() != null) {
               groupQDataAggregatedQueryColumn.setValue(businessAssetTerm.getBusinessTerm().getRange().getId().toString());   
            }
            qDataAggregatedColumns.add(groupQDataAggregatedQueryColumn);
         }
      }
      if (ExecueCoreUtil.isCollectionNotEmpty(structuredQuery.getPopulations())) {
         for (BusinessAssetTerm businessAssetTerm : structuredQuery.getPopulations()) {
            qDataAggregatedColumns.add(populateQDataAggregatedQueryColumn(businessAssetTerm.getBusinessTerm(),
                     QuerySectionType.POPULATION));
         }
      }
      qDataAggregatedColumns.addAll(populateQDataAggregatedColumnConditions(structuredQuery.getConditions(),
               QuerySectionType.CONDITION));
      qDataAggregatedColumns.addAll(populateQDataAggregatedColumnConditions(structuredQuery.getHavingClauses(),
               QuerySectionType.HAVING));
      if (ExecueCoreUtil.isCollectionNotEmpty(structuredQuery.getOrderClauses())) {
         // TODO : -VG- Entity Type is getting saved as a string in value column
         for (StructuredOrderClause structuredOrderClause : structuredQuery.getOrderClauses()) {
            QDataAggregatedQueryColumn qDataAggregatedQueryColumn = populateQDataAggregatedQueryColumn(
                     structuredOrderClause.getBusinessAssetTerm().getBusinessTerm(), QuerySectionType.ORDER);
            qDataAggregatedQueryColumn.setValue(structuredOrderClause.getOrderEntityType().getValue());
            qDataAggregatedColumns.add(qDataAggregatedQueryColumn);
         }
      }
      if (structuredQuery.getTopBottom() != null) {
         // TODO : -VG- Entity Type and value has to be accomodated. Entity type and value is getting saved as a
         // string with '~' as delimeter
         QDataAggregatedQueryColumn qDataAggregatedQueryColumn = populateQDataAggregatedQueryColumn(structuredQuery
                  .getTopBottom().getBusinessAssetTerm().getBusinessTerm(), QuerySectionType.TOP_BOTTOM);
         List<String> values = new ArrayList<String>();
         values.add(structuredQuery.getTopBottom().getLimitType().getValue());
         values.add(structuredQuery.getTopBottom().getLimitValue().toString());
         qDataAggregatedQueryColumn.setValue(getConcatenatedString(values));
         qDataAggregatedColumns.add(qDataAggregatedQueryColumn);
      }
      if (structuredQuery.getCohort() != null) {
         qDataAggregatedColumns.addAll(populateQDataAggregatedColumnConditions(structuredQuery.getCohort()
                  .getConditions(), QuerySectionType.COHORT_CONDITION));
         if (ExecueCoreUtil.isCollectionNotEmpty(structuredQuery.getCohort().getSummarizations())) {
            for (BusinessAssetTerm businessAssetTerm : structuredQuery.getCohort().getSummarizations()) {
               qDataAggregatedColumns.add(populateQDataAggregatedQueryColumn(businessAssetTerm.getBusinessTerm(),
                        QuerySectionType.GROUP));
            }
         }
      }
      return qDataAggregatedColumns;
   }

   /**
    * This method will populate the QDataBusinessQuery columns
    * 
    * @param businessQuery
    * @return qDataBusinessColumns
    */
   public static Set<QDataBusinessQueryColumn> generateBusinessQueryColumns (BusinessQuery businessQuery) {
      Set<QDataBusinessQueryColumn> qDataBusinessColumns = new HashSet<QDataBusinessQueryColumn>();
      if (ExecueCoreUtil.isCollectionNotEmpty(businessQuery.getMetrics())) {
         for (BusinessTerm businessTerm : businessQuery.getMetrics()) {
            qDataBusinessColumns.add(populateQDataBusinessQueryColumn(businessTerm, QuerySectionType.SELECT));
         }
      }
      if (ExecueCoreUtil.isCollectionNotEmpty(businessQuery.getSummarizations())) {
         for (BusinessTerm businessTerm : businessQuery.getSummarizations()) {
            qDataBusinessColumns.add(populateQDataBusinessQueryColumn(businessTerm, QuerySectionType.GROUP));
         }
      }
      if (ExecueCoreUtil.isCollectionNotEmpty(businessQuery.getPopulations())) {
         for (BusinessTerm businessTerm : businessQuery.getPopulations()) {
            qDataBusinessColumns.add(populateQDataBusinessQueryColumn(businessTerm, QuerySectionType.POPULATION));
         }
      }
      qDataBusinessColumns.addAll(populateQDataBusinessColumnConditions(businessQuery.getConditions(),
               QuerySectionType.CONDITION));
      qDataBusinessColumns.addAll(populateQDataBusinessColumnConditions(businessQuery.getHavingClauses(),
               QuerySectionType.HAVING));
      if (ExecueCoreUtil.isCollectionNotEmpty(businessQuery.getOrderClauses())) {
         // TODO : -VG- Entity Type is getting saved as a string in value column
         for (BusinessOrderClause businessOrderClause : businessQuery.getOrderClauses()) {
            QDataBusinessQueryColumn qDataBusinessQueryColumn = populateQDataBusinessQueryColumn(businessOrderClause
                     .getBusinessTerm(), QuerySectionType.ORDER);
            qDataBusinessQueryColumn.setValue(businessOrderClause.getOrderEntityType().getValue());
            qDataBusinessColumns.add(qDataBusinessQueryColumn);
         }
      }
      if (businessQuery.getTopBottom() != null) {
         // TODO : -VG- Entity Type and value has to be accomodated. Entity type and value is getting saved as a
         // string with '~' as delimeter
         QDataBusinessQueryColumn qDataBusinessQueryColumn = populateQDataBusinessQueryColumn(businessQuery
                  .getTopBottom().getBusinessTerm(), QuerySectionType.TOP_BOTTOM);
         List<String> values = new ArrayList<String>();
         values.add(businessQuery.getTopBottom().getLimitType().getValue());
         values.add(businessQuery.getTopBottom().getLimitValue());
         qDataBusinessQueryColumn.setValue(getConcatenatedString(values));
         qDataBusinessColumns.add(qDataBusinessQueryColumn);
      }
      if (businessQuery.getCohort() != null) {
         qDataBusinessColumns.addAll(populateQDataBusinessColumnConditions(businessQuery.getCohort().getConditions(),
                  QuerySectionType.COHORT_CONDITION));
         if (ExecueCoreUtil.isCollectionNotEmpty(businessQuery.getCohort().getSummarizations())) {
            for (BusinessTerm businessTerm : businessQuery.getCohort().getSummarizations()) {
               qDataBusinessColumns.add(populateQDataBusinessQueryColumn(businessTerm, QuerySectionType.COHORT_GROUP));
            }
         }
      }
      return qDataBusinessColumns;
   }

   /**
    * This method will populate the QDataUserQuery columns
    * 
    * @param queryForm
    * @return qDataUserColumns
    */
   public static Set<QDataUserQueryColumn> generateUserQueryColumns (QueryForm queryForm) {
      Set<QDataUserQueryColumn> qDataUserColumns = new HashSet<QDataUserQueryColumn>();
      if (ExecueCoreUtil.isCollectionNotEmpty(queryForm.getSelects())) {
         for (QISelect select : queryForm.getSelects()) {
            QDataUserQueryColumn qDataUserQueryColumn = populateQDataUserQueryColumn(select.getTerm(),
                     QuerySectionType.SELECT);
            qDataUserQueryColumn.setStat(select.getStat());
            qDataUserColumns.add(qDataUserQueryColumn);
         }
      }
      if (ExecueCoreUtil.isCollectionNotEmpty(queryForm.getSummarizations())) {
         for (QIBusinessTerm qBusinessTerm : queryForm.getSummarizations()) {
            qDataUserColumns.add(populateQDataUserQueryColumn(qBusinessTerm, QuerySectionType.GROUP));
         }
      }
      if (ExecueCoreUtil.isCollectionNotEmpty(queryForm.getPopulations())) {
         for (QIBusinessTerm qBusinessTerm : queryForm.getPopulations()) {
            qDataUserColumns.add(populateQDataUserQueryColumn(qBusinessTerm, QuerySectionType.POPULATION));
         }
      }
      qDataUserColumns.addAll(populateQDataUserColumnConditions(queryForm.getConditions(), QuerySectionType.CONDITION));

      if (ExecueCoreUtil.isCollectionNotEmpty(queryForm.getOrderBys())) {
         for (QIOrderBy qOrderBy : queryForm.getOrderBys()) {
            // TODO : -VG- Entity Type and value has to be accomodated. Entity type and value is getting saved as a
            // string
            QDataUserQueryColumn qDataUserQueryColumn = populateQDataUserQueryColumn(qOrderBy.getTerm(),
                     QuerySectionType.ORDER);
            List<String> values = new ArrayList<String>();
            values.add(qOrderBy.getType().getValue());
            values.add(qOrderBy.getValue());
            qDataUserQueryColumn.setValue(getConcatenatedString(values));
            qDataUserColumns.add(qDataUserQueryColumn);
         }
      }
      if (queryForm.getTopBottom() != null) {
         // TODO : -VG- Entity Type and value has to be accomodated. Entity type and value is getting saved as a
         // string with '~' as delimeter
         QDataUserQueryColumn qDataUserQueryColumn = populateQDataUserQueryColumn(queryForm.getTopBottom().getTerm(),
                  QuerySectionType.TOP_BOTTOM);
         List<String> values = new ArrayList<String>();
         values.add(queryForm.getTopBottom().getType().getValue());
         values.add(queryForm.getTopBottom().getValue());
         qDataUserQueryColumn.setValue(getConcatenatedString(values));
         qDataUserColumns.add(qDataUserQueryColumn);
      }
      if (queryForm.getCohort() != null) {
         qDataUserColumns.addAll(populateQDataUserColumnConditions(queryForm.getCohort().getConditions(),
                  QuerySectionType.COHORT_CONDITION));
         if (ExecueCoreUtil.isCollectionNotEmpty(queryForm.getCohort().getSummarizations())) {
            for (QIBusinessTerm qBusinessTerm : queryForm.getCohort().getSummarizations()) {
               qDataUserColumns.add(populateQDataUserQueryColumn(qBusinessTerm, QuerySectionType.COHORT_GROUP));
            }
         }
      }
      return qDataUserColumns;
   }

   /**
    * This method will prepare QDataUserQueryColumn object from qBusinessTerm
    * 
    * @param qBusinessTerm
    * @param querySectionType
    * @return qDataUserQueryColumn
    */
   private static QDataUserQueryColumn populateQDataUserQueryColumn (QIBusinessTerm qBusinessTerm,
            QuerySectionType querySectionType) {
      QDataUserQueryColumn qDataUserQueryColumn = new QDataUserQueryColumn();
      qDataUserQueryColumn.setName(qBusinessTerm.getName());
      qDataUserQueryColumn.setQuerySection(querySectionType);
      qDataUserQueryColumn.setBusinessEntityId(0L);
      // TODO -VG- : business entity id should come from QueryForm itself
      return qDataUserQueryColumn;
   }

   /**
    * This method will prepare the QDataBusinessQueryColumn object from businessTerm
    * 
    * @param businessTerm
    * @param querySectionType
    * @return qDataBusinessQueryColumn
    */
   private static QDataBusinessQueryColumn populateQDataBusinessQueryColumn (BusinessTerm businessTerm,
            QuerySectionType querySectionType) {
      QDataBusinessQueryColumn qDataBusinessQueryColumn = new QDataBusinessQueryColumn();
      // TODO : -VG- need to handle other entity types
      if (BusinessEntityType.CONCEPT.equals(businessTerm.getBusinessEntityTerm().getBusinessEntityType())) {
         Concept concept = (Concept) businessTerm.getBusinessEntityTerm().getBusinessEntity();
         qDataBusinessQueryColumn.setName(concept.getName());
      } else if (BusinessEntityType.CONCEPT_LOOKUP_INSTANCE.equals(businessTerm.getBusinessEntityTerm()
               .getBusinessEntityType())) {
         Instance instance = (Instance) businessTerm.getBusinessEntityTerm().getBusinessEntity();
         qDataBusinessQueryColumn.setName(instance.getName());
      }
      if (businessTerm.getBusinessStat() != null) {
         qDataBusinessQueryColumn.setStat(businessTerm.getBusinessStat().getStat().getStatType().getValue());
      }
      qDataBusinessQueryColumn
               .setBusinessEntityId(businessTerm.getBusinessEntityTerm().getBusinessEntityDefinitionId());
      qDataBusinessQueryColumn.setQuerySection(querySectionType);
      qDataBusinessQueryColumn.setIsRequestedByUser(getCheckType(businessTerm));
      return qDataBusinessQueryColumn;
   }

   /**
    * This method will prepare QDataAggregatedQueryColumn from businessTerm
    * 
    * @param businessTerm
    * @param querySectionType
    * @return qDataAggregatedQueryColumn
    */
   private static QDataAggregatedQueryColumn populateQDataAggregatedQueryColumn (BusinessTerm businessTerm,
            QuerySectionType querySectionType) {
      QDataAggregatedQueryColumn qDataAggregatedQueryColumn = new QDataAggregatedQueryColumn();
      // TODO : -VG- need to handle other entity types
      if (BusinessEntityType.CONCEPT.equals(businessTerm.getBusinessEntityTerm().getBusinessEntityType())) {
         Concept concept = (Concept) businessTerm.getBusinessEntityTerm().getBusinessEntity();
         qDataAggregatedQueryColumn.setName(concept.getName());
      } else if (BusinessEntityType.CONCEPT_LOOKUP_INSTANCE.equals(businessTerm.getBusinessEntityTerm()
               .getBusinessEntityType())) {
         Instance instance = (Instance) businessTerm.getBusinessEntityTerm().getBusinessEntity();
         qDataAggregatedQueryColumn.setName(instance.getName());
      }
      if (businessTerm.getBusinessStat() != null) {
         qDataAggregatedQueryColumn.setStat(businessTerm.getBusinessStat().getStat().getStatType().getValue());
      }
      qDataAggregatedQueryColumn.setBusinessEntityId(businessTerm.getBusinessEntityTerm()
               .getBusinessEntityDefinitionId());
      qDataAggregatedQueryColumn.setQuerySection(querySectionType);
      qDataAggregatedQueryColumn.setIsRequestedByUser(getCheckType(businessTerm));
      return qDataAggregatedQueryColumn;
   }

   /**
    * This method will prepare QDataUserQueryColumn conditions from qiConditions
    * 
    * @param qiConditions
    * @param querySectionType
    * @return qDataUserColumns
    */
   private static List<QDataUserQueryColumn> populateQDataUserColumnConditions (List<QICondition> qiConditions,
            QuerySectionType querySectionType) {
      List<QDataUserQueryColumn> qDataUserColumns = new ArrayList<QDataUserQueryColumn>();
      if (ExecueCoreUtil.isCollectionNotEmpty(qiConditions)) {
         for (QICondition qCondition : qiConditions) {
            QIConditionLHS qConditionLHS = qCondition.getLhsBusinessTerm();
            QDataUserQueryColumn qDataUserQueryColumn = populateQDataUserQueryColumn(qConditionLHS.getTerm(),
                     querySectionType);
            qDataUserQueryColumn.setOperator(qCondition.getOperator());
            // TODO: -VG- values has to be carried.Now, '~' delimeter is being used to store the values, but if right
            // side is a sub query, then we need to accomodate that situation
            List<String> rhsValues = new ArrayList<String>();
            if (ExecueCoreUtil.isCollectionNotEmpty(qCondition.getRhsValue().getTerms())) {
               for (QIBusinessTerm qBusinessTerm : qCondition.getRhsValue().getTerms()) {
                  rhsValues.add(qBusinessTerm.getName());
               }
            } else if (ExecueCoreUtil.isCollectionNotEmpty(qCondition.getRhsValue().getValues())) {
               List<QIValue> values = qCondition.getRhsValue().getValues();
               for (QIValue value : values) {
                  rhsValues.add(value.getValue());
               }
            } else {
               // handle the subquery here
            }
            qDataUserQueryColumn.setValue(getConcatenatedString(rhsValues));
            qDataUserColumns.add(qDataUserQueryColumn);
         }
      }
      return qDataUserColumns;
   }

   /**
    * This method will prepare QDataBusinessQueryColumn from businessConditions
    * 
    * @param businessConditions
    * @param querySectionType
    * @return qDataBusinessQueryColumns
    */
   private static List<QDataBusinessQueryColumn> populateQDataBusinessColumnConditions (
            List<BusinessCondition> businessConditions, QuerySectionType querySectionType) {
      List<QDataBusinessQueryColumn> qDataBusinessQueryColumns = new ArrayList<QDataBusinessQueryColumn>();
      if (ExecueCoreUtil.isCollectionNotEmpty(businessConditions)) {
         for (BusinessCondition businessCondition : businessConditions) {
            QDataBusinessQueryColumn qDataBusinessQueryColumn = populateQDataBusinessQueryColumn(businessCondition
                     .getLhsBusinessTerm(), querySectionType);
            qDataBusinessQueryColumn.setOperator(businessCondition.getOperator());
            // TODO: -VG- values has to be carried.Now, '~' delimeter is being used to store the values, but if right
            // side is a sub query, then we need to accomodate that situation
            List<String> rhsValues = new ArrayList<String>();
            if (OperandType.BUSINESS_TERM.equals(businessCondition.getOperandType())) {
               for (BusinessTerm rhsBusinessTerm : businessCondition.getRhsBusinessTerms()) {
                  // TODO : -VG- need to test whether rhs is always a instance
                  Instance instance = (Instance) rhsBusinessTerm.getBusinessEntityTerm().getBusinessEntity();
                  rhsValues.add(instance.getName());
               }
            } else if (OperandType.VALUE.equals(businessCondition.getOperandType())) {
               for (QueryValue queryValue : businessCondition.getRhsValues()) {
                  rhsValues.add(queryValue.getValue());
               }
            } else {
               // handle the subquery here
            }
            qDataBusinessQueryColumn.setValue(getConcatenatedString(rhsValues));
            qDataBusinessQueryColumns.add(qDataBusinessQueryColumn);
         }
      }
      return qDataBusinessQueryColumns;
   }

   /**
    * This method will prepare QDataAggregatedQueryColumn from structuredConditions
    * 
    * @param structuredConditions
    * @param querySectionType
    * @return qDataAggregatedQueryColumns
    */
   private static List<QDataAggregatedQueryColumn> populateQDataAggregatedColumnConditions (
            List<StructuredCondition> structuredConditions, QuerySectionType querySectionType) {
      List<QDataAggregatedQueryColumn> qDataAggregatedQueryColumns = new ArrayList<QDataAggregatedQueryColumn>();
      if (ExecueCoreUtil.isCollectionNotEmpty(structuredConditions)) {
         for (StructuredCondition structuredCondition : structuredConditions) {
            if (structuredCondition.isSubCondition()) {
               for (StructuredCondition subCondition : structuredCondition.getSubConditions()) {
                  qDataAggregatedQueryColumns.add(populateQDataAggregatedColumnConditions(subCondition,
                           querySectionType));
               }
            } else {
               qDataAggregatedQueryColumns.add(populateQDataAggregatedColumnConditions(structuredCondition,
                        querySectionType));
            }
         }
      }
      return qDataAggregatedQueryColumns;
   }

   private static QDataAggregatedQueryColumn populateQDataAggregatedColumnConditions (
            StructuredCondition structuredCondition, QuerySectionType querySectionType) {
      QDataAggregatedQueryColumn qDataAggregatedQueryColumn = populateQDataAggregatedQueryColumn(structuredCondition
               .getLhsBusinessAssetTerm().getBusinessTerm(), querySectionType);
      qDataAggregatedQueryColumn.setOperator(structuredCondition.getOperator());
      // TODO: -VG- values has to be carried.Now, '~' delimeter is being used to store the values, but if right
      // side is a sub query, then we need to accommodate that situation
      List<String> rhsValues = new ArrayList<String>();
      if (OperandType.BUSINESS_TERM.equals(structuredCondition.getOperandType())) {
         for (BusinessAssetTerm rhsBusinessAssetTerm : structuredCondition.getRhsBusinessAssetTerms()) {
            // TODO : -VG- need to test whether rhs is always a instance
            Instance instance = (Instance) rhsBusinessAssetTerm.getBusinessTerm().getBusinessEntityTerm()
                     .getBusinessEntity();
            rhsValues.add(instance.getName());
         }
      } else if (OperandType.VALUE.equals(structuredCondition.getOperandType())) {
         for (QueryValue queryValue : structuredCondition.getRhsValues()) {
            rhsValues.add(queryValue.getValue());
         }
      } else {
         // handle the subquery here
      }
      qDataAggregatedQueryColumn.setValue(getConcatenatedString(rhsValues));
      return qDataAggregatedQueryColumn;
   }

   public static QDataReducedQuery populateQDataReducedQuery (Long userQueryId, Long applicationId,
            String reducedQueryString, double entityCount, double queryMaxMatchWeight, Double reducedQueryWeight,
            double baseUserQueryWeight, Double reducedQueryMatchPercent) {
      QDataReducedQuery qDataReducedQuery = new QDataReducedQuery();
      qDataReducedQuery.setUserQueryId(userQueryId);
      qDataReducedQuery.setApplicationId(applicationId);
      qDataReducedQuery.setReducedQueryString(reducedQueryString);
      qDataReducedQuery.setEntityCount(entityCount);
      qDataReducedQuery.setMaxMatchWeight(queryMaxMatchWeight);
      qDataReducedQuery.setReducedQueryWeight(reducedQueryWeight);
      qDataReducedQuery.setBaseUserQueryWeight(baseUserQueryWeight);
      qDataReducedQuery.setReducedQueryMatchPercent(reducedQueryMatchPercent);
      return qDataReducedQuery;
   }

   /**
    * This method will return the check type from businessTerm's requested by user information
    * 
    * @param businessTerm
    * @return checkType
    */
   private static CheckType getCheckType (BusinessTerm businessTerm) {
      CheckType checkType = CheckType.NO;
      if (businessTerm.isRequestedByUser()) {
         checkType = CheckType.YES;
      }
      return checkType;
   }

   /**
    * This method will return a concatenated string using '~' as delimeter
    * 
    * @param values
    * @return stringBuilder
    */
   private static String getConcatenatedString (List<String> values) {
      StringBuilder stringBuilder = new StringBuilder();
      if (ExecueCoreUtil.isCollectionNotEmpty(values)) {
         for (String value : values) {
            stringBuilder.append(value);
            stringBuilder.append(delimiter);
         }
         stringBuilder.deleteCharAt(stringBuilder.length() - 1);
      }
      return stringBuilder.toString();
   }
}
