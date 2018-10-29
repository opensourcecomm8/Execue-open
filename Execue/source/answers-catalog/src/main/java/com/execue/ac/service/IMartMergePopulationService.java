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
import com.execue.ac.bean.MartFractionalPopulationTableStructure;
import com.execue.ac.exception.MartMergePopulationException;

/**
 * This service represents the step 4 of mart. It means merge all population tables and de-dup them and create final
 * samples population table.
 * 
 * @author Vishay
 * @version 1.0
 * @since 11/01/2011
 */
public interface IMartMergePopulationService {

   /**
    * This method merges all the fractional datasets creates one per dimension. It de-dups the records and creates final
    * merged table which contains unique sampled population which will be used for extraction of the warehouse.
    * 
    * @param martCreationOutputInfo
    * @return martFractionalPopulationTableStructure
    * @throws MartMergePopulationException
    */
   public MartFractionalPopulationTableStructure mergeSampledPopulation (MartCreationOutputInfo martCreationOutputInfo)
            throws MartMergePopulationException;
}
