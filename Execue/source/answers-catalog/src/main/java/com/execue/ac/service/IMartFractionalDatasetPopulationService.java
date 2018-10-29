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
import com.execue.ac.exception.MartFractionalDatasetPopulationException;

/**
 * This service represents the step2 of mart creation. It means for each dimension create a fractional table along with
 * distributions from cube or ETL.
 * 
 * @author Vishay
 * @version 1.0
 * @since 11/01/2011
 */
public interface IMartFractionalDatasetPopulationService {

   /**
    * This method creates the fractional dataset table per dimension passed to it, There are four scenarios here 1. Cube
    * has the dimension so we can populate the data from cube 2. Cube is in same location as target data source 3. cube
    * is in different location as target data source 4. Cube doesn't have dimension, we are using source to populate 5.
    * Source is in same place as target 6. Source is in different place as target In nutshell, if source data source
    * whether coming from cube or source is same as target data source, then we can use insert into select else ETL
    * 
    * @param martCreationOutputInfo
    * @param dimensionName
    * @return martFractionalDataSetTempTableStructure
    * @throws MartFractionalDatasetPopulationException
    */
   public MartFractionalDataSetTempTableStructure createFractionalDataset (
            MartCreationOutputInfo martCreationOutputInfo, String dimensionName)
            throws MartFractionalDatasetPopulationException;
}
