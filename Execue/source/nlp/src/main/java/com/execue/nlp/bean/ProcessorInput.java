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


package com.execue.nlp.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.execue.core.common.bean.IWeightedEntity;
import com.execue.core.common.bean.SearchFilter;
import com.execue.core.common.bean.entity.Model;
import com.execue.nlp.bean.entity.Association;
import com.execue.nlp.bean.entity.RecognitionEntity;
import com.execue.nlp.bean.entity.RecognizedCloudEntity;
import com.execue.nlp.bean.matrix.CloudParticipationMonitor;

/**
 * @author Abhijit
 * @since Jul 30, 2008 - 3:41:43 PM
 */
public class ProcessorInput {

   private Group                              group;
   private Model                              model;
   private SearchFilter                       searchFilter;
   private CloudParticipationMonitor          cloudParticipationMonitor;
   private List<IWeightedEntity>              recognitionEntities;
   private List<IWeightedEntity>              unrecognizedEntities;
   private List<IWeightedEntity>              outputRecognitionEntities;
   private List<IWeightedEntity>              processedRecognitions;
   private List<IWeightedEntity>              defaultedOutputRecognitionEntities;
   private List<IWeightedEntity>              processedRecognitionsWithDefaults;
   private List<IWeightedEntity>              unrecognizedBaseRecEntities;
   private List<IWeightedEntity>              recEntitiesToReConsider;           //Currently it holds tag entities processed during SFL processing(ignore words)
   private List<RecognizedCloudEntity>        appCloudEntities;
   private List<Set<Long>>                    convertibleBeds;
   private Map<Long, List<Association>>       modelAssociationMap;
   private Map<Long, List<RecognitionEntity>> recEntitiesByIdMap;
   private Map<Long, List<RecognitionEntity>> unifiedEntitiesByModelGroup;
   private Integer                            currentIteration;
   private int                                implicitRecognitionCounter;        // variable to maintain the implicit recognition counter for a possibility.
   private boolean                            fromArticle;
   private boolean                            skipLocationTypeRecognition;

   /**
    * @return the group
    */
   public Group getGroup () {
      return group;
   }

   /**
    * @param group
    *           the group to set
    */
   public void setGroup (Group group) {
      this.group = group;
   }

   /**
    * @return the model
    */
   public Model getModel () {
      return model;
   }

   /**
    * @param model
    *           the model to set
    */
   public void setModel (Model model) {
      this.model = model;
   }

   /**
    * @return the searchFilter
    */
   public SearchFilter getSearchFilter () {
      return searchFilter;
   }

   /**
    * @param searchFilter
    *           the searchFilter to set
    */
   public void setSearchFilter (SearchFilter searchFilter) {
      this.searchFilter = searchFilter;
   }

   /**
    * @return the cloudParticipationMonitor
    */
   public CloudParticipationMonitor getCloudParticipationMonitor () {
      return cloudParticipationMonitor;
   }

   /**
    * @param cloudParticipationMonitor
    *           the cloudParticipationMonitor to set
    */
   public void setCloudParticipationMonitor (CloudParticipationMonitor cloudParticipationMonitor) {
      this.cloudParticipationMonitor = cloudParticipationMonitor;
   }

   /**
    * @return the appCloudEntities
    */
   public List<RecognizedCloudEntity> getAppCloudEntities () {
      if (appCloudEntities == null) {
         appCloudEntities = new ArrayList<RecognizedCloudEntity>(1);
      }
      return appCloudEntities;
   }

   /**
    * @param appCloudEntities
    *           the appCloudEntities to set
    */
   public void setAppCloudEntities (List<RecognizedCloudEntity> appCloudEntities) {
      this.appCloudEntities = appCloudEntities;
   }

   /**
    * @return the unrecognizedEntities
    */
   public List<IWeightedEntity> getUnrecognizedEntities () {
      if (unrecognizedEntities == null) {
         return new ArrayList<IWeightedEntity>(1);
      }
      return unrecognizedEntities;
   }

   /**
    * @param unrecognizedEntities
    *           the unrecognizedEntities to set
    */
   public void setUnrecognizedEntities (List<IWeightedEntity> unrecognizedEntities) {
      this.unrecognizedEntities = unrecognizedEntities;
   }

   /**
    * @return the defaultedOutputRecognitionEntities
    */
   public List<IWeightedEntity> getDefaultedOutputRecognitionEntities () {
      if (defaultedOutputRecognitionEntities == null) {
         defaultedOutputRecognitionEntities = new ArrayList<IWeightedEntity>(1);
      }
      return defaultedOutputRecognitionEntities;
   }

   /**
    * @param defaultedOutputRecognitionEntities
    *           the defaultedOutputRecognitionEntities to set
    */
   public void setDefaultedOutputRecognitionEntities (List<IWeightedEntity> defaultedOutputRecognitionEntities) {
      this.defaultedOutputRecognitionEntities = defaultedOutputRecognitionEntities;
   }

   /**
    * @return the processedRecognitionsWithDefaults
    */
   public List<IWeightedEntity> getProcessedRecognitionsWithDefaults () {
      if (processedRecognitionsWithDefaults == null) {
         processedRecognitionsWithDefaults = new ArrayList<IWeightedEntity>(1);
      }
      return processedRecognitionsWithDefaults;
   }

   /**
    * @param processedRecognitionsWithDefaults
    *           the processedRecognitionsWithDefaults to set
    */
   public void setProcessedRecognitionsWithDefaults (List<IWeightedEntity> processedRecognitionsWithDefaults) {
      this.processedRecognitionsWithDefaults = processedRecognitionsWithDefaults;
   }

   /**
    * @return the unrecognizedBaseRecEntities
    */
   public List<IWeightedEntity> getUnrecognizedBaseRecEntities () {
      return unrecognizedBaseRecEntities;
   }

   /**
    * @param unrecognizedBaseRecEntities
    *           the unrecognizedBaseRecEntities to set
    */
   public void setUnrecognizedBaseRecEntities (List<IWeightedEntity> unrecognizedBaseRecEntities) {
      this.unrecognizedBaseRecEntities = unrecognizedBaseRecEntities;
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

   public void addOutputRecognitionEntity (IWeightedEntity entity) {
      if (outputRecognitionEntities == null) {
         outputRecognitionEntities = new ArrayList<IWeightedEntity>();
      }
      outputRecognitionEntities.add(entity);
   }

   /**
    * @return the recEntitiesToBeConsider
    */
   public List<IWeightedEntity> getRecEntitiesToReConsider () {
      if (recEntitiesToReConsider == null) {
         recEntitiesToReConsider = new ArrayList<IWeightedEntity>();
      }
      return recEntitiesToReConsider;
   }

   /**
    * @param recEntitiesToReConsider the recEntitiesToReConsider to set
    */
   public void setRecEntitiesToReConsider (List<IWeightedEntity> recEntitiesToReConsider) {
      this.recEntitiesToReConsider = recEntitiesToReConsider;
   }

   /**
    * @return the recognitionEntities
    */
   public List<IWeightedEntity> getRecognitionEntities () {
      if (recognitionEntities == null) {
         recognitionEntities = new ArrayList<IWeightedEntity>();
      }
      return recognitionEntities;
   }

   /**
    * @param recognitionEntities
    *           the recognitionEntities to set
    */
   public void setRecognitionEntities (List<IWeightedEntity> recognitionEntities) {
      this.recognitionEntities = recognitionEntities;
   }

   /**
    * @return the outputRecognitionEntities
    */
   public List<IWeightedEntity> getOutputRecognitionEntities () {
      if (outputRecognitionEntities == null) {
         outputRecognitionEntities = new ArrayList<IWeightedEntity>(1);
      }
      return outputRecognitionEntities;
   }

   /**
    * @param outputRecognitionEntities
    *           the outputRecognitionEntities to set
    */
   public void setOutputRecognitionEntities (List<IWeightedEntity> outputRecognitionEntities) {
      this.outputRecognitionEntities = outputRecognitionEntities;
   }

   /**
    * @return the processedRecognitions
    */
   public List<IWeightedEntity> getProcessedRecognitions () {
      if (processedRecognitions == null) {
         processedRecognitions = new ArrayList<IWeightedEntity>(1);
      }
      return processedRecognitions;
   }

   /**
    * @param processedRecognitions
    *           the processedRecognitions to set
    */
   public void setProcessedRecognitions (List<IWeightedEntity> processedRecognitions) {
      this.processedRecognitions = processedRecognitions;
   }

   /**
    * @return the unifiedEntitiesByModelGroup
    */
   public Map<Long, List<RecognitionEntity>> getUnifiedEntitiesByModelGroup () {
      return unifiedEntitiesByModelGroup;
   }

   /**
    * @param unifiedEntitiesByModelGroup
    *           the unifiedEntitiesByModelGroup to set
    */
   public void setUnifiedEntitiesByModelGroup (Map<Long, List<RecognitionEntity>> unifiedEntitiesByModelGroup) {
      this.unifiedEntitiesByModelGroup = unifiedEntitiesByModelGroup;
   }

   /**
    * @return the recEntitiesByIdMap
    */
   public Map<Long, List<RecognitionEntity>> getRecEntitiesByIdMap () {
      if (recEntitiesByIdMap == null) {
         recEntitiesByIdMap = new HashMap<Long, List<RecognitionEntity>>(1);
      }
      return recEntitiesByIdMap;
   }

   /**
    * @param recEntitiesByIdMap
    *           the recEntitiesByIdMap to set
    */
   public void setRecEntitiesByIdMap (Map<Long, List<RecognitionEntity>> recEntitiesByIdMap) {
      this.recEntitiesByIdMap = recEntitiesByIdMap;
   }

   /**
    * @return the modelAssociationMap
    */
   public Map<Long, List<Association>> getModelAssociationMap () {
      if (modelAssociationMap == null) {
         modelAssociationMap = new HashMap<Long, List<Association>>(1);
      }
      return modelAssociationMap;
   }

   /**
    * @param modelAssociationMap
    *           the modelAssociationMap to set
    */
   public void setModelAssociationMap (Map<Long, List<Association>> modelAssociationMap) {
      this.modelAssociationMap = modelAssociationMap;
   }

   /**
    * Method to add the Association for a model.
    * 
    * @param modelId
    * @param association
    */
   public void addAssociationForModel (Long modelId, Association association) {
      if (modelAssociationMap == null) {
         modelAssociationMap = new HashMap<Long, List<Association>>(1);
      }
      List<Association> associationList = modelAssociationMap.get(modelId);
      if (associationList == null) {
         associationList = new ArrayList<Association>(1);
         modelAssociationMap.put(modelId, associationList);
      }
      associationList.add(association);
   }

   /**
    * @return the currentIteration
    */
   public Integer getCurrentIteration () {
      return currentIteration;
   }

   /**
    * @param currentIteration
    *           the currentIteration to set
    */
   public void setCurrentIteration (Integer currentIteration) {
      this.currentIteration = currentIteration;
   }

   /**
    * This method increments the current iteration by one
    */
   public void incrementIteration () {
      ++this.currentIteration;
   }

   /**
    * @return the implicitRecognitionCounter
    */
   public int getImplicitRecognitionCounter () {
      return implicitRecognitionCounter;
   }

   /**
    * @return the implicitRecognitionCounter
    */
   public int getNextImplicitRecognitionCounter () {
      return --implicitRecognitionCounter;
   }

   /**
    * @param implicitRecognitionCounter
    *           the implicitRecognitionCounter to set
    */
   public void setImplicitRecognitionCounter (int implicitRecognitionCounter) {
      if (implicitRecognitionCounter >= 0) {
         return;
      }
      this.implicitRecognitionCounter = implicitRecognitionCounter;
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