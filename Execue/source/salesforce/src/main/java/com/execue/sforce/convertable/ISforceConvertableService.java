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


package com.execue.sforce.convertable;

import java.util.List;

import com.execue.core.common.bean.querygen.QueryColumn;
import com.execue.sforce.bean.SObjectColumn;
import com.execue.sforce.bean.SObjectNormalizedData;
import com.execue.sforce.bean.SObjectNormalizedMeta;
import com.execue.sforce.bean.SObjectTable;
import com.execue.sforce.exception.SforceException;

/**
 * This interface populates the normalized sObjects
 * 
 * @author Vishay
 * @version 1.0
 * @since 18/08/09
 */
public interface ISforceConvertableService {

   public SObjectNormalizedData populateSObjectNormalizedData (SObjectTable sObjectTable,
            List<QueryColumn> queryColumns, List<List<String>> soapResponseData) throws SforceException;

   public SObjectNormalizedMeta populateSObjectNormalizedMeta (SObjectTable sObjectTable,
            List<SObjectColumn> sobjectColumns) throws SforceException;
}
