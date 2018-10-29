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


package com.execue.repoting.aggregation.helper;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.AggregateQuery;
import com.execue.core.common.bean.ReportQueryData;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.governor.BusinessAssetTerm;
import com.execue.core.common.type.AggregateQueryType;
import com.execue.core.common.type.ColumnType;
import com.execue.core.common.type.ConversionType;
import com.execue.core.common.type.ReportType;
import com.execue.core.configuration.ICoreConfigurationService;
import com.execue.repoting.aggregation.bean.ReportColumnInfo;
import com.execue.repoting.aggregation.bean.ReportMetaInfo;
import com.execue.repoting.aggregation.bean.ReportSelection;
import com.execue.repoting.aggregation.bean.ReportSelectionConstants;
import com.execue.repoting.aggregation.exception.AggregationExceptionCodes;
import com.execue.repoting.aggregation.exception.ReportException;

public class ReportSelectionHelper {

   private static final Logger       logger = Logger.getLogger(ReportSelectionHelper.class);

   private ICoreConfigurationService coreConfigurationService;

   public ReportSelection populateReportSelection (ReportMetaInfo reportMetaInfo, AggregateQuery aggregateQuery,
            ReportQueryData queryData) throws ReportException {
      int numberOfRecords = 0;
      int numberOfGroups = 0;
      int numberOfEffectiveGroups = 0;
      int numberOfIdColumns = 0;
      int numberOfMeasures = 0;
      int numberOfEffectiveRecords = 0;
      int dataPoints = 0;
      List<String> effGrps = new ArrayList<String>();
      List<String> grps = new ArrayList<String>();

      ReportSelection reportSelection = new ReportSelection();

      // check for profiles
      checkForProfiles(reportMetaInfo);

      int measures = 0, dimensions = 0, locationColumnCount = 0;
      try {
         for (ReportColumnInfo reportColumnInfo : reportMetaInfo.getReportColumns()) {
            ColumnType columnType = reportColumnInfo.getColumnType();
            Colum column = reportColumnInfo.getColumn();
            switch (columnType) {
               case DIMENSION:
               case SIMPLE_LOOKUP:
               case RANGE_LOOKUP:
                  int count = (int) reportColumnInfo.getCountSize();
                  String name = ((Colum) reportColumnInfo.getBizAssetTerm().getAssetEntityTerm().getAssetEntity())
                           .getName();
                  if (count == 1 && reportMetaInfo.isUnivariants()) {
                     // this is the case of univariant - Vishy
                     grps.add(name);
                  } else {
                     effGrps.add(name);
                     grps.add(name);
                     numberOfEffectiveGroups++;
                  }
                  dimensions++;
                  if (ConversionType.LOCATION.equals(column.getConversionType())) {
                     locationColumnCount++;
                  }
                  if (numberOfEffectiveRecords > 0) {
                     numberOfEffectiveRecords *= count;
                  } else {
                     numberOfEffectiveRecords = count;
                  }
                  break;
               case ID:
                  numberOfIdColumns++;
                  break;
               case MEASURE:
                  measures++;
                  break;
               default:
                  logger.debug("It should not reach here!! : " + reportColumnInfo.getColumnName());
                  break;
            }
         }
         // No of Records
         if (queryData != null) {
            numberOfRecords = queryData.getNumberOfRows();
         } else {
            numberOfRecords = (int) reportMetaInfo.getTotalCount();
         }
         // No of Groups
         if (aggregateQuery.getAssetQuery().getLogicalQuery().getSummarizations() != null) {
            numberOfGroups = aggregateQuery.getAssetQuery().getLogicalQuery().getSummarizations().size();
         }
         // No of Measures = SQ.metrics.size() - (the sum of ids and dimensions)
         numberOfMeasures = aggregateQuery.getAssetQuery().getLogicalQuery().getMetrics().size()
                  - (numberOfIdColumns + dimensions);
         // No of Data points
         dataPoints = numberOfMeasures * numberOfEffectiveRecords;

         reportSelection.setNumberOfRecords(numberOfRecords);
         reportSelection.setNumberOfGroups(numberOfGroups);
         reportSelection.setNumberOfEffectiveGroups(numberOfEffectiveGroups);
         reportSelection.setNumberOfIdColumns(numberOfIdColumns);
         reportSelection.setNumberOfDimensions(dimensions);
         reportSelection.setNumberOfMeasures(numberOfMeasures);
         reportSelection.setNumberOfEffectiveRecords(numberOfEffectiveRecords);
         reportSelection.setDataPoints(dataPoints);
         reportSelection.setApplicationId(reportMetaInfo.getAssetQuery().getLogicalQuery().getAsset().getApplication()
                  .getId());
         reportSelection.setProfilePresent(reportMetaInfo.isProfilePresent());
         reportSelection.setNumberOfDimensionOfLocationType(locationColumnCount);

         // set the top/bottom info
         if (AggregateQueryType.BUSINESS_SUMMARY.equals(aggregateQuery.getType())
                  && aggregateQuery.getAssetQuery().getLogicalQuery().getTopBottom() != null) {
            reportSelection.setTopBottom(true);
         }

         // set the data browser flag if its detail report path
         if (AggregateQueryType.DETAILED_SUMMARY.equals(aggregateQuery.getType())) {
            if (reportMetaInfo.isGenerateOnlyDataBrowser()) {
               reportSelection.setDataBrowser(true);
            } else {
               // -JM- 18-FEB-2011 : Added code to use the ID columns as DIMENSIONS in the detail path
               reportSelection.setDetailPath(true);
               logger
                        .info("In the Detail Path : considering the ID columns as DIMENSIONS for report type selection; Number of ID columns : "
                                 + numberOfIdColumns);
               numberOfEffectiveGroups = numberOfEffectiveGroups + numberOfIdColumns;
               numberOfIdColumns = 0;
               reportSelection.setNumberOfEffectiveGroups(numberOfEffectiveGroups);
               reportSelection.setNumberOfIdColumns(numberOfIdColumns);
            }
         }

         // check portrait report eligibility
         // At minimum atleast one dimension should exists
         if (reportSelection.getNumberOfDimensions() >= 1) {
            checkForPortraitReportEligibility(reportSelection);
         }

         StringBuffer sb = new StringBuffer();
         // sb.append("\n#####################################################");
         sb.append("Number of Records : " + reportSelection.getNumberOfRecords());
         sb.append("\tNumber of Groups : " + reportSelection.getNumberOfGroups() + grps);
         sb.append("\tNumber of Effective Groups : " + reportSelection.getNumberOfEffectiveGroups() + effGrps);
         sb.append("\nNumber of Id Columns : " + reportSelection.getNumberOfIdColumns());
         sb.append("\tNumber of Measures : " + reportSelection.getNumberOfMeasures());
         sb.append("\tNumber of Effective Records : " + reportSelection.getNumberOfEffectiveRecords());
         sb.append("\tNumber of Data Points : " + reportSelection.getDataPoints());
         sb.append("\tNumber of Dimension LocationType : " + reportSelection.getNumberOfDimensionOfLocationType());
         // sb.append("\n#####################################################");
         if (logger.isInfoEnabled()) {
            logger.info(sb.toString());
         }
      } catch (Exception exception) {
         logger.error("ReportException in ReportSelectionHelper", exception);
         logger.error("Cause : " + exception.getCause());
         throw new ReportException(AggregationExceptionCodes.AGG_REPORT_SELECTION_EXCEPTION_CODE,
                  "Error in populating the report selection object", exception.getCause());
      }
      return reportSelection;
   }

   public void selectReportTypes (ReportSelection selection, AggregateQuery aggregateQuery) throws ReportException {
      try {
         List<ReportType> reportTypes = selection.getReportTypes();
         // Using local variables instead of making method calls
         int dataPoints = selection.getDataPoints();
         int numberOfEffectiveGroups = selection.getNumberOfEffectiveGroups();
         int numberOfEffectiveRecords = selection.getNumberOfEffectiveRecords();
         int numberOfGroups = selection.getNumberOfGroups();
         int numberOfIdColumns = selection.getNumberOfIdColumns();
         int numberOfMeasures = selection.getNumberOfMeasures();
         int numberOfRecords = selection.getNumberOfRecords();
         // int locationDimensionCount = selection.getNumberOfDimensionOfLocationType();
         // Iterator dataPointsGItr=selection.getgroups.iterator();
         // while(dataPointsGItr.hasNext()){
         // String key=(String)dataPointsGItr.next();
         // ColumnData colData = (ColumnData)metaDataMap.get(key);
         // int dataP = colData.getUniqueCount();
         // dataPoints *= colData.getUniqueCount();
         // numberOfEffectiveRecords = dataPoints;
         // }

         // if(idcolumns.size()>0 || measures.size()>0) {
         // dataPoints*=(idcolumns.size()+measures.size());
         // }

         // / RULES: SHOW ALL-GROUPS not just EFFECTIVE GROUPS when showing PIVOT or CSV
         // / REVISED CODE .....

         if (numberOfRecords <= ReportSelectionConstants.LOW_RECORDS_LIMIT) {
            if (numberOfEffectiveGroups == 0) {
               reportTypes.add(ReportType.Grid);
               reportTypes.add(ReportType.CsvFile);
               // checking for Portrait report
               if (selection.isEligibleForPortraitReport()) {
                  reportTypes.add(ReportType.PortraitTable);
                  // -JM- added on 18-OCT-2010 : Remove GRID when Portrait report is selected
                  reportTypes.remove(ReportType.Grid);
               }
            } else if (numberOfEffectiveGroups == 1) {
               // // TABLES
               reportTypes.add(ReportType.Grid);
               if (numberOfGroups >= 2 && numberOfRecords > numberOfEffectiveRecords) {
                  reportTypes.add(ReportType.PivotTable);
               }
               reportTypes.add(ReportType.CsvFile);

               // checking for Portrait report
               if (selection.isEligibleForPortraitReport()) {
                  reportTypes.add(ReportType.PortraitTable);
                  // -JM- added on 18-OCT-2010 : Remove GRID when Portrait report is selected
                  reportTypes.remove(ReportType.Grid);
               }

               // // CHARTS
               // Added pie chart selection logic on 21-Feb-2012
               int MAX_MEASURE_COUNT_FOR_PIE_CHART = 5;
               if (numberOfMeasures > 0 && numberOfMeasures <= MAX_MEASURE_COUNT_FOR_PIE_CHART && numberOfRecords > 1
                        && numberOfRecords <= 20) {
                  reportTypes.add(ReportType.PieChart);
               }
               if (dataPoints <= ReportSelectionConstants.LOW_DATA_POINTS) // 60
               {
                  // ID columns when they have count applied on them, will be considered as measures
                  // without this change, simple bar chart is coming out even when there is one measure and one count
                  // column
                  // we really need to show a multi-bar chart in this case.
                  if (numberOfMeasures == 1 && numberOfIdColumns == 0) {
                     // commented out below because of zero measure issue
                     // || (numberOfMeasures == 0 && numberOfIdColumns == 1)) {
                     reportTypes.add(ReportType.BarChart);
                  } else {
                     if (numberOfMeasures > 0) {
                        reportTypes.add(ReportType.BarLineChart);
                        reportTypes.add(ReportType.MultiBarChart);
                     }
                     if (numberOfMeasures > 0 && numberOfMeasures < 5) {
                        reportTypes.add(ReportType.ClusterBarChart);
                     }
                     if (numberOfMeasures >= 5) {
                        reportTypes.add(ReportType.CMultiBarChart);
                     }
                     // if (locationDimensionCount == 1)
                     // reportTypes.add(ReportType.CountryMapChart);
                  }
               } else if (dataPoints <= ReportSelectionConstants.MED_DATA_POINTS) { // 120
                  if (numberOfMeasures == 1 && numberOfIdColumns == 0) {
                     // || (numberOfMeasures == 0 && numberOfIdColumns == 1)) {
                     reportTypes.add(ReportType.LineChart);
                  } else {
                     // logic for adding portrait table
                     if (selection.isProfilePresent() && numberOfRecords <= 3) {
                        reportTypes.add(ReportType.PortraitTable);
                     }
                     if (numberOfMeasures > 0) {
                        reportTypes.add(ReportType.BarLineChart);
                        reportTypes.add(ReportType.MultiLineChart);
                     }
                     if (numberOfMeasures > 0 && numberOfMeasures < 5) {
                        reportTypes.add(ReportType.ClusterBarChart);
                     }
                     if (numberOfMeasures >= 5) {
                        reportTypes.add(ReportType.CMultiBarChart);
                     }
                     // if (locationDimensionCount == 1)
                     // reportTypes.add(ReportType.CountryMapChart);
                  }
               } else if (dataPoints <= ReportSelectionConstants.HIGH_DATA_POINTS) { // 650
                  // if there is only measure, linechart is the only possibility
                  if (numberOfMeasures == 1) {
                     // || (numberOfMeasures == 0 && numberOfIdColumns == 1)) {
                     reportTypes.add(ReportType.LineChart);
                  } else {
                     if (numberOfMeasures > 0) {
                        reportTypes.add(ReportType.BarLineChart);
                        reportTypes.add(ReportType.MultiLineChart);
                        reportTypes.add(ReportType.CMultiLineChart);
                     }
                     // if (locationDimensionCount == 1)
                     // reportTypes.add(ReportType.CountryMapChart);
                  }
               } else if (numberOfMeasures > 1) {
                  reportTypes.add(ReportType.BarLineChart);
                  reportTypes.add(ReportType.MultiLineChart);
                  reportTypes.add(ReportType.CMultiLineChart);
                  // if (locationDimensionCount == 1)
                  // reportTypes.add(ReportType.CountryMapChart);
               }
            } else if (numberOfEffectiveGroups == 2) {
               // // TABLES
               // CHANGE ON 12-DEC-2009 : -JVK- Select only the grid when there are no measures
               if (numberOfMeasures == 0) {
                  reportTypes.add(ReportType.Grid);
               } else {
                  reportTypes.add(ReportType.CrossTable);
                  reportTypes.add(ReportType.GroupTable);
                  reportTypes.add(ReportType.PivotTable);
                  if (dataPoints <= 60) {
                     reportTypes.add(ReportType.CrossBarChart);
                  } else if (dataPoints <= 650) {
                     reportTypes.add(ReportType.CrossLineChart);
                  }
                  // if (locationDimensionCount == 1 && dataPoints <= 650)
                  // reportTypes.add(ReportType.CrossStateMapChart);
               }
               reportTypes.add(ReportType.CsvFile);
               // // CHARTS
               if (numberOfMeasures == 1) {
                  // reportTypes.add(ReportType.MultiLineChart);
                  // if(dataPoints<=36)
                  // {
                  // reportTypes.add(ReportType.ClusterBarChart);
                  // reportTypes.add(ReportType.MultiBarChart);
                  //          
                  // } else {
                  // reportTypes.add(ReportType.ClusterBarChart);
                  // }
               } else if (numberOfMeasures >= 2) {

                  // reportTypes.add(ReportType.MultiLineClusterChart);
                  // if (dataPoints <= 60) {
                  // reportTypes.add(ReportType.CMMultiBarChart);

                  // } else if (dataPoints <= 600) {

                  // reportTypes.add(ReportType.CMMultiLineChart);
                  // }
               }
            } else if (numberOfEffectiveGroups > 2) {
               reportTypes.add(ReportType.Grid);
               reportTypes.add(ReportType.PivotTable);
               reportTypes.add(ReportType.CsvFile);
            }
         } else if (numberOfRecords <= ReportSelectionConstants.HIGH_RECORDS_LIMIT) {
            reportTypes.add(ReportType.CsvFile);
            if (numberOfGroups >= 2) {
               // -JM- added on 18-Oct-2010 : adding simple grid when there are more than 2 dims
               reportTypes.add(ReportType.Grid);
               reportTypes.add(ReportType.PivotTable);
            }
         }
         // TURN-OFF pivot if rows>1200 or something else based on timetaken-2-XML-2-rendering
         else if (numberOfRecords > ReportSelectionConstants.HIGH_RECORDS_LIMIT) {
            // Remove the Pivot
            // reportTypes.remove(ReportType.PivotTable);
            // Add Grid & CSV
            reportTypes.add(ReportType.Grid);
            reportTypes.add(ReportType.CsvFile);
         }
         // Handle Top/Bottom case
         // if (selection.isTopBottom()) {
         // if (reportTypes.size() > 0) {
         // reportTypes.clear();
         // reportTypes.add(ReportType.Grid);
         // reportTypes.add(ReportType.CsvFile);
         // }
         // }
         selection.setReportTypes(reportTypes);
      } catch (Exception exception) {
         logger.error("ReportException in ReportSelectionHelper", exception);
         logger.error("Cause : " + exception.getCause());
         throw new ReportException(AggregationExceptionCodes.AGG_REPORT_SELECTION_EXCEPTION_CODE,
                  "Error in selection of Report Types", exception.getCause());
      }
   }

   /**
    * This method to be used under some dimension check block, so the dimension count is the developers responsibility
    * to place it properly
    * 
    * @param selection
    * @return
    */
   private void checkForPortraitReportEligibility (ReportSelection selection) {
      boolean isEligible = false;
      int noOfRecords = selection.getNumberOfRecords();
      // int noOfDims = selection.getNumberOfEffectiveGroups();
      int noOfMeasures = selection.getNumberOfMeasures();
      boolean isProfilePresent = selection.isProfilePresent();
      logger.debug("Checking for Portrait Report eligibility....");
      logger.debug("Is Profile present : " + isProfilePresent);
      logger.debug("Number of records : " + noOfRecords);
      // logger.debug("Number of dimensions : " + noOfDims);
      logger.debug("Number of measures : " + noOfMeasures);
      // TODO: -JM- check if presence of profile is required
      if (noOfRecords <= 3 && noOfMeasures >= 5) {
         isEligible = true;
      }
      logger.debug("Is the query eligible for Portrait Report : " + isEligible);
      selection.setEligibleForPortraitReport(isEligible);
   }

   private void checkForProfiles (ReportMetaInfo reportMetaInfo) {
      boolean isProfilePresent = false;
      // check for the presence of profiles
      for (BusinessAssetTerm metric : reportMetaInfo.getAssetQuery().getLogicalQuery().getMetrics()) {
         if (metric.getBusinessTerm().getProfileDomainEntityDefinitionId() != null) {
            isProfilePresent = true;
            break;
         }
      }
      reportMetaInfo.setProfilePresent(isProfilePresent);
   }

   /**
    * @return the coreConfigurationService
    */
   public ICoreConfigurationService getCoreConfigurationService () {
      return coreConfigurationService;
   }

   /**
    * @param coreConfigurationService
    *           the coreConfigurationService to set
    */
   public void setCoreConfigurationService (ICoreConfigurationService coreConfigurationService) {
      this.coreConfigurationService = coreConfigurationService;
   }

}