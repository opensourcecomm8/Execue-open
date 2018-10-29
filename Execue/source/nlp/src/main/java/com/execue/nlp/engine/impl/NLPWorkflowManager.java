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


/**
 * 
 */
package com.execue.nlp.engine.impl;

import org.apache.log4j.Logger;

import com.execue.nlp.engine.barcode.possibility.PossibilityController;
import com.execue.nlp.exception.NLPExceptionCodes;
import com.execue.nlp.exception.NLPSystemException;

/**
 * @author Nitesh
 */
public class NLPWorkflowManager {

   private Logger logger = Logger.getLogger(NLPWorkflowManager.class);

   public void findSemantics (PossibilityController pController) {
      try {
         pController.findSemantics();
      } catch (NLPSystemException exception) {
         if (exception.getCode() == NLPExceptionCodes.MAX_ALLOWED_TIME_EXCEEDED && logger.isDebugEnabled()) {
            logger.debug(
                     "Time out for NLP Type Cloud processing...proceeding with the possibilities processed so far...",
                     exception);
         } else {
            throw exception;
         }
      }

   }

   public void enhanceSemantics (PossibilityController pController) throws NLPSystemException {
      try {
         pController.enhanceSemantics();
      } catch (NLPSystemException exception) {
         if (exception.getCode() == NLPExceptionCodes.MAX_ALLOWED_TIME_EXCEEDED && logger.isDebugEnabled()) {
            logger.debug(
                     "Time out for NLP App Cloud processing...proceeding with the possibilities processed so far...",
                     exception);
         } else {
            throw exception;
         }
      }

   }
}
