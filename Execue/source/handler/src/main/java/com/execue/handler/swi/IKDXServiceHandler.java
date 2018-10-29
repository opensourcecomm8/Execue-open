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
package com.execue.handler.swi;

import java.util.List;
import java.util.Set;

import com.execue.core.common.bean.Page;
import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.Cloud;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.Hierarchy;
import com.execue.core.common.bean.entity.Instance;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.common.bean.entity.Relation;
import com.execue.core.common.bean.entity.SFLTerm;
import com.execue.core.common.bean.entity.SFLTermToken;
import com.execue.core.common.bean.entity.Stat;
import com.execue.core.common.type.AppSourceType;
import com.execue.core.exception.ExeCueException;
import com.execue.core.exception.HandlerException;
import com.execue.handler.bean.UIConcept;

/**
 * @author Deenu
 */
public interface IKDXServiceHandler {

   public void createConcept (Long modelId, Concept concept) throws ExeCueException;

   public void createInstance (Long appId, Long modelId, AppSourceType appSourceType, Long conceptId, Instance instance)
            throws ExeCueException;

   public void createHierarchy (Long modelId, Hierarchy hierarchy) throws ExeCueException;

   public void updateHierarchy (Long modelId, Hierarchy hierarchy) throws ExeCueException;

   public List<Concept> getConcepts (Long modelId) throws ExeCueException;

   public List<Concept> getConceptsForAssetMetaInfo (Long assetId) throws ExeCueException;

   public void updateConcept (Long modelId, Concept concept) throws ExeCueException;

   public void updateInstance (Long appId, Long modelId, AppSourceType appSourceType, Long conceptId, Instance instance)
            throws ExeCueException;

   public Model getModel (Long modelId) throws ExeCueException;

   public List<Model> getModelsByApplicationId (Long applicationId) throws ExeCueException;

   public Concept getConcept (Long id) throws ExeCueException;

   public Instance getInstance (Long instanceId) throws ExeCueException;

   public Relation getRelation (Long relationId) throws ExeCueException;

   public List<Instance> getInstancesForAssetMetaInfo (Long modelId, Long conceptId) throws ExeCueException;

   public List<Instance> getInstances (Long modelId, Long conceptId) throws ExeCueException;

   public List<Instance> getInstancesByPage (Long modelId, Long conceptId, Page page) throws ExeCueException;

   public Set<Stat> getAllStats () throws ExeCueException;

   public BusinessEntityDefinition getBusinessEntityDefinitionByIds (Long modelId, Long conceptId, Long instanceId)
            throws ExeCueException;

   public Concept getConceptByName (Long modelId, String conceptName) throws HandlerException;

   public List<SFLTerm> getAllExistingSFLTerms () throws HandlerException;

   public List<SFLTermToken> getSFLTermTokensBySFLTerm (String sflTermWord) throws HandlerException;

   public void updateSFLTermTokens (List<SFLTermToken> termTokens) throws HandlerException;

   public List<SFLTerm> getSFLTermsByKeyWord (String keyWord) throws HandlerException;

   public Cloud getDefaultAppCloud (Long modelId) throws ExeCueException;

   public List<Relation> getRelations (Long modelId) throws ExeCueException;

   public void createRelation (Long modelId, Relation relation) throws ExeCueException;

   public void updateRelation (Long modelId, Relation relation) throws ExeCueException;

   public List<UIConcept> getBusinessTermsByPopularity (Long modelId) throws ExeCueException;

   public Concept getPopulatedConceptWithStats (Long conceptId) throws ExeCueException;

   public void saveBusinessEntityVariations (Long modelId, Long bedId, List<String> variations) throws ExeCueException;

   public List<String> getBusinessEntityVariations (Long entityBedId) throws ExeCueException;

   public void deleteConceptHeirarchy (Long modelId, Long conceptBedId) throws HandlerException;

   public void deleteInstanceHeirarchy (Long modelId, Long conceptId, Long instanceId) throws HandlerException;

   public void deleteRelationHeirarchy (Long modelId, Long relationId) throws HandlerException;

   public boolean hasInstances (Long modelId, Long conceptId) throws HandlerException;

   public void deleteInstancesHeirarchyForConcept (Long modelId, Long conceptId) throws HandlerException;
}
