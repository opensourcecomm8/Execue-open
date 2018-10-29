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
package com.execue.swi.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.swi.PossibleAssetInfo;
import com.execue.core.common.bean.swi.UserQueryPossibility;
import com.execue.core.common.type.AppSourceType;
import com.execue.core.common.type.BusinessEntityType;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.swi.configuration.ISWIConfigurationService;
import com.execue.swi.dataaccess.IUserQueryPossibilityManager;
import com.execue.swi.exception.SDXException;
import com.execue.swi.exception.SWIException;
import com.execue.swi.exception.SWIExceptionCodes;
import com.execue.swi.service.ISDXRetrievalService;
import com.execue.swi.service.IUserQueryPossibilityService;

/**
 * @author Nitesh
 */
public class UserQueryPossibilityServiceImpl implements IUserQueryPossibilityService {

   private IUserQueryPossibilityManager userQueryPossibilityManager;
   private ISDXRetrievalService         sdxRetrievalService;
   private ISWIConfigurationService     swiConfigurationService;

   public IUserQueryPossibilityManager getUserQueryPossibilityManager () {
      return userQueryPossibilityManager;
   }

   public void setUserQueryPossibilityManager (IUserQueryPossibilityManager userQueryPossibilityManager) {
      this.userQueryPossibilityManager = userQueryPossibilityManager;
   }

   public void createUserQueryPossibilities (List<UserQueryPossibility> userQueryPossibility) throws SWIException {
      try {
         getUserQueryPossibilityManager().createUserQueryPossibilities(userQueryPossibility);
      } catch (DataAccessException e) {
         throw new SWIException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public List<PossibleAssetInfo> getPossibleAssetsForUserRequest (Long userQueryId) throws SWIException {
      try {
         return getUserQueryPossibilityManager().getPossibleAssetsForUserRequest(userQueryId);
      } catch (DataAccessException e) {
         throw new SWIException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public List<UserQueryPossibility> getUserQueryPossibilities (Long userQueryId) throws SWIException {
      try {
         return getUserQueryPossibilityManager().getUserQueryPossibilities(userQueryId);
      } catch (DataAccessException e) {
         throw new SWIException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.swi.IUserQueryPossibilityService#filterByAssetSecurity(java.util.List)
    */
   public Set<Asset> filterByAssetSecurity (List<PossibleAssetInfo> possibleAssetInfoList) throws SWIException {
      Set<Asset> answerableLightAssets = new HashSet<Asset>();
      List<Long> answerableAssetIds = new ArrayList<Long>();
      for (PossibleAssetInfo possibleAssetInfo : possibleAssetInfoList) {
         answerableAssetIds.add(possibleAssetInfo.getAssetId());
      }
      answerableLightAssets.addAll(getLightAssets(answerableAssetIds));
      return answerableLightAssets;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.swi.IUserQueryPossibilityService#loadAnswerableAssets(java.util.Set)
    */
   public Set<Asset> loadAnswerableAssets (List<PossibleAssetInfo> possibleAssetInfos,
            List<PossibleAssetInfo> cachePossibleAssetInfos) throws SWIException {
      Set<Asset> loadedAnswerableAssets = new LinkedHashSet<Asset>();
      for (PossibleAssetInfo possibleAssetInfo : possibleAssetInfos) {
         if (possibleAssetInfo.getApplicationType() == AppSourceType.UNSTRUCTURED) {
            continue;
         }
         if (!possibleAssetInfo.isFromQueryCache()) {
            loadedAnswerableAssets.add(getSdxRetrievalService().getAsset(possibleAssetInfo.getAssetId()));
         } else {
            cachePossibleAssetInfos.add(possibleAssetInfo);
         }
      }
      return loadedAnswerableAssets;
   }

   public void deleteUserQueryPossibilitiesByEntityBedId (Long entityBedId, BusinessEntityType entityType)
            throws SWIException {
      try {
         getUserQueryPossibilityManager().deleteUserQueryPossibilitiesByEntityBedId(entityBedId, entityType);
      } catch (DataAccessException e) {
         e.printStackTrace();
         throw new SWIException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }

   }

   /**
    * Return the asset object loaded with just the id
    * 
    * @param assetId
    * @return
    * @throws SDXException
    */
   private List<Asset> getLightAssets (List<Long> assetIds) throws SDXException {
      return getSdxRetrievalService().getLightAssets(assetIds);
   }

   public void cleanUserQueryPossibilities () throws SWIException {
      try {
         Long userQueryMaxExecutionDate = userQueryPossibilityManager.getUserQueryMaxExecutionDate();
         if (userQueryMaxExecutionDate != null) {
            // Time should be in ms.
            Long maintainUserQueryPossiblityTime = getSwiConfigurationService()
                     .getUserQueryPossiblilityTimeInMillisecond();
            userQueryPossibilityManager.deleteUserQueryPossibilitiesByExecutionDate(userQueryMaxExecutionDate
                     - maintainUserQueryPossiblityTime);
         }
      } catch (DataAccessException e) {
         e.printStackTrace();
         throw new SWIException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public ISDXRetrievalService getSdxRetrievalService () {
      return sdxRetrievalService;
   }

   public void setSdxRetrievalService (ISDXRetrievalService sdxRetrievalService) {
      this.sdxRetrievalService = sdxRetrievalService;
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