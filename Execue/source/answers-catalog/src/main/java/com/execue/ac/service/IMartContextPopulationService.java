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

import com.execue.ac.bean.MartCreationInputInfo;
import com.execue.ac.exception.MartContextPopulationException;
import com.execue.core.common.bean.ac.MartCreationContext;

/**
 * This service will populate the mart context. It means populate the input needed for mart creation.
 * 
 * @author Vishay
 * @version 1.0
 * @since 11/01/2011
 */
public interface IMartContextPopulationService {

   /**
    * This method populates the input required for building the mart. It populates the information from swi,
    * configuration etc.
    * 
    * @param martCreationContext
    * @return mart creation input info
    * @throws MartContextPopulationException
    */
   public MartCreationInputInfo populateMartCreationInputInfo (MartCreationContext martCreationContext)
            throws MartContextPopulationException;
}
