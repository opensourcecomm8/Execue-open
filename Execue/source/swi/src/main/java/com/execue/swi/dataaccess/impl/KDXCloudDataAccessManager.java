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
package com.execue.swi.dataaccess.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.execue.core.common.bean.entity.Cloud;
import com.execue.core.common.bean.entity.CloudComponent;
import com.execue.core.common.bean.entity.RICloud;
import com.execue.core.common.type.CloudCategory;
import com.execue.core.common.type.CloudOutput;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.swi.exception.KDXException;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.dataaccess.swi.dao.ICloudDAO;
import com.execue.swi.dataaccess.IKDXCloudDataAccessManager;

/**
 * @author Nitesh
 */
public class KDXCloudDataAccessManager implements IKDXCloudDataAccessManager {

   private ICloudDAO cloudDAO;

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.swi.dataaccess.IKDXCloudDataAccessManager#getRICloudsByCompBedIdsAndCloudOutput(java.util.Set,
    *      com.execue.core.common.type.CloudOutput)
    */

   public <BusinessObject extends Serializable> BusinessObject getById (Long id, Class<BusinessObject> clazz)
            throws KDXException {
      try {
         return getCloudDAO().getById(id, clazz);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.swi.dataaccess.IKDXCloudDataAccessManager#create(com.execue.core.common.bean.entity.Cloud)
    */
   public void create (Cloud cloud) throws KDXException {
      try {
         getCloudDAO().create(cloud);
         if (ExecueCoreUtil.isCollectionNotEmpty(cloud.getCloudComponents())) {
            List<CloudComponent> cloudComponents = new ArrayList<CloudComponent>(cloud.getCloudComponents());
            getCloudDAO().saveOrUpdateAll(cloudComponents);
         }
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public void update (Cloud cloud) throws KDXException {
      try {
         getCloudDAO().update(cloud);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public void deleteAll (List<Cloud> clouds) throws KDXException {
      try {
         getCloudDAO().deleteAll(clouds);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.swi.dataaccess.IKDXCloudDataAccessManager#getCloudByName(java.lang.String)
    */
   public Cloud getCloudByName (String cloudName) throws KDXException {
      try {
         return getCloudDAO().getCloudByName(cloudName);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<Cloud> getAllCloudsByModelId (Long modelId) throws KDXException {
      try {
         return getCloudDAO().getAllCloudsByModelId(modelId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.swi.dataaccess.IKDXCloudDataAccessManager#addComponentsToCloud(java.util.List)
    */
   public void addComponentsToCloud (List<CloudComponent> componentsToBeAdded) throws KDXException {
      try {
         for (CloudComponent cloudComponent : componentsToBeAdded) {
            // TODO: -JM- add validations before adding the component
            // validate(cloudComponent);
            getCloudDAO().create(cloudComponent);
         }
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public void updateCloudComponent (CloudComponent cloudComponent) throws KDXException {
      try {
         getCloudDAO().update(cloudComponent);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public void updateCloudComponents (List<CloudComponent> cloudComponents) throws KDXException {
      try {
         getCloudDAO().updateAll(cloudComponents);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.swi.dataaccess.ICloudDataAccessManager#getCloudComponentByCloudIdAndComponentBedId(java.lang.Long,
    *      java.lang.Long)
    */
   public CloudComponent getCloudComponentByCloudIdAndComponentBedId (Long cloudId, Long componentBedId)
            throws KDXException {
      try {
         return getCloudDAO().getCloudComponentByCloudIdAndComponentBedId(cloudId, componentBedId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.swi.dataaccess.IKDXCloudDataAccessManager#getAllCloudComponentsByCloudId(java.lang.Long)
    */
   public List<CloudComponent> getAllCloudComponentsByCloudId (Long cloudId) throws KDXException {
      try {
         return getCloudDAO().getAllCloudComponentsByCloudId(cloudId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.swi.dataaccess.IKDXCloudDataAccessManager#removeComponentsFromCloud(java.util.List)
    */
   public void removeComponentsFromCloud (List<CloudComponent> componentsToBeRemoved) throws KDXException {
      try {
         for (CloudComponent cloudComponent : componentsToBeRemoved) {
            CloudComponent cloudComponentToBeRemoved = getCloudComponentByCloudIdAndComponentBedId(cloudComponent
                     .getCloud().getId(), cloudComponent.getComponentBed().getId());
            getCloudDAO().delete(cloudComponentToBeRemoved);
         }
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public void removeComponentFromCloud (Long cloudId, Long componentBEId) throws KDXException {
      try {
         CloudComponent cloudComponent = getCloudComponentByCloudIdAndComponentBedId(cloudId, componentBEId);
         getCloudDAO().delete(cloudComponent);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.swi.dataaccess.IKDXCloudDataAccessManager#getRICloudsByCompBedIdsAndCloudOutput(java.util.Set,
    *      com.execue.core.common.type.CloudOutput)
    */
   public List<RICloud> getRICloudsByCompBedIdsAndCloudOutput (Set<Long> compBedIds, CloudOutput cloudOutput,
            Long cloudId) throws KDXException {
      try {
         return getCloudDAO().getRICloudsByCompBedIdsAndCloudOutput(compBedIds, cloudOutput, cloudId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public void deleteCloudComponentsByCloudId (Long cloudId) throws KDXException {
      try {
         getCloudDAO().deleteCloudComponentsByCloudId(cloudId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public Cloud getCloudById (Long cloudId) throws KDXException {
      try {
         return getCloudDAO().getById(cloudId, Cloud.class);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   /**
    * @return the cloudDAO
    */
   public ICloudDAO getCloudDAO () {
      return cloudDAO;
   }

   /**
    * @param cloudDAO
    *           the cloudDAO to set
    */
   public void setCloudDAO (ICloudDAO cloudDAO) {
      this.cloudDAO = cloudDAO;
   }

   public List<Cloud> getCloudsByCategory (CloudCategory category) throws KDXException {
      try {
         return getCloudDAO().getCloudsByCategory(category);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

}