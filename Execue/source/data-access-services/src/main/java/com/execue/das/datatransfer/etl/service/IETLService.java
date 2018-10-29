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


package com.execue.das.datatransfer.etl.service;

import com.execue.das.datatransfer.etl.bean.ETLInput;
import com.execue.das.datatransfer.etl.bean.DataTransferQueryStatus;
import com.execue.das.datatransfer.etl.exception.ETLException;

/**
 * @author Jayadev
 */
public interface IETLService {

   /**
    * Executes a specific ETL process (scriptella/kettle) that transfers data from the source to staging space; the data
    * transfer component checks on the integrity of the data transferred before moving it into permanent form, i.e., a
    * cube/mart
    * 
    * @param etlInput
    * @throws ETLException
    */
   public DataTransferQueryStatus executeETLProcess (ETLInput etlInput) throws ETLException;

}
