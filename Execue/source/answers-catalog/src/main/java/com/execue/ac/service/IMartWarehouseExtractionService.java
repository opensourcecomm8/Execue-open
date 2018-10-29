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

import java.util.List;

import com.execue.ac.bean.MartCreationOutputInfo;
import com.execue.ac.bean.MartWarehouseTableStructure;
import com.execue.ac.exception.MartWarehouseExtractionException;

/**
 * This service represents the step5 of mart creation process. In this step we will copy source into AC based on sampled
 * population.
 * 
 * @author Vishay
 * @version 1.0
 * @since 11/01/2011
 */
public interface IMartWarehouseExtractionService {

   /**
    * This method will extract the warehouse (source asset tables) for mart asset based on sampled population table
    * created.
    * 
    * @param martCreationOutputInfo
    * @return MartWarehouseTableStructure list
    * @throws MartWarehouseExtractionException
    */
   public List<MartWarehouseTableStructure> extractWarehouse (MartCreationOutputInfo martCreationOutputInfo)
            throws MartWarehouseExtractionException;
}
