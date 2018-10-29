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


package com.execue.driver.semantic.configuration.impl;

import com.execue.core.configuration.IConfiguration;
import com.execue.driver.semantic.configuration.ISemanticDriverConfigurationService;

/**
 * This class define the configuration constants and methods  for semantic driver.
 * 
 * @author Jitendra
 * @version 1.0
 * @since 18/10/11
 */
public class SemanticDriverConfigurationServiceImpl implements ISemanticDriverConfigurationService {

   private IConfiguration semanticDriverConfiguration;

   /**
    * @return the semanticDriverConfiguration
    */
   public IConfiguration getSemanticDriverConfiguration () {
      return semanticDriverConfiguration;
   }

   /**
    * @param semanticDriverConfiguration the semanticDriverConfiguration to set
    */
   public void setSemanticDriverConfiguration (IConfiguration semanticDriverConfiguration) {
      this.semanticDriverConfiguration = semanticDriverConfiguration;
   }

}
