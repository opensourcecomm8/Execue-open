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


/**
 * 
 */
package com.execue.swi.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.execue.core.common.bean.entity.Cloud;
import com.execue.core.common.bean.entity.CloudComponent;
import com.execue.core.common.bean.entity.RICloud;
import com.execue.core.common.type.CloudCategory;
import com.execue.core.common.type.CloudOutput;
import com.execue.swi.exception.KDXException;

/**
 * @author Nitesh
 * @version 1.0
 * @since 29/01/10
 */
public interface IKDXCloudRetrievalService {

   /**
    * Method to return the list of RI Clouds for the given compBedIds and cloud output
    * 
    * @param compBedIds
    * @param cloudOutput
    * @return Map<CloudCategory, List<RICloud>>
    * @throws KDXException
    */
   public Map<CloudCategory, List<RICloud>> getRICloudsByCompBedIdsAndCloudOutput (Set<Long> compBedIds,
            CloudOutput cloudOutput) throws KDXException;

   /**
    * Returns the default app cloud for the model
    * 
    * @param modelId
    * @return
    * @throws KDXException
    */
   public Cloud getDefaultAppCloud (Long modelId) throws KDXException;

   public List<Cloud> getAllCloudsByModelId (Long modelId) throws KDXException;

   /**
    * Returns cloud by name
    * 
    * @param cloudName
    * @return
    * @throws KDXException
    */
   public Cloud getCloudByName (String cloudName) throws KDXException;

   /**
    * Method to retrieve the CloudComponent by the cloud id and the component bed id
    * 
    * @param cloudId
    * @param componentBedId
    * @return the CloudComponent
    * @throws KDXException
    */
   public CloudComponent getCloudComponentByCloudIdAndComponentBedId (Long cloudId, Long componentBedId)
            throws KDXException;

   /**
    * Extracts all components with a given cloud id. The returned objects are fully loaded and this method call shoudl
    * not be used in regular request processing and is written for maintenance purposes
    * 
    * @param cloudId
    * @return
    * @throws KDXException
    */
   public List<CloudComponent> getAllCloudComponentsByCloudIdWithDetails (Long cloudId) throws KDXException;

   public Cloud getDefaultAppCloudByAppId (Long appId) throws KDXException;

   public Map<CloudCategory, List<RICloud>> getRICloudsByCompBedIdsAndCloudOutput (Set<Long> componentBedIds,
            CloudOutput enhanced, Long cloudId) throws KDXException;

   public Cloud getCloudById (Long cloudId) throws KDXException;

   /**
    * Method to get list of clouds by category.
    * 
    * @param category
    * @return
    * @throws KDXException
    */
   public List<Cloud> getCloudsByCategory (CloudCategory category) throws KDXException;
}
