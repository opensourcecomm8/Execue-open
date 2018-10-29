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


package com.execue.ac.service;

import com.execue.ac.bean.MartCreationOutputInfo;
import com.execue.ac.bean.MartFractionalDataSetTempTableStructure;
import com.execue.ac.bean.MartFractionalDatasetTableStructure;
import com.execue.ac.exception.MartSfactorPopulationException;

/**
 * This service represents the step3 of mart creation. It means create actual population table from each dimensional
 * table created in step2. Populate the sfactor for each record in each created table needs to be populated.
 * 
 * @author Vishay
 * @version 1.0
 * @since 11/01/2011
 */
public interface IMartSfactorPopulationService {

   /**
    * This method creates the fractional dataset table for each dimension. It uses the information from temp table
    * created and process the islands one by one to create fractional dataset table.
    * 
    * @param martCreationOutputInfo
    * @param fractionalDataSetTempTable
    * @param dimension
    * @return martFractionalDatasetTableStructure
    * @throws MartSfactorPopulationException
    */
   public MartFractionalDatasetTableStructure sfactorPopulation (MartCreationOutputInfo martCreationOutputInfo,
            MartFractionalDataSetTempTableStructure fractionalDataSetTempTable, String dimension)
            throws MartSfactorPopulationException;
}
