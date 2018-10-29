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

import java.util.List;

import com.execue.core.common.bean.entity.Join;
import com.execue.core.common.bean.entity.JoinDefinition;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.swi.exception.JoinException;
import com.execue.swi.dataaccess.IJoinDataAccessManager;
import com.execue.swi.dataaccess.JoinDAOComponents;
import com.execue.swi.exception.SWIExceptionCodes;

/**
 * This class implements the data access manager methods for joins(direct and indirect joins in the system)
 * 
 * @author Vishay
 * @version 1.0
 * @since 10/03/09
 */
public class JoinDataAccessManagerImpl extends JoinDAOComponents implements IJoinDataAccessManager {

   public List<Join> getDirectAssetJoins (Long assetId) throws JoinException {
      try {
         return getJoinDAO().getDirectAssetJoins(assetId);
      } catch (DataAccessException dataAccessException) {
         throw new JoinException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public List<Join> getInDirectAssetJoins (Long assetId) throws JoinException {
      try {
         return getJoinDAO().getInDirectAssetJoins(assetId);
      } catch (DataAccessException dataAccessException) {
         throw new JoinException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public List<Join> getAllAssetJoins (Long assetId) throws JoinException {
      try {
         return getJoinDAO().getAllAssetJoins(assetId);
      } catch (DataAccessException dataAccessException) {
         throw new JoinException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public List<JoinDefinition> getExistingDirectJoinDefinitions (Long assetId, String sourceTableName,
            String destinationTableName) throws JoinException {
      try {
         return getJoinDAO().getExistingDirectJoinDefinitions(assetId, sourceTableName, destinationTableName);
      } catch (DataAccessException dataAccessException) {
         throw new JoinException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public List<JoinDefinition> getAllExistingJoinDefinitions (Long assetId, String sourceTableName,
            String destinationTableName) throws JoinException {
      try {
         return getJoinDAO().getAllExistingJoinDefinitions(assetId, sourceTableName, destinationTableName);
      } catch (DataAccessException dataAccessException) {
         throw new JoinException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public List<JoinDefinition> getSuggestedJoinDefinitions (Long assetId, String sourceTableName,
            String destinationTableName) throws JoinException {
      try {
         return getJoinDAO().getSuggestedJoinDefinitions(assetId, sourceTableName, destinationTableName);
      } catch (DataAccessException dataAccessException) {
         throw new JoinException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public void createJoins (List<Join> joins) throws JoinException {
      try {
         getJoinDAO().createJoins(joins);
      } catch (DataAccessException dataAccessException) {
         throw new JoinException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public void deleteDirectJoins (List<Join> joins) throws JoinException {
      try {
         getJoinDAO().deleteDirectJoins(joins);
      } catch (DataAccessException dataAccessException) {
         throw new JoinException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public void deleteIndirectJoins (List<Join> joins) throws JoinException {
      try {
         getJoinDAO().deleteAll(joins);
      } catch (DataAccessException dataAccessException) {
         throw new JoinException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public int getIndirectJoinsMaxLength (Long assetId) throws JoinException {
      try {
         return getJoinDAO().getIndirectJoinsMaxLength(assetId);
      } catch (DataAccessException dataAccessException) {
         throw new JoinException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public List<Join> getAllExistingJoins (Long assetId, String tableName) throws JoinException {
      try {
         return getJoinDAO().getAllExistingJoins(assetId, tableName);
      } catch (DataAccessException e) {
         throw new JoinException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public List<Long> getTablesParticipatingInJoins (Long assetId) throws JoinException {
      try {
         return getJoinDAO().getTablesParticipatingInJoins(assetId);
      } catch (DataAccessException e) {
         throw new JoinException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public List<Join> suggestDirectJoinsByForeignKey (Long assetId) throws JoinException {
      try {
         return getJoinDAO().suggestDirectJoinsByForeignKey(assetId);
      } catch (DataAccessException e) {
         throw new JoinException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public List<Join> suggestIndirectJoinsForDirectJoins (Long assetId, int maxHops) throws JoinException {
      try {
         return getJoinDAO().suggestIndirectJoinsForDirectJoins(assetId, maxHops);
      } catch (DataAccessException dataAccessException) {
         throw new JoinException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

}
