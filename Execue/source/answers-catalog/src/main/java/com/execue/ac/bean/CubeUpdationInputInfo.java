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


package com.execue.ac.bean;

/**
 * This bean contains the information needed for updating cube.
 * 
 * @author Vishay
 * @version 1.0
 * @since 17/02/2011
 */
public class CubeUpdationInputInfo {

   private CubeUpdationContext                cubeUpdationContext;
   private CubeUpdationPopulatedContext       cubeUpdationPopulatedContext;
   private AnswersCatalogConfigurationContext answersCatalogConfigurationContext;
   private CubeConfigurationContext           cubeConfigurationContext;

   public AnswersCatalogConfigurationContext getAnswersCatalogConfigurationContext () {
      return answersCatalogConfigurationContext;
   }

   public void setAnswersCatalogConfigurationContext (
            AnswersCatalogConfigurationContext answersCatalogConfigurationContext) {
      this.answersCatalogConfigurationContext = answersCatalogConfigurationContext;
   }

   public CubeConfigurationContext getCubeConfigurationContext () {
      return cubeConfigurationContext;
   }

   public void setCubeConfigurationContext (CubeConfigurationContext cubeConfigurationContext) {
      this.cubeConfigurationContext = cubeConfigurationContext;
   }

   public CubeUpdationContext getCubeUpdationContext () {
      return cubeUpdationContext;
   }

   public void setCubeUpdationContext (CubeUpdationContext cubeUpdationContext) {
      this.cubeUpdationContext = cubeUpdationContext;
   }

   public CubeUpdationPopulatedContext getCubeUpdationPopulatedContext () {
      return cubeUpdationPopulatedContext;
   }

   public void setCubeUpdationPopulatedContext (CubeUpdationPopulatedContext cubeUpdationPopulatedContext) {
      this.cubeUpdationPopulatedContext = cubeUpdationPopulatedContext;
   }
}
