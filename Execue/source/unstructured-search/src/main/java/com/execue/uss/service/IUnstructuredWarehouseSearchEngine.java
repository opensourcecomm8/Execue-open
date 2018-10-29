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


package com.execue.uss.service;

import java.util.List;
import java.util.Set;

import com.execue.core.common.bean.nlp.SemanticPossibility;
import com.execue.core.common.bean.security.UserRemoteLocationInfo;
import com.execue.core.common.bean.uss.UniversalUnstructuredSearchResult;

/**
 * @author Nitesh
 * @since April 26, 2011 : 11:45:11 AM
 */
public interface IUnstructuredWarehouseSearchEngine {

   public List<UniversalUnstructuredSearchResult> performWarehouseSearch (Set<SemanticPossibility> possibilities,
            UserRemoteLocationInfo userRemoteLocationInfo, Long userQueryId);
}
