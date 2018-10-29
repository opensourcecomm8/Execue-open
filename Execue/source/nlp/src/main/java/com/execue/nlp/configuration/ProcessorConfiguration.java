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


package com.execue.nlp.configuration;

import java.util.List;
import java.util.Map;

import com.execue.nlp.processor.IProcessor;

/**
 * @author Abhijit
 * @since Oct 14, 2008 : 3:04:43 PM
 */
public class ProcessorConfiguration {

   protected IProcessor          processor;
   protected List<String>        fileNames;
   protected Map<String, String> flags;
   protected String              processorType;

   public IProcessor getProcessor () {
      return processor;
   }

   public void setProcessor (IProcessor processor) {
      this.processor = processor;
   }

   public List<String> getFileNames () {
      return fileNames;
   }

   public void setFileNames (List<String> fileNames) {
      this.fileNames = fileNames;
   }

   public Map<String, String> getFlags () {
      return flags;
   }

   public void setFlags (Map<String, String> flags) {
      this.flags = flags;
   }

   public String getProcessorType () {
      return processorType;
   }

   public void setProcessorType (String processorType) {
      this.processorType = processorType;
   }
}
