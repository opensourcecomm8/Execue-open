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
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.execue.core.common.bean.IWeightedEntity;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.common.bean.nlp.RecognizedType;
import com.execue.nlp.bean.ProcessorInput;
import com.execue.nlp.bean.entity.Association;
import com.execue.nlp.bean.entity.RecognitionEntity;
import com.execue.nlp.bean.entity.RecognizedCloudEntity;
import com.execue.nlp.bean.entity.ReferedTokenPosition;
import com.execue.nlp.bean.entity.TypeEntity;
import com.execue.nlp.bean.matrix.CloudParticipationMonitor;
import com.execue.nlp.bean.matrix.Possibility;
import com.execue.nlp.bean.matrix.RootMatrix;
import com.execue.nlp.engine.barcode.BarcodeScannerFactory;
import com.execue.nlp.exception.NLPExceptionCodes;
import com.execue.nlp.exception.NLPSystemException;
import com.execue.nlp.helper.NLPServiceHelper;
import com.execue.nlp.service.IRecogntionRealizationService;
import com.execue.nlp.util.NLPUtilities;
import com.execue.swi.exception.KDXException;
import com.execue.swi.service.IKDXRetrievalService;

public class PossibilityController {

   private static Logger                 logger = Logger.getLogger(PossibilityController.class);

   private RootMatrix                    rootMatrix;
   private SemanticScopingHandler        semanticScopingHandler;
   private IKDXRetrievalService          kdxRetrievalService;
   private NLPServiceHelper              nlpServiceHelper;
   private IRecogntionRealizationService recogntionRealizationServiceImpl;

   public PossibilityController () {
   }

   public PossibilityController (RootMatrix rootMatrix) {
      this.rootMatrix = rootMatrix;
   }

   /**
    * This method performs the semantic scoping
    */
   public void performSemanticScoping () {
      getSemanticScopingHandler().performSemanticScoping(rootMatrix.getPossibilities().get(0).getRecognitionEntities(),
               rootMatrix);
   }

   /**
    * This method iterates over each possibility in root matrix and finds the meaning in the user query of each
    * possibility
    */
   public void findSemantics () {
      long elapsedTime = 0;
      List<Possibility> processedPossibilityList = new ArrayList<Possibility>();
      List<Possibility> possibilityList = rootMatrix.getPossibilities();
      for (int i = 0; i < possibilityList.size(); i++) {
         elapsedTime = System.currentTimeMillis() - rootMatrix.getFindSemanticsStartTime();
         // If the query processing is taking more than configured max allowed time, throw exception
         if (rootMatrix.isTimeBasedCutoffEnabled() && rootMatrix.getMaxAllowedTimeForFindingSemantics() < elapsedTime) {
            // Set the processed list into the rootMatrix
            rootMatrix.setPossibilities(processedPossibilityList);
            logger.error("Actual Number of Possibilities : " + possibilityList.size());
            logger.error("Processed Number of Possibilities before time-out : " + processedPossibilityList.size());
            throw new NLPSystemException(NLPExceptionCodes.MAX_ALLOWED_TIME_EXCEEDED,
                     "System is taking more than allowed time for NLP Type Cloud Processing");
         }
         Possibility possibility = possibilityList.get(i);
         // This method populating app specific normalized data.For normal portal it will just return the
         // List<IWeightedEntity> recEntities but for app specific it will populate the normalized data and return the
         // rectEntities.
         getRecogntionRealizationServiceImpl().realizeRecognitions(possibility.getRecognitionEntities());
         if (logger.isDebugEnabled()) {
            logger.debug("Root Matrix # " + rootMatrix.getRootMatrixId() + " Executing Posssibility # "
                     + possibility.getId());
         }
         if (CollectionUtils.isEmpty(possibility.getRecognitionEntities())) {
            continue;
         }
         try {
            getNlpServiceHelper().updateBehaviors(possibility.getRecognitionEntities());
         } catch (KDXException e) {
            throw new NLPSystemException(e.code, e);
         }
         ProcessorInput processorInput = new ProcessorInput();
         processorInput.setRecognitionEntities(possibility.getRecognitionEntities());
         processorInput.setUnrecognizedEntities(possibility.getUnrecognizedEntities());
         processorInput.setUnrecognizedBaseRecEntities(possibility.getUnrecognizedBaseRecEntities());
         boolean isTypeProcessingComplete = false;
         Set<IWeightedEntity> typeCloudRecognitions = new HashSet<IWeightedEntity>(1);
         Integer currentIteration = 1; // By default all the rec entities will have iteration set as 1 from Semantic
         // Scoping
         CloudParticipationMonitor cloudParticipationMonitor = new CloudParticipationMonitor();
         processorInput.setCloudParticipationMonitor(cloudParticipationMonitor);
         while (!isTypeProcessingComplete) {
            if (logger.isInfoEnabled()) {
               logger.info("Finding the semantics in the user query...");
            }
            // set the current iteration and then increment(post)
            processorInput.setCurrentIteration(currentIteration++);
            List<IWeightedEntity> defaultedOutputRecognitionEntities = processorInput
                     .getDefaultedOutputRecognitionEntities();
            BarcodeScannerFactory.getInstance().getMatrixExecutor().executeMatrix(processorInput, "TypeCloud");
            List<IWeightedEntity> newDefaultedOutputRecognitionEntities = processorInput
                     .getDefaultedOutputRecognitionEntities();
            List<IWeightedEntity> entities = new ArrayList<IWeightedEntity>();
            if (!CollectionUtils.isEmpty(defaultedOutputRecognitionEntities)
                     && !CollectionUtils.isEmpty(newDefaultedOutputRecognitionEntities)) {
               for (IWeightedEntity weightedEntity : defaultedOutputRecognitionEntities) {
                  if (newDefaultedOutputRecognitionEntities.contains(weightedEntity)) {
                     entities.add(weightedEntity);
                  }
               }
            }

            Set<IWeightedEntity> newRecognitions = new HashSet<IWeightedEntity>(1);

            if (!CollectionUtils.isEmpty(processorInput.getOutputRecognitionEntities())) {
               newRecognitions.addAll(processorInput.getOutputRecognitionEntities());
            } else if (CollectionUtils.isEmpty(entities)
                     && !CollectionUtils.isEmpty(newDefaultedOutputRecognitionEntities)) {
               newRecognitions.addAll(newDefaultedOutputRecognitionEntities);
               processorInput.getDefaultedOutputRecognitionEntities().removeAll(newDefaultedOutputRecognitionEntities);
            }
            if (!CollectionUtils.isEmpty(entities)) {
               newRecognitions.addAll(entities);
               processorInput.getDefaultedOutputRecognitionEntities().removeAll(entities);
            }
            if (CollectionUtils.isEmpty(newRecognitions)) {
               typeCloudRecognitions.addAll(processorInput.getDefaultedOutputRecognitionEntities());
               isTypeProcessingComplete = true;
            } else {
               typeCloudRecognitions.addAll(newRecognitions);
               List<IWeightedEntity> newInput = new ArrayList<IWeightedEntity>(1);
               newInput.addAll(newRecognitions);
               newInput.addAll(processorInput.getRecognitionEntities());
               List<IWeightedEntity> defaultRecList = processorInput.getDefaultedOutputRecognitionEntities();
               processorInput = new ProcessorInput();
               processorInput.setRecognitionEntities(newInput);
               processorInput.setUnrecognizedEntities(possibility.getUnrecognizedEntities());
               processorInput.setUnrecognizedBaseRecEntities(possibility.getUnrecognizedBaseRecEntities());
               processorInput.setDefaultedOutputRecognitionEntities(defaultRecList);
               processorInput.setCloudParticipationMonitor(cloudParticipationMonitor);
            }
         }

         List<IWeightedEntity> ontoRecognitions = possibility.getRecognitionEntities();
         List<IWeightedEntity> finalRecognitionEntities = new ArrayList<IWeightedEntity>();
         finalRecognitionEntities.addAll(ontoRecognitions);
         finalRecognitionEntities.addAll(typeCloudRecognitions);

         // Get the non realizable type entities
         List<IWeightedEntity> nonRealizableTypeEntities = nlpServiceHelper
                  .getNonRealizableTypeEntities(finalRecognitionEntities);

         // Remove the non realizable type entities
         typeCloudRecognitions.removeAll(nonRealizableTypeEntities);
         ontoRecognitions.removeAll(nonRealizableTypeEntities);

         // Apply filters related to only type cloud recognitions
         nlpServiceHelper.filterLessPriorityTypeRecognitions(typeCloudRecognitions);
         nlpServiceHelper.filterSubsetTypeEntities(typeCloudRecognitions);
         performListForTypeInstances(typeCloudRecognitions, ontoRecognitions, processorInput);

         // Reset the final recognition entities
         finalRecognitionEntities.clear();
         finalRecognitionEntities.addAll(ontoRecognitions);
         finalRecognitionEntities.addAll(typeCloudRecognitions);

         // Apply filters related to type cloud and onto recognitions
         nlpServiceHelper.filterSharedModelTypeEntities(finalRecognitionEntities);
         possibility.setRecognitionEntities(finalRecognitionEntities);
         if (logger.isDebugEnabled()) {
            logCumulativeListForPlane1(finalRecognitionEntities);
         }
         processedPossibilityList.add(possibility);
      }
   }

   /**
    * Method to perform list for type Instances. Here the instances of type TLI and RTLI will get merged(if they can).
    * 
    * @param typeCloudRecognitionEntities
    * @param ontoRecognitions 
    * @param processorInput
    */
   private void performListForTypeInstances (Collection<IWeightedEntity> typeCloudRecognitionEntities,
            List<IWeightedEntity> ontoRecognitions, ProcessorInput processorInput) {
      if (CollectionUtils.isEmpty(typeCloudRecognitionEntities)) {
         return;
      }

      List<IWeightedEntity> recognitionEntities = new ArrayList<IWeightedEntity>(1);
      recognitionEntities.addAll(typeCloudRecognitionEntities);

      Map<Long, List<IWeightedEntity>> instancesByTypeBedId = getNlpServiceHelper().groupInstanceEntitiesByTypeBedId(
               recognitionEntities);

      //This map will be used for checking in b/w recognition are allowed for making list
      Set<IWeightedEntity> origRecognitionEntities = new HashSet<IWeightedEntity>(1);
      origRecognitionEntities.addAll(typeCloudRecognitionEntities);
      origRecognitionEntities.addAll(ontoRecognitions);

      Map<Integer, List<IWeightedEntity>> origRecEntitesByPosMap = NLPUtilities
               .getRecognitionEntitiesByPositionMap(origRecognitionEntities);
      Map<Integer, List<IWeightedEntity>> unrecognizedEntityByPosMap = NLPUtilities
               .getRecognitionEntitiesByPositionMap(processorInput.getUnrecognizedEntities());
      Map<Integer, List<IWeightedEntity>> unrecognizedBaseEntityByPosMap = NLPUtilities
               .getRecognitionEntitiesByPositionMap(processorInput.getUnrecognizedBaseRecEntities());
      // NOTE: In case of Type Instances we are removing the single entities if the entities are merged
      getNlpServiceHelper().performListForInstances(recognitionEntities, instancesByTypeBedId, origRecEntitesByPosMap,
               unrecognizedBaseEntityByPosMap, unrecognizedEntityByPosMap, true);
      typeCloudRecognitionEntities.clear();
      typeCloudRecognitionEntities.addAll(recognitionEntities);
   }

   /**
    * This method performs the app cloud processing. At the end also performs the association for the model based
    * possibilities.
    */
   public void enhanceSemantics () {
      List<Possibility> processedPossibilityList = new ArrayList<Possibility>(1);
      List<Possibility> pList = rootMatrix.getPossibilities();
      List<Possibility> enhancedSemanticsBasedPossibilityList = new ArrayList<Possibility>(1);
      for (Possibility possibility : pList) {
         long elapsedTime = System.currentTimeMillis() - rootMatrix.getEnhanceSemanticsStartTime();
         // If the query processing is taking more than configured max allowed time, throw exception
         if (rootMatrix.isTimeBasedCutoffEnabled() && rootMatrix.getMaxAllowedTimeForEnhancingSemantics() < elapsedTime) {
            // set the processed list into the rootMatrix
            rootMatrix.setPossibilities(processedPossibilityList);
            logger.error("Actual Number of Possibilities : " + pList.size());
            logger.error("Processed Number of Possibilities before time-out : " + processedPossibilityList.size());
            throw new NLPSystemException(NLPExceptionCodes.MAX_ALLOWED_TIME_EXCEEDED,
                     "System is taking more than allowed time for NLP Processing");
         }

         if (CollectionUtils.isEmpty(possibility.getRecognitionEntities())) {
            continue;
         }
         if (logger.isInfoEnabled()) {
            logger.info("Enhancing the semantics in the user query...");
         }
         try {
            getNlpServiceHelper().updateBehaviors(possibility.getRecognitionEntities());
         } catch (KDXException e) {
            throw new NLPSystemException(e.code, e);
         }
         ProcessorInput processorInput = new ProcessorInput();
         processorInput.setSearchFilter(rootMatrix.getSearchFilter());
         processorInput.setRecognitionEntities(possibility.getRecognitionEntities());
         processorInput.setUnrecognizedEntities(possibility.getUnrecognizedEntities());
         processorInput.setUnrecognizedBaseRecEntities(possibility.getUnrecognizedBaseRecEntities());
         processorInput.setImplicitRecognitionCounter(possibility.getImplicitRecognitionCounter());
         processorInput.setFromArticle(rootMatrix.isFromArticle());
         BarcodeScannerFactory.getInstance().getMatrixExecutor().executeMatrix(processorInput, "AppCloud");
         // Reset the recognition entities in the possibility
         enhancedSemanticsBasedPossibilityList.addAll(getAppCloudBasedPossibiliities(processorInput));
         // push the processed possibility into the new list
         processedPossibilityList.addAll(enhancedSemanticsBasedPossibilityList);
         rootMatrix.setConvertibleBeds(processorInput.getConvertibleBeds());
      }

      rootMatrix.setPossibilities(enhancedSemanticsBasedPossibilityList);
      if (logger.isDebugEnabled()) {
         logCumulativeListForPossibilities(enhancedSemanticsBasedPossibilityList);
      }
   }

   private void logCumulativeListForPlane1 (Collection<IWeightedEntity> cumulativeList) {
      if (logger.isDebugEnabled()) {
         StringBuffer sb = new StringBuffer();
         sb.append("\n\nPLANE 1 RESULTS: ");
         for (IWeightedEntity weightedEntity : cumulativeList) {
            RecognitionEntity recognitionEntity = (RecognitionEntity) weightedEntity;
            recognitionEntity.setLevel(2);
            List<Integer> referredTokenPositions = recognitionEntity.getReferedTokenPositions();
            ReferedTokenPosition rtp = new ReferedTokenPosition(referredTokenPositions);
            sb.append("\nToken Position: ").append(rtp).append("\tEntity: ").append(recognitionEntity.print()).append(
                     "\t\tIteration: ").append(recognitionEntity.getIteration());
         }
         logger.debug(sb.toString());
      }
   }

   private StringBuilder logCumulativeListForPlane2 (List<IWeightedEntity> cumulativeList) {
      StringBuilder sb = new StringBuilder();
      for (IWeightedEntity weightedEntity : cumulativeList) {
         RecognitionEntity recognitionEntity = (RecognitionEntity) weightedEntity;
         if (recognitionEntity.getLevel() == 3) {
            List<Integer> referedTokenPositions = recognitionEntity.getReferedTokenPositions();
            ReferedTokenPosition rtp = new ReferedTokenPosition(referedTokenPositions);
            sb.append("\nToken Position: ").append(rtp).append("\tEntity: ").append(recognitionEntity.print()).append(
                     "\t\tIteration: ").append(recognitionEntity.getIteration());
         }
      }
      return sb;
   }

   private void logCumulativeListForPossibilities (List<Possibility> modelBasedPossibilityList) {
      if (logger.isDebugEnabled()) {
         StringBuilder sb = new StringBuilder();
         sb.append("\n\nPLANE 2 RESULTS: ");
         for (Possibility possibility : modelBasedPossibilityList) {
            sb.append("\nModel: ").append(possibility.getModel().getName());
            sb.append(logCumulativeListForPlane2(possibility.getRecognitionEntities()));
         }
         logger.debug(sb.toString());
      }
   }

   private List<Possibility> getAppCloudBasedPossibiliities (ProcessorInput processorInput) {
      List<Possibility> possibilities = new ArrayList<Possibility>(1);
      List<RecognizedCloudEntity> appCloudEntities = processorInput.getAppCloudEntities();
      try {
         for (RecognizedCloudEntity appCloudEntity : appCloudEntities) {
            Possibility poss = new Possibility();
            Model model = getKdxRetrievalService().getModelByUserModelGroupId(appCloudEntity.getModelGroupId());
            poss.setId(rootMatrix.getNextPossibilityId());
            poss.setModel(model);
            for (IWeightedEntity weightedEntity : appCloudEntity.getRecognitionEntities()) {
               IWeightedEntity weightedEntityToAdd = (IWeightedEntity) weightedEntity.clone();
               reduceImportanceForAdjectiveEntity(weightedEntityToAdd, appCloudEntity);
               poss.addRecognitionEntities(weightedEntityToAdd);
            }
            List<Association> association = processorInput.getModelAssociationMap().get(
                     appCloudEntity.getModelGroupId());
            if (!CollectionUtils.isEmpty(association)) {
               poss.setAssociations(association);
            }
            poss.setUnrecognizedEntities(processorInput.getUnrecognizedEntities());
            poss.setUnrecognizedBaseRecEntities(processorInput.getUnrecognizedBaseRecEntities());
            poss.setImplicitRecognitionCounter(processorInput.getImplicitRecognitionCounter());
            possibilities.add(poss);
         }
      } catch (KDXException e) {
         throw new NLPSystemException(e.code, e);
      } catch (CloneNotSupportedException e) {
         throw new NLPSystemException(NLPExceptionCodes.CLONE_FAILED, e);
      }
      return possibilities;
   }

   private void reduceImportanceForAdjectiveEntity (IWeightedEntity weightedEntityToAdd,
            RecognizedCloudEntity appCloudEntity) {
      TypeEntity recEntityToAdd = (TypeEntity) weightedEntityToAdd;
      if (recEntityToAdd.getTypeDisplayName().equalsIgnoreCase(RecognizedType.ADJECTIVE_TYPE.getValue())) {
         for (IWeightedEntity weightedEntity : appCloudEntity.getRecognitionEntities()) {
            TypeEntity typeEntity = (TypeEntity) weightedEntity;
            if (typeEntity.getTypeDisplayName().equalsIgnoreCase(RecognizedType.TF_TYPE.getValue())
                     && typeEntity.getReferedTokenPositions().containsAll(recEntityToAdd.getReferedTokenPositions())) {
               recEntityToAdd.getWeightInformation().setImportance(0.5);
               break;
            }
         }
      }
   }

   /**
    * @return the rootMatrix
    */
   public RootMatrix getRootMatrix () {
      return rootMatrix;
   }

   /**
    * @param rootMatrix
    *           the rootMatrix to set
    */
   public void setRootMatrix (RootMatrix rootMatrix) {
      this.rootMatrix = rootMatrix;
   }

   /**
    * @return the semanticScopingHandler
    */
   public SemanticScopingHandler getSemanticScopingHandler () {
      return semanticScopingHandler;
   }

   /**
    * @param semanticScopingHandler
    *           the semanticScopingHandler to set
    */
   public void setSemanticScopingHandler (SemanticScopingHandler semanticScopingHandler) {
      this.semanticScopingHandler = semanticScopingHandler;
   }

   /**
    * @return the kdxRetrievalService
    */
   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   /**
    * @param kdxRetrievalService
    *           the kdxRetrievalService to set
    */
   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }

   /**
    * @return the nlpServiceHelper
    */
   public NLPServiceHelper getNlpServiceHelper () {
      return nlpServiceHelper;
   }

   /**
    * @param nlpServiceHelper
    *           the nlpServiceHelper to set
    */
   public void setNlpServiceHelper (NLPServiceHelper nlpServiceHelper) {
      this.nlpServiceHelper = nlpServiceHelper;
   }

   /**
    * @return the recogntionRealizationServiceImpl
    */
   public IRecogntionRealizationService getRecogntionRealizationServiceImpl () {
      return recogntionRealizationServiceImpl;
   }

   /**
    * @param recogntionRealizationServiceImpl
    *           the recogntionRealizationServiceImpl to set
    */
   public void setRecogntionRealizationServiceImpl (IRecogntionRealizationService recogntionRealizationServiceImpl) {
      this.recogntionRealizationServiceImpl = recogntionRealizationServiceImpl;
   }
}
