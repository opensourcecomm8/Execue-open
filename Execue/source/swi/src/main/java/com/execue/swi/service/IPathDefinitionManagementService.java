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
import java.util.Set;

import com.execue.core.common.bean.entity.EntityTripleDefinition;
import com.execue.core.common.bean.entity.InstancePathDefinition;
import com.execue.core.common.bean.entity.PathDefinition;
import com.execue.core.common.bean.entity.Rule;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.SWIException;

public interface IPathDefinitionManagementService {

   public void createPathDefinition (PathDefinition pathDefinition) throws SWIException;

   public void updatePathDefinitions (List<PathDefinition> pathDefinitions) throws SWIException;

   /**
    * This method creates the path definition for the triple. The ETD should be populated with the source, relation &
    * destination beds along with the triple type information before using this service method
    * 
    * @param cloudId
    * @param entityTripleDefinition
    * @param rules
    */
   public void createPathDefinition (Long cloudId, EntityTripleDefinition entityTripleDefinition, Set<Rule> rules)
            throws KDXException;

   // ------------ instance path definition method calls ---------------------- //
   public void createInstancePathDefinition (InstancePathDefinition instancePathDefinition) throws SWIException;

   /**
    * This method marks the central concept paths
    * 
    * @param modelId
    * @throws SWIException
    */
   public void markCentralConceptPaths (Long modelId) throws SWIException;

}
