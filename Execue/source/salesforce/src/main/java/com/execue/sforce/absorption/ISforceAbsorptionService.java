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


package com.execue.sforce.absorption;

import java.util.List;

import com.execue.core.common.bean.ExeCueResultSet;
import com.execue.core.common.bean.entity.DataSource;
import com.execue.sforce.bean.SObjectNormalizedData;
import com.execue.sforce.bean.SObjectNormalizedMeta;
import com.execue.sforce.exception.SforceException;

/**
 * This interface contains the absorption services to target data source
 * 
 * @author Vishay
 * @version 1.0
 * @since 11/08/09
 */
public interface ISforceAbsorptionService {

   /**
    * This method will absorb the sObjectNormalizedDataat the data source location specified. Returns status about the
    * progress
    * 
    * @param dataSource
    * @param sObjectNormalizedData
    * @return status
    * @throws SforceException
    */
   public boolean absorbSObjectData (DataSource dataSource, SObjectNormalizedData sObjectNormalizedData)
            throws SforceException;

   public boolean executeDefinitionStatement (DataSource dataSource, String ddlStatement) throws SforceException;

   public ExeCueResultSet executeSelectStatement (DataSource dataSource, String selectStatement) throws SforceException;

   public boolean executeManipulationStatement (DataSource dataSource, String dmlStatement,
            List<Object> parameterValueRecords, List<Integer> parameterTypes) throws SforceException;

   public boolean executeManipulationStatements (DataSource dataSource, String dmlStatement,
            List<List<Object>> parameterValueRecords, List<Integer> parameterTypes) throws SforceException;

   /**
    * This method will absorb the sObjectNormalizedMeta at the data source location specified. Returns status about the
    * progress
    * 
    * @param dataSource
    * @param sObjectNormalizedMeta
    * @return status
    * @throws SforceException
    */
   public boolean absorbSObjectMeta (DataSource dataSource, SObjectNormalizedMeta sObjectNormalizedMeta)
            throws SforceException;
}
