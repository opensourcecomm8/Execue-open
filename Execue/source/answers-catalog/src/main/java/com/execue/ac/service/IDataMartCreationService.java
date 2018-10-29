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
import com.execue.ac.exception.DataMartCreationException;
import com.execue.core.common.bean.ac.MartCreationContext;

/**
 * This service is the wrapper to create data mart
 * 
 * @author Vishay
 * @version 1.0
 * @since 11/01/2011
 */
public interface IDataMartCreationService {

   /**
    * This method controls the data mart creation flow. It executes the steps defined to create data mart
    * 
    * @param martCreationContext
    * @return martCreationOutputInfo
    * @throws DataMartCreationException
    */
   public MartCreationOutputInfo dataMartCreation (MartCreationContext martCreationContext)
            throws DataMartCreationException;
}
