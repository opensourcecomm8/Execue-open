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


package com.execue.sdata.configuration.impl;

import com.execue.core.configuration.IConfiguration;
import com.execue.sdata.configuration.ISharedDataConfigurationService;

/**
 * This class define the configuration constants and methods  that will be further used by shared data services.
 * 
 * @author Jitendra
 * @version 1.0
 * @since 18/10/11
 */
public class SharedDataConfigurationServiceImpl implements ISharedDataConfigurationService {

   private IConfiguration sdataConfiguration;

   /**
    * @return the sdataConfiguration
    */
   public IConfiguration getSdataConfiguration () {
      return sdataConfiguration;
   }

   /**
    * @param sdataConfiguration the sdataConfiguration to set
    */
   public void setSdataConfiguration (IConfiguration sdataConfiguration) {
      this.sdataConfiguration = sdataConfiguration;
   }

}
