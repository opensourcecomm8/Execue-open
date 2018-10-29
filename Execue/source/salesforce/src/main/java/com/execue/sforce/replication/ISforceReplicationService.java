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


package com.execue.sforce.replication;

import com.execue.sforce.bean.SforceLoginContext;
import com.execue.sforce.exception.SforceException;

/**
 * This interface controls the replication flow for sforce user
 * 
 * @author Vishay
 * @version 1.0
 * @since 11/08/09
 */
public interface ISforceReplicationService {

   public boolean startReplication (SforceLoginContext sforceLoginContext) throws SforceException;

   public boolean replicateSforceMeta (SforceLoginContext sforceLoginContext) throws SforceException;

   public boolean replicateSforceData (SforceLoginContext sforceLoginContext) throws SforceException;

   public boolean startDataSyncUpProcess (SforceLoginContext sforceLoginContext) throws SforceException;

   public boolean startMetaSyncUpProcess (SforceLoginContext sforceLoginContext) throws SforceException;
}
