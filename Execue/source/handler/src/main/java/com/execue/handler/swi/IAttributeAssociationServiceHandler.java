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

import com.execue.core.exception.HandlerException;
import com.execue.handler.bean.UIConcept;
import com.execue.handler.bean.UIPathDefinition;
import com.execue.handler.bean.UIRelation;

public interface IAttributeAssociationServiceHandler {

   public List<UIConcept> getConceptsByType (Long modelId, String typeName) throws HandlerException;

   public List<UIPathDefinition> getExistingPathDefinitions (Long srcBedId, Long reletionBedId) throws HandlerException;

   public UIRelation getRelationByName (Long modelId, String relationName) throws HandlerException;

   public void associateAttributePathDefinitions (Long modeId, List<UIPathDefinition> savedPathdefinitions,
            List<UIPathDefinition> existingPathdefinitions) throws HandlerException;

}
