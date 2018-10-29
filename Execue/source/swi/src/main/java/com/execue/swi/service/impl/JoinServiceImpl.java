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


package com.execue.swi.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.execue.core.common.bean.entity.Join;
import com.execue.core.common.bean.entity.JoinDefinition;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.swi.configuration.ISWIConfigurationService;
import com.execue.swi.dataaccess.IJoinDataAccessManager;
import com.execue.swi.exception.JoinException;
import com.execue.swi.service.IJoinService;

/**
 * This class implements the service methods for joins(direct and indirect joins in the system)
 * 
 * @author Vishay
 * @version 1.0
 * @since 10/03/09
 */
public class JoinServiceImpl implements IJoinService {

   private ISWIConfigurationService swiConfigurationService;
   private IJoinDataAccessManager   joinDataAccessManager;

   public List<Join> getDirectAssetJoins (Long assetId) throws JoinException {
      return getJoinDataAccessManager().getDirectAssetJoins(assetId);
   }

   public List<Join> getInDirectAssetJoins (Long assetId) throws JoinException {
      return getJoinDataAccessManager().getInDirectAssetJoins(assetId);
   }

   public List<Join> getAllAssetJoins (Long assetId) throws JoinException {
      return getJoinDataAccessManager().getAllAssetJoins(assetId);
   }

   public List<JoinDefinition> getExistingDirectJoinDefinitions (Long assetId, String sourceTableName,
            String destinationTableName) throws JoinException {
      return getJoinDataAccessManager()
               .getExistingDirectJoinDefinitions(assetId, sourceTableName, destinationTableName);
   }

   public List<JoinDefinition> getAllExistingJoinDefinitions (Long assetId, String sourceTableName,
            String destinationTableName) throws JoinException {
      return getJoinDataAccessManager().getAllExistingJoinDefinitions(assetId, sourceTableName, destinationTableName);
   }

   public List<JoinDefinition> getSuggestedJoinDefinitions (Long assetId, String sourceTableName,
            String destinationTableName) throws JoinException {
      return getJoinDataAccessManager().getSuggestedJoinDefinitions(assetId, sourceTableName, destinationTableName);
   }

   public List<Join> getAllExistingJoins (Long assetId, String tableName) throws JoinException {
      return getJoinDataAccessManager().getAllExistingJoins(assetId, tableName);
   }

   public List<Join> suggestDirectJoinsByForeignKey (Long assetId) throws JoinException {
      return getJoinDataAccessManager().suggestDirectJoinsByForeignKey(assetId);
   }

   public List<Join> suggestIndirectJoinsForDirectJoins (Long assetId) throws JoinException {
      int maxHops = getJoinDataAccessManager().getIndirectJoinsMaxLength(assetId);
      // check if maxHops is zero, it means there are no indirect joins in the system, so we will create with default
      // Hops because addition of new join can create a possibility of having a indirect join
      if (maxHops == 0) {
         maxHops = getSwiConfigurationService().getMaxHopsForIndirectJoins();
      }
      return getJoinDataAccessManager().suggestIndirectJoinsForDirectJoins(assetId, maxHops);
   }

   public void createJoins (Long assetId, List<Join> joins) throws JoinException {
      List<Join> existingIndirectAssetJoins = getInDirectAssetJoins(assetId);
      if (ExecueCoreUtil.isCollectionNotEmpty(existingIndirectAssetJoins)) {
         getJoinDataAccessManager().deleteIndirectJoins(existingIndirectAssetJoins);
      }
      List<Join> directJoins = new ArrayList<Join>();
      for (Join join : joins) {
         if (join.getJoinLength() == 0) {
            directJoins.add(join);
         }
      }
      if (ExecueCoreUtil.isCollectionNotEmpty(directJoins)) {
         getJoinDataAccessManager().deleteDirectJoins(joins);
      }
      getJoinDataAccessManager().createJoins(joins);
   }

   public void deleteJoins (Long assetId, List<Join> joins) throws JoinException {
      List<Join> existingIndirectAssetJoins = getInDirectAssetJoins(assetId);
      if (ExecueCoreUtil.isCollectionNotEmpty(existingIndirectAssetJoins)) {
         getJoinDataAccessManager().deleteIndirectJoins(existingIndirectAssetJoins);
      }
      List<Join> directJoins = new ArrayList<Join>();
      for (Join join : joins) {
         if (join.getJoinLength() == 0) {
            directJoins.add(join);
         }
      }
      if (ExecueCoreUtil.isCollectionNotEmpty(directJoins)) {
         getJoinDataAccessManager().deleteDirectJoins(joins);
      }
   }

   public List<Long> getTableIdsParticipatingInJoins (Long assetId) throws JoinException {
      return joinDataAccessManager.getTablesParticipatingInJoins(assetId);
   }

   public void rebuildIndirectJoins (Long assetId) throws JoinException {
      List<Join> freshIndirectJoins = suggestIndirectJoinsForDirectJoins(assetId);
      createJoins(assetId, freshIndirectJoins);
   }

   public IJoinDataAccessManager getJoinDataAccessManager () {
      return joinDataAccessManager;
   }

   public void setJoinDataAccessManager (IJoinDataAccessManager joinDataAccessManager) {
      this.joinDataAccessManager = joinDataAccessManager;
   }

   /**
    * @return the swiConfigurationService
    */
   public ISWIConfigurationService getSwiConfigurationService () {
      return swiConfigurationService;
   }

   /**
    * @param swiConfigurationService the swiConfigurationService to set
    */
   public void setSwiConfigurationService (ISWIConfigurationService swiConfigurationService) {
      this.swiConfigurationService = swiConfigurationService;
   }

}