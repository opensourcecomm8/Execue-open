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


package com.execue.uswhda.dataaccess.dao.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.orm.hibernate3.HibernateTemplate;

import com.execue.dataaccess.AbstractCrudDAO;
import com.execue.dataaccess.callback.HibernateCallbackParameterInfo;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.uswhda.configuration.impl.UnstructuredWHPooledDataManager;
import com.execue.uswhda.dataaccess.dao.IUnstructuredWHContextCrudDAO;

/**
 * This class performs crud operations using datasource location based on context passed.
 * 
 * @author Vishay
 * @version 1.0
 * @since 27/08/11
 */
public abstract class UnstructuredWHContextCrudDAOImpl extends AbstractCrudDAO implements IUnstructuredWHContextCrudDAO {

   private UnstructuredWHPooledDataManager unstructuredWHPooledDataManager;

   public <DomainObject extends Serializable> DomainObject create (Long contextId, final DomainObject serializableObject)
            throws DataAccessException {
      return super.create(getHibernateTemplate(contextId), serializableObject);
   }

   public <DomainObject extends Serializable> void createAll (Long contextId,
            final List<DomainObject> serializableObjects) throws DataAccessException {
      super.createAll(getHibernateTemplate(contextId), serializableObjects);
   }

   public <DomainObject extends Serializable> void saveOrUpdateAll (Long contextId,
            final List<DomainObject> serializableObjects) throws DataAccessException {
      super.saveOrUpdateAll(getHibernateTemplate(contextId), serializableObjects);
   }

   public <DomainObject extends Serializable> void saveOrUpdate (Long contextId, final DomainObject serializableObject)
            throws DataAccessException {
      super.saveOrUpdate(getHibernateTemplate(contextId), serializableObject);
   }

   public <DomainObject extends Serializable> void delete (Long contextId, final DomainObject serializableObject)
            throws DataAccessException {
      super.delete(getHibernateTemplate(contextId), serializableObject);
   }

   public <DomainObject extends Serializable> void deleteAll (Long contextId,
            final List<DomainObject> serializableObjects) throws DataAccessException {
      super.deleteAll(getHibernateTemplate(contextId), serializableObjects);
   }

   public <DomainObject extends Serializable> void update (Long contextId, final DomainObject serializableObject)
            throws DataAccessException {
      super.update(getHibernateTemplate(contextId), serializableObject);
   }

   public <DomainObject extends Serializable> void merge (Long contextId, final DomainObject serializableObject)
            throws DataAccessException {
      super.merge(getHibernateTemplate(contextId), serializableObject);
   }

   public <DomainObject extends Serializable> void updateAll (Long contextId,
            final List<DomainObject> serializableObjects) throws DataAccessException {
      super.updateAll(getHibernateTemplate(contextId), serializableObjects);
   }

   public <DomainObject extends Serializable> DomainObject getById (Long contextId, final Long id,
            final Class<DomainObject> clazz) throws DataAccessException {
      return super.getById(getHibernateTemplate(contextId), id, clazz);
   }

   public List<? extends Serializable> getByIds (Long contextId, List<Long> ids, Class<?> clazz)
            throws DataAccessException {
      return super.getByIds(getHibernateTemplate(contextId), ids, clazz);
   }

   public <DomainObject extends Serializable> DomainObject getByName (Long contextId, final String name,
            final Class<DomainObject> clazz) throws DataAccessException {
      return super.getByName(getHibernateTemplate(contextId), name, clazz);
   }

   public <DomainObject extends Serializable> DomainObject getByName (Long contextId, final String name,
            final Class<DomainObject> clazz, boolean ignoreCase) throws DataAccessException {
      return super.getByName(getHibernateTemplate(contextId), name, clazz);
   }

   public <DomainObject extends Serializable> DomainObject getByStringField (Long contextId, final String fieldValue,
            final String fieldName, final Class<DomainObject> clazz, boolean ignoreCase) throws DataAccessException {
      return super.getByStringField(getHibernateTemplate(contextId), fieldValue, fieldName, clazz, ignoreCase);
   }

   public <DomainObject extends Serializable> DomainObject getByField (Long contextId, final String fieldValue,
            final String fieldName, final Class<DomainObject> clazz) throws DataAccessException {
      return super.getByField(getHibernateTemplate(contextId), fieldValue, fieldName, clazz);
   }

   public <DomainObject extends Serializable> DomainObject getByField (Long contextId, final Long fieldValue,
            final String fieldName, final Class<DomainObject> clazz) throws DataAccessException {
      return super.getByField(getHibernateTemplate(contextId), fieldValue, fieldName, clazz);
   }

   public Long getCount (Long contextId, String query, Map<String, Object> paramsMap) throws DataAccessException {
      return super.getCount(getHibernateTemplate(contextId), query, paramsMap);
   }

   public Object executeNativeStatement (Long contextId, String nativeStatement) throws DataAccessException {
      return super.executeNativeStatement(getHibernateTemplate(contextId), nativeStatement);
   }

   public Object executeNativeStatement (Long contextId, String nativeStatement,
            List<HibernateCallbackParameterInfo> parameters) throws DataAccessException {
      return super.executeNativeStatement(getHibernateTemplate(contextId), nativeStatement, parameters);
   }

   protected HibernateTemplate getHibernateTemplate (Long contextId) throws DataAccessException {
      return unstructuredWHPooledDataManager.getHibernateTemplate(contextId);
   }

   public UnstructuredWHPooledDataManager getUnstructuredWHPooledDataManager () {
      return unstructuredWHPooledDataManager;
   }

   public void setUnstructuredWHPooledDataManager (UnstructuredWHPooledDataManager unstructuredWHPooledDataManager) {
      this.unstructuredWHPooledDataManager = unstructuredWHPooledDataManager;
   }

}