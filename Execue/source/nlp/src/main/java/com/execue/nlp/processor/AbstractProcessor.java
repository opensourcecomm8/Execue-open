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


package com.execue.nlp.processor;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.execue.nlp.bean.Group;
import com.execue.nlp.bean.ProcessorInput;
import com.execue.nlp.bean.matrix.Summary;
import com.execue.nlp.configuration.INLPConfigurationService;
import com.execue.swi.service.IBaseKDXRetrievalService;
import com.execue.swi.service.IKDXRetrievalService;

/**
 * @author Kaliki
 * @version 1.0
 */
public abstract class AbstractProcessor implements IProcessor {

   private Logger                   logger = Logger.getLogger(this.getClass());

   private IBaseKDXRetrievalService baseKDXRetrievalService;
   private IKDXRetrievalService     kdxRetrievalService;
   private INLPConfigurationService        nlpConfigurationService;

   public void process (ProcessorInput baseProcessorInput) {
      // TODO: NK: Need to find better way of representing the below implemented processor input
      ProcessorInput implProcessorInput = initializeProcessorInput(baseProcessorInput);
      // TODO -PK- Need to fix below code.Nihar will update on this.
      Group grp = new Group();
      grp.setIterationId(1);
      grp.setPossibilityId(1);
      Map<String, Integer> groupIdMap = new HashMap<String, Integer>();
      grp.setGroupIdMap(groupIdMap);
      implProcessorInput.setGroup(grp);

      execute(implProcessorInput);

      // Abstract method to update the actual processor input with the implemented processor input specific information
      // TODO: NK: Need to find better way of representing the below implemented processor input and base processor
      // input
      updateProcessorInput(baseProcessorInput, implProcessorInput);
   }

   /**
    * Gate keeping logic in performed here.
    * 
    * @param input
    * @return the boolean
    */
   protected abstract boolean isValidForExecution (Summary input);

   /**
    * Initialize the Processor specific ProcessorInput from the generic ProcessorInput object
    * 
    * @param input
    * @return the ProcessorInput
    */
   protected abstract ProcessorInput initializeProcessorInput (ProcessorInput processorInput);

   /**
    * Place where business logic goes. This method should be implemented by every Processor .
    * 
    * @param input
    */
   protected abstract void execute (ProcessorInput input);

   /**
    * Put back the Processor specific ProcessorInput information into the generic ProcessorInput object
    * 
    * @param input
    * @param processorInput
    */
   protected abstract void updateProcessorInput (ProcessorInput input, ProcessorInput processorInput);

   public IBaseKDXRetrievalService getBaseKDXRetrievalService () {
      return baseKDXRetrievalService;
   }

   public void setBaseKDXRetrievalService (IBaseKDXRetrievalService baseKDXRetrievalService) {
      this.baseKDXRetrievalService = baseKDXRetrievalService;
   }

   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }

   public INLPConfigurationService getNlpConfigurationService () {
      return nlpConfigurationService;
   }

   public void setNlpConfigurationService (INLPConfigurationService nlpConfigurationService) {
      this.nlpConfigurationService = nlpConfigurationService;
   }
}
