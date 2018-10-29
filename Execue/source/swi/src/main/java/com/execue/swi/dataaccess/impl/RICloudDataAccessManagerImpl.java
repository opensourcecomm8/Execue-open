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


package com.execue.swi.dataaccess.impl;

import java.util.ArrayList;
import java.util.List;

import com.execue.core.common.bean.entity.RICloud;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.swi.exception.KDXException;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.dataaccess.swi.dao.ICloudDAO;
import com.execue.swi.dataaccess.IRICloudDataAccessManager;

public class RICloudDataAccessManagerImpl implements IRICloudDataAccessManager {

   private ICloudDAO cloudDAO;

   public void createRICloud (RICloud riCloud) throws KDXException {
      try {
         cloudDAO.create(riCloud);
      } catch (DataAccessException e) {
         throw new KDXException(e.code, e.getMessage(), e);
      }
   }

   public void deleteRICloud (RICloud riCloud) throws KDXException {
      try {
         cloudDAO.delete(riCloud);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public ICloudDAO getCloudDAO () {
      return cloudDAO;
   }

   public void setCloudDAO (ICloudDAO cloudDAO) {
      this.cloudDAO = cloudDAO;
   }

   public void truncateRICloudByCloudId (Long cloudId) throws KDXException {
      try {
         getCloudDAO().truncateRICloudByCloudId(cloudId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }
   
   public void clearRICloudEntriesByComponentBEId (Long cloudId, Long componentBEId) throws KDXException {
      try {
         List<RICloud> riClouds = new ArrayList<RICloud>();
         RICloud parentRICloud = getCloudDAO().getRICloudByComponentBEId(cloudId, componentBEId);
         if (parentRICloud != null) {
            riClouds.add(parentRICloud);
            RICloud childRICloud = getCloudDAO().getRICloudByPrimaryRICloudId(cloudId, parentRICloud.getId());
            if (childRICloud != null) {
               riClouds.add(childRICloud);
            }
         }
         if (ExecueCoreUtil.isCollectionNotEmpty(riClouds)) {
            getCloudDAO().deleteAll(riClouds);
         }
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }
}
