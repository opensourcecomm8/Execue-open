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

import java.util.Date;
import java.util.List;

import com.execue.core.util.ExeCueUtils;
import com.execue.dataaccess.swi.dao.impl.CrudDAOImpl;
import com.execue.sforce.dataaccess.ISforceDAO;
import com.execue.sforce.exception.SforceException;

/**
 * This DAO contains methods to work on sforce tables
 * 
 * @author Vishay
 * @version 1.0
 * @since 20/08/09
 */
public class SforceDAOImpl extends CrudDAOImpl implements ISforceDAO {

   private static final String QUERY_GET_LAST_MODIFIED_DATA_DATE_FOR_NAME = "select sobjectMetaEntity.lastModifiedData "
                                                                                   + "from SObjectMetaEntity sobjectMetaEntity "
                                                                                   + "where sobjectMetaEntity.sobjectName=:sobjectName";

   private static final String QUERY_GET_LAST_MODIFIED_DATA_DATE_FOR_ID   = "select sobjectMetaEntity.lastModifiedData "
                                                                                   + "from SObjectMetaEntity sobjectMetaEntity "
                                                                                   + "where sobjectMetaEntity.id=:id";

   private static final String QUERY_GET_LAST_MODIFIED_META_DATE_FOR_NAME = "select sobjectMetaEntity.lastModifiedMeta "
                                                                                   + "from SObjectMetaEntity sobjectMetaEntity "
                                                                                   + "where sobjectMetaEntity.sobjectName=:sobjectName";

   private static final String QUERY_GET_LAST_MODIFIED_META_DATE_FOR_ID   = "select sobjectMetaEntity.lastModifiedMeta "
                                                                                   + "from SObjectMetaEntity sobjectMetaEntity "
                                                                                   + "where sobjectMetaEntity.id=:id";

   public Date getLastModifiedDataDate (String sobjectName) throws SforceException {
      Date modifiedDateDate = null;
      List<Date> modifiedDataDates = getHibernateTemplate().findByNamedParam(
               QUERY_GET_LAST_MODIFIED_DATA_DATE_FOR_NAME, "sobjectName", sobjectName);
      if (!ExeCueUtils.isCollectionEmpty(modifiedDataDates))
         modifiedDateDate = modifiedDataDates.get(0);
      return modifiedDateDate;
   }

   public Date getLastModifiedDataDate (Long id) throws SforceException {
      Date modifiedDateDate = null;
      List<Date> modifiedDataDates = getHibernateTemplate().findByNamedParam(QUERY_GET_LAST_MODIFIED_DATA_DATE_FOR_ID,
               "id", id);
      if (!ExeCueUtils.isCollectionEmpty(modifiedDataDates))
         modifiedDateDate = modifiedDataDates.get(0);
      return modifiedDateDate;
   }

   public Date getLastModifiedMetaDate (String sobjectName) throws SforceException {
      Date modifiedDateDate = null;
      List<Date> modifiedDataDates = getHibernateTemplate().findByNamedParam(
               QUERY_GET_LAST_MODIFIED_META_DATE_FOR_NAME, "sobjectName", sobjectName);
      return modifiedDateDate;
   }

   public Date getLastModifiedMetaDate (Long id) throws SforceException {
      Date modifiedDateDate = null;
      List<Date> modifiedDataDates = getHibernateTemplate().findByNamedParam(QUERY_GET_LAST_MODIFIED_META_DATE_FOR_ID,
               "id", id);
      return modifiedDateDate;
   }

}
