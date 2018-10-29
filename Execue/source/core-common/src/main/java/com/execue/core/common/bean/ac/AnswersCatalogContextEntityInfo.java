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
package com.execue.core.common.bean.ac;

import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.collections.CollectionUtils;

import com.execue.core.util.ExecueCoreUtil;

/**
 * @author Nitesh
 *
 */
public class AnswersCatalogContextEntityInfo {

   // NOTE: This is used for comparison and analysis, so should be sorted in order to compare hence using treeset 
   private TreeSet<String> requestedDimensions = new TreeSet<String>();
   private TreeSet<String> requestedMeasures   = new TreeSet<String>();

   @Override
   public String toString () {
      StringBuilder sb = new StringBuilder();
      sb.append(ExecueCoreUtil.joinCollection(requestedDimensions));
      sb.append(ExecueCoreUtil.joinCollection(requestedMeasures));
      return sb.toString();
   }

   @Override
   public boolean equals (Object obj) {
      return (obj instanceof AnswersCatalogContextEntityInfo || obj instanceof String)
               && this.toString().equalsIgnoreCase(obj.toString());
   }

   @Override
   public int hashCode () {
      return this.toString().hashCode();
   }

   public boolean isAtleastOneDimensionPresent (Set<String> matchingDimensions) {
      return CollectionUtils.containsAny(requestedDimensions, matchingDimensions);
   }

   /**
    * @return the requestedDimensions
    */
   public TreeSet<String> getRequestedDimensions () {
      return requestedDimensions;
   }

   /**
    * @param requestedDimensions the requestedDimensions to set
    */
   public void setRequestedDimensions (TreeSet<String> requestedDimensions) {
      this.requestedDimensions = requestedDimensions;
   }

   /**
    * @return the requestedMeasures
    */
   public TreeSet<String> getRequestedMeasures () {
      return requestedMeasures;
   }

   /**
    * @param requestedMeasures the requestedMeasures to set
    */
   public void setRequestedMeasures (TreeSet<String> requestedMeasures) {
      this.requestedMeasures = requestedMeasures;
   }
}