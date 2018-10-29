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

import java.util.List;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.entity.RIParallelWord;
import com.execue.core.common.bean.nlp.RecognitionEntityType;
import com.execue.core.common.bean.nlp.InstanceInformation;

/**
 * @author Abhijit
 * @since Jul 29, 2008 - 4:11:02 PM
 */
public class EntityFactory {

   private static final Logger log = Logger.getLogger(EntityFactory.class);

   /**
    * Restrict access to static
    */
   private EntityFactory () {
   }

   public static RecognitionEntity getRecognitionEntity (RecognitionEntityType entityType, String entityName, String nlpTag,
            String grpID) {
      RecognitionEntity entity = getRecognitionEntity(entityType, nlpTag);
      if (entity instanceof OntoEntity) {
         ((OntoEntity) entity).setOntoName(entityName);
      } else if (entity instanceof SFLEntity) {
         ((SFLEntity) entity).setSflName(entityName);
      } else {
         entity.setWord(entityName);
      }
      entity.setGroupId(grpID);
      return entity;
   }

   public static RecognitionEntity getRecognitionEntity (RecognitionEntityType entityType, String entityName, String nlpTag) {
      RecognitionEntity entity = getRecognitionEntity(entityType, entityName, nlpTag, "");
      return entity;
   }

   public static RecognitionEntity getRecognitionEntity (RecognitionEntityType entityType, String entityName, String nlpTag,
            String grpID, int position) {
      RecognitionEntity entity = getRecognitionEntity(entityType, entityName, nlpTag, grpID);
      entity.setPosition(position);
      return entity;
   }

   public static RecognitionEntity getParallelWordEntity (RecognitionEntityType entityType, List<RIParallelWord> entityNames,
            String nlpTag, String grpID) {
      RecognitionEntity entity = getRecognitionEntity(entityType, nlpTag);
      entity.setGroupId(grpID);
      if (entity instanceof PWEntity) {
         ((PWEntity) entity).addWordValues(entityNames);
      }
      return entity;
   }

   public static RecognitionEntity getRecognitionEntity (RecognitionEntityType entityType, String entityName, String entityValue,
            String nlpTag, String grpID) {
      return getRecognitionEntity(entityType, entityName, entityValue, nlpTag, grpID, false);
   }

   public static RecognitionEntity getRecognitionEntity (RecognitionEntityType entityType, String entityName, String entityValue,
            String nlpTag, String grpID, Boolean compoundType) {
      RecognitionEntity entity = getRecognitionEntity(entityType, entityName, nlpTag, grpID);
      if (entity instanceof InstanceEntity) {
         InstanceInformation information = new InstanceInformation();
         information.setInstanceValue(entityValue);
         ((InstanceEntity) entity).addInstanceInformation(information);
      } else if (entity instanceof ProfileEntity) {
         ((ProfileEntity) entity).setProfileName(entityValue);
      }
      return entity;
   }

   public static RecognitionEntity getRecognitionEntity (RecognitionEntityType entityType, String nlpTag) {
      RecognitionEntity entity = null;

      try {
         entity = (RecognitionEntity) Class.forName(entityType.getValue()).newInstance();
         entity.setNLPtag(nlpTag);
         entity.setEntityType(entityType);

      } catch (ClassNotFoundException classNotFoundException) {
         log.error(classNotFoundException);
         classNotFoundException.printStackTrace();
      } catch (InstantiationException instantiationException) {
         log.error(instantiationException);
         instantiationException.printStackTrace();
      } catch (IllegalAccessException e) {
         e.printStackTrace();
      }

      return entity;
   }

}