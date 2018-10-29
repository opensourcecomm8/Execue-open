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


package com.execue.ac.algorithm.optimaldsetoldversion;

import com.execue.ac.exception.AnswersCatalogException;

/**
 * This service is used to invoke the optimalDSet algorithm
 * 
 * @author Vishay
 * @version 1.0
 * @since 25/06/09
 */
public interface IOptimalDsetInvocationService {

   public void invokeOptimalDSetAlgorithm (Long applicationId, Long modelId) throws AnswersCatalogException;
}
