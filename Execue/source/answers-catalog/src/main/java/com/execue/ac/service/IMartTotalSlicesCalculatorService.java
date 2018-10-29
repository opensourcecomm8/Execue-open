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

import com.execue.ac.bean.SlicingAlgorithmStaticInput;
import com.execue.ac.exception.AnswersCatalogException;

/**
 * This service will populate the number of slices based on total records in population table.
 * 
 * @author Vishay
 * @version 1.0
 * @since 11/01/2011
 */
public interface IMartTotalSlicesCalculatorService {

   /**
    * This method runs the heuristics to calculate the numberOfSlices and if it falls out of range then we take default
    * from configuration.
    * 
    * @param slicingAlgorithmStaticInput
    * @param totalPopulationRecordCount
    * @return numberOfSlices
    * @throws AnswersCatalogException
    */
   public Integer calculateTotalSlices (SlicingAlgorithmStaticInput slicingAlgorithmStaticInput,
            Long totalPopulationRecordCount) throws AnswersCatalogException;
}
