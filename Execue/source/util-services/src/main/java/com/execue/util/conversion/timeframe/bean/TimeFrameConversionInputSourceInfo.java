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


package com.execue.util.conversion.timeframe.bean;

import java.util.ArrayList;
import java.util.List;

import com.execue.core.common.bean.entity.NormalizedDataType;
import com.execue.core.common.bean.nlp.TimeFrameNormalizedData;
import com.execue.core.common.type.OperatorType;

/**
 * This bean contains the information required to convert the time frame normalized data in business condition
 * 
 * @author Vishay
 * @version 1.0
 * @since 11/04/2011
 */
public class TimeFrameConversionInputSourceInfo implements Cloneable {

   private List<TimeFrameNormalizedData> timeFrameNormalizedDataList;
   private NormalizedDataType            normalizedDataType;
   private OperatorType                  sourceOperatorType;

   public List<TimeFrameNormalizedData> getTimeFrameNormalizedDataList () {
      return timeFrameNormalizedDataList;
   }

   public void setTimeFrameNormalizedDataList (List<TimeFrameNormalizedData> timeFrameNormalizedDataList) {
      this.timeFrameNormalizedDataList = timeFrameNormalizedDataList;
   }

   public NormalizedDataType getNormalizedDataType () {
      return normalizedDataType;
   }

   public void setNormalizedDataType (NormalizedDataType normalizedDataType) {
      this.normalizedDataType = normalizedDataType;
   }

   public OperatorType getSourceOperatorType () {
      return sourceOperatorType;
   }

   public void setSourceOperatorType (OperatorType sourceOperatorType) {
      this.sourceOperatorType = sourceOperatorType;
   }

   @Override
   public Object clone () throws CloneNotSupportedException {
      TimeFrameConversionInputSourceInfo clonedTimeFrameConversionInputInfo = (TimeFrameConversionInputSourceInfo) super
               .clone();
      clonedTimeFrameConversionInputInfo.setNormalizedDataType(normalizedDataType);
      clonedTimeFrameConversionInputInfo.setSourceOperatorType(sourceOperatorType);
      List<TimeFrameNormalizedData> clonedTimeFrameNormalizedDataList = new ArrayList<TimeFrameNormalizedData>();
      for (TimeFrameNormalizedData timeFrameNormalizedData : timeFrameNormalizedDataList) {
         clonedTimeFrameNormalizedDataList.add((TimeFrameNormalizedData) timeFrameNormalizedData.clone());
      }
      clonedTimeFrameConversionInputInfo.setTimeFrameNormalizedDataList(clonedTimeFrameNormalizedDataList);
      return clonedTimeFrameConversionInputInfo;
   }

   public TimeFrameConversionInputSourceInfo (List<TimeFrameNormalizedData> timeFrameNormalizedDataList,
            NormalizedDataType normalizedDataType, OperatorType sourceOperatorType) {
      super();
      this.timeFrameNormalizedDataList = timeFrameNormalizedDataList;
      this.normalizedDataType = normalizedDataType;
      this.sourceOperatorType = sourceOperatorType;
   }

}
