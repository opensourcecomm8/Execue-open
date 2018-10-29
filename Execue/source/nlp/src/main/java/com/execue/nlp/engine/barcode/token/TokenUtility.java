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


package com.execue.nlp.engine.barcode.token;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.execue.core.common.bean.IWeightedEntity;
import com.execue.core.common.bean.entity.RIParallelWord;
import com.execue.core.common.type.BehaviorType;
import com.execue.core.util.ExecueStringUtil;
import com.execue.nlp.bean.NLPToken;
import com.execue.nlp.bean.entity.ConceptEntity;
import com.execue.nlp.bean.entity.InstanceEntity;
import com.execue.nlp.bean.entity.OntoEntity;
import com.execue.nlp.bean.entity.PWEntity;
import com.execue.nlp.bean.entity.RecognitionEntity;
import com.execue.nlp.bean.entity.SFLEntity;
import com.execue.nlp.bean.entity.TypeEntity;
import com.execue.util.StringUtilities;

/**
 * User: Abhijti Date: Aug 27, 2008 Time: 6:14:28 PM
 */
public class TokenUtility {

   // Other Methods

   public static String getCurrentWordValue (NLPToken token) {
      String word = null;
      if (token.getRecognitionEntities() == null || token.getRecognitionEntities().isEmpty()) {
         // TODO Should never happen, if this happens throw exception
      } else {
         RecognitionEntity entity = token.getDefaultRecognitionEntity();
         if (entity instanceof InstanceEntity) {
            word = ((InstanceEntity) entity).getDefaultInstanceValue();
         } else {
            word = entity.getWord();
         }
      }
      return word;
   }

   public static List<String> getAllPossibleWordValues (NLPToken token) {
      List<String> wordList = new ArrayList<String>(1);
      String word = token.getDefaultRecognitionEntity().getWord();
      if (token.getRecognitionEntities() != null && !token.getRecognitionEntities().isEmpty()) {

         IWeightedEntity entity = token.getDefaultRecognitionEntity();
         getWordForEntity(wordList, entity, new HashMap<String, RIParallelWord>(1), true);
      } else {
         wordList.add(word);
      }
      return wordList;
   }

   /**
    * @param wordList
    * @param entity
    * @param linguisticRootParallelWordMap
    * @param b
    */
   public static void getWordForEntity (List<String> wordList, IWeightedEntity entity,
            Map<String, RIParallelWord> linguisticRootParallelWordMap, boolean addLinguisticRoot) {
      String str;
      if (entity instanceof SFLEntity) {
         str = ((SFLEntity) entity).getSflName();
         wordList.add(str);
         String linguisticRoot = StringUtilities.getLinguisticRoot(str);
         if (addLinguisticRoot && !linguisticRoot.equalsIgnoreCase(str)) {
            wordList.add(linguisticRoot);
         }
      } else if (entity instanceof OntoEntity) {
         str = ((OntoEntity) entity).getOntoName();
         wordList.add(str);
         String linguisticRoot = StringUtilities.getLinguisticRoot(str);
         if (addLinguisticRoot && !linguisticRoot.equalsIgnoreCase(str)) {
            wordList.add(linguisticRoot);
         }
      } else if (entity instanceof PWEntity) {
         Map<String, RIParallelWord> pWords = ((PWEntity) entity).getWordValues();
         for (String equivalentWord : pWords.keySet()) {
            str = equivalentWord;
            wordList.add(str);
            String linguisticRoot = StringUtilities.getLinguisticRoot(str);
            if (addLinguisticRoot && !linguisticRoot.equalsIgnoreCase(str)) {
               wordList.add(linguisticRoot);
               linguisticRootParallelWordMap.put(linguisticRoot, pWords.get(str));
            }
         }
      } else if (entity instanceof RecognitionEntity) {
         str = ((RecognitionEntity) entity).getWord();
         wordList.add(str);
         String linguisticRoot = StringUtilities.getLinguisticRoot(str);
         if (addLinguisticRoot && !linguisticRoot.equalsIgnoreCase(str)) {
            wordList.add(linguisticRoot);
         }
      }
   }

   public static String getStringFormOfRecognitionEntity (RecognitionEntity recEntity) {

      StringBuilder taggedWord = new StringBuilder();

      taggedWord.append("Token@word:");
      String word = "";
      if (recEntity instanceof InstanceEntity) {
         if (!CollectionUtils.isEmpty(((InstanceEntity) recEntity).getInstanceInformations())) {
            word = word + ((InstanceEntity) recEntity).getDefaultInstanceValue();
         }
      } else if (recEntity instanceof ConceptEntity) {
         word = recEntity.getWord();
      } else {
         word = recEntity.getWord();
      }
      word = ExecueStringUtil.compactString(word);
      taggedWord.append(word);

      //If it is not from onto entity hierarchy, then return  
      if (!(recEntity instanceof OntoEntity)) {
         return taggedWord.toString();
      }

      // If it is from the onto entity hierarchy then get below information also
      taggedWord.append("#type:");
      taggedWord.append(((TypeEntity) recEntity).getTypeDisplayName());

      taggedWord.append("#concept:");
      if (recEntity instanceof ConceptEntity) {
         taggedWord.append(((ConceptEntity) recEntity).getConceptDisplayName());
      } else {
         taggedWord.append("null");
      }
      taggedWord.append("#nlpTag:");
      String nlpTag = recEntity.getNLPtag();
      if (StringUtils.isBlank(nlpTag)) {
         nlpTag = "null";
      }
      taggedWord.append(nlpTag);

      taggedWord.append("#behavior:");
      String behavior = getPossibleBehaviorComponents(((OntoEntity) recEntity).getBehaviors());
      if (StringUtils.isBlank(behavior)) {
         behavior = "null";
      }
      taggedWord.append(behavior);

      return taggedWord.toString();
   }

   private static String getPossibleBehaviorComponents (Collection<BehaviorType> behaviorComponents) {
      StringBuilder sb = new StringBuilder();
      if (CollectionUtils.isEmpty(behaviorComponents)) {
         return sb.toString();
      }
      for (BehaviorType behavior : behaviorComponents) {
         sb.append(behavior).append("~");
      }
      return sb.toString();
   }

   public static String getLatestOntConcept (NLPToken word) {
      if (!(word.getDefaultRecognitionEntity() instanceof OntoEntity)) {
         return null;
      }
      return ((OntoEntity) word.getDefaultRecognitionEntity()).getOntoName();
   }

   public static Long getLatestOntConceptID (NLPToken word) {
      if (!(word.getDefaultRecognitionEntity() instanceof OntoEntity)) {
         return null;
      }
      return ((ConceptEntity) word.getDefaultRecognitionEntity()).getConceptBedId();
   }

   public static String getLatestGroupConcept (NLPToken word) {
      if (!(word.getDefaultRecognitionEntity() instanceof OntoEntity)) {
         return null;
      }
      return ((OntoEntity) word.getDefaultRecognitionEntity()).getOntoName();
   }

   public static int getPos (NLPToken word) {
      return word.getDefaultRecognitionEntity().getPosition();
   }

   /**
    * Method to get the list of possible word for SFL processor. It iterate through all the recognition entities and add
    * the word to the word list for that token.
    * 
    * @param recognitionEntity
    * @param wordEntitiesMap
    * @return the List<String>
    */
   public static List<String> getAllPossibleWordValuesForSFLSemanticScoping (RecognitionEntity recognitionEntity,
            Map<String, List<RecognitionEntity>> wordEntitiesMap) {
      List<String> wordList = new ArrayList<String>(1);
      if (recognitionEntity instanceof PWEntity) {
         Map<String, RIParallelWord> pWords = ((PWEntity) recognitionEntity).getWordValues();
         for (RIParallelWord pWord : pWords.values()) {
            String word = pWord.getEquivalentWord();
            if (!pWord.getWord().contains(word + " ") && !pWord.getWord().contains(" " + word)) {
               wordList.add(word);
               List<RecognitionEntity> list = wordEntitiesMap.get(word.toLowerCase());
               if (list == null) {
                  list = new ArrayList<RecognitionEntity>(1);
                  wordEntitiesMap.put(word.toLowerCase(), list);
               }
               list.add(recognitionEntity);
            }
         }
      } else {
         String word = recognitionEntity.getWord();
         wordList.add(word);
         List<RecognitionEntity> list = wordEntitiesMap.get(word.toLowerCase());
         if (list == null) {
            list = new ArrayList<RecognitionEntity>(1);
            wordEntitiesMap.put(word.toLowerCase(), list);
         }
         list.add(recognitionEntity);
      }

      return wordList;
   }

   /**
    * Method to get the map of recognition entity by possible linguistic root word or words in case of recognition
    * entity is parallel word entity. In case of parallel word entity, will also populate the linguisticRootPWMap Map
    * where linguistic root word is key and RIParallelWord is value similar to equivalent word, RIParallelWord map.
    * 
    * @param token
    * @param linguisticRootPWMap
    * @param recEntitiesByLinguisticRoot
    * @return the Map<String, RecognitionEntity>
    */
   public static List<String> getAllPossibleLinguisticRootWordValuesForSFLSemanticScoping (
            RecognitionEntity recognitionEntity, Map<String, RIParallelWord> linguisticRootPWMap,
            Map<String, List<RecognitionEntity>> recEntitiesByLinguisticRoot) {
      List<String> linguisticRoots = new ArrayList<String>(1);
      if (recognitionEntity instanceof PWEntity) {
         Map<String, RIParallelWord> pWords = ((PWEntity) recognitionEntity).getWordValues();
         for (RIParallelWord pWord : pWords.values()) {
            String word = pWord.getEquivalentWord();
            String linguisticRoot = StringUtilities.getLinguisticRoot(word);
            if (!linguisticRoot.equalsIgnoreCase(word)) {
               linguisticRoots.add(linguisticRoot);
               List<RecognitionEntity> list = recEntitiesByLinguisticRoot.get(linguisticRoot.toLowerCase());
               if (list == null) {
                  list = new ArrayList<RecognitionEntity>(1);
                  recEntitiesByLinguisticRoot.put(linguisticRoot.toLowerCase(), list);
               }
               list.add(recognitionEntity);
               linguisticRootPWMap.put(linguisticRoot.toLowerCase(), pWord);
            }
         }
      } else {
         String word = recognitionEntity.getWord();
         String linguisticRoot = StringUtilities.getLinguisticRoot(word);
         if (!linguisticRoot.equalsIgnoreCase(word)) {
            linguisticRoots.add(linguisticRoot);
            List<RecognitionEntity> list = recEntitiesByLinguisticRoot.get(linguisticRoot.toLowerCase());
            if (list == null) {
               list = new ArrayList<RecognitionEntity>(1);
               recEntitiesByLinguisticRoot.put(linguisticRoot.toLowerCase(), list);
            }
            list.add(recognitionEntity);
         }
      }
      return linguisticRoots;
   }

   public static List<String> getAllPossibleWordValuesForPWSemanticScoping (NLPToken token) {
      String str = null;
      List<String> wordList = new ArrayList<String>(1);
      String word = token.getDefaultRecognitionEntity().getWord();
      if (token.getRecognitionEntities() != null && !token.getRecognitionEntities().isEmpty()) {

         for (RecognitionEntity entity : token.getRecognitionEntities()) {
            if (entity instanceof SFLEntity) {
               str = ((SFLEntity) entity).getSflName();
               wordList.add(str);
               String linguisticRoot = StringUtilities.getLinguisticRoot(str);
               if (!linguisticRoot.equalsIgnoreCase(str)) {
                  wordList.add(linguisticRoot);
               }
            } else if (entity instanceof PWEntity) {

            } else if (entity instanceof RecognitionEntity) {
               str = entity.getWord();
               wordList.add(str);
               String linguisticRoot = StringUtilities.getLinguisticRoot(str);
               if (!linguisticRoot.equalsIgnoreCase(str)) {
                  wordList.add(linguisticRoot);
               }
            }
         }
      } else {
         wordList.add(word);
      }
      return wordList;
   }

   public static List<String> getRecognitionEntityAsString (List<IWeightedEntity> recognitionEntities) {
      List<String> rulePatternInputTokensAsStrings = new ArrayList<String>();
      String tempString;
      for (IWeightedEntity recognitionEntity : recognitionEntities) {
         tempString = getStringFormOfRecognitionEntity((RecognitionEntity) recognitionEntity);
         rulePatternInputTokensAsStrings.add(tempString);
      }
      return rulePatternInputTokensAsStrings;
   }
}
