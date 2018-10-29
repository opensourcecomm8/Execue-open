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


package com.execue.swi.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.EntityTripleDefinition;
import com.execue.core.common.bean.entity.InstancePathDefinition;
import com.execue.core.common.bean.entity.Path;
import com.execue.core.common.bean.entity.PathDefinition;
import com.execue.core.common.bean.entity.Rule;
import com.execue.core.common.type.AssociationPositionType;
import com.execue.core.common.type.AssociationType;
import com.execue.core.common.type.BehaviorType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.EntityTriplePropertyType;
import com.execue.core.common.type.HierarchyType;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.SWIException;
import com.execue.swi.dataaccess.IBaseKDXDataAccessManager;
import com.execue.swi.dataaccess.IKDXDataAccessManager;
import com.execue.swi.dataaccess.IPathDefinitionDataAccessManager;
import com.execue.swi.exception.SWIExceptionCodes;
import com.execue.swi.service.IPathDefinitionManagementService;
import com.execue.swi.service.IPathDefinitionRetrievalService;

public class PathDefinitionManagementServiceImpl implements IPathDefinitionManagementService {

   private IPathDefinitionDataAccessManager pathDefinitionDataAccessManager;

   private IPathDefinitionRetrievalService  pathDefinitionRetrievalService;

   private IKDXDataAccessManager            kdxDataAccessManager;

   private IBaseKDXDataAccessManager        baseKDXDataAccessManager;

   public void createInstancePathDefinition (InstancePathDefinition instancePathDefinition) throws SWIException {
      try {
         getPathDefinitionDataAccessManager().createInstancePathDefinition(instancePathDefinition);
      } catch (DataAccessException e) {
         throw new SWIException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public void createPathDefinition (PathDefinition pathDefinition) throws KDXException {
      try {
         populatePathRulesBasedOnBehavior(pathDefinition);
         getPathDefinitionDataAccessManager().createPathDefinition(pathDefinition);
      } catch (DataAccessException e) {
         throw new KDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   /**
    * Method to Populate Path Rules Based on End Point behavior.
    * 
    * @param pathDefinition
    * @throws KDXException
    */
   private void populatePathRulesBasedOnBehavior (PathDefinition pathDefinition) throws KDXException {
      Set<Rule> pathRules = pathDefinition.getPathRules();
      if (pathRules == null) {
         pathRules = new HashSet<Rule>(1);
      }
      BusinessEntityDefinition srcBed = pathDefinition.getSourceBusinessEntityDefinition();
      populatePathRulesByEndPoint(pathRules, srcBed, AssociationPositionType.SOURCE);
      BusinessEntityDefinition destBed = pathDefinition.getDestinationBusinessEntityDefinition();
      populatePathRulesByEndPoint(pathRules, destBed, AssociationPositionType.DESTINATION);
      pathDefinition.setPathRules(pathRules);
   }

   /**
    * @param pathRules
    * @param endPoint
    * @throws KDXException
    */
   private void populatePathRulesByEndPoint (Set<Rule> pathRules, BusinessEntityDefinition endPoint,
            AssociationPositionType assocPosType) throws KDXException {
      List<BehaviorType> behaviorList = getKdxDataAccessManager().getAllBehaviorForEntity(endPoint.getId());
      List<Long> behaviorIds = new ArrayList<Long>(1);
      for (BehaviorType behaviorType : behaviorList) {
         behaviorIds.add(behaviorType.getValue().longValue());
      }
      if (!CollectionUtils.isEmpty(behaviorIds)) {
         List<Rule> rules = getBaseKDXDataAccessManager().getRulesForBehaviorsIdsAndPosType(behaviorIds, assocPosType);
         if (!CollectionUtils.isEmpty(rules)) {
            pathRules.addAll(rules);
         }
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.swi.IPathDefinitionService#createPathDefinition(com.execue.core.common.bean.entity.BusinessEntityDefinition,
    *      com.execue.core.common.bean.entity.BusinessEntityDefinition,
    *      com.execue.core.common.bean.entity.BusinessEntityDefinition, java.util.Set, java.lang.Long)
    */
   public void createPathDefinition (Long cloudId, EntityTripleDefinition entityTripleDefinition, Set<Rule> rules)
            throws KDXException {
      EntityTripleDefinition entityTriple = new EntityTripleDefinition();
      entityTriple.setOrigin(entityTripleDefinition.getOrigin());
      entityTriple.setCardinality(1);
      entityTriple.setRelation(entityTripleDefinition.getRelation());
      entityTriple.setSourceBusinessEntityDefinition(entityTripleDefinition.getSourceBusinessEntityDefinition());
      entityTriple.setDestinationBusinessEntityDefinition(entityTripleDefinition
               .getDestinationBusinessEntityDefinition());
      entityTriple.setTripleType(entityTripleDefinition.getTripleType());
      entityTriple.setPropertyType(EntityTriplePropertyType.NORMAL);

      // Create the path definition
      PathDefinition pd = new PathDefinition();
      pd.setDestinationBusinessEntityDefinition(entityTripleDefinition.getDestinationBusinessEntityDefinition());
      // TODO: NK: possible_attribute_rule entries were getting deleted, so for now we touched the possible attribute
      // inside the rule, need to find better solution??
      pd.setPathRules(rules);
      pd.setSourceBusinessEntityDefinition(entityTripleDefinition.getSourceBusinessEntityDefinition());
      pd.setHierarchyType(HierarchyType.NON_HIERARCHICAL);
      pd.setPriority(1);
      pd.setPathLength(1);
      pd.setCentralConceptType(CheckType.NO);
      pd.setType(entityTripleDefinition.getTripleType());
      pd.setCloudId(cloudId);
      pd.setAssociationType(AssociationType.RELATION_ASSOCIATION);

      // Create the path
      Path path = new Path();
      path.setEntityTripleDefinition(entityTriple);
      path.setEntityTripleOrder(0);
      path.setPathDefinition(pd);
      Set<Path> pathSet = new HashSet<Path>();
      pathSet.add(path);
      pd.setPaths(pathSet);

      createPathDefinition(pd);
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.swi.IPathDefinitionService#markCentralConceptPaths(java.lang.Long)
    */
   public void markCentralConceptPaths (Long modelId) throws SWIException {

      List<PathDefinition> centralPaths = getPathDefinitionRetrievalService().getAllPathsBySourceBehaviorType(modelId,
               BehaviorType.POPULATION);

      for (PathDefinition path : centralPaths) {
         path.setCentralConceptType(CheckType.YES);
      }

      updatePathDefinitions(centralPaths);
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.swi.IPathDefinitionService#updatePathDefinitions(java.util.List)
    */
   public void updatePathDefinitions (List<PathDefinition> pathDefinitions) throws SWIException {
      try {
         getPathDefinitionDataAccessManager().updatePathDefinitions(pathDefinitions);
      } catch (DataAccessException e) {
         throw new SWIException(e.code, e);
      }

   }

   public IPathDefinitionDataAccessManager getPathDefinitionDataAccessManager () {
      return pathDefinitionDataAccessManager;
   }

   public void setPathDefinitionDataAccessManager (IPathDefinitionDataAccessManager pathDefinitionDataAccessManager) {
      this.pathDefinitionDataAccessManager = pathDefinitionDataAccessManager;
   }

   public IPathDefinitionRetrievalService getPathDefinitionRetrievalService () {
      return pathDefinitionRetrievalService;
   }

   public void setPathDefinitionRetrievalService (IPathDefinitionRetrievalService pathDefinitionRetrievalService) {
      this.pathDefinitionRetrievalService = pathDefinitionRetrievalService;
   }

   public IKDXDataAccessManager getKdxDataAccessManager () {
      return kdxDataAccessManager;
   }

   public void setKdxDataAccessManager (IKDXDataAccessManager kdxDataAccessManager) {
      this.kdxDataAccessManager = kdxDataAccessManager;
   }

   public IBaseKDXDataAccessManager getBaseKDXDataAccessManager () {
      return baseKDXDataAccessManager;
   }

   public void setBaseKDXDataAccessManager (IBaseKDXDataAccessManager baseKDXDataAccessManager) {
      this.baseKDXDataAccessManager = baseKDXDataAccessManager;
   }

}
