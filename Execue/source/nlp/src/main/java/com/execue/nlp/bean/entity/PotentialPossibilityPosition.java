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
package com.execue.nlp.bean.entity;

import java.util.Set;
import java.util.TreeSet;

/**
 * @author Execue01
 */
public class PotentialPossibilityPosition {

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#toString()
    */
   @Override
   public String toString () {
      StringBuilder sb = new StringBuilder();
      sb.append("Covered Positions " + coveredPosition).append(" participatingPosition " + participatingPositions);
      return sb.toString();
   }

   private ReferedTokenPosition      coveredPosition;
   private Set<ReferedTokenPosition> participatingPositions;

   /**
    * @return the coveredPosition
    */
   public ReferedTokenPosition getCoveredPosition () {
      return coveredPosition;
   }

   /**
    * @param coveredPosition
    *           the coveredPosition to set
    */
   public void setCoveredPosition (ReferedTokenPosition coveredPosition) {
      this.coveredPosition = coveredPosition;
   }

   /**
    * @return the participatingPositions
    */
   public Set<ReferedTokenPosition> getParticipatingPositions () {
      return participatingPositions;
   }

   /**
    * @param participatingPositions
    *           the participatingPositions to set
    */
   public void addParticipatingPositions (ReferedTokenPosition participatingPosition) {
      if (participatingPositions == null) {
         participatingPositions = new TreeSet<ReferedTokenPosition>();
         coveredPosition = new ReferedTokenPosition();
      }
      participatingPositions.add(participatingPosition);
      coveredPosition.addAll(participatingPosition.getReferedTokenPositions());
   }

   /**
    * @param participatingPositions
    *           the participatingPositions to set
    */
   public void setParticipatingPositions (Set<ReferedTokenPosition> participatingPositions) {
      this.participatingPositions = participatingPositions;
   }
}
