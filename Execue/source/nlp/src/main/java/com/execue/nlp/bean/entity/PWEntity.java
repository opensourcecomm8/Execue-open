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


package com.execue.nlp.bean.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.execue.core.common.bean.entity.RIParallelWord;
import com.execue.core.common.bean.nlp.RecognitionEntityType;

public class PWEntity extends TagEntity implements Cloneable, Serializable {

   Map<String, RIParallelWord> wordValues;
   Long                        contextId;

   public Map<String, RIParallelWord> getWordValues () {
      return wordValues;
   }

   public void setWordValues (Map<String, RIParallelWord> wordValues) {
      this.wordValues = wordValues;
   }

   @Override
   public Object clone () throws CloneNotSupportedException {
      PWEntity recognitionEntity = (PWEntity) super.clone();
      recognitionEntity.setContextId(this.contextId);
      if (wordValues != null) {
         recognitionEntity.wordValues = (Map<String, RIParallelWord>) ((HashMap<String, RIParallelWord>) wordValues)
                  .clone();
         recognitionEntity.entityType = RecognitionEntityType.getRecognitionEntityType(getEntityType().getValue());
      }
      return recognitionEntity;
   }

   @Override
   public String toString () {
      StringBuffer sb = new StringBuffer();
      sb.append(contextId).append("_");
      for (RIParallelWord pWord : wordValues.values()) {
         sb.append(pWord.getWord()).append('_');
      }
      sb.append(super.toString());
      return sb.toString();
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#equals(java.lang.Object)
    */
   @Override
   public boolean equals (Object obj) {
      return (obj instanceof PWEntity || obj instanceof String) && this.toString().equalsIgnoreCase(obj.toString());
   }

   @Override
   public int hashCode () {
      return this.toString().hashCode();
   } // Utility Methods

   public void addWordValue (RIParallelWord wordValue) {
      if (this.wordValues == null) {
         this.wordValues = new HashMap<String, RIParallelWord>();
      }
      this.wordValues.put(wordValue.getEquivalentWord().toLowerCase().trim(), wordValue);
   }

   public void addWordValues (List<RIParallelWord> wordValues) {
      if (this.wordValues == null) {
         this.wordValues = new HashMap<String, RIParallelWord>();
      }
      for (RIParallelWord parallelWord : wordValues) {
         addWordValue(parallelWord);
      }
   }

   /**
    * @return the contextId
    */
   public Long getContextId () {
      return contextId;
   }

   /**
    * @param contextId
    *           the contextId to set
    */
   public void setContextId (Long contextId) {
      this.contextId = contextId;
   }
}
