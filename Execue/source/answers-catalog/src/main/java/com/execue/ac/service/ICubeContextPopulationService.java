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

import com.execue.ac.bean.CubeCreationInputInfo;
import com.execue.ac.bean.CubeUpdationContext;
import com.execue.ac.bean.CubeUpdationInputInfo;
import com.execue.ac.exception.CubeContextPopulationException;
import com.execue.core.common.bean.ac.CubeCreationContext;

/**
 * This service used to populate the cube creation and updation context
 * 
 * @author Vishay
 * @version 1.0
 * @since 17/02/2011
 */
public interface ICubeContextPopulationService {

   public CubeCreationInputInfo populateCubeCreationInputInfo (CubeCreationContext cubeCreationContext)
            throws CubeContextPopulationException;

   public CubeUpdationInputInfo populateCubeUpdationInputInfo (CubeUpdationContext cubeUpdationContext)
            throws CubeContextPopulationException;

}
