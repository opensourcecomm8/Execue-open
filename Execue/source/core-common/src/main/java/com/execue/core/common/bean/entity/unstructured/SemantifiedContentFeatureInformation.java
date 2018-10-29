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


package com.execue.core.common.bean.entity.unstructured;

import java.io.Serializable;
import java.util.Date;

import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.FeatureValueType;
import com.execue.core.common.type.ProcessingFlagType;

public class SemantifiedContentFeatureInformation implements Serializable, Cloneable {

   private Long                id;
   private Long                semantifiedContentId;
   private Long                contextId;
   private String              zipCode;
   private Long                featureId;
   private String              featureValue;
   private Double              featureNumberValue;
   private Double              featureWeight;
   private FeatureValueType    featureValueType;
   private Date                semantifiedContentDate;
   private CheckType           imagePresent             = CheckType.NO;
   private ProcessingFlagType  processingState          = ProcessingFlagType.NOT_PROCESSED;
   private Long                locationId;
   private String              locationDisplayName;

   private transient Double    featureNumberEndValue;
   private transient String    featureNumberStartOperator;
   private transient String    featureNumberEndOperator;
   private transient CheckType locationType             = CheckType.NO;
   private transient CheckType rangeType                = CheckType.NO;
   private transient CheckType multiValued              = CheckType.NO;
   private transient CheckType multiValuedGlobalPenalty = CheckType.NO;

   private Double              latitude;
   private Double              longitude;

   //private Point locationPoint; // This is commented as right now implementation is not available

   private Double              numericDisplayableValue1;
   private String              stringDisplayableValue1;
   private Double              numericDisplayableValue2;
   private String              stringDisplayableValue2;
   private Double              numericDisplayableValue3;
   private String              stringDisplayableValue3;
   private Double              numericDisplayableValue4;
   private String              stringDisplayableValue4;
   private Double              numericDisplayableValue5;
   private String              stringDisplayableValue5;
   private Double              numericDisplayableValue6;
   private String              stringDisplayableValue6;
   private Double              numericDisplayableValue7;
   private String              stringDisplayableValue7;
   private Double              numericDisplayableValue8;
   private String              stringDisplayableValue8;
   private Double              numericDisplayableValue9;
   private String              stringDisplayableValue9;
   private Double              numericDisplayableValue10;
   private String              stringDisplayableValue10;

   @Override
   public Object clone () throws CloneNotSupportedException {
      SemantifiedContentFeatureInformation semantifiedContentFeatureInformation = (SemantifiedContentFeatureInformation) super
               .clone();

      semantifiedContentFeatureInformation.setSemantifiedContentId(this.getSemantifiedContentId());
      semantifiedContentFeatureInformation.setContextId(this.getContextId());
      semantifiedContentFeatureInformation.setZipCode(this.getZipCode());
      semantifiedContentFeatureInformation.setFeatureId(this.getFeatureId());
      semantifiedContentFeatureInformation.setFeatureValue(this.getFeatureValue());
      semantifiedContentFeatureInformation.setFeatureNumberValue(this.getFeatureNumberValue());
      semantifiedContentFeatureInformation.setFeatureWeight(this.getFeatureWeight());
      semantifiedContentFeatureInformation.setFeatureNumberStartOperator(this.getFeatureNumberStartOperator());
      semantifiedContentFeatureInformation.setFeatureNumberEndOperator(this.getFeatureNumberEndOperator());
      semantifiedContentFeatureInformation.setFeatureValueType(this.getFeatureValueType());
      semantifiedContentFeatureInformation.setSemantifiedContentDate(this.getSemantifiedContentDate());
      semantifiedContentFeatureInformation.setImagePresent(CheckType.NO);
      semantifiedContentFeatureInformation.setRangeType(CheckType.NO);
      semantifiedContentFeatureInformation.setLocationType(CheckType.NO);
      semantifiedContentFeatureInformation.setLocationId(this.getLocationId());
      semantifiedContentFeatureInformation.setLocationDisplayName(this.getLocationDisplayName());
      semantifiedContentFeatureInformation.setLatitude(this.getLatitude());
      semantifiedContentFeatureInformation.setLongitude(this.getLongitude());
      semantifiedContentFeatureInformation.setProcessingState(this.getProcessingState());

      semantifiedContentFeatureInformation.setNumericDisplayableValue1(this.getNumericDisplayableValue1());
      semantifiedContentFeatureInformation.setStringDisplayableValue1(this.getStringDisplayableValue1());

      semantifiedContentFeatureInformation.setNumericDisplayableValue2(this.getNumericDisplayableValue2());
      semantifiedContentFeatureInformation.setStringDisplayableValue2(this.getStringDisplayableValue2());

      semantifiedContentFeatureInformation.setNumericDisplayableValue3(this.getNumericDisplayableValue3());
      semantifiedContentFeatureInformation.setStringDisplayableValue3(this.getStringDisplayableValue3());

      semantifiedContentFeatureInformation.setNumericDisplayableValue4(this.getNumericDisplayableValue4());
      semantifiedContentFeatureInformation.setStringDisplayableValue4(this.getStringDisplayableValue4());

      semantifiedContentFeatureInformation.setNumericDisplayableValue5(this.getNumericDisplayableValue5());
      semantifiedContentFeatureInformation.setStringDisplayableValue5(this.getStringDisplayableValue5());

      semantifiedContentFeatureInformation.setNumericDisplayableValue6(this.getNumericDisplayableValue6());
      semantifiedContentFeatureInformation.setStringDisplayableValue6(this.getStringDisplayableValue6());

      semantifiedContentFeatureInformation.setNumericDisplayableValue7(this.getNumericDisplayableValue7());
      semantifiedContentFeatureInformation.setStringDisplayableValue7(this.getStringDisplayableValue7());

      semantifiedContentFeatureInformation.setNumericDisplayableValue8(this.getNumericDisplayableValue8());
      semantifiedContentFeatureInformation.setStringDisplayableValue8(this.getStringDisplayableValue8());

      semantifiedContentFeatureInformation.setNumericDisplayableValue9(this.getNumericDisplayableValue9());
      semantifiedContentFeatureInformation.setStringDisplayableValue9(this.getStringDisplayableValue9());

      semantifiedContentFeatureInformation.setNumericDisplayableValue10(this.getNumericDisplayableValue10());
      semantifiedContentFeatureInformation.setStringDisplayableValue10(this.getStringDisplayableValue10());

      return semantifiedContentFeatureInformation;
   }

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public Long getSemantifiedContentId () {
      return semantifiedContentId;
   }

   public void setSemantifiedContentId (Long semantifiedContentId) {
      this.semantifiedContentId = semantifiedContentId;
   }

   public Long getContextId () {
      return contextId;
   }

   public void setContextId (Long contextId) {
      this.contextId = contextId;
   }

   public String getZipCode () {
      return zipCode;
   }

   public void setZipCode (String zipCode) {
      this.zipCode = zipCode;
   }

   public Long getFeatureId () {
      return featureId;
   }

   public void setFeatureId (Long featureId) {
      this.featureId = featureId;
   }

   public String getFeatureValue () {
      return featureValue;
   }

   public void setFeatureValue (String featureValue) {
      this.featureValue = featureValue;
   }

   public Double getFeatureNumberValue () {
      return featureNumberValue;
   }

   public void setFeatureNumberValue (Double featureNumberValue) {
      this.featureNumberValue = featureNumberValue;
   }

   public Double getFeatureWeight () {
      return featureWeight;
   }

   public void setFeatureWeight (Double featureWeight) {
      this.featureWeight = featureWeight;
   }

   public FeatureValueType getFeatureValueType () {
      return featureValueType;
   }

   public void setFeatureValueType (FeatureValueType featureValueType) {
      this.featureValueType = featureValueType;
   }

   public Date getSemantifiedContentDate () {
      return semantifiedContentDate;
   }

   public void setSemantifiedContentDate (Date semantifiedContentDate) {
      this.semantifiedContentDate = semantifiedContentDate;
   }

   public CheckType getImagePresent () {
      return imagePresent;
   }

   public void setImagePresent (CheckType imagePresent) {
      this.imagePresent = imagePresent;
   }

   public ProcessingFlagType getProcessingState () {
      return processingState;
   }

   public void setProcessingState (ProcessingFlagType processingState) {
      this.processingState = processingState;
   }

   public Double getFeatureNumberEndValue () {
      return featureNumberEndValue;
   }

   public void setFeatureNumberEndValue (Double featureNumberEndValue) {
      this.featureNumberEndValue = featureNumberEndValue;
   }

   public String getFeatureNumberStartOperator () {
      return featureNumberStartOperator;
   }

   public void setFeatureNumberStartOperator (String featureNumberStartOperator) {
      this.featureNumberStartOperator = featureNumberStartOperator;
   }

   public String getFeatureNumberEndOperator () {
      return featureNumberEndOperator;
   }

   public void setFeatureNumberEndOperator (String featureNumberEndOperator) {
      this.featureNumberEndOperator = featureNumberEndOperator;
   }

   public CheckType getRangeType () {
      return rangeType;
   }

   public void setRangeType (CheckType rangeType) {
      this.rangeType = rangeType;
   }

   public boolean isRangeType () {
      return rangeType == CheckType.YES;
   }

   /**
    * @return the locationType
    */
   public CheckType getLocationType () {
      return locationType;
   }

   /**
    * @param locationType the locationType to set
    */
   public void setLocationType (CheckType locationType) {
      this.locationType = locationType;
   }

   public boolean isLocationType () {
      return locationType == CheckType.YES;
   }

   /**
    * @return the locationId
    */
   public Long getLocationId () {
      return locationId;
   }

   /**
    * @param locationId the locationId to set
    */
   public void setLocationId (Long locationId) {
      this.locationId = locationId;
   }

   public String getLocationDisplayName () {
      return locationDisplayName;
   }

   public void setLocationDisplayName (String locationDisplayName) {
      this.locationDisplayName = locationDisplayName;
   }

   /**
    * @return the latitude
    */
   public Double getLatitude () {
      return latitude;
   }

   /**
    * @param latitude the latitude to set
    */
   public void setLatitude (Double latitude) {
      this.latitude = latitude;
   }

   /**
    * @return the longitude
    */
   public Double getLongitude () {
      return longitude;
   }

   /**
    * @param longitude the longitude to set
    */
   public void setLongitude (Double longitude) {
      this.longitude = longitude;
   }

   /**
    * @return the numericDisplayableValue1
    */
   public Double getNumericDisplayableValue1 () {
      return numericDisplayableValue1;
   }

   /**
    * @param numericDisplayableValue1 the numericDisplayableValue1 to set
    */
   public void setNumericDisplayableValue1 (Double numericDisplayableValue1) {
      this.numericDisplayableValue1 = numericDisplayableValue1;
   }

   /**
    * @return the stringDisplayableValue1
    */
   public String getStringDisplayableValue1 () {
      return stringDisplayableValue1;
   }

   /**
    * @param stringDisplayableValue1 the stringDisplayableValue1 to set
    */
   public void setStringDisplayableValue1 (String stringDisplayableValue1) {
      this.stringDisplayableValue1 = stringDisplayableValue1;
   }

   /**
    * @return the numericDisplayableValue2
    */
   public Double getNumericDisplayableValue2 () {
      return numericDisplayableValue2;
   }

   /**
    * @param numericDisplayableValue2 the numericDisplayableValue2 to set
    */
   public void setNumericDisplayableValue2 (Double numericDisplayableValue2) {
      this.numericDisplayableValue2 = numericDisplayableValue2;
   }

   /**
    * @return the stringDisplayableValue2
    */
   public String getStringDisplayableValue2 () {
      return stringDisplayableValue2;
   }

   /**
    * @param stringDisplayableValue2 the stringDisplayableValue2 to set
    */
   public void setStringDisplayableValue2 (String stringDisplayableValue2) {
      this.stringDisplayableValue2 = stringDisplayableValue2;
   }

   /**
    * @return the numericDisplayableValue3
    */
   public Double getNumericDisplayableValue3 () {
      return numericDisplayableValue3;
   }

   /**
    * @param numericDisplayableValue3 the numericDisplayableValue3 to set
    */
   public void setNumericDisplayableValue3 (Double numericDisplayableValue3) {
      this.numericDisplayableValue3 = numericDisplayableValue3;
   }

   /**
    * @return the stringDisplayableValue3
    */
   public String getStringDisplayableValue3 () {
      return stringDisplayableValue3;
   }

   /**
    * @param stringDisplayableValue3 the stringDisplayableValue3 to set
    */
   public void setStringDisplayableValue3 (String stringDisplayableValue3) {
      this.stringDisplayableValue3 = stringDisplayableValue3;
   }

   /**
    * @return the numericDisplayableValue4
    */
   public Double getNumericDisplayableValue4 () {
      return numericDisplayableValue4;
   }

   /**
    * @param numericDisplayableValue4 the numericDisplayableValue4 to set
    */
   public void setNumericDisplayableValue4 (Double numericDisplayableValue4) {
      this.numericDisplayableValue4 = numericDisplayableValue4;
   }

   /**
    * @return the stringDisplayableValue4
    */
   public String getStringDisplayableValue4 () {
      return stringDisplayableValue4;
   }

   /**
    * @param stringDisplayableValue4 the stringDisplayableValue4 to set
    */
   public void setStringDisplayableValue4 (String stringDisplayableValue4) {
      this.stringDisplayableValue4 = stringDisplayableValue4;
   }

   /**
    * @return the numericDisplayableValue5
    */
   public Double getNumericDisplayableValue5 () {
      return numericDisplayableValue5;
   }

   /**
    * @param numericDisplayableValue5 the numericDisplayableValue5 to set
    */
   public void setNumericDisplayableValue5 (Double numericDisplayableValue5) {
      this.numericDisplayableValue5 = numericDisplayableValue5;
   }

   /**
    * @return the stringDisplayableValue5
    */
   public String getStringDisplayableValue5 () {
      return stringDisplayableValue5;
   }

   /**
    * @param stringDisplayableValue5 the stringDisplayableValue5 to set
    */
   public void setStringDisplayableValue5 (String stringDisplayableValue5) {
      this.stringDisplayableValue5 = stringDisplayableValue5;
   }

   /**
    * @return the numericDisplayableValue6
    */
   public Double getNumericDisplayableValue6 () {
      return numericDisplayableValue6;
   }

   /**
    * @param numericDisplayableValue6 the numericDisplayableValue6 to set
    */
   public void setNumericDisplayableValue6 (Double numericDisplayableValue6) {
      this.numericDisplayableValue6 = numericDisplayableValue6;
   }

   /**
    * @return the stringDisplayableValue6
    */
   public String getStringDisplayableValue6 () {
      return stringDisplayableValue6;
   }

   /**
    * @param stringDisplayableValue6 the stringDisplayableValue6 to set
    */
   public void setStringDisplayableValue6 (String stringDisplayableValue6) {
      this.stringDisplayableValue6 = stringDisplayableValue6;
   }

   /**
    * @return the numericDisplayableValue7
    */
   public Double getNumericDisplayableValue7 () {
      return numericDisplayableValue7;
   }

   /**
    * @param numericDisplayableValue7 the numericDisplayableValue7 to set
    */
   public void setNumericDisplayableValue7 (Double numericDisplayableValue7) {
      this.numericDisplayableValue7 = numericDisplayableValue7;
   }

   /**
    * @return the stringDisplayableValue7
    */
   public String getStringDisplayableValue7 () {
      return stringDisplayableValue7;
   }

   /**
    * @param stringDisplayableValue7 the stringDisplayableValue7 to set
    */
   public void setStringDisplayableValue7 (String stringDisplayableValue7) {
      this.stringDisplayableValue7 = stringDisplayableValue7;
   }

   /**
    * @return the numericDisplayableValue8
    */
   public Double getNumericDisplayableValue8 () {
      return numericDisplayableValue8;
   }

   /**
    * @param numericDisplayableValue8 the numericDisplayableValue8 to set
    */
   public void setNumericDisplayableValue8 (Double numericDisplayableValue8) {
      this.numericDisplayableValue8 = numericDisplayableValue8;
   }

   /**
    * @return the stringDisplayableValue8
    */
   public String getStringDisplayableValue8 () {
      return stringDisplayableValue8;
   }

   /**
    * @param stringDisplayableValue8 the stringDisplayableValue8 to set
    */
   public void setStringDisplayableValue8 (String stringDisplayableValue8) {
      this.stringDisplayableValue8 = stringDisplayableValue8;
   }

   /**
    * @return the numericDisplayableValue9
    */
   public Double getNumericDisplayableValue9 () {
      return numericDisplayableValue9;
   }

   /**
    * @param numericDisplayableValue9 the numericDisplayableValue9 to set
    */
   public void setNumericDisplayableValue9 (Double numericDisplayableValue9) {
      this.numericDisplayableValue9 = numericDisplayableValue9;
   }

   /**
    * @return the stringDisplayableValue9
    */
   public String getStringDisplayableValue9 () {
      return stringDisplayableValue9;
   }

   /**
    * @param stringDisplayableValue9 the stringDisplayableValue9 to set
    */
   public void setStringDisplayableValue9 (String stringDisplayableValue9) {
      this.stringDisplayableValue9 = stringDisplayableValue9;
   }

   /**
    * @return the numericDisplayableValue10
    */
   public Double getNumericDisplayableValue10 () {
      return numericDisplayableValue10;
   }

   /**
    * @param numericDisplayableValue10 the numericDisplayableValue10 to set
    */
   public void setNumericDisplayableValue10 (Double numericDisplayableValue10) {
      this.numericDisplayableValue10 = numericDisplayableValue10;
   }

   /**
    * @return the stringDisplayableValue10
    */
   public String getStringDisplayableValue10 () {
      return stringDisplayableValue10;
   }

   /**
    * @param stringDisplayableValue10 the stringDisplayableValue10 to set
    */
   public void setStringDisplayableValue10 (String stringDisplayableValue10) {
      this.stringDisplayableValue10 = stringDisplayableValue10;
   }

   /**
    * @return the multiValued
    */
   public CheckType getMultiValued () {
      return multiValued;
   }

   /**
    * @param multiValued the multiValued to set
    */
   public void setMultiValued (CheckType multiValued) {
      this.multiValued = multiValued;
   }

   /**
    * @return the multiValuedGlobalPenalty
    */
   public CheckType getMultiValuedGlobalPenalty () {
      return multiValuedGlobalPenalty;
   }

   /**
    * @param multiValuedGlobalPenalty the multiValuedGlobalPenalty to set
    */
   public void setMultiValuedGlobalPenalty (CheckType multiValuedGlobalPenalty) {
      this.multiValuedGlobalPenalty = multiValuedGlobalPenalty;
   }
}