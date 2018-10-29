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
package com.execue.core.common.bean;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Nitesh
 */
public abstract class AbstractNormalizedData implements INormalizedData {

   private Set<Integer> referredTokenPositions;
   private Set<Integer> originalReferredTokenPositions;
   private boolean      defaultAdded;

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#clone()
    */
   @Override
   public Object clone () throws CloneNotSupportedException {
      AbstractNormalizedData abstractNormalizedData = (AbstractNormalizedData) super.clone();
      TreeSet<Integer> referredTokenPositions = new TreeSet<Integer>();
      referredTokenPositions.addAll(getReferredTokenPositions());
      abstractNormalizedData.setReferredTokenPositions(referredTokenPositions);
      TreeSet<Integer> originalReferredTokenPositions = new TreeSet<Integer>();
      originalReferredTokenPositions.addAll(getOriginalReferredTokenPositions());
      abstractNormalizedData.setOriginalReferredTokenPositions(originalReferredTokenPositions);
      return abstractNormalizedData;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.bean.INormalizedData#getReferredTokenPositions()
    */
   public Set<Integer> getReferredTokenPositions () {
      if (referredTokenPositions == null) {
         referredTokenPositions = new TreeSet<Integer>();
      }
      return referredTokenPositions;
   }

   /**
    * @param referredTokenPositions
    *           the referredTokenPositions to set
    */
   public void setReferredTokenPositions (TreeSet<Integer> referredTokenPositions) {
      this.referredTokenPositions = referredTokenPositions;
   }

   /**
    * Method to add the input list of referredTokenPositions to the existing set of referredTokenPositions
    * 
    * @param referredTokenPositions
    */
   public void addReferredTokenPositions (Collection<Integer> referredTokenPositions) {
      getReferredTokenPositions().addAll(referredTokenPositions);
   }

   /**
    * Method to clear the referredTokenPositions
    */
   public void clearReferredTokenPositions () {
      getReferredTokenPositions().clear();
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.bean.INormalizedData#getReferredTokenPositions()
    */
   public Set<Integer> getOriginalReferredTokenPositions () {
      if (originalReferredTokenPositions == null) {
         originalReferredTokenPositions = new TreeSet<Integer>();
      }
      return originalReferredTokenPositions;
   }

   /**
    * @param referredTokenPositions
    *           the referredTokenPositions to set
    */
   public void setOriginalReferredTokenPositions (TreeSet<Integer> originalReferredTokenPositions) {
      this.originalReferredTokenPositions = originalReferredTokenPositions;
   }

   /**
    * Method to add the input list of referredTokenPositions to the existing set of referredTokenPositions
    * 
    * @param referredTokenPositions
    */
   public void addOriginalReferredTokenPositions (Collection<Integer> originalReferredTokenPositions) {
      getOriginalReferredTokenPositions().addAll(originalReferredTokenPositions);
   }

   /**
    * @return the defaultAdded
    */
   public boolean isDefaultAdded () {
      return defaultAdded;
   }

   /**
    * @param defaultAdded
    *           the defaultAdded to set
    */
   public void setDefaultAdded (boolean defaultAdded) {
      this.defaultAdded = defaultAdded;
   }
}
