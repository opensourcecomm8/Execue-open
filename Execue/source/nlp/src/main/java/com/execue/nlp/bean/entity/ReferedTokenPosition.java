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


package com.execue.nlp.bean.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

import org.apache.commons.collections.CollectionUtils;

/**
 * @author Nitesh
 */
public class ReferedTokenPosition implements Cloneable, Serializable, Comparable<ReferedTokenPosition> {

   private static final long  serialVersionUID = 1L;

   protected TreeSet<Integer> referedTokenPositions;

   /**
    * Default constructor
    */
   public ReferedTokenPosition () {

   }

   /**
    * constructor which takes the set of referedTokenPositions
    * 
    * @param referedTokenPositions
    */
   public ReferedTokenPosition (TreeSet<Integer> referedTokenPositions) {
      this.referedTokenPositions = referedTokenPositions;
   }

   public ReferedTokenPosition (List<Integer> referedTokenPositions) {
      this.referedTokenPositions = new TreeSet<Integer>();
      this.referedTokenPositions.addAll(referedTokenPositions);
   }

   /**
    * @return the referedTokenPositions
    */
   public TreeSet<Integer> getReferedTokenPositions () {
      if (referedTokenPositions == null) {
         referedTokenPositions = new TreeSet<Integer>();
      }
      return referedTokenPositions;
   }

   /**
    * @return the referedTokenPositions
    */
   public List<Integer> getReferedTokenPositionsList () {
      if (referedTokenPositions == null) {
         return new ArrayList<Integer>();
      }
      return new ArrayList<Integer>(referedTokenPositions);
   }

   /**
    * @param referedTokenPositions
    *           the referedTokenPositions to set
    */
   public void setReferedTokenPositions (TreeSet<Integer> referedTokenPositions) {
      this.referedTokenPositions = referedTokenPositions;
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#toString()
    */
   @Override
   public String toString () {
      StringBuilder sb = new StringBuilder();
      for (Integer position : referedTokenPositions) {
         sb.append(position).append(",");
      }
      sb.deleteCharAt(sb.toString().length() - 1);
      return sb.toString();
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#equals(java.lang.Object)
    */
   @Override
   public boolean equals (Object obj) {
      return (obj instanceof ReferedTokenPosition || obj instanceof String)
               && this.toString().equalsIgnoreCase(obj.toString());
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#hashCode()
    */
   @Override
   public int hashCode () {
      return this.toString().hashCode();
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Comparable#compareTo(java.lang.Object)
    */
   public int compareTo (ReferedTokenPosition rtp) {
      List<Integer> referedTokenPositions1 = new ArrayList<Integer>();
      referedTokenPositions1.addAll(this.getReferedTokenPositions());
      List<Integer> referedTokenPositions2 = new ArrayList<Integer>();
      referedTokenPositions2.addAll(rtp.getReferedTokenPositions());
      int i = 0;
      for (; i < referedTokenPositions1.size() && i < referedTokenPositions2.size(); i++) {
         int result = referedTokenPositions1.get(i).compareTo(referedTokenPositions2.get(i));
         if (result == 0) {
            continue;
         }
         return result;
      }
      if (i < referedTokenPositions1.size()) {
         return 1;
      } else if (i < referedTokenPositions2.size()) {
         return -1;
      }

      return 0;
   }

   /**
    * This method returns true if this ReferedTokenPosition is subset of the passed in ReferedTokenPosition
    * 
    * @param rtp
    * @return the boolean true/false
    */
   public boolean isSubset (ReferedTokenPosition rtp) {
      return rtp.referedTokenPositions.size() > this.referedTokenPositions.size()
               && rtp.referedTokenPositions.containsAll(this.referedTokenPositions);
   }

   public void setReferedTokenPositions (List<Integer> referedTokenPositions) {
      TreeSet<Integer> treeSet = new TreeSet<Integer>();
      treeSet.addAll(referedTokenPositions);
      this.referedTokenPositions = treeSet;
   }

   public int getDifference (ReferedTokenPosition rtp) {
      int max1 = Collections.max(this.referedTokenPositions);
      int min1 = Collections.min(this.referedTokenPositions);
      int max2 = Collections.max(rtp.getReferedTokenPositions());
      int min2 = Collections.min(rtp.getReferedTokenPositions());

      if (min1 > max2) {
         return min1 - max2;
      } else {
         return min2 - max1;
      }
   }

   public List<Integer> getInBetweenPositions (ReferedTokenPosition rtp) {
      int max1 = Collections.max(this.referedTokenPositions);
      int min1 = Collections.min(this.referedTokenPositions);
      int max2 = Collections.max(rtp.getReferedTokenPositions());
      int min2 = Collections.min(rtp.getReferedTokenPositions());
      List<Integer> betweenPositions = new ArrayList<Integer>(1);
      if (max1 < min2 - 1) {
         for (int pos = max1 + 1; pos < min2; pos++) {
            betweenPositions.add(pos);
         }
      } else if (max2 < min1 - 1) {
         for (int pos = max2 + 1; pos < min1; pos++) {
            betweenPositions.add(pos);
         }
      } else if (max1 == min2 - 1 && min1 < min2 - 1) {
         for (int pos = min1 + 1; pos < min2; pos++) {
            if (!this.referedTokenPositions.contains(pos) && !rtp.getReferedTokenPositions().contains(pos)) {
               betweenPositions.add(pos);
            }
         }
      } else if (max2 == max1) {
         if (min2 < min1) {
            for (int pos = min2 + 1; pos < min1; pos++) {
               if (!this.referedTokenPositions.contains(pos) && !rtp.getReferedTokenPositions().contains(pos)) {
                  betweenPositions.add(pos);
               }
            }
         } else {
            for (int pos = min1 + 1; pos < min2; pos++) {
               if (!this.referedTokenPositions.contains(pos) && !rtp.getReferedTokenPositions().contains(pos)) {
                  betweenPositions.add(pos);
               }
            }
         }

      } else if (min2 == min1) {
         if (max2 < max1) {
            for (int pos = max2 + 1; pos < max1; pos++) {
               if (!this.referedTokenPositions.contains(pos) && !rtp.getReferedTokenPositions().contains(pos)) {
                  betweenPositions.add(pos);
               }
            }
         } else {
            for (int pos = max1 + 1; pos < max2; pos++) {
               if (!this.referedTokenPositions.contains(pos) && !rtp.getReferedTokenPositions().contains(pos)) {
                  betweenPositions.add(pos);
               }
            }
         }
      }
      return betweenPositions;
   }

   /**
    * This method returns true if this ReferedTokenPosition overlaps with the passed in ReferedTokenPosition
    * 
    * @param rtp
    * @return the boolean true/false
    */
   public boolean isOverLap (ReferedTokenPosition rtp) {
      for (Integer posInteger : rtp.getReferedTokenPositions()) {
         if (this.getReferedTokenPositions().contains(posInteger)) {
            return true;
         }
      }
      return false;
   }

   public List<Integer> getOverlappingPositions (ReferedTokenPosition rtp) {
      List<Integer> overlappList = new ArrayList<Integer>(1);
      for (Integer posInteger : rtp.getReferedTokenPositions()) {
         if (this.getReferedTokenPositions().contains(posInteger)) {
            overlappList.add(posInteger);
         }
      }
      return overlappList;
   }

   /**
    * @return
    */
   public List<Integer> getInBetweenPos () {
      List<Integer> inBetweenPos = new ArrayList<Integer>(1);
      int min = Collections.min(referedTokenPositions);
      int max = Collections.max(referedTokenPositions);
      for (int i = min + 1; i < max; i++) {
         if (!referedTokenPositions.contains(i)) {
            inBetweenPos.add(i);
         }
      }
      return inBetweenPos;
   }

   public void addAll (Collection<Integer> referedTokenPositions) {
      if (CollectionUtils.isEmpty(referedTokenPositions)) {
         return;
      }
      if (this.referedTokenPositions == null) {
         this.referedTokenPositions = new TreeSet<Integer>();
      }
      this.referedTokenPositions.addAll(referedTokenPositions);
   }

   public void clear () {
      this.getReferedTokenPositions().clear();
   }
}
