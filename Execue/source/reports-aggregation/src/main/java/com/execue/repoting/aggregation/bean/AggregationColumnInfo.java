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

import java.util.List;

import com.execue.core.common.type.ColumnType;
import com.execue.core.common.type.StatType;

/**
 * @author John Mallavalli
 */
public class AggregationColumnInfo {

   private AggregationBusinessAssetTerm bizAssetTerm;
   private List<StatType>               businessStatTypes;
   private ColumnType                   columnType;
   private String                       minimumValue;
   private String                       maximumValue;
   private long                         countSize = -1;
   private boolean                      isColumnTypeDeduced;
   private boolean                      countRequired;
   private boolean                      statsDeduced;
   private boolean                      isEligibleForRanges;
   private boolean                      markedForRangeDerivation;
   private String                       univariantValue;
   private boolean                      isUserRequestedSummarization;

   public boolean isUserRequestedSummarization () {
      return isUserRequestedSummarization;
   }

   public void setUserRequestedSummarization (boolean isUserRequestedSummarization) {
      this.isUserRequestedSummarization = isUserRequestedSummarization;
   }

   public void setBizAssetTerm (AggregationBusinessAssetTerm bizAssetTerm) {
      this.bizAssetTerm = bizAssetTerm;
   }

   public AggregationBusinessAssetTerm getBizAssetTerm () {
      return bizAssetTerm;
   }

   public ColumnType getColumnType () {
      return columnType;
   }

   public void setColumnType (ColumnType columnType) {
      this.columnType = columnType;
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

   public long getCountSize () {
      return countSize;
   }

   public void setCountSize (long countSize) {
      this.countSize = countSize;
   }

   public boolean isColumnTypeDeduced () {
      return isColumnTypeDeduced;
   }

   public void setColumnTypeDeduced (boolean isColumnTypeDeduced) {
      this.isColumnTypeDeduced = isColumnTypeDeduced;
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

   public String getUnivariantValue () {
      return univariantValue;
   }

   public void setUnivariantValue (String univariantValue) {
      this.univariantValue = univariantValue;
   }

   /**
    * @return the businessStatTypes
    */
   public List<StatType> getBusinessStatTypes () {
      return businessStatTypes;
   }

   /**
    * @param businessStatTypes the businessStatTypes to set
    */
   public void setBusinessStatTypes (List<StatType> businessStatTypes) {
      this.businessStatTypes = businessStatTypes;
   }

   /**
    * @return the statsDeduced
    */
   public boolean isStatsDeduced () {
      return statsDeduced;
   }

   /**
    * @param statsDeduced the statsDeduced to set
    */
   public void setStatsDeduced (boolean statsDeduced) {
      this.statsDeduced = statsDeduced;
   }
}