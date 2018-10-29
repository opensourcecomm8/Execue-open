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


package com.execue.report.configuration.service.imp;

import com.execue.core.configuration.IConfigurable;
import com.execue.core.exception.ConfigurationException;

/**
 * This class will populate reporting data  at system boot time.
 * 
 * @author Jitendra
 * @version 1.0
 * @since 13/10/11
 */
public class PresentationConfigurableService implements IConfigurable {

   private PresentationConfigurationServiceImpl presentationConfigurationService;

   public void doConfigure () throws ConfigurationException {
      getPresentationConfigurationService().loadChartFxMeta();
      getPresentationConfigurationService().loadGridFxMeta();
   }

   public void reConfigure () throws ConfigurationException {
      doConfigure();
   }

   /**
    * @return the presentationConfigurationService
    */
   public PresentationConfigurationServiceImpl getPresentationConfigurationService () {
      return presentationConfigurationService;
   }

   /**
    * @param presentationConfigurationService the presentationConfigurationService to set
    */
   public void setPresentationConfigurationService (
            PresentationConfigurationServiceImpl presentationConfigurationService) {
      this.presentationConfigurationService = presentationConfigurationService;
   }

}
