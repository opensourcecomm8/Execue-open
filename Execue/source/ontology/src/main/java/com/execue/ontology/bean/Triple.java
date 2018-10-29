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


package com.execue.ontology.bean;

import com.execue.core.common.bean.algorithm.BaseBean;

public class Triple extends BaseBean {

   public String domain;
   public String range;
   public String property;
   public Long   domainBedId;
   public Long   rangeBedId;
   public Long   propertyBedId;
   public int    cardinality = 1;

   public String getDomain () {
      return domain;
   }

   public void setDomain (String domain) {
      this.domain = domain;
   }

   public String getRange () {
      return range;
   }

   public void setRange (String range) {
      this.range = range;
   }

   public String getProperty () {
      return property;
   }

   public void setProperty (String property) {
      this.property = property;
   }

   public int getCardinality () {
      return cardinality;
   }

   public void setCardinality (int cardinality) {
      this.cardinality = cardinality;
   }

   @Override
   public Object clone () throws CloneNotSupportedException {
      Triple tempTriple = new Triple();
      tempTriple.setDomain(this.domain);
      tempTriple.setRange(this.range);
      tempTriple.setProperty(this.property);
      tempTriple.setCardinality(this.cardinality);

      return tempTriple;
   }

   @Override
   public boolean equals (Object obj) {
      if (obj instanceof Triple) {
         Triple tempTriple = (Triple) obj;
         return tempTriple.getDomain().equalsIgnoreCase(this.domain)
                  && tempTriple.getRange().equalsIgnoreCase(this.range)
                  && tempTriple.getProperty().equalsIgnoreCase(this.property);
      }
      return false;
   }

   public Long getDomainBedId () {
      return domainBedId;
   }

   public void setDomainBedId (Long domainBedId) {
      this.domainBedId = domainBedId;
   }

   public Long getRangeBedId () {
      return rangeBedId;
   }

   public void setRangeBedId (Long rangeBedId) {
      this.rangeBedId = rangeBedId;
   }

   public Long getPropertyBedId () {
      return propertyBedId;
   }

   public void setPropertyBedId (Long propertyBedId) {
      this.propertyBedId = propertyBedId;
   }
}
