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


package com.execue.repoting.aggregation.bean;

import java.util.Set;

import com.execue.core.common.bean.BusinessStat;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.governor.AssetEntityTerm;
import com.execue.core.common.bean.governor.BusinessAssetTerm;
import com.execue.core.common.type.AssetEntityType;
import com.execue.core.common.type.ColumnType;

/**
 * This bean class holds a column entity which contains the business asset term and the other attributes of a column
 * like type, statistics etc
 * 
 * @author kaliki
 * @since 4.0
 */
public class ReportColumnInfo {

   private final BusinessAssetTerm bizAssetTerm;
   private ColumnType              columnType;
   private ColumnType              detailReportColumnType;
   private String                  minimumValue;
   private String                  maximumValue;
   private long                    countSize = -1;
   private Set<BusinessStat>       businessStats;
   private boolean                 isColumnTypeDeduced;
   private boolean                 isDetailReportColumnTypeDeduced;
   private boolean                 areStatsDeduced;
   private boolean                 countRequired;
   private boolean                 isEligibleForRanges;
   private boolean                 markedForRangeDerivation;
   private Colum                   column;
   private String                  columnName;
   private Concept                 concept;
   private String                  univariantValue;
   private boolean                 isUserRequestedSummarization;

   public boolean isUserRequestedSummarization () {
      return isUserRequestedSummarization;
   }

   public void setUserRequestedSummarization (boolean isUserRequestedSummarization) {
      this.isUserRequestedSummarization = isUserRequestedSummarization;
   }

   public ReportColumnInfo (BusinessAssetTerm bizAssetTerm) {
      this.bizAssetTerm = bizAssetTerm;
      this.setColumnType(getDefaultColumnType());
      this.setDetailReportColumnType(getColumnType());
   }

   private ColumnType getDefaultColumnType () {
      ColumnType defaultColumnType = null;
      AssetEntityTerm assetTerm = bizAssetTerm.getAssetEntityTerm();
      if (AssetEntityType.COLUMN.equals(assetTerm.getAssetEntityType())) {
         defaultColumnType = ((Colum) assetTerm.getAssetEntity()).getKdxDataType();
      }
      return defaultColumnType;
   }

   public ColumnType getColumnType () {
      return columnType;
   }

   private void setColumnType (ColumnType columnType) {
      this.columnType = columnType;
   }

   /**
    * This mutator method modifies the column type ONLY if the deduced column type is different from the original column
    * type and if the change happens it sets the column type deduced flag
    */
   public void modifyColumnType (ColumnType deducedColumnType) {
      if (this.columnType != deducedColumnType) {
         setColumnTypeDeduced(true);
         setColumnType(deducedColumnType);
      }
   }

   /**
    * This mutator method modifies the column type ONLY if the deduced column type is different from the original column
    * type and if the change happens it sets the column type deduced flag
    */
   public void modifyDetailReportColumnType (ColumnType deducedDetailReportColumnType) {
      if (this.detailReportColumnType != deducedDetailReportColumnType) {
         setDetailReportColumnTypeDeduced(true);
         setDetailReportColumnType(deducedDetailReportColumnType);
      }
   }

   public long getCountSize () {
      return countSize;
   }

   public void setCountSize (long countSize) {
      this.countSize = countSize;
   }

   public Set<BusinessStat> getBusinessStats () {
      return businessStats;
   }

   public void setBusinessStats (Set<BusinessStat> businessStats) {
      this.businessStats = businessStats;
   }

   public BusinessAssetTerm getBizAssetTerm () {
      return bizAssetTerm;
   }

   public boolean isColumnTypeDeduced () {
      return isColumnTypeDeduced;
   }

   public void setColumnTypeDeduced (boolean isColumnTypeDeduced) {
      this.isColumnTypeDeduced = isColumnTypeDeduced;
   }

   public boolean areStatsDeduced () {
      return areStatsDeduced;
   }

   public void setStatsDeduced (boolean areStatsDeduced) {
      this.areStatsDeduced = areStatsDeduced;
   }

   public String getMinimumValue () {
      return minimumValue;
   }

   public void setMinimumValue (String minimumValue) {
      this.minimumValue = minimumValue;
   }

   public String getMaximumValue () {
      return maximumValue;
   }

   public void setMaximumValue (String maximumValue) {
      this.maximumValue = maximumValue;
   }

   public boolean isCountRequired () {
      return countRequired;
   }

   public void setCountRequired (boolean countRequired) {
      this.countRequired = countRequired;
   }

   public boolean isEligibleForRanges () {
      return isEligibleForRanges;
   }

   public void setEligibleForRanges (boolean isEligibleForRanges) {
      this.isEligibleForRanges = isEligibleForRanges;
   }

   public boolean isMarkedForRangeDerivation () {
      return markedForRangeDerivation;
   }

   public void setMarkedForRangeDerivation (boolean markedForRangeDerivation) {
      this.markedForRangeDerivation = markedForRangeDerivation;
   }

   public Colum getColumn () {
      if (this.bizAssetTerm != null) {
         column = ((Colum) bizAssetTerm.getAssetEntityTerm().getAssetEntity());
         columnName = column.getName();
      }
      return column;
   }

   public String getColumnName () {
      return columnName;
   }

   public Concept getConcept () {
      if (this.bizAssetTerm != null) {
         concept = ((Concept) bizAssetTerm.getBusinessTerm().getBusinessEntityTerm().getBusinessEntity());
      }
      return concept;
   }

   public String getUnivariantValue () {
      return univariantValue;
   }

   public void setUnivariantValue (String univariantValue) {
      this.univariantValue = univariantValue;
   }

   public ColumnType getDetailReportColumnType () {
      return detailReportColumnType;
   }

   public void setDetailReportColumnType (ColumnType detailReportColumnType) {
      this.detailReportColumnType = detailReportColumnType;
   }

   public boolean isDetailReportColumnTypeDeduced () {
      return isDetailReportColumnTypeDeduced;
   }

   public void setDetailReportColumnTypeDeduced (boolean isDetailReportColumnTypeDeduced) {
      this.isDetailReportColumnTypeDeduced = isDetailReportColumnTypeDeduced;
   }
}