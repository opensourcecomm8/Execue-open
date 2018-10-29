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


package com.execue.das.solr.bean;

/**
 * @author Vishay
 */
public class SolrRangeEntity {

   private String  lowerBound;
   private String  upperBound;
   private boolean inclusive = true;
   private String  rangeName;

   public String getLowerBound () {
      return lowerBound;
   }

   public void setLowerBound (String lowerBound) {
      this.lowerBound = lowerBound;
   }

   public String getUpperBound () {
      return upperBound;
   }

   public void setUpperBound (String upperBound) {
      this.upperBound = upperBound;
   }

   public boolean isInclusive () {
      return inclusive;
   }

   public void setInclusive (boolean inclusive) {
      this.inclusive = inclusive;
   }

   public String getRangeName () {
      return rangeName;
   }

   public void setRangeName (String rangeName) {
      this.rangeName = rangeName;
   }
}
