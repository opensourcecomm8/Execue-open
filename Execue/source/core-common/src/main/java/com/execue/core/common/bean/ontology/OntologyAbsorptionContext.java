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


package com.execue.core.common.bean.ontology;

import com.execue.core.common.bean.JobRequestIdentifier;

public class OntologyAbsorptionContext extends JobRequestIdentifier {

   private boolean generateSFLTerms;

   private boolean generateRIOntoterms;

   private Long    modelId;

   private Long    cloudId;

   private Long    userId;

   private String  filePath;

   /**
    * @return the filePath
    */
   public String getFilePath () {
      return filePath;
   }

   /**
    * @param filePath
    *           the filePath to set
    */
   public void setFilePath (String filePath) {
      this.filePath = filePath;
   }

   /**
    * @return the modelId
    */
   public Long getModelId () {
      return modelId;
   }

   /**
    * @param modelId
    *           the modelId to set
    */
   public void setModelId (Long modelId) {
      this.modelId = modelId;
   }

   /**
    * @return the userId
    */
   public Long getUserId () {
      return userId;
   }

   /**
    * @param userId
    *           the userId to set
    */
   public void setUserId (Long userId) {
      this.userId = userId;
   }

   /**
    * @return the generateSFLTerms
    */
   public boolean isGenerateSFLTerms () {
      return generateSFLTerms;
   }

   /**
    * @param generateSFLTerms
    *           the generateSFLTerms to set
    */
   public void setGenerateSFLTerms (boolean generateSFLTerms) {
      this.generateSFLTerms = generateSFLTerms;
   }

   /**
    * @return the generateRIOntoterms
    */
   public boolean isGenerateRIOntoterms () {
      return generateRIOntoterms;
   }

   /**
    * @param generateRIOntoterms
    *           the generateRIOntoterms to set
    */
   public void setGenerateRIOntoterms (boolean generateRIOntoterms) {
      this.generateRIOntoterms = generateRIOntoterms;
   }

   public Long getCloudId () {
      return cloudId;
   }

   public void setCloudId (Long cloudId) {
      this.cloudId = cloudId;
   }

}
