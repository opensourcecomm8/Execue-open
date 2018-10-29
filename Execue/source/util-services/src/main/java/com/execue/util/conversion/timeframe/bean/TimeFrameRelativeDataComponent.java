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

import com.execue.core.common.type.DateQualifier;
import com.execue.util.conversion.timeframe.type.RelativeQualifierType;

/**
 * This bean represents the components needed for relative time conversion. It has forward/back and how many represented
 * by relative value and what is the component represented by realtiveDateQualifier.
 * 
 * @author Vishay
 * @version 1.0
 */
public class TimeFrameRelativeDataComponent {

   private Integer               relativeValue;
   private RelativeQualifierType relativeQualifierType;
   private DateQualifier         relativeDateQualifier;

   public TimeFrameRelativeDataComponent (Integer relativeValue, RelativeQualifierType relativeQualifierType,
            DateQualifier relativeDateQualifier) {
      super();
      this.relativeValue = relativeValue;
      this.relativeQualifierType = relativeQualifierType;
      this.relativeDateQualifier = relativeDateQualifier;
   }

   public Integer getRelativeValue () {
      return relativeValue;
   }

   public void setRelativeValue (Integer relativeValue) {
      this.relativeValue = relativeValue;
   }

   public RelativeQualifierType getRelativeQualifierType () {
      return relativeQualifierType;
   }

   public void setRelativeQualifierType (RelativeQualifierType relativeQualifierType) {
      this.relativeQualifierType = relativeQualifierType;
   }

   public DateQualifier getRelativeDateQualifier () {
      return relativeDateQualifier;
   }

   public void setRelativeDateQualifier (DateQualifier relativeDateQualifier) {
      this.relativeDateQualifier = relativeDateQualifier;
   }
}
