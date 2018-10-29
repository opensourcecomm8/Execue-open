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


package com.execue.content.preprocessor.configuration.impl;

import com.execue.content.preprocessor.configuration.IContentPreProcessorConfigurationService;
import com.execue.core.configuration.IConfiguration;

/**
 * @author John Mallavalli
 */
public class ContentPreProcessorConfigurationServiceImpl implements IContentPreProcessorConfigurationService {

   private IConfiguration contentPreProcessorConfiguration;

   private String         MAX_CHARS_PER_SENTENCE_KEY = "content.preprocessor.max-chars-per-sentence";

   @Override
   public Integer getMaximumCharactersPerSentence () {
      return getContentPreProcessorConfiguration().getInt(MAX_CHARS_PER_SENTENCE_KEY);
   }

   public IConfiguration getContentPreProcessorConfiguration () {
      return contentPreProcessorConfiguration;
   }

   public void setContentPreProcessorConfiguration (IConfiguration contentPreProcessorConfiguration) {
      this.contentPreProcessorConfiguration = contentPreProcessorConfiguration;
   }
}
