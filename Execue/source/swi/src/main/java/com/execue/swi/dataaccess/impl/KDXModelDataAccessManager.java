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

import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.Cloud;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.swi.exception.KDXException;
import com.execue.swi.dataaccess.IKDXModelDataAccessManager;
import com.execue.swi.dataaccess.KDXDAOComponents;

/**
 * @author nihar
 */
public class KDXModelDataAccessManager extends KDXDAOComponents implements IKDXModelDataAccessManager {

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.swi.dataaccess.IKDXModelDataAccessManager#createCloud(com.execue.core.common.bean.entity.Cloud)
    */
   public void createCloud(Cloud cloud) throws KDXException {
      try {
         getCloudDAO().create(cloud);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public Cloud getCloudByName(String cloudName) throws KDXException {
      try {
         return getCloudDAO().getCloudByName(cloudName);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.swi.dataaccess.IKDXModelDataAccessManager#addBusinessEntityToCloud(com.execue.core.common.bean.entity.BusinessEntityDefinition)
    */
   public void addBusinessEntityToCloud(BusinessEntityDefinition bed) throws KDXException {
      try {
         getBusinessEntityDefinitionDAO().update(bed);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }
}