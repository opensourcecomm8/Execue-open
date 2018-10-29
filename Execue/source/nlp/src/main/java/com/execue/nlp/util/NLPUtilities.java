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


/*
 * Created on Sep 2, 2008
 */
package com.execue.nlp.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.execue.core.common.bean.IWeightedEntity;
import com.execue.core.common.bean.WeightInformation;
import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.NormalizedDataType;
import com.execue.core.common.bean.graph.IGraphComponent;
import com.execue.core.common.bean.nlp.DomainRecognition;
import com.execue.core.common.bean.nlp.InstanceInformation;
import com.execue.core.common.bean.nlp.NLPConstants;
import com.execue.core.common.bean.nlp.RecognitionEntityType;
import com.execue.core.common.bean.nlp.RecognizedType;
import com.execue.core.common.bean.nlp.SemanticPossibility;
import com.execue.core.common.bean.nlp.TimeFrameNormalizedData;
import com.execue.core.common.type.BehaviorType;
import com.execue.core.common.type.BusinessEntityType;
import com.execue.core.common.type.DateQualifier;
import com.execue.core.constants.ExecueConstants;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.core.util.ExecueStringUtil;
import com.execue.nlp.bean.entity.Association;
import com.execue.nlp.bean.entity.ConceptEntity;
import com.execue.nlp.bean.entity.ConceptProfileEntity;
import com.execue.nlp.bean.entity.HitsUpdateInfo;
import com.execue.nlp.bean.entity.InstanceEntity;
import com.execue.nlp.bean.entity.InstanceProfileEntity;
import com.execue.nlp.bean.entity.OntoEntity;
import com.execue.nlp.bean.entity.PotentialPossibilityPosition;
import com.execue.nlp.bean.entity.ProfileEntity;
import com.execue.nlp.bean.entity.PropertyEntity;
import com.execue.nlp.bean.entity.RecognitionEntity;
import com.execue.nlp.bean.entity.RecognizedCloudEntity;
import com.execue.nlp.bean.entity.ReferedTokenPosition;
import com.execue.nlp.bean.entity.TypeEntity;
import com.execue.nlp.bean.matrix.Possibility;
import com.execue.ontology.configuration.OntologyConstants;
import com.execue.util.MathUtil;
import com.execue.util.conversion.timeframe.util.TimeFrameUtility;

/**
 * @author kaliki
 */
public class NLPUtilities {

   public static Map<DateQualifier, List<DateQualifier>> dateQualifierHierarchyConversionMap = new HashMap<DateQualifier, List<DateQualifier>>();
   static {
      TimeFrameUtility.populateDateQualifierHierarchyConversionMap(dateQualifierHierarchyConversionMap);
   }

   public static String getNextSuffix (String prevSuffix) {
      String nextSuffix;
      int intPrevSuffix = Integer.parseInt(prevSuffix);
      if (intPrevSuffix < 9) {
         nextSuffix = "0" + (intPrevSuffix + 1);
      } else {
         nextSuffix = String.valueOf(intPrevSuffix + 1);
      }
      return nextSuffix;
   }

   public static <K, V> Map<K, Double> getPercentileMap (Map<K, Double> percentageMap) {
      Map<K, Double> percentileMap = new HashMap<K, Double>(1);
      Object[] vals = percentageMap.values().toArray();
      Map<Double, Double> percentiles = Percentile.getPercentileMap(vals);

      for (K key : percentageMap.keySet()) {
         percentileMap.put(key, percentiles.get(percentageMap.get(key)));
      }
      return percentileMap;
   }

   public static <K, V> Map<K, Double> getRelativePercentageMap (Map<K, Double> percentageMap) {
      Map<K, Double> percentileMap = new HashMap<K, Double>(1);
      if (percentageMap.isEmpty()) {
         return percentileMap;
      }
      Object[] vals = percentageMap.values().toArray();
      Map<Double, Double> percentiles = Percentile.getRelativePercentageMap(vals);

      for (K key : percentageMap.keySet()) {
         percentileMap.put(key, percentiles.get(percentageMap.get(key)));
      }
      return percentileMap;
   }

   // methods for Time Frame Rules Processor Begin

   public static String getNormValue (String timeFrameUnit) {
      String normVal = "DNV";
      normVal = getNormalizedMonthValue(timeFrameUnit);
      if (normVal == null) {
         normVal = getNormalizedQuarterValue(timeFrameUnit.toUpperCase());
      }
      if (normVal == null) {
         normVal = getNormalizedYearValue(timeFrameUnit.toUpperCase());
      }
      return normVal;
   }

   public static String getNormalizedMonthValue (String timeFrameUnit) {
      String normVal;
      if (ExecueCoreUtil.isNumber(timeFrameUnit)) {
         normVal = (String) ExecueConstants.TFM_MONTH.get(new Integer(timeFrameUnit));
      } else {
         normVal = (String) ExecueConstants.TFM_MONTH.get(timeFrameUnit.toUpperCase());
      }
      return normVal;
   }

   public static String getNormalizedQuarterValue (String val) {
      String qtrVal;
      if (ExecueCoreUtil.isNumber(val)) {
         qtrVal = (String) ExecueConstants.TFM_QUARTER.get(new Integer(val));
      } else {
         qtrVal = (String) ExecueConstants.TFM_QUARTER.get(val);
      }
      return qtrVal;
   }

   public static String getNormalizedYearValue (String val) {
      String yearVal;
      if (val == null) {
         return null;
      }
      if (ExecueCoreUtil.isNumber(val)) {
         yearVal = (String) ExecueConstants.TFM_YEAR.get(new Integer(val));
      } else {
         yearVal = (String) ExecueConstants.TFM_YEAR.get(val);
      }
      return yearVal;
   }

   public static String getNormalizedRelativeYearValue (String val) {
      String yearVal;
      if (val == null) {
         return null;
      }
      if (ExecueCoreUtil.isNumber(val)) {
         yearVal = (String) ExecueConstants.RELATIVE_TFM_YEAR.get(new Integer(val));
      } else {
         yearVal = (String) ExecueConstants.RELATIVE_TFM_YEAR.get(val);
      }
      if (yearVal == null) {
         yearVal = val;
      }
      return yearVal;
   }

   public static String getNormalizedRelativeQuarterValue (String val) {
      String qtrVal;
      if (ExecueCoreUtil.isNumber(val)) {
         qtrVal = (String) ExecueConstants.RELATIVE_TFM_QUARTER.get(new Integer(val));
      } else {
         qtrVal = (String) ExecueConstants.RELATIVE_TFM_QUARTER.get(val);
      }
      if (qtrVal == null) {
         qtrVal = val;
      }
      return qtrVal;
   }

   public static String getNormalizedRelativeMonthValue (String timeFrameUnit) {
      String normVal;
      if (ExecueCoreUtil.isNumber(timeFrameUnit)) {
         normVal = (String) ExecueConstants.RELATIVE_TFM_MONTH.get(new Integer(timeFrameUnit));
      } else {
         normVal = (String) ExecueConstants.RELATIVE_TFM_MONTH.get(timeFrameUnit.toUpperCase());
      }
      if (normVal == null) {
         normVal = timeFrameUnit;
      }
      return normVal;
   }

   public static String getRelativeNormValue (String timeFrameUnit, String timeUnitType) {
      String normVal = "DNV";
      // if (timeUnitType.equalsIgnoreCase("FiscalQuarter")) {
      // normVal = getNormalizedRelativeQuarterValue(timeFrameUnit.toUpperCase());
      // } else if (timeUnitType.equalsIgnoreCase("FiscalYear")) {
      // normVal = getNormalizedRelativeYearValue(timeFrameUnit.toUpperCase());
      // } else {
      // normVal = getNormalizedMonthValue(timeFrameUnit);
      // }
      normVal = timeUnitType + timeFrameUnit;
      return normVal;
   }

   public static String getRelativeDefaultValue (String timeFrameUnit) {
      String defaultVal = "DFV-";
      if ("YEAR".equalsIgnoreCase(timeFrameUnit)) {
         defaultVal = (String) ExecueConstants.RELATIVE_TFM_YEAR.get("DEFAULT");
      } else if ("MONTH".equalsIgnoreCase(timeFrameUnit)) {
         defaultVal = (String) ExecueConstants.RELATIVE_TFM_MONTH.get("DEFAULT");
      } else if ("QUARTER".equalsIgnoreCase(timeFrameUnit)) {
         defaultVal = (String) ExecueConstants.RELATIVE_TFM_QUARTER.get("DEFAULT");
      }
      return defaultVal;
   }

   public static String getDistributedValue (String timeFrameUnit) {
      String distributedVal = "DNV";
      String normVal = "DNV";

      normVal = getNormalizedMonthValue(timeFrameUnit);
      distributedVal = normVal;

      if (normVal == null) {
         normVal = getNormalizedQuarterValue(timeFrameUnit.toUpperCase());
         if (normVal != null) {
            int qtrPos = Integer.parseInt(normVal.substring(1));
            int start = (qtrPos - 1) * 3 + 1;
            int end = start + 2;
            distributedVal = getNormalizedMonthValue(String.valueOf(start)) + ExecueConstants.RANGE_DENOTER
                     + getNormalizedMonthValue(String.valueOf(end));
         }
      }
      if (normVal == null) {
         normVal = getNormalizedYearValue(timeFrameUnit.toUpperCase());
         if (normVal != null) {
            distributedVal = "M01-" + normVal + ExecueConstants.RANGE_DENOTER + "M12-" + normVal;
         }
      }
      return distributedVal;
   }

   public static int[] extractIndexes (String indexString) {
      int[] indexes = { -1, -1 };
      indexes[0] = Integer.parseInt(indexString.substring(0, indexString.indexOf("#")));
      indexes[1] = Integer.parseInt(indexString.substring(indexString.indexOf("#") + 1));
      return indexes;
   }

   public static <K> List<K> getKeysToRemoveByThresholdValue (Map<K, Double> topNMap, double threshold) {
      List<K> removeKeys = new ArrayList<K>(1);
      for (K key : topNMap.keySet()) {
         double d = topNMap.get(key);
         if (d < threshold) {
            removeKeys.add(key);
         }
      }
      return removeKeys;
   }

   public static <K, V> void removeKeysListedFromMap (Map<K, Double> map, Collection<K> removeKeys) {
      for (K removeKey : removeKeys) {
         map.remove(removeKey);
      }
   }

   public static <K, V> Map<K, Double> getMapWithListedKeys (Map<K, Double> map, List<K> keys) {
      Map<K, Double> newMap = new HashMap<K, Double>(1);
      for (K key : keys) {
         newMap.put(key, map.get(key));
      }
      return newMap;
   }

   public static <K, V> Map<K, Double> filterMapByThreshold (Map<K, Double> map, double threshold) {
      List<K> removeKeys = getKeysToRemoveByThresholdValue(map, threshold);
      removeKeysListedFromMap(map, removeKeys);
      return map;
   }

   public static List<String> getByVariables (SemanticPossibility reducedForm) {
      List<String> byVariables = new ArrayList<String>(1);
      Collection<IGraphComponent> vertices = reducedForm.getReducedForm().getVertices().values();
      for (IGraphComponent vertex : vertices) {
         DomainRecognition rec = (DomainRecognition) vertex;
         if (rec.getFlag(ExecueConstants.BY_VARIABLE) || rec.getFlag(ExecueConstants.GROUP_VARIABLE)) {
            if (!byVariables.contains(rec.getConceptName().toUpperCase())) {
               String byVarName = rec.getConceptName().toUpperCase() + " @ " + rec.getPosition();
               byVariables.add(byVarName);
            }
         }
      }
      return byVariables;
   }

   /**
    * This method returns the NLPTag for the given BusinessEntityType.
    * 
    * @param entityType
    * @return String
    */
   public static String getNLPTag (BusinessEntityType entityType) {

      if (entityType.equals(BusinessEntityType.CONCEPT)) {
         return NLPConstants.NLP_TAG_ONTO_CONCEPT;
      }

      if (entityType.equals(BusinessEntityType.REALIZED_TYPE)) {
         return NLPConstants.NLP_TAG_ONTO_REALIZED_TYPE;
      }

      if (entityType.equals(BusinessEntityType.CONCEPT_LOOKUP_INSTANCE)) {
         return NLPConstants.NLP_TAG_ONTO_INSTANCE;
      }

      if (entityType.equals(BusinessEntityType.TYPE_LOOKUP_INSTANCE)) {
         return NLPConstants.NLP_TAG_ONTO_TYPE_INSTANCE;
      }

      if (entityType.equals(BusinessEntityType.REALIZED_TYPE_LOOKUP_INSTANCE)) {
         return NLPConstants.NLP_TAG_ONTO_REALIZED_TYPE_INSTANCE;
      }

      if (entityType.equals(BusinessEntityType.REGEX_INSTANCE)) {
         return NLPConstants.NLP_TAG_ONTO_TYPE_REGEX_INSTANCE;
      }
      return null;
   }

   /**
    * This method returns the NLPTag for the given NormalizedDataType.
    * 
    * @param normalizedDataType
    * @return the String nlpTag
    */
   public static String getNLPTag (NormalizedDataType normalizedDataType) {
      if (normalizedDataType == NormalizedDataType.TIME_FRAME_NORMALIZED_DATA
               || normalizedDataType == NormalizedDataType.UNIT_NORMALIZED_DATA) {
         return NLPConstants.NLP_TAG_ONTO_TYPE_INSTANCE;
      }
      if (normalizedDataType == NormalizedDataType.VALUE_NORMALIZED_DATA
               || normalizedDataType == NormalizedDataType.RANGE_NORMALIZED_DATA) {
         return NLPConstants.NLP_TAG_ONTO_REALIZED_TYPE_INSTANCE;
      }
      return NLPConstants.NLP_TAG_ONTO_TYPE_INSTANCE;
   }

   /**
    * This method returns the recognized type for the given cloud bed
    * 
    * @param typeBed
    * @return the RecognizedType
    */
   public static RecognizedType getRecognizedType (BusinessEntityDefinition typeBed) {
      String typeName = typeBed.getType().getName();
      RecognizedType cloudType = RecognizedType.getCloudType(typeName);
      // // TODO: NK: what should be return as default??
      // if (cloudType == null) {
      // return RecognizedType.VALUE_TYPE;
      // }
      return cloudType;
   }

   /**
    * This method returns the NormalizedDataType for the given type
    * 
    * @param cloudEntity
    *           TODO
    * @return the NormalizedDataType
    */
   public static NormalizedDataType getNormalizedDataType (RecognizedCloudEntity cloudEntity) {
      String outputName = cloudEntity.getCloudOutputName();
      if (cloudEntity.getCloud().getName().equalsIgnoreCase(NLPConstants.RELATIVE_QUARTER_CLOUD)
               || cloudEntity.getCloud().getName().equalsIgnoreCase(NLPConstants.RELATIVE_MONTH_CLOUD)
               || cloudEntity.getCloud().getName().equalsIgnoreCase(NLPConstants.RELATIVE_DAY_CLOUD)
               || cloudEntity.getCloud().getName().equalsIgnoreCase(NLPConstants.RELATIVE_TIME_CLOUD)
               || cloudEntity.getCloud().getName().equalsIgnoreCase(NLPConstants.RELATIVE_WEEK_CLOUD)) {
         return NormalizedDataType.TIME_FRAME_NORMALIZED_DATA;
      } else if (outputName.equalsIgnoreCase(ExecueConstants.TIME_FRAME_TYPE)) {
         return NormalizedDataType.TIME_FRAME_NORMALIZED_DATA;
      } else if (outputName.equalsIgnoreCase(ExecueConstants.VALUE_TYPE)) {
         return NormalizedDataType.VALUE_NORMALIZED_DATA;
      } else if (outputName.equalsIgnoreCase(ExecueConstants.UNIT_TYPE)) {
         return NormalizedDataType.UNIT_NORMALIZED_DATA;
      } else if (outputName.equalsIgnoreCase(ExecueConstants.COMPARATIVE_INFORMATION_TYPE)) {
         return NormalizedDataType.COMPARATIVE_INFO_NORMALIZED_DATA;
      } else if (outputName.equalsIgnoreCase(ExecueConstants.WEEK_TYPE)) {
         return NormalizedDataType.WEEK_NORMALIZED_DATA;
      } else {
         return NormalizedDataType.DEFAULT_NORMALIZED_DATA;
      }
   }

   /**
    * Method to return the recognition entity by id map. As of now this will work with only ontoEntity. Need to
    * type-cast carefully if we want it to work with any other entity.
    * 
    * @param recognitionEntities
    * @return the Map<Long, OntoEntity>
    */
   public static Map<Long, OntoEntity> getRecognitionEntityByIdMap (List<IWeightedEntity> recognitionEntities) {
      Map<Long, OntoEntity> recognitionEntityById = new HashMap<Long, OntoEntity>(1);
      for (IWeightedEntity weightedEntity : recognitionEntities) {
         OntoEntity ontoEntity = (OntoEntity) weightedEntity;
         recognitionEntityById.put(ontoEntity.getId(), ontoEntity);
      }
      return recognitionEntityById;
   }

   /**
    * Method to return the list of recognition entities(instance of type entity only) by component bed id from the given
    * input list of recognition entities
    * 
    * @param recognitionEntities
    * @return the Map<Long, List<IWeightedEntity>>
    */
   public static Map<Long, List<IWeightedEntity>> getRecognitionEntitiesByComponentBedMap (
            List<IWeightedEntity> recognitionEntities) {
      // Create a Map for storing component BED ID versus Recognition Entity List
      Map<Long, List<IWeightedEntity>> recognitionEntitiesByComponentBedId = new HashMap<Long, List<IWeightedEntity>>();
      for (IWeightedEntity recognitionEntity : recognitionEntities) {
         if (recognitionEntity instanceof TypeEntity) {

            // Put the recognition entities against the type bed id
            List<IWeightedEntity> recEntities = recognitionEntitiesByComponentBedId
                     .get(((TypeEntity) recognitionEntity).getTypeBedId());
            if (recEntities == null) {
               recEntities = new ArrayList<IWeightedEntity>();
               recognitionEntitiesByComponentBedId.put(((TypeEntity) recognitionEntity).getTypeBedId(), recEntities);
            }
            if (!recEntities.contains(recognitionEntity)) {
               recEntities.add(recognitionEntity);
            }

            // Put the recognition entities against the behavior bed id
            OntoEntity ontoEntity = (OntoEntity) recognitionEntity;
            for (Long behaviorBedId : ontoEntity.getBehaviorBedIds()) {
               recEntities = recognitionEntitiesByComponentBedId.get(behaviorBedId);
               if (recEntities == null) {
                  recEntities = new ArrayList<IWeightedEntity>();
                  recognitionEntitiesByComponentBedId.put(behaviorBedId, recEntities);
               }
               if (!recEntities.contains(recognitionEntity)) {
                  recEntities.add(recognitionEntity);
               }
            }

            // Put the recognition entities against the concept bed id or instance bed id
            if (ontoEntity instanceof ConceptEntity && ((ConceptEntity) ontoEntity).getConceptBedId() != null) {
               recEntities = recognitionEntitiesByComponentBedId.get(((ConceptEntity) ontoEntity).getConceptBedId());
               if (recEntities == null) {
                  recEntities = new ArrayList<IWeightedEntity>();
                  recognitionEntitiesByComponentBedId.put(((ConceptEntity) ontoEntity).getConceptBedId(), recEntities);
               }
               if (!recEntities.contains(recognitionEntity)) {
                  recEntities.add(recognitionEntity);
               }
            } else if (ontoEntity instanceof InstanceEntity) {
               for (InstanceInformation instanceInformation : ((InstanceEntity) ontoEntity).getInstanceInformations()) {
                  if (instanceInformation.getInstanceBedId() == null) {
                     continue;
                  }
                  recEntities = recognitionEntitiesByComponentBedId.get(instanceInformation.getInstanceBedId());
                  if (recEntities == null) {
                     recEntities = new ArrayList<IWeightedEntity>();
                     recognitionEntitiesByComponentBedId.put(instanceInformation.getInstanceBedId(), recEntities);
                  }
                  if (!recEntities.contains(recognitionEntity)) {
                     recEntities.add(recognitionEntity);
                  }
               }

            }
         }
      }
      return recognitionEntitiesByComponentBedId;
   }

   /**
    * Method to return the list of recognition entities by component bed id i.e. concept, concept profile or relation
    * bed id from the given input list of recognition entities.
    * 
    * @param recognitionEntities
    * @return the Map<Long, List<IWeightedEntity>>
    */
   public static Map<Long, List<IWeightedEntity>> getRecognizedEntitiesByComponentBedIdMap (
            List<IWeightedEntity> recognitionEntities) {
      Map<Long, List<IWeightedEntity>> recEntitiesByComponentBedId = new HashMap<Long, List<IWeightedEntity>>();
      for (IWeightedEntity weightedEntity : recognitionEntities) {
         RecognitionEntity recognitionEntity = (RecognitionEntity) weightedEntity;
         if (recognitionEntity.getEntityType() == RecognitionEntityType.TYPE_ENTITY) {
            continue;
         }
         Long componentBedId = null;
         if (recognitionEntity.getEntityType() == RecognitionEntityType.PROPERTY_ENTITY) {
            PropertyEntity propertyEntity = (PropertyEntity) weightedEntity;
            componentBedId = propertyEntity.getRelationBedId();
         } else {
            ConceptEntity conceptEntity = (ConceptEntity) weightedEntity;
            if (conceptEntity.getEntityType() == RecognitionEntityType.CONCEPT_PROFILE_ENTITY) {
               componentBedId = ((ProfileEntity) conceptEntity).getProfileID();
            } else {
               componentBedId = conceptEntity.getConceptBedId();
            }
         }

         if (componentBedId != null) {
            List<IWeightedEntity> componentRecognitionEntities = recEntitiesByComponentBedId.get(componentBedId);
            if (componentRecognitionEntities == null) {
               componentRecognitionEntities = new ArrayList<IWeightedEntity>(1);
               recEntitiesByComponentBedId.put(componentBedId, componentRecognitionEntities);
            }
            componentRecognitionEntities.add(weightedEntity);
         }
      }
      return recEntitiesByComponentBedId;
   }

   /**
    * This method returns the map containing the position as key and value as IWeightedEntity
    * 
    * @param recognitionEntities
    * @return the Map<Integer, IWeightedEntity>
    */
   public static Map<Integer, IWeightedEntity> getRecognitionEntityByPositionMap (
            List<IWeightedEntity> recognitionEntities) {
      Map<Integer, IWeightedEntity> recEntitiesByPosMap = new HashMap<Integer, IWeightedEntity>();
      if (CollectionUtils.isEmpty(recognitionEntities)) {
         return recEntitiesByPosMap;
      }
      for (IWeightedEntity weightedEntity : recognitionEntities) {
         recEntitiesByPosMap.put(((RecognitionEntity) weightedEntity).getPosition(), weightedEntity);
      }
      return recEntitiesByPosMap;
   }

   /**
    * This method returns the linked hash map containing the position as key and value as list of IWeightedEntity
    * 
    * @param recognitionEntities
    * @return the Map<Integer, IWeightedEntity>
    */
   public static Map<Integer, List<IWeightedEntity>> getRecognitionEntitiesByPositionMap (
            Collection<IWeightedEntity> recognitionEntities) {
      Map<Integer, List<IWeightedEntity>> recEntitiesByPosMap = new LinkedHashMap<Integer, List<IWeightedEntity>>();
      if (CollectionUtils.isEmpty(recognitionEntities)) {
         return recEntitiesByPosMap;
      }
      for (IWeightedEntity weightedEntity : recognitionEntities) {
         List<Integer> referedTokenPositions = ((RecognitionEntity) weightedEntity).getReferedTokenPositions();
         for (Integer position : referedTokenPositions) {
            List<IWeightedEntity> entitiesInPosition = recEntitiesByPosMap.get(position);
            if (entitiesInPosition == null) {
               entitiesInPosition = new ArrayList<IWeightedEntity>(1);
               recEntitiesByPosMap.put(position, entitiesInPosition);
            }
            entitiesInPosition.add(weightedEntity);
         }
      }
      return recEntitiesByPosMap;
   }

   /**
    * This method returns the linked hash map containing the ReferedTokenPosition as key and value as list of
    * IWeightedEntity
    * 
    * @param recognitionEntities
    * @return the Map<Integer, IWeightedEntity>
    */
   public static Map<ReferedTokenPosition, List<IWeightedEntity>> getRecognitionEntitiesByRTPMap (
            List<IWeightedEntity> recognitionEntities) {
      Map<ReferedTokenPosition, List<IWeightedEntity>> recEntitiesByPosMap = new LinkedHashMap<ReferedTokenPosition, List<IWeightedEntity>>(
               1);
      for (IWeightedEntity weightedEntity : recognitionEntities) {
         ReferedTokenPosition rtp = new ReferedTokenPosition(((RecognitionEntity) weightedEntity)
                  .getReferedTokenPositions());
         List<IWeightedEntity> entitiesInPosition = recEntitiesByPosMap.get(rtp);
         if (entitiesInPosition == null) {
            entitiesInPosition = new ArrayList<IWeightedEntity>(1);
            recEntitiesByPosMap.put(rtp, entitiesInPosition);
         }
         entitiesInPosition.add(weightedEntity);
      }
      return recEntitiesByPosMap;
   }

   /**
    * This method returns the linked hash map containing the ReferedTokenPosition as key and value as list of
    * IWeightedEntity which are either referring to ReferedTokenPosition or subset of ReferedTokenPosition
    * 
    * @param recognitionEntities
    * @return the Map<Integer, IWeightedEntity>
    */
   public static Map<ReferedTokenPosition, List<IWeightedEntity>> getRecognitionEntitiesByRTPWithSubsetRTPMap (
            List<IWeightedEntity> recognitionEntities) {
      Map<ReferedTokenPosition, List<IWeightedEntity>> recEntitiesByPosMap = new LinkedHashMap<ReferedTokenPosition, List<IWeightedEntity>>(
               1);
      NLPUtilities.sortRecognitionEntitiesByRTPSize(recognitionEntities);
      for (IWeightedEntity weightedEntity : recognitionEntities) {
         ReferedTokenPosition rtp = new ReferedTokenPosition(((RecognitionEntity) weightedEntity)
                  .getReferedTokenPositions());
         List<IWeightedEntity> entitiesInPosition = recEntitiesByPosMap.get(rtp);
         if (entitiesInPosition == null) {
            boolean added = false;
            // Check if current rtp is subset of any other existing entry in the map
            for (Entry<ReferedTokenPosition, List<IWeightedEntity>> entry : recEntitiesByPosMap.entrySet()) {
               ReferedTokenPosition rtp2 = entry.getKey();
               List<IWeightedEntity> existingRecEntities = entry.getValue();
               if (rtp.isSubset(rtp2)) {
                  added = true;
                  existingRecEntities.add(weightedEntity);
                  break;
               }
            }
            if (!added) {
               entitiesInPosition = new ArrayList<IWeightedEntity>(1);
               entitiesInPosition.add(weightedEntity);
               recEntitiesByPosMap.put(rtp, entitiesInPosition);
            }
         } else {
            entitiesInPosition.add(weightedEntity);
         }
      }
      return recEntitiesByPosMap;
   }

   public static Integer[] getMinAndMaxPos (List<IWeightedEntity> recEntities) {
      Integer[] minAndMaxPos = new Integer[2];
      minAndMaxPos[0] = ((RecognitionEntity) recEntities.get(0)).getPosition(); // to hold Minimum position
      minAndMaxPos[1] = ((RecognitionEntity) recEntities.get(0)).getPosition(); // to hold Maximum position
      for (IWeightedEntity weightedEntity : recEntities) {
         if (minAndMaxPos[0] > ((RecognitionEntity) weightedEntity).getPosition()) {
            minAndMaxPos[0] = ((RecognitionEntity) weightedEntity).getPosition();
         }
         if (minAndMaxPos[1] < ((RecognitionEntity) weightedEntity).getPosition()) {
            minAndMaxPos[1] = ((RecognitionEntity) weightedEntity).getPosition();
         }
      }
      return minAndMaxPos;
   }

   /**
    * This method returns the position difference for the source and destination
    * 
    * @param source
    * @param destination
    * @return the position difference value
    */
   public static int getPositionDifference (RecognitionEntity source, RecognitionEntity destination) {
      ReferedTokenPosition rtpSource = new ReferedTokenPosition();
      rtpSource.setReferedTokenPositions(source.getReferedTokenPositions());
      ReferedTokenPosition rtpDestination = new ReferedTokenPosition();
      rtpDestination.setReferedTokenPositions(destination.getReferedTokenPositions());
      int positionDiff = rtpSource.getDifference(rtpDestination);
      return positionDiff;
   }

   /**
    * This method returns all the referred token positions of the given list of recEntities. If recEntity if of compound
    * type, then it all add the in between positions of that entity as referred token positions
    * 
    * @param recEntities
    * @return the TreeSet<Integer>
    */
   public static TreeSet<Integer> getAllReferredTokenPositions (List<IWeightedEntity> recEntities) {
      TreeSet<Integer> existingRecsPositions = new TreeSet<Integer>();
      if (CollectionUtils.isEmpty(recEntities)) {
         return existingRecsPositions;
      }
      for (IWeightedEntity weightedEntity : recEntities) {
         ReferedTokenPosition rtp = new ReferedTokenPosition(((RecognitionEntity) weightedEntity)
                  .getReferedTokenPositions());
         existingRecsPositions.addAll(rtp.getReferedTokenPositions());
         if (!CollectionUtils.isEmpty(rtp.getInBetweenPos())) {
            existingRecsPositions.addAll(rtp.getInBetweenPos());
         }
      }
      return existingRecsPositions;
   }

   /**
    * This method returns all the referred token positions of the given list of recEntities. If recEntity if of compound
    * type, then it all add the in between positions of that entity as referred token positions
    * 
    * @param recEntities
    * @return the TreeSet<Integer>
    */
   public static TreeSet<Integer> getReferredTokenPositions (Collection<? extends IWeightedEntity> recEntities) {
      TreeSet<Integer> existingRecsPositions = new TreeSet<Integer>();
      if (CollectionUtils.isEmpty(recEntities)) {
         return existingRecsPositions;
      }
      for (IWeightedEntity weightedEntity : recEntities) {
         existingRecsPositions.addAll(((RecognitionEntity) weightedEntity).getReferedTokenPositions());
      }
      return existingRecsPositions;
   }

   /**
    * This method returns all the referred token positions of the given list of recEntities. If recEntity if of compound
    * type, then it all add the in between positions of that entity as referred token positions
    * 
    * @param recEntities
    * @return the TreeSet<Integer>
    */
   public static TreeSet<Integer> getOriginalReferredTokenPositions (List<IWeightedEntity> recEntities) {
      TreeSet<Integer> existingRecsPositions = new TreeSet<Integer>();
      if (CollectionUtils.isEmpty(recEntities)) {
         return existingRecsPositions;
      }
      for (IWeightedEntity weightedEntity : recEntities) {
         existingRecsPositions.addAll(((RecognitionEntity) weightedEntity).getOriginalReferedTokenPositions());
      }
      return existingRecsPositions;
   }

   public static Map<Long, Integer> getFrequencyByTypeBedIdMap (List<IWeightedEntity> recognitionEntities) {
      Map<Long, Integer> frequencyMap = new HashMap<Long, Integer>();
      for (IWeightedEntity weightedEntity : recognitionEntities) {
         Long typeBedId = ((TypeEntity) weightedEntity).getTypeBedId();
         Integer frequency = frequencyMap.get(typeBedId);
         if (frequency == null) {
            frequency = 0;
         }
         frequencyMap.put(typeBedId, frequency + 1);
      }
      return frequencyMap;
   }

   public static IWeightedEntity chooseBestRecognitionEntity (List<? extends IWeightedEntity> entityList) {
      IWeightedEntity bestEntity = null;
      if (entityList.size() == 1) {
         bestEntity = entityList.get(0);
      } else {
         // prepare a map with the weight as the key
         Map<Double, List<IWeightedEntity>> entitiesSortedByWeightMap = new HashMap<Double, List<IWeightedEntity>>();
         for (IWeightedEntity recEntity : entityList) {
            Double quality = new Double(((RecognitionEntity) recEntity).getWeightInformation().getRecognitionQuality());
            List<IWeightedEntity> list = entitiesSortedByWeightMap.get(quality);
            if (ExecueCoreUtil.isCollectionEmpty(list)) {
               list = new ArrayList<IWeightedEntity>();
            }
            list.add(recEntity);
            entitiesSortedByWeightMap.put(quality, list);
         }
         // sort the keys of the map
         List<Double> weightList = new ArrayList<Double>(entitiesSortedByWeightMap.keySet());
         Collections.sort(weightList);
         Double topQuality = weightList.get(weightList.size() - 1);
         List<IWeightedEntity> bestEntities = entitiesSortedByWeightMap.get(topQuality);
         if (bestEntities.size() == 1) {
            bestEntity = bestEntities.get(0);
         } else {
            // filter by the popularity
            Map<Long, List<IWeightedEntity>> entitiesSortedByPopularityMap = new HashMap<Long, List<IWeightedEntity>>();
            for (IWeightedEntity entity : bestEntities) {
               Long popularity = new Long(((OntoEntity) entity).getPopularity());
               List<IWeightedEntity> list = entitiesSortedByPopularityMap.get(popularity);
               if (ExecueCoreUtil.isCollectionEmpty(list)) {
                  list = new ArrayList<IWeightedEntity>();
               }
               list.add(entity);
               entitiesSortedByPopularityMap.put(popularity, list);
            }
            // sort the entities by the popularity
            List<Long> popularityList = new ArrayList<Long>(entitiesSortedByPopularityMap.keySet());
            Collections.sort(popularityList);
            Long topPopularity = popularityList.get(popularityList.size() - 1);
            List<IWeightedEntity> bestPopularityEntities = entitiesSortedByPopularityMap.get(topPopularity);
            if (bestPopularityEntities.size() == 1) {
               bestEntity = bestEntities.get(0);
            } else {
               // use the alphabetical order to pick the final entity
               Map<String, IWeightedEntity> entitiesSortedByNameMap = new HashMap<String, IWeightedEntity>();
               for (IWeightedEntity entity : bestPopularityEntities) {
                  if (entity instanceof InstanceEntity) {
                     InstanceEntity iEntity = (InstanceEntity) entity;
                     // TODO NA check if we need to put recENtity by all the instance names available.
                     String instanceDisplayName = iEntity.getInstanceInformations().get(0).getInstanceDisplayName();
                     entitiesSortedByNameMap.put(instanceDisplayName, entity);
                  }
               }
               List<String> nameList = new ArrayList<String>(entitiesSortedByNameMap.keySet());
               Collections.sort(popularityList);
               String topName = nameList.get(0);
               bestEntity = entitiesSortedByNameMap.get(topName);
            }
         }
      }
      return bestEntity;
   }

   /**
    * This method returns the list of rec entities after applying the top cluster. If applyLiberal flag is set to true
    * then it will apply the top liberal cluster.
    * 
    * @param entityList
    * @param applyLiberal
    * @return the List<IWeightedEntity>
    */
   public static List<IWeightedEntity> performTopCluster (List<? extends IWeightedEntity> entityList,
            boolean applyLiberal) {
      List<Double> ontoEntitiesWeights = new ArrayList<Double>();
      List<Double> topOntoEntitiesWeights = new ArrayList<Double>();
      List<RecognitionEntity> topOntoEntities = new ArrayList<RecognitionEntity>();
      List<IWeightedEntity> finalEnList = new ArrayList<IWeightedEntity>(1);

      // dont need a top cluster if only one entity exist for token.
      if (entityList.size() <= 1) {
         finalEnList.addAll(entityList);
         return finalEnList;
      }
      for (IWeightedEntity recEntity : entityList) {
         ontoEntitiesWeights.add(((RecognitionEntity) recEntity).getWeightInformation().getRecognitionQuality());
      }
      if (!CollectionUtils.isEmpty(ontoEntitiesWeights) && ontoEntitiesWeights.size() > 1) {
         topOntoEntitiesWeights = applyLiberal ? MathUtil.getLiberalTopCluster(ontoEntitiesWeights) : MathUtil
                  .getTopCluster(ontoEntitiesWeights);
      } else {
         topOntoEntitiesWeights = ontoEntitiesWeights;
      }
      for (IWeightedEntity recEntity : entityList) {
         if (topOntoEntitiesWeights.contains(((RecognitionEntity) recEntity).getWeightInformation()
                  .getRecognitionQuality())) {
            topOntoEntities.add((RecognitionEntity) recEntity);
         }
      }
      finalEnList.addAll(topOntoEntities);
      return finalEnList;
   }

   /**
    * This method returns the map of list of IWeightedEntity against the ReferedTokenPosition
    * 
    * @param recognitionEntities
    * @return the Map<Integer, List<IWeightedEntity>>
    */
   public static Map<ReferedTokenPosition, List<IWeightedEntity>> getWeightedEntityByPosMap (
            List<IWeightedEntity> recognitionEntities) {
      Map<ReferedTokenPosition, List<IWeightedEntity>> weightedEntitiesByPosMap = new TreeMap<ReferedTokenPosition, List<IWeightedEntity>>();
      for (IWeightedEntity weightedEntity : recognitionEntities) {
         ReferedTokenPosition rtp = new ReferedTokenPosition(((RecognitionEntity) weightedEntity)
                  .getOriginalReferedTokenPositions());
         List<IWeightedEntity> entities = weightedEntitiesByPosMap.get(rtp);
         if (CollectionUtils.isEmpty(entities)) {
            entities = new ArrayList<IWeightedEntity>(1);
            weightedEntitiesByPosMap.put(rtp, entities);
         }
         entities.add(weightedEntity);
      }
      return weightedEntitiesByPosMap;
   }

   public static List<Possibility> getUnambiguousPossibilities (List<IWeightedEntity> recognitionEntities,
            int maxPossibilityLimit) throws CloneNotSupportedException {
      List<Possibility> allUnAmbiguousPossibilities = new ArrayList<Possibility>(1);
      if (CollectionUtils.isEmpty(recognitionEntities)) {
         return allUnAmbiguousPossibilities;
      }
      sortRecEntitiesByQuality(recognitionEntities);
      Map<Integer, TreeSet<Integer>> coveredPositionsByPossibilityId = new HashMap<Integer, TreeSet<Integer>>(1);
      Map<Integer, Possibility> possibilityById = new HashMap<Integer, Possibility>(1);
      int id = 1;
      for (IWeightedEntity weightedEntity : recognitionEntities) {
         RecognitionEntity recEntity = (RecognitionEntity) weightedEntity;
         if (CollectionUtils.isEmpty(allUnAmbiguousPossibilities)) {
            createPossibility(allUnAmbiguousPossibilities, weightedEntity, id, coveredPositionsByPossibilityId,
                     possibilityById);
            id++;
         } else {
            boolean addedInPoss = false;
            Map<String, Possibility> newPossibilities = new HashMap<String, Possibility>(1);
            for (Possibility possibility : allUnAmbiguousPossibilities) {
               if (checkIfRecEntityCanBeAdded(possibility, recEntity, coveredPositionsByPossibilityId)) {
                  possibility.addRecognitionEntities((IWeightedEntity) recEntity.clone());
                  if (recEntity.getFlags().containsKey(OntologyConstants.POPULATION_CONCEPT)) {
                     possibility.setCentralConceptExists(true);
                  }
                  if (!((OntoEntity) recEntity).getBehaviors().contains(BehaviorType.ATTRIBUTE)
                           && !((OntoEntity) recEntity).getModelGroupId().equals(1L)) {
                     possibility.setNonAttributePossibility(true);
                  }
                  TreeSet<Integer> positions = coveredPositionsByPossibilityId.get(possibility.getId());
                  if (positions == null) {
                     positions = new TreeSet<Integer>();
                     coveredPositionsByPossibilityId.put(id, positions);
                  }
                  positions.addAll(recEntity.getReferedTokenPositions());
                  addedInPoss = true;
               } else if (maxPossibilityLimit == -1 || allUnAmbiguousPossibilities.size() < maxPossibilityLimit) {
                  Possibility possibility2 = mergeWithPossibility(id, possibility, recEntity,
                           coveredPositionsByPossibilityId, possibilityById);
                  if (possibility2 != null) {
                     id++;
                     newPossibilities.put(getAlphabeticalKey(possibility2.getRecognitionEntities()), possibility2);
                  }
               }
            }
            if (newPossibilities.size() > 0) {
               addedInPoss = true;
               allUnAmbiguousPossibilities.addAll(newPossibilities.values());
            }
            if (!addedInPoss && (maxPossibilityLimit == -1 || allUnAmbiguousPossibilities.size() < maxPossibilityLimit)) {
               createPossibility(allUnAmbiguousPossibilities, weightedEntity, id, coveredPositionsByPossibilityId,
                        possibilityById);
               id++;
            }
         }
      }

      // Prepare the sorted list of entries having the possibility with the most covered positions on top
      List<Entry<Integer, TreeSet<Integer>>> positionsCoveredByPossibilityIdEntryList = new ArrayList<Entry<Integer, TreeSet<Integer>>>(
               coveredPositionsByPossibilityId.entrySet());
      Collections.sort(positionsCoveredByPossibilityIdEntryList, new Comparator<Entry<Integer, TreeSet<Integer>>>() {

         public int compare (Entry<Integer, TreeSet<Integer>> o1, Entry<Integer, TreeSet<Integer>> o2) {
            TreeSet<Integer> c1 = o1.getValue();
            TreeSet<Integer> c2 = o2.getValue();
            if (c1.size() > c2.size()) {
               return -1;
            } else {
               return 1;
            }
         }

      });

      // Get the possibilities with the maximum token coverage
      int maxCoveredPositionsSize = positionsCoveredByPossibilityIdEntryList.get(0).getValue().size();
      List<Possibility> topPossibilitiesByCoveredPositions = new ArrayList<Possibility>(1);
      for (Map.Entry<Integer, TreeSet<Integer>> entry : positionsCoveredByPossibilityIdEntryList) {
         if (maxCoveredPositionsSize == entry.getValue().size()) {
            topPossibilitiesByCoveredPositions.add(possibilityById.get(entry.getKey()));
         } else {
            break;
         }
      }
      return topPossibilitiesByCoveredPositions;

   }

   public static String getAlphabeticalKey (List<IWeightedEntity> recEnList) {
      StringBuffer sb = new StringBuffer();

      for (IWeightedEntity weightedEntity : recEnList) {
         RecognitionEntity recognitionEntity = (RecognitionEntity) weightedEntity;
         String word = recognitionEntity.getWord();

         if (recognitionEntity instanceof InstanceEntity) {
            String conceptName = ((ConceptEntity) recognitionEntity).getConceptDisplayName();
            if (conceptName != null) {
               word = word + "-" + conceptName;
            }
            for (InstanceInformation instanceInformation : ((InstanceEntity) recognitionEntity)
                     .getInstanceInformations()) {
               String instanceName = instanceInformation.getInstanceDisplayName();
               String instanceValue = instanceInformation.getInstanceValue();
               word = word + "-" + (instanceName != null ? instanceName : instanceValue);
            }

         } else if (recognitionEntity instanceof ProfileEntity) {
            word = word + "-" + ((ProfileEntity) recognitionEntity).getProfileName();
         }
         sb.append(word.replaceAll(" ", "").toLowerCase());
      }

      return sb.toString();

   }

   private static Possibility mergeWithPossibility (int id, Possibility possibility, RecognitionEntity recEntity,
            Map<Integer, TreeSet<Integer>> possibilityPositionMap, Map<Integer, Possibility> possibilityIdMap)
            throws CloneNotSupportedException {
      ReferedTokenPosition rtpRec = new ReferedTokenPosition(recEntity.getReferedTokenPositions());
      TreeSet<Integer> positions = new TreeSet<Integer>();
      List<IWeightedEntity> newRecList = new ArrayList<IWeightedEntity>(1);
      for (IWeightedEntity weightedEntity : possibility.getRecognitionEntities()) {
         RecognitionEntity recEntity1 = (RecognitionEntity) weightedEntity;
         ReferedTokenPosition rtpRec1 = new ReferedTokenPosition(recEntity1.getReferedTokenPositions());
         if (!rtpRec.isOverLap(rtpRec1)) {
            newRecList.add((IWeightedEntity) recEntity1.clone());
            positions.addAll(recEntity1.getReferedTokenPositions());
         }
      }
      if (CollectionUtils.isEmpty(newRecList)) {
         return null;
      } else {
         Possibility newPossibility = (Possibility) possibility.clone();
         newPossibility.setId(id);
         newRecList.add(recEntity);
         boolean populationConceptFound = false;
         boolean nonAttributePossibility = false;
         for (IWeightedEntity weightedEntity : newRecList) {
            if (((RecognitionEntity) weightedEntity).getFlags().containsKey(OntologyConstants.POPULATION_CONCEPT)) {
               populationConceptFound = true;
               if (nonAttributePossibility) {
                  break;
               }
            }
            if (!((OntoEntity) weightedEntity).getBehaviors().contains(BehaviorType.ATTRIBUTE)
                     && !((OntoEntity) weightedEntity).getModelGroupId().equals(1L)) {
               nonAttributePossibility = true;
               if (populationConceptFound) {
                  break;
               }
            }
         }
         newPossibility.setCentralConceptExists(populationConceptFound);
         newPossibility.setNonAttributePossibility(nonAttributePossibility);
         positions.addAll(recEntity.getReferedTokenPositions());
         possibilityPositionMap.put(id, positions);
         newPossibility.setRecognitionEntities(newRecList);
         possibilityIdMap.put(id, newPossibility);
         return newPossibility;
      }
   }

   private static boolean checkIfRecEntityCanBeAdded (Possibility possibility, RecognitionEntity recEntity,
            Map<Integer, TreeSet<Integer>> possinilityPositionMap) {
      ReferedTokenPosition rtpPoss = new ReferedTokenPosition();
      rtpPoss.setReferedTokenPositions(possinilityPositionMap.get(possibility.getId()));
      ReferedTokenPosition rtpRec = new ReferedTokenPosition(recEntity.getReferedTokenPositions());
      if (!rtpRec.isOverLap(rtpPoss)) {
         return true;
      } else {
         for (IWeightedEntity weightedEntity : possibility.getRecognitionEntities()) {
            RecognitionEntity recEntity1 = (RecognitionEntity) weightedEntity;
            ReferedTokenPosition rtpRec1 = new ReferedTokenPosition(recEntity1.getReferedTokenPositions());
            if (rtpRec.isOverLap(rtpRec1)) {
               if (recEntity.getClusterInformation() == null || recEntity1.getClusterInformation() == null
                        || !recEntity.getClusterInformation().equals(recEntity1.getClusterInformation())
                        || recEntity.getReferedTokenPositions().containsAll(recEntity1.getReferedTokenPositions())
                        || recEntity1.getReferedTokenPositions().containsAll(recEntity.getReferedTokenPositions())) {
                  return false;
               }
            }
         }
      }
      return true;
   }

   private static void createPossibility (List<Possibility> allUnAmbiguousPossibilities,
            IWeightedEntity weightedEntity, int id, Map<Integer, TreeSet<Integer>> possinilityPositionMap,
            Map<Integer, Possibility> possibilityIdMap) throws CloneNotSupportedException {
      Possibility poss = new Possibility();
      // poss.addRecognitionEntities(weightedEntity);
      RecognitionEntity recEntity = (RecognitionEntity) weightedEntity;
      RecognitionEntity newRecEntity = (RecognitionEntity) recEntity.clone();
      poss.addRecognitionEntities(newRecEntity);
      if (newRecEntity.getFlags().containsKey(OntologyConstants.POPULATION_CONCEPT)) {
         poss.setCentralConceptExists(true);
      }
      if (!((OntoEntity) recEntity).getBehaviors().contains(BehaviorType.ATTRIBUTE)
               && !((OntoEntity) recEntity).getModelGroupId().equals(1L)) {
         poss.setNonAttributePossibility(true);
      }
      poss.setId(id);
      TreeSet<Integer> positions = possinilityPositionMap.get(id);
      if (positions == null) {
         positions = new TreeSet<Integer>();
         possinilityPositionMap.put(id, positions);
      }
      positions.addAll(recEntity.getReferedTokenPositions());
      possibilityIdMap.put(id, poss);
      allUnAmbiguousPossibilities.add(poss);

   }

   public static List<PotentialPossibilityPosition> getUnambiguousPositions (Set<ReferedTokenPosition> existingRTPs) {
      List<PotentialPossibilityPosition> positionList = new ArrayList<PotentialPossibilityPosition>(1);
      ReferedTokenPosition completePosition = new ReferedTokenPosition();
      for (ReferedTokenPosition referedTokenPosition : existingRTPs) {
         completePosition.addAll(referedTokenPosition.getReferedTokenPositions());
         List<PotentialPossibilityPosition> newPositions = new ArrayList<PotentialPossibilityPosition>(1);
         if (CollectionUtils.isEmpty(positionList)) {
            PotentialPossibilityPosition potentialPossibilityPosition = new PotentialPossibilityPosition();
            potentialPossibilityPosition.addParticipatingPositions(referedTokenPosition);
            positionList.add(potentialPossibilityPosition);
            continue;
         }
         for (PotentialPossibilityPosition possibilityPosition : positionList) {
            ReferedTokenPosition coveredPosition = possibilityPosition.getCoveredPosition();
            if (referedTokenPosition.isOverLap(coveredPosition)) {
               List<ReferedTokenPosition> nonOverLappingPositions = new ArrayList<ReferedTokenPosition>(1);
               for (ReferedTokenPosition referedTokenPosition2 : possibilityPosition.getParticipatingPositions()) {
                  if (!referedTokenPosition2.isOverLap(referedTokenPosition)) {
                     nonOverLappingPositions.add(referedTokenPosition2);
                  }
               }
               PotentialPossibilityPosition newPossibilityPosition = new PotentialPossibilityPosition();
               newPossibilityPosition.addParticipatingPositions(referedTokenPosition);
               for (ReferedTokenPosition rtp : nonOverLappingPositions) {
                  newPossibilityPosition.addParticipatingPositions(rtp);
               }
               newPositions.add(newPossibilityPosition);
            } else {
               possibilityPosition.addParticipatingPositions(referedTokenPosition);
            }
         }
         positionList.addAll(newPositions);
      }
      List<PotentialPossibilityPosition> finalPoList = filterUnwantedPositions(positionList, completePosition);
      return finalPoList;
   }

   private static List<PotentialPossibilityPosition> filterUnwantedPositions (
            List<PotentialPossibilityPosition> positionList, ReferedTokenPosition completePosition) {
      List<PotentialPossibilityPosition> finalList = new ArrayList<PotentialPossibilityPosition>(1);
      for (PotentialPossibilityPosition possibilityPosition : positionList) {
         if (possibilityPosition.getCoveredPosition().equals(completePosition)) {
            finalList.add(possibilityPosition);
         }
      }
      return finalList;
   }

   /**
    * Checks if the POS type is valid or not
    * 
    * @param posType
    * @return
    */
   public static boolean checkValidPosType (String posType) {
      String regex = "[CC|IN|TO|SYM|UH|MD|DT|W+|,]";
      Pattern pattern = Pattern.compile(regex);
      return pattern.matcher(posType).lookingAt();
   }

   /**
    * Method to sort the recognition entity list based on compareTo method of ReferredTokenPosition present in them.
    * 
    * @param recognitionEntities
    */
   public static void sortRecognitionEntitiesByRTP (List<? extends IWeightedEntity> recognitionEntities) {
      Collections.sort(recognitionEntities, new RecognitionEntityComparator());
   }

   /**
    * Method to sort the recognition entity list based on position. If two or more rec entities shared the common end
    * position then sort the entities based on start position.
    * 
    * @param recognitionEntities
    */
   // TODO: NK: In future, should avoid the below call and use the above sortRecEntitiesByRTP method only
   public static void sortRecognitionEntitiesByPosition (List<IWeightedEntity> recognitionEntities) {
      Map<Integer, List<IWeightedEntity>> startPositionRecEntityMap = new TreeMap<Integer, List<IWeightedEntity>>();
      Map<Integer, IWeightedEntity> endPositionRecEntityMap = new TreeMap<Integer, IWeightedEntity>();
      boolean sortOnEndPos = false;
      for (IWeightedEntity weightedEntity : recognitionEntities) {
         RecognitionEntity recEntity = (RecognitionEntity) weightedEntity;
         ReferedTokenPosition referedTokenPosition = new ReferedTokenPosition(recEntity.getReferedTokenPositions());
         Integer startPos = referedTokenPosition.getReferedTokenPositions().first();
         List<IWeightedEntity> recEntityByStartPos = startPositionRecEntityMap.get(startPos);
         if (recEntityByStartPos == null) {
            recEntityByStartPos = new ArrayList<IWeightedEntity>(1);
            recEntityByStartPos.add(recEntity);
            startPositionRecEntityMap.put(startPos, recEntityByStartPos);
         } else {
            sortOnEndPos = true;
         }
         Integer endPos = referedTokenPosition.getReferedTokenPositions().last();

         if (!endPositionRecEntityMap.containsKey(endPos)) {
            endPositionRecEntityMap.put(endPos, recEntity);
         } else {
            sortOnEndPos = false;
            break;
         }
      }
      if (!sortOnEndPos) {
         Collections.sort(recognitionEntities, new RecognitionEntityComparator());
      } else {
         recognitionEntities.clear();
         recognitionEntities.addAll(endPositionRecEntityMap.values());
      }
   }

   /**
    * Method to sort the recognition entity list based on size of the RTP.
    * 
    * @param recognitionEntities
    */
   public static void sortRecognitionEntitiesByRTPSize (List<? extends IWeightedEntity> recognitionEntities) {
      Collections.sort(recognitionEntities, new Comparator<IWeightedEntity>() {

         public int compare (IWeightedEntity o1, IWeightedEntity o2) {
            ReferedTokenPosition rtp1 = new ReferedTokenPosition();
            rtp1.setReferedTokenPositions(((RecognitionEntity) o1).getReferedTokenPositions());
            ReferedTokenPosition rtp2 = new ReferedTokenPosition();
            rtp2.setReferedTokenPositions(((RecognitionEntity) o2).getReferedTokenPositions());
            if (rtp1.getReferedTokenPositions().size() < rtp2.getReferedTokenPositions().size()) {
               return 1;
            } else {
               return -1;
            }
         }
      });
   }

   /**
    * Method to sort the recognition entity list based on quality.
    * 
    * @param recognitionEntities
    */
   public static void sortRecEntitiesByQuality (List<IWeightedEntity> recognitionEntities) {
      Collections.sort(recognitionEntities, new Comparator<IWeightedEntity>() {

         public int compare (IWeightedEntity o1, IWeightedEntity o2) {
            double quality1 = ((RecognitionEntity) o1).getRecognitionQuality();
            double quality2 = ((RecognitionEntity) o2).getRecognitionQuality();
            if (quality1 > quality2) {
               return -1;
            } else {
               return 1;
            }
         }
      });
   }

   /**
    * Utility method to convert the input set of long as set of BehaviorType enumeration
    * 
    * @param cloudAllowedBehavior
    * @return the Set<BehaviorType>
    */
   public static Set<BehaviorType> getBehaviorTypes (Set<Long> cloudAllowedBehavior) {
      Set<BehaviorType> behaviorTypes = new HashSet<BehaviorType>(1);
      for (Long behavior : cloudAllowedBehavior) {
         behaviorTypes.add(BehaviorType.getType(behavior.intValue()));
      }
      return behaviorTypes;
   }

   public static Map<Long, Integer> getFrequencyByTypeAndBehaviorBedIdMap (List<IWeightedEntity> recognitionEntities) {
      Map<Long, Integer> frequencyMap = new HashMap<Long, Integer>();
      for (IWeightedEntity weightedEntity : recognitionEntities) {
         if (weightedEntity instanceof TypeEntity) {
            Long typeBedId = ((TypeEntity) weightedEntity).getTypeBedId();
            Integer frequency = frequencyMap.get(typeBedId);
            if (frequency == null) {
               frequency = 0;
            }
            frequencyMap.put(typeBedId, frequency + 1);
            OntoEntity ontoEntity = (OntoEntity) weightedEntity;
            for (Long behaviorBedId : ontoEntity.getBehaviorBedIds()) {
               frequency = frequencyMap.get(behaviorBedId);
               if (frequency == null) {
                  frequency = 0;
               }
               frequencyMap.put(behaviorBedId, frequency + 1);
            }
         }
      }
      return frequencyMap;
   }

   /**
    * This method checks if the weighted entity position is less than zero and flags contains population concept key
    * then adds to the default central concept list
    * 
    * @param recognitionEntities
    * @return the List<IWeightedEntity>
    */
   public static List<IWeightedEntity> getDefaultCentralConcepts (List<IWeightedEntity> recognitionEntities) {

      List<IWeightedEntity> defaultCentralConcepts = new ArrayList<IWeightedEntity>(1);
      for (IWeightedEntity weightedEntity : recognitionEntities) {
         RecognitionEntity recEntity = (RecognitionEntity) weightedEntity;
         if (recEntity.getPosition() < 0 && recEntity.getFlags().containsKey(OntologyConstants.POPULATION_CONCEPT)) {
            defaultCentralConcepts.add(recEntity);
         }
      }
      return defaultCentralConcepts;

   }

   public static boolean checkPopulationConcept (IWeightedEntity weightedEntity) {
      RecognitionEntity recEntity = (RecognitionEntity) weightedEntity;
      boolean populationConcept = false;
      if (recEntity instanceof ConceptEntity
               && (recEntity.getNLPtag().equals(NLPConstants.NLP_TAG_ONTO_CONCEPT) || recEntity.getNLPtag().equals(
                        NLPConstants.NLP_TAG_ONTO_INSTANCE))) {
         populationConcept = ((ConceptEntity) recEntity).getBehaviors().contains(BehaviorType.POPULATION);
      }
      return populationConcept;
   }

   /**
    * This method returns the HitsUpdateInfo for all the tokens present in the input list of weightedEntities
    * 
    * @param weightedEntities
    * @return the HitsUpdateInfo
    */
   public static HitsUpdateInfo getHitsUpdateInfo (List<? extends IWeightedEntity> weightedEntities) {
      HitsUpdateInfo hitsUpdateInfo = new HitsUpdateInfo();
      for (IWeightedEntity weightedEntity : weightedEntities) {
         RecognitionEntity recEntity = (RecognitionEntity) weightedEntity;
         HitsUpdateInfo hitsUpdateInfo2 = recEntity.getHitsUpdateInfo();
         if (hitsUpdateInfo2 != null) {
            for (Long tokenId : hitsUpdateInfo2.getSflTokensIds()) {
               hitsUpdateInfo.getSflTokensIds().add(tokenId);
            }
         }
      }

      return hitsUpdateInfo;
   }

   /**
    * This method returns the map where key is model_group_id and the value is the list of rec entiities belonging to
    * that model group
    * 
    * @param topClusterByConceptList
    * @return the Map<Long, List<IWeightedEntity>>
    */
   public static Map<Long, List<IWeightedEntity>> getRecEntitiesMapByModelGroup (
            List<? extends IWeightedEntity> topClusterByConceptList) {
      Map<Long, List<IWeightedEntity>> recEntitiesMap = new HashMap<Long, List<IWeightedEntity>>(1);
      if (CollectionUtils.isEmpty(topClusterByConceptList)) {
         return recEntitiesMap;
      }
      for (IWeightedEntity recognitionEntity : topClusterByConceptList) {
         Long modelGroupId = ((OntoEntity) recognitionEntity).getModelGroupId();
         List<IWeightedEntity> recEntities = recEntitiesMap.get(modelGroupId);
         if (recEntities == null) {
            recEntities = new ArrayList<IWeightedEntity>(1);
            recEntitiesMap.put(modelGroupId, recEntities);
         }
         recEntities.add(recognitionEntity);
      }
      return recEntitiesMap;
   }

   /**
    * This method returns the average of all the popularity for the given list of recognitionEntities
    * 
    * @param recognitionEntities
    * @return the Double
    */
   public static Double getPopularityAverage (List<IWeightedEntity> recognitionEntities) {
      Double avgPopularity = 0d;
      if (CollectionUtils.isEmpty(recognitionEntities)) {
         return avgPopularity;
      }
      for (IWeightedEntity weightedEntity : recognitionEntities) {
         avgPopularity += ((OntoEntity) weightedEntity).getPopularity();
      }
      avgPopularity = avgPopularity / recognitionEntities.size();
      return avgPopularity;
   }

   /**
    * @param recognizedType
    * @return
    */
   public static DateQualifier getDateQualifierBasedOnTFType (TimeFrameNormalizedData tfNormalizedData) {
      RecognizedType recognizedType = tfNormalizedData.getTimeFrameType();
      if (RecognizedType.DAY_TYPE == recognizedType || RecognizedType.WEEK_DAY_TYPE == recognizedType) {
         return DateQualifier.DAY;
      } else if (RecognizedType.MONTH_TYPE == recognizedType) {
         return DateQualifier.MONTH;
      } else if (RecognizedType.QUARTER_TYPE == recognizedType) {
         return DateQualifier.QUARTER;
      } else if (RecognizedType.YEAR_TYPE == recognizedType) {
         return DateQualifier.YEAR;
      } else if (RecognizedType.HOUR_TYPE == recognizedType) {
         return DateQualifier.HOUR;
      } else if (RecognizedType.MINUTE_TYPE == recognizedType) {
         return DateQualifier.MINUTE;
      } else if (RecognizedType.SECOND_TYPE == recognizedType) {
         return DateQualifier.SECOND;
      } else if (RecognizedType.WEEK_TYPE == recognizedType) {
         return DateQualifier.WEEK;
      } else if (RecognizedType.TIME_TYPE == recognizedType) {
         if (tfNormalizedData.getSecond() != null) {
            return DateQualifier.SECOND;
         } else if (tfNormalizedData.getMinute() != null) {
            return DateQualifier.MINUTE;
         } else {
            return DateQualifier.HOUR;
         }
      }
      return null;

   }

   public static List<IWeightedEntity> getImlicitAddedConcepts (List<IWeightedEntity> recognitionEntities) {
      List<IWeightedEntity> defaultCentralConcepts = new ArrayList<IWeightedEntity>(1);
      for (IWeightedEntity weightedEntity : recognitionEntities) {
         RecognitionEntity recEntity = (RecognitionEntity) weightedEntity;
         if (recEntity.getPosition() < 0) {
            defaultCentralConcepts.add(recEntity);
         }
      }
      return defaultCentralConcepts;
   }

   public static boolean checkIfEntityIsOfEntityType (BusinessEntityType entityType, IWeightedEntity weightedEntity) {
      RecognitionEntity recEntity = (RecognitionEntity) weightedEntity;
      if (entityType.equals(BusinessEntityType.CONCEPT_LOOKUP_INSTANCE)
               || entityType.equals(BusinessEntityType.TYPE_LOOKUP_INSTANCE)
               || entityType.equals(BusinessEntityType.REALIZED_TYPE_LOOKUP_INSTANCE)
               || entityType.equals(BusinessEntityType.REGEX_INSTANCE)) {
         if (!(recEntity.getEntityType() == RecognitionEntityType.INSTANCE_ENTITY)) {
            return false;
         }
      } else if (entityType == BusinessEntityType.CONCEPT || entityType == BusinessEntityType.REALIZED_TYPE) {
         if (!(recEntity.getEntityType() == RecognitionEntityType.CONCEPT_ENTITY)) {
            return false;
         }
      } else if (entityType.equals(BusinessEntityType.TYPE)) {
         if (!(recEntity.getEntityType() == RecognitionEntityType.TYPE_ENTITY)) {
            return false;
         }
      }
      return true;
   }

   /**
    * @param componentInfo
    *           Component to be checked for validation
    * @return
    */
   public static boolean isValidBasedOnRequiredBehavior (BehaviorType behaviorType, IWeightedEntity weightedEntity) {
      OntoEntity ontoEntity = (OntoEntity) weightedEntity;
      return ontoEntity.getBehaviors().contains(behaviorType);
   }

   /**
    * Method to process the unit scale and symbols. Also will compact and mark the currency pattern for currency regex
    * instance recognition.
    * 
    * @param userQuerySentence
    * @param operatorSymbolMap
    * @param unitScaleMap
    * @return the String
    */
   public static String processCurrencyPattern (String userQuerySentence, Map<String, String> operatorSymbolMap,
            Map<String, String> unitScaleMap) {
      boolean compactedUnitFound = false;
      String regex = "(=|<|>|>=|<=|Less Than|Greater Than|Greater Than Equal To|Less Than Equal To|Equals|Under|Over|Below|Above|More Than)?\\s*([$])\\s*(\\d*)?(\\.)?(\\d+)?\\s*(Hundred|Thousand|Millions|Million|Billions|Billion|Trillions|Trillion|h|k|m|b|t)?($|\\s+)"
               + "|(=|<|>|>=|<=|Less Than|Greater Than|Greater Than Equal To|Less Than Equal To|Equals|Under|Over|Below|Above|More Than)?\\s*(\\d*)?(\\.)?(\\d+)?(Hundred|Thousand|Millions|Million|Billions|Billion|Trillions|Trillion|h|k|m|b|t)?([$])($|\\s+)";
      Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
      Matcher matcher = pattern.matcher(userQuerySentence);
      Map<String, String> compactedPatternMap = new HashMap<String, String>(1);
      while (matcher.find()) {
         if (StringUtils.isNotEmpty(matcher.group(1))) {
            String operator = matcher.group(1).trim();
            if (operatorSymbolMap.containsKey(operator.toLowerCase())) {
               operator = operatorSymbolMap.get(operator.toLowerCase());
            } else if (operator.equalsIgnoreCase("More Than")) {
               operator = ">";
            }

            if (StringUtils.isNotEmpty(matcher.group(2))) {
               String currencySymbol = matcher.group(2).trim();
               // Get the group's captured text
               String groupStr = matcher.group().trim();
               if (StringUtils.isEmpty(groupStr)) {
                  continue;
               }

               // Check if unitscale is present, then normalize unit scale
               String unitScale = "";
               if (StringUtils.isNotEmpty(matcher.group(6))) {
                  unitScale = matcher.group(6).trim();
                  if (unitScale.toLowerCase().endsWith("s")) {
                     unitScale = unitScale.substring(0, unitScale.length() - 1);
                  }
                  if (unitScaleMap.containsKey(unitScale.toLowerCase())) {
                     unitScale = unitScaleMap.get(unitScale.toLowerCase());
                  }
               }

               // normalize the number
               String decimal = matcher.group(3);
               String separator = matcher.group(4);
               String precision = matcher.group(5);
               if (StringUtils.isNotEmpty(decimal) || StringUtils.isNotEmpty(separator)
                        && StringUtils.isNotEmpty(precision)) {
                  compactedUnitFound = true;
                  String number = "";
                  if (StringUtils.isNotEmpty(decimal)) {
                     number = decimal.trim();
                  }
                  if (StringUtils.isNotEmpty(separator) && StringUtils.isNotEmpty(precision)) {
                     number = number + separator.trim() + precision.trim();
                  }
                  String compactedCurrencyPattern = operator + currencySymbol + number + unitScale;
                  compactedPatternMap.put(compactedCurrencyPattern, groupStr);
               }
            }
         } else if (StringUtils.isNotEmpty(matcher.group(8))) {
            String operator = matcher.group(8).trim();
            if (operatorSymbolMap.containsKey(operator.toLowerCase())) {
               operator = operatorSymbolMap.get(operator.toLowerCase());
            } else if (operator.equalsIgnoreCase("More Than")) {
               operator = ">";
            }
            if (StringUtils.isNotEmpty(matcher.group(13))) {
               String currencySymbol = matcher.group(13).trim();
               // Get the group's captured text
               String groupStr = matcher.group().trim();
               if (StringUtils.isEmpty(groupStr)) {
                  continue;
               }

               // Check if unitscale is present, then normalize unit scale
               String unitScale = "";
               if (matcher.group(12) != null) {
                  unitScale = matcher.group(12).trim();
                  if (unitScale.toLowerCase().endsWith("s")) {
                     unitScale = unitScale.substring(0, unitScale.length() - 1);
                  }
                  if (unitScaleMap.containsKey(unitScale.toLowerCase())) {
                     unitScale = unitScaleMap.get(unitScale.toLowerCase());
                  }
               }
               // normalize the number
               String decimal = matcher.group(9);
               String separator = matcher.group(10);
               String precision = matcher.group(11);
               if (StringUtils.isNotEmpty(decimal) || StringUtils.isNotEmpty(separator)
                        && StringUtils.isNotEmpty(precision)) {
                  compactedUnitFound = true;
                  String number = "";
                  if (StringUtils.isNotEmpty(decimal)) {
                     number = decimal.trim();
                  }
                  if (StringUtils.isNotEmpty(separator) && StringUtils.isNotEmpty(precision)) {
                     number = number + separator.trim() + precision.trim();
                  }
                  String compactedCurrencyPattern = operator + number + unitScale + currencySymbol;
                  compactedPatternMap.put(compactedCurrencyPattern, groupStr);
               }
            }
         }
      }
      if (compactedUnitFound) {
         for (Entry<String, String> entry : compactedPatternMap.entrySet()) {
            userQuerySentence = userQuerySentence.replace(entry.getValue(), "~#" + entry.getKey() + "~#");
         }
      }
      return userQuerySentence;
   }

   /**
    * Method to return the normalized input user query sentence. It puts the spaces between each groups which are
    * matched by the given regex. NOTE: regex should be properly grouped
    * 
    * @param userQuerySentence
    * @return the normalized String
    */
   public static String normalizeByRegexPattern (String userQuerySentence, String regex) {
      StringBuffer newStr = new StringBuffer();
      StringTokenizer st = new StringTokenizer(userQuerySentence);
      while (st.hasMoreTokens()) {
         String temp = st.nextToken();
         Pattern pattern = Pattern.compile(regex.toLowerCase());
         Matcher matcher = pattern.matcher(temp.toLowerCase());
         boolean matchFound = matcher.find() && matcher.start() == 0 && matcher.end() == temp.length();
         if (matchFound) {
            temp = "";
            // Get all groups for this match
            // start the loop from 1 as 0 will always denote the complete string
            for (int i = 1; i <= matcher.groupCount(); i++) {
               // Get the group's captured text
               String groupStr = matcher.group(i);
               if (StringUtils.isEmpty(groupStr)) {
                  continue;
               }
               // Get the group's indices
               int groupStart = matcher.start(i);
               int groupEnd = matcher.end(i);
               if (!temp.contains(groupStr)) {
                  temp = temp + " " + groupStr;
               }
            }
         }
         newStr.append(temp.trim()).append(" ");
      }
      String newString = newStr.toString();
      return newString;
   }

   /**
    * This method checks for the special chars in each unmarked token and replaces it with space+token+space Note we use ~#
    * for marking the token in processForUnits for compacted currency pattern
    * 
    * @param str
    * @return the String
    */
   public static String processForSpecialChars (String str) {
      StringTokenizer st = new StringTokenizer(str);
      StringBuffer newStr = new StringBuffer();
      while (st.hasMoreTokens()) {
         String temp = st.nextToken();
         if (!ExecueStringUtil.isOperatorSymbol(temp) && temp.indexOf("=") != -1
                  && !(temp.startsWith("~#") && temp.endsWith("~#"))) {
            temp = temp.replaceAll("\\=", " = ");
         }
         newStr.append(temp).append(" ");
      }
      return newStr.toString();
   }

   /**
    * This method returns the default weight information with max weight and max quality
    * 
    * @return the WeightInformation
    */
   public static WeightInformation getDefaultWeightInformation () {
      WeightInformation weightInformation = new WeightInformation();
      weightInformation.setRecognitionWeight(NLPConstants.MAX_WEIGHT);
      weightInformation.setRecognitionQuality(NLPConstants.MAX_QUALITY);
      return weightInformation;
   }

   /**
    * This method returns the default weight information with half the max weight and full max quality
    * 
    * @return the WeightInformation
    */
   public static WeightInformation getDefaultWeightInformationForImplicitEntity () {
      WeightInformation weightInformation = new WeightInformation();
      weightInformation.setRecognitionWeight(NLPConstants.MAX_WEIGHT / 2);
      weightInformation.setRecognitionQuality(NLPConstants.MAX_QUALITY);
      return weightInformation;
   }

   /**
    * Group entities by ModelGroupId. Base entities will be part of each group. This method has to be used carefully as
    * it is not creating a separate group for base recognition entities.
    * 
    * @param recognitionEntities
    * @return the Map<Long, List<IWeightedEntity>>
    */
   public static Map<Long, List<IWeightedEntity>> groupRecEntitiesByModelAndBase (
            List<IWeightedEntity> recognitionEntities) {
      Map<Long, List<IWeightedEntity>> entitiesByModel = new HashMap<Long, List<IWeightedEntity>>(1);
      if (CollectionUtils.isEmpty(recognitionEntities)) {
         return entitiesByModel;
      }
      List<IWeightedEntity> baseEnList = new ArrayList<IWeightedEntity>(1);
      for (IWeightedEntity weightedEntity : recognitionEntities) {
         OntoEntity ontoEntity = (OntoEntity) weightedEntity;
         Long modelId = ontoEntity.getModelGroupId();
         if (modelId == null || modelId.equals(1L)) {
            baseEnList.add(weightedEntity);
            continue;
         }
         List<IWeightedEntity> enList = entitiesByModel.get(modelId);
         if (enList == null) {
            enList = new ArrayList<IWeightedEntity>(1);
            entitiesByModel.put(modelId, enList);
         }
         enList.add(weightedEntity);
      }
      if (baseEnList != null) {
         for (Entry<Long, List<IWeightedEntity>> entry : entitiesByModel.entrySet()) {
            entry.getValue().addAll(baseEnList);
         }
      }

      return entitiesByModel;
   }

   public static List<IWeightedEntity> getDefaultNonCentralConcepts (List<IWeightedEntity> recognitionEntities) {
      List<IWeightedEntity> defaultNonCentralConcepts = new ArrayList<IWeightedEntity>(1);
      for (IWeightedEntity weightedEntity : recognitionEntities) {
         RecognitionEntity recEntity = (RecognitionEntity) weightedEntity;
         if (recEntity.getPosition() < 0 && !recEntity.getFlags().containsKey(OntologyConstants.POPULATION_CONCEPT)) {
            defaultNonCentralConcepts.add(recEntity);
         }
      }
      return defaultNonCentralConcepts;

   }

   public static List<Association> getDefaultAssociations (List<Association> revisedAssocList) {
      List<Association> defaultPaths = new ArrayList<Association>(1);
      for (Association association : revisedAssocList) {
         if (association.isDefaultPath()) {
            defaultPaths.add(association);
         }
      }
      return defaultPaths;
   }

   /**
    * Method to return the set of concept, concept profile and type bed ids from the given list of recognition entities
    * 
    * @param recognitionEntities
    * @return the Set<Long>
    */
   public static Set<Long> getAllBedIdsForConceptTypeEntity (List<IWeightedEntity> recognitionEntities) {
      Set<Long> conceptAndTypeBedIds = new HashSet<Long>();
      if (CollectionUtils.isEmpty(recognitionEntities)) {
         return conceptAndTypeBedIds;
      }
      for (IWeightedEntity weightedEntity : recognitionEntities) {
         RecognitionEntity recognitionEntity = (RecognitionEntity) weightedEntity;
         // Add the concept BED id into the source & destination BED lists
         Long typeBedId = ((TypeEntity) recognitionEntity).getTypeBedId();
         conceptAndTypeBedIds.add(typeBedId);
         if (recognitionEntity instanceof ConceptEntity) {
            Long conceptBedId = null;
            if (recognitionEntity instanceof ProfileEntity) {
               if (recognitionEntity.getEntityType() == RecognitionEntityType.INSTANCE_PROFILE_ENTITY) {
                  InstanceProfileEntity entity = (InstanceProfileEntity) recognitionEntity;
                  conceptBedId = entity.getConceptBedId();
               } else {
                  conceptBedId = ((ConceptProfileEntity) recognitionEntity).getProfileID();
               }
            } else {
               conceptBedId = ((ConceptEntity) recognitionEntity).getConceptBedId();
            }
            // add the concept BED id into the source & destination BED lists
            if (conceptBedId != null) {
               conceptAndTypeBedIds.add(conceptBedId);
            }
         }
      }
      return conceptAndTypeBedIds;
   }

   /**
    * Method to return the list of list of continuous positions.
    * 
    * @param positions
    * @return the List<List<Integer>>
    */
   public static List<List<Integer>> getContinuousPositionsList (Set<Integer> positions) {
      List<List<Integer>> continuousPositionsList = new ArrayList<List<Integer>>();
      if (CollectionUtils.isEmpty(positions)) {
         return continuousPositionsList;
      }

      // Sort the input positions set
      List<Integer> positionList = new ArrayList<Integer>(positions);
      Collections.sort(positionList);

      int count = -1;
      // Prepare the list for the continuous position(s)
      List<Integer> continuousPositions = new ArrayList<Integer>();
      for (Integer position : positionList) {
         if (position != count) {
            if (!CollectionUtils.isEmpty(continuousPositions)) {
               continuousPositionsList.add(continuousPositions);
               continuousPositions = new ArrayList<Integer>();
            }
            count = position;
         }
         continuousPositions.add(position);
         count++;
      }
      if (!CollectionUtils.isEmpty(continuousPositions)) {
         continuousPositionsList.add(continuousPositions);
      }
      return continuousPositionsList;
   }

   /**
    * This method returns the list of recognition entities having the position related to rtpToCheck for the given input
    * list of recognition entities.
    * 
    * @param recognitionEntities
    * @param rtpToCheck
    * @return the List<IWeightedEntity>
    */
   public static List<IWeightedEntity> getRecognitionEntitiesForRelatedPosition (
            Collection<IWeightedEntity> recognitionEntities, ReferedTokenPosition rtpToCheck) {
      List<IWeightedEntity> relationRecs = new ArrayList<IWeightedEntity>();
      for (IWeightedEntity weightedEntity : recognitionEntities) {
         ReferedTokenPosition rtp = new ReferedTokenPosition(((RecognitionEntity) weightedEntity)
                  .getReferedTokenPositions());
         if (rtp.isOverLap(rtpToCheck)) {
            relationRecs.add(weightedEntity);
         }
      }
      return relationRecs;
   }
}