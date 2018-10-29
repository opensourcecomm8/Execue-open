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


package com.execue.swi.dataaccess;

import java.util.List;

import com.execue.core.common.bean.entity.Join;
import com.execue.core.common.bean.entity.JoinDefinition;
import com.execue.swi.exception.JoinException;

/**
 * This interface defines the data access manager methods for joins(direct and indirect joins in the system)
 * 
 * @author Vishay
 * @version 1.0
 * @since 10/03/09
 */
public interface IJoinDataAccessManager {

   public void deleteDirectJoins (List<Join> joins) throws JoinException;

   public List<JoinDefinition> getExistingDirectJoinDefinitions (Long assetId, String sourceTableName,
            String destinationTableName) throws JoinException;

   public List<JoinDefinition> getAllExistingJoinDefinitions (Long assetId, String sourceTableName,
            String destinationTableName) throws JoinException;

   public List<JoinDefinition> getSuggestedJoinDefinitions (Long assetId, String sourceTableName,
            String destinationTableName) throws JoinException;

   public List<Join> suggestIndirectJoinsForDirectJoins (Long assetId, int maxHops) throws JoinException;

   public List<Join> getDirectAssetJoins (Long assetId) throws JoinException;

   public int getIndirectJoinsMaxLength (Long assetId) throws JoinException;

   public List<Join> getAllExistingJoins (Long assetId, String tableName) throws JoinException;

   public List<Long> getTablesParticipatingInJoins (Long assetId) throws JoinException;

   public List<Join> getAllAssetJoins (Long assetId) throws JoinException;

   public void createJoins (List<Join> joins) throws JoinException;

   public List<Join> suggestDirectJoinsByForeignKey (Long assetId) throws JoinException;

   public List<Join> getInDirectAssetJoins (Long assetId) throws JoinException;

   public void deleteIndirectJoins (List<Join> joins) throws JoinException;

}
