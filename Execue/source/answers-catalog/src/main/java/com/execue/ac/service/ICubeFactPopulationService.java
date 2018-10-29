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

import com.execue.ac.bean.CubeCreationOutputInfo;
import com.execue.ac.bean.CubeFactTableStructure;
import com.execue.ac.bean.CubeUpdationOutputInfo;
import com.execue.ac.exception.CubeFactPopulationException;

/**
 * This service is used for populating the fact table for cube.
 * 
 * @author Vishay
 * @version 1.0
 * @since 17/02/2011
 */
public interface ICubeFactPopulationService {

   public CubeFactTableStructure createCubeFactTable (CubeCreationOutputInfo cubeCreationOutputInfo)
            throws CubeFactPopulationException;

   public CubeFactTableStructure manageCubeFactTable (CubeUpdationOutputInfo cubeUpdationOutputInfo)
            throws CubeFactPopulationException;
}
