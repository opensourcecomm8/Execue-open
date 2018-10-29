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


package com.execue.swi.validation;

import java.io.Serializable;

import com.execue.core.common.bean.entity.DataSource;
import com.execue.core.common.bean.entity.Hierarchy;
import com.execue.core.common.bean.entity.Profile;
import com.execue.core.common.bean.entity.Range;
import com.execue.swi.exception.KDXException;

public interface ISWIValidator {

   public <DomainObject extends Serializable> boolean objectExists (String name, Class<DomainObject> input);

   public boolean profileExists (Long modelId, Profile profile, Long parentBedId) throws KDXException;

   public boolean isConceptProfileExists (Profile profile, Long modelId);

   public boolean isInstanceProfileExists (Profile profile, Long modelId);

   // public <DomainObject extends Serializable> boolean instanceExistsForConcept (Domain domain, Concept concept,
   // String name);
   public <DomainObject extends Serializable> boolean instanceExistsForConcept (Long modelId, Long conceptId,
            String instanceDisplayName);

   public <DomainObject extends Serializable> DomainObject objectExistsById (Long id, Class<DomainObject> clazz);

   public boolean dataSourceAlreadyExists (DataSource dataSource);

   public boolean assetsExistForDataSource (Long dataSourceId);

   public <DomainObject extends Serializable> boolean instanceExistsForType (Long modelId, Long typeId,
            String instanceDisplayName);

   public <DomainObject extends Serializable> Range rangeExists (Long modelId, String range);

   public boolean hierarchyExists (Long modelId, Hierarchy hierarchy);

}
