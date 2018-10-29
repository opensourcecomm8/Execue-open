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

import com.execue.core.common.bean.entity.PathDefinition;
import com.execue.swi.exception.SWIException;

/**
 * @author Nitesh
 */
public interface IPathAbsorptionService {

   /**
    * Method to absorb the indirect paths for the given model and cloud
    * 
    * @param cloudId
    * @param modelId
    * @throws SWIException
    */
   public void absorbIndirectPaths (Long cloudId, Long modelId) throws SWIException;

   /**
    * This method return the list of path definition having source as child and the destination of parent as destination
    * of child
    * 
    * @param parentPaths
    * @param modelId
    * @param existingTriples
    * @return the List<PathDefinition>
    * @throws SWIException
    */
   public List<PathDefinition> absorbParentPropertiesInAllChildConcepts (List<PathDefinition> parentPaths,
            Long modelId, Set<String> existingTriples) throws SWIException;

   /**
    * This method return the list of path definition having source as child and the destination of parent as destination
    * of child
    * 
    * @param parentPaths
    * @param modelId
    * @param existingTriples
    * @param execludeARA
    * @return the List<PathDefinition>
    * @throws SWIException
    */
   public List<PathDefinition> absorbParentPropertiesInAllChildConcepts (List<PathDefinition> parentPaths,
            Long modelId, Set<String> existingTriples, boolean execludeARA) throws SWIException;

   /**
    * This method return the list of path definition having destination as child where destination was parent
    * 
    * @param parentPaths
    * @param modelId
    * @param existingTriples
    * @return the List<PathDefinition>
    * @throws SWIException
    */
   public List<PathDefinition> assignChildrenAsRangeInPlaceOfParent (List<PathDefinition> parentPaths, Long modelId,
            Set<String> existingTriples) throws SWIException;

}
