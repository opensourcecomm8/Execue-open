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

import java.util.ArrayList;
import java.util.List;

import com.execue.content.preprocessor.bean.PreProcessorInput;
import com.execue.content.preprocessor.configuration.IContentPreProcessorConfigurationService;
import com.execue.content.preprocessor.exception.ContentPreProcessorException;
import com.execue.content.preprocessor.helper.ContentPreProcessorHelper;
import com.execue.content.preprocessor.service.IContentCleanupPatternProcessor;
import com.execue.content.preprocessor.service.IContentMarkupTagCleanupProcessor;
import com.execue.content.preprocessor.service.IContentMeaningExtractingProcessor;
import com.execue.content.preprocessor.service.IContentPreProcessingService;
import com.execue.content.preprocessor.service.IEscapeUnescapeContentProcessor;
import com.execue.content.preprocessor.service.IUnwantedContentCleanupProcessor;
import com.execue.core.util.ExecueCoreUtil;

/**
 * @author John Mallavalli
 */
public class ContentPreProcessingServiceImpl implements IContentPreProcessingService {

   private IContentMarkupTagCleanupProcessor        contentMarkupTagCleanupProcessor;
   private IContentMeaningExtractingProcessor       contentMeaningExtractingProcessor;
   private IContentCleanupPatternProcessor          contentCleanupPatternProcessor;
   private IEscapeUnescapeContentProcessor          escapeUnescapeContentProcessor;
   private IUnwantedContentCleanupProcessor         unwantedContentCleanupProcessor;
   private IContentPreProcessorConfigurationService contentPreProcessorConfigurationService;

   @Override
   public List<String> preProcess (PreProcessorInput preProcessorInput) throws ContentPreProcessorException {
      List<String> newsItemLinesToSemantify = new ArrayList<String>(1);
      Long applicationId = preProcessorInput.getApplicationId();
      String processedContent = preProcessorInput.getInput();
      // 1. Un-escaping of special chars
      processedContent = getEscapeUnescapeContentProcessor()
               .processContent(preProcessorInput.getInput(), applicationId);

      // 2. Meaning extraction from HTML Blocks
      processedContent = getContentMeaningExtractingProcessor().processContent(processedContent, applicationId);

      // 3. Cleaning up of HTML tags
      processedContent = getContentMarkupTagCleanupProcessor().processContent(processedContent, applicationId);

      // 4. Truncation of unwanted portion of the article (content stating similar items, ads etc.)
      // Take the List of strings from configuration after the presence of the string, we will take off
      // every thing from the article for processing purposes
      processedContent = getUnwantedContentCleanupProcessor().processContent(processedContent, applicationId);

      // 5. pattern based content cleanup/modification A service based on ContentCleanupPattern
      // object list obtained from DB by application id
      processedContent = getContentCleanupPatternProcessor().processContent(processedContent, applicationId);

      if (preProcessorInput.isTitle()) {
         if (ExecueCoreUtil.isNotEmpty(processedContent)) {
            newsItemLinesToSemantify.add(processedContent);
         }
      } else {
         int tokenLimit = getContentPreProcessorConfigurationService().getMaximumCharactersPerSentence();
         if (ExecueCoreUtil.isNotEmpty(processedContent)) {
            newsItemLinesToSemantify
                     .addAll(ContentPreProcessorHelper.getLinesFromSummary(processedContent, tokenLimit));
         }
      }
      return newsItemLinesToSemantify;
   }

   public IContentMarkupTagCleanupProcessor getContentMarkupTagCleanupProcessor () {
      return contentMarkupTagCleanupProcessor;
   }

   public void setContentMarkupTagCleanupProcessor (IContentMarkupTagCleanupProcessor contentMarkupTagCleanupProcessor) {
      this.contentMarkupTagCleanupProcessor = contentMarkupTagCleanupProcessor;
   }

   public IContentMeaningExtractingProcessor getContentMeaningExtractingProcessor () {
      return contentMeaningExtractingProcessor;
   }

   public void setContentMeaningExtractingProcessor (
            IContentMeaningExtractingProcessor contentMeaningExtractingProcessor) {
      this.contentMeaningExtractingProcessor = contentMeaningExtractingProcessor;
   }

   public IContentCleanupPatternProcessor getContentCleanupPatternProcessor () {
      return contentCleanupPatternProcessor;
   }

   public void setContentCleanupPatternProcessor (IContentCleanupPatternProcessor contentCleanupPatternProcessor) {
      this.contentCleanupPatternProcessor = contentCleanupPatternProcessor;
   }

   public IEscapeUnescapeContentProcessor getEscapeUnescapeContentProcessor () {
      return escapeUnescapeContentProcessor;
   }

   public void setEscapeUnescapeContentProcessor (IEscapeUnescapeContentProcessor escapeUnescapeContentProcessor) {
      this.escapeUnescapeContentProcessor = escapeUnescapeContentProcessor;
   }

   public IUnwantedContentCleanupProcessor getUnwantedContentCleanupProcessor () {
      return unwantedContentCleanupProcessor;
   }

   public void setUnwantedContentCleanupProcessor (IUnwantedContentCleanupProcessor unwantedContentCleanupProcessor) {
      this.unwantedContentCleanupProcessor = unwantedContentCleanupProcessor;
   }

   public IContentPreProcessorConfigurationService getContentPreProcessorConfigurationService () {
      return contentPreProcessorConfigurationService;
   }

   public void setContentPreProcessorConfigurationService (
            IContentPreProcessorConfigurationService contentPreProcessorConfigurationService) {
      this.contentPreProcessorConfigurationService = contentPreProcessorConfigurationService;
   }
}
