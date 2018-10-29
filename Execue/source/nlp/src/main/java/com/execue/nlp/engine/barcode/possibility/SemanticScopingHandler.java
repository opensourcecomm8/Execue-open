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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.execue.core.common.bean.IWeightedEntity;
import com.execue.nlp.bean.ProcessorInput;
import com.execue.nlp.bean.matrix.Possibility;
import com.execue.nlp.bean.matrix.RootMatrix;
import com.execue.nlp.engine.barcode.matrix.SemanticScopingExecutor;
import com.execue.nlp.engine.impl.CumulativeDecisionMaker;
import com.execue.nlp.processor.IEliminationSerive;
import com.execue.swi.service.IBaseKDXRetrievalService;
import com.execue.swi.service.IKDXRetrievalService;

public class SemanticScopingHandler {

   private final Logger             logger = Logger.getLogger(SemanticScopingHandler.class);

   private SemanticScopingExecutor  semanticScopingExecutor;
   private IKDXRetrievalService     kdxRetrievalService;
   private IBaseKDXRetrievalService baseKDXRetrievalService;
   private IEliminationSerive       eliminationService;
   private CumulativeDecisionMaker  cumulativeDecisionMaker;

   /**
    * In this method we will perform semantic scoping ie parallel word,SFL processing.
    * 
    * @param possibility
    * @param rootMatrix
    */

   public void performSemanticScoping (List<IWeightedEntity> recognitionEntities, RootMatrix rootMatrix) {
      long elapsedTime = 0;
      rootMatrix.setSemanticScopingStartTime(System.currentTimeMillis());
      boolean isScopingCompleted = false;
      Set<IWeightedEntity> cumulativeList = new HashSet<IWeightedEntity>(1);
      List<Possibility> processedPossibilityList = new ArrayList<Possibility>();
      cumulativeList.addAll(recognitionEntities);
      ProcessorInput processorInput = new ProcessorInput();
      processorInput.setRecognitionEntities(recognitionEntities);
      processorInput.setSearchFilter(rootMatrix.getSearchFilter());
      while (!isScopingCompleted) {
         elapsedTime = System.currentTimeMillis() - rootMatrix.getSemanticScopingStartTime();
         if (rootMatrix.isTimeBasedCutoffEnabled() && rootMatrix.getMaxAllowedTimeForSemanticScoping() < elapsedTime) {
            // Set the processed list into the rootMatrix
            rootMatrix.setPossibilities(processedPossibilityList);
            logger.error("Semantic Scoping taking more time then max allowed Time.");
            break;
         }

         semanticScopingExecutor.executeMatrix(processorInput, rootMatrix.getProcessorContext());

         // If we do not get any output recognitions, then stop the processing
         if (CollectionUtils.isEmpty(processorInput.getOutputRecognitionEntities())) {
            cumulativeList.addAll(processorInput.getDefaultedOutputRecognitionEntities());
            isScopingCompleted = true;
         } else {
            // Get the new recognitions
            Set<IWeightedEntity> newRecognitions = new HashSet<IWeightedEntity>(1);
            if (!CollectionUtils.isEmpty(processorInput.getOutputRecognitionEntities())) {
               newRecognitions.addAll(processorInput.getOutputRecognitionEntities());
            }
            // Check if all the newRecognitions are already addressed in the previous cumulative list, then stop the
            // processing
            if (cumulativeList.containsAll(newRecognitions)) {
               isScopingCompleted = true;
               continue;
            }
            cumulativeList.addAll(newRecognitions);
            List<IWeightedEntity> newInput = new ArrayList<IWeightedEntity>(1);
            newInput.addAll(newRecognitions);
            List<IWeightedEntity> recEntitiesToBeConsider = new ArrayList<IWeightedEntity>();
            recEntitiesToBeConsider.addAll(processorInput.getRecEntitiesToReConsider());
            processorInput = new ProcessorInput();
            processorInput.setRecognitionEntities(newInput);
            // Also add the recognition entities to be consider to the new input recognitions
            processorInput.setRecEntitiesToReConsider(recEntitiesToBeConsider);
            processorInput.setSearchFilter(rootMatrix.getSearchFilter());
         }
      }
      Possibility possibility = new Possibility();
      possibility.setRecognitionEntities(new ArrayList<IWeightedEntity>(cumulativeList));
      processedPossibilityList.add(possibility);
      rootMatrix.setPossibilities(processedPossibilityList);
   }

   /**
    * @return
    */
   public SemanticScopingExecutor getSemanticScopingExecutor () {
      return semanticScopingExecutor;
   }

   /**
    * @param semanticScopingExecutor
    */
   public void setSemanticScopingExecutor (SemanticScopingExecutor semanticScopingExecutor) {
      this.semanticScopingExecutor = semanticScopingExecutor;
   }

   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }

   /**
    * @return the baseKDXRetrievalService
    */
   public IBaseKDXRetrievalService getBaseKDXRetrievalService () {
      return baseKDXRetrievalService;
   }

   /**
    * @param baseKDXRetrievalService
    *           the baseKDXRetrievalService to set
    */
   public void setBaseKDXRetrievalService (IBaseKDXRetrievalService baseKDXRetrievalService) {
      this.baseKDXRetrievalService = baseKDXRetrievalService;
   }

   /**
    * @return the eliminationService
    */
   public IEliminationSerive getEliminationService () {
      return eliminationService;
   }

   /**
    * @param eliminationService
    *           the eliminationService to set
    */
   public void setEliminationService (IEliminationSerive eliminationService) {
      this.eliminationService = eliminationService;
   }

   /**
    * @return the cumulativeDecisionMaker
    */
   public CumulativeDecisionMaker getCumulativeDecisionMaker () {
      return cumulativeDecisionMaker;
   }

   /**
    * @param cumulativeDecisionMaker
    *           the cumulativeDecisionMaker to set
    */
   public void setCumulativeDecisionMaker (CumulativeDecisionMaker cumulativeDecisionMaker) {
      this.cumulativeDecisionMaker = cumulativeDecisionMaker;
   }

}
