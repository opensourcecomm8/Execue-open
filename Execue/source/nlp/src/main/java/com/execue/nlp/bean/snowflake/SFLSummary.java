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


package com.execue.nlp.bean.snowflake;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author kaliki
 */
public class SFLSummary implements Cloneable {

   private long                       sFLId;
   private String                     sFLName;
   private List<Integer>              wordPositions;
   private Map<Integer, Double>       positionWeight;
   private Map<Integer, Integer>      positionOrder;
   private Map<Integer, Long>         positionTokenId;
   private Map<Integer, Double>       participatingWordQuality;
   private Map<String, List<Integer>> tokenPositionsMap;
   private Map<Integer, Integer>      requiredTokenByPosition; //Map to have required value for the Token based on its position.
   private Map<Integer, Integer>      primaryTokenByPosition;  //Map to have primary/secondary value for the Token based on its position.
   private Long                       contextId;
   private Integer                    requiredCount;           //To represent how many tokens are required for this SFL.
   private double                     sumWeight = 0;
   private boolean                    fromAlteredToken;

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#clone()
    */
   @Override
   public Object clone () throws CloneNotSupportedException {
      SFLSummary sflSummary = (SFLSummary) super.clone();
      sflSummary.setSFLId(this.sFLId);
      sflSummary.setSFLName(this.sFLName);
      sflSummary.setSumWeight(this.sumWeight);
      sflSummary.setContextId(this.contextId);

      Map<Integer, Double> positionWeight = new HashMap<Integer, Double>(1);
      if (this.positionWeight != null) {
         positionWeight.putAll(this.positionWeight);
      }
      sflSummary.setPositionWeight(positionWeight);
      Map<Integer, Integer> positionOrder = new HashMap<Integer, Integer>(1);
      if (this.positionOrder != null) {
         positionOrder.putAll(this.positionOrder);
      }
      sflSummary.setPositionOrder(positionOrder);
      Map<Integer, Long> positionTokenId = new HashMap<Integer, Long>(1);
      if (this.positionTokenId != null) {
         positionTokenId.putAll(this.positionTokenId);
      }
      sflSummary.setPositionTokenId(positionTokenId);
      List<Integer> wordPosition = new ArrayList<Integer>(1);
      if (this.wordPositions != null) {
         wordPosition.addAll(this.wordPositions);
      }
      sflSummary.setWordPositions(wordPosition);
      Map<Integer, Double> particiaptingWordQuality = new HashMap<Integer, Double>(1);
      if (this.participatingWordQuality != null) {
         particiaptingWordQuality.putAll(this.participatingWordQuality);
      }
      sflSummary.setParticipatingWordQuality(particiaptingWordQuality);
      Map<String, List<Integer>> tokenPositionsMap = new HashMap<String, List<Integer>>(1);
      if (this.tokenPositionsMap != null) {
         tokenPositionsMap.putAll(this.tokenPositionsMap);
      }
      sflSummary.setTokenPositionsMap(tokenPositionsMap);
      Map<Integer, Integer> requiredTokenByPosition = new HashMap<Integer, Integer>(1);
      if (this.requiredTokenByPosition != null) {
         requiredTokenByPosition.putAll(this.requiredTokenByPosition);
      }
      sflSummary.setRequiredTokenByPosition(requiredTokenByPosition);
      Map<Integer, Integer> primaryTokenByPosition = new HashMap<Integer, Integer>(1);
      if (this.primaryTokenByPosition != null) {
         primaryTokenByPosition.putAll(this.primaryTokenByPosition);
      }
      sflSummary.setPrimaryTokenByPosition(primaryTokenByPosition);
      return sflSummary;
   }

   /* (non-Javadoc)
    * @see java.lang.Object#toString()
    */
   @Override
   public String toString () {
      StringBuffer sb = new StringBuffer();
      sb.append("SFL ID = ").append(sFLId);
      sb.append(" ; ");
      if (positionWeight == null) {
         sb.append("Sum Weight = " + sumWeight);
      } else {
         sb.append("Position Weight = " + positionWeight);
      }
      sb.append(" ; ");
      sb.append("Positions = ").append(wordPositions.toString());
      return sb.toString();
   }

   /* (non-Javadoc)
    * @see java.lang.Object#equals(java.lang.Object)
    */
   @Override
   public boolean equals (Object obj) {
      return (obj instanceof SFLSummary || obj instanceof String) && this.toString().equalsIgnoreCase(obj.toString());
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

   /**
    * @return the fromAlteredToken
    */
   public boolean isFromAlteredToken () {
      return fromAlteredToken;
   }

   /**
    * @param fromAlteredToken
    *           the fromAlteredToken to set
    */
   public void setFromAlteredToken (boolean fromAlteredToken) {
      this.fromAlteredToken = fromAlteredToken;
   }

   /**
    * @return the tokenPositionsMap
    */
   public Map<String, List<Integer>> getTokenPositionsMap () {
      if (tokenPositionsMap == null) {
         tokenPositionsMap = new HashMap<String, List<Integer>>(1);
      }
      return tokenPositionsMap;
   }

   /**
    * @param tokenPositionsMap
    *           the tokenPositionsMap to set
    */
   public void setTokenPositionsMap (Map<String, List<Integer>> tokenPositionsMap) {
      this.tokenPositionsMap = tokenPositionsMap;
   }

   public void putTokenPosition (String token, Integer position) {
      if (tokenPositionsMap == null) {
         tokenPositionsMap = new HashMap<String, List<Integer>>(1);
      }
      List<Integer> positions = tokenPositionsMap.get(token);
      if (positions == null) {
         positions = new ArrayList<Integer>(1);
         tokenPositionsMap.put(token, positions);
      }
      positions.add(position);
   }

   public long getSFLId () {
      return sFLId;
   }

   public void setSFLId (long id) {
      sFLId = id;
   }

   public double getSumWeight () {
      return sumWeight;
   }

   public void setSumWeight (double sumWeight) {
      this.sumWeight = sumWeight;
   }

   public void addWeight (double weight) {
      this.sumWeight = this.sumWeight + weight;
   }

   public List<Integer> getWordPositions () {
      return wordPositions;
   }

   public void setWordPositions (List<Integer> wordPositions) {
      this.wordPositions = wordPositions;
   }

   public void addWordPosition (Integer position) {
      if (wordPositions == null) {
         wordPositions = new ArrayList<Integer>();
      }
      if (!wordPositions.contains(position)) {
         wordPositions.add(position);
      }
   }

   public Map<Integer, Double> getPositionWeight () {
      return positionWeight;
   }

   public Map<Integer, Integer> getPositionOrder () {
      return positionOrder;
   }

   public void setPositionWeight (Map<Integer, Double> positionWeight) {
      this.positionWeight = positionWeight;
   }

   public void putPositionWeight (Integer position, double weight) {
      if (positionWeight == null) {
         positionWeight = new HashMap<Integer, Double>();
      }
      positionWeight.put(position, weight);
   }

   public void setPositionOrder (Map<Integer, Integer> positionOrder) {
      this.positionOrder = positionOrder;
   }

   public void putPositionOrder (Integer position, Integer order) {
      if (positionOrder == null) {
         positionOrder = new HashMap<Integer, Integer>();
      }
      positionOrder.put(position, order);
   }

   public Integer getOrderByPosition (Integer position) {
      if (positionOrder == null) {
         return null;
      }
      return positionOrder.get(position);
   }

   public Double getWeightByPosition (Integer position) {
      if (positionWeight == null) {
         return null;
      }
      return positionWeight.get(position);
   }

   public Long getTokenIdByPosition (Integer position) {
      if (positionTokenId == null) {
         return null;
      }
      return positionTokenId.get(position);
   }

   public String getSFLName () {
      return sFLName;
   }

   public void setSFLName (String name) {
      sFLName = name;
   }

   /**
    * @return the participatingWordQuality
    */
   public Map<Integer, Double> getParticipatingWordQuality () {
      if (participatingWordQuality == null) {
         participatingWordQuality = new HashMap<Integer, Double>(1);
      }
      return participatingWordQuality;
   }

   /**
    * @param participatingWordQuality
    *           the participatingWordQuality to set
    */
   public void setParticipatingWordQuality (Map<Integer, Double> participatingWordQuality) {
      this.participatingWordQuality = participatingWordQuality;
   }

   public void putParticiaptingWordQuality (Integer position, Double quality) {
      if (participatingWordQuality == null) {
         participatingWordQuality = new HashMap<Integer, Double>(1);
      }
      participatingWordQuality.put(position, quality);

   }

   /**
    * Method to add a tokenId for this SFL summary in given position.
    * 
    * @param position
    * @param tokenId
    */
   public void putPositionTokenId (Integer position, Long tokenId) {
      if (positionTokenId == null) {
         positionTokenId = new HashMap<Integer, Long>();
      }
      positionTokenId.put(position, tokenId);
   }

   /**
    * @return the positionTokenId
    */
   public Map<Integer, Long> getPositionTokenId () {
      return positionTokenId;
   }

   /**
    * @param positionTokenId
    *           the positionTokenId to set
    */
   public void setPositionTokenId (Map<Integer, Long> positionTokenId) {
      this.positionTokenId = positionTokenId;
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

   public void putRequiredTokenPosition (Integer position, int required) {
      getRequiredTokenByPosition().put(position, required);
   }

   public void putPrimaryTokenPosition (Integer position, int primary) {
      getPrimaryTokenByPosition().put(position, primary);
   }

   /**
    * @return the requiredTokenByPosition
    */
   public Map<Integer, Integer> getRequiredTokenByPosition () {
      if (requiredTokenByPosition == null) {
         requiredTokenByPosition = new HashMap<Integer, Integer>();
      }
      return requiredTokenByPosition;
   }

   /**
    * @param requiredTokenByPosition
    *           the requiredTokenByPosition to set
    */
   public void setRequiredTokenByPosition (Map<Integer, Integer> requiredTokenByPosition) {
      this.requiredTokenByPosition = requiredTokenByPosition;
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

   /**
    * @return the primaryTokenByPosition
    */
   public Map<Integer, Integer> getPrimaryTokenByPosition () {
      if (primaryTokenByPosition == null) {
         primaryTokenByPosition = new HashMap<Integer, Integer>();
      }
      return primaryTokenByPosition;
   }

   /**
    * @param primaryTokenByPosition the primaryTokenByPosition to set
    */
   public void setPrimaryTokenByPosition (Map<Integer, Integer> primaryTokenByPosition) {
      this.primaryTokenByPosition = primaryTokenByPosition;
   }
}
