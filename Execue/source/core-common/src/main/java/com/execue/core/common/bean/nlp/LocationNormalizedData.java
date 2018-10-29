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


package com.execue.core.common.bean.nlp;

import java.util.ArrayList;
import java.util.List;

import com.execue.core.common.bean.AbstractNormalizedData;
import com.execue.core.common.bean.entity.NormalizedDataType;
import com.execue.core.common.type.LocationType;

/**
 * @author Nitesh
 *
 */
public class LocationNormalizedData extends AbstractNormalizedData {

   private List<NormalizedDataEntity> cities    = new ArrayList<NormalizedDataEntity>();
   private List<NormalizedDataEntity> states    = new ArrayList<NormalizedDataEntity>();
   private List<NormalizedDataEntity> countries = new ArrayList<NormalizedDataEntity>();
   private List<NormalizedDataEntity> counties  = new ArrayList<NormalizedDataEntity>();
   private String                     outputName;
   private Long                       outputBedId;
   private LocationType               locationType;

   /* (non-Javadoc)
    * @see com.execue.core.common.bean.INormalizedData#getNormalizedDataType()
    */
   @Override
   public NormalizedDataType getNormalizedDataType () {
      return NormalizedDataType.LOCATION_NORMALIZED_DATA;
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#toString()
    */
   @Override
   public String toString () {
      return getValue();
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#hashCode()
    */
   @Override
   public int hashCode () {
      return toString().hashCode();
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#equals(java.lang.Object)
    */
   @Override
   public boolean equals (Object obj) {
      return (obj instanceof LocationNormalizedData || obj instanceof String) && this.toString().equals(obj.toString());
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#clone()
    */
   @Override
   public Object clone () throws CloneNotSupportedException {
      LocationNormalizedData clonedLocationNormalizedData = (LocationNormalizedData) super.clone();
      List<NormalizedDataEntity> clonedCities = new ArrayList<NormalizedDataEntity>();
      for (NormalizedDataEntity normalizedDataEntity : cities) {
         clonedCities.add((NormalizedDataEntity) normalizedDataEntity.clone());
      }
      List<NormalizedDataEntity> clonedStates = new ArrayList<NormalizedDataEntity>();
      for (NormalizedDataEntity normalizedDataEntity : states) {
         clonedStates.add((NormalizedDataEntity) normalizedDataEntity.clone());
      }
      List<NormalizedDataEntity> clonedCountries = new ArrayList<NormalizedDataEntity>();
      for (NormalizedDataEntity normalizedDataEntity : countries) {
         clonedCountries.add((NormalizedDataEntity) normalizedDataEntity.clone());
      }
      List<NormalizedDataEntity> clonedCounties = new ArrayList<NormalizedDataEntity>();
      for (NormalizedDataEntity normalizedDataEntity : counties) {
         clonedCounties.add((NormalizedDataEntity) normalizedDataEntity.clone());
      }
      clonedLocationNormalizedData.setCities(clonedCities);
      clonedLocationNormalizedData.setStates(clonedStates);
      clonedLocationNormalizedData.setCountries(clonedCountries);
      clonedLocationNormalizedData.setCounties(clonedCounties);
      clonedLocationNormalizedData.setOutputName(this.outputName);
      clonedLocationNormalizedData.setOutputBedId(this.outputBedId);
      clonedLocationNormalizedData.setLocationType(this.locationType);
      return clonedLocationNormalizedData;
   }

   /* (non-Javadoc)
    * @see com.execue.core.common.bean.INormalizedData#getValue()
    */
   @Override
   public String getValue () {
      StringBuilder sb = new StringBuilder();
      for (NormalizedDataEntity city : cities) {
         sb.append(city.toString()).append(", ");
      }
      for (NormalizedDataEntity state : states) {
         sb.append(state.toString()).append(", ");
      }
      for (NormalizedDataEntity country : countries) {
         sb.append(country.toString()).append(", ");
      }
      for (NormalizedDataEntity county : counties) {
         sb.append(county.toString()).append(", ");
      }
      return sb.toString();
   }

   /* (non-Javadoc)
    * @see com.execue.core.common.bean.INormalizedData#getDisplayValue()
    */
   @Override
   public String getDisplayValue () {
      StringBuilder sb = new StringBuilder();
      for (NormalizedDataEntity city : cities) {
         sb.append(city.getDisplayValue()).append(", ");
      }
      for (NormalizedDataEntity state : states) {
         sb.append(state.getDisplayValue()).append(", ");
      }
      for (NormalizedDataEntity country : countries) {
         sb.append(country.getDisplayValue()).append(", ");
      }
      for (NormalizedDataEntity county : counties) {
         sb.append(county.getDisplayValue()).append(", ");
      }
      return sb.toString();
   }

   /* (non-Javadoc)
    * @see com.execue.core.common.bean.INormalizedData#getType()
    */
   @Override
   public String getType () {
      return outputName;
   }

   @Override
   public Long getTypeBedId () {
      return outputBedId;
   }

   /**
    * @return the cities
    */
   public List<NormalizedDataEntity> getCities () {
      return cities;
   }

   /**
    * @param cities the cities to set
    */
   public void setCities (List<NormalizedDataEntity> cities) {
      this.cities = cities;
   }

   /**
    * @return the states
    */
   public List<NormalizedDataEntity> getStates () {
      return states;
   }

   /**
    * @param states the states to set
    */
   public void setStates (List<NormalizedDataEntity> states) {
      this.states = states;
   }

   /**
    * @return the countries
    */
   public List<NormalizedDataEntity> getCountries () {
      return countries;
   }

   /**
    * @param countries the countries to set
    */
   public void setCountries (List<NormalizedDataEntity> countries) {
      this.countries = countries;
   }

   /**
    * @return the counties
    */
   public List<NormalizedDataEntity> getCounties () {
      return counties;
   }

   /**
    * @param counties the counties to set
    */
   public void setCounties (List<NormalizedDataEntity> counties) {
      this.counties = counties;
   }

   /**
    * @return the outputName
    */
   public String getOutputName () {
      return outputName;
   }

   /**
    * @param outputName the outputName to set
    */
   public void setOutputName (String outputName) {
      this.outputName = outputName;
   }

   /**
    * @return the outputBedId
    */
   public Long getOutputBedId () {
      return outputBedId;
   }

   /**
    * @param outputBedId the outputBedId to set
    */
   public void setOutputBedId (Long outputBedId) {
      this.outputBedId = outputBedId;
   }

   
   /**
    * @return the locationType
    */
   public LocationType getLocationType () {
      return locationType;
   }

   
   /**
    * @param locationType the locationType to set
    */
   public void setLocationType (LocationType locationType) {
      this.locationType = locationType;
   }

  
}