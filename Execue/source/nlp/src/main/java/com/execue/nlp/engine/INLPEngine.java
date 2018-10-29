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


//
// Project : Execue NLP
// File Name : NLPEngine.java
// Date : 7/22/2008
// Author : Kaliki
//
//

package com.execue.nlp.engine;

import com.execue.core.common.bean.nlp.NLPInformation;
import com.execue.core.common.bean.nlp.UserQuery;
import com.execue.nlp.exception.NLPException;

/**
 * This is NLP Engine Interface.
 * 
 * @author kaliki
 */

public interface INLPEngine {

   /*
    * Process user request. Only function what will be called for NLP processing
    */
   public NLPInformation processQuery (String query) throws NLPException;

   /**
    * Information will be passed as part of bean userQuery
    * 
    * @param userQuery
    * @return
    * @throws NLPException
    */
   public NLPInformation processQuery (UserQuery userQuery, String contextName) throws NLPException;

}
