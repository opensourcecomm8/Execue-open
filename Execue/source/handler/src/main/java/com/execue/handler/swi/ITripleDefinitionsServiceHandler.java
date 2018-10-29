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

import com.execue.core.common.type.HierarchyRelationType;
import com.execue.core.exception.HandlerException;
import com.execue.handler.bean.UIConcept;
import com.execue.handler.bean.UIPathDefinition;
import com.execue.handler.bean.UIRelation;

public interface ITripleDefinitionsServiceHandler {

   public List<UIPathDefinition> getUIPathDefinitionsForHeirarchy (Long modelId, Long conceptBedId,
            HierarchyRelationType heirarchyRelationType) throws HandlerException;

   public List<UIConcept> getUIConceptsForHeirarchy (Long modelId, Long conceptBedId,
            HierarchyRelationType heirarchyRelationType) throws HandlerException;

   public List<UIPathDefinition> getUIPathDefinitionsForRelation (Long modelId, Long conceptBedId)
            throws HandlerException;

   public List<UIConcept> getUIConceptsForRelation (Long modelId, Long conceptBedId) throws HandlerException;

   public void saveHierarchyPathDefinitions (Long modelId, HierarchyRelationType selectedHierarchyType,
            List<UIPathDefinition> selectedHierarchyPathDefinitions,
            List<UIPathDefinition> savedHierarchyPathDefinitions) throws HandlerException;

   public void saveRelationPathDefinitions (Long modelId, List<UIPathDefinition> selectedRelationPathDefinitions,
            List<UIPathDefinition> savedRelationPathDefinitions) throws HandlerException;

   public List<UIRelation>  suggestRelationsByName (Long modelId, String searchString) throws HandlerException;
}
