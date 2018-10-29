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
import java.util.List;

import com.execue.core.common.bean.IWeightedEntity;

/**
 * @author kaliki
 */
public class ProcessorOutputToken {

   private int                   position;
   private List<IWeightedEntity> entities;
   private int                   state          = EXISTING_STATE;
   private String                word;

   public static int             VIRTUAL_STATE  = 1;
   public static int             EXISTING_STATE = 0;

   public int getPosition () {
      return position;
   }

   public void setPosition (int position) {
      this.position = position;
      if (position < 0)
         this.state = VIRTUAL_STATE;
   }

   public String getWord () {
      return word;
   }

   public void setWord (String word) {
      this.word = word;
   }

   public int getState () {
      return state;
   }

   public void setState (int state) {
      this.state = state;
   }

   public List<IWeightedEntity> getEntities () {
      return entities;
   }

   public void setEntities (List<IWeightedEntity> entities) {
      this.entities = entities;
   }

   public void addEntity (IWeightedEntity entity) {
      if (entities == null) {
         entities = new ArrayList<IWeightedEntity>();
      }
      entities.add(entity);
   }
}
