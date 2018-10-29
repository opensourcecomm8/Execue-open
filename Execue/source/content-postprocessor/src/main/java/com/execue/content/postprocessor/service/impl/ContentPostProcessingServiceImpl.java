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


/**
 * 
 */
package com.execue.content.postprocessor.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.execue.content.postprocessor.bean.PostProcessorInput;
import com.execue.content.postprocessor.bean.PostProcessorOutput;
import com.execue.content.postprocessor.beanfactory.ContentPostProcessingRuleFactory;
import com.execue.content.postprocessor.configuration.IContentPostProcessorConfigurationService;
import com.execue.content.postprocessor.exception.ContentPostProcessorException;
import com.execue.content.postprocessor.service.IContentPostProcessingOptimizationService;
import com.execue.content.postprocessor.service.IContentPostProcessingService;
import com.execue.core.common.bean.nlp.SemanticPossibility;
import com.execue.core.common.bean.sdata.location.LocationPointInfo;
import com.execue.platform.configuration.IPlatformServicesConfigurationService;
import com.execue.sus.helper.ReducedFormHelper;

/**
 * @author Nitesh
 *
 */
public class ContentPostProcessingServiceImpl implements IContentPostProcessingService {

   private static Logger                             logger = Logger.getLogger(ContentPostProcessingServiceImpl.class);
   private IContentPostProcessorConfigurationService contentPostProcessorConfigurationService;
   private IContentPostProcessingOptimizationService contentPostProcessingOptimizationService;
   private IPlatformServicesConfigurationService     platformServicesConfigurationService;

   @Override
   public PostProcessorOutput postProcess (PostProcessorInput postProcessorInput) throws ContentPostProcessorException {
      PostProcessorOutput postProcessorOutput = new PostProcessorOutput();

      Long applicationId = postProcessorInput.getApplicationId();
      List<SemanticPossibility> semanticPossibilities = postProcessorInput.getSemanticPossibilities();
      SemanticPossibility semanticPossibility = ReducedFormHelper.mergeSemanticPossibilities(semanticPossibilities);
      if (logger.isDebugEnabled()) {
         logger.debug("\nReduced Form For Merged Semantic Possibility: \n" + semanticPossibility.getDisplayString());
      }

      boolean isAppWithLocation = getPlatformServicesConfigurationService().isApplicationHasLocationRealization(
               applicationId);

      // Check and optimize the merged possibility based on the valid location information
      if (isAppWithLocation) {
         List<LocationPointInfo> validLocationInfos = getContentPostProcessingOptimizationService()
                  .optimizeGraphForValidLocation(semanticPossibility);
         postProcessorOutput.setLocationPointInfo(validLocationInfos.get(0));
      }
      if (logger.isDebugEnabled()) {
         logger.debug("\nReduced Form For Semantic Possibility after Location Optimization: \n"
                  + semanticPossibility.getDisplayString());
      }

      // Optimize the possibility based on the single instance or multi-instance run time behavior
      getContentPostProcessingOptimizationService().optimizeGraph(semanticPossibility);
      if (logger.isDebugEnabled()) {
         logger.debug("\nReduced Form For Optimized Semantic Possibility: \n" + semanticPossibility.getDisplayString());
      }

      // Apply the app specific rules by calling the factory 
      ContentPostProcessingRuleFactory.getInstance().getContentPostProcessingRuleService(applicationId).processRules(
               semanticPossibility);

      postProcessorOutput.setSemanticPossibility(semanticPossibility);
      return postProcessorOutput;
   }

   public List<SemanticPossibility> updatePossibilities (Long applicationId,
            List<SemanticPossibility> semanticPossibilities) throws ContentPostProcessorException {
      List<SemanticPossibility> newSemanticPossibilities = new ArrayList<SemanticPossibility>(1);
      SemanticPossibility semanticPossibility = ReducedFormHelper.mergeSemanticPossibilities(semanticPossibilities);
      if (logger.isDebugEnabled()) {
         logger.debug("\nGenerating RFX for Merged Semantic Possibility: \n" + semanticPossibility.getDisplayString());
      }

      // Optimize the merged possibility based on the single instance or multi-instance run time behavior
      getContentPostProcessingOptimizationService().optimizeGraph(semanticPossibility);
      if (logger.isDebugEnabled()) {
         logger.debug("\nGenerating RFX for Optimized Semantic Possibility: \n"
                  + semanticPossibility.getDisplayString());
      }

      // Apply the app specific rules by calling the factory 
      ContentPostProcessingRuleFactory.getInstance().getContentPostProcessingRuleService(applicationId).processRules(
               semanticPossibility);

      newSemanticPossibilities.add(semanticPossibility);
      return newSemanticPossibilities;
   }

   /**
    * @return the contentPostProcessorConfigurationService
    */
   public IContentPostProcessorConfigurationService getContentPostProcessorConfigurationService () {
      return contentPostProcessorConfigurationService;
   }

   /**
    * @param contentPostProcessorConfigurationService the contentPostProcessorConfigurationService to set
    */
   public void setContentPostProcessorConfigurationService (
            IContentPostProcessorConfigurationService contentPostProcessorConfigurationService) {
      this.contentPostProcessorConfigurationService = contentPostProcessorConfigurationService;
   }

   /**
    * @return the contentPostProcessingOptimizationService
    */
   public IContentPostProcessingOptimizationService getContentPostProcessingOptimizationService () {
      return contentPostProcessingOptimizationService;
   }

   /**
    * @param contentPostProcessingOptimizationService the contentPostProcessingOptimizationService to set
    */
   public void setContentPostProcessingOptimizationService (
            IContentPostProcessingOptimizationService contentPostProcessingOptimizationService) {
      this.contentPostProcessingOptimizationService = contentPostProcessingOptimizationService;
   }

   /**
    * @return the platformServicesConfigurationService
    */
   public IPlatformServicesConfigurationService getPlatformServicesConfigurationService () {
      return platformServicesConfigurationService;
   }

   /**
    * @param platformServicesConfigurationService the platformServicesConfigurationService to set
    */
   public void setPlatformServicesConfigurationService (
            IPlatformServicesConfigurationService platformServicesConfigurationService) {
      this.platformServicesConfigurationService = platformServicesConfigurationService;
   }
}