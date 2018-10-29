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

import com.execue.core.common.type.ReportType;

/**
 * This bean class holds the information about all the attributes that are required for selecting the report types
 * 
 * @author John Mallavalli
 */
public class ReportSelection {

   private boolean          dataBrowser;

   private boolean          detailPath;

   /**
    * Number of records return for the data source
    */
   private int              numberOfRecords;                // From Physical Query
   /**
    * Number of dimensions
    */
   private int              numberOfGroups;                 // size of summarizations on SQ
   /**
    * Number of dimensions - single variant groups
    */
   private int              numberOfEffectiveGroups;        // numberOfGroups - single variant groups ( check where
   // condition)
   /**
    * Number of dimensions
    */
   private int              numberOfDimensions;             // metrics from SQ

   /**
    * Number of measures
    */
   private int              numberOfMeasures;               // metrics from SQ
   /**
    * Number of ID columns
    */
   private int              numberOfIdColumns;              // metrics from SQ
   /**
    * group combination(multiple no. member of a each group) multiplied by number of measures Ex. query having 2
    * dimensions(D1- x members, D2 - y members) and 3 measures (M1, M2, M3) group combination: x multiplied by y
    * dataPoints : group combination multiplied by 3 (no of measures)
    */
   private int              dataPoints;
   /**
    * group combination
    */
   private int              numberOfEffectiveRecords;       // same as number
   // Added by John - data structure to hold the report types after the selection logic is applied
   private List<ReportType> reportTypes;                    // report types that are possible after running the
   // selection

   // TOP/BOTTOM type or not
   private boolean          isTopBottom;

   private Long             applicationId;

   private boolean          profilePresent;

   private int              numberOfDimensionOfLocationType;

   private String           title;

   private boolean          eligibleForPortraitReport;

   public boolean isEligibleForPortraitReport () {
      return eligibleForPortraitReport;
   }

   public void setEligibleForPortraitReport (boolean eligibleForPortraitReport) {
      this.eligibleForPortraitReport = eligibleForPortraitReport;
   }

   public String getTitle () {
      return title;
   }

   public void setTitle (String title) {
      this.title = title;
   }

   public boolean isProfilePresent () {
      return profilePresent;
   }

   public void setProfilePresent (boolean profilePresent) {
      this.profilePresent = profilePresent;
   }

   public Long getApplicationId () {
      return applicationId;
   }

   public void setApplicationId (Long applicationId) {
      this.applicationId = applicationId;
   }

   public boolean isTopBottom () {
      return isTopBottom;
   }

   public void setTopBottom (boolean isTopBottom) {
      this.isTopBottom = isTopBottom;
   }

   public int getNumberOfRecords () {
      return numberOfRecords;
   }

   public void setNumberOfRecords (int numberOfRecords) {
      this.numberOfRecords = numberOfRecords;
   }

   public int getNumberOfGroups () {
      return numberOfGroups;
   }

   public void setNumberOfGroups (int numberOfGroups) {
      this.numberOfGroups = numberOfGroups;
   }

   public int getNumberOfEffectiveGroups () {
      return numberOfEffectiveGroups;
   }

   public void setNumberOfEffectiveGroups (int numberOfEffectiveGroups) {
      this.numberOfEffectiveGroups = numberOfEffectiveGroups;
   }

   public int getNumberOfDimensions () {
      return numberOfDimensions;
   }

   public void setNumberOfDimensions (int numberOfDimensions) {
      this.numberOfDimensions = numberOfDimensions;
   }

   public int getNumberOfMeasures () {
      return numberOfMeasures;
   }

   public void setNumberOfMeasures (int numberOfMeasures) {
      this.numberOfMeasures = numberOfMeasures;
   }

   public int getDataPoints () {
      return dataPoints;
   }

   public void setDataPoints (int dataPoints) {
      this.dataPoints = dataPoints;
   }

   public int getNumberOfEffectiveRecords () {
      return numberOfEffectiveRecords;
   }

   public void setNumberOfEffectiveRecords (int numberOfEffectiveRecords) {
      this.numberOfEffectiveRecords = numberOfEffectiveRecords;
   }

   public List<ReportType> getReportTypes () {
      return reportTypes;
   }

   public void setReportTypes (List<ReportType> reportTypes) {
      this.reportTypes = reportTypes;
   }

   public int getNumberOfIdColumns () {
      return numberOfIdColumns;
   }

   public void setNumberOfIdColumns (int numberOfIdColumns) {
      this.numberOfIdColumns = numberOfIdColumns;
   }

   public boolean isDataBrowser () {
      return dataBrowser;
   }

   public void setDataBrowser (boolean dataBrowser) {
      this.dataBrowser = dataBrowser;
   }

   public int getNumberOfDimensionOfLocationType () {
      return numberOfDimensionOfLocationType;
   }

   public void setNumberOfDimensionOfLocationType (int numberOfDimensionOfLocationType) {
      this.numberOfDimensionOfLocationType = numberOfDimensionOfLocationType;
   }

   public boolean isDetailPath () {
      return detailPath;
   }

   public void setDetailPath (boolean detailPath) {
      this.detailPath = detailPath;
   }
}