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


package com.execue.core.common.bean.entity.unstructured;

import java.util.Date;

import com.execue.core.common.type.CheckType;

public class UserQueryFeatureInformation implements java.io.Serializable, Cloneable {

   private static final long   serialVersionUID         = 1L;
   private Long                id;
   private Long                queryId;
   private Long                contextId;
   private Long                featureId;
   private String              startValue;
   private String              endValue;
   private Double              featureWeightFactor      = 1d;
   private Date                executionDate;
   private transient CheckType multiValued              = CheckType.NO;
   private transient CheckType multiValuedGlobalPenalty = CheckType.NO;

   public static void main (String[] args) {
      System.out.println(Double.MAX_VALUE);
   }

   @Override
   public String toString () {
      StringBuilder sb = new StringBuilder(1);
      sb.append(queryId).append(" ").append(contextId).append(" ").append(featureId).append(" ").append(startValue)
               .append(" ").append(endValue);
      return sb.toString();
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#equals(java.lang.Object)
    */
   @Override
   public boolean equals (Object obj) {
      return (obj instanceof UserQueryFeatureInformation || obj instanceof String)
               && this.toString().equalsIgnoreCase(obj.toString());
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

   @Override
   public Object clone () throws CloneNotSupportedException {
      UserQueryFeatureInformation clonedCarInformation = (UserQueryFeatureInformation) super.clone();
      clonedCarInformation.setQueryId(this.getQueryId());
      clonedCarInformation.setContextId(this.getContextId());
      clonedCarInformation.setFeatureId(this.getFeatureId());
      clonedCarInformation.setStartValue(this.getStartValue());
      clonedCarInformation.setEndValue(this.getEndValue());
      clonedCarInformation.setFeatureWeightFactor(this.getFeatureWeightFactor());
      clonedCarInformation.setExecutionDate(this.getExecutionDate());
      return clonedCarInformation;
   }

   /**
    * @return the id
    */
   public Long getId () {
      return id;
   }

   /**
    * @param id
    *           the id to set
    */
   public void setId (Long id) {
      this.id = id;
   }

   /**
    * @return the queryId
    */
   public Long getQueryId () {
      return queryId;
   }

   /**
    * @param queryId
    *           the queryId to set
    */
   public void setQueryId (Long queryId) {
      this.queryId = queryId;
   }

   /**
    * @return the featureId
    */
   public Long getFeatureId () {
      return featureId;
   }

   /**
    * @param featureId
    *           the featureId to set
    */
   public void setFeatureId (Long featureId) {
      this.featureId = featureId;
   }

   /**
    * @return the startValue
    */
   public String getStartValue () {
      return startValue;
   }

   /**
    * @param startValue
    *           the startValue to set
    */
   public void setStartValue (String startValue) {
      this.startValue = startValue;
   }

   /**
    * @return the endValue
    */
   public String getEndValue () {
      return endValue;
   }

   /**
    * @param endValue
    *           the endValue to set
    */
   public void setEndValue (String endValue) {
      this.endValue = endValue;
   }

   /**
    * @return the featureWeightFactor
    */
   public Double getFeatureWeightFactor () {
      return featureWeightFactor;
   }

   /**
    * @param featureWeightFactor
    *           the featureWeightFactor to set
    */
   public void setFeatureWeightFactor (Double featureWeightFactor) {
      this.featureWeightFactor = featureWeightFactor;
   }

   /**
    * @return the executionDate
    */
   public Date getExecutionDate () {
      return executionDate;
   }

   /**
    * @param executionDate
    *           the executionDate to set
    */
   public void setExecutionDate (Date executionDate) {
      this.executionDate = executionDate;
   }

   /**
    * @return the contextId
    */
   public Long getContextId () {
      return contextId;
   }

   /**
    * @param contextId
    *           the contextId to set
    */
   public void setContextId (Long contextId) {
      this.contextId = contextId;
   }

   /**
    * @return the multiValued
    */
   public CheckType getMultiValued () {
      return multiValued;
   }

   /**
    * @param multiValued
    *           the multiValued to set
    */
   public void setMultiValued (CheckType multiValued) {
      this.multiValued = multiValued;
   }

   /**
    * @return the multiValuedGlobalPenalty
    */
   public CheckType getMultiValuedGlobalPenalty () {
      return multiValuedGlobalPenalty;
   }

   /**
    * @param multiValuedGlobalPenalty
    *           the multiValuedGlobalPenalty to set
    */
   public void setMultiValuedGlobalPenalty (CheckType multiValuedGlobalPenalty) {
      this.multiValuedGlobalPenalty = multiValuedGlobalPenalty;
   }
}