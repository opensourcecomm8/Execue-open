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


package com.execue.platform.swi.impl;

import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.Cloud;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.Instance;
import com.execue.core.common.bean.entity.Relation;
import com.execue.core.common.bean.entity.Type;
import com.execue.core.exception.UidException;
import com.execue.platform.IUidService;
import com.execue.platform.exception.PlatformException;
import com.execue.platform.swi.IBusinessEntityManagementWrapperService;
import com.execue.swi.exception.KDXException;
import com.execue.swi.service.IKDXManagementService;

public class BusinessEntityManagementWrapperServiceImpl implements IBusinessEntityManagementWrapperService {

   private IKDXManagementService kdxManagementService;
   private IUidService           knowledgeIdGenerationService;

   @Override
   public BusinessEntityDefinition createConcept (Long modelId, Concept concept) throws PlatformException {
      try {
         return getKdxManagementService()
                  .createConcept(modelId, concept, getKnowledgeIdGenerationService().getNextId());
      } catch (UidException e) {
         throw new PlatformException(e.getCode(), e);
      } catch (KDXException e) {
         throw new PlatformException(e.getCode(), e);
      }
   }

   @Override
   public BusinessEntityDefinition createConcept (Long modelId, Type type, Concept concept) throws PlatformException {
      try {
         return getKdxManagementService().createConcept(modelId, type, concept,
                  getKnowledgeIdGenerationService().getNextId());
      } catch (UidException e) {
         throw new PlatformException(e.getCode(), e);
      } catch (KDXException e) {
         throw new PlatformException(e.getCode(), e);
      }
   }

   @Override
   public BusinessEntityDefinition createConcept (Long modelId, Cloud cloud, Type type, Concept concept,
            Long knowledgeId) throws PlatformException {
      try {
         return getKdxManagementService().createConcept(modelId, cloud, type, concept,
                  getKnowledgeIdGenerationService().getNextId());
      } catch (UidException e) {
         throw new PlatformException(e.getCode(), e);
      } catch (KDXException e) {
         throw new PlatformException(e.getCode(), e);
      }
   }

   @Override
   public BusinessEntityDefinition createInstance (Long modelId, Long conceptId, Instance instance)
            throws PlatformException {
      try {
         return getKdxManagementService().createInstance(modelId, conceptId, instance,
                  getKnowledgeIdGenerationService().getNextId());
      } catch (UidException e) {
         throw new PlatformException(e.getCode(), e);
      } catch (KDXException e) {
         throw new PlatformException(e.getCode(), e);
      }
   }

   @Override
   public BusinessEntityDefinition createRelation (Long modelId, Relation relation) throws PlatformException {
      try {
         return getKdxManagementService().createRelation(modelId, relation,
                  getKnowledgeIdGenerationService().getNextId());
      } catch (UidException e) {
         throw new PlatformException(e.getCode(), e);
      } catch (KDXException e) {
         throw new PlatformException(e.getCode(), e);
      }
   }

   @Override
   public BusinessEntityDefinition createRelation (Long modelId, Cloud cloud, Type type, Relation relation,
            Long knowledgeId) throws PlatformException {
      try {
         return getKdxManagementService().createRelation(modelId, cloud, type, relation,
                  getKnowledgeIdGenerationService().getNextId());
      } catch (UidException e) {
         throw new PlatformException(e.getCode(), e);
      } catch (KDXException e) {
         throw new PlatformException(e.getCode(), e);
      }
   }

   @Override
   public void createType (Long modelId, Type type, boolean isRealizedType) throws PlatformException {
      try {
         getKdxManagementService().createType(modelId, type, isRealizedType,
                  getKnowledgeIdGenerationService().getNextId());
      } catch (UidException e) {
         throw new PlatformException(e.getCode(), e);
      } catch (KDXException e) {
         throw new PlatformException(e.getCode(), e);
      }

   }

   @Override
   public BusinessEntityDefinition createTypeInstance (Long modelId, Long typeId, Instance instance)
            throws PlatformException {
      try {
         return getKdxManagementService().createTypeInstance(modelId, typeId, instance,
                  getKnowledgeIdGenerationService().getNextId());
      } catch (UidException e) {
         throw new PlatformException(e.getCode(), e);
      } catch (KDXException e) {
         throw new PlatformException(e.getCode(), e);
      }
   }

   @Override
   public BusinessEntityDefinition createTypeInstance (Long modelId, Long typeModelGroupId, Long typeId,
            Instance instance) throws PlatformException {
      try {
         return getKdxManagementService().createTypeInstance(modelId, typeModelGroupId, typeId, instance,
                  getKnowledgeIdGenerationService().getNextId());
      } catch (UidException e) {
         throw new PlatformException(e.getCode(), e);
      } catch (KDXException e) {
         throw new PlatformException(e.getCode(), e);
      }
   }

   @Override
   public BusinessEntityDefinition saveOrUpdateConcept (Long modelId, Concept concept) throws PlatformException {
      try {
         return getKdxManagementService().saveOrUpdateConcept(modelId, concept,
                  getKnowledgeIdGenerationService().getNextId());
      } catch (UidException e) {
         throw new PlatformException(e.getCode(), e);
      } catch (KDXException e) {
         throw new PlatformException(e.getCode(), e);
      }
   }

   @Override
   public BusinessEntityDefinition saveOrUpdateConcept (Long modelId, Type type, Concept concept)
            throws PlatformException {
      try {
         return getKdxManagementService().saveOrUpdateConcept(modelId, type, concept,
                  getKnowledgeIdGenerationService().getNextId());
      } catch (UidException e) {
         throw new PlatformException(e.getCode(), e);
      } catch (KDXException e) {
         throw new PlatformException(e.getCode(), e);
      }
   }

   @Override
   public BusinessEntityDefinition saveOrUpdateConcept (Long modelId, Cloud cloud, Type type, Concept concept,
            Long knowledgeId) throws PlatformException {
      try {
         return getKdxManagementService().saveOrUpdateConcept(modelId, cloud, type, concept,
                  getKnowledgeIdGenerationService().getNextId());
      } catch (UidException e) {
         throw new PlatformException(e.getCode(), e);
      } catch (KDXException e) {
         throw new PlatformException(e.getCode(), e);
      }
   }

   public IKDXManagementService getKdxManagementService () {
      return kdxManagementService;
   }

   public void setKdxManagementService (IKDXManagementService kdxManagementService) {
      this.kdxManagementService = kdxManagementService;
   }

   public IUidService getKnowledgeIdGenerationService () {
      return knowledgeIdGenerationService;
   }

   public void setKnowledgeIdGenerationService (IUidService knowledgeIdGenerationService) {
      this.knowledgeIdGenerationService = knowledgeIdGenerationService;
   }

}
