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


package com.execue.ac.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.execue.ac.bean.ConceptColumnMapping;
import com.execue.ac.bean.CubeSourceColumnMapping;
import com.execue.ac.bean.SourceAssetMappingInfo;
import com.execue.ac.service.IAnswersCatalogConstants;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.Join;
import com.execue.core.common.bean.entity.JoinDefinition;
import com.execue.core.common.bean.entity.Mapping;
import com.execue.core.common.bean.entity.Membr;
import com.execue.core.common.bean.governor.BusinessEntityTerm;
import com.execue.core.common.bean.querygen.ConditionEntity;
import com.execue.core.common.bean.querygen.QueryColumn;
import com.execue.core.common.bean.querygen.QueryTable;
import com.execue.core.common.bean.querygen.QueryTableColumn;
import com.execue.core.common.type.BusinessEntityType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.DataType;
import com.execue.core.common.type.LookupType;
import com.execue.core.common.type.OperatorType;
import com.execue.core.common.type.QueryConditionOperandType;
import com.execue.core.common.util.ExecueBeanManagementUtil;
import com.execue.core.common.util.ExecueBeanUtil;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.core.util.ExecueStringUtil;

/**
 * @author Raju Gottumukkala
 * @version 4.0
 * @since 4.0
 */
public class AnswersCatalogUtil implements IAnswersCatalogConstants {

   /**
    * Helper method to get column names form the list of QueryColumn objects
    * 
    * @param columns
    * @return
    */
   public static List<String> getColumnNames (List<QueryColumn> columns) {
      List<String> columnNames = new ArrayList<String>();
      for (QueryColumn column : columns) {
         columnNames.add(column.getColumnName());
      }
      return columnNames;
   }

   public static void maintainColumnUniqueness (QueryColumn queryColumn, List<String> existingColumnNames,
            int maxDBObjectLength) {
      String normalizedObjectName = ExecueStringUtil.getNormalizedNameByTrimmingFromMiddle(queryColumn.getColumnName(),
               existingColumnNames, maxDBObjectLength);
      queryColumn.setColumnName(normalizedObjectName);
      existingColumnNames.add(normalizedObjectName);
   }

   public static void cleanQueryColumn (QueryColumn queryColumn) {
      queryColumn.setDefaultValue(null);
      queryColumn.setNullable(true);
   }

   private static QueryTable createQueryTable (String tableName, List<String> existingAliases) {
      return ExecueBeanManagementUtil.createQueryTable(tableName, ExecueCoreUtil.getAlias(existingAliases),
               CheckType.NO, LookupType.None);
   }

   public static QueryTable createQueryTable (String martTableName, List<String> existingAliases, String owner) {
      QueryTable queryTable = createQueryTable(martTableName, existingAliases);
      queryTable.setOwner(owner);
      return queryTable;
   }

   public static QueryTable createQueryTable (String tableName) {
      return ExecueBeanManagementUtil.createQueryTable(tableName, ExecueCoreUtil.getAlias(new ArrayList<String>()),
               CheckType.NO, LookupType.None);
   }

   public static QueryTable createQueryTable (String tableName, String owner) {
      QueryTable queryTable = createQueryTable(tableName);
      queryTable.setOwner(owner);
      return queryTable;
   }

   public static ConditionEntity prepareConditionEntity (QueryTable queryTable, QueryColumn queryColumn,
            QueryTable rhsQueryTable, QueryColumn rhsQueryColumn) {
      return prepareConditionEntity(queryTable, queryColumn, rhsQueryTable, rhsQueryColumn, OperatorType.EQUALS);
   }

   public static ConditionEntity prepareConditionEntity (QueryTable queryTable, QueryColumn queryColumn,
            String queryValueString) {
      return prepareConditionEntity(queryTable, queryColumn, queryValueString, OperatorType.EQUALS);
   }

   public static ConditionEntity prepareConditionEntity (QueryTable queryTable, QueryColumn queryColumn,
            String queryValueString, OperatorType operatorType) {
      List<String> rhsQueryValues = new ArrayList<String>();
      rhsQueryValues.add(queryValueString);
      return ExecueBeanManagementUtil.prepareConditionEntity(queryTable, queryColumn, rhsQueryValues, operatorType);
   }

   public static ConditionEntity prepareConditionEntity (QueryTable queryTable, QueryColumn queryColumn,
            List<String> rhsQueryValues) {
      if (rhsQueryValues.size() == 1) {
         return prepareConditionEntity(queryTable, queryColumn, rhsQueryValues.get(0));
      } else {
         return ExecueBeanManagementUtil.prepareConditionEntity(queryTable, queryColumn, rhsQueryValues,
                  OperatorType.IN);
      }
   }

   public static List<String> getMemberValues (List<Membr> members) {
      List<String> memberValues = new ArrayList<String>();
      for (Membr membr : members) {
         memberValues.add(membr.getLookupValue());
      }
      return memberValues;
   }

   public static ConditionEntity prepareConditionEntity (QueryTable queryTable, QueryColumn queryColumn,
            QueryTable rhsQueryTable, QueryColumn rhsQueryColumn, OperatorType operatorType) {
      QueryTableColumn queryTableColumn = ExecueBeanManagementUtil.prepareQueryTableColumn(queryColumn, queryTable);
      QueryTableColumn rhsQueryTableColumn = ExecueBeanManagementUtil.prepareQueryTableColumn(rhsQueryColumn,
               rhsQueryTable);
      ConditionEntity conditionEntity = new ConditionEntity();
      conditionEntity.setLhsTableColumn(queryTableColumn);
      conditionEntity.setOperator(operatorType);
      List<QueryTableColumn> rhsQueryTableColumns = new ArrayList<QueryTableColumn>();
      rhsQueryTableColumns.add(rhsQueryTableColumn);
      conditionEntity.setRhsTableColumns(rhsQueryTableColumns);
      conditionEntity.setOperandType(QueryConditionOperandType.TABLE_COLUMN);
      return conditionEntity;
   }

   public static SourceAssetMappingInfo prepareSourceAssetMappingInfo (Asset sourceAsset,
            ConceptColumnMapping populatedPopulation, List<ConceptColumnMapping> populatedDistributions,
            List<ConceptColumnMapping> populatedProminentMeasures,
            List<ConceptColumnMapping> populatedProminentDimensions) {
      SourceAssetMappingInfo sourceAssetMappingInfo = new SourceAssetMappingInfo();
      sourceAssetMappingInfo.setSourceAsset(sourceAsset);
      sourceAssetMappingInfo.setPopulatedPopulation(populatedPopulation);
      sourceAssetMappingInfo.setPopulatedDistributions(populatedDistributions);
      sourceAssetMappingInfo.setPopulatedProminentDimensions(populatedProminentDimensions);
      sourceAssetMappingInfo.setPopulatedProminentMeasures(populatedProminentMeasures);
      return sourceAssetMappingInfo;
   }

   public static ConceptColumnMapping getMatchedDimensionMapping (String dimensionName,
            List<ConceptColumnMapping> dimensionMappings) {
      ConceptColumnMapping matchedMapping = null;
      for (ConceptColumnMapping columnMapping : dimensionMappings) {
         if (columnMapping.getConcept().getName().equalsIgnoreCase(dimensionName)) {
            matchedMapping = columnMapping;
            break;
         }
      }
      return matchedMapping;
   }

   public static ConceptColumnMapping prepareConceptColumnMapping (Mapping mapping) {
      ConceptColumnMapping conceptColumnMapping = new ConceptColumnMapping();
      conceptColumnMapping.setMapping(mapping);
      conceptColumnMapping.setAssetEntityDefinition(mapping.getAssetEntityDefinition());
      conceptColumnMapping.setBusinessEntityDefinition(mapping.getBusinessEntityDefinition());
      conceptColumnMapping.setConcept(mapping.getBusinessEntityDefinition().getConcept());
      conceptColumnMapping.setColumn(mapping.getAssetEntityDefinition().getColum());
      conceptColumnMapping.setTabl(mapping.getAssetEntityDefinition().getTabl());
      QueryColumn queryColumn = ExecueBeanManagementUtil.prepareQueryColumn(mapping.getAssetEntityDefinition()
               .getColum());
      QueryTable queryTable = ExecueBeanManagementUtil.prepareQueryTable(mapping.getAssetEntityDefinition().getTabl());
      QueryTableColumn queryTableColumn = ExecueBeanManagementUtil.prepareQueryTableColumn(queryColumn, queryTable);
      conceptColumnMapping.setQueryTable(queryTable);
      conceptColumnMapping.setQueryColumn(queryColumn);
      conceptColumnMapping.setQueryTableColumn(queryTableColumn);
      return conceptColumnMapping;
   }

   public static Join prepareJoin (Join join, Asset martAsset, Map<String, String> sourceMartTableNameMap) {
      Join clonedJoin = new Join();
      clonedJoin.setAsset(martAsset);
      clonedJoin.setSourceTableName(sourceMartTableNameMap.get(join.getSourceTableName()));
      clonedJoin.setDestTableName(sourceMartTableNameMap.get(join.getDestTableName()));
      clonedJoin.setJoinLength(join.getJoinLength());
      clonedJoin.setJoinOrder(join.getJoinOrder());
      return clonedJoin;
   }

   public static Set<JoinDefinition> prepareJoinDefinition (List<JoinDefinition> joinDefinitions, Asset martAsset,
            Map<String, String> sourceMartTableNameMap) {
      Set<JoinDefinition> clonedJoinDefinitions = new HashSet<JoinDefinition>();
      for (JoinDefinition joinDefinition : joinDefinitions) {
         JoinDefinition clonedJoinDefinition = new JoinDefinition();
         clonedJoinDefinition.setAsset(martAsset);
         clonedJoinDefinition.setType(joinDefinition.getType());
         clonedJoinDefinition.setSourceTableName(sourceMartTableNameMap.get(joinDefinition.getSourceTableName()));
         clonedJoinDefinition.setDestTableName(sourceMartTableNameMap.get(joinDefinition.getDestTableName()));
         clonedJoinDefinition.setSourceColumnName(joinDefinition.getSourceColumnName());
         clonedJoinDefinition.setDestColumnName(joinDefinition.getDestColumnName());
         clonedJoinDefinitions.add(clonedJoinDefinition);
      }
      return clonedJoinDefinitions;
   }

   public static BusinessEntityTerm prepareBusinessEntityTerm (BusinessEntityDefinition conceptBED) {
      BusinessEntityTerm businessEntityTerm = new BusinessEntityTerm();
      businessEntityTerm.setBusinessEntity(conceptBED.getConcept());
      businessEntityTerm.setBusinessEntityType(BusinessEntityType.CONCEPT);
      businessEntityTerm.setBusinessEntityDefinitionId(conceptBED.getId());
      return businessEntityTerm;
   }

   public static CubeSourceColumnMapping prepareCubeSourceColumnMapping (QueryColumn queryColumn,
            ConceptColumnMapping sourceConceptColumnMapping) {
      CubeSourceColumnMapping cubeSourceColumnMapping = new CubeSourceColumnMapping();
      cubeSourceColumnMapping.setQueryColumn(queryColumn);
      cubeSourceColumnMapping.setSourceConceptColumnMapping(sourceConceptColumnMapping);
      return cubeSourceColumnMapping;
   }

   public static Integer getSQLDataType (QueryColumn queryColumn) {
      return ExecueBeanUtil.getSQLDataType(queryColumn.getDataType());
   }

   public static Integer getSQLDataType (Colum column) {
      return ExecueBeanUtil.getSQLDataType(column.getDataType());
   }

   public static Double getRoundedValue (Double currentValue, Integer maxDecimalDigits) {
      String format = "%." + maxDecimalDigits + "f";
      Double formattedValue = currentValue;
      if (currentValue != null) {
         formattedValue = Double.parseDouble(String.format(format, currentValue));
      }
      return formattedValue;
   }

   public static QueryColumn getRoundFunctionTargetColumn (DataType dataType, int precision, int scale) {
      QueryColumn roundFunctionTargetColumn = new QueryColumn();
      roundFunctionTargetColumn.setDataType(dataType);
      roundFunctionTargetColumn.setPrecision(precision);
      roundFunctionTargetColumn.setScale(scale);
      return roundFunctionTargetColumn;
   }
}