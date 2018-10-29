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

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.execue.core.common.bean.swi.PossibleAssetInfo;
import com.execue.core.common.bean.swi.UserQueryPossibility;
import com.execue.core.common.type.BusinessEntityType;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.dataaccess.swi.dao.IUserQueryPossibilityDAO;
import com.execue.swi.dataaccess.IUserQueryPossibilityManager;

/**
 * This class contains the operations for UserQueryPossiblity, PossibleAssetInfo object
 * 
 * @author Nitesh
 * @version 1.0
 * @since 23/12/09
 */
public class UserQueryPossibilityManagerImpl implements IUserQueryPossibilityManager {

   private IUserQueryPossibilityDAO userQueryPossibilityDAO;

   public void createUserQueryPossibilities (List<UserQueryPossibility> userQueryPossibilities)
            throws DataAccessException {
      getUserQueryPossibilityDAO().saveOrUpdateAll(userQueryPossibilities);
   }

   public List<PossibleAssetInfo> getPossibleAssetsForUserRequest (Long userQueryId) throws DataAccessException {
      return getUserQueryPossibilityDAO().getPossibleAssetsForUserRequest(userQueryId);
   }

   public List<UserQueryPossibility> getUserQueryPossibilities (Long userQueryId) throws DataAccessException {
      return getUserQueryPossibilityDAO().getAllUserQueryPossibilities(userQueryId);
   }

   public IUserQueryPossibilityDAO getUserQueryPossibilityDAO () {
      return userQueryPossibilityDAO;
   }

   public void setUserQueryPossibilityDAO (IUserQueryPossibilityDAO userQueryPossibilityDAO) {
      this.userQueryPossibilityDAO = userQueryPossibilityDAO;
   }

   public <DomainObject extends Serializable> DomainObject getById (Long id, Class<DomainObject> clazz)
            throws DataAccessException {
      return getUserQueryPossibilityDAO().getById(id, clazz);
   }

   public Long getUserQueryMaxExecutionDate () throws DataAccessException {
      return getUserQueryPossibilityDAO().getUserQueryMaxExecutionDate();
   }

   public void deleteUserQueryPossibilitiesByExecutionDate (Long executionDateTime) throws DataAccessException {
      Date executionDate = new Date(executionDateTime);
      getUserQueryPossibilityDAO().deleteUserQueryPossibilitiesByExecutionDate(executionDate);
   }

   public void deleteUserQueryPossibilitiesByEntityBedId (Long entityBedId, BusinessEntityType entityType)
            throws DataAccessException {
      getUserQueryPossibilityDAO().deleteUserQueryPossibilitiesByEntityBedId(entityBedId, entityType);
   }
}
