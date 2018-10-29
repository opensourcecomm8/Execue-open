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


package com.execue.nlp.engine.barcode.matrix;

import java.util.Collection;

import com.execue.nlp.bean.ProcessorInput;
import com.execue.nlp.configuration.INLPConfigurationService;
import com.execue.nlp.processor.IProcessor;

/**
 * @author kaliki
 */
public abstract class AbstractMatrixExecutor implements IMatrixExecutor {

   private INLPConfigurationService   nlpConfigurationService;

   public INLPConfigurationService getNlpConfigurationService () {
      return nlpConfigurationService;
   }

   public void setNlpConfigurationService (INLPConfigurationService nlpConfigurationService) {
      this.nlpConfigurationService = nlpConfigurationService;
   }
   protected Collection<IProcessor> getProcessors () {
      return nlpConfigurationService.getProcessors();
   }
   protected Collection<IProcessor> getProcessors (String contextName) {
	      return nlpConfigurationService.getProcessors(contextName);
   }

   protected void executeProcessor (IProcessor processor, ProcessorInput processorInput) {
      processor.process(processorInput);
   }
   /**
    * Method to get The list of Processors used in semantic Scoping
    * @param contextName
    * @return
    */
   protected Collection<IProcessor> getSemanticScopingProcessors (String contextName) {
      return nlpConfigurationService.getSemanticScopingProcessors(contextName);
   }
}
