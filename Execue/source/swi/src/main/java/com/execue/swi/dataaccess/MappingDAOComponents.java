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

import com.execue.dataaccess.swi.dao.IBusinessEntityDefinitionDAO;
import com.execue.dataaccess.swi.dao.IDefaultMetricDAO;
import com.execue.dataaccess.swi.dao.IMappingDAO;
import com.execue.dataaccess.swi.dao.IOntoReverseIndexDAO;
import com.execue.dataaccess.swi.dao.IPopularityHitDAO;

public abstract class MappingDAOComponents {

   private IOntoReverseIndexDAO         ontoReverseIndexDAO;
   private IMappingDAO                  mappingDAO;
   private IPopularityHitDAO            popularityHitDAO;
   private IDefaultMetricDAO            defaultMetricDAO;
   private IBusinessEntityDefinitionDAO businessEntityDefinitionDAO;

   public IOntoReverseIndexDAO getOntoReverseIndexDAO () {
      return ontoReverseIndexDAO;
   }

   public void setOntoReverseIndexDAO (IOntoReverseIndexDAO ontoReverseIndexDAO) {
      this.ontoReverseIndexDAO = ontoReverseIndexDAO;
   }

   public IMappingDAO getMappingDAO () {
      return mappingDAO;
   }

   public void setMappingDAO (IMappingDAO mappingDAO) {
      this.mappingDAO = mappingDAO;
   }

   public IPopularityHitDAO getPopularityHitDAO () {
      return popularityHitDAO;
   }

   public void setPopularityHitDAO (IPopularityHitDAO popularityHitDAO) {
      this.popularityHitDAO = popularityHitDAO;
   }

   public IDefaultMetricDAO getDefaultMetricDAO () {
      return defaultMetricDAO;
   }

   public void setDefaultMetricDAO (IDefaultMetricDAO defaultMetricDAO) {
      this.defaultMetricDAO = defaultMetricDAO;
   }

   public IBusinessEntityDefinitionDAO getBusinessEntityDefinitionDAO () {
      return businessEntityDefinitionDAO;
   }

   public void setBusinessEntityDefinitionDAO (IBusinessEntityDefinitionDAO businessEntityDefinitionDAO) {
      this.businessEntityDefinitionDAO = businessEntityDefinitionDAO;
   }

}
