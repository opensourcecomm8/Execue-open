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


package com.execue.content.postprocessor.configuration.impl;

import com.execue.content.postprocessor.configuration.IContentPostProcessorConfigurationService;
import com.execue.core.configuration.IConfiguration;

/**
 * @author Nitesh
 */
public class ContentPostProcessorConfigurationServiceImpl implements IContentPostProcessorConfigurationService {

   private IConfiguration      contentPostProcessorConfiguration;
   private static final String QUALITY_THRESHOLD_FOR_INSTANCE = "qualityThresholdForInstance";

   @Override
   public double getQualityThresholdForInstance () {
      return getContentPostProcessorConfiguration().getDouble(QUALITY_THRESHOLD_FOR_INSTANCE);
   }

   /**
    * @return the contentPostProcessorConfiguration
    */
   public IConfiguration getContentPostProcessorConfiguration () {
      return contentPostProcessorConfiguration;
   }

   /**
    * @param contentPostProcessorConfiguration the contentPostProcessorConfiguration to set
    */
   public void setContentPostProcessorConfiguration (IConfiguration contentPostProcessorConfiguration) {
      this.contentPostProcessorConfiguration = contentPostProcessorConfiguration;
   }
}
