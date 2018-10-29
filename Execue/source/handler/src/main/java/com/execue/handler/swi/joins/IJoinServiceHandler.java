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


package com.execue.handler.swi.joins;

import java.util.List;

import com.execue.handler.bean.UIColumnForJoins;
import com.execue.handler.bean.UIJoinDefintionInfo;
import com.execue.handler.bean.UIJoinInfo;
import com.execue.handler.bean.UITableForJoins;
import com.execue.swi.exception.JoinException;

public interface IJoinServiceHandler {

   public void deleteJoins (Long assetId, List<UIJoinInfo> deleteJoins) throws JoinException;

   public void persistJoins (Long assetId, String sourceTableName, String destTableName,
            List<UIJoinDefintionInfo> uiJoinDefintionInfo) throws JoinException;

   public List<UIJoinDefintionInfo> getExistingJoinDefinitions (Long assetId, String sourceTableName,
            String destinationTableName) throws JoinException;

   public List<UIJoinDefintionInfo> getSuggestedJoinDefinitions (Long assetId, String sourceTableName,
            String destinationTableName) throws JoinException;

   public List<UIJoinInfo> getAssetJoins (long assetId) throws JoinException;

   public List<UIColumnForJoins> getColForJoinTables (long assetId, String tableName) throws JoinException;

   public List<UITableForJoins> getAssetTables (long assetId) throws JoinException;
}
