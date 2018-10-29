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


/**
 * @author Nitesh
 *
 */
public class DateConversionInfo {
   
   private String input;
   private DateUnitType sourceUnitType;
   private DateUnitType destinationUnitType;
   
   /**
    * @return the input
    */
   public String getInput () {
      return input;
   }
   
   /**
    * @param input the input to set
    */
   public void setInput (String input) {
      this.input = input;
   }
   
   /**
    * @return the sourceUnitType
    */
   public DateUnitType getSourceUnitType () {
      return sourceUnitType;
   }
   
   /**
    * @param sourceUnitType the sourceUnitType to set
    */
   public void setSourceUnitType (DateUnitType sourceUnitType) {
      this.sourceUnitType = sourceUnitType;
   }
   
   /**
    * @return the destinationUnitType
    */
   public DateUnitType getDestinationUnitType () {
      return destinationUnitType;
   }
   
   /**
    * @param destinationUnitType the destinationUnitType to set
    */
   public void setDestinationUnitType (DateUnitType destinationUnitType) {
      this.destinationUnitType = destinationUnitType;
   }
}
