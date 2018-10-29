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
package com.execue.core.common.bean.nlp;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Nihar
 *
 */
public class TokenCandidate {
      Integer               position;
      String                word;
      List<CandidateEntity> entities;

      /**
       * @return the position
       */
      public Integer getPosition () {
         return position;
      }

      /**
       * @param position the position to set
       */
      public void setPosition (Integer position) {
         this.position = position;
      }

      /**
       * @return the word
       */
      public String getWord () {
         return word;
      }

      /**
       * @param word the word to set
       */
      public void setWord (String word) {
         this.word = word;
      }

      /**
       * @return the entities
       */
      public List<CandidateEntity> getEntities () {
         if(entities ==  null){
            entities = new ArrayList<CandidateEntity>();
         }
         return entities;
      }

      /**
       * @return the entities
       */
      public void addEntity (CandidateEntity entity) {
         if(entities ==  null){
            entities = new ArrayList<CandidateEntity>();
         }
         if(!entities.contains(entity)) {
            entities.add(entity);
         }
      }

      /**
       * @param entities the entities to set
       */
      public void setEntities (List<CandidateEntity> entities) {
         this.entities = entities;
      }

}
