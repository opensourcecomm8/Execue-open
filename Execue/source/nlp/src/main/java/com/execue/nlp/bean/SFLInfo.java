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


package com.execue.nlp.bean;

import java.util.List;

/**
 * @author John Mallavalli
 */
public class SFLInfo {

   private Long          sflId;
   private String        sflTerm;
   private String        tokensAsString;
   private String        tokenIdsAsString;
   private String        weightsAsString;
   private String        orderAsString;
   private List<String>  tokens;
   private List<Double>  weights;
   private List<Integer> orders;
   private List<Long>    tokenIds;
   private double        totalWeight;
   private Long          contextId;
   private List<Integer> requiredList;
   private Integer       requiredCount;

   /**
    * @return the requiredList
    */
   public List<Integer> getRequiredList () {
      return requiredList;
   }

   /**
    * @param requiredList
    *           the requiredList to set
    */
   public void setRequiredList (List<Integer> requiredList) {
      this.requiredList = requiredList;
   }

   /**
    * @return the requiredCount
    */
   public Integer getRequiredCount () {
      return requiredCount;
   }

   /**
    * @param requiredCount
    *           the requiredCount to set
    */
   public void setRequiredCount (Integer requiredCount) {
      this.requiredCount = requiredCount;
   }

   public Long getSflId () {
      return sflId;
   }

   public void setSflId (Long sflId) {
      this.sflId = sflId;
   }

   public String getSflTerm () {
      return sflTerm;
   }

   public void setSflTerm (String sflTerm) {
      this.sflTerm = sflTerm;
   }

   public String getTokensAsString () {
      return tokensAsString;
   }

   public void setTokensAsString (String tokensAsString) {
      this.tokensAsString = tokensAsString;
   }

   public String getWeightsAsString () {
      return weightsAsString;
   }

   public void setWeightsAsString (String weightsAsString) {
      this.weightsAsString = weightsAsString;
   }

   public String getOrderAsString () {
      return orderAsString;
   }

   public void setOrderAsString (String orderAsString) {
      this.orderAsString = orderAsString;
   }

   public double getTotalWeight () {
      return totalWeight;
   }

   public void setTotalWeight (double totalWeight) {
      this.totalWeight = totalWeight;
   }

   /**
    * @return the tokens
    */
   public List<String> getTokens () {
      return tokens;
   }

   /**
    * @param tokens
    *           the tokens to set
    */
   public void setTokens (List<String> tokens) {
      this.tokens = tokens;
   }

   /**
    * @return the weights
    */
   public List<Double> getWeights () {
      return weights;
   }

   /**
    * @param weights
    *           the weights to set
    */
   public void setWeights (List<Double> weights) {
      this.weights = weights;
   }

   /**
    * @return the orders
    */
   public List<Integer> getOrders () {
      return orders;
   }

   /**
    * @param orders
    *           the orders to set
    */
   public void setOrders (List<Integer> orders) {
      this.orders = orders;
   }

   /**
    * @return the tokenIdsAsString
    */
   public String getTokenIdsAsString () {
      return tokenIdsAsString;
   }

   /**
    * @param tokenIdsAsString
    *           the tokenIdsAsString to set
    */
   public void setTokenIdsAsString (String tokenIdsAsString) {
      this.tokenIdsAsString = tokenIdsAsString;
   }

   /**
    * @return the tokenIds
    */
   public List<Long> getTokenIds () {
      return tokenIds;
   }

   /**
    * @param tokenIds
    *           the tokenIds to set
    */
   public void setTokenIds (List<Long> tokenIds) {
      this.tokenIds = tokenIds;
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
}
