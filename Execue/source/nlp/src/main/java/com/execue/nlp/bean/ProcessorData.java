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
 * Created on Aug 26, 2008
 */
package com.execue.nlp.bean;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.execue.core.common.bean.IWeightedEntity;
import com.execue.core.common.bean.SearchFilter;
import com.execue.core.common.bean.entity.Model;
import com.execue.nlp.bean.entity.RecognitionEntity;

/**
 * @author kaliki
 */
public class ProcessorData {

   private List<ProcessorOutputToken> output;
   private Group                      group;
   private Model                      model;
   private SearchFilter               searchFilter;

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

   public Group getGroup () {
      return group;
   }

   public void setGroup (Group group) {
      this.group = group;
   }

   public List<ProcessorOutputToken> getOutput () {
      return output;
   }

   public void addToOutput (ProcessorOutputToken token) {
      if (output == null) {
         output = new ArrayList<ProcessorOutputToken>();
      }
      output.add(token);
   }

   public void addToOutput (int position, List<IWeightedEntity> entities) {
      for (IWeightedEntity entity : entities) {
         addToOutput(position, entity);
      }
   }

   public void addToOutput (int position, IWeightedEntity entity) {
      addToOutput(position, entity, new ArrayList<String>());
   }

   public void addToOutput (int position, IWeightedEntity entity, String flag) {
      List<String> flags = new ArrayList<String>(1);
      flags.add(flag);
      addToOutput(position, entity, flags);
   }

   public void addToOutput (int position, IWeightedEntity entity, List<String> flags) {
      if (output == null) {
         output = new ArrayList<ProcessorOutputToken>();
      }
      ProcessorOutputToken outToken = getTokenByPositionIfExists(position);
      if (entity instanceof RecognitionEntity) {
         if (CollectionUtils.isEmpty(((RecognitionEntity) entity).getReferedTokenPositions())) {
            ((RecognitionEntity) entity).addReferedTokenPosition(position);
         }
         ((RecognitionEntity) entity).setStartPosition(position);
         ((RecognitionEntity) entity).setEndPosition(position);
         ((RecognitionEntity) entity).setFlags(flags);
      }
      outToken.addEntity(entity);
   }

   private ProcessorOutputToken getTokenByPositionIfExists (int position) {
      ProcessorOutputToken outToken = null;
      for (ProcessorOutputToken token : output) {
         if (token.getPosition() == position) {
            outToken = token;
            break;
         }
      }
      if (outToken == null) {
         outToken = new ProcessorOutputToken();
         outToken.setPosition(position);
         output.add(outToken);
      }
      return outToken;
   }

   public void addToOutput (int start, int end, RecognitionEntity entity) {
      addToOutput(start, end, entity, new ArrayList<String>());
   }

   public void addToOutput (int start, int end, List<RecognitionEntity> entities) {
      addToOutput(start, end, entities, new ArrayList<String>());
   }

   public void addToOutput (int start, int end, RecognitionEntity entity, List<String> flags) {
      List<RecognitionEntity> newList = new ArrayList<RecognitionEntity>(1);
      newList.add(entity);
      addToOutput(start, end, newList, flags);
   }

   // TODO: NK: Need to check with nihar/abhijit for the logic
   public void addToOutput (int start, int end, List<RecognitionEntity> entities, List<String> flags) {
      if (output == null) {
         output = new ArrayList<ProcessorOutputToken>();
      }

      RecognitionEntity ent = entities.get(0); // NK: Not sure if this is correct??
      // ent.setRecognitionEntities(entities);
      ent.setStartPosition(start);
      ent.setEndPosition(end);
      ent.setFlags(flags);

      List<ProcessorOutputToken> targetTokens = new ArrayList<ProcessorOutputToken>(1);
      for (int pos = start; pos <= end; pos++) {
         ProcessorOutputToken outToken = getTokenByPositionIfExists(pos);
         if (outToken != null) {
            targetTokens.add(outToken);
         } else {
            outToken = new ProcessorOutputToken();
            outToken.setPosition(pos);
            this.output.add(outToken);
            targetTokens.add(outToken);
         }
      }
      for (ProcessorOutputToken token : targetTokens) {
         token.addEntity(ent);
      }
   }

   public Model getModel () {
      return model;
   }

   public void setModel (Model model) {
      this.model = model;
   }

}
