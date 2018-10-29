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


package com.execue.platform.swi;

import java.util.List;

import com.execue.core.common.bean.entity.Profile;
import com.execue.platform.exception.PlatformException;

/**
 * @author Vishay
 */
public interface IBusinessEntityDeletionWrapperService {

   public void deleteConceptHeirarchy (Long modelId, Long conceptBedId) throws PlatformException;

   public void deleteRelationHeirarchy (Long modelId, Long relationBedId) throws PlatformException;

   public void deleteInstanceHeirarchy (Long modelId, Long instanceBedId, Long parentConceptBedId)
            throws PlatformException;

   public void deleteInstancesHierarchyForConcept (Long modelId, Long conceptId) throws PlatformException;

   public void deleteInstanceProfileHeirarchy (Long modelId, Long instanceProfileBedId) throws PlatformException;

   public void deleteConceptProfileHeirarchy (Long conceptProfileBedId, Long modelId) throws PlatformException;

   public void deleteProfile (Long modelId, Profile profile) throws PlatformException;

   public void deleteProfiles (Long modelId, List<Profile> profiles) throws PlatformException;

   public void deleteRangeHeirarchy (Long rangeId) throws PlatformException;

   public void deleteInstanceHeirarchyForConcept (Long modelId, Long parentConceptBedId) throws PlatformException;

}
