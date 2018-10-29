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

import java.util.Date;
import java.util.List;

import com.execue.core.common.bean.Page;
import com.execue.core.common.bean.entity.unstructured.UserQueryFeatureInformation;
import com.execue.core.common.bean.uss.UniversalUnstructuredSearchResult;
import com.execue.core.common.bean.uss.UnstructuredSearchInput;
import com.execue.dataaccess.exception.DataAccessException;

public interface IUserQueryFeatureInformationDAO extends IUnstructuredWHContextCrudDAO {

   public List<UserQueryFeatureInformation> getUserQueryFeatureInformation (Long contextId, Long userQueryId)
            throws DataAccessException;

   public UniversalUnstructuredSearchResult getUnstructuredSearchResult (Long contextId,
            UnstructuredSearchInput unstructuredSearchInput, Page page) throws DataAccessException;

   public void deleteUserQuerFeatureInfoByExecutionDate (Long contextId, Date executionDate) throws DataAccessException;

   public Long getUserQueryFeatureInfoMaxExecutionDate (Long contextId) throws DataAccessException;

   public void saveUserQueryFeatureInformation (Long contextId,
            List<UserQueryFeatureInformation> userQueryFeatureInformationList) throws DataAccessException;

}
