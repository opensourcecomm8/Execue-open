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


package com.execue.nlp.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeSet;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.execue.core.common.bean.INormalizedData;
import com.execue.core.common.bean.IWeightedEntity;
import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.CloudComponent;
import com.execue.core.common.bean.entity.NormalizedDataType;
import com.execue.core.common.bean.entity.TimeFrameType;
import com.execue.core.common.bean.nlp.ComparativeInfoNormalizedData;
import com.execue.core.common.bean.nlp.DefaultNormalizedData;
import com.execue.core.common.bean.nlp.InstanceInformation;
import com.execue.core.common.bean.nlp.LocationNormalizedData;
import com.execue.core.common.bean.nlp.LocationNormalizedDataEntity;
import com.execue.core.common.bean.nlp.NLPConstants;
import com.execue.core.common.bean.nlp.NormalizedCloudDataFactory;
import com.execue.core.common.bean.nlp.NormalizedDataEntity;
import com.execue.core.common.bean.nlp.RangeNormalizedData;
import com.execue.core.common.bean.nlp.RecognitionEntityType;
import com.execue.core.common.bean.nlp.RecognizedType;
import com.execue.core.common.bean.nlp.RelativeTimeNormalizedData;
import com.execue.core.common.bean.nlp.TimeFrameNormalizedData;
import com.execue.core.common.bean.nlp.UnitNormalizedData;
import com.execue.core.common.bean.nlp.ValueRealizationNormalizedData;
import com.execue.core.common.bean.nlp.WeekDayNormalizedData;
import com.execue.core.common.bean.nlp.WeekDayNormalizedDataComponent;
import com.execue.core.common.bean.nlp.WeekNormalizedData;
import com.execue.core.common.bean.util.TimeRange;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.DateQualifier;
import com.execue.core.constants.ExecueConstants;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.core.util.ExecueStringUtil;
import com.execue.nlp.bean.entity.ConceptEntity;
import com.execue.nlp.bean.entity.InstanceEntity;
import com.execue.nlp.bean.entity.OntoEntity;
import com.execue.nlp.bean.entity.RecognitionEntity;
import com.execue.nlp.bean.entity.RecognizedCloudEntity;
import com.execue.nlp.bean.entity.ReferedTokenPosition;
import com.execue.nlp.bean.entity.TypeEntity;
import com.execue.nlp.exception.NLPExceptionCodes;
import com.execue.nlp.exception.NLPSystemException;
import com.execue.nlp.helper.NLPServiceHelper;
import com.execue.nlp.util.NLPUtilities;
import com.execue.swi.configuration.ITimeConfigurationService;

public class MeaningFinderHelper {

   protected NLPServiceHelper          nlpServiceHelper;
   protected ITimeConfigurationService timeConfigurationService;

   /**
    * Method to return the list of normalized data for the given recognizedCloudEntity
    * 
    * @param recognizedCloudEntity
    * @return the List<INormalizedData>
    */
   protected List<INormalizedData> getNormalizedCloudData (RecognizedCloudEntity recognizedCloudEntity) {
      List<INormalizedData> normalizedDataList = new ArrayList<INormalizedData>(1);
      if (NLPConstants.RELATIVE_FOR_QUANTITATIVE_CLOUD.equalsIgnoreCase(recognizedCloudEntity.getCloud().getName())) {
         normalizedDataList = getRelativeForQuantitativeNormalizedData(recognizedCloudEntity);
         return normalizedDataList;
      }

      // Return if the cloud output name is empty 
      if (StringUtils.isEmpty(recognizedCloudEntity.getCloudOutputName())) {
         return normalizedDataList;
      }

      if (NLPConstants.LOCATION_CLOUD.equalsIgnoreCase(recognizedCloudEntity.getCloud().getName())) {
         normalizedDataList = getLocationNormalizedData(recognizedCloudEntity);
         return normalizedDataList;
      }

      if (NLPConstants.VALUE_WITH_PREPOSITION_CLOUD.equalsIgnoreCase(recognizedCloudEntity.getCloud().getName())) {
         normalizedDataList = getValueNormalizedDataWithPrepositions(recognizedCloudEntity);
         return normalizedDataList;
      }
      if (NLPConstants.RELATIVE_WEEKDAY_CLOUD.equalsIgnoreCase(recognizedCloudEntity.getCloud().getName())) {
         normalizedDataList = getRelativeWeekDayNormalizedData(recognizedCloudEntity);
         return normalizedDataList;
      }
      NormalizedDataType normalizedDataType = NLPUtilities.getNormalizedDataType(recognizedCloudEntity);
      if (normalizedDataType == NormalizedDataType.TIME_FRAME_NORMALIZED_DATA) {
         normalizedDataList = getTFNormalizedData(recognizedCloudEntity);
      } else if (normalizedDataType == NormalizedDataType.VALUE_NORMALIZED_DATA) {
         normalizedDataList = getOPValueNormalizedData(recognizedCloudEntity);
      } else if (normalizedDataType == NormalizedDataType.UNIT_NORMALIZED_DATA) {
         normalizedDataList = getUnitNormalizedData(recognizedCloudEntity);
      } else if (normalizedDataType == NormalizedDataType.COMPARATIVE_INFO_NORMALIZED_DATA) {
         normalizedDataList = getCompInfoNormalizedData(recognizedCloudEntity);
      } else if (normalizedDataType == NormalizedDataType.WEEK_NORMALIZED_DATA) {
         normalizedDataList = getWeekNormalizedData(recognizedCloudEntity);
      } else {
         // TODO: prepare default normalized data
         normalizedDataList = getDefaultNormalizedData(recognizedCloudEntity);
      }
      return normalizedDataList;
   }

   /**
    * Method to return the list of LocationNormalizedData for the given input recognizedCloudEntity
    * 
    * @param recognizedCloudEntity
    * @return the List<INormalizedData>
    */
   private List<INormalizedData> getLocationNormalizedData (RecognizedCloudEntity recognizedCloudEntity) {
      List<INormalizedData> locationNormalizedDataList = new ArrayList<INormalizedData>();
      LocationDataHolder locationDataHolder = getLocationDataHolder(recognizedCloudEntity);
      List<IWeightedEntity> cityRecognitions = locationDataHolder.getCityRecognitions();
      List<IWeightedEntity> stateRecognitions = locationDataHolder.getStateRecognitions();
      List<IWeightedEntity> countryRecognitions = locationDataHolder.getCountryRecognitions();
      List<IWeightedEntity> countyRecognitions = locationDataHolder.getCountyRecognitions();

      LocationNormalizedData locationNormalizedData = (LocationNormalizedData) NormalizedCloudDataFactory
               .getNormalizedCloudData(NormalizedDataType.LOCATION_NORMALIZED_DATA);
      locationNormalizedData.setOutputBedId(recognizedCloudEntity.getCloudOutputBedId());
      locationNormalizedData.setOutputName(recognizedCloudEntity.getCloudOutputName());

      updateTokenPositions(recognizedCloudEntity, locationNormalizedData);

      List<NormalizedDataEntity> cityDataEntities = getLocationNormalizedDataEntities(cityRecognitions);
      List<NormalizedDataEntity> stateDataEntities = getLocationNormalizedDataEntities(stateRecognitions);
      List<NormalizedDataEntity> countryDataEntities = getLocationNormalizedDataEntities(countryRecognitions);
      List<NormalizedDataEntity> countyDataEntities = getLocationNormalizedDataEntities(countyRecognitions);

      locationNormalizedData.getCities().addAll(cityDataEntities);
      locationNormalizedData.getStates().addAll(stateDataEntities);
      locationNormalizedData.getCounties().addAll(countryDataEntities);
      locationNormalizedData.getCounties().addAll(countyDataEntities);

      locationNormalizedDataList.add(locationNormalizedData);

      return locationNormalizedDataList;
   }

   /**
    * @param recognizedCloudEntity
    * @param locationNormalizedData
    */
   private void updateTokenPositions (RecognizedCloudEntity recognizedCloudEntity,
            LocationNormalizedData locationNormalizedData) {
      ReferedTokenPosition rtp = new ReferedTokenPosition();
      ReferedTokenPosition rtpForOrigPos = new ReferedTokenPosition();
      for (IWeightedEntity weightedEntity : recognizedCloudEntity.getRecognitionEntities()) {
         RecognitionEntity recEntity = (RecognitionEntity) weightedEntity;
         rtp.addAll(recEntity.getReferedTokenPositions());
         rtpForOrigPos.addAll(recEntity.getOriginalReferedTokenPositions());
      }
      locationNormalizedData.setReferredTokenPositions(rtp.getReferedTokenPositions());
      locationNormalizedData.setOriginalReferredTokenPositions(rtpForOrigPos.getReferedTokenPositions());
   }

   /**
    * @param cityRecognitions
    * @param typeBedMap
    * @return the List<NormalizedDataEntity> 
    */
   private List<NormalizedDataEntity> getLocationNormalizedDataEntities (List<IWeightedEntity> cityRecognitions) {
      List<NormalizedDataEntity> allNormalizeDataEntities = new ArrayList<NormalizedDataEntity>();
      for (IWeightedEntity weightedEntity : cityRecognitions) {
         List<NormalizedDataEntity> normalizeDataEntities = getLocationNormalizeDataEntities(weightedEntity);
         allNormalizeDataEntities.addAll(normalizeDataEntities);
      }
      return allNormalizeDataEntities;
   }

   private List<INormalizedData> getRelativeWeekDayNormalizedData (RecognizedCloudEntity recognizedCloudEntity) {
      WeekDayNormalizedData weekDayNormalizedData = new WeekDayNormalizedData();
      List<IWeightedEntity> recognitionEntities = recognizedCloudEntity.getRecognitionEntities();
      ReferedTokenPosition rtp = new ReferedTokenPosition();
      ReferedTokenPosition rtpForOrigPos = new ReferedTokenPosition();
      Map<Long, BusinessEntityDefinition> typeBedMap = recognizedCloudEntity.getTypeBedMap();
      for (IWeightedEntity weightedEntity : recognitionEntities) {
         RecognitionEntity recEntity = (RecognitionEntity) weightedEntity;
         BusinessEntityDefinition typeBed = typeBedMap.get(((TypeEntity) weightedEntity).getTypeBedId());
         RecognizedType recognizedTypeForEntity = NLPUtilities.getRecognizedType(typeBed);
         if (recognizedTypeForEntity == RecognizedType.WEEK_DAY_TYPE) {
            if (recEntity.getNormalizedData() != null
                     && recEntity.getNormalizedData().getNormalizedDataType() == NormalizedDataType.WEEK_DAY_NORMALIZED_DATA) {
               return new ArrayList<INormalizedData>(1);
            }
            weekDayNormalizedData.setWeekDayBedId(typeBed.getId());
            if (((InstanceEntity) weightedEntity).getDefaultInstanceDisplayName().equalsIgnoreCase("Weekend")) {
               List<NormalizedDataEntity> weekendEntities = populateWeekendEntities((RecognitionEntity) weightedEntity);
               weekDayNormalizedData.setWeekdays(weekendEntities);
            } else {
               NormalizedDataEntity weekDayDataEntity = nlpServiceHelper
                        .getNormalizeDataEntity(weightedEntity, typeBed);
               weekDayNormalizedData.getWeekdays().add(weekDayDataEntity);
            }
         } else if (recognizedTypeForEntity == RecognizedType.ADJECTIVE_TYPE) {
            weekDayNormalizedData.setAdjective(nlpServiceHelper.getNormalizeDataEntity(weightedEntity, typeBed));
         } else if (recognizedTypeForEntity == RecognizedType.NUMBER_TYPE) {
            weekDayNormalizedData.setNumber(nlpServiceHelper.getNormalizeDataEntity(weightedEntity, typeBed));
         }
         rtp.addAll(recEntity.getReferedTokenPositions());
         rtpForOrigPos.addAll(recEntity.getOriginalReferedTokenPositions());
      }
      weekDayNormalizedData.setType(recognizedCloudEntity.getCloudOutputName());
      weekDayNormalizedData.setReferredTokenPositions(rtp.getReferedTokenPositions());
      weekDayNormalizedData.setOriginalReferredTokenPositions(rtpForOrigPos.getReferedTokenPositions());
      List<INormalizedData> normalizedDataList = new ArrayList<INormalizedData>(1);

      normalizedDataList.add(weekDayNormalizedData);
      return normalizedDataList;
   }

   /**
    * @param recognizedCloudEntity
    * @return the List<INormalizedData>
    */
   private List<INormalizedData> getRelativeForQuantitativeNormalizedData (RecognizedCloudEntity recognizedCloudEntity) {

      ReferedTokenPosition rtp = new ReferedTokenPosition();
      ReferedTokenPosition rtpForOrigPos = new ReferedTokenPosition();
      RelativeTimeNormalizedData normalizedData = (RelativeTimeNormalizedData) NormalizedCloudDataFactory
               .getNormalizedCloudData(NormalizedDataType.RELATIVE_TIME_NORMALIZED_DATA);
      Map<Long, BusinessEntityDefinition> typeBedMap = recognizedCloudEntity.getTypeBedMap();

      for (Entry<Long, CloudComponent> entry : recognizedCloudEntity.getCloudComponentInfoMap().entrySet()) {
         CloudComponent cloudComponent = entry.getValue();
         for (IWeightedEntity weightedEntity : cloudComponent.getRecognitionEntities()) {
            RecognitionEntity recEntity = (RecognitionEntity) weightedEntity;
            rtp.addAll(recEntity.getReferedTokenPositions());
            rtpForOrigPos.addAll(recEntity.getOriginalReferedTokenPositions());
            BusinessEntityDefinition typeBed = typeBedMap.get(((TypeEntity) weightedEntity).getTypeBedId());
            if (cloudComponent.getOutputComponent() == CheckType.YES) {
               OntoEntity ontoEntity = (OntoEntity) weightedEntity;
               String detailTypeName = ontoEntity.getDetailTypeName();
               if (detailTypeName != null) {
                  RecognizedType cloudType = RecognizedType.getCloudType(detailTypeName);
                  if (cloudType == null) {
                     return new ArrayList<INormalizedData>(1);
                  } else {
                     normalizedData.setTimeFrameType(cloudType);
                     normalizedData.setRelativeDateQualifier(DateQualifier.getType(cloudType.getValue()));
                  }
               } else {
                  return new ArrayList<INormalizedData>(1);
               }
            }
            RecognizedType recognizedTypeForEntity = NLPUtilities.getRecognizedType(typeBed);
            if (recognizedTypeForEntity == RecognizedType.ADJECTIVE_TYPE) {
               normalizedData.setAdjective(nlpServiceHelper.getNormalizeDataEntity(weightedEntity, typeBed));
            } else if (recognizedTypeForEntity == RecognizedType.NUMBER_TYPE) {
               normalizedData.setNumber(nlpServiceHelper.getNormalizeDataEntity(weightedEntity, typeBed));
            }

         }
      }
      normalizedData.setReferredTokenPositions(rtp.getReferedTokenPositions());
      normalizedData.setOriginalReferredTokenPositions(rtpForOrigPos.getReferedTokenPositions());
      List<INormalizedData> normalizedDataList = new ArrayList<INormalizedData>(1);
      normalizedDataList.add(normalizedData);
      normalizedData.setDateQualifier(NLPUtilities.getDateQualifierBasedOnTFType(normalizedData));
      return normalizedDataList;

   }

   private List<INormalizedData> getWeekNormalizedData (RecognizedCloudEntity recognizedCloudEntity) {
      Map<Long, BusinessEntityDefinition> typeBedMap = recognizedCloudEntity.getTypeBedMap();
      List<IWeightedEntity> numberRecognitions = new ArrayList<IWeightedEntity>(1);
      List<INormalizedData> normalizedDataList = new ArrayList<INormalizedData>();
      WeekNormalizedData weekNormalizedData = new WeekNormalizedData();
      weekNormalizedData.setWeekBedId(recognizedCloudEntity.getCloudOutputBedId());
      weekNormalizedData.setType(recognizedCloudEntity.getCloudOutputName());
      for (IWeightedEntity weightedEntity : recognizedCloudEntity.getRecognitionEntities()) {
         BusinessEntityDefinition typeBed = typeBedMap.get(((TypeEntity) weightedEntity).getTypeBedId());
         RecognizedType recognizedType = NLPUtilities.getRecognizedType(typeBed);
         if (recognizedType == RecognizedType.NUMBER_TYPE) {
            NormalizedDataEntity normalizeDataEntity = nlpServiceHelper.getNormalizeDataEntity(weightedEntity, typeBed);
            weekNormalizedData.setNumber(normalizeDataEntity);
         }
         weekNormalizedData.addReferredTokenPositions(((RecognitionEntity) weightedEntity).getReferedTokenPositions());
         weekNormalizedData.addOriginalReferredTokenPositions(((RecognitionEntity) weightedEntity)
                  .getOriginalReferedTokenPositions());

      }
      normalizedDataList.add(weekNormalizedData);

      return normalizedDataList;
   }

   /**
    * @param recognizedCloudEntity
    * @return the List<INormalizedData>
    */
   private List<INormalizedData> getCompInfoNormalizedData (RecognizedCloudEntity recognizedCloudEntity) {
      List<IWeightedEntity> recognitionEntities = recognizedCloudEntity.getRecognitionEntities();
      List<INormalizedData> normalizedDataList = new ArrayList<INormalizedData>();
      if (CollectionUtils.isEmpty(recognitionEntities)) {
         return null;
      }
      ComparativeInfoNormalizedData normalizedData = new ComparativeInfoNormalizedData();
      normalizedData.setType(recognizedCloudEntity.getCloudOutputName());
      normalizedData.setTypeBeId(recognizedCloudEntity.getCloudOutputBedId());
      Map<Long, BusinessEntityDefinition> typeBedMap = recognizedCloudEntity.getTypeBedMap();
      for (IWeightedEntity weightedEntity : recognizedCloudEntity.getRecognitionEntities()) {
         BusinessEntityDefinition typeBed = typeBedMap.get(((TypeEntity) weightedEntity).getTypeBedId());
         RecognizedType recognizedType = NLPUtilities.getRecognizedType(typeBed);
         if (recognizedType == RecognizedType.NUMBER_TYPE) {
            normalizedData.setLimit(nlpServiceHelper.getNormalizeDataEntity(weightedEntity, typeBed));
         } else {
            normalizedData.setStatistics(nlpServiceHelper.getNormalizeDataEntity(weightedEntity, typeBed));
         }
         normalizedData.addReferredTokenPositions(((RecognitionEntity) weightedEntity).getReferedTokenPositions());
         normalizedData.addOriginalReferredTokenPositions(((RecognitionEntity) weightedEntity)
                  .getOriginalReferedTokenPositions());
      }
      normalizedDataList.add(normalizedData);
      return normalizedDataList;
   }

   private List<INormalizedData> getDefaultNormalizedData (RecognizedCloudEntity recognizedCloudEntity) {
      List<IWeightedEntity> recognitionEntities = recognizedCloudEntity.getRecognitionEntities();
      List<INormalizedData> normalizedDataList = new ArrayList<INormalizedData>();
      if (CollectionUtils.isEmpty(recognitionEntities)) {
         return null;
      }
      DefaultNormalizedData normalizedData = (DefaultNormalizedData) NormalizedCloudDataFactory
               .getNormalizedCloudData(NormalizedDataType.DEFAULT_NORMALIZED_DATA);
      normalizedData.setTypeBedId(recognizedCloudEntity.getCloudOutputBedId());
      normalizedData.setType(recognizedCloudEntity.getCloudOutputName());
      StringBuilder wordValue = new StringBuilder();
      StringBuilder wordDisplayValue = new StringBuilder();
      boolean isYearCloud = false;
      if (recognizedCloudEntity.getCloud().getName().equalsIgnoreCase(NLPConstants.YEAR_CLOUD)) {
         isYearCloud = true;
      }
      for (IWeightedEntity weightedEntity : recognitionEntities) {
         RecognitionEntity recEntity = (RecognitionEntity) weightedEntity;
         if (isYearCloud && recEntity.getEntityType() == RecognitionEntityType.INSTANCE_ENTITY
                  && ((InstanceEntity) recEntity).getTypeDisplayName().equalsIgnoreCase(ExecueConstants.DIGIT)) {
            String instanceValue = ExecueCoreUtil.get4DigitYearFrom2DigitYear(((InstanceEntity) recEntity)
                     .getDefaultInstanceValue());
            wordValue.append(instanceValue);
            wordDisplayValue.append(instanceValue).append(" ");
         } else {
            wordValue.append(getValue(weightedEntity));
            wordDisplayValue.append(getDisplayValue(weightedEntity)).append(" ");
         }
         normalizedData.addReferredTokenPositions(recEntity.getReferedTokenPositions());
         normalizedData.addOriginalReferredTokenPositions(recEntity.getOriginalReferedTokenPositions());
      }

      normalizedData.setValue(wordValue.toString());
      normalizedData.setDisplayValue(wordDisplayValue.toString().trim());
      normalizedDataList.add(normalizedData);
      return normalizedDataList;
   }

   /**
    * This method updates the Time Frame normalized data
    * 
    * @param recognizedCloudEntity
    */
   // TODO:NK: Need to extend this to handle the entire time frame types(eg. quarter, day, week,etc)
   private List<INormalizedData> getTFNormalizedData (RecognizedCloudEntity recognizedCloudEntity) {
      // TODO handle for range.
      String cloudName = recognizedCloudEntity.getCloud().getName();
      if (cloudName.equalsIgnoreCase(NLPConstants.BETWEEN_AND_TIME_RANGE_CLOUD)
               || recognizedCloudEntity.getCloud().getName().equalsIgnoreCase(NLPConstants.FROM_TO_TIME_RANGE_CLOUD)) {
         return getRangeNormalizedData(recognizedCloudEntity, RecognizedType.TF_TYPE);
      }
      if (NLPConstants.WEEK_DAY_TF_CLOUD.equals(recognizedCloudEntity.getCloud().getName())) {
         return getTFNormalizedDataForWeekDay(recognizedCloudEntity);
      }
      if (cloudName.equalsIgnoreCase(NLPConstants.RELATIVE_YEAR_CLOUD)
               || cloudName.equalsIgnoreCase(NLPConstants.RELATIVE_MONTH_CLOUD)
               || cloudName.equalsIgnoreCase(NLPConstants.RELATIVE_QUARTER_CLOUD)
               || cloudName.equalsIgnoreCase(NLPConstants.RELATIVE_DAY_CLOUD)
               || cloudName.equalsIgnoreCase(NLPConstants.RELATIVE_WITH_OPERATOR_CLOUD)
               || cloudName.equalsIgnoreCase(NLPConstants.RELATIVE_TIME_CLOUD)
               || cloudName.equalsIgnoreCase(NLPConstants.RELATIVE_WEEK_CLOUD)) {
         return getRelativeTimeNormalizedData(recognizedCloudEntity);
      }
      for (IWeightedEntity weightedEntity : recognizedCloudEntity.getRecognitionEntities()) {
         RecognitionEntity recEntity = (RecognitionEntity) weightedEntity;
         if (recEntity.getNormalizedData() != null
                  && recEntity.getNormalizedData().getNormalizedDataType() == NormalizedDataType.RELATIVE_TIME_NORMALIZED_DATA) {
            return getRelativeTimeNormalizedData(recognizedCloudEntity);
         }
      }
      TFDataHolder tfDataHolder = getTFDataHolder(recognizedCloudEntity);
      List<IWeightedEntity> dayRecognitions = tfDataHolder.getDayRecognitions();
      List<IWeightedEntity> weekRecognitions = tfDataHolder.getWeekRecognitions();
      List<IWeightedEntity> monthRecognitions = tfDataHolder.getMonthRecognitions();
      List<IWeightedEntity> quarterRecognitions = tfDataHolder.getQuarterRecognitions();
      List<IWeightedEntity> yearRecognitions = tfDataHolder.getYearRecognitions();
      List<IWeightedEntity> timeRecognitions = tfDataHolder.getTimeRecognitions();
      List<IWeightedEntity> timeQualifierRecognitions = tfDataHolder.getTimeQualiFierRecognitions();
      List<IWeightedEntity> weekdayRecognitions = tfDataHolder.getWeekdayRecognitions();

      List<IWeightedEntity> valuePrepositionRecognitions = tfDataHolder.getValuePrepositionRecognitions();

      TimeFrameNormalizedData tfData = (TimeFrameNormalizedData) NormalizedCloudDataFactory
               .getNormalizedCloudData(NormalizedDataType.TIME_FRAME_NORMALIZED_DATA);
      tfData.setTimeFrameBedId(recognizedCloudEntity.getCloudOutputBedId());
      tfData.setType(recognizedCloudEntity.getCloudOutputName());

      ReferedTokenPosition rtp = new ReferedTokenPosition();
      ReferedTokenPosition rtpForOrigPos = new ReferedTokenPosition();
      Map<Long, BusinessEntityDefinition> typeBedMap = recognizedCloudEntity.getTypeBedMap();
      for (IWeightedEntity weightedEntity : recognizedCloudEntity.getRecognitionEntities()) {
         RecognitionEntity recEntity = (RecognitionEntity) weightedEntity;
         rtp.addAll(recEntity.getReferedTokenPositions());
         rtpForOrigPos.addAll(recEntity.getOriginalReferedTokenPositions());
      }
      tfData.setReferredTokenPositions(rtp.getReferedTokenPositions());
      tfData.setOriginalReferredTokenPositions(rtpForOrigPos.getReferedTokenPositions());
      boolean timeframeFound = false;

      String tfCloudName = recognizedCloudEntity.getCloud().getName();
      //TODO: NK: We should later change the way we get the time frame type to be more generic
      TimeFrameType timeFrameType = TimeFrameType.getTimeFrameType(tfCloudName);
      if (!CollectionUtils.isEmpty(timeRecognitions) && timeFrameType == TimeFrameType.TIME) {
         timeframeFound = populateTimeTFNormalizedData(dayRecognitions, monthRecognitions, yearRecognitions,
                  timeRecognitions, tfData, typeBedMap, timeframeFound, timeQualifierRecognitions, weekdayRecognitions,
                  recognizedCloudEntity);
      } else if (!CollectionUtils.isEmpty(weekRecognitions) && timeFrameType == TimeFrameType.WEEK_TIME_FRAME) {
         timeframeFound = populateWeekTFNormalizedData(weekRecognitions, monthRecognitions, yearRecognitions,
                  quarterRecognitions, tfData, typeBedMap, timeframeFound);
      } else if (!CollectionUtils.isEmpty(dayRecognitions) && timeFrameType == TimeFrameType.DAY_TIME_FRAME) {
         timeframeFound = populateDayTFNormalizedData(dayRecognitions, monthRecognitions, yearRecognitions, tfData,
                  typeBedMap, timeframeFound);
      } else if (!CollectionUtils.isEmpty(monthRecognitions) && timeFrameType == TimeFrameType.MONTH_TIME_FRAME) {
         tfData.setTimeFrameType(RecognizedType.MONTH_TYPE);
         IWeightedEntity weightedEntity = monthRecognitions.get(0);
         BusinessEntityDefinition typeBed = typeBedMap.get(((TypeEntity) weightedEntity).getTypeBedId());
         NormalizedDataEntity normalizeDataEntity = getTFNormalizeDataEntity(weightedEntity, typeBed);
         if (normalizeDataEntity != null) {
            timeframeFound = true;
            tfData.setMonth(normalizeDataEntity);
            if (!CollectionUtils.isEmpty(yearRecognitions)) {
               IWeightedEntity yearWeightedEntity = yearRecognitions.get(0);
               BusinessEntityDefinition yearTypeBed = typeBedMap.get(((TypeEntity) yearWeightedEntity).getTypeBedId());
               NormalizedDataEntity yearNormalizeDataEntity = nlpServiceHelper.getNormalizeDataEntity(
                        yearWeightedEntity, yearTypeBed);
               yearNormalizeDataEntity.setValue(yearNormalizeDataEntity.getValue().replaceAll("Year", ""));
               if (yearNormalizeDataEntity != null) {
                  tfData.setYear(yearNormalizeDataEntity);
               }
            } else {
               tfData.setDefaultAdded(true);
            }
         }
      } else if (!CollectionUtils.isEmpty(quarterRecognitions) && timeFrameType == TimeFrameType.QUARTER_TIME_FRAME) {
         tfData.setTimeFrameType(RecognizedType.QUARTER_TYPE);
         IWeightedEntity weightedEntity = quarterRecognitions.get(0);
         BusinessEntityDefinition typeBed = typeBedMap.get(((TypeEntity) weightedEntity).getTypeBedId());
         NormalizedDataEntity normalizeDataEntity = getTFNormalizeDataEntity(weightedEntity, typeBed);
         if (normalizeDataEntity != null) {
            timeframeFound = true;
            tfData.setQuarter(normalizeDataEntity);
            normalizeDataEntity.setValue(normalizeDataEntity.getValue().replaceAll("Quarter", ""));
            if (!CollectionUtils.isEmpty(yearRecognitions)) {
               IWeightedEntity yearWeightedEntity = yearRecognitions.get(0);
               BusinessEntityDefinition yearTypeBed = typeBedMap.get(((TypeEntity) yearWeightedEntity).getTypeBedId());
               NormalizedDataEntity yearNormalizeDataEntity = getTFNormalizeDataEntity(yearWeightedEntity, yearTypeBed);
               yearNormalizeDataEntity.setValue(yearNormalizeDataEntity.getValue().replaceAll("Year", ""));

               if (yearNormalizeDataEntity != null) {
                  tfData.setYear(yearNormalizeDataEntity);
               }
            } else {
               tfData.setDefaultAdded(true);
            }
         }
      } else if (!CollectionUtils.isEmpty(yearRecognitions)
               && (timeFrameType == TimeFrameType.YEAR_TIME_FRAME || timeFrameType == TimeFrameType.YEAR_TIME_FRAME_CONCEPT)) {
         tfData.setTimeFrameType(RecognizedType.YEAR_TYPE);
         IWeightedEntity weightedEntity = yearRecognitions.get(0);
         BusinessEntityDefinition typeBed = typeBedMap.get(((TypeEntity) weightedEntity).getTypeBedId());
         NormalizedDataEntity normalizeDataEntity = getTFNormalizeDataEntity(weightedEntity, typeBed);
         if (normalizeDataEntity != null) {
            timeframeFound = true;
            normalizeDataEntity.setValue(normalizeDataEntity.getValue().replaceAll("Year", ""));
            tfData.setYear(normalizeDataEntity);
         }

      }
      if (!CollectionUtils.isEmpty(valuePrepositionRecognitions)) {
         IWeightedEntity operatorEntity = valuePrepositionRecognitions.get(0);
         BusinessEntityDefinition typeBed = typeBedMap.get(((TypeEntity) operatorEntity).getTypeBedId());
         NormalizedDataEntity operatorNormalizeDataEntity = nlpServiceHelper.getNormalizeDataEntity(operatorEntity,
                  typeBed);
         if (operatorNormalizeDataEntity != null) {
            tfData.setValuePreposition(operatorNormalizeDataEntity);
         }
      }
      List<INormalizedData> tfDataList = new ArrayList<INormalizedData>(1);
      if (timeframeFound) {
         tfData.setDateQualifier(NLPUtilities.getDateQualifierBasedOnTFType(tfData));
         if (tfData.isRangeTimeFound()) {
            INormalizedData normalizedData = convertTFDataToRangeNormalizedData(tfData, timeRecognitions);
            if (normalizedData != null) {
               tfDataList.add(normalizedData);
            }
         } else {
            tfDataList.add(tfData);
         }
      }

      return tfDataList;
   }

   /**
    * @param tfData
    * @param timeRecognitions
    * @return
    */
   private INormalizedData convertTFDataToRangeNormalizedData (TimeFrameNormalizedData tfData,
            List<IWeightedEntity> timeRecognitions) {
      if (!CollectionUtils.isEmpty(timeRecognitions)) {
         InstanceEntity timeEntity = (InstanceEntity) timeRecognitions.get(0);
         TimeRange timeRange = getTimeConfigurationService().getTimeRange(timeEntity.getDefaultInstanceDisplayName());
         if (timeRange == null) {
            return null;
         }
         RangeNormalizedData rangeNormalizedData = new RangeNormalizedData();
         NormalizedDataEntity startNormalizedDataEntity = new NormalizedDataEntity();
         startNormalizedDataEntity.setTypeBedId(tfData.getTypeBedId());
         NormalizedDataEntity endNormalizedDataEntity = new NormalizedDataEntity();
         endNormalizedDataEntity.setTypeBedId(tfData.getTypeBedId());
         rangeNormalizedData.setTypeBedId(tfData.getTypeBedId());
         rangeNormalizedData.setType(tfData.getType());
         rangeNormalizedData.setReferredTokenPositions((TreeSet<Integer>) tfData.getReferredTokenPositions());
         rangeNormalizedData.setOriginalReferredTokenPositions((TreeSet<Integer>) tfData
                  .getOriginalReferredTokenPositions());
         try {
            TimeFrameNormalizedData start = (TimeFrameNormalizedData) tfData.clone();
            TimeFrameNormalizedData end = (TimeFrameNormalizedData) tfData.clone();
            NormalizedDataEntity startHourEntity = new NormalizedDataEntity();
            startHourEntity.setDisplayValue(timeRange.getStartHour());
            startHourEntity.setValue(timeRange.getStartHour());
            NormalizedDataEntity startMinuteEntity = new NormalizedDataEntity();
            startMinuteEntity.setDisplayValue(timeRange.getStartMinute());
            startMinuteEntity.setValue(timeRange.getStartMinute());
            NormalizedDataEntity startTmeQualifier = new NormalizedDataEntity();
            startTmeQualifier.setDisplayValue(timeRange.getStartTimeQualifier());
            startTmeQualifier.setValue(timeRange.getStartTimeQualifier());
            start.setTimeQualifier(startTmeQualifier);
            start.setHour(startHourEntity);
            start.setMinute(startMinuteEntity);
            startNormalizedDataEntity.setNormalizedData(start);
            startNormalizedDataEntity.setValue(start.getValue());
            startNormalizedDataEntity.setDisplayValue(start.getDisplayValue());

            NormalizedDataEntity endHourEntity = new NormalizedDataEntity();
            endHourEntity.setDisplayValue(timeRange.getEndHour());
            endHourEntity.setValue(timeRange.getEndHour());
            NormalizedDataEntity endMinuteEntity = new NormalizedDataEntity();
            endMinuteEntity.setDisplayValue(timeRange.getEndMinute());
            endMinuteEntity.setValue(timeRange.getEndMinute());
            NormalizedDataEntity endTmeQualifier = new NormalizedDataEntity();
            endTmeQualifier.setDisplayValue(timeRange.getEndTimeQualifier());
            endTmeQualifier.setValue(timeRange.getEndTimeQualifier());
            end.setTimeQualifier(endTmeQualifier);
            end.setHour(endHourEntity);
            end.setMinute(endMinuteEntity);
            endNormalizedDataEntity.setValue(end.getValue());
            endNormalizedDataEntity.setDisplayValue(end.getDisplayValue());
            endNormalizedDataEntity.setNormalizedData(end);
            rangeNormalizedData.setStart(startNormalizedDataEntity);
            rangeNormalizedData.setEnd(endNormalizedDataEntity);
            return rangeNormalizedData;

         } catch (CloneNotSupportedException e) {
            throw new NLPSystemException(NLPExceptionCodes.CLONE_FAILED, e);
         }

      }
      return null;
   }

   private List<INormalizedData> getTFNormalizedDataForWeekDay (RecognizedCloudEntity recognizedCloudEntity) {

      ReferedTokenPosition rtp = new ReferedTokenPosition();
      ReferedTokenPosition rtpForOrigPos = new ReferedTokenPosition();
      TFDataHolder dataHolder = getTFDataHolder(recognizedCloudEntity);
      List<IWeightedEntity> weekdayRecognitions = dataHolder.getWeekdayRecognitions();
      List<IWeightedEntity> monthRecognitions = dataHolder.getMonthRecognitions();
      List<IWeightedEntity> weekRecognitions = dataHolder.getWeekRecognitions();
      List<IWeightedEntity> yearRecognitions = dataHolder.getYearRecognitions();
      RecognitionEntity weekDay = (RecognitionEntity) weekdayRecognitions.get(0);
      WeekDayNormalizedDataComponent weekDayNormalizedDataComponent = getWeekDayComponent(recognizedCloudEntity,
               weekDay);
      rtp.addAll(weekDay.getReferedTokenPositions());
      rtpForOrigPos.addAll(weekDay.getOriginalReferedTokenPositions());
      RecognitionEntity weekRecognitionEntity = null;
      RecognitionEntity monthRecognitionEntity = null;
      RecognitionEntity yearRecognitionEntity = null;
      if (!CollectionUtils.isEmpty(monthRecognitions)) {
         monthRecognitionEntity = (RecognitionEntity) monthRecognitions.get(0);
      }
      if (!CollectionUtils.isEmpty(yearRecognitions)) {
         yearRecognitionEntity = (RecognitionEntity) yearRecognitions.get(0);
      }
      if (!CollectionUtils.isEmpty(weekRecognitions)) {
         weekRecognitionEntity = (RecognitionEntity) weekRecognitions.get(0);
      }
      TimeFrameNormalizedData tfData = null;
      if (monthRecognitionEntity != null
               && monthRecognitionEntity.getNormalizedData() != null
               && monthRecognitionEntity.getNormalizedData().getNormalizedDataType() == NormalizedDataType.RELATIVE_TIME_NORMALIZED_DATA
               || yearRecognitionEntity != null
               && yearRecognitionEntity.getNormalizedData().getNormalizedDataType() == NormalizedDataType.RELATIVE_TIME_NORMALIZED_DATA
               || weekRecognitionEntity != null
               && weekRecognitionEntity.getNormalizedData().getNormalizedDataType() == NormalizedDataType.RELATIVE_TIME_NORMALIZED_DATA) {
         tfData = (TimeFrameNormalizedData) NormalizedCloudDataFactory
                  .getNormalizedCloudData(NormalizedDataType.RELATIVE_TIME_NORMALIZED_DATA);
         if (weekRecognitionEntity != null) {
            if (weekRecognitionEntity.getNormalizedData() != null
                     && weekRecognitionEntity.getNormalizedData().getNormalizedDataType() == NormalizedDataType.RELATIVE_TIME_NORMALIZED_DATA) {
               ((RelativeTimeNormalizedData) tfData).setRelativeDateQualifier(DateQualifier.WEEK);
               ((RelativeTimeNormalizedData) tfData).setAdjective(((RelativeTimeNormalizedData) weekRecognitionEntity
                        .getNormalizedData()).getAdjective());
               ((RelativeTimeNormalizedData) tfData).setNumber(((RelativeTimeNormalizedData) weekRecognitionEntity
                        .getNormalizedData()).getNumber());

            } else {
               NormalizedDataEntity weekEntity = getNlpServiceHelper().getNormalizeDataEntity(weekRecognitionEntity,
                        recognizedCloudEntity.getTypeBedMap().get(((TypeEntity) weekRecognitionEntity).getTypeBedId()));
               tfData.setWeek(weekEntity);
            }
            rtp.addAll(weekRecognitionEntity.getReferedTokenPositions());
            rtpForOrigPos.addAll(weekRecognitionEntity.getOriginalReferedTokenPositions());
         }
         if (monthRecognitionEntity != null) {
            if (monthRecognitionEntity.getNormalizedData() != null
                     && monthRecognitionEntity.getNormalizedData().getNormalizedDataType() == NormalizedDataType.RELATIVE_TIME_NORMALIZED_DATA) {
               ((RelativeTimeNormalizedData) tfData).setRelativeDateQualifier(DateQualifier.MONTH);
               ((RelativeTimeNormalizedData) tfData).setAdjective(((RelativeTimeNormalizedData) monthRecognitionEntity
                        .getNormalizedData()).getAdjective());
               ((RelativeTimeNormalizedData) tfData).setNumber(((RelativeTimeNormalizedData) monthRecognitionEntity
                        .getNormalizedData()).getNumber());

            } else {
               NormalizedDataEntity monthEntity = getNlpServiceHelper()
                        .getNormalizeDataEntity(
                                 monthRecognitionEntity,
                                 recognizedCloudEntity.getTypeBedMap().get(
                                          ((TypeEntity) monthRecognitionEntity).getTypeBedId()));
               tfData.setMonth(monthEntity);
            }
            rtp.addAll(monthRecognitionEntity.getReferedTokenPositions());
            rtpForOrigPos.addAll(monthRecognitionEntity.getOriginalReferedTokenPositions());
         } else if (yearRecognitionEntity != null) {
            if (yearRecognitionEntity.getNormalizedData().getNormalizedDataType() == NormalizedDataType.RELATIVE_TIME_NORMALIZED_DATA) {
               ((RelativeTimeNormalizedData) tfData).setRelativeDateQualifier(DateQualifier.YEAR);
               ((RelativeTimeNormalizedData) tfData).setAdjective(((RelativeTimeNormalizedData) yearRecognitionEntity
                        .getNormalizedData()).getAdjective());
               ((RelativeTimeNormalizedData) tfData).setNumber(((RelativeTimeNormalizedData) yearRecognitionEntity
                        .getNormalizedData()).getNumber());

            } else {
               NormalizedDataEntity yearEntity = getNlpServiceHelper().getNormalizeDataEntity(yearRecognitionEntity,
                        recognizedCloudEntity.getTypeBedMap().get(((TypeEntity) yearRecognitionEntity).getTypeBedId()));
               tfData.setYear(yearEntity);
            }
            rtp.addAll(yearRecognitionEntity.getReferedTokenPositions());
            rtpForOrigPos.addAll(yearRecognitionEntity.getOriginalReferedTokenPositions());
         }
      } else {
         tfData = (TimeFrameNormalizedData) NormalizedCloudDataFactory
                  .getNormalizedCloudData(NormalizedDataType.TIME_FRAME_NORMALIZED_DATA);
         if (weekRecognitionEntity != null) {
            NormalizedDataEntity weekEntity = getNlpServiceHelper().getNormalizeDataEntity(weekRecognitionEntity,
                     recognizedCloudEntity.getTypeBedMap().get(((TypeEntity) weekRecognitionEntity).getTypeBedId()));
            tfData.setMonth(weekEntity);
            rtp.addAll(weekRecognitionEntity.getReferedTokenPositions());
            rtpForOrigPos.addAll(weekRecognitionEntity.getOriginalReferedTokenPositions());
         }
         if (monthRecognitionEntity != null) {
            NormalizedDataEntity monthEntity = getNlpServiceHelper().getNormalizeDataEntity(monthRecognitionEntity,
                     recognizedCloudEntity.getTypeBedMap().get(((TypeEntity) monthRecognitionEntity).getTypeBedId()));
            tfData.setMonth(monthEntity);
            rtp.addAll(monthRecognitionEntity.getReferedTokenPositions());
            rtpForOrigPos.addAll(monthRecognitionEntity.getOriginalReferedTokenPositions());
         }
         if (yearRecognitionEntity != null) {
            NormalizedDataEntity yearEntity = getNlpServiceHelper().getNormalizeDataEntity(yearRecognitionEntity,
                     recognizedCloudEntity.getTypeBedMap().get(((TypeEntity) yearRecognitionEntity).getTypeBedId()));
            yearEntity.setValue(yearEntity.getValue().replaceAll("Year", ""));
            tfData.setYear(yearEntity);
            rtp.addAll(yearRecognitionEntity.getReferedTokenPositions());
            rtpForOrigPos.addAll(yearRecognitionEntity.getOriginalReferedTokenPositions());
         }
      }
      tfData.setTimeFrameType(RecognizedType.WEEK_DAY_TYPE);
      tfData.setReferredTokenPositions(rtp.getReferedTokenPositions());
      tfData.setOriginalReferredTokenPositions(rtpForOrigPos.getReferedTokenPositions());
      tfData.setWeekDayNormalizedDataComponent(weekDayNormalizedDataComponent);
      tfData.setTimeFrameBedId(recognizedCloudEntity.getCloudOutputBedId());
      tfData.setType(recognizedCloudEntity.getCloudOutputName());
      tfData.setDateQualifier(NLPUtilities.getDateQualifierBasedOnTFType(tfData));
      if (tfData.getYear() == null) {
         tfData.setDefaultAdded(true);
      }
      List<INormalizedData> dataList = new ArrayList<INormalizedData>();
      dataList.add(tfData);
      return dataList;
   }

   /**
    * @param recognizedCloudEntity
    * @param weekDay
    * @return
    */
   private WeekDayNormalizedDataComponent getWeekDayComponent (RecognizedCloudEntity recognizedCloudEntity,
            RecognitionEntity weekDay) {
      INormalizedData normalizedData = weekDay.getNormalizedData();
      WeekDayNormalizedDataComponent weekDayNormalizedDataComponent = new WeekDayNormalizedDataComponent();
      if (normalizedData == null) {
         weekDayNormalizedDataComponent.setWeekDayBedId(((TypeEntity) weekDay).getTypeBedId());
         weekDayNormalizedDataComponent.setType(((TypeEntity) weekDay).getTypeDisplayName());
         // special handling if the instance recognized is weekend in this case we need to get the weekdays entities for
         // weekend
         if (((InstanceEntity) weekDay).getDefaultInstanceDisplayName().equalsIgnoreCase("Weekend")) {
            List<NormalizedDataEntity> weekendEntities = populateWeekendEntities(weekDay);
            weekDayNormalizedDataComponent.setWeekdays(weekendEntities);
            weekDayNormalizedDataComponent.setWeekend(true);
         } else {
            NormalizedDataEntity weekDayEntity = getNlpServiceHelper().getNormalizeDataEntity(weekDay,
                     recognizedCloudEntity.getTypeBedMap().get(((TypeEntity) weekDay).getTypeBedId()));
            weekDayNormalizedDataComponent.getWeekdays().add(weekDayEntity);
         }
      } else {
         WeekDayNormalizedData weekDayNormalizedData = (WeekDayNormalizedData) normalizedData;
         weekDayNormalizedDataComponent.setAdjective(weekDayNormalizedData.getAdjective());
         weekDayNormalizedDataComponent.setWeekDayBedId(weekDayNormalizedData.getWeekDayBedId());
         weekDayNormalizedDataComponent.setWeekdays(weekDayNormalizedData.getWeekdays());
         weekDayNormalizedDataComponent.setType(weekDayNormalizedData.getType());
         weekDayNormalizedDataComponent.setNumber(weekDayNormalizedData.getNumber());
         weekDayNormalizedDataComponent.setWeekend(weekDayNormalizedData.isWeekend());
      }
      return weekDayNormalizedDataComponent;
   }

   /**
    * populate Weekend entities. As of now Saturday And sunday are considered as weekend.
    * 
    * @param weekDay
    * @return
    */
   private List<NormalizedDataEntity> populateWeekendEntities (RecognitionEntity weekDay) {
      List<NormalizedDataEntity> enList = new ArrayList<NormalizedDataEntity>(1);
      NormalizedDataEntity satDataEntity = new NormalizedDataEntity();
      satDataEntity.setTypeBedId(((TypeEntity) weekDay).getTypeBedId());
      satDataEntity.setValue("Saturday");
      satDataEntity.setDisplayValue("Saturday");
      satDataEntity.setWeightInformation(((InstanceEntity) weekDay).getWeightInformation());

      NormalizedDataEntity sunDataEntity = new NormalizedDataEntity();
      sunDataEntity.setTypeBedId(((TypeEntity) weekDay).getTypeBedId());
      sunDataEntity.setValue("Sunday");
      sunDataEntity.setDisplayValue("Sunday");
      sunDataEntity.setWeightInformation(((InstanceEntity) weekDay).getWeightInformation());
      enList.add(satDataEntity);
      enList.add(sunDataEntity);
      return enList;
   }

   private boolean populateWeekTFNormalizedData (List<IWeightedEntity> weekRecognitions,
            List<IWeightedEntity> monthRecognitions, List<IWeightedEntity> yearRecognitions,
            List<IWeightedEntity> quarterRecognitions, TimeFrameNormalizedData tfData,
            Map<Long, BusinessEntityDefinition> typeBedMap, boolean timeframeFound) {
      tfData.setTimeFrameType(RecognizedType.WEEK_TYPE);
      IWeightedEntity weightedEntity = weekRecognitions.get(0);
      BusinessEntityDefinition typeBed = typeBedMap.get(((TypeEntity) weightedEntity).getTypeBedId());
      NormalizedDataEntity normalizeDataEntity = getTFNormalizeDataEntity(weightedEntity, typeBed);
      // TODO code to replace th end characters from day.
      normalizeDataEntity.setValue(normalizeDataEntity.getValue().replaceAll("th|rd|st|nd", ""));
      RecognitionEntity recEntity = (RecognitionEntity) weightedEntity;
      if (normalizeDataEntity != null) {
         timeframeFound = true;
         tfData.setWeek(normalizeDataEntity);
         if (!CollectionUtils.isEmpty(monthRecognitions)) {
            IWeightedEntity monthWeightedEntity = monthRecognitions.get(0);
            BusinessEntityDefinition monthTypeBed = typeBedMap.get(((TypeEntity) monthWeightedEntity).getTypeBedId());
            NormalizedDataEntity monthNormalizeDataEntity = nlpServiceHelper.getNormalizeDataEntity(
                     monthWeightedEntity, monthTypeBed);
            if (monthNormalizeDataEntity != null) {
               tfData.setMonth(monthNormalizeDataEntity);
            }
         } else {
            tfData.setDefaultAdded(true);
         }
         if (!CollectionUtils.isEmpty(quarterRecognitions)) {
            IWeightedEntity quarterEntity = quarterRecognitions.get(0);
            BusinessEntityDefinition monthTypeBed = typeBedMap.get(((TypeEntity) quarterEntity).getTypeBedId());
            NormalizedDataEntity monthNormalizeDataEntity = nlpServiceHelper.getNormalizeDataEntity(quarterEntity,
                     monthTypeBed);
            if (monthNormalizeDataEntity != null) {
               tfData.setQuarter(monthNormalizeDataEntity);
            }
         } else {
            tfData.setDefaultAdded(true);
         }
         if (!CollectionUtils.isEmpty(yearRecognitions)) {
            IWeightedEntity yearWeightedEntity = yearRecognitions.get(0);
            BusinessEntityDefinition yearTypeBed = typeBedMap.get(((TypeEntity) yearWeightedEntity).getTypeBedId());
            NormalizedDataEntity yearNormalizeDataEntity = nlpServiceHelper.getNormalizeDataEntity(yearWeightedEntity,
                     yearTypeBed);
            yearNormalizeDataEntity.setValue(yearNormalizeDataEntity.getValue().replaceAll("Year", ""));
            if (yearNormalizeDataEntity != null) {
               tfData.setYear(yearNormalizeDataEntity);
            }
         } else {
            tfData.setDefaultAdded(true);
         }
      }
      return timeframeFound;
   }

   private boolean populateTimeTFNormalizedData (List<IWeightedEntity> dayRecognitions,
            List<IWeightedEntity> monthRecognitions, List<IWeightedEntity> yearRecognitions,
            List<IWeightedEntity> timeRecognitions, TimeFrameNormalizedData tfData,
            Map<Long, BusinessEntityDefinition> typeBedMap, boolean timeframeFound,
            List<IWeightedEntity> timeQualifierRecognitions, List<IWeightedEntity> weekdayRecognitions,
            RecognizedCloudEntity recognizedCloudEntity) {
      tfData.setTimeFrameType(RecognizedType.TIME_TYPE);
      IWeightedEntity weightedEntity = timeRecognitions.get(0);
      RecognitionEntity timeRecEntity = (RecognitionEntity) weightedEntity;
      if (timeRecEntity.getNormalizedData() == null) {
         timeframeFound = true;
         boolean rangeTimeExist = checkIfRangeTimeFound(timeRecEntity);
         tfData.setRangeTimeFound(rangeTimeExist);
         if (!rangeTimeExist) {
            String word = timeRecEntity.getWord();
            List<String> asList = ExecueStringUtil.getAsList(word, ":");
            if (!CollectionUtils.isEmpty(asList)) {
               String hours = asList.get(0);
               NormalizedDataEntity normalizedDataEntity = new NormalizedDataEntity();
               normalizedDataEntity.setTypeBedId(207L);
               normalizedDataEntity.setValue(hours);
               normalizedDataEntity.setDisplayValue(hours);
               tfData.setHour(normalizedDataEntity);
               if (asList.size() > 1) {
                  String minutes = asList.get(1);
                  NormalizedDataEntity minuteNormalizedDataEntity = new NormalizedDataEntity();
                  minuteNormalizedDataEntity.setTypeBedId(208L);
                  minuteNormalizedDataEntity.setValue(minutes);
                  minuteNormalizedDataEntity.setDisplayValue(minutes);
                  tfData.setMinute(minuteNormalizedDataEntity);
               }
               if (asList.size() > 2) {
                  String seconds = asList.get(2);
                  NormalizedDataEntity secondNormalizedDataEntity = new NormalizedDataEntity();
                  secondNormalizedDataEntity.setTypeBedId(209L);
                  secondNormalizedDataEntity.setValue(seconds);
                  secondNormalizedDataEntity.setDisplayValue(seconds);
                  tfData.setSecond(secondNormalizedDataEntity);
               }
            }
         }
      }
      if (!CollectionUtils.isEmpty(timeQualifierRecognitions)) {
         IWeightedEntity timeQualifier = timeQualifierRecognitions.get(0);
         BusinessEntityDefinition timeQualifierBed = typeBedMap.get(((TypeEntity) timeQualifier).getTypeBedId());
         NormalizedDataEntity timeQualifierNormalizeDataEntity = nlpServiceHelper.getNormalizeDataEntity(timeQualifier,
                  timeQualifierBed);
         if (timeQualifierNormalizeDataEntity != null) {
            tfData.setTimeQualifier(timeQualifierNormalizeDataEntity);
         }
      }
      if (!CollectionUtils.isEmpty(weekdayRecognitions)) {
         WeekDayNormalizedDataComponent weekDayComponent = getWeekDayComponent(recognizedCloudEntity,
                  (RecognitionEntity) weekdayRecognitions.get(0));
         weekDayComponent.setTimeProvided(true);
         tfData.setWeekDayNormalizedDataComponent(weekDayComponent);
      }
      boolean directTFFound = false;
      if (!CollectionUtils.isEmpty(dayRecognitions)) {
         IWeightedEntity dayWeightedEntity = dayRecognitions.get(0);
         directTFFound = checkAndPopulateDirectDayTF(tfData, (RecognitionEntity) dayWeightedEntity);
         if (!directTFFound) {
            BusinessEntityDefinition dayTypeBed = typeBedMap.get(((TypeEntity) dayWeightedEntity).getTypeBedId());
            NormalizedDataEntity dayNormalizeDataEntity = nlpServiceHelper.getNormalizeDataEntity(dayWeightedEntity,
                     dayTypeBed);
            dayNormalizeDataEntity.setValue(dayNormalizeDataEntity.getValue().replaceAll("th|rd|st|nd", ""));

            if (dayNormalizeDataEntity != null) {
               tfData.setDay(dayNormalizeDataEntity);
            } else {
               tfData.setDefaultAdded(true);
            }
         }
      }
      if (!CollectionUtils.isEmpty(monthRecognitions) && !directTFFound) {
         IWeightedEntity monthWeightedEntity = monthRecognitions.get(0);
         BusinessEntityDefinition monthTypeBed = typeBedMap.get(((TypeEntity) monthWeightedEntity).getTypeBedId());
         NormalizedDataEntity monthNormalizeDataEntity = nlpServiceHelper.getNormalizeDataEntity(monthWeightedEntity,
                  monthTypeBed);
         if (monthNormalizeDataEntity != null) {
            tfData.setMonth(monthNormalizeDataEntity);
         }
      } else {
         tfData.setDefaultAdded(true);
      }
      if (!CollectionUtils.isEmpty(yearRecognitions) && !directTFFound) {
         IWeightedEntity yearWeightedEntity = yearRecognitions.get(0);
         BusinessEntityDefinition yearTypeBed = typeBedMap.get(((TypeEntity) yearWeightedEntity).getTypeBedId());
         NormalizedDataEntity yearNormalizeDataEntity = nlpServiceHelper.getNormalizeDataEntity(yearWeightedEntity,
                  yearTypeBed);
         yearNormalizeDataEntity.setValue(yearNormalizeDataEntity.getValue().replaceAll("Year", ""));
         if (yearNormalizeDataEntity != null) {
            tfData.setYear(yearNormalizeDataEntity);
         }
      } else {
         tfData.setDefaultAdded(true);
      }

      return timeframeFound;
   }

   private boolean checkIfRangeTimeFound (RecognitionEntity timeRecEntity) {
      if (timeRecEntity.getEntityType() == RecognitionEntityType.INSTANCE_ENTITY) {
         if (timeRecEntity.getNLPtag().equalsIgnoreCase("OTI")) {
            return true;
         }
      }
      return false;
   }

   private boolean populateDayTFNormalizedData (List<IWeightedEntity> dayRecognitions,
            List<IWeightedEntity> monthRecognitions, List<IWeightedEntity> yearRecognitions,
            TimeFrameNormalizedData tfData, Map<Long, BusinessEntityDefinition> typeBedMap, boolean timeframeFound) {
      tfData.setTimeFrameType(RecognizedType.DAY_TYPE);
      RecognitionEntity weightedEntity = (RecognitionEntity) dayRecognitions.get(0);
      boolean directTFFound = checkAndPopulateDirectDayTF(tfData, weightedEntity);
      if (directTFFound) {
         return directTFFound;
      }
      BusinessEntityDefinition typeBed = typeBedMap.get(((TypeEntity) weightedEntity).getTypeBedId());
      NormalizedDataEntity normalizeDataEntity = getTFNormalizeDataEntity(weightedEntity, typeBed);
      // TODO code to replace th end characters from day.
      normalizeDataEntity.setValue(normalizeDataEntity.getValue().replaceAll("th|rd|st|nd", ""));
      RecognitionEntity recEntity = weightedEntity;
      if (normalizeDataEntity != null) {
         timeframeFound = true;
         tfData.setDay(normalizeDataEntity);
         if (!CollectionUtils.isEmpty(monthRecognitions)) {
            IWeightedEntity monthWeightedEntity = monthRecognitions.get(0);
            BusinessEntityDefinition monthTypeBed = typeBedMap.get(((TypeEntity) monthWeightedEntity).getTypeBedId());
            NormalizedDataEntity monthNormalizeDataEntity = nlpServiceHelper.getNormalizeDataEntity(
                     monthWeightedEntity, monthTypeBed);
            if (monthNormalizeDataEntity != null) {
               tfData.setMonth(monthNormalizeDataEntity);
            }
         } else if (recEntity.getNormalizedData() == null
                  || recEntity.getNormalizedData().getNormalizedDataType() != NormalizedDataType.RELATIVE_TIME_NORMALIZED_DATA) {
            return false;
         }
         if (!CollectionUtils.isEmpty(yearRecognitions)) {
            IWeightedEntity yearWeightedEntity = yearRecognitions.get(0);
            BusinessEntityDefinition yearTypeBed = typeBedMap.get(((TypeEntity) yearWeightedEntity).getTypeBedId());
            NormalizedDataEntity yearNormalizeDataEntity = nlpServiceHelper.getNormalizeDataEntity(yearWeightedEntity,
                     yearTypeBed);
            yearNormalizeDataEntity.setValue(yearNormalizeDataEntity.getValue().replaceAll("Year", ""));
            if (yearNormalizeDataEntity != null) {
               tfData.setYear(yearNormalizeDataEntity);
            }
         } else {
            tfData.setDefaultAdded(true);
         }
      }
      return timeframeFound;
   }

   /**
    * Method to populate the direct Day Instance such a today, tomorrow etc based on current callender dates.
    * 
    * @param tfData
    * @param weightedEntity
    * @return
    */
   private boolean checkAndPopulateDirectDayTF (TimeFrameNormalizedData tfData, RecognitionEntity weightedEntity) {
      if (weightedEntity.getEntityType() == RecognitionEntityType.INSTANCE_ENTITY) {
         InstanceEntity instanceEntity = (InstanceEntity) weightedEntity;
         if (instanceEntity.getDefaultInstanceValue().equalsIgnoreCase(NLPConstants.NLP_INSTANCE_NAME_TODAY)) {
            populateTFNormalizedData(Calendar.getInstance(), tfData);
            return true;
         }
         if (instanceEntity.getDefaultInstanceValue().equalsIgnoreCase(NLPConstants.NLP_INSTANCE_NAME_TOMORROW)) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, 1);
            populateTFNormalizedData(calendar, tfData);
            return true;
         }
         if (instanceEntity.getDefaultInstanceValue().equalsIgnoreCase(NLPConstants.NLP_INSTANCE_NAME_YESTERDAY)) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -1);
            populateTFNormalizedData(calendar, tfData);
            return true;
         }
         if (instanceEntity.getDefaultInstanceValue().equalsIgnoreCase(
                  NLPConstants.NLP_INSTANCE_NAME_DAY_BEFORE_YESTERDAY)) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -2);
            populateTFNormalizedData(calendar, tfData);
            return true;
         }
         if (instanceEntity.getDefaultInstanceValue().equalsIgnoreCase(
                  NLPConstants.NLP_INSTANCE_NAME_DAY_AFTER_TOMORROW)) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, 2);
            populateTFNormalizedData(calendar, tfData);
            return true;
         }
      }
      return false;
   }

   /**
    * Populate the pssed TF data based on the passed calaender object
    * 
    * @param calendar
    * @param timeFrameNormalizedData
    * @return
    */
   private TimeFrameNormalizedData populateTFNormalizedData (Calendar calendar,
            TimeFrameNormalizedData timeFrameNormalizedData) {
      timeFrameNormalizedData.setYear(populateNormalizedDataEntity(calendar.get(Calendar.YEAR) + ""));
      timeFrameNormalizedData.setMonth(populateNormalizedDataEntity(calendar.getDisplayName(Calendar.MONTH,
               Calendar.LONG, Locale.getDefault())
               + ""));
      timeFrameNormalizedData.setDay(populateNormalizedDataEntity(calendar.get(Calendar.DAY_OF_MONTH) + ""));
      return timeFrameNormalizedData;
   }

   public NormalizedDataEntity populateNormalizedDataEntity (String value) {
      NormalizedDataEntity normalizedDataEntity = new NormalizedDataEntity();
      normalizedDataEntity.setValue(value);
      normalizedDataEntity.setDisplayValue(value);
      return normalizedDataEntity;
   }

   private List<INormalizedData> getValueNormalizedDataWithPrepositions (RecognizedCloudEntity recognizedCloudEntity) {

      // Get the OPValueData holder
      ValuePrepositionDataHolder valueDataHolder = getValuePrepositionDataHolder(recognizedCloudEntity);
      List<IWeightedEntity> valueRecognitions = valueDataHolder.getValueRecognitions();
      List<IWeightedEntity> valuePrepositions = valueDataHolder.getValuePrepositionRecognitions();
      // get the type bed map
      Map<Long, BusinessEntityDefinition> typeBedMap = recognizedCloudEntity.getTypeBedMap();

      ValueRealizationNormalizedData normalizedOPValueData = (ValueRealizationNormalizedData) NormalizedCloudDataFactory
               .getNormalizedCloudData(NormalizedDataType.VALUE_NORMALIZED_DATA);
      normalizedOPValueData.setValueTypeBeId(recognizedCloudEntity.getCloudOutputBedId());
      normalizedOPValueData.setType(recognizedCloudEntity.getCloudOutputName());

      List<INormalizedData> normalizedDataList = new ArrayList<INormalizedData>();

      if (!CollectionUtils.isEmpty(valuePrepositions)) {
         for (IWeightedEntity valuePreposition : valuePrepositions) {
            BusinessEntityDefinition typeBed = typeBedMap.get(((TypeEntity) valuePreposition).getTypeBedId());
            NormalizedDataEntity preposition = nlpServiceHelper.getNormalizeDataEntity(valuePreposition, typeBed);
            normalizedOPValueData.setValuePreposition(preposition);
            normalizedOPValueData.addReferredTokenPositions(((RecognitionEntity) valuePreposition)
                     .getReferedTokenPositions());
            normalizedOPValueData.addOriginalReferredTokenPositions(((RecognitionEntity) valuePreposition)
                     .getOriginalReferedTokenPositions());
         }
      }

      if (!CollectionUtils.isEmpty(valueRecognitions)) {
         for (IWeightedEntity unitRecognition : valueRecognitions) {
            // Clone the normalizedTFData and set the year recognitions
            ValueRealizationNormalizedData clonedOPValueNormalizedData = null;
            try {
               clonedOPValueNormalizedData = (ValueRealizationNormalizedData) normalizedOPValueData.clone();
            } catch (CloneNotSupportedException e1) {
               throw new NLPSystemException(NLPExceptionCodes.CLONE_FAILED, e1);
            }
            ValueRealizationNormalizedData valueNormalizedData = (ValueRealizationNormalizedData) ((RecognitionEntity) unitRecognition)
                     .getNormalizedData();
            if (valueNormalizedData != null) {
               if (valueNormalizedData.getValuePreposition() != null) {
                  return normalizedDataList;
               }
               clonedOPValueNormalizedData.setNumber(valueNormalizedData.getNumber());
               clonedOPValueNormalizedData.setUnitScale(valueNormalizedData.getUnitScale());
               clonedOPValueNormalizedData.setUnitScale(valueNormalizedData.getUnitScale());
               clonedOPValueNormalizedData.setUnitSymbol(valueNormalizedData.getUnitSymbol());
               clonedOPValueNormalizedData.addReferredTokenPositions(valueNormalizedData.getReferredTokenPositions());
               clonedOPValueNormalizedData.addOriginalReferredTokenPositions(valueNormalizedData
                        .getOriginalReferredTokenPositions());
            }
            normalizedDataList.add(clonedOPValueNormalizedData);
         }
      }
      return normalizedDataList;
   }

   private List<INormalizedData> getOPValueNormalizedData (RecognizedCloudEntity recognizedCloudEntity) {
      // check for range
      if (recognizedCloudEntity.getCloud().getName().equalsIgnoreCase(NLPConstants.BETWEEN_AND_UNIT_RANGE_CLOUD)
               || recognizedCloudEntity.getCloud().getName().equalsIgnoreCase(NLPConstants.FROM_TO_UNIT_RANGE_CLOUD)) {
         return getRangeNormalizedData(recognizedCloudEntity, RecognizedType.UNIT_TYPE);
      } else if (recognizedCloudEntity.getCloud().getName()
               .equalsIgnoreCase(NLPConstants.BETWEEN_AND_VALUE_RANGE_CLOUD)
               || recognizedCloudEntity.getCloud().getName().equalsIgnoreCase(NLPConstants.FROM_TO_VALUE_RANGE_CLOUD)) {
         return getRangeNormalizedData(recognizedCloudEntity, RecognizedType.VALUE_TYPE);
      }
      // Get the OPValueData holder
      OPValueDataHolder opValueDataHolder = getOPValueDataHolder(recognizedCloudEntity);
      List<IWeightedEntity> operatorRecognitions = opValueDataHolder.getOperatorRecognitions();
      List<IWeightedEntity> unitRecognitions = opValueDataHolder.getUnitRecognitions();
      List<IWeightedEntity> valuePrepositions = opValueDataHolder.getValuePrepositionRecognitions();
      List<IWeightedEntity> unitSymbolRecognitions = opValueDataHolder.getUnitSymbolRecognitions();
      // get the type bed map
      Map<Long, BusinessEntityDefinition> typeBedMap = recognizedCloudEntity.getTypeBedMap();

      ValueRealizationNormalizedData normalizedOPValueData = (ValueRealizationNormalizedData) NormalizedCloudDataFactory
               .getNormalizedCloudData(NormalizedDataType.VALUE_NORMALIZED_DATA);
      normalizedOPValueData.setValueTypeBeId(recognizedCloudEntity.getCloudOutputBedId());
      normalizedOPValueData.setType(recognizedCloudEntity.getCloudOutputName());

      List<INormalizedData> normalizedDataList = new ArrayList<INormalizedData>();

      // prepare the opvalue normalized data list
      if (!CollectionUtils.isEmpty(operatorRecognitions)) {
         for (IWeightedEntity operatorRecognition : operatorRecognitions) {
            BusinessEntityDefinition typeBed = typeBedMap.get(((TypeEntity) operatorRecognition).getTypeBedId());
            NormalizedDataEntity operator = nlpServiceHelper.getNormalizeDataEntity(operatorRecognition, typeBed);
            normalizedOPValueData.setOperator(operator);
            normalizedOPValueData.addReferredTokenPositions(((RecognitionEntity) operatorRecognition)
                     .getReferedTokenPositions());
            normalizedOPValueData.addOriginalReferredTokenPositions(((RecognitionEntity) operatorRecognition)
                     .getOriginalReferedTokenPositions());
         }
      }
      if (!CollectionUtils.isEmpty(valuePrepositions)) {
         for (IWeightedEntity valuePreposition : valuePrepositions) {
            BusinessEntityDefinition typeBed = typeBedMap.get(((TypeEntity) valuePreposition).getTypeBedId());
            NormalizedDataEntity preposition = nlpServiceHelper.getNormalizeDataEntity(valuePreposition, typeBed);
            normalizedOPValueData.setValuePreposition(preposition);
            normalizedOPValueData.addReferredTokenPositions(((RecognitionEntity) valuePreposition)
                     .getReferedTokenPositions());
            normalizedOPValueData.addOriginalReferredTokenPositions(((RecognitionEntity) valuePreposition)
                     .getOriginalReferedTokenPositions());
         }
      }
      if (!CollectionUtils.isEmpty(unitSymbolRecognitions)) {
         for (IWeightedEntity unitSymbolRecognition : unitSymbolRecognitions) {
            BusinessEntityDefinition typeBed = typeBedMap.get(((TypeEntity) unitSymbolRecognition).getTypeBedId());
            RecognizedType recognizedType = NLPUtilities.getRecognizedType(typeBed);

            NormalizedDataEntity unitSymbol = nlpServiceHelper.getNormalizeDataEntity(unitSymbolRecognition, typeBed);
            if (recognizedType == RecognizedType.MONTH_TYPE || recognizedType == RecognizedType.YEAR_TYPE
                     || recognizedType == RecognizedType.DAY_TYPE || recognizedType == RecognizedType.WEEK_TYPE) {
               unitSymbol.setValueBedId(typeBed.getId());
            }
            normalizedOPValueData.setUnitSymbol(unitSymbol);
            normalizedOPValueData.addReferredTokenPositions(((RecognitionEntity) unitSymbolRecognition)
                     .getReferedTokenPositions());
            normalizedOPValueData.addOriginalReferredTokenPositions(((RecognitionEntity) unitSymbolRecognition)
                     .getOriginalReferedTokenPositions());
         }
      }
      if (!CollectionUtils.isEmpty(unitRecognitions)) {
         for (IWeightedEntity unitRecognition : unitRecognitions) {
            // Clone the normalizedTFData and set the year recognitions
            ValueRealizationNormalizedData clonedOPValueNormalizedData = null;
            try {
               clonedOPValueNormalizedData = (ValueRealizationNormalizedData) normalizedOPValueData.clone();
            } catch (CloneNotSupportedException e1) {
               e1.printStackTrace();
            }
            UnitNormalizedData unitNormalizedData = (UnitNormalizedData) ((RecognitionEntity) unitRecognition)
                     .getNormalizedData();
            if (unitNormalizedData != null) {
               clonedOPValueNormalizedData.setNumber(unitNormalizedData.getNumber());
               clonedOPValueNormalizedData.setUnitScale(unitNormalizedData.getUnitScale());
               clonedOPValueNormalizedData.addReferredTokenPositions(unitNormalizedData.getReferredTokenPositions());
               clonedOPValueNormalizedData.addOriginalReferredTokenPositions(unitNormalizedData
                        .getOriginalReferredTokenPositions());
            }
            normalizedDataList.add(clonedOPValueNormalizedData);
         }
      }
      return normalizedDataList;
   }

   /**
    * Method top get the Normalized Data for relative time frame pattern
    * 
    * @param recognizedCloudEntity
    * @return
    */
   private List<INormalizedData> getRelativeTimeNormalizedData (RecognizedCloudEntity recognizedCloudEntity) {
      List<IWeightedEntity> recognitionEntities = recognizedCloudEntity.getRecognitionEntities();
      ReferedTokenPosition rtp = new ReferedTokenPosition();
      ReferedTokenPosition rtpForOrigPos = new ReferedTokenPosition();
      RelativeTimeNormalizedData normalizedData = (RelativeTimeNormalizedData) NormalizedCloudDataFactory
               .getNormalizedCloudData(NormalizedDataType.RELATIVE_TIME_NORMALIZED_DATA);

      Map<Long, BusinessEntityDefinition> typeBedMap = recognizedCloudEntity.getTypeBedMap();
      boolean fromRelativeEntity = false;
      for (IWeightedEntity weightedEntity : recognitionEntities) {
         RecognitionEntity recEntity = (RecognitionEntity) weightedEntity;
         if (recEntity.getNormalizedData() != null
                  && recEntity.getNormalizedData().getNormalizedDataType() == NormalizedDataType.RELATIVE_TIME_NORMALIZED_DATA) {
            normalizedData = (RelativeTimeNormalizedData) recEntity.getNormalizedData();
            fromRelativeEntity = true;
         }
         RecognizedType timeFrameType = normalizedData.getTimeFrameType();
         if (timeFrameType == null
                  && normalizedData.getNormalizedDataType() == NormalizedDataType.RELATIVE_TIME_NORMALIZED_DATA) {
            DateQualifier relativeDateQualifier = normalizedData.getRelativeDateQualifier();
            if (relativeDateQualifier != null) {
               timeFrameType = RecognizedType.getCloudType(relativeDateQualifier.getValue());
            }
         }
         BusinessEntityDefinition typeBed = typeBedMap.get(((TypeEntity) weightedEntity).getTypeBedId());
         RecognizedType recognizedTypeForEntity = NLPUtilities.getRecognizedType(typeBed);
         if (recognizedTypeForEntity == RecognizedType.MONTH_TYPE
                  || recognizedTypeForEntity == RecognizedType.QUARTER_TYPE
                  || recognizedTypeForEntity == RecognizedType.YEAR_TYPE
                  || recognizedTypeForEntity == RecognizedType.DAY_TYPE
                  || recognizedTypeForEntity == RecognizedType.HOUR_TYPE
                  || recognizedTypeForEntity == RecognizedType.MINUTE_TYPE
                  || recognizedTypeForEntity == RecognizedType.SECOND_TYPE
                  || recognizedTypeForEntity == RecognizedType.WEEK_TYPE) {
            if (fromRelativeEntity) {
               if (recognizedTypeForEntity == RecognizedType.YEAR_TYPE) {
                  NormalizedDataEntity yearNormalizeDataEntity = nlpServiceHelper.getNormalizeDataEntity(
                           weightedEntity, typeBed);
                  yearNormalizeDataEntity.setValue(yearNormalizeDataEntity.getValue().replaceAll("Year", ""));
                  normalizedData.setYear(yearNormalizeDataEntity);
               } else {
                  if (timeFrameType != null
                           && (timeFrameType == RecognizedType.DAY_TYPE || timeFrameType == RecognizedType.HOUR_TYPE
                                    || timeFrameType == RecognizedType.MINUTE_TYPE
                                    || timeFrameType == RecognizedType.SECOND_TYPE
                                    || timeFrameType == RecognizedType.WEEK_DAY_TYPE || timeFrameType == RecognizedType.WEEK_TYPE)
                           && recognizedTypeForEntity == RecognizedType.MONTH_TYPE) {
                     normalizedData.setMonth(nlpServiceHelper.getNormalizeDataEntity(weightedEntity, typeBed));
                  } else if (timeFrameType != null
                           && (timeFrameType == RecognizedType.HOUR_TYPE || timeFrameType == RecognizedType.MINUTE_TYPE || timeFrameType == RecognizedType.SECOND_TYPE)
                           && recognizedTypeForEntity == RecognizedType.DAY_TYPE) {
                     normalizedData.setDay(nlpServiceHelper.getNormalizeDataEntity(weightedEntity, typeBed));
                  }
               }
            } else {
               normalizedData.setRelativeDateQualifier(DateQualifier.getType(recognizedTypeForEntity.getValue()));
            }

         } else if (recognizedTypeForEntity == RecognizedType.ADJECTIVE_TYPE) {
            normalizedData.setAdjective(nlpServiceHelper.getNormalizeDataEntity(weightedEntity, typeBed));
         } else if (recognizedTypeForEntity == RecognizedType.NUMBER_TYPE) {
            normalizedData.setNumber(nlpServiceHelper.getNormalizeDataEntity(weightedEntity, typeBed));
         } else if (recognizedTypeForEntity == RecognizedType.OPERATOR_TYPE) {
            normalizedData.setOperator(nlpServiceHelper.getNormalizeDataEntity(weightedEntity, typeBed));
         }
         rtp.addAll(recEntity.getReferedTokenPositions());
         rtpForOrigPos.addAll(recEntity.getOriginalReferedTokenPositions());
      }
      normalizedData.setTimeFrameBedId(recognizedCloudEntity.getCloudOutputBedId());
      normalizedData.setType(recognizedCloudEntity.getCloudOutputName());
      normalizedData.setReferredTokenPositions(rtp.getReferedTokenPositions());
      normalizedData.setOriginalReferredTokenPositions(rtpForOrigPos.getReferedTokenPositions());
      List<INormalizedData> normalizedDataList = new ArrayList<INormalizedData>(1);
      // If relative time type is not present then return empty list
      if (normalizedData.getRelativeDateQualifier() == null) {
         return normalizedDataList;
      }
      normalizedData.setDateQualifier(NLPUtilities.getDateQualifierBasedOnTFType(normalizedData));
      if (normalizedData.getDateQualifier() == null) {
         normalizedData.setDateQualifier(normalizedData.getRelativeDateQualifier());
      }
      normalizedDataList.add(normalizedData);
      return normalizedDataList;
   }

   private String getValue (IWeightedEntity weightedEntity) {
      if (weightedEntity instanceof InstanceEntity) {
         return ((InstanceEntity) weightedEntity).getDefaultInstanceValue();
      } else if (weightedEntity instanceof ConceptEntity) {
         return ((ConceptEntity) weightedEntity).getConceptDisplayName();
      } else {
         return ((OntoEntity) weightedEntity).getWord();
      }
   }

   private String getDisplayValue (IWeightedEntity weightedEntity) {
      if (weightedEntity instanceof InstanceEntity) {
         return ((InstanceEntity) weightedEntity).getDefaultInstanceDisplayName();
      } else if (weightedEntity instanceof ConceptEntity) {
         return ((ConceptEntity) weightedEntity).getConceptDisplayName();
      } else {
         return ((OntoEntity) weightedEntity).getDisplayName();
      }
   }

   /**
    * Method top get the Normalized Data for Range type pattern for Units.
    * 
    * @param recognizedCloudEntity
    * @return
    */
   private List<INormalizedData> getRangeNormalizedData (RecognizedCloudEntity recognizedCloudEntity,
            RecognizedType recognizedType) {
      List<IWeightedEntity> recognitionEntities = recognizedCloudEntity.getRecognitionEntities();
      ReferedTokenPosition rtp = new ReferedTokenPosition();
      ReferedTokenPosition rtpForOrigPos = new ReferedTokenPosition();
      RangeNormalizedData normalizedData = (RangeNormalizedData) NormalizedCloudDataFactory
               .getNormalizedCloudData(NormalizedDataType.RANGE_NORMALIZED_DATA);
      normalizedData.setTypeBedId(recognizedCloudEntity.getCloudOutputBedId());
      normalizedData.setType(recognizedCloudEntity.getCloudOutputName());
      List<INormalizedData> normalizedDataList = new ArrayList<INormalizedData>(1);
      Map<Long, BusinessEntityDefinition> typeBedMap = recognizedCloudEntity.getTypeBedMap();
      for (IWeightedEntity weightedEntity : recognitionEntities) {
         RecognitionEntity recEntity = (RecognitionEntity) weightedEntity;
         BusinessEntityDefinition typeBed = typeBedMap.get(((TypeEntity) weightedEntity).getTypeBedId());
         RecognizedType recognizedTypeForEntity = NLPUtilities.getRecognizedType(typeBed);
         if (recognizedType == recognizedTypeForEntity) {
            if (normalizedData.getStart() == null) {
               NormalizedDataEntity normalizedDataEntity = nlpServiceHelper.getNormalizeDataEntity(recEntity, typeBed);
               if (recEntity.getNormalizedData().getNormalizedDataType() == NormalizedDataType.RANGE_NORMALIZED_DATA) {
                  return normalizedDataList;
               }
               normalizedDataEntity.setNormalizedData(recEntity.getNormalizedData());
               normalizedData.setStart(normalizedDataEntity);
            } else {
               NormalizedDataEntity normalizedDataEntity = nlpServiceHelper.getNormalizeDataEntity(recEntity, typeBed);
               normalizedDataEntity.setNormalizedData(recEntity.getNormalizedData());
               normalizedData.setEnd(normalizedDataEntity);
            }
         }
         rtp.addAll(recEntity.getReferedTokenPositions());
         rtpForOrigPos.addAll(recEntity.getOriginalReferedTokenPositions());
      }
      normalizedData.setReferredTokenPositions(rtp.getReferedTokenPositions());
      normalizedData.setOriginalReferredTokenPositions(rtpForOrigPos.getReferedTokenPositions());
      // If start or end is not found, then return the empty list
      if (normalizedData.getStart() == null || normalizedData.getEnd() == null) {
         return normalizedDataList;
      }
      normalizedDataList.add(normalizedData);
      return normalizedDataList;
   }

   private List<INormalizedData> getUnitNormalizedData (RecognizedCloudEntity recognizedCloudEntity) {

      // NK: Get the OPValueData holder
      UnitDataHolder opValueDataHolder = getUnitDataHolder(recognizedCloudEntity);
      List<IWeightedEntity> numberRecognitions = opValueDataHolder.getNumberRecognitions();
      List<IWeightedEntity> unitScaleRecognitions = opValueDataHolder.getUnitScaleRecognitions();
      List<IWeightedEntity> unitSymbolRecognitions = opValueDataHolder.getUnitSymbolRecognitions();

      // get the type bed map
      Map<Long, BusinessEntityDefinition> typeBedMap = recognizedCloudEntity.getTypeBedMap();
      UnitNormalizedData unitNormalizedData = (UnitNormalizedData) NormalizedCloudDataFactory
               .getNormalizedCloudData(NormalizedDataType.UNIT_NORMALIZED_DATA);
      List<INormalizedData> normalizedDataList = new ArrayList<INormalizedData>();

      // prepare the opvalue normalized data list
      if (!CollectionUtils.isEmpty(numberRecognitions)) {
         for (IWeightedEntity numberRecognition : numberRecognitions) {

            // Clone the unitNormalizedData and set the number recognitions
            UnitNormalizedData clonedUnitNormalizedData = null;
            try {
               clonedUnitNormalizedData = (UnitNormalizedData) unitNormalizedData.clone();
            } catch (CloneNotSupportedException e1) {
               e1.printStackTrace();
            }
            BusinessEntityDefinition typeBed = typeBedMap.get(((TypeEntity) numberRecognition).getTypeBedId());
            NormalizedDataEntity numberDataEntity = nlpServiceHelper.getNormalizeDataEntity(numberRecognition, typeBed);
            clonedUnitNormalizedData.setUnitTypeBeId(recognizedCloudEntity.getCloudOutputBedId());
            clonedUnitNormalizedData.setType(recognizedCloudEntity.getCloudOutputName());
            clonedUnitNormalizedData.setNumber(numberDataEntity);
            clonedUnitNormalizedData.addReferredTokenPositions(((RecognitionEntity) numberRecognition)
                     .getReferedTokenPositions());
            clonedUnitNormalizedData.addOriginalReferredTokenPositions(((RecognitionEntity) numberRecognition)
                     .getOriginalReferedTokenPositions());
            if (!CollectionUtils.isEmpty(unitScaleRecognitions)) {
               for (IWeightedEntity unitScaleRecognition : unitScaleRecognitions) {
                  BusinessEntityDefinition unitTypeBed = typeBedMap.get(((TypeEntity) unitScaleRecognition)
                           .getTypeBedId());

                  NormalizedDataEntity unitScale = nlpServiceHelper.getNormalizeDataEntity(unitScaleRecognition,
                           unitTypeBed);
                  clonedUnitNormalizedData.setUnitScale(unitScale);
                  clonedUnitNormalizedData.addReferredTokenPositions(((RecognitionEntity) unitScaleRecognition)
                           .getReferedTokenPositions());
                  clonedUnitNormalizedData.addOriginalReferredTokenPositions(((RecognitionEntity) unitScaleRecognition)
                           .getOriginalReferedTokenPositions());
               }
            }
            if (!CollectionUtils.isEmpty(unitSymbolRecognitions)) {
               for (IWeightedEntity unitSymbolRecognition : unitSymbolRecognitions) {
                  NormalizedDataEntity unitSymbol = nlpServiceHelper.getNormalizeDataEntity(unitSymbolRecognition,
                           typeBed);
                  clonedUnitNormalizedData.setUnitSymbol(unitSymbol);
                  clonedUnitNormalizedData.addReferredTokenPositions(((RecognitionEntity) unitSymbolRecognition)
                           .getReferedTokenPositions());
                  clonedUnitNormalizedData
                           .addOriginalReferredTokenPositions(((RecognitionEntity) unitSymbolRecognition)
                                    .getOriginalReferedTokenPositions());
               }
            }
            normalizedDataList.add(clonedUnitNormalizedData);
         }
      }
      return normalizedDataList;
   }

   private TFDataHolder getTFDataHolder (RecognizedCloudEntity recognizedCloudEntity) {
      Map<Long, BusinessEntityDefinition> typeBedMap = recognizedCloudEntity.getTypeBedMap();
      List<IWeightedEntity> yearRecognitions = new ArrayList<IWeightedEntity>();
      List<IWeightedEntity> quarterRecognitions = new ArrayList<IWeightedEntity>();
      List<IWeightedEntity> monthRecognitions = new ArrayList<IWeightedEntity>();
      List<IWeightedEntity> dayRecognitions = new ArrayList<IWeightedEntity>();
      List<IWeightedEntity> weekRecognitions = new ArrayList<IWeightedEntity>();
      List<IWeightedEntity> weekdayRecognitions = new ArrayList<IWeightedEntity>();
      List<IWeightedEntity> valuePrepositions = new ArrayList<IWeightedEntity>();
      List<IWeightedEntity> timeRecognition = new ArrayList<IWeightedEntity>(1);
      List<IWeightedEntity> timeQualifierRecognitions = new ArrayList<IWeightedEntity>(1);
      List<IWeightedEntity> recognitionEntities = recognizedCloudEntity.getRecognitionEntities();
      for (IWeightedEntity weightedEntity : recognitionEntities) {
         BusinessEntityDefinition typeBed = typeBedMap.get(((TypeEntity) weightedEntity).getTypeBedId());
         RecognizedType recognizedType = NLPUtilities.getRecognizedType(typeBed);
         if (recognizedType == RecognizedType.DAY_TYPE) {
            dayRecognitions.add(weightedEntity);
         } else if (recognizedType == RecognizedType.MONTH_TYPE) {
            monthRecognitions.add(weightedEntity);
         } else if (recognizedType == RecognizedType.QUARTER_TYPE) {
            quarterRecognitions.add(weightedEntity);
         } else if (recognizedType == RecognizedType.YEAR_TYPE) {
            yearRecognitions.add(weightedEntity);
         } else if (recognizedType == RecognizedType.NUMBER_TYPE) {
            yearRecognitions.add(weightedEntity);
         } else if (recognizedType == RecognizedType.TIME_PREPOSITION || recognizedType == RecognizedType.OPERATOR_TYPE) {
            valuePrepositions.add(weightedEntity);
         } else if (recognizedType == RecognizedType.TIME_TYPE) {
            timeRecognition.add(weightedEntity);
         } else if (recognizedType == RecognizedType.WEEK_TYPE) {
            weekRecognitions.add(weightedEntity);
         } else if (recognizedType == RecognizedType.TIME_QULAIFIER_TYPE) {
            timeQualifierRecognitions.add(weightedEntity);
         } else if (recognizedType == RecognizedType.WEEK_DAY_TYPE) {
            weekdayRecognitions.add(weightedEntity);
         }
      }
      TFDataHolder tfDataHolder = new TFDataHolder();
      tfDataHolder.setValuePrepositionRecognitions(valuePrepositions);
      tfDataHolder.setDayRecognitions(dayRecognitions);
      tfDataHolder.setMonthRecognitions(monthRecognitions);
      tfDataHolder.setQuarterRecognitions(quarterRecognitions);
      tfDataHolder.setYearRecognitions(yearRecognitions);
      tfDataHolder.setTimeRecognitions(timeRecognition);
      tfDataHolder.setWeekRecognitions(weekRecognitions);
      tfDataHolder.setWeekdayRecognitions(weekdayRecognitions);
      tfDataHolder.setTimeQualiFierRecognitions(timeQualifierRecognitions);
      return tfDataHolder;
   }

   private LocationDataHolder getLocationDataHolder (RecognizedCloudEntity recognizedCloudEntity) {
      Map<Long, BusinessEntityDefinition> typeBedMap = recognizedCloudEntity.getTypeBedMap();
      List<IWeightedEntity> cityRecognitions = new ArrayList<IWeightedEntity>();
      List<IWeightedEntity> stateRecognitions = new ArrayList<IWeightedEntity>();
      List<IWeightedEntity> countryRecognitions = new ArrayList<IWeightedEntity>();
      List<IWeightedEntity> countyRecognitions = new ArrayList<IWeightedEntity>();

      List<IWeightedEntity> recognitionEntities = recognizedCloudEntity.getRecognitionEntities();
      for (IWeightedEntity weightedEntity : recognitionEntities) {
         BusinessEntityDefinition typeBed = typeBedMap.get(((TypeEntity) weightedEntity).getTypeBedId());
         RecognizedType recognizedType = NLPUtilities.getRecognizedType(typeBed);
         if (recognizedType == RecognizedType.CITY_TYPE) {
            cityRecognitions.add(weightedEntity);
         } else if (recognizedType == RecognizedType.STATE_TYPE) {
            stateRecognitions.add(weightedEntity);
         } else if (recognizedType == RecognizedType.COUNTRY_TYPE) {
            countryRecognitions.add(weightedEntity);
         } else if (recognizedType == RecognizedType.COUNTY_TYPE) {
            countyRecognitions.add(weightedEntity);
         }
      }
      LocationDataHolder locationDataHolder = new LocationDataHolder();
      locationDataHolder.setCityRecognitions(cityRecognitions);
      locationDataHolder.setStateRecognitions(stateRecognitions);
      locationDataHolder.setCountryRecognitions(countryRecognitions);
      locationDataHolder.setCountyRecognitions(countyRecognitions);
      return locationDataHolder;
   }

   private OPValueDataHolder getOPValueDataHolder (RecognizedCloudEntity recognizedCloudEntity) {
      Map<Long, BusinessEntityDefinition> typeBedMap = recognizedCloudEntity.getTypeBedMap();
      List<IWeightedEntity> operatorRecognitions = new ArrayList<IWeightedEntity>();
      List<IWeightedEntity> unitRecognitions = new ArrayList<IWeightedEntity>();
      List<IWeightedEntity> unitSymbolRecognitions = new ArrayList<IWeightedEntity>();
      List<IWeightedEntity> valuePrepositions = new ArrayList<IWeightedEntity>();

      List<IWeightedEntity> recognitionEntities = recognizedCloudEntity.getRecognitionEntities();
      for (IWeightedEntity weightedEntity : recognitionEntities) {
         BusinessEntityDefinition typeBed = typeBedMap.get(((TypeEntity) weightedEntity).getTypeBedId());
         RecognizedType recognizedType = NLPUtilities.getRecognizedType(typeBed);
         if (recognizedType == RecognizedType.OPERATOR_TYPE) {
            operatorRecognitions.add(weightedEntity);
         } else if (recognizedType == RecognizedType.UNIT_TYPE) {
            unitRecognitions.add(weightedEntity);
         } else if (recognizedType == RecognizedType.VALUE_PREPOSITION) {
            valuePrepositions.add(weightedEntity);
         } else if (recognizedType == RecognizedType.UNIT_SYMBOL_TYPE) {
            unitSymbolRecognitions.add(weightedEntity);
         } else if (recognizedType == RecognizedType.MONTH_TYPE) {
            unitSymbolRecognitions.add(weightedEntity);
         } else if (recognizedType == RecognizedType.DAY_TYPE) {
            unitSymbolRecognitions.add(weightedEntity);
         } else if (recognizedType == RecognizedType.WEEK_TYPE) {
            unitSymbolRecognitions.add(weightedEntity);
         }
      }
      OPValueDataHolder opValueDataHolder = new OPValueDataHolder();
      opValueDataHolder.setOperatorRecognitions(operatorRecognitions);
      opValueDataHolder.setUnitRecognitions(unitRecognitions);
      opValueDataHolder.setValuePrepositionRecognitions(valuePrepositions);
      opValueDataHolder.setUnitSymbolRecognitions(unitSymbolRecognitions);

      return opValueDataHolder;
   }

   private ValuePrepositionDataHolder getValuePrepositionDataHolder (RecognizedCloudEntity recognizedCloudEntity) {
      Map<Long, BusinessEntityDefinition> typeBedMap = recognizedCloudEntity.getTypeBedMap();
      List<IWeightedEntity> valueRecognitions = new ArrayList<IWeightedEntity>();
      List<IWeightedEntity> valuePrepositions = new ArrayList<IWeightedEntity>();

      List<IWeightedEntity> recognitionEntities = recognizedCloudEntity.getRecognitionEntities();
      for (IWeightedEntity weightedEntity : recognitionEntities) {
         BusinessEntityDefinition typeBed = typeBedMap.get(((TypeEntity) weightedEntity).getTypeBedId());
         RecognizedType recognizedType = NLPUtilities.getRecognizedType(typeBed);
         if (recognizedType == RecognizedType.VALUE_TYPE) {
            valueRecognitions.add(weightedEntity);
         } else if (recognizedType == RecognizedType.VALUE_PREPOSITION) {
            valuePrepositions.add(weightedEntity);
         }
      }
      ValuePrepositionDataHolder opValueDataHolder = new ValuePrepositionDataHolder();
      opValueDataHolder.setValueRecognitions(valueRecognitions);
      opValueDataHolder.setValuePrepositionRecognitions(valuePrepositions);
      return opValueDataHolder;
   }

   private UnitDataHolder getUnitDataHolder (RecognizedCloudEntity recognizedCloudEntity) {
      Map<Long, BusinessEntityDefinition> typeBedMap = recognizedCloudEntity.getTypeBedMap();
      List<IWeightedEntity> numberRecognitions = new ArrayList<IWeightedEntity>();
      List<IWeightedEntity> symbolRecognitions = new ArrayList<IWeightedEntity>();
      List<IWeightedEntity> scaleRecognitions = new ArrayList<IWeightedEntity>();
      List<IWeightedEntity> recognitionEntities = recognizedCloudEntity.getRecognitionEntities();
      for (IWeightedEntity weightedEntity : recognitionEntities) {
         BusinessEntityDefinition typeBed = typeBedMap.get(((TypeEntity) weightedEntity).getTypeBedId());
         RecognizedType recognizedType = NLPUtilities.getRecognizedType(typeBed);
         if (recognizedType == RecognizedType.NUMBER_TYPE) {
            numberRecognitions.add(weightedEntity);
         } else if (recognizedType == RecognizedType.UNIT_SYMBOL_TYPE) {
            symbolRecognitions.add(weightedEntity);
         } else if (recognizedType == RecognizedType.UNIT_SCALE_TYPE) {
            scaleRecognitions.add(weightedEntity);
         }
      }
      UnitDataHolder unitDataHolder = new UnitDataHolder();
      unitDataHolder.setNumberRecognitions(numberRecognitions);
      unitDataHolder.setUnitScaleRecognitions(scaleRecognitions);
      unitDataHolder.setUnitSymbolRecognitions(symbolRecognitions);
      return unitDataHolder;
   }

   private DistanceDataHolder getDistanceDataHolder (RecognizedCloudEntity recognizedCloudEntity) {
      Map<Long, BusinessEntityDefinition> typeBedMap = recognizedCloudEntity.getTypeBedMap();
      List<IWeightedEntity> operatorRecognitions = new ArrayList<IWeightedEntity>(1);
      List<IWeightedEntity> numberRecognitions = new ArrayList<IWeightedEntity>(1);
      List<IWeightedEntity> symbolRecognitions = new ArrayList<IWeightedEntity>(1);
      List<IWeightedEntity> scaleRecognitions = new ArrayList<IWeightedEntity>(1);
      List<IWeightedEntity> recognitionEntities = recognizedCloudEntity.getRecognitionEntities();
      for (IWeightedEntity weightedEntity : recognitionEntities) {
         BusinessEntityDefinition typeBed = typeBedMap.get(((TypeEntity) weightedEntity).getTypeBedId());
         RecognizedType recognizedType = NLPUtilities.getRecognizedType(typeBed);
         if (recognizedType == RecognizedType.OPERATOR_TYPE) {
            operatorRecognitions.add(weightedEntity);
         } else if (recognizedType == RecognizedType.NUMBER_TYPE) {
            numberRecognitions.add(weightedEntity);
         } else if (recognizedType == RecognizedType.UNIT_SYMBOL_TYPE) {
            symbolRecognitions.add(weightedEntity);
         } else if (recognizedType == RecognizedType.UNIT_SCALE_TYPE) {
            scaleRecognitions.add(weightedEntity);
         }
      }
      DistanceDataHolder distanceDataHolder = new DistanceDataHolder();
      distanceDataHolder.setOperatorRecognitions(operatorRecognitions);
      distanceDataHolder.setNumberRecognitions(numberRecognitions);
      distanceDataHolder.setUnitScaleRecognitions(scaleRecognitions);
      distanceDataHolder.setUnitSymbolRecognitions(symbolRecognitions);
      return distanceDataHolder;
   }

   private NormalizedDataEntity getTFNormalizeDataEntity (IWeightedEntity weightedEntity,
            BusinessEntityDefinition typeBed) {
      INormalizedData normalizedData = ((RecognitionEntity) weightedEntity).getNormalizedData();
      String value = null;
      if (normalizedData != null) {
         value = normalizedData.getValue();
      } else {
         value = ((RecognitionEntity) weightedEntity).getWord();
      }
      NormalizedDataEntity normalizedDataEntity = new NormalizedDataEntity();
      normalizedDataEntity.setTypeBedId(typeBed.getId());
      normalizedDataEntity.setValue(value);
      normalizedDataEntity.setDisplayValue(value);
      return normalizedDataEntity;
   }

   private List<NormalizedDataEntity> getLocationNormalizeDataEntities (IWeightedEntity weightedEntity) {
      List<NormalizedDataEntity> locationDataEntities = new ArrayList<NormalizedDataEntity>();
      List<InstanceInformation> instanceInformations = ((InstanceEntity) weightedEntity).getInstanceInformations();
      for (InstanceInformation instanceInformation : instanceInformations) {
         locationDataEntities.add(getLocationNormalizeDataEntity(instanceInformation, ((TypeEntity) weightedEntity)
                  .getTypeBedId()));
      }
      return locationDataEntities;
   }

   private NormalizedDataEntity getLocationNormalizeDataEntity (InstanceInformation instanceInformation, long typeBedId) {
      LocationNormalizedDataEntity normalizedDataEntity = new LocationNormalizedDataEntity();
      normalizedDataEntity.setTypeBedId(typeBedId);
      normalizedDataEntity.setValueBedId(instanceInformation.getInstanceBedId());
      normalizedDataEntity.setValueKnowledgeId(instanceInformation.getKnowledgeId());
      normalizedDataEntity.setValue(instanceInformation.getInstanceValue());
      normalizedDataEntity.setDisplayValue(instanceInformation.getInstanceDisplayName());
      normalizedDataEntity.setDisplaySymbol(instanceInformation.getDisplaySymbol());
      normalizedDataEntity.setWeightInformation(instanceInformation.getWeightInformation());
      return normalizedDataEntity;
   }

   class UnitDataHolder {

      List<IWeightedEntity> numberRecognitions;
      List<IWeightedEntity> unitScaleRecognitions;
      List<IWeightedEntity> unitSymbolRecognitions;

      /**
       * @return the numberRecognitions
       */
      public List<IWeightedEntity> getNumberRecognitions () {
         return numberRecognitions;
      }

      /**
       * @param numberRecognitions
       *           the numberRecognitions to set
       */
      public void setNumberRecognitions (List<IWeightedEntity> numberRecognitions) {
         this.numberRecognitions = numberRecognitions;
      }

      /**
       * @return the unitRecognitions
       */
      public List<IWeightedEntity> getUnitScaleRecognitions () {
         return unitScaleRecognitions;
      }

      /**
       * @param unitScaleRecognitions
       *           the unitRecognitions to set
       */
      public void setUnitScaleRecognitions (List<IWeightedEntity> unitScaleRecognitions) {
         this.unitScaleRecognitions = unitScaleRecognitions;
      }

      /**
       * @return the unitSymbolRecognitions
       */
      public List<IWeightedEntity> getUnitSymbolRecognitions () {
         return unitSymbolRecognitions;
      }

      /**
       * @param unitSymbolRecognitions
       *           the unitSymbolRecognitions to set
       */
      public void setUnitSymbolRecognitions (List<IWeightedEntity> unitSymbolRecognitions) {
         this.unitSymbolRecognitions = unitSymbolRecognitions;
      }

   }

   class DistanceDataHolder extends UnitDataHolder {

      List<IWeightedEntity> operatorRecognitions;

      /**
       * @return the operatorRecognitions
       */
      public List<IWeightedEntity> getOperatorRecognitions () {
         return operatorRecognitions;
      }

      /**
       * @param operatorRecognitions
       *           the operatorRecognitions to set
       */
      public void setOperatorRecognitions (List<IWeightedEntity> operatorRecognitions) {
         this.operatorRecognitions = operatorRecognitions;
      }

   }

   class OPValueDataHolder {

      List<IWeightedEntity> operatorRecognitions;
      List<IWeightedEntity> unitRecognitions;
      List<IWeightedEntity> valuePrepositionRecognitions;
      List<IWeightedEntity> unitSymbolRecognitions;

      /**
       * @return the operatorRecognitions
       */
      public List<IWeightedEntity> getOperatorRecognitions () {
         return operatorRecognitions;
      }

      /**
       * @param operatorRecognitions
       *           the operatorRecognitions to set
       */
      public void setOperatorRecognitions (List<IWeightedEntity> operatorRecognitions) {
         this.operatorRecognitions = operatorRecognitions;
      }

      /**
       * @return the unitRecognitions
       */
      public List<IWeightedEntity> getUnitRecognitions () {
         return unitRecognitions;
      }

      /**
       * @param unitRecognitions
       *           the unitRecognitions to set
       */
      public void setUnitRecognitions (List<IWeightedEntity> unitRecognitions) {
         this.unitRecognitions = unitRecognitions;
      }

      /**
       * @return the valuePrepositionRecognitions
       */
      public List<IWeightedEntity> getValuePrepositionRecognitions () {
         return valuePrepositionRecognitions;
      }

      /**
       * @param valuePrepositionRecognitions
       *           the valuePrepositionRecognitions to set
       */
      public void setValuePrepositionRecognitions (List<IWeightedEntity> valuePrepositionRecognitions) {
         this.valuePrepositionRecognitions = valuePrepositionRecognitions;
      }

      /**
       * @return the unitSymbolRecognitions
       */
      public List<IWeightedEntity> getUnitSymbolRecognitions () {
         return unitSymbolRecognitions;
      }

      /**
       * @param unitSymbolRecognitions
       *           the unitSymbolRecognitions to set
       */
      public void setUnitSymbolRecognitions (List<IWeightedEntity> unitSymbolRecognitions) {
         this.unitSymbolRecognitions = unitSymbolRecognitions;
      }

   }

   class ValuePrepositionDataHolder {

      List<IWeightedEntity> valueRecognitions;
      List<IWeightedEntity> valuePrepositionRecognitions;

      /**
       * @return the operatorRecognitions
       */
      public List<IWeightedEntity> getValueRecognitions () {
         return valueRecognitions;
      }

      /**
       * @param operatorRecognitions
       *           the operatorRecognitions to set
       */
      public void setValueRecognitions (List<IWeightedEntity> valueRecognitions) {
         this.valueRecognitions = valueRecognitions;
      }

      /**
       * @return the valuePrepositionRecognitions
       */
      public List<IWeightedEntity> getValuePrepositionRecognitions () {
         return valuePrepositionRecognitions;
      }

      /**
       * @param valuePrepositionRecognitions
       *           the valuePrepositionRecognitions to set
       */
      public void setValuePrepositionRecognitions (List<IWeightedEntity> valuePrepositionRecognitions) {
         this.valuePrepositionRecognitions = valuePrepositionRecognitions;
      }
   }

   class LocationDataHolder {

      List<IWeightedEntity> cityRecognitions    = new ArrayList<IWeightedEntity>();
      List<IWeightedEntity> stateRecognitions   = new ArrayList<IWeightedEntity>();
      List<IWeightedEntity> countryRecognitions = new ArrayList<IWeightedEntity>();
      List<IWeightedEntity> countyRecognitions  = new ArrayList<IWeightedEntity>();

      /**
       * @return the cityRecognitions
       */
      public List<IWeightedEntity> getCityRecognitions () {
         return cityRecognitions;
      }

      /**
       * @param cityRecognitions the cityRecognitions to set
       */
      public void setCityRecognitions (List<IWeightedEntity> cityRecognitions) {
         this.cityRecognitions = cityRecognitions;
      }

      /**
       * @return the stateRecognitions
       */
      public List<IWeightedEntity> getStateRecognitions () {
         return stateRecognitions;
      }

      /**
       * @param stateRecognitions the stateRecognitions to set
       */
      public void setStateRecognitions (List<IWeightedEntity> stateRecognitions) {
         this.stateRecognitions = stateRecognitions;
      }

      /**
       * @return the countryRecognitions
       */
      public List<IWeightedEntity> getCountryRecognitions () {
         return countryRecognitions;
      }

      /**
       * @param countryRecognitions the countryRecognitions to set
       */
      public void setCountryRecognitions (List<IWeightedEntity> countryRecognitions) {
         this.countryRecognitions = countryRecognitions;
      }

      /**
       * @return the countyRecognitions
       */
      public List<IWeightedEntity> getCountyRecognitions () {
         return countyRecognitions;
      }

      /**
       * @param countyRecognitions the countyRecognitions to set
       */
      public void setCountyRecognitions (List<IWeightedEntity> countyRecognitions) {
         this.countyRecognitions = countyRecognitions;
      }
   }

   class TFDataHolder {

      List<IWeightedEntity> timeRecognitions;
      List<IWeightedEntity> dayRecognitions;
      List<IWeightedEntity> weekRecognitions;
      List<IWeightedEntity> monthRecognitions;
      List<IWeightedEntity> yearRecognitions;
      List<IWeightedEntity> quarterRecognitions;
      List<IWeightedEntity> valuePrepositionRecognitions;
      List<IWeightedEntity> timeQualiFierRecognitions;
      List<IWeightedEntity> weekdayRecognitions;

      /**
       * @return the timeQualiFierRecognitions
       */
      public List<IWeightedEntity> getTimeQualiFierRecognitions () {
         return timeQualiFierRecognitions;
      }

      /**
       * @param timeQualiFierRecognitions
       *           the timeQualiFierRecognitions to set
       */
      public void setTimeQualiFierRecognitions (List<IWeightedEntity> timeQualiFierRecognitions) {
         this.timeQualiFierRecognitions = timeQualiFierRecognitions;
      }

      /**
       * @return the monthRecognitions
       */
      public List<IWeightedEntity> getMonthRecognitions () {
         return monthRecognitions;
      }

      /**
       * @param monthRecognitions
       *           the monthRecognitions to set
       */
      public void setMonthRecognitions (List<IWeightedEntity> monthRecognitions) {
         this.monthRecognitions = monthRecognitions;
      }

      /**
       * @return the yearRecognitions
       */
      public List<IWeightedEntity> getYearRecognitions () {
         return yearRecognitions;
      }

      /**
       * @param yearRecognitions
       *           the yearRecognitions to set
       */
      public void setYearRecognitions (List<IWeightedEntity> yearRecognitions) {
         this.yearRecognitions = yearRecognitions;
      }

      /**
       * @return the quarterRecognitions
       */
      public List<IWeightedEntity> getQuarterRecognitions () {
         return quarterRecognitions;
      }

      /**
       * @param quarterRecognitions
       *           the quarterRecognitions to set
       */
      public void setQuarterRecognitions (List<IWeightedEntity> quarterRecognitions) {
         this.quarterRecognitions = quarterRecognitions;
      }

      /**
       * @return the dayRecognitions
       */
      public List<IWeightedEntity> getDayRecognitions () {
         return dayRecognitions;
      }

      /**
       * @param dayRecognitions
       *           the dayRecognitions to set
       */
      public void setDayRecognitions (List<IWeightedEntity> dayRecognitions) {
         this.dayRecognitions = dayRecognitions;
      }

      /**
       * @return the valuePrepositionRecognitions
       */
      public List<IWeightedEntity> getValuePrepositionRecognitions () {
         return valuePrepositionRecognitions;
      }

      /**
       * @param valuePrepositionRecognitions
       *           the valuePrepositionRecognitions to set
       */
      public void setValuePrepositionRecognitions (List<IWeightedEntity> valuePrepositionRecognitions) {
         this.valuePrepositionRecognitions = valuePrepositionRecognitions;
      }

      /**
       * @return the secondRecognitions
       */
      public List<IWeightedEntity> getTimeRecognitions () {
         return timeRecognitions;
      }

      /**
       * @param secondRecognitions
       *           the secondRecognitions to set
       */

      public void setTimeRecognitions (List<IWeightedEntity> secondRecognitions) {
         this.timeRecognitions = secondRecognitions;
      }

      /**
       * @return the weekRecognitions
       */
      public List<IWeightedEntity> getWeekRecognitions () {
         return weekRecognitions;
      }

      /**
       * @param weekRecognitions
       *           the weekRecognitions to set
       */
      public void setWeekRecognitions (List<IWeightedEntity> weekRecognitions) {
         this.weekRecognitions = weekRecognitions;
      }

      /**
       * @return the weekdayRecognitions
       */
      public List<IWeightedEntity> getWeekdayRecognitions () {
         return weekdayRecognitions;
      }

      /**
       * @param weekdayRecognitions
       *           the weekdayRecognitions to set
       */
      public void setWeekdayRecognitions (List<IWeightedEntity> weekdayRecognitions) {
         this.weekdayRecognitions = weekdayRecognitions;
      }

   }

   /**
    * @return the timeConfigurationService
    */
   public ITimeConfigurationService getTimeConfigurationService () {
      return timeConfigurationService;
   }

   /**
    * @param timeConfigurationService
    *           the timeConfigurationService to set
    */
   public void setTimeConfigurationService (ITimeConfigurationService timeConfigurationService) {
      this.timeConfigurationService = timeConfigurationService;
   }

   /**
    * @return the nlpServiceHelper
    */
   public NLPServiceHelper getNlpServiceHelper () {
      return nlpServiceHelper;
   }

   /**
    * @param nlpServiceHelper
    *           the nlpServiceHelper to set
    */
   public void setNlpServiceHelper (NLPServiceHelper nlpServiceHelper) {
      this.nlpServiceHelper = nlpServiceHelper;
   }
}