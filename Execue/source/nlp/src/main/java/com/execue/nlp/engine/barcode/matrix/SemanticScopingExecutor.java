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
package com.execue.nlp.engine.barcode.matrix;

import java.util.Collection;

import org.apache.log4j.Logger;

import com.execue.nlp.bean.ProcessorInput;
import com.execue.nlp.processor.IProcessor;

/**
 * Class to execute the Semantic Scoping processor over input in a loop.
 * 
 * @author Nihar
 */
public class SemanticScopingExecutor extends AbstractMatrixExecutor {

   private Logger logger = Logger.getLogger(SemanticScopingExecutor.class); ;

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.nlp.engine.barcode.matrix.IMatrixExecutor#executeMatrix(com.execue.nlp.bean.ProcessorInput,
    *      java.lang.String)
    */
   public boolean executeMatrix (ProcessorInput processorInput, String contextName) {
      if (contextName == null) {
         contextName = "Default";
      }
      Collection<IProcessor> pList = getSemanticScopingProcessors(contextName);

      for (IProcessor processor : pList) {
         long start = System.currentTimeMillis();
         if (logger.isDebugEnabled()) {
            logger.debug("Running Processor " + processor.getClass().getName());
         }
         executeProcessor(processor, processorInput);
         long end = System.currentTimeMillis();
         if (logger.isInfoEnabled()) {
            logger.info("Time to Run Processor " + processor.getClass().getName() + " # is " + (end - start));
         }

      }
      return true;
   }

}
