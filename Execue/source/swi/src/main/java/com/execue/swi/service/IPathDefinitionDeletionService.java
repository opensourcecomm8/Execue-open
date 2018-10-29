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


package com.execue.swi.service;

import java.util.List;

import com.execue.core.common.bean.entity.EntityTripleDefinition;
import com.execue.core.common.bean.entity.PathDefinition;
import com.execue.swi.exception.SWIException;

public interface IPathDefinitionDeletionService {

   public void deletePathDefinition (PathDefinition pathDefinition) throws SWIException;

   public void deletePathDefinitions (List<Long> pathDefinitionIds) throws SWIException;

   public void deleteEntityTripleDefinitions (List<Long> etdIds) throws SWIException;

   /**
    * This method first deletes all the indirect paths in which the given direct path is participating. Then it deletes
    * the direct path and its corresponding entity triple definition.
    * 
    * @param pathDefinition
    * @throws SWIException
    */
   public void deleteDirectPath (PathDefinition pathDefinition) throws SWIException;

   /**
    * This method deletes all the path_definition_etds, path_definition_rules, path_definitions and
    * entity_triple_definitions for the given cloud id
    * 
    * @param cloudId
    * @throws SWIException
    */
   public void deleteAllPathDefinitionsForCloud (Long cloudId) throws SWIException;

   public void deleteEntityTripleDefinition (EntityTripleDefinition tripleDefinition) throws SWIException;

   public void deletePathDefinitions (Long entityBedId) throws SWIException;
}
