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


package com.execue.governor;

import java.util.Comparator;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.governor.StructuredQuery;

/**
 * This class is uses as a comparator to sort in descending order the structured Queries on basis of Priorities of the
 * Asset the Query belongs to.
 * 
 * @author Vishay
 * @version 1.0
 * @since 12/02/09
 */
public class StructuredQueryAssetPriorityComparator implements Comparator<StructuredQuery> {

   private static final Logger logger = Logger.getLogger(StructuredQueryAssetPriorityComparator.class);

   /**
    * This method will compare two structured Queries on basis of their asset priority. If any of the Asset don't have
    * priority, we are considering them as equal.
    * 
    * @param structuredQuery1
    * @param structuredQuery2
    * @return 0,-1,+1 value indicating the comparison result
    */
   public int compare (StructuredQuery structuredQuery1, StructuredQuery structuredQuery2) {
      logger.debug("Inside compare method");
      logger.debug("Got Structured Query 1 : " + structuredQuery1);
      logger.debug("Got Structured Query 2 : " + structuredQuery2);
      if (structuredQuery1.getAsset().getPriority() != null && structuredQuery2.getAsset().getPriority() != null) {
         if (structuredQuery1.getAsset().getPriority() > structuredQuery2.getAsset().getPriority()) {
            return -1;
         } else if (structuredQuery1.getAsset().getPriority() < structuredQuery2.getAsset().getPriority()) {
            return +1;
         } else {
            return 0;
         }
      } else {
         return 0;
      }
   }
}