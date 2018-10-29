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
package com.execue.nlp.bean.matrix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.execue.core.common.bean.IWeightedEntity;
import com.execue.nlp.bean.entity.RecognizedCloudEntity;
import com.execue.nlp.util.NLPUtilities;

/**
 * class to keep track of the clouds for which a RecognitionEntity has already participated.
 * 
 * @author Nihar
 */
public class CloudParticipationMonitor {

   private Map<String, List<String>> cloudsProcessed;
   private Map<String, List<String>> cloudsProcessedForRecEntity;
   private Map<String, List<String>> cloudsProcessedWithDefaultForRecEntity;

   public CloudParticipationMonitor () {
      super();
      this.cloudsProcessed = new HashMap<String, List<String>>(1);
      this.cloudsProcessedForRecEntity = new HashMap<String, List<String>>(1);
      this.cloudsProcessedWithDefaultForRecEntity = new HashMap<String, List<String>>(1);
   }

   /**
    * @return the cloudsProcessedForRecEntity
    */
   public Map<String, List<String>> getCloudsProcessedForRecEntity () {
      return cloudsProcessedForRecEntity;
   }

   /**
    * @return the cloudsProcessedForRecEntity
    */
   public Map<String, List<String>> getCloudsProcessed () {
      return cloudsProcessed;
   }

   /**
    * @param cloudEntity as RecognizedCloudEntity
    * @return true/false
    */
   public boolean isCloudAlreadyProcessed (RecognizedCloudEntity cloudEntity) {
      String cloudIdWithRTP = cloudEntity.getCloud().getId() + ""
               + NLPUtilities.getReferredTokenPositions(cloudEntity.getRecognitionEntities());

      String cloudProcessedKey = getProcessedCloudKey(cloudEntity.getRecognitionEntities());
      List<String> cloudIdsWithRTP = cloudsProcessed.get(cloudProcessedKey);
      if (!CollectionUtils.isEmpty(cloudIdsWithRTP) && cloudIdsWithRTP.contains(cloudIdWithRTP)) {
         return true;
      }
      return false;
   }

   private String getProcessedCloudKey (List<IWeightedEntity> recognitionEntities) {
      StringBuilder sb = new StringBuilder();
      if (CollectionUtils.isEmpty(recognitionEntities)) {
         return sb.toString();
      }
      NLPUtilities.sortRecognitionEntitiesByRTP(recognitionEntities);
      for (IWeightedEntity weightedEntity : recognitionEntities) {
         sb.append(weightedEntity.toString());
      }
      return sb.toString();
   }

   /**
    * Add Cloud ID with RTPs against the recEntity. This is done so that we can identify that a recEntity is already
    * considered for this cloud.
    * 
    * @param weightedEntity
    * @param cloudIdWithRTP
    */
   public void addCloudProcessed (RecognizedCloudEntity cloudEntity) {
      String cloudIdWithRTP = cloudEntity.getCloud().getId() + ""
               + NLPUtilities.getReferredTokenPositions(cloudEntity.getRecognitionEntities());
      String processedCloudKey = getProcessedCloudKey(cloudEntity.getRecognitionEntities());
      List<String> cloudIdsWithRTP = cloudsProcessed.get(processedCloudKey);
      if (cloudIdsWithRTP == null) {
         cloudIdsWithRTP = new ArrayList<String>(1);
         cloudsProcessed.put(processedCloudKey, cloudIdsWithRTP);
      }
      cloudIdsWithRTP.add(cloudIdWithRTP);
   }

   /**
    * @param weightedEntity
    * @param cloudId
    * @return
    */
   public boolean isCloudProcessedForRecEntity (IWeightedEntity weightedEntity, String cloudIdWithRTP) {
      List<String> cloudIdsWithRTP = cloudsProcessedForRecEntity.get(weightedEntity.toString());
      if (!CollectionUtils.isEmpty(cloudIdsWithRTP) && cloudIdsWithRTP.contains(cloudIdWithRTP)) {
         return true;
      }
      return false;
   }

   /**
    * Add Cloud ID with RTPs against the recEntity. This is done so that we can identify that a recEntity is already
    * considered for this cloud.
    * 
    * @param weightedEntity
    * @param cloudIdWithRTP
    */
   public void addCloudAsPartcipatedForRecEntity (IWeightedEntity weightedEntity, String cloudIdWithRTP) {
      List<String> cloudIdsWithRTP = cloudsProcessedForRecEntity.get(weightedEntity.toString());
      if (cloudIdsWithRTP == null) {
         cloudIdsWithRTP = new ArrayList<String>(1);
         cloudsProcessedForRecEntity.put(weightedEntity.toString(), cloudIdsWithRTP);
      }
      cloudIdsWithRTP.add(cloudIdWithRTP);
   }

   /**
    * @return the cloudsProcessedWithDefaultForRecEntity
    */
   public Map<String, List<String>> getCloudsProcessedWithDefaultForRecEntity () {
      return cloudsProcessedWithDefaultForRecEntity;
   }

   /**
    * @param weightedEntity
    * @param cloudId
    * @return
    */
   public boolean isCloudProcessedWithDefaultForRecEntity (IWeightedEntity weightedEntity, String outputEntity) {
      List<String> entities = cloudsProcessedWithDefaultForRecEntity.get(weightedEntity.toString());
      if (!CollectionUtils.isEmpty(entities) && entities.contains(outputEntity)) {
         return true;
      }
      return false;
   }

   /**
    * @param weightedEntity
    * @param outputEntity
    */
   public void addCloudAsPartcipatedWithDefaultForRecEntity (IWeightedEntity weightedEntity, String outputEntity) {
      List<String> entities = cloudsProcessedWithDefaultForRecEntity.get(weightedEntity.toString());
      if (entities == null) {
         entities = new ArrayList<String>(1);
         cloudsProcessedWithDefaultForRecEntity.put(weightedEntity.toString(), entities);
      }
      entities.add(outputEntity);
   }
}
