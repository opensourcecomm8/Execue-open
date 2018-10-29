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


package com.execue.nlp.bean.snowflake;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.execue.nlp.bean.ProcessorData;
import com.execue.nlp.bean.entity.RecognitionEntity;
import com.execue.nlp.bean.entity.SFLEntity;

/**
 * @author Kaliki
 * @since NLP 3.0
 */
public class SFLProcessorData extends ProcessorData {

   private List<SFLToken>                input;
   private List<SFLToken>                tagInput;
   private List<SFLToken>                interationInput;
   private List<SFLWord>                 sflSecondaryWordList       = new ArrayList<SFLWord>();
   private List<SFLWord>                 sflwordList                = new ArrayList<SFLWord>();
   private List<SFLWord>                 sflIngoreWordList          = new ArrayList<SFLWord>();
   private Map<Long, List<SFLEntity>>    existingSFlsById           = new HashMap<Long, List<SFLEntity>>(1);
   Map<Integer, List<RecognitionEntity>> existingEntitiesByPosition = new HashMap<Integer, List<RecognitionEntity>>(1);

   public List<SFLToken> getInput () {
      return input;
   }

   public void setInput (List<SFLToken> input) {
      this.input = input;
   }

   public List<SFLToken> getInterationInput () {
      return interationInput;
   }

   public void setInterationInput (List<SFLToken> interationInput) {
      this.interationInput = interationInput;
   }

   public List<SFLToken> getTagInput () {
      return tagInput;
   }

   public void setTagInput (List<SFLToken> tagInput) {
      this.tagInput = tagInput;
   }

   public List<SFLWord> getSflSecondaryWordList () {
      return sflSecondaryWordList;
   }

   public void setSflSecondaryWordList (List<SFLWord> sflSecondaryWordList) {
      this.sflSecondaryWordList = sflSecondaryWordList;
   }

   public List<SFLWord> getSflwordList () {
      return sflwordList;
   }

   public void setSflwordList (List<SFLWord> sflwordList) {
      this.sflwordList = sflwordList;
   }

   public List<SFLWord> getSflIngoreWordList () {
      return sflIngoreWordList;
   }

   public void setSflIngoreWordList (List<SFLWord> sflIngoreWordList) {
      this.sflIngoreWordList = sflIngoreWordList;
   }

   /**
    * @return the existingSFls
    */
   public Map<Long, List<SFLEntity>> getExistingSFlsById () {
      return existingSFlsById;
   }

   /**
    * @param existingSFls
    *           the existingSFls to set
    */
   public void setExistingSFlsById (Map<Long, List<SFLEntity>> existingSFlsById) {
      this.existingSFlsById = existingSFlsById;
   }

   /**
    * @return the existingEntitiesByPosition
    */
   public Map<Integer, List<RecognitionEntity>> getExistingEntitiesByPosition () {
      return existingEntitiesByPosition;
   }

   /**
    * @param existingEntitiesByPosition
    *           the existingEntitiesByPosition to set
    */
   public void setExistingEntitiesByPosition (Map<Integer, List<RecognitionEntity>> existingEntitiesByPosition) {
      this.existingEntitiesByPosition = existingEntitiesByPosition;
   }

}
