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


package com.execue.sforce.dataaccess.impl;

import java.io.Serializable;
import java.util.Date;

import com.execue.core.exception.dataaccess.DataAccessException;
import com.execue.sforce.bean.entity.SObjectMetaEntity;
import com.execue.sforce.dataaccess.ISforceDAO;
import com.execue.sforce.dataaccess.ISforceDataAccessService;
import com.execue.sforce.exception.SforceException;
import com.execue.sforce.exception.SforceExceptionCodes;

/**
 * This service contains methods to work on sforce tables
 * 
 * @author Vishay
 * @version 1.0
 * @since 20/08/09
 */
public class SforceDataAccessServiceImpl implements ISforceDataAccessService {

   private ISforceDAO sforceDAO;

   public Date getLastModifiedData (String objectName) throws SforceException {
      return getSforceDAO().getLastModifiedDataDate(objectName);
   }

   public Date getLastModifiedData (Long sobjectMetaId) throws SforceException {
      return getSforceDAO().getLastModifiedDataDate(sobjectMetaId);
   }

   public Date getLastModifiedMeta (String objectName) throws SforceException {
      return getSforceDAO().getLastModifiedMetaDate(objectName);
   }

   public Date getLastModifiedMeta (Long sobjectMetaId) throws SforceException {
      return getSforceDAO().getLastModifiedMetaDate(sobjectMetaId);
   }

   public void createSobjectMetaEntity (SObjectMetaEntity sobjectMetaEntity) throws SforceException {

      try {
         getSforceDAO().create(sobjectMetaEntity);
      } catch (DataAccessException e) {
         throw new SforceException(SforceExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }

   }

   public void deleteSobjectMetaEntity (SObjectMetaEntity sobjectMetaEntity) throws SforceException {
      try {
         getSforceDAO().delete(sobjectMetaEntity);
      } catch (DataAccessException e) {
         throw new SforceException(SforceExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }

   }

   public <DomainObject extends Serializable> DomainObject getById (Long id, Class<DomainObject> clazz)
            throws SforceException {
      try {
         return getSforceDAO().getById(id, clazz);
      } catch (DataAccessException e) {
         throw new SforceException(SforceExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public void updateSobjectMetaEntity (SObjectMetaEntity sobjectMetaEntity) throws SforceException {
      try {
         getSforceDAO().update(sobjectMetaEntity);
      } catch (DataAccessException e) {
         throw new SforceException(SforceExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }

   }

   public ISforceDAO getSforceDAO () {
      return sforceDAO;
   }

   public void setSforceDAO (ISforceDAO sforceDAO) {
      this.sforceDAO = sforceDAO;
   }

   public SObjectMetaEntity getSObjectMetaByName (String sobjectName) throws SforceException {
      SObjectMetaEntity sObjectMetaEntity = null;
      try {
         sObjectMetaEntity = getSforceDAO().getByField(sobjectName, "SOBJECT_NAME"  , SObjectMetaEntity.class);
      } catch (DataAccessException e) {
         throw new SforceException(SforceExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
      return sObjectMetaEntity;
   }
}
