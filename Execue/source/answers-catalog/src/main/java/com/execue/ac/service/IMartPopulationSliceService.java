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
import com.execue.ac.bean.MartPopulationTableStructure;
import com.execue.ac.exception.MartPopulationSliceException;

/**
 * This service represents the step1 of mart creation. It creates population table copy and assign slice_number to each
 * record.
 * 
 * @author Vishay
 * @version 1.0
 * @since 11/01/2011
 */
public interface IMartPopulationSliceService {

   /**
    * This method slices the population across numberOfSlices using random number approach.
    * 
    * @param martCreationOutputInfo
    * @return martPopulationTableStructure
    * @throws MartPopulationSliceException
    */
   public MartPopulationTableStructure populationSlicing (MartCreationOutputInfo martCreationOutputInfo)
            throws MartPopulationSliceException;
}
