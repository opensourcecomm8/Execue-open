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

/**
 * This bean represents the range component for the conversion.
 * 
 * @author Vishay
 * @version 1.0
 */
public class TimeFrameRangeComponent implements Cloneable {

   private String lowerRange;
   private String upperRange;

   public String getLowerRange () {
      return lowerRange;
   }

   @Override
   public boolean equals (Object obj) {
      TimeFrameRangeComponent timeFrameRangeComponent = (TimeFrameRangeComponent) obj;
      return (timeFrameRangeComponent.getLowerRange().equals(lowerRange) && timeFrameRangeComponent.getUpperRange()
               .equals(upperRange));
   }

   @Override
   public int hashCode () {
      return (lowerRange + upperRange).hashCode();
   }

   public void setLowerRange (String lowerRange) {
      this.lowerRange = lowerRange;
   }

   public String getUpperRange () {
      return upperRange;
   }

   public void setUpperRange (String upperRange) {
      this.upperRange = upperRange;
   }

   public TimeFrameRangeComponent (String lowerRange, String upperRange) {
      super();
      this.lowerRange = lowerRange;
      this.upperRange = upperRange;
   }

   @Override
   public Object clone () throws CloneNotSupportedException {
      TimeFrameRangeComponent clonedTimeFrameRangeComponent = (TimeFrameRangeComponent) super.clone();
      clonedTimeFrameRangeComponent.setLowerRange(lowerRange);
      clonedTimeFrameRangeComponent.setUpperRange(upperRange);
      return clonedTimeFrameRangeComponent;
   }

}
