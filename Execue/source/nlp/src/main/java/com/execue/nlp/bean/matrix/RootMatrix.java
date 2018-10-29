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


//
// Project : Execue NLP
// File Name : RootMatrix.java
// Date : 7/22/2008
// Author : Kaliki
//
//

package com.execue.nlp.bean.matrix;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.execue.core.common.bean.SearchFilter;
import com.execue.core.common.bean.WeightInformation;

/**
 * This will be the base Matrix. This will have possibilities on X-Axis and iteration on Y-Axis. This should hold all
 * the details a query has gone through during NLP barcode Processing
 * 
 * @author kaliki
 */

public class RootMatrix {

   private List<Possibility> possibilities;
   private WeightInformation baseWeightInformation;
   // TODO -NA- need to check if this variable can be set some other class instead of Root Matrix.
   private List<Set<Long>>   convertibleBeds;                        //The List Of beIds sets in each set beIds are convertible to each other
   private SearchFilter      searchFilter;

   private int               rootMatrixId;
   private int               possibilityIdCounter                = 0;
   private int               userQueryTokensCount;

   private long              processStartTime                    = 0;
   private long              semanticScopingStartTime            = 0;
   private long              findSemanticsStartTime              = 0;
   private long              enhanceSemanticsStartTime           = 0;
   private long              maxAllowedTimeForSemanticScoping    = 0;
   private long              maxAllowedTimeForFindingSemantics   = 0;
   private long              maxAllowedTimeForEnhancingSemantics = 0;

   private boolean           timeBasedCutoffEnabled;
   private boolean           fromArticle;
   private boolean           skipLocationTypeRecognition;

   private String            processorContext;                       //variable to store the context of the processors.

   public int getRootMatrixId () {
      return rootMatrixId;
   }

   public void setRootMatrixId (int rootMatrixId) {
      this.rootMatrixId = rootMatrixId;
   }

   public List<Possibility> getPossibilities () {
      return possibilities;
   }

   public void setPossibilities (List<Possibility> possibilities) {
      this.possibilities = possibilities;
   }

   public int getNextPossibilityId () {
      return ++possibilityIdCounter;
   }

   public void addPossibility (Possibility possibility) {
      if (possibilities == null) {
         possibilities = new ArrayList<Possibility>();
      }
      possibilities.add(possibility);
   }

   /**
    * Method to get the processor Context set for this root matrix.
    * 
    * @return
    */
   public String getProcessorContext () {
      return processorContext;
   }

   /**
    * Method to set the processorContext for the root Matrix.
    * 
    * @param processorContext
    */
   public void setProcessorContext (String processorContext) {
      this.processorContext = processorContext;
   }

   public long getProcessStartTime () {
      return processStartTime;
   }

   public void setProcessStartTime (long processStartTime) {
      this.processStartTime = processStartTime;
   }

   public boolean isTimeBasedCutoffEnabled () {
      return timeBasedCutoffEnabled;
   }

   public void setTimeBasedCutoffEnabled (boolean timeBasedCutoffEnabled) {
      this.timeBasedCutoffEnabled = timeBasedCutoffEnabled;
   }

   /**
    * @return the findSemanticsStartTime
    */
   public long getFindSemanticsStartTime () {
      return findSemanticsStartTime;
   }

   /**
    * @param findSemanticsStartTime
    *           the findSemanticsStartTime to set
    */
   public void setFindSemanticsStartTime (long findSemanticsStartTime) {
      this.findSemanticsStartTime = findSemanticsStartTime;
   }

   /**
    * @return the enhanceSemanticsStartTime
    */
   public long getEnhanceSemanticsStartTime () {
      return enhanceSemanticsStartTime;
   }

   /**
    * @param enhanceSemanticsStartTime
    *           the enhanceSemanticsStartTime to set
    */
   public void setEnhanceSemanticsStartTime (long enhanceSemanticsStartTime) {
      this.enhanceSemanticsStartTime = enhanceSemanticsStartTime;
   }

   /**
    * @return the maxAllowedTimeForFindingSemantics
    */
   public long getMaxAllowedTimeForFindingSemantics () {
      return maxAllowedTimeForFindingSemantics;
   }

   /**
    * @param maxAllowedTimeForFindingSemantics
    *           the maxAllowedTimeForFindingSemantics to set
    */
   public void setMaxAllowedTimeForFindingSemantics (long maxAllowedTimeForFindingSemantics) {
      this.maxAllowedTimeForFindingSemantics = maxAllowedTimeForFindingSemantics;
   }

   /**
    * @return the maxAllowedTimeForEnhancingSemantics
    */
   public long getMaxAllowedTimeForEnhancingSemantics () {
      return maxAllowedTimeForEnhancingSemantics;
   }

   /**
    * @param maxAllowedTimeForEnhancingSemantics
    *           the maxAllowedTimeForEnhancingSemantics to set
    */
   public void setMaxAllowedTimeForEnhancingSemantics (long maxAllowedTimeForEnhancingSemantics) {
      this.maxAllowedTimeForEnhancingSemantics = maxAllowedTimeForEnhancingSemantics;
   }

   /**
    * @return the userQueryTokensCount
    */
   public int getUserQueryTokensCount () {
      return userQueryTokensCount;
   }

   /**
    * @param userQueryTokensCount
    *           the userQueryTokensCount to set
    */
   public void setUserQueryTokensCount (int userQueryTokensCount) {
      this.userQueryTokensCount = userQueryTokensCount;
   }

   /**
    * @return the baseWeightInformation
    */
   public WeightInformation getBaseWeightInformation () {
      return baseWeightInformation;
   }

   /**
    * @param baseWeightInformation
    *           the baseWeightInformation to set
    */
   public void setBaseWeightInformation (WeightInformation baseWeightInformation) {
      this.baseWeightInformation = baseWeightInformation;
   }

   /**
    * @return the maxAllowedTimeForSemanticScoping
    */
   public long getMaxAllowedTimeForSemanticScoping () {
      return maxAllowedTimeForSemanticScoping;
   }

   /**
    * @param maxAllowedTimeForSemanticScoping
    *           the maxAllowedTimeForSemanticScoping to set
    */
   public void setMaxAllowedTimeForSemanticScoping (long maxAllowedTimeForSemanticScoping) {
      this.maxAllowedTimeForSemanticScoping = maxAllowedTimeForSemanticScoping;
   }

   /**
    * @return the semanticScopingStartTime
    */
   public long getSemanticScopingStartTime () {
      return semanticScopingStartTime;
   }

   /**
    * @param semanticScopingStartTime
    *           the semanticScopingStartTime to set
    */
   public void setSemanticScopingStartTime (long semanticScopingStartTime) {
      this.semanticScopingStartTime = semanticScopingStartTime;
   }

   public SearchFilter getSearchFilter () {
      return searchFilter;
   }

   public void setSearchFilter (SearchFilter searchFilter) {
      this.searchFilter = searchFilter;
   }

   /**
    * @return the fromArticle
    */
   public boolean isFromArticle () {
      return fromArticle;
   }

   /**
    * @param fromArticle
    *           the fromArticle to set
    */
   public void setFromArticle (boolean fromArticle) {
      this.fromArticle = fromArticle;
   }

   /**
    * @return the convertibleBeds
    */
   public List<Set<Long>> getConvertibleBeds () {
      if (convertibleBeds == null) {
         convertibleBeds = new ArrayList<Set<Long>>(1);
      }
      return convertibleBeds;
   }

   /**
    * @param convertibleBeds
    *           the convertibleBeds to set
    */
   public void setConvertibleBeds (List<Set<Long>> convertibleBeds) {
      this.convertibleBeds = convertibleBeds;
   }

   /**
    * @return the skipLocationTypeRecognition
    */
   public boolean isSkipLocationTypeRecognition () {
      return skipLocationTypeRecognition;
   }

   /**
    * @param skipLocationTypeRecognition the skipLocationTypeRecognition to set
    */
   public void setSkipLocationTypeRecognition (boolean skipLocationTypeRecognition) {
      this.skipLocationTypeRecognition = skipLocationTypeRecognition;
   }
}