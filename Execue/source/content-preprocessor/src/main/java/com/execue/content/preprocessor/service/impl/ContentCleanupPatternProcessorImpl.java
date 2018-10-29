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

import java.util.List;
import java.util.regex.PatternSyntaxException;

import org.apache.log4j.Logger;

import com.execue.content.preprocessor.exception.ContentPreProcessorException;
import com.execue.content.preprocessor.service.IContentCleanupPatternProcessor;
import com.execue.core.common.bean.ContentCleanupPattern;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.swi.exception.KDXException;
import com.execue.swi.service.IKDXRetrievalService;

/**
 * @author John Mallavalli
 */
public class ContentCleanupPatternProcessorImpl implements IContentCleanupPatternProcessor {

   private static final Logger  logger = Logger.getLogger(ContentCleanupPatternProcessorImpl.class);

   private IKDXRetrievalService kdxRetrievalService;

   @Override
   public String processContent (String input, Long applicationId) throws ContentPreProcessorException {
      if (ExecueCoreUtil.isNotEmpty(input)) {
         return input;
      }
      List<ContentCleanupPattern> contentCleanupPatterns = null;
      try {
         contentCleanupPatterns = getKdxRetrievalService().getContentCleanupPatterns(applicationId);
      } catch (KDXException kdxException) {
         throw new ContentPreProcessorException(kdxException.getCode(), kdxException.getMessage());
      }
      if (ExecueCoreUtil.isCollectionNotEmpty(contentCleanupPatterns)) {
         for (ContentCleanupPattern contentCleanupPattern : contentCleanupPatterns) {
            try {
               input.replaceAll(contentCleanupPattern.getLookupPattern(), contentCleanupPattern.getReplacePattern());
            } catch (PatternSyntaxException patternSyntaxException) {
               // log the regex pattern error and continue with the next pattern
               logger.error(patternSyntaxException.getMessage());
            }
         }
      }
      return input;
   }

   // TODO: -JM- remove later
   // private static String getTrimmedForSpecialChars (String inPut) {
   // String outPut = inPut;
   // outPut = outPut.replaceAll("\\%", " Percentage ");
   // outPut = outPut.replaceAll(",", "");
   // outPut = outPut.replaceAll("'", "");
   // outPut = outPut.replaceAll("\\?", "");
   // outPut = outPut.replaceAll("!", "");
   // outPut = outPut.replaceAll("\"", "");
   // outPut = outPut.replaceAll("Inc\\.", "Inc");
   // outPut = outPut.replaceAll("Co\\.", "Co");
   // outPut = outPut.replaceAll("Ltd\\.", "Ltd");
   // outPut = outPut.replaceAll("\\.com", "com");
   // outPut = outPut.replaceAll("&", "and");
   // return outPut;
   // }

   /**
    * @return the kdxRetrievalService
    */
   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   /**
    * @param kdxRetrievalService the kdxRetrievalService to set
    */
   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }
}
