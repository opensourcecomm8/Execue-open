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


package com.execue.content.postprocessor.bean;

import java.util.List;

import com.execue.core.common.bean.nlp.SemanticPossibility;

/**
 * @author Nitesh
 */
public class PostProcessorInput {

   private Long                      applicationId;
   private List<SemanticPossibility> semanticPossibilities;
   private boolean                   resemantification;

   /**
    * @return the applicationId
    */
   public Long getApplicationId () {
      return applicationId;
   }

   /**
    * @param applicationId the applicationId to set
    */
   public void setApplicationId (Long applicationId) {
      this.applicationId = applicationId;
   }

   /**
    * @return the semanticPossibilities
    */
   public List<SemanticPossibility> getSemanticPossibilities () {
      return semanticPossibilities;
   }

   /**
    * @param semanticPossibilities the semanticPossibilities to set
    */
   public void setSemanticPossibilities (List<SemanticPossibility> semanticPossibilities) {
      this.semanticPossibilities = semanticPossibilities;
   }

   /**
    * @return the resemantification
    */
   public boolean isResemantification () {
      return resemantification;
   }

   /**
    * @param resemantification the resemantification to set
    */
   public void setResemantification (boolean resemantification) {
      this.resemantification = resemantification;
   }
}
