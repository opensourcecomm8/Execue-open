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


package com.execue.swi.dataaccess;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import com.execue.core.common.bean.entity.Cloud;
import com.execue.core.common.bean.entity.CloudComponent;
import com.execue.core.common.bean.entity.RICloud;
import com.execue.core.common.type.CloudCategory;
import com.execue.core.common.type.CloudOutput;
import com.execue.swi.exception.KDXException;

/**
 * @author Nitesh
 */
public interface IKDXCloudDataAccessManager {

   /**
    * @param <BusinessObject>
    * @param id
    * @param clazz
    * @return
    * @throws KDXException
    */
   public <BusinessObject extends Serializable> BusinessObject getById (Long id, Class<BusinessObject> clazz)
            throws KDXException;

   /**
    * @param cloud
    * @throws KDXException
    */
   public void create (Cloud cloud) throws KDXException;

   public void update (Cloud cloud) throws KDXException;

   public void deleteAll (List<Cloud> clouds) throws KDXException;

   /**
    * @param cloudName
    * @return
    * @throws KDXException
    */
   public Cloud getCloudByName (String cloudName) throws KDXException;

   public List<Cloud> getAllCloudsByModelId (Long modelId) throws KDXException;

   /**
    * @param componentsToBeAdded
    * @throws KDXException
    */
   public void addComponentsToCloud (List<CloudComponent> componentsToBeAdded) throws KDXException;

   /**
    * @param componentsToBeRemoved
    * @throws KDXException
    */
   public void removeComponentsFromCloud (List<CloudComponent> componentsToBeRemoved) throws KDXException;

   public void removeComponentFromCloud (Long cloudId, Long componentBEId) throws KDXException;

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
    * Retrieves all the components for the give cloud by id
    * 
    * @param cloudId
    * @return
    * @throws KDXException
    */
   public List<CloudComponent> getAllCloudComponentsByCloudId (Long cloudId) throws KDXException;

   /**
    * Method to return the list of RI Clouds for the given compBedIds and cloudOutput
    * 
    * @param compBedIds
    * @param cloudOutput
    * @param cloudId
    * @return the List<RICloud>
    * @throws KDXException
    */
   public List<RICloud> getRICloudsByCompBedIdsAndCloudOutput (Set<Long> compBedIds, CloudOutput cloudOutput,
            Long cloudId) throws KDXException;

   /**
    * @param cloudComponent
    * @throws KDXException
    */
   public void updateCloudComponent (CloudComponent cloudComponent) throws KDXException;

   /**
    * @param cloudComponents
    * @throws KDXException
    */
   public void updateCloudComponents (List<CloudComponent> cloudComponents) throws KDXException;

   public void deleteCloudComponentsByCloudId (Long cloudId) throws KDXException;

   public Cloud getCloudById (Long cloudId) throws KDXException;

   public List<Cloud> getCloudsByCategory (CloudCategory category) throws KDXException;;
}