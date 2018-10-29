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


package com.execue.nlp.engine.barcode.possibility;

import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.nlp.CandidateEntity;
import com.execue.core.constants.ExecueConstants;
import com.execue.nlp.bean.NLPToken;
import com.execue.nlp.bean.ProcessorInput;
import com.execue.nlp.bean.entity.ICandidate;
import com.execue.nlp.bean.entity.RecognitionEntity;
import com.execue.nlp.bean.matrix.Iteration;
import com.execue.nlp.bean.matrix.IterationSummary;
import com.execue.nlp.bean.matrix.Possibility;
import com.execue.nlp.bean.matrix.PossibilityStatus;
import com.execue.nlp.bean.matrix.RootMatrix;
import com.execue.nlp.bean.matrix.Summary;
import com.execue.nlp.engine.barcode.BarcodeScannerFactory;
import com.execue.nlp.engine.barcode.matrix.MatrixUtility;

/**
 * @author Kaliki
 */
public abstract class AbstractExecPossibility implements IExecutablePossibility {

   private static Logger logger = Logger.getLogger(AbstractExecPossibility.class);

   private Possibility   possibility;
   private RootMatrix    rootMatrix;

   public void setPossibility (Possibility possibility) {
      this.possibility = possibility;
   }

   public Possibility getPossibility () {
      return possibility;
   }

   public RootMatrix getRootMatrix () {
      return rootMatrix;
   }

   public void setRootMatrix (RootMatrix rootMatrix) {
      this.rootMatrix = rootMatrix;
   }

   protected void process () {
      // TODO: MODIFY TO GOOD CODE
      while (!possibility.isCompleted()) {
         runIteration();

         // Check for Output Summary List
         // If more than one summaries are present then there was a change and iterations
         // should continue with new possibilities (Possibilities need to be created here)
         // Else check if Input and new summary is same.
         // If yes change the possibility status to complete, else continue Iterations
         IterationSummary iterationSummary = possibility.getCurrentIteration().getIterationSummary();
         Summary input = possibility.getCurrentIteration().getInput();
         Possibility tempPossibility;
         if (iterationSummary != null && iterationSummary.size() > 0) {
            for (int i = 0; i < iterationSummary.size(); i++) {
               Summary summary = iterationSummary.getSummary(i);
               if (i == 0) {
                  tempPossibility = possibility;
                  // if(MatrixUtility.isSummaryEquals(input,summary)){
                  if (summary.getStatus().equals(PossibilityStatus.COMPLETED)) {
                     possibility.setStatus(Possibility.COMPLETED);
                  } else {
                     tempPossibility.addIteration(MatrixUtility.createIteration(summary, tempPossibility
                              .getNextIterationId()));
                  }
               } else {
                  tempPossibility = MatrixUtility.createPossibility(summary, rootMatrix.getNextPossibilityId());
                  tempPossibility.setModel(possibility.getModel());
                  rootMatrix.addPossibility(tempPossibility);
               }
               // Iterate Over Input Tokens
               // Check for Candidate Entity
               // populateCandidates(summary, tempPossibility);
            }
         } else {
            possibility.setStatus(Possibility.COMPLETED);
         }

         /*
          * Iteration iteration = possibility.getCurrentIteration(); Iteration newIteration =
          * MatrixUtility.createIteration(iteration.getInput()); possibility.addIteration(newIteration); runIteration();
          */
      }
   }

   /**
    * Run current iteration on the possibility
    */
   protected void runIteration () {
      Iteration iteration = possibility.getCurrentIteration();
      if (logger.isInfoEnabled()) {
         logger.info("Root Matrix # " + rootMatrix.getRootMatrixId() + " Possibility # " + possibility.getId()
                  + " Executing Iteration # " + iteration.getId());
      }
      // run Matrix

      ProcessorInput processorInput = new ProcessorInput();
      processorInput.setModel(possibility.getModel());
      BarcodeScannerFactory.getInstance().getMatrixExecutor().executeMatrix(processorInput,
               rootMatrix.getProcessorContext());
   }

   /**
    * Run matrix for current iteration
    */
   protected void runMatrix () {
      // moved to different method
   }

   /**
    * Summerizes the Matrix Information
    * 
    * @return Summary at end for each processing
    */
   protected Summary summarize () {
      Iteration iteration = possibility.getCurrentIteration();
      // TODO: possibility detection
      return null;
   }

   public void start () {
      possibility.setStatus(Possibility.STARTED);
   }

   private void populateCandidates (Summary summary, Possibility tempPossibility) {
      for (NLPToken token : summary.getNLPTokens()) {
         for (RecognitionEntity entity : token.getRecognitionEntities()) {
            if (entity instanceof ICandidate) {
               for (CandidateEntity centity : ((ICandidate) entity).getCandidates()) {
                  List<Integer> positions = token.getOriginalPositions();
                  Collections.sort(positions);
                  String positionRange = positions.get(0) + ExecueConstants.RANGE_DENOTER
                           + positions.get(positions.size() - 1);
                  centity.setOriginalPositionRange(positionRange);
                  possibility.addPossibleCandidate(centity);
               }
            }
         }
      }
   }
}
