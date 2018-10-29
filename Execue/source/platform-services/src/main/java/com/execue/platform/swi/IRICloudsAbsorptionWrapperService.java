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


package com.execue.platform.swi;

import com.execue.core.common.bean.entity.Cloud;
import com.execue.swi.exception.KDXException;

/**
 * This service manages the CRUD operations associated with RIClouds
 * 
 * @author John Mallavalli
 */
public interface IRICloudsAbsorptionWrapperService {

   /**
    * This method generates RICloud entries for given Cloud. Truncate the existing entries for he given cloud and then
    * regenerate the entries a fresh from the Cloud' Components
    * 
    * @throws KDXException
    */
   public void regenerateRICloudFromCloud (Cloud cloud, Long modelId) throws KDXException;

   public void truncateRICloudByCloudId (Long cloudId) throws KDXException;

}
