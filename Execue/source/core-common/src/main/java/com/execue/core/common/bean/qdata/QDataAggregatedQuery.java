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


package com.execue.core.common.bean.qdata;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.type.AggregateQueryType;
import com.execue.core.common.type.CheckType;

/**
 * This bean represents the aggregated query.
 * 
 * @author Vishay
 * @version 1.0
 * @since 25/06/09
 */
public class QDataAggregatedQuery implements Serializable {

   private Long                            id;
   private AggregateQueryType              type;
   private Date                            executionDate;
   private Double                          relevance   = 0D;
   private Double                          assetWeight = 0D;
   private String                          title;
   private String                          englishQueryString;
   private String                          governorQueryString;
   private String                          aggregatedQueryString;
   private String                          governorQueryStructure;
   private String                          aggregatedQueryStructure;
   private String                          reportMetaInfoStructure;

   private CheckType                       dataPresent;
   private CheckType                       dataExtracted;

   private Long                            assetId;
   private Long                            businessQueryId;
   private transient Asset                 asset;                   // not saved to hibernate
   private QDataUserQuery                  userQuery;
   private QDataBusinessQuery              businessQuery;

   private Set<QDataAggregatedQueryColumn> queryColumns;
   // private Set<QDataReportData> reportDatas;

   private QDataReportData                 reportDatas;
   private Set<QDataAggregatedReportType>  reportTypes;

   private Long                            queryExecutionTime;

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public Date getExecutionDate () {
      return executionDate;
   }

   public void setExecutionDate (Date executionDate) {
      this.executionDate = executionDate;
   }

   public Double getRelevance () {
      return relevance;
   }

   public void setRelevance (Double relevance) {
      this.relevance = relevance;
   }

   public String getTitle () {
      return title;
   }

   public void setTitle (String title) {
      this.title = title;
   }

   public String getEnglishQueryString () {
      return englishQueryString;
   }

   public void setEnglishQueryString (String englishQueryString) {
      this.englishQueryString = englishQueryString;
   }

   public String getGovernorQueryString () {
      return governorQueryString;
   }

   public void setGovernorQueryString (String governorQueryString) {
      this.governorQueryString = governorQueryString;
   }

   public String getAggregatedQueryString () {
      return aggregatedQueryString;
   }

   public void setAggregatedQueryString (String aggregatedQueryString) {
      this.aggregatedQueryString = aggregatedQueryString;
   }

   public QDataUserQuery getUserQuery () {
      return userQuery;
   }

   public void setUserQuery (QDataUserQuery userQuery) {
      this.userQuery = userQuery;
   }

   public QDataBusinessQuery getBusinessQuery () {
      return businessQuery;
   }

   public void setBusinessQuery (QDataBusinessQuery businessQuery) {
      this.businessQuery = businessQuery;
   }

   public Set<QDataAggregatedQueryColumn> getQueryColumns () {
      return queryColumns;
   }

   public void setQueryColumns (Set<QDataAggregatedQueryColumn> queryColumns) {
      this.queryColumns = queryColumns;
   }

   // public Set<QDataReportData> getReportDatas() {
   // return reportDatas;
   // }
   //
   // public void setReportDatas(Set<QDataReportData> reportDatas) {
   // this.reportDatas = reportDatas;
   // }

   public Set<QDataAggregatedReportType> getReportTypes () {
      return reportTypes;
   }

   public void setReportTypes (Set<QDataAggregatedReportType> reportTypes) {
      this.reportTypes = reportTypes;
   }

   public Asset getAsset () {
      return asset;
   }

   public void setAsset (Asset asset) {
      this.asset = asset;
   }

   public Long getAssetId () {
      return assetId;
   }

   public void setAssetId (Long assetId) {
      this.assetId = assetId;
   }

   public QDataReportData getReportDatas () {
      return reportDatas;
   }

   public void setReportDatas (QDataReportData reportDatas) {
      this.reportDatas = reportDatas;
   }

   public CheckType isDataPresent () {
      return dataPresent;
   }

   public void setDataPresent (CheckType dataPresent) {
      this.dataPresent = dataPresent;
   }

   public String getGovernorQueryStructure () {
      return governorQueryStructure;
   }

   public void setGovernorQueryStructure (String governorQueryStructure) {
      this.governorQueryStructure = governorQueryStructure;
   }

   public String getAggregatedQueryStructure () {
      return aggregatedQueryStructure;
   }

   public void setAggregatedQueryStructure (String aggregatedQueryStructure) {
      this.aggregatedQueryStructure = aggregatedQueryStructure;
   }

   public AggregateQueryType getType () {
      return type;
   }

   public void setType (AggregateQueryType type) {
      this.type = type;
   }

   public Long getBusinessQueryId () {
      return businessQueryId;
   }

   public void setBusinessQueryId (Long businessQueryId) {
      this.businessQueryId = businessQueryId;
   }

   public CheckType getDataExtracted () {
      return dataExtracted;
   }

   public void setDataExtracted (CheckType dataExtracted) {
      this.dataExtracted = dataExtracted;
   }

   public String getReportMetaInfoStructure () {
      return reportMetaInfoStructure;
   }

   public void setReportMetaInfoStructure (String reportMetaInfoStructure) {
      this.reportMetaInfoStructure = reportMetaInfoStructure;
   }

   public Double getAssetWeight () {
      return assetWeight;
   }

   public void setAssetWeight (Double assetWeight) {
      this.assetWeight = assetWeight;
   }

   public Long getQueryExecutionTime () {
      return queryExecutionTime;
   }

   public void setQueryExecutionTime (Long queryExecutionTime) {
      this.queryExecutionTime = queryExecutionTime;
   }
}
