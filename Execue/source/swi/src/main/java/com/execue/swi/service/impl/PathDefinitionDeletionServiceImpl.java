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
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.execue.core.common.bean.entity.EntityTripleDefinition;
import com.execue.core.common.bean.entity.PathDefinition;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.SWIException;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.swi.dataaccess.IPathDefinitionDataAccessManager;
import com.execue.swi.exception.SWIExceptionCodes;
import com.execue.swi.service.IPathDefinitionDeletionService;
import com.execue.swi.service.IPathDefinitionRetrievalService;

public class PathDefinitionDeletionServiceImpl implements IPathDefinitionDeletionService {

   private IPathDefinitionDataAccessManager pathDefinitionDataAccessManager;

   private IPathDefinitionRetrievalService  pathDefinitionRetrievalService;

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.swi.IPathDefinitionService#deleteAllPathDefinitionsForCloud(java.lang.Long)
    */
   public void deleteAllPathDefinitionsForCloud (Long cloudId) throws SWIException {
      try {

         // First delete all the indirect path definition ids
         List<Long> indirectPathDefIds = getPathDefinitionDataAccessManager().getAllIndirectPathDefIdsForCloud(cloudId);
         deletePathDefinitions(indirectPathDefIds);

         // Then get all the path def ids i.e only all the direct paths will be coming
         List<PathDefinition> allPathDefinitionsForModel = getPathDefinitionDataAccessManager()
                  .getAllPathDefinitionsForCloud(cloudId);
         for (PathDefinition pathDefinition : allPathDefinitionsForModel) {
            deletePathDefinition(pathDefinition);
         }
      } catch (KDXException e) {
         throw new SWIException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.swi.IPathDefinitionService#deleteDirectPath(com.execue.core.common.bean.entity.PathDefinition)
    */
   public void deleteDirectPath (PathDefinition pathDefinition) throws SWIException {
      // First delete all the indirect paths of which this direct path is getting used into
      List<Long> indirectPathDefinitionIds = getPathDefinitionRetrievalService()
               .getIndirectPathDefinitionIdsForDirectPath(pathDefinition.getId());
      deletePathDefinitions(indirectPathDefinitionIds);

      // Finally, call the delete path definition which also deletes the etd
      deletePathDefinition(pathDefinition);
   }

   public void deleteEntityTripleDefinition (EntityTripleDefinition tripleDefinition) throws SWIException {
      // First delete all the indirect paths of which this direct path is getting used into
      List<Long> pathDefinitionIds = getPathDefinitionRetrievalService().getPathDefinitionIdsForETD(
               tripleDefinition.getId());
      List<PathDefinition> pathDefinitions = getPathDefinitionDataAccessManager().getPathDefinitionsByIds(
               pathDefinitionIds);

      for (PathDefinition pathDefinition : pathDefinitions) {
         deletePathDefinition(pathDefinition);
      }
      List<Long> etdIds = new ArrayList<Long>(1);
      etdIds.add(tripleDefinition.getId());
      // Finally, call the delete path definition which also deletes the etd
      deleteEntityTripleDefinitions(etdIds);

   }

   public void deleteEntityTripleDefinitions (List<Long> etdIds) throws SWIException {
      try {
         if (ExecueCoreUtil.isCollectionNotEmpty(etdIds)) {
            getPathDefinitionDataAccessManager().deleteEntityTripleDefinitions(etdIds);
         }
      } catch (DataAccessException e) {
         throw new SWIException(e.code, e);
      }

   }

   public void deletePathDefinition (PathDefinition pathDefinition) throws SWIException {
      try {
         getPathDefinitionDataAccessManager().deletePathDefinition(pathDefinition);
      } catch (DataAccessException e) {
         throw new SWIException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public void deletePathDefinitions (List<Long> pathDefinitionIds) throws SWIException {
      try {
         if (CollectionUtils.isEmpty(pathDefinitionIds)) {
            return;
         }
         getPathDefinitionDataAccessManager().deletePathDefinitions(pathDefinitionIds);
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

   public void deletePathDefinitions (Long entityBedId) throws SWIException {
      List<PathDefinition> pathDefinitions = null;
      try {
         pathDefinitions = getPathDefinitionRetrievalService().getAllDirectPaths(entityBedId);
      } catch (KDXException e) {
         throw new SWIException(SWIExceptionCodes.ENTITY_RETRIEVAL_FAILED, e);
      }

      if (ExecueCoreUtil.isCollectionNotEmpty(pathDefinitions)) {
         for (PathDefinition pathDefinition : pathDefinitions) {
            try {
               deleteDirectPath(pathDefinition);
            } catch (SWIException e) {
               throw new SWIException(SWIExceptionCodes.ENTITY_DELETE_FAILED, e);
            }
         }
      }
   }
}
