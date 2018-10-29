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


package com.execue.handler.presentation.impl;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.execue.core.common.bean.Page;
import com.execue.core.common.bean.entity.Instance;
import com.execue.core.common.bean.qdata.QDataAggregatedQueryColumn;
import com.execue.core.common.bean.qdata.QDataCachedReportResults;
import com.execue.core.common.bean.reports.prsntion.HierarchicalReportColumnMetaData;
import com.execue.core.common.bean.reports.prsntion.HierarchicalReportInfo;
import com.execue.core.common.bean.reports.prsntion.UniversalColumn;
import com.execue.core.common.bean.reports.prsntion.UniversalColumnData;
import com.execue.core.common.bean.reports.prsntion.UniversalHierarchyEntity;
import com.execue.core.common.bean.reports.prsntion.UniversalReport;
import com.execue.core.common.bean.reports.prsntion.UniversalReportHeader;
import com.execue.core.common.bean.reports.prsntion.UniversalValue;
import com.execue.core.configuration.ICoreConfigurationService;
import com.execue.core.constants.ExecueConstants;
import com.execue.core.exception.ExeCueException;
import com.execue.core.exception.HandlerException;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.driver.IPresentationDriver;
import com.execue.exception.UniversalDataTransformationException;
import com.execue.handler.bean.grid.UIColumnGrid;
import com.execue.handler.bean.grid.UIColumnGridHeader;
import com.execue.handler.bean.grid.UIDataBrowserGrid;
import com.execue.handler.presentation.IPresentationHandler;
import com.execue.qdata.exception.QueryDataException;
import com.execue.qdata.service.IQueryDataService;
import com.execue.report.configuration.service.IPresentationConfigurationService;
import com.execue.reporting.presentation.bean.AggregationColumn;
import com.execue.reporting.presentation.bean.AggregationColumnData;
import com.execue.reporting.presentation.bean.AggregationReport;
import com.execue.reporting.presentation.bean.AggregationReportHeader;
import com.execue.reporting.presentation.bean.AggregationValue;
import com.execue.reporting.presentation.bean.ReportPresentationConstants;
import com.execue.reporting.presentation.helper.PresentationTransformHelper;
import com.execue.swi.exception.KDXException;
import com.execue.swi.service.IKDXRetrievalService;

/**
 * @author John Mallavalli
 */
public class PresentationHandler implements IPresentationHandler {

   private IPresentationDriver               presentationDriver;
   private IKDXRetrievalService              kdxRetrievalService;
   private IQueryDataService                 queryDataService;
   private ICoreConfigurationService         coreConfigurationService;
   private IPresentationConfigurationService presentationConfigurationService;

   public String getTkrNameByAggregatedQuery (Long aggQueryId, Long applicationId) throws HandlerException {
      String tkrName = null;
      try {
         List<String> stockChartValidApps = getCoreConfigurationService().getStockChartValidApps();
         List<String> stockChartAppConceptBedIds = getCoreConfigurationService().getStockChartValidConceptBeds();
         String conceptBedIdList = null;
         int index = 0;
         for (String validAppId : stockChartValidApps) {
            if (applicationId.equals(Long.parseLong(validAppId))) {
               conceptBedIdList = stockChartAppConceptBedIds.get(index);
               break;
            }
            index++;
         }
         String[] conceptBedIds = conceptBedIdList.split(",");
         QDataAggregatedQueryColumn aggQueryColumn = null;
         Long matchedConceptBedId = null;
         for (String conceptBedId : conceptBedIds) {
            aggQueryColumn = getQueryDataService().getMatchedAggregatedQueryColumnByConceptBedId(
                     Long.parseLong(conceptBedId), aggQueryId);
            if (aggQueryColumn != null) {
               matchedConceptBedId = Long.parseLong(conceptBedId);
               break;
            }
         }
         if (aggQueryColumn != null) {
            String value = aggQueryColumn.getValue();
            String[] values = value.split("~");
            Instance instance = getKdxRetrievalService().getInstanceByInstanceName(matchedConceptBedId, values[0]);
            if (instance != null) {
               tkrName = instance.getAbbreviatedName();
            }
         }
      } catch (QueryDataException queryDataException) {
         queryDataException.printStackTrace();
      } catch (KDXException e) {
         e.printStackTrace();
      }
      return tkrName;
   }

   public String processReportRequest (Long aggregateQueryId) throws HandlerException {
      try {
         return getPresentationDriver().processReportRequest(aggregateQueryId);
      } catch (ExeCueException e) {
         throw new HandlerException(e.getCode(), e.getCause());
      }
   }

   public UIColumnGrid processDetailReportHeaderRequest (Long aggregateQueryId) throws HandlerException {
      try {
         String xmlData = getPresentationDriver().processDetailReportHeaderRequest(aggregateQueryId);
         if (ExecueCoreUtil.isNotEmpty(xmlData)) {
            AggregationReport aggregationReport = transformXmlData(xmlData);
            return transformToUIColumnGridHeader(aggregationReport.getHeader());
         }
         return null;
      } catch (ExeCueException e) {
         throw new HandlerException(e.getCode(), e.getCause());
      }
   }

   public List<UIDataBrowserGrid> processReportRequest (Long aggregateQueryId, Page pageDetail) throws HandlerException {
      try {
         // TODO -SS- create a service layer to presentation so that all the transformation can be handled there without
         // any dependency issues
         return transformIntoUIObject(transformXmlData(presentationDriver.processReportRequest(aggregateQueryId,
                  pageDetail)));
      } catch (ExeCueException e) {
         throw new HandlerException(e.getCode(), e.getCause());
      }
   }

   public QDataCachedReportResults getCachedReportResultsById (Long cachedReportId) throws HandlerException {
      try {
         return getQueryDataService().getCachedReportResultsById(cachedReportId);
      } catch (QueryDataException e) {
         throw new HandlerException(e.getCode(), e.getCause());
      }
   }

   public boolean getConfigurationRevertToDefaultGridFlag () throws HandlerException {
      return getPresentationConfigurationService().isRevertToDefaultGriid();
   }

   @Override
   public String getReportXMLData (Long aggregateQueryId) throws HandlerException {
      try {
         String universalReportXMLData = getPresentationDriver().processReportRequest(aggregateQueryId);
         return universalReportXMLData;
      } catch (ExeCueException exeCueException) {
         throw new HandlerException(exeCueException.getCode(), exeCueException);
      }
   }

   @Override
   public HierarchicalReportInfo getHierarchicalReportInfo (Long aggregateQueryId) throws HandlerException {

      HierarchicalReportInfo hierarchicalReportInfo = new HierarchicalReportInfo();

      try {
         String universalReportXMLData = getPresentationDriver().processReportRequest(aggregateQueryId);
         UniversalReport universalReport = PresentationTransformHelper
                  .getUniversalXMLTranformedToUniversalReport(universalReportXMLData);

         // get the data re ordered as per the hierarchical report needs
         // universalReport = getUniversalXMLAfterReOrderingData(universalReport);
         // TODO : -RG- Above call is not needed as the ordering is taken care via the SQL itself

         // build the hierarchical report info from universal report as per the hierarchical grid needs
         hierarchicalReportInfo = getHierarchicalReportInfoFromUniveralReport(universalReport);
      } catch (ExeCueException exeCueException) {
         throw new HandlerException(exeCueException.getCode(), exeCueException);
      }
      return hierarchicalReportInfo;
   }

   private HierarchicalReportInfo getHierarchicalReportInfoFromUniveralReport (UniversalReport universalReport) {
      HierarchicalReportInfo hierarchicalReportInfo = new HierarchicalReportInfo();
      // title
      hierarchicalReportInfo.setTitle(universalReport.getHeader().getTITLE());
      // hierarchyColumnName
      hierarchicalReportInfo.setHierarchyColumnName(universalReport.getHeader().getHIERARCHY().getNAME());

      Map<String, Integer> hierarchyColumnLevelByName = new LinkedHashMap<String, Integer>();
      List<Integer> columnLevels = new ArrayList<Integer>();

      for (UniversalHierarchyEntity hierarchyEntity : universalReport.getHeader().getHIERARCHY().getHIERARCHYDETAILS()) {
         hierarchyColumnLevelByName.put(hierarchyEntity.getREFCOLUMN(), hierarchyEntity.getLEVEL());
         columnLevels.add(hierarchyEntity.getLEVEL());
      }

      // List hierarchicalReportColumnNames
      hierarchicalReportInfo.setHierarchicalReportColumnNames(getColumnNamesFromUniversalColumn(universalReport
               .getHeader(), hierarchyColumnLevelByName));

      // List hierarchicalReportData
      hierarchicalReportInfo.setHierarchicalReportData(getHierarchicalReportDataFromUniversalColumn(universalReport,
               hierarchyColumnLevelByName, columnLevels));

      // List<HierarchicalReportColumnMetaData>
      hierarchicalReportInfo.setHierarchicalReportColumnMetaInfo(getHierarchicalReportColumnMetaData(universalReport,
               hierarchyColumnLevelByName));
      return hierarchicalReportInfo;
   }

   private List<HierarchicalReportColumnMetaData> getHierarchicalReportColumnMetaData (UniversalReport universalReport,
            Map<String, Integer> hierarchyColumnLevelByName) {

      // Maintain the separate list for summarization column
      List<UniversalColumn> summarizationColumns = new ArrayList<UniversalColumn>();
      for (UniversalColumn universalColumn : universalReport.getHeader().getColumns()) {
         if (!isHierarchyColumnData(hierarchyColumnLevelByName, universalColumn.getId())) {
            summarizationColumns.add(universalColumn);
         }
      }

      List<HierarchicalReportColumnMetaData> hierarchicalReportColumnMetaDataList = new ArrayList<HierarchicalReportColumnMetaData>();

      // Add the id column metadata 
      HierarchicalReportColumnMetaData hierarchicalReportIdColumnMetaData = new HierarchicalReportColumnMetaData();
      hierarchicalReportIdColumnMetaData.setIndex("id");
      hierarchicalReportIdColumnMetaData.setName("id");
      hierarchicalReportIdColumnMetaData.setHidden(true);
      hierarchicalReportIdColumnMetaData.setKey(true);
      hierarchicalReportColumnMetaDataList.add(hierarchicalReportIdColumnMetaData);

      // Add the hierarchy column metadata
      HierarchicalReportColumnMetaData hierarchicalReportHierarchyColumnMetaData = new HierarchicalReportColumnMetaData();
      hierarchicalReportHierarchyColumnMetaData.setIndex(universalReport.getHeader().getHIERARCHY().getNAME());
      hierarchicalReportHierarchyColumnMetaData.setName(universalReport.getHeader().getHIERARCHY().getNAME());
      hierarchicalReportColumnMetaDataList.add(hierarchicalReportHierarchyColumnMetaData);

      for (UniversalColumn universalColumn : summarizationColumns) {
         HierarchicalReportColumnMetaData hierarchicalReportColumnMetaData = new HierarchicalReportColumnMetaData();
         hierarchicalReportColumnMetaData.setIndex(universalColumn.getDesc());
         hierarchicalReportColumnMetaData.setName(universalColumn.getDesc());
         hierarchicalReportColumnMetaDataList.add(hierarchicalReportColumnMetaData);
      }
      return hierarchicalReportColumnMetaDataList;
   }

   private List<Map<String, Object>> getHierarchicalReportDataFromUniversalColumn (UniversalReport universalReport,
            Map<String, Integer> hierarchyColumnLevelByName, List<Integer> columnLevels) {

      // Prepare the map
      Map<String, UniversalColumn> columnNameByColumnDataNameReferenceMap = new HashMap<String, UniversalColumn>();
      for (UniversalColumn universalColumn : universalReport.getHeader().getColumns()) {
         columnNameByColumnDataNameReferenceMap.put(universalColumn.getId(), universalColumn);
      }

      // Initialize the data structures required to populate the hierarchical report data
      List<Map<String, Object>> hierarchicalReportData = new ArrayList<Map<String, Object>>();
      List<Map<String, Object>> previousParentHierarchicalReportData = new ArrayList<Map<String, Object>>();
      Map<Integer, Integer> parentLevelByParentId = new HashMap<Integer, Integer>();
      List<UniversalValue> leafRecordUniversalValues = new ArrayList<UniversalValue>();

      // Check how the data is positioned in the universal report
      boolean parentRecordIsOnTop = checkIfParentIsOnTop(universalReport.getData().getValues(),
               hierarchyColumnLevelByName);

      int idCounter = 1;
      String hierarchyColumnName = universalReport.getHeader().getHIERARCHY().getNAME();

      // Iterate over the universal values and prepare the hierarchical report data
      List<UniversalValue> universalValues = universalReport.getData().getValues();
      for (UniversalValue universalValue : universalValues) {

         // Get the current hierarchy column data as the summarization data is valid for one hierarchy column 
         // based on the grouping of the data
         UniversalColumnData currentHierarchyColumn = getCurrentHierarchyColumnData(universalValue,
                  hierarchyColumnLevelByName);
         if (currentHierarchyColumn == null || isEmpty(currentHierarchyColumn.getValue())) {
            // Discard this row
            continue;
         }

         boolean isLeafRecord = isLeaf(columnLevels, hierarchyColumnLevelByName.get(currentHierarchyColumn.getName()));
         if (isLeafRecord) {

            // Add to the leaf record list
            leafRecordUniversalValues.add(universalValue);

         } else {

            // Add to the leaf record list
            if (parentRecordIsOnTop) {

               // Get the record data for the leaf records
               for (UniversalValue leafRecordUniversalValue : leafRecordUniversalValues) {
                  Map<String, Object> leafRecordReportData = populateLeafRecordData(leafRecordUniversalValue,
                           columnNameByColumnDataNameReferenceMap, hierarchyColumnLevelByName, parentLevelByParentId,
                           idCounter, hierarchyColumnName);
                  hierarchicalReportData.add(leafRecordReportData);
                  idCounter++;
               }

               // Re Initialize the child record universal values list
               leafRecordUniversalValues = new ArrayList<UniversalValue>();

               Map<String, Object> parentReportData = populateParentRecordColumn(hierarchyColumnLevelByName,
                        universalValue, currentHierarchyColumn, columnNameByColumnDataNameReferenceMap,
                        parentLevelByParentId, idCounter, hierarchyColumnName, isLeafRecord);

               hierarchicalReportData.add(parentReportData);

               // Increment the id counter
               idCounter++;

            } else {
               Map<String, Object> parentReportData = populateParentRecordColumn(hierarchyColumnLevelByName,
                        universalValue, currentHierarchyColumn, columnNameByColumnDataNameReferenceMap,
                        parentLevelByParentId, idCounter, hierarchyColumnName, isLeafRecord);

               // Maintain the parent hierarchy report data in the list
               previousParentHierarchicalReportData.add(parentReportData);

               if (leafRecordUniversalValues.size() == 0) {

                  if (ExecueCoreUtil.isCollectionNotEmpty(previousParentHierarchicalReportData)) {

                     // Need to add the parent report data to the specific index in the list
                     // TODO:: NK:: Need to do revisit this logic for hierarchy with more than 3 levels
                     Map<String, Object> previousTopParentRecord = previousParentHierarchicalReportData.get(0);
                     Integer previousTopParentId = Integer.valueOf(previousTopParentRecord.get("id").toString());
                     hierarchicalReportData.add(previousTopParentId - 1, parentReportData);

                     // Increment the id counter
                     idCounter++;

                     // Update the previous parent's parent Ids
                     for (Map<String, Object> previousReportData : previousParentHierarchicalReportData) {
                        Integer previosParentLevel = Integer.valueOf(previousReportData.get("level").toString());
                        Integer currentChildParentId = parentLevelByParentId.get(previosParentLevel - 1);
                        if (currentChildParentId != null) {
                           previousReportData.put("parent", currentChildParentId.toString());
                        }
                     }

                     previousParentHierarchicalReportData = new ArrayList<Map<String, Object>>();
                  }
               } else {
                  hierarchicalReportData.add(parentReportData);

                  // Increment the id counter
                  idCounter++;

                  // Get the record data for the leaf records
                  for (UniversalValue leafRecordUniversalValue : leafRecordUniversalValues) {

                     Map<String, Object> leafRecordReportData = populateLeafRecordData(leafRecordUniversalValue,
                              columnNameByColumnDataNameReferenceMap, hierarchyColumnLevelByName,
                              parentLevelByParentId, idCounter, hierarchyColumnName);
                     hierarchicalReportData.add(leafRecordReportData);
                     idCounter++;
                  }

                  // Re Initialize the child record universal values list
                  leafRecordUniversalValues = new ArrayList<UniversalValue>();
               }
            }
         }
      }

      // Get the record data for the remaining leaf records, if any
      for (UniversalValue leafRecordUniversalValue : leafRecordUniversalValues) {
         Map<String, Object> leafRecordReportData = populateLeafRecordData(leafRecordUniversalValue,
                  columnNameByColumnDataNameReferenceMap, hierarchyColumnLevelByName, parentLevelByParentId, idCounter,
                  hierarchyColumnName);
         hierarchicalReportData.add(leafRecordReportData);
         idCounter++;
      }

      return hierarchicalReportData;
   }

   /**
    * @param hierarchyColumnLevelByName
    * @param universalValue
    * @param currentHierarchyColumn
    * @param columnNameByColumnDataNameReferenceMap
    * @param parentLevelByParentId
    * @param idCounter
    * @param hierarchyColumnName
    * @param isLeafRecord
    * @return
    */
   private Map<String, Object> populateParentRecordColumn (Map<String, Integer> hierarchyColumnLevelByName,
            UniversalValue universalValue, UniversalColumnData currentHierarchyColumn,
            Map<String, UniversalColumn> columnNameByColumnDataNameReferenceMap,
            Map<Integer, Integer> parentLevelByParentId, int idCounter, String hierarchyColumnName, boolean isLeafRecord) {
      List<UniversalColumnData> summarizationColumnData = getSummarizationColumnData(universalValue,
               hierarchyColumnLevelByName);

      Integer columnLevel = hierarchyColumnLevelByName.get(currentHierarchyColumn.getName());
      Integer parentId = parentLevelByParentId.get(columnLevel - 1);

      // Maintain the parent level and parent id in the map
      parentLevelByParentId.put(columnLevel, idCounter);

      Map<String, Object> parentReportData = getHierarchyReportData(universalValue, currentHierarchyColumn,
               summarizationColumnData, columnNameByColumnDataNameReferenceMap, idCounter, hierarchyColumnName,
               columnLevel, parentId, isLeafRecord);
      return parentReportData;
   }

   /**
    * @param leafRecordUniversalValue
    * @param columnNameByColumnDataNameReferenceMap
    * @param hierarchyColumnLevelByName
    * @param parentLevelByParentId
    * @param idCounter
    * @param hierarchyColumnName
    * @return the Map<String, Object>
    */
   private Map<String, Object> populateLeafRecordData (UniversalValue leafRecordUniversalValue,
            Map<String, UniversalColumn> columnNameByColumnDataNameReferenceMap,
            Map<String, Integer> hierarchyColumnLevelByName, Map<Integer, Integer> parentLevelByParentId,
            int idCounter, String hierarchyColumnName) {

      // Get the current hierarchy column data as the summarization data in valid for one hierarchy column 
      // based on the grouping of the data
      UniversalColumnData leafRecordCurrentHierarchyColumn = getCurrentHierarchyColumnData(leafRecordUniversalValue,
               hierarchyColumnLevelByName);

      // Get the summarization column data
      List<UniversalColumnData> leafRecordSummarizationColumnData = getSummarizationColumnData(
               leafRecordUniversalValue, hierarchyColumnLevelByName);

      Integer leafRecordColumnLevel = hierarchyColumnLevelByName.get(leafRecordCurrentHierarchyColumn.getName());
      Integer leafRecordParentId = parentLevelByParentId.get(leafRecordColumnLevel - 1);

      Map<String, Object> leafRecordReportData = getHierarchyReportData(leafRecordUniversalValue,
               leafRecordCurrentHierarchyColumn, leafRecordSummarizationColumnData,
               columnNameByColumnDataNameReferenceMap, idCounter, hierarchyColumnName, leafRecordColumnLevel,
               leafRecordParentId, true);
      return leafRecordReportData;
   }

   private List<UniversalColumnData> getHiearchyColumnData (UniversalValue universalValue,
            Map<String, Integer> hierarchyColumnLevelByName) {
      List<UniversalColumnData> hierarchyColumnData = new ArrayList<UniversalColumnData>();
      for (UniversalColumnData universalColumnData : universalValue.getColumndata()) {
         if (isHierarchyColumnData(hierarchyColumnLevelByName, universalColumnData.getName())) {
            hierarchyColumnData.add(universalColumnData);
         }
      }
      return hierarchyColumnData;
   }

   private List<UniversalColumnData> getSummarizationColumnData (UniversalValue universalValue,
            Map<String, Integer> hierarchyColumnLevelByName) {
      List<UniversalColumnData> summarizationData = new ArrayList<UniversalColumnData>();
      for (UniversalColumnData universalColumnData : universalValue.getColumndata()) {
         if (!isHierarchyColumnData(hierarchyColumnLevelByName, universalColumnData.getName())) {
            summarizationData.add(universalColumnData);
         }
      }
      return summarizationData;
   }

   private Map<String, Object> getHierarchyReportData (UniversalValue universalValue,
            UniversalColumnData currentHierarchyColumn, List<UniversalColumnData> summarizationColumns,
            Map<String, UniversalColumn> columnNameByColumnDataNameReferenceMap, Integer id,
            String hierarchyColumnName, Integer columnLevel, Integer parentId, boolean isLeaf) {

      // Add the id column data
      Map<String, Object> reportData = new LinkedHashMap<String, Object>();
      reportData.put("id", id.toString());

      // Add the hierarchy column data
      reportData.put(hierarchyColumnName, currentHierarchyColumn.getValue());

      // Add the summarization column data
      int maxScale = getPresentationConfigurationService().getGridLayoutObject().getGRIDPROPERTIES()
               .getMaxscaleValueToFormat();
      NumberFormat measureNumberFormat = PresentationTransformHelper.getNumberFormatWithTwoDigitPrecision(maxScale);
      NumberFormat idNumberFormat = new DecimalFormat("#,###");
      Map<String, NumberFormat> numberFormatByCtype = new HashMap<String, NumberFormat>();
      numberFormatByCtype.put(ReportPresentationConstants.CTYPE_ID, idNumberFormat);
      numberFormatByCtype.put(ReportPresentationConstants.CTYPE_MEASURE, measureNumberFormat);
      for (UniversalColumnData summarizationColumnData : summarizationColumns) {
         UniversalColumn universalColumn = columnNameByColumnDataNameReferenceMap
                  .get(summarizationColumnData.getName());
         String columnName = universalColumn.getDesc();
         String columnValue = getFormattedValue(summarizationColumnData, universalColumn, numberFormatByCtype);
         reportData.put(columnName, columnValue);
      }

      // Add the related hierarchy column data like
      // level:"0", parent:null, isLeaf:false, expanded:false, loaded:true
      reportData.put("level", columnLevel.toString());
      reportData.put("parent", parentId == null ? null : parentId.toString());
      reportData.put("isLeaf", isLeaf);
      reportData.put("expanded", true);
      reportData.put("loaded", true);

      return reportData;
   }

   /**
    * @param hierarchyColumnData
    * @return the current UniversalColumnData in consideration
    */
   private UniversalColumnData getCurrentHierarchyColumnData (UniversalValue universalValue,
            Map<String, Integer> hierarchyColumnLevelByName) {

      // Get the hierarchy column data
      List<UniversalColumnData> hierarchyColumnData = getHiearchyColumnData(universalValue, hierarchyColumnLevelByName);

      UniversalColumnData currentHierarchyColumn = null;
      // Assumption here is list of column data is in proper order, hence we go from left to right to reach
      // till the hierarchy column data is not empty
      for (int index = 0; index < hierarchyColumnData.size(); index++) {
         if (isEmpty(hierarchyColumnData.get(index).getValue())) {
            break;
         }
         currentHierarchyColumn = hierarchyColumnData.get(index);
      }

      return currentHierarchyColumn;
   }

   private boolean checkIfParentIsOnTop (List<UniversalValue> values, Map<String, Integer> hierarchyColumnLevelByName) {
      boolean parentRecordIsOnTop = false;
      UniversalValue universalValue = values.get(0);
      List<UniversalColumnData> hiearchyColumnValues = getHiearchyColumnData(universalValue, hierarchyColumnLevelByName);
      for (UniversalColumnData hierarchicalColumnValue : hiearchyColumnValues) {
         if (isEmpty(hierarchicalColumnValue.getValue())) {
            parentRecordIsOnTop = true;
            break;
         }
      }
      return parentRecordIsOnTop;
   }

   private String getFormattedValue (UniversalColumnData universalColumnData, UniversalColumn universalColumn,
            Map<String, NumberFormat> numberFormaterByCtype) {
      String formattedValue = universalColumnData.getValue();
      if (isEmpty(formattedValue)) {
         return formattedValue;
      }
      String ctype = universalColumn.getCtype();
      NumberFormat numberFormat = numberFormaterByCtype.get(ctype);
      if (ReportPresentationConstants.CTYPE_ID.equalsIgnoreCase(ctype)
               && ReportPresentationConstants.CTYPE_MEASURE.equalsIgnoreCase(universalColumn.getPlotAs())) {
         formattedValue = numberFormat.format(Double.parseDouble(formattedValue));
      } else if (ReportPresentationConstants.CTYPE_MEASURE.equalsIgnoreCase(ctype)) {
         if (universalColumn.getScale() >= getPresentationConfigurationService().getGridLayoutObject()
                  .getGRIDPROPERTIES().getMinscaleValueToFormat()) {
            numberFormat.setMinimumFractionDigits(getPresentationConfigurationService().getGridLayoutObject()
                     .getGRIDPROPERTIES().getMinscaleValueToFormat());
         }

         formattedValue = numberFormat.format(Double.parseDouble(universalColumnData.getValue()));
      }
      return formattedValue;
   }

   private boolean isEmpty (String value) {
      return ExecueCoreUtil.isEmpty(value) || value.equalsIgnoreCase(ExecueConstants.NOT_AVAILABLE)
               || value.equalsIgnoreCase(ExecueConstants.VALUE_ALL);
   }

   private boolean isLeaf (List<Integer> columnLevels, Integer level) {
      if (columnLevels.get(columnLevels.size() - 1).equals(level)) {
         return true;
      }
      return false;
   }

   private boolean isHierarchyColumnData (Map<String, Integer> hierarchyColumnLevelByName, String columnName) {
      return hierarchyColumnLevelByName.containsKey(columnName);
   }

   private List<String> getColumnNamesFromUniversalColumn (UniversalReportHeader universalReportHeader,
            Map<String, Integer> hierarchyColumnLevelByName) {
      List<String> columnNames = new ArrayList<String>();

      // Maintain the separate list for summarization column
      List<UniversalColumn> summarizationColumns = new ArrayList<UniversalColumn>();
      for (UniversalColumn universalColumn : universalReportHeader.getColumns()) {
         if (!isHierarchyColumnData(hierarchyColumnLevelByName, universalColumn.getId())) {
            summarizationColumns.add(universalColumn);
         }
      }
      columnNames.add("id");
      columnNames.add(universalReportHeader.getHIERARCHY().getNAME());
      for (UniversalColumn universalColumn : summarizationColumns) {
         columnNames.add(universalColumn.getDesc());
      }
      return columnNames;
   }

   private List<UIDataBrowserGrid> transformIntoUIObject (AggregationReport aggregationReport) {
      List<UIDataBrowserGrid> aggregatedCellData = new ArrayList<UIDataBrowserGrid>();
      for (AggregationValue values : aggregationReport.getData().getValues()) {
         UIDataBrowserGrid uiColumn = new UIDataBrowserGrid();
         List<String> columnValues = new ArrayList<String>();
         uiColumn.setGridCellValues(columnValues);
         aggregatedCellData.add(uiColumn);

         for (AggregationColumnData columnData : values.getColumndata()) {
            columnValues.add(columnData.getValue());
         }
      }
      return aggregatedCellData;
   }

   private UIColumnGrid transformToUIColumnGridHeader (AggregationReportHeader reportHeader) {
      UIColumnGrid uiColGrid = new UIColumnGrid();
      List<UIColumnGridHeader> uiColGridHeaders = new ArrayList<UIColumnGridHeader>();
      for (AggregationColumn header : reportHeader.getColumns()) {
         UIColumnGridHeader gridHeader = new UIColumnGridHeader();
         gridHeader.setId(header.getId());
         gridHeader.setName(header.getDesc());
         gridHeader.setType(header.getCtype());
         uiColGridHeaders.add(gridHeader);
      }
      uiColGrid.setColumnGridHeader(uiColGridHeaders);
      uiColGrid.setNumOfColumns(Long.parseLong(reportHeader.getNumcolumns()));
      uiColGrid.setGridTitle(reportHeader.getTitle());
      return uiColGrid;
   }

   private AggregationReport transformXmlData (String xmlData) throws UniversalDataTransformationException {
      return PresentationTransformHelper.applyTransformationOnUniversalXml(xmlData, true);
   }

   public IPresentationDriver getPresentationDriver () {
      return presentationDriver;
   }

   public void setPresentationDriver (IPresentationDriver presentationDriver) {
      this.presentationDriver = presentationDriver;
   }

   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }

   public IQueryDataService getQueryDataService () {
      return queryDataService;
   }

   public void setQueryDataService (IQueryDataService queryDataService) {
      this.queryDataService = queryDataService;
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

   /**
    * @return the presentationConfigurationService
    */
   public IPresentationConfigurationService getPresentationConfigurationService () {
      return presentationConfigurationService;
   }

   /**
    * @param presentationConfigurationService
    *           the presentationConfigurationService to set
    */
   public void setPresentationConfigurationService (IPresentationConfigurationService presentationConfigurationService) {
      this.presentationConfigurationService = presentationConfigurationService;
   }

}
