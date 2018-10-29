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
package com.execue.content.postprocessor.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.execue.content.postprocessor.configuration.IContentPostProcessorConfigurationService;
import com.execue.content.postprocessor.exception.ContentPostProcessorException;
import com.execue.content.postprocessor.exception.ContentPostProcessorExceptionCodes;
import com.execue.content.postprocessor.service.IContentPostProcessingOptimizationService;
import com.execue.core.common.bean.INormalizedData;
import com.execue.core.common.bean.entity.NormalizedDataType;
import com.execue.core.common.bean.graph.Graph;
import com.execue.core.common.bean.graph.GraphPath;
import com.execue.core.common.bean.graph.IGraphComponent;
import com.execue.core.common.bean.graph.IGraphPath;
import com.execue.core.common.bean.nlp.DomainRecognition;
import com.execue.core.common.bean.nlp.InstanceInformation;
import com.execue.core.common.bean.nlp.ListNormalizedData;
import com.execue.core.common.bean.nlp.NormalizedDataEntity;
import com.execue.core.common.bean.nlp.SemanticPossibility;
import com.execue.core.common.bean.sdata.location.LocationPointInfo;
import com.execue.core.common.util.NLPUtils;
import com.execue.sdata.exception.LocationException;
import com.execue.sus.helper.SemantificationHelper;

/**
 * @author Nitesh
 *
 */
public class ContentPostProcessingOptimizationServiceImpl implements IContentPostProcessingOptimizationService {

   private static Logger                             logger = Logger
                                                                     .getLogger(ContentPostProcessingOptimizationServiceImpl.class);

   private IContentPostProcessorConfigurationService contentPostProcessorConfigurationService;
   private SemantificationHelper                     semantificationHelper;

   @Override
   public List<LocationPointInfo> optimizeGraphForValidLocation (SemanticPossibility semanticPossibility)
            throws ContentPostProcessorException {
      try {
         List<LocationPointInfo> validLocationInfos = getSemantificationHelper().getLocationPointInformations(
                  semanticPossibility, true);
         if (CollectionUtils.isEmpty(validLocationInfos)) {
            logger.error("LOCATION INFORMATION NOT FOUND... ");
            throw new ContentPostProcessorException(ContentPostProcessorExceptionCodes.DEFAULT_SYSTEM_EXCEPTION_CODE,
                     "LOCATION INFORMATION NOT FOUND... ");
         }
         return validLocationInfos;
      } catch (LocationException e) {
         throw new ContentPostProcessorException(e.getCode(), e);
      }
   }

   /**
    * Method to optimize the grapth while removing the extra recognitions and paths for single instance concepts.
    * 
    * @param semanticPossibility
    * @param conceptsConsideredForSingleInstance
    * @param requiredInstanceRecQuality
    */
   public void optimizeGraph (SemanticPossibility semanticPossibility) {

      Graph reducedForm = semanticPossibility.getReducedForm();

      Set<String> singleValuedRecognitionNames = NLPUtils.getSingleValueRecognitionNames(reducedForm);
      double qualityThresholdForInstance = getContentPostProcessorConfigurationService()
               .getQualityThresholdForInstance();

      Set<IGraphComponent> removedVertex = new HashSet<IGraphComponent>(1);

      for (String conceptName : singleValuedRecognitionNames) {

         // Prepare the connected, unconnected vertices information each time as they might get updated in each
         // iteration
         Collection<IGraphComponent> connectedVertices = NLPUtils.getConnectedVertices(reducedForm);
         Collection<IGraphComponent> unconnectedVertices = NLPUtils.getUnconnectedVertices(reducedForm);
         Map<String, List<IGraphComponent>> connectedVerticesByConceptNameMap = getGraphComponentsByConceptNameMap(connectedVertices);
         Map<String, List<IGraphComponent>> unconnectedVerticesByConceptNameMap = getGraphComponentsByConceptNameMap(unconnectedVertices);

         // Populate paths which attach instances of single instance concept and populate paths which attach single
         // instance concept entity.
         List<IGraphPath> singleInstanceToInstancePaths = new ArrayList<IGraphPath>(1);
         List<IGraphPath> singleInstancePaths = new ArrayList<IGraphPath>(1);
         List<IGraphPath> singleInstanceConceptPaths = new ArrayList<IGraphPath>(1);
         Set<Long> existingPathConceptIds = new HashSet<Long>(1);
         populatePathsForSingleInstanceConcepts(reducedForm, conceptName, singleInstancePaths,
                  singleInstanceConceptPaths, existingPathConceptIds, singleInstanceToInstancePaths);

         // populate single instance recognition which are not connected with the given concept
         List<IGraphComponent> unconnectedVerticesOfConcept = unconnectedVerticesByConceptNameMap.get(conceptName
                  .toLowerCase());
         List<IGraphComponent> singleInstanceUnconnectedRecognitions = new ArrayList<IGraphComponent>(1);
         List<IGraphComponent> singleInstanceUnconnectedFirstSentenceRecognitions = new ArrayList<IGraphComponent>(1);
         populateUnconnectedSingleInstanceRecognitions(reducedForm, unconnectedVerticesOfConcept,
                  singleInstanceUnconnectedRecognitions, singleInstanceUnconnectedFirstSentenceRecognitions,
                  qualityThresholdForInstance);

         // populate single instance recognition which are connected with the given concept.
         List<IGraphComponent> connectedVerticesOfConcept = connectedVerticesByConceptNameMap.get(conceptName
                  .toLowerCase());
         List<IGraphComponent> singleInstanceConnectedFirstSentenceRecognitions = new ArrayList<IGraphComponent>(1);
         populateConnectedSingleInstanceRecognitions(reducedForm, connectedVerticesOfConcept,
                  singleInstanceConnectedFirstSentenceRecognitions, qualityThresholdForInstance);

         // First priority is the title of the article i.e. first sentence(possibility) unconnected good quality
         // recognitions
         if (!CollectionUtils.isEmpty(singleInstanceUnconnectedFirstSentenceRecognitions)
                  && CollectionUtils.isEmpty(singleInstanceConnectedFirstSentenceRecognitions)) {
            // Remove all the unconnected recognitions for the single Instance Concept if a Path already exists
            removeUnconnectedSingleInstanceRecognitions(reducedForm, removedVertex,
                     singleInstanceUnconnectedRecognitions);

            // Remove paths which attach single instance concept entity
            removePathsForSingleInstanceConcepts(reducedForm, removedVertex, singleInstanceConceptPaths, null,
                     conceptName);

            // Remove instance to concept can be removed i.e chevy-isrealizedAs-Impala is
            // found then make-isRelaizedAs-Impala can be removed where model is current concept.
            removePathsForSingleInstanceConcepts(reducedForm, removedVertex, singleInstancePaths, null, conceptName);

            List<IGraphComponent> usedVertices = new ArrayList<IGraphComponent>(1);

            if (singleInstanceUnconnectedFirstSentenceRecognitions.size() > 1) {
               NLPUtils.sortComponentsBySentenceIdAndQuality(singleInstanceUnconnectedFirstSentenceRecognitions);
               DomainRecognition domainRecognition = (DomainRecognition) singleInstanceUnconnectedFirstSentenceRecognitions
                        .get(0);
               for (IGraphComponent graphComponent : singleInstanceUnconnectedFirstSentenceRecognitions) {
                  DomainRecognition recognition = (DomainRecognition) graphComponent;
                  if (!domainRecognition.equals(recognition)) {
                     removedVertex.add(recognition);
                     reducedForm.removeVertex(recognition);
                  }
               }
            }

            // Method to remove the extra single Instance to Instance Paths and the unused vertex they connect
            removeInstanceToInstancePaths(reducedForm, removedVertex, singleInstanceToInstancePaths,
                     existingPathConceptIds, usedVertices, conceptName);

         } else if (singleInstancePaths.size() > 0 || singleInstanceToInstancePaths.size() > 0) {
            // Remove all the unconnected recognitions for the single Instance Concept if a path already exists
            removeUnconnectedSingleInstanceRecognitions(reducedForm, removedVertex,
                     singleInstanceUnconnectedRecognitions);
            removeUnconnectedSingleInstanceRecognitions(reducedForm, removedVertex,
                     singleInstanceUnconnectedFirstSentenceRecognitions);

            if (singleInstanceToInstancePaths.size() > 0) {
               // sort the path by proximity of sentence first, then quality, then paths start and end proximity
               NLPUtils.sortPathsByProximity(singleInstanceToInstancePaths);
               IGraphPath graphPath = singleInstanceToInstancePaths.get(0);
               // remove paths which attach single Instance Concept entity if the corresponding single Instance Path is
               // present.
               removePathsForSingleInstanceConcepts(reducedForm, removedVertex, singleInstanceConceptPaths, graphPath,
                        conceptName);
               // if instance to instance paths are found instance to concept can be removed i.e
               // chevy-isrealizedAs-Impala is
               // found then make-isRelaizedAs-Impala can be removed where model is current concept.
               removePathsForSingleInstanceConcepts(reducedForm, removedVertex, singleInstancePaths, graphPath,
                        conceptName);
               List<IGraphComponent> usedVertices = new ArrayList<IGraphComponent>(1);
               usedVertices.add(graphPath.getStartVertex());
               usedVertices.add(graphPath.getEndVertex());
               // Method to remove the extra single Instance Paths and the unused Vertice they conncet
               removeExtraPathsAndRecognition(reducedForm, removedVertex, singleInstanceToInstancePaths,
                        existingPathConceptIds, graphPath, usedVertices, conceptName);
            } else {
               // sort the path by proximity of sentence first, then quality, then paths start and end proximity
               NLPUtils.sortPathsByProximity(singleInstancePaths);
               IGraphPath graphPath = singleInstancePaths.get(0);
               removePathsForSingleInstanceConcepts(reducedForm, removedVertex, singleInstanceConceptPaths, graphPath,
                        conceptName);

               List<IGraphComponent> usedVertices = new ArrayList<IGraphComponent>(1);
               usedVertices.add(graphPath.getStartVertex());
               usedVertices.add(graphPath.getEndVertex());
               // Method to remove the extra single Instance Paths and the unused vertex they connect
               removeExtraPathsAndRecognition(reducedForm, removedVertex, singleInstancePaths, existingPathConceptIds,
                        graphPath, usedVertices, conceptName);
            }
         } else if (!CollectionUtils.isEmpty(singleInstanceUnconnectedRecognitions)
                  && singleInstanceUnconnectedRecognitions.size() > 1) {
            singleInstanceUnconnectedRecognitions.addAll(singleInstanceUnconnectedFirstSentenceRecognitions);
            NLPUtils.sortComponentsBySentenceIdAndQuality(singleInstanceUnconnectedRecognitions);
            DomainRecognition domainRecognition = getBestRecognitionBasedOnQualityThreshold(
                     singleInstanceUnconnectedRecognitions, qualityThresholdForInstance);
            for (IGraphComponent graphComponent : singleInstanceUnconnectedRecognitions) {
               DomainRecognition recognition = (DomainRecognition) graphComponent;
               if (domainRecognition == null || !domainRecognition.equals(recognition)) {
                  removedVertex.add(recognition);
                  reducedForm.removeVertex(recognition);
               }
            }
         }
      }
      semanticPossibility.setRemovedVertex(removedVertex);
   }

   private DomainRecognition getBestRecognitionBasedOnQualityThreshold (
            List<IGraphComponent> singleInstanceUnconnectedRecognitions, double requiredInstanceRecQuality) {
      DomainRecognition rec = null;
      for (IGraphComponent graphComponent : singleInstanceUnconnectedRecognitions) {
         if (((DomainRecognition) graphComponent).getWeightInformation().getRecognitionQuality() >= requiredInstanceRecQuality) {
            rec = (DomainRecognition) graphComponent;
            break;
         }
      }
      return rec;
   }

   /**
    * @param reducedForm
    * @param removedVertex
    * @param singleInstanceRecognitions
    */
   private void removeUnconnectedSingleInstanceRecognitions (Graph reducedForm, Set<IGraphComponent> removedVertex,
            List<IGraphComponent> singleInstanceRecognitions) {
      for (IGraphComponent singleInstanceRec : singleInstanceRecognitions) {
         removedVertex.add(singleInstanceRec);
         reducedForm.removeVertex(singleInstanceRec);
      }
   }

   /**
    * @param reducedForm
    * @param removedVertex
    * @param singleInstanceConceptPaths
    * @param conceptName
    * @param graphPathToBeRetain
    */
   private void removePathsForSingleInstanceConcepts (Graph reducedForm, Set<IGraphComponent> removedVertex,
            List<IGraphPath> singleInstanceConceptPaths, IGraphPath graphPathToBeRetain, String conceptName) {
      for (IGraphPath graphPath1 : singleInstanceConceptPaths) {
         DomainRecognition endVertex = (DomainRecognition) graphPath1.getEndVertex();
         DomainRecognition startVertex = (DomainRecognition) graphPath1.getStartVertex();
         if (graphPathToBeRetain != null) {
            DomainRecognition endVertexSelected = (DomainRecognition) graphPathToBeRetain.getEndVertex();
            DomainRecognition startVertexSelected = (DomainRecognition) graphPathToBeRetain.getStartVertex();
            if (startVertexSelected.getConceptName().equalsIgnoreCase(conceptName)
                     && startVertexSelected.equals(endVertex)) {
               continue;
            } else if (endVertexSelected.getConceptName().equalsIgnoreCase(conceptName)
                     && endVertexSelected.equals(startVertex)) {
               continue;
            }
         }
         reducedForm.removePath(graphPath1);
         int pathLength = graphPath1.getPathLength();
         int start = 0;
         for (int i = 3; i <= pathLength;) {
            List<IGraphComponent> components = graphPath1.getPartialPath(start, i);
            DomainRecognition destination = (DomainRecognition) components.get(2);
            removedVertex.add(destination);
            reducedForm.removeVertex(destination);
            start = start + 2;
            i = i + 2;
         }
      }
   }

   private void removeInstanceToInstancePaths (Graph reducedForm, Set<IGraphComponent> removedVertex,
            List<IGraphPath> singleInstanceToInstancePaths, Set<Long> existingPathConceptIds,
            List<IGraphComponent> usedVertices, String conceptName) {
      if (CollectionUtils.isEmpty(singleInstanceToInstancePaths)) {
         return;
      }

      for (IGraphPath graphPath1 : singleInstanceToInstancePaths) {
         int startIndex = 0;
         int pathLength = graphPath1.getPathLength();
         for (int endIndex = 3; endIndex <= pathLength;) {
            List<IGraphComponent> components = graphPath1.getPartialPath(startIndex, endIndex);
            DomainRecognition startVertex = (DomainRecognition) components.get(0);
            DomainRecognition endVertex = (DomainRecognition) components.get(2);

            if (!usedVertices.contains(endVertex)) {
               removedVertex.add(endVertex);
               reducedForm.removeVertex(endVertex);
            }
            if (!usedVertices.contains(startVertex) && existingPathConceptIds.contains(startVertex.getConceptBEDId())) {
               removedVertex.add(startVertex);
               reducedForm.removeVertex(startVertex);
            }
            startIndex = startIndex + 2;
            endIndex = endIndex + 2;
         }
         reducedForm.removePath(graphPath1);
      }
   }

   /**
    * Method to remove the extra recognition and paths for the single instance concepts.
    * 
    * @param reducedForm
    * @param removedVertex
    * @param singleInstanceToInstancePaths
    * @param existingPathConceptIds
    * @param graphPathToRetain
    * @param usedVertices
    * @param conceptName
    */
   private void removeExtraPathsAndRecognition (Graph reducedForm, Set<IGraphComponent> removedVertex,
            List<IGraphPath> singleInstanceToInstancePaths, Set<Long> existingPathConceptIds,
            IGraphPath graphPathToRetain, List<IGraphComponent> usedVertices, String conceptName) {
      if (CollectionUtils.isEmpty(singleInstanceToInstancePaths) || graphPathToRetain == null) {
         return;
      }
      List<List<IGraphComponent>> pathsToRetain = new ArrayList<List<IGraphComponent>>(1);
      int pathToRetainStartIndex = 0;
      int pathToRetainLength = graphPathToRetain.getPathLength();

      // Add the partial paths to paths to retain list
      for (int pathToRetainEndIndex = 3; pathToRetainEndIndex <= pathToRetainLength;) {
         List<IGraphComponent> components = graphPathToRetain.getPartialPath(pathToRetainStartIndex,
                  pathToRetainEndIndex);
         pathsToRetain.add(components);
         pathToRetainStartIndex = pathToRetainStartIndex + 2;
         pathToRetainEndIndex = pathToRetainEndIndex + 2;
      }

      for (List<IGraphComponent> pathToRetain : pathsToRetain) {
         DomainRecognition pathToRetainStartVertex = (DomainRecognition) pathToRetain.get(0);
         DomainRecognition pathToRetainEndVertex = (DomainRecognition) pathToRetain.get(2);
         for (IGraphPath graphPath1 : singleInstanceToInstancePaths) {
            // If path to retain and current path is same, then continue
            if (graphPath1.equals(graphPathToRetain) || graphPath1.intersectsWith(graphPathToRetain)) {
               continue;
            }

            reducedForm.removePath(graphPath1);

            int startIndex = 0;
            int pathLength = graphPath1.getPathLength();
            for (int endIndex = 3; endIndex <= pathLength;) {
               List<IGraphComponent> components = graphPath1.getPartialPath(startIndex, endIndex);
               DomainRecognition startVertex = (DomainRecognition) components.get(0);
               DomainRecognition endVertex = (DomainRecognition) components.get(2);
               startIndex = startIndex + 2;
               endIndex = endIndex + 2;

               // If start and end vertex are not of same concept, then continue
               if (!startVertex.getConceptName().equalsIgnoreCase(pathToRetainStartVertex.getConceptName())
                        || !endVertex.getConceptName().equalsIgnoreCase(pathToRetainEndVertex.getConceptName())) {
                  continue;
               }

               // If start vertex is the current single instance but start vertex is same as the selected end vertex
               if (pathToRetainStartVertex.getConceptName().equalsIgnoreCase(conceptName)
                        && pathToRetainStartVertex.equals(endVertex)) {
                  continue;
               } else if (pathToRetainEndVertex.getConceptName().equalsIgnoreCase(conceptName)
                        && pathToRetainEndVertex.equals(startVertex)) {
                  continue;
               }

               if (!usedVertices.contains(endVertex)) {
                  removedVertex.add(endVertex);
                  reducedForm.removeVertex(endVertex);
               }

               if (!usedVertices.contains(startVertex)
                        && existingPathConceptIds.contains(startVertex.getConceptBEDId())) {
                  removedVertex.add(startVertex);
                  reducedForm.removeVertex(startVertex);
               }
            }
         }
      }
   }

   /**
    * @param reducedForm
    * @param singleInstanceUnconnectedRecognitions
    * @param singleInstanceUnconnectedFirstSentenceRecognitions
    * @param requiredInstanceRecQuality
    * @param singleInstanceConnectedFirstSentenceRecognitions2
    */
   private void populateUnconnectedSingleInstanceRecognitions (Graph reducedForm,
            List<IGraphComponent> unconnectedVertices, List<IGraphComponent> singleInstanceUnconnectedRecognitions,
            List<IGraphComponent> singleInstanceUnconnectedFirstSentenceRecognitions, double requiredInstanceRecQuality) {
      if (CollectionUtils.isEmpty(unconnectedVertices)) {
         return;
      }
      for (IGraphComponent gc : unconnectedVertices) {
         DomainRecognition rec = (DomainRecognition) gc;
         if (rec.getDefaultInstanceValue() == null) {
            continue;
         }
         INormalizedData normalizedData = rec.getNormalizedData();
         if (normalizedData != null
                  && normalizedData.getNormalizedDataType() == NormalizedDataType.LIST_NORMALIZED_DATA) {
            updateAndMakeSingleInstance(rec, reducedForm);
         }

         if (rec.getSentenceId().equals(1)
                  && rec.getWeightInformation().getRecognitionQuality() >= requiredInstanceRecQuality) {
            singleInstanceUnconnectedFirstSentenceRecognitions.add(rec);
         } else {
            singleInstanceUnconnectedRecognitions.add(rec);
         }
      }
   }

   /**
    * @param reducedForm
    * @param connectedVertices
    * @param singleInstanceConnectedFirstSentenceRecognitions
    * @param requiredInstanceRecQuality
    */
   private void populateConnectedSingleInstanceRecognitions (Graph reducedForm,
            List<IGraphComponent> connectedVertices,
            List<IGraphComponent> singleInstanceConnectedFirstSentenceRecognitions, double requiredInstanceRecQuality) {
      if (CollectionUtils.isEmpty(connectedVertices)) {
         return;
      }
      for (IGraphComponent gc : connectedVertices) {
         DomainRecognition rec = (DomainRecognition) gc;
         if (rec.getDefaultInstanceValue() == null
                  || rec.getWeightInformation().getRecognitionQuality() < requiredInstanceRecQuality) {
            continue;
         }
         if (rec.getSentenceId().equals(1)) {
            singleInstanceConnectedFirstSentenceRecognitions.add(rec);
         }
      }
   }

   /**
    * @param reducedForm
    * @param conceptName
    * @param singleInstancePaths
    * @param singleInstanceConceptPaths
    * @param existingPathConceptIds
    * @param singleInstanceToInstancePaths
    */
   private void populatePathsForSingleInstanceConcepts (Graph reducedForm, String conceptName,
            List<IGraphPath> singleInstancePaths, List<IGraphPath> singleInstanceConceptPaths,
            Set<Long> existingPathConceptIds, List<IGraphPath> singleInstanceToInstancePaths) {
      List<IGraphPath> paths = reducedForm.getPaths();
      for (IGraphPath graphPath : paths) {
         GraphPath path = (GraphPath) graphPath;
         int pathLength = graphPath.getPathLength();
         int start = 0;
         for (int i = 3; i <= pathLength;) {
            List<IGraphComponent> components = graphPath.getPartialPath(start, i);
            DomainRecognition source = (DomainRecognition) components.get(0);
            DomainRecognition destination = (DomainRecognition) components.get(2);
            if (source.getConceptBEDId() != null) {
               existingPathConceptIds.add(source.getConceptBEDId());
            }
            if (destination.getConceptBEDId() != null) {
               existingPathConceptIds.add(destination.getConceptBEDId());
            }
            INormalizedData normalizedData = destination.getNormalizedData();
            if (conceptName.equals(destination.getConceptName()) && normalizedData != null
                     && normalizedData.getNormalizedDataType() == NormalizedDataType.LIST_NORMALIZED_DATA) {
               updateAndMakeSingleInstance(destination, reducedForm);
            }
            normalizedData = source.getNormalizedData();
            if (conceptName.equals(source.getConceptName()) && normalizedData != null
                     && normalizedData.getNormalizedDataType() == NormalizedDataType.LIST_NORMALIZED_DATA) {
               updateAndMakeSingleInstance(source, reducedForm);
            }
            if (conceptName.equalsIgnoreCase(source.getConceptName()) && source.getDefaultInstanceBedId() != null
                     && destination.getDefaultInstanceBedId() != null) {
               singleInstanceToInstancePaths.add(path);
            } else if (conceptName.equals(source.getConceptName()) && source.getDefaultInstanceBedId() != null) {
               singleInstancePaths.add(path);
            } else if (conceptName.equals(source.getConceptName())) {
               singleInstanceConceptPaths.add(path);
            } else if (conceptName.equalsIgnoreCase(destination.getConceptName())
                     && destination.getDefaultInstanceBedId() != null && source.getDefaultInstanceBedId() != null) {
               singleInstanceToInstancePaths.add(path);
            } else if (conceptName.equals(destination.getConceptName())
                     && destination.getDefaultInstanceBedId() != null) {
               singleInstancePaths.add(path);
            } else if (conceptName.equals(destination.getConceptName())) {
               singleInstanceConceptPaths.add(path);
            }
            start = start + 2;
            i = i + 2;
         }
      }
   }

   private void updateAndMakeSingleInstance (DomainRecognition domainRec, Graph reducedForm) {
      INormalizedData normalizedData = domainRec.getNormalizedData();
      if (normalizedData == null || normalizedData.getNormalizedDataType() != NormalizedDataType.LIST_NORMALIZED_DATA) {
         return;
      }
      String key = getBusinessEntityName(domainRec) + domainRec.getConceptName() + "-" + domainRec.getPosition();
      ListNormalizedData listNormalizedData = (ListNormalizedData) normalizedData;
      NormalizedDataEntity bestEntity = chooseBestNormalizedEntity(listNormalizedData.getNormalizedDataEntities());
      domainRec.setNormalizedData(bestEntity.getNormalizedData());
      InstanceInformation instanceInformation = new InstanceInformation();
      instanceInformation.setInstanceBedId(bestEntity.getValueBedId());
      instanceInformation.setInstanceDisplayName(bestEntity.getDisplayValue());
      instanceInformation.setInstanceValue(bestEntity.getValue());
      domainRec.getInstanceInformations().clear();
      domainRec.getInstanceInformations().add(instanceInformation);
      String newKey = getBusinessEntityName(domainRec) + domainRec.getConceptName() + "-" + domainRec.getPosition();
      reducedForm.getVertices().remove(key);
      reducedForm.getVertices().put(newKey, domainRec);

   }

   /**
    * @param normalizedDataEntities
    * @return
    */
   public static NormalizedDataEntity chooseBestNormalizedEntity (List<NormalizedDataEntity> normalizedDataEntities) {
      if (CollectionUtils.isEmpty(normalizedDataEntities)) {
         return null;
      }
      double quality = 0;
      Collections.sort(normalizedDataEntities, new Comparator<NormalizedDataEntity>() {

         public int compare (NormalizedDataEntity p1, NormalizedDataEntity p2) {
            if (p1.getWeightInformation().getRecognitionQuality() > p2.getWeightInformation().getRecognitionQuality()) {
               return -1;
            } else if (p1.getWeightInformation().getRecognitionQuality() == p2.getWeightInformation()
                     .getRecognitionQuality()) {
               return 0;
            } else {
               return 1;
            }
         }
      });
      List<NormalizedDataEntity> bestRecognitions = new ArrayList<NormalizedDataEntity>(1);
      for (NormalizedDataEntity normalizedDataEntity : normalizedDataEntities) {
         if (quality == 0) {
            quality = normalizedDataEntity.getWeightInformation().getRecognitionQuality();
            bestRecognitions.add(normalizedDataEntity);
         } else if (quality == normalizedDataEntity.getWeightInformation().getRecognitionQuality()) {
            bestRecognitions.add(normalizedDataEntity);
         } else {
            break;
         }
      }
      NormalizedDataEntity bestEntity = bestRecognitions.get(0);
      return bestEntity;
   }

   /**
    * @param recognition
    * @return
    */
   private String getBusinessEntityName (DomainRecognition recognition) {
      String businessEntityName = "";
      if (recognition.getDefaultInstanceValue() != null) {
         int i = 0;
         for (String name : recognition.getInstanceNames()) {
            if (i > 0) {
               businessEntityName = businessEntityName + ",";
            }
            businessEntityName = businessEntityName + name;
            i++;
         }
      } else {
         businessEntityName = recognition.getBusinessEntityName();
      }
      return businessEntityName;
   }

   private Map<String, List<IGraphComponent>> getGraphComponentsByConceptNameMap (
            Collection<IGraphComponent> unconnectedVertices) {
      Map<String, List<IGraphComponent>> unconnectedVerticesByConceptNameMap = new HashMap<String, List<IGraphComponent>>(
               1);

      if (CollectionUtils.isEmpty(unconnectedVertices)) {
         return unconnectedVerticesByConceptNameMap;
      }

      for (IGraphComponent graphComponent : unconnectedVertices) {
         DomainRecognition rec = (DomainRecognition) graphComponent;
         if (rec.getConceptName() == null) {
            continue;
         }
         String conceptName = rec.getConceptName().toLowerCase();
         List<IGraphComponent> list = unconnectedVerticesByConceptNameMap.get(conceptName);
         if (list == null) {
            list = new ArrayList<IGraphComponent>(1);
            unconnectedVerticesByConceptNameMap.put(conceptName, list);
         }
         list.add(rec);
      }

      return unconnectedVerticesByConceptNameMap;
   }

   /**
    * @return the contentPostProcessorConfigurationService
    */
   public IContentPostProcessorConfigurationService getContentPostProcessorConfigurationService () {
      return contentPostProcessorConfigurationService;
   }

   /**
    * @param contentPostProcessorConfigurationService the contentPostProcessorConfigurationService to set
    */
   public void setContentPostProcessorConfigurationService (
            IContentPostProcessorConfigurationService contentPostProcessorConfigurationService) {
      this.contentPostProcessorConfigurationService = contentPostProcessorConfigurationService;
   }

   /**
    * @return the semantificationHelper
    */
   public SemantificationHelper getSemantificationHelper () {
      return semantificationHelper;
   }

   /**
    * @param semantificationHelper the semantificationHelper to set
    */
   public void setSemantificationHelper (SemantificationHelper semantificationHelper) {
      this.semantificationHelper = semantificationHelper;
   }
}