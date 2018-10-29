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


package com.execue.pseudolang.configuration.impl;

import com.execue.core.configuration.IConfiguration;
import com.execue.pseudolang.configuration.IPseudoLanguageConfigurationService;

/**
 * This class define the configuration constants and methods for Pseudo language services.
 * 
 * @author Jitendra
 * @version 1.0
 * @since 18/10/11
 */
public class PseudoLanguageConfigurationServiceImpl implements IPseudoLanguageConfigurationService {

   private IConfiguration pseudoLanguageConfiguration;

   /**
    * @return the pseudoLanguageConfiguration
    */
   public IConfiguration getPseudoLanguageConfiguration () {
      return pseudoLanguageConfiguration;
   }

   /**
    * @param pseudoLanguageConfiguration the pseudoLanguageConfiguration to set
    */
   public void setPseudoLanguageConfiguration (IConfiguration pseudoLanguageConfiguration) {
      this.pseudoLanguageConfiguration = pseudoLanguageConfiguration;
   }
}
