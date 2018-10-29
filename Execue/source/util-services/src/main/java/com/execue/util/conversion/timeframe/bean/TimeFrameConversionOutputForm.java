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

import com.execue.core.common.type.OperatorType;
import com.execue.core.util.ExecueCoreUtil;

/**
 * This bean represents the output of the time frame conversion. It can be a range or can be a plain value. In case of
 * range normalized data, we will get multiple ranges or multiple values. Plain time frame normalized data, lower
 * conversion one range will be there upper conversion, single value will be there relative time frame, lower or upper
 * conversion one range will be there. list normalized data, lower conversion multiple ranges will be there upper
 * conversion, multiple values will be there range normalized data, one range will be there
 * 
 * @author Vishay
 * @version 1.0
 */
public class TimeFrameConversionOutputForm {

   private List<String>                  values;
   private List<TimeFrameRangeComponent> rangeValues;
   private OperatorType                  finalOperatorType;

   public void add (TimeFrameConversionOutputForm timeFrameConversionOutputForm) {
      if (ExecueCoreUtil.isCollectionNotEmpty(timeFrameConversionOutputForm.getValues())) {
         if (ExecueCoreUtil.isCollectionEmpty(values)) {
            values = new ArrayList<String>();
         }
         values.addAll(timeFrameConversionOutputForm.getValues());
      }
      if (ExecueCoreUtil.isCollectionNotEmpty(timeFrameConversionOutputForm.getRangeValues())) {
         if (ExecueCoreUtil.isCollectionEmpty(rangeValues)) {
            rangeValues = new ArrayList<TimeFrameRangeComponent>();
         }
         rangeValues.addAll(timeFrameConversionOutputForm.getRangeValues());
      }
   }

   public void add (TimeFrameRangeComponent rangeComponent) {
      if (ExecueCoreUtil.isCollectionEmpty(rangeValues)) {
         rangeValues = new ArrayList<TimeFrameRangeComponent>();
      }
      if (rangeComponent != null) {
         rangeValues.add(rangeComponent);
      }
   }

   public void add (String value) {
      if (ExecueCoreUtil.isCollectionEmpty(values)) {
         values = new ArrayList<String>();
      }
      if (value != null) {
         values.add(value);
      }
   }

   public void addRangeComponents (List<TimeFrameRangeComponent> rangeComponents) {
      if (ExecueCoreUtil.isCollectionEmpty(rangeValues)) {
         rangeValues = new ArrayList<TimeFrameRangeComponent>();
      }
      if (ExecueCoreUtil.isCollectionNotEmpty(rangeComponents)) {
         rangeValues.addAll(rangeComponents);
      }
   }

   public void add (List<String> values) {
      if (ExecueCoreUtil.isCollectionEmpty(this.values)) {
         this.values = new ArrayList<String>();
      }
      if (ExecueCoreUtil.isCollectionNotEmpty(values)) {
         this.values.addAll(values);
      }
   }

   public List<String> getValues () {
      return values;
   }

   public void setValues (List<String> values) {
      this.values = values;
   }

   public List<TimeFrameRangeComponent> getRangeValues () {
      return rangeValues;
   }

   public void setRangeValues (List<TimeFrameRangeComponent> rangeValues) {
      this.rangeValues = rangeValues;
   }

   public OperatorType getFinalOperatorType () {
      return finalOperatorType;
   }

   public void setFinalOperatorType (OperatorType finalOperatorType) {
      this.finalOperatorType = finalOperatorType;
   }
}
