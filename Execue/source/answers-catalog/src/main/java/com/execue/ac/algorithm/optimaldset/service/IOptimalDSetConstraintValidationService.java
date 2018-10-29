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


package com.execue.ac.algorithm.optimaldset.service;

import java.util.List;

import com.execue.ac.algorithm.optimaldset.bean.OptimalDSetCandidateDimensionPattern;
import com.execue.ac.algorithm.optimaldset.bean.OptimalDSetLevelInputInfo;
import com.execue.ac.algorithm.optimaldset.bean.OptimalDSetLevelOutputInfo;
import com.execue.ac.exception.AnswersCatalogException;

/**
 * @author Vishay
 * @version 1.0
 * @since 18/02/2012
 */
public interface IOptimalDSetConstraintValidationService {

   public OptimalDSetLevelOutputInfo validateConstraints (
            List<OptimalDSetCandidateDimensionPattern> currentLevelPatterns, OptimalDSetLevelInputInfo levelInputInfo)
            throws AnswersCatalogException;
}
