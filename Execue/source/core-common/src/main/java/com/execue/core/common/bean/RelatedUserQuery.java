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


package com.execue.core.common.bean;

/**
 * @author Nitesh
 */
public class RelatedUserQuery {

   private Long   applicationId;
   private Long   userQueryId;
   private String userQuery;
   private Double rank;

   // @Override
   @Override
   public String toString () {
      StringBuilder sb = new StringBuilder();
      sb.append("Application Id: ").append(applicationId).append("\tUser Query Id: ").append(userQueryId).append(
               "\tUser Query: ").append(userQuery);
      return sb.toString();
   }

   public String toUserQueryString () {
      return userQuery;
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#equals(java.lang.Object)
    */
   @Override
   public boolean equals (Object obj) {
      return obj instanceof RelatedUserQuery
               && this.toUserQueryString().equals(((RelatedUserQuery) obj).toUserQueryString());

   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#hashCode()
    */
   @Override
   public int hashCode () {
      return toUserQueryString().hashCode();
   }

   /**
    * @return the applicationId
    */
   public Long getApplicationId () {
      return applicationId;
   }

   /**
    * @param applicationId
    *           the applicationId to set
    */
   public void setApplicationId (Long applicationId) {
      this.applicationId = applicationId;
   }

   /**
    * @return the userQuery
    */
   public String getUserQuery () {
      return userQuery;
   }

   /**
    * @param userQuery
    *           the userQuery to set
    */
   public void setUserQuery (String userQuery) {
      this.userQuery = userQuery;
   }

   /**
    * @return the userQueryId
    */
   public Long getUserQueryId () {
      return userQueryId;
   }

   /**
    * @param userQueryId
    *           the userQueryId to set
    */
   public void setUserQueryId (Long userQueryId) {
      this.userQueryId = userQueryId;
   }

   /**
    * @return the rank
    */
   public final Double getRank () {
      return rank;
   }

   /**
    * @param rank
    *           the rank to set
    */
   public final void setRank (Double rank) {
      this.rank = rank;
   }
}