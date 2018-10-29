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


package com.execue.platform;

import com.execue.core.common.bean.entity.Instance;
import com.execue.platform.exception.PlatformException;

/**
 * @author Vishay
 */

// TODO : - VG- Only instance as business entities are handled.
public interface IRealTimeBusinessEntityWrapperService {

   public void createInstance (Long modelId, Long conceptId, Instance instance) throws PlatformException;

   public void updateInstance (Long modelId, Long conceptId, Instance instance) throws PlatformException;

   public Long createTypeInstance (Long modelId, Long typeModelGroupId, Long typeId, Instance instance)
            throws PlatformException;

}
