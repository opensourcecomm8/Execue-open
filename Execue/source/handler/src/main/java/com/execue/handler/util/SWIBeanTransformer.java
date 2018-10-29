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


package com.execue.handler.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.Join;
import com.execue.core.common.bean.entity.JoinDefinition;
import com.execue.core.common.bean.entity.Tabl;
import com.execue.core.common.type.JoinType;
import com.execue.handler.bean.UIColumn;
import com.execue.handler.bean.UIColumnForJoins;
import com.execue.handler.bean.UIJoinDefintionInfo;
import com.execue.handler.bean.UIJoinInfo;
import com.execue.handler.bean.UITable;
import com.execue.handler.bean.UITableForJoins;

/**
 * Provides methods to transform the beans from UI wrappers to Domain Objects or vice-versa
 * 
 * @author Raju Gottumukkala
 */
public class SWIBeanTransformer {

   private SWIBeanTransformer () {
      // Nothing {to make the class accessible as static only}
   }

   // UIJoinInfo to Join
   public static List<Join> getJoins (List<UIJoinInfo> uiJoins) {
      List<Join> joins = new ArrayList<Join>();
      for (UIJoinInfo uiJoin : uiJoins) {
         Join join = new Join();
         join.setSourceTableName(uiJoin.getLhsTableName());
         join.setDestTableName(uiJoin.getRhsTableName());
         joins.add(join);
      }
      return joins;
   }

   // Join to UIJoinInfo
   public static List<UIJoinInfo> getUIJoinInfo (Map<String, String> tableNameDisplayNameMap, List<Join> joins) {
      List<UIJoinInfo> uiJoins = new ArrayList<UIJoinInfo>();
      for (Join join : joins) {
         UIJoinInfo uiJoin = new UIJoinInfo();
         uiJoin.setLhsTableName(join.getSourceTableName());
         uiJoin.setLhsTableDisplayName(tableNameDisplayNameMap.get(join.getSourceTableName()));
         uiJoin.setRhsTableName(join.getDestTableName());
         uiJoin.setRhsTableDisplayName(tableNameDisplayNameMap.get(join.getDestTableName()));
         uiJoins.add(uiJoin);
      }
      return uiJoins;
   }

   // JoinDefinition to UIJoinDefintionInfo
   public static List<UIJoinDefintionInfo> getUIJoinDefinitions (List<JoinDefinition> joinDefinitions) {
      List<UIJoinDefintionInfo> uiJoindefinitions = new ArrayList<UIJoinDefintionInfo>();
      for (JoinDefinition joinDefinition : joinDefinitions) {
         UIJoinDefintionInfo uiJoinDefintionInfo = new UIJoinDefintionInfo();
         uiJoinDefintionInfo.setLhsColumn(joinDefinition.getSourceColumnName());
         uiJoinDefintionInfo.setRhsColumn(joinDefinition.getDestColumnName());
         uiJoinDefintionInfo.setType(joinDefinition.getType().getValue().trim());
         uiJoindefinitions.add(uiJoinDefintionInfo);
      }
      return uiJoindefinitions;
   }

   // UIJoinDefintionInfo to JoinDefinition
   public static List<JoinDefinition> getJoinDefinitions (List<UIJoinDefintionInfo> uiJoinDefinitions) {
      List<JoinDefinition> joinDefinitions = new ArrayList<JoinDefinition>();
      for (UIJoinDefintionInfo uiJoinDefinition : uiJoinDefinitions) {
         JoinDefinition joinDefinition = new JoinDefinition();
         joinDefinition.setSourceColumnName(uiJoinDefinition.getLhsColumn());
         joinDefinition.setDestColumnName(uiJoinDefinition.getRhsColumn());
         // TODO: -JVK- remove later - this is a hot fix
         String joinType = uiJoinDefinition.getType().trim();//.toUpperCase() + " JOIN";
         joinDefinition.setType(JoinType.getType(joinType));
         joinDefinitions.add(joinDefinition);
      }
      return joinDefinitions;
   }

   // TableInfo to UITableForJoins
   public static List<UITableForJoins> getUITablesForJoins (List<Tabl> assetTables) {
      List<UITableForJoins> uiAssetTables = new ArrayList<UITableForJoins>();
      for (Tabl tabl : assetTables) {
         UITableForJoins uiTableForJoins = new UITableForJoins();
         uiTableForJoins.setTableId(tabl.getId());
         uiTableForJoins.setTableDisplayName(tabl.getDisplayName());
         uiTableForJoins.setTableName(tabl.getName());
         uiTableForJoins.setVirtual(tabl.getVirtual());
         uiAssetTables.add(uiTableForJoins);
      }
      return uiAssetTables;
   }

   // Colum to UIColumnForJoins
   public static List<UIColumnForJoins> getUIColumnsForColums (List<Colum> joinColums) {
      List<UIColumnForJoins> uiJoinColumns = new ArrayList<UIColumnForJoins>();
      for (Colum colum : joinColums) {
         UIColumnForJoins uiColumnForJoins = new UIColumnForJoins();
         uiColumnForJoins.setColumnId(colum.getId());
         uiColumnForJoins.setColumnName(colum.getName());
         uiJoinColumns.add(uiColumnForJoins);
      }
      return uiJoinColumns;
   }

   public static List<UITable> getUITables (List<Tabl> tables) {
      List<UITable> uiTables = new ArrayList<UITable>();
      for (Tabl tabl : tables) {
         uiTables.add(getUITable(tabl));
      }
      return uiTables;
   }

   public static List<Tabl> getTables (List<UITable> uiTables) {
      List<Tabl> tables = new ArrayList<Tabl>();
      for (UITable uiTable : uiTables) {
         tables.add(getTable(uiTable));
      }
      return tables;
   }

   public static List<UIColumn> getUIColums (List<Colum> colums) {
      List<UIColumn> uiColums = new ArrayList<UIColumn>();
      for (Colum colum : colums) {
         uiColums.add(getUIColumn(colum));
      }
      return uiColums;
   }

   public static List<Colum> getColums (List<UIColumn> uiColums) {
      List<Colum> colums = new ArrayList<Colum>();
      for (UIColumn uiColumn : uiColums) {
         colums.add(getColum(uiColumn));
      }
      return colums;
   }

   public static UITable getUITable (Tabl tabl) {
      UITable uiTable = new UITable();
      uiTable.setId(tabl.getId());
      uiTable.setName(tabl.getName());
      uiTable.setEligibleSystemDefaultMetric(tabl.getEligibleDefaultMetric());
      uiTable.setDisplayName(tabl.getDisplayName());
      uiTable.setDescription(tabl.getDescription());
      return uiTable;
   }

   public static Tabl getTable (UITable uiTable) {
      Tabl tabl = new Tabl();
      tabl.setId(uiTable.getId());
      tabl.setName(uiTable.getName());
      tabl.setDisplayName(uiTable.getDisplayName());
      tabl.setDescription(uiTable.getDescription());
      tabl.setEligibleDefaultMetric(uiTable.getEligibleSystemDefaultMetric());
      return tabl;
   }

   public static UIColumn getUIColumn (Colum colum) {
      UIColumn uiColumn = new UIColumn();
      uiColumn.setName(colum.getName());
      uiColumn.setId(colum.getId());
      uiColumn.setDescription(colum.getDescription());
      uiColumn.setDataType(colum.getDataType());
      uiColumn.setColumnType(colum.getKdxDataType());
      return uiColumn;
   }

   public static Colum getColum (UIColumn uiColumn) {
      Colum colum = new Colum();
      colum.setId(uiColumn.getId());
      colum.setName(uiColumn.getName());
      colum.setDescription(uiColumn.getDescription());
      colum.setDataType(uiColumn.getDataType());
      colum.setKdxDataType(uiColumn.getColumnType());
      return colum;
   }

   /*
    * public static List<UIConstraint> getUIConstraints (List<Constraint> constraints) { List<UIConstraint>
    * uiConstraints = new ArrayList<UIConstraint>(); for (Constraint constraint : constraints) { UIConstraint
    * uiConstraint = new UIConstraint(); uiConstraint.setConstraintName(constraint.getName());
    * uiConstraint.setId(constraint.getId()); uiConstraint.setConstraintType(constraint.getType());
    * uiConstraint.setDescription(constraint.getDescription());
    * uiConstraint.setReferenceTable(getUITable(constraint.getReferenceTable()));
    * uiConstraint.setReferenceColumn(getUIColumn(constraint.getReferenceColumn()));
    * uiConstraint.setColumOrder(constraint.getColumOrder()); } return uiConstraints; }
    */
}
