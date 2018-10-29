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


package com.execue.uswhda.dataaccess.dao;

import java.io.Serializable;
import java.util.List;

import com.execue.dataaccess.callback.HibernateCallbackParameterInfo;
import com.execue.dataaccess.exception.DataAccessException;

/**
 * This interface contains basic DAO operations. Any serializable object can be saved using the methods defined here.
 * 
 * @author Vishay
 * @version 1.0
 * @since 09/01/09
 */
public interface IUnstructuredWHContextCrudDAO {

   /**
    * This Method will save the serialiazble object passed to it in the DB.
    * 
    * @param serializableObject
    * @return DomainObject - the object passed with the Id inserted.
    */
   public <DomainObject extends Serializable> DomainObject create (Long contextId, final DomainObject serializableObject)
            throws DataAccessException;

   public <DomainObject extends Serializable> void createAll (Long contextId,
            final List<DomainObject> serializableObjects) throws DataAccessException;

   /**
    * Saves all the objects in the list by using saveOrUpdateAll method on hibernate template
    * 
    * @param <DomainObject>
    * @param serializableObjects
    * @throws DataAccessException
    */
   public <DomainObject extends Serializable> void saveOrUpdateAll (Long contextId,
            final List<DomainObject> serializableObjects) throws DataAccessException;

   public <DomainObject extends Serializable> void saveOrUpdate (Long contextId, final DomainObject serializableObject)
            throws DataAccessException;

   /**
    * This Method will delete the serialiazble object passed to it in the DB.
    * 
    * @param serializableObject
    */
   public <DomainObject extends Serializable> void delete (Long contextId, final DomainObject serializableObject)
            throws DataAccessException;

   public <DomainObject extends Serializable> void deleteAll (Long contextId,
            final List<DomainObject> serializableObjects) throws DataAccessException;

   /**
    * This Method will update the serialiazble object passed to it in the DB.
    * 
    * @param serializableObject
    */
   public <DomainObject extends Serializable> void update (Long contextId, final DomainObject serializableObject)
            throws DataAccessException;

   public <DomainObject extends Serializable> void updateAll (Long contextId,
            final List<DomainObject> serializableObjects) throws DataAccessException;

   /**
    * Generic method to get the object by id. Object should have an id column named as id. A Crude implementation -
    * Revise it
    * 
    * @param id -
    *           Long value of the id field
    * @param clazz -
    *           class of the Object that implements serializable
    * @return
    * @throws DataAccessException
    */
   public <DomainObject extends Serializable> DomainObject getById (Long contextId, final Long id,
            final Class<DomainObject> clazz) throws DataAccessException;

   // TODO -RG- Refine the implementation of getById method

   public List<? extends Serializable> getByIds (Long contextId, final List<Long> ids, final Class<?> clazz)
            throws DataAccessException;

   /**
    * Generic method to get the object by name. Object should have an name column named as name. A Crude implementation -
    * Revise it
    */
   public <DomainObject extends Serializable> DomainObject getByName (Long contextId, final String name,
            final Class<DomainObject> clazz) throws DataAccessException;

   /**
    * Generic method to get the object by name. Object should have an name column named as name.<br/>if ignoreCase is
    * true then checks the name by ignoreing the case<br/>A Crude implementation - Revise it
    */
   public <DomainObject extends Serializable> DomainObject getByName (Long contextId, final String name,
            final Class<DomainObject> clazz, boolean ignoreCase) throws DataAccessException;

   /**
    * Generic method to get the object by a field and value combination.
    * 
    * @param <DomainObject>
    * @param fieldValue
    * @param fieldName
    * @param clazz
    * @return
    * @throws DataAccessException
    */
   public <DomainObject extends Serializable> DomainObject getByField (Long contextId, final String fieldValue,
            final String fieldName, final Class<DomainObject> clazz) throws DataAccessException;

   public <DomainObject extends Serializable> DomainObject getByField (Long contextId, final Long fieldValue,
            final String fieldName, final Class<DomainObject> clazz) throws DataAccessException;

   public <DomainObject extends Serializable> DomainObject getByStringField (Long contextId, final String fieldValue,
            final String fieldName, final Class<DomainObject> clazz, boolean ignoreCase) throws DataAccessException;

   public <DomainObject extends Serializable> void merge (Long contextId, final DomainObject serializableObject)
            throws DataAccessException;

   public Object executeNativeStatement (Long contextId, String nativeStatement) throws DataAccessException;

   public Object executeNativeStatement (Long contextId, String nativeStatement,
            List<HibernateCallbackParameterInfo> parameters) throws DataAccessException;
}
