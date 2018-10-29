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
import com.execue.core.common.bean.entity.Type;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.SWIException;

/**
 * This service manages the CRUD operations associated with Clouds
 * 
 * @author John Mallavalli
 */
public interface IKDXCloudManagementService {

   /**
    * Creates an empty cloud without any components that can be used as a place holder
    * 
    * @param cloud
    * @throws KDXException
    */
   public void create (Cloud cloud) throws KDXException;

   /**
    * Adds the components to an existing cloud
    * 
    * @param componentsToBeAdded
    * @throws KDXException
    */
   public void addComponentsToCloud (List<CloudComponent> componentsToBeAdded) throws KDXException;

   /**
    * Removes any of the existing components from a cloud
    * 
    * @param componentsToBeRemoved
    * @throws KDXException
    */
   public void removeComponentsFromCloud (List<CloudComponent> componentsToBeRemoved) throws KDXException;

   public void removeComponentFromCloud (Long cloudId, Long componentBEId) throws KDXException;

   public void updateCloud (Cloud cloud) throws KDXException;

   /**
    * Creates a new complex type, resulting in a new type cloud
    * 
    * @param type
    * @param components
    * @return
    * @throws KDXException
    */
   public Cloud createComplexType (Type type, List<CloudComponent> components, Long knowledgeId) throws KDXException;

   public void manageBaseConceptsInCloud (Cloud cloud, Long modelId) throws SWIException;
}
