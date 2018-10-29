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


package com.execue.sus.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.execue.core.common.bean.INormalizedData;
import com.execue.core.common.bean.WeightInformation;
import com.execue.core.common.bean.entity.Application;
import com.execue.core.common.bean.entity.NormalizedDataType;
import com.execue.core.common.bean.graph.Graph;
import com.execue.core.common.bean.graph.GraphPath;
import com.execue.core.common.bean.graph.IGraphComponent;
import com.execue.core.common.bean.graph.IGraphPath;
import com.execue.core.common.bean.nlp.DomainRecognition;
import com.execue.core.common.bean.nlp.InstanceInformation;
import com.execue.core.common.bean.nlp.ListNormalizedData;
import com.execue.core.common.bean.nlp.NormalizedDataEntity;
import com.execue.core.common.bean.nlp.RangeNormalizedData;
import com.execue.core.common.bean.nlp.SemanticPossibility;
import com.execue.core.common.bean.qdata.ContentRFXValue;
import com.execue.core.common.bean.qdata.ContentReducedFormIndex;
import com.execue.core.common.bean.qdata.NewsItem;
import com.execue.core.common.bean.qdata.RFXValue;
import com.execue.core.common.bean.qdata.RIQueryCache;
import com.execue.core.common.bean.qdata.RIUnStructuredIndex;
import com.execue.core.common.bean.qdata.RIUniversalSearch;
import com.execue.core.common.bean.qdata.RIUserQuery;
import com.execue.core.common.bean.qdata.ReducedFormIndex;
import com.execue.core.common.bean.qdata.UnStructuredIndex;
import com.execue.core.common.bean.qdata.UserQueryRFXValue;
import com.execue.core.common.bean.qdata.UserQueryReducedFormIndex;
import com.execue.core.common.type.OperatorType;
import com.execue.core.common.type.RFXEntityType;
import com.execue.core.common.type.RFXType;
import com.execue.core.common.type.RFXValueType;
import com.execue.core.common.type.RFXVariationSubType;
import com.execue.core.common.type.SearchType;
import com.execue.core.common.type.UniversalSearchType;
import com.execue.core.constants.ExecueConstants;

/**
 * @author John Mallavalli
 */
public class RFXServiceHelper {

   private Logger            logger    = Logger.getLogger(RFXServiceHelper.class);
   private ReducedFormHelper reducedFormHelper;
   private static final Long MAX_VALUE = Long.MAX_VALUE;

   public static Double getMaxMatchWeight (Collection<ReducedFormIndex> rfxList, Collection<RFXValue> rfxValueList) {
      Double maxMatchWeight = 0.0;
      if (CollectionUtils.isEmpty(rfxList) && CollectionUtils.isEmpty(rfxValueList)) {
         return maxMatchWeight;
      }
      // Add the rfx triples weight to the max weight
      for (ReducedFormIndex reducedFormIndex : rfxList) {
         maxMatchWeight += reducedFormIndex.getMaxWeight();
      }
      // Also add the rfx values triples to the max weight
      if (!CollectionUtils.isEmpty(rfxValueList)) {
         maxMatchWeight += 30 * rfxValueList.size();
      }
      return maxMatchWeight;
   }

   public Set<ReducedFormIndex> generateRFXFromInstanceBeds (Set<Long> knowledgeSearchBeds, RFXType rfxType, Long rfId,
            Long applicationId) {
      Set<ReducedFormIndex> rfxSet = new HashSet<ReducedFormIndex>(1);
      if (CollectionUtils.isEmpty(knowledgeSearchBeds)) {
         return rfxSet;
      }

      for (Long instanceBed : knowledgeSearchBeds) {
         // Add the component as subject and object
         ReducedFormIndex subjectRFX = getReducedFormIndexObject(rfxType);
         subjectRFX.setApplicationId(applicationId);
         subjectRFX.setRfId(rfId);
         subjectRFX.setOrder(1);
         subjectRFX.setRfxEntityType(RFXEntityType.BUSINESS_ENTITY_DEFINITION);
         subjectRFX.setSrcInstanceBEId(instanceBed);
         subjectRFX.setRfxVariationSubType(RFXVariationSubType.SUBJECTINSTANCE);
         subjectRFX.setMaxWeight(10);
         subjectRFX.setSrcRecWeight(8); // TODO: NK: Should later take it from configuration

         ReducedFormIndex objectRFX = getReducedFormIndexObject(rfxType);
         subjectRFX.setApplicationId(applicationId);
         objectRFX.setRfId(rfId);
         objectRFX.setOrder(1);
         objectRFX.setRfxEntityType(RFXEntityType.BUSINESS_ENTITY_DEFINITION);
         objectRFX.setDestInstanceBEId(instanceBed);
         objectRFX.setRfxVariationSubType(RFXVariationSubType.OBJECTINSTANCE);
         objectRFX.setMaxWeight(10);
         objectRFX.setDestRecWeight(8); // TODO: NK: Should later take it from configuration

         rfxSet.add(subjectRFX);
         rfxSet.add(objectRFX);
      }

      return rfxSet;
   }

   /**
    * @param semanticPossibility
    * @return the Set<RFXValue>
    */
   public Set<RFXValue> getRFXValueSet (SemanticPossibility semanticPossibility, Long queryId) {
      Graph reducedFormGraph = semanticPossibility.getReducedForm();
      List<IGraphPath> graphPaths = new ArrayList<IGraphPath>(1);
      if (!CollectionUtils.isEmpty(reducedFormGraph.getPaths())) {
         graphPaths.addAll(reducedFormGraph.getPaths());
      }
      Set<RFXValue> rfxValueSet = new HashSet<RFXValue>(1);
      for (IGraphPath path : graphPaths) {
         GraphPath graphPath = (GraphPath) path;
         int pathLength = graphPath.getPathLength();
         int start = 0;
         for (int i = 3; i <= pathLength;) {
            List<IGraphComponent> components = graphPath.getPartialPath(start, i);

            DomainRecognition source = (DomainRecognition) components.get(0);
            DomainRecognition relation = (DomainRecognition) components.get(1);
            DomainRecognition destination = (DomainRecognition) components.get(2);
            Set<Long> srcInstanceIds = new HashSet<Long>(1);
            Set<Long> destInstanceIds = new HashSet<Long>(1);
            if (source.getDefaultInstanceBedId() != null) {
               for (InstanceInformation instanceInformation : source.getInstanceInformations()) {
                  srcInstanceIds.add(instanceInformation.getInstanceBedId());
               }
            } else if (source.getNormalizedData() != null
                     && source.getNormalizedData().getNormalizedDataType() == NormalizedDataType.LIST_NORMALIZED_DATA) {
               ListNormalizedData normalizedData = (ListNormalizedData) source.getNormalizedData();
               List<NormalizedDataEntity> normalizedDataEntities = normalizedData.getNormalizedDataEntities();
               for (NormalizedDataEntity normalizedDataEntity : normalizedDataEntities) {
                  if (normalizedDataEntity.getValueBedId() != null) {
                     srcInstanceIds.add(normalizedDataEntity.getValueBedId());
                  }
               }
            }
            if (destination.getDefaultInstanceBedId() != null) {
               for (InstanceInformation instanceInformation : destination.getInstanceInformations()) {
                  destInstanceIds.add(instanceInformation.getInstanceBedId());
               }
            } else if (destination.getNormalizedData() != null
                     && destination.getNormalizedData().getNormalizedDataType() == NormalizedDataType.LIST_NORMALIZED_DATA) {
               ListNormalizedData normalizedData = (ListNormalizedData) destination.getNormalizedData();
               List<NormalizedDataEntity> normalizedDataEntities = normalizedData.getNormalizedDataEntities();
               for (NormalizedDataEntity normalizedDataEntity : normalizedDataEntities) {
                  if (normalizedDataEntity.getValueBedId() != null) {
                     destInstanceIds.add(normalizedDataEntity.getValueBedId());
                  }
               }
            }
            if (CollectionUtils.isEmpty(srcInstanceIds) && CollectionUtils.isEmpty(destInstanceIds)) {
               if (ReducedFormHelper.isValidValue(destination)) {
                  List<RFXValue> rfxValues = getRFXValueEntry(source, destination, relation, new HashSet<String>(1),
                           RFXValueType.RFX_VALUE_USER_QUERY, queryId, new HashMap<Long, Integer>(1));
                  if (!CollectionUtils.isEmpty(rfxValues)) {
                     rfxValueSet.addAll(rfxValues);
                  }
               }
            }
            start = start + 2;
            i = i + 2;
         }
      }
      return rfxValueSet;
   }

   public void populateRFXAndRFXValueFromSemanticPossibility (SemanticPossibility semanticPossibility,
            Set<ReducedFormIndex> rfxSet, Set<RFXValue> rfxValueSet, Long rfId, Long userQueryId,
            boolean skipUnconnectedRFXVariation, RFXType rfxType, RFXValueType rfxValueType,
            Set<String> conceptsConsideredForSingleInstance,
            Set<String> conceptsConsideredForSingleInstanceValueAssociation, Map<Long, Integer> conceptPriorityByBedId,
            Map<String, String> dependentConceptByRequiredConcept) {
      Graph reducedFormGraph = semanticPossibility.getReducedForm();
      List<IGraphPath> graphPaths = new ArrayList<IGraphPath>(1);
      if (!CollectionUtils.isEmpty(reducedFormGraph.getPaths())) {
         graphPaths.addAll(reducedFormGraph.getPaths());
      }

      // Sort the graph path by max token position
      Collections.sort(graphPaths, new Comparator<IGraphPath>() {

         public int compare (IGraphPath o1, IGraphPath o2) {
            return o1.getMaxTokenPosition() - o2.getMaxTokenPosition();
         }
      });

      int pathCount = 1;
      Application application = semanticPossibility.getApplication();
      Long applicationId = null;
      if (application != null) {
         applicationId = application.getId();
      }

      Set<Long> tripleBeds = new HashSet<Long>(1);
      List<DomainRecognition> visitedRecList = new ArrayList<DomainRecognition>(1);
      for (IGraphPath path : graphPaths) {
         GraphPath graphPath = (GraphPath) path;
         int pathLength = graphPath.getPathLength();
         int start = 0;
         for (int i = 3; i <= pathLength;) {
            List<IGraphComponent> components = graphPath.getPartialPath(start, i);

            DomainRecognition source = (DomainRecognition) components.get(0);
            DomainRecognition relation = (DomainRecognition) components.get(1);
            DomainRecognition destination = (DomainRecognition) components.get(2);
            boolean isMutuallyDependant = false;
            if (dependentConceptByRequiredConcept.containsKey(source.getConceptDisplayName())) {
               isMutuallyDependant = true;
            }

            visitedRecList.add(source);
            visitedRecList.add(relation);
            visitedRecList.add(destination);
            Map<Long, WeightInformation> sourceWeightInfoMap = new HashMap<Long, WeightInformation>(1);
            Map<Long, WeightInformation> destinationWeightInfoMap = new HashMap<Long, WeightInformation>(1);
            if (source.getDefaultInstanceBedId() != null) {
               for (InstanceInformation instanceInformation : source.getInstanceInformations()) {
                  sourceWeightInfoMap.put(instanceInformation.getInstanceBedId(), source.getWeightInformation());
               }
            } else if (source.getNormalizedData() != null
                     && source.getNormalizedData().getNormalizedDataType() == NormalizedDataType.LIST_NORMALIZED_DATA) {
               ListNormalizedData normalizedData = (ListNormalizedData) source.getNormalizedData();
               List<NormalizedDataEntity> normalizedDataEntities = normalizedData.getNormalizedDataEntities();
               // TODO: NK: Since single instance concept can be list, for now we just Get only the first model from the
               // list
               if (conceptsConsideredForSingleInstance.contains(destination.getConceptDisplayName())) {
                  NormalizedDataEntity normalizedDataEntity = normalizedDataEntities.get(0);
                  if (normalizedDataEntity.getValueBedId() != null) {
                     sourceWeightInfoMap.put(normalizedDataEntity.getValueBedId(), normalizedDataEntity
                              .getWeightInformation());
                  }
               } else {
                  for (NormalizedDataEntity normalizedDataEntity : normalizedDataEntities) {
                     if (normalizedDataEntity.getValueBedId() != null) {
                        sourceWeightInfoMap.put(normalizedDataEntity.getValueBedId(), normalizedDataEntity
                                 .getWeightInformation());
                     }
                  }
               }
            }

            if (destination.getDefaultInstanceBedId() != null) {
               for (InstanceInformation instanceInformation : destination.getInstanceInformations()) {
                  destinationWeightInfoMap.put(instanceInformation.getInstanceBedId(), destination
                           .getWeightInformation());
               }
            } else if (destination.getNormalizedData() != null
                     && destination.getNormalizedData().getNormalizedDataType() == NormalizedDataType.LIST_NORMALIZED_DATA) {
               ListNormalizedData normalizedData = (ListNormalizedData) destination.getNormalizedData();
               List<NormalizedDataEntity> normalizedDataEntities = normalizedData.getNormalizedDataEntities();

               // TODO: NK: Since single instance concept can be list, for now we just get only the first model from the
               // list
               if (conceptsConsideredForSingleInstance.contains(destination.getConceptDisplayName())) {
                  NormalizedDataEntity normalizedDataEntity = normalizedDataEntities.get(0);
                  if (normalizedDataEntity.getValueBedId() != null) {
                     destinationWeightInfoMap.put(normalizedDataEntity.getValueBedId(), normalizedDataEntity
                              .getWeightInformation());
                  }
               } else {
                  for (NormalizedDataEntity normalizedDataEntity : normalizedDataEntities) {
                     if (normalizedDataEntity.getValueBedId() != null) {
                        destinationWeightInfoMap.put(normalizedDataEntity.getValueBedId(), normalizedDataEntity
                                 .getWeightInformation());
                     }
                  }
               }
            }

            // Skip if the instance of the concept is already added and that concept is single instance concept
            if (!visitedRecList.contains(source) && tripleBeds.contains(source.getConceptBEDId())
                     && conceptsConsideredForSingleInstance.contains(source.getConceptDisplayName())
                     || !visitedRecList.contains(destination) && tripleBeds.contains(destination.getConceptBEDId())
                     && conceptsConsideredForSingleInstance.contains(destination.getConceptDisplayName())) {
               start = start + 2;
               i = i + 2;
               continue;
            }

            // Add to the triple beds set
            Set<Long> srcInstanceIds = sourceWeightInfoMap.keySet();
            Set<Long> destInstanceIds = destinationWeightInfoMap.keySet();
            tripleBeds.addAll(srcInstanceIds);
            tripleBeds.addAll(destInstanceIds);
            tripleBeds.add(source.getConceptBEDId());
            tripleBeds.add(destination.getConceptBEDId());
            ReducedFormIndex rfx;

            if (CollectionUtils.isEmpty(srcInstanceIds) && CollectionUtils.isEmpty(destInstanceIds)) {
               if (ReducedFormHelper.isValidValue(destination)) {
                  List<RFXValue> rfxValues = getRFXValueEntry(source, destination, relation,
                           conceptsConsideredForSingleInstanceValueAssociation, rfxValueType,
                           rfxValueType == RFXValueType.RFX_VALUE_CONTENT ? rfId : userQueryId, conceptPriorityByBedId);
                  if (!CollectionUtils.isEmpty(rfxValues)) {
                     rfxValueSet.addAll(rfxValues);
                  }
               } else {
                  Long sourceConceptBedId = source.getConceptBEDId();
                  if (sourceConceptBedId == null) {
                     sourceConceptBedId = source.getProfileBedId();
                  }
                  Long destinationConceptBedId = destination.getConceptBEDId();
                  if (destinationConceptBedId == null) {
                     destinationConceptBedId = destination.getProfileBedId();
                  }
                  rfx = getRFXEntry(sourceConceptBedId, null, relation.getRelationBEDId(), destinationConceptBedId,
                           null, null, rfxType, pathCount, applicationId, rfId, source.getWeightInformation(), relation
                                    .getWeightInformation(), destination.getWeightInformation(),
                           RFXEntityType.CONCEPT_TRIPLE, RFXVariationSubType.SUBJECTCONCEPT_RELATION_OBJECTCONCEPT,
                           isMutuallyDependant);
                  rfxSet.add(rfx);
               }
            } else if (CollectionUtils.isEmpty(srcInstanceIds) && !CollectionUtils.isEmpty(destInstanceIds)) {
               Long sourceConceptBedId = source.getConceptBEDId();
               if (sourceConceptBedId == null) {
                  sourceConceptBedId = source.getProfileBedId();
               }
               for (Long instanceBeId : destInstanceIds) {
                  rfx = getRFXEntry(sourceConceptBedId, null, relation.getRelationBEDId(), destination
                           .getConceptBEDId(), instanceBeId, null, rfxType, pathCount, applicationId, rfId, source
                           .getWeightInformation(), relation.getWeightInformation(), destinationWeightInfoMap
                           .get(instanceBeId), RFXEntityType.CONCEPT_INSTANCE_TRIPLE,
                           RFXVariationSubType.SUBJECTCONCEPT_RELATION_OBJECTINSTANCE, isMutuallyDependant);
                  rfxSet.add(rfx);
               }
            } else if (!CollectionUtils.isEmpty(srcInstanceIds) && CollectionUtils.isEmpty(destInstanceIds)) {
               Long destinationConceptBedId = destination.getConceptBEDId();
               if (destinationConceptBedId == null) {
                  destinationConceptBedId = destination.getProfileBedId();
               }
               for (Long instanceBeId : srcInstanceIds) {
                  rfx = getRFXEntry(source.getConceptBEDId(), instanceBeId, relation.getRelationBEDId(),
                           destinationConceptBedId, null, null, rfxType, pathCount, applicationId, rfId,
                           sourceWeightInfoMap.get(instanceBeId), relation.getWeightInformation(), destination
                                    .getWeightInformation(), RFXEntityType.INSTANCE_CONCEPT_TRIPLE,
                           RFXVariationSubType.SUBJECTINSTANCE_RELATION_OBJECTCONCEPT, isMutuallyDependant);
                  rfxSet.add(rfx);
               }
            } else {
               for (Long srcInstanceBeId : srcInstanceIds) {
                  for (Long destInstanceBeId : destInstanceIds) {
                     rfx = getRFXEntry(source.getConceptBEDId(), srcInstanceBeId, relation.getRelationBEDId(),
                              destination.getConceptBEDId(), destInstanceBeId, null, rfxType, pathCount, applicationId,
                              rfId, sourceWeightInfoMap.get(srcInstanceBeId), relation.getWeightInformation(),
                              destinationWeightInfoMap.get(destInstanceBeId), RFXEntityType.INSTANCE_TRIPLE,
                              RFXVariationSubType.SUBJECTINSTANCE_RELATION_OBJECTINSTANCE, isMutuallyDependant);
                     rfxSet.add(rfx);
                  }
               }
            }
            start = start + 2;
            i = i + 2;
         }
         pathCount++;
      }
      Collection<IGraphComponent> rootVertices = semanticPossibility.getRootVertices();
      for (IGraphComponent rv : rootVertices) {
         DomainRecognition entity = (DomainRecognition) rv;
         if (tripleBeds.contains(entity.getConceptBEDId())
                  && conceptsConsideredForSingleInstance.contains(entity.getConceptDisplayName())) {
            continue;
         }
         if (tripleBeds.contains(entity.getEntityBeId())) {
            continue;
         }

         if (entity.getRelationBEDId() != null) {
            // Add the component as relation bed
            ReducedFormIndex relationRFX = getRFXEntry(null, null, entity.getRelationBEDId(), null, null, null,
                     rfxType, 1, applicationId, rfId, null, entity.getWeightInformation(), null,
                     RFXEntityType.BUSINESS_ENTITY_DEFINITION, RFXVariationSubType.RELATION, false);
            rfxSet.add(relationRFX);
         } else if (entity.getNormalizedData() != null
                  && entity.getNormalizedData().getNormalizedDataType() == NormalizedDataType.LIST_NORMALIZED_DATA) {
            ListNormalizedData normalizedData = (ListNormalizedData) entity.getNormalizedData();
            List<NormalizedDataEntity> normalizedDataEntities = normalizedData.getNormalizedDataEntities();

            if (conceptsConsideredForSingleInstance.contains(entity.getConceptDisplayName())) {
               // NK: Assumption here is Model should always come connected, so skipping it here as its coming
               // unconnected that means is not a valid model attached to the given reduced form make
               // continue;
               NormalizedDataEntity normalizedDataEntity = normalizedDataEntities.get(0);
               if (normalizedDataEntity.getValueBedId() != null) {
                  ReducedFormIndex subjectRFX = getRFXEntry(entity.getConceptBEDId(), normalizedDataEntity
                           .getValueBedId(), null, null, null, null, rfxType, 1, applicationId, rfId,
                           normalizedDataEntity.getWeightInformation(), null, null,
                           RFXEntityType.BUSINESS_ENTITY_DEFINITION, RFXVariationSubType.SUBJECTINSTANCE, false);

                  ReducedFormIndex objectRFX = getRFXEntry(null, null, null, entity.getConceptBEDId(),
                           normalizedDataEntity.getValueBedId(), null, rfxType, 1, applicationId, rfId, null, null,
                           normalizedDataEntity.getWeightInformation(), RFXEntityType.BUSINESS_ENTITY_DEFINITION,
                           RFXVariationSubType.OBJECTINSTANCE, false);
                  rfxSet.add(subjectRFX);
                  rfxSet.add(objectRFX);
               }
            } else {
               for (NormalizedDataEntity normalizedDataEntity : normalizedDataEntities) {
                  if (normalizedDataEntity.getValueBedId() != null) {
                     // Add the component as subject and object
                     ReducedFormIndex subjectRFX = getRFXEntry(entity.getConceptBEDId(), normalizedDataEntity
                              .getValueBedId(), null, null, null, null, rfxType, 1, applicationId, rfId,
                              normalizedDataEntity.getWeightInformation(), null, null,
                              RFXEntityType.BUSINESS_ENTITY_DEFINITION, RFXVariationSubType.SUBJECTINSTANCE, false);

                     ReducedFormIndex objectRFX = getRFXEntry(null, null, null, entity.getConceptBEDId(),
                              normalizedDataEntity.getValueBedId(), null, rfxType, 1, applicationId, rfId, null, null,
                              normalizedDataEntity.getWeightInformation(), RFXEntityType.BUSINESS_ENTITY_DEFINITION,
                              RFXVariationSubType.OBJECTINSTANCE, false);
                     rfxSet.add(subjectRFX);
                     rfxSet.add(objectRFX);
                  }
               }
            }
         } else {
            if (entity.getDefaultInstanceBedId() != null) {
               tripleBeds.add(entity.getDefaultInstanceBedId());
            } else {
               tripleBeds.add(entity.getEntityBeId());
            }
            tripleBeds.add(entity.getConceptBEDId());
            Long conceptBedId = entity.getConceptBEDId();
            if (conceptBedId == null) {
               conceptBedId = entity.getProfileBedId();
            }
            for (InstanceInformation instanceInformation : entity.getInstanceInformations()) {
               // Add the component as subject and object
               ReducedFormIndex subjectRFX = getRFXEntry(conceptBedId, instanceInformation.getInstanceBedId(), null,
                        null, null, null, rfxType, 1, applicationId, rfId, entity.getWeightInformation(), null, null,
                        RFXEntityType.BUSINESS_ENTITY_DEFINITION,
                        instanceInformation.getInstanceBedId() != null ? RFXVariationSubType.SUBJECTINSTANCE
                                 : RFXVariationSubType.SUBJECTCONCEPT, false);

               ReducedFormIndex objectRFX = getRFXEntry(null, null, null, conceptBedId, instanceInformation
                        .getInstanceBedId(), null, rfxType, 1, applicationId, rfId, null, null, entity
                        .getWeightInformation(), RFXEntityType.BUSINESS_ENTITY_DEFINITION, instanceInformation
                        .getInstanceBedId() != null ? RFXVariationSubType.OBJECTINSTANCE
                        : RFXVariationSubType.OBJECTCONCEPT, false);
               rfxSet.add(subjectRFX);
               rfxSet.add(objectRFX);
            }
         }
      }
   }

   /**
    * @param source
    * @param source
    * @param destination
    * @param relation
    * @param destination
    * @param relation
    * @param conceptsConsideredForSingleInstanceValueAssociation
    * @param conceptPriorityMapById
    */
   public List<RFXValue> getRFXValueEntry (DomainRecognition source, DomainRecognition destination,
            DomainRecognition relation, Set<String> conceptsConsideredForSingleInstanceValueAssociation,
            RFXValueType rfxValueType, Long contextId, Map<Long, Integer> conceptPriorityMapById) {
      List<RFXValue> rfxValues = new ArrayList<RFXValue>(1);
      INormalizedData normalizedData = destination.getNormalizedData();
      if (normalizedData.getNormalizedDataType() == NormalizedDataType.RANGE_NORMALIZED_DATA) {
         RFXValue fromRfxValue = getRFXValueByType(rfxValueType, contextId);
         fromRfxValue.setSrcConceptBedId(source.getConceptBEDId());
         if (fromRfxValue.getSrcConceptBedId() == null && source.getProfileBedId() != null) {
            fromRfxValue.setSrcConceptBedId(source.getProfileBedId());
         }
         fromRfxValue.setRelationBedId(relation.getRelationBEDId());
         fromRfxValue.setDestConceptBedId(destination.getConceptBEDId());
         RangeNormalizedData rangeNormalizedData = (RangeNormalizedData) normalizedData;
         Double startValue = getReducedFormHelper().getValueFromRangeNormalizedData(
                  rangeNormalizedData.getStart().getNormalizedData());
         if (startValue == null) {
            return null;
         }
         fromRfxValue.setOperator(OperatorType.GREATER_THAN_EQUALS.getValue());
         fromRfxValue.setValue(startValue);

         RFXValue toRfxValue = getRFXValueByType(rfxValueType, contextId);
         toRfxValue.setSrcConceptBedId(source.getConceptBEDId());
         if (toRfxValue.getSrcConceptBedId() == null && source.getProfileBedId() != null) {
            toRfxValue.setSrcConceptBedId(source.getProfileBedId());
         }
         toRfxValue.setRelationBedId(relation.getRelationBEDId());
         toRfxValue.setDestConceptBedId(destination.getConceptBEDId());
         Double endValue = getReducedFormHelper().getValueFromRangeNormalizedData(
                  rangeNormalizedData.getEnd().getNormalizedData());
         if (endValue == null) {
            return null;
         }
         toRfxValue.setOperator(OperatorType.LESS_THAN_EQUALS.getValue());
         toRfxValue.setValue(endValue);

         // Finally, add the from to rfx value to the list
         rfxValues.add(fromRfxValue);
         rfxValues.add(toRfxValue);
      } else if (normalizedData.getNormalizedDataType() == NormalizedDataType.LIST_NORMALIZED_DATA) {
         ListNormalizedData listNormalizedData = (ListNormalizedData) normalizedData;
         List<NormalizedDataEntity> normalizedDataEntities = new ArrayList<NormalizedDataEntity>(1);
         if (conceptsConsideredForSingleInstanceValueAssociation.contains(source.getConceptDisplayName())) {
            // Get only the best normalized data entity from the list
            NormalizedDataEntity bestNormalizedEntity = ReducedFormHelper.chooseBestNormalizedEntity(listNormalizedData
                     .getNormalizedDataEntities());
            if (bestNormalizedEntity != null) {
               normalizedDataEntities.add(bestNormalizedEntity);
            }
         } else {
            normalizedDataEntities = listNormalizedData.getNormalizedDataEntities();
         }
         for (NormalizedDataEntity normalizedDataEntity : normalizedDataEntities) {
            RFXValue rfxValue = getRFXValueByType(rfxValueType, contextId);
            rfxValue.setSrcConceptBedId(source.getConceptBEDId());
            if (rfxValue.getSrcConceptBedId() == null && source.getProfileBedId() != null) {
               rfxValue.setSrcConceptBedId(source.getProfileBedId());
            }
            rfxValue.setRelationBedId(relation.getRelationBEDId());
            rfxValue.setDestConceptBedId(destination.getConceptBEDId());
            normalizedData = normalizedDataEntity.getNormalizedData();
            Object object[] = getReducedFormHelper().getOperatorAndValue(normalizedData);
            if (object[0] == null || object[1] == null) {
               return null;
            }
            String operator = (String) object[0];
            Double value = (Double) object[1];
            rfxValue.setOperator(operator);
            rfxValue.setValue(value);
            rfxValues.add(rfxValue);
         }
      } else {
         RFXValue rfxValue = getRFXValueByType(rfxValueType, contextId);
         rfxValue.setSrcConceptBedId(source.getConceptBEDId());
         if (rfxValue.getSrcConceptBedId() == null && source.getProfileBedId() != null) {
            rfxValue.setSrcConceptBedId(source.getProfileBedId());
         }
         rfxValue.setRelationBedId(relation.getRelationBEDId());
         rfxValue.setDestConceptBedId(destination.getConceptBEDId());
         Object object[] = getReducedFormHelper().getOperatorAndValue(normalizedData);
         if (object[0] == null || object[1] == null) {
            return null;
         }
         String operator = (String) object[0];
         Double value = (Double) object[1];

         if (rfxValueType == RFXValueType.RFX_VALUE_USER_QUERY) {
            Integer priorityOrder = conceptPriorityMapById.get(source.getConceptBEDId());
            if (priorityOrder != null) {
               ((UserQueryRFXValue) rfxValue).setTripleIdentifier(priorityOrder);
            }
            populateStartAndEndValue((UserQueryRFXValue) rfxValue, operator, value);
         } else {
            rfxValue.setOperator(operator);
            rfxValue.setValue(value);
         }
         rfxValues.add(rfxValue);
      }
      return rfxValues;
   }

   private void populateStartAndEndValue (UserQueryRFXValue rfxValue, String operator, Double value) {
      rfxValue.setOperator(operator);
      if (operator.equals(OperatorType.EQUALS.getValue())) {
         rfxValue.setStartValue(value);
         rfxValue.setEndValue(value);
      }
      if (operator.equals(OperatorType.LESS_THAN.getValue())) {
         rfxValue.setStartValue(0d);
         rfxValue.setEndValue(value - 1);
      }
      if (operator.equals(OperatorType.GREATER_THAN.getValue())) {
         rfxValue.setStartValue(value + 1);
         rfxValue.setEndValue(MAX_VALUE.doubleValue());
      }
      if (operator.equals(OperatorType.LESS_THAN_EQUALS.getValue())) {
         rfxValue.setStartValue(0d);
         rfxValue.setEndValue(value);
      }
      if (operator.equals(OperatorType.GREATER_THAN_EQUALS.getValue())) {
         rfxValue.setStartValue(value);
         rfxValue.setEndValue(MAX_VALUE.doubleValue());
      }
   }

   public RFXValue getRFXValueByType (RFXValueType rfxValueType, Long contextId) {
      if (rfxValueType == RFXValueType.RFX_VALUE_CONTENT) {
         ContentRFXValue contentRFXValue = new ContentRFXValue();
         contentRFXValue.setRfxId(contextId);
         return contentRFXValue;
      }
      if (rfxValueType == RFXValueType.RFX_VALUE_USER_QUERY) {
         UserQueryRFXValue userQueryRFXValue = new UserQueryRFXValue();
         userQueryRFXValue.setQueryId(contextId);
         return userQueryRFXValue;
      }
      return new ContentRFXValue();
   }

   /**
    * @param srcConceptBedId
    * @param srcInstanceBedId
    * @param relationBedId
    * @param destConceptBedId
    * @param destInstanceBedId
    * @param destValue
    * @param rfxType
    * @param rfxOrder
    * @param applicationId
    * @param rfId
    * @param source
    * @param relation
    * @param destination
    * @param rfxEntityType
    * @param rfxVariationSubType
    * @param isMutuallyDependant
    *           TODO
    * @return
    */
   private ReducedFormIndex getRFXEntry (Long srcConceptBedId, Long srcInstanceBedId, Long relationBedId,
            Long destConceptBedId, Long destInstanceBedId, String destValue, RFXType rfxType, int rfxOrder,
            Long applicationId, Long rfId, WeightInformation source, WeightInformation relation,
            WeightInformation destination, RFXEntityType rfxEntityType, RFXVariationSubType rfxVariationSubType,
            boolean isMutuallyDependant) {
      ReducedFormIndex rfx = getReducedFormIndexObject(rfxType);
      rfx.setApplicationId(applicationId);
      rfx.setRfId(rfId);
      rfx.setMutuallyDependant(isMutuallyDependant);
      double maxWeight = 0d;
      if (source != null) {
         rfx.setSrcRecWeight(source.getRecognitionQuality() * 10);
         maxWeight += 10;
      }
      if (destination != null) {
         rfx.setDestRecWeight(destination.getRecognitionQuality() * 10);
         maxWeight += 10;
      }
      if (relation != null) {
         rfx.setRelRecWeight(relation.getRecognitionQuality() * 10);
         maxWeight += 10;
      }
      if (srcInstanceBedId != null) {
         rfx.setSrcInstanceBEId(srcInstanceBedId);
      }
      if (destInstanceBedId != null) {
         rfx.setDestInstanceBEId(destInstanceBedId);
      }
      rfx.setMaxWeight(maxWeight);
      rfx.setSrcConceptBEId(srcConceptBedId);
      rfx.setDestConceptBEId(destConceptBedId);
      rfx.setRelationBEId(relationBedId);
      if (destValue != null) {
         rfx.setValue(destValue);
      }
      rfx.setRfxEntityType(rfxEntityType);
      rfx.setRfxVariationSubType(rfxVariationSubType);
      rfx.setOrder(rfxOrder);
      return rfx;
   }

   /**
    * This method returns the implementation of abstract ReducedFormIndex based on the given rfxType
    * 
    * @param rfxType
    * @return the ReducedFormIndex
    */
   public static ReducedFormIndex getReducedFormIndexObject (RFXType rfxType) {
      if (RFXType.RFX_CONTENT == rfxType) {
         return new ContentReducedFormIndex();
      }

      // NK: For all other case, we create the usery query rfx ie.
      // if (RFXType.RFX_QUERY_CACHE == rfxType || RFXType.RFX_ENTITY_SEARCH == rfxType
      // || RFXType.RFX_KNOWLEDGE_SEARCH == rfxType) {
      UserQueryReducedFormIndex rfx = new UserQueryReducedFormIndex();
      rfx.setRfxType(rfxType);
      // }
      return rfx;
   }

   public Set<RIUserQuery> getRIUserQueryEntries (ReducedFormIndex reducedFormIndex,
            Map<RFXVariationSubType, Double> rankingWeightsMap, Map<Long, Integer> conceptPriorityMap,
            SearchType searchType, Long userQueryId, Integer entityCount, boolean skipDerivedUserQueryVariation) {
      Set<RIUserQuery> userQueryEntries = new HashSet<RIUserQuery>(1);
      if (rankingWeightsMap.isEmpty()) {
         return userQueryEntries;
      }
      RFXVariationSubType originalSubType = reducedFormIndex.getRfxVariationSubType();
      for (Entry<RFXVariationSubType, Double> entry : rankingWeightsMap.entrySet()) {
         RFXVariationSubType variationSubType = entry.getKey();
         // TODO: NK: Check with AP, if its fine to skip the concept user query variation
         // if the source/object is instance for knowledge search
         if (searchType == SearchType.KNOWLEDGE_SEARCH
                  && (reducedFormIndex.getRfxVariationSubType() == RFXVariationSubType.SUBJECTINSTANCE
                           && variationSubType == RFXVariationSubType.SUBJECTCONCEPT || reducedFormIndex
                           .getRfxVariationSubType() == RFXVariationSubType.OBJECTINSTANCE
                           && variationSubType == RFXVariationSubType.OBJECTCONCEPT)) {
            continue;
         }

         // TODO: NK: Ignore the partial triple variation for now
         if (isPartialVariation(variationSubType)) {
            continue;
         }
         int derived = originalSubType == variationSubType ? 0 : 1;
         if (skipDerivedUserQueryVariation && derived == 1) {
            // SKIP any derived variation of IRI rfx, If they are mutually dependants
            if (originalSubType == RFXVariationSubType.SUBJECTINSTANCE_RELATION_OBJECTINSTANCE
                     && reducedFormIndex.isMutuallyDependant()) {
               continue;
            }
            // Skip any derived CRC variation
            // Skip the derived variation except the subject and object instance variation
            if (variationSubType == RFXVariationSubType.SUBJECTCONCEPT_RELATION_OBJECTCONCEPT
                     || variationSubType != RFXVariationSubType.SUBJECTINSTANCE
                     && variationSubType != RFXVariationSubType.OBJECTINSTANCE) {
               continue;
            }
         }
         RIUserQuery riUserQuery = new RIUserQuery();
         riUserQuery.setApplicationId(reducedFormIndex.getApplicationId());
         riUserQuery.setQueryId(userQueryId);
         riUserQuery.setEntityCount(entityCount.doubleValue());
         riUserQuery.setVariationWeight(entry.getValue() / 100);
         riUserQuery.setVariationSubType(variationSubType);
         riUserQuery.setOriginalSubType(originalSubType);
         riUserQuery.setDerived(derived);
         populateRIUserQueryBeds(riUserQuery, reducedFormIndex, variationSubType, conceptPriorityMap);
         userQueryEntries.add(riUserQuery);
      }
      return userQueryEntries;
   }

   public static boolean isPartialVariation (RFXVariationSubType variationSubType) {
      return variationSubType == RFXVariationSubType.RELATION_OBJECTCONCEPT
               || variationSubType == RFXVariationSubType.RELATION_OBJECTINSTANCE
               || variationSubType == RFXVariationSubType.SUBJECTCONCEPT_OBJECTCONCEPT
               || variationSubType == RFXVariationSubType.SUBJECTCONCEPT_OBJECTINSTANCE
               || variationSubType == RFXVariationSubType.SUBJECTCONCEPT_RELATION
               || variationSubType == RFXVariationSubType.SUBJECTINSTANCE_OBJECTCONCEPT
               || variationSubType == RFXVariationSubType.SUBJECTINSTANCE_OBJECTINSTANCE
               || variationSubType == RFXVariationSubType.SUBJECTINSTANCE_RELATION;
   }

   public Set<RIUniversalSearch> getRIUniversalSearchEntries (ReducedFormIndex reducedFormIndex,
            Map<RFXVariationSubType, Double> rankingWeightsMap, UniversalSearchType searchType, Long contextId,
            Double entityCount, Date contentDate) {
      Set<RIUniversalSearch> riUniversalSearchEntries = new HashSet<RIUniversalSearch>(1);
      if (rankingWeightsMap.isEmpty()) {
         return riUniversalSearchEntries;
      }
      RFXVariationSubType originalSubType = reducedFormIndex.getRfxVariationSubType();
      for (Entry<RFXVariationSubType, Double> entry : rankingWeightsMap.entrySet()) {
         RFXVariationSubType variationSubType = entry.getKey();
         // TODO: NK: Skipping the partial variation sub types for now
         if (isPartialVariation(variationSubType)) {
            continue;
         }
         int derived = originalSubType == variationSubType ? 0 : 1;
         RIUniversalSearch riUniversalSearch = getRIUniversalSearchObject(searchType, contextId, reducedFormIndex
                  .getRfId(), contentDate);
         riUniversalSearch.setApplicationId(reducedFormIndex.getApplicationId());
         riUniversalSearch.setVariationSubType(variationSubType);
         riUniversalSearch.setOriginalSubType(originalSubType);
         riUniversalSearch.setDerived(derived);
         riUniversalSearch.setVariationWeight(entry.getValue() / 100);
         riUniversalSearch.setEntityCount(entityCount);
         populateRIUnStructuredIndexBeds(riUniversalSearch, reducedFormIndex, variationSubType);
         riUniversalSearchEntries.add(riUniversalSearch);
      }
      return riUniversalSearchEntries;
   }

   private RIUniversalSearch getRIUniversalSearchObject (UniversalSearchType searchType, Long contextId, Long rfId,
            Date contentDate) {
      RIUniversalSearch riUniversalSearch = null;
      if (UniversalSearchType.UNSTRUCTURED_SEARCH == searchType) {
         riUniversalSearch = new RIUnStructuredIndex();
         ((RIUnStructuredIndex) riUniversalSearch).setUdxId(contextId);
         ((RIUnStructuredIndex) riUniversalSearch).setContentDate(contentDate);
         ((RIUnStructuredIndex) riUniversalSearch).setRfId(rfId);
      } else if (UniversalSearchType.QUERY_CACHE_SEARCH == searchType) {
         riUniversalSearch = new RIQueryCache();
         ((RIQueryCache) riUniversalSearch).setUserQueryId(contextId);
      }
      return riUniversalSearch;
   }

   public static void populateRIUserQueryBeds (RIUserQuery riUserQuery, ReducedFormIndex reducedFormIndex,
            RFXVariationSubType variationSubType, Map<Long, Integer> conceptPriorityMap) {

      switch (variationSubType.getValue()) {
         case ExecueConstants.SUBJECTINSTANCE:
            if (RFXVariationSubType.OBJECTINSTANCE == reducedFormIndex.getRfxVariationSubType()) {
               riUserQuery.setBeId1(reducedFormIndex.getDestInstanceBEId());
               riUserQuery.setVariationType(2);
               riUserQuery.setRecWeight(reducedFormIndex.getDestRecWeight());
            } else {
               riUserQuery.setBeId1(reducedFormIndex.getSrcInstanceBEId());
               riUserQuery.setVariationType(1);
               riUserQuery.setRecWeight(reducedFormIndex.getSrcRecWeight());
            }
            break;
         case ExecueConstants.SUBJECTCONCEPT:
            if (RFXVariationSubType.OBJECTCONCEPT == reducedFormIndex.getRfxVariationSubType()) {
               riUserQuery.setBeId1(reducedFormIndex.getDestConceptBEId());
               riUserQuery.setVariationType(2);
               riUserQuery.setRecWeight(reducedFormIndex.getDestRecWeight());
            } else {
               riUserQuery.setBeId1(reducedFormIndex.getSrcConceptBEId());
               riUserQuery.setVariationType(1);
               riUserQuery.setRecWeight(reducedFormIndex.getSrcRecWeight());
            }
            break;
         case ExecueConstants.OBJECTINSTANCE:
            if (RFXVariationSubType.SUBJECTINSTANCE == reducedFormIndex.getRfxVariationSubType()) {
               riUserQuery.setBeId1(reducedFormIndex.getSrcInstanceBEId());
               riUserQuery.setVariationType(1);
               riUserQuery.setRecWeight(reducedFormIndex.getSrcRecWeight());
            } else {
               riUserQuery.setBeId1(reducedFormIndex.getDestInstanceBEId());
               riUserQuery.setVariationType(2);
               riUserQuery.setRecWeight(reducedFormIndex.getDestRecWeight());
            }
            break;
         case ExecueConstants.OBJECTCONCEPT:
            if (RFXVariationSubType.SUBJECTCONCEPT == reducedFormIndex.getRfxVariationSubType()) {
               riUserQuery.setBeId1(reducedFormIndex.getSrcConceptBEId());
               riUserQuery.setVariationType(1);
               riUserQuery.setRecWeight(reducedFormIndex.getSrcRecWeight());
            } else {
               riUserQuery.setBeId1(reducedFormIndex.getDestConceptBEId());
               riUserQuery.setVariationType(2);
               riUserQuery.setRecWeight(reducedFormIndex.getDestRecWeight());
            }
            break;
         case ExecueConstants.RELATION:
            riUserQuery.setBeId1(reducedFormIndex.getRelationBEId());
            riUserQuery.setVariationType(3);
            riUserQuery.setRecWeight(reducedFormIndex.getRelRecWeight());
            break;
         case ExecueConstants.SUBJECTINSTANCE_RELATION:
            riUserQuery.setBeId1(reducedFormIndex.getSrcInstanceBEId());
            riUserQuery.setBeId2(reducedFormIndex.getRelationBEId());
            riUserQuery.setVariationType(4);
            riUserQuery.setRecWeight(reducedFormIndex.getSrcRecWeight() + reducedFormIndex.getRelRecWeight());
            break;
         case ExecueConstants.SUBJECTCONCEPT_RELATION:
            riUserQuery.setBeId1(reducedFormIndex.getSrcConceptBEId());
            riUserQuery.setBeId2(reducedFormIndex.getRelationBEId());
            riUserQuery.setVariationType(4);
            riUserQuery.setRecWeight(reducedFormIndex.getSrcRecWeight() + reducedFormIndex.getRelRecWeight());
            break;
         case ExecueConstants.RELATION_OBJECTINSTANCE:
            riUserQuery.setBeId1(reducedFormIndex.getRelationBEId());
            riUserQuery.setBeId2(reducedFormIndex.getDestInstanceBEId());
            riUserQuery.setVariationType(5);
            riUserQuery.setRecWeight(reducedFormIndex.getRelRecWeight() + reducedFormIndex.getDestRecWeight());
            break;
         case ExecueConstants.SUBJECTCONCEPT_OBJECTINSTANCE:
            riUserQuery.setBeId1(reducedFormIndex.getRelationBEId());
            riUserQuery.setBeId2(reducedFormIndex.getDestConceptBEId());
            riUserQuery.setVariationType(5);
            riUserQuery.setRecWeight(reducedFormIndex.getSrcRecWeight() + reducedFormIndex.getDestRecWeight());
            break;
         case ExecueConstants.SUBJECTINSTANCE_OBJECTINSTANCE:
            riUserQuery.setBeId1(reducedFormIndex.getSrcInstanceBEId());
            riUserQuery.setBeId2(reducedFormIndex.getDestInstanceBEId());
            riUserQuery.setVariationType(6);
            riUserQuery.setRecWeight(reducedFormIndex.getSrcRecWeight() + reducedFormIndex.getDestRecWeight());
            break;
         case ExecueConstants.SUBJECTINSTANCE_OBJECTCONCEPT:
            riUserQuery.setBeId1(reducedFormIndex.getSrcConceptBEId());
            riUserQuery.setBeId2(reducedFormIndex.getDestInstanceBEId());
            riUserQuery.setVariationType(6);
            riUserQuery.setRecWeight(reducedFormIndex.getSrcRecWeight() + reducedFormIndex.getDestRecWeight());
            break;
         case ExecueConstants.RELATION_OBJECTCONCEPT:
            riUserQuery.setBeId1(reducedFormIndex.getSrcInstanceBEId());
            riUserQuery.setBeId2(reducedFormIndex.getDestConceptBEId());
            riUserQuery.setVariationType(6);
            riUserQuery.setRecWeight(reducedFormIndex.getRelRecWeight() + reducedFormIndex.getDestRecWeight());
            break;
         case ExecueConstants.SUBJECTCONCEPT_OBJECTCONCEPT:
            riUserQuery.setBeId1(reducedFormIndex.getSrcConceptBEId());
            riUserQuery.setBeId2(reducedFormIndex.getDestConceptBEId());
            riUserQuery.setVariationType(6);
            riUserQuery.setRecWeight(reducedFormIndex.getSrcRecWeight() + reducedFormIndex.getDestRecWeight());
            break;
         case ExecueConstants.SUBJECTINSTANCE_RELATION_OBJECTINSTANCE:
            riUserQuery.setBeId1(reducedFormIndex.getSrcInstanceBEId());
            riUserQuery.setBeId2(reducedFormIndex.getRelationBEId());
            riUserQuery.setBeId3(reducedFormIndex.getDestInstanceBEId());
            riUserQuery.setVariationType(7);
            riUserQuery.setRecWeight(reducedFormIndex.getSrcRecWeight() + reducedFormIndex.getRelRecWeight()
                     + reducedFormIndex.getDestRecWeight());
            Integer priority = conceptPriorityMap.get(reducedFormIndex.getDestConceptBEId());
            if (priority != null) {
               riUserQuery.setTripleIdentifier(priority);
            }
            break;
         case ExecueConstants.SUBJECTINSTANCE_RELATION_OBJECTCONCEPT:
            riUserQuery.setBeId1(reducedFormIndex.getSrcInstanceBEId());
            riUserQuery.setBeId2(reducedFormIndex.getRelationBEId());
            riUserQuery.setBeId3(reducedFormIndex.getDestConceptBEId());
            riUserQuery.setVariationType(7);
            riUserQuery.setRecWeight(reducedFormIndex.getSrcRecWeight() + reducedFormIndex.getRelRecWeight()
                     + reducedFormIndex.getDestRecWeight());
            break;
         case ExecueConstants.SUBJECTCONCEPT_RELATION_OBJECTINSTANCE:
            riUserQuery.setBeId1(reducedFormIndex.getSrcConceptBEId());
            riUserQuery.setBeId2(reducedFormIndex.getRelationBEId());
            riUserQuery.setBeId3(reducedFormIndex.getDestInstanceBEId());
            riUserQuery.setVariationType(7);
            riUserQuery.setRecWeight(reducedFormIndex.getSrcRecWeight() + reducedFormIndex.getRelRecWeight()
                     + reducedFormIndex.getDestRecWeight());
            priority = conceptPriorityMap.get(reducedFormIndex.getDestConceptBEId());
            if (priority != null) {
               riUserQuery.setTripleIdentifier(priority);
            }
            break;
         case ExecueConstants.SUBJECTCONCEPT_RELATION_OBJECTCONCEPT:
            riUserQuery.setBeId1(reducedFormIndex.getSrcConceptBEId());
            riUserQuery.setBeId2(reducedFormIndex.getRelationBEId());
            riUserQuery.setBeId3(reducedFormIndex.getDestConceptBEId());
            riUserQuery.setVariationType(7);
            riUserQuery.setRecWeight(reducedFormIndex.getSrcRecWeight() + reducedFormIndex.getRelRecWeight()
                     + reducedFormIndex.getDestRecWeight());
            break;
         default:
            // Should never come in default case
      }
   }

   public static void populateRIUnStructuredIndexBeds (RIUniversalSearch riUnStructuredIndex,
            ReducedFormIndex reducedFormIndex, RFXVariationSubType variationSubType) {
      switch (variationSubType.getValue()) {
         case ExecueConstants.SUBJECTINSTANCE:
            riUnStructuredIndex.setBeId1(reducedFormIndex.getSrcInstanceBEId());
            riUnStructuredIndex.setMaxWeight(10.0);
            riUnStructuredIndex.setRecWeight(reducedFormIndex.getSrcRecWeight());
            riUnStructuredIndex.setSearchType(3);
            riUnStructuredIndex.setVariationType(1);
            break;
         case ExecueConstants.SUBJECTCONCEPT:
            riUnStructuredIndex.setBeId1(reducedFormIndex.getSrcConceptBEId());
            riUnStructuredIndex.setMaxWeight(10.0);
            riUnStructuredIndex.setRecWeight(reducedFormIndex.getSrcRecWeight());
            riUnStructuredIndex.setSearchType(3);
            riUnStructuredIndex.setVariationType(1);
            break;
         case ExecueConstants.OBJECTINSTANCE:
            riUnStructuredIndex.setBeId1(reducedFormIndex.getDestInstanceBEId());
            riUnStructuredIndex.setMaxWeight(10.0);
            riUnStructuredIndex.setRecWeight(reducedFormIndex.getDestRecWeight());
            riUnStructuredIndex.setSearchType(3);
            riUnStructuredIndex.setVariationType(2);
            break;
         case ExecueConstants.OBJECTCONCEPT:
            riUnStructuredIndex.setBeId1(reducedFormIndex.getDestConceptBEId());
            riUnStructuredIndex.setMaxWeight(10.0);
            riUnStructuredIndex.setRecWeight(reducedFormIndex.getDestRecWeight());
            riUnStructuredIndex.setSearchType(3);
            riUnStructuredIndex.setVariationType(2);
            break;
         case ExecueConstants.RELATION:
            riUnStructuredIndex.setBeId1(reducedFormIndex.getRelationBEId());
            riUnStructuredIndex.setMaxWeight(10.0);
            riUnStructuredIndex.setRecWeight(reducedFormIndex.getRelRecWeight());
            riUnStructuredIndex.setSearchType(3);
            riUnStructuredIndex.setVariationType(3);
            break;
         case ExecueConstants.SUBJECTINSTANCE_RELATION:
            riUnStructuredIndex.setBeId1(reducedFormIndex.getSrcInstanceBEId());
            riUnStructuredIndex.setBeId2(reducedFormIndex.getRelationBEId());
            riUnStructuredIndex.setMaxWeight(20.0);
            riUnStructuredIndex.setRecWeight(reducedFormIndex.getSrcRecWeight() + reducedFormIndex.getRelRecWeight());
            riUnStructuredIndex.setSearchType(2);
            riUnStructuredIndex.setVariationType(4);
            break;
         case ExecueConstants.SUBJECTCONCEPT_RELATION:
            riUnStructuredIndex.setBeId1(reducedFormIndex.getSrcConceptBEId());
            riUnStructuredIndex.setBeId2(reducedFormIndex.getRelationBEId());
            riUnStructuredIndex.setMaxWeight(20.0);
            riUnStructuredIndex.setRecWeight(reducedFormIndex.getSrcRecWeight() + reducedFormIndex.getRelRecWeight());
            riUnStructuredIndex.setSearchType(2);
            riUnStructuredIndex.setVariationType(4);
            break;
         case ExecueConstants.RELATION_OBJECTINSTANCE:
            riUnStructuredIndex.setBeId1(reducedFormIndex.getRelationBEId());
            riUnStructuredIndex.setBeId2(reducedFormIndex.getDestInstanceBEId());
            riUnStructuredIndex.setMaxWeight(20.0);
            riUnStructuredIndex.setRecWeight(reducedFormIndex.getRelRecWeight() + reducedFormIndex.getDestRecWeight());
            riUnStructuredIndex.setSearchType(2);
            riUnStructuredIndex.setVariationType(5);
            break;
         case ExecueConstants.SUBJECTCONCEPT_OBJECTINSTANCE:
            riUnStructuredIndex.setBeId1(reducedFormIndex.getRelationBEId());
            riUnStructuredIndex.setBeId2(reducedFormIndex.getDestConceptBEId());
            riUnStructuredIndex.setMaxWeight(20.0);
            riUnStructuredIndex.setRecWeight(reducedFormIndex.getSrcRecWeight() + reducedFormIndex.getDestRecWeight());
            riUnStructuredIndex.setSearchType(2);
            riUnStructuredIndex.setVariationType(5);
            break;
         case ExecueConstants.SUBJECTINSTANCE_OBJECTINSTANCE:
            riUnStructuredIndex.setBeId1(reducedFormIndex.getSrcInstanceBEId());
            riUnStructuredIndex.setBeId2(reducedFormIndex.getDestInstanceBEId());
            riUnStructuredIndex.setMaxWeight(20.0);
            riUnStructuredIndex.setRecWeight(reducedFormIndex.getSrcRecWeight() + reducedFormIndex.getDestRecWeight());
            riUnStructuredIndex.setSearchType(2);
            riUnStructuredIndex.setVariationType(6);
            break;
         case ExecueConstants.SUBJECTINSTANCE_OBJECTCONCEPT:
            riUnStructuredIndex.setBeId1(reducedFormIndex.getSrcConceptBEId());
            riUnStructuredIndex.setBeId2(reducedFormIndex.getDestInstanceBEId());
            riUnStructuredIndex.setMaxWeight(20.0);
            riUnStructuredIndex.setRecWeight(reducedFormIndex.getSrcRecWeight() + reducedFormIndex.getDestRecWeight());
            riUnStructuredIndex.setSearchType(2);
            riUnStructuredIndex.setVariationType(6);
            break;
         case ExecueConstants.RELATION_OBJECTCONCEPT:
            riUnStructuredIndex.setBeId1(reducedFormIndex.getSrcInstanceBEId());
            riUnStructuredIndex.setBeId2(reducedFormIndex.getDestConceptBEId());
            riUnStructuredIndex.setMaxWeight(20.0);
            riUnStructuredIndex.setRecWeight(reducedFormIndex.getRelRecWeight() + reducedFormIndex.getDestRecWeight());
            riUnStructuredIndex.setSearchType(2);
            riUnStructuredIndex.setVariationType(6);
            break;
         case ExecueConstants.SUBJECTCONCEPT_OBJECTCONCEPT:
            riUnStructuredIndex.setBeId1(reducedFormIndex.getSrcConceptBEId());
            riUnStructuredIndex.setBeId2(reducedFormIndex.getDestConceptBEId());
            riUnStructuredIndex.setMaxWeight(10.0);
            riUnStructuredIndex.setRecWeight(reducedFormIndex.getSrcRecWeight() + reducedFormIndex.getDestRecWeight());
            riUnStructuredIndex.setSearchType(2);
            riUnStructuredIndex.setVariationType(6);
            break;
         case ExecueConstants.SUBJECTINSTANCE_RELATION_OBJECTINSTANCE:
            riUnStructuredIndex.setBeId1(reducedFormIndex.getSrcInstanceBEId());
            riUnStructuredIndex.setBeId2(reducedFormIndex.getRelationBEId());
            riUnStructuredIndex.setBeId3(reducedFormIndex.getDestInstanceBEId());
            riUnStructuredIndex.setMaxWeight(30.0);
            riUnStructuredIndex.setRecWeight(reducedFormIndex.getSrcRecWeight() + reducedFormIndex.getRelRecWeight()
                     + reducedFormIndex.getDestRecWeight());
            riUnStructuredIndex.setSearchType(1);
            riUnStructuredIndex.setVariationType(7);
            break;
         case ExecueConstants.SUBJECTINSTANCE_RELATION_OBJECTCONCEPT:
            riUnStructuredIndex.setBeId1(reducedFormIndex.getSrcInstanceBEId());
            riUnStructuredIndex.setBeId2(reducedFormIndex.getRelationBEId());
            riUnStructuredIndex.setBeId3(reducedFormIndex.getDestConceptBEId());
            riUnStructuredIndex.setMaxWeight(30.0);
            riUnStructuredIndex.setRecWeight(reducedFormIndex.getSrcRecWeight() + reducedFormIndex.getRelRecWeight()
                     + reducedFormIndex.getDestRecWeight());
            riUnStructuredIndex.setSearchType(1);
            riUnStructuredIndex.setVariationType(7);
            break;
         case ExecueConstants.SUBJECTCONCEPT_RELATION_OBJECTINSTANCE:
            riUnStructuredIndex.setBeId1(reducedFormIndex.getSrcConceptBEId());
            riUnStructuredIndex.setBeId2(reducedFormIndex.getRelationBEId());
            riUnStructuredIndex.setBeId3(reducedFormIndex.getDestInstanceBEId());
            riUnStructuredIndex.setMaxWeight(30.0);
            riUnStructuredIndex.setRecWeight(reducedFormIndex.getSrcRecWeight() + reducedFormIndex.getRelRecWeight()
                     + reducedFormIndex.getDestRecWeight());
            riUnStructuredIndex.setSearchType(1);
            riUnStructuredIndex.setVariationType(7);
            break;
         case ExecueConstants.SUBJECTCONCEPT_RELATION_OBJECTCONCEPT:
            riUnStructuredIndex.setBeId1(reducedFormIndex.getSrcConceptBEId());
            riUnStructuredIndex.setBeId2(reducedFormIndex.getRelationBEId());
            riUnStructuredIndex.setBeId3(reducedFormIndex.getDestConceptBEId());
            riUnStructuredIndex.setMaxWeight(30.0);
            riUnStructuredIndex.setRecWeight(reducedFormIndex.getSrcRecWeight() + reducedFormIndex.getRelRecWeight()
                     + reducedFormIndex.getDestRecWeight());
            riUnStructuredIndex.setSearchType(1);
            riUnStructuredIndex.setVariationType(7);
            break;
         default:
            // Should never come in default case
      }
   }

   public static UnStructuredIndex populateUDXForNewsItem (NewsItem newsItem, Long reducedFormId, Double entityCount,
            Double maxMatchWeight) {
      UnStructuredIndex udx = new UnStructuredIndex();
      udx.setRfId(reducedFormId);
      udx.setUrl(newsItem.getUrl());
      udx.setShortDescription(newsItem.getTitle());
      udx.setLongDescription(newsItem.getSummary());
      udx.setContentSource(newsItem.getSource());
      udx.setContentSourceType(newsItem.getCategory().getValue());
      udx.setCreatedDate(new Date());
      udx.setArticleRefId(newsItem.getId());
      udx.setContentDate(newsItem.getAddedDate());
      udx.setEntityCount(entityCount);
      udx.setMaxMatchWeight(maxMatchWeight);
      return udx;
   }

   /**
    * @return the reducedFormHelper
    */
   public ReducedFormHelper getReducedFormHelper () {
      return reducedFormHelper;
   }

   /**
    * @param reducedFormHelper the reducedFormHelper to set
    */
   public void setReducedFormHelper (ReducedFormHelper reducedFormHelper) {
      this.reducedFormHelper = reducedFormHelper;
   }
}