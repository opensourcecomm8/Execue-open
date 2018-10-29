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

import com.execue.core.common.bean.entity.Cloud;
import com.execue.core.common.bean.entity.CloudComponent;
import com.execue.swi.exception.KDXException;

public interface IKDXCloudDeletionService {

   /**
    * Removes any of the existing components from a cloud
    * 
    * @param componentsToBeRemoved
    * @throws KDXException
    */
   public void removeComponentsFromCloud (List<CloudComponent> componentsToBeRemoved) throws KDXException;

   public void removeComponentFromCloud (Long cloudId, Long componentBEId) throws KDXException;

   public void removeComponentFromCloudByModelId (Long modelId, Long componentBEId) throws KDXException;

   public void deleteCloudComponentsByCloudId (Long cloudId) throws KDXException;

   /**
    * Deletes the cloud from the system
    * 
    * @param cloudId
    * @throws KDXException
    */
   public void deleteClouds (List<Cloud> clouds) throws KDXException;

}
