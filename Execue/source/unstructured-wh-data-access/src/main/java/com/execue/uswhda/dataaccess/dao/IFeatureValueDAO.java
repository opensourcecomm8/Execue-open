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

import java.util.Collection;
import java.util.List;

import com.execue.core.common.bean.entity.unstructured.FeatureValue;
import com.execue.dataaccess.exception.DataAccessException;

public interface IFeatureValueDAO extends IUnstructuredWHContextCrudDAO {

   public List<FeatureValue> getFeatureValue (Long contextId, Collection<Long> featureValueBedIds)
            throws DataAccessException;

   public FeatureValue getFeatureValueByFeatureValueBEDID (Long contextId, Long featureValueBEDId)
            throws DataAccessException;

   public void deleteFeatureValuesByFeatureId (Long contextId, Long featureId) throws DataAccessException;

   // public List<FeatureValue> getFeatureValueByFeatureIdAndBatchSize (Long contextId, Long featureId, int batchSize)
   // throws DataAccessException;

   public FeatureValue getFeatureValueByFeatureIdAndFeatureValue (Long contextId, Long featureId, String featureValue)
            throws DataAccessException;
}
