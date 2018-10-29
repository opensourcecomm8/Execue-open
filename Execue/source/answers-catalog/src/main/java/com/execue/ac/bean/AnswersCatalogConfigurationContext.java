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


package com.execue.ac.bean;

import com.execue.core.common.type.DataType;

/**
 * This bean contains the information needed for answers catalog cubes and marts prepration.
 * 
 * @author Vishay
 * @version 1.0
 * @since 17/02/2011
 */

public class AnswersCatalogConfigurationContext {

   private int      maxDBObjectLength;
   private String   statisticsConceptName;
   private DataType measureColumnDataType;
   private DataType frequencyColumnDataType;
   private int      measurePrecisionValue;
   private int      measureScaleValue;

   public String getStatisticsConceptName () {
      return statisticsConceptName;
   }

   public void setStatisticsConceptName (String statisticsConceptName) {
      this.statisticsConceptName = statisticsConceptName;
   }

   public int getMeasurePrecisionValue () {
      return measurePrecisionValue;
   }

   public void setMeasurePrecisionValue (int measurePrecisionValue) {
      this.measurePrecisionValue = measurePrecisionValue;
   }

   public int getMeasureScaleValue () {
      return measureScaleValue;
   }

   public void setMeasureScaleValue (int measureScaleValue) {
      this.measureScaleValue = measureScaleValue;
   }

   public int getMaxDBObjectLength () {
      return maxDBObjectLength;
   }

   public void setMaxDBObjectLength (int maxDBObjectLength) {
      this.maxDBObjectLength = maxDBObjectLength;
   }

   public DataType getMeasureColumnDataType () {
      return measureColumnDataType;
   }

   public void setMeasureColumnDataType (DataType measureColumnDataType) {
      this.measureColumnDataType = measureColumnDataType;
   }

   public DataType getFrequencyColumnDataType () {
      return frequencyColumnDataType;
   }

   public void setFrequencyColumnDataType (DataType frequencyColumnDataType) {
      this.frequencyColumnDataType = frequencyColumnDataType;
   }
}
