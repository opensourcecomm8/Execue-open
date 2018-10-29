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

import com.execue.core.common.bean.governor.AssetQuery;
import com.execue.core.common.bean.querygen.QueryRepresentation;
import com.execue.core.common.type.AggregateQueryType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.ReportType;

/**
 * @author kaliki
 * @since 4.0
 */

public class AggregateQuery {

   private AssetQuery          assetQuery;
   private List<ReportType>    reportTypes;
   private String              xmlData;
   private String              reportMetaInfoStructure;
   private QueryRepresentation queryRepresentation;
   // set this flag to false when no data is returned by the asset
   private CheckType           dataPresent   = CheckType.NO;
   private CheckType           dataExtracted = CheckType.NO;
   private AggregateQueryType  type;
   private Long                queryExecutionTime;

   public AssetQuery getAssetQuery () {
      return assetQuery;
   }

   public void setAssetQuery (AssetQuery assetQuery) {
      this.assetQuery = assetQuery;
   }

   public List<ReportType> getReportTypes () {
      return reportTypes;
   }

   public void setReportTypes (List<ReportType> reportTypes) {
      this.reportTypes = reportTypes;
   }

   public String getXmlData () {
      return xmlData;
   }

   public void setXmlData (String xmlData) {
      this.xmlData = xmlData;
   }

   public CheckType isDataPresent () {
      return dataPresent;
   }

   public void setDataPresent (CheckType dataPresent) {
      this.dataPresent = dataPresent;
   }

   public QueryRepresentation getQueryRepresentation () {
      return queryRepresentation;
   }

   public void setQueryRepresentation (QueryRepresentation queryRepresentation) {
      this.queryRepresentation = queryRepresentation;
   }

   public AggregateQueryType getType () {
      return type;
   }

   public void setType (AggregateQueryType type) {
      this.type = type;
   }

   public CheckType isDataExtracted () {
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

   public Long getQueryExecutionTime () {
      return queryExecutionTime;
   }

   public void setQueryExecutionTime (Long queryExecutionTime) {
      this.queryExecutionTime = queryExecutionTime;
   }
}