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


package com.execue.platform.swi;

import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.Cloud;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.Instance;
import com.execue.core.common.bean.entity.Relation;
import com.execue.core.common.bean.entity.Type;
import com.execue.platform.exception.PlatformException;

public interface IBusinessEntityManagementWrapperService {

   public void createType (Long modelId, Type type, boolean isRealizedType) throws PlatformException;

   public BusinessEntityDefinition saveOrUpdateConcept (Long modelId, Concept concept) throws PlatformException;

   public BusinessEntityDefinition saveOrUpdateConcept (Long modelId, Type type, Concept concept)
            throws PlatformException;

   public BusinessEntityDefinition saveOrUpdateConcept (Long modelId, Cloud cloud, Type type, Concept concept,
            Long knowledgeId) throws PlatformException;

   public BusinessEntityDefinition createTypeInstance (Long modelId, Long typeModelGroupId, Long typeId,
            Instance instance) throws PlatformException;

   public BusinessEntityDefinition createTypeInstance (Long modelId, Long typeId, Instance instance)
            throws PlatformException;

   public BusinessEntityDefinition createConcept (Long modelId, Concept concept) throws PlatformException;

   public BusinessEntityDefinition createConcept (Long modelId, Type type, Concept concept) throws PlatformException;

   public BusinessEntityDefinition createConcept (Long modelId, Cloud cloud, Type type, Concept concept,
            Long knowledgeId) throws PlatformException;

   public BusinessEntityDefinition createRelation (Long modelId, Relation relation) throws PlatformException;

   public BusinessEntityDefinition createInstance (Long modelId, Long conceptId, Instance instance)
            throws PlatformException;

   public BusinessEntityDefinition createRelation (Long modelId, Cloud cloud, Type type, Relation relation,
            Long knowledgeId) throws PlatformException;

}
