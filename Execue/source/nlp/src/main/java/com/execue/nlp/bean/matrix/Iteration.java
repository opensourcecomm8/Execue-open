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
// File Name : Iteration.java
// Date : 7/22/2008
// Author : Kaliki
//
//

package com.execue.nlp.bean.matrix;

import com.execue.nlp.bean.Group;

/**
 * Object representing an Iteration in a possibility.
 * 
 * @author kaliki
 */

public class Iteration {

   // Iteration number for that possibility. Should be unique for that possibility
   private int              id;
   // Matrix data structure for that Iteration
   private Matrix           matrix;
   // Place holder which contains all possible scenarios at end of each iteration
   private IterationSummary iterationSummary;
   // Input object for this iteration. Derived from iteration summary.
   private Summary          input;
   // Reference for previous Iteration. This object has more meaning when creating possibilities from a Iteration
   private Iteration        previousIteration;
   // Group ID Map
   private Group            group;
   // Any processor finds a new recognition will have to update this flag
   private boolean          latestRecognitionComplete;
   // Any processor finds a new association will have to update this flag
   private boolean          latestAssociationComplete;

   public Iteration (int id, Summary input) {
      this.id = id;
      this.input = input;
      // latestRecognitionComplete = input.isGroupRecognitionComplete();
      // latestAssociationComplete = input.isAssociationComplete();
   }

   public Iteration () {

   }

   public int getId () {
      return id;
   }

   public Matrix getMatrix () {
      return matrix;
   }

   public void setMatrix (Matrix matrix) {
      this.matrix = matrix;
   }

   public IterationSummary getIterationSummary () {
      return iterationSummary;
   }

   public void setIterationSummary (IterationSummary iterationSummary) {
      this.iterationSummary = iterationSummary;
   }

   public Summary getInput () {
      return input;
   }

   public Iteration getPreviousIteration () {
      return previousIteration;
   }

   public void setPreviousIteration (Iteration previousIteration) {
      this.previousIteration = previousIteration;
   }

   public Group getGroup () {
      return group;
   }

   public void setGroup (Group group) {
      this.group = group;
   }

   public boolean isLatestRecognitionComplete () {
      return latestRecognitionComplete;
   }

   public void setLatestRecognitionComplete (boolean latestRecognitionComplete) {
      this.latestRecognitionComplete = latestRecognitionComplete;
   }

   public boolean isLatestAssociationComplete () {
      return latestAssociationComplete;
   }

   public void setLatestAssociationComplete (boolean latestAssociationComplete) {
      this.latestAssociationComplete = latestAssociationComplete;
   }

   @Override
   protected Object clone () throws CloneNotSupportedException {
      Iteration iterationToBeCloned = this;
      Iteration clonedIteration = new Iteration();
      clonedIteration.setId(iterationToBeCloned.getId());
      clonedIteration.setLatestAssociationComplete(iterationToBeCloned.isLatestAssociationComplete());
      clonedIteration.setLatestRecognitionComplete(iterationToBeCloned.isLatestRecognitionComplete());
      if (iterationToBeCloned.getPreviousIteration() != null) {
         clonedIteration.setPreviousIteration((Iteration) iterationToBeCloned.getPreviousIteration().clone());
      }
      if (iterationToBeCloned.getIterationSummary() != null) {
         clonedIteration.setIterationSummary((IterationSummary) iterationToBeCloned.getIterationSummary().clone());
      }
      clonedIteration.setMatrix((Matrix) iterationToBeCloned.getMatrix().clone());
      clonedIteration.setGroup(iterationToBeCloned.getGroup());
      clonedIteration.setInput((Summary) iterationToBeCloned.getInput().clone());
      return clonedIteration;
   }

   public void setId (int id) {
      this.id = id;
   }

   public void setInput (Summary input) {
      this.input = input;
   }
}
