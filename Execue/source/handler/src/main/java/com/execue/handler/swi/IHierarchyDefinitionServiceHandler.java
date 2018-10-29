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


package com.execue.handler.swi;

import java.util.List;

import com.execue.core.common.bean.entity.Hierarchy;
import com.execue.core.exception.HandlerException;
import com.execue.handler.bean.UIConcept;

public interface IHierarchyDefinitionServiceHandler {

   public List<Hierarchy> getHierarchiesForModel (Long modelId) throws HandlerException;

   public List<UIConcept> getExistingHierarchyDefinitions (Long herarchyId) throws HandlerException;

   public List<UIConcept> getAllDimensionsForModel (Long modelId) throws HandlerException;

   public Hierarchy saveUpdateHierarchyDefinitions (Long modelId, Hierarchy hierarchy,
            List<Long> hierarchyDetailConceptBEDIDs) throws HandlerException;

   public void deleteHierarchyDefinitions (Long hierarchyId) throws HandlerException;

   public Long getMaxHierarchySize ();

}
