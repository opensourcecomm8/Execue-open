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


package com.execue.swi.service;

import java.util.List;
import java.util.Set;

import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.swi.PossibleAssetInfo;
import com.execue.core.common.bean.swi.UserQueryPossibility;
import com.execue.core.common.type.BusinessEntityType;
import com.execue.swi.exception.SWIException;

public interface IUserQueryPossibilityService {

   public void createUserQueryPossibilities (List<UserQueryPossibility> userQueryPossibilities) throws SWIException;

   public List<UserQueryPossibility> getUserQueryPossibilities (Long userQueryId) throws SWIException;

   public List<PossibleAssetInfo> getPossibleAssetsForUserRequest (Long userQueryId) throws SWIException;

   /**
    * Return the Assets populated with only the id. As the security (if applied) might discard some of the assets on top
    * of the result from this method. So, loading assets to the full is not required.
    * 
    * @param possibleAssetInfo
    * @return
    * @throws SWIException
    */
   public Set<Asset> filterByAssetSecurity (List<PossibleAssetInfo> possibleAssetInfo) throws SWIException;

   /**
    * Load the assets passed in to the full (with data source and application references)
    * 
    * @param answerableAssets
    * @return
    * @throws SWIException
    */
   public Set<Asset> loadAnswerableAssets (List<PossibleAssetInfo> possibleAssetInfos,
            List<PossibleAssetInfo> cachePossibleAssetInfos) throws SWIException;

   /**
    * clean user query possibilities
    * 
    * @throws SWIException
    */
   public void cleanUserQueryPossibilities () throws SWIException;

   public void deleteUserQueryPossibilitiesByEntityBedId (Long entityBedId, BusinessEntityType entityType)
            throws SWIException;
}