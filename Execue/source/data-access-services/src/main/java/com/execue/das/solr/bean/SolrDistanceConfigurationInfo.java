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
public class SolrDistanceConfigurationInfo {

   private String singleLocationDistanceFormula;
   private String multipleLocationDistanceFormula;
   private String distanceReplaceToken;
   private String longitudeReplaceToken;
   private String latitudeReplaceToken;
   private String mutipleLontitudeLatitudeReplaceToken;
   private String latitudeFieldReplaceToken;
   private String longitudeFieldReplaceToken;

   public String getSingleLocationDistanceFormula () {
      return singleLocationDistanceFormula;
   }

   public void setSingleLocationDistanceFormula (String singleLocationDistanceFormula) {
      this.singleLocationDistanceFormula = singleLocationDistanceFormula;
   }

   public String getMultipleLocationDistanceFormula () {
      return multipleLocationDistanceFormula;
   }

   public void setMultipleLocationDistanceFormula (String multipleLocationDistanceFormula) {
      this.multipleLocationDistanceFormula = multipleLocationDistanceFormula;
   }

   public String getDistanceReplaceToken () {
      return distanceReplaceToken;
   }

   public void setDistanceReplaceToken (String distanceReplaceToken) {
      this.distanceReplaceToken = distanceReplaceToken;
   }

   public String getLongitudeReplaceToken () {
      return longitudeReplaceToken;
   }

   public void setLongitudeReplaceToken (String longitudeReplaceToken) {
      this.longitudeReplaceToken = longitudeReplaceToken;
   }

   public String getLatitudeReplaceToken () {
      return latitudeReplaceToken;
   }

   public void setLatitudeReplaceToken (String latitudeReplaceToken) {
      this.latitudeReplaceToken = latitudeReplaceToken;
   }

   public String getMutipleLontitudeLatitudeReplaceToken () {
      return mutipleLontitudeLatitudeReplaceToken;
   }

   public void setMutipleLontitudeLatitudeReplaceToken (String mutipleLontitudeLatitudeReplaceToken) {
      this.mutipleLontitudeLatitudeReplaceToken = mutipleLontitudeLatitudeReplaceToken;
   }

   public String getLatitudeFieldReplaceToken () {
      return latitudeFieldReplaceToken;
   }

   public void setLatitudeFieldReplaceToken (String latitudeFieldReplaceToken) {
      this.latitudeFieldReplaceToken = latitudeFieldReplaceToken;
   }

   public String getLongitudeFieldReplaceToken () {
      return longitudeFieldReplaceToken;
   }

   public void setLongitudeFieldReplaceToken (String longitudeFieldReplaceToken) {
      this.longitudeFieldReplaceToken = longitudeFieldReplaceToken;
   }

}
