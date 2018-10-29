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


package com.execue.content.preprocessor.service.impl;

import com.execue.content.preprocessor.exception.ContentPreProcessorException;
import com.execue.content.preprocessor.helper.ContentPreProcessorHelper;
import com.execue.content.preprocessor.service.IEscapeUnescapeContentProcessor;

/**
 * @author John Mallavalli
 */
public class EscapeUnescapeContentProcessorImpl implements IEscapeUnescapeContentProcessor {

   @Override
   public String processContent (String newsItemLine, Long applicationId) throws ContentPreProcessorException {
      return ContentPreProcessorHelper.getActualCharsFromEscapeChars(newsItemLine);
   }
}