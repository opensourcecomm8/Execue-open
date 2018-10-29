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
 * This class contains the exception codes for asset synchronization process
 * 
 * @author Vishay
 * @version 1.0
 * @since 20/08/09
 */
public interface AssetAnalysisExceptionCodes extends ExeCueExceptionCodes {

   int NON_MEMBER_LOOKUP_ANALYSIS_FAILED = 80001;
   int COLUMN_WITH_NO_KDXTYPE_FAILED     = 80002;
   int NON_JOIN_TABLES_FAILED            = 80003;
   int UN_MAPPED_TABLES_COLUMNS_FAILED   = 80004;
   int UN_MAPPED_TABLES_MEMBERS_FAILED   = 80005;
   int ASSET_GRAIN_CHECK_FAILED          = 80006;

}
