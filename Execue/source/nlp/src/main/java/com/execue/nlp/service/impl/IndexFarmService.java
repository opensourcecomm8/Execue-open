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
package com.execue.nlp.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.execue.core.common.bean.WeightInformation;
import com.execue.core.common.bean.entity.KeyWord;
import com.execue.core.common.bean.entity.ParallelWord;
import com.execue.core.common.bean.nlp.NLPConstants;
import com.execue.core.common.bean.nlp.RecognitionEntityType;
import com.execue.core.common.bean.security.User;
import com.execue.core.common.type.ParallelWordType;
import com.execue.nlp.bean.entity.EntityFactory;
import com.execue.nlp.bean.entity.InflectionEntity;
import com.execue.nlp.bean.entity.InflectionType;
import com.execue.nlp.bean.entity.RecognitionEntity;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.PreferencesException;
import com.execue.swi.service.IPreferencesManagementService;
import com.execue.swi.service.IPreferencesRetrievalService;
import com.execue.util.algorithm.Inflector;

/**
 * @author Nihar
 */
public class IndexFarmService {

   private IPreferencesRetrievalService  preferencesRetrievalService;

   private IPreferencesManagementService preferencesManagementService;

   private VerbFormServiceImpl           verbFormService;

   /**
    * @return the verbFormServiceImpl
    */
   public VerbFormServiceImpl getVerbFormService () {
      return verbFormService;
   }

   /**
    * @param verbFormServiceImpl
    *           the verbFormServiceImpl to set
    */
   public void setVerbFormService (VerbFormServiceImpl verbFormService) {
      this.verbFormService = verbFormService;
   }

   public InflectionEntity processForInflectorEntity (RecognitionEntity recEntity) {
      Inflector inflector = new Inflector();
      if (recEntity.getEntityType() == RecognitionEntityType.GENERAL_ENTITY) {
         String nlpTag = recEntity.getNLPtag();
         if (nlpTag.equalsIgnoreCase(NLPConstants.NLP_TAG_POS_NOUN)
                  || nlpTag.equalsIgnoreCase(NLPConstants.NLP_TAG_POS_ADVERB)
                  || nlpTag.equalsIgnoreCase(NLPConstants.NLP_TAG_POS_NOUN)
                  || nlpTag.equalsIgnoreCase(NLPConstants.NLP_TAG_POS_PLURAL_NOUN)
                  || nlpTag.equalsIgnoreCase(NLPConstants.NLP_TAG_POS_PLURAL_PROPER_NOUN)
                  || nlpTag.equalsIgnoreCase(NLPConstants.NLP_TAG_POS_PREDETRMINER)
                  || nlpTag.equalsIgnoreCase(NLPConstants.NLP_TAG_POS_VERB_BASE_FORM)
                  || nlpTag.equalsIgnoreCase(NLPConstants.NLP_TAG_POS_VERB_PRESENT_PARTICIPLE)) {

            String word = recEntity.getWord();
            String singular = inflector.singularize(word);
            if (!singular.equalsIgnoreCase(word)) {
               return getInflectionEntity(singular, InflectionType.SINGULAR);
            } else {
               String plural = inflector.pluralize(word);
               if (!plural.equalsIgnoreCase(word)) {
                  return getInflectionEntity(plural, InflectionType.PLURAL);
               }
            }
         }
      }
      return null;
   }

   public List<InflectionEntity> processForVerbForms (RecognitionEntity recEntity) {
      if (recEntity.getEntityType() == RecognitionEntityType.GENERAL_ENTITY) {
         String nlpTag = recEntity.getNLPtag();
         if (nlpTag.equals(NLPConstants.NLP_TAG_POS_VERB_BASE_FORM)
                  || nlpTag.equals(NLPConstants.NLP_TAG_POS_VERB_PAST_TENSE)
                  || nlpTag.equals(NLPConstants.NLP_TAG_POS_VERB_PRESENT_PARTICIPLE)
                  || nlpTag.equals(NLPConstants.NLP_TAG_POS_VERB_PAST_PARTICIPLE)
                  || nlpTag.equals(NLPConstants.NLP_TAG_POS_VERB_NON_3D_PERSON_PRESENT_SINGULAR)
                  || nlpTag.equals(NLPConstants.NLP_TAG_POS_VERB_3D_PERSON_PRESENT_SINGULAR)) {
            List<InflectionEntity> entities = new ArrayList<InflectionEntity>(1);
            Set<String> verbForms = new HashSet<String>(1);
            String word = recEntity.getWord();
            String pastParticiple = getVerbFormService().getPastParticiple(word);
            if (!word.equalsIgnoreCase(pastParticiple)) {
               verbForms.add(pastParticiple);
            }

            String presentParticiple = getVerbFormService().getPresentParticiple(word);
            if (!word.equalsIgnoreCase(presentParticiple)) {
               verbForms.add(presentParticiple);
            }
            String simplePast = getVerbFormService().getSimplePast(word);
            if (!word.equalsIgnoreCase(simplePast)) {
               verbForms.add(simplePast);
            }
            String simpleVerbFromPresentParticiple = getVerbFormService().getSimpleVerbFromPresentParticiple(word);
            if (!word.equalsIgnoreCase(simpleVerbFromPresentParticiple)) {
               verbForms.add(simpleVerbFromPresentParticiple);
            }
            String verbFromPastParticiple = verbFormService.getVerbFromPastParticiple(word);
            if (!word.equalsIgnoreCase(verbFromPastParticiple)) {
               verbForms.add(verbFromPastParticiple);
            }
            String verbFromSimplePast = getVerbFormService().getVerbFromSimplePast(word);
            if (!word.equalsIgnoreCase(verbFromSimplePast)) {
               verbForms.add(verbFromSimplePast);
            }
            for (String verbForm : verbForms) {
               InflectionEntity inflectionEntity = getInflectionEntity(verbForm, InflectionType.VERB_TENSE);
               entities.add(inflectionEntity);
            }
            return entities;
         }
      }
      return null;
   }

   private InflectionEntity getInflectionEntity (String inflectionWord, InflectionType inflectionType) {
      InflectionEntity inflectionEntity = (InflectionEntity) EntityFactory.getRecognitionEntity(
               RecognitionEntityType.INFLECTION_ENTITY, inflectionWord, NLPConstants.NLP_TAG_PRALLEL_WORD, null);
      inflectionEntity.setInflectionType(inflectionType);
      inflectionEntity.setTokenAltered(true);
      WeightInformation weightInformation = new WeightInformation();
      weightInformation.setRecognitionQuality(0.95);
      weightInformation.setRecognitionWeight(NLPConstants.MAX_WEIGHT);
      inflectionEntity.setWeightInformation(weightInformation);
      return inflectionEntity;
   }

   public void createInflectionPWWordsForKeyWord (Long keyWordId, User user) throws KDXException {
      try {
         KeyWord keyWord = getPreferencesRetrievalService().getKeyWord(keyWordId);
         List<ParallelWord> parallelWordsForKeyWord = getPreferencesRetrievalService().getParallelWordsForKeyWord(
                  keyWordId);
         Inflector inflector = new Inflector();
         String inflection = inflector.singularize(keyWord.getWord());
         if (inflection.equalsIgnoreCase(keyWord.getWord())) {
            inflection = inflector.pluralize(keyWord.getWord());
         }
         boolean pwWordAlreadyExists = false;
         for (ParallelWord pwWord : parallelWordsForKeyWord) {
            if (pwWord.getParallelWord().equalsIgnoreCase(inflection)) {
               pwWordAlreadyExists = true;
               break;
            }
         }
         if (!pwWordAlreadyExists) {
            ParallelWord parallelWord = new ParallelWord();
            parallelWord.setKeyWord(keyWord);
            parallelWord.setUser(user);
            parallelWord.setParallelWord(inflection);
            parallelWord.setPwdType(ParallelWordType.Inflection);
            parallelWord.setQuality(0.95);
            parallelWord.setPopularity(0L);
            getPreferencesManagementService().createParallelWord(parallelWord);
         }
      } catch (PreferencesException e) {
         throw new KDXException(e.getCode(), e);
      }
   }

   public IPreferencesRetrievalService getPreferencesRetrievalService () {
      return preferencesRetrievalService;
   }

   public void setPreferencesRetrievalService (IPreferencesRetrievalService preferencesRetrievalService) {
      this.preferencesRetrievalService = preferencesRetrievalService;
   }

   public IPreferencesManagementService getPreferencesManagementService () {
      return preferencesManagementService;
   }

   public void setPreferencesManagementService (IPreferencesManagementService preferencesManagementService) {
      this.preferencesManagementService = preferencesManagementService;
   }

}
