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


package com.execue.governor.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.execue.core.common.bean.BusinessCondition;
import com.execue.core.common.bean.BusinessTerm;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.DateFormat;
import com.execue.core.common.bean.entity.DefaultDynamicValue;
import com.execue.core.common.bean.entity.NormalizedDataType;
import com.execue.core.common.bean.governor.BusinessAssetTerm;
import com.execue.core.common.bean.governor.EntityMappingInfo;
import com.execue.core.common.bean.governor.StructuredCondition;
import com.execue.core.common.bean.nlp.RelativeTimeNormalizedData;
import com.execue.core.common.bean.nlp.TimeFrameNormalizedData;
import com.execue.core.common.bean.querygen.QueryValue;
import com.execue.core.common.type.DataType;
import com.execue.core.common.type.DateQualifier;
import com.execue.core.common.type.DynamicValueQualifierType;
import com.execue.core.common.type.OperandType;
import com.execue.core.common.type.OperatorType;
import com.execue.core.common.type.PublishedFileType;
import com.execue.core.exception.ExeCueException;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.governor.exception.GovernorException;
import com.execue.governor.exception.GovernorExceptionCodes;
import com.execue.governor.helper.GovernorServiceHelper;
import com.execue.governor.helper.GovernorTimeFrameServiceHelper;
import com.execue.governor.service.IGovernorTimeFrameConstants;
import com.execue.governor.service.IGovernorTimeFrameHandlerService;
import com.execue.swi.configuration.ISWIConfigurationService;
import com.execue.swi.exception.SWIException;
import com.execue.swi.service.ILookupService;
import com.execue.util.conversion.timeframe.ITimeFrameHandlerService;
import com.execue.util.conversion.timeframe.bean.TimeFrameConversionInputStructure;
import com.execue.util.conversion.timeframe.bean.TimeFrameConversionOutputInfo;
import com.execue.util.conversion.timeframe.bean.TimeFrameRangeComponent;
import com.execue.util.conversion.timeframe.util.TimeFrameUtility;

/**
 * This class will handle the time frame related conversions and will act as a helper to governor.
 * 
 * @author Vishay Gupta
 * @version 1.0
 * @since 24/08/10
 */

// OPEN_ISSUES :
// 1. TODO :-VG- same value on between and in can be avoided upfront. This will lessen the iterations for the same
// value processing.;
public class GovernorTimeFrameHandlerServiceImpl extends GovernorTimeFrameServiceHelper implements
         IGovernorTimeFrameHandlerService, IGovernorTimeFrameConstants {

   private ILookupService                                 lookupService;
   private ISWIConfigurationService                       swiConfigurationService;
   private ITimeFrameHandlerService                       timeFrameHandlerService;

   private static Map<DateQualifier, List<DateQualifier>> dateQualifierHierarchyConversionMap = new HashMap<DateQualifier, List<DateQualifier>>();
   static {
      TimeFrameUtility.populateDateQualifierHierarchyConversionMap(dateQualifierHierarchyConversionMap);
   }

   // if left side business term is TIME_FRAME concept, we need to get all the variations also,
   // if total size on lhs is 1, we need to adjust the Normalized data according to Date Qualifier
   // of the lhs column. If more than one is present on lhs, we need to pick the best one
   // by looking at DateQualifier of normalized data. If exact match is found good else
   // pick the closet one. we need to fill in the data components as we cannot change the
   // column data format(qualifier)
   // once left side is fixed along with data we need to get the values from the normalized data
   // using column format
   // finally we will build a structured condition
   public StructuredCondition buildTimeFrameCondition (BusinessCondition businessCondition,
            List<EntityMappingInfo> entityMappings, Asset asset) throws GovernorException {
      StructuredCondition structuredCondition = null;
      try {
         // collecting all the lhs business terms including alternate list.
         List<BusinessTerm> lhsBusinessTerms = new ArrayList<BusinessTerm>();
         lhsBusinessTerms.add(businessCondition.getLhsBusinessTerm());
         if (ExecueCoreUtil.isCollectionNotEmpty(businessCondition.getLhsBusinessTermVariations())) {
            lhsBusinessTerms.addAll(businessCondition.getLhsBusinessTermVariations());
         }
         // populate business asset terms from this list.
         List<BusinessAssetTerm> lhsBusinessAssetTerms = GovernorServiceHelper.populateBusinessAssetTerms(
                  lhsBusinessTerms, entityMappings, asset);
         // if we have atleast one of the term mapped, we can process the time frame.
         if (ExecueCoreUtil.isCollectionNotEmpty(lhsBusinessAssetTerms)) {
            // picking the correct lhs term. If it is exact match, pick it else based on the data DateQualifier,
            // pick the closet one.
            BusinessAssetTerm finalLhsBusinessAssetTerm = null;
            if (lhsBusinessAssetTerms.size() == 1) {
               finalLhsBusinessAssetTerm = lhsBusinessAssetTerms.get(0);
            } else {
               DateQualifier dataDateQualifier = null;
               if (NormalizedDataType.TIME_FRAME_NORMALIZED_DATA.equals(businessCondition.getNormalizedDataType())) {
                  dataDateQualifier = ((TimeFrameNormalizedData) businessCondition.getRhsValues().get(0)
                           .getNormalizedData()).getDateQualifier();
               } else if (NormalizedDataType.RELATIVE_TIME_NORMALIZED_DATA.equals(businessCondition
                        .getNormalizedDataType())) {
                  dataDateQualifier = ((RelativeTimeNormalizedData) businessCondition.getRhsValues().get(0)
                           .getNormalizedData()).getRelativeDateQualifier();
               }
               finalLhsBusinessAssetTerm = pickCorrectLhsBusinessAssetTerm(asset, lhsBusinessAssetTerms,
                        dataDateQualifier);
            }

            BusinessTerm originalLhsBusinessTerm = businessCondition.getLhsBusinessTerm();
            // if original and final term is same, then we dont need to correct lhs
            boolean isLhsTermReplaced = false;
            if (!originalLhsBusinessTerm.getBusinessEntityTerm().equals(
                     finalLhsBusinessAssetTerm.getBusinessTerm().getBusinessEntityTerm())) {
               correctLhsBusinessAssetTerm(finalLhsBusinessAssetTerm, businessCondition.getLhsBusinessTerm());
               isLhsTermReplaced = true;
            }
            // building the structured condition.
            structuredCondition = new StructuredCondition();
            structuredCondition.setOperandType(OperandType.VALUE);
            structuredCondition.setLhsBusinessAssetTerm(finalLhsBusinessAssetTerm);
            if (isLhsTermReplaced) {
               structuredCondition.setOriginalLhsBusinessTerm(originalLhsBusinessTerm);
            }
            // convert the time frame data and build the structured condition.
            populateTimeFrameStructuredCondition(businessCondition, structuredCondition, asset,
                     finalLhsBusinessAssetTerm);
         }
      } catch (Exception e) {
         e.printStackTrace();
         throw new GovernorException(GovernorExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
      return structuredCondition;
   }

   /**
    * This method correct the lhs term. If there was different term in lhs of the condition and we pick the different
    * term from alternate list, we need to carry the information from old term to new term.
    * 
    * @param finalLhsBusinessAssetTerm
    * @param originalLhsBusinessTerm
    */
   private void correctLhsBusinessAssetTerm (BusinessAssetTerm finalLhsBusinessAssetTerm,
            BusinessTerm originalLhsBusinessTerm) {
      finalLhsBusinessAssetTerm.getBusinessTerm()
               .setBusinessTermWeight(originalLhsBusinessTerm.getBusinessTermWeight());
   }

   /**
    * This method builds the structured condition for time frame. It handles normal TimeFrame conditions as well as
    * RelativeTimeFrames. In normal TimeFrames, condition can come as equal to, between or range.Till,Since,Before,After
    * conditions can also come in normal time frames. In RelativeTimeFrames, the variations are First/Last.
    * 
    * @param businessCondition
    * @param structuredCondition
    * @param asset
    * @param lhsBusinessAssetTerm
    * @throws GovernorException
    * @throws CloneNotSupportedException
    * @throws ParseException
    * @throws SWIException
    */
   private void populateTimeFrameStructuredCondition (BusinessCondition businessCondition,
            StructuredCondition structuredCondition, Asset asset, BusinessAssetTerm lhsBusinessAssetTerm)
            throws GovernorException, Exception {
      // getting column date format and column date qualifier
      Colum lhsColumn = (Colum) lhsBusinessAssetTerm.getAssetEntityTerm().getAssetEntity();
      DateQualifier columnDateQualifier = getSwiConfigurationService().getDateQualifier(lhsColumn.getDataFormat());
      String columnDateFormat = lhsColumn.getDataFormat();
      TimeFrameNormalizedData defaultTimeFrameNormalizedData = null;

      // if condition is a normal time frame condition.
      if (NormalizedDataType.TIME_FRAME_NORMALIZED_DATA.equals(businessCondition.getNormalizedDataType())) {
         // default time frame normalized data for current asset and business term.
         defaultTimeFrameNormalizedData = populateDefaultDynamicValue(asset, lhsBusinessAssetTerm, columnDateQualifier,
                  columnDateFormat, DynamicValueQualifierType.LAST);

      }
      // if time frame data is of RelativetimeFrame type.
      else if (NormalizedDataType.RELATIVE_TIME_NORMALIZED_DATA.equals(businessCondition.getNormalizedDataType())) {
         RelativeTimeNormalizedData relativeTimeNormalizedData = (RelativeTimeNormalizedData) businessCondition
                  .getRhsValues().get(0).getNormalizedData();
         // default time frame normalized data for current asset and business term.
         defaultTimeFrameNormalizedData = populateDefaultDynamicValue(asset, lhsBusinessAssetTerm, columnDateQualifier,
                  columnDateFormat, relativeTimeNormalizedData.getDynamicValueQualifierType());

      }
      List<TimeFrameNormalizedData> timeFrameNormalizedDataList = new ArrayList<TimeFrameNormalizedData>();
      for (QueryValue queryValue : businessCondition.getRhsValues()) {
         timeFrameNormalizedDataList.add((TimeFrameNormalizedData) queryValue.getNormalizedData());
      }
      TimeFrameConversionInputStructure timeFrameConversionInputStructure = TimeFrameUtility
               .buildTimeFrameConversionInputStructure(timeFrameNormalizedDataList, businessCondition
                        .getNormalizedDataType(), businessCondition.getOperator(), columnDateFormat,
                        columnDateQualifier, defaultTimeFrameNormalizedData);
      TimeFrameConversionOutputInfo timeFrameConversionOutputInfo = getTimeFrameHandlerService().timeFrameConversion(
               timeFrameConversionInputStructure);
      structuredCondition.setOperator(timeFrameConversionOutputInfo.getOperatorType());
      // populate the query values from normalized values.
      DateFormat dateFormat = getSwiConfigurationService().getSupportedDateFormat(lhsColumn.getDataFormat(),
               asset.getDataSource().getProviderType());
      switch (timeFrameConversionOutputInfo.getConversionOutputOperandType()) {
         case SINGLE:
            List<QueryValue> queryValues = new ArrayList<QueryValue>();
            queryValues.add(populateQueryValueFromNormalizedData(timeFrameConversionOutputInfo.getSingleOperandValue(),
                     lhsColumn, dateFormat));
            structuredCondition.setRhsValues(queryValues);
            break;
         case DOUBLE:
            structuredCondition.setRhsValues(populateQueryValueFromRangeComponent(timeFrameConversionOutputInfo
                     .getDoubleOperandValue(), lhsColumn, dateFormat));
            break;
         case MULTIPLE:
            structuredCondition.setRhsValues(populateQueryValueFromNormalizedData(timeFrameConversionOutputInfo
                     .getMultipleOperandValues(), lhsColumn, dateFormat));
            break;
         case SUBCONDITION:
            List<TimeFrameRangeComponent> subConditionOperandValues = timeFrameConversionOutputInfo
                     .getSubConditionOperandValues();
            structuredCondition.setSubCondition(true);
            List<StructuredCondition> subConditions = new ArrayList<StructuredCondition>();
            for (int index = 0; index < subConditionOperandValues.size(); index++) {
               StructuredCondition subCondition = copyStructuredCondition(structuredCondition);
               subCondition.setRhsValues(populateQueryValueFromRangeComponent(subConditionOperandValues.get(index),
                        lhsColumn, dateFormat));
               subCondition.setOperator(OperatorType.BETWEEN);
               subConditions.add(subCondition);
            }
            structuredCondition.setSubConditions(subConditions);
            break;
      }
   }

   /**
    * This method fetches the default value from db and converts it into TimeFrameNormalizedData object.
    * 
    * @param asset
    * @param lhsBusinessAssetTerm
    * @param columnDateQualifier
    * @param dateFormat
    * @param dynamicValueQualifierType
    * @return defaultTimeFrameNormalizedData
    * @throws GovernorException
    * @throws CloneNotSupportedException
    */
   private TimeFrameNormalizedData populateDefaultDynamicValue (Asset asset, BusinessAssetTerm lhsBusinessAssetTerm,
            DateQualifier columnDateQualifier, String dateFormat, DynamicValueQualifierType dynamicValueQualifierType)
            throws GovernorException, CloneNotSupportedException {
      TimeFrameNormalizedData defaultTimeFrameNormalizedData = null;
      try {
         DynamicValueQualifierType normalizedDynamicValueQualifierType = normalizeDynamicValueQualifierType(dynamicValueQualifierType);

         DefaultDynamicValue defaultDynamicValue = lookupService.getDefaultDynamicValue(asset.getId(),
                  lhsBusinessAssetTerm.getBusinessTerm().getBusinessEntityTerm().getBusinessEntityDefinitionId(),
                  normalizedDynamicValueQualifierType);

         defaultTimeFrameNormalizedData = TimeFrameUtility.populateTimeFrameNormalizedData(defaultDynamicValue
                  .getDefaultValue(), columnDateQualifier, dateFormat);

         TimeFrameNormalizedData currentDateTimeFrameNormalizedData = TimeFrameUtility.populateTimeFrameNormalizedData(
                  columnDateQualifier, dateFormat);
         currentDateTimeFrameNormalizedData.setDateQualifier(columnDateQualifier);

         if (DynamicValueQualifierType.LAST.equals(normalizedDynamicValueQualifierType)) {
            overrideDefaultTimeFrameNormalizedData(defaultTimeFrameNormalizedData, currentDateTimeFrameNormalizedData,
                     columnDateQualifier, dateFormat);
         }
      } catch (ExeCueException e) {
         throw new GovernorException(GovernorExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
      return defaultTimeFrameNormalizedData;
   }

   /**
    * If the defaultTimeFrameNormalizedData is after the currentDateTimeFrameNormalizedData, then over-ride the default
    * with current
    * 
    * @param defaultTimeFrameNormalizedData
    * @param currentDateTimeFrameNormalizedData
    */
   private void overrideDefaultTimeFrameNormalizedData (TimeFrameNormalizedData defaultTimeFrameNormalizedData,
            TimeFrameNormalizedData currentDateTimeFrameNormalizedData, DateQualifier dateQualifier, String dateFormat) {
      long defaultTimeFrameNormalizedDataInMillis = TimeFrameUtility.parseTimeFrameNormalizedDataAsLong(
               defaultTimeFrameNormalizedData, dateFormat, dateQualifier);
      long currentTimeFrameNormalizedDataInMillis = TimeFrameUtility.parseTimeFrameNormalizedDataAsLong(
               currentDateTimeFrameNormalizedData, dateFormat, dateQualifier);
      if (defaultTimeFrameNormalizedDataInMillis > currentTimeFrameNormalizedDataInMillis) {
         defaultTimeFrameNormalizedData = currentDateTimeFrameNormalizedData;
      }
   }

   /**
    * This method populates the QueryValue object from string normalized values. also.
    * 
    * @param normalizedValues
    * @param colum
    * @return queryValues
    */
   private List<QueryValue> populateQueryValueFromNormalizedData (List<String> normalizedValues, Colum colum,
            DateFormat dateFormat) {
      List<QueryValue> queryValues = new ArrayList<QueryValue>();
      for (String normalizedValue : normalizedValues) {
         queryValues.add(populateQueryValueFromNormalizedData(normalizedValue, colum, dateFormat));
      }
      return queryValues;
   }

   private List<QueryValue> populateQueryValueFromRangeComponent (TimeFrameRangeComponent rangeComponent, Colum colum,
            DateFormat dateFormat) {
      List<QueryValue> queryValues = new ArrayList<QueryValue>();
      queryValues.add(populateQueryValueFromNormalizedData(rangeComponent.getLowerRange(), colum, dateFormat));
      queryValues.add(populateQueryValueFromNormalizedData(rangeComponent.getUpperRange(), colum, dateFormat));
      return queryValues;
   }

   private QueryValue populateQueryValueFromNormalizedData (String normalizedValue, Colum colum, DateFormat dateFormat) {
      QueryValue queryValue = new QueryValue();
      queryValue.setValue(normalizedValue);
      queryValue.setDataType(colum.getDataType());
      return queryValue;
   }

   /**
    * This method picks the correct lhs term among the list.
    * 
    * @param asset
    * @param businessAssetTerms
    * @param dataDateQualifier
    * @return correctLhsBusinessAssetTerm.
    */
   private BusinessAssetTerm pickCorrectLhsBusinessAssetTerm (Asset asset, List<BusinessAssetTerm> businessAssetTerms,
            DateQualifier dataDateQualifier) {
      // closest hierarchy map is defined upfront. For the DataQualifier in hand, we fetch the hierarchy from the map
      // from the hierarchy we will try to find the closest.For this reason we need to iterate over all the column
      // qualifiers, keep on collecting the index of the match in the list. Pick the least index and return the
      // corresponding column.
      List<DateQualifier> dateQualifierConversionHierarchyList = dateQualifierHierarchyConversionMap
               .get(dataDateQualifier);
      List<Long> matchedDateQualifierTermIndexList = new ArrayList<Long>();
      for (BusinessAssetTerm businessAssetTerm : businessAssetTerms) {
         Colum lhsColumn = (Colum) businessAssetTerm.getAssetEntityTerm().getAssetEntity();
         // if column datatype is datetime and asset is file based and it has original file format
         // then use that to get the correct asset
         String dateFormat = lhsColumn.getDataFormat();
         if (DataType.DATETIME.equals(lhsColumn.getDataType())
                  && !PublishedFileType.RDBMS.equals(asset.getOriginType()) && lhsColumn.getFileDateFormat() != null) {
            dateFormat = lhsColumn.getFileDateFormat();
         }
         DateQualifier columnDataQualifier = getSwiConfigurationService().getDateQualifier(dateFormat);
         matchedDateQualifierTermIndexList.add(new Long(dateQualifierConversionHierarchyList
                  .indexOf(columnDataQualifier)));
      }
      Long shortestMatchedQualifierTermIndex = getShortestElementIndex(matchedDateQualifierTermIndexList);
      return businessAssetTerms.get(new Long(shortestMatchedQualifierTermIndex).intValue());
   }

   public ILookupService getLookupService () {
      return lookupService;
   }

   public void setLookupService (ILookupService lookupService) {
      this.lookupService = lookupService;
   }

   public ISWIConfigurationService getSwiConfigurationService () {
      return swiConfigurationService;
   }

   public void setSwiConfigurationService (ISWIConfigurationService swiConfigurationService) {
      this.swiConfigurationService = swiConfigurationService;
   }

   public ITimeFrameHandlerService getTimeFrameHandlerService () {
      return timeFrameHandlerService;
   }

   public void setTimeFrameHandlerService (ITimeFrameHandlerService timeFrameHandlerService) {
      this.timeFrameHandlerService = timeFrameHandlerService;
   }
}
