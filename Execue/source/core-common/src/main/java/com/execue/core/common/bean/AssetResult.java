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


package com.execue.core.common.bean;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.execue.core.common.bean.governor.AssetQuery;
import com.execue.core.common.bean.uss.UniversalSearchResultItemType;
import com.execue.core.common.type.AggregateQueryType;
import com.execue.core.common.type.AssetType;

/**
 * @author kaliki
 * @since 4.0
 */
public class AssetResult {

   private long                          id;
   private List<Long>                    aggregateQueryId;
   private Long                          assetId;
   private Long                          businessQueryId;
   private long                          messageId;
   private String                        name;
   private String                        description;
   private List<ReportGroupResult>       reportGroupList;
   private String                        query;
   private String                        reportStatus;
   private AssetType                     assetType;
   private double                        relevance;
   private String                        reportHeader;
   private String                        headerUrl;
   private String                        imageUrl;
   private String                        pseudoStatement;
   private boolean                       dataPresent                      = false;
   private boolean                       resultFromBusinessAggregateQuery = true;
   private String                        error;
   private AssetQuery                    assetQuery;
   private String                        shortNote;
   private Long                          assetDetailId;
   private Long                          appOwnerId;
   private String                        appOwnerName;
   private boolean                       fromQueryCache;
   private String                        cachedDate;
   private String                        aggregatedQueryIdsList;
   private String                        unstructuredContentDate;
   private UniversalSearchResultItemType resultItemType;
   private AggregateQueryType            aggregateQueryType;
   private List<Long>                    hierarchySummaryIds;

   public Long getAppOwnerId () {
      return appOwnerId;
   }

   public void setAppOwnerId (Long appOwnerId) {
      this.appOwnerId = appOwnerId;
   }

   public String getAppOwnerName () {
      return appOwnerName;
   }

   public void setAppOwnerName (String appOwnerName) {
      this.appOwnerName = appOwnerName;
   }

   public AssetQuery getAssetQuery () {
      return assetQuery;
   }

   public void setAssetQuery (AssetQuery assetQuery) {
      this.assetQuery = assetQuery;
   }

   public long getId () {
      return id;
   }

   public void setId (long id) {
      this.id = id;
   }

   public String getName () {
      return name;
   }

   public void setName (String name) {
      this.name = name;
   }

   public List<ReportGroupResult> getReportGroupList () {
      return reportGroupList;
   }

   public void setReportGroupList (List<ReportGroupResult> reportGroupList) {
      this.reportGroupList = reportGroupList;
   }

   public String getQuery () {
      return query;
   }

   public void setQuery (String query) {
      this.query = query;
   }

   public String getReportStatus () {
      return reportStatus;
   }

   public void setReportStatus (String reportStatus) {
      this.reportStatus = reportStatus;
   }

   public AssetType getAssetType () {
      return assetType;
   }

   public void setAssetType (AssetType assetType) {
      this.assetType = assetType;
   }

   public double getRelevance () {
      return relevance;
   }

   public void setRelevance (double relevance) {
      this.relevance = relevance;
   }

   public String getReportHeader () {
      return reportHeader;
   }

   public void setReportHeader (String reportHeader) {
      this.reportHeader = reportHeader;
   }

   public String getPseudoStatement () {
      return pseudoStatement;
   }

   public void setPseudoStatement (String pseudoStatement) {
      this.pseudoStatement = pseudoStatement;
   }

   public String getDescription () {
      return description;
   }

   public void setDescription (String description) {
      this.description = description;
   }

   public boolean isDataPresent () {
      return dataPresent;
   }

   public void setDataPresent (boolean dataPresent) {
      this.dataPresent = dataPresent;
   }

   public String getError () {
      return error;
   }

   public void setError (String error) {
      this.error = error;
   }

   public long getMessageId () {
      return messageId;
   }

   public void setMessageId (long messageId) {
      this.messageId = messageId;
   }

   public Long getAssetId () {
      return assetId;
   }

   public void setAssetId (Long assetId) {
      this.assetId = assetId;
   }

   public Long getBusinessQueryId () {
      return businessQueryId;
   }

   public void setBusinessQueryId (Long businessQueryId) {
      this.businessQueryId = businessQueryId;
   }

   public boolean isResultFromBusinessAggregateQuery () {
      return resultFromBusinessAggregateQuery;
   }

   public void setResultFromBusinessAggregateQuery (boolean resultFromBusinessAggregateQuery) {
      this.resultFromBusinessAggregateQuery = resultFromBusinessAggregateQuery;
   }

   /**
    * @return the shortNote
    */
   public String getShortNote () {
      return shortNote;
   }

   /**
    * @param shortNote
    *           the shortNote to set
    */
   public void setShortNote (String shortNote) {
      this.shortNote = shortNote;
   }

   public Long getAssetDetailId () {
      return assetDetailId;
   }

   public void setAssetDetailId (Long assetDetailId) {
      this.assetDetailId = assetDetailId;
   }

   public List<Long> getAggregateQueryId () {
      return aggregateQueryId;
   }

   public void setAggregateQueryId (List<Long> aggregateQueryId) {
      this.aggregateQueryId = aggregateQueryId;
   }

   public boolean isFromQueryCache () {
      return fromQueryCache;
   }

   public void setFromQueryCache (boolean fromQueryCache) {
      this.fromQueryCache = fromQueryCache;
   }

   public String getAggregatedQueryIdsList () {
      aggregatedQueryIdsList = StringUtils.join(aggregateQueryId, ",");
      return aggregatedQueryIdsList;
   }

   public String getCachedDate () {
      return cachedDate;
   }

   public void setCachedDate (String cachedDate) {
      this.cachedDate = cachedDate;
   }

   public String getHeaderUrl () {
      return headerUrl;
   }

   public void setHeaderUrl (String headerUrl) {
      this.headerUrl = headerUrl;
   }

   public String getImageUrl () {
      return imageUrl;
   }

   public void setImageUrl (String imageUrl) {
      this.imageUrl = imageUrl;
   }

   /**
    * @return the unstructuredContentDate
    */
   public String getUnstructuredContentDate () {
      return unstructuredContentDate;
   }

   /**
    * @param unstructuredContentDate
    *           the unstructuredContentDate to set
    */
   public void setUnstructuredContentDate (String unstructuredContentDate) {
      this.unstructuredContentDate = unstructuredContentDate;
   }

   /**
    * @return the resultItemType
    */
   public UniversalSearchResultItemType getResultItemType () {
      return resultItemType;
   }

   /**
    * @param resultItemType the resultItemType to set
    */
   public void setResultItemType (UniversalSearchResultItemType resultItemType) {
      this.resultItemType = resultItemType;
   }

   public AggregateQueryType getAggregateQueryType () {
      return aggregateQueryType;
   }

   public void setAggregateQueryType (AggregateQueryType aggregateQueryType) {
      this.aggregateQueryType = aggregateQueryType;
   }

   /**
    * @return the hierarchySummaryIds
    */
   public List<Long> getHierarchySummaryIds () {
      return hierarchySummaryIds;
   }

   /**
    * @param hierarchySummaryIds the hierarchySummaryIds to set
    */
   public void setHierarchySummaryIds (List<Long> hierarchySummaryIds) {
      this.hierarchySummaryIds = hierarchySummaryIds;
   }

}
