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


package com.execue.governor.service;

import java.util.List;

import com.execue.core.common.bean.BusinessCondition;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.governor.EntityMappingInfo;
import com.execue.core.common.bean.governor.StructuredCondition;
import com.execue.governor.exception.GovernorException;

/**
 * This class will handle the time frame related conversions and will act as a helper to governor.
 * 
 * @author Vishay Gupta
 * @version 1.0
 * @since 24/08/10
 */
public interface IGovernorTimeFrameHandlerService {

   public StructuredCondition buildTimeFrameCondition (BusinessCondition businessCondition,
            List<EntityMappingInfo> entityMappings, Asset asset) throws GovernorException;

}
