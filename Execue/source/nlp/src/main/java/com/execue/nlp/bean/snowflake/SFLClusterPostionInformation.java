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
package com.execue.nlp.bean.snowflake;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.collections.CollectionUtils;

/**
 * Class To contain the information about position and the Intersection position of the Summaries participating in a SFL
 * Cluster. If a SFL Summary with position (3,4) and Summary with position (4,6) makes a SFL cluster. {(3,4)(4,6)} will
 * make summaryPositions in cluster and {4} will be intersecting position.
 * 
 * @author Nihar
 */
public class SFLClusterPostionInformation {

   Set<Integer>        clusterPositionSet;
   List<List<Integer>> summaryPosList;
   List<Integer>       intersectingPosition;

   /**
    * @return the summaryPosList
    */
   public List<List<Integer>> getSummaryPosList () {
      return summaryPosList;
   }

   /**
    * @param summaryPosList
    *           the summaryPosList to set
    */
   public void setSummaryPosList (List<List<Integer>> summaryPosList) {
      this.summaryPosList = summaryPosList;
   }

   /**
    * @return the intersectingPosition
    */
   public List<Integer> getIntersectingPosition () {
      return intersectingPosition;
   }

   /**
    * @param intersectingPosition
    *           the intersectingPosition to set
    */
   public void setIntersectingPosition (List<Integer> intersectingPosition) {
      this.intersectingPosition = intersectingPosition;
   }

   @Override
   public String toString () {
      StringBuffer sb = new StringBuffer();
      sb.append("InterSecting Position List " + intersectingPosition);
      sb.append(" Participating Positions " + summaryPosList);
      return sb.toString();
   }

   /**
    * @return the clusterPositionSet
    */
   public Set<Integer> getClusterPositionSet () {
      if (CollectionUtils.isEmpty(clusterPositionSet)) {
         clusterPositionSet = new TreeSet<Integer>();
      }

      return clusterPositionSet;
   }

   /**
    * @param clusterPositionSet
    *           the clusterPositionSet to set
    */
   public void setClusterPositionSet (Set<Integer> clusterPositionSet) {
      this.clusterPositionSet = clusterPositionSet;
   }
}
