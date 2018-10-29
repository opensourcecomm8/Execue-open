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


package com.execue.core.common.bean.entity;

import java.io.Serializable;
import java.util.Set;

import com.execue.core.common.bean.IBusinessEntity;
import com.execue.core.common.type.ConversionType;
import com.execue.core.common.type.SamplingStrategy;

/**
 * This class represents the Concept object.
 * 
 * @author Vishay
 * @version 1.0
 * @since 12/01/09
 */
public class Concept implements IBusinessEntity, Serializable {

   private static final long             serialVersionUID = 1L;
   private Long                          id;
   private String                        name;
   private String                        description;
   private String                        displayName;
   private String                        defaultUnit;
   private String                        defaultDataFormat;
   private Set<BusinessEntityDefinition> businessEntityDefinitions;
   private Set<Stat>                     stats;
   private String                        sampleValue;
   private ConversionType                defaultConversionType;
   private SamplingStrategy              dataSamplingStrategy;
   private transient Long                bedId;
   private transient boolean             hasInstance      = false;

   @Override
   public int hashCode () {
      return 100;
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#equals(java.lang.Object)
    */
   @Override
   public boolean equals (Object obj) {
      if (obj instanceof Concept) {
         return this.id.equals(((Concept) obj).getId());
      }
      return false;
   }

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public String getName () {
      return name;
   }

   public void setName (String name) {
      this.name = name;
   }

   public String getDescription () {
      return description;
   }

   public void setDescription (String description) {
      this.description = description;
   }

   public String getDisplayName () {
      return displayName;
   }

   public void setDisplayName (String displayName) {
      this.displayName = displayName;
   }

   public Set<Stat> getStats () {
      return stats;
   }

   public void setStats (Set<Stat> stats) {
      this.stats = stats;
   }

   public String getDefaultDataFormat () {
      return defaultDataFormat;
   }

   public void setDefaultDataFormat (String defaultDataFormat) {
      this.defaultDataFormat = defaultDataFormat;
   }

   public String getDefaultUnit () {
      return defaultUnit;
   }

   public void setDefaultUnit (String defaultUnit) {
      this.defaultUnit = defaultUnit;
   }

   public String getSampleValue () {
      return sampleValue;
   }

   public void setSampleValue (String sampleValue) {
      this.sampleValue = sampleValue;
   }

   public Set<BusinessEntityDefinition> getBusinessEntityDefinitions () {
      return businessEntityDefinitions;
   }

   public void setBusinessEntityDefinitions (Set<BusinessEntityDefinition> businessEntityDefinitions) {
      this.businessEntityDefinitions = businessEntityDefinitions;
   }

   public Long getBedId () {
      return bedId;
   }

   public void setBedId (Long bedId) {
      this.bedId = bedId;
   }

   public ConversionType getDefaultConversionType () {
      return defaultConversionType;
   }

   public void setDefaultConversionType (ConversionType defaultConversionType) {
      this.defaultConversionType = defaultConversionType;
   }

   public SamplingStrategy getDataSamplingStrategy () {
      return dataSamplingStrategy;
   }

   public void setDataSamplingStrategy (SamplingStrategy dataSamplingStrategy) {
      this.dataSamplingStrategy = dataSamplingStrategy;
   }

   /**
    * @return the hasInstance
    */
   public boolean isHasInstance () {
      return hasInstance;
   }

   /**
    * @param hasInstance the hasInstance to set
    */
   public void setHasInstance (boolean hasInstance) {
      this.hasInstance = hasInstance;
   }
}