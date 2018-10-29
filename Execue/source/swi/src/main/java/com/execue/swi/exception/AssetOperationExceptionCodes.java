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


package com.execue.swi.exception;

import com.execue.core.exception.ExeCueExceptionCodes;

/**
 * This class contains the exception codes for asset operation process
 * 
 * @author Vishay
 * @version 1.0
 * @since 20/08/09
 */
public interface AssetOperationExceptionCodes extends ExeCueExceptionCodes {

   public int FETCHING_ASSET_OPERATION_DATA_FAILED       = 7600;
   public int TABLE_SYNC_DATA_ABSORPTION_FAILED          = 7601;
   public int COLUMN_SYNC_DATA_ABSORPTION_FAILED         = 7602;
   public int EMEMBER_SYNC_DATA_ABSORPTION_FAILED        = 7603;
   public int ASSET_OPERATION_CREATION_FAILED            = 7604;
   public int HISTORY_ASSET_OPERATION_CREATION_FAILED    = 7605;
   public int ASSET_OPERATION_RETRIEVAL_FAILED           = 7606;
   public int ASSET_OPERATION_DATA_RETRIEVAL_FAILED      = 7607;
   public int ASSET_OPERATION_ENTRY_NOTFOUND_BY_ID       = 7608;
   public int ASSET_OPERATION_DATA_NOTFOUND_BY_ID        = 7609;
   public int ASSET_OPERATION_ENTRY_NOTFOUND_BY_ASSET_ID = 7610;
   public int ASSET_OPERATION_DATA_NOTFOUND_BY_ASSET_ID  = 7611;
   public int ASSET_OPERATION_DELETION_FAILED            = 7612;
   public int HISTORY_ASSET_OPERATION_DELETION_FAILED    = 7613;
   public int ASSET_OPERATION_UPDATION_FAILED            = 7614;
   public int HISTORY_ASSET_OPERATION_UPDATION_FAILED    = 7615;
}
