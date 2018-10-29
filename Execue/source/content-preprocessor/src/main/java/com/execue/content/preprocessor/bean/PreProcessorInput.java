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


package com.execue.content.preprocessor.bean;

/**
 * @author John Mallavalli
 */
public class PreProcessorInput {

   private Long    applicationId;
   private String  input;
   private boolean title;

   public Long getApplicationId () {
      return applicationId;
   }

   public void setApplicationId (Long applicationId) {
      this.applicationId = applicationId;
   }

   public String getInput () {
      return input;
   }

   public void setInput (String input) {
      this.input = input;
   }

   public boolean isTitle () {
      return title;
   }

   public void setTitle (boolean title) {
      this.title = title;
   }
}
