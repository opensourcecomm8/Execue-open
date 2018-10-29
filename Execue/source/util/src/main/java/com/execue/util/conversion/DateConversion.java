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


/**
 * 
 */
package com.execue.util.conversion;

import java.util.Date;
import java.util.List;


/**
 * @author Nitesh
 *
 */
public class DateConversion {
   
   private List<Date> convertedDates;
   private DateUnitType dateUnitType;
   
   /**
    * @return the convertedDates
    */
   public List<Date> getConvertedDates () {
      return convertedDates;
   }
   
   /**
    * @param convertedDates the convertedDates to set
    */
   public void setConvertedDates (List<Date> convertedDates) {
      this.convertedDates = convertedDates;
   }
   
   /**
    * @return the dateUnitType
    */
   public DateUnitType getDateUnitType () {
      return dateUnitType;
   }
   
   /**
    * @param dateUnitType the unitType to set
    */
   public void setDateUnitType (DateUnitType dateUnitType) {
      this.dateUnitType = dateUnitType;
   }     
}
